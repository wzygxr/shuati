package class129;

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
 * 这是一个经典的倍增算法（Binary Lifting）问题。倍增算法是一种高效的预处理技术，
 * 能够将树上的跳跃查询时间复杂度从 O(n) 优化到 O(log n)。
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
 * 时间复杂度分析：
 * - 预处理：O(n * log n) - 需要为每个节点处理 log n 个层级
 * - 查询：O(log k) - 最多需要处理 log k 位二进制位
 * 空间复杂度：O(n * log n) - 存储倍增表
 * 
 * 倍增算法总结：
 * 1. 倍增算法是一种基于二进制拆分和预处理的高效算法
 * 2. 核心思想：通过预处理 2^j 步的信息，将大问题分解为多个小问题
 * 3. 典型应用场景：
 *    - 树上祖先查询
 *    - 最近公共祖先（LCA）
 *    - 路径最大值/最小值查询
 *    - 距离计算
 *    - 字符串匹配（KMP的一种优化方式）
 * 4. 优化技巧：
 *    - 合理选择最大幂次 MAX_LOG，通常取 log2(最大可能值) + 1
 *    - 预处理时注意边界条件处理
 *    - 查询时可以提前终止以优化性能
 * 
 * 补充题目汇总：
 * 1. Luogu P1613. 跑路（倍增算法）
 * 2. Codeforces 609E. Minimum spanning tree for each edge（倍增算法）
 * 3. LeetCode 1143. 最长公共子序列（动态规划 + 倍增思想）
 * 4. LeetCode 827. 最大人工岛（倍增思想优化）
 * 5. Codeforces 835D. Palindromic characteristics (字符串倍增)
 * 6. AtCoder ABC160F. Distributing Integers
 * 7. HackerEarth - Ancestor Queries
 * 8. SPOJ - LCA
 * 9. UVa 12950. Airport
 * 10. POJ 3728. The merchant (倍增 + 动态规划)
 * 11. 杭电OJ 6799. Tree
 * 12. 牛客网 NC24460. 树上距离
 * 13. CodeChef - SUBINC
 * 14. MarsCode - Binary Lifting
 * 15. TimusOJ 2133. Medieval History
 * 16. AizuOJ ALDS1_14_D. Pattern Matching
 * 17. Comet OJ C1303. 旅行
 * 18. LOJ 10130. 黑暗城堡
 * 19. 剑指Offer 54. 二叉搜索树的第k大节点（可以用倍增思想优化）
 * 20. acwing 161. 电话线路（倍增 + 二分）
 * 
 * 工程化考量：
 * 1. 在实际应用中，倍增算法常用于：
 *    - 网络路由算法
 *    - 数据库索引优化
 *    - 游戏开发中的路径查找
 *    - 分布式系统中的一致性协议
 * 2. 实现优化：
 *    - 使用位运算加速二进制分解过程
 *    - 考虑空间优化，对于超大树可以使用稀疏表
 *    - 预计算最大需要的幂次，避免浪费空间
 * 3. 线程安全：
 *    - 预处理的倍增表是只读的，可以安全地被多个线程并发访问
 *    - 在多线程环境下初始化时需要注意同步
 * 4. 性能调优：
 *    - 对于频繁查询的场景，可以缓存常用查询结果
 *    - 考虑使用更紧凑的数据结构减少内存占用
 *    - 对于特定的树结构（如二叉树），可以使用更优化的实现
public class LeetCode1483_KthAncestorOfATreeNode {
    // 倍增表，p[i][j] 表示节点 i 的第 2^j 个祖先
    private int[][] p;
    // 最大幂次，17 足够处理 10^5 范围内的节点
    private static final int MAX_LOG = 18;
    
    /**
     * 构造函数，初始化树结构并预处理倍增表
     * 
     * @param n 节点数量
     * @param parent 父节点数组
     */
    public LeetCode1483_KthAncestorOfATreeNode(int n, int[] parent) {
        // 初始化倍增表
        p = new int[n][MAX_LOG];
        
        // 初始化所有值为 -1（表示不存在祖先）
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < MAX_LOG; j++) {
                p[i][j] = -1;
            }
        }
        
        // 初始化直接父节点（2^0 = 1 步）
        for (int i = 0; i < n; i++) {
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
    public int getKthAncestor(int node, int k) {
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
    
    // 测试用例
    public static void main(String[] args) {
        // 测试用例1
        int n1 = 7;
        int[] parent1 = {-1, 0, 0, 1, 1, 2, 2};
        LeetCode1483_KthAncestorOfATreeNode treeAncestor1 = new LeetCode1483_KthAncestorOfATreeNode(n1, parent1);
        
        System.out.println("测试用例1:");
        System.out.println("树结构: 节点0为根，节点1,2是节点0的子节点，节点3,4是节点1的子节点，节点5,6是节点2的子节点");
        System.out.println("getKthAncestor(3, 1) = " + treeAncestor1.getKthAncestor(3, 1)); // 期望输出: 1
        System.out.println("getKthAncestor(5, 2) = " + treeAncestor1.getKthAncestor(5, 2)); // 期望输出: 0
        System.out.println("getKthAncestor(6, 3) = " + treeAncestor1.getKthAncestor(6, 3)); // 期望输出: -1
        
        // 测试用例2
        int n2 = 5;
        int[] parent2 = {-1, 0, 0, 1, 2};
        LeetCode1483_KthAncestorOfATreeNode treeAncestor2 = new LeetCode1483_KthAncestorOfATreeNode(n2, parent2);
        
        System.out.println("\n测试用例2:");
        System.out.println("树结构: 节点0为根，节点1,2是节点0的子节点，节点3是节点1的子节点，节点4是节点2的子节点");
        System.out.println("getKthAncestor(3, 1) = " + treeAncestor2.getKthAncestor(3, 1)); // 期望输出: 1
        System.out.println("getKthAncestor(3, 2) = " + treeAncestor2.getKthAncestor(3, 2)); // 期望输出: 0
        System.out.println("getKthAncestor(4, 3) = " + treeAncestor2.getKthAncestor(4, 3)); // 期望输出: -1
    }
}