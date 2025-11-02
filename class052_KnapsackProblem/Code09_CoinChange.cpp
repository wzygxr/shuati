// 零钱兑换
// 给你一个整数数组 coins ，表示不同面额的硬币；以及一个整数 amount ，表示总金额。
// 计算并返回可以凑成总金额所需的最少的硬币个数。如果没有任何一种硬币组合能组成总金额，返回 -1 。
// 你可以认为每种硬币的数量是无限的。
// 测试链接 : https://leetcode.cn/problems/coin-change/

/*
 * 算法详解：
 * 这是一个典型的完全背包问题。每种硬币可以使用无限次，我们需要找出组成指定金额所需的最少硬币数。
 * 
 * 解题思路：
 * 1. 状态定义：dp[j]表示组成金额j所需的最少硬币数
 * 2. 状态转移方程：
 *    dp[j] = min(dp[j], dp[j-coins[i]] + 1)
 * 3. 初始化：dp[0] = 0，表示组成金额0需要0个硬币；其余初始化为无穷大
 * 
 * 时间复杂度分析：
 * 设有n种硬币，总金额为amount
 * 1. 动态规划计算：O(n * amount)
 * 总时间复杂度：O(n * amount)
 * 
 * 空间复杂度分析：
 * 1. 一维DP数组：O(amount)
 * 
 * 相关题目扩展：
 * 1. LeetCode 322. 零钱兑换（本题）
 * 2. LeetCode 518. 零钱兑换 II（完全背包求方案数）
 * 3. LeetCode 279. 完全平方数（完全背包）
 * 4. 洛谷 P1616 疯狂的采药（完全背包模板题）
 * 5. 洛谷 P2918 购买干草（完全背包变种）
 * 
 * 工程化考量：
 * 1. 输入验证：检查输入参数的有效性
 * 2. 异常处理：处理空输入、非法输入等边界情况
 * 3. 可配置性：可以将MAX_AMOUNT作为配置参数传入
 * 4. 单元测试：为coinChange方法编写测试用例
 * 5. 性能优化：对于大数据量场景，考虑使用滚动数组优化空间
 * 
 * 语言特性差异：
 * 1. Java：使用静态数组提高访问速度
 * 2. C++：可以使用vector，但要注意内存分配开销
 * 3. Python：列表推导式简洁但性能较低
 * 
 * 调试技巧：
 * 1. 打印dp数组中间状态，观察状态转移过程
 * 2. 使用断言验证边界条件
 * 3. 构造小规模测试用例手动验证结果
 * 
 * 优化点：
 * 1. 剪枝优化：当硬币面额大于当前金额时跳过
 * 2. 排序优化：先处理大面额硬币可能减少计算量
 * 3. 提前终止：当发现无法组成目标金额时提前返回
 * 
 * 与标准背包的区别：
 * 1. 目标函数：标准背包求最大价值，本题求最小硬币数
 * 2. 状态初始化：标准背包dp[0] = 0，其余为0；本题dp[0] = 0，其余为无穷大
 * 3. 状态转移：标准背包取max，本题取min
 */

const int MAX_AMOUNT = 10001;

// dp[j]表示组成金额j所需的最少硬币数
int dp[MAX_AMOUNT];

// 简单的最小值函数
int min(int a, int b) {
    return a < b ? a : b;
}

int coinChange(int* coins, int coinsSize, int amount) {
    // 初始化
    for (int j = 1; j <= amount; j++) {
        dp[j] = 2147483647; // 使用最大整数值代替无穷大
    }
    dp[0] = 0;
    
    // 动态规划填表
    for (int i = 0; i < coinsSize; i++) {
        for (int j = coins[i]; j <= amount; j++) {
            if (dp[j - coins[i]] != 2147483647) {
                dp[j] = min(dp[j], dp[j - coins[i]] + 1);
            }
        }
    }
    
    return dp[amount] == 2147483647 ? -1 : dp[amount];
}

// 简单的输出函数
void print_result(char* msg, int result) {
    // 简化输出，实际使用时可以替换为printf或其他输出方式
    // 这里只是示意
}

// 测试方法
int main() {
    // 测试用例1: coins = [1, 3, 4], amount = 6
    // 预期输出: 2 (3+3=6)
    int coins1[] = {1, 3, 4};
    int result1 = coinChange(coins1, 3, 6);
    
    // 测试用例2: coins = [2], amount = 3
    // 预期输出: -1 (无法组成)
    int coins2[] = {2};
    int result2 = coinChange(coins2, 1, 3);
    
    // 测试用例3: coins = [1], amount = 0
    // 预期输出: 0 (金额为0不需要硬币)
    int coins3[] = {1};
    int result3 = coinChange(coins3, 1, 0);
    
    return 0;
}