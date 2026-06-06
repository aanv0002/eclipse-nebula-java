# REQ3 Feature Proposal: Eclipse Weather System

## The Pitch

The Eclipse Weather System introduces unstable moon-wide weather events that periodically alter the battlefield.

The system consists of three weather events: Crystal Bloom, Fungal Bloom and Gravity Surge. Each weather event creates a unique anomaly that remains in the world and continues affecting the game state after the weather event has ended.

---

## The Mechanics

* A `WeatherController` periodically triggers weather events during the game loop every 10 turns.
* Each `WeatherEvent` scans the map and determines suitable target locations and spawns a unique anomaly ground.
* Each anomaly remains active for several turns and continues affecting the surrounding environment.
* Each weather event triggers a corresponding `WeatherEffect`.
* Weather effects interact with multiple game components including:

    * actors,
    * grounds,
    * statuses,
    * consumable items,
    * movement,
    * loose ground items.

---

# Crystal Bloom Weather

## Weather Event

### `CrystalBloomWeather`

Responsibilities:

* Scans the map for locations near actors.
* Selects nearby unoccupied locations.
* Spawns `SolarCrystal` grounds (`*`).
* Triggers `CrystalBloomEffect`.

### Spawned Anomaly

### `SolarCrystal` (`*`)

Represents unstable solar crystals that spread across the terrain.

Behaviour:

* Has a chance to spread to nearby valid tiles (even walls).
* Explodes when stepped on by an actor.
* Damages nearby actors in an area-of-effect blast.
* Applies `BlindStatus` to affected actors.
* Disappears after exploding.

## Weather Effect

### `CrystalBloomEffect`

Responsibilities:

* Activates newly created `SolarCrystal` grounds after they are spawned by `CrystalBloomWeather`.
* Displays that SolarCrystals have begun pulsing and may spread on later turns.
* The actual spreading is handled by `SolarCrystal` during later ground ticks.

## Status Effect

### `BlindStatus`

Behaviour:

* Applied by SolarCrystal explosions.
* Causes affected workers to lose directional control.
* The worker performs a random movement instead of a chosen movement action.

---

# Fungal Bloom Weather

## Weather Event

### `FungalBloomWeather`

Responsibilities:

* Searches for damp environments near puddles.
* Selects suitable fungal growth locations.
* Spawns `SporeNest` grounds (`Ⓢ`).
* Triggers `FungalBloomEffect`.

### Spawned Anomaly

### `SporeNest` (`Ⓢ`)

Represents a growing fungal colony.

Behaviour:

* Incubates for several turns after spawning.
* Tracks an incubation timer.
* Releases a single spores after incubation completes.
* Removes itself after releasing spores.
* Restores the original ground beneath it.

## Weather Effect

### `FungalBloomEffect`

Responsibilities:

* Activates newly created `SporeNest` grounds after they are spawned by `FungalBloomWeather`.
* Does not release `Spores` immediately during the weather event.
* Displays that SporeNest grounds have started incubating.
* The actual spore release is handled by `SporeNest` after its incubation timer reaches zero.

## Spawned Actor

### `Spores` (`웃`)

Represents living fungal spores released by a SporeNest.

Behaviour:

* Searches surrounding tiles for moldable food.
* Mutates nearby Apples into Molded Apples.
* Mutates nearby Cookies into Molded Cookies.
* Disappears after completing its mutation cycle.

## Mutated Consumables

### `MoldedApple` (`õ`)

Behaviour:

* Created when spores mutate an Apple.
* Can still be consumed by workers.
* Applies `RootStatus` when consumed.

### `MoldedCookies` (`ĉ`)

Behaviour:

* Created when spores mutate Cookies.
* Can still be consumed by workers.
* Applies `RootStatus` when consumed.

## Status Effect

### `RootStatus`

Behaviour:

* Applied by MoldedApple and MoldedCookies.
* Prevents movement for 3 turns but still able to do actions.
* Causes the affected worker to lose their next movement opportunity.

---

# Gravity Surge Weather

## Weather Event

### `GravitySurgeWeather`

Responsibilities:

* Scans the map for areas containing loose items.
* Randomly selects one nearby locations as gravity centres.
* Spawns `GravityAnchor` grounds (`֍`).
* Triggers `GravitySurgeEffect` to activate the spawned anchor.

### Spawned Anomaly

### `GravityAnchor` (`֍`)

Represents a temporary gravitational anomaly.

Behaviour:

* Stabilises on the map on its first ground tick
* Pulls nearby actors and loose ground items toward itself.
* Changes actor positioning and item placement.
* Restores the original ground when the anomaly expires.

## Weather Effect

### `GravitySurgeEffect`

Responsibilities:

* Activates newly spawned `GravityAnchor` grounds.
* Displays that the anchors are stabilising.
* Leaves the actual pulling behaviour to `GravityAnchor` on its later ground tick.

---

# Architecture

## New Abstraction 1: `WeatherEvent`

Represents a moon-wide weather event.

Responsibilities:

* Select target locations.
* Spawn anomaly grounds.
* Trigger corresponding weather effects.

Concrete implementations:

* `CrystalBloomWeather`
* `FungalBloomWeather`
* `GravitySurgeWeather`

---

## New Abstraction 2: `WeatherEffect<T>`

Represents the secondary environmental consequence caused by a weather event.

Responsibilities:

* Operate on anomaly objects created by weather events.
* Modify the game state through movement, statuses, item mutation, terrain effects, or anomaly activation.

Concrete implementations:

* `CrystalBloomEffect`
* `FungalBloomEffect`
* `GravitySurgeEffect`

---

# New Classes and Supporting Classes

## Required New Abstractions

### `WeatherEvent`

Implemented by:

* `CrystalBloomWeather`
* `FungalBloomWeather`
* `GravitySurgeWeather`

### `WeatherEffect<T>`

Implemented by:

* `CrystalBloomEffect`
* `FungalBloomEffect`
* `GravitySurgeEffect`

---

## Supporting Ground Classes

* `SolarCrystal` (`*`)
* `SporeNest` (`Ⓢ`)
* `GravityAnchor` (`֍`)

---

## Supporting Actor Classes

* `Spores` (`웃`)

---

## Supporting Item Classes

* `MoldedApple` (`õ`)
* `MoldedCookies` (`ĉ`)

---

## Supporting Status Classes

* `BlindStatus`
* `RootStatus`

---

## Supporting Abstractions

* `Moldable`

Implemented by:

* `Apple`
* `Cookies`

---

## Higher-Level Class

### `WeatherController`

Responsibilities:

* Stores all available weather events.
* Cycles through weather events periodically.
* Triggers weather events during gameplay.
* Integrates the weather system into EclipseNebula.
