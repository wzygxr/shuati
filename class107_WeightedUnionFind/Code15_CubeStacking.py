import sys

"""
带权并查集解决立方体积木叠放问题 (Python版本)

问题分析：
维护立方体积木列的合并和查询操作，需要支持：
1. 将一个积木列整体移动到另一个积木列顶部
2. 查询某个积木下方的积木数量

核心思想：
1. 使用带权并查集维护积木之间的相对位置关系
2. dist[i] 表示积木i到其所在积木列底部的距离（以积木数量为单位）
3. size[i] 表示以积木i为根的积木列中积木的数量

时间复杂度分析：
- prepare: O(n)
- find: O(α(n)) 近似O(1)
- union: O(α(n)) 近似O(1)
- query: O(α(n)) 近似O(1)
- 总体: O(n + P * α(n))

空间复杂度: O(n) 用于存储father、dist和size数组

应用场景：
- 积木叠放与查询
- 动态维护序列位置关系
- 游戏中的编队系统

题目来源：POJ 1988
题目链接：http://poj.org/problem?id=1988
题目名称：Cube Stacking
"""

class WeightedUnionFind:
    def __init__(self, n):
        """
        初始化带权并查集
        :param n: 积木数量
        """
        self.father = list(range(n + 1))  # father[i] 表示积木i的父节点
        self.dist = [0] * (n + 1)         # dist[i] 表示积木i到其所在积木列底部的距离
        self.size = [1] * (n + 1)         # size[i] 表示以积木i为根的积木列中积木的数量
        
    def find(self, i):
        """
        查找积木i所在积木列的代表（底部），并进行路径压缩
        同时更新dist[i]为积木i到积木列底部的距离
        时间复杂度: O(α(n)) 近似O(1)
        
        :param i: 要查找的积木编号
        :return: 积木i所在积木列的代表（底部）
        """
        # 使用栈模拟递归过程，避免栈溢出
        stack = []
        # 找到根节点
        while i != self.father[i]:
            stack.append(i)
            i = self.father[i]
        stack.append(i)
        # 从根节点开始，向上更新距离
        for j in range(len(stack) - 2, -1, -1):
            self.father[stack[j]] = i
            # 更新距离：当前积木到积木列底部的距离 = 当前积木到父节点的距离 + 父节点到积木列底部的距离
            self.dist[stack[j]] += self.dist[stack[j + 1]]
        return i
    
    def union(self, x, y):
        """
        合并两个积木列，将包含积木x的积木列整体移动到包含积木y的积木列顶部
        时间复杂度: O(α(n)) 近似O(1)
        
        :param x: 积木x的编号
        :param y: 积木y的编号
        """
        # 查找两个积木所在积木列的代表
        xf = self.find(x)
        yf = self.find(y)
        # 如果不在同一积木列中
        if xf != yf:
            # 将包含积木x的积木列合并到包含积木y的积木列顶部
            self.father[xf] = yf
            # 更新包含积木x的积木列底部到包含积木y的积木列底部的距离
            # 距离 = 包含积木y的积木列的积木数量（即包含积木y的积木列顶部到新积木列底部的距离）
            self.dist[xf] += self.size[yf]
            # 更新新积木列的积木数量
            self.size[yf] += self.size[xf]
            
    def query(self, x):
        """
        查询积木x下方的积木数量
        时间复杂度: O(α(n)) 近似O(1)
        
        :param x: 积木x的编号
        :return: 积木x下方的积木数量
        """
        # 确保路径压缩完成
        self.find(x)
        # 积木x下方的积木数量 = 积木x到积木列底部的距离
        return self.dist[x]

def main():
    n = 30000
    # 初始化带权并查集
    wuf = WeightedUnionFind(n)
    
    # 读取操作数量
    p = int(sys.stdin.readline())
    
    # 处理P个操作
    for _ in range(p):
        line = sys.stdin.readline().strip().split()
        if line[0] == "M":
            # 合并积木列
            x = int(line[1])
            y = int(line[2])
            wuf.union(x, y)
        else:
            # 查询积木下方的积木数量
            x = int(line[1])
            print(wuf.query(x))

if __name__ == "__main__":
    main()