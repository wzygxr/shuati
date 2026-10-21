#!/usr/bin/env bash
# 一键复现所有计算结果（Python 3.11+，需要 numpy/pandas/scipy/statsmodels/scikit-learn/matplotlib）
set -euo pipefail
cd "$(dirname "$0")"
PY=${PY:-python3}
$PY p1_p4_verify.py
$PY p5_power.py
$PY p6_argentina.py
$PY p7_tips.py
$PY p8_student.py
echo "全部完成：表格见 ../results/，图见 ../figures/"
