"""
数组分割方法数（Array Split Ways）

题目描述：
给定一个长度为n的数组A，将其分割成数组B和数组C，满足A[i] = B[i] + C[i]。
要求B[i], C[i] >= 1，同时B数组从左到右不降序，C数组从左到右不升序。
返回有多少种有效的划分方式。

算法思路：
1. 通过数学推导将问题转化为组合数计算
2. 根据约束条件推导出k值
3. 结果为C(k+n-1, n)

时间复杂度：O(n)
空间复杂度：O(1)

相关题目：
1. LeetCode 62 - Unique Paths（不同路径）
   题目链接：https://leetcode.cn/problems/unique-paths/
2. LeetCode 96 - Unique Binary Search Trees（不同的二叉搜索树）
   题目链接：https://leetcode.cn/problems/unique-binary-search-trees/
3. 洛谷 相关组合数学问题
4. LeetCode 118 - 杨辉三角（Pascal's Triangle）
   题目链接：https://leetcode.cn/problems/pascals-triangle/
5. LeetCode 119 - 杨辉三角 II（Pascal's Triangle II）
   题目链接：https://leetcode.cn/problems/pascals-triangle-ii/
6. LeetCode 120 - 三角形最小路径和（Triangle）
   题目链接：https://leetcode.cn/problems/triangle/
7. LeetCode 63 - 不同路径 II（Unique Paths II）
   题目链接：https://leetcode.cn/problems/unique-paths-ii/
8. LeetCode 64 - 最小路径和（Minimum Path Sum）
   题目链接：https://leetcode.cn/problems/minimum-path-sum/
9. LeetCode 164 - 最大间距（Maximum Gap）
   题目链接：https://leetcode.cn/problems/maximum-gap/
10. LeetCode 174 - 地下城游戏（Dungeon Game）
    题目链接：https://leetcode.cn/problems/dungeon-game/
11. LeetCode 188 - 买卖股票的最佳时机 IV（Best Time to Buy and Sell Stock IV）
    题目链接：https://leetcode.cn/problems/best-time-to-buy-and-sell-stock-iv/
12. LeetCode 221 - 最大正方形（Maximal Square）
    题目链接：https://leetcode.cn/problems/maximal-square/
13. LeetCode 343 - 整数拆分（Integer Break）
    题目链接：https://leetcode.cn/problems/integer-break/
14. LeetCode 357 - 计算各个位数不同的数字个数（Count Numbers with Unique Digits）
    题目链接：https://leetcode.cn/problems/count-numbers-with-unique-digits/
15. LeetCode 377 - 组合总和 Ⅳ（Combination Sum IV）
    题目链接：https://leetcode.cn/problems/combination-sum-iv/
16. LeetCode 403 - 青蛙过河（Frog Jump）
    题目链接：https://leetcode.cn/problems/frog-jump/
17. LeetCode 416 - 分割等和子集（Partition Equal Subset Sum）
    题目链接：https://leetcode.cn/problems/partition-equal-subset-sum/
18. LeetCode 494 - 目标和（Target Sum）
    题目链接：https://leetcode.cn/problems/target-sum/
19. LeetCode 518 - 零钱兑换 II（Coin Change 2）
    题目链接：https://leetcode.cn/problems/coin-change-2/
20. LeetCode 629 - K个逆序对数组（K Inverse Pairs Array）
    题目链接：https://leetcode.cn/problems/k-inverse-pairs-array/
21. LeetCode 688 - 骑士在棋盘上的概率（Knight Probability in Chessboard）
    题目链接：https://leetcode.cn/problems/knight-probability-in-chessboard/
22. LeetCode 712 - 两个字符串的最小ASCII删除和（Minimum ASCII Delete Sum for Two Strings）
    题目链接：https://leetcode.cn/problems/minimum-ascii-delete-sum-for-two-strings/
23. LeetCode 741 - 摘樱桃（Cherry Pickup）
    题目链接：https://leetcode.cn/problems/cherry-pickup/
24. LeetCode 790 - 多米诺和托米诺平铺（Domino and Tromino Tiling）
    题目链接：https://leetcode.cn/problems/domino-and-tromino-tiling/
25. LeetCode 801 - 使序列递增的最小交换次数（Minimum Swaps To Make Sequences Increasing）
    题目链接：https://leetcode.cn/problems/minimum-swaps-to-make-sequences-increasing/
26. LeetCode 808 - 分汤（Soup Servings）
    题目链接：https://leetcode.cn/problems/soup-servings/
27. LeetCode 813 - 最大平均值和的分组（Largest Sum of Averages）
    题目链接：https://leetcode.cn/problems/largest-sum-of-averages/
28. LeetCode 823 - 带因子的二叉树（Binary Trees With Factors）
    题目链接：https://leetcode.cn/problems/binary-trees-with-factors/
29. LeetCode 877 - 石子游戏（Stone Game）
    题目链接：https://leetcode.cn/problems/stone-game/
30. LeetCode 887 - 鸡蛋掉落（Super Egg Drop）
    题目链接：https://leetcode.cn/problems/super-egg-drop/
31. LeetCode 902 - 最大为 N 的数字组合（Numbers At Most N Given Digit Set）
    题目链接：https://leetcode.cn/problems/numbers-at-most-n-given-digit-set/
32. LeetCode 907 - 子数组的最小值之和（Sum of Subarray Minimums）
    题目链接：https://leetcode.cn/problems/sum-of-subarray-minimums/
33. LeetCode 920 - 播放列表的数量（Number of Music Playlists）
    题目链接：https://leetcode.cn/problems/number-of-music-playlists/
34. LeetCode 940 - 不同的子序列 II（Distinct Subsequences II）
    题目链接：https://leetcode.cn/problems/distinct-subsequences-ii/
35. LeetCode 956 - 最高的广告牌（Tallest Billboard）
    题目链接：https://leetcode.cn/problems/tallest-billboard/
36. LeetCode 960 - 删列造序 III（Delete Columns to Make Sorted III）
    题目链接：https://leetcode.cn/problems/delete-columns-to-make-sorted-iii/
37. LeetCode 1025 - 除数博弈（Divisor Game）
    题目链接：https://leetcode.cn/problems/divisor-game/
38. LeetCode 1027 - 最长等差数列（Longest Arithmetic Sequence）
    题目链接：https://leetcode.cn/problems/longest-arithmetic-sequence/
39. LeetCode 1035 - 不相交的线（Uncrossed Lines）
    题目链接：https://leetcode.cn/problems/uncrossed-lines/
40. LeetCode 1049 - 最后一块石头的重量 II（Last Stone Weight II）
    题目链接：https://leetcode.cn/problems/last-stone-weight-ii/
41. LeetCode 1105 - 填充书架（Filling Bookcase Shelves）
    题目链接：https://leetcode.cn/problems/filling-bookcase-shelves/
42. LeetCode 1155 - 掷骰子的N种方法（Number of Dice Rolls With Target Sum）
    题目链接：https://leetcode.cn/problems/number-of-dice-rolls-with-target-sum/
43. LeetCode 1216 - 验证回文字符串 III（Valid Palindrome III）
    题目链接：https://leetcode.cn/problems/valid-palindrome-iii/
44. LeetCode 1220 - 统计元音字母序列的数目（Count Vowels Permutation）
    题目链接：https://leetcode.cn/problems/count-vowels-permutation/
45. LeetCode 1231 - 分享巧克力（Divide Chocolate）
    题目链接：https://leetcode.cn/problems/divide-chocolate/
46. LeetCode 1269 - 停在原地的方案数（Number of Ways to Stay in the Same Place After Some Steps）
    题目链接：https://leetcode.cn/problems/number-of-ways-to-stay-in-the-same-place-after-some-steps/
47. LeetCode 1312 - 让字符串成为回文串的最少插入次数（Minimum Insertion Steps to Make a String Palindrome）
    题目链接：https://leetcode.cn/problems/minimum-insertion-steps-to-make-a-string-palindrome/
48. LeetCode 1320 - 二指输入的的最小距离（Minimum Distance to Type a Word Using Two Fingers）
    题目链接：https://leetcode.cn/problems/minimum-distance-to-type-a-word-using-two-fingers/
49. LeetCode 1335 - 工作计划的最低难度（Minimum Difficulty of a Job Schedule）
    题目链接：https://leetcode.cn/problems/minimum-difficulty-of-a-job-schedule/
50. LeetCode 1411 - 给 N x 3 网格图涂色的方案数（Number of Ways to Paint N × 3 Grid）
    题目链接：https://leetcode.cn/problems/number-of-ways-to-paint-n-3-grid/
51. LeetCode 1420 - 生成数组（Build Array Where You Can Find The Maximum Exactly K Comparisons）
    题目链接：https://leetcode.cn/problems/build-array-where-you-can-find-the-maximum-exactly-k-comparisons/
52. LeetCode 1463 - 摘樱桃 II（Cherry Pickup II）
    题目链接：https://leetcode.cn/problems/cherry-pickup-ii/
53. LeetCode 1531 - 压缩字符串 II（String Compression II）
    题目链接：https://leetcode.cn/problems/string-compression-ii/
54. LeetCode 1575 - 统计所有可行路径（Count All Possible Routes）
    题目链接：https://leetcode.cn/problems/count-all-possible-routes/
55. LeetCode 1594 - 矩阵的最大非负积（Maximum Non Negative Product in a Matrix）
    题目链接：https://leetcode.cn/problems/maximum-non-negative-product-in-a-matrix/
56. LeetCode 1621 - 大小为 K 的不重叠线段的数目（Number of Sets of K Non-overlapping Line Segments）
    题目链接：https://leetcode.cn/problems/number-of-sets-of-k-non-overlapping-line-segments/
57. LeetCode 1639 - 通过给定词典构造目标字符串的方案数（Number of Ways to Form a Target String Given a Dictionary）
    题目链接：https://leetcode.cn/problems/number-of-ways-to-form-a-target-string-given-a-dictionary/
58. LeetCode 1641 - 统计字典序元音字符串的数目（Count Sorted Vowel Strings）
    题目链接：https://leetcode.cn/problems/count-sorted-vowel-strings/
59. LeetCode 1655 - 分配重复整数（Distribute Repeating Integers）
    题目链接：https://leetcode.cn/problems/distribute-repeating-integers/
60. LeetCode 1692 - 计算分配糖果的不同方式（Count Ways to Distribute Candies）
    题目链接：https://leetcode.cn/problems/count-ways-to-distribute-candies/
61. LeetCode 1723 - 完成所有工作的最短时间（Find Minimum Time to Finish All Jobs）
    题目链接：https://leetcode.cn/problems/find-minimum-time-to-finish-all-jobs/
62. LeetCode 1735 - 生成乘积数组的方案数（Count Ways to Make Array With Product）
    题目链接：https://leetcode.cn/problems/count-ways-to-make-array-with-product/
63. LeetCode 1745 - 回文串分割 IV（Palindrome Partitioning IV）
    题目链接：https://leetcode.cn/problems/palindrome-partitioning-iv/
64. LeetCode 1787 - 使所有区间的异或结果为零（Make the XOR of All Segments Equal to Zero）
    题目链接：https://leetcode.cn/problems/make-the-xor-of-all-segments-equal-to-zero/
65. LeetCode 1866 - 恰有 K 根木棍可以看到的排列数目（Number of Ways to Rearrange Sticks With K Sticks Visible）
    题目链接：https://leetcode.cn/problems/number-of-ways-to-rearrange-sticks-with-k-sticks-visible/
66. LeetCode 1955 - 统计特殊子序列的数目（Count Number of Special Subsequences）
    题目链接：https://leetcode.cn/problems/count-number-of-special-subsequences/
67. LeetCode 1981 - 最小化目标值与所选元素的差（Minimize the Difference Between Target and Chosen Elements）
    题目链接：https://leetcode.cn/problems/minimize-the-difference-between-target-and-chosen-elements/
68. LeetCode 1987 - 不同的好子序列数目（Number of Unique Good Subsequences）
    题目链接：https://leetcode.cn/problems/number-of-unique-good-subsequences/
69. LeetCode 2088 - 统计农场中肥沃金字塔的数目（Count Fertile Pyramids in a Land）
    题目链接：https://leetcode.cn/problems/count-fertile-pyramids-in-a-land/
70. LeetCode 2140 - 解决智力问题（Solving Questions With Brainpower）
    题目链接：https://leetcode.cn/problems/solving-questions-with-brainpower/
71. LeetCode 2266 - 统计打字方案数（Count Number of Texts）
    题目链接：https://leetcode.cn/problems/count-number-of-texts/
72. LeetCode 2318 - 不同骰子序列的数目（Number of Distinct Roll Sequences）
    题目链接：https://leetcode.cn/problems/number-of-distinct-roll-sequences/
73. LeetCode 2320 - 统计放置房子的方式数（Count Number of Ways to Place Houses）
    题目链接：https://leetcode.cn/problems/count-number-of-ways-to-place-houses/
74. LeetCode 2370 - 最长理想子序列（Longest Ideal Subsequence）
    题目链接：https://leetcode.cn/problems/longest-ideal-subsequence/
75. LeetCode 2400 - 恰好移动 k 步到达某一位置的方法数目（Number of Ways to Reach a Position After Exactly k Steps）
    题目链接：https://leetcode.cn/problems/number-of-ways-to-reach-a-position-after-exactly-k-steps/
76. LeetCode 2431 - 最大限度地提高购买水果的性价比（Maximize Total Tastiness of Purchased Fruits）
    题目链接：https://leetcode.cn/problems/maximize-total-tastiness-of-purchased-fruits/
77. Codeforces 1359E - 组合数学问题
    题目链接：https://codeforces.com/problemset/problem/1359/E
78. Codeforces 551D - GukiZ and Binary Operations（组合数学应用）
    题目链接：https://codeforces.com/problemset/problem/551/D
79. Codeforces 1117D - Magic Gems（组合数学+矩阵快速幂）
    题目链接：https://codeforces.com/problemset/problem/1117/D
80. Codeforces 2072F - 组合数次幂异或问题
    题目链接：https://codeforces.com/problemset/problem/2072/F
81. AtCoder ABC165D - Floor Function
    题目链接：https://atcoder.jp/contests/abc165/tasks/abc165_d
82. AtCoder ABC098D - Xor Sum 2（组合数学应用）
    题目链接：https://atcoder.jp/contests/abc098/tasks/abc098_d
83. USACO 2006 November - Bad Hair Day（组合数学应用）
    题目链接：http://www.usaco.org/index.php?page=viewproblem2&cpid=187
84. 计蒜客 T1565 - 合并果子（组合数学应用）
    题目链接：https://nanti.jisuanke.com/t/T1565
85. ZOJ 3537 - Cake（组合数学应用）
    题目链接：https://zoj.pintia.cn/problem-sets/91827364500/problems/91827364577
86. TimusOJ 1001 - Reverse Root（组合数学应用）
    题目链接：https://acm.timus.ru/problem.aspx?space=1&num=1001
87. 牛客网 NC95 - 数组中的逆序对
    题目链接：https://www.nowcoder.com/practice/8c6984f3dc664ef0a305c24e1473729e
88. 牛客网 - 计算数组的小和
    题目链接：https://www.nowcoder.com/practice/4385fa1c390e49f69fcf77ecffee7164
89. LintCode 1297 - 统计右侧小于当前元素的个数
    题目链接：https://www.lintcode.com/problem/1297/
90. LintCode 1497 - 区间和的个数
    题目链接：https://www.lintcode.com/problem/1497/
91. LintCode 3653 - Meeting Scheduler（组合数学应用）
    题目链接：https://www.lintcode.com/problem/3653/
92. HackerRank - Merge Sort: Counting Inversions（归并排序逆序对计数）
    题目链接：https://www.hackerrank.com/challenges/ctci-merge-sort/problem
93. POJ 2299 - Ultra-QuickSort（逆序对计数）
    题目链接：http://poj.org/problem?id=2299
94. HDU 1394 - Minimum Inversion Number（最小逆序对数）
    题目链接：http://acm.hdu.edu.cn/showproblem.php?pid=1394
95. SPOJ MSUBSTR - 最大子串（组合数学应用）
    题目链接：https://www.spoj.com/problems/MSUBSTR/
96. UVa 11300 - Spreading the Wealth（组合数学应用）
    题目链接：https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=25&page=show_problem&problem=2275
97. CodeChef INVCNT - 逆序对计数（组合数学应用）
    题目链接：https://www.codechef.com/problems/INVCNT
98. 洛谷 P3414 - SAC#1 - 组合数
    题目链接：https://www.luogu.com.cn/problem/P3414
99. 洛谷 P2822 - 组合数问题
    题目链接：https://www.luogu.com.cn/problem/P2822
100. 洛谷 P1313 - 计算系数
     题目链接：https://www.luogu.com.cn/problem/P1313
101. 洛谷 P5732 - 杨辉三角
     题目链接：https://www.luogu.com.cn/problem/P5732
"""

