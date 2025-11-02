/*
 * Alyona and a tree (Codeforces 739B)
 * 算法：倍增法 + 二分查找 + 树上差分
 * 
 * 【算法原理】
 * 1. 对于每个节点v，我们需要找到最远的祖先u，使得dist(u,v) <= a[v]
 * 2. 这意味着所有从u到v路径上的节点都是满足条件的祖先
 * 3. 使用树上差分标记这个区间：diff[v]++, diff[parent(u)]--
 * 4. 最后通过DFS回溯累加差分数组，得到每个节点的答案
 * 
 * 【复杂度分析】
 * - 时间复杂度：O(N log N)
 *   预处理倍增数组: O(N log N)
 *   二分查找每个节点: O(N log N)
 *   DFS回溯统计: O(N)
 * - 空间复杂度：O(N log N)
 *   链式前向星存储树: O(N)
 *   倍增数组stjump和stsum: O(N log N)
 * 
 * 【工程化考量】
 * 1. 链式前向星作为高效的图存储结构，适合树结构的实现
 * 2. 变量命名：使用next_edge而非next，避免与C++关键字冲突
 * 3. 使用long long类型存储距离，避免溢出
 * 4. 边界处理：根节点(1)的特殊处理
 * 
 * 【最优解分析】
 * 此解法是本题的最优解，时间复杂度为O(N log N)
 * 暴力枚举每个节点的所有祖先需要O(N^2)时间，远不如本解法高效
 */

#include <iostream>
#include <cstdio>
using namespace std;

const int MAXN = 200001; // 最大节点数
int n;                  // 节点数量
int a[MAXN];            // 节点权值数组

// 链式前向星存储树结构
int head[MAXN];         // 邻接表头节点数组
int next_edge[MAXN << 1]; // 下一条边的索引（避免与C++关键字冲突）
int to[MAXN << 1];      // 边的目标节点
int weight[MAXN << 1];  // 边的权值
int cnt;                // 边计数器

// 深度数组和到根节点的距离数组
int deep[MAXN];         // 节点深度数组
long long dist[MAXN];   // 节点到根节点的距离数组（使用long long避免溢出）

// 倍增数组相关
const int LIMIT = 18;   // 倍增数组的大小限制，2^18足够处理2e5规模的树
int stjump[MAXN][LIMIT]; // 倍增跳跃数组，stjump[u][p]表示u的2^p级祖先
long long stsum[MAXN][LIMIT]; // 倍增距离数组，记录跳跃的路径距离和

// 差分数组和DFS序相关
int diff[MAXN];         // 差分数组，用于标记区间修改
int dfn[MAXN];          // 节点的进入时间戳
int dfn2[MAXN];         // 节点的离开时间戳
int dfc;                // 时间戳计数器

/**
 * 初始化数据结构
 */
void build() {
    cnt = 1;           // 边计数器从1开始，方便链式前向星操作
    for (int i = 0; i <= n; i++) {
        head[i] = 0;   // 初始化邻接表头数组
    }
    dfc = 0;           // 重置时间戳计数器
    for (int i = 1; i <= n; i++) {
        diff[i] = 0;   // 初始化差分数组
    }
}

/**
 * 添加边到树中（无向边，需要添加双向）
 * @param u 边的起点
 * @param v 边的终点
 * @param w 边的权值
 */
void addEdge(int u, int v, int w) {
    next_edge[cnt] = head[u]; // 当前边的next指向当前head[u]
    to[cnt] = v;             // 当前边的目标节点是v
    weight[cnt] = w;         // 当前边的权值是w
    head[u] = cnt++;         // 更新head[u]为当前边的索引，并递增计数器
}

/**
 * 第一次DFS：预处理深度、距离和倍增数组
 * @param u 当前节点
 * @param f 父节点
 * @param d 当前节点到根节点的距离
 */
