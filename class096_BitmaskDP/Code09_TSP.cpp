#include <iostream>
#include <vector>
#include <algorithm>
#include <climits>
#include <cstring>
using namespace std;

// 经典TSP问题解法
int tsp(vector<vector<int>>& graph) {
    int n = graph.size();
    if (n == 0) return 0;
    
    vector<vector<int>> dp(1 << n, vector<int>(n, INT_MAX));
    dp[1][0] = 0;
    
    for (int mask = 1; mask < (1 << n); mask++) {
        for (int i = 0; i < n; i++) {
            if (dp[mask][i] == INT_MAX) continue;
            
            for (int j = 0; j < n; j++) {
                if ((mask & (1 << j)) != 0) continue;
                if (graph[i][j] > 0) {
                    int newMask = mask | (1 << j);
                    int newDist = dp[mask][i] + graph[i][j];
                    if (dp[newMask][j] > newDist) {
                        dp[newMask][j] = newDist;
                    }
                }
            }
        }
    }
    
    int result = INT_MAX;
    int allVisited = (1 << n) - 1;
    for (int i = 0; i < n; i++) {
        if (dp[allVisited][i] != INT_MAX && graph[i][0] > 0) {
            result = min(result, dp[allVisited][i] + graph[i][0]);
        }
    }
    
    return result == INT_MAX ? -1 : result;
}

// LeetCode 980 哈密顿路径解法
int uniquePathsIII(vector<vector<int>>& grid) {
    int m = grid.size(), n = grid[0].size();
    int startX = -1, startY = -1, targetX = -1, targetY = -1;
    int emptyCount = 0;
    
    for (int i = 0; i < m; i++) {
        for (int j = 0; j < n; j++) {
            if (grid[i][j] == 0) emptyCount++;
            else if (grid[i][j] == 1) {
                startX = i; startY = j;
            } else if (grid[i][j] == 2) {
                targetX = i; targetY = j;
            }
        }
    }
    
    int totalCells = emptyCount + 2;
    int totalStates = 1 << totalCells;
    vector<vector<int>> dp(totalStates, vector<int>(totalCells, 0));
    
    int startPos = startX * n + startY;
    dp[1 << startPos][startPos] = 1;
    
    vector<int> dx = {-1, 1, 0, 0};
    vector<int> dy = {0, 0, -1, 1};
    
    for (int mask = 0; mask < totalStates; mask++) {
        for (int pos = 0; pos < totalCells; pos++) {
            if (dp[mask][pos] == 0) continue;
            
            int x = pos / n, y = pos % n;
            for (int d = 0; d < 4; d++) {
                int nx = x + dx[d], ny = y + dy[d];
                if (nx < 0 || nx >= m || ny < 0 || ny >= n || grid[nx][ny] == -1) continue;
                
                int newPos = nx * n + ny;
                if ((mask & (1 << newPos)) != 0) continue;
                
                int newMask = mask | (1 << newPos);
                dp[newMask][newPos] += dp[mask][pos];
            }
        }
    }
    
    int targetPos = targetX * n + targetY;
    int targetMask = (1 << totalCells) - 1;
    return dp[targetMask][targetPos];
}

// CodeForces 165E 最大兼容数对解法
vector<int> compatibleNumbers(vector<int>& nums) {
    int n = nums.size();
    int maxVal = 0;
    for (int num : nums) maxVal = max(maxVal, num);
    
    int bits = 0;
    while ((1 << bits) <= maxVal) bits++;
    
    vector<int> complement(1 << bits, -1);
    for (int i = 0; i < n; i++) {
        complement[nums[i]] = i;
    }
    
    // SOS DP
    for (int mask = 0; mask < (1 << bits); mask++) {
        if (complement[mask] != -1) {
            for (int subMask = mask; subMask > 0; subMask = (subMask - 1) & mask) {
                if (complement[subMask] == -1) {
                    complement[subMask] = complement[mask];
                }
            }
        }
    }
    
    vector<int> result(n, -1);
    for (int i = 0; i < n; i++) {
        int complementMask = ((1 << bits) - 1) ^ nums[i];
        if (complement[complementMask] != -1) {
            result[i] = complement[complementMask];
        }
    }
    
    return result;
}

int main() {
    // 测试TSP问题
    vector<vector<int>> graph = {
        {0, 10, 15, 20},
        {10, 0, 35, 25},
        {15, 35, 0, 30},
        {20, 25, 30, 0}
    };
    cout << "TSP问题测试:" << endl;
    cout << "最短路径长度: " << tsp(graph) << endl;
    
    // 测试LeetCode 980 哈密顿路径
    vector<vector<int>> grid = {
        {1, 0, 0, 0},
        {0, 0, 0, 0},
        {0, 0, 2, -1}
    };
    cout << "\nLeetCode 980 哈密顿路径测试:" << endl;
    cout << "路径数量: " << uniquePathsIII(grid) << endl;
    
    // 测试CodeForces 165E 最大兼容数对
    vector<int> nums = {3, 1, 4, 2};
    cout << "\nCodeForces 165E 最大兼容数对测试:" << endl;
    vector<int> result = compatibleNumbers(nums);
    cout << "数组: ";
    for (int num : nums) cout << num << " ";
    cout << endl;
    cout << "结果: ";
    for (int idx : result) cout << idx << " ";
    cout << endl;
    
    return 0;
}