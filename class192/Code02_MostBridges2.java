package class192;

// 最多的桥，C++版
// 给定一张无向图，一共n个点、m条边，保证所有点连通
// 沿途的边只能经过一次，找到能通过最多割边的路径，打印割边的数量
// 1 <= n、m <= 3 * 10^5
// 测试链接 : https://www.luogu.com.cn/problem/CF1000E
// 测试链接 : https://codeforces.com/problemset/problem/1000_E
// 如下实现是C++的版本，C++版本和java版本逻辑完全一样
// 提交如下代码，可以通过所有测试用例

// 注意：以下为C++代码实现，已被注释掉，仅供参考
// Java版本请参考 Code02_MostBridges1.java

// C++代码逻辑说明：
// 1. 使用Tarjan算法求边双连通分量（E-DCC）
// 2. 将边双连通分量缩点，形成一棵树（树中的边都是割边）
// 3. 在这棵树上求树的直径（最长路径）
// 4. 树的直径长度即为最多能经过的割边数量

//#include <bits/stdc++.h>  // 引入C++标准库头文件
//
//using namespace std;      // 使用标准命名空间
//
//// ------------------- 静态常量定义 -------------------
//const int MAXN = 300001;  // 最大节点数量，加1用于数组下标从1开始
//const int MAXM = 300001;  // 最大边数量，加1用于数组下标从1开始
//
//// ------------------- 全局变量定义 -------------------
//int n, m;                // 节点数n，边数m
//int a[MAXM];             // 边的第一个端点数组
//int b[MAXM];             // 边的第二个端点数组
//
//// 邻接表数据结构
//int head[MAXN];          // 头指针数组
//int nxt[MAXM << 1];     // 下一条边指针数组
//int to[MAXM << 1];      // 边的终点数组
//int cntg;                // 当前边的编号计数器
//
//// Tarjan算法相关变量
//int dfn[MAXN];          // 深度优先搜索编号数组
//int low[MAXN];          // Low值数组
//int cntd;               // 时间戳计数器
//
//// 栈相关变量
//int sta[MAXN];          // Tarjan算法使用的栈
//int top;                // 栈顶指针
//
//// 边双连通分量相关变量
//int belong[MAXN];       // 节点所属的边双连通分量编号
//int ebccCnt;            // 边双连通分量的数量
//
//// 树的直径相关变量
//int dist[MAXN];         // dist[u]表示从根节点到节点u的距离
//int diameter;           // 树的直径（最长路径的长度）
//
//// 添加边的函数
//void addEdge(int u, int v) {
//    nxt[++cntg] = head[u];  // 将当前边插入到节点u的邻接表头部
//    to[cntg] = v;            // 设置当前边的终点为v
//    head[u] = cntg;         // 更新节点u的头指针
//}
//
//// Tarjan算法求边双连通分量（E-DCC）
//// 边双连通分量是指图中删除任意一条边后仍然连通的极大子图
//// 
//// 算法原理：
//// 1. dfn[u]记录节点u被访问的时间戳
//// 2. low[u]记录节点u及其子树中所有节点能追溯到的最小dfn值
//// 3. 如果dfn[u] == low[u]，说明找到了一个边双连通分量的根
//// 4. 栈中从u到栈顶的所有节点属于同一个边双连通分量
//void tarjan(int u, int preEdge) {
//    dfn[u] = low[u] = ++cntd;  // 初始化dfn和low值
//    sta[++top] = u;            // 将当前节点入栈
//    
//    // 遍历节点u的所有邻接边
//    for (int e = head[u]; e > 0; e = nxt[e]) {
//        // 如果这条边是来自父节点的边，则跳过
//        if ((e ^ 1) == preEdge) {
//            continue;
//        }
//        
//        int v = to[e];  // 获取边的终点节点
//        
//        // 如果节点v还没有被访问过（树边）
//        if (dfn[v] == 0) {
//            tarjan(v, e);  // 递归访问子节点
//            low[u] = min(low[u], low[v]);  // 更新low值
//        } else {
//            // 如果节点v已经被访问过（回边）
//            low[u] = min(low[u], dfn[v]);
//        }
//    }
//    
//    // 如果dfn[u] == low[u]，说明u是某个边双连通分量的根
//    if (dfn[u] == low[u]) {
//        ebccCnt++;  // 找到一个新的边双连通分量
//        int pop;
//        // 弹出栈中节点，直到弹出u为止
//        do {
//            pop = sta[top--];      // 弹出栈顶节点
//            belong[pop] = ebccCnt; // 标记该节点属于当前边双连通分量
//        } while (pop != u);
//    }
//}
//
//// 将原图缩点，构建边双连通分量树
//// 将原图中的每个边双连通分量收缩为一个节点
//// 原来的割边（桥）成为连接这些新节点的边
//void condense() {
//    cntg = 0;  // 重置边计数器
//    
//    // 清空新图的邻接表
//    for (int i = 1; i <= ebccCnt; i++) {
//        head[i] = 0;
//    }
//    
//    // 遍历原图的所有边
//    for (int i = 1; i <= m; i++) {
//        // 获取边的两端点所属的边双连通分量编号
//        int ebcc1 = belong[a[i]];
//        int ebcc2 = belong[b[i]];
//        
//        // 如果两端点属于不同的边双连通分量，添加边
//        if (ebcc1 != ebcc2) {
//            addEdge(ebcc1, ebcc2);  // 添加两条有向边表示无向边
//            addEdge(ebcc2, ebcc1);
//        }
//    }
//}
//
//// 在边双连通分量树上进行树形DP，计算树的直径
//// 
//// 使用一次DFS可以同时计算直径：
//// - 维护dist[u]表示从当前节点到其子树中某个最远节点的距离
//// - 遍历所有子树，计算两个子树的dist之和，更新直径
//void dpOnTree(int u, int fa) {
//    // 遍历当前节点的所有邻接边
//    for (int e = head[u]; e > 0; e = nxt[e]) {
//        int v = to[e];  // 获取边的终点节点
//        
//        // 避免走回头路
//        if (v != fa) {
//            dpOnTree(v, u);  // 递归处理子树
//            
//            // 更新树的直径
//            // 取dist[u] + dist[v] + 1的最大值
//            // dist[u]是当前节点到其子树中最远节点的距离
//            // dist[v]是子节点v到其子树中最远节点的距离
//            // +1表示加上u到v这条边（一条割边）
//            diameter = max(diameter, dist[u] + dist[v] + 1);
//            
//            // 更新dist[u]：取经过子节点v的最长距离
//            // dist[v] + 1表示从u出发，经过v能到达的最远距离
//            dist[u] = max(dist[u], dist[v] + 1);
//        }
//    }
//}
//
//// 主函数
//int main() {
//    ios::sync_with_stdio(false);  // 关闭同步，提高IO效率
//    cin.tie(nullptr);             // 解除cin和cout的绑定
//    
//    cntg = 1;  // 初始化边计数器为1
//    
//    cin >> n >> m;  // 读取节点数和边数
//    
//    // 读取所有边的信息
//    for (int i = 1; i <= m; i++) {
//        cin >> a[i] >> b[i];  // 读取边的两个端点
//        addEdge(a[i], b[i]);  // 添加无向边
//        addEdge(b[i], a[i]);
//    }
//    
//    // 运行Tarjan算法求边双连通分量
//    tarjan(1, 0);
//    
//    // 构建边双连通分量树
//    condense();
//    
//    // 在边双连通分量树上进行DP，计算树的直径
//    dpOnTree(1, 0);
//    
//    // 输出树的直径（最多能经过的割边数量）
//    cout << diameter << "\n";
//    
//    return 0;  // 程序结束
//}
