#include <iostream>
#include <vector>
#include <algorithm>
#include <climits>

using namespace std;

/**
 * 樱桃采摘II问题 - C++实现
 * 算法思路：三维动态规划
 * 时间复杂度：O(rows * cols^2)
 * 空间复杂度：O(rows * cols^2)，可优化到O(cols^2)
 * 
 * 核心思想：
 * 1. 使用三维DP数组dp[i][j1][j2]表示当机器人1在(i,j1)，机器人2在(i,j2)时能收集的最大樱桃数
 * 2. 状态转移：考虑两个机器人从前一行的9种可能位置组合转移而来
 * 3. 关键优化：如果两个机器人在同一位置，只计算一次樱桃数量
 */

class CherryPickupII {
public:
    /**
     * 计算两个机器人能收集的最大樱桃数
     * @param grid 二维矩阵，表示樱桃田
     * @return 最大樱桃数
     */
    int cherryPickup(vector<vector<int>>& grid) {
        int rows = grid.size();
        if (rows == 0) return 0;
        int cols = grid[0].size();
        
        // 输入验证
        if (cols == 0) return 0;
        
        // 创建三维DP数组，初始化为-1表示未访问
        vector<vector<vector<int>>> dp(rows, 
            vector<vector<int>>(cols, 
                vector<int>(cols, -1)));
        
        // 初始化起始位置
        dp[0][0][cols - 1] = grid[0][0] + grid[0][cols - 1];
        
        // 填充DP表
        for (int i = 1; i < rows; i++) {
            for (int j1 = 0; j1 < cols; j1++) {
                for (int j2 = 0; j2 < cols; j2++) {
                    // 计算当前位置的樱桃数
                    int cherries = (j1 == j2) ? grid[i][j1] : (grid[i][j1] + grid[i][j2]);
                    
                    // 检查所有可能的前驱状态
                    for (int p1 = j1 - 1; p1 <= j1 + 1; p1++) {
                        for (int p2 = j2 - 1; p2 <= j2 + 1; p2++) {
                            // 检查前驱状态是否有效
                            if (p1 >= 0 && p1 < cols && p2 >= 0 && p2 < cols && dp[i - 1][p1][p2] != -1) {
                                dp[i][j1][j2] = max(dp[i][j1][j2], dp[i - 1][p1][p2] + cherries);
                            }
                        }
                    }
                }
            }
        }
        
        // 找到最后一行的最大值
        int result = 0;
        for (int j1 = 0; j1 < cols; j1++) {
            for (int j2 = 0; j2 < cols; j2++) {
                result = max(result, dp[rows - 1][j1][j2]);
            }
        }
        
        return result;
    }
    
    /**
     * 空间优化版本：使用滚动数组将空间复杂度优化到O(cols^2)
     * @param grid 二维矩阵
     * @return 最大樱桃数
     */
    int cherryPickupOptimized(vector<vector<int>>& grid) {
        int rows = grid.size();
        if (rows == 0) return 0;
        int cols = grid[0].size();
        
        // 输入验证
        if (cols == 0) return 0;
        
        // 使用滚动数组，只需要保存当前行和上一行的状态
        vector<vector<int>> prev(cols, vector<int>(cols, -1));
        vector<vector<int>> curr(cols, vector<int>(cols, -1));
        
        // 初始化起始位置
        prev[0][cols - 1] = grid[0][0] + grid[0][cols - 1];
        
        for (int i = 1; i < rows; i++) {
            // 清空当前行状态
            for (int j1 = 0; j1 < cols; j1++) {
                for (int j2 = 0; j2 < cols; j2++) {
                    curr[j1][j2] = -1;
                }
            }
            
            for (int j1 = 0; j1 < cols; j1++) {
                for (int j2 = 0; j2 < cols; j2++) {
                    // 计算当前位置的樱桃数
                    int cherries = (j1 == j2) ? grid[i][j1] : (grid[i][j1] + grid[i][j2]);
                    
                    // 检查所有可能的前驱状态
                    for (int p1 = j1 - 1; p1 <= j1 + 1; p1++) {
                        for (int p2 = j2 - 1; p2 <= j2 + 1; p2++) {
                            if (p1 >= 0 && p1 < cols && p2 >= 0 && p2 < cols && prev[p1][p2] != -1) {
                                curr[j1][j2] = max(curr[j1][j2], prev[p1][p2] + cherries);
                            }
                        }
                    }
                }
            }
            
            // 交换prev和curr
            swap(prev, curr);
        }
        
        // 找到最大值
        int result = 0;
        for (int j1 = 0; j1 < cols; j1++) {
            for (int j2 = 0; j2 < cols; j2++) {
                result = max(result, prev[j1][j2]);
            }
        }
        
        return result;
    }
    
