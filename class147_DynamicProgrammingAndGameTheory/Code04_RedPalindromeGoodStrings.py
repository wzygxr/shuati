"""
好串问题 - Python实现

题目描述：
可以用r、e、d三种字符拼接字符串，如果拼出来的字符串中
有且仅有1个长度>=2的回文子串，那么这个字符串定义为"好串"
返回长度为n的所有可能的字符串中，好串有多少个

解题思路：
这是一个组合数学问题，可以使用暴力递归、数学规律等方法解决
1. 暴力递归：生成所有字符串并检查（仅适用于小数据）
2. 数学规律：观察小数据找到规律公式
3. 动态规划：状态设计复杂，适用于中等规模数据

相关题目：
1. LeetCode 5. Longest Palindromic Substring：https://leetcode.com/problems/longest-palindromic-substring/
2. LeetCode 647. Palindromic Substrings：https://leetcode.com/problems/palindromic-substrings/
3. LeetCode 131. Palindrome Partitioning：https://leetcode.com/problems/palindrome-partitioning/
4. POJ 1159. Palindrome：http://poj.org/problem?id=1159
5. Manacher算法：线性时间求最长回文子串

工程化考量：
1. 异常处理：处理边界条件
2. 性能优化：使用数学规律O(1)解法
3. 取模运算：防止整数溢出
4. 可读性：清晰的变量命名和注释
"""

