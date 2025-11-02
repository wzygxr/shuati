# 线段树扩展题目与实现

## 1. POJ 3468 A Simple Problem with Integers

### 题目描述
给定一个长度为 N 的整数序列，执行以下操作：
1. C a b c: 将区间 [a,b] 中的每个数都加上 c
2. Q a b: 查询区间 [a,b] 中所有数的和

### 解题思路
这是线段树的经典应用，使用懒惰标记来优化区间更新操作。

### Java实现
```java
import java.io.*;
import java.util.*;

public class POJ3468 {
    static final int MAXN = 100005;
    static long[] sum = new long[MAXN << 2];
    static long[] add = new long[MAXN << 2];
    static int[] arr = new int[MAXN];
    static int n, m;
    
    static void pushUp(int rt) {
        sum[rt] = sum[rt << 1] + sum[rt << 1 | 1];
    }
    
    static void pushDown(int rt, int ln, int rn) {
        if (add[rt] != 0) {
            add[rt << 1] += add[rt];
            add[rt << 1 | 1] += add[rt];
            sum[rt << 1] += add[rt] * ln;
            sum[rt << 1 | 1] += add[rt] * rn;
            add[rt] = 0;
        }
    }
    
    static void build(int l, int r, int rt) {
        add[rt] = 0;
        if (l == r) {
            sum[rt] = arr[l];
            return;
        }
        int mid = (l + r) >> 1;
        build(l, mid, rt << 1);
        build(mid + 1, r, rt << 1 | 1);
        pushUp(rt);
    }
    
    static void update(int L, int R, long C, int l, int r, int rt) {
        if (L <= l && r <= R) {
            sum[rt] += C * (r - l + 1);
            add[rt] += C;
            return;
        }
        int mid = (l + r) >> 1;
        pushDown(rt, mid - l + 1, r - mid);
        if (L <= mid) update(L, R, C, l, mid, rt << 1);
        if (R > mid) update(L, R, C, mid + 1, r, rt << 1 | 1);
        pushUp(rt);
    }
    
    static long query(int L, int R, int l, int r, int rt) {
        if (L <= l && r <= R) {
            return sum[rt];
        }
        int mid = (l + r) >> 1;
        pushDown(rt, mid - l + 1, r - mid);
        long ans = 0;
        if (L <= mid) ans += query(L, R, l, mid, rt << 1);
        if (R > mid) ans += query(L, R, mid + 1, r, rt << 1 | 1);
        return ans;
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = new StringTokenizer(br.readLine());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        
        st = new StringTokenizer(br.readLine());
        for (int i = 1; i <= n; i++) {
            arr[i] = Integer.parseInt(st.nextToken());
        }
        
        build(1, n, 1);
        
        for (int i = 1; i <= m; i++) {
            st = new StringTokenizer(br.readLine());
            String op = st.nextToken();
            if (op.equals("C")) {
                int a = Integer.parseInt(st.nextToken());
                int b = Integer.parseInt(st.nextToken());
                long c = Long.parseLong(st.nextToken());
                update(a, b, c, 1, n, 1);
            } else {
                int a = Integer.parseInt(st.nextToken());
                int b = Integer.parseInt(st.nextToken());
                out.println(query(a, b, 1, n, 1));
            }
        }
        out.flush();
        out.close();
    }
}
```

### C++实现
```cpp
#include <cstdio>
#include <cstring>
using namespace std;

const int MAXN = 100005;
long long sum[MAXN << 2], add[MAXN << 2];
int arr[MAXN];
int n, m;

void pushUp(int rt) {
    sum[rt] = sum[rt << 1] + sum[rt << 1 | 1];
}

void pushDown(int rt, int ln, int rn) {
    if (add[rt]) {
        add[rt << 1] += add[rt];
        add[rt << 1 | 1] += add[rt];
        sum[rt << 1] += add[rt] * ln;
        sum[rt << 1 | 1] += add[rt] * rn;
        add[rt] = 0;
    }
}

void build(int l, int r, int rt) {
    add[rt] = 0;
    if (l == r) {
        scanf("%d", &arr[l]);
        sum[rt] = arr[l];
        return;
    }
    int mid = (l + r) >> 1;
    build(l, mid, rt << 1);
    build(mid + 1, r, rt << 1 | 1);
    pushUp(rt);
}

void update(int L, int R, long long C, int l, int r, int rt) {
    if (L <= l && r <= R) {
        sum[rt] += C * (r - l + 1);
        add[rt] += C;
        return;
    }
    int mid = (l + r) >> 1;
    pushDown(rt, mid - l + 1, r - mid);
    if (L <= mid) update(L, R, C, l, mid, rt << 1);
    if (R > mid) update(L, R, C, mid + 1, r, rt << 1 | 1);
    pushUp(rt);
}

long long query(int L, int R, int l, int r, int rt) {
    if (L <= l && r <= R) {
        return sum[rt];
    }
    int mid = (l + r) >> 1;
    pushDown(rt, mid - l + 1, r - mid);
    long long ans = 0;
    if (L <= mid) ans += query(L, R, l, mid, rt << 1);
    if (R > mid) ans += query(L, R, mid + 1, r, rt << 1 | 1);
    return ans;
}

int main() {
    scanf("%d%d", &n, &m);
    build(1, n, 1);
    
    for (int i = 1; i <= m; i++) {
        char op[2];
        scanf("%s", op);
        if (op[0] == 'C') {
            int a, b;
            long long c;
            scanf("%d%d%lld", &a, &b, &c);
            update(a, b, c, 1, n, 1);
        } else {
            int a, b;
            scanf("%d%d", &a, &b);
            printf("%lld\n", query(a, b, 1, n, 1));
        }
    }
    return 0;
}
```

### Python实现
```python
import sys

class SegmentTree:
    def __init__(self, arr):
        self.n = len(arr)
        self.sum = [0] * (4 * self.n)
        self.add = [0] * (4 * self.n)
        self.arr = arr
        self.build(1, 0, self.n - 1)
    
    def push_up(self, rt):
        self.sum[rt] = self.sum[2 * rt] + self.sum[2 * rt + 1]
    
    def push_down(self, rt, ln, rn):
        if self.add[rt] != 0:
            self.add[2 * rt] += self.add[rt]
            self.add[2 * rt + 1] += self.add[rt]
            self.sum[2 * rt] += self.add[rt] * ln
            self.sum[2 * rt + 1] += self.add[rt] * rn
            self.add[rt] = 0
    
    def build(self, rt, l, r):
        self.add[rt] = 0
        if l == r:
            self.sum[rt] = self.arr[l]
            return
        mid = (l + r) // 2
        self.build(2 * rt, l, mid)
        self.build(2 * rt + 1, mid + 1, r)
        self.push_up(rt)
    
    def update(self, L, R, C, l, r, rt):
        if L <= l and r <= R:
            self.sum[rt] += C * (r - l + 1)
            self.add[rt] += C
            return
        mid = (l + r) // 2
        self.push_down(rt, mid - l + 1, r - mid)
        if L <= mid:
            self.update(L, R, C, l, mid, 2 * rt)
        if R > mid:
            self.update(L, R, C, mid + 1, r, 2 * rt + 1)
        self.push_up(rt)
    
    def query(self, L, R, l, r, rt):
        if L <= l and r <= R:
            return self.sum[rt]
        mid = (l + r) // 2
        self.push_down(rt, mid - l + 1, r - mid)
        ans = 0
        if L <= mid:
            ans += self.query(L, R, l, mid, 2 * rt)
        if R > mid:
            ans += self.query(L, R, mid + 1, r, 2 * rt + 1)
        return ans

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
        op = data[idx]
        idx += 1
        
        if op == 'C':
            a = int(data[idx]) - 1  # 转换为0索引
            b = int(data[idx + 1]) - 1
            c = int(data[idx + 2])
            idx += 3
            seg_tree.update(a, b, c, 0, n - 1, 1)
        else:  # op == 'Q'
            a = int(data[idx]) - 1
            b = int(data[idx + 1]) - 1
            idx += 2
            print(seg_tree.query(a, b, 0, n - 1, 1))

if __name__ == "__main__":
    main()
```

## 2. POJ 2528 Mayor's posters

### 题目描述
城市的墙上贴海报，每张海报贴在一个连续区间上。后来贴的海报会覆盖之前贴的海报。求最后可以看到多少张不同的海报。

### 解题思路
这是一个区间染色问题，由于值域很大(10^7)，需要使用离散化技术。有两种处理方法：
1. 倒序处理：从后往前贴海报，只贴未被覆盖的部分
2. 离散化+线段树：将坐标离散化后用线段树维护区间颜色

### Java实现
```java
import java.io.*;
import java.util.*;

public class POJ2528 {
    static final int MAXN = 10005;
    static int[] li = new int[MAXN];
    static int[] ri = new int[MAXN];
    static int[] x = new int[MAXN * 6]; // 离散化数组
    static int[] sum = new int[MAXN * 24]; // 线段树
    static int[] lazy = new int[MAXN * 24]; // 懒惰标记
    static int n, tot;
    
    static void pushUp(int rt) {
        sum[rt] = sum[rt << 1] | sum[rt << 1 | 1];
    }
    
    static void pushDown(int rt) {
        if (lazy[rt] != 0) {
            lazy[rt << 1] = lazy[rt];
            lazy[rt << 1 | 1] = lazy[rt];
            sum[rt << 1] = lazy[rt];
            sum[rt << 1 | 1] = lazy[rt];
            lazy[rt] = 0;
        }
    }
    
    static void build(int l, int r, int rt) {
        lazy[rt] = 0;
        sum[rt] = 0;
        if (l == r) return;
        int mid = (l + r) >> 1;
        build(l, mid, rt << 1);
        build(mid + 1, r, rt << 1 | 1);
    }
    
    static void update(int L, int R, int C, int l, int r, int rt) {
        if (L <= l && r <= R) {
            sum[rt] = 1 << C;
            lazy[rt] = 1 << C;
            return;
        }
        pushDown(rt);
        int mid = (l + r) >> 1;
        if (L <= mid) update(L, R, C, l, mid, rt << 1);
        if (R > mid) update(L, R, C, mid + 1, r, rt << 1 | 1);
        pushUp(rt);
    }
    
    static int query(int L, int R, int l, int r, int rt) {
        if (L <= l && r <= R) {
            return sum[rt];
        }
        pushDown(rt);
        int mid = (l + r) >> 1;
        int ans = 0;
        if (L <= mid) ans |= query(L, R, l, mid, rt << 1);
        if (R > mid) ans |= query(L, R, mid + 1, r, rt << 1 | 1);
        return ans;
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        int T = Integer.parseInt(br.readLine());
        
        while (T-- > 0) {
            n = Integer.parseInt(br.readLine());
            tot = 0;
            
            // 读取区间并构建离散化数组
            for (int i = 1; i <= n; i++) {
                StringTokenizer st = new StringTokenizer(br.readLine());
                li[i] = Integer.parseInt(st.nextToken());
                ri[i] = Integer.parseInt(st.nextToken());
                x[++tot] = li[i];
                x[++tot] = ri[i];
            }
            
            // 离散化
            Arrays.sort(x, 1, tot + 1);
            int m = 1;
            for (int i = 2; i <= tot; i++) {
                if (x[i] != x[i - 1]) {
                    x[++m] = x[i];
                }
            }
            tot = m;
            
            // 添加额外点避免相邻区间合并错误
            m = tot;
            for (int i = 1; i < m; i++) {
                if (x[i + 1] - x[i] > 1) {
                    x[++tot] = x[i] + 1;
                }
            }
            
            Arrays.sort(x, 1, tot + 1);
            
            // 构建坐标映射
            HashMap<Integer, Integer> mp = new HashMap<>();
            for (int i = 1; i <= tot; i++) {
                mp.put(x[i], i);
            }
            
            // 建树并倒序处理
            build(1, tot, 1);
            for (int i = n; i >= 1; i--) {
                int l = mp.get(li[i]);
                int r = mp.get(ri[i]);
                update(l, r, i, 1, tot, 1);
            }
            
            // 统计可见海报数
            int result = query(1, tot, 1, tot, 1);
            int count = 0;
            for (int i = 1; i <= n; i++) {
                if ((result & (1 << i)) != 0) {
                    count++;
                }
            }
            out.println(count);
        }
        out.flush();
        out.close();
    }
}
```

### C++实现
```cpp
#include <cstdio>
#include <cstring>
#include <algorithm>
#include <map>
using namespace std;

const int MAXN = 10005;
int li[MAXN], ri[MAXN];
int x[MAXN * 6];
int sum[MAXN * 24], lazy[MAXN * 24];
int n, tot;

void pushUp(int rt) {
    sum[rt] = sum[rt << 1] | sum[rt << 1 | 1];
}

void pushDown(int rt) {
    if (lazy[rt]) {
        lazy[rt << 1] = lazy[rt];
        lazy[rt << 1 | 1] = lazy[rt];
        sum[rt << 1] = lazy[rt];
        sum[rt << 1 | 1] = lazy[rt];
        lazy[rt] = 0;
    }
}

void build(int l, int r, int rt) {
    lazy[rt] = 0;
    sum[rt] = 0;
    if (l == r) return;
    int mid = (l + r) >> 1;
    build(l, mid, rt << 1);
    build(mid + 1, r, rt << 1 | 1);
}

void update(int L, int R, int C, int l, int r, int rt) {
    if (L <= l && r <= R) {
        sum[rt] = 1 << C;
        lazy[rt] = 1 << C;
        return;
    }
    pushDown(rt);
    int mid = (l + r) >> 1;
    if (L <= mid) update(L, R, C, l, mid, rt << 1);
    if (R > mid) update(L, R, C, mid + 1, r, rt << 1 | 1);
    pushUp(rt);
}

int query(int L, int R, int l, int r, int rt) {
    if (L <= l && r <= R) {
        return sum[rt];
    }
    pushDown(rt);
    int mid = (l + r) >> 1;
    int ans = 0;
    if (L <= mid) ans |= query(L, R, l, mid, rt << 1);
    if (R > mid) ans |= query(L, R, mid + 1, r, rt << 1 | 1);
    return ans;
}

int main() {
    int T;
    scanf("%d", &T);
    
    while (T--) {
        scanf("%d", &n);
        tot = 0;
        
        // 读取区间并构建离散化数组
        for (int i = 1; i <= n; i++) {
            scanf("%d%d", &li[i], &ri[i]);
            x[++tot] = li[i];
            x[++tot] = ri[i];
        }
        
        // 离散化
        sort(x + 1, x + tot + 1);
        int m = 1;
        for (int i = 2; i <= tot; i++) {
            if (x[i] != x[i - 1]) {
                x[++m] = x[i];
            }
        }
        tot = m;
        
        // 添加额外点避免相邻区间合并错误
        m = tot;
        for (int i = 1; i < m; i++) {
            if (x[i + 1] - x[i] > 1) {
                x[++tot] = x[i] + 1;
            }
        }
        
        sort(x + 1, x + tot + 1);
        
        // 构建坐标映射
        map<int, int> mp;
        for (int i = 1; i <= tot; i++) {
            mp[x[i]] = i;
        }
        
        // 建树并倒序处理
        build(1, tot, 1);
        for (int i = n; i >= 1; i--) {
            int l = mp[li[i]];
            int r = mp[ri[i]];
            update(l, r, i, 1, tot, 1);
        }
        
        // 统计可见海报数
        int result = query(1, tot, 1, tot, 1);
        int count = 0;
        for (int i = 1; i <= n; i++) {
            if (result & (1 << i)) {
                count++;
            }
        }
        printf("%d\n", count);
    }
    return 0;
}
```

