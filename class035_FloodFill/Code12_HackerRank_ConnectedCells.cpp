#include <iostream>
#include <vector>
#include <queue>
#include <algorithm>

using namespace std;

/**
 * HackerRank - Connected Cells in a Grid (C++版本)
 * 题目链接: https://www.hackerrank.com/challenges/connected-cell-in-a-grid/problem
 * 
 * 解题思路:
 * 使用Flood Fill算法（8连通）遍历整个矩阵，计算每个连通区域的大小，并记录最大值。
 * 
 * 时间复杂度: O(m*n)
 * 空间复杂度: O(m*n)
 * 是否最优解: 是
 */
class Solution {
public:
    // 八个方向的偏移量
    vector<int> dx = {-1, -1, -1, 0, 0, 1, 1, 1};
    vector<int> dy = {-1, 0, 1, -1, 1, -1, 0, 1};
    
    int connectedCell(vector<vector<int>>& matrix) {
        if (matrix.empty() || matrix[0].empty()) return 0;
        
        int m = matrix.size();
        int n = matrix[0].size();
        int maxRegion = 0;
        vector<vector<bool>> visited(m, vector<bool>(n, false));
        
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (matrix[i][j] == 1 && !visited[i][j]) {
                    int regionSize = dfs(matrix, i, j, visited, m, n);
                    maxRegion = max(maxRegion, regionSize);
                }
            }
        }
        
        return maxRegion;
    }
    
private:
    int dfs(vector<vector<int>>& matrix, int x, int y, 
            vector<vector<bool>>& visited, int m, int n) {
        if (x < 0 || x >= m || y < 0 || y >= n || 
            matrix[x][y] == 0 || visited[x][y]) {
            return 0;
        }
        
        visited[x][y] = true;
        int size = 1;
        
        for (int i = 0; i < 8; i++) {
            int nx = x + dx[i];
            int ny = y + dy[i];
            size += dfs(matrix, nx, ny, visited, m, n);
        }
        
        return size;
    }
    
public:
    // BFS版本
    int connectedCellBFS(vector<vector<int>>& matrix) {
        if (matrix.empty() || matrix[0].empty()) return 0;
        
        int m = matrix.size();
        int n = matrix[0].size();
        int maxRegion = 0;
        vector<vector<bool>> visited(m, vector<bool>(n, false));
        
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (matrix[i][j] == 1 && !visited[i][j]) {
                    int regionSize = bfs(matrix, i, j, visited, m, n);
                    maxRegion = max(maxRegion, regionSize);
                }
            }
        }
        
        return maxRegion;
    }
    
private:
    int bfs(vector<vector<int>>& matrix, int x, int y, 
            vector<vector<bool>>& visited, int m, int n) {
        queue<pair<int, int>> q;
        q.push({x, y});
        visited[x][y] = true;
        int size = 0;
        
        while (!q.empty()) {
            auto [i, j] = q.front();
            q.pop();
            size++;
            
            for (int k = 0; k < 8; k++) {
                int ni = i + dx[k];
                int nj = j + dy[k];
                
                if (ni >= 0 && ni < m && nj >= 0 && nj < n && 
                    matrix[ni][nj] == 1 && !visited[ni][nj]) {
                    visited[ni][nj] = true;
                    q.push({ni, nj});
                }
            }
        }
        
        return size;
    }
};

// 测试函数
int main() {
    Solution solution;
    
    // 测试用例1
    vector<vector<int>> matrix1 = {
        {1, 1, 0, 0},
        {0, 1, 1, 0},
        {0, 0, 1, 0},
        {1, 0, 0, 0}
    };
    
    cout << "测试用例1 - 标准网格:" << endl;
    cout << "DFS版本最大连通区域大小: " << solution.connectedCell(matrix1) << endl;
    
    vector<vector<int>> matrix1_copy = matrix1;
    cout << "BFS版本最大连通区域大小: " << solution.connectedCellBFS(matrix1_copy) << endl;
    
    return 0;
}