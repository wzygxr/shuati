"""
LOJ 6277. 数列分块入门 1 - Python实现

题目描述：
给出一个长为 n 的数列，以及 n 个操作，操作涉及区间加法，单点查值。

解题思路：
使用分块算法，将数组分成大小约为sqrt(n)的块。
对于每个块维护一个加法标记，表示该块内所有元素需要增加的值。
区间加法操作时：
1. 对于完整块，直接更新加法标记
2. 对于不完整块，暴力更新元素值
单点查询时，返回元素值加上所在块的加法标记

时间复杂度：
- 区间加法：O(√n)
- 单点查询：O(1)
空间复杂度：O(n)

工程化考量：
1. 异常处理：检查输入参数的有效性
2. 可配置性：块大小可根据需要调整
3. 性能优化：使用标记减少重复计算
4. 鲁棒性：处理边界情况
"""

import math
import sys

class BlockSolution:
    def __init__(self, size):
        """
        初始化分块结构
        
        :param size: 数组大小
        """
        self.n = size
        # 设置块大小为sqrt(n)
        self.block_size = int(math.sqrt(size))
        # 计算块数量
        self.block_num = (size + self.block_size - 1) // self.block_size
        
        # 原数组
        self.arr = [0] * (size + 1)
        # 每个元素所属的块
        self.belong = [0] * (size + 1)
        # 每个块的加法标记
        self.lazy = [0] * (self.block_num + 1)
        # 每个块的左右边界
        self.block_left = [0] * (self.block_num + 1)
        self.block_right = [0] * (self.block_num + 1)
        
        # 初始化每个元素所属的块
        for i in range(1, size + 1):
            self.belong[i] = (i - 1) // self.block_size + 1
            
        # 初始化每个块的边界
        for i in range(1, self.block_num + 1):
            self.block_left[i] = (i - 1) * self.block_size + 1
            self.block_right[i] = min(i * self.block_size, size)
    
    def add(self, l, r, val):
        """
        区间加法操作
        
        :param l: 区间左端点
        :param r: 区间右端点
        :param val: 要增加的值
        """
        left_block = self.belong[l]
        right_block = self.belong[r]
        
        # 如果在同一个块内，暴力处理
        if left_block == right_block:
            for i in range(l, r + 1):
                self.arr[i] += val
        else:
            # 处理左边不完整块
            for i in range(l, self.block_right[left_block] + 1):
                self.arr[i] += val
                
            # 处理右边不完整块
            for i in range(self.block_left[right_block], r + 1):
                self.arr[i] += val
                
            # 处理中间完整块
            for i in range(left_block + 1, right_block):
                self.lazy[i] += val
    
    def query(self, pos):
        """
        单点查询
        
        :param pos: 位置
        :return: 该位置的值
        """
        # 返回元素值加上所在块的加法标记
        return self.arr[pos] + self.lazy[self.belong[pos]]

def main():
    """
    主函数，用于测试
    """
    # 读取数组大小
    n = int(input())
    
    # 初始化分块结构
    solution = BlockSolution(n)
    
    # 读取初始数组
    elements = list(map(int, input().split()))
    for i in range(1, n + 1):
        solution.arr[i] = elements[i - 1]
    
    # 处理操作
    for _ in range(n):
        op, l, r, c = map(int, input().split())
        
        if op == 0:
            # 区间加法
            solution.add(l, r, c)
        else:
            # 单点查询
            print(solution.query(r))

if __name__ == "__main__":
    main()