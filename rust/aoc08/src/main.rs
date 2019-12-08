use std::io;
use std::io::Read;
extern crate itertools;

use itertools::Itertools;

// pixels[0] is for black
// pixels[1] is for white
// pixels[2] is for transparent
const PIXELS: [&str; 3] = ["  ", "██", "  "];

fn main() {
    let mut input = String::new();
    match io::stdin().read_to_string(&mut input) {
        Ok(val) => val,
        Err(_) => panic!("Unable to read input."),
    };
    run(&input, 25, 6);
}

fn run(input: &str, width: usize, height: usize) {
    let layer_len = width * height;
    let layers = input
        .chars()
        .chunks(layer_len)
        .into_iter()
        .map(|chunk| chunk.collect::<Vec<char>>())
        .collect::<Vec<_>>();
    println!("PART 1 - Integrity code: {}", check_integrity(&layers));
    render(&layers, width, height);
}

fn check_integrity(layers: &Vec<Vec<char>>) -> usize {
    let l_num: usize = layers //Line with fewest zeros
        .iter()
        .enumerate()
        .map(|(i, layer)| (i, cnt_char('0', layer)))
        .min_by(|a, b| Ord::cmp(&a.1, &b.1))
        .expect("No layer has fewest zeros")
        .0;
    cnt_char('1', &layers[l_num]) * cnt_char('2', &layers[l_num])
}

fn cnt_char(c: char, layer: &Vec<char>) -> usize {
    layer.iter().filter(|x| **x == c).count()
}

fn render(layers: &Vec<Vec<char>>, width: usize, height: usize) {
    (0..height) /*for each row */
        .for_each(|row| {
            println!(
                "{}",
                (0..width) /* for each column */
                    .map(|col| pixel_at(layers, row, col, width))
                    .join("")
            )
        });
}

fn pixel_at(layers: &Vec<Vec<char>>, row: usize, col: usize, width: usize) -> &str {
    for layer in layers {
        let pixel = layer[row * width + col];
        if pixel != '2' {
            return PIXELS[pixel.to_digit(10u32).unwrap() as usize];
        }
    }
    PIXELS[2]
}
