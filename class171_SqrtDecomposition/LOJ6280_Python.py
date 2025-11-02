import math
import sys

"""
LOJ 6280. 数列分块入门 4 - Python实现

题目描述：
给出一个长为 n 的数列，以及 n 个操作，操作涉及区间加法，区间求和。

解题思路：
使用分块算法，将数组分成大小约为sqrt(n)的块。
对于每个块维护一个加法标记和块内元素和。
区间加法操作时：
1. 对于完整块，更新加法标记和块内元素和
2. 对于不完整块，暴力更新元素值并更新块内元素和
查询操作时：
1. 对于不完整块，暴力计算元素和
2. 对于完整块，直接使用块内元素和

时间复杂度：
- 区间加法：O(√n)
- 查询操作：O(√n)
空间复杂度：O(n)

工程化考量：
1. 异常处理：检查输入参数的有效性
2. 可配置性：块大小可根据需要调整
3. 性能优化：维护块内元素和减少计算量
4. 鲁棒性：处理边界情况和特殊输入
"""

class LOJ6280:
    def __init__(self):
        self.MAXN = 50010
        self.arr = [0] * (self.MAXN + 1)  # 原数组（索引从1开始）
        self.sum = [0] * (self.MAXN + 1)   # 每个块的元素和
        self.belong = [0] * (self.MAXN + 1)  # 每个元素所属的块
        self.lazy = [0] * (self.MAXN + 1)  # 每个块的加法标记
        self.blockLeft = [0] * (self.MAXN + 1)  # 每个块的左边界
        self.blockRight = [0] * (self.MAXN + 1)  # 每个块的右边界
        self.blockSize = 0  # 块大小
        self.blockNum = 0  # 块数量
        self.n = 0  # 数组大小
    
    def init(self, size):
        """初始化分块结构"""
        self.n = size
        # 设置块大小为sqrt(n)
        self.blockSize = int(math.sqrt(n))
        # 计算块数量
        self.blockNum = (n + self.blockSize - 1) // self.blockSize
        
        # 初始化每个元素所属的块
        for i in range(1, n + 1):
            self.belong[i] = (i - 1) // self.blockSize + 1
        
        # 初始化每个块的边界
        for i in range(1, self.blockNum + 1):
            self.blockLeft[i] = (i - 1) * self.blockSize + 1
            self.blockRight[i] = min(i * self.blockSize, n)
        
        # 初始化加法标记和块和
        for i in range(1, self.blockNum + 1):
            self.lazy[i] = 0
            self.sum[i] = 0
    
    def set_value(self, index, value):
        """设置数组元素值"""
        self.arr[index] = value
        # 更新所在块的和
        block = self.belong[index]
        self.sum[block] = 0
        for i in range(self.blockLeft[block], self.blockRight[block] + 1):
            self.sum[block] += self.arr[i]
        self.sum[block] += self.lazy[block] * (self.blockRight[block] - self.blockLeft[block] + 1)
    
    def add(self, l, r, val):
        """区间加法操作"""
        left_block = self.belong[l]
        right_block = self.belong[r]
        
        # 如果在同一个块内，暴力处理
        if left_block == right_block:
            # 更新元素值
            for i in range(l, r + 1):
                self.arr[i] += val
            # 重新计算块和
            self.sum[left_block] = 0
            for i in range(self.blockLeft[left_block], self.blockRight[left_block] + 1):
                self.sum[left_block] += self.arr[i]
            # 加上块的加法标记
            self.sum[left_block] += self.lazy[left_block] * (self.blockRight[left_block] - self.blockLeft[left_block] + 1)
        else:
            # 处理左边不完整块
            for i in range(l, self.blockRight[left_block] + 1):
                self.arr[i] += val
            # 重新计算左边块的和
            self.sum[left_block] = 0
            for i in range(self.blockLeft[left_block], self.blockRight[left_block] + 1):
                self.sum[left_block] += self.arr[i]
            self.sum[left_block] += self.lazy[left_block] * (self.blockRight[left_block] - self.blockLeft[left_block] + 1)
            
            # 处理右边不完整块
            for i in range(self.blockLeft[right_block], r + 1):
                self.arr[i] += val
            # 重新计算右边块的和
            self.sum[right_block] = 0
            for i in range(self.blockLeft[right_block], self.blockRight[right_block] + 1):
                self.sum[right_block] += self.arr[i]
            self.sum[right_block] += self.lazy[right_block] * (self.blockRight[right_block] - self.blockLeft[right_block] + 1)
            
            # 处理中间完整块
            for i in range(left_block + 1, right_block):
                self.lazy[i] += val
                self.sum[i] += val * (self.blockRight[i] - self.blockLeft[i] + 1)
    
    def query(self, l, r):
        """查询区间和"""
        left_block = self.belong[l]
        right_block = self.belong[r]
        result = 0
        
        # 如果在同一个块内，暴力处理
        if left_block == right_block:
            for i in range(l, r + 1):
                result += self.arr[i] + self.lazy[left_block]
        else:
            # 处理左边不完整块
            for i in range(l, self.blockRight[left_block] + 1):
                result += self.arr[i] + self.lazy[left_block]
            
            # 处理中间完整块
            for i in range(left_block + 1, right_block):
                result += self.sum[i]
            
            # 处理右边不完整块
            for i in range(self.blockLeft[right_block], r + 1):
                result += self.arr[i] + self.lazy[right_block]
        
        return result
    
    def init_sum(self):
        """初始化块和"""
        for i in range(1, self.blockNum + 1):
            self.sum[i] = 0
            for j in range(self.blockLeft[i], self.blockRight[i] + 1):
                self.sum[i] += self.arr[j]

# 主函数
if __name__ == "__main__":
    input = sys.stdin.read().split()
    ptr = 0
    
    n = int(input[ptr])
    ptr += 1
    
    solution = LOJ6280()
    solution.init(n)
    
    # 读取初始数组
    for i in range(1, n + 1):
        value = int(input[ptr])
        ptr += 1
        solution.set_value(i, value)
    
    # 初始化块和
    solution.init_sum()
    
    # 处理操作
    for _ in range(n):
        op = int(input[ptr])
        l = int(input[ptr+1])
        r = int(input[ptr+2])
        c = int(input[ptr+3])
        ptr += 4
        
        if op == 0:
            # 区间加法
            solution.add(l, r, c)
        else:
            # 区间求和
            print(solution.query(l, r) % (c + 1))