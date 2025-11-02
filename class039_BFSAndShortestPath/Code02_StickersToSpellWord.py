# 贴纸拼词
# 我们有 n 种不同的贴纸。每个贴纸上都有一个小写的英文单词。
# 您想要拼写出给定的字符串 target ，方法是从收集的贴纸中切割单个字母并重新排列它们
# 如果你愿意，你可以多次使用每个贴纸，每个贴纸的数量是无限的。
# 返回你需要拼出 target 的最小贴纸数量。如果任务不可能，则返回 -1
# 注意：在所有的测试用例中，所有的单词都是从 1000 个最常见的美国英语单词中随机选择的
# 并且 target 被选择为两个随机单词的连接。
# 测试链接 : https://leetcode.cn/problems/stickers-to-spell-word/
# 
# 算法思路：
# 使用BFS搜索，状态是当前还需要拼写的字符串
# 初始状态是target，目标状态是空字符串
# 对于每个状态，尝试使用每种贴纸，得到新的状态
# 使用记忆化搜索避免重复计算
# 
# 时间复杂度：O(2^n * m * k)，其中n是target长度，m是贴纸数量，k是贴纸平均长度
# 空间复杂度：O(2^n)，用于存储访问过的状态
# 
# 工程化考量：
# 1. 字符串预处理：对贴纸中的字符进行排序，便于处理
# 2. 优化：只考虑能减少目标字符串第一个字符的贴纸
# 3. 边界情况：如果目标字符串中有贴纸中没有的字符，直接返回-1

from collections import deque
from typing import List

def minStickers(stickers: List[str], target: str) -> int:
    """
    计算拼出目标字符串所需的最少贴纸数量
    
    Args:
        stickers: List[str] - 贴纸列表，每个贴纸是一个小写英文单词
        target: str - 目标字符串
        
    Returns:
        int - 最少贴纸数量，如果无法拼出则返回-1
    """
    # 对贴纸和目标字符串进行排序，便于处理
    sorted_stickers = [sort_string(s) for s in stickers]
    target = sort_string(target)
    
    # 构建图结构：每个字符对应的贴纸列表
    graph = [[] for _ in range(26)]
    
    # 对每个贴纸进行预处理，按字符排序
    for sticker in sorted_stickers:
        # 对于每个字符，记录包含该字符的贴纸
        for i in range(len(sticker)):
            # 避免重复添加相同贴纸
            if i == 0 or sticker[i] != sticker[i - 1]:
                graph[ord(sticker[i]) - ord('a')].append(sticker)
    
    # BFS队列和访问记录
    queue = deque()
    visited = set()
    
    # 初始状态
    queue.append(target)
    visited.add(target)
    level = 1
    
    # 使用队列的形式是整层弹出
    while queue:
        size = len(queue)
        # 处理当前层的所有状态
        for _ in range(size):
            cur = queue.popleft()
            # 只考虑能消除第一个字符的贴纸
            for sticker in graph[ord(cur[0]) - ord('a')]:
                next_state = next_string(cur, sticker)
                # 如果已经拼完所有字符
                if next_state == "":
                    return level
                elif next_state not in visited:
                    visited.add(next_state)
                    queue.append(next_state)
        level += 1
    
    return -1

def sort_string(s: str) -> str:
    """对字符串按字符排序"""
    return ''.join(sorted(s))

def next_string(t: str, s: str) -> str:
    """用贴纸s消除目标字符串t中的字符"""
    builder = []
    i, j = 0, 0
    
    # 双指针处理
    while i < len(t):
        # 如果贴纸字符用完了，或者目标字符小于贴纸字符，保留目标字符
        if j == len(s):
            builder.append(t[i])
            i += 1
        else:
            # 如果目标字符小于贴纸字符，保留目标字符
            if t[i] < s[j]:
                builder.append(t[i])
                i += 1
            # 如果目标字符大于贴纸字符，移动贴纸指针
            elif t[i] > s[j]:
                j += 1
            # 如果字符相等，同时移动两个指针（相当于消除）
            else:
                i += 1
                j += 1
    
    return ''.join(builder)

# 测试代码
if __name__ == "__main__":
    # 测试用例1
    stickers1 = ["with","example","science"]
    target1 = "thehat"
    print("测试用例1结果:", minStickers(stickers1, target1))  # 期望输出: 3
    
    # 测试用例2
    stickers2 = ["notice","possible"]
    target2 = "basicbasic"
    print("测试用例2结果:", minStickers(stickers2, target2))  # 期望输出: -1