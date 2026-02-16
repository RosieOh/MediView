"""Structure Extraction Node - Extracts structured symptoms from free text."""

from langchain_openai import ChatOpenAI
from langchain_core.prompts import ChatPromptTemplate


EXTRACT_PROMPT = ChatPromptTemplate.from_messages([
    ("system", """You are a medical intake structuring assistant for a telehealth platform.
You do NOT diagnose or recommend treatment. You only organize patient-provided information.

Given the patient's free text description and checklist, extract:
1. Chief complaint (주호소)
2. Timeline (증상 기간/경과)
3. Associated symptoms (동반 증상)
4. Current medications (복용약)
5. Relevant medical history (기저질환)

Output in JSON format:
{{"chief_complaint": "...", "timeline": "...", "associated": [...], "meds": [...], "history": [...]}}

IMPORTANT: Only state what the patient explicitly mentioned. Do NOT infer or assume."""),
    ("human", "Patient description: {text}\n\nChecklist data: {checklist}\n\nMedical history: {med_history}")
])


def structure_extract(state: dict) -> dict:
    """Extract structured information from patient free text."""
    llm = ChatOpenAI(model="gpt-4o-mini", temperature=0)
    chain = EXTRACT_PROMPT | llm

    result = chain.invoke({
        "text": state.get("patient_free_text", ""),
        "checklist": str(state.get("checklist", {})),
        "med_history": str(state.get("med_history", {})),
    })

    return {**state, "structured_data": result.content}
