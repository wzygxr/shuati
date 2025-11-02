#include <iostream>
#include <vector>
#include <algorithm>
#include <climits>
using namespace std;

// 石子游戏III (LeetCode 1406)
// 题目来源：LeetCode 1406. Stone Game III - https://leetcode.com/problems/stone-game-iii/
// 题目描述：爱丽丝和鲍勃用几堆石子做游戏。几堆石子排成一行，每堆都有正整数颗石子 piles[i]。
// 游戏以谁手中的石子最多来决出胜负。爱丽丝先开始。
// 在每个玩家的回合中，该玩家可以拿走剩下的石子堆的前 1、2 或 3 堆。
// 游戏持续到所有石子堆都被拿走。
// 假设爱丽丝和鲍勃都发挥出最佳水平，返回游戏结果。
//
// 算法核心思想：
// 1. 动态规划：dp[i]表示从第i堆开始，当前玩家能获得的最大净胜分数
// 2. 状态转移：dp[i] = max(当前玩家拿1堆 - dp[i+1], 拿2堆 - dp[i+2], 拿3堆 - dp[i+3])
// 3. 最终结果：根据dp[0]的值判断胜负
//
// 时间复杂度分析：
// 1. 时间复杂度：O(n) - 线性遍历石子堆
// 2. 空间复杂度：O(n) - 使用一维dp数组
//
// 工程化考量：
// 1. 异常处理：处理空数组和边界情况
// 2. 性能优化：使用动态规划避免重复计算
// 3. 可读性：添加详细注释说明算法原理
// 4. 可扩展性：支持不同的取石子策略
class Code20_StoneGameIIILeetCode1406 {
public:
    /**
     * 解决石子游戏III问题
     * @param piles 石子堆数组
     * @return 游戏结果："Alice"、"Bob"或"Tie"
     */
    static string stoneGameIII(vector<int>& piles) {
        // 异常处理：处理空数组
        if (piles.empty()) {
            return "Tie";
        }
        
        int n = piles.size();
        
        // 创建dp数组，dp[i]表示从第i堆开始，当前玩家能获得的最大净胜分数
        vector<int> dp(n + 1, 0);
        
        // 从后向前递推
        for (int i = n - 1; i >= 0; i--) {
            // 当前玩家拿1堆石子
            int take1 = piles[i] - dp[i + 1];
            
            // 当前玩家拿2堆石子（如果可能）
            int take2 = INT_MIN;
            if (i + 1 < n) {
                take2 = piles[i] + piles[i + 1] - dp[i + 2];
            }
            
            // 当前玩家拿3堆石子（如果可能）
            int take3 = INT_MIN;
            if (i + 2 < n) {
                take3 = piles[i] + piles[i + 1] + piles[i + 2] - dp[i + 3];
            }
            
            // 当前玩家选择最优策略
            dp[i] = max(take1, max(take2, take3));
        }
        
        // 根据dp[0]的值判断胜负
        if (dp[0] > 0) {
            return "Alice";
        } else if (dp[0] < 0) {
            return "Bob";
        } else {
            return "Tie";
        }
    }
    
    /**
     * 优化版本：使用前缀和简化计算
     * 时间复杂度：O(n)，空间复杂度：O(n)
     */
    static string stoneGameIIIOptimized(vector<int>& piles) {
        if (piles.empty()) {
            return "Tie";
        }
        
        int n = piles.size();
        
        // 创建前缀和数组
        vector<int> prefixSum(n + 1, 0);
        for (int i = 1; i <= n; i++) {
            prefixSum[i] = prefixSum[i - 1] + piles[i - 1];
        }
        
        // 创建dp数组
        vector<int> dp(n + 1, 0);
        
        // 从后向前递推
        for (int i = n - 1; i >= 0; i--) {
            int maxScore = INT_MIN;
            
            // 尝试拿1、2、3堆石子
            for (int x = 1; x <= 3 && i + x <= n; x++) {
                // 当前玩家拿x堆石子获得的总分数
                int currentScore = prefixSum[i + x] - prefixSum[i];
                // 对手从i+x位置开始的最优解
                int opponentScore = (i + x < n) ? dp[i + x] : 0;
                // 当前玩家的净胜分数
                maxScore = max(maxScore, currentScore - opponentScore);
            }
            
            dp[i] = maxScore;
        }
        
        // 根据dp[0]的值判断胜负
        if (dp[0] > 0) {
            return "Alice";
        } else if (dp[0] < 0) {
            return "Bob";
        } else {
            return "Tie";
        }
    }
    
