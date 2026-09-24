#!/usr/bin/env bash
set -euo pipefail

if ! compgen -G 'source_bundle_parts/part_*' > /dev/null; then
  cat >&2 <<'EOF'
缺少 source_bundle_parts/part_*。
GitHub 目录只保存版本索引与校验信息；Base64 分片位于本轮 ChatGPT v071 完整交付包中。
请先下载并解压“范小辉两书_Ch9Q26-Q50_完整交付包_v071.zip”，再在含有 source_bundle_parts/ 的目录运行本脚本。
EOF
  exit 2
fi

cat source_bundle_parts/part_* > v071_text_sources.tar.gz.b64
printf '826fa72930598c4db5249480f08e4c51082753e6c76112ee26556b3755032e49  v071_text_sources.tar.gz.b64\n' | sha256sum -c -
base64 -d v071_text_sources.tar.gz.b64 > v071_text_sources.tar.gz
printf 'f18278027978265ffac9b2bc0ad470d6e3ec9feb311b1f08510c992ee2a38cff  v071_text_sources.tar.gz\n' | sha256sum -c -
mkdir -p v071_text_sources
tar -xzf v071_text_sources.tar.gz -C v071_text_sources
printf 'Recovered into: %s/v071_text_sources\n' "$PWD"
