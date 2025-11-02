package class175;

// 哈希冲突问题 - 分块算法实现 (Java版本2)
// 题目来源: https://www.luogu.com.cn/problem/P3396
// 题目大意: 给定一个长度为n的数组arr，支持两种操作：
// 1. 查询操作 A x y: 查询所有满足 i % x == y 的位置i对应的arr[i]之和
// 2. 更新操作 C x y: 将arr[x]的值更新为y
// 约束条件: 1 <= n、m <= 1.5 * 10^5
// 相关解答: 
// - C++版本: Code01_HashCollision.cpp
// - Java版本: Code01_HashCollision1.java, Code01_HashCollision2.java
// - Python版本: Code01_HashCollision.py

// 以下是C++版本的实现代码
// 注意：这些代码当前被注释掉了，仅作为参考
// 这个文件提供了C++实现的参考版本，与Java版本实现的是相同的分块算法
// 分块算法的核心思想：
// 1. 对于x <= sqrt(n)的情况：预处理所有可能的余数结果，使查询时间为O(1)
// 2. 对于x > sqrt(n)的情况：直接暴力枚举，因为x较大时最多执行sqrt(n)次循环
// 这种设计使得查询和更新操作的时间复杂度都是O(sqrt(n))

//#include <bits/stdc++.h>
//
//using namespace std;
//
//const int MAXN = 150001;
//const int MAXB = 401;
//int n, m, blen;
//int arr[MAXN];
//long long dp[MAXB][MAXB];
//
//long long query(int x, int y) {
//    if (x <= blen) {
//        return dp[x][y];
//    }
//    long long ans = 0;
//    for (int i = y; i <= n; i += x) {
//        ans += arr[i];
//    }
//    return ans;
//}
//
//void update(int i, int v) {
//    int delta = v - arr[i];
//    arr[i] = v;
//    for (int x = 1; x <= blen; x++) {
//        dp[x][i % x] += delta;
//    }
//}
//
//void prepare() {
//    blen = (int)sqrt(n);
//    for (int x = 1; x <= blen; x++) {
//        for (int i = 1; i <= n; i++) {
//            dp[x][i % x] += arr[i];
//        }
//    }
//}
//
//int main() {
//    ios::sync_with_stdio(false);
//    cin.tie(nullptr);
//    cin >> n >> m;
//    for (int i = 1; i <= n; i++) {
//        cin >> arr[i];
//    }
//    prepare();
//    char op;
//    int x, y;
//    for (int i = 1; i <= m; i++) {
//        cin >> op >> x >> y;
//        if (op == 'A') {
//            cout << query(x, y) << '\n';
//        } else {
//            update(x, y);
//        }
//    }
//    return 0;
//}