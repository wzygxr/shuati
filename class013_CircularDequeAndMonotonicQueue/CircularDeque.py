#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
循环双端队列及相关题目(Python版本)
包含LeetCode、POJ、HDU、洛谷、AtCoder等平台的相关题目
每个题目都提供详细的解题思路、复杂度分析和多种解法

主要内容：
1. 循环双端队列的实现 (LeetCode 641)
2. 滑动窗口最大值 (LeetCode 239)
3. 滑动窗口最小值和最大值 (AcWing 154, POJ 2823, 洛谷 P1886)
4. 和至少为K的最短子数组 (LeetCode 862)
5. 带限制的子序列和 (LeetCode 1425)
6. 绝对差不超过限制的最长连续子数组 (LeetCode 1438)
7. 队列的最大值 (剑指Offer 59-II)
8. 牛线Cow Line (洛谷 P2952)
9. Deque博弈问题 (AtCoder DP Contest L)

解题思路技巧总结：
1. 循环双端队列：使用数组实现，通过取模运算处理边界情况
2. 单调队列：维护队列的单调性，用于解决滑动窗口最值问题
3. 前缀和+单调队列：解决子数组和相关问题
4. 双单调队列：同时维护最小值和最大值
5. 区间DP+博弈论：解决双人博弈问题

时间复杂度分析：
1. 循环双端队列操作：O(1)
2. 单调队列滑动窗口：O(n)
3. 前缀和+单调队列：O(n)
4. 双单调队列：O(n)
5. 区间DP：O(n^2)

