package class185.game_of_life_problems;

import java.util.*;

/**
 * 康威生命游戏实现 (Java版本)
 * 
 * 算法思路：
 * 康威生命游戏是一个细胞自动机，每个细胞根据其周围8个邻居的状态按照以下规则演化：
 * 1. 活细胞周围活细胞数少于2个，则死亡（孤独）
 * 2. 活细胞周围活细胞数为2或3个，则继续存活
 * 3. 活细胞周围活细胞数多于3个，则死亡（拥挤）
 * 4. 死细胞周围活细胞数为3个，则复活（繁殖）
 * 
 * 时间复杂度：O(m*n) 每代
 * 空间复杂度：O(m*n)
 * 
 * 应用场景：
 * 1. 生物学：细胞自动机模型
 * 2. 物理学：粒子系统模拟
 * 3. 艺术：生成艺术图案
 * 4. 教育：复杂系统教学
 * 
 * 相关题目：
 * 1. LeetCode 289. 生命游戏
 */
public class GameOfLife2D {
    private int[][] board;
    private int rows;
    private int cols;
    
    /**
     * 构造函数
     * @param initialBoard 初始棋盘状态，二维数组，1表示活细胞，0表示死细胞
     */
    public GameOfLife2D(int[][] initialBoard) {
        if (initialBoard == null || initialBoard.length == 0 || initialBoard[0].length == 0) {
            throw new IllegalArgumentException("初始棋盘不能为空");
        }
        
        this.rows = initialBoard.length;
        this.cols = initialBoard[0].length;
        this.board = new int[rows][cols];
        
        // 深拷贝初始棋盘
        for (int i = 0; i < rows; i++) {
            System.arraycopy(initialBoard[i], 0, this.board[i], 0, cols);
        }
    }
    
