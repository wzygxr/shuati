/**
 * 松鼠的新家 (洛谷 P3258)
 * 题目来源：洛谷
 * 题目链接：https://www.luogu.com.cn/problem/P3258
 * 
 * 题目描述：
 * 松鼠家族的成员需要在树上移动，从一个节点到另一个节点。
 * 给定一棵包含N个节点的树，以及N-1次移动操作。
 * 每次移动操作表示从节点a移动到节点b，经过的路径上的所有节点（包括起点和终点）都会被访问一次。
 * 求每个节点被访问的次数。
 * 
 * 算法原理：树上点差分
 * 树上差分是一种将路径操作转化为点标记操作的高效算法。
 * 对于树上的路径u->v，我们需要让路径上的所有节点计数加1。
 * 通过点差分，我们可以：
 * 1. diff[u]++
 * 2. diff[v]++
 * 3. diff[lca(u,v)]--
 * 4. diff[parent(lca(u,v))]--
 * 最后通过一次DFS回溯累加子节点的差分标记，得到每个节点的最终计数。
 * 
 * 时间复杂度分析：
 * - 预处理LCA：O(N log N)
 * - 差分标记：O(M)，其中M是操作次数
 * - DFS回溯统计：O(N)
 * 总时间复杂度：O(N log N + M)，对于本题M=N-1，所以总时间复杂度为O(N log N)
 * 
 * 空间复杂度分析：
 * - 树的存储：O(N)
 * - LCA倍增数组：O(N log N)
 * - 差分数组：O(N)
 * 总空间复杂度：O(N log N)
 * 
 * 工程化考量：
 * 1. 使用链式前向星存储树结构，节省空间
 * 2. 使用scanf/printf进行高效输入输出，避免cin/cout的性能开销
 * 3. 预处理log2值，优化倍增数组的大小
 * 4. 递归DFS实现简洁明了，但需注意在C++中递归深度过大可能导致栈溢出
 * 5. 注意全局变量的初始化和数组大小的设置
 * 
 * 最优解分析：
 * 树上差分是解决此类路径覆盖问题的最优解，相比暴力遍历每条路径的O(N*M)复杂度，
 * 树上差分可以将时间复杂度优化到O(N log N)，在大规模数据下效率提升显著。
 */

#include <iostream>
#include <cstdio>
#include <algorithm>

using namespace std;

/**
 * 松鼠的新家 (洛谷 P3258) - C++实现
 * 树上点差分算法的经典应用
 */

/**
 * 最大节点数，根据题目数据范围设定
 * 题目中N最大为3e5，设置为300001以避免越界
 */
const int MAXN = 300001;

/**
 * 倍增数组的最大级别
 * 2^19 = 524,288 > 3e5，足够处理最大节点数
 */
const int MAX_LEVEL = 19;

/**
 * 全局变量声明
 */
int n;                       // 节点数
int max_level;               // 倍增数组的最大级别
int visit_order[MAXN];       // 存储节点访问顺序

/**
 * 差分数组，用于记录每个节点的差分标记
 */
int diff[MAXN];

/**
 * 链式前向星存储树结构
 * head[u]：节点u的第一条边
 * next_edge[e]：边e的下一条边
 * to[e]：边e的目标节点
 * edge_count：边计数器
 */
int head[MAXN], next_edge[MAXN << 1], to_[MAXN << 1], edge_count;

/**
 * LCA相关数组
 * depth[u]：节点u的深度
 * stjump[u][p]：节点u的2^p级祖先
 */
int depth[MAXN];
int stjump[MAXN][MAX_LEVEL];

// 函数声明
int find_lca(int a, int b);
void calculate_access_counts(int u, int parent);

/**
 * 计算log2(n)的整数部分，用于确定倍增数组需要的最大级别
 * 
 * @param n 输入的节点数
 * @return 最大的k使得2^k <= n/2
 * 
 * 该函数通过位运算高效计算log2值，避免使用浮点数运算
 */
int calculate_max_level(int n) {
    int result = 0;
    while ((1 << result) <= (n >> 1)) {
        result++;
    }
    return result;
}

