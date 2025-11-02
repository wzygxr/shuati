package class155;

/**
 * 洛谷 P3273 棘手的操作
 * 题目链接：https://www.luogu.com.cn/problem/P3273
 * 
 * 题目描述：
 * 编号1~n个节点，每个节点独立且有自己的权值，实现如下7种操作，操作一共调用m次：
 * U x y  : x所在的集合和y所在的集合合并
 * A1 x v : x节点的权值增加v
 * A2 x v : x所在的集合所有节点的权值增加v
 * A3 v   : 所有节点的权值增加v
 * F1 x   : 打印x节点的权值
 * F2 x   : 打印x所在的集合中，权值最大的节点的权值
 * F3     : 打印所有节点中，权值最大的节点的权值
 * 
 * 解题思路：
 * 使用左偏树（Leftist Tree）+ 并查集（Union-Find）+ 延迟标记（Lazy Propagation）的组合数据结构。
 * 
 * 核心思想：
 * 1. 使用左偏树维护每个集合中的元素，支持高效合并和删除操作
 * 2. 使用并查集维护集合的连通性
 * 3. 使用延迟标记技术处理区间更新操作
 * 4. 使用multiset维护所有集合头节点的权值，支持快速查询最大值
 * 
 * 关键优化：
 * 1. 延迟标记：避免对整棵树进行实际更新，只在需要时才下传标记
 * 2. 迭代遍历：避免递归遍历导致的栈溢出问题
 * 3. multiset：维护头节点权值的有序性，支持O(log n)查询最大值
 * 
 * 时间复杂度分析：
 * - U操作: O(log n)
 * - A1操作: O(log n)
 * - A2操作: O(log n)
 * - A3操作: O(1)
 * - F1操作: O(log n)
 * - F2操作: O(log n)
 * - F3操作: O(log n)
 * 
 * 空间复杂度分析:
 * - 存储节点信息: O(n)
 * - 存储左偏树结构: O(n)
 * - 存储并查集: O(n)
 * - multiset存储头节点: O(n)
 * - 总体: O(n)
 * 
 * 相关题目：
 * - Java实现：Code02_TrickyOperation1.java
 * - C++实现：Code02_TrickyOperation2.java
 */

