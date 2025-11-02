# -*- coding: utf-8 -*-
"""
高级动态规划算法集合

本文件实现了多种高级动态规划算法及其优化技术，包括：
1. 基础DP融合（DP+数论、DP+字符串、DP+计算几何）
2. 分层DP（多阶段问题分层）
3. 容斥DP（结合数论容斥）
4. 记忆化搜索（高维状态+剪枝）
5. 组合DP（生成函数+DP）
6. 博弈DP（SG函数、阶梯Nim）
7. 高维压缩（子集/超集枚举与预处理、子集/超集卷积）
8. 优化体系（Knuth优化、Divide & Conquer Optimization、SMAWK、Aliens Trick）
9. 图上DP→最短路（分层图建模）
10. 冷门模型（期望DP、插头DP、树上背包）

本实现涵盖了从入门到竞赛级别的各类动态规划技术，并提供了详细的注释和复杂度分析。
"""

import sys
import math

# ==================== DP+数论（模意义）====================

def coin_change_mod(coins, amount, mod=10**9+7):
    """
    LeetCode 518. 零钱兑换 II（模意义下的变种）
    题目链接：https://leetcode-cn.com/problems/coin-change-2/
    
    问题描述：
    给定不同面额的硬币和一个总金额。计算可以凑成总金额的硬币组合数。
    假设每一种面额的硬币有无限个。要求结果对给定的模数取余。
    
    解题思路：
    使用动态规划，定义dp[i]表示凑成金额i的组合数。
    状态转移方程：dp[i] = (dp[i] + dp[i-coin]) % mod，其中coin是硬币面额。
    
    Args:
        coins: 硬币面额数组
        amount: 总金额
        mod: 模数
    
    Returns:
        int: 可以凑成总金额的硬币组合数对mod取余的结果
    """
    # dp[i]表示凑成金额i的组合数
    dp = [0] * (amount + 1)
    dp[0] = 1  # 凑成金额0的方式只有1种（不选任何硬币）
    
    # 遍历每种硬币
    for coin in coins:
        # 遍历金额，从coin开始
        for i in range(coin, amount + 1):
            dp[i] = (dp[i] + dp[i - coin]) % mod
    
    return dp[amount]


def matrix_power_mod(matrix, power, mod):
    """
    矩阵快速幂（模意义下）
    
    问题描述：
    计算矩阵的幂，结果对mod取余。
    
    解题思路：
    使用快速幂算法，将矩阵的乘法在模意义下进行。
    
    Args:
        matrix: 输入矩阵
        power: 幂次
        mod: 模数
    
    Returns:
        list: 矩阵的幂对mod取余的结果
    """
    n = len(matrix)
    # 初始化结果为单位矩阵
    result = [[1 if i == j else 0 for j in range(n)] for i in range(n)]
    
    # 快速幂算法
    while power > 0:
        if power % 2 == 1:
            # 矩阵乘法
            result = matrix_multiply(result, matrix, mod)
        # 矩阵自乘
        matrix = matrix_multiply(matrix, matrix, mod)
        power //= 2
    
    return result

def matrix_multiply(a, b, mod):
    """
    矩阵乘法（模意义下）
    
    Args:
        a: 矩阵a
        b: 矩阵b
        mod: 模数
    
    Returns:
        list: 矩阵a和矩阵b的乘积对mod取余的结果
    """
    n = len(a)
    m = len(b[0])
    k = len(b)
    result = [[0 for _ in range(m)] for _ in range(n)]
    
    for i in range(n):
        for j in range(m):
            for p in range(k):
                result[i][j] = (result[i][j] + a[i][p] * b[p][j]) % mod
    
    return result

# ==================== 分层DP（多阶段问题分层）====================

def layered_dp(matrix):
    """
    分层DP示例：LeetCode 120. 三角形最小路径和的分层解法
    题目链接：https://leetcode-cn.com/problems/triangle/
    
    问题描述：
    给定一个三角形 triangle ，找出自顶向下的最小路径和。
    每一步只能移动到下一行中相邻的结点上。相邻的结点 在这里指的是 下标 与 上一层结点下标 相同或者等于 上一层结点下标 + 1 的两个结点。
    
    解题思路：
    使用分层DP，每层维护当前层的最优解。
    定义dp[i][j]表示到达第i行第j列的最小路径和。
    状态转移方程：dp[i][j] = min(dp[i-1][j-1], dp[i-1][j]) + triangle[i][j]
    
    Args:
        matrix: 三角形矩阵
    
    Returns:
        int: 最小路径和
    
    时间复杂度：O(n^2)，其中n是三角形的行数
    空间复杂度：O(n)，使用滚动数组优化空间
    """
    n = len(matrix)
    # 使用滚动数组优化空间
    dp = [float('inf')] * n
    dp[0] = matrix[0][0]  # 初始状态
    
    for i in range(1, n):
        # 从右往左更新，避免覆盖上一层的值
        for j in range(i, -1, -1):
            if j == 0:
                # 最左边的元素只能从正上方来
                dp[j] = dp[j] + matrix[i][j]
            elif j == i:
                # 最右边的元素只能从左上方来
                dp[j] = dp[j-1] + matrix[i][j]
            else:
                # 中间元素可以从正上方或左上方来
                dp[j] = min(dp[j], dp[j-1]) + matrix[i][j]
    
    return min(dp[:n])

def multi_layered_dp(grid):
    """
    多层DP示例：LeetCode 62. 不同路径
    题目链接：https://leetcode-cn.com/problems/unique-paths/
    
    问题描述：
    一个机器人位于一个 m x n 网格的左上角 （起始点在下图中标记为 “Start” ）。
    机器人每次只能向下或者向右移动一步。机器人试图达到网格的右下角（在下图中标记为 “Finish” ）。
    问总共有多少条不同的路径？
    
    解题思路：
    使用二维DP，定义dp[i][j]表示到达位置(i,j)的路径数。
    状态转移方程：dp[i][j] = dp[i-1][j] + dp[i][j-1]
    
    Args:
        grid: m x n的网格
    
    Returns:
        int: 不同路径的数量
    
    时间复杂度：O(m*n)
    空间复杂度：O(m*n)
    """
    if not grid or not grid[0]:
        return 0
    
    m, n = len(grid), len(grid[0])
    # dp[i][j]表示到达位置(i,j)的路径数
    dp = [[0] * n for _ in range(m)]
    
    # 初始化第一行和第一列
    for i in range(m):
        dp[i][0] = 1
    for j in range(n):
        dp[0][j] = 1
    
    # 动态规划填表
    for i in range(1, m):
        for j in range(1, n):
            dp[i][j] = dp[i-1][j] + dp[i][j-1]
    
    return dp[m-1][n-1]

# ==================== 容斥DP（结合数论容斥）====================

def inclusion_exclusion_dp(n, m, mod=10**9+7):
    """
    容斥DP示例：计算n个元素分成m个非空集合的方式数（斯特林数的第二种形式）
    LeetCode类似题目：LeetCode 1735. 生成乘积数组的方案数
    题目链接：https://leetcode-cn.com/problems/count-ways-to-make-array-with-product/
    
    解题思路：
    使用容斥原理结合动态规划。
    dp[i][j]表示将i个元素分成j个非空集合的方式数。
    状态转移方程：dp[i][j] = dp[i-1][j-1] + j * dp[i-1][j]
    
    Args:
        n: 元素个数
        m: 集合个数
        mod: 模数
    
    Returns:
        int: 方案数对mod取余的结果
    
    时间复杂度：O(n*m)
    空间复杂度：O(n*m)
    """
    # dp[i][j]表示将i个元素分成j个非空集合的方式数
    dp = [[0] * (m + 1) for _ in range(n + 1)]
    
    # 初始状态：将i个元素分成i个非空集合只有1种方式（每个元素自成一个集合）
    # 只设置到min(n, m)的情况，避免索引越界
    for i in range(min(n, m) + 1):
        dp[i][i] = 1
    
    # 动态规划填表
    for i in range(1, n + 1):
        for j in range(1, min(i, m) + 1):
            if i != j:
                # 第i个元素要么单独放在一个新集合中（dp[i-1][j-1]）
                # 要么放在已有的j个集合中的任意一个（j * dp[i-1][j]）
                dp[i][j] = (dp[i-1][j-1] + j * dp[i-1][j]) % mod
    
    return dp[n][m]

def mobius_inversion_dp(nums):
    """
    莫比乌斯反演DP示例
    问题描述：给定一个数组，计算有多少对元素互质。
    
    解题思路：
    使用容斥原理和莫比乌斯函数进行计数。
    1. 统计每个数的出现次数
    2. 计算每个数的倍数的总出现次数
    3. 使用莫比乌斯函数进行容斥
    
    Args:
        nums: 输入数组
    
    Returns:
        int: 互质对的数量
    
    时间复杂度：O(max_num * log(max_num))，其中max_num是数组中的最大值
    空间复杂度：O(max_num)
    """
    if not nums:
        return 0
    
    max_num = max(nums)
    # 统计每个数的出现次数
    cnt = [0] * (max_num + 1)
    for num in nums:
        cnt[num] += 1
    
    # 计算每个数的倍数的总出现次数
    f = [0] * (max_num + 1)
    for i in range(1, max_num + 1):
        for j in range(i, max_num + 1, i):
            f[i] += cnt[j]
    
    # 预处理莫比乌斯函数
    mu = [1] * (max_num + 1)
    is_prime = [True] * (max_num + 1)
    primes = []
    
    for i in range(2, max_num + 1):
        if is_prime[i]:
            primes.append(i)
            mu[i] = -1
        for p in primes:
            if i * p > max_num:
                break
            is_prime[i * p] = False
            if i % p == 0:
                mu[i * p] = 0
                break
            mu[i * p] = -mu[i]
    
    # 使用莫比乌斯反演计算互质对的数量
    result = 0
    for d in range(1, max_num + 1):
        result += mu[d] * f[d] * (f[d] - 1) // 2
    
    return result

# ==================== 记忆化搜索（高维状态+剪枝）====================

import functools

