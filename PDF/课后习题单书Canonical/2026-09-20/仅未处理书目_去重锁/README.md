# 先查此文件：禁止重复处理

本目录是课后习题工程的永久去重锁。

## 当前结论

- Library 已核验 **119 本单书 Canonical**，这些书全部禁止从头重做。
- 另有陈省身-陈维桓、Dixmier、萧荫堂 3 个包外审计项目，也已处理，不再重复开工。
- 当前高置信、书名明确且真正零起点的书只有 5 本，见 `真正零起点_5本_只处理这些新书.csv`。
- 统计学第三本、优化第三本仍缺书目身份，不猜书名、不伪造答案。

开始任何新书前：

1. 先查 Library 中的 `Library去重锁_机器可读.json`。
2. 命中 `baseline_119` 或 `processed_outside_119` 时，禁止从头重做。
3. ACTIVE/PARTIAL 也属于已经处理；用户明确要求继续时，只从真实断点前推。
4. 只处理 `true_zero_start_named_books`；匿名槽必须先恢复书目身份。
5. 新书一书一 PDF；批量交付保存 ZIP、SHA-256、CSV/JSON，并上传 Library。
6. 本 GitHub 目录只保存去重元数据和校验值；完整 PDF/ZIP 已上传 ChatGPT Library：`/课后习题单书Canonical_2026-09-20/仅未处理书目_去重锁_20260920/`。

完整工作包 ZIP SHA-256：

```text
dff6e65d09955fbe0b059c73212f9f9594204f9b43fd509a611d9e2f2966b35c
```
