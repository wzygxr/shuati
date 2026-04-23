package class192;

// 旅行家，C++版
// 给定一张无向图，一共n个点、m条边，每个点给定点权，保证所有点连通
// 一条路径要求，点可以重复经过，边不能重复经过
// 一共有q条操作，格式 x y : 从点x到点y所有可能的路径都走一遍
// 一共q条操作，可能涉及非常多的路径，如果一条路径通过了某个点
// 该点的点权就算入收益，但是以后再有其他路径通过该点，不重复获得收益
// 打印总收益是多少
// 1 <= n <= 5 * 10^5
// 1 <= m <= 2 * 10^6
// 1 <= q <= 10^6
// 测试链接 : https://www.luogu.com.cn/problem/P7924
// 如下实现是C++的版本，C++版本和java版本逻辑完全一样
// 提交如下代码，可以通过所有测试用例

// 注意：以下为C++代码实现，已被注释掉，仅供参考
// Java版本请参考 Code05_Traveler1.java

// C++代码逻辑说明：
// 1. 使用Tarjan算法求边双连通分量（E-DCC）
// 2. 将边双连通分量缩点，形成一棵树
// 3. 在缩点后的树上统计每个E-DCC的点权之和
// 4. 对于每个操作(x, y)，使用树上差分标记路径上的E-DCC
// 5. 最后DFS累加useCnt，计算哪些E-DCC被至少一条路径经过
// 6. 将被经过的E-DCC的点权之和相加即为答案

