/**
 * LeetCode 684 - 冗余连接
 * https://leetcode-cn.com/problems/redundant-connection/
 * 
 * 题目描述：
 * 在本问题中，树指的是一个连通且无环的无向图。
 * 
 * 输入一个图，该图由一个有着N个节点（节点值不重复1, 2, ..., N）的树及一条附加的边构成。附加的边的两个顶点包含在1到N中间，这条附加的边不属于树中已存在的边。
 * 
 * 结果图是一个以边组成的二维数组。每一个边的元素是一对[u, v] ，满足 u < v，表示连接顶点u和v的无向图的边。
 * 
 * 返回一条可以删去的边，使得结果图是一个有着N个节点的树。如果有多个答案，则返回二维数组中最后出现的边。
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
 * 解题思路（并查集）：
 * 1. 对于每一条边(u, v)，检查u和v是否已经连通
 * 2. 如果已经连通，说明这条边是冗余的，可以形成环
 * 3. 否则，将u和v合并到同一个集合中
 * 4. 返回最后一条导致环的边
 * 
 * 时间复杂度分析：
 * - 并查集操作（find和union）的平均时间复杂度为O(α(n))，其中α是阿克曼函数的反函数
 * - 遍历m条边需要O(m * α(n))时间
 * - 总体时间复杂度：O(m * α(n)) ≈ O(m)
 * 
 * 空间复杂度分析：
 * - 并查集数组：O(n)
 * - 总体空间复杂度：O(n)
 */

public class Code34_RedundantConnection {
    private int[] parent;
    private int[] rank; // 用于按秩合并
    
    /**
     * 初始化并查集
     * @param n 节点数量
     */
    private void initUnionFind(int n) {
        parent = new int[n + 1]; // 节点编号从1开始
        rank = new int[n + 1];
        for (int i = 0; i <= n; i++) {
            parent[i] = i;
            rank[i] = 1;
        }
    }
    
    /**
     * 查找元素所在集合的根节点，并进行路径压缩
     * @param x 要查找的元素
     * @return 根节点
     */
    private int find(int x) {
        if (parent[x] != x) {
            parent[x] = find(parent[x]); // 路径压缩
        }
        return parent[x];
    }
    
    /**
     * 合并两个元素所在的集合
     * @param x 第一个元素
     * @param y 第二个元素
     * @return 如果两个元素已经在同一集合中，返回false；否则合并并返回true
     */
    private boolean union(int x, int y) {
        int rootX = find(x);
        int rootY = find(y);
        
        if (rootX == rootY) {
            return false; // 已经连通，说明边是冗余的
        }
        
        // 按秩合并：将较小的树合并到较大的树下
        if (rank[rootX] < rank[rootY]) {
            parent[rootX] = rootY;
        } else if (rank[rootX] > rank[rootY]) {
            parent[rootY] = rootX;
        } else {
            parent[rootY] = rootX;
            rank[rootX]++;
        }
        
        return true;
    }
    
    /**
     * 找到冗余连接
     * @param edges 边的数组
     * @return 冗余的边
     */
    public int[] findRedundantConnection(int[][] edges) {
        int n = edges.length; // 节点数量为n，因为树有n个节点和n-1条边，加上一条冗余边，总共有n条边
        
        // 初始化并查集
        initUnionFind(n);
        
        // 遍历每条边
        for (int[] edge : edges) {
            int u = edge[0];
            int v = edge[1];
            
            // 如果u和v已经连通，说明这条边是冗余的
            if (!union(u, v)) {
                return edge; // 返回最后一条导致环的边
            }
        }
        
        return new int[0]; // 不应该到达这里
    }
    
    /**
     * 打印结果数组
     * @param result 结果数组
     */
    private static void printResult(int[] result) {
        System.out.print("[" + result[0] + ", " + result[1] + "]");
    }
    
    /**
     * 主方法，用于测试
     */
    public static void main(String[] args) {
        Code34_RedundantConnection solution = new Code34_RedundantConnection();
        
        // 测试用例1
        int[][] edges1 = {
            {1, 2},
            {1, 3},
            {2, 3}
        };
        int[] result1 = solution.findRedundantConnection(edges1);
        System.out.print("测试用例1结果：");
        printResult(result1);
        System.out.println();
        // 预期输出：[2, 3]
        
        // 测试用例2
        int[][] edges2 = {
            {1, 2},
            {2, 3},
            {3, 4},
            {1, 4},
            {1, 5}
        };
        int[] result2 = solution.findRedundantConnection(edges2);
        System.out.print("测试用例2结果：");
        printResult(result2);
        System.out.println();
        // 预期输出：[1, 4]
        
        // 测试用例3
        int[][] edges3 = {
            {1, 2},
            {2, 3},
            {3, 4},
            {4, 5},
            {5, 1}
        };
        int[] result3 = solution.findRedundantConnection(edges3);
        System.out.print("测试用例3结果：");
        printResult(result3);
        System.out.println();
        // 预期输出：[5, 1]
    }
    
    /**
     * 优化说明：
     * 1. 使用路径压缩和按秩合并优化并查集操作
     * 2. 边的处理顺序按照输入顺序，确保返回最后一条导致环的边
     * 3. 代码结构清晰，易于理解和维护
     * 
     * 时间复杂度分析：
     * - 并查集操作的平均时间复杂度为O(α(n))
     * - 遍历m条边需要O(m * α(n))时间
     * - 总体时间复杂度：O(m * α(n)) ≈ O(m)
     * 
     * 空间复杂度分析：
     * - 并查集数组：O(n)
     * - 总体空间复杂度：O(n)
     */
}