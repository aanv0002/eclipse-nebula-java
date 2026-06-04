# REQ3 Feature Proposal: Eclipse Weather System

## The Pitch

The Eclipse Weather System introduces unstable moon-wide weather events that periodically alter the battlefield.

Each weather event performs its own targeting logic, spawns a unique environmental anomaly, and triggers a corresponding effect that changes actors, items, terrain, movement or statuses.

---

## The Mechanics

- A `WeatherController` periodically triggers weather events during the game loop.
- Each `WeatherEvent` scans the map and chooses affected locations.
- Each `WeatherEvent` spawns a unique anomaly ground.
- Each `WeatherEvent` triggers its corresponding `WeatherEffect`.
- Each `WeatherEffect` applies complex secondary consequences.
- The system interacts with actors, grounds, items, statuses, movement and environmental hazards.

---

## Crystal Bloom Weather

### Weather Event

`CrystalBloomWeather`

Responsibilities:

- Scans exposed and enterable tiles.
- Selects valid crystal bloom locations.
- Spawns `SolarCrystal` grounds.
- Triggers `CrystalBloomEffect`.

### Weather Effect

`CrystalBloomEffect`

Behaviour:

- Causes SolarCrystals to spread to nearby valid tiles.
- When stepped on, SolarCrystal explodes.
- Explosion damages nearby actors.
- Workers affected by the explosion receive `BlindStatus`.
- `BlindStatus` forces random movement on the worker’s next turn.

---

## Fungal Bloom Weather

### Weather Event

`FungalBloomWeather`

Responsibilities:

- Scans damp or fertile areas (nearby puddles).
- Selects valid fungal bloom locations.
- Spawns `SporeNest` grounds.
- Triggers `FungalBloomEffect`.

### Weather Effect

`FungalBloomEffect`

Behaviour:

- SporeNest incubates for several turns.
- After incubation, it releases `Spores` and disappears.
- `Spores` mutate nearby `Apple` into `MoldedApple`.
- `Spores` mutate nearby `Cookies` into `MoldedCookies`.
- Molded consumables apply `RootStatus` when consumed.
- `RootStatus` prevents the actor from moving for the next turn.

---

## Gravity Surge Weather

### Weather Event

`GravitySurgeWeather`

Responsibilities:

- Scans the map for actor-heavy or item-heavy zones.
- Selects gravity centre locations.
- Spawns `GravityAnchor` grounds.
- Triggers `GravitySurgeEffect`.

### Weather Effect

`GravitySurgeEffect`

Behaviour:

- Pulls nearby actors toward the GravityAnchor.
- Pulls loose ground items toward the GravityAnchor.
- Relocates actors and items dynamically.

---

## Architecture

### New Abstraction 1: `WeatherEvent`

`WeatherEvent` represents a weather event that targets locations and spawns an anomaly.

Concrete implementations:

- `CrystalBloomWeather`
- `FungalBloomWeather`
- `GravitySurgeWeather`

### New Abstraction 2: `WeatherEffect`

`WeatherEffect` represents the secondary consequence caused by the spawned anomaly.

Concrete implementations:

- `CrystalBloomEffect`
- `FungalBloomEffect`
- `GravitySurgeEffect`

---

## New Classes and Supporting Classes

### Required New Abstractions

- `WeatherEvent`
- `WeatherEffect`

### Required Concrete Classes

- `CrystalBloomWeather`
- `FungalBloomWeather`
- `GravitySurgeWeather`
- `CrystalBloomEffect`
- `FungalBloomEffect`
- `GravitySurgeEffect`

### Supporting Ground Classes

- `SolarCrystal`
- `SporeNest`
- `GravityAnchor`

### Supporting Actor Classes

- `Spores`

### Supporting Item Classes

- `MoldedApple`
- `MoldedCookies`

### Supporting Status Classes

- `BlindStatus`
- `RootStatus`

### Higher-Level Class

- `WeatherController`

