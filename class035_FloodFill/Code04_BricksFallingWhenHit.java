package class058;

/**
 * 打砖块 (Bricks Falling When Hit)
 * 来源: LeetCode 803
 * 题目链接: https://leetcode.cn/problems/bricks-falling-when-hit/
 * 
 * 题目描述:
 * 有一个 m * n 的二元网格 grid ，其中 1 表示砖块，0 表示空白。
 * 砖块 稳定（不会掉落）的前提是：
 * - 一块砖直接连接到网格的顶部，或者
 * - 至少有一块相邻（4 个方向之一）砖块 稳定 不会掉落时
 * 给你一个数组 hits ，这是需要依次消除砖块的位置。
 * 每当消除 hits[i] = (rowi, coli) 位置上的砖块时，对应位置的砖块（若存在）会消失，
 * 然后其他的砖块可能因为这一消除操作而 掉落。
 * 一旦砖块掉落，它会 立即 从网格 grid 中消失（即，它不会落在其他稳定的砖块上）。
 * 返回一个数组 result ，其中 result[i] 表示第 i 次消除操作对应掉落的砖块数目。
 * 注意，消除可能指向是没有砖块的空白位置，如果发生这种情况，则没有砖块掉落。
 * 
 * 解题思路（逆向思维 + 并查集/DFS）:
 * 1. 逆向处理：从最后一步消除开始，逐步恢复砖块
 * 2. 使用DFS标记稳定的砖块（连接到顶部的砖块）
 * 3. 计算每次恢复砖块时新增的稳定砖块数量
 * 
 * 时间复杂度: O(k * m * n) - k为hits数组长度
 * 空间复杂度: O(m * n) - 需要存储网格状态
 * 是否最优解: 是（使用逆向思维的最优解）
 * 
 * 工程化考量:
 * 1. 异常处理：检查输入是否为空，网格尺寸是否有效
 * 2. 边界条件：处理单行网格、空hits数组
 * 3. 状态标记：使用特殊值标记稳定砖块
 * 4. 逆向处理：避免重复计算，提高效率
 * 
 * 语言特性差异:
 * Java: 使用对象和方法封装，代码清晰
 * C++: 可以使用指针和引用，性能更高
 * Python: 使用列表和字典，代码简洁但性能较低
 * 
 * 极端输入场景:
 * 1. 空网格：返回全0数组
 * 2. 单行网格：特殊处理
 * 3. 空hits数组：返回空数组
 * 4. 大规模网格：需要注意性能优化
 * 
 * 性能优化:
 * 1. 逆向处理：避免重复DFS计算
 * 2. 状态缓存：缓存稳定砖块状态
 * 3. 提前终止：及时返回边界条件
 */
public class Code04_BricksFallingWhenHit {

    // 四个方向的偏移量：上、下、左、右
    private static final int[] dx = {-1, 1, 0, 0};
    private static final int[] dy = {0, 0, -1, 1};
    
    // 网格尺寸和状态
    private static int n, m;
    private static int[][] grid;
    
