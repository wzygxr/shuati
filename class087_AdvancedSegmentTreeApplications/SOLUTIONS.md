# 线段树题目详解与实现

## 1. HDU 5306 Gorgeous Sequence

### 题目描述
维护一个序列 a，执行以下操作：
1. 0 l r t: 对于所有的 i ∈ [l,r]，将 a[i] 变成 min(a[i], t)
2. 1 l r: 输出 max{a[i] | i ∈ [l,r]}
3. 2 l r: 输出 Σ{a[i] | i ∈ [l,r]}

### 解题思路
这是经典的吉司机线段树问题，核心思想是维护区间最大值、次大值和最大值个数，利用势能分析法保证时间复杂度。

### 关键点
1. 维护信息：最大值(mx)、严格次大值(sem)、最大值个数(cnt)、区间和(sum)
2. 三种更新情况：
   - 当更新值 >= 最大值时，无需更新
   - 当次大值 < 更新值 < 最大值时，直接更新最大值
   - 当更新值 <= 次大值时，递归处理左右子树

### Java实现
已在 [Code03_SegmentTreeSetminQueryMaxSum2.java](Code03_SegmentTreeSetminQueryMaxSum2.java) 中实现。

### C++实现
```cpp
#include <bits/stdc++.h>
using namespace std;

const int MAXN = 1000006;
const int INF = 0x3f3f3f3f;

struct Node {
    long long sum;
    int mx, sem, cnt;
} tree[MAXN << 2];

int arr[MAXN];
int n, m;

inline void push_up(int rt) {
    int l = rt << 1, r = rt << 1 | 1;
    tree[rt].sum = tree[l].sum + tree[r].sum;
    
    if (tree[l].mx > tree[r].mx) {
        tree[rt].mx = tree[l].mx;
        tree[rt].cnt = tree[l].cnt;
        tree[rt].sem = max(tree[l].sem, tree[r].mx);
    } else if (tree[l].mx < tree[r].mx) {
        tree[rt].mx = tree[r].mx;
        tree[rt].cnt = tree[r].cnt;
        tree[rt].sem = max(tree[l].mx, tree[r].sem);
    } else {
        tree[rt].mx = tree[l].mx;
        tree[rt].cnt = tree[l].cnt + tree[r].cnt;
        tree[rt].sem = max(tree[l].sem, tree[r].sem);
    }
}

inline void apply(int rt, int v) {
    if (v >= tree[rt].mx) return;
    tree[rt].sum -= (long long)(tree[rt].mx - v) * tree[rt].cnt;
    tree[rt].mx = v;
}

inline void push_down(int rt) {
    if (tree[rt].mx != INF) {
        apply(rt << 1, tree[rt].mx);
        apply(rt << 1 | 1, tree[rt].mx);
        tree[rt].mx = INF;
    }
}

void build(int l, int r, int rt) {
    tree[rt].mx = -1;
    tree[rt].sem = -1;
    tree[rt].cnt = 0;
    
    if (l == r) {
        scanf("%d", &tree[rt].mx);
        tree[rt].sum = tree[rt].mx;
        tree[rt].cnt = 1;
        return;
    }
    
    int mid = (l + r) >> 1;
    build(l, mid, rt << 1);
    build(mid + 1, r, rt << 1 | 1);
    push_up(rt);
}

void update(int L, int R, int v, int l, int r, int rt) {
    if (v >= tree[rt].mx) return;
    
    if (L <= l && r <= R && tree[rt].sem < v) {
        apply(rt, v);
        return;
    }
    
    // push_down(rt);
    int mid = (l + r) >> 1;
    if (L <= mid) update(L, R, v, l, mid, rt << 1);
    if (R > mid) update(L, R, v, mid + 1, r, rt << 1 | 1);
    push_up(rt);
}

int query_max(int L, int R, int l, int r, int rt) {
    if (L <= l && r <= R) {
        return tree[rt].mx;
    }
    
    // push_down(rt);
    int mid = (l + r) >> 1;
    int res = -INF;
    if (L <= mid) res = max(res, query_max(L, R, l, mid, rt << 1));
    if (R > mid) res = max(res, query_max(L, R, mid + 1, r, rt << 1 | 1));
    return res;
}

long long query_sum(int L, int R, int l, int r, int rt) {
    if (L <= l && r <= R) {
        return tree[rt].sum;
    }
    
    // push_down(rt);
    int mid = (l + r) >> 1;
    long long res = 0;
    if (L <= mid) res += query_sum(L, R, l, mid, rt << 1);
    if (R > mid) res += query_sum(L, R, mid + 1, r, rt << 1 | 1);
    return res;
}

int main() {
    int T;
    scanf("%d", &T);
    while (T--) {
        scanf("%d%d", &n, &m);
        build(1, n, 1);
        
        for (int i = 1; i <= m; i++) {
            int op, l, r, v;
            scanf("%d", &op);
            if (op == 0) {
                scanf("%d%d%d", &l, &r, &v);
                update(l, r, v, 1, n, 1);
            } else if (op == 1) {
                scanf("%d%d", &l, &r);
                printf("%d\n", query_max(l, r, 1, n, 1));
            } else {
                scanf("%d%d", &l, &r);
                printf("%lld\n", query_sum(l, r, 1, n, 1));
            }
        }
    }
    return 0;
}
```

