# 范小辉两书 Ch8-11 电磁学累计工程 v067

本版本直接在 v066 的 Q1-Q10 后连续推进，新增《实用题典》Ch8 Q11-Q21，并把《奥赛指导》Ch8 §2 的三个方法例并入累计讲义。

## 当前进度

- 正式分母：247 题
- 累计完成：21 题
- 本批新增：11 题
- 尚余：226 题
- 每道正式题均含 2 道本质不同变式与 1 道同类自检
- 下一断点：《实用题典》Ch8 Q22；《奥赛指导》Ch8 §3“电势”印刷页274起

## 主文件

- `范小辉两书_Ch8-11电磁学_累计主文档_v002.md`
- `范小辉两书_Ch8-11电磁学_累计主文档_v002.pdf`
- `范小辉两书_Ch8-11_统一账_v067.csv`
- `范小辉两书_Ch8Q11-Q21_公式验证_v067.py`
- `范小辉两书_Ch8Q11-Q21_验证报告_v067.txt`
- `范小辉两书_Ch8Q11-Q21_质量验收报告_v067.txt`
- `RELEASE_MANIFEST_v067.json`
- `SHA256SUMS_v067.txt`
- `draw_clean_diagrams_v066.py` 与 `draw_clean_diagrams_v067.py`
- `build_release_v067.sh`
- `PDF_Preflight_v067.json` 与 `PDF_Info_v067.txt`

## 编译

在本目录最省事的执行方式是：

```bash
bash build_release_v067.sh
```

也可以只编译主文档：

```bash
pandoc '范小辉两书_Ch8-11电磁学_累计主文档_v002.md' \
  --from='markdown+tex_math_single_backslash+raw_tex' \
  --pdf-engine=xelatex \
  --resource-path=. \
  -o '范小辉两书_Ch8-11电磁学_累计主文档_v002.pdf'
```

需要 Noto Serif CJK、Noto Sans CJK、DejaVu Sans Mono 与 Latin Modern Math。图片位于 `assets/`。

## 继续规则

1. 从 Q22 连续追加，禁止返回重复生成 Q1-Q21；
2. 《实用题典》作为正式题号分母，《指导》只作为理论与方法例层；
3. 同源题只建映射，不重复计数；
4. 每次发布都更新 Markdown、PDF、CSV、验证报告和剩余总账；
5. PDF 必须同时通过编译、预检、EOF/startxref、Ghostscript 和全页渲染。
