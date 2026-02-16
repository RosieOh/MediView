"""MediView AI Orchestrator - FastAPI service with LangGraph workflows."""

import os
import json
from contextlib import asynccontextmanager

from dotenv import load_dotenv
from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
from typing import Optional

load_dotenv()

from app.graphs.intake_summary import intake_summary_graph
from app.graphs.visit_summary import visit_summary_graph
from app.graphs.cs_answer import cs_answer_graph
from app.rag.retriever import ingest_documents
from app.schemas.intake import IntakeSummaryInput, IntakeSummaryOutput, DoctorSummary, SafetyFlags
from app.schemas.visit import VisitSummaryInput, VisitSummaryOutput
from app.schemas.cs import CsAnswerInput, CsAnswerOutput


@asynccontextmanager
async def lifespan(app: FastAPI):
    print("AI Orchestrator starting...")
    yield
    print("AI Orchestrator shutting down...")


app = FastAPI(
    title="MediView AI Orchestrator",
    description="LangGraph-based AI service for telehealth platform",
    version="1.0.0",
    lifespan=lifespan,
)


@app.get("/health")
async def health():
    return {"status": "ok", "service": "ai-orchestrator"}


@app.post("/api/ai/intake-summary", response_model=IntakeSummaryOutput)
async def intake_summary(request: IntakeSummaryInput):
    """Graph A: Process patient intake and create structured summary for doctor."""
    try:
        state = {
            "intake_id": request.intake_id,
            "patient_free_text": request.patient_free_text,
            "checklist": request.checklist or {},
            "med_history": request.med_history or {},
            "attachments": request.attachments or [],
            "pii_found": False,
            "structured_data": "",
            "red_flags": [],
            "emergency_hint": False,
            "suggested_questions": "[]",
            "doctor_summary": "",
        }

        result = intake_summary_graph.invoke(state)

        # Parse the doctor summary JSON
        try:
            summary_data = json.loads(result["doctor_summary"])
        except (json.JSONDecodeError, TypeError):
            summary_data = {
                "chief_complaint": result.get("doctor_summary", ""),
                "timeline": "",
                "associated": [],
                "meds": [],
                "red_flags": result.get("red_flags", []),
                "questions": [],
            }

        return IntakeSummaryOutput(
            intake_id=request.intake_id,
            doctor_summary=DoctorSummary(
                chief_complaint=summary_data.get("chief_complaint", ""),
                timeline=summary_data.get("timeline", ""),
                associated=summary_data.get("associated", []),
                meds=summary_data.get("meds", []),
                red_flags=summary_data.get("red_flags", result.get("red_flags", [])),
                questions=summary_data.get("questions", []),
            ),
            safety_flags=SafetyFlags(
                emergency_hint=result.get("emergency_hint", False),
                pii_found=result.get("pii_found", False),
            ),
            citations=[],
        )
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Intake summary failed: {str(e)}")


@app.post("/api/ai/visit-summary", response_model=VisitSummaryOutput)
async def visit_summary(request: VisitSummaryInput):
    """Graph B: Create patient-friendly visit summary."""
    try:
        state = {
            "appointment_id": request.appointment_id,
            "doctor_note": request.doctor_note,
            "chat_log": request.chat_log or "",
            "output_content": "",
            "precautions": [],
            "safety_violations": [],
            "safety_filtered": False,
        }

        result = visit_summary_graph.invoke(state)

        return VisitSummaryOutput(
            appointment_id=request.appointment_id,
            patient_summary=result.get("output_content", ""),
            precautions=result.get("precautions", []),
            safety_flags={
                "filtered": result.get("safety_filtered", False),
                "violations": result.get("safety_violations", []),
            },
            citations=[],
        )
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Visit summary failed: {str(e)}")


@app.post("/api/ai/cs-answer", response_model=CsAnswerOutput)
async def cs_answer(request: CsAnswerInput):
    """Graph C: Answer CS queries using policy RAG."""
    try:
        state = {
            "ticket_id": request.ticket_id,
            "question": request.question,
            "category": request.category,
            "retrieved_docs": "",
            "output_content": "",
            "sources": [],
            "safety_violations": [],
            "safety_filtered": False,
        }

        result = cs_answer_graph.invoke(state)

        return CsAnswerOutput(
            ticket_id=request.ticket_id,
            answer=result.get("output_content", ""),
            sources=result.get("sources", []),
            safety_flags={
                "filtered": result.get("safety_filtered", False),
                "violations": result.get("safety_violations", []),
            },
        )
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"CS answer failed: {str(e)}")


class IngestRequest(BaseModel):
    documents: list[dict]


@app.post("/api/rag/ingest")
async def rag_ingest(request: IngestRequest):
    """Ingest documents into the RAG vector store."""
    try:
        count = ingest_documents(request.documents)
        return {"status": "ok", "chunks_ingested": count}
    except Exception as e:
        raise HTTPException(status_code=500, detail=f"Ingestion failed: {str(e)}")


@app.get("/api/ai/jobs/{job_id}")
async def get_job_status(job_id: str):
    """Get AI job status (proxied from core-api or cached)."""
    return {
        "job_id": job_id,
        "status": "completed",
        "message": "Job status should be queried from core-api",
    }
