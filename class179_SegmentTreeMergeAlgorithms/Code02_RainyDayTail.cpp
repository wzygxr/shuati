// 雨天的尾巴问题 (Rainy Day Tail) - C++版本
// 测试链接 : https://www.luogu.com.cn/problem/P4556

/**
 * 题目来源: Vani有约会 洛谷P4556
 * 题目链接: https://www.luogu.com.cn/problem/P4556
 * 
 * 题目描述:
 * 给定一棵 n 个节点的树和 m 次操作，每次操作在两点间路径上投放某种类型的物品。
 * 要求最后统计每个节点收到最多物品的类型。
 * 
 * 解题思路:
 * 1. 利用树上差分技术，在路径端点和LCA处打标记
 * 2. 为每个节点建立线段树，维护各类型物品的数量
 * 3. 自底向上合并子树信息，查询最大值对应的类型
 * 
 * 算法复杂度:
 * - 时间复杂度: O((n + m) log n)
 * - 空间复杂度: O(n log n)
 * 
 * 树上差分核心思想:
 * 1. 对于路径 u->v，在 u 和 v 处 +1，在 lca(u,v) 和 fa[lca(u,v)] 处 -1
 * 2. 通过DFS遍历，子树内的标记和即为该节点的物品数量
 */

// 为了解决编译问题，使用基本的C头文件
extern "C" {
    int scanf(const char*, ...);
    int printf(const char*, ...);
}

const int MAXN = 100001;
const int MAXV = 100000; // 物品类型值域上限
const int MAXT = MAXN * 50; // 线段树节点数上限
const int MAXP = 20; // 倍增数组大小

int n, m;

// 邻接表存储树结构
int head[MAXN], nxt[MAXN << 1], to[MAXN << 1], cntg;

// 节点深度和倍增跳转表（用于求LCA）
int dep[MAXN];
int stjump[MAXN][MAXP];

// 每个节点对应的线段树根节点及相关数组
int root[MAXN];
int ls[MAXT];
int rs[MAXT];
int maxCnt[MAXT]; // 维护区间最大值
int cntt;

// 答案数组
int ans[MAXN];

/**
 * 添加边到邻接表
 * @param u 起点
 * @param v 终点
 */
void addEdge(int u, int v) {
    nxt[++cntg] = head[u];
    to[cntg] = v;
    head[u] = cntg;
}

/**
 * BFS计算深度和父节点（用于LCA计算）
 */
void bfs() {
    // 简单队列实现
    int queue[MAXN];
    int front = 0, rear = 0;
    int visited[MAXN] = {0};
    
    queue[rear++] = 1;
    visited[1] = 1;
    dep[1] = 1;
    
    while (front < rear) {
        int u = queue[front++];
        // 初始化跳转表
        stjump[u][0] = (dep[u] > 1) ? to[head[u]] : 0;
        for (int p = 1; p < MAXP; p++) {
            stjump[u][p] = stjump[stjump[u][p - 1]][p - 1];
        }
        
        for (int e = head[u]; e; e = nxt[e]) {
            int v = to[e];
            if (!visited[v]) {
                visited[v] = 1;
                dep[v] = dep[u] + 1;
                queue[rear++] = v;
            }
        }
    }
}

/**
 * 求两个节点的最近公共祖先(LCA)
 * @param a 节点a
 * @param b 节点b
 * @return LCA节点
 */
int getLca(int a, int b) {
    // 保证a的深度不小于b
    if (dep[a] < dep[b]) {
        int tmp = a;
        a = b;
        b = tmp;
    }
    // 将a向上跳到与b同一深度
    for (int p = MAXP - 1; p >= 0; p--) {
        if (dep[stjump[a][p]] >= dep[b]) {
            a = stjump[a][p];
        }
    }
    // 如果a就是b，说明b是a的祖先
    if (a == b) {
        return a;
    }
    // a和b一起向上跳，直到它们的父节点相同
    for (int p = MAXP - 1; p >= 0; p--) {
        if (stjump[a][p] != stjump[b][p]) {
            a = stjump[a][p];
            b = stjump[b][p];
        }
    }
    return stjump[a][0]; // 返回父节点即为LCA
}

/**
 * 更新线段树节点信息（维护区间最大值）
 * @param i 节点索引
 */
void up(int i) {
    int left_max = ls[i] ? maxCnt[ls[i]] : 0;
    int right_max = rs[i] ? maxCnt[rs[i]] : 0;
    maxCnt[i] = (left_max > right_max) ? left_max : right_max;
}

