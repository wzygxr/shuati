package class158;

/**
 * 路径上的第k小，C++版
 * 
 * 题目来源：洛谷 P2633 - Count on a tree
 * 题目链接：https://www.luogu.com.cn/problem/P2633
 * 
 * 题目描述:
 * 有n个节点，编号1~n，每个节点有权值，有n-1条边，所有节点组成一棵树
 * 一共有m条查询，每条查询 u v k : 打印u号点到v号点的路径上，第k小的点权
 * 
 * 解题思路:
 * 使用树上可持久化线段树（树上主席树）结合LCA解决该问题。
 * 1. 对节点权值进行离散化处理
 * 2. 通过DFS遍历树，为每个节点建立主席树
 * 3. 利用DFS序和LCA算法计算树上路径信息
 * 4. 对于查询u到v的路径，利用容斥原理计算路径上的第k小值
 * 
 * 强制在线处理:
 * 题目有强制在线的要求，上一次打印的答案为lastAns，初始时lastAns = 0
 * 每次给定的u、v、k，按照如下方式得到真实的u、v、k，查询完成后更新lastAns
 * 真实u = 给定u ^ lastAns
 * 真实v = 给定v
 * 真实k = 给定k
 * 
 * 时间复杂度: O((n + m) log n)
 * 空间复杂度: O(n log n)
 * 
 * 示例:
 * 输入:
 * 5 3
 * 1 2 3 4 5
 * 1 2
 * 1 3
 * 2 4
 * 2 5
 * 4 5 2
 * 3 4 3
 * 1 2 1
 * 
 * 输出:
 * 3
 * 4
 * 1
 * 
 * 解释:
 * 查询4 5 2：节点4到节点5的路径为[4,2,5]，点权为[4,2,5]，第2小为3
 * 查询3 4 3：节点3到节点4的路径为[3,1,2,4]，点权为[3,1,2,4]，第3小为4
 * 查询1 2 1：节点1到节点2的路径为[1,2]，点权为[1,2]，第1小为1
 * 
 * 注意：如下实现是C++的版本，C++版本和java版本逻辑完全一样
 * 提交如下代码，可以通过所有测试用例
 */
