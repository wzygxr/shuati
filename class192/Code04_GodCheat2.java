package class192;

// 神会作弊，C++版
// 给定一张无向图，一共n个点、m条边，保证所有点连通，两点间出现重边只连一次
// 边双连通分量缩点后，如果点x和点y属于同一个边双连通分量，认为两点距离是1
// 如果不属于同一个边双连通分量，距离为缩点后的树上，两点简单路径上的节点个数
// 一共有q条查询，格式 x y : 计算点x和点y的距离，打印距离的二进制形式
// 1 <= n <= 10000
// 1 <= m <= 50000
// 测试链接 : https://www.luogu.com.cn/problem/P2783
// 如下实现是C++的版本，C++版本和java版本逻辑完全一样
// 提交如下代码，可以通过所有测试用例

// 注意：以下为C++代码实现，已被注释掉，仅供参考
// Java版本请参考 Code04_GodCheat1.java

// C++代码逻辑说明：
// 1. 读取边并去重（相同的边只保留一条）
// 2. 使用Tarjan算法求边双连通分量（E-DCC）
// 3. 将边双连通分量缩点，形成一棵树
// 4. 在缩点后的树上进行DFS，预处理深度和ST表
// 5. 使用LCA（最近公共祖先）求两点距离
// 6. 将距离+1后转换为二进制输出

