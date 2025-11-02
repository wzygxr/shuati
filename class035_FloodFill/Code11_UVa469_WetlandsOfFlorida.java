package class058;

import java.util.*;

/**
 * UVa 469 - Wetlands of Florida (佛罗里达湿地)
 * 题目链接: https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=6&page=show_problem&problem=410
 * 
 * 题目描述:
 * 给定一个由'L'和'W'组成的网格，'L'表示陆地，'W'表示水域(湿地)。
 * 8个方向相连的'W'构成一个湿地。对于每个查询点，需要计算包含该点的湿地大小。
 * 
 * 解题思路:
 * 这是一个动态Flood Fill问题，需要对每个查询点计算其所在连通分量的大小。
 * 与POJ 2386的不同之处在于：
 * 1. 需要多次查询不同点的连通分量大小
 * 2. 查询点可能是陆地('L')，此时返回0
 * 3. 需要保持原始网格不变，不能原地修改
 * 
 * 解决方案：
 * 1. 对于每个查询点，使用Flood Fill算法计算连通分量大小
 * 2. 使用辅助访问数组避免重复访问
 * 3. 使用8连通判断相邻关系
 * 
 * 时间复杂度: O(Q*N*M) - Q为查询次数，每次查询最坏需要遍历整个网格
 * 空间复杂度: O(N*M) - 访问数组的空间
 * 是否最优解: 对于多次查询不是最优，可以使用预处理优化
 * 
 * 工程化考量:
 * 1. 异常处理：检查输入是否为空，坐标是否越界
 * 2. 边界条件：处理查询点为陆地的情况
 * 3. 可配置性：可以支持4连通和8连通切换
 * 
 * 语言特性差异:
 * Java: 对象引用和垃圾回收
 * C++: 指针操作和手动内存管理
 * Python: 动态类型和自动内存管理
 * 
 * 极端输入场景:
 * 1. 空网格
 * 2. 全为'W'的网格
 * 3. 全为'L'的网格
 * 4. 查询点在边界上
 * 
 * 性能优化:
 * 1. 预处理所有连通分量并编号，查询时直接返回
 * 2. 使用并查集优化多次查询
 * 3. 提前终止条件判断
 * 
 * 与其他算法的联系:
 * 1. DFS/BFS: 核心算法
 * 2. 并查集: 优化多次查询
 * 3. 图论: 连通分量问题
 */
public class Code11_UVa469_WetlandsOfFlorida {
    
    // 八个方向的偏移量：上下左右和四个对角线方向
    private static final int[] dx = {-1, -1, -1, 0, 0, 1, 1, 1};
    private static final int[] dy = {-1, 0, 1, -1, 1, -1, 0, 1};
    
    /**
     * 计算包含指定点的湿地大小
     * 
     * @param grid 网格矩阵，'L'表示陆地，'W'表示水域
     * @param row 查询点行坐标(1-based)
     * @param col 查询点列坐标(1-based)
     * @return 湿地大小
     */
    public static int wetlandSize(char[][] grid, int row, int col) {
        // 边界条件检查
        if (grid == null || grid.length == 0 || grid[0].length == 0) {
            return 0;
        }
        
        int rows = grid.length;
        int cols = grid[0].length;
        
        // 转换为0-based坐标
        int x = row - 1;
        int y = col - 1;
        
        // 检查坐标是否越界
        if (x < 0 || x >= rows || y < 0 || y >= cols) {
            return 0;
        }
        
        // 如果查询点是陆地，返回0
        if (grid[x][y] == 'L') {
            return 0;
        }
        
        // 使用辅助数组标记访问状态
        boolean[][] visited = new boolean[rows][cols];
        
        // 使用Flood Fill计算连通分量大小
        return dfs(grid, x, y, rows, cols, visited);
    }
    
    /**
     * 深度优先搜索计算连通分量大小
     * 
     * @param grid 网格矩阵
     * @param x 当前行坐标
     * @param y 当前列坐标
     * @param rows 行数
     * @param cols 列数
     * @param visited 访问标记数组
     * @return 连通分量大小
     */
    private static int dfs(char[][] grid, int x, int y, int rows, int cols, boolean[][] visited) {
        // 边界检查、值检查和访问检查
        if (x < 0 || x >= rows || y < 0 || y >= cols || 
            grid[x][y] != 'W' || visited[x][y]) {
            return 0;
        }
        
        // 标记为已访问
        visited[x][y] = true;
        
        // 计算当前格子的贡献（1）加上八个方向相邻格子的贡献
        int size = 1;
        for (int i = 0; i < 8; i++) {
            int newX = x + dx[i];
            int newY = y + dy[i];
            size += dfs(grid, newX, newY, rows, cols, visited);
        }
        
        return size;
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试用例1
        char[][] grid1 = {
            {'L', 'L', 'L', 'L', 'L', 'L', 'L', 'L', 'L', 'L'},
            {'L', 'W', 'W', 'W', 'L', 'L', 'W', 'W', 'W', 'L'},
            {'L', 'W', 'W', 'W', 'L', 'L', 'W', 'W', 'W', 'L'},
            {'L', 'W', 'W', 'W', 'L', 'L', 'W', 'W', 'W', 'L'},
            {'L', 'L', 'L', 'L', 'L', 'L', 'L', 'L', 'L', 'L'},
            {'L', 'W', 'W', 'W', 'L', 'L', 'W', 'W', 'W', 'L'},
            {'L', 'W', 'W', 'W', 'L', 'L', 'W', 'W', 'W', 'L'},
            {'L', 'W', 'W', 'W', 'L', 'L', 'W', 'W', 'W', 'L'},
            {'L', 'L', 'L', 'L', 'L', 'L', 'L', 'L', 'L', 'L'},
            {'L', 'W', 'W', 'W', 'L', 'L', 'W', 'W', 'W', 'L'}
        };
        
        System.out.println("测试用例1:");
        System.out.println("网格:");
        printGrid(grid1);
        System.out.println("查询点(2,2)的湿地大小: " + wetlandSize(grid1, 2, 2));
        System.out.println("查询点(5,5)的湿地大小: " + wetlandSize(grid1, 5, 5));
        System.out.println("查询点(1,1)的湿地大小: " + wetlandSize(grid1, 1, 1));
        
        // 测试用例2
        char[][] grid2 = {
            {'W', 'W', 'W'},
            {'W', 'L', 'W'},
            {'W', 'W', 'W'}
        };
        
        System.out.println("\n测试用例2:");
        System.out.println("网格:");
        printGrid(grid2);
        System.out.println("查询点(2,2)的湿地大小: " + wetlandSize(grid2, 2, 2));
        System.out.println("查询点(1,1)的湿地大小: " + wetlandSize(grid2, 1, 1));
    }
    
    // 辅助方法：打印网格
    private static void printGrid(char[][] grid) {
        for (char[] row : grid) {
            for (char cell : row) {
                System.out.print(cell + " ");
            }
            System.out.println();
        }
    }
}