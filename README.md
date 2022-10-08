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
The game engine is split into four core classes that do most of the heavy-lifting - StagParser, StagModel, StagController and StagNLP.

## StagParser

The StagParser class is designed to parse two main file types for game initialisation:
1. An entities file (DOT / XML) which defines in game objects and locations.
2. An actions file (JSON) which defines the actions a player can perform such as looking around or interacting with an object.

The game engine server listens for incoming connections from clients on port `8888`. When a connection has been made, the server will receive the incoming command from the connected client 

# Usage
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