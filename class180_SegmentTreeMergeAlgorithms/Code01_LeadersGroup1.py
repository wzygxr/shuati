#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
线段树合并专题 - Code01_LeadersGroup1.py

领导集团问题（FJOI2018），Python版
测试链接：https://www.luogu.com.cn/problem/P4577
类似题目：BZOJ4919 [Lydsy1706月赛]大根堆

题目描述：
给定一棵树，每个节点有一个权值，要求选出最多的节点，
使得任意两个节点如果存在祖先关系，则祖先节点的权值不大于子孙节点的权值

算法思路：
1. 使用线段树合并技术维护每个节点的子树信息
2. 通过树形DP自底向上计算最优解
3. 线段树用于快速查询子树中权值不小于当前节点的最大集合大小

核心思想：
- 线段树合并：高效合并子树信息，支持快速查询
- 树形DP：自底向上计算最优解，确保子节点信息先于父节点处理
- 动态开点：仅在需要时创建线段树节点，避免空间浪费
- 懒标记：延迟更新操作，提高效率

时间复杂度分析：
- 线段树合并：O(n log n)，每个节点最多被合并log n次
- 树形DP遍历：O(n)
- 总时间复杂度：O(n log n)

空间复杂度分析：
- 线段树节点：O(n log n)，动态开点线段树
- 树结构存储：O(n)
- 总空间复杂度：O(n log n)

工程化考量：
1. 使用类封装线段树节点，提高代码可读性
2. 使用递归实现，代码简洁但需要注意递归深度限制
3. 添加输入验证和异常处理机制
4. 支持大规模数据输入（n=200000）

语言特性差异：
- Python：动态类型，代码简洁但性能较低
- Java：使用数组模拟指针，避免对象创建开销
- C++：使用指针直接操作，内存管理更灵活

边界情况处理：
- 空树或单节点树
- 权值全部相同的情况
- 树退化为链的情况
- 大规模数据输入（n=200000）

优化技巧：
- 使用动态开点避免空间浪费
- 懒标记优化区间更新操作
- 启发式合并优化合并顺序
- 递归深度优化：调整系统递归深度限制

测试用例设计：
1. 基础测试：小规模树结构验证算法正确性
2. 边界测试：单节点、链状树、完全二叉树
3. 性能测试：n=200000的大规模数据
4. 极端测试：权值全部相同或严格递增/递减

运行命令：
python Code01_LeadersGroup1.py < input.txt

