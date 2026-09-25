#!/usr/bin/env python3
"""极简 Markdown → PDF 转换器（reportlab + matplotlib mathtext）。

为什么不用 pandoc/LaTeX：本机没有 TeX 发行版也装不上（apt 源不可达），
所以用 matplotlib 的 mathtext 把 $...$ / $$...$$ 公式渲染成透明 PNG 再嵌入 PDF。

支持的语法子集
  # ## ### 标题        段落             - / 1. 列表        > 引用
  | 表格 |              ``` 代码块 ```   $$ 行间公式 $$     $ 行内公式 $
  **粗体**  `行内代码`  ![说明](图片路径)  --- 分隔线

用法
  python3 md2pdf.py solution.md solution.pdf --title "标题" [--toc]

公式写法注意：mathtext 不支持 \\begin{...}/\\Big/\\displaystyle/\\overbrace，
请改用 \\left( \\right)、\\binom、\\overline 等（本脚本遇到无法解析的公式会自动
退回成等宽字体原文，不会中断转换）。
"""
from __future__ import annotations

import argparse
import hashlib
import os
import re
import sys
import tempfile

import matplotlib

matplotlib.use("Agg")
import matplotlib.pyplot as plt  # noqa: E402
from matplotlib import mathtext  # noqa: E402
from matplotlib.font_manager import FontProperties  # noqa: E402

from reportlab.lib import colors  # noqa: E402
from reportlab.lib.enums import TA_CENTER, TA_JUSTIFY, TA_LEFT  # noqa: E402
from reportlab.lib.pagesizes import A4  # noqa: E402
from reportlab.lib.styles import ParagraphStyle  # noqa: E402
from reportlab.lib.units import cm  # noqa: E402
from reportlab.pdfbase import pdfmetrics  # noqa: E402
from reportlab.pdfbase.ttfonts import TTFont  # noqa: E402
from reportlab.platypus import (BaseDocTemplate, Frame, Image, KeepTogether,  # noqa: E402
                                PageTemplate, Paragraph, Spacer, Table, TableStyle,
                                XPreformatted)

# --- reportlab 的 CJK 断行遇到空文本片段（例如行内 <img>）会抛
#     `ord() expected a character, but string of length 0 found`，这里打上补丁。
def _patch_reportlab_cjk():
    import inspect
    import reportlab.platypus.paragraph as _p
    src = inspect.getsource(_p.cjkFragSplit)
    src = src.replace("                if ord(u)<0x3000:",
                      "                if len(u) and ord(u)<0x3000:")
    src = src.replace("if uj and category(uj)=='Zs' or ord(uj)>=0x3000:",
                      "if (uj and category(uj)=='Zs') or (uj and ord(uj)>=0x3000):")
    ns = _p.__dict__
    exec(compile(src, "<patched cjkFragSplit>", "exec"), ns)
    _p.cjkFragSplit = ns["cjkFragSplit"]


_patch_reportlab_cjk()

matplotlib.rcParams["mathtext.fontset"] = "cm"
matplotlib.rcParams["mathtext.default"] = "regular"

CACHE_DIR = os.path.join(tempfile.gettempdir(), "md2pdf-math-cache")
os.makedirs(CACHE_DIR, exist_ok=True)

# 中文字体（SimHei）缺少的少数符号（如 ö、⇒、²）用 DejaVu Sans 兜底；
# reportlab 嵌入 TTF 时会自动做子集化，所以直接用完整的 SimHei.ttf 即可（PDF 体积不会变大）。
LATIN_FALLBACK = "/home/user/.venv/lib/python3.11/site-packages/matplotlib/mpl-data/fonts/ttf/DejaVuSans.ttf"
MISSING: set[str] = set()          # 中文字体缺失、需要回退到 Latin 字体的字符
DEFAULT_SONG = "/home/user/fonts/SimHei.ttf"
DEFAULT_HEI = "/home/user/fonts/SimHei.ttf"

_parser = mathtext.MathTextParser("path")


