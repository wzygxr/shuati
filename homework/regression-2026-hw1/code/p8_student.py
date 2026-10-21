"""
Problem 8 (15 pts)
Student_Performance.csv：Y = Performance Index，其余变量为自变量
（Extracurricular Activities 处理成虚拟变量 0/1）。
用 FS（前向）、BE（后向）、FS+BE（逐步）三种方法选模型；选择准则自选 —— 本解采用 AIC
（同时给出 BIC 与 p 值准则的稳健性对照，并做穷举子集搜索验证是否达到全局最优）。

运行： python3 p8_student.py
"""
from __future__ import annotations

import itertools
import os
import sys

import matplotlib
matplotlib.use("Agg")
import matplotlib.pyplot as plt
import numpy as np
import pandas as pd
import statsmodels.api as sm

sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
from common import load, md_table, ols_fit, save_json, save_table, save_text, savefig  # noqa: E402

plt.rcParams.update({"figure.dpi": 110, "font.size": 9, "axes.grid": True, "grid.alpha": 0.3})

df = load("Student_Performance").rename(columns={
    "Hours Studied": "HS", "Previous Scores": "PS",
    "Extracurricular Activities": "EA", "Sleep Hours": "SH",
    "Sample Question Papers Practiced": "SQ", "Performance Index": "Y"})
df["EA"] = (df.EA == "Yes").astype(int)                    # 虚拟变量
y = df.Y.to_numpy(float)
n = len(df)
cand = ["HS", "PS", "EA", "SH", "SQ"]
print("n =", n, " candidates:", cand)

Xall = sm.add_constant(df[cand].to_numpy(float))
full = sm.OLS(y, Xall).fit()


# ------------------------------------------------------------- 工具
def fit_terms(terms):
    X = sm.add_constant(df[list(terms)].to_numpy(float)) if terms else np.ones((n, 1))
    m = sm.OLS(y, X).fit()
    k = 1 + len(terms)
    rss = float(m.ssr)
    return dict(terms=tuple(terms), k=k, rss=rss, aic=n * np.log(rss / n) + 2 * k,
                bic=n * np.log(rss / n) + k * np.log(n), r2=float(m.rsquared),
                r2adj=float(m.rsquared_adj), pvals=m.pvalues[1:] if terms else np.array([]),
                model=m)


def forward(criterion="aic", tol=1e-9):
    chosen, cur, path = [], fit_terms([]), []
    improved = True
    while improved and len(chosen) < len(cand):
        improved = False
        best_add, best_obj = None, cur[criterion]
        for t in [c for c in cand if c not in chosen]:
            f = fit_terms(chosen + [t])
            if f[criterion] < best_obj - tol:
                best_add, best_obj = t, f[criterion]
        if best_add:
            chosen.append(best_add)
            cur = fit_terms(chosen)
            path.append(("add", best_add, round(cur[criterion], 4)))
            improved = True
    return chosen, cur, path


def backward(criterion="aic", tol=1e-9):
    chosen = list(cand)
    cur = fit_terms(chosen)
    path = []
    improved = True
    while improved and len(chosen) > 0:
        improved = False
        best_drop, best_obj = None, cur[criterion]
        for t in chosen:
            f = fit_terms([c for c in chosen if c != t])
            if f[criterion] < best_obj - tol:
                best_drop, best_obj = t, f[criterion]
        if best_drop:
            chosen.remove(best_drop)
            cur = fit_terms(chosen)
            path.append(("drop", best_drop, round(cur[criterion], 4)))
            improved = True
    return chosen, cur, path


