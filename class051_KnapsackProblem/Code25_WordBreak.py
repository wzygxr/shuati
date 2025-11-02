# LeetCode 139. 单词拆分
# 题目描述：给你一个字符串 s 和一个字符串列表 wordDict 作为字典。请你判断是否可以利用字典中出现的单词拼接出 s 。
# 注意：不要求字典中出现的单词全部都使用，并且字典中的单词可以重复使用。
# 链接：https://leetcode.cn/problems/word-break/
# 
# 解题思路：
# 这是一个完全背包问题的变种，其中：
# - 背包容量：字符串s的长度
# - 物品：字典中的单词
# - 问题转化为：是否可以选择一些单词（可重复），恰好拼接成字符串s
# 
# 状态定义：dp[i] 表示字符串s的前i个字符是否可以被拆分
# 状态转移方程：对于每个位置i，遍历所有位置j（j < i），如果dp[j]为true且s[j:i]在字典中，则dp[i]为true
# 初始状态：dp[0] = true，表示空字符串可以被拆分
# 
# 时间复杂度：O(n^3)，其中n是字符串s的长度（需要两层循环，并且每次需要判断子字符串是否在字典中）
# 空间复杂度：O(n)，使用一维DP数组

from typing import List, Set, Dict, Optional


def word_break(s: str, word_dict: List[str]) -> bool:
    """
    判断是否可以利用字典中出现的单词拼接出s
    
    Args:
        s: 目标字符串
        word_dict: 单词字典
    
    Returns:
        bool: 是否可以拼接出s
    """
    # 参数验证
    if not s:
        return False  # 空字符串返回false
    
    # 将word_dict转换为set，提高查找效率
    word_set = set(word_dict)
    
    # 获取字典中单词的最大长度，用于后续剪枝
    max_word_length = 0
    for word in word_dict:
        max_word_length = max(max_word_length, len(word))
    
    # 创建一维DP数组，dp[i]表示字符串s的前i个字符是否可以被拆分
    dp = [False] * (len(s) + 1)
    
    # 初始状态：空字符串可以被拆分
    dp[0] = True
    
    # 遍历字符串s的每个位置i
    for i in range(1, len(s) + 1):
        # 遍历之前的位置j，从max(0, i - max_word_length)到i-1
        # 这样可以避免检查过长的子字符串
        start = max(0, i - max_word_length)
        for j in range(start, i):
            # 状态转移：如果前j个字符可以被拆分，且子字符串s[j:i]在字典中，则前i个字符可以被拆分
            if dp[j] and s[j:i] in word_set:
                dp[i] = True
                break  # 只要找到一种拆分方式即可
    
    # 返回结果：整个字符串s是否可以被拆分
    return dp[len(s)]


def word_break_optimized(s: str, word_dict: List[str]) -> bool:
    """
    优化版本：移除max_word_length的计算，直接使用普通的两层循环
    """
    # 参数验证
    if not s:
        return False
    
    # 将word_dict转换为set，提高查找效率
    word_set = set(word_dict)
    
    # 创建一维DP数组
    dp = [False] * (len(s) + 1)
    dp[0] = True
    
    # 遍历字符串s的每个位置i
    for i in range(1, len(s) + 1):
        # 遍历之前的位置j
        for j in range(i):
            # 状态转移
            if dp[j] and s[j:i] in word_set:
                dp[i] = True
                break  # 只要找到一种拆分方式即可
    
    return dp[len(s)]


def word_break_knapsack_style(s: str, word_dict: List[str]) -> bool:
    """
    另一种实现方式：先遍历字典中的单词，再遍历字符串位置
    这更符合完全背包问题的思路
    """
    # 参数验证
    if not s:
        return False
    
    # 创建一维DP数组
    dp = [False] * (len(s) + 1)
    dp[0] = True
    
    # 先遍历容量（字符串长度）
    for i in range(1, len(s) + 1):
        # 再遍历物品（字典中的单词）
        for word in word_dict:
            word_length = len(word)
            # 如果当前位置i大于等于单词长度，并且前i-word_length个字符可以被拆分
            # 并且子字符串s[i-word_length:i]等于当前单词
            if i >= word_length and dp[i - word_length] and \
               s[i - word_length:i] == word:
                dp[i] = True
                break  # 只要找到一种拆分方式即可
    
    return dp[len(s)]


