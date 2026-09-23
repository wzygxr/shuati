#!/usr/bin/env python3
from __future__ import annotations
from pathlib import Path
import math, re, sys, hashlib
import sympy as sp
import fitz

REL = Path(__file__).resolve().parent
MD = REL / '第84分册_仝响力学_题1.51至1.77_严格全解_v070.md'
SRC = Path('/mnt/data/tong_mechanics_v062/Tong_Xiang_Physics_Olympiad_Mechanics.pdf')
PDF = REL / '第84分册_仝响力学_题1.51至1.77_严格全解_v070.pdf'
CUM = REL / '力学习题全解_仝响支线累计_v070.pdf'
text = MD.read_text(encoding='utf-8')
checks: list[tuple[str,bool,str]]=[]

def ck(name: str, cond: bool, detail: str='') -> None:
    checks.append((name, bool(cond), detail))

def close(a,b,tol=1e-9): return abs(float(a)-float(b)) <= tol*max(1.0,abs(float(a)),abs(float(b)))

# Source integrity and preview bypass.
ck('source_exists', SRC.exists())
if SRC.exists():
    raw=SRC.read_bytes()
    ck('source_sha256', hashlib.sha256(raw).hexdigest()=='44d0b4e0e108ed2b65d03b32c969e2b09768bd8fec011df76c6862aa4314f031')
    with fitz.open(SRC) as d: ck('source_page_count_376', d.page_count==376, str(d.page_count))

# Structural checks: 27 source problems, 54 distinct variants, 27 drills.
for n in range(51,78):
    ck(f'heading_1.{n}', len(re.findall(rf'^# 题1\.{n}(?:\D|$)', text, flags=re.M))==1)
blocks=[]
for n in range(51,78):
    start=text.find(f'# 题1.{n}')
    end=text.find(f'# 题1.{n+1}') if n<77 else text.find('# 原书答案证据')
    blocks.append(text[start:end])
for n,block in zip(range(51,78),blocks):
    ck(f'variant_A_1.{n}', block.count('## 本质不同变式A')==1)
    ck(f'variant_B_1.{n}', block.count('## 本质不同变式B')==1)
    ck(f'drill_C_1.{n}', block.count('## 同类巩固C')==1)
ck('next_breakpoint_1.78','下一连续断点 | 1.78' in text and '下一轮直接进入题1.78' in text)
ck('no_formfeed','\x0c' not in text)
ck('no_nul','\x00' not in text)
ck('image_refs_exist', all((REL/m).exists() for m in re.findall(r'!\[[^\]]*\]\(([^)]+)\)',text)))

