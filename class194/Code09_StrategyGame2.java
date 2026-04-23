package class194;

// 战略游戏问题 C++ 版
// 问题描述：给定一张无向图，包含 n 个点和 m 条边，所有点保证连通，图中可能有重边
// 支持 q 条查询，每条查询格式为 s K1 K2 ... Ks，含义如下：
// 当前查询涉及 s 个点，给出 s 个点的编号，你只能删除一个另外的点
// 删除该点后，只要这 s 个点中存在任意两点不连通，就算成功
// 对每条查询，打印你能选择的点的个数
// 数据范围：2 <= n <= 10^5，1 <= m <= 2 * 10^5，1 <= q <= 10^5
// 所有查询中 s 的总和 <= 2 * 10^5
// 测试链接：https://www.luogu.com.cn/problem/P4606
// 实现说明：C++ 版本与 Java 版本逻辑完全一致
// 提交说明：直接提交以下代码即可通过所有测试用例

#include <bits/stdc++.h>

using namespace std;

// 最大节点数，根据题目数据范围设置为 100001
const int MAXN = 100001;
// 最大边数，根据题目数据范围设置为 200001
const int MAXM = 200001;
// 倍增数组的最大幂次，用于 LCA 计算
const int MAXP = 20;
// t：测试用例数，n：节点数，m：边数，q：查询数，s：当前查询的节点数
// cntn：圆方树的总节点数（初始为原图节点数）
int t, n, m, q, s, cntn;

// 原图的链式前向星存储结构
// head1[u]：节点 u 的第一条边的索引
// next1[e]：边 e 的下一条边的索引
// to1[e]：边 e 指向的节点
// cnt1：边的计数器，初始为 0
int head1[MAXN];
int next1[MAXM << 1];  // 无向图，边数翻倍
int to1[MAXM << 1];
int cnt1;

// 圆方树的链式前向星存储结构
// head2[u]：圆方树中节点 u 的第一条边的索引
// next2[e]：圆方树中边 e 的下一条边的索引
// to2[e]：圆方树中边 e 指向的节点
// cnt2：圆方树边的计数器，初始为 0
int head2[MAXN << 1];  // 圆方树节点数最多为 2n
int next2[MAXM << 2];  // 圆方树边数最多为 4m
int to2[MAXM << 2];
int cnt2;

// Tarjan 算法相关变量
// dfn[u]：节点 u 的深度优先搜索时间戳
// low[u]：节点 u 能够回溯到的最早的时间戳
// cntd：时间戳计数器，初始为 0
int dfn[MAXN];
int low[MAXN];
int cntd;

// Tarjan 算法中使用的栈，存储当前连通分量的节点
// top：栈顶指针，初始为 0
int sta[MAXN];
int top;

// 圆方树相关变量
// dist[u]：圆方树中从根节点到节点 u 的圆点数量
// dep[u]：圆方树中节点 u 的深度
// stjump[u][p]：圆方树中节点 u 的 2^p 级祖先
// nid[u]：圆方树节点 u 的 dfn 序号
// cnti：圆方树 dfn 序号计数器，初始为 0
int dist[MAXN << 1];
int dep[MAXN << 1];
int stjump[MAXN << 1][MAXP];
int nid[MAXN << 1];
int cnti;

// 存储当前查询的节点数组
int arr[MAXN];

// 向原图添加一条无向边
// u：边的一个端点
// v：边的另一个端点
void addEdge1(int u, int v) {
    next1[++cnt1] = head1[u];  // 新边的 next 指向当前节点的第一条边
    to1[cnt1] = v;              // 新边指向节点 v
    head1[u] = cnt1;             // 更新当前节点的第一条边为新边
}

// 向圆方树添加一条无向边
// u：边的一个端点
// v：边的另一个端点
void addEdge2(int u, int v) {
    next2[++cnt2] = head2[u];  // 新边的 next 指向当前节点的第一条边
    to2[cnt2] = v;              // 新边指向节点 v
    head2[u] = cnt2;             // 更新当前节点的第一条边为新边
}

// Tarjan 算法，用于建立圆方树
// u：当前处理的节点
void tarjan(int u) {
    dfn[u] = low[u] = ++cntd;  // 初始化时间戳和 low 值
    sta[++top] = u;             // 将当前节点压入栈
    // 遍历当前节点的所有邻边
    for (int e = head1[u]; e > 0; e = next1[e]) {
        int v = to1[e];  // 获取邻边指向的节点
        if (dfn[v] == 0) {  // 如果邻节点未被访问过
            tarjan(v);  // 递归处理邻节点
            // 更新当前节点的 low 值
            low[u] = min(low[u], low[v]);
            // 如果邻节点的 low 值大于等于当前节点的 dfn 值
            // 说明当前节点是一个割点，需要建立圆方树的方点
            if (low[v] >= dfn[u]) {
                cntn++;  // 圆方树节点数加 1（新增一个方点）
                // 建立方点与当前节点的双向边
                addEdge2(cntn, u);
                addEdge2(u, cntn);
                int pop;
                // 将栈中从 v 到当前节点的所有节点弹出
                // 并建立这些节点与新方点的双向边
                do {
                    pop = sta[top--];  // 弹出栈顶节点
                    addEdge2(cntn, pop);  // 建立方点与弹出节点的边
                    addEdge2(pop, cntn);  // 建立弹出节点与方点的边
                } while (pop != v);  // 直到弹出节点为 v
            }
        } else {  // 如果邻节点已被访问过
            // 更新当前节点的 low 值
            low[u] = min(low[u], dfn[v]);
        }
    }
}

