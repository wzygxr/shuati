#!/usr/bin/env bash
set -euo pipefail

here="$(cd "$(dirname "$0")" && pwd)"
parts="$here/source_bundle_parts"
archive="$here/jiang_sixi_em_v004_text_source.tar.gz"

cat \
  "$parts/part_000.b64part" \
  "$parts/part_001.b64part" \
  "$parts/part_002.b64part" \
  | base64 -d > "$archive"

(
  cd "$here"
  sha256sum -c SOURCE_BUNDLE_SHA256.txt
)

echo "Reconstructed and verified: $archive"
echo "Extract with: tar -xzf '$archive'"
