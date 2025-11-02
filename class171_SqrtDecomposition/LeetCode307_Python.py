import math

"""
LeetCode 307. 区域和检索 - 数组可修改 - Python实现
题目链接：https://leetcode.cn/problems/range-sum-query-mutable/

题目描述：
给你一个数组 nums ，请你实现两类查询：
1. update(index, val)：将下标为 index 的元素更新为 val 。
2. sumRange(left, right)：返回数组 nums 中，下标范围 [left, right] 内的元素之和。

解题思路：
使用分块算法，将数组分成大小约为sqrt(n)的块。
对于每个块维护块内元素和，这样：
- 更新操作：找到对应的块，更新元素值并更新块内和，复杂度 O(1)
- 求和操作：遍历左右不完整块，累加元素值；遍历中间完整块，累加块内和，复杂度 O(√n)

时间复杂度：
- update操作：O(1)
- sumRange操作：O(√n)
空间复杂度：O(n)

工程化考量：
1. 异常处理：检查输入参数的有效性
2. 可配置性：块大小可根据需要调整
3. 性能优化：使用块内和减少求和操作的复杂度
4. 鲁棒性：处理边界情况和特殊输入
5. 数据结构：使用列表存储原始数据和块内和
"""

class NumArray:
    def __init__(self, nums):
        """
        初始化NumArray对象
        
        Args:
            nums: 输入数组
        """
        self.nums = nums.copy()  # 复制原数组
        self.n = len(nums)
        # 计算块大小
        self.block_size = int(math.sqrt(self.n)) + 1
        # 计算块数量
        self.block_num = (self.n + self.block_size - 1) // self.block_size
        # 初始化块内和数组
        self.block_sum = [0] * self.block_num
        
        # 计算每个块的和
        for i in range(self.n):
            block_index = i // self.block_size
            self.block_sum[block_index] += nums[i]
    
    def update(self, index, val):
        """
        更新数组中的元素
        
        Args:
            index: 要更新的元素索引
            val: 新值
        
        Raises:
            IndexError: 当索引超出范围时
        """
        # 检查索引有效性
        if index < 0 or index >= self.n:
            raise IndexError("Index out of bounds")
        
        # 计算元素所在块
        block_index = index // self.block_size
        # 更新块内和
        self.block_sum[block_index] += val - self.nums[index]
        # 更新原数组
        self.nums[index] = val
    
    def sumRange(self, left, right):
        """
        计算区间和
        
        Args:
            left: 区间左边界（包含）
            right: 区间右边界（包含）
            
        Returns:
            区间内元素的和
            
        Raises:
            ValueError: 当区间无效时
        """
        # 检查参数有效性
        if left < 0 or right >= self.n or left > right:
            raise ValueError("Invalid range")
        
        sum_result = 0
        left_block = left // self.block_size
        right_block = right // self.block_size
        
        # 如果在同一个块内，暴力计算
        if left_block == right_block:
            for i in range(left, right + 1):
                sum_result += self.nums[i]
        else:
            # 计算左边不完整块
            for i in range(left, (left_block + 1) * self.block_size):
                sum_result += self.nums[i]
            
            # 计算中间完整块的和
            for i in range(left_block + 1, right_block):
                sum_result += self.block_sum[i]
            
            # 计算右边不完整块
            for i in range(right_block * self.block_size, right + 1):
                sum_result += self.nums[i]
        
        return sum_result

# 测试代码
if __name__ == "__main__":
    # 测试用例
    nums = [1, 3, 5]
    numArray = NumArray(nums)
    
    # 测试sumRange
    print(f"sumRange(0, 2) = {numArray.sumRange(0, 2)}")  # 输出: 9
    
    # 测试update
    numArray.update(1, 2)
    print(f"sumRange(0, 2) after update = {numArray.sumRange(0, 2)}")  # 输出: 8
    
    # 更多测试用例
    numArray.update(0, 10)
    print(f"sumRange(0, 0) = {numArray.sumRange(0, 0)}")  # 输出: 10
    print(f"sumRange(1, 2) = {numArray.sumRange(1, 2)}")  # 输出: 7