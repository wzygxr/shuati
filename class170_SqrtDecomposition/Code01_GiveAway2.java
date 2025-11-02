package class172;

// Give Away，C++版
// 题目来源：SPOJ GIVEAWAY
// 题目链接：https://www.spoj.com/problems/GIVEAWAY/
// 题目大意：
// 给定一个长度为n的数组arr，接下来有m条操作，每条操作是如下两种类型中的一种
// 操作 0 a b c : 打印arr[a..b]范围上>=c的数字个数
// 操作 1 a b   : 把arr[a]的值改成b
// 1 <= n <= 5 * 10^5
// 1 <= m <= 10^5
// 1 <= 数组中的值 <= 10^9
// 测试链接 : https://www.luogu.com.cn/problem/SP18185
// 测试链接 : https://www.spoj.com/problems/GIVEAWAY
// 如下实现是C++的版本，C++版本和java版本逻辑完全一样
// 提交如下代码，可以通过所有测试用例

// 解题思路：
// 使用分块算法解决此问题
// 1. 将数组分成sqrt(n)大小的块
// 2. 每个块内维护一个排序后的数组，用于二分查找
// 3. 对于查询操作，完整块使用二分查找，不完整块直接遍历
// 4. 对于更新操作，更新原数组和对应块的排序数组，并重新排序

// 时间复杂度分析：
// 1. 预处理：O(n*sqrt(n))，对每个块进行排序
// 2. 查询操作：O(sqrt(n)*log(sqrt(n)))，遍历不完整块 + 二分查找完整块
// 3. 更新操作：O(sqrt(n)*log(sqrt(n)))，更新元素并重新排序块

// 空间复杂度：O(n)，存储原数组和排序数组

//#include <bits/stdc++.h>
//
//using namespace std;
//
//// 最大数组大小
//const int MAXN = 500001;
//// 最大块数
//const int MAXB = 1001;
//// 数组长度和操作数
//int n, m;
//// 原数组
//int arr[MAXN];
//// 排序后的数组，用于二分查找
//int sortv[MAXN];
//
//// 块大小和块数量
//int blen, bnum;
//// 每个元素所属的块
//int bi[MAXN];
//// 每个块的左右边界
//int bl[MAXB];
//int br[MAXB];
//
///**
// * 构建分块结构
// * 时间复杂度：O(n*sqrt(n))
// */
//void build() {
//    // 块大小取sqrt(n)
//    blen = (int)sqrt(n);
//    // 块数量
//    bnum = (n + blen - 1) / blen;
//    
//    // 计算每个元素属于哪个块
//    for (int i = 1; i <= n; i++) {
//        bi[i] = (i - 1) / blen + 1;
//    }
//    
//    // 计算每个块的左右边界
//    for (int i = 1; i <= bnum; i++) {
//        bl[i] = (i - 1) * blen + 1;
//        br[i] = min(i * blen, n);
//    }
//    
//    // 复制原数组用于排序
//    for (int i = 1; i <= n; i++) {
//        sortv[i] = arr[i];
//    }
//    
//    // 对每个块内的元素进行排序
//    for (int i = 1; i <= bnum; i++) {
//        sort(sortv + bl[i], sortv + br[i] + 1);
//    }
//}
//
///**
// * 在指定块内查找>=v的元素个数
// * 使用二分查找优化
// * 时间复杂度：O(log(sqrt(n)))
// * @param i 块编号
// * @param v 查找的值
// * @return >=v的元素个数
// */
//int getCnt(int i, int v) {
//    int l = bl[i], r = br[i], m, ans = 0;
//    // 二分查找第一个>=v的位置
//    while (l <= r) {
//        m = (l + r) >> 1;
//        if (sortv[m] >= v) {
//            // 找到一个>=v的元素，其后面的所有元素都>=v
//            ans += r - m + 1;
//            r = m - 1;
//        } else {
//            l = m + 1;
//        }
//    }
//    return ans;
//}
//
///**
// * 查询区间[l,r]内>=v的元素个数
// * 时间复杂度：O(sqrt(n)*log(sqrt(n)))
// * @param l 区间左端点
// * @param r 区间右端点
// * @param v 查找的值
// * @return >=v的元素个数
// */
//int query(int l, int r, int v) {
//    int ans = 0;
//    // 如果在同一个块内，直接暴力处理
//    if (bi[l] == bi[r]) {
//        for (int i = l; i <= r; i++) {
//            if (arr[i] >= v) {
//                ans++;
//            }
//        }
//    } else {
//        // 处理左端不完整块
//        for (int i = l; i <= br[bi[l]]; i++) {
//            if (arr[i] >= v) {
//                ans++;
//            }
//        }
//        // 处理右端不完整块
//        for (int i = bl[bi[r]]; i <= r; i++) {
//            if (arr[i] >= v) {
//                ans++;
//            }
//        }
//        // 处理中间的完整块，使用二分查找优化
//        for (int i = bi[l] + 1; i <= bi[r] - 1; i++) {
//            ans += getCnt(i, v);
//        }
//    }
//    return ans;
//}
//
///**
// * 更新位置i的值为v
// * 时间复杂度：O(sqrt(n)*log(sqrt(n)))
// * @param i 位置
// * @param v 新值
// */
//void update(int i, int v) {
//    int l = bl[bi[i]];
//    int r = br[bi[i]];
//    arr[i] = v;
//    // 更新块内所有元素到排序数组
//    for (int j = l; j <= r; j++) {
//        sortv[j] = arr[j];
//    }
//    // 重新排序该块
//    sort(sortv + l, sortv + r + 1);
//}
//
//int main() {
//    ios::sync_with_stdio(false);
//    cin.tie(nullptr);
//    cin >> n;
//    for (int i = 1; i <= n; i++) {
//        cin >> arr[i];
//    }
//    build();
//    cin >> m;
//    int op, a, b, c;
//    for (int i = 1; i <= m; i++) {
//        cin >> op >> a >> b;
//        if (op == 0) {
//            cin >> c;
//            cout << query(a, b, c) << '\n';
//        } else {
//            update(a, b);
//        }
//    }
//    return 0;
//}