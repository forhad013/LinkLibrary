#!/usr/bin/env python3
"""
Lightweight Work Diary Updater - Token Efficient Implementation

This script provides token-efficient work diary updates by processing
user-provided summaries instead of automatic comprehensive analysis.

Token Cost: ~70 tokens per daily update (vs 10,000+ for automatic)
"""

import os
import re
from datetime import datetime, timedelta
from pathlib import Path

class LightweightDiaryUpdater:
    """Token-efficient work diary updater using user-driven summaries."""

    def __init__(self, project_path):
        """
        Initialize the diary updater.

        Args:
            project_path: Path to the LinkLibrary project
        """
        self.project_path = Path(project_path)
        self.diary_path = self.project_path / "WORK_DIARY.md"
        self.template_path = self.project_path / "WORK_DIARY_TEMPLATE.md"

    def quick_update(self, user_summary, date=None):
        """
        Quick diary update from user summary.

        Args:
            user_summary: Brief user summary (50-100 words)
            date: Date for the entry (defaults to today)

        Returns:
            str: Formatted diary entry

        Token Cost: ~70 tokens
        """
        if date is None:
            date = datetime.now().strftime("%B %d, %Y")

        # Parse user summary into components (lightweight processing)
        components = self.parse_summary(user_summary)

        # Format using template (minimal tokens)
        entry = self.format_entry(components, date)

        # Update diary (no re-analysis)
        self.add_entry_to_diary(entry)

        return entry

    def parse_summary(self, user_summary):
        """
        Parse user summary into components.

        Args:
            user_summary: User-provided summary text

        Returns:
            dict: Parsed components

        Token Cost: ~20 tokens
        """
        components = {
            "achievement": "",
            "tasks": [],
            "files": [],
            "claude_usage": [],
            "next_priority": ""
        }

        lines = user_summary.strip().split(',')

        # Extract main achievement (first item)
        if lines:
            components["achievement"] = lines[0].strip()

        # Extract tasks (middle items)
        for line in lines[1:-1]:
            line = line.strip()
            if line and not any(word in line.lower() for word in ["file", "claude", "next", "tomorrow"]):
                components["tasks"].append(line)

        # Extract files (look for file mentions)
        for line in lines:
            if any(word in line.lower() for word in ["file", "created", "plugin", "implementation"]):
                components["files"].append(line.strip())

        # Extract Claude usage (look for tool mentions)
        for line in lines:
            if any(word in line.lower() for word in ["claude", "task", "search", "write"]):
                components["claude_usage"].append(line.strip())

        # Extract next priority (last item)
        if len(lines) > 1:
            last_line = lines[-1].strip().lower()
            if any(word in last_line for word in ["next", "tomorrow", "then", "plan"]):
                components["next_priority"] = lines[-1].strip()

        return components

    def format_entry(self, components, date):
        """
        Format diary entry from components.

        Args:
            components: Parsed summary components
            date: Entry date

        Returns:
            str: Formatted entry

        Token Cost: ~30 tokens
        """
        entry = f"""### {date} - Quick Daily Update

**Main Achievement:** {components['achievement']}

**Key Tasks:**
"""

        for task in components['tasks']:
            entry += f"- ✅ {task}\n"

        if components['files']:
            entry += "\n**Files Created/Modified:**\n"
            for file in components['files']:
                entry += f"- {file}\n"

        if components['claude_usage']:
            entry += "\n**Claude Usage:**\n"
            for usage in components['claude_usage']:
                entry += f"- {usage}\n"

        if components['next_priority']:
            entry += f"\n**Next Priority:** {components['next_priority']}\n"

        return entry

    def add_entry_to_diary(self, entry):
        """
        Add entry to diary without reprocessing existing content.

        Args:
            entry: Formatted entry to add

        Token Cost: ~20 tokens
        """
        # Read existing diary
        if self.diary_path.exists():
            with open(self.diary_path, 'r') as f:
                content = f.read()
        else:
            content = "# LinkLibrary Work Diary\n\n## 📅 Daily Work Log\n\n"

        # Find insertion point (after daily work log header)
        insertion_point = content.find("## 📅 Daily Work Log")
        if insertion_point == -1:
            insertion_point = content.find("## 📊")
        if insertion_point == -1:
            insertion_point = len(content)

        # Find end of section to insert after
        section_end = content.find("\n\n", insertion_point)
        if section_end == -1:
            section_end = insertion_point + len("## 📅 Daily Work Log")

        # Insert new entry
        updated_content = content[:section_end] + "\n\n" + entry + content[section_end:]

        # Write updated diary
        with open(self.diary_path, 'w') as f:
            f.write(updated_content)

    def weekly_summary_from_diary(self):
        """
        Generate weekly summary from existing diary entries.

        Returns:
            str: Weekly summary

        Token Cost: ~450 tokens (reads existing entries only)
        """
        # Read existing diary
        if not self.diary_path.exists():
            return "No diary entries found."

        with open(self.diary_path, 'r') as f:
            content = f.read()

        # Extract daily entries from content (lightweight parsing)
        daily_entries = self.extract_daily_entries(content)

        if not daily_entries:
            return "No daily entries found for this week."

        # Generate summary from existing entries
        summary = self.generate_weekly_summary(daily_entries)

        return summary

    def extract_daily_entries(self, content):
        """
        Extract daily entries from existing diary content.

        Args:
            content: Existing diary content

        Returns:
            list: Daily entries found

        Token Cost: ~200 tokens
        """
        entries = []
        # Simple pattern matching for daily entries
        pattern = r"### ([A-Z][a-z]+ \d+, \d{4}) - .*?\n(.*?)(?=###|\Z)"
        matches = re.findall(pattern, content, re.DOTALL)

        for date, entry_content in matches:
            entries.append({
                "date": date,
                "content": entry_content.strip()
            })

        return entries

    def generate_weekly_summary(self, daily_entries):
        """
        Generate weekly summary from daily entries.

        Args:
            daily_entries: List of daily entries

        Returns:
            str: Formatted weekly summary

        Token Cost: ~200 tokens
        """
        # Get week range
        if daily_entries:
            latest_date = datetime.strptime(daily_entries[0]['date'], "%B %d, %Y")
            week_start = (latest_date - timedelta(days=latest_date.weekday())).strftime("%B %d")
            week_end = (latest_date + timedelta(days=6-latest_date.weekday())).strftime("%B %d, %Y")
        else:
            week_start = "This week"
            week_end = datetime.now().strftime("%B %d, %Y")

        summary = f"""### Week Summary: {week_start} - {week_end}

**Major Achievements:**
"""

        # Extract achievements from daily entries
        achievement_count = 0
        for entry in daily_entries[:7]:  # Last 7 entries
            # Look for achievement line
            achievement_match = re.search(r"Main Achievement: (.*?)\n", entry['content'])
            if achievement_match and achievement_count < 5:  # Top 5 achievements
                summary += f"- ✅ {achievement_match.group(1).strip()}\n"
                achievement_count += 1

        summary += f"\n**Total Development Days:** {len(daily_entries)}\n"
        summary += "**Claude Sessions:** Documented in daily entries\n"
        summary += "**Files Created:** See individual daily entries\n"

        return summary


def main():
    """Main function for command-line usage."""
    import sys

    if len(sys.argv) < 2:
        print("Usage: python lightweight-diary-updater.py <command> [args]")
        print("Commands:")
        print("  update <summary>  - Quick daily update from summary")
        print("  weekly             - Generate weekly summary from existing entries")
        return

    command = sys.argv[1]
    project_path = "/Volumes/Projects/KMP project/LinkLibrary"
    updater = LightweightDiaryUpdater(project_path)

    if command == "update":
        if len(sys.argv) < 3:
            print("Please provide a summary: update 'your summary here'")
            return

        summary = sys.argv[2]
        entry = updater.quick_update(summary)
        print("✅ Diary updated successfully!")
        print("\nGenerated Entry:")
        print(entry)

    elif command == "weekly":
        summary = updater.weekly_summary_from_diary()
        print("✅ Weekly summary generated!")
        print("\nSummary:")
        print(summary)

    else:
        print(f"Unknown command: {command}")


if __name__ == "__main__":
    main()