def word_break_dfs(s: str, word_dict: List[str]) -> bool:
    """
    递归+记忆化搜索实现
    """
    # 参数验证
    if not s:
        return False
    
    # 将word_dict转换为set，提高查找效率
    word_set = set(word_dict)
    
    # 使用字典作为缓存，键为子字符串的起始索引，值为该子字符串是否可以被拆分
    memo = {}
    
    def dfs(start: int) -> bool:
        """递归辅助函数"""
        # 基础情况：如果已经到达字符串末尾，表示成功拆分
        if start == len(s):
            return True
        
        # 检查缓存
        if start in memo:
            return memo[start]
        
        # 尝试从start位置开始的所有可能的子字符串
        for end in range(start + 1, len(s) + 1):
            # 如果子字符串在字典中，并且剩余部分也可以被拆分
            if s[start:end] in word_set and dfs(end):
                memo[start] = True
                return True
        
        # 所有可能的拆分方式都失败了
        memo[start] = False
        return False
    
    # 调用递归函数
    return dfs(0)


def word_break_bfs(s: str, word_dict: List[str]) -> bool:
    """
    BFS实现
    """
    # 参数验证
    if not s:
        return False
    
    # 将word_dict转换为set，提高查找效率
    word_set = set(word_dict)
    
    # 创建队列，存储可以拆分到的位置
    from collections import deque
    queue = deque()
    # 标记已经访问过的位置，避免重复处理
    visited = [False] * len(s)
    
    # 初始位置为0
    queue.append(0)
    visited[0] = True
    
    while queue:
        start = queue.popleft()
        
        # 尝试从start位置开始的所有可能的子字符串
        for end in range(start + 1, len(s) + 1):
            # 如果子字符串在字典中
            if s[start:end] in word_set:
                # 如果已经到达字符串末尾，表示成功拆分
                if end == len(s):
                    return True
                # 如果该位置尚未访问过，将其加入队列
                if not visited[end]:
                    queue.append(end)
                    visited[end] = True
    
    # 队列为空仍未返回true，表示无法拆分
    return False


class TrieNode:
    """
    Trie树节点类
    """
    def __init__(self):
        self.children = {}
        self.is_end = False


def build_trie(word_dict: List[str]) -> TrieNode:
    """
    构建Trie树
    """
    root = TrieNode()
    for word in word_dict:
        node = root
        for char in word:
            if char not in node.children:
                node.children[char] = TrieNode()
            node = node.children[char]
        node.is_end = True
    return root


def word_break_with_trie(s: str, word_dict: List[str]) -> bool:
    """
    使用Trie树优化查找效率
    """
    # 参数验证
    if not s:
        return False
    
    # 构建Trie树
    root = build_trie(word_dict)
    
    # 创建一维DP数组
    dp = [False] * (len(s) + 1)
    dp[0] = True
    
    # 遍历字符串s的每个位置i
    for i in range(len(s)):
        # 如果前i个字符无法被拆分，跳过
        if not dp[i]:
            continue
        
        # 从i位置开始，在Trie树中查找可能的单词
        node = root
        for j in range(i, len(s)):
            char = s[j]
            if char not in node.children:
                break  # 无法继续匹配
            node = node.children[char]
            # 如果找到一个单词，标记dp[j+1]为true
            if node.is_end:
                dp[j + 1] = True
    
    return dp[len(s)]


# 测试代码
if __name__ == "__main__":
    # 测试用例1
    s1 = "leetcode"
    word_dict1 = ["leet", "code"]
    print(f"测试用例1结果: {word_break(s1, word_dict1)}")  # 预期输出: True
    
    # 测试用例2
    s2 = "applepenapple"
    word_dict2 = ["apple", "pen"]
    print(f"测试用例2结果: {word_break(s2, word_dict2)}")  # 预期输出: True
    
    # 测试用例3
    s3 = "catsandog"
    word_dict3 = ["cats", "dog", "sand", "and", "cat"]
    print(f"测试用例3结果: {word_break(s3, word_dict3)}")  # 预期输出: False
    
    # 测试用例4
    s4 = ""
    word_dict4 = ["a"]
    print(f"测试用例4结果: {word_break(s4, word_dict4)}")  # 预期输出: False