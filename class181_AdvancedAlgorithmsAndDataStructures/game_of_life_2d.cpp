#include <iostream>
#include <vector>
#include <algorithm>
#include <cmath>
#include <stdexcept>
#include <memory>
#include <limits>
#include <random>
#include <chrono>

using namespace std;

/**
 * 康威生命游戏实现 (C++版本)
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

class GameOfLife2D {
private:
    vector<vector<int>> board;
    int rows;
    int cols;
    
    /**
     * 计算指定位置周围的活细胞数量
     * @param row 行索引
     * @param col 列索引
     * @return 活细胞数量
     */
    int countLiveNeighbors(int row, int col) const {
        int count = 0;
        // 8个方向的偏移
        vector<pair<int, int>> directions = {
            {-1, -1}, {-1, 0}, {-1, 1},
            {0, -1},           {0, 1},
            {1, -1},  {1, 0}, {1, 1}
        };
        
        for (const auto& dir : directions) {
            int newRow = row + dir.first;
            int newCol = col + dir.second;
            
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
    int countLiveNeighborsWithMarkers(int row, int col) const {
        int count = 0;
        vector<pair<int, int>> directions = {
            {-1, -1}, {-1, 0}, {-1, 1},
            {0, -1},           {0, 1},
            {1, -1},  {1, 0}, {1, 1}
        };
        
        for (const auto& dir : directions) {
            int newRow = row + dir.first;
            int newCol = col + dir.second;
            
            if (newRow >= 0 && newRow < rows && newCol >= 0 && newCol < cols) {
                // 1或2表示之前是活细胞
                if (board[newRow][newCol] == 1 || board[newRow][newCol] == 2) {
                    count++;
                }
            }
        }
        
        return count;
    }
    
public:
    /**
     * 构造函数
     * @param initialBoard 初始棋盘状态，二维数组，1表示活细胞，0表示死细胞
     */
    GameOfLife2D(const vector<vector<int>>& initialBoard) {
        if (initialBoard.empty() || initialBoard[0].empty()) {
            throw invalid_argument("初始棋盘不能为空");
        }
        
        this->rows = initialBoard.size();
        this->cols = initialBoard[0].size();
        this->board = initialBoard;
    }
    
    /**
     * 计算下一代的状态（使用额外空间）
     * 时间复杂度：O(m*n)
     * 空间复杂度：O(m*n)
     */
    void nextGeneration() {
        // 创建新的棋盘来存储下一代状态
        vector<vector<int>> newBoard(rows, vector<int>(cols, 0));
        
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
    void nextGenerationInPlace() {
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
     * 获取当前棋盘状态
     * @return 棋盘状态的深拷贝
     */
    vector<vector<int>> getBoard() const {
        return board;
    }
    
    /**
     * 打印棋盘状态
     */
    void printBoard() const {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                cout << (board[i][j] == 1 ? "█ " : "· ");
            }
            cout << endl;
        }
        cout << endl;
    }
    
    /**
     * 模拟多代生命游戏
     * @param generations 模拟的代数
     * @param inPlace 是否使用原地算法
     */
    void simulate(int generations, bool inPlace = true) {
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
};

/**
 * 测试生命游戏
 */
void testGameOfLife() {
    cout << "=== 测试康威生命游戏 ===" << endl;
    
    // 测试用例：闪烁器（Blinker）
    vector<vector<int>> blinker = {
        {0, 1, 0},
        {0, 1, 0},
        {0, 1, 0}
    };
    
    cout << "闪烁器 - 初始状态:" << endl;
    GameOfLife2D game1(blinker);
    game1.printBoard();
    
    cout << "第1代（原地算法）:" << endl;
    game1.nextGenerationInPlace();
    game1.printBoard();
    
    cout << "第2代（原地算法）:" << endl;
    game1.nextGenerationInPlace();
    game1.printBoard();
    
    // 测试用例：滑翔机（Glider）
    vector<vector<int>> glider = {
        {0, 1, 0},
        {0, 0, 1},
        {1, 1, 1}
    };
    
    cout << "滑翔机 - 初始状态:" << endl;
    GameOfLife2D game2(glider);
    game2.printBoard();
    
    for (int i = 1; i <= 4; i++) {
        cout << "滑翔机 - 第" << i << "代:" << endl;
        game2.nextGeneration();
        game2.printBoard();
    }
    
    // 测试用例：高斯帕机枪（Gosper Glider Gun）的一部分
    cout << "高斯帕机枪 - 初始状态:" << endl;
    vector<vector<int>> gosperGliderGun(10, vector<int>(38, 0));
    
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
    
    GameOfLife2D game3(gosperGliderGun);
    cout << "高斯帕机枪 - 前几代:" << endl;
    for (int i = 1; i <= 2; i++) {
        cout << "第" << i << "代:" << endl;
        game3.nextGeneration();
        game3.printBoard();
    }
}

int main() {
    testGameOfLife();
    return 0;
}