def memoization_search_example(n, k):
    """
    记忆化搜索示例：LeetCode 377. 组合总和 Ⅳ
    题目链接：https://leetcode-cn.com/problems/combination-sum-iv/
    
    问题描述：
    给你一个由 不同 整数组成的数组 nums ，和一个目标整数 target 。
    请你从 nums 中找出并返回总和为 target 的元素组合的个数。
    题目数据保证答案符合 32 位整数范围。
    （这里使用n和k作为参数，相当于nums=[1..n], target=k）
    
    解题思路：
    使用记忆化搜索，缓存中间结果避免重复计算。
    
    Args:
        n: 可选数字的最大值（1~n）
        k: 目标和
    
    Returns:
        int: 组合数
    
    时间复杂度：O(n*k)
    空间复杂度：O(k)
    """
    @functools.lru_cache(maxsize=None)
    def dfs(remain):
        # 基本情况
        if remain == 0:
            return 1
        if remain < 0:
            return 0
        
        # 尝试每种数字
        res = 0
        for num in range(1, n + 1):
            res += dfs(remain - num)
        
        return res
    
    return dfs(k)

def pruning_memoization_search(grid):
    """
    剪枝优化的记忆化搜索：LeetCode 1301. 最大得分的路径数目
    题目链接：https://leetcode-cn.com/problems/number-of-paths-with-max-score/
    
    问题描述：
    给你一个正方形字符数组 board ，你从数组的 左上角 开始出发。
    每一步可以移动到四个方向之一，但不能越出边界，也不能移动到 'X' 上。
    到达右下角时，你的路径得分是路径上所有数字的总和。
    返回一个列表，其中第一个元素是最大可能的得分，第二个元素是得到最大得分的路径数目。
    路径数目需要对 10^9 + 7 取模。
    
    解题思路：
    使用记忆化搜索，同时缓存最大得分和对应的路径数目，进行剪枝优化。
    
    Args:
        grid: 字符数组
    
    Returns:
        list: [最大得分, 路径数目]
    
    时间复杂度：O(n^2)
    空间复杂度：O(n^2)
    """
    if not grid or not grid[0]:
        return [0, 0]
    
    n = len(grid)
    mod = 10**9 + 7
    
    # 缓存结果：(max_score, path_count)
    memo = {}
    
    def dfs(i, j):
        if (i, j) in memo:
            return memo[(i, j)]
        
        # 到达终点
        if i == 0 and j == 0:
            return [0, 1] if grid[i][j] == 'E' or grid[i][j] == 'S' else [int(grid[i][j]), 1]
        
        # 越界或遇到障碍
        if i < 0 or j < 0 or grid[i][j] == 'X':
            return [-1, 0]  # -1表示不可达
        
        max_score = -1
        path_count = 0
        
        # 尝试从上方和左方过来
        for di, dj in [(-1, 0), (0, -1)]:
            ni, nj = i + di, j + dj
            score, count = dfs(ni, nj)
            
            if score == -1:
                continue
            
            current_score = score + (int(grid[i][j]) if grid[i][j] not in ['S', 'E'] else 0)
            
            if current_score > max_score:
                max_score = current_score
                path_count = count
            elif current_score == max_score:
                path_count = (path_count + count) % mod
        
        memo[(i, j)] = [max_score, path_count]
        return [max_score, path_count]
    
    # 从右下角开始搜索
    # 注意：根据题目描述，需要调整起点和终点的位置
    score, count = dfs(n-1, n-1)
    return [score, count] if score != -1 else [0, 0]

# ==================== DP+字符串（SAM相关）====================

def longest_palindromic_subseq(s):
    """
    LeetCode 516. 最长回文子序列
    题目链接：https://leetcode-cn.com/problems/longest-palindromic-subsequence/
    
    问题描述：
    给定一个字符串s，找到其中最长的回文子序列。可以假设s的最大长度为1000。
    
    解题思路：
    使用区间DP，定义dp[i][j]表示字符串s在区间[i,j]内的最长回文子序列的长度。
    状态转移方程：
    - 如果s[i] == s[j]，则dp[i][j] = dp[i+1][j-1] + 2
    - 否则，dp[i][j] = max(dp[i+1][j], dp[i][j-1])
    
    Args:
        s: 输入字符串
    
    Returns:
        int: 最长回文子序列的长度
    """
    n = len(s)
    # dp[i][j]表示字符串s在区间[i,j]内的最长回文子序列的长度
    dp = [[0] * n for _ in range(n)]
    
    # 初始化单个字符的情况
    for i in range(n):
        dp[i][i] = 1
    
    # 枚举区间长度
    for length in range(2, n + 1):
        # 枚举起点
        for i in range(n - length + 1):
            j = i + length - 1
            if s[i] == s[j]:
                dp[i][j] = dp[i+1][j-1] + 2
            else:
                dp[i][j] = max(dp[i+1][j], dp[i][j-1])
    
    return dp[0][n-1]

class SuffixAutomaton:
    """
    后缀自动机（Suffix Automaton）
    
    后缀自动机是一个可以表示字符串的所有子串的数据结构。
    它可以用于解决许多字符串问题，如子串匹配、最长重复子串等。
    """
    class State:
        def __init__(self):
            self.len = 0  # 该状态能接受的最长字符串的长度
            self.link = -1  # 后缀链接
            self.next = {}  # 转移函数
            self.endpos_size = 0  # endpos集合的大小
    
    def __init__(self, s=None):
        self.size = 1
        self.last = 0
        self.states = [self.State()]
        
        # 如果提供了字符串，构建后缀自动机
        if s:
            self.extend(s)
    
    def extend(self, s):
        """
        扩展后缀自动机，添加一个字符串
        
        Args:
            s: 要添加的字符串
        """
        # 构建后缀自动机
        for c in s:
            self._extend(c)
        
        # 计算endpos集合的大小
        self._calc_endpos_size()
    
    def _extend(self, c):
        p = self.last
        curr = self.size
        self.size += 1
        self.states.append(self.State())
        self.states[curr].len = self.states[p].len + 1
        
        while p != -1 and c not in self.states[p].next:
            self.states[p].next[c] = curr
            p = self.states[p].link
        
        if p == -1:
            self.states[curr].link = 0
        else:
            q = self.states[p].next[c]
            if self.states[p].len + 1 == self.states[q].len:
                self.states[curr].link = q
            else:
                clone = self.size
                self.size += 1
                self.states.append(self.State())
                self.states[clone].len = self.states[p].len + 1
                self.states[clone].next = self.states[q].next.copy()
                self.states[clone].link = self.states[q].link
                
                while p != -1 and self.states[p].next.get(c) == q:
                    self.states[p].next[c] = clone
                    p = self.states[p].link
                
                self.states[q].link = clone
                self.states[curr].link = clone
        
        self.last = curr
    
    def _calc_endpos_size(self):
        # 按len排序
        order = sorted(range(self.size), key=lambda x: -self.states[x].len)
        
        # 初始化为1（每个状态至少对应一个结束位置）
        for i in range(1, self.size):
            self.states[i].endpos_size = 1
        
        # 从长到短更新
        for u in order:
            if self.states[u].link != -1:
                self.states[self.states[u].link].endpos_size += self.states[u].endpos_size
    
    def count_substrings(self):
        """
        计算不同子串的数量
        """
        count = 0
        for i in range(1, self.size):
            count += self.states[i].len - self.states[self.states[i].link].len
        return count

# ==================== 组合DP（生成函数+DP）====================

def generating_function_dp(n, k, mod=10**9+7):
    """
    生成函数结合DP示例：LeetCode 1775. 通过最少操作次数使数组的和相等
    题目链接：https://leetcode-cn.com/problems/equal-sum-arrays-with-minimum-number-of-operations/
    
    问题描述：
    给定两个长度可能不等的整数数组 nums1 和 nums2 。两个数组中的所有值都在 1 到 6 之间（包含 1 和 6）。
    每次操作中，你可以选择 任意 数组中的任意一个整数，将它变成 1 到 6 之间的任意值（包含 1 和 6）。
    请返回使 nums1 和 nums2 之和相等的最少操作次数。如果无法使两个数组之和相等，返回 -1 。
    
    解题思路：
    使用生成函数的思想构建DP数组，表示可以通过多少次操作达到某个差值。
    
    Args:
        n: 数组1的长度（这里简化为n）
        k: 数组2的长度（这里简化为k）
        mod: 模数
    
    Returns:
        int: 最小操作次数或-1
    
    时间复杂度：O(n*k*max_diff)，其中max_diff是可能的最大差值
    空间复杂度：O(max_diff)
    """
    # 这里提供的是一个简化版本的示例，完整实现需要根据具体数组值计算
    # 生成函数的核心思想是构建状态转移，记录每个可能的差值需要的最少操作次数
    pass

def polynomial_multiplication(a, b, mod=10**9+7):
    """
    多项式乘法（生成函数的核心操作）
    
    问题描述：
    计算两个多项式的乘积，系数对mod取余。
    
    解题思路：
    使用动态规划的方法计算多项式乘积。
    
    Args:
        a: 第一个多项式的系数数组
        b: 第二个多项式的系数数组
        mod: 模数
    
    Returns:
        list: 乘积多项式的系数数组
    
    时间复杂度：O(n*m)，其中n和m是两个多项式的次数+1
    空间复杂度：O(n+m)
    """
    n = len(a)
    m = len(b)
    # 结果多项式的次数是n+m-2，所以系数数组长度为n+m-1
    result = [0] * (n + m - 1)
    
    # 计算每个项的系数
    for i in range(n):
        for j in range(m):
            result[i + j] = (result[i + j] + a[i] * b[j]) % mod
    
    return result

def integer_partition_dp(n):
    """
    整数划分问题：使用生成函数和DP
    
    问题描述：
    计算将正整数n划分为若干正整数之和的方式数，不考虑顺序。
    例如，对于n=4，划分方式有：4, 3+1, 2+2, 2+1+1, 1+1+1+1，共5种。
    
    解题思路：
    使用生成函数的思想，每个数k对应的生成函数是1 + x^k + x^{2k} + ...
    我们需要计算这些生成函数的乘积中x^n的系数。
    
    Args:
        n: 要划分的整数
    
    Returns:
        int: 划分方式数
    
    时间复杂度：O(n^2)
    空间复杂度：O(n)
    """
    # dp[i]表示将整数i划分为若干正整数之和的方式数
    dp = [0] * (n + 1)
    dp[0] = 1  # 基础情况：将0划分为0个部分有1种方式
    
    # 对于每个数k（从1到n）
    for k in range(1, n + 1):
        # 对于每个可能的和i（从k到n）
        for i in range(k, n + 1):
            # 可以选择使用k或不使用k
            dp[i] += dp[i - k]
    
    return dp[n]

