# DING-C3-v001 GitHub 无损恢复说明（gzip + Base64）

将5个 `gzip_base64_partNNN_of005.txt` 按编号直接拼接，再Base64解码并解压：

```bash
cat 丁文革_DING-C3-v001_增量Markdown_gzip_base64_part*_of005.txt > increment.md.gz.b64
base64 -d increment.md.gz.b64 > increment.md.gz
gzip -dc increment.md.gz > 丁文革_第3章例3.1-3.20全解_合流增量_DING-C3-v001.md
sha256sum 丁文革_第3章例3.1-3.20全解_合流增量_DING-C3-v001.md
```

恢复后的Markdown应为53,499字节，SHA-256：

```text
1e19a2211b98925ef882d5a00f814f9057065eb8bfe455645ca4b7f307f0cd82
```

压缩字节流SHA-256：

```text
42162f0a3a8c2160cd6c10f834824f2dd748e2cfc352c3d616398b6e9e7e92b8
```
