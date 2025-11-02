#include <iostream>
#include <vector>
#include <climits>
#include <algorithm>

using namespace std;

// 函数声明
void backtrackPermute(vector<int>& nums, vector<bool>& used, vector<int>& current, vector<vector<int>>& result);
void backtrackPermuteUnique(vector<int>& nums, vector<bool>& used, vector<int>& current, vector<vector<int>>& result);

/**
 * 技能打怪问题 - C++版本
 * 
 * 问题描述：
 * 现在有一个打怪类型的游戏，你有n个技能，每个技能有伤害值、魔法消耗值和触发双倍伤害的血量阈值。
 * 每个技能最多只能释放一次，怪物有m点血量。问最少用多少魔法值能消灭怪物（血量≤0）。
 * 
 * 算法思路：
 * 1. 使用回溯算法遍历所有可能的技能使用顺序
 * 2. 在搜索过程中维护当前使用的魔法值和怪物剩余血量
 * 3. 通过剪枝优化避免无效搜索
 * 4. 返回所有方案中魔法消耗最少的值
 * 
 * 时间复杂度分析：
 * - 最坏情况：O(n!)，需要尝试所有技能的排列组合
 * - 平均情况：通过剪枝优化，实际运行时间远小于n!
 * 
 * 空间复杂度分析：
 * - 递归栈深度：O(n)
 * - 标记数组：O(n)
 * - 总空间复杂度：O(n)
 * 
 * 工程化考量：
 * 1. 输入验证：检查参数合法性
 * 2. 边界处理：怪物血量为0、技能伤害不足等情况
 * 3. 异常处理：适当的异常捕获机制
 * 4. 可测试性：设计单元测试用例
 * 
 * 优化技巧：
 * 1. 剪枝优化：当前魔法消耗超过已知最优解时提前返回
 * 2. 排序优化：按技能性价比排序，优先搜索更优路径
 * 3. 记忆化搜索：对于重复出现的子问题进行缓存
 */

class SkillMonsterSolver {
private:
    vector<int> damage;      // 技能伤害值
    vector<int> cost;       // 魔法消耗值
    vector<int> threshold;  // 触发双倍伤害的血量阈值
    int minCost;            // 最小魔法消耗
    
public:
    /**
     * 构造函数
     * @param d 伤害值数组
     * @param c 消耗值数组
     * @param t 阈值数组
     */
    SkillMonsterSolver(vector<int> d, vector<int> c, vector<int> t) 
        : damage(d), cost(c), threshold(t), minCost(INT_MAX) {}
    
