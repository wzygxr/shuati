/**
 * LeetCode 1483. Kth Ancestor of a Tree Node
 * 
 * 题目描述：
 * 给你一棵树，树上有 n 个节点，编号从 0 到 n-1。
 * 树用一个父节点数组 parent 来表示，其中 parent[i] 是节点 i 的父节点。
 * 节点 0 是树的根节点，所以 parent[0] = -1。
 * 
 * 实现 TreeAncestor 类：
 * - TreeAncestor(int n, int[] parent)：初始化树结构
 * - getKthAncestor(int node, int k)：返回节点 node 的第 k 个祖先节点，如果不存在则返回 -1
 * 
 * 解题思路：
 * 这是一个经典的倍增算法（Binary Lifting）问题。
 * 
 * 算法步骤：
 * 1. 预处理阶段：构建倍增表
 *    - 创建二维数组 p[i][j]，表示节点 i 的第 2^j 个祖先
 *    - p[i][0] = parent[i]（第 1 个祖先就是直接父节点）
 *    - p[i][j] = p[p[i][j-1]][j-1]（第 2^j 个祖先 = 第 2^(j-1) 个祖先的第 2^(j-1) 个祖先）
 * 2. 查询阶段：利用二进制分解
 *    - 将 k 分解为二进制表示
 *    - 对于 k 的每一位为 1 的位置 j，向上跳 2^j 步
 * 
 * 时间复杂度：
 * - 预处理：O(n * log n)
 * - 查询：O(log k)
 * 空间复杂度：O(n * log n)
 * 
 * 相关题目：
 * - Luogu P1613. 跑路（倍增算法）
 * - Codeforces 609E. Minimum spanning tree for each edge（倍增算法）
 */

// 简化版C++实现，避免使用STL容器
// 由于编译环境限制，使用基本数组和手动实现算法

const int MAX_N = 50005;
const int MAX_LOG = 18;

// 倍增表，p[i][j] 表示节点 i 的第 2^j 个祖先
int p[MAX_N][MAX_LOG];
int n;

/**
 * 初始化树结构并预处理倍增表
 * 
 * @param n_input 节点数量
 * @param parent 父节点数组
 * @param parent_size 父节点数组大小
 */
void initialize(int n_input, int parent[], int parent_size) {
    n = n_input;
    
    // 初始化所有值为 -1（表示不存在祖先）
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < MAX_LOG; j++) {
            p[i][j] = -1;
        }
    }
    
    // 初始化直接父节点（2^0 = 1 步）
    for (int i = 0; i < parent_size && i < n; i++) {
        p[i][0] = parent[i];
    }
    
    // 构建倍增表
    for (int j = 1; j < MAX_LOG; j++) {
        for (int i = 0; i < n; i++) {
            // 如果第 2^(j-1) 个祖先存在
            if (p[i][j - 1] != -1) {
                // 第 2^j 个祖先 = 第 2^(j-1) 个祖先的第 2^(j-1) 个祖先
                p[i][j] = p[p[i][j - 1]][j - 1];
            }
        }
    }
}

/**
 * 获取节点 node 的第 k 个祖先
 * 
 * @param node 起始节点
 * @param k 祖先的步数
 * @return 第 k 个祖先节点，如果不存在则返回 -1
 */
int getKthAncestor(int node, int k) {
    // 按二进制位从高到低遍历
    for (int i = MAX_LOG - 1; i >= 0; i--) {
        // 如果 k 的第 i 位是 1
        if (((k >> i) & 1) != 0) {
            // 向上跳 2^i 步
            node = p[node][i];
            // 如果不存在祖先，直接返回 -1
            if (node == -1) {
                return -1;
            }
        }
    }
    return node;
}

// 简单的测试函数
void runTests() {
    // 测试用例1
    int parent1[] = {-1, 0, 0, 1, 1, 2, 2};
    int size1 = 7;
    initialize(size1, parent1, size1);
    // 期望输出: getKthAncestor(3, 1) = 1, getKthAncestor(5, 2) = 0, getKthAncestor(6, 3) = -1
    
    // 测试用例2
    int parent2[] = {-1, 0, 0, 1, 2};
    int size2 = 5;
    initialize(size2, parent2, size2);
    // 期望输出: getKthAncestor(3, 1) = 1, getKthAncestor(3, 2) = 0, getKthAncestor(4, 3) = -1
}