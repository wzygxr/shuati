import heapq
from typing import List, Optional
from collections import defaultdict

"""
堆排序Python实现及相关题目

本文件包含堆排序的基本实现以及多个经典堆相关题目的完整解法
每个解法都包含详细的时间复杂度、空间复杂度分析和工程化考量

作者: 算法之旅
创建时间: 2024年
版本: 1.0

主要功能:
1. 堆排序的两种实现方式
2. 多个经典堆相关问题的Python解法
3. 详细的注释和复杂度分析
4. 工程化考量和异常处理

题目来源平台:
- LeetCode (力扣): https://leetcode.cn/
- LintCode (炼码): https://www.lintcode.com/
- HackerRank: https://www.hackerrank.com/
- 洛谷 (Luogu): https://www.luogu.com.cn/
- AtCoder: https://atcoder.jp/
- 牛客网: https://www.nowcoder.com/
- CodeChef: https://www.codechef.com/
- SPOJ: https://www.spoj.com/
- Project Euler: https://projecteuler.net/
- HackerEarth: https://www.hackerearth.com/
- 计蒜客: https://www.jisuanke.com/
- USACO: http://usaco.org/
- UVa OJ: https://onlinejudge.org/
- Codeforces: https://codeforces.com/
- POJ: http://poj.org/
- HDU: http://acm.hdu.edu.cn/
- 剑指Offer: 面试经典题目
- 杭电 OJ: http://acm.hdu.edu.cn/
- LOJ: https://loj.ac/
- acwing: https://www.acwing.com/
- 赛码: https://www.acmcoder.com/
- zoj: http://acm.zju.edu.cn/
- MarsCode: https://www.marscode.cn/
- TimusOJ: http://acm.timus.ru/
- AizuOJ: http://judge.u-aizu.ac.jp/
- Comet OJ: https://www.cometoj.com/
- 杭州电子科技大学 OJ
"""

