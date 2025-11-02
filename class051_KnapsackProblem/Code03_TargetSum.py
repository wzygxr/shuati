# 目标和问题
# 
# 问题描述：
# 给你一个非负整数数组 nums 和一个整数 target 。
# 向数组中的每个整数前添加 '+' 或 '-' ，然后串联起所有整数，
# 可以构造一个表达式，返回可以通过上述方法构造的，运算结果等于 target 的不同表达式的数目。
# 
# 示例：
# 输入：nums = [1,1,1,1,1], target = 3
# 输出：5
# 解释：一共有 5 种方法让最终目标和为 3 。
# 
# 解题思路：
# 本题有多种解法：
# 1. 暴力递归：对每个数字尝试加号或减号，递归计算所有可能的组合
# 2. 记忆化搜索：在暴力递归基础上增加缓存，避免重复计算
# 3. 转化为01背包问题：通过数学推导将问题转化为求子集和的问题
# 
# 第三种方法是最优解：
# 1. 将数组分为两个子集：正数集合A和负数集合B
# 2. 有 sum(A) - sum(B) = target
# 3. 两边同时加上 sum(A) + sum(B) 得到：2 * sum(A) = target + sum
# 4. 即 sum(A) = (target + sum) / 2
# 5. 问题转化为：求有多少个子集的和等于 (target + sum) / 2，这就是标准的01背包问题
# 
# 测试链接 : https://leetcode.cn/problems/target-sum/

# 普通尝试，暴力递归版
# 
# 解题思路：
# 对于数组中的每个元素，都有两种选择：加上该元素或减去该元素
# 递归地尝试所有可能的组合，当遍历完所有元素后，检查累加和是否等于target
# 
# 时间复杂度：O(2^n)，其中n是数组长度
# 空间复杂度：O(n)，递归栈深度
def findTargetSumWays1(nums, target):
    """
    暴力递归求解目标和问题
    
    Args:
        nums: 非负整数数组
        target: 目标和
    
    Returns:
        不同表达式的数目
    """
    def f1(nums, target, i, sum_val):
        # 基础情况：已经处理完所有元素
        if i == len(nums):
            # 如果当前累加和等于目标值，说明找到了一种有效方案
            return 1 if sum_val == target else 0
        # 递归情况：对当前元素尝试加号和减号两种情况
        # 返回两种情况的方案数之和
        return f1(nums, target, i + 1, sum_val + nums[i]) + f1(nums, target, i + 1, sum_val - nums[i])
    
    return f1(nums, target, 0, 0)

# 普通尝试，记忆化搜索版
# 
# 解题思路：
# 在暴力递归的基础上，使用字典缓存已经计算过的结果
# 避免重复计算相同状态（位置i和当前累加和sum）下的结果
# 
# 时间复杂度：O(n * sum)，其中n是数组长度，sum是数组元素和的范围
# 空间复杂度：O(n * sum)
def findTargetSumWays2(nums, target):
    """
    记忆化搜索求解目标和问题
    
    Args:
        nums: 非负整数数组
        target: 目标和
    
    Returns:
        不同表达式的数目
    """
    dp = {}
    
    def f2(nums, target, i, j):
        # 基础情况：已经处理完所有元素
        if i == len(nums):
            # 如果当前累加和等于目标值，说明找到了一种有效方案
            return 1 if j == target else 0
        
        # 检查是否已经计算过当前状态
        if (i, j) in dp:
            return dp[(i, j)]
        
        # 递归计算两种情况的方案数之和
        ans = f2(nums, target, i + 1, j + nums[i]) + f2(nums, target, i + 1, j - nums[i])
        # 缓存计算结果
        dp[(i, j)] = ans
        return ans
    
    return f2(nums, target, 0, 0)

