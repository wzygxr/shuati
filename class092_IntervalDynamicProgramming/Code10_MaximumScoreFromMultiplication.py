# LeetCode 1770. 执行乘法运算的最大分数
# 给定两个数组nums和multipliers，每次从nums的头部或尾部取一个数与multipliers[i]相乘，求最大得分。
# 测试链接: https://leetcode.cn/problems/maximum-score-from-performing-multiplication-operations/
# 
# 解题思路:
# 1. 状态定义：dp[i][j]表示使用前i个multipliers，从nums左端取了j个元素的最大分数
# 2. 状态转移：考虑两种选择：取左端或取右端
# 3. 时间复杂度：O(m^2)，其中m是multipliers的长度
# 4. 空间复杂度：O(m^2)，可以优化到O(m)
#
# 工程化考量:
# 1. 异常处理：检查输入合法性
# 2. 边界处理：处理空数组和边界情况
# 3. 性能优化：使用滚动数组优化空间复杂度
# 4. 测试覆盖：设计全面的测试用例

import sys

def maximum_score(nums, multipliers):
    """
    区间DP解法 - 基本版本（空间复杂度O(m^2)）
    时间复杂度: O(m^2) - 其中m是multipliers的长度
    空间复杂度: O(m^2) - dp数组占用空间
    
    解题思路:
    1. 状态定义：dp[i][j]表示使用前i个multipliers，从nums左端取了j个元素的最大分数
    2. 状态转移：
        - 取左端：dp[i][j] = dp[i-1][j-1] + multipliers[i-1] * nums[j-1]
        - 取右端：dp[i][j] = dp[i-1][j] + multipliers[i-1] * nums[n - (i - j)]
    3. 初始化：dp[0][0] = 0
    4. 结果：max(dp[m][j]) for j in [0, m]
    """
    # 异常处理
    if not nums or not multipliers:
        return 0
    
    n = len(nums)
    m = len(multipliers)
    
    # 状态定义：dp[i][j]表示使用前i个multipliers，从nums左端取了j个元素的最大分数
    # 使用负无穷初始化表示不可达状态
    dp = [[-10**9] * (m + 1) for _ in range(m + 1)]
    
    # 初始化：dp[0][0] = 0
    dp[0][0] = 0
    
    max_score = -10**9
    
    # 动态规划填表
    for i in range(1, m + 1):  # 使用前i个multipliers
        for j in range(i + 1):  # 从左端取了j个元素
            # 计算右端取的元素数量
            right_count = i - j
            
            # 检查左端取j个元素是否合法
            if j > 0:
                # 选择取左端：第i个multiplier乘以nums中左端第j个元素
                left_score = dp[i-1][j-1] + multipliers[i-1] * nums[j-1]
                if dp[i][j] < left_score:
                    dp[i][j] = left_score
            
            # 检查右端取right_count个元素是否合法
            if right_count > 0 and j <= i - 1:
                # 选择取右端：第i个multiplier乘以nums中右端第right_count个元素
                right_index = n - right_count
                right_score = dp[i-1][j] + multipliers[i-1] * nums[right_index]
                if dp[i][j] < right_score:
                    dp[i][j] = right_score
            
            # 更新最大分数
            if i == m:
                if max_score < dp[i][j]:
                    max_score = dp[i][j]
    
    return max_score

def maximum_score_optimized(nums, multipliers):
    """
    优化版本 - 使用滚动数组将空间复杂度优化到O(m)
    时间复杂度: O(m^2)
    空间复杂度: O(m)
    
    优化思路:
    1. 观察状态转移方程，发现dp[i]只依赖于dp[i-1]
    2. 可以使用两个数组交替计算，减少空间占用
    3. 适用于大规模数据场景
    """
    # 异常处理
    if not nums or not multipliers:
        return 0
    
    n = len(nums)
    m = len(multipliers)
    
    # 使用两个数组进行滚动计算
    dp = [-10**9] * (m + 1)  # 当前状态
    prev = [-10**9] * (m + 1)  # 前一个状态
    
    # 初始化prev数组
    prev[0] = 0
    
    # 动态规划填表
    for i in range(1, m + 1):
        # 重置当前状态数组
        dp = [-10**9] * (m + 1)
        
        for j in range(i + 1):
            right_count = i - j
            
            # 取左端
            if j > 0 and prev[j-1] != -10**9:
                left_score = prev[j-1] + multipliers[i-1] * nums[j-1]
                if dp[j] < left_score:
                    dp[j] = left_score
            
            # 取右端
            if right_count > 0 and j <= i - 1 and prev[j] != -10**9:
                right_index = n - right_count
                right_score = prev[j] + multipliers[i-1] * nums[right_index]
                if dp[j] < right_score:
                    dp[j] = right_score
        
        # 交换数组，准备下一轮计算
        prev, dp = dp, prev
    
    # 寻找最大分数
    max_score = -10**9
    for j in range(m + 1):
        if max_score < prev[j]:
            max_score = prev[j]
    
    return max_score

