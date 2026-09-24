#!/usr/bin/env bash
set -euo pipefail
cat source_bundle_parts/part_* > v074_text_sources.tar.gz.b64
printf '7ab83f94d0abae8e0e7168504e0e655d538953498790d7bc79975cdbc0aba04b  v074_text_sources.tar.gz.b64\n' | sha256sum -c -
base64 -d v074_text_sources.tar.gz.b64 > v074_text_sources.tar.gz
printf '1ed23a5c4c57368f31e4f790ae0675c820ca437bdb17c8c0d24b854597271154  v074_text_sources.tar.gz\n' | sha256sum -c -
mkdir -p v074_text_sources
tar -xzf v074_text_sources.tar.gz -C v074_text_sources
printf 'Recovered into: %s/v074_text_sources\n' "$PWD"
