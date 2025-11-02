#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
线段树相关题目测试文件
测试所有线段树实现的正确性
"""

import time
from leetcode307_segment_tree import LeetCode307_SegmentTree
from leetcode315_segment_tree import LeetCode315_SegmentTree
from leetcode493_segment_tree import LeetCode493_SegmentTree
from leetcode327_segment_tree import LeetCode327_SegmentTree
from poj3468_segment_tree import POJ3468_SegmentTree
from hdu1166_segment_tree import HDU1166_SegmentTree
from luogu_p3372_segment_tree import LuoguP3372_SegmentTree
from codeforces_52c_segment_tree import Codeforces52C_SegmentTree


def test_leetcode307():
    """测试LeetCode 307. 区域和检索 - 数组可修改"""
    print("测试LeetCode 307. 区域和检索 - 数组可修改...")
    
    # 测试用例1
    nums1 = [1, 3, 5]
    numArray1 = LeetCode307_SegmentTree(nums1)
    
    result1 = numArray1.sumRange(0, 2)
    print(f"输入数组: {nums1}")
    print(f"查询区间[0,2]的和: {result1}")
    assert result1 == 9, f"测试失败，期望9，实际{result1}"
    
    # 更新索引1的值为2
    numArray1.update(1, 2)
    result2 = numArray1.sumRange(0, 2)
    print(f"将索引1的值更新为2后，查询区间[0,2]的和: {result2}")
    assert result2 == 8, f"测试失败，期望8，实际{result2}"
    
    # 测试用例2
    nums2 = [9, -8]
    numArray2 = LeetCode307_SegmentTree(nums2)
    
    numArray2.update(0, 3)
    result3 = numArray2.sumRange(1, 1)
    print(f"输入数组: {nums2}")
    print(f"将索引0的值更新为3后，查询区间[1,1]的和: {result3}")
    assert result3 == -8, f"测试失败，期望-8，实际{result3}"
    
    result4 = numArray2.sumRange(0, 1)
    print(f"查询区间[0,1]的和: {result4}")
    assert result4 == -5, f"测试失败，期望-5，实际{result4}"
    
    numArray2.update(1, -3)
    result5 = numArray2.sumRange(0, 1)
    print(f"将索引1的值更新为-3后，查询区间[0,1]的和: {result5}")
    assert result5 == 0, f"测试失败，期望0，实际{result5}"
    
    print("LeetCode 307测试通过！\n")


def test_leetcode315():
    """测试LeetCode 315. 计算右侧小于当前元素的个数"""
    print("测试LeetCode 315. 计算右侧小于当前元素的个数...")
    
    # 测试用例1
    solution1 = LeetCode315_SegmentTree()
    nums1 = [5, 2, 6, 1]
    result1 = solution1.countSmaller(nums1)
    print(f"输入数组: {nums1}")
    print(f"输出结果: {result1}")
    assert result1 == [2, 1, 1, 0], f"测试失败，期望[2, 1, 1, 0]，实际{result1}"
    
    # 测试用例2
    solution2 = LeetCode315_SegmentTree()
    nums2 = [-1]
    result2 = solution2.countSmaller(nums2)
    print(f"输入数组: {nums2}")
    print(f"输出结果: {result2}")
    assert result2 == [0], f"测试失败，期望[0]，实际{result2}"
    
    # 测试用例3
    solution3 = LeetCode315_SegmentTree()
    nums3 = [-1, -1]
    result3 = solution3.countSmaller(nums3)
    print(f"输入数组: {nums3}")
    print(f"输出结果: {result3}")
    assert result3 == [0, 0], f"测试失败，期望[0, 0]，实际{result3}"
    
    print("LeetCode 315测试通过！\n")


def test_leetcode493():
    """测试LeetCode 493. 翻转对"""
    print("测试LeetCode 493. 翻转对...")
    
    # 测试用例1
    solution1 = LeetCode493_SegmentTree()
    nums1 = [1, 3, 2, 3, 1]
    result1 = solution1.reversePairs(nums1)
    print(f"输入数组: {nums1}")
    print(f"输出结果: {result1}")
    assert result1 == 2, f"测试失败，期望2，实际{result1}"
    
    # 测试用例2
    solution2 = LeetCode493_SegmentTree()
    nums2 = [2, 4, 3, 5, 1]
    result2 = solution2.reversePairs(nums2)
    print(f"输入数组: {nums2}")
    print(f"输出结果: {result2}")
    assert result2 == 3, f"测试失败，期望3，实际{result2}"
    
    # 测试用例3
    solution3 = LeetCode493_SegmentTree()
    nums3 = [2147483647, 2147483647, 2147483647, 2147483647, 2147483647, 2147483647]
    result3 = solution3.reversePairs(nums3)
    print(f"输入数组: {nums3}")
    print(f"输出结果: {result3}")
    assert result3 == 0, f"测试失败，期望0，实际{result3}"
    
    print("LeetCode 493测试通过！\n")


def test_leetcode327():
    """测试LeetCode 327. 区间和的个数"""
    print("测试LeetCode 327. 区间和的个数...")
    
    # 测试用例1
    solution1 = LeetCode327_SegmentTree()
    nums1 = [-2, 5, -1]
    lower1, upper1 = -2, 2
    result1 = solution1.countRangeSum(nums1, lower1, upper1)
    print(f"输入数组: {nums1}, lower = {lower1}, upper = {upper1}")
    print(f"输出结果: {result1}")
    assert result1 == 3, f"测试失败，期望3，实际{result1}"
    
    # 测试用例2
    solution2 = LeetCode327_SegmentTree()
    nums2 = [0]
    lower2, upper2 = 0, 0
    result2 = solution2.countRangeSum(nums2, lower2, upper2)
    print(f"输入数组: {nums2}, lower = {lower2}, upper = {upper2}")
    print(f"输出结果: {result2}")
    assert result2 == 1, f"测试失败，期望1，实际{result2}"
    
    # 测试用例3
    solution3 = LeetCode327_SegmentTree()
    nums3 = [2147483647, -2147483648, -1, 0]
    lower3, upper3 = -1, 0
    result3 = solution3.countRangeSum(nums3, lower3, upper3)
    print(f"输入数组: {nums3}, lower = {lower3}, upper = {upper3}")
    print(f"输出结果: {result3}")
    
    print("LeetCode 327测试通过！\n")


def test_poj3468():
    """测试POJ 3468 A Simple Problem with Integers"""
    print("测试POJ 3468 A Simple Problem with Integers...")
    
    # 测试用例
    nums = [1, 2, 3, 4, 5]
    seg_tree = POJ3468_SegmentTree(nums)
    
    print(f"初始数组: {nums}")
    result1 = seg_tree.query(1, 3)
    print(f"查询区间[1,3]的和: {result1}")
    assert result1 == 9, f"测试失败，期望9，实际{result1}"
    
    # 区间更新：将区间[1,3]中的每个元素都加上2
    seg_tree.update(1, 3, 2)
    result2 = seg_tree.query(1, 3)
    print("将区间[1,3]中的每个元素都加上2后:")
    print(f"查询区间[1,3]的和: {result2}")
    assert result2 == 15, f"测试失败，期望15，实际{result2}"
    
    print("POJ 3468测试通过！\n")


def test_hdu1166():
    """测试HDU 1166 敌兵布阵"""
    print("测试HDU 1166 敌兵布阵...")
    
    # 测试用例
    nums = [1, 2, 3, 4, 5]
    seg_tree = HDU1166_SegmentTree(nums)
    
    print(f"初始数组: {nums}")
    result1 = seg_tree.query(1, 3)
    print(f"查询区间[1,3]的和: {result1}")
    assert result1 == 9, f"测试失败，期望9，实际{result1}"
    
    # 单点更新：第2个营地增加3个士兵
    seg_tree.add(2, 3)
    result2 = seg_tree.query(1, 3)
    print("第2个营地增加3个士兵后:")
    print(f"查询区间[1,3]的和: {result2}")
    assert result2 == 12, f"测试失败，期望12，实际{result2}"
    
    print("HDU 1166测试通过！\n")


def test_luogu_p3372():
    """测试洛谷P3372 【模板】线段树1"""
    print("测试洛谷P3372 【模板】线段树1...")
    
    # 测试用例
    nums = [1, 2, 3, 4, 5]
    seg_tree = LuoguP3372_SegmentTree(nums)
    
    print(f"初始数组: {nums}")
    result1 = seg_tree.query(1, 3)
    print(f"查询区间[1,3]的和: {result1}")
    assert result1 == 9, f"测试失败，期望9，实际{result1}"
    
    # 区间更新：将区间[1,3]中的每个元素都加上2
    seg_tree.update(1, 3, 2)
    result2 = seg_tree.query(1, 3)
    print("将区间[1,3]中的每个元素都加上2后:")
    print(f"查询区间[1,3]的和: {result2}")
    assert result2 == 15, f"测试失败，期望15，实际{result2}"
    
    print("洛谷P3372测试通过！\n")


def test_codeforces_52c():
    """测试Codeforces 52C Circular RMQ"""
    print("测试Codeforces 52C Circular RMQ...")
    
    # 测试用例
    nums = [1, 2, 3, 4, 5]
    seg_tree = Codeforces52C_SegmentTree(nums)
    
    print(f"初始数组: {nums}")
    result1 = seg_tree.query(1, 3)
    print(f"查询区间[1,3]的最小值: {result1}")
    assert result1 == 2, f"测试失败，期望2，实际{result1}"
    
    # 区间更新：将区间[1,3]中的每个元素都加上2
    seg_tree.update(1, 3, 2)
    result2 = seg_tree.query(1, 3)
    print("将区间[1,3]中的每个元素都加上2后:")
    print(f"查询区间[1,3]的最小值: {result2}")
    assert result2 == 4, f"测试失败，期望4，实际{result2}"
    
    # 环形区间查询：查询区间[3,1]（环形）
    # 环形区间[3,1]包含元素索引3,4,0,1，对应值为4,5,1,2，加上之前的更新后为4,5,1,4，最小值是1
    result3 = seg_tree.query(3, 1)
    print(f"环形区间[3,1]的最小值: {result3}")
    assert result3 == 1, f"测试失败，期望1，实际{result3}"
    
    print("Codeforces 52C测试通过！\n")


def performance_test():
    """性能测试"""
    print("性能测试...")
    
    # 生成大规模测试数据
    import random
    n = 10000
    nums = [random.randint(-10000, 10000) for _ in range(n)]
    
    # 测试LeetCode 315性能
    solution = LeetCode315_SegmentTree()
    start_time = time.time()
    result = solution.countSmaller(nums)
    end_time = time.time()
    print(f"LeetCode 315处理{n}个元素耗时: {end_time - start_time:.4f}秒")
    
    print("性能测试完成！\n")


def main():
    """主测试函数"""
    print("开始测试所有线段树相关题目实现...\n")
    
    try:
        test_leetcode307()
        test_leetcode315()
        test_leetcode493()
        test_leetcode327()
        test_poj3468()
        test_hdu1166()
        test_luogu_p3372()
        test_codeforces_52c()
        performance_test()
        
        print("所有测试通过！所有线段树实现正确。")
    except Exception as e:
        print(f"测试失败: {e}")
        raise


if __name__ == "__main__":
    main()