//#include <bits/stdc++.h>  // 引入C++标准库头文件
//
//using namespace std;      // 使用标准命名空间
//
//// ------------------- 边结构体定义 -------------------
//struct Edge {
//    int u, v;  // 边的两个端点
//};
//
//// 边比较函数（按端点字典序排序）
//bool EdgeCmp(Edge e1, Edge e2) {
//    if (e1.u != e2.u) {
//        return e1.u < e2.u;  // 先按第一个端点排序
//    }
//    return e1.v < e2.v;      // 再按第二个端点排序
//}
//
//// ------------------- 静态常量定义 -------------------
//const int MAXN = 10001;  // 最大节点数量
//const int MAXM = 50001;  // 最大边数量
//const int MAXP = 15;     // 最大二进制位数
//
//// ------------------- 全局变量定义 -------------------
//int n, m, q;             // 节点数、边数、查询数
//Edge edgeArr[MAXM];     // 边的数组
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
//int cntd;              // 时间戳计数器
//
//// 栈相关变量
//int sta[MAXN];         // Tarjan算法使用的栈
//int top;               // 栈顶指针
//
//// 边双连通分量相关变量
//int belong[MAXN];      // 节点所属的边双连通分量编号
//int ebccCnt;          // 边双连通分量的数量
//
//// LCA相关变量
//int dep[MAXN];         // 节点深度数组
//int stjump[MAXN][MAXP];  // ST表，用于LCA查询
//
//// 添加边的函数
//void addEdge(int u, int v) {
//    nxt[++cntg] = head[u];  // 将当前边插入到节点u的邻接表头部
//    to[cntg] = v;            // 设置当前边的终点为v
//    head[u] = cntg;         // 更新节点u的头指针
//}
//
//// 对边进行去重排序
//void buildGraph() {
//    // 使用sort对边进行排序
//    sort(edgeArr + 1, edgeArr + m + 1, EdgeCmp);
//    
//    // 去重：k表示保留的边的数量
//    int k = 1;
//    for (int i = 2; i <= m; i++) {
//        // 如果当前边与前一条边不同，则保留
//        if (edgeArr[k].u != edgeArr[i].u || edgeArr[k].v != edgeArr[i].v) {
//            edgeArr[++k] = edgeArr[i];  // 复制当前边
//        }
//    }
//    
//    // 将去重后的边添加到邻接表中
//    for (int i = 1; i <= k; i++) {
//        addEdge(edgeArr[i].u, edgeArr[i].v);
//        addEdge(edgeArr[i].v, edgeArr[i].u);
//    }
//    
//    // 更新边的数量为去重后的数量
//    m = k;
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
//    // 遍历去重后的所有边
//    for (int i = 1; i <= m; i++) {
//        // 获取边的两端点所属的边双连通分量编号
//        int ebcc1 = belong[edgeArr[i].u];
//        int ebcc2 = belong[edgeArr[i].v];
//        
//        // 如果两端点属于不同的边双连通分量，添加边
//        if (ebcc1 != ebcc2) {
//            addEdge(ebcc1, ebcc2);  // 添加两条有向边表示无向边
//            addEdge(ebcc2, ebcc1);
//        }
//    }
//}
//
//// 在边双连通分量树上进行DFS，预处理深度和ST表
//void dfs(int u, int fa) {
//    // 设置当前节点的深度为父节点深度+1
//    dep[u] = dep[fa] + 1;
//    
//    // 设置第2^0 = 1个祖先为父节点
//    stjump[u][0] = fa;
//    
//    // 预处理ST表
//    for (int p = 1; p < MAXP; p++) {
//        stjump[u][p] = stjump[stjump[u][p - 1]][p - 1];
//    }
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
//// 求两个节点的最近公共祖先（LCA）
//int getLca(int a, int b) {
//    // 确保a是深度较大的节点
//    if (dep[a] < dep[b]) {
//        swap(a, b);  // 交换a和b
//    }
//    
//    // 第一步：让节点a跳到与节点b同一深度
//    for (int p = MAXP - 1; p >= 0; p--) {
//        if (dep[stjump[a][p]] >= dep[b]) {
//            a = stjump[a][p];  // 跳到该祖先
//        }
//    }
//    
//    // 如果此时a和b相同，说明b是a的祖先
//    if (a == b) {
//        return a;
//    }
//    
//    // 第二步：同时从高位到低位跳，找最近公共祖先
//    for (int p = MAXP - 1; p >= 0; p--) {
//        if (stjump[a][p] != stjump[b][p]) {
//            a = stjump[a][p];
//            b = stjump[b][p];
//        }
//    }
//    
//    // 此时a和b是相邻的节点，它们的父节点就是最近公共祖先
//    return stjump[a][0];
//}
//
//// 计算两个节点之间的距离
//int getDist(int x, int y) {
//    return dep[x] + dep[y] - 2 * dep[getLca(x, y)];
//}
//
//// 整数转二进制字符串函数
//string toBinaryString(int x) {
//    if (x == 0) {
//        return "0";  // 特殊情况：0的二进制是"0"
//    }
//    
//    string s;  // 用于存储二进制字符串
//    
//    // 从低位到高位依次取出每一位
//    while (x > 0) {
//        // 如果当前位是1，添加'1'；否则添加'0'
//        s.push_back((x & 1) ? '1' : '0');
//        x >>= 1;  // 右移一位
//    }
//    
//    // 反转字符串，得到正确的二进制表示
//    reverse(s.begin(), s.end());
//    
//    return s;
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
//    // 读取边的信息
//    for (int i = 1; i <= m; i++) {
//        int u, v;
//        cin >> u >> v;
//        // 将边的端点排序，小的在前，大的在后
//        edgeArr[i].u = min(u, v);
//        edgeArr[i].v = max(u, v);
//    }
//    
//    // 对边进行去重，并构建邻接表
//    buildGraph();
//    
//    // 运行Tarjan算法求边双连通分量
//    tarjan(1, 0);
//    
//    // 构建边双连通分量树
//    condense();
//    
//    // 在边双连通分量树上进行DFS，预处理LCA
//    dfs(1, 0);
//    
//    // 读取查询数量
//    cin >> q;
//    
//    // 处理每条查询
//    for (int i = 1; i <= q; i++) {
//        int x, y;
//        cin >> x >> y;
//        
//        // 将节点转换为它们所属的边双连通分量编号
//        x = belong[x];
//        y = belong[y];
//        
//        // 计算距离，然后+1，转换为二进制字符串输出
//        cout << toBinaryString(getDist(x, y) + 1) << "\n";
//    }
//    
//    return 0;  // 程序结束
//}
