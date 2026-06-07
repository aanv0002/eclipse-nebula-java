Here's a cleaner README version without the Java code snippets or boxed code sections:

---

# REQ4 Feature Proposal

## Overview

This proposal introduces two interconnected systems to expand environmental interaction, strategic planning, and hazard management within the moon facility:

1. **Automated Security Hazard System** – malfunctioning corporate defence infrastructure that dynamically alters terrain, applies status effects, destroys items, and manipulates actors.
2. **Countermeasure Deployment System** – worker-operated tools designed to neutralise hazards, reclaim corrupted terrain, and establish temporary defensive structures.

Together, these systems create emergent gameplay through terrain mutation, area-of-effect calculations, status management, item interactions, and polymorphic architecture.

---

# Feature 1: Automated Security Hazard System

## The Pitch

The moon facility's obsolete automated defence network has become unstable, identifying both workers and biological entities as unauthorised threats. Security hazards are distributed throughout the map and respond to nearby activity through environmental destruction, terrain corruption, status effects, and spatial manipulation.

## Core Mechanics

### Hazard Monitoring

* Hazards continuously monitor nearby coordinates.
* Hazards may exist as actors, ground tiles, or items.

### Environmental Mutation

* Hazard activation can permanently alter terrain.
* Terrain changes remain until explicitly removed.

### Cross-System Interactions

Hazards interact with multiple game systems simultaneously:

* Actors
* Items
* Ground tiles
* Status effects
* Movement mechanics
* Area-of-effect calculations

---

## Abstraction: SecurityHazard

### Responsibility

Represents any defensive infrastructure component capable of monitoring nearby coordinates and executing hazard effects.

### Contract Methods

* `disarm(Location location)`
* `isActive()`

### Implementations

#### SpikeDrone

**Type:** Extends Actor, implements SecurityHazard

A stationary security mine that scans all eight adjacent tiles each turn. If a worker is detected, it detonates.

**Detonation Effects**

* Applies CorrodedStatus for 10 turns.
* Removes all loose items on its tile.
* Converts its tile into AcidPool.
* Removes itself from the map.

The drone only reacts to workers and ignores creatures or other actors.

---

#### StaticPylon

**Type:** Extends Ground, implements SecurityHazard

An embedded high-voltage defence pillar.

Every 8 turns, it fires an electrical arc in the direction of its first listed exit. The arc travels three tiles in a straight line.

**Arc Effects**

* Converts affected tiles into ChargedFloor.
* Applies StunnedStatus to actors standing on affected tiles.

**Disarm Effect**

* Restores its tile to a standard Floor.

---

#### GravityCore

**Type:** Extends Item, implements SecurityHazard

A portable piece of industrial scrap weighing 15 units.

When an actor enters within one tile of the core, it implodes.

**Implosion Effects**

* Pulls actors within a radius of three tiles one step toward the centre.
* Pulls loose items within the same radius one step toward the centre.
* Movement only occurs if destination tiles are valid and unoccupied.

After activation, the core removes itself from the map and becomes inactive.

---

# Feature 2: Countermeasure Deployment System

## The Pitch

Workers can purchase and deploy advanced field equipment to counter the malfunctioning defence network.

These tools provide hazard neutralisation, terrain restoration, and tactical battlefield control.

## Core Mechanics

### Tactical Defusal

Workers spend a turn disabling nearby security infrastructure.

### Environmental Reclamation

Corrupted terrain can be restored to safe floor tiles.

### Temporary Defences

Workers may deploy barriers that damage hostile entities.

---

## Abstraction: GridCountermeasure

### Responsibility

Represents carried utility equipment capable of interacting with hazards or modifying terrain behaviour.

### Contract Methods

* `getCountermeasureType()`
* `allowableActions(Actor actor, GameMap map)`

### Implementations

#### DefuseKit

**Type:** Extends Item, implements GridCountermeasure
**Weight:** 2

Generates a DefuseSecurityAction for every adjacent active hazard.

The kit searches all engine layers:

* Ground layer
* Actor layer
* Item layer

