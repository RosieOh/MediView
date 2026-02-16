"""Red Flag Detection Node - Detects emergency indicators in patient symptoms."""

import re

EMERGENCY_KEYWORDS = [
    "호흡곤란", "흉통", "의식저하", "의식불명", "심한출혈", "경련",
    "마비", "심정지", "쇼크", "호흡정지", "대량출혈", "고열",
    "breathlessness", "chest pain", "loss of consciousness",
    "seizure", "paralysis", "severe bleeding", "cardiac arrest",
]

EMERGENCY_PATTERN = re.compile("|".join(EMERGENCY_KEYWORDS), re.IGNORECASE)


def red_flag_detect(state: dict) -> dict:
    """Detect emergency red flags in patient text."""
    text = state.get("patient_free_text", "")
    structured = state.get("structured_data", "")
    combined = f"{text} {structured}"

    matches = EMERGENCY_PATTERN.findall(combined)
    red_flags = list(set(matches))
    emergency_hint = len(red_flags) > 0

    return {
        **state,
        "red_flags": red_flags,
        "emergency_hint": emergency_hint,
    }
