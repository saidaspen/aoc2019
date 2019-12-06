use std::io;
use std::io::Read;

fn main() {
    let mut input = String::new();
    match io::stdin().read_to_string(&mut input) {
        Ok(val) => val,
        Err(_) => panic!("Unable to read input."),
    };
    let codes: Vec<i32> = input
        .split(',')
        .map(|s| s.trim())
        .map(|s| s.parse::<i32>().unwrap())
        .collect();

    run_program(&codes);
}

fn run_program(input2: &[i32]) {
    let mut input = input2.to_vec();
    let mut pc = 0;
    loop {
        let cmd = format!("{:0>5}", input[pc].to_string());
        let op_code = &cmd[3..];
        if op_code == "99" {
            return;
        } else if op_code == "03" {
            let pos = input[pc + 1] as usize;
            input[pos] = 1;
            pc += 2; // This is speial input
            continue;
        } else if op_code == "04" {
            let pos = input[pc + 1] as usize;
            println!("{}", input[pos as usize]);
            pc += 2;
            continue;
        }
        let param1 = match cmd.chars().nth(3).unwrap() {
            '0' => input[pc + 1],
            '1' => input[input[pc + 1] as usize],
            _ => panic!("Unexpected mode"),
        };
        let param2 = match cmd.chars().nth(2).unwrap() {
            '0' => input[pc + 2],
            '1' => input[input[pc + 2] as usize],
            _ => panic!("Unexpected mode"),
        };
        let store_pos = input[pc + 3] as usize;
        println!(
            "param1={}, param2={}, store_pos={}",
            param1, param2, store_pos
        );
        match op_code {
            "99" => return,
            "01" => {
                input[store_pos] = input[param1 as usize] + input[param2 as usize];
                pc += 4;
            }
            "02" => {
                input[store_pos] = input[param1 as usize] * input[param2 as usize];
                pc += 4;
            }
            code => panic!("Unsupported code {}", code),
        };
    }
}
