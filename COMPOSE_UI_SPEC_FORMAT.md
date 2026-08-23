# Claude Code Compose UI Implementation Format

## DSL Structure for Jetpack Compose UI

This format is designed to be parsed by Claude Code and directly translated into Jetpack Compose Kotlin code.

## Screen Definition Schema

```yaml
# Screen Metadata
metadata:
  name: "LinkDetailsScreen"
  package: "com.greenrobotdev.linklibrary.screens.details"
  file: "LinkDetailScreen.kt"
  description: "Displays comprehensive information about a saved link"

# Import Statements
imports:
  - "androidx.compose.foundation.layout.*"
  - "androidx.compose.material3.*"
  - "androidx.compose.runtime.*"
  - "com.greenrobotdev.linklibrary.model.Link"

# Screen Function Signature
function:
  name: "LinkDetailScreen"
  parameters:
    - name: "link"
      type: "Link"
      nullable: false
    - name: "onBack"
      type: "() -> Unit"
      default: "{}"
    - name: "onEdit"
      type: "(Link) -> Unit"
      default: "{}"
    - name: "onDelete"
      type: "() -> Unit"
      default: "{}"

# Component Hierarchy
components:
  # Root Component
  - type: "Scaffold"
    id: "root_scaffold"
    properties:
      modifier: "Modifier.fillMaxSize()"
      topBar:
        reference: "app_bar"
      bottomBar:
        reference: "action_bar"
    children:
      - reference: "scrollable_content"

  # Top App Bar
  - type: "TopAppBar"
    id: "app_bar"
    properties:
      title: "Link Details"
      navigationIcon:
        icon: "Icons.AutoMirrored.Filled.ArrowBack"
        onClick: "onBack"
      colors: "TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.primary)"
      modifier: "Modifier.height(56.dp)"

  # Scrollable Content
  - type: "LazyColumn"
    id: "scrollable_content"
    properties:
      modifier: "Modifier.fillMaxSize()"
      contentPadding: "PaddingValues(16.dp)"
      verticalArrangement: "Arrangement.spacedBy(16.dp)"
    children:
      - reference: "hero_card"
      - reference: "metadata_section"
      - reference: "tags_section"
      - reference: "notes_section"
      - reference: "related_links_section"

  # Hero Card
  - type: "Card"
    id: "hero_card"
    properties:
      modifier: "Modifier.fillMaxWidth()"
      elevation: "CardDefaults.cardElevation(defaultElevation = 4.dp)"
      shape: "RoundedCornerShape(16.dp)"
      colors: "CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)"
    children:
      - type: "Column"
        properties:
          modifier: "Modifier.padding(20.dp)"
          verticalArrangement: "Arrangement.spacedBy(12.dp)"
        children:
          - type: "Row"
            properties:
              verticalAlignment: "Alignment.CenterVertically"
              horizontalArrangement: "Arrangement.spacedBy(16.dp)"
            children:
              - type: "Card"
                properties:
                  modifier: "Modifier.size(64.dp)"
                  shape: "RoundedCornerShape(12.dp)"
                  colors: "CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)"
                children:
                  - type: "AsyncImage"
                    properties:
                      model: "link.faviconUrl"
                      contentDescription: "link title"
                      modifier: "Modifier.padding(16.dp)"
                      placeholder: "Icons.Default.Link"
              - type: "Column"
                properties:
                  modifier: "Modifier.weight(1f)"
                  verticalArrangement: "Arrangement.spacedBy(4.dp)"
                children:
                  - type: "Text"
                    properties:
                      text: "link.title"
                      style: "MaterialTheme.typography.headlineMedium"
                      color: "MaterialTheme.colorScheme.onSurface"
                      maxLines: 2
                      overflow: "TextOverflow.Ellipsis"
                  - type: "Text"
                    properties:
                      text: "link.url"
                      style: "MaterialTheme.typography.bodyMedium"
                      color: "MaterialTheme.colorScheme.onSurfaceVariant"
                      maxLines: 1
                      overflow: "TextOverflow.Ellipsis"

  # Metadata Section
  - type: "Card"
    id: "metadata_section"
    properties:
      modifier: "Modifier.fillMaxWidth()"
      elevation: "CardDefaults.cardElevation(defaultElevation = 2.dp)"
      shape: "RoundedCornerShape(12.dp)"
    children:
      - type: "Column"
        properties:
          modifier: "Modifier.padding(16.dp)"
          verticalArrangement: "Arrangement.spacedBy(8.dp)"
        children:
          - type: "Row"
            properties:
              horizontalArrangement: "Arrangement.spacedBy(8.dp)"
            children:
              - type: "Icon"
                properties:
                  imageVector: "Icons.Default.CalendarMonth"
                  tint: "MaterialTheme.colorScheme.primary"
                  size: "20.dp"
              - type: "Text"
                properties:
                  text: "formatDate(link.createdAt)"
                  style: "MaterialTheme.typography.bodySmall"
                  color: "MaterialTheme.colorScheme.onSurfaceVariant"

  # Action Bar
  - type: "Surface"
    id: "action_bar"
    properties:
      modifier: "Modifier.fillMaxWidth()"
      color: "MaterialTheme.colorScheme.surface"
      elevation: 8.dp
      shadowElevation: 8.dp
    children:
      - type: "Row"
        properties:
          modifier: "Modifier.padding(16.dp)"
          horizontalArrangement: "Arrangement.spacedBy(12.dp)"
        children:
          - type: "Button"
            properties:
              onClick: "{ /* Open in browser */ }"
              modifier: "Modifier.weight(1f)"
              colors: "ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)"
              shape: "RoundedCornerShape(12.dp)"
            children:
              - type: "Icon"
                properties:
                  imageVector: "Icons.Default.OpenInBrowser"
                  modifier: "Modifier.size(20.dp)"
                  tint: "MaterialTheme.colorScheme.onPrimary"
              - type: "Spacer"
                properties:
                  modifier: "Modifier.width(8.dp)"
              - type: "Text"
                properties:
                  text: "Open"
                  style: "MaterialTheme.typography.labelLarge"
                  color: "MaterialTheme.colorScheme.onPrimary"
```