    /**
     * 单元测试函数
     */
    void test() {
        // 测试用例1
        vector<vector<int>> grid1 = {
            {3, 1, 1},
            {2, 5, 1},
            {1, 5, 5}
        };
        int result1 = cherryPickup(grid1);
        cout << "Test 1 - Basic: " << result1 << " (Expected: 21)" << endl;
        
        // 测试用例2
        vector<vector<int>> grid2 = {
            {1, 0, 0, 0, 0, 0, 1},
            {0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0, 0, 0},
            {1, 0, 0, 0, 0, 0, 1}
        };
        int result2 = cherryPickup(grid2);
        cout << "Test 2 - Large Grid: " << result2 << " (Expected: 8)" << endl;
        
        // 测试用例3：边界情况 - 单行
        vector<vector<int>> grid3 = {
            {1, 0, 1}
        };
        int result3 = cherryPickup(grid3);
        cout << "Test 3 - Single Row: " << result3 << " (Expected: 2)" << endl;
        
        // 测试优化版本
        int result1Opt = cherryPickupOptimized(grid1);
        cout << "Test 1 - Optimized: " << result1Opt << " (Expected: 21)" << endl;
        
        // 性能测试：大规模数据
        vector<vector<int>> largeGrid(50, vector<int>(50, 1));
        largeGrid[0][0] = 0;
        largeGrid[0][49] = 0;
        
        cout << "Performance test started..." << endl;
        int largeResult = cherryPickupOptimized(largeGrid);
        cout << "Performance test completed. Result: " << largeResult << endl;
    }
};

int main() {
    CherryPickupII solution;
    
    // 运行单元测试
    solution.test();
    
    return 0;
}

/*
 * 相关题目扩展：
 * 
 * 1. LeetCode 741 - Cherry Pickup (摘樱桃 I)
 *    链接：https://leetcode.cn/problems/cherry-pickup/
 *    区别：一个人从(0,0)走到(n-1,n-1)再走回(0,0)，求最大收集樱桃数
 *    算法：三维动态规划，转化为两个人同时从起点到终点的问题
 * 
 * 2. LeetCode 64 - Minimum Path Sum (最小路径和)
 *    链接：https://leetcode.cn/problems/minimum-path-sum/
 *    区别：求从左上角到右下角的最小路径和，每步只能向下或向右
 *    算法：二维动态规划
 * 
 * 3. LeetCode 174 - Dungeon Game (地下城游戏)
 *    链接：https://leetcode.cn/problems/dungeon-game/
 *    区别：骑士需要从左上角到右下角，保证健康点数始终大于0的最小初始值
 *    算法：从右下角向左上角动态规划
 * 
 * 4. LeetCode 62 - Unique Paths (不同路径)
 *    链接：https://leetcode.cn/problems/unique-paths/
 *    区别：计算从左上角到右下角的不同路径数量
 *    算法：组合数学或二维动态规划
 * 
 * 5. LeetCode 63 - Unique Paths II (不同路径 II)
 *    链接：https://leetcode.cn/problems/unique-paths-ii/
 *    区别：网格中有障碍物，计算不同路径数量
 *    算法：二维动态规划，遇到障碍物时dp[i][j] = 0
 * 
 * 6. Codeforces 1296D - Fight with Monsters
 *    链接：https://codeforces.com/problemset/problem/1296/D
 *    区别：贪心策略解决怪物战斗问题
 *    算法：排序后贪心选择最优策略
 * 
 * 7. AtCoder ABC159E - Dividing Chocolate
 *    链接：https://atcoder.jp/contests/abc159/tasks/abc159_e
 *    区别：二维网格分割问题
 *    算法：前缀和+状态压缩动态规划
 * 
 * 8. 洛谷 P1434 - [SHOI2002] 滑雪
 *    链接：https://www.luogu.com.cn/problem/P1434
 *    区别：寻找最长滑雪路径
 *    算法：记忆化搜索或拓扑排序动态规划
 * 
 * 9. 牛客网 NC14552 - 方格取数
 *    链接：https://ac.nowcoder.com/acm/problem/14552
 *    区别：与摘樱桃I非常相似
 *    算法：三维动态规划
 * 
 * 10. UVa 10913 - Walking on a Grid
 *     链接：https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=24&page=show_problem&problem=1854
 *     区别：在网格中行走，有负数，最多允许k次负数
 *     算法：四维动态规划
 * 
 * 算法技巧总结：
 * 1. 三维状态设计：同时跟踪两个机器人的位置
 * 2. 状态转移优化：考虑所有可能的前驱状态组合
 * 3. 避免重复计算：同一位置的樱桃只计算一次
 * 4. 边界条件处理：仔细检查机器人移动是否越界
 * 5. 空间优化：使用滚动数组优化空间复杂度
 * 
 * C++工程化考量：
 * 1. 内存管理：使用vector避免手动内存管理
 * 2. 性能优化：使用引用避免不必要的拷贝
 * 3. 异常安全：使用RAII原则管理资源
 * 4. 代码可读性：使用有意义的变量名和注释
 * 5. 测试覆盖：包含边界测试、性能测试
 * 
 * 调试技巧：
 * 1. 使用cout输出中间变量值
 * 2. 使用gdb调试器进行调试
 * 3. 编写单元测试验证算法正确性
 * 4. 使用性能分析工具优化代码效率
 */