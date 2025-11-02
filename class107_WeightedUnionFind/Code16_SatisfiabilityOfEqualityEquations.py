"""
带权并查集解决等式方程可满足性问题 (Python版本)

问题分析：
判断给定的等式和不等式约束是否可以同时满足

核心思想：
1. 先处理所有等式约束，建立变量间的连通关系
2. 再检查所有不等式约束，确保不会破坏已建立的连通关系

时间复杂度分析：
- prepare: O(1)
- find: O(α(1)) 近似O(1)
- union: O(α(1)) 近似O(1)
- 总体: O(n * α(1))，其中n是方程数量

空间复杂度: O(1) 用于存储26个小写字母的father数组

应用场景：
- 约束满足问题
- 逻辑一致性验证
- 等式方程求解

题目来源：LeetCode 990
题目链接：https://leetcode.com/problems/satisfiability-of-equality-equations/
题目名称：Satisfiability of Equality Equations
"""

class WeightedUnionFind:
    def __init__(self):
        """
        初始化带权并查集
        """
        self.father = list(range(26))  # father[i] 表示变量i的父节点（这里用0-25表示a-z）
        
    def find(self, i):
        """
        查找变量i的根节点，并进行路径压缩
        时间复杂度: O(α(1)) 近似O(1)
        
        :param i: 要查找的变量（0-25表示a-z）
        :return: 变量i所在集合的根节点
        """
        # 如果不是根节点
        if i != self.father[i]:
            # 递归查找根节点，同时进行路径压缩
            self.father[i] = self.find(self.father[i])
        return self.father[i]
    
    def union(self, i, j):
        """
        合并两个变量所在的集合
        时间复杂度: O(α(1)) 近似O(1)
        
        :param i: 变量i（0-25表示a-z）
        :param j: 变量j（0-25表示a-z）
        """
        # 查找两个变量的根节点
        fi = self.find(i)
        fj = self.find(j)
        # 如果不在同一集合中
        if fi != fj:
            # 合并两个集合
            self.father[fi] = fj

def equationsPossible(equations):
    """
    判断等式方程是否可满足
    
    :param equations: 等式方程数组
    :return: 如果可满足返回True，否则返回False
    """
    # 初始化带权并查集
    wuf = WeightedUnionFind()
    
    # 先处理所有等式约束
    for equation in equations:
        # 如果是等式
        if equation[1] == '=':
            # 提取变量
            a = ord(equation[0]) - ord('a')
            b = ord(equation[3]) - ord('a')
            # 合并变量
            wuf.union(a, b)
    
    # 再检查所有不等式约束
    for equation in equations:
        # 如果是不等式
        if equation[1] == '!':
            # 提取变量
            a = ord(equation[0]) - ord('a')
            b = ord(equation[3]) - ord('a')
            # 如果两个变量在同一集合中，说明矛盾
            if wuf.find(a) == wuf.find(b):
                return False
    
    # 所有约束都满足
    return True

# 测试用例
if __name__ == "__main__":
    # 测试用例1
    equations1 = ["a==b", "b!=a"]
    print(equationsPossible(equations1))  # False
    
    # 测试用例2
    equations2 = ["b==a", "a==b"]
    print(equationsPossible(equations2))  # True
    
    # 测试用例3
    equations3 = ["a==b", "b==c", "a==c"]
    print(equationsPossible(equations3))  # True
    
    # 测试用例4
    equations4 = ["a==b", "b!=c", "c==a"]
    print(equationsPossible(equations4))  # False
    
    # 测试用例5
    equations5 = ["c==c", "b==d", "x!=z"]
    print(equationsPossible(equations5))  # True