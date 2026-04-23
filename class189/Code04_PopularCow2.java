package class189; // 声明包名

// 受欢迎的牛，C++版
// 一共有n只牛，牛和牛之间存在喜欢关系，喜欢关系是有向的
// 喜欢关系可以传递，如果a喜欢b，b喜欢c，那么a也喜欢c
// 每只牛都喜欢自己，如果某只牛被所有牛喜欢，那么这只牛是明星
// 给定m个喜欢关系，打印明星的数量
// 1 <= n <= 10^4
// 1 <= m <= 5 * 10^4
// 测试链接 : https://www.luogu.com.cn/problem/P2341
// 如下实现是C++的版本，C++版本和java版本逻辑完全一样
// 提交如下代码，可以通过所有测试用例

/**
 * 【C++版本代码】受欢迎的牛问题
 * 核心思路：缩点后找出度为0的强连通分量
 * 如果只有一个出度为0的SCC，该SCC的大小就是答案
 */

// #include <bits/stdc++.h>
// using namespace std;
// 
// const int MAXN = 10001;   // 最大节点数
// const int MAXM = 50001;   // 最大边数
// int n, m;                 // 节点数和边数
// int a[MAXM], b[MAXM];     // 存储边的起点和终点
// 
// // 邻接表
// int head[MAXN], nxt[MAXM], to[MAXM], cntg;
// 
// // Tarjan算法
// int dfn[MAXN], low[MAXN], cntd;
// int sta[MAXN], top;
// 
// // SCC结果
// int belong[MAXN], sccSiz[MAXN], sccCnt;
// int outdegree[MAXN];  // 每个SCC的出度
// 
// // 添加边
// void addEdge(int u, int v) {
//     nxt[++cntg] = head[u];
//     to[cntg] = v;
//     head[u] = cntg;
// }
// 
// // Tarjan算法
// void tarjan(int u) {
//     dfn[u] = low[u] = ++cntd;
//     sta[++top] = u;
//     for (int e = head[u]; e > 0; e = nxt[e]) {
//         int v = to[e];
//         if (dfn[v] == 0) {
//             tarjan(v);
//             low[u] = min(low[u], low[v]);
//         } else if (belong[v] == 0) {
//             low[u] = min(low[u], dfn[v]);
//         }
//     }
//     if (dfn[u] == low[u]) {
//         sccCnt++;
//         int pop;
//         do {
//             pop = sta[top--];
//             belong[pop] = sccCnt;
//             sccSiz[sccCnt]++;
//         } while (pop != u);
//     }
// }
// 
// int main() {
//     ios::sync_with_stdio(false);
//     cin.tie(nullptr);
//     
//     cin >> n >> m;
//     for (int i = 1; i <= m; i++) {
//         cin >> a[i] >> b[i];
//         addEdge(a[i], b[i]);
//     }
//     
//     // 找强连通分量
//     for (int i = 1; i <= n; i++) {
//         if (dfn[i] == 0) tarjan(i);
//     }
//     
//     // 计算缩点后各SCC的出度
//     for (int i = 1; i <= m; i++) {
//         int scc1 = belong[a[i]], scc2 = belong[b[i]];
//         if (scc1 != scc2) outdegree[scc1]++;
//     }
//     
//     // 统计出度为0的SCC
//     int num = 0, siz = 0;
//     for (int i = 1; i <= sccCnt; i++) {
//         if (outdegree[i] == 0) {
//             num++;
//             siz = sccSiz[i];
//         }
//         if (num > 1) { siz = 0; break; }
//     }
//     
//     cout << siz << "\n";
//     return 0;
// }
