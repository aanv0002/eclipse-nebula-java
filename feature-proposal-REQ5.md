# REQ5 Feature Proposal: Lunar Evolution System

## The Pitch

The Lunar Evolution System connects the game to an external moon phase API and uses the returned lunar phase data to change creature behaviour inside the Eclipse Nebula. Instead of the API only printing information to the console, the moon phase directly alters the game state by evolving creatures, spawning new actors, changing enemy behaviour, and corrupting terrain.

The system includes three lunar influences: Full Moon, New Moon, and Blood Moon. Each influence scans the map for different creature or terrain conditions, then applies a matching evolution behaviour that changes how enemies act during gameplay.

---

## The Mechanics

* A `MoonPhaseController` periodically requests moon phase data from an external API.
* The API request is generated dynamically using game-state information, such as the current in-game day and the active moon map.
* The API response returns moon phase data, including phase name and illumination percentage.
* The phase name determines which `LunarInfluence` is activated.
* Each `LunarInfluence` scans the map for valid targets.
* Each influence then applies an `EvolutionBehaviour` to selected creatures or regions.
* Lunar evolution can affect:
    * hostile actors,
    * movement behaviour,
    * creature spawning,
    * actor replacement,
    * terrain corruption,
    * combat behaviour.

---

# Full Moon Influence

## Lunar Influence

### `FullMoonInfluence`

Responsibilities:

* Activated when the API returns a Full Moon or a high illumination value.
* Scans the map for clustered creatures, especially groups of nearby Slimes.
* Detects creature clusters by checking whether multiple hostile actors are located within a small radius.
* Selects clustered Slimes as evolution targets.
* Triggers `ColonyEvolution` on the selected cluster.

## Evolution Behaviour

### `ColonyEvolution`

Responsibilities:

* Causes selected Slimes to split into additional Slimes.
* Encourages swarming behaviour by increasing local creature density.
* Alters the map state by spawning new Slime actors near the original cluster.

Example gameplay outcome:

* If the Full Moon is active and three Slimes are close together, the colony becomes unstable.
* Each selected Slime may split into another Slime on a nearby valid tile.

---

# New Moon Influence

## Lunar Influence

### `NewMoonInfluence`

Responsibilities:

* Activated when the API returns a New Moon or a very low illumination value.
* Scans the map for creatures in dark, enclosed, or low-visibility areas.
* Prioritises creatures near walls, corners or inside rooms.
* Selects undead or parasites become hidden ambushers.
* Triggers `AmbushEvolution` on selected creatures.

## Evolution Behaviour

### `AmbushEvolution`

Responsibilities:

* Changes selected creatures into dormant ambush enemies.
* Temporarily stops normal wandering behaviour.
* Keeps evolved creatures inactive until a worker enters a nearby range.
* When a worker comes close, the creature becomes aggressive and begins attacking actor with more damange.

Example gameplay outcome:

* During a New Moon, a creature inside an enclosed room becomes dormant.
* It does not wander normally while dormant.
* When a worker enters its detection range, it wakes up and becomes aggressive.

---

# Blood Moon Influence

## Lunar Influence

### `BloodMoonInfluence`

Responsibilities:

* Activated when the API returns a Blood Moon, high instability value, or a phase interpreted as dangerous by the game.
* Scans the map for isolated creatures.
* Selects unstable creatures as mutation targets.
* Triggers `MutationEvolution` on selected creatures.

## Evolution Behaviour

### `MutationEvolution`

Responsibilities:

* Change selected creatures with a single stronger `MutatedCreature`.

* `MutatedCreature` has higher health and aggressive worker-chasing behaviour.
* `MutatedCreature` spreads `CorruptionGround` on the ground when walking.
* `CorruptionGround` weakens actors standing on it, causing them to receive 50% more damage from attacks.

Example gameplay outcome:

* During a Blood Moon, an isolated Undead near toxic terrain becomes unstable.
* The normal Undead is removed and replaced by a `MutatedUndead`.
* Ground tiles are converted into `CorruptionGround` when `MutatedUndead` is walking.

---

# Required Architecture

## New Abstraction 1: `LunarInfluence`

`LunarInfluence` represents the moon phase influence selected from the API response.

Responsibilities:

* Interpret the active lunar phase.
* Scan the map for suitable targets.
* Decide which creatures or areas should evolve.
* Trigger the appropriate `EvolutionBehaviour`.

Concrete implementations:

### `FullMoonInfluence` — new class

* Searches for clustered Slimes or grouped hostile creatures.
* Targets creature-heavy areas.
* Triggers `ColonyEvolution` to increase enemy density.

### `NewMoonInfluence` — new class

* Searches for creatures in dark or enclosed areas.
* Targets creatures suitable for stealth behaviour.
* Triggers `AmbushEvolution` to create dormant ambush enemies.

### `BloodMoonInfluence` — new class

* Searches for isolated creatures.
* Targets unstable creatures for mutation.
* Triggers `MutationEvolution` to replace them with mutated variants and corrupt nearby terrain.

