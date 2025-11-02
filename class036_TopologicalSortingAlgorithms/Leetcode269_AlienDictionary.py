"""
LeetCode 269. Alien Dictionary

题目链接: https://leetcode.com/problems/alien-dictionary/

题目描述：
现有一种使用英语字母的外星语言，这门语言的字母顺序与英语顺序不同。
给定一个字符串数组 words，表示这门新语言的词典。words 中的字符串按这门新语言的字母顺序排列。
如果这种说法是错误的，并且按字典序排列是无效的，则返回 ""。
否则，请返回该语言的唯一字母顺序，按新语言的字母顺序排列。
如果有多种可能的答案，返回其中任意一个即可。

解题思路：
这是一个拓扑排序问题。我们需要根据给定的单词顺序推断出字符之间的顺序关系，
然后使用拓扑排序来确定字符的正确顺序。

步骤：
1. 构建图：比较相邻的单词，找到第一个不同的字符，建立字符间的有向边
2. 计算每个字符的入度
3. 使用Kahn算法进行拓扑排序
4. 检查结果是否包含所有字符（判断是否存在环）

时间复杂度：O(C)，其中C是所有单词中字符的总数
空间复杂度：O(1)，因为字符集大小是固定的（最多26个小写字母）

示例：
输入：words = ["wrt","wrf","er","ett","rftt"]
输出："wertf"

输入：words = ["z","x"]
输出："zx"

输入：words = ["z","x","z"]
输出：""
解释：不存在合法的字母顺序，因为存在环。
"""

from collections import deque, defaultdict
from typing import List

class Solution:
    def alienOrder(self, words: List[str]) -> str:
        """
        返回外星语的字母顺序
        :param words: 按外星语字典序排列的单词数组
        :return: 外星语的字母顺序，如果不存在合法顺序则返回空字符串
        """
        # 构建图和入度数组
        graph = defaultdict(set)
        in_degree = defaultdict(int)
        
        # 初始化所有字符
        for word in words:
            for char in word:
                graph[char] = set()
                in_degree[char] = 0
        
        # 构建图：比较相邻单词，找到字符顺序关系
        for i in range(len(words) - 1):
            word1 = words[i]
            word2 = words[i + 1]
            
            # 检查无效情况：word1比word2长，但word2是word1的前缀
            # 例如：["abc", "ab"] 这种情况是无效的
            if len(word1) > len(word2) and word1.startswith(word2):
                return ""
            
            # 找到第一个不同的字符，建立边
            for j in range(min(len(word1), len(word2))):
                char1 = word1[j]
                char2 = word2[j]
                
                if char1 != char2:
                    # 如果这条边还没有添加过
                    if char2 not in graph[char1]:
                        graph[char1].add(char2)
                        in_degree[char2] += 1
                    break  # 只比较第一个不同的字符
        
        # 使用Kahn算法进行拓扑排序
        return self.topological_sort(graph, in_degree)
    
    def topological_sort(self, graph: defaultdict, in_degree: defaultdict) -> str:
        """
        使用Kahn算法进行拓扑排序，返回字符顺序
        :param graph: 字符关系图
        :param in_degree: 字符入度映射
        :return: 字符的拓扑排序结果，如果存在环则返回空字符串
        """
        queue = deque()
        
        # 将所有入度为0的字符加入队列
        for char in in_degree:
            if in_degree[char] == 0:
                queue.append(char)
        
        result = []
        
        # Kahn算法进行拓扑排序
        while queue:
            current_char = queue.popleft()
            result.append(current_char)
            
            # 遍历当前字符的所有后续字符
            for next_char in graph[current_char]:
                # 将后续字符的入度减1
                in_degree[next_char] -= 1
                
                # 如果后续字符的入度变为0，则加入队列
                if in_degree[next_char] == 0:
                    queue.append(next_char)
        
        # 如果结果包含所有字符，说明不存在环，返回结果；否则返回空字符串
        return "".join(result) if len(result) == len(in_degree) else ""

def main():
    solution = Solution()
    
    # 测试用例1
    words1 = ["wrt", "wrf", "er", "ett", "rftt"]
    print(f"Test Case 1: {solution.alienOrder(words1)}")  # 应该输出 "wertf"
    
    # 测试用例2
    words2 = ["z", "x"]
    print(f"Test Case 2: {solution.alienOrder(words2)}")  # 应该输出 "zx"
    
    # 测试用例3
    words3 = ["z", "x", "z"]
    print(f"Test Case 3: {solution.alienOrder(words3)}")  # 应该输出 ""
    
    # 测试用例4
    words4 = ["abc", "ab"]
    print(f"Test Case 4: {solution.alienOrder(words4)}")  # 应该输出 ""

if __name__ == "__main__":
    main()