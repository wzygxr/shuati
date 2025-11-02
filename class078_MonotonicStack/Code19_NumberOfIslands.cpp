#include <iostream>
#include <vector>
#include <queue>
#include <chrono>
#include <random>
#include <algorithm>
#include <utility>
using namespace std;

using namespace std;

/**
 * 岛屿数量 - C++实现
 * 
 * 题目描述：
 * 给你一个由 '1'（陆地）和 '0'（水）组成的二维网格，请你计算网格中岛屿的数量。
 * 岛屿总是被水包围，并且每座岛屿只能由水平方向和/或竖直方向上相邻的陆地连接形成。
 * 
 * 测试链接：https://leetcode.cn/problems/number-of-islands/
 * 题目来源：LeetCode
 * 难度：中等
 * 
 * 核心算法：深度优先搜索（DFS）或广度优先搜索（BFS）
 * 
 * 解题思路：
 * 1. 遍历网格中的每个单元格
 * 2. 当遇到陆地（'1'）时，进行DFS/BFS标记所有相连的陆地
 * 3. 每次完整的DFS/BFS标记过程计数为一个岛屿
 * 4. 继续遍历直到所有单元格都被访问
 * 
 * 时间复杂度分析：
 * O(m*n) - 每个单元格最多被访问一次，m和n分别是网格的行数和列数
 * 
 * 空间复杂度分析：
 * O(m*n) - 最坏情况下DFS递归栈深度或BFS队列大小可能达到m*n
 * 
 * C++语言特性：
 * - 使用vector容器存储网格
 * - 使用queue实现BFS
 * - 使用递归或迭代实现DFS
 * - 使用RAII原则管理资源
 */