def stepwise(criterion="aic", tol=1e-9):
    chosen, cur, path = [], fit_terms([]), []
    improved = True
    while improved:
        improved = False
        # 前向一步
        best_add = None
        for t in [c for c in cand if c not in chosen]:
            f = fit_terms(chosen + [t])
            if f[criterion] < cur[criterion] - tol:
                if best_add is None or f[criterion] < fit_terms(chosen + [best_add])[criterion]:
                    best_add = t
        if best_add is not None:
            chosen.append(best_add)
            cur = fit_terms(chosen)
            path.append(("add", best_add, round(cur[criterion], 4)))
            improved = True
        # 后向一步
        best_drop = None
        for t in chosen:
            f = fit_terms([c for c in chosen if c != t])
            if f[criterion] < cur[criterion] - tol:
                if best_drop is None or f[criterion] < fit_terms([c for c in chosen if c != best_drop])[criterion]:
                    best_drop = t
        if best_drop is not None:
            chosen.remove(best_drop)
            cur = fit_terms(chosen)
            path.append(("drop", best_drop, round(cur[criterion], 4)))
            improved = True
    return chosen, cur, path


# ------------------------------------------------------------- 穷举（真·全局最优，作为校验）
all_sub = []
for r in range(len(cand) + 1):
    for comb in itertools.combinations(cand, r):
        all_sub.append(fit_terms(list(comb)))
sub = pd.DataFrame([{k: v for k, v in d.items() if k not in ("model", "pvals", "terms")} |
                    {"terms": "+".join(d["terms"]) if d["terms"] else "(intercept only)"}
                    for d in all_sub])
best_subset = dict(AIC=sub.loc[sub.aic.idxmin(), "terms"], BIC=sub.loc[sub.bic.idxmin(), "terms"])
print("穷举最优:", best_subset)

# ------------------------------------------------------------- 三种方法（AIC 主准则 + BIC 对照）
out, paths = {}, {}
for crit in ("aic", "bic"):
    for meth, fn in [("FS(前向)", forward), ("BE(后向)", backward), ("FS+BE(逐步)", stepwise)]:
        terms, cur, path = fn(crit)
        out[(crit, meth)] = cur
        paths[(crit, meth)] = path
        print(f"[{crit.upper():>3}] {meth:<12} -> {terms}  {crit.upper()}={cur[crit]:.4f}  R2adj={cur['r2adj']:.4f}")

rows = []
for (crit, meth), cur in out.items():
    rows.append(dict(criterion=crit.upper(), method=meth,
                     selected=" + ".join(cur["terms"]) if cur["terms"] else "(none)",
                     k=cur["k"], RSS=cur["rss"], R2=cur["r2"], R2adj=cur["r2adj"],
                     AIC=cur["aic"], BIC=cur["bic"]))
tab = pd.DataFrame(rows)
save_table(tab.round(6), "p8_stepwise_results")

# p 值准则（α=0.05）对照：前向逐步
def forward_p(alpha=0.05):
    chosen = []
    while True:
        best, best_p = None, alpha
        for t in [c for c in cand if c not in chosen]:
            f = fit_terms(chosen + [t])
            pv = float(f["pvals"][-1])
            if pv < best_p:
                best, best_p = t, pv
        if best is None:
            break
        chosen.append(best)
    return chosen, fit_terms(chosen)


terms_p, cur_p = forward_p(0.05)
print("p 值准则（α=0.05）前向:", terms_p)

# ------------------------------------------------------------- 最终模型摘要
final_fit = out[("aic", "FS+BE(逐步)")]
final_terms = list(final_fit["terms"])
final_path = paths[("aic", "FS+BE(逐步)")]
Xf = sm.add_constant(df[list(final_terms)].to_numpy(float))
final = sm.OLS(y, Xf).fit()
mine = ols_fit(y, Xf)
coef_tab = pd.DataFrame(dict(term=["const"] + list(final_terms), coef=final.params,
                             se=final.bse, t=final.tvalues, p=final.pvalues,
                             ci_lo=final.conf_int()[:, 0], ci_hi=final.conf_int()[:, 1]))
save_text(md_table(coef_tab.round(6), "最终模型（AIC 逐步法）系数"),
          "p8_final_coefficients.md")
print(final.summary().as_text()[:1800])