### Python实现
```python
import sys
from math import inf

class SegmentTree:
    def __init__(self, arr):
        self.n = len(arr)
        self.tree = [{'sum': 0, 'mx': -1, 'sem': -1, 'cnt': 0} for _ in range(4 * self.n)]
        self.arr = arr
        self.build(1, 0, self.n - 1)
    
    def push_up(self, rt):
        l, r = 2 * rt, 2 * rt + 1
        self.tree[rt]['sum'] = self.tree[l]['sum'] + self.tree[r]['sum']
        
        if self.tree[l]['mx'] > self.tree[r]['mx']:
            self.tree[rt]['mx'] = self.tree[l]['mx']
            self.tree[rt]['cnt'] = self.tree[l]['cnt']
            self.tree[rt]['sem'] = max(self.tree[l]['sem'], self.tree[r]['mx'])
        elif self.tree[l]['mx'] < self.tree[r]['mx']:
            self.tree[rt]['mx'] = self.tree[r]['mx']
            self.tree[rt]['cnt'] = self.tree[r]['cnt']
            self.tree[rt]['sem'] = max(self.tree[l]['mx'], self.tree[r]['sem'])
        else:
            self.tree[rt]['mx'] = self.tree[l]['mx']
            self.tree[rt]['cnt'] = self.tree[l]['cnt'] + self.tree[r]['cnt']
            self.tree[rt]['sem'] = max(self.tree[l]['sem'], self.tree[r]['sem'])
    
    def apply(self, rt, v):
        if v >= self.tree[rt]['mx']:
            return
        self.tree[rt]['sum'] -= (self.tree[rt]['mx'] - v) * self.tree[rt]['cnt']
        self.tree[rt]['mx'] = v
    
    def build(self, rt, l, r):
        self.tree[rt]['mx'] = -1
        self.tree[rt]['sem'] = -1
        self.tree[rt]['cnt'] = 0
        
        if l == r:
            self.tree[rt]['mx'] = self.arr[l]
            self.tree[rt]['sum'] = self.arr[l]
            self.tree[rt]['cnt'] = 1
            return
        
        mid = (l + r) // 2
        self.build(2 * rt, l, mid)
        self.build(2 * rt + 1, mid + 1, r)
        self.push_up(rt)
    
    def update(self, L, R, v, l, r, rt):
        if v >= self.tree[rt]['mx']:
            return
        
        if L <= l and r <= R and self.tree[rt]['sem'] < v:
            self.apply(rt, v)
            return
        
        mid = (l + r) // 2
        if L <= mid:
            self.update(L, R, v, l, mid, 2 * rt)
        if R > mid:
            self.update(L, R, v, mid + 1, r, 2 * rt + 1)
        self.push_up(rt)
    
    def query_max(self, L, R, l, r, rt):
        if L <= l and r <= R:
            return self.tree[rt]['mx']
        
        mid = (l + r) // 2
        res = -inf
        if L <= mid:
            res = max(res, self.query_max(L, R, l, mid, 2 * rt))
        if R > mid:
            res = max(res, self.query_max(L, R, mid + 1, r, 2 * rt + 1))
        return res
    
    def query_sum(self, L, R, l, r, rt):
        if L <= l and r <= R:
            return self.tree[rt]['sum']
        
        mid = (l + r) // 2
        res = 0
        if L <= mid:
            res += self.query_sum(L, R, l, mid, 2 * rt)
        if R > mid:
            res += self.query_sum(L, R, mid + 1, r, 2 * rt + 1)
        return res

def main():
    input = sys.stdin.read
    data = input().split()
    
    idx = 0
    T = int(data[idx])
    idx += 1
    
    for _ in range(T):
        n = int(data[idx])
        m = int(data[idx + 1])
        idx += 2
        
        arr = [int(data[idx + i]) for i in range(n)]
        idx += n
        
        seg_tree = SegmentTree(arr)
        
        for _ in range(m):
            op = int(data[idx])
            idx += 1
            
            if op == 0:
                l = int(data[idx]) - 1
                r = int(data[idx + 1]) - 1
                v = int(data[idx + 2])
                idx += 3
                seg_tree.update(l, r, v, 0, n - 1, 1)
            elif op == 1:
                l = int(data[idx]) - 1
                r = int(data[idx + 1]) - 1
                idx += 2
                print(seg_tree.query_max(l, r, 0, n - 1, 1))
            else:
                l = int(data[idx]) - 1
                r = int(data[idx + 1]) - 1
                idx += 2
                print(seg_tree.query_sum(l, r, 0, n - 1, 1))

if __name__ == "__main__":
    main()
```