# --------------------------------------------------------------------- 字体
def register_fonts(song: str, hei: str) -> None:
    pdfmetrics.registerFont(TTFont("Song", song))
    pdfmetrics.registerFont(TTFont("Hei", hei))
    pdfmetrics.registerFont(TTFont("Latin", LATIN_FALLBACK))
    from reportlab.pdfbase.pdfmetrics import registerFontFamily
    registerFontFamily("Song", normal="Song", bold="Hei", italic="Song", boldItalic="Hei")
    registerFontFamily("Hei", normal="Hei", bold="Hei", italic="Hei", boldItalic="Hei")
    registerFontFamily("Latin", normal="Latin", bold="Latin", italic="Latin", boldItalic="Latin")


# --------------------------------------------------------------------- 公式渲染
def render_math(tex: str, fontsize: float, display: bool = False) -> tuple[str, float, float, float]:
    """把一段 LaTeX 公式渲染成 PNG，返回 (路径, 宽pt, 高pt, 基线深pt)。"""
    key = hashlib.md5(f"{tex}|{fontsize}|{display}".encode()).hexdigest()[:16]
    path = os.path.join(CACHE_DIR, f"{key}.png")
    try:
        width, height, depth, _, _ = _parser.parse(f"${tex}$", dpi=72,
                                                   prop=FontProperties(size=fontsize))
    except Exception:
        return "", 0.0, 0.0, 0.0                      # 交给调用方退回原文
    if not os.path.exists(path):
        if display:
            fig = plt.figure(figsize=(0.01, 0.01))
            fig.text(0, 0, f"${tex}$", fontsize=fontsize)
            fig.savefig(path, dpi=400, transparent=True, bbox_inches="tight",
                        pad_inches=0.01)
            plt.close(fig)
        else:
            fig = plt.figure(figsize=(0.01, 0.01))
            fig.patch.set_alpha(0.0)
            fig.text(0, 0, f"${tex}$", fontsize=fontsize)
            fig.savefig(path, dpi=400, transparent=True, bbox_inches="tight",
                        pad_inches=0.01)
            plt.close(fig)
    return path, width, height, depth


def math_img_tag(tex: str, fontsize: float) -> str:
    path, w, h, d = render_math(tex, fontsize, display=False)
    if not path:
        BAD_MATH.append(tex)
        return f'<font face="Courier" size="{fontsize - 1:.0f}"> {tex} </font>'
    return (f'<img src="{path}" width="{w:.2f}" height="{h:.2f}" '
            f'valign="{-d:.2f}"/>')


BAD_MATH: list[str] = []


def display_math_flowable(tex: str, fontsize: float, avail_width: float):
    path, w, h, d = render_math(tex, fontsize, display=True)
    if not path:
        BAD_MATH.append(tex)
        st = ParagraphStyle("badmath", fontName="Courier", fontSize=8, leading=11,
                            wordWrap="CJK", textColor=colors.HexColor("#aa2222"),
                            spaceBefore=3, spaceAfter=3, borderPadding=3,
                            backColor=colors.HexColor("#fff5f5"))
        return Paragraph(code_markup(tex, 8.5), st)
    scale = min(1.0, (avail_width * 0.96) / max(w, 1e-6))
    return Image(path, width=w * scale, height=h * scale, hAlign="CENTER")


# --------------------------------------------------------------------- 行内解析
def esc(t: str) -> str:
    return t.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")


def latin_fallback(text: str) -> str:
    """把中文字体里没有的字符（如 ö、⇒）包进 DejaVu 字体，避免显示成方框。"""
    if not MISSING:
        return text
    out, buf, in_latin = [], [], False

    def flush():
        if buf:
            run = esc("".join(buf))
            out.append(f'<font face="Latin">{run}</font>' if in_latin else run)
            buf.clear()

    for ch in text:
        flag = ch in MISSING
        if flag != in_latin:
            flush()
            in_latin = flag
        buf.append(ch)
    flush()
    return "".join(out)


def code_markup(text: str, size: float) -> str:
    """等宽字体里没有中文字形，把中日韩字符切成 Song 字体的小段。"""
    out, buf, cjk = [], [], False

    def flush():
        if buf:
            run = latin_fallback("".join(buf))
            out.append(run if not cjk else f'<font face="Song" size="{size:.1f}">{run}</font>')
            buf.clear()

    for ch in text:
        is_cjk = ord(ch) > 0x2E80
        if is_cjk != cjk:
            flush()
            cjk = is_cjk
        buf.append(ch)
    flush()
    return "".join(out)


