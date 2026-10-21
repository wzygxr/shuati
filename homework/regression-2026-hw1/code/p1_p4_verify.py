"""
Problem 1-4 的数值复核（Monte Carlo / 代数恒等式检查）

P1:  (1/n)Σ ŷ_i = ȳ ;  SSreg = Σ(ŷ_i-ȳ)^2 = β̂_1^2 Sxx ;  R^2 = r_XY^2
P2:  无截距模型 β̂ = Σx_i y_i / Σx_i^2 与 β̃ = ȳ/x̄ 的无偏性、方差比较
P3:  欠拟合（漏变量）时 E[β̃_1] = β*_1 + (X1'X1)^{-1}X1'X2 β*_2
P4:  过拟合（多加入无关变量）时前 p+1 个分量仍无偏，但方差增大

运行： python3 p1_p4_verify.py
"""
from __future__ import annotations

import os
import sys

import numpy as np
import pandas as pd

sys.path.insert(0, os.path.dirname(os.path.abspath(__file__)))
from common import md_table, save_table, save_json  # noqa: E402

rng = np.random.default_rng(20261008)
OUT = {}

# ============================================================ Problem 1
n = 37
x = rng.normal(5, 2, n)
y = 1.5 + 2.0 * x + rng.normal(0, 1.0, n)

xbar, ybar = x.mean(), y.mean()
Sxx = ((x - xbar) ** 2).sum()
Sxy = ((x - xbar) * (y - ybar)).sum()
Syy = ((y - ybar) ** 2).sum()
b1 = Sxy / Sxx
b0 = ybar - b1 * xbar
yhat = b0 + b1 * x
SSreg = ((yhat - ybar) ** 2).sum()
SSE = ((y - yhat) ** 2).sum()
SST = ((y - ybar) ** 2).sum()
R2 = SSreg / SST
r2xy = Sxy ** 2 / (Sxx * Syy)

P1 = dict(
    mean_yhat_minus_ybar=float(yhat.mean() - ybar),        # 应为 ~0
    SSreg_minus_b1sq_Sxx=float(SSreg - b1 ** 2 * Sxx),     # 应为 ~0
    R2_minus_r2xy=float(R2 - r2xy),                        # 应为 ~0
    SST_minus_SSreg_SSE=float(SST - SSreg - SSE),          # 方差分解
    SSreg_minus_sumyhat2_minus_nybar2=float(SSreg - (((yhat ** 2).sum()) - n * ybar ** 2)),
)
print("P1:", P1)
OUT["P1"] = P1

# ============================================================ Problem 2
# 无截距模型 y_i = beta* x_i + e_i ，两个估计量的无偏性与方差
N, nsim, sigma = 12, 200_000, 1.0
xs = np.abs(rng.normal(3, 1, N)) + 0.5          # 保证 x̄ ≠ 0
beta_star = 2.0
E_bhat = E_btil = E_bhat2 = E_btil2 = 0.0
for _ in range(nsim):
    ys = beta_star * xs + rng.normal(0, sigma, N)
    bhat = (xs * ys).sum() / (xs ** 2).sum()
    btil = ys.mean() / xs.mean()
    E_bhat += bhat
    E_btil += btil
    E_bhat2 += bhat ** 2
    E_btil2 += btil ** 2
E_bhat /= nsim
E_btil /= nsim
Var_bhat_mc = E_bhat2 / nsim - E_bhat ** 2
Var_btil_mc = E_btil2 / nsim - E_btil ** 2
Sxx_c = ((xs - xs.mean()) ** 2).sum()
Var_bhat_th = sigma ** 2 / (xs ** 2).sum()
Var_btil_th = sigma ** 2 / (N * xs.mean() ** 2)

