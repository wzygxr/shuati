package class160;

/**
 * 排队问题 - 树状数组套线段树解法 (C++版本)
 * 
 * 问题描述：
 * 给定一个长度为n的数组arr，下标从1到n。
 * 如果存在i < j，并且arr[i] > arr[j]，那么(i,j)就叫做一个逆序对。
 * 首先打印原始arr中有多少逆序对，然后进行m次操作：
 * 操作 a b：交换arr中a位置和b位置的数，打印数组中逆序对的数量。
 * 
 * 算法思路：
 * 这是一个动态维护逆序对数量的问题。采用树状数组套线段树的数据结构来解决。
 * 
 * 数据结构设计：
 * 1. 外层使用树状数组(BIT)维护位置信息
 * 2. 内层使用权值线段树维护每个位置上数字的出现次数
 * 3. 通过离散化处理大数值范围，将[1, 10^9]映射到[1, s]范围内
 * 
 * 核心思想：
 * 1. 初始时，从左到右依次处理每个元素，通过查询前面元素中比当前元素大的数量来统计初始逆序对
 * 2. 交换操作时，通过数学推导计算交换带来的逆序对数量变化
 * 3. 利用树状数组维护前缀信息，在线段树上进行区间查询和单点更新
 * 
 * 时间复杂度分析：
 * 1. 预处理阶段：O(n log n) - 主要是离散化排序的时间复杂度
 * 2. 初始逆序对计算：O(n log n * log s) - 对每个元素查询前面比它大的元素数量
 * 3. 单次交换操作：O(log n * log s) - 计算交换带来的逆序对变化并更新数据结构
 * 其中n为数组长度，s为离散化后的值域大小
 * 
 * 空间复杂度分析：
 * 1. 存储原始数组：O(n)
 * 2. 树状数组：O(n)
 * 3. 线段树节点：最坏情况下O(n * log s)，实际使用中远小于该值
 * 总体空间复杂度：O(n * log s)
 * 
 * 算法优势：
 * 1. 支持动态交换操作和逆序对数量的实时维护
 * 2. 相比于朴素O(n²)算法，效率大幅提升
 * 3. 实现相对简单，常数因子较小
 * 
 * 算法劣势：
 * 1. 空间消耗较大
 * 2. 交换操作的逆序对变化计算较为复杂
 * 
 * 适用场景：
 * 1. 需要动态维护数组的逆序对数量
 * 2. 数组元素可以交换但整体结构保持不变
 * 3. 查询操作频繁
 * 
 * 测试链接：https://www.luogu.com.cn/problem/P1975
 * 
 * 输入格式：
 * 第一行包含一个整数n，表示数组长度
 * 第二行包含n个整数，表示初始数组元素
 * 第三行包含一个整数m，表示操作次数
 * 接下来m行，每行包含两个整数a和b，表示交换操作
 * 
 * 输出格式：
 * 第一行输出初始逆序对数量
 * 接下来m行，每行输出一次交换操作后的逆序对数量
 */

