#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
LeetCode 370. 区间加法

问题描述：
假设你有一个长度为 n 的数组，初始情况下所有的数字均为 0，
你将会被给出 k 个更新操作。其中，每个操作会被表示为一个三元组：
[startIndex, endIndex, inc]，你需要将子数组 A[startIndex ... endIndex]
（包括 startIndex 和 endIndex）增加 inc。
请你返回 k 次操作后的数组。

算法思路：
使用差分数组来优化区间更新操作。差分数组的核心思想是：
1. 对于区间 [l, r] 增加 val，我们只需要在差分数组的 l 位置加 val，
   在 r+1 位置减 val
2. 最后通过计算差分数组的前缀和来得到最终结果

时间复杂度：
- 区间更新：O(1)
- 获取结果数组：O(n)
- 总时间复杂度：O(k + n)
空间复杂度：O(n)

应用场景：
1. 数组操作优化：批量更新处理
2. 前缀和计算：快速计算区间和
3. 算法竞赛：区间操作问题的优化

相关题目：
1. LeetCode 1094. 拼车
2. LeetCode 1109. 航班预订统计
3. LeetCode 1893. 检查是否区域内所有整数都被覆盖
"""

import time

class LeetCode370RangeAddition:
    """LeetCode 370. 区间加法解法实现"""
    
    def get_modified_array(self, length, updates):
        """
        使用差分数组解决区间加法问题
        时间复杂度：O(k + n)
        空间复杂度：O(n)
        :param length: 数组长度
        :param updates: 更新操作数组，每个操作是 [startIndex, endIndex, inc]
        :return: k 次操作后的数组
        """
        # 创建差分数组，大小为 length + 1 便于处理边界
        diff = [0] * (length + 1)
        
        # 处理每个更新操作
        for update in updates:
            start, end, inc = update[0], update[1], update[2]
            
            # 在差分数组中标记区间更新
            diff[start] += inc
            if end + 1 < length:
                diff[end + 1] -= inc
        
        # 通过计算差分数组的前缀和来得到最终结果
        result = [0] * length
        result[0] = diff[0]
        for i in range(1, length):
            result[i] = result[i - 1] + diff[i]
        
        return result
    
    def get_modified_array_brute_force(self, length, updates):
        """
        暴力解法（用于对比）
        时间复杂度：O(k * n)
        空间复杂度：O(1)
        """
        result = [0] * length
        
        # 处理每个更新操作
        for update in updates:
            start, end, inc = update[0], update[1], update[2]
            
            # 直接更新区间内的每个元素
            for i in range(start, end + 1):
                result[i] += inc
        
        return result
    
    @staticmethod
    def test_range_addition():
        """测试函数"""
        solution = LeetCode370RangeAddition()
        
        print("=== 测试 LeetCode 370. 区间加法 ===")
        
        # 测试用例1
        length1 = 5
        updates1 = [[1, 3, 2], [2, 4, 3], [0, 2, -2]]
        print("测试用例1:")
        print("数组长度:", length1)
        print("更新操作:", updates1)
        print("差分数组结果:", solution.get_modified_array(length1, updates1))
        print("暴力解法结果:", solution.get_modified_array_brute_force(length1, updates1))
        
        # 测试用例2
        length2 = 10
        updates2 = [[2, 4, 6], [0, 6, -2], [5, 8, 1]]
        print("\n测试用例2:")
        print("数组长度:", length2)
        print("更新操作:", updates2)
        print("差分数组结果:", solution.get_modified_array(length2, updates2))
        print("暴力解法结果:", solution.get_modified_array_brute_force(length2, updates2))
        
        # 性能测试
        print("\n=== 性能测试 ===")
        length3 = 10000
        k = 1000
        updates3 = []
        for i in range(k):
            start = i % length3
            end = min(start + 100, length3 - 1)
            inc = i % 10 - 5
            updates3.append([start, end, inc])
        
        start_time = time.time()
        solution.get_modified_array(length3, updates3)
        end_time = time.time()
        print(f"差分数组法处理长度为{length3}的数组，{k}次更新操作时间: {(end_time - start_time) * 1000:.2f} ms")
        
        start_time = time.time()
        solution.get_modified_array_brute_force(length3, updates3)
        end_time = time.time()
        print(f"暴力解法处理长度为{length3}的数组，{k}次更新操作时间: {(end_time - start_time) * 1000:.2f} ms")

if __name__ == "__main__":
    LeetCode370RangeAddition.test_range_addition()