# LeetCode 139. 单词拆分
# 题目描述：给你一个字符串 s 和一个字符串列表 wordDict 作为字典。请你判断是否可以利用字典中出现的单词拼接出 s 。
# 注意：不要求字典中出现的单词全部都使用，并且字典中的单词可以重复使用。
# 链接：https://leetcode.cn/problems/word-break/
# 
# 解题思路：
# 这是一个完全背包问题的变种。我们可以将字符串s看作是背包，将字典中的单词看作是物品。
# 问题转化为：是否可以从字典中选择一些单词（可以重复选择），使得它们的拼接恰好等于字符串s。
# 
# 状态定义：dp[i] 表示字符串s的前i个字符是否可以被拆分成字典中的单词
# 状态转移方程：对于每个i，我们检查所有j < i，如果dp[j]为True且s[j:i]在字典中，则dp[i]为True
# 初始状态：dp[0] = True，表示空字符串可以被拆分
# 
# 时间复杂度：O(n^3)，其中n是字符串s的长度
# 空间复杂度：O(n + m)，其中m是字典中所有单词的字符总数

def word_break(s: str, word_dict: list[str]) -> bool:
    """
    判断字符串是否可以被拆分成字典中的单词
    
    参数:
        s: 要检查的字符串
        word_dict: 单词字典
    
    返回:
        是否可以拆分
    """
    if not s:
        return False
    
    # 将字典转换为集合，提高查找效率
    word_set = set(word_dict)
    n = len(s)
    
    # 创建DP数组，dp[i]表示字符串s的前i个字符是否可以被拆分成字典中的单词
    dp = [False] * (n + 1)
    
    # 初始状态：空字符串可以被拆分
    dp[0] = True
    
    # 遍历字符串的每个位置
    for i in range(1, n + 1):
        # 遍历所有可能的拆分点j
        for j in range(0, i):
            # 如果dp[j]为True（前j个字符可以拆分），且s[j:i]在字典中，那么dp[i]为True
            if dp[j] and s[j:i] in word_set:
                dp[i] = True
                break  # 只要找到一个可行的拆分方式就可以提前结束内层循环
    
    return dp[n]

def word_break_optimized(s: str, word_dict: list[str]) -> bool:
    """
    优化的版本，限制j的范围为最大单词长度，避免不必要的检查
    
    参数:
        s: 要检查的字符串
        word_dict: 单词字典
    
    返回:
        是否可以拆分
    """
    if not s:
        return False
    
    # 将字典转换为集合，提高查找效率
    word_set = set(word_dict)
    n = len(s)
    
    # 找出字典中最长单词的长度
    max_length = 0
    for word in word_dict:
        max_length = max(max_length, len(word))
    
    # 创建DP数组
    dp = [False] * (n + 1)
    dp[0] = True
    
    # 遍历字符串的每个位置
    for i in range(1, n + 1):
        # 只检查j >= i - max_length的情况，避免不必要的检查
        start = max(0, i - max_length)
        for j in range(start, i):
            if dp[j] and s[j:i] in word_set:
                dp[i] = True
                break
    
    return dp[n]

from functools import lru_cache

def word_break_recursive(s: str, word_dict: list[str]) -> bool:
    """
    使用递归+记忆化搜索实现
    
    参数:
        s: 要检查的字符串
        word_dict: 单词字典
    
    返回:
        是否可以拆分
    """
    if not s:
        return False
    
    # 将字典转换为集合
    word_set = set(word_dict)
    n = len(s)
    
    @lru_cache(maxsize=None)
    def dfs(start: int) -> bool:
        """
        递归辅助函数
        
        参数:
            start: 起始位置
        
        返回:
            从start位置开始的子串是否可以被拆分
        """
        # 基础情况：已经处理到字符串末尾
        if start == n:
            return True
        
        # 尝试所有可能的结束位置
        for end in range(start + 1, n + 1):
            # 如果s[start:end]在字典中，且剩余部分可以拆分，则返回True
            if s[start:end] in word_set and dfs(end):
                return True
        
        # 如果所有可能性都尝试过仍无法拆分，返回False
        return False
    
    return dfs(0)

def word_break_bfs(s: str, word_dict: list[str]) -> bool:
    """
    使用BFS实现
    
    参数:
        s: 要检查的字符串
        word_dict: 单词字典
    
    返回:
        是否可以拆分
    """
    if not s:
        return False
    
    word_set = set(word_dict)
    n = len(s)
    visited = [False] * n  # 记录哪些位置已经被访问过，避免重复处理
    
    # 使用队列进行BFS，队列中存储的是当前处理到的位置
    from collections import deque
    queue = deque([0])
    visited[0] = True
    
    while queue:
        start = queue.popleft()
        
        # 尝试所有可能的结束位置
        for end in range(start + 1, n + 1):
            # 如果当前子串在字典中，且结束位置尚未访问过，则继续BFS
            if s[start:end] in word_set and not visited[end]:
                if end == n:
                    return True  # 已经到达字符串末尾，找到了解决方案
                queue.append(end)
                visited[end] = True
    
    return False  # 无法拆分

def word_break_prefix_tree(s: str, word_dict: list[str]) -> bool:
    """
    使用前缀树（字典树）优化实现
    
    参数:
        s: 要检查的字符串
        word_dict: 单词字典
    
    返回:
        是否可以拆分
    """
    if not s:
        return False
    
    # 构建前缀树
    class TrieNode:
        def __init__(self):
            self.children = {}
            self.is_end_of_word = False
    
    root = TrieNode()
    for word in word_dict:
        node = root
        for char in word:
            if char not in node.children:
                node.children[char] = TrieNode()
            node = node.children[char]
        node.is_end_of_word = True
    
    n = len(s)
    dp = [False] * (n + 1)
    dp[0] = True
    
    for i in range(n):
        if not dp[i]:
            continue
        
        # 从当前位置开始查找字典树
        node = root
        for j in range(i, n):
            if s[j] not in node.children:
                break
            node = node.children[s[j]]
            if node.is_end_of_word:
                dp[j + 1] = True
    
    return dp[n]

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
    
    # 测试不同实现
    print("\n测试不同实现:")
    print(f"优化版本 (测试用例1): {word_break_optimized(s1, word_dict1)}")
    print(f"递归版本 (测试用例1): {word_break_recursive(s1, word_dict1)}")
    print(f"BFS版本 (测试用例1): {word_break_bfs(s1, word_dict1)}")
    print(f"前缀树版本 (测试用例1): {word_break_prefix_tree(s1, word_dict1)}")
    
    # 测试用例4
    s4 = "catsandog"
    word_dict4 = ["cats", "dog", "sand", "and", "cat", "sando", "g"]
    print(f"\n测试用例4结果: {word_break(s4, word_dict4)}")  # 预期输出: True
    
    # 测试用例5
    s5 = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaab"
    word_dict5 = ["a","aa","aaa","aaaa","aaaaa","aaaaaa","aaaaaaa","aaaaaaaa","aaaaaaaaa","aaaaaaaaaa"]
    print(f"\n测试用例5结果: {word_break_optimized(s5, word_dict5)}")  # 预期输出: False