save_json(dict(n=n, candidates=cand,
               full_model=dict(R2=float(full.rsquared), R2adj=float(full.rsquared_adj),
                               AIC=n * np.log(float(full.ssr) / n) + 2 * (len(cand) + 1)),
               results={f"{crit}-{meth}": list(out[(crit, meth)]["terms"]) for crit, meth in out},
               paths_AIC={meth: paths[("aic", meth)]
                          for meth in ("FS(前向)", "BE(后向)", "FS+BE(逐步)")},
               exhaustive_optimum=best_subset,
               p_value_forward=terms_p,
               final=dict(terms=final_terms, k=len(final_terms) + 1, rss=float(final.ssr),
                          R2=float(final.rsquared), R2adj=float(final.rsquared_adj),
                          AIC=final_fit["aic"], BIC=final_fit["bic"],
                          loocv=mine["loocv"], dw=mine["dw"])), "p8_summary")

# ------------------------------------------------------------- 单变量模型对照（各变量的边际贡献）
single = []
for c in cand:
    f = fit_terms([c])
    single.append(dict(variable=c, R2=round(f["r2"], 4), AIC=round(f["aic"], 2),
                       BIC=round(f["bic"], 2),
                       coef_final=round(float(final.params[1 + final_terms.index(c)]), 4)
                       if c in final_terms else None,
                       p_final=float(final.pvalues[1 + final_terms.index(c)]) if c in final_terms else None))
single_df = pd.DataFrame(single).sort_values("R2", ascending=False)
save_table(single_df.round(6), "p8_single_variable")
print(single_df.to_string(index=False))

# ------------------------------------------------------------- 图
fig, axes = plt.subplots(1, 3, figsize=(10.8, 3.4))
axes[0].bar(["full (5)", "+".join(final_terms)], [full.rsquared, final.rsquared],
            color=["#bbbbbb", "#4c72b0"])
axes[0].set(title="R²: full model vs. selected", ylabel="R²")
axes[0].tick_params(axis="x", labelsize=8)
axes[1].scatter(final.fittedvalues, final.resid, s=4, alpha=0.35, color="#55a868")
axes[1].axhline(0, color="k", lw=0.8)
axes[1].set(xlabel="fitted", ylabel="residual", title="Residuals vs fitted (final model)")
sm.qqplot(final.resid, line="45", ax=axes[2], markerfacecolor="#4c72b0",
          markeredgecolor="#4c72b0", markersize=3)
axes[2].set_title("Normal Q–Q (final model)")
fig.tight_layout()
savefig(fig, "p8_diagnostics")

# 每个候选变量的相关性/边际图
fig, axes = plt.subplots(1, 5, figsize=(12.6, 2.8))
for ax, c in zip(axes, cand):
    ax.scatter(df[c], df.Y, s=3, alpha=0.25, color="#4c72b0")
    ax.set(title=f"{c} vs Y", xlabel=c, ylabel="Y")
fig.tight_layout()
savefig(fig, "p8_marginal")

summary = ["# Problem 8 结果摘要\n",
           f"n = {n}，候选自变量：{cand}（EA 已转成 0/1 虚拟变量）。主准则：AIC；"
           "括号中给出 BIC / p 值准则的对照。\n",
           md_table(tab.round(6), "FS / BE / FS+BE 的选模结果（AIC 与 BIC 两种准则）"),
           f"\n- AIC 准则下三种方法都选中：**{' + '.join(final_terms)}**\n"
           f"- 穷举全子集搜索的最优（AIC）：{best_subset['AIC']}；（BIC）：{best_subset['BIC']}\n"
           f"- p 值准则（α=0.05，前向）：{' + '.join(terms_p) if terms_p else '(none)'}\n"
           f"- 全模型 R² = {full.rsquared:.4f}，R²adj = {full.rsquared_adj:.4f}；"
           f"最终模型 R² = {final.rsquared:.4f}，R²adj = {final.rsquared_adj:.4f}，"
           f"LOOCV = {mine['loocv']:.4f}\n",
           "\n## 选模路径（AIC）\n",
           "\n".join(f"- {meth}: " + " → ".join(f"{a} {t} ({v})" for a, t, v in paths[("aic", meth)])
                     for meth in ["FS(前向)", "BE(后向)", "FS+BE(逐步)"]),
           "\n## 最终模型系数\n", md_table(coef_tab.round(6))]
save_text("\n".join(summary), "p8_summary.md")
print("=== Problem 8 完成 ===")
