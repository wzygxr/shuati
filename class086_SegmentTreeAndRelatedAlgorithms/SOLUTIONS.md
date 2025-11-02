# 线段树题目详解与实现

## 1. 序列操作 (Code01_SequenceOperation.java)

### 题目解析

本题要求实现一个支持多种操作的01序列：
1. 区间置0
2. 区间置1
3. 区间取反
4. 查询区间1的个数
5. 查询区间连续1的最长长度

### 解题思路

使用线段树维护每个区间的以下信息：
- sum：区间内1的个数
- len0/len1：区间内连续0/1的最长子串长度
- pre0/pre1：区间内连续0/1的最长前缀长度
- suf0/suf1：区间内连续0/1的最长后缀长度

同时使用三种懒标记：
- change：区间被置为的值（0或1）
- update：是否有更新操作
- reverse：是否有翻转操作

### 关键技术点

1. **懒标记优先级**：更新操作优先于翻转操作
2. **区间合并**：需要考虑左右子区间连接处的情况
3. **懒标记下传**：正确处理多种标记的相互影响

### Java实现

```java
package class113;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

public class Code01_SequenceOperation {

    public static int MAXN = 100001;

    // 原始数组
    public static int[] arr = new int[MAXN];

    // 累加和用来统计1的数量
    public static int[] sum = new int[MAXN << 2];

    // 连续0的最长子串长度
    public static int[] len0 = new int[MAXN << 2];

    // 连续0的最长前缀长度
    public static int[] pre0 = new int[MAXN << 2];

    // 连续0的最长后缀长度
    public static int[] suf0 = new int[MAXN << 2];

    // 连续1的最长子串长度
    public static int[] len1 = new int[MAXN << 2];

    // 连续1的最长前缀长度
    public static int[] pre1 = new int[MAXN << 2];

    // 连续1的最长后缀长度
    public static int[] suf1 = new int[MAXN << 2];

    // 懒更新信息，范围上所有数字被重置成了什么
    public static int[] change = new int[MAXN << 2];

    // 懒更新信息，范围上有没有重置任务
    public static boolean[] update = new boolean[MAXN << 2];

    // 懒更新信息，范围上有没有翻转任务
    public static boolean[] reverse = new boolean[MAXN << 2];

    public static void up(int i, int ln, int rn) {
        int l = i << 1;
        int r = i << 1 | 1;
        sum[i] = sum[l] + sum[r];
        len0[i] = Math.max(Math.max(len0[l], len0[r]), suf0[l] + pre0[r]);
        pre0[i] = len0[l] < ln ? pre0[l] : (pre0[l] + pre0[r]);
        suf0[i] = len0[r] < rn ? suf0[r] : (suf0[l] + suf0[r]);
        len1[i] = Math.max(Math.max(len1[l], len1[r]), suf1[l] + pre1[r]);
        pre1[i] = len1[l] < ln ? pre1[l] : (pre1[l] + pre1[r]);
        suf1[i] = len1[r] < rn ? suf1[r] : (suf1[l] + suf1[r]);
    }

    public static void down(int i, int ln, int rn) {
        if (update[i]) {
            updateLazy(i << 1, change[i], ln);
            updateLazy(i << 1 | 1, change[i], rn);
            update[i] = false;
        }
        if (reverse[i]) {
            reverseLazy(i << 1, ln);
            reverseLazy(i << 1 | 1, rn);
            reverse[i] = false;
        }
    }

    public static void updateLazy(int i, int v, int n) {
        sum[i] = v * n;
        len0[i] = pre0[i] = suf0[i] = v == 0 ? n : 0;
        len1[i] = pre1[i] = suf1[i] = v == 1 ? n : 0;
        change[i] = v;
        update[i] = true;
        reverse[i] = false;
    }

    public static void reverseLazy(int i, int n) {
        sum[i] = n - sum[i];
        int tmp;
        tmp = len0[i]; len0[i] = len1[i]; len1[i] = tmp;
        tmp = pre0[i]; pre0[i] = pre1[i]; pre1[i] = tmp;
        tmp = suf0[i]; suf0[i] = suf1[i]; suf1[i] = tmp;
        reverse[i] = !reverse[i];
    }

    public static void build(int l, int r, int i) {
        if (l == r) {
            sum[i] = arr[l];
            len0[i] = pre0[i] = suf0[i] = arr[l] == 0 ? 1 : 0;
            len1[i] = pre1[i] = suf1[i] = arr[l] == 1 ? 1 : 0;
        } else {
            int mid = (l + r) >> 1;
            build(l, mid, i << 1);
            build(mid + 1, r, i << 1 | 1);
            up(i, mid - l + 1, r - mid);
        }
        update[i] = false;
        reverse[i] = false;
    }

    public static void update(int jobl, int jobr, int jobv, int l, int r, int i) {
        if (jobl <= l && r <= jobr) {
            updateLazy(i, jobv, r - l + 1);
        } else {
            int mid = (l + r) >> 1;
            down(i, mid - l + 1, r - mid);
            if (jobl <= mid) {
                update(jobl, jobr, jobv, l, mid, i << 1);
            }
            if (jobr > mid) {
                update(jobl, jobr, jobv, mid + 1, r, i << 1 | 1);
            }
            up(i, mid - l + 1, r - mid);
        }
    }

    public static void reverse(int jobl, int jobr, int l, int r, int i) {
        if (jobl <= l && r <= jobr) {
            reverseLazy(i, r - l + 1);
        } else {
            int mid = (l + r) >> 1;
            down(i, mid - l + 1, r - mid);
            if (jobl <= mid) {
                reverse(jobl, jobr, l, mid, i << 1);
            }
            if (jobr > mid) {
                reverse(jobl, jobr, mid + 1, r, i << 1 | 1);
            }
            up(i, mid - l + 1, r - mid);
        }
    }

    // 线段树范围l~r上，被jobl~jobr影响的区域里，返回1的数量
    public static int querySum(int jobl, int jobr, int l, int r, int i) {
        if (jobl <= l && r <= jobr) {
            return sum[i];
        }
        int mid = (l + r) >> 1;
        down(i, mid - l + 1, r - mid);
        int ans = 0;
        if (jobl <= mid) {
            ans += querySum(jobl, jobr, l, mid, i << 1);
        }
        if (jobr > mid) {
            ans += querySum(jobl, jobr, mid + 1, r, i << 1 | 1);
        }
        return ans;
    }

    // 返回一个长度为3的数组ans，代表结果，具体含义如下：
    // ans[0] : 线段树范围l~r上，被jobl~jobr影响的区域里，连续1的最长子串长度
    // ans[1] : 线段树范围l~r上，被jobl~jobr影响的区域里，连续1的最长前缀长度
    // ans[2] : 线段树范围l~r上，被jobl~jobr影响的区域里，连续1的最长后缀长度
    public static int[] queryLongest(int jobl, int jobr, int l, int r, int i) {
        if (jobl <= l && r <= jobr) {
            return new int[] { len1[i], pre1[i], suf1[i] };
        } else {
            int mid = (l + r) >> 1;
            int ln = mid - l + 1;
            int rn = r - mid;
            down(i, ln, rn);
            if (jobr <= mid) {
                return queryLongest(jobl, jobr, l, mid, i << 1);
            }
            if (jobl > mid) {
                return queryLongest(jobl, jobr, mid + 1, r, i << 1 | 1);
            }
            int[] l3 = queryLongest(jobl, jobr, l, mid, i << 1);
            int[] r3 = queryLongest(jobl, jobr, mid + 1, r, i << 1 | 1);
            int llen = l3[0], lpre = l3[1], lsuf = l3[2];
            int rlen = r3[0], rpre = r3[1], rsuf = r3[2];
            int len = Math.max(Math.max(llen, rlen), lsuf + rpre);
            // 任务实际影响了左侧范围的几个点 -> mid - Math.max(jobl, l) + 1
            int pre = llen < mid - Math.max(jobl, l) + 1 ? lpre : (lpre + rpre);
            // 任务实际影响了右侧范围的几个点 -> Math.min(r, jobr) - mid
            int suf = rlen < Math.min(r, jobr) - mid ? rsuf : (lsuf + rsuf);
            return new int[] { len, pre, suf };
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        in.nextToken();
        int n = (int) in.nval;
        in.nextToken();
        int m = (int) in.nval;
        for (int i = 1; i <= n; i++) {
            in.nextToken();
            arr[i] = (int) in.nval;
        }
        build(1, n, 1);
        for (int i = 1, op, jobl, jobr; i <= m; i++) {
            in.nextToken();
            op = (int) in.nval;
            in.nextToken();
            jobl = (int) in.nval + 1; // 注意题目给的下标从0开始，线段树下标从1开始
            in.nextToken();
            jobr = (int) in.nval + 1; // 注意题目给的下标从0开始，线段树下标从1开始
            if (op == 0) {
                update(jobl, jobr, 0, 1, n, 1);
            } else if (op == 1) {
                update(jobl, jobr, 1, 1, n, 1);
            } else if (op == 2) {
                reverse(jobl, jobr, 1, n, 1);
            } else if (op == 3) {
                out.println(querySum(jobl, jobr, 1, n, 1));
            } else {
                out.println(queryLongest(jobl, jobr, 1, n, 1)[0]);
            }
        }
        out.flush();
        out.close();
        br.close();
    }

}
```

