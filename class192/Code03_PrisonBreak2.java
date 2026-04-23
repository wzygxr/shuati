package class192;

// 越狱老虎桥，C++版
// 给定一张无向图，一共n个点、m条边，保证所有点连通
// 每条边给定边权，表示破坏这条边需要花费的钱数
// 敌人可能在任意两点之间新增一条边，新增的这条边无法被破坏
// 敌人新增一条边之后，你的目标是只破坏一条边，就让图变成两个连通区
// 你不知道敌人会选择哪两个端点来新增这条边，你需要尽可能的做好准备
// 假设遭遇最差情况，打印你至少准备多少钱才能完成目标，无法完成目标打印-1
// 1 <= n <= 5 * 10^5
// 1 <= m <= 10^6
// 1 <= 边权 <= 10^5
// 测试链接 : https://www.luogu.com.cn/problem/P5234
// 如下实现是C++的版本，C++版本和java版本逻辑完全一样
// 提交如下代码，可以通过所有测试用例

// 注意：以下为C++代码实现，已被注释掉，仅供参考
// Java版本请参考 Code03_PrisonBreak1.java

// C++代码逻辑说明：
// 1. 使用Tarjan算法求边双连通分量（E-DCC）
// 2. 将边双连通分量缩点，形成一棵树（割边树）
// 3. 使用二分搜索 + 树形DP验证某个阈值是否可行
// 4. 关键：检查diameter < edgeCnt来确定是否可行

//#include <bits/stdc++.h>  // 引入C++标准库头文件
//
//using namespace std;      // 使用标准命名空间
//
//// ------------------- 静态常量定义 -------------------
//const int MAXN = 500001;  // 最大节点数量
//const int MAXM = 1000001; // 最大边数量
//
//// ------------------- 全局变量定义 -------------------
//int n, m, maxv;          // 节点数、边数、最大权值
//int a[MAXM];             // 边的第一个端点数组
//int b[MAXM];             // 边的第二个端点数组
//int c[MAXM];             // 边的权值数组
//
//// 邻接表数据结构
//int head[MAXN];          // 头指针数组
//int nxt[MAXM << 1];    // 下一条边指针数组
//int to[MAXM << 1];     // 边的终点数组
//int weight[MAXM << 1]; // 边的权值数组
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
//// 树的直径和割边计数相关变量
//int dist[MAXN];         // dist[u]表示从根节点到节点u的距离
//int diameter;           // 树的直径
//int edgeCnt;            // 有效割边计数
//
//// 添加边的函数
//void addEdge(int u, int v, int w) {
//    nxt[++cntg] = head[u];  // 将当前边插入到节点u的邻接表头部
//    to[cntg] = v;            // 设置当前边的终点为v
//    weight[cntg] = w;        // 设置当前边的权值
//    head[u] = cntg;         // 更新节点u的头指针
//}
//
//// Tarjan算法求边双连通分量（E-DCC）
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
//        int w = c[i];  // 获取边的权值
//        
//        // 如果两端点属于不同的边双连通分量，添加边
//        if (ebcc1 != ebcc2) {
//            addEdge(ebcc1, ebcc2, w);  // 添加两条有向边表示无向边
//            addEdge(ebcc2, ebcc1, w);
//        }
//    }
//}
//
//// 在边双连通分量树上进行树形DP
//void dpOnTree(int u, int fa, int limit) {
//    // 遍历当前节点的所有邻接边
//    for (int e = head[u]; e > 0; e = nxt[e]) {
//        int v = to[e];  // 获取边的终点节点
//        
//        // 避免走回头路
//        if (v != fa) {
//            dpOnTree(v, u, limit);  // 递归处理子树
//            
//            // 如果这条边的权值 <= limit，则视为需要派兵看守的割边
//            int w = weight[e] <= limit ? 1 : 0;
//            
//            // 累加有效割边数量
//            edgeCnt += w;
//            
//            // 更新树的直径
//            diameter = max(diameter, dist[u] + dist[v] + w);
//            
//            // 更新dist[u]
//            dist[u] = max(dist[u], dist[v] + w);
//        }
//    }
//}
//
//// 检查给定的花费阈值limit是否可行
//bool check(int limit) {
//    // 初始化dist数组
//    for (int i = 1; i <= ebccCnt; i++) {
//        dist[i] = 0;
//    }
//    
//    // 初始化直径和有效割边计数
//    diameter = edgeCnt = 0;
//    
//    // 从根节点开始DP
//    dpOnTree(1, 0, limit);
//    
//    // 如果最长路径上的有效割边数小于总的有效割边数，则可行
//    return diameter < edgeCnt;
//}
//
//// 使用二分搜索找到最小的可行花费
//int compute() {
//    // 二分搜索的范围是[1, maxv]
//    int l = 1, r = maxv, mid, ans = -1;
//    
//    while (l <= r) {
//        // 取中间值
//        mid = (l + r) / 2;
//        
//        // 如果阈值mid可行
//        if (check(mid)) {
//            // 记录答案，尝试更小的阈值
//            ans = mid;
//            r = mid - 1;
//        } else {
//            // 如果阈值mid不可行，增加阈值
//            l = mid + 1;
//        }
//    }
//    
//    // 返回答案
//    return ans;
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
//    maxv = 0;  // 初始化最大权值
//    
//    // 读取所有边的信息
//    for (int i = 1; i <= m; i++) {
//        // 读取边的两个端点和权值
//        cin >> a[i] >> b[i] >> c[i];
//        
//        // 添加无向边到图中（暂时用0填充权值）
//        addEdge(a[i], b[i], 0);
//        addEdge(b[i], a[i], 0);
//        
//        // 更新最大权值
//        maxv = max(maxv, c[i]);
//    }
//    
//    // 运行Tarjan算法求边双连通分量
//    tarjan(1, 0);
//    
//    // 构建边双连通分量树
//    condense();
//    
//    // 使用二分搜索计算最小花费
//    cout << compute() << "\n";
//    
//    return 0;  // 程序结束
//}
