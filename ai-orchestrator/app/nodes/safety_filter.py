"""Safety Filter Node - Filters prohibited expressions from AI outputs."""

import re

PROHIBITED_PATTERNS = [
    # Diagnostic assertions
    r"진단[은는이가]\s",
    r"(확실히|분명히|틀림없이)\s*(.*?)(입니다|합니다|이에요)",
    # Treatment recommendations
    r"(처방|투약|복용)(하세요|하십시오|해야|바랍니다)",
    r"(약을|약을\s)(드세요|먹으세요|복용하세요)",
    # Specific facility recommendations (anti-recommendation rule)
    r"(병원|약국|의원)을?\s*(추천|권유|소개)",
    r"(가까운|근처)\s*(병원|약국|의원)에?\s*(가세요|방문)",
]

DISCLAIMER = "\n\n[본 내용은 AI가 생성한 초안이며, 의료적 판단이나 진단이 아닙니다. 반드시 담당 의료인의 검토를 거쳐야 합니다.]"


def safety_filter(state: dict) -> dict:
    """Filter prohibited expressions and add disclaimers."""
    content = state.get("output_content", "")
    violations = []

    for pattern in PROHIBITED_PATTERNS:
        matches = re.findall(pattern, content)
        if matches:
            violations.extend([str(m) for m in matches])
            content = re.sub(pattern, "[표현 필터링됨]", content)

    content += DISCLAIMER

    return {
        **state,
        "output_content": content,
        "safety_violations": violations,
        "safety_filtered": len(violations) > 0,
    }
