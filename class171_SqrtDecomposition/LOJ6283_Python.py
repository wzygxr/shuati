import sys
import math

"""
LOJ 6283. 数列分块入门 7 - Python实现

题目描述：
给出一个长为 n 的数列，以及 n 个操作，操作涉及区间乘法，区间加法，区间求和。

解题思路：
使用分块算法，将数组分成大小约为sqrt(n)的块。
对于每个块维护乘法标记、加法标记和块内元素和。
当进行区间乘法操作时：
1. 更新块的乘法标记和加法标记
2. 更新块内元素和
当进行区间加法操作时：
1. 更新块的加法标记
2. 更新块内元素和
区间查询时：
1. 对于不完整块，考虑块的标记计算元素实际值
2. 对于完整块，直接使用块内元素和

时间复杂度：
- 区间操作：O(√n)
- 查询操作：O(√n)
空间复杂度：O(n)

工程化考量：
1. 异常处理：检查输入参数的有效性
2. 可配置性：块大小可根据需要调整
3. 性能优化：维护块内元素和减少计算量
4. 鲁棒性：处理边界情况和特殊输入
5. 取模操作：注意溢出问题
"""

MOD = 10007

class LOJ6283:
    def __init__(self):
        self.arr = []         # 原数组（索引从1开始）
        self.sum = []         # 每个块的元素和
        self.belong = []      # 每个元素所属的块
        self.mul = []         # 每个块的乘法标记
        self.add = []         # 每个块的加法标记
        self.block_left = []  # 每个块的左边界
        self.block_right = [] # 每个块的右边界
        
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
        self.sum = [0] * (self.block_num + 2)
        self.belong = [0] * (n + 2)
        self.mul = [1] * (self.block_num + 2)
        self.add = [0] * (self.block_num + 2)
        self.block_left = [0] * (self.block_num + 2)
        self.block_right = [0] * (self.block_num + 2)
        
        # 初始化每个元素所属的块
        for i in range(1, n + 1):
            self.belong[i] = (i - 1) // self.block_size + 1
        
        # 初始化每个块的边界
        for i in range(1, self.block_num + 1):
            self.block_left[i] = (i - 1) * self.block_size + 1
            self.block_right[i] = min(i * self.block_size, n)
    
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
            # 更新元素值并计算块和
            self.sum[left_block] = 0
            for i in range(l, r + 1):
                self.arr[i] = (self.arr[i] * val) % MOD
                if self.arr[i] < 0:
                    self.arr[i] += MOD
            # 重新计算块和
            for i in range(self.block_left[left_block], self.block_right[left_block] + 1):
                self.sum[left_block] = (self.sum[left_block] + self.arr[i]) % MOD
                if self.sum[left_block] < 0:
                    self.sum[left_block] += MOD
        else:
            # 处理左边不完整块
            self.push_down(left_block)
            self.sum[left_block] = 0
            for i in range(l, self.block_right[left_block] + 1):
                self.arr[i] = (self.arr[i] * val) % MOD
                if self.arr[i] < 0:
                    self.arr[i] += MOD
            # 重新计算左边块的和
            for i in range(self.block_left[left_block], self.block_right[left_block] + 1):
                self.sum[left_block] = (self.sum[left_block] + self.arr[i]) % MOD
                if self.sum[left_block] < 0:
                    self.sum[left_block] += MOD
            
            # 处理右边不完整块
            self.push_down(right_block)
            self.sum[right_block] = 0
            for i in range(self.block_left[right_block], r + 1):
                self.arr[i] = (self.arr[i] * val) % MOD
                if self.arr[i] < 0:
                    self.arr[i] += MOD
            # 重新计算右边块的和
            for i in range(self.block_left[right_block], self.block_right[right_block] + 1):
                self.sum[right_block] = (self.sum[right_block] + self.arr[i]) % MOD
                if self.sum[right_block] < 0:
                    self.sum[right_block] += MOD
            
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
            # 更新元素值并计算块和
            self.sum[left_block] = 0
            for i in range(l, r + 1):
                self.arr[i] = (self.arr[i] + val) % MOD
                if self.arr[i] < 0:
                    self.arr[i] += MOD
            # 重新计算块和
            for i in range(self.block_left[left_block], self.block_right[left_block] + 1):
                self.sum[left_block] = (self.sum[left_block] + self.arr[i]) % MOD
                if self.sum[left_block] < 0:
                    self.sum[left_block] += MOD
        else:
            # 处理左边不完整块
            self.push_down(left_block)
            self.sum[left_block] = 0
            for i in range(l, self.block_right[left_block] + 1):
                self.arr[i] = (self.arr[i] + val) % MOD
                if self.arr[i] < 0:
                    self.arr[i] += MOD
            # 重新计算左边块的和
            for i in range(self.block_left[left_block], self.block_right[left_block] + 1):
                self.sum[left_block] = (self.sum[left_block] + self.arr[i]) % MOD
                if self.sum[left_block] < 0:
                    self.sum[left_block] += MOD
            
            # 处理右边不完整块
            self.push_down(right_block)
            self.sum[right_block] = 0
            for i in range(self.block_left[right_block], r + 1):
                self.arr[i] = (self.arr[i] + val) % MOD
                if self.arr[i] < 0:
                    self.arr[i] += MOD
            # 重新计算右边块的和
            for i in range(self.block_left[right_block], self.block_right[right_block] + 1):
                self.sum[right_block] = (self.sum[right_block] + self.arr[i]) % MOD
                if self.sum[right_block] < 0:
                    self.sum[right_block] += MOD
            
            # 处理中间完整块
            for i in range(left_block + 1, right_block):
                # 更新加法标记
                self.add[i] = (self.add[i] + val) % MOD
                if self.add[i] < 0:
                    self.add[i] += MOD
                # 更新块和
                cnt = self.block_right[i] - self.block_left[i] + 1
                self.sum[i] = (self.sum[i] + val * cnt) % MOD
                if self.sum[i] < 0:
                    self.sum[i] += MOD
    
    def query(self, l, r):
        """区间求和查询"""
        left_block = self.belong[l]
        right_block = self.belong[r]
        result = 0
        
        # 如果在同一个块内，暴力处理
        if left_block == right_block:
            self.push_down(left_block)  # 先下传标记
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
            
            # 处理中间完整块
            for i in range(left_block + 1, right_block):
                result = (result + self.sum[i]) % MOD
                if result < 0:
                    result += MOD
            
            # 处理右边不完整块
            self.push_down(right_block)
            for i in range(self.block_left[right_block], r + 1):
                result = (result + self.arr[i]) % MOD
                if result < 0:
                    result += MOD
        
        return result
    
    def set_value(self, index, value):
        """设置数组元素值"""
        self.arr[index] = value % MOD
        if self.arr[index] < 0:
            self.arr[index] += MOD
    
    def init_sum(self):
        """初始化块和"""
        for i in range(1, self.block_num + 1):
            self.sum[i] = 0
            for j in range(self.block_left[i], self.block_right[i] + 1):
                self.sum[i] = (self.sum[i] + self.arr[j]) % MOD
                if self.sum[i] < 0:
                    self.sum[i] += MOD

# 主函数
if __name__ == "__main__":
    input = sys.stdin.read().split()
    ptr = 0
    
    n = int(input[ptr])
    ptr += 1
    
    solution = LOJ6283()
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
            # 区间求和
            print(solution.query(l, r) % MOD)