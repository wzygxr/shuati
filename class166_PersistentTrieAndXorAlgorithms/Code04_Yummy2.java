package class159;

// 美味，C++版
// 给定一个长度为n的数组arr，一共有m条查询，查询格式如下
// b x l r : 从arr[l..r]中选一个数字，希望b ^ (该数字 + x)的值最大，打印这个值
// 1 <= n <= 2 * 10^5
// 1 <= m <= 10^5
// 0 <= arr[i]、b、x < 10^5
// 测试链接 : https://www.luogu.com.cn/problem/P3293
// 如下实现是C++的版本，C++版本和java版本逻辑完全一样
// 提交如下代码，可以通过所有测试用例

// 补充题目1: 区间异或最大值查询
// 给定一个数组和多个查询，每个查询包含一个区间和一个目标值，要求找出区间内与目标值异或的最大值
// 相关题目:
// - https://www.luogu.com.cn/problem/P3293
// - https://codeforces.com/problemset/problem/1715/E
// - https://www.hdu.edu.cn/problem/5325

// 补充题目2: 可持久化线段树应用
// 利用可持久化线段树解决区间查询问题
// 相关题目:
// - https://www.luogu.com.cn/problem/P3919
// - https://codeforces.com/problemset/problem/1354/D
// - https://www.spoj.com/problems/MKTHNUM/

// 补充题目3: 位运算优化
// 利用位运算和贪心策略优化异或最大值查询
// 相关题目:
// - https://leetcode.cn/problems/maximum-xor-of-two-numbers-in-an-array/
// - https://www.luogu.com.cn/problem/P4551
// - https://codeforces.com/problemset/problem/282/E