class Inliner:
    """Markdown 行内语法 → reportlab Paragraph 标记。

    处理顺序：`行内代码` → $行内公式$ → ![图]()/[链接]() → **粗体**（可嵌套公式）。
    """

    def __init__(self, base_dir: str, fontsize: float):
        self.base_dir = base_dir
        self.fontsize = fontsize

    def __call__(self, text: str, allow_bold: bool = True) -> str:
        out, i, n = [], 0, len(text)
        buf: list[str] = []

        def flush():
            if buf:
                out.append(latin_fallback("".join(buf)))
                buf.clear()

        while i < n:
            ch = text[i]
            if ch == "$":                                   # 行内公式
                j = text.find("$", i + 1)
                if j > i:
                    flush()
                    out.append(math_img_tag(text[i + 1:j], self.fontsize))
                    i = j + 1
                    continue
            if ch == "`":                                   # 行内代码
                j = text.find("`", i + 1)
                if j > i:
                    flush()
                    out.append(f'<font face="Courier" size="{self.fontsize - 0.5:.1f}">'
                               f'{code_markup(text[i + 1:j], self.fontsize - 1.2)}</font>')
                    i = j + 1
                    continue
            if allow_bold and text.startswith("**", i):     # 粗体（内部仍可含公式）
                j = text.find("**", i + 2)
                if j > i:
                    flush()
                    out.append(f"<b>{self(text[i + 2:j], allow_bold=False)}</b>")
                    i = j + 2
                    continue
            if ch == "!" and text.startswith("![", i):       # 图片（行内忽略，交给块解析）
                j = text.find("]", i)
                k = text.find(")", j)
                if 0 < j < k:
                    flush()
                    i = k + 1
                    continue
            if ch == "[":                                    # 链接 → 只留文字
                j = text.find("]", i)
                k = text.find(")", j)
                if 0 < j < k:
                    flush()
                    out.append(self(text[i + 1:j], allow_bold=False))
                    i = k + 1
                    continue
            buf.append(ch)
            i += 1
        flush()
        return "".join(out)


# --------------------------------------------------------------------- 文档样式
def make_styles(fontsize: float, leading_mult: float = 1.65):
    s = {}
    s["h1"] = ParagraphStyle("h1", fontName="Hei", fontSize=fontsize + 5,
                             leading=(fontsize + 5) * 1.4, spaceBefore=6, spaceAfter=10,
                             textColor=colors.HexColor("#10315a"), alignment=TA_LEFT)
    s["h2"] = ParagraphStyle("h2", fontName="Hei", fontSize=fontsize + 2.5,
                             leading=(fontsize + 2.5) * 1.4, spaceBefore=14, spaceAfter=6,
                             textColor=colors.HexColor("#15507f"))
    s["h3"] = ParagraphStyle("h3", fontName="Hei", fontSize=fontsize + 0.5,
                             leading=(fontsize + 0.5) * 1.4, spaceBefore=10, spaceAfter=4,
                             textColor=colors.HexColor("#1f1f1f"))
    s["body"] = ParagraphStyle("body", fontName="Song", fontSize=fontsize,
                               leading=fontsize * leading_mult, alignment=TA_JUSTIFY,
                               wordWrap="CJK", spaceAfter=4)
    s["li"] = ParagraphStyle("li", parent=s["body"], leftIndent=16, bulletIndent=4,
                             spaceAfter=2)
    s["quote"] = ParagraphStyle("quote", parent=s["body"], leftIndent=18,
                                textColor=colors.HexColor("#444444"),
                                borderPadding=4)
    s["caption"] = ParagraphStyle("caption", fontName="Hei", fontSize=fontsize - 1.5,
                                  leading=(fontsize - 1.5) * 1.4, alignment=TA_CENTER,
                                  textColor=colors.HexColor("#555555"), spaceBefore=2,
                                  spaceAfter=8)
    s["table"] = ParagraphStyle("table", fontName="Song", fontSize=fontsize - 2,
                                leading=(fontsize - 2) * 1.35, wordWrap="CJK")
    s["tableh"] = ParagraphStyle("tableh", parent=s["table"], fontName="Hei")
    s["code"] = ParagraphStyle("code", fontName="Courier", fontSize=fontsize - 2.2,
                               leading=(fontsize - 2.2) * 1.3, backColor=colors.HexColor("#f4f4f4"),
                               borderPadding=5, leftIndent=4, spaceBefore=4, spaceAfter=6)
    s["title"] = ParagraphStyle("title", fontName="Hei", fontSize=fontsize + 9,
                                leading=(fontsize + 9) * 1.35, alignment=TA_CENTER,
                                textColor=colors.HexColor("#0d2b4e"), spaceAfter=6)
    s["subtitle"] = ParagraphStyle("subtitle", fontName="Song", fontSize=fontsize,
                                   leading=fontsize * 1.5, alignment=TA_CENTER,
                                   textColor=colors.HexColor("#555555"), spaceAfter=14)
    return s


