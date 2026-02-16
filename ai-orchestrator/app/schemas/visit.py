from pydantic import BaseModel
from typing import Optional


class VisitSummaryInput(BaseModel):
    appointment_id: str
    doctor_note: str
    chat_log: Optional[str] = None


class VisitSummaryOutput(BaseModel):
    appointment_id: str
    patient_summary: str
    precautions: list[str]
    safety_flags: dict
    citations: list[str] = []