### 时间复杂度分析
- 建树: O(n)
- 区间最值操作: O(n log² n) 均摊
- 区间查询: O(log n)

### 空间复杂度分析
- O(n)

## 2. 洛谷 P6242 【模板】线段树 3

### 题目描述
给出一个长度为 n 的数列 A，同时定义一个辅助数组 B，B 开始与 A 完全相同。接下来进行 m 次操作：
1. 1 l r k: 对于所有的 i ∈ [l,r]，将 A[i] 加上 k（k 可以为负数）
2. 2 l r v: 对于所有的 i ∈ [l,r]，将 A[i] 变成 min(A[i], v)
3. 3 l r: 求 Σ{A[i] | i ∈ [l,r]}
4. 4 l r: 对于所有的 i ∈ [l,r]，求 A[i] 的最大值
5. 5 l r: 对于所有的 i ∈ [l,r]，求 B[i] 的最大值

在每一次操作后，都进行一次更新，让 B[i] ← max(B[i], A[i])

### 解题思路
这是区间加法、区间最值操作和历史最值查询的综合应用。需要维护多种信息和懒惰标记。

### 关键点
1. 维护信息：区间和(sum)、最大值(mx)、次大值(sem)、最大值个数(cnt)、历史最大值(max_history)
2. 懒惰标记：最大值增加量(max_add)、其他值增加量(other_add)、最大值历史最大增加量(max_add_top)、其他值历史最大增加量(other_add_top)
3. 操作2需要使用吉司机线段树的技术
4. 操作5需要维护历史最大值信息

### Java实现
已在 [Code05_MaximumMinimumHistory.java](Code05_MaximumMinimumHistory.java) 中实现。

