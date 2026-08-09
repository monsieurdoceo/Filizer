<div align="center">

# Filizer

**A lightweight file-management API for Paper / Bukkit servers**

[![Channel](https://img.shields.io/github/v/release/monsieurdoceo/Filizer?include_prereleases&label=release&style=flat-square)](https://github.com/monsieurdoceo/Filizer/releases)
[![Java](https://img.shields.io/badge/java-21-blue?style=flat-square)](https://adoptium.net/)
[![Paper](https://img.shields.io/badge/paper-1.21.10-blue?style=flat-square)](https://papermc.io/)

[Installation](docs/INSTALLATION.md)&nbsp; · &nbsp;[API Reference](docs/API_REFERENCE.md)&nbsp; · &nbsp;[Architecture](docs/ARCHITECTURE.md)&nbsp; · &nbsp;[Commands](docs/COMMANDS.md)

</div>

---

It gives plugin developers a clean, typed facade over the files their
plugin owns: create them, read them, write them, keep them in sync with the
disk, and get meaningful exceptions when something goes wrong — without
repeating boilerplate in every plugin.

File handling is format-agnostic: any extension can be created, registered,
scanned and deleted. The typed configuration layer on top is backed by YAML
today; support for other formats such as JSON is planned.

---

## Table of contents

- [Why Filizer](#why-filizer)
- [Place in the ecosystem](#place-in-the-ecosystem)
- [Installation](#installation)
- [Quick start](#quick-start)
- [Core concepts](#core-concepts)
- [API reference](#api-reference)
- [Error handling](#error-handling)
- [Commands](#commands)
- [Building and running](#building-and-running)
- [Project layout](#project-layout)
- [Credits](#credits)

---

## Why Filizer

Working with configuration files in a Bukkit plugin usually means the same
repeated code: check the parent directory, create the file, parse it,
remember to save, remember to reload when someone edits the file by hand,
and wrap every `IOException` yourself. Then repeat it for the next file
format.

Filizer factors all of that into a small set of components:

| Concern | Handled by |
| --- | --- |
| Creating files and parent directories | `FileFactory` |
| Holding the managed files in memory | `FileRegistry` |
| Public entry point for plugins | `FileManager` |
| One managed file (read/write/save) | `CustomFile` |
| Typed reads on the file content | `FileReader` |
| Detecting external edits | `FileSynchronizer` + strategies |
| Typed, self-logging exceptions | `FilizerExceptions` |

The plugin entry point stays thin: `FilizerPlugin` only delegates to `PluginBootstrap`, which wires everything together.

---

## Place in the ecosystem

Filizer is one module of a larger set of server-side API modules (GUI,
World, Exception, and others). Each module is standalone and consumable
independently as a **Gradle dependency**.

For our official server ecosystem, these modules are also combined through
a common internal wrapper, providing a unified API layer for our mini-game
plugins. This wrapper is specific to our server infrastructure and is not
required when using the modules independently.

Filizer is the **storage / file** layer of that stack.

---

## Installation

Ready to use Filizer?

See the [Installation Guide](docs/INSTALLATION.md) for everything you need to
add Filizer to your project.

---

## Quick start

```java
public final class MyPlugin extends JavaPlugin {

    private FileManager fileManager;

    @Override
    public void onEnable() {
        AppLogger logger = new BukkitAppLogger(getLogger());

        this.fileManager = new FileManager(
            new FileRegistry(),
            new LastModifiedStrategy(),
            logger
        );

        // Create (or reuse) a managed file
        CustomFile config = fileManager.createFile(getDataFolder(), "config.yml");

        /*
         * Write values — setters are chainable
         */
        config.set("server.name", "Lobby")
              .set("server.max-players", 120)
              .list("motd", "Welcome!", "Have fun.")
              .save();

        /*
         * Read values
         */
        FileReader reader = config.getFileReader();
        String name = reader.getString("server.name");
        int maxPlayers = reader.getInt("server.max-players");

        logger.info(name + " -> " + maxPlayers);
    }
}
```

### Registering an existing folder

```java
/*
 * Recursively registers every regular file under the given root.
 * The directory is created if it does not exist.
 */
fileManager.storeAllFiles(getDataFolder().toPath());
```

### Building a section

```java
ConfigurationSectionBuilder rewards = new ConfigurationSectionBuilder("rewards")
        .set("coins", 250)
        .set("xp", 40);

config.section(rewards).save();
```

---

## Core concepts

### `FileManager`

The single public facade. It owns a `FileRegistry` and a `FileSynchronizer`, and exposes lookup, creation, registration, deletion and directory scanning.

### `CustomFile`

One managed file on disk plus its loaded `FileConfiguration`. Created through `FileManager` — the underlying file (and its parent directories) is created automatically if missing.

Every mutating call (`set`, `list`, `section`) and every read through `getFileReader()` first asks the synchronizer whether the on-disk file changed, so you never operate on stale content. Changes stay in memory until you call `save()`.

### `FileRegistry`

A `ConcurrentHashMap` of managed files keyed by **normalized absolute path**. This is what makes two files named `config.yml` in different folders coexist safely.

### `FileReader`

A thin typed wrapper over `FileConfiguration`: `get`, `getString`, `getInt`, `getDouble`, `getStringList`, `getIntegerList`, `getSection`, `getKeys`, `has`.

---

## API reference

Looking for the full list of classes and methods?

See the [API Reference](docs/API_REFERENCE.md) for complete documentation of
`FileManager`, `CustomFile`, `FileReader`, `FileRegistry`,
`ConfigurationSectionBuilder`, and the synchronization strategies.

---

## Error handling

Errors go through `FilizerExceptions`, a factory that **logs and builds** the exception in one step, so failures are always traceable.

| Factory method | Exception | Type |
| --- | --- | --- |
| `fileNotFound` | `FilizerFileNotFoundException` | `IllegalArgumentException` |
| `invalidFilePath` | `FilizerInvalidFilePathException` | `IllegalArgumentException` |
| `invalidFileName` | `FilizerInvalidFileNameException` | `IllegalArgumentException` |
| `fileCreationFailed` | `FilizerFileCreationException` | `IllegalStateException` |
| `fileDeletionFailed` | `FilizerFileDeletionException` | `IllegalStateException` |
| `fileSaveFailed` | `FilizerFileSaveException` | `IllegalStateException` |
| `ambiguousFileName` | *(warning only)* | — |

Logging is abstracted behind `AppLogger`; `BukkitAppLogger` adapts it to the plugin's `java.util.logging.Logger`. Implement `AppLogger` yourself to route logs elsewhere (tests, another module of the stack, a file appender).

> **Planned:** `FilizerExceptions` is a local solution to a problem every
> module of the stack shares. A dedicated **Exception** module will
> generalize it — one factory pattern, one logging contract, one error
> vocabulary across Filizer, GUI, World and the rest. Filizer will then
> depend on that module and wire it in automatically, so consumers get the
> shared behaviour without any extra setup. The table above describes the
> current, self-contained implementation.

---

## Commands

Filizer provides a set of Bukkit commands for debugging and interacting
with the plugin.

See [COMMANDS.md](docs/COMMANDS.md) for the complete list of available commands,
their usage, and detailed descriptions.

---

## Building and running

```bash
# Build the jar
./gradlew build

# Start a Paper 1.21.10 test server with the plugin loaded (run-paper)
./gradlew runServer
```

The test server runs in `run/`. Gradle's configuration cache is enabled in `gradle.properties`, and the Java 21 toolchain is auto-detected/downloaded.

---

## Project layout

```
src/main/java/io/github/monsieurdoceo/filizer/
├── FilizerPlugin.java              # Bukkit entry point (thin)
├── bootstrap/                      # Application wiring and initialization
├── commands/                       # Bukkit command entry points
├── shared/                         # Shared infrastructure and cross-cutting concerns
└── storage/                        # File management, persistence, and synchronization
```

Design notes and open questions live in [`docs/ARCHITECTURE.md`](docs/ARCHITECTURE.md).

## Credits

Authors: **[MonsieurDoceo](https://github.com/monsieurdoceo)**, **[TheSakyo](https://github.com/TheSakyo)**