class Code19_NumberOfIslands {
private:
    // 方向数组：上、右、下、左
    static const vector<vector<int>> DIRECTIONS;
    
public:
    /**
     * DFS解法：使用递归实现深度优先搜索
     */
    static int numIslandsDFS(vector<vector<char>>& grid) {
        if (grid.empty() || grid[0].empty()) {
            return 0;
        }
        
        int m = grid.size();
        int n = grid[0].size();
        int count = 0;
        
        // 遍历所有单元格
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == '1') {
                    count++;
                    dfs(grid, i, j, m, n);
                }
            }
        }
        
        return count;
    }
    
    /**
     * DFS辅助函数：标记所有相连的陆地
     */
    static void dfs(vector<vector<char>>& grid, int i, int j, int m, int n) {
        // 边界检查或已经访问过（标记为'0'）
        if (i < 0 || i >= m || j < 0 || j >= n || grid[i][j] != '1') {
            return;
        }
        
        // 标记当前单元格为已访问（改为'0'）
        grid[i][j] = '0';
        
        // 向四个方向递归搜索
        for (const auto& dir : DIRECTIONS) {
            int newI = i + dir[0];
            int newJ = j + dir[1];
            dfs(grid, newI, newJ, m, n);
        }
    }
    
    /**
     * BFS解法：使用队列实现广度优先搜索
     */
    static int numIslandsBFS(vector<vector<char>>& grid) {
        if (grid.empty() || grid[0].empty()) {
            return 0;
        }
        
        int m = grid.size();
        int n = grid[0].size();
        int count = 0;
        
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == '1') {
                    count++;
                    bfs(grid, i, j, m, n);
                }
            }
        }
        
        return count;
    }
    
    /**
     * BFS辅助函数：使用队列标记所有相连的陆地
     */
    static void bfs(vector<vector<char>>& grid, int i, int j, int m, int n) {
        queue<pair<int, int>> q;
        q.push({i, j});
        grid[i][j] = '0';  // 标记为已访问
        
        while (!q.empty()) {
            auto [x, y] = q.front();
            q.pop();
            
            // 检查四个方向
            for (const auto& dir : DIRECTIONS) {
                int newX = x + dir[0];
                int newY = y + dir[1];
                
                if (newX >= 0 && newX < m && newY >= 0 && newY < n && grid[newX][newY] == '1') {
                    q.push({newX, newY});
                    grid[newX][newY] = '0';  // 标记为已访问
                }
            }
        }
    }
    
    /**
     * 并查集解法：使用并查集数据结构
     */
    static int numIslandsUnionFind(vector<vector<char>>& grid) {
        if (grid.empty() || grid[0].empty()) {
            return 0;
        }
        
        int m = grid.size();
        int n = grid[0].size();
        UnionFind uf(grid);
        
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == '1') {
                    // 检查右边和下面的邻居
                    if (i + 1 < m && grid[i + 1][j] == '1') {
                        uf.union_set(i * n + j, (i + 1) * n + j);
                    }
                    if (j + 1 < n && grid[i][j + 1] == '1') {
                        uf.union_set(i * n + j, i * n + (j + 1));
                    }
                }
            }
        }
        
        return uf.getCount();
    }
    
    /**
     * 并查集实现类
     */
    class UnionFind {
    private:
        vector<int> parent;
        vector<int> rank;
        int count;
        
    public:
        UnionFind(vector<vector<char>>& grid) {
            int m = grid.size();
            int n = grid[0].size();
            parent.resize(m * n);
            rank.resize(m * n, 0);
            count = 0;
            
            // 初始化并查集
            for (int i = 0; i < m; i++) {
                for (int j = 0; j < n; j++) {
                    if (grid[i][j] == '1') {
                        int index = i * n + j;
                        parent[index] = index;
                        count++;
                    }
                }
            }
        }
        
        int find(int x) {
            if (parent[x] != x) {
                parent[x] = find(parent[x]);  // 路径压缩
            }
            return parent[x];
        }
        
        void union_set(int x, int y) {
            int rootX = find(x);
            int rootY = find(y);
            
            if (rootX != rootY) {
                // 按秩合并
                if (rank[rootX] > rank[rootY]) {
                    parent[rootY] = rootX;
                } else if (rank[rootX] < rank[rootY]) {
                    parent[rootX] = rootY;
                } else {
                    parent[rootY] = rootX;
                    rank[rootX]++;
                }
                count--;
            }
        }
        
        int getCount() const {
            return count;
        }
    };
    
    /**
     * 单元测试函数
     */
    static void testNumIslands() {
        cout << "=== 岛屿数量单元测试 ===" << endl;
        
        // 测试用例1：常规情况
        vector<vector<char>> grid1 = {
            {'1','1','1','1','0'},
            {'1','1','0','1','0'},
            {'1','1','0','0','0'},
            {'0','0','0','0','0'}
        };
        int result1 = numIslandsDFS(grid1);
        cout << "测试用例1 - 网格1:" << endl;
        printGrid(grid1);
        cout << "岛屿数量 (DFS): " << result1 << endl;
        cout << "期望: 1" << endl;
        
        // 测试用例2：多个岛屿
        vector<vector<char>> grid2 = {
            {'1','1','0','0','0'},
            {'1','1','0','0','0'},
            {'0','0','1','0','0'},
            {'0','0','0','1','1'}
        };
        int result2 = numIslandsDFS(grid2);
        cout << "\n测试用例2 - 网格2:" << endl;
        printGrid(grid2);
        cout << "岛屿数量 (DFS): " << result2 << endl;
        cout << "期望: 3" << endl;
        
        // 测试用例3：边界情况 - 空网格
        vector<vector<char>> grid3 = {};
        int result3 = numIslandsDFS(grid3);
        cout << "\n测试用例3 - 空网格" << endl;
        cout << "岛屿数量: " << result3 << endl;
        cout << "期望: 0" << endl;
        
        // 测试用例4：全陆地
        vector<vector<char>> grid4 = {
            {'1','1','1'},
            {'1','1','1'},
            {'1','1','1'}
        };
        int result4 = numIslandsDFS(grid4);
        cout << "\n测试用例4 - 全陆地:" << endl;
        printGrid(grid4);
        cout << "岛屿数量: " << result4 << endl;
        cout << "期望: 1" << endl;
        
        // 测试用例5：全水域
        vector<vector<char>> grid5 = {
            {'0','0','0'},
            {'0','0','0'},
            {'0','0','0'}
        };
        int result5 = numIslandsDFS(grid5);
        cout << "\n测试用例5 - 全水域:" << endl;
        printGrid(grid5);
        cout << "岛屿数量: " << result5 << endl;
        cout << "期望: 0" << endl;
    }
    
    /**
     * 性能对比测试：DFS vs BFS vs 并查集
     */
    static void performanceComparison() {
        cout << "\n=== 性能对比测试 ===" << endl;
        
        // 生成大规模测试网格
        int m = 1000, n = 1000;
        auto largeGrid = generateLargeGrid(m, n, 0.3);  // 30%陆地
        
        // 测试DFS
        auto gridDFS = largeGrid;
        auto startTime1 = chrono::high_resolution_clock::now();
        int result1 = numIslandsDFS(gridDFS);
        auto endTime1 = chrono::high_resolution_clock::now();
        auto duration1 = chrono::duration_cast<chrono::milliseconds>(endTime1 - startTime1);
        
        // 测试BFS
        auto gridBFS = largeGrid;
        auto startTime2 = chrono::high_resolution_clock::now();
        int result2 = numIslandsBFS(gridBFS);
        auto endTime2 = chrono::high_resolution_clock::now();
        auto duration2 = chrono::duration_cast<chrono::milliseconds>(endTime2 - startTime2);
        
        // 测试并查集
        auto gridUF = largeGrid;
        auto startTime3 = chrono::high_resolution_clock::now();
        int result3 = numIslandsUnionFind(gridUF);
        auto endTime3 = chrono::high_resolution_clock::now();
        auto duration3 = chrono::duration_cast<chrono::milliseconds>(endTime3 - startTime3);
        
        cout << "网格规模: " << m << " × " << n << endl;
        cout << "DFS执行时间: " << duration1.count() << "ms, 岛屿数量: " << result1 << endl;
        cout << "BFS执行时间: " << duration2.count() << "ms, 岛屿数量: " << result2 << endl;
        cout << "并查集执行时间: " << duration3.count() << "ms, 岛屿数量: " << result3 << endl;
        cout << "结果一致性: " << (result1 == result2 && result2 == result3) << endl;
    }
    
    /**
     * 生成大规模测试网格
     */
    static vector<vector<char>> generateLargeGrid(int m, int n, double landRatio) {
        vector<vector<char>> grid(m, vector<char>(n));
        random_device rd;
        mt19937 gen(rd());
        uniform_real_distribution<double> dist(0.0, 1.0);
        
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                grid[i][j] = dist(gen) < landRatio ? '1' : '0';
            }
        }
        
        return grid;
    }
    
    /**
     * 打印网格（用于调试）
     */
    static void printGrid(const vector<vector<char>>& grid) {
        if (grid.empty()) {
            cout << "[]" << endl;
            return;
        }
        
        int rowsToPrint = min(static_cast<int>(grid.size()), 10);
        int colsToPrint = min(static_cast<int>(grid[0].size()), 10);
        
        for (int i = 0; i < rowsToPrint; i++) {
            for (int j = 0; j < colsToPrint; j++) {
                cout << grid[i][j] << " ";
            }
            if (grid[0].size() > 10) cout << "...";
            cout << endl;
        }
        if (grid.size() > 10) cout << "..." << endl;
    }
    
    /**
     * 主运行函数
     */
    static void run() {
        // 运行单元测试
        testNumIslands();
        
        // 运行性能对比测试
        performanceComparison();
        
        cout << "\n=== 岛屿数量算法验证完成 ===" << endl;
    }
};

// 初始化静态成员
const vector<vector<int>> Code19_NumberOfIslands::DIRECTIONS = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};

// 程序入口点
int main() {
    Code19_NumberOfIslands::run();
    return 0;
}