class ArraySplitWays:
    def __init__(self, mod=1000000007):
        """
        初始化数组分割计算器
        
        Args:
            mod: 模数，用于大数取模
        """
        self.mod = mod
    
    def count_ways(self, arr):
        """
        计算数组分割方法数
        
        Args:
            arr: 输入数组
            
        Returns:
            有效的分割方法数
        """
        n = len(arr)
        if n == 0:
            return 0
            
        # 计算k值
        k = arr[0] - 1
        for i in range(1, n):
            if arr[i-1] > arr[i]:
                k -= arr[i-1] - arr[i]
        
        # 如果k <= 0，无有效分割方法
        if k <= 0:
            return 0
            
        # 返回组合数C(k+n-1, n)
        return self._combination(k + n - 1, n)
    
    def _combination(self, n, k):
        """
        计算组合数C(n, k) % mod
        
        Args:
            n: 总数
            k: 选取数
            
        Returns:
            组合数C(n, k) % mod
        """
        if k > n or k < 0:
            return 0
            
        # 优化：C(n, k) = C(n, n-k)
        if k > n - k:
            k = n - k
            
        # 计算分子和分母
        numerator = 1  # 分子
        denominator = 1  # 分母
        
        for i in range(k):
            numerator = (numerator * (n - i)) % self.mod
            denominator = (denominator * (i + 1)) % self.mod
        
        # 计算分母的逆元
        inv_denominator = self._mod_inverse(denominator, self.mod)
        
        # 返回结果
        return (numerator * inv_denominator) % self.mod
    
    def _mod_inverse(self, a, mod):
        """
        计算a在模mod下的逆元，使用费马小定理
        
        Args:
            a: 要计算逆元的数
            mod: 模数（必须为质数）
            
        Returns:
            a的逆元
        """
        return self._power(a, mod - 2, mod)
    
    def _power(self, base, exp, mod):
        """
        快速幂运算
        
        Args:
            base: 底数
            exp: 指数
            mod: 模数
            
        Returns:
            (base^exp) % mod
        """
        result = 1
        base = base % mod
        
        while exp > 0:
            if exp % 2 == 1:
                result = (result * base) % mod
            exp = exp >> 1
            base = (base * base) % mod
        
        return result

def test_array_split_ways():
    """测试数组分割方法数计算"""
    print("=== 数组分割方法数测试 ===")
    
    # 创建计算器实例
    calculator = ArraySplitWays()
    
    # 测试用例1: 基本情况
    print("测试用例1:")
    arr1 = [5, 4, 5]
    ways1 = calculator.count_ways(arr1)
    print(f"数组 {arr1} 的分割方法数: {ways1}")
    print()
    
    # 测试用例2: 递增数组
    print("测试用例2:")
    arr2 = [3, 4, 5]
    ways2 = calculator.count_ways(arr2)
    print(f"数组 {arr2} 的分割方法数: {ways2}")
    print()
    
    # 测试用例3: 相同元素数组
    print("测试用例3:")
    arr3 = [4, 4, 4]
    ways3 = calculator.count_ways(arr3)
    print(f"数组 {arr3} 的分割方法数: {ways3}")
    print()

if __name__ == "__main__":
    test_array_split_ways()