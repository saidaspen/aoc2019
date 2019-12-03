use std::io;
use std::io::Read;

fn main() {
    let mut input = String::new();
    match io::stdin().read_to_string(&mut input) {
        Ok(val) => val,
        Err(_) => panic!("Unable to read input as floats."),
    };
    let lines = input.split("\n").collect::<Vec<&str>>();
    println!(
        "Closest crossing:{}",
        closest_crossing(wire_of(lines[0]), wire_of(lines[1]))
    );
}

#[derive(Debug, Copy, Clone, PartialEq, PartialOrd)]
struct Point {
    x: i32,
    y: i32,
}

impl Point {
    fn manhattan_distance_to(&self, p: Point) -> i32 {
        (self.x - p.x).abs() + (self.y - p.y).abs()
    }
}

const ORIGIN: Point = Point { x: 0, y: 0 };

#[allow(dead_code)]
fn closest_crossing(wire1: Vec<Point>, wire2: Vec<Point>) -> i32 {
    wire1
        .iter()
        .filter(|p| *p != &ORIGIN)
        .filter(|p| wire2.contains(p))
        .map(|p| p.manhattan_distance_to(ORIGIN))
        .min()
        .expect("Unable to find minimum distance")
}

fn wire_of(val: &str) -> Vec<Point> {
    let mut pos = ORIGIN;
    let mut wire: Vec<Point> = Vec::new();
    let split = val.split(',').collect::<Vec<&str>>();
    wire.push(pos);
    for op in split {
        let dir: char = op.chars().next().unwrap();
        let mag: i32 = op.chars().skip(1).collect::<String>().parse().unwrap();
        for _ in 0..mag {
            match dir {
                'R' => {
                    pos = Point {
                        x: pos.x + 1,
                        y: pos.y,
                    }
                }
                'L' => {
                    pos = Point {
                        x: pos.x - 1,
                        y: pos.y,
                    }
                }
                'U' => {
                    pos = Point {
                        x: pos.x,
                        y: pos.y + 1,
                    }
                }
                'D' => {
                    pos = Point {
                        x: pos.x,
                        y: pos.y - 1,
                    }
                }
                _ => panic!("Unsupported direction found"),
            }
            wire.push(pos);
        }
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
        assert_eq!(closest_crossing(wire_of(l1), wire_of(l2)), 6);
    }

    #[test]
    fn test_find_closest2() {
        let l1 = "R75,D30,R83,U83,L12,D49,R71,U7,L72";
        let l2 = "U62,R66,U55,R34,D71,R55,D58,R83";
        assert_eq!(closest_crossing(wire_of(l1), wire_of(l2)), 159);
    }

    #[test]
    fn test_find_closest3() {
        let l1 = "R98,U47,R26,D63,R33,U87,L62,D20,R33,U53,R51";
        let l2 = "U98,R91,D20,R16,D67,R40,U7,R15,U6,R7";
        assert_eq!(closest_crossing(wire_of(l1), wire_of(l2)), 135);
    }
}
