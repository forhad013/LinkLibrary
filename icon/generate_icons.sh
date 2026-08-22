#!/bin/bash

# Android App Icon Generator Script
# This script helps generate Android app icons from a source image
# Place your source icon in /icon folder as "icon.png" or "icon.jpg"

echo "Android App Icon Generator"
echo "========================="
echo ""

SOURCE_DIR="/Volumes/Projects/KMP project/LinkLibrary/icon"
TARGET_DIR="/Volumes/Projects/KMP project/LinkLibrary/app/androidApp/src/main/res"

# Check if source icon exists
if [ ! -f "$SOURCE_DIR/icon.png" ] && [ ! -f "$SOURCE_DIR/icon.jpg" ]; then
    echo "❌ No icon found in $SOURCE_DIR"
    echo "Please place your app icon as 'icon.png' or 'icon.jpg' in the icon folder"
    echo "Recommended size: 512x512 pixels (PNG format with transparency)"
    echo ""
    echo "Icon requirements:"
    echo "  - Square aspect ratio"
    echo "  - Minimum 512x512 pixels"
    echo "  - PNG or JPG format"
    echo "  - Transparent background recommended"
    exit 1
fi

# Find the source icon
SOURCE_ICON="$SOURCE_DIR/icon.png"
if [ ! -f "$SOURCE_ICON" ]; then
    SOURCE_ICON="$SOURCE_DIR/icon.jpg"
fi

echo "✅ Found source icon: $SOURCE_ICON"
echo ""

# Check if ImageMagick is installed and determine command to use
MAGICK_CMD=""
if command -v magick &> /dev/null; then
    MAGICK_CMD="magick"
    echo "✅ ImageMagick v7+ is installed"
elif command -v convert &> /dev/null; then
    MAGICK_CMD="convert"
    echo "✅ ImageMagick (legacy) is installed"
else
    echo "❌ ImageMagick is not installed"
    echo ""
    echo "To generate icons automatically, install ImageMagick:"
    echo "  brew install imagemagick"
    echo ""
    echo "Or manually create icons in these sizes:"
    echo "  - mipmap-mdpi/ic_launcher.png (36x36)"
    echo "  - mipmap-hdpi/ic_launcher.png (48x48)"
    echo "  - mipmap-xhdpi/ic_launcher.png (72x72)"
    echo "  - mipmap-xxhdpi/ic_launcher.png (96x96)"
    echo "  - mipmap-xxxhdpi/ic_launcher.png (144x144)"
    echo "  - mipmap-anydpi-v26/ic_launcher_foreground.png (108x108)"
    echo ""
    echo "Then place them in: $TARGET_DIR"
    exit 1
fi

echo "Generating app icons for all densities..."
echo ""

# Define icon sizes for different densities (density:size pairs)
densities=(
    "mdpi:36x36"
    "hdpi:48x48"
    "xhdpi:72x72"
    "xxhdpi:96x96"
    "xxxhdpi:144x144"
)

# Generate icons for each density
for item in "${densities[@]}"; do
    IFS=':' read -ra density_info <<< "$item"
    density="${density_info[0]}"
    size="${density_info[1]}"

    target_file="$TARGET_DIR/mipmap-$density/ic_launcher.png"
    target_round="$TARGET_DIR/mipmap-$density/ic_launcher_round.png"

    # Convert and save icon
    $MAGICK_CMD "$SOURCE_ICON" -resize "$size" -background none -gravity center -extent "$size" "$target_file"
    $MAGICK_CMD "$SOURCE_ICON" -resize "$size" -background none -gravity center -extent "$size" "$target_round"

    echo "  ✓ Generated ${size} icons for $density"
done

# Generate adaptive icon (foreground)
adaptive_dir="$TARGET_DIR/mipmap-anydpi-v26"
$MAGICK_CMD "$SOURCE_ICON" -resize "108x108" -background none -gravity center -extent "108x108" "$adaptive_dir/ic_launcher_foreground.png"
echo "  ✓ Generated adaptive icon (108x108)"

echo ""
echo "✅ Icon generation complete!"
echo "Generated icons for all Android densities"

echo ""
echo "🎨 App icon setup instructions:"
echo "1. Place your icon as 'icon.png' in: $SOURCE_DIR"
echo "2. Run: ./icon/generate_icons.sh (or use ImageMagick manually)"
echo "3. Build and install the app"
echo "4. Your icon will appear as the app launcher icon"
