# Commands

Filizer provides a small set of commands for interacting with and debugging
registered configuration files.

> This page lists the commands currently available in Filizer.
> More commands may be added as the project evolves.

---

## `/check`

Inspect the contents of a registered configuration file.

### Usage

```text
/check <file>
```

### Arguments

| Argument | Required | Description |
|----------|----------|-------------|
| `<file>` | Yes | The name of the registered file to inspect. |

### Example

```text
/check config.yml
```

The command displays the keys and values stored in the file:

```text
The file contain key: database.host with the value: localhost
The file contain key: database.port with the value: 3306
The file contain key: debug with the value: true
```

Configuration sections themselves are not displayed.

---

## More commands

Additional commands may be added in future versions of Filizer.

This document is intentionally kept separate from the main
[README](../README.md) so that command documentation can evolve independently.
