# 区间动态规划（Interval Dynamic Programming）综合题目实现
# 本文件包含多个经典区间DP问题的解决方案，涵盖不同平台的题目

# =============================================================================
# 题目1: 戳气球（LeetCode 312）
# 有 n 个气球，编号为0 到 n - 1，每个气球上都标有一个数字，这些数字存在数组 nums 中。
# 现在要求你戳破所有的气球。戳破第 i 个气球，可以获得 nums[i - 1] * nums[i] * nums[i + 1] 枚硬币。
# 这里的 i - 1 和 i + 1 代表和 i 相邻的两个气球的序号。如果 i - 1 或 i + 1 超出了数组的边界，
# 那么就当它是一个数字为 1 的气球。
# 求所能获得硬币的最大数量。
# 测试链接 : https://leetcode.cn/problems/burst-balloons/
# =============================================================================
def maxCoins(nums):
    """
    区间动态规划解法
    时间复杂度: O(n^3) - 三层循环：区间长度、区间起点、分割点
    空间复杂度: O(n^2) - dp数组占用空间
    解题思路:
    1. 状态定义：dp[i][j]表示戳破开区间(i,j)内所有气球能获得的最大硬币数
    2. 状态转移：枚举最后戳破的气球k，dp[i][j] = max(dp[i][k] + dp[k][j] + val[i] * val[k] * val[j])
    3. 边界处理：在首尾添加值为1的虚拟气球，处理边界情况
    4. 工程化考虑：
       - 异常处理：检查输入合法性
       - 边界处理：正确处理数组边界
       - 性能优化：使用虚拟气球简化边界判断
    """
    # 异常处理：空数组情况
    if not nums:
        return 0
    
    n = len(nums)
    
    # 创建新数组，在首尾添加值为1的虚拟气球，处理边界情况
    val = [1] * (n + 2)
    for i in range(1, n + 1):
        val[i] = nums[i - 1]
    
    # 状态定义：dp[i][j]表示戳破开区间(i,j)内所有气球能获得的最大硬币数
    dp = [[0] * (n + 2) for _ in range(n + 2)]
    
    # 枚举区间长度，从2开始（至少要有一个气球可以戳破）
    for length in range(2, n + 2):
        # 枚举区间起点i
        for i in range(n + 2 - length):
            # 计算区间终点j
            j = i + length
            # 枚举最后戳破的气球k
            for k in range(i + 1, j):
                # 状态转移方程：
                # 戳破k气球时，左右区间已经处理完毕，所以获得硬币数为val[i] * val[k] * val[j]
                dp[i][j] = max(dp[i][j], 
                    dp[i][k] + dp[k][j] + val[i] * val[k] * val[j])
    
    return dp[0][n + 1]
# 算法分析：
# 时间复杂度：O(n^3)
#   - 第一层循环枚举区间长度：O(n)
#   - 第二层循环枚举区间起点：O(n)
#   - 第三层循环枚举分割点：O(n)
# 空间复杂度：O(n^2)
#   - 二维dp数组占用空间：O(n^2)
# 优化说明：
#   - 该解法是最优解，因为问题规模为n时，区间DP的时间复杂度无法低于O(n^3)
#   - 使用虚拟气球技巧简化边界处理

