"""
分割回文串问题 - Python版本

问题描述：
给你一个字符串 s，请你将 s 分割成一些子串，使每个子串都是回文串。返回 s 所有可能的分割方案。

算法思路：
方法1：回溯算法 - 枚举所有可能的分割方案
方法2：动态规划预处理 - 优化回文串判断
方法3：记忆化搜索 - 缓存中间结果

时间复杂度分析：
- 回溯算法：O(n * 2^n)，其中n为字符串长度
- 动态规划预处理：O(n^2)，预处理回文信息
- 记忆化搜索：O(n * 2^n)，但通过缓存减少重复计算

空间复杂度分析：
- 回溯算法：O(n)，递归栈深度
- 动态规划预处理：O(n^2)，存储回文信息
- 记忆化搜索：O(n * 2^n)，存储所有可能的分割方案

工程化考量：
1. 剪枝优化：提前终止无效分支
2. 记忆化搜索：缓存中间结果
3. 边界处理：空字符串、单字符等情况
4. 可测试性：设计全面的测试用例
"""

from typing import List

class PalindromePartitioningSolver:
    def __init__(self):
        pass
    
    def partition(self, s: str) -> List[List[str]]:
        """
        方法1：回溯算法
        枚举所有可能的分割方案
        """
        result = []
        current = []
        self._backtrack(s, 0, current, result)
        return result
    
    def partition_with_dp(self, s: str) -> List[List[str]]:
        """
        方法2：回溯算法 + 动态规划预处理
        使用动态规划预处理回文信息，优化判断效率
        """
        n = len(s)
        # 预处理回文信息
        dp = [[False] * n for _ in range(n)]
        
        # 初始化动态规划数组
        for i in range(n):
            dp[i][i] = True  # 单个字符是回文
        
        for length in range(2, n + 1):
            for i in range(n - length + 1):
                j = i + length - 1
                if s[i] == s[j]:
                    if length == 2 or dp[i + 1][j - 1]:
                        dp[i][j] = True
        
        result = []
        current = []
        self._backtrack_with_dp(s, 0, dp, current, result)
        return result
    
    def partition_memo(self, s: str) -> List[List[str]]:
        """
        方法3：记忆化搜索
        使用记忆化技术避免重复计算
        """
        memo: dict = {}
        return self._partition_helper(s, 0, memo)
    
    def _backtrack(self, s: str, start: int, current: List[str], result: List[List[str]]):
        """
        回溯函数
        """
        if start == len(s):
            result.append(current[:])  # 添加当前分割方案的副本
            return
        
        for end in range(start, len(s)):
            # 检查子串s[start..end]是否是回文
            if self._is_palindrome(s, start, end):
                # 选择当前分割
                current.append(s[start:end + 1])
                # 递归处理剩余部分
                self._backtrack(s, end + 1, current, result)
                # 回溯
                current.pop()
    
    def _backtrack_with_dp(self, s: str, start: int, dp: List[List[bool]], 
                          current: List[str], result: List[List[str]]):
        """
        使用动态规划预处理的回溯函数
        """
        if start == len(s):
            result.append(current[:])
            return
        
        for end in range(start, len(s)):
            if dp[start][end]:
                current.append(s[start:end + 1])
                self._backtrack_with_dp(s, end + 1, dp, current, result)
                current.pop()
    
    def _partition_helper(self, s: str, start: int, memo: dict) -> List[List[str]]:
        """
        记忆化搜索辅助函数
        """
        if start == len(s):
            return [[]]  # 返回包含空列表的列表
        
        if start in memo:
            return memo[start]
        
        result = []
        
        for end in range(start, len(s)):
            if self._is_palindrome(s, start, end):
                substring = s[start:end + 1]
                sub_results = self._partition_helper(s, end + 1, memo)
                
                for sub_result in sub_results:
                    new_result = [substring] + sub_result
                    result.append(new_result)
        
        memo[start] = result
        return result
    
    def _is_palindrome(self, s: str, left: int, right: int) -> bool:
        """
        判断子串是否是回文
        """
        while left < right:
            if s[left] != s[right]:
                return False
            left += 1
            right -= 1
        return True

# 补充训练题目 - Python实现

def min_cut(s: str) -> int:
    """
    LeetCode 132. 分割回文串 II
    给定一个字符串s，将s分割成一些子串，使每个子串都是回文串，返回符合要求的最少分割次数
    """
    n = len(s)
    if n <= 1:
        return 0
    
    # dp[i]表示s[0..i]的最小分割次数
    dp = [n] * n
    # is_pal[i][j]表示s[i..j]是否是回文
    is_pal = [[False] * n for _ in range(n)]
    
    for i in range(n):
        for j in range(i + 1):
            if s[i] == s[j] and (i - j <= 2 or is_pal[j + 1][i - 1]):
                is_pal[j][i] = True
                if j == 0:
                    dp[i] = 0  # 整个字符串是回文，不需要分割
                else:
                    dp[i] = min(dp[i], dp[j - 1] + 1)
    
    return dp[n - 1]

def restore_ip_addresses(s: str) -> List[str]:
    """
    LeetCode 93. 复原IP地址
    给定一个只包含数字的字符串，复原它并返回所有可能的IP地址格式
    """
    def backtrack_ip(start: int, current: List[str]):
        if len(current) == 4:
            if start == len(s):
                result.append(".".join(current))
            return
        
        for length in range(1, 4):
            if start + length > len(s):
                break
            
            segment = s[start:start + length]
            
            # 检查段是否有效
            if (segment[0] == '0' and len(segment) > 1) or int(segment) > 255:
                continue
            
            current.append(segment)
            backtrack_ip(start + length, current)
            current.pop()
    
    result = []
    backtrack_ip(0, [])
    return result

