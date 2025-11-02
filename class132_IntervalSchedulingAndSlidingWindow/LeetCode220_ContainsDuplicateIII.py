#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
LeetCode 220. 存在重复元素 III - Python实现

题目描述：
给你一个整数数组 nums 和两个整数 indexDiff 和 valueDiff 。
找出满足下述条件的下标对 (i, j)：
1. i != j
2. abs(i - j) <= indexDiff
3. abs(nums[i] - nums[j]) <= valueDiff
如果存在，返回 true ；否则，返回 false 。

解题思路：
这是一个滑动窗口结合有序数据结构的问题。

核心思想：
1. 使用滑动窗口维护最多k+1个元素（k=indexDiff）
2. 使用有序数据结构维护窗口内元素的有序性
3. 对于每个新元素，检查是否存在满足条件的旧元素

具体步骤：
1. 遍历数组，维护一个大小为k+1的滑动窗口
2. 对于窗口中的每个元素，使用有序数据结构的查找方法查找值域范围内最近的元素
3. 如果找到满足条件的元素，返回true
4. 当窗口大小超过k+1时，移除最早加入的元素

时间复杂度：O(n log k)
空间复杂度：O(k)

Python实现特点：
- 使用bisect模块进行二分查找
- 使用列表维护有序窗口
- 利用Python的简洁语法
"""

import bisect
from typing import List

class ContainsDuplicateIII:
    """
    存在重复元素 III 解决方案
    
    方法1: 使用有序列表和二分查找
    方法2: 使用桶排序思想（更高效）
    """
    
    @staticmethod
    def contains_nearby_almost_duplicate_bisect(nums: List[int], index_diff: int, value_diff: int) -> bool:
        """
        方法1: 使用有序列表和二分查找
        
        参数:
            nums: 整数数组
            index_diff: 下标差的最大值
            value_diff: 值差的最大值
            
        返回:
            是否存在满足条件的下标对
            
        时间复杂度: O(n log k)
        空间复杂度: O(k)
        """
        if not nums or index_diff < 0 or value_diff < 0:
            return False
        
        # 使用有序列表维护滑动窗口
        window = []
        
        for i, num in enumerate(nums):
            # 将当前元素插入到有序位置
            pos = bisect.bisect_left(window, num)
            
            # 检查插入位置附近的元素是否满足条件
            # 检查左侧元素
            if pos > 0 and abs(window[pos-1] - num) <= value_diff:
                return True
            # 检查右侧元素
            if pos < len(window) and abs(window[pos] - num) <= value_diff:
                return True
            
            # 插入当前元素
            bisect.insort(window, num)
            
            # 维护窗口大小不超过index_diff + 1
            if len(window) > index_diff + 1:
                # 移除最早加入的元素
                remove_num = nums[i - index_diff]
                remove_pos = bisect.bisect_left(window, remove_num)
                window.pop(remove_pos)
        
        return False
    
    @staticmethod
    def contains_nearby_almost_duplicate_bucket(nums: List[int], index_diff: int, value_diff: int) -> bool:
        """
        方法2: 使用桶排序思想（更高效）
        
        核心思想：
        1. 将数值范围划分为大小为(value_diff+1)的桶
        2. 每个元素根据其值分配到对应的桶中
        3. 如果同一个桶中有元素，说明值差<=value_diff
        4. 相邻桶中的元素也可能满足条件，需要额外检查
        
        参数:
            nums: 整数数组
            index_diff: 下标差的最大值
            value_diff: 值差的最大值
            
        返回:
            是否存在满足条件的下标对
            
        时间复杂度: O(n)
        空间复杂度: O(k)
        """
        if not nums or index_diff < 0 or value_diff < 0:
            return False
        
        # 桶的大小
        bucket_size = value_diff + 1
        
        # 使用字典存储桶，key为桶ID，value为桶中的元素值
        buckets = {}
        
        for i, num in enumerate(nums):
            # 计算当前元素所在的桶ID
            bucket_id = num // bucket_size if num >= 0 else (num + 1) // bucket_size - 1
            
            # 检查当前桶中是否有元素
            if bucket_id in buckets:
                return True
            
            # 检查左侧相邻桶
            if bucket_id - 1 in buckets and abs(buckets[bucket_id - 1] - num) <= value_diff:
                return True
            
            # 检查右侧相邻桶
            if bucket_id + 1 in buckets and abs(buckets[bucket_id + 1] - num) <= value_diff:
                return True
            
            # 将当前元素放入桶中
            buckets[bucket_id] = num
            
            # 维护窗口大小，移除超出范围的元素
            if i >= index_diff:
                remove_num = nums[i - index_diff]
                remove_bucket_id = remove_num // bucket_size if remove_num >= 0 else (remove_num + 1) // bucket_size - 1
                del buckets[remove_bucket_id]
        
        return False
    
    @staticmethod
    def contains_nearby_almost_duplicate(nums: List[int], index_diff: int, value_diff: int) -> bool:
        """
        主函数：根据value_diff选择最优方法
        
        当value_diff较小时，使用桶方法更高效
        当value_diff较大时，使用二分查找方法更稳定
        """
        if value_diff == 0:
            # 特殊情况：值差为0，转化为存在重复元素II问题
            return ContainsDuplicateIII.contains_nearby_duplicate_ii(nums, index_diff)
        elif value_diff < 100:  # 阈值可根据实际情况调整
            return ContainsDuplicateIII.contains_nearby_almost_duplicate_bucket(nums, index_diff, value_diff)
        else:
            return ContainsDuplicateIII.contains_nearby_almost_duplicate_bisect(nums, index_diff, value_diff)
    
    @staticmethod
    def contains_nearby_duplicate_ii(nums: List[int], index_diff: int) -> bool:
        """
        特殊情况：value_diff = 0，转化为存在重复元素II问题
        """
        window = set()
        
        for i, num in enumerate(nums):
            if num in window:
                return True
            
            window.add(num)
            
            if len(window) > index_diff:
                window.remove(nums[i - index_diff])
        
        return False


def test_contains_duplicate_iii():
    """
    测试函数
    """
    print("=== LeetCode 220 存在重复元素 III 测试 ===")
    
    # 测试用例1：基础测试
    nums1 = [1, 2, 3, 1]
    index_diff1 = 3
    value_diff1 = 0
    result1 = ContainsDuplicateIII.contains_nearby_almost_duplicate(nums1, index_diff1, value_diff1)
    print(f"测试用例1 - 预期: True, 实际: {result1}")
    
    # 测试用例2：值差测试
    nums2 = [1, 5, 9, 1, 5, 9]
    index_diff2 = 2
    value_diff2 = 3
    result2 = ContainsDuplicateIII.contains_nearby_almost_duplicate(nums2, index_diff2, value_diff2)
    print(f"测试用例2 - 预期: False, 实际: {result2}")
    
    # 测试用例3：边界测试
    nums3 = [1, 2, 3, 4]
    index_diff3 = 1
    value_diff3 = 1
    result3 = ContainsDuplicateIII.contains_nearby_almost_duplicate(nums3, index_diff3, value_diff3)
    print(f"测试用例3 - 预期: False, 实际: {result3}")
    
    # 测试用例4：负数测试
    nums4 = [-1, -2, -3, -1]
    index_diff4 = 3
    value_diff4 = 0
    result4 = ContainsDuplicateIII.contains_nearby_almost_duplicate(nums4, index_diff4, value_diff4)
    print(f"测试用例4 - 预期: True, 实际: {result4}")
    
    # 测试用例5：大值差测试
    nums5 = [1, 1000, 2000, 3000]
    index_diff5 = 3
    value_diff5 = 1000
    result5 = ContainsDuplicateIII.contains_nearby_almost_duplicate(nums5, index_diff5, value_diff5)
    print(f"测试用例5 - 实际结果: {result5}")
    
    # 测试不同方法的性能
    import time
    
    # 生成测试数据
    test_nums = list(range(1000))
    
    # 测试二分查找方法
    start = time.time()
    result_bisect = ContainsDuplicateIII.contains_nearby_almost_duplicate_bisect(test_nums, 50, 10)
    time_bisect = time.time() - start
    
    # 测试桶方法
    start = time.time()
    result_bucket = ContainsDuplicateIII.contains_nearby_almost_duplicate_bucket(test_nums, 50, 10)
    time_bucket = time.time() - start
    
    print(f"性能测试 - 二分查找方法: {time_bisect:.6f}秒")
    print(f"性能测试 - 桶方法: {time_bucket:.6f}秒")
    print(f"结果一致性: {result_bisect == result_bucket}")
    
    print("=== 测试完成 ===")


if __name__ == "__main__":
    test_contains_duplicate_iii()


"""
复杂度分析：
方法1（二分查找）：
- 时间复杂度：O(n log k) - 每个元素插入和删除需要O(log k)时间
- 空间复杂度：O(k) - 维护大小为k的窗口

方法2（桶排序）：
- 时间复杂度：O(n) - 每个元素处理时间为常数
- 空间复杂度：O(k) - 维护最多k个桶

算法选择策略：
- 当value_diff较小时（如<100），使用桶方法更高效
- 当value_diff较大时，使用二分查找方法更稳定
- 特殊情况value_diff=0时，使用简化版本

Python特性利用：
1. 使用bisect模块进行高效二分查找
2. 利用列表的有序插入特性
3. 使用字典实现桶数据结构
4. 提供多种实现方法供选择

工程化改进：
1. 根据输入参数自动选择最优算法
2. 添加完整的边界条件检查
3. 提供详细的测试用例
4. 包含性能对比测试

跨语言对比：
- Python版本比Java版本更简洁
- 桶方法在Python中实现更简单
- 利用Python的动态类型特性
"""