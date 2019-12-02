use std::io;
use std::io::Read;

const PART: usize = 1;

fn main() {
    let mut input = String::new();
    match io::stdin().read_to_string(&mut input) {
        Ok(val) => val,
        Err(_) => panic!("Unable to read input as floats."),
    };

    let mut op_codes: Vec<usize> = input
        .split(",")
        .map(|s| s.trim())
        .map(|s| s.parse::<usize>().unwrap())
        .collect();
    println!("{:?}", op_codes);

    if PART == 1 {
        op_codes[1] = 12;
        op_codes[2] = 2;
        println!("{}", run_program(op_codes));
    } else {
        println!("Part 2");
    }
}

fn run_program(input2: Vec<usize>) -> usize {
    let mut input = input2.to_vec();
    let pos = 0;
    loop {
        match input[pos] {
            99 => return input[0],
            1 => input[input[pos + 3]] = input[input[pos + 1]] + input[input[pos + 2]],
            2 => input[input[pos + 3]] = input[input[pos + 1]] * input[input[pos + 2]],
            _ => panic!("Unsupported code"),
        }
        pos = pos + 4;
    }
}
