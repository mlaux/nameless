# Nameless

![screenshot](https://github.com/mlaux/nameless/blob/master/gameplay.gif)

Nameless is a minimalist platform game implemented in 4 kilobytes of Java
bytecode. Trent Davies and I entered the game in the 2011 Java4k
programming competition, where it was the #7 user-voted game out of 51
entries.

We had to perform a number of unorthodox optimizations in order to fit our
game within the 4K limit. For example, we named our game's class "run"
because the string "run" was already in the .class file's constant pool as
the name of a method. We also devised a compact level encoding that
completely misuses Java's String class to store binary data.