# --------------------------------------------------------------------- 表格
def split_table_row(line: str) -> list[str]:
    """按 | 切分表格行，但忽略 $...$ 公式内部以及转义的 \\| 中的竖线。"""
    body = line.strip()
    if body.startswith("|"):
        body = body[1:]
    if body.endswith("|"):
        body = body[:-1]
    cells, buf, in_math, i = [], [], False, 0
    while i < len(body):
        ch = body[i]
        if ch == "\\":
            buf.append(ch)
            if i + 1 < len(body):
                buf.append(body[i + 1])
                i += 2
                continue
            i += 1
            continue
        if ch == "$":
            in_math = not in_math
        if ch == "|" and not in_math:
            cells.append("".join(buf).strip())
            buf = []
        else:
            buf.append(ch)
        i += 1
    cells.append("".join(buf).strip())
    return cells


def parse_table(lines: list[str]) -> list[list[str]]:
    rows = []
    for ln in lines:
        cells = split_table_row(ln)
        if all(re.fullmatch(r":?-{1,}:?", c or "-") for c in cells) and cells:
            continue                                    # 分隔行
        rows.append(cells)
    return rows


def build_table(rows: list[list[str]], styles, inliner, avail: float):
    ncol = max(len(r) for r in rows)
    rows = [r + [""] * (ncol - len(r)) for r in rows]
    data = []
    for i, r in enumerate(rows):
        st = styles["tableh"] if i == 0 else styles["table"]
        data.append([Paragraph(inliner(c), st) for c in r])
    # 列宽：按各列最长内容加权
    weights = []
    for j in range(ncol):
        w = max(len(re.sub(r"[\$`*]", "", r[j])) for r in rows)
        weights.append(max(w, 4) ** 0.85)
    tot = sum(weights)
    widths = [avail * w / tot for w in weights]
    t = Table(data, colWidths=widths, repeatRows=1, hAlign="CENTER")
    t.setStyle(TableStyle([
        ("GRID", (0, 0), (-1, -1), 0.4, colors.HexColor("#b9c4d0")),
        ("BACKGROUND", (0, 0), (-1, 0), colors.HexColor("#e8eef6")),
        ("VALIGN", (0, 0), (-1, -1), "MIDDLE"),
        ("LEFTPADDING", (0, 0), (-1, -1), 3),
        ("RIGHTPADDING", (0, 0), (-1, -1), 3),
        ("TOPPADDING", (0, 0), (-1, -1), 2.5),
        ("BOTTOMPADDING", (0, 0), (-1, -1), 2.5),
        ("ROWBACKGROUNDS", (0, 1), (-1, -1), [colors.white, colors.HexColor("#f7f9fc")]),
    ]))
    return t


