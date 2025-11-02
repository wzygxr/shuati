package class047;

import java.util.LinkedList;
import java.util.Queue;

/**
 * LeetCode 286. 墙与门 (Walls and Gates)
 * 
 * 题目描述:
 * 你被给定一个 m x n 的二维网格 rooms。
 * 网格中的每个元素有以下三种可能的值：
 * -1 表示墙壁或障碍物
 * 0 表示一扇门
 * INF 表示一个空房间。我们使用值 2^31 - 1 = 2147483647 来表示 INF
 * 
 * 你需要填充每个空房间，使其表示到最近的门的距离。如果不可能到达门（比如周围都是墙壁），保持 INF 不变。
 * 
 * 示例:
 * 输入：
 * [[2147483647,-1,0,2147483647],
 *  [2147483647,2147483647,2147483647,-1],
 *  [2147483647,-1,2147483647,-1],
 *  [0,-1,2147483647,2147483647]]
 * 输出：
 * [[3,-1,0,1],
 *  [2,2,1,-1],
 *  [1,-1,2,-1],
 *  [0,-1,3,4]]
 * 
 * 提示:
 * m 和 n 的值在 [1, 250] 范围内。
 * rooms[i][j] 的值只能是 -1、0 或 2^31 - 1。
 * 
 * 题目链接: https://leetcode.com/problems/walls-and-gates/
 * 
 * 解题思路:
 * 这个问题可以使用广度优先搜索（BFS）来解决：
 * 1. 首先，将所有的门（值为0的单元格）加入队列
 * 2. 然后从每个门开始，进行广度优先搜索
 * 3. 每次搜索时，更新相邻的空房间（值为INF的单元格）的距离为当前距离+1
 * 4. 继续搜索直到队列为空
 * 
 * 这种方法的优点是：
 * - BFS能够保证第一次到达某个单元格时，得到的是到最近门的最短距离
 * - 从所有门同时开始BFS，可以避免重复计算
 * 
 * 时间复杂度: O(m*n)，其中 m 和 n 分别是网格的行数和列数。
 * 每个单元格最多被访问一次，因为一旦被访问，它的值就会被更新为一个非INF值。
 * 
 * 空间复杂度: O(m*n)，队列在最坏情况下可能需要存储所有单元格。
 * 
 * 这是最优解，因为我们需要至少遍历每个单元格一次，时间复杂度无法更低。
 * BFS是解决最短路径问题的标准方法，特别适合于这种多源最短路径问题。
 */
public class Code15_WallsAndGates {
    
    // 表示空房间的常量
    private static final int INF = Integer.MAX_VALUE;
    // 表示墙壁的常量
    private static final int WALL = -1;
    // 表示门的常量
    private static final int GATE = 0;
    