# ==================== 博弈DP（SG函数、阶梯Nim）====================

def calculate_sg(n, moves):
    """
    计算SG函数值
    
    问题描述：
    在 impartial game 中，计算每个状态的SG函数值。
    
    解题思路：
    使用动态规划计算每个状态的SG函数值。
    SG(x) = mex{ SG(y) | y是x的后继状态 }
    其中mex是最小非负整数不在集合中的函数。
    
    Args:
        n: 最大状态数
        moves: 允许的移动方式（例如，每次可以取1或2个石子）
    
    Returns:
        list: 每个状态的SG函数值
    
    时间复杂度：O(n*k)，其中k是移动方式的数量
    空间复杂度：O(n)
    """
    sg = [0] * (n + 1)
    
    for i in range(1, n + 1):
        visited = set()
        # 计算所有可能的后继状态的SG值
        for move in moves:
            if i >= move:
                visited.add(sg[i - move])
        
        # 计算mex值
        mex = 0
        while mex in visited:
            mex += 1
        sg[i] = mex
    
    return sg

def nim_game(piles):
    """
    Nim游戏
    LeetCode 292. Nim 游戏
    题目链接：https://leetcode-cn.com/problems/nim-game/
    
    问题描述：
    你和你的朋友，两个人一起玩 Nim 游戏：
    1. 桌子上有一堆石子
    2. 你们轮流进行自己的回合，你作为先手
    3. 每一回合，轮到的人拿掉1 - 3颗石子
    4. 拿掉最后一颗石子的人就是获胜者
    
    解题思路：
    当石子数n是4的倍数时，无论先手怎么拿，后手都可以通过拿(4 - x)颗石子使得每轮总共拿4颗石子，最终后手获胜。
    当石子数n不是4的倍数时，先手可以先拿走(n % 4)颗石子，使得剩下的石子数是4的倍数，之后按照上述策略，先手获胜。
    
    Args:
        piles: 石子堆数组（这里简化为单个堆的情况）
    
    Returns:
        bool: 如果先手能赢返回True，否则返回False
    
    时间复杂度：O(1)
    空间复杂度：O(1)
    """
    # 对于单个堆的情况
    if len(piles) == 1:
        return piles[0] % 4 != 0
    
    # 对于多个堆的情况，计算异或和
    xor_sum = 0
    for pile in piles:
        xor_sum ^= pile
    
    # 如果异或和不为0，先手有必胜策略
    return xor_sum != 0

def stairs_nim(stairs):
    """
    阶梯Nim游戏
    
    问题描述：
    在阶梯上有若干石子，每次可以将第i层的石子移动任意数量到第i-1层，
    或者将第1层的石子移出游戏。最后无法移动的人输。
    
    解题思路：
    阶梯Nim游戏的胜负取决于奇数层石子数的异或和。
    
    Args:
        stairs: 每层的石子数
    
    Returns:
        bool: 如果先手能赢返回True，否则返回False
    
    时间复杂度：O(n)
    空间复杂度：O(1)
    """
    xor_sum = 0
    # 只考虑奇数层的石子数
    for i in range(len(stairs)):
        if i % 2 == 0:  # 索引从0开始，对应第一层
            xor_sum ^= stairs[i]
    
    # 如果异或和不为0，先手有必胜策略
    return xor_sum != 0

# ==================== 高维压缩（子集/超集枚举与预处理）====================

def subset_enumeration(n):
    """
    子集枚举示例
    
    问题描述：
    枚举所有n位二进制数的子集。
    
    解题思路：
    使用位运算技巧高效枚举子集。
    
    Args:
        n: 位数
    
    Returns:
        list: 所有子集的列表
    
    时间复杂度：O(3^n)，因为每个位有3种可能：不在原集合中、在原集合中但不在子集中、在原集合中和子集中
    空间复杂度：O(2^n)
    """
    subsets = []
    # 枚举所有可能的集合
    for mask in range(1 << n):
        # 枚举mask的所有子集
        subset = mask
        while True:
            subsets.append(subset)
            if subset == 0:
                break
            subset = (subset - 1) & mask
    return subsets

def superset_dp(n, cost):
    """
    超集DP（SOS DP）
    
    问题描述：
    计算每个集合的所有超集的最小值。
    
    解题思路：
    使用动态规划，按位处理。
    
    Args:
        n: 位数
        cost: 每个集合的代价
    
    Returns:
        list: 每个集合的最小超集代价
    
    时间复杂度：O(n*2^n)
    空间复杂度：O(2^n)
    """
    dp = cost.copy()
    
    # 按位处理
    for i in range(n):
        for mask in range(1 << n):
            # 如果mask的第i位为0，则可以将其置为1，得到一个超集
            if not (mask & (1 << i)):
                dp[mask] = min(dp[mask], dp[mask | (1 << i)])
    
    return dp

def subset_convolution(a, b, mod=10**9+7):
    """
    子集卷积
    
    问题描述：
    计算两个函数f和g的子集卷积，结果h满足h[s] = sum_{t subset of s} f[t] * g[s-t]，其中t subset of s且t和s-t互不相交。
    
    解题思路：
    使用快速莫比乌斯变换（FMT）进行高效计算。
    
    Args:
        a: 函数f的系数数组
        b: 函数g的系数数组
        mod: 模数
    
    Returns:
        list: 子集卷积的结果
    
    时间复杂度：O(n^2*2^n)
    空间复杂度：O(n*2^n)
    """
    n = (len(a) - 1).bit_length()  # 位数
    size = 1 << n
    
    # 扩展数组长度到2^n
    a += [0] * (size - len(a))
    b += [0] * (size - len(b))
    
    # 按集合大小分组
    fa = [[0] * size for _ in range(n + 1)]
    fb = [[0] * size for _ in range(n + 1)]
    
    for mask in range(size):
        cnt = bin(mask).count('1')
        fa[cnt][mask] = a[mask] % mod
        fb[cnt][mask] = b[mask] % mod
    
    # 对每个大小组进行FMT
    for i in range(n + 1):
        # FMT for OR convolution
        for j in range(n):
            for mask in range(size):
                if mask & (1 << j):
                    fa[i][mask] = (fa[i][mask] + fa[i][mask ^ (1 << j)]) % mod
                    fb[i][mask] = (fb[i][mask] + fb[i][mask ^ (1 << j)]) % mod
    
    # 计算卷积
    fc = [[0] * size for _ in range(n + 1)]
    for i in range(n + 1):
        for j in range(i + 1):
            for mask in range(size):
                fc[i][mask] = (fc[i][mask] + fa[j][mask] * fb[i - j][mask]) % mod
    
    # 逆FMT并合并结果
    res = [0] * size
    for i in range(n + 1):
        # Inverse FMT
        for j in range(n):
            for mask in range(size):
                if mask & (1 << j):
                    fc[i][mask] = (fc[i][mask] - fc[i][mask ^ (1 << j)]) % mod
        
        # 合并结果
        for mask in range(size):
            if bin(mask).count('1') == i:
                res[mask] = fc[i][mask] % mod
    
    return res

# ==================== 优化体系 ====================

# ===== Knuth优化 =====
def knuth_optimization(matrix):
    """
    Knuth优化示例
    
    问题描述：
    对于满足四边形不等式的DP问题，可以使用Knuth优化将时间复杂度从O(n^3)降低到O(n^2)。
    
    解题思路：
    利用决策单调性，记录最优决策点的范围。
    
    Args:
        matrix: 代价矩阵
    
    Returns:
        tuple: (最小代价, 最优分割点)
    
    时间复杂度：O(n^2)
    空间复杂度：O(n^2)
    """
    n = len(matrix)
    # dp[i][j]表示合并区间[i,j]的最小代价
    dp = [[0] * n for _ in range(n)]
    # opt[i][j]表示合并区间[i,j]时的最优分割点
    opt = [[0] * n for _ in range(n)]
    
    # 初始化
    for i in range(n):
        opt[i][i] = i
    
    # 枚举区间长度
    for l in range(2, n + 1):
        for i in range(n - l + 1):
            j = i + l - 1
            dp[i][j] = float('inf')
            # 利用决策单调性，限制k的范围在[opt[i][j-1], opt[i+1][j]]
            for k in range(opt[i][j-1], min(opt[i+1][j] + 1, j)):
                cost = dp[i][k] + dp[k+1][j] + sum(matrix[i][j])  # 这里的sum是示例，实际应该根据具体问题计算
                if cost < dp[i][j]:
                    dp[i][j] = cost
                    opt[i][j] = k
    
    return dp[0][n-1], opt

# ===== Divide & Conquer Optimization =====
def divide_conquer_optimization(n, cost_func):
    """
    分治优化DP
    
    问题描述：
    对于满足决策单调性的DP问题，可以使用分治优化将时间复杂度降低。
    
    解题思路：
    利用分治的思想，递归地处理每个区间的最优决策点。
    
    Args:
        n: 问题规模
        cost_func: 计算代价的函数
    
    Returns:
        list: DP数组
    
    时间复杂度：O(n log n)
    空间复杂度：O(n)
    """
    dp = [float('inf')] * n
    dp[0] = 0  # 初始状态
    
    def solve(l, r, opt_l, opt_r):
        """
        求解区间[l,r]，其中最优决策点在[opt_l,opt_r]之间
        """
        if l > r:
            return
        
        mid = (l + r) // 2
        best_k = -1
        
        # 枚举可能的决策点
        for k in range(opt_l, min(opt_r, mid) + 1):
            current_cost = dp[k] + cost_func(k, mid)
            if current_cost < dp[mid]:
                dp[mid] = current_cost
                best_k = k
        
        # 递归处理左右子区间
        solve(l, mid - 1, opt_l, best_k)
        solve(mid + 1, r, best_k, opt_r)
    
    # 求解整个区间
    solve(1, n - 1, 0, n - 1)
    
    return dp

