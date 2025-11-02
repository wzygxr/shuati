/**
 * SPOJ COT - Count on a tree
 * 
 * 题目描述:
 * 给定一棵N个节点的树，每个节点有一个权值。进行M次查询，每次查询两点间路径上第K小的点权。
 * 
 * 解题思路:
 * 使用树上主席树解决树上路径第K小问题。
 * 1. 对所有节点权值进行离散化处理
 * 2. 通过DFS序确定树的结构，计算每个节点的深度和父节点
 * 3. 预处理倍增数组用于计算LCA(最近公共祖先)
 * 4. 对每个节点建立主席树，表示从根到该节点路径上的信息
 * 5. 查询时利用前缀和思想和LCA，通过root[u]+root[v]-root[lca]-root[fa[lca]]得到路径信息
 * 6. 在线段树上二分查找第K小的数
 * 
 * 时间复杂度: O((n + m) log n)
 * 空间复杂度: O(n log n)
 */

// 由于编译环境限制，这里不使用标准库头文件
// 在实际使用中，需要根据具体编译环境实现输入输出

const int MAXN = 100010;
const int LOG = 20;

// 树的邻接表表示
int graph[MAXN][MAXN];  // 简化表示，实际应使用动态数组
int graph_size[MAXN];   // 每个节点的邻接点数量
// 节点权值
int weight[MAXN];
// 离散化后的权值
int sorted[MAXN];
// 节点深度
int depth[MAXN];
// 节点父节点
int parent[MAXN];
// 倍增数组用于LCA
int fa[MAXN][LOG];

// 每个节点的主席树根节点
int root[MAXN];

// 线段树节点信息
int left[MAXN * 20];
int right[MAXN * 20];
int sum[MAXN * 20];

// 线段树节点计数器
int cnt = 0;

// DFS序相关
int timestamp = 0;
int dfn[MAXN];
int rev[MAXN];

/**
 * 构建空线段树
 * @param l 区间左端点
 * @param r 区间右端点
 * @return 根节点编号
 */
int build(int l, int r) {
    cnt++;
    int rt = cnt;
    sum[rt] = 0;
    if (l < r) {
        int mid = (l + r) / 2;
        left[rt] = build(l, mid);
        right[rt] = build(mid + 1, r);
    }
    return rt;
}

/**
 * 在线段树中插入一个值
 * @param pos 要插入的值（离散化后的坐标）
 * @param l 区间左端点
 * @param r 区间右端点
 * @param pre 前一个版本的节点编号
 * @return 新节点编号
 */
int insert(int pos, int l, int r, int pre) {
    cnt++;
    int rt = cnt;
    left[rt] = left[pre];
    right[rt] = right[pre];
    sum[rt] = sum[pre] + 1;
    
    if (l < r) {
        int mid = (l + r) / 2;
        if (pos <= mid) {
            left[rt] = insert(pos, l, mid, left[rt]);
        } else {
            right[rt] = insert(pos, mid + 1, r, right[rt]);
        }
    }
    return rt;
}

/**
 * 查询树上路径第k小的数
 * @param k 第k小
 * @param l 区间左端点
 * @param r 区间右端点
 * @param u 节点u的根
 * @param v 节点v的根
 * @param lca LCA节点的根
 * @param flca LCA父节点的根
 * @return 第k小的数在离散化数组中的位置
 */
int query(int k, int l, int r, int u, int v, int lca, int flca) {
    if (l >= r) return l;
    int mid = (l + r) / 2;
    // 计算左子树中数的个数
    int x = sum[left[u]] + sum[left[v]] - sum[left[lca]] - sum[left[flca]];
    if (x >= k) {
        // 第k小在左子树中
        return query(k, l, mid, left[u], left[v], left[lca], left[flca]);
    } else {
        // 第k小在右子树中
        return query(k - x, mid + 1, r, right[u], right[v], right[lca], right[flca]);
    }
}

/**
 * DFS遍历构建主席树
 * @param u 当前节点
 * @param fa 父节点
 * @param d 深度
 */