### C++实现
```cpp
#include <bits/stdc++.h>
using namespace std;

const int MAXN = 500006;
const long long INF = 1e18;

struct Node {
    long long sum, mx, sem, max_history;
    long long max_add, other_add;
    long long max_add_top, other_add_top;
    int cnt;
} tree[MAXN << 2];

long long arr[MAXN];
int n, m;

inline void push_up(int rt) {
    int l = rt << 1, r = rt << 1 | 1;
    tree[rt].max_history = max(tree[l].max_history, tree[r].max_history);
    tree[rt].sum = tree[l].sum + tree[r].sum;
    
    if (tree[l].mx > tree[r].mx) {
        tree[rt].mx = tree[l].mx;
        tree[rt].cnt = tree[l].cnt;
        tree[rt].sem = max(tree[l].sem, tree[r].mx);
    } else if (tree[l].mx < tree[r].mx) {
        tree[rt].mx = tree[r].mx;
        tree[rt].cnt = tree[r].cnt;
        tree[rt].sem = max(tree[l].mx, tree[r].sem);
    } else {
        tree[rt].mx = tree[l].mx;
        tree[rt].cnt = tree[l].cnt + tree[r].cnt;
        tree[rt].sem = max(tree[l].sem, tree[r].sem);
    }
}

inline void apply(int rt, int len, long long max_add_v, long long other_add_v, 
                  long long max_up_v, long long other_up_v) {
    tree[rt].max_history = max(tree[rt].max_history, tree[rt].mx + max_up_v);
    tree[rt].max_add_top = max(tree[rt].max_add_top, tree[rt].max_add + max_up_v);
    tree[rt].other_add_top = max(tree[rt].other_add_top, tree[rt].other_add + other_up_v);
    
    tree[rt].sum += max_add_v * tree[rt].cnt + other_add_v * (len - tree[rt].cnt);
    tree[rt].mx += max_add_v;
    if (tree[rt].sem != -INF) tree[rt].sem += other_add_v;
    tree[rt].max_add += max_add_v;
    tree[rt].other_add += other_add_v;
}

inline void push_down(int rt, int ln, int rn) {
    int l = rt << 1, r = rt << 1 | 1;
    long long tmp = max(tree[l].mx, tree[r].mx);
    
    if (tree[l].mx == tmp) {
        apply(l, ln, tree[rt].max_add, tree[rt].other_add, 
              tree[rt].max_add_top, tree[rt].other_add_top);
    } else {
        apply(l, ln, tree[rt].other_add, tree[rt].other_add, 
              tree[rt].other_add_top, tree[rt].other_add_top);
    }
    
    if (tree[r].mx == tmp) {
        apply(r, rn, tree[rt].max_add, tree[rt].other_add, 
              tree[rt].max_add_top, tree[rt].other_add_top);
    } else {
        apply(r, rn, tree[rt].other_add, tree[rt].other_add, 
              tree[rt].other_add_top, tree[rt].other_add_top);
    }
    
    tree[rt].max_add = tree[rt].other_add = 0;
    tree[rt].max_add_top = tree[rt].other_add_top = 0;
}

void build(int l, int r, int rt) {
    tree[rt].max_add = tree[rt].other_add = 0;
    tree[rt].max_add_top = tree[rt].other_add_top = 0;
    
    if (l == r) {
        tree[rt].sum = tree[rt].mx = tree[rt].max_history = arr[l];
        tree[rt].sem = -INF;
        tree[rt].cnt = 1;
        return;
    }
    
    int mid = (l + r) >> 1;
    build(l, mid, rt << 1);
    build(mid + 1, r, rt << 1 | 1);
    push_up(rt);
}

void add(int L, int R, long long v, int l, int r, int rt) {
    if (L <= l && r <= R) {
        apply(rt, r - l + 1, v, v, v, v);
        return;
    }
    
    int mid = (l + r) >> 1;
    push_down(rt, mid - l + 1, r - mid);
    if (L <= mid) add(L, R, v, l, mid, rt << 1);
    if (R > mid) add(L, R, v, mid + 1, r, rt << 1 | 1);
    push_up(rt);
}

void set_min(int L, int R, long long v, int l, int r, int rt) {
    if (v >= tree[rt].mx) return;
    
    if (L <= l && r <= R && tree[rt].sem < v) {
        apply(rt, r - l + 1, v - tree[rt].mx, 0, v - tree[rt].mx, 0);
        return;
    }
    
    int mid = (l + r) >> 1;
    push_down(rt, mid - l + 1, r - mid);
    if (L <= mid) set_min(L, R, v, l, mid, rt << 1);
    if (R > mid) set_min(L, R, v, mid + 1, r, rt << 1 | 1);
    push_up(rt);
}

long long query_sum(int L, int R, int l, int r, int rt) {
    if (L <= l && r <= R) {
        return tree[rt].sum;
    }
    
    int mid = (l + r) >> 1;
    push_down(rt, mid - l + 1, r - mid);
    long long res = 0;
    if (L <= mid) res += query_sum(L, R, l, mid, rt << 1);
    if (R > mid) res += query_sum(L, R, mid + 1, r, rt << 1 | 1);
    return res;
}

long long query_max(int L, int R, int l, int r, int rt) {
    if (L <= l && r <= R) {
        return tree[rt].mx;
    }
    
    int mid = (l + r) >> 1;
    push_down(rt, mid - l + 1, r - mid);
    long long res = -INF;
    if (L <= mid) res = max(res, query_max(L, R, l, mid, rt << 1));
    if (R > mid) res = max(res, query_max(L, R, mid + 1, r, rt << 1 | 1));
    return res;
}

long long query_history_max(int L, int R, int l, int r, int rt) {
    if (L <= l && r <= R) {
        return tree[rt].max_history;
    }
    
    int mid = (l + r) >> 1;
    push_down(rt, mid - l + 1, r - mid);
    long long res = -INF;
    if (L <= mid) res = max(res, query_history_max(L, R, l, mid, rt << 1));
    if (R > mid) res = max(res, query_history_max(L, R, mid + 1, r, rt << 1 | 1));
    return res;
}

int main() {
    scanf("%d%d", &n, &m);
    for (int i = 1; i <= n; i++) {
        scanf("%lld", &arr[i]);
    }
    build(1, n, 1);
    
    for (int i = 1; i <= m; i++) {
        int op, l, r;
        long long v;
        scanf("%d%d%d", &op, &l, &r);
        
        if (op == 1) {
            scanf("%lld", &v);
            add(l, r, v, 1, n, 1);
        } else if (op == 2) {
            scanf("%lld", &v);
            set_min(l, r, v, 1, n, 1);
        } else if (op == 3) {
            printf("%lld\n", query_sum(l, r, 1, n, 1));
        } else if (op == 4) {
            printf("%lld\n", query_max(l, r, 1, n, 1));
        } else {
            printf("%lld\n", query_history_max(l, r, 1, n, 1));
        }
    }
    
    return 0;
}
```

