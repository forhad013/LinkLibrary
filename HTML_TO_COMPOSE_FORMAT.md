# HTML to Jetpack Compose UI Format

## Why HTML Works Great

HTML is actually **excellent** for UI specifications because:
- ✅ Most AI design tools can export to HTML
- ✅ Natural hierarchical structure (like Compose components)
- ✅ Easy to understand and visualize
- ✅ Can be tested directly in browsers
- ✅ Rich ecosystem of design tools

## HTML Structure for Compose Conversion

### Basic Component Mapping

```html
<!-- Maps to: Scaffold -->
<div compose-type="Scaffold" class="root-container">

  <!-- Maps to: TopAppBar -->
  <header compose-type="TopAppBar" class="app-bar">
    <button compose-type="IconButton" class="back-button" data-on-click="onBack">
      <span data-icon="arrow-back">←</span>
    </button>
    <h1 class="app-title">Link Details</h1>
  </header>

  <!-- Maps to: LazyColumn -->
  <main compose-type="LazyColumn" class="scrollable-content">

    <!-- Maps to: Card -->
    <article compose-type="Card" class="hero-card" data-elevation="4">
      <div class="card-content">

        <!-- Maps to: Row -->
        <div compose-type="Row" class="hero-row">

          <!-- Maps to: Card with AsyncImage -->
          <div compose-type="Card" class="favicon-container">
            <img compose-type="AsyncImage"
                 src="{link.faviconUrl}"
                 alt="Website favicon"
                 data-placeholder="link-icon">
          </div>

          <!-- Maps to: Column -->
          <div compose-type="Column" class="text-content">
            <h2 compose-type="Text"
                class="link-title"
                data-style="headlineMedium"
                data-max-lines="2">{link.title}</h2>

            <p compose-type="Text"
               class="link-url"
               data-style="bodyMedium"
               data-color="onSurfaceVariant"
               data-max-lines="1">{link.url}</p>
          </div>
        </div>
      </div>
    </article>

    <!-- Maps to: FlowRow (for tags) -->
    <div compose-type="FlowRow" class="tags-container">
      <span compose-type="TagChip"
            class="tag"
            data-on-click="navigateToTag">{tag.name}</span>
    </div>

  </main>

  <!-- Maps to: Surface with Row of Buttons -->
  <footer compose-type="Surface" class="action-bar">
    <div compose-type="Row" class="button-row">
      <button compose-type="Button"
              class="primary-button"
              data-on-click="openInBrowser">
        <span data-icon="open-in-new">Open</span>
      </button>

      <button compose-type="OutlinedButton"
              class="icon-button"
              data-on-click="shareLink">
        <span data-icon="share">Share</span>
      </button>
    </div>
  </footer>
</div>
```

## HTML Data Attributes for Compose Properties

### Core Attributes

```html
<!-- Component Type -->
<div compose-type="Card">           <!-- Specifies Compose component -->

<!-- Layout Properties -->
<div data-modifier="fillMaxWidth">  <!-- Modifier.fillMaxWidth() -->
<div data-modifier="padding(16.dp)"> <!-- Modifier.padding(16.dp) -->
<div data-modifier="size(64.dp)">     <!-- Modifier.size(64.dp) -->

<!-- Style Properties -->
<h1 data-style="headlineMedium">    <!-- MaterialTheme.typography.headlineMedium -->
<p data-color="primary">            <!-- MaterialTheme.colorScheme.primary -->
<span data-font-weight="Bold">       <!-- FontWeight.Bold -->

<!-- Layout Behavior -->
<div data-vertical-arrangement="spacedBy(12.dp)">  <!-- Arrangement.spacedBy(12.dp) -->
<div data-horizontal-alignment="center">           <!-- Alignment.CenterHorizontally -->

<!-- State & Interaction -->
<button data-on-click="openInBrowser">            <!-- onClick handler -->
<div data-enabled="false">                        <!-- enabled = false -->
<div data-visible="isLoading">                    <!-- Conditional visibility -->

<!-- Content Properties -->
<img data-max-lines="2" data-overflow="ellipsis">  <!-- maxLines, overflow -->
<p data-line-height="20.sp">                     <!-- lineHeight -->
```

