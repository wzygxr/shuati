# 块内最小值查询与更新 - 分块算法实现 (Python版本)
# 题目来源: LeetCode 307. Range Minimum Query (修改版)
# 题目链接: https://leetcode.com/problems/range-sum-query-mutable/
# 题目大意: 维护一个数组，支持单点更新和区间最小值查询
# 约束条件: 数组长度n ≤ 1e5，操作次数m ≤ 1e5

import sys
import math

class BlockMin:
    def __init__(self):
        self.n = 0
        self.m = 0
        self.blen = 0
        self.arr = []
        self.block_min = []
        self.block = []
    
    # 初始化分块结构
    def init(self):
        self.blen = int(math.sqrt(self.n))
        if self.blen == 0:
            self.blen = 1
        
        # 为每个元素分配块
        self.block = [0] * self.n
        for i in range(self.n):
            self.block[i] = i // self.blen
        
        # 计算每个块的最小值
        block_count = (self.n + self.blen - 1) // self.blen
        self.block_min = [0] * block_count
        for i in range(block_count):
            start = i * self.blen
            end = min((i + 1) * self.blen, self.n)
            min_val = float('inf')
            for j in range(start, end):
                if self.arr[j] < min_val:
                    min_val = self.arr[j]
            self.block_min[i] = min_val
    
    # 优化版本的单点更新（只在需要时重新计算块最小值）
    def update_point(self, pos, val):
        old_val = self.arr[pos]
        self.arr[pos] = val
        
        # 只有当新值小于块最小值或者旧值等于块最小值时，才需要重新计算
        block_id = self.block[pos]
        if val < self.block_min[block_id] or old_val == self.block_min[block_id]:
            start = block_id * self.blen
            end = min((block_id + 1) * self.blen, self.n)
            min_val = float('inf')
            for i in range(start, end):
                if self.arr[i] < min_val:
                    min_val = self.arr[i]
            self.block_min[block_id] = min_val
    
    # 区间最小值查询
    def query_min(self, l, r):
        left_block = self.block[l]
        right_block = self.block[r]
        min_val = float('inf')
        
        if left_block == right_block:
            # 所有元素都在同一个块内，直接暴力查询
            for i in range(l, r + 1):
                if self.arr[i] < min_val:
                    min_val = self.arr[i]
        else:
            # 处理左边不完整的块
            for i in range(l, (left_block + 1) * self.blen):
                if self.arr[i] < min_val:
                    min_val = self.arr[i]
            
            # 处理中间完整的块，使用块的最小值
            for i in range(left_block + 1, right_block):
                if self.block_min[i] < min_val:
                    min_val = self.block_min[i]
            
            # 处理右边不完整的块
            for i in range(right_block * self.blen, r + 1):
                if self.arr[i] < min_val:
                    min_val = self.arr[i]
        
        return min_val

def main():
    # 使用sys.stdin.readline提高输入效率
    input = sys.stdin.read().split()
    ptr = 0
    
    solution = BlockMin()
    
    # 读取n
    solution.n = int(input[ptr])
    ptr += 1
    
    # 读取初始数组
    solution.arr = list(map(int, input[ptr:ptr + solution.n]))
    ptr += solution.n
    
    # 初始化分块结构
    solution.init()
    
    # 读取m
    solution.m = int(input[ptr])
    ptr += 1
    
    # 处理每个操作
    output = []
    for i in range(solution.m):
        op = int(input[ptr])
        ptr += 1
        
        if op == 0:
            # 单点更新操作
            a = int(input[ptr]) - 1  # 转换为0-based索引
            ptr += 1
            b = int(input[ptr])
            ptr += 1
            solution.update_point(a, b)
        elif op == 1:
            # 区间查询操作
            a = int(input[ptr]) - 1  # 转换为0-based索引
            ptr += 1
            b = int(input[ptr]) - 1
            ptr += 1
            result = solution.query_min(a, b)
            output.append(str(result))
    
    # 批量输出结果
    print('\n'.join(output))

# 测试用例
# 示例：
# 输入：
# 5 3
# 5 2 3 1 4
# 1 1 5
# 0 3 0
# 1 1 5
# 输出：
# 1
# 0

if __name__ == "__main__":
    main()

'''
时间复杂度分析：
- 初始化：O(n)
- 单点更新（优化版本）：
  - 最好情况：O(1)（当新值不影响块最小值时）
  - 最坏情况：O(√n)（当需要重新计算块最小值时）
  - 平均时间复杂度：O(√n)
- 区间查询：O(√n)
- 对于m次操作，总体时间复杂度：O(m√n)

空间复杂度分析：
- 数组arr：O(n)
- 块最小值数组block_min：O(√n)
- 块分配数组block：O(n)
- 总体空间复杂度：O(n + √n) = O(n)

Python语言特性注意事项：
1. 使用sys.stdin.read()一次性读取所有输入，提高效率
2. 使用列表存储数组和块信息
3. 使用float('inf')表示无穷大
4. 注意Python中的整数除法使用//运算符
5. 对于大规模数据，批量输出结果可以减少I/O操作

算法说明：
块内最小值查询与更新是分块算法的一个典型应用，主要用于处理单点更新和区间最小值查询问题。

算法步骤：
1. 将数组分成大小为√n的块
2. 预处理每个块的最小值，存储在block_min数组中
3. 对于单点更新操作：
   - 更新原始数组中的值
   - 检查是否需要重新计算对应块的最小值
4. 对于区间最小值查询操作：
   - 对于不完整的块，直接遍历块中的元素，找到最小值
   - 对于完整的块，直接使用预处理好的块最小值
   - 综合所有部分的结果，得到最终的最小值

优化说明：
1. 在单点更新时，只有当新值小于块最小值或者旧值等于块最小值时，才需要重新计算块最小值
2. 块的大小选择为√n，平衡了查询和更新的时间复杂度
3. 对于区间查询，利用预处理的块最小值，避免重复计算
4. 在Python中，使用列表一次性存储输出结果，最后统一打印，可以提高I/O效率

与其他方法的对比：
- 暴力法：每次更新O(1)，每次查询O(n)，总时间复杂度O(mn)
- 线段树：每次更新和查询都是O(log n)，但实现复杂
- ST表：查询O(1)，但不支持更新
- 块内最小值：实现简单，时间复杂度适中，支持更新和查询

工程化考虑：
1. 在Python中，对于大规模数据，输入方法的选择对性能影响很大
2. 避免在循环中使用print语句，应该收集结果后批量输出
3. 可以将块的大小作为参数，根据具体数据调整以获得最佳性能
4. 对于非常大的n，需要考虑内存的使用
'''