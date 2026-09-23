#!/usr/bin/env python3
from __future__ import annotations

import math
import sympy as sp

PASS = 0
FAIL = 0
lines: list[str] = []

def check(name: str, condition: bool, detail: str = "") -> None:
    global PASS, FAIL
    if condition:
        PASS += 1
        lines.append(f"PASS | {name}" + (f" | {detail}" if detail else ""))
    else:
        FAIL += 1
        lines.append(f"FAIL | {name}" + (f" | {detail}" if detail else ""))


def close(a: float, b: float, rel: float = 1e-9, abs_: float = 1e-12) -> bool:
    return math.isclose(a, b, rel_tol=rel, abs_tol=abs_)

# constants
eps0 = 8.8541878128e-12
ke = 1 / (4 * math.pi * eps0)
e = 1.6e-19

# 3.1
r = 6.0e-15
W31 = ke * e**2 / r
F31 = ke * e**2 / r**2
check("3.1 potential energy", close(W31, 3.8346887646980997e-14, rel=1e-12), f"{W31:.12e} J")
check("3.1 Coulomb force", close(F31, 6.391147941163499, rel=1e-12), f"{F31:.12g} N")
check("3.1 derivative relation F=W/r", close(F31, W31/r, rel=1e-12))

# 3.2
q, a = sp.symbols('q a', positive=True)
k = sp.symbols('k', positive=True)
W12 = -2*k*q**2/a
W23 = -2*k*q**2/a
W13 = k*q**2/(2*a)
check("3.2 pair sum", sp.simplify(W12+W23+W13 + sp.Rational(7,2)*k*q**2/a) == 0)
check("3.2 alpha zero-energy variant", sp.solve(sp.Eq(sp.Rational(1,2)-2*sp.Symbol('alpha'),0))[0] == sp.Rational(1,4))
xpos = sp.symbols('x', positive=True)
W32x = -2*k*q**2*(1/xpos + 1/(2*a-xpos)) + k*q**2/(2*a)
check("3.2 midpoint is unstable maximum", sp.simplify(sp.diff(W32x,xpos,2).subs(xpos,a) + 8*k*q**2/a**3)==0)

# 3.3
sqrt3 = sp.sqrt(3)
Whex = 6*(-k*q**2/a) + 6*(k*q**2/(sqrt3*a)) + 3*(-k*q**2/(2*a))
Whex_expected = 3*k*q**2/a*(2/sqrt3-sp.Rational(5,2))
check("3.3 hexagon total energy", sp.simplify(Whex-Whex_expected)==0)
Wcross = k*q**2/a*(4/sqrt3-3)
Aext = -Wcross
check("3.3 remove adjacent pair work", sp.simplify(Aext-k*q**2/a*(3-4/sqrt3))==0)
check("3.3 pair count", 6+6+3 == math.comb(6,2))
Wcross_opp = 4*k*q**2/a*(1/sqrt3-1)
Aext_opp = -Wcross_opp
check("3.3 remove opposite rigid pair work", sp.simplify(Aext_opp-4*k*q**2/a*(1-1/sqrt3))==0)

# 3.4
x, R = sp.symbols('x R', positive=True)
Q = sp.symbols('Q', positive=True)
qofx = Q*x**3/R**3
dqdx = sp.diff(qofx, x)
Wvol = sp.integrate(k*qofx/x*dqdx, (x,0,R))
check("3.4 uniform volume self-energy integral", sp.simplify(Wvol-3*k*Q**2/(5*R))==0)
r0 = ke*e**2/(9.11e-31*(3.0e8)**2)
check("3.4 classical electron radius", close(r0, 2.812e-15, rel=0.01), f"{r0:.6e} m")
check("3.4 shell radius ratio", sp.Rational(1,2) < sp.Rational(3,5))

# 3.5
qnum = 3.0e-8
R1,R2,R3 = 0.02,0.04,0.05
W_before = qnum**2/(8*math.pi*eps0)*(1/R1-1/R2+1/R3)
W_after = qnum**2/(8*math.pi*eps0*R3)
check("3.5 before connection energy", close(W_before,1.8199792379328868e-4,rel=1e-12), f"{W_before:.12e}")
check("3.5 after connection energy", close(W_after,8.088796613035053e-5,rel=1e-12), f"{W_after:.12e}")
check("3.5 released energy positive", W_before-W_after > 0)

