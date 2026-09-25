"""
Problem 6 (10 pts)
argentina.csv：用除 province 外的变量预测 gdp。
  1. 求自变量的前三个主成分，并给出它们解释的方差比例；
  2. 用前三个主成分作为新的自变量拟合线性模型。

要点
  * 各变量量纲差异极大（gdp ~1e8，movie_theatres_per_cap ~1e-6），做主成分前必须标准化；
    同时给出"不标准化"的结果作为对照，说明标准化的必要性。
  * PCA 用 sklearn（SVD），PC 得分正交 => 回归系数等于 y 对 PC 的简单投影，可直接检验。
  * 主分析按题目要求用 gdp；另外附 gdp 取对数（gdp 高度右偏）的稳健性对照。

运行： python3 p6_argentina.py
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
from sklearn.decomposition import PCA
from sklearn.preprocessing import StandardScaler

sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
from common import FIGURES_DIR, load, md_table, ols_fit, save_json, save_table, save_text, savefig  # noqa: E402

plt.rcParams.update({"figure.dpi": 110, "font.size": 9, "axes.grid": True, "grid.alpha": 0.3})

df = load("argentina")
feat = [c for c in df.columns if c not in ("province", "gdp")]
y = df["gdp"].to_numpy(float)
Xraw = df[feat].to_numpy(float)
print("n =", len(df), " predictors =", len(feat))

# ============================================================ 1. 标准化 + PCA
scaler = StandardScaler()
Xs = scaler.fit_transform(Xraw)
pca = PCA()
scores = pca.fit_transform(Xs)
evr = pca.explained_variance_ratio_
print("方差解释比例(标准化):", np.round(evr[:5], 4))

pca_raw = PCA()
pca_raw.fit(Xraw)
evr_raw = pca_raw.explained_variance_ratio_

# 独立验证：相关矩阵（X 已标准化，numpy.corrcoef 用 ddof=1）的特征值乘 n/(n-1)
# 应等于 sklearn（StandardScaler 的 sd 用 ddof=0）给出的特征值。
n_obs, p_feat = Xs.shape
corr = np.corrcoef(Xs, rowvar=False)
eig_np = np.sort(np.linalg.eigvalsh(corr))[::-1]
max_diff = float(np.max(np.abs(eig_np * n_obs / (n_obs - 1) - pca.explained_variance_)))
print(f"[check] |eigen(numpy)*n/(n-1) - eigen(sklearn)|max = {max_diff:.3e}; "
      f"sum(eigen)={pca.explained_variance_.sum():.6f} vs p*n/(n-1)={p_feat*n_obs/(n_obs-1):.6f}")

var_tab = pd.DataFrame(dict(
    PC=[f"PC{i+1}" for i in range(len(evr))],
    eigenvalue=pca.explained_variance_,
    var_ratio_standardized=evr,
    cum_standardized=np.cumsum(evr),
    var_ratio_raw=evr_raw,
    cum_raw=np.cumsum(evr_raw),
    eigen_numpy=eig_np))
save_table(var_tab.round(6), "p6_pca_variance")
print(var_tab.head(4).round(4).to_string())

loading = pd.DataFrame(pca.components_[:3].T, index=feat,
                       columns=["PC1", "PC2", "PC3"]).round(4)
save_table(loading.reset_index().rename(columns={"index": "variable"}), "p6_pca_loadings")
print(loading)

# ============================================================ 2. 用前三个主成分回归
Z = scores[:, :3]
Xds = sm.add_constant(Z)
sm_fit = sm.OLS(y, Xds).fit()
mine = ols_fit(y, Xds)
print(sm_fit.summary().as_text()[:1500])

coef_tab = pd.DataFrame(dict(
    term=["const", "PC1", "PC2", "PC3"],
    coef=sm_fit.params, se=sm_fit.bse, t=sm_fit.tvalues, p=sm_fit.pvalues))
save_text(md_table(coef_tab.round(6), "gdp ~ 1 + PC1 + PC2 + PC3（自变量已标准化）"),
          "p6_regression_pcs.md")

# 把 PC 系数换算回标准化变量尺度： beta_x = V beta_PC
V = pca.components_[:3].T                        # 10 x 3
beta_x_std = V @ sm_fit.params[1:]
back = pd.DataFrame({"variable": feat, "beta_on_standardized_x": beta_x_std}).round(6)
save_table(back, "p6_coef_back_to_standardized_x")

# 稳健性：log gdp；以及不标准化的 PCA 回归
y_log = np.log(y)
sm_log = sm.OLS(y_log, Xds).fit()
Xds_raw = sm.add_constant(pca_raw.transform(Xraw)[:, :3])
sm_raw = sm.OLS(y, Xds_raw).fit()
summary = dict(
    n=len(df), n_features=len(feat),
    eigen_check_max_diff=max_diff,
    eigen_sum=float(pca.explained_variance_.sum()),
    eigenvalue_sanity=p_feat * n_obs / (n_obs - 1),
    evr_pc1_3_standardized=[float(v) for v in evr[:3]],
    evr_pc1_3_cum_standardized=float(np.sum(evr[:3])),
    evr_pc1_3_raw=[float(v) for v in evr_raw[:3]],
    evr_pc1_3_cum_raw=float(np.sum(evr_raw[:3])),
    r2_pc3=float(sm_fit.rsquared), r2adj_pc3=float(sm_fit.rsquared_adj),
    f_pc3=float(sm_fit.fvalue), fp_pc3=float(sm_fit.f_pvalue),
    rss_pc3=float(sm_fit.ssr), rss_full_mine=float(mine["rss"]),
    r2_log_pc3=float(sm_log.rsquared), r2adj_log_pc3=float(sm_log.rsquared_adj),
    r2_rawpca_pc3=float(sm_raw.rsquared), r2adj_rawpca_pc3=float(sm_raw.rsquared_adj),
    r2_full_ols_all10=float(sm.OLS(y, sm.add_constant(Xs)).fit().rsquared),
)
print(summary)
save_json(summary, "p6_summary")

# ============================================================ 3. 图
fig, axes = plt.subplots(1, 2, figsize=(9.4, 3.6))
axes[0].bar(range(1, len(evr) + 1), evr, color="#4c72b0")
axes[0].plot(range(1, len(evr) + 1), np.cumsum(evr), "o-", color="crimson", ms=3)
axes[0].axhline(np.sum(evr[:3]), color="crimson", ls=":", lw=1)
axes[0].set(xlabel="principal component", ylabel="proportion of variance",
            title="Scree / cumulative (standardized)")
axes[1].scatter(scores[:, 0], scores[:, 1], s=14, color="#4c72b0")
for i, p in enumerate(df["province"]):
    axes[1].annotate(p[:11], (scores[i, 0], scores[i, 1]), fontsize=5.5,
                     xytext=(2, 2), textcoords="offset points")
axes[1].set(xlabel=f"PC1 ({evr[0]*100:.1f}%)", ylabel=f"PC2 ({evr[1]*100:.1f}%)",
            title="Provinces in the PC1–PC2 plane")
fig.tight_layout()
savefig(fig, "p6_pca")

fig, axes = plt.subplots(1, 2, figsize=(9.0, 3.6))
axes[0].imshow(pca.components_[:3], cmap="RdBu_r", vmin=-0.6, vmax=0.6, aspect="auto")
axes[0].set_yticks(range(3), ["PC1", "PC2", "PC3"])
axes[0].set_xticks(range(len(feat)), feat, rotation=60, ha="right", fontsize=7)
axes[0].set_title("Loadings (standardized)")
axes[1].scatter(sm_fit.fittedvalues, sm_fit.resid, s=16, color="#55a868")
axes[1].axhline(0, color="k", lw=0.8)
axes[1].set(xlabel="fitted gdp (PC regression)", ylabel="residual", title="Residuals vs fitted")
fig.tight_layout()
savefig(fig, "p6_loadings_resid")
print("=== Problem 6 完成 ===")
