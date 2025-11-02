#include <iostream>
#include <vector>
#include <queue>
#include <utility>
using namespace std;

// 岛屿数量
// 给你一个由 '1'（陆地）和 '0'（水）组成的的二维网格，请你计算网格中岛屿的数量。
// 岛屿总是被水包围，并且每座岛屿只能由水平方向和/或竖直方向上相邻的陆地连接形成。
// 此外，你可以假设该网格的四条边均被水包围。
// 测试链接 : https://leetcode.cn/problems/number-of-islands/
// 
// 算法思路：
// 使用BFS进行岛屿的遍历和标记。遍历整个网格，当遇到未访问的陆地('1')时，
// 启动BFS遍历整个岛屿，并将所有相连的陆地标记为已访问。
// 岛屿数量就是启动BFS的次数。
// 
// 时间复杂度：O(m * n)，其中m和n分别是网格的行数和列数，每个单元格最多被访问一次
// 空间复杂度：O(min(m, n))，BFS队列的最大大小取决于网格的较小维度
// 
// 工程化考量：
// 1. 使用pair表示坐标，提高代码可读性
// 2. 使用方向数组简化移动逻辑
// 3. 边界检查确保数组访问安全
// 4. 使用引用避免不必要的拷贝
class Solution {
public:
    int numIslands(vector<vector<char>>& grid) {
        if (grid.empty() || grid[0].empty()) {
            return 0;
        }
        
        int m = grid.size();
        int n = grid[0].size();
        int islandCount = 0;
        
        // 方向数组：上、右、下、左
        vector<pair<int, int>> directions = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};
        
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                // 找到未访问的陆地
                if (grid[i][j] == '1') {
                    islandCount++;
                    bfs(grid, i, j, m, n, directions);
                }
            }
        }
        
        return islandCount;
    }
    
private:
    void bfs(vector<vector<char>>& grid, int startX, int startY, int m, int n, 
             const vector<pair<int, int>>& directions) {
        queue<pair<int, int>> q;
        q.push({startX, startY});
        grid[startX][startY] = '0'; // 标记为已访问
        
        while (!q.empty()) {
            auto [x, y] = q.front();
            q.pop();
            
            // 向四个方向扩展
            for (const auto& dir : directions) {
                int nx = x + dir.first;
                int ny = y + dir.second;
                
                // 检查边界和是否为未访问的陆地
                if (nx >= 0 && nx < m && ny >= 0 && ny < n && grid[nx][ny] == '1') {
                    grid[nx][ny] = '0'; // 标记为已访问
                    q.push({nx, ny});
                }
            }
        }
    }
};

// 单元测试
int main() {
    Solution solution;
    
    // 测试用例1：标准情况
    vector<vector<char>> grid1 = {
        {'1','1','1','1','0'},
        {'1','1','0','1','0'},
        {'1','1','0','0','0'},
        {'0','0','0','0','0'}
    };
    cout << "测试用例1 - 岛屿数量: " << solution.numIslands(grid1) << endl; // 期望输出: 1
    
    // 测试用例2：多个岛屿
    vector<vector<char>> grid2 = {
        {'1','1','0','0','0'},
        {'1','1','0','0','0'},
        {'0','0','1','0','0'},
        {'0','0','0','1','1'}
    };
    cout << "测试用例2 - 岛屿数量: " << solution.numIslands(grid2) << endl; // 期望输出: 3
    
    // 测试用例3：空网格
    vector<vector<char>> grid3;
    cout << "测试用例3 - 岛屿数量: " << solution.numIslands(grid3) << endl; // 期望输出: 0
    
    // 测试用例4：全为水
    vector<vector<char>> grid4 = {
        {'0','0','0'},
        {'0','0','0'},
        {'0','0','0'}
    };
    cout << "测试用例4 - 岛屿数量: " << solution.numIslands(grid4) << endl; // 期望输出: 0
    
    // 测试用例5：全为陆地
    vector<vector<char>> grid5 = {
        {'1','1','1'},
        {'1','1','1'},
        {'1','1','1'}
    };
    cout << "测试用例5 - 岛屿数量: " << solution.numIslands(grid5) << endl; // 期望输出: 1
    
    return 0;
}