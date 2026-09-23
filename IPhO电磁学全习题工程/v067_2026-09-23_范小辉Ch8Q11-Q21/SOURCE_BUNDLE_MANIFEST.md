# v067 文本源码归档说明

由于 Google Drive 与 ChatGPT Library 当前均已达到存储限额，本版本把可复现的文本源码以 gzip + Base64 分片保存到 GitHub。

## 归档内容

恢复后的 `v067_text_sources.tar.gz` 包含：

- `范小辉两书_Ch8-11电磁学_累计主文档_v002.md`
- `范小辉两书_Ch8-11_统一账_v067.csv`
- `范小辉两书_Ch8Q11-Q21_公式验证_v067.py`
- `范小辉两书_Ch8Q11-Q21_验证报告_v067.txt`
- `范小辉两书_Ch8Q11-Q21_质量验收报告_v067.txt`
- `RELEASE_MANIFEST_v067.json`
- `README_v067.md`
- `PDF_Preflight_v067.json`
- `PDF_Info_v067.txt`
- `append_v067.md`
- `build_cumulative_v067.py`
- `update_ledger_v067.py`
- `draw_clean_diagrams_v066.py`
- `draw_clean_diagrams_v067.py`
- `build_release_v067.sh`
- `SHA256SUMS_v067.txt`

配图不重复存二进制副本，运行两个 `draw_clean_diagrams_*.py` 即可无损重建 26 张 PNG。PDF 由 Markdown、配图和 XeLaTeX 重建。

## 校验值

- 拼接后的 Base64：`0ba710ae2c0a9a017e56d751453951f9a456753d71bebabc35bdab6dee8fb9a0`
- 解码后的 tar.gz：`2b34b9aca3a877de9ca3168bf22c43e884b83c7dfaa46c2ae8fbad8fc165c5ba`
- 本轮累计 Markdown：`7330c7b9ea45bd067bb95dec8c03d356a04743ba8d263d1e7b4ba245dbe98737`
- 本轮累计 PDF：`25a86c675be7e1aa1f6ee8152a7cce36b92cb6f5df78b03ca665e9f406d88ade`
- 本轮统一账：`272d03f91495b3b5e3aeb0555a0b390d03a049d913ec4d12e0c57141a4257b5a`

## 恢复与编译

```bash
bash reconstruct_text_sources.sh
bash build_release_v067.sh
```

第二条命令会重新生成配图、编译累计 PDF，并运行 22 项公式与结构复算。

## 连续断点

- 已完成：《实用题典》Ch8 Q1—Q21。
- 下一题：《实用题典》Ch8 Q22。
- 《奥赛指导》下一理论段：Ch8 §3“电势”，印刷页 274 起。