### Python实现
```python
import sys
from collections import defaultdict

class SegmentTree:
    def __init__(self, n):
        self.n = n
        self.sum = [0] * (4 * n)
        self.lazy = [0] * (4 * n)
    
    def push_up(self, rt):
        self.sum[rt] = self.sum[2 * rt] | self.sum[2 * rt + 1]
    
    def push_down(self, rt):
        if self.lazy[rt] != 0:
            self.lazy[2 * rt] = self.lazy[rt]
            self.lazy[2 * rt + 1] = self.lazy[rt]
            self.sum[2 * rt] = self.lazy[rt]
            self.sum[2 * rt + 1] = self.lazy[rt]
            self.lazy[rt] = 0
    
    def build(self, l, r, rt):
        self.lazy[rt] = 0
        self.sum[rt] = 0
        if l == r:
            return
        mid = (l + r) // 2
        self.build(l, mid, 2 * rt)
        self.build(mid + 1, r, 2 * rt + 1)
    
    def update(self, L, R, C, l, r, rt):
        if L <= l and r <= R:
            self.sum[rt] = 1 << C
            self.lazy[rt] = 1 << C
            return
        self.push_down(rt)
        mid = (l + r) // 2
        if L <= mid:
            self.update(L, R, C, l, mid, 2 * rt)
        if R > mid:
            self.update(L, R, C, mid + 1, r, 2 * rt + 1)
        self.push_up(rt)
    
    def query(self, L, R, l, r, rt):
        if L <= l and r <= R:
            return self.sum[rt]
        self.push_down(rt)
        mid = (l + r) // 2
        ans = 0
        if L <= mid:
            ans |= self.query(L, R, l, mid, 2 * rt)
        if R > mid:
            ans |= self.query(L, R, mid + 1, r, 2 * rt + 1)
        return ans

def main():
    input = sys.stdin.read
    data = input().split()
    
    idx = 0
    T = int(data[idx])
    idx += 1
    
    results = []
    
    for _ in range(T):
        n = int(data[idx])
        idx += 1
        
        li = [0] * (n + 1)
        ri = [0] * (n + 1)
        x = []
        
        # 读取区间并构建离散化数组
        for i in range(1, n + 1):
            li[i] = int(data[idx])
            ri[i] = int(data[idx + 1])
            idx += 2
            x.append(li[i])
            x.append(ri[i])
        
        # 离散化
        x = sorted(list(set(x)))
        tot = len(x)
        
        # 添加额外点避免相邻区间合并错误
        extra_x = []
        for i in range(tot - 1):
            if x[i + 1] - x[i] > 1:
                extra_x.append(x[i] + 1)
        x.extend(extra_x)
        x.sort()
        tot = len(x)
        
        # 构建坐标映射
        mp = {x[i]: i + 1 for i in range(tot)}
        
        # 建树并倒序处理
        seg_tree = SegmentTree(tot)
        seg_tree.build(1, tot, 1)
        for i in range(n, 0, -1):
            l = mp[li[i]]
            r = mp[ri[i]]
            seg_tree.update(l, r, i, 1, tot, 1)
        
        # 统计可见海报数
        result = seg_tree.query(1, tot, 1, tot, 1)
        count = 0
        for i in range(1, n + 1):
            if result & (1 << i):
                count += 1
        results.append(str(count))
    
    print('\n'.join(results))

if __name__ == "__main__":
    main()
```

## 3. Codeforces 438D The Child and Sequence

### 题目描述
维护一个序列，支持以下操作：
1. 1 l r: 查询区间 [l,r] 的最大值
2. 2 l r: 查询区间 [l,r] 的和
3. 3 l r x: 将区间 [l,r] 中的每个数对 x 取模

### 解题思路
这也是吉司机线段树的经典应用。当一个数对 x 取模后，如果这个数小于 x，则不会发生变化。我们可以维护最大值，当最大值小于模数时直接返回，否则递归处理。

### Java实现
```java
import java.io.*;
import java.util.*;

public class CF438D {
    static final int MAXN = 100005;
    static long[] sum = new long[MAXN << 2];
    static int[] max = new int[MAXN << 2];
    static int[] arr = new int[MAXN];
    static int n, m;
    
    static void pushUp(int rt) {
        sum[rt] = sum[rt << 1] + sum[rt << 1 | 1];
        max[rt] = Math.max(max[rt << 1], max[rt << 1 | 1]);
    }
    
    static void build(int l, int r, int rt) {
        if (l == r) {
            sum[rt] = max[rt] = arr[l];
            return;
        }
        int mid = (l + r) >> 1;
        build(l, mid, rt << 1);
        build(mid + 1, r, rt << 1 | 1);
        pushUp(rt);
    }
    
    static void update(int L, int R, int x, int l, int r, int rt) {
        if (max[rt] < x) return;
        if (l == r) {
            sum[rt] %= x;
            max[rt] %= x;
            return;
        }
        int mid = (l + r) >> 1;
        if (L <= mid) update(L, R, x, l, mid, rt << 1);
        if (R > mid) update(L, R, x, mid + 1, r, rt << 1 | 1);
        pushUp(rt);
    }
    
    static long querySum(int L, int R, int l, int r, int rt) {
        if (L <= l && r <= R) {
            return sum[rt];
        }
        int mid = (l + r) >> 1;
        long ans = 0;
        if (L <= mid) ans += querySum(L, R, l, mid, rt << 1);
        if (R > mid) ans += querySum(L, R, mid + 1, r, rt << 1 | 1);
        return ans;
    }
    
    static int queryMax(int L, int R, int l, int r, int rt) {
        if (L <= l && r <= R) {
            return max[rt];
        }
        int mid = (l + r) >> 1;
        int ans = 0;
        if (L <= mid) ans = Math.max(ans, queryMax(L, R, l, mid, rt << 1));
        if (R > mid) ans = Math.max(ans, queryMax(L, R, mid + 1, r, rt << 1 | 1));
        return ans;
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        StringTokenizer st = new StringTokenizer(br.readLine());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        
        st = new StringTokenizer(br.readLine());
        for (int i = 1; i <= n; i++) {
            arr[i] = Integer.parseInt(st.nextToken());
        }
        
        build(1, n, 1);
        
        for (int i = 1; i <= m; i++) {
            st = new StringTokenizer(br.readLine());
            int op = Integer.parseInt(st.nextToken());
            if (op == 1) {
                int l = Integer.parseInt(st.nextToken());
                int r = Integer.parseInt(st.nextToken());
                out.println(querySum(l, r, 1, n, 1));
            } else if (op == 2) {
                int l = Integer.parseInt(st.nextToken());
                int r = Integer.parseInt(st.nextToken());
                int x = Integer.parseInt(st.nextToken());
                update(l, r, x, 1, n, 1);
            } else {
                int l = Integer.parseInt(st.nextToken());
                int r = Integer.parseInt(st.nextToken());
                out.println(queryMax(l, r, 1, n, 1));
            }
        }
        out.flush();
        out.close();
    }
}
```

### C++实现
```cpp
#include <cstdio>
#include <algorithm>
using namespace std;

const int MAXN = 100005;
long long sum[MAXN << 2];
int max_val[MAXN << 2];
int arr[MAXN];
int n, m;

void pushUp(int rt) {
    sum[rt] = sum[rt << 1] + sum[rt << 1 | 1];
    max_val[rt] = max(max_val[rt << 1], max_val[rt << 1 | 1]);
}

void build(int l, int r, int rt) {
    if (l == r) {
        scanf("%d", &arr[l]);
        sum[rt] = max_val[rt] = arr[l];
        return;
    }
    int mid = (l + r) >> 1;
    build(l, mid, rt << 1);
    build(mid + 1, r, rt << 1 | 1);
    pushUp(rt);
}

void update(int L, int R, int x, int l, int r, int rt) {
    if (max_val[rt] < x) return;
    if (l == r) {
        sum[rt] %= x;
        max_val[rt] %= x;
        return;
    }
    int mid = (l + r) >> 1;
    if (L <= mid) update(L, R, x, l, mid, rt << 1);
    if (R > mid) update(L, R, x, mid + 1, r, rt << 1 | 1);
    pushUp(rt);
}

long long querySum(int L, int R, int l, int r, int rt) {
    if (L <= l && r <= R) {
        return sum[rt];
    }
    int mid = (l + r) >> 1;
    long long ans = 0;
    if (L <= mid) ans += querySum(L, R, l, mid, rt << 1);
    if (R > mid) ans += querySum(L, R, mid + 1, r, rt << 1 | 1);
    return ans;
}

int queryMax(int L, int R, int l, int r, int rt) {
    if (L <= l && r <= R) {
        return max_val[rt];
    }
    int mid = (l + r) >> 1;
    int ans = 0;
    if (L <= mid) ans = max(ans, queryMax(L, R, l, mid, rt << 1));
    if (R > mid) ans = max(ans, queryMax(L, R, mid + 1, r, rt << 1 | 1));
    return ans;
}

int main() {
    scanf("%d%d", &n, &m);
    build(1, n, 1);
    
    for (int i = 1; i <= m; i++) {
        int op;
        scanf("%d", &op);
        if (op == 1) {
            int l, r;
            scanf("%d%d", &l, &r);
            printf("%lld\n", querySum(l, r, 1, n, 1));
        } else if (op == 2) {
            int l, r, x;
            scanf("%d%d%d", &l, &r, &x);
            update(l, r, x, 1, n, 1);
        } else {
            int l, r;
            scanf("%d%d", &l, &r);
            printf("%d\n", queryMax(l, r, 1, n, 1));
        }
    }
    return 0;
}
```

### Python实现
```python
import sys

class SegmentTree:
    def __init__(self, arr):
        self.n = len(arr)
        self.sum = [0] * (4 * self.n)
        self.max_val = [0] * (4 * self.n)
        self.arr = arr
        self.build(1, 0, self.n - 1)
    
    def push_up(self, rt):
        self.sum[rt] = self.sum[2 * rt] + self.sum[2 * rt + 1]
        self.max_val[rt] = max(self.max_val[2 * rt], self.max_val[2 * rt + 1])
    
    def build(self, rt, l, r):
        if l == r:
            self.sum[rt] = self.max_val[rt] = self.arr[l]
            return
        mid = (l + r) // 2
        self.build(2 * rt, l, mid)
        self.build(2 * rt + 1, mid + 1, r)
        self.push_up(rt)
    
    def update(self, L, R, x, l, r, rt):
        if self.max_val[rt] < x:
            return
        if l == r:
            self.sum[rt] %= x
            self.max_val[rt] %= x
            return
        mid = (l + r) // 2
        if L <= mid:
            self.update(L, R, x, l, mid, 2 * rt)
        if R > mid:
            self.update(L, R, x, mid + 1, r, 2 * rt + 1)
        self.push_up(rt)
    
    def query_sum(self, L, R, l, r, rt):
        if L <= l and r <= R:
            return self.sum[rt]
        mid = (l + r) // 2
        ans = 0
        if L <= mid:
            ans += self.query_sum(L, R, l, mid, 2 * rt)
        if R > mid:
            ans += self.query_sum(L, R, mid + 1, r, 2 * rt + 1)
        return ans
    
    def query_max(self, L, R, l, r, rt):
        if L <= l and r <= R:
            return self.max_val[rt]
        mid = (l + r) // 2
        ans = 0
        if L <= mid:
            ans = max(ans, self.query_max(L, R, l, mid, 2 * rt))
        if R > mid:
            ans = max(ans, self.query_max(L, R, mid + 1, r, 2 * rt + 1))
        return ans

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
    
    results = []
    for _ in range(m):
        op = int(data[idx])
        idx += 1
        
        if op == 1:
            l = int(data[idx]) - 1
            r = int(data[idx + 1]) - 1
            idx += 2
            results.append(str(seg_tree.query_sum(l, r, 0, n - 1, 1)))
        elif op == 2:
            l = int(data[idx]) - 1
            r = int(data[idx + 1]) - 1
            x = int(data[idx + 2])
            idx += 3
            seg_tree.update(l, r, x, 0, n - 1, 1)
        else:  # op == 3
            l = int(data[idx]) - 1
            r = int(data[idx + 1]) - 1
            idx += 2
            results.append(str(seg_tree.query_max(l, r, 0, n - 1, 1)))
    
    print('\n'.join(results))

if __name__ == "__main__":
    main()
```

## 4. SPOJ GSS1 - Can you answer these queries I

### 题目描述
给定一个长度为n的整数序列，执行m次查询操作，每次查询[l,r]区间内的最大子段和。

### 解题思路
使用线段树维护区间信息，每个节点存储以下信息：
1. 区间最大子段和(maxSum)
2. 区间从左端点开始的最大子段和(lSum)
3. 区间到右端点结束的最大子段和(rSum)
4. 区间总和(sum)

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

## 5. LeetCode 731. 我的日程安排表 II

### 题目描述
设计一个日程类 MyCalendar，包含以下功能：有一个 book(int start, int end)方法。它意味着在 start 到 end 时间内增加一个日程安排，注意，这里的时间是半开区间，即 [start, end)。当三个日程安排有一些时间上的交叉时（例如三个日程安排都在同一时间内），就会产生三重预订。每次调用 book 方法会产生一个日程，如果发生了三重预定则返回 false，并取消该日程，否则添加该日程。

### 解题思路
这是动态开点线段树的经典应用场景，因为时间范围可以达到 10^9，无法使用普通线段树。我们需要：
1. 使用动态开点线段树维护每个时间点的日程数量
2. 每次查询区间内的最大值，判断是否会导致三重预订
3. 如果不会，则更新区间

### Java实现
```java
import java.util.*;

class MyCalendarTwo {
    // 动态开点线段树节点定义
    static class Node {
        int max;      // 当前区间的最大预订数
        int lazy;     // 懒惰标记，表示待下传的增量
        Node left;    // 左子节点
        Node right;   // 右子节点
    }
    
    private Node root;  // 线段树根节点
    private final int MAX_RANGE = 1_000_000_000;  // 最大时间范围
    
    public MyCalendarTwo() {
        root = new Node();
    }
    
    // 区间更新：将 [L, R) 区间内的所有值加上 val
    private void update(Node node, int start, int end, int L, int R, int val) {
        // 如果当前区间完全包含在目标区间内
        if (L <= start && end <= R) {
            node.max += val;
            node.lazy += val;
            return;
        }
        
        // 下传懒惰标记
        pushDown(node);
        
        int mid = start + (end - start) / 2;
        // 更新左子区间
        if (L <= mid) {
            if (node.left == null) node.left = new Node();
            update(node.left, start, mid, L, R, val);
        }
        // 更新右子区间
        if (R > mid) {
            if (node.right == null) node.right = new Node();
            update(node.right, mid + 1, end, L, R, val);
        }
        
        // 上传信息
        node.max = Math.max(
            (node.left != null ? node.left.max : 0),
            (node.right != null ? node.right.max : 0)
        );
    }
    
    // 查询区间 [L, R) 内的最大值
    private int query(Node node, int start, int end, int L, int R) {
        // 如果当前节点为空，返回0
        if (node == null) return 0;
        
        // 如果当前区间完全包含在目标区间内
        if (L <= start && end <= R) {
            return node.max;
        }
        
        // 下传懒惰标记
        pushDown(node);
        
        int mid = start + (end - start) / 2;
        int maxVal = 0;
        
        // 查询左子区间
        if (L <= mid) {
            maxVal = Math.max(maxVal, query(node.left, start, mid, L, R));
        }
        // 查询右子区间
        if (R > mid) {
            maxVal = Math.max(maxVal, query(node.right, mid + 1, end, L, R));
        }
        
        return maxVal;
    }
    
    // 下传懒惰标记
    private void pushDown(Node node) {
        if (node.lazy != 0) {
            // 为左子节点创建并传递标记
            if (node.left == null) node.left = new Node();
            node.left.max += node.lazy;
            node.left.lazy += node.lazy;
            
            // 为右子节点创建并传递标记
            if (node.right == null) node.right = new Node();
            node.right.max += node.lazy;
            node.right.lazy += node.lazy;
            
            // 清除当前节点的懒惰标记
            node.lazy = 0;
        }
    }
    
    public boolean book(int start, int end) {
        // 先查询当前区间的最大预订数
        int currentMax = query(root, 0, MAX_RANGE, start, end - 1);
        
        // 如果添加后会超过2（即出现三重预订），则返回false
        if (currentMax >= 2) {
            return false;
        }
        
        // 否则，更新区间，增加预订数
        update(root, 0, MAX_RANGE, start, end - 1, 1);
        return true;
    }
}

// 测试代码
class Main {
    public static void main(String[] args) {
        MyCalendarTwo myCalendar = new MyCalendarTwo();
        System.out.println(myCalendar.book(10, 20)); // 返回 true
        System.out.println(myCalendar.book(50, 60)); // 返回 true
        System.out.println(myCalendar.book(10, 40)); // 返回 true
        System.out.println(myCalendar.book(5, 15));  // 返回 false
        System.out.println(myCalendar.book(5, 10));  // 返回 true
        System.out.println(myCalendar.book(25, 55)); // 返回 true
    }
}
```

