# FIT2099 Assignment (Semester 1, 2026)

```
 _______  _______  ______    _______  _______  _______  _______                          
|       ||   _   ||    _ |  |  _    ||   _   ||       ||       |                         
|    ___||  |_|  ||   | ||  | |_|   ||  |_|  ||    ___||    ___|                         
|   | __ |       ||   |_||_ |       ||       ||   | __ |   |___                          
|   ||  ||       ||    __  ||  _   | |       ||   ||  ||    ___|                         
|   |_| ||   _   ||   |  | || |_|   ||   _   ||   |_| ||   |___                          
|_______||__| |__||___|  |_||_______||__| |__||_______||_______|                         
 _______  _______  ___      ___      _______  _______  _______  ___   _______  __    _   
|       ||       ||   |    |   |    |       ||       ||       ||   | |       ||  |  | |  
|       ||   _   ||   |    |   |    |    ___||       ||_     _||   | |   _   ||   |_| |  
|       ||  | |  ||   |    |   |    |   |___ |       |  |   |  |   | |  | |  ||       |  
|      _||  |_|  ||   |___ |   |___ |    ___||      _|  |   |  |   | |  |_|  ||  _    |  
|     |_ |       ||       ||       ||   |___ |     |_   |   |  |   | |       || | |   |  
|_______||_______||_______||_______||_______||_______|  |___|  |___| |_______||_|  |__|  
 ___   __    _  _______                                                                  
|   | |  |  | ||       |                                                                 
|   | |   |_| ||       |                                                                 
|   | |       ||       |                                                                 
|   | |  _    ||      _| ___                                                             
|   | | | |   ||     |_ |   |                                                            
|___| |_|  |__||_______||___|                                                                                                                                                                                                                         
```
---
# REQ5 details:

The **Anomalous Mannequin** is a deterministic stateful creature with four distinct states:

- IdleState
- ActiveState
- StalkingState
- BerserkState
 
Each transition depends on nearby workers, nearby items, and surrounding environmental conditions.

Every state also contains:

- A unique regular behaviour
- Deterministic transition conditions
- An immediate transition effect triggered exactly when entering the state

---
# State Overview


## IdleState

### Regular Behaviour
- The mannequin uses IdleScanBehaviour to stay still while scanning nearby workers and items.

### Deterministic State Transitions
- **IdleState → BerserkState**
    - Triggered when **two or more adjacent workers** are detected.

- **IdleState → StalkingState**
    - Triggered when **exactly one worker** is detected within **3 tiles radius**.

- **IdleState → ActiveState**
    - Triggered when:
        - no workers are detected within 3 tiles radius AND
        - at least one item exists within 3 tiles radius.

- **IdleState → IdleState**
    - Triggered when none of the above conditions are satisfied.

### Immediate Transition Effect
#### CorruptSurroundingsEffect
- All adjacent enterable tiles radius are transformed into **ToxicWaste**.
- This effect triggers immediately when entering IdleState.

---

## ActiveState

### Regular Behaviour
- The mannequin uses CollectGroundItemsBehaviour to collect nearby  by moving towards nearest ground item and picks up items on its current tile.

### Deterministic State Transitions
- **ActiveState → BerserkState**
    - Triggered when at least one worker becomes adjacent.

- **ActiveState → StalkingState**
    - Triggered when:
        - a worker exists within 3 tiles radius BUT
        - the worker is not adjacent.

- **ActiveState → IdleState**
    - Triggered when no items exist within 3 tiles radius.

- **ActiveState → ActiveState**
    - Triggered when none of the above conditions are satisfied.

### Immediate Transition Effect
#### ScatterNearbyItemsEffect
- All nearby ground items within 3 tiles radius are redistributed into surrounding empty tiles.
- This effect triggers immediately when entering ActiveState.

---

## StalkingState

### Regular Behaviour
- The mannequin uses FollowNearestWorkerBehaviour to follow the nearest reachable worker.

### Deterministic State Transitions
- **StalkingState → BerserkState**
    - Triggered when at least one worker becomes adjacent.

- **StalkingState → ActiveState**
    - Triggered when:
        - no worker exists within 3 tiles radius AND
        - at least one item exists within 3 tiles radius.

- **StalkingState → IdleState**
    - Triggered when:
        - no worker exists within 3 tiles radius AND
        - no nearby items exist.

- **StalkingState → StalkingState**
    - Triggered when none of the above conditions are satisfied.

### Immediate Transition Effect
#### PoisonSurroundingsEffect
- All actors within 3 tiles radius are poisoned for 3 turns.
- This effect triggers immediately when entering StalkingState.

---

## BerserkState

### Regular Behaviour
- The mannequin uses BerserkAttackAndDropBehaviour to attacks adjacent workers, successful attacks force the target to immediately drop all carried inventory items onto the ground.

### Deterministic State Transitions
- **BerserkState → ActiveState**
    - Triggered when:
        - no workers are within 3 tiles radius AND
        - at least one item exists within 3 tiles radius.

- **BerserkState → IdleState**
    - Triggered when:
        - no workers are within 3 tiles radius AND
        - no items within 3 tiles radius exist.

- **BerserkState → StalkingState**
    - Triggered when:
        - a worker exists within 3 tiles radius BUT
        - the worker is not adjacent.

- **BerserkState → BerserkState**
    - Triggered when at least one worker remains adjacent.

### Immediate Transition Effect
#### IgniteSurroundingsEffect
- All adjacent enterable tiles radius are ignited with Fire.
- This effect triggers immediately when entering BerserkState.

---
### Contribution log link: 
https://docs.google.com/spreadsheets/d/1wdOijFaBssJSIYkIbtEd6_YEAU6Sa9LpDJsAg3OyCls/edit?usp=sharing