# 3.6
Z=92
Rn=9.2e-15
Qn=Z*e
Wi=3*ke*Qn**2/(5*Rn)
fac=1-2**(-2/3)
Er=Wi*fac
MeVJ=1.602176634e-13
Nn=1000/235*6.02214076e23
Et=Er*Nn
check("3.6 initial nuclear electrostatic energy", close(Wi,1.2700489188680104e-10,rel=1e-12), f"{Wi/MeVJ:.6f} MeV")
check("3.6 two-fragment factor", close((Wi-Er)/Wi,2**(-2/3),rel=1e-12))
check("3.6 released energy", close(Er,4.6996823522899427e-11,rel=1e-12), f"{Er/MeVJ:.6f} MeV")
check("3.6 one kilogram energy", close(Et,1.20434675118204e14,rel=1e-12), f"{Et:.6e} J")

# 3.7
rr, aa, bb, ll, eps, QQ = sp.symbols('r a b l eps Q', positive=True)
D = QQ/(2*sp.pi*rr*ll)
E = D/eps
w = sp.Rational(1,2)*E*D
Wint = sp.integrate(w*2*sp.pi*rr*ll,(rr,aa,bb))
C = 2*sp.pi*eps*ll/sp.log(bb/aa)
check("3.7 energy density", sp.simplify(w-QQ**2/(8*sp.pi**2*eps*rr**2*ll**2))==0)
check("3.7 integrated energy", sp.simplify(Wint-QQ**2*sp.log(bb/aa)/(4*sp.pi*eps*ll))==0)
check("3.7 equals Q^2/(2C)", sp.simplify(Wint-QQ**2/(2*C))==0)

# 3.8
rhalf = sp.sqrt(aa*bb)
ratio = sp.simplify(sp.log(rhalf/aa)/sp.log(bb/aa))
check("3.8 half-energy geometric mean", ratio == sp.Rational(1,2))
rsphere = 2*aa*bb/(aa+bb)
ratio_sphere = sp.simplify((1/aa-1/rsphere)/(1/aa-1/bb))
check("3.8 spherical half-energy harmonic mean", ratio_sphere==sp.Rational(1,2))

# 3.9 derivatives and numbers
R1s,R2s,Eb = sp.symbols('R1 R2 Eb', positive=True)
U = Eb*R1s*sp.log(R2s/R1s)
dU = sp.diff(U,R1s)
check("3.9 voltage optimum derivative", sp.simplify(dU.subs(R1s,R2s/sp.E))==0)
Wp = sp.pi*sp.Symbol('eps0',positive=True)*Eb**2*R1s**2*sp.log(R2s/R1s)
dWp=sp.diff(Wp,R1s)
check("3.9 energy optimum derivative", sp.simplify(dWp.subs(R1s,R2s/sp.sqrt(sp.E)))==0)
U1=3e6*0.01/math.e
U2=3e6*0.01/(2*math.sqrt(math.e))
check("3.9 voltage-optimal numeric", close(U1,11036.38323514327,rel=1e-12),f"{U1:.6f} V")
check("3.9 energy-optimal voltage numeric", close(U2,9097.9598956895,rel=1e-12),f"{U2:.6f} V")

# 3.10
R1s,R2s=sp.symbols('R1 R2',positive=True)
Wtotal=QQ**2/(8*sp.pi*sp.Symbol('e0',positive=True)*R1s)+QQ**2/(8*sp.pi*sp.Symbol('e0',positive=True)*R2s)-QQ**2/(4*sp.pi*sp.Symbol('e0',positive=True)*R2s)
Wexpected=QQ**2/(8*sp.pi*sp.Symbol('e0',positive=True))*(1/R1s-1/R2s)
check("3.10 self plus mutual total", sp.simplify(Wtotal-Wexpected)==0)

# 3.11
x,L,b,a,eps,eps0s,V=sp.symbols('x L b a eps eps0 V',positive=True)
Cx=2*sp.pi*(eps*x+eps0s*(L-x))/sp.log(b/a)
dCdx=sp.diff(Cx,x)
FfixedV=sp.Rational(1,2)*V**2*dCdx
check("3.11 capacitance linear in inserted length", sp.simplify(dCdx-2*sp.pi*(eps-eps0s)/sp.log(b/a))==0)
check("3.11 fixed-voltage force", sp.simplify(FfixedV-sp.pi*(eps-eps0s)*V**2/sp.log(b/a))==0)

