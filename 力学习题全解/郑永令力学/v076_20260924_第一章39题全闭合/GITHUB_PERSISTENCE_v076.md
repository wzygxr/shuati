# GitHub 持久化状态 v076

- 仓库：`wzygxr/shuati`
- 基线分支：`mechanics-v075-20260924`
- 本轮分支：`mechanics-v076-zheng-ch1-20260926`
- Pull Request：`#40`
- PR 标题：`力学习题全解 v076：郑永令第二版第一章 1-1—1-39 全闭合`
- PR 状态：OPEN
- 本轮新增 GitHub 文本文件：16
- Markdown 正文：按 `part_01`—`part_04` 四个文件完整持久化。
- 题位、进度、QA、最终验收和剩余总账：均已写入。
- Round-trip：已通过 GitHub `fetch_file` 回读 `PROGRESS_v076.json`，内容、版本、完成范围和下一断点与本地文件一致。

## 二进制边界

当前 GitHub 连接器只写入 UTF-8 文本，PDF、PNG 和 ZIP 没有伪装成已上传。它们保存在本轮完整交付包中；GitHub 额外保存资产清单与 SHA-256，便于核对和恢复。
