/*
 * 暗的连锁 (LOJ 10131)
 * 算法：树上边差分 + LCA (最近公共祖先) + DFS回溯
 * 
 * 【题目来源】
 * LibreOJ
 * 题目链接：https://loj.ac/problem/10131
 * 
 * 【题目描述】
 * 给定一棵包含N个节点的树（N-1条边），以及M条额外的边（非树边）
 * 每条非树边连接两个节点，与树边一起构成一个连通图
 * 求有多少种方案，通过切断一条树边和一条非树边，使得图变得不连通
 * 
 * 【算法原理】
 * 1. 每条非树边(u,v)会在树上形成一个环
 * 2. 使用边差分标记所有被非树边覆盖的树边
 * 3. 根据覆盖次数计算切断方案：
 *    - 覆盖0次：可以与任意一条非树边配对，有m种方案
 *    - 覆盖1次：只能与对应的非树边配对，有1种方案
 *    - 覆盖≥2次：无法通过切断一条树边和一条非树边使图不连通，有0种方案
 * 
 * 【复杂度分析】
 * - 时间复杂度：O(N log N + M log N)
 *   预处理LCA: O(N log N)
 *   处理每条非树边: O(M log N)
 *   计算边覆盖次数: O(N)
 * - 空间复杂度：O(N log N)
 *   链式前向星存储树: O(N)
 *   倍增数组stjump: O(N log N)
 * 
 * 【工程化考量】
 * 1. 链式前向星作为高效的图存储结构，适合树结构的实现
 * 2. 变量命名：使用next_而非next，避免与C++标准库中的next冲突
 * 3. 输入输出效率：使用scanf/printf保证大数据量下的性能
 * 4. 边界处理：根节点(1)的父节点设置为0，避免数组越界
 * 
 * 【最优解分析】
 * 此解法是本题的最优解，时间复杂度为O(N log N + M log N)，无法进一步优化
 * 其他可能的算法如暴力枚举所有树边和非树边需要O(NM)时间，远不如本解法高效
 */

#include <cstdio>
#include <algorithm>
#include <cstring>
using namespace std;

const int MAXN = 100001;  // 最大节点数
const int LIMIT = 17;     // 倍增数组的大小限制，2^17足够处理1e5规模的树

// 全局变量定义
int power;                // 最大幂次，2^power > n
int n, m;                 // 节点数和非树边数
int num[MAXN];            // num[i]表示节点i到父节点的边被非树边覆盖的次数

// 链式前向星存储树结构
int head[MAXN];           // 邻接表头节点数组
int next_[MAXN << 1];     // 下一条边的索引（使用next_避免与C++标准库冲突）
int to[MAXN << 1];        // 边的目标节点
int cnt;                  // 边计数器

// LCA相关数组
int deep[MAXN];           // 节点深度数组
int stjump[MAXN][LIMIT];  // 倍增数组，stjump[u][p]表示u的2^p级祖先
long long ans;            // 最终答案，使用long long避免溢出

/**
 * 计算log2(n)的整数值，表示最大需要的幂次
 * @param n 节点数量
 * @return 最大幂次，满足2^ans <= n/2
 */
int log2(int n) {
    int ans = 0;
    while ((1 << ans) <= (n >> 1)) {
        ans++;
    }
    return ans;
}

/**
 * 初始化数据结构
 */
void build() {
    power = log2(n);
    memset(num, 0, sizeof(num));  // 初始化差分数组
    cnt = 1;                     // 边计数器从1开始，方便链式前向星操作
    memset(head, 0, sizeof(head)); // 初始化邻接表头数组
    ans = 0;                     // 初始化答案
}

/**
 * 添加边到树中（无向边，需要添加双向）
 * @param u 边的起点
 * @param v 边的终点
 */
void addEdge(int u, int v) {
    next_[cnt] = head[u];  // 当前边的next指向当前head[u]
    to[cnt] = v;           // 当前边的目标节点是v
    head[u] = cnt++;       // 更新head[u]为当前边的索引，并递增计数器
}

/**
 * 第一次DFS：预处理深度数组和倍增数组
 * @param u 当前节点
 * @param f 父节点
 */