---

## New Abstraction 2: `EvolutionBehaviour`

`EvolutionBehaviour` represents the actual evolution effect applied to targets selected by a `LunarInfluence`.

Responsibilities:

* Modify creature behaviour.
* Spawn new creatures.
* Replace existing actors.
* Modify terrain.
* Apply aggressive, dormant, colony, or mutation effects.

Concrete implementations:

### `ColonyEvolution` — new class

* Used by `FullMoonInfluence`.
* Spawns additional Slimes near clustered Slimes.
* Increases the number of hostile actors on the map.

### `AmbushEvolution` — new class

* Used by `NewMoonInfluence`.
* Changes selected creatures into dormant ambush enemies.
* Prevents normal roaming until a worker enters range.
* Causes the creature to become aggressive once triggered.

### `MutationEvolution` — new class

* Used by `BloodMoonInfluence`.
* Replaces selected creatures with stronger mutated versions.
* Creates `MutatedCreature`.

---

# Supporting Classes

## Higher-Level Classes

### `MoonPhaseController`

Responsibilities:

* Runs periodically during the game loop.
* Requests moon phase data through the API service.
* Chooses the correct `LunarInfluence` based on the API response.
* Applies lunar influence to the active game map.
* Depends on the `LunarInfluence` abstraction rather than concrete influence classes.

### `MoonPhaseApiService`

Responsibilities:

* Builds the dynamic API request.
* Sends the request to the external Moon Phase API.
* Parses the JSON response.
* Converts API data into a simple internal moon phase model.
* Loads the API key from an environment variable or local `.env` file.

### `MoonPhaseData`

Responsibilities:

* Stores parsed API data.
* Contains fields such as phase name, illumination percentage, date, latitude, and longitude.
* Allows the controller to choose a lunar influence without depending on raw JSON.

---

## Supporting Actor Classes

### `MutatedCreature` (`Ṃ`)

Behaviour:

* A single mutated enemy created by `MutationEvolution`.
* Replaces selected Slimes, Undead, or Parasites during Blood Moon influence.
* Has stronger health and combat ability than normal creatures.
* Chases nearby workers similarly to Undead behaviour.
* Attacks workers when adjacent.
* Spreads `CorruptionGround` when walking, making the surrounding area more dangerous.
---

## Supporting Ground Classes

### `CorruptionGround` (`¤`)

Behaviour:

* Created by `MutationEvolution`.
* Created as a trail by mutated creatures when walking.
* Applies a weakening effect to actors standing on it.
* Weakened actors receive 50% more damage from attacks.

---

## Supporting Status or Behaviour Classes

### `DormantStatus`

Behaviour:

* Applied by `AmbushEvolution`.
* Marks a creature as dormant.
* Prevents normal wandering until a worker enters range.

### `AggressiveStatus`

Behaviour:

* Applied when a dormant creature wakes up or when lunar energy makes creatures hostile.
* Causes the affected creature to prioritise chasing or attacking nearby workers.

---

# The Request

The API request is not static. It is built from current game-state variables.

Example dynamic request:

```text
https://api.astronomyapi.com/api/v2/studio/moon-phase
?latitude=-37.8136
&longitude=144.9631
&date=2026-06-05
```

Dynamic values:

* `date` is based on the current in-game day or real current date.
* `latitude` and `longitude` are selected based on the active moon map.
* `99-Deprecated` and `20-Overflow` may use different configured coordinates.
* The API response changes which `LunarInfluence` is activated.

Example mapping:

| Game State Variable | API Parameter | Purpose |
| --- | --- | --- |
| Current game day | `date` | Changes moon phase over time |
| Active map | `latitude`, `longitude` | Allows different maps to use different lunar readings |
| API phase name | internal influence selection | Chooses Full Moon, New Moon, or Blood Moon logic |
| API illumination | evolution intensity | Controls how many creatures may be affected |

---

# The Schema

Example JSON response expected from the API:

```json
{
  "data": {
    "phase": {
      "name": "Full Moon",
      "illumination": 98.7
    },
    "date": "2026-06-05",
    "observer": {
      "latitude": -37.8136,
      "longitude": 144.9631
    }
  }
}
```

Expected fields:

* `data.phase.name` — used to select the active `LunarInfluence`.
* `data.phase.illumination` — used to determine evolution strength.
* `data.date` — confirms which date the lunar phase belongs to.
* `data.observer.latitude` and `data.observer.longitude` — confirm the coordinates used for the request.

Example interpretation:

| API Value | Game Interpretation |
| --- | --- |
| `Full Moon` | Activate `FullMoonInfluence` |
| `New Moon` | Activate `NewMoonInfluence` |
| `Blood Moon` or high instability mapping | Activate `BloodMoonInfluence` |
| Illumination above 80% | Stronger evolution effect |
| Illumination below 20% | Stealth or dormant behaviour becomes more likely |


