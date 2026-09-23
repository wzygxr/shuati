#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")"

OUT_B64="v067_text_sources.tar.gz.b64"
OUT_TGZ="v067_text_sources.tar.gz"

cat \
  source_bundle_parts/v067_text_sources_00.b64part \
  source_bundle_parts/v067_text_sources_01.b64part \
  source_bundle_parts/v067_text_sources_02.b64part \
  source_bundle_parts/v067_text_sources_03_04.b64part \
  source_bundle_parts/v067_text_sources_05_06.b64part \
  source_bundle_parts/v067_text_sources_07_08.b64part \
  source_bundle_parts/v067_text_sources_09_10.b64part \
  source_bundle_parts/v067_text_sources_11_12.b64part \
  source_bundle_parts/v067_text_sources_13_14.b64part \
  source_bundle_parts/v067_text_sources_15.b64part \
  > "$OUT_B64"

echo "0ba710ae2c0a9a017e56d751453951f9a456753d71bebabc35bdab6dee8fb9a0  $OUT_B64" | sha256sum -c -
base64 --decode "$OUT_B64" > "$OUT_TGZ"
echo "2b34b9aca3a877de9ca3168bf22c43e884b83c7dfaa46c2ae8fbad8fc165c5ba  $OUT_TGZ" | sha256sum -c -
tar -xzf "$OUT_TGZ"

echo "v067 文本源码已恢复。"
echo "执行 bash build_release_v067.sh 可重画 26 张配图、复算公式并编译 PDF。"
