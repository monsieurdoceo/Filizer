# Installation

Everything you need to add Filizer to your project.

---

## Repository

Filizer is published to the Gradle Plugin Portal's Maven repository. Add it
alongside the Paper repository:

```groovy
repositories {
    maven { url = 'https://plugins.gradle.org/m2/' }
    maven { url = 'https://repo.papermc.io/repository/maven-public/' }
}
```

> **Do not use the `plugins { }` block** shown on the Gradle Plugin Portal
> page. That syntax applies a *Gradle build plugin*, and Filizer is a Bukkit
> server plugin — applying it would fail. Filizer is a dependency, so it
> belongs in `dependencies { }`.

## Gradle (Groovy DSL)

```groovy
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

## Complete build file

The sections above show each piece separately. Here they are assembled, for
a plugin consuming Filizer as a standalone server plugin.

```groovy
plugins {
    id 'java'
}

group = 'com.example.minigame'
version = '1.0.0'

repositories {
    maven { url = 'https://plugins.gradle.org/m2/' }
    maven { url = 'https://repo.papermc.io/repository/maven-public/' }
}

dependencies {
    compileOnly 'io.papermc.paper:paper-api:1.21.10-R0.1-SNAPSHOT'
    compileOnly 'io.github.monsieurdoceo.filizer:Filizer:0.1.0'
}

java {
    toolchain { languageVersion = JavaLanguageVersion.of(21) }
}
```

Drop `Filizer-0.1.0.jar` into the server's `plugins/` folder next to your own
plugin. To avoid that step, switch `compileOnly` to `implementation`, shade
Filizer into your jar, and remove the `depend` entry from `plugin.yml`.

## Next steps

With the dependency in place, see the
[Quick start](../README.md#quick-start) for a working plugin that creates,
writes and reads a file, then the
[API Reference](API_REFERENCE.md) for the full set of classes and methods.

---

This document is intentionally kept separate from the main
[README](../README.md) so that installation instructions can evolve
independently.
