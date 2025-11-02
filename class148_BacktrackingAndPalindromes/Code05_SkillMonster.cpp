#include <iostream>
#include <vector>
#include <algorithm>
#include <climits>

using namespace std;

/**
 * 技能打怪问题（升级版） - C++版本
 * 
 * 问题描述：
 * 现在有一个打怪类型的游戏，你有n个技能，每个技能有伤害值、魔法消耗值和触发双倍伤害的血量阈值。
 * 每个技能最多只能释放一次，怪物有m点血量。问如何用最少的魔法值消灭怪物（血量≤0）。
 * 
 * 算法思路：
 * 方法1：回溯算法 - 遍历所有可能的技能使用顺序
 * 方法2：动态规划 - 使用状态压缩DP
 * 方法3：贪心+回溯 - 按技能性价比排序优化
 * 
 * 时间复杂度分析：
 * - 回溯算法：O(n!)，需要尝试所有技能的排列组合
 * - 动态规划：O(n * 2^n)，状态压缩DP
 * - 贪心+回溯：O(n!)，但通过剪枝优化实际运行更快
 * 
 * 空间复杂度分析：
 * - 回溯算法：O(n)，递归栈深度
 * - 动态规划：O(2^n)，DP数组空间
 * - 贪心+回溯：O(n)，递归栈深度
 * 
 * 工程化考量：
 * 1. 输入验证：检查参数合法性
 * 2. 边界处理：怪物血量为0、技能伤害不足等情况
 * 3. 性能优化：剪枝、排序、缓存等优化技术
 * 4. 可测试性：设计全面的测试用例
 */

class SkillMonsterAdvancedSolver {
private:
    vector<int> damage;      // 技能伤害值
    vector<int> cost;        // 魔法消耗值
    vector<int> threshold;  // 触发双倍伤害的血量阈值
    int minCost;            // 最小魔法消耗
    
public:
    /**
     * 构造函数
     */
    SkillMonsterAdvancedSolver(vector<int> d, vector<int> c, vector<int> t) 
        : damage(d), cost(c), threshold(t), minCost(INT_MAX) {}
    
    /**
     * 方法1：回溯算法
     * 遍历所有可能的技能使用顺序
     */
    int minMagicCost(int n, int m) {
        if (m <= 0) return 0;
        
        minCost = INT_MAX;
        vector<bool> used(n, false);
        
        backtrack(m, 0, used);
        
        return minCost == INT_MAX ? -1 : minCost;
    }
    
    /**
     * 方法2：动态规划解法
     * 使用状态压缩DP，mask表示技能使用状态
     */
    int minMagicCostDP(int n, int m) {
        int totalStates = 1 << n;
        
        // dp[mask]表示使用mask对应技能时的最小魔法消耗
        vector<int> dp(totalStates, INT_MAX);
        dp[0] = 0; // 没有使用任何技能
        
        // hp[mask]表示使用mask对应技能后怪物的剩余血量
        vector<int> hp(totalStates, m);
        
        for (int mask = 0; mask < totalStates; mask++) {
            if (dp[mask] == INT_MAX) continue;
            
            for (int i = 0; i < n; i++) {
                if (!(mask & (1 << i))) { // 技能i未使用
                    int newMask = mask | (1 << i);
                    int actualDamage = calculateDamage(hp[mask], i);
                    int newHP = hp[mask] - actualDamage;
                    int newCost = dp[mask] + cost[i];
                    
                    if (newHP <= 0) {
                        // 怪物被击败
                        if (newCost < dp[newMask]) {
                            dp[newMask] = newCost;
                        }
                    } else {
                        if (newCost < dp[newMask]) {
                            dp[newMask] = newCost;
                            hp[newMask] = newHP;
                        }
                    }
                }
            }
        }
        
        // 找到所有状态中的最小魔法消耗
        int result = INT_MAX;
        for (int mask = 0; mask < totalStates; mask++) {
            if (hp[mask] <= 0 && dp[mask] < result) {
                result = dp[mask];
            }
        }
        
        return result == INT_MAX ? -1 : result;
    }
    
