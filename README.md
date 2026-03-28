# MaceDMGSwap

MaceDMGSwap is a small NeoForge client-side mod for Minecraft 1.21.x that boosts mace attacks by simulating fall height and can automatically swap from a sword or axe to a mace for the hit.

The project is inspired by the MaceDMG behavior found in some utility clients, but implemented here as a standalone NeoForge mod with a simple toggle and lightweight on-screen status display.

## Features

- Simulates fall height before a mace hit to increase damage.
- Automatically swaps to a mace from the hotbar when attacking with a sword or axe.
- Swaps back to the original hotbar slot after the attack.
- Includes an in-game toggle key.
- Shows a small status overlay so you can tell whether the mod is enabled.
- Works entirely on the client side.

## How It Works

When the mod is enabled and you attack a living entity:

- If you are already holding a mace, the mod sends a short fake movement packet pattern before the hit.
- If you are holding a sword or axe and a mace exists in your hotbar, the mod cancels the original input, switches to the mace, sends the fake-fall packets, performs the attack on the next client tick, and then switches back.

The fake-fall pattern is based on the same general idea used by MaceDMG implementations: the client sends movement packets that make the server believe the player fell before the hit, which increases mace damage.

## Controls

- `V`: Toggle the mod on or off

When toggled, the game log will print either `ENABLED` or `DISABLED`.

## Requirements

- Minecraft 1.21.1
- NeoForge 21.1.x
- Java 21

This repository is currently configured and tested around the 1.21.1 toolchain. If you want to target later 1.21.x NeoForge builds, additional compatibility work may be required because some client APIs changed between minor versions.

## Download

- Repository: https://github.com/nhqit/Mace-DMG-and-Auto-Swap-Mace
- Releases page: https://github.com/nhqit/Mace-DMG-and-Auto-Swap-Mace/releases
- Built jar in repository: `releases/macedmgswap-1.0.0.jar`

## Installation

1. Build the mod or download the compiled jar.
2. Put the jar into your Minecraft `mods` folder.
3. Launch Minecraft with NeoForge.
4. Join a world.
5. Press `V` to enable the mod.

## Usage

### Direct Mace Use

1. Hold a mace.
2. Enable the mod.
3. Attack a living target.

The mod will apply the fake-fall packet sequence before the hit.

### Auto Swap Mode

1. Put a mace somewhere in your hotbar.
2. Hold a sword or axe.
3. Enable the mod.
4. Attack a living target.

The mod will:

1. intercept the attack input,
2. swap to the mace,
3. send the fake-fall packets,
4. perform the attack,
5. swap back to your original weapon slot.

## Build From Source

Clone the repository and run:

```powershell
.\gradlew.bat build
```

The output jar will be generated in:

```text
build/libs/
```

## Project Structure

- `src/main/java/com/iamnhq/macedmg/MaceDmgMod.java`: Mod entry point and event registration
- `src/main/java/com/iamnhq/macedmg/MaceDmgKeyHandler.java`: Toggle key handling
- `src/main/java/com/iamnhq/macedmg/MaceDmgAttackHandler.java`: Attack interception, fake-fall logic, scheduled attack, and swap-back
- `src/main/java/com/iamnhq/macedmg/MaceDmgOverlay.java`: Simple HUD overlay

## Notes

- This is a client-side combat behavior mod. Server behavior may vary depending on server rules, plugins, validation logic, or anti-cheat systems.
- The mod is intended for environments where you understand and accept the gameplay and server-side implications.
- The implementation focuses on lightweight behavior rather than a full configuration UI.

## License

This project is released under the MIT License.