class RedPalindromeGoodStrings:
    MOD = 1000000007
    
    @staticmethod
    def num1(n: int) -> int:
        """暴力递归（仅适用于小数据）"""
        if n <= 0:
            return 0
            
        path = [''] * n
        return RedPalindromeGoodStrings._f(path, 0)
    
    @staticmethod
    def _f(path: list, i: int) -> int:
        if i == len(path):
            cnt = 0
            for l in range(len(path)):
                for r in range(l + 1, len(path)):
                    if RedPalindromeGoodStrings._is_palindrome(path, l, r):
                        cnt += 1
                    if cnt > 1:
                        return 0
            return 1 if cnt == 1 else 0
        else:
            ans = 0
            for char in ['r', 'e', 'd']:
                path[i] = char
                ans += RedPalindromeGoodStrings._f(path, i + 1)
            return ans
    
    @staticmethod
    def _is_palindrome(s: list, l: int, r: int) -> bool:
        while l < r:
            if s[l] != s[r]:
                return False
            l += 1
            r -= 1
        return True
    
    @staticmethod
    def num2(n: int) -> int:
        """数学规律法（最优解）"""
        if n == 1:
            return 0
        if n == 2:
            return 3
        if n == 3:
            return 18
        return (6 * (n + 1)) % RedPalindromeGoodStrings.MOD
    
    # ==================== 扩展题目1: 最长回文子串 ====================
    """
    LeetCode 5. Longest Palindromic Substring
    题目：找到字符串中最长的回文子串
    网址：https://leetcode.com/problems/longest-palindromic-substring/
    
    中心扩展法：
    时间复杂度：O(n^2)
    空间复杂度：O(1)
    """
    @staticmethod
    def longest_palindrome(s: str) -> str:
        if not s:
            return ""
            
        start, end = 0, 0
        
        for i in range(len(s)):
            len1 = RedPalindromeGoodStrings._expand_around_center(s, i, i)
            len2 = RedPalindromeGoodStrings._expand_around_center(s, i, i + 1)
            length = max(len1, len2)
            
            if length > end - start:
                start = i - (length - 1) // 2
                end = i + length // 2
                
        return s[start:end + 1]
    
    @staticmethod
    def _expand_around_center(s: str, left: int, right: int) -> int:
        while left >= 0 and right < len(s) and s[left] == s[right]:
            left -= 1
            right += 1
        return right - left - 1
    
    # ==================== 扩展题目2: 回文子串个数 ====================
    """
    LeetCode 647. Palindromic Substrings
    题目：计算字符串中回文子串的个数
    网址：https://leetcode.com/problems/palindromic-substrings/
    
    中心扩展法：
    时间复杂度：O(n^2)
    空间复杂度：O(1)
    """
    @staticmethod
    def count_substrings(s: str) -> int:
        if not s:
            return 0
            
        count = 0
        for i in range(len(s)):
            count += RedPalindromeGoodStrings._expand_and_count(s, i, i)
            count += RedPalindromeGoodStrings._expand_and_count(s, i, i + 1)
        return count
    
    @staticmethod
    def _expand_and_count(s: str, left: int, right: int) -> int:
        count = 0
        while left >= 0 and right < len(s) and s[left] == s[right]:
            count += 1
            left -= 1
            right += 1
        return count
    
    # ==================== 扩展题目3: 回文分割 ====================
    """
    LeetCode 131. Palindrome Partitioning
    题目：将字符串分割成回文子串，返回所有可能的分割方案
    网址：https://leetcode.com/problems/palindrome-partitioning/
    
    回溯+动态规划预处理：
    时间复杂度：O(n * 2^n)
    空间复杂度：O(n^2)
    """
    @staticmethod
    def partition(s: str) -> list:
        if not s:
            return []
            
        is_palindrome = RedPalindromeGoodStrings._preprocess(s)
        result = []
        RedPalindromeGoodStrings._backtrack(s, 0, [], result, is_palindrome)
        return result
    
    @staticmethod
    def _preprocess(s: str) -> list:
        n = len(s)
        dp = [[False] * n for _ in range(n)]
        
        for i in range(n):
            dp[i][i] = True
            
        for length in range(2, n + 1):
            for i in range(n - length + 1):
                j = i + length - 1
                if length == 2:
                    dp[i][j] = (s[i] == s[j])
                else:
                    dp[i][j] = (s[i] == s[j]) and dp[i + 1][j - 1]
                    
        return dp
    
    @staticmethod
    def _backtrack(s: str, start: int, current: list, result: list, is_palindrome: list):
        if start == len(s):
            result.append(current[:])
            return
            
        for end in range(start, len(s)):
            if is_palindrome[start][end]:
                current.append(s[start:end + 1])
                RedPalindromeGoodStrings._backtrack(s, end + 1, current, result, is_palindrome)
                current.pop()
    
    # ==================== 扩展题目4: 回文插入 ====================
    """
    POJ 1159. Palindrome
    题目：计算最少插入多少个字符能使字符串变成回文串
    网址：http://poj.org/problem?id=1159
    
    动态规划解法：
    时间复杂度：O(n^2)
    空间复杂度：O(n^2)
    """
    @staticmethod
    def min_insertions(s: str) -> int:
        if len(s) <= 1:
            return 0
            
        n = len(s)
        dp = [[0] * n for _ in range(n)]
        
        for length in range(2, n + 1):
            for i in range(n - length + 1):
                j = i + length - 1
                if s[i] == s[j]:
                    dp[i][j] = dp[i + 1][j - 1]
                else:
                    dp[i][j] = min(dp[i + 1][j], dp[i][j - 1]) + 1
                    
        return dp[0][n - 1]
    
    # ==================== 扩展题目5: Manacher算法 ====================
    """
    Manacher算法：线性时间求最长回文子串
    时间复杂度：O(n)
    空间复杂度：O(n)
    """
    @staticmethod
    def longest_palindrome_manacher(s: str) -> str:
        if not s:
            return ""
        if len(s) == 1:
            return s
            
        T = '#'
        for c in s:
            T += c + '#'
            
        n = len(T)
        p = [0] * n
        C, R = 0, 0
        max_len, center_index = 0, 0
        
        for i in range(n):
            mirror = 2 * C - i
            if i < R:
                p[i] = min(R - i, p[mirror])
                
            while (i - p[i] - 1 >= 0 and i + p[i] + 1 < n and 
                   T[i - p[i] - 1] == T[i + p[i] + 1]):
                p[i] += 1
                
            if i + p[i] > R:
                C = i
                R = i + p[i]
                
            if p[i] > max_len:
                max_len = p[i]
                center_index = i
                
        start = (center_index - max_len) // 2
        return s[start:start + max_len]

# 测试函数
def main():
    print("=== 好串问题测试 ===")
    for i in range(1, 11):
        result1 = 0
        if i <= 5:  # 只对小数据使用暴力方法
            result1 = RedPalindromeGoodStrings.num1(i)
        result2 = RedPalindromeGoodStrings.num2(i)
        print(f"n={i}: {result1} / {result2}")
    
    print("\n=== 扩展题目测试 ===")
    
    # 测试最长回文子串
    print(f"Longest Palindrome ('babad'): {RedPalindromeGoodStrings.longest_palindrome('babad')}")
    
    # 测试回文子串个数
    print(f"Count Substrings ('abc'): {RedPalindromeGoodStrings.count_substrings('abc')}")
    
    # 测试回文分割
    partitions = RedPalindromeGoodStrings.partition("aab")
    print(f"Palindrome Partitioning ('aab'): {len(partitions)} partitions")
    
    # 测试回文插入
    print(f"Min Insertions ('abca'): {RedPalindromeGoodStrings.min_insertions('abca')}")
    
    # 测试Manacher算法
    print(f"Longest Palindrome Manacher ('cbbd'): {RedPalindromeGoodStrings.longest_palindrome_manacher('cbbd')}")

if __name__ == "__main__":
    main()