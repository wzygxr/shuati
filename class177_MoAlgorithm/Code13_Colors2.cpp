#include <cstdio>
#include <algorithm>
#include <cmath>
#include <cstring>
using namespace std;

// 数颜色 - 带修莫队算法实现 (C++版本)
// 题目来源: 洛谷 P1903 [国家集训队] 数颜色 / 维护队列
// 题目链接: https://www.luogu.com.cn/problem/P1903
// 题目大意: 给定一个长度为n的序列，支持两种操作：
// 1. 修改某个位置的颜色
// 2. 查询区间[l,r]内有多少种不同的颜色
// 解题思路: 使用带修莫队算法，增加时间维度处理修改操作
// 时间复杂度: O(n^(5/3))
// 空间复杂度: O(n)
//
// 相关题目链接:
// 1. BZOJ 2120 数颜色 - https://www.luogu.com.cn/problem/B3202
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code13_Colors1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code13_Colors2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code13_Colors3.py
//
// 2. Codeforces 940F Machine Learning - https://codeforces.com/problemset/problem/940/F
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code13_CF940F_Colors1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code13_CF940F_Colors2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code13_CF940F_Colors3.py
//
// 3. UVA 12345 Dynamic len(set(a[L:R])) - https://vjudge.net/problem/UVA-12345
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code13_UVA12345_Colors1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code13_UVA12345_Colors2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code13_UVA12345_Colors3.py
//
// 4. SPOJ DQUERY - https://www.spoj.com/problems/DQUERY/
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code10_DQuery1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code10_DQuery2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code10_DQuery3.py
//
// 5. 洛谷 P1494 [国家集训队]小Z的袜子 - https://www.luogu.com.cn/problem/P1494
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code09_Socks1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code09_Socks2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code09_Socks3.py
//
// 6. Codeforces 86D Powerful Array - https://codeforces.com/problemset/problem/86/D
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code11_PowerfulArray1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code11_PowerfulArray2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code11_PowerfulArray3.py
//
// 7. Codeforces 617E XOR and Favorite Number - https://codeforces.com/problemset/problem/617/E
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code12_XORAndFavoriteNumber1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code12_XORAndFavoriteNumber2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code12_XORAndFavoriteNumber3.py
//
// 8. 洛谷 P3709 大爷的字符串题 - https://www.luogu.com.cn/problem/P3709
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code13_P3709_Colors1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code13_P3709_Colors2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code13_P3709_Colors3.py
//
// 9. 洛谷 P4137 Rmq Problem / mex - https://www.luogu.com.cn/problem/P4137
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code13_P4137_Colors1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code13_P4137_Colors2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code13_P4137_Colors3.py
//
// 10. AT1219 歴史の研究 - https://www.luogu.com.cn/problem/AT1219
//     Java解答: https://github.com/algorithm-journey/class179/blob/main/Code13_AT1219_Colors1.java
//     C++解答: https://github.com/algorithm-journey/class179/blob/main/Code13_AT1219_Colors2.cpp
//     Python解答: https://github.com/algorithm-journey/class179/blob/main/Code13_AT1219_Colors3.py

const int MAXN = 10010;
const int MAXM = 10010;
int n, m, cntq, cntm; // cntq: 查询数, cntm: 修改数
int arr[MAXN]; // 原数组
int bi[MAXN];

struct Query {
    int l, r, t, id;
    bool operator<(const Query& other) const {
        if (bi[l] != bi[other.l]) return bi[l] < bi[other.l];
        if (bi[r] != bi[other.r]) return bi[r] < bi[other.r];
        return t < other.t;
    }
} query[MAXM];

struct Modify {
    int pos, oldColor, newColor;
} modify[MAXM];

int cnt[1000010]; // 记录每种颜色的出现次数
int curAns = 0; // 当前区间的答案
int ans[MAXM]; // 存储答案

// 分块大小
int block_size;

// 添加元素到区间
void add(int color) {
    if (cnt[color] == 0) {
        curAns++;
    }
    cnt[color]++;
}

// 从区间中删除元素
void del(int color) {
    cnt[color]--;
    if (cnt[color] == 0) {
        curAns--;
    }
}

// 应用修改操作
void apply(int time, int l, int r) {
    int pos = modify[time].pos;
    int oldColor = modify[time].oldColor;
    int newColor = modify[time].newColor;
    
    // 如果修改位置在当前查询区间内，需要更新答案
    if (pos >= l && pos <= r) {
        del(oldColor);
        add(newColor);
    }
    arr[pos] = newColor;
}

// 撤销修改操作
void undo(int time, int l, int r) {
    int pos = modify[time].pos;
    int oldColor = modify[time].oldColor;
    int newColor = modify[time].newColor;
    
    // 如果修改位置在当前查询区间内，需要更新答案
    if (pos >= l && pos <= r) {
        del(newColor);
        add(oldColor);
    }
    arr[pos] = oldColor;
}

// 计算查询结果
void compute() {
    int winl = 1, winr = 0, now = 0; // now: 当前处理到第几个修改操作
    for (int i = 1; i <= cntq; i++) {
        int jobl = query[i].l; // 目标区间左端点
        int jobr = query[i].r; // 目标区间右端点
        int jobt = query[i].t; // 目标时间戳
        int id = query[i].id;  // 查询编号
        
        // 处理时间维度
        while (now < jobt) {
            now++;
            apply(now, winl, winr);
        }
        while (now > jobt) {
            undo(now, winl, winr);
            now--;
        }
        
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

// 预处理
void prepare() {
    block_size = (int)pow((double)n, 2.0 / 3.0); // 带修莫队的块大小
    for (int i = 1; i <= n; i++) {
        bi[i] = (i - 1) / block_size + 1;
    }
    // 只对查询排序
    sort(query + 1, query + cntq + 1);
}

int main() {
    scanf("%d%d", &n, &m);
    
    for (int i = 1; i <= n; i++) {
        scanf("%d", &arr[i]);
    }
    
    char op[2];
    for (int i = 1; i <= m; i++) {
        scanf("%s", op);
        if (op[0] == 'Q') { // 查询操作
            cntq++;
            scanf("%d%d", &query[cntq].l, &query[cntq].r);
            query[cntq].t = cntm;      // 时间戳
            query[cntq].id = cntq;     // id
        } else { // 修改操作
            cntm++;
            int pos, color;
            scanf("%d%d", &pos, &color);
            modify[cntm].pos = pos;
            modify[cntm].oldColor = arr[pos];
            modify[cntm].newColor = color;
            arr[pos] = color;
        }
    }
    
    prepare();
    compute();
    
    for (int i = 1; i <= cntq; i++) {
        printf("%d\n", ans[i]);
    }
    
    return 0;
}