    /**
     * 方法3：贪心+回溯优化
     * 按技能性价比排序，优先尝试高性价比技能
     */
    int minMagicCostGreedy(int n, int m) {
        if (m <= 0) return 0;
        
        // 计算技能性价比（伤害/消耗）
        vector<pair<int, double>> skills;
        for (int i = 0; i < n; i++) {
            double efficiency = (cost[i] > 0) ? (double)damage[i] / cost[i] : INT_MAX;
            skills.push_back({i, efficiency});
        }
        
        // 按性价比降序排序
        sort(skills.begin(), skills.end(), [](const pair<int, double>& a, const pair<int, double>& b) {
            return a.second > b.second;
        });
        
        minCost = INT_MAX;
        vector<bool> used(n, false);
        
        backtrackGreedy(m, 0, used, skills);
        
        return minCost == INT_MAX ? -1 : minCost;
    }
    
private:
    /**
     * 回溯函数
     */
    void backtrack(int remainingHP, int currentCost, vector<bool>& used) {
        // 基础情况：怪物已被击败
        if (remainingHP <= 0) {
            if (currentCost < minCost) {
                minCost = currentCost;
            }
            return;
        }
        
        // 剪枝优化：如果当前魔法消耗已经超过已知最优解，提前返回
        if (currentCost >= minCost) {
            return;
        }
        
        // 尝试使用每个未使用的技能
        for (int i = 0; i < used.size(); i++) {
            if (!used[i]) {
                used[i] = true;
                
                int actualDamage = calculateDamage(remainingHP, i);
                backtrack(remainingHP - actualDamage, currentCost + cost[i], used);
                
                used[i] = false;
            }
        }
    }
    
    /**
     * 贪心优化的回溯函数
     */
    void backtrackGreedy(int remainingHP, int currentCost, vector<bool>& used, 
                        vector<pair<int, double>>& skills) {
        if (remainingHP <= 0) {
            if (currentCost < minCost) {
                minCost = currentCost;
            }
            return;
        }
        
        if (currentCost >= minCost) {
            return;
        }
        
        // 按性价比顺序尝试技能
        for (auto& skill : skills) {
            int i = skill.first;
            if (!used[i]) {
                used[i] = true;
                
                int actualDamage = calculateDamage(remainingHP, i);
                backtrackGreedy(remainingHP - actualDamage, currentCost + cost[i], used, skills);
                
                used[i] = false;
            }
        }
    }
    
    /**
     * 计算实际伤害（考虑双倍伤害）
     */
    int calculateDamage(int remainingHP, int skillIndex) {
        return (remainingHP <= threshold[skillIndex]) ? damage[skillIndex] * 2 : damage[skillIndex];
    }
};

/**
 * 补充训练题目 - C++实现
 */

/**
 * LeetCode 322. 零钱兑换
 * 给定不同面额的硬币coins和总金额amount，计算凑成总金额所需的最少硬币个数。
 */
int coinChange(vector<int>& coins, int amount) {
    vector<int> dp(amount + 1, amount + 1);
    dp[0] = 0;
    
    for (int coin : coins) {
        for (int i = coin; i <= amount; i++) {
            if (dp[i - coin] != amount + 1) {
                dp[i] = min(dp[i], dp[i - coin] + 1);
            }
        }
    }
    
    return dp[amount] > amount ? -1 : dp[amount];
}

/**
 * LeetCode 518. 零钱兑换 II
 * 给定不同面额的硬币和一个总金额，计算可以凑成总金额的硬币组合数。
 */
int change(int amount, vector<int>& coins) {
    vector<int> dp(amount + 1, 0);
    dp[0] = 1;
    
    for (int coin : coins) {
        for (int i = coin; i <= amount; i++) {
            dp[i] += dp[i - coin];
        }
    }
    
    return dp[amount];
}

/**
 * LeetCode 279. 完全平方数
 * 给定正整数n，找到若干个完全平方数使得它们的和等于n，返回需要的最少完全平方数。
 */
int numSquares(int n) {
    vector<int> dp(n + 1, INT_MAX);
    dp[0] = 0;
    
    for (int i = 1; i <= n; i++) {
        for (int j = 1; j * j <= i; j++) {
            dp[i] = min(dp[i], dp[i - j * j] + 1);
        }
    }
    
    return dp[n];
}

/**
 * LeetCode 377. 组合总和 Ⅳ
 * 给定一个由正整数组成且不存在重复数字的数组，找出和为给定目标正整数的组合的个数。
 */
int combinationSum4(vector<int>& nums, int target) {
    vector<unsigned int> dp(target + 1, 0);
    dp[0] = 1;
    
    for (int i = 1; i <= target; i++) {
        for (int num : nums) {
            if (i >= num) {
                dp[i] += dp[i - num];
            }
        }
    }
    
    return dp[target];
}

/**
 * LeetCode 416. 分割等和子集
 * 给定一个只包含正整数的非空数组，判断是否可以将这个数组分割成两个子集，使得两个子集的元素和相等。
 */
bool canPartition(vector<int>& nums) {
    int sum = 0;
    for (int num : nums) {
        sum += num;
    }
    
    if (sum % 2 != 0) return false;
    
    int target = sum / 2;
    vector<bool> dp(target + 1, false);
    dp[0] = true;
    
    for (int num : nums) {
        for (int i = target; i >= num; i--) {
            dp[i] = dp[i] || dp[i - num];
        }
    }
    
    return dp[target];
}

