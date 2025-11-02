// D-query - 普通莫队算法实现 (C++版本)
// 题目来源: SPOJ DQUERY - D-query
// 题目链接: https://www.spoj.com/problems/DQUERY/
// 题目大意: 给定一个长度为n的数组，每次查询区间[l,r]内有多少种不同的数字
// 时间复杂度: O(n*sqrt(n))
// 空间复杂度: O(n)
//
// 相关题目链接:
// 1. 洛谷 P1972 [SDOI2009] HH的项链 - https://www.luogu.com.cn/problem/P1972
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code14_DQuery1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code14_DQuery2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code14_DQuery3.py
//
// 2. Codeforces 86D Powerful Array - https://codeforces.com/problemset/problem/86/D
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code11_PowerfulArray1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code11_PowerfulArray2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code11_PowerfulArray3.py
//
// 3. Codeforces 617E XOR and Favorite Number - https://codeforces.com/problemset/problem/617/E
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code12_XORAndFavoriteNumber1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code12_XORAndFavoriteNumber2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code12_XORAndFavoriteNumber3.py
//
// 4. 洛谷 P1494 [国家集训队]小Z的袜子 - https://www.luogu.com.cn/problem/P1494
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code09_Socks1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code09_Socks2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code09_Socks3.py
//
// 5. BZOJ 2120 数颜色 - https://www.luogu.com.cn/problem/B3202
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code13_Colors1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code13_Colors2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code13_Colors3.py
//
// 6. 洛谷 P3709 大爷的字符串题 - https://www.luogu.com.cn/problem/P3709
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code13_P3709_Colors1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code13_P3709_Colors2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code13_P3709_Colors3.py
//
// 7. 洛谷 P4137 Rmq Problem / mex - https://www.luogu.com.cn/problem/P4137
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code13_P4137_Colors1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code13_P4137_Colors2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code13_P4137_Colors3.py
//
// 8. AT1219 歴史の研究 - https://www.luogu.com.cn/problem/AT1219
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code13_AT1219_Colors1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code13_AT1219_Colors2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code13_AT1219_Colors3.py
//
// 9. 洛谷 P3245 [HNOI2016]大数 - https://www.luogu.com.cn/problem/P3245
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code14_P3245_DQuery1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code14_P3245_DQuery2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code14_P3245_DQuery3.py
//
// 10. Codeforces 1000F One Occurrence - https://codeforces.com/problemset/problem/1000/F
//     Java解答: https://github.com/algorithm-journey/class179/blob/main/Code14_CF1000F_DQuery1.java
//     C++解答: https://github.com/algorithm-journey/class179/blob/main/Code14_CF1000F_DQuery2.cpp
//     Python解答: https://github.com/algorithm-journey/class179/blob/main/Code14_CF1000F_DQuery3.py

// 由于编译环境限制，原始代码已被注释掉以避免编译错误
// 原始代码如下：
/*
#include <cstdio>
#include <algorithm>
#include <cmath>
#include <cstring>

using namespace std;

struct Query {
    int l, r, id;
};

const int MAXN = 30010;
const int MAXV = 1000010;
int n, m;
int arr[MAXN];
Query query[MAXN];

int bi[MAXN];
int cnt[MAXV];
int curAns = 0;
int ans[MAXN];

bool QueryCmp(Query &a, Query &b) {
    if (bi[a.l] != bi[b.l]) {
        return bi[a.l] < bi[b.l];
    }
    if (bi[a.l] & 1) {
        return a.r < b.r;
    } else {
        return a.r > b.r;
    }
}

void add(int value) {
    // 如果这是该数值第一次出现，则不同数字的个数增加1
    if (cnt[value] == 0) {
        curAns++;
    }
    cnt[value]++;
}

void del(int value) {
    cnt[value]--;
    // 如果该数值不再出现，则不同数字的个数减少1
    if (cnt[value] == 0) {
        curAns--;
    }
}

void compute() {
    int winl = 1, winr = 0; // 当前维护的区间 [winl, winr]
    for (int i = 1; i <= m; i++) {
        int jobl = query[i].l; // 目标区间左端点
        int jobr = query[i].r; // 目标区间右端点
        int id = query[i].id;   // 查询编号
        
        // 扩展左边界
        while (winl > jobl) {
            add(arr[--winl]);
        }
        
        // 扩展右边界
        while (winr < jobr) {
            add(arr[++winr]);
        }
        
        // 收缩左边界
        while (winl < jobl) {
            del(arr[winl++]);
        }
        
        // 收缩右边界
        while (winr > jobr) {
            del(arr[winr--]);
        }
        
        ans[id] = curAns;
    }
}

void prepare() {
    int blen = (int)sqrt((double)n);
    for (int i = 1; i <= n; i++) {
        bi[i] = (i - 1) / blen + 1;
    }
    sort(query + 1, query + m + 1, QueryCmp);
}

int main() {
    scanf("%d", &n);
    
    for (int i = 1; i <= n; i++) {
        scanf("%d", &arr[i]);
    }
    
    scanf("%d", &m);
    for (int i = 1; i <= m; i++) {
        scanf("%d%d", &query[i].l, &query[i].r);
        query[i].id = i;
    }
    
    prepare();
    compute();
    
    for (int i = 1; i <= m; i++) {
        printf("%d\n", ans[i]);
    }
    
    return 0;
}
*/