### C++实现
```cpp
#include <iostream>
#include <algorithm>
using namespace std;

class MyCalendarTwo {
private:
    // 动态开点线段树节点定义
    struct Node {
        int maxVal;     // 当前区间的最大预订数
        int lazy;       // 懒惰标记，表示待下传的增量
        Node* left;     // 左子节点
        Node* right;    // 右子节点
        
        Node() : maxVal(0), lazy(0), left(nullptr), right(nullptr) {}
    };
    
    Node* root;       // 线段树根节点
    const int MAX_RANGE = 1e9;  // 最大时间范围
    
    // 区间更新：将 [L, R] 区间内的所有值加上 val
    void update(Node* &node, int start, int end, int L, int R, int val) {
        if (!node) node = new Node();
        
        // 如果当前区间完全包含在目标区间内
        if (L <= start && end <= R) {
            node->maxVal += val;
            node->lazy += val;
            return;
        }
        
        // 下传懒惰标记
        pushDown(node);
        
        int mid = start + (end - start) / 2;
        // 更新左子区间
        if (L <= mid) {
            update(node->left, start, mid, L, R, val);
        }
        // 更新右子区间
        if (R > mid) {
            update(node->right, mid + 1, end, L, R, val);
        }
        
        // 上传信息
        node->maxVal = max(
            (node->left ? node->left->maxVal : 0),
            (node->right ? node->right->maxVal : 0)
        );
    }
    
    // 查询区间 [L, R] 内的最大值
    int query(Node* node, int start, int end, int L, int R) {
        if (!node) return 0;
        
        // 如果当前区间完全包含在目标区间内
        if (L <= start && end <= R) {
            return node->maxVal;
        }
        
        // 下传懒惰标记
        pushDown(node);
        
        int mid = start + (end - start) / 2;
        int maxVal = 0;
        
        // 查询左子区间
        if (L <= mid) {
            maxVal = max(maxVal, query(node->left, start, mid, L, R));
        }
        // 查询右子区间
        if (R > mid) {
            maxVal = max(maxVal, query(node->right, mid + 1, end, L, R));
        }
        
        return maxVal;
    }
    
    // 下传懒惰标记
    void pushDown(Node* node) {
        if (node->lazy != 0) {
            // 为左子节点创建并传递标记
            if (!node->left) node->left = new Node();
            node->left->maxVal += node->lazy;
            node->left->lazy += node->lazy;
            
            // 为右子节点创建并传递标记
            if (!node->right) node->right = new Node();
            node->right->maxVal += node->lazy;
            node->right->lazy += node->lazy;
            
            // 清除当前节点的懒惰标记
            node->lazy = 0;
        }
    }
    
    // 释放节点内存
    void freeTree(Node* node) {
        if (node) {
            freeTree(node->left);
            freeTree(node->right);
            delete node;
        }
    }
    
public:
    MyCalendarTwo() {
        root = new Node();
    }
    
    ~MyCalendarTwo() {
        freeTree(root);
    }
    
    bool book(int start, int end) {
        // 先查询当前区间的最大预订数
        int currentMax = query(root, 0, MAX_RANGE, start, end - 1);
        
        // 如果添加后会超过2（即出现三重预订），则返回false
        if (currentMax >= 2) {
            return false;
        }
        
        // 否则，更新区间，增加预订数
        update(root, 0, MAX_RANGE, start, end - 1, 1);
        return true;
    }
};

// 测试代码
int main() {
    MyCalendarTwo myCalendar;
    cout << boolalpha << myCalendar.book(10, 20) << endl; // 输出 true
    cout << boolalpha << myCalendar.book(50, 60) << endl; // 输出 true
    cout << boolalpha << myCalendar.book(10, 40) << endl; // 输出 true
    cout << boolalpha << myCalendar.book(5, 15) << endl;  // 输出 false
    cout << boolalpha << myCalendar.book(5, 10) << endl;  // 输出 true
    cout << boolalpha << myCalendar.book(25, 55) << endl; // 输出 true
    return 0;
}
```

### Python实现
```python
class MyCalendarTwo:
    # 动态开点线段树节点定义
    class Node:
        def __init__(self):
            self.max_val = 0  # 当前区间的最大预订数
            self.lazy = 0     # 懒惰标记，表示待下传的增量
            self.left = None  # 左子节点
            self.right = None # 右子节点
    
    def __init__(self):
        self.root = self.Node()
        self.MAX_RANGE = 10**9  # 最大时间范围
    
    # 区间更新：将 [L, R] 区间内的所有值加上 val
    def _update(self, node, start, end, L, R, val):
        if not node:
            node = self.Node()
        
        # 如果当前区间完全包含在目标区间内
        if L <= start and end <= R:
            node.max_val += val
            node.lazy += val
            return node
        
        # 下传懒惰标记
        self._push_down(node)
        
        mid = start + (end - start) // 2
        # 更新左子区间
        if L <= mid:
            if not node.left:
                node.left = self.Node()
            node.left = self._update(node.left, start, mid, L, R, val)
        # 更新右子区间
        if R > mid:
            if not node.right:
                node.right = self.Node()
            node.right = self._update(node.right, mid + 1, end, L, R, val)
        
        # 上传信息
        node.max_val = max(
            node.left.max_val if node.left else 0,
            node.right.max_val if node.right else 0
        )
        return node
    
    # 查询区间 [L, R] 内的最大值
    def _query(self, node, start, end, L, R):
        if not node:
            return 0
        
        # 如果当前区间完全包含在目标区间内
        if L <= start and end <= R:
            return node.max_val
        
        # 下传懒惰标记
        self._push_down(node)
        
        mid = start + (end - start) // 2
        max_val = 0
        
        # 查询左子区间
        if L <= mid:
            max_val = max(max_val, self._query(node.left, start, mid, L, R))
        # 查询右子区间
        if R > mid:
            max_val = max(max_val, self._query(node.right, mid + 1, end, L, R))
        
        return max_val
    
    # 下传懒惰标记
    def _push_down(self, node):
        if node.lazy != 0:
            # 为左子节点创建并传递标记
            if not node.left:
                node.left = self.Node()
            node.left.max_val += node.lazy
            node.left.lazy += node.lazy
            
            # 为右子节点创建并传递标记
            if not node.right:
                node.right = self.Node()
            node.right.max_val += node.lazy
            node.right.lazy += node.lazy
            
            # 清除当前节点的懒惰标记
            node.lazy = 0
    
    def book(self, start: int, end: int) -> bool:
        # 先查询当前区间的最大预订数
        current_max = self._query(self.root, 0, self.MAX_RANGE, start, end - 1)
        
        # 如果添加后会超过2（即出现三重预订），则返回false
        if current_max >= 2:
            return False
        
        # 否则，更新区间，增加预订数
        self.root = self._update(self.root, 0, self.MAX_RANGE, start, end - 1, 1)
        return True

# 测试代码
if __name__ == "__main__":
    my_calendar = MyCalendarTwo()
    print(my_calendar.book(10, 20)) # 输出 True
    print(my_calendar.book(50, 60)) # 输出 True
    print(my_calendar.book(10, 40)) # 输出 True
    print(my_calendar.book(5, 15))  # 输出 False
    print(my_calendar.book(5, 10))  # 输出 True
    print(my_calendar.book(25, 55)) # 输出 True
```

## 6. LeetCode 699. 掉落的方块

### 题目描述
在二维平面上的 x 轴上，放置着一些方块。给你一个二维整数数组 positions，其中 positions[i] = [lefti, sideLengthi] 表示：第 i 个方块边长为 sideLengthi，其左侧边与 x 轴上坐标点 lefti 对齐。每个方块都从一个比目前所有的落地方块更高的高度掉落而下。方块沿 y 轴负方向下落，直到着陆到另一个正方形的顶边或者是 x 轴上。一旦着陆，它就会固定在原地，无法移动。在每个方块掉落后，你必须记录目前所有已经落稳的方块堆叠的最高高度。返回一个整数数组 ans，其中 ans[i] 表示在第 i 块方块掉落后堆叠的最高高度。

### 解题思路
这是动态开点线段树的另一个经典应用。我们需要：
1. 对于每个方块，确定其覆盖的区间 [left, left + sideLength)
2. 查询该区间当前的最大高度，这将是新方块的底部高度
3. 新方块的顶部高度为底部高度 + sideLength
4. 更新该区间的高度为新方块的顶部高度
5. 记录当前全局的最大高度

### Java实现
```java
import java.util.*;

class Solution {
    // 动态开点线段树节点定义
    static class Node {
        int max;      // 当前区间的最大高度
        int lazy;     // 懒惰标记，表示待下传的更新值
        Node left;    // 左子节点
        Node right;   // 右子节点
    }
    
    // 区间更新（将区间设置为某个值）
    private void update(Node node, int start, int end, int L, int R, int val) {
        // 如果当前节点为空，创建新节点
        if (node == null) {
            node = new Node();
        }
        
        // 如果当前区间完全包含在目标区间内，且新值大于当前最大值，则更新
        if (L <= start && end <= R && val > node.max) {
            node.max = val;
            node.lazy = val;
            return;
        }
        
        // 下传懒惰标记
        pushDown(node);
        
        int mid = start + (end - start) / 2;
        // 更新左子区间
        if (L <= mid) {
            if (node.left == null) node.left = new Node();
            update(node.left, start, mid, L, R, val);
        }
        // 更新右子区间
        if (R > mid) {
            if (node.right == null) node.right = new Node();
            update(node.right, mid + 1, end, L, R, val);
        }
        
        // 上传信息
        node.max = Math.max(
            (node.left != null ? node.left.max : 0),
            (node.right != null ? node.right.max : 0)
        );
    }
    
    // 查询区间 [L, R] 内的最大值
    private int query(Node node, int start, int end, int L, int R) {
        // 如果当前节点为空，返回0
        if (node == null) return 0;
        
        // 如果当前区间完全包含在目标区间内
        if (L <= start && end <= R) {
            return node.max;
        }
        
        // 下传懒惰标记
        pushDown(node);
        
        int mid = start + (end - start) / 2;
        int maxVal = 0;
        
        // 查询左子区间
        if (L <= mid) {
            maxVal = Math.max(maxVal, query(node.left, start, mid, L, R));
        }
        // 查询右子区间
        if (R > mid) {
            maxVal = Math.max(maxVal, query(node.right, mid + 1, end, L, R));
        }
        
        return maxVal;
    }
    
    // 下传懒惰标记
    private void pushDown(Node node) {
        if (node.lazy != 0) {
            // 为左子节点创建并传递标记
            if (node.left == null) node.left = new Node();
            if (node.lazy > node.left.max) {
                node.left.max = node.lazy;
                node.left.lazy = node.lazy;
            }
            
            // 为右子节点创建并传递标记
            if (node.right == null) node.right = new Node();
            if (node.lazy > node.right.max) {
                node.right.max = node.lazy;
                node.right.lazy = node.lazy;
            }
            
            // 清除当前节点的懒惰标记
            node.lazy = 0;
        }
    }
    
    public List<Integer> fallingSquares(int[][] positions) {
        Node root = new Node();
        List<Integer> result = new ArrayList<>();
        int maxHeight = 0;
        
        // 确定坐标范围，进行离散化（可选优化）
        // 这里为了简化，使用较大的范围
        int MAX_RANGE = 1_000_000_000;
        
        for (int[] pos : positions) {
            int left = pos[0];
            int sideLength = pos[1];
            int right = left + sideLength - 1;
            
            // 查询当前区间的最大高度
            int currentMax = query(root, 0, MAX_RANGE, left, right);
            
            // 计算新方块的顶部高度
            int newHeight = currentMax + sideLength;
            
            // 更新区间高度
            update(root, 0, MAX_RANGE, left, right, newHeight);
            
            // 更新全局最大高度
            maxHeight = Math.max(maxHeight, newHeight);
            result.add(maxHeight);
        }
        
        return result;
    }
}

// 测试代码
class Main {
    public static void main(String[] args) {
        Solution solution = new Solution();
        
        // 测试用例1
        int[][] positions1 = {{1, 2}, {2, 3}, {6, 1}};
        System.out.println(solution.fallingSquares(positions1)); // 输出 [2, 5, 5]
        
        // 测试用例2
        int[][] positions2 = {{100, 100}, {200, 100}};
        System.out.println(solution.fallingSquares(positions2)); // 输出 [100, 100]
    }
}
```

### C++实现
```cpp
#include <iostream>
#include <vector>
#include <algorithm>
using namespace std;

class Solution {
private:
    // 动态开点线段树节点定义
    struct Node {
        int maxHeight;  // 当前区间的最大高度
        int lazy;       // 懒惰标记，表示待下传的更新值
        Node* left;     // 左子节点
        Node* right;    // 右子节点
        
        Node() : maxHeight(0), lazy(0), left(nullptr), right(nullptr) {}
    };
    
    // 区间更新（将区间设置为某个值，但只在新值更大时更新）
    void update(Node* &node, int start, int end, int L, int R, int val) {
        if (!node) node = new Node();
        
        // 如果当前区间完全包含在目标区间内，且新值大于当前最大值，则更新
        if (L <= start && end <= R && val > node->maxHeight) {
            node->maxHeight = val;
            node->lazy = val;
            return;
        }
        
        // 下传懒惰标记
        pushDown(node);
        
        int mid = start + (end - start) / 2;
        // 更新左子区间
        if (L <= mid) {
            update(node->left, start, mid, L, R, val);
        }
        // 更新右子区间
        if (R > mid) {
            update(node->right, mid + 1, end, L, R, val);
        }
        
        // 上传信息
        node->maxHeight = max(
            (node->left ? node->left->maxHeight : 0),
            (node->right ? node->right->maxHeight : 0)
        );
    }
    
    // 查询区间 [L, R] 内的最大值
    int query(Node* node, int start, int end, int L, int R) {
        if (!node) return 0;
        
        // 如果当前区间完全包含在目标区间内
        if (L <= start && end <= R) {
            return node->maxHeight;
        }
        
        // 下传懒惰标记
        pushDown(node);
        
        int mid = start + (end - start) / 2;
        int maxVal = 0;
        
        // 查询左子区间
        if (L <= mid) {
            maxVal = max(maxVal, query(node->left, start, mid, L, R));
        }
        // 查询右子区间
        if (R > mid) {
            maxVal = max(maxVal, query(node->right, mid + 1, end, L, R));
        }
        
        return maxVal;
    }
    
    // 下传懒惰标记
    void pushDown(Node* node) {
        if (node->lazy != 0) {
            // 为左子节点创建并传递标记
            if (!node->left) node->left = new Node();
            if (node->lazy > node->left->maxHeight) {
                node->left->maxHeight = node->lazy;
                node->left->lazy = node->lazy;
            }
            
            // 为右子节点创建并传递标记
            if (!node->right) node->right = new Node();
            if (node->lazy > node->right->maxHeight) {
                node->right->maxHeight = node->lazy;
                node->right->lazy = node->lazy;
            }
            
            // 清除当前节点的懒惰标记
            node->lazy = 0;
        }
    }
    
    // 释放节点内存
    void freeTree(Node* node) {
        if (node) {
            freeTree(node->left);
            freeTree(node->right);
            delete node;
        }
    }
    
public:
    vector<int> fallingSquares(vector<vector<int>>& positions) {
        Node* root = new Node();
        vector<int> result;
        int maxHeight = 0;
        
        // 确定坐标范围
        const int MAX_RANGE = 1e9;
        
        for (auto& pos : positions) {
            int left = pos[0];
            int sideLength = pos[1];
            int right = left + sideLength - 1;
            
            // 查询当前区间的最大高度
            int currentMax = query(root, 0, MAX_RANGE, left, right);
            
            // 计算新方块的顶部高度
            int newHeight = currentMax + sideLength;
            
            // 更新区间高度
            update(root, 0, MAX_RANGE, left, right, newHeight);
            
            // 更新全局最大高度
            maxHeight = max(maxHeight, newHeight);
            result.push_back(maxHeight);
        }
        
        freeTree(root);
        return result;
    }
};

// 测试代码
int main() {
    Solution solution;
    
    // 测试用例1
    vector<vector<int>> positions1 = {{1, 2}, {2, 3}, {6, 1}};
    vector<int> result1 = solution.fallingSquares(positions1);
    cout << "[";
    for (int i = 0; i < result1.size(); i++) {
        cout << result1[i];
        if (i < result1.size() - 1) cout << ", ";
    }
    cout << "]" << endl; // 输出 [2, 5, 5]
    
    // 测试用例2
    vector<vector<int>> positions2 = {{100, 100}, {200, 100}};
    vector<int> result2 = solution.fallingSquares(positions2);
    cout << "[";
    for (int i = 0; i < result2.size(); i++) {
        cout << result2[i];
        if (i < result2.size() - 1) cout << ", ";
    }
    cout << "]" << endl; // 输出 [100, 100]
    
    return 0;
}
```

