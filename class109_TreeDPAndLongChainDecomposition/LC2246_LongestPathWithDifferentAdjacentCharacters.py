# LeetCode 2246. 相邻字符不同的最长路径 - Python实现
# 树形DP经典题目

from typing import List
from collections import defaultdict

class LC2246_LongestPathWithDifferentAdjacentCharacters:
    """
    计算树中相邻字符不同的最长路径
    
    解题思路:
    1. 树形DP，通过DFS遍历来解决
    2. 对于每个节点，计算经过该节点的最长路径
    3. 维护每个节点向下延伸的最长路径和次长路径
    
    时间复杂度: O(n)
    空间复杂度: O(n)
    """
    
    def longestPath(self, parent: List[int], s: str) -> int:
        """
        计算树中相邻字符不同的最长路径
        
        Args:
            parent: 父节点数组，parent[i]表示节点i的父节点
            s: 节点字符组成的字符串
            
        Returns:
            相邻字符不同的最长路径长度
        """
        n = len(parent)
        self.s = s
        self.maxLength = 0
        
        # 构建邻接表表示的树
        graph = defaultdict(list)
        for i in range(1, n):
            graph[parent[i]].append(i)
        
        # 从根节点开始DFS
        self.dfs(0, graph)
        
        return self.maxLength
    
    def dfs(self, node: int, graph: dict) -> int:
        """
        DFS计算以当前节点为根的子树中最长路径
        
        Args:
            node: 当前节点
            graph: 邻接表表示的树
            
        Returns:
            从当前节点向下延伸的最长路径长度
        """
        first = 0   # 最长路径
        second = 0  # 次长路径
        
        # 遍历所有子节点
        for child in graph[node]:
            childPath = self.dfs(child, graph)
            
            # 只有当子节点字符与当前节点字符不同时，才能连接
            if self.s[child] != self.s[node]:
                # 更新最长路径和次长路径
                if childPath > first:
                    second = first
                    first = childPath
                elif childPath > second:
                    second = childPath
        
        # 经过当前节点的最长路径 = 最长路径 + 次长路径 + 1（当前节点）
        self.maxLength = max(self.maxLength, first + second + 1)
        
        # 返回从当前节点向下延伸的最长路径长度
        return first + 1

# 测试函数
def main():
    solution = LC2246_LongestPathWithDifferentAdjacentCharacters()
    
    # 测试用例1
    parent1 = [-1, 0, 0, 1, 1, 2]
    s1 = "abacbe"
    result1 = solution.longestPath(parent1, s1)
    print(f"测试用例1结果: {result1}")
    # 预期输出: 3
    
    # 测试用例2
    parent2 = [-1, 0, 0, 0]
    s2 = "aabc"
    result2 = solution.longestPath(parent2, s2)
    print(f"测试用例2结果: {result2}")
    # 预期输出: 3

# 程序入口
if __name__ == "__main__":
    main()