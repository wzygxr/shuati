#include <iostream>
#include <vector>
#include <algorithm>
#include <climits>
using namespace std;

/*
 * 苹果袋子问题 - C++实现
 * 
 * 题目描述：
 * 有装下8个苹果的袋子、装下6个苹果的袋子，一定要保证买苹果时所有使用的袋子都装满
 * 对于无法装满所有袋子的方案不予考虑，给定n个苹果，返回至少要多少个袋子
 * 如果不存在每个袋子都装满的方案返回-1
 * 
 * 解题思路：
 * 这是一个典型的背包问题变种，可以使用动态规划或数学规律来解决
 * 1. 动态规划解法：使用dp数组记录每个苹果数的最少袋子数
 * 2. 数学规律解法：通过观察规律发现最优解
 * 3. 贪心解法：优先使用大容量袋子
 * 
 * 相关题目：
 * 1. 牛客网 - 买苹果：https://www.nowcoder.com/practice/61cfbb2e62104bc8aa3da5d44d38a6ef
 * 2. LeetCode 322. Coin Change (硬币找零)：https://leetcode.com/problems/coin-change/
 * 3. POJ 1742. Coins (多重背包)：http://poj.org/problem?id=1742
 * 4. 洛谷 P1616 疯狂的采药：https://www.luogu.com.cn/problem/P1616
 * 5. Codeforces 996A. Hit the Lottery：https://codeforces.com/problemset/problem/996/A
 * 
 * 工程化考量：
 * 1. 异常处理：处理负数输入
 * 2. 边界条件：0个苹果需要0个袋子
 * 3. 性能优化：使用数学规律O(1)解法
 * 4. 可读性：清晰的变量命名和注释
 */

class AppleMinBags {
public:
    /*
     * 方法1：动态规划解法
     * 
     * 解题思路：
     * 使用dp[i]表示装i个苹果所需的最少袋子数
     * 状态转移方程：dp[i] = min(dp[i-6]+1, dp[i-8]+1)
     * 
     * 时间复杂度：O(n)
     * 空间复杂度：O(n)
     * 
     * 优缺点分析：
     * 优点：时间复杂度较低，适合中等规模数据
     * 缺点：需要额外的O(n)空间
     * 
     * 适用场景：中等规模数据，需要准确结果的场景
     */
    static int minBagsDP(int n) {
        // 异常处理：负数输入无意义
        if (n < 0) return -1;
        
        // 边界条件：0个苹果需要0个袋子
        if (n == 0) return 0;
        
        // 创建dp数组，初始化为INT_MAX表示初始状态下无法装袋
        vector<int> dp(n + 1, INT_MAX);
        dp[0] = 0; // 0个苹果需要0个袋子
        
        // 动态规划填表，从小到大计算每个苹果数的最少袋子数
        for (int i = 1; i <= n; i++) {
            // 尝试使用6规格的袋子
            // 如果当前苹果数大于等于6，且使用6规格袋子后剩余苹果可以装袋
            if (i >= 6 && dp[i - 6] != INT_MAX) {
                dp[i] = min(dp[i], dp[i - 6] + 1);
            }
            
            // 尝试使用8规格的袋子
            // 如果当前苹果数大于等于8，且使用8规格袋子后剩余苹果可以装袋
            if (i >= 8 && dp[i - 8] != INT_MAX) {
                dp[i] = min(dp[i], dp[i - 8] + 1);
            }
        }
        
        // 如果dp[n]仍为INT_MAX，说明无法装袋，返回-1；否则返回最少袋子数
        return dp[n] == INT_MAX ? -1 : dp[n];
    }
    
    /*
     * 方法2：数学规律解法（最优解）
     * 
     * 解题思路：
     * 通过观察小规模数据的规律，发现：
     * 1. 当苹果数量为奇数时无解（因为袋子都是偶数规格）
     * 2. 当苹果数量小于18时，只有特定偶数有解
     * 3. 当苹果数量>=18时，所有偶数都有解
     * 
     * 时间复杂度：O(1)
     * 空间复杂度：O(1)
     * 
     * 优缺点分析：
     * 优点：时间空间复杂度都是O(1)，性能最优
     * 缺点：需要预先发现规律
     * 
     * 适用场景：大规模数据，对性能要求高的场景
     */
    static int minBagsMath(int n) {
        // 如果苹果数量为奇数，则无解
        // 因为6和8都是偶数，偶数个苹果无法组合成奇数个苹果
        if (n & 1) return -1; // 奇数一定无解
        
        // 当苹果数量小于18时，只有特定的偶数有解
        if (n < 18) {
            // 小数据直接查表
            switch (n) {
                case 0: return 0;  // 0个苹果需要0个袋子
                case 6: case 8: return 1;  // 6个或8个苹果需要1个袋子
                case 12: case 14: case 16: return 2;  // 12、14、16个苹果需要2个袋子
                case 18: case 20: case 22: return 3;  // 18、20、22个苹果需要3个袋子
                case 24: case 26: case 28: return 4;  // 24、26、28个苹果需要4个袋子
                default: return -1;  // 其他情况无解
            }
        }
        
        // 当苹果数量>=18时，所有偶数都有解
        // 规律：(n + 7) / 8
        // 这是一个近似公式，实际规律是(n - 18) / 2 + 3
        return (n + 7) / 8;
    }
    
