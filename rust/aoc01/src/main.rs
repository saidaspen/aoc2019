use std::io;
use std::io::Read;

fn main() {
    let mut input = String::new();
    match io::stdin().read_to_string(&mut input) {
        Ok(val) => val,
        Err(_) => panic!("Unable to read input as floats."),
    };
    println!(
        "{}",
        input
            .lines()
            .map(|s| String::from(s))
            .map(|s| match s.parse::<f32>() {
                Ok(val) => val,
                Err(_) => panic!("Unable to parse line as float."),
            })
            .map(|w| fuel_req_for(w))
            .sum::<i32>()
    );
}

fn fuel_req_for(val: f32) -> i32 {
    match (val / 3.0).floor() as i32 - 2 {
        i if i < 1 => 0,
        i => i + fuel_req_for(i as f32),
    }
}

#[test]
fn test_fuel_req_for() {
    assert_eq!(fuel_req_for(14_f32), 2);
    assert_eq!(fuel_req_for(1969_f32), 966);
    assert_eq!(fuel_req_for(100756_f32), 50346);
}