### Python实现
```python
class Solution:
    # 动态开点线段树节点定义
    class Node:
        def __init__(self):
            self.max_height = 0  # 当前区间的最大高度
            self.lazy = 0        # 懒惰标记，表示待下传的更新值
            self.left = None     # 左子节点
            self.right = None    # 右子节点
    
    def fallingSquares(self, positions):
        root = self.Node()
        result = []
        max_height = 0
        
        # 确定坐标范围
        MAX_RANGE = 10**9
        
        for pos in positions:
            left = pos[0]
            side_length = pos[1]
            right = left + side_length - 1
            
            # 查询当前区间的最大高度
            current_max = self._query(root, 0, MAX_RANGE, left, right)
            
            # 计算新方块的顶部高度
            new_height = current_max + side_length
            
            # 更新区间高度
            self._update(root, 0, MAX_RANGE, left, right, new_height)
            
            # 更新全局最大高度
            max_height = max(max_height, new_height)
            result.append(max_height)
        
        return result
    
    # 区间更新（将区间设置为某个值，但只在新值更大时更新）
    def _update(self, node, start, end, L, R, val):
        if not node:
            node = self.Node()
        
        # 如果当前区间完全包含在目标区间内，且新值大于当前最大值，则更新
        if L <= start and end <= R and val > node.max_height:
            node.max_height = val
            node.lazy = val
            return node
        
        # 下传懒惰标记
        self._push_down(node)
        
        mid = start + (end - start) // 2
        # 更新左子区间
        if L <= mid:
            if not node.left:
                node.left = self.Node()
            node.left = self._update(node.left, start, mid, L, R, val)
        # 更新右子区间
        if R > mid:
            if not node.right:
                node.right = self.Node()
            node.right = self._update(node.right, mid + 1, end, L, R, val)
        
        # 上传信息
        node.max_height = max(
            node.left.max_height if node.left else 0,
            node.right.max_height if node.right else 0
        )
        return node
    
    # 查询区间 [L, R] 内的最大值
    def _query(self, node, start, end, L, R):
        if not node:
            return 0
        
        # 如果当前区间完全包含在目标区间内
        if L <= start and end <= R:
            return node.max_height
        
        # 下传懒惰标记
        self._push_down(node)
        
        mid = start + (end - start) // 2
        max_val = 0
        
        # 查询左子区间
        if L <= mid:
            max_val = max(max_val, self._query(node.left, start, mid, L, R))
        # 查询右子区间
        if R > mid:
            max_val = max(max_val, self._query(node.right, mid + 1, end, L, R))
        
        return max_val
    
    # 下传懒惰标记
    def _push_down(self, node):
        if node.lazy != 0:
            # 为左子节点创建并传递标记
            if not node.left:
                node.left = self.Node()
            if node.lazy > node.left.max_height:
                node.left.max_height = node.lazy
                node.left.lazy = node.lazy
            
            # 为右子节点创建并传递标记
            if not node.right:
                node.right = self.Node()
            if node.lazy > node.right.max_height:
                node.right.max_height = node.lazy
                node.right.lazy = node.lazy
            
            # 清除当前节点的懒惰标记
            node.lazy = 0

# 测试代码
if __name__ == "__main__":
    solution = Solution()
    
    # 测试用例1
    positions1 = [[1, 2], [2, 3], [6, 1]]
    print(solution.fallingSquares(positions1))  # 输出 [2, 5, 5]
    
    # 测试用例2
    positions2 = [[100, 100], [200, 100]]
    print(solution.fallingSquares(positions2))  # 输出 [100, 100]

## 5. SPOJ KGSS - Maximum Sum

### 题目描述
给定一个长度为n的整数序列，执行m次操作：
1. U i x: 将第i个位置的值更新为x
2. Q l r: 查询[l,r]区间内两个最大值的和

### 解题思路
使用线段树维护区间信息，每个节点存储区间最大值和次大值。

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

## 6. POJ 2777 - Count Color

### 题目描述
给定一个长度为L的板条，初始时所有位置都是颜色1，执行O次操作：
1. "C A B C": 将区间[A,B]染成颜色C
2. "P A B": 查询区间[A,B]中有多少种不同的颜色

### 解题思路
使用线段树维护区间信息，每个节点存储区间颜色集合(用位运算表示)，结合懒惰标记实现区间染色。

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

## 7. LeetCode 715. Range Module

### 题目描述
实现一个Range模块，支持以下操作：
1. `addRange(int left, int right)`: 添加一个区间 [left, right) 到模块中
2. `queryRange(int left, int right)`: 查询区间 [left, right) 是否完全被覆盖
3. `removeRange(int left, int right)`: 移除区间 [left, right) 中的所有元素

### 解题思路
使用动态开点线段树实现，因为数据范围很大（10^9）但实际操作次数有限。

### Java实现
```java
public class RangeModule {
    // 动态开点线段树节点类
    private static class Node {
        int left, right; // 左右子节点索引
        boolean covered; // 当前区间是否被完全覆盖
        boolean lazy;    // 懒惰标记
    }
    
    private List<Node> tree; // 使用List动态存储节点
    private final int MAX_VAL = 1000000000;
    
    public RangeModule() {
        tree = new ArrayList<>();
        tree.add(new Node()); // 根节点索引为0
    }
    
    // 处理懒惰标记
    private void pushDown(int node, int start, int end) {
        if (tree.get(node).lazy && start < end) {
            // 确保左右子节点存在
            if (tree.get(node).left == 0) {
                tree.add(new Node());
                tree.get(node).left = tree.size() - 1;
            }
            if (tree.get(node).right == 0) {
                tree.add(new Node());
                tree.get(node).right = tree.size() - 1;
            }
            
            int left = tree.get(node).left;
            int right = tree.get(node).right;
            
            // 下传标记
            tree.get(left).covered = tree.get(node).covered;
            tree.get(left).lazy = true;
            tree.get(right).covered = tree.get(node).covered;
            tree.get(right).lazy = true;
            
            // 清除当前节点标记
            tree.get(node).lazy = false;
        }
    }
    
    // 更新区间
    private void update(int node, int start, int end, int l, int r, boolean covered) {
        if (start > r || end < l) {
            return;
        }
        
        if (l <= start && end <= r) {
            tree.get(node).covered = covered;
            tree.get(node).lazy = true;
            return;
        }
        
        pushDown(node, start, end);
        int mid = start + (end - start) / 2;
        
        // 递归更新左右子树
        if (tree.get(node).left == 0) {
            tree.add(new Node());
            tree.get(node).left = tree.size() - 1;
        }
        if (tree.get(node).right == 0) {
            tree.add(new Node());
            tree.get(node).right = tree.size() - 1;
        }
        
        update(tree.get(node).left, start, mid, l, r, covered);
        update(tree.get(node).right, mid + 1, end, l, r, covered);
    }
    
    // 查询区间
    private boolean query(int node, int start, int end, int l, int r) {
        if (start > r || end < l) {
            return true; // 不在查询范围内，返回true不影响结果
        }
        
        if (l <= start && end <= r) {
            return tree.get(node).covered;
        }
        
        pushDown(node, start, end);
        int mid = start + (end - start) / 2;
        
        // 确保左右子节点存在
        if (tree.get(node).left == 0) {
            tree.add(new Node());
            tree.get(node).left = tree.size() - 1;
        }
        if (tree.get(node).right == 0) {
            tree.add(new Node());
            tree.get(node).right = tree.size() - 1;
        }
        
        return query(tree.get(node).left, start, mid, l, r) && 
               query(tree.get(node).right, mid + 1, end, l, r);
    }
    
    public void addRange(int left, int right) {
        update(0, 0, MAX_VAL, left, right - 1, true);
    }
    
    public boolean queryRange(int left, int right) {
        return query(0, 0, MAX_VAL, left, right - 1);
    }
    
    public void removeRange(int left, int right) {
        update(0, 0, MAX_VAL, left, right - 1, false);
    }
}
```

### C++实现
```cpp
#include <vector>
using namespace std;

struct Node {
    int left = 0, right = 0;
    bool covered = false;
    bool lazy = false;
};

class RangeModule {
private:
    vector<Node> tree;
    const int MAX_VAL = 1e9;
    
    void pushDown(int node, int start, int end) {
        if (tree[node].lazy && start < end) {
            // 确保左右子节点存在
            if (tree[node].left == 0) {
                tree.push_back(Node());
                tree[node].left = tree.size() - 1;
            }
            if (tree[node].right == 0) {
                tree.push_back(Node());
                tree[node].right = tree.size() - 1;
            }
            
            int left = tree[node].left;
            int right = tree[node].right;
            
            tree[left].covered = tree[node].covered;
            tree[left].lazy = true;
            tree[right].covered = tree[node].covered;
            tree[right].lazy = true;
            
            tree[node].lazy = false;
        }
    }
    
    void update(int node, int start, int end, int l, int r, bool covered) {
        if (start > r || end < l) return;
        
        if (l <= start && end <= r) {
            tree[node].covered = covered;
            tree[node].lazy = true;
            return;
        }
        
        pushDown(node, start, end);
        int mid = start + (end - start) / 2;
        
        if (tree[node].left == 0) {
            tree.push_back(Node());
            tree[node].left = tree.size() - 1;
        }
        if (tree[node].right == 0) {
            tree.push_back(Node());
            tree[node].right = tree.size() - 1;
        }
        
        update(tree[node].left, start, mid, l, r, covered);
        update(tree[node].right, mid + 1, end, l, r, covered);
    }
    
    bool query(int node, int start, int end, int l, int r) {
        if (start > r || end < l) return true;
        
        if (l <= start && end <= r) {
            return tree[node].covered;
        }
        
        pushDown(node, start, end);
        int mid = start + (end - start) / 2;
        
        if (tree[node].left == 0) {
            tree.push_back(Node());
            tree[node].left = tree.size() - 1;
        }
        if (tree[node].right == 0) {
            tree.push_back(Node());
            tree[node].right = tree.size() - 1;
        }
        
        return query(tree[node].left, start, mid, l, r) && 
               query(tree[node].right, mid + 1, end, l, r);
    }
public:
    RangeModule() {
        tree.push_back(Node()); // 根节点
    }
    
    void addRange(int left, int right) {
        update(0, 0, MAX_VAL, left, right - 1, true);
    }
    
    bool queryRange(int left, int right) {
        return query(0, 0, MAX_VAL, left, right - 1);
    }
    
    void removeRange(int left, int right) {
        update(0, 0, MAX_VAL, left, right - 1, false);
    }
};
```

### Python实现
```python
class RangeModule:
    def __init__(self):
        # 使用字典动态存储节点，键为节点索引，值为节点信息
        self.tree = {0: {'left': 0, 'right': 0, 'covered': False, 'lazy': False}}
        self.node_count = 1  # 当前已使用的节点数
        self.MAX_VAL = 10**9
    
    def push_down(self, node, start, end):
        if self.tree[node]['lazy'] and start < end:
            # 确保左右子节点存在
            if self.tree[node]['left'] == 0:
                self.tree[node]['left'] = self.node_count
                self.tree[self.node_count] = {'left': 0, 'right': 0, 'covered': False, 'lazy': False}
                self.node_count += 1
            if self.tree[node]['right'] == 0:
                self.tree[node]['right'] = self.node_count
                self.tree[self.node_count] = {'left': 0, 'right': 0, 'covered': False, 'lazy': False}
                self.node_count += 1
            
            left_node = self.tree[node]['left']
            right_node = self.tree[node]['right']
            
            # 下传标记
            self.tree[left_node]['covered'] = self.tree[node]['covered']
            self.tree[left_node]['lazy'] = True
            self.tree[right_node]['covered'] = self.tree[node]['covered']
            self.tree[right_node]['lazy'] = True
            
            # 清除当前节点标记
            self.tree[node]['lazy'] = False
    
    def update(self, node, start, end, l, r, covered):
        if start > r or end < l:
            return
        
        if l <= start and end <= r:
            self.tree[node]['covered'] = covered
            self.tree[node]['lazy'] = True
            return
        
        self.push_down(node, start, end)
        mid = start + (end - start) // 2
        
        left_node = self.tree[node]['left']
        right_node = self.tree[node]['right']
        
        self.update(left_node, start, mid, l, r, covered)
        self.update(right_node, mid + 1, end, l, r, covered)
    
    def query(self, node, start, end, l, r):
        if start > r or end < l:
            return True  # 不在查询范围内
        
        if l <= start and end <= r:
            return self.tree[node]['covered']
        
        self.push_down(node, start, end)
        mid = start + (end - start) // 2
        
        left_node = self.tree[node]['left']
        right_node = self.tree[node]['right']
        
        return self.query(left_node, start, mid, l, r) and \
               self.query(right_node, mid + 1, end, l, r)
    
    def addRange(self, left: int, right: int) -> None:
        self.update(0, 0, self.MAX_VAL, left, right - 1, True)
    
    def queryRange(self, left: int, right: int) -> bool:
        return self.query(0, 0, self.MAX_VAL, left, right - 1)
    
    def removeRange(self, left: int, right: int) -> None:
        self.update(0, 0, self.MAX_VAL, left, right - 1, False)

## 8. LeetCode 218. The Skyline Problem

### 题目描述
给定n座建筑物，每个建筑物由左下角坐标、右上角坐标和高度表示。找出这些建筑物在二维平面上形成的天际线。

### 解题思路
使用扫描线算法结合线段树：
1. 将所有建筑物的左右边界作为事件点
2. 按x坐标排序事件点
3. 遍历事件点，维护当前活跃的高度集合
4. 使用线段树高效处理区间更新和最大值查询

### Java实现
```java
import java.util.*;

public class SkylineProblem {
    // 线段树节点类
    private static class Node {
        int maxHeight;
        int addMark; // 懒惰标记
    }
    
    private Node[] tree;
    
    // 构建线段树
    private void build(int node, int start, int end) {
        tree[node].maxHeight = 0;
        tree[node].addMark = 0;
        
        if (start < end) {
            int mid = start + (end - start) / 2;
            build(2 * node, start, mid);
            build(2 * node + 1, mid + 1, end);
        }
    }
    
    // 下传懒惰标记
    private void pushDown(int node) {
        if (tree[node].addMark != 0) {
            int leftNode = 2 * node;
            int rightNode = 2 * node + 1;
            
            // 更新左右子节点的最大高度
            tree[leftNode].maxHeight = Math.max(tree[leftNode].maxHeight, tree[node].addMark);
            tree[rightNode].maxHeight = Math.max(tree[rightNode].maxHeight, tree[node].addMark);
            
            // 更新左右子节点的懒惰标记
            tree[leftNode].addMark = Math.max(tree[leftNode].addMark, tree[node].addMark);
            tree[rightNode].addMark = Math.max(tree[rightNode].addMark, tree[node].addMark);
            
            // 清除当前节点的懒惰标记
            tree[node].addMark = 0;
        }
    }
    
    // 更新区间
    private void update(int node, int start, int end, int l, int r, int height) {
        if (start > r || end < l) {
            return;
        }
        
        if (l <= start && end <= r) {
            tree[node].maxHeight = Math.max(tree[node].maxHeight, height);
            tree[node].addMark = Math.max(tree[node].addMark, height);
            return;
        }
        
        pushDown(node);
        int mid = start + (end - start) / 2;
        update(2 * node, start, mid, l, r, height);
        update(2 * node + 1, mid + 1, end, l, r, height);
        
        // 更新当前节点的最大高度
        tree[node].maxHeight = Math.max(tree[2 * node].maxHeight, tree[2 * node + 1].maxHeight);
    }
    
    // 查询单点
    private int query(int node, int start, int end, int idx) {
        if (start == end) {
            return tree[node].maxHeight;
        }
        
        pushDown(node);
        int mid = start + (end - start) / 2;
        if (idx <= mid) {
            return query(2 * node, start, mid, idx);
        } else {
            return query(2 * node + 1, mid + 1, end, idx);
        }
    }
    
    public List<List<Integer>> getSkyline(int[][] buildings) {
        // 收集所有x坐标并排序去重
        Set<Integer> xSet = new TreeSet<>();
        for (int[] building : buildings) {
            xSet.add(building[0]);
            xSet.add(building[1]);
        }
        List<Integer> xCoords = new ArrayList<>(xSet);
        Map<Integer, Integer> xToIdx = new HashMap<>();
        for (int i = 0; i < xCoords.size(); i++) {
            xToIdx.put(xCoords.get(i), i);
        }
        
        // 初始化线段树
        int n = xCoords.size();
        tree = new Node[4 * n];
        for (int i = 0; i < 4 * n; i++) {
            tree[i] = new Node();
        }
        build(1, 0, n - 1);
        
        // 处理所有建筑物
        for (int[] building : buildings) {
            int l = xToIdx.get(building[0]);
            int r = xToIdx.get(building[1]) - 1; // 闭区间
            int height = building[2];
            if (l <= r) {
                update(1, 0, n - 1, l, r, height);
            }
        }
        
        // 生成天际线
        List<List<Integer>> result = new ArrayList<>();
        int prevHeight = 0;
        for (int i = 0; i < xCoords.size(); i++) {
            int currentHeight = query(1, 0, n - 1, i);
            if (currentHeight != prevHeight) {
                result.add(Arrays.asList(xCoords.get(i), currentHeight));
                prevHeight = currentHeight;
            }
        }
        return result;
    }
}

### C++实现
```cpp
#include <vector>
#include <set>
#include <map>
#include <algorithm>
using namespace std;

