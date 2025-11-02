#!/usr/bin/env python3
"""
LeetCode 2444. 统计定界子数组的数目 (Count Subarrays With Fixed Bounds) - Python版本

题目来源：https://leetcode.cn/problems/count-subarrays-with-fixed-bounds/

题目描述：
给你一个整数数组 nums 和两个整数 minK 以及 maxK。
nums 的定界子数组是满足下述条件的一个子数组：
- 子数组中的最小值等于 minK。
- 子数组中的最大值等于 maxK。
返回定界子数组的数目。
子数组是数组中的一个连续部分。

算法思路：
这个问题可以通过以下方法解决：
1. 滑动窗口：维护满足条件的窗口
2. 稀疏表：预处理范围最值查询，然后枚举所有可能的子数组
3. 数学方法：通过计算包含minK和maxK的子数组数量

使用稀疏表的方法：
1. 预处理稀疏表用于范围最值查询
2. 对于每个左端点，使用二分查找找到满足条件的右端点范围

时间复杂度：
- 稀疏表预处理：O(n log n)
- 查询：O(1)
- 总时间复杂度：O(n²)（最坏情况）或O(n log n)（优化后）
- 空间复杂度：O(n log n)

应用场景：
1. 数据分析：统计满足特定条件的连续数据段
2. 金融：分析价格波动区间
3. 信号处理：检测特定幅度的信号段

相关题目：
1. LeetCode 315. 计算右侧小于当前元素的个数
2. LeetCode 743. 网络延迟时间
3. LeetCode 1584. 连接所有点的最小费用
"""

import math
import random
import time