## Complete HTML Template Example

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Link Details Screen - Compose Spec</title>
    <style>
        /* This is just for visualization - Claude will extract semantic info */
        .root-container { min-height: 100vh; display: flex; flex-direction: column; }
        .app-bar { background: #4285F4; color: white; padding: 16px; }
        .scrollable-content { flex: 1; padding: 16px; }
        .card { border-radius: 16px; margin-bottom: 16px; box-shadow: 0 2px 8px rgba(0,0,0,0.1); }
        .hero-card { background: white; padding: 20px; }
        .hero-row { display: flex; align-items: center; gap: 16px; }
        .favicon-container { width: 64px; height: 64px; border-radius: 12px; background: #E8F0FE; }
        .text-content { flex: 1; display: flex; flex-direction: column; gap: 4px; }
        .link-title { font-size: 22px; font-weight: 600; color: #202124; margin: 0; }
        .link-url { font-size: 14px; color: #5F6368; margin: 0; }
        .action-bar { background: white; padding: 16px; box-shadow: 0 -2px 8px rgba(0,0,0,0.1); }
        .button-row { display: flex; gap: 12px; }
        .primary-button { flex: 1; background: #4285F4; color: white; border: none; padding: 12px; border-radius: 12px; }
        .icon-button { width: 48px; height: 48px; border: 1px solid #4285F4; border-radius: 12px; background: transparent; }
    </style>
</head>
<body>

<!-- Screen Metadata -->
<meta name="screen-name" content="LinkDetailsScreen">
<meta name="package" content="com.greenrobotdev.linklibrary.screens.details">
<meta name="function" content="LinkDetailScreen">
<meta name="description" content="Displays comprehensive information about a saved link">

<!-- Function Parameters -->
<meta name="parameters" content='[
  {"name": "routeKey", "type": "NavKey"},
  {"name": "linkId", "type": "String"},
  {"name": "onBack", "type": "() -> Unit", "default": "{}"},
  {"name": "onEdit", "type": "(String) -> Unit", "default": "{}"}
]'>

<!-- Main Screen Structure -->
<div compose-type="Scaffold" class="root-container">

    <!-- Top App Bar -->
    <header compose-type="TopAppBar" class="app-bar">
        <button compose-type="IconButton"
                class="back-button"
                data-on-click="onBack"
                aria-label="Navigate back">
            <span data-icon="arrow-back">←</span>
        </button>
        <h1 class="app-title">Link Details</h1>
    </header>

    <!-- Scrollable Content Area -->
    <main compose-type="LazyColumn"
          class="scrollable-content"
          data-content-padding="16.dp"
          data-vertical-arrangement="spacedBy(16.dp)">

        <!-- Hero Card with Link Information -->
        <article compose-type="Card"
                 class="hero-card"
                 data-elevation="4"
                 data-shape="RoundedCornerShape(16.dp)"
                 data-colors="CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)">

            <div class="card-content" data-modifier="padding(20.dp)">

                <!-- Favicon and Title Row -->
                <div compose-type="Row"
                     class="hero-row"
                     data-vertical-alignment="CenterVertically"
                     data-horizontal-arrangement="spacedBy(16.dp)">

                    <!-- Favicon Preview -->
                    <div compose-type="Card"
                         class="favicon-container"
                         data-modifier="size(64.dp)"
                         data-shape="RoundedCornerShape(12.dp)"
                         data-colors="CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f))">

                        <img compose-type="AsyncImage"
                             src="{link.faviconUrl}"
                             alt="Website favicon"
                             data-modifier="padding(16.dp)"
                             data-placeholder="Icons.Default.Link"
                             data-error="Icons.Default.BrokenImage">
                    </div>

                    <!-- Title and URL Column -->
                    <div compose-type="Column"
                         class="text-content"
                         data-modifier="weight(1f)"
                         data-vertical-arrangement="spacedBy(4.dp)">

                        <h2 compose-type="Text"
                            class="link-title"
                            data-style="headlineMedium"
                            data-color="onSurface"
                            data-max-lines="2"
                            data-overflow="ellipsis"
                            data-font-weight="SemiBold">{link.title}</h2>

                        <p compose-type="Text"
                           class="link-url"
                           data-style="bodyMedium"
                           data-color="onSurfaceVariant"
                           data-max-lines="2"
                           data-overflow="ellipsis">{link.url}</p>
                    </div>
                </div>

                <!-- Description (conditional) -->
                <div compose-type="Column"
                     data-condition="link.description != null"
                     data-vertical-arrangement="spacedBy(8.dp)">
                    <p compose-type="Text"
                       data-style="bodyMedium"
                       data-color="onSurfaceVariant"
                       data-line-height="20.sp">{link.description}</p>
                </div>

                <!-- Quick Stats -->
                <div compose-type="Row"
                     class="stats-row"
                     data-horizontal-arrangement="spacedBy(24.dp)">

                    <div compose-type="Row" data-horizontal-arrangement="spacedBy(8.dp)">
                        <span data-icon="calendar_today" data-size="18.dp" data-color="primary">📅</span>
                        <span compose-type="Text"
                              data-style="bodySmall"
                              data-color="onSurfaceVariant">{formatDate(link.createdAt)}</span>
                    </div>

                    <div compose-type="Row" data-horizontal-arrangement="spacedBy(8.dp)">
                        <span data-icon="visibility" data-size="18.dp" data-color="primary">👁️</span>
                        <span compose-type="Text"
                              data-style="bodySmall"
                              data-color="onSurfaceVariant">{link.visitCount} views</span>
                    </div>
                </div>
            </div>
        </article>

        <!-- Tags Section -->
        <article compose-type="Card"
                 class="tags-card"
                 data-elevation="2"
                 data-shape="RoundedCornerShape(12.dp)">

            <div class="card-content" data-modifier="padding(16.dp)">

                <!-- Tags Header -->
                <div compose-type="Row"
                     data-horizontal-arrangement="SpaceBetween"
                     data-vertical-alignment="CenterVertically">
                    <div compose-type="Row" data-horizontal-arrangement="spacedBy(8.dp)">
                        <span data-icon="local_offer" data-size="20.dp" data-color="primary">🏷️</span>
                        <h3 compose-type="Text"
                            data-style="titleMedium"
                            data-font-weight="SemiBold">Tags</h3>
                    </div>
                    <button compose-type="TextButton"
                            data-on-click="navigateToTags"
                            data-style="labelLarge">Manage</button>
                </div>

                <!-- Tags List -->
                <div compose-type="FlowRow"
                     class="tags-list"
                     data-horizontal-arrangement="spacedBy(8.dp)"
                     data-vertical-arrangement="spacedBy(8.dp)">

                    <!-- Dynamic Tag Items -->
                    <span compose-type="TagChip"
                          class="tag-item"
                          data-on-click="filterByTag(tag)"
                          data-repeat-for="tag in link.tags">{tag.name}</span>
                </div>
            </div>
        </article>

        <!-- Notes Section -->
        <article compose-type="Card"
                 class="notes-card"
                 data-elevation="2"
                 data-shape="RoundedCornerShape(12.dp)">

            <div class="card-content" data-modifier="padding(16.dp)">
                <div compose-type="Row" data-horizontal-arrangement="spacedBy(8.dp">
                    <span data-icon="note" data-size="20.dp" data-color="primary">📝</span>
                    <h3 compose-type="Text"
                        data-style="titleMedium"
                        data-font-weight="SemiBold">Notes</h3>
                </div>

                <p compose-type="Text"
                   data-style="bodyMedium"
                   data-color="if (link.notes != null) onSurface else onSurfaceVariant.copy(alpha = 0.7f)"
                   data-line-height="22.sp">{link.notes ?: "No notes added"}</p>
            </div>
        </article>

    </main>

    <!-- Bottom Action Bar -->
    <footer compose-type="Surface"
            class="action-bar"
            data-shadow-elevation="8"
            data-tonal-elevation="8">

        <div compose-type="Row"
             class="button-row"
             data-modifier="padding(16.dp)"
             data-horizontal-arrangement="spacedBy(12.dp)">

            <!-- Primary Action Button -->
            <button compose-type="Button"
                    class="primary-button"
                    data-on-click="openInBrowser"
                    data-modifier="weight(1f)"
                    data-colors="ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)"
                    data-shape="RoundedCornerShape(12.dp)">
                <span data-icon="open_in_browser" data-size="20.dp" data-color="onPrimary">🌐</span>
                <span>Open</span>
            </button>

            <!-- Secondary Action Buttons -->
            <button compose-type="OutlinedButton"
                    class="icon-button"
                    data-on-click="shareLink"
                    data-colors="ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.primary)"
                    data-shape="RoundedCornerShape(12.dp)">
                <span data-icon="share" data-size="20.dp" data-color="primary">📤</span>
            </button>

            <button compose-type="OutlinedButton"
                    class="icon-button"
                    data-on-click="onEdit(linkId)"
                    data-colors="ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.primary)"
                    data-shape="RoundedCornerShape(12.dp)">
                <span data-icon="edit" data-size="20.dp" data-color="primary">✏️</span>
            </button>

            <button compose-type="OutlinedButton"
                    class="icon-button delete-button"
                    data-on-click="onDelete"
                    data-colors="ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)"
                    data-shape="RoundedCornerShape(12.dp)">
                <span data-icon="delete" data-size="20.dp" data-color="error">🗑️</span>
            </button>
        </div>
    </footer>

</div>

<!-- State Management -->
<meta name="viewmodel" content="LinkDetailViewModel">
<meta name="state" content='{
  "isLoading": "Boolean = false",
  "error": "String? = null",
  "link": "Link? = null",
  "isDeleting": "Boolean = false"
}'>

<!-- Navigation Integration -->
<meta name="navigation-route" content="RootScreens.LinkDetail(linkId)">
<meta name="navigation-back" content="onBack()">
<meta name="navigation-edit" content="onEdit(linkId)">

</body>
</html>
```

## HTML Component Reference

### Layout Components

```html
<!-- Container Components -->
<div compose-type="Box">              <!-- Box -->
<div compose-type="Row">              <!-- Row -->
<div compose-type="Column">           <!-- Column -->
<div compose-type="LazyColumn">       <!-- LazyColumn -->
<div compose-type="LazyRow">          <!-- LazyRow -->
<div compose-type="FlowRow">          <!-- FlowRow -->
<div compose-type="Grid">             <!-- Grid -->

<!-- Layout modifiers -->
<div data-layout="weight(1f)">        <!-- Modifier.weight(1f) -->
<div data-layout="fillMaxWidth()">     <!-- Modifier.fillMaxWidth() -->
<div data-layout="fillMaxHeight()">    <!-- Modifier.fillMaxHeight() -->
<div data-layout="fillMaxSize()">      <!-- Modifier.fillMaxSize() -->
```

### Material Components

```html
<!-- Surface Components -->
<header compose-type="TopAppBar">      <!-- TopAppBar -->
<div compose-type="Scaffold">          <!-- Scaffold -->
<div compose-type="Surface">           <!-- Surface -->
<div compose-type="Card">              <!-- Card -->
<div compose-type="BottomAppBar">      <!-- BottomAppBar -->

<!-- Interactive Components -->
<button compose-type="Button">         <!-- Button -->
<button compose-type="OutlinedButton"> <!-- OutlinedButton -->
<button compose-type="TextButton">     <!-- TextButton -->
<button compose-type="IconButton">     <!-- IconButton -->
<div compose-type="Switch">            <!-- Switch -->
<div compose-type="CheckBox">          <!-- CheckBox -->
<div compose-type="Slider">            <!-- Slider -->

<!-- Input Components
<input compose-type="TextField">       <!-- OutlinedTextField -->
<textarea compose-type="TextField">    <!-- OutlinedTextField (minLines) -->
<div compose-type="DropdownMenu">      <!-- ExposedDropdownMenu -->

<!-- Display Components -->
<h1 compose-type="Text">             <!-- Text with data-style -->
<p compose-type="Text">              <!-- Text with data-style -->
<span compose-type="Text">          <!-- Text (inline) -->
<div compose-type="LazyColumn">      <!-- For lists -->
<div compose-type="CircularProgress"> <!-- CircularProgressIndicator -->
```

### Icons and Images

```html
<!-- Icons -->
<span data-icon="arrow_back">←</span>         <!-- Icons.AutoMirrored.Filled.ArrowBack -->
<span data-icon="edit">✏️</span>               <!-- Icons.Default.Edit -->
<span data-icon="favorite">❤️</span>            <!-- Icons.Default.Favorite -->
<span data-icon="settings">⚙️</span>            <!-- Icons.Default.Settings -->

<!-- With properties -->
<span data-icon="share"
      data-size="20.dp"
      data-tint="primary">Share text</span>

<!-- Images -->
<img compose-type="AsyncImage"
     src="{imageUrl}"
     alt="description"
     data-modifier="size(64.dp)"
     data-placeholder="placeholder-icon">
```

## How Claude Code Processes HTML

### Processing Steps

1. **Parse HTML Structure**
   - Extract semantic HTML elements
   - Identify component hierarchies
   - Read data attributes for properties

2. **Map to Compose Components**
   - `<header>` → `TopAppBar`
   - `<button>` → `Button`
   - `<div>` → `Box/Row/Column` (based on context)
   - `<img>` → `AsyncImage`

3. **Extract Properties**
   - `compose-type` → Component class
   - `data-on-click` → onClick handler
   - `data-style` → Typography style
   - `data-color` → Color scheme
   - `data-modifier` → Modifier chain

4. **Generate Kotlin Code**
   - Create proper Compose functions
   - Add Material 3 theming
   - Include state management
   - Add navigation integration

### Example HTML → Compose Conversion

**HTML Input:**
```html
<button compose-type="Button"
        data-on-click="openBrowser"
        data-modifier="weight(1f)"
        data-colors="primary">
    <span data-icon="open_in_new">Open</span>
</button>
```

**Compose Output:**
```kotlin
Button(
    onClick = { openBrowser() },
    modifier = Modifier.weight(1f),
    colors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.primary
    )
) {
    Icon(
        imageVector = Icons.Default.OpenInNew,
        contentDescription = null,
        modifier = Modifier.size(20.dp)
    )
    Spacer(modifier = Modifier.width(8.dp))
    Text("Open", style = MaterialTheme.typography.labelLarge)
}
```

## Tips for Better HTML Specifications

### ✅ Best Practices

1. **Use Semantic HTML**
   ```html
   <!-- Good -->
   <header compose-type="TopAppBar">
   <main compose-type="LazyColumn">
   <footer compose-type="Surface">

   <!-- Avoid -->
   <div compose-type="TopAppBar">
   ```

2. **Be Specific with Data Attributes**
   ```html
   <!-- Good -->
   <div data-modifier="padding(16.dp)" data-elevation="4">

   <!-- Less clear -->
   <div class="card">
   ```

3. **Include All States**
   ```html
   <!-- Loading State -->
   <div data-state="loading">
     <div compose-type="CircularProgress"></div>
   </div>

   <!-- Content State -->
   <div data-state="content">
     <!-- Actual content -->
   </div>
   ```

4. **Add Accessibility Info**
   ```html
   <button aria-label="Navigate back" data-on-click="goBack">
   <img alt="Website favicon" src="...">
   ```

### ❌ Common Mistakes

1. **Missing compose-type attributes**
2. **Inconsistent data attribute naming**
3. **Forgetting to specify layout behaviors**
4. **Not including conditional rendering**
5. **Missing state management definitions**

## Complete Example Files

I can provide complete HTML examples for:
- Link Details Screen
- Add Link Screen
- Library Screen
- Tags Management Screen
- Settings Screen

Each HTML file will be immediately convertible to working Compose code!

---

## Ready to Use HTML?

Just provide me your HTML design specification and I'll convert it to proper Jetpack Compose implementation following your existing app architecture!

The HTML approach is often more intuitive since you can:
1. Design in tools that export HTML
2. Visualize directly in browser
3. Use familiar web concepts
4. Get immediate feedback

Would you like me to create a specific HTML template for one of your screens?