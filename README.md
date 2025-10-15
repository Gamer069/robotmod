## Programmable Robots
This mod adds robots to the game which execute actions specified by the player. 
<br>The player can specify these actions by right clicking the robot at daytime, specifying an action type, clicking +, and specifying all required parameters.
<br>To power it, however, you need an active lunar panel block near your robot. (***specifically 35 blocks near your robot***)

To spawn a robot, create a structure like so
```
P
I
I
```

where P is carved pumpkin,
and I is iron block.

To give an item to the robot, you can either
<br>A: drop the item near the robot so it picks it up
<br>B: right click the robot while crouching, and put any items inside.

### Creating addons for this mod
Just create a regular mod which depends on this mod, and use the `ModRegistries.ACTION_TYPE` registry to register your own CustomAction like so:
```java
Registry.register(ModRegistries.ACTION_TYPE, Identifier.of(ExampleMod.MODID, "example"), new ExampleCustomAction());
```

where ExampleCustomAction extends me.illia.robotmod.actions.CustomAction