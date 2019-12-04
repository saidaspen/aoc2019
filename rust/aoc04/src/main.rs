fn main() {
    println!(
        "{}",
        (137683..596253).filter(|i| is_cand(*i as i32)).count()
    );
}

fn is_cand(i: i32) -> bool {
    let chars: Vec<char> = i.to_string().chars().collect();
    chars.windows(2).all(|c| c[0] <= c[1])
        && chars
            .iter()
            .any(|c| chars.iter().filter(|d| c == *d).count() == 2)
}
