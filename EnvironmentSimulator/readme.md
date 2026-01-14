# Environment Simulator

This repository contains an extensible Java SE ecosystem simulation demonstrating OOP design, design patterns, and core Java technologies.

## Important Documents

- **Requirements:** [docs/requirements.md](docs/requirements.md)
- **Design patterns:** [docs/design-patterns.md](docs/design-patterns.md)
- **Design & technologies:** [docs/design-technologies.md](docs/design-technologies.md)
- **Tests:** [docs/test.md](docs/test.md)

## UML Diagrams

- [Class diagram](docs/uml/EnvironmentSimulator-CLASS%20DIAGRAM.drawio.png)
- [Use cases](docs/uml/EnvironmentSimulator-USE%20CASES.drawio.png)
- [Overview / BP diagram](docs/uml/EnvironmentSimulator.drawio)

## How the game works (high-level)

- The simulator runs a deterministic tick-based game loop (`template.Game.GameLoop`).
- Each tick the engine performs: resource respawn, entity movement, attacks, hunger processing, reproduction, experience assignment, and command handling.
- Entities (herbivores, carnivores, packs) are managed by an `AnimalRepository` and placed on a map built by `MapBuilder`.
- Events generated during ticks are published to observers (e.g., `RecapObserver`, `LoggingObserver`) and collected into a recap printed each tick.
- Persistence supports saving/loading snapshots (JSON and binary) via pluggable strategies.
- Commands are processed through an extensible handler chain (spawn, save, load, help, etc.).

## Run / Build

On Windows (from project root):

```powershell
.\gradlew.bat build
.\gradlew.bat test
.\gradlew.bat run
```

On macOS/Linux:

```bash
./gradlew build
./gradlew test
./gradlew run
```

Tests and reports are produced under `build/reports/tests/` and `build/test-results/`.

## Admin mode (environment variable)

Some command handlers (inspection, reflection-based tools) are only enabled in admin mode. Enable admin mode by setting the `ADMIN` environment variable before running the app.


```cmd
set ADMIN=true
.\gradlew.bat run
```

- An example for setting the `ADMIN` environment variable is included above as a quick reference (provided by the project owner).

## Commands

Below are the available commands and what each does. Most commands are interactive and will prompt for additional input; many support typing `cancel` to abort and destructive actions ask for confirmation.

- `h` / `help` — show the help listing (prints available commands).
- `c` / `continue` — finish the current turn; marks the turn finished so the loop proceeds (`ContinueCommandHandler`).
- `exit` — prompts for confirmation, then requests game shutdown (`ExitCommandHandler`).
- `create` — interactive creation of a single animal:
	- prompts: type (`carnivore`/`herbivore`) and sex (`m`/`f`);
	- spawns at a random valid position and adds to repository (`CreateCommandHandler`).
- `spawn` — interactive bulk spawn for resources or animals:
	- prompts: spawn type (`water`, `grass`, `herbivore`, `carnivore`) and amount;
	- resources update map positions; animals are created via factories at random valid positions (`SpawnCommandHandler`).
- `save` / `s` — interactive save flow:
	- prompts: file name and format (json/bin), asks confirmation;
	- builds a `GameSnapshot` and writes via `strategy.IO.SaveGame` (`SaveCommandHandler`).
- `load` / `l` — interactive load flow:
	- prompts: file name and format, asks confirmation;
	- loads snapshot via `strategy.IO.LoadGame` and applies states to `AnimalRepository` and `MapBuilder` (`LoadCommandHandler`).
- `deleteAnimal` — prompts for animal ID, asks confirmation, removes animal from repository (`DeleteAnimalCommandHandler`).
- `pack` — group animals into a pack:
	- prompts for comma-separated animal IDs and destination pack ID or `0` to create a new pack;
	- creates or updates `AnimalPack` and registers it in the repository (`PackCommandHandler`).
- `listAnimals` — prints all non-pack animals with details (ID, type, sex, position, HP, EXP, Level, Pack) (`ListAnimalsCommandHandler`).
- `listPacks` — prints all packs and their member details (`ListPacksCommandHandler`).
- `listMap` — prints map statistics (counts of water, grass, obstacles, animals) (`ListMapCommandHandler`).
- `clearAnimals` — confirmation, then removes all animals from repository (`ClearAnimalsCommandHandler`).
- `clearResources` — confirmation, then clears map resources (grass/water) (`ClearMapResourcesCommandHandler`).
- `inspect` — admin-only (annotated with `@AdminOnly`):
	- interactive reflection tools: inspect a single animal, discover/load factories, compare two animals; uses `factoryMethod.AnimalFactory.AnimalInspector` (`InspectCommandHandler`).
- Unknown/invalid input — handled gracefully by `InvalidInputCommandHandler`.

### Notes

- Many handlers support typing `cancel` during prompts to abort the operation.
- Destructive operations require confirmation to avoid accidental data loss.
- Errors and I/O failures are logged and shown as friendly messages (exception shielding).
- Admin-only handlers are registered only when the `ADMIN` environment variable is enabled (see Admin mode in this `README`).

- Windows (PowerShell session only):

```powershell
$env:ADMIN = 'true'
.\gradlew.bat run
```

- Linux / macOS:

```bash
export ADMIN=true
./gradlew run
```

Notes:
- When `ADMIN` is `true` the command chain builder registers handlers annotated with `@AdminOnly` (see `annotations/AdminOnly.java`).
- You can also toggle admin behavior by passing an appropriate startup flag if implemented in the launcher.

## Links

- Source code: `src/main/java`
- Tests: `src/test/java`
- Saved games directory: `src/data/saved/`

