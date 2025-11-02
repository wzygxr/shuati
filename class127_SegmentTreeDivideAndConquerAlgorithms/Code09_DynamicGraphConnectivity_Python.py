"""
动态图连通性问题 - 线段树分治 + 可撤销并查集实现 (Python版本)

题目来源：LeetCode Dynamic Graph Connectivity
题目链接：https://leetcode.com/problems/dynamic-graph-connectivity/

问题描述：
支持动态加边、删边操作，查询两点间连通性

算法思路：
1. 使用线段树分治处理动态加边/删边操作
2. 通过可撤销并查集维护节点间的连通性
3. 离线处理所有操作，把每条边的存在时间区间分解到线段树的节点上
4. 通过DFS遍历线段树，处理每个时间点的查询

时间复杂度：O((n + m) log m)
空间复杂度：O(n + m)
"""

import sys
from typing import List

# 增加递归深度限制，防止栈溢出
sys.setrecursionlimit(1 << 25)

class Solution:
    def __init__(self):
        # 常量定义
        self.MAXN = 100001   # 最大节点数
        self.MAXT = 500001   # 最大线段树任务数
        
        # 全局变量
        self.n = 0  # 节点数
        self.m = 0  # 操作数
        
        # 事件数组：记录所有边的添加和删除事件
        # event[i][0]: 边的左端点x
        # event[i][1]: 边的右端点y
        # event[i][2]: 事件发生的时间点t
        self.event = [[0, 0, 0] for _ in range(self.MAXN << 1)]
        self.eventCnt = 0  # 事件计数器
        
        # 记录每个时间点的操作信息
        self.op = [0] * self.MAXN   # 操作类型：1(添加边)、2(删除边)、3(查询)
        self.x = [0] * self.MAXN    # 操作涉及的第一个节点
        self.y = [0] * self.MAXN    # 操作涉及的第二个节点
        
        # 可撤销并查集：维护连通性
        self.father = [0] * self.MAXN     # 父节点数组
        self.siz = [0] * self.MAXN        # 集合大小数组
        self.rollback = [[0, 0] for _ in range(self.MAXN)]  # 回滚栈，记录合并操作
        self.opsize = 0                   # 操作计数
        
        # 时间轴线段树上的区间任务列表：链式前向星结构
        self.head = [0] * (self.MAXN << 2)  # 线段树节点的头指针
        self.next_ = [0] * self.MAXT        # 下一个任务的指针
        self.tox = [0] * self.MAXT          # 任务边的起点
        self.toy = [0] * self.MAXT          # 任务边的终点
        self.cnt = 0                        # 任务计数
        
        # 存储查询操作的答案
        self.ans = [False] * self.MAXN

    def find(self, i: int) -> int:
        """
        并查集的find操作：查找集合代表元素
        @param i 要查找的节点
        @return 节点所在集合的代表元素（根节点）
        @note 注意：此实现没有路径压缩，以支持撤销操作
        """
        # 非路径压缩版本，以支持撤销操作
        while i != self.father[i]:
            i = self.father[i]
        return i

    def union(self, u: int, v: int) -> bool:
        """
        可撤销并查集的合并操作，在节点u和v之间添加一条边
        @param u 第一个节点
        @param v 第二个节点
        @return 如果合并了两个不同的集合，返回true；否则返回false
        """
        # 查找u和v的根节点
        fu = self.find(u)
        fv = self.find(v)
        
        if fu == fv:
            return False  # 没有合并新的集合
        
        # 按秩合并，始终将较小的树合并到较大的树中
        if self.siz[fu] < self.siz[fv]:
            fu, fv = fv, fu
        
        # 合并操作
        self.father[fv] = fu
        self.siz[fu] += self.siz[fv]
        
        # 记录操作，用于撤销
        self.opsize += 1
        self.rollback[self.opsize][0] = fu
        self.rollback[self.opsize][1] = fv
        
        return True  # 成功合并两个集合

    def undo(self) -> None:
        """
        撤销最近的一次合并操作
        """
        # 获取最后一次合并操作的信息
        fx = self.rollback[self.opsize][0]  # 父节点
        fy = self.rollback[self.opsize][1]  # 子节点
        self.opsize -= 1
        
        # 恢复fy的父节点为自己
        self.father[fy] = fy
        # 恢复父节点集合的大小
        self.siz[fx] -= self.siz[fy]

    def addEdge(self, i: int, x: int, y: int) -> None:
        """
        给线段树节点i添加一个任务：在节点x和y之间添加边
        @param i 线段树节点编号
        @param x 边的起点
        @param y 边的终点
        """
        # 创建新任务
        self.cnt += 1
        self.next_[self.cnt] = self.head[i]  # 指向前一个任务
        self.tox[self.cnt] = x               # 边的起点
        self.toy[self.cnt] = y               # 边的终点
        self.head[i] = self.cnt              # 更新头指针

    def add(self, jobl: int, jobr: int, jobx: int, joby: int, l: int, r: int, i: int) -> None:
        """
        线段树区间更新：将边(jobx, joby)添加到时间区间[jobl, jobr]内
        @param jobl 任务开始时间
        @param jobr 任务结束时间
        @param jobx 边的起点
        @param joby 边的终点
        @param l 当前线段树节点的左区间
        @param r 当前线段树节点的右区间
        @param i 当前线段树节点编号
        """
        # 如果当前区间完全包含在目标区间内，直接添加到当前节点
        if jobl <= l and r <= jobr:
            self.addEdge(i, jobx, joby)
        else:
            # 否则递归到左右子树
            mid = (l + r) >> 1
            if jobl <= mid:
                self.add(jobl, jobr, jobx, joby, l, mid, i << 1)
            if jobr > mid:
                self.add(jobl, jobr, jobx, joby, mid + 1, r, i << 1 | 1)

    def dfs(self, l: int, r: int, i: int) -> None:
        """
        线段树分治的深度优先搜索核心方法
        
        @param l 当前线段树节点的左时间区间边界
        @param r 当前线段树节点的右时间区间边界
        @param i 当前线段树节点编号（根节点为1，左子节点为2*i，右子节点为2*i+1）
        """
        # 记录合并操作的数量，用于后续撤销
        unionCnt = 0
        
        # 处理当前节点上的所有边
        # 这些边在[l, r]时间区间内都是活跃的
        e = self.head[i]
        while e > 0:
            # 尝试合并两个集合
            # 如果成功合并（两个不同的集合），增加计数
            if self.union(self.tox[e], self.toy[e]):
                unionCnt += 1
            e = self.next_[e]
        
        # 处理叶子节点（对应具体的时间点）
        if l == r:
            # 如果当前时间点是查询操作（类型3）
            if self.op[l] == 3:
                # 检查x[l]和y[l]是否连通
                self.ans[l] = (self.find(self.x[l]) == self.find(self.y[l]))
        else:
            # 非叶子节点，递归处理左右子树
            mid = (l + r) >> 1  # 计算中间点
            self.dfs(l, mid, i << 1)      # 处理左子区间
            self.dfs(mid + 1, r, i << 1 | 1)  # 处理右子区间
        
        # 回溯：撤销所有合并操作，按逆序撤销
        for k in range(1, unionCnt + 1):
            self.undo()  # 撤销并查集的合并操作

    def prepare(self) -> None:
        """
        预处理函数：初始化并查集、排序事件、构建线段树
        """
        # 初始化并查集结构
        # 每个节点初始时都是独立的集合，父节点指向自己，集合大小为1
        for i in range(1, self.n + 1):
            self.father[i] = i  # 每个节点初始是自己的父节点
            self.siz[i] = 1     # 每个集合初始大小为1
        
        # 按边的两个端点和时间排序事件
        self.event[1:self.eventCnt + 1] = sorted(
            self.event[1:self.eventCnt + 1],
            key=lambda x: (x[0], x[1], x[2])
        )
        
        # 处理每条边的生命周期，确定边的有效时间段
        l = 1
        while l <= self.eventCnt:
            r = l
            x_val = self.event[l][0]  # 当前处理的边的起点
            y_val = self.event[l][1]  # 当前处理的边的终点
            
            # 找到所有相同边(x,y)的事件
            while r + 1 <= self.eventCnt and self.event[r + 1][0] == x_val and self.event[r + 1][1] == y_val:
                r += 1
            
            # 处理每对添加和删除事件，确定边的有效时间区间
            i = l
            while i <= r:
                start = self.event[i][2]     # 边开始的时间点（添加事件的时间）
                
                # 确定边结束的时间点
                end = self.event[i + 1][2] - 1 if (i + 1 <= r) else self.m
                
                # 将边添加到线段树的相应时间区间[start, end]
                self.add(start, end, x_val, y_val, 1, self.m, 1)
                i += 2
            
            l = r + 1

    def dynamic_graph_connectivity(self, n: int, operations: List[List[int]]) -> List[bool]:
        """
        动态图连通性问题主函数
        
        @param n 节点数
        @param operations 操作列表，每个操作为[op, x, y]的形式
        @return 查询操作的结果列表
        """
        # 初始化
        self.n = n
        self.m = len(operations)
        
        # 读取每个操作
        for i in range(1, self.m + 1):
            self.op[i] = operations[i - 1][0]   # 操作类型
            self.x[i] = operations[i - 1][1]    # 操作涉及的第一个节点
            self.y[i] = operations[i - 1][2]    # 操作涉及的第二个节点
            
            # 对于添加和删除操作，记录事件信息
            if self.op[i] != 3:
                self.eventCnt += 1
                self.event[self.eventCnt][0] = self.x[i]   # 边的起点
                self.event[self.eventCnt][1] = self.y[i]   # 边的终点
                self.event[self.eventCnt][2] = i           # 事件发生的时间点
        
        # 预处理阶段：初始化并查集，排序事件，构建线段树
        self.prepare()
        
        # 执行线段树分治的核心算法
        self.dfs(1, self.m, 1)
        
        # 收集所有查询操作的答案
        result = []
        for i in range(1, self.m + 1):
            if self.op[i] == 3:
                result.append(self.ans[i])
        
        return result

# 测试代码
if __name__ == "__main__":
    solution = Solution()
    
    # 测试用例
    n = 5
    operations = [
        [1, 0, 1],  # 添加边(0,1)
        [1, 1, 2],  # 添加边(1,2)
        [3, 0, 2],  # 查询0和2是否连通
        [2, 1, 2],  # 删除边(1,2)
        [3, 0, 2]   # 查询0和2是否连通
    ]
    
    result = solution.dynamic_graph_connectivity(n, operations)
    print(result)  # 应该输出 [True, False]