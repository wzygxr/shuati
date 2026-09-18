from math import gcd, isqrt
from fractions import Fraction
import random

# ---------- helpers ----------
def norm_m5(a,b): return a*a+5*b*b
def norm_m6(a,b): return a*a+6*b*b
def norm_g(a,b): return a*a+b*b

def is_prime(n):
    if n<2: return False
    if n%2==0: return n==2
    d=3
    while d*d<=n:
        if n%d==0: return False
        d+=2
    return True

def square_residues(p): return {x*x%p for x in range(p)}

def gauss_prime(a,b):
    if a==0 and b==0: return False
    a0,b0=abs(a),abs(b)
    if a0==0 or b0==0:
        p=max(a0,b0)
        return is_prime(p) and p%4==3
    return is_prime(a*a+b*b)

# ---------- Z[sqrt(-5)] source examples ----------
assert norm_m5(1,1)==6
assert norm_m5(3,1)==14
for target in (2,3,7,23):
    sols=[]
    B=isqrt(target//5)+2
    for y in range(-B,B+1):
        rem=target-5*y*y
        if rem>=0:
            x=isqrt(rem)
            if x*x==rem:
                sols += [(x,y),(-x,y)] if x else [(0,y)]
    assert not sols, (target,sols)
assert (1*1+5)==6 and (3*3+5)==14 and (8*8+5)==69
# 13,17 prime criterion: -5 non-residue
for p in (13,17,11):
    assert (-5)%p not in square_residues(p)
# norm-one rational field counterexample
r,s=Fraction(2,3),Fraction(1,3)
assert r*r+5*s*s==1 and (r.denominator!=1 or s.denominator!=1)
# Bezout obstruction from coefficient equations: 3a-6b-9d=1 impossible mod 3
for a in range(-5,6):
  for b in range(-5,6):
    for c in range(-5,6):
      for d in range(-5,6):
        if 3*a+2*c-5*d==1 and 3*b+c+2*d==0:
            raise AssertionError('unexpected Bezout solution')
# Z[sqrt(-6)] nonunique factorization 10
assert norm_m6(2,0)==4 and norm_m6(5,0)==25 and norm_m6(2,1)==10
for target in (2,5):
    assert all(x*x+6*y*y!=target for x in range(-5,6) for y in range(-5,6))

# ---------- polynomial source/errata sanity ----------
# Ex.11(ii) missing primitive counterexample: 2x+6 satisfies p=3 pattern but factors by 2 in Z[x].
assert 6%3==0 and 6%9!=0 and 2%3!=0 and gcd(2,6)!=1
# Ex.11(iii) n=0 counterexample: x^0 + 7*2 = 15 reducible constant.
assert 1+7*2==15 and 15==3*5
# Phi_p(x+1): non-leading binomial coefficients divisible by p and constant not p^2
from math import comb
for p in (2,3,5,7,11,13):
    assert is_prime(p)
    vals=[comb(p,k) for k in range(1,p)]
    assert all(v%p==0 for v in vals)
    assert p%(p*p)!=0

# ---------- quadratic algebraic integer normal form sanity ----------
# sample squarefree d; enumerate half-integral r,s and compare trace/norm integrality with stated lattice.
for d in (-7,-5,-3,2,3,5,6,13):
    # squarefree samples only
    if any(d%(q*q)==0 for q in range(2,isqrt(abs(d))+1)): continue
    for R2 in range(-8,9):  # r=R2/2
      for S2 in range(-8,9): # s=S2/2
        r=Fraction(R2,2); s=Fraction(S2,2)
        trace=2*r
        nor=r*r-d*s*s
        integral=(trace.denominator==1 and nor.denominator==1)
        if d%4 in (2,3):
            lattice=(r.denominator==1 and s.denominator==1)
        else: # d ==1 mod 4, omega=(-1+sqrt d)/2
            # n=2s integer and m=r+s integer
            lattice=((2*s).denominator==1 and (r+s).denominator==1)
        assert integral==lattice,(d,r,s,trace,nor,integral,lattice)

# ---------- Gaussian integers ----------
# nearest lattice Euclidean remainder random test
for _ in range(5000):
    a,b,c,d=[random.randint(-50,50) for _ in range(4)]
    if c==0 and d==0: continue
    den=c*c+d*d
    xr=Fraction(a*c+b*d,den)
    yi=Fraction(b*c-a*d,den)
    # nearest integers; check the 4 surrounding choices to avoid tie issues
    fx=xr.numerator//xr.denominator; fy=yi.numerator//yi.denominator
    candidates=[]
    for qx in (fx,fx+1):
      for qy in (fy,fy+1):
        # r=(a+bi)-(qx+qyi)(c+di)
        ra=a-(qx*c-qy*d); rb=b-(qx*d+qy*c)
        candidates.append((norm_g(ra,rb),ra,rb,qx,qy))
    nr,ra,rb,qx,qy=min(candidates)
    assert nr < den or nr==0,(a,b,c,d,nr,den)
# Gaussian prime classification on bounded box; independently test reducibility by factor search.
def reducible_gauss(a,b):
    n=norm_g(a,b)
    if n<=1: return False
    B=isqrt(n)+1
    # nonunit factor alpha with norm strictly between 1 and n whose norm divides n and exact division exists
    for x in range(-B,B+1):
      for y in range(-B,B+1):
        na=norm_g(x,y)
        if na<=1 or na>=n: continue
        # quotient ((a+bi)(x-yi))/na integer coords
        qr=a*x+b*y; qi=b*x-a*y
        if qr%na==0 and qi%na==0:
            return True
    return False
for a in range(-12,13):
  for b in range(-12,13):
    if a==0 and b==0: continue
    if norm_g(a,b)==1: continue
    empirical=not reducible_gauss(a,b)
    classified=gauss_prime(a,b)
    assert empirical==classified,(a,b,norm_g(a,b),empirical,classified)
# sample rational prime behaviors
assert gauss_prime(0,3) and gauss_prime(0,7)
assert not gauss_prime(0,5)
assert gauss_prime(2,1) and gauss_prime(3,2) and gauss_prime(4,1)

print('ALL V023 SANITY CHECKS PASS')
print('checked Z[sqrt(-5/-6)] norm arguments, source errata, quadratic-integer normal forms, Gaussian Euclidean division and prime classification')