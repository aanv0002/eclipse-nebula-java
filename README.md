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
# REQ5: Lunar Evolution System (API Integration)

## Overview
The Lunar Evolution System connects the Eclipse Nebula facility to the 
real-world moon phase using the FreeAstroAPI Moon Phase endpoint. Every 
50 turns the game fetches the current moon phase and applies a corresponding 
lunar influence to the active game map, physically altering creature behaviour 
and terrain.

## API Used
FreeAstroAPI Moon Phase  
Endpoint: `https://api.freeastroapi.com/api/v1/moon/phase`

## How to Get the API Key
1. Go to https://www.freeastroapi.com
2. Sign up for a free account
3. Go to the dashboard
4. Generate a new API key
5. Copy the full key

## How to Set Up the API Key
1. Create a file named `.env` in the root of the project (same level as `pom.xml`)
2. Add the following line to the `.env` file: MOON_API_KEY=your_api_key_here
3. Replace `your_api_key_here` with your actual API key
4. Save the file
5. Do NOT commit this file to GitLab - it is already listed in `.gitignore`

## How to Run
1. Complete the API key setup above
2. Run the game normally through `Application.java`
3. Every 50 turns the lunar system will automatically fetch the moon phase
4. The console will print the current moon phase, illumination and active influence

## Dynamic API Request
The API request is not static. The following parameters change based on game state:

| Parameter | Source | Purpose |
|-----------|--------|---------|
| `date` | Current real-world date | Changes moon phase over time |
| `lat` | Active map identifier | 99-Deprecated uses Melbourne (-37.8136) |
| `lon` | Active map identifier | 20-Overflow uses Kuala Lumpur (101.6869) |

Example request for 99-Deprecated:
GET https://api.freeastroapi.com/api/v1/moon/phase
?date=2026-06-06
&lat=-37.8136
&lon=144.9631
&include_eclipse=true
x-api-key: your_api_key_here

## Expected JSON Response
```json
{
  "phase": {
    "name": "Full Moon",
    "illumination": 0.98,
    "age_days": 14.5
  },
  "eclipse": {
    "is_blood_moon": false
  }
}
```

## Phase Mapping
| API Response | Game Effect |
|-------------|-------------|
| `is_blood_moon: true` | BloodMoonInfluence — isolated creatures mutate into MutatedCreatures |
| `phase.name: Full Moon` | FullMoonInfluence — clustered Slimes split into additional Slimes |
| `phase.name: New Moon` | NewMoonInfluence — enclosed creatures become dormant ambushers |
| All other phases | DefaultLunarInfluence — no major effect |

## Architecture
### Abstractions
- `LunarInfluence` — interface for applying lunar phase effects to the map
- `EvolutionBehaviour` — interface for complex evolution effects on creatures

### Concrete Classes
- `FullMoonInfluence` — triggers ColonyEvolution on clustered Slimes
- `NewMoonInfluence` — triggers AmbushEvolution on enclosed creatures
- `BloodMoonInfluence` — triggers MutationEvolution on isolated creatures
- `DefaultLunarInfluence` — no effect for non-major phases
- `ColonyEvolution` — spawns additional Slimes near clusters
- `AmbushEvolution` — applies DormantStatus making creatures dormant
- `MutationEvolution` — replaces creatures with MutatedCreature

### Higher-Level Classes
- `MoonPhaseController` — orchestrates API calls and influence selection
- `MoonPhaseApiService` — handles API communication and JSON parsing

## SOLID Principles Applied
- **SRP** — each class has one responsibility
- **OCP** — new influences and evolutions implement interfaces without modifying existing classes
- **DIP** — MoonPhaseController depends on LunarInfluence abstraction not concrete classes
- **DRY** — one MoonPhaseApiService handles all API calls

---
### Contribution log link: 
https://docs.google.com/spreadsheets/d/1wdOijFaBssJSIYkIbtEd6_YEAU6Sa9LpDJsAg3OyCls/edit?usp=sharing