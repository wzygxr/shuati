#include <iostream>
#include <vector>
#include <queue>
#include <utility>
#include <climits>
using namespace std;

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
class Solution {
public:
    vector<vector<int>> updateMatrix(vector<vector<int>>& mat) {
        if (mat.empty() || mat[0].empty()) {
            return {};
        }
        
        int m = mat.size();
        int n = mat[0].size();
        vector<vector<int>> result(m, vector<int>(n, -1));
        
        // 方向数组：上、右、下、左
        vector<pair<int, int>> directions = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};
        queue<pair<int, int>> q;
        
        // 初始化：将所有0加入队列
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (mat[i][j] == 0) {
                    q.push({i, j});
                    result[i][j] = 0;
                }
            }
        }
        
        int distance = 0;
        
        while (!q.empty()) {
            distance++;
            int size = q.size();
            
            for (int i = 0; i < size; i++) {
                auto [x, y] = q.front();
                q.pop();
                
                for (auto& dir : directions) {
                    int nx = x + dir.first;
                    int ny = y + dir.second;
                    
                    // 检查边界和是否为未访问的1
                    if (nx >= 0 && nx < m && ny >= 0 && ny < n && result[nx][ny] == -1) {
                        result[nx][ny] = distance;
                        q.push({nx, ny});
                    }
                }
            }
        }
        
        return result;
    }
};

// 优化版本：使用数组模拟队列
class SolutionOptimized {
public:
    vector<vector<int>> updateMatrix(vector<vector<int>>& mat) {
        if (mat.empty() || mat[0].empty()) return {};
        
        int m = mat.size(), n = mat[0].size();
        vector<vector<int>> dist(m, vector<int>(n, INT_MAX));
        
        // 第一次遍历：从左上方到右下方
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (mat[i][j] == 0) {
                    dist[i][j] = 0;
                } else {
                    if (i > 0) dist[i][j] = min(dist[i][j], dist[i-1][j] + 1);
                    if (j > 0) dist[i][j] = min(dist[i][j], dist[i][j-1] + 1);
                }
            }
        }
        
        // 第二次遍历：从右下方到左上方
        for (int i = m-1; i >= 0; i--) {
            for (int j = n-1; j >= 0; j--) {
                if (mat[i][j] == 1) {
                    if (i < m-1) dist[i][j] = min(dist[i][j], dist[i+1][j] + 1);
                    if (j < n-1) dist[i][j] = min(dist[i][j], dist[i][j+1] + 1);
                }
            }
        }
        
        return dist;
    }
};

// 单元测试
void printMatrix(const vector<vector<int>>& matrix) {
    for (const auto& row : matrix) {
        for (int num : row) {
            cout << num << " ";
        }
        cout << endl;
    }
    cout << endl;
}

int main() {
    Solution solution;
    
    // 测试用例1：标准情况
    vector<vector<int>> mat1 = {
        {0, 0, 0},
        {0, 1, 0},
        {0, 0, 0}
    };
    auto result1 = solution.updateMatrix(mat1);
    cout << "测试用例1结果:" << endl;
    printMatrix(result1);
    
    // 测试用例2：复杂情况
    vector<vector<int>> mat2 = {
        {0, 0, 0},
        {0, 1, 0},
        {1, 1, 1}
    };
    auto result2 = solution.updateMatrix(mat2);
    cout << "测试用例2结果:" << endl;
    printMatrix(result2);
    
    // 测试用例3：全为0
    vector<vector<int>> mat3 = {
        {0, 0},
        {0, 0}
    };
    auto result3 = solution.updateMatrix(mat3);
    cout << "测试用例3结果:" << endl;
    printMatrix(result3);
    
    return 0;
}