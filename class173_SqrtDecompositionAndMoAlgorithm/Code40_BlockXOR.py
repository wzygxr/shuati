# 基于块分解的子数组信息预处理 - 区间异或和查询 (Python版本)
# 题目来源: Codeforces 914C
# 题目链接: https://codeforces.com/problemset/problem/914/C
# 题目大意: 维护一个数组，支持区间异或和查询
# 约束条件: 数组长度n ≤ 1e5，查询次数m ≤ 1e5

import sys
import math

class BlockXOR:
    def __init__(self):
        self.n = 0
        self.m = 0
        self.blen = 0
        self.block_count = 0
        self.arr = []
        self.block = []
        self.block_xor = []
        self.pre_xor = []
    
    # 初始化分块结构
    def init(self):
        self.blen = int(math.sqrt(self.n))
        if self.blen == 0:
            self.blen = 1
        self.block_count = (self.n + self.blen - 1) // self.blen
        
        # 为每个元素分配块
        self.block = [0] * self.n
        for i in range(self.n):
            self.block[i] = i // self.blen
        
        # 计算每个块的异或和
        self.block_xor = [0] * self.block_count
        for i in range(self.block_count):
            start = i * self.blen
            end = min((i + 1) * self.blen, self.n)
            xor_sum = 0
            for j in range(start, end):
                xor_sum ^= self.arr[j]
            self.block_xor[i] = xor_sum
        
        # 预处理块间的异或和
        self.pre_xor = [[0] * self.block_count for _ in range(self.block_count)]
        for i in range(self.block_count):
            current_xor = 0
            for j in range(i, self.block_count):
                current_xor ^= self.block_xor[j]
                self.pre_xor[i][j] = current_xor
    
    # 区间异或和查询
    def query_xor(self, l, r):
        left_block = self.block[l]
        right_block = self.block[r]
        xor_sum = 0
        
        if left_block == right_block:
            # 所有元素都在同一个块内，直接暴力查询
            for i in range(l, r + 1):
                xor_sum ^= self.arr[i]
        else:
            # 处理左边不完整的块
            for i in range(l, (left_block + 1) * self.blen):
                xor_sum ^= self.arr[i]
            
            # 处理中间完整的块，使用预处理的块间异或和
            if left_block + 1 <= right_block - 1:
                xor_sum ^= self.pre_xor[left_block + 1][right_block - 1]
            
            # 处理右边不完整的块
            for i in range(right_block * self.blen, r + 1):
                xor_sum ^= self.arr[i]
        
        return xor_sum
    
    # 单点更新
    def update_point(self, pos, val):
        old_val = self.arr[pos]
        self.arr[pos] = val
        
        # 更新对应块的异或和
        block_id = self.block[pos]
        start = block_id * self.blen
        end = min((block_id + 1) * self.blen, self.n)
        xor_sum = 0
        for i in range(start, end):
            xor_sum ^= self.arr[i]
        self.block_xor[block_id] = xor_sum
        
        # 重新计算受影响的预处理块间异或和
        for i in range(self.block_count):
            if i > block_id:
                break
            current_xor = 0
            for j in range(i, self.block_count):
                current_xor ^= self.block_xor[j]
                self.pre_xor[i][j] = current_xor

def main():
    # 使用sys.stdin.readline提高输入效率
    input = sys.stdin.read().split()
    ptr = 0
    
    solution = BlockXOR()
    
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
            result = solution.query_xor(a, b)
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
# 5^2^3^1^4 = 5
# 5^2^0^1^4 = 6

if __name__ == "__main__":
    main()

'''
时间复杂度分析：
- 初始化：
  - 计算块异或和：O(n)
  - 预处理块间异或和：O(n√n)
- 单点更新：
  - 重新计算块异或和：O(√n)
  - 重新计算预处理块间异或和：O(n)
  - 总体时间复杂度：O(n)
- 区间查询：
  - 不完整块的处理：O(√n)
  - 完整块的处理：O(1)（使用预处理的块间异或和）
  - 总体时间复杂度：O(√n)

空间复杂度分析：
- 数组arr：O(n)
- 块分配数组block：O(n)
- 块异或和数组block_xor：O(√n)
- 预处理块间异或和数组pre_xor：O(n)
- 总体空间复杂度：O(n)

Python语言特性注意事项：
1. 使用sys.stdin.read()一次性读取所有输入，提高效率
2. 使用二维列表存储预处理的块间异或和
3. 注意Python中的整数除法使用//运算符
4. 对于大规模数据，批量输出结果可以减少I/O操作

算法说明：
基于块分解的子数组信息预处理是分块算法的一个重要应用，特别适用于区间异或和查询等问题。

算法步骤：
1. 将数组分成大小为√n的块
2. 预处理每个块的异或和
3. 预处理块间的异或和，pre_xor[i][j]表示从第i个块到第j个块的异或和
4. 对于区间异或和查询操作：
   - 对于不完整的块，直接遍历块中的元素，计算异或和
   - 对于完整的块，使用预处理的块间异或和
   - 综合所有部分的结果，得到最终的异或和
5. 对于单点更新操作：
   - 更新原始数组中的值
   - 重新计算对应块的异或和
   - 重新计算受影响的预处理块间异或和

优化说明：
1. 使用预处理的块间异或和，减少查询时的计算量
2. 块的大小选择为√n，平衡了查询和更新的时间复杂度
3. 在更新时，只重新计算受影响的预处理块间异或和

与其他方法的对比：
- 暴力法：每次查询O(n)，每次更新O(1)，总时间复杂度O(mn)
- 前缀异或数组：每次查询O(1)，但更新需要O(n)时间
- 线段树：每次更新和查询都是O(log n)，但实现复杂
- 基于块分解的预处理：查询O(√n)，更新O(n)，实现相对简单

工程化考虑：
1. 在Python中，对于大规模数据，输入方法的选择对性能影响很大
2. 避免在循环中使用print语句，应该收集结果后批量输出
3. 可以将块的大小作为参数，根据具体数据调整以获得最佳性能
4. 对于非常大的n，需要考虑内存的使用和Python的性能限制
'''