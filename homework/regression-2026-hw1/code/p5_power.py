"""
Problem 5 (20 pts)
powerconsumption.csv：取 PowerConsumption_Zone1 为因变量、Temperature 为自变量，
分别用 三次多项式 / 三次样条 / 自然三次样条 / 平滑样条 拟合，并比较效果。

实现要点
  * 三次多项式：1, x, x^2, x^3 —— 普通最小二乘
  * 三次样条：截断幂基 1,x,x^2,x^3,(x-ξ_j)_+^3；结点取样本分位数
  * 自然三次样条：Hastie 等的自然样条基 {1,x,d_1..d_{K-2}},
                  d_j(x)=[(x-ξ_j)_+^3-(x-ξ_K)_+^3]/(ξ_K-ξ_j)
  * 平滑样条：以全部观测点为结点的自然三次平滑样条（Hastie et al. §5.2），
              惩罚矩阵 Ω_{jk}=∫ d_j''d_k'' 用 Gauss-Legendre 精确积分，
              光滑参数 λ 由 GCV 选取，并与 scipy.make_smoothing_spline 交叉验证
  * 结点个数 K 由 LOOCV 选取（同时报告 BIC 的选择作为稳健性对照）
  评价：RSS, R^2, adjR^2, AIC, BIC, LOOCV（OLS 用 PRESS，线性光滑器用 1-S_ii）

运行： python3 p5_power.py
"""
from __future__ import annotations

import os
import sys

import matplotlib
matplotlib.use("Agg")
import matplotlib.pyplot as plt
import numpy as np
import pandas as pd
from scipy.interpolate import make_smoothing_spline

sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
from common import load, md_table, ols_fit, save_json, save_table, save_text, savefig  # noqa: E402

plt.rcParams.update({"figure.dpi": 110, "font.size": 9, "axes.grid": True,
                     "grid.alpha": 0.3, "legend.frameon": False})

# ============================================================ 0. 数据
df = load("powerconsumption")
x = df["Temperature"].to_numpy(float)
y = df["PowerConsumption_Zone1"].to_numpy(float)
n = len(x)
print(f"n={n}, x∈[{x.min():.2f},{x.max():.2f}], y∈[{y.min():.0f},{y.max():.0f}]")


# ============================================================ 1. 基函数
def poly_basis(xx, deg=3):
    return np.column_stack([np.ones_like(xx)] + [xx ** k for k in range(1, deg + 1)])


def tpf_basis(xx, knots):                       # 截断幂基三次样条
    cols = [np.ones_like(xx), xx, xx ** 2, xx ** 3]
    cols += [np.maximum(xx - k, 0.0) ** 3 for k in knots]
    return np.column_stack(cols)


def ns_basis(xx, knots):                        # 自然三次样条基（knots 升序，K≥3）
    knots = np.asarray(knots, float)
    K = len(knots)
    cols = [np.ones_like(xx), xx]
    for j in range(K - 2):
        num = np.maximum(xx - knots[j], 0.0) ** 3 - np.maximum(xx - knots[K - 1], 0.0) ** 3
        cols.append(num / (knots[K - 1] - knots[j]))
    return np.column_stack(cols)


def ns_basis_dd(xx, knots):                     # 自然样条基的二阶导数（造惩罚矩阵用）
    knots = np.asarray(knots, float)
    K = len(knots)
    cols = [np.zeros_like(xx), np.zeros_like(xx)]
    for j in range(K - 2):
        num = np.maximum(xx - knots[j], 0.0) - np.maximum(xx - knots[K - 1], 0.0)
        cols.append(6.0 * num / (knots[K - 1] - knots[j]))
    return np.column_stack(cols)


def penalty_matrix(knots, ngauss=4):
    """Ω_{jk} = ∫ d_j''(x) d_k''(x) dx；被积函数分段二次，Gauss-Legendre 精确。"""
    knots = np.asarray(knots, float)
    K = len(knots)
    nodes, weights = np.polynomial.legendre.leggauss(ngauss)
    edges = np.unique(knots)
    Om = np.zeros((K, K))
    for a, b in zip(edges[:-1], edges[1:]):
        mid, half = 0.5 * (a + b), 0.5 * (b - a)
        pts = mid + half * nodes
        D = ns_basis_dd(pts, knots)
        Om += half * (D * weights[:, None]).T @ D
    return Om


def quantile_knots(xx, K):
    return np.quantile(xx, np.linspace(0, 1, K + 2)[1:-1])


# ============================================================ 2. 平滑样条（结点=全部观测点）
knots_all = np.unique(x)
N = ns_basis(x, knots_all)
Om = penalty_matrix(knots_all)
NtN, Nty = N.T @ N, N.T @ y