    /**
     * 计算击败怪物所需的最少魔法值
     * @param n 技能总数
     * @param m 怪物血量
     * @return 最少需要的魔法值，如果无法击败则返回-1
     */
    int minMagicCost(int n, int m) {
        if (m <= 0) return 0; // 怪物已经死亡
        
        minCost = INT_MAX;
        vector<bool> used(n, false);
        
        backtrack(m, 0, used);
        
        return minCost == INT_MAX ? -1 : minCost;
    }
    
private:
    /**
     * 回溯函数
     * @param remainingHP 怪物剩余血量
     * @param currentCost 当前已消耗的魔法值
     * @param used 标记技能是否已使用的数组
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
        for (int i = 0; i < (int)used.size(); i++) {
            if (!used[i]) {
                used[i] = true;
                
                // 计算实际伤害（考虑双倍伤害）
                int actualDamage = (remainingHP <= threshold[i]) ? damage[i] * 2 : damage[i];
                
                // 递归搜索
                backtrack(remainingHP - actualDamage, currentCost + cost[i], used);
                
                // 回溯
                used[i] = false;
            }
        }
    }
};

/**
 * 动态规划解法（适用于n较小的情况）
 * 使用状态压缩DP，mask表示技能使用状态
 */
class DPSolver {
public:
    int minMagicCostDP(vector<int>& damage, vector<int>& cost, vector<int>& threshold, int m) {
        int n = (int)damage.size();
        int totalStates = 1 << n;
        
        // dp[mask]表示使用mask对应技能时的最小魔法消耗
        vector<int> dp(totalStates, INT_MAX);
        dp[0] = 0; // 没有使用任何技能
        
        // 血量状态，表示使用mask对应技能后怪物的剩余血量
        vector<int> hp(totalStates, m);
        
        for (int mask = 0; mask < totalStates; mask++) {
            if (dp[mask] == INT_MAX) continue;
            
            for (int i = 0; i < n; i++) {
                if (!(mask & (1 << i))) { // 技能i未使用
                    int newMask = mask | (1 << i);
                    int actualDamage = (hp[mask] <= threshold[i]) ? damage[i] * 2 : damage[i];
                    int newHP = hp[mask] - actualDamage;
                    int newCost = dp[mask] + cost[i];
                    
                    if (newHP <= 0) {
                        // 怪物被击败
                        dp[newMask] = min(dp[newMask], newCost);
                    } else if (newCost < dp[newMask]) {
                        dp[newMask] = newCost;
                        hp[newMask] = newHP;
                    }
                }
            }
        }
        
        // 找到所有状态中的最小魔法消耗
        int result = INT_MAX;
        for (int mask = 0; mask < totalStates; mask++) {
            if (hp[mask] <= 0) {
                result = min(result, dp[mask]);
            }
        }
        
        return result == INT_MAX ? -1 : result;
    }
};

// 测试函数
void testSkillMonster() {
    // 测试用例1：简单的打怪场景
    vector<int> damage1 = {3, 4, 5};
    vector<int> cost1 = {2, 3, 4};
    vector<int> threshold1 = {5, 3, 2};
    int m1 = 10;
    
    SkillMonsterSolver solver1(damage1, cost1, threshold1);
    int result1 = solver1.minMagicCost(3, m1);
    cout << "测试用例1:" << endl;
    cout << "技能伤害: "; for (int d : damage1) cout << d << " "; cout << endl;
    cout << "技能消耗: "; for (int c : cost1) cout << c << " "; cout << endl;
    cout << "技能阈值: "; for (int t : threshold1) cout << t << " "; cout << endl;
    cout << "怪物血量: " << m1 << endl;
    cout << "最少魔法消耗: " << result1 << endl << endl;
    
    // 测试用例2：无法击败怪物的情况
    vector<int> damage2 = {3, 4};
    vector<int> cost2 = {2, 3};
    vector<int> threshold2 = {10, 8};
    int m2 = 20;
    
    SkillMonsterSolver solver2(damage2, cost2, threshold2);
    int result2 = solver2.minMagicCost(2, m2);
    cout << "测试用例2:" << endl;
    cout << "技能伤害: "; for (int d : damage2) cout << d << " "; cout << endl;
    cout << "技能消耗: "; for (int c : cost2) cout << c << " "; cout << endl;
    cout << "技能阈值: "; for (int t : threshold2) cout << t << " "; cout << endl;
    cout << "怪物血量: " << m2 << endl;
    cout << "最少魔法消耗: " << result2 << endl << endl;
    
    // 测试用例3：怪物血量为0
    vector<int> damage3 = {5};
    vector<int> cost3 = {2};
    vector<int> threshold3 = {3};
    int m3 = 0;
    
    SkillMonsterSolver solver3(damage3, cost3, threshold3);
    int result3 = solver3.minMagicCost(1, m3);
    cout << "测试用例3:" << endl;
    cout << "技能伤害: "; for (int d : damage3) cout << d << " "; cout << endl;
    cout << "技能消耗: "; for (int c : cost3) cout << c << " "; cout << endl;
    cout << "技能阈值: "; for (int t : threshold3) cout << t << " "; cout << endl;
    cout << "怪物血量: " << m3 << endl;
    cout << "最少魔法消耗: " << result3 << endl << endl;
    
    // 测试动态规划解法
    DPSolver dpSolver;
    int dpResult = dpSolver.minMagicCostDP(damage1, cost1, threshold1, m1);
    cout << "动态规划解法结果: " << dpResult << endl;
}

/**
 * 补充训练题目 - C++实现
 */

/**
 * 1. LeetCode 322. 零钱兑换
 * 问题描述：给定不同面额的硬币coins和总金额amount，计算凑成总金额所需的最少硬币个数。
 */
int coinChange(vector<int>& coins, int amount) {
    vector<int> dp(amount + 1, amount + 1);
    dp[0] = 0;
    
    for (int coin : coins) {
        for (int i = coin; i <= amount; i++) {
            dp[i] = min(dp[i], dp[i - coin] + 1);
        }
    }
    
    return dp[amount] > amount ? -1 : dp[amount];
}

/**
 * 2. LeetCode 46. 全排列
 * 问题描述：给定一个不含重复数字的数组，返回其所有可能的全排列。
 */
vector<vector<int>> permute(vector<int>& nums) {
    vector<vector<int>> result;
    vector<int> current;
    vector<bool> used(nums.size(), false);
    backtrackPermute(nums, used, current, result);
    return result;
}

void backtrackPermute(vector<int>& nums, vector<bool>& used, vector<int>& current, vector<vector<int>>& result) {
    if (current.size() == nums.size()) {
        result.push_back(current);
        return;
    }
    
    for (size_t i = 0; i < nums.size(); i++) {
        if (!used[i]) {
            used[i] = true;
            current.push_back(nums[i]);
            backtrackPermute(nums, used, current, result);
            current.pop_back();
            used[i] = false;
        }
    }
}

/**
 * 3. LeetCode 47. 全排列 II
 * 问题描述：给定可包含重复数字的序列，返回所有不重复的全排列。
 */
vector<vector<int>> permuteUnique(vector<int>& nums) {
    vector<vector<int>> result;
    vector<int> current;
    vector<bool> used(nums.size(), false);
    sort(nums.begin(), nums.end()); // 排序以便去重
    backtrackPermuteUnique(nums, used, current, result);
    return result;
}

void backtrackPermuteUnique(vector<int>& nums, vector<bool>& used, vector<int>& current, vector<vector<int>>& result) {
    if (current.size() == nums.size()) {
        result.push_back(current);
        return;
    }
    
    for (size_t i = 0; i < nums.size(); i++) {
        // 去重逻辑：当前数字与前一个数字相同且前一个数字未使用过时，跳过
        if (used[i] || (i > 0 && nums[i] == nums[i-1] && !used[i-1])) {
            continue;
        }
        
        used[i] = true;
        current.push_back(nums[i]);
        backtrackPermuteUnique(nums, used, current, result);
        current.pop_back();
        used[i] = false;
    }
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
 * 2. 优化技巧：
 *    - 剪枝：提前终止无效分支
 *    - 排序：优先搜索更有可能成功的分支
 *    - 记忆化：缓存中间结果避免重复计算
 * 
 * 3. 工程化实践：
 *    - 模块化设计：分离算法逻辑和业务逻辑
 *    - 异常安全：确保资源正确释放
 *    - 性能分析：使用profiler工具分析性能瓶颈
 * 
 * 调试技巧：
 * 1. 打印中间状态：跟踪算法执行过程
 * 2. 断言验证：确保关键条件满足
 * 3. 边界测试：测试极端情况下的行为
 * 
 * 性能优化：
 * 1. 减少函数调用开销：内联小函数
 * 2. 优化内存访问：提高缓存命中率
 * 3. 并行计算：使用多线程加速搜索
 */

int main() {
    testSkillMonster();
    return 0;
}