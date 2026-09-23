#!/usr/bin/env bash
set -euo pipefail
ROOT="$(cd "$(dirname "$0")" && pwd)"
LOG="$ROOT/qc/rebuild_logs"
mkdir -p "$LOG"

python "$ROOT/scripts/build_figures_v004.py"
python "$ROOT/scripts/qc_v004.py"

compile_md() {
  local base="$1"
  pandoc "$ROOT/$base.md" -s --pdf-engine=xelatex -o "$ROOT/$base.tex" 2>"$LOG/${base}_pandoc.log"
  (cd "$ROOT" && xelatex -interaction=nonstopmode -halt-on-error "$base.tex" >"$LOG/${base}_pass1.log")
  (cd "$ROOT" && xelatex -interaction=nonstopmode -halt-on-error "$base.tex" >"$LOG/${base}_pass2.log")
}

compile_md '孙婷雅_量子力学教程习题剖析_累计详解_v004_第1至4章完成_2026-09-23'
compile_md '孙婷雅_量子力学教程习题剖析_v004_第4章4.1-4.10_增量_2026-09-23'
compile_md '剩余总账_v004_2026-09-23'
compile_md '编译与渲染验收记录_v004'
compile_md '存储与同步状态_v004_2026-09-23'

echo 'v004 rebuild completed.'