//#include <bits/stdc++.h>  // 引入C++标准库头文件
//
//using namespace std;      // 使用标准命名空间
//
//// ------------------- 静态常量定义 -------------------
//const int MAXN = 500001;  // 最大节点数量
//const int MAXM = 2000001; // 最大边数量的两倍
//const int MAXP = 20;      // 最大二进制位数
//
//// ------------------- 全局变量定义 -------------------
//int n, m, q;             // 节点数、边数、查询数
//int arr[MAXN];           // arr[i]表示节点i的点权
//int a[MAXM];             // 边的第一个端点数组
//int b[MAXM];             // 边的第二个端点数组
//
//// 邻接表数据结构
//int head[MAXN];          // 头指针数组
//int nxt[MAXM << 1];   // 下一条边指针数组
//int to[MAXM << 1];    // 边的终点数组
//int cntg;              // 当前边的编号计数器
//
//// Tarjan算法相关变量
//int dfn[MAXN];         // 深度优先搜索编号数组
//int low[MAXN];         // Low值数组
//int cntd;             // 时间戳计数器
//
//// 栈相关变量
//int sta[MAXN];         // Tarjan算法使用的栈
//int top;              // 栈顶指针
//
//// 边双连通分量相关变量
//int belong[MAXN];      // 节点所属的边双连通分量编号
//int sum[MAXN];        // sum[e]表示第e个E-DCC内所有节点的点权之和
//int ebccCnt;          // 边双连通分量的数量计数器
//
//// RMQ/LCA相关变量
//int lg2[MAXN];        // lg2[i]表示log2(i)的向下取整
//int rmq[MAXN][MAXP];  // RMQ/ST表
//
//// useCnt[u]表示节点u被路径经过的次数（差分值）
//int useCnt[MAXN];
//
//// 添加边的函数
//void addEdge(int u, int v) {
//    nxt[++cntg] = head[u];  // 将当前边插入到节点u的邻接表头部
//    to[cntg] = v;            // 设置当前边的终点为v
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
//            sum[ebccCnt] += arr[pop];  // 累加该节点的点权到E-DCC的总和中
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
//        
//        // 如果两端点属于不同的边双连通分量，添加边
//        if (ebcc1 != ebcc2) {
//            addEdge(ebcc1, ebcc2);  // 添加两条有向边表示无向边
//            addEdge(ebcc2, ebcc1);
//        }
//    }
//}
//
//// RMQ比较函数：返回dfn值较小的节点
//int getUp(int x, int y) {
//    return dfn[x] < dfn[y] ? x : y;
//}
//
//// 在边双连通分量树上进行DFS，构建RMQ/ST表
//void dfs(int u, int fa) {
//    dfn[u] = ++cntd;          // 记录dfs序编号
//    rmq[dfn[u]][0] = fa;     // 记录该节点的父节点
//    
//    // 遍历当前节点的所有邻接边
//    for (int e = head[u]; e > 0; e = nxt[e]) {
//        int v = to[e];  // 获取边的终点节点
//        
//        // 避免走回头路
//        if (v != fa) {
//            dfs(v, u);  // 递归处理子树
//        }
//    }
//}
//
//// 构建RMQ/ST表
//void buildRmq() {
//    cntd = 0;
//    dfs(1, 0);  // 从根节点开始DFS
//    
//    // 预处理lg2数组
//    for (int i = 2; i <= ebccCnt; i++) {
//        lg2[i] = lg2[i >> 1] + 1;
//    }
//    
//    // 构建ST表
//    for (int pre = 0, cur = 1; cur <= lg2[ebccCnt]; pre++, cur++) {
//        for (int i = 1; i + (1 << cur) - 1 <= ebccCnt; i++) {
//            rmq[i][cur] = getUp(rmq[i][pre], rmq[i + (1 << pre)][pre]);
//        }
//    }
//}
//
//// 获取节点的父节点
//int getFather(int x) {
//    return rmq[dfn[x]][0];
//}
//
//// 使用RMQ求最近公共祖先（LCA）
//int getLCA(int x, int y) {
//    // 如果两个节点相同，直接返回
//    if (x == y) {
//        return x;
//    }
//    
//    // 转换为dfs序编号
//    x = dfn[x];
//    y = dfn[y];
//    
//    // 确保x <= y
//    if (x > y) {
//        swap(x, y);
//    }
//    
//    // x需要加1，因为RMQ查询的是开区间
//    x++;
//    
//    // 计算需要查询的区间长度
//    int k = lg2[y - x + 1];
//    
//    // 返回区间[x, y]的最小值对应的节点
//    return getUp(rmq[x][k], rmq[y - (1 << k) + 1][k]);
//}
//
//// 树上差分的后序遍历
//void dfsOnTree(int u, int fa) {
//    // 遍历当前节点的所有邻接边
//    for (int e = head[u]; e > 0; e = nxt[e]) {
//        int v = to[e];  // 获取边的终点节点
//        
//        // 避免走回头路
//        if (v != fa) {
//            dfsOnTree(v, u);  // 递归处理子树
//            
//            // 累加子节点的useCnt到父节点
//            useCnt[u] += useCnt[v];
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
//    // 读取每个节点的点权
//    for (int i = 1; i <= n; i++) {
//        cin >> arr[i];
//    }
//    
//    // 读取所有边
//    for (int i = 1; i <= m; i++) {
//        cin >> a[i] >> b[i];
//        addEdge(a[i], b[i]);
//        addEdge(b[i], a[i]);
//    }
//    
//    // 使用Tarjan求边双连通分量
//    tarjan(1, 0);
//    
//    // 构建边双连通分量树
//    condense();
//    
//    // 构建RMQ表
//    buildRmq();
//    
//    // 读取查询数量
//    cin >> q;
//    
//    // 处理每条查询，使用树上差分
//    for (int i = 1; i <= q; i++) {
//        int x, y;
//        cin >> x >> y;
//        
//        // 将节点转换为它们所属的边双连通分量编号
//        x = belong[x];
//        y = belong[y];
//        
//        // 求x和y的最近公共祖先
//        int xylca = getLCA(x, y);
//        // 求lca的父节点
//        int lcafa = getFather(xylca);
//        
//        // 树上差分：
//        // x++, y++, xylca--, lcafa--
//        useCnt[x]++;
//        useCnt[y]++;
//        useCnt[xylca]--;
//        useCnt[lcafa]--;
//    }
//    
//    // 后序遍历，累加useCnt
//    dfsOnTree(1, 0);
//    
//    // 统计答案：所有useCnt > 0的E-DCC的点权之和
//    int ans = 0;
//    for (int i = 1; i <= ebccCnt; i++) {
//        if (useCnt[i] > 0) {
//            ans += sum[i];
//        }
//    }
//    
//    cout << ans << "\n";  // 输出答案
//    
//    return 0;  // 程序结束
//}
