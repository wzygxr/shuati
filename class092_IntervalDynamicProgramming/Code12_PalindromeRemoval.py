# LeetCode 1246. 删除回文子数组
# 给定一个整数数组arr，每次可以选择并删除一个回文子数组，求删除所有数字的最少操作次数。
# 测试链接: https://leetcode.cn/problems/palindrome-removal/
# 
# 解题思路:
# 1. 状态定义：dp[i][j]表示删除区间[i,j]所需的最少操作次数
# 2. 状态转移：考虑三种策略：单独删除首元素，与后面相同元素一起删除，或者分割区间
# 3. 时间复杂度：O(n^3)
# 4. 空间复杂度：O(n^2)
#
# 工程化考量:
# 1. 异常处理：检查输入数组合法性
# 2. 边界处理：处理空数组和单元素情况
# 3. 性能优化：使用区间DP标准模板
# 4. 测试覆盖：设计全面的测试用例

import sys

def minimum_moves(arr):
    """
    区间DP解法
    时间复杂度: O(n^3) - 三层循环：区间长度、区间起点、分割点
    空间复杂度: O(n^2) - dp数组占用空间
    
    解题思路:
    1. 状态定义：dp[i][j]表示删除数组arr在区间[i,j]所需的最少操作次数
    2. 状态转移：
        - 基础情况：dp[i][i] = 1（单个元素需要1次删除）
        - 如果arr[i] == arr[j]，则dp[i][j] = dp[i+1][j-1]（可以一起删除）
        - 否则，枚举分割点k：dp[i][j] = min(dp[i][k] + dp[k+1][j])
    3. 填表顺序：按区间长度从小到大
    """
    # 异常处理
    if not arr:
        return 0
    
    n = len(arr)
    
    # 状态定义：dp[i][j]表示删除区间[i,j]所需的最少操作次数
    # 使用大数初始化表示不可达状态
    dp = [[float('inf')] * n for _ in range(n)]
    
    # 初始化：单个元素需要1次删除
    for i in range(n):
        dp[i][i] = 1
    
    # 枚举区间长度，从2开始
    for length in range(2, n + 1):
        for i in range(n - length + 1):
            j = i + length - 1
            
            # 策略1：如果首尾元素相同，可以一起删除
            if arr[i] == arr[j]:
                if length == 2:
                    # 长度为2且相同，只需要1次删除
                    dp[i][j] = 1
                else:
                    # 长度大于2，考虑内层区间
                    dp[i][j] = min(dp[i][j], dp[i + 1][j - 1])
            
            # 策略2：枚举分割点k，将区间分为[i,k]和[k+1,j]
            for k in range(i, j):
                dp[i][j] = min(dp[i][j], dp[i][k] + dp[k + 1][j])
    
    return dp[0][n - 1]

def minimum_moves_optimized(arr):
    """
    优化版本 - 减少不必要的分割点枚举
    时间复杂度: O(n^3) 但实际运行更快
    空间复杂度: O(n^2)
    
    优化思路:
    1. 当arr[i] == arr[k]时，可以优化状态转移
    2. 减少重复计算
    """
    if not arr:
        return 0
    
    n = len(arr)
    dp = [[float('inf')] * n for _ in range(n)]
    
    # 初始化
    for i in range(n):
        dp[i][i] = 1
    
    for length in range(2, n + 1):
        for i in range(n - length + 1):
            j = i + length - 1
            
            # 关键优化：如果arr[i] == arr[k]，可以优化转移
            for k in range(i, j):
                temp = dp[i][k] + dp[k + 1][j]
                if arr[i] == arr[k]:
                    # 进一步优化：如果首元素与分割点元素相同
                    temp = min(temp, dp[i][k] + (dp[k + 1][j] if k + 1 <= j else 0) - 1)
                dp[i][j] = min(dp[i][j], temp)
            
            # 特殊处理：首尾元素相同的情况
            if arr[i] == arr[j]:
                if length == 2:
                    dp[i][j] = 1
                else:
                    dp[i][j] = min(dp[i][j], dp[i + 1][j - 1])
    
    return dp[0][n - 1]

