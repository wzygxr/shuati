"""
SIMIS 2026  《回归模型与方差分析》 Assignment 1  —— 公共工具函数

本文件提供：
  * 数据 / 结果 / 图目录路径
  * 最小二乘拟合 + 常用模型评价指标（RSS, R^2, adj R^2, AIC, BIC, LOOCV/PRESS, DW）
  * Markdown / CSV / JSON 结果输出工具

作者：作业解答
"""
from __future__ import annotations

import json
import os

import numpy as np
import pandas as pd
from scipy import stats

HERE = os.path.dirname(os.path.abspath(__file__))
ROOT = os.path.dirname(HERE)                       # homework/regression-2026-hw1
DATA_DIR = os.path.join(ROOT, "data")
RESULTS_DIR = os.path.join(ROOT, "results")
FIGURES_DIR = os.path.join(ROOT, "figures")
for _d in (RESULTS_DIR, FIGURES_DIR):
    os.makedirs(_d, exist_ok=True)


# ----------------------------------------------------------------------------- data
def load(name: str) -> pd.DataFrame:
    """读取 data/ 下的数据文件，name 不带 .csv 后缀。"""
    return pd.read_csv(os.path.join(DATA_DIR, name + ".csv"))


# ----------------------------------------------------------------------------- OLS
def ols_fit(y, X) -> dict:
    """
    普通最小二乘（含截距的 X 需自行提供一列 1）。

    返回字典：beta, se, t, pval, fitted, resid, n, p, rss, tss, r2, r2adj,
              aic, bic, loocv(PRESS), dw(Durbin-Watson), sigma2
    其中 AIC = n*log(RSS/n) + 2p,  BIC = n*log(RSS/n) + p*log(n)
    （与 R 中 AIC()/BIC() 只差常数，模型比较等价）
    """
    y = np.asarray(y, dtype=float).ravel()
    X = np.asarray(X, dtype=float)
    n, p = X.shape
    XtX_inv = np.linalg.pinv(X.T @ X)
    beta = XtX_inv @ (X.T @ y)
    fitted = X @ beta
    resid = y - fitted
    rss = float(resid @ resid)
    tss = float(((y - y.mean()) ** 2).sum())
    h = np.einsum("ij,jk,ik->i", X, XtX_inv, X)          # 杠杆值
    loocv = float(np.mean((resid / (1.0 - h)) ** 2))      # PRESS / n
    sigma2 = rss / (n - p) if n > p else np.nan
    se = np.sqrt(np.diag(XtX_inv) * sigma2)
    with np.errstate(divide="ignore", invalid="ignore"):
        tval = beta / se
    pval = 2 * stats.t.sf(np.abs(tval), df=max(n - p, 1))
    r2 = 1 - rss / tss
    r2adj = 1 - (1 - r2) * (n - 1) / (n - p)
    aic = n * np.log(rss / n) + 2 * p
    bic = n * np.log(rss / n) + p * np.log(n)
    dw = float(((np.diff(resid)) ** 2).sum() / rss) if rss > 0 else np.nan
    return dict(beta=beta, se=se, t=tval, pval=pval, fitted=fitted, resid=resid,
                n=n, p=p, rss=rss, tss=tss, r2=float(r2), r2adj=float(r2adj),
                aic=float(aic), bic=float(bic), loocv=loocv, dw=dw, sigma2=float(sigma2))


def model_row(name: str, fit: dict, k: int | None = None) -> dict:
    """把 ols_fit 的结果整理成表格中的一行。"""
    return dict(model=name, k=(fit["p"] if k is None else k), RSS=fit["rss"],
                R2=fit["r2"], R2adj=fit["r2adj"], AIC=fit["aic"], BIC=fit["bic"],
                LOOCV=fit["loocv"])


# ----------------------------------------------------------------------------- 输出
def md_table(df: pd.DataFrame, title: str | None = None, floatfmt: str = "{:.4f}") -> str:
    """把 DataFrame 转成 Markdown 表格（不依赖 tabulate）。"""

    def fmt(v):
        if isinstance(v, (float, np.floating)):
            if np.isnan(v):
                return "NA"
            if v != 0 and (abs(v) < 1e-4 or abs(v) >= 1e6):
                return f"{v:.4e}"
            return floatfmt.format(v)
        return str(v)

    header = "| " + " | ".join(str(c) for c in df.columns) + " |"
    sep = "| " + " | ".join("---" for _ in df.columns) + " |"
    rows = ["| " + " | ".join(fmt(v) for v in row) + " |" for row in df.itertuples(index=False)]
    body = "\n".join([header, sep] + rows)
    return (f"**{title}**\n\n" if title else "") + body + "\n"


def save_text(text: str, fname: str, folder: str = RESULTS_DIR) -> str:
    path = os.path.join(folder, fname)
    with open(path, "w", encoding="utf-8") as fh:
        fh.write(text)
    return path


def save_table(df: pd.DataFrame, name: str, title: str | None = None) -> str:
    """同时保存 markdown（便于写报告）与 csv（便于复核）。"""
    save_text(md_table(df, title), name + ".md")
    df.to_csv(os.path.join(RESULTS_DIR, name + ".csv"), index=False)
    return os.path.join(RESULTS_DIR, name + ".md")


def save_json(obj, name: str) -> str:
    path = os.path.join(RESULTS_DIR, name + ".json")

    def default(o):
        if isinstance(o, (np.floating, np.integer)):
            return o.item()
        if isinstance(o, np.ndarray):
            return o.tolist()
        raise TypeError(str(type(o)))

    with open(path, "w", encoding="utf-8") as fh:
        json.dump(obj, fh, ensure_ascii=False, indent=2, default=default)
    return path


def savefig(fig, name: str, dpi: int = 150) -> str:
    path = os.path.join(FIGURES_DIR, name + ".png")
    fig.savefig(path, dpi=dpi, bbox_inches="tight")
    print(f"[figure] {path}")
    return path
