# Work Diary Automator Skill

**Purpose:** Automatically maintain and update the LinkLibrary project work diary with development activities, Claude usage, and progress tracking.

**Usage:** `/work-diary-update` or invoke this skill when you want to update your work diary after development sessions.

---

## Overview

This skill automates the work diary maintenance process by:
- Parsing your development activities and extracting key information
- Formatting diary entries with proper structure
- Tracking Claude usage and tools used
- Updating different sections of the work diary
- Generating daily summaries and statistics
- Managing weekly and monthly reviews

---

## How It Works

### 1. Activity Capture
The skill analyzes your recent development activities and extracts:
- Tasks completed during the session
- Files created or modified
- Claude tools and prompts used
- Technical decisions made
- Challenges encountered and solutions found
- Code statistics (lines written, tests created)
- Design tools used

### 2. Diary Update Process
The skill updates the appropriate section of your work diary:
- **Daily Work Log**: Adds detailed session entries
- **Weekly Progress**: Updates weekly summaries
- **Technical Decisions**: Logs new decisions with reasoning
- **Feature Tracker**: Updates implementation status
- **Statistics**: Refreshes productivity metrics

### 3. Intelligent Formatting
The skill ensures all entries follow the established template format with proper:
- Session organization (Morning/Afternoon/Evening)
- Task categorization and priority
- Claude usage documentation
- Technical decision logging
- Challenge-solution pairing

---

## Usage Examples

### After a Development Session
```
"I just finished implementing the convention plugins and I want to update my work diary."
```

The skill will:
- Analyze the files you created/modified
- Extract the technical decisions made
- Document Claude usage patterns
- Format the entry for the appropriate time period
- Update statistics and progress

### Daily Summary
```
"Update my work diary with today's progress."
```

The skill will:
- Create a comprehensive daily entry
- Update weekly summary
- Refresh statistics
- Plan tomorrow's priorities

### Weekly Review
```
"Generate a weekly work diary summary."
```

The skill will:
- Compile all weekly activities
- Calculate productivity metrics
- Assess goal progress
- Identify technical debt
- Plan next week's priorities

---

## Features

### 🔄 Automatic Activity Detection
- Monitors file system changes
- Tracks Claude interactions
- Captures build activities
- Records testing sessions

### 📝 Smart Formatting
- Maintains diary template consistency
- Organizes entries by session type
- Categorizes activities properly
- Ensures complete documentation

### 📊 Statistics Generation
- Claude session counts and types
- Files created/modified tracking
- Code generation metrics
- Productivity calculations

### 🎯 Progress Tracking
- Feature implementation status
- Goal and OKR progress
- Technical debt management
- Project health assessment

### 🧠 Intelligent Summarization
- Extracts key achievements
- Identifies patterns and trends
- Highlights important decisions
- Notes recurring challenges

---

## Skill Parameters

### Input Options
- **Session Type**: Daily, Weekly, Monthly
- **Focus Area**: Development, Design, Research, Testing
- **Time Period**: Specific date or range
- **Detail Level**: Brief, Standard, Comprehensive

### Output Options
- **Update Type**: Add entry, Update summary, Generate report
- **Format**: Markdown, JSON, Statistics only
- **Scope**: Specific sections or entire diary

---

## Integration Points

### File System Monitoring
- Watches for file creation/modification
- Tracks build outputs
- Monitors documentation changes

### Claude Usage Tracking
- Records prompts and responses
- Tracks tool usage patterns
- Documents assistance provided

### Build System Integration
- Captures build activities
- Records dependency changes
- Tracks configuration updates

---

## Best Practices

### When to Use
1. **After development sessions** - Capture activities while fresh
2. **End of day** - Complete daily documentation
3. **End of week** - Generate comprehensive summaries
4. **After major milestones** - Document key achievements

### What Gets Captured
- All files created and modified
- Claude interactions and assistance
- Technical decisions with reasoning
- Challenges and solutions
- Code statistics and metrics
- Design tools and resources used

### What Gets Generated
- Properly formatted diary entries
- Statistical summaries
- Progress assessments
- Next steps and priorities
- Patterns and insights

---

## Troubleshooting

### Common Issues

**Issue**: Skill doesn't capture recent activities
**Solution**: Ensure files are saved and build system has completed

**Issue**: Diary format inconsistent
**Solution**: Use the standard templates in WORK_DIARY_TEMPLATE.md

**Issue**: Missing statistics
**Solution**: Run a weekly summary to recalculate metrics

**Issue**: Claude usage not tracked
**Solution**: Ensure you're using Claude tools during development

---

## Maintenance

### Regular Updates
- **Daily**: Update work diary after development sessions
- **Weekly**: Generate weekly summaries and reviews
- **Monthly**: Complete monthly assessments and goal reviews

### Template Updates
- Keep templates synchronized with WORK_DIARY_TEMPLATE.md
- Update formats as project evolves
- Add new categories as needed

### Archive Management
- Archive old weekly entries
- Maintain monthly summaries
- Keep yearly overviews

---

## Future Enhancements

### Planned Features
- [ ] Automatic time tracking
- [ ] Screenshot capture for design sessions
- [ ] Integration with git commit history
- [ ] Automated trend analysis
- [ ] Calendar integration for planning
- [ ] Export to different formats (PDF, HTML)

### Advanced Analytics
- [ ] Productivity trend analysis
- [ ] Claude usage optimization
- [ ] Technical debt forecasting
- [ ] Goal achievement probability
- [ ] Development velocity metrics

---

## Dependencies

### Required Files
- `WORK_DIARY.md` - Main work diary
- `WORK_DIARY_TEMPLATE.md` - Template reference
- Project file system for activity monitoring

### Optional Files
- `.git/config` - For commit history integration
- Build logs for activity tracking
- Claude conversation history

---

## Support and Feedback

This skill is designed to be self-maintaining and should adapt to your development workflow. If you encounter issues or have suggestions for improvement, document them in the work diary for future refinement.

**Last Updated:** August 23, 2025
**Version:** 1.0.0