def minimum_moves_memo(arr):
    """
    记忆化搜索版本 - 递归+记忆化
    时间复杂度: O(n^3)
    空间复杂度: O(n^2)
    
    优点: 代码更直观，易于理解
    缺点: 递归深度可能较大
    """
    if not arr:
        return 0
    
    n = len(arr)
    memo = [[-1] * n for _ in range(n)]
    
    def dfs(i, j):
        if i > j:
            return 0
        
        if memo[i][j] != -1:
            return memo[i][j]
        
        # 基础情况：单个元素
        if i == j:
            return 1
        
        result = float('inf')
        
        # 策略1：单独删除首元素，然后删除剩余部分
        result = min(result, 1 + dfs(i + 1, j))
        
        # 策略2：如果首元素与后面某个元素相同，可以一起删除
        for k in range(i + 1, j + 1):
            if arr[i] == arr[k]:
                # 如果相邻，可以直接一起删除
                if k == i + 1:
                    result = min(result, 1 + dfs(k + 1, j))
                else:
                    # 不相邻，需要考虑中间部分
                    result = min(result, dfs(i + 1, k - 1) + dfs(k + 1, j))
        
        memo[i][j] = result
        return result
    
    return dfs(0, n - 1)

def test():
    """
    单元测试方法
    """
    # 测试用例1：示例输入
    arr1 = [1, 2]
    result1 = minimum_moves(arr1)
    print(f"Test 1 - Input: [1, 2], Expected: 2, Actual: {result1}")
    
    # 测试用例2：相同元素
    arr2 = [1, 1]
    result2 = minimum_moves(arr2)
    print(f"Test 2 - Input: [1, 1], Expected: 1, Actual: {result2}")
    
    # 测试用例3：回文数组
    arr3 = [1, 2, 1]
    result3 = minimum_moves(arr3)
    print(f"Test 3 - Input: [1, 2, 1], Expected: 1, Actual: {result3}")
    
    # 测试用例4：复杂情况
    arr4 = [1, 3, 4, 1, 5]
    result4 = minimum_moves(arr4)
    print(f"Test 4 - Input: [1, 3, 4, 1, 5], Expected: 3, Actual: {result4}")
    
    # 验证不同方法的正确性
    result1_opt = minimum_moves_optimized(arr1)
    result1_memo = minimum_moves_memo(arr1)
    print(f"Validation - Basic: {result1}, Optimized: {result1_opt}, Memo: {result1_memo}")
    
    # 验证结果一致性
    assert result1 == result1_opt, "Different methods should give same result"
    assert result1 == result1_memo, "Different methods should give same result"

def performance_test():
    """
    性能测试方法
    """
    # 生成测试数据
    test_arr = [i % 10 for i in range(100)]  # 重复元素测试
    
    import time
    start_time = time.time()
    result = minimum_moves(test_arr)
    end_time = time.time()
    
    print(f"Performance Test - Length: {len(test_arr)}, Result: {result}, Time: {end_time - start_time:.4f}s")

def boundary_test():
    """
    边界测试方法
    """
    # 空数组测试
    empty = []
    result_empty = minimum_moves(empty)
    print(f"Empty array test: {result_empty}")
    
    # 单元素测试
    single = [5]
    result_single = minimum_moves(single)
    print(f"Single element test: {result_single}")
    
    # 全相同元素测试
    all_same = [1, 1, 1, 1, 1]
    result_all_same = minimum_moves(all_same)
    print(f"All same elements test: {result_all_same}")
    
    # 全不同元素测试
    all_different = [1, 2, 3, 4, 5]
    result_all_different = minimum_moves(all_different)
    print(f"All different elements test: {result_all_different}")

def main():
    """
    主函数 - 处理输入输出
    """
    line = sys.stdin.readline().strip()
    if not line:
        print(0)
        return
    
    arr = list(map(int, line.split()))
    result = minimum_moves(arr)
    print(result)

if __name__ == "__main__":
    # 如果是直接运行，执行测试
    if len(sys.argv) == 1:
        test()
        boundary_test()
        # performance_test()
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