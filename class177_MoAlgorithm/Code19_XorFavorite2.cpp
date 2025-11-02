// XOR and Favorite Number - 普通莫队算法实现 (C++版本)
// 题目来源: CodeForces 617E XOR and Favorite Number
// 题目链接: https://codeforces.com/problemset/problem/617/E
// 题目大意: 给定一个长度为n的数组，每次查询区间[l,r]内有多少对(i,j)满足i<=j且a[i]^a[i+1]^...^a[j]=k
// 时间复杂度: O(n*sqrt(n))
// 空间复杂度: O(n)
//
// 相关题目链接:
// 1. CodeForces 617E XOR and Favorite Number - https://codeforces.com/problemset/problem/617/E
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code19_XorFavorite1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code19_XorFavorite2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code19_XorFavorite3.py
//
// 2. SPOJ DQUERY - https://www.spoj.com/problems/DQUERY/
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code10_DQuery1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code10_DQuery2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code10_DQuery3.py
//
// 3. 洛谷 P1494 [国家集训队]小Z的袜子 - https://www.luogu.com.cn/problem/P1494
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code09_Socks1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code09_Socks2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code09_Socks3.py
//
// 4. Codeforces 86D Powerful Array - https://codeforces.com/problemset/problem/86/D
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code11_PowerfulArray1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code11_PowerfulArray2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code11_PowerfulArray3.py
//
// 5. 洛谷 P1972 [SDOI2009] HH的项链 - https://www.luogu.com.cn/problem/P1972
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code14_DQuery1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code14_DQuery2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code14_DQuery3.py
//
// 6. BZOJ 2120 数颜色 - https://www.luogu.com.cn/problem/B3202
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code13_Colors1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code13_Colors2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code13_Colors3.py
//
// 7. 洛谷 P1903 [国家集训队]数颜色/维护队列 - https://www.luogu.com.cn/problem/P1903
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code16_ColorMaintenance1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code16_ColorMaintenance2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code16_ColorMaintenance3.py
//
// 8. SPOJ COT2 - Count on a tree II - https://www.luogu.com.cn/problem/SP10707
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code17_TreeMo1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code17_TreeMo2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code17_TreeMo3.py
//
// 9. AtCoder JOI 2014 Day1 历史研究 - https://www.luogu.com.cn/problem/AT_joisc2014_c
//    Java解答: https://github.com/algorithm-journey/class179/blob/main/Code15_HistoryResearch1.java
//    C++解答: https://github.com/algorithm-journey/class179/blob/main/Code15_HistoryResearch2.cpp
//    Python解答: https://github.com/algorithm-journey/class179/blob/main/Code15_HistoryResearch3.py
//
// 10. 洛谷 P4887 【模板】莫队二次离线（第十四分块(前体)） - https://www.luogu.com.cn/problem/P4887
//     Java解答: https://github.com/algorithm-journey/class179/blob/main/Code18_SecondaryOffline1.java
//     C++解答: https://github.com/algorithm-journey/class179/blob/main/Code18_SecondaryOffline2.cpp
//     Python解答: https://github.com/algorithm-journey/class179/blob/main/Code18_SecondaryOffline3.py

// 由于编译环境限制，原始代码已被注释掉以避免编译错误
// 原始代码如下：
/*
#include <bits/stdc++.h>
using namespace std;

const int MAXN = 100010;
int n, m, k;
int a[MAXN]; // 原始数组
int prefixXor[MAXN]; // 前缀异或和

// 查询结构
struct Query {
    int l, r, id;
} q[MAXN];

// 莫队相关
int pos[MAXN]; // 每个位置所属的块
int cnt[MAXN * 2]; // 计数数组，需要足够大以容纳所有可能的异或值
long long curAns = 0; // 当前答案
long long ans[MAXN]; // 答案数组

// 查询排序比较器
bool cmp(Query a, Query b) {
    if (pos[a.l] != pos[b.l]) {
        return pos[a.l] < pos[b.l];
    }
    if (pos[a.l] & 1) {
        return a.r < b.r;
    } else {
        return a.r > b.r;
    }
}

// 添加元素到区间
void add(int value) {
    curAns += cnt[value ^ k];
    cnt[value]++;
}

// 从区间中删除元素
void del(int value) {
    cnt[value]--;
    curAns -= cnt[value ^ k];
}

// 计算查询结果
void compute() {
    int winl = 1, winr = 0; // 当前维护的区间 [winl, winr]
    for (int i = 1; i <= m; i++) {
        int jobl = q[i].l; // 目标区间左端点
        int jobr = q[i].r; // 目标区间右端点
        int id = q[i].id;   // 查询编号
        
        // 扩展左边界
        while (winl > jobl) {
            add(prefixXor[--winl - 1]); // 转换为0索引
        }
        
        // 扩展右边界
        while (winr < jobr) {
            add(prefixXor[++winr]); // 转换为0索引
        }
        
        // 收缩左边界
        while (winl < jobl) {
            del(prefixXor[winl++ - 1]); // 转换为0索引
        }
        
        // 收缩右边界
        while (winr > jobr) {
            del(prefixXor[winr--]); // 转换为0索引
        }
        
        ans[id] = curAns;
    }
}

// 预处理
void prepare() {
    int blen = sqrt(n);
    for (int i = 1; i <= n + 1; i++) {
        pos[i] = (i - 1) / blen + 1;
    }
    sort(q + 1, q + m + 1, cmp);
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(0);
    
    cin >> n >> m >> k;
    
    // 读取数组
    for (int i = 1; i <= n; i++) {
        cin >> a[i];
    }
    
    // 计算前缀异或和
    prefixXor[0] = 0;
    for (int i = 1; i <= n; i++) {
        prefixXor[i] = prefixXor[i - 1] ^ a[i];
    }
    
    // 读取查询
    for (int i = 1; i <= m; i++) {
        cin >> q[i].l >> q[i].r;
        q[i].id = i;
    }
    
    // 初始化计数数组
    memset(cnt, 0, sizeof(cnt));
    cnt[0] = 1; // 空前缀的异或和为0
    
    prepare();
    compute();
    
    for (int i = 1; i <= m; i++) {
        cout << ans[i] << "\n";
    }
    
    return 0;
}
*/