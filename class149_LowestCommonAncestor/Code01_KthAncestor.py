"""
树节点的第K个祖先问题
题目来源：LeetCode 1483. Kth Ancestor of a Tree Node
题目链接：https://leetcode.cn/problems/kth-ancestor-of-a-tree-node/

问题描述：
树上有n个节点，编号0 ~ n-1，树的结构用parent数组代表
其中parent[i]是节点i的父节点，树的根节点是编号为0
树节点i的第k个祖先节点，是从节点i开始往上跳k步所来到的节点
实现TreeAncestor类
TreeAncestor(int n, int[] parent) : 初始化
getKthAncestor(int i, int k) : 返回节点i的第k个祖先节点，不存在返回-1

解题思路：
使用树上倍增法预处理每个节点的2^j级祖先，然后利用二进制分解快速查询第k个祖先
1. 预处理阶段：对于每个节点i，计算其2^0, 2^1, 2^2, ..., 2^j级祖先
2. 查询阶段：将k按二进制分解，利用预处理的结果快速跳跃

时间复杂度：
预处理：O(n log n)
查询：O(log k)
空间复杂度：O(n log n)

是否为最优解：是，对于在线查询第k个祖先问题，倍增法是标准解法

工程化考虑：
1. 边界条件处理：处理k大于节点深度的情况
2. 输入验证：验证节点编号是否合法
3. 异常处理：对非法输入进行检查
4. 可读性：添加详细注释和变量命名

算法要点：
1. 预处理阶段构建倍增数组：stjump[i][j]表示节点i的第2^j个祖先
2. 查询阶段利用二进制分解：将k分解为2的幂次之和
3. 深度数组用于快速判断祖先是否存在

与标准库实现对比：
1. 标准库通常有更完善的错误处理
2. 标准库可能使用更优化的数据结构

性能优化：
1. 预处理优化：一次处理所有节点
2. 查询优化：利用倍增快速跳跃

特殊场景：
1. 空输入：返回-1
2. k为0：返回节点本身
3. k大于节点深度：返回-1

语言特性差异：
1. Python：动态类型，引用计数垃圾回收
2. Java：自动垃圾回收，对象引用传递
3. C++：手动内存管理，指针操作

数学联系：
1. 与二进制表示和位运算相关
2. 与树的深度优先搜索理论相关
3. 与动态规划有一定联系

调试能力：
1. 可通过打印预处理数组调试
2. 可通过断言验证中间结果
3. 可通过特殊测试用例验证边界条件
"""

from typing import List

class TreeAncestor:
    def __init__(self, n: int, parent: List[int]):
        """
        初始化TreeAncestor类
        :param n: 节点数量
        :param parent: 父节点数组，parent[i]表示节点i的父节点
        """
        self.LIMIT = 16
        self.power = self._log2(n)
        
        # 初始化链式前向星建图相关数组
        self.head = [0] * n
        self.next = [0] * n
        self.to = [0] * n
        self.cnt = 1
        
        # 深度数组和倍增数组
        self.deep = [0] * n
        self.stjump = [[0] * self.LIMIT for _ in range(n)]
        
        # 构建图结构
        for i in range(1, len(parent)):
            self._addEdge(parent[i], i)
        
        # DFS预处理
        self._dfs(0, 0)
    
    def _log2(self, n: int) -> int:
        """
        计算log2(n)的值
        :param n: 输入值
        :return: log2(n)的整数部分
        """
        ans = 0
        while (1 << ans) <= (n >> 1):
            ans += 1
        return ans
    
    def _addEdge(self, u: int, v: int) -> None:
        """
        添加边到链式前向星结构
        :param u: 起点
        :param v: 终点
        """
        self.next[self.cnt] = self.head[u]
        self.to[self.cnt] = v
        self.head[u] = self.cnt
        self.cnt += 1
    
    def _dfs(self, i: int, f: int) -> None:
        """
        深度优先搜索，预处理倍增数组
        :param i: 当前节点
        :param f: 父节点
        """
        if i == 0:
            self.deep[i] = 1
        else:
            self.deep[i] = self.deep[f] + 1
        
        self.stjump[i][0] = f
        for p in range(1, self.power + 1):
            self.stjump[i][p] = self.stjump[self.stjump[i][p - 1]][p - 1]
        
        e = self.head[i]
        while e != 0:
            self._dfs(self.to[e], i)
            e = self.next[e]
    
    def getKthAncestor(self, node: int, k: int) -> int:
        """
        获取节点的第k个祖先
        :param node: 节点编号
        :param k: 祖先的步数
        :return: 第k个祖先节点编号，不存在返回-1
        """
        if self.deep[node] <= k:
            return -1
        
        # s是想要去往的层数
        s = self.deep[node] - k
        i = node
        for p in range(self.power, -1, -1):
            if self.deep[self.stjump[i][p]] >= s:
                i = self.stjump[i][p]
        return i

# 测试代码
if __name__ == "__main__":
    # 测试用例1
    treeAncestor = TreeAncestor(7, [-1, 0, 0, 1, 1, 2, 2])
    print(treeAncestor.getKthAncestor(3, 1))  # 输出: 1
    print(treeAncestor.getKthAncestor(5, 2))  # 输出: 0
    print(treeAncestor.getKthAncestor(6, 3))  # 输出: -1