// #include <bits/stdc++.h>
// 
// using namespace std;
// 
// const int MAXN = 300001;
// int n, m;
// int num[MAXN];    // 节点权值
// int up[MAXN];     // 父节点
// int ls[MAXN];     // 左子节点
// int rs[MAXN];     // 右子节点
// int dist[MAXN];   // 距离（空路径长度）
// int fa[MAXN];     // 并查集的路径信息
// int siz[MAXN];    // 集合的大小信息
// int add[MAXN];    // 集合内所有数字应该加多少值（延迟标记）
// int sta[MAXN];    // 准备好一个栈，用迭代方式实现先序遍历
// multiset<int> heads;  // 所有集合头节点的值，进入这个有序表
// int addAll = 0;   // 所有数字应该加多少（全局延迟标记）
// 
// /**
//  * 编号为h的节点不再是左偏树的头，在头节点有序表里删掉一份h节点的值
//  * @param h 节点编号
//  */
// void minusHead(int h) {
//     if (h != 0) {
//         heads.erase(heads.find(num[h] + add[h]));
//     }
// }
// 
// /**
//  * 编号为h的节点当前是左偏树的头，在头节点有序表里增加一份h节点的值
//  * @param h 节点编号
//  */
// void addHead(int h) {
//     if (h != 0) {
//         heads.insert(num[h] + add[h]);
//     }
// }
// 
// /**
//  * 初始化数据结构
//  */
// void prepare() {
//     dist[0] = -1;
//     heads.clear();
//     for (int i = 1; i <= n; i++) {
//         up[i] = ls[i] = rs[i] = dist[i] = 0;
//         fa[i] = i;
//         siz[i] = 1;
//         add[i] = 0;
//         addHead(i);
//     }
//     addAll = 0;
// }
// 
// /**
//  * 返回i节点所在左偏树的树头（并查集查找）
//  * @param i 节点编号
//  * @return 树头节点编号
//  */
// int find(int i) {
//     fa[i] = fa[i] == i ? i : find(fa[i]);
//     return fa[i];
// }
// 
// /**
//  * 合并两棵左偏树
//  * @param i 第一棵左偏树的根节点
//  * @param j 第二棵左偏树的根节点
//  * @return 合并后的左偏树根节点
//  */
// int merge(int i, int j) {
//     if (i == 0 || j == 0) return i + j;
//     // 维护大根堆性质
//     if (num[i] < num[j]) {
//         swap(i, j);
//     }
//     rs[i] = merge(rs[i], j);
//     up[rs[i]] = i;
//     // 维护左偏性质
//     if (dist[ls[i]] < dist[rs[i]]) {
//         swap(ls[i], rs[i]);
//     }
//     dist[i] = dist[rs[i]] + 1;
//     fa[ls[i]] = i;
//     fa[rs[i]] = i;
//     return i;
// }
// 
// /**
//  * 节点i是所在左偏树的任意节点，删除节点i，返回整棵树的头节点编号
//  * @param i 要删除的节点编号
//  * @return 删除节点后整棵树的头节点编号
//  */
// int remove(int i) {
//     int h = find(i);
//     fa[ls[i]] = ls[i];
//     fa[rs[i]] = rs[i];
//     int s = merge(ls[i], rs[i]);
//     int f = up[i];
//     fa[i] = s;
//     up[s] = f;
//     if (h != i) {
//         fa[s] = h;
//         if (ls[f] == i) {
//             ls[f] = s;
//         } else {
//             rs[f] = s;
//         }
//         for (int d = dist[s]; dist[f] > d + 1; f = up[f], d++) {
//             dist[f] = d + 1;
//             if (dist[ls[f]] < dist[rs[f]]) {
//                 swap(ls[f], rs[f]);
//             }
//         }
//     }
//     up[i] = ls[i] = rs[i] = dist[i] = 0;
//     return fa[s];
// }
// 
// /**
//  * 以i为头的左偏树，遭遇了更大的左偏树
//  * i的标签信息取消，以i为头的整棵树所有节点的值增加v
//  * 不用递归实现先序遍历，容易爆栈，所以用迭代实现先序遍历
//  * @param i 左偏树根节点
//  * @param v 要增加的值
//  */
// void down(int i, int v) {
//     if (i != 0) {
//         add[i] = 0;
//         int size = 0;
//         sta[++size] = i;
//         while (size > 0) {
//             i = sta[size--];
//             num[i] += v;
//             if (rs[i] != 0) sta[++size] = rs[i];
//             if (ls[i] != 0) sta[++size] = ls[i];
//         }
//     }
// }
// 
// /**
//  * U x y  : x所在的集合和y所在的集合合并
//  * @param i 节点x
//  * @param j 节点y
//  */
// void u(int i, int j) {
//     int l = find(i);
//     int r = find(j);
//     if (l == r) return;
//     int lsize = siz[l];
//     minusHead(l);
//     int rsize = siz[r];
//     minusHead(r);
//     int addTag;
//     if (lsize <= rsize) {
//         down(l, add[l] - add[r]);
//         addTag = add[r];
//     } else {
//         down(r, add[r] - add[l]);
//         addTag = add[l];
//     }
//     int h = merge(l, r);
//     siz[h] = lsize + rsize;
//     add[h] = addTag;
//     addHead(h);
// }
// 
// /**
//  * A1 x v : x节点的权值增加v
//  * @param i 节点x
//  * @param v 增加的值
//  */
// void a1(int i, int v) {
//     int h = find(i);
//     minusHead(h);
//     int l = remove(i);
//     if (l != 0) {
//         siz[l] = siz[h] - 1;
//         add[l] = add[h];
//         addHead(l);
//     }
//     num[i] = num[i] + add[h] + v;
//     fa[i] = i;
//     siz[i] = 1;
//     add[i] = 0;
//     addHead(i);
//     u(l, i);
// }
// 
// /**
//  * A2 x v : x所在的集合所有节点的权值增加v
//  * @param i 节点x
//  * @param v 增加的值
//  */
// void a2(int i, int v) {
//     int h = find(i);
//     minusHead(h);
//     add[h] += v;
//     addHead(h);
// }
// 
// /**
//  * A3 v   : 所有节点的权值增加v
//  * @param v 增加的值
//  */
// void a3(int v) {
//     addAll += v;
// }
// 
// /**
//  * F1 x   : 打印x节点的权值
//  * @param i 节点x
//  * @return 节点x的权值
//  */
// int f1(int i) {
//     return num[i] + add[find(i)] + addAll;
// }
// 
// /**
//  * F2 x   : 打印x所在的集合中，权值最大的节点的权值
//  * @param i 节点x
//  * @return x所在集合中权值最大的节点的权值
//  */
// int f2(int i) {
//     int h = find(i);
//     return num[h] + add[h] + addAll;
// }
// 
// /**
//  * F3     : 打印所有节点中，权值最大的节点的权值
//  * @return 所有节点中权值最大的节点的权值
//  */
// int f3() {
//     return (*heads.rbegin()) + addAll;
// }
// 
// /**
//  * 主函数
//  * 输入格式：
//  * 第一行包含一个整数n，表示节点数量
//  * 第二行包含n个整数，表示每个节点的初始权值
//  * 第三行包含一个整数m，表示操作数量
//  * 接下来m行，每行包含一个操作：
//  *   U x y  : x所在的集合和y所在的集合合并
//  *   A1 x v : x节点的权值增加v
//  *   A2 x v : x所在的集合所有节点的权值增加v
//  *   A3 v   : 所有节点的权值增加v
//  *   F1 x   : 打印x节点的权值
//  *   F2 x   : 打印x所在的集合中，权值最大的节点的权值
//  *   F3     : 打印所有节点中，权值最大的节点的权值
//  * 输出格式：
//  * 对于F1、F2、F3操作，输出相应的结果
//  */
// int main(){
//     ios::sync_with_stdio(false);
//     cin.tie(nullptr);
//     cin >> n;
//     for (int i = 1; i <= n; i++) cin >> num[i];
//     prepare();
//     cin >> m;
//     for (int i = 1; i <= m; i++) {
//         string op; 
//         cin >> op;
//         if (op == "F3") {
//             cout << f3() << "\n";
//         } else {
//             int x; cin >> x;
//             if (op == "U") {
//                 int y; cin >> y;
//                 u(x, y);
//             } else if (op == "A1") {
//                 int y; cin >> y;
//                 a1(x, y);
//             } else if (op == "A2") {
//                 int y; cin >> y;
//                 a2(x, y);
//             } else if (op == "A3") {
//                 a3(x);
//             } else if (op == "F1") {
//                 cout << f1(x) << "\n";
//             } else {
//                 cout << f2(x) << "\n";
//             }
//         }
//     }
//     return 0;
// }