struct Node {
    int maxHeight = 0;
    int addMark = 0;
};

class SkylineProblem {
private:
    vector<Node> tree;
    
    void build(int node, int start, int end) {
        tree[node].maxHeight = 0;
        tree[node].addMark = 0;
        
        if (start < end) {
            int mid = start + (end - start) / 2;
            build(2 * node, start, mid);
            build(2 * node + 1, mid + 1, end);
        }
    }
    
    void pushDown(int node) {
        if (tree[node].addMark != 0) {
            int leftNode = 2 * node;
            int rightNode = 2 * node + 1;
            
            tree[leftNode].maxHeight = max(tree[leftNode].maxHeight, tree[node].addMark);
            tree[rightNode].maxHeight = max(tree[rightNode].maxHeight, tree[node].addMark);
            
            tree[leftNode].addMark = max(tree[leftNode].addMark, tree[node].addMark);
            tree[rightNode].addMark = max(tree[rightNode].addMark, tree[node].addMark);
            
            tree[node].addMark = 0;
        }
    }
    
    void update(int node, int start, int end, int l, int r, int height) {
        if (start > r || end < l) {
            return;
        }
        
        if (l <= start && end <= r) {
            tree[node].maxHeight = max(tree[node].maxHeight, height);
            tree[node].addMark = max(tree[node].addMark, height);
            return;
        }
        
        pushDown(node);
        int mid = start + (end - start) / 2;
        update(2 * node, start, mid, l, r, height);
        update(2 * node + 1, mid + 1, end, l, r, height);
        
        tree[node].maxHeight = max(tree[2 * node].maxHeight, tree[2 * node + 1].maxHeight);
    }
    
    int query(int node, int start, int end, int idx) {
        if (start == end) {
            return tree[node].maxHeight;
        }
        
        pushDown(node);
        int mid = start + (end - start) / 2;
        if (idx <= mid) {
            return query(2 * node, start, mid, idx);
        } else {
            return query(2 * node + 1, mid + 1, end, idx);
        }
    }
public:
    vector<vector<int>> getSkyline(vector<vector<int>>& buildings) {
        set<int> xSet;
        for (const auto& building : buildings) {
            xSet.insert(building[0]);
            xSet.insert(building[1]);
        }
        vector<int> xCoords(xSet.begin(), xSet.end());
        map<int, int> xToIdx;
        for (int i = 0; i < xCoords.size(); i++) {
            xToIdx[xCoords[i]] = i;
        }
        
        int n = xCoords.size();
        tree.resize(4 * n);
        build(1, 0, n - 1);
        
        for (const auto& building : buildings) {
            int l = xToIdx[building[0]];
            int r = xToIdx[building[1]] - 1; // 闭区间
            int height = building[2];
            if (l <= r) {
                update(1, 0, n - 1, l, r, height);
            }
        }
        
        vector<vector<int>> result;
        int prevHeight = 0;
        for (int i = 0; i < xCoords.size(); i++) {
            int currentHeight = query(1, 0, n - 1, i);
            if (currentHeight != prevHeight) {
                result.push_back({xCoords[i], currentHeight});
                prevHeight = currentHeight;
            }
        }
        return result;
    }
};
```

### Python实现
```python
class SkylineProblem:
    def __init__(self):
        self.tree = []  # 线段树数组
    
    def build(self, node, start, end):
        # 确保树的大小足够
        if len(self.tree) <= node:
            self.tree += [{'maxHeight': 0, 'addMark': 0} for _ in range(node - len(self.tree) + 1)]
        
        self.tree[node]['maxHeight'] = 0
        self.tree[node]['addMark'] = 0
        
        if start < end:
            mid = start + (end - start) // 2
            self.build(2 * node, start, mid)
            self.build(2 * node + 1, mid + 1, end)
    
    def push_down(self, node):
        if len(self.tree) <= node:
            return
        
        if self.tree[node]['addMark'] != 0:
            # 确保左右子节点存在
            left_node = 2 * node
            right_node = 2 * node + 1
            
            if len(self.tree) <= left_node:
                self.tree += [{'maxHeight': 0, 'addMark': 0} for _ in range(left_node - len(self.tree) + 1)]
            if len(self.tree) <= right_node:
                self.tree += [{'maxHeight': 0, 'addMark': 0} for _ in range(right_node - len(self.tree) + 1)]
            
            # 更新左右子节点
            self.tree[left_node]['maxHeight'] = max(self.tree[left_node]['maxHeight'], self.tree[node]['addMark'])
            self.tree[right_node]['maxHeight'] = max(self.tree[right_node]['maxHeight'], self.tree[node]['addMark'])
            
            self.tree[left_node]['addMark'] = max(self.tree[left_node]['addMark'], self.tree[node]['addMark'])
            self.tree[right_node]['addMark'] = max(self.tree[right_node]['addMark'], self.tree[node]['addMark'])
            
            # 清除当前节点标记
            self.tree[node]['addMark'] = 0
    
    def update(self, node, start, end, l, r, height):
        if start > r or end < l:
            return
        
        # 确保节点存在
        if len(self.tree) <= node:
            self.tree += [{'maxHeight': 0, 'addMark': 0} for _ in range(node - len(self.tree) + 1)]
        
        if l <= start and end <= r:
            self.tree[node]['maxHeight'] = max(self.tree[node]['maxHeight'], height)
            self.tree[node]['addMark'] = max(self.tree[node]['addMark'], height)
            return
        
        self.push_down(node)
        mid = start + (end - start) // 2
        self.update(2 * node, start, mid, l, r, height)
        self.update(2 * node + 1, mid + 1, end, l, r, height)
        
        # 确保左右子节点存在
        left_node = 2 * node
        right_node = 2 * node + 1
        if len(self.tree) <= left_node:
            self.tree += [{'maxHeight': 0, 'addMark': 0} for _ in range(left_node - len(self.tree) + 1)]
        if len(self.tree) <= right_node:
            self.tree += [{'maxHeight': 0, 'addMark': 0} for _ in range(right_node - len(self.tree) + 1)]
        
        # 更新当前节点的最大高度
        self.tree[node]['maxHeight'] = max(self.tree[left_node]['maxHeight'], self.tree[right_node]['maxHeight'])
    
    def query(self, node, start, end, idx):
        # 确保节点存在
        if len(self.tree) <= node:
            self.tree += [{'maxHeight': 0, 'addMark': 0} for _ in range(node - len(self.tree) + 1)]
        
        if start == end:
            return self.tree[node]['maxHeight']
        
        self.push_down(node)
        mid = start + (end - start) // 2
        if idx <= mid:
            return self.query(2 * node, start, mid, idx)
        else:
            return self.query(2 * node + 1, mid + 1, end, idx)
    
    def get_skyline(self, buildings):
        # 收集所有x坐标并排序去重
        x_set = set()
        for building in buildings:
            x_set.add(building[0])
            x_set.add(building[1])
        x_coords = sorted(x_set)
        x_to_idx = {x: i for i, x in enumerate(x_coords)}
        
        n = len(x_coords)
        self.tree = []  # 重置线段树
        self.build(1, 0, n - 1)
        
        # 处理所有建筑物
        for building in buildings:
            l = x_to_idx[building[0]]
            r = x_to_idx[building[1]] - 1  # 闭区间
            height = building[2]
            if l <= r:
                self.update(1, 0, n - 1, l, r, height)
        
        # 生成天际线
        result = []
        prev_height = 0
        for i in range(n):
            current_height = self.query(1, 0, n - 1, i)
            if current_height != prev_height:
                result.append([x_coords[i], current_height])
                prev_height = current_height
        
        return result
```

## 总结

### 线段树的应用场景与技巧

线段树作为一种强大的数据结构，适用于以下场景：

1. **区间查询与更新**：
   - 区间求和、最大值、最小值查询
   - 区间加法、乘法、赋值操作
   - 时间复杂度：O(log n) per operation

2. **离散化技术**：
   - 当数据范围很大但实际用到的点较少时
   - 如POJ 2528 Mayor's posters中的坐标压缩

3. **懒惰标记优化**：
   - 延迟传播更新操作，避免不必要的子树访问
   - 适用于区间批量操作
   - 注意标记的合并顺序与优先级

4. **动态开点线段树**：
   - 适用于值域范围极大的场景（如10^9）
   - 按需创建节点，节省空间
   - 如LeetCode 715 Range Module

5. **线段树的扩展变体**：
   - 吉司机线段树：处理区间取模等操作
   - 线段树维护多个信息：如最大子段和问题需要维护四个值
   - 扫描线算法结合线段树：如LeetCode 218 Skyline Problem

6. **括号匹配问题**：
   - 维护区间内的括号匹配情况
   - 需要记录左括号数量、右括号数量、匹配数

### 解题技巧总结

1. **节点信息设计**：
   - 根据问题需求设计合适的节点结构
   - 考虑需要维护哪些区间信息
   - 如何高效合并子节点信息

2. **懒惰标记处理**：
   - 正确处理标记的下传顺序
   - 注意标记的叠加规则（如多次加法的合并）
   - 确保不会遗漏标记的传播

3. **边界条件处理**：
   - 单点更新与查询的正确性
   - 区间不完全覆盖时的递归处理
   - 避免数组越界等低级错误

4. **性能优化**：
   - 离散化处理大范围数据
   - 动态开点减少空间使用
   - 合理预估线段树数组大小（通常为4*n）

5. **多语言实现注意事项**：
   - Java中注意整数溢出问题
   - C++中注意vector的预分配大小
   - Python中注意递归深度限制（可能需要非递归实现）

## 9. Codeforces 446C - DZY Loves Fibonacci Numbers

### 题目描述
给定一个序列，支持两种操作：
1. 将区间[L, R]加上斐波那契数列（F[1], F[2], ..., F[R-L+1]）
2. 查询区间[L, R]的和

### 解题思路
使用线段树维护区间和，并结合数学性质：斐波那契数列的前缀和满足一定规律，可以通过维护两个参数a和b来表示斐波那契数列的起始项。

### Java实现
```java
public class DZYLovesFibonacciNumbers {
    private static final int MOD = 1000000009;
    
    static class Node {
        long sum;      // 区间和
        long a, b;     // 斐波那契增量的参数
        boolean hasAdd; // 是否有增量标记
    }
    
    private Node[] tree;
    private long[] F, S; // 斐波那契数列和前缀和数组
    
    // 快速幂求逆元
    private long pow(long a, long b) {
        long res = 1;
        while (b > 0) {
            if (b % 2 == 1) res = res * a % MOD;
            a = a * a % MOD;
            b /= 2;
        }
        return res;
    }
    
    // 预处理斐波那契数列和前缀和
    private void precompute(int n) {
        F = new long[n + 3];
        S = new long[n + 3];
        F[1] = 1; F[2] = 1;
        S[1] = 1; S[2] = 2;
        
        for (int i = 3; i <= n; i++) {
            F[i] = (F[i-1] + F[i-2]) % MOD;
            S[i] = (S[i-1] + F[i]) % MOD;
        }
    }
    
    // 计算斐波那契数列的第n项
    private long fib(long n) {
        if (n <= 0) return 0;
        if (n <= F.length - 1) return F[(int)n];
        // 对于大n可以用矩阵快速幂，但这里假设预处理足够大
        return 0;
    }
    
    // 计算斐波那契数列前n项和
    private long sumFib(long n) {
        if (n <= 0) return 0;
        if (n <= S.length - 1) return S[(int)n];
        // 公式：S(n) = F(n+2) - 1
        return (fib(n+2) - 1 + MOD) % MOD;
    }
    
    // 计算区间[L, R]的增量和
    private long getAddSum(long a, long b, long L, long R) {
        long len = R - L + 1;
        // 斐波那契数列的区间和
        long term1 = a * (sumFib(len + 1) - 1 + MOD) % MOD;
        long term2 = b * (sumFib(len) - 1 + MOD) % MOD;
        return (term1 + term2) % MOD;
    }
    
    // 下传标记
    private void pushDown(int node, int start, int end) {
        if (tree[node].hasAdd) {
            int left = 2 * node;
            int right = 2 * node + 1;
            int mid = start + (end - start) / 2;
            
            // 计算左右子区间的增量参数
            long a = tree[node].a;
            long b = tree[node].b;
            
            // 左子区间：[start, mid]
            tree[left].a = (tree[left].a + a) % MOD;
            tree[left].b = (tree[left].b + b) % MOD;
            tree[left].sum = (tree[left].sum + getAddSum(a, b, 1, mid - start + 1)) % MOD;
            tree[left].hasAdd = true;
            
            // 右子区间：[mid+1, end]，起始项为F[mid-start+2], F[mid-start+3]
            long newA = (a * F[mid - start + 1] % MOD + b * F[mid - start] % MOD) % MOD;
            long newB = (a * F[mid - start + 2] % MOD + b * F[mid - start + 1] % MOD) % MOD;
            tree[right].a = (tree[right].a + newA) % MOD;
            tree[right].b = (tree[right].b + newB) % MOD;
            tree[right].sum = (tree[right].sum + getAddSum(newA, newB, 1, end - mid)) % MOD;
            tree[right].hasAdd = true;
            
            // 清除当前节点标记
            tree[node].a = 0;
            tree[node].b = 0;
            tree[node].hasAdd = false;
        }
    }
    
    // 构建线段树
    private void build(int node, int start, int end, long[] arr) {
        tree[node].sum = 0;
        tree[node].a = 0;
        tree[node].b = 0;
        tree[node].hasAdd = false;
        
        if (start == end) {
            tree[node].sum = arr[start] % MOD;
            return;
        }
        
        int mid = start + (end - start) / 2;
        build(2 * node, start, mid, arr);
        build(2 * node + 1, mid + 1, end, arr);
        tree[node].sum = (tree[2 * node].sum + tree[2 * node + 1].sum) % MOD;
    }
    
    // 区间更新
    private void update(int node, int start, int end, int l, int r) {
        if (start > r || end < l) return;
        
        if (l <= start && end <= r) {
            // 计算该区间对应的斐波那契起始项
            long a = F[start - l + 1];
            long b = F[start - l + 2];
            
            tree[node].a = (tree[node].a + a) % MOD;
            tree[node].b = (tree[node].b + b) % MOD;
            tree[node].sum = (tree[node].sum + getAddSum(a, b, 1, end - start + 1)) % MOD;
            tree[node].hasAdd = true;
            return;
        }
        
        pushDown(node, start, end);
        int mid = start + (end - start) / 2;
        update(2 * node, start, mid, l, r);
        update(2 * node + 1, mid + 1, end, l, r);
        tree[node].sum = (tree[2 * node].sum + tree[2 * node + 1].sum) % MOD;
    }
    
    // 区间查询
    private long query(int node, int start, int end, int l, int r) {
        if (start > r || end < l) return 0;
        
        if (l <= start && end <= r) {
            return tree[node].sum;
        }
        
        pushDown(node, start, end);
        int mid = start + (end - start) / 2;
        long leftSum = query(2 * node, start, mid, l, r);
        long rightSum = query(2 * node + 1, mid + 1, end, l, r);
        return (leftSum + rightSum) % MOD;
    }
    
    public static void main(String[] args) {
        // 示例使用
    }
}

