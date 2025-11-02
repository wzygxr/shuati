package class158;

/**
 * 更为厉害，C++版
 * 
 * 题目来源：洛谷 P3899 - [湖南集训]谈笑风生
 * 题目链接：https://www.luogu.com.cn/problem/P3899
 * 
 * 题目描述:
 * 为了方便理解，改写题意（与原始题意等效）：
 * 有n个节点，编号1~n，给定n-1条边，连成一棵树，1号点是树头
 * 如果x是y的祖先节点，认为"x比y更厉害"
 * 如果x到y的路径上，边的数量 <= 某个常数，认为"x和y是邻居"
 * 一共有m条查询，每条查询 a k : 打印有多少三元组(a, b, c)满足如下规定
 * a、b、c为三个不同的点；a和b都比c厉害；a和b是邻居，路径边的数量 <= 给定的k
 * 
 * 解题思路:
 * 使用可持久化线段树（主席树）结合DFS序解决该问题。
 * 1. 通过DFS遍历树，计算每个节点的深度和子树大小
 * 2. 利用DFS序将树上问题转化为区间问题
 * 3. 对于每个节点，建立主席树维护其子树信息
 * 4. 对于查询节点a和常数k，计算满足条件的三元组数量
 * 
 * 时间复杂度: O(n log n + m log n)
 * 空间复杂度: O(n log n)
 * 
 * 示例:
 * 输入:
 * 5 2
 * 1 2
 * 1 3
 * 2 4
 * 2 5
 * 1 2
 * 2 1
 * 
 * 输出:
 * 6
 * 2
 * 
 * 解释:
 * 查询1 2：节点1为根，k=2，满足条件的三元组有6个
 * 查询2 1：节点2为根，k=1，满足条件的三元组有2个
 * 
 * 注意：如下实现是C++的版本，C++版本和java版本逻辑完全一样
 * 提交如下代码，可以通过所有测试用例
 */
//#include <bits/stdc++.h>
//
//using namespace std;
//
//const int MAXN = 300001;
//const int MAXT = MAXN * 22;
//int n, m, depth;
//
//int head[MAXN];
//int to[MAXN << 1];
//int nxt[MAXN << 1];
//int cntg = 0;
//
//int root[MAXN];
//int ls[MAXT];
//int rs[MAXT];
//long long sum[MAXT];
//int cntt = 0;
//
//int dep[MAXN];
//int siz[MAXN];
//int dfn[MAXN];
//int cntd = 0;
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
// * 构建空线段树
// * @param l 区间左端点
// * @param r 区间右端点
// * @return 根节点编号
// */
//int build(int l, int r) {
//    int rt = ++cntt;
//    sum[rt] = 0LL;
//    if (l < r) {
//        int mid = (l + r) >> 1;
//        ls[rt] = build(l, mid);
//        rs[rt] = build(mid + 1, r);
//    }
//    return rt;
//}
//
///** 
// * 更新线段树节点
// * @param jobi 要更新的位置
// * @param jobv 要增加的值
// * @param l 当前区间左端点
// * @param r 当前区间右端点
// * @param i 前一个版本的节点编号
// * @return 新版本的根节点编号
// */
//int add(int jobi, long long jobv, int l, int r, int i) {
//    int rt = ++cntt;
//    ls[rt] = ls[i];
//    rs[rt] = rs[i];
//    sum[rt] = sum[i] + jobv;
//    if (l < r) {
//        int mid = (l + r) >> 1;
//        if (jobi <= mid) {
//            ls[rt] = add(jobi, jobv, l, mid, ls[rt]);
//        } else {
//            rs[rt] = add(jobi, jobv, mid + 1, r, rs[rt]);
//        }
//    }
//    return rt;
//}
//
///** 
// * 查询区间[jobl,jobr]的和
// * @param jobl 查询区间左端点
// * @param jobr 查询区间右端点
// * @param l 当前区间左端点
// * @param r 当前区间右端点
// * @param u 前一个版本的根节点
// * @param v 当前版本的根节点
// * @return 区间和
// */
//long long query(int jobl, int jobr, int l, int r, int u, int v) {
//    if (jobl <= l && r <= jobr) {
//        return sum[v] - sum[u];
//    }
//    long long ans = 0;
//    int mid = (l + r) >> 1;
//    if (jobl <= mid) {
//        ans += query(jobl, jobr, l, mid, ls[u], ls[v]);
//    }
//    if (jobr > mid) {
//        ans += query(jobl, jobr, mid + 1, r, rs[u], rs[v]);
//    }
//    return ans;
//}
//
///** 
// * DFS遍历树并计算节点信息
// * @param u 当前节点
// * @param f 父节点
// */
//void dfs1(int u, int f) {
//    dep[u] = dep[f] + 1;
//    depth = max(depth, dep[u]);
//    siz[u] = 1;
//    dfn[u] = ++cntd;
//    for (int ei = head[u]; ei > 0; ei = nxt[ei]) {
//        if (to[ei] != f) {
//            dfs1(to[ei], u);
//        }
//    }
//    for (int ei = head[u]; ei > 0; ei = nxt[ei]) {
//        if (to[ei] != f) {
//            siz[u] += siz[to[ei]];
//        }
//    }
//}
//
///** 
// * DFS遍历树并构建主席树
// * @param u 当前节点
// * @param f 父节点
// */
//void dfs2(int u, int f) {
//    root[dfn[u]] = add(dep[u], (long long)siz[u] - 1, 1, depth, root[dfn[u] - 1]);
//    for (int ei = head[u]; ei > 0; ei = nxt[ei]) {
//        if (to[ei] != f) {
//            dfs2(to[ei], u);
//        }
//    }
//}
//
///** 
// * 预处理，建立主席树
// */
//void prepare() {
//    depth = 0;
//    dfs1(1, 0);
//    root[0] = build(1, depth);
//    dfs2(1, 0);
//}
//
///** 
// * 计算查询a k的结果
// * @param a 查询节点
// * @param k 路径长度限制
// * @return 满足条件的三元组数量
// */
//long long compute(int a, int k) {
//    // 计算a的子树中深度不超过dep[a]+k的节点贡献
//    long long ans = (long long)(siz[a] - 1) * min(k, dep[a] - 1);
//    // 查询dfn序在[dfn[a], dfn[a]+siz[a]-1]范围内，深度在[dep[a]+1, dep[a]+k]的节点贡献
//    ans += query(dep[a] + 1, dep[a] + k, 1, depth, root[dfn[a] - 1], root[dfn[a] + siz[a] - 1]);
//    return ans;
//}
//
//int main() {
//    ios::sync_with_stdio(false);
//    cin.tie(nullptr);
//    cin >> n >> m;
//    for (int i = 1, u, v; i < n; i++) {
//        cin >> u >> v;
//        addEdge(u, v);
//        addEdge(v, u);
//    }
//    prepare();
//    for(int i = 1, a, k; i <= m; i++) {
//        cin >> a >> k;
//        cout << compute(a, k) << "\n";
//    }
//    return 0;
//}