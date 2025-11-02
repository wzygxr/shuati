package class058;

import java.util.*;

/**
 * HackerRank - Connected Cells in a Grid
 * 题目链接: https://www.hackerrank.com/challenges/connected-cell-in-a-grid/problem
 * 
 * 题目描述:
 * 给定一个m×n的矩阵，其中1表示填充单元格，0表示空白单元格。
 * 找到矩阵中最大的连通区域（由相邻的1组成，相邻包括上下左右和对角线方向）。
 * 
 * 解题思路:
 * 使用Flood Fill算法（8连通）遍历整个矩阵，计算每个连通区域的大小，并记录最大值。
 * 
 * 时间复杂度: O(m*n) - 每个单元格最多被访问一次
 * 空间复杂度: O(m*n) - 递归栈深度或队列空间
 * 是否最优解: 是
 * 
 * 工程化考量:
 * 1. 异常处理：检查输入是否为空
 * 2. 边界条件：处理单元素矩阵
 * 3. 8连通：支持对角线方向的连接
 * 
 * 语言特性差异:
 * Java: 递归实现简洁，有自动内存管理
 * C++: 可以选择递归或使用栈手动实现
 * Python: 递归实现简洁，但有递归深度限制
 * 
 * 极端输入场景:
 * 1. 空矩阵：返回0
 * 2. 全0矩阵：返回0
 * 3. 全1矩阵：返回m*n
 * 4. 大规模矩阵：使用BFS避免栈溢出
 */
public class Code12_HackerRank_ConnectedCells {
    
    // 八个方向的偏移量：上下左右和四个对角线方向
    private static final int[] dx = {-1, -1, -1, 0, 0, 1, 1, 1};
    private static final int[] dy = {-1, 0, 1, -1, 1, -1, 0, 1};
    
    /**
     * 计算最大连通区域大小
     * 
     * @param matrix 二维矩阵，1表示填充单元格，0表示空白
     * @return 最大连通区域的大小
     * 
     * 算法步骤:
     * 1. 遍历矩阵中的每个单元格
     * 2. 当遇到未访问的1时，使用DFS/BFS计算连通区域大小
     * 3. 更新最大连通区域大小
     * 4. 返回最大值
     */
    public static int connectedCell(List<List<Integer>> matrix) {
        // 边界条件检查
        if (matrix == null || matrix.size() == 0 || matrix.get(0).size() == 0) {
            return 0;
        }
        
        int rows = matrix.size();
        int cols = matrix.get(0).size();
        int maxRegion = 0;
        
        // 创建访问标记数组
        boolean[][] visited = new boolean[rows][cols];
        
        // 遍历整个矩阵
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                // 如果当前单元格是1且未访问
                if (matrix.get(i).get(j) == 1 && !visited[i][j]) {
                    int regionSize = dfs(matrix, i, j, visited, rows, cols);
                    maxRegion = Math.max(maxRegion, regionSize);
                }
            }
        }
        
        return maxRegion;
    }
    
    /**
     * 深度优先搜索计算连通区域大小
     * 
     * @param matrix 矩阵
     * @param x 当前行坐标
     * @param y 当前列坐标
     * @param visited 访问标记数组
     * @param rows 行数
     * @param cols 列数
     * @return 当前连通区域的大小
     */
    private static int dfs(List<List<Integer>> matrix, int x, int y, 
                          boolean[][] visited, int rows, int cols) {
        // 边界条件检查
        if (x < 0 || x >= rows || y < 0 || y >= cols || 
            matrix.get(x).get(y) == 0 || visited[x][y]) {
            return 0;
        }
        
        // 标记为已访问
        visited[x][y] = true;
        int size = 1;
        
        // 递归处理八个方向的相邻单元格
        for (int i = 0; i < 8; i++) {
            int newX = x + dx[i];
            int newY = y + dy[i];
            size += dfs(matrix, newX, newY, visited, rows, cols);
        }
        
        return size;
    }
    
    /**
     * 广度优先搜索版本（避免递归深度问题）
     * 
     * @param matrix 矩阵
     * @return 最大连通区域大小
     */
    public static int connectedCellBFS(List<List<Integer>> matrix) {
        if (matrix == null || matrix.size() == 0 || matrix.get(0).size() == 0) {
            return 0;
        }
        
        int rows = matrix.size();
        int cols = matrix.get(0).size();
        int maxRegion = 0;
        boolean[][] visited = new boolean[rows][cols];
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (matrix.get(i).get(j) == 1 && !visited[i][j]) {
                    int regionSize = bfs(matrix, i, j, visited, rows, cols);
                    maxRegion = Math.max(maxRegion, regionSize);
                }
            }
        }
        
        return maxRegion;
    }
    
    /**
     * 广度优先搜索实现
     */
    private static int bfs(List<List<Integer>> matrix, int x, int y, 
                          boolean[][] visited, int rows, int cols) {
        java.util.Queue<int[]> queue = new java.util.LinkedList<>();
        queue.offer(new int[]{x, y});
        visited[x][y] = true;
        int size = 0;
        
        while (!queue.isEmpty()) {
            int[] cell = queue.poll();
            int i = cell[0], j = cell[1];
            size++;
            
            for (int k = 0; k < 8; k++) {
                int ni = i + dx[k];
                int nj = j + dy[k];
                
                if (ni >= 0 && ni < rows && nj >= 0 && nj < cols && 
                    matrix.get(ni).get(nj) == 1 && !visited[ni][nj]) {
                    visited[ni][nj] = true;
                    queue.offer(new int[]{ni, nj});
                }
            }
        }
        
        return size;
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试用例1：标准网格
        List<List<Integer>> matrix1 = Arrays.asList(
            Arrays.asList(1, 1, 0, 0),
            Arrays.asList(0, 1, 1, 0),
            Arrays.asList(0, 0, 1, 0),
            Arrays.asList(1, 0, 0, 0)
        );
        
        System.out.println("测试用例1 - 标准网格:");
        System.out.println("矩阵:");
        printMatrix(matrix1);
        System.out.println("DFS版本最大连通区域大小: " + connectedCell(matrix1));
        
        List<List<Integer>> matrix1Copy = copyMatrix(matrix1);
        System.out.println("BFS版本最大连通区域大小: " + connectedCellBFS(matrix1Copy));
        
        // 测试用例2：全1网格
        List<List<Integer>> matrix2 = Arrays.asList(
            Arrays.asList(1, 1),
            Arrays.asList(1, 1)
        );
        
        System.out.println("\n测试用例2 - 全1网格:");
        System.out.println("矩阵:");
        printMatrix(matrix2);
        System.out.println("最大连通区域大小: " + connectedCell(matrix2));
        
        // 测试用例3：全0网格
        List<List<Integer>> matrix3 = Arrays.asList(
            Arrays.asList(0, 0),
            Arrays.asList(0, 0)
        );
        
        System.out.println("\n测试用例3 - 全0网格:");
        System.out.println("矩阵:");
        printMatrix(matrix3);
        System.out.println("最大连通区域大小: " + connectedCell(matrix3));
    }
    
    // 辅助方法：打印矩阵
    private static void printMatrix(List<List<Integer>> matrix) {
        for (List<Integer> row : matrix) {
            for (int cell : row) {
                System.out.print(cell + " ");
            }
            System.out.println();
        }
    }
    
    // 辅助方法：复制矩阵
    private static List<List<Integer>> copyMatrix(List<List<Integer>> matrix) {
        List<List<Integer>> copy = new ArrayList<>();
        for (List<Integer> row : matrix) {
            copy.add(new ArrayList<>(row));
        }
        return copy;
    }
}