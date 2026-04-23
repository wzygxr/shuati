package class189; // 声明包名

// 强连通分量模版题2，C++版
// 给定一张n个点，m条边的有向图
// 求出所有强连通分量，先打印强连通分量的数量
// 每个强连通分量先打印大小，然后打印节点编号，顺序随意
// 1 <= n <= 5 * 10^4
// 1 <= m <= 10^5
// 测试链接 : https://www.luogu.com.cn/problem/U224391
// 如下实现是C++的版本，C++版本和java版本逻辑完全一样
// 提交如下代码，可以通过所有测试用例

/**
 * 【C++版本代码】强连通分量模板题2
 * 本文件包含C++版本的实现，与Java版本逻辑一致
 * 主要区别在于：
 * 1. 使用数组而非ArrayList
 * 2. 使用printf/scanf或cin/cout进行IO
 * 3. 使用min函数而非Math.min
 */

// #include <bits/stdc++.h>  // C++万能头文件
// 
// using namespace std;  // 使用标准命名空间
// 
// // ==================== 常量定义 ====================
// const int MAXN = 50001;    // 最大节点数，5*10^4+1
// const int MAXM = 100001;   // 最大边数，10^5+1
// 
// // ==================== 图的基本信息 ====================
// int n, m;  // n为节点数，m为边数
// 
// // ==================== 邻接表 ====================
// int head[MAXN];   // 邻接表头指针
// int nxt[MAXM];    // 边的下一条边索引
// int to[MAXM];     // 边指向的目标节点
// int cntg;         // 边计数器
// 
// // ==================== Tarjan算法数据结构 ====================
// int dfn[MAXN];    // DFS序数组
// int low[MAXN];    // Low Link数组
// int cntd;         // DFS时间戳计数器
// 
// int sta[MAXN];    // Tarjan栈
// int top;          // 栈顶指针
// 
// // ==================== 强连通分量结果 ====================
// int belong[MAXN]; // 节点所属强连通分量编号
// int sccArr[MAXN]; // 存储所有强连通分量的节点
// int sccSiz[MAXN]; // 每个强连通分量的大小
// int sccl[MAXN];   // 每个强连通分量的起始位置
// int sccr[MAXN];   // 每个强连通分量的结束位置
// int idx;          // sccArr索引计数器
// int sccCnt;       // 强连通分量数量
// 
// // ==================== 添加边 ====================
// /**
//  * 添加边到邻接表
//  * 使用链式前向星实现
//  * 
//  * @param u 起点
//  * @param v 终点
//  */
// void addEdge(int u, int v) {
//     nxt[++cntg] = head[u];  // 新边的next指向原头
//     to[cntg] = v;           // 设置目标节点
//     head[u] = cntg;         // 更新头指针
// }
// 
// // ==================== Tarjan算法 ====================
// /**
//  * Tarjan算法寻找强连通分量
//  * 
//  * 核心逻辑：
//  * 1. 初始化dfn和low
//  * 2. 节点入栈
//  * 3. DFS遍历邻接边
//  * 4. 判断强连通分量根
//  * 
//  * @param u 当前节点
//  */
// void tarjan(int u) {
//     // 初始化dfn和low为当前时间戳
//     dfn[u] = low[u] = ++cntd;
//     // 节点入栈
//     sta[++top] = u;
//     // 遍历所有邻接边
//     for (int e = head[u]; e > 0; e = nxt[e]) {
//         int v = to[e];  // 邻接节点
//         if (dfn[v] == 0) {  // 树边，v未访问
//             tarjan(v);  // 递归处理
//             low[u] = min(low[u], low[v]);  // 更新low
//         } else {
//             // 回边判断：v在栈中
//             if (belong[v] == 0) {
//                 low[u] = min(low[u], dfn[v]);  // 更新low
//             }
//         }
//     }
//     // 检查是否为强连通分量根
//     if (dfn[u] == low[u]) {
//         sccCnt++;  // 强连通分量计数加1
//         sccl[sccCnt] = idx + 1;  // 记录起始位置
//         int pop;  // 临时变量
//         // 弹出栈中节点直到u
//         do {
//             pop = sta[top--];  // 弹出栈顶
//             belong[pop] = sccCnt;  // 标记所属SCC
//             sccArr[++idx] = pop;   // 加入结果数组
//             sccSiz[sccCnt]++;      // SCC大小加1
//         } while (pop != u);
//         sccr[sccCnt] = idx;  // 记录结束位置
//     }
// }
// 
// // ==================== 主函数 ====================
// /**
//  * 主函数
//  * 
//  * 执行流程：
//  * 1. 加速IO设置
//  * 2. 读取输入
//  * 3. 建图
//  * 4. 运行Tarjan算法
//  * 5. 输出结果
//  */
// int main() {
//     ios::sync_with_stdio(false);  // 关闭同步加速
//     cin.tie(nullptr);             // 解除绑定加速
//     
//     cin >> n >> m;  // 读取节点数和边数
//     
//     // 读取m条边
//     for (int i = 1, u, v; i <= m; i++) {
//         cin >> u >> v;  // 读取边
//         addEdge(u, v);  // 添加边
//     }
//     
//     // 对所有未访问节点运行Tarjan算法
//     for (int i = 1; i <= n; i++) {
//         if (dfn[i] == 0) {  // 节点i未访问
//             tarjan(i);
//         }
//     }
//     
//     // 输出强连通分量数量
//     cout << sccCnt << "\n";
//     
//     // 输出每个强连通分量的信息
//     for (int i = 1; i <= sccCnt; i++) {
//         // 先输出大小
//         cout << sccSiz[i] << " ";
//         // 输出所有节点
//         for (int j = sccl[i]; j <= sccr[i]; j++) {
//             cout << sccArr[j] << " ";
//         }
//         cout << "\n";
//     }
//     
//     return 0;
// }