When used:

* Calls `SecurityHazard.disarm()` without triggering the hazard.
* Spawns a SecuritySalvage reward item.

Supported hazards:

* SpikeDrone
* StaticPylon
* GravityCore

No concrete type checks are required.

---

#### PurgeCanister

**Type:** Extends Item, implements GridCountermeasure
**Weight:** 4

Always provides a PurgeTerrainAction.

When activated:

* Scans all tiles within radius one of the worker.
* Includes the worker's current tile.
* Identifies terrain implementing PurgeableTerrain.
* Restores affected terrain to Floor.

The canister is consumed after use.

Supported terrain:

* AcidPool
* ChargedFloor

---

#### LaserBarrier

**Type:** Extends Item, implements GridCountermeasure
**Weight:** 6

Generates a DeployBarrierAction for each adjacent passable and unoccupied floor tile.

When deployed:

* Removes itself from inventory.
* Places a LaserBarrierGround object on the chosen tile.

**Barrier Effect**
When a hostile creature enters:

* Deals 50 damage.
* Converts into Puddle terrain.

Targets include actors with:

* CREATURE capability
* HOSTILE_TO_WORKER capability

---

# Supporting Classes

The following supporting classes provide the implementation details required by the primary systems.

## Terrain

### AcidPool

Extends Ground and implements PurgeableTerrain.

* Spawned by SpikeDrone.
* Destroys loose items on its tile each tick.
* Restored to Floor through the purge contract.

### ChargedFloor

Extends Ground and implements PurgeableTerrain.

* Spawned by StaticPylon.
* Applies StunnedStatus to actors occupying the tile.
* Restored to Floor through the purge contract.

### LaserBarrierGround

Extends Ground.

* Created by LaserBarrier.
* Passable by all actors.
* Deals 50 damage to hostile targets.
* Converts into Puddle after activation.

---

## Status Effects

### CorrodedStatus

Implements Status.

Applied by SpikeDrone.

**Duration:** 10 turns

Effects:

* Reduces carrying capacity to 30.
* Stores the original limit.
* Restores the original limit when expired.

### StunnedStatus

Implements Status.

Applied by:

* StaticPylon
* ChargedFloor

**Duration:** 1 turn

Effects:

* Prevents actions during playTurn().
* Forces DoNothingAction.

---

## Actions

### DefuseSecurityAction

Generated by DefuseKit.

* Calls the hazard's disarm method.
* Spawns SecuritySalvage on success.

### PurgeTerrainAction

Generated by PurgeCanister.

* Scans a 3×3 area centred on the worker.
* Purges all PurgeableTerrain.
* Consumes the canister.

### DeployBarrierAction

Generated by LaserBarrier.

* Removes the barrier from inventory.
* Places LaserBarrierGround.

---

## Items

### SecuritySalvage

A lightweight reward item produced when a hazard is successfully disarmed.

**Weight:** 1

---

## Interface

### PurgeableTerrain

Provides a generic mechanism for terrain restoration.

Implemented by:

* AcidPool
* ChargedFloor

This allows new hazardous terrain types to be added in the future without modifying PurgeTerrainAction.

---

# Complexity Justification

This design satisfies the requirement for two independent feature sets, each supported by its own abstraction and three primary concrete classes.

The six primary classes collectively interact with:

* Actors
* Items
* Terrain
* Status systems
* Movement validation
* Area-of-effect calculations
* Inventory management
* Map mutation
* Custom actions

The design is highly extensible through abstraction-based interactions.

The DefuseKit operates solely through the SecurityHazard interface and can interact with hazards implemented as actors, ground, or items without requiring concrete type checks. Similarly, PurgeTerrainAction interacts exclusively through the PurgeableTerrain interface, allowing future hazardous terrain types to be supported without modifying existing code.

The proposal also avoids direct reuse of Assignment 2 mechanics such as infection systems, burning terrain, toxic waste, random teleportation, or forced item dropping. Instead, it introduces malfunctioning defence infrastructure and worker-operated tactical countermeasures as a distinct gameplay subsystem.
