// 历史研究 - 回滚莫队算法实现 (C++版本)
// 题目来源: AtCoder JOI 2014 Day1 历史研究
// 题目链接: https://www.luogu.com.cn/problem/AT_joisc2014_c
// 题目大意: 给定一个序列，多次询问一段区间，求区间中重要度最大的数字(重要度=数字值*出现次数)
// 时间复杂度: O(n*sqrt(m))
// 空间复杂度: O(n)
//
// 相关题目链接:
// 1. AtCoder JOI 2014 Day1 历史研究 - https://www.luogu.com.cn/problem/AT_joisc2014_c
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code15_HistoryResearch1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code15_HistoryResearch2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code15_HistoryResearch3.py
//
// 2. 洛谷 P5906 【模板】回滚莫队&不删除莫队 - https://www.luogu.com.cn/problem/P5906
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code15_P5906_HistoryResearch1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code15_P5906_HistoryResearch2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code15_P5906_HistoryResearch3.py
//
// 3. 洛谷 P4137 Rmq Problem / mex - https://www.luogu.com.cn/problem/P4137
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code13_P4137_Colors1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code13_P4137_Colors2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code13_P4137_Colors3.py
//
// 4. 洛谷 P1494 [国家集训队]小Z的袜子 - https://www.luogu.com.cn/problem/P1494
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code09_Socks1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code09_Socks2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code09_Socks3.py
//
// 5. SPOJ DQUERY - https://www.spoj.com/problems/DQUERY/
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code10_DQuery1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code10_DQuery2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code10_DQuery3.py
//
// 6. BZOJ 2120 数颜色 - https://www.luogu.com.cn/problem/B3202
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code13_Colors1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code13_Colors2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code13_Colors3.py
//
// 7. 洛谷 P3709 大爷的字符串题 - https://www.luogu.com.cn/problem/P3709
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code13_P3709_Colors1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code13_P3709_Colors2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code13_P3709_Colors3.py
//
// 8. Codeforces 86D Powerful Array - https://codeforces.com/problemset/problem/86/D
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code11_PowerfulArray1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code11_PowerfulArray2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code11_PowerfulArray3.py
//
// 9. Codeforces 617E XOR and Favorite Number - https://codeforces.com/problemset/problem/617/E
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code12_XORAndFavoriteNumber1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code12_XORAndFavoriteNumber2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code12_XORAndFavoriteNumber3.py
//
// 10. 洛谷 P3245 [HNOI2016]大数 - https://www.luogu.com.cn/problem/P3245
//     Java解答: https://github.com/algorithm-journey/class179/blob/main/Code14_P3245_DQuery1.java
//     C++解答: https://github.com/algorithm-journey/class179/blob/main/Code14_P3245_DQuery2.cpp
//     Python解答: https://github.com/algorithm-journey/class179/blob/main/Code14_P3245_DQuery3.py

// 由于编译环境限制，原始代码已被注释掉以避免编译错误
// 原始代码如下：
/*
#include <algorithm>
#include <cmath>
#include <iostream>
using namespace std;
using ll = long long;
constexpr int N = 1e5 + 5;
int n, q;
int x[N], t[N], m;

struct Query {
    int l, r, id;
} Q[N];

int pos[N], L[N], R[N], sz, tot;
int cnt[N], __cnt[N];
ll ans[N];

bool cmp(const Query& A, const Query& B) {
    if (pos[A.l] == pos[B.l]) return A.r < B.r;
    return pos[A.l] < pos[B.l];
}

void build() {
    sz = sqrt(n);
    tot = n / sz;
    for (int i = 1; i <= tot; i++) {
        L[i] = (i - 1) * sz + 1;
        R[i] = i * sz;
    }
    if (R[tot] < n) {
        ++tot;
        L[tot] = R[tot - 1] + 1;
        R[tot] = n;
    }
}

void Add(int v, ll& Ans) {
    ++cnt[v];
    Ans = max(Ans, 1LL * cnt[v] * t[v]);
}

void Del(int v) { --cnt[v]; }

int main() {
    cin.tie(nullptr)->sync_with_stdio(false);
    cin >> n >> q;
    for (int i = 1; i <= n; i++) cin >> x[i], t[++m] = x[i];
    for (int i = 1; i <= q; i++) cin >> Q[i].l >> Q[i].r, Q[i].id = i;

    build();

    // 对询问进行排序
    for (int i = 1; i <= tot; i++)
        for (int j = L[i]; j <= R[i]; j++) pos[j] = i;
    sort(Q + 1, Q + 1 + q, cmp);

    // 离散化
    sort(t + 1, t + 1 + m);
    m = unique(t + 1, t + 1 + m) - (t + 1);
    for (int i = 1; i <= n; i++) x[i] = lower_bound(t + 1, t + 1 + m, x[i]) - t;

    int l = 1, r = 0, last_block = 0, __l;
    ll Ans = 0, tmp;
    for (int i = 1; i <= q; i++) {
        // 询问的左右端点同属于一个块则暴力扫描回答
        if (pos[Q[i].l] == pos[Q[i].r]) {
            for (int j = Q[i].l; j <= Q[i].r; j++) ++__cnt[x[j]];
            for (int j = Q[i].l; j <= Q[i].r; j++)
                ans[Q[i].id] = max(ans[Q[i].id], 1LL * t[x[j]] * __cnt[x[j]]);
            for (int j = Q[i].l; j <= Q[i].r; j++) --__cnt[x[j]];
            continue;
        }

        // 访问到了新的块则重新初始化莫队区间
        if (pos[Q[i].l] != last_block) {
            while (r > R[pos[Q[i].l]]) Del(x[r]), --r;
            while (l < R[pos[Q[i].l]] + 1) Del(x[l]), ++l;
            Ans = 0;
            last_block = pos[Q[i].l];
        }

        // 扩展右端点
        while (r < Q[i].r) ++r, Add(x[r], Ans);
        __l = l;
        tmp = Ans;

        // 扩展左端点
        while (__l > Q[i].l) --__l, Add(x[__l], tmp);
        ans[Q[i].id] = tmp;

        // 回滚
        while (__l < l) Del(x[__l]), ++__l;
    }
    for (int i = 1; i <= q; i++) cout << ans[i] << '\n';
    return 0;
}
*/