class HeapSortSolution:
    @staticmethod
    def sort_array(nums: List[int]) -> List[int]:
        """
        堆排序主函数
        时间复杂度: O(n log n)
        空间复杂度: O(1)
        """
        if len(nums) <= 1:
            return nums
        
        # heap_sort2为从底到顶建堆然后排序
        HeapSortSolution.heap_sort2(nums)
        return nums

    @staticmethod
    def heap_insert(arr: List[int], i: int) -> None:
        """i位置的数，向上调整大根堆"""
        while arr[i] > arr[(i - 1) // 2]:
            arr[i], arr[(i - 1) // 2] = arr[(i - 1) // 2], arr[i]
            i = (i - 1) // 2

    @staticmethod
    def heapify(arr: List[int], i: int, size: int) -> None:
        """
        i位置的数，变小了，又想维持大根堆结构
        向下调整大根堆
        当前堆的大小为size
        """
        l = i * 2 + 1
        while l < size:
            # 有左孩子，l
            # 右孩子，l+1
            # 评选，最强的孩子，是哪个下标的孩子
            best = l + 1 if l + 1 < size and arr[l + 1] > arr[l] else l
            # 上面已经评选了最强的孩子，接下来，当前的数和最强的孩子之前，最强下标是谁
            best = best if arr[best] > arr[i] else i
            if best == i:
                break
            arr[best], arr[i] = arr[i], arr[best]
            i = best
            l = i * 2 + 1

    @staticmethod
    def heap_sort1(arr: List[int]) -> None:
        """
        从顶到底建立大根堆，O(n * logn)
        依次弹出堆内最大值并排好序，O(n * logn)
        整体时间复杂度O(n * logn)
        """
        n = len(arr)
        for i in range(n):
            HeapSortSolution.heap_insert(arr, i)
        size = n
        while size > 1:
            arr[0], arr[size - 1] = arr[size - 1], arr[0]
            size -= 1
            HeapSortSolution.heapify(arr, 0, size)

    @staticmethod
    def heap_sort2(arr: List[int]) -> None:
        """
        从底到顶建立大根堆，O(n)
        依次弹出堆内最大值并排好序，O(n * logn)
        整体时间复杂度O(n * logn)
        """
        n = len(arr)
        for i in range(n - 1, -1, -1):
            HeapSortSolution.heapify(arr, i, n)
        size = n
        while size > 1:
            arr[0], arr[size - 1] = arr[size - 1], arr[0]
            size -= 1
            HeapSortSolution.heapify(arr, 0, size)
    
    """
    补充题目1: LeetCode 215. 数组中的第K个最大元素
    链接: https://leetcode.cn/problems/kth-largest-element-in-an-array/
    题目描述: 给定整数数组 nums 和整数 k，请返回数组中第 k 个最大的元素
    
    解题思路:
    方法1: 使用堆排序完整排序后取第k个元素 - 时间复杂度 O(n log n)
    方法2: 使用大小为k的最小堆维护前k个最大元素 - 时间复杂度 O(n log k)
    方法3: 快速选择算法 - 平均时间复杂度 O(n)
    
    最优解: 快速选择算法，但这里展示堆的解法
    时间复杂度: O(n log k) - 遍历数组O(n)，每次堆操作O(log k)
    空间复杂度: O(k) - 堆的大小
    
    相关题目:
    - 剑指Offer 40. 最小的k个数
    - 牛客网 BM46 最小的K个数
    - LintCode 461. Kth Smallest Numbers in Unsorted Array
    """
    @staticmethod
    def find_kth_largest(nums: List[int], k: int) -> int:
        # 使用最小堆维护前k个最大元素
        min_heap = []
        
        for num in nums:
            if len(min_heap) < k:
                heapq.heappush(min_heap, num)
            elif num > min_heap[0]:
                heapq.heapreplace(min_heap, num)
        
        return min_heap[0]
    
    """
    补充题目2: LeetCode 347. 前 K 个高频元素
    链接: https://leetcode.cn/problems/top-k-frequent-elements/
    题目描述: 给你一个整数数组 nums 和一个整数 k，请你返回其中出现频率前 k 高的元素
    
    解题思路:
    1. 使用哈希表统计每个元素的频率 - 时间复杂度 O(n)
    2. 使用大小为k的最小堆维护前k个高频元素 - 时间复杂度 O(n log k)
    3. 遍历哈希表，维护堆的大小为k
    4. 从堆中取出元素即为结果
    
    时间复杂度: O(n log k) - n为数组长度
    空间复杂度: O(n + k) - 哈希表O(n)，堆O(k)
    
    是否最优解: 是，满足题目要求的复杂度优于O(n log n)
    
    相关题目:
    - LeetCode 692. 前K个高频单词
    - LintCode 1297. 统计右侧小于当前元素的个数
    """
    @staticmethod
    def top_k_frequent(nums: List[int], k: int) -> List[int]:
        # 1. 统计频率
        freq_map = defaultdict(int)
        for num in nums:
            freq_map[num] += 1
        
        # 2. 使用最小堆维护前k个高频元素
        # 堆中存储的是元素值，比较依据是频率
        min_heap = []
        
        # 3. 遍历频率表，维护堆大小为k
        for num, freq in freq_map.items():
            if len(min_heap) < k:
                heapq.heappush(min_heap, (freq, num))
            elif freq > min_heap[0][0]:
                heapq.heapreplace(min_heap, (freq, num))
        
        # 4. 构造结果数组
        result = [num for freq, num in min_heap]
        return result
    
    """
    补充题目3: LeetCode 295. 数据流的中位数
    链接: https://leetcode.cn/problems/find-median-from-data-stream/
    题目描述: 中位数是有序整数列表中的中间值。如果列表的大小是偶数，则没有中间值，中位数是两个中间值的平均值
    
    解题思路:
    使用两个堆：
    1. 最大堆max_heap存储较小的一半元素
    2. 最小堆min_heap存储较大的一半元素
    3. 保持两个堆的大小平衡（差值不超过1）
    
    时间复杂度: 
    - 添加元素: O(log n) - 堆的插入和调整
    - 查找中位数: O(1) - 直接访问堆顶
    空间复杂度: O(n) - 存储所有元素
    
    是否最优解: 是，这是处理动态中位数的经典解法
    
    相关题目:
    - 剑指Offer 41. 数据流中的中位数
    - HackerRank Find the Running Median
    - 牛客网 NC134. 数据流中的中位数
    - AtCoder ABC 127F - Absolute Minima
    """
    class MedianFinder:
        def __init__(self):
            # 存储较小一半元素的最大堆（Python没有最大堆，使用相反数模拟）
            self.max_heap = []
            # 存储较大一半元素的最小堆
            self.min_heap = []
        
        """
        添加数字到数据结构中
        时间复杂度: O(log n)
        """
        def add_num(self, num: int) -> None:
            # 1. 根据num与两个堆堆顶的比较结果决定插入哪个堆
            if not self.max_heap or num <= -self.max_heap[0]:
                heapq.heappush(self.max_heap, -num)
            else:
                heapq.heappush(self.min_heap, num)
            
            # 2. 平衡两个堆的大小
            # 如果max_heap比min_heap多2个元素，则移动一个元素到min_heap
            if len(self.max_heap) > len(self.min_heap) + 1:
                heapq.heappush(self.min_heap, -heapq.heappop(self.max_heap))
            # 如果min_heap比max_heap多1个元素，则移动一个元素到max_heap
            elif len(self.min_heap) > len(self.max_heap) + 1:
                heapq.heappush(self.max_heap, -heapq.heappop(self.min_heap))
        
        """
        查找当前数据结构中的中位数
        时间复杂度: O(1)
        """
        def find_median(self) -> float:
            # 如果两个堆大小相等，返回两个堆顶的平均值
            if len(self.max_heap) == len(self.min_heap):
                return (-self.max_heap[0] + self.min_heap[0]) / 2.0
            # 如果max_heap多一个元素，返回其堆顶
            elif len(self.max_heap) > len(self.min_heap):
                return -self.max_heap[0]
            # 如果min_heap多一个元素，返回其堆顶
            else:
                return self.min_heap[0]
    
    """
    补充题目4: LeetCode 23. 合并K个升序链表
    链接: https://leetcode.cn/problems/merge-k-sorted-lists/
    题目描述: 给你一个链表数组，每个链表都已经按升序排列。请你将所有链表合并到一个升序链表中
    
    解题思路:
    使用最小堆维护K个链表的当前头节点，每次取出最小节点加入结果链表，
    并将该节点的下一个节点加入堆中
    
    时间复杂度: O(N log k) - N为所有节点总数，k为链表数量
    空间复杂度: O(k) - 堆的大小
    
    是否最优解: 是，这是合并K个有序链表的经典解法之一
    
    相关题目:
    - LintCode 104. 合并k个排序链表
    - 牛客网 NC51. 合并k个排序链表
    """
    class ListNode:
        def __init__(self, val: int = 0, next: Optional['HeapSortSolution.ListNode'] = None):
            self.val = val
            self.next = next
    
    @staticmethod
    def merge_k_lists(lists: List[Optional[ListNode]]) -> Optional[ListNode]:
        if not lists:
            return None
        
        # 使用最小堆维护K个链表的当前头节点
        # 堆中存储元组(节点值, 节点)，节点值用于比较
        min_heap = []
        
        # 将所有非空链表的头节点加入堆中
        for i, node in enumerate(lists):
            if node:
                heapq.heappush(min_heap, (node.val, i, node))
        
        # 创建虚拟头节点
        dummy = HeapSortSolution.ListNode(0)
        current = dummy
        
        # 当堆不为空时，不断取出最小节点
        while min_heap:
            # 取出当前最小节点
            val, i, node = heapq.heappop(min_heap)
            
            # 加入结果链表
            current.next = node
            current = current.next
            
            # 将该节点的下一个节点加入堆中（如果不为空）
            if node.next:
                heapq.heappush(min_heap, (node.next.val, i, node.next))
        
        return dummy.next
    
    """
    补充题目5: LeetCode 703. 数据流的第K大元素
    链接: https://leetcode.cn/problems/kth-largest-element-in-a-stream/
    题目描述: 设计一个找到数据流中第 k 大元素的类
    
    解题思路:
    使用大小为k的最小堆维护数据流中前k个最大元素
    堆顶即为第k大元素
    
    时间复杂度: 
    - 初始化: O(n log k) - n为初始数组长度
    - 添加元素: O(log k)
    空间复杂度: O(k) - 堆的大小
    
    是否最优解: 是，这是处理动态第K大元素的经典解法
    
    相关题目:
    - 剑指Offer II 059. 数据流的第K大数值
    """
    class KthLargest:
        def __init__(self, k: int, nums: List[int]):
            self.k = k
            # 使用最小堆维护前k个最大元素
            self.min_heap = []
            
            # 将初始数组中的元素加入堆中
            for num in nums:
                self.add(num)
        
        """
        向数据流中添加元素并返回当前第k大元素
        时间复杂度: O(log k)
        """
        def add(self, val: int) -> int:
            if len(self.min_heap) < self.k:
                heapq.heappush(self.min_heap, val)
            elif val > self.min_heap[0]:
                heapq.heapreplace(self.min_heap, val)
            return self.min_heap[0]
    
    """
    补充题目6: LeetCode 407. 接雨水 II
    链接: https://leetcode.cn/problems/trapping-rain-water-ii/
    题目描述: 给定一个 m x n 的矩阵，其中的值都是非负整数，代表二维高度图每个单元的高度，请计算图中形状最多能接多少体积的雨水。
    
    解题思路:
    使用最小堆实现的Dijkstra算法变种：
    1. 从边界开始，将所有边界点加入最小堆
    2. 维护一个visited数组标记已访问的点
    3. 每次从堆中取出高度最小的点，向四个方向扩展
    4. 如果相邻点未访问过，计算能积累的水量并更新
    
    时间复杂度: O(m*n log(m+n)) - m,n为矩阵维度，堆操作复杂度O(log(m+n))
    空间复杂度: O(m*n) - 存储visited数组
    
    是否最优解: 是，这是解决二维接雨水问题的最优解法之一
    
    相关题目:
    - LeetCode 42. 接雨水
    - LintCode 364. Trapping Rain Water II
    """
    @staticmethod
    def trap_rain_water(heightMap):
        if not heightMap or len(heightMap) <= 2 or len(heightMap[0]) <= 2:
            return 0
        
        import heapq
        m, n = len(heightMap), len(heightMap[0])
        visited = [[False for _ in range(n)] for _ in range(m)]
        min_heap = []
        
        # 初始化：将所有边界点加入堆中
        for i in range(m):
            for j in range(n):
                if i == 0 or i == m - 1 or j == 0 or j == n - 1:
                    heapq.heappush(min_heap, (heightMap[i][j], i, j))
                    visited[i][j] = True
        
        water = 0
        directions = [(-1, 0), (1, 0), (0, -1), (0, 1)]  # 上下左右四个方向
        
        # 从边界开始向内部处理
        while min_heap:
            height, row, col = heapq.heappop(min_heap)
            
            for dr, dc in directions:
                new_row, new_col = row + dr, col + dc
                
                if 0 <= new_row < m and 0 <= new_col < n and not visited[new_row][new_col]:
                    # 计算当前位置能积累的水量
                    if heightMap[new_row][new_col] < height:
                        water += height - heightMap[new_row][new_col]
                    
                    # 将新点加入堆中，高度取最大值（当前点高度或原始高度）
                    heapq.heappush(min_heap, (max(heightMap[new_row][new_col], height), new_row, new_col))
                    visited[new_row][new_col] = True
        
        return water
    
    """
    补充题目7: LeetCode 264. 丑数 II
    链接: https://leetcode.cn/problems/ugly-number-ii/
    题目描述: 给你一个整数 n ，请你找出并返回第 n 个 丑数 。丑数就是质因子只包含 2、3 和 5 的正整数。
    
    解题思路:
    使用最小堆生成有序的丑数序列：
    1. 初始化堆，放入第一个丑数1
    2. 使用集合去重
    3. 每次从堆中取出最小的丑数，乘以2、3、5生成新的丑数
    4. 第n次取出的数即为第n个丑数
    
    时间复杂度: O(n log n) - 进行n次堆操作，每次O(log n)
    空间复杂度: O(n) - 堆和集合的大小
    
    是否最优解: 不是，更优的解法是使用动态规划，时间复杂度O(n)，空间复杂度O(n)
    
    相关题目:
    - LeetCode 313. 超级丑数
    - 牛客网 丑数系列
    """
    @staticmethod
    def nth_ugly_number(n):
        if n <= 0:
            raise ValueError("n must be positive")
        
        import heapq
        # 使用最小堆生成有序丑数
        min_heap = []
        seen = set()
        
        # 初始丑数为1
        heapq.heappush(min_heap, 1)
        seen.add(1)
        
        ugly = 1
        # 生成因子
        factors = [2, 3, 5]
        
        # 循环n次，第n次取出的就是第n个丑数
        for _ in range(n):
            ugly = heapq.heappop(min_heap)
            
            # 生成新的丑数
            for factor in factors:
                next_ugly = ugly * factor
                if next_ugly not in seen:
                    seen.add(next_ugly)
                    heapq.heappush(min_heap, next_ugly)
        
        return ugly
    
    """
    补充题目8: LeetCode 378. 有序矩阵中第 K 小的元素
    链接: https://leetcode.cn/problems/kth-smallest-element-in-a-sorted-matrix/
    题目描述: 给你一个 n x n 矩阵 matrix ，其中每行和每列元素均按升序排序，找到矩阵中第 k 小的元素。
    
    解题思路:
    使用最小堆进行多路归并：
    1. 初始时将第一列的所有元素加入堆中
    2. 每次从堆中取出最小的元素，这是当前的第m小元素
    3. 如果m等于k，返回该元素
    4. 否则，将该元素所在行的下一个元素加入堆中
    
    时间复杂度: O(k log n) - k次堆操作，每次O(log n)
    空间复杂度: O(n) - 堆的大小最多为n
    
    是否最优解: 不是，更优的解法是二分查找，时间复杂度O(n log(max-min))
    
    相关题目:
    - LeetCode 373. 查找和最小的K对数字
    - LeetCode 719. 找出第k小的距离对
    """
    @staticmethod
    def kth_smallest(matrix, k):
        if not matrix or not matrix[0]:
            raise ValueError("Invalid matrix")
        
        import heapq
        n = len(matrix)
        min_heap = []
        
        # 将第一列的所有元素加入堆中
        for i in range(n):
            heapq.heappush(min_heap, (matrix[i][0], i, 0))
        
        # 取出k-1个元素，第k次取出的就是第k小的元素
        value = 0
        for _ in range(k):
            value, row, col = heapq.heappop(min_heap)
            # 如果当前行还有下一个元素，加入堆中
            if col < n - 1:
                heapq.heappush(min_heap, (matrix[row][col + 1], row, col + 1))
        
        return value
    
    """
    补充题目9: LeetCode 239. 滑动窗口最大值
    链接: https://leetcode.cn/problems/sliding-window-maximum/
    题目描述: 给你一个整数数组 nums，有一个大小为 k 的滑动窗口从数组的最左侧移动到数组的最右侧。你只可以看到在滑动窗口内的 k 个数字。滑动窗口每次只向右移动一位。返回滑动窗口中的最大值。
    
    解题思路:
    使用最大堆维护滑动窗口内的元素：
    1. 维护一个最大堆，存储元素值的负数和索引（因为Python的heapq是最小堆）
    2. 窗口滑动时，将新元素加入堆中
    3. 检查堆顶元素是否在当前窗口内，如果不在则移除
    4. 堆顶元素即为当前窗口的最大值
    
    时间复杂度: O(n log k) - n个元素，每个元素最多进出堆一次
    空间复杂度: O(k) - 堆的大小最多为k
    
    是否最优解: 不是，更优的解法是使用单调队列，时间复杂度O(n)
    
    相关题目:
    - 牛客网 BM45 滑动窗口的最大值
    - HackerRank Sliding Window Maximum
    """
    @staticmethod
    def max_sliding_window(nums, k):
        if not nums or k <= 0:
            return []
        
        import heapq
        n = len(nums)
        result = []
        max_heap = []  # 使用负数实现最大堆
        
        # 初始化第一个窗口
        for i in range(k):
            # 存储(-value, index)，这样最小堆就相当于最大堆
            heapq.heappush(max_heap, (-nums[i], i))
        
        result.append(-max_heap[0][0])
        
        # 滑动窗口
        for i in range(k, n):
            # 将新元素加入堆
            heapq.heappush(max_heap, (-nums[i], i))
            
            # 移除不在当前窗口内的堆顶元素
            while max_heap[0][1] <= i - k:
                heapq.heappop(max_heap)
            
            # 记录当前窗口的最大值
            result.append(-max_heap[0][0])
        
        return result
    
    """
    补充题目10: LeetCode 502. IPO
    链接: https://leetcode.cn/problems/ipo/
    题目描述: 假设 力扣（LeetCode）即将开始 IPO 。为了以更高的价格将股票卖给风险投资公司，力扣 希望在 IPO 之前开展一些项目以增加其资本。由于资源有限，它只能在 IPO 之前完成最多 k 个不同的项目。帮助力扣 设计完成最多 k 个不同项目后得到最大总资本的方式。
    
    解题思路:
    使用两个堆组合解决：
    1. 按资本排序的列表，存储可投资项目
    2. 最大堆按利润排序，存储当前可以投资的项目
    3. 每次从列表中取出所有可以投资的项目（资本<=当前总资本）放入最大堆
    4. 从最大堆中取出利润最大的项目投资，增加总资本
    5. 重复3-4步骤k次
    
    时间复杂度: O(N log N) - N为项目数量，排序和堆操作
    空间复杂度: O(N) - 堆的大小
    
    是否最优解: 是，这是解决此类资源分配问题的最优解法
    
    相关题目:
    - LeetCode 857. 雇佣 K 名工人的最低成本
    - LeetCode 1383. 最大的团队表现值
    """
    @staticmethod
    def find_maximized_capital(k, w, profits, capital):
        import heapq
        n = len(profits)
        
        # 构建项目列表
        projects = [(capital[i], profits[i]) for i in range(n)]
        
        # 按资本升序排序
        projects.sort()
        
        # 最大堆存储利润（使用负数实现最大堆）
        max_profit_heap = []
        
        current_capital = w
        project_index = 0
        
        for _ in range(k):
            # 将所有满足资本要求的项目加入最大堆
            while project_index < n and projects[project_index][0] <= current_capital:
                heapq.heappush(max_profit_heap, -projects[project_index][1])
                project_index += 1
            
            # 如果没有可投资的项目，退出循环
            if not max_profit_heap:
                break
            
            # 选择利润最大的项目投资
            current_capital += -heapq.heappop(max_profit_heap)
        
        return current_capital
    
    """
    补充题目11: LeetCode 692. 前K个高频单词
    链接: https://leetcode.cn/problems/top-k-frequent-words/
    题目描述: 给定一个单词列表 words 和一个整数 k ，返回前 k 个出现次数最多的单词。
    
    解题思路:
    1. 使用哈希表统计每个单词的频率
    2. 使用最小堆维护前k个高频单词
    3. 自定义比较器：先按频率升序，频率相同按字典序降序
    4. 最后反转结果列表
    
    时间复杂度: O(n log k) - n为单词数量
    空间复杂度: O(n) - 哈希表和堆
    
    是否最优解: 是，满足题目要求的复杂度
    
    相关题目:
    - LeetCode 347. 前 K 个高频元素
    - LintCode 471. 前K个高频单词
    """
    @staticmethod
    def top_k_frequent_words(words, k):
        from collections import defaultdict
        import heapq
        
        # 1. 统计频率
        freq_map = defaultdict(int)
        for word in words:
            freq_map[word] += 1
        
        # 2. 使用最小堆维护前k个高频单词
        # 自定义比较器：频率升序，频率相同按字典序降序
        min_heap = []
        
        # 3. 遍历频率表，维护堆大小为k
        for word, freq in freq_map.items():
            if len(min_heap) < k:
                heapq.heappush(min_heap, (freq, word))
            else:
                min_freq, min_word = min_heap[0]
                if freq > min_freq or (freq == min_freq and word < min_word):
                    heapq.heapreplace(min_heap, (freq, word))
        
        # 4. 构造结果列表（需要反转）
        result = []
        while min_heap:
            result.append(heapq.heappop(min_heap)[1])
        result.reverse()
        
        return result
    
    """
    堆和堆排序知识点总结：
    
    1. 堆的定义：
    - 堆是一种特殊的完全二叉树，其中每个节点的值都大于等于（或小于等于）其子节点的值
    - 最大堆：每个节点的值都大于等于其子节点的值
    - 最小堆：每个节点的值都小于等于其子节点的值
    
    2. 堆的存储方式：
    - 通常使用数组实现完全二叉树
    - 对于索引为i的节点：
      - 父节点索引：(i - 1) // 2
      - 左子节点索引：2 * i + 1
      - 右子节点索引：2 * i + 2
    
    3. 堆的基本操作：
    - heapify：将以某个节点为根的子树调整为堆结构，时间复杂度O(log n)
    - heapInsert：将新元素插入堆中并调整，时间复杂度O(log n)
    - heapExtractMax/Min：取出并返回堆顶元素，并调整堆结构，时间复杂度O(log n)
    
    4. 堆排序：
    - 建堆：O(n)时间复杂度（从底到顶）或O(n log n)（从顶到底）
    - 排序过程：O(n log n)时间复杂度
    - 空间复杂度：O(1)，原地排序
    
    5. 堆的应用场景：
    - 优先队列实现
    - Top K问题（如前K大、前K小元素）
    - 中位数维护
    - 多路归并排序
    - Dijkstra算法
    - 堆排序
    
    6. Python中的堆实现：
    - 使用heapq模块
    - heapq模块实现的是最小堆
    - 主要函数：heappush, heappop, heappushpop, heapreplace, nlargest, nsmallest
    - 实现最大堆通常有两种方法：
      1. 对值取负数存储
      2. 使用自定义比较器（Python不直接支持，需通过包装类实现）
    
    7. 堆与其他数据结构的比较：
    - 与二叉搜索树相比，堆更适合维护最大/最小值，但不支持快速查找特定元素
    - 与有序数组相比，堆的插入和删除操作更高效，但不支持随机访问
    - 与链表相比，堆的随机访问更高效，但插入和删除操作复杂度相同
    
    8. 堆的工程化考量：
    - 异常处理：处理空堆、索引越界等情况
    - 线程安全：考虑并发环境下的访问问题
    - 性能优化：根据不同场景选择合适的堆实现和参数
    - 内存管理：避免不必要的内存分配
    
    9. 常见解题思路和优化技巧：
    - 最小堆常用于找最大的K个元素，最大堆常用于找最小的K个元素
    - 当需要频繁获取最大值时，考虑使用最大堆
    - 当需要同时维护多组数据的优先级时，考虑使用多个堆
    - 对于滑动窗口问题，可以结合堆与哈希表来优化查找效率
    - 使用堆时注意处理重复元素和边界条件
    
    10. 堆在工程实践中的应用：
    - 任务调度系统：根据优先级执行任务
    - 网络流量控制：优先处理高优先级的数据包
    - 资源分配：如内存分配、CPU调度等
    - 大数据处理：如MapReduce中的排序阶段
    - 缓存系统：淘汰最久未使用或优先级最低的数据
    
    11. 与其他技术领域的联系：
    - 机器学习：在决策树算法中用于特征选择
    - 深度学习：在梯度下降中用于管理批量样本
    - 强化学习：在优先经验回放中管理样本优先级
    - 图像处理：在图像分割和特征提取中用于优先级管理
    - 自然语言处理：在词频统计和主题模型中用于排序
    
    12. 其他平台堆相关题目列表：
    - 力扣(LeetCode)：
      * 23. 合并K个升序链表
      * 215. 数组中的第K个最大元素
      * 295. 数据流的中位数
      * 347. 前K个高频元素
      * 407. 接雨水II
      * 264. 丑数II
      * 378. 有序矩阵中第K小的元素
      * 239. 滑动窗口最大值
      * 502. IPO
      * 703. 数据流的第K大元素
    - LintCode(炼码)：
      * 81. 数据流中位数
      * 545. 前K大数
      * 1319. 最接近原点的K个点
      * 151. 买卖股票的最佳时机III
    - HackerRank：
      * Find the Running Median
      * Jesse and Cookies
      * K Closest Points to Origin
    - CodeChef：
      * PRIME1 - Prime Generator
      * MAXCOMP - Maximum Component Size
    - Codeforces：
      * 1201C. Maximum Median
      * 1355C. Count Triangles
    - POJ：
      * 3253. Fence Repair
      * 2442. Sequence
    - HDU：
      * 1242. Rescue
      * 2159. FATE
    - 牛客：
      * NC142. 最大的奇约数
      * NC134. 数据流中的中位数
    - 剑指Offer：
      * 40. 最小的k个数
      * 41. 数据流中的中位数
    
    13. 堆相关算法的调试与问题定位技巧：
    - 打印堆的状态：在关键操作后打印堆的内容，检查是否符合预期
    - 使用断言验证堆性质：在操作前后验证父节点与子节点的大小关系
    - 性能分析：对于大规模数据，监控堆操作的耗时
    - 边界情况测试：测试空堆、单元素堆、满堆等情况
    
    14. 堆的高级变种：
    - 斐波那契堆：理论上某些操作更高效，但实现复杂
    - 二叉堆：最常见的实现，平衡了效率和实现复杂度
    - 二项堆：支持更高效的合并操作
    - 左偏树：支持高效合并操作的堆结构
    
    15. 堆的复杂度分析深入理解：
    - 建堆操作O(n)复杂度的证明：虽然表面上每个节点下沉是O(log n)，但大部分节点下沉次数较少
    - 堆排序的平均情况与最坏情况复杂度均为O(n log n)
    - 常数因子的影响：在实际应用中，堆排序通常比快速排序慢，但比归并排序快
    """

# 添加测试代码
if __name__ == "__main__":
    # 简单测试代码
    test = [3, 1, 4, 1, 5, 9, 2, 6, 5, 3]
    print("Original array:", test)
    
    HeapSortSolution.sort_array(test)
    print("Sorted array:", test)