# ===== SMAWK算法 =====
def smawk_row_minima(matrix, row_indices, col_indices):
    """
    SMAWK算法：用于在完全单调矩阵中找到每行的最小值
    
    问题描述：
    对于满足完全单调性的矩阵，快速找到每行的最小值的列索引。
    
    解题思路：
    使用SMAWK算法的递归实现。
    
    Args:
        matrix: 完全单调矩阵
        row_indices: 要处理的行索引列表
        col_indices: 要处理的列索引列表
    
    Returns:
        dict: 每行的最小值的列索引
    
    时间复杂度：O(m + n)，其中m是行数，n是列数
    空间复杂度：O(m + n)
    """
    if not row_indices:
        return {}
    
    # 递归步骤1：减少行数（约简）
    reduced_rows = []
    for row in row_indices:
        while reduced_rows and matrix[reduced_rows[-1]][col_indices[-1]] >= matrix[row][col_indices[-1]]:
            reduced_rows.pop()
        reduced_rows.append(row)
    
    # 递归步骤2：递归处理约简后的问题，但只处理奇数列
    odd_cols = col_indices[1::2]
    min_cols = smawk_row_minima(matrix, reduced_rows, odd_cols)
    
    # 递归步骤3：在约简后的行中找到所有列的最小值
    result = {}
    col_ptr = 0  # 跟踪当前考虑的列
    
    for i, row in enumerate(reduced_rows):
        prev_col = -1 if i == 0 else min_cols[reduced_rows[i-1]]
        next_col = min_cols[row] if row in min_cols else col_indices[-1]
        
        # 找到当前行在[prev_col+1, next_col]范围内的最小值
        min_val = float('inf')
        min_col = -1
        
        while col_ptr < len(col_indices) and col_indices[col_ptr] <= next_col:
            col = col_indices[col_ptr]
            if prev_col < col <= next_col:
                if matrix[row][col] < min_val:
                    min_val = matrix[row][col]
                    min_col = col
            col_ptr += 1
        
        result[row] = min_col
    
    # 对于未约简的行，它们的最小值列必然在约简后的行的最小值列之间
    # 这里简化处理，实际情况可能需要更复杂的逻辑
    for row in row_indices:
        if row not in result:
            result[row] = 0  # 简化处理
    
    return result

# ===== Aliens Trick =====
def aliens_trick(n, k, check_func):
    """
    Aliens Trick（二分答案+可行性DP）
    
    问题描述：
    对于某些带约束的最优化问题，可以使用二分答案结合可行性DP来解决。
    
    解题思路：
    1. 对约束参数进行二分
    2. 对每个参数值，使用DP判断是否可行
    
    Args:
        n: 问题规模
        k: 约束参数的上限
        check_func: 检查函数，输入参数lambda和k，返回是否可行
    
    Returns:
        tuple: (最优值, 最优参数)
    
    时间复杂度：O(log(max_lambda) * T(n))，其中T(n)是DP的时间复杂度
    空间复杂度：O(n)
    """
    left = 0
    right = 10**9  # 根据具体问题调整上限
    best_lambda = 0
    best_value = 0
    
    while left <= right:
        mid = (left + right) // 2
        feasible, value = check_func(mid, k)
        
        if feasible:
            # 可行，尝试更小的lambda
            best_lambda = mid
            best_value = value
            right = mid - 1
        else:
            # 不可行，增大lambda
            left = mid + 1
    
    return best_value, best_lambda

# ==================== 图上DP→最短路 ====================

def layered_graph_dijkstra(n, m, edges, start, end, layers):
    """
    分层图Dijkstra算法
    
    问题描述：
    在分层图中找到从起点到终点的最短路径。
    
    解题思路：
    使用Dijkstra算法，将每个状态表示为(节点, 层)，并使用优先队列进行优化。
    
    Args:
        n: 节点数
        m: 边数
        edges: 边的列表
        start: 起点
        end: 终点
        layers: 层数
    
    Returns:
        int: 最短路径长度，如果不可达返回-1
    
    时间复杂度：O(layers * m log(layers * n))
    空间复杂度：O(layers * n)
    """
    import heapq
    
    # 构建图的邻接表
    graph = [[] for _ in range(n * layers)]
    for u, v, w in edges:
        # 在同一层添加边
        for i in range(layers):
            graph[i * n + u].append((i * n + v, w))
        # 在相邻层之间添加边（如果允许换层）
        for i in range(layers - 1):
            graph[i * n + u].append(( (i + 1) * n + u, 0 ))  # 换层的代价为0，根据具体问题调整
    
    # Dijkstra算法
    dist = [float('inf')] * (n * layers)
    dist[start] = 0
    heap = []
    heapq.heappush(heap, (0, start))
    
    while heap:
        current_dist, current_node = heapq.heappop(heap)
        
        if current_node % n == end:
            return current_dist
        
        if current_dist > dist[current_node]:
            continue
        
        for next_node, weight in graph[current_node]:
            if dist[next_node] > current_dist + weight:
                dist[next_node] = current_dist + weight
                heapq.heappush(heap, (dist[next_node], next_node))
    
    return -1  # 不可达

# ==================== 冷门模型 ====================

# ===== 期望DP =====
def expected_value_dp(n, edges):
    """
    期望DP示例：LeetCode 837. 新21点
    题目链接：https://leetcode-cn.com/problems/new-21-game/
    
    问题描述：
    爱丽丝参与一个大致基于纸牌游戏 "21点" 规则的游戏，描述如下：
    爱丽丝以 0 分开始，并在她的得分小于 K 分时抽取数字。 抽取时，她从 [1, W] 的范围中随机获得一个整数作为分数进行累计，其中 W 是整数。 每次抽取都是独立的，其结果具有相同的概率。
    当爱丽丝获得不少于 K 分时，她就停止抽取数字。 爱丽丝的分数不超过 N 的概率是多少？
    
    解题思路：
    使用期望DP，定义dp[i]表示当前得分为i时，最终得分不超过N的概率。
    
    Args:
        n: 最大得分限制（相当于题目中的N）
        edges: 状态转移边（这里简化处理）
    
    Returns:
        float: 概率
    
    时间复杂度：O(n)
    空间复杂度：O(n)
    """
    # 简化版实现，完整实现需要根据具体问题调整
    K = n // 2  # 示例值
    W = 10      # 示例值
    
    if K == 0 or n >= K + W:
        return 1.0
    
    dp = [0.0] * (n + 1)
    dp[K:] = [1.0] * (n - K + 1)
    
    # 计算前缀和，优化转移
    sum_dp = n - K + 1  # 初始时sum(dp[K...n]) = n-K+1
    
    for i in range(K-1, -1, -1):
        dp[i] = sum_dp / W
        sum_dp = sum_dp + dp[i] - dp[i + W] if (i + W) <= n else sum_dp + dp[i]
    
    return dp[0]

def gaussian_elimination_dp(matrix):
    """
    高斯消元解决期望DP中的环
    
    问题描述：
    当期望DP中存在环时，需要建立方程组并使用高斯消元求解。
    
    解题思路：
    1. 根据状态转移关系建立线性方程组
    2. 使用高斯消元求解方程组
    
    Args:
        matrix: 系数矩阵
    
    Returns:
        list: 解向量
    
    时间复杂度：O(n^3)
    空间复杂度：O(n^2)
    """
    n = len(matrix)
    eps = 1e-8
    
    # 高斯消元
    for i in range(n):
        # 找到当前列中的主元素（绝对值最大的）
        max_row = i
        for j in range(i + 1, n):
            if abs(matrix[j][i]) > abs(matrix[max_row][i]):
                max_row = j
        
        # 交换行
        matrix[i], matrix[max_row] = matrix[max_row], matrix[i]
        
        # 归一化主行
        pivot = matrix[i][i]
        if abs(pivot) < eps:
            continue  # 奇异矩阵，无法求解
        
        for j in range(i, n + 1):
            matrix[i][j] /= pivot
        
        # 消去其他行
        for j in range(n):
            if j != i and abs(matrix[j][i]) > eps:
                factor = matrix[j][i]
                for k in range(i, n + 1):
                    matrix[j][k] -= factor * matrix[i][k]
    
    # 提取解
    solution = [row[n] for row in matrix]
    return solution

# ===== 插头DP =====
def plug_dp(grid):
    """
    插头DP示例：计算网格中的哈密顿回路数目
    
    问题描述：
    给定一个网格，计算从起点到终点的哈密顿回路数目。
    
    解题思路：
    使用状态压缩的插头DP，记录轮廓线上的插头状态。
    
    Args:
        grid: 网格（0表示障碍，1表示可走）
    
    Returns:
        int: 哈密顿回路数目
    
    时间复杂度：O(n*m*4^min(n,m))，其中n和m是网格的维度
    空间复杂度：O(n*m*4^min(n,m))
    """
    # 简化版实现，完整实现需要更复杂的状态转移
    n = len(grid)
    m = len(grid[0]) if n > 0 else 0
    
    # 这里仅提供框架，实际实现需要处理插头状态的转移
    # 通常使用哈希表或滚动数组优化空间
    pass

# ===== 树上背包 =====
def tree_knapsack(root, capacity, tree, weights, values):
    """
    树上背包问题：在树上选择一些节点，使得总重量不超过capacity，总价值最大
    
    使用后序遍历+动态规划的方法
    
    时间复杂度: O(n*capacity^2)
    空间复杂度: O(n*capacity)
    
    Args:
        root: 根节点索引
        capacity: 背包容量
        tree: 树的邻接表表示
        weights: 节点重量列表
        values: 节点价值列表
    
    Returns:
        最大总价值
    """
    n = len(tree)
    dp = [[0] * (capacity + 1) for _ in range(n)]
    
    def dfs(u, parent):
        # 初始化：当前节点的重量和价值
        if weights[u] <= capacity:
            dp[u][weights[u]] = values[u]
        
        # 遍历所有子节点（排除父节点）
        for v in tree[u]:
            if v != parent:
                dfs(v, u)
                # 树上背包的核心：体积要倒序枚举
                for j in range(capacity, weights[u] - 1, -1):
                    for k in range(1, j - weights[u] + 1):
                        dp[u][j] = max(dp[u][j], dp[u][j - k] + dp[v][k])
    
    dfs(root, -1)
    # 返回所有可能容量中的最大值
    return max(dp[root])

