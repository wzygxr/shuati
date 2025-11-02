package class056;

import java.util.*;

/**
 * LeetCode 803. 打砖块
 * 链接: https://leetcode.cn/problems/bricks-falling-when-hit/
 * 难度: 困难
 * 
 * 题目描述:
 * 有一个 m x n 的二元网格，其中 1 表示砖块，0 表示空白。砖块稳定（不会掉落）的前提是：
 * 1. 一块砖直接连接到网格的顶部（第一行）
 * 2. 或者至少有一块相邻（4个方向之一）的稳定砖块
 * 
 * 给你一个数组 hits ，这是需要依次消除砖块的位置。每当消除 hits[i] = (rowi, coli) 位置上的砖块时，对应位置的砖块会消失，
 * 然后有一批砖块可能因为这个消除而掉落。你需要返回一个数组 result，其中 result[i] 表示第 i 次消除后掉落的砖块数目。
 * 
 * 注意：消除可能指向没有砖块的位置，此时不会有砖块掉落。
 * 
 * 示例 1:
 * 输入: grid = [[1,0,0,0],[1,1,1,0]], hits = [[1,0]]
 * 输出: [2]
 * 解释:
 * 网格开始为：
 * [[1,0,0,0],
 *  [1,1,1,0]]
 * 消除 (1,0) 的砖块，网格变成：
 * [[1,0,0,0],
 *  [0,1,1,0]]
 * 两个标红的砖块掉落，返回2。
 * 
 * 示例 2:
 * 输入: grid = [[1,0,0,0],[1,1,0,0]], hits = [[1,1],[1,0]]
 * 输出: [0,0]
 * 解释:
 * 网格开始为：
 * [[1,0,0,0],
 *  [1,1,0,0]]
 * 消除 (1,1) 的砖块，网格不变，没有砖块掉落。
 * 消除 (1,0) 的砖块，网格变成：
 * [[1,0,0,0],
 *  [0,0,0,0]]
 * 标红的砖块已经掉落过，所以返回0。
 * 
 * 约束条件:
 * m == grid.length
 * n == grid[i].length
 * 1 <= m, n <= 200
 * grid[i][j] 为 0 或 1
 * 1 <= hits.length <= 4 * 10^4
 * hits[i].length == 2
 * 0 <= xi <= m - 1
 * 0 <= yi <= n - 1
 * 所有 (xi, yi) 互不相同
 */
public class Code21_LeetCode803 {
    
    /**
     * 方法1: 逆向并查集（推荐解法）
     * 时间复杂度: O((M*N + H) * α(M*N))，其中M*N是网格大小，H是hits数量
     * 空间复杂度: O(M*N)
     * 
     * 解题思路:
     * 1. 逆向思考：从最终状态开始，逐步添加被消除的砖块
     * 2. 使用并查集维护连通性，特别关注与顶部相连的连通分量
     * 3. 添加砖块时，计算新增的稳定砖块数量
     */
    public int[] hitBricks(int[][] grid, int[][] hits) {
        int m = grid.length;
        int n = grid[0].length;
        
        // 复制网格，用于逆向处理
        int[][] copy = new int[m][n];
        for (int i = 0; i < m; i++) {
            copy[i] = grid[i].clone();
        }
        
        // 第一步：先消除所有要打的砖块
        for (int[] hit : hits) {
            int x = hit[0], y = hit[1];
            copy[x][y] = 0;
        }
        
        // 第二步：初始化并查集，大小为m*n + 1（额外一个虚拟节点代表顶部）
        int size = m * n;
        UnionFind uf = new UnionFind(size + 1);
        
        // 将第一行的砖块与虚拟顶部节点连接
        for (int j = 0; j < n; j++) {
            if (copy[0][j] == 1) {
                uf.union(j, size);  // 第一行第j列连接到顶部
            }
        }
        
        // 第三步：构建初始连通性（消除所有hits后的状态）
        for (int i = 1; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (copy[i][j] == 1) {
                    // 检查上方砖块
                    if (copy[i-1][j] == 1) {
                        uf.union(i * n + j, (i-1) * n + j);
                    }
                    // 检查左方砖块
                    if (j > 0 && copy[i][j-1] == 1) {
                        uf.union(i * n + j, i * n + j - 1);
                    }
                }
            }
        }
        
        // 第四步：逆向添加砖块
        int hitsLen = hits.length;
        int[] res = new int[hitsLen];
        
        // 四个方向
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        
        for (int i = hitsLen - 1; i >= 0; i--) {
            int x = hits[i][0];
            int y = hits[i][1];
            
            // 如果原始网格中这个位置没有砖块，不会导致掉落
            if (grid[x][y] == 0) {
                res[i] = 0;
                continue;
            }
            
            // 记录添加前的稳定砖块数量
            int origin = uf.getSize(size);
            
            // 如果这是第一行的砖块，连接到顶部
            if (x == 0) {
                uf.union(y, size);
            }
            
            // 添加当前砖块
            copy[x][y] = 1;
            
            // 检查四个方向，连接相邻砖块
            for (int[] dir : directions) {
                int newX = x + dir[0];
                int newY = y + dir[1];
                
                if (newX >= 0 && newX < m && newY >= 0 && newY < n && copy[newX][newY] == 1) {
                    uf.union(x * n + y, newX * n + newY);
                }
            }
            
            // 计算新增的稳定砖块数量（减去当前添加的砖块）
            int current = uf.getSize(size);
            res[i] = Math.max(0, current - origin - 1);
        }
        
