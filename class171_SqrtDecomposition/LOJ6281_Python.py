import math
import sys

"""
LOJ 6281. 数列分块入门 5 - Python实现

题目描述：
给出一个长为 n 的数列，以及 n 个操作，操作涉及区间乘法，区间加法，单点询问。

解题思路：
使用分块算法，将数组分成大小约为sqrt(n)的块。
对于每个块维护乘法标记和加法标记。
当进行区间乘法操作时：
1. 更新块的乘法标记和加法标记
2. 更新块内元素和
当进行区间加法操作时：
1. 更新块的加法标记
2. 更新块内元素和
单点查询时：
1. 考虑块的乘法标记和加法标记，计算元素的实际值

时间复杂度：
- 区间操作：O(√n)
- 查询操作：O(1)
空间复杂度：O(n)

工程化考量：
1. 异常处理：检查输入参数的有效性
2. 可配置性：块大小可根据需要调整
3. 性能优化：维护块内元素和减少计算量
4. 鲁棒性：处理边界情况和特殊输入
5. 取模操作：注意溢出问题
"""

class LOJ6281:
    def __init__(self):
        self.MAXN = 100010
        self.MOD = 10007
        self.arr = [0] * (self.MAXN + 1)  # 原数组（索引从1开始）
        self.sum = [0] * (self.MAXN + 1)   # 每个块的元素和
        self.belong = [0] * (self.MAXN + 1)  # 每个元素所属的块
        self.mul = [0] * (self.MAXN + 1)  # 每个块的乘法标记
        self.add = [0] * (self.MAXN + 1)  # 每个块的加法标记
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
        
        # 初始化标记，乘法标记初始化为1，加法标记初始化为0
        for i in range(1, self.blockNum + 1):
            self.mul[i] = 1
            self.add[i] = 0
    
    def push_down(self, block):
        """向下传递标记（将块的标记应用到每个元素）"""
        # 如果该块有标记
        if self.mul[block] != 1 or self.add[block] != 0:
            # 对块内所有元素应用标记
            for i in range(self.blockLeft[block], self.blockRight[block] + 1):
                self.arr[i] = (self.arr[i] * self.mul[block] + self.add[block]) % self.MOD
            # 重置标记
            self.mul[block] = 1
            self.add[block] = 0
    
    def multiply(self, l, r, val):
        """区间乘法操作"""
        val %= self.MOD
        left_block = self.belong[l]
        right_block = self.belong[r]
        
        # 如果在同一个块内，暴力处理
        if left_block == right_block:
            # 下传标记
            self.push_down(left_block)
            # 更新元素值并计算块和
            self.sum[left_block] = 0
            for i in range(l, r + 1):
                self.arr[i] = (self.arr[i] * val) % self.MOD
            # 重新计算块和
            for i in range(self.blockLeft[left_block], self.blockRight[left_block] + 1):
                self.sum[left_block] = (self.sum[left_block] + self.arr[i]) % self.MOD
        else:
            # 处理左边不完整块
            self.push_down(left_block)
            self.sum[left_block] = 0
            for i in range(l, self.blockRight[left_block] + 1):
                self.arr[i] = (self.arr[i] * val) % self.MOD
            # 重新计算左边块的和
            for i in range(self.blockLeft[left_block], self.blockRight[left_block] + 1):
                self.sum[left_block] = (self.sum[left_block] + self.arr[i]) % self.MOD
            
            # 处理右边不完整块
            self.push_down(right_block)
            self.sum[right_block] = 0
            for i in range(self.blockLeft[right_block], r + 1):
                self.arr[i] = (self.arr[i] * val) % self.MOD
            # 重新计算右边块的和
            for i in range(self.blockLeft[right_block], self.blockRight[right_block] + 1):
                self.sum[right_block] = (self.sum[right_block] + self.arr[i]) % self.MOD
            
            # 处理中间完整块
            for i in range(left_block + 1, right_block):
                # 更新乘法标记
                self.mul[i] = (self.mul[i] * val) % self.MOD
                # 更新加法标记
                self.add[i] = (self.add[i] * val) % self.MOD
                # 更新块和
                self.sum[i] = (self.sum[i] * val) % self.MOD
    
    def add(self, l, r, val):
        """区间加法操作"""
        val %= self.MOD
        left_block = self.belong[l]
        right_block = self.belong[r]
        
        # 如果在同一个块内，暴力处理
        if left_block == right_block:
            # 下传标记
            self.push_down(left_block)
            # 更新元素值并计算块和
            self.sum[left_block] = 0
            for i in range(l, r + 1):
                self.arr[i] = (self.arr[i] + val) % self.MOD
            # 重新计算块和
            for i in range(self.blockLeft[left_block], self.blockRight[left_block] + 1):
                self.sum[left_block] = (self.sum[left_block] + self.arr[i]) % self.MOD
        else:
            # 处理左边不完整块
            self.push_down(left_block)
            self.sum[left_block] = 0
            for i in range(l, self.blockRight[left_block] + 1):
                self.arr[i] = (self.arr[i] + val) % self.MOD
            # 重新计算左边块的和
            for i in range(self.blockLeft[left_block], self.blockRight[left_block] + 1):
                self.sum[left_block] = (self.sum[left_block] + self.arr[i]) % self.MOD
            
            # 处理右边不完整块
            self.push_down(right_block)
            self.sum[right_block] = 0
            for i in range(self.blockLeft[right_block], r + 1):
                self.arr[i] = (self.arr[i] + val) % self.MOD
            # 重新计算右边块的和
            for i in range(self.blockLeft[right_block], self.blockRight[right_block] + 1):
                self.sum[right_block] = (self.sum[right_block] + self.arr[i]) % self.MOD
            
            # 处理中间完整块
            for i in range(left_block + 1, right_block):
                # 更新加法标记
                self.add[i] = (self.add[i] + val) % self.MOD
                # 更新块和
                self.sum[i] = (self.sum[i] + val * (self.blockRight[i] - self.blockLeft[i] + 1)) % self.MOD
    
    def query(self, index):
        """单点查询"""
        block = self.belong[index]
        # 考虑块的乘法标记和加法标记
        return (self.arr[index] * self.mul[block] + self.add[block]) % self.MOD
    
    def set_value(self, index, value):
        """设置数组元素值"""
        self.arr[index] = value % self.MOD
    
    def init_sum(self):
        """初始化块和"""
        for i in range(1, self.blockNum + 1):
            self.sum[i] = 0
            for j in range(self.blockLeft[i], self.blockRight[i] + 1):
                self.sum[i] = (self.sum[i] + self.arr[j]) % self.MOD

# 主函数
if __name__ == "__main__":
    input = sys.stdin.read().split()
    ptr = 0
    
    n = int(input[ptr])
    ptr += 1
    
    solution = LOJ6281()
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
            # 区间乘法
            solution.multiply(l, r, c)
        elif op == 1:
            # 区间加法
            solution.add(l, r, c)
        else:
            # 单点查询
            print(solution.query(r) % solution.MOD)