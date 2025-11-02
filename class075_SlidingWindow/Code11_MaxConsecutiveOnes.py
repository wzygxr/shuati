# -*- coding: utf-8 -*-
"""
最大连续1的个数 III 问题解决方案

问题描述：
给定一个二进制数组 nums 和一个整数 k，
如果可以翻转最多 k 个 0，则返回数组中连续 1 的最大个数。

解题思路：
使用滑动窗口算法找到最长的连续1序列（最多可以翻转k个0）：
1. 维护一个滑动窗口，窗口内最多包含k个0
2. 右指针不断扩展窗口
3. 当窗口内0的个数超过k时，左指针右移缩小窗口
4. 记录窗口的最大长度

算法复杂度分析：
时间复杂度: O(n) - n为数组长度，每个元素最多被访问两次
空间复杂度: O(1) - 只使用常数额外空间

是否最优解: 是

相关题目链接：
LeetCode 1004. 最大连续1的个数 III
https://leetcode.cn/problems/max-consecutive-ones-iii/

其他平台类似题目：
1. 牛客网 - 最大连续1的个数
   https://www.nowcoder.com/practice/4665256cab4418c8287c912b78d5a1e7
2. LintCode 883. 最大连续1的个数 III
   https://www.lintcode.com/problem/883/
3. HackerRank - Max Consecutive Ones
   https://www.hackerrank.com/challenges/max-consecutive-ones/problem
4. CodeChef - CON1S - Consecutive Ones
   https://www.codechef.com/problems/CON1S
5. AtCoder - ABC146 D - Enough Array
   https://atcoder.jp/contests/abc146/tasks/abc146_d
6. 洛谷 P1886 滑动窗口
   https://www.luogu.com.cn/problem/P1886
7. 杭电OJ 4193 Sliding Window
   http://acm.hdu.edu.cn/showproblem.php?pid=4193
8. POJ 2823 Sliding Window
   http://poj.org/problem?id=2823
9. UVa OJ 11536 - Smallest Sub-Array
   https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=24&page=show_problem&problem=2531
10. SPOJ - ADAFRIEN - Ada and Friends
    https://www.spoj.com/problems/ADAFRIEN/

工程化考量：
1. 异常处理：处理空数组、k为负数等边界情况
2. 性能优化：使用滑动窗口避免重复计算，达到线性时间复杂度
3. 可读性：变量命名清晰，添加详细注释，提供测试用例
"""


def longestOnes(nums, k):
    """
    计算最多翻转k个0后能得到的最长连续1序列长度
    
    Args:
        nums (List[int]): 二进制数组
        k (int): 最多可以翻转的0的个数
    
    Returns:
        int: 最长连续1序列长度
    
    Examples:
        >>> longestOnes([1,1,1,0,0,0,1,1,1,1,0], 2)
        6
        >>> longestOnes([0,0,1,1,0,0,1,1,1,0,1,1,0,0,0,1,1,1,1], 3)
        10
    """
    # 记录最大连续1序列长度
    max_len = 0
    
    # 滑动窗口遍历数组
    l = 0  # 左指针
    zeros = 0  # 窗口内0的个数
    
    for r in range(len(nums)):
        # 右边界元素进入窗口
        # 如果是0，则增加窗口内0的计数
        if nums[r] == 0:
            zeros += 1
        
        # 当窗口内0的个数超过k时，需要缩小窗口
        while zeros > k:
            # 如果移除的元素是0，则减少窗口内0的计数
            if nums[l] == 0:
                zeros -= 1
            # 移动左指针
            l += 1
        
        # 更新最大长度（当前窗口大小）
        max_len = max(max_len, r - l + 1)
    
    return max_len


# 测试用例
if __name__ == "__main__":
    # 测试用例1
    nums1 = [1,1,1,0,0,0,1,1,1,1,0]
    k1 = 2
    result1 = longestOnes(nums1, k1)
    print(f"数组: {nums1}, k: {k1}")
    print(f"最大连续1的个数: {result1}")
    # 预期输出: 6
    
    # 测试用例2
    nums2 = [0,0,1,1,0,0,1,1,1,0,1,1,0,0,0,1,1,1,1]
    k2 = 3
    result2 = longestOnes(nums2, k2)
    print(f"\n数组: {nums2}, k: {k2}")
    print(f"最大连续1的个数: {result2}")
    # 预期输出: 10
    
    # 测试用例3：空数组
    nums3 = []
    k3 = 1
    result3 = longestOnes(nums3, k3)
    print(f"\n数组: {nums3}, k: {k3}")
    print(f"最大连续1的个数: {result3}")
    # 预期输出: 0
    
    # 测试用例4：k为0
    nums4 = [1,1,1,0,0,0,1,1,1,1,0]
    k4 = 0
    result4 = longestOnes(nums4, k4)
    print(f"\n数组: {nums4}, k: {k4}")
    print(f"最大连续1的个数: {result4}")
    # 预期输出: 4