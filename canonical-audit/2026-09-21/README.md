# 课后习题单书 Canonical 核验合流（2026-09-21）

本目录记录 2026-09-21 对 Library、Google Drive 与 GitHub 的交叉核验结果。

## 结论

- 本轮整理并逐册验收 **19 册独立 PDF**。
- 19 册均通过 `pdfinfo`、`%%EOF`、`startxref`、Ghostscript 全页解释，以及首/中/末页抽样渲染。
- 其中 **12 册**在正式题或项目指定范围内闭合；**7 册**仍属于 source-lock、open problem、reader-task、版次审计或书目锁。
- ChatGPT Library 已建立目录：`/课后习题单书Canonical_核验合流_2026-09-21/单书PDF`，并实际列出 19 个 PDF。
- Google Drive 已创建同名目录，但文件上传被 Drive 返回 `403 storageQuotaExceeded` 拒绝，因此 **Drive 目录目前没有这批 PDF**。
- 当前 GitHub 连接只完成本 README 与 `SHA256SUMS.txt` 的文本归档；**19 个 PDF 二进制尚未上传 GitHub**。不得把本清单的存在解释成 PDF 已在 GitHub 可下载。

## 19 册清单

1. `005_Peskin_Schroeder_An_Introduction_to_QFT_全习题详解_76of76_Canonical.pdf`
2. `019_Matthias_Blau_Lecture_Notes_on_General_Relativity_当前最全_213_reader_tasks_v014.pdf`
3. `02_涂振汉_多元复分析_全习题详解_110of110_Canonical.pdf`
4. `030_Preskill_Quantum_Computation_Quantum_Information_123unique_Canonical_v019.pdf`
5. `03_谭小江_多复分析与复流形引论_全习题详解_195of195_Canonical.pdf`
6. `046_陈省身_陈维桓_微分几何讲义_1983_250reader_750variants_v012_Canonical_AUDIT.pdf`
7. `050_J_Dixmier_谱理论讲义_第二版_正式题源审计_AUDIT_ONLY_Canonical.pdf`
8. `077_Sakurai_Napolitano_Modern_Quantum_Mechanics_3e_241of242_SOURCE_LOCK_Canonical.pdf`
9. `078_Simon_Topological_Quantum_v043_141math_solved_49final_certified_Canonical_AUDIT.pdf`
10. `082_Serge_Lang_Algebra_Revised_3e_习题详解_515formal_BOOK_END_OPEN_ExV30.pdf`
11. `088_白正国_沈一兵_黎曼几何初步_第三版_全习题详解_191of191_Canonical.pdf`
12. `093_陆金甫_关治_偏微分方程数值解法_第3版_全习题详解_70of70_Canonical.pdf`
13. `094_Chung_A_Course_in_Probability_Theory_后版连续数学账_513of513_EDITION_AUDIT.pdf`
14. `109_Rubakov_Classical_Theory_of_Gauge_Fields_258of258_Canonical.pdf`
15. `110_Weinberg_Quantum_Theory_of_Fields_Volume_I_65numbered_SOURCE_INVENTORY_AUDIT_Canonical.pdf`
16. `111_Mnev_Quantum_Field_Theory_BV_Chapter3_Project_Canonical.pdf`
17. `112_Radovanovic_Problem_Book_in_QFT_2e_222of222_Canonical.pdf`
18. `117_Weinberg_Quantum_Theory_of_Fields_Volume_II_46numbered_SOURCE_INVENTORY_AUDIT_Canonical.pdf`
19. `萧荫堂_复几何讲义_书目题源审计_v002.pdf`

## 尚未形成可验收独立 PDF

- 吴密霞、王松桂《线性模型引论（第二版）》：NOT STARTED。
- 胡迪鹤《应用随机过程引论》(1984)：PENDING REOPEN。
- Nesterov, *Lectures on Convex Optimization* (2018)：PENDING REOPEN。

仍需恢复唯一书目身份：统计学项目第三本、优化项目第三本、历史《应用随机过程》项目。

QFT 的“第三本/其他分支”已按 P&S、Rubakov、Weinberg I/II、Mnev、Radovanović 拆分，不再把 umbrella 重复计作一本新书。

## 校验文件

完整哈希见同目录的 `SHA256SUMS.txt`。GitHub 当前仅保存审计文本；PDF 的已验证副本位于 ChatGPT Library 与本轮交付 ZIP 中。
