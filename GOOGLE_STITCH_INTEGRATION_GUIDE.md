# Google Stitch AI Integration Guide for LinkLibrary

## Overview
This guide shows you how to use Google Stitch AI to enhance your LinkLibrary app with intelligent features.

## 5 AI Features You Can Add Today

### 1. **Auto-Generate Tags** ⭐
**File:** `AITagsHelper.kt`
**What it does:** Suggests relevant tags for your links based on URL and title
**Where to add it:** In `AddLinkScreen.kt`, below the URL/title fields

**Usage:**
```kotlin
AITagSuggestionSection(
    url = state.url,
    title = state.title,
    onTagsSelected = { tags ->
        viewModel.onEvent(AddLinkEvent.TagsChanged(tags))
    }
)
```

### 2. **Auto-Generate Descriptions** 📝
**File:** `AIDescriptionHelper.kt`
**What it does:** Writes link descriptions automatically using AI
**Where to add it:** In `AddLinkScreen.kt`, above the description field

**Usage:**
```kotlin
AIDescriptionGenerator(
    url = state.url,
    title = state.title,
    currentDescription = state.description,
    onDescriptionGenerated = { description ->
        viewModel.onEvent(AddLinkEvent.DescriptionChanged(description))
    }
)
```

### 3. **AI-Powered Search** 🔍
**File:** `AISearchHelper.kt`
**What it does:** Natural language search with relevance scoring
**Where to add it:** In `LibraryScreen.kt`, replace existing search bar

**Usage:**
```kotlin
AISearchBar(
    searchQuery = searchQuery,
    onSearchQueryChanged = { query ->
        searchQuery = query
        // Trigger search
    },
    onAISuggestions = { results ->
        aiSearchResults = results
    }
)
```

### 4. **Smart Categorization** 📁
**File:** `AICategorizationHelper.kt`
**What it does:** Suggests which collection a link belongs to
**Where to add it:** In `AddLinkScreen.kt`, after tags section

**Usage:**
```kotlin
AICollectionSuggestion(
    url = state.url,
    title = state.title,
    availableCollections = listOf("Development", "Design", "Articles"),
    onCollectionSelected = { collection ->
        // Add to this collection
    }
)
```

### 5. **Duplicate Detection** 🔗
**File:** `DuplicateDetectionHelper.kt`
**What it does:** Warns when adding duplicate or similar links
**Where to add it:** In `AddLinkScreen.kt`, after URL field

**Usage:**
```kotlin
DuplicateDetectionWarning(
    url = state.url,
    existingLinks = allLinks, // Pass your existing links
    onContinueAnyway = {
        // User chose to add anyway
    }
)
```

## Setup Instructions

### Step 1: Configure API Key
```bash
# Create .env file in project root
cat > .env << 'EOF'
GOOGLE_STITCH_API_KEY=your_api_key_here
EOF
```

### Step 2: Add Dependencies
Already added to `app/build.gradle.kts`:
- Ktor HTTP client
- JSON serialization
- Content negotiation

### Step 3: Enable AI Features in Your Screens

1. **Update AddLinkScreen.kt:**
   - Import the AI helper components
   - Add state for tags and AI suggestions
   - Insert AI components at appropriate locations

2. **Update LibraryScreen.kt:**
   - Replace search bar with `AISearchBar`
   - Handle AI search results

3. **Update AddLinkStateModels.kt:**
   - Add `tags: List<String>` to state
   - Add `TagsChanged` event

### Step 4: Test the Features
1. Open AddLinkScreen
2. Enter a URL (e.g., "https://github.com/user/repo")
3. Click "Generate Tags" - AI suggests relevant tags
4. Click "Auto-generate description" - AI writes a description
5. Click "Suggest Collection" - AI recommends a collection
6. Check for duplicates - AI warns about similar links

## Customization Options

### Customize AI Prompts
Edit the prompts in `StitchRepository.kt`:

```kotlin
override fun suggestTags(linkUrl: String, linkTitle: String?): Flow<Result<List<String>>> = flow {
    val prompt = """
        Generate 5-10 relevant tags for this link:
        URL: $linkUrl
        Title: $linkTitle

        Focus on: technology, programming, design, and content type
    """.trimIndent()

    // ... rest of implementation
}
```

### Adjust AI Behavior
Modify generation config in requests:

```kotlin
GenerationConfig(
    temperature = 0.7,  // Higher = more creative
    maxOutputTokens = 1024,  // Response length
    topP = 0.9  // Nucleus sampling
)
```

## Production Considerations

1. **Rate Limiting:** Add throttling to AI requests
2. **Caching:** Cache AI responses to reduce API calls
3. **Error Handling:** Show user-friendly error messages
4. **Cost Management:** Monitor API usage and set limits

## Future Enhancements

- Batch processing for multiple links
- Link summarization for collections
- Content recommendation engine
- Smart folder organization
- Natural language queries ("show me all React tutorials")

## Files Created

✅ `StitchApiClient.kt` - HTTP client for Stitch API
✅ `StitchRepository.kt` - Repository for AI operations
✅ `StitchModels.kt` - Data models for API
✅ `StitchConfig.kt` - Platform-specific config
✅ `StitchModule.kt` - Koin dependency injection
✅ `AITagsHelper.kt` - Tag generation component
✅ `AIDescriptionHelper.kt` - Description generator
✅ `AISearchHelper.kt` - AI search component
✅ `AICategorizationHelper.kt` - Collection suggestions
✅ `DuplicateDetectionHelper.kt` - Duplicate detection

## API Reference

### StitchRepository Methods

```kotlin
// Generate tags for a link
repository.suggestTags(url, title)

// Generate description
repository.generateDescription(url, title)

// Analyze multiple links
repository.analyzeLinks(urls)

// Generate with custom tools
repository.generateWithTools(prompt, tools, systemInstruction)
```

## Support

- **Google Stitch Docs:** [Official Documentation](https://cloud.google.com/stitch)
- **API Key Setup:** [Google Cloud Console](https://console.cloud.google.com)
- **Issues:** Check `.env` file and API key if errors occur

## License

This integration follows the same license as your LinkLibrary project.
