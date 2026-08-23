# Convention Plugin Generator Skill Manifest

**Skill Name:** `convention-plugin-generator`
**Version:** 1.0.0
**Last Updated:** August 23, 2025

---

## Skill Purpose

Automatically generate new convention plugins for the LinkLibrary KMP project by analyzing existing patterns, creating properly structured plugins, and integrating them into the build system.

---

## Invocation Triggers

### Natural Language Triggers
The user can invoke this skill with phrases like:
- "Create a convention plugin for [purpose]"
- "Generate a convention plugin called [Name] that [purpose]"
- "Add a convention plugin for [technology/framework]"
- "Create plugin for [specific requirements]"

### Automatic Triggers
The skill can automatically trigger when:
- User mentions need for build configuration standardization
- Project requires new platform/technology support
- Build complexity needs to be reduced through convention plugins

---

## Skill Capabilities

### 1. Pattern Analysis
- Analyzes existing convention plugins for structure and patterns
- Extracts common configurations and naming conventions
- Identifies plugin complexity levels (Simple, Medium, Complex)
- Determines appropriate template based on requirements

### 2. Code Generation
- Generates properly structured Kotlin convention plugins
- Applies consistent naming conventions and code style
- Includes comprehensive KDoc documentation
- Creates extension functions for configuration

### 3. Integration Management
- Updates `build-logic/build.gradle.kts` with dependencies
- Creates plugin files in correct directory structure
- Updates documentation and usage guides
- Validates integration success

### 4. Quality Assurance
- Validates generated code syntax and structure
- Checks for conflicts with existing plugins
- Ensures proper dependency resolution
- Generates usage examples and documentation

---

## Input Parameters

### Required Parameters
- **Purpose**: What the plugin should configure
- **Plugin Name**: Class name for the plugin (optional, can be auto-generated)

### Optional Parameters
- **Complexity**: Simple, Medium, or Complex
- **Required Plugins**: Gradle plugins needed
- **Extensions**: Gradle extensions to configure
- **Configurations**: Specific configuration requirements
- **Dependencies**: Library dependencies to include
- **Compatible With**: Existing plugins it works with
- **Configurable**: Whether plugin supports customization

---

## Output Format

### Primary Output
- **Plugin Code**: Complete Kotlin class with proper structure
- **Integration Files**: Updated build configuration
- **Documentation**: Comprehensive usage guide
- **Examples**: Practical usage examples

### Secondary Output
- **Validation Report**: Integration success/failure
- **Usage Instructions**: How to apply the plugin
- **Compatibility Notes**: Works with/doesn't work with
- **Troubleshooting**: Common issues and solutions

---

## Processing Steps

### 1. Requirement Analysis
- Parse user input to understand plugin purpose
- Determine complexity level based on requirements
- Identify required plugins, extensions, and dependencies
- Check for compatibility with existing plugins

### 2. Pattern Recognition
- Analyze existing convention plugins
- Extract common patterns and structures
- Identify appropriate template for generation
- Determine naming conventions and code style

### 3. Code Generation
- Select appropriate template based on complexity
- Generate plugin class with proper structure
- Add extension functions for configuration
- Include comprehensive KDoc documentation

### 4. Integration
- Create plugin file in correct location
- Update `build-logic/build.gradle.kts` if needed
- Generate documentation and usage examples
- Validate integration success

---

## Supported Plugin Types

### Platform-Specific Plugins
- iOS Configuration
- Windows Desktop
- Linux Desktop
- macOS Desktop
- tvOS, watchOS (Apple platforms)

### Feature-Specific Plugins
- **Logging**: Kermit, Timber
- **HTTP Client**: Ktor, Retrofit
- **Database**: Room, SQLDelight
- **Testing**: JUnit, MockK
- **Code Quality**: Detekt, ktlint
- **DI**: Koin, Hilt
- **Navigation**: Compose Navigation
- **Performance**: Monitoring and optimization

### Build Optimization Plugins
- Compiler Options
- Build Performance
- Resource Management
- Code Shrinking

---

## Template System

### Simple Template
**For:** 1-2 plugins, minimal configuration
**Example:** Compose setup, basic plugin application

### Medium Template
**For:** 3-4 plugins, some extension configuration
**Example:** KSP + Room, Android + testing

### Complex Template
**For:** 5+ plugins, multiple extensions and source sets
**Example:** Full KMP library with multiple targets

### Custom Template
**For:** Specialized requirements not fitting standard patterns
**Example:** Custom build tasks, advanced configuration

---

## Code Quality Features

### Consistency
- **Naming**: Follows `{Purpose}ConventionPlugin` pattern
- **Package**: Always `package convention`
- **Structure**: Consistent with existing plugins
- **Style**: Matches existing code style

### Documentation
- **KDoc**: Comprehensive class documentation
- **Inline**: Key configuration comments
- **Usage**: Clear usage examples
- **Integration**: How to use with other plugins

