package class192;

// 建造军营，C++版
// 一共n个城市、m条道路，道路是无向边，保证所有城市连通
// 你可以选择任何城市，在城市内建造军营，至少要选一座城市
// 你可以选择任何道路，在道路上派兵看守，也可以一条都不选
// 选择的城市集合 + 选择的道路集合，被认为是一种方案
// 敌人会袭击任意一条道路，如果有兵看守就不会被切断，否则会被切断
// 敌人袭击之后，如果造成任意两座军营无法连通，那么算你失败
// 确保不会失败的情况下，计算方案数，答案对 1000000007 取余
// 1 <= n <= 5 * 10^5
// 1 <= m <= 10^6
// 测试链接 : https://www.luogu.com.cn/problem/P8867
// 如下实现是C++的版本，C++版本和java版本逻辑完全一样
// 提交如下代码，可以通过所有测试用例

// 注意：以下为C++代码实现，已被注释掉，仅供参考
// Java版本请参考 Code07_MilitaryCamp1.java

// C++代码逻辑说明：
// 1. 使用Tarjan算法求边双连通分量（E-DCC）
// 2. 将边双连通分量缩点，形成一棵树（割边树）
// 3. 在树上进行DP计算方案数
// 4. 考虑每个E-DCC内部的选择和割边的选择
// 5. 最后合并所有情况得到答案

//#include <bits/stdc++.h>  // 引入C++标准库头文件
//
//using namespace std;      // 使用标准命名空间
//
//// 使用long long类型别名
//using ll = long long;
//
//// ------------------- 静态常量定义 -------------------
//const int MAXN = 500001;  // 最大节点数量
//const int MAXM = 1000001; // 最大边数量
//const int MOD = 1000000007;  // 取模数值
//
//// ------------------- 全局变量定义 -------------------
//int n, m;                // 节点数、边数
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
//int ebccSiz[MAXN];    // ebccSiz[e]表示第e个E-DCC包含的节点数量
//int ebccCnt;          // 边双连通分量的数量计数器
//
//// DP相关变量
//ll power2[MAXM];      // power2[i] = 2^i % MOD
//ll dp[MAXN];          // dp[u] : 子树u上的方案数
//int bridge[MAXN];     // bridge[u] : 子树u上割边的数量
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
//        ebccSiz[ebccCnt] = 0;  // 初始化E-DCC大小
//        int pop;
//        // 弹出栈中节点，直到弹出u为止
//        do {
//            pop = sta[top--];      // 弹出栈顶节点
//            belong[pop] = ebccCnt; // 标记该节点属于当前边双连通分量
//            ebccSiz[ebccCnt]++;   // 该E-DCC大小+1
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
//// 树形DP，计算每个子树的方案数
//void dpOnTree(int u, int fa) {
//    // 情况1：不考虑下方的节点，在u自己的E-DCC里选点造军营
//    // 2^ebccSiz[u] - 1 表示至少选一个城市的方案数
//    dp[u] = power2[ebccSiz[u]] - 1;
//    bridge[u] = 0;  // 初始化割边数量
//    
//    // 遍历当前节点的所有邻接边
//    for (int e = head[u]; e > 0; e = nxt[e]) {
//        int v = to[e];  // 获取边的终点节点
//        
//        // 避免走回头路
//        if (v != fa) {
//            dpOnTree(v, u);  // 递归处理子树
//            
//            // 合并子树的方案数
//            // dp[u] * power2[bridge[v]] * 2 : 
//            //   之前部分的方案数 * 子树v割边的自由发挥 * u到v割边的自由发挥
//            // power2[bridge[u]] * dp[v] : 
//            //   之前部分割边的自由发挥 * 子树v的方案数
//            // dp[u] * dp[v] : 
//            //   之前部分的方案数 * 子树v的方案数
//            dp[u] = (dp[u] * power2[bridge[v]] * 2 % MOD
//                    + power2[bridge[u]] * dp[v] % MOD
//                    + dp[u] * dp[v] % MOD) % MOD;
//            
//            // 累加子树v中的割边数量，加上u到v之间的这条割边
//            bridge[u] += bridge[v] + 1;
//        }
//    }
//}
//
//// 计算最终答案
//ll compute() {
//    // 预计算2的幂次
//    power2[0] = 1;
//    for (int i = 1; i <= m; i++) {
//        power2[i] = power2[i - 1] * 2 % MOD;
//    }
//    
//    // 使用DP计算方案数
//    dpOnTree(1, 0);
//    
//    // 总割边数量
//    int total = bridge[1];
//    
//    // 根节点的方案数
//    ll ans = dp[1];
//    
//    // 考虑其他连通块的情况
//    // 如果选择某个子树的根节点作为唯一有军营的E-DCC
//    for (int i = 2; i <= ebccCnt; i++) {
//        ans = (ans + dp[i] * power2[total - bridge[i] - 1] % MOD) % MOD;
//    }
//    
//    // 考虑非割边的选择（不在树中的边，即E-DCC内部的边）
//    // 共有 m - total 条边
//    ans = ans * power2[m - total] % MOD;
//    
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
//    // 计算答案
//    ll ans = compute();
//    
//    cout << ans << "\n";  // 输出答案
//    
//    return 0;  // 程序结束
//}
