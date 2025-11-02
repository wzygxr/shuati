package class175;

// 数组查询问题 - 分块算法实现 (C++版本的Java注释版)
// 题目来源: https://www.luogu.com.cn/problem/CF797E
// 题目来源: https://codeforces.com/problemset/problem/797/E
// 题目大意: 给定一个长度为n的数组arr，支持查询操作：
// 查询 p k : 从位置p开始，每次跳跃arr[p] + k步，直到越界，返回跳跃次数
// 约束条件: 
// 1 <= n、q <= 10^5
// 1 <= arr[i] <= n

// 相关解答:
// C++版本: class175/Code02_ArrayQueries.cpp
// Java版本: class175/Code02_ArrayQueries1.java
// Python版本: class175/Code02_ArrayQueries.py

// 分块算法分析:
// - 时间复杂度：预处理O(n*sqrt(n)) + 查询O(q*sqrt(n))
// - 空间复杂度：O(n*sqrt(n))
// - 分块思想：将k分为k<=sqrt(n)和k>sqrt(n)两种情况处理
//   - 当k<=sqrt(n)时：预处理所有位置p和k的结果，查询时间O(1)
//   - 当k>sqrt(n)时：直接暴力计算，由于k>sqrt(n)，每次跳跃步长至少sqrt(n)+1
//     因此最多只会跳sqrt(n)次，时间复杂度O(sqrt(n))

// 注：此文件包含的是C++版本的实现（已注释），用于参考
// 提交到OJ平台时，需要将代码转换为C++格式并移除注释符号

//#include <bits/stdc++.h>
//
//using namespace std;
//
//const int MAXN = 100001;
//const int MAXB = 401;
//int n, q, blen;
//int arr[MAXN];
//int dp[MAXN][MAXB];
//
//int query(int p, int k) {
//    if (k <= blen) {
//        return dp[p][k];
//    }
//    int ans = 0;
//    while (p <= n) {
//        ans++;
//        p += arr[p] + k;
//    }
//    return ans;
//}
//
//void prepare() {
//    blen = (int)sqrt(n);
//    for (int p = n; p >= 1; p--) {
//        for (int k = 1; k <= blen; k++) {
//            dp[p][k] = 1 + (p + arr[p] + k > n ? 0 : dp[p + arr[p] + k][k]);
//        }
//    }
//}
//
//int main() {
//    ios::sync_with_stdio(false);
//    cin.tie(nullptr);
//    cin >> n;
//    for (int i = 1; i <= n; i++) {
//        cin >> arr[i];
//    }
//    prepare();
//    cin >> q;
//    for (int i = 1, p, k; i <= q; i++) {
//        cin >> p >> k;
//        cout << query(p, k) << '\n';
//    }
//    return 0;
//}