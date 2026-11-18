# Koin: keep module definitions found via reflection
-keep class org.koin.** { *; }
-dontwarn org.koin.**

# kotlinx.serialization: keep generated serializers of app models
-keepattributes *Annotation*, InnerClasses
-keepclassmembers class kotlinx.serialization.json.** { *** Companion; }
-keepclasseswithmembers class com.greenrobotdev.linklibrary.** {
    kotlinx.serialization.KSerializer serializer(...);
}
-dontwarn kotlinx.serialization.**

# Room: entities are compiled in; nothing needed beyond defaults