### C++实现

```cpp
#include <bits/stdc++.h>
using namespace std;

const int MAXN = 100001;

int arr[MAXN];

// 线段树数组
int sum[MAXN << 2];
int len0[MAXN << 2], pre0[MAXN << 2], suf0[MAXN << 2];
int len1[MAXN << 2], pre1[MAXN << 2], suf1[MAXN << 2];

// 懒标记
int change[MAXN << 2];
bool update[MAXN << 2];
bool reverse_flag[MAXN << 2];

void up(int i, int ln, int rn) {
    int l = i << 1, r = i << 1 | 1;
    sum[i] = sum[l] + sum[r];
    len0[i] = max({len0[l], len0[r], suf0[l] + pre0[r]});
    pre0[i] = len0[l] < ln ? pre0[l] : (pre0[l] + pre0[r]);
    suf0[i] = len0[r] < rn ? suf0[r] : (suf0[l] + suf0[r]);
    len1[i] = max({len1[l], len1[r], suf1[l] + pre1[r]});
    pre1[i] = len1[l] < ln ? pre1[l] : (pre1[l] + pre1[r]);
    suf1[i] = len1[r] < rn ? suf1[r] : (suf1[l] + suf1[r]);
}

void down(int i, int ln, int rn) {
    if (update[i]) {
        int l = i << 1, r = i << 1 | 1;
        sum[l] = change[i] * ln;
        len0[l] = pre0[l] = suf0[l] = change[i] == 0 ? ln : 0;
        len1[l] = pre1[l] = suf1[l] = change[i] == 1 ? ln : 0;
        change[l] = change[i];
        update[l] = true;
        reverse_flag[l] = false;
        
        sum[r] = change[i] * rn;
        len0[r] = pre0[r] = suf0[r] = change[i] == 0 ? rn : 0;
        len1[r] = pre1[r] = suf1[r] = change[i] == 1 ? rn : 0;
        change[r] = change[i];
        update[r] = true;
        reverse_flag[r] = false;
        
        update[i] = false;
    }
    if (reverse_flag[i]) {
        int l = i << 1, r = i << 1 | 1;
        sum[l] = ln - sum[l];
        swap(len0[l], len1[l]);
        swap(pre0[l], pre1[l]);
        swap(suf0[l], suf1[l]);
        reverse_flag[l] = !reverse_flag[l];
        
        sum[r] = rn - sum[r];
        swap(len0[r], len1[r]);
        swap(pre0[r], pre1[r]);
        swap(suf0[r], suf1[r]);
        reverse_flag[r] = !reverse_flag[r];
        
        reverse_flag[i] = false;
    }
}

void build(int l, int r, int i) {
    if (l == r) {
        sum[i] = arr[l];
        len0[i] = pre0[i] = suf0[i] = arr[l] == 0 ? 1 : 0;
        len1[i] = pre1[i] = suf1[i] = arr[l] == 1 ? 1 : 0;
    } else {
        int mid = (l + r) >> 1;
        build(l, mid, i << 1);
        build(mid + 1, r, i << 1 | 1);
        up(i, mid - l + 1, r - mid);
    }
    update[i] = false;
    reverse_flag[i] = false;
}

void update_range(int jobl, int jobr, int jobv, int l, int r, int i) {
    if (jobl <= l && r <= jobr) {
        sum[i] = jobv * (r - l + 1);
        len0[i] = pre0[i] = suf0[i] = jobv == 0 ? (r - l + 1) : 0;
        len1[i] = pre1[i] = suf1[i] = jobv == 1 ? (r - l + 1) : 0;
        change[i] = jobv;
        update[i] = true;
        reverse_flag[i] = false;
    } else {
        int mid = (l + r) >> 1;
        down(i, mid - l + 1, r - mid);
        if (jobl <= mid) {
            update_range(jobl, jobr, jobv, l, mid, i << 1);
        }
        if (jobr > mid) {
            update_range(jobl, jobr, jobv, mid + 1, r, i << 1 | 1);
        }
        up(i, mid - l + 1, r - mid);
    }
}

void reverse_range(int jobl, int jobr, int l, int r, int i) {
    if (jobl <= l && r <= jobr) {
        sum[i] = (r - l + 1) - sum[i];
        swap(len0[i], len1[i]);
        swap(pre0[i], pre1[i]);
        swap(suf0[i], suf1[i]);
        reverse_flag[i] = !reverse_flag[i];
    } else {
        int mid = (l + r) >> 1;
        down(i, mid - l + 1, r - mid);
        if (jobl <= mid) {
            reverse_range(jobl, jobr, l, mid, i << 1);
        }
        if (jobr > mid) {
            reverse_range(jobl, jobr, mid + 1, r, i << 1 | 1);
        }
        up(i, mid - l + 1, r - mid);
    }
}

int query_sum(int jobl, int jobr, int l, int r, int i) {
    if (jobl <= l && r <= jobr) {
        return sum[i];
    }
    int mid = (l + r) >> 1;
    down(i, mid - l + 1, r - mid);
    int ans = 0;
    if (jobl <= mid) {
        ans += query_sum(jobl, jobr, l, mid, i << 1);
    }
    if (jobr > mid) {
        ans += query_sum(jobl, jobr, mid + 1, r, i << 1 | 1);
    }
    return ans;
}

vector<int> query_longest(int jobl, int jobr, int l, int r, int i) {
    if (jobl <= l && r <= jobr) {
        return {len1[i], pre1[i], suf1[i]};
    } else {
        int mid = (l + r) >> 1;
        int ln = mid - l + 1;
        int rn = r - mid;
        down(i, ln, rn);
        if (jobr <= mid) {
            return query_longest(jobl, jobr, l, mid, i << 1);
        }
        if (jobl > mid) {
            return query_longest(jobl, jobr, mid + 1, r, i << 1 | 1);
        }
        vector<int> l3 = query_longest(jobl, jobr, l, mid, i << 1);
        vector<int> r3 = query_longest(jobl, jobr, mid + 1, r, i << 1 | 1);
        int llen = l3[0], lpre = l3[1], lsuf = l3[2];
        int rlen = r3[0], rpre = r3[1], rsuf = r3[2];
        int len = max({llen, rlen, lsuf + rpre});
        int pre = llen < mid - max(jobl, l) + 1 ? lpre : (lpre + rpre);
        int suf = rlen < min(r, jobr) - mid ? rsuf : (lsuf + rsuf);
        return {len, pre, suf};
    }
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(0);
    cout.tie(0);
    
    int n, m;
    cin >> n >> m;
    for (int i = 1; i <= n; i++) {
        cin >> arr[i];
    }
    build(1, n, 1);
    
    for (int i = 1; i <= m; i++) {
        int op, jobl, jobr;
        cin >> op >> jobl >> jobr;
        jobl++; jobr++; // 转换为1-based
        
        if (op == 0) {
            update_range(jobl, jobr, 0, 1, n, 1);
        } else if (op == 1) {
            update_range(jobl, jobr, 1, 1, n, 1);
        } else if (op == 2) {
            reverse_range(jobl, jobr, 1, n, 1);
        } else if (op == 3) {
            cout << query_sum(jobl, jobr, 1, n, 1) << "\n";
        } else {
            cout << query_longest(jobl, jobr, 1, n, 1)[0] << "\n";
        }
    }
    
    return 0;
}
```

