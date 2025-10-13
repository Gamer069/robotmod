## Programmable Robots
This mod adds robots to the game which execute actions specified by the player. To power it, however, you need a lunar panel block near your robot.

To spawn a robot, create a structure like so
```
P
I
I
```

where P is carved pumpkin,
and I is iron block.

### Creating addons for this mod
Just create a regular mod which depends on this mod, and use the `ModRegistries.ACTION_TYPE` registry to register your own CustomAction like so:
```java
Registry.register(ModRegistries.ACTION_TYPE, Identifier.of(ExampleMod.MODID, "example"), new ExampleCustomAction());
```

where ExampleCustomAction extends me.illia.robotmod.actions.CustomAction
