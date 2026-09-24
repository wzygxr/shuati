#!/usr/bin/env bash
set -euo pipefail
cat source_bundle_parts/part_* > v071_text_sources.tar.gz.b64
printf '826fa72930598c4db5249480f08e4c51082753e6c76112ee26556b3755032e49  v071_text_sources.tar.gz.b64\n' | sha256sum -c -
base64 -d v071_text_sources.tar.gz.b64 > v071_text_sources.tar.gz
printf 'f18278027978265ffac9b2bc0ad470d6e3ec9feb311b1f08510c992ee2a38cff  v071_text_sources.tar.gz\n' | sha256sum -c -
mkdir -p v071_text_sources
tar -xzf v071_text_sources.tar.gz -C v071_text_sources
printf 'Recovered into: %s/v071_text_sources\n' "$PWD"
