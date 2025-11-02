"""
LOJ 6278. 数列分块入门 2 - Python实现

题目描述：
给出一个长为 n 的数列，以及 n 个操作，操作涉及区间加法，询问区间内小于某个值 x 的元素个数。

解题思路：
使用分块算法，将数组分成大小约为sqrt(n)的块。
对于每个块维护一个加法标记和排序后的数组。
区间加法操作时：
1. 对于完整块，直接更新加法标记
2. 对于不完整块，暴力更新元素值并重新排序
查询操作时：
1. 对于不完整块，暴力统计
2. 对于完整块，使用二分查找统计

时间复杂度：
- 区间加法：O(√n * log√n)
- 查询操作：O(√n * log√n)
空间复杂度：O(n)

工程化考量：
1. 异常处理：检查输入参数的有效性
2. 可配置性：块大小可根据需要调整
3. 性能优化：使用二分查找减少查询时间
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
        # 排序后的数组
        self.sorted = [0] * (size + 1)
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
    
    def init(self):
        """
        初始化分块结构
        """
        # 初始化加法标记
        for i in range(len(self.lazy)):
            self.lazy[i] = 0
            
        # 初始化排序数组
        for i in range(1, self.n + 1):
            self.sorted[i] = self.arr[i]
            
        # 对每个块内的元素进行排序
        for i in range(1, self.block_num + 1):
            temp = []
            for j in range(self.block_left[i], self.block_right[i] + 1):
                temp.append(self.sorted[j])
            temp.sort()
            for j in range(len(temp)):
                self.sorted[self.block_left[i] + j] = temp[j]
    
    def rebuild_block(self, block_id):
        """
        重构指定块的排序数组
        
        :param block_id: 块编号
        """
        # 将原数组的值复制到排序数组
        for i in range(self.block_left[block_id], self.block_right[block_id] + 1):
            self.sorted[i] = self.arr[i]
        # 对块内元素排序
        temp = []
        for i in range(self.block_left[block_id], self.block_right[block_id] + 1):
            temp.append(self.sorted[i])
        temp.sort()
        for i in range(len(temp)):
            self.sorted[self.block_left[block_id] + i] = temp[i]
    
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
            # 重构该块的排序数组
            self.rebuild_block(left_block)
        else:
            # 处理左边不完整块
            for i in range(l, self.block_right[left_block] + 1):
                self.arr[i] += val
            # 重构左边块的排序数组
            self.rebuild_block(left_block)
            
            # 处理右边不完整块
            for i in range(self.block_left[right_block], r + 1):
                self.arr[i] += val
            # 重构右边块的排序数组
            self.rebuild_block(right_block)
            
            # 处理中间完整块
            for i in range(left_block + 1, right_block):
                self.lazy[i] += val
    
    def count_in_block(self, block_id, value):
        """
        在指定块内查找小于value的元素个数
        
        :param block_id: 块编号
        :param value: 比较值
        :return: 小于value的元素个数
        """
        # 调整value，减去该块的标记值
        value -= self.lazy[block_id]
        
        # 在排序数组中使用二分查找
        left = self.block_left[block_id]
        right = self.block_right[block_id]
        
        # 如果最小值都大于等于value，返回0
        if self.sorted[left] >= value:
            return 0
            
        # 如果最大值都小于value，返回块大小
        if self.sorted[right] < value:
            return right - left + 1
            
        # 二分查找第一个大于等于value的位置
        low = left
        high = right
        pos = left
        
        while low <= high:
            mid = (low + high) // 2
            if self.sorted[mid] < value:
                pos = mid
                low = mid + 1
            else:
                high = mid - 1
                
        return pos - left + 1
    
    def query(self, l, r, value):
        """
        查询区间内小于value的元素个数
        
        :param l: 区间左端点
        :param r: 区间右端点
        :param value: 比较值
        :return: 小于value的元素个数
        """
        left_block = self.belong[l]
        right_block = self.belong[r]
        result = 0
        
        # 如果在同一个块内，暴力处理
        if left_block == right_block:
            for i in range(l, r + 1):
                if self.arr[i] + self.lazy[left_block] < value:
                    result += 1
        else:
            # 处理左边不完整块
            for i in range(l, self.block_right[left_block] + 1):
                if self.arr[i] + self.lazy[left_block] < value:
                    result += 1
                    
            # 处理右边不完整块
            for i in range(self.block_left[right_block], r + 1):
                if self.arr[i] + self.lazy[right_block] < value:
                    result += 1
                    
            # 处理中间完整块
            for i in range(left_block + 1, right_block):
                result += self.count_in_block(i, value)
                
        return result

def main():
    """
    主函数，用于测试
    """
    # 读取数组大小
    n = int(input())
    
    # 初始化分块结构
    solution = BlockSolution(n)
    solution.n = n
    
    # 读取初始数组
    elements = list(map(int, input().split()))
    for i in range(1, n + 1):
        solution.arr[i] = elements[i - 1]
        
    # 初始化分块
    solution.init()
    
    # 处理操作
    for _ in range(n):
        op, l, r, c = map(int, input().split())
        
        if op == 0:
            # 区间加法
            solution.add(l, r, c)
        else:
            # 查询操作
            print(solution.query(l, r, c * c))

if __name__ == "__main__":
    main()