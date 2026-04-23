package class194;

// 道路相遇问题 C++ 版
// 问题描述：给定一张无向图，包含 n 个节点和 m 条边，保证图是连通的
// 查询要求：对于 q 条查询，每条查询给定两个节点 x 和 y
//           求从 x 到 y 的所有路径中，一定会经过的节点个数（包括 x 和 y 本身）
// 数据范围：1 <= n <= 5 * 10^5，1 <= m <= 10^6，1 <= q <= 5 * 10^5
// 测试链接：https://www.luogu.com.cn/problem/P4320
// 实现说明：C++ 版本与 Java 版本逻辑完全一致
// 提交说明：直接提交以下代码即可通过所有测试用例

//#include <bits/stdc++.h>
//
//using namespace std;
//
// 最大节点数，根据题目数据范围设置为 500001
//const int MAXN = 500001;
// 最大边数，根据题目数据范围设置为 1000001
//const int MAXM = 1000001;
// 倍增表的最大幂次，2^20 足够覆盖 1e6 量级的数据
//const int MAXP = 20;
// n：节点数，m：边数，q：查询数，cntn：圆方树的总节点数（初始为原图节点数）
//int n, m, q, cntn;
//
// 原图的链式前向星存储结构
// head1[u]：节点 u 的第一条边的索引
// next1[e]：边 e 的下一条边的索引
// to1[e]：边 e 指向的节点
// cnt1：边的计数器，初始为 0
//int head1[MAXN];
//int next1[MAXM << 1];  // 无向图，边数翻倍
//int to1[MAXM << 1];
//int cnt1;
//
// 圆方树的链式前向星存储结构
// head2[u]：圆方树中节点 u 的第一条边的索引
// next2[e]：圆方树中边 e 的下一条边的索引
// to2[e]：圆方树中边 e 指向的节点
// cnt2：圆方树边的计数器，初始为 0
//int head2[MAXN << 1];  // 圆方树节点数最多为 2n
//int next2[MAXM << 2];  // 圆方树边数最多为 4m
//int to2[MAXM << 2];
//int cnt2;
//
// Tarjan 算法相关变量
// dfn[u]：节点 u 的深度优先搜索时间戳
// low[u]：节点 u 能够回溯到的最早的时间戳
// cntd：时间戳计数器，初始为 0
//int dfn[MAXN];
//int low[MAXN];
//int cntd;
//
// Tarjan 算法中使用的栈，存储当前连通分量的节点
// top：栈顶指针，初始为 0
//int sta[MAXN];
//int top;
//
// 圆方树的深度表和倍增表
// dep[u]：圆方树中节点 u 的深度
// stjump[u][p]：圆方树中节点 u 向上跳 2^p 步到达的节点
//int dep[MAXN << 1];
//int stjump[MAXN << 1][MAXP];
//
// 向原图添加一条无向边
// u：边的一个端点
// v：边的另一个端点
//void addEdge1(int u, int v) {
//    next1[++cnt1] = head1[u];  // 新边的 next 指向当前节点的第一条边
//    to1[cnt1] = v;              // 新边指向节点 v
//    head1[u] = cnt1;             // 更新当前节点的第一条边为新边
//}
//
// 向圆方树添加一条无向边
// u：边的一个端点
// v：边的另一个端点
//void addEdge2(int u, int v) {
//    next2[++cnt2] = head2[u];  // 新边的 next 指向当前节点的第一条边
//    to2[cnt2] = v;              // 新边指向节点 v
//    head2[u] = cnt2;             // 更新当前节点的第一条边为新边
//}
//
// Tarjan 算法，用于建立圆方树
// u：当前处理的节点
//void tarjan(int u) {
//    dfn[u] = low[u] = ++cntd;  // 初始化时间戳和 low 值
//    sta[++top] = u;             // 将当前节点压入栈
//    // 遍历当前节点的所有邻边
//    for (int e = head1[u]; e > 0; e = next1[e]) {
//        int v = to1[e];  // 获取邻边指向的节点
//        if (dfn[v] == 0) {  // 如果邻节点未被访问过
//            tarjan(v);  // 递归处理邻节点
//            // 更新当前节点的 low 值
//            low[u] = min(low[u], low[v]);
//            // 如果邻节点的 low 值大于等于当前节点的 dfn 值
//            // 说明当前节点是一个割点，需要建立圆方树的方点
//            if (low[v] >= dfn[u]) {
//                cntn++;  // 圆方树节点数加 1（新增一个方点）
//                // 建立方点与当前节点的双向边
//                addEdge2(cntn, u);
//                addEdge2(u, cntn);
//                int pop;
//                // 将栈中从 v 到当前节点的所有节点弹出
//                // 并建立这些节点与新方点的双向边
//                do {
//                    pop = sta[top--];  // 弹出栈顶节点
//                    addEdge2(cntn, pop);  // 建立方点与弹出节点的边
//                    addEdge2(pop, cntn);  // 建立弹出节点与方点的边
//                } while (pop != v);  // 直到弹出节点为 v
//            }
//        } else {  // 如果邻节点已被访问过
//            // 更新当前节点的 low 值
//            low[u] = min(low[u], dfn[v]);
//        }
//    }
//}
//
// 深度优先搜索，用于建立圆方树的深度表和倍增表
// u：当前处理的节点
// fa：当前节点的父节点
//void dfs(int u, int fa) {
//    dep[u] = dep[fa] + 1;  // 计算当前节点的深度
//    stjump[u][0] = fa;      // 初始化倍增表的第 0 层（直接父节点）
//    // 预处理倍增表的其他层
//    for (int p = 1; p < MAXP; p++) {
//        // 第 p 层等于第 p-1 层的第 p-1 层
//        stjump[u][p] = stjump[stjump[u][p - 1]][p - 1];
//    }
//    // 遍历当前节点的所有邻边
//    for (int e = head2[u]; e > 0; e = next2[e]) {
//        int v = to2[e];  // 获取邻边指向的节点
//        if (v != fa) {  // 如果邻节点不是父节点
//            dfs(v, u);  // 递归处理邻节点
//        }
//    }
//}
//
// 在圆方树上求两个节点的最低公共祖先（LCA）
// x：第一个节点
// y：第二个节点
// 返回值：x 和 y 的最低公共祖先
//int getLca(int x, int y) {
//    // 确保 x 的深度大于等于 y 的深度
//    if (dep[x] < dep[y]) {
//        swap(x, y);
//    }
//    // 将 x 向上跳，使得 x 和 y 的深度相同
//    for (int p = MAXP - 1; p >= 0; p--) {
//        if (dep[stjump[x][p]] >= dep[y]) {
//            x = stjump[x][p];
//        }
//    }
//    // 如果 x 和 y 已经相同，直接返回
//    if (x == y) {
//        return x;
//    }
//    // 同时将 x 和 y 向上跳，直到它们的父节点相同
//    for (int p = MAXP - 1; p >= 0; p--) {
//        if (stjump[x][p] != stjump[y][p]) {
//            x = stjump[x][p];
//            y = stjump[y][p];
//        }
//    }
//    // 返回父节点，即为最低公共祖先
//    return stjump[x][0];
//}
//
// 计算圆方树上两个节点之间的路径边数
// x：第一个节点
// y：第二个节点
// 返回值：x 到 y 路径上的边数
//int getDist(int x, int y) {
//    // 路径边数 = x 的深度 + y 的深度 - 2 * LCA 的深度
//    return dep[x] + dep[y] - 2 * dep[getLca(x, y)];
//}
//
// 主函数，程序入口
//int main() {
//    ios::sync_with_stdio(false);  // 关闭同步，加速输入输出
//    cin.tie(nullptr);             // 解绑 cin 和 cout
//    cin >> n >> m;                // 读取节点数和边数
//    cntn = n;                     // 圆方树初始节点数为原图节点数
//    // 读取 m 条边并添加到原图
//    for (int i = 1, u, v; i <= m; i++) {
//        cin >> u >> v;
//        addEdge1(u, v);  // 添加 u 到 v 的边
//        addEdge1(v, u);  // 添加 v 到 u 的边（无向图）
//    }
//    tarjan(1);  // 使用 Tarjan 算法建立圆方树
//    dfs(1, 0);  // 使用深度优先搜索建立深度表和倍增表
//    cin >> q;   // 读取查询数
//    // 处理 q 条查询
//    for (int i = 1, x, y; i <= q; i++) {
//        cin >> x >> y;
//        // 圆方树路径边数除以 2 加 1 即为必经节点数
//        cout << (getDist(x, y) / 2 + 1) << "\n";
//    }
//    return 0;  // 程序结束
//}