# =============================================================================
# 题目2: 分割回文串 II（LeetCode 132）
# 给你一个字符串 s，请你将 s 分割成一些子串，使每个子串都是回文串。
# 返回符合要求的 最少分割次数 。
# 测试链接: https://leetcode.cn/problems/palindrome-partitioning-ii/
# =============================================================================
def minCut(s):
    """
    区间动态规划解法
    时间复杂度: O(n^2) - 预处理O(n^2) + DP计算O(n^2)
    空间复杂度: O(n^2) - isPalindrome和dp数组占用空间
    解题思路:
    1. 预处理：判断子串s[i...j]是否为回文串
    2. 状态定义：dp[i]表示字符串s[0...i]的最少分割次数
    3. 状态转移：枚举最后一个分割点j，如果s[j+1...i]是回文串，则dp[i] = min(dp[i], dp[j] + 1)
    4. 工程化考虑：
       - 异常处理：检查输入合法性
       - 边界处理：正确处理空字符串和单字符情况
       - 性能优化：预处理回文串判断，避免重复计算
    """
    # 异常处理
    if not s or len(s) <= 1:
        return 0  # 空字符串或单字符字符串不需要分割
    
    n = len(s)
    
    # 预处理：判断子串s[i...j]是否为回文串
    isPalindrome = [[False] * n for _ in range(n)]
    for i in range(n - 1, -1, -1):
        for j in range(i, n):
            if s[i] == s[j] and (j - i <= 2 or isPalindrome[i + 1][j - 1]):
                isPalindrome[i][j] = True
    
    # 状态定义：dp[i]表示字符串s[0...i]的最少分割次数
    dp = [float('inf')] * n
    
    # 初始化：单个字符不需要分割
    for i in range(n):
        if isPalindrome[0][i]:
            dp[i] = 0
            continue
        # 状态转移：枚举最后一个分割点j
        for j in range(i):
            if isPalindrome[j + 1][i]:
                dp[i] = min(dp[i], dp[j] + 1)
    
    return int(dp[n - 1])
# 算法分析：
# 时间复杂度：O(n^2)
#   - 预处理回文串：O(n^2)
#   - 计算dp数组：O(n^2)
# 空间复杂度：O(n^2)
#   - isPalindrome数组：O(n^2)
#   - dp数组：O(n)
# 优化说明：
#   - 该解法是最优解，时间复杂度无法进一步降低
#   - 可以使用中心扩展法优化回文串预处理

# =============================================================================
# 题目3: 切棍子的最小成本（LeetCode 1547）
# 有一根长度为n的木棍，我们需要把它切成k段。
# 给定一个整数数组cuts，其中cuts[i]表示将木棍切开的位置。
# 每次切割的成本是当前要切割的木棍的长度，求切完所有位置的最小总成本。
# 测试链接: https://leetcode.cn/problems/minimum-cost-to-cut-a-stick/
# =============================================================================
def minCost(n, cuts):
    """
    区间动态规划解法
    时间复杂度: O(m^3) - m是切割点数量+2
    空间复杂度: O(m^2) - dp数组占用空间
    解题思路:
    1. 状态定义：dp[i][j]表示切割points[i]到points[j]之间的木棍的最小成本
    2. 状态转移：枚举分割点k，dp[i][j] = min(dp[i][k] + dp[k][j] + cost)
    3. 预处理：对切割点进行排序，构造包含0和n的切割点数组
    4. 工程化考虑：
       - 异常处理：检查输入合法性
       - 边界处理：正确处理无切割点情况
       - 性能优化：排序切割点，使用前缀和计算区间长度
    """
    # 异常处理
    if not cuts:
        return 0  # 不需要切割
    
    # 对切割点进行排序
    cuts.sort()
    
    # 构造新的切割点数组，包含0和n
    m = len(cuts) + 2
    points = [0] * m
    points[0] = 0
    points[m - 1] = n
    for i in range(1, m - 1):
        points[i] = cuts[i - 1]
    
    # 状态定义：dp[i][j]表示切割points[i]到points[j]之间的木棍的最小成本
    # 使用float类型以支持无穷大值
    dp = [[0.0 for _ in range(m)] for _ in range(m)]
    
    # 枚举区间长度，从2开始（至少有两个点）
    for length in range(2, m):
        # 枚举区间起点i
        for i in range(m - length):
            j = i + length
            # 初始化dp[i][j]为较大值
            dp[i][j] = float('inf')
            # 当前木棍的长度
            cost = points[j] - points[i]
            # 枚举分割点k
            for k in range(i + 1, j):
                dp[i][j] = min(dp[i][j], dp[i][k] + dp[k][j] + cost)
    
    return int(dp[0][m - 1])
