"""Graph A: Intake Summary - Structures patient intake for the doctor."""

from langgraph.graph import StateGraph, END
from typing import TypedDict, Optional

from app.nodes.pii_redact import redact_pii
from app.nodes.structure_extract import structure_extract
from app.nodes.red_flag_detect import red_flag_detect
from app.nodes.question_suggest import question_suggest
from app.nodes.format_doctor import format_for_doctor


class IntakeState(TypedDict):
    intake_id: str
    patient_free_text: str
    checklist: Optional[dict]
    med_history: Optional[dict]
    attachments: Optional[list]
    pii_found: bool
    structured_data: str
    red_flags: list
    emergency_hint: bool
    suggested_questions: str
    doctor_summary: str


def build_intake_summary_graph():
    """Build the intake summary LangGraph."""
    graph = StateGraph(IntakeState)

    graph.add_node("pii_redact", redact_pii)
    graph.add_node("structure_extract", structure_extract)
    graph.add_node("red_flag_detect", red_flag_detect)
    graph.add_node("question_suggest", question_suggest)
    graph.add_node("format_for_doctor", format_for_doctor)

    graph.set_entry_point("pii_redact")
    graph.add_edge("pii_redact", "structure_extract")
    graph.add_edge("structure_extract", "red_flag_detect")
    graph.add_edge("red_flag_detect", "question_suggest")
    graph.add_edge("question_suggest", "format_for_doctor")
    graph.add_edge("format_for_doctor", END)

    return graph.compile()


intake_summary_graph = build_intake_summary_graph()
