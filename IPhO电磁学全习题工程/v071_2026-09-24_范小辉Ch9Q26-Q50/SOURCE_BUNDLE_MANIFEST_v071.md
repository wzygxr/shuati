# v071 文本源码归档说明

由于 GitHub Contents 接口不适合直接保存 9 MB PDF 和 61 MB 完整复现包，本目录把可复现的文本源码压缩后，以 Base64 分片保存。

## 归档内容

恢复后的 `v071_text_sources.tar.gz` 包括：累计 Markdown v006；Q26--Q50 增量 Markdown 和独立卷 Markdown；统一题位账 CSV；公式/结构验证脚本与报告；构建和账本更新脚本；README、发布清单、源页映射、剩余总账、外部参考、质量报告；PDF 信息、预检、字体和字节/渲染审计摘要。

## 校验值

- 拼接后的 Base64：`826fa72930598c4db5249480f08e4c51082753e6c76112ee26556b3755032e49`
- 解码后的 tar.gz：`f18278027978265ffac9b2bc0ad470d6e3ec9feb311b1f08510c992ee2a38cff`
- 分片数量：6，按 `part_000` 到 `part_005` 排序拼接。

## 恢复

```bash
bash reconstruct_text_sources_v071.sh
```

完整 PDF、源页图片、累计配图和 ZIP 在 ChatGPT v071 交付附件中；文本归档可用于恢复进度、继续写作、重跑验证和更新统一账。

## 连续断点

- 已完成：Ch8 Q1--Q79；Ch9 Q1--Q50。
- 当前正式进度：129/247。
- 下一题：Ch9 Q51。
