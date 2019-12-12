use std::cmp;
use std::collections::HashMap;
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
    mem: HashMap<i32, i32>,
    pc: i32,
    rbo: i32,
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

    fn param_addr(&mut self, cmd: &str, i: usize) -> i32 {
        let c = cmd.chars().nth(3 - i).unwrap();
        match c {
            '0' => self.load(self.pc + 1),
            '1' => self.pc + 1,
            '2' => self.load(self.pc + 1) + self.rbo,
        }
    }

    fn load(&mut self, i: i32) -> i32 {
        match self.mem.get(&i) {
            Some(v) => *v,
            None => 0,
        }
    }

    fn set(&mut self, pos: i32, val: i32) {
        self.mem.insert(pos, val);
    }

    fn run(&mut self) {
        loop {
            let cmd = format!("{:0>5}", self.load(self.pc).to_string());
            let op_code = &cmd[3..];
            let p1Addr = self.param_addr(&cmd, 1);
            let p2Addr = self.param_addr(&cmd, 2);
            let p3Addr = self.param_addr(&cmd, 3);
            match op_code {
                "99" => {
                    self.status = Status::Halted;
                    return;
                }
                "01" => {
                    self.set(self.load(p3Addr), self.load(p1Addr) + self.load(p2Addr));
                    self.pc += 4;
                }
                "02" => {
                    self.set(self.load(p3Addr), self.load(p1Addr) * self.load(p2Addr));
                    self.pc += 4;
                }
                "03" => {
                    let pos = self.load(self.pc + 1);
                    if self.input.is_empty() {
                        self.status = Status::Waiting;
                        return;
                    }
                    let in_val = self.input.remove(0);
                    self.set(pos, in_val);
                    self.status = Status::Runnable;
                    self.pc += 2;
                }
                "04" => {
                    let val = self.load(p1Addr);
                    self.output.push(val);
                    self.pc += 2;
                }
                "05" => {
                    if self.load(p1Addr) > 0 {
                        self.pc = self.load(p2Addr);
                    } else {
                        self.pc += 3;
                    }
                }
                "06" => {
                    if self.load(p1Addr) == 0 {
                        self.pc = self.load(p2Addr);
                    } else {
                        self.pc += 3;
                    }
                }
                "07" => {
                    let mut val: i32 = match self.load(p1Addr) < self.load(p2Addr) {
                        true => 1,
                        false => 0,
                    };
                    let pos = self.load(p3Addr);
                    self.set(pos, val);
                    self.pc += 4;
                }
                "08" => {
                    let mut val: i32 = match self.load(p1Addr) == self.load(p2Addr) {
                        true => 1,
                        false => 0,
                    };
                    let pos = self.load(p3Addr);
                    self.set(pos, val);
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