    /*
     * 方法3：贪心解法
     * 
     * 解题思路：
     * 贪心策略：优先使用容量大的袋子（8规格），然后使用小容量袋子（6规格）
     * 遍历所有可能的8规格袋子数量，检查剩余苹果是否能被6整除
     * 
     * 时间复杂度：O(n/8) ≈ O(n)
     * 空间复杂度：O(1)
     * 
     * 优缺点分析：
     * 优点：思路直观，实现简单
     * 缺点：时间复杂度较高，不如数学规律解法
     * 
     * 适用场景：中等规模数据，作为动态规划的替代方案
     */
    static int minBagsGreedy(int n) {
        // 异常处理：负数输入无意义
        if (n < 0) return -1;
        
        // 边界条件：0个苹果需要0个袋子
        if (n == 0) return 0;
        
        // 优先使用8个的袋子
        // 计算最多能使用多少个8规格袋子
        int max8 = n / 8;
        
        // 遍历所有可能的8规格袋子数量，从最多到最少
        for (int i = max8; i >= 0; i--) {
            // 计算使用i个8规格袋子后剩余的苹果数
            int remaining = n - i * 8;
            
            // 如果剩余苹果数能被6整除，说明可以全部用6规格袋子装完
            if (remaining % 6 == 0) {
                // 返回总袋子数：i个8规格袋子 + remaining/6个6规格袋子
                return i + remaining / 6;
            }
        }
        
        // 如果所有组合都无法装完，则无解
        return -1;
    }
    
    // ==================== 扩展题目1: 零钱兑换 ====================
    /*
     * LeetCode 322. Coin Change
     * 题目：给定不同面额的硬币coins和总金额amount，计算凑成总金额所需的最少的硬币个数
     * 网址：https://leetcode.com/problems/coin-change/
     * 
     * 动态规划解法：
     * dp[i]表示凑成金额i所需的最少硬币数
     * 时间复杂度：O(n * amount)
     * 空间复杂度：O(amount)
     */
    static int coinChange(vector<int>& coins, int amount) {
        if (amount < 0) return -1;
        if (amount == 0) return 0;
        
        vector<int> dp(amount + 1, amount + 1);
        dp[0] = 0;
        
        for (int i = 1; i <= amount; i++) {
            for (int coin : coins) {
                if (i >= coin) {
                    dp[i] = min(dp[i], dp[i - coin] + 1);
                }
            }
        }
        
        return dp[amount] > amount ? -1 : dp[amount];
    }
    
    // ==================== 扩展题目2: 零钱兑换II ====================
    /*
     * LeetCode 518. Coin Change 2
     * 题目：计算可以凑成总金额的硬币组合数
     * 网址：https://leetcode.com/problems/coin-change-ii/
     * 
     * 动态规划解法：
     * dp[i]表示凑成金额i的组合数
     * 时间复杂度：O(n * amount)
     * 空间复杂度：O(amount)
     */
    static int coinChange2(vector<int>& coins, int amount) {
        if (amount < 0) return 0;
        
        vector<int> dp(amount + 1, 0);
        dp[0] = 1;
        
        for (int coin : coins) {
            for (int i = coin; i <= amount; i++) {
                dp[i] += dp[i - coin];
            }
        }
        
        return dp[amount];
    }
    
    // ==================== 扩展题目3: POJ 1742 Coins ====================
    /*
     * POJ 1742. Coins
     * 题目：多重背包问题，求可以凑成的金额数
     * 网址：http://poj.org/problem?id=1742
     * 
     * 多重背包解法：
     * 使用二进制优化或单调队列优化
     * 时间复杂度：O(n * amount)
     * 空间复杂度：O(amount)
     */
    static int poj1742(vector<int>& values, vector<int>& counts, int amount) {
        vector<bool> dp(amount + 1, false);
        dp[0] = true;
        
        for (int i = 0; i < values.size(); i++) {
            int value = values[i];
            int count = counts[i];
            
            // 二进制优化
            for (int k = 1; k <= count; k *= 2) {
                int currentValue = value * k;
                for (int j = amount; j >= currentValue; j--) {
                    if (dp[j - currentValue]) {
                        dp[j] = true;
                    }
                }
                count -= k;
            }
            
            if (count > 0) {
                int currentValue = value * count;
                for (int j = amount; j >= currentValue; j--) {
                    if (dp[j - currentValue]) {
                        dp[j] = true;
                    }
                }
            }
        }
        
        int result = 0;
        for (int i = 1; i <= amount; i++) {
            if (dp[i]) result++;
        }
        return result;
    }
};

// 测试函数
int main() {
    cout << "=== 苹果袋子问题测试 ===" << endl;
    for (int i = 0; i <= 20; i++) {
        int result1 = AppleMinBags::minBagsDP(i);
        int result2 = AppleMinBags::minBagsMath(i);
        int result3 = AppleMinBags::minBagsGreedy(i);
        cout << i << "个苹果: " << result1 << " / " << result2 << " / " << result3 << endl;
    }
    
    cout << "\n=== 扩展题目测试 ===" << endl;
    
    // 测试零钱兑换
    vector<int> coins1 = {1, 2, 5};
    cout << "Coin Change (11): " << AppleMinBags::coinChange(coins1, 11) << endl;
    
    // 测试零钱兑换II
    vector<int> coins2 = {1, 2, 5};
    cout << "Coin Change 2 (5): " << AppleMinBags::coinChange2(coins2, 5) << endl;
    
    // 测试POJ 1742
    vector<int> values = {1, 2, 5};
    vector<int> counts = {3, 2, 1};
    cout << "POJ 1742 (10): " << AppleMinBags::poj1742(values, counts, 10) << endl;
    
    return 0;
}