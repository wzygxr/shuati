#!/usr/bin/env python3
"""（可选）生成报告用的中文字体子集（TTF）。

注意：md2pdf.py 默认直接用完整的 /home/user/fonts/SimHei.ttf —— reportlab 嵌入 TTF 时
会自动子集化，PDF 体积不会因为用完整字体而膨胀；另外子集必须包含标题/页脚里出现的字符，
否则那些字符会显示成方框。下面的脚本只在需要手工控制字体文件时使用。


reportlab 只能嵌入 TrueType 轮廓的字体，而 Noto/SourceHan 的 .otf 是 CFF 轮廓，
所以流程是： 收集正文用到的所有字符 → fontTools 子集化 (仍为 CFF) → otf2ttf 转成
TrueType 子集 (几十 KB) → 供 md2pdf.py 使用。

字体来源： pip install mplfonts 后位于 site-packages/mplfonts/fonts/ 下，
已在本机解包到 /home/user/fonts/（该目录不在 git 仓库内，避免把大字体提交进仓库）。

用法： python3 prepare_font.py 文本文件1 文本文件2 ...
"""
from __future__ import annotations

import os
import sys

from fontTools import subset

# 注意：SimHei.ttf 本身是 TrueType 轮廓，可以直接被子集化后交给 reportlab；
# 而 Noto/SourceHan 的 .otf 是 CFF 轮廓，需要 otf2ttf 转换，实测该转换会静默丢掉部分
# 汉字字形（拟/程/阶 …），因此这里统一使用 SimHei.ttf。
FONT_SRC = "/home/user/fonts/SimHei.ttf"
FONT_OUT = "/home/user/fonts/SimHei-subset.ttf"
EXTRA_CHARS = (
    " 　"
    "，。、；：？！（）【】《》“”‘’—…·％×÷≈≤≥≠±∑√∈⊂∪∩∀∃→←↑↓↔"
    "①②③④⑤⑥⑦⑧⑨⑩⑪⑫⑬⑭⑮⑯⑰⑱⑲⑳°′″μσαβγδηθλνρτφχψωΓΔΘΛΞΠΣΦΨΩ"
    "\u0302\u0304\u0303"          # 组合用的 hat / bar / tilde
    "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
    " .,;:!?()[]{}+-*/=<>|~^%$#@&_'\"\\`~"
    "\u2013\u2014\u2018\u2019\u201c\u201d\u2026\u00b7\u00a0\u00d7\u00f7"
)


def build(paths: list[str], out: str = FONT_OUT) -> str:
    text = set(EXTRA_CHARS)
    for p in paths:
        with open(p, encoding="utf-8") as f:
            text |= set(f.read())
    text = "".join(sorted(c for c in text if c.isprintable()))
    opts = subset.Options()
    opts.layout_features = ["*"]
    opts.name_IDs = ["*"]
    opts.notdef_outline = True
    opts.drop_tables += ["DSIG"]
    font = subset.load_font(FONT_SRC, opts)
    ss = subset.Subsetter(options=opts)
    ss.populate(text=text)
    ss.subset(font)
    subset.save_font(font, out, opts)
    # 校核：子集里确实包含所有需要的字符（避免静默丢字）
    from fontTools.ttLib import TTFont
    cmap = TTFont(out).getBestCmap()
    missing = sorted({c for c in text if ord(c) not in cmap and c.strip()})
    print(f"[prepare_font] {len(text)} 个字符 → {out} ({os.path.getsize(out) / 1024:.0f} KB)")
    if missing:
        print("[prepare_font] !! 缺字 %d 个: %s" % (len(missing), "".join(missing[:60])))
    return out


if __name__ == "__main__":
    build(sys.argv[1:] or [])
