/**
 * LeetCode 1319 - 连通网络的操作次数
 * https://leetcode-cn.com/problems/number-of-operations-to-make-network-connected/
 * 
 * 题目描述：
 * 用以太网线缆将 n 台计算机连接成一个网络，计算机的编号从 0 到 n-1。线缆用 connections 表示，其中 connections[i] = [a, b] 表示连接了计算机 a 和 b。
 * 
 * 网络中的任何一台计算机都可以通过网络直接或者间接访问同一个网络中其他任意一台计算机。
 * 
 * 给你这个计算机网络的初始布线 connections，你可以拔开任意两台直连计算机之间的线缆，并用它连接一对未直连的计算机。请你计算并返回使所有计算机都连通所需的最少操作次数。如果不可能，则返回 -1 。
 * 
 * 示例 1：
 * 输入：n = 4, connections = [[0,1],[0,2],[1,2]]
 * 输出：1
 * 解释：拔下计算机 1 和 2 之间的线缆，并将它插到计算机 1 和 3 上，使所有计算机都连通。
 * 
 * 示例 2：
 * 输入：n = 6, connections = [[0,1],[0,2],[0,3],[1,2],[1,3]]
 * 输出：2
 * 解释：需要至少两条线缆才能连通所有计算机。
 * 
 * 示例 3：
 * 输入：n = 6, connections = [[0,1],[0,2],[0,3],[1,2]]
 * 输出：-1
 * 解释：线缆数量不足。
 * 
 * 解题思路：
 * 1. 使用并查集来计算网络中的连通分量数量
 * 2. 首先，我们需要检查线缆数量是否足够：至少需要n-1条线缆才能连接n台计算机
 * 3. 使用并查集统计连通分量的数量count
 * 4. 将所有计算机连通所需的最少操作次数为count - 1
 * 
 * 时间复杂度分析：
 * - 初始化并查集：O(n)
 * - 处理所有连接：O(m * α(n))，其中m是connections数组的长度，α是阿克曼函数的反函数，近似为常数
 * - 计算连通分量：O(n)
 * - 总体时间复杂度：O(n + m * α(n)) ≈ O(n + m)
 * 
 * 空间复杂度分析：
 * - 并查集数组：O(n)
 * - 总体空间复杂度：O(n)
 */

public class Code26_NumberOfOperationsToMakeNetworkConnected {
    // 并查集的父节点数组
    private int[] parent;
    // 并查集的秩数组，用于按秩合并优化
    private int[] rank;
    // 连通分量的数量
    private int count;
    
    /**
     * 初始化并查集
     * @param n 计算机数量
     */
    public void initUnionFind(int n) {
        parent = new int[n];
        rank = new int[n];
        count = n; // 初始时，每个计算机都是一个独立的连通分量
        
        // 初始化，每个元素的父节点是自己，秩为0
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            rank[i] = 0;
        }
    }
    
    /**
     * 查找元素所在集合的根节点，并进行路径压缩
     * @param x 要查找的元素
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
     * 合并两个元素所在的集合
     * @param x 第一个元素
     * @param y 第二个元素
     * @return 如果两个元素原本不在同一个集合中，则返回true；否则返回false
     */
    public boolean union(int x, int y) {
        int rootX = find(x);
        int rootY = find(y);
        
        if (rootX == rootY) {
            return false; // 已经在同一个集合中
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
        
        // 合并后，连通分量数量减1
        count--;
        return true;
    }
    
    /**
     * 计算使所有计算机都连通所需的最少操作次数
     * @param n 计算机数量
     * @param connections 连接列表
     * @return 最少操作次数，如果不可能则返回-1
     */
    public int makeConnected(int n, int[][] connections) {
        // 检查线缆数量是否足够：至少需要n-1条线缆
        if (connections.length < n - 1) {
            return -1;
        }
        
        // 初始化并查集
        initUnionFind(n);
        
        // 处理所有连接
        for (int[] connection : connections) {
            int a = connection[0];
            int b = connection[1];
            union(a, b);
        }
        
        // 将所有计算机连通所需的最少操作次数为连通分量数量减1
        return count - 1;
    }
    
    /**
     * 主方法，用于测试
     */
    public static void main(String[] args) {
        Code26_NumberOfOperationsToMakeNetworkConnected solution = new Code26_NumberOfOperationsToMakeNetworkConnected();
        
        // 测试用例1
        int n1 = 4;
        int[][] connections1 = {{0, 1}, {0, 2}, {1, 2}};
        System.out.println("测试用例1结果：" + solution.makeConnected(n1, connections1));
        // 预期输出：1
        
        // 测试用例2
        int n2 = 6;
        int[][] connections2 = {{0, 1}, {0, 2}, {0, 3}, {1, 2}, {1, 3}};
        System.out.println("测试用例2结果：" + solution.makeConnected(n2, connections2));
        // 预期输出：2
        
        // 测试用例3
        int n3 = 6;
        int[][] connections3 = {{0, 1}, {0, 2}, {0, 3}, {1, 2}};
        System.out.println("测试用例3结果：" + solution.makeConnected(n3, connections3));
        // 预期输出：-1
        
        // 测试用例4：已经连通的情况
        int n4 = 5;
        int[][] connections4 = {{0, 1}, {1, 2}, {2, 3}, {3, 4}};
        System.out.println("测试用例4结果：" + solution.makeConnected(n4, connections4));
        // 预期输出：0
        
        // 测试用例5：只有一台计算机
        int n5 = 1;
        int[][] connections5 = {};
        System.out.println("测试用例5结果：" + solution.makeConnected(n5, connections5));
        // 预期输出：0
    }
    
    /**
     * 异常处理考虑：
     * 1. 输入参数校验：确保n为正整数，connections数组中的连接是有效的
     * 2. 线缆数量检查：如果线缆数量不足n-1，则无法连接所有计算机
     * 3. 单台计算机的情况：不需要任何线缆，直接返回0
     * 4. 重复连接处理：并查集会自动处理重复连接
     */
    
    /**
     * 优化点：
     * 1. 使用路径压缩优化并查集查找效率
     * 2. 使用按秩合并优化并查集合并效率
     * 3. 提前检查线缆数量，避免不必要的计算
     * 4. 实时维护连通分量数量，避免最后再遍历计算
     */
}