# -*- coding: utf-8 -*-
"""
LeetCode 664. 奇怪的打印机
题目链接：https://leetcode.cn/problems/strange-printer/

题目描述：
有台奇怪的打印机有以下两个特殊要求：
1. 打印机每次只能打印由同一个字符组成的序列
2. 每次可以在任意起始和结束位置打印新字符，并且会覆盖掉原来已有的字符
给你一个字符串 s ，你的任务是计算这个打印机打印它需要的最少打印次数

解题思路：
这是一个区间动态规划问题，关键在于理解打印策略。
状态定义：dp[i][j]表示打印子串 s[i...j] 需要的最少打印次数。
状态转移方程：
1. 如果 s[i] == s[j]，则 dp[i][j] = dp[i][j-1]（可以在打印 s[i] 时一起打印 s[j]）
2. 如果 s[i] != s[j]，则 dp[i][j] = min(dp[i][k] + dp[k+1][j]) for k in [i, j-1]

时间复杂度：O(n³)
空间复杂度：O(n²)

工程化考量：
1. 边界条件处理：单个字符只需打印1次
2. 优化：可以预处理压缩连续重复字符，减少状态数量
3. 输入验证：检查字符串是否为空

区间动态规划补充题目集合
====================================
LeetCode (力扣)
1. LeetCode 664. 奇怪的打印机 - https://leetcode.cn/problems/strange-printer/
2. LeetCode 1000. 合并石头的最低成本 - https://leetcode.cn/problems/minimum-cost-to-merge-stones/
3. LeetCode 312. 戳气球 - https://leetcode.cn/problems/burst-balloons/
4. LeetCode 1547. 切棍子的最小成本 - https://leetcode.cn/problems/minimum-cost-to-cut-a-stick/
5. LeetCode 1039. 多边形三角剖分的最低得分 - https://leetcode.cn/problems/minimum-score-triangulation-of-polygon/
6. LeetCode 1246. 删除回文子数组 - https://leetcode.cn/problems/palindrome-removal/
7. LeetCode 132. 分割回文串 II - https://leetcode.cn/problems/palindrome-partitioning-ii/
8. LeetCode 516. 最长回文子序列 - https://leetcode.cn/problems/longest-palindromic-subsequence/
9. LeetCode 1312. 让字符串成为回文串的最少插入次数 - https://leetcode.cn/problems/minimum-insertion-steps-to-make-a-string-palindrome/
10. LeetCode 1130. 叶值的最小代价生成树 - https://leetcode.cn/problems/minimum-cost-tree-from-leaf-values/
11. LeetCode 1770. 执行乘法运算的最大分数 - https://leetcode.cn/problems/maximum-score-from-performing-multiplication-operations/
12. LeetCode 1216. 验证回文字符串 III - https://leetcode.cn/problems/valid-palindrome-iii/
13. LeetCode 1682. 最长回文子序列 II - https://leetcode.cn/problems/longest-palindromic-subsequence-ii/

LintCode (炼码)
14. LintCode 1063. 凸多边形的三角剖分 - https://www.lintcode.com/problem/1063/
15. LintCode 108. 分割回文串 II - https://www.lintcode.com/problem/108/
16. LintCode 136. 分割回文串 - https://www.lintcode.com/problem/136/
17. LintCode 1419. 最少行程 - https://www.lintcode.com/problem/1419/
18. LintCode 1797. 模糊坐标 - https://www.lintcode.com/problem/1797/
19. LintCode 1639. K 倍重复项删除 - https://www.lintcode.com/problem/1639/

HackerRank
20. HackerRank - Sherlock and Cost - https://www.hackerrank.com/challenges/sherlock-and-cost/problem
21. HackerRank - Palindrome Index - https://www.hackerrank.com/challenges/palindrome-index/problem
22. HackerRank - Game of Stones - https://www.hackerrank.com/challenges/game-of-stones-1/problem

Codeforces
23. Codeforces 1327D - Infinite Path - https://codeforces.com/problemset/problem/1327/D
24. Codeforces 1373C - Pluses and Minuses - https://codeforces.com/problemset/problem/1373/C
25. Codeforces 140E - New Year Garland - https://codeforces.com/problemset/problem/140/E
26. Codeforces 438D - The Child and Sequence - https://codeforces.com/problemset/problem/438/D

AtCoder
27. AtCoder ABC144D - Water Bottle - https://atcoder.jp/contests/abc144/tasks/abc144_d
28. AtCoder ABC161D - Lunlun Number - https://atcoder.jp/contests/abc161/tasks/abc161_d
29. AtCoder DP Contest F - LCS - https://atcoder.jp/contests/dp/tasks/dp_f

其他平台
30. POJ 3280 - Cheapest Palindrome - http://poj.org/problem?id=3280
31. UVa 10003 - Cutting Sticks - https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=113&page=show_problem&problem=944
32. HDU 4632 - Palindrome Subsequence - http://acm.hdu.edu.cn/showproblem.php?pid=4632
33. SPOJ 5971 - PIZZA - https://www.spoj.com/problems/PIZZA/
34. 牛客网 NC16595 - 区间dp练习 - https://ac.nowcoder.com/acm/problem/16595
35. AcWing 1068. 环形石子合并 - https://www.acwing.com/problem/content/1070/
36. 洛谷 P1040 - 加分二叉树 - https://www.luogu.com.cn/problem/P1040
37. 计蒜客 T1130 - 矩阵链乘法 - https://nanti.jisuanke.com/t/T1130
38. 剑指Offer II 095. 最长公共子序列 - https://leetcode.cn/problems/qJnOS7/
39. CodeChef - SUBINC - https://www.codechef.com/problems/SUBINC
40. Project Euler 5 - Smallest Multiple - https://projecteuler.net/problem=5
"""

