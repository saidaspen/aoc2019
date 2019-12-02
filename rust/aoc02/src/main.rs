use std::io;
use std::io::Read;

const PART: usize = 2;

fn main() {
    let mut input = String::new();
    match io::stdin().read_to_string(&mut input) {
        Ok(val) => val,
        Err(_) => panic!("Unable to read input as floats."),
    };

    let mut op_codes: Vec<usize> = input
        .split(',')
        .map(|s| s.trim())
        .map(|s| s.parse::<usize>().unwrap())
        .collect();

    if PART == 1 {
        op_codes[1] = 12;
        op_codes[2] = 2;
        println!("{}", run_program(&op_codes));
    } else {
        for noun in 0..99 {
            for verb in 0..99 {
                op_codes[1] = noun as usize;
                op_codes[2] = verb as usize;
                if run_program(&op_codes) == 19690720 {
                    println!("{}", 100 * noun + verb);
                }
            }
        }
    }
}

fn run_program(input2: &[usize]) -> usize {
    let mut input = input2.to_vec();
    let mut pos = 0;
    loop {
        let param1 = input[pos + 1];
        let param2 = input[pos + 2];
        let store_pos = input[pos + 3];
        match input[pos] {
            99 => return input[0],
            1 => input[store_pos] = input[param1] + input[param2],
            2 => input[store_pos] = input[param1] * input[param2],
            _ => panic!("Unsupported code"),
        }
        pos += 4;
    }
}
