# 一和零
# 给你一个二进制字符串数组 strs 和两个整数 m 和 n
# 请你找出并返回 strs 的最大子集的长度，该子集中 最多 有 m 个 0 和 n 个 1
# 如果 x 的所有元素也是 y 的元素，集合 x 是集合 y 的 子集
# 测试链接 : https://leetcode.cn/problems/ones-and-zeroes/

"""
算法详解：
这是一个二维背包问题，其中每个字符串有两个维度的"重量"：0的个数和1的个数。
我们需要在不超过m个0和n个1的限制下，选择尽可能多的字符串。

解题思路：
1. 状态定义：dp[i][j][k]表示前i个字符串，使用不超过j个0和k个1的最大子集大小
2. 状态转移：对于每个字符串，可以选择或不选择
   - 不选择：dp[i][j][k] = dp[i-1][j][k]
   - 选择：dp[i][j][k] = dp[i-1][j-zeros][k-ones] + 1
3. 空间优化：使用滚动数组将三维优化到二维

时间复杂度分析：
设字符串数量为L，m和n为背包容量
1. 预处理每个字符串的0和1数量：O(L * avg_len)
2. 动态规划计算：O(L * m * n)
总时间复杂度：O(L * m * n)

空间复杂度分析：
1. 三维DP数组：O(L * m * n)
2. 空间优化后：O(m * n)

相关题目扩展：
1. LeetCode 474. 一和零（本题）
2. LeetCode 494. 目标和（背包变种）
3. LeetCode 1049. 最后一块石头的重量 II（背包问题）
4. LeetCode 416. 分割等和子集（背包问题）
5. 洛谷 P1757 通天之分组背包（分组背包）

工程化考量：
1. 输入验证：检查输入参数的有效性
2. 异常处理：处理空数组、非法参数等边界情况
3. 可配置性：可以将m和n作为配置参数传入
4. 单元测试：为findMaxForm方法编写测试用例
5. 性能优化：对于大数据量场景，考虑使用空间优化版本

语言特性差异：
1. Python：使用列表推导式简洁但性能较低
2. 动态类型：无需声明变量类型，但运行效率相对较低
3. 丰富库支持：有丰富的标准库和第三方库支持

调试技巧：
1. 打印dp数组中间状态，观察状态转移过程
2. 使用断言验证边界条件
3. 构造小规模测试用例手动验证结果

优化点：
1. 空间压缩：从三维dp优化到二维dp
2. 预处理优化：提前计算每个字符串的0和1数量
3. 剪枝优化：当字符串的0或1数量超过限制时跳过
"""

def findMaxForm(strs, m, n):
    """
    三维DP版本（便于理解）
    """
    length = len(strs)
    # 预处理每个字符串的0和1数量
    counts = []
    for s in strs:
        zeros = s.count('0')
        ones = len(s) - zeros
        counts.append((zeros, ones))
    
    # 初始化三维DP数组
    dp = [[[0] * (n + 1) for _ in range(m + 1)] for _ in range(length + 1)]
    
    for i in range(1, length + 1):
        zeros, ones = counts[i - 1]
        for j in range(m + 1):
            for k in range(n + 1):
                # 不选择当前字符串
                dp[i][j][k] = dp[i - 1][j][k]
                # 如果可以选择当前字符串
                if j >= zeros and k >= ones:
                    dp[i][j][k] = max(dp[i][j][k], dp[i - 1][j - zeros][k - ones] + 1)
    
    return dp[length][m][n]

def findMaxFormOptimized(strs, m, n):
    """
    空间优化版本（推荐使用）
    """
    # 预处理每个字符串的0和1数量
    counts = []
    for s in strs:
        zeros = s.count('0')
        ones = len(s) - zeros
        counts.append((zeros, ones))
    
    # 初始化二维DP数组
    dp = [[0] * (n + 1) for _ in range(m + 1)]
    
    for zeros, ones in counts:
        # 从后往前更新，避免重复使用
        for j in range(m, zeros - 1, -1):
            for k in range(n, ones - 1, -1):
                dp[j][k] = max(dp[j][k], dp[j - zeros][k - ones] + 1)
    
    return dp[m][n]

def test_ones_and_zeroes():
    """测试函数"""
    # 测试用例1
    strs1 = ["10", "0001", "111001", "1", "0"]
    m1, n1 = 5, 3
    print("测试用例1:")
    print(f"三维DP结果: {findMaxForm(strs1, m1, n1)}")
    print(f"优化版本结果: {findMaxFormOptimized(strs1, m1, n1)}")
    print("预期结果: 4")
    print()
    
    # 测试用例2
    strs2 = ["10", "0", "1"]
    m2, n2 = 1, 1
    print("测试用例2:")
    print(f"三维DP结果: {findMaxForm(strs2, m2, n2)}")
    print(f"优化版本结果: {findMaxFormOptimized(strs2, m2, n2)}")
    print("预期结果: 2")
    print()
    
    # 测试用例3：边界情况
    strs3 = []
    m3, n3 = 0, 0
    print("测试用例3（空数组）:")
    print(f"三维DP结果: {findMaxForm(strs3, m3, n3)}")
    print(f"优化版本结果: {findMaxFormOptimized(strs3, m3, n3)}")
    print("预期结果: 0")

if __name__ == "__main__":
    test_ones_and_zeroes()

"""
=============================================================================================
补充题目：LeetCode 494. 目标和（Python实现）
题目链接：https://leetcode.cn/problems/target-sum/

Python实现：
def findTargetSumWays(nums, target):
    total = sum(nums)
    
    # 边界条件检查
    if abs(target) > total:
        return 0
    if (target + total) % 2 != 0:
        return 0
    
    P = (target + total) // 2
    if P < 0:
        return 0
    
    dp = [0] * (P + 1)
    dp[0] = 1  # 和为0的方案数为1
    
    for num in nums:
        # 从后往前更新
        for j in range(P, num - 1, -1):
            dp[j] += dp[j - num]
    
    return dp[P]

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