### Python实现
```python
import sys
from math import inf

class SegmentTree:
    def __init__(self, arr):
        self.n = len(arr)
        self.tree = [None] * (4 * self.n)
        for i in range(4 * self.n):
            self.tree[i] = {
                'sum': 0, 'mx': 0, 'sem': 0, 'max_history': 0,
                'max_add': 0, 'other_add': 0,
                'max_add_top': 0, 'other_add_top': 0,
                'cnt': 0
            }
        self.arr = arr
        self.build(1, 0, self.n - 1)
    
    def push_up(self, rt):
        l, r = 2 * rt, 2 * rt + 1
        self.tree[rt]['max_history'] = max(self.tree[l]['max_history'], self.tree[r]['max_history'])
        self.tree[rt]['sum'] = self.tree[l]['sum'] + self.tree[r]['sum']
        
        if self.tree[l]['mx'] > self.tree[r]['mx']:
            self.tree[rt]['mx'] = self.tree[l]['mx']
            self.tree[rt]['cnt'] = self.tree[l]['cnt']
            self.tree[rt]['sem'] = max(self.tree[l]['sem'], self.tree[r]['mx'])
        elif self.tree[l]['mx'] < self.tree[r]['mx']:
            self.tree[rt]['mx'] = self.tree[r]['mx']
            self.tree[rt]['cnt'] = self.tree[r]['cnt']
            self.tree[rt]['sem'] = max(self.tree[l]['mx'], self.tree[r]['sem'])
        else:
            self.tree[rt]['mx'] = self.tree[l]['mx']
            self.tree[rt]['cnt'] = self.tree[l]['cnt'] + self.tree[r]['cnt']
            self.tree[rt]['sem'] = max(self.tree[l]['sem'], self.tree[r]['sem'])
    
    def apply(self, rt, length, max_add_v, other_add_v, max_up_v, other_up_v):
        self.tree[rt]['max_history'] = max(self.tree[rt]['max_history'], self.tree[rt]['mx'] + max_up_v)
        self.tree[rt]['max_add_top'] = max(self.tree[rt]['max_add_top'], self.tree[rt]['max_add'] + max_up_v)
        self.tree[rt]['other_add_top'] = max(self.tree[rt]['other_add_top'], self.tree[rt]['other_add'] + other_up_v)
        
        self.tree[rt]['sum'] += max_add_v * self.tree[rt]['cnt'] + other_add_v * (length - self.tree[rt]['cnt'])
        self.tree[rt]['mx'] += max_add_v
        if self.tree[rt]['sem'] != -inf:
            self.tree[rt]['sem'] += other_add_v
        self.tree[rt]['max_add'] += max_add_v
        self.tree[rt]['other_add'] += other_add_v
    
    def push_down(self, rt, ln, rn):
        l, r = 2 * rt, 2 * rt + 1
        tmp = max(self.tree[l]['mx'], self.tree[r]['mx'])
        
        if self.tree[l]['mx'] == tmp:
            self.apply(l, ln, self.tree[rt]['max_add'], self.tree[rt]['other_add'],
                       self.tree[rt]['max_add_top'], self.tree[rt]['other_add_top'])
        else:
            self.apply(l, ln, self.tree[rt]['other_add'], self.tree[rt]['other_add'],
                       self.tree[rt]['other_add_top'], self.tree[rt]['other_add_top'])
        
        if self.tree[r]['mx'] == tmp:
            self.apply(r, rn, self.tree[rt]['max_add'], self.tree[rt]['other_add'],
                       self.tree[rt]['max_add_top'], self.tree[rt]['other_add_top'])
        else:
            self.apply(r, rn, self.tree[rt]['other_add'], self.tree[rt]['other_add'],
                       self.tree[rt]['other_add_top'], self.tree[rt]['other_add_top'])
        
        self.tree[rt]['max_add'] = self.tree[rt]['other_add'] = 0
        self.tree[rt]['max_add_top'] = self.tree[rt]['other_add_top'] = 0
    
    def build(self, rt, l, r):
        self.tree[rt]['max_add'] = self.tree[rt]['other_add'] = 0
        self.tree[rt]['max_add_top'] = self.tree[rt]['other_add_top'] = 0
        
        if l == r:
            self.tree[rt]['sum'] = self.tree[rt]['mx'] = self.tree[rt]['max_history'] = self.arr[l]
            self.tree[rt]['sem'] = -inf
            self.tree[rt]['cnt'] = 1
            return
        
        mid = (l + r) // 2
        self.build(2 * rt, l, mid)
        self.build(2 * rt + 1, mid + 1, r)
        self.push_up(rt)
    
    def add(self, L, R, v, l, r, rt):
        if L <= l and r <= R:
            self.apply(rt, r - l + 1, v, v, v, v)
            return
        
        mid = (l + r) // 2
        self.push_down(rt, mid - l + 1, r - mid)
        if L <= mid:
            self.add(L, R, v, l, mid, 2 * rt)
        if R > mid:
            self.add(L, R, v, mid + 1, r, 2 * rt + 1)
        self.push_up(rt)
    
    def set_min(self, L, R, v, l, r, rt):
        if v >= self.tree[rt]['mx']:
            return
        
        if L <= l and r <= R and self.tree[rt]['sem'] < v:
            self.apply(rt, r - l + 1, v - self.tree[rt]['mx'], 0, v - self.tree[rt]['mx'], 0)
            return
        
        mid = (l + r) // 2
        self.push_down(rt, mid - l + 1, r - mid)
        if L <= mid:
            self.set_min(L, R, v, l, mid, 2 * rt)
        if R > mid:
            self.set_min(L, R, v, mid + 1, r, 2 * rt + 1)
        self.push_up(rt)
    
    def query_sum(self, L, R, l, r, rt):
        if L <= l and r <= R:
            return self.tree[rt]['sum']
        
        mid = (l + r) // 2
        self.push_down(rt, mid - l + 1, r - mid)
        res = 0
        if L <= mid:
            res += self.query_sum(L, R, l, mid, 2 * rt)
        if R > mid:
            res += self.query_sum(L, R, mid + 1, r, 2 * rt + 1)
        return res
    
    def query_max(self, L, R, l, r, rt):
        if L <= l and r <= R:
            return self.tree[rt]['mx']
        
        mid = (l + r) // 2
        self.push_down(rt, mid - l + 1, r - mid)
        res = -inf
        if L <= mid:
            res = max(res, self.query_max(L, R, l, mid, 2 * rt))
        if R > mid:
            res = max(res, self.query_max(L, R, mid + 1, r, 2 * rt + 1))
        return res
    
    def query_history_max(self, L, R, l, r, rt):
        if L <= l and r <= R:
            return self.tree[rt]['max_history']
        
        mid = (l + r) // 2
        self.push_down(rt, mid - l + 1, r - mid)
        res = -inf
        if L <= mid:
            res = max(res, self.query_history_max(L, R, l, mid, 2 * rt))
        if R > mid:
            res = max(res, self.query_history_max(L, R, mid + 1, r, 2 * rt + 1))
        return res

def main():
    input = sys.stdin.read
    data = input().split()
    
    idx = 0
    n = int(data[idx])
    m = int(data[idx + 1])
    idx += 2
    
    arr = [int(data[idx + i]) for i in range(n)]
    idx += n
    
    seg_tree = SegmentTree(arr)
    
    for _ in range(m):
        op = int(data[idx])
        l = int(data[idx + 1]) - 1  # 转换为0索引
        r = int(data[idx + 2]) - 1
        idx += 3
        
        if op == 1:
            v = int(data[idx])
            idx += 1
            seg_tree.add(l, r, v, 0, n - 1, 1)
        elif op == 2:
            v = int(data[idx])
            idx += 1
            seg_tree.set_min(l, r, v, 0, n - 1, 1)
        elif op == 3:
            print(seg_tree.query_sum(l, r, 0, n - 1, 1))
        elif op == 4:
            print(seg_tree.query_max(l, r, 0, n - 1, 1))
        else:  # op == 5
            print(seg_tree.query_history_max(l, r, 0, n - 1, 1))

if __name__ == "__main__":
    main()
```

