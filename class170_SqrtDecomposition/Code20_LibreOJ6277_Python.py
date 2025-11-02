"""
LibreOJ #6277 数列分块入门1 - Python实现
题目：区间加法，单点查询
来源：LibreOJ (https://loj.ac/p/6277)

算法：平方根分解（分块算法）
时间复杂度：O(n√n)
空间复杂度：O(n)
最优解：是，对于这种简单的区间更新单点查询，平方根分解是最优解之一

思路：
1. 将数组分成√n个块，每个块大小为block_size
2. 维护每个块的加法标记（懒标记）
3. 区间更新时，完整块直接更新标记，不完整块暴力更新
4. 单点查询时，返回原值加上所在块的标记

工程化考量：
- 使用懒标记减少不必要的更新操作
- 块大小选择√n，平衡查询和更新效率
- 边界处理：正确处理区间边界和块边界
- Python特性：利用列表推导和切片操作优化性能
"""

import math

class BlockArray:
    def __init__(self, nums):
        """
        初始化分块数组
        
        Args:
            nums: 原始数组
        """
        self.arr = nums[:]  # 原始数组的副本
        n = len(nums)
        self.block_size = int(math.sqrt(n))  # 块大小
        self.block_count = (n + self.block_size - 1) // self.block_size  # 块数量
        self.block_add = [0] * self.block_count  # 每个块的加法标记
    
    def range_add(self, l, r, val):
        """
        区间加法操作
        
        Args:
            l: 区间左边界（包含）
            r: 区间右边界（包含）
            val: 要加的值
        """
        block_l = l // self.block_size
        block_r = r // self.block_size
        
        # 同一个块内，直接暴力更新
        if block_l == block_r:
            for i in range(l, r + 1):
                self.arr[i] += val
            return
        
        # 更新左边界不完整块
        for i in range(l, (block_l + 1) * self.block_size):
            self.arr[i] += val
        
        # 更新中间完整块
        for i in range(block_l + 1, block_r):
            self.block_add[i] += val
        
        # 更新右边界不完整块
        for i in range(block_r * self.block_size, r + 1):
            self.arr[i] += val
    
    def point_query(self, index):
        """
        单点查询
        
        Args:
            index: 查询索引
            
        Returns:
            索引处的值
        """
        block_idx = index // self.block_size
        return self.arr[index] + self.block_add[block_idx]

def main():
    """测试函数"""
    # 测试用例
    nums = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
    ba = BlockArray(nums)
    
    # 测试区间加法
    ba.range_add(2, 7, 5)  # 给索引2-7的元素加5
    
    # 测试单点查询
    print(f"索引3的值: {ba.point_query(3)}")  # 应该输出9 (4+5)
    print(f"索引8的值: {ba.point_query(8)}")  # 应该输出9 (9+0)
    
    # 边界测试
    ba.range_add(0, 9, 10)  # 给所有元素加10
    print(f"索引0的值: {ba.point_query(0)}")  # 应该输出11 (1+10)
    print(f"索引9的值: {ba.point_query(9)}")  # 应该输出20 (10+10)
    
    # 性能测试
    import time
    large_nums = list(range(10000))
    large_ba = BlockArray(large_nums)
    
    start_time = time.time()
    for i in range(100):
        large_ba.range_add(i * 10, i * 10 + 50, i)
    end_time = time.time()
    
    print(f"性能测试: 100次区间更新耗时 {end_time - start_time:.4f} 秒")

if __name__ == "__main__":
    main()