### Python实现

```python
import sys
from collections import deque

class SegmentTree:
    def __init__(self, arr):
        self.n = len(arr)
        self.arr = arr
        self.sum = [0] * (4 * self.n)
        self.len0 = [0] * (4 * self.n)
        self.pre0 = [0] * (4 * self.n)
        self.suf0 = [0] * (4 * self.n)
        self.len1 = [0] * (4 * self.n)
        self.pre1 = [0] * (4 * self.n)
        self.suf1 = [0] * (4 * self.n)
        self.change = [0] * (4 * self.n)
        self.update = [False] * (4 * self.n)
        self.reverse = [False] * (4 * self.n)
        self.build(1, 1, self.n)
    
    def up(self, i, ln, rn):
        l = i << 1
        r = i << 1 | 1
        self.sum[i] = self.sum[l] + self.sum[r]
        self.len0[i] = max(self.len0[l], self.len0[r], self.suf0[l] + self.pre0[r])
        self.pre0[i] = self.pre0[l] if self.len0[l] < ln else self.pre0[l] + self.pre0[r]
        self.suf0[i] = self.suf0[r] if self.len0[r] < rn else self.suf0[l] + self.suf0[r]
        self.len1[i] = max(self.len1[l], self.len1[r], self.suf1[l] + self.pre1[r])
        self.pre1[i] = self.pre1[l] if self.len1[l] < ln else self.pre1[l] + self.pre1[r]
        self.suf1[i] = self.suf1[r] if self.len1[r] < rn else self.suf1[l] + self.suf1[r]
    
    def update_lazy(self, i, v, n):
        self.sum[i] = v * n
        self.len0[i] = self.pre0[i] = self.suf0[i] = n if v == 0 else 0
        self.len1[i] = self.pre1[i] = self.suf1[i] = n if v == 1 else 0
        self.change[i] = v
        self.update[i] = True
        self.reverse[i] = False
    
    def reverse_lazy(self, i, n):
        self.sum[i] = n - self.sum[i]
        self.len0[i], self.len1[i] = self.len1[i], self.len0[i]
        self.pre0[i], self.pre1[i] = self.pre1[i], self.pre0[i]
        self.suf0[i], self.suf1[i] = self.suf1[i], self.suf0[i]
        self.reverse[i] = not self.reverse[i]
    
    def down(self, i, ln, rn):
        if self.update[i]:
            self.update_lazy(i << 1, self.change[i], ln)
            self.update_lazy(i << 1 | 1, self.change[i], rn)
            self.update[i] = False
        if self.reverse[i]:
            self.reverse_lazy(i << 1, ln)
            self.reverse_lazy(i << 1 | 1, rn)
            self.reverse[i] = False
    
    def build(self, i, l, r):
        if l == r:
            self.sum[i] = self.arr[l]
            self.len0[i] = self.pre0[i] = self.suf0[i] = 1 if self.arr[l] == 0 else 0
            self.len1[i] = self.pre1[i] = self.suf1[i] = 1 if self.arr[l] == 1 else 0
        else:
            mid = (l + r) >> 1
            self.build(i << 1, l, mid)
            self.build(i << 1 | 1, mid + 1, r)
            self.up(i, mid - l + 1, r - mid)
        self.update[i] = False
        self.reverse[i] = False
    
    def update_range(self, jobl, jobr, jobv, l, r, i):
        if jobl <= l and r <= jobr:
            self.update_lazy(i, jobv, r - l + 1)
        else:
            mid = (l + r) >> 1
            ln = mid - l + 1
            rn = r - mid
            self.down(i, ln, rn)
            if jobl <= mid:
                self.update_range(jobl, jobr, jobv, l, mid, i << 1)
            if jobr > mid:
                self.update_range(jobl, jobr, jobv, mid + 1, r, i << 1 | 1)
            self.up(i, ln, rn)
    
    def reverse_range(self, jobl, jobr, l, r, i):
        if jobl <= l and r <= jobr:
            self.reverse_lazy(i, r - l + 1)
        else:
            mid = (l + r) >> 1
            ln = mid - l + 1
            rn = r - mid
            self.down(i, ln, rn)
            if jobl <= mid:
                self.reverse_range(jobl, jobr, l, mid, i << 1)
            if jobr > mid:
                self.reverse_range(jobl, jobr, mid + 1, r, i << 1 | 1)
            self.up(i, ln, rn)
    
    def query_sum(self, jobl, jobr, l, r, i):
        if jobl <= l and r <= jobr:
            return self.sum[i]
        mid = (l + r) >> 1
        ln = mid - l + 1
        rn = r - mid
        self.down(i, ln, rn)
        ans = 0
        if jobl <= mid:
            ans += self.query_sum(jobl, jobr, l, mid, i << 1)
        if jobr > mid:
            ans += self.query_sum(jobl, jobr, mid + 1, r, i << 1 | 1)
        return ans
    
    def query_longest(self, jobl, jobr, l, r, i):
        if jobl <= l and r <= jobr:
            return [self.len1[i], self.pre1[i], self.suf1[i]]
        else:
            mid = (l + r) >> 1
            ln = mid - l + 1
            rn = r - mid
            self.down(i, ln, rn)
            if jobr <= mid:
                return self.query_longest(jobl, jobr, l, mid, i << 1)
            if jobl > mid:
                return self.query_longest(jobl, jobr, mid + 1, r, i << 1 | 1)
            l3 = self.query_longest(jobl, jobr, l, mid, i << 1)
            r3 = self.query_longest(jobl, jobr, mid + 1, r, i << 1 | 1)
            llen, lpre, lsuf = l3[0], l3[1], l3[2]
            rlen, rpre, rsuf = r3[0], r3[1], r3[2]
            length = max(llen, rlen, lsuf + rpre)
            pre = lpre if llen < mid - max(jobl, l) + 1 else lpre + rpre
            suf = rsuf if rlen < min(r, jobr) - mid else lsuf + rsuf
            return [length, pre, suf]

def main():
    import sys
    input = sys.stdin.read
    data = input().split()
    
    idx = 0
    n = int(data[idx])
    idx += 1
    m = int(data[idx])
    idx += 1
    
    arr = [0] * (n + 1)
    for i in range(1, n + 1):
        arr[i] = int(data[idx])
        idx += 1
    
    seg_tree = SegmentTree(arr)
    
    results = []
    for _ in range(m):
        op = int(data[idx])
        idx += 1
        jobl = int(data[idx]) + 1  # 转换为1-based
        idx += 1
        jobr = int(data[idx]) + 1  # 转换为1-based
        idx += 1
        
        if op == 0:
            seg_tree.update_range(jobl, jobr, 0, 1, n, 1)
        elif op == 1:
            seg_tree.update_range(jobl, jobr, 1, 1, n, 1)
        elif op == 2:
            seg_tree.reverse_range(jobl, jobr, 1, n, 1)
        elif op == 3:
            results.append(str(seg_tree.query_sum(jobl, jobr, 1, n, 1)))
        else:
            results.append(str(seg_tree.query_longest(jobl, jobr, 1, n, 1)[0]))
    
    print('\n'.join(results))

if __name__ == "__main__":
    main()
```