//#include <bits/stdc++.h>
//
//using namespace std;
//
//// 最大数组长度
//const int MAXN = 200001;
//
//// 线段树最大节点数
//const int MAXT = 4000001;
//
//// 位数，由于数字范围是0 <= arr[i]、b、x < 10^5，所以最多需要17位（2^17 = 131072 > 10^5）
//const int BIT = 18;
//
//// 数组长度、查询数、数组最大值
//int n, m, s;
//
//// 原数组
//int arr[MAXN];
//
//// 可持久化线段树需要的数组
//// root[i]表示前i个数构成的可持久化线段树的根节点编号
//int root[MAXN];
//
//// ls[i]表示线段树节点i的左子节点编号
//int ls[MAXT];
//
//// rs[i]表示线段树节点i的右子节点编号
//int rs[MAXT];
//
//// siz[i]表示线段树节点i对应的区间中数字的个数
//int siz[MAXT];
//
//// 线段树节点计数器
//int cnt;
//
///**
// * 构建空的线段树
// * @param l 区间左端点
// * @param r 区间右端点
// * @return 根节点编号
// */
//int build(int l, int r) {
//    // 创建新节点
//    int rt = ++cnt;
//    // 初始化节点大小为0
//    siz[rt] = 0;
//    // 如果不是叶子节点，递归构建左右子树
//    if (l < r) {
//        int mid = (l + r) / 2;
//        ls[rt] = build(l, mid);
//        rs[rt] = build(mid + 1, r);
//    }
//    return rt;
//}
//
///**
// * 在可持久化线段树中插入一个数字
// * @param jobi 要插入的数字
// * @param l 区间左端点
// * @param r 区间右端点
// * @param i 前一个版本的根节点编号
// * @return 新版本的根节点编号
// */
//int insert(int jobi, int l, int r, int i) {
//    // 创建新节点
//    int rt = ++cnt;
//    // 复用前一个版本的左右子树
//    ls[rt] = ls[i];
//    rs[rt] = rs[i];
//    // 节点大小加1
//    siz[rt] = siz[i] + 1;
//    // 如果不是叶子节点，递归插入
//    if (l < r) {
//        int mid = (l + r) / 2;
//        // 根据要插入的数字决定插入左子树还是右子树
//        if (jobi <= mid) {
//            ls[rt] = insert(jobi, l, mid, ls[rt]);
//        } else {
//            rs[rt] = insert(jobi, mid + 1, r, rs[rt]);
//        }
//    }
//    return rt;
//}
//
///**
// * 在可持久化线段树中查询区间[jobl, jobr]中数字的个数
// * @param jobl 查询区间左端点
// * @param jobr 查询区间右端点
// * @param l 当前节点对应区间左端点
// * @param r 当前节点对应区间右端点
// * @param u 区间左边界对应版本的根节点编号
// * @param v 区间右边界对应版本的根节点编号
// * @return 区间内数字的个数
// */
//int query(int jobl, int jobr, int l, int r, int u, int v) {
//    // 如果查询区间与当前节点区间无交集，返回0
//    if (jobr < l || jobl > r) {
//        return 0;
//    }
//    // 如果当前节点区间完全包含在查询区间内，直接返回节点大小差
//    if (jobl <= l && r <= jobr) {
//        return siz[v] - siz[u];
//    }
//    // 否则递归查询左右子树
//    int mid = (l + r) / 2;
//    int ans = 0;
//    // 如果查询区间与左子树有交集，查询左子树
//    if (jobl <= mid) {
//        ans += query(jobl, jobr, l, mid, ls[u], ls[v]);
//    }
//    // 如果查询区间与右子树有交集，查询右子树
//    if (jobr > mid) {
//        ans += query(jobl, jobr, mid + 1, r, rs[u], rs[v]);
//    }
//    return ans;
//}
//
///**
// * 预处理函数，构建可持久化线段树
// */
//void prepare() {
//    // 重置计数器
//    cnt = 0;
//    // 计算数组最大值
//    s = 0;
//    for (int i = 1; i <= n; i++) {
//        s = max(s, arr[i]);
//    }
//    // 构建空的线段树
//    root[0] = build(0, s);
//    // 逐个插入数组元素构建可持久化线段树
//    for (int i = 1; i <= n; i++) {
//        root[i] = insert(arr[i], 0, s, root[i - 1]);
//    }
//}
//
///**
// * 计算查询结果：在区间[l,r]中选一个数字，使b ^ (该数字 + x)的值最大
// * @param b 查询参数b
// * @param x 查询参数x
// * @param l 区间左端点
// * @param r 区间右端点
// * @return 最大值
// */
//int compute(int b, int x, int l, int r) {
//    // 贪心策略：从高位到低位逐位确定最优解
//    int best = 0;
//    for (int i = BIT; i >= 0; i--) {
//        // 提取b的第i位
//        if (((b >> i) & 1) == 1) {
//            // 如果b的第i位是1，希望(best+x)的第i位是0，这样异或结果是1
//            // 检查区间[l,r]中是否存在数字num使得(best+x)的第i位是0
//            if (query(best - x, best + (1 << i) - 1 - x, 0, s, root[l - 1], root[r]) == 0) {
//                // 如果不存在，则best的第i位必须是1
//                best += 1 << i;
//            }
//        } else {
//            // 如果b的第i位是0，希望(best+x)的第i位是1，这样异或结果是1
//            // 检查区间[l,r]中是否存在数字num使得(best+x)的第i位是1
//            if (query(best + (1 << i) - x, best + (1 << (i + 1)) - 1 - x, 0, s, root[l - 1], root[r]) != 0) {
//                // 如果存在，则best的第i位可以是1
//                best += 1 << i;
//            }
//        }
//    }
//    // 返回最终结果
//    return best ^ b;
//}
//
//int main() {
//    ios::sync_with_stdio(false);
//    cin.tie(nullptr);
//    cin >> n >> m;
//    // 读入数组元素
//    for (int i = 1; i <= n; i++) {
//        cin >> arr[i];
//    }
//    // 预处理构建可持久化线段树
//    prepare();
//    // 处理查询
//    for (int i = 1, b, x, l, r; i <= m; i++) {
//        cin >> b >> x >> l >> r;
//        // 输出查询结果
//        cout << compute(b, x, l, r) << "\n";
//    }
//    return 0;
//}