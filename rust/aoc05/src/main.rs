use std::io;
use std::io::Read;

fn main() {
    let mut input = String::new();
    match io::stdin().read_to_string(&mut input) {
        Ok(val) => val,
        Err(_) => panic!("Unable to read input."),
    };
    let mut codes: Vec<i32> = input
        .split(',')
        .map(|s| s.trim())
        .map(|s| s.parse::<i32>().unwrap())
        .collect();
    run_program(&mut codes);
}

fn run_program(input: &mut Vec<i32>) {
    let mut pc = 0;
    loop {
        let cmd = format!("{:0>5}", input[pc].to_string());
        let op_code = &cmd[3..];
        if op_code == "99" {
            return;
        } else if op_code == "03" {
            let pos = input[pc + 1] as usize;
            input[pos] = 5; // This is special input given in the problem.
            pc += 2;
            continue;
        } else if op_code == "04" {
            let pos = input[pc + 1] as usize;
            println!("{}", input[pos]);
            pc += 2;
            continue;
        }
        let param1 = match cmd.chars().nth(2).unwrap() {
            '1' => input[pc + 1],
            '0' => input[input[pc + 1] as usize],
            v => panic!("Unexpected mode {}", v),
        };
        let param2 = match cmd.chars().nth(1).unwrap() {
            '1' => input[pc + 2],
            '0' => input[input[pc + 2] as usize],
            _ => panic!("Unexpected mode"),
        };
        let store_pos = input[pc + 3] as usize;
        match op_code {
            "99" => return,
            "01" => {
                input[store_pos] = param1 + param2;
                pc += 4;
            }
            "02" => {
                input[store_pos] = param1 * param2;
                pc += 4;
            }
            "05" if param1 > 0 => pc = param2 as usize,
            "05" => pc += 3,
            "06" if param1 == 0 => pc = param2 as usize,
            "06" => pc += 3,
            "07" if param1 < param2 => {
                input[store_pos] = 1;
                pc += 4;
            }
            "07" => {
                input[store_pos] = 0;
                pc += 4;
            }
            "08" if param1 == param2 => {
                input[store_pos] = 1;
                pc += 4;
            }
            "08" => {
                input[store_pos] = 0;
                pc += 4;
            }
            code => panic!("Unsupported code {}", code),
        };
    }
}
