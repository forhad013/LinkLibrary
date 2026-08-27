# Manual Merge Instructions for Material 3 Expressive

## 🚀 Quick Merge Steps

Since there are permission issues with automatic merge, here are the manual steps to bring Material 3 Expressive changes to your `fh/design_library` branch:

### Option 1: Direct Cherry-Pick (Recommended)

```bash
cd "/Volumes/Projects/KMP project/LinkLibrary"

# Switch to your target branch
git checkout fh/design_library

# Cherry-pick the key commits
git cherry-pick 970c3f0  # Material 3 Expressive upgrade
git cherry-pick f5a4a81  # AppIcon fix
git cherry-pick 046049b  # Compilation fixes
```

### Option 2: Manual File Changes

If cherry-pick fails, manually copy these changes:

#### 1. Update `gradle/libs.versions.toml`
```toml
compose-material3 = "1.5.0-alpha02"  # Change from 1.3.1
```

#### 2. Create `core/design/src/commonMain/kotlin/com/greenrobotdev/linklibrary/design/theme/MaterialExpressiveTheme.kt`
```kotlin
package com.greenrobotdev.linklibrary.design.theme

import androidx.compose.material3.Experimentalexpressive.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MaterialExpressiveAppTheme(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    MaterialExpressiveTheme(
        modifier = modifier,
        content = content
    )
}
```

#### 3. Update `FilledButton.kt`
Add this import at the top:
```kotlin
import androidx.compose.material3.Experimentalexpressive.ExperimentalMaterial3ExpressiveApi
```

Add this annotation to the `FilledButton` function:
```kotlin
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun FilledButton(
```

#### 4. Fix `AppIcon.kt`
Change line 9 from:
```kotlin
import androidx.compose.ui.draw.size
```
To:
```kotlin
import androidx.compose.ui.draw.size as sizeModifier
```

And change line 49 from:
```kotlin
modifier = modifier.then(androidx.compose.ui.draw.size(size.dp)),
```
To:
```kotlin
modifier = modifier.sizeModifier(size.dp),
```

### Option 3: Use the Worktree Directly

```bash
# Work in the worktree where changes already exist
cd "/Volumes/Projects/KMP project/LinkLibrary/.claude/worktrees/reusable-compose-ui-e69c15"

# Push changes to a remote branch first
git push origin claude/reusable-compose-ui-e69c15

# Then on your main branch
cd "/Volumes/Projects/KMP project/LinkLibrary"
git checkout fh/design_library
git merge origin/claude/reusable-compose-ui-e69c15
```

## 📋 Summary of Changes to Apply

✅ **Version Upgrade**: `compose-material3: 1.3.1 → 1.5.0-alpha02`
✅ **Expressive Theme**: Added `MaterialExpressiveTheme.kt` with opt-in annotations  
✅ **Component Updates**: `FilledButton.kt` with `@OptIn(ExperimentalMaterial3ExpressiveApi::class)`
✅ **Bug Fixes**: `AppIcon.kt` size modifier conflict resolved
✅ **Documentation**: Added migration guides and status docs

## 🎯 What You'll Get

After applying these changes, you'll have:
- **Official Material 3 Expressive APIs** via Compose Material3 1.5.0-alpha02
- **Spring-based motion physics** (bouncy, gentle, stiff springs)
- **Enhanced visual effects** (dramatic scaling, elevation, color transitions)
- **Expressive theme support** with proper opt-in annotations
- **Dramatic ripple effects** (60-80dp radius)

## 🔧 Test After Merge

```bash
./gradlew :core:design:build
./gradlew :app:assembleDebug
```

---

**Choose the option that works best for you!** Option 1 (cherry-pick) is usually the fastest and safest approach.