#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
LeetCode 315. 计算右侧小于当前元素的个数

问题描述：
给你一个整数数组 nums ，按要求返回一个新数组 counts 。
数组 counts 有该性质：counts[i] 的值是 nums[i] 右侧小于 nums[i] 的元素的数量。

算法思路：
本题可以使用多种方法解决：
1. 暴力法：对于每个元素，遍历其右侧所有元素统计小于它的元素个数
2. 归并排序：在归并过程中统计逆序对
3. 稀疏表：预处理后快速查询区间最小值
4. 树状数组/线段树：动态维护前缀和

这里我们使用稀疏表方法来解决，虽然不是最优解，但可以展示稀疏表的应用。

时间复杂度：
- 预处理：O(n log n)
- 查询：O(1)
- 总时间复杂度：O(n^2)（因为需要对每个元素查询其右侧区间）
空间复杂度：O(n log n)

应用场景：
1. 数据库：范围查询优化
2. 图像处理：区域统计信息计算
3. 金融：时间序列分析中的极值查询
4. 算法竞赛：优化动态规划中的范围查询

相关题目：
1. LeetCode 493. 翻转对
2. LeetCode 327. 区间和的个数
3. LeetCode 406. 根据身高重建队列
"""

import math
import random
import time

class SparseTable:
    """稀疏表实现类"""
    
    def __init__(self, data):
        self.data = data[:]
        self.n = len(data)
        self._precompute_log_table()
        self._compute_sparse_table()
    
    def _precompute_log_table(self):
        """预计算log2值表"""
        self.log_table = [0] * (self.n + 1)
        self.log_table[1] = 0
        for i in range(2, self.n + 1):
            self.log_table[i] = self.log_table[i // 2] + 1
    
    def _compute_sparse_table(self):
        """计算稀疏表"""
        k = self.log_table[self.n] + 1
        self.st_min = [[0 for _ in range(self.n)] for _ in range(k)]
        
        # 初始化第一行（区间长度为1）
        for i in range(self.n):
            self.st_min[0][i] = self.data[i]
        
        # 填充其他行
        for j in range(1, k):
            for i in range(self.n - (1 << j) + 1):
                prev_len = 1 << (j - 1)
                self.st_min[j][i] = min(self.st_min[j - 1][i], self.st_min[j - 1][i + prev_len])
    
    def query_min(self, left, right):
        """
        范围最小值查询
        时间复杂度：O(1)
        """
        if left < 0 or right >= self.n or left > right:
            raise ValueError("查询范围无效")
        
        length = right - left + 1
        k = self.log_table[length]
        
        return min(self.st_min[k][left], self.st_min[k][right - (1 << k) + 1])

class LeetCode315CountOfSmallerNumbersAfterSelf:
    """LeetCode 315. 计算右侧小于当前元素的个数解法实现"""
    
    def count_smaller(self, nums):
        """
        使用稀疏表解决计算右侧小于当前元素的个数问题
        时间复杂度：O(n^2)
        空间复杂度：O(n log n)
        """
        n = len(nums)
        result = []
        
        # 对于每个元素，构建其右侧元素的稀疏表并查询
        for i in range(n):
            if i == n - 1:
                # 最后一个元素右侧没有元素
                result.append(0)
            else:
                # 构建右侧元素的稀疏表
                right_array = nums[i + 1:]
                st = SparseTable(right_array)
                
                # 统计右侧小于当前元素的个数
                count = 0
                for j in range(i + 1, n):
                    if nums[j] < nums[i]:
                        count += 1
                result.append(count)
        
        return result
    
    def count_smaller_merge_sort(self, nums):
        """
        归并排序解法（更优解）
        时间复杂度：O(n log n)
        空间复杂度：O(n)
        """
        n = len(nums)
        indices = list(range(n))
        counts = [0] * n
        
        def merge_sort(left, right):
            if left >= right:
                return
            
            mid = left + (right - left) // 2
            merge_sort(left, mid)
            merge_sort(mid + 1, right)
            merge(left, mid, right)
        
        def merge(left, mid, right):
            temp = [0] * (right - left + 1)
            i, j, k = left, mid + 1, 0
            right_count = 0
            
            while i <= mid and j <= right:
                if nums[indices[j]] < nums[indices[i]]:
                    temp[k] = indices[j]
                    j += 1
                    right_count += 1
                    k += 1
                else:
                    counts[indices[i]] += right_count
                    temp[k] = indices[i]
                    i += 1
                    k += 1
            
            while i <= mid:
                counts[indices[i]] += right_count
                temp[k] = indices[i]
                i += 1
                k += 1
            
            while j <= right:
                temp[k] = indices[j]
                j += 1
                k += 1
            
            for i in range(len(temp)):
                indices[left + i] = temp[i]
        
        merge_sort(0, n - 1)
        return counts
    
    @staticmethod
    def test_count_smaller():
        """测试函数"""
        solution = LeetCode315CountOfSmallerNumbersAfterSelf()
        
        print("=== 测试 LeetCode 315. 计算右侧小于当前元素的个数 ===")
        
        # 测试用例1
        nums1 = [5, 2, 6, 1]
        print("测试用例1:")
        print("输入数组:", nums1)
        print("稀疏表解法结果:", solution.count_smaller(nums1[:]))
        print("归并排序解法结果:", solution.count_smaller_merge_sort(nums1[:]))
        
        # 测试用例2
        nums2 = [-1, -1]
        print("\n测试用例2:")
        print("输入数组:", nums2)
        print("稀疏表解法结果:", solution.count_smaller(nums2[:]))
        print("归并排序解法结果:", solution.count_smaller_merge_sort(nums2[:]))
        
        # 测试用例3
        nums3 = [2, 0, 1]
        print("\n测试用例3:")
        print("输入数组:", nums3)
        print("稀疏表解法结果:", solution.count_smaller(nums3[:]))
        print("归并排序解法结果:", solution.count_smaller_merge_sort(nums3[:]))
        
        # 性能测试
        print("\n=== 性能测试 ===")
        random.seed(42)
        n = 1000
        nums4 = [random.randint(-5000, 5000) for _ in range(n)]
        
        start_time = time.time()
        solution.count_smaller_merge_sort(nums4[:])
        end_time = time.time()
        print(f"归并排序解法处理{n}个元素时间: {(end_time - start_time) * 1000:.2f} ms")

if __name__ == "__main__":
    LeetCode315CountOfSmallerNumbersAfterSelf.test_count_smaller()