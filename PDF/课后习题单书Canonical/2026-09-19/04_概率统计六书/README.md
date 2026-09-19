# 概率统计六书：单书 Canonical（2026-09-19）

## 结果

|序号|书目|严格状态|页数|书签|链接|SHA-256|
|---:|---|---|---:|---:|---:|---|
|1|Rick Durrett, *Probability: Theory and Examples*, 4th ed.|448/448 numbered formal complete|2110|25|4328|`870113555f1f934615bcb71282c0dd4062b56a73503395a52aed56d7cd6fa315`|
|2|Casella & Berger, *Statistical Inference*, 2nd ed.|624/624 formal complete|840|0|12|`e9f24954082f01f5babafc8e1c5f574a1fee94612ee20b228f935f98098133be`|
|3|Kai Lai Chung, *A Course in Probability Theory*|513/513 later-edition continuous mathematics ledger complete；1968 first-edition mapping pending|889|20|1921|`69a5869fe707cb743bed9be3fbcfe7cf2ccf2b98b4e8def1838465c35e110c0d`|
|4|郑明、陈子毅、汪嘉冈《数理统计讲义》|177/177 formal complete|316|939|939|`a4db8d11c6685c338773b24952b5bec7f043c049a5f11a5847b8a02110becd14`|
|5|陈希孺《数理统计学教程》|121/121 formal complete|198|661|669|`f23c31af0b793778d32c349a0465ce3ab3661e7088c6d5bf92d0a5b4ebc1ae89`|
|6|陈家鼎等《数理统计学讲义（第2版）》|126/126 formal complete|182|463|473|`e8957be9cf856ae304815ae5a612ed2a54bdb1a2ebc33481a3cadb1a4c6f95ce`|

## 版本裁决

- **Durrett**：采用第026批全书闭合累计 PDF；覆盖 Chapters 1–8 和 Appendix A。末部是项目状态页，不含下一本书的解答正文。
- **Casella–Berger**：采用第012批 Chapter 1–12 全书闭合终卷。封面沿用历史第011批基线，但末页总账明确 624/624。
- **Chung**：从八书第046批合订本物理页 **2119–3007** 无损抽出。严格表述为“后版连续数学账 513/513”；1968 初版 SAME/MODIFIED/ADDED/DELETED/RENUMBERED 页级映射仍待完成，不能冒称目标初版已清零。
- **郑明等**：采用独立 v006 177题闭合终卷，不采用同时夹带另外两本书部分正文的 v007 umbrella。
- **陈希孺《数理统计学教程》**：采用 v007 121题全闭环。Drive 中两个同名 v007 副本大小均为 3,202,764 bytes，实际 SHA-256 完全相同；封面仍写 v005 属于封面元数据滞后，末页是 v007 最终账。
- **陈家鼎等**：采用 v009 126题闭合终卷。

## PDF QA

- 6 本合计 **4,535 页**、**2,108 个书签**、**8,342 个链接对象**。
- 所有页面均有文本层；全部 PDF 未加密，页面尺寸合法。
- 每本抽查封面、前部、1/3、2/3、末页；Chung 首尾与拆分边界单独检查。
- Ghostscript 对 6 本 PDF 和 QA 报告逐页完整解析，全部通过。
- 完整交付 ZIP 已通过 `unzip -t`。

## 存储状态与删除政策

- 6 本 PDF、QA、CSV、SHA-256 和 ZIP 已持久化到 ChatGPT Library。
- Google Drive 已创建目标目录 `04_概率统计六书`，但账号存储配额此前已拒绝新增二进制上传，因此本轮没有虚报 PDF 已上传。
- 本 GitHub 目录保存审计账与哈希。旧版本暂不删除；只有新 canonical 远端二进制上传、重新打开、页数和 SHA-256 复核通过，并确认旧文件不是独立分支或唯一题源证据后，才按精确文件 ID 清理。