### 时间复杂度分析
- 建树: O(n)
- 区间加法: O(log n)
- 区间最值操作: O(n log² n) 均摊
- 区间查询: O(log n)

### 空间复杂度分析
- O(n)

## 3. LeetCode 715. Range Module

### 题目描述
Range Module 是一个模块，用于跟踪数字范围。设计一个数据结构来高效地实现以下接口：
1. addRange(left, right): 添加半开区间 [left, right)
2. queryRange(left, right): 查询半开区间 [left, right) 是否完全被跟踪
3. removeRange(left, right): 移除半开区间 [left, right) 的跟踪

### 解题思路
使用动态开点线段树来维护区间覆盖状态。由于值域很大(10^9)，不能预先建立完整的线段树，需要按需创建节点。

### 关键点
1. 动态开点：只在需要时创建节点
2. 懒惰标记：维护区间覆盖状态
3. 区间操作：支持设置区间为覆盖或未覆盖状态

### Java实现
已在 [Code01_DynamicSegmentTree.java](Code01_DynamicSegmentTree.java) 和 [Code02_CountIntervals.java](Code02_CountIntervals.java) 中实现。

### C++实现
```cpp
#include <bits/stdc++.h>
using namespace std;

const int MAXN = 1000006;
const int INF = 1e9;

struct Node {
    int ls, rs;
    int sum, add;
} tree[MAXN];

int cnt = 1;
int root = 1;

inline void push_up(int rt) {
    tree[rt].sum = tree[tree[rt].ls].sum + tree[tree[rt].rs].sum;
}

inline void apply(int rt, int len, int v) {
    tree[rt].sum = v ? len : 0;
    tree[rt].add = v;
}

inline void push_down(int rt, int ln, int rn) {
    if (!tree[rt].add) return;
    
    if (!tree[rt].ls) tree[rt].ls = ++cnt;
    if (!tree[rt].rs) tree[rt].rs = ++cnt;
    
    apply(tree[rt].ls, ln, tree[rt].add);
    apply(tree[rt].rs, rn, tree[rt].add);
    tree[rt].add = 0;
}

void update(int &rt, int l, int r, int L, int R, int v) {
    if (!rt) rt = ++cnt;
    
    if (L <= l && r <= R) {
        apply(rt, r - l + 1, v);
        return;
    }
    
    int mid = (l + r) >> 1;
    push_down(rt, mid - l + 1, r - mid);
    
    if (L <= mid) update(tree[rt].ls, l, mid, L, R, v);
    if (R > mid) update(tree[rt].rs, mid + 1, r, L, R, v);
    
    push_up(rt);
}

int query(int rt, int l, int r, int L, int R) {
    if (!rt) return 0;
    
    if (L <= l && r <= R) {
        return tree[rt].sum;
    }
    
    int mid = (l + r) >> 1;
    push_down(rt, mid - l + 1, r - mid);
    
    int res = 0;
    if (L <= mid) res += query(tree[rt].ls, l, mid, L, R);
    if (R > mid) res += query(tree[rt].rs, mid + 1, r, L, R);
    
    return res;
}

class RangeModule {
private:
    int root;
    
public:
    RangeModule() {
        root = 0;
        cnt = 1;
        memset(tree, 0, sizeof(tree));
    }
    
    void addRange(int left, int right) {
        update(root, 1, INF, left, right - 1, 1);
    }
    
    bool queryRange(int left, int right) {
        return query(root, 1, INF, left, right - 1) == (right - left);
    }
    
    void removeRange(int left, int right) {
        update(root, 1, INF, left, right - 1, 0);
    }
};

int main() {
    RangeModule* obj = new RangeModule();
    obj->addRange(10, 20);
    obj->removeRange(14, 16);
    
    cout << obj->queryRange(10, 14) << endl; // 应该输出1(true)
    cout << obj->queryRange(13, 16) << endl; // 应该输出0(false)
    cout << obj->queryRange(16, 17) << endl; // 应该输出1(true)
    
    delete obj;
    return 0;
}
```

