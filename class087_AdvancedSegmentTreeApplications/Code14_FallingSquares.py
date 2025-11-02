#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
LeetCode 699. Falling Squares

题目链接: https://leetcode.cn/problems/falling-squares/

题目描述:
在二维平面上的 x 轴上，放置着一些方块。给你一个二维整数数组 positions，
其中 positions[i] = [lefti, sideLengthi] 表示：第 i 个方块边长为 sideLengthi，
其左侧边与 x 轴上坐标点 lefti 对齐。每个方块都从一个比目前所有的落地方块更高的高度掉落而下。
方块沿 y 轴负方向下落，直到着陆到另一个正方形的顶边或者是 x 轴上。一旦着陆，它就会固定在原地，无法移动。
在每个方块掉落后，你必须记录目前所有已经落稳的方块堆叠的最高高度。返回一个整数数组 ans，
其中 ans[i] 表示在第 i 块方块掉落后堆叠的最高高度。

解题思路:
使用动态开点线段树维护区间最大值。对于每个掉落的方块：
1. 查询其底部区间的最大高度
2. 计算方块顶部的高度（底部最大高度 + 方块边长）
3. 更新区间为方块顶部高度
4. 记录当前全局最大高度

关键技术:
1. 动态开点线段树：处理大数据范围
2. 区间更新：将区间更新为固定值
3. 区间查询：查询区间最大值

时间复杂度分析:
1. 建树：O(1) - 按需创建
2. 区间更新：O(log n)
3. 区间查询：O(log n)
4. 总体复杂度：O(n log n)
5. 空间复杂度：O(n log n)

是否最优解：是
动态开点线段树是处理此类区间操作问题的最优解法

工程化考量:
1. 动态内存分配：按需创建节点
2. 懒惰标记优化：延迟更新子区间
3. 边界处理：处理节点创建和查询边界情况
"""

class Code14_FallingSquares:
    """
    掉落的方块解决方案类
    """
    
    def __init__(self):
        """
        初始化线段树
        """
        # 使用字典动态存储节点，键为节点索引，值为节点信息
        self.tree = {0: {'max_height': 0, 'lazy': 0, 'left': 0, 'right': 0}}
        self.node_count = 1  # 当前已使用的节点数
        self.MAX_VAL = 10**9  # 最大坐标范围
    
    def push_down(self, node, start, end):
        """
        下推懒惰标记
        
        Args:
            node (int): 当前节点索引
            start (int): 当前区间左端点
            end (int): 当前区间右端点
        """
        if self.tree[node]['lazy'] != 0 and start < end:
            # 确保左右子节点存在
            if self.tree[node]['left'] == 0:
                self.tree[node]['left'] = self.node_count
                self.tree[self.node_count] = {'max_height': 0, 'lazy': 0, 'left': 0, 'right': 0}
                self.node_count += 1
            if self.tree[node]['right'] == 0:
                self.tree[node]['right'] = self.node_count
                self.tree[self.node_count] = {'max_height': 0, 'lazy': 0, 'left': 0, 'right': 0}
                self.node_count += 1
            
            left_node = self.tree[node]['left']
            right_node = self.tree[node]['right']
            
            # 下传标记
            self.tree[left_node]['max_height'] = self.tree[node]['lazy']
            self.tree[left_node]['lazy'] = self.tree[node]['lazy']
            self.tree[right_node]['max_height'] = self.tree[node]['lazy']
            self.tree[right_node]['lazy'] = self.tree[node]['lazy']
            
            # 清除当前节点标记
            self.tree[node]['lazy'] = 0
    
    def update(self, node, start, end, l, r, val):
        """
        区间更新：将 [l, r] 区间内的所有值更新为 val
        
        Args:
            node (int): 当前节点索引
            start (int): 当前区间左端点
            end (int): 当前区间右端点
            l (int): 更新区间左端点
            r (int): 更新区间右端点
            val (int): 更新的值
        """
        if start > r or end < l:
            return
        
        if l <= start and end <= r:
            self.tree[node]['max_height'] = val
            self.tree[node]['lazy'] = val
            return
        
        self.push_down(node, start, end)
        mid = start + (end - start) // 2
        
        left_node = self.tree[node]['left']
        right_node = self.tree[node]['right']
        
        self.update(left_node, start, mid, l, r, val)
        self.update(right_node, mid + 1, end, l, r, val)
        
        # 更新当前节点的最大值
        self.tree[node]['max_height'] = max(
            self.tree[left_node]['max_height'] if left_node != 0 else 0,
            self.tree[right_node]['max_height'] if right_node != 0 else 0
        )
    
    def query(self, node, start, end, l, r):
        """
        查询区间 [l, r] 内的最大值
        
        Args:
            node (int): 当前节点索引
            start (int): 当前区间左端点
            end (int): 当前区间右端点
            l (int): 查询区间左端点
            r (int): 查询区间右端点
            
        Returns:
            int: 区间最大值
        """
        if start > r or end < l:
            return 0  # 不在查询范围内
        
        if l <= start and end <= r:
            return self.tree[node]['max_height']
        
        self.push_down(node, start, end)
        mid = start + (end - start) // 2
        
        left_node = self.tree[node]['left']
        right_node = self.tree[node]['right']
        
        max_val = 0
        if left_node != 0:
            max_val = max(max_val, self.query(left_node, start, mid, l, r))
        if right_node != 0:
            max_val = max(max_val, self.query(right_node, mid + 1, end, l, r))
        
        return max_val
    
    def fallingSquares(self, positions):
        """
        处理掉落的方块，返回每次掉落后的最大高度列表
        
        Args:
            positions (List[List[int]]): 方块的位置列表，每个元素为 [left, side_length]
            
        Returns:
            List[int]: 每次掉落后的最大高度列表
        """
        result = []
        max_height = 0
        
        for pos in positions:
            left = pos[0]
            side_length = pos[1]
            right = left + side_length - 1
            
            # 查询当前区间的最大高度
            current_max = self.query(0, 0, self.MAX_VAL, left, right)
            
            # 计算新方块的顶部高度
            new_height = current_max + side_length
            
            # 更新区间高度
            self.update(0, 0, self.MAX_VAL, left, right, new_height)
            
            # 更新全局最大高度
            max_height = max(max_height, new_height)
            result.append(max_height)
        
        return result

def main():
    """
    主函数，用于测试
    """
    solution = Code14_FallingSquares()
    
    # 测试用例1
    positions1 = [[1, 2], [2, 3], [6, 1]]
    result1 = solution.fallingSquares(positions1)
    print("测试用例1结果:", result1)  # 输出: [2, 5, 5]
    
    # 测试用例2
    positions2 = [[100, 100], [200, 100]]
    result2 = solution.fallingSquares(positions2)
    print("测试用例2结果:", result2)  # 输出: [100, 100]

if __name__ == "__main__":
    main()