package class194;

// 警察问题 C++ 版
// 问题描述：给定一张无向图，包含 n 个节点和 m 条边，保证图是连通的且无重边
// 查询类型：共有 q 条查询，分为两种类型
//           类型 1 a b c d：已知 c 和 d 之间有边，如果删除这条边，判断 a 和 b 是否连通
//           类型 2 a b c：如果删除点 c，判断 a 和 b 是否连通
// 数据范围：1 <= n <= 10^5，1 <= m <= 5 * 10^5，1 <= q <= 3 * 10^5
// 测试链接：https://www.luogu.com.cn/problem/P4334
// 实现说明：C++ 版本与 Java 版本逻辑完全一致
// 提交说明：直接提交以下代码即可通过所有测试用例

#include <bits/stdc++.h>

using namespace std;

// 最大节点数，根据题目数据范围设置为 100001
const int MAXN = 100001;
// 最大边数，根据题目数据范围设置为 500001
const int MAXM = 500001;

// n：节点数，m：边数，q：查询数，cntn：圆方树的总节点数（初始为原图节点数）
int n, m, q, cntn;

// 原图的链式前向星存储结构
// head1[u]：节点 u 的第一条边的索引
// next1[e]：边 e 的下一条边的索引
// to1[e]：边 e 指向的节点
// cnt1：边的计数器，初始为 1（避免异或 0 出错）
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
// cnts：栈顶指针，初始为 0
int sta[MAXN];
int cnts;

// 存储桥边对应的方点
// 键：pair<int, int>，值：对应的方点编号
map<pair<int, int>, int> cutMap;

// 树链剖分相关变量
// fa[u]：节点 u 的父节点
// dep[u]：节点 u 的深度
// siz[u]：以节点 u 为根的子树大小
// son[u]：节点 u 的重儿子
// top[u]：节点 u 所在链的顶端节点
int fa[MAXN << 1];
int dep[MAXN << 1];
int siz[MAXN << 1];
int son[MAXN << 1];
int top[MAXN << 1];

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

// 记录桥边对应的方点
// x：桥边的一个端点
// y：桥边的另一个端点
// cut：桥边对应的方点编号
void addCut(int x, int y, int cut) {
    int a = min(x, y);  // 确保 a <= b
    int b = max(x, y);
    // 将 a 和 b 作为键存储到 cutMap 中
    cutMap[{a, b}] = cut;
}

// 获取桥边对应的方点
// x：桥边的一个端点
// y：桥边的另一个端点
// 返回值：桥边对应的方点编号，0 表示不是桥边
int getCut(int x, int y) {
    int a = min(x, y);  // 确保 a <= b
    int b = max(x, y);
    // 在 cutMap 中查找对应的方点编号
    auto ans = cutMap.find({a, b});
    return ans == cutMap.end() ? 0 : ans->second;
}

