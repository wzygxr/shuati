# 滑动窗口中位数
# 中位数是有序序列最中间的那个数。如果序列的长度是偶数，则没有最中间的数；
# 此时中位数是最中间的两个数的平均数。
# 例如：
# [2,3,4]，中位数是 3
# [2,3]，中位数是 (2 + 3) / 2 = 2.5
# 给你一个数组 nums，有一个长度为 k 的窗口从最左端滑动到最右端。
# 窗口中有 k 个数，每次窗口向右移动 1 位。
# 你的任务是找出每次窗口移动后得到的新窗口中元素的中位数，并输出由它们组成的数组。
# 测试链接：https://leetcode.cn/problems/sliding-window-median/
#
# 题目解析：
# 这是滑动窗口的另一个变种，需要维护窗口内元素的有序性以便快速获取中位数。
# 虽然不是直接使用单调队列，但可以使用类似的思想来优化。
#
# 算法思路：
# 1. 使用有序数组维护窗口内的元素
# 2. 滑动窗口移动时，添加新元素并移除旧元素
# 3. 每次移动后计算并记录中位数
#
# 时间复杂度：O(n*k) - 每次插入和删除需要O(k)时间，共n次操作
# 空间复杂度：O(k) - 存储窗口内k个元素

import bisect

class Solution:
    def medianSlidingWindow(self, nums, k):
        """
        计算滑动窗口中位数
        :param nums: 输入数组
        :param k: 窗口大小
        :return: 每个窗口的中位数组成的数组
        """
        # 初始化窗口
        window = sorted(nums[:k])
        result = []
        
        # 计算第一个窗口的中位数
        result.append(self.get_median(window, k))
        
        # 处理后续窗口
        for i in range(k, len(nums)):
            # 移除窗口左边的元素
            window.remove(nums[i - k])
            # 添加窗口右边的新元素
            bisect.insort(window, nums[i])
            # 计算当前窗口的中位数
            result.append(self.get_median(window, k))
        
        return result
    
    def get_median(self, window, k):
        """
        获取当前窗口的中位数
        :param window: 有序窗口数组
        :param k: 窗口大小
        :return: 当前窗口的中位数
        """
        if k % 2 == 1:
            # 奇数个元素，返回中间元素
            return float(window[k // 2])
        else:
            # 偶数个元素，返回中间两个元素的平均值
            return (window[k // 2 - 1] + window[k // 2]) / 2.0
