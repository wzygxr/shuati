package class159;

// 字符串树，C++版
// 一共有n个节点，n-1条边，组成一棵树，每条边的边权为字符串
// 一共有m条查询，每条查询的格式为
// u v s : 查询节点u到节点v的路径中，有多少边的字符串以字符串s作为前缀
// 1 <= n、m <= 10^5
// 所有字符串长度不超过10，并且都由字符a~z组成
// 测试链接 : https://www.luogu.com.cn/problem/P6088
// 如下实现是C++的版本，C++版本和java版本逻辑完全一样
// 提交如下代码，可以通过所有测试用例

// 补充题目1: 字符串前缀查询
// 给定一个字符串数组和多个查询，每个查询包含一个字符串，要求找出数组中以该字符串为前缀的字符串数量
// 可以使用Trie树解决
// 相关题目:
// - https://leetcode.cn/problems/longest-common-prefix/
// - https://leetcode.cn/problems/implement-trie-prefix-tree/
// - https://www.luogu.com.cn/problem/P2580

// 补充题目2: 树上路径字符串查询
// 在树结构中，每条边有权值（字符串），查询两点间路径上满足特定条件的边数量
// 相关题目:
// - https://www.luogu.com.cn/problem/P6088
// - https://codeforces.com/problemset/problem/1076/E
// - https://www.hdu.edu.cn/problem/6394

// 补充题目3: LCA应用 - 树上路径查询
// 利用最近公共祖先(LCA)算法解决树上路径查询问题
// 相关题目:
// - https://www.luogu.com.cn/problem/P3379
// - https://codeforces.com/problemset/problem/1304/E
// - https://www.spoj.com/problems/LCA/

