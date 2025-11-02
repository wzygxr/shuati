#include <vector>
#include <iostream>
using namespace std;

/**
 * 边框着色 (Coloring A Border)
 * 给你一个大小为 m x n 的整数矩阵 grid ，表示一个网格。另给你三个整数 row、col 和 color 。
 * 两相同颜色的相邻像素称为一个连通分量。连通分量的边界是指连通分量中满足以下条件之一的所有像素：
 * 1. 在上、下、左、右任意一个方向上与不属于同一连通分量的网格块相邻
 * 2. 在网格的边界上（第一行/列或最后一行/列）
 * 请你使用指定颜色 color 为所有包含 grid[row][col] 的连通分量的边界进行着色，并返回最终的网格 grid 。
 * 
 * 测试链接: https://leetcode.cn/problems/coloring-a-border/
 * 
 * 解题思路:
 * 1. 首先使用Flood Fill算法找到包含起始点(row, col)的整个连通分量
 * 2. 在遍历过程中判断每个像素是否为边界像素
 * 3. 将边界像素着色为指定颜色
 * 
 * 判断边界像素的条件:
 * 1. 像素在网格边界上（第一行/列或最后一行/列）
 * 2. 像素在四个方向上至少有一个相邻像素不属于同一连通分量
 * 
 * 时间复杂度: O(m*n) - 最坏情况下需要访问所有格子
 * 空间复杂度: O(m*n) - 递归栈深度和辅助数组空间
 * 是否最优解: 是
 * 
 * 工程化考量:
 * 1. 异常处理：检查输入是否为空，坐标是否越界
 * 2. 特殊情况：如果新颜色与原颜色相同，则直接返回原图像
 * 3. 可配置性：可以扩展支持8个方向的连接
 * 
 * 语言特性差异:
 * Java: 递归实现简洁，有自动内存管理
 * C++: 可以选择递归或使用栈手动实现，需要手动管理内存
 * Python: 递归实现简洁，但有递归深度限制
 * 
 * 极端输入场景:
 * 1. 空网格
 * 2. 单像素网格
 * 3. 所有像素颜色相同
 * 4. 新颜色与原颜色相同
 * 5. 大规模网格（可能导致栈溢出）
 * 
 * 性能优化:
 * 1. 提前判断边界条件
 * 2. 使用方向数组简化代码
 * 3. 根据数据规模选择DFS或BFS
 * 
 * 与其他算法的联系:
 * 1. DFS/BFS: 核心算法
 * 2. 图论: 连通分量问题
 * 3. 几何: 边界判断
 */
class Solution {
private:
    // 四个方向的偏移量：上、下、左、右
    const int dx[4] = {-1, 1, 0, 0};
    const int dy[4] = {0, 0, -1, 1};
    
public:
    /**
     * 边框着色主函数
     * 
     * @param grid 网格矩阵
     * @param row 起始行坐标
     * @param col 起始列坐标
     * @param color 新颜色值
     * @return 着色后的网格
     */
    vector<vector<int>> colorBorder(vector<vector<int>>& grid, int row, int col, int color) {
        // 边界条件检查
        if (grid.empty() || grid[0].empty()) {
            return grid;
        }
        
        int rows = grid.size();
        int cols = grid[0].size();
        
        // 检查坐标是否越界
        if (row < 0 || row >= rows || col < 0 || col >= cols) {
            return grid;
        }
        
        int originalColor = grid[row][col];
        
        // 如果新颜色与原颜色相同，直接返回原图像
        if (originalColor == color) {
            return grid;
        }
        
        // 使用辅助数组标记访问状态和边界
        vector<vector<bool>> visited(rows, vector<bool>(cols, false));
        vector<vector<bool>> isBorder(rows, vector<bool>(cols, false));
        
        // 执行DFS找到连通分量并标记边界
        dfs(grid, row, col, originalColor, visited, isBorder, rows, cols);
        
        // 对边界进行着色
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (isBorder[i][j]) {
                    grid[i][j] = color;
                }
            }
        }
        
        return grid;
    }
    
    /**
     * 深度优先搜索找到连通分量并标记边界
     * 
     * @param grid 网格矩阵
     * @param x 当前行坐标
     * @param y 当前列坐标
     * @param originalColor 原始颜色
     * @param visited 访问标记数组
     * @param isBorder 边界标记数组
     * @param rows 行数
     * @param cols 列数
     */
    void dfs(vector<vector<int>>& grid, int x, int y, int originalColor,
             vector<vector<bool>>& visited, vector<vector<bool>>& isBorder,
             int rows, int cols) {
        // 边界检查和颜色检查
        if (x < 0 || x >= rows || y < 0 || y >= cols || grid[x][y] != originalColor || visited[x][y]) {
            return;
        }
        
        // 标记为已访问
        visited[x][y] = true;
        
        // 判断是否为边界像素
        bool border = false;
        
        // 条件1: 像素在网格边界上
        if (x == 0 || x == rows - 1 || y == 0 || y == cols - 1) {
            border = true;
        }
        
        // 条件2: 四个方向上至少有一个相邻像素不属于同一连通分量
        if (!border) {
            for (int i = 0; i < 4; i++) {
                int newX = x + dx[i];
                int newY = y + dy[i];
                // 如果相邻像素颜色不同或者是未访问的相同颜色像素（说明不属于同一连通分量）
                if (newX >= 0 && newX < rows && newY >= 0 && newY < cols) {
                    if (grid[newX][newY] != originalColor && !visited[newX][newY]) {
                        border = true;
                        break;
                    }
                }
            }
        }
        
        // 标记为边界像素
        if (border) {
            isBorder[x][y] = true;
        }
        
        // 递归处理四个方向的相邻像素
        for (int i = 0; i < 4; i++) {
            int newX = x + dx[i];
            int newY = y + dy[i];
            dfs(grid, newX, newY, originalColor, visited, isBorder, rows, cols);
        }
    }
};

// 测试方法
void printGrid(const vector<vector<int>>& grid) {
    for (const auto& row : grid) {
        for (int cell : row) {
            cout << cell << " ";
        }
        cout << endl;
    }
}

int main() {
    Solution solution;
    
    // 测试用例1
    vector<vector<int>> grid1 = {{1,1},{1,2}};
    cout << "测试用例1:" << endl;
    cout << "原网格:" << endl;
    printGrid(grid1);
    solution.colorBorder(grid1, 0, 0, 3);
    cout << "着色后:" << endl;
    printGrid(grid1);
    
    // 测试用例2
    vector<vector<int>> grid2 = {{1,2,2},{2,3,2}};
    cout << "\n测试用例2:" << endl;
    cout << "原网格:" << endl;
    printGrid(grid2);
    solution.colorBorder(grid2, 0, 1, 3);
    cout << "着色后:" << endl;
    printGrid(grid2);
    
    return 0;
}