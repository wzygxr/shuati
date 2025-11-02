# 两个排列的最长公共子序列长度问题扩展实现 (Python版本)
# 给出由1~n这些数字组成的两个排列
# 求它们的最长公共子序列长度
# n <= 10^5
# 测试链接 : https://www.luogu.com.cn/problem/P1439

from typing import List

class Code03_PermutationLCS_Expanded:
    '''
    类似题目1：最长公共子序列（LeetCode 1143）
    题目描述：
    给定两个字符串 text1 和 text2，返回这两个字符串的最长公共子序列的长度。
    若这两个字符串没有公共子序列，则返回 0。
    
    示例：
    输入：text1 = "abcde", text2 = "ace"
    输出：3
    解释：最长公共子序列是 "ace"，它的长度为 3。
    
    解题思路：
    这是经典的LCS问题，使用动态规划解决。
    dp[i][j] 表示text1的前i个字符和text2的前j个字符的最长公共子序列长度
    状态转移方程：
    如果text1[i-1] == text2[j-1]，则dp[i][j] = dp[i-1][j-1] + 1
    否则dp[i][j] = max(dp[i-1][j], dp[i][j-1])
    '''
    
    # 最长公共子序列 - 二维动态规划解法
    # 时间复杂度: O(m * n)，其中m和n分别是两个字符串的长度
    # 空间复杂度: O(m * n)
    @staticmethod
    def longest_common_subsequence1(text1: str, text2: str) -> int:
        m = len(text1)
        n = len(text2)
        
        # dp[i][j] 表示text1的前i个字符和text2的前j个字符的最长公共子序列长度
        dp = [[0 for _ in range(n + 1)] for _ in range(m + 1)]
        
        # 状态转移
        for i in range(1, m + 1):
            for j in range(1, n + 1):
                if text1[i-1] == text2[j-1]:
                    dp[i][j] = dp[i-1][j-1] + 1
                else:
                    dp[i][j] = max(dp[i-1][j], dp[i][j-1])
        
        return dp[m][n]
    
    # 最长公共子序列 - 一维动态规划解法（空间优化）
    # 时间复杂度: O(m * n)，其中m和n分别是两个字符串的长度
    # 空间复杂度: O(n)
    @staticmethod
    def longest_common_subsequence2(text1: str, text2: str) -> int:
        m = len(text1)
        n = len(text2)
        
        # 使用一维数组优化空间
        dp = [0 for _ in range(n + 1)]
        
        # 状态转移
        for i in range(1, m + 1):
            pre = 0  # 保存dp[i-1][j-1]的值
            for j in range(1, n + 1):
                temp = dp[j]  # 保存当前dp[j]的值，用于下一次循环作为dp[i-1][j-1]
                if text1[i-1] == text2[j-1]:
                    dp[j] = pre + 1
                else:
                    dp[j] = max(dp[j], dp[j-1])
                pre = temp
        
        return dp[n]
    
    '''
    类似题目2：最长重复子数组（LeetCode 718）
    题目描述：
    给两个整数数组 A 和 B ，返回两个数组中公共的、长度最长的子数组的长度。
    
    示例：
    输入：A = [1,2,3,2,1], B = [3,2,1,4,7]
    输出：3
    解释：长度最长的公共子数组是 [3,2,1]。
    
    解题思路：
    这个问题与LCS类似，但要求是连续的子数组。
    dp[i][j] 表示以A[i-1]和B[j-1]结尾的公共子数组的长度
    状态转移方程：
    如果A[i-1] == B[j-1]，则dp[i][j] = dp[i-1][j-1] + 1
    否则dp[i][j] = 0
    '''
    
    # 最长重复子数组 - 动态规划解法
    # 时间复杂度: O(m * n)，其中m和n分别是两个数组的长度
    # 空间复杂度: O(m * n)
    @staticmethod
    def find_length(A: List[int], B: List[int]) -> int:
        m = len(A)
        n = len(B)
        
        # dp[i][j] 表示以A[i-1]和B[j-1]结尾的公共子数组的长度
        dp = [[0 for _ in range(n + 1)] for _ in range(m + 1)]
        max_len = 0
        
        # 状态转移
        for i in range(1, m + 1):
            for j in range(1, n + 1):
                if A[i-1] == B[j-1]:
                    dp[i][j] = dp[i-1][j-1] + 1
                    max_len = max(max_len, dp[i][j])
                else:
                    dp[i][j] = 0
        
        return max_len
    
    # 最长重复子数组 - 一维动态规划解法（空间优化）
    # 时间复杂度: O(m * n)，其中m和n分别是两个数组的长度
    # 空间复杂度: O(n)
    @staticmethod
    def find_length2(A: List[int], B: List[int]) -> int:
        m = len(A)
        n = len(B)
        
        # 使用一维数组优化空间
        dp = [0 for _ in range(n + 1)]
        max_len = 0
        
        # 状态转移
        for i in range(1, m + 1):
            pre = 0  # 保存dp[i-1][j-1]的值
            for j in range(1, n + 1):
                temp = dp[j]  # 保存当前dp[j]的值，用于下一次循环作为dp[i-1][j-1]
                if A[i-1] == B[j-1]:
                    dp[j] = pre + 1
                    max_len = max(max_len, dp[j])
                else:
                    dp[j] = 0
                pre = temp
        
        return max_len
    
    '''
    类似题目3：不同的子序列（LeetCode 115）
    题目描述：
    给定一个字符串 s 和一个字符串 t ，计算在 s 的子序列中 t 出现的个数。
    
    示例：
    输入：s = "rabbbit", t = "rabbit"
    输出：3
    解释：有3种可以从 s 中得到 "rabbit" 的方案。
    
    解题思路：
    这是一个动态规划问题，类似于LCS但求的是方案数。
    dp[i][j] 表示s的前i个字符的子序列中t的前j个字符出现的次数
    状态转移方程：
    如果s[i-1] == t[j-1]，则dp[i][j] = dp[i-1][j-1] + dp[i-1][j]
    否则dp[i][j] = dp[i-1][j]
    '''
    
    # 不同的子序列 - 动态规划解法
    # 时间复杂度: O(m * n)，其中m和n分别是两个字符串的长度
    # 空间复杂度: O(m * n)
    @staticmethod
    def num_distinct(s: str, t: str) -> int:
        m = len(s)
        n = len(t)
        
        # dp[i][j] 表示s的前i个字符的子序列中t的前j个字符出现的次数
        dp = [[0 for _ in range(n + 1)] for _ in range(m + 1)]
        
        # 初始化：空字符串是任何字符串的一个子序列
        for i in range(m + 1):
            dp[i][0] = 1
        
        # 状态转移
        for i in range(1, m + 1):
            for j in range(1, n + 1):
                if s[i-1] == t[j-1]:
                    dp[i][j] = dp[i-1][j-1] + dp[i-1][j]
                else:
                    dp[i][j] = dp[i-1][j]
        
        return dp[m][n]
    
    '''
    类似题目4：编辑距离（LeetCode 72）
    题目描述：
    给你两个单词 word1 和 word2， 请返回将 word1 转换成 word2 所使用的最少操作数 。
    你可以对一个单词进行如下三种操作：
    插入一个字符
    删除一个字符
    替换一个字符
    
    示例：
    输入：word1 = "horse", word2 = "ros"
    输出：3
    解释：
    horse -> rorse (将 'h' 替换为 'r')
    rorse -> rose (删除 'r')
    rose -> ros (删除 'e')
    
    解题思路：
    这是一个经典的动态规划问题。
    dp[i][j] 表示word1的前i个字符转换成word2的前j个字符所需的最少操作数
    状态转移方程：
    如果word1[i-1] == word2[j-1]，则dp[i][j] = dp[i-1][j-1]
    否则dp[i][j] = min(dp[i-1][j], dp[i][j-1], dp[i-1][j-1]) + 1
    '''
    
    # 编辑距离 - 动态规划解法
    # 时间复杂度: O(m * n)，其中m和n分别是两个字符串的长度
    # 空间复杂度: O(m * n)
    @staticmethod
    def min_distance(word1: str, word2: str) -> int:
        m = len(word1)
        n = len(word2)
        
        # dp[i][j] 表示word1的前i个字符转换成word2的前j个字符所需的最少操作数
        dp = [[0 for _ in range(n + 1)] for _ in range(m + 1)]
        
        # 初始化
        for i in range(m + 1):
            dp[i][0] = i
        for j in range(n + 1):
            dp[0][j] = j
        
        # 状态转移
        for i in range(1, m + 1):
            for j in range(1, n + 1):
                if word1[i-1] == word2[j-1]:
                    dp[i][j] = dp[i-1][j-1]
                else:
                    dp[i][j] = min(dp[i-1][j], dp[i][j-1], dp[i-1][j-1]) + 1
        
        return dp[m][n]
    
    '''
    类似题目5：最长递增子序列（LeetCode 300）
    题目描述：
    给你一个整数数组 nums ，找到其中最长严格递增子序列的长度。
    子序列是由数组派生而来的序列，删除（或不删除）数组中的元素而不改变其余元素的顺序。
    
    示例：
    输入：nums = [10,9,2,5,3,7,101,18]
    输出：4
    解释：最长递增子序列是 [2,3,7,101]，长度为4。
    
    解题思路：
    经典的LIS问题，可以使用贪心+二分查找优化到O(n log n)时间复杂度。
    维护一个数组tails，其中tails[i]表示长度为i+1的递增子序列的末尾元素的最小值。
    '''
    
    # 最长递增子序列 - 贪心 + 二分查找解法
    # 时间复杂度: O(n log n)，其中n是数组长度
    # 空间复杂度: O(n)
    @staticmethod
    def length_of_lis(nums: List[int]) -> int:
        n = len(nums)
        if n == 0:
            return 0
        
        # tails[i]表示长度为i+1的递增子序列的末尾元素的最小值
        tails = []
        
        for num in nums:
            # 二分查找在tails数组中找到第一个大于等于num的位置
            left, right = 0, len(tails)
            while left < right:
                mid = left + (right - left) // 2
                if tails[mid] < num:
                    left = mid + 1
                else:
                    right = mid
            
            # 更新tails数组
            if left == len(tails):
                tails.append(num)
            else:
                tails[left] = num
        
        return len(tails)
    
    '''
    类似题目6：通配符匹配（LeetCode 44）
    题目描述：
    给定一个字符串 (s) 和一个字符模式 (p) ，实现一个支持 '?' 和 '*' 的通配符匹配。
    '?' 可以匹配任何单个字符。
    '*' 可以匹配任意字符串（包括空字符串）。
    两个字符串完全匹配才算匹配成功。
    
    示例：
    输入：s = "adceb", p = "*a*b"
    输出：true
    解释：第一个 '*' 可以匹配空字符串，第二个 '*' 可以匹配 "dce"。
    
    解题思路：
    使用动态规划解决。
    dp[i][j] 表示s的前i个字符和p的前j个字符是否匹配。
    '''
    
    # 通配符匹配 - 动态规划解法
    # 时间复杂度: O(m * n)，其中m和n分别是字符串s和p的长度
    # 空间复杂度: O(m * n)
    @staticmethod
    def is_match(s: str, p: str) -> bool:
        m = len(s)
        n = len(p)
        
        # dp[i][j] 表示s的前i个字符和p的前j个字符是否匹配
        dp = [[False for _ in range(n + 1)] for _ in range(m + 1)]
        
        # 空字符串和空模式匹配
        dp[0][0] = True
        
        # 处理p以若干个*开头的情况
        for j in range(1, n + 1):
            if p[j-1] == '*':
                dp[0][j] = dp[0][j-1]
        
        # 状态转移
        for i in range(1, m + 1):
            for j in range(1, n + 1):
                p_char = p[j-1]
                if p_char == '*':
                    # '*'可以匹配0个或多个字符
                    dp[i][j] = dp[i][j-1] or dp[i-1][j]
                elif p_char == '?' or p_char == s[i-1]:
                    # '?'匹配任意单个字符，或者字符相等
                    dp[i][j] = dp[i-1][j-1]
                # 其他情况默认为False
        
        return dp[m][n]
    
    '''
    类似题目7：交错字符串（LeetCode 97）
    题目描述：
    给定三个字符串 s1、s2、s3，请你帮忙验证 s3 是否是由 s1 和 s2 交错组成的。
    两个字符串 s 和 t 交错的定义与过程如下，其中每个字符串都会被分割成若干非空子字符串：
    s = s1 + s2 + ... + sn
    t = t1 + t2 + ... + tm
    |n - m| <= 1
    交错 是 s1 + t1 + s2 + t2 + s3 + t3 + ... 或者 t1 + s1 + t2 + s2 + t3 + s3 + ...
    
    示例：
    输入：s1 = "aabcc", s2 = "dbbca", s3 = "aadbbcbcac"
    输出：true
    
    解题思路：
    使用动态规划解决。
    dp[i][j] 表示s1的前i个字符和s2的前j个字符是否能组成s3的前i+j个字符。
    '''
    
    # 交错字符串 - 动态规划解法
    # 时间复杂度: O(m * n)，其中m和n分别是字符串s1和s2的长度
    # 空间复杂度: O(m * n)
    @staticmethod
    def is_interleave(s1: str, s2: str, s3: str) -> bool:
        m = len(s1)
        n = len(s2)
        length = len(s3)
        
        # 长度不匹配，直接返回false
        if m + n != length:
            return False
        
        # dp[i][j] 表示s1的前i个字符和s2的前j个字符是否能组成s3的前i+j个字符
        dp = [[False for _ in range(n + 1)] for _ in range(m + 1)]
        
        # 空字符串和空字符串可以组成空字符串
        dp[0][0] = True
        
        # 初始化第一行：只使用s2的情况
        for j in range(1, n + 1):
            dp[0][j] = dp[0][j-1] and (s2[j-1] == s3[j-1])
        
        # 初始化第一列：只使用s1的情况
        for i in range(1, m + 1):
            dp[i][0] = dp[i-1][0] and (s1[i-1] == s3[i-1])
        
        # 状态转移
        for i in range(1, m + 1):
            for j in range(1, n + 1):
                # 可以从s1转移过来或从s2转移过来
                dp[i][j] = (dp[i-1][j] and s1[i-1] == s3[i+j-1]) or \
                          (dp[i][j-1] and s2[j-1] == s3[i+j-1])
        
        return dp[m][n]