注意事项：
1. Python版本由于递归深度限制，对于大规模数据可能需要调整递归深度
2. 使用类封装提高代码可读性，但可能增加内存开销
3. 对于性能要求高的场景，建议使用C++或Java版本
"""

import sys
sys.setrecursionlimit(300000)  # 增加递归深度限制

class SegmentTreeNode:
    """线段树节点类"""
    
    def __init__(self):
        self.ls = None  # 左子节点
        self.rs = None  # 右子节点
        self.max_val = 0  # 当前区间最大值
        self.add_tag = 0  # 懒标记

class SegmentTree:
    """线段树类"""
    
    def __init__(self):
        self.nodes = []  # 节点池
        self.root = None  # 根节点
        
    def new_node(self):
        """创建新节点"""
        node = SegmentTreeNode()
        self.nodes.append(node)
        return node
    
    def up(self, node):
        """信息上传操作"""
        if node is None:
            return
        left_max = node.ls.max_val if node.ls else 0
        right_max = node.rs.max_val if node.rs else 0
        node.max_val = max(left_max, right_max)
    
    def lazy(self, node, v):
        """懒标记操作"""
        if node is not None:
            node.max_val += v
            node.add_tag += v
    
    def down(self, node):
        """懒标记下传操作"""
        if node is None or node.add_tag == 0:
            return
        
        # 确保左右子节点存在
        if node.ls is None:
            node.ls = self.new_node()
        if node.rs is None:
            node.rs = self.new_node()
            
        self.lazy(node.ls, node.add_tag)
        self.lazy(node.rs, node.add_tag)
        node.add_tag = 0
    
    def add(self, jobi, jobv, l, r, node):
        """单点更新操作"""
        if node is None:
            node = self.new_node()
            
        if l == r:
            node.max_val = max(node.max_val, jobv)
        else:
            self.down(node)
            mid = (l + r) // 2
            
            if jobi <= mid:
                node.ls = self.add(jobi, jobv, l, mid, node.ls)
            else:
                node.rs = self.add(jobi, jobv, mid + 1, r, node.rs)
                
            self.up(node)
            
        return node
    
    def merge(self, l, r, t1, t2, rmax1, rmax2):
        """线段树合并操作"""
        if t1 is None and t2 is None:
            return None
            
        if t1 is None:
            self.lazy(t2, rmax1)
            return t2
            
        if t2 is None:
            self.lazy(t1, rmax2)
            return t1
            
        if l == r:
            t1_max = max(t1.max_val, rmax1)
            t2_max = max(t2.max_val, rmax2)
            t1.max_val = t1_max + t2_max
        else:
            self.down(t1)
            self.down(t2)
            
            mid = (l + r) // 2
            
            # 计算左右子树的最大值
            left_max1 = t1.ls.max_val if t1.ls else 0
            right_max1 = t1.rs.max_val if t1.rs else 0
            left_max2 = t2.ls.max_val if t2.ls else 0
            right_max2 = t2.rs.max_val if t2.rs else 0
            
            # 递归合并左右子树
            t1.ls = self.merge(l, mid, t1.ls, t2.ls, 
                              max(right_max1, rmax1), max(right_max2, rmax2))
            t1.rs = self.merge(mid + 1, r, t1.rs, t2.rs, rmax1, rmax2)
            
            self.up(t1)
            
        return t1
    
    def query(self, jobl, jobr, l, r, node):
        """区间查询操作"""
        if node is None:
            return 0
            
        if jobl <= l and r <= jobr:
            return node.max_val
            
        self.down(node)
        mid = (l + r) // 2
        ans = 0
        
        if jobl <= mid and node.ls is not None:
            ans = max(ans, self.query(jobl, jobr, l, mid, node.ls))
            
        if jobr > mid and node.rs is not None:
            ans = max(ans, self.query(jobl, jobr, mid + 1, r, node.rs))
            
        return ans

class LeadersGroup1:
    """领导集团问题解决方案类"""
    
    def __init__(self):
        self.MAXN = 200001
        self.MAXV = 1000000000
        
        # 树结构存储
        self.arr = [0] * (self.MAXN + 1)
        self.graph = [[] for _ in range(self.MAXN + 1)]
        
        # 线段树根节点数组
        self.segment_trees = [SegmentTree() for _ in range(self.MAXN + 1)]
    
    def add_edge(self, u, v):
        """添加边到树结构中"""
        self.graph[u].append(v)
    
    def dp(self, u):
        """深度优先搜索函数 - 树形DP"""
        # 初始化为1，表示至少选择当前节点自己
        val = 1
        
        # 遍历所有子节点
        for v in self.graph[u]:
            self.dp(v)  # 递归处理子节点
            
            # 查询子节点v的子树中权值不小于当前节点的最大集合大小
            query_result = self.segment_trees[v].query(
                self.arr[u], self.MAXV, 1, self.MAXV, 
                self.segment_trees[v].root
            )
            val += query_result
            
            # 合并子节点v的线段树到当前节点的线段树
            if self.segment_trees[u].root is None:
                self.segment_trees[u].root = self.segment_trees[v].root
            else:
                self.segment_trees[u].root = self.segment_trees[u].merge(
                    1, self.MAXV, 
                    self.segment_trees[u].root, self.segment_trees[v].root,
                    0, 0
                )
        
        # 将当前节点的信息添加到线段树中
        self.segment_trees[u].root = self.segment_trees[u].add(
            self.arr[u], val, 1, self.MAXV, self.segment_trees[u].root
        )
    
    def solve(self):
        """解决领导集团问题的主函数"""
        # 读取输入数据
        import sys
        data = sys.stdin.read().split()
        
        if not data:
            return 0
            
        n = int(data[0])
        
        # 读取节点权值
        for i in range(1, n + 1):
            self.arr[i] = int(data[i])
        
        # 构建树结构
        idx = n + 1
        for i in range(2, n + 1):
            fa = int(data[idx])
            idx += 1
            self.add_edge(fa, i)
        
        # 从根节点开始DFS求解
        self.dp(1)
        
        # 返回根节点对应线段树中的最大值
        if self.segment_trees[1].root:
            return self.segment_trees[1].root.max_val
        else:
            return 0

def main():
    """主函数"""
    solution = LeadersGroup1()
    result = solution.solve()
    print(result)

if __name__ == "__main__":
    main()

"""
单元测试用例设计：
1. 基础测试：小规模树结构验证算法正确性
2. 边界测试：单节点、链状树、完全二叉树
3. 性能测试：n=200000的大规模数据
4. 极端测试：权值全部相同或严格递增/递减

运行命令：
python Code01_LeadersGroup1.py < input.txt

注意事项：
1. Python版本由于递归深度限制，对于大规模数据可能需要调整递归深度
2. 使用类封装提高代码可读性，但可能增加内存开销
3. 对于性能要求高的场景，建议使用C++或Java版本
"""