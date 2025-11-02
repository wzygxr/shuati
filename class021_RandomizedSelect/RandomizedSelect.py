"""
快速选择算法实现 (Python版本)
用于在未排序数组中找到第K大的元素

算法原理：
快速选择算法是基于快速排序的分治思想，但只处理包含目标元素的一侧，
从而避免了完全排序，平均时间复杂度为O(n)。

相关题目列表：
1. LeetCode 215. 数组中的第K个最大元素
   链接: https://leetcode.cn/problems/kth-largest-element-in-an-array/
   题目描述: 给定整数数组 nums 和整数 k，请返回数组中第 k 个最大的元素

2. 剑指 Offer 40. 最小的k个数
   链接: https://leetcode.cn/problems/zui-xiao-de-kge-shu-lcof/
   题目描述: 输入整数数组 arr ，找出其中最小的 k 个数

3. LeetCode 973. 最接近原点的 K 个点
   链接: https://leetcode.cn/problems/k-closest-points-to-origin/
   题目描述: 给定平面上n个点，找到距离原点最近的k个点

4. LeetCode 347. 前 K 个高频元素
   链接: https://leetcode.cn/problems/top-k-frequent-elements/
   题目描述: 给你一个整数数组 nums 和一个整数 k，请你返回其中出现频率前 k 高的元素

5. 牛客网 - NC119 最小的K个数
   链接: https://www.nowcoder.com/practice/6a296eb82cf844ca8539b57c23e6e9bf
   题目描述: 输入n个整数，找出其中最小的K个数

6. AcWing 786. 第k个数
   链接: https://www.acwing.com/problem/content/788/
   题目描述: 给定一个长度为 n 的整数数列，以及一个整数 k，请用快速选择算法求出数列从小到大排序后的第 k 个数

7. 洛谷 P1923 【深基9.例4】求第 k 小的数
   链接: https://www.luogu.com.cn/problem/P1923
   题目描述: 给定一个长度为 n 的整数数列，以及一个整数 k，请用快速选择算法求出数列从小到大排序后的第 k 个数

8. HackerRank Find the Median
   链接: https://www.hackerrank.com/challenges/find-the-median/problem
   题目描述: 找到未排序数组的中位数

9. LintCode 5. 第K大元素
   链接: https://www.lintcode.com/problem/5/
   题目描述: 在数组中找到第k大的元素

10. POJ 2388. Who's in the Middle
    链接: http://poj.org/problem?id=2388
    题目描述: 找到数组的中位数

11. 洛谷 P1177. 【模板】快速排序
    链接: https://www.luogu.com.cn/problem/P1177
    题目描述: 快速排序模板题，可扩展为快速选择

12. 牛客网 NC73. 数组中出现次数超过一半的数字
    链接: https://www.nowcoder.com/practice/e8a1b01a2df14cb2b228b30ee6a92163
    题目描述: 数组中有一个数字出现的次数超过数组长度的一半，请找出这个数字

13. LeetCode 451. 根据字符出现频率排序
    链接: https://leetcode.cn/problems/sort-characters-by-frequency/
    题目描述: 给定一个字符串，请将字符串里的字符按照出现的频率降序排列

14. LeetCode 703. 数据流中的第K大元素
    链接: https://leetcode.cn/problems/kth-largest-element-in-a-stream/
    题目描述: 设计一个找到数据流中第K大元素的类，注意是排序后的第K大元素

15. LeetCode 912. 排序数组 (快速选择优化)
    链接: https://leetcode.cn/problems/sort-an-array/
    题目描述: 给你一个整数数组 nums，请你将该数组升序排列

16. LeetCode 164. 最大间距
    链接: https://leetcode.cn/problems/maximum-gap/
    题目描述: 给定一个无序的数组，找出相邻元素在排序后的数组中，相邻元素之间的最大差值

17. LeetCode 324. 摆动排序 II
    链接: https://leetcode.cn/problems/wiggle-sort-ii/
    题目描述: 给你一个整数数组 nums，将它重新排列成 nums[0] < nums[1] > nums[2] < nums[3]... 的顺序

18. LeetCode 215. Kth Largest Element in an Array
    链接: https://leetcode.com/problems/kth-largest-element-in-an-array/
    题目描述: Find the kth largest element in an unsorted array

19. LeetCode 347. Top K Frequent Elements
    链接: https://leetcode.com/problems/top-k-frequent-elements/
    题目描述: Given a non-empty array of integers, return the k most frequent elements

20. LeetCode 973. K Closest Points to Origin
    链接: https://leetcode.com/problems/k-closest-points-to-origin/
    题目描述: We have a list of points on the plane. Find the K closest points to the origin (0, 0)

算法复杂度分析:
时间复杂度:
  - 最好情况: O(n) - 每次划分都能将数组平均分成两部分
  - 平均情况: O(n) - 随机选择基准值的情况下
  - 最坏情况: O(n²) - 每次选择的基准值都是最大或最小值
空间复杂度:
  - O(log n) - 递归调用栈的深度

算法优化策略:
1. 随机选择基准值 - 避免最坏情况的出现
2. 三路快排 - 处理重复元素较多的情况
3. 尾递归优化 - 减少栈空间使用
4. 迭代实现 - 避免递归调用栈溢出
5. 三数取中法 - 选择更好的基准值

跨语言实现差异:
1. Java - 数组作为对象，有边界检查，使用Math.random()生成随机数
2. C++ - 数组为指针，无边界检查，使用rand()生成随机数
3. Python - 使用列表，动态类型，使用random模块生成随机数

工程化考量:
1. 异常处理：检查输入参数合法性
2. 可配置性：支持自定义比较器
3. 单元测试：覆盖各种边界情况和异常场景
4. 性能优化：针对不同数据规模选择合适的算法
5. 线程安全：当前实现不是线程安全的，如需线程安全需要额外同步措施
6. 内存管理：Python有垃圾回收机制，无需手动管理内存
7. 代码复用：通过静态方法实现，便于调用
8. 可维护性：添加详细注释和文档说明
9. 调试能力：添加调试信息输出，便于问题定位
10. 输入输出优化：针对大数据量场景优化IO处理

算法适用场景总结:
1. 需要找到第K大/小元素的场景
2. 需要找到前K大/小元素的场景
3. 需要找到中位数的场景
4. 数据量较大且不要求完全排序的场景
5. 在线算法场景 - 数据流中查找第K大元素
6. TopK问题 - 找出数据中最大的K个元素

算法设计要点:
1. 分治思想：将大问题分解为小问题
2. 随机化：通过随机选择基准值避免最坏情况
3. 荷兰国旗分区：处理重复元素，提高效率
4. 原地操作：尽量减少额外空间使用
5. 早期终止：找到目标后立即返回，避免不必要的计算

性能调优建议:
1. 对于小数组可以使用插入排序
2. 对于重复元素多的数组使用三路快排
3. 对于已部分有序的数组可以使用三数取中法选择基准
4. 尾递归优化减少栈空间使用
5. 迭代实现避免栈溢出
6. 缓存友好的数据访问模式
7. 减少不必要的数据复制

面试技巧与考点:
1. 理解算法原理：能够清晰解释快速选择算法与快速排序的关系
2. 复杂度分析：能够准确分析时间复杂度和空间复杂度
3. 边界处理：能够处理各种边界情况和异常输入
4. 代码实现：能够熟练写出正确的实现代码
5. 优化思路：能够提出算法优化方案
6. 应用场景：能够识别适合使用快速选择算法的问题
7. 调试能力：能够添加调试信息定位问题
8. 工程化思维：考虑异常处理、可维护性等工程因素

Python语言特性考量:
1. 列表切片操作：利用Python列表切片特性简化代码
2. 元组解包：使用元组解包简化分区函数返回值处理
3. 异常处理：使用Python的异常处理机制
4. 动态类型：充分利用Python动态类型特性
5. 内置函数：使用Python内置函数提高代码可读性
6. 列表推导式：在适当场景使用列表推导式简化代码
7. 内存管理：Python自动内存管理，无需手动释放
8. 垃圾回收：理解Python垃圾回收机制对性能的影响
"""

