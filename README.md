# Advent of Code 2019 Solutions
Here are my attempts at solving the [Advent of Code](https://adventofcode.com/) puzzles in Java and in Rust.
I write Java code daily and have been for the last 15 years or so, so one would think I would be able to handle it.
Rust, however, is new to me. 

## Java solutions
To run a solution. 
First build it using ```make```. This will compile the java classes into ```dist/``` directory.
```
$ cd java
$ make
```

From there, either run it yourself or use the shell-script provided. Here is an example running the puzzle or day 1 (Aoc01)
with the inputs taken from file ```/inputs/aoc01.txt```

```
$ dist/aoc.sh Aoc01 <input file>
```
where ```<input file>```is the input for that particular day.

## Rust solutions
To run a solution, change to its directory and then run it using Cargo. 

```
$ cd rust/aoc01
$ cargo run --release < <input file>
```
where ```<input file>```is the input for that particular day.

Some days there is no direct input to the puzzle, or the input is a number or two. For some of these days the input is actually hard coded directly in the source. 

## The programming challenges
* Day 25 
* Day 24
* Day 23
* Day 22
* [Day 21: Springdroid Adventure](https://adventofcode.com/2019/day/21) - Solutions [Java](https://github.com/saidaspen/aoc2019/blob/master/java/src/se/saidaspen/aoc2019/aoc21/Aoc21.java), Rust
* [Day 20: Donut Maze](https://adventofcode.com/2019/day/20) - Solutions [Java](https://github.com/saidaspen/aoc2019/blob/master/java/src/se/saidaspen/aoc2019/aoc20/Aoc20.java), Rust
* [Day 19: Tractor Beam](https://adventofcode.com/2019/day/19) - Solutions [Java](https://github.com/saidaspen/aoc2019/blob/master/java/src/se/saidaspen/aoc2019/aoc19/Aoc19.java), Rust
* Day 18: Many-Worlds Interpretation
* [Day 17: Set and Forget](https://adventofcode.com/2019/day/17) - Solutions [Java](https://github.com/saidaspen/aoc2019/blob/master/java/src/se/saidaspen/aoc2019/aoc17/Aoc17.java), Rust
* [Day 16: Flawed Frequency Transmission](https://adventofcode.com/2019/day/16) - Solutions [Java](https://github.com/saidaspen/aoc2019/blob/master/java/src/se/saidaspen/aoc2019/aoc16/Aoc16.java), Rust
* [Day 15: Oxygen System](https://adventofcode.com/2019/day/15) - Solutions [Java](https://github.com/saidaspen/aoc2019/blob/master/java/src/se/saidaspen/aoc2019/aoc15/Aoc15.java), Rust
* [Day 14: Space Stoichiometry](https://adventofcode.com/2019/day/14) - Solutions [Java](https://github.com/saidaspen/aoc2019/blob/master/java/src/se/saidaspen/aoc2019/aoc14/Aoc14.java), Rust
* [Day 13: Care Package](https://adventofcode.com/2019/day/13) - Solutions [Java](https://github.com/saidaspen/aoc2019/blob/master/java/src/se/saidaspen/aoc2019/aoc13/Aoc13.java), Rust
* [Day 12: The N-Body Problem](https://adventofcode.com/2019/day/12) - Solutions [Java](https://github.com/saidaspen/aoc2019/blob/master/java/src/se/saidaspen/aoc2019/aoc12/Aoc12.java), Rust
* [Day 11:Day 11: Space Police](https://adventofcode.com/2019/day/11) - Solutions [Java](https://github.com/saidaspen/aoc2019/blob/master/java/src/se/saidaspen/aoc2019/aoc11/Aoc11.java), Rust
* [Day 10: Monitoring Station](https://adventofcode.com/2019/day/10) - Solutions [Java](https://github.com/saidaspen/aoc2019/blob/master/java/src/se/saidaspen/aoc2019/aoc10/Aoc10.java), Rust
* [Day 9: Sensor Boost](https://adventofcode.com/2019/day/9) - Solutions : [Java](https://github.com/saidaspen/aoc2019/tree/master/java/src/se/saidaspen/aoc2019/aoc09), Rust
* [Day 8: Day 8: Space Image Format](https://adventofcode.com/2019/day/8) - Solutions: [Java](https://github.com/saidaspen/aoc2019/blob/master/java/src/se/saidaspen/aoc2019/aoc08/Aoc08.java), [Rust](https://github.com/saidaspen/aoc2019/blob/master/rust/aoc08/src/main.rs)
* [Day 7: Amplification Circuit](https://adventofcode.com/2019/day/7) - Solutions: [Java](https://github.com/saidaspen/aoc2019/blob/master/java/src/se/saidaspen/aoc2019/aoc07/Aoc07.java), [Rust](https://github.com/saidaspen/aoc2019/blob/master/rust/aoc07/src/main.rs)
* [Day 6: Universal Orbit Map](https://adventofcode.com/2019/day/6) - Solutions: [Java](https://github.com/saidaspen/aoc2019/blob/master/java/src/se/saidaspen/aoc2019/aoc06/Aoc06.java), [Rust](https://github.com/saidaspen/aoc2019/blob/master/rust/aoc06/src/main.rs)
* [Day 5: Sunny with a Chance of Asteroids](https://adventofcode.com/2019/day/5) - Solutions: [Java](https://github.com/saidaspen/aoc2019/blob/master/java/src/se/saidaspen/aoc2019/aoc05/Aoc05.java), [Rust](https://github.com/saidaspen/aoc2019/blob/master/rust/aoc05/src/main.rs)
* [Day 4: Secure Container](https://adventofcode.com/2019/day/4) - Solutions: [Java](https://github.com/saidaspen/aoc2019/blob/master/java/src/se/saidaspen/aoc2019/aoc04/Aoc04.java), [Rust](https://github.com/saidaspen/aoc2019/blob/master/rust/aoc04/src/main.rs)
* [Day 3: Crossed Wires](https://adventofcode.com/2019/day/3) - Solutions: [Java](https://github.com/saidaspen/aoc2019/blob/master/java/src/se/saidaspen/aoc2019/aoc03/Aoc03.java), [Rust](https://github.com/saidaspen/aoc2019/blob/master/rust/aoc03/src/main.rs)
* [Day 2: 1202 Program Alarm](https://adventofcode.com/2019/day/2) - Solutions: [Java](https://github.com/saidaspen/aoc2019/blob/master/java/src/se/saidaspen/aoc2019/aoc02/Aoc02.java), [Rust](https://github.com/saidaspen/aoc2019/blob/master/rust/aoc02/src/main.rs)
* [Day 1: The Tyranny of the Rocket Equation](https://adventofcode.com/2019/day/1) - Solutions: [Java](https://github.com/saidaspen/aoc2019/blob/master/java/src/se/saidaspen/aoc2019/aoc01/Aoc01.java), [Rust](https://github.com/saidaspen/aoc2019/blob/master/rust/aoc01/src/main.rs) 


## The poems
On the Advent of Code Reddit page there is a daily thread where people show off not only their solutions but also poems.
Here are the poems I entered in the Advent of Code 2019 Reddit Megathreads.

### Day 8

    It’s digital now

    There's no password managers in space,
    You have to memorise them all;
    Or take a picture, just in case,
    To help, when time comes to recall.
    Oh no, I forgot the code again,
    Time to load the picture up;
    Render it, layer by layer, then;
    It’s digital now, new way to develop


