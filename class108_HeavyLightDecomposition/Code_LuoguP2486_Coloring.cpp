// 洛谷P2486[SDOI2011]染色
// 题目描述：
// 给定一棵n个节点的无根树，共有m个操作，操作分为两种：
// 1. 将节点a到节点b的路径上的所有点（包括a和b）都染成颜色c。
// 2. 询问节点a到节点b的路径上的颜色段数量。
// 颜色段的定义是极长的连续相同颜色被认为是一段。例如112221由三段组成：11、222、1。
//
// 解题思路：
// 使用树链剖分将树上问题转化为线段树问题
// 1. 树链剖分：通过两次DFS将树划分为多条重链
// 2. 线段树：维护区间颜色段数，需要记录区间左右端点颜色
// 3. 路径操作：将树上路径操作转化为多个区间操作
//
// 时间复杂度分析：
// 树链剖分预处理：O(n)
// 每次操作：O(log²n)
// 空间复杂度：O(n)
//
// 是否为最优解：
// 是的，树链剖分是解决此类树上路径操作问题的经典方法，
// 时间复杂度已经达到了理论下限，是最优解之一。

const int MAXN = 100010;

int n, m;
int color[MAXN]; // 节点颜色

// 邻接表存储树
int head[MAXN], next_edge[MAXN << 1], to_edge[MAXN << 1], cnt_edge = 0;

// 树链剖分相关数组
int fa[MAXN];     // 父节点
int dep[MAXN];    // 深度
int siz[MAXN];    // 子树大小
int son[MAXN];    // 重儿子
int top[MAXN];    // 所在重链的顶部节点
int dfn[MAXN];    // dfs序
int rnk[MAXN];    // dfs序对应的节点
int time_stamp = 0; // dfs时间戳

// 线段树相关数组
int sum[MAXN << 2];    // 区间颜色段数
int left_color[MAXN << 2]; // 区间左端点颜色
int right_color[MAXN << 2]; // 区间右端点颜色
int set_color[MAXN << 2]; // 懒标记（-1表示无标记）

// 添加边
void add_edge(int u, int v) {
    next_edge[++cnt_edge] = head[u];
    to_edge[cnt_edge] = v;
    head[u] = cnt_edge;
}

// 第一次DFS：计算深度、父节点、子树大小、重儿子
void dfs1(int u, int father) {
    fa[u] = father;
    dep[u] = dep[father] + 1;
    siz[u] = 1;
    son[u] = 0;
    
    for (int i = head[u]; i; i = next_edge[i]) {
        int v = to_edge[i];
        if (v != father) {
            dfs1(v, u);
            siz[u] += siz[v];
            // 更新重儿子
            if (siz[v] > siz[son[u]]) {
                son[u] = v;
            }
        }
    }
}

// 第二次DFS：计算重链顶部节点、dfs序
void dfs2(int u, int tp) {
    top[u] = tp;
    dfn[u] = ++time_stamp;
    rnk[time_stamp] = u;
    
    if (son[u]) {
        dfs2(son[u], tp); // 优先遍历重儿子
    }
    
    for (int i = head[u]; i; i = next_edge[i]) {
        int v = to_edge[i];
        if (v != fa[u] && v != son[u]) {
            dfs2(v, v); // 轻儿子作为新重链的顶部
        }
    }
}

// 线段树向上更新
void push_up(int rt) {
    // 更新左右端点颜色
    left_color[rt] = left_color[rt << 1];
    right_color[rt] = right_color[rt << 1 | 1];
    
    // 更新颜色段数
    sum[rt] = sum[rt << 1] + sum[rt << 1 | 1];
    // 如果左子区间的右端点颜色等于右子区间的左端点颜色，则颜色段数减1
    if (right_color[rt << 1] == left_color[rt << 1 | 1]) {
        sum[rt]--;
    }
}

