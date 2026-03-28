# MaceDMGSwap

MaceDMGSwap is a clean NeoForge PvP mod for Minecraft 1.21.1 that combines fake-fall mace damage with automatic weapon swapping. It is built for players who want mace-level burst damage without manually switching off a sword or axe every hit.

The mod is designed to stay fast, minimal, and practical: no bloated interface, no setup screen, and no extra conditions before it does its job.

## What It Does

When enabled, the mod boosts mace hits by sending a fake-fall packet sequence before the attack. If you are holding a sword or axe and you have a mace in your hotbar, it will automatically:

1. detect the target,
2. swap to the mace,
3. apply the fake-fall sequence,
4. perform the attack,
5. swap back to your original slot.

This gives sword and axe engagements access to mace-style burst damage with a seamless swap flow.

## Highlights

- Toggleable with keybind. Default key: `V`
- Works with sword and axe
- Detects mace only in the hotbar, slots `1-9`
- Automatic StunSlam-style flow when attacking with an axe
- Instant `swap -> attack -> swap back`
- No extra conditions or complex setup required
- Clean and lightweight implementation
- No unnecessary UI
- PvP-focused behavior

## Features

- Fake-fall mace damage boost
- Automatic hotbar mace swap
- Automatic return to the previous slot after the hit
- Direct mace support when you are already holding a mace
- Lightweight HUD status text
- Client-side keybind toggle

## How It Works

### If You Are Holding a Mace

The mod sends a short fake-fall packet pattern right before the hit so the mace attack gets increased damage.

### If You Are Holding a Sword or Axe

If a mace is found in the hotbar, the mod intercepts the attack input and runs the full swap sequence automatically. The original weapon slot is restored immediately after the hit.

### Hotbar-Only Detection

The mod only looks for a mace in the hotbar. If your mace is not in slots `1-9`, auto-swap will not trigger.

## Controls

- `V`: Enable or disable MaceDMGSwap

The mod also logs its current state in-game as `ENABLED` or `DISABLED`.

## Requirements

- Minecraft 1.21.1
- NeoForge 21.1.x
- Java 21

This repository is currently configured around the 1.21.1 NeoForge toolchain.

## Download

- Repository: https://github.com/nhqit/Mace_DMG_Swap
- Releases page: https://github.com/nhqit/Mace_DMG_Swap/releases
- Download the latest jar from the Releases page.

## Installation

1. Download the mod jar.
2. Put it in your Minecraft `mods` folder.
3. Launch Minecraft with NeoForge 1.21.1.
4. Join a world.
5. Press `V` to toggle the mod.

## Usage

### Mace Mode

1. Hold a mace.
2. Enable the mod.
3. Hit a living target.

The fake-fall sequence is applied before the attack.

### Swap Mode

1. Put a mace in the hotbar.
2. Hold a sword or axe.
3. Enable the mod.
4. Attack a living target.

The mod will swap to the mace, hit, and return to your original slot automatically.

## Build From Source

Run:

```powershell
.\gradlew.bat build
```

The generated artifacts will appear in:

```text
build/libs/
```

## Project Structure

- `src/main/java/com/iamnhq/macedmg/MaceDmgMod.java`: Mod entry point and event registration
- `src/main/java/com/iamnhq/macedmg/MaceDmgKeyHandler.java`: Keybind toggle logic
- `src/main/java/com/iamnhq/macedmg/MaceDmgAttackHandler.java`: Fake-fall, target detection, swap handling, and attack scheduling
- `src/main/java/com/iamnhq/macedmg/MaceDmgOverlay.java`: Minimal HUD status text

## Notes

- This is a client-side combat mod.
- Server behavior can vary depending on validation, anti-cheat, or PvP rules.
- The mod is intentionally minimal and does not include a config GUI.
- Use this mod only on servers or worlds where this kind of combat assistance is allowed.

## License

Released under the MIT License.