### 复杂度分析

- **时间复杂度**：
  - 建树：O(n)
  - 单次操作：O(log n)
  - 总时间复杂度：O((n + m) log n)

- **空间复杂度**：O(n)

## 2. 最长LR交替子串 (Code02_LongestAlternateSubstring.java)

### 题目解析

给定一个字符串，初始全为'L'，每次操作翻转一个位置的字符，求每次操作后最长的LR交替子串长度。

### 解题思路

使用线段树维护每个区间的最长交替子串长度，以及前缀和后缀的最长交替长度。

### 关键技术点

1. 区间合并时需要判断中间连接处是否可以连接
2. 单点更新时需要重新计算区间信息

### Java实现

```java
package class113;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

public class Code02_LongestAlternateSubstring {

    public static int MAXN = 200001;

    // 原始数组
    public static int[] arr = new int[MAXN];

    // 交替最长子串长度
    public static int[] len = new int[MAXN << 2];

    // 交替最长前缀长度
    public static int[] pre = new int[MAXN << 2];

    // 交替最长后缀长度
    public static int[] suf = new int[MAXN << 2];

    public static void up(int l, int r, int i) {
        len[i] = Math.max(len[i << 1], len[i << 1 | 1]);
        pre[i] = pre[i << 1];
        suf[i] = suf[i << 1 | 1];
        int mid = (l + r) >> 1;
        int ln = mid - l + 1;
        int rn = r - mid;
        if (arr[mid] != arr[mid + 1]) {
            len[i] = Math.max(len[i], suf[i << 1] + pre[i << 1 | 1]);
            if (len[i << 1] == ln) {
                pre[i] = ln + pre[i << 1 | 1];
            }
            if (len[i << 1 | 1] == rn) {
                suf[i] = rn + suf[i << 1];
            }
        }
    }

    public static void build(int l, int r, int i) {
        if (l == r) {
            len[i] = 1;
            pre[i] = 1;
            suf[i] = 1;
        } else {
            int mid = (l + r) >> 1;
            build(l, mid, i << 1);
            build(mid + 1, r, i << 1 | 1);
            up(l, r, i);
        }
    }

    public static void reverse(int jobi, int l, int r, int i) {
        if (l == r) {
            arr[jobi] ^= 1;
        } else {
            int mid = (l + r) >> 1;
            if (jobi <= mid) {
                reverse(jobi, l, mid, i << 1);
            } else {
                reverse(jobi, mid + 1, r, i << 1 | 1);
            }
            up(l, r, i);
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        in.nextToken();
        int n = (int) in.nval;
        in.nextToken();
        int q = (int) in.nval;
        build(1, n, 1);
        for (int i = 1, index; i <= q; i++) {
            in.nextToken();
            index = (int) in.nval;
            reverse(index, 1, n, 1);
            out.println(len[1]);
        }
        out.flush();
        out.close();
        br.close();
    }

}
```

### C++实现

```cpp
#include <bits/stdc++.h>
using namespace std;

const int MAXN = 200001;

int arr[MAXN];
int len[MAXN << 2];
int pre[MAXN << 2];
int suf[MAXN << 2];

void up(int l, int r, int i) {
    len[i] = max(len[i << 1], len[i << 1 | 1]);
    pre[i] = pre[i << 1];
    suf[i] = suf[i << 1 | 1];
    int mid = (l + r) >> 1;
    int ln = mid - l + 1;
    int rn = r - mid;
    if (arr[mid] != arr[mid + 1]) {
        len[i] = max(len[i], suf[i << 1] + pre[i << 1 | 1]);
        if (len[i << 1] == ln) {
            pre[i] = ln + pre[i << 1 | 1];
        }
        if (len[i << 1 | 1] == rn) {
            suf[i] = rn + suf[i << 1];
        }
    }
}

void build(int l, int r, int i) {
    if (l == r) {
        len[i] = pre[i] = suf[i] = 1;
    } else {
        int mid = (l + r) >> 1;
        build(l, mid, i << 1);
        build(mid + 1, r, i << 1 | 1);
        up(l, r, i);
    }
}

void reverse_char(int jobi, int l, int r, int i) {
    if (l == r) {
        arr[jobi] ^= 1;
    } else {
        int mid = (l + r) >> 1;
        if (jobi <= mid) {
            reverse_char(jobi, l, mid, i << 1);
        } else {
            reverse_char(jobi, mid + 1, r, i << 1 | 1);
        }
        up(l, r, i);
    }
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(0);
    cout.tie(0);
    
    int n, q;
    cin >> n >> q;
    build(1, n, 1);
    
    for (int i = 1; i <= q; i++) {
        int index;
        cin >> index;
        reverse_char(index, 1, n, 1);
        cout << len[1] << "\n";
    }
    
    return 0;
}
```

### Python实现

```python
import sys

class AlternateSubstringTree:
    def __init__(self, n):
        self.n = n
        self.arr = [0] * (n + 1)  # 0 represents 'L', 1 represents 'R'
        self.len = [0] * (4 * n)
        self.pre = [0] * (4 * n)
        self.suf = [0] * (4 * n)
        self.build(1, 1, n)
    
    def up(self, l, r, i):
        self.len[i] = max(self.len[i << 1], self.len[i << 1 | 1])
        self.pre[i] = self.pre[i << 1]
        self.suf[i] = self.suf[i << 1 | 1]
        mid = (l + r) >> 1
        ln = mid - l + 1
        rn = r - mid
        if self.arr[mid] != self.arr[mid + 1]:
            self.len[i] = max(self.len[i], self.suf[i << 1] + self.pre[i << 1 | 1])
            if self.len[i << 1] == ln:
                self.pre[i] = ln + self.pre[i << 1 | 1]
            if self.len[i << 1 | 1] == rn:
                self.suf[i] = rn + self.suf[i << 1]
    
    def build(self, i, l, r):
        if l == r:
            self.len[i] = self.pre[i] = self.suf[i] = 1
        else:
            mid = (l + r) >> 1
            self.build(i << 1, l, mid)
            self.build(i << 1 | 1, mid + 1, r)
            self.up(l, r, i)
    
    def reverse_char(self, jobi, l, r, i):
        if l == r:
            self.arr[jobi] ^= 1
        else:
            mid = (l + r) >> 1
            if jobi <= mid:
                self.reverse_char(jobi, l, mid, i << 1)
            else:
                self.reverse_char(jobi, mid + 1, r, i << 1 | 1)
            self.up(l, r, i)

def main():
    import sys
    input = sys.stdin.read
    data = input().split()
    
    n = int(data[0])
    q = int(data[1])
    
    tree = AlternateSubstringTree(n)
    
    results = []
    idx = 2
    for _ in range(q):
        index = int(data[idx])
        idx += 1
        tree.reverse_char(index, 1, n, 1)
        results.append(str(tree.len[1]))
    
    print('\n'.join(results))

if __name__ == "__main__":
    main()
```

