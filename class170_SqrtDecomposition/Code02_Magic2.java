package class172;

// 教主的魔法，C++版
// 题目来源：洛谷P2801
// 题目链接：https://www.luogu.com.cn/problem/P2801
// 题目大意：
// 给定一个长度为n的数组arr，接下来有m条操作，每条操作是如下两种类型中的一种
// 操作 A l r v : 打印arr[l..r]范围上>=v的数字个数
// 操作 M l r v : 把arr[l..r]范围上每个值都加上v
// 1 <= n <= 10^6
// 1 <= m <= 3000
// 1 <= 数组中的值 <= 10^9
// 测试链接 : https://www.luogu.com.cn/problem/P2801
// 如下实现是C++的版本，C++版本和java版本逻辑完全一样
// 提交如下代码，可以通过所有测试用例

// 解题思路：
// 使用分块算法解决此问题
// 1. 将数组分成sqrt(n)大小的块
// 2. 每个块内维护一个排序后的数组，用于二分查找
// 3. 使用懒惰标记处理区间加法操作
// 4. 对于查询操作，完整块使用二分查找，不完整块直接遍历
// 5. 对于区间加法操作，不完整块直接更新并重构排序数组，完整块使用懒惰标记

// 时间复杂度分析：
// 1. 预处理：O(n*sqrt(n))，对每个块进行排序
// 2. 查询操作：O(sqrt(n)*log(sqrt(n)))，遍历不完整块 + 二分查找完整块
// 3. 区间加法操作：O(sqrt(n)*log(sqrt(n)))，更新不完整块并重新排序 + 更新完整块的懒惰标记

// 空间复杂度：O(n)，存储原数组、排序数组和懒惰标记数组

//#include <bits/stdc++.h>
//
//using namespace std;
//
//// 最大数组大小
//const int MAXN = 1000001;
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
//// 每个块的懒惰标记（区间加法标记）
//int lazy[MAXB];
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
// * 使用二分查找优化，考虑懒惰标记的影响
// * 时间复杂度：O(log(sqrt(n)))
// * @param i 块编号
// * @param v 查找的值
// * @return >=v的元素个数
// */
//int getCnt(int i, int v) {
//    // 调整v的值，考虑懒惰标记的影响
//    v -= lazy[i];
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
// * 在不完整块内查询>=v的元素个数
// * 直接遍历元素，考虑懒惰标记的影响
// * 时间复杂度：O(sqrt(n))
// * @param l 区间左端点
// * @param r 区间右端点
// * @param v 查找的值
// * @return >=v的元素个数
// */
//int innerQuery(int l, int r, int v) {
//    // 调整v的值，考虑懒惰标记的影响
//    v -= lazy[bi[l]];
//    int ans = 0;
//    for (int i = l; i <= r; i++) {
//        if (arr[i] >= v) {
//            ans++;
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
//    // 如果在同一个块内，直接调用innerQuery处理
//    if (bi[l] == bi[r]) {
//        ans = innerQuery(l, r, v);
//    } else {
//        // 处理左端不完整块
//        ans += innerQuery(l, br[bi[l]], v);
//        // 处理右端不完整块
//        ans += innerQuery(bl[bi[r]], r, v);
//        // 处理中间的完整块，使用二分查找优化
//        for (int i = bi[l] + 1; i <= bi[r] - 1; i++) {
//            ans += getCnt(i, v);
//        }
//    }
//    return ans;
//}
//
///**
// * 在不完整块内执行区间加法操作
// * 时间复杂度：O(sqrt(n)*log(sqrt(n)))
// * @param l 区间左端点
// * @param r 区间右端点
// * @param v 加的值
// */
//void innerAdd(int l, int r, int v) {
//    // 更新原数组元素
//    for (int i = l; i <= r; i++) {
//        arr[i] += v;
//    }
//    // 更新块内所有元素到排序数组
//    for (int i = bl[bi[l]]; i <= br[bi[l]]; i++) {
//        sortv[i] = arr[i];
//    }
//    // 重新排序该块
//    sort(sortv + bl[bi[l]], sortv + br[bi[l]] + 1);
//}
//
///**
// * 执行区间加法操作[l,r] += v
// * 时间复杂度：O(sqrt(n)*log(sqrt(n)))
// * @param l 区间左端点
// * @param r 区间右端点
// * @param v 加的值
// */
//void add(int l, int r, int v) {
//    // 如果在同一个块内，直接调用innerAdd处理
//    if (bi[l] == bi[r]) {
//        innerAdd(l, r, v);
//    } else {
//        // 处理左端不完整块
//        innerAdd(l, br[bi[l]], v);
//        // 处理右端不完整块
//        innerAdd(bl[bi[r]], r, v);
//        // 处理中间的完整块，使用懒惰标记
//        for (int b = bi[l] + 1; b <= bi[r] - 1; b++) {
//            lazy[b] += v;
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
//    build();
//    char op;
//    int l, r, v;
//    for (int i = 1; i <= m; i++) {
//        cin >> op >> l >> r >> v;
//        if (op == 'A') {
//            cout << query(l, r, v) << '\n';
//        } else {
//            add(l, r, v);
//        }
//    }
//    return 0;
//}