### C++实现
```cpp
#include <iostream>
#include <vector>
using namespace std;

const int MOD = 1000000009;

struct Node {
    long long sum;
    long long a, b;
    bool hasAdd;
    Node() : sum(0), a(0), b(0), hasAdd(false) {}
};

class DZYLovesFibonacciNumbers {
private:
    vector<Node> tree;
    vector<long long> F, S;
    
    long long pow(long long a, long long b) {
        long long res = 1;
        while (b > 0) {
            if (b % 2 == 1) res = res * a % MOD;
            a = a * a % MOD;
            b /= 2;
        }
        return res;
    }
    
    void precompute(int n) {
        F.resize(n + 3);
        S.resize(n + 3);
        F[1] = 1; F[2] = 1;
        S[1] = 1; S[2] = 2;
        
        for (int i = 3; i <= n; i++) {
            F[i] = (F[i-1] + F[i-2]) % MOD;
            S[i] = (S[i-1] + F[i]) % MOD;
        }
    }
    
    long long fib(long long n) {
        if (n <= 0) return 0;
        if (n < F.size()) return F[n];
        // 对于大n可以用矩阵快速幂
        return 0;
    }
    
    long long sumFib(long long n) {
        if (n <= 0) return 0;
        if (n < S.size()) return S[n];
        // 公式：S(n) = F(n+2) - 1
        return (fib(n+2) - 1 + MOD) % MOD;
    }
    
    long long getAddSum(long long a, long long b, long long L, long long R) {
        long long len = R - L + 1;
        long long term1 = a * (sumFib(len + 1) - 1 + MOD) % MOD;
        long long term2 = b * (sumFib(len) - 1 + MOD) % MOD;
        return (term1 + term2) % MOD;
    }
    
    void pushDown(int node, int start, int end) {
        if (tree[node].hasAdd) {
            int left = 2 * node;
            int right = 2 * node + 1;
            int mid = start + (end - start) / 2;
            
            long long a = tree[node].a;
            long long b = tree[node].b;
            
            // 左子区间
            tree[left].a = (tree[left].a + a) % MOD;
            tree[left].b = (tree[left].b + b) % MOD;
            tree[left].sum = (tree[left].sum + getAddSum(a, b, 1, mid - start + 1)) % MOD;
            tree[left].hasAdd = true;
            
            // 右子区间
            long long newA = (a * F[mid - start + 1] % MOD + b * F[mid - start] % MOD) % MOD;
            long long newB = (a * F[mid - start + 2] % MOD + b * F[mid - start + 1] % MOD) % MOD;
            tree[right].a = (tree[right].a + newA) % MOD;
            tree[right].b = (tree[right].b + newB) % MOD;
            tree[right].sum = (tree[right].sum + getAddSum(newA, newB, 1, end - mid)) % MOD;
            tree[right].hasAdd = true;
            
            // 清除标记
            tree[node].a = 0;
            tree[node].b = 0;
            tree[node].hasAdd = false;
        }
    }
    
    void build(int node, int start, int end, const vector<long long>& arr) {
        if (start == end) {
            tree[node].sum = arr[start] % MOD;
            return;
        }
        
        int mid = start + (end - start) / 2;
        build(2 * node, start, mid, arr);
        build(2 * node + 1, mid + 1, end, arr);
        tree[node].sum = (tree[2 * node].sum + tree[2 * node + 1].sum) % MOD;
    }
    
    void update(int node, int start, int end, int l, int r) {
        if (start > r || end < l) return;
        
        if (l <= start && end <= r) {
            long long a = F[start - l + 1];
            long long b = F[start - l + 2];
            
            tree[node].a = (tree[node].a + a) % MOD;
            tree[node].b = (tree[node].b + b) % MOD;
            tree[node].sum = (tree[node].sum + getAddSum(a, b, 1, end - start + 1)) % MOD;
            tree[node].hasAdd = true;
            return;
        }
        
        pushDown(node, start, end);
        int mid = start + (end - start) / 2;
        update(2 * node, start, mid, l, r);
        update(2 * node + 1, mid + 1, end, l, r);
        tree[node].sum = (tree[2 * node].sum + tree[2 * node + 1].sum) % MOD;
    }
    
    long long query(int node, int start, int end, int l, int r) {
        if (start > r || end < l) return 0;
        
        if (l <= start && end <= r) {
            return tree[node].sum;
        }
        
        pushDown(node, start, end);
        int mid = start + (end - start) / 2;
        long long leftSum = query(2 * node, start, mid, l, r);
        long long rightSum = query(2 * node + 1, mid + 1, end, l, r);
        return (leftSum + rightSum) % MOD;
    }
public:
    void solve() {
        int n, m;
        cin >> n >> m;
        
        precompute(n);
        tree.resize(4 * n);
        
        vector<long long> arr(n + 1);
        for (int i = 1; i <= n; i++) {
            cin >> arr[i];
        }
        build(1, 1, n, arr);
        
        while (m--) {
            int op, l, r;
            cin >> op >> l >> r;
            if (op == 1) {
                update(1, 1, n, l, r);
            } else {
                cout << query(1, 1, n, l, r) << endl;
            }
        }
    }
};
```

### Python实现
```python
MOD = 10**9 + 9

class DZYLovesFibonacciNumbers:
    def __init__(self):
        self.tree = []
        self.F = []
        self.S = []
    
    def pow_mod(self, a, b):
        res = 1
        while b > 0:
            if b % 2 == 1:
                res = res * a % MOD
            a = a * a % MOD
            b //= 2
        return res
    
    def precompute(self, n):
        self.F = [0] * (n + 3)
        self.S = [0] * (n + 3)
        self.F[1] = 1
        self.F[2] = 1
        self.S[1] = 1
        self.S[2] = 2
        
        for i in range(3, n + 1):
            self.F[i] = (self.F[i-1] + self.F[i-2]) % MOD
            self.S[i] = (self.S[i-1] + self.F[i]) % MOD
    
    def fib(self, n):
        if n <= 0:
            return 0
        if n < len(self.F):
            return self.F[n]
        # 对于大n可以用矩阵快速幂
        return 0
    
    def sum_fib(self, n):
        if n <= 0:
            return 0
        if n < len(self.S):
            return self.S[n]
        # 公式：S(n) = F(n+2) - 1
        return (self.fib(n+2) - 1 + MOD) % MOD
    
    def get_add_sum(self, a, b, L, R):
        len_ = R - L + 1
        term1 = a * (self.sum_fib(len_ + 1) - 1 + MOD) % MOD
        term2 = b * (self.sum_fib(len_) - 1 + MOD) % MOD
        return (term1 + term2) % MOD
    
    def push_down(self, node, start, end):
        if self.tree[node]['hasAdd']:
            left = 2 * node
            right = 2 * node + 1
            mid = start + (end - start) // 2
            
            a = self.tree[node]['a']
            b = self.tree[node]['b']
            
            # 确保左右子节点存在
            if len(self.tree) <= left:
                self.tree += [{'sum': 0, 'a': 0, 'b': 0, 'hasAdd': False} for _ in range(left - len(self.tree) + 1)]
            if len(self.tree) <= right:
                self.tree += [{'sum': 0, 'a': 0, 'b': 0, 'hasAdd': False} for _ in range(right - len(self.tree) + 1)]
            
            # 左子区间
            self.tree[left]['a'] = (self.tree[left]['a'] + a) % MOD
            self.tree[left]['b'] = (self.tree[left]['b'] + b) % MOD
            self.tree[left]['sum'] = (self.tree[left]['sum'] + self.get_add_sum(a, b, 1, mid - start + 1)) % MOD
            self.tree[left]['hasAdd'] = True
            
            # 右子区间
            new_a = (a * self.F[mid - start + 1] % MOD + b * self.F[mid - start] % MOD) % MOD
            new_b = (a * self.F[mid - start + 2] % MOD + b * self.F[mid - start + 1] % MOD) % MOD
            self.tree[right]['a'] = (self.tree[right]['a'] + new_a) % MOD
            self.tree[right]['b'] = (self.tree[right]['b'] + new_b) % MOD
            self.tree[right]['sum'] = (self.tree[right]['sum'] + self.get_add_sum(new_a, new_b, 1, end - mid)) % MOD
            self.tree[right]['hasAdd'] = True
            
            # 清除标记
            self.tree[node]['a'] = 0
            self.tree[node]['b'] = 0
            self.tree[node]['hasAdd'] = False
    
    def build(self, node, start, end, arr):
        # 确保节点存在
        if len(self.tree) <= node:
            self.tree += [{'sum': 0, 'a': 0, 'b': 0, 'hasAdd': False} for _ in range(node - len(self.tree) + 1)]
        
        if start == end:
            self.tree[node]['sum'] = arr[start] % MOD
            return
        
        mid = start + (end - start) // 2
        self.build(2 * node, start, mid, arr)
        self.build(2 * node + 1, mid + 1, end, arr)
        # 确保左右子节点存在
        left = 2 * node
        right = 2 * node + 1
        if len(self.tree) <= left:
            self.tree += [{'sum': 0, 'a': 0, 'b': 0, 'hasAdd': False} for _ in range(left - len(self.tree) + 1)]
        if len(self.tree) <= right:
            self.tree += [{'sum': 0, 'a': 0, 'b': 0, 'hasAdd': False} for _ in range(right - len(self.tree) + 1)]
        
        self.tree[node]['sum'] = (self.tree[left]['sum'] + self.tree[right]['sum']) % MOD
    
    def update(self, node, start, end, l, r):
        if start > r or end < l:
            return
        
        # 确保节点存在
        if len(self.tree) <= node:
            self.tree += [{'sum': 0, 'a': 0, 'b': 0, 'hasAdd': False} for _ in range(node - len(self.tree) + 1)]
        
        if l <= start and end <= r:
            a = self.F[start - l + 1]
            b = self.F[start - l + 2]
            
            self.tree[node]['a'] = (self.tree[node]['a'] + a) % MOD
            self.tree[node]['b'] = (self.tree[node]['b'] + b) % MOD
            self.tree[node]['sum'] = (self.tree[node]['sum'] + self.get_add_sum(a, b, 1, end - start + 1)) % MOD
            self.tree[node]['hasAdd'] = True
            return
        
        self.push_down(node, start, end)
        mid = start + (end - start) // 2
        self.update(2 * node, start, mid, l, r)
        self.update(2 * node + 1, mid + 1, end, l, r)
        # 确保左右子节点存在
        left = 2 * node
        right = 2 * node + 1
        if len(self.tree) <= left:
            self.tree += [{'sum': 0, 'a': 0, 'b': 0, 'hasAdd': False} for _ in range(left - len(self.tree) + 1)]
        if len(self.tree) <= right:
            self.tree += [{'sum': 0, 'a': 0, 'b': 0, 'hasAdd': False} for _ in range(right - len(self.tree) + 1)]
        
        self.tree[node]['sum'] = (self.tree[left]['sum'] + self.tree[right]['sum']) % MOD
    
    def query(self, node, start, end, l, r):
        if start > r or end < l:
            return 0
        
        # 确保节点存在
        if len(self.tree) <= node:
            self.tree += [{'sum': 0, 'a': 0, 'b': 0, 'hasAdd': False} for _ in range(node - len(self.tree) + 1)]
        
        if l <= start and end <= r:
            return self.tree[node]['sum']
        
        self.push_down(node, start, end)
        mid = start + (end - start) // 2
        left_sum = self.query(2 * node, start, mid, l, r)
        right_sum = self.query(2 * node + 1, mid + 1, end, l, r)
        return (left_sum + right_sum) % MOD
    
    def solve(self):
        # 示例使用
        pass

## 10. SPOJ HORRIBLE - Horrible Queries

### 题目描述
给定一个数组，支持两种操作：
1. 将区间[L, R]的每个元素加上一个值X
2. 查询区间[L, R]的元素和

### 解题思路
使用线段树维护区间和，并结合懒惰标记进行区间更新优化。

### Java实现
```java
public class HorribleQueries {
    static class Node {
        long sum;      // 区间和
        long addMark;  // 加法标记
    }
    
    private Node[] tree;
    private int n;
    
    public HorribleQueries(int[] arr) {
        n = arr.length;
        tree = new Node[4 * n];
        for (int i = 0; i < 4 * n; i++) {
            tree[i] = new Node();
        }
        build(1, 0, n - 1, arr);
    }
    
    // 下传标记
    private void pushDown(int node, int start, int end) {
        if (tree[node].addMark != 0) {
            int left = 2 * node;
            int right = 2 * node + 1;
            int mid = start + (end - start) / 2;
            
            // 左右子区间的长度
            long leftLen = mid - start + 1;
            long rightLen = end - mid;
            
            // 更新左右子节点的和
            tree[left].sum += tree[node].addMark * leftLen;
            tree[right].sum += tree[node].addMark * rightLen;
            
            // 传递标记
            tree[left].addMark += tree[node].addMark;
            tree[right].addMark += tree[node].addMark;
            
            // 清除当前节点的标记
            tree[node].addMark = 0;
        }
    }
    
    // 构建线段树
    private void build(int node, int start, int end, int[] arr) {
        if (start == end) {
            tree[node].sum = arr[start];
            return;
        }
        
        int mid = start + (end - start) / 2;
        build(2 * node, start, mid, arr);
        build(2 * node + 1, mid + 1, end, arr);
        tree[node].sum = tree[2 * node].sum + tree[2 * node + 1].sum;
    }
    
    // 区间更新
    private void update(int node, int start, int end, int l, int r, long val) {
        if (start > r || end < l) return;
        
        if (l <= start && end <= r) {
            // 当前区间完全包含在目标区间内
            tree[node].sum += val * (end - start + 1);
            tree[node].addMark += val;
            return;
        }
        
        pushDown(node, start, end);
        int mid = start + (end - start) / 2;
        update(2 * node, start, mid, l, r, val);
        update(2 * node + 1, mid + 1, end, l, r, val);
        tree[node].sum = tree[2 * node].sum + tree[2 * node + 1].sum;
    }
    
    // 区间查询
    private long query(int node, int start, int end, int l, int r) {
        if (start > r || end < l) return 0;
        
        if (l <= start && end <= r) {
            return tree[node].sum;
        }
        
        pushDown(node, start, end);
        int mid = start + (end - start) / 2;
        long leftSum = query(2 * node, start, mid, l, r);
        long rightSum = query(2 * node + 1, mid + 1, end, l, r);
        return leftSum + rightSum;
    }
    
    // 公开方法
    public void rangeAdd(int l, int r, long val) {
        update(1, 0, n - 1, l, r, val);
    }
    
    public long rangeQuery(int l, int r) {
        return query(1, 0, n - 1, l, r);
    }
}
```

### C++实现
```cpp
#include <iostream>
#include <vector>
using namespace std;

struct Node {
    long long sum;
    long long addMark;
    Node() : sum(0), addMark(0) {}
};

class HorribleQueries {
private:
    vector<Node> tree;
    int n;
    
    void pushDown(int node, int start, int end) {
        if (tree[node].addMark != 0) {
            int left = 2 * node;
            int right = 2 * node + 1;
            int mid = start + (end - start) / 2;
            
            long long leftLen = mid - start + 1;
            long long rightLen = end - mid;
            
            tree[left].sum += tree[node].addMark * leftLen;
            tree[right].sum += tree[node].addMark * rightLen;
            
            tree[left].addMark += tree[node].addMark;
            tree[right].addMark += tree[node].addMark;
            
            tree[node].addMark = 0;
        }
    }
    
    void build(int node, int start, int end, const vector<long long>& arr) {
        if (start == end) {
            tree[node].sum = arr[start];
            return;
        }
        
        int mid = start + (end - start) / 2;
        build(2 * node, start, mid, arr);
        build(2 * node + 1, mid + 1, end, arr);
        tree[node].sum = tree[2 * node].sum + tree[2 * node + 1].sum;
    }
    
    void update(int node, int start, int end, int l, int r, long long val) {
        if (start > r || end < l) return;
        
        if (l <= start && end <= r) {
            tree[node].sum += val * (end - start + 1);
            tree[node].addMark += val;
            return;
        }
        
        pushDown(node, start, end);
        int mid = start + (end - start) / 2;
        update(2 * node, start, mid, l, r, val);
        update(2 * node + 1, mid + 1, end, l, r, val);
        tree[node].sum = tree[2 * node].sum + tree[2 * node + 1].sum;
    }
    
