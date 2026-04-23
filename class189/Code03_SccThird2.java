package class189; // 声明包名

// 强连通分量模版题3，C++版
// 一共有n个节点，给定m条边，边的格式 a b t
// 如果t为1，表示a到b的单向边，如果t为2，表示a到b的双向边
// 找到图中最大的强连通分量，先打印大小，然后打印包含的节点，编号从小到大输出
// 如果有多个最大的强连通分量，打印字典序最小的结果
// 1 <= n <= 5 * 10^3
// 0 <= m <= 5 * 10^4
// 测试链接 : https://www.luogu.com.cn/problem/P1726
// 如下实现是C++的版本，C++版本和java版本逻辑完全一样
// 提交如下代码，可以通过所有测试用例

/**
 * 【C++版本代码】强连通分量模板题3
 * 本文件包含C++版本的实现，支持单向边和双向边
 * 需要找到最大的强连通分量，多个时输出字典序最小的
 */

// #include <bits/stdc++.h>  // C++万能头文件
// 
// using namespace std;  // 使用标准命名空间
// 
// // ==================== 常量定义 ====================
// const int MAXN = 5001;     // 最大节点数
// const int MAXM = 100001;   // 最大边数（双向边占两条）
// int n, m;                  // 节点数和边数
// 
// // ==================== 邻接表 ====================
// int head[MAXN];   // 邻接表头
// int nxt[MAXM];    // 下一条边
// int to[MAXM];     // 目标节点
// int cntg;         // 边计数器
// 
// // ==================== Tarjan算法 ====================
// int dfn[MAXN];    // DFS序
// int low[MAXN];    // Low Link
// int cntd;         // 时间戳计数器
// int sta[MAXN];    // 栈
// int top;          // 栈顶指针
// 
// // ==================== SCC结果 ====================
// int belong[MAXN]; // 节点所属SCC
// int sccSiz[MAXN]; // 每个SCC的大小
// int sccCnt;       // SCC数量
// 
// // ==================== 添加边 ====================
// void addEdge(int u, int v) {
//     nxt[++cntg] = head[u];
//     to[cntg] = v;
//     head[u] = cntg;
// }
// 
// // ==================== Tarjan算法 ====================
// void tarjan(int u) {
//     dfn[u] = low[u] = ++cntd;  // 初始化
//     sta[++top] = u;            // 入栈
//     for (int e = head[u]; e > 0; e = nxt[e]) {
//         int v = to[e];
//         if (dfn[v] == 0) {     // 树边
//             tarjan(v);
//             low[u] = min(low[u], low[v]);
//         } else {
//             if (belong[v] == 0) {  // 回边
//                 low[u] = min(low[u], dfn[v]);
//             }
//         }
//     }
//     if (dfn[u] == low[u]) {    // SCC根
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
// // ==================== 主函数 ====================
// int main() {
//     ios::sync_with_stdio(false);
//     cin.tie(nullptr);
//     
//     cin >> n >> m;  // 读取节点数和边数
//     
//     // 读取m条边
//     for (int i = 1, a, b, t; i <= m; i++) {
//         cin >> a >> b >> t;  // 读取起点、终点、类型
//         if (t == 1) {        // 单向边
//             addEdge(a, b);
//         } else {             // 双向边
//             addEdge(a, b);
//             addEdge(b, a);
//         }
//     }
//     
//     // 运行Tarjan算法
//     for (int i = 1; i <= n; i++) {
//         if (dfn[i] == 0) {
//             tarjan(i);
//         }
//     }
//     
//     // 找出最大的强连通分量
//     int largest = 0;
//     for (int i = 1; i <= sccCnt; i++) {
//         largest = max(largest, sccSiz[i]);
//     }
//     
//     cout << largest << "\n";  // 输出最大SCC大小
//     
//     // 输出字典序最小的最大SCC
//     for (int i = 1; i <= n; i++) {
//         if (sccSiz[belong[i]] == largest) {
//             int scc = belong[i];
//             for (int j = i; j <= n; j++) {
//                 if (belong[j] == scc) {
//                     cout << j << " ";
//                 }
//             }
//             break;
//         }
//     }
//     cout << "\n";
//     
//     return 0;
// }