    /**
     * 空间优化版本：使用滚动数组降低空间复杂度
     * 时间复杂度：O(n)，空间复杂度：O(1)
     */
    static string stoneGameIIISpaceOptimized(vector<int>& piles) {
        if (piles.empty()) {
            return "Tie";
        }
        
        int n = piles.size();
        
        // 使用滚动数组，只需要保存最近3个状态
        int dp1 = 0, dp2 = 0, dp3 = 0;
        
        // 从后向前递推
        for (int i = n - 1; i >= 0; i--) {
            int maxScore = INT_MIN;
            
            // 尝试拿1堆石子
            maxScore = max(maxScore, piles[i] - dp1);
            
            // 尝试拿2堆石子（如果可能）
            if (i + 1 < n) {
                maxScore = max(maxScore, piles[i] + piles[i + 1] - dp2);
            }
            
            // 尝试拿3堆石子（如果可能）
            if (i + 2 < n) {
                maxScore = max(maxScore, piles[i] + piles[i + 1] + piles[i + 2] - dp3);
            }
            
            // 更新滚动数组
            dp3 = dp2;
            dp2 = dp1;
            dp1 = maxScore;
        }
        
        // 根据最终结果判断胜负
        if (dp1 > 0) {
            return "Alice";
        } else if (dp1 < 0) {
            return "Bob";
        } else {
            return "Tie";
        }
    }
};

// 测试函数
int main() {
    // 测试用例1：标准情况
    vector<int> piles1 = {1, 2, 3, 7};
    cout << "测试用例1 [1,2,3,7]: " << Code20_StoneGameIIILeetCode1406::stoneGameIII(piles1) << endl; // 应输出"Bob"
    
    // 测试用例2：平局
    vector<int> piles2 = {1, 2, 3, 6};
    cout << "测试用例2 [1,2,3,6]: " << Code20_StoneGameIIILeetCode1406::stoneGameIII(piles2) << endl; // 应输出"Tie"
    
    // 测试用例3：爱丽丝获胜
    vector<int> piles3 = {1, 2, 3, -1, -2, -3, 7};
    cout << "测试用例3 [1,2,3,-1,-2,-3,7]: " << Code20_StoneGameIIILeetCode1406::stoneGameIII(piles3) << endl; // 应输出"Alice"
    
    // 测试用例4：单堆石子
    vector<int> piles4 = {10};
    cout << "测试用例4 [10]: " << Code20_StoneGameIIILeetCode1406::stoneGameIII(piles4) << endl; // 应输出"Alice"
    
    // 测试用例5：两堆石子
    vector<int> piles5 = {3, 2};
    cout << "测试用例5 [3,2]: " << Code20_StoneGameIIILeetCode1406::stoneGameIII(piles5) << endl; // 应输出"Alice"
    
    // 验证优化版本
    cout << "优化版本测试:" << endl;
    cout << "测试用例1 [1,2,3,7]: " << Code20_StoneGameIIILeetCode1406::stoneGameIIIOptimized(piles1) << endl;
    cout << "测试用例2 [1,2,3,6]: " << Code20_StoneGameIIILeetCode1406::stoneGameIIIOptimized(piles2) << endl;
    
    // 验证空间优化版本
    cout << "空间优化版本测试:" << endl;
    cout << "测试用例1 [1,2,3,7]: " << Code20_StoneGameIIILeetCode1406::stoneGameIIISpaceOptimized(piles1) << endl;
    cout << "测试用例2 [1,2,3,6]: " << Code20_StoneGameIIILeetCode1406::stoneGameIIISpaceOptimized(piles2) << endl;
    
    // 边界测试：空数组
    vector<int> emptyPiles;
    cout << "空数组测试: " << Code20_StoneGameIIILeetCode1406::stoneGameIII(emptyPiles) << endl; // 应输出"Tie"
    
    return 0;
}