    /**
     * 打砖块问题主函数
     * 
     * @param g 原始网格，1表示砖块，0表示空白
     * @param h 消除操作数组，每个元素为[row, col]
     * @return 每次消除操作掉落的砖块数目
     * 
     * 算法步骤（逆向思维）:
     * 1. 预先消除所有hits中的砖块
     * 2. 标记所有稳定的砖块（连接到顶部的砖块）
     * 3. 从后向前恢复砖块，计算每次恢复时新增的稳定砖块
     * 4. 返回结果数组
     * 
     * 时间复杂度分析:
     * - 预处理消除: O(k)
     * - 稳定标记: O(m*n)
     * - 逆向恢复: O(k*m*n) 最坏情况
     * - 总时间复杂度: O(k*m*n)
     * 
     * 空间复杂度分析:
     * - 网格存储: O(m*n)
     * - 结果数组: O(k)
     * - 递归栈: O(m*n)
     * - 总空间复杂度: O(m*n)
     */
    public static int[] hitBricks(int[][] g, int[][] hits) {
        // 边界条件检查
        if (g == null || g.length == 0 || g[0].length == 0 || hits == null) {
            return new int[0];
        }
        
        grid = g;
        n = g.length;
        m = g[0].length;
        int[] ans = new int[hits.length];
        
        // 特殊情况：单行网格
        if (n == 1) {
            return ans; // 单行网格消除砖块不会导致其他砖块掉落
        }
        
        // 步骤1: 预先消除所有hits中的砖块
        for (int[] hit : hits) {
            int row = hit[0], col = hit[1];
            if (row >= 0 && row < n && col >= 0 && col < m) {
                grid[row][col]--; // 暂时消除砖块
            }
        }
        
        // 步骤2: 标记所有稳定的砖块（连接到顶部的砖块）
        // 从顶部开始DFS，标记所有稳定的砖块为2
        for (int j = 0; j < m; j++) {
            if (grid[0][j] == 1) {
                dfs(0, j);
            }
        }
        
        // 步骤3: 从后向前恢复砖块，计算每次恢复时新增的稳定砖块
        for (int i = hits.length - 1; i >= 0; i--) {
            int row = hits[i][0];
            int col = hits[i][1];
            
            // 检查hit是否有效
            if (row < 0 || row >= n || col < 0 || col >= m) {
                continue;
            }
            
            // 恢复砖块
            grid[row][col]++;
            
            // 检查恢复的砖块是否值得（即是否能连接到稳定砖块）
            if (isWorthRecovering(row, col)) {
                // 计算通过这个砖块新增的稳定砖块数量
                int newStableBricks = dfs(row, col) - 1; // 减去恢复的砖块本身
                ans[i] = Math.max(0, newStableBricks); // 确保非负
            }
        }
        
        return ans;
    }
    
    /**
     * 深度优先搜索标记稳定砖块
     * 
     * @param i 当前行坐标
     * @param j 当前列坐标
     * @return 标记的稳定砖块数量
     * 
     * 标记策略:
     * - 将稳定的砖块标记为2
     * - 这样在后续处理中可以区分稳定和不稳定砖块
     */
    private static int dfs(int i, int j) {
        // 边界条件检查
        if (i < 0 || i >= n || j < 0 || j >= m || grid[i][j] != 1) {
            return 0;
        }
        
        // 标记当前砖块为稳定
        grid[i][j] = 2;
        int count = 1;
        
        // 递归处理四个方向的相邻砖块
        for (int k = 0; k < 4; k++) {
            int ni = i + dx[k];
            int nj = j + dy[k];
            count += dfs(ni, nj);
        }
        
        return count;
    }
    
