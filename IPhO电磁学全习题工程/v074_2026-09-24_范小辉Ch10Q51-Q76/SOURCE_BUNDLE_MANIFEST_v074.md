# v074 文本源码归档说明

完整 PDF、页图、完整复现 ZIP，以及 `source_bundle_parts/` 八个分片由本轮 ChatGPT 附件中的完整交付包提供。GitHub 目录保存版本索引、校验值和恢复脚本，不把尚未上传的大体积二进制内容虚报为已持久化。

## 交付包内的文本归档校验值

- 拼接后的 Base64：`7ab83f94d0abae8e0e7168504e0e655d538953498790d7bc79975cdbc0aba04b`
- 解码后的 tar.gz：`1ed23a5c4c57368f31e4f790ae0675c820ca437bdb17c8c0d24b854597271154`
- 分片数量：8，按 `part_000` 至 `part_007` 排序拼接。

## 恢复

从完整交付 ZIP 中取出 `source_bundle_parts/` 和 `reconstruct_text_sources_v074.sh`，置于同一目录，然后运行：

```bash
bash reconstruct_text_sources_v074.sh
```

恢复后进入 `v074_text_sources`，运行：

```bash
bash build_release_v074.sh
```

可重跑题位账、公式/结构验证与 PDF 编译。

## 连续断点

- 已完成：Ch8 Q1--Q79；Ch9 Q1--Q66；Ch10 Q1--Q76。
- 当前正式进度：221/247。
- 下一题：Ch11 Q1。