    /**
     * 计算下一代的状态（使用额外空间）
     * 时间复杂度：O(m*n)
     * 空间复杂度：O(m*n)
     */
    public void nextGeneration() {
        // 创建新的棋盘来存储下一代状态
        int[][] newBoard = new int[rows][cols];
        
        // 遍历每个细胞
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                // 计算周围活细胞数量
                int liveNeighbors = countLiveNeighbors(i, j);
                
                // 应用生命游戏规则
                if (board[i][j] == 1) {
                    // 活细胞
                    if (liveNeighbors < 2 || liveNeighbors > 3) {
                        newBoard[i][j] = 0;  // 死亡
                    } else {
                        newBoard[i][j] = 1;  // 存活
                    }
                } else {
                    // 死细胞
                    if (liveNeighbors == 3) {
                        newBoard[i][j] = 1;  // 繁殖
                    } else {
                        newBoard[i][j] = 0;  // 保持死亡
                    }
                }
            }
        }
        
        // 更新棋盘
        board = newBoard;
    }
    
    /**
     * 使用原地算法计算下一代状态
     * 时间复杂度：O(m*n)
     * 空间复杂度：O(1)
     * 使用特殊标记：2表示从活到死，-1表示从死到活
     */
    public void nextGenerationInPlace() {
        // 遍历每个细胞
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                // 计算周围活细胞数量
                int liveNeighbors = countLiveNeighborsWithMarkers(i, j);
                
                // 应用生命游戏规则
                if (board[i][j] == 1) {
                    // 活细胞
                    if (liveNeighbors < 2 || liveNeighbors > 3) {
                        board[i][j] = 2;  // 标记为从活到死
                    }
                    // 否则保持为1，继续存活
                } else {
                    // 死细胞
                    if (liveNeighbors == 3) {
                        board[i][j] = -1;  // 标记为从死到活
                    }
                    // 否则保持为0，继续死亡
                }
            }
        }
        
        // 解析标记，恢复真实状态
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (board[i][j] == 2) {
                    board[i][j] = 0;  // 死亡
                } else if (board[i][j] == -1) {
                    board[i][j] = 1;  // 新生
                }
            }
        }
    }
    
    /**
     * 计算指定位置周围的活细胞数量
     * @param row 行索引
     * @param col 列索引
     * @return 活细胞数量
     */
    private int countLiveNeighbors(int row, int col) {
        int count = 0;
        // 8个方向的偏移
        int[][] directions = {
            {-1, -1}, {-1, 0}, {-1, 1},
            {0, -1},           {0, 1},
            {1, -1},  {1, 0}, {1, 1}
        };
        
        for (int[] dir : directions) {
            int newRow = row + dir[0];
            int newCol = col + dir[1];
            
            // 检查边界并计算活细胞
            if (newRow >= 0 && newRow < rows && newCol >= 0 && newCol < cols) {
                if (board[newRow][newCol] == 1) {
                    count++;
                }
            }
        }
        
        return count;
    }
    
    /**
     * 在原地算法中计算周围的活细胞数量（考虑标记）
     * @param row 行索引
     * @param col 列索引
     * @return 活细胞数量
     */
    private int countLiveNeighborsWithMarkers(int row, int col) {
        int count = 0;
        int[][] directions = {
            {-1, -1}, {-1, 0}, {-1, 1},
            {0, -1},           {0, 1},
            {1, -1},  {1, 0}, {1, 1}
        };
        
        for (int[] dir : directions) {
            int newRow = row + dir[0];
            int newCol = col + dir[1];
            
            if (newRow >= 0 && newRow < rows && newCol >= 0 && newCol < cols) {
                // 1或2表示之前是活细胞
                if (board[newRow][newCol] == 1 || board[newRow][newCol] == 2) {
                    count++;
                }
            }
        }
        
        return count;
    }
    
    /**
     * 获取当前棋盘状态
     * @return 棋盘状态的深拷贝
     */
    public int[][] getBoard() {
        int[][] copy = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            System.arraycopy(board[i], 0, copy[i], 0, cols);
        }
        return copy;
    }
    
    /**
     * 打印棋盘状态
     */
    public void printBoard() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.print(board[i][j] == 1 ? "█ " : "· ");
            }
            System.out.println();
        }
        System.out.println();
    }
    
    /**
     * 模拟多代生命游戏
     * @param generations 模拟的代数
     * @param inPlace 是否使用原地算法
     */
    public void simulate(int generations, boolean inPlace) {
        if (generations <= 0) {
            return;
        }
        
        for (int i = 0; i < generations; i++) {
            if (inPlace) {
                nextGenerationInPlace();
            } else {
                nextGeneration();
            }
        }
    }
    
    /**
     * 测试生命游戏
     */
    public static void main(String[] args) {
        System.out.println("=== 测试康威生命游戏 ===");
        
        // 测试用例：闪烁器（Blinker）
        int[][] blinker = {
            {0, 1, 0},
            {0, 1, 0},
            {0, 1, 0}
        };
        
        System.out.println("闪烁器 - 初始状态:");
        GameOfLife2D game1 = new GameOfLife2D(blinker);
        game1.printBoard();
        
        System.out.println("第1代（原地算法）:");
        game1.nextGenerationInPlace();
        game1.printBoard();
        
        System.out.println("第2代（原地算法）:");
        game1.nextGenerationInPlace();
        game1.printBoard();
        
        // 测试用例：滑翔机（Glider）
        int[][] glider = {
            {0, 1, 0},
            {0, 0, 1},
            {1, 1, 1}
        };
        
        System.out.println("滑翔机 - 初始状态:");
        GameOfLife2D game2 = new GameOfLife2D(glider);
        game2.printBoard();
        
        for (int i = 1; i <= 4; i++) {
            System.out.println("滑翔机 - 第" + i + "代:");
            game2.nextGeneration();
            game2.printBoard();
        }
        
        // 测试用例：高斯帕机枪（Gosper Glider Gun）的一部分
        System.out.println("高斯帕机枪 - 初始状态:");
        int[][] gosperGliderGun = new int[10][38];
        // 添加高斯帕机枪的图案
        // 第一部分
        gosperGliderGun[5][1] = 1;
        gosperGliderGun[6][1] = 1;
        gosperGliderGun[5][2] = 1;
        gosperGliderGun[6][2] = 1;
        
        // 第二部分
        gosperGliderGun[5][11] = 1;
        gosperGliderGun[6][11] = 1;
        gosperGliderGun[7][11] = 1;
        gosperGliderGun[4][12] = 1;
        gosperGliderGun[8][12] = 1;
        gosperGliderGun[3][13] = 1;
        gosperGliderGun[9][13] = 1;
        gosperGliderGun[3][14] = 1;
        gosperGliderGun[9][14] = 1;
        gosperGliderGun[6][15] = 1;
        gosperGliderGun[4][16] = 1;
        gosperGliderGun[8][16] = 1;
        gosperGliderGun[5][17] = 1;
        gosperGliderGun[6][17] = 1;
        gosperGliderGun[7][17] = 1;
        gosperGliderGun[6][18] = 1;
        
        // 第三部分
        gosperGliderGun[3][21] = 1;
        gosperGliderGun[4][21] = 1;
        gosperGliderGun[5][21] = 1;
        gosperGliderGun[3][22] = 1;
        gosperGliderGun[4][22] = 1;
        gosperGliderGun[5][22] = 1;
        gosperGliderGun[2][23] = 1;
        gosperGliderGun[6][23] = 1;
        gosperGliderGun[1][25] = 1;
        gosperGliderGun[2][25] = 1;
        gosperGliderGun[6][25] = 1;
        gosperGliderGun[7][25] = 1;
        
        // 第四部分
        gosperGliderGun[3][35] = 1;
        gosperGliderGun[4][35] = 1;
        gosperGliderGun[3][36] = 1;
        gosperGliderGun[4][36] = 1;
        
        GameOfLife2D game3 = new GameOfLife2D(gosperGliderGun);
        System.out.println("高斯帕机枪 - 前几代:");
        for (int i = 1; i <= 2; i++) {
            System.out.println("第" + i + "代:");
            game3.nextGeneration();
            game3.printBoard();
        }
    }
}