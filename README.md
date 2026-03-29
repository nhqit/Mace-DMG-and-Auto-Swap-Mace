<table>
	<tr>
		<td>
			<img src="logo.jpg" alt="MaceDMGSwap Logo" width="84" />
		</td>
		<td>
			<h1>Mace DMG Swap</h1>
		</td>
	</tr>
</table>

Mace DMG Swap is a lightweight NeoForge combat mod for Minecraft 1.21.1.
It combines two ideas into one fast workflow:

- Wurst-style mace damage spoofing (fake fall packets)
- Automatic swap from sword or axe to mace and back

The goal is simple: keep your normal weapon in hand, but still land mace-style burst damage when conditions are met.

## Key Features

- Toggleable with a keybind. Default key is `V`
- Works with sword and axe auto-swap logic
- Detects mace in hotbar slots 1 to 9
- Sends Wurst-style fake-fall packet sequence for mace damage boost
- Instant flow: swap -> fake fall -> hit -> swap back
- Clean implementation with minimal overhead
- Small status overlay, no heavy UI
- PvP-oriented behavior

## Current Behavior

When the mod is enabled and you attack a living target:

1. If you already hold a mace, it applies fake-fall packets before the hit.
2. If you hold a sword or axe and a mace is in the hotbar, it swaps to mace, applies fake-fall packets at hit timing, attacks, then swaps back.

The packet sequence follows the Wurst-style pattern:

- `0`
- `0`
- `0`
- `0`
- `sqrt(500)`
- `0`

## Controls

- `V` toggles MaceDMGSwap on and off

The mod logs state changes as `ENABLED` and `DISABLED`.

## Requirements

- Minecraft 1.21.1
- NeoForge 21.1.x
- Java 21

## Download

- Repository: https://github.com/nhqit/Mace_DMG_Swap
- Releases: https://github.com/nhqit/Mace_DMG_Swap/releases

Download the latest jar from the Releases page.

## Installation

1. Download the latest jar.
2. Place it in your Minecraft `mods` folder.
3. Start Minecraft with NeoForge 1.21.1.
4. Join a world.
5. Press `V` to enable the mod.

## Quick Usage

Direct mace usage:

1. Hold a mace.
2. Attack a living target.

Auto-swap usage:

1. Put a mace in hotbar slots 1 to 9.
2. Hold a sword or axe.
3. Attack a living target.

## Build From Source

```powershell
.\gradlew.bat build
```

Build output is generated in:

```text
build/libs/
```

## Project Structure

- `src/main/java/com/iamnhq/macedmg/MaceDmgMod.java` mod entry point and event registration
- `src/main/java/com/iamnhq/macedmg/MaceDmgKeyHandler.java` keybind toggle handling
- `src/main/java/com/iamnhq/macedmg/MaceDmgAttackHandler.java` fake-fall, swap, and attack timing logic
- `src/main/java/com/iamnhq/macedmg/MaceDmgOverlay.java` in-game status overlay

## Notes

- This is a client-side combat mod.
- Behavior may vary across servers due to anti-cheat and movement validation.
- Use only where this type of mod is allowed.

## License

Released under the MIT License.