// #include <bits/stdc++.h>
//
// using namespace std;
//
// const int MAXN = 20001;
// const int MAXT = MAXN * 80;
// const int INF = 1000000001;
// int n, m, s;
// int arr[MAXN];
// int sorted[MAXN + 2];
// int root[MAXN];
// int ls[MAXT];
// int rs[MAXT];
// int sum[MAXT];
// int cnt = 0;
// int ans = 0;
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
//         if (sorted[mid] == num) return mid;
//         else if (sorted[mid] < num) l = mid + 1;
//         else r = mid - 1;
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
//  * 线段树单点修改，增加或减少某个值的计数
//  * @param jobi 需要操作的值（离散化后的索引）
//  * @param jobv 操作的数值（+1表示增加，-1表示减少）
//  * @param l 线段树当前节点维护的区间左端点
//  * @param r 线段树当前节点维护的区间右端点
//  * @param i 线段树当前节点编号（0表示需要新建节点）
//  * @return 更新后的节点编号
//  */
// int innerAdd(int jobi, int jobv, int l, int r, int i) {
//     if (i == 0) i = ++cnt;
//     if (l == r) {
//         sum[i] += jobv;
//     } else {
//         int mid = (l + r) / 2;
//         if (jobi <= mid) {
//             ls[i] = innerAdd(jobi, jobv, l, mid, ls[i]);
//         } else {
//             rs[i] = innerAdd(jobi, jobv, mid + 1, r, rs[i]);
//         }
//         sum[i] = sum[ls[i]] + sum[rs[i]];
//     }
//     return i;
// }
//
// /**
//  * 查询线段树上某个值域区间内的元素数量
//  * @param jobl 查询值域区间左端点
//  * @param jobr 查询值域区间右端点
//  * @param l 线段树当前节点维护的区间左端点
//  * @param r 线段树当前节点维护的区间右端点
//  * @param i 线段树当前节点编号
//  * @return 值域[jobl, jobr]内元素的数量
//  */
// int innerQuery(int jobl, int jobr, int l, int r, int i) {
//     if (i == 0) return 0;
//     if (jobl <= l && r <= jobr) return sum[i];
//     int mid = (l + r) / 2;
//     int ans = 0;
//     if (jobl <= mid) {
//     	ans += innerQuery(jobl, jobr, l, mid, ls[i]);
//     }
//     if (jobr > mid) {
//     	ans += innerQuery(jobl, jobr, mid + 1, r, rs[i]);
//     }
//     return ans;
// }
//
// /**
//  * 在树状数组中增加或减少某个位置上值的计数
//  * @param i 数组位置
//  * @param v 操作数值（+1表示增加，-1表示减少）
//  */
// void add(int i, int v) {
//     for (int j = i; j <= n; j += lowbit(j)) {
//         root[j] = innerAdd(arr[i], v, 1, s, root[j]);
//     }
// }
//
// /**
//  * 查询区间[al, ar]中，值域[numl, numr]范围内元素的数量
//  * @param al 查询区间左端点
//  * @param ar 查询区间右端点
//  * @param numl 值域区间左端点
//  * @param numr 值域区间右端点
//  * @return 满足条件的元素数量
//  */
// int query(int al, int ar, int numl, int numr) {
//     int ans = 0;
//     for (int i = ar; i > 0; i -= lowbit(i)) {
//         ans += innerQuery(numl, numr, 1, s, root[i]);
//     }
//     for (int i = al - 1; i > 0; i -= lowbit(i)) {
//         ans -= innerQuery(numl, numr, 1, s, root[i]);
//     }
//     return ans;
// }
//
// /**
//  * 交换a和b位置的数字，并更新逆序对数量
//  * 保证a在前，b在后
//  * @param a 位置a
//  * @param b 位置b
//  */
// void compute(int a, int b) {
//     ans -= query(a + 1, b - 1, 1, arr[a] - 1);
//     ans += query(a + 1, b - 1, arr[a] + 1, s);
//     ans -= query(a + 1, b - 1, arr[b] + 1, s);
//     ans += query(a + 1, b - 1, 1, arr[b] - 1);
//     if (arr[a] < arr[b]) ans++;
//     else if (arr[a] > arr[b]) ans--;
//     add(a, -1);
//     add(b, -1);
//     swap(arr[a], arr[b]);
//     add(a, 1);
//     add(b, 1);
// }
//
// /**
//  * 预处理函数，包括离散化和初始化树状数组
//  */
// void prepare() {
//     s = 0;
//     for (int i = 1; i <= n; i++) sorted[++s] = arr[i];
//     sorted[++s] = -INF;
//     sorted[++s] = INF;
//     sort(sorted + 1, sorted + s + 1);
//     int len = 1;
//     for (int i = 2; i <= s; i++) {
//         if (sorted[len] != sorted[i]) sorted[++len] = sorted[i];
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
//     cin >> n;
//     for (int i = 1; i <= n; i++) cin >> arr[i];
//     prepare();
//     for (int i = 2; i <= n; i++) ans += query(1, i - 1, arr[i] + 1, s);
//     cout << ans << '\n';
//     cin >> m;
//     for (int i = 1, a, b; i <= m; i++) {
//         cin >> a >> b;
//         if (a > b) swap(a, b);
//         compute(a, b);
//         cout << ans << '\n';
//     }
//     return 0;
// }