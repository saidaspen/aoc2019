use itertools::Itertools;
use std::cmp;
use std::io;
use std::io::Read;

#[derive(Debug, PartialEq, Eq)]
enum Status {
    Runnable,
    Waiting,
    Halted,
}

fn main() {
    let mut input = String::new();
    match io::stdin().read_to_string(&mut input) {
        Ok(val) => val,
        Err(_) => panic!("Unable to read input."),
    };
    println!("PART1: {}", run_part1(input.clone()));
    println!("PART2: {}", run_part2(input.clone()));
}

fn run_part1(input: String) -> i32 {
    // This the entire program, each amplifier will each one run its own instance
    // if this code.
    let codes: Vec<i32> = input
        .split(',')
        .map(|s| s.trim())
        .map(|s| s.parse::<i32>().unwrap())
        .collect();

    let mut largest = 0;
    // Each phase needs to be unique
    for phase in [0, 1, 2, 3, 4].iter().cloned().permutations(5) {
        let output = get_output(&phase, &codes, false);
        largest = cmp::max(largest, output);
    }
    largest
}

fn run_part2(input: String) -> i32 {
    // This the entire program, each amplifier will each one run its own instance
    // if this code.
    let codes: Vec<i32> = input
        .split(',')
        .map(|s| s.trim())
        .map(|s| s.parse::<i32>().unwrap())
        .collect();

    let mut largest = 0;
    // Each phase needs to be unique
    for phase in [5, 6, 7, 8, 9].iter().cloned().permutations(5) {
        let output = get_output(&phase, &codes, true);
        largest = cmp::max(largest, output);
    }
    largest
}

fn get_output(phase: &Vec<i32>, pcode: &Vec<i32>, feedback: bool) -> i32 {
    let amp_a = Processor {
        code: pcode.clone(),
        pc: 0,
        status: Status::Runnable,
        input: Vec::new(),
        output: Vec::new(),
    };
    let amp_b = Processor {
        code: pcode.clone(),
        pc: 0,
        status: Status::Runnable,
        input: Vec::new(),
        output: Vec::new(),
    };
    let amp_c = Processor {
        code: pcode.clone(),
        pc: 0,
        status: Status::Runnable,
        input: Vec::new(),
        output: Vec::new(),
    };
    let amp_d = Processor {
        code: pcode.clone(),
        pc: 0,
        status: Status::Runnable,
        input: Vec::new(),
        output: Vec::new(),
    };
    let amp_e = Processor {
        code: pcode.clone(),
        pc: 0,
        status: Status::Runnable,
        input: Vec::new(),
        output: Vec::new(),
    };
    let mut amps = vec![amp_a, amp_b, amp_c, amp_d, amp_e];
    for i in 0..amps.len() {
        amps[i].input.push(phase[i]);
    }
    // Given input
    amps[0].input.push(0);
    let mut to_thrust: i32 = 0;
    loop {
        let mut all_stopped = true;
        let num_amps = amps.len();
        for i in 0..num_amps {
            if amps[i].is_runnable() {
                all_stopped = false;
                amps[i].run();
                if i == (num_amps - 1) {
                    let mut outs = amps[i].drain_out();
                    while !outs.is_empty() {
                        let val = outs.remove(0);
                        to_thrust = val;
                        if feedback {
                            amps[0].input.push(val);
                        }
                    }
                } else if i < num_amps - 1 {
                    let mut outs = amps[i].drain_out();
                    while !outs.is_empty() {
                        let val = outs.remove(0);
                        amps[i + 1].input.push(val);
                    }
                }
            }
        }
        if all_stopped {
            return to_thrust;
        }
    }
}

#[derive(Debug)]
struct Processor {
    code: Vec<i32>,
    pc: usize,
    status: Status,
    input: Vec<i32>,
    output: Vec<i32>,
}

impl Processor {
    fn is_runnable(&self) -> bool {
        self.status == Status::Runnable
            || (!self.input.is_empty() && self.status == Status::Waiting)
    }

