use std::collections::HashMap;
use std::io;
use std::io::Read;

fn main() {
    let mut input = String::new();
    match io::stdin().read_to_string(&mut input) {
        Ok(v) => v,
        Err(_) => panic!("Unable to read input"),
    };
    let mut orbits = HashMap::new();
    input
        .lines()
        .map(|l| l.split(")").collect::<Vec<&str>>())
        .for_each(|o| {
            orbits.insert(o[1].to_string(), o[0].to_string());
        });
    let num_orbits = orbits
        .iter()
        .map(|k| path(k.0, &orbits))
        .map(|p| p.len())
        .sum::<usize>();

    let mut you_path = path("YOU", &orbits);
    let mut san_path = path("SAN", &orbits);

    while you_path.get(you_path.len() - 1) == san_path.get(san_path.len() - 1) {
        you_path.pop();
        san_path.pop();
    }

    println!(
        "Number of orbits: {}, Number of jumps: {}",
        num_orbits,
        (you_path.len() + san_path.len())
    );
}

fn path(from: &str, orbits: &HashMap<String, String>) -> Vec<String> {
    let mut p = Vec::new();
    let mut inner = from;
    loop {
        match orbits.get(inner) {
            Some(v) => {
                p.push(v.to_string());
                inner = v;
            }
            None => break,
        };
    }
    p
}