### Python实现
```python
class Node:
    def __init__(self):
        self.ls = self.rs = None
        self.sum = 0
        self.add = 0

class RangeModule:
    def __init__(self):
        self.root = Node()
        self.INF = int(1e9)
    
    def _push_up(self, node):
        node.sum = (node.ls.sum if node.ls else 0) + (node.rs.sum if node.rs else 0)
    
    def _apply(self, node, length, v):
        node.sum = length if v else 0
        node.add = v
    
    def _push_down(self, node, ln, rn):
        if not node.add:
            return
        
        if not node.ls:
            node.ls = Node()
        if not node.rs:
            node.rs = Node()
            
        self._apply(node.ls, ln, node.add)
        self._apply(node.rs, rn, node.add)
        node.add = 0
    
    def _update(self, node, l, r, L, R, v):
        if L <= l and r <= R:
            self._apply(node, r - l + 1, v)
            return
        
        mid = (l + r) >> 1
        self._push_down(node, mid - l + 1, r - mid)
        
        if L <= mid:
            if not node.ls:
                node.ls = Node()
            self._update(node.ls, l, mid, L, R, v)
        
        if R > mid:
            if not node.rs:
                node.rs = Node()
            self._update(node.rs, mid + 1, r, L, R, v)
        
        self._push_up(node)
    
    def _query(self, node, l, r, L, R):
        if not node:
            return 0
        
        if L <= l and r <= R:
            return node.sum
        
        mid = (l + r) >> 1
        self._push_down(node, mid - l + 1, r - mid)
        
        res = 0
        if L <= mid:
            res += self._query(node.ls, l, mid, L, R) if node.ls else 0
        if R > mid:
            res += self._query(node.rs, mid + 1, r, L, R) if node.rs else 0
        
        return res
    
    def addRange(self, left: int, right: int) -> None:
        self._update(self.root, 1, self.INF, left, right - 1, 1)
    
    def queryRange(self, left: int, right: int) -> bool:
        return self._query(self.root, 1, self.INF, left, right - 1) == (right - left)
    
    def removeRange(self, left: int, right: int) -> None:
        self._update(self.root, 1, self.INF, left, right - 1, 0)

# 测试
if __name__ == "__main__":
    rm = RangeModule()
    rm.addRange(10, 20)
    rm.removeRange(14, 16)
    
    print(rm.queryRange(10, 14))  # True
    print(rm.queryRange(13, 16))  # False
    print(rm.queryRange(16, 17))  # True
```

### 时间复杂度分析
- addRange: O(log INF)
- queryRange: O(log INF)
- removeRange: O(log INF)

