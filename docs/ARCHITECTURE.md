# Architecture

How Filizer is put together, and why.

> This page explains the design. For the list of classes and methods, see
> the [API Reference](API_REFERENCE.md). For planned work, see the
> [Roadmap](ROADMAP.md).

---

## Table of contents

- [Goal](#goal)
- [Layers](#layers)
- [Dependency flow](#dependency-flow)
- [Lifecycle](#lifecycle)
- [Design decisions](#design-decisions)
- [Threading model](#threading-model)
- [Extension points](#extension-points)
- [Known limitations](#known-limitations)

---

## Goal

Filizer wraps YAML configuration handling for Paper / Bukkit plugins so
that consumers never write file plumbing themselves.

The guiding constraint is that **the plugin entry point stays thin**. The
Bukkit lifecycle is an adapter, not the place where logic lives. Everything
that matters — file handling, logging, error construction — sits in plain
Java classes that can be instantiated and tested without a running server.

A consequence worth stating explicitly: `FileManager` and everything it
depends on can be constructed with `new`. Filizer is usable as a library,
not only as a plugin.

---

## Layers

The `storage` package follows a ports-and-adapters split. Each layer only
knows about the ones beneath it.

| Layer | Package | Role |
| --- | --- | --- |
| **API** | `storage/api` | The public surface. `FileManager` is the only entry point consumers need. |
| **Domain** | `storage/domain` | What a managed file *is*: `CustomFile`, `ConfigurationSectionBuilder`. |
| **Infrastructure** | `storage/infrastructure` | How it touches the outside world: `FileFactory` (disk writes), `FileReader` (config reads), `FileRegistry` (in-memory store), `FileSynchronizer` (staleness checks). |
| **Sync** | `storage/sync` | The strategy contract and its implementations. |

Two cross-cutting packages sit outside that stack:

| Package | Role |
| --- | --- |
| `shared/logging` | `AppLogger` abstraction + `BukkitAppLogger` adapter. |
| `shared/exceptions` | `FilizerExceptions` factory + the typed exceptions it builds. |

And two Bukkit-facing ones:

| Package | Role |
| --- | --- |
| `bootstrap` | `PluginBootstrap` — constructs and wires every runtime object. |
| `commands` | Bukkit command entry points. |

---

## Dependency flow

```text
FilizerPlugin  (Bukkit entry point)
      │
      ▼
PluginBootstrap  ──────────────┐  wires everything
      │                        │
      ▼                        ▼
 FileManager  ◄──────────  DebugCommand
      │
      ├──► FileRegistry        (in-memory store, keyed by absolute path)
      ├──► FileSynchronizer ──► FileSynchronizationStrategy
      └──► CustomFile
              ├──► FileFactory   (creates the file on disk)
              ├──► FileReader    (typed reads over FileConfiguration)
              └──► FileSynchronizer

 AppLogger  and  FilizerExceptions  are injected downward into all of them.
```

Two properties hold and are worth preserving:

- **Nothing points back up.** `CustomFile` does not know about
  `FileManager`; `FileRegistry` does not know about the plugin.
- **No static state.** Every collaborator is passed through a constructor.
  There is no service locator and no singleton in the runtime path.

---

## Lifecycle

### Startup

1. Bukkit calls `FilizerPlugin.onEnable()`.
2. It constructs a `PluginBootstrap` and calls `start()`.
3. `start()` builds, in order:
   - `BukkitAppLogger` over the plugin's `java.util.logging.Logger`
   - `FilizerExceptions`, bound to that logger
   - `FileRegistry` (empty)
   - the `FileSynchronizationStrategy` (currently `LastModifiedStrategy`)
   - `FileManager`, receiving all of the above
4. Commands are registered against that `FileManager`.

The order is not incidental: the logger comes first because the exception
factory needs it, and the exception factory comes before anything that can
fail.

### Shutdown

`FilizerPlugin.onDisable()` delegates to `PluginBootstrap.stop()`, which
currently releases the references it holds. Nothing else needs closing yet
— but this is the hook that will have to stop the watcher thread once
`WatchServiceSynchronizationStrategy` exists. See
[Known limitations](#known-limitations).

---

## Design decisions

### The registry is keyed by normalized absolute path

`FileRegistry` stores files in a map keyed by
`path.toAbsolutePath().normalize().toString()`, not by file name.

Name-based keys break as soon as two modules each want their own
`config.yml`. Path-based keys make that case correct by construction, at
the cost of one ambiguity: `findFile(String name)` may match several
entries. It returns `Optional.empty()` and logs a warning rather than
guessing.

*Trade-off accepted:* name lookup is convenient but lossy. It stays for
ergonomics; the path-based overload is the correct one for library code.

### Exceptions come from a factory, not from constructors

`FilizerExceptions` builds every failure. It holds the `AppLogger`, so
constructing an exception also logs it — a failure can never be thrown
silently.

The typed exceptions extend `IllegalArgumentException` or
`IllegalStateException` rather than a Filizer-specific hierarchy, so
consumers are not forced to catch anything Filizer-specific to use the API.

### Logging is an interface

Filizer never calls `java.util.logging` directly. `AppLogger` has one
adapter today (`BukkitAppLogger`), but the indirection is what lets the
storage layer be unit-tested without a server, and lets other modules of
the stack route Filizer's output through their own logging.

### Synchronization is a strategy, not a flag

Whether to reload a file that changed on disk is a policy, and it differs
per deployment: an admin-editable config wants reloads, a plugin-owned data
file does not. Encoding it as a `FileSynchronizationStrategy` keeps
`CustomFile` free of branching and makes the policy swappable in one line
at bootstrap.

`CustomFile` calls `sync()` before **every** read and **every** mutation,
so a strategy is the single choke point through which staleness is
handled.

### Writes are explicit

Mutations stay in memory until `save()` is called. Auto-saving on every
`set()` would make chained calls quadratic in disk writes; making it
explicit keeps the write cost visible at the call site.

---

## Threading model

| Component | Guarantee |
| --- | --- |
| `FileRegistry` | Backed by a `ConcurrentHashMap`. Registration and lookup are safe from any thread. |
| `FileSynchronizer` / strategies | `synchronize()` runs **on the calling thread**, before every access. |
| `CustomFile` | **Not** thread-safe. Its `FileConfiguration` and `lastModified` field are unguarded. |

The practical rule: the registry may be shared freely, but a single
`CustomFile` should be mutated from one thread — in a Bukkit plugin, the
main server thread.

Because `synchronize()` is on the hot path of every read, strategy
implementations must stay cheap. `LastModifiedStrategy` is a single
`File.lastModified()` stat call; anything materially more expensive belongs
on a background thread instead.

---

## Extension points

Three seams are meant to be implemented by consumers:

| Interface | Implement it to… |
| --- | --- |
| `FileSynchronizationStrategy` | Change how external edits are detected. |
| `AppLogger` | Route Filizer's logs somewhere other than the Bukkit logger. |
| `FilizerExceptions` (subclass/replace) | Customize how failures are built and reported. |

`FileManager` accepts all three through its constructor, so none of them
requires touching Filizer's source.

---

## Known limitations

These are the places where the current design is knowingly incomplete.
Planned work for each is tracked in the [Roadmap](ROADMAP.md).

**`WatchServiceSynchronizationStrategy` is a stub.** The class exists and
documents its intended implementation, but `synchronize()` is empty. Using
it today means no synchronization at all — `NeverSynchronizationStrategy`
with extra steps. Event-driven sync also requires a background thread and a
`WatchService` to close, which is the first thing that will give
`PluginBootstrap.stop()` real work to do.

**`findFile(String)` is ambiguous by design.** Documented above; callers
that cannot tolerate it should use the path-based overload.

**`PluginBootstrap.stop()` does not release external resources.** Nothing
holds any yet. This becomes a correctness issue the moment a watcher
thread exists.

**No test suite.** The design deliberately avoids static state and Bukkit
coupling so that `FileManager`, `FileRegistry` and the exception factory
are testable — but the tests are not written.

---

This document is intentionally kept separate from the main
[README](../README.md) so that design notes can evolve independently.
