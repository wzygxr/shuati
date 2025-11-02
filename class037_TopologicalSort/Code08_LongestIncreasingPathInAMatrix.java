package class060;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

/**
 * 矩阵中的最长递增路径
 * 给定一个 m x n 整数矩阵 matrix ，找出其中 最长递增路径 的长度。
 * 对于每个单元格，你可以往上，下，左，右四个方向移动。 你 不能 在 对角线 方向上移动或移动到 边界外（即不允许环绕）。
 * 测试链接 : https://leetcode.cn/problems/longest-increasing-path-in-a-matrix/
 * 
 * 算法思路：
 * 这道题可以使用两种方法解决：
 * 1. 记忆化搜索（DFS + 缓存）
 * 2. 拓扑排序（基于入度的BFS）
 * 
 * 解法一：记忆化搜索
 * 1. 对每个单元格进行深度优先搜索
 * 2. 缓存每个单元格的最长递增路径长度
 * 3. 递归搜索四个方向中值更大的相邻单元格
 * 4. 返回当前单元格的最长路径长度
 * 
 * 解法二：拓扑排序
 * 1. 构建图：对于每个单元格，如果相邻单元格的值更大，则建立一条有向边
 * 2. 计算每个单元格的入度
 * 3. 使用拓扑排序，每次处理入度为0的节点
 * 4. 在拓扑排序过程中更新最长路径长度
 * 
 * 时间复杂度：
 * - 记忆化搜索：O(M*N)，每个单元格只被访问一次
 * - 拓扑排序：O(M*N)，构建图和拓扑排序的时间复杂度都是O(M*N)
 * 
 * 空间复杂度：
 * - 记忆化搜索：O(M*N)，需要缓存每个单元格的结果
 * - 拓扑排序：O(M*N)，需要存储图的邻接表和入度数组
 * 
 * 两种方法对比：
 * - 记忆化搜索实现更简洁，但在最坏情况下可能有栈溢出风险
 * - 拓扑排序实现更复杂，但避免了递归调用栈的问题
 * 
 * 工程化考虑：
 * 1. 边界处理：处理空矩阵、单元素矩阵等特殊情况
 * 2. 性能优化：使用缓存避免重复计算
 * 3. 异常处理：验证输入矩阵的有效性
 * 4. 可读性：添加详细注释和变量命名
 * 5. 内存管理：合理使用数据结构减少内存占用
 * 6. 模块化设计：将两种解法分离实现
 * 7. 代码复用：将通用功能封装成独立方法
 * 8. 可维护性：添加详细注释和文档说明
 * 
 * 相关题目：
 * 1. LeetCode 695. 岛屿的最大面积 - https://leetcode.cn/problems/max-area-of-island/
 * 2. LeetCode 542. 01矩阵 - https://leetcode.cn/problems/01-matrix/
 * 3. LeetCode 1091. 二进制矩阵中的最短路径 - https://leetcode.cn/problems/shortest-path-in-binary-matrix/
 * 4. LeetCode 329. 矩阵中的最长递增路径 - https://leetcode.cn/problems/longest-increasing-path-in-a-matrix/
 * 5. LeetCode 79. 单词搜索 - https://leetcode.cn/problems/word-search/
 * 6. LeetCode 200. 岛屿数量 - https://leetcode.cn/problems/number-of-islands/
 * 7. LeetCode 130. 被围绕的区域 - https://leetcode.cn/problems/surrounded-regions/
 * 8. LeetCode 417. 太平洋大西洋水流问题 - https://leetcode.cn/problems/pacific-atlantic-water-flow/
 * 9. LeetCode 695. 岛屿的最大面积 - https://leetcode.cn/problems/max-area-of-island/
 * 10. LeetCode 733. 图像渲染 - https://leetcode.cn/problems/flood-fill/
 * 11. LeetCode 463. 岛屿的周长 - https://leetcode.cn/problems/island-perimeter/
 * 12. LeetCode 694. 不同岛屿的数量 - https://leetcode.cn/problems/number-of-distinct-islands/
 * 13. LeetCode 695. 岛屿的最大面积 - https://leetcode.cn/problems/max-area-of-island/
 * 14. LeetCode 827. 最大人工岛 - https://leetcode.cn/problems/making-a-large-island/
 * 15. LeetCode 1020. 飞地的数量 - https://leetcode.cn/problems/number-of-enclaves/
 * 16. LeetCode 1219. 黄金矿工 - https://leetcode.cn/problems/path-with-maximum-gold/
 * 17. LeetCode 1254. 统计封闭岛屿的数目 - https://leetcode.cn/problems/number-of-closed-islands/
 * 18. LeetCode 1905. 统计子岛屿 - https://leetcode.cn/problems/count-sub-islands/
 * 19. LeetCode 2101. 引爆最多的炸弹 - https://leetcode.cn/problems/detonate-the-maximum-bombs/
 * 20. LeetCode 2258. 逃离火灾 - https://leetcode.cn/problems/escape-the-spreading-fire/
 * 21. LeetCode 2309. 兼具大小写的最好英文字母 - https://leetcode.cn/problems/greatest-english-letter-in-upper-and-lower-case/
 * 22. LeetCode 2336. 无限集中的最小数字 - https://leetcode.cn/problems/smallest-number-in-infinite-set/
 * 23. LeetCode 2359. 找到离给定两个节点最近的节点 - https://leetcode.cn/problems/find-closest-node-to-given-two-nodes/
 * 24. LeetCode 2468. 根据限制划分消息 - https://leetcode.cn/problems/split-message-based-on-limit/
 * 25. LeetCode 2471. 逐层排序二叉树所需的最少操作数目 - https://leetcode.cn/problems/minimum-number-of-operations-to-sort-a-binary-tree-by-level/
 * 26. 洛谷 P1101 单词方阵 - https://www.luogu.com.cn/problem/P1101
 * 27. 洛谷 P1141 01迷宫 - https://www.luogu.com.cn/problem/P1141
 * 28. 洛谷 P1331 海战 - https://www.luogu.com.cn/problem/P1331
 * 29. 洛谷 P1331 海战 - https://www.luogu.com.cn/problem/P1331
 * 30. 洛谷 P1620 [NOIP2002 普及组] 均分纸牌 - https://www.luogu.com.cn/problem/P1620
 */
