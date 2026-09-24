# v073 文本源码归档说明

累计 PDF 和完整复现包体积较大，当前对话附件提供完整二进制交付；本目录保存可复现文本源码归档的校验值和恢复脚本。

## 归档内容

恢复后的 `v073_text_sources.tar.gz` 包含：

- 累计 Markdown v008；
- Q17--Q50 增量 Markdown 和可独立编译 Markdown；
- 247 题统一账 CSV；
- 公式/结构验证脚本与报告；
- 账本更新、累计构建和发布脚本；
- README、发布清单、源页映射、剩余总账、外部参考和质量报告；
- PDF 信息、预检、字体、字节和渲染审计摘要；
- SHA-256 校验表。

## 校验值

- 拼接后的 Base64：`403638201e3dec164cc7b285c2d817161c1c2ba2c43451e2acaaea293cf22862`
- 解码后的 tar.gz：`29e2776838ff4173ca284601417f0bbfa06bf7f466f31337e8ef1600c1612880`
- 分片数量：6，按 `part_000` 至 `part_005` 排序拼接。

## 恢复

```bash
bash reconstruct_text_sources_v073.sh
```

恢复后运行：

```bash
cd v073_text_sources
bash build_release_v073.sh
```

## 连续断点

- 已完成：Ch8 Q1--Q79；Ch9 Q1--Q66；Ch10 Q1--Q50。
- 当前正式进度：195/247。
- 下一题：Ch10 Q51。
