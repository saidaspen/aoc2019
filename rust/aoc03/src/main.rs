use std::io;
use std::io::Read;

const PART: usize = 2;

const ORIGIN: Point = Point { x: 0, y: 0 };

fn main() {
    let mut input = String::new();
    match io::stdin().read_to_string(&mut input) {
        Ok(val) => val,
        Err(_) => panic!("Unable to read input as floats."),
    };
    let lines = input.split("\n").collect::<Vec<&str>>();
    if PART == 1 {
        println!(
            "Closest crossing:{}",
            closest_crossing(&wire_of(lines[0]), &wire_of(lines[1]))
        );
    } else {
        println!(
            "Shortest distance: {}",
            shortest_wire(&wire_of(lines[0]), &wire_of(lines[1]))
        );
    }
}

#[derive(Debug, Copy, Clone, PartialEq, PartialOrd)]
struct Point {
    x: i32,
    y: i32,
}

impl Point {
    fn manhattan_distance_to(&self, p: &Point) -> i32 {
        (self.x - p.x).abs() + (self.y - p.y).abs()
    }

    fn is_on_line(&self, start: Point, end: Point) -> bool {
        if self.x == start.x {
            return (self.y < start.y && self.y > end.y) || (self.y > start.y && self.y < end.y);
        } else if self.y == start.y {
            return (self.x < start.x && self.x > end.x) || (self.x > start.x && self.x < end.x);
        }
        false
    }
}

fn shortest_wire(wire1: &Vec<Point>, wire2: &Vec<Point>) -> i32 {
    find_crossings(wire1, wire2)
        .iter()
        .map(|p| length_to(wire1, &p) + length_to(wire2, &p))
        .min()
        .expect("Unable to find minimum wire lengths")
}

fn closest_crossing(wire1: &Vec<Point>, wire2: &Vec<Point>) -> i32 {
    find_crossings(wire1, wire2)
        .iter()
        .filter(|p| *p != &ORIGIN)
        .map(|p| p.manhattan_distance_to(&ORIGIN))
        .min()
        .expect("Unable to find minimum distance")
}

fn find_crossings(wire1: &Vec<Point>, wire2: &Vec<Point>) -> Vec<Point> {
    let mut crossings: Vec<Point> = Vec::new();
    for i in 0..wire1.len() - 1 {
        for j in 0..wire2.len() - 1 {
            match crosses(wire2[j], wire2[j + 1], wire1[i], wire1[i + 1]) {
                Some(p) => crossings.push(p),
                _ => {}
            }
        }
    }
    crossings
}

fn length_to(wire: &Vec<Point>, p: &Point) -> i32 {
    let mut len = 0;
    for i in 0..wire.len() - 1 {
        if !p.is_on_line(wire[i], wire[i + 1]) {
            len += wire[i].manhattan_distance_to(&wire[i + 1]);
        } else {
            len += wire[i].manhattan_distance_to(p);
            break;
        }
    }
    len
}

fn crosses(a: Point, b: Point, c: Point, d: Point) -> Option<Point> {
    let candidate = if a.x == b.x {
        Point { x: a.x, y: c.y }
    } else {
        Point { x: c.x, y: a.y }
    };
    if candidate.is_on_line(c, d) && candidate.is_on_line(a, b) {
        Some(candidate)
    } else {
        None
    }
}

fn wire_of(val: &str) -> Vec<Point> {
    let mut pos = ORIGIN;
    let mut wire: Vec<Point> = Vec::new();
    let split = val.split(',').collect::<Vec<&str>>();
    wire.push(pos);
    for op in split {
        let dir: char = op.chars().next().unwrap();
        let mag: i32 = op.chars().skip(1).collect::<String>().parse().unwrap();
        match dir {
            'R' => {
                pos = Point {
                    x: pos.x + mag,
                    y: pos.y,
                }
            }
            'L' => {
                pos = Point {
                    x: pos.x - mag,
                    y: pos.y,
                }
            }
            'U' => {
                pos = Point {
                    x: pos.x,
                    y: pos.y + mag,
                }
            }
            'D' => {
                pos = Point {
                    x: pos.x,
                    y: pos.y - mag,
                }
            }
            _ => panic!("Unsupported direction found"),
        }
        wire.push(pos);
    }
    wire
}

#[cfg(test)]
mod tests {
    // Note this useful idiom: importing names from outer (for mod tests) scope.
    use super::*;

    #[test]
    fn test_find_closest1() {
        let l1 = "R8,U5,L5,D3";
        let l2 = "U7,R6,D4,L4";
        assert_eq!(closest_crossing(&wire_of(l1), &wire_of(l2)), 6);
    }

    #[test]
    fn test_find_closest2() {
        let l1 = "R75,D30,R83,U83,L12,D49,R71,U7,L72";
        let l2 = "U62,R66,U55,R34,D71,R55,D58,R83";
        assert_eq!(closest_crossing(&wire_of(l1), &wire_of(l2)), 159);
    }

    #[test]
    fn test_find_closest3() {
        let l1 = "R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51";
        let l2 = "U98,R91,D20,R16,D67,R40,U7,R15,U6,R7";
        assert_eq!(closest_crossing(&wire_of(l1), &wire_of(l2)), 135);
    }

    #[test]
    fn test_find_shortest() {
        let l1 = "R75,D30,R83,U83,L12,D49,R71,U7,L72";
        let l2 = "U62,R66,U55,R34,D71,R55,D58,R83";
        assert_eq!(shortest_wire(&wire_of(l1), &wire_of(l2)), 610);
    }

    #[test]
    fn test_find_shortest2() {
        let l1 = "R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51";
        let l2 = "U98,R91,D20,R16,D67,R40,U7,R15,U6,R7";
        assert_eq!(shortest_wire(&wire_of(l1), &wire_of(l2)), 410);
    }
}
