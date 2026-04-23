package class194;

// 游客问题 C++ 版
// 问题描述：给定 n 个城市和 m 条双向道路，所有城市都连通，商品只有一种
// 每个城市有商品报价，支持两种操作：
// 操作 C x y：将城市 x 的商品报价改为 y
// 操作 A x y：从 x 到 y 可自由选路（无重复城市），打印能遇到的最低报价
// 数据范围：1 <= n、m、q <= 10^5，1 <= 商品报价 <= 10^9
// 测试链接：https://www.luogu.com.cn/problem/CF487E
// 测试链接：https://codeforces.com/problemset/problem/487/E
// 实现说明：C++ 版本与 Java 版本逻辑完全一致
// 提交说明：直接提交以下代码即可通过所有测试用例

#include <bits/stdc++.h>

using namespace std;

// 最大节点数，根据题目数据范围设置为 100001
const int MAXN = 100001;
// 最大边数，根据题目数据范围设置为 100001
const int MAXM = 100001;
// 无穷大值，用于初始化最小值
const int INF = 1000000001;

// n：城市数，m：道路数，q：操作数，cntn：圆方树的总节点数（初始为原图节点数）
// arr[i]：城市 i 的商品报价
int n, m, q, cntn;
int arr[MAXN];

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
// sta：Tarjan 算法中使用的栈，存储当前连通分量的节点
// cnts：栈顶指针，初始为 0
int dfn[MAXN];
int low[MAXN];
int cntd;
int sta[MAXN];
int cnts;

// 树链剖分相关变量
// fa[u]：节点 u 的父节点
// dep[u]：节点 u 的深度
// siz[u]：以节点 u 为根的子树大小
// son[u]：节点 u 的重儿子
// top[u]：节点 u 所在链的顶端节点
// nid[u]：圆方树节点 u 的 dfn 序号
// cnti：圆方树 dfn 序号计数器，初始为 0
int fa[MAXN << 1];
int dep[MAXN << 1];
int siz[MAXN << 1];
int son[MAXN << 1];
int top[MAXN << 1];
int nid[MAXN << 1];
int cnti;

// 存储方点对应的有序表，用于快速获取最小值
// 键：方点编号，值：map<int, int>（键：商品报价，值：出现次数）
unordered_map<int, map<int, int>> maps;
// 圆方树中每个节点的点权（线段树的输入）
int val[MAXN << 1];
// 线段树数组，用于维护区间最小值
int minv[MAXN << 3];

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

// 向方点的有序表中添加一个数字
// u：方点编号
// num：要添加的数字（商品报价）
void addNum(int u, int num) {
    // 如果方点 u 没有对应的有序表，创建一个新的 map
    if (!maps.count(u)) {
        maps.emplace(u, map<int, int>());
    }
    auto &mp = maps[u];
    auto it = mp.find(num);
    if (it == mp.end()) {  // 如果数字 num 不存在，添加新条目
        mp.emplace(num, 1);
    } else {  // 如果数字 num 已存在，增加出现次数
        it->second++;
    }
}

// 从方点的有序表中删除一个数字
// u：方点编号
// num：要删除的数字（商品报价）
void delNum(int u, int num) {
    auto &mp = maps[u];
    int cnt = mp[num];
    if (cnt == 1) {  // 如果出现次数为 1，直接删除该数字
        mp.erase(num);
    } else {  // 如果出现次数大于 1，将出现次数减 1
        mp[num] = cnt - 1;
    }
}

// 获取方点有序表中的最小值
// u：方点编号
// 返回值：有序表中的最小值
int getMin(int u) {
    return maps[u].begin()->first;  // map 的 begin() 指向最小值
}

