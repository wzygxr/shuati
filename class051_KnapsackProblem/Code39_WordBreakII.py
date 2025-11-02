# LeetCode 140. 单词拆分 II
# 题目描述：给定一个字符串 s 和一个字符串字典 wordDict ，在字符串 s 中增加空格来构建一个句子，使得句子中所有的单词都在词典中。返回所有这样的可能句子。
# 链接：https://leetcode.cn/problems/word-break-ii/
# 
# 解题思路：
# 这是一个完全背包问题的变种，同时也是一个组合问题。我们需要找到所有可能的单词组合，使得它们的拼接等于字符串s。
# 
# 我们可以使用递归+记忆化搜索来解决这个问题：
# 1. 使用记忆化缓存，避免重复计算
# 2. 对于每个位置i，我们尝试所有可能的单词，如果s[i:j]在字典中，我们递归处理剩余部分
# 3. 将当前单词与剩余部分的结果组合
# 
# 时间复杂度：O(n^2 * 2^n)，其中n是字符串s的长度。在最坏情况下，每个字符之间都可以拆分，会产生2^(n-1)种拆分方式。
# 空间复杂度：O(n^2)，用于存储记忆化缓存。

from functools import lru_cache
from typing import List

def word_break(s: str, word_dict: List[str]) -> List[str]:
    """
    返回所有可能的单词拆分方案
    
    参数:
        s: 字符串
        word_dict: 单词字典
    
    返回:
        所有可能的拆分方案列表
    """
    if not s or not word_dict:
        return []
    
    # 将字典转换为集合，提高查找效率
    word_set = set(word_dict)
    
    # 首先使用动态规划检查是否可以拆分，如果不能拆分直接返回空列表
    # 这一步可以避免不必要的递归计算
    if not can_break(s, word_set):
        return []
    
    # 创建记忆化缓存，使用lru_cache装饰器
    @lru_cache(maxsize=None)
    def dfs(start: int) -> List[str]:
        """
        递归辅助函数，使用记忆化搜索找出所有可能的拆分方案
        
        参数:
            start: 起始位置
        
        返回:
            从start位置开始的子串的所有可能拆分方案
        """
        # 基础情况：已经处理到字符串末尾
        if start == len(s):
            return ['']  # 返回空字符串作为递归终止条件
        
        result = []
        
        # 尝试所有可能的结束位置
        for end in range(start + 1, len(s) + 1):
            # 获取当前子串
            word = s[start:end]
            
            # 如果当前子串在字典中，递归处理剩余部分
            if word in word_set:
                # 递归获取剩余部分的所有拆分方案
                sub_list = dfs(end)
                
                # 将当前单词与剩余部分的拆分方案组合
                for sub in sub_list:
                    # 如果sub为空字符串，说明已经到达字符串末尾，不需要添加空格
                    if sub:
                        result.append(word + ' ' + sub)
                    else:
                        result.append(word)
        
        return result
    
    return dfs(0)

def can_break(s: str, word_set: set) -> bool:
    """
    使用动态规划检查字符串是否可以拆分
    
    参数:
        s: 字符串
        word_set: 字典集合
    
    返回:
        是否可以拆分
    """
    n = len(s)
    dp = [False] * (n + 1)
    dp[0] = True  # 空字符串可以被拆分
    
    # 找出字典中最长单词的长度
    max_length = 0
    for word in word_set:
        max_length = max(max_length, len(word))
    
    for i in range(1, n + 1):
        # 只检查j >= i - max_length的情况，避免不必要的检查
        start = max(0, i - max_length)
        for j in range(start, i):
            if dp[j] and s[j:i] in word_set:
                dp[i] = True
                break
    
    return dp[n]

def word_break_dp(s: str, word_dict: List[str]) -> List[str]:
    """
    使用动态规划来存储所有可能的拆分方案
    
    参数:
        s: 字符串
        word_dict: 单词字典
    
    返回:
        所有可能的拆分方案列表
    """
    if not s or not word_dict:
        return []
    
    # 将字典转换为集合，提高查找效率
    word_set = set(word_dict)
    n = len(s)
    
    # dp[i]存储前i个字符的所有可能拆分方案
    dp = [[] for _ in range(n + 1)]
    
    # 初始状态：空字符串有一个拆分方案（空字符串）
    dp[0].append('')
    
    # 填充dp数组
    for i in range(1, n + 1):
        for j in range(0, i):
            word = s[j:i]
            if word in word_set and dp[j]:
                # 将当前单词与前j个字符的所有拆分方案组合
                for prev in dp[j]:
                    if prev:
                        dp[i].append(prev + ' ' + word)
                    else:
                        dp[i].append(word)
    
    return dp[n]