### 复杂度分析

- **时间复杂度**：
  - 建树：O(n)
  - 单次操作：O(log n)
  - 总时间复杂度：O(n + q log n)

- **空间复杂度**：O(n)

## 3. 地道相连的房子 (Code03_TunnelWarfare.java)

### 题目解析

有n个房子排成一排，相邻房子有地道连接。支持摧毁、恢复和查询操作。

### 解题思路

使用线段树维护每个区间的连续1的前缀和后缀长度，其中1表示房子未被摧毁。

### 关键技术点

1. 查询操作需要根据位置在区间中的位置进行不同处理
2. 区间合并时考虑跨区间的情况

### Java实现

```java
package class113;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

public class Code03_TunnelWarfare {

    public static int MAXN = 50001;

    // 连续1的最长前缀长度
    public static int[] pre = new int[MAXN << 2];

    // 连续1的最长后缀长度
    public static int[] suf = new int[MAXN << 2];

    // 摧毁的房屋编号入栈，以便执行恢复操作
    public static int[] stack = new int[MAXN];

    public static void up(int l, int r, int i) {
        pre[i] = pre[i << 1];
        suf[i] = suf[i << 1 | 1];
        int mid = (l + r) >> 1;
        if (pre[i << 1] == mid - l + 1) {
            pre[i] += pre[i << 1 | 1];
        }
        if (suf[i << 1 | 1] == r - mid) {
            suf[i] += suf[i << 1];
        }
    }

    public static void build(int l, int r, int i) {
        if (l == r) {
            pre[i] = suf[i] = 1;
        } else {
            int mid = (l + r) >> 1;
            build(l, mid, i << 1);
            build(mid + 1, r, i << 1 | 1);
            up(l, r, i);
        }
    }

    public static void update(int jobi, int jobv, int l, int r, int i) {
        if (l == r) {
            pre[i] = suf[i] = jobv;
        } else {
            int mid = (l + r) >> 1;
            if (jobi <= mid) {
                update(jobi, jobv, l, mid, i << 1);
            } else {
                update(jobi, jobv, mid + 1, r, i << 1 | 1);
            }
            up(l, r, i);
        }
    }

    // 已知jobi在l...r范围上
    // 返回jobi往两侧扩展出的最大长度
    // 递归需要遵循的潜台词 : 从jobi往两侧扩展，一定无法扩展到l...r范围之外！
    public static int query(int jobi, int l, int r, int i) {
        if (l == r) {
            return pre[i];
        } else {
            int mid = (l + r) >> 1;
            if (jobi <= mid) {
                if (jobi > mid - suf[i << 1]) {
                    return suf[i << 1] + pre[i << 1 | 1];
                } else {
                    return query(jobi, l, mid, i << 1);
                }
            } else {
                if (mid + pre[i << 1 | 1] >= jobi) {
                    return suf[i << 1] + pre[i << 1 | 1];
                } else {
                    return query(jobi, mid + 1, r, i << 1 | 1);
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        while (in.nextToken() != StreamTokenizer.TT_EOF) {
            int n = (int) in.nval;
            in.nextToken();
            int m = (int) in.nval;
            build(1, n, 1);
            String op;
            int stackSize = 0;
            for (int i = 1, x; i <= m; i++) {
                in.nextToken();
                op = in.sval;
                if (op.equals("D")) {
                    in.nextToken();
                    x = (int) in.nval;
                    update(x, 0, 1, n, 1);
                    stack[stackSize++] = x;
                } else if (op.equals("R")) {
                    update(stack[--stackSize], 1, 1, n, 1);
                } else {
                    in.nextToken();
                    x = (int) in.nval;
                    out.println(query(x, 1, n, 1));
                }
            }
        }
        out.flush();
        out.close();
        br.close();
    }

}
```

### C++实现

```cpp
#include <bits/stdc++.h>
using namespace std;

const int MAXN = 50001;

int pre[MAXN << 2];
int suf[MAXN << 2];
int stack_arr[MAXN];

void up(int l, int r, int i) {
    pre[i] = pre[i << 1];
    suf[i] = suf[i << 1 | 1];
    int mid = (l + r) >> 1;
    if (pre[i << 1] == mid - l + 1) {
        pre[i] += pre[i << 1 | 1];
    }
    if (suf[i << 1 | 1] == r - mid) {
        suf[i] += suf[i << 1];
    }
}

void build(int l, int r, int i) {
    if (l == r) {
        pre[i] = suf[i] = 1;
    } else {
        int mid = (l + r) >> 1;
        build(l, mid, i << 1);
        build(mid + 1, r, i << 1 | 1);
        up(l, r, i);
    }
}

void update(int jobi, int jobv, int l, int r, int i) {
    if (l == r) {
        pre[i] = suf[i] = jobv;
    } else {
        int mid = (l + r) >> 1;
        if (jobi <= mid) {
            update(jobi, jobv, l, mid, i << 1);
        } else {
            update(jobi, jobv, mid + 1, r, i << 1 | 1);
        }
        up(l, r, i);
    }
}

int query(int jobi, int l, int r, int i) {
    if (l == r) {
        return pre[i];
    } else {
        int mid = (l + r) >> 1;
        if (jobi <= mid) {
            if (jobi > mid - suf[i << 1]) {
                return suf[i << 1] + pre[i << 1 | 1];
            } else {
                return query(jobi, l, mid, i << 1);
            }
        } else {
            if (mid + pre[i << 1 | 1] >= jobi) {
                return suf[i << 1] + pre[i << 1 | 1];
            } else {
                return query(jobi, mid + 1, r, i << 1 | 1);
            }
        }
    }
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(0);
    cout.tie(0);
    
    int n, m;
    while (cin >> n >> m) {
        build(1, n, 1);
        int stack_arr[MAXN];
        int stackSize = 0;
        
        for (int i = 1; i <= m; i++) {
            string op;
            cin >> op;
            if (op == "D") {
                int x;
                cin >> x;
                update(x, 0, 1, n, 1);
                stack_arr[stackSize++] = x;
            } else if (op == "R") {
                update(stack_arr[--stackSize], 1, 1, n, 1);
            } else {
                int x;
                cin >> x;
                cout << query(x, 1, n, 1) << "\n";
            }
        }
    }
    
    return 0;
}
```

### Python实现

