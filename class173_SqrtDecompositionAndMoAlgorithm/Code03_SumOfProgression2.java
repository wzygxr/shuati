package class175;

// 等差数列求和问题 - 分块算法实现 (C++版本的Java注释版)
// 题目来源: https://www.luogu.com.cn/problem/CF1921F
// 题目来源: https://codeforces.com/problemset/problem/1921/F
// 题目大意: 给定一个长度为n的数组arr，支持查询操作：
// 查询 s d k : arr[s]作为第1项、arr[s + 1*d]作为第2项、arr[s + 2*d]作为第3项...
//             每项的值 * 项的编号，一共k项都累加起来，打印累加和
// 约束条件: 
// 1 <= n <= 10^5
// 1 <= q <= 2 * 10^5

// 以下是C++版本的实现，逻辑与Java版本完全一致
// 提交时需要将代码转换为C++格式

//#include <bits/stdc++.h>
//
//using namespace std;
//
//const int MAXN = 100001;
//const int MAXB = 401;
//int t, n, q, blen;
//int arr[MAXN];
//long long f[MAXB][MAXN];
//long long g[MAXB][MAXN];
//
//long long query(int s, int d, int k) {
//    long long ans = 0;
//    if (d <= blen) {
//        ans = g[d][s];
//        if (s + d * k <= n) {
//            ans -= g[d][s + d * k] + f[d][s + d * k] * k;
//        }
//    } else {
//        for (int i = 1; i <= k; i++) {
//            ans += 1LL * arr[s + (i - 1) * d] * i;
//        }
//    }
//    return ans;
//}
//
//void prepare() {
//    blen = (int)sqrt(n);
//    for (int d = 1; d <= blen; d++) {
//        for (int i = n; i >= 1; i--) {
//            f[d][i] = arr[i] + (i + d > n ? 0 : f[d][i + d]);
//        }
//    }
//    for (int d = 1; d <= blen; d++) {
//        for (int i = n; i >= 1; i--) {
//            g[d][i] = f[d][i] + (i + d > n ? 0 : g[d][i + d]);
//        }
//    }
//}
//
//int main() {
//    ios::sync_with_stdio(false);
//    cin.tie(nullptr);
//    cin >> t;
//    for (int c = 1; c <= t; c++) {
//        cin >> n >> q;
//        for (int i = 1; i <= n; i++) {
//            cin >> arr[i];
//        }
//        prepare();
//        for (int i = 1, s, d, k; i <= q; i++) {
//            cin >> s >> d >> k;
//            cout << query(s, d, k) << '\n';
//        }
//    }
//    return 0;
//}