// Tarjan 算法，用于建立圆方树
// u：当前处理的节点
// preEdge：父边的索引，用于避免重复访问父节点
void tarjan(int u, int preEdge) {
    dfn[u] = low[u] = ++cntd;  // 初始化时间戳和 low 值
    sta[++cnts] = u;             // 将当前节点压入栈
    // 遍历当前节点的所有邻边
    for (int e = head1[u]; e > 0; e = next1[e]) {
        if ((e ^ 1) == preEdge) {  // 跳过父边（无向图的反向边）
            continue;
        }
        int v = to1[e];  // 获取邻边指向的节点
        if (dfn[v] == 0) {  // 如果邻节点未被访问过
            tarjan(v, e);  // 递归处理邻节点
            // 更新当前节点的 low 值
            low[u] = min(low[u], low[v]);
            // 如果邻节点的 low 值大于等于当前节点的 dfn 值
            // 说明当前节点是一个割点，需要建立圆方树的方点
            if (low[v] >= dfn[u]) {
                cntn++;  // 圆方树节点数加 1（新增一个方点）
                // 如果邻节点的 low 值大于当前节点的 dfn 值
                // 说明当前边是桥边，记录桥边对应的方点
                if (low[v] > dfn[u]) {
                    addCut(u, v, cntn);
                }
                // 建立方点与当前节点的双向边
                addEdge2(cntn, u);
                addEdge2(u, cntn);
                int pop;
                // 将栈中从 v 到当前节点的所有节点弹出
                // 并建立这些节点与新方点的双向边
                do {
                    pop = sta[cnts--];  // 弹出栈顶节点
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

// 深度优先搜索，用于树链剖分的第一遍处理
// 计算节点的父节点、深度、子树大小和重儿子
// u：当前处理的节点
// f：当前节点的父节点
void dfs1(int u, int f) {
    fa[u] = f;      // 记录父节点
    dep[u] = dep[f] + 1;  // 计算深度
    siz[u] = 1;     // 初始化子树大小
    // 遍历当前节点的所有邻边
    for (int e = head2[u], v; e > 0; e = next2[e]) {
        v = to2[e];  // 获取邻边指向的节点
        if (v != f) {  // 如果邻节点不是父节点
            dfs1(v, u);  // 递归处理邻节点
            siz[u] += siz[v];  // 更新子树大小
            // 更新重儿子（选择子树最大的节点作为重儿子）
            if (son[u] == 0 || siz[son[u]] < siz[v]) {
                son[u] = v;
            }
        }
    }
}

// 深度优先搜索，用于树链剖分的第二遍处理
// 计算节点所在链的顶端节点
// u：当前处理的节点
// t：当前节点所在链的顶端节点
void dfs2(int u, int t) {
    top[u] = t;  // 记录当前节点所在链的顶端节点
    if (son[u] == 0) {  // 如果是叶子节点，返回
        return;
    }
    dfs2(son[u], t);  // 递归处理重儿子，保持链的顶端节点不变
    // 遍历当前节点的所有邻边
    for (int e = head2[u], v; e > 0; e = next2[e]) {
        v = to2[e];  // 获取邻边指向的节点
        // 如果邻节点不是父节点且不是重儿子
        if (v != fa[u] && v != son[u]) {
            dfs2(v, v);  // 递归处理轻儿子，链的顶端节点为自身
        }
    }
}

// 判断节点 c 是否在 a 到 b 的路径上
// a：路径的起点
// b：路径的终点
// c：需要判断的节点
// 返回值：true 表示 c 在 a 到 b 的路径上，false 表示不在
bool mustPass(int a, int b, int c) {
    // 当 a 和 b 不在同一条链上时
    while (top[a] != top[b]) {
        // 确保 a 所在链的顶端节点深度大于等于 b 所在链的顶端节点深度
        if (dep[top[a]] < dep[top[b]]) {
            swap(a, b);
        }
        // 判断 c 是否在 a 到 top[a] 的路径上
        if (top[c] == top[a] && dep[c] <= dep[a]) {
            return true;
        }
        a = fa[top[a]];  // 将 a 跳转到所在链的顶端节点的父节点
    }
    // 当 a 和 b 在同一条链上时
    if (dep[a] < dep[b]) {  // 确保 a 的深度大于等于 b 的深度
        swap(a, b);
    }
    // 判断 c 是否在 b 到 a 的路径上
    return top[a] == top[c] && dep[b] <= dep[c] && dep[c] <= dep[a];
}

// 主函数，程序入口
int main() {
    // 关闭同步，加速输入输出
    ios::sync_with_stdio(false);
    cin.tie(nullptr);
    // 读取节点数和边数
    cin >> n >> m;
    cntn = n;  // 圆方树初始节点数为原图节点数
    cnt1 = 1;  // 边计数器初始化为 1，避免异或 0 出错
    // 读取 m 条边并添加到原图
    for (int i = 1, u, v; i <= m; i++) {
        cin >> u >> v;
        addEdge1(u, v);  // 添加 u 到 v 的边
        addEdge1(v, u);  // 添加 v 到 u 的边（无向图）
    }
    tarjan(1, 0);  // 使用 Tarjan 算法建立圆方树
    dfs1(1, 0);   // 树链剖分第一遍处理
    dfs2(1, 1);   // 树链剖分第二遍处理
    cin >> q;  // 读取查询数
    // 处理 q 条查询
    for (int i = 1, op, a, b, c, d; i <= q; i++) {
        cin >> op;  // 读取查询类型
        if (op == 1) {  // 类型 1：删除边 c-d，判断 a 和 b 是否连通
            cin >> a >> b >> c >> d;
            int cut = getCut(c, d);  // 获取边 c-d 对应的方点
            if (cut == 0) {  // 如果边 c-d 不是桥边
                cout << "yes\n";  // 删除后 a 和 b 仍然连通
            } else {  // 如果边 c-d 是桥边
                // 判断 cut 是否在 a 到 b 的路径上
                cout << (mustPass(a, b, cut) ? "no" : "yes") << "\n";
            }
        } else {  // 类型 2：删除点 c，判断 a 和 b 是否连通
            cin >> a >> b >> c;
            // 判断 c 是否在 a 到 b 的路径上
            cout << (mustPass(a, b, c) ? "no" : "yes") << "\n";
        }
    }
    return 0;
}