## Component Type Mappings

```yaml
# Layout Components
Column:
  compose: "Column"
  properties:
    modifier: "Modifier"
    verticalArrangement: "Arrangement"
    horizontalAlignment: "Alignment"

Row:
  compose: "Row"
  properties:
    modifier: "Modifier"
    horizontalArrangement: "Arrangement"
    verticalAlignment: "Alignment"

Box:
  compose: "Box"
  properties:
    modifier: "Modifier"
    contentAlignment: "Alignment"

LazyColumn:
  compose: "LazyColumn"
  properties:
    modifier: "Modifier"
    contentPadding: "PaddingValues"
    verticalArrangement: "Arrangement"

# Material Components
Card:
  compose: "Card"
  properties:
    modifier: "Modifier"
    elevation: "CardDefaults.cardElevation"
    shape: "RoundedCornerShape"
    colors: "CardDefaults.cardColors"
    onClick: "() -> Unit"

Button:
  compose: "Button"
  properties:
    onClick: "() -> Unit"
    modifier: "Modifier"
    enabled: "Boolean"
    colors: "ButtonDefaults.buttonColors"
    shape: "RoundedCornerShape"

OutlinedButton:
  compose: "OutlinedButton"
  properties:
    onClick: "() -> Unit"
    modifier: "Modifier"
    colors: "ButtonDefaults.outlinedButtonColors"

Text:
  compose: "Text"
  properties:
    text: "String"
    style: "MaterialTheme.typography"
    color: "MaterialTheme.colorScheme"
    modifier: "Modifier"
    maxLines: "Int"
    overflow: "TextOverflow"

Icon:
  compose: "Icon"
  properties:
    imageVector: "ImageVector"
    contentDescription: "String?"
    tint: "Color"
    modifier: "Modifier"

Image:
  compose: "AsyncImage"
  properties:
    model: "String"
    contentDescription: "String?"
    modifier: "Modifier"
    placeholder: "ImageVector?"

TextField:
  compose: "OutlinedTextField"
  properties:
    value: "String"
    onValueChange: "(String) -> Unit"
    modifier: "Modifier"
    label: "String"
    placeholder: "String"
    isError: "Boolean"
    enabled: "Boolean"

# Surface Components
Scaffold:
  compose: "Scaffold"
  properties:
    modifier: "Modifier"
    topBar: "@Composable () -> Unit"
    bottomBar: "@Composable () -> Unit"
    floatingActionButton: "@Composable () -> Unit"

TopAppBar:
  compose: "TopAppBar"
  properties:
    title: "String"
    navigationIcon: "@Composable () -> Unit"
    actions: "@Composable () -> Unit"
    colors: "TopAppBarDefaults.topAppBarColors"
```

## Property Value Types

```yaml
# Modifiers
modifier_examples:
  fillMaxSize: "Modifier.fillMaxSize()"
  fillMaxWidth: "Modifier.fillMaxWidth()"
  padding: "Modifier.padding(16.dp)"
  size: "Modifier.size(64.dp)"
  combined: "Modifier.fillMaxWidth().padding(16.dp)"

# Arrangement
arrangement_examples:
  spacedBy: "Arrangement.spacedBy(12.dp)"
  center: "Arrangement.Center"
  spaceAround: "Arrangement.SpaceAround"
  spaceBetween: "Arrangement.SpaceBetween"

# Alignment
alignment_examples:
  centerVertically: "Alignment.CenterVertically"
  centerHorizontally: "Alignment.CenterHorizontally"
  top: "Alignment.Top"
  bottom: "Alignment.Bottom"

# Colors
color_examples:
  primary: "MaterialTheme.colorScheme.primary"
  onPrimary: "MaterialTheme.colorScheme.onPrimary"
  primaryContainer: "MaterialTheme.colorScheme.primaryContainer"
  surface: "MaterialTheme.colorScheme.surface"
  onSurface: "MaterialTheme.colorScheme.onSurface"
  background: "MaterialTheme.colorScheme.background"

# Typography
typography_examples:
  displayLarge: "MaterialTheme.typography.displayLarge"
  headlineMedium: "MaterialTheme.typography.headlineMedium"
  titleLarge: "MaterialTheme.typography.titleLarge"
  bodyMedium: "MaterialTheme.typography.bodyMedium"
  labelSmall: "MaterialTheme.typography.labelSmall"

# Icons
icon_examples:
  arrowBack: "Icons.AutoMirrored.Filled.ArrowBack"
  edit: "Icons.Default.Edit"
  delete: "Icons.Default.Delete"
  share: "Icons.Default.Share"
  link: "Icons.Default.Link"
  tag: "Icons.Default.LocalOffer"
```