void dfs1(int u, int f, long long d) {
    deep[u] = deep[f] + 1;   // 设置当前节点深度
    dist[u] = d;             // 设置当前节点到根节点的距离
    dfn[u] = ++dfc;          // 设置进入时间戳
    stjump[u][0] = f;        // 设置直接父节点
    stsum[u][0] = d - dist[f]; // 设置到父节点的距离
    
    // 预处理倍增数组
    for (int p = 1; p < LIMIT; p++) {
        stjump[u][p] = stjump[stjump[u][p - 1]][p - 1];
        stsum[u][p] = stsum[u][p - 1] + stsum[stjump[u][p - 1]][p - 1];
    }
    
    // 递归处理所有子节点
    for (int e = head[u]; e != 0; e = next_edge[e]) {
        if (to[e] != f) {   // 避免回到父节点
            dfs1(to[e], u, d + weight[e]);
        }
    }
    
    dfn2[u] = dfc;          // 设置离开时间戳
}

/**
 * 二分查找满足条件的最远祖先
 * 找到最远的祖先u，使得dist(u,v) <= a[v]
 * 
 * @param v 当前节点
 * @return 最远的满足条件的祖先节点编号
 */
int findFarthestAncestor(int v) {
    // 计算v节点的最远允许距离边界
    long long need = dist[v] - a[v];
    
    // 如果允许距离超过根节点，直接返回根节点
    if (need < 0) {
        return 1; // 根节点
    }
    
    int u = v; // 从v开始向上查找
    
    // 【倍增法向上查找】
    // 从最高幂次开始，尽可能向上跳，但保持祖先的距离 > need
    for (int p = LIMIT - 1; p >= 0; p--) {
        // 确保祖先存在且其距离仍大于need
        if (stjump[u][p] > 0 && dist[stjump[u][p]] > need) {
            u = stjump[u][p];
        }
    }
    
    // 最终确定最远的满足条件的祖先
    int ans;
    if (dist[u] > need) {
        ans = stjump[u][0]; // 如果当前节点距离仍大于need，再向上跳一级
    } else {
        ans = u; // 当前节点距离已满足条件
    }
    
    return ans;
}

/**
 * 第二次DFS：回溯计算差分数组的累加结果
 * 将子节点的差分值累加到父节点，得到每个节点的最终答案
 * 
 * @param u 当前节点
 * @param f 父节点
 */
void dfs2(int u, int f) {
    // 递归处理所有子节点
    for (int e = head[u]; e != 0; e = next_edge[e]) {
        int v = to[e];
        if (v != f) {   // 避免回到父节点
            dfs2(v, u); // 后序遍历，先处理子节点
            diff[u] += diff[v]; // 累加子节点的差分值
        }
    }
}

int main() {
    // 读取节点数量
    scanf("%d", &n);
    
    // 初始化数据结构
    build();
    
    // 读取每个节点的权值
    for (int i = 1; i <= n; i++) {
        scanf("%d", &a[i]);
    }
    
    // 读取树的边（题目中树是有根的，父节点编号小于子节点）
    for (int i = 2, p, w; i <= n; i++) {
        scanf("%d %d", &p, &w); // 读取父节点和边权
        addEdge(p, i, w);        // 添加正向边
        addEdge(i, p, w);        // 添加反向边（无向图）
    }
    
    // 预处理深度、距离和倍增数组
    dfs1(1, 0, 0); // 根节点为1，父节点为0（无效节点），距离为0
    
    // 【树上差分标记】
    // 对每个节点v，找到满足条件的最远祖先u，并标记区间[u, v]
    for (int v = 1; v <= n; v++) {
        int u = findFarthestAncestor(v); // 找到最远的满足条件的祖先
        
        // 执行区间标记：所有从u到v的祖先都满足条件
        // 这等价于：在v处+1，在u的父节点处-1（如果u不是根节点）
        if (u != 1) { // 如果u不是根节点
            diff[stjump[u][0]]--; // u的父节点减1，结束区间
            diff[v]++;           // v处加1，开始区间
        } else { // 如果u是根节点
            diff[v]++;           // 直接在v处加1，区间从根节点开始
        }
    }
    
    // 计算差分数组的累加结果，得到每个节点的最终答案
    dfs2(1, 0);
    
    // 输出结果，每个节点的满足条件的祖先数量
    for (int i = 1; i <= n; i++) {
        printf("%d ", diff[i]);
    }
    printf("\n");
    
    return 0;
}