# --------------------------------------------------------------------- 主解析
def convert(md_path: str, pdf_path: str, title: str | None = None,
            subtitle: str | None = None, fontsize: float = 10.5,
            song: str = DEFAULT_SONG, hei: str = DEFAULT_HEI) -> None:
    register_fonts(song, hei)
    base_dir = os.path.dirname(os.path.abspath(md_path))
    with open(md_path, encoding="utf-8") as f:
        raw = f.read()
    lines = raw.split("\n")

    # 中文字体缺哪些字符？（数学公式会被渲染成图片，这里只需处理正文）
    from fontTools.ttLib import TTFont as _TTFont  # noqa: PLC0415
    cmap = _TTFont(song).getBestCmap()
    MISSING.clear()
    MISSING.update(c for c in raw if c.strip() and ord(c) not in cmap)
    if MISSING:
        print("[md2pdf] 需要回退字体的字符：", "".join(sorted(MISSING))[:60])

    styles = make_styles(fontsize)
    inliner = Inliner(base_dir, fontsize)
    avail = A4[0] - 4.4 * cm
    story: list = []
    sources: list = []

    def push(flow, src):
        story.append(flow)
        sources.append(src)

    if title:
        story.append(Paragraph(inliner(title), styles["title"]))
        if subtitle:
            story.append(Paragraph(inliner(subtitle), styles["subtitle"]))
        story.append(Spacer(1, 2))

    i, n = 0, len(lines)
    skipped_h1 = False
    while i < n:
        line = lines[i]
        stripped = line.strip()

        if not stripped:
            i += 1
            continue

        if stripped.startswith("```"):                      # 代码块
            i += 1
            code = []
            while i < n and not lines[i].strip().startswith("```"):
                code.append(lines[i])
                i += 1
            i += 1
            push(XPreformatted("\n".join(code_markup(c, fontsize - 2.6) for c in code),
                                 styles["code"]), "```\n" + "\n".join(code[:3]))
            continue

        if stripped.startswith("$$"):                       # 行间公式
            buf = []
            if stripped.endswith("$$") and len(stripped) > 4:
                buf.append(stripped[2:-2])
                i += 1
            else:
                if stripped != "$$":
                    buf.append(stripped[2:])
                i += 1
                while i < n and not lines[i].strip().startswith("$$"):
                    buf.append(lines[i])
                    i += 1
                if i < n:
                    tail = lines[i].strip()
                    if tail != "$$":
                        buf.append(tail[:-2] if tail.endswith("$$") else tail)
                    i += 1
            push(Spacer(1, 4), "$$"); push(display_math_flowable(" ".join(b.strip() for b in buf).strip(),
                                               fontsize + 2.0, avail), "$$ " + " ".join(buf)[:120]); push(Spacer(1, 6), "$$")
            continue

        if re.match(r"^#{1,6}\s", stripped):                # 标题
            level = len(stripped) - len(stripped.lstrip("#"))
            txt = stripped[level:].strip()
            if title and level == 1 and not skipped_h1:
                skipped_h1 = True                           # 与 --title 重复，跳过
                if not subtitle:
                    story[0] = Paragraph(inliner(txt), styles["title"])
                i += 1
                continue
            key = "h1" if level == 1 else ("h2" if level == 2 else "h3")
            push(Paragraph(inliner(txt), styles[key]), txt)
            i += 1
            continue

        if re.fullmatch(r"(-{3,}|\*{3,}|_{3,})", stripped):  # 分隔线
            story.append(Spacer(1, 8))
            i += 1
            continue

        if stripped.startswith("|") and i + 1 < n and set(lines[i + 1].strip()) <= set("|-: "):
            rows = []
            while i < n and lines[i].strip().startswith("|"):
                rows.append(lines[i])
                i += 1
            story.append(Spacer(1, 3))
            push(build_table(parse_table(rows), styles, inliner, avail), rows[0][:120])
            story.append(Spacer(1, 8))
            continue

        if stripped.startswith("!["):                       # 独占一行的图片
            m = re.match(r"!\[(.*?)\]\((.*?)\)", stripped)
            if m:
                cap, rel = m.group(1), m.group(2)
                path = rel if os.path.isabs(rel) else os.path.join(base_dir, rel)
                if os.path.exists(path):
                    from reportlab.lib.utils import ImageReader  # noqa: PLC0415
                    iw, ih = ImageReader(path).getSize()
                    scale = min(1.0, avail / iw)
                    push(Image(path, width=iw * scale, height=ih * scale, hAlign="CENTER"), rel)
                    if cap:
                        push(Paragraph(inliner(cap), styles["caption"]), cap)
                    else:
                        story.append(Spacer(1, 6))
                else:
                    push(Paragraph(f"[缺少图片: {esc(rel)}]", styles["body"]), rel)
                i += 1
                continue

        if re.match(r"^([-*+]|\d+\.)\s", stripped):         # 列表
            while i < n and re.match(r"^([-*+]|\d+\.)\s", lines[i].strip()):
                item = re.sub(r"^([-*+]|\d+\.)\s+", "", lines[i].strip())
                marker = re.match(r"^(\d+\.)\s", lines[i].strip())
                push(Paragraph(inliner(item), styles["li"],
                               bulletText=(marker.group(1) if marker else "\u2022")), item)
                i += 1
            story.append(Spacer(1, 4))
            continue

        if stripped.startswith(">"):                        # 引用
            buf = []
            while i < n and lines[i].strip().startswith(">"):
                buf.append(lines[i].strip().lstrip("> ").strip())
                i += 1
            push(Paragraph(inliner(" ".join(buf)), styles["quote"]), " ".join(buf)[:120])
            continue

        para = [stripped]                                   # 普通段落
        i += 1
        while i < n:
            nxt = lines[i].strip()
            if (not nxt or nxt.startswith(("#", "|", "```", "$$", ">", "!["))
                    or re.match(r"^([-*+]|\d+\.)\s", nxt)
                    or re.fullmatch(r"(-{3,}|\*{3,}|_{3,})", nxt)):
                break
            para.append(nxt)
            i += 1
        push(Paragraph(inliner(" ".join(para)), styles["body"]), " ".join(para)[:160])

    # ---------------------------------------------------------------- 页脚
    if os.environ.get("MD2PDF_DEBUG"):
        from reportlab.pdfgen import canvas as _canvas
        c = _canvas.Canvas("/dev/null", pagesize=A4)
        for idx, (flow, src) in enumerate(zip(story, sources)):
            try:
                flow.wrapOn(c, avail, 10000)
            except Exception as exc:  # noqa: BLE001
                print(f"[DEBUG] flowable {idx} ({type(flow).__name__}) FAILED: {exc}")
                print("        source:", src[:160].replace("\n", " | "))
        c.save()

    def footer(canvas, doc):
        canvas.saveState()
        canvas.setFont("Song", 8)
        canvas.setFillColor(colors.HexColor("#777777"))
        canvas.drawCentredString(A4[0] / 2, 1.15 * cm, f"{doc.page}")
        if title:
            canvas.drawString(2.2 * cm, 1.15 * cm, title)
        canvas.restoreState()

    doc = BaseDocTemplate(pdf_path, pagesize=A4,
                          leftMargin=2.2 * cm, rightMargin=2.2 * cm,
                          topMargin=1.9 * cm, bottomMargin=1.9 * cm,
                          title=title or os.path.basename(pdf_path),
                          author="solutions")
    frame = Frame(doc.leftMargin, doc.bottomMargin, doc.width, doc.height, id="main")
    doc.addPageTemplates([PageTemplate(id="all", frames=[frame], onPage=footer)])
    doc.build(story)
    print(f"[md2pdf] {md_path} → {pdf_path}")
    if BAD_MATH:
        print(f"[md2pdf] !! {len(BAD_MATH)} 个公式无法用 mathtext 渲染，已按原文输出：")
        for t in BAD_MATH[:10]:
            print("        ", t[:110])


def main() -> None:
    ap = argparse.ArgumentParser()
    ap.add_argument("md")
    ap.add_argument("pdf")
    ap.add_argument("--title", default=None)
    ap.add_argument("--subtitle", default=None)
    ap.add_argument("--fontsize", type=float, default=10.5)
    ap.add_argument("--song", default=DEFAULT_SONG)
    ap.add_argument("--hei", default=DEFAULT_HEI)
    a = ap.parse_args()
    convert(a.md, a.pdf, a.title, a.subtitle, a.fontsize, a.song, a.hei)


if __name__ == "__main__":
    main()