```python
import sys

class TunnelWarfareTree:
    def __init__(self, n):
        self.n = n
        self.pre = [0] * (4 * n)
        self.suf = [0] * (4 * n)
        self.build(1, 1, n)
    
    def up(self, l, r, i):
        self.pre[i] = self.pre[i << 1]
        self.suf[i] = self.suf[i << 1 | 1]
        mid = (l + r) >> 1
        if self.pre[i << 1] == mid - l + 1:
            self.pre[i] += self.pre[i << 1 | 1]
        if self.suf[i << 1 | 1] == r - mid:
            self.suf[i] += self.suf[i << 1]
    
    def build(self, i, l, r):
        if l == r:
            self.pre[i] = self.suf[i] = 1
        else:
            mid = (l + r) >> 1
            self.build(i << 1, l, mid)
            self.build(i << 1 | 1, mid + 1, r)
            self.up(l, r, i)
    
    def update(self, jobi, jobv, l, r, i):
        if l == r:
            self.pre[i] = self.suf[i] = jobv
        else:
            mid = (l + r) >> 1
            if jobi <= mid:
                self.update(jobi, jobv, l, mid, i << 1)
            else:
                self.update(jobi, jobv, mid + 1, r, i << 1 | 1)
            self.up(l, r, i)
    
    def query(self, jobi, l, r, i):
        if l == r:
            return self.pre[i]
        else:
            mid = (l + r) >> 1
            if jobi <= mid:
                if jobi > mid - self.suf[i << 1]:
                    return self.suf[i << 1] + self.pre[i << 1 | 1]
                else:
                    return self.query(jobi, l, mid, i << 1)
            else:
                if mid + self.pre[i << 1 | 1] >= jobi:
                    return self.suf[i << 1] + self.pre[i << 1 | 1]
                else:
                    return self.query(jobi, mid + 1, r, i << 1 | 1)

def main():
    import sys
    input = sys.stdin.read
    data = input().split()
    
    idx = 0
    while idx < len(data):
        n = int(data[idx])
        idx += 1
        m = int(data[idx])
        idx += 1
        
        tree = TunnelWarfareTree(n)
        stack_arr = []
        
        results = []
        for _ in range(m):
            op = data[idx]
            idx += 1
            
            if op == "D":
                x = int(data[idx])
                idx += 1
                tree.update(x, 0, 1, n, 1)
                stack_arr.append(x)
            elif op == "R":
                x = stack_arr.pop()
                tree.update(x, 1, 1, n, 1)
            else:  # op == "Q"
                x = int(data[idx])
                idx += 1
                results.append(str(tree.query(x, 1, n, 1)))
        
        print('\n'.join(results))

if __name__ == "__main__":
    main()
```

### 复杂度分析

- **时间复杂度**：
  - 建树：O(n)
  - 单次操作：O(log n)
  - 总时间复杂度：O((n + m) log n)

- **空间复杂度**：O(n)

## 4. 旅馆 (Code04_Hotel.java)

### 题目解析

有n个房间，初始都为空房。支持查找连续空房间和清空房间操作。

### 解题思路

使用线段树维护每个区间的连续空房间信息，包括最长连续空房间长度、前缀和后缀长度。

### 关键技术点

1. 查询最左边满足条件的区间需要特殊处理
2. 区间合并时需要考虑左右子区间的连接情况

### Java实现

```java
package class113;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;

public class Code04_Hotel {

    public static int MAXN = 50001;

    // 连续空房最长子串长度
    public static int[] len = new int[MAXN << 2];

    // 连续空房最长前缀长度
    public static int[] pre = new int[MAXN << 2];

    // 连续空房最长后缀长度
    public static int[] suf = new int[MAXN << 2];

    // 懒更新信息，范围上所有数字被重置成了什么
    public static int[] change = new int[MAXN << 2];

    // 懒更新信息，范围上有没有重置任务
    public static boolean[] update = new boolean[MAXN << 2];

    public static void up(int i, int ln, int rn) {
        int l = i << 1;
        int r = i << 1 | 1;
        len[i] = Math.max(Math.max(len[l], len[r]), suf[l] + pre[r]);
        pre[i] = len[l] < ln ? pre[l] : (pre[l] + pre[r]);
        suf[i] = len[r] < rn ? suf[r] : (suf[l] + suf[r]);
    }

    public static void down(int i, int ln, int rn) {
        if (update[i]) {
            lazy(i << 1, change[i], ln);
            lazy(i << 1 | 1, change[i], rn);
            update[i] = false;
        }
    }

    public static void lazy(int i, int v, int n) {
        len[i] = pre[i] = suf[i] = v == 0 ? n : 0;
        change[i] = v;
        update[i] = true;
    }

    public static void build(int l, int r, int i) {
        if (l == r) {
            len[i] = pre[i] = suf[i] = 1;
        } else {
            int mid = (l + r) >> 1;
            build(l, mid, i << 1);
            build(mid + 1, r, i << 1 | 1);
            up(i, mid - l + 1, r - mid);
        }
        update[i] = false;
    }

    public static void update(int jobl, int jobr, int jobv, int l, int r, int i) {
        if (jobl <= l && r <= jobr) {
            lazy(i, jobv, r - l + 1);
        } else {
            int mid = (l + r) >> 1;
            down(i, mid - l + 1, r - mid);
            if (jobl <= mid) {
                update(jobl, jobr, jobv, l, mid, i << 1);
            }
            if (jobr > mid) {
                update(jobl, jobr, jobv, mid + 1, r, i << 1 | 1);
            }
            up(i, mid - l + 1, r - mid);
        }
    }

    // 在l..r范围上，在满足空房长度>=x的情况下，返回尽量靠左的开头位置
    // 递归需要遵循的潜台词 : l..r范围上一定存在连续空房长度>=x的区域
    public static int queryLeft(int x, int l, int r, int i) {
        if (l == r) {
            return l;
        } else {
            int mid = (l + r) >> 1;
            down(i, mid - l + 1, r - mid);
            // 最先查左边
            if (len[i << 1] >= x) {
                return queryLeft(x, l, mid, i << 1);
            }
            // 然后查中间向两边扩展的可能区域
            if (suf[i << 1] + pre[i << 1 | 1] >= x) {
                return mid - suf[i << 1] + 1;
            }
            // 前面都没有再最后查右边
            return queryLeft(x, mid + 1, r, i << 1 | 1);
        }
    }

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StreamTokenizer in = new StreamTokenizer(br);
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        in.nextToken();
        int n = (int) in.nval;
        build(1, n, 1);
        in.nextToken();
        int m = (int) in.nval;
        for (int i = 1, op, x, y, left; i <= m; i++) {
            in.nextToken();
            op = (int) in.nval;
            if (op == 1) {
                in.nextToken();
                x = (int) in.nval;
                if (len[1] < x) {
                    left = 0;
                } else {
                    left = queryLeft(x, 1, n, 1);
                    update(left, left + x - 1, 1, 1, n, 1);
                }
                out.println(left);
            } else {
                in.nextToken();
                x = (int) in.nval;
                in.nextToken();
                y = (int) in.nval;
                update(x, Math.min(x + y - 1, n), 0, 1, n, 1);
            }
        }
        out.flush();
        out.close();
        br.close();
    }

}
```

### C++实现