// 测试函数
void testSkillMonsterAdvanced() {
    // 测试用例1：简单的打怪场景
    vector<int> damage1 = {3, 4, 5};
    vector<int> cost1 = {2, 3, 4};
    vector<int> threshold1 = {5, 3, 2};
    int m1 = 10;
    
    SkillMonsterAdvancedSolver solver1(damage1, cost1, threshold1);
    int result1 = solver1.minMagicCost(3, m1);
    int result1_dp = solver1.minMagicCostDP(3, m1);
    int result1_greedy = solver1.minMagicCostGreedy(3, m1);
    
    cout << "测试用例1:" << endl;
    cout << "技能伤害: "; for (int d : damage1) cout << d << " "; cout << endl;
    cout << "技能消耗: "; for (int c : cost1) cout << c << " "; cout << endl;
    cout << "技能阈值: "; for (int t : threshold1) cout << t << " "; cout << endl;
    cout << "怪物血量: " << m1 << endl;
    cout << "回溯算法结果: " << result1 << endl;
    cout << "动态规划结果: " << result1_dp << endl;
    cout << "贪心优化结果: " << result1_greedy << endl;
    cout << endl;
    
    // 测试用例2：无法击败怪物的情况
    vector<int> damage2 = {3, 4};
    vector<int> cost2 = {2, 3};
    vector<int> threshold2 = {10, 8};
    int m2 = 20;
    
    SkillMonsterAdvancedSolver solver2(damage2, cost2, threshold2);
    int result2 = solver2.minMagicCost(2, m2);
    
    cout << "测试用例2:" << endl;
    cout << "技能伤害: "; for (int d : damage2) cout << d << " "; cout << endl;
    cout << "技能消耗: "; for (int c : cost2) cout << c << " "; cout << endl;
    cout << "技能阈值: "; for (int t : threshold2) cout << t << " "; cout << endl;
    cout << "怪物血量: " << m2 << endl;
    cout << "最少魔法消耗: " << result2 << endl;
    cout << endl;
    
    // 测试补充题目
    cout << "=== 补充训练题目测试 ===" << endl;
    
    // 测试零钱兑换
    vector<int> coins = {1, 2, 5};
    int amount = 11;
    cout << "零钱兑换: coins={"; for (int c : coins) cout << c << " "; cout << "}, amount=" << amount;
    cout << ", 结果=" << coinChange(coins, amount) << endl;
    
    // 测试零钱兑换II
    cout << "零钱兑换II: coins={"; for (int c : coins) cout << c << " "; cout << "}, amount=" << amount;
    cout << ", 结果=" << change(amount, coins) << endl;
    
    // 测试完全平方数
    int n = 12;
    cout << "完全平方数: n=" << n << ", 结果=" << numSquares(n) << endl;
    
    // 测试组合总和IV
    vector<int> nums = {1, 2, 3};
    int target = 4;
    cout << "组合总和IV: nums={"; for (int num : nums) cout << num << " "; cout << "}, target=" << target;
    cout << ", 结果=" << combinationSum4(nums, target) << endl;
    
    // 测试分割等和子集
    vector<int> partitionNums = {1, 5, 11, 5};
    cout << "分割等和子集: nums={"; for (int num : partitionNums) cout << num << " "; cout << "}";
    cout << ", 结果=" << (canPartition(partitionNums) ? "true" : "false") << endl;
}

int main() {
    testSkillMonsterAdvanced();
    return 0;
}

/**
 * 算法技巧总结 - C++版本
 * 
 * 核心概念：
 * 1. 回溯算法框架：
 *    - 状态定义：当前选择、剩余选择、目标状态
 *    - 选择策略：遍历所有可能的选择
 *    - 终止条件：达到目标状态或无法继续
 *    - 回溯操作：撤销当前选择，尝试其他选择
 * 
 * 2. 动态规划技术：
 *    - 状态压缩：使用位运算表示状态
 *    - 状态转移：根据当前状态推导下一状态
 *    - 边界处理：处理初始状态和终止状态
 * 
 * 3. 优化技巧：
 *    - 剪枝优化：提前终止无效分支
 *    - 排序优化：优先搜索更优路径
 *    - 贪心策略：局部最优选择
 * 
 * 调试技巧：
 * 1. 打印中间状态：跟踪算法执行过程
 * 2. 边界值测试：测试各种边界情况
 * 3. 性能分析：比较不同算法的执行时间
 * 
 * 工程化实践：
 * 1. 模块化设计：分离算法逻辑和业务逻辑
 * 2. 异常安全：确保资源正确释放
 * 3. 代码可读性：使用有意义的变量名和注释
 */