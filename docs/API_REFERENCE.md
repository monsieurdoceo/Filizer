# API reference

Complete reference of the public Filizer API.

> This page documents the classes and methods intended for use by other
> plugins. Internal wiring (`FilizerPlugin`, `PluginBootstrap`) is covered
> in [ARCHITECTURE.md](ARCHITECTURE.md).

---

## Table of contents

- [FileManager](#filemanager)
- [CustomFile](#customfile)
- [FileReader](#filereader)
- [FileRegistry](#fileregistry)
- [ConfigurationSectionBuilder](#configurationsectionbuilder)
- [Synchronization strategies](#synchronization-strategies)
- [AppLogger](#applogger)

---

## `FileManager`

`io.github.monsieurdoceo.filizer.storage.api.FileManager`

The single public facade of the API. It owns a [`FileRegistry`](#fileregistry)
and a `FileSynchronizer`, and exposes lookup, creation, registration,
deletion and directory scanning.

### Constructors

| Constructor | Description |
| --- | --- |
| `FileManager(FileRegistry registry, FileSynchronizationStrategy strategy, AppLogger logger)` | Builds a manager with a default `FilizerExceptions` factory bound to the given logger. |
| `FileManager(FileRegistry registry, FileSynchronizationStrategy strategy, AppLogger logger, FilizerExceptions errors)` | Same, with an explicit exception factory. |

```java
FileManager fileManager = new FileManager(
    new FileRegistry(),
    new LastModifiedStrategy(),
    new BukkitAppLogger(getLogger())
);
```

### Methods

| Method | Description |
| --- | --- |
| `boolean containsFile(String name)` | Whether a file with this name is registered. |
| `Optional<CustomFile> findFile(String name)` | Lookup by name. Empty if absent **or ambiguous** (see note below). |
| `Optional<CustomFile> findFile(Path path, String name)` | Lookup by parent path + name. Unambiguous. |
| `CustomFile requireFile(String name)` | Like `findFile`, but throws if absent. |
| `CustomFile createFile(String path, String name)` | Creates/registers a file from a path string. |
| `CustomFile createFile(File parent, String name)` | Same, from a parent `File` (e.g. `getDataFolder()`). |
| `void removeFile(String name)` | Unregisters by name. Does **not** touch the disk. |
| `void removeFile(Path path, String name)` | Unregisters by path. Does **not** touch the disk. |
| `void removeFile(CustomFile file)` | Unregisters an instance. Does **not** touch the disk. |
| `void storeAllFiles(Path root)` | Recursively registers every regular file under `root`. Throws `IOException`. |
| `boolean deleteFile(String name)` | Deletes from disk **and** unregisters. |
| `boolean deleteFile(CustomFile file)` | Same, on an instance. |
| `FileRegistry getRegistry()` | The backing registry. |
| `FileSynchronizer getSynchronizer()` | The synchronizer in use. |

> **Note on name-based lookup.** `findFile(String)` returns
> `Optional.empty()` when several registered files share the same name, and
> logs a warning through `FilizerExceptions.ambiguousFileName`. Prefer the
> path-based overload in new code.

> **`removeFile` vs `deleteFile`.** `removeFile` only drops the entry from
> the registry; the file remains on disk. `deleteFile` removes the file from
> the filesystem *and* the registry.

### Creation is idempotent

`createFile` returns the already-registered instance when the same path is
requested twice. Calling it on every startup is safe.

```java
CustomFile a = fileManager.createFile(getDataFolder(), "config.yml");
CustomFile b = fileManager.createFile(getDataFolder(), "config.yml");
/* a == b */
```

If the file does not exist on disk, it is created — along with any missing
parent directories.

---

## `CustomFile`

`io.github.monsieurdoceo.filizer.storage.domain.CustomFile`

One managed file on disk plus its loaded `FileConfiguration`. Instances are
obtained through [`FileManager`](#filemanager) rather than constructed
directly.

Every mutating call (`set`, `list`, `section`) and every read through
`getFileReader()` first asks the synchronizer whether the on-disk file
changed, so you never operate on stale content. Changes stay in memory
until you call `save()`.

### Methods

| Method | Description |
| --- | --- |
| `CustomFile set(String path, Object value)` | Sets a value. Chainable. |
| `CustomFile list(String path, List<?> values)` | Sets a list. Chainable. |
| `CustomFile list(String path, Object... values)` | Varargs variant. Chainable. |
| `CustomFile section(ConfigurationSectionBuilder builder)` | Creates a configuration section. Chainable. |
| `void save()` | Writes the configuration to disk and refreshes the timestamp. |
| `void reload()` | Reloads the configuration from disk. |
| `FileReader getFileReader()` | Typed reader, synchronized before it is returned. |
| `String getName()` | The file name. |
| `File getFile()` | The backing `File`. |
| `long getLastModified()` | Cached last-modified timestamp. |

### Example

```java
CustomFile config = fileManager.createFile(getDataFolder(), "config.yml");

config.set("database.host", "localhost")
      .set("database.port", 3306)
      .list("admins", "Doceo", "Sakyo")
      .save();
```

> **Nothing is written until `save()` is called.** Chained setters only
> mutate the in-memory configuration.

---

## `FileReader`

`io.github.monsieurdoceo.filizer.storage.infrastructure.FileReader`

A thin typed wrapper over Bukkit's `FileConfiguration`. Obtain one through
`CustomFile.getFileReader()` — never construct it yourself, otherwise you
bypass synchronization.

### Methods

| Method | Description |
| --- | --- |
| `Object get(String path)` | Raw value at the given path. |
| `String getString(String path)` | String value, or `null`. |
| `int getInt(String path)` | Integer value, `0` if absent. |
| `double getDouble(String path)` | Double value, `0.0` if absent. |
| `List<String> getStringList(String path)` | String list, empty if absent. |
| `List<Integer> getIntegerList(String path)` | Integer list, empty if absent. |
| `ConfigurationSection getSection(String path)` | Section at the given path, or `null`. |
| `Set<String> getKeys(boolean deep)` | Root keys. `deep` walks nested sections. |
| `Set<String> getKeys(String path, boolean deep)` | Keys under `path`. Falls back to the root keys when `path` is null, empty or absent. |
| `boolean has(String path)` | Whether the path exists. |

### Example

```java
FileReader reader = config.getFileReader();

String host = reader.getString("database.host");
int port = reader.getInt("database.port");

if (reader.has("admins")) {
    for (String admin : reader.getStringList("admins")) {
        getLogger().info("Admin: " + admin);
    }
}
```

---

## `FileRegistry`

`io.github.monsieurdoceo.filizer.storage.infrastructure.FileRegistry`

The in-memory store of managed files, backed by a `ConcurrentHashMap` keyed
by **normalized absolute path**. This is what allows two files named
`config.yml` in different folders to coexist safely.

Most plugins never call it directly — go through `FileManager`. It is
exposed for advanced use and for iterating over everything that is
registered.

### Methods

| Method | Description |
| --- | --- |
| `boolean contains(CustomFile file)` | Whether the instance is registered. |
| `void add(CustomFile file)` | Registers a file. No-op if the path is already present. |
| `void remove(String name)` | Removes every file matching this name. |
| `void remove(Path path)` | Removes the file at this path. |
| `void remove(CustomFile file)` | Removes this instance. |
| `Optional<CustomFile> find(Path path)` | Lookup by path. |
| `Optional<CustomFile> find(File file)` | Lookup by `File`. |
| `Collection<CustomFile> findByName(String name)` | Every file with this name — may return more than one. |
| `Collection<CustomFile> getFiles()` | All registered files. |

```java
for (CustomFile file : fileManager.getRegistry().getFiles()) {
    getLogger().info("Managed: " + file.getFile().getAbsolutePath());
}
```

---

## `ConfigurationSectionBuilder`

`io.github.monsieurdoceo.filizer.storage.domain.ConfigurationSectionBuilder`

Builds a YAML section from key/value pairs before applying it to a file
through `CustomFile.section(...)`.

### Methods

| Method | Description |
| --- | --- |
| `ConfigurationSectionBuilder(String name)` | Creates a builder for the section named `name`. |
| `ConfigurationSectionBuilder set(String key, Object value)` | Adds an entry. Chainable. |
| `ConfigurationSectionBuilder replaceData(Map<String, Object> data)` | Clears the builder and replaces its content. Chainable. |
| `void createSection(FileConfiguration config)` | Applies the section. Usually called for you by `CustomFile.section(...)`. |
| `String getName()` | The section name. |
| `Map<String, Object> getData()` | The current entries. |

### Example

```java
ConfigurationSectionBuilder rewards =
    new ConfigurationSectionBuilder("rewards")
        .set("coins", 250)
        .set("xp", 40);

config.section(rewards).save();
```

Produces:

```yaml
rewards:
  coins: 250
  xp: 40
```

---

## Synchronization strategies

`io.github.monsieurdoceo.filizer.storage.sync`

A strategy decides what happens when Filizer is about to read or write a
file that may have been edited outside the plugin. It is chosen once, when
the `FileManager` is constructed, and applies to every file that manager
handles.

| Strategy | Behaviour |
| --- | --- |
| `LastModifiedStrategy` | Compares the file's `lastModified()` with the cached timestamp and reloads when it is newer. **Default and recommended.** |
| `NeverSynchronizationStrategy` | No-op. Use when the plugin is the only writer and you want zero overhead. |
| `WatchServiceSynchronizationStrategy` | Placeholder for event-driven sync via `java.nio.file.WatchService`. **Not implemented yet.** |

### Choosing a strategy

```java
// Reload automatically when an admin edits the file by hand.
new FileManager(registry, new LastModifiedStrategy(), logger);

// Skip the filesystem check entirely.
new FileManager(registry, new NeverSynchronizationStrategy(), logger);
```

### Writing your own

Implement `FileSynchronizationStrategy`. The contract is a single method,
called before every read and every mutation:

```java
public final class MyStrategy implements FileSynchronizationStrategy {

    @Override
    public void synchronize(CustomFile file) {
        /*
         * Called before every read and every mutation.
         * Call file.reload() when the on-disk content is newer.
         */
    }
}
```

> Keep implementations cheap. `synchronize` runs on the calling thread, on
> every single access.

---

## `AppLogger`

`io.github.monsieurdoceo.filizer.shared.logging.AppLogger`

Logging abstraction used across the API, so Filizer never depends directly
on `java.util.logging`.

| Method | Description |
| --- | --- |
| `String getCurrentlyLoggedMessage()` | The last message that was logged. |
| `void info(String message)` | Informational message. |
| `void warn(String message)` | Warning. |
| `void error(String message)` | Error. |
| `void error(String message, Throwable throwable)` | Error with a cause. |

`BukkitAppLogger` is the default adapter over the plugin's
`java.util.logging.Logger`:

```java
AppLogger logger = new BukkitAppLogger(getLogger());
```

Implement `AppLogger` yourself to route logs elsewhere — tests, a file
appender, or another module of the stack.

---

## Errors

Every failure path goes through `FilizerExceptions`, a factory that **logs
and builds** the exception in one step. See the
[Error handling](../README.md#error-handling) section of the README for the
full table of exception types.

> **Planned.** A dedicated **Exception** module will generalize this
> mechanism across the whole stack, and Filizer will wire it in
> automatically. Because the typed exceptions extend
> `IllegalArgumentException` and `IllegalStateException` rather than a
> Filizer-specific hierarchy, that change is not expected to break consumer
> code. See
> [Relationship to the Exception module](ARCHITECTURE.md#relationship-to-the-exception-module).

---

This document is intentionally kept separate from the main
[README](../README.md) so that the API reference can evolve independently.
