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
}

#[derive(Debug)]
struct IcProcessor {
    code: Vec<i32>,
    pc: usize,
    status: Status,
    input: Vec<i32>,
    output: Vec<i32>,
}

impl IcProcessor {
    fn is_runnable(&self) -> bool {
        self.status == Status::Runnable
            || (!self.input.is_empty() && self.status == Status::Waiting)
    }

    fn drain_out(&mut self) -> Vec<i32> {
        let val = self.output.clone();
        self.output.clear();
        return val;
    }

    fn get_param(cmd: &str, i: usize) -> usize {
        0
    }

    fn run(&mut self) {
        loop {
            let cmd = format!("{:0>5}", self.code[self.pc].to_string());
            let op_code = &cmd[3..];
            let p1Addr = param_addr(cmd, 1);
            let p2Addr = param_addr(cmd, 2);
            let p3Addr = param_addr(cmd, 3);

            match op_code {
                "99" => {
                    self.status = Status::Halted;
                    return;
                }
                "01" => {}
                "02" => {}
                "03" => {}
                "04" => {}
                "05" => {}
                "06" => {}
                "07" => {}
                "08" => {}
                code => panic!("Unexepected op code recieved {}", code),
            }

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
}
