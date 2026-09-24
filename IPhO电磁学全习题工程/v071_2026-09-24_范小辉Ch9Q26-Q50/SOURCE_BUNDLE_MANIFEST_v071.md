# v071 文本源码归档说明

GitHub 本目录持久保存版本范围、断点、校验值和验收报告。由于 GitHub Contents 接口不适合直接保存本轮约 9 MB 的累计 PDF、约 61 MB 的完整复现包，也没有在仓库中重复提交 Base64 分片，因此**仓库当前不包含 `source_bundle_parts/`**。

完整 PDF、完整 ZIP、累计 Markdown、增量 Markdown、统一账、验证脚本、源页图片和 Base64 文本归档均随本轮 ChatGPT v071 下载附件交付。

## ZIP 内文本归档

完整交付包内包含 `v071_text_sources.tar.gz`、对应 Base64 分片和恢复脚本。恢复后的归档包括：累计 Markdown v006；Q26--Q50 增量 Markdown和独立卷 Markdown；统一题位账 CSV；公式/结构验证脚本与报告；构建和账本更新脚本；README、发布清单、源页映射、剩余总账、外部参考、质量报告；PDF 信息、预检、字体和字节/渲染审计摘要。

## 校验值

- 拼接后的 Base64：`826fa72930598c4db5249480f08e4c51082753e6c76112ee26556b3755032e49`
- 解码后的 tar.gz：`f18278027978265ffac9b2bc0ad470d6e3ec9feb311b1f08510c992ee2a38cff`
- 分片数量：6，按 `part_000` 到 `part_005` 排序拼接。

## 恢复方法

先下载并解压本轮完整交付包，再进入其中含有 `source_bundle_parts/` 的目录运行：

```bash
bash reconstruct_text_sources_v071.sh
```

仓库内的同名脚本只是恢复命令记录；单独克隆 GitHub 目录而没有下载完整交付包时，脚本会明确提示缺少分片并退出，不会伪装恢复成功。

## 连续断点

- 已完成：Ch8 Q1--Q79；Ch9 Q1--Q50。
- 当前正式进度：129/247。
- 下一题：Ch9 Q51。
