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

# Design
The game engine is split into four core classes that do most of the heavy-lifting - *StagParser*, *StagModel*, *StagController* and *StagNLP*.

## StagParser

The StagParser class is designed to parse two main file types for game initialisation:
1. An entities file (DOT / XML) which defines in game objects and locations.
2. An actions file (JSON) which defines the actions a player can perform such as looking around or interacting with an object.

Once the game objects, actions and locations are setup via the JSON and DOT files, the class instance is passed on to to the StagModel class.

## StagModel
The StagModel class stores the current state of the game and includes the list of players currently in game, player inventories, players health and their current locations.

The StagModel class is mutated by the StagController class.


## StagController
The StagController class contains the list of valid actions parsed by the StagParser class. To perform an action, a player types a string of text on the command line. This text is interpreted by the StagNLP class where in a trigger word within the string is matched to a valid action. Since each action is unique, the **HashMap** data structure is used to store the various triggers that fall under an action. The key is the action and the value is an ArrayList of triggers for that action.

# Usage

## Game Setup
The game server, *StagServer* can be launched along with the two command line arguments being the actions (JSON) file path and entities (DOT) file path as shown below:

```java
java StagServer actions.json entities.dot
```
The game server is now listening for incoming connections from clients on port `8888`.

A player can join the game session by launching the *StagClient* class with the command line argument of the player name as shown below:

```java
java StagClient PlayerOne
```

When a connection has been made, the server will receive the incoming commands from the connected client and the game can now be played as intended. The game does support multiplayer, so any additional players will need to launch their own instances of the *StagClient* class with their unique player names.

## Gameplay


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