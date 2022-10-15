<h1 align="center">
  <br>
    <img src=./docs/STAG.png alt="DB" width="100"></a>
  <br>
  STAG
  <br>
</h1>

<h4 align="center">A simple text adventure game (STAG) engine built in Java.</h4>

<p align="center">
  <a href="#Features">Features</a> |
  <a href="#Usage">Usage</a> |
  <a href="#Design">Design</a> |
  <a href="#License">License</a>
</p>

<p align="center">
<img src="./docs/logo.jpg/../stag.gif" width=75% />
</p>

# Features
- Configurable in-game actions using **JSON** setup files
- Customised game environment setup via **DOT/XML** import 
- Supports natural language processing
- Handles multiple players in the same game session

# Gameplay
## Basic Commands
There are a number of standard "built-in" gameplay commands that the game engine supports:

- "inventory" (or "inv" for short): lists all of the artefacts currently being carried by the player
- "get": picks up a specified artefact from the current location and adds it into player's inventory
- "drop": puts down an artefact from player's inventory and places it into the current location
- "goto": moves the player to a new location (if there is a path to that location)
- "look": describes the entities in the current location and lists the paths to other locations

## Game Entities
Entities represent a range of different "things" that exist within a game. There are a number of different types of entity, including the following:

- Locations: Rooms or places within the game
- Artefacts: Physical things within the game that can be collected by the player
- Furniture: Physical things that are an integral part of a location (these can NOT be collected by the player)
- Characters: The various creatures or people involved in game
- Players: A special kind of character that represents the user in the game

It is worth noting that locations are complex constructs and as such have various different attributes in their own right. These attributes include:

- Paths to other locations
- Characters that are currently at a location
- Artefacts that are currently present in a location
- Furniture that belongs in a location

## Custom Actions
In addition to the standard "built-in" commands (e.g. get, goto, look etc.), the game engine responds to a number of game-specific commands (as specified in the [actions.json](data/actions.json)). Each of these actions will have the following elements:

- A set of possible trigger words/phrases (ANY of which can be used to initiate the action)
- A set of subject entities that are acted upon (ALL of which need to be present to perform the action)
- A set of consumed entities that are all removed ("eaten up") by the action
- A set of produced entities that are all created ("generated") by the action
- A narration that provides a human-readable explanation of what happened when the action was performed

## Command Flexibility
The game engine is designed to be as flexible and versatile as possible in order to cope with natural language interpretation on the command line.

For example, `chop tree with axe` might well be entered by the user as `chop down the tree using the axe`. Both versions are equivalent and produce the same in game result.

Furthermore, to further support natural language communication, the game engine allows for "fuzzy" matching where the user is conveniently allowed to omit some objects from a command, whilst providing enough information for the correct action to be executed. For example, `unlock trapdoor with key` could alternatively be entered as either `unlock trapdoor` or `unlock with key` - both provide enough detail for an action match to be attempted.

## Multiple Players
The game supports multiple players within the same game session - and players can view other players in game via the "look" command. This is under the assumption that both players are in the same location.

## Player Health
Each player starts with a health level of 3. This health can be modified in game through the use of actions on in game objects (think health potions, being on the receiving end of attacks etc.)


# Design
The game engine is split into three core classes that do most of the heavy-lifting - *StagParser*, *StagModel* and *StagController*.

## StagParser

The StagParser class is designed to parse two main file types for game initialisation:
1. An entities file (DOT / XML) which defines in-game objects and locations.
2. An actions file (JSON) which defines the actions a player can perform such as looking around or interacting with an object.

Once the game objects, actions and locations are setup via the JSON and DOT files, the class instance is passed on to the StagModel class.

## StagModel
The StagModel class stores the current state of the game and includes the list of players currently in game, player inventories, players health and their current locations.

The StagModel class is mutated by the StagController class.


## StagController
The StagController class contains the list of valid actions parsed by the StagParser class. To perform an action, a player types a string of text on the command line. This text is interpreted by the StagNLP class where a trigger word within the string is matched to a valid action. Since each action is unique, the **HashMap** data structure is used to store the various triggers that fall under an action. The key is the action and the value is an ArrayList of triggers for that action.

# Usage

## Game Setup
The game server, *StagServer* can be launched along with the two command line arguments being the actions (JSON) file path and entities (DOT) file path as shown below:

```java
java StagServer actions.json entities.dot
```
At this point, the game server is now listening for incoming connections from clients on port `8888`.

A player can join the game session by launching the *StagClient* class with the command line argument of the player name as shown below:

```java
java StagClient PlayerOne
```
When a connection has been made, the server will receive incoming commands from the connected client and the game can now be played as intended. The game does support multiplayer, so any additional players will need to launch their own instances of the *StagClient* class with their unique player names.

# License

```
Copyright (c) 2021 Keane Fernandes

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```