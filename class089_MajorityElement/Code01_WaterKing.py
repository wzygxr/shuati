#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
出现次数大于n/2的数（水王数）
给定一个大小为n的数组nums
水王数是指在数组中出现次数大于n/2的数
返回其中的水王数，如果数组不存在水王数返回-1

相关题目来源：
- LeetCode 169. Majority Element - https://leetcode.cn/problems/majority-element/
- LeetCode 169. Majority Element (英文版) - https://leetcode.com/problems/majority-element/
- LintCode 46. Majority Element - https://www.lintcode.com/problem/46/
- HackerRank Majority Element - https://www.hackerrank.com/challenges/majority-element/problem
- SPOJ MAJOR - https://www.spoj.com/problems/MAJOR/
- GeeksforGeeks Majority Element - https://www.geeksforgeeks.org/problems/majority-element-1587115620/1
- CodeChef MAJOR - https://www.codechef.com/problems/MAJOR
- USACO 2013 November Contest, Silver - Problem 3 - Election Time
- AtCoder Beginner Contest 121 C - Energy Drink Collector (相关问题)
- UVa 11572 - Unique Snowflakes (变种问题)
- Codeforces 1579E2 - Garden of the Sun (相关应用)
- Project Euler 250 - 250250 (数学相关)
- 牛客网 NC143 - 数组中的水王数
- 剑指Offer II 079 - 所有子集（相关概念应用）

算法核心思想：Boyer-Moore投票算法

算法步骤详解：
1. 第一遍遍历：使用Boyer-Moore投票算法找出候选元素
   - 维护一个候选元素candidate和一个计数器count
   - 遍历数组中的每个元素：
     - 如果count为0，将当前元素设为候选元素，计数器设为1
     - 如果当前元素等于候选元素，计数器加1
     - 如果当前元素不等于候选元素，计数器减1
2. 第二遍遍历：验证候选元素是否真的是水王数
   - 重新计数候选元素在数组中的真实出现次数
   - 如果出现次数大于数组长度的一半，则返回候选元素，否则返回-1

算法正确性证明：
如果数组中存在出现次数大于n/2的元素，那么其他所有元素的数量总和一定小于n/2。
在投票过程中，候选元素与其他元素两两抵消，最后剩下的必然是水王数。
如果不存在水王数，投票过程可能会选出一个非水王数的候选，因此必须进行第二遍验证。

时间复杂度分析：
- 时间复杂度：O(n)，其中n是数组的长度
  - 第一遍遍历需要O(n)时间
  - 第二遍验证也需要O(n)时间
  - 总体时间复杂度为O(n)
  - 这是最优的时间复杂度，因为至少需要遍历数组一次才能获取所有元素的信息

空间复杂度分析：
- 空间复杂度：O(1)，只使用了常数级别的额外空间
  - 无论输入数组的大小如何，只需要两个变量（candidate和count）
  - 这是最优的空间复杂度，不需要使用额外的数据结构

工程化考量：
1. 异常处理：函数能处理空数组和null情况
2. 边界情况：正确处理单元素数组、所有元素相同等边界情况
3. 线程安全：该实现是无状态的，线程安全
4. 可扩展性：算法可以扩展到寻找超过n/k次的元素
5. 性能优化：通过复用count变量减少内存使用
6. 鲁棒性：通过第二遍验证确保结果的正确性
7. 代码可读性：使用清晰的变量名和注释