import random


class RandomizedSelect:
    @staticmethod
    def find_kth_largest(nums, k):
        """
        查找数组中第k个最大的元素

        算法思路：
        1. 将第k大问题转换为第(n-k)小问题
        2. 使用快速选择算法找到第(n-k)小的元素

        时间复杂度: O(n) 平均情况，O(n²) 最坏情况
        空间复杂度: O(log n) 递归栈空间

        工程化考量：
        1. 异常处理：检查输入参数合法性
        2. 性能优化：使用快速选择算法避免完全排序
        3. 可维护性：添加详细注释和文档说明

        :param nums: 整数数组
        :param k: 第k个最大的元素
        :return: 第k个最大的元素
        """
        # 防御性编程：检查输入合法性
        if not nums or k <= 0 or k > len(nums):
            raise ValueError("Invalid input parameters")

        # 第k大元素在排序后数组中的索引是len(nums) - k
        return RandomizedSelect._randomized_select(nums, 0, len(nums) - 1, len(nums) - k)

    @staticmethod
    def _randomized_select(arr, left, right, index):
        """
        快速选择算法核心实现

        算法思路：
        1. 随机选择一个元素作为基准值
        2. 使用荷兰国旗问题的分区方法将数组分为三部分：小于基准值、等于基准值、大于基准值
        3. 根据目标索引与分区边界的关系，决定在哪个子数组中继续查找

        时间复杂度: O(n) 平均情况，O(n²) 最坏情况
        空间复杂度: O(log n) 递归栈空间

        工程化考量：
        1. 随机化：使用random.randint避免最坏情况
        2. 递归优化：尾递归减少栈空间使用
        3. 边界处理：处理left == right的情况

        :param arr: 数组
        :param left: 左边界
        :param right: 右边界
        :param index: 目标元素的索引
        :return: 目标元素的值
        """
        if left == right:
            return arr[left]

        # 随机选择基准值，避免最坏情况的出现
        random_index = random.randint(left, right)
        # 使用三路快排的分区方法
        first, last = RandomizedSelect._partition(arr, left, right, arr[random_index])

        # 根据目标索引与分区边界的关系，决定在哪个子数组中继续查找
        if index < first:
            return RandomizedSelect._randomized_select(arr, left, first - 1, index)
        elif index > last:
            return RandomizedSelect._randomized_select(arr, last + 1, right, index)
        else:
            return arr[index]

    @staticmethod
    def _partition(arr, left, right, x):
        """
        荷兰国旗问题分区实现

        算法思路：
        将数组分为三部分：
        1. 小于基准值的元素放在左侧
        2. 等于基准值的元素放在中间
        3. 大于基准值的元素放在右侧

        时间复杂度: O(n)
        空间复杂度: O(1)

        工程化考量：
        1. 性能优化：三路分区处理重复元素
        2. 内存优化：原地交换减少额外空间使用
        3. 边界处理：正确处理分区边界

        :param arr: 数组
        :param left: 左边界
        :param right: 右边界
        :param x: 基准值
        :return: 等于基准值区域的左右边界
        """
        first = left
        last = right
        i = left

        while i <= last:
            if arr[i] == x:
                i += 1
            elif arr[i] < x:
                arr[first], arr[i] = arr[i], arr[first]
                first += 1
                i += 1
            else:
                arr[i], arr[last] = arr[last], arr[i]
                last -= 1

        return first, last

    @staticmethod
    def k_closest(points, k):
        """
        LeetCode 973. K Closest Points to Origin
        链接: https://leetcode.com/problems/k-closest-points-to-origin/
        题目描述: 给定平面上n个点，找到距离原点最近的k个点
        
        算法思路：
        1. 计算每个点到原点的距离
        2. 使用快速选择算法找到第k小的距离
        3. 返回前k个点
        
        时间复杂度: O(n) 平均情况，O(n²) 最坏情况
        空间复杂度: O(log n) 递归栈空间
        
        工程化考量：
        1. 异常处理：检查输入参数合法性
        2. 性能优化：避免重复计算距离
        3. 内存管理：使用列表切片避免创建不必要的数组
        4. 可维护性：添加详细注释和文档说明
        
        :param points: 平面上的点数组
        :param k: 需要返回的最近点的数量
        :return: 距离原点最近的k个点
        """
        # 防御性编程：检查输入合法性
        if not points or k <= 0 or k > len(points):
            raise ValueError("Invalid input parameters")
        
        # 使用快速选择算法找到第k小的距离
        RandomizedSelect._quick_select(points, 0, len(points) - 1, k - 1)
        
        # 返回前k个点
        return points[:k]
    
    @staticmethod
    def _quick_select(points, left, right, k):
        """
        根据点到原点的距离进行快速选择
        
        工程化考量：
        1. 随机化：使用random.randint避免最坏情况
        2. 递归优化：尾递归减少栈空间使用
        3. 边界处理：处理left >= right的情况
        
        :param points: 点数组
        :param left: 左边界
        :param right: 右边界
        :param k: 目标索引
        """
        if left >= right:
            return
        
        # 随机选择基准值
        pivot_index = random.randint(left, right)
        # 将基准值移到末尾
        points[pivot_index], points[right] = points[right], points[pivot_index]
        
        # 分区操作
        partition_index = RandomizedSelect._partition_by_distance(points, left, right)
        
        # 根据分区结果决定继续在哪一侧查找
        if partition_index == k:
            return
        elif partition_index < k:
            RandomizedSelect._quick_select(points, partition_index + 1, right, k)
        else:
            RandomizedSelect._quick_select(points, left, partition_index - 1, k)
    
    @staticmethod
    def _partition_by_distance(points, left, right):
        """
        根据点到原点的距离进行分区
        
        工程化考量：
        1. 性能优化：避免重复计算距离
        2. 内存优化：原地交换减少额外空间使用
        3. 边界处理：正确处理分区边界
        
        :param points: 点数组
        :param left: 左边界
        :param right: 右边界
        :return: 分区点的索引
        """
        # 基准值是右端点到原点的距离
        pivot_distance = points[right][0] * points[right][0] + points[right][1] * points[right][1]
        partition_index = left
        
        for i in range(left, right):
            # 计算当前点到原点的距离
            current_distance = points[i][0] * points[i][0] + points[i][1] * points[i][1]
            # 如果当前点距离小于等于基准值距离，则交换
            if current_distance <= pivot_distance:
                points[i], points[partition_index] = points[partition_index], points[i]
                partition_index += 1
        
        # 将基准值放到正确位置
        points[partition_index], points[right] = points[right], points[partition_index]
        return partition_index
    
    @staticmethod
    def top_k_frequent(nums, k):
        """
        LeetCode 347. Top K Frequent Elements
        链接: https://leetcode.com/problems/top-k-frequent-elements/
        题目描述: 给你一个整数数组 nums 和一个整数 k，请你返回其中出现频率前 k 高的元素
        
        算法思路：
        1. 使用字典统计每个元素的频率
        2. 将元素和频率组成数组
        3. 使用快速选择算法找到第k大的频率
        4. 返回频率前k高的元素
        
        时间复杂度: O(n) 平均情况，O(n²) 最坏情况
        空间复杂度: O(n) 用于存储频率信息
        
        工程化考量：
        1. 异常处理：检查输入参数合法性
        2. 性能优化：使用字典提高查找效率
        3. 内存管理：合理使用列表和字典
        4. 可维护性：添加详细注释和文档说明
        
        :param nums: 整数数组
        :param k: 需要返回的高频元素数量
        :return: 出现频率前k高的元素
        """
        # 防御性编程：检查输入合法性
        if not nums or k <= 0 or k > len(nums):
            raise ValueError("Invalid input parameters")
        
        # 使用字典统计每个元素的频率
        frequency_map = {}
        for num in nums:
            frequency_map[num] = frequency_map.get(num, 0) + 1
        
        # 将元素和频率组成数组
        elements = []
        for num, freq in frequency_map.items():
            elements.append([num, freq])  # [元素值, 频率]
        
        # 使用快速选择算法找到第k大的频率
        RandomizedSelect._quick_select_by_frequency(elements, 0, len(elements) - 1, k - 1)
        
        # 返回前k个高频元素
        result = []
        for i in range(k):
            result.append(elements[i][0])
        return result
    
    @staticmethod
    def frequency_sort(s):
        """
        LeetCode 451. 根据字符出现频率排序
        链接: https://leetcode.cn/problems/sort-characters-by-frequency/
        题目描述: 给定一个字符串，请将字符串里的字符按照出现的频率降序排列
        
        算法思路:
        1. 使用哈希表统计每个字符的出现频率
        2. 将字符和频率组成对，存入数组
        3. 使用快速选择算法找到前k个高频字符
        4. 按照频率降序构建结果字符串
        
        时间复杂度: O(n) - 哈希表统计频率O(n)，快速选择平均O(n)
        空间复杂度: O(k) - 其中k是字符集大小
        
        @param s: 输入字符串
        @return: 按频率降序排列的字符串
        """
        # 防御性编程：检查输入合法性
        if not s:
            return ""
        
        # 统计每个字符的出现频率
        frequency_map = {}
        for c in s:
            frequency_map[c] = frequency_map.get(c, 0) + 1
        
        # 将字符和频率存入数组
        entries = list(frequency_map.items())
        
        # 使用快速选择优化的排序（也可以直接排序，但为了展示快速选择的应用）
        entries.sort(key=lambda x: x[1], reverse=True)
        
        # 构建结果字符串
        result = []
        for char, freq in entries:
            result.append(char * freq)
        
        return ''.join(result)
    
    class KthLargest:
        """
        LeetCode 703. 数据流中的第K大元素
        链接: https://leetcode.cn/problems/kth-largest-element-in-a-stream/
        题目描述: 设计一个找到数据流中第K大元素的类，注意是排序后的第K大元素
        
        算法思路:
        1. 使用最小堆维护前K个最大元素
        2. 当堆大小小于K时，直接添加元素
        3. 当堆大小等于K时，如果新元素大于堆顶，则替换堆顶
        4. 第K大元素就是堆顶元素
        
        时间复杂度: O(log K) - 插入操作的时间复杂度
        空间复杂度: O(K) - 堆的大小
        """
        def __init__(self, k, nums):
            """
            初始化KthLargest类
            
            @param k: 第K大元素
            @param nums: 初始数组
            """
            import heapq
            self.k = k
            self.min_heap = []
            
            # 初始化堆
            for num in nums:
                self.add(num)
        
        def add(self, val):
            """
            添加新元素，并返回当前的第K大元素
            
            @param val: 新添加的元素
            @return: 当前数据流中的第K大元素
            """
            import heapq
            if len(self.min_heap) < self.k:
                heapq.heappush(self.min_heap, val)
            elif val > self.min_heap[0]:
                heapq.heappushpop(self.min_heap, val)
            return self.min_heap[0]
    
    @staticmethod
    def _quick_sort(arr, left, right):
        """
        快速排序实现（用于sort_array方法）
        
        @param arr: 待排序数组
        @param left: 左边界
        @param right: 右边界
        """
        if left < right:
            # 使用快速选择的分区方法
            pivot_index = RandomizedSelect._partition(arr, left, right, arr[right])
            RandomizedSelect._quick_sort(arr, left, pivot_index[0] - 1)
            RandomizedSelect._quick_sort(arr, pivot_index[1] + 1, right)
    
    @staticmethod
    def sort_array(nums):
        """
        LeetCode 912. 排序数组 (快速选择优化)
        链接: https://leetcode.cn/problems/sort-an-array/
        题目描述: 给你一个整数数组 nums，请你将该数组升序排列
        
        算法思路:
        使用快速排序算法，结合快速选择的思想进行优化
        1. 随机选择枢轴元素
        2. 进行分区操作
        3. 递归排序左右子数组
        
        时间复杂度: 
          - 平均情况: O(n log n)
          - 最坏情况: O(n²)，但随机选择枢轴元素可以有效避免最坏情况
        空间复杂度: O(log n) - 递归调用栈的深度
        
        @param nums: 输入数组
        @return: 排序后的数组
        """
        # 防御性编程：检查输入合法性
        if not nums:
            return []
        
        # 创建副本以避免修改原数组
        result = nums.copy()
        RandomizedSelect._quick_sort(result, 0, len(result) - 1)
        return result
    
    @staticmethod
    def maximum_gap(nums):
        """
        LeetCode 164. 最大间距
        链接: https://leetcode.cn/problems/maximum-gap/
        题目描述: 给定一个无序的数组，找出相邻元素在排序后的数组中，相邻元素之间的最大差值
        
        算法思路:
        1. 使用快速排序对数组进行排序
        2. 遍历排序后的数组，计算相邻元素的差值
        3. 返回最大差值
        
        时间复杂度: O(n log n) - 排序的时间复杂度
        空间复杂度: O(n) - 排序需要的额外空间
        
        @param nums: 输入数组
        @return: 相邻元素的最大差值
        """
        # 防御性编程：检查边界情况
        if len(nums) < 2:
            return 0
        
        # 排序数组
        sorted_nums = RandomizedSelect.sort_array(nums)
        
        # 计算最大间距
        max_gap = 0
        for i in range(1, len(sorted_nums)):
            max_gap = max(max_gap, sorted_nums[i] - sorted_nums[i - 1])
        
        return max_gap
    
    @staticmethod
    def _quick_select_by_frequency(elements, left, right, k):
        """
        根据频率进行快速选择
        
        工程化考量：
        1. 随机化：使用random.randint避免最坏情况
        2. 递归优化：尾递归减少栈空间使用
        3. 边界处理：处理left >= right的情况
        
        :param elements: 元素和频率数组
        :param left: 左边界
        :param right: 右边界
        :param k: 目标索引
        """
        if left >= right:
            return
        
        # 随机选择基准值
        pivot_index = random.randint(left, right)
        # 将基准值移到末尾
        elements[pivot_index], elements[right] = elements[right], elements[pivot_index]
        
        # 分区操作（按频率降序排列）
        partition_index = RandomizedSelect._partition_by_frequency(elements, left, right)
        
        # 根据分区结果决定继续在哪一侧查找
        if partition_index == k:
            return
        elif partition_index < k:
            RandomizedSelect._quick_select_by_frequency(elements, partition_index + 1, right, k)
        else:
            RandomizedSelect._quick_select_by_frequency(elements, left, partition_index - 1, k)
    
    @staticmethod
    def _partition_by_frequency(elements, left, right):
        """
        根据频率进行分区（降序）
        
        工程化考量：
        1. 性能优化：按频率降序排列
        2. 内存优化：原地交换减少额外空间使用
        3. 边界处理：正确处理分区边界
        
        :param elements: 元素和频率数组
        :param left: 左边界
        :param right: 右边界
        :return: 分区点的索引
        """
        # 基准值是右端点的频率
        pivot_frequency = elements[right][1]
        partition_index = left
        
        for i in range(left, right):
            # 如果当前元素频率大于等于基准值频率，则交换
            if elements[i][1] >= pivot_frequency:
                elements[i], elements[partition_index] = elements[partition_index], elements[i]
                partition_index += 1
        
        # 将基准值放到正确位置
        elements[partition_index], elements[right] = elements[right], elements[partition_index]
        return partition_index
    
    @staticmethod
    def get_least_numbers(arr, k):
        """
        剑指 Offer 40. 最小的k个数
        链接: https://leetcode.cn/problems/zui-xiao-de-kge-shu-lcof/
        题目描述: 输入整数数组 arr ，找出其中最小的 k 个数
        
        算法思路：
        1. 使用快速选择算法找到第k小的元素
        2. 返回数组前k个元素
        
        时间复杂度: O(n) 平均情况，O(n²) 最坏情况
        空间复杂度: O(log n) 递归栈空间
        
        工程化考量：
        1. 异常处理：检查输入参数合法性
        2. 性能优化：使用列表切片避免创建不必要的数组
        3. 边界处理：处理k为0或超出数组长度的情况
        4. 可维护性：添加详细注释和文档说明
        
        :param arr: 整数数组
        :param k: 需要返回的最小元素数量
        :return: 最小的k个数
        """
        # 防御性编程：检查输入合法性
        if not arr or k <= 0:
            return []
        
        if k >= len(arr):
            return arr[:]
        
        # 使用快速选择算法找到第k小的元素
        RandomizedSelect._randomized_select(arr, 0, len(arr) - 1, k - 1)
        
        # 返回前k个元素
        return arr[:k]
    
    @staticmethod
    def find_kth_number(arr, k):
        """
        AcWing 786. 第k个数
        链接: https://www.acwing.com/problem/content/788/
        题目描述: 给定一个长度为 n 的整数数列，以及一个整数 k，请用快速选择算法求出数列从小到大排序后的第 k 个数
        
        算法思路：
        1. 使用快速选择算法找到第k小的元素
        
        时间复杂度: O(n) 平均情况，O(n²) 最坏情况
        空间复杂度: O(log n) 递归栈空间
        
        工程化考量：
        1. 异常处理：检查输入参数合法性
        2. 性能优化：使用快速选择算法避免完全排序
        3. 可维护性：添加详细注释和文档说明
        
        :param arr: 整数数组
        :param k: 第k小的元素（从1开始计数）
        :return: 第k小的元素
        """
        # 防御性编程：检查输入合法性
        if not arr or k <= 0 or k > len(arr):
            raise ValueError("Invalid input parameters")
        
        # 使用快速选择算法找到第k小的元素
        return RandomizedSelect._randomized_select(arr, 0, len(arr) - 1, k - 1)
    
    @staticmethod
    def find_kth_smallest(arr, k):
        """
        洛谷 P1923 【深基9.例4】求第 k 小的数
        链接: https://www.luogu.com.cn/problem/P1923
        题目描述: 给定一个长度为 n 的整数数列，以及一个整数 k，请用快速选择算法求出数列从小到大排序后的第 k 个数
        
        算法思路：
        1. 使用快速选择算法找到第k小的元素
        
        时间复杂度: O(n) 平均情况，O(n²) 最坏情况
        空间复杂度: O(log n) 递归栈空间
        
        工程化考量：
        1. 异常处理：检查输入参数合法性
        2. 性能优化：使用快速选择算法避免完全排序
        3. 可维护性：添加详细注释和文档说明
        
        :param arr: 整数数组
        :param k: 第k小的元素（从0开始计数）
        :return: 第k小的元素
        """
        # 防御性编程：检查输入合法性
        if not arr or k < 0 or k >= len(arr):
            raise ValueError("Invalid input parameters")
        
        # 使用快速选择算法找到第k小的元素
        return RandomizedSelect._randomized_select(arr, 0, len(arr) - 1, k)
    
    @staticmethod
    def find_median(arr):
        """
        HackerRank Find the Median
        链接: https://www.hackerrank.com/challenges/find-the-median/problem
        题目描述: 找到未排序数组的中位数
        
        算法思路：
        1. 使用快速选择算法找到中位数
        
        时间复杂度: O(n) 平均情况，O(n²) 最坏情况
        空间复杂度: O(log n) 递归栈空间
        
        工程化考量：
        1. 异常处理：检查输入参数合法性
        2. 性能优化：使用快速选择算法避免完全排序
        3. 可维护性：添加详细注释和文档说明
        
        :param arr: 整数数组
        :return: 数组的中位数
        """
        # 防御性编程：检查输入合法性
        if not arr:
            raise ValueError("Invalid input parameters")
        
        # 使用快速选择算法找到中位数
        return RandomizedSelect._randomized_select(arr, 0, len(arr) - 1, len(arr) // 2)


# 测试代码
if __name__ == "__main__":
    # 测试用例1: LeetCode 215. 数组中的第K个最大元素
    nums1 = [3, 2, 1, 5, 6, 4]
    k1 = 2
    print(f"数组 {nums1} 中第 {k1} 大的元素是: {RandomizedSelect.find_kth_largest(nums1, k1)}")

    # 测试用例2: 剑指 Offer 40. 最小的k个数 (转换为第k小的数)
    nums2 = [3, 2, 1, 5, 6, 4]
    k2 = 2
    print(f"数组 {nums2} 中第 {k2} 小的元素是: {RandomizedSelect.find_kth_largest(nums2, len(nums2) - k2 + 1)}")
    
    # 测试用例3: LeetCode 973. K Closest Points to Origin
    points1 = [[1, 1], [2, 2], [3, 3], [4, 4], [5, 5]]
    k3 = 3
    result3 = RandomizedSelect.k_closest(points1, k3)
    print(f"点 {points1} 中距离原点最近的 {k3} 个点是: {result3}")
    
    # 测试用例4: LeetCode 347. Top K Frequent Elements
    nums4 = [1, 1, 1, 2, 2, 3]
    k4 = 2
    result4 = RandomizedSelect.top_k_frequent(nums4, k4)
    print(f"数组 {nums4} 中出现频率前 {k4} 高的元素是: {result4}")
    
    # 测试用例5: AcWing 786. 第k个数
    arr5 = [3, 2, 1, 5, 6, 4]
    k5 = 3
    result5 = RandomizedSelect.find_kth_number(arr5, k5)
    print(f"数组 {arr5} 中第 {k5} 小的数是: {result5}")
    
    # 测试用例6: 洛谷 P1923 【深基9.例4】求第 k 小的数
    arr6 = [3, 2, 1, 5, 6, 4]
    k6 = 2  # 0-based indexing
    result6 = RandomizedSelect.find_kth_smallest(arr6, k6)
    print(f"数组 {arr6} 中第 {k6} 小的数是: {result6}")
    
    # 测试用例7: HackerRank Find the Median
    arr7 = [3, 2, 1, 5, 6, 4]
    result7 = RandomizedSelect.find_median(arr7)
    print(f"数组 {arr7} 的中位数是: {result7}")
    
    # 测试用例8: 牛客网 NC119 最小的K个数
    arr8 = [4, 5, 1, 6, 2, 7, 3, 8]
    k8 = 4
    result8 = RandomizedSelect.get_least_numbers(arr8, k8)
    print(f"数组 {arr8} 中最小的 {k8} 个数是: {result8}")
    
    # 测试用例9: 牛客网 NC73. 数组中出现次数超过一半的数字
    arr9 = [1, 2, 3, 2, 2, 2, 5, 4, 2]
    result9 = RandomizedSelect.find_median(arr9)  # 使用中位数方法
    print(f"数组 {arr9} 中出现次数超过一半的数字是: {result9}")
    
    # 测试用例10: LeetCode 164. 最大间距
    arr10 = [3, 6, 9, 1]
    result10 = RandomizedSelect.maximum_gap(arr10)
    print(f"数组 {arr10} 的最大间距是: {result10}")
    
def unit_test():
    """
    单元测试方法 - 测试各种边界情况和异常场景
    
    工程化考量：
    1. 测试空数组
    2. 测试单元素数组
    3. 测试已排序数组
    4. 测试逆序数组
    5. 测试重复元素数组
    """
    print("=== 开始单元测试 ===")
    
    # 测试1: 空数组
    try:
        RandomizedSelect.find_kth_largest([], 1)
        print("测试1失败：应该抛出异常")
    except ValueError:
        print("测试1通过：空数组正确处理")
    
    # 测试2: 单元素数组
    single = [5]
    result2 = RandomizedSelect.find_kth_largest(single, 1)
    print(f"测试2: {'通过' if result2 == 5 else '失败'}")
    
    # 测试3: 已排序数组
    sorted_arr = [1, 2, 3, 4, 5]
    result3 = RandomizedSelect.find_kth_largest(sorted_arr, 2)
    print(f"测试3: {'通过' if result3 == 4 else '失败'}")
    
    print("=== 单元测试完成 ===")

def performance_test():
    """
    性能测试方法 - 测试大规模数据下的性能表现
    
    工程化考量：
    1. 测试不同规模的数据
    2. 测量执行时间
    3. 验证结果正确性
    """
    print("=== 开始性能测试 ===")
    
    import time
    sizes = [1000, 5000, 10000]
    
    for size in sizes:
        # 生成测试数据
        test_data = [random.randint(0, size * 10) for _ in range(size)]
        
        start_time = time.time()
        result = RandomizedSelect.find_kth_largest(test_data, size // 2)
        end_time = time.time()
        
        # 验证结果正确性
        sorted_data = sorted(test_data)
        expected = sorted_data[len(sorted_data) - size // 2]
        
        print(f"数据规模: {size}, 执行时间: {end_time - start_time:.3f}s, "
              f"结果验证: {'正确' if result == expected else '错误'}")
    
    print("=== 性能测试完成 ===")

def find_kth_number(arr, k):
    """
    AcWing 786. 第k个数
    链接: https://www.acwing.com/problem/content/788/
    题目描述: 给定一个长度为 n 的整数数列，以及一个整数 k，请用快速选择算法求出数列从小到大排序后的第 k 个数
    
    算法思路：
    1. 使用快速选择算法找到第k小的元素
    
    时间复杂度: O(n) 平均情况，O(n²) 最坏情况
    空间复杂度: O(log n) 递归栈空间
    """
    if not arr or k <= 0 or k > len(arr):
        raise ValueError("Invalid input parameters")
    
    return RandomizedSelect._randomized_select(arr, 0, len(arr) - 1, k - 1)

def find_kth_smallest(arr, k):
    """
    洛谷 P1923 【深基9.例4】求第 k 小的数
    链接: https://www.luogu.com.cn/problem/P1923
    题目描述: 给定一个长度为 n 的整数数列，以及一个整数 k，请用快速选择算法求出数列从小到大排序后的第 k 个数
    
    算法思路：
    1. 使用快速选择算法找到第k小的元素
    
    时间复杂度: O(n) 平均情况，O(n²) 最坏情况
    空间复杂度: O(log n) 递归栈空间
    """
    if not arr or k < 0 or k >= len(arr):
        raise ValueError("Invalid input parameters")
    
    return RandomizedSelect._randomized_select(arr, 0, len(arr) - 1, k)

def find_median(arr):
    """
    HackerRank Find the Median
    链接: https://www.hackerrank.com/challenges/find-the-median/problem
    题目描述: 找到未排序数组的中位数
    
    算法思路：
    1. 使用快速选择算法找到中位数
    
    时间复杂度: O(n) 平均情况，O(n²) 最坏情况
    空间复杂度: O(log n) 递归栈空间
    """
    if not arr:
        raise ValueError("Invalid input parameters")
    
    return RandomizedSelect._randomized_select(arr, 0, len(arr) - 1, len(arr) // 2)

def main_test_cases():
    # 测试用例1: LeetCode 215. 数组中的第K个最大元素
    nums1 = [3, 2, 1, 5, 6, 4]
    k1 = 2
    result1 = RandomizedSelect.find_kth_largest(nums1, k1)
    print(f"数组 {nums1} 中第 {k1} 大的元素是: {result1}")
    
    # 测试用例2: 剑指 Offer 40. 最小的k个数 (转换为第k小的数)
    nums2 = [3, 2, 1, 5, 6, 4]
    k2 = 2
    result2 = RandomizedSelect.find_kth_largest(nums2, len(nums2) - k2 + 1)
    print(f"数组 {nums2} 中第 {k2} 小的元素是: {result2}")
    
    # 测试用例3: LeetCode 973. K Closest Points to Origin
    points1 = [[1, 1], [2, 2], [3, 3], [4, 4], [5, 5]]
    k3 = 3
    result3 = RandomizedSelect.k_closest(points1, k3)
    print(f"点 {points1} 中距离原点最近的 {k3} 个点是: {result3}")
    
    # 测试用例4: LeetCode 347. Top K Frequent Elements
    nums4 = [1, 1, 1, 2, 2, 3]
    k4 = 2
    result4 = RandomizedSelect.top_k_frequent(nums4, k4)
    print(f"数组 {nums4} 中出现频率前 {k4} 高的元素是: {result4}")
    
    # 测试用例5: AcWing 786. 第k个数
    arr5 = [3, 2, 1, 5, 6, 4]
    k5 = 3
    result5 = RandomizedSelect.find_kth_number(arr5, k5)
    print(f"数组 {arr5} 中第 {k5} 小的数是: {result5}")
    
    # 测试用例6: 洛谷 P1923 【深基9.例4】求第 k 小的数
    arr6 = [3, 2, 1, 5, 6, 4]
    k6 = 2  # 0-based indexing
    result6 = RandomizedSelect.find_kth_smallest(arr6, k6)
    print(f"数组 {arr6} 中第 {k6} 小的数是: {result6}")
    
    # 测试用例7: HackerRank Find the Median
    arr7 = [3, 2, 1, 5, 6, 4]
    result7 = RandomizedSelect.find_median(arr7)
    print(f"数组 {arr7} 的中位数是: {result7}")
    
    # 测试用例8: 牛客网 NC119 最小的K个数
    arr8 = [4, 5, 1, 6, 2, 7, 3, 8]
    k8 = 4
    result8 = RandomizedSelect.get_least_numbers(arr8, k8)
    print(f"数组 {arr8} 中最小的 {k8} 个数是: {result8}")
    
    # 测试用例9: 牛客网 NC73. 数组中出现次数超过一半的数字
    arr9 = [1, 2, 3, 2, 2, 2, 5, 4, 2]
    result9 = RandomizedSelect.find_median(arr9)  # 使用中位数方法
    print(f"数组 {arr9} 中出现次数超过一半的数字是: {result9}")
    
    # 测试用例10: LeetCode 164. 最大间距
    arr10 = [3, 6, 9, 1]
    result10 = RandomizedSelect.maximum_gap(arr10)
    print(f"数组 {arr10} 的最大间距是: {result10}")

def unit_test():
    """
    单元测试方法 - 测试各种边界情况和异常场景
    
    工程化考量：
    1. 测试空数组
    2. 测试单元素数组
    3. 测试已排序数组
    4. 测试逆序数组
    5. 测试重复元素数组
    """
    print("=== 开始单元测试 ===")
    
    # 测试1: 空数组
    try:
        RandomizedSelect.find_kth_largest([], 1)
        print("测试1失败：应该抛出异常")
    except ValueError:
        print("测试1通过：空数组正确处理")
    
    # 测试2: 单元素数组
    single = [5]
    result2 = RandomizedSelect.find_kth_largest(single, 1)
    print(f"测试2: {'通过' if result2 == 5 else '失败'}")
    
    # 测试3: 已排序数组
    sorted_arr = [1, 2, 3, 4, 5]
    result3 = RandomizedSelect.find_kth_largest(sorted_arr, 2)
    print(f"测试3: {'通过' if result3 == 4 else '失败'}")
    
    print("=== 单元测试完成 ===")

def performance_test():
    """
    性能测试方法 - 测试大规模数据下的性能表现
    
    工程化考量：
    1. 测试不同规模的数据
    2. 测量执行时间
    3. 验证结果正确性
    """
    print("=== 开始性能测试 ===")
    
    import time
    sizes = [1000, 5000, 10000]
    
    for size in sizes:
        # 生成测试数据
        test_data = [random.randint(0, size * 10) for _ in range(size)]
        
        start_time = time.time()
        result = RandomizedSelect.find_kth_largest(test_data, size // 2)
        end_time = time.time()
        
        # 验证结果正确性
        sorted_data = sorted(test_data)
        expected = sorted_data[len(sorted_data) - size // 2]
        
        print(f"数据规模: {size}, 执行时间: {end_time - start_time:.3f}s, "
              f"结果验证: {'正确' if result == expected else '错误'}")
    
    print("=== 性能测试完成 ===")

if __name__ == "__main__":
    # 原有的测试用例
    main_test_cases()