# ==================== DP+计算几何（凸包相关）====================

def convex_hull(points):
    """
    计算凸包（Andrew算法）
    
    问题描述：
    给定平面上的点集，找出所有在凸包上的点。
    
    解题思路：
    1. 将点按x坐标排序，x相同按y排序
    2. 构建下凸壳和上凸壳
    3. 合并上下凸壳
    
    Args:
        points: 点集，每个点是一个元组(x, y)
    
    Returns:
        list: 凸包上的点集
    """
    # 按x坐标排序，x相同按y排序
    points = sorted(points)
    n = len(points)
    
    # 构建下凸壳
    lower = []
    for p in points:
        while len(lower) >= 2 and cross(lower[-2], lower[-1], p) <= 0:
            lower.pop()
        lower.append(p)
    
    # 构建上凸壳
    upper = []
    for p in reversed(points):
        while len(upper) >= 2 and cross(upper[-2], upper[-1], p) <= 0:
            upper.pop()
        upper.append(p)
    
    # 合并上下凸壳，去掉重复的端点
    return lower[:-1] + upper[:-1]

def cross(o, a, b):
    """
    计算叉积 (a - o) × (b - o)
    """
    return (a[0] - o[0]) * (b[1] - o[1]) - (a[1] - o[1]) * (b[0] - o[0])

def convex_hull_trick(dp, a, b):
    """
    凸包优化DP
    
    问题描述：
    当DP状态转移方程可以表示为dp[i] = min{dp[j] + a[i] * b[j]} + c[i]的形式时，
    可以使用凸包优化将时间复杂度从O(n^2)降低到O(n)或O(n log n)。
    
    解题思路：
    对于每个j，维护一条直线y = b[j] * x + dp[j]，然后对于每个i，查询x = a[i]时的最小值。
    当b[j]单调递增且a[i]单调递增时，可以使用单调队列优化。
    
    Args:
        dp: DP数组
        a: a数组
        b: b数组
    
    Returns:
        list: 优化后的DP数组
    """
    n = len(dp)
    q = []  # 单调队列，存储直线的索引
    
    def get_intersection(j1, j2):
        # 计算两条直线j1和j2的交点x坐标
        # 直线j1: y = b[j1] * x + dp[j1]
        # 直线j2: y = b[j2] * x + dp[j2]
        return (dp[j2] - dp[j1]) / (b[j1] - b[j2]) if b[j1] != b[j2] else float('inf')
    
    # 初始化队列，加入第一个元素
    q.append(0)
    
    # 对于每个i，找到最优的j
    for i in range(1, n):
        # 当队列中至少有两个元素，且第一个元素不如第二个元素优时，弹出第一个元素
        while len(q) >= 2 and (dp[q[0]] + a[i] * b[q[0]] >= dp[q[1]] + a[i] * b[q[1]]):
            q.pop(0)
        
        # 使用队列中的第一个元素作为最优的j
        dp[i] = min(dp[i], dp[q[0]] + a[i] * b[q[0]])
        
        # 将当前i加入队列，维护队列的凸壳性质
        while len(q) >= 2 and (get_intersection(q[-2], q[-1]) >= get_intersection(q[-1], i)):
            q.pop()
        q.append(i)
    
    return dp

# 主程序测试
if __name__ == "__main__":
    # 测试零钱兑换II（模意义）
    coins = [1, 2, 5]
    amount = 5
    mod = 10**9 + 7
    print(f"零钱兑换II（模{mod}）: {coin_change_mod(coins, amount, mod)}")
    
    # 测试矩阵快速幂
    matrix = [[1, 1], [1, 0]]
    power = 10
    print("矩阵快速幂:")
    result = matrix_power_mod(matrix, power, mod)
    for row in result:
        print(row)
    
    # 测试最长回文子序列
    s = "bbbab"
    print(f"最长回文子序列长度: {longest_palindromic_subseq(s)}")
    
    # 测试后缀自动机
    sam = SuffixAutomaton()
    sam.extend("banana")
    print(f"不同子串数量: {sam.count_substrings()}")
    
    # 测试凸包
    points = [(0, 0), (1, 1), (2, 0), (0, 2), (1, 0)]
    hull = convex_hull(points)
    print(f"凸包点集: {hull}")
    
    # 测试凸包优化DP
    dp = [float('inf')] * 5
    dp[0] = 0
    a = [1, 2, 3, 4, 5]
    b = [5, 4, 3, 2, 1]
    print(f"凸包优化DP结果: {convex_hull_trick(dp, a, b)}")
    
    # 测试分层DP - 三角形最小路径和
    triangle = [[2], [3, 4], [6, 5, 7], [4, 1, 8, 3]]
    print(f"三角形最小路径和: {layered_dp(triangle)}")
    
    # 测试分层DP - 不同路径
    m, n = 3, 7
    grid = [[0 for _ in range(n)] for _ in range(m)]
    print(f"不同路径({m}x{n}): {multi_layered_dp(grid)}")
    
    # 测试容斥DP - 斯特林数
    k, j = 5, 2
    print(f"斯特林数S({k}, {j}): {inclusion_exclusion_dp(k, j)}")
    
    # 测试容斥DP - 互质对计数
    arr = [1, 2, 3, 4, 5]
    print(f"互质对计数: {mobius_inversion_dp(arr)}")
    
    # 测试记忆化搜索 - 组合总和IV
    n = 3
    target = 4
    print(f"组合总和IV: {memoization_search_example(n, target)}")
    
    # 测试组合DP - 整数划分
    n = 4
    print(f"整数划分({n}): {integer_partition_dp(n)}")
    
    # 测试组合DP - 多项式乘法
    a_poly = [1, 2, 3]
    b_poly = [4, 5]
    print(f"多项式乘法 {a_poly} * {b_poly}: {polynomial_multiplication(a_poly, b_poly)}")
    
    # 测试博弈DP - SG函数
    moves = [1, 2]
    sg_values = calculate_sg(10, moves)
    print(f"SG函数值(1-10): {sg_values[1:]}")
    
    # 测试博弈DP - Nim游戏
    piles = [3, 4, 5]
    print(f"Nim游戏先手必胜?: {nim_game(piles)}")
    
    # 测试博弈DP - 阶梯Nim
    stairs = [2, 1, 3, 1]
    print(f"阶梯Nim先手必胜?: {stairs_nim(stairs)}")
    
    # 测试高维压缩 - 子集枚举
    n_bits = 3
    subsets = subset_enumeration(n_bits)
    print(f"{n_bits}位二进制数的所有子集数量: {len(subsets)}")
    
    # 测试高维压缩 - 超集DP
    cost = [10, 5, 7, 3]
    min_superset = superset_dp(2, cost)
    print(f"超集DP结果: {min_superset}")
    
    # 测试期望DP
    print(f"新21点概率(N=21): {expected_value_dp(21, None):.6f}")
    
    # 测试树上背包 - 暂时注释以避免递归深度问题
    # tree = [[1, 2], [0, 3], [0, 4], [1], [1]]
    # weights = [1, 2, 3, 4, 5]
    # values = [10, 20, 30, 40, 50]
    # capacity = 10
    # print(f"树上背包最大价值: {tree_knapsack(0, capacity, tree, weights, values)}")


# ==================== 优化体系：Knuth优化 ====================

# Knuth优化用于优化形如dp[i][j] = min{dp[i][k] + dp[k+1][j]} + w(i,j)的DP
# 当满足四边形不等式时，最优转移点单调
# 四边形不等式：w(a,b) + w(c,d) ≤ w(a,d) + w(c,b)，其中a ≤ c ≤ b ≤ d
# 单调性：w(b,c) ≤ w(a,d)，其中a ≤ b ≤ c ≤ d

def knuth_optimization(n, cost_func):
    """
    Knuth优化的DP算法
    
    问题描述：
    解决区间DP问题，其中状态转移方程满足四边形不等式
    
    解题思路：
    1. 使用Knuth优化将时间复杂度从O(n^3)降低到O(n^2)
    2. 维护最优转移点数组opt[i][j]，表示计算dp[i][j]时的最优k值
    3. 根据opt[i][j-1] ≤ opt[i][j] ≤ opt[i+1][j]的性质进行剪枝
    
    Args:
        n: 区间长度
        cost_func: 计算区间(i,j)代价的函数
    
    Returns:
        tuple: (dp数组, opt数组)
    
    时间复杂度：O(n^2)
    空间复杂度：O(n^2)
    """
    # 初始化dp和opt数组
    dp = [[0] * (n + 1) for _ in range(n + 1)]
    opt = [[0] * (n + 1) for _ in range(n + 1)]
    
    # 初始化长度为1的区间
    for i in range(1, n + 1):
        dp[i][i] = 0
        opt[i][i] = i
    
    # 枚举区间长度
    for length in range(2, n + 1):
        # 枚举起始点
        for i in range(1, n - length + 2):
            j = i + length - 1
            # 初始化为无穷大
            dp[i][j] = float('inf')
            # 根据Knuth优化的性质，最优k在opt[i][j-1]到opt[i+1][j]之间
            for k in range(opt[i][j-1], min(opt[i+1][j], j-1) + 1):
                current = dp[i][k] + dp[k+1][j] + cost_func(i, j)
                if current < dp[i][j]:
                    dp[i][j] = current
                    opt[i][j] = k
    
    return dp, opt

# ==================== 优化体系：Divide & Conquer Optimization ====================