class SparseTable:
    """稀疏表类，用于范围最值查询"""
    
    def __init__(self, data):
        """
        构造函数
        :param data: 输入数组
        """
        if not data:
            raise ValueError("输入数组不能为空")
        
        self.data = data[:]
        self.n = len(data)
        
        # 预计算log表
        self._precompute_log_table()
        
        # 构建稀疏表
        self._build_sparse_table()
    
    def _precompute_log_table(self):
        """预计算log2值表"""
        self.log_table = [0] * (self.n + 1)
        self.log_table[1] = 0
        for i in range(2, self.n + 1):
            self.log_table[i] = self.log_table[i // 2] + 1
    
    def _build_sparse_table(self):
        """构建稀疏表"""
        k = self.log_table[self.n] + 1
        
        # 初始化稀疏表
        self.st_min = [[0 for _ in range(self.n)] for _ in range(k)]
        self.st_max = [[0 for _ in range(self.n)] for _ in range(k)]
        
        # 初始化k=0的情况（长度为1的区间）
        for i in range(self.n):
            self.st_min[0][i] = self.data[i]
            self.st_max[0][i] = self.data[i]
        
        # 动态规划构建其他k值
        for j in range(1, k):
            for i in range(self.n - (1 << j) + 1):
                prev_len = 1 << (j - 1)
                # 范围最小值查询
                self.st_min[j][i] = min(self.st_min[j-1][i], self.st_min[j-1][i + prev_len])
                # 范围最大值查询
                self.st_max[j][i] = max(self.st_max[j-1][i], self.st_max[j-1][i + prev_len])
    
    def query_min(self, left, right):
        """
        范围最小值查询
        时间复杂度：O(1)
        :param left: 左边界（包含）
        :param right: 右边界（包含）
        :return: 区间内的最小值
        """
        if left < 0 or right >= self.n or left > right:
            raise ValueError("查询范围无效")
        
        length = right - left + 1
        k = self.log_table[length]
        
        return min(self.st_min[k][left], self.st_min[k][right - (1 << k) + 1])
    
    def query_max(self, left, right):
        """
        范围最大值查询
        时间复杂度：O(1)
        :param left: 左边界（包含）
        :param right: 右边界（包含）
        :return: 区间内的最大值
        """
        if left < 0 or right >= self.n or left > right:
            raise ValueError("查询范围无效")
        
        length = right - left + 1
        k = self.log_table[length]
        
        return max(self.st_max[k][left], self.st_max[k][right - (1 << k) + 1])

def count_subarrays_with_sparse_table(nums, minK, maxK):
    """
    方法1：使用稀疏表的解法
    时间复杂度：O(n log n)
    空间复杂度：O(n log n)
    :param nums: 输入数组
    :param minK: 最小值边界
    :param maxK: 最大值边界
    :return: 定界子数组的数目
    """
    n = len(nums)
    if n == 0:
        return 0
    
    # 构建稀疏表
    st = SparseTable(nums)
    
    count = 0
    
    # 对于每个左端点，找到满足条件的右端点范围
    for i in range(n):
        # 跳过不满足边界条件的元素
        if nums[i] < minK or nums[i] > maxK:
            continue
        
        # 使用二分查找找到满足条件的右端点范围
        left_bound = i
        right_bound = n - 1
        valid_right_start = -1
        valid_right_end = -1
        
        # 找到第一个满足条件的右端点
        low, high = i, n - 1
        while low <= high:
            mid = (low + high) // 2
            min_val = st.query_min(i, mid)
            max_val = st.query_max(i, mid)
            
            if min_val == minK and max_val == maxK:
                valid_right_start = mid
                high = mid - 1
            elif min_val < minK or max_val > maxK:
                low = mid + 1
            else:
                low = mid + 1
        
        if valid_right_start == -1:
            continue
        
        # 找到最后一个满足条件的右端点
        low, high = valid_right_start, n - 1
        while low <= high:
            mid = (low + high) // 2
            min_val = st.query_min(i, mid)
            max_val = st.query_max(i, mid)
            
            if min_val == minK and max_val == maxK:
                valid_right_end = mid
                low = mid + 1
            elif min_val < minK or max_val > maxK:
                high = mid - 1
            else:
                low = mid + 1
        
        # 计算满足条件的子数组数量
        if valid_right_start != -1 and valid_right_end != -1:
            count += valid_right_end - valid_right_start + 1
    
    return count

def count_subarrays_optimized(nums, minK, maxK):
    """
    方法2：优化的数学解法（最优解）
    时间复杂度：O(n)
    空间复杂度：O(1)
    :param nums: 输入数组
    :param minK: 最小值边界
    :param maxK: 最大值边界
    :return: 定界子数组的数目
    """
    result = 0
    bad_idx = -1
    left_idx = -1
    right_idx = -1
    
    for i in range(len(nums)):
        # 如果当前元素超出边界，则更新bad_idx
        if not (minK <= nums[i] <= maxK):
            bad_idx = i
        
        # 更新最近的minK位置
        if nums[i] == minK:
            left_idx = i
        
        # 更新最近的maxK位置
        if nums[i] == maxK:
            right_idx = i
        
        # 计算以当前位置结尾的有效子数组数量
        result += max(0, min(left_idx, right_idx) - bad_idx)
    
    return result

def count_subarrays_sliding_window(nums, minK, maxK):
    """
    方法3：滑动窗口解法
    时间复杂度：O(n)
    空间复杂度：O(1)
    :param nums: 输入数组
    :param minK: 最小值边界
    :param maxK: 最大值边界
    :return: 定界子数组的数目
    """
    result = 0
    start = 0
    min_pos = -1
    max_pos = -1
    
    for i in range(len(nums)):
        # 如果当前元素超出边界，重置窗口
        if nums[i] < minK or nums[i] > maxK:
            start = i + 1
            min_pos = -1
            max_pos = -1
        else:
            # 更新minK和maxK的位置
            if nums[i] == minK:
                min_pos = i
            if nums[i] == maxK:
                max_pos = i
            
            # 如果已经找到minK和maxK，计算有效子数组数量
            if min_pos != -1 and max_pos != -1:
                result += min(min_pos, max_pos) - start + 1
    
    return result

def test_count_subarrays_with_fixed_bounds():
    """测试函数"""
    print("=== 测试 LeetCode 2444. 统计定界子数组的数目 ===")
    
    # 测试用例1
    nums1 = [1, 3, 5, 2, 7, 5]
    minK1, maxK1 = 1, 5
    print("测试用例1:")
    print("数组:", nums1)
    print("minK:", minK1, ", maxK:", maxK1)
    print("稀疏表解法结果:", count_subarrays_with_sparse_table(nums1, minK1, maxK1))
    print("优化解法结果:", count_subarrays_optimized(nums1, minK1, maxK1))
    print("滑动窗口解法结果:", count_subarrays_sliding_window(nums1, minK1, maxK1))
    print("期望结果: 2")
    print()
    
    # 测试用例2
    nums2 = [1, 1, 1, 1]
    minK2, maxK2 = 1, 1
    print("测试用例2:")
    print("数组:", nums2)
    print("minK:", minK2, ", maxK:", maxK2)
    print("稀疏表解法结果:", count_subarrays_with_sparse_table(nums2, minK2, maxK2))
    print("优化解法结果:", count_subarrays_optimized(nums2, minK2, maxK2))
    print("滑动窗口解法结果:", count_subarrays_sliding_window(nums2, minK2, maxK2))
    print("期望结果: 10")
    print()
    
    # 测试用例3
    nums3 = [1, 3, 5, 2, 7, 5, 1, 3, 5]
    minK3, maxK3 = 1, 5
    print("测试用例3:")
    print("数组:", nums3)
    print("minK:", minK3, ", maxK:", maxK3)
    print("稀疏表解法结果:", count_subarrays_with_sparse_table(nums3, minK3, maxK3))
    print("优化解法结果:", count_subarrays_optimized(nums3, minK3, maxK3))
    print("滑动窗口解法结果:", count_subarrays_sliding_window(nums3, minK3, maxK3))
    print()
    
    # 性能测试
    print("=== 性能测试 ===")
    random.seed(42)
    array_size = 10000
    nums = [random.randint(1, 100) for _ in range(array_size)]
    minK, maxK = 10, 50
    
    start_time = time.time()
    result1 = count_subarrays_optimized(nums, minK, maxK)
    end_time = time.time()
    print("优化解法处理{}个元素时间: {:.2f} ms, 结果: {}".format(
        array_size, (end_time - start_time) * 1000, result1))
    
    start_time = time.time()
    result2 = count_subarrays_sliding_window(nums, minK, maxK)
    end_time = time.time()
    print("滑动窗口解法处理{}个元素时间: {:.2f} ms, 结果: {}".format(
        array_size, (end_time - start_time) * 1000, result2))

if __name__ == "__main__":
    test_count_subarrays_with_fixed_bounds()