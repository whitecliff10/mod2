# Everpulse

Everpulse is a global Vanilla+ progression mod for Minecraft 1.21.1 on NeoForge. The world develops a supernatural heartbeat: every few nights, one of four pulses changes the rules for players and hostile creatures.

## The four pulses

- **Verdant Pulse** — regeneration and luck spread through the world, while hostile creatures also recover.
- **Ember Pulse** — players and monsters gain fire resistance and strength.
- **Zephyr Pulse** — speed, jumping, and slow falling reshape nighttime movement; monsters become faster too.
- **Umbral Pulse** — night vision reveals the dark, but rare monsters awaken as dangerous pulse-touched champions.

Pulses begin at night, end before dawn, and rotate in a deterministic world calendar. The default interval is every third Minecraft day.

## Progression

1. Craft a **Pulse Compass** to learn the next pulse and check its remaining time.
2. Defeat hostile mobs during a pulse to collect its unique sigil.
3. During Umbral Pulses, hunt glowing **pulse-touched champions** for Champion Cores.
4. Craft a **Pulse Altar**. Offer sigils to it for long-lasting thematic blessings.
5. Unite all four sigils and a Champion Core into the powerful consumable **Worldheart**.
6. Turn spare sigils into four glowing thematic building sets.

## Content in 1.0.0

- Four global supernatural events
- Dynamically enhanced hostile mobs
- Pulse-touched champion system
- Seven progression items
- Five glowing building/ritual blocks
- Seven survival recipes
- Four advancements
- English and Russian localization
- Server configuration
- No required content libraries beyond NeoForge

## Requirements

- Minecraft Java Edition 1.21.1
- NeoForge 21.1.249 or newer in the 21.1.x line
- Java 21

## Build

With JDK 21 and Gradle 9.2.1 installed:

```bash
gradle build
```

The distributable file is `build/libs/everpulse-1.0.0.jar`. The jar task deliberately fails if compiled classes or `neoforge.mods.toml` are missing, preventing the empty-jar problem.

The included GitHub Actions workflow builds and uploads the JAR automatically.

## Edit textures

All game textures are ordinary 16x16 PNG files in:

```text
src/main/resources/assets/everpulse/textures/block/
src/main/resources/assets/everpulse/textures/item/
```

Replace their contents without changing the filenames, then rebuild. `tools/generate_assets.py` regenerates the default set and will overwrite hand-painted versions.

## Configuration

After first launch, edit `config/everpulse-common.toml`:

- `pulseIntervalDays` — event frequency, default 3 days
- `sigilDropChance` — hostile-mob sigil drop chance, default 16%
- `championChance` — Umbral champion chance, default 4.5%
- `announcePulses` — global event announcements

## License

MIT. Modpacks may include Everpulse freely.