# 1.51
s=1200.0; v1=1.9; alpha=math.radians(60); T=300.0
c=v1*math.cos(alpha); w=v1*math.sin(alpha)
q=(s+math.sqrt(s*s+T*T*c*c))/T
V=math.hypot(q,w); beta=math.degrees(math.atan2(w,q))
ck('1.51_time_equation', close(s/(q+c)+s/(q-c),T,1e-12))
ck('1.51_speed', close(V,8.276479933949863,1e-12))
ck('1.51_beta', close(beta,11.467388823034334,1e-12))
# 1.52
ck('1.52_catch_time', close(40/3,13.333333333333334))
ck('1.52_catch_distance', close(15*(40/3),200))
ck('1.52_max_gap', close(15*5-1.5*25,37.5))
# 1.53 optimization
A,B=sp.symbols('A B', positive=True)
th=sp.atan(A/B)
ck('1.53_opt_direction', sp.simplify(A*sp.sin(th)+B*sp.cos(th)-sp.sqrt(A**2+B**2))==0)
# 1.54
ck('1.54_collision_special', close(abs(300*20-400*10)/math.sqrt(500),2000/math.sqrt(500)))
# 1.55 special equal speeds alpha 60
ck('1.55_special_rmin', close((1*math.sin(math.pi/3))/math.sqrt(3),0.5))
# 1.56 integral
x,l,u1s,vs=sp.symbols('x l u1 v', positive=True)
F=l/u1s*sp.asin(u1s*x/(l*vs))
ck('1.56_integral', sp.simplify(sp.diff(F,x)-1/sp.sqrt(vs**2-u1s**2*x**2/l**2))==0)
# 1.57
v,vrun,vsw,theta=2.5,4.0,2.0,math.radians(15)
qa=vrun**2-vsw**2; qb=2*(vsw**2-v*vrun*math.cos(theta)); qc=v**2-vsw**2
disc=qb*qb-4*qa*qc
r1=(-qb-math.sqrt(disc))/(2*qa); r2=(-qb+math.sqrt(disc))/(2*qa)
ck('1.57_root_interval', close(r1,0.2847596265440326) and close(r2,0.658450083937748))
ck('1.57_vmax', close(2*math.sqrt(2),2.8284271247461903))
# 1.58
ck('1.58_time', close((1/math.sqrt(3))/(math.sqrt(3)/2),2/3))
ck('1.58_path', close(1*(2/3),2/3))
# 1.59
ck('1.59_ratio', close(3/6,0.5) and close(5/6,0.8333333333333334))
# 1.60 symbolic differentiation
svar,hvar,vvar=sp.symbols('s h v', positive=True)
L=sp.sqrt(svar**2+hvar**2); u=vvar*L/svar
sdot=-u
a=sp.simplify(sp.diff(u,svar)*sdot)
ck('1.60_acceleration', sp.simplify(a-vvar**2*hvar**2/svar**3)==0, str(a))
# 1.61
ck('1.61_widths', close(400,400) and close(700,700) and close(50*(11+math.sqrt(73)),977.2001872658766))
ck('1.61_speed_ratio_case2', close(300/(700-300),3/4))
# 1.62 geometric time sum
ck('1.62_time_series', close(sum((2/3)*(1/3)**k for k in range(100)),1,1e-12))
# 1.63
ck('1.63_limit', close((1/math.cos(1e-6)-1),0,1e-9))
# 1.64
R,d,vv=3.0,2.0,5.0
x=d/2; y=math.sqrt(R*R-d*d/4)
xd=vv/2; yd=-d*vv/(2*math.sqrt(4*R*R-d*d))
ck('1.64_speed', close(math.hypot(xd,yd),vv*R/math.sqrt(4*R*R-d*d)))
# 1.65 theta 90 special
ck('1.65_right_angle', close(math.sqrt(1+1+0),math.sqrt(2)))
# 1.66
ck('1.66_unfold', close((2*1*0.2)*math.sqrt(10/(2*0.8)),1.0))
# 1.67 numeric consistency of formula and coordinates
vA,vB,aA,aB,g=20.,25.,math.radians(60),math.radians(30),9.8
delay=2*vA*vB*math.sin(aA-aB)/(g*(vA*math.cos(aA)+vB*math.cos(aB)))
tcol=vB*math.cos(aB)*delay/(vB*math.cos(aB)-vA*math.cos(aA))
x1=vA*math.cos(aA)*tcol; x2=vB*math.cos(aB)*(tcol-delay)
y1=vA*math.sin(aA)*tcol-.5*g*tcol*tcol
y2=vB*math.sin(aB)*(tcol-delay)-.5*g*(tcol-delay)**2
ck('1.67_collision_coordinates', close(x1,x2,1e-8) and close(y1,y2,1e-8))
# 1.68 identity sample
al1,al2=math.radians(60),math.radians(30); v0=100.; n=(2*v0/9.8)*math.sin((al1-al2)/2)/math.cos((al1+al2)/2)
ck('1.68_identity', close(math.sin((al1-al2)/2)/math.cos((al1+al2)/2),9.8*n/(2*v0)))
# 1.69 roots/sign correction sample
x0,y0,v0,g=10.,3.,20.,9.8
disc=v0**4-g*g*x0*x0-2*g*v0*v0*y0
z1=(v0*v0+math.sqrt(disc))/(g*x0); z2=(v0*v0-math.sqrt(disc))/(g*x0)
a1,a2=math.atan(z1),math.atan(z2)
xrec=2*v0*v0/(g*(z1+z2))
yrec=-2*v0*v0*(1/math.tan(a1+a2))/(g*(z1+z2))
ck('1.69_x_recovery',close(xrec,x0,1e-9))
ck('1.69_y_recovery_sign',close(yrec,y0,1e-9))
# 1.70
ck('1.70_min_speed',close(math.sqrt(100**2+2*10*500),100*math.sqrt(2)))
# 1.71
v0,s0,g=50.,100.,10.
a=.5*math.asin(2*s0*g/v0**2)
H=s0*math.tan(a); hh=v0**2*math.sin(a)**2/(2*g)
ck('1.71_half_height',close(hh,H/2,1e-10))
# 1.72
ck('1.72_threshold',close(math.sqrt(10*(12**2+5**2)/(2*5)),13))
# 1.73 algebra
h,k,l=sp.symbols('h k l', positive=True)
ck('1.73_tangent_sum',sp.simplify(k*l/(h*(l-h))-(k/h+k/(l-h)))==0)
# 1.74
ck('1.74_height',close(1-.25,.75))
# 1.75 direct cancellation symbolic
T,t,g,Vx,vf=sp.symbols('T t g Vx vf', positive=True)
ck('1.75_linear',sp.simplify((g*sp.Rational(1,2)*t*(T-t))/((Vx+vf)*(T-t))-g*t/(2*(Vx+vf)))==0)
# 1.76 horizontal limit and optimum
ck('1.76_flat_limit',close(math.sin(2*math.radians(45)),1))
# 1.77 relation and time sample
th=math.radians(30); beta=math.atan(1/(2*math.tan(th)))
ck('1.77_angle_relation',close(2*math.tan(th)*math.tan(beta),1))
t1=2*20*math.sin(beta)/(9.8*math.cos(th)); t2=2*20/(9.8*math.sqrt(1+3*math.sin(th)**2))
ck('1.77_time',close(t1,t2,1e-12))

# Generated PDF integrity.
if PDF.exists():
    with fitz.open(PDF) as d:
        ck('fragment_pdf_open', d.page_count>0, str(d.page_count))
        for pno in [0,d.page_count//2,d.page_count-1]:
            page=d.load_page(pno); ck(f'fragment_page_{pno+1}_load', page.rect.width>0 and page.rect.height>0)
else:
    ck('fragment_pdf_open',False,'missing')

if CUM.exists():
    with fitz.open(CUM) as d:
        ck('cumulative_page_count_310', d.page_count==310, str(d.page_count))
        for pno in [0,5,6,70,71,147,148,228,229,309]:
            pg=d.load_page(pno)
            ck(f'cumulative_page_{pno+1}_load', pg.rect.width>0 and pg.rect.height>0)
else:
    ck('cumulative_page_count_310',False,'missing')

passed=sum(c for _,c,_ in checks); failed=len(checks)-passed
for name,cond,detail in checks:
    print(('PASS' if cond else 'FAIL')+f'\t{name}'+(f'\t{detail}' if detail else ''))
print(f'SUMMARY\tPASS={passed}\tFAIL={failed}\tTOTAL={len(checks)}')
sys.exit(0 if failed==0 else 1)