    fn drain_out(&mut self) -> Vec<i32> {
        let val = self.output.clone();
        self.output.clear();
        return val;
    }

    fn run(&mut self) {
        loop {
            let cmd = format!("{:0>5}", self.code[self.pc].to_string());
            let op_code = &cmd[3..];
            if op_code == "99" {
                self.status = Status::Halted;
                return;
            } else if op_code == "03" {
                let pos = self.code[self.pc + 1] as usize;
                if self.input.is_empty() {
                    self.status = Status::Waiting;
                    return;
                }
                let in_val = self.input.remove(0);
                self.code[pos] = in_val;
                self.status = Status::Runnable;
                self.pc += 2;
                continue;
            } else if op_code == "04" {
                let pos = self.code[self.pc + 1] as usize;
                let out_val = self.code[pos as usize];
                self.output.push(out_val);
                self.pc += 2;
                continue;
            }
            let param1 = match cmd.chars().nth(2).unwrap() {
                '1' => self.code[self.pc + 1],
                '0' => self.code[self.code[self.pc + 1] as usize],
                _ => panic!("Unexpected mode"),
            };
            let param2 = match cmd.chars().nth(1).unwrap() {
                '1' => self.code[self.pc + 2],
                '0' => self.code[self.code[self.pc + 2] as usize],
                _ => panic!("Unexpected mode"),
            };
            let store_pos = self.code[self.pc + 3] as usize;
            match op_code {
                "01" => {
                    self.code[store_pos] = param1 + param2;
                    self.pc += 4;
                }
                "02" => {
                    self.code[store_pos] = param1 * param2;
                    self.pc += 4;
                }
                "05" => {
                    if param1 > 0 {
                        self.pc = param2 as usize;
                    } else {
                        self.pc += 3;
                    }
                }
                "06" => {
                    if param1 == 0 {
                        self.pc = param2 as usize;
                    } else {
                        self.pc += 3;
                    }
                }
                "07" => {
                    if param1 < param2 {
                        self.code[store_pos] = 1;
                    } else {
                        self.code[store_pos] = 0;
                    }
                    self.pc += 4;
                }
                "08" => {
                    if param1 == param2 {
                        self.code[store_pos] = 1;
                    } else {
                        self.code[store_pos] = 0;
                    }
                    self.pc += 4;
                }

                code => panic!("Unsupported code {}", code),
            };
        }
    }
}

#[cfg(test)]
mod tests {

    use super::*;

    #[test]
    fn test_part1_1() {
        assert_eq!(
            run_part1("3,15,3,16,1002,16,10,16,1,16,15,15,4,15,99,0,0".to_string()),
            43210
        );
    }
    #[test]
    fn test_part1_2() {
        assert_eq!(
            run_part1(
                "3,23,3,24,1002,24,10,24,1002,23,-1,23,101,5,23,23,1,24,23,23,4,23,99,0,0"
                    .to_string()
            ),
            54321
        );
    }

    #[test]
    fn test_part1_3() {
        assert_eq!(
            run_part1(
                "3,31,3,32,1002,32,10,32,1001,31,-2,31,1007,31,0,33,1002,33,7,33,1,33,31,31,1,32,31,31,4,31,99,0,0,0"
                    .to_string()
            ),
            65210
        );
    }

    #[test]
    fn test_part2_1() {
        assert_eq!(
            run_part2(
               "3,26,1001,26,-4,26,3,27,1002,27,2,27,1,27,26,27,4,27,1001,28,-1,28,1005,28,6,99,0,0,5" 
                    .to_string()
            ),
            139629729
        );
    }

    #[test]
    fn test_part2_2() {
        assert_eq!(
            run_part2(
                "3,52,1001,52,-5,52,3,53,1,52,56,54,1007,54,5,55,1005,55,26,1001,54,-5,54,1105,1,12,1,53,54,53,1008,54,0,55,1001,55,1,55,2,53,55,53,4,53,1001,56,-1,56,1005,56,6,99,0,0,0,0,10"
                    .to_string()
            ),
            18216
        );
    }
}
