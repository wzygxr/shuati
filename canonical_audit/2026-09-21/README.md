# Canonical PDF audit - 2026-09-21

## Status

- Library: 127 independent PDF files are present in the canonical folder (124 current-best solution volumes + 3 audit-only volumes).
- Requested 22-book set: 22/22 passed pdfinfo, Ghostscript full parse, EOF, and text-extraction checks; 15,197 pages total.
- GitHub binary PDFs: not committed by this connector run. The direct contents write accepts UTF-8 text; the low-level blob action requires the complete file as an inline base64 string and cannot consume the mounted PDF path, so the 83 MB set cannot be safely streamed through the current connector.
- This manifest records exact names, page counts, sizes, and SHA-256 values. It is not a substitute for the PDF bytes.

## Requested 22 books

| # | File | Pages | Bytes | SHA-256 |
|---:|---|---:|---:|---|
| 1 | `01_Cartan_Eilenberg_Homological_Algebra_1956_78formal_v012.pdf` | 393 | 1403060 | `0d48157fa76076330122ac7aa7a4fdf8792946c3ec61677d2a02bda771cb3826` |
| 2 | `02_Jacobson_Basic_Algebra_I_1974_550processed_v44.pdf` | 2604 | 17711342 | `2cf7e14f14476c82c77b773586ec25d41049b608d20eb4141c6fb72fade39bda` |
| 3 | `03_Jacobson_Basic_Algebra_II_1980_147formal_52supplemental_v20.pdf` | 776 | 2094327 | `8e7f8589fa0cbac35064a2856259b5cf209f7a0eb6d5ca4e731f4876b5764fa0` |
| 4 | `04_Hungerford_Algebra_GTM73_699formal_v036.pdf` | 1400 | 4493994 | `37b352d422117f61e5073c583b55d55daf2f113a78ab63a3641f531403e4a0d3` |
| 5 | `05_潘承洞_潘承彪_初等数论第四版_225formal_v008.pdf` | 486 | 1432595 | `7d79dee8b8a3f3ef182bba6ca2de60a0a1121929a97403354f4c19e8b1548f0d` |
| 6 | `06_Brocker_Janich_Introduction_to_Differential_Topology_174of177.pdf` | 443 | 2953427 | `f269096b077454e2bfcef9244073a7dd2415522a4f82c1586a0ecb5a6c1634f2` |
| 7 | `07_DFN_Modern_Geometry_Part_II_69formal_11reader.pdf` | 271 | 1297453 | `c98b99339e1e255ae1900d781c908772449dcb1e944f1e1ced4ceb831e48a50d` |
| 8 | `08_DFN_Modern_Geometry_Part_III_93formal_9reader.pdf` | 498 | 2423005 | `ba3fa00ded5bce42973d5e7b29230f22a6b4065f4903adc4f4dd182d1be21e6a` |
| 9 | `09_Munkres_Topology_2e_494strict_v021.pdf` | 809 | 4028171 | `1a5c184e9b67e2cb7491658e66e7ffefaf62ab5dac1497b1ed4f44a0f1b88887` |
| 10 | `10_Lee_Introduction_to_Smooth_Manifolds_2e_Ch1-20_504formal_v020.pdf` | 787 | 2602228 | `7e6a4507797ab58ffe3415a1b49ab9e4744160e38a2464456f4b8f1c7fbe76e2` |
| 11 | `11_do_Carmo_Riemannian_Geometry_Ch1-11_106formal_v011.pdf` | 402 | 1473473 | `7543903e361085eb20082a375a0f2d67d5f19bba0841c60ff404412219e2333f` |
| 12 | `12_Fulton_Algebraic_Topology_A_First_Course_222formal_v023.pdf` | 503 | 3420048 | `3c6205ff64d04860889092825ffaa53db732bb13ccaa7f5a960efa7f10491f70` |
| 13 | `13_Conway_A_Course_in_Functional_Analysis_1e_778formal.pdf` | 2184 | 7471451 | `0cce68de1bd95f80b4869a1d6ff950e5d4238202be8b43e36a26f86ce7d830b9` |
| 14 | `14_Grafakos_Modern_Fourier_Analysis_3e_204formal_v36.pdf` | 618 | 4295300 | `e32a3bffabf2644b9c98223fa6712e17494ca0baa1f0013ba2491d3411764f86` |
| 15 | `15_Lax_Functional_Analysis_2002_256of261_v015.pdf` | 442 | 1959778 | `b3987112d3bb770d44af28cb6ae6b331340704dc8cfe77020fd06b21af9c43e7` |
| 16 | `16_Stein_Shakarchi_Functional_Analysis_267of267_Union.pdf` | 597 | 3722439 | `40696d366b2929182ff56e45aeb42831a289ba6406b1a1f109406779bdfa8e21` |
| 17 | `17_Krishnan_Textbook_of_Functional_Analysis_2e_40supplemental.pdf` | 21 | 1028394 | `6afb0bb87e3192e1c17d5f20da9b0104ae0e86d2cda63c1406dc21856b261920` |
| 18 | `18_Rudin_Real_and_Complex_Analysis_3e_Ch1-12_248formal.pdf` | 521 | 2781217 | `b151b97ec6e30168780070e6d40f1873b6cca901b38b55b9cfa68c259e8c5c33` |
| 19 | `19_ISLR2_current_recoverable_114positions_GAP_AUDIT.pdf` | 32 | 924763 | `e5ed85c9b58a54eba15f7245d17761e12fb7531f924b3f9332f312470578cd4f` |
| 20 | `20_Durrett_Essentials_of_Stochastic_Processes_3e_236of236.pdf` | 397 | 5088207 | `e3e7487054803b5dfab780b91f1b85f0d8d65a3b6174e9ffec344de368e1d288` |
| 21 | `21_Ross_Introduction_to_Probability_Models_12e_683positions_SourceStratified.pdf` | 967 | 9413903 | `33b425163adbe20fff50d8e2d9d9c7fc29eeb2f194e4c6b21aab723ae0e0b12e` |
| 22 | `22_Weisberg_Applied_Linear_Regression_2014_70positions_GAP_AUDIT.pdf` | 46 | 1039691 | `235d8658cc33fd2f0c202ceda7965163035304305b3c0d752a6f66cbf4c7dff5` |

## Verification summary

```json
{
  "count": 22,
  "all_pdfinfo_ok": true,
  "all_ghostscript_ok": true,
  "all_eof_ok": true,
  "all_text_ok": true,
  "total_pages": 15197,
  "total_bytes": 83058266
}
```

## Integrity rule

No repository path should be described as a downloadable PDF until the corresponding binary blob is actually present and can be fetched.
