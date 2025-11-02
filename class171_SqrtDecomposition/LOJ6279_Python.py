import math
import sys

"""
LOJ 6279. 数列分块入门 3 - Python实现

题目描述：
给出一个长为 n 的数列，以及 n 个操作，操作涉及区间加法，询问区间内小于某个值 x 的前驱（比x小的最大元素）。

解题思路：
使用分块算法，将数组分成大小约为sqrt(n)的块。
对于每个块维护一个加法标记和排序后的数组。
区间加法操作时：
1. 对于完整块，直接更新加法标记
2. 对于不完整块，暴力更新元素值并重新排序
查询操作时：
1. 对于不完整块，暴力查找前驱
2. 对于完整块，使用二分查找寻找前驱

时间复杂度：
- 区间加法：O(√n * log√n)
- 查询操作：O(√n * log√n)
空间复杂度：O(n)

工程化考量：
1. 异常处理：检查输入参数的有效性，处理没有前驱的情况
2. 可配置性：块大小可根据需要调整
3. 性能优化：使用二分查找减少查询时间
4. 鲁棒性：处理边界情况和特殊输入
"""

class LOJ6279:
    def __init__(self):
        self.MAXN = 50010
        self.arr = [0] * (self.MAXN + 1)  # 原数组（索引从1开始）
        self.sorted = [0] * (self.MAXN + 1)  # 排序后的数组
        self.belong = [0] * (self.MAXN + 1)  # 每个元素所属的块
        self.lazy = [0] * (self.MAXN + 1)  # 每个块的加法标记
        self.blockLeft = [0] * (self.MAXN + 1)  # 每个块的左边界
        self.blockRight = [0] * (self.MAXN + 1)  # 每个块的右边界
        self.blockSize = 0  # 块大小
        self.blockNum = 0  # 块数量
        self.n = 0  # 数组大小
    
    def rebuild(self, block):
        """重构指定块的排序数组"""
        left = self.blockLeft[block]
        right = self.blockRight[block]
        
        # 复制原数组到排序数组
        for i in range(left, right + 1):
            self.sorted[i] = self.arr[i]
        
        # 对块内元素排序
        self.sorted[left:right+1] = sorted(self.sorted[left:right+1])
    
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
        
        # 初始化加法标记
        for i in range(1, self.blockNum + 1):
            self.lazy[i] = 0
    
    def set_value(self, index, value):
        """设置数组元素值"""
        self.arr[index] = value
    
    def add(self, l, r, val):
        """区间加法操作"""
        left_block = self.belong[l]
        right_block = self.belong[r]
        
        # 如果在同一个块内，暴力处理
        if left_block == right_block:
            for i in range(l, r + 1):
                self.arr[i] += val
            # 重构排序数组
            self.rebuild(left_block)
        else:
            # 处理左边不完整块
            for i in range(l, self.blockRight[left_block] + 1):
                self.arr[i] += val
            self.rebuild(left_block)
            
            # 处理右边不完整块
            for i in range(self.blockLeft[right_block], r + 1):
                self.arr[i] += val
            self.rebuild(right_block)
            
            # 处理中间完整块
            for i in range(left_block + 1, right_block):
                self.lazy[i] += val
    
    def query(self, l, r, x):
        """查询区间内小于x的前驱"""
        left_block = self.belong[l]
        right_block = self.belong[r]
        result = -1
        
        # 如果在同一个块内，暴力处理
        if left_block == right_block:
            for i in range(l, r + 1):
                actual_value = self.arr[i] + self.lazy[left_block]
                if actual_value < x and actual_value > result:
                    result = actual_value
        else:
            # 处理左边不完整块
            for i in range(l, self.blockRight[left_block] + 1):
                actual_value = self.arr[i] + self.lazy[left_block]
                if actual_value < x and actual_value > result:
                    result = actual_value
            
            # 处理中间完整块
            for i in range(left_block + 1, right_block):
                current_block_value = x - self.lazy[i]
                # 在排序数组中二分查找前驱
                left = self.blockLeft[i]
                right = self.blockRight[i]
                current_max = -1
                
                # 二分查找小于current_block_value的最大元素
                low = left
                high = right
                while low <= high:
                    mid = (low + high) // 2
                    if self.sorted[mid] < current_block_value:
                        current_max = self.sorted[mid]
                        low = mid + 1
                    else:
                        high = mid - 1
                
                if current_max != -1:
                    current_max += self.lazy[i]
                    if current_max > result:
                        result = current_max
            
            # 处理右边不完整块
            for i in range(self.blockLeft[right_block], r + 1):
                actual_value = self.arr[i] + self.lazy[right_block]
                if actual_value < x and actual_value > result:
                    result = actual_value
        
        return result
    
    def init_sorted_arrays(self):
        """初始化所有块的排序数组"""
        for i in range(1, self.blockNum + 1):
            self.rebuild(i)

# 主函数
if __name__ == "__main__":
    input = sys.stdin.read().split()
    ptr = 0
    
    n = int(input[ptr])
    ptr += 1
    
    solution = LOJ6279()
    solution.init(n)
    
    # 读取初始数组
    for i in range(1, n + 1):
        value = int(input[ptr])
        ptr += 1
        solution.set_value(i, value)
    
    # 初始化排序数组
    solution.init_sorted_arrays()
    
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
            # 查询区间内小于x的前驱
            print(solution.query(l, r, c))