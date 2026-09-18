from math import gcd
from datetime import date

# ---------- round robin ----------
def schedule_even(N):
    rounds=[]
    for r in range(1,N):
        special = r//2 if r%2==0 else (r+N-1)//2
        pairs=[(special,N)]; used={special,N}
        for x in range(1,N):
            if x in used: continue
            y=(r-x)%(N-1)
            if y==0: y=N-1
            assert y not in used and y!=x
            pairs.append((x,y)); used|={x,y}
        assert len(used)==N
        rounds.append(pairs)
    return rounds

def schedule_odd(N):
    dummy=N+1
    rounds=[]
    for ps in schedule_even(dummy):
        pairs=[]; bye=None
        for a,b in ps:
            if dummy in (a,b): bye=b if a==dummy else a
            else: pairs.append((a,b))
        rounds.append((pairs,bye))
    return rounds

def check_schedule(N):
    seen=set()
    if N%2==0:
        rs=[(p,None) for p in schedule_even(N)]
        assert len(rs)==N-1
    else:
        rs=schedule_odd(N); assert len(rs)==N
    for pairs,bye in rs:
        used=set()
        for a,b in pairs:
            assert a!=b and a not in used and b not in used
            used|={a,b}; seen.add(tuple(sorted((a,b))))
        if bye is not None:
            assert bye not in used and len(used)==N-1
        else:
            assert len(used)==N
    assert len(seen)==N*(N-1)//2
    assert seen=={(a,b) for a in range(1,N+1) for b in range(a+1,N+1)}
for N in (6,7,9,10): check_schedule(N)
# exercise 2/3 formula check for several even N
for N in range(4,22,2):
    r2=dict(sum(([a,b] for a,b in []),[])) if False else None
    pairs=schedule_even(N)[1]
    opp={}
    for a,b in pairs: opp[a]=b; opp[b]=a
    assert [opp[x] for x in range(1,N+1)]==list(range(N,0,-1))
    for r in range(1,N):
        opp={}
        for a,b in schedule_even(N)[r-1]: opp[a]=b; opp[b]=a
        assert opp[r]==(N-1 if r<=N-2 else N)

# ---------- weekday formula ----------
def book_weekday(Y,M,d):
    if M>=3: N,m=Y,M-2
    else: N,m=Y-1,M+10
    c,y=divmod(N,100)
    return (d+(13*m-1)//5+y+y//4+c//4-2*c)%7
for Y,M,d in [(1937,7,7),(1945,8,14),(1991,9,2),(2000,1,1),(2001,1,1),(2012,2,29)]:
    # Python Monday=0; book Sunday=0 => (weekday+1)%7
    assert book_weekday(Y,M,d)==(date(Y,M,d).weekday()+1)%7,(Y,M,d)
assert book_weekday(1937,7,7)==3
assert book_weekday(1945,8,14)==2

# ---------- lambda_0 ----------
def n0(m,s):
    x=1
    for n in range(1,10000):
        x=x*s%m
        if x in (1%m,(-1)%m): return n
    raise RuntimeError

def lambda0(m):
    vals=[(n0(m,s),s) for s in range(1,m) if gcd(s,m)==1]
    mx=max(x for x,_ in vals)
    return mx,[s for x,s in vals if x==mx]
def carmichael_bruteforce(m):
    units=[s for s in range(1,m) if gcd(s,m)==1]
    for n in range(1,10000):
        if all(pow(s,n,m)==1 for s in units): return n
    raise RuntimeError
expected={16:(4,3),17:(8,3),19:(9,2),22:(5,7),25:(10,2),32:(8,3),36:(6,5),60:(4,7),99:(30,5),100:(20,3)}
for m,(want,s) in expected.items():
    l0,ss=lambda0(m); lam=carmichael_bruteforce(m)
    assert l0==want and s in ss,(m,l0,ss)
    assert n0(m,s)==want
    assert l0 in (lam,lam//2),(m,l0,lam)
assert all(lambda0(m)[0]==carmichael_bruteforce(m) for m in (16,32,36,60,99,100))

# ---------- Nim property P ----------
tuples=[
((2,4,5),False),((3,7,8),False),((6,10,12),True),((16,39,47),False),
((29,63,66),False),((7,12,21,25),False),((58,19,23),False),
((14,31,33,29,63,66),False),
]
for t,want in tuples:
    x=0
    for a in t: x^=a
    assert (x==0)==want,(t,x)
assert 3^5^6==0 and 25^43^50==0

print('ALL V024 SANITY CHECKS PASS')
print('checked round-robin completeness, weekday formula, lambda_0 table, and Nim property P')