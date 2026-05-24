# REQ3 Feature Proposal: Eclipse Weather System

## The Pitch

The Eclipse Weather System introduces unstable moon-wide weather events that periodically alter the battlefield. Instead of weather being purely cosmetic, each weather event performs its own environmental targeting logic and interacts with actors, terrain, items, and hazards across the map.

The feature includes three weather events: Solar Storm, Acid Rain, and Gravity Surge. Each weather event determines where and how the weather strikes, while separate weather effects apply environmental consequences such as creating Fire, corrupting terrain into ToxicWaste, poisoning actors, and relocating loose items across the map.

---

## The Mechanics

- A `WeatherController` periodically triggers weather events during the game loop.
- Weather events are applied to target moon maps such as `20-Overflow`.
- Each weather event contains its own environmental targeting logic and determines:
    - where the weather occurs,
    - what areas are affected,
    - and which weather effect should be applied.
- Weather effects apply the actual environmental consequence after a weather event selects its target locations.
- Weather events and effects interact with multiple game components, including:
    - actors,
    - ground tiles,
    - statuses,
    - loose items.

### Solar Storm

- `SolarStormWeather` scans exposed and enterable areas of the map.
- It prioritises areas containing workers and generates multiple solar flare strike locations.
- Selected locations trigger `IgniteExposedTilesEffect`.

`IgniteExposedTilesEffect`:
- creates `Fire` on affected tiles,
- spreads burning effects into surrounding tiles,
- applies `BurningStatus` to actors caught within affected areas.

### Acid Rain

- `AcidRainWeather` scans open areas and determines corrosion zones.
- It targets terrain and actors exposed to the environment.
- Selected locations trigger `CorrodeTerrainEffect`.

`CorrodeTerrainEffect`:
- converts safe terrain into `ToxicWaste`,
- applies `PoisonStatus` to actors,
- affects organic consumables such as Apples and Cookies.

### Gravity Surge

- `GravitySurgeWeather` selects a gravity centre on the map.
- It scans the entire map for loose ground items.
- Selected items trigger `GravityDisplacementEffect`.

`GravityDisplacementEffect`:
- relocates loose items toward or around the gravity centre,
- changes item accessibility,
- forces workers to adapt their movement route.

- Weather interactions are complex because they manipulate terrain, hazards, statuses, items and actor positioning instead of only modifying numerical values.
- Weather events are deterministic or controller-driven so they can be consistently tested and demonstrated.

---

## The Architecture

### New Abstraction 1: `WeatherEvent`

`WeatherEvent` represents a weather event occurring on a moon.  
Each weather event contains its own targeting logic and determines where the weather strikes before applying an environmental consequence.

Concrete classes implementing `WeatherEvent`:

1. `SolarStormWeather` — **new class**
    - Scans exposed actor-heavy areas and generates solar flare strike locations.

2. `AcidRainWeather` — **new class**
    - Scans exposed terrain regions and determines corrosion zones affecting actors, terrain and consumables.

3. `GravitySurgeWeather` — **new class**
    - Selects a gravity centre and scans the map for loose ground items to be affected.

---

### New Abstraction 2: `WeatherEffect`

`WeatherEffect` represents the environmental consequence caused by a weather event.  
Each effect manipulates the game state by interacting with terrain, actors, statuses, hazards or items.

Concrete classes implementing `WeatherEffect`:

1. `IgniteExposedTilesEffect` — **new class**
    - Creates `Fire`, spreads burning effects and applies `BurningStatus`.

2. `CorrodeTerrainEffect` — **new class**
    - Converts terrain into `ToxicWaste`, applies `PoisonStatus`, and affects organic consumables.

3. `GravityDisplacementEffect` — **new class**
    - Relocates loose ground items toward or around a gravity surge centre.

