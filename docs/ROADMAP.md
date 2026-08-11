      # Roadmap

Planned and pending work on Filizer.

> Context for each item lives in [ARCHITECTURE.md](ARCHITECTURE.md), under
> [Known limitations](ARCHITECTURE.md#known-limitations).

---

## High priority

### Finish `WatchServiceSynchronizationStrategy`

The class exists but `synchronize()` is empty. A complete implementation
needs to:

- [ ] Create and manage a single `WatchService` instance
- [ ] Register every parent directory containing managed files
- [ ] Run a dedicated background thread listening for `ENTRY_CREATE`,
      `ENTRY_MODIFY` and `ENTRY_DELETE`
- [ ] Resolve the affected `CustomFile` from the `FileRegistry`
- [ ] Reload that `CustomFile` when its backing file changes
- [ ] Shut the watcher thread down cleanly and close the `WatchService`
      on plugin disable

Once event-driven, `synchronize()` legitimately stays empty — the reload
happens from the watcher thread rather than on access.

### Support formats other than YAML

Parsing is currently hardwired to `YamlConfiguration`. Making the format a
seam rather than an assumption:

- [ ] Extract a parser abstraction behind `CustomFile` (load + save)
- [ ] Select the implementation per file, resolved from the extension,
      with an explicit override available
- [ ] Add a JSON implementation
- [ ] Decide what `FileReader` returns for formats that have no Bukkit
      `ConfigurationSection` equivalent

This is the single biggest change to the public API, so it should land
before the first stable release.

### Adopt the shared Exception module

`FilizerExceptions` and `AppLogger` solve locally what every module of the
stack needs. The dedicated **Exception** module will generalize both.

- [ ] Declare the Exception module as a dependency
- [ ] Re-parent the typed exceptions under the shared hierarchy, keeping
      `IllegalArgumentException` / `IllegalStateException` as supertypes so
      existing catch blocks keep working
- [ ] Replace `FilizerExceptions` with the shared factory, wired in
      `PluginBootstrap` — consumers should need no configuration
- [ ] Decide whether `AppLogger` is replaced by the shared logging contract
      or kept as a thin adapter over it

Blocked on the Exception module itself.

### Add a test suite

- [ ] `FileManager` — creation, lookup, deletion, idempotence
- [ ] `FileRegistry` — path normalization, name collisions
- [ ] `FilizerExceptions` — each factory method logs and builds correctly
- [ ] Lightweight integration tests over real temporary files

The design already avoids static state and Bukkit coupling to make this
possible.

### Document collision behaviour

- [x] Spell out, in the API reference, exactly what `findFile(String)`
      does when several registered files share a name

---

## Medium priority

### Lifecycle handling in `PluginBootstrap.stop()`

- [ ] Stop background/async resources
- [ ] Close watchers
- [ ] Release runtime references

Blocked on the WatchService work — until then there is nothing to close.

### Stricter validation

- [ ] Reject file names containing path separators
- [ ] Guard against traversal outside the intended root
- [ ] Validate paths before they reach `FileFactory`

`FileChecker` currently only rejects null and blank names.

### An explicit API for loading existing files

`storeAllFiles(Path)` registers everything under a root indiscriminately.
A narrower entry point for "register this file if it already exists,
without creating it" would be clearer.

---

## Low priority

- [ ] More API usage examples
- [ ] Expanded internal documentation
- [ ] Decide whether `shared/util` keeps `FileChecker` or moves it to a
      more specific package
- [ ] Thread-safety story for `CustomFile`, if concurrent access ever
      becomes a real use case

---

## Repository hygiene

- [ ] Stop tracking `bin/` — it holds Eclipse build output (`.class` files)
      and a stale `plugin.yml`, which pollutes every diff

```
git rm -r --cached bin
echo "bin/" >> .gitignore
```

---

## Build and publishing

- [x] Decide the publication target — the Gradle Plugin Portal, whose Maven
      repository at `https://plugins.gradle.org/m2/` hosts the jar
- [x] Produce `-sources` and `-javadoc` jars
- [x] Align the version between `build.gradle` and `plugin.yml`
- [x] Add a `LICENSE` file — PolyForm Perimeter 1.0.0, source-available:
      free to use and to build commercial products on, but not to
      redistribute as a competing library
- [x] Document the release procedure — kept internally, outside this
      repository
- [x] Automate publication — `.github/workflows/publish.yml` runs
      `publishPlugins` when a GitHub release is published
- [ ] Add the `GRADLE_PUBLISH_KEY` and `GRADLE_PUBLISH_SECRET` secrets in
      the repository settings, without which the workflow fails

> The `gradlePlugin { }` block and the `com.gradle.plugin-publish` plugin in
> `build.gradle` are what upload the jar to `plugins.gradle.org/m2/`. They
> look out of place for a Bukkit plugin, but removing them would stop
> publication.

---

This document is intentionally kept separate from the main
[README](../README.md) so that planning can evolve independently.