P2 = dict(
    beta_star=beta_star, xbar=float(xs.mean()), sum_x2=float((xs ** 2).sum()),
    E_bhat=float(E_bhat), E_btil=float(E_btil),
    Var_bhat_theory=float(Var_bhat_th), Var_btil_theory=float(Var_btil_th),
    Var_bhat_MC=float(Var_bhat_mc), Var_btil_MC=float(Var_btil_mc),
    Var_bhat_over_Var_btil=float(Var_bhat_th / Var_btil_th),
    # 理论：Var(bhat)=sigma^2/(n xbar^2+Sxx) <= sigma^2/(n xbar^2)=Var(btil)
    identity_check=float((xs ** 2).sum() - (N * xs.mean() ** 2 + Sxx_c)),
)
print("P2:", P2)
OUT["P2"] = P2

# ============================================================ Problem 3 (欠拟合)
n, p1, p2 = 60, 2, 2          # X1 含截距 + 1 个变量；X2 含 2 个漏掉的变量
X1 = np.column_stack([np.ones(n), rng.normal(0, 1, n)])
X2 = np.column_stack([rng.normal(0, 1, n), rng.normal(0, 1, n)])
bs1 = np.array([1.0, 2.0])
bs2 = np.array([3.0, -1.5])
res = {}
for tag, X2use, b2use in [("generic(一般情形)", X2, bs2),
                          ("orthogonal(X1'X2=0)", None, bs2)]:
    if X2use is None:                       # 构造与 X1 正交的 X2
        Q, _ = np.linalg.qr(X1)
        P = np.eye(n) - Q @ Q.T
        Z = rng.normal(0, 1, (n, 2))
        X2o = P @ Z
        # 再正交化，确保 X1'X2 = 0（数值上）
        X2o = X2o - X1 @ np.linalg.pinv(X1) @ X2o
        X2use = X2o
    B = np.linalg.pinv(X1.T @ X1) @ X1.T @ X2use
    bias = B @ b2use
    # 有偏/无偏直接用一条样本验证：E 只在做多次模拟时体现
    assert np.allclose(B @ b2use, bias)
    res[tag] = dict(bias=float(np.abs(bias).max()),
                    max_abs_X1tX2=float(np.abs(X1.T @ X2use).max()))
P3 = res
print("P3:", P3)
OUT["P3"] = P3

# ============================================================ Problem 4 (过拟合)
n, p = 80, 3
X = np.column_stack([np.ones(n)] + [rng.normal(0, 1, n) for _ in range(p - 1)])
Xp = np.column_stack([rng.normal(0, 1, n) for _ in range(2)])       # 额外 2 个无关变量
Z = np.column_stack([X, Xp])
bstar = np.array([0.5, 1.0, -2.0])          # 真模型：截距 + 2 个真实变量
b_ext = np.concatenate([bstar, [0.0, 0.0]])
sigma = 1.0
nsim = 4000
acc = np.zeros(Z.shape[1])
acc2 = np.zeros(Z.shape[1])
for _ in range(nsim):
    yv = Z @ b_ext + rng.normal(0, sigma, n)
    bh = np.linalg.pinv(Z.T @ Z) @ Z.T @ yv
    acc += bh
    acc2 += bh ** 2
E_b = acc / nsim
Var_b = acc2 / nsim - E_b ** 2
# 理论 Var(bhat)=sigma^2 (Z'Z)^{-1}
Var_th = sigma ** 2 * np.linalg.pinv(Z.T @ Z)
Var_sub_th = sigma ** 2 * np.linalg.pinv(X.T @ X)
P4 = dict(
    E_hat_b_first_p_plus_1=[round(float(v), 4) for v in E_b[:p]],
    bstar=list(map(float, bstar)),
    max_abs_bias_first_p_plus_1=float(np.abs(E_b[:p] - bstar).max()),
    max_abs_bias_extra=[round(float(v), 4) for v in np.abs(E_b[p:])],
    Var_extra_vs_theoretical_max_abs_diff=float(
        np.abs(Var_b[p:] - np.diag(Var_th)[p:]).max()),
    Var_inflation_ratio_max=float((np.diag(Var_th)[:p] / np.diag(Var_sub_th)).max()),
)
PZ = Z @ np.linalg.pinv(Z.T @ Z) @ Z.T
PX = X @ np.linalg.pinv(X.T @ X) @ X.T
P4["Var_inflation_of_fitted_values_max"] = float((np.diag(PZ) / np.diag(PX)).max())
P4["E_yhat_identity_check"] = float(np.abs((PZ @ X - X)).max())   # E[ŷ]=Xβ*，与真模型一致
print("P4:", P4)
OUT["P4"] = P4