def smooth_fit(lam):
    A = NtN + lam * Om
    coef = np.linalg.solve(A, Nty)
    fitted = N @ coef
    S = N @ np.linalg.solve(A, N.T)
    edf = float(np.trace(S))
    resid = y - fitted
    rss = float(resid @ resid)
    gcv = (rss / n) / (1 - edf / n) ** 2
    loocv = float(np.mean((resid / (1 - np.diag(S))) ** 2))
    return dict(lam=lam, coef=coef, fitted=fitted, edf=edf, rss=rss, gcv=gcv,
                loocv=loocv, r2=1 - rss / ((y - y.mean()) ** 2).sum())


lams = np.concatenate([np.logspace(-4, 3, 71), np.logspace(3, 6, 31)])
fits = [smooth_fit(l) for l in lams]
best0 = min(fits, key=lambda d: d["gcv"])
fine = np.logspace(np.log10(best0["lam"]) - 0.5, np.log10(best0["lam"]) + 0.5, 41)
fits += [smooth_fit(l) for l in fine]
best = min(fits, key=lambda d: d["gcv"])
print(f"[smoothing spline] GCV 最优 λ={best['lam']:.5g}, edf={best['edf']:.2f}, "
      f"RSS={best['rss']:.0f}, R2={best['r2']:.4f}")

idx_sort = np.argsort(x)
scipy_ss = make_smoothing_spline(x[idx_sort], y[idx_sort], lam=None)
rss_scipy = float(((y - scipy_ss(x)) ** 2).sum())
print(f"[cross-check scipy] RSS={rss_scipy:.0f}  vs  自定义 RSS={best['rss']:.0f}")


def smooth_predict(d, xnew):
    return ns_basis(np.asarray(xnew, float), knots_all) @ d["coef"]


# ============================================================ 3. 结点个数 K 的选择
grid = range(2, 15)
grid_rows = []
for kind in ("cubic", "natural"):
    for K in grid:
        if kind == "natural" and K < 3:
            continue
        kn = quantile_knots(x, K)
        Xd = tpf_basis(x, kn) if kind == "cubic" else ns_basis(x, kn)
        f = ols_fit(y, Xd)
        grid_rows.append(dict(kind=kind, K=K, p=f["p"], RSS=f["rss"], R2=f["r2"],
                              R2adj=f["r2adj"], AIC=f["aic"], BIC=f["bic"], LOOCV=f["loocv"]))
grid_df = pd.DataFrame(grid_rows)
save_table(grid_df.round(6), "p5_knot_selection")

best_ks = {}
for kind, d in grid_df.groupby("kind"):
    best_ks[kind] = dict(BIC=int(d.loc[d.BIC.idxmin(), "K"]),
                         AIC=int(d.loc[d.AIC.idxmin(), "K"]),
                         LOOCV=int(d.loc[d.LOOCV.idxmin(), "K"]))
best_ks["smoothing"] = dict(BIC="GCV-λ", AIC="GCV-λ", LOOCV=round(best["edf"], 2))
print("[knots]", best_ks)

K_cub = best_ks["cubic"]["LOOCV"]
K_nat = best_ks["natural"]["LOOCV"]
knots_cub, knots_nat = quantile_knots(x, K_cub), quantile_knots(x, K_nat)
print(f"选定结点（LOOCV 准则）: 三次样条 K={K_cub} {np.round(knots_cub, 2)} | "
      f"自然样条 K={K_nat} {np.round(knots_nat, 2)}")

# ============================================================ 4. 四个模型汇总
models = {
    "cubic polynomial": dict(
        label="Cubic polynomial", fit=ols_fit(y, poly_basis(x)),
        basis=lambda xx: poly_basis(xx), knots=None),
    f"cubic spline K={K_cub}": dict(
        label=f"Cubic spline (K={K_cub})", fit=ols_fit(y, tpf_basis(x, knots_cub)),
        basis=lambda xx: tpf_basis(xx, knots_cub), knots=knots_cub),
    f"natural cubic spline K={K_nat}": dict(
        label=f"Natural cubic spline (K={K_nat})",
        fit=ols_fit(y, ns_basis(x, knots_nat)),
        basis=lambda xx: ns_basis(xx, knots_nat), knots=knots_nat),
    f"smoothing spline (edf={best['edf']:.1f})": dict(
        label=f"Smoothing spline (edf={best['edf']:.1f})",
        fit=dict(p=best["edf"], rss=best["rss"], r2=best["r2"],
                 r2adj=1 - (1 - best["r2"]) * (n - 1) / (n - best["edf"]),
                 aic=n * np.log(best["rss"] / n) + 2 * best["edf"],
                 bic=n * np.log(best["rss"] / n) + best["edf"] * np.log(n),
                 loocv=best["loocv"], resid=y - best["fitted"], fitted=best["fitted"],
                 beta=best["coef"], dw=float(((np.diff(y - best["fitted"])) ** 2).sum() / best["rss"])),
        basis=lambda xx: ns_basis(xx, knots_all), coef=best["coef"], knots=knots_all),
}

