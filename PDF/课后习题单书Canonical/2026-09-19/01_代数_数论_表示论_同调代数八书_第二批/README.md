# 代数・数论・表示论・同调代数八书：单书 Canonical 第二批（2026-09-19）

## 处理结果

|序号|书目|严格状态|页数|SHA-256|
|---:|---|---|---:|---|
|1|Jürgen Neukirch, *Algebraic Number Theory*|294/294 formal Exercises complete|1020|`a5f158fcd9474243efeddf978e62393447ca88f224f67564d303f2fbe3de1655`|
|2|Etingof et al., *Introduction to Representation Theory*|98/98 formal complete|339|`dc81e9103fc26b763e187360894a2ea969bb5ae9c240e3b3e18f71620a16df15`|
|3|Joe Harris, *Algebraic Geometry: A First Course*|301/301 formal complete；QA2 maintenance|639|`eebbe6143edb68fbced70705fc9ed4675fc54a091061fd4082c328fcd8beaa50`|
|4|Tom M. Apostol, *Introduction to Analytic Number Theory*|287/287 numbered formal complete|594|`00f527b02cb2e6e2d4eae4169b3041544217c6ba8f03746810e03a10e8556c73`|
|5|Joseph J. Rotman, *An Introduction to Homological Algebra*, 2e|301/301 numbered formal complete|532|`d2462a1c69e6e4fec423e02788b5d20bccc5a1b0373b9b4675b32867512db9ce`|
|6|Humphreys, *Representations of Semisimple Lie Algebras in the BGG Category O*|66/66 explicit formal Exercises complete|302|`6f6c83da5703f49b35d47e88bd020e4310b2387cc4e78bd12019ffa590e3f401`|
|7|Fulton–Harris, *Representation Theory: A First Course*|485 processed/formal positions；全项目当前总账标为 DONE|754|`546d131369855bae29b733a549be71033c6570f356fc20b7a200659fa4ca65ec`|
|8|Serre, *Linear Representations of Finite Groups*|104/104 current canonical formal ledger complete|194|`2eb26fa92592ed4beabb7a4d135e0835681e7dfdac59ff7cc8567b25b8a596ea`|

## 版本裁决

- **Neukirch**：采用独立终卷 v030，而不是更早 v026 清零快照。
- **Etingof**：采用 v1.0。该版把旧口径 102 校正为正式出版版 98，并完整闭合；不能因 v0.4 文件更大而回退。
- **Harris**：采用 v021 QA2；不虚增 formal，吸收 v020 全书闭环并继续二轮正确率与来源审计。
- **Apostol**：Drive 中两个同名 v007 PDF 字节数一致，选较晚创建的副本作为源快照。
- **Rotman**：两个同名 v013 分支中选较晚且更大的分支：1,948,823 bytes；旧支为 1,916,729 bytes。
- **Humphreys BGG O**：采用 v007 全书终卷，覆盖 v005 Chapters 1–7 及更早快照。
- **Fulton–Harris / Serre**：从 `表示论三书_已交付解答合订本_FH及Serre全书.pdf` 无损拆分。物理页 1–754 为 Fulton–Harris；页 754 明确宣布下一阶段进入 Serre。物理页 755–948 为 Serre；页 755 是 Serre 新卷封面。Hall 不在该合订本中。

Fulton–Harris 历史末页仍保留三项来源待核说明，而全项目最新总账把该书列为 DONE 485。本批保留这个历史证据，不通过删除旧稿掩盖口径差异。

## PDF QA

- 8 本合计 **4,374 页**、**12,347 个书签**、**3,808 个链接对象**。
- 所有 PDF 均可打开、未加密；无文本层页面 0，非法页面矩形 0。
- Ghostscript 对 8 本 PDF 与 QA 报告做全页解析，全部通过。
- 每本抽查封面、前部、中页、末页；Fulton–Harris / Serre 另核合订本接缝。
- 完整 ZIP 已通过 `unzip -t`。

## 存储状态与删除政策

- 8 本单书 PDF、QA、CSV、SHA-256、ZIP 已持久化到 ChatGPT Library。
- Google Drive 已创建目标目录 `01_代数_数论_表示论_同调代数八书_第二批`，但账号存储配额此前已拒绝二进制新增上传，因此没有虚报 PDF 已上传。
- GitHub 本目录保存审计账与哈希；PDF 二进制等待可用的大文件通道或 Drive 配额恢复后同步。
- 本批没有删除任何旧版本。只有新 canonical 远端上传成功、重新打开、页数与 SHA-256 复核通过，并确认旧文件不是独立分支、回滚点或唯一题源证据后，才按精确文件 ID 清理。
