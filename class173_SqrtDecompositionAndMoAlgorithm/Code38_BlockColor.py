# 块颜色标记 - 分块算法实现 (Python版本)
# 题目来源: 洛谷 P3203
# 题目链接: https://www.luogu.com.cn/problem/P3203
# 题目大意: 维护一个序列，支持区间颜色修改和单点查询颜色
# 约束条件: 数组长度n ≤ 1e5，操作次数m ≤ 1e5

import sys
import math

class BlockColor:
    def __init__(self):
        self.n = 0
        self.m = 0
        self.blen = 0
        self.arr = []
        self.color_tag = []
        self.block = []
        self.has_tag = []
    
    # 初始化分块结构
    def init(self):
        self.blen = int(math.sqrt(self.n))
        if self.blen == 0:
            self.blen = 1
        
        # 为每个元素分配块
        self.block = [0] * self.n
        for i in range(self.n):
            self.block[i] = i // self.blen
        
        # 初始化块颜色标记
        block_count = (self.n + self.blen - 1) // self.blen
        self.color_tag = [-1] * block_count
        self.has_tag = [False] * block_count
    
    # 将颜色标记下推到块中的所有元素
    def push_down(self, block_id):
        if not self.has_tag[block_id]:
            return
        
        start = block_id * self.blen
        end = min((block_id + 1) * self.blen, self.n)
        
        # 将标记应用到块中的每个元素
        for i in range(start, end):
            self.arr[i] = self.color_tag[block_id]
        
        # 清除标记
        self.has_tag[block_id] = False
        self.color_tag[block_id] = -1
    
    # 区间颜色修改
    def update_range(self, l, r, c):
        left_block = self.block[l]
        right_block = self.block[r]
        
        if left_block == right_block:
            # 所有元素都在同一个块内
            # 先下推该块的标记
            self.push_down(left_block)
            
            # 直接修改每个元素
            for i in range(l, r + 1):
                self.arr[i] = c
        else:
            # 处理左边不完整的块
            self.push_down(left_block)
            for i in range(l, (left_block + 1) * self.blen):
                self.arr[i] = c
            
            # 处理中间完整的块（使用块标记）
            for i in range(left_block + 1, right_block):
                self.has_tag[i] = True
                self.color_tag[i] = c
            
            # 处理右边不完整的块
            self.push_down(right_block)
            for i in range(right_block * self.blen, r + 1):
                self.arr[i] = c
    
    # 单点查询颜色
    def query_point(self, pos):
        block_id = self.block[pos]
        
        if self.has_tag[block_id]:
            # 如果块有标记，直接返回标记的颜色
            return self.color_tag[block_id]
        else:
            # 否则返回原始数组中的颜色
            return self.arr[pos]

def main():
    # 使用sys.stdin.readline提高输入效率
    input = sys.stdin.read().split()
    ptr = 0
    
    solution = BlockColor()
    
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
        
        if op == 1:
            # 区间修改操作
            a = int(input[ptr]) - 1  # 转换为0-based索引
            ptr += 1
            b = int(input[ptr]) - 1
            ptr += 1
            c = int(input[ptr])
            ptr += 1
            solution.update_range(a, b, c)
        elif op == 2:
            # 单点查询操作
            a = int(input[ptr]) - 1  # 转换为0-based索引
            ptr += 1
            result = solution.query_point(a)
            output.append(str(result))
    
    # 批量输出结果
    print('\n'.join(output))

# 测试用例
# 示例：
# 输入：
# 5 3
# 1 2 3 4 5
# 1 1 3 6
# 2 2
# 2 4
# 输出：
# 6
# 4

if __name__ == "__main__":
    main()

'''
时间复杂度分析：
- 初始化：O(n)
- 单点查询：O(1)
- 区间修改：
  - 对于完整的块：O(1)（只需要设置标记）
  - 对于不完整的块：O(√n)（需要下推标记并暴力修改）
  - 总体时间复杂度：O(√n)
- 对于m次操作，总体时间复杂度：O(m√n)

空间复杂度分析：
- 数组arr：O(n)
- 标记数组color_tag和has_tag：O(√n)
- 块分配数组block：O(n)
- 总体空间复杂度：O(n + √n) = O(n)

Python语言特性注意事项：
1. 使用sys.stdin.read()一次性读取所有输入，提高效率
2. 使用列表存储数组和标记信息
3. 注意Python中的整数除法使用//运算符
4. 对于大规模数据，批量输出结果可以减少I/O操作
5. 避免使用递归，Python的递归深度有限

算法说明：
块颜色标记是分块算法的一个典型应用，主要用于处理区间颜色修改和单点查询问题。

算法步骤：
1. 将数组分成大小为√n的块
2. 对于区间颜色修改操作：
   - 对于不完整的块，先下推可能存在的标记，然后暴力修改每个元素
   - 对于完整的块，只需要设置块的颜色标记，不需要立即修改每个元素
3. 对于单点查询操作：
   - 如果该元素所在的块有颜色标记，直接返回标记的颜色
   - 否则返回原始数组中的颜色

优化说明：
1. 使用延迟标记技术，避免不必要的元素修改操作
2. 块的大小选择为√n，平衡了查询和修改的时间复杂度
3. 只有在需要访问块中的元素时才下推标记，减少操作次数
4. 在Python中，使用列表一次性存储输出结果，最后统一打印，可以提高I/O效率

与其他方法的对比：
- 暴力法：每次修改O(n)，每次查询O(1)，总时间复杂度O(mn)
- 线段树：每次修改和查询都是O(log n)，但实现复杂
- 块颜色标记：实现简单，时间复杂度适中

工程化考虑：
1. 在Python中，对于大规模数据，输入方法的选择对性能影响很大
2. 避免在循环中使用print语句，应该收集结果后批量输出
3. 可以将块的大小作为参数，根据具体数据调整以获得最佳性能
4. 对于非常大的n，需要考虑内存的使用
'''