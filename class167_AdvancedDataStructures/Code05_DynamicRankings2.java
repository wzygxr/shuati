package class160;

/**
 * 动态排名问题 - 树状数组套线段树解法 (C++版本)
 * 
 * 问题描述：
 * 给定一个长度为n的数组arr，下标从1到n，支持以下两种操作：
 * 1. 查询操作 Q x y z：查询arr[x..y]区间内第z小的数字
 * 2. 更新操作 C x y：将arr[x]位置的数字修改为y
 * 
 * 算法思路：
 * 这是一个经典的动态区间第k小问题，采用树状数组套线段树的数据结构来解决。
 * 
 * 数据结构设计：
 * 1. 外层使用树状数组(BIT)维护前缀信息
 * 2. 内层使用权值线段树维护每个位置上数字的出现次数
 * 3. 通过离散化处理大数值范围，将[0, 10^9]映射到[1, s]范围内
 * 
 * 核心思想：
 * 1. 对于查询操作，利用树状数组的前缀和特性，通过差分思想获取区间[x, y]的信息
 * 2. 在线段树上进行二分查找，确定第k小的数字
 * 3. 对于更新操作，先删除旧值，再插入新值
 * 
 * 时间复杂度分析：
 * 1. 预处理阶段：O(n log n) - 主要是离散化排序的时间复杂度
 * 2. 单次查询操作：O(log n * log s) - 树状数组查询路径上各节点的线段树操作
 * 3. 单次更新操作：O(log n * log s) - 树状数组更新路径上各节点的线段树操作
 * 其中n为数组长度，s为离散化后的值域大小
 * 
 * 空间复杂度分析：
 * 1. 存储原始数组：O(n)
 * 2. 树状数组：O(n)
 * 3. 线段树节点：最坏情况下O(n * log s)，实际使用中远小于该值
 * 总体空间复杂度：O(n * log s)
 * 
 * 算法优势：
 * 1. 支持动态修改和查询操作
 * 2. 相比于平衡树套线段树，实现更简单
 * 3. 常数因子较小，实际运行效率较高
 * 
 * 算法劣势：
 * 1. 空间消耗较大，特别是在线段树节点较多时
 * 2. 实现复杂度高于单一数据结构
 * 
 * 适用场景：
 * 1. 需要频繁进行区间第k小查询
 * 2. 数组元素可以动态修改
 * 3. 查询和更新操作混合进行
 * 
 * 测试链接：https://www.luogu.com.cn/problem/P2617
 * 
 * 输入格式：
 * 第一行包含两个整数n和m，分别表示数组长度和操作次数
 * 第二行包含n个整数，表示初始数组元素
 * 接下来m行，每行描述一个操作：
 *   - "Q x y z" 表示查询操作
 *   - "C x y" 表示更新操作
 * 
 * 输出格式：
 * 对于每个查询操作，输出一行包含一个整数，表示查询结果
 */