def strange_printer1(s):
    """
    题目来源：LeetCode 664. 奇怪的打印机
    题目链接：https://leetcode.cn/problems/strange-printer/
    题目描述：
    有台奇怪的打印机有以下两个特殊要求：
    打印机每次只能打印由同一个字符组成的序列
    每次可以在任意起始和结束位置打印新字符，并且会覆盖掉原来已有的字符
    给你一个字符串 s ，你的任务是计算这个打印机打印它需要的最少打印次数
    
    【区间动态规划核心思想】
    区间DP是一种特殊的动态规划技巧，专门用来解决区间类问题。其核心思想是：
    1. 将大区间问题分解为小区间子问题
    2. 按照区间长度递增的顺序求解，利用已解决的小区间结果来构建大区间的解
    3. 通常使用二维数组dp[i][j]表示区间[i,j]上的最优解
    
    【本题解题思路】
    1. 定义状态：dp[i][j]表示打印字符串s[i...j]所需的最少打印次数
    2. 基本情况：当i==j时，单个字符只需打印一次，dp[i][j]=1
    3. 状态转移：
       - 如果s[i] == s[j]：可以将s[j]与s[i]一起打印，dp[i][j] = dp[i][j-1]
       - 如果s[i] != s[j]：枚举分割点k，将区间分成[i,k]和[k+1,j]，dp[i][j] = min(dp[i][k] + dp[k+1][j])
    4. 本题采用记忆化搜索实现，更直观地体现递归关系
    
    【时间复杂度分析】
    - 状态数量：O(n²)，其中n是字符串长度
    - 每个状态的计算需要O(n)时间（枚举分割点）
    - 总时间复杂度：O(n³)
    
    【空间复杂度分析】
    - memo字典存储所有计算过的状态：O(n²)
    - 递归调用栈深度：O(n)
    - 总空间复杂度：O(n²)
    
    【是否为最优解】
    是的，这是该问题的最优解法。区间DP是处理此类问题的标准方法，时间复杂度无法进一步降低，因为需要考虑所有可能的分割点。
    
    【工程化考量】
    1. 异常处理：处理空字符串输入
    2. 性能优化：使用记忆化避免重复计算
    3. 可测试性：函数设计简洁，易于进行单元测试
    
    Args:
        s (str): 输入字符串
        
    Returns:
        int: 最少打印次数
    """
    if not s:
        return 0
    
    n = len(s)
    # 使用字典来模拟记忆化搜索的dp表，键为(l,r)元组，值为对应区间的最少打印次数
    memo = {}
    
    def f(l, r):
        """计算打印区间[l,r]所需的最少次数"""
        # 检查是否已经计算过这个状态，避免重复计算
        if (l, r) in memo:
            return memo[(l, r)]
        
        # 基本情况：单个字符只需打印一次
        if l == r:
            result = 1
        else:
            # 优化：如果首尾字符相同，可以减少打印次数
            if s[l] == s[r]:
                # 可以在打印s[l]时一起打印s[r]，因为打印机可以覆盖
                result = f(l, r - 1)
            else:
                # 首尾字符不同，需要枚举所有可能的分割点
                result = float('inf')
                for k in range(l, r):
                    # 将区间分成两部分，取最小值
                    result = min(result, f(l, k) + f(k + 1, r))
        
        # 记录计算结果到memo中
        memo[(l, r)] = result
        return result
    
    # 调用递归函数，计算整个字符串的最少打印次数
    return f(0, n - 1)