# 新思路，转化为01背包问题
# 
# 解题思路：
# 通过数学推导将问题转化为01背包问题：
# 1. 将数组分为两个子集：正数集合A和负数集合B
# 2. 有 sum(A) - sum(B) = target
# 3. 两边同时加上 sum(A) + sum(B) 得到：2 * sum(A) = target + sum
# 4. 即 sum(A) = (target + sum) / 2
# 5. 问题转化为：求有多少个子集的和等于 (target + sum) / 2
# 
# 时间复杂度：O(n * t)，其中n是数组长度，t是(target + sum) / 2
# 空间复杂度：O(t)
# 
# 思考过程:
# 1. 虽然题目说nums是非负数组，但即使nums中有负数比如[3,-4,2]
#    因为能在每个数前面用+或者-号
#    所以[3,-4,2]其实和[3,4,2]会达成一样的结果
#    所以即使nums中有负数，也可以把负数直接变成正数，也不会影响结果
# 2. 如果nums都是非负数，并且所有数的累加和是sum
#    那么如果target>sum，很明显没有任何方法可以达到target，可以直接返回0
# 3. nums内部的数组，不管怎么+和-，最终的结果都一定不会改变奇偶性
#    所以，如果所有数的累加和是sum，并且与target的奇偶性不一样
#    那么没有任何方法可以达到target，可以直接返回0
# 4. 最重要:
#    比如说给定一个数组, nums = [1, 2, 3, 4, 5] 并且 target = 3
#    其中一个方案是 : +1 -2 +3 -4 +5 = 3
#    该方案中取了正的集合为A = {1，3，5}
#    该方案中取了负的集合为B = {2，4}
#    所以任何一种方案，都一定有 sum(A) - sum(B) = target
#    现在我们来处理一下这个等式，把左右两边都加上sum(A) + sum(B)，那么就会变成如下：
#    sum(A) - sum(B) + sum(A) + sum(B) = target + sum(A) + sum(B)
#    2 * sum(A) = target + 数组所有数的累加和
#    sum(A) = (target + 数组所有数的累加和) / 2
#    也就是说，任何一个集合，只要累加和是(target + 数组所有数的累加和) / 2
#    那么就一定对应一种target的方式
#    比如非负数组nums，target = 1, nums所有数累加和是11
#    求有多少方法组成1，其实就是求，有多少种子集累加和达到6的方法，(1+11)/2=6
#    因为，子集累加和6 - 另一半的子集累加和5 = 1(target)
#    所以有多少个累加和为6的不同集合，就代表有多少个target==1的表达式数量
#    至此已经转化为01背包问题了
def findTargetSumWays4(nums, target):
    """
    转化为01背包问题求解目标和
    
    Args:
        nums: 非负整数数组
        target: 目标和
    
    Returns:
        不同表达式的数目
    """
    total_sum = sum(nums)
    
    # 如果sum小于target或者(target+sum)是奇数，直接返回0
    if total_sum < abs(target) or (target + total_sum) % 2 == 1:
        return 0
    
    # 转化为求子集和为(target + total_sum) // 2的方案数
    return subsets(nums, (target + total_sum) // 2)

# 求非负数组nums有多少个子序列累加和是t
# 01背包问题(子集累加和严格是t) + 空间压缩
# dp[i][j] = dp[i-1][j] + dp[i-1][j-nums[i]]
def subsets(nums, t):
    """
    求非负数组nums有多少个子序列累加和是t
    
    解题思路：
    使用01背包问题的解法，dp[j]表示和为j的子集数目
    状态转移方程：dp[j] = dp[j] + dp[j - nums[i]]
    
    Args:
        nums: 非负整数数组
        t: 目标子集和
    
    Returns:
        和为t的子集数目
    """
    # 如果目标值为负数，直接返回0
    if t < 0:
        return 0
    
    # dp[j]表示和为j的子集数目
    dp = [0] * (t + 1)
    # 初始状态：和为0的子集有1个（空集）
    dp[0] = 1
    
    # 遍历每个数字
    for num in nums:
        # 从后往前遍历，确保每个数字只使用一次
        for j in range(t, num - 1, -1):
            # 状态转移方程：选择当前数字或不选择当前数字
            dp[j] += dp[j - num]
    
    # 返回和为t的子集数目
    return dp[t]

# 牛客网背包问题
# 
# 题目描述：玥玥带乔乔一起逃亡，现在有许多的东西要放到乔乔的包里面，
# 但是包的大小有限，所以我们只能够在里面放入非常重要的物品。
# 现在给出该种物品的数量、体积、价值的数值，希望你能够算出怎样能使背包的价值最大的组合方式，
# 并且输出这个数值，乔乔会非常感谢你。
# 
# 解题思路：
# 这是经典的01背包问题，使用动态规划求解
# dp[j]表示背包容量为j时能装入的最大价值
# 状态转移方程：dp[j] = max(dp[j], dp[j - volumes[i]] + values[i])
def backpack(n, v, volumes, values):
    """
    牛客网背包问题求解
    
    Args:
        n: 物品数量
        v: 背包容量
        volumes: 物品体积数组
        values: 物品价值数组
    
    Returns:
        背包能装入的最大价值
    """
    # dp[j] 表示背包容量为j时能装入的最大价值
    dp = [0] * (v + 1)
    
    # 遍历物品
    for i in range(n):
        # 倒序遍历背包容量，确保每个物品只使用一次
        for j in range(v, volumes[i] - 1, -1):
            dp[j] = max(dp[j], dp[j - volumes[i]] + values[i])
    
    return dp[v]

'''
示例:
输入: n = 4, v = 5
volumes = [1, 2, 3, 4]
values = [2, 4, 4, 5]
输出: 8
解释: 选择第1个和第3个物品，总重量为2+3=5，总价值为4+4=8

时间复杂度: O(n * v)
空间复杂度: O(v)
'''