```cpp
#include <bits/stdc++.h>
using namespace std;

const int MAXN = 50001;

int len[MAXN << 2];
int pre[MAXN << 2];
int suf[MAXN << 2];
int change[MAXN << 2];
bool update[MAXN << 2];

void up(int i, int ln, int rn) {
    int l = i << 1;
    int r = i << 1 | 1;
    len[i] = max({len[l], len[r], suf[l] + pre[r]});
    pre[i] = len[l] < ln ? pre[l] : (pre[l] + pre[r]);
    suf[i] = len[r] < rn ? suf[r] : (suf[l] + suf[r]);
}

void down(int i, int ln, int rn) {
    if (update[i]) {
        int l = i << 1, r = i << 1 | 1;
        len[l] = pre[l] = suf[l] = change[i] == 0 ? ln : 0;
        change[l] = change[i];
        update[l] = true;
        
        len[r] = pre[r] = suf[r] = change[i] == 0 ? rn : 0;
        change[r] = change[i];
        update[r] = true;
        
        update[i] = false;
    }
}

void lazy_update(int i, int v, int n) {
    len[i] = pre[i] = suf[i] = v == 0 ? n : 0;
    change[i] = v;
    update[i] = true;
}

void build(int l, int r, int i) {
    if (l == r) {
        len[i] = pre[i] = suf[i] = 1;
    } else {
        int mid = (l + r) >> 1;
        build(l, mid, i << 1);
        build(mid + 1, r, i << 1 | 1);
        up(i, mid - l + 1, r - mid);
    }
    update[i] = false;
}

void update_range(int jobl, int jobr, int jobv, int l, int r, int i) {
    if (jobl <= l && r <= jobr) {
        lazy_update(i, jobv, r - l + 1);
    } else {
        int mid = (l + r) >> 1;
        down(i, mid - l + 1, r - mid);
        if (jobl <= mid) {
            update_range(jobl, jobr, jobv, l, mid, i << 1);
        }
        if (jobr > mid) {
            update_range(jobl, jobr, jobv, mid + 1, r, i << 1 | 1);
        }
        up(i, mid - l + 1, r - mid);
    }
}

int query_left(int x, int l, int r, int i) {
    if (l == r) {
        return l;
    } else {
        int mid = (l + r) >> 1;
        down(i, mid - l + 1, r - mid);
        // 最先查左边
        if (len[i << 1] >= x) {
            return query_left(x, l, mid, i << 1);
        }
        // 然后查中间向两边扩展的可能区域
        if (suf[i << 1] + pre[i << 1 | 1] >= x) {
            return mid - suf[i << 1] + 1;
        }
        // 前面都没有再最后查右边
        return query_left(x, mid + 1, r, i << 1 | 1);
    }
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(0);
    cout.tie(0);
    
    int n, m;
    cin >> n;
    build(1, n, 1);
    cin >> m;
    
    for (int i = 1; i <= m; i++) {
        int op;
        cin >> op;
        if (op == 1) {
            int x;
            cin >> x;
            int left;
            if (len[1] < x) {
                left = 0;
            } else {
                left = query_left(x, 1, n, 1);
                update_range(left, left + x - 1, 1, 1, n, 1);
            }
            cout << left << "\n";
        } else {
            int x, y;
            cin >> x >> y;
            update_range(x, min(x + y - 1, n), 0, 1, n, 1);
        }
    }
    
    return 0;
}
```

### Python实现

```python
import sys

class HotelTree:
    def __init__(self, n):
        self.n = n
        self.len = [0] * (4 * n)
        self.pre = [0] * (4 * n)
        self.suf = [0] * (4 * n)
        self.change = [0] * (4 * n)
        self.update = [False] * (4 * n)
        self.build(1, 1, n)
    
    def up(self, i, ln, rn):
        l = i << 1
        r = i << 1 | 1
        self.len[i] = max(self.len[l], self.len[r], self.suf[l] + self.pre[r])
        self.pre[i] = self.pre[l] if self.len[l] < ln else self.pre[l] + self.pre[r]
        self.suf[i] = self.suf[r] if self.len[r] < rn else self.suf[l] + self.suf[r]
    
    def down(self, i, ln, rn):
        if self.update[i]:
            l = i << 1
            r = i << 1 | 1
            self.len[l] = self.pre[l] = self.suf[l] = 0 if self.change[i] == 1 else ln
            self.change[l] = self.change[i]
            self.update[l] = True
            
            self.len[r] = self.pre[r] = self.suf[r] = 0 if self.change[i] == 1 else rn
            self.change[r] = self.change[i]
            self.update[r] = True
            
            self.update[i] = False
    
    def lazy_update(self, i, v, n):
        self.len[i] = self.pre[i] = self.suf[i] = 0 if v == 1 else n
        self.change[i] = v
        self.update[i] = True
    
    def build(self, i, l, r):
        if l == r:
            self.len[i] = self.pre[i] = self.suf[i] = 1
        else:
            mid = (l + r) >> 1
            self.build(i << 1, l, mid)
            self.build(i << 1 | 1, mid + 1, r)
            self.up(i, mid - l + 1, r - mid)
        self.update[i] = False
    
    def update_range(self, jobl, jobr, jobv, l, r, i):
        if jobl <= l and r <= jobr:
            self.lazy_update(i, jobv, r - l + 1)
        else:
            mid = (l + r) >> 1
            ln = mid - l + 1
            rn = r - mid
            self.down(i, ln, rn)
            if jobl <= mid:
                self.update_range(jobl, jobr, jobv, l, mid, i << 1)
            if jobr > mid:
                self.update_range(jobl, jobr, jobv, mid + 1, r, i << 1 | 1)
            self.up(i, ln, rn)
    
    def query_left(self, x, l, r, i):
        if l == r:
            return l
        else:
            mid = (l + r) >> 1
            ln = mid - l + 1
            rn = r - mid
            self.down(i, ln, rn)
            # 最先查左边
            if self.len[i << 1] >= x:
                return self.query_left(x, l, mid, i << 1)
            # 然后查中间向两边扩展的可能区域
            if self.suf[i << 1] + self.pre[i << 1 | 1] >= x:
                return mid - self.suf[i << 1] + 1
            # 前面都没有再最后查右边
            return self.query_left(x, mid + 1, r, i << 1 | 1)

def main():
    import sys
    input = sys.stdin.read
    data = input().split()
    
    n = int(data[0])
    m = int(data[1])
    
    tree = HotelTree(n)
    
    results = []
    idx = 2
    for _ in range(m):
        op = int(data[idx])
        idx += 1
        
        if op == 1:
            x = int(data[idx])
            idx += 1
            if tree.len[1] < x:
                left = 0
            else:
                left = tree.query_left(x, 1, n, 1)
                tree.update_range(left, left + x - 1, 1, 1, n, 1)
            results.append(str(left))
        else:
            x = int(data[idx])
            idx += 1
            y = int(data[idx])
            idx += 1
            tree.update_range(x, min(x + y - 1, n), 0, 1, n, 1)
    
    print('\n'.join(results))

if __name__ == "__main__":
    main()
```

### 复杂度分析

- **时间复杂度**：
  - 建树：O(n)
  - 单次操作：O(log n)
  - 总时间复杂度：O((n + m) log n)

- **空间复杂度**：O(n)

## 📈 总结

线段树是一种非常强大的数据结构，可以高效地处理各种区间操作问题。通过以上四个题目的实现，我们可以看到线段树在不同场景下的应用：

1. **多标记维护**：在序列操作问题中，我们需要同时维护多种信息和多种懒标记
2. **区间合并**：在最长交替子串问题中，需要仔细处理区间合并逻辑
3. **查询策略**：在地道相连问题中，需要根据位置进行不同的查询策略
4. **最值查询**：在旅馆问题中，需要查找满足条件的最左位置

掌握线段树的关键在于：
1. 根据题目需求设计节点信息
2. 正确实现区间合并逻辑
3. 合理使用懒标记优化
4. 熟练掌握各种查询和更新策略

## 📚 补充题目详解

### LeetCode 307. 区域和检索 - 数组可修改

#### 题目解析
实现一个支持更新和区间求和操作的数据结构。

#### 解题思路
使用线段树维护区间和，支持单点更新和区间查询。

#### Java实现