# 测试方法
if __name__ == "__main__":
    # 测试最长公共子序列
    text1 = "abcde"
    text2 = "ace"
    print("最长公共子序列解法一结果:", Code03_PermutationLCS_Expanded.longest_common_subsequence1(text1, text2))
    print("最长公共子序列解法二结果:", Code03_PermutationLCS_Expanded.longest_common_subsequence2(text1, text2))
    
    # 测试最长重复子数组
    A = [1,2,3,2,1]
    B = [3,2,1,4,7]
    print("最长重复子数组解法一结果:", Code03_PermutationLCS_Expanded.find_length(A, B))
    print("最长重复子数组解法二结果:", Code03_PermutationLCS_Expanded.find_length2(A, B))
    
    # 测试不同的子序列
    s = "rabbbit"
    t = "rabbit"
    print("不同的子序列结果:", Code03_PermutationLCS_Expanded.num_distinct(s, t))
    
    # 测试编辑距离
    word1 = "horse"
    word2 = "ros"
    print("编辑距离结果:", Code03_PermutationLCS_Expanded.min_distance(word1, word2))
    
    # 测试最长递增子序列
    nums = [10, 9, 2, 5, 3, 7, 101, 18]
    print("最长递增子序列结果:", Code03_PermutationLCS_Expanded.length_of_lis(nums))
    
    # 测试通配符匹配
    s_pattern = "adceb"
    p_pattern = "*a*b"
    print("通配符匹配结果:", Code03_PermutationLCS_Expanded.is_match(s_pattern, p_pattern))
    
    # 测试交错字符串
    s1 = "aabcc"
    s2 = "dbbca"
    s3 = "aadbbcbcac"
    print("交错字符串结果:", Code03_PermutationLCS_Expanded.is_interleave(s1, s2, s3))