/**
 * 初始化树结构和相关数据
 * 
 * 功能：
 * 1. 计算倍增数组所需的最大级别
 * 2. 初始化差分数组和邻接表
 * 3. 重置边计数器
 * 
 * 注意：
 * 在C++中，全局变量会被自动初始化为0，这里显式初始化确保正确性
 */
void initialize_tree() {
    max_level = calculate_max_level(n);
    for (int i = 0; i < MAXN; i++) {
        diff[i] = 0;
        head[i] = 0;
    }
    edge_count = 1;  // 边编号从1开始，方便链式前向星操作
}

/**
 * 向树中添加一条无向边
 * 
 * @param u 边的起始节点
 * @param v 边的结束节点
 * 
 * 注意：
 * 由于树是无向的，通常需要调用两次add_edge(u, v)和add_edge(v, u)
 * 这里只实现了单向添加，在main函数中需要双向添加
 */
void add_edge(int u, int v) {
    next_edge[edge_count] = head[u];
    to_[edge_count] = v;
    head[u] = edge_count++;
}

/**
 * 预处理LCA所需的数据结构
 * 通过深度优先搜索，记录每个节点的深度和倍增跳跃数组
 * 
 * @param u 当前处理的节点
 * @param parent 当前节点的父节点
 * 
 * 时间复杂度：O(N log N)，每个节点需要处理log N次倍增跳跃
 */
void preprocess_lca(int u, int parent) {
    // 设置当前节点的深度（父节点深度+1）
    depth[u] = depth[parent] + 1;
    // 设置当前节点的直接父节点
    stjump[u][0] = parent;
    
    // 预处理倍增数组，stjump[u][p]表示u的2^p级祖先
    // 利用动态规划的思想：u的2^p级祖先 = u的2^(p-1)级祖先的2^(p-1)级祖先
    for (int p = 1; p <= max_level; p++) {
        // 确保不会越界到根节点的父节点（0）
        if (stjump[u][p-1] != 0) {
            stjump[u][p] = stjump[stjump[u][p - 1]][p - 1];
        }
    }
    
    // 深度优先遍历所有子节点
    for (int e = head[u]; e != 0; e = next_edge[e]) {
        int v = to_[e];
        // 避免回到父节点，造成无限递归
        if (v != parent) {
            preprocess_lca(v, u);
        }
    }
}

/**
 * 主函数，处理输入、算法执行和输出
 * 
 * 输入格式：
 * - 第一行：一个整数n，表示节点数
 * - 第二行：n个整数，表示访问顺序
 * - 接下来n-1行：每行两个整数u和v，表示树中的一条无向边
 * 
 * 输出格式：
 * - 输出n行，每行一个整数，表示对应节点被访问的次数
 */
