"""Question Suggestion Node - Suggests follow-up questions for the doctor."""

from langchain_openai import ChatOpenAI
from langchain_core.prompts import ChatPromptTemplate


QUESTION_PROMPT = ChatPromptTemplate.from_messages([
    ("system", """You are an assistant helping doctors prepare for telehealth consultations.
Based on the structured patient intake data, suggest 3-5 follow-up questions the doctor
should ask during the consultation.

Rules:
- Do NOT suggest diagnoses or treatment recommendations
- Do NOT recommend specific medications
- Focus on gathering additional clinical information
- Questions should be in Korean

Output as a JSON array of strings."""),
    ("human", "Structured intake data:\n{structured_data}\n\nRed flags: {red_flags}")
])


def question_suggest(state: dict) -> dict:
    """Suggest follow-up questions for the doctor."""
    llm = ChatOpenAI(model="gpt-4o-mini", temperature=0.3)
    chain = QUESTION_PROMPT | llm

    result = chain.invoke({
        "structured_data": state.get("structured_data", ""),
        "red_flags": str(state.get("red_flags", [])),
    })

    return {**state, "suggested_questions": result.content}