// Tarjan 算法，用于建立圆方树
// u：当前处理的节点
void tarjan(int u) {
    dfn[u] = low[u] = ++cntd;  // 初始化时间戳和 low 值
    sta[++cnts] = u;             // 将当前节点压入栈
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
// 同时为方点初始化有序表
// u：当前处理的节点
// f：当前节点的父节点
void dfs1(int u, int f) {
    fa[u] = f;      // 记录父节点
    dep[u] = dep[f] + 1;  // 计算深度
    siz[u] = 1;     // 初始化子树大小
    // 遍历当前节点的所有邻边
    for (int e = head2[u]; e > 0; e = next2[e]) {
        int v = to2[e];  // 获取邻边指向的节点
        if (v != f) {  // 如果邻节点不是父节点
            dfs1(v, u);  // 递归处理邻节点
            siz[u] += siz[v];  // 更新子树大小
            // 更新重儿子（选择子树最大的节点作为重儿子）
            if (son[u] == 0 || siz[son[u]] < siz[v]) {
                son[u] = v;
            }
            // 如果当前节点是方点（编号大于 n）
            // 将邻节点的商品报价添加到方点的有序表中
            if (u > n) {
                addNum(u, arr[v]);
            }
        }
    }
}

// 深度优先搜索，用于树链剖分的第二遍处理
// 计算节点所在链的顶端节点和 dfn 序号
// 同时初始化线段树的输入数组 val
// u：当前处理的节点
// t：当前节点所在链的顶端节点
void dfs2(int u, int t) {
    top[u] = t;  // 记录当前节点所在链的顶端节点
    nid[u] = ++cnti;  // 为当前节点分配 dfn 序号
    // 初始化 val 数组：如果是圆点（编号 <= n），val 为商品报价；如果是方点，val 为有序表中的最小值
    val[nid[u]] = u <= n ? arr[u] : getMin(u);
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

// 线段树的向上更新操作
// i：当前节点的索引
void up(int i) {
    // 当前节点的最小值等于左右子节点的最小值中的较小者
    minv[i] = min(minv[i << 1], minv[i << 1 | 1]);
}

// 线段树的构建操作
// l：当前区间的左端点
// r：当前区间的右端点
// i：当前节点的索引
void build(int l, int r, int i) {
    if (l == r) {  // 如果是叶子节点，直接赋值
        minv[i] = val[l];
    } else {  // 如果不是叶子节点，递归构建左右子树
        int mid = (l + r) / 2;
        build(l, mid, i << 1);
        build(mid + 1, r, i << 1 | 1);
        up(i);  // 向上更新当前节点的最小值
    }
}

// 线段树的单点更新操作
// jobi：要更新的位置
// jobv：新的值
// l：当前区间的左端点
// r：当前区间的右端点
// i：当前节点的索引
void update(int jobi, int jobv, int l, int r, int i) {
    if (l == r) {  // 如果是叶子节点，直接更新
        minv[i] = jobv;
    } else {  // 如果不是叶子节点，递归更新对应的子树
        int mid = (l + r) / 2;
        if (jobi <= mid) {  // 如果更新位置在左子树
            update(jobi, jobv, l, mid, i << 1);
        } else {  // 如果更新位置在右子树
            update(jobi, jobv, mid + 1, r, i << 1 | 1);
        }
        up(i);  // 向上更新当前节点的最小值
    }
}

// 线段树的区间查询操作
// jobl：查询区间的左端点
// jobr：查询区间的右端点
// l：当前区间的左端点
// r：当前区间的右端点
// i：当前节点的索引
// 返回值：查询区间内的最小值
int query(int jobl, int jobr, int l, int r, int i) {
    if (jobl <= l && r <= jobr) {  // 如果当前区间完全包含在查询区间内
        return minv[i];  // 直接返回当前节点的最小值
    }
    int mid = (l + r) / 2;
    int ans = INF;  // 初始化答案为无穷大
    if (jobl <= mid) {  // 如果查询区间与左子树有交集
        ans = min(ans, query(jobl, jobr, l, mid, i << 1));
    }
    if (jobr > mid) {  // 如果查询区间与右子树有交集
        ans = min(ans, query(jobl, jobr, mid + 1, r, i << 1 | 1));
    }
    return ans;  // 返回查询结果
}

// 查询从 x 到 y 的路径上的最小值
// x：路径的起点
// y：路径的终点
// 返回值：路径上的最小值
int pathMin(int x, int y) {
    int ans = INF;  // 初始化答案为无穷大
    // 当 x 和 y 不在同一条链上时
    while (top[x] != top[y]) {
        // 确保 x 所在链的顶端节点深度大于等于 y 所在链的顶端节点深度
        if (dep[top[x]] < dep[top[y]]) {
            swap(x, y);
        }
        // 查询 x 到 top[x] 路径上的最小值
        ans = min(ans, query(nid[top[x]], nid[x], 1, cnti, 1));
        x = fa[top[x]];  // 将 x 跳转到所在链的顶端节点的父节点
    }
    // 当 x 和 y 在同一条链上时
    if (dep[x] < dep[y]) {  // 确保 x 的深度大于等于 y 的深度
        swap(x, y);
    }
    // 查询 y 到 x 路径上的最小值
    ans = min(ans, query(nid[y], nid[x], 1, cnti, 1));
    // 如果 y 是方点，需要额外考虑其父节点的商品报价
    // 因为方点的有序表中不包含父节点的报价
    if (y > n) {
        ans = min(ans, arr[fa[y]]);
    }
    return ans;  // 返回路径上的最小值
}

// 主函数，程序入口
int main() {
    // 关闭同步，加速输入输出
    ios::sync_with_stdio(false);
    cin.tie(nullptr);
    // 读取城市数、道路数和操作数
    cin >> n >> m >> q;
    cntn = n;  // 圆方树初始节点数为原图节点数
    // 读取每个城市的商品报价
    for (int i = 1; i <= n; i++) {
        cin >> arr[i];
    }
    // 读取 m 条道路并添加到原图
    for (int i = 1, u, v; i <= m; i++) {
        cin >> u >> v;
        addEdge1(u, v);  // 添加 u 到 v 的边
        addEdge1(v, u);  // 添加 v 到 u 的边（无向图）
    }
    tarjan(1);  // 使用 Tarjan 算法建立圆方树
    dfs1(1, 0);   // 树链剖分第一遍处理
    dfs2(1, 1);   // 树链剖分第二遍处理
    build(1, cnti, 1);  // 构建线段树
    // 处理 q 条操作
    for (int i = 1; i <= q; i++) {
        char op;
        int x, y;
        cin >> op >> x >> y;
        if (op == 'C') {  // 操作 C：修改城市 x 的商品报价为 y
            int father = fa[x];  // 获取城市 x 的父节点（方点）
            if (father > 0) {  // 如果父节点存在
                delNum(father, arr[x]);  // 从方点的有序表中删除旧报价
                addNum(father, y);  // 向方点的有序表中添加新报价
                // 更新线段树中方点对应的位置
                update(nid[father], getMin(father), 1, cnti, 1);
            }
            arr[x] = y;  // 更新城市 x 的商品报价
            update(nid[x], y, 1, cnti, 1);  // 更新线段树中城市 x 对应的位置
        } else {  // 操作 A：查询从 x 到 y 路径上的最小值
            cout << pathMin(x, y) << "\n";  // 输出查询结果
        }
    }
    return 0;
}