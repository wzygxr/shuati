from pathlib import Path
import hashlib
root=Path(__file__).resolve().parent
parts=sorted((root/'markdown_parts').glob('part_*.mdpart'))
out=root/'王矜奉_固体物理教程_全习题详解_累计_v002_Ch1_1-27_27题81变式.md'
out.write_bytes(b''.join(p.read_bytes() for p in parts))
print(out)
print('sha256',hashlib.sha256(out.read_bytes()).hexdigest())
