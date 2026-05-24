# REQ5 Feature Proposal: Lunar Alignment System (API Integration)

## API 

API:

FarmSense Moon Phase API

Example endpoint:

`https://api.farmsense.net/v1/moonphases/?d=TIMESTAMP`

Retrieved information:

- Moon phase
- Illumination percentage
- Moon age

The API request depends on game state.

The timestamp used in the request changes according to:

- Current game turn
- Current active moon map
- Real-world time

Different maps use different map modifiers so the API request changes dynamically during gameplay.

---

## Mechanics

The system periodically checks the moon phase.

The detected phase activates a corresponding lunar event which modifies the game world.

---

## New Moon Phase

Condition:

- Illumination between 0–10%

Effects:

- ToxicWaste spreads
- Workers receive PoisonStatus
- Corrupted areas expand
- Parasite activity increases

---

## Full Moon Phase

Condition:

- Illumination between 90–100%

Effects:

- Fire appears on terrain
- Undead gain additional strength
- Fleshy Trees accelerate growth
- Burning effects apply

---

## Gibbous Phase

Condition:

- Illumination between 50–89%

Effects:

- Puddles spawn
- Prices of item increases
- Items relocate around anomaly zones

---

## Architecture

### Abstraction 1: LunarPhase

Represents moon phases retrieved from the API.

Concrete implementations:

- NewMoonPhase
- FullMoonPhase
- GibbousPhase

Each phase determines which environmental effect becomes active.

---

### Abstraction 2: LunarEffect

Purpose:

Represents environmental changes caused by moon phases.

Concrete implementations:

- DarkMoonEffect
- FullMoonEffect
- GibbousEffect

These effects manipulate terrain, hazards, statuses and environmental behaviour.

---

## Higher-Level Classes

### LunarService

Responsibilities:

- Calls API
- Retrieves moon data
- Determines active phase

Depends on:

- LunarPhase abstraction

### LunarWorldManager

Responsibilities:

- Applies environmental effects
- Updates the game world

Depends on:

- LunarEffect abstraction

