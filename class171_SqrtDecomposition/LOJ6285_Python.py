import sys
import math

"""
LOJ 6285. 数列分块入门 9 - Python实现

题目描述：
给出一个长为 n 的数列，以及 n 个操作，操作涉及区间乘法，区间加法，区间求和。

解题思路：
使用分块算法，将数组分成大小约为sqrt(n)的块。
对于每个块维护：
1. 乘法标记和加法标记用于延迟更新
2. 块内元素和用于快速区间求和
当进行区间乘法操作时：
1. 更新块的乘法标记和加法标记
2. 更新块内元素和
当进行区间加法操作时：
1. 更新块的加法标记
2. 更新块内元素和
区间求和时：
1. 对不完整块，先下传标记，再暴力计算
2. 对完整块，直接利用块内元素和

时间复杂度：
- 区间操作：O(√n)
- 区间求和：O(√n)
空间复杂度：O(n)

工程化考量：
1. 异常处理：检查输入参数的有效性
2. 可配置性：块大小可根据需要调整
3. 性能优化：使用延迟标记减少实际操作次数
4. 鲁棒性：处理边界情况和特殊输入
5. 取模操作：注意溢出问题，使用long类型存储中间结果
"""

MOD = 10007

class LOJ6285:
    def __init__(self):
        self.arr = []         # 原数组（索引从1开始）
        self.belong = []      # 每个元素所属的块
        self.mul = []         # 每个块的乘法标记
        self.add = []         # 每个块的加法标记
        self.block_left = []  # 每个块的左边界
        self.block_right = [] # 每个块的右边界
        self.sum = []         # 每个块的元素和
        
        self.block_size = 0   # 块大小
        self.block_num = 0    # 块数量
        self.n = 0            # 数组大小
    
    def init(self, size):
        """初始化分块结构"""
        self.n = size
        # 设置块大小为sqrt(n)
        self.block_size = int(math.sqrt(n))
        # 计算块数量
        self.block_num = (n + self.block_size - 1) // self.block_size
        
        # 初始化数组，索引从1开始
        self.arr = [0] * (n + 2)
        self.belong = [0] * (n + 2)
        self.mul = [1] * (self.block_num + 2)
        self.add = [0] * (self.block_num + 2)
        self.block_left = [0] * (self.block_num + 2)
        self.block_right = [0] * (self.block_num + 2)
        self.sum = [0] * (self.block_num + 2)
        
        # 初始化每个元素所属的块
        for i in range(1, n + 1):
            self.belong[i] = (i - 1) // self.block_size + 1
        
        # 初始化每个块的边界
        for i in range(1, self.block_num + 1):
            self.block_left[i] = (i - 1) * self.block_size + 1
            self.block_right[i] = min(i * self.block_size, n)
    
    def update_sum(self, block):
        """更新块内元素和"""
        self.sum[block] = 0
        for i in range(self.block_left[block], self.block_right[block] + 1):
            # 计算元素的实际值并累加到块和中
            actual_val = (self.arr[i] * self.mul[block] + self.add[block]) % MOD
            if actual_val < 0:
                actual_val += MOD
            self.sum[block] = (self.sum[block] + actual_val) % MOD
    
    def push_down(self, block):
        """向下传递标记（将块的标记应用到每个元素）"""
        # 如果该块有标记
        if self.mul[block] != 1 or self.add[block] != 0:
            # 对块内所有元素应用标记
            for i in range(self.block_left[block], self.block_right[block] + 1):
                self.arr[i] = (self.arr[i] * self.mul[block] + self.add[block]) % MOD
                # 确保结果为非负数
                if self.arr[i] < 0:
                    self.arr[i] += MOD
            # 重置标记
            self.mul[block] = 1
            self.add[block] = 0
    
    def multiply(self, l, r, val):
        """区间乘法操作"""
        val %= MOD
        # 确保val为正数
        if val < 0:
            val += MOD
        left_block = self.belong[l]
        right_block = self.belong[r]
        
        # 如果在同一个块内，暴力处理
        if left_block == right_block:
            # 下传标记
            self.push_down(left_block)
            # 更新元素值
            for i in range(l, r + 1):
                self.arr[i] = (self.arr[i] * val) % MOD
                if self.arr[i] < 0:
                    self.arr[i] += MOD
            # 更新块和
            self.update_sum(left_block)
        else:
            # 处理左边不完整块
            self.push_down(left_block)
            for i in range(l, self.block_right[left_block] + 1):
                self.arr[i] = (self.arr[i] * val) % MOD
                if self.arr[i] < 0:
                    self.arr[i] += MOD
            self.update_sum(left_block)
            
            # 处理右边不完整块
            self.push_down(right_block)
            for i in range(self.block_left[right_block], r + 1):
                self.arr[i] = (self.arr[i] * val) % MOD
                if self.arr[i] < 0:
                    self.arr[i] += MOD
            self.update_sum(right_block)
            
            # 处理中间完整块
            for i in range(left_block + 1, right_block):
                # 更新乘法标记
                self.mul[i] = (self.mul[i] * val) % MOD
                if self.mul[i] < 0:
                    self.mul[i] += MOD
                # 更新加法标记
                self.add[i] = (self.add[i] * val) % MOD
                if self.add[i] < 0:
                    self.add[i] += MOD
                # 更新块和
                self.sum[i] = (self.sum[i] * val) % MOD
                if self.sum[i] < 0:
                    self.sum[i] += MOD
    
    def add(self, l, r, val):
        """区间加法操作"""
        val %= MOD
        # 确保val为正数
        if val < 0:
            val += MOD
        left_block = self.belong[l]
        right_block = self.belong[r]
        
        # 如果在同一个块内，暴力处理
        if left_block == right_block:
            # 下传标记
            self.push_down(left_block)
            # 更新元素值
            for i in range(l, r + 1):
                self.arr[i] = (self.arr[i] + val) % MOD
                if self.arr[i] < 0:
                    self.arr[i] += MOD
            # 更新块和
            self.update_sum(left_block)
        else:
            # 处理左边不完整块
            self.push_down(left_block)
            for i in range(l, self.block_right[left_block] + 1):
                self.arr[i] = (self.arr[i] + val) % MOD
                if self.arr[i] < 0:
                    self.arr[i] += MOD
            self.update_sum(left_block)
            
            # 处理右边不完整块
            self.push_down(right_block)
            for i in range(self.block_left[right_block], r + 1):
                self.arr[i] = (self.arr[i] + val) % MOD
                if self.arr[i] < 0:
                    self.arr[i] += MOD
            self.update_sum(right_block)
            
            # 处理中间完整块
            for i in range(left_block + 1, right_block):
                # 更新加法标记
                self.add[i] = (self.add[i] + val) % MOD
                if self.add[i] < 0:
                    self.add[i] += MOD
                # 更新块和
                block_size = self.block_right[i] - self.block_left[i] + 1
                self.sum[i] = (self.sum[i] + val * block_size) % MOD
                if self.sum[i] < 0:
                    self.sum[i] += MOD
    
    def query(self, l, r):
        """区间求和查询"""
        left_block = self.belong[l]
        right_block = self.belong[r]
        result = 0
        
        # 如果在同一个块内，暴力处理
        if left_block == right_block:
            # 下传标记
            self.push_down(left_block)
            # 暴力计算和
            for i in range(l, r + 1):
                result = (result + self.arr[i]) % MOD
                if result < 0:
                    result += MOD
        else:
            # 处理左边不完整块
            self.push_down(left_block)
            for i in range(l, self.block_right[left_block] + 1):
                result = (result + self.arr[i]) % MOD
                if result < 0:
                    result += MOD
            
            # 处理右边不完整块
            self.push_down(right_block)
            for i in range(self.block_left[right_block], r + 1):
                result = (result + self.arr[i]) % MOD
                if result < 0:
                    result += MOD
            
            # 处理中间完整块，直接累加块和
            for i in range(left_block + 1, right_block):
                result = (result + self.sum[i]) % MOD
                if result < 0:
                    result += MOD
        
        return result
    
    def set_value(self, index, value):
        """设置数组元素值"""
        self.arr[index] = value % MOD
        # 确保值为非负数
        if self.arr[index] < 0:
            self.arr[index] += MOD
        # 更新所属块的和
        self.update_sum(self.belong[index])

# 主函数
if __name__ == "__main__":
    input = sys.stdin.read().split()
    ptr = 0
    
    n = int(input[ptr])
    ptr += 1
    
    solution = LOJ6285()
    solution.init(n)
    
    # 读取初始数组
    for i in range(1, n + 1):
        value = int(input[ptr])
        ptr += 1
        solution.set_value(i, value)
    
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
            # 区间求和
            print(solution.query(l, r) % MOD)