"""
Problem 7 (10 pts)
tip.csv：Y = tip，A = sex，B = smoker，C = time（注意题目未使用 day 变量）。
7 个候选模型（含交互项）；要求
  1. 统计 A,B,C 的类别数、样本数，并计算每个模型的 RSS；
  2. 分别用 R²_adj、AIC、BIC 选出最佳模型。

要点
  * 参数个数 k：M1:1, M2:4, M3:5, M4:5, M5:5, M6:7, M7:8（M7 = 2×2×2 单元均值模型）
  * AIC = n·log(RSS/n) + 2k，BIC = n·log(RSS/n) + k·log n（与 R 的 AIC()/BIC() 只差常数）
  * 额外给出 M7 的 8 个单元均值、以及嵌套模型的 F 检验，帮助解释结论

运行： python3 p7_tips.py
"""
from __future__ import annotations

import os
import sys

import matplotlib
matplotlib.use("Agg")
import matplotlib.pyplot as plt
import numpy as np
import pandas as pd
import statsmodels.api as sm
import statsmodels.formula.api as smf
from scipy import stats

sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
from common import load, md_table, save_json, save_table, save_text, savefig  # noqa: E402

plt.rcParams.update({"figure.dpi": 110, "font.size": 9, "axes.grid": True, "grid.alpha": 0.3})

df = load("tip")
n = len(df)
print("n =", n)
for c in ["sex", "smoker", "time", "day"]:
    print(c, dict(df[c].value_counts()))

# ============================================================ 1. 七个模型
formulas = {
    "M1: Y ~ 1": "tip ~ 1",
    "M2: Y ~ A+B+C": "tip ~ sex + smoker + time",
    "M3: Y ~ A+B+C+A:B": "tip ~ sex + smoker + time + sex:smoker",
    "M4: Y ~ A+B+C+B:C": "tip ~ sex + smoker + time + smoker:time",
    "M5: Y ~ A+B+C+A:C": "tip ~ sex + smoker + time + sex:time",
    "M6: Y ~ A+B+C+A:B+B:C+A:C": "tip ~ sex + smoker + time + sex:smoker + smoker:time + sex:time",
    "M7: Y ~ A+B+C+A:B+B:C+A:C+A:B:C": "tip ~ (sex + smoker + time) ** 3",
}
rows = []
fits = {}
for name, f in formulas.items():
    m = smf.ols(f, data=df).fit()
    fits[name] = m
    k = int(m.df_model + 1)
    rss = float(m.ssr)
    r2 = float(m.rsquared)
    r2adj = float(m.rsquared_adj)
    rows.append(dict(model=name, k=k, RSS=rss, R2=r2, R2adj=r2adj,
                     AIC=n * np.log(rss / n) + 2 * k,
                     BIC=n * np.log(rss / n) + k * np.log(n)))
res = pd.DataFrame(rows)
res["dAIC"] = res.AIC - res.AIC.min()
res["dBIC"] = res.BIC - res.BIC.min()
save_table(res.round(6), "p7_model_comparison")
print(res.round(3).to_string(index=False))

best = dict(r2adj=res.loc[res.R2adj.idxmax(), "model"],
            AIC=res.loc[res.AIC.idxmin(), "model"],
            BIC=res.loc[res.BIC.idxmin(), "model"])
print("最佳模型:", best)

# M7 与分组均值一致性检验（RSS 应等于组内平方和）
cell = df.groupby(["sex", "smoker", "time"], observed=True)["tip"]
rss_within = float(((df["tip"] - cell.transform("mean")) ** 2).sum())
cell_means = cell.agg(["mean", "count"]).round(4).reset_index()
save_table(cell_means, "p7_cell_means")
check = dict(rss_M7=float(fits["M7: Y ~ A+B+C+A:B+B:C+A:C+A:B:C"].ssr),
             rss_within_cells=rss_within,
             diff=abs(float(fits["M7: Y ~ A+B+C+A:B+B:C+A:C+A:B:C"].ssr) - rss_within))
