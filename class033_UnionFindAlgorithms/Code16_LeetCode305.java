package class056;

import java.util.*;

/**
 * LeetCode 305. 岛屿数量 II
 * 链接: https://leetcode.cn/problems/number-of-islands-ii/
 * 难度: 困难
 * 
 * 题目描述:
 * 给你一个大小为 m x n 的二进制网格 grid 。网格表示一个地图，其中，0 表示水，1 表示陆地。
 * 最初，网格中的单元格要么是水（0），要么是陆地（1）。
 * 你可以执行 addLand 操作，将位置 (row, col) 的水变成陆地。
 * 返回一个结果数组，其中每个结果[i] 表示在第 i 次操作后，地图中岛屿的数量。
 * 
 * 注意: 一个岛屿被水包围，并且通过水平或垂直方向上相邻的陆地连接而成。
 * 你可以假设网格网格的四边均被水包围。
 * 
 * 示例 1:
 * 输入: m = 3, n = 3, positions = [[0,0],[0,1],[1,2],[2,1]]
 * 输出: [1,1,2,3]
 * 
 * 示例 2:
 * 输入: m = 1, n = 1, positions = [[0,0]]
 * 输出: [1]
 * 
 * 约束条件:
 * 1 <= m, n, positions.length <= 10^4
 * positions[i].length == 2
 * 0 <= positions[i][0] < m
 * 0 <= positions[i][1] < n
 */
public class Code16_LeetCode305 {
    
    /**
     * 使用并查集解决动态岛屿数量问题
     * 时间复杂度: O(L * α(m*n))，其中L是positions的长度
     * 空间复杂度: O(m*n)
     * 
     * 解题思路:
     * 1. 初始化并查集，大小为m*n，初始时所有位置都是水（不属于任何集合）
     * 2. 对于每个添加陆地的操作:
     *    - 如果该位置已经是陆地，直接跳过
     *    - 否则，将该位置标记为陆地，岛屿数量加1
     *    - 检查四个方向，如果相邻位置是陆地，则合并集合，岛屿数量相应减少
     * 3. 记录每次操作后的岛屿数量
     */
    public List<Integer> numIslands2(int m, int n, int[][] positions) {
        List<Integer> result = new ArrayList<>();
        if (m <= 0 || n <= 0 || positions == null || positions.length == 0) {
            return result;
        }
        
        UnionFind uf = new UnionFind(m, n);
        int[][] grid = new int[m][n];  // 0表示水，1表示陆地
        
        // 四个方向：上、右、下、左
        int[][] directions = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};
        
        for (int[] pos : positions) {
            int x = pos[0], y = pos[1];
            
            // 如果该位置已经是陆地，直接添加当前岛屿数量
            if (grid[x][y] == 1) {
                result.add(uf.getCount());
                continue;
            }
            
            // 标记为陆地
            grid[x][y] = 1;
            uf.addLand(x, y);  // 岛屿数量加1
            
            // 检查四个方向，合并相邻的陆地
            for (int[] dir : directions) {
                int newX = x + dir[0];
                int newY = y + dir[1];
                
                // 检查新位置是否在网格内且是陆地
                if (newX >= 0 && newX < m && newY >= 0 && newY < n && grid[newX][newY] == 1) {
                    uf.union(x, y, newX, newY);
                }
            }
            
            result.add(uf.getCount());
        }
        
