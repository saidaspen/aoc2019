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