    long long query(int node, int start, int end, int l, int r) {
        if (start > r || end < l) return 0;
        
        if (l <= start && end <= r) {
            return tree[node].sum;
        }
        
        pushDown(node, start, end);
        int mid = start + (end - start) / 2;
        long long leftSum = query(2 * node, start, mid, l, r);
        long long rightSum = query(2 * node + 1, mid + 1, end, l, r);
        return leftSum + rightSum;
    }
public:
    HorribleQueries(const vector<long long>& arr) {
        n = arr.size();
        tree.resize(4 * n);
        build(1, 0, n - 1, arr);
    }
    
    void rangeAdd(int l, int r, long long val) {
        update(1, 0, n - 1, l, r, val);
    }
    
    long long rangeQuery(int l, int r) {
        return query(1, 0, n - 1, l, r);
    }
    
    void solve() {
        int t;
        cin >> t;
        while (t--) {
            int n, q;
            cin >> n >> q;
            vector<long long> arr(n, 0);
            HorribleQueries hq(arr);
            while (q--) {
                int op, l, r;
                long long val;
                cin >> op >> l >> r;
                l--; r--; // 转换为0-based索引
                if (op == 0) {
                    cin >> val;
                    hq.rangeAdd(l, r, val);
                } else {
                    cout << hq.rangeQuery(l, r) << endl;
                }
            }
        }
    }
};
```

### Python实现
```python
class HorribleQueries:
    def __init__(self, arr):
        self.n = len(arr)
        self.tree = [{'sum': 0, 'addMark': 0} for _ in range(4 * self.n)]
        self.build(1, 0, self.n - 1, arr)
    
    def push_down(self, node, start, end):
        if self.tree[node]['addMark'] != 0:
            left = 2 * node
            right = 2 * node + 1
            mid = start + (end - start) // 2
            
            left_len = mid - start + 1
            right_len = end - mid
            
            # 更新左右子节点的和
            self.tree[left]['sum'] += self.tree[node]['addMark'] * left_len
            self.tree[right]['sum'] += self.tree[node]['addMark'] * right_len
            
            # 传递标记
            self.tree[left]['addMark'] += self.tree[node]['addMark']
            self.tree[right]['addMark'] += self.tree[node]['addMark']
            
            # 清除当前节点的标记
            self.tree[node]['addMark'] = 0
    
    def build(self, node, start, end, arr):
        if start == end:
            self.tree[node]['sum'] = arr[start]
            return
        
        mid = start + (end - start) // 2
        self.build(2 * node, start, mid, arr)
        self.build(2 * node + 1, mid + 1, end, arr)
        self.tree[node]['sum'] = self.tree[2 * node]['sum'] + self.tree[2 * node + 1]['sum']
    
    def update(self, node, start, end, l, r, val):
        if start > r or end < l:
            return
        
        if l <= start and end <= r:
            self.tree[node]['sum'] += val * (end - start + 1)
            self.tree[node]['addMark'] += val
            return
        
        self.push_down(node, start, end)
        mid = start + (end - start) // 2
        self.update(2 * node, start, mid, l, r, val)
        self.update(2 * node + 1, mid + 1, end, l, r, val)
        self.tree[node]['sum'] = self.tree[2 * node]['sum'] + self.tree[2 * node + 1]['sum']
    
    def query(self, node, start, end, l, r):
        if start > r or end < l:
            return 0
        
        if l <= start and end <= r:
            return self.tree[node]['sum']
        
        self.push_down(node, start, end)
        mid = start + (end - start) // 2
        left_sum = self.query(2 * node, start, mid, l, r)
        right_sum = self.query(2 * node + 1, mid + 1, end, l, r)
        return left_sum + right_sum
    
    def range_add(self, l, r, val):
        self.update(1, 0, self.n - 1, l, r, val)
    
    def range_query(self, l, r):
        return self.query(1, 0, self.n - 1, l, r)

## 总结

通过以上扩展题目，我们可以看到线段树在各种场景下的强大应用。从基础的区间查询与更新，到更复杂的动态开点、扫描线算法、斐波那契数列区间更新等，线段树都能高效处理。

在实现线段树时，需要特别注意以下几点：

1. **节点信息设计**：根据问题需求设计合适的节点结构，包括需要维护的数据和懒惰标记类型
2. **懒惰标记的正确传播**：确保标记能够正确地下传给子节点，并在适当的时候清除
3. **边界条件处理**：特别注意区间边界的处理，避免出现错误
4. **性能优化**：对于大规模数据，可以考虑离散化、动态开点等技术
5. **多语言实现差异**：不同语言在数据类型、递归深度等方面有不同的限制，需要适当调整实现方式

线段树作为一种通用的数据结构，在算法竞赛和实际工程中都有广泛的应用。掌握线段树的各种变体和优化技巧，对于解决复杂的区间操作问题非常有帮助。

通过这些题目的学习，我们不仅能够掌握线段树的基本用法，还能够深入理解其设计思想和优化技巧，为解决更复杂的算法问题打下坚实的基础。

在实际应用中，线段树通常与其他算法（如扫描线、离散化）结合使用，以解决更复杂的问题。因此，在学习线段树的同时，也应该关注相关算法和技术的学习，形成完整的知识体系。

1. **POJ 3468 A Simple Problem with Integers** 是线段树区间更新和查询的经典模板题
2. **POJ 2528 Mayor's posters** 展示了离散化在线段树中的重要应用
3. **Codeforces 438D The Child and Sequence** 是吉司机线段树的另一个经典应用，处理取模操作
4. **SPOJ GSS1 - Can you answer these queries I** 展示了线段树在最大子段和问题中的应用
5. **SPOJ KGSS - Maximum Sum** 展示了线段树在维护最大值和次大值中的应用
6. **POJ 2777 - Count Color** 展示了线段树在区间染色和颜色计数中的应用

线段树的其他重要应用场景还包括：

7. **动态开点线段树**：适用于值域范围极大但实际使用点稀疏的情况，如LeetCode 715. Range Module
8. **扫描线算法结合线段树**：解决区间覆盖、面积计算等问题，如LeetCode 218. The Skyline Problem
9. **线段树优化DP**：例如某些区间转移的动态规划问题
10. **线段树分治**：处理离线的时间相关查询和更新操作

## 11. LeetCode 731. My Calendar II

### 题目描述
实现一个 MyCalendarTwo 类来存放你的日程安排。如果要添加的时间内不会导致三重预订时，则可以存储这个新的日程安排。

MyCalendarTwo 有一个 book(int start, int end) 方法。它意味着在 start 到 end 时间内增加一个日程安排，注意，这里的时间是半开区间，即 [start, end), 实数 x 的范围为， start <= x < end。

当三个日程安排有一些时间上的重叠时（例如三个日程安排都在同一时间内），就会产生三重预订。

每次调用 MyCalendarTwo.book 方法时，如果可以将日程安排成功添加而不会导致三重预订，返回 true。否则，返回 false 并且不要添加该日程安排。

### 解题思路
这道题可以使用动态开点线段树来高效处理。由于时间范围可能很大（到10^9），使用普通线段树会占用过多内存，而动态开点线段树只在需要时创建节点，更加高效。

### Java实现
```java
// LeetCode 731. 我的日程安排表 II - Java实现
// 动态开点线段树解法
public class MyCalendarTwo {
    // 线段树节点
    private static class Node {
        int left; // 左边界
        int right; // 右边界
        int count; // 当前区间的覆盖次数
        int add; // 懒惰标记
        Node leftChild; // 左子节点
        Node rightChild; // 右子节点
        
        public Node(int left, int right) {
            this.left = left;
            this.right = right;
        }
    }
    
    private Node root; // 线段树的根节点
    private final int MAX_TIME = 1_000_000_000; // 根据题目约束设置最大值
    
    public MyCalendarTwo() {
        // 初始化根节点，覆盖题目中可能的时间范围
        root = new Node(0, MAX_TIME);
    }
    
    /**
     * 尝试预订一个新的区间
     * @param start 开始时间
     * @param end 结束时间（不包含）
     * @return 如果预订成功（不产生三重预订）返回true，否则返回false
     */
    public boolean book(int start, int end) {
        // 首先查询区间[start, end-1]的最大覆盖次数
        if (query(root, start, end - 1) >= 2) {
            return false; // 已经有两个预订，不能再预订
        }
        // 然后对区间[start, end-1]进行加1操作
        update(root, start, end - 1, 1);
        return true;
    }
    
    /**
     * 查询区间[L, R]的最大覆盖次数
     */
    private int query(Node node, int L, int R) {
        if (node == null) return 0;
        
        // 当前节点的区间完全包含在查询区间外
        if (node.right < L || node.left > R) {
            return 0;
        }
        
        // 当前节点的区间完全包含在查询区间内
        if (L <= node.left && node.right <= R) {
            return node.count;
        }
        
        // 否则需要递归查询左右子节点
        pushDown(node);
        int leftMax = query(node.leftChild, L, R);
        int rightMax = query(node.rightChild, L, R);
        return Math.max(leftMax, rightMax);
    }
    
    /**
     * 更新区间[L, R]，增加val
     */
    private void update(Node node, int L, int R, int val) {
        // 当前节点的区间完全包含在更新区间外
        if (node.right < L || node.left > R) {
            return;
        }
        
        // 当前节点的区间完全包含在更新区间内
        if (L <= node.left && node.right <= R) {
            node.count += val;
            node.add += val;
            return;
        }
        
        // 否则需要递归更新左右子节点
        pushDown(node);
        update(node.leftChild, L, R, val);
        update(node.rightChild, L, R, val);
        // 更新当前节点的最大覆盖次数
        node.count = Math.max(node.leftChild != null ? node.leftChild.count : 0, 
                             node.rightChild != null ? node.rightChild.count : 0);
    }
    
    /**
     * 下推懒惰标记，创建子节点（动态开点）
     */
    private void pushDown(Node node) {
        int mid = node.left + (node.right - node.left) / 2;
        
        if (node.leftChild == null) {
            node.leftChild = new Node(node.left, mid);
        }
        if (node.rightChild == null) {
            node.rightChild = new Node(mid + 1, node.right);
        }
        
        if (node.add != 0) {
            node.leftChild.count += node.add;
            node.leftChild.add += node.add;
            node.rightChild.count += node.add;
            node.rightChild.add += node.add;
            node.add = 0; // 清除当前节点的懒惰标记
        }
    }
    
    public static void main(String[] args) {
        MyCalendarTwo calendar = new MyCalendarTwo();
        System.out.println(calendar.book(10, 20)); // 预期输出: true
        System.out.println(calendar.book(50, 60)); // 预期输出: true
        System.out.println(calendar.book(10, 40)); // 预期输出: true
        System.out.println(calendar.book(5, 15));  // 预期输出: false
        System.out.println(calendar.book(5, 10));  // 预期输出: true
        System.out.println(calendar.book(25, 55)); // 预期输出: true
    }
}
```

### C++实现
```cpp
// LeetCode 731. My Calendar II - C++实现
#include <iostream>
using namespace std;

class MyCalendarTwo {
private:
    // 线段树节点
    struct Node {
        long long left, right; // 使用long long避免溢出
        int count; // 当前区间的最大覆盖次数
        int add; // 懒惰标记
        Node *leftChild, *rightChild;
        
        Node(long long l, long long r) : left(l), right(r), count(0), add(0), 
                                         leftChild(nullptr), rightChild(nullptr) {}
    };
    
    Node* root; // 线段树的根节点
    const long long MAX_TIME = 1e9; // 根据题目约束设置最大值
    
    // 下推懒惰标记，创建子节点（动态开点）
    void pushDown(Node* node) {
        long long mid = node->left + (node->right - node->left) / 2;
        
        if (!node->leftChild) {
            node->leftChild = new Node(node->left, mid);
        }
        if (!node->rightChild) {
            node->rightChild = new Node(mid + 1, node->right);
        }
        
        if (node->add != 0) {
            // 下传标记到左右子节点
            node->leftChild->count += node->add;
            node->leftChild->add += node->add;
            node->rightChild->count += node->add;
            node->rightChild->add += node->add;
            node->add = 0; // 清除当前节点的懒惰标记
        }
    }
    
    // 更新区间[L, R]，增加val
    void update(Node* node, long long L, long long R, int val) {
        // 当前节点的区间完全包含在更新区间外
        if (node->right < L || node->left > R) {
            return;
        }
        
        // 当前节点的区间完全包含在更新区间内
        if (L <= node->left && node->right <= R) {
            node->count += val;
            node->add += val;
            return;
        }
        
        // 否则需要递归更新左右子节点
        pushDown(node);
        update(node->leftChild, L, R, val);
        update(node->rightChild, L, R, val);
        // 更新当前节点的最大覆盖次数
        node->count = max(node->leftChild->count, node->rightChild->count);
    }
    
    // 查询区间[L, R]的最大覆盖次数
    int query(Node* node, long long L, long long R) {
        if (!node) return 0;
        
        // 当前节点的区间完全包含在查询区间外
        if (node->right < L || node->left > R) {
            return 0;
        }
        
        // 当前节点的区间完全包含在查询区间内
        if (L <= node->left && node->right <= R) {
            return node->count;
        }
        
        // 否则需要递归查询左右子节点
        pushDown(node);
        int leftMax = query(node->leftChild, L, R);
        int rightMax = query(node->rightChild, L, R);
        return max(leftMax, rightMax);
    }
    
    // 释放内存，避免内存泄漏
    void clearTree(Node* node) {
        if (!node) return;
        clearTree(node->leftChild);
        clearTree(node->rightChild);
        delete node;
    }
    
public:
    MyCalendarTwo() {
        // 初始化根节点，覆盖题目中可能的时间范围
        root = new Node(0, MAX_TIME);
    }
    
    ~MyCalendarTwo() {
        // 析构函数中释放内存
        clearTree(root);
    }
    
    bool book(int start, int end) {
        // 将end-1，因为题目中的时间是半开区间 [start, end)
        long long L = start;
        long long R = end - 1;
        
        // 首先查询区间[start, end-1]的最大覆盖次数
        if (query(root, L, R) >= 2) {
            return false; // 已经有两个预订，不能再预订
        }
        
        // 然后对区间[start, end-1]进行加1操作
        update(root, L, R, 1);
        return true;
    }
};

int main() {
    MyCalendarTwo calendar;
    cout << (calendar.book(10, 20) ? "true" : "false") << endl; // 输出: true
    cout << (calendar.book(50, 60) ? "true" : "false") << endl; // 输出: true
    cout << (calendar.book(10, 40) ? "true" : "false") << endl; // 输出: true
    cout << (calendar.book(5, 15)  ? "true" : "false") << endl; // 输出: false
    cout << (calendar.book(5, 10)  ? "true" : "false") << endl; // 输出: true
    cout << (calendar.book(25, 55) ? "true" : "false") << endl; // 输出: true
    return 0;
}
```

### Python实现
```python
# LeetCode 731. My Calendar II - Python实现
class MyCalendarTwo:
    def __init__(self):
        # 初始化根节点，覆盖题目中可能的时间范围
        self.root = {'left': 0, 'right': 10**9, 'count': 0, 'add': 0, 
                    'leftChild': None, 'rightChild': None}
    
    def book(self, start: int, end: int) -> bool:
        """
        尝试预订一个新的区间
        :param start: 开始时间
        :param end: 结束时间（不包含）
        :return: 如果预订成功（不产生三重预订）返回True，否则返回False
        """
        # 由于时间是半开区间 [start, end)，我们查询 [start, end-1]
        if self._query(self.root, start, end - 1) >= 2:
            return False  # 已经有两个预订，不能再预订
        
        # 更新区间，增加预订次数
        self._update(self.root, start, end - 1, 1)
        return True
    
    def _push_down(self, node):
        """
        下推懒惰标记，创建子节点（动态开点）
        """
        mid = node['left'] + (node['right'] - node['left']) // 2
        
        # 如果子节点不存在，则创建
        if not node['leftChild']:
            node['leftChild'] = {'left': node['left'], 'right': mid, 
                               'count': 0, 'add': 0, 
                               'leftChild': None, 'rightChild': None}
        if not node['rightChild']:
            node['rightChild'] = {'left': mid + 1, 'right': node['right'], 
                                'count': 0, 'add': 0, 
                                'leftChild': None, 'rightChild': None}
        
        # 如果有懒惰标记需要下传
        if node['add'] != 0:
            # 下传标记到左右子节点
            node['leftChild']['count'] += node['add']
            node['leftChild']['add'] += node['add']
            node['rightChild']['count'] += node['add']
            node['rightChild']['add'] += node['add']
            # 清除当前节点的懒惰标记
            node['add'] = 0
    
    def _update(self, node, L, R, val):
        """
        更新区间[L, R]，增加val
        """
        # 当前节点的区间完全包含在更新区间外
        if node['right'] < L or node['left'] > R:
            return
        
        # 当前节点的区间完全包含在更新区间内
        if L <= node['left'] and node['right'] <= R:
            node['count'] += val
            node['add'] += val
            return
        
        # 否则需要递归更新左右子节点
        self._push_down(node)
        self._update(node['leftChild'], L, R, val)
        self._update(node['rightChild'], L, R, val)
        # 更新当前节点的最大覆盖次数
        node['count'] = max(node['leftChild']['count'], node['rightChild']['count'])
    
    def _query(self, node, L, R):
        """
        查询区间[L, R]的最大覆盖次数
        """
        if not node:
            return 0
        
        # 当前节点的区间完全包含在查询区间外
        if node['right'] < L or node['left'] > R:
            return 0
        
        # 当前节点的区间完全包含在查询区间内
        if L <= node['left'] and node['right'] <= R:
            return node['count']
        
        # 否则需要递归查询左右子节点
        self._push_down(node)
        left_max = self._query(node['leftChild'], L, R)
        right_max = self._query(node['rightChild'], L, R)
        return max(left_max, right_max)

# 测试代码
if __name__ == "__main__":
    calendar = MyCalendarTwo()
    print(calendar.book(10, 20))  # 输出: True
    print(calendar.book(50, 60))  # 输出: True
    print(calendar.book(10, 40))  # 输出: True
    print(calendar.book(5, 15))   # 输出: False
    print(calendar.book(5, 10))   # 输出: True
    print(calendar.book(25, 55))  # 输出: True
```