def word_break_optimized(s: str, word_dict: List[str]) -> List[str]:
    """
    优化的DFS实现，使用最大单词长度来限制搜索范围
    
    参数:
        s: 字符串
        word_dict: 单词字典
    
    返回:
        所有可能的拆分方案列表
    """
    if not s or not word_dict:
        return []
    
    # 将字典转换为集合，提高查找效率
    word_set = set(word_dict)
    
    # 找出字典中最长单词的长度
    max_length = 0
    for word in word_dict:
        max_length = max(max_length, len(word))
    
    # 首先检查是否可以拆分
    if not can_break(s, word_set):
        return []
    
    # 创建记忆化缓存
    @lru_cache(maxsize=None)
    def dfs(start: int) -> List[str]:
        """
        递归辅助函数，使用记忆化搜索找出所有可能的拆分方案
        
        参数:
            start: 起始位置
        
        返回:
            从start位置开始的子串的所有可能拆分方案
        """
        # 基础情况：已经处理到字符串末尾
        if start == len(s):
            return ['']
        
        result = []
        
        # 限制end的范围为start + max_length，避免不必要的检查
        end_max = min(start + max_length, len(s))
        for end in range(start + 1, end_max + 1):
            word = s[start:end]
            
            if word in word_set:
                sub_list = dfs(end)
                
                for sub in sub_list:
                    if sub:
                        result.append(word + ' ' + sub)
                    else:
                        result.append(word)
        
        return result
    
    return dfs(0)

def word_break_backtracking(s: str, word_dict: List[str]) -> List[str]:
    """
    使用回溯算法实现
    
    参数:
        s: 字符串
        word_dict: 单词字典
    
    返回:
        所有可能的拆分方案列表
    """
    if not s or not word_dict:
        return []
    
    word_set = set(word_dict)
    result = []
    path = []
    
    # 首先检查是否可以拆分
    if not can_break(s, word_set):
        return []
    
    def backtrack(start: int):
        """回溯辅助函数"""
        # 如果已经处理到字符串末尾，将当前路径添加到结果中
        if start == len(s):
            result.append(' '.join(path))
            return
        
        # 尝试所有可能的结束位置
        for end in range(start + 1, len(s) + 1):
            word = s[start:end]
            
            if word in word_set:
                # 选择当前单词
                path.append(word)
                # 递归处理剩余部分
                backtrack(end)
                # 回溯，撤销选择
                path.pop()
    
    backtrack(0)
    return result

# 测试代码
if __name__ == "__main__":
    # 测试用例1
    s1 = "catsanddog"
    word_dict1 = ["cat", "cats", "and", "sand", "dog"]
    print(f"测试用例1结果: {word_break(s1, word_dict1)}")
    # 预期输出: ["cats and dog", "cat sand dog"]
    
    # 测试用例2
    s2 = "pineapplepenapple"
    word_dict2 = ["apple", "pen", "applepen", "pine", "pineapple"]
    print(f"测试用例2结果: {word_break(s2, word_dict2)}")
    # 预期输出: ["pine apple pen apple", "pineapple pen apple", "pine applepen apple"]
    
    # 测试用例3
    s3 = "catsandog"
    word_dict3 = ["cats", "dog", "sand", "and", "cat"]
    print(f"测试用例3结果: {word_break(s3, word_dict3)}")
    # 预期输出: []
    
    # 测试不同实现
    print("\n测试不同实现:")
    print(f"动态规划版本 (测试用例1): {word_break_dp(s1, word_dict1)}")
    print(f"优化DFS版本 (测试用例1): {word_break_optimized(s1, word_dict1)}")
    print(f"回溯算法版本 (测试用例1): {word_break_backtracking(s1, word_dict1)}")
    
    # 测试用例4
    s4 = "a"
    word_dict4 = ["a"]
    print(f"\n测试用例4结果: {word_break(s4, word_dict4)}")
    # 预期输出: ["a"]
    
    # 测试用例5
    s5 = "aaaaaaaa"
    word_dict5 = ["a","aa","aaa","aaaa","aaaaa","aaaaaa","aaaaaaa","aaaaaaaa"]
    print(f"\n测试用例5结果: {word_break_optimized(s5, word_dict5)}")
    # 应该输出所有可能的拆分方案