### 空间复杂度分析
- O(q log INF)，其中 q 是操作次数

## 4. SPOJ GSS1 - Can you answer these queries I

### 题目描述
给定一个长度为n的整数序列，执行m次查询操作，每次查询[l,r]区间内的最大子段和。

### 解题思路
使用线段树维护区间信息，每个节点存储以下信息：
1. 区间最大子段和(maxSum)
2. 区间从左端点开始的最大子段和(lSum)
3. 区间到右端点结束的最大子段和(rSum)
4. 区间总和(sum)

### 关键点
1. 合并两个子区间的信息需要考虑三种情况：
   - 最大子段和在左子区间内
   - 最大子段和在右子区间内
   - 最大子段和跨越左右子区间（左子区间的右最大子段和 + 右子区间的左最大子段和）
2. 每个节点维护四个信息，通过push_up操作合并子节点信息

### Java实现
```java
// 详细实现见 Code06_MaximumSubarraySum.java
```

### C++实现
```cpp
// 详细实现见 Code06_MaximumSubarraySum.cpp
```

### Python实现
```python
# 详细实现见 Code06_MaximumSubarraySum.py
```

### 时间复杂度分析
- 建树: O(n)
- 查询: O(log n)
- 空间复杂度: O(n)

### 是否最优解
是，这是解决最大子段和区间查询问题的最优解法，时间复杂度为O(log n)

## 5. SPOJ KGSS - Maximum Sum

### 题目描述
给定一个长度为n的整数序列，执行m次操作：
1. U i x: 将第i个位置的值更新为x
2. Q l r: 查询[l,r]区间内两个最大值的和

### 解题思路
使用线段树维护区间信息，每个节点存储区间最大值和次大值。

### 关键点
1. 每个节点维护两个信息：最大值(max1)和次大值(max2)
2. 合并两个子区间的信息：
   - 区间最大值 = max(左区间max1, 右区间max1)
   - 区间次大值 = max(左区间max2, 右区间max2, min(左区间max1, 右区间max1))
3. 更新操作：单点更新，时间复杂度O(log n)
4. 查询操作：返回区间最大值与次大值之和

### Java实现
```java
// 详细实现见 Code07_MaximumTwoValuesSum.java
```

### C++实现
```cpp
// 详细实现见 Code07_MaximumTwoValuesSum.cpp
```

### Python实现
```python
# 详细实现见 Code07_MaximumTwoValuesSum.py
```

### 时间复杂度分析
- 建树: O(n)
- 更新: O(log n)
- 查询: O(log n)
- 空间复杂度: O(n)

### 是否最优解
是，这是解决区间两个最大值之和查询问题的最优解法，时间复杂度为O(log n)

## 6. POJ 2777 - Count Color

### 题目描述
给定一个长度为L的板条，初始时所有位置都是颜色1，执行O次操作：
1. "C A B C": 将区间[A,B]染成颜色C
2. "P A B": 查询区间[A,B]中有多少种不同的颜色

### 解题思路
使用线段树维护区间信息，每个节点存储区间颜色集合(用位运算表示)，结合懒惰标记实现区间染色。

### 关键点
1. 位运算优化：用一个整数的二进制位表示颜色集合，第i位为1表示有颜色i
2. 懒惰标记：延迟更新子区间，当区间被染成同一种颜色时打标记
3. 区间染色：将整个区间染成同一种颜色
4. 颜色计数：计算一个整数二进制表示中1的个数

### Java实现
```java
// 详细实现见 Code08_CountColor.java
```

### C++实现
```cpp
// 详细实现见 Code08_CountColor.cpp
```

### Python实现
```python
# 详细实现见 Code08_CountColor.py
```

### 时间复杂度分析
- 建树: O(L)
- 更新: O(log L)
- 查询: O(log L)
- 空间复杂度: O(L)

### 是否最优解
是，这是解决区间染色和颜色计数问题的最优解法，时间复杂度为O(log L)

## 总结

以上六道题目分别代表了线段树的六种重要应用：

1. **HDU 5306 Gorgeous Sequence** 展示了吉司机线段树处理区间最值操作的强大能力
2. **洛谷 P6242 【模板】线段树 3** 综合了多种线段树操作，包括区间加法、最值操作和历史信息维护
3. **LeetCode 715. Range Module** 展示了动态开点线段树在处理大数据范围时的优势
4. **SPOJ GSS1 - Can you answer these queries I** 展示了线段树在最大子段和问题中的应用
5. **SPOJ KGSS - Maximum Sum** 展示了线段树在维护最大值和次大值中的应用
6. **POJ 2777 - Count Color** 展示了线段树在区间染色和颜色计数中的应用

通过这些题目的学习和实践，可以深入理解线段树的各种高级应用，为进一步学习更复杂的数据结构和算法打下坚实基础。