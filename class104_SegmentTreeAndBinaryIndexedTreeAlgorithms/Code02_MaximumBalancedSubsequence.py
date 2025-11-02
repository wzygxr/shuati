"""
LeetCode 2784. 平衡子序列的最大和 (Maximum Balanced Subsequence Sum)
题目链接: https://leetcode.cn/problems/maximum-balanced-subsequence-sum/

题目描述:
给定一个长度为n的数组nums，定义平衡子序列为满足以下条件的子序列：
对于子序列中任意两个下标i和j（i在j的左边），必须满足nums[j] - nums[i] >= j - i
求所有平衡子序列中元素和的最大值。

解题思路:
使用树状数组优化动态规划的方法解决此问题。
1. 首先将约束条件nums[j] - nums[i] >= j - i变形为nums[j] - j >= nums[i] - i
   这样我们定义一个新的指标值：nums[i] - i
2. 对于每个元素nums[i]，我们计算其指标值nums[i] - i
3. 使用树状数组维护以指标值为维度的动态规划状态
   dp[k]表示以指标值不超过sort[k]的元素结尾的平衡子序列的最大和
4. 遍历数组，对于每个元素，查询之前指标值不超过当前指标值的最大dp值
   然后更新当前指标值对应的dp值

时间复杂度分析:
- 离散化: O(n log n)
- 遍历更新: O(n log n)
- 总时间复杂度: O(n log n)
空间复杂度: O(n) 用于存储离散化数组和树状数组

工程化考量:
1. 异常处理: 处理空数组和边界情况
2. 性能优化: 使用离散化减少空间占用
3. 边界测试: 测试单元素、全正数、全负数等场景
4. 可读性: 清晰的变量命名和注释
"""

class FenwickTree:
    """树状数组类，用于高效维护前缀最大值"""
    
    def __init__(self, size):
        """
        初始化树状数组
        
        Args:
            size: 树状数组的大小
        """
        self.size = size
        self.tree = [-10**18] * (size + 1)  # 初始化为极小值
    
    def update(self, index, value):
        """
        更新树状数组
        
        Args:
            index: 要更新的位置
            value: 新的值
        """
        while index <= self.size:
            if value > self.tree[index]:
                self.tree[index] = value
            index += index & -index
    
    def query(self, index):
        """
        查询前缀最大值
        
        Args:
            index: 查询的结束位置
            
        Returns:
            前缀最大值
        """
        result = -10**18
        while index > 0:
            if self.tree[index] > result:
                result = self.tree[index]
            index -= index & -index
        return result

def maxBalancedSubsequenceSum(nums):
    """
    计算平衡子序列的最大和
    
    Args:
        nums: 输入数组
        
    Returns:
        平衡子序列的最大和
        
    Raises:
        ValueError: 如果输入为空数组
    """
    # 异常处理：空数组
    if not nums:
        raise ValueError("输入数组不能为空")
    
    n = len(nums)
    
    # 特殊情况：单元素数组
    if n == 1:
        return nums[0]
    
    # 计算指标值：nums[i] - i
    indicators = [nums[i] - i for i in range(n)]
    
    # 离散化处理
    sorted_indicators = sorted(set(indicators))
    rank_map = {val: idx + 1 for idx, val in enumerate(sorted_indicators)}
    
    # 初始化树状数组
    fenwick = FenwickTree(len(sorted_indicators))
    
    # 遍历数组进行动态规划
    for i in range(n):
        # 获取当前指标的排名
        k = rank_map[indicators[i]]
        
        # 查询之前指标值不超过当前指标值的最大和
        pre_max = fenwick.query(k)
        
        # 计算当前状态值
        current_val = nums[i]
        if pre_max > 0:
            current_val += pre_max
        
        # 更新树状数组
        fenwick.update(k, current_val)
    
    # 返回最大值
    return fenwick.query(len(sorted_indicators))

# 单元测试
def test_maxBalancedSubsequenceSum():
    """测试函数，验证算法正确性"""
    
    # 测试用例1: 正常情况
    nums1 = [3, 5, 6, 9]
    result1 = maxBalancedSubsequenceSum(nums1)
    print(f"测试用例1: {nums1} -> {result1}")
    assert result1 == 23, f"预期23，实际{result1}"
    
    # 测试用例2: 包含负数
    nums2 = [-2, -1, -3, -4]
    result2 = maxBalancedSubsequenceSum(nums2)
    print(f"测试用例2: {nums2} -> {result2}")
    assert result2 == -1, f"预期-1，实际{result2}"
    
    # 测试用例3: 混合正负数
    nums3 = [10, -2, 5, -3, 8]
    result3 = maxBalancedSubsequenceSum(nums3)
    print(f"测试用例3: {nums3} -> {result3}")
    
    # 测试用例4: 单元素
    nums4 = [7]
    result4 = maxBalancedSubsequenceSum(nums4)
    print(f"测试用例4: {nums4} -> {result4}")
    assert result4 == 7, f"预期7，实际{result4}"
    
    # 测试用例5: 全正数
    nums5 = [1, 2, 3, 4, 5]
    result5 = maxBalancedSubsequenceSum(nums5)
    print(f"测试用例5: {nums5} -> {result5}")
    
    print("所有测试用例通过！")

if __name__ == "__main__":
    # 运行测试
    test_maxBalancedSubsequenceSum()
    
    # 性能测试示例
    import time
    
    # 大规模数据测试
    large_nums = list(range(10000))
    start_time = time.time()
    result = maxBalancedSubsequenceSum(large_nums)
    end_time = time.time()
    
    print(f"大规模测试: 数组长度{len(large_nums)}，结果{result}，耗时{end_time - start_time:.4f}秒")
    
    # 边界情况测试
    try:
        maxBalancedSubsequenceSum([])
    except ValueError as e:
        print(f"边界测试通过: {e}")