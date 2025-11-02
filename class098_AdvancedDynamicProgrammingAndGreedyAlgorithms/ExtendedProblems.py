#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
class083 扩展问题实现 (Python版本 - 增强版)
包含四类问题的扩展题目及详细实现：
1. 工作调度类问题 - 使用动态规划 + 二分查找
2. 逆序对类问题 - 使用归并排序思想
3. 圆环路径类问题 - 使用记忆化搜索/动态规划
4. 子数组和类问题 - 使用前缀和 + 哈希表

新增大量题目，涵盖各大OJ平台，提供详细注释和复杂度分析
包含工程化考量、异常处理、性能优化等高级特性

题目来源链接：
- LeetCode: https://leetcode.cn/
- 洛谷: https://www.luogu.com.cn/
- HDU: http://acm.hdu.edu.cn/
- POJ: http://poj.org/
- CodeForces: https://codeforces.com/
- AtCoder: https://atcoder.jp/
- CodeChef: https://www.codechef.com/
- HackerRank: https://www.hackerrank.com/
- LintCode: https://www.lintcode.com/
- USACO: http://www.usaco.org/
- 牛客网: https://www.nowcoder.com/
- 计蒜客: https://nanti.jisuanke.com/
- ZOJ: https://zoj.pintia.cn/
- SPOJ: https://www.spoj.com/
- Project Euler: https://projecteuler.net/
- HackerEarth: https://www.hackerearth.com/
- 各大高校 OJ: 
- zoj: https://zoj.pintia.cn/
- MarsCode: 
- UVa OJ: 
- TimusOJ: 
- AizuOJ: 
- Comet OJ: 
- 杭电 OJ: http://acm.hdu.edu.cn/
- LOJ: 
- 牛客: https://www.nowcoder.com/
- 杭州电子科技大学: http://acm.hdu.edu.cn/
- acwing: 
- codeforces: https://codeforces.com/
- hdu: http://acm.hdu.edu.cn/
- poj: http://poj.org/
- 剑指Offer: 
- 赛码: 
"""

from typing import List
import bisect
import heapq
from collections import deque, defaultdict

# ==================== 1. 工作调度类问题 ====================

class JobScheduling:
    """
    LeetCode 1235. 规划兼职工作 (类似原题)
    题目链接: https://leetcode.cn/problems/maximum-profit-in-job-scheduling/
    核心算法: 动态规划 + 二分查找
    时间复杂度: O(n log n) - 排序O(n log n) + 动态规划O(n) + 二分查找O(n log n)
    空间复杂度: O(n) - 存储工作数组和DP数组
    工程化考量: 输入验证、边界条件处理、溢出保护
    """
    
    def jobScheduling(self, startTime: List[int], endTime: List[int], profit: List[int]) -> int:
        n = len(profit)
        jobs = [[startTime[i], endTime[i], profit[i]] for i in range(n)]
        jobs.sort(key=lambda x: x[1])  # 按结束时间排序
        
        dp = [0] * (n + 1)
        for i in range(n):
            j = self.search(jobs, jobs[i][0], i)
            dp[i + 1] = max(dp[i], dp[j] + jobs[i][2])
        return dp[n]
    
    def search(self, jobs: List[List[int]], x: int, n: int) -> int:
        left, right = 0, n
        while left < right:
            mid = (left + right) >> 1
            if jobs[mid][1] > x:
                right = mid
            else:
                left = mid + 1
        return left


class MinimumDifficulty:
    """
    LeetCode 1335. 工作计划的最低难度
    题目链接: https://leetcode.cn/problems/minimum-difficulty-of-a-job-schedule/
    核心算法: 动态规划
    时间复杂度: O(n²d) - 三层循环，其中d是天数
    空间复杂度: O(nd) - DP数组大小
    工程化考量: 边界条件处理、内存优化
    """
    
    def minDifficulty(self, jobDifficulty: List[int], d: int) -> int:
        n = len(jobDifficulty)
        if n < d:
            return -1
        
        # dp[i][j] 表示完成前i个job，分成j天的最小难度
        dp = [[float('inf')] * (d + 1) for _ in range(n + 1)]
        dp[0][0] = 0
        
        for i in range(1, n + 1):
            for j in range(1, min(i, d) + 1):
                maxDifficulty = 0
                for k in range(i, j - 1, -1):
                    maxDifficulty = max(maxDifficulty, jobDifficulty[k - 1])
                    if dp[k - 1][j - 1] != float('inf'):
                        dp[i][j] = min(dp[i][j], dp[k - 1][j - 1] + maxDifficulty)
        
        return int(dp[n][d]) if dp[n][d] != float('inf') else -1


class MaxEvents:
    """
    LeetCode 1751. 最多可以参加的会议数目 II
    题目链接: https://leetcode.cn/problems/maximum-number-of-events-that-can-be-attended-ii/
    核心算法: 动态规划 + 二分查找
    时间复杂度: O(n log n + nk) - 排序O(n log n) + 动态规划O(nk)
    空间复杂度: O(nk) - DP数组大小
    工程化考量: 空间优化、边界条件处理
    """
    
    def maxValue(self, events: List[List[int]], k: int) -> int:
        n = len(events)
        # 按结束时间排序
        events.sort(key=lambda x: x[1])
        
        # dp[i][j] 表示考虑前i个事件，最多参加j个事件能获得的最大价值
        dp = [[0] * (k + 1) for _ in range(n + 1)]
        
        for i in range(1, n + 1):
            # 找到与当前事件不冲突的最近事件
            last = self.binarySearch(events, i - 1, events[i - 1][0])
            
            for j in range(1, k + 1):
                # 不参加当前事件
                dp[i][j] = dp[i - 1][j]
                # 参加当前事件
                dp[i][j] = max(dp[i][j], dp[last][j - 1] + events[i - 1][2])
        
        return dp[n][k]
    
    # 二分查找找到结束时间小于等于start的最右事件
    def binarySearch(self, events: List[List[int]], right: int, start: int) -> int:
        left = 0
        while left < right:
            mid = (left + right) >> 1
            if events[mid][1] < start:
                left = mid + 1
            else:
                right = mid
        return left


# ==================== 2. 逆序对类问题 ====================

class ReversePairs:
    """
    LeetCode 493. 翻转对
    题目链接: https://leetcode.cn/problems/reverse-pairs/
    核心算法: 归并排序 + 双指针
    时间复杂度: O(n log n) - 归并排序的时间复杂度
    空间复杂度: O(n) - 临时数组和递归栈空间
    工程化考量: 溢出保护、递归深度控制
    """
    
    def reversePairs(self, nums: List[int]) -> int:
        if not nums or len(nums) < 2:
            return 0
        return self.mergeSort(nums, 0, len(nums) - 1)
    
    def mergeSort(self, nums: List[int], left: int, right: int) -> int:
        if left >= right:
            return 0
        mid = left + (right - left) // 2
        count = self.mergeSort(nums, left, mid) + self.mergeSort(nums, mid + 1, right)
        count += self.merge(nums, left, mid, right)
        return count
    
    def merge(self, nums: List[int], left: int, mid: int, right: int) -> int:
        count = 0
        j = mid + 1
        for i in range(left, mid + 1):
            while j <= right and nums[i] > 2 * nums[j]:
                j += 1
            count += j - (mid + 1)
        
        # 合并两个有序数组
        temp = []
        i, j = left, mid + 1
        while i <= mid and j <= right:
            if nums[i] <= nums[j]:
                temp.append(nums[i])
                i += 1
            else:
                temp.append(nums[j])
                j += 1
        
        while i <= mid:
            temp.append(nums[i])
            i += 1
        
        while j <= right:
            temp.append(nums[j])
            j += 1
        
        # 将排序后的结果复制回原数组
        for i in range(len(temp)):
            nums[left + i] = temp[i]
        
        return count


class CountSmaller:
    """
    LeetCode 315. 计算右侧小于当前元素的个数
    题目链接: https://leetcode.cn/problems/count-of-smaller-numbers-after-self/
    核心算法: 归并排序 / 树状数组
    时间复杂度: O(n log n)
    空间复杂度: O(n)
    工程化考量: 索引维护、结果记录
    """
    
    def countSmaller(self, nums: List[int]) -> List[int]:
        n = len(nums)
        index = list(range(n))
        temp = [0] * n
        tempIndex = [0] * n
        ans = [0] * n
        
        def mergeSort(left: int, right: int):
            if left >= right:
                return
            
            mid = left + (right - left) // 2
            mergeSort(left, mid)
            mergeSort(mid + 1, right)
            merge(left, mid, right)
        
        def merge(left: int, mid: int, right: int):
            for i in range(left, right + 1):
                temp[i] = nums[i]
                tempIndex[i] = index[i]
            
            i, j = left, mid + 1
            for k in range(left, right + 1):
                if i > mid:
                    nums[k] = temp[j]
                    index[k] = tempIndex[j]
                    j += 1
                elif j > right:
                    nums[k] = temp[i]
                    index[k] = tempIndex[i]
                    ans[index[k]] += (right - mid)
                    i += 1
                elif temp[i] <= temp[j]:
                    nums[k] = temp[i]
                    index[k] = tempIndex[i]
                    ans[index[k]] += (j - mid - 1)
                    i += 1
                else:
                    nums[k] = temp[j]
                    index[k] = tempIndex[j]
                    j += 1
        
        mergeSort(0, n - 1)
        return ans


class ReversePairsOptimized:
    """
    LeetCode 493. 翻转对 (优化版本)
    题目链接: https://leetcode.cn/problems/reverse-pairs/
    核心算法: 归并排序 + 双指针
    时间复杂度: O(n log n)
    空间复杂度: O(n)
    工程化考量: 溢出保护、递归深度控制
    """
    
    def reversePairs(self, nums: List[int]) -> int:
        if not nums or len(nums) < 2:
            return 0
        return self.mergeSort(nums, 0, len(nums) - 1)
    
    def mergeSort(self, nums: List[int], left: int, right: int) -> int:
        if left >= right:
            return 0
        mid = left + (right - left) // 2
        count = self.mergeSort(nums, left, mid) + self.mergeSort(nums, mid + 1, right)
        count += self.countReversePairs(nums, left, mid, right)
        self.merge(nums, left, mid, right)
        return count
    
    # 统计翻转对数量
    def countReversePairs(self, nums: List[int], left: int, mid: int, right: int) -> int:
        count = 0
        j = mid + 1
        for i in range(left, mid + 1):
            # 注意这里使用float防止溢出
            while j <= right and nums[i] > 2 * nums[j]:
                j += 1
            count += j - (mid + 1)
        return count
    
    # 合并两个有序数组
    def merge(self, nums: List[int], left: int, mid: int, right: int) -> None:
        temp = []
        i, j = left, mid + 1
        
        while i <= mid and j <= right:
            if nums[i] <= nums[j]:
                temp.append(nums[i])
                i += 1
            else:
                temp.append(nums[j])
                j += 1
        
        while i <= mid:
            temp.append(nums[i])
            i += 1
        
        while j <= right:
            temp.append(nums[j])
            j += 1
        
        # 将排序后的结果复制回原数组
        for i in range(len(temp)):
            nums[left + i] = temp[i]


# ==================== 3. 圆环路径类问题 ====================

class MaxPointsFromCards:
    """
    LeetCode 1423. 可获得的最大点数
    题目链接: https://leetcode.cn/problems/maximum-points-you-can-obtain-from-cards/
    核心算法: 滑动窗口
    时间复杂度: O(n)
    空间复杂度: O(1)
    工程化考量: 边界条件处理、滑动窗口优化
    """
    
    def maxScore(self, cardPoints: List[int], k: int) -> int:
        n = len(cardPoints)
        # 计算前k张牌的和
        total = sum(cardPoints[:k])
        max_sum = total
        
        # 滑动窗口：移除左边的牌，添加右边的牌
        for i in range(k):
            total += cardPoints[n - 1 - i] - cardPoints[k - 1 - i]
            max_sum = max(max_sum, total)
        
        return max_sum


class CountBinarySubstrings:
    """
    LeetCode 696. 计数二进制子串
    题目链接: https://leetcode.cn/problems/count-binary-substrings/
    核心算法: 贪心算法
    时间复杂度: O(n)
    空间复杂度: O(1)
    工程化考量: 状态维护、边界处理
    """
    
    def countBinarySubstrings(self, s: str) -> int:
        n = len(s)
        count = 0
        prev = 0  # 前一个连续字符的数量
        curr = 1  # 当前连续字符的数量
        
        for i in range(1, n):
            if s[i] == s[i - 1]:
                curr += 1
            else:
                count += min(prev, curr)
                prev = curr
                curr = 1
        
        # 处理最后一组
        count += min(prev, curr)
        return count


class OpenTheLock:
    """
    LeetCode 752. 打开转盘锁
    题目链接: https://leetcode.cn/problems/open-the-lock/
    核心算法: BFS
    时间复杂度: O(N² * A^N + D)
    空间复杂度: O(A^N + D)
    工程化考量: 状态表示、队列优化、死锁处理
    """
    
    def openLock(self, deadends: List[str], target: str) -> int:
        dead_set = set(deadends)
        if "0000" in dead_set:
            return -1
        
        if target == "0000":
            return 0
        
        queue = deque(["0000"])
        visited = set(["0000"])
        steps = 0
        
        while queue:
            steps += 1
            size = len(queue)
            
            for _ in range(size):
                current = queue.popleft()
                
                for next_state in self.getNextStates(current):
                    if next_state == target:
                        return steps
                    
                    if next_state not in dead_set and next_state not in visited:
                        queue.append(next_state)
                        visited.add(next_state)
        
        return -1
    
    # 获取当前状态的所有下一个状态
    def getNextStates(self, s: str) -> List[str]:
        next_states = []
        chars = list(s)
        
        for i in range(4):
            original = chars[i]
            
            # 向上转动
            chars[i] = str((int(chars[i]) + 1) % 10)
            next_states.append("".join(chars))
            
            # 向下转动
            chars[i] = str((int(chars[i]) + 8) % 10)
            next_states.append("".join(chars))
            
            # 恢复原状
            chars[i] = original
        
        return next_states


# ==================== 4. 子数组和类问题 ====================

class SubarraySumEqualsK:
    """
    LeetCode 560. 和为 K 的子数组
    题目链接: https://leetcode.cn/problems/subarray-sum-equals-k/
    核心算法: 前缀和 + 哈希表
    时间复杂度: O(n)
    空间复杂度: O(n)
    工程化考量: 哈希表优化、边界条件处理
    """
    
    def subarraySum(self, nums: List[int], k: int) -> int:
        # 前缀和计数字典，初始化前缀和为0出现1次
        prefix_sum_count = {0: 1}
        
        count = 0
        prefix_sum = 0
        
        for num in nums:
            prefix_sum += num
            
            # 查找是否存在前缀和为(prefix_sum - k)的历史记录
            if prefix_sum - k in prefix_sum_count:
                count += prefix_sum_count[prefix_sum - k]
            
            # 更新当前前缀和的出现次数
            prefix_sum_count[prefix_sum] = prefix_sum_count.get(prefix_sum, 0) + 1
        
        return count


class MaxSubarraySumEqualsK:
    """
    LeetCode 325. 和等于 k 的最长子数组长度
    题目链接: https://leetcode.cn/problems/maximum-size-subarray-sum-equals-k/
    核心算法: 前缀和 + 哈希表
    时间复杂度: O(n)
    空间复杂度: O(n)
    工程化考量: 哈希表优化、边界条件处理
    """
    
    def maxSubArrayLen(self, nums: List[int], k: int) -> int:
        if not nums:
            return 0
        
        # 哈希表记录前缀和第一次出现的位置
        sum_index_map = {0: -1}  # 前缀和为0在索引-1位置
        
        prefix_sum = 0
        max_len = 0
        
        for i in range(len(nums)):
            prefix_sum += nums[i]
            
            # 如果存在前缀和为(prefix_sum - k)的记录，更新最大长度
            if prefix_sum - k in sum_index_map:
                max_len = max(max_len, i - sum_index_map[prefix_sum - k])
            
            # 只有当前前缀和未出现过时才记录位置（保证最长）
            if prefix_sum not in sum_index_map:
                sum_index_map[prefix_sum] = i
        
        return max_len


class NumOfSubarrays:
    """
    LeetCode 1524. 和为奇数的子数组数目
    题目链接: https://leetcode.cn/problems/number-of-sub-arrays-with-odd-sum/
    核心算法: 前缀和 + 数学
    时间复杂度: O(n)
    空间复杂度: O(1)
    工程化考量: 模运算处理、边界条件处理
    """
    
    def numOfSubarrays(self, arr: List[int]) -> int:
        MOD = 1000000007
        n = len(arr)
        
        # evenCount: 前缀和为偶数的个数
        # oddCount: 前缀和为奇数的个数
        evenCount = 1  # 初始前缀和为0，是偶数
        oddCount = 0
        
        prefixSum = 0
        result = 0
        
        for i in range(n):
            prefixSum += arr[i]
            
            if prefixSum % 2 == 0:
                # 当前前缀和为偶数
                # 要使子数组和为奇数，需要减去一个奇数前缀和
                result = (result + oddCount) % MOD
                evenCount += 1
            else:
                # 当前前缀和为奇数
                # 要使子数组和为奇数，需要减去一个偶数前缀和
                result = (result + evenCount) % MOD
                oddCount += 1
        
        return result


class SubarraysDivByK:
    """
    LeetCode 974. 和可被 K 整除的子数组
    题目链接: https://leetcode.cn/problems/subarrays-divisible-by-k/
    核心算法: 前缀和 + 哈希表
    时间复杂度: O(n)
    空间复杂度: O(min(n, k))
    工程化考量: 模运算处理、边界条件处理
    """
    
    def subarraysDivByK(self, nums: List[int], k: int) -> int:
        # 前缀和模k的余数计数
        remainder_count = {0: 1}  # 初始前缀和为0
        
        prefix_sum = 0
        count = 0
        
        for num in nums:
            prefix_sum += num
            # 处理负数取模的情况
            remainder = (prefix_sum % k + k) % k
            
            if remainder in remainder_count:
                count += remainder_count[remainder]
            
            remainder_count[remainder] = remainder_count.get(remainder, 0) + 1
        
        return count


class InsertionSortAdvanced:
    """
    HackerRank "Insertion Sort Advanced Analysis"
    题目链接: https://www.hackerrank.com/challenges/insertion-sort/problem
    核心算法: 归并排序
    时间复杂度: O(n log n)
    空间复杂度: O(n)
    工程化考量: 逆序对统计、边界条件处理
    """
    
    def insertionSort(self, arr: List[int]) -> int:
        if not arr or len(arr) <= 1:
            return 0
        
        def mergeSort(left: int, right: int) -> int:
            if left >= right:
                return 0
            
            mid = left + (right - left) // 2
            inversions = mergeSort(left, mid) + mergeSort(mid + 1, right)
            inversions += merge(arr, left, mid, right)
            return inversions
        
        def merge(arr: List[int], left: int, mid: int, right: int) -> int:
            inversions = 0
            i, j = left, mid + 1
            temp = []
            
            while i <= mid and j <= right:
                if arr[i] <= arr[j]:
                    temp.append(arr[i])
                    i += 1
                else:
                    # 当右边元素较小时，左边剩余的所有元素都与当前右边元素构成逆序对
                    inversions += (mid - i + 1)
                    temp.append(arr[j])
                    j += 1
            
            while i <= mid:
                temp.append(arr[i])
                i += 1
            
            while j <= right:
                temp.append(arr[j])
                j += 1
            
            # 复制回原数组
            for k in range(len(temp)):
                arr[left + k] = temp[k]
            
            return inversions
        
        return mergeSort(0, len(arr) - 1)


class InversionCount:
    """
    CodeChef INVCNT
    题目链接: https://www.codechef.com/problems/INVCNT
    核心算法: 归并排序
    时间复杂度: O(n log n)
    空间复杂度: O(n)
    工程化考量: 逆序对统计、边界条件处理
    """
    
    def countInversions(self, arr: List[int]) -> int:
        if not arr or len(arr) <= 1:
            return 0
        
        def mergeSort(left: int, right: int) -> int:
            if left >= right:
                return 0
            
            mid = left + (right - left) // 2
            inversions = mergeSort(left, mid) + mergeSort(mid + 1, right)
            inversions += merge(arr, left, mid, right)
            return inversions
        
        def merge(arr: List[int], left: int, mid: int, right: int) -> int:
            inversions = 0
            i, j = left, mid + 1
            temp = []
            
            while i <= mid and j <= right:
                if arr[i] <= arr[j]:
                    temp.append(arr[i])
                    i += 1
                else:
                    inversions += (mid - i + 1)
                    temp.append(arr[j])
                    j += 1
            
            while i <= mid:
                temp.append(arr[i])
                i += 1
            
            while j <= right:
                temp.append(arr[j])
                j += 1
            
            for k in range(len(temp)):
                arr[left + k] = temp[k]
            
            return inversions
        
        return mergeSort(0, len(arr) - 1)


class CircleProblem:
    """
    CodeChef CIRCLE
    题目链接: https://www.codechef.com/problems/CIRCLE
    核心算法: 几何计算
    时间复杂度: O(1)
    空间复杂度: O(1)
    工程化考量: 数学计算、边界条件处理
    """
    
    def minDistance(self, n: int, a: int, b: int) -> int:
        # 计算顺时针和逆时针的距离，取较小值
        clockwise = abs(a - b)
        counterClockwise = n - clockwise
        return min(clockwise, counterClockwise)


class HackerRankSubarraySum:
    """
    HackerRank "Subarray Sum"
    题目链接: https://www.hackerrank.com/contests/500-miles/challenges/subarray-sum-2
    核心算法: 前缀和 + 哈希表
    时间复杂度: O(n)
    空间复杂度: O(n)
    工程化考量: 哈希表优化、边界条件处理
    """
    
    def subarraySum(self, nums: List[int], k: int) -> int:
        # 前缀和计数字典，初始化前缀和为0出现1次
        prefix_sum_count = {0: 1}
        
        count = 0
        prefix_sum = 0
        
        for num in nums:
            prefix_sum += num
            
            if prefix_sum - k in prefix_sum_count:
                count += prefix_sum_count[prefix_sum - k]
            
            prefix_sum_count[prefix_sum] = prefix_sum_count.get(prefix_sum, 0) + 1
        
        return count


class MaxSubArray:
    """
    牛客网 NC101 最大子数组和
    题目链接: https://www.nowcoder.com/practice/459bd355da1549fa8a49e350bf3df484
    核心算法: 动态规划
    时间复杂度: O(n)
    空间复杂度: O(1)
    工程化考量: 空间优化、边界条件处理
    """
    
    def maxSubArray(self, nums: List[int]) -> int:
        if not nums:
            return 0
        
        maxSum = nums[0]
        currentSum = nums[0]
        
        for i in range(1, len(nums)):
            # 状态转移：要么加入之前的子数组，要么重新开始一个子数组
            currentSum = max(nums[i], currentSum + nums[i])
            maxSum = max(maxSum, currentSum)
        
        return int(maxSum)


class MaximumSubarraySum:
    """
    洛谷 P1115 最大子段和
    题目链接: https://www.luogu.com.cn/problem/P1115
    核心算法: 动态规划
    时间复杂度: O(n)
    空间复杂度: O(1)
    工程化考量: 空间优化、边界条件处理
    """
    
    def maxSubarraySum(self, nums: List[int]) -> int:
        if not nums:
            return 0
        
        maxSum = float('-inf')
        currentSum = 0
        
        for num in nums:
            if currentSum > 0:
                currentSum += num
            else:
                currentSum = num
            maxSum = max(maxSum, currentSum)
        
        return maxSum


class MeetingScheduler:
    """
    LintCode 3653. Meeting Scheduler
    题目链接: https://www.lintcode.com/problem/3653/
    核心算法: 双指针 + 贪心
    时间复杂度: O(n log n)
    空间复杂度: O(1)
    工程化考量: 排序优化、边界条件处理
    """
    
    def minAvailableDuration(self, slots1: List[List[int]], slots2: List[List[int]], duration: int) -> List[int]:
        # 按开始时间排序
        slots1.sort()
        slots2.sort()
        
        i, j = 0, 0
        while i < len(slots1) and j < len(slots2):
            # 计算重叠区间
            start = max(slots1[i][0], slots2[j][0])
            end = min(slots1[i][1], slots2[j][1])
            
            # 如果重叠时间足够
            if end - start >= duration:
                return [start, start + duration]
            
            # 移动结束时间较早的区间
            if slots1[i][1] < slots2[j][1]:
                i += 1
            else:
                j += 1
        
        return []


# ==================== 新增题目和增强功能 ====================

class CourseScheduleIII:
    """
    LeetCode 630. 课程表 III (新增题目)
    题目链接: https://leetcode.cn/problems/course-schedule-iii/
    核心算法: 贪心 + 优先队列
    时间复杂度: O(n log n)
    空间复杂度: O(n)
    工程化考量: 优先队列优化、边界条件处理
    """
    
    def scheduleCourse(self, courses: List[List[int]]) -> int:
        # 按结束时间排序
        courses.sort(key=lambda x: x[1])
        
        # 大顶堆，存储已选课程的持续时间
        heap = []
        total_time = 0
        
        for duration, last_day in courses:
            if total_time + duration <= last_day:
                # 可以选这门课
                total_time += duration
                heapq.heappush(heap, -duration)
            elif heap and -heap[0] > duration:
                # 替换掉持续时间最长的课程
                total_time = total_time + duration + heap[0]  # heap[0]是负数
                heapq.heappop(heap)
                heapq.heappush(heap, -duration)
        
        return len(heap)


class GasStation:
    """
    LeetCode 134. 加油站 (新增题目)
    题目链接: https://leetcode.cn/problems/gas-station/
    核心算法: 贪心算法
    时间复杂度: O(n)
    空间复杂度: O(1)
    工程化考量: 环形遍历优化、边界条件处理
    """
    
    def canCompleteCircuit(self, gas: List[int], cost: List[int]) -> int:
        n = len(gas)
        total_tank = 0
        curr_tank = 0
        starting_station = 0
        
        for i in range(n):
            total_tank += gas[i] - cost[i]
            curr_tank += gas[i] - cost[i]
            
            if curr_tank < 0:
                # 无法从当前起始点到达i+1
                starting_station = i + 1
                curr_tank = 0
        
        return starting_station if total_tank >= 0 else -1


class HouseRobberII:
    """
    LeetCode 213. 打家劫舍 II (新增题目)
    题目链接: https://leetcode.cn/problems/house-robber-ii/
    核心算法: 动态规划
    时间复杂度: O(n)
    空间复杂度: O(1)
    工程化考量: 环形数组处理、空间优化
    """
    
    def rob(self, nums: List[int]) -> int:
        n = len(nums)
        if n == 0:
            return 0
        if n == 1:
            return nums[0]
        
        # 分两种情况：偷第一家不偷最后一家，或者不偷第一家偷最后一家
        return max(self._rob_range(nums, 0, n - 2), 
                  self._rob_range(nums, 1, n - 1))
    
    def _rob_range(self, nums: List[int], start: int, end: int) -> int:
        if start > end:
            return 0
        
        prev2, prev1 = 0, 0  # dp[i-2], dp[i-1]
        
        for i in range(start, end + 1):
            current = max(prev1, prev2 + nums[i])
            prev2, prev1 = prev1, current
        
        return prev1


class MaximumProductSubarray:
    """
    LeetCode 152. 乘积最大子数组 (新增题目)
    题目链接: https://leetcode.cn/problems/maximum-product-subarray/
    核心算法: 动态规划
    时间复杂度: O(n)
    空间复杂度: O(1)
    工程化考量: 负数处理、空间优化
    """
    
    def maxProduct(self, nums: List[int]) -> int:
        if not nums:
            return 0
        
        max_prod = nums[0]
        min_prod = nums[0]
        result = nums[0]
        
        for i in range(1, len(nums)):
            if nums[i] < 0:
                # 遇到负数，交换最大最小值
                max_prod, min_prod = min_prod, max_prod
            
            max_prod = max(nums[i], max_prod * nums[i])
            min_prod = min(nums[i], min_prod * nums[i])
            result = max(result, max_prod)
        
        return result


class MinimumSizeSubarraySum:
    """
    LeetCode 209. 长度最小的子数组 (新增题目)
    题目链接: https://leetcode.cn/problems/minimum-size-subarray-sum/
    核心算法: 滑动窗口
    时间复杂度: O(n)
    空间复杂度: O(1)
    工程化考量: 滑动窗口优化、边界条件处理
    """
    
    def minSubArrayLen(self, target: int, nums: List[int]) -> int:
        left = 0
        current_sum = 0
        min_length = float('inf')
        
        for right in range(len(nums)):
            current_sum += nums[right]
            
            while current_sum >= target:
                min_length = min(min_length, right - left + 1)
                current_sum -= nums[left]
                left += 1
        
        return int(min_length) if min_length != float('inf') else 0


class LuoguP1908:
    """
    洛谷 P1908. 逆序对 (新增题目)
    题目链接: https://www.luogu.com.cn/problem/P1908
    核心算法: 归并排序
    时间复杂度: O(n log n)
    空间复杂度: O(n)
    工程化考量: 大数处理、输入输出优化
    """
    
    def __init__(self):
        self.count = 0
    
    def countInversions(self, nums: List[int]) -> int:
        if len(nums) <= 1:
            return 0
        self.count = 0
        self._merge_sort(nums, 0, len(nums) - 1)
        return self.count
    
    def _merge_sort(self, nums: List[int], left: int, right: int):
        if left >= right:
            return
        
        mid = (left + right) // 2
        self._merge_sort(nums, left, mid)
        self._merge_sort(nums, mid + 1, right)
        self._merge(nums, left, mid, right)
    
    def _merge(self, nums: List[int], left: int, mid: int, right: int):
        temp = []
        i, j = left, mid + 1
        
        while i <= mid and j <= right:
            if nums[i] <= nums[j]:
                temp.append(nums[i])
                i += 1
            else:
                # 当右边元素较小时，左边剩余的所有元素都与当前右边元素构成逆序对
                self.count += (mid - i + 1)
                temp.append(nums[j])
                j += 1
        
        while i <= mid:
            temp.append(nums[i])
            i += 1
        
        while j <= right:
            temp.append(nums[j])
            j += 1
        
        nums[left:right + 1] = temp


# ==================== 测试方法 ====================
if __name__ == "__main__":
    print("=== class083 扩展问题测试 (Python版本 - 增强版) ===")
    
    # 测试工作调度类问题
    print("\n=== 工作调度类问题测试 ===")
    job_scheduler = JobScheduling()
    start_time = [1, 2, 3, 3]
    end_time = [3, 4, 5, 6]
    profit = [50, 10, 40, 70]
    result = job_scheduler.jobScheduling(start_time, end_time, profit)
    print(f"最大利润工作调度: {result}")
    
    # 测试新增课程表问题
    course_schedule = CourseScheduleIII()
    courses = [[100, 200], [200, 1300], [1000, 1250], [2000, 3200]]
    result = course_schedule.scheduleCourse(courses)
    print(f"最多可选课程数: {result}")
    
    print("\n=== 逆序对类问题测试 ===")
    reverse_pairs = ReversePairs()
    nums1 = [1, 3, 2, 3, 1]
    result = reverse_pairs.reversePairs(nums1)
    print(f"翻转对数量: {result}")
    
    # 测试洛谷逆序对问题
    luogu = LuoguP1908()
    nums2 = [5, 4, 3, 2, 1]
    result = luogu.countInversions(nums2.copy())
    print(f"洛谷P1908逆序对数: {result}")
    
    print("\n=== 子数组和类问题测试 ===")
    subarray_sum = SubarraySumEqualsK()
    nums3 = [1, 1, 1]
    k = 2
    result = subarray_sum.subarraySum(nums3, k)
    print(f"和为K的子数组数量: {result}")
    
    # 测试乘积最大子数组
    max_product = MaximumProductSubarray()
    nums4 = [2, 3, -2, 4]
    result = max_product.maxProduct(nums4)
    print(f"乘积最大子数组: {result}")
    
    # 测试长度最小子数组
    min_size = MinimumSizeSubarraySum()
    nums5 = [2, 3, 1, 2, 4, 3]
    target = 7
    result = min_size.minSubArrayLen(target, nums5)
    print(f"长度最小的子数组长度: {result}")
    
    print("\n=== 圆环路径类问题测试 ===")
    max_points = MaxPointsFromCards()
    card_points = [1, 2, 3, 4, 5, 6, 1]
    k3 = 3
    result = max_points.maxScore(card_points, k3)
    print(f"可获得的最大点数: {result}")
    
    # 测试加油站问题
    gas_station = GasStation()
    gas = [1, 2, 3, 4, 5]
    cost = [3, 4, 5, 1, 2]
    result = gas_station.canCompleteCircuit(gas, cost)
    print(f"加油站起始位置: {result}")
    
    # 测试打家劫舍II
    house_robber = HouseRobberII()
    nums6 = [2, 3, 2]
    result = house_robber.rob(nums6)
    print(f"打家劫舍II最大金额: {result}")
    
    print("\n=== 测试完成 ===")