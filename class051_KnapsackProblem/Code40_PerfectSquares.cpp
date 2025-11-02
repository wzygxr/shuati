#include <iostream>
#include <vector>
#include <queue>
#include <cmath>
#include <climits>

// LeetCode 279. 完全平方数
// 题目描述：给你一个整数 n ，返回和为 n 的完全平方数的最少数量。
// 完全平方数是一个整数，其值等于另一个整数的平方；换句话说，其值等于一个整数自乘的积。例如，1、4、9 和 16 都是完全平方数，而 3 和 11 不是。
// 链接：https://leetcode.cn/problems/perfect-squares/
// 
// 解题思路：
// 这是一个完全背包问题。我们可以将问题转化为：使用最少数量的物品（每个物品是一个完全平方数），恰好装满容量为n的背包。
// 
// 状态定义：dp[i] 表示和为i的完全平方数的最少数量
// 状态转移方程：dp[i] = min(dp[i], dp[i - j * j] + 1)，其中j * j <= i
// 初始状态：dp[0] = 0，表示和为0的完全平方数的最少数量为0
// 
// 时间复杂度：O(n * sqrt(n))，其中n是给定的整数
// 空间复杂度：O(n)，使用一维DP数组

class Solution {
public:
    // 基础DP解法
    int numSquares(int n) {
        if (n < 1) {
            return 0;
        }
        
        // 创建DP数组，dp[i]表示和为i的完全平方数的最少数量
        std::vector<int> dp(n + 1, 0);
        
        // 初始化DP数组，初始值设为最大可能值（即全部用1相加）
        for (int i = 1; i <= n; ++i) {
            dp[i] = i; // 最坏情况下，i可以由i个1组成
        }
        
        // 填充DP数组
        for (int i = 2; i <= n; ++i) {
            // 尝试所有可能的完全平方数j^2，其中j^2 <= i
            for (int j = 1; j * j <= i; ++j) {
                // 更新状态：选择当前完全平方数j^2，那么问题转化为求dp[i - j * j] + 1
                dp[i] = std::min(dp[i], dp[i - j * j] + 1);
            }
        }
        
        return dp[n];
    }
    
    // 优化版本，预先生成所有可能的完全平方数
    int numSquaresOptimized(int n) {
        if (n < 1) {
            return 0;
        }
        
        // 预先生成所有可能的完全平方数
        int maxSquareRoot = std::sqrt(n);
        std::vector<int> squares;
        squares.reserve(maxSquareRoot);
        for (int i = 1; i <= maxSquareRoot; ++i) {
            squares.push_back(i * i);
        }
        
        // 创建DP数组
        std::vector<int> dp(n + 1, 0);
        
        // 初始化DP数组
        for (int i = 1; i <= n; ++i) {
            dp[i] = i;
        }
        
        // 填充DP数组
        for (int i = 2; i <= n; ++i) {
            // 尝试所有可能的完全平方数
            for (int square : squares) {
                if (square > i) {
                    break; // 由于squares是递增的，当square > i时，后面的平方数也都大于i，直接break
                }
                dp[i] = std::min(dp[i], dp[i - square] + 1);
            }
        }
        
        return dp[n];
    }
    
    // 使用广度优先搜索(BFS)实现
    int numSquaresBFS(int n) {
        if (n < 1) {
            return 0;
        }
        
        // 预先生成所有可能的完全平方数
        int maxSquareRoot = std::sqrt(n);
        std::vector<int> squares;
        squares.reserve(maxSquareRoot);
        for (int i = 1; i <= maxSquareRoot; ++i) {
            squares.push_back(i * i);
        }
        
        // 使用队列进行BFS
        std::queue<int> q;
        std::vector<bool> visited(n + 1, false); // 记录哪些数字已经访问过，避免重复处理
        
        q.push(0); // 从0开始
        visited[0] = true;
        int level = 0; // 当前的层数，即使用的完全平方数的数量
        
        while (!q.empty()) {
            level++;
            int size = q.size();
            
            // 处理当前层的所有节点
            for (int i = 0; i < size; ++i) {
                int current = q.front();
                q.pop();
                
                // 尝试所有可能的完全平方数
                for (int square : squares) {
                    int next = current + square;
                    
                    if (next == n) {
                        return level; // 找到目标值，返回当前层数
                    }
                    
                    if (next > n || visited[next]) {
                        continue; // 超过目标值或者已经访问过，跳过
                    }
                    
                    visited[next] = true;
                    q.push(next);
                }
            }
        }
        
        return n; // 默认返回n（实际上不应该到达这里）
    }
    
    // 使用数学方法优化，基于拉格朗日四平方定理
    int numSquaresMath(int n) {
        if (n < 1) {
            return 0;
        }
        
        // 如果n是完全平方数，直接返回1
        if (isPerfectSquare(n)) {
            return 1;
        }
        
        // 检查是否可以表示为两个完全平方数的和
        if (canBeExpressedAsSumOfTwoSquares(n)) {
            return 2;
        }
        
        // 检查是否可以表示为三个完全平方数的和
        // 根据Legendre三平方定理，如果n不是形如4^k*(8m+7)，则可以表示为三个平方数的和
        int temp = n;
        while (temp % 4 == 0) {
            temp /= 4;
        }
        if (temp % 8 != 7) {
            return 3;
        }
        
        // 根据四平方定理，所有自然数都可以表示为4个平方数的和
        return 4;
    }
    
private:
    /**
     * 判断一个数是否是完全平方数
     */
    bool isPerfectSquare(int num) {
        int sqrtNum = std::sqrt(num);
        return sqrtNum * sqrtNum == num;
    }
    
    /**
     * 判断一个数是否可以表示为两个完全平方数的和
     */
    bool canBeExpressedAsSumOfTwoSquares(int num) {
        for (int i = 0; i * i <= num; ++i) {
            int remainder = num - i * i;
            if (isPerfectSquare(remainder)) {
                return true;
            }
        }
        return false;
    }
};

int main() {
    Solution solution;
    
    // 测试用例1
    int n1 = 12;
    std::cout << "测试用例1结果: " << solution.numSquares(n1) << " (预期: 3)" << std::endl;
    std::cout << "优化版本结果: " << solution.numSquaresOptimized(n1) << std::endl;
    std::cout << "BFS版本结果: " << solution.numSquaresBFS(n1) << std::endl;
    std::cout << "数学优化版本结果: " << solution.numSquaresMath(n1) << std::endl;
    
    std::cout << "---------------------------" << std::endl;
    
    // 测试用例2
    int n2 = 13;
    std::cout << "测试用例2结果: " << solution.numSquares(n2) << " (预期: 2)" << std::endl;
    std::cout << "优化版本结果: " << solution.numSquaresOptimized(n2) << std::endl;
    std::cout << "BFS版本结果: " << solution.numSquaresBFS(n2) << std::endl;
    std::cout << "数学优化版本结果: " << solution.numSquaresMath(n2) << std::endl;
    
    std::cout << "---------------------------" << std::endl;
    
    // 测试用例3
    int n3 = 1;
    std::cout << "测试用例3结果: " << solution.numSquares(n3) << " (预期: 1)" << std::endl;
    
    std::cout << "---------------------------" << std::endl;
    
    // 测试用例4
    int n4 = 2;
    std::cout << "测试用例4结果: " << solution.numSquares(n4) << " (预期: 2)" << std::endl;
    
    return 0;
}