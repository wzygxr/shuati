# ACboy needs your help
# 题目描述：ACboy有N门课程，他有M天时间复习。每门课程复习不同的天数会有不同的收益。
# 求在M天时间内，如何安排复习计划使得总收益最大。
# 测试链接 : http://acm.hdu.edu.cn/showproblem.php?pid=1712

"""
算法详解：
这是一个典型的分组背包问题。每门课程可以看作一组物品，每组内的物品互斥（同一门课程只能选择一种复习天数）。
每组物品的价值就是复习该课程不同天数对应的收益。

解题思路：
1. 状态定义：dp[i][j]表示前i门课程，使用j天时间复习的最大收益
2. 状态转移：对于每门课程，枚举所有可能的复习天数k（1 <= k <= j）
   dp[i][j] = max(dp[i-1][j], dp[i-1][j-k] + value[i][k])
3. 空间优化：使用滚动数组将二维优化到一维

时间复杂度分析：
设有N门课程，M天时间，每门课程最多有M种选择
1. 动态规划计算：O(N * M * M)
总时间复杂度：O(N * M^2)

空间复杂度分析：
1. 二维DP数组：O(N * M)
2. 空间优化后：O(M)

相关题目扩展：
1. HDU 1712 ACboy needs your help（本题）
2. 洛谷 P1757 通天之分组背包
3. LeetCode 1155. 掷骰子的N种方法
4. 洛谷 P1064 金明的预算方案（依赖背包）
5. 洛谷 P1941 飞扬的小鸟（多重背包+分组背包）

工程化考量：
1. 输入验证：检查输入参数的有效性
2. 异常处理：处理非法输入、边界情况等
3. 可配置性：可以将课程数和天数作为配置参数传入
4. 单元测试：为solve方法编写测试用例
5. 性能优化：对于大数据量场景，考虑使用空间优化版本

语言特性差异：
1. Python：使用列表推导式简洁但性能较低
2. 动态类型：无需声明变量类型，但运行效率相对较低
3. 丰富库支持：有丰富的标准库和第三方库支持
"""

def solve(N, M, value):
    """
    标准二维DP版本
    """
    # 初始化DP数组
    dp = [[0] * (M + 1) for _ in range(N + 1)]
    
    for i in range(1, N + 1):  # 遍历每门课程
        for j in range(M + 1):  # 遍历可用天数
            # 初始值：不复习当前课程
            dp[i][j] = dp[i - 1][j]
            
            # 枚举复习当前课程的天数k
            for k in range(1, j + 1):
                if value[i][k] > 0:  # 只有收益为正时才考虑
                    dp[i][j] = max(dp[i][j], dp[i - 1][j - k] + value[i][k])
    
    return dp[N][M]

def solveOptimized(N, M, value):
    """
    空间优化版本（推荐使用）
    """
    # 初始化DP数组
    dp = [0] * (M + 1)
    
    for i in range(1, N + 1):  # 遍历每门课程
        # 从后往前更新，避免重复使用
        for j in range(M, -1, -1):
            # 枚举复习当前课程的天数k
            for k in range(1, j + 1):
                if value[i][k] > 0:  # 只有收益为正时才考虑
                    dp[j] = max(dp[j], dp[j - k] + value[i][k])
    
    return dp[M]

def test_acboy():
    """测试函数"""
    # 测试用例1：标准情况
    N1, M1 = 2, 2
    value1 = [
        [0, 0, 0],  # 占位，索引从1开始
        [0, 1, 2],   # 课程1：复习1天收益1，复习2天收益2
        [0, 1, 3]    # 课程2：复习1天收益1，复习2天收益3
    ]
    
    print("测试用例1:")
    print(f"标准版本: {solve(N1, M1, value1)}")
    print(f"优化版本: {solveOptimized(N1, M1, value1)}")
    print("预期结果: 3")
    print()
    
    # 测试用例2：边界情况
    N2, M2 = 0, 0
    value2 = [[0]]
    
    print("测试用例2（边界情况）:")
    print(f"标准版本: {solve(N2, M2, value2)}")
    print(f"优化版本: {solveOptimized(N2, M2, value2)}")
    print("预期结果: 0")
    print()
    
    # 测试用例3：较大规模
    N3, M3 = 3, 3
    value3 = [
        [0, 0, 0, 0],
        [0, 2, 1, 3],  # 课程1
        [0, 1, 2, 1],  # 课程2
        [0, 3, 2, 1]   # 课程3
    ]
    
    print("测试用例3:")
    result1 = solve(N3, M3, value3)
    result2 = solveOptimized(N3, M3, value3)
    print(f"标准版本: {result1}")
    print(f"优化版本: {result2}")
    print("预期结果分析：应该选择课程1复习3天获得收益3，或者课程2复习2天+课程3复习1天获得收益2+3=5")

if __name__ == "__main__":
    test_acboy()

"""
=============================================================================================
补充题目：洛谷 P1757 通天之分组背包（Python实现）
题目链接：https://www.luogu.com.cn/problem/P1757

Python实现：
def group_knapsack(m, items):
    # items = [(weight, value, group), ...]
    # 先按组号排序
    items.sort(key=lambda x: x[2])
    
    dp = [0] * (m + 1)
    n = len(items)
    i = 0
    
    while i < n:
        group = items[i][2]
        j = i
        while j < n and items[j][2] == group:
            j += 1
        
        # 当前组包含物品[i, j-1]
        for k in range(m, -1, -1):
            for x in range(i, j):
                weight, value, _ = items[x]
                if k >= weight:
                    dp[k] = max(dp[k], dp[k - weight] + value)
        
        i = j
    
    return dp[m]

工程化考量：
1. 使用元组组织数据，提高代码可读性
2. 使用lambda表达式简化排序逻辑
3. 添加类型注解提高代码可维护性
4. 使用单元测试框架进行测试

优化思路：
1. 使用字典预处理分组信息
2. 使用numpy数组加速计算
3. 使用缓存装饰器进行记忆化
4. 使用多进程并行处理不同组
"""