int main() {
    // 读取节点数量
    scanf("%d", &n);
    
    // 初始化数据结构
    initialize_tree();
    
    // 读取每个节点的访问顺序
    for (int i = 1; i <= n; i++) {
        scanf("%d", &visit_order[i]);
    }
    
    // 读取树的边，构建无向树
    for (int i = 1, u, v; i < n; i++) {
        scanf("%d%d", &u, &v);
        add_edge(u, v);
        add_edge(v, u);  // 无向树需要添加双向边
    }
    
    // 预处理LCA所需的数据（根节点设为1）
    preprocess_lca(1, 0);
    
    // 处理每次移动操作 - 执行树上点差分
    for (int i = 1; i < n; i++) {
        int u = visit_order[i];         // 当前移动的起点
        int v = visit_order[i + 1];     // 当前移动的终点
        int lca_node = find_lca(u, v);  // 计算u和v的最近公共祖先
        int lca_father = stjump[lca_node][0]; // LCA的父节点
        
        /**
         * 树上点差分核心操作：
         * 对于路径u->v，我们希望路径上的所有节点计数加1
         * 通过差分技巧，我们只需要修改四个点：
         * 1. diff[u]++ - 在起点增加标记
         * 2. diff[v]++ - 在终点增加标记
         * 3. diff[lca_node]-- - 在LCA处抵消一次（因为u和v都会到达LCA）
         * 4. diff[lca_father]-- - 在LCA的父节点处抵消一次
         */
        diff[u]++;
        diff[v]++;
        diff[lca_node]--;
        // 注意：根节点的父节点是0，不需要对0进行操作
        if (lca_father != 0) {
            diff[lca_father]--;
        }
    }
    
    // 执行DFS回溯，计算每个节点的最终访问次数
    calculate_access_counts(1, 0);
    
    // 输出结果，需要注意题目中的特殊处理
    /**
     * 为什么需要特殊处理？
     * 因为题目中松鼠的移动路径是连续的，除了最后一个终点外，
     * 每个节点如果是某次移动的终点，它也会是下一次移动的起点。
     * 但实际上，松鼠在移动时，起点只算一次访问，而不是两次。
     * 因此，除了最后一个节点外，其他节点的访问次数需要减1。
     */
    for (int i = 1; i <= n; i++) {
        // 最后一个节点（即visit_order[n]）不需要减1
        if (i == visit_order[n]) {
            printf("%d\n", diff[i]);
        } else {
            // 其他节点需要减1
            printf("%d\n", diff[i] - 1);
        }
    }
    
    return 0;
}

/**
 * 使用倍增法计算两个节点的最近公共祖先(LCA)
 * 
 * @param a 第一个节点
 * @param b 第二个节点
 * @return a和b的最近公共祖先
 * 
 * 算法步骤：
 * 1. 确保a的深度不小于b
 * 2. 将a向上跳跃到与b同一深度
 * 3. 如果此时a==b，则直接返回a作为LCA
 * 4. 否则，a和b同时向上跳跃，直到它们的父节点相同
 * 5. 返回最终的父节点作为LCA
 * 
 * 时间复杂度：O(log N)，每次查询最多跳跃log N次
 */
int find_lca(int a, int b) {
    // 步骤1：确保a的深度不小于b
    if (depth[a] < depth[b]) {
        // 交换a和b
        swap(a, b);
    }
    
    // 步骤2：将a向上跳到与b同一深度
    // 从最高级别开始尝试跳跃，确保最大步长
    for (int p = max_level; p >= 0; p--) {
        // 只有当跳跃后的深度不小于b的深度时才跳跃
        if (stjump[a][p] != 0 && depth[stjump[a][p]] >= depth[b]) {
            a = stjump[a][p];
        }
    }
    
    // 步骤3：如果a和b相遇，说明找到了LCA
    if (a == b) {
        return a;
    }
    
    // 步骤4：同时向上跳跃，直到找到LCA的直接子节点
    for (int p = max_level; p >= 0; p--) {
        if (stjump[a][p] != stjump[b][p]) {
            a = stjump[a][p];
            b = stjump[b][p];
        }
    }
    
    // 步骤5：返回它们的父节点作为LCA
    return stjump[a][0];
}

/**
 * 计算每个节点的最终访问次数
 * 通过深度优先搜索回溯，累加子节点的差分标记
 * 这是树上差分的关键步骤，将局部标记转化为全局计数
 * 
 * @param u 当前处理的节点
 * @param parent 当前节点的父节点
 * 
 * 时间复杂度：O(N)，每个节点和边只被访问一次
 * 
 * 注意：该DFS必须在所有差分标记完成后执行
 */
void calculate_access_counts(int u, int parent) {
    // 步骤1：先递归处理所有子节点
    // 采用后序遍历的方式，确保子节点的计数先计算完成
    for (int e = head[u]; e != 0; e = next_edge[e]) {
        int v = to_[e];
        if (v != parent) {
            calculate_access_counts(v, u);
        }
    }
    
    // 步骤2：将子节点的访问次数累加到当前节点
    // 这一步实现了差分标记的传播和累加
    for (int e = head[u]; e != 0; e = next_edge[e]) {
        int v = to_[e];
        if (v != parent) {
            diff[u] += diff[v];
        }
    }
}