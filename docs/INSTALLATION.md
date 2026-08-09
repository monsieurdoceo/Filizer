# Installation

Everything you need to add Filizer to your project.

---

## Gradle (Groovy DSL)

```groovy
repositories {
    mavenCentral()
    maven { url = 'https://repo.papermc.io/repository/maven-public/' }
}

dependencies {
    implementation 'io.github.monsieurdoceo.filizer:Filizer:0.1.0'
}
```

## Gradle (Kotlin DSL)

```kotlin
dependencies {
    implementation("io.github.monsieurdoceo.filizer:Filizer:0.1.0")
}
```

## Shading

Filizer is designed to be embedded into your plugin rather than installed
as a separate server plugin.

When using Gradle, make sure your build configuration shades Filizer into
your final plugin JAR.

This allows your plugin to be deployed without requiring a separate
`Filizer.jar` on the server.

## Using Filizer as a standalone plugin

Filizer can also be installed as a separate server plugin.

In that case, use `compileOnly` instead:

### Groovy DSL

```groovy
dependencies {
    compileOnly 'io.github.monsieurdoceo.filizer:Filizer:0.1.0'
}
```

### Kotlin DSL

```kotlin
dependencies {
    compileOnly("io.github.monsieurdoceo.filizer:Filizer:0.1.0")
}
```

Then declare Filizer as a runtime dependency in your `plugin.yml`:

```yaml
depend:
  - Filizer
```

---

This document is intentionally kept separate from the main
[README](../README.md) so that installation instructions can evolve
independently.