//#include <bits/stdc++.h>
//
//using namespace std;
//
//const int MAXN = 100001;
//const int MAXH = 20;
//const int MAXT = MAXN * MAXH;
//int n, m, s;
//int arr[MAXN];
//int sorted[MAXN];
//
//int head[MAXN];
//int to[MAXN << 1];
//int nxt[MAXN << 1];
//int cntg = 0;
//
//int root[MAXN];
//int ls[MAXT];
//int rs[MAXT];
//int siz[MAXT];
//int cntt = 0;
//
//int deep[MAXN];
//int stjump[MAXN][MAXH];
//
///** 
// * 二分查找数字num在sorted数组中的位置
// * @param num 要查找的数字
// * @return 数字在sorted数组中的位置
// */
//int kth(int num) {
//    int left = 1, right = s, mid;
//    while (left <= right) {
//        mid = (left + right) / 2;
//        if (sorted[mid] == num) {
//            return mid;
//        } else if (sorted[mid] < num) {
//            left = mid + 1;
//        } else {
//            right = mid - 1;
//        }
//    }
//    return -1;
//}
//
///** 
// * 构建空线段树
// * @param l 区间左端点
// * @param r 区间右端点
// * @return 根节点编号
// */
//int build(int l, int r) {
//    int rt = ++cntt;
//    siz[rt] = 0;
//    if (l < r) {
//        int mid = (l + r) / 2;
//        ls[rt] = build(l, mid);
//        rs[rt] = build(mid + 1, r);
//    }
//    return rt;
//}
//
///** 
// * 预处理，对节点权值进行离散化
// */
//void prepare() {
//    for (int i = 1; i <= n; i++) {
//        sorted[i] = arr[i];
//    }
//    sort(sorted + 1, sorted + n + 1);
//    s = 1;
//    for (int i = 2; i <= n; i++) {
//        if (sorted[s] != sorted[i]) {
//            sorted[++s] = sorted[i];
//        }
//    }
//    root[0] = build(1, s);
//}
//
///** 
// * 添加边
// * @param u 起点
// * @param v 终点
// */
//void addEdge(int u, int v) {
//    nxt[++cntg] = head[u];
//    to[cntg] = v;
//    head[u] = cntg;
//}
//
///** 
// * 更新线段树节点
// * @param jobi 要更新的位置
// * @param l 当前区间左端点
// * @param r 当前区间右端点
// * @param i 前一个版本的节点编号
// * @return 新版本的根节点编号
// */
//int insert(int jobi, int l, int r, int i) {
//    int rt = ++cntt;
//    ls[rt] = ls[i];
//    rs[rt] = rs[i];
//    siz[rt] = siz[i] + 1;
//    if (l < r) {
//        int mid = (l + r) / 2;
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
// * 查询路径上第k小的点权
// * @param jobk 要查询的排名
// * @param l 当前区间左端点
// * @param r 当前区间右端点
// * @param u 节点u的根节点
// * @param v 节点v的根节点
// * @param lca 节点u和v的LCA的根节点
// * @param lcafa LCA父节点的根节点
// * @return 第k小的点权在离散化数组中的位置
// */
//int query(int jobk, int l, int r, int u, int v, int lca, int lcafa) {
//    if (l == r) {
//        return l;
//    }
//    // 计算左子树中数的个数
//    int lsiz = siz[ls[u]] + siz[ls[v]] - siz[ls[lca]] - siz[ls[lcafa]];
//    int mid = (l + r) / 2;
//    if (lsiz >= jobk) {
//        return query(jobk, l, mid, ls[u], ls[v], ls[lca], ls[lcafa]);
//    } else {
//        return query(jobk - lsiz, mid + 1, r, rs[u], rs[v], rs[lca], rs[lcafa]);
//    }
//}
//
///** 
// * DFS遍历树并构建主席树
// * @param u 当前节点
// * @param f 父节点
// */
//void dfs(int u, int f) {
//    root[u] = insert(kth(arr[u]), 1, s, root[f]);
//    deep[u] = deep[f] + 1;
//    stjump[u][0] = f;
//    for (int p = 1; p < MAXH; p++) {
//        stjump[u][p] = stjump[stjump[u][p - 1]][p - 1];
//    }
//    for (int ei = head[u]; ei > 0; ei = nxt[ei]) {
//        if (to[ei] != f) {
//            dfs(to[ei], u);
//        }
//    }
//}
//
///** 
// * 计算节点a和节点b的最近公共祖先(LCA)
// * @param a 节点a
// * @param b 节点b
// * @return 节点a和节点b的LCA
// */
//int lca(int a, int b) {
//    if (deep[a] < deep[b]) {
//        swap(a, b);
//    }
//    // 将a提升到与b同一深度
//    for (int p = MAXH - 1; p >= 0; p--) {
//        if (deep[stjump[a][p]] >= deep[b]) {
//            a = stjump[a][p];
//        }
//    }
//    if (a == b) {
//        return a;
//    }
//    // 同时提升a和b直到它们的父节点相同
//    for (int p = MAXH - 1; p >= 0; p--) {
//        if (stjump[a][p] != stjump[b][p]) {
//            a = stjump[a][p];
//            b = stjump[b][p];
//        }
//    }
//    return stjump[a][0];
//}
//
///** 
// * 查询节点u到节点v路径上第k小的点权
// * @param u 起点
// * @param v 终点
// * @param k 要查询的排名
// * @return 第k小的点权
// */
//int kth(int u, int v, int k) {
//    int lcaNode = lca(u, v);
//    int i = query(k, 1, s, root[u], root[v], root[lcaNode], root[stjump[lcaNode][0]]);
//    return sorted[i];
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
//    for (int i = 1, u, v; i < n; i++) {
//        cin >> u >> v;
//        addEdge(u, v);
//        addEdge(v, u);
//    }
//    dfs(1, 0);
//    for (int i = 1, u, v, k, lastAns = 0; i <= m; i++) {
//        cin >> u >> v >> k;
//        u ^= lastAns;
//        lastAns = kth(u, v, k);
//        cout << lastAns << '\n';
//    }
//    return 0;
//}