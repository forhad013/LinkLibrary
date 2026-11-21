"""Android Lint XML reports -> Markdown for the GitHub Actions run summary.

Usage: python3 lint_summary.py [--brief] <repo root> <lint-results.xml>... >> "$GITHUB_STEP_SUMMARY"
--brief: counts per rule instead of every finding (for the PR comment)
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
    args = sys.argv[1:]
    brief = args[:1] == ["--brief"]
    if brief:
        args = args[1:]
    root, paths = args[0], args[1:]
    found = sorted(set(rows(root, paths)), key=lambda r: (ORDER.get(r[0], 9), r[1], r[2]))
    counts = collections.Counter(r[0] for r in found)
    print("## Android Lint")
    if not found:
        print("No findings.")
        return
    print(", ".join(f"**{counts[s]}** {s.lower()}" for s in sorted(counts, key=lambda s: ORDER.get(s, 9))))
    print()
    if brief:
        per_rule = collections.Counter((r[0], r[1]) for r in found)  # found is already in severity order
        print("| Severity | Rule | Count |")
        print("|---|---|---|")
        for (severity, rule), n in per_rule.items():
            print(f"| {severity} | `{rule}` | {n} |")
        return
    print("| Severity | Rule | Where | Message |")
    print("|---|---|---|---|")
    for severity, rule, where, message in found[:MAX_ROWS]:
        print(f"| {severity} | `{rule}` | `{where}` | {message} |")
    if len(found) > MAX_ROWS:
        print(f"\n…and {len(found) - MAX_ROWS} more in the `lint-report` artifact.")


if __name__ == "__main__":
    main()
