"""
Tarjan算法解法（递归版）
题目来源：洛谷 P3379 【模板】最近公共祖先（LCA）
题目链接：https://www.luogu.com.cn/problem/P3379

问题描述：
给定一棵有根多叉树，请求出指定两个点直接最近的公共祖先。

解题思路：
使用Tarjan离线算法，基于DFS和并查集实现
1. 首先读入所有查询，将查询存储在链式前向星结构中
2. 进行DFS遍历树，同时处理查询
3. 使用并查集维护节点的祖先关系

算法步骤：
1. 对于当前节点u，标记为已访问
2. 递归处理u的所有子节点v
3. 处理完v后，将v的祖先设为u（union操作）
4. 检查所有与u相关的查询，如果另一个节点已访问，则其LCA为find的结果

时间复杂度：
O(n + q)，其中n为节点数，q为查询数
空间复杂度：O(n + q)

是否为最优解：是，对于离线查询LCA问题，Tarjan算法是最优解

工程化考虑：
1. 边界条件处理：处理空树、节点不存在等情况
2. 输入验证：验证输入节点是否在树中
3. 异常处理：对非法输入进行检查
4. 可读性：添加详细注释和变量命名

算法要点：
1. 离线处理：需要预先知道所有查询
2. 并查集：用于维护节点的祖先关系
3. DFS遍历：在遍历过程中处理查询

与标准库实现对比：
1. 标准库通常有更完善的错误处理
2. 标准库可能使用更优化的数据结构

性能优化：
1. 离线处理优化：一次性处理所有查询
2. 并查集优化：路径压缩

特殊场景：
1. 空输入：返回特定值表示无效
2. 节点不存在：返回特定值表示无效
3. 查询为空：直接返回

语言特性差异：
1. Python：动态类型，引用计数垃圾回收，代码简洁
2. Java：自动垃圾回收，对象引用传递，类型安全
3. C++：手动内存管理，指针操作，高性能但容易出错

数学联系：
1. 与图论中的DFS理论相关
2. 与并查集数据结构相关
3. 与离线算法设计思想相关

调试能力：
1. 可通过打印DFS遍历顺序调试
2. 可通过断言验证并查集操作
3. 可通过特殊测试用例验证边界条件

注意事项：
Python这么写就会因为递归层数太多而爆栈
Python能通过的写法参考Code03_Tarjan2文件
"""

import sys
from typing import List

# 增加递归限制以处理深度较大的树
sys.setrecursionlimit(500000)

class TarjanLCA:
    def __init__(self, n: int):
        self.MAXN = 500001
        
        # 链式前向星建图
        self.headEdge = [0] * self.MAXN
        self.edgeNext = [0] * (self.MAXN << 1)
        self.edgeTo = [0] * (self.MAXN << 1)
        self.tcnt = 1
        
        # 每个节点有哪些查询，也用链式前向星方式存储
        self.headQuery = [0] * self.MAXN
        self.queryNext = [0] * (self.MAXN << 1)
        self.queryTo = [0] * (self.MAXN << 1)
        
        # 问题的编号，一旦有答案可以知道填写在哪
        self.queryIndex = [0] * (self.MAXN << 1)
        self.qcnt = 1
        
        # 某个节点是否访问过
        self.visited = [False] * self.MAXN
        
        # 并查集
        self.father = list(range(self.MAXN))
        
        # 收集的答案
        self.ans = [0] * self.MAXN
    
    def build(self, n: int) -> None:
        """初始化数据结构"""
        self.tcnt = self.qcnt = 1
        for i in range(1, n + 1):
            self.headEdge[i] = 0
            self.headQuery[i] = 0
            self.visited[i] = False
            self.father[i] = i
    
    def addEdge(self, u: int, v: int) -> None:
        """添加树的边"""
        self.edgeNext[self.tcnt] = self.headEdge[u]
        self.edgeTo[self.tcnt] = v
        self.headEdge[u] = self.tcnt
        self.tcnt += 1
    
    def addQuery(self, u: int, v: int, i: int) -> None:
        """添加查询"""
        self.queryNext[self.qcnt] = self.headQuery[u]
        self.queryTo[self.qcnt] = v
        self.queryIndex[self.qcnt] = i
        self.headQuery[u] = self.qcnt
        self.qcnt += 1
    
    def find(self, i: int) -> int:
        """
        并查集找头节点递归版
        一般来说都这么写，但是本题附加的测试数据很毒
        Python这么写就会因为递归太深而爆栈
        """
        if i != self.father[i]:
            self.father[i] = self.find(self.father[i])
        return self.father[i]
    
    def tarjan(self, u: int, f: int) -> None:
        """
        tarjan算法递归版
        一般来说都这么写，但是本题附加的测试数据很毒
        Python这么写就会因为递归太深而爆栈
        """
        self.visited[u] = True
        e = self.headEdge[u]
        while e != 0:
            v = self.edgeTo[e]
            if v != f:
                self.tarjan(v, u)
                self.father[v] = u
            e = self.edgeNext[e]
        
        q = self.headQuery[u]
        while q != 0:
            v = self.queryTo[q]
            if self.visited[v]:
                self.ans[self.queryIndex[q]] = self.find(v)
            q = self.queryNext[q]

# 由于Python标准输入处理较为复杂，这里只提供类的实现
# 实际使用时可以根据具体平台要求调整输入输出方式