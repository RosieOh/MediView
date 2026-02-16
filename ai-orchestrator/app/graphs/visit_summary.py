"""Graph B: Visit Summary - Creates patient-friendly post-visit summary."""

from langgraph.graph import StateGraph, END
from langchain_openai import ChatOpenAI
from langchain_core.prompts import ChatPromptTemplate
from typing import TypedDict, Optional

from app.nodes.safety_filter import safety_filter


class VisitState(TypedDict):
    appointment_id: str
    doctor_note: str
    chat_log: Optional[str]
    output_content: str
    precautions: list
    safety_violations: list
    safety_filtered: bool


SIMPLIFY_PROMPT = ChatPromptTemplate.from_messages([
    ("system", """You are creating a patient-friendly visit summary for a telehealth platform.
Convert the doctor's notes into easy-to-understand Korean language.

Rules:
- Only state CONFIRMED facts from the doctor's notes
- Do NOT add inferences, diagnoses, or treatment recommendations
- Use simple, non-medical language where possible
- Include any precautions mentioned by the doctor
- Mark as "AI 생성 초안 - 의료인 승인 전 문서"

Output JSON: {{"summary": "...", "precautions": [...]}}"""),
    ("human", "Doctor note:\n{doctor_note}\n\nChat log (if any):\n{chat_log}")
])


def simplify_for_patient(state: dict) -> dict:
    """Convert doctor notes to patient-friendly summary."""
    llm = ChatOpenAI(model="gpt-4o-mini", temperature=0)
    chain = SIMPLIFY_PROMPT | llm

    result = chain.invoke({
        "doctor_note": state.get("doctor_note", ""),
        "chat_log": state.get("chat_log", ""),
    })

    return {**state, "output_content": result.content, "precautions": []}


def build_visit_summary_graph():
    """Build the visit summary LangGraph."""
    graph = StateGraph(VisitState)

    graph.add_node("simplify", simplify_for_patient)
    graph.add_node("safety_filter", safety_filter)

    graph.set_entry_point("simplify")
    graph.add_edge("simplify", "safety_filter")
    graph.add_edge("safety_filter", END)

    return graph.compile()


visit_summary_graph = build_visit_summary_graph()