## 12. LeetCode 699. Falling Squares

### 题目描述
在无限长的数轴（即 x 轴）上，我们根据给定的顺序放置对应的正方形方块。

第 i 个掉落的方块（positions[i] = (left, side_length)）是正方形，其左下角在 (left, 0)，右上角在 (left + side_length, side_length)。

当方块掉落并停在某个高度时，它会立即粘住下面的任何方块，或者粘在地面上（如果没有方块在它下面的话）。方块一旦粘住，就不会再移动。

返回一个堆叠高度列表 ans，其中 ans[i] 表示在第 i 个方块掉落之后，最高堆叠方块的高度。

### 解题思路
这道题可以使用动态开点线段树来维护每个区间的最大高度。当放置一个新方块时，我们需要：
1. 查询该方块覆盖区间的当前最大高度
2. 计算新方块的顶部高度（当前最大高度 + 方块边长）
3. 更新该区间的高度为新方块的顶部高度
4. 记录全局最大高度

### Java实现
```java
// LeetCode 699. Falling Squares - Java实现
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FallingSquares {
    // 线段树节点
    static class Node {
        int max;      // 当前区间的最大高度
        int lazy;     // 懒惰标记，表示待下传的更新值
        Node left;    // 左子节点
        Node right;   // 右子节点
    }
    
    private Node root;
    private final int MAX_RANGE = 1_000_000_000; // 根据题目约束设置最大值范围
    
    public FallingSquares() {
        root = new Node();
    }
    
    /**
     * 处理掉落的方块，返回每次掉落后的最大高度列表
     */
    public List<Integer> fallingSquares(int[][] positions) {
        List<Integer> result = new ArrayList<>();
        int maxHeight = 0;
        
        for (int[] pos : positions) {
            int left = pos[0];
            int sideLength = pos[1];
            int right = left + sideLength - 1;
            
            // 查询当前区间的最大高度
            int currentMax = query(root, 0, MAX_RANGE, left, right);
            
            // 计算新方块的顶部高度
            int newHeight = currentMax + sideLength;
            
            // 更新区间高度
            update(root, 0, MAX_RANGE, left, right, newHeight);
            
            // 更新全局最大高度
            maxHeight = Math.max(maxHeight, newHeight);
            result.add(maxHeight);
        }
        
        return result;
    }
    
    /**
     * 更新区间[L, R]的最大高度为val
     * 这里使用的是区间覆盖操作
     */
    private void update(Node node, int l, int r, int L, int R, int val) {
        // 确保节点存在
        if (node == null) {
            return;
        }
        
        // 当前区间完全包含在更新区间内
        if (L <= l && r <= R) {
            // 使用区间覆盖，只保留最大值
            node.max = Math.max(node.max, val);
            node.lazy = Math.max(node.lazy, val);
            return;
        }
        
        // 下推懒惰标记
        pushDown(node);
        
        int mid = l + (r - l) / 2;
        if (L <= mid) {
            // 确保左子节点存在
            if (node.left == null) {
                node.left = new Node();
            }
            update(node.left, l, mid, L, R, val);
        }
        if (R > mid) {
            // 确保右子节点存在
            if (node.right == null) {
                node.right = new Node();
            }
            update(node.right, mid + 1, r, L, R, val);
        }
        
        // 更新当前节点的最大值
        int leftMax = (node.left != null) ? node.left.max : 0;
        int rightMax = (node.right != null) ? node.right.max : 0;
        node.max = Math.max(leftMax, rightMax);
    }
    
    /**
     * 查询区间[L, R]的最大高度
     */
    private int query(Node node, int l, int r, int L, int R) {
        if (node == null) {
            return 0;
        }
        
        // 当前区间完全包含在查询区间内
        if (L <= l && r <= R) {
            return node.max;
        }
        
        // 下推懒惰标记
        pushDown(node);
        
        int mid = l + (r - l) / 2;
        int maxVal = 0;
        
        if (L <= mid && node.left != null) {
            maxVal = Math.max(maxVal, query(node.left, l, mid, L, R));
        }
        if (R > mid && node.right != null) {
            maxVal = Math.max(maxVal, query(node.right, mid + 1, r, L, R));
        }
        
        return maxVal;
    }
    
    /**
     * 下推懒惰标记
     */
    private void pushDown(Node node) {
        if (node.lazy != 0) {
            // 确保左右子节点存在
            if (node.left == null) {
                node.left = new Node();
            }
            if (node.right == null) {
                node.right = new Node();
            }
            
            // 更新子节点的max和lazy值
            node.left.max = Math.max(node.left.max, node.lazy);
            node.left.lazy = Math.max(node.left.lazy, node.lazy);
            node.right.max = Math.max(node.right.max, node.lazy);
            node.right.lazy = Math.max(node.right.lazy, node.lazy);
            
            // 清除当前节点的懒惰标记
            node.lazy = 0;
        }
    }
    
    public static void main(String[] args) {
        FallingSquares fs = new FallingSquares();
        int[][] positions1 = {{1, 2}, {2, 3}, {6, 1}};
        System.out.println(Arrays.toString(fs.fallingSquares(positions1).toArray())); // 输出: [2, 5, 5]
        
        int[][] positions2 = {{100, 100}, {200, 100}};
        System.out.println(Arrays.toString(fs.fallingSquares(positions2).toArray())); // 输出: [100, 100]
    }
}
```

### C++实现
```cpp
// LeetCode 699. Falling Squares - C++实现
#include <iostream>
#include <vector>
#include <algorithm>
using namespace std;

class FallingSquares {
private:
    // 线段树节点
    struct Node {
        int max_height;    // 当前区间的最大高度
        int lazy;          // 懒惰标记，表示待下传的更新值
        Node* left;        // 左子节点
        Node* right;       // 右子节点
        
        Node() : max_height(0), lazy(0), left(nullptr), right(nullptr) {}
    };
    
    Node* root;
    const int MAX_RANGE = 1e9; // 根据题目约束设置最大值范围
    
    /**
     * 下推懒惰标记
     */
    void pushDown(Node* node) {
        if (node->lazy != 0) {
            // 确保左右子节点存在
            if (!node->left) {
                node->left = new Node();
            }
            if (!node->right) {
                node->right = new Node();
            }
            
            // 更新子节点的max_height和lazy值
            node->left->max_height = max(node->left->max_height, node->lazy);
            node->left->lazy = max(node->left->lazy, node->lazy);
            node->right->max_height = max(node->right->max_height, node->lazy);
            node->right->lazy = max(node->right->lazy, node->lazy);
            
            // 清除当前节点的懒惰标记
            node->lazy = 0;
        }
    }
    
    /**
     * 更新区间[L, R]的最大高度为val
     */
    void update(Node* node, int l, int r, int L, int R, int val) {
        // 当前区间完全包含在更新区间内
        if (L <= l && r <= R) {
            // 使用区间覆盖，只保留最大值
            node->max_height = max(node->max_height, val);
            node->lazy = max(node->lazy, val);
            return;
        }
        
        // 下推懒惰标记
        pushDown(node);
        
        int mid = l + (r - l) / 2;
        if (L <= mid) {
            // 确保左子节点存在
            if (!node->left) {
                node->left = new Node();
            }
            update(node->left, l, mid, L, R, val);
        }
        if (R > mid) {
            // 确保右子节点存在
            if (!node->right) {
                node->right = new Node();
            }
            update(node->right, mid + 1, r, L, R, val);
        }
        
        // 更新当前节点的最大值
        int left_max = node->left ? node->left->max_height : 0;
        int right_max = node->right ? node->right->max_height : 0;
        node->max_height = max(left_max, right_max);
    }
    
    /**
     * 查询区间[L, R]的最大高度
     */
    int query(Node* node, int l, int r, int L, int R) {
        if (!node) {
            return 0;
        }
        
        // 当前区间完全包含在查询区间内
        if (L <= l && r <= R) {
            return node->max_height;
        }
        
        // 下推懒惰标记
        pushDown(node);
        
        int mid = l + (r - l) / 2;
        int max_val = 0;
        
        if (L <= mid && node->left) {
            max_val = max(max_val, query(node->left, l, mid, L, R));
        }
        if (R > mid && node->right) {
            max_val = max(max_val, query(node->right, mid + 1, r, L, R));
        }
        
        return max_val;
    }
    
    /**
     * 释放内存，避免内存泄漏
     */
    void clearTree(Node* node) {
        if (!node) return;
        clearTree(node->left);
        clearTree(node->right);
        delete node;
    }
    
public:
    FallingSquares() {
        root = new Node();
    }
    
    ~FallingSquares() {
        clearTree(root);
    }
    
    vector<int> fallingSquares(vector<vector<int>>& positions) {
        vector<int> result;
        int max_height = 0;
        
        for (auto& pos : positions) {
            int left = pos[0];
            int side_length = pos[1];
            int right = left + side_length - 1;
            
            // 查询当前区间的最大高度
            int current_max = query(root, 0, MAX_RANGE, left, right);
            
            // 计算新方块的顶部高度
            int new_height = current_max + side_length;
            
            // 更新区间高度
            update(root, 0, MAX_RANGE, left, right, new_height);
            
            // 更新全局最大高度
            max_height = max(max_height, new_height);
            result.push_back(max_height);
        }
        
        return result;
    }
};

int main() {
    FallingSquares fs;
    
    // 测试用例1
    vector<vector<int>> positions1 = {{1, 2}, {2, 3}, {6, 1}};
    vector<int> result1 = fs.fallingSquares(positions1);
    cout << "[";
    for (int i = 0; i < result1.size(); i++) {
        cout << result1[i];
        if (i < result1.size() - 1) cout << ", ";
    }
    cout << "]" << endl;  // 输出: [2, 5, 5]
    
    // 测试用例2
    vector<vector<int>> positions2 = {{100, 100}, {200, 100}};
    vector<int> result2 = fs.fallingSquares(positions2);
    cout << "[";
    for (int i = 0; i < result2.size(); i++) {
        cout << result2[i];
        if (i < result2.size() - 1) cout << ", ";
    }
    cout << "]" << endl;  // 输出: [100, 100]
    
    return 0;
}
```

### Python实现
```python
# LeetCode 699. Falling Squares - Python实现
class FallingSquares:
    def __init__(self):
        # 初始化根节点，使用字典表示
        self.root = {'max_height': 0, 'lazy': 0, 'left': None, 'right': None}
        self.MAX_RANGE = 10**9  # 根据题目约束设置最大值范围
    
    def fallingSquares(self, positions):
        """
        处理掉落的方块，返回每次掉落后的最大高度列表
        :param positions: 方块的位置列表，每个元素为 [left, side_length]
        :return: 每次掉落后的最大高度列表
        """
        result = []
        max_height = 0
        
        for pos in positions:
            left = pos[0]
            side_length = pos[1]
            right = left + side_length - 1
            
            # 查询当前区间的最大高度
            current_max = self._query(self.root, 0, self.MAX_RANGE, left, right)
            
            # 计算新方块的顶部高度
            new_height = current_max + side_length
            
            # 更新区间高度
            self._update(self.root, 0, self.MAX_RANGE, left, right, new_height)
            
            # 更新全局最大高度
            max_height = max(max_height, new_height)
            result.append(max_height)
        
        return result
    
    def _push_down(self, node):
        """
        下推懒惰标记
        """
        if node['lazy'] != 0:
            # 确保左右子节点存在
            if not node['left']:
                node['left'] = {'max_height': 0, 'lazy': 0, 'left': None, 'right': None}
            if not node['right']:
                node['right'] = {'max_height': 0, 'lazy': 0, 'left': None, 'right': None}
            
            # 更新子节点的max_height和lazy值
            node['left']['max_height'] = max(node['left']['max_height'], node['lazy'])
            node['left']['lazy'] = max(node['left']['lazy'], node['lazy'])
            node['right']['max_height'] = max(node['right']['max_height'], node['lazy'])
            node['right']['lazy'] = max(node['right']['lazy'], node['lazy'])
            
            # 清除当前节点的懒惰标记
            node['lazy'] = 0
    
    def _update(self, node, l, r, L, R, val):
        """
        更新区间[L, R]的最大高度为val
        """
        # 当前区间完全包含在更新区间内
        if L <= l and r <= R:
            # 使用区间覆盖，只保留最大值
            node['max_height'] = max(node['max_height'], val)
            node['lazy'] = max(node['lazy'], val)
            return
        
        # 下推懒惰标记
        self._push_down(node)
        
        mid = l + (r - l) // 2
        if L <= mid:
            # 确保左子节点存在
            if not node['left']:
                node['left'] = {'max_height': 0, 'lazy': 0, 'left': None, 'right': None}
            self._update(node['left'], l, mid, L, R, val)
        if R > mid:
            # 确保右子节点存在
            if not node['right']:
                node['right'] = {'max_height': 0, 'lazy': 0, 'left': None, 'right': None}
            self._update(node['right'], mid + 1, r, L, R, val)
        
        # 更新当前节点的最大值
        left_max = node['left']['max_height'] if node['left'] else 0
        right_max = node['right']['max_height'] if node['right'] else 0
        node['max_height'] = max(left_max, right_max)
    
    def _query(self, node, l, r, L, R):
        """
        查询区间[L, R]的最大高度
        """
        if not node:
            return 0
        
        # 当前区间完全包含在查询区间内
        if L <= l and r <= R:
            return node['max_height']
        
        # 下推懒惰标记
        self._push_down(node)
        
        mid = l + (r - l) // 2
        max_val = 0
        
        if L <= mid and node['left']:
            max_val = max(max_val, self._query(node['left'], l, mid, L, R))
        if R > mid and node['right']:
            max_val = max(max_val, self._query(node['right'], mid + 1, r, L, R))
        
        return max_val

# 测试代码
if __name__ == "__main__":
    fs = FallingSquares()
    
    # 测试用例1
    positions1 = [[1, 2], [2, 3], [6, 1]]
    print(fs.fallingSquares(positions1))  # 输出: [2, 5, 5]
    
    # 测试用例2
    positions2 = [[100, 100], [200, 100]]
    print(fs.fallingSquares(positions2))  # 输出: [100, 100]
```

这些题目帮助我们更深入地理解线段树在实际问题中的灵活应用，包括：
- 懒惰标记的使用
- 离散化技术
- 复杂操作的处理
- 多种查询类型的组合
- 位运算优化

对于线段树的学习，建议关注以下几个方面：

1. **基础操作的底层细节**：理解pushUp、pushDown等核心操作的实现原理
2. **懒惰标记的正确使用**：掌握不同类型操作的懒惰标记设计和下传策略
3. **边界条件的处理**：注意区间的开闭性质、索引的起始位置等细节
4. **性能优化**：在大规模数据情况下，考虑常数优化、内存优化等技术
5. **多维度思考**：从算法设计、工程实现、语言特性等多个角度分析问题

线段树作为一种通用的区间处理工具，在算法竞赛和实际工程中都有广泛的应用。通过深入学习这一数据结构，可以显著提升解决复杂算法问题的能力。