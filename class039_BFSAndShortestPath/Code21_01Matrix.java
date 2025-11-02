package class062;

import java.util.*;

// 01矩阵
// 给定一个由 0 和 1 组成的矩阵 mat ，请输出一个大小相同的矩阵，其中每一个格子是 mat 中对应位置元素到最近的 0 的距离。
// 两个相邻元素间的距离为 1 。
// 测试链接 : https://leetcode.cn/problems/01-matrix/
// 
// 算法思路：
// 使用多源BFS，从所有的0开始同时进行BFS搜索。这样每个1第一次被访问时就是到最近0的距离。
// 这种方法比从每个1开始单独BFS要高效得多。
// 
// 时间复杂度：O(m * n)，其中m和n分别是矩阵的行数和列数，每个单元格最多被访问一次
// 空间复杂度：O(m * n)，用于存储队列和结果矩阵
// 
// 工程化考量：
// 1. 多源BFS：从所有0开始同时搜索，避免重复计算
// 2. 原地修改：使用结果矩阵同时记录距离和访问状态
// 3. 边界检查：确保移动后的位置在矩阵范围内
// 4. 性能优化：使用数组队列避免对象创建开销
public class Code21_01Matrix {

    // 四个方向的移动：上、右、下、左
    private static final int[][] DIRECTIONS = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};
    
    public static int[][] updateMatrix(int[][] mat) {
        if (mat == null || mat.length == 0 || mat[0].length == 0) {
            return new int[0][0];
        }
        
        int m = mat.length;
        int n = mat[0].length;
        int[][] result = new int[m][n];
        
        // 使用队列进行多源BFS
        Queue<int[]> queue = new LinkedList<>();
        
        // 初始化：将所有0加入队列，1的位置初始化为-1（表示未访问）
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (mat[i][j] == 0) {
                    queue.offer(new int[]{i, j});
                    result[i][j] = 0;
                } else {
                    result[i][j] = -1; // 标记为未访问
                }
            }
        }
        
        // 多源BFS
        int distance = 0;
        while (!queue.isEmpty()) {
            distance++;
            int size = queue.size();
            
            // 处理当前距离的所有点
            for (int i = 0; i < size; i++) {
                int[] current = queue.poll();
                int x = current[0];
                int y = current[1];
                
                // 向四个方向扩展
                for (int[] dir : DIRECTIONS) {
                    int nx = x + dir[0];
                    int ny = y + dir[1];
                    
                    // 检查边界和是否为未访问的1
                    if (nx >= 0 && nx < m && ny >= 0 && ny < n && result[nx][ny] == -1) {
                        result[nx][ny] = distance;
                        queue.offer(new int[]{nx, ny});
                    }
                }
            }
        }
        
        return result;
    }
    
    // 优化版本：使用数组模拟队列，避免对象创建开销
    public static int[][] updateMatrixOptimized(int[][] mat) {
        if (mat == null || mat.length == 0 || mat[0].length == 0) {
            return new int[0][0];
        }
        
        int m = mat.length;
        int n = mat[0].length;
        int[][] result = new int[m][n];
        
        // 使用数组模拟队列
        int[][] queue = new int[m * n][2];
        int front = 0, rear = 0;
        
        // 初始化：将所有0加入队列，1的位置初始化为-1
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (mat[i][j] == 0) {
                    queue[rear][0] = i;
                    queue[rear][1] = j;
                    rear++;
                    result[i][j] = 0;
                } else {
                    result[i][j] = -1;
                }
            }
        }
        
        // 多源BFS
        int distance = 0;
        while (front < rear) {
            distance++;
            int size = rear - front;
            
            // 处理当前距离的所有点
            for (int i = 0; i < size; i++) {
                int x = queue[front][0];
                int y = queue[front][1];
                front++;
                
                // 向四个方向扩展
                for (int d = 0; d < 4; d++) {
                    int nx = x + DIRECTIONS[d][0];
                    int ny = y + DIRECTIONS[d][1];
                    
                    // 检查边界和是否为未访问的1
                    if (nx >= 0 && nx < m && ny >= 0 && ny < n && result[nx][ny] == -1) {
                        result[nx][ny] = distance;
                        queue[rear][0] = nx;
                        queue[rear][1] = ny;
                        rear++;
                    }
                }
            }
        }
        
        return result;
    }
    
    // 单元测试
    public static void main(String[] args) {
        // 测试用例1：标准情况
        int[][] mat1 = {
            {0, 0, 0},
            {0, 1, 0},
            {0, 0, 0}
        };
        int[][] result1 = updateMatrix(mat1);
        System.out.println("测试用例1结果:");
        printMatrix(result1);
        
        // 测试用例2：复杂情况
        int[][] mat2 = {
            {0, 0, 0},
            {0, 1, 0},
            {1, 1, 1}
        };
        int[][] result2 = updateMatrix(mat2);
        System.out.println("测试用例2结果:");
        printMatrix(result2);
        
        // 测试用例3：全为0
        int[][] mat3 = {
            {0, 0},
            {0, 0}
        };
        int[][] result3 = updateMatrix(mat3);
        System.out.println("测试用例3结果:");
        printMatrix(result3);
        
        // 测试用例4：全为1
        int[][] mat4 = {
            {1, 1},
            {1, 1}
        };
        int[][] result4 = updateMatrix(mat4);
        System.out.println("测试用例4结果:");
        printMatrix(result4);
    }
    
    private static void printMatrix(int[][] matrix) {
        for (int[] row : matrix) {
            for (int num : row) {
                System.out.print(num + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
}