void dfs1(int u, int f) {
    deep[u] = deep[f] + 1;              // 设置当前节点深度
    stjump[u][0] = f;                   // 设置直接父节点
    
    // 预处理倍增数组
    for (int p = 1; p <= power; p++) {
        stjump[u][p] = stjump[stjump[u][p - 1]][p - 1];
    }
    
    // 递归处理所有子节点
    for (int e = head[u]; e != 0; e = next_[e]) {
        if (to[e] != f) {  // 避免回到父节点
            dfs1(to[e], u);
        }
    }
}

/**
 * 使用倍增法求两个节点的最近公共祖先(LCA)
 * @param a 节点a
 * @param b 节点b
 * @return a和b的最近公共祖先
 */
int lca(int a, int b) {
    // 确保a的深度不小于b
    if (deep[a] < deep[b]) {
        int tmp = a;
        a = b;
        b = tmp;
    }
    
    // 将a上移到与b同一深度
    for (int p = power; p >= 0; p--) {
        if (deep[stjump[a][p]] >= deep[b]) {
            a = stjump[a][p];
        }
    }
    
    // 如果此时a == b，则找到LCA
    if (a == b) {
        return a;
    }
    
    // 同时上移a和b，直到找到LCA
    for (int p = power; p >= 0; p--) {
        if (stjump[a][p] != stjump[b][p]) {
            a = stjump[a][p];
            b = stjump[b][p];
        }
    }
    
    // LCA是a和b的父节点
    return stjump[a][0];
}

/**
 * 第二次DFS：回溯计算每条边的覆盖次数，并计算答案
 * @param u 当前节点
 * @param f 父节点
 */
void dfs2(int u, int f) {
    // 递归处理所有子节点，计算子节点的覆盖次数
    for (int e = head[u], v; e != 0; e = next_[e]) {
        v = to[e];
        if (v != f) {
            dfs2(v, u);  // 后序遍历，先处理子节点
        }
    }
    
    // 计算每条边的贡献
    for (int e = head[u], v, w; e != 0; e = next_[e]) {
        v = to[e];
        if (v != f) {
            w = num[v];  // 获取v到u这条边的覆盖次数
            
            // 【方案数统计逻辑】
            // 1. 覆盖次数为0：这条树边不在任何环中，切断它后图会被分成两部分
            //    此时需要再切断一条非树边，但即使切断任意非树边，图仍然不连通
            //    所以共有m种方案
            if (w == 0) {
                ans += m;
            }
            // 2. 覆盖次数为1：这条树边只在一个环中，切断它后需要切断形成该环的非树边
            //    此时只有1种方案
            else if (w == 1) {
                ans += 1;
            }
            // 3. 覆盖次数≥2：这条树边在多个环中，切断它后图仍然连通
            //    此时无论切断哪条非树边，图仍然连通，所以有0种方案
            // (此处else分支可以省略，因为ans += 0无影响)
            
            // 将子节点的覆盖次数累加到父节点，继续向上传递
            num[u] += num[v];
        }
    }
}

int main() {
    // 读取节点数和非树边数
    scanf("%d", &n);
    build();
    scanf("%d", &m);
    
    // 读取树边并构建邻接表（无向图，每条边添加两次）
    for (int i = 1, u, v; i < n; i++) {
        scanf("%d%d", &u, &v);
        addEdge(u, v);
        addEdge(v, u);
    }
    
    // 预处理LCA所需的深度数组和倍增数组
    dfs1(1, 0);  // 根节点为1，父节点为0（无效节点）
    
    // 处理每条非树边，执行边差分操作
    for (int i = 1, u, v, l; i <= m; i++) {
        scanf("%d%d", &u, &v);
        l = lca(u, v);  // 计算u和v的LCA
        
        // 【树上边差分操作】
        // 1. 在u和v处各+1
        // 2. 在LCA处-2
        // 3. 通过DFS回溯时累加，就能得到每条边被覆盖的次数
        num[u]++;
        num[v]++;
        num[l] -= 2;
    }
    
    // 计算最终答案
    dfs2(1, 0);
    
    // 输出结果
    printf("%lld\n", ans);
    return 0;
}