print("M7 RSS == 组内平方和 ?", check)
save_json(dict(best=best, check=check,
               levels={c: int(df[c].nunique()) for c in ["sex", "smoker", "time"]},
               n=n), "p7_summary")

# 嵌套 F 检验（说明交互项是否真的有用）
def f_test(m_small, m_big):
    rss_s, rss_b = float(m_small.ssr), float(m_big.ssr)
    df1 = int(m_big.df_model - m_small.df_model)
    df2 = int(n - m_big.df_model - 1)
    F = ((rss_s - rss_b) / df1) / (rss_b / df2)
    return F, df1, df2, float(stats.f.sf(F, df1, df2))


ftab = []
for a, b in [("M1: Y ~ 1", "M2: Y ~ A+B+C"), ("M2: Y ~ A+B+C", "M3: Y ~ A+B+C+A:B"),
             ("M2: Y ~ A+B+C", "M6: Y ~ A+B+C+A:B+B:C+A:C"),
             ("M6: Y ~ A+B+C+A:B+B:C+A:C", "M7: Y ~ A+B+C+A:B+B:C+A:C+A:B:C")]:
    F, d1, d2, p = f_test(fits[a], fits[b])
    ftab.append(dict(small=a, big=b, F=F, df1=d1, df2=d2, p_value=p))
save_table(pd.DataFrame(ftab).round(6), "p7_nested_F_tests")

# ============================================================ 图
fig, axes = plt.subplots(1, 3, figsize=(10.6, 3.4))
axes[0].bar(range(len(res)), res.RSS, color="#4c72b0")
axes[0].set_xticks(range(len(res)), [f"M{i+1}" for i in range(len(res))])
axes[0].set(title="RSS of the 7 models", ylabel="RSS")
axes[1].bar(range(len(res)), res.AIC, color="#dd8452")
axes[1].set_xticks(range(len(res)), [f"M{i+1}" for i in range(len(res))])
axes[1].set(title="AIC", ylabel="AIC")
axes[2].bar(range(len(res)), res.BIC, color="#55a868")
axes[2].set_xticks(range(len(res)), [f"M{i+1}" for i in range(len(res))])
axes[2].set(title="BIC", ylabel="BIC")
fig.tight_layout()
savefig(fig, "p7_criteria")

fig, axes = plt.subplots(1, 3, figsize=(10.6, 3.2))
for ax, g, t in zip(axes, ["sex", "smoker", "time"], ["sex", "smoker", "time"]):
    for key, sub in df.groupby(g, observed=True):
        ax.hist(sub["tip"], bins=15, alpha=0.5, label=str(key))
    ax.set(title=f"tip by {t}", xlabel="tip", ylabel="count")
    ax.legend(fontsize=8)
fig.tight_layout()
savefig(fig, "p7_histograms")

summary = ["# Problem 7 结果摘要\n",
           f"样本量 n = {n}；A = sex（{df.sex.nunique()} 类），B = smoker（{df.smoker.nunique()} 类），"
           f"C = time（{df.time.nunique()} 类）。\n",
           md_table(res.round(6), "七个模型的 RSS / R²adj / AIC / BIC"),
           "\n最佳模型：R²adj → " + best["r2adj"] + "；AIC → " + best["AIC"] + "；BIC → " + best["BIC"] + "\n",
           "\n## 嵌套 F 检验\n", md_table(pd.DataFrame(ftab).round(6)),
           "\n## 8 个单元的 tip 均值\n", md_table(cell_means),
           f"\n一致性检验：M7 的 RSS = {check['rss_M7']:.4f}，组内平方和 = {check['rss_within_cells']:.4f}"
           f"（差 {check['diff']:.2e}）——M7 即 2×2×2 单元均值模型。\n"]
save_text("\n".join(summary), "p7_summary.md")
print("=== Problem 7 完成 ===")