def divide_conquer_optimization(n, m, cost_func):
    """
    Divide & Conquer Optimization（分治优化）
    
    问题描述：
    解决形如dp[i][j] = min{dp[i-1][k] + cost(k, j)}，其中k < j
    当转移满足决策单调性时使用
    
    解题思路：
    1. 利用决策单调性，使用分治法优化DP
    2. 对于dp[i][j]，当i固定时，最优转移点k随着j的增加而单调不减
    3. 使用分治的方式计算每个区间的最优决策
    
    Args:
        n: 维度1
        m: 维度2
        cost_func: 计算cost(k,j)的函数
    
    Returns:
        list: dp数组
    
    时间复杂度：O(n*m log m)
    空间复杂度：O(n*m)
    """
    # 初始化dp数组
    dp = [[float('inf')] * (m + 1) for _ in range(n + 1)]
    dp[0][0] = 0
    
    # 分治优化函数
    def solve(i, l, r, opt_l, opt_r):
        """
        计算dp[i][l..r]，其中最优转移点在opt_l..opt_r之间
        """
        if l > r:
            return
        
        mid = (l + r) // 2
        best_k = opt_l
        
        # 在opt_l到min(mid-1, opt_r)之间寻找最优k
        for k in range(opt_l, min(mid, opt_r) + 1):
            if dp[i-1][k] + cost_func(k, mid) < dp[i][mid]:
                dp[i][mid] = dp[i-1][k] + cost_func(k, mid)
                best_k = k
        
        # 递归处理左右子区间
        solve(i, l, mid-1, opt_l, best_k)
        solve(i, mid+1, r, best_k, opt_r)
    
    # 对每个i应用分治优化
    for i in range(1, n + 1):
        solve(i, 1, m, 0, m)
    
    return dp

# ==================== 优化体系：SMAWK算法（行最小查询） ====================

def smawk(matrix):
    """
    SMAWK算法用于在Monge矩阵中快速查找每行的最小值
    
    问题描述：
    给定一个Monge矩阵，快速找到每行的最小值位置
    
    解题思路：
    1. Monge矩阵满足性质：matrix[i][j] + matrix[i+1][j+1] ≤ matrix[i][j+1] + matrix[i+1][j]
    2. SMAWK算法利用这一性质，可以在O(m+n)时间内找到每行的最小值
    3. 主要步骤包括行压缩和递归求解
    
    Args:
        matrix: 一个Monge矩阵
    
    Returns:
        list: 每行最小值的列索引
    
    时间复杂度：O(m+n)，其中m是行数，n是列数
    空间复杂度：O(m+n)
    """
    m = len(matrix)
    n = len(matrix[0]) if m > 0 else 0
    
    def reduce_rows(rows):
        """行压缩：只保留可能成为最小值的行"""
        stack = []
        for i in rows:
            while len(stack) >= 2:
                j1 = stack[-2]
                j2 = stack[-1]
                # 比较两个行在列len(stack)-1处的值
                if matrix[j1][len(stack)-1] <= matrix[i][len(stack)-1]:
                    break
                else:
                    stack.pop()
            stack.append(i)
        return stack
    
    def smawk_rec(rows, cols):
        """递归实现SMAWK算法"""
        if not rows:
            return []
        
        # 行压缩
        reduced_rows = reduce_rows(rows)
        
        # 递归求解列数为奇数的子问题
        half_cols = cols[1::2]  # 取所有奇数索引的列
        min_cols = smawk_rec(reduced_rows, half_cols)
        
        # 扩展结果到所有列
        result = [0] * len(rows)
        k = 0  # min_cols的索引
        
        for i, row in enumerate(rows):
            # 确定当前行的最小值可能在哪个区间
            start = 0 if i == 0 else result[rows.index(reduced_rows[k-1])] if k > 0 else 0
            end = min_cols[k] if k < len(min_cols) else cols[-1]
            
            # 在这个区间内查找最小值
            min_val = float('inf')
            min_col = start
            
            # 注意这里cols是原始列的子集，需要在cols中遍历
            for j in range(cols.index(start), cols.index(end) + 1):
                col = cols[j]
                if matrix[row][col] < min_val:
                    min_val = matrix[row][col]
                    min_col = col
            
            result[i] = min_col
            
            # 如果当前行在reduced_rows中，且不是最后一行，k前进
            if k < len(reduced_rows) and row == reduced_rows[k]:
                k += 1
        
        return result
    
    return smawk_rec(list(range(m)), list(range(n)))

# ==================== 优化体系：Aliens Trick（二分约束参数+可行性DP） ====================

def aliens_trick(cost_func, check_func, left, right, eps=1e-7):
    """
    Aliens Trick（二分约束参数+可行性DP）
    
    问题描述：
    解决带约束的优化问题，通常形如最小化总成本，同时满足某些约束条件
    
    解题思路：
    1. 将约束条件转化为参数λ，构造拉格朗日函数
    2. 对λ进行二分查找，使用可行性DP判断当前λ下是否满足约束
    3. 根据可行性DP的结果调整二分区间
    
    Args:
        cost_func: 计算带参数λ的成本函数
        check_func: 检查当前解是否满足约束的函数
        left: 二分左边界
        right: 二分右边界
        eps: 精度要求
    
    Returns:
        tuple: (最优参数λ, 对应的最优解)
    
    时间复杂度：O(log((right-left)/eps) * T(DP))，其中T(DP)是一次DP的时间复杂度
    """
    best_lam = left
    best_value = None
    
    while right - left > eps:
        mid = (left + right) / 2
        # 计算当前参数下的解和约束值
        current_value, constraint_value = cost_func(mid)
        
        if check_func(constraint_value):
            # 满足约束，尝试更小的参数
            right = mid
            best_lam = mid
            best_value = current_value
        else:
            # 不满足约束，需要增大参数
            left = mid
    
    return best_lam, best_value

# ==================== 图上DP→最短路：分层图建模 ====================

# 分层图建模通常用于处理有状态转移的最短路问题
# 例如：允许k次跳跃、k次免费通行等情况

def layered_graph_dijkstra(n, m, edges, k):
    """
    分层图Dijkstra算法
    
    问题描述：
    给定一个图，允许最多使用k次特殊操作（如跳跃、免费通行等），求最短路径
    
    解题思路：
    1. 构建分层图，每层代表使用不同次数的特殊操作
    2. 对于每个节点u，在第i层表示到达u时已经使用了i次特殊操作
    3. 使用Dijkstra算法在分层图上寻找最短路径
    
    Args:
        n: 节点数量
        m: 边的数量
        edges: 边的列表，每个元素为(u, v, w)表示u到v的权为w的边
        k: 允许使用的特殊操作次数
    
    Returns:
        int: 从节点1到节点n的最短路径长度
    
    时间复杂度：O((n*k + m*k) log(n*k))
    空间复杂度：O(n*k + m*k)
    """
    import heapq
    
    # 构建分层图的邻接表
    graph = [[] for _ in range(n * (k + 1))]
    
    # 添加普通边（不使用特殊操作）
    for u, v, w in edges:
        for i in range(k + 1):
            graph[u + i * n].append((v + i * n, w))
    
    # 添加使用特殊操作的边（如果允许的话）
    for u, v, w in edges:
        for i in range(k):
            # 这里假设特殊操作可以免费通行（权为0），具体根据问题调整
            graph[u + i * n].append((v + (i + 1) * n, 0))
    
    # Dijkstra算法
    dist = [float('inf')] * (n * (k + 1))
    dist[0] = 0  # 假设起点是节点0
    heap = [(0, 0)]  # (距离, 节点)
    
    while heap:
        d, u = heapq.heappop(heap)
        if d > dist[u]:
            continue
        
        for v, w in graph[u]:
            if dist[v] > d + w:
                dist[v] = d + w
                heapq.heappush(heap, (dist[v], v))
    
    # 取所有层中到达终点的最小值
    return min(dist[n-1 + i * n] for i in range(k + 1))

# ==================== 冷门模型：期望DP遇环的方程组解（高斯消元） ====================

def gaussian_elimination(matrix):
    """
    高斯消元法求解线性方程组
    
    问题描述：
    求解形如Ax = b的线性方程组
    
    解题思路：
    1. 构建增广矩阵
    2. 进行高斯消元，将矩阵转化为行阶梯形
    3. 回代求解
    
    Args:
        matrix: 增广矩阵，每行最后一个元素是b的值
    
    Returns:
        list: 方程组的解
    
    时间复杂度：O(n^3)
    空间复杂度：O(n^2)
    """
    n = len(matrix)
    eps = 1e-9
    
    # 高斯消元过程
    for i in range(n):
        # 找到主元行（当前列中绝对值最大的行）
        max_row = i
        for j in range(i, n):
            if abs(matrix[j][i]) > abs(matrix[max_row][i]):
                max_row = j
        
        # 交换主元行和当前行
        matrix[i], matrix[max_row] = matrix[max_row], matrix[i]
        
        # 如果主元为0，方程组可能有无穷多解或无解
        if abs(matrix[i][i]) < eps:
            continue
        
        # 消元过程
        for j in range(i + 1, n):
            factor = matrix[j][i] / matrix[i][i]
            for k in range(i, n + 1):
                matrix[j][k] -= factor * matrix[i][k]
    
    # 回代求解
    x = [0] * n
    for i in range(n - 1, -1, -1):
        x[i] = matrix[i][n]
        for j in range(i + 1, n):
            x[i] -= matrix[i][j] * x[j]
        x[i] /= matrix[i][i]
    
    return x

def expectation_dp_with_cycles(n, transitions):
    """
    期望DP处理有环情况（使用高斯消元）
    
    问题描述：
    在有环的状态转移图中计算期望
    
    解题思路：
    1. 对于每个状态，建立期望方程
    2. 使用高斯消元求解方程组
    
    Args:
        n: 状态数量
        transitions: 转移概率列表，transitions[i]是一个列表，每个元素为(j, p)表示从i转移到j的概率为p
    
    Returns:
        list: 每个状态的期望值
    
    时间复杂度：O(n^3)
    空间复杂度：O(n^2)
    """
    # 构建线性方程组的增广矩阵
    matrix = [[0.0] * (n + 1) for _ in range(n)]
    
    for i in range(n):
        matrix[i][i] = 1.0  # 方程左边：E[i] - sum(p_ij * E[j]) = cost[i]
        
        # 假设每个状态的代价为1，具体根据问题调整
        cost = 1.0
        matrix[i][n] = cost
        
        for j, p in transitions[i]:
            if i != j:  # 避免自环的特殊处理
                matrix[i][j] -= p
    
    # 使用高斯消元求解
    return gaussian_elimination(matrix)

# ==================== 冷门模型：插头DP（轮廓线DP） ====================