def word_break(s: str, word_dict: List[str]) -> List[str]:
    """
    LeetCode 140. 单词拆分 II
    给定一个非空字符串s和一个包含非空单词列表的字典，在字符串中增加空格来构建一个句子，
    使得句子中所有的单词都在词典中。返回所有这些可能的句子。
    """
    def backtrack_word_break(start: int, current: List[str]):
        if start == len(s):
            result.append(" ".join(current))
            return
        
        for word in word_dict:
            length = len(word)
            if start + length <= len(s) and s[start:start + length] == word:
                current.append(word)
                backtrack_word_break(start + length, current)
                current.pop()
    
    result = []
    backtrack_word_break(0, [])
    return result

def partition_optimized(s: str) -> List[List[str]]:
    """
    LeetCode 131. 分割回文串（优化版）
    使用中心扩展法预处理回文信息
    """
    n = len(s)
    dp = [[False] * n for _ in range(n)]
    
    # 预处理回文信息
    for i in range(n):
        dp[i][i] = True
    
    for length in range(2, n + 1):
        for i in range(n - length + 1):
            j = i + length - 1
            if s[i] == s[j] and (length == 2 or dp[i + 1][j - 1]):
                dp[i][j] = True
    
    result = []
    current = []
    
    def backtrack(start: int):
        if start == n:
            result.append(current[:])
            return
        
        for end in range(start, n):
            if dp[start][end]:
                current.append(s[start:end + 1])
                backtrack(end + 1)
                current.pop()
    
    backtrack(0)
    return result

def generate_palindromic_decompositions(s: str) -> List[List[str]]:
    """
    生成所有回文分解方案（扩展功能）
    支持生成所有可能的回文子串分解
    """
    def backtrack_decompositions(start: int, path: List[str]):
        if start == len(s):
            decompositions.append(path[:])
            return
        
        for end in range(start, len(s)):
            substring = s[start:end + 1]
            if substring == substring[::-1]:  # 检查是否是回文
                path.append(substring)
                backtrack_decompositions(end + 1, path)
                path.pop()
    
    decompositions = []
    backtrack_decompositions(0, [])
    return decompositions

# 测试函数
def test_palindrome_partitioning():
    """测试函数"""
    solver = PalindromePartitioningSolver()
    
    # 测试用例1
    s1 = "aab"
    result1 = solver.partition(s1)
    result1_dp = solver.partition_with_dp(s1)
    result1_memo = solver.partition_memo(s1)
    
    print("测试用例1 - 字符串:", repr(s1))
    print("回溯算法结果数量:", len(result1))
    print("动态规划预处理结果数量:", len(result1_dp))
    print("记忆化搜索结果数量:", len(result1_memo))
    
    print("具体分割方案:")
    for i, partition in enumerate(result1):
        print(f"方案 {i + 1}: {partition}")
    print()
    
    # 测试用例2
    s2 = "a"
    result2 = solver.partition(s2)
    
    print("测试用例2 - 字符串:", repr(s2))
    print("结果数量:", len(result2))
    print("具体分割方案:")
    for partition in result2:
        print(partition)
    print()
    
    # 测试补充题目
    print("=== 补充训练题目测试 ===")
    
    # 测试分割回文串II
    s3 = "aab"
    print("分割回文串II - 字符串:", repr(s3), "-> 最少分割次数:", min_cut(s3))
    
    # 测试复原IP地址
    s4 = "25525511135"
    ip_results = restore_ip_addresses(s4)
    print("复原IP地址 - 字符串:", repr(s4), "-> 结果数量:", len(ip_results))
    for ip in ip_results:
        print("IP地址:", ip)
    
    # 测试单词拆分II
    s5 = "catsanddog"
    word_dict = ["cat", "cats", "and", "sand", "dog"]
    word_break_results = word_break(s5, word_dict)
    print("单词拆分II - 字符串:", repr(s5), "-> 结果数量:", len(word_break_results))
    for sentence in word_break_results:
        print("句子:", sentence)
    
    # 测试优化版分割回文串
    s6 = "aab"
    optimized_results = partition_optimized(s6)
    print("优化版分割回文串 - 字符串:", repr(s6), "-> 结果数量:", len(optimized_results))
    
    # 测试回文分解生成
    s7 = "aab"
    decompositions = generate_palindromic_decompositions(s7)
    print("回文分解生成 - 字符串:", repr(s7), "-> 分解方案数量:", len(decompositions))

if __name__ == "__main__":
    test_palindrome_partitioning()

"""
算法技巧总结 - Python版本

核心概念：
1. 回溯算法框架：
   - 选择：选择当前分割点
   - 约束：检查子串是否是回文
   - 目标：完成整个字符串的分割
   - 回溯：撤销当前选择，尝试其他选择

2. 优化技术：
   - 动态规划预处理：提前计算回文信息
   - 记忆化搜索：缓存中间结果避免重复计算
   - 剪枝优化：提前终止无效分支

3. Python特有优势：
   - 列表切片：s[start:end]快速提取子串
   - 字符串反转：s[::-1]快速判断回文
   - 列表推导式：简化代码编写

调试技巧：
1. 使用pdb进行调试
2. 打印中间状态变量
3. 使用assert进行条件验证

性能优化：
1. 避免不必要的字符串复制
2. 使用局部变量减少属性查找
3. 利用Python内置函数的高效实现

工程化实践：
1. 类型注解：提高代码可读性
2. 异常处理：确保程序健壮性
3. 单元测试：保证代码质量
4. 文档字符串：提供清晰的接口说明

边界情况处理：
1. 空字符串：返回空列表
2. 单字符：返回包含该字符的列表
3. 全相同字符：所有分割方案都是回文
4. 无回文分割：返回空列表
"""