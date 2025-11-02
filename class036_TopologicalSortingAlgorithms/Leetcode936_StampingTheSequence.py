"""
LeetCode 936. Stamping The Sequence

题目链接: https://leetcode.com/problems/stamping-the-sequence/

题目描述：
你想要用小写字母组成一个目标字符串 target。
开始时，序列由 target.length 个 '?' 记号组成。你有一个小写字母印章 stamp。
在每个回合中，你可以在序列上任意位置将 stamp 放置（可能与上一个位置重叠），并将该位置的字符替换为 stamp 中对应的字符。
一旦序列中出现 target，你的工作就完成了。
返回一个数组，包含在每个回合中放置印章的最左边位置。如果不能形成目标，则返回空数组。

解题思路：
这道题可以使用逆向思维和拓扑排序来解决。
我们从目标字符串开始，逆向思考如何通过移除印章来回到初始状态（全为'?'的状态）。

步骤：
1. 对于每个可能的印章位置，计算该位置的印章与目标字符串匹配的字符数
2. 将所有完全匹配（即入度为0）的位置加入队列
3. 使用类似Kahn算法的方法处理队列中的位置：
   - 处理一个位置时，将该位置的字符标记为'?'（表示已处理）
   - 更新受影响位置的匹配数（入度）
   - 如果某个位置变为完全匹配，则加入队列
4. 最后检查是否所有字符都被处理，如果是则返回结果的逆序

时间复杂度：O(N*(N-M))，其中N是target的长度，M是stamp的长度
空间复杂度：O(N*(N-M))

示例：
输入：stamp = "abc", target = "ababc"
输出：[0,2]
解释：最初 s = "?????"。
      - 选择位置 0，s = "abc??"
      - 选择位置 2，s = "ababc"

输入：stamp = "abca", target = "aabcaca"
输出：[3,0,1]
解释：最初 s = "???????"。
      - 选择位置 3，s = "???abca"
      - 选择位置 0，s = "abcabca"
      - 选择位置 1，s = "aabcaca"
"""

from collections import deque
from typing import List

class Solution:
    def movesToStamp(self, stamp: str, target: str) -> List[int]:
        """
        返回印章放置位置的序列
        :param stamp: 印章字符串
        :param target: 目标字符串
        :return: 印章放置位置的序列，如果无法形成目标则返回空数组
        """
        n = len(target)
        m = len(stamp)
        
        # 记录每个位置的匹配字符数（入度）
        in_degree = [0] * (n - m + 1)
        
        # 记录每个字符属于哪些印章位置
        belongs = [[] for _ in range(n)]
        
        # 初始化入度和belongs数组
        for i in range(n - m + 1):
            for j in range(m):
                if target[i + j] == stamp[j]:
                    in_degree[i] += 1
                belongs[i + j].append(i)
        
        # 将所有完全匹配的位置加入队列
        queue = deque()
        visited = [False] * (n - m + 1)
        result = []
        
        for i in range(n - m + 1):
            if in_degree[i] == m:
                queue.append(i)
                visited[i] = True
        
        # 标记已处理的字符
        processed = [False] * n
        
        # 类似Kahn算法的处理过程
        while queue:
            index = queue.popleft()
            result.append(index)
            
            # 将该位置的字符标记为已处理
            for i in range(m):
                if not processed[index + i]:
                    processed[index + i] = True
                    
                    # 更新受影响位置的入度
                    for j in belongs[index + i]:
                        if not visited[j]:
                            in_degree[j] -= 1
                            # 如果某个位置变为完全匹配，则加入队列
                            if in_degree[j] == 0:
                                queue.append(j)
                                visited[j] = True
        
        # 检查是否所有字符都被处理
        for i in range(n):
            if not processed[i]:
                return []  # 无法形成目标
        
        # 返回结果的逆序
        return result[::-1]

def main():
    solution = Solution()
    
    # 测试用例1
    stamp1 = "abc"
    target1 = "ababc"
    result1 = solution.movesToStamp(stamp1, target1)
    print(f"Test Case 1: {result1}")  # 应该输出 [0, 2]
    
    # 测试用例2
    stamp2 = "abca"
    target2 = "aabcaca"
    result2 = solution.movesToStamp(stamp2, target2)
    print(f"Test Case 2: {result2}")  # 应该输出 [3, 0, 1]

if __name__ == "__main__":
    main()