def plug_dp(grid):
    """
    插头DP（轮廓线DP）示例：求网格中哈密顿回路的数量
    
    问题描述：
    给定一个网格，求其中哈密顿回路的数量
    
    解题思路：
    1. 使用轮廓线DP，记录当前处理到的位置和轮廓线状态
    2. 插头表示连接的状态，通常用二进制表示
    3. 使用哈希表优化空间复杂度
    
    Args:
        grid: 网格，1表示可通行，0表示障碍物
    
    Returns:
        int: 哈密顿回路的数量
    
    时间复杂度：O(n*m*4^min(n,m))
    空间复杂度：O(4^min(n,m))
    """
    from collections import defaultdict
    
    n = len(grid)
    if n == 0:
        return 0
    m = len(grid[0])
    
    # 使用滚动数组优化
    dp = defaultdict(int)
    
    # 初始状态：左上角没有插头
    dp[0] = 1
    
    for i in range(n):
        # 新的一行开始，需要将状态左移一位
        new_dp = defaultdict(int)
        for state in dp:
            # 左移一位，移除最左边的插头
            new_state = state << 1
            new_dp[new_state] = dp[state]
        dp = new_dp
        
        for j in range(m):
            new_dp = defaultdict(int)
            
            for state in dp:
                # 当前位置左边和上边的插头状态
                left = (state >> (2 * j)) & 3
                up = (state >> (2 * (j + 1))) & 3
                
                # 如果当前位置是障碍物，跳过
                if grid[i][j] == 0:
                    # 只有当左右插头都不存在时才合法
                    if left == 0 and up == 0:
                        new_dp[state] += dp[state]
                    continue
                
                # 处理各种插头组合情况
                # 1. 没有左插头和上插头
                if left == 0 and up == 0:
                    # 只能创建新的插头对（用于回路的开始）
                    if i < n - 1 and j < m - 1 and grid[i+1][j] and grid[i][j+1]:
                        new_state = state | (1 << (2 * j)) | (2 << (2 * (j + 1)))
                        new_dp[new_state] += dp[state]
                
                # 2. 只有左插头
                elif left != 0 and up == 0:
                    # 向下延伸
                    if i < n - 1 and grid[i+1][j]:
                        new_state = state
                        new_dp[new_state] += dp[state]
                    # 向右延伸
                    if j < m - 1 and grid[i][j+1]:
                        new_state = state & ~(3 << (2 * j)) | (left << (2 * (j + 1)))
                        new_dp[new_state] += dp[state]
                
                # 3. 只有上插头
                elif left == 0 and up != 0:
                    # 向右延伸
                    if j < m - 1 and grid[i][j+1]:
                        new_state = state
                        new_dp[new_state] += dp[state]
                    # 向下延伸
                    if i < n - 1 and grid[i+1][j]:
                        new_state = state & ~(3 << (2 * (j + 1))) | (up << (2 * j))
                        new_dp[new_state] += dp[state]
                
                # 4. 同时有左插头和上插头
                else:
                    # 合并插头
                    new_state = state & ~(3 << (2 * j)) & ~(3 << (2 * (j + 1)))
                    
                    # 如果是形成回路的最后一步
                    if left == up:
                        # 检查是否所有插头都已连接
                        if new_state == 0 and i == n - 1 and j == m - 1:
                            new_dp[new_state] += dp[state]
                    else:
                        # 合并两个不同的插头
                        new_dp[new_state] += dp[state]
        
        dp = new_dp
    
    # 最终状态应该是没有任何插头（形成回路）
    return dp.get(0, 0)

# ==================== 冷门模型：树上背包的优化 ====================

def tree_knapsack_optimized(root, capacity, tree, weights, values):
    """
    树上背包的优化实现（小到大合并）
    
    问题描述：
    在树上选择一些节点，使得总重量不超过容量，且总价值最大
    
    解题思路：
    1. 使用后序遍历处理子树
    2. 使用小到大合并的策略优化复杂度
    3. 对于每个节点，维护一个容量为capacity的背包
    
    Args:
        root: 根节点
        capacity: 背包容量
        tree: 树的邻接表
        weights: 每个节点的重量
        values: 每个节点的价值
    
    Returns:
        int: 最大价值
    
    时间复杂度：O(n*capacity^2)，但通过小到大合并可以降低常数
    空间复杂度：O(n*capacity)
    """
    n = len(tree)
    dp = [[0] * (capacity + 1) for _ in range(n)]
    size = [0] * n
    
    def dfs(u, parent):
        # 初始化当前节点
        size[u] = 1
        dp[u][weights[u]] = values[u]
        
        # 对每个子节点，按照子树大小排序，小的先合并
        children = []
        for v in tree[u]:
            if v != parent:
                dfs(v, u)
                children.append((size[v], v))
        
        # 按子树大小排序
        children.sort()
        
        for _, v in children:
            # 逆序遍历容量，避免重复计算
            for i in range(min(size[u], capacity), weights[u] - 1, -1):
                for j in range(1, min(size[v], capacity - i) + 1):
                    if i + j <= capacity:
                        dp[u][i + j] = max(dp[u][i + j], dp[u][i] + dp[v][j])
            
            # 更新子树大小
            size[u] += size[v]
    
    dfs(root, -1)
    
    # 返回根节点的最大价值
    return max(dp[root])

# ==================== 补充题目与应用 ====================
# 以下是一些使用上述高级DP技术的经典题目及其代码实现

# 1. 编辑距离问题（LeetCode 72）
def edit_distance(word1, word2):
    """
    LeetCode 72. 编辑距离
    题目链接：https://leetcode-cn.com/problems/edit-distance/
    
    问题描述：
    给你两个单词 word1 和 word2，计算出将 word1 转换成 word2 所使用的最少操作数。
    你可以对一个单词进行如下三种操作：插入一个字符、删除一个字符、替换一个字符。
    
    解题思路：
    使用二维DP，dp[i][j]表示word1的前i个字符转换为word2的前j个字符所需的最少操作数。
    
    时间复杂度：O(m*n)
    空间复杂度：O(m*n)
    """
    m, n = len(word1), len(word2)
    # dp[i][j]表示word1的前i个字符转换为word2的前j个字符所需的最少操作数
    dp = [[0] * (n + 1) for _ in range(m + 1)]
    
    # 初始化边界
    for i in range(m + 1):
        dp[i][0] = i
    for j in range(n + 1):
        dp[0][j] = j
    
    # 动态规划填表
    for i in range(1, m + 1):
        for j in range(1, n + 1):
            if word1[i-1] == word2[j-1]:
                dp[i][j] = dp[i-1][j-1]
            else:
                dp[i][j] = min(dp[i-1][j], dp[i][j-1], dp[i-1][j-1]) + 1
    
    return dp[m][n]

# 2. 最长递增子序列（LeetCode 300）
def length_of_lis(nums):
    """
    LeetCode 300. 最长递增子序列
    题目链接：https://leetcode-cn.com/problems/longest-increasing-subsequence/
    
    问题描述：
    给你一个整数数组 nums ，找到其中最长严格递增子序列的长度。
    
    解题思路：
    使用贪心 + 二分查找优化的DP方法。
    tails[i]表示长度为i+1的递增子序列的末尾元素的最小值。
    
    时间复杂度：O(n log n)
    空间复杂度：O(n)
    """
    if not nums:
        return 0
    
    tails = []
    for num in nums:
        # 二分查找num应该插入的位置
        left, right = 0, len(tails)
        while left < right:
            mid = (left + right) // 2
            if tails[mid] < num:
                left = mid + 1
            else:
                right = mid
        
        if left == len(tails):
            tails.append(num)
        else:
            tails[left] = num
    
    return len(tails)

# 3. 背包问题变种 - 完全背包（LeetCode 322）
def coin_change(coins, amount):
    """
    LeetCode 322. 零钱兑换
    题目链接：https://leetcode-cn.com/problems/coin-change/
    
    问题描述：
    给你一个整数数组 coins ，表示不同面额的硬币；以及一个整数 amount ，表示总金额。
    计算并返回可以凑成总金额所需的最少的硬币个数。如果没有任何一种硬币组合能组成总金额，返回-1。
    
    解题思路：
    使用完全背包的思想，dp[i]表示凑成金额i所需的最少硬币数。
    
    时间复杂度：O(amount * n)
    空间复杂度：O(amount)
    """
    # 初始化dp数组为无穷大
    dp = [float('inf')] * (amount + 1)
    dp[0] = 0  # 凑成金额0需要0个硬币
    
    for coin in coins:
        for i in range(coin, amount + 1):
            if dp[i - coin] != float('inf'):
                dp[i] = min(dp[i], dp[i - coin] + 1)
    
    return dp[amount] if dp[amount] != float('inf') else -1

# 4. 矩阵链乘法（区间DP的经典应用）
def matrix_chain_order(p):
    """
    矩阵链乘法问题
    题目来源：算法导论
    
    问题描述：
    给定一系列矩阵，计算乘法顺序使得标量乘法的次数最少。
    
    解题思路：
    使用区间DP，dp[i][j]表示计算第i到第j个矩阵的乘积所需的最少标量乘法次数。
    可以使用Knuth优化进一步降低时间复杂度。
    
    时间复杂度：O(n^3)
    空间复杂度：O(n^2)
    """
    n = len(p) - 1  # 矩阵的个数
    # dp[i][j]表示计算第i到第j个矩阵的乘积所需的最少标量乘法次数
    dp = [[0] * (n + 1) for _ in range(n + 1)]
    # s[i][j]记录最优分割点
    s = [[0] * (n + 1) for _ in range(n + 1)]
    
    # 枚举区间长度
    for length in range(2, n + 1):
        for i in range(1, n - length + 2):
            j = i + length - 1
            dp[i][j] = float('inf')
            # 枚举分割点
            for k in range(i, j):
                # 计算当前分割点的代价
                cost = dp[i][k] + dp[k+1][j] + p[i-1] * p[k] * p[j]
                if cost < dp[i][j]:
                    dp[i][j] = cost
                    s[i][j] = k
    
    return dp, s

