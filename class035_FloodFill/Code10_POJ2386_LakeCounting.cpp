#include <vector>
#include <iostream>
using namespace std;

/**
 * POJ 2386 - Lake Counting (湖泊计数)
 * 题目链接: http://poj.org/problem?id=2386
 * 
 * 题目描述:
 * 由于最近的暴雨，Farmer John的田地里出现了水坑。由于田地的地形不同，FJ的田地可以用N×M的网格来表示。
 * 每个格子包含水('W')或旱地('.')。一个水坑是由相邻的格子连接而成的（水平、垂直或对角线方向）。
 * 你的任务是确定网格中包含多少个水坑。
 * 
 * 解题思路:
 * 这是一个经典的Flood Fill问题。我们需要：
 * 1. 遍历整个网格
 * 2. 当遇到一个未访问的'W'时，使用Flood Fill算法标记整个连通的水坑
 * 3. 计数器加1
 * 4. 重复直到遍历完整个网格
 * 
 * 与LeetCode 200题的不同之处在于，这里使用8连通而不是4连通。
 * 
 * 时间复杂度: O(N*M) - 最坏情况下需要访问所有格子
 * 空间复杂度: O(N*M) - 递归栈深度
 * 是否最优解: 是
 * 
 * 工程化考量:
 * 1. 异常处理：检查输入是否为空
 * 2. 边界条件：处理空网格
 * 3. 可配置性：可以支持4连通和8连通切换
 * 
 * 语言特性差异:
 * Java: 递归实现简洁，有自动内存管理
 * C++: 可以选择递归或使用栈手动实现，需要手动管理内存
 * Python: 递归实现简洁，但有递归深度限制
 * 
 * 极端输入场景:
 * 1. 空网格
 * 2. 全为'W'的网格
 * 3. 全为'.'的网格
 * 4. 交替的'W'和'.'模式
 * 
 * 性能优化:
 * 1. 提前判断边界条件
 * 2. 使用方向数组简化代码
 * 3. 原地修改避免额外空间
 * 
 * 与其他算法的联系:
 * 1. DFS/BFS: 核心算法
 * 2. 图论: 连通分量问题
 * 3. 并查集: 另一种解决连通性问题的方法
 */
class Solution {
private:
    // 八个方向的偏移量：上下左右和四个对角线方向
    const int dx[8] = {-1, -1, -1, 0, 0, 1, 1, 1};
    const int dy[8] = {-1, 0, 1, -1, 1, -1, 0, 1};
    
public:
    /**
     * 计算湖泊数量
     * 
     * @param grid 网格矩阵，'W'表示水，'.'表示旱地
     * @return 湖泊数量
     */
    int lakeCounting(vector<vector<char>>& grid) {
        // 边界条件检查
        if (grid.empty() || grid[0].empty()) {
            return 0;
        }
        
        int rows = grid.size();
        int cols = grid[0].size();
        int count = 0;
        
        // 遍历整个网格
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                // 如果遇到未访问的水坑
                if (grid[i][j] == 'W') {
                    // 使用Flood Fill标记整个水坑
                    dfs(grid, i, j, rows, cols);
                    count++;
                }
            }
        }
        
        return count;
    }
    
    /**
     * 深度优先搜索标记连通的水坑
     * 
     * @param grid 网格矩阵
     * @param x 当前行坐标
     * @param y 当前列坐标
     * @param rows 行数
     * @param cols 列数
     */
    void dfs(vector<vector<char>>& grid, int x, int y, int rows, int cols) {
        // 边界检查和值检查
        if (x < 0 || x >= rows || y < 0 || y >= cols || grid[x][y] != 'W') {
            return;
        }
        
        // 标记当前格子已访问
        grid[x][y] = '.';  // 将'W'改为'.'表示已访问
        
        // 递归处理八个方向的相邻格子
        for (int i = 0; i < 8; i++) {
            int newX = x + dx[i];
            int newY = y + dy[i];
            dfs(grid, newX, newY, rows, cols);
        }
    }
};

// 测试方法
void printGrid(const vector<vector<char>>& grid) {
    for (const auto& row : grid) {
        for (char cell : row) {
            cout << cell << " ";
        }
        cout << endl;
    }
}

int main() {
    Solution solution;
    
    // 测试用例1
    vector<vector<char>> grid1 = {
        {'W', '.', '.', '.', '.', '.', '.', '.', '.', 'W'},
        {'.', 'W', 'W', '.', '.', '.', '.', '.', '.', '.'},
        {'.', '.', '.', '.', '.', '.', '.', '.', 'W', '.'},
        {'.', '.', '.', '.', '.', '.', '.', '.', '.', '.'},
        {'.', '.', '.', '.', '.', '.', '.', '.', '.', '.'},
        {'.', '.', '.', '.', '.', '.', '.', '.', '.', '.'},
        {'.', '.', '.', '.', '.', '.', '.', '.', '.', '.'},
        {'.', '.', '.', '.', '.', '.', '.', '.', '.', '.'},
        {'.', '.', '.', '.', '.', '.', '.', '.', '.', '.'},
        {'.', '.', '.', '.', '.', '.', '.', '.', '.', 'W'}
    };
    
    cout << "测试用例1:" << endl;
    cout << "网格:" << endl;
    printGrid(grid1);
    cout << "湖泊数量: " << solution.lakeCounting(grid1) << endl;
    
    // 测试用例2
    vector<vector<char>> grid2 = {
        {'W', 'W', 'W'},
        {'W', 'W', 'W'},
        {'W', 'W', 'W'}
    };
    
    cout << "\n测试用例2:" << endl;
    cout << "网格:" << endl;
    printGrid(grid2);
    cout << "湖泊数量: " << solution.lakeCounting(grid2) << endl;
    
    return 0;
}