<img src='http://galenscovell.github.io/css/pics/flicker.png' width=500px />

Flicker-libgdx
======

Flicker is a graphical roguelike game for <b>Android and Desktop</b> that is coded in Java with Libgdx. It's currently a Work in progress with numerous features added regularly.

<b>Current Features:</b>
======
<b>Mechanics</b>
* Procedurally generated caves/dungeons with persistent Player across levels
* Viewport rendering with orthographic camera complete with zoom and pan touch controls
* Efficient game-loop with clear separation of rendering and logic
* Smooth player input free of delays
* Turn-based gameplay
* Entity passive and aggressive behaviors
* JSON data deserialization for procedurally spawned entities and items with unique attributes
* Clean package organization utilizing interfaces, inheritance and DRY principles

<b>Graphics</b>
* Scaling graphics for accurate display on all device resolutions
* Sprites: loading and rendering, animation, linear interpolation
* Torchlight: recursive shadowcasting for dynamic field-of-view
* Transparency effects: slow-moving fog, FOV brightness fall-off, 'fog of war'
* Tile bitmasking: comprehensive world skinning
* GUI HUD: updating stats depending on attributes and combat

