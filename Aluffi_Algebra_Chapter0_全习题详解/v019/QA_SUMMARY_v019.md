# QA SUMMARY v019

## Scope and counts

- Batch: Chapter V §2 Exercises 2.1–2.25
- Formal exercises: 25
- Variants A/B: 25 each
- Same-type drills: 25
- Cumulative formal exercises: 485
- Cumulative training units: 1940

## Mathematical verification

```text
status=PASS
passed=273052
failed=0
```

The checks cover UFD exponent arithmetic, divisibility of `c^a-1`, the semigroup ring `Z[t^2,t^3]`, the norm on `Z[(1+sqrt(-19))/2]`, DVR valuation arithmetic, a large extended-Euclidean computation, polynomial-value congruences, document structure, images, and hygiene.

## PDF verification

| Artifact | Pages | Result |
|---|---:|---|
| Cumulative v019 PDF | 1090 | PASS |
| V.§2 increment PDF | 54 | PASS |
| Remaining-work ledger | 7 | PASS |

For all three PDFs:

- A4 page size;
- unencrypted;
- `pdfinfo` reports `Suspects: no`;
- PDF preflight warnings: 0;
- Ghostscript full parse: PASS;
- `startxref` and final `%%EOF`: PASS;
- extracted-text replacement characters and NUL bytes: 0.

Full-page rendering:

```text
bad pages = 0
blank-page candidates = 0
black-page candidates = 0
edge-clipping candidates = 0
```

The 54-page increment was rendered independently with PDFium/PyMuPDF and Poppler. Renderer parity: PASS; mean absolute pixel difference 1.300733, maximum page mean 2.062931. Visual differences were antialiasing only.

## Cumulative build note

The full monolithic XeLaTeX pass exceeded the execution window near page 994. It was not used as the delivered PDF. The delivered cumulative PDF was reproducibly assembled from the previously validated 1036-page v018 PDF plus the newly three-pass-compiled 54-page v019 increment; version marks, global page numbering, bookmarks, metadata, fonts, and all 1090 final pages were then revalidated.
