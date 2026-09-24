# DING-C3-v002 压缩增量恢复说明

4个文本分卷依次拼接、Base64解码、gzip解压，即可恢复权威增量Markdown。

```bash
cat DING-C3-v002_markdown.md.gz.b64.part001-of-004.txt \
    DING-C3-v002_markdown.md.gz.b64.part002-of-004.txt \
    DING-C3-v002_markdown.md.gz.b64.part003-of-004.txt \
    DING-C3-v002_markdown.md.gz.b64.part004-of-004.txt \
  | base64 -d | gzip -d \
  > 丁文革_第3章例3.21-3.49全解_合流增量_DING-C3-v002.md
```

恢复后字节数：55485

恢复后SHA-256：`a919dfb57b94b6598a25626d7cc62fcb24f2cf227e0ccdf5111b87739ac0725c`

压缩流SHA-256：`37204572b4c4c295280f2be5138f0c3b0a7726a5cf29c355eeb2a0a0d2875ced`
