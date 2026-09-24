# v074 文本源码归档说明

完整 PDF、页图和完整复现 ZIP 由本轮 ChatGPT 附件交付。GitHub 通过这里的文本分片恢复累计 Markdown、统一账、脚本和报告。

## 校验值

- 拼接后的 Base64：`7ab83f94d0abae8e0e7168504e0e655d538953498790d7bc79975cdbc0aba04b`
- 解码后的 tar.gz：`1ed23a5c4c57368f31e4f790ae0675c820ca437bdb17c8c0d24b854597271154`
- 分片数量：8，按 `part_000` 起顺序拼接。

## 恢复

```bash
bash reconstruct_text_sources_v074.sh
```

恢复后进入 `v074_text_sources`，运行：

```bash
bash build_release_v074.sh
```

可以重跑题位账、公式/结构验证与 PDF 编译。

## 连续断点

- 已完成：Ch8 Q1--Q79；Ch9 Q1--Q66；Ch10 Q1--Q76。
- 当前正式进度：221/247。
- 下一题：Ch11 Q1。
