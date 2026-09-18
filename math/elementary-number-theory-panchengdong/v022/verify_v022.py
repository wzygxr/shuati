from itertools import combinations

# v022 sanity checks: arithmetic/order examples + Peano-independence finite models.

def powerset(s):
    s=list(s)
    for r in range(len(s)+1):
        for c in combinations(s,r):
            yield frozenset(c)

def induction_axiom(M, succ, e):
    for S in powerset(M):
        if e not in S:
            continue
        closed=True
        for x in S:
            if x in succ and succ[x] not in S:
                closed=False; break
        if closed and S != frozenset(M):
            return False
    return True

def injective_on_defined(succ):
    vals=list(succ.values())
    return len(vals)==len(set(vals))

def no_predecessor(succ,e):
    return e not in set(succ.values())

for a in range(1,50):
    for b in range(1,50):
        for c in range(1,20):
            assert (a*b >= a*c) == (b >= c)
            if a == b*c:
                assert a >= b
                assert (a == b) == (c == 1)
for a in range(2,30):
    for b in range(1,a):
        for c in range(2,30):
            for d in range(1,c):
                assert a+c > b+d
                assert a*c > b*d

A=frozenset({1}); B=frozenset({2})
assert not (A <= B or B <= A)
assert not (2%3==0 or 3%2==0)

M={'e','a'}; succ={'e':'a'}; e='e'
assert no_predecessor(succ,e)
assert injective_on_defined(succ)
assert induction_axiom(M,succ,e)
assert set(succ.keys()) != M

M={'e','a','b'}; succ={'e':'a','a':'b','b':'e'}; e='e'
assert set(succ.keys())==M and injective_on_defined(succ)
assert induction_axiom(M,succ,e)
assert not no_predecessor(succ,e)

M={'e','a','b'}; succ={'e':'a','a':'b','b':'a'}; e='e'
assert set(succ.keys())==M and no_predecessor(succ,e)
assert induction_axiom(M,succ,e)
assert not injective_on_defined(succ)

M={(n,i) for n in range(6) for i in (0,1)}
succ={(n,i):(n+1,i) for n in range(5) for i in (0,1)}
S={(n,0) for n in range(6)}
assert (0,0) in S and S != M
assert all((x not in succ or succ[x] in S) for x in S)

I=list(range(-20,21))
for idx,x in enumerate(I[:-1]):
    y=I[idx+1]
    assert x<y and not any(x<z<y for z in I)
for idx,x in enumerate(I[1:], start=1):
    y=I[idx-1]
    assert y<x and not any(y<z<x for z in I)

print('ALL V022 SANITY CHECKS PASS')
print('checked arithmetic/order identities, poset counterexamples, and Peano-independence models')