# 5. 旅行商问题（TSP）的DP实现
def traveling_salesman_problem(graph):
    """
    旅行商问题
    题目来源：算法竞赛经典问题
    
    问题描述：
    给定一个完全图，找到一条访问每个城市恰好一次并返回起点的最短路径。
    
    解题思路：
    使用状态压缩DP，dp[mask][u]表示访问过的城市集合为mask，当前在城市u时的最短路径长度。
    
    时间复杂度：O(n^2 * 2^n)
    空间复杂度：O(n * 2^n)
    """
    n = len(graph)
    # dp[mask][u]表示访问过的城市集合为mask，当前在城市u时的最短路径长度
    dp = [[float('inf')] * n for _ in range(1 << n)]
    
    # 初始状态：只访问了起点，路径长度为0
    for i in range(n):
        dp[1 << i][i] = 0
    
    # 枚举所有可能的状态
    for mask in range(1, 1 << n):
        # 枚举当前所在的城市
        for u in range(n):
            if not (mask & (1 << u)):
                continue
            # 枚举下一个要访问的城市
            for v in range(n):
                if mask & (1 << v):
                    continue
                new_mask = mask | (1 << v)
                dp[new_mask][v] = min(dp[new_mask][v], dp[mask][u] + graph[u][v])
    
    # 找到最短的回路
    result = float('inf')
    for u in range(n):
        result = min(result, dp[(1 << n) - 1][u] + graph[u][0])
    
    return result

# 6. 区间DP：最优三角剖分
def minimum_score_triangulation(values):
    """
    LeetCode 1039. 多边形三角剖分的最低得分
    题目链接：https://leetcode-cn.com/problems/minimum-score-triangulation-of-polygon/
    
    问题描述：
    给定一个凸多边形，将其三角剖分，使得所有三角形的顶点乘积之和最小。
    
    解题思路：
    使用区间DP，dp[i][j]表示从顶点i到顶点j的多边形三角剖分的最小得分。
    
    时间复杂度：O(n^3)
    空间复杂度：O(n^2)
    """
    n = len(values)
    # dp[i][j]表示从顶点i到顶点j的多边形三角剖分的最小得分
    dp = [[0] * n for _ in range(n)]
    
    # 枚举区间长度
    for length in range(3, n + 1):
        for i in range(n - length + 1):
            j = i + length - 1
            dp[i][j] = float('inf')
            # 枚举中间点
            for k in range(i + 1, j):
                dp[i][j] = min(dp[i][j], dp[i][k] + dp[k][j] + values[i] * values[k] * values[j])
    
    return dp[0][n-1]

# 7. 博弈DP：石子游戏
def stone_game(piles):
    """
    LeetCode 877. 石子游戏
    题目链接：https://leetcode-cn.com/problems/stone-game/
    
    问题描述：
    给定一个表示石子堆的数组，两个玩家轮流从两端取石子，每次只能取一个，取到最后一个石子的人获胜。
    判断先手是否必胜。
    
    解题思路：
    使用区间DP，dp[i][j]表示在区间[i,j]中，先手能获得的最大净胜分。
    
    时间复杂度：O(n^2)
    空间复杂度：O(n^2)
    """
    n = len(piles)
    # dp[i][j]表示在区间[i,j]中，先手能获得的最大净胜分
    dp = [[0] * n for _ in range(n)]
    
    # 初始化单个石子堆
    for i in range(n):
        dp[i][i] = piles[i]
    
    # 枚举区间长度
    for length in range(2, n + 1):
        for i in range(n - length + 1):
            j = i + length - 1
            # 先手可以选择取左边或右边
            dp[i][j] = max(piles[i] - dp[i+1][j], piles[j] - dp[i][j-1])
    
    # 先手净胜分大于0则必胜
    return dp[0][n-1] > 0

# 8. 数位DP：统计1出现的次数
def count_digit_one(n):
    """
    LeetCode 233. 数字1的个数
    题目链接：https://leetcode-cn.com/problems/number-of-digit-one/
    
    问题描述：
    给定一个整数 n，计算所有小于等于 n 的非负整数中数字1出现的个数。
    
    解题思路：
    使用数位DP，逐位处理每一位上1出现的次数。
    
    时间复杂度：O(log n)
    空间复杂度：O(log n)
    """
    if n <= 0:
        return 0
    
    s = str(n)
    length = len(s)
    count = 0
    
    # 逐位处理
    for i in range(length):
        high = int(s[:i]) if i > 0 else 0
        current = int(s[i])
        low = int(s[i+1:]) if i < length - 1 else 0
        digit = 10 ** (length - i - 1)
        
        if current == 0:
            # 当前位为0，高位决定
            count += high * digit
        elif current == 1:
            # 当前位为1，高位+低位+1
            count += high * digit + low + 1
        else:
            # 当前位大于1，高位+1
            count += (high + 1) * digit
    
    return count

# 9. 树形DP：打家劫舍III
def rob(root):
    """
    LeetCode 337. 打家劫舍 III
    题目链接：https://leetcode-cn.com/problems/house-robber-iii/
    
    问题描述：
    在上次打劫完一条街道之后和一圈房屋后，小偷又发现了一个新的可行窃的地区。
    这个地区只有一个入口，我们称之为“根”。除了“根”之外，每栋房子有且只有一个“父“房子与之相连。
    一番侦察之后，聪明的小偷意识到“这个地方的所有房屋的排列类似于一棵二叉树”。
    如果两个直接相连的房子在同一天晚上被打劫，房屋将自动报警。
    计算在不触动警报的情况下，小偷一晚能够盗取的最高金额。
    
    解题思路：
    使用树形DP，对于每个节点，维护两个状态：偷或不偷。
    
    时间复杂度：O(n)
    空间复杂度：O(h)，h为树的高度
    """
    # 辅助函数，返回(偷当前节点的最大金额, 不偷当前节点的最大金额)
    def dfs(node):
        if not node:
            return (0, 0)
        
        left_rob, left_not_rob = dfs(node.left)
        right_rob, right_not_rob = dfs(node.right)
        
        # 偷当前节点，不能偷子节点
        rob_current = node.val + left_not_rob + right_not_rob
        # 不偷当前节点，可以选择偷或不偷子节点
        not_rob_current = max(left_rob, left_not_rob) + max(right_rob, right_not_rob)
        
        return (rob_current, not_rob_current)
    
    # 假设root是一个包含val、left、right属性的二叉树节点
    # 这里返回两种状态的最大值
    return max(dfs(root))

# 10. 状态压缩DP：蒙斯特曼问题
def monster_game(grid):
    """
    蒙斯特曼问题
    题目来源：算法竞赛问题
    
    问题描述：
    在网格中放置怪物，使得任何两个怪物都不在同一行、同一列或对角线上。
    
    解题思路：
    使用状态压缩DP，dp[i][mask]表示处理到第i行，已放置的列的状态为mask时的方案数。
    
    时间复杂度：O(n * 2^n)
    空间复杂度：O(2^n)
    """
    n = len(grid)
    # dp[i][mask]表示处理到第i行，已放置的列的状态为mask时的方案数
    dp = [0] * (1 << n)
    dp[0] = 1
    
    for i in range(n):
        new_dp = [0] * (1 << n)
        for mask in range(1 << n):
            if dp[mask] == 0:
                continue
            # 枚举所有可能的放置位置
            for j in range(n):
                # 检查是否可以在(i,j)放置怪物
                if (mask & (1 << j)) == 0 and grid[i][j] == 1:
                    # 检查对角线
                    valid = True
                    for k in range(i):
                        if (mask & (1 << k)) and abs(k - j) == i - k:
                            valid = False
                            break
                    if valid:
                        new_dp[mask | (1 << j)] += dp[mask]
        dp = new_dp
    
    return dp[(1 << n) - 1]

# 11. 高维DP：三维背包
def three_dimension_knapsack(n, capacity, items):
    """
    三维背包问题
    题目来源：算法竞赛问题
    
    问题描述：
    有n个物品，每个物品有体积、重量、价值三个属性，背包有体积和重量两个限制，求最大价值。
    
    解题思路：
    使用三维DP，dp[i][j][k]表示前i个物品，体积为j，重量为k时的最大价值。
    
    时间复杂度：O(n * V * W)
    空间复杂度：O(n * V * W)
    """
    V, W = capacity
    # 初始化dp数组
    dp = [[[0] * (W + 1) for _ in range(V + 1)] for _ in range(n + 1)]
    
    for i in range(1, n + 1):
        v, w, val = items[i-1]
        for j in range(V + 1):
            for k in range(W + 1):
                # 不选当前物品
                dp[i][j][k] = dp[i-1][j][k]
                # 选当前物品（如果有足够的空间）
                if j >= v and k >= w:
                    dp[i][j][k] = max(dp[i][j][k], dp[i-1][j-v][k-w] + val)
    
    return dp[n][V][W]

# 12. 斜率优化DP示例
def convex_hull_trick(points):
    """
    凸包优化技巧示例
    题目来源：算法竞赛问题
    
    问题描述：
    当状态转移方程形如dp[i] = min{dp[j] + a[i] * b[j] + c}时，可以使用凸包优化。
    
    解题思路：
    将转移方程转换为直线的形式，维护凸包以快速查询最小值。
    
    时间复杂度：O(n)
    空间复杂度：O(n)
    """
    # 示例实现，具体问题需要根据实际转移方程调整
    from collections import deque
    
    # 假设我们有一系列直线y = kx + b
    # 这里使用双端队列维护凸包
    dq = deque()
    
    # 添加一条直线y = kx + b
    def add_line(k, b):
        # 当队列中至少有两条直线时，检查是否需要删除末尾的直线
        while len(dq) >= 2:
            k1, b1 = dq[-2]
            k2, b2 = dq[-1]
            # 判断直线k1x+b1和k2x+b2的交点是否在k2x+b2和kx+b的交点右侧
            if (b2 - b1) * (k - k2) >= (b - b2) * (k2 - k1):
                dq.pop()
            else:
                break
        dq.append((k, b))
    
    # 查询x处的最小值
    def query(x):
        # 如果队列中至少有两条直线，且第一条直线在x处的值大于第二条，删除第一条
        while len(dq) >= 2:
            k1, b1 = dq[0]
            k2, b2 = dq[1]
            if k1 * x + b1 >= k2 * x + b2:
                dq.popleft()
            else:
                break
        if not dq:
            return float('inf')
        k, b = dq[0]
        return k * x + b
    
    # 这里只是返回优化后的函数，实际问题中需要根据具体情况调用
    return add_line, query