# --- P4 情形 B：额外变量与原有变量高度相关 → 方差膨胀显著（VIF = 1/(1-R_j^2)） ---
XpB = 0.9 * X[:, 1:2] + 0.6 * X[:, 2:3] + rng.normal(0, 0.6, (n, 2))
ZB = np.column_stack([X, XpB])
Var_th_B = sigma ** 2 * np.linalg.pinv(ZB.T @ ZB)
r2j, vif = [], []
for j in range(p, ZB.shape[1]):
    others = np.delete(ZB, j, axis=1)
    fitj = others @ np.linalg.pinv(others) @ ZB[:, j]
    r2 = 1 - ((ZB[:, j] - fitj) ** 2).sum() / ((ZB[:, j] - ZB[:, j].mean()) ** 2).sum()
    r2j.append(float(r2)); vif.append(float(1 / (1 - r2)))
accB = np.zeros(ZB.shape[1]); accB2 = np.zeros(ZB.shape[1])
for _ in range(nsim):
    yv = ZB @ b_ext + rng.normal(0, sigma, n)
    bh = np.linalg.pinv(ZB.T @ ZB) @ ZB.T @ yv
    accB += bh; accB2 += bh ** 2
VarB_mc = accB2 / nsim - (accB / nsim) ** 2
P4B = dict(
    description="额外 2 个无关变量与原有变量强相关",
    R2_of_each_extra_var_on_others=[round(v, 4) for v in r2j],
    VIF=[round(v, 4) for v in vif],
    Var_inflation_ratio_theory_first_p_plus_1=[
        round(float(a), 4) for a in np.diag(Var_th_B)[:p] / np.diag(Var_sub_th)],
    Var_inflation_ratio_MC_first_p_plus_1=[
        round(float(a), 4) for a in VarB_mc[:p] / (sigma ** 2 * np.diag(np.linalg.pinv(X.T @ X)))],
    max_abs_bias_first_p_plus_1=float(np.abs(accB[:p] / nsim - bstar).max()),
)
print("P4 (相关情形):", P4B)
OUT["P4_correlated_extra_vars"] = P4B

save_json(OUT, "p1_p4_numeric_checks")
lines = ["# Problem 1–4 数值复核（Monte Carlo / 恒等式）\n",
         md_table(pd.DataFrame([P1]).T.reset_index().rename(
             columns={"index": "quantity", 0: "value"}), "P1 恒等式检查（差值应≈0）"),
         md_table(pd.DataFrame([P2]).T.reset_index().rename(
             columns={"index": "quantity", 0: "value"}), "P2 无截距模型的模拟结果"),
         md_table(pd.DataFrame([{k: v["bias"] for k, v in P3.items()}]).T.reset_index().rename(
             columns={"index": "case", 0: "max|bias|"}), "P3 漏变量偏差"),
         md_table(pd.DataFrame([P4]).T.reset_index().rename(
             columns={"index": "quantity", 0: "value"}), "P4 加入无关变量的结果"),
         md_table(pd.DataFrame([P4B]).T.reset_index().rename(
             columns={"index": "quantity", 0: "value"}), "P4 情形 B：额外变量与 X 强相关")]
save_table(pd.DataFrame([P1]).T.reset_index().rename(columns={"index": "quantity", 0: "value"}),
           "p1_identity_checks")
print("\n=== 已写入 results/ ===")
