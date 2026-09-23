#!/usr/bin/env bash
set -euo pipefail

here="$(cd "$(dirname "$0")" && pwd)"
parts="$here/source_bundle_parts"
work="$here/.reconstruct_tmp"
archive="$here/jiang_sixi_em_v003_text_source.tar.gz"

rm -rf "$work"
mkdir -p "$work"

cat \
  "$parts/part_000.b64part" \
  "$parts/part_001.b64part" \
  "$parts/part_002.b64part" > "$work/source_bundle.b64"

# part_003 的有效载荷严格为前 16000 字节；忽略上传接口可能附加的尾字符。
dd if="$parts/part_003.b64part" bs=1 count=16000 status=none >> "$work/source_bundle.b64"

cat \
  "$parts/part_004.b64part" \
  "$parts/part_005.b64part" \
  "$parts/part_006.b64part" \
  "$parts/part_007_00.b64part" \
  "$parts/part_007_01.b64part" \
  "$parts/part_007_02.b64part" \
  "$parts/part_007_03.b64part" >> "$work/source_bundle.b64"

base64 -d "$work/source_bundle.b64" > "$archive"
(
  cd "$here"
  sha256sum -c SOURCE_BUNDLE_SHA256.txt
)

echo "Reconstructed: $archive"
echo "Extract with: tar -xzf '$archive'"
