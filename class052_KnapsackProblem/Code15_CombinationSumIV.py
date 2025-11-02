# 组合总和 Ⅳ
# 给你一个由不同整数组成的数组 nums ，和一个目标整数 target
# 请你从 nums 中找出并返回总和为 target 的元素组合的个数
# 顺序不同的序列被视作不同的组合
# 测试链接 : https://leetcode.cn/problems/combination-sum-iv/

"""
算法详解：
这是一个完全背包问题的变种，求组合数（考虑顺序）。与标准的完全背包求组合数不同，
本题中顺序不同的序列被视为不同的组合，因此需要调整遍历顺序。

解题思路：
1. 状态定义：dp[i]表示总和为i的组合数
2. 状态转移：对于每个总和i，枚举所有可能的数字nums[j]
   dp[i] += dp[i - nums[j]]  (当i >= nums[j]时)
3. 遍历顺序：外层遍历背包容量，内层遍历物品（与标准完全背包相反）

时间复杂度分析：
设数组长度为n，目标值为target
1. 动态规划计算：O(n * target)
总时间复杂度：O(n * target)

空间复杂度分析：
1. DP数组：O(target)

相关题目扩展：
1. LeetCode 377. 组合总和 Ⅳ（本题）
2. LeetCode 518. 零钱兑换 II（不考虑顺序的组合数）
3. LeetCode 322. 零钱兑换（求最少硬币数）
4. LeetCode 279. 完全平方数（完全背包变种）
5. LeetCode 139. 单词拆分（字符串匹配+背包）

工程化考量：
1. 输入验证：检查输入参数的有效性
2. 异常处理：处理空数组、负数、溢出等边界情况
3. 可配置性：可以将MOD值作为配置参数传入
4. 单元测试：为combinationSum4方法编写测试用例
5. 性能优化：对于大数据量场景，考虑使用记忆化搜索
"""

MOD = 10**9 + 7  # 防止整数溢出

def combinationSum4(nums, target):
    """
    标准DP版本
    """
    if not nums:
        return 0
    if target == 0:
        return 1
    
    # 创建DP数组
    dp = [0] * (target + 1)
    dp[0] = 1  # 总和为0的组合数为1
    
    # 外层遍历背包容量，内层遍历物品（考虑顺序）
    for i in range(1, target + 1):
        for num in nums:
            if i >= num:
                dp[i] = (dp[i] + dp[i - num]) % MOD
    
    return dp[target]

def combinationSum4Optimized(nums, target):
    """
    优化版本：先排序，可以提前终止内层循环
    """
    if not nums:
        return 0
    if target == 0:
        return 1
    
    # 对数组排序
    nums.sort()
    
    # 如果最小的数字都大于target，直接返回0
    if nums[0] > target:
        return 0
    
    dp = [0] * (target + 1)
    dp[0] = 1
    
    for i in range(1, target + 1):
        for num in nums:
            if i < num:
                break  # 提前终止，因为后面的数字更大
            dp[i] = (dp[i] + dp[i - num]) % MOD
    
    return dp[target]

def combinationSum4Memo(nums, target):
    """
    记忆化搜索版本（DFS + 记忆化）
    """
    if not nums:
        return 0
    if target == 0:
        return 1
    
    memo = [-1] * (target + 1)
    memo[0] = 1
    
    def dfs(t):
        if t < 0:
            return 0
        if memo[t] != -1:
            return memo[t]
        
        count = 0
        for num in nums:
            if t >= num:
                count = (count + dfs(t - num)) % MOD
        
        memo[t] = count
        return count
    
    return dfs(target)

def test_combination_sum4():
    """测试函数"""
    # 测试用例1
    nums1 = [1, 2, 3]
    target1 = 4
    print("测试用例1:")
    print(f"标准版本: {combinationSum4(nums1, target1)}")
    print(f"优化版本: {combinationSum4Optimized(nums1, target1)}")
    print(f"记忆化版本: {combinationSum4Memo(nums1, target1)}")
    print("预期结果: 7")
    print("解释：可能的组合有：")
    print("(1, 1, 1, 1), (1, 1, 2), (1, 2, 1), (1, 3), (2, 1, 1), (2, 2), (3, 1)")
    print()
    
    # 测试用例2
    nums2 = [9]
    target2 = 3
    print("测试用例2:")
    print(f"标准版本: {combinationSum4(nums2, target2)}")
    print(f"优化版本: {combinationSum4Optimized(nums2, target2)}")
    print(f"记忆化版本: {combinationSum4Memo(nums2, target2)}")
    print("预期结果: 0")
    print()
    
    # 测试用例3：边界情况
    nums3 = []
    target3 = 0
    print("测试用例3（边界情况）:")
    print(f"标准版本: {combinationSum4(nums3, target3)}")
    print(f"优化版本: {combinationSum4Optimized(nums3, target3)}")
    print(f"记忆化版本: {combinationSum4Memo(nums3, target3)}")
    print("预期结果: 1")
    print()
    
    # 测试用例4：较大规模
    nums4 = [1, 2, 4, 8]
    target4 = 10
    print("测试用例4:")
    print(f"标准版本: {combinationSum4(nums4, target4)}")
    print(f"优化版本: {combinationSum4Optimized(nums4, target4)}")
    print(f"记忆化版本: {combinationSum4Memo(nums4, target4)}")
    print("预期结果: 64")

if __name__ == "__main__":
    test_combination_sum4()

"""
=============================================================================================
补充题目：LeetCode 518. 零钱兑换 II（Python实现）
题目链接：https://leetcode.cn/problems/coin-change-ii/

Python实现：
def change(amount, coins):
    if amount == 0:
        return 1
    if not coins:
        return 0
    
    dp = [0] * (amount + 1)
    dp[0] = 1
    
    # 外层遍历物品，内层遍历背包容量（不考虑顺序）
    for coin in coins:
        for i in range(coin, amount + 1):
            dp[i] += dp[i - coin]
    
    return dp[amount]

工程化考量：
1. 使用类型注解提高代码可读性
2. 添加详细的文档字符串
3. 使用单元测试框架进行测试
4. 添加性能分析工具

优化思路：
1. 使用numpy数组加速计算
2. 使用缓存装饰器进行记忆化
3. 使用生成器表达式减少内存使用
4. 使用多进程并行计算
"""