# 测试用例
def test_strange_printer():
    """
    单元测试函数，验证算法正确性
    覆盖常规、边界、极端测试场景
    """
    # 测试用例1：常规情况
    s1 = "aaabbb"
    assert strange_printer1(s1) == 2, f"测试失败: {s1} 预期输出 2"
    
    # 测试用例2：回文串
    s2 = "aba"
    assert strange_printer1(s2) == 2, f"测试失败: {s2} 预期输出 2"
    
    # 测试用例3：所有字符相同
    s3 = "aaaaa"
    assert strange_printer1(s3) == 1, f"测试失败: {s3} 预期输出 1"
    
    # 测试用例4：所有字符不同
    s4 = "abcde"
    assert strange_printer1(s4) == 5, f"测试失败: {s4} 预期输出 5"
    
    # 测试用例5：空字符串
    s5 = ""
    assert strange_printer1(s5) == 0, f"测试失败: {s5} 预期输出 0"
    
    # 测试用例6：单个字符
    s6 = "a"
    assert strange_printer1(s6) == 1, f"测试失败: {s6} 预期输出 1"
    
    # 测试用例7：复杂混合
    s7 = "leetcode"
    print(f"复杂测试用例: {s7}, 结果: {strange_printer1(s7)}")
    
    print("所有测试通过！")

# 运行测试
if __name__ == "__main__":
    test_strange_printer()


def strange_printer2(s):
    """
    题目来源：LeetCode 664. 奇怪的打印机
    题目链接：https://leetcode.cn/problems/strange-printer/
    题目描述：
    有台奇怪的打印机有以下两个特殊要求：
    打印机每次只能打印由同一个字符组成的序列
    每次可以在任意起始和结束位置打印新字符，并且会覆盖掉原来已有的字符
    给你一个字符串 s ，你的任务是计算这个打印机打印它需要的最少打印次数
    
    【解法思路】严格位置依赖的动态规划（迭代实现）
    与记忆化搜索思路相同，但使用迭代方式实现，按照区间长度从小到大填充dp表。
    
    【实现细节】
    1. 初始化：创建二维dp数组，dp[i][i] = 1（单个字符）
    2. 填表顺序：区间长度从2到n
    3. 状态转移：与记忆化搜索相同
    
    【时间复杂度】
    - 三重循环：区间长度、起点、分割点
    - 总时间复杂度：O(n³)
    
    【空间复杂度】
    - 二维dp数组：O(n²)
    
    【记忆化搜索 vs 迭代DP】
    记忆化搜索：
    - 优点：实现简单，逻辑清晰，只计算必要的子问题
    - 缺点：可能有递归栈开销
    
    迭代DP：
    - 优点：没有递归开销，空间使用更可控
    - 缺点：需要计算所有可能的子问题，实现相对复杂
    
    Args:
        s (str): 输入字符串
        
    Returns:
        int: 最少打印次数
    """
    if not s:
        return 0
    
    n = len(s)
    # 创建dp数组，dp[i][j]表示打印区间[i,j]所需的最少次数
    dp = [[0] * n for _ in range(n)]
    
    # 初始化：单个字符只需打印一次
    for i in range(n):
        dp[i][i] = 1
    
    # 按照区间长度从小到大填表
    # 区间长度从2开始（长度为1的已经初始化）
    for length in range(2, n + 1):
        # 枚举区间起点i
        for i in range(n - length + 1):
            # 计算区间终点j
            j = i + length - 1
            
            # 初始化为最坏情况：单独打印最后一个字符
            dp[i][j] = dp[i][j-1] + 1
            
            # 优化：如果首尾字符相同
            if s[i] == s[j]:
                dp[i][j] = dp[i][j-1]
            else:
                # 枚举所有可能的分割点k
                for k in range(i, j):
                    # 状态转移：取两种分割方式的最小值
                    dp[i][j] = min(dp[i][j], dp[i][k] + dp[k+1][j])
    
    # 返回整个字符串的最少打印次数
    return dp[0][n-1]