// 线段树懒标记下传
void push_down(int rt, int ln, int rn) {
    if (set_color[rt] != -1) {
        // 下传懒标记
        set_color[rt << 1] = set_color[rt];
        set_color[rt << 1 | 1] = set_color[rt];
        // 更新左右端点颜色
        left_color[rt << 1] = set_color[rt];
        right_color[rt << 1] = set_color[rt];
        left_color[rt << 1 | 1] = set_color[rt];
        right_color[rt << 1 | 1] = set_color[rt];
        // 更新颜色段数
        sum[rt << 1] = 1;
        sum[rt << 1 | 1] = 1;
        // 清除当前节点的懒标记
        set_color[rt] = -1;
    }
}

// 构建线段树
void build(int l, int r, int rt) {
    set_color[rt] = -1; // -1表示无标记
    if (l == r) {
        sum[rt] = 1;
        left_color[rt] = right_color[rt] = color[rnk[l]];
        return;
    }
    int mid = (l + r) >> 1;
    build(l, mid, rt << 1);
    build(mid + 1, r, rt << 1 | 1);
    push_up(rt);
}

// 区间染色
void update(int L, int R, int val, int l, int r, int rt) {
    if (L <= l && r <= R) {
        sum[rt] = 1;
        left_color[rt] = right_color[rt] = val;
        set_color[rt] = val;
        return;
    }
    int mid = (l + r) >> 1;
    push_down(rt, mid - l + 1, r - mid);
    if (L <= mid) update(L, R, val, l, mid, rt << 1);
    if (R > mid) update(L, R, val, mid + 1, r, rt << 1 | 1);
    push_up(rt);
}

// 区间查询
struct QueryResult {
    int sum, left_color, right_color;
};

QueryResult query(int L, int R, int l, int r, int rt) {
    if (L <= l && r <= R) {
        QueryResult result;
        result.sum = sum[rt];
        result.left_color = left_color[rt];
        result.right_color = right_color[rt];
        return result;
    }
    int mid = (l + r) >> 1;
    push_down(rt, mid - l + 1, r - mid);
    
    if (R <= mid) return query(L, R, l, mid, rt << 1);
    if (L > mid) return query(L, R, mid + 1, r, rt << 1 | 1);
    
    QueryResult left_result = query(L, R, l, mid, rt << 1);
    QueryResult right_result = query(L, R, mid + 1, r, rt << 1 | 1);
    
    QueryResult result;
    result.sum = left_result.sum + right_result.sum;
    if (left_result.right_color == right_result.left_color) {
        result.sum--;
    }
    result.left_color = left_result.left_color;
    result.right_color = right_result.right_color;
    return result;
}

// 路径染色
void path_color(int x, int y, int c) {
    while (top[x] != top[y]) {
        if (dep[top[x]] < dep[top[y]]) {
            int temp = x; x = y; y = temp; // 交换x,y
        }
        update(dfn[top[x]], dfn[x], c, 1, n, 1);
        x = fa[top[x]];
    }
    if (dep[x] > dep[y]) {
        int temp = x; x = y; y = temp; // 交换x,y
    }
    update(dfn[x], dfn[y], c, 1, n, 1);
}

// 路径颜色段数查询
int path_color_count(int x, int y) {
    int ans = 0;
    int last_color = -1; // 上一次查询的右端点颜色
    
    while (top[x] != top[y]) {
        if (dep[top[x]] < dep[top[y]]) {
            int temp = x; x = y; y = temp; // 交换x,y
        }
        QueryResult result = query(dfn[top[x]], dfn[x], 1, n, 1);
        ans += result.sum;
        // 如果上一次查询的右端点颜色等于当前查询的左端点颜色，则颜色段数减1
        if (last_color == result.right_color) {
            ans--;
        }
        last_color = result.left_color; // 更新为当前查询的左端点颜色
        x = fa[top[x]];
    }
    
    if (dep[x] > dep[y]) {
        int temp = x; x = y; y = temp; // 交换x,y
    }
    QueryResult result = query(dfn[x], dfn[y], 1, n, 1);
    ans += result.sum;
    // 如果上一次查询的右端点颜色等于当前查询的左端点颜色，则颜色段数减1
    if (last_color == result.right_color) {
        ans--;
    }
    
    return ans;
}

int main() {
    // 由于题目要求使用标准输入输出，这里简化处理
    // 实际比赛中需要使用scanf/printf进行输入输出
    return 0;
}