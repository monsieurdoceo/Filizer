# Filizer - Architecture overview

## Goal of the project

Filizer is a small Bukkit/Paper plugin focused on file management.
The codebase is organized so that the plugin entry point stays thin, while the file logic, logging, and error handling live in dedicated packages.

## Current structure

### Startup and wiring

- `FilizerPlugin` is the Bukkit entry point.
- `PluginBootstrap` creates and wires the runtime objects.
- The bootstrap initializes:
  - the application logger
  - the exception factory
  - the file registry
  - the file manager
  - the command bindings

### File management

- `storage/api/FileManager` is the main facade used by commands and future services.
- It handles:
  - file lookup
  - file creation
  - file registration
  - deletion
  - directory scanning
  - synchronization delegation

### File model

- `storage/domain/CustomFile` represents one managed file.
- It stores the file state and the loaded YAML configuration.
- It exposes the main read/write operations on the file content.

### Infrastructure

- `storage/infrastructure/FileFactory` creates files on disk.
- `storage/infrastructure/FileReader` wraps configuration reads.
- `storage/infrastructure/FileRegistry` stores the managed files in memory.
- `storage/infrastructure/FileSynchronizer` delegates synchronization to a strategy.

### Synchronization

- `storage/sync/FileSynchronizationStrategy` is the contract for sync behavior.
- Implementations currently available:
  - `LastModifiedStrategy`
  - `NeverSynchronizationStrategy`
  - `WatchServiceSynchronizationStrategy`

### Shared concerns

- `shared/logging` contains the application logger abstraction and the Bukkit adapter.
- `shared/exceptions` contains the centralized exception factory and the specific exception types.
- `shared/util/FileChecker` contains basic file-name and path helpers.

## What is already in place

- The plugin entry point is separated from the bootstrap logic.
- Logging is centralized instead of being scattered through the codebase.
- Exceptions are centralized and typed.
- The file registry is no longer used as a global singleton in the normal runtime flow.
- Files are indexed by normalized absolute path, which avoids collisions between same-name files in different folders.
- The project compiles successfully.

## What needs attention

### 1. WatchService strategy

`WatchServiceSynchronizationStrategy` is present, but it is still the part to treat with the most care if you want real event-driven sync.

Recommended follow-up:
- create and manage a single watcher
- register parent directories for managed files
- react to file create / modify / delete events
- reload the matching `CustomFile`
- close the watcher cleanly on shutdown

### 2. Name-based lookups

`findFile(String)` is convenient, but it can become ambiguous if two files share the same name in different folders.

Current recommendation:
- prefer path-based lookup in new code
- keep name-based lookup for compatibility
- document the ambiguity behavior clearly for users of the API

### 3. Plugin shutdown

`PluginBootstrap.stop()` is intentionally light for now.

It should eventually be responsible for:
- stopping async resources
- closing watchers
- releasing runtime references if needed

### 4. Package naming

The current package layout is already readable.
The only package that may still evolve is `shared/util`, depending on whether you want to keep `FileChecker` there or move it to a more specific package later.

## What remains to be added

### Functional

- a complete WatchService implementation
- a more explicit API for loading existing files
- a clearer story for ambiguous file names
- stronger lifecycle handling for background services

### Quality

- unit tests for `FileManager`
- tests for `FileRegistry`
- tests for the centralized exceptions
- lightweight integration tests for file operations
- stricter validation of paths and file names

### Documentation

- usage examples for the public API
- notes on edge cases
- notes on duplicate names and path resolution

## Suggested priorities

### High priority

1. Finish the WatchService strategy
2. Add tests around file creation, lookup, and deletion
3. Document the lookup behavior when names collide

### Medium priority

4. Improve plugin shutdown handling
5. Refine error handling if new cases appear
6. Decide whether `shared/util` should stay as-is or be renamed later

### Low priority

7. Add API usage examples
8. Expand internal documentation
9. Prepare the codebase for future features

## Summary

The codebase is already in a good shape for a small plugin:
- the entry point is clean
- the bootstrap owns wiring
- the file subsystem is split by responsibility
- shared logging and exceptions are centralized

The main remaining work is about finishing the sync story, adding tests, and keeping the API behavior well documented for other developers.
