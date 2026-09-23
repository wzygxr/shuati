from pathlib import Path
import hashlib

ROOT = Path(__file__).resolve().parents[1]
parts = sorted((ROOT / "parts").glob("cumulative_v002.part*.md"))
if len(parts) != 11:
    raise SystemExit(f"expected 11 parts, found {len(parts)}")

out = ROOT / "大学物理通用教程_近代物理_Ch5-9_全习题详解_累计_v002.md"
out.write_bytes(b"".join(p.read_bytes() for p in parts))
sha = hashlib.sha256(out.read_bytes()).hexdigest()
expected = "f2ca1fdff60a45cc345a3fe93a72eaa4e78ba5d40196397b2b2d8bb95759ba1c"
print(out)
print("bytes", out.stat().st_size)
print("sha256", sha)
if sha != expected:
    raise SystemExit(f"SHA-256 mismatch: expected {expected}")
print("PASS")