// #include <bits/stdc++.h>
//
// using namespace std;
//
// const int MAXN = 100001;
// const int MAXT = MAXN * 130;
// int n, m, s;
// int arr[MAXN];
// int ques[MAXN][4];
// int sorted[MAXN * 2];
// int root[MAXN];
// int sum[MAXT];
// int ls[MAXT];
// int rs[MAXT];
// int cntt = 0;
// int addTree[MAXN];
// int minusTree[MAXN];
// int cntadd;
// int cntminus;
//
// /**
//  * 在已排序的sorted数组中查找数字num的位置（离散化后的值）
//  * @param num 待查找的数字
//  * @return 离散化后的值，如果未找到返回-1
//  */
// int kth(int num) {
//     int l = 1, r = s, mid;
//     while (l <= r) {
//         mid = (l + r) / 2;
//         if (sorted[mid] == num) {
//             return mid;
//         } else if (sorted[mid] < num) {
//             l = mid + 1;
//         } else {
//             r = mid - 1;
//         }
//     }
//     return -1;
// }
//
// /**
//  * 计算树状数组的lowbit值
//  * @param i 输入数字
//  * @return i的lowbit值，即i的二进制表示中最右边的1所代表的数值
//  */
// int lowbit(int i) {
//     return i & -i;
// }
//
// /**
//  * 在线段树中增加或减少某个值的计数
//  * @param jobi 需要操作的值（离散化后的索引）
//  * @param jobv 操作的数值（+1表示增加，-1表示减少）
//  * @param l 线段树当前节点维护的区间左端点
//  * @param r 线段树当前节点维护的区间右端点
//  * @param i 线段树当前节点编号（0表示需要新建节点）
//  * @return 更新后的节点编号
//  */
// int innerAdd(int jobi, int jobv, int l, int r, int i) {
//     if (i == 0) {
//         i = ++cntt;
//     }
//     if (l == r) {
//     	sum[i] += jobv;
//     } else {
//         int mid = (l + r) / 2;
//         if (jobi <= mid) {
//         	ls[i] = innerAdd(jobi, jobv, l, mid, ls[i]);
//         } else {
//         	rs[i] = innerAdd(jobi, jobv, mid + 1, r, rs[i]);
//         }
//         sum[i] = sum[ls[i]] + sum[rs[i]];
//     }
//     return i;
// }
//
// /**
//  * 在线段树上二分查找第k小的值
//  * @param jobk 查找第k小的值
//  * @param l 当前查询区间左端点
//  * @param r 当前查询区间右端点
//  * @return 第k小值在sorted数组中的索引
//  */
// int innerQuery(int jobk, int l, int r) {
//     if (l == r) {
//         return l;
//     }
//     int mid = (l + r) / 2;
//     int leftsum = 0;
//     for (int i = 1; i <= cntadd; i++) {
//         leftsum += sum[ls[addTree[i]]];
//     }
//     for (int i = 1; i <= cntminus; i++) {
//         leftsum -= sum[ls[minusTree[i]]];
//     }
//     if (jobk <= leftsum) {
//         for (int i = 1; i <= cntadd; i++) {
//             addTree[i] = ls[addTree[i]];
//         }
//         for (int i = 1; i <= cntminus; i++) {
//             minusTree[i] = ls[minusTree[i]];
//         }
//         return innerQuery(jobk, l, mid);
//     } else {
//         for (int i = 1; i <= cntadd; i++) {
//             addTree[i] = rs[addTree[i]];
//         }
//         for (int i = 1; i <= cntminus; i++) {
//             minusTree[i] = rs[minusTree[i]];
//         }
//         return innerQuery(jobk - leftsum, mid + 1, r);
//     }
// }
//
// /**
//  * 在树状数组中增加或减少某个位置上值的计数
//  * @param i 数组位置（dfn序号）
//  * @param cnt 操作数值（+1表示增加，-1表示减少）
//  */
// void add(int i, int cnt) {
//     for (int j = i; j <= n; j += lowbit(j)) {
//         root[j] = innerAdd(arr[i], cnt, 1, s, root[j]);
//     }
// }
//
// /**
//  * 更新数组中某个位置的值
//  * @param i 需要更新的位置
//  * @param v 新的值
//  */
// void update(int i, int v) {
//     add(i, -1);
//     arr[i] = kth(v);
//     add(i, 1);
// }
//
// /**
//  * 查询区间[l, r]中第k小的值
//  * @param l 区间左端点
//  * @param r 区间右端点
//  * @param k 查询第k小
//  * @return 第k小的原始数值
//  */
// int number(int l, int r, int k) {
//     cntadd = cntminus = 0;
//     for (int i = r; i > 0; i -= lowbit(i)) {
//         addTree[++cntadd] = root[i];
//     }
//     for (int i = l - 1; i > 0; i -= lowbit(i)) {
//         minusTree[++cntminus] = root[i];
//     }
//     return sorted[innerQuery(k, 1, s)];
// }
//
// /**
//  * 预处理函数，包括离散化和初始化树状数组
//  */
// void prepare() {
//     s = 0;
//     for (int i = 1; i <= n; i++) {
//     	sorted[++s] = arr[i];
//     }
//     for (int i = 1; i <= m; i++) {
//         if (ques[i][0] == 2) {
//         	sorted[++s] = ques[i][2];
//         }
//     }
//     sort(sorted + 1, sorted + s + 1);
//     int len = 1;
//     for (int i = 2; i <= s; i++) {
//         if (sorted[len] != sorted[i]) {
//         	sorted[++len] = sorted[i];
//         }
//     }
//     s = len;
//     for (int i = 1; i <= n; i++) {
//         arr[i] = kth(arr[i]);
//         add(i, 1);
//     }
// }
//
// int main() {
//     ios::sync_with_stdio(false);
//     cin.tie(nullptr);
//     cin >> n >> m;
//     for (int i = 1; i <= n; i++) {
//         cin >> arr[i];
//     }
//     for (int i = 1; i <= m; i++) {
//         string op;
//         cin >> op;
//         if (op == "Q") {
//             ques[i][0] = 1;
//         } else {
//             ques[i][0] = 2;
//         }
//         cin >> ques[i][1];
//         cin >> ques[i][2];
//         if (ques[i][0] == 1) {
//             cin >> ques[i][3];
//         }
//     }
//     prepare();
//     for (int i = 1, op, x, y, z; i <= m; i++) {
//         op = ques[i][0];
//         x = ques[i][1];
//         y = ques[i][2];
//         if (op == 1) {
//             z = ques[i][3];
//             cout << number(x, y, z) << "\n";
//         } else {
//             update(x, y);
//         }
//     }
//     return 0;
// }