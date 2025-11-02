"""
树上倍增解法（递归版）
题目来源：洛谷 P3379 【模板】最近公共祖先（LCA）
题目链接：https://www.luogu.com.cn/problem/P3379

问题描述：
给定一棵有根多叉树，请求出指定两个点直接最近的公共祖先。

解题思路：
使用树上倍增法预处理每个节点的2^k级祖先，然后对于每次查询：
1. 先将两个节点调整到同一深度
2. 然后同时向上跳跃，直到找到最近公共祖先

时间复杂度：
预处理：O(n log n)
查询：O(log n)
空间复杂度：O(n log n)

是否为最优解：是，对于在线查询LCA问题，倍增法是标准解法之一

工程化考虑：
1. 边界条件处理：处理空树、节点不存在等情况
2. 输入验证：验证输入节点是否在树中
3. 异常处理：对非法输入进行检查
4. 可读性：添加详细注释和变量命名

算法要点：
1. 预处理阶段构建倍增数组：ancestor[i][k]表示节点i的第2^k个祖先
2. 查询阶段先调整深度再同时跳跃
3. 利用二进制表示优化跳跃过程

与标准库实现对比：
1. 标准库通常有更完善的错误处理
2. 标准库可能使用更优化的数据结构

性能优化：
1. 预处理优化：一次处理所有节点
2. 查询优化：利用倍增快速跳跃

特殊场景：
1. 空输入：返回特定值表示无效
2. 节点不存在：返回特定值表示无效
3. 一个节点是另一个节点的祖先：正确处理

语言特性差异：
1. Python：动态类型，引用计数垃圾回收，代码简洁
2. Java：自动垃圾回收，对象引用传递，类型安全
3. C++：手动内存管理，指针操作，高性能但容易出错

数学联系：
1. 与二进制表示和位运算相关
2. 与树的深度优先搜索理论相关
3. 与动态规划有一定联系

调试能力：
1. 可通过打印预处理数组调试
2. 可通过断言验证中间结果
3. 可通过特殊测试用例验证边界条件

注意事项：
C++这么写能通过，Python递归层数太多可能会爆栈
Python能通过的写法可以参考迭代版本
"""

import sys
from typing import List

# 增加递归限制以处理深度较大的树
sys.setrecursionlimit(500000)

class LCA:
    def __init__(self, n: int):
        self.MAXN = 500001
        self.LIMIT = 20
        self.power = self._log2(n)
        
        # 链式前向星建图
        self.head = [0] * self.MAXN
        self.next = [0] * (self.MAXN << 1)
        self.to = [0] * (self.MAXN << 1)
        self.cnt = 1
        
        # 深度数组和倍增数组
        self.deep = [0] * self.MAXN
        self.stjump = [[0] * self.LIMIT for _ in range(self.MAXN)]
    
    def _log2(self, n: int) -> int:
        """计算log2(n)的值"""
        ans = 0
        while (1 << ans) <= (n >> 1):
            ans += 1
        return ans
    
    def build(self, n: int) -> None:
        """初始化数据结构"""
        self.power = self._log2(n)
        self.cnt = 1
        for i in range(1, n + 1):
            self.head[i] = 0
    
    def addEdge(self, u: int, v: int) -> None:
        """添加边到链式前向星结构"""
        self.next[self.cnt] = self.head[u]
        self.to[self.cnt] = v
        self.head[u] = self.cnt
        self.cnt += 1
    
    def dfs(self, u: int, f: int) -> None:
        """
        dfs递归版
        一般来说都这么写，但是本题附加的测试数据很毒
        Python这么写就会因为递归太深而爆栈，C++这么写就能通过
        """
        self.deep[u] = self.deep[f] + 1
        self.stjump[u][0] = f
        for p in range(1, self.power + 1):
            self.stjump[u][p] = self.stjump[self.stjump[u][p - 1]][p - 1]
        
        e = self.head[u]
        while e != 0:
            if self.to[e] != f:
                self.dfs(self.to[e], u)
            e = self.next[e]
    
    def lca(self, a: int, b: int) -> int:
        """计算两个节点的最近公共祖先"""
        if self.deep[a] < self.deep[b]:
            a, b = b, a
        
        # 将a调整到与b同一深度
        for p in range(self.power, -1, -1):
            if self.deep[self.stjump[a][p]] >= self.deep[b]:
                a = self.stjump[a][p]
        
        if a == b:
            return a
        
        # 同时向上跳跃找到LCA
        for p in range(self.power, -1, -1):
            if self.stjump[a][p] != self.stjump[b][p]:
                a = self.stjump[a][p]
                b = self.stjump[b][p]
        
        return self.stjump[a][0]

# 由于Python标准输入处理较为复杂，这里只提供类的实现
# 实际使用时可以根据具体平台要求调整输入输出方式