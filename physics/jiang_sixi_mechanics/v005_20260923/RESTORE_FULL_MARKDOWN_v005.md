# 恢复江四喜力学全解 v005 累计 Markdown

仓库中的两个 `.b64` 分片按文件名顺序拼接后，是累计 Markdown 的 `gzip + base64` 快照。

```bash
cat 江四喜_累计v005_完整Markdown.md.gz.part*.b64 \
  | base64 -d \
  | gzip -d \
  > 江四喜_物理竞赛专题精编_力学全解_累计v005_专题1至6共45题.md
```

恢复后可核对：

```bash
wc -l 江四喜_物理竞赛专题精编_力学全解_累计v005_专题1至6共45题.md
# 16731
```

PDF、ZIP 与 PNG 插图属于二进制文件，GitHub 当前连接动作只直接写 UTF-8 文本，因此它们保留在本轮 ChatGPT 交付件中。累计 Markdown 可由这两个分片无损恢复。