/**
 * 在线段树中添加/删除一个值
 * @param jobi 要操作的值（物品类型）
 * @param jobv 操作值（+1表示添加，-1表示删除）
 * @param l 区间左端点
 * @param r 区间右端点
 * @param i 当前节点索引
 * @return 更新后的节点索引
 */
int add(int jobi, int jobv, int l, int r, int i) {
    int rt = i;
    if (rt == 0) {
        rt = ++cntt; // 动态开点
    }
    if (l == r) {
        maxCnt[rt] += jobv; // 叶子节点更新计数
    } else {
        int mid = (l + r) >> 1;
        if (jobi <= mid) {
            ls[rt] = add(jobi, jobv, l, mid, ls[rt]); // 递归更新左子树
        } else {
            rs[rt] = add(jobi, jobv, mid + 1, r, rs[rt]); // 递归更新右子树
        }
        up(rt); // 更新当前节点信息
    }
    return rt;
}

/**
 * 合并两棵线段树
 * @param l 区间左端点
 * @param r 区间右端点
 * @param t1 第一棵线段树根节点
 * @param t2 第二棵线段树根节点
 * @return 合并后的线段树根节点
 */
int merge(int l, int r, int t1, int t2) {
    // 边界条件：如果其中一个节点为空，返回另一个节点
    if (t1 == 0 || t2 == 0) {
        return t1 + t2;
    }
    // 叶子节点：合并节点信息
    if (l == r) {
        maxCnt[t1] += maxCnt[t2]; // 累加计数
    } else {
        // 递归合并左右子树
        int mid = (l + r) >> 1;
        ls[t1] = merge(l, mid, ls[t1], ls[t2]);
        rs[t1] = merge(mid + 1, r, rs[t1], rs[t2]);
        up(t1); // 更新当前节点信息
    }
    return t1;
}

/**
 * 查询最大值对应的物品类型
 * @param l 区间左端点
 * @param r 区间右端点
 * @param i 当前节点索引
 * @return 最大值对应的物品类型
 */
int query(int l, int r, int i) {
    // 叶子节点：返回该类型
    if (l == r) {
        return l;
    }
    int mid = (l + r) >> 1;
    // 根据左右子树的最大值决定递归方向
    int left_max = ls[i] ? maxCnt[ls[i]] : 0;
    int right_max = rs[i] ? maxCnt[rs[i]] : 0;
    if (left_max >= right_max) {
        return query(l, mid, ls[i]);
    } else {
        return query(mid + 1, r, rs[i]);
    }
}

/**
 * DFS遍历树并计算答案
 * @param u 当前节点
 * @param fa 父节点
 */
void dfs(int u, int fa) {
    // 先递归处理所有子节点
    for (int e = head[u]; e; e = nxt[e]) {
        int v = to[e];
        if (v != fa) {
            dfs(v, u);
        }
    }
    
    // 将所有子节点的线段树合并到当前节点
    for (int e = head[u]; e; e = nxt[e]) {
        int v = to[e];
        if (v != fa) {
            root[u] = merge(1, MAXV, root[u], root[v]);
        }
    }
    
    // 如果当前节点有物品，查询最大值对应的类型
    if (maxCnt[root[u]] > 0) {
        ans[u] = query(1, MAXV, root[u]);
    }
}

int main() {
    // 读取节点数和操作数
    scanf("%d%d", &n, &m);
    
    // 读取边信息
    for (int i = 1, u, v; i < n; i++) {
        scanf("%d%d", &u, &v);
        addEdge(u, v);
        addEdge(v, u);
    }
    
    // BFS计算深度和父节点
    bfs();
    
    // 处理操作
    for (int i = 1, x, y, food; i <= m; i++) {
        scanf("%d%d%d", &x, &y, &food);
        int lca = getLca(x, y);
        int lcafa = stjump[lca][0];
        // 树上差分：在路径端点和LCA处打标记
        root[x] = add(food, 1, 1, MAXV, root[x]);
        root[y] = add(food, 1, 1, MAXV, root[y]);
        root[lca] = add(food, -1, 1, MAXV, root[lca]);
        root[lcafa] = add(food, -1, 1, MAXV, root[lcafa]);
    }
    
    // DFS计算答案
    dfs(1, 0);
    
    // 输出结果
    for (int i = 1; i <= n; i++) {
        printf("%d\n", ans[i]);
    }
    
    return 0;
}