    /**
     * 填充每个空房间到最近门的距离
     * 
     * @param rooms 二维网格，表示房间、墙壁和门
     */
    public static void wallsAndGates(int[][] rooms) {
        // 参数校验
        if (rooms == null || rooms.length == 0 || rooms[0].length == 0) {
            return;
        }
        
        int rows = rooms.length;
        int cols = rooms[0].length;
        
        // 创建队列，用于BFS
        Queue<int[]> queue = new LinkedList<>();
        
        // 首先将所有的门（值为0的单元格）加入队列
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (rooms[i][j] == GATE) {
                    queue.offer(new int[] {i, j});
                }
            }
        }
        
        // 定义四个方向：上、右、下、左
        int[][] directions = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};
        
        // BFS过程
        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int row = current[0];
            int col = current[1];
            int distance = rooms[row][col];
            
            // 探索四个方向的相邻单元格
            for (int[] dir : directions) {
                int newRow = row + dir[0];
                int newCol = col + dir[1];
                
                // 检查新位置是否有效，并且是一个空房间（值为INF）
                if (newRow >= 0 && newRow < rows && newCol >= 0 && newCol < cols && rooms[newRow][newCol] == INF) {
                    // 更新距离
                    rooms[newRow][newCol] = distance + 1;
                    // 将新位置加入队列
                    queue.offer(new int[] {newRow, newCol});
                }
            }
        }
    }
    
    /**
     * 另一种实现方式，使用更简洁的代码
     * 
     * @param rooms 二维网格，表示房间、墙壁和门
     */
    public static void wallsAndGatesAlternative(int[][] rooms) {
        if (rooms == null || rooms.length == 0) {
            return;
        }
        
        int m = rooms.length;
        int n = rooms[0].length;
        Queue<int[]> queue = new LinkedList<>();
        
        // 添加所有门到队列
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (rooms[i][j] == GATE) {
                    queue.offer(new int[]{i, j});
                }
            }
        }
        
        // BFS
        while (!queue.isEmpty()) {
            int[] pos = queue.poll();
            int row = pos[0];
            int col = pos[1];
            
            // 上
            if (row > 0 && rooms[row - 1][col] == INF) {
                rooms[row - 1][col] = rooms[row][col] + 1;
                queue.offer(new int[]{row - 1, col});
            }
            // 右
            if (col < n - 1 && rooms[row][col + 1] == INF) {
                rooms[row][col + 1] = rooms[row][col] + 1;
                queue.offer(new int[]{row, col + 1});
            }
            // 下
            if (row < m - 1 && rooms[row + 1][col] == INF) {
                rooms[row + 1][col] = rooms[row][col] + 1;
                queue.offer(new int[]{row + 1, col});
            }
            // 左
            if (col > 0 && rooms[row][col - 1] == INF) {
                rooms[row][col - 1] = rooms[row][col] + 1;
                queue.offer(new int[]{row, col - 1});
            }
        }
    }
    
    /**
     * 打印二维数组
     * 
     * @param matrix 要打印的二维数组
     */
    public static void printMatrix(int[][] matrix) {
        for (int[] row : matrix) {
            System.out.print("[");
            for (int j = 0; j < row.length; j++) {
                // 为了美观，处理INF的显示
                if (row[j] == INF) {
                    System.out.print("INF");
                } else {
                    System.out.print(row[j]);
                }
                if (j < row.length - 1) {
                    System.out.print(", ");
                }
            }
            System.out.println("]");
        }
        System.out.println();
    }
    
    /**
     * 测试用例
     */
    public static void main(String[] args) {
        // 测试用例1
        int[][] rooms1 = {
            {INF, WALL, GATE, INF},
            {INF, INF, INF, WALL},
            {INF, WALL, INF, WALL},
            {GATE, WALL, INF, INF}
        };
        
        System.out.println("测试用例1 - 原始矩阵:");
        printMatrix(rooms1);
        
        wallsAndGates(rooms1);
        
        System.out.println("测试用例1 - 处理后矩阵:");
        printMatrix(rooms1);
        
        // 测试用例2
        int[][] rooms2 = {
            {INF, WALL, GATE, INF},
            {INF, INF, INF, WALL},
            {INF, WALL, INF, WALL},
            {GATE, WALL, INF, INF}
        };
        
        System.out.println("测试用例2 - 使用替代方法:");
        printMatrix(rooms2);
        
        wallsAndGatesAlternative(rooms2);
        
        System.out.println("测试用例2 - 处理后矩阵:");
        printMatrix(rooms2);
        
        // 测试用例3 - 边界情况：只有门和墙
        int[][] rooms3 = {
            {GATE, WALL},
            {WALL, GATE}
        };
        
        System.out.println("测试用例3 - 原始矩阵:");
        printMatrix(rooms3);
        
        wallsAndGates(rooms3);
        
        System.out.println("测试用例3 - 处理后矩阵:");
        printMatrix(rooms3);
        
        // 测试用例4 - 边界情况：只有一个单元格
        int[][] rooms4 = {{INF}};
        
        System.out.println("测试用例4 - 原始矩阵:");
        printMatrix(rooms4);
        
        wallsAndGates(rooms4);
        
        System.out.println("测试用例4 - 处理后矩阵:");
        printMatrix(rooms4);
        
        // 性能测试
        System.out.println("性能测试:");
        int[][] largeRooms = new int[100][100];
        // 初始化所有单元格为INF
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                largeRooms[i][j] = INF;
            }
        }
        // 添加一些门
        for (int i = 0; i < 10; i++) {
            largeRooms[i * 10][i * 10] = GATE;
        }
        // 添加一些墙
        for (int i = 0; i < 100; i += 5) {
            for (int j = 0; j < 100; j += 5) {
                largeRooms[i][j] = WALL;
            }
        }
        
        long startTime = System.currentTimeMillis();
        wallsAndGates(largeRooms);
        long endTime = System.currentTimeMillis();
        
        System.out.println("大矩阵处理耗时: " + (endTime - startTime) + "ms");
        System.out.println("大矩阵处理结果示例（左上角10x10）:");
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                if (largeRooms[i][j] == INF) {
                    System.out.print("INF	");
                } else if (largeRooms[i][j] == WALL) {
                    System.out.print("WALL	");
                } else {
                    System.out.print(largeRooms[i][j] + "	");
                }
            }
            System.out.println();
        }
    }
}