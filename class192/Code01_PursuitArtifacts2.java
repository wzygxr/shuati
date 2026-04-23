package class192;

// 追寻文物，C++版
// 给定一张无向图，一共n个点、m条边，保证所有点连通
// 每条边除了端点之外，还有一个属性，值为1表示该边上有商品，值为0表示该边上无商品
// 给定起点s和终点t，路途怎么走随意，但是沿途每条边只能经过一次
// 从s到t的路途中能遇到商品打印"YES"，否则打印"NO"
// 1 <= n、m <= 3 * 10^5
// 测试链接 : https://www.luogu.com.cn/problem/CF652E
// 测试链接 : https://codeforces.com/problemset/problem/652/E
// 如下实现是C++的版本，C++版本和java版本逻辑完全一样
// 提交如下代码，可以通过所有测试用例

// 注意：以下为C++代码实现，已被注释掉，仅供参考
// Java版本请参考 Code01_PursuitArtifacts1.java

// C++代码逻辑说明：
// 1. 使用Tarjan算法求边双连通分量（E-DCC）
// 2. 将边双连通分量缩点，形成一棵树
// 3. 在缩点后的树上进行DFS，检查从s所在的块到t所在的块路径上是否有商品边
// 4. 如果路径上有商品输出YES，否则输出NO

//#include <bits/stdc++.h>  // 引入C++标准库头文件
//
//using namespace std;      // 使用标准命名空间
//
//// ------------------- 静态常量定义 -------------------
//const int MAXN = 300001;  // 最大节点数量，加1用于数组下标从1开始
//const int MAXM = 300001;  // 最大边数量，加1用于数组下标从1开始
//
//// ------------------- 全局变量定义 -------------------
//int n, m, s, t;           // 节点数n，边数m，起点s，终点t
//int a[MAXM];              // 边的第一个端点数组
//int b[MAXM];              // 边的第二个端点数组
//int c[MAXM];              // 边的商品标记数组（1表示有商品，0表示无商品）
//
//// 邻接表数据结构
//int head[MAXN];           // 头指针数组，head[u]表示节点u的第一条边的编号
//int nxt[MAXM << 1];       // 下一条边指针数组
//int to[MAXM << 1];       // 边的终点数组
//int weight[MAXM << 1];   // 边的权重（商品标记）数组
//int cntg;                 // 当前边的编号计数器
//
//// Tarjan算法相关变量
//int dfn[MAXN];           // 深度优先搜索编号数组
//int low[MAXN];           // Low值数组
//int cntd;                // 时间戳计数器
//
//// 栈相关变量
//int sta[MAXN];           // Tarjan算法使用的栈
//int top;                 // 栈顶指针
//
//// 边双连通分量相关变量
//int belong[MAXN];        // 节点所属的边双连通分量编号
//int val[MAXN];           // 边双连通分量的价值（是否包含商品边）
//int ebccCnt;             // 边双连通分量的数量
//
//// 添加边的函数
//// 使用链式前向星（邻接表）存储图
//// 每条无向边会添加两条有向边，编号分别为cntg和cntg^1
//void addEdge(int u, int v, int w) {
//    nxt[++cntg] = head[u];  // 将当前边插入到节点u的邻接表头部
//    to[cntg] = v;            // 设置当前边的终点为v
//    weight[cntg] = w;        // 设置当前边的权重（商品标记）
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
//        // 利用异或特性：无向边的两条有向边编号互为e^1
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
//            // 如果节点v已经被访问过（回边），更新low值
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
//// 原来的桥（割边）成为连接这些新节点的边
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
//        int w = c[i];  // 获取边的商品标记
//        
//        // 如果两端点属于同一个边双连通分量
//        if (ebcc1 == ebcc2) {
//            // 如果该边上有商品，则该边双连通分量的val设为1
//            if (w == 1) {
//                val[ebcc1] = 1;
//            }
//        } else {
//            // 如果两端点属于不同的边双连通分量，添加边
//            addEdge(ebcc1, ebcc2, w);
//            addEdge(ebcc2, ebcc1, w);
//        }
//    }
//}
//
//// 在边双连通分量树上进行DFS检查
//// 检查从起点s到终点t的路径上是否有商品边
//bool check(int u, int fa, bool ok) {
//    ok |= val[u] > 0;  // 如果当前E-DCC内包含商品边，标记为true
//    
//    // 如果到达目标节点t，返回当前状态
//    if (u == t) {
//        return ok;
//    }
//    
//    // 遍历当前节点的所有邻接边
//    for (int e = head[u]; e > 0; e = nxt[e]) {
//        int v = to[e];   // 获取边的终点节点
//        int w = weight[e];  // 获取边的商品标记
//        
//        // 避免走回头路
//        if (v != fa) {
//            // 递归检查子节点，累加路径上是否有商品
//            if (check(v, u, ok || w > 0)) {
//                return true;
//            }
//        }
//    }
//    
//    return false;  // 未找到商品，返回false
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
//        cin >> a[i] >> b[i] >> c[i];  // 读取边的两个端点和商品标记
//        addEdge(a[i], b[i], 0);        // 添加无向边
//        addEdge(b[i], a[i], 0);
//    }
//    
//    // 运行Tarjan算法求边双连通分量
//    tarjan(1, 0);
//    
//    // 构建边双连通分量树
//    condense();
//    
//    // 读取起点s和终点t
//    cin >> s >> t;
//    
//    // 将起点和终点转换为它们所属的边双连通分量编号
//    s = belong[s];
//    t = belong[t];
//    
//    // 检查从s到t的路径上是否有商品
//    if (check(s, 0, false)) {
//        cout << "YES\n";  // 输出YES
//    } else {
//        cout << "NO\n";   // 输出NO
//    }
//    
//    return 0;  // 程序结束
//}
