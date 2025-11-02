// LeetCode 1102. Path With Maximum Minimum Value
// 题目链接: https://leetcode.cn/problems/path-with-maximum-minimum-value/
// 
// 题目描述:
// 给你一个由正整数组成的二维网格grid，你需要找到一条从左上角(0,0)到右下角(m-1,n-1)的路径，使得路径上所有数字中的最小值尽可能大。
// 路径可以向四个方向移动：上、下、左、右。
//
// 解题思路:
// 这是一个典型的最大-最小路径问题，可以使用以下几种方法解决：
// 1. 二分答案 + BFS/DFS：二分搜索可能的最小值，然后检查是否存在一条路径上的所有值都不小于该值
// 2. 并查集：将所有点按照值从大到小排序，然后逐步合并相邻的点，直到起点和终点连通
// 3. 最大堆（贪心）：总是选择当前可到达的最大最小值的路径
//
// 这里我们使用最大堆的方法，这类似于最小生成树中的Kruskal算法的思想
//
// 时间复杂度: O(m*n log(m*n))，其中m和n分别是网格的行数和列数
// 空间复杂度: O(m*n)
// 是否为最优解: 是，这是解决此类问题的有效方法之一

#include <iostream>
#include <vector>
#include <queue>
using namespace std;

// 定义最大堆的元素结构
struct Cell {
    int value;
    int x;
    int y;
    Cell(int val, int x_coord, int y_coord) : value(val), x(x_coord), y(y_coord) {}
    // 为了使用优先队列作为最大堆，我们需要重载比较运算符
    bool operator<(const Cell& other) const {
        return value < other.value; // 这样优先队列会按value从大到小排列
    }
};

int maximumMinimumPath(vector<vector<int>>& grid) {
    if (grid.empty() || grid[0].empty()) {
        return 0;
    }
    
    int m = grid.size();
    int n = grid[0].size();
    // 四个方向：下、右、上、左
    vector<pair<int, int>> directions = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}};
    // 记录已访问的点
    vector<vector<bool>> visited(m, vector<bool>(n, false));
    // 最大堆
    priority_queue<Cell> maxHeap;
    
    maxHeap.emplace(grid[0][0], 0, 0);
    visited[0][0] = true;
    
    // 结果初始化为起点的值
    int result = grid[0][0];
    
    while (!maxHeap.empty()) {
        Cell current = maxHeap.top();
        maxHeap.pop();
        
        // 更新结果为路径上的最小值
        if (current.value < result) {
            result = current.value;
        }
        
        // 如果到达终点，返回结果
        if (current.x == m - 1 && current.y == n - 1) {
            return result;
        }
        
        // 探索四个方向
        for (auto& dir : directions) {
            int nx = current.x + dir.first;
            int ny = current.y + dir.second;
            // 检查边界和是否已访问
            if (nx >= 0 && nx < m && ny >= 0 && ny < n && !visited[nx][ny]) {
                visited[nx][ny] = true;
                maxHeap.emplace(grid[nx][ny], nx, ny);
            }
        }
    }
    
    return -1; // 理论上不会到达这里
}

// 并查集实现
class UnionFind {
private:
    vector<int> parent;
public:
    UnionFind(int size) {
        parent.resize(size);
        for (int i = 0; i < size; i++) {
            parent[i] = i;
        }
    }
    
    int find(int x) {
        if (parent[x] != x) {
            parent[x] = find(parent[x]); // 路径压缩
        }
        return parent[x];
    }
    
    void unite(int x, int y) {
        int fx = find(x);
        int fy = find(y);
        if (fx != fy) {
            parent[fy] = fx;
        }
    }
};

int maximumMinimumPath_uf(vector<vector<int>>& grid) {
    if (grid.empty() || grid[0].empty()) {
        return 0;
    }
    
    int m = grid.size();
    int n = grid[0].size();
    int totalCells = m * n;
    UnionFind uf(totalCells);
    
    // 将所有单元格按照值从大到小排序
    // 使用优先队列作为最大堆
    priority_queue<Cell> cells;
    for (int i = 0; i < m; i++) {
        for (int j = 0; j < n; j++) {
            cells.emplace(grid[i][j], i, j);
        }
    }
    
    // 四个方向
    vector<pair<int, int>> directions = {{1, 0}, {0, 1}, {-1, 0}, {0, -1}};
    // 记录已访问的单元格
    vector<vector<bool>> visited(m, vector<bool>(n, false));
    
    int start = 0;  // (0,0)
    int end = m * n - 1;  // (m-1, n-1)
    
    while (!cells.empty()) {
        Cell current = cells.top();
        cells.pop();
        int val = current.value;
        int x = current.x;
        int y = current.y;
        visited[x][y] = true;
        
        // 检查四个方向的邻居
        for (auto& dir : directions) {
            int nx = x + dir.first;
            int ny = y + dir.second;
            if (nx >= 0 && nx < m && ny >= 0 && ny < n && visited[nx][ny]) {
                // 合并当前单元格和已访问的邻居
                uf.unite(x * n + y, nx * n + ny);
            }
        }
        
        // 检查起点和终点是否连通
        if (uf.find(start) == uf.find(end)) {
            return val;
        }
    }
    
    return -1;
}

// 测试函数
void test() {
    // 测试用例1
    vector<vector<int>> grid1 = {{5, 4, 5}, {1, 2, 6}, {7, 4, 6}};
    cout << "Test 1 (max heap): " << maximumMinimumPath(grid1) << endl;  // 预期输出: 4
    cout << "Test 1 (union find): " << maximumMinimumPath_uf(grid1) << endl;  // 预期输出: 4
    
    // 测试用例2
    vector<vector<int>> grid2 = {{2, 2, 1, 2, 2, 2}, {1, 2, 2, 2, 1, 2}};
    cout << "Test 2 (max heap): " << maximumMinimumPath(grid2) << endl;  // 预期输出: 2
    cout << "Test 2 (union find): " << maximumMinimumPath_uf(grid2) << endl;  // 预期输出: 2
    
    // 测试用例3
    vector<vector<int>> grid3 = {{
        3, 4, 6, 3, 4},
        {0, 2, 1, 1, 7},
        {8, 8, 3, 2, 7},
        {3, 2, 4, 9, 8},
        {4, 1, 2, 0, 0},
        {4, 6, 5, 4, 3}
    };
    cout << "Test 3 (max heap): " << maximumMinimumPath(grid3) << endl;  // 预期输出: 3
    cout << "Test 3 (union find): " << maximumMinimumPath_uf(grid3) << endl;  // 预期输出: 3
}

int main() {
    test();
    return 0;
}