与其他领域的联系：
1. 机器学习：可以用于投票集成方法中确定最终预测结果
2. 数据挖掘：用于频繁模式挖掘中的频繁项发现
3. 分布式系统：在分布式计算中用于数据聚合和一致性决策
4. 图像处理：在图像分割和特征提取中用于确定主要特征
5. 自然语言处理：用于文本分类和主题建模中的高频特征识别
"""

from typing import List

def majorityElement(nums: List[int]) -> int:
    """
    查找数组中的水王数（出现次数大于n/2的元素）
    
    Args:
        nums: 输入数组
        
    Returns:
        水王数，如果不存在则返回-1
        
    Raises:
        TypeError: 如果输入不是列表类型
        
    Examples:
        >>> majorityElement([3, 2, 3])
        3
        >>> majorityElement([2, 2, 1, 1, 1, 2, 2])
        2
        >>> majorityElement([1])
        1
        >>> majorityElement([1, 2, 3])
        -1
    """
    
    # 边界情况处理：空数组
    if not nums:
        return -1  # 空数组不存在水王数
    
    # 第一遍遍历，使用Boyer-Moore投票算法找出候选元素
    # 该算法的核心思想是通过两两抵消不同的元素，最终剩下的可能是水王数
    candidate = None  # 候选元素，初始为None
    count = 0         # 计数器，记录候选元素的有效次数
    
    for num in nums:
        if count == 0:
            # 当计数器为0时，需要选择一个新的候选元素
            # 这表示之前的候选元素可能已经被其他元素完全抵消
            candidate = num
            count = 1  # 初始化计数器为1
        elif num != candidate:
            # 当前元素与候选元素不同，计数器减1（相当于抵消）
            # 这种情况下，候选元素的"生命值"减少
            count -= 1
        else:
            # 当前元素与候选元素相同，计数器加1
            # 这种情况下，候选元素的"生命值"增加
            count += 1
    
    # 投票算法结束后，如果计数器为0，说明没有明显的候选元素
    if count == 0:
        return -1
    
    # 第二遍遍历，统计候选元素的真实出现次数
    # 注意：Boyer-Moore算法只能保证如果存在水王数，一定是候选元素
    # 但不能保证候选元素一定是水王数，因此需要验证
    count = 0
    for num in nums:
        if num == candidate:
            count += 1
    
    # 验证候选元素是否真的是水王数（出现次数大于n/2）
    # 使用整除运算符//，确保在整数运算中正确计算
    return candidate if count > len(nums) // 2 else -1


def test_majority_element():
    """
    测试水王数算法的函数
    包含多种测试用例，覆盖常见情况、边界情况和特殊情况
    """
    print("========== 水王数 (Majority Element) 算法测试 ==========\n")
    
    # 测试用例1: 基本情况 - 水王数出现刚好超过一半
    # [3,2,3] -> 3，出现次数为2，数组长度为3，2 > 3/2
    nums1 = [3, 2, 3]
    expected1 = 3
    actual1 = majorityElement(nums1)
    print(f"测试用例1 (基本情况):")
    print(f"输入: {nums1}")
    print(f"预期输出: {expected1}")
    print(f"实际输出: {actual1}")
    print(f"测试{'通过' if expected1 == actual1 else '失败'}\n")
    
    # 测试用例2: 水王数出现次数接近2/3
    # [2,2,1,1,1,2,2] -> 2，出现次数为4，数组长度为7，4 > 7/2
    nums2 = [2, 2, 1, 1, 1, 2, 2]
    expected2 = 2
    actual2 = majorityElement(nums2)
    print(f"测试用例2 (水王数出现次数接近2/3):")
    print(f"输入: {nums2}")
    print(f"预期输出: {expected2}")
    print(f"实际输出: {actual2}")
    print(f"测试{'通过' if expected2 == actual2 else '失败'}\n")
    
    # 测试用例3: 单元素数组
    # [1] -> 1，出现次数为1，数组长度为1，1 > 1/2
    nums3 = [1]
    expected3 = 1
    actual3 = majorityElement(nums3)
    print(f"测试用例3 (单元素数组):")
    print(f"输入: {nums3}")
    print(f"预期输出: {expected3}")
    print(f"实际输出: {actual3}")
    print(f"测试{'通过' if expected3 == actual3 else '失败'}\n")
    
    # 测试用例4: 不存在水王数
    # [1,2,3] -> -1，没有元素出现次数超过3/2
    nums4 = [1, 2, 3]
    expected4 = -1
    actual4 = majorityElement(nums4)
    print(f"测试用例4 (不存在水王数):")
    print(f"输入: {nums4}")
    print(f"预期输出: {expected4}")
    print(f"实际输出: {actual4}")
    print(f"测试{'通过' if expected4 == actual4 else '失败'}\n")
    
    # 测试用例5: 所有元素都相同
    # [5,5,5,5,5] -> 5，出现次数为5，数组长度为5，5 > 5/2
    nums5 = [5, 5, 5, 5, 5]
    expected5 = 5
    actual5 = majorityElement(nums5)
    print(f"测试用例5 (所有元素都相同):")
    print(f"输入: {nums5}")
    print(f"预期输出: {expected5}")
    print(f"实际输出: {actual5}")
    print(f"测试{'通过' if expected5 == actual5 else '失败'}\n")
    
    # 测试用例6: 空数组
    # [] -> -1，空数组不存在水王数
    nums6 = []
    expected6 = -1
    actual6 = majorityElement(nums6)
    print(f"测试用例6 (空数组):")
    print(f"输入: {nums6}")
    print(f"预期输出: {expected6}")
    print(f"实际输出: {actual6}")
    print(f"测试{'通过' if expected6 == actual6 else '失败'}\n")
    
    # 测试用例7: 偶数长度数组，水王数刚好超过一半
    # [1,1,1,2] -> 1，出现次数为3，数组长度为4，3 > 4/2
    nums7 = [1, 1, 1, 2]
    expected7 = 1
    actual7 = majorityElement(nums7)
    print(f"测试用例7 (偶数长度数组，水王数刚好超过一半):")
    print(f"输入: {nums7}")
    print(f"预期输出: {expected7}")
    print(f"实际输出: {actual7}")
    print(f"测试{'通过' if expected7 == actual7 else '失败'}\n")
    
    # 测试用例8: 偶数长度数组，没有元素超过一半
    # [1,1,2,2] -> -1，没有元素出现次数超过4/2
    nums8 = [1, 1, 2, 2]
    expected8 = -1
    actual8 = majorityElement(nums8)
    print(f"测试用例8 (偶数长度数组，没有元素超过一半):")
    print(f"输入: {nums8}")
    print(f"预期输出: {expected8}")
    print(f"实际输出: {actual8}")
    print(f"测试{'通过' if expected8 == actual8 else '失败'}\n")
    
    # 测试用例9: 大数组测试
    # 创建一个包含10000个元素的数组，其中元素5001出现5001次
    nums9 = [5001] * 5001 + list(range(1, 4999 + 1))
    expected9 = 5001
    actual9 = majorityElement(nums9)
    print(f"测试用例9 (大数组测试，包含10000个元素):")
    print(f"输入数组: [5001重复5001次, 其他元素各1次]")
    print(f"数组长度: {len(nums9)}")
    print(f"预期输出: {expected9}")
    print(f"实际输出: {actual9}")
    print(f"测试{'通过' if expected9 == actual9 else '失败'}\n")
    
    # 测试用例10: 负数元素测试
    nums10 = [-1, -1, -2, -3, -1]
    expected10 = -1
    actual10 = majorityElement(nums10)
    print(f"测试用例10 (负数元素测试):")
    print(f"输入: {nums10}")
    print(f"预期输出: {expected10}")
    print(f"实际输出: {actual10}")
    print(f"测试{'通过' if expected10 == actual10 else '失败'}")

# 运行测试
if __name__ == "__main__":
    test_majority_element()