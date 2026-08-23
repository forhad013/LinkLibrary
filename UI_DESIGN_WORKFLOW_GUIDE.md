# Complete UI Design Workflow Guide

## Quick Start: End-to-End Process

### Step 1: Give This Prompt to External AI Designer

Copy the content from **`AI_UI_DESIGNER_PROMPT.md`** and provide it to your external AI designer (like Midjourney, Figma AI, or any UI design service).

**What to tell the designer:**
> "Please create detailed UI designs for my Link Library mobile app following these specifications. Focus on the Link Details Screen first, then provide designs for the Add Link Screen, Library Screen, and Tags Management Screen. Use Material 3 design system with the Google brand colors specified in the requirements."

### Step 2: Receive Design Specifications

The AI designer will provide:
- Wireframes and layouts
- Component hierarchies
- Color schemes and typography
- Interaction designs
- Responsive behaviors

### Step 3: Convert Designs to Compose Format

Using the **`COMPOSE_UI_SPEC_FORMAT.md`** guide, convert the AI designer's output into the YAML format shown in **`stich/link_details_spec.yaml`**.

**Key conversion points:**
- Every UI element becomes a YAML component
- Layout structures become nested component hierarchies
- Colors/typography map to Material 3 theme properties
- Actions become onClick handlers
- States become different component configurations

### Step 4: Provide YAML to Claude Code

Give Claude Code the YAML specification with context:

```
Here's a UI specification for a Link Details Screen in my Link Library app.
Please implement this in Jetpack Compose following the existing app architecture.

Key context:
- Package: com.greenrobotdev.linklibrary.screens.details
- Follow existing MVVM+UseCase pattern
- Use Material 3 theming
- Integrate with existing navigation system
- Link model is already defined in com.greenrobotdev.linklibrary.model
```

### Step 5: Claude Code Generates Implementation

Claude Code will:
1. Parse the YAML structure
2. Generate proper Kotlin Compose code
3. Create ViewModels and State classes
4. Add navigation integration
5. Include all imports and dependencies
6. Follow your existing code patterns
7. Add proper error handling and states

### Step 6: Review and Iterate

- Test the generated implementation
- Provide feedback to Claude Code
- Refine the YAML specification
- Regenerate as needed

## Example: Full Workflow

### Input from AI Designer

```
Hero Section:
- Large card with rounded corners (16dp)
- Elevation of 4dp
- Contains favicon (64x64), title, and URL
- Row layout with favicon on left, text on right
- Title is bold, headline medium size
- URL is body medium, secondary color
- Spacing of 16dp between elements
```

### Convert to YAML

```yaml
- type: "Card"
  id: "hero_card"
  properties:
    modifier: "Modifier.fillMaxWidth()"
    elevation: "CardDefaults.cardElevation(defaultElevation = 4.dp)"
    shape: "RoundedCornerShape(16.dp)"
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
          children:
            - type: "AsyncImage"
              properties:
                model: "link.faviconUrl"
                modifier: "Modifier.padding(16.dp)"
        - type: "Column"
          properties:
            verticalArrangement: "Arrangement.spacedBy(4.dp)"
          children:
            - type: "Text"
              properties:
                text: "link.title"
                style: "MaterialTheme.typography.headlineMedium"
                fontWeight: "FontWeight.Bold"
```

### Claude Code Generates

```kotlin
@Composable
fun HeroCard(link: Link) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            // Favicon
            Card(
                modifier = Modifier.size(64.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                AsyncImage(
                    model = link.faviconUrl,
                    contentDescription = "Website favicon",
                    modifier = Modifier.padding(16.dp)
                )
            }

            // Title and URL
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = link.title,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = link.url,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
```

## Template Files Provided

### 1. `AI_UI_DESIGNER_PROMPT.md`
**Purpose**: Give this to external AI designers
**Contains**: Complete design system, brand guidelines, component specifications
**Usage**: Copy entire content to AI design tools

### 2. `COMPOSE_UI_SPEC_FORMAT.md`
**Purpose**: Reference for converting designs to Compose YAML
**Contains**: Complete schema, component mappings, property types
**Usage**: Use as reference when creating YAML specs

### 3. `stich/link_details_spec.yaml`
**Purpose**: Example of complete YAML specification
**Contains**: Full Link Details Screen specification
**Usage**: Template for creating other screen specifications

## Advanced Usage Tips

### For Complex Screens
1. Break down into multiple reusable components
2. Define state management upfront
3. Specify all loading/error/empty states
4. Include animation specifications

### For Consistent Design
1. Use the same spacing scales across screens
2. Maintain color consistency with Material 3 theme
3. Follow typography hierarchy strictly
4. Keep touch targets accessible (minimum 48dp)

### For Better Results
1. Provide context about your app architecture
2. Show examples of existing code patterns
3. Specify exact file locations and package names
4. Include navigation and routing information
5. Define data models and API contracts

## Common Mistakes to Avoid

❌ **Don't**: Give vague descriptions like "make it look nice"
✅ **Do**: Specify exact components, sizes, colors, spacing

❌ **Don't**: Mix design specs with implementation details
✅ **Do**: Keep YAML focused on UI structure, let Claude handle implementation

❌ **Don't**: Forget about accessibility and different screen sizes
✅ **Do**: Always include content descriptions and responsive behavior

❌ **Don't**: Skip state management and error handling
✅ **Do**: Define all states (loading, error, empty, content)

## Validation Checklist

Before giving YAML to Claude Code, ensure:

- [ ] All components have proper types and IDs
- [ ] All properties use correct Compose syntax
- [ ] All imports are listed
- [ ] State management is defined
- [ ] Navigation integration is specified
- [ ] Accessibility features are included
- [ ] Responsive behavior is considered
- [ ] All states are covered (loading, error, etc.)

## Expected Output

Claude Code should generate:

1. **Screen Composable** (`LinkDetailScreen.kt`)
2. **ViewModel** (`LinkDetailViewModel.kt`)
3. **State Models** (`LinkDetailStateModels.kt`)
4. **UseCase** (`LinkDetailUseCase.kt`)
5. **Navigation integration** in existing files
6. **Proper imports** and dependencies

---

## Need Help?

If something doesn't work:

1. **Check YAML syntax** - Ensure proper indentation
2. **Verify component types** - Use only supported Compose components
3. **Review property mappings** - Ensure properties match Compose APIs
4. **Look for typos** - Check all IDs and references
5. **Test incrementally** - Start with simple screens first

The system works best when you provide clear, structured specifications. Start simple and build up complexity as needed.