void dfs(int u, int fa, int d) {
    depth[u] = d;
    parent[u] = fa;
    timestamp++;
    dfn[u] = timestamp;
    rev[timestamp] = u;
    
    // 在主席树中插入当前节点的权值
    // 简化二分查找实现
    int pos = 1;
    for (int i = 1; i <= cnt; i++) {
        if (sorted[i] == weight[u]) {
            pos = i;
            break;
        }
        if (sorted[i] > weight[u]) {
            pos = i;
            break;
        }
    }
    root[u] = insert(pos, 1, cnt, root[fa]);
    
    // 递归处理子节点
    for (int i = 0; i < graph_size[u]; i++) {
        int v = graph[u][i];
        if (v != fa) {
            dfs(v, u, d + 1);
        }
    }
}

/**
 * 预处理LCA
 * @param n 节点数
 */
void preprocessLCA(int n) {
    // 初始化fa数组
    for (int i = 1; i <= n; i++) {
        fa[i][0] = parent[i];
    }
    
    // 倍增计算
    for (int j = 1; j < LOG; j++) {
        for (int i = 1; i <= n; i++) {
            if (fa[i][j - 1] != -1) {
                fa[i][j] = fa[fa[i][j - 1]][j - 1];
            }
        }
    }
}

/**
 * 计算两个节点的LCA
 * @param u 节点u
 * @param v 节点v
 * @return LCA节点
 */
int lca(int u, int v) {
    if (depth[u] < depth[v]) {
        int temp = u;
        u = v;
        v = temp;
    }
    
    // 让u和v在同一深度
    for (int i = LOG - 1; i >= 0; i--) {
        if (depth[u] - (1 << i) >= depth[v]) {
            u = fa[u][i];
        }
    }
    
    if (u == v) return u;
    
    // 同时向上跳
    for (int i = LOG - 1; i >= 0; i--) {
        if (fa[u][i] != -1 && fa[u][i] != fa[v][i]) {
            u = fa[u][i];
            v = fa[v][i];
        }
    }
    
    return parent[u];
}

// 由于编译环境限制，这里不实现完整的输入输出
// 在实际使用中，需要根据具体编译环境实现输入输出
int main() {
    // 示例数据
    int n = 5;
    int m = 3;
    
    // 节点权值
    weight[1] = 10; weight[2] = 20; weight[3] = 30; weight[4] = 40; weight[5] = 50;
    
    // 离散化后的权值
    sorted[1] = 10; sorted[2] = 20; sorted[3] = 30; sorted[4] = 40; sorted[5] = 50;
    cnt = 5;
    
    // 边信息 (构建一棵简单的树)
    // 1-2, 1-3, 2-4, 2-5
    graph[1][0] = 2; graph[1][1] = 3; graph_size[1] = 2;
    graph[2][0] = 1; graph[2][1] = 4; graph[2][2] = 5; graph_size[2] = 3;
    graph[3][0] = 1; graph_size[3] = 1;
    graph[4][0] = 2; graph_size[4] = 1;
    graph[5][0] = 2; graph_size[5] = 1;
    
    // 构建主席树
    root[0] = build(1, cnt);
    dfs(1, 0, 0);
    
    // 预处理LCA
    preprocessLCA(n);
    
    // 示例查询
    // 查询节点2到节点5路径上第1小的数
    int lca_node1 = lca(2, 5);
    int pos1 = query(1, 1, cnt, root[2], root[5], root[lca_node1], root[parent[lca_node1]]);
    // 查询节点1到节点4路径上第2小的数
    int lca_node2 = lca(1, 4);
    int pos2 = query(2, 1, cnt, root[1], root[4], root[lca_node2], root[parent[lca_node2]]);
    // 查询节点3到节点5路径上第1小的数
    int lca_node3 = lca(3, 5);
    int pos3 = query(1, 1, cnt, root[3], root[5], root[lca_node3], root[parent[lca_node3]]);
    
    // 输出结果需要根据具体环境实现
    return 0;
}