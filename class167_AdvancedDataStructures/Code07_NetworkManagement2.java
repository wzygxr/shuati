package class160;

/**
 * 网络管理问题 - 树链剖分 + 树状数组套线段树解法 (C++版本)
 * 
 * 问题描述：
 * 给定一棵包含n个节点的树，每个节点有一个点权。
 * 支持以下两种操作：
 * 1. 更新操作 0 x y：将节点x的点权修改为y
 * 2. 查询操作 k x y：查询节点x到节点y路径上第k大的点权值，如果路径上节点数不足k个，则输出"invalid request!"
 * 
 * 算法思路：
 * 这是一个树上路径第k大查询问题，采用树链剖分 + 树状数组套线段树的解决方案。
 * 
 * 数据结构设计：
 * 1. 使用树链剖分将树上路径查询转化为区间查询问题
 * 2. 外层使用树状数组维护DFS序上的信息
 * 3. 内层使用权值线段树维护每个位置上数字的出现次数
 * 4. 通过离散化处理大数值范围
 * 
 * 核心思想：
 * 1. 通过DFS序将树上操作转化为序列操作
 * 2. 利用树状数组维护前缀信息，在线段树上进行第k大查询
 * 3. 树上路径[x,y]的查询转化为4个DFS序区间的组合操作
 * 
 * 时间复杂度分析：
 * 1. 预处理阶段：O(n log n) - DFS序和离散化排序
 * 2. 单次更新操作：O(log n * log s) - 树状数组更新路径上各节点的线段树操作
 * 3. 单次查询操作：O(log²n * log s) - 树链剖分跳转 + 树状数组查询 + 线段树第k大查询
 * 其中n为节点数，s为离散化后的值域大小
 * 
 * 空间复杂度分析：
 * 1. 存储树结构：O(n)
 * 2. 树状数组：O(n)
 * 3. 线段树节点：最坏情况下O(n * log s)，实际使用中远小于该值
 * 4. 树链剖分辅助数组：O(n)
 * 总体空间复杂度：O(n * log s)
 * 
 * 算法优势：
 * 1. 支持动态修改和查询操作
 * 2. 可以处理任意树上路径查询
 * 3. 相比于树链剖分套线段树，实现更简单
 * 
 * 算法劣势：
 * 1. 空间消耗较大
 * 2. 常数因子较大
 * 
 * 适用场景：
 * 1. 树上动态路径第k大查询
 * 2. 树上节点权值可以动态修改
 * 3. 查询和更新操作混合进行
 * 
 * 测试链接：https://www.luogu.com.cn/problem/P4175
 * 
 * 输入格式：
 * 第一行包含两个整数n和m，分别表示节点数和操作数
 * 第二行包含n个整数，表示每个节点的初始点权
 * 接下来n-1行，每行包含两个整数u和v，表示节点u和v之间有一条边
 * 接下来m行，每行描述一个操作：
 *   - "0 x y" 表示更新操作
 *   - "k x y" 表示查询操作（k > 0）
 * 
 * 输出格式：
 * 对于每个查询操作，如果路径上节点数不足k个，输出"invalid request!"，否则输出第k大的点权值
 */

