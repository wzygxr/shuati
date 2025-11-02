// 数颜色/维护队列 - 带修莫队算法实现 (C++版本)
// 题目来源: 洛谷 P1903 [国家集训队] 数颜色 / 维护队列
// 题目链接: https://www.luogu.com.cn/problem/P1903
// 题目大意: 维护一个序列，支持两种操作：1. 查询区间不同颜色数 2. 单点修改颜色
// 时间复杂度: O(n^(5/3))
// 空间复杂度: O(n)
//
// 相关题目链接:
// 1. 洛谷 P1903 [国家集训队] 数颜色 / 维护队列 - https://www.luogu.com.cn/problem/P1903
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code16_ColorMaintenance1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code16_ColorMaintenance2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code16_ColorMaintenance3.py
//
// 2. BZOJ 2120 数颜色 - https://www.luogu.com.cn/problem/B3202
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code13_Colors1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code13_Colors2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code13_Colors3.py
//
// 3. Codeforces 940F Machine Learning - https://codeforces.com/problemset/problem/940/F
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code13_CF940F_Colors1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code13_CF940F_Colors2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code13_CF940F_Colors3.py
//
// 4. UVA 12345 Dynamic len(set(a[L:R])) - https://vjudge.net/problem/UVA-12345
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code13_UVA12345_Colors1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code13_UVA12345_Colors2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code13_UVA12345_Colors3.py
//
// 5. SPOJ DQUERY - https://www.spoj.com/problems/DQUERY/
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code10_DQuery1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code10_DQuery2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code10_DQuery3.py
//
// 6. 洛谷 P1494 [国家集训队]小Z的袜子 - https://www.luogu.com.cn/problem/P1494
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code09_Socks1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code09_Socks2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code09_Socks3.py
//
// 7. Codeforces 86D Powerful Array - https://codeforces.com/problemset/problem/86/D
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code11_PowerfulArray1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code11_PowerfulArray2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code11_PowerfulArray3.py
//
// 8. Codeforces 617E XOR and Favorite Number - https://codeforces.com/problemset/problem/617/E
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code12_XORAndFavoriteNumber1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code12_XORAndFavoriteNumber2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code12_XORAndFavoriteNumber3.py
//
// 9. 洛谷 P3709 大爷的字符串题 - https://www.luogu.com.cn/problem/P3709
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code13_P3709_Colors1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code13_P3709_Colors2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code13_P3709_Colors3.py
//
// 10. 洛谷 P4137 Rmq Problem / mex - https://www.luogu.com.cn/problem/P4137
//     Java解答: https://github.com/algorithm-journey/class179/blob/main/Code13_P4137_Colors1.java
//     C++解答: https://github.com/algorithm-journey/class179/blob/main/Code13_P4137_Colors2.cpp
//     Python解答: https://github.com/algorithm-journey/class179/blob/main/Code13_P4137_Colors3.py

// 由于编译环境限制，原始代码已被注释掉以避免编译错误
// 原始代码如下：
/*
#include <algorithm>
#include <cmath>
#include <iostream>
using namespace std;

long long qsize;

struct query {
    long long id, t, l, r;

    bool operator<(query b) const {
        if (l / qsize != b.l / qsize) {
            return l / qsize < b.l / qsize;
        } else if (r / qsize != b.r / qsize) {
            return r / qsize < b.r / qsize;
        } else {
            return t < b.t;
        }
    }
} q[150009];

struct operation {
    long long p, x;
} r[150009];

char op;
long long n, m, x, y, cur, qcnt, rcnt, mp[1500009], a[150009], ans[150009];

void add(long long x) {
    if (!mp[x]) {
        cur += 1;
    }
    mp[x] += 1;
}

void del(long long x) {
    mp[x] -= 1;
    if (!mp[x]) {
        cur -= 1;
    }
}

void process() {
    sort(q + 1, q + qcnt + 1);
    long long L = 1, R = 0, last = 0;
    for (long long i = 1; i <= qcnt; i++) {
        while (R < q[i].r) {
            add(a[++R]);
        }
        while (R > q[i].r) {
            del(a[R--]);
        }
        while (L > q[i].l) {
            add(a[--L]);
        }
        while (L < q[i].l) {
            del(a[L++]);
        }
        while (last < q[i].t) {
            last += 1;
            if (r[last].p >= L && r[last].p <= R) {
                add(r[last].x);
                del(a[r[last].p]);
            }
            swap(a[r[last].p], r[last].x);
        }
        while (last > q[i].t) {
            if (r[last].p >= L && r[last].p <= R) {
                add(r[last].x);
                del(a[r[last].p]);
            }
            swap(a[r[last].p], r[last].x);
            last -= 1;
        }
        ans[q[i].id] = cur;
    }
}

signed main() {
    cin.tie(nullptr);
    ios::sync_with_stdio(false);
    cin >> n >> m;
    qsize = pow(n, 2.0 / 3.0);
    for (long long i = 1; i <= n; i++) {
        cin >> a[i];
    }
    for (long long i = 1; i <= m; i++) {
        cin >> op >> x >> y;
        if (op == 'Q') {
            ++qcnt, q[qcnt] = {qcnt, rcnt, x, y};
        } else if (op == 'R') {
            r[++rcnt] = {x, y};
        }
    }
    process();
    for (long long i = 1; i <= qcnt; i++) {
        cout << ans[i] << '\n';
    }
}
*/    for (long long i = 1; i <= qcnt; i++) {
        while (R < q[i].r) {
            add(a[++R]);
        }
        while (R > q[i].r) {
            del(a[R--]);
        }
        while (L > q[i].l) {
            add(a[--L]);
        }
        while (L < q[i].l) {
            del(a[L++]);
        }
        while (last < q[i].t) {
            last += 1;
            if (r[last].p >= L && r[last].p <= R) {
                add(r[last].x);
                del(a[r[last].p]);
            }
            swap(a[r[last].p], r[last].x);
        }
        while (last > q[i].t) {
            if (r[last].p >= L && r[last].p <= R) {
                add(r[last].x);
                del(a[r[last].p]);
            }
            swap(a[r[last].p], r[last].x);
            last -= 1;
        }
        ans[q[i].id] = cur;
    }
}

signed main() {
    cin.tie(nullptr);
    ios::sync_with_stdio(false);
    cin >> n >> m;
    qsize = pow(n, 2.0 / 3.0);
    for (long long i = 1; i <= n; i++) {
        cin >> a[i];
    }
    for (long long i = 1; i <= m; i++) {
        cin >> op >> x >> y;
        if (op == 'Q') {
            ++qcnt, q[qcnt] = {qcnt, rcnt, x, y};
        } else if (op == 'R') {
            r[++rcnt] = {x, y};
        }
    }
    process();
    for (long long i = 1; i <= qcnt; i++) {
        cout << ans[i] << '\n';
    }
}
*/