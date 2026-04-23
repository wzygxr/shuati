package class192;

// 只能一个方向，C++版
// 给定一张无向图，一共n个点、m条边，图上可能有多个连通区
// 给定q条要求，格式 x y : 从点x出发，要求可以去往点y
// 你必须把每条无向边变成有向边，也就是每条边确定唯一的方向
// 如果改造后能满足所有要求，打印"Yes"，如果不存在方案，打印"No"
// 1 <= n、m <= 2 * 10^5
// 测试链接 : https://www.luogu.com.cn/problem/CF555E
// 测试链接 : https://codeforces.com/problemset/problem/555_E
// 如下实现是C++的版本，C++版本和java版本逻辑完全一样
// 提交如下代码，可以通过所有测试用例

// 注意：以下为C++代码实现，已被注释掉，仅供参考
// Java版本请参考 Code06_OneDirection1.java

// C++代码逻辑说明：
// 1. 使用Tarjan算法求边双连通分量（E-DCC）
// 2. 将边双连通分量缩点，形成一棵树（森林）
// 3. 对于每条要求(x, y)，使用差分标记路径方向
// 4. 检查每个节点是否同时需要向上和向下的方向（矛盾检查）

//#include <bits/stdc++.h>  // 引入C++标准库头文件
//
//using namespace std;      // 使用标准命名空间
//
//// ------------------- 静态常量定义 -------------------
//const int MAXN = 200001;  // 最大节点数量
//const int MAXM = 200001;  // 最大边数量
//const int MAXP = 20;      // 最大二进制位数
//
//// ------------------- 全局变量定义 -------------------
//int n, m, q;             // 节点数、边数、查询数
//int a[MAXM];            // 边的第一个端点数组
//int b[MAXM];            // 边的第二个端点数组
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
//int ebccCnt;          // 边双连通分量的数量计数器
//
//// LCA相关变量
//int block[MAXN];       // block[u]表示节点u属于哪个连通块（树）
//int dep[MAXN];         // dep[u]表示节点u在树中的深度
//int stjump[MAXN][MAXP];  // stjump[u][p]表示节点u的第2^p个祖先
//
//// 方向标记相关变量
//bool vis[MAXN];        // vis[u]表示节点u是否已经被检查过
//int upCnt[MAXN];       // upCnt[u]表示有多少条路径需要从u向上
//int downCnt[MAXN];     // downCnt[u]表示有多少条路径需要从u向下
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
//        } while (pop != u);
//    }
//}
//
//// 将原图缩点，构建边双连通分量森林
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
//// 在边双连通分量森林上进行DFS，预处理LCA
//void dfs(int u, int fa, int bid) {
//    block[u] = bid;            // 记录节点属于哪个连通块
//    dep[u] = dep[fa] + 1;     // 设置节点深度
//    stjump[u][0] = fa;        // 设置父节点
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
//            dfs(v, u, bid);  // 递归处理子树
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
//// 检查某个节点及其子树是否满足方向要求
//bool check(int u, int fa) {
//    vis[u] = true;  // 标记为已检查
//    
//    // 遍历当前节点的所有邻接边
//    for (int e = head[u]; e > 0; e = nxt[e]) {
//        int v = to[e];  // 获取边的终点节点
//        
//        // 避免走回头路
//        if (v != fa) {
//            // 递归检查子节点
//            if (!check(v, u)) {
//                return false;
//            }
//            
//            // 累加子节点的计数
//            upCnt[u] += upCnt[v];
//            downCnt[u] += downCnt[v];
//        }
//    }
//    
//    // 如果同时需要向上和向下的方向，则矛盾，返回false
//    // 至少需要其中一种方向为0
//    return upCnt[u] == 0 || downCnt[u] == 0;
//}
//
//// 主函数
//int main() {
//    ios::sync_with_stdio(false);  // 关闭同步，提高IO效率
//    cin.tie(nullptr);             // 解除cin和cout的绑定
//    
//    cntg = 1;  // 初始化边计数器为1
//    
//    cin >> n >> m >> q;  // 读取节点数、边数和查询数
//    
//    // 读取所有边
//    for (int i = 1; i <= m; i++) {
//        cin >> a[i] >> b[i];
//        addEdge(a[i], b[i]);
//        addEdge(b[i], a[i]);
//    }
//    
//    // 对每个连通分量运行Tarjan算法
//    for (int i = 1; i <= n; i++) {
//        if (dfn[i] == 0) {
//            tarjan(i, 0);
//        }
//    }
//    
//    // 构建边双连通分量森林
//    condense();
//    
//    // 对每个连通块进行DFS，预处理LCA
//    for (int i = 1, b = 0; i <= ebccCnt; i++) {
//        if (block[i] == 0) {
//            dfs(i, 0, ++b);
//        }
//    }
//    
//    bool ans = true;  // 默认答案为true（有解）
//    
//    // 处理每条要求
//    for (int i = 1; i <= q; i++) {
//        int x, y;
//        cin >> x >> y;
//        
//        // 将节点转换为它们所属的边双连通分量编号
//        x = belong[x];
//        y = belong[y];
//        
//        // 如果x和y不在同一个连通块，无解
//        if (block[x] != block[y]) {
//            ans = false;
//            break;
//        }
//        
//        // 求x和y的最近公共祖先
//        int xylca = getLca(x, y);
//        
//        // 路径(x, y)可以分成两段：
//        // 1. x到xylca：方向向上（从子节点到父节点）
//        // 2. xylca到y：方向向下（从父节点到子节点）
//        
//        // x到xylca的路径上，所有节点都需要向上方向
//        upCnt[x]++;
//        upCnt[xylca]--;
//        
//        // xylca到y的路径上，所有节点都需要向下方向
//        downCnt[y]++;
//        downCnt[xylca]--;
//    }
//    
//    // 检查是否满足方向要求
//    if (ans) {
//        for (int i = 1; i <= ebccCnt; i++) {
//            // 如果节点未被检查过且检查失败
//            if (!vis[i] && !check(i, 0)) {
//                ans = false;
//                break;
//            }
//        }
//    }
//    
//    // 输出结果
//    cout << (ans ? "Yes" : "No") << "\n";
//    
//    return 0;  // 程序结束
//}
