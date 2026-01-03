# Introduction and Brief
The project will implement a modular ecosystem simulation where environments, resources, 
and animal actors interact through movement, feeding, reproduction, and combat. 
Users will be able to add resources such as water and grass, introduce herbivores and carnivores 
(individually or in packs), and observe how they behave according to species rules. 
The simulation will support saving and loading the world state in JSON format and will include 
a plugin system that dynamically transforms generic actors into more specific species types—such as wolves, 
lions, or bears—with unique stats. Plugins will be loadable and unloadable at runtime, 
allowing the system to revert seamlessly between generic and specialized actors.

---

# Functional Requirements

## REQ‑F‑001 — Resource Management

### REQ‑F‑001.1
- The App will allow the user to add different resources such as Water and Grass.

### REQ‑F‑001.2
- Herbivores will consume Grass to survive.  
- Carnivores will consume Herbivores to survive.  
- Both Herbivores and Carnivores will consume Water to survive.
- If they are not eating, both herbivores and carnivores lose life points.
- When life points reach 0, the animal dies.

---

## REQ‑F‑002 — Entity Management

### REQ‑F‑002.1
- The App will allow the user to add members of different classes, specifically Herbivores and Carnivores.

### REQ‑F‑002.2
- Carnivores will be addable in packs or as separate entities.
- If two Carnivores are not in the same pack, they will potentially damage each other.

---

## REQ‑F‑003 — Movement and Interaction

### REQ‑F‑003.1
- Both Carnivores and Herbivores will move every game‑loop cycle by a random amount multiplied by their base speed.

### REQ‑F‑003.2
- If a Carnivore and a Herbivore get close enough, the Carnivore will attack the Herbivore.  
- The damage will be a random amount multiplied by the Carnivore’s base attack.

### REQ‑F‑003.3
- If Carnivores or Herbivores of different genders get close, they will reproduce, generating a random number of newborns.  
- The two parents and the newborns will not move for a certain amount of time after reproduction.

---

## REQ‑F‑004 — Persistence

### REQ‑F‑004.1
- The user will be able to save the game state.  
- The serialized data will be stored in a JSON file.

### REQ‑F‑004.2
- The user will be able to load a previously saved game from a JSON file.

---

## REQ‑F‑005 — Plugin System

### REQ‑F‑005.1
- The user will be able to load a plugin that converts generic actors into more specific subclasses.
  - Example: Carnivores will be split into Wolves, Lions, Bears, etc., each with different stats.

### REQ‑F‑005.2
- The user will be able to unload the plugin.  
- When the plugin is unloaded, all actors will revert back to their generic classes (Carnivore / Herbivore) flawlessly.

---

# Non‑Functional Requirements

## REQ‑NF‑001 — Performance
- The simulation will run at a stable tick rate.

## REQ‑NF‑002 — Logging
- The App will log all major events (movement, attacks, reproduction, plugin load/unload).

## REQ‑NF‑003 — Security
- The App will sanitize all user inputs and will prevent crashes caused by invalid data.

## REQ‑NF‑004 — Modularity
- The plugin system will support loading and unloading at runtime without restarting the App.

## REQ‑NF‑005 — Persistence Integrity
- Save files will remain compatible across sessions unless explicitly invalidated.

---

# System Requirements

## REQ‑SYS‑001 — Platform
- The App will run as a Java SE console application.

## REQ‑SYS‑002 — Dependencies
- The App will require no external UI framework.
