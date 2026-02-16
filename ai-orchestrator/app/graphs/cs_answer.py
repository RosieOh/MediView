"""Graph C: CS Policy RAG - Answers customer service queries using policy documents."""

from langgraph.graph import StateGraph, END
from langchain_openai import ChatOpenAI
from langchain_core.prompts import ChatPromptTemplate
from typing import TypedDict, Optional

from app.nodes.safety_filter import safety_filter
from app.rag.retriever import get_policy_retriever


class CsState(TypedDict):
    ticket_id: Optional[str]
    question: str
    category: Optional[str]
    retrieved_docs: str
    output_content: str
    sources: list
    safety_violations: list
    safety_filtered: bool


def classify_query(state: dict) -> dict:
    """Classify the CS query into a category."""
    question = state.get("question", "")
    categories = {
        "환불": ["환불", "취소", "반환", "refund"],
        "예약": ["예약", "변경", "일정", "스케줄", "appointment"],
        "개인정보": ["개인정보", "삭제", "탈퇴", "privacy"],
        "기술": ["오류", "에러", "접속", "bug", "error"],
    }

    detected = "일반"
    for cat, keywords in categories.items():
        if any(kw in question.lower() for kw in keywords):
            detected = cat
            break

    return {**state, "category": detected}


def retrieve_policy(state: dict) -> dict:
    """Retrieve relevant policy documents via RAG."""
    retriever = get_policy_retriever()
    question = state.get("question", "")

    if retriever:
        docs = retriever.invoke(question)
        doc_texts = "\n\n".join([d.page_content for d in docs])
        sources = [d.metadata.get("source", "unknown") for d in docs]
    else:
        doc_texts = "[RAG not configured - using fallback]"
        sources = []

    return {**state, "retrieved_docs": doc_texts, "sources": sources}


CS_ANSWER_PROMPT = ChatPromptTemplate.from_messages([
    ("system", """You are a customer service assistant for a telehealth platform (MediView).
Answer the user's question based ONLY on the provided policy documents.

Rules:
- Only answer based on provided policy documents
- Do NOT provide medical advice or recommendations
- Do NOT recommend specific hospitals or pharmacies
- If the answer is not in the documents, say "담당자에게 문의를 전달하겠습니다"
- Answer in Korean
- Be polite and professional"""),
    ("human", "Category: {category}\nQuestion: {question}\n\nPolicy documents:\n{retrieved_docs}")
])


def generate_answer(state: dict) -> dict:
    """Generate CS answer based on policy RAG."""
    llm = ChatOpenAI(model="gpt-4o-mini", temperature=0)
    chain = CS_ANSWER_PROMPT | llm

    result = chain.invoke({
        "category": state.get("category", "일반"),
        "question": state.get("question", ""),
        "retrieved_docs": state.get("retrieved_docs", ""),
    })

    return {**state, "output_content": result.content}


def build_cs_answer_graph():
    """Build the CS answer LangGraph."""
    graph = StateGraph(CsState)

    graph.add_node("classify", classify_query)
    graph.add_node("retrieve", retrieve_policy)
    graph.add_node("generate", generate_answer)
    graph.add_node("safety_filter", safety_filter)

    graph.set_entry_point("classify")
    graph.add_edge("classify", "retrieve")
    graph.add_edge("retrieve", "generate")
    graph.add_edge("generate", "safety_filter")
    graph.add_edge("safety_filter", END)

    return graph.compile()


cs_answer_graph = build_cs_answer_graph()
