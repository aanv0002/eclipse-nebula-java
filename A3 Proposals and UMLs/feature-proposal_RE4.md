# REQ4 Feature Proposal

---

# Feature 1: Automated Security Hazard System

## The Pitch

The moon facility's obsolete automated defence architecture has malfunctioned, registering both workers and biological entities as unauthorised threats. Stationary security hazards are distributed across the map and react to nearby movement by triggering complex area-of-effect calculations, permanent terrain mutation, item destruction, and status penalties.

## The Mechanics

- **Grid Hazard Tracking:** autonomous mechanical security units are fixed across the facility map and monitor nearby coordinate movement.
- **Complex Detonations:** activating or ignoring a security hazard trips a defensive overload subroutine that permanently alters walkable ground layouts or shifts local spatial properties.
- **Cross-Component Cascades:** triggering a trap connects actors, items, map terrain mutations, item deletion routines, and custom status penalties.

## The Architecture

### New Abstraction 1: `SecurityHazard` (interface)

**Responsibility:** Defines any structural map component or entity that monitors nearby coordinates and executes complex area-of-effect layout destruction or grid displacement.

- **`SpikeDrone` [NEW]**
  - **Type:** extends `Actor`, implements `SecurityHazard`.
  - **Complex Effect:** Acts as a stationary security mine on the floor grid. Because actors cannot share a tile in the engine, the drone triggers when a worker moves adjacent to it or attempts to move into its occupied coordinate. It then explodes, removes itself from the map, permanently mutates its ground tile into `AcidPool`, vaporises loose items on that coordinate, and applies `CorrodedStatus` to the worker. `CorrodedStatus` reduces the worker's maximum carrying weight capacity from 50 units to 30 units for 10 turns.

- **`StaticPylon` [NEW]**
  - **Type:** extends `Ground`, implements `SecurityHazard`.
  - **Complex Effect:** A high-voltage structural pillar. Every 8 game turns, it shoots an electrical arc down a 3-tile linear path, permanently converting standard floor tiles in that path into `ChargedFloor` terrain and applying `StunnedStatus` for 1 turn to any actor caught inside the path.

- **`GravityCore` [NEW]**
  - **Type:** extends `Item`, implements `SecurityHazard`.
  - **Complex Effect:** Sits on the floor as heavy industrial scrap. During its ground tick, if an actor is within 1 tile of its coordinate, it implodes and performs a 3-tile-radius area-of-effect gravity calculation. All loose floor items and actors within the radius are dragged exactly 1 coordinate tile closer to the epicentre if the destination is valid.

---

# Feature 2: Countermeasure Deployment System

## The Pitch

To navigate the rogue defence architecture, workers can purchase and deploy high-tech field tools. These countermeasure modules allow workers to spend turns actively disarming security hardware, reclaiming corrupted terrain, or erecting temporary tactical barriers that alter movement and creature behaviour.

## The Mechanics

- **Tactical Defuses:** workers can spend an active game turn using specialised countermeasure tools to rewrite the safety flags of adjacent hostile corporate infrastructure.
- **Environmental Reclamation:** handled modules can wipe active hazardous terrain structures and restore coordinates back into safe walkways.
- **Perimeter Defence:** deployed components project active laser barriers that intercept oncoming threats and then convert into inert ground remnants.

## The Architecture

### New Abstraction 2: `GridCountermeasure` (interface)

**Responsibility:** Governs operational modules or utility tools carried or deployed by the worker to intercept structural threats or rewrite terrain rules.

- **`DefuseKit` [NEW]**
  - **Type:** extends `Item`, implements `GridCountermeasure`.
  - **Complex Effect:** A high-tech defusing tool used through `DefuseSecurityAction` when standing adjacent to an active `SpikeDrone` or `StaticPylon`. It cleanly disarms the security hardware, removes it from the map without triggering its hazard effect, and dynamically spawns a stable, valuable salvage scrap item on the floor as a reward.

- **`PurgeCanister` [NEW]**
  - **Type:** extends `Item`, implements `GridCountermeasure`.
  - **Complex Effect:** A consumable spray item used through `PurgeTerrainAction`. When activated, it releases a neutralising chemical mist across a 3x3 square radius, deletes active hazardous terrain fields such as `AcidPool` or `ChargedFloor`, and restores those coordinates back to clean `Floor` tiles.

- **`LaserBarrier` [NEW]**
  - **Type:** extends `Item`, implements `GridCountermeasure`.
  - **Complex Effect:** A carried deployment kit used through `DeployBarrierAction`. When deployed, it is removed from the worker's inventory and places a `LaserBarrierGround` object on a selected adjacent standard floor tile. Any hostile creature that attempts to cross the barrier is intercepted, heavily damaged or destroyed if weak enough, and the barrier burns out into harmless `Puddle` ground.

---

## Supporting Classes Directory

The following supporting classes are explicitly added to execute the data payloads and conditions generated by the six primary compliance classes listed above. These do not count towards the six concrete requirement classes.

1. **`AcidPool`** (extends `Ground`)
   - A melted terrain type spawned by `SpikeDrone`.
   - Dissolves loose items on its coordinate and applies item-handling penalties instead of copying Toxic Waste's simple damage-over-time behaviour.

2. **`ChargedFloor`** (extends `Ground`)
   - A static-filled floor tile spawned by `StaticPylon`.
   - Shocks traversing actors and can apply `StunnedStatus`.

3. **`LaserBarrierGround`** (extends `Ground`)
   - The deployed field version of `LaserBarrier`.
   - Intercepts hostile creatures attempting to cross it, then burns out into `Puddle`.

4. **`CorrodedStatus`** (implements `Status`)
   - A custom condition attached to an actor.
   - Temporarily reduces maximum carrying weight capacity from 50 units to 30 units for 10 turns.

5. **`StunnedStatus`** (implements `Status`)
   - A short status that causes affected actors to lose their next action.
   - Actors check for this status during `playTurn()` and return `DoNothingAction` while stunned.

6. **`DefuseSecurityAction`** (extends `Action`)
   - Uses a carried `DefuseKit` to disarm adjacent security hazards.

7. **`PurgeTerrainAction`** (extends `Action`)
   - Uses a carried `PurgeCanister` to restore hazardous terrain in a 3x3 area.

8. **`DeployBarrierAction`** (extends `Action`)
   - Uses a carried `LaserBarrier` to place `LaserBarrierGround` on a valid adjacent floor tile.

9. **`SecuritySalvage`** (extends `Item`)
   - A valuable reward item spawned after successful defusing.

---

## Complexity Justification

This requirement contains two separate features, each with its own abstraction and three concrete classes. The six concrete classes each produce complex effects involving multiple parts of the game state: actors, items, statuses, ground tiles, movement validation, area-of-effect calculations, item deletion, map mutation, and custom actions.

The design avoids directly repeating Assignment 2 mechanics such as infection, burning terrain, Toxic Waste, random teleportation, and forced item dropping. The Automated Security Hazard System focuses on malfunctioning corporate infrastructure, while the Countermeasure Deployment System focuses on player-controlled tactical tools that neutralise or reshape those hazards.
