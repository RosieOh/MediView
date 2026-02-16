from pydantic import BaseModel
from typing import Optional


class CsAnswerInput(BaseModel):
    ticket_id: Optional[str] = None
    question: str
    category: Optional[str] = None


class CsAnswerOutput(BaseModel):
    ticket_id: Optional[str] = None
    answer: str
    sources: list[str] = []
    safety_flags: dict = {}
