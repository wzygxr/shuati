# 作业解答（2026-09）

本目录存放仓库中两份作业的完整解答：

| 作业 | 题面 | 解答（PDF） | 解答（可编辑源文件） |
| --- | --- | --- | --- |
| 《回归模型与方差分析》Assignment 1（8 题 100 分） | [`../hw1-2026.pdf`](../hw1-2026.pdf) | [`hw1-2026-solution.pdf`](hw1-2026-solution.pdf) | [`regression-2026-hw1/solution.md`](regression-2026-hw1/solution.md) |
| Quasilinear elliptic equations of second order · Homework 1 | [`../Homework1.pdf`](../Homework1.pdf) | [`Homework1-solution.pdf`](Homework1-solution.pdf) | [`quasilinear-hw1/solution.md`](quasilinear-hw1/solution.md) |

## 目录结构

```
homework/
├── hw1-2026-solution.pdf          # 回归作业解答（16 页，含推导+数据结果+图）
├── Homework1-solution.pdf         # 偏微分方程作业解答（6 页：反例 + 对数型定理）
├── regression-2026-hw1/           # 回归作业的可复现工程
│   ├── solution.md                #   解答正文（Markdown+LaTeX，PDF 即由此生成）
│   ├── code/                      #   6 个脚本 + run_all.sh（一键复现全部结果）
│   ├── data/                      #   题面数据（来自 hw1-2023-data.zip）
│   ├── results/                   #   全部数值结果（csv / md / json）
│   └── figures/                   #   全部插图（12 张 png）
├── quasilinear-hw1/
│   └── solution.md                # 偏微分方程作业解答正文
└── tools/                         # Markdown → PDF 工具（见下）
```

## 复现方式

回归作业（Python 3.11 + numpy/pandas/scipy/statsmodels/scikit-learn/matplotlib）：

```bash
cd regression-2026-hw1/code
bash run_all.sh            # 约 30 秒；结果写入 ../results，图写入 ../figures
```

`run_all.sh` 依次运行：

| 脚本 | 内容 |
| --- | --- |
| `p1_p4_verify.py` | 题 1 的恒等式校核、题 2 的 Monte Carlo（$10^5$ 次）、题 3 的漏变量偏差、题 4 的方差膨胀 |
| `p5_power.py` | 题 5：三次多项式/三次样条/自然三次样条/平滑样条，结点数与 GCV 选择 |
| `p6_argentina.py` | 题 6：标准化 PCA、前三个主成分回归、系数回换算与稳健性对照 |
| `p7_tips.py` | 题 7：7 个候选模型的 RSS / $R^2_{adj}$ / AIC / BIC 与嵌套 F 检验 |
| `p8_student.py` | 题 8：FS / BE / FS+BE（AIC、BIC、p 值三种准则）+ $2^5$ 全子集穷举 |

## 关于 PDF 的生成

沙箱里没有 TeX 发行版（apt 源不可达），因此解答用自带的
[`tools/md2pdf.py`](tools/md2pdf.py) 从 Markdown 生成：正文用 reportlab 排版，
行间/行内数学公式用 matplotlib 的 mathtext 渲染成矢量图片嵌入（`$...$` 与 `$$...$$` 语法）。

```bash
python3 tools/md2pdf.py regression-2026-hw1/solution.md out.pdf \
        --title "《回归模型与方差分析》Assignment 1 解答"
```

注意 mathtext 支持的宏是 LaTeX 的子集：`\le/\ge` 要写 `\leq/\geq`，
不支持 `\begin{...}`、`\Big`、`\displaystyle`、`\overbrace` 等（遇到无法解析的公式脚本会
退回成等宽原文并在日志里提示）。字体使用 `/home/user/fonts/SimHei.ttf`
（由 `pip install mplfonts` 解包得到；中文字体缺少的个别符号如 `ö`、`²` 自动回退到 DejaVu Sans）。

## 主要结论速览

**回归作业**：题 1–4 的代数结论全部给出证明，并用 Monte Carlo 逐条校核；
题 5 中自然三次样条（$K=10$）与三次样条（$K=7$）最优，三次多项式最差，残差存在强自相关（DW≈0.7）；
题 6 前三个主成分解释 72.5% 的方差、GDP 回归 $R^2=0.6908$；
题 7 中 $R^2_{adj}$ 选 $Y\sim A+B+C$，AIC/BIC 选只有截距的模型，F 检验全部不显著；
题 8 三种方法在 AIC/BIC 下都保留全部 5 个自变量，与全子集穷举一致。

**偏微分方程作业**：把边界条件换成对数模量后，**原来的幂型结论不成立**
（反例：半球上 $\Delta u=0$、$\varphi=|\ln|x||^{-\alpha}$，解满足 $u(0,x_n)\asymp|\ln x_n|^{-\alpha}$，
任何正幂次估计都失效）；**正确的"类似结论"是对数型**
$|u(x)-u(x_0)|\leq C(\sup|u|+\Phi_\alpha+\sup|f|)\,|\ln|x-x_0||^{-\alpha/(1+\alpha)}$，
模型情形下可证更强的 $|\ln|x-x_0||^{-\alpha}$；定性结论（$u$ 在 $x_0$ 连续）仍然成立。
