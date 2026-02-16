from pydantic import BaseModel
from typing import Optional


class IntakeSummaryInput(BaseModel):
    intake_id: str
    patient_free_text: str
    checklist: Optional[dict] = None
    med_history: Optional[dict] = None
    attachments: Optional[list] = None


class DoctorSummary(BaseModel):
    chief_complaint: str
    timeline: str
    associated: list[str]
    meds: list[str]
    red_flags: list[str]
    questions: list[str]


class SafetyFlags(BaseModel):
    emergency_hint: bool = False
    pii_found: bool = False


class IntakeSummaryOutput(BaseModel):
    intake_id: str
    doctor_summary: DoctorSummary
    safety_flags: SafetyFlags
    citations: list[str] = []