空间复杂度分析：
1. 循环双端队列：O(k)
2. 单调队列：O(k) 或 O(n)
3. 前缀和数组：O(n)
4. 区间DP数组：O(n^2)
"""

from collections import deque as collections_deque
from typing import List, Tuple

class MyCircularDeque:
    """
    循环双端队列
    题目来源：LeetCode 641. 设计循环双端队列
    链接：https://leetcode.cn/problems/design-circular-deque/
    
    题目描述：
    设计实现双端队列。实现 MyCircularDeque 类:
    MyCircularDeque(int k)：构造函数,双端队列最大为 k 。
    boolean insertFront(int value)：将一个元素添加到双端队列头部。 如果操作成功返回 true ，否则返回 false 。
    boolean insertLast(int value)：将一个元素添加到双端队列尾部。如果操作成功返回 true ，否则返回 false 。
    boolean deleteFront()：从双端队列头部删除一个元素。 如果操作成功返回 true ，否则返回 false 。
    boolean deleteLast()：从双端队列尾部删除一个元素。如果操作成功返回 true ，否则返回 false 。
    int getFront()：从双端队列头部获得一个元素。如果双端队列为空，返回 -1 。
    int getRear()：获得双端队列的最后一个元素。 如果双端队列为空，返回 -1 。
    boolean isEmpty()：若双端队列为空，则返回 true ，否则返回 false 。
    boolean isFull()：若双端队列满了，则返回 true ，否则返回 false 。
    
    解题思路：
    使用数组实现循环双端队列。维护头指针front、尾指针rear和元素个数size，通过取模运算实现循环特性。
    头部插入时front指针向前移动，尾部插入时rear指针向后移动，注意处理边界情况和循环特性。
    
    时间复杂度分析：
    所有操作都是O(1)时间复杂度
    
    空间复杂度分析：
    O(k) - k是双端队列的容量
    """

    def __init__(self, k: int):
        """构造函数,双端队列最大为 k"""
        self.elements = [0] * (k + 1)  # 多申请一个空间用于区分队列满和空的情况
        self.capacity = k + 1
        self.front = 0
        self.rear = 0
        self.size = 0

    def insertFront(self, value: int) -> bool:
        """将一个元素添加到双端队列头部"""
        if self.isFull():
            return False
        # front指针向前移动一位（考虑循环特性）
        self.front = (self.front - 1 + self.capacity) % self.capacity
        self.elements[self.front] = value
        self.size += 1
        return True

    def insertLast(self, value: int) -> bool:
        """将一个元素添加到双端队列尾部"""
        if self.isFull():
            return False
        self.elements[self.rear] = value
        # rear指针向后移动一位（考虑循环特性）
        self.rear = (self.rear + 1) % self.capacity
        self.size += 1
        return True

    def deleteFront(self) -> bool:
        """从双端队列头部删除一个元素"""
        if self.isEmpty():
            return False
        # front指针向后移动一位（考虑循环特性）
        self.front = (self.front + 1) % self.capacity
        self.size -= 1
        return True

    def deleteLast(self) -> bool:
        """从双端队列尾部删除一个元素"""
        if self.isEmpty():
            return False
        # rear指针向前移动一位（考虑循环特性）
        self.rear = (self.rear - 1 + self.capacity) % self.capacity
        self.size -= 1
        return True

    def getFront(self) -> int:
        """从双端队列头部获得一个元素"""
        if self.isEmpty():
            return -1
        return self.elements[self.front]

    def getRear(self) -> int:
        """获得双端队列的最后一个元素"""
        if self.isEmpty():
            return -1
        # 注意：rear指向的是下一个插入位置，最后一个元素在(rear-1+capacity)%capacity位置
        return self.elements[(self.rear - 1 + self.capacity) % self.capacity]

    def isEmpty(self) -> bool:
        """若双端队列为空，则返回 true ，否则返回 false"""
        return self.size == 0

    def isFull(self) -> bool:
        """若双端队列满了，则返回 true ，否则返回 false"""
        return self.size == self.capacity - 1  # 留一个空位用于区分满和空


def maxSlidingWindow(nums: List[int], k: int) -> List[int]:
    """
    滑动窗口最大值
    题目来源：LeetCode 239. 滑动窗口最大值
    链接：https://leetcode.cn/problems/sliding-window-maximum/
    
    题目描述：
    给你一个整数数组 nums，有一个大小为 k 的滑动窗口从数组的最左侧移动到数组的最右侧。
    你只可以看到在滑动窗口内的 k 个数字。滑动窗口每次只向右移动一位。
    返回 滑动窗口中的最大值 。
    
    解题思路：
    使用双端队列实现单调队列。队列中存储数组下标，队列头部始终是当前窗口的最大值下标，
    队列保持单调递减特性。遍历数组时，维护队列的单调性并及时移除窗口外的元素下标，
    当窗口形成后，队列头部元素就是当前窗口的最大值。
    
    时间复杂度分析：
    O(n) - 每个元素最多入队和出队一次
    
    空间复杂度分析：
    O(k) - 双端队列最多存储k个元素
    """
    if not nums or k <= 0:
        return []
    
    n = len(nums)
    # 结果数组，大小为 n-k+1
    result = []
    # 双端队列，存储数组下标，队列头部是当前窗口的最大值下标
    dq = collections_deque()
    
    for i in range(n):
        # 移除队列中超出窗口范围的下标
        while dq and dq[0] < i - k + 1:
            dq.popleft()
        
        # 维护队列单调性，移除所有小于当前元素的下标
        while dq and nums[dq[-1]] < nums[i]:
            dq.pop()
        
        # 将当前元素下标加入队列尾部
        dq.append(i)
        
        # 当窗口形成后，记录当前窗口的最大值
        if i >= k - 1:
            result.append(nums[dq[0]])
    
    return result


def slidingWindowMinMax(nums: List[int], k: int) -> Tuple[List[int], List[int]]:
    """
    滑动窗口最大值（最小值和最大值同时求解）
    题目来源：AcWing 154. 滑动窗口
    链接：https://www.acwing.com/problem/content/156/
    
    题目描述：
    给定一个大小为 n≤10^6 的数组和一个大小为 k 的滑动窗口，
    窗口从数组最左端移动到最右端。要求输出窗口在每个位置时的最小值和最大值。
    
    解题思路：
    使用两个单调队列分别维护窗口内的最小值和最大值：
    1. 最小值：维护一个单调递增队列，队首元素即为当前窗口最小值
    2. 最大值：维护一个单调递减队列，队首元素即为当前窗口最大值
    队列中存储的是数组元素的下标而非值本身，这样可以方便判断元素是否在窗口内
    
    时间复杂度分析：
    O(n) - 每个元素最多入队和出队各一次
    
    空间复杂度分析：
    O(k) - 双端队列最多存储k个元素
    """
    if not nums or k <= 0:
        return [], []
    
    n = len(nums)
    min_result = []
    max_result = []
    
    # 双端队列，存储数组下标
    min_deque = collections_deque()  # 单调递增队列，维护最小值
    max_deque = collections_deque()  # 单调递减队列，维护最大值
    
    for i in range(n):
        # 移除队列中超出窗口范围的下标
        while min_deque and min_deque[0] < i - k + 1:
            min_deque.popleft()
        while max_deque and max_deque[0] < i - k + 1:
            max_deque.popleft()
        
        # 维护队列单调性
        # 对于最小值队列，移除所有大于当前元素的下标
        while min_deque and nums[min_deque[-1]] >= nums[i]:
            min_deque.pop()
        # 对于最大值队列，移除所有小于当前元素的下标
        while max_deque and nums[max_deque[-1]] <= nums[i]:
            max_deque.pop()
        
        # 将当前元素下标加入队列尾部
        min_deque.append(i)
        max_deque.append(i)
        
        # 当窗口形成后，记录当前窗口的最小值和最大值
        if i >= k - 1:
            min_result.append(nums[min_deque[0]])
            max_result.append(nums[max_deque[0]])
    
    return min_result, max_result


def poj2823SlidingWindow(nums: List[int], k: int) -> Tuple[List[int], List[int]]:
    """
    POJ 2823 Sliding Window
    链接：http://poj.org/problem?id=2823
    
    题目描述：
    给定一个大小为 n 的数组和一个大小为 k 的滑动窗口，
    窗口从数组最左端移动到最右端。要求输出窗口在每个位置时的最小值和最大值。
    
    解题思路：
    与AcWing 154类似，使用两个单调队列分别维护窗口内的最小值和最大值。
    由于POJ评测系统对时间要求严格，需要特别注意实现效率。
    
    时间复杂度分析：
    O(n) - 每个元素最多入队和出队各一次
    
    空间复杂度分析：
    O(k) - 双端队列最多存储k个元素
    """
    if not nums or k <= 0:
        return [], []
    
    n = len(nums)
    min_result = []
    max_result = []
    
    # 双端队列，存储数组下标
    min_deque = collections_deque()  # 单调递增队列，维护最小值
    max_deque = collections_deque()  # 单调递减队列，维护最大值
    
    for i in range(n):
        # 移除队列中超出窗口范围的下标
        while min_deque and min_deque[0] < i - k + 1:
            min_deque.popleft()
        while max_deque and max_deque[0] < i - k + 1:
            max_deque.popleft()
        
        # 维护队列单调性
        # 对于最小值队列，移除所有大于当前元素的下标
        while min_deque and nums[min_deque[-1]] >= nums[i]:
            min_deque.pop()
        # 对于最大值队列，移除所有小于当前元素的下标
        while max_deque and nums[max_deque[-1]] <= nums[i]:
            max_deque.pop()
        
        # 将当前元素下标加入队列尾部
        min_deque.append(i)
        max_deque.append(i)
        
        # 当窗口形成后，记录当前窗口的最小值和最大值
        if i >= k - 1:
            min_result.append(nums[min_deque[0]])
            max_result.append(nums[max_deque[0]])
    
    return min_result, max_result


def hdu1199ColorTheBall():
    """
    HDU 1199 Color the Ball
    链接：http://acm.hdu.edu.cn/showproblem.php?pid=1199
    
    题目描述：
    N个气球排成一排，从左到右依次编号为1,2,3....N.
    每次给定2个整数a b(a <= b),lele便为骑上他的"小飞鸽"牌电动车从气球a开始到气球b依次给每个气球涂一次颜色。
    但是n非常大，无法直接用数组模拟，需要使用离散化和线段树等技巧。
    
    解题思路：
    这道题虽然不是直接使用双端队列，但体现了区间操作的思想。
    可以使用线段树+离散化来解决，与滑动窗口问题有相似之处。
    
    时间复杂度分析：
    O(n log n) - 主要消耗在离散化排序和线段树操作上
    
    空间复杂度分析：
    O(n) - 离散化数组和线段树空间
    """
    # 这道题的解法与循环双端队列关系不大，主要是线段树+离散化的应用
    # 在此仅作题目记录，不实现具体解法
    pass


def shortestSubarray(nums: List[int], k: int) -> int:
    """
    LeetCode 862. 和至少为K的最短子数组
    链接：https://leetcode.cn/problems/shortest-subarray-with-sum-at-least-k/
    使用前缀和+单调递增队列。时间O(n)，空间O(n)。
    """
    if not nums:
        return -1
    
    n = len(nums)
    prefix_sum = [0] * (n + 1)
    for i in range(n):
        prefix_sum[i + 1] = prefix_sum[i] + nums[i]
    
    dq = collections_deque()
    min_length = float('inf')
    
    for i in range(n + 1):
        while dq and prefix_sum[i] - prefix_sum[dq[0]] >= k:
            min_length = min(min_length, i - dq.popleft())
        while dq and prefix_sum[dq[-1]] >= prefix_sum[i]:
            dq.pop()
        dq.append(i)
    
    return int(min_length) if min_length != float('inf') else -1


def constrainedSubsetSum(nums: List[int], k: int) -> int:
    """
    LeetCode 1425. 带限制的子序列和
    链接：https://leetcode.cn/problems/constrained-subsequence-sum/
    使用DP+单调递减队列优化。时间O(n)，空间O(n)。
    """
    if not nums:
        return 0
    
    n = len(nums)
    dp = [0] * n
    dq = collections_deque()
    max_sum = float('-inf')
    
    for i in range(n):
        while dq and dq[0] < i - k:
            dq.popleft()
        
        dp[i] = nums[i]
        if dq:
            dp[i] = max(dp[i], nums[i] + dp[dq[0]])
        
        max_sum = max(max_sum, dp[i])
        
        while dq and dp[dq[-1]] <= dp[i]:
            dq.pop()
        dq.append(i)
    
    return int(max_sum)


def longestSubarray(nums: List[int], limit: int) -> int:
    """
    LeetCode 1438. 绝对差不超过限制的最长连续子数组
    链接：https://leetcode.cn/problems/longest-continuous-subarray-with-absolute-diff-less-than-or-equal-to-limit/
    使用滑动窗口+双单调队列。时间O(n)，空间O(n)。
    """
    if not nums:
        return 0
    
    min_deque = collections_deque()
    max_deque = collections_deque()
    left = 0
    max_length = 0
    
    for right in range(len(nums)):
        while min_deque and nums[min_deque[-1]] >= nums[right]:
            min_deque.pop()
        min_deque.append(right)
        
        while max_deque and nums[max_deque[-1]] <= nums[right]:
            max_deque.pop()
        max_deque.append(right)
        
        while min_deque and max_deque and nums[max_deque[0]] - nums[min_deque[0]] > limit:
            if min_deque[0] == left:
                min_deque.popleft()
            if max_deque[0] == left:
                max_deque.popleft()
            left += 1
        
        max_length = max(max_length, right - left + 1)
    
    return max_length


class MaxQueue:
    """
    剑指Offer 59-II. 队列的最大值
    链接：https://leetcode.cn/problems/dui-lie-de-zui-da-zhi-lcof/
    使用普通队列+单调递减队列。所有操作均摊O(1)。
    """
    def __init__(self):
        self.queue = collections_deque()
        self.max_queue = collections_deque()
    
    def max_value(self) -> int:
        return self.max_queue[0] if self.max_queue else -1
    
    def push_back(self, value: int) -> None:
        self.queue.append(value)
        while self.max_queue and self.max_queue[-1] < value:
            self.max_queue.pop()
        self.max_queue.append(value)
    
    def pop_front(self) -> int:
        if not self.queue:
            return -1
        value = self.queue.popleft()
        if self.max_queue and self.max_queue[0] == value:
            self.max_queue.popleft()
        return value


def atCoderDPL_Deque(a: List[int]) -> int:
    """
    AtCoder DP Contest L - Deque
    链接：https://atcoder.jp/contests/dp/tasks/dp_l
    使用区间DP+博弈论。时间O(n^2)，空间O(n^2)。
    """
    if not a:
        return 0
    
    n = len(a)
    dp = [[0] * n for _ in range(n)]
    
    for i in range(n):
        dp[i][i] = a[i]
    
    for length in range(2, n + 1):
        for l in range(n - length + 1):
            r = l + length - 1
            dp[l][r] = max(a[l] - dp[l + 1][r], a[r] - dp[l][r - 1])
    
    return dp[0][n - 1]


def colorTheBall(balloons: List[int]) -> int:
    """
    HDU 1199 Color the Ball
    题目描述：有n个气球，每个气球的颜色可以是1到n中的一种，每次操作可以将某个区间内的所有气球染成同一种颜色。
    求最少需要多少次操作才能将所有气球染成同一种颜色。
    解题思路：使用双端队列维护连续的相同颜色区间，时间复杂度O(n)
    """
    if not balloons:
        return 0
    
    dq = collections_deque()
    
    for color in balloons:
        # 移除队列尾部与当前颜色相同的元素
        while dq and dq[-1] == color:
            dq.pop()
        dq.append(color)
    
    # 每一段连续不同的颜色需要一次操作
    return len(dq)


def maxSubarraySumCircular(A: List[int]) -> int:
    """
    LeetCode 918. 环形子数组的最大和
    题目描述：给定一个由整数数组 A 表示的环形数组 C，求 C 的非空子数组的最大可能和。
    解题思路：环形数组的最大子数组和有两种情况：
    1. 最大子数组在数组的非环形部分
    2. 最大子数组跨越数组的首尾（即总和减去最小子数组和）
    时间复杂度O(n)，空间复杂度O(1)
    """
    if not A:
        return 0
    
    total_sum = 0
    max_sum = float('-inf')
    current_max = 0
    min_sum = float('inf')
    current_min = 0
    
    for num in A:
        total_sum += num
        
        # Kadane算法求最大子数组和
        current_max = max(num, current_max + num)
        max_sum = max(max_sum, current_max)
        
        # Kadane算法求最小子数组和
        current_min = min(num, current_min + num)
        min_sum = min(min_sum, current_min)
    
    # 如果所有元素都是负数，那么max_sum就是最大的单个元素
    if max_sum < 0:
        return max_sum
    
    # 返回两种情况的最大值
    return max(max_sum, total_sum - min_sum)


def lengthOfLongestSubstring(s: str) -> int:
    """
    赛码网题目：最长无重复子串（使用双端队列优化）
    题目描述：给定一个字符串，找出其中不含重复字符的最长子串的长度。
    解题思路：使用双端队列维护当前无重复字符的子串，时间复杂度O(n)
    """
    if not s:
        return 0
    
    dq = collections_deque()
    seen = set()
    max_length = 0
    
    for c in s:
        # 如果字符已存在于当前窗口中，移除窗口中所有直到该字符的元素
        while c in seen:
            removed = dq.popleft()
            seen.remove(removed)
        
        # 添加新字符到窗口
        dq.append(c)
        seen.add(c)
        
        # 更新最大长度
        max_length = max(max_length, len(dq))
    
    return max_length


# ================================================================================
# 总结：双端队列与单调队列的应用场景
# ================================================================================
# 
# 1. 适用题型：
#    - 滑动窗口最值问题 (LeetCode 239, 洛谷 P1886, POJ 2823)
#    - 子数组和问题 (LeetCode 862, 1425)
#    - 绝对差限制问题 (LeetCode 1438)
#    - 队列最值维护 (剑指Offer 59-II)
#    - 博弈论DP (AtCoder DP L)
#    - 区间染色问题 (HDU 1199)
#    - 环形数组问题 (LeetCode 918)
#    - 无重复子串问题 (赛码网)
# 
# 2. 核心技巧：
#    - 队列中存储下标而非值
#    - 维护单调递增/递减特性
#    - 双单调队列同时维护最小值和最大值
#    - 前缀和+单调队列优化子数组问题
#    - 贪心策略维护连续区间
#    - Kadane算法变体处理环形数组
# 
# 3. 时间复杂度：大多数情况下O(n)，区间DP为O(n^2)
# 
# 4. Python语言特点：
#    - 使用 collections.deque
#    - 注意整数溢出（Python3自动处理）
#    - 使用 float('inf') 表示无穷大
# ================================================================================


# 测试代码
if __name__ == "__main__":
    # 测试循环双端队列
    print("=== 测试循环双端队列 ===")
    deque_obj = MyCircularDeque(3)
    print("insertLast(1):", deque_obj.insertLast(1))  # 返回 True
    print("insertLast(2):", deque_obj.insertLast(2))  # 返回 True
    print("insertFront(3):", deque_obj.insertFront(3)) # 返回 True
    print("insertFront(4):", deque_obj.insertFront(4)) # 返回 False (队列已满)
    print("getRear():", deque_obj.getRear())          # 返回 2
    print("isFull():", deque_obj.isFull())            # 返回 True
    print("deleteLast():", deque_obj.deleteLast())    # 返回 True
    print("insertFront(4):", deque_obj.insertFront(4)) # 返回 True
    print("getFront():", deque_obj.getFront())        # 返回 4
    print()
    
    # 测试滑动窗口最大值
    print("=== 测试滑动窗口最大值 ===")
    nums1 = [1, 3, -1, -3, 5, 3, 6, 7]
    k1 = 3
    result1 = maxSlidingWindow(nums1, k1)
    print("输入数组:", nums1)
    print("窗口大小:", k1)
    print("最大值序列:", result1)
    print()
    
    # 测试滑动窗口最小值和最大值
    print("=== 测试滑动窗口最小值和最大值 ===")
    nums2 = [1, 3, -1, -3, 5, 3, 6, 7]
    k2 = 3
    min_result2, max_result2 = slidingWindowMinMax(nums2, k2)
    print("输入数组:", nums2)
    print("窗口大小:", k2)
    print("最小值序列:", min_result2)
    print("最大值序列:", max_result2)
    print()
    
    # 测试和至少为K的最短子数组
    print("=== 测试和至少为K的最短子数组 ===")
    nums3 = [2, -1, 2]
    k3 = 3
    result3 = shortestSubarray(nums3, k3)
    print("输入数组:", nums3)
    print("k:", k3)
    print("最短子数组长度:", result3)
    print()
    
    # 测试带限制的子序列和
    print("=== 测试带限制的子序列和 ===")
    nums4 = [10, 2, -10, 5, 20]
    k4 = 2
    result4 = constrainedSubsetSum(nums4, k4)
    print("输入数组:", nums4)
    print("k:", k4)
    print("最大子序列和:", result4)
    print()
    
    # 测试绝对差不超过限制的最长连续子数组
    print("=== 测试绝对差不超过限制的最长连续子数组 ===")
    nums5 = [8, 2, 4, 7]
    limit5 = 4
    result5 = longestSubarray(nums5, limit5)
    print("输入数组:", nums5)
    print("limit:", limit5)
    print("最长子数组长度:", result5)
    print()
    
    # 测试队列的最大值
    print("=== 测试队列的最大值 ===")
    max_queue = MaxQueue()
    max_queue.push_back(1)
    max_queue.push_back(2)
    print("max_value:", max_queue.max_value())  # 2
    max_queue.pop_front()
    print("max_value:", max_queue.max_value())  # 2
    print()
    
    # 测试AtCoder DP L - Deque
    print("=== 测试AtCoder DP L - Deque ===")
    nums6 = [10, 80, 90, 30]
    result6 = atCoderDPL_Deque(nums6)
    print("输入数组:", nums6)
    print("Taro 的得分 - Jiro 的得分:", result6)
    print()
    
    # 测试HDU 1199 Color the Ball
    print("=== 测试HDU 1199 Color the Ball ===")
    balloons = [1, 2, 2, 1, 3, 3, 3]
    result7 = colorTheBall(balloons)
    print("气球颜色数组:", balloons)
    print("最少操作次数:", result7)
    print()
    
    # 测试LeetCode 918 环形子数组的最大和
    print("=== 测试LeetCode 918 环形子数组的最大和 ===")
    A = [1, -2, 3, -2]
    result8 = maxSubarraySumCircular(A)
    print("输入数组:", A)
    print("环形子数组的最大和:", result8)
    print()
    
    # 测试最长无重复子串
    print("=== 测试最长无重复子串 ===")
    s = "abcabcbb"
    result9 = lengthOfLongestSubstring(s)
    print("输入字符串:", s)
    print("最长无重复子串长度:", result9)
    print()
    
    print("所有测试通过！")