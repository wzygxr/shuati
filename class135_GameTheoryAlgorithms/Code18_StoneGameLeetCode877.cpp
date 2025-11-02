#include <iostream>
#include <vector>
#include <algorithm>
using namespace std;

// 取石子游戏变种 (LeetCode 877)
// 题目来源：LeetCode 877. Stone Game - https://leetcode.com/problems/stone-game/
// 题目描述：亚历克斯和李用几堆石子做游戏。偶数堆石子排成一行，每堆都有正整数颗石子 piles[i] 。
// 游戏以谁手中的石子最多来决出胜负。石子的总数是奇数，所以没有平局。
// 亚历克斯先开始拿石子。总是从行的开始或结束处拿取整堆石子。
// 返回亚历克斯是否获胜。
//
// 算法核心思想：
// 1. 动态规划：dp[i][j]表示从i到j堆石子中，先手能获得的最大净胜分数
// 2. 状态转移：dp[i][j] = max(piles[i] - dp[i+1][j], piles[j] - dp[i][j-1])
// 3. 最终结果：dp[0][n-1] > 0 表示亚历克斯获胜
//
// 时间复杂度分析：
// 1. 时间复杂度：O(n^2) - 需要填充n*n的dp表
// 2. 空间复杂度：O(n^2) - 使用二维dp数组
//
// 工程化考量：
// 1. 异常处理：处理空数组和边界情况
// 2. 性能优化：使用动态规划避免重复计算
// 3. 可读性：添加详细注释说明算法原理
// 4. 可扩展性：支持不同的石子堆配置
class Code18_StoneGameLeetCode877 {
public:
    /**
     * 解决取石子游戏问题
     * @param piles 石子堆数组
     * @return 亚历克斯是否能获胜
     */
    static bool stoneGame(vector<int>& piles) {
        // 异常处理：处理空数组
        if (piles.empty()) {
            return false;
        }
        
        int n = piles.size();
        
        // 创建dp数组，dp[i][j]表示从i到j堆石子中，先手能获得的最大净胜分数
        vector<vector<int>> dp(n, vector<int>(n, 0));
        
        // 初始化对角线：当只有一堆石子时，先手获得该堆石子
        for (int i = 0; i < n; i++) {
            dp[i][i] = piles[i];
        }
        
        // 填充dp表，从长度为2的子数组开始
        for (int len = 2; len <= n; len++) {
            for (int i = 0; i <= n - len; i++) {
                int j = i + len - 1;
                
                // 状态转移方程：
                // 先手可以选择拿左边或右边的石子堆
                // 如果拿左边，净胜分数 = piles[i] - 后手在[i+1,j]区间的最优解
                // 如果拿右边，净胜分数 = piles[j] - 后手在[i,j-1]区间的最优解
                dp[i][j] = max(piles[i] - dp[i + 1][j], piles[j] - dp[i][j - 1]);
            }
        }
        
        // 如果整个区间的净胜分数大于0，亚历克斯获胜
        return dp[0][n - 1] > 0;
    }
    
    /**
     * 优化版本：使用一维数组降低空间复杂度
     * 时间复杂度：O(n^2)，空间复杂度：O(n)
     */
    static bool stoneGameOptimized(vector<int>& piles) {
        if (piles.empty()) {
            return false;
        }
        
        int n = piles.size();
        vector<int> dp(n, 0);
        
        // 初始化：复制石子堆的值
        for (int i = 0; i < n; i++) {
            dp[i] = piles[i];
        }
        
        // 从后向前填充dp数组
        for (int len = 2; len <= n; len++) {
            for (int i = 0; i <= n - len; i++) {
                int j = i + len - 1;
                dp[i] = max(piles[i] - dp[i + 1], piles[j] - dp[i]);
            }
        }
        
        return dp[0] > 0;
    }
    
    /**
     * 数学解法：由于石子堆数是偶数且总数是奇数，先手总是可以获胜
     * 时间复杂度：O(1)，空间复杂度：O(1)
     */
    static bool stoneGameMath(vector<int>& piles) {
        // 数学原理：由于堆数是偶数，先手可以控制拿所有奇数堆或所有偶数堆
        // 由于总数是奇数，奇数堆和偶数堆的总和必然不同
        // 先手可以选择拿总和更大的那一组，因此总是可以获胜
        return true;
    }
};

// 测试函数
int main() {
    // 测试用例1：标准情况
    vector<int> piles1 = {5, 3, 4, 5};
    cout << "测试用例1 [5,3,4,5]: " << Code18_StoneGameLeetCode877::stoneGame(piles1) << endl; // 应输出1(true)
    
    // 测试用例2：边界情况
    vector<int> piles2 = {3, 7, 2, 3};
    cout << "测试用例2 [3,7,2,3]: " << Code18_StoneGameLeetCode877::stoneGame(piles2) << endl; // 应输出1(true)
    
    // 测试用例3：两堆石子
    vector<int> piles3 = {3, 7};
    cout << "测试用例3 [3,7]: " << Code18_StoneGameLeetCode877::stoneGame(piles3) << endl; // 应输出1(true)
    
    // 测试用例4：四堆石子
    vector<int> piles4 = {1, 2, 3, 4};
    cout << "测试用例4 [1,2,3,4]: " << Code18_StoneGameLeetCode877::stoneGame(piles4) << endl; // 应输出1(true)
    
    // 验证优化版本
    cout << "优化版本测试:" << endl;
    cout << "测试用例1 [5,3,4,5]: " << Code18_StoneGameLeetCode877::stoneGameOptimized(piles1) << endl;
    cout << "测试用例2 [3,7,2,3]: " << Code18_StoneGameLeetCode877::stoneGameOptimized(piles2) << endl;
    
    // 验证数学解法
    cout << "数学解法测试:" << endl;
    cout << "所有测试用例: " << Code18_StoneGameLeetCode877::stoneGameMath(piles1) << endl;
    
    return 0;
}