// 深度优先搜索，用于初始化圆方树的 LCA 相关信息
// u：当前处理的节点
// fa：当前节点的父节点
void dfs(int u, int fa) {
    nid[u] = ++cnti;  // 为当前节点分配 dfn 序号
    // 计算从根节点到当前节点的圆点数量
    dist[u] = dist[fa] + (u <= n ? 1 : 0);
    dep[u] = dep[fa] + 1;  // 计算当前节点的深度
    stjump[u][0] = fa;  // 初始化当前节点的 2^0 级祖先
    // 初始化倍增数组
    for (int p = 1; p < MAXP; p++) {
        stjump[u][p] = stjump[stjump[u][p - 1]][p - 1];
    }
    // 遍历当前节点的所有邻边
    for (int e = head2[u]; e > 0; e = next2[e]) {
        int v = to2[e];  // 获取邻边指向的节点
        if (v != fa) {  // 如果邻节点不是父节点
            dfs(v, u);  // 递归处理邻节点
        }
    }
}

// 计算圆方树中两个节点的最近公共祖先（LCA）
// a：节点 a
// b：节点 b
// 返回值：节点 a 和节点 b 的最近公共祖先
int getLca(int a, int b) {
    // 如果节点 a 的深度小于节点 b 的深度，交换 a 和 b
    if (dep[a] < dep[b]) {
        swap(a, b);
    }
    // 将节点 a 提升到与节点 b 相同的深度
    for (int p = MAXP - 1; p >= 0; p--) {
        if (dep[stjump[a][p]] >= dep[b]) {
            a = stjump[a][p];
        }
    }
    if (a == b) {  // 如果节点 a 和节点 b 相同，直接返回
        return a;
    }
    // 同时提升节点 a 和节点 b，直到它们的父节点相同
    for (int p = MAXP - 1; p >= 0; p--) {
        if (stjump[a][p] != stjump[b][p]) {
            a = stjump[a][p];
            b = stjump[b][p];
        }
    }
    return stjump[a][0];  // 返回节点 a 的父节点（即 LCA）
}

// 计算圆方树中两个节点之间的圆点数量（路径上的节点数）
// x：节点 x
// y：节点 y
// 返回值：节点 x 和节点 y 之间的圆点数量
int getDist(int x, int y) {
    int lca = getLca(x, y);  // 获取节点 x 和节点 y 的最近公共祖先
    // 计算公式：dist[x] + dist[y] - 2 * dist[lca]
    return dist[x] + dist[y] - 2 * dist[lca];
}

// 计算当前查询的答案
// 返回值：可以选择的点的个数
int compute() {
    // 按照圆方树的 dfn 序号对查询节点进行排序
    sort(arr + 1, arr + s + 1, [](int x, int y) {return nid[x] < nid[y];});
    int sumDist = 0;
    // 计算相邻节点之间的圆点数量之和
    for (int i = 1; i < s; i++) {
        sumDist += getDist(arr[i], arr[i + 1]);
    }
    // 计算首尾节点之间的圆点数量
    sumDist += getDist(arr[1], arr[s]);
    // 计算额外的圆点数量（如果首尾节点的 LCA 是圆点）
    int extra = getLca(arr[1], arr[s]) <= n ? 1 : 0;
    // 计算公式：sumDist / 2 + extra - s
    return sumDist / 2 + extra - s;
}

// 初始化函数，用于重置全局变量
void prepare() {
    cnt1 = cnt2 = cntd = top = cnti = 0;
    // 重置原图和圆方树的链式前向星
    for (int i = 1; i <= n; i++) {
        head1[i] = head2[i] = dfn[i] = 0;
        head2[i + n] = 0;
    }
}

// 主函数，程序入口
int main() {
    // 关闭同步，加速输入输出
    ios::sync_with_stdio(false);
    cin.tie(nullptr);
    cin >> t;  // 读取测试用例数
    for (int c = 1; c <= t; c++) {
        cin >> n >> m;  // 读取节点数和边数
        cntn = n;  // 圆方树初始节点数为原图节点数
        prepare();  // 初始化全局变量
        // 读取 m 条边并添加到原图
        for (int i = 1, u, v; i <= m; i++) {
            cin >> u >> v;
            addEdge1(u, v);  // 添加 u 到 v 的边
            addEdge1(v, u);  // 添加 v 到 u 的边（无向图）
        }
        tarjan(1);  // 使用 Tarjan 算法建立圆方树
        dfs(1, 0);  // 初始化圆方树的 LCA 相关信息
        cin >> q;  // 读取查询数
        // 处理 q 条查询
        for (int i = 1; i <= q; i++) {
            cin >> s;  // 读取当前查询的节点数
            // 读取当前查询的 s 个节点
            for (int j = 1; j <= s; j++) {
                cin >> arr[j];
            }
            cout << compute() << "\n";  // 计算并输出答案
        }
    }
    return 0;
}