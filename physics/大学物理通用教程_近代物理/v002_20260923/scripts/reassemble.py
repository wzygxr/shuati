from pathlib import Path
import base64
import gzip
import hashlib

ROOT = Path(__file__).resolve().parents[1]
parts = sorted((ROOT / "archive").glob("cumulative_v002.md.gz.b64.part*"))
if len(parts) != 6:
    raise SystemExit(f"expected 6 archive parts, found {len(parts)}")

encoded = "".join(p.read_text(encoding="ascii").strip() for p in parts)
compressed = base64.b64decode(encoded, validate=True)
compressed_sha = hashlib.sha256(compressed).hexdigest()
expected_compressed_sha = "9deadb7760e3675f2dffef533be311fbcae5e75c3a8f1506bb1c8ff5e2b7418e"
if compressed_sha != expected_compressed_sha:
    raise SystemExit(
        f"compressed SHA-256 mismatch: {compressed_sha} != {expected_compressed_sha}"
    )

data = gzip.decompress(compressed)
out = ROOT / "大学物理通用教程_近代物理_Ch5-9_全习题详解_累计_v002.md"
out.write_bytes(data)
sha = hashlib.sha256(data).hexdigest()
expected = "6e7f5c41673216c8f2dffa4dab998db0be62179e341f74a901a512f66bd4d319"
print(out)
print("bytes", out.stat().st_size)
print("sha256", sha)
if sha != expected:
    raise SystemExit(f"SHA-256 mismatch: expected {expected}")
print("PASS")
