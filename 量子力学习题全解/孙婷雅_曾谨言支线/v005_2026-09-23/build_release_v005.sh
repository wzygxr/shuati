#!/usr/bin/env bash
set -euo pipefail
ROOT="$(cd "$(dirname "$0")" && pwd)"
LOG="$ROOT/logs/v005_rebuild"
mkdir -p "$LOG"

python "$ROOT/scripts/build_figures_v005.py"
python "$ROOT/scripts/qc_v005.py"
python "$ROOT/scripts/build_docs_v005.py"

compile_md() {
  local base="$1"
  pandoc "$ROOT/$base.md" -s --pdf-engine=xelatex -o "$ROOT/$base.tex" 2>"$LOG/${base}_pandoc.log"
  (cd "$ROOT" && xelatex -interaction=nonstopmode -halt-on-error "$base.tex" >"$LOG/${base}_pass1.log" 2>&1)
  (cd "$ROOT" && xelatex -interaction=nonstopmode -halt-on-error "$base.tex" >"$LOG/${base}_pass2.log" 2>&1)
}

compile_md '孙婷雅_量子力学教程习题剖析_累计详解_v005_第1至5章完成_2026-09-23'
compile_md '孙婷雅_量子力学教程习题剖析_v005_第5章5.1-5.13_增量_2026-09-23'
compile_md '剩余总账_v005_2026-09-23'
compile_md '编译与渲染验收记录_v005'
compile_md '存储与同步状态_v005_2026-09-23'

echo 'v005 rebuild completed.'
