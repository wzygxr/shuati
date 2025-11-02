/**
 * LeetCode 547 - 省份数量
 * https://leetcode-cn.com/problems/number-of-provinces/
 * 
 * 题目描述：
 * 有 n 个城市，其中一些彼此相连，另一些没有相连。如果城市 a 与城市 b 直接相连，且城市 b 与城市 c 直接相连，那么城市 a 与城市 c 间接相连。
 * 
 * 省份是一组直接或间接相连的城市，组内不含其他没有相连的城市。
 * 
 * 给你一个 n x n 的矩阵 isConnected ，其中 isConnected[i][j] = 1 表示第 i 个城市和第 j 个城市直接相连，而 isConnected[i][j] = 0 表示二者不直接相连。
 * 
 * 返回矩阵中省份的数量。
 * 
 * 示例 1：
 * 输入：isConnected = [[1,1,0],[1,1,0],[0,0,1]]
 * 输出：2
 * 
 * 示例 2：
 * 输入：isConnected = [[1,0,0],[0,1,0],[0,0,1]]
 * 输出：3
 * 
 * 解题思路：
 * 1. 使用并查集来管理城市之间的连通关系
 * 2. 遍历矩阵，将相连的城市合并到同一个集合中
 * 3. 最后统计集合的数量，即为省份的数量
 * 
 * 时间复杂度分析：
 * - 初始化并查集：O(n)
 * - 遍历矩阵并合并相连的城市：O(n² * α(n))，其中α是阿克曼函数的反函数，近似为常数
 * - 统计集合数量：O(n)
 * - 总体时间复杂度：O(n² * α(n)) ≈ O(n²)
 * 
 * 空间复杂度分析：
 * - 并查集数组：O(n)
 * - 总体空间复杂度：O(n)
 */

public class Code28_NumberOfProvinces {
    // 并查集的父节点数组
    private int[] parent;
    // 并查集的秩数组，用于按秩合并优化
    private int[] rank;
    
    /**
     * 初始化并查集
     * @param n 城市数量
     */
    public void initUnionFind(int n) {
        parent = new int[n];
        rank = new int[n];
        
        // 初始化，每个城市的父节点是自己，秩为0
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            rank[i] = 0;
        }
    }
    
    /**
     * 查找元素所在集合的根节点，并进行路径压缩
     * @param x 要查找的城市
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
     * 合并两个城市所在的集合
     * @param x 第一个城市
     * @param y 第二个城市
     */
    public void union(int x, int y) {
        int rootX = find(x);
        int rootY = find(y);
        
        if (rootX == rootY) {
            return; // 已经在同一个集合中
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
    }
    
    /**
     * 计算省份的数量
     * @param isConnected 连通矩阵
     * @return 省份数量
     */
    public int findCircleNum(int[][] isConnected) {
        int n = isConnected.length;
        
        // 初始化并查集
        initUnionFind(n);
        
        // 遍历矩阵，合并相连的城市
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (isConnected[i][j] == 1) {
                    union(i, j);
                }
            }
        }
        
        // 统计省份数量（即集合的数量）
        int count = 0;
        for (int i = 0; i < n; i++) {
            if (parent[i] == i) {
                count++;
            }
        }
        
        return count;
    }
    
    /**
     * 主方法，用于测试
     */
    public static void main(String[] args) {
        Code28_NumberOfProvinces solution = new Code28_NumberOfProvinces();
        
        // 测试用例1
        int[][] isConnected1 = {
            {1, 1, 0},
            {1, 1, 0},
            {0, 0, 1}
        };
        System.out.println("测试用例1结果：" + solution.findCircleNum(isConnected1));
        // 预期输出：2
        
        // 测试用例2
        int[][] isConnected2 = {
            {1, 0, 0},
            {0, 1, 0},
            {0, 0, 1}
        };
        System.out.println("测试用例2结果：" + solution.findCircleNum(isConnected2));
        // 预期输出：3
        
        // 测试用例3：所有城市都相连
        int[][] isConnected3 = {
            {1, 1, 1},
            {1, 1, 1},
            {1, 1, 1}
        };
        System.out.println("测试用例3结果：" + solution.findCircleNum(isConnected3));
        // 预期输出：1
        
        // 测试用例4：单个城市
        int[][] isConnected4 = {{1}};
        System.out.println("测试用例4结果：" + solution.findCircleNum(isConnected4));
        // 预期输出：1
        
        // 测试用例5：四个城市，形成两个省份
        int[][] isConnected5 = {
            {1, 1, 0, 0},
            {1, 1, 0, 0},
            {0, 0, 1, 1},
            {0, 0, 1, 1}
        };
        System.out.println("测试用例5结果：" + solution.findCircleNum(isConnected5));
        // 预期输出：2
    }
    
    /**
     * 优化说明：
     * 1. 只遍历矩阵的上三角部分，避免重复处理
     * 2. 使用路径压缩和按秩合并优化并查集的性能
     * 3. 直接在父节点数组中统计集合数量，不需要额外的哈希表
     * 
     * 时间复杂度分析：
     * - 遍历上三角矩阵的时间复杂度为O(n²/2)，即O(n²)
     * - 并查集操作的平均时间复杂度为O(α(n))，其中α是阿克曼函数的反函数
     * - 总体时间复杂度为O(n² * α(n)) ≈ O(n²)
     * 
     * 空间复杂度分析：
     * - 并查集数组的空间复杂度为O(n)
     * - 总体空间复杂度为O(n)
     */
}