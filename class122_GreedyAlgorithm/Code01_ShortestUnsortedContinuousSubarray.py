# 最短无序连续子数组
# 给你一个整数数组nums，你需要找出一个 连续子数组
# 如果对这个子数组进行升序排序，那么整个数组都会变为升序排序
# 请你找出符合题意的最短子数组，并输出它的长度
# 测试链接 : https://leetcode.cn/problems/shortest-unsorted-continuous-subarray/
# 相关题目链接：
# https://leetcode.cn/problems/shortest-unsorted-continuous-subarray/ (LeetCode 581)
# https://www.lintcode.com/problem/shortest-unsorted-continuous-subarray/ (LintCode 1206)
# https://practice.geeksforgeeks.org/problems/minimum-subarray-to-sort/ (GeeksforGeeks)
# https://www.nowcoder.com/practice/2f9264b48cc24799925d48d355094c78 (牛客网)
# https://ac.nowcoder.com/acm/problem/14251 (牛客网)
# https://codeforces.com/problemset/problem/1139/C (Codeforces)
# https://atcoder.jp/contests/abc134/tasks/abc134_c (AtCoder)
# https://www.hackerrank.com/challenges/shortest-unsorted-continuous-subarray/problem (HackerRank)
# https://www.luogu.com.cn/problem/P1525 (洛谷)
# https://vjudge.net/problem/HDU-6375 (HDU)
# https://www.spoj.com/problems/ARRAYSUB/ (SPOJ)
# https://www.codechef.com/problems/SUBSPLAY (CodeChef)

import sys

def findUnsortedSubarray(nums):
    """
    找到最短无序连续子数组
    
    算法思路：
    使用两次遍历的贪心策略：
    1. 从左到右遍历，维护最大值，如果当前元素小于最大值，则更新右边界
    2. 从右到左遍历，维护最小值，如果当前元素大于最小值，则更新左边界
    
    时间复杂度：O(n) - 需要遍历数组两次
    空间复杂度：O(1) - 只使用常数额外空间
    
    :param nums: 输入的整数数组
    :return: 需要排序的最短子数组长度
    """
    n = len(nums)
    
    # 从左往右遍历，找到最右的不达标位置
    # max_val > 当前数，认为不达标（即当前数应该在前面）
    right = -1
    max_val = -sys.maxsize - 1
    for i in range(n):
        if max_val > nums[i]:
            right = i
        max_val = max(max_val, nums[i])
    
    # 从右往左遍历，找到最左的不达标位置
    # min_val < 当前数，认为不达标（即当前数应该在后面）
    min_val = sys.maxsize
    left = n
    for i in range(n - 1, -1, -1):
        if min_val < nums[i]:
            left = i
        min_val = min(min_val, nums[i])
    
    # 如果left和right没有更新，说明数组已经有序
    # 否则返回子数组长度
    return max(0, right - left + 1)

# 测试用例
if __name__ == "__main__":
    # 测试用例1: [2, 6, 4, 8, 10, 9, 15] -> [6, 4, 8, 10, 9] 长度为5
    nums1 = [2, 6, 4, 8, 10, 9, 15]
    print("测试用例1: " + str(nums1))
    print("结果: " + str(findUnsortedSubarray(nums1)))  # 期望输出: 5
    
    # 测试用例2: [1, 2, 3, 4] -> 已经有序，长度为0
    nums2 = [1, 2, 3, 4]
    print("测试用例2: " + str(nums2))
    print("结果: " + str(findUnsortedSubarray(nums2)))  # 期望输出: 0
    
    # 测试用例3: [1] -> 单个元素，长度为0
    nums3 = [1]
    print("测试用例3: " + str(nums3))
    print("结果: " + str(findUnsortedSubarray(nums3)))  # 期望输出: 0