# 算法分析：
# 时间复杂度：O(m^3)，其中m是切割点数量+2
# 空间复杂度：O(m^2)，用于存储dp数组
# 优化说明：
#   - 该解法是最优解，因为问题规模为m时，区间DP的时间复杂度无法低于O(m^3)
#   - 可以使用四边形不等式优化到O(m^2)

# =============================================================================
# 题目4: 多边形三角剖分的最低得分（LeetCode 1039）
# 给定一个凸多边形，顶点按顺时针顺序标记为A[0], A[1], ..., A[n-1]。
# 对于每个三角形，计算三个顶点标记的乘积，然后将所有三角形的乘积相加。
# 返回所有可能的三角剖分中，分数最低的那个。
# 测试链接: https://leetcode.cn/problems/minimum-score-triangulation-of-polygon/
# =============================================================================
def minScoreTriangulation(values):
    """
    区间动态规划解法
    时间复杂度: O(n^3) - 三层循环：区间长度、区间起点、分割点
    空间复杂度: O(n^2) - dp数组占用空间
    解题思路:
    1. 状态定义：dp[i][j]表示顶点i到j构成的多边形的最小三角剖分得分
    2. 状态转移：枚举中间点k，将多边形分为三角形i-j-k和两个子多边形
    3. 工程化考虑：
       - 异常处理：检查输入合法性
       - 边界处理：正确处理顶点数小于3的情况
       - 性能优化：避免重复计算
    """
    # 异常处理
    if not values or len(values) < 3:
        return 0  # 无法形成三角形
    
    n = len(values)
    
    # 状态定义：dp[i][j]表示顶点i到j构成的多边形的最小三角剖分得分
    # 使用float类型以支持无穷大值
    dp = [[0.0 for _ in range(n)] for _ in range(n)]
    
    # 枚举区间长度，从3开始（至少需要3个点才能形成三角形）
    for length in range(3, n + 1):
        # 枚举区间起点i
        for i in range(n - length + 1):
            j = i + length - 1
            # 初始化dp[i][j]为较大值
            dp[i][j] = float('inf')
            # 枚举中间点k，将多边形分为三角形i-j-k和两个子多边形
            for k in range(i + 1, j):
                dp[i][j] = min(dp[i][j], 
                    dp[i][k] + dp[k][j] + values[i] * values[k] * values[j])
    
    return int(dp[0][n - 1])
# 算法分析：
# 时间复杂度：O(n^3)，其中n是顶点数量
# 空间复杂度：O(n^2)，用于存储dp数组
# 优化说明：
#   - 该解法是最优解，因为问题规模为n时，区间DP的时间复杂度无法低于O(n^3)
#   - 可以使用四边形不等式优化到O(n^2)

# =============================================================================
# 区间动态规划解题技巧总结
# =============================================================================
'''
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
'''

# 输入输出处理
if __name__ == "__main__":
    import sys
    # 测试用例验证
    if len(sys.argv) == 1:
        # 运行测试
        test_cases = [
            ([3, 1, 5, 8], 167),  # LeetCode示例
            ([1, 2, 3], 12),      # 简单测试 - 修正期望值
        ]
        
        all_passed = True
        for nums, expected in test_cases:
            result = maxCoins(nums)
            if result != expected:
                all_passed = False
                print(f"Test failed for input {nums}: expected {expected}, got {result}")
        
        if all_passed:
            # 读取标准输入并输出结果
            line = sys.stdin.readline().strip()
            if line:
                nums = list(map(int, line.split()))
                print(maxCoins(nums))
            else:
                print("0")
    else:
        # 从标准输入读取
        nums = list(map(int, sys.stdin.readline().split()))
        print(maxCoins(nums))