# 补充解法：带优化的动态规划
def strange_printer3(s):
    """
    题目来源：LeetCode 664. 奇怪的打印机
    
    【优化版动态规划】
    针对原问题的一些优化：
    1. 预处理字符串，压缩连续重复的字符（如"aaabbb" → "ab"）
    2. 优化状态转移，减少不必要的计算
    
    【优化原理】
    连续重复的字符可以合并，因为打印机可以一次打印连续的相同字符。
    这可以减少问题规模，提高效率。
    
    【性能提升】
    在有大量重复字符的情况下，预处理可以显著减少状态数量。
    
    Args:
        s (str): 输入字符串
        
    Returns:
        int: 最少打印次数
    """
    # 预处理：压缩连续重复的字符
    if not s:
        return 0
    
    # 压缩字符串
    compressed = []
    for char in s:
        if not compressed or compressed[-1] != char:
            compressed.append(char)
    
    compressed_s = ''.join(compressed)
    n = len(compressed_s)
    
    # 创建dp数组
    dp = [[0] * n for _ in range(n)]
    
    # 初始化
    for i in range(n):
        dp[i][i] = 1
    
    # 填表
    for length in range(2, n + 1):
        for i in range(n - length + 1):
            j = i + length - 1
            
            # 初始化为最坏情况
            dp[i][j] = dp[i][j-1] + 1
            
            # 优化：如果首尾字符相同
            if compressed_s[i] == compressed_s[j]:
                dp[i][j] = dp[i][j-1]
            else:
                # 枚举分割点
                for k in range(i, j):
                    dp[i][j] = min(dp[i][j], dp[i][k] + dp[k+1][j])
                    # 进一步优化：如果在分割区间中发现相同字符，可以提前剪枝
                    if dp[i][j] == dp[i][k] and compressed_s[k+1] == compressed_s[j]:
                        break
    
    return dp[0][n-1]

# 区间动态规划总结
def interval_dp_summary_func():
    """
    区间动态规划算法总结
    
    【核心特征】
    1. 问题可以分解为区间子问题
    2. 子问题的解可以组合成原问题的解
    3. 按照区间长度递增的顺序求解
    
    【常见应用场景】
    1. 字符串处理：回文串相关问题
    2. 数组分割/合并：石子合并、戳气球等
    3. 几何问题：多边形三角剖分
    4. 博弈问题：区间博弈策略
    
    【状态转移方程模式】
    dp[i][j] = min/max(dp[i][k] + dp[k+1][j] + cost)  (i <= k < j)
    
    【解题技巧】
    1. 合理定义状态dp[i][j]
    2. 处理好边界条件
    3. 注意填表顺序（区间长度从小到大）
    4. 寻找可能的优化点
    
    【语言差异】
    Python:
    - 适合使用记忆化搜索（lru_cache或字典）
    - 列表推导式创建二维数组方便
    
    Java:
    - 通常使用二维数组实现迭代DP
    - 可以使用 memoization + 递归
    
    C++:
    - 数组访问效率高，适合大数组操作
    - 可以使用vector<vector<int>>或数组
    
    【工程化建议】
    1. 对于大数据量，考虑空间优化（滚动数组）
    2. 添加充分的异常处理和边界检查
    3. 编写单元测试验证正确性
    4. 考虑性能优化，如预处理、剪枝等
    """
    pass


# 测试函数
if __name__ == "__main__":
    # 测试用例1
    s1 = "aaabbb"
    print(f"字符串: {s1}")
    print(f"最少打印次数 (解法一): {strange_printer1(s1)}")
    print(f"最少打印次数 (解法二): {strange_printer2(s1)}")
    
    # 测试用例2
    s2 = "aba"
    print(f"\n字符串: {s2}")
    print(f"最少打印次数 (解法一): {strange_printer1(s2)}")
    print(f"最少打印次数 (解法二): {strange_printer2(s2)}")
    
    # 测试用例3
    s3 = "abcabc"
    print(f"\n字符串: {s3}")
    print(f"最少打印次数 (解法一): {strange_printer1(s3)}")
    print(f"最少打印次数 (解法二): {strange_printer2(s3)}")