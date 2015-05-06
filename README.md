<img src='http://galenscovell.github.io/css/pics/flicker.png' width=500px />

Flicker-libgdx
======

Full-fledged graphical roguelike coded in Java with Libgdx/OpenGL. Work in progress with numerous features added daily.

<b>Current Features:</b>
======
<b>Mechanics</b>
* Procedurally generated caves/dungeons with persistent Player across levels
* Viewport rendering with orthographic camera centered on Player
* Efficient game-loop with clear separation of rendering and logic
* Smooth player input free of delays
* Turn-based gameplay
* Entity passive and aggressive behaviors
* JSON data deserialization for procedurally spawned entities and items with unique attributes
* Clean package organization utilizing interfaces, inheritance and DRY principles

<b>Graphics</b>
* Sprites: loading and rendering, animation, linear interpolation
* Torchlight: recursive shadowcasting for dynamic field-of-view
* Transparency effects: slow-moving fog, field-of-view brightness fall-off
* Tile bitmasking: comprehensive world skinning
* GUI HUD: updating stats depending on attributes and combat