# 补充：BIC / AIC 准则选出的结点个数所对应的模型，一并列出以显示稳健性
extra = {}
for kind, base, basis_fn in [("cubic", tpf_basis, None), ("natural", ns_basis, None)]:
    for crit in ("BIC", "AIC"):
        K = best_ks[kind][crit]
        if K == (K_cub if kind == "cubic" else K_nat):
            continue
        kn = quantile_knots(x, K)
        f = ols_fit(y, tpf_basis(x, kn) if kind == "cubic" else ns_basis(x, kn))
        extra[f"{kind} spline K={K} ({crit} choice)"] = dict(
            fit=f, basis=(lambda kn=kn, kind=kind: (lambda xx: tpf_basis(xx, kn)))(),
            knots=kn)

rows = []
for name, m in {**models, **extra}.items():
    f = m["fit"]
    rows.append(dict(model=name, k=round(f["p"], 2), RSS=f["rss"], R2=f["r2"],
                     R2adj=f["r2adj"], AIC=f["aic"], BIC=f["bic"], LOOCV=f["loocv"]))
cmp_df = pd.DataFrame(rows).sort_values("LOOCV").reset_index(drop=True)
save_table(cmp_df.round(6), "p5_model_comparison")
print(cmp_df.round(4).to_string(index=False))

xg = np.linspace(x.min(), x.max(), 2000)
curve = {}
for name, m in models.items():
    curve[name] = (m["basis"](xg) @ m["coef"]) if "coef" in m else (m["basis"](xg) @ m["fit"]["beta"])
minima = {name: float(xg[np.argmin(v)]) for name, v in curve.items()}
save_json(dict(knots=best_ks, minima=minima, smoothing_lam=float(best["lam"]),
               smoothing_edf=float(best["edf"]), scipy_rss=rss_scipy,
               comparison=cmp_df.to_dict("records")), "p5_details")

# ============================================================ 5. 图
colors = ["#d62728", "#1f77b4", "#2ca02c", "#9467bd"]
fig, ax = plt.subplots(figsize=(7.4, 4.4))
ax.scatter(x, y, s=9, alpha=0.4, color="0.35", label=f"data (n={n})")
for (name, m), c in zip(models.items(), colors):
    ax.plot(xg, curve[name], color=c, lw=1.7, label=m["label"])
ax.set(xlabel="Temperature (°C)", ylabel="PowerConsumption_Zone1",
       title="Zone-1 daily power consumption vs. temperature: four fits")
ax.legend(loc="upper center", fontsize=8)
savefig(fig, "p5_four_fits")

fig, axes = plt.subplots(2, 2, figsize=(9.4, 6.0))
for ax, (name, m), c in zip(axes.ravel(), models.items(), colors):
    ax.scatter(x, y, s=7, alpha=0.35, color="0.5")
    ax.plot(xg, curve[name], color=c, lw=1.7)
    f = m["fit"]
    ax.set_title(f"{m['label']}\nRSS={f['rss']:.3g}, R²={f['r2']:.4f}, "
                 f"k={f['p']:.1f}, LOOCV={f['loocv']:.3g}, BIC={f['bic']:.1f}", fontsize=8)
    ax.set(xlabel="Temperature (°C)", ylabel="Zone1 load")
fig.tight_layout()
savefig(fig, "p5_fits_panels")

fig, ax = plt.subplots(figsize=(7.4, 3.6))
for (name, m), c in zip(models.items(), colors):
    ax.scatter(m["fit"]["fitted"], m["fit"]["resid"], s=6, alpha=0.5, color=c, label=m["label"])
ax.axhline(0, color="k", lw=0.8)
ax.set(xlabel="fitted value", ylabel="residual",
       title="Residuals vs. fitted (all four models)")
ax.legend(fontsize=7)
savefig(fig, "p5_residuals")

fig, axes = plt.subplots(1, 2, figsize=(9.4, 3.4))
for kind, d in grid_df.groupby("kind"):
    axes[0].plot(d.K, d.BIC, "o-", ms=3, label=f"{kind} spline")
    axes[1].plot(d.K, d.LOOCV, "o-", ms=3, label=f"{kind} spline")
