"""Android Lint XML reports -> Markdown for the GitHub Actions run summary.

Usage: python3 lint_summary.py <repo root> <lint-results.xml>... >> "$GITHUB_STEP_SUMMARY"
"""
import collections
import os
import sys
import xml.etree.ElementTree as ET

MAX_ROWS = 150  # the summary page caps out at 1 MB; the HTML artifact has everything
ORDER = {"Fatal": 0, "Error": 1, "Warning": 2, "Information": 3}


def rows(root, paths):
    for path in paths:
        if not os.path.exists(path):
            continue
        for issue in ET.parse(path).getroot().iter("issue"):
            loc = issue.find("location")
            file = os.path.relpath(loc.get("file"), root) if loc is not None else "?"
            line = loc.get("line") if loc is not None else None
            yield (
                issue.get("severity"),
                issue.get("id"),
                f"{file}:{line}" if line else file,
                issue.get("message", "").replace("|", "\\|").replace("\n", " "),
            )


def main():
    root, paths = sys.argv[1], sys.argv[2:]
    found = sorted(set(rows(root, paths)), key=lambda r: (ORDER.get(r[0], 9), r[1], r[2]))
    counts = collections.Counter(r[0] for r in found)
    print("## Android Lint")
    if not found:
        print("No findings.")
        return
    print(", ".join(f"**{counts[s]}** {s.lower()}" for s in sorted(counts, key=lambda s: ORDER.get(s, 9))))
    print()
    print("| Severity | Rule | Where | Message |")
    print("|---|---|---|---|")
    for severity, rule, where, message in found[:MAX_ROWS]:
        print(f"| {severity} | `{rule}` | `{where}` | {message} |")
    if len(found) > MAX_ROWS:
        print(f"\n…and {len(found) - MAX_ROWS} more in the `lint-report` artifact.")


if __name__ == "__main__":
    main()
