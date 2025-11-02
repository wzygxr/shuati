# 掷骰子的N种方法
# 这里有 n 个一样的骰子，每个骰子上都有 k 个面，分别标号为 1 到 k 。
# 给定三个整数 n、k 和 target，请返回可能的方式(从总共 kn 种方式中)滚动骰子，
# 使得骰子面朝上的数字总和等于 target。
# 由于答案可能很大，你需要对 10^9 + 7 取模。
# 测试链接 : https://leetcode.cn/problems/number-of-dice-rolls-with-target-sum/

'''
算法详解：
这是一个典型的分组背包问题。每个骰子可以看作一个组，每个组有k个物品（骰子的面值1到k），
且每个组必须选择一个物品。我们需要计算恰好装满容量为target的背包的方案数。

解题思路：
1. 状态定义：dp[i][j]表示使用前i个骰子，使得点数和为j的方案数
2. 状态转移方程：
   dp[i][j] = sum(dp[i-1][j-x]) 其中x是第i个骰子的点数（1到k）
3. 初始化：dp[0][0] = 1，表示不使用骰子得到和为0的方案数为1

时间复杂度分析：
设有n个骰子，每个骰子有k个面，目标和为target
1. 动态规划计算：O(n * target * k)
总时间复杂度：O(n * target * k)

空间复杂度分析：
1. 二维DP数组：O(n * target)
2. 空间优化后：O(target)

相关题目扩展：
1. LeetCode 1155. 掷骰子的N种方法（本题）
2. HDU 1712 ACboy needs your help（分组背包模板题）
3. 洛谷 P1757 通天之分组背包
4. LeetCode 322. 零钱兑换（完全背包）
5. LeetCode 279. 完全平方数（完全背包）

工程化考量：
1. 输入验证：检查输入参数的有效性
2. 异常处理：处理空输入、非法输入等边界情况
3. 可配置性：可以将MOD作为配置参数传入
4. 单元测试：为numRollsToTarget1和numRollsToTarget2方法编写测试用例
5. 性能优化：对于大数据量场景，考虑使用滚动数组优化空间

语言特性差异：
1. Java：使用静态数组提高访问速度
2. C++：可以使用vector，但要注意内存分配开销
3. Python：列表推导式简洁但性能较低

调试技巧：
1. 打印dp数组中间状态，观察状态转移过程
2. 使用断言验证边界条件
3. 构造小规模测试用例手动验证结果

优化点：
1. 空间压缩：从二维dp优化到一维dp
2. 剪枝优化：当j小于当前骰子最小点数或大于最大点数时跳过
3. 前缀和优化：使用前缀和加速状态转移计算

与标准分组背包的区别：
1. 选择限制：标准分组背包每组最多选一个物品，本题每组必须选一个物品
2. 目标函数：标准分组背包求最大价值，本题求方案数
3. 状态初始化：标准分组背包dp[0][0] = 0，本题dp[0][0] = 1
'''

MOD = 1000000007

def numRollsToTarget1(n, k, target):
    """
    使用二维DP数组实现的解法
    
    Args:
        n: 骰子数量
        k: 每个骰子的面数
        target: 目标和
    
    Returns:
        int: 满足条件的方案数
    """
    # 初始化dp数组
    dp = [[0] * (target + 1) for _ in range(n + 1)]
    dp[0][0] = 1
    
    # 动态规划填表
    for i in range(1, n + 1):
        for j in range(target + 1):
            # 枚举第i个骰子的点数
            for x in range(1, min(k, j) + 1):
                dp[i][j] = (dp[i][j] + dp[i-1][j-x]) % MOD
    
    return dp[n][target]

def numRollsToTarget2(n, k, target):
    """
    使用一维DP数组实现的空间优化解法
    
    Args:
        n: 骰子数量
        k: 每个骰子的面数
        target: 目标和
    
    Returns:
        int: 满足条件的方案数
    """
    # 初始化dp数组
    dp = [0] * (target + 1)
    dp[0] = 1
    
    # 动态规划填表
    for i in range(1, n + 1):
        # 从后往前更新，避免重复使用本轮更新的值
        for j in range(target, -1, -1):
            dp[j] = 0
            # 枚举第i个骰子的点数
            for x in range(1, min(k, j) + 1):
                dp[j] = (dp[j] + dp[j-x]) % MOD
    
    return dp[target]

# 测试方法
if __name__ == "__main__":
    # 测试用例1: n = 1, k = 6, target = 3
    # 预期输出: 1
    print("Test 1:", numRollsToTarget1(1, 6, 3))
    print("Test 1 (空间压缩):", numRollsToTarget2(1, 6, 3))
    
    # 测试用例2: n = 2, k = 6, target = 7
    # 预期输出: 6
    print("Test 2:", numRollsToTarget1(2, 6, 7))
    print("Test 2 (空间压缩):", numRollsToTarget2(2, 6, 7))
    
    # 测试用例3: n = 30, k = 30, target = 500
    # 预期输出: 222616187
    print("Test 3:", numRollsToTarget1(30, 30, 500))
    print("Test 3 (空间压缩):", numRollsToTarget2(30, 30, 500))