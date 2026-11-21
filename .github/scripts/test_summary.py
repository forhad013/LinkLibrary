"""JUnit XML test results -> Markdown for the unit test PR comment.

Usage: python3 test_summary.py <test-results dir>... > test-comment.md
"""
import glob
import os
import sys
import xml.etree.ElementTree as ET

MAX_FAILURES = 30  # PR comments cap out at 64 KB; the test-report artifact has everything
PASS_GIF = os.environ.get("TESTS_PASS_GIF")


def suites(dirs):
    for d in dirs:
        for path in sorted(glob.glob(os.path.join(d, "**", "TEST-*.xml"), recursive=True)):
            yield ET.parse(path).getroot()


def main():
    total = skipped = 0
    failures = []
    for suite in suites(sys.argv[1:]):
        for case in suite.iter("testcase"):
            total += 1
            if case.find("skipped") is not None:
                skipped += 1
            problem = case.find("failure")
            if problem is None:
                problem = case.find("error")
            if problem is not None:
                message = (problem.get("message") or problem.text or "").strip().splitlines()
                failures.append((case.get("classname"), case.get("name"), message[0] if message else ""))

    if total == 0:
        print("## Unit tests ❌\n\nNo test results found; see the \"Unit tests\" step log.")
        return
    passed = total - skipped - len(failures)
    counts = f"**{passed}** passed, **{len(failures)}** failed, **{skipped}** skipped"
    if not failures:
        print(f"## Unit tests ✅\n\n{counts}. All green!")
        if PASS_GIF:
            print(f"\n![All green]({PASS_GIF})")
        return
    print("## Unit tests ❌\n")
    print(f"{counts}. Failing tests block the merge; fix them and push, this comment updates itself.\n")
    print("| Test | Message |")
    print("|---|---|")
    for cls, name, message in failures[:MAX_FAILURES]:
        short = cls.rsplit(".", 1)[-1] if cls else "?"
        print(f"| `{short}.{name}` | {message[:200].replace('|', chr(92) + '|')} |")
    if len(failures) > MAX_FAILURES:
        print(f"\n…and {len(failures) - MAX_FAILURES} more in the `test-report` artifact.")


if __name__ == "__main__":
    main()
