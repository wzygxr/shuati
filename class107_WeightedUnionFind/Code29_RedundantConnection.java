/**
 * LeetCode 684 - 冗余连接
 * https://leetcode-cn.com/problems/redundant-connection/
 * 
 * 题目描述：
 * 在本问题中，树指的是一个连通且无环的无向图。
 * 
 * 输入一个图，该图由一个有着n个节点（节点值不重复1，2，...，n）的树及一条附加的边构成。附加的边的两个顶点包含在1到n中间，
 * 这条附加的边不属于树中已存在的边。
 * 
 * 结果图是一个以边组成的二维数组edges。每一个边的元素是一对[u, v]，满足u < v，表示连接顶点u和v的无向图的边。
 * 
 * 返回一条可以删去的边，使得结果图是一个有着n个节点的树。如果有多个答案，则返回二维数组中最后出现的边。
 * 
 * 示例 1：
 * 输入: [[1,2], [1,3], [2,3]]
 * 输出: [2,3]
 * 解释: 给定的无向图为:
 *   1
 *  / \
 * 2 - 3
 * 
 * 示例 2：
 * 输入: [[1,2], [2,3], [3,4], [1,4], [1,5]]
 * 输出: [1,4]
 * 解释: 给定的无向图为:
 * 5 - 1 - 2
 *     |   |
 *     4 - 3
 * 
 * 解题思路：
 * 1. 使用并查集来检测环
 * 2. 遍历每一条边，尝试将两个顶点合并
 * 3. 如果两个顶点已经在同一个集合中，说明添加这条边会形成环，这条边就是冗余的
 * 4. 返回最后一条导致环的边
 * 
 * 时间复杂度分析：
 * - 初始化并查集：O(n)
 * - 处理每条边：O(m * α(n))，其中m是边的数量，α是阿克曼函数的反函数，近似为常数
 * - 总体时间复杂度：O(n + m * α(n)) ≈ O(n + m)
 * 
 * 空间复杂度分析：
 * - 并查集数组：O(n)
 * - 总体空间复杂度：O(n)
 */

public class Code29_RedundantConnection {
    // 并查集的父节点数组
    private int[] parent;
    // 并查集的秩数组，用于按秩合并优化
    private int[] rank;
    
    /**
     * 初始化并查集
     * @param n 节点数量
     */
    public void initUnionFind(int n) {
        parent = new int[n + 1]; // 节点编号从1开始
        rank = new int[n + 1];
        
        // 初始化，每个节点的父节点是自己，秩为0
        for (int i = 1; i <= n; i++) {
            parent[i] = i;
            rank[i] = 0;
        }
    }
    
    /**
     * 查找元素所在集合的根节点，并进行路径压缩
     * @param x 要查找的节点
     * @return 根节点
     */
    public int find(int x) {
        if (parent[x] != x) {
            // 路径压缩：将x的父节点直接设置为根节点
            parent[x] = find(parent[x]);
        }
        return parent[x];
    }
    
    /**
     * 合并两个节点所在的集合
     * @param x 第一个节点
     * @param y 第二个节点
     * @return 如果两个节点已经在同一个集合中（即添加这条边会形成环），则返回true；否则返回false
     */
    public boolean union(int x, int y) {
        int rootX = find(x);
        int rootY = find(y);
        
        if (rootX == rootY) {
            return true; // 已经在同一个集合中，添加这条边会形成环
        }
        
        // 按秩合并：将秩小的树连接到秩大的树下
        if (rank[rootX] < rank[rootY]) {
            parent[rootX] = rootY;
        } else if (rank[rootX] > rank[rootY]) {
            parent[rootY] = rootX;
        } else {
            // 秩相同时，任选一个作为根，并增加其秩
            parent[rootY] = rootX;
            rank[rootX]++;
        }
        
        return false; // 合并成功，没有形成环
    }
    
    /**
     * 查找冗余连接
     * @param edges 边的数组
     * @return 冗余的边
     */
    public int[] findRedundantConnection(int[][] edges) {
        int n = edges.length; // 节点数量等于边的数量（树有n-1条边，加上一条冗余边）
        
        // 初始化并查集
        initUnionFind(n);
        
        // 遍历每一条边
        for (int[] edge : edges) {
            int u = edge[0];
            int v = edge[1];
            
            // 如果两个节点已经在同一个集合中，说明添加这条边会形成环
            if (union(u, v)) {
                return edge;
            }
        }
        
        // 根据题目描述，一定存在冗余边，所以不会执行到这里
        return new int[0];
    }
    
    /**
     * 主方法，用于测试
     */
    public static void main(String[] args) {
        Code29_RedundantConnection solution = new Code29_RedundantConnection();
        
        // 测试用例1
        int[][] edges1 = {{1, 2}, {1, 3}, {2, 3}};
        int[] result1 = solution.findRedundantConnection(edges1);
        System.out.print("测试用例1结果：[");
        System.out.print(result1[0] + ", " + result1[1]);
        System.out.println("]");
        // 预期输出：[2, 3]
        
        // 测试用例2
        int[][] edges2 = {{1, 2}, {2, 3}, {3, 4}, {1, 4}, {1, 5}};
        int[] result2 = solution.findRedundantConnection(edges2);
        System.out.print("测试用例2结果：[");
        System.out.print(result2[0] + ", " + result2[1]);
        System.out.println("]");
        // 预期输出：[1, 4]
        
        // 测试用例3
        int[][] edges3 = {{1, 2}, {1, 3}, {3, 4}, {2, 4}, {4, 5}};
        int[] result3 = solution.findRedundantConnection(edges3);
        System.out.print("测试用例3结果：[");
        System.out.print(result3[0] + ", " + result3[1]);
        System.out.println("]");
        // 预期输出：[2, 4]
        
        // 测试用例4：简单情况
        int[][] edges4 = {{1, 2}, {2, 1}}; // 自环的情况
        int[] result4 = solution.findRedundantConnection(edges4);
        System.out.print("测试用例4结果：[");
        System.out.print(result4[0] + ", " + result4[1]);
        System.out.println("]");
        // 预期输出：[2, 1]
    }
    
    /**
     * 优化说明：
     * 1. 使用路径压缩和按秩合并优化并查集的性能
     * 2. 直接在union方法中检测是否形成环，避免了重复的find操作
     * 3. 利用题目特性：节点编号从1开始，数组大小设为n+1
     * 
     * 时间复杂度分析：
     * - 并查集操作的平均时间复杂度为O(α(n))，其中α是阿克曼函数的反函数
     * - 对于m条边，总体时间复杂度为O(n + m * α(n)) ≈ O(n + m)
     * 
     * 空间复杂度分析：
     * - 并查集数组的空间复杂度为O(n)
     * - 总体空间复杂度为O(n)
     */
}