        return result;
    }
    
    /**
     * 并查集实现，专门用于处理网格岛屿问题
     */
    static class UnionFind {
        private int[] parent;    // 父节点数组
        private int[] rank;      // 秩数组，用于按秩合并
        private int count;        // 当前岛屿数量
        private int n;           // 网格列数
        
        public UnionFind(int m, int n) {
            this.n = n;
            int size = m * n;
            parent = new int[size];
            rank = new int[size];
            count = 0;
            
            // 初始化时，所有位置都指向-1（表示水）
            Arrays.fill(parent, -1);
        }
        
        /**
         * 将二维坐标转换为一维索引
         */
        private int getIndex(int x, int y) {
            return x * n + y;
        }
        
        /**
         * 添加陆地
         */
        public void addLand(int x, int y) {
            int index = getIndex(x, y);
            if (parent[index] == -1) {
                parent[index] = index;  // 指向自己
                rank[index] = 1;
                count++;
            }
        }
        
        /**
         * 查找操作，使用路径压缩优化
         */
        public int find(int x, int y) {
            int index = getIndex(x, y);
            if (parent[index] == -1) {
                return -1;  // 水的位置
            }
            
            if (parent[index] != index) {
                parent[index] = find(parent[index] / n, parent[index] % n);
            }
            return parent[index];
        }
        
        /**
         * 合并两个陆地
         */
        public void union(int x1, int y1, int x2, int y2) {
            int root1 = find(x1, y1);
            int root2 = find(x2, y2);
            
            // 如果有一个是水，或者已经在同一个集合中，则不需要合并
            if (root1 == -1 || root2 == -1 || root1 == root2) {
                return;
            }
            
            // 按秩合并
            if (rank[root1] > rank[root2]) {
                parent[root2] = root1;
            } else if (rank[root1] < rank[root2]) {
                parent[root1] = root2;
            } else {
                parent[root2] = root1;
                rank[root1]++;
            }
            count--;  // 合并后岛屿数量减1
        }
        
        /**
         * 获取当前岛屿数量
         */
        public int getCount() {
            return count;
        }
    }
    
    /**
     * 方法2: 使用哈希表存储陆地位置的优化解法
     * 时间复杂度: O(L * α(L))，其中L是positions的长度
     * 空间复杂度: O(L)
     */
    public List<Integer> numIslands2Optimized(int m, int n, int[][] positions) {
        List<Integer> result = new ArrayList<>();
        if (m <= 0 || n <= 0 || positions == null || positions.length == 0) {
            return result;
        }
        
        // 使用哈希表存储陆地位置，避免使用m*n的数组
        Map<Integer, Integer> parent = new HashMap<>();  // 位置索引 -> 父节点索引
        Map<Integer, Integer> rank = new HashMap<>();    // 位置索引 -> 秩
        int count = 0;
        
        // 四个方向
        int[][] directions = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};
        
        for (int[] pos : positions) {
            int x = pos[0], y = pos[1];
            int index = x * n + y;
            
            // 如果该位置已经是陆地，直接添加当前岛屿数量
            if (parent.containsKey(index)) {
                result.add(count);
                continue;
            }
            
            // 添加新陆地
            parent.put(index, index);
            rank.put(index, 1);
            count++;
            
            // 检查四个方向
            for (int[] dir : directions) {
                int newX = x + dir[0];
                int newY = y + dir[1];
                int newIndex = newX * n + newY;
                
                // 检查新位置是否在网格内且是陆地
                if (newX >= 0 && newX < m && newY >= 0 && newY < n && parent.containsKey(newIndex)) {
                    // 合并集合
                    int root1 = find(index, parent);
                    int root2 = find(newIndex, parent);
                    
                    if (root1 != root2) {
                        // 按秩合并
                        if (rank.get(root1) > rank.get(root2)) {
                            parent.put(root2, root1);
                        } else if (rank.get(root1) < rank.get(root2)) {
                            parent.put(root1, root2);
                        } else {
                            parent.put(root2, root1);
                            rank.put(root1, rank.get(root1) + 1);
                        }
                        count--;
                    }
                }
            }
            
            result.add(count);
        }
        
        return result;
    }
    
    /**
     * 查找操作（用于优化解法）
     */
    private int find(int x, Map<Integer, Integer> parent) {
        if (parent.get(x) != x) {
            parent.put(x, find(parent.get(x), parent));  // 路径压缩
        }
        return parent.get(x);
    }
    
    // 测试方法
    public static void main(String[] args) {
        Code16_LeetCode305 solution = new Code16_LeetCode305();
        
        // 测试用例1
        int m1 = 3, n1 = 3;
        int[][] positions1 = {{0, 0}, {0, 1}, {1, 2}, {2, 1}};
        List<Integer> result1 = solution.numIslands2(m1, n1, positions1);
        System.out.println("测试用例1 - 方法1: " + result1); // 预期: [1, 1, 2, 3]
        
        List<Integer> result1Opt = solution.numIslands2Optimized(m1, n1, positions1);
        System.out.println("测试用例1 - 方法2: " + result1Opt); // 预期: [1, 1, 2, 3]
        
        // 测试用例2
        int m2 = 1, n2 = 1;
        int[][] positions2 = {{0, 0}};
        List<Integer> result2 = solution.numIslands2(m2, n2, positions2);
        System.out.println("测试用例2 - 方法1: " + result2); // 预期: [1]
        
        List<Integer> result2Opt = solution.numIslands2Optimized(m2, n2, positions2);
        System.out.println("测试用例2 - 方法2: " + result2Opt); // 预期: [1]
        
        // 测试用例3: 重复添加同一位置
        int m3 = 2, n3 = 2;
        int[][] positions3 = {{0, 0}, {0, 0}, {0, 1}};
        List<Integer> result3 = solution.numIslands2(m3, n3, positions3);
        System.out.println("测试用例3 - 方法1: " + result3); // 预期: [1, 1, 1]
        
        List<Integer> result3Opt = solution.numIslands2Optimized(m3, n3, positions3);
        System.out.println("测试用例3 - 方法2: " + result3Opt); // 预期: [1, 1, 1]
        
        // 测试用例4: 形成一个大岛屿
        int m4 = 2, n4 = 2;
        int[][] positions4 = {{0, 0}, {0, 1}, {1, 0}, {1, 1}};
        List<Integer> result4 = solution.numIslands2(m4, n4, positions4);
        System.out.println("测试用例4 - 方法1: " + result4); // 预期: [1, 2, 2, 1]
        
        List<Integer> result4Opt = solution.numIslands2Optimized(m4, n4, positions4);
        System.out.println("测试用例4 - 方法2: " + result4Opt); // 预期: [1, 2, 2, 1]
    }
}