```java
public class NumArray {
    private int[] tree;
    private int[] nums;
    private int n;
    
    public NumArray(int[] nums) {
        this.n = nums.length;
        this.nums = nums;
        this.tree = new int[4 * n];
        build(0, 0, n - 1);
    }
    
    private void build(int node, int start, int end) {
        if (start == end) {
            tree[node] = nums[start];
        } else {
            int mid = (start + end) / 2;
            build(2 * node + 1, start, mid);
            build(2 * node + 2, mid + 1, end);
            tree[node] = tree[2 * node + 1] + tree[2 * node + 2];
        }
    }
    
    public void update(int index, int val) {
        update(0, 0, n - 1, index, val);
    }
    
    private void update(int node, int start, int end, int index, int val) {
        if (start == end) {
            nums[index] = val;
            tree[node] = val;
        } else {
            int mid = (start + end) / 2;
            if (index <= mid) {
                update(2 * node + 1, start, mid, index, val);
            } else {
                update(2 * node + 2, mid + 1, end, index, val);
            }
            tree[node] = tree[2 * node + 1] + tree[2 * node + 2];
        }
    }
    
    public int sumRange(int left, int right) {
        return query(0, 0, n - 1, left, right);
    }
    
    private int query(int node, int start, int end, int left, int right) {
        if (right < start || end < left) {
            return 0;
        }
        if (left <= start && end <= right) {
            return tree[node];
        }
        int mid = (start + end) / 2;
        int leftSum = query(2 * node + 1, start, mid, left, right);
        int rightSum = query(2 * node + 2, mid + 1, end, left, right);
        return leftSum + rightSum;
    }
}
```

#### 复杂度分析
- 时间复杂度：
  - 构建：O(n)
  - 更新：O(log n)
  - 查询：O(log n)
- 空间复杂度：O(n)

### LeetCode 315. 计算右侧小于当前元素的个数

#### 题目解析
给定一个整数数组 nums，返回一个新数组 counts，其中 counts[i] 是 nums[i] 右侧小于 nums[i] 的元素的数量。

#### 解题思路
使用离散化+线段树的方法。从右往左遍历数组，用线段树维护每个值出现的次数，查询小于当前值的元素个数。

#### Java实现

```java
import java.util.*;

public class Solution {
    public List<Integer> countSmaller(int[] nums) {
        // 离散化
        int[] sorted = nums.clone();
        Arrays.sort(sorted);
        Map<Integer, Integer> ranks = new HashMap<>();
        int rank = 0;
        for (int num : sorted) {
            if (!ranks.containsKey(num)) {
                ranks.put(num, ++rank);
            }
        }
        
        // 使用线段树
        SegmentTree tree = new SegmentTree(ranks.size());
        List<Integer> result = new ArrayList<>();
        
        // 从右往左遍历
        for (int i = nums.length - 1; i >= 0; i--) {
            int r = ranks.get(nums[i]);
            tree.update(1, 1, ranks.size(), r, 1);
            result.add(tree.query(1, 1, ranks.size(), 1, r - 1));
        }
        
        Collections.reverse(result);
        return result;
    }
    
    class SegmentTree {
        private int[] tree;
        private int n;
        
        public SegmentTree(int size) {
            this.n = size;
            this.tree = new int[4 * (size + 1)];
        }
        
        public void update(int node, int start, int end, int index, int val) {
            if (start == end) {
                tree[node] += val;
            } else {
                int mid = (start + end) / 2;
                if (index <= mid) {
                    update(2 * node, start, mid, index, val);
                } else {
                    update(2 * node + 1, mid + 1, end, index, val);
                }
                tree[node] = tree[2 * node] + tree[2 * node + 1];
            }
        }
        
        public int query(int node, int start, int end, int left, int right) {
            if (left > end || right < start) {
                return 0;
            }
            if (left <= start && end <= right) {
                return tree[node];
            }
            int mid = (start + end) / 2;
            int leftSum = query(2 * node, start, mid, left, right);
            int rightSum = query(2 * node + 1, mid + 1, end, left, right);
            return leftSum + rightSum;
        }
    }
}
```

#### 复杂度分析
- 时间复杂度：O(n log n)
- 空间复杂度：O(n)

### 洛谷 P3372 【模板】线段树 1

#### 题目解析
实现一个支持区间加法和区间求和的线段树。

#### 解题思路
使用带懒标记的线段树，支持区间更新和区间查询。

#### Java实现

```java
import java.io.*;
import java.util.*;

public class Main {
    static long[] tree;
    static long[] lazy;
    static long[] arr;
    static int n, m;
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        n = Integer.parseInt(st.nextToken());
        m = Integer.parseInt(st.nextToken());
        
        arr = new long[n + 1];
        tree = new long[4 * n];
        lazy = new long[4 * n];
        
        st = new StringTokenizer(br.readLine());
        for (int i = 1; i <= n; i++) {
            arr[i] = Long.parseLong(st.nextToken());
        }
        
        build(1, 1, n);
        
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < m; i++) {
            st = new StringTokenizer(br.readLine());
            int op = Integer.parseInt(st.nextToken());
            if (op == 1) {
                int x = Integer.parseInt(st.nextToken());
                int y = Integer.parseInt(st.nextToken());
                long k = Long.parseLong(st.nextToken());
                update(1, 1, n, x, y, k);
            } else {
                int x = Integer.parseInt(st.nextToken());
                int y = Integer.parseInt(st.nextToken());
                sb.append(query(1, 1, n, x, y)).append("\n");
            }
        }
        
        System.out.print(sb);
    }
    
    static void build(int node, int start, int end) {
        if (start == end) {
            tree[node] = arr[start];
        } else {
            int mid = (start + end) / 2;
            build(2 * node, start, mid);
            build(2 * node + 1, mid + 1, end);
            tree[node] = tree[2 * node] + tree[2 * node + 1];
        }
    }
    
    static void pushDown(int node, int start, int end) {
        if (lazy[node] != 0) {
            int mid = (start + end) / 2;
            tree[2 * node] += lazy[node] * (mid - start + 1);
            tree[2 * node + 1] += lazy[node] * (end - mid);
            lazy[2 * node] += lazy[node];
            lazy[2 * node + 1] += lazy[node];
            lazy[node] = 0;
        }
    }
    
    static void update(int node, int start, int end, int l, int r, long val) {
        if (l <= start && end <= r) {
            tree[node] += val * (end - start + 1);
            lazy[node] += val;
        } else {
            pushDown(node, start, end);
            int mid = (start + end) / 2;
            if (l <= mid) update(2 * node, start, mid, l, r, val);
            if (r > mid) update(2 * node + 1, mid + 1, end, l, r, val);
            tree[node] = tree[2 * node] + tree[2 * node + 1];
        }
    }
    
    static long query(int node, int start, int end, int l, int r) {
        if (l <= start && end <= r) {
            return tree[node];
        }
        pushDown(node, start, end);
        int mid = (start + end) / 2;
        long sum = 0;
        if (l <= mid) sum += query(2 * node, start, mid, l, r);
        if (r > mid) sum += query(2 * node + 1, mid + 1, end, l, r);
        return sum;
    }
}
```

#### 复杂度分析
- 时间复杂度：
  - 构建：O(n)
  - 更新：O(log n)
  - 查询：O(log n)
- 空间复杂度：O(n)

## 🎯 线段树进阶应用

### 1. 二维线段树
用于处理二维平面上的区间查询和更新问题。

### 2. 动态开点线段树
适用于数据范围很大但实际使用较少的情况，避免预先开满数组。

### 3. 可持久化线段树（主席树）
支持保存历史版本信息，可以查询历史状态。

### 4. 扫描线 + 线段树
用于解决平面几何问题，如矩形面积并、周长并等。

### 5. 树链剖分 + 线段树
用于处理树上路径操作问题。

## 🧠 学习建议

1. **掌握基础**：熟练掌握线段树的基本操作和懒标记技术
2. **多做练习**：通过大量练习掌握不同题型的解法
3. **理解变种**：学习线段树的各种变种和高级应用
4. **工程实践**：将线段树应用到实际项目中，理解其工程化考量

## 📚 参考资料

1. 《算法竞赛进阶指南》- 李煜东
2. 《挑战程序设计竞赛》- 秋叶拓哉等
3. TopCoder数据结构教程
4. Codeforces Educational Round相关讲解