# SOURCE AUDIT v022

## Edition baseline

Paolo Aluffi, *Algebra: Chapter 0*, Graduate Studies in Mathematics 104, corrected second printing, AMS, 2016.

Priority order:

1. author’s official corrected-second-printing errata;
2. corrected second printing and complete exercise order;
3. author or university lecture notes;
4. independent public solutions/discussions, only for locating risks and comparing routes.

Official references:

- `https://www.math.fsu.edu/~aluffi/`
- `https://www.math.fsu.edu/~aluffi/algebraerrata.2016/Errata.html`
- `https://bookstore.ams.org/gsm-104`

## Range frozen in v022

- V.5.1–V.5.24: 24 formal exercise positions
- V.6.1–V.6.21: 21 formal exercise positions
- Total: 45
- Chapter VI begins after V.6.21

## Official errata affecting this range

The author’s errata index explicitly flags:

- p.290, Exercise 5.22;
- p.295, bottom;
- p.302, Exercise 6.19.

Implemented corrections and boundary checks:

1. V.5.22 uses an irreducible `p` with `p | a` but $p^2\nmid a$, so Eisenstein applies to `x^n-a` for every `n>1`.
2. V.6.9 assumes a nonzero ideal; otherwise `Z[i]/(0)` is infinite.
3. V.6.19 uses the standard Hurwitz ring: all four coordinates are integers or all four are half-integers; its unit group has 24 elements.
4. V.6.18 includes zero in the interval `0 <= a <= (p-1)/2` to obtain exactly `(p+1)/2` square classes.

## Secondary checks

Public Aluffi solution repositories were consulted for numbering and candidate approaches, but they are incomplete in this range and were not treated as authoritative. Noncommutative left/right directions in V.6.19–6.21 were rebuilt directly from definitions.

## Copyright boundary

Exercise statements are mathematically restated in Chinese rather than reproduced verbatim. Proofs, explanations, variants, drills, figure, and verification code are newly organized.
