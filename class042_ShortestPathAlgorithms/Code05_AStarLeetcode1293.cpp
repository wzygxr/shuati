#include <iostream>
#include <vector>
#include <queue>
#include <climits>
#include <algorithm>
#include <cmath>
using namespace std;

// LeetCode 1293. 网格中的最短路径
// 题目链接: https://leetcode.cn/problems/shortest-path-in-a-grid-with-obstacles-elimination/
// 题目描述: 给你一个 m * n 的网格，其中每个单元格不是 0 （空）就是 1 （障碍物）。
// 每一步，您都可以在空白单元格中上、下、左、右移动。
// 如果您最多可以消除 k 个障碍物，请找出从左上角 (0, 0) 到右下角 (m-1, n-1) 的最短路径，
// 并返回通过该路径所需的步数。如果找不到这样的路径，则返回 -1。
//
// 解题思路:
// 这道题可以使用A*算法来解决。状态不仅包括位置(x,y)，还包括已经消除的障碍物数量。
// 我们使用优先队列来存储状态，状态包括:
// 1. 当前位置(x,y)
// 2. 已经走过的步数
// 3. 已经消除的障碍物数量
// 4. 估价函数f = g + h，其中g是已经走过的步数，h是启发函数(曼哈顿距离)
//
// 时间复杂度: O(M*N*K*log(M*N*K))，其中M和N是网格的行数和列数，K是最多可以消除的障碍物数量
// 空间复杂度: O(M*N*K)

// 定义状态结构体
struct State {
    int x, y, steps, obstacles, f;
    
    State(int _x, int _y, int _steps, int _obstacles, int _f) 
        : x(_x), y(_y), steps(_steps), obstacles(_obstacles), f(_f) {}
    
    // 重载小于运算符，用于优先队列
    bool operator>(const State& other) const {
        return f > other.f;
    }
};

class Solution {
public:
    // 方向数组：上、右、下、左
    vector<int> move = {-1, 0, 1, 0, -1};
    
    int shortestPath(vector<vector<int>>& grid, int k) {
        int m = grid.size();
        int n = grid[0].size();
        
        // 特殊情况：起点就是终点
        if (m == 1 && n == 1) {
            return 0;
        }
        
        // 如果k足够大，可以直接走曼哈顿距离最短路径
        if (k >= m + n - 2) {
            return m + n - 2;
        }
        
        // visited[x][y][obs]表示在位置(x,y)且已经消除obs个障碍物的状态是否已经访问过
        vector<vector<vector<bool>>> visited(m, vector<vector<bool>>(n, vector<bool>(k + 1, false)));
        
        // 优先队列
        priority_queue<State, vector<State>, greater<State>> pq;
        
        // 初始状态
        int startX = 0, startY = 0;
        int startObstacles = grid[0][0] == 1 ? 1 : 0;
        if (startObstacles <= k) {
            int h = manhattanDistance(0, 0, m - 1, n - 1);
            pq.push(State(startX, startY, 0, startObstacles, h));
            visited[startX][startY][startObstacles] = true;
        }
        
        while (!pq.empty()) {
            State current = pq.top();
            pq.pop();
            
            int x = current.x;
            int y = current.y;
            int steps = current.steps;
            int obstacles = current.obstacles;
            
            // 到达终点
            if (x == m - 1 && y == n - 1) {
                return steps;
            }
            
            // 四个方向探索
            for (int i = 0; i < 4; i++) {
                int nx = x + move[i];
                int ny = y + move[i + 1];
                
                // 检查边界
                if (nx >= 0 && nx < m && ny >= 0 && ny < n) {
                    // 计算新的障碍物数量
                    int newObstacles = obstacles + grid[nx][ny];
                    
                    // 如果障碍物数量不超过k且该状态未访问过
                    if (newObstacles <= k && !visited[nx][ny][newObstacles]) {
                        visited[nx][ny][newObstacles] = true;
                        int newSteps = steps + 1;
                        int h = manhattanDistance(nx, ny, m - 1, n - 1);
                        int f = newSteps + h;
                        pq.push(State(nx, ny, newSteps, newObstacles, f));
                    }
                }
            }
        }
        
        return -1;
    }
    
    // 曼哈顿距离启发函数
    int manhattanDistance(int x1, int y1, int x2, int y2) {
        return abs(x1 - x2) + abs(y1 - y2);
    }
};

// 测试函数
int main() {
    Solution solution;
    
    // 测试用例1
    vector<vector<int>> grid1 = {{0,0,0},{1,1,0},{0,0,0},{0,1,1},{0,0,0}};
    int k1 = 1;
    cout << "测试用例1结果: " << solution.shortestPath(grid1, k1) << endl; // 期望输出: 6
    
    // 测试用例2
    vector<vector<int>> grid2 = {{0,1,1},{1,1,1},{1,0,0}};
    int k2 = 1;
    cout << "测试用例2结果: " << solution.shortestPath(grid2, k2) << endl; // 期望输出: -1
    
    return 0;
}