// #include <bits/stdc++.h>
//
// using namespace std;
//
// const int MAXN = 80001;
// const int MAXT = MAXN * 110;
// const int MAXH = 18;
// int n, m, s;
//
// // 节点权值数组
// int arr[MAXN];
// int ques[MAXN][3];
// int sorted[MAXN << 1];
//
// // 链式前向星存储树结构
// int head[MAXN];
// int nxt[MAXN << 1];
// int to[MAXN << 1];
// int cntg;
//
// // 树状数组，root[i]表示以节点i为根的线段树根节点编号
// int root[MAXN];
// int ls[MAXT];
// int rs[MAXT];
// int sum[MAXT];
// int cntt;
//
// // 树链剖分和DFS序相关数组
// int deep[MAXN];
// int siz[MAXN];
// int dfn[MAXN];
// int stjump[MAXN][MAXH];
// int cntd;
//
// // 查询时使用的辅助数组
// int addTree[MAXN];
// int minusTree[MAXN];
// int cntadd, cntminus;
//
// /**
//  * 添加一条无向边到链式前向星结构中
//  * @param u 起点
//  * @param v 终点
//  */
// void addEdge(int u, int v) {
//     nxt[++cntg] = head[u];
//     to[cntg] = v;
//     head[u] = cntg;
// }
//
// /**
//  * 在已排序的sorted数组中查找数字num的位置（离散化后的值）
//  * @param num 待查找的数字
//  * @return 离散化后的值，如果未找到返回-1
//  */
// int kth(int num) {
//     int ls = 1, rs = s, mid;
//     while (ls <= rs) {
//         mid = (ls + rs) / 2;
//         if (sorted[mid] == num) return mid;
//         else if (sorted[mid] < num) ls = mid + 1;
//         else rs = mid - 1;
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
//  * DFS递归版，用于计算树链剖分所需信息
//  * @param u 当前节点
//  * @param fa 父节点
//  */
// void dfs(int u, int fa) {
//     deep[u] = deep[fa] + 1;
//     siz[u] = 1;
//     dfn[u] = ++cntd;
//     stjump[u][0] = fa;
//     for (int p = 1; p < MAXH; p++) {
//         stjump[u][p] = stjump[stjump[u][p - 1]][p - 1];
//     }
//     for (int e = head[u]; e; e = nxt[e]) {
//         if (to[e] != fa) dfs(to[e], u);
//     }
//     for (int e = head[u]; e; e = nxt[e]) {
//         if (to[e] != fa) siz[u] += siz[to[e]];
//     }
// }
//
// /**
//  * 计算两个节点的最近公共祖先(LCA)
//  * @param a 节点a
//  * @param b 节点b
//  * @return 节点a和b的最近公共祖先
//  */
// int lca(int a, int b) {
//     if (deep[a] < deep[b]) swap(a, b);
//     for (int p = MAXH - 1; p >= 0; p--) {
//         if (deep[stjump[a][p]] >= deep[b]) {
//             a = stjump[a][p];
//         }
//     }
//     if (a == b) return a;
//     for (int p = MAXH - 1; p >= 0; p--) {
//         if (stjump[a][p] != stjump[b][p]) {
//             a = stjump[a][p];
//             b = stjump[b][p];
//         }
//     }
//     return stjump[a][0];
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
//     if (i == 0) i = ++cntt;
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
//  * 在线段树上二分查找第k大的值
//  * @param jobk 查找第k大的值
//  * @param l 当前查询区间左端点
//  * @param r 当前查询区间右端点
//  * @return 第k大值在sorted数组中的索引
//  */
// int innerQuery(int jobk, int l, int r) {
//     if (l == r) return l;
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
//  * @param i DFS序位置
//  * @param val 值（离散化后的索引）
//  * @param cnt 操作数值（+1表示增加，-1表示减少）
//  */
// void add(int i, int val, int cnt) {
//     for (; i <= n; i += lowbit(i)) {
//         root[i] = innerAdd(val, cnt, 1, s, root[i]);
//     }
// }
//
// /**
//  * 更新节点的点权
//  * @param i 需要更新的节点编号
//  * @param v 新的点权值
//  */
// void update(int i, int v) {
//     add(dfn[i], arr[i], -1);
//     add(dfn[i] + siz[i], arr[i], 1);
//     arr[i] = kth(v);
//     add(dfn[i], arr[i], 1);
//     add(dfn[i] + siz[i], arr[i], -1);
// }
//
// /**
//  * 查询树上路径[x, y]中第k大的点权值
//  * @param x 路径起点
//  * @param y 路径终点
//  * @param k 查询第k大
//  * @return 第k大的点权值，如果不存在则返回-1
//  */
// int query(int x, int y, int k) {
//     int xylca = lca(x, y);
//     int lcafa = stjump[xylca][0];
//     int num = deep[x] + deep[y] - deep[xylca] - deep[lcafa];
//     if (num < k) return -1;
//     cntadd = cntminus = 0;
//     for (int i = dfn[x]; i; i -= lowbit(i)) {
//         addTree[++cntadd] = root[i];
//     }
//     for (int i = dfn[y]; i; i -= lowbit(i)) {
//         addTree[++cntadd] = root[i];
//     }
//     for (int i = dfn[xylca]; i; i -= lowbit(i)) {
//         minusTree[++cntminus] = root[i];
//     }
//     for (int i = dfn[lcafa]; i; i -= lowbit(i)) {
//         minusTree[++cntminus] = root[i];
//     }
//     return sorted[innerQuery(num - k + 1, 1, s)];
// }
//
// /**
//  * 预处理函数，包括离散化、DFS序计算和初始化树状数组
//  */
// void prepare() {
//     s = 0;
//     for (int i = 1; i <= n; i++) sorted[++s] = arr[i];
//     for (int i = 1; i <= m; i++) {
//         if (ques[i][0] == 0) sorted[++s] = ques[i][2];
//     }
//     sort(sorted + 1, sorted + s + 1);
//     s = unique(sorted + 1, sorted + s + 1) - sorted - 1;
//     for (int i = 1; i <= n; i++) arr[i] = kth(arr[i]);
//     dfs(1, 0);
//     for (int i = 1; i <= n; i++) {
//         add(dfn[i], arr[i], 1);
//         add(dfn[i] + siz[i], arr[i], -1);
//     }
// }
//
// int main() {
//     ios::sync_with_stdio(false);
//     cin.tie(nullptr);
//     cin >> n >> m;
//     for (int i = 1; i <= n; i++) cin >> arr[i];
//     for (int i = 1, u, v; i < n; i++) {
//         cin >> u >> v;
//         addEdge(u, v);
//         addEdge(v, u);
//     }
//     for (int i = 1; i <= m; i++) cin >> ques[i][0] >> ques[i][1] >> ques[i][2];
//     prepare();
//     for (int i = 1, k, x, y; i <= m; i++) {
//         k = ques[i][0];
//         x = ques[i][1];
//         y = ques[i][2];
//         if (k == 0) {
//         	update(x, y);
//         } else {
//             int ans = query(x, y, k);
//             if(ans == -1) {
//             	cout << "invalid request!" << "\n";
//             } else {
//             	cout << ans << "\n";
//             }
//         }
//     }
//     return 0;
// }