"""PII Redaction Node - Masks personal identifiable information."""

import re
from typing import TypedDict


class PiiState(TypedDict):
    text: str
    pii_found: bool


# Korean resident registration number pattern
RRN_PATTERN = re.compile(r'\d{6}[-\s]?\d{7}')
# Phone patterns
PHONE_PATTERN = re.compile(r'0\d{1,2}[-\s]?\d{3,4}[-\s]?\d{4}')
# Email pattern
EMAIL_PATTERN = re.compile(r'[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}')
# Address keywords (Korean)
ADDRESS_PATTERN = re.compile(r'\b\d{5}\b')  # Korean postal code


def redact_pii(state: dict) -> dict:
    """Redact PII from text content."""
    text = state.get("patient_free_text", "")
    pii_found = False

    if RRN_PATTERN.search(text):
        text = RRN_PATTERN.sub("[주민번호 마스킹]", text)
        pii_found = True

    if PHONE_PATTERN.search(text):
        text = PHONE_PATTERN.sub("[연락처 마스킹]", text)
        pii_found = True

    if EMAIL_PATTERN.search(text):
        text = EMAIL_PATTERN.sub("[이메일 마스킹]", text)
        pii_found = True

    if ADDRESS_PATTERN.search(text):
        text = ADDRESS_PATTERN.sub("[우편번호 마스킹]", text)
        pii_found = True

    return {**state, "patient_free_text": text, "pii_found": pii_found}
