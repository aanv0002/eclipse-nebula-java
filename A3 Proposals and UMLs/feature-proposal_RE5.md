# REQ5 - Lunar Evolution System

## The Pitch

The moon facility is influenced by unstable lunar phases retrieved from the external API. Different moon phases dynamically affect nearby creatures, causing them to evolve, mutate, hide, swarm, or enter dormant ambush states depending on environmental conditions and creature positioning.

This feature introduces adaptive enemy evolution mechanics that continuously scan the game map and modify creature behaviour, terrain, and spawning patterns in response to changing lunar energy conditions.

---

## The Mechanics

* The game retrieves the current moon phase from the API.
* Different lunar phases activate different `LunarInfluence` implementations.
* Lunar influences scan the map for valid targets and environmental conditions.
* `FullMoonInfluence` searches for nearby creature clusters and triggers colony-style evolution.
* `NewMoonInfluence` searches for hidden or enclosed areas and triggers stealth or dormant behaviour.
* `BloodMoonInfluence` searches for damaged, isolated, or hazard-adjacent creatures and triggers unstable mutations.
* Selected creatures receive an `EvolutionBehaviour` effect depending on the active moon phase.
* `ColonyEvolution` allows grouped creatures to evolve into colony variants, split into additional Slimes, and swarm nearby workers.
* `DormantEvolution` replaces normal movement with stealth or ambush behaviour until a worker enters range.
* `MutationEvolution` replaces existing creatures with mutated variants such as `MutatedSlime`, `MutatedParasite`, or `MutatedUndead`.
* Some mutations can generate `CorruptionGround` around evolved creatures.
* The feature dynamically modifies actor behaviour, creature spawning, terrain, and map state during gameplay.

---

## The Architecture

### Abstraction 1: `LunarInfluence`

**Type:** Interface or abstract class

Represents the active moon phase influence retrieved from the API. Determines which creatures and map regions are affected by lunar energy.

#### Concrete Classes

| Class                | Type |
| -------------------- | ---- |
| `FullMoonInfluence`  | New  |
| `NewMoonInfluence`   | New  |
| `BloodMoonInfluence` | New  |

---

### Abstraction 2: `EvolutionBehaviour`

**Type:** Interface or abstract class

Represents the evolution effect applied to creatures selected by a `LunarInfluence`.

#### Concrete Classes

| Class               | Type |
| ------------------- | ---- |
| `ColonyEvolution`   | New  |
| `DormantEvolution`  | New  |
| `MutationEvolution` | New  |