def maximum_score_memo(nums, multipliers):
    """
    记忆化搜索版本 - 递归+记忆化
    时间复杂度: O(m^2)
    空间复杂度: O(m^2)
    
    优点:
    1. 代码更直观，易于理解
    2. 避免不必要的状态计算
    缺点:
    1. 递归深度可能较大
    2. 栈空间开销
    """
    if not nums or not multipliers:
        return 0
    
    n = len(nums)
    m = len(multipliers)
    
    # 记忆化字典
    memo = {}
    
    def dfs(left, right, idx):
        # 边界条件：所有multipliers都已使用
        if idx == m:
            return 0
        
        # 检查记忆化结果
        if (left, idx) in memo:
            return memo[(left, idx)]
        
        # 选择取左端
        take_left = multipliers[idx] * nums[left] + dfs(left + 1, right, idx + 1)
        
        # 选择取右端
        take_right = multipliers[idx] * nums[right] + dfs(left, right - 1, idx + 1)
        
        # 取较大值并记忆化
        result = max(take_left, take_right)
        memo[(left, idx)] = result
        
        return result
    
    return dfs(0, n - 1, 0)

def test():
    """
    单元测试方法 - 验证算法正确性
    """
    # 测试用例1：示例输入
    nums1 = [1, 2, 3]
    multipliers1 = [3, 2, 1]
    result1 = maximum_score(nums1, multipliers1)
    print(f"Test 1 - Expected: 14, Actual: {result1}")
    
    # 测试用例2：边界情况
    nums2 = [1]
    multipliers2 = [1]
    result2 = maximum_score(nums2, multipliers2)
    print(f"Test 2 - Expected: 1, Actual: {result2}")
    
    # 测试用例3：大规模数据测试
    nums3 = [1, 2, 3, 4, 5]
    multipliers3 = [1, 2, 3, 4, 5]
    result3 = maximum_score(nums3, multipliers3)
    print(f"Test 3 - Actual: {result3}")
    
    # 测试优化版本
    result1_opt = maximum_score_optimized(nums1, multipliers1)
    result2_opt = maximum_score_optimized(nums2, multipliers2)
    print(f"Optimized Test 1: {result1_opt}, Test 2: {result2_opt}")
    
    # 验证结果一致性
    assert result1 == result1_opt, "Basic and optimized versions should give same result"
    assert result2 == result2_opt, "Basic and optimized versions should give same result"

def main():
    """
    主函数 - 处理输入输出
    """
    # 读取输入
    try:
        nums_line = sys.stdin.readline().strip()
        multipliers_line = sys.stdin.readline().strip()
        
        if not nums_line or not multipliers_line:
            print(0)
            return
        
        nums = list(map(int, nums_line.split()))
        multipliers = list(map(int, multipliers_line.split()))
        
        # 计算结果
        result = maximum_score(nums, multipliers)
        
        # 输出结果
        print(result)
        
    except Exception as e:
        print(f"Error: {e}")
        print(0)

if __name__ == "__main__":
    # 如果是直接运行，执行测试
    if len(sys.argv) == 1:
        test()
    else:
        main()

# 区间动态规划解题技巧总结
"""
1. 题型识别方法：
   - 涉及区间最优解问题，如最大值、最小值
   - 问题可以分解为子区间的最优解
   - 需要枚举分割点将大区间分解为小区间

2. 状态设计模式：
   - 通常定义dp[i][j]表示区间[i,j]的最优解
   - 根据具体问题调整状态含义

3. 填表顺序：
   - 按区间长度从小到大枚举
   - 长度为1的区间通常可以直接初始化
   - 长度大于1的区间通过分割点由小区间组合而来

4. 优化技巧：
   - 预处理：提前计算辅助信息（如回文判断）
   - 空间压缩：某些问题可以优化空间复杂度
   - 剪枝：利用问题特性减少不必要的计算

5. 工程化考量：
   - 异常处理：检查输入合法性，处理边界情况
   - 边界条件：正确初始化长度为1的区间
   - 性能优化：使用前缀和等技术减少重复计算
"""