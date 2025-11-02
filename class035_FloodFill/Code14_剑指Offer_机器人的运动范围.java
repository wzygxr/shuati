package class058;

import java.util.*;

/**
 * 剑指Offer - 机器人的运动范围
 * 题目链接: https://leetcode-cn.com/problems/ji-qi-ren-de-yun-dong-fan-wei-lcof/
 * 
 * 题目描述:
 * 地上有一个m行n列的方格，从坐标[0,0]到坐标[m-1,n-1]。
 * 一个机器人从坐标[0,0]的格子开始移动，它每次可以向左、右、上、下移动一格（不能移动到方格外），
 * 也不能进入行坐标和列坐标的数位之和大于k的格子。
 * 例如，当k为18时，机器人能够进入方格[35,37]，因为3+5+3+7=18。
 * 但它不能进入方格[35,38]，因为3+5+3+8=19。
 * 请问该机器人能够到达多少个格子？
 * 
 * 解题思路:
 * 使用Flood Fill算法（BFS或DFS）从起点开始探索可达的格子。
 * 需要满足两个条件：
 * 1. 格子坐标在网格范围内
 * 2. 坐标数位之和不大于k
 * 
 * 时间复杂度: O(m*n) - 最坏情况下需要访问所有格子
 * 空间复杂度: O(m*n) - 访问标记数组和队列空间
 * 是否最优解: 是
 * 
 * 工程化考量:
 * 1. 数位和计算：高效计算坐标的数位和
 * 2. 访问标记：避免重复访问
 * 3. 边界条件：处理k为负数的情况
 */
public class Code14_剑指Offer_机器人的运动范围 {
    
    // 四个方向的偏移量：上、下、左、右
    private static final int[] dx = {-1, 1, 0, 0};
    private static final int[] dy = {0, 0, -1, 1};
    
    /**
     * 计算机器人能够到达的格子数量
     * 
     * @param m 网格行数
     * @param n 网格列数
     * @param k 数位和阈值
     * @return 可达的格子数量
     */
    public static int movingCount(int m, int n, int k) {
        // 边界条件检查
        if (m <= 0 || n <= 0 || k < 0) {
            return 0;
        }
        
        // 访问标记数组
        boolean[][] visited = new boolean[m][n];
        int count = 0;
        
        // 使用BFS进行探索
        java.util.Queue<int[]> queue = new java.util.LinkedList<>();
        queue.offer(new int[]{0, 0});
        visited[0][0] = true;
        count++;
        
        while (!queue.isEmpty()) {
            int[] cell = queue.poll();
            int x = cell[0], y = cell[1];
            
            // 探索四个方向
            for (int i = 0; i < 4; i++) {
                int nx = x + dx[i];
                int ny = y + dy[i];
                
                // 检查新坐标是否可达
                if (nx >= 0 && nx < m && ny >= 0 && ny < n && 
                    !visited[nx][ny] && getDigitSum(nx, ny) <= k) {
                    visited[nx][ny] = true;
                    count++;
                    queue.offer(new int[]{nx, ny});
                }
            }
        }
        
        return count;
    }
    
    /**
     * 计算坐标的数位和
     * 
     * @param x 行坐标
     * @param y 列坐标
     * @return 数位和
     */
    private static int getDigitSum(int x, int y) {
        int sum = 0;
        
        // 计算x的数位和
        while (x > 0) {
            sum += x % 10;
            x /= 10;
        }
        
        // 计算y的数位和
        while (y > 0) {
            sum += y % 10;
            y /= 10;
        }
        
        return sum;
    }
    
    /**
     * 深度优先搜索版本
     */
    public static int movingCountDFS(int m, int n, int k) {
        if (m <= 0 || n <= 0 || k < 0) {
            return 0;
        }
        
        boolean[][] visited = new boolean[m][n];
        return dfs(0, 0, m, n, k, visited);
    }
    
    private static int dfs(int x, int y, int m, int n, int k, boolean[][] visited) {
        // 边界条件和访问检查
        if (x < 0 || x >= m || y < 0 || y >= n || visited[x][y] || getDigitSum(x, y) > k) {
            return 0;
        }
        
        visited[x][y] = true;
        int count = 1;
        
        // 递归探索四个方向
        for (int i = 0; i < 4; i++) {
            int nx = x + dx[i];
            int ny = y + dy[i];
            count += dfs(nx, ny, m, n, k, visited);
        }
        
        return count;
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试用例1：标准情况
        System.out.println("测试用例1 - 标准情况:");
        System.out.println("网格: 3x3, k=1");
        System.out.println("BFS版本可达格子数: " + movingCount(3, 3, 1));
        System.out.println("DFS版本可达格子数: " + movingCountDFS(3, 3, 1));
        
        // 测试用例2：较大网格
        System.out.println("\n测试用例2 - 较大网格:");
        System.out.println("网格: 10x10, k=5");
        System.out.println("BFS版本可达格子数: " + movingCount(10, 10, 5));
        
        // 测试用例3：k=0
        System.out.println("\n测试用例3 - k=0:");
        System.out.println("网格: 2x2, k=0");
        System.out.println("可达格子数: " + movingCount(2, 2, 0));
        
        // 测试用例4：边界情况
        System.out.println("\n测试用例4 - 边界情况:");
        System.out.println("网格: 1x1, k=0");
        System.out.println("可达格子数: " + movingCount(1, 1, 0));
    }
}