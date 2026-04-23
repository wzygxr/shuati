package class189; // 声明包名

// 消息扩散，C++版
// 一共有n个城市，给定m条道路，道路可以传递消息，但道路是单向
// 你有一个消息，需要让所有城市都收到，计算至少要在几个城市发布该消息
// 1 <= n <= 10^5
// 1 <= m <= 5 * 10^5
// 测试链接 : https://www.luogu.com.cn/problem/P2002
// 如下实现是C++的版本，C++版本和java版本逻辑完全一样
// 提交如下代码，可以通过所有测试用例

/**
 * 【C++版本代码】消息扩散问题
 * 核心思路：缩点后统计入度为0的强连通分量数量
 */

// #include <bits/stdc++.h>
// using namespace std;
// 
// const int MAXN = 100001;  // 最大节点数
// const int MAXM = 500001;  // 最大边数
// int n, m;
// int a[MAXM], b[MAXM];     // 存储边
// 
// // 邻接表
// int head[MAXN], nxt[MAXM], to[MAXM], cntg;
// 
// // Tarjan算法
// int dfn[MAXN], low[MAXN], cntd;
// int sta[MAXN], top;
// 
// // SCC结果
// int belong[MAXN], sccCnt;
// int indegree[MAXN];  // 每个SCC的入度
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
//     // 计算缩点后各SCC的入度
//     for (int i = 1; i <= m; i++) {
//         int scc1 = belong[a[i]], scc2 = belong[b[i]];
//         if (scc1 != scc2) indegree[scc2]++;
//     }
//     
//     // 统计入度为0的SCC数量
//     int ans = 0;
//     for (int i = 1; i <= sccCnt; i++) {
//         if (indegree[i] == 0) ans++;
//     }
//     
//     cout << ans << "\n";
//     return 0;
// }