## State Management Schema

```yaml
state_management:
  viewmodel:
    name: "LinkDetailViewModel"
    package: "com.greenrobotdev.linklibrary.screens.details"
    state:
      name: "LinkDetailState"
      properties:
        isLoading: "Boolean"
        error: "String?"
        link: "Link?"
        isDeleting: "Boolean"
    events:
      - "LoadLink(linkId: String)"
      - "DeleteLink()"
      - "ShareLink()"
      - "OpenInBrowser()"

  # State usage in composable
  state_usage:
    collect: "val state by viewModel.states.collectAsState()"
    property_access: "state.link?.title"
    event_handling: "viewModel.onEvent(LinkDetailEvent.DeleteLink)"
```

## Navigation Integration

```yaml
navigation:
  route:
    sealed_class: "RootScreens"
    object: "LinkDetail"
    parameters:
      - name: "linkId"
        type: "String"

  navigation_calls:
    navigate_to_detail: "navigator.navigate(RootScreens.LinkDetail(linkId))"
    navigate_back: "navigator.goBack()"

  # Navigation parameters
  params_usage:
    access_param: "route.linkId"
    pass_param: "onNavigateToDetail(link.id)"
```

## Complete Example: Simple Link Details Card

```yaml
screen_name: "SimpleLinkDetailCard"
package: "com.greenrobotdev.linklibrary.components"

imports:
  - "androidx.compose.foundation.layout.*"
  - "androidx.compose.material3.*"
  - "androidx.compose.runtime.*"
  - "com.greenrobotdev.linklibrary.model.Link"

function:
  name: "LinkDetailCard"
  parameters:
    - name: "link"
      type: "Link"
    - name: "onClick"
      type: "() -> Unit"
      default: "{}"

components:
  - type: "Card"
    properties:
      modifier: "Modifier.fillMaxWidth()"
      onClick: "onClick"
      elevation: "CardDefaults.cardElevation(defaultElevation = 2.dp)"
      shape: "RoundedCornerShape(12.dp)"
    children:
      - type: "Row"
        properties:
          modifier: "Modifier.padding(16.dp)"
          verticalAlignment: "Alignment.CenterVertically"
          horizontalArrangement: "Arrangement.spacedBy(16.dp)"
        children:
          - type: "Card"
            properties:
              modifier: "Modifier.size(48.dp)"
              shape: "RoundedCornerShape(8.dp)"
              colors: "CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)"
            children:
              - type: "Icon"
                properties:
                  imageVector: "Icons.Default.Link"
                  tint: "MaterialTheme.colorScheme.primary"
                  modifier: "Modifier.padding(12.dp)"
                  contentDescription: "null"
          - type: "Column"
            properties:
              modifier: "Modifier.weight(1f)"
              verticalArrangement: "Arrangement.spacedBy(4.dp)"
            children:
              - type: "Text"
                properties:
                  text: "link.title"
                  style: "MaterialTheme.typography.titleMedium"
                  color: "MaterialTheme.colorScheme.onSurface"
                  maxLines: 1
                  overflow: "TextOverflow.Ellipsis"
              - type: "Text"
                properties:
                  text: "link.url"
                  style: "MaterialTheme.typography.bodySmall"
                  color: "MaterialTheme.colorScheme.onSurfaceVariant"
                  maxLines: 1
                  overflow: "TextOverflow.Ellipsis"
          - type: "Icon"
            properties:
              imageVector: "Icons.Default.ChevronRight"
              tint: "MaterialTheme.colorScheme.onSurfaceVariant"
```

---

## How Claude Code Should Process This

1. **Parse the YAML structure** and understand component hierarchy
2. **Generate Kotlin Compose code** following the exact component structure
3. **Apply proper imports** based on components used
4. **Use Material 3 theming** with the specified color schemes
5. **Implement state management** as specified in the state section
6. **Add proper navigation** integration
7. **Include accessibility** features (content descriptions, proper touch targets)
8. **Handle different states** (loading, error, empty, content)
9. **Apply animations and transitions** where specified
10. **Follow Kotlin coding conventions** and best practices

## Usage Instructions

1. **Create a YAML file** following this schema for each screen
2. **Provide the YAML to Claude Code** with context about your app structure
3. **Claude Code will generate** the complete Compose implementation
4. **Review and refine** the generated code as needed
5. **Iterate on the design** by updating the YAML and regenerating

This format bridges the gap between UI design specifications and implementation code, making it easy for both designers and developers to collaborate.