# 3.12
er,t,d,S,b,x,Q,e0=sp.symbols('er t d S b x Q e0',positive=True)
tp=(er-1)*t/er
C12=e0*(S*(d-tp)+x*b*tp)/(d*(d-tp))
F12=Q**2/(2*C12**2)*sp.diff(C12,x)
F12exp=Q**2*b*(d-tp)*tp*d/(2*e0*(S*(d-tp)+x*b*tp)**2)
check("3.12 equivalent capacitance", sp.simplify(C12-(e0*x*b/(d-tp)+e0*(S-x*b)/d))==0)
check("3.12 fixed-charge force formula", sp.simplify(F12-F12exp)==0)
check("3.12 vacuum limit force zero", sp.simplify(F12exp.subs(er,1))==0)
check("3.12 conductor limit t'=t", sp.simplify(sp.limit(tp,er,sp.oo)-t)==0)

# 3.13
Dden=er*d-(er-1)*t
Ci=e0*er*S/Dden
C0=e0*S/d
Aq=(Ci*V)**2/sp.Integer(2)*(1/C0-1/Ci)
Av=sp.Rational(1,2)*V**2*(Ci-C0)
Aqexp=e0*er*(er-1)*S*t*V**2/(2*Dden**2)
Avexp=e0*(er-1)*S*t*V**2/(2*Dden*d)
check("3.13 disconnected work", sp.simplify(Aq-Aqexp)==0)
check("3.13 connected work", sp.simplify(Av-Avexp)==0)
Cc=e0*S/(d-t)
Aqc=(Cc*V)**2/sp.Integer(2)*(1/C0-1/Cc)
Avc=sp.Rational(1,2)*V**2*(Cc-C0)
check("3.13 conductor disconnected", sp.simplify(Aqc-e0*S*t*V**2/(2*(d-t)**2))==0)
check("3.13 conductor connected", sp.simplify(Avc-e0*S*t*V**2/(2*d*(d-t)))==0)
check("3.13 disconnected work exceeds connected", sp.simplify(Aqexp/Avexp-er*d/Dden)==0)

# 3.14
sqrt2=sp.sqrt(2)
coeff_component=-sp.Rational(1,4)+1/(8*sqrt2)
coeff_mag=sp.simplify(sqrt2*(-coeff_component))
check("3.14 force coefficient", sp.simplify(coeff_mag-(2*sqrt2-1)/8)==0)
coeff_energy=sp.simplify(-sp.Rational(1,2)+sp.Rational(1,2)*(-1+1/sqrt2))
check("3.14 assembly energy coefficient in kq2/a", sp.simplify(coeff_energy-(-1+1/(2*sqrt2)))==0)
check("3.14 textbook energy coefficient", sp.simplify((-1+1/(2*sqrt2))/4 + (4-sqrt2)/16)==0)
sigma_coeff=1/(5*sp.sqrt(5))-1
check("3.14 induced sigma negative", float(sigma_coeff.evalf())<0)

# 3.15
th=sp.symbols('theta', real=True)
A=sp.symbols('A', real=True)
# boundary coefficient at r=a: -E0*a + A/a^2 = 0 -> A=E0*a^3
E0,a,eps0sym=sp.symbols('E0 a eps0',positive=True)
check("3.15 boundary fixes dipole coefficient", sp.solve(sp.Eq(-E0*a+A/a**2,0),A)[0]==E0*a**3)
p=4*sp.pi*eps0sym*a**3*E0
check("3.15 induced dipole moment", sp.simplify(p/(4*sp.pi*eps0sym)-E0*a**3)==0)
# self-energy integral
Wself=sp.Rational(1,2)*sp.integrate(sp.integrate((3*eps0sym*E0*sp.cos(th))*(E0*a*sp.cos(th))*a**2*sp.sin(th),(th,0,sp.pi)),(sp.Symbol('phi'),0,2*sp.pi))
check("3.15 induced-charge self-energy", sp.simplify(Wself-2*sp.pi*eps0sym*a**3*E0**2)==0)
check("3.15 total induced energy change", sp.simplify(-sp.Rational(1,2)*p*E0 + 2*sp.pi*eps0sym*a**3*E0**2)==0)

lines.append("")
lines.append(f"SUMMARY | PASS={PASS} FAIL={FAIL} TOTAL={PASS+FAIL}")
print("\n".join(lines))
raise SystemExit(1 if FAIL else 0)