//#include <bits/stdc++.h>
//
//using namespace std;
//
//// 最大节点数
//static const int MAXN = 100001;
//
//// Trie树最大节点数
//static const int MAXT = 1000001;
//
//// 倍增数组最大高度
//static const int MAXH = 20;
//
//// 节点数和查询数
//int n, m;
//
//// 链式前向星需要的数组
//// head[i]表示节点i的第一条边的编号
//int head[MAXN];
//// nxt[i]表示第i条边的下一条边的编号
//int nxt[MAXN << 1];
//// to[i]表示第i条边指向的节点
//int to[MAXN << 1];
//// weight[i]表示第i条边的权值（字符串）
//string weight[MAXN << 1];
//// 边的计数器
//int cntg = 0;
//
//// 可持久化前缀树需要的数组
//// root[i]表示节点i对应的可持久化Trie树根节点编号
//int root[MAXN];
//// tree[i][j]表示Trie树节点i的第j个子节点编号（1-26对应a-z，0表示空）
//int tree[MAXT][27];
//// pass[i]表示经过Trie树节点i的字符串数量
//int pass[MAXT];
//// Trie树节点计数器
//int cntt = 0;
//
//// 树上倍增和LCA需要的数组
//// deep[i]表示节点i的深度
//int deep[MAXN];
//// stjump[i][j]表示节点i向上跳2^j步到达的节点
//int stjump[MAXN][MAXH];
//
///**
// * 添加一条无向边到链式前向星
// * @param u 起点
// * @param v 终点
// * @param w 边权（字符串）
// */
//void addEdge(int u, int v, const string &w) {
//	// 创建新边
//	nxt[++cntg] = head[u];
//    to[cntg] = v;
//    weight[cntg] = w;
//    head[u] = cntg;
//}
//
///**
// * 将字符转换为数字（a->1, b->2, ..., z->26）
// * @param c 字符
// * @return 对应的数字
// */
//int num(char c) {
//    return c - 'a' + 1;
//}
//
///**
// * 克隆Trie树节点
// * @param i 要克隆的节点编号
// * @return 新节点编号
// */
//int clone(int i) {
//    // 创建新节点
//    int rt = ++cntt;
//    // 复制子节点信息
//    for (int c = 1; c <= 26; c++) {
//        tree[rt][c] = tree[i][c];
//    }
//    // 复制经过该节点的字符串数量
//    pass[rt] = pass[i];
//    return rt;
//}
//
///**
// * 在可持久化Trie树中插入字符串
// * @param str 要插入的字符串
// * @param i 前一个版本的根节点编号
// * @return 新版本的根节点编号
// */
//int insert(const string &str, int i) {
//    // 克隆根节点
//    int rt = clone(i);
//    // 经过根节点的字符串数量加1
//    pass[rt]++;
//    int pre = rt;
//    // 逐字符插入字符串
//    for (int j = 0; j < (int)str.size(); j++) {
//        // 获取当前字符对应的数字
//        int path = num(str[j]);
//        // 获取前一个版本中对应子节点
//        i = tree[i][path];
//        // 克隆子节点
//        int cur = clone(i);
//        // 经过该节点的字符串数量加1
//        pass[cur]++;
//        // 连接父子节点
//        tree[pre][path] = cur;
//        pre = cur;
//    }
//    return rt;
//}
//
///**
// * 在Trie树中查询以指定字符串为前缀的字符串数量
// * @param str 查询的前缀字符串
// * @param i Trie树根节点编号
// * @return 匹配的字符串数量
// */
//int query(const string &str, int i) {
//    // 逐字符匹配前缀
//    for (int j = 0; j < (int)str.size(); j++) {
//        // 获取当前字符对应的数字
//        int path = num(str[j]);
//        // 移动到子节点
//        i = tree[i][path];
//        // 如果节点不存在，返回0
//        if (!i) return 0;
//    }
//    // 返回经过该节点的字符串数量
//    return pass[i];
//}
//
///**
// * DFS遍历树，构建可持久化Trie树和LCA所需信息
// * @param u 当前节点
// * @param fa 父节点
// * @param path 到当前节点的路径字符串
// */
//void dfs(int u, int fa, const string &path) {
//    // 在父节点的Trie树基础上插入路径字符串
//    root[u] = insert(path, root[fa]);
//    // 计算节点深度
//    deep[u] = deep[fa] + 1;
//    // 设置直接父节点
//    stjump[u][0] = fa;
//    // 倍增计算祖先节点
//    for (int p = 1; p < MAXH; p++) {
//        stjump[u][p] = stjump[stjump[u][p - 1]][p - 1];
//    }
//    // 遍历子节点
//    for (int e = head[u]; e; e = nxt[e]) {
//        if (to[e] != fa) {
//            dfs(to[e], u, weight[e]);
//        }
//    }
//}
//
///**
// * 计算两个节点的最近公共祖先(LCA)
// * @param a 节点a
// * @param b 节点b
// * @return 最近公共祖先节点编号
// */
//int lca(int a, int b) {
//    // 确保a节点深度不小于b节点
//    if (deep[a] < deep[b]) swap(a, b);
//    // 将a节点向上跳到与b节点同一深度
//    for (int p = MAXH - 1; p >= 0; p--) {
//        if (deep[stjump[a][p]] >= deep[b]) {
//            a = stjump[a][p];
//        }
//    }
//    // 如果a和b在同一节点，直接返回
//    if (a == b) return a;
//    // 同时向上跳，直到找到最近公共祖先
//    for (int p = MAXH - 1; p >= 0; p--) {
//        if (stjump[a][p] != stjump[b][p]) {
//            a = stjump[a][p];
//            b = stjump[b][p];
//        }
//    }
//    // 返回最近公共祖先的父节点
//    return stjump[a][0];
//}
//
///**
// * 计算树上路径中以指定字符串为前缀的边数量
// * 利用容斥原理：u到v路径上的边 = (根到u的路径) + (根到v的路径) - 2*(根到lca的路径)
// * @param u 起点
// * @param v 终点
// * @param s 查询的前缀字符串
// * @return 匹配的边数量
// */
//int compute(int u, int v, const string &s) {
//    return query(s, root[u]) + query(s, root[v]) - 2 * query(s, root[lca(u, v)]);
//}
//
//int main() {
//    ios::sync_with_stdio(false);
//    cin.tie(nullptr);
//    cin >> n;
//    // 读入树的边信息
//    for (int i = 1; i < n; i++) {
//        int u, v;
//        string s;
//        cin >> u >> v >> s;
//        // 添加无向边
//        addEdge(u, v, s);
//        addEdge(v, u, s);
//    }
//    // DFS遍历树
//    dfs(1, 0, "");
//    cin >> m;
//    // 处理查询
//    while (m--) {
//        int u, v;
//        string s;
//        cin >> u >> v >> s;
//        // 输出查询结果
//        cout << compute(u, v, s) << "\n";
//    }
//    return 0;
//}