"""Format for Doctor Node - Formats the final summary for the doctor."""

from langchain_openai import ChatOpenAI
from langchain_core.prompts import ChatPromptTemplate


FORMAT_PROMPT = ChatPromptTemplate.from_messages([
    ("system", """You are formatting a patient intake summary for a doctor in a telehealth platform.

Create a concise, structured summary in Korean with these sections:
- 주호소 (Chief Complaint)
- 경과 (Timeline)
- 동반증상 (Associated Symptoms)
- 복용약/기저질환 (Medications/History)
- 위험신호 (Red Flags) - if any
- 추가 확인 질문 (Suggested Questions)

Mark this clearly as "AI 생성 초안 - 의료인 검토 필요" at the top.

Output as JSON:
{{"chief_complaint": "...", "timeline": "...", "associated": [...], "meds": [...], "red_flags": [...], "questions": [...]}}"""),
    ("human", "Structured data:\n{structured_data}\n\nRed flags: {red_flags}\n\nSuggested questions: {questions}")
])


def format_for_doctor(state: dict) -> dict:
    """Format the final doctor summary."""
    llm = ChatOpenAI(model="gpt-4o-mini", temperature=0)
    chain = FORMAT_PROMPT | llm

    result = chain.invoke({
        "structured_data": state.get("structured_data", ""),
        "red_flags": str(state.get("red_flags", [])),
        "questions": state.get("suggested_questions", "[]"),
    })

    return {**state, "doctor_summary": result.content}
