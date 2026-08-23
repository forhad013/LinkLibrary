# Chrome Extension Icons

This directory should contain the extension icons in the following sizes:

- `icon16.png` - 16x16 pixels
- `icon48.png` - 48x48 pixels
- `icon128.png` - 128x128 pixels

## How to Create Icons

### Option 1: Using Image Files
Place properly sized PNG files in this directory with the exact names above.

### Option 2: Generate from SVG
1. Create an SVG version of your icon
2. Use an online tool or ImageMagick to convert to PNG:
   ```bash
   convert icon.svg -resize 16x16 icon16.png
   convert icon.svg -resize 48x48 icon48.png
   convert icon.svg -resize 128x128 icon128.png
   ```

### Option 3: Using Figma/Sketch
Design the icon in your preferred design tool and export at the required sizes.

## Current Status
⚠️ **Placeholder icons needed** - The extension will not load properly without these icon files.

## Recommended Icon Design
For LinkLibrary, consider using:
- A book or bookmark icon
- The letter "L" in a circle
- A chain link symbol
- Your app's existing logo scaled appropriately

The icons should be:
- Simple and recognizable at small sizes
- Using your brand colors (purple #6200EE from the app)
- High contrast for visibility
- Consistent across all sizes