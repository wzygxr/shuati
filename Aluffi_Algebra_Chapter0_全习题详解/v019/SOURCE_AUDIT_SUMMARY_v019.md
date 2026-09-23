# SOURCE AUDIT SUMMARY v019

## Edition baseline

Paolo Aluffi, *Algebra: Chapter 0*, Graduate Studies in Mathematics 104, AMS, 2009, 713 pages; corrected second printing, 2016.

Priority order:

1. Author's publication page and official second-printing errata;
2. Corrected second printing of the book;
3. Standard algebra texts and university notes;
4. Public solution repositories only for exercise-number cross-checking and error discovery.

Official sources:

- https://www.math.fsu.edu/~aluffi/mainhtmls/pubs.html
- https://www.math.fsu.edu/~aluffi/algebraerrata.2016/Errata.html

## v019 frozen source range

The corrected printing has exactly **Chapter V §2 Exercises 2.1–2.25**, followed by Chapter V §3. Therefore the strict denominator for this batch is 25, all closed.

## High-risk points independently rederived

- Principal-ideal inclusion reverses divisibility in a UFD.
- gcd equal to a unit does not imply a Bézout identity in a general UFD.
- In `Z[t^2,t^3]`, `t` is not an element and cannot be listed as a common divisor.
- A Noetherian domain is a UFD iff all height-one prime ideals are principal, with the stated height-one existence input.
- `R[x]` being a PID forces `R` to be a field, but not because `(x-c)` is automatically maximal for arbitrary `R`.
- For `Z[(1+sqrt(-19))/2]`, the norm is `a^2+ab+5b^2`.
- Dedekind–Hasse valuations characterize PIDs and are weaker than Euclidean division.
- Schur's polynomial-value theorem gives infinitely many prime divisors across the values; it does not claim infinitely many prime values.

All final Chinese proofs were reorganized independently and include prerequisites, proof strategy, full derivation, error warnings, self-check, two materially different variants, and one same-type drill.