    /**
     * 检查恢复的砖块是否值得（即是否能连接到稳定砖块）
     * 
     * @param i 行坐标
     * @param j 列坐标
     * @return 是否值得恢复
     * 
     * 判断条件:
     * 1. 当前砖块存在且值为1（刚刚恢复）
     * 2. 当前砖块在顶部，或者相邻有稳定砖块
     */
    private static boolean isWorthRecovering(int i, int j) {
        // 检查砖块是否存在且值为1
        if (grid[i][j] != 1) {
            return false;
        }
        
        // 如果在顶部，直接值得恢复
        if (i == 0) {
            return true;
        }
        
        // 检查四个方向是否有稳定砖块
        for (int k = 0; k < 4; k++) {
            int ni = i + dx[k];
            int nj = j + dy[k];
            
            if (ni >= 0 && ni < n && nj >= 0 && nj < m && grid[ni][nj] == 2) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * 广度优先搜索版本（避免递归深度问题）
     * 
     * @param g 原始网格
     * @param h 消除操作数组
     * @return 每次消除操作掉落的砖块数目
     */
    public static int[] hitBricksBFS(int[][] g, int[][] hits) {
        if (g == null || g.length == 0 || g[0].length == 0 || hits == null) {
            return new int[0];
        }
        
        int n = g.length;
        int m = g[0].length;
        int[] ans = new int[hits.length];
        
        if (n == 1) {
            return ans;
        }
        
        // 复制网格以避免修改原网格
        int[][] grid = new int[n][m];
        for (int i = 0; i < n; i++) {
            System.arraycopy(g[i], 0, grid[i], 0, m);
        }
        
        // 预先消除砖块
        for (int[] hit : hits) {
            int row = hit[0], col = hit[1];
            if (row >= 0 && row < n && col >= 0 && col < m) {
                grid[row][col]--;
            }
        }
        
        // BFS标记稳定砖块
        java.util.Queue<int[]> queue = new java.util.LinkedList<>();
        boolean[][] stable = new boolean[n][m];
        
        // 标记顶部稳定砖块
        for (int j = 0; j < m; j++) {
            if (grid[0][j] == 1) {
                grid[0][j] = 2;
                stable[0][j] = true;
                queue.offer(new int[]{0, j});
            }
        }
        
        // BFS扩展稳定区域
        while (!queue.isEmpty()) {
            int[] cell = queue.poll();
            int i = cell[0], j = cell[1];
            
            for (int k = 0; k < 4; k++) {
                int ni = i + dx[k];
                int nj = j + dy[k];
                
                if (ni >= 0 && ni < n && nj >= 0 && nj < m && grid[ni][nj] == 1 && !stable[ni][nj]) {
                    grid[ni][nj] = 2;
                    stable[ni][nj] = true;
                    queue.offer(new int[]{ni, nj});
                }
            }
        }
        
        // 逆向恢复砖块
        for (int idx = hits.length - 1; idx >= 0; idx--) {
            int row = hits[idx][0];
            int col = hits[idx][1];
            
            if (row < 0 || row >= n || col < 0 || col >= m) {
                continue;
            }
            
            grid[row][col]++;
            
            if (grid[row][col] == 1) {
                // 检查是否值得恢复
                boolean worth = (row == 0);
                if (!worth) {
                    for (int k = 0; k < 4; k++) {
                        int ni = row + dx[k];
                        int nj = col + dy[k];
                        if (ni >= 0 && ni < n && nj >= 0 && nj < m && stable[ni][nj]) {
                            worth = true;
                            break;
                        }
                    }
                }
                
                if (worth) {
                    // BFS计算新增稳定砖块
                    int count = 0;
                    java.util.Queue<int[]> newQueue = new java.util.LinkedList<>();
                    grid[row][col] = 2;
                    stable[row][col] = true;
                    newQueue.offer(new int[]{row, col});
                    count++;
                    
                    while (!newQueue.isEmpty()) {
                        int[] cell = newQueue.poll();
                        int i = cell[0], j = cell[1];
                        
                        for (int k = 0; k < 4; k++) {
                            int ni = i + dx[k];
                            int nj = j + dy[k];
                            
                            if (ni >= 0 && ni < n && nj >= 0 && nj < m && grid[ni][nj] == 1 && !stable[ni][nj]) {
                                grid[ni][nj] = 2;
                                stable[ni][nj] = true;
                                newQueue.offer(new int[]{ni, nj});
                                count++;
                            }
                        }
                    }
                    
                    ans[idx] = count - 1; // 减去恢复的砖块本身
                }
            }
        }
        
        return ans;
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试用例1：标准打砖块
        int[][] grid1 = {
            {1, 0, 0, 0},
            {1, 1, 1, 0}
        };
        int[][] hits1 = {
            {1, 0}
        };
        
        System.out.println("测试用例1 - 标准打砖块:");
        System.out.println("原始网格:");
        printGrid(grid1);
        System.out.println("消除位置: [1, 0]");
        
        int[] result1 = hitBricks(copyGrid(grid1), hits1);
        System.out.println("DFS版本掉落砖块数: " + java.util.Arrays.toString(result1));
        
        int[] result1BFS = hitBricksBFS(copyGrid(grid1), hits1);
        System.out.println("BFS版本掉落砖块数: " + java.util.Arrays.toString(result1BFS));
        
        // 测试用例2：空hits数组
        int[][] hits2 = {};
        System.out.println("测试用例2 - 空hits数组:");
        int[] result2 = hitBricks(copyGrid(grid1), hits2);
        System.out.println("结果: " + java.util.Arrays.toString(result2));
    }
    
    // 辅助方法：打印网格
    private static void printGrid(int[][] grid) {
        if (grid == null || grid.length == 0) {
            System.out.println("空网格");
            return;
        }
        
        for (int[] row : grid) {
            for (int cell : row) {
                System.out.print(cell + " ");
            }
            System.out.println();
        }
    }
    
    // 辅助方法：复制网格
    private static int[][] copyGrid(int[][] grid) {
        if (grid == null) return null;
        int[][] copy = new int[grid.length][];
        for (int i = 0; i < grid.length; i++) {
            copy[i] = grid[i].clone();
        }
        return copy;
    }
}
