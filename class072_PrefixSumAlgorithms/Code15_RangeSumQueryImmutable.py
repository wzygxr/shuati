"""
区间和查询 - 不可变 (Range Sum Query - Immutable)

题目描述:
给定一个整数数组 nums，计算索引 left 和 right （包含 left 和 right）之间的元素的和，其中 left <= right。
实现 NumArray 类：
NumArray(int[] nums) 使用数组 nums 初始化对象
int sumRange(int left, int right) 返回数组 nums 中索引 left 和 right 之间的元素的总和，包含 left 和 right 两点

示例:
输入：
["NumArray", "sumRange", "sumRange", "sumRange"]
[[[-2, 0, 3, -5, 2, -1]], [0, 2], [2, 5], [0, 5]]
输出：
[null, 1, -1, -3]
解释：
NumArray numArray = new NumArray([-2, 0, 3, -5, 2, -1]);
numArray.sumRange(0, 2); // return (-2) + 0 + 3 = 1
numArray.sumRange(2, 5); // return 3 + (-5) + 2 + (-1) = -1
numArray.sumRange(0, 5); // return (-2) + 0 + 3 + (-5) + 2 + (-1) = -3

提示:
1 <= nums.length <= 10^4
-10^5 <= nums[i] <= 10^5
0 <= left <= right < nums.length
最多调用 10^4 次 sumRange 方法

题目链接: https://leetcode.com/problems/range-sum-query-immutable/

解题思路:
使用前缀和数组预处理原数组，使得每次查询区间和的操作时间复杂度为O(1)。
1. 计算前缀和数组 prefixSum，其中 prefixSum[i] 表示原数组 nums 中前 i 个元素的和
2. 对于区间查询 [left, right]，区间和为 prefixSum[right+1] - prefixSum[left]

时间复杂度:
- 初始化: O(n) - 预处理前缀和数组
- 查询: O(1) - 直接利用前缀和数组计算区间和
空间复杂度: O(n) - 存储前缀和数组
"""

class NumArray:
    def __init__(self, nums):
        """
        使用数组 nums 初始化对象，预处理计算前缀和数组
        
        Args:
            nums (List[int]): 输入数组
        """
        # 边界检查
        if not nums:
            self.prefix_sum = []
            return
        
        # 初始化前缀和数组，长度为 len(nums) + 1
        # prefix_sum[0] = 0 表示前0个元素的和为0
        # prefix_sum[i] 表示前i个元素的和，即 nums[0] + nums[1] + ... + nums[i-1]
        self.prefix_sum = [0] * (len(nums) + 1)
        
        # 计算前缀和
        for i in range(len(nums)):
            self.prefix_sum[i + 1] = self.prefix_sum[i] + nums[i]
    
    def sumRange(self, left, right):
        """
        返回数组 nums 中索引 left 和 right 之间的元素的总和，包含两个端点
        
        Args:
            left (int): 左边界索引
            right (int): 右边界索引
            
        Returns:
            int: 区间 [left, right] 的和
            
        Raises:
            ValueError: 如果索引参数无效
        """
        # 参数合法性检查
        if not self.prefix_sum:
            raise ValueError("数组为空")
        if left < 0 or left >= len(self.prefix_sum) - 1:
            raise ValueError(f"左边界索引无效: {left}")
        if right < 0 or right >= len(self.prefix_sum) - 1:
            raise ValueError(f"右边界索引无效: {right}")
        if left > right:
            raise ValueError(f"左边界不能大于右边界: {left} > {right}")
        
        # 利用前缀和数组计算区间和
        # [left, right] 的和 = prefix_sum[right+1] - prefix_sum[left]
        return self.prefix_sum[right + 1] - self.prefix_sum[left]


# 测试用例
if __name__ == "__main__":
    # 创建测试用例数组
    nums = [-2, 0, 3, -5, 2, -1]
    
    # 初始化 NumArray 对象
    num_array = NumArray(nums)
    
    # 测试区间和查询
    # 测试用例1: [0, 2] 预期输出: 1
    print(f"区间 [0, 2] 的和: {num_array.sumRange(0, 2)}")
    
    # 测试用例2: [2, 5] 预期输出: -1
    print(f"区间 [2, 5] 的和: {num_array.sumRange(2, 5)}")
    
    # 测试用例3: [0, 5] 预期输出: -3
    print(f"区间 [0, 5] 的和: {num_array.sumRange(0, 5)}")
    
    # 测试边界情况
    # 测试用例4: [3, 3] 预期输出: -5
    print(f"区间 [3, 3] 的和: {num_array.sumRange(3, 3)}")
    
    # 测试空数组
    try:
        empty_array = NumArray([])
        empty_array.sumRange(0, 0)
    except ValueError as e:
        print(f"空数组测试: {e}")