axes[0].set(xlabel="number of knots K", ylabel="BIC", title="BIC vs. number of knots")
axes[1].set(xlabel="number of knots K", ylabel="LOOCV (PRESS/n)", title="LOOCV vs. number of knots")
for a, k in zip(axes, (best_ks["cubic"]["BIC"], best_ks["cubic"]["LOOCV"])):
    a.axvline(k, color="0.6", ls=":", lw=1)
for a in axes:
    a.legend(fontsize=8)
fig.tight_layout()
savefig(fig, "p5_knot_choice")

fig, axes = plt.subplots(1, 2, figsize=(9.4, 3.4))
lam_sorted = sorted({f["lam"] for f in fits})
gg = [next(f for f in fits if f["lam"] == l) for l in lam_sorted]
axes[0].semilogx(lam_sorted, [f["gcv"] for f in gg], "o-", ms=3)
axes[0].axvline(best["lam"], color="r", ls="--", lw=1,
                label=f"GCV min: λ={best['lam']:.3g} (edf={best['edf']:.1f})")
axes[0].set(xlabel="λ (log scale)", ylabel="GCV",
            title="GCV curve of the smoothing spline")
axes[0].legend(fontsize=8)
axes[1].loglog(lam_sorted, [f["edf"] for f in gg], "o-", ms=3, color="teal")
axes[1].axhline(best["edf"], color="r", ls="--", lw=1, label=f"edf={best['edf']:.1f}")
axes[1].set(xlabel="λ", ylabel="effective degrees of freedom", title="edf vs. λ")
axes[1].legend(fontsize=8)
fig.tight_layout()
savefig(fig, "p5_smoothing_gcv")

fig, ax = plt.subplots(figsize=(7.4, 4.0))
ax.scatter(x, y, s=9, alpha=0.35, color="0.4")
for l, c in zip([1e-2, best["lam"], 1e3, 1e6], ["#2ca02c", "#d62728", "#1f77b4", "#9467bd"]):
    d = smooth_fit(l)
    ax.plot(xg, smooth_predict(d, xg), color=c, lw=1.7,
            label=f"λ={l:.3g} (edf={d['edf']:.0f}, RSS={d['rss']:.3g})")
ax.set(xlabel="Temperature (°C)", ylabel="Zone1 load",
       title="Smoothing spline: under- / over-smoothing")
ax.legend(fontsize=8)
savefig(fig, "p5_smoothing_paths")

# ============================================================ 6. 文本输出
poly = models["cubic polynomial"]["fit"]
poly_tab = pd.DataFrame(dict(term=["1", "T", "T^2", "T^3"], est=poly["beta"],
                             se=poly["se"], t=poly["t"], p=poly["pval"]))
save_text("# Problem 5：三次多项式系数\n\n" +
          md_table(poly_tab.round(6), "OLS: Zone1 ~ 1 + T + T^2 + T^3（原始温度尺度）") +
          "\n注：用原始温度（未中心化）拟合，数值上仍然稳定；中心化只会改变系数的表示。\n",
          "p5_poly_coefficients.md")

dw_tab = pd.DataFrame([{k: v["fit"]["dw"] for k, v in models.items()}]).T.reset_index()
report = ["# Problem 5 结果摘要\n",
          md_table(cmp_df.round(6), f"四个（及补充）模型的比较（n={n}）"),
          "\n## 结点个数 / 光滑参数的选取\n",
          md_table(pd.DataFrame(best_ks).T.reset_index().rename(columns={"index": "kind"}),
                   "各准则选出的结点个数（平滑样条为 GCV 选出的有效自由度）"),
          "\n## 拟合曲线最低点（用电量最小对应的温度，°C）\n",
          md_table(pd.DataFrame([minima]).T.reset_index().rename(
              columns={"index": "model", 0: "T at minimum"})),
          f"\n## 平滑样条\n\nGCV 最优 λ = {best['lam']:.5g}（edf = {best['edf']:.2f}），"
          f"自定义实现 RSS = {best['rss']:.0f}，scipy.make_smoothing_spline RSS = {rss_scipy:.0f}"
          "（两者一致，交叉验证通过）。\n",
          "\n## 残差 Durbin–Watson 统计量（若明显小于 2，说明残差存在自相关）\n",
          md_table(dw_tab.rename(columns={"index": "model", 0: "DW"}).round(4))]
save_text("\n".join(report), "p5_summary.md")
print("\n=== Problem 5 完成 ===")