public class Code08_LongestIncreasingPathInAMatrix {
    
    // 四个方向：上、右、下、左
    private static final int[][] dirs = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};
    
    /**
     * 方法一：记忆化搜索
     * 使用深度优先搜索结合缓存来找出最长递增路径
     * 
     * @param matrix 输入的整数矩阵
     * @return 最长递增路径的长度
     */
    public static int longestIncreasingPath1(int[][] matrix) {
        // 边界检查
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return 0;
        }
        
        int rows = matrix.length;
        int cols = matrix[0].length;
        // 缓存每个单元格的最长递增路径长度
        int[][] memo = new int[rows][cols];
        int maxLength = 0;
        
        // 对每个单元格进行深度优先搜索
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                maxLength = Math.max(maxLength, dfs(matrix, i, j, memo));
            }
        }
        
        return maxLength;
    }
    
    /**
     * 深度优先搜索函数
     * 
     * @param matrix 输入的整数矩阵
     * @param i 当前单元格的行索引
     * @param j 当前单元格的列索引
     * @param memo 缓存数组
     * @return 从当前单元格出发的最长递增路径长度
     */
    private static int dfs(int[][] matrix, int i, int j, int[][] memo) {
        // 如果已经计算过，直接返回缓存的结果
        if (memo[i][j] != 0) {
            return memo[i][j];
        }
        
        int rows = matrix.length;
        int cols = matrix[0].length;
        int maxLength = 1; // 至少包含当前单元格
        
        // 探索四个方向
        for (int[] dir : dirs) {
            int ni = i + dir[0];
            int nj = j + dir[1];
            
            // 检查新位置是否有效且值更大
            if (ni >= 0 && ni < rows && nj >= 0 && nj < cols && matrix[ni][nj] > matrix[i][j]) {
                // 递归计算从新位置出发的最长路径，并加1（包含当前单元格）
                maxLength = Math.max(maxLength, 1 + dfs(matrix, ni, nj, memo));
            }
        }
        
        // 缓存结果
        memo[i][j] = maxLength;
        return maxLength;
    }
    
    /**
     * 方法二：拓扑排序
     * 构建有向无环图并使用拓扑排序找出最长路径
     * 
     * @param matrix 输入的整数矩阵
     * @return 最长递增路径的长度
     */
    public static int longestIncreasingPath2(int[][] matrix) {
        // 边界检查
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return 0;
        }
        
        int rows = matrix.length;
        int cols = matrix[0].length;
        int totalCells = rows * cols;
        
        // 构建邻接表表示的图
        ArrayList<ArrayList<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < totalCells; i++) {
            graph.add(new ArrayList<>());
        }
        
        // 计算每个节点的入度
        int[] indegree = new int[totalCells];
        
        // 构建图并计算入度
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int currValue = matrix[i][j];
                int currIndex = i * cols + j;
                
                // 探索四个方向
                for (int[] dir : dirs) {
                    int ni = i + dir[0];
                    int nj = j + dir[1];
                    
                    // 检查新位置是否有效且值更大
                    if (ni >= 0 && ni < rows && nj >= 0 && nj < cols && matrix[ni][nj] > currValue) {
                        int neighborIndex = ni * cols + nj;
                        // 添加边：当前节点 -> 邻居节点（值更大的节点）
                        graph.get(currIndex).add(neighborIndex);
                        // 邻居节点的入度加1
                        indegree[neighborIndex]++;
                    }
                }
            }
        }
        
        // 初始化队列，将所有入度为0的节点加入队列
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < totalCells; i++) {
            if (indegree[i] == 0) {
                queue.offer(i);
            }
        }
        
        // 记录每个节点的最长路径长度（初始化为1，因为每个节点自身是一条长度为1的路径）
        int[] pathLength = new int[totalCells];
        for (int i = 0; i < totalCells; i++) {
            pathLength[i] = 1;
        }
        
        // 最长路径长度
        int maxLength = 1;
        
        // 拓扑排序
        while (!queue.isEmpty()) {
            int curr = queue.poll();
            
            // 处理所有邻居节点
            for (int neighbor : graph.get(curr)) {
                // 更新邻居节点的最长路径长度
                pathLength[neighbor] = Math.max(pathLength[neighbor], pathLength[curr] + 1);
                maxLength = Math.max(maxLength, pathLength[neighbor]);
                
                // 入度减1
                indegree[neighbor]--;
                // 如果入度变为0，加入队列
                if (indegree[neighbor] == 0) {
                    queue.offer(neighbor);
                }
            }
        }
        
        return maxLength;
    }
    
    /**
     * 测试函数
     */
    public static void main(String[] args) {
        // 测试用例1
        int[][] matrix1 = {
            {9, 9, 4},
            {6, 6, 8},
            {2, 1, 1}
        };
        System.out.println("测试用例1（记忆化搜索）: " + longestIncreasingPath1(matrix1)); // 应输出 4
        System.out.println("测试用例1（拓扑排序）: " + longestIncreasingPath2(matrix1)); // 应输出 4
        
        // 测试用例2
        int[][] matrix2 = {
            {3, 4, 5},
            {3, 2, 6},
            {2, 2, 1}
        };
        System.out.println("测试用例2（记忆化搜索）: " + longestIncreasingPath1(matrix2)); // 应输出 4
        System.out.println("测试用例2（拓扑排序）: " + longestIncreasingPath2(matrix2)); // 应输出 4
        
        // 测试用例3：单元素矩阵
        int[][] matrix3 = {{1}};
        System.out.println("测试用例3（记忆化搜索）: " + longestIncreasingPath1(matrix3)); // 应输出 1
        System.out.println("测试用例3（拓扑排序）: " + longestIncreasingPath2(matrix3)); // 应输出 1
    }
}