        return res;
    }
    
    /**
     * 支持获取连通分量大小的并查集
     */
    static class UnionFind {
        private int[] parent;
        private int[] size;
        
        public UnionFind(int n) {
            parent = new int[n];
            size = new int[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }
        
        public int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }
        
        public void union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);
            
            if (rootX != rootY) {
                // 小树合并到大树下
                if (size[rootX] < size[rootY]) {
                    parent[rootX] = rootY;
                    size[rootY] += size[rootX];
                } else {
                    parent[rootY] = rootX;
                    size[rootX] += size[rootY];
                }
            }
        }
        
        public int getSize(int x) {
            int root = find(x);
            return size[root];
        }
    }
    
    /**
     * 方法2: DFS/BFS解法（适用于小规模数据）
     * 时间复杂度: O(H * M * N)，对于大规模数据会超时
     * 空间复杂度: O(M * N)
     * 
     * 解题思路:
     * 1. 对于每个hit，消除对应砖块
     * 2. 使用DFS/BFS标记所有不稳定的砖块
     * 3. 计算掉落的砖块数量
     */
    public int[] hitBricksDFS(int[][] grid, int[][] hits) {
        int m = grid.length;
        int n = grid[0].length;
        int[][] copy = new int[m][n];
        
        // 复制网格
        for (int i = 0; i < m; i++) {
            copy[i] = grid[i].clone();
        }
        
        // 先消除所有要打的砖块
        for (int[] hit : hits) {
            int x = hit[0], y = hit[1];
            copy[x][y] = 0;
        }
        
        // 标记稳定的砖块（与顶部相连）
        boolean[][] stable = new boolean[m][n];
        for (int j = 0; j < n; j++) {
            if (copy[0][j] == 1) {
                dfsMarkStable(copy, stable, 0, j);
            }
        }
        
        int[] res = new int[hits.length];
        
        // 逆向处理hits
        for (int i = hits.length - 1; i >= 0; i--) {
            int x = hits[i][0];
            int y = hits[i][1];
            
            if (grid[x][y] == 0) {
                res[i] = 0;
                continue;
            }
            
            // 恢复砖块
            copy[x][y] = 1;
            
            // 检查当前砖块是否应该稳定
            boolean shouldBeStable = (x == 0); // 如果在第一行，直接稳定
            
            // 检查四个方向是否有稳定砖块
            int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
            for (int[] dir : directions) {
                int newX = x + dir[0];
                int newY = y + dir[1];
                if (newX >= 0 && newX < m && newY >= 0 && newY < n && stable[newX][newY]) {
                    shouldBeStable = true;
                    break;
                }
            }
            
            if (shouldBeStable) {
                // 从当前砖块开始DFS，标记新稳定的砖块
                int before = countStable(stable);
                dfsMarkStable(copy, stable, x, y);
                int after = countStable(stable);
                res[i] = after - before - 1; // 减去当前添加的砖块
            } else {
                res[i] = 0;
            }
        }
        
        return res;
    }
    
    private void dfsMarkStable(int[][] grid, boolean[][] stable, int x, int y) {
        int m = grid.length;
        int n = grid[0].length;
        
        if (x < 0 || x >= m || y < 0 || y >= n || grid[x][y] == 0 || stable[x][y]) {
            return;
        }
        
        stable[x][y] = true;
        
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int[] dir : directions) {
            dfsMarkStable(grid, stable, x + dir[0], y + dir[1]);
        }
    }
    
    private int countStable(boolean[][] stable) {
        int count = 0;
        for (boolean[] row : stable) {
            for (boolean cell : row) {
                if (cell) count++;
            }
        }
        return count;
    }
    
    /**
     * 方法3: 使用带权并查集优化（记录每个连通分量到顶部的距离）
     * 时间复杂度: 与逆向并查集相同
     * 空间复杂度: O(M*N)
     */
    public int[] hitBricksWeighted(int[][] grid, int[][] hits) {
        int m = grid.length;
        int n = grid[0].length;
        int size = m * n;
        
        // 复制网格
        int[][] copy = new int[m][n];
        for (int i = 0; i < m; i++) {
            copy[i] = grid[i].clone();
        }
        
        // 消除所有hits
        for (int[] hit : hits) {
            copy[hit[0]][hit[1]] = 0;
        }
        
        // 初始化并查集
        WeightedUnionFind uf = new WeightedUnionFind(size);
        
        // 构建初始连通性
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (copy[i][j] == 1) {
                    int index = i * n + j;
                    // 如果是第一行，标记为连接到顶部
                    if (i == 0) {
                        uf.setTopConnected(index);
                    }
                    
                    // 连接相邻砖块
                    if (i > 0 && copy[i-1][j] == 1) {
                        uf.union(index, (i-1) * n + j);
                    }
                    if (j > 0 && copy[i][j-1] == 1) {
                        uf.union(index, i * n + j - 1);
                    }
                }
            }
        }
        
        // 逆向处理
        int[] res = new int[hits.length];
        int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        
        for (int i = hits.length - 1; i >= 0; i--) {
            int x = hits[i][0];
            int y = hits[i][1];
            
            if (grid[x][y] == 0) {
                res[i] = 0;
                continue;
            }
            
            int index = x * n + y;
            int before = uf.getTopConnectedCount();
            
            // 恢复砖块
            copy[x][y] = 1;
            if (x == 0) {
                uf.setTopConnected(index);
            }
            
            // 连接相邻砖块
            for (int[] dir : directions) {
                int newX = x + dir[0];
                int newY = y + dir[1];
                if (newX >= 0 && newX < m && newY >= 0 && newY < n && copy[newX][newY] == 1) {
                    uf.union(index, newX * n + newY);
                }
            }
            
            int after = uf.getTopConnectedCount();
            res[i] = Math.max(0, after - before - 1);
        }
        
        return res;
    }
    
    /**
     * 带权并查集，记录连通分量是否连接到顶部
     */
    static class WeightedUnionFind {
        private int[] parent;
        private boolean[] topConnected; // 根节点是否连接到顶部
        private int[] size;
        private int topCount; // 连接到顶部的砖块总数
        
        public WeightedUnionFind(int n) {
            parent = new int[n];
            topConnected = new boolean[n];
            size = new int[n];
            topCount = 0;
            
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                size[i] = 1;
                topConnected[i] = false;
            }
        }
        
        public int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);
            }
            return parent[x];
        }
        
        public void union(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);
            
            if (rootX != rootY) {
                // 小树合并到大树下
                if (size[rootX] < size[rootY]) {
                    parent[rootX] = rootY;
                    size[rootY] += size[rootX];
                    if (topConnected[rootX] && !topConnected[rootY]) {
                        topConnected[rootY] = true;
                        topCount += size[rootX];
                    }
                } else {
                    parent[rootY] = rootX;
                    size[rootX] += size[rootY];
                    if (topConnected[rootY] && !topConnected[rootX]) {
                        topConnected[rootX] = true;
                        topCount += size[rootY];
                    }
                }
            }
        }
        
        public void setTopConnected(int x) {
            int root = find(x);
            if (!topConnected[root]) {
                topConnected[root] = true;
                topCount += size[root];
            }
        }
        
        public int getTopConnectedCount() {
            return topCount;
        }
    }
    
    // 测试方法
    public static void main(String[] args) {
        Code21_LeetCode803 solution = new Code21_LeetCode803();
        
        // 测试用例1
        int[][] grid1 = {
            {1, 0, 0, 0},
            {1, 1, 1, 0}
        };
        int[][] hits1 = {{1, 0}};
        int[] result1 = solution.hitBricks(grid1, hits1);
        System.out.println("测试用例1: " + Arrays.toString(result1));
        // 预期: [2]
        
        // 测试用例2
        int[][] grid2 = {
            {1, 0, 0, 0},
            {1, 1, 0, 0}
        };
        int[][] hits2 = {{1, 1}, {1, 0}};
        int[] result2 = solution.hitBricks(grid2, hits2);
        System.out.println("测试用例2: " + Arrays.toString(result2));
        // 预期: [0, 0]
        
        // 测试用例3: 更大规模的测试
        int[][] grid3 = {
            {1, 1, 1, 1},
            {1, 1, 1, 1},
            {1, 1, 1, 1}
        };
        int[][] hits3 = {{1, 1}, {2, 2}};
        int[] result3 = solution.hitBricks(grid3, hits3);
        System.out.println("测试用例3: " + Arrays.toString(result3));
        
        System.out.println("逆向并查集解法总结:");
        System.out.println("1. 核心思想: 从最终状态逆向添加砖块");
        System.out.println("2. 优势: 避免重复计算连通性");
        System.out.println("3. 关键技巧: 使用虚拟节点表示顶部");
        System.out.println("4. 适用场景: 需要处理多次消除操作的问题");
    }
    
    /**
     * 工程化考量:
     * 1. 内存优化: 对于超大网格，考虑使用一维数组存储
     * 2. 边界处理: 处理网格为空或hits为空的情况
     * 3. 性能监控: 添加性能统计，监控处理时间
     * 4. 测试覆盖: 编写单元测试覆盖各种边界情况
     * 
     * 扩展应用:
     * 1. 可以扩展到三维网格的打砖块问题
     * 2. 可以处理不同类型的砖块（不同重量、强度）
     * 3. 可以支持动态添加砖块的操作
     */
}