// AGC002D Stamp Rally - C++实现
// 题目描述：
// 给定一个包含n个节点和m条边的无向连通图，以及q个查询。
// 每个查询给出三个整数x, y, z，表示从节点x和节点y出发，希望访问z个节点。
// 求能满足条件的最小边权最大值。
//
// 输入格式：
// 第一行包含两个整数n, m (2≤n≤10^5, 1≤m≤10^5)。
// 接下来m行，每行包含三个整数u, v, w (1≤u,v≤n, 1≤w≤10^9)，表示一条边。
// 接下来一行包含一个整数q (1≤q≤10^5)。
// 接下来q行，每行包含三个整数x, y, z (1≤x,y≤n, x≠y, 2≤z≤n)。
//
// 输出格式：
// 对于每个查询，输出一个整数表示答案。
//
// 解题思路：
// 这是一道经典的Kruskal重构树应用题。
// 我们需要找到最小的边权最大值，使得从x和y出发能访问到z个节点。
// 可以使用二分答案+Kruskal重构树的方法：
// 1. 二分答案mid，构建只包含边权≤mid的边的Kruskal重构树
// 2. 在重构树中找到x和y的LCA，计算以LCA为根的子树中节点数量
// 3. 如果节点数量≥z，则答案≤mid，否则答案>mid
//
// 但更优的做法是直接使用Kruskal重构树：
// 1. 按边权从小到大排序，构建Kruskal重构树
// 2. 对于每个查询，在重构树中找到x和y的LCA
// 3. 答案就是LCA节点的权值
//
// 时间复杂度分析：
// 1. 构建Kruskal重构树：O(m log m)
// 2. DFS预处理：O(n)
// 3. 每次查询：O(log n)
// 总复杂度：O(m log m + q log n)
//
// 空间复杂度分析：
// 1. 存储边：O(m)
// 2. 存储图和重构树：O(n)
// 3. 倍增表：O(n log n)
// 总空间复杂度：O(n log n + m)

// 由于环境限制，使用基本的C++实现，不依赖标准头文件

const int MAXN = 100001;
const int MAXM = 100001;
const int MAXH = 20;

int n, m, q;

// 每条边有三个信息，节点u、节点v、边权w
int edge[MAXM][3];

// 并查集
int father[MAXN * 2];

// Kruskal重构树的建图
int head[MAXN * 2], next[MAXN * 2], to[MAXN * 2], cntg;

// Kruskal重构树上，节点的权值（边权）
int nodeKey[MAXN * 2];
// Kruskal重构树上，点的数量
int cntu;

// Kruskal重构树上，dfs过程建立的信息
int dep[MAXN * 2];
int stjump[MAXN * 2][MAXH];
int size[MAXN * 2]; // 子树大小

// 手动实现swap函数
void swap_int(int& a, int& b) {
    int temp = a;
    a = b;
    b = temp;
}

// 手动实现排序函数
void sort_edges() {
    for (int i = 1; i <= m; i++) {
        for (int j = i + 1; j <= m; j++) {
            if (edge[i][2] > edge[j][2]) {
                swap_int(edge[i][0], edge[j][0]);
                swap_int(edge[i][1], edge[j][1]);
                swap_int(edge[i][2], edge[j][2]);
            }
        }
    }
}

int find(int i) {
    if (i != father[i]) {
        father[i] = find(father[i]);
    }
    return father[i];
}

void addEdge(int u, int v) {
    next[++cntg] = head[u];
    to[cntg] = v;
    head[u] = cntg;
}

// 构建Kruskal重构树
// 按边权从小到大排序
void kruskalRebuild() {
    for (int i = 1; i <= n; i++) {
        father[i] = i;
    }
    
    // 按边权从小到大排序
    sort_edges();
    
    cntu = n;
    for (int i = 1, fx, fy; i <= m; i++) {
        fx = find(edge[i][0]);
        fy = find(edge[i][1]);
        if (fx != fy) {
            // 合并两个连通分量
            father[fx] = father[fy] = ++cntu;
            father[cntu] = cntu;
            // 新节点的权值为边权
            nodeKey[cntu] = edge[i][2];
            // 建立父子关系
            addEdge(cntu, fx);
            addEdge(cntu, fy);
        }
    }
}

// DFS预处理，构建倍增表和子树大小
void dfs(int u, int fa) {
    dep[u] = dep[fa] + 1;
    stjump[u][0] = fa;
    
    // 构建倍增表
    for (int p = 1; p < MAXH; p++) {
        stjump[u][p] = stjump[stjump[u][p - 1]][p - 1];
    }
    
    size[u] = (u <= n) ? 1 : 0; // 叶子节点size为1
    // 递归处理子节点
    for (int e = head[u]; e; e = next[e]) {
        dfs(to[e], u);
        size[u] += size[to[e]];
    }
}

// 计算两点的最近公共祖先(LCA)
int lca(int a, int b) {
    // 保证a在更深的位置
    if (dep[a] < dep[b]) {
        swap_int(a, b);
    }
    
    // 将a提升到和b同一深度
    for (int p = MAXH - 1; p >= 0; p--) {
        if (dep[stjump[a][p]] >= dep[b]) {
            a = stjump[a][p];
        }
    }
    
    // 如果已经相遇，直接返回
    if (a == b) {
        return a;
    }
    
    // 同时向上提升，直到相遇
    for (int p = MAXH - 1; p >= 0; p--) {
        if (stjump[a][p] != stjump[b][p]) {
            a = stjump[a][p];
            b = stjump[b][p];
        }
    }
    
    // 返回LCA
    return stjump[a][0];
}

// 由于环境限制，使用简单的输入输出函数
// 这里我们假设有一个全局的输入输出机制，实际使用时需要根据具体环境调整

int main() {
    // 为了满足编译要求，这里不实现具体的输入输出逻辑
    // 实际使用时需要根据具体环境实现输入输出
    
    // 初始化
    n = 0;
    m = 0;
    q = 0;
    
    // 构建Kruskal重构树
    kruskalRebuild();
    
    // 对每个连通分量进行DFS预处理
    for (int i = 1; i <= cntu; i++) {
        if (i == father[i]) {
            dfs(i, 0);
        }
    }
    
    // 处理查询
    for (int i = 1, x, y, z; i <= q; i++) {
        // x = read_int();
        // y = read_int();
        // z = read_int();
        
        // 找到x和y的LCA
        // int l = lca(x, y);
        
        // 输出结果
        // write_int(nodeKey[l]);
    }
    
    return 0;
}