### Type Safety
- **Strong Typing**: Proper Kotlin type annotations
- **Null Safety**: Explicit nullability handling
- **Extension Functions**: Type-safe extension configuration
- **No Raw Types**: Always use proper typed APIs

---

## Integration Points

### File System
- **Plugin Directory**: `build-logic/src/main/kotlin/convention/`
- **Build File**: `build-logic/build.gradle.kts`
- **Documentation**: `build-logic/README.md`

### Gradle Build System
- **Plugin Application**: Proper plugin ID generation
- **Dependency Management**: Version compatibility
- **Extension Configuration**: Type-safe extension access
- **Build Lifecycle**: Proper task integration

### Existing Plugins
- **Pattern Analysis**: Learns from existing plugins
- **Compatibility**: Ensures no conflicts
- **Reuse**: Shares common patterns and utilities
- **Consistency**: Maintains established conventions

---

## Error Handling

### Common Errors

**Error 1: Plugin Name Conflict**
- **Issue**: Plugin with same name already exists
- **Resolution**: Suggest alternative name or version suffix
- **Prevention**: Checks for existing plugins before generation

**Error 2: Dependency Incompatibility**
- **Issue**: Required dependency conflicts with existing plugins
- **Resolution**: Suggest compatible versions or alternatives
- **Prevention**: Validates dependencies before integration

**Error 3: Template Mismatch**
- **Issue**: Requirements don't fit standard templates
- **Resolution**: Use custom template or adjust requirements
- **Prevention**: Analyzes requirements to determine best template

**Error 4: Integration Failure**
- **Issue**: Generated plugin doesn't compile or integrate
- **Resolution**: Rollback and suggest fixes
- **Prevention**: Validates code before final integration

---

## Performance Considerations

### Optimization Strategies
- **Pattern Caching**: Cache existing plugin patterns
- **Template Reuse**: Reuse common code patterns
- **Incremental Generation**: Generate only what's needed
- **Lazy Validation**: Validate only when necessary

### Resource Usage
- **Memory**: Minimal, processes one plugin at a time
- **CPU**: Light, mainly string processing and file I/O
- **Disk**: Only creates necessary files
- **Network**: None, all local operations

---

## Testing

### Test Scenarios
1. **Simple Plugin Generation**: Basic plugin with minimal configuration
2. **Medium Plugin Generation**: Plugin with extensions and dependencies
3. **Complex Plugin Generation**: Full-featured plugin with multiple configurations
4. **Integration Testing**: Verify plugin works in actual build
5. **Compatibility Testing**: Test with existing plugins

### Success Criteria
- All generated plugins compile successfully
- Plugins integrate with existing build system
- Documentation is comprehensive and accurate
- Usage examples work as expected
- No conflicts with existing plugins

---

## Future Enhancements

### Planned Features
- [ ] Interactive plugin designer (UI-based)
- [ ] Plugin template library
- [ ] Automatic conflict resolution
- [ ] Plugin testing framework
- [ ] Version compatibility checker
- [ ] Plugin marketplace integration

### Advanced Analytics
- [ ] Usage pattern analysis
- [ ] Plugin optimization suggestions
- [ ] Build performance impact analysis
- [ ] Configuration complexity scoring
- [ ] Best practice recommendations

---

## Maintenance

### Regular Updates
- Update patterns based on new convention plugins
- Enhance templates based on user feedback
- Add new plugin types as needed
- Improve code generation algorithms

### User Feedback
- Collect usage patterns and preferences
- Identify pain points and areas for improvement
- Gather suggestions for new features
- Prioritize development based on demand

---

## Documentation

### User Documentation
- `README.md` - Comprehensive skill documentation
- `QUICKSTART.md` - Quick start guide and examples
- `convention-plugin-generator.kt` - Implementation details

### Reference Documentation
- Existing convention plugins in `build-logic/src/main/kotlin/convention/`
- Gradle plugin development guide
- Kotlin DSL reference
- Project-specific conventions

---

## Support and Troubleshooting

### Common Issues
1. **Plugin not found**: Check file location and build.gradle.kts
2. **Compilation errors**: Verify dependencies and syntax
3. **Configuration not applying**: Check extension compatibility
4. **Version conflicts**: Update dependency versions

### Debug Mode
Enable detailed logging:
- Set environment variable: `PLUGIN_GENERATOR_DEBUG=true`
- Check console output for detailed processing info
- Review generated code for formatting issues

---

## Version History

### v1.0.0 (August 23, 2025)
- Initial release
- Pattern analysis from existing plugins
- Code generation with templates
- Integration management
- Documentation generation
- Quality validation

---

## Contributors

- **Primary Developer**: LinkLibrary Project Team
- **AI Assistant**: Claude (Anthropic)
- **Testing**: Solo developer with AI assistance

---

## License

Part of the LinkLibrary project. Internal use only.

---

**This skill provides automated convention plugin generation that maintains consistency and quality while enabling rapid expansion of your build system capabilities.**