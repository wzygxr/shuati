# Python版本快速排序实现
# 包含多种快速排序的实现方式

'''
补充题目列表:

1. LeetCode 912. 排序数组
   链接: https://leetcode.cn/problems/sort-an-array/
   题目描述: 给你一个整数数组 nums，请你将该数组升序排列。
   时间复杂度: O(n log n)，空间复杂度: O(log n)
   最优解: 快速排序或归并排序

2. 洛谷 P1177 【模板】快速排序
   链接: https://www.luogu.com.cn/problem/P1177
   题目描述: 利用快速排序算法将读入的N个数从小到大排序后输出。
   时间复杂度: O(n log n)，空间复杂度: O(log n)
   最优解: 快速排序算法实现

3. LeetCode 215. 数组中的第K个最大元素
   链接: https://leetcode.cn/problems/kth-largest-element-in-an-array/
   题目描述: 给定整数数组 nums 和整数 k，请返回数组中第 k 个最大的元素。
   时间复杂度: O(n) 平均，空间复杂度: O(log n)
   最优解: 快速选择算法

4. LeetCode 75. 颜色分类
   链接: https://leetcode.cn/problems/sort-colors/
   题目描述: 给定一个包含红色、白色和蓝色、共 n 个元素的数组 nums，原地对它们进行排序，使得相同颜色的元素相邻，并按照红色、白色、蓝色顺序排列。
   时间复杂度: O(n)，空间复杂度: O(1)
   最优解: 三路快排思想

5. LeetCode 283. 移动零
   链接: https://leetcode.cn/problems/move-zeroes/
   题目描述: 给定一个数组 nums，编写一个函数将所有 0 移动到数组的末尾，同时保持非零元素的相对顺序。
   时间复杂度: O(n)，空间复杂度: O(1)
   最优解: 双指针法

6. Codeforces 401C. Team
   链接: https://codeforces.com/problemset/problem/401/C
   题目描述: 构造一个01序列，满足特定的约束条件。
   时间复杂度: O(n+m)，空间复杂度: O(n+m)
   最优解: 贪心构造

7. AtCoder ABC121C. Energy Drink Collector
   链接: https://atcoder.jp/contests/abc121/tasks/abc121_c
   题目描述: 购买能量饮料以获得最少的总花费。
   时间复杂度: O(n log n)，空间复杂度: O(1)
   最优解: 贪心+排序

8. 牛客网 - 快速排序
   链接: https://www.nowcoder.com/practice/e016ad9b7f0b45048c58a9f27ba618bf
   题目描述: 实现快速排序算法
   时间复杂度: O(n log n)，空间复杂度: O(log n)
   最优解: 标准快速排序实现

9. PAT 1101 Quick Sort
   链接: https://pintia.cn/problem-sets/994805342720868352/problems/994805366343188480
   题目描述: 快速排序中的主元(pivot)是左面都比它小、右边都比它大的位置对应的数字。找出所有满足条件的主元。
   时间复杂度: O(n)，空间复杂度: O(n)
   最优解: 预处理左右边界最大值数组

10. 剑指 Offer 40. 最小的k个数
    链接: https://leetcode.cn/problems/zui-xiao-de-kge-shu-lcof/
    题目描述: 输入整数数组 arr ，找出其中最小的 k 个数。
    时间复杂度: O(n) 平均，空间复杂度: O(log n)
    最优解: 快速选择算法

11. 杭电 OJ 1425. sort
    链接: http://acm.hdu.edu.cn/showproblem.php?pid=1425
    题目描述: 对整数数组进行快速排序
    时间复杂度: O(n log n)，空间复杂度: O(log n)
    最优解: 快速排序或堆排序

12. POJ 2388. Who's in the Middle
    链接: http://poj.org/problem?id=2388
    题目描述: 找出一组数的中位数，快速选择的经典应用
    时间复杂度: O(n) 平均，空间复杂度: O(log n)
    最优解: 快速选择算法找中位数

13. AizuOJ ALDS1_6_C. Quick Sort
    链接: https://onlinejudge.u-aizu.ac.jp/problems/ALDS1_6_C
    题目描述: 实现快速排序算法并输出每一步的分区结果
    时间复杂度: O(n log n)，空间复杂度: O(log n)
    最优解: 快速排序算法实现

14. LeetCode 169. 多数元素
    链接: https://leetcode.cn/problems/majority-element/
    题目描述: 给定一个大小为 n 的数组，找到其中的多数元素
    时间复杂度: O(n)，空间复杂度: O(1)
    最优解: Boyer-Moore投票算法（与快速选择思想相关）

15. LeetCode 274. H 指数
    链接: https://leetcode.cn/problems/h-index/
    题目描述: 计算研究人员的 h 指数
    时间复杂度: O(n) 平均，空间复杂度: O(n)
    最优解: 计数排序或快速选择

16. LeetCode 462. 最少移动次数使数组元素相等II
    链接: https://leetcode.cn/problems/minimum-moves-to-equal-array-elements-ii/
    题目描述: 找到使所有数组元素相等所需的最少移动次数
    时间复杂度: O(n) 平均，空间复杂度: O(log n)
    最优解: 快速选择找中位数

17. LeetCode 324. 摆动排序II
    链接: https://leetcode.cn/problems/wiggle-sort-ii/
    题目描述: 将数组重新排列成 nums[0] < nums[1] > nums[2] < nums[3]... 的顺序
    时间复杂度: O(n) 平均，空间复杂度: O(n)
    最优解: 快速选择+三路快排

18. LeetCode 347. 前K个高频元素
    链接: https://leetcode.cn/problems/top-k-frequent-elements/
    题目描述: 返回数组中出现频率前 k 高的元素
    时间复杂度: O(n) 平均，空间复杂度: O(n)
    最优解: 快速选择或堆排序

19. LeetCode 973. 最接近原点的K个点
    链接: https://leetcode.cn/problems/k-closest-points-to-origin/
    题目描述: 找到最接近原点的 k 个点
    时间复杂度: O(n) 平均，空间复杂度: O(log n)
    最优解: 快速选择算法

20. LeetCode 4. 寻找两个正序数组的中位数
    链接: https://leetcode.cn/problems/median-of-two-sorted-arrays/
    题目描述: 找到两个有序数组的中位数
    时间复杂度: O(log(min(m,n)))，空间复杂度: O(1)
    最优解: 二分查找（与快速选择思想相关）

算法复杂度分析:
时间复杂度:
  - 最好情况: O(n log n) - 每次划分都能将数组平均分成两部分
  - 平均情况: O(n log n) - 随机选择基准值的情况下
  - 最坏情况: O(n^2) - 每次选择的基准值都是最大或最小值
空间复杂度:
  - O(log n) - 递归调用栈的深度

与Java/C++版本的差异:
1. Python使用列表，动态类型，无需声明数组大小
2. Python使用random模块生成随机数
3. Python列表是对象，有动态扩容功能
4. Python语法简洁，但性能相对较低
5. Python有列表推导式等特性，代码更简洁

工程化考量:
1. 异常处理: 处理空列表、None输入等边界情况
2. 性能优化: 对于小数组使用插入排序优化
3. 内存使用: 原地排序减少额外空间开销
4. 稳定性: 标准快排不稳定，如需稳定排序需特殊处理

调试技巧:
1. 使用print打印中间过程
2. 添加断言验证分区正确性
3. 测试边界情况（空列表、单元素等）
4. 使用Python的unittest模块进行单元测试

面试技巧:
1. 理解快排与其它排序算法的比较（如归并排序、堆排序）
2. 掌握快排的优化方法（随机化、三路快排等）
3. 理解快排在不同数据分布下的性能表现
4. 能够分析快排的稳定性和适用场景
'''

import random

class QuickSortSolution:
    def __init__(self):
        pass
    
    # 方法1: 基础快速排序
    def quick_sort1(self, arr, l, r):
        """
        基础快速排序实现
        使用双指针分区法实现快速排序
        :param arr: 待排序列表
        :param l: 排序区间的左边界（包含）
        :param r: 排序区间的右边界（包含）
        """
        # 递归终止条件：当左边界大于等于右边界时，表示区间内没有元素或只有一个元素，无需排序
        if l >= r:
            return
        
        # 随机选择基准值，避免最坏情况
        # random.randint(l, r)生成[l, r]之间的随机整数
        i = random.randint(l, r)
        
        # 将随机选择的基准值交换到第一个位置，便于后续分区操作
        arr[l], arr[i] = arr[i], arr[l]
        
        # 基准值
        pivot = arr[l]
        
        # 双指针分区：left从左向右扫描，right从右向左扫描
        left, right = l, r
        
        # 双指针分区操作
        while left < right:
            # 从右向左找小于基准值的元素
            while left < right and arr[right] >= pivot:
                right -= 1
            
            # 从左向右找大于基准值的元素
            while left < right and arr[left] <= pivot:
                left += 1
            
            # 如果left < right，说明找到了需要交换的元素对
            if left < right:
                # 交换元素
                arr[left], arr[right] = arr[right], arr[left]
        
        # 将基准值放到正确位置（left == right时的位置）
        arr[l], arr[left] = arr[left], arr[l]
        
        # 递归排序左右两部分
        self.quick_sort1(arr, l, left - 1)
        self.quick_sort1(arr, left + 1, r)
    
    # 方法2: 三路快速排序（处理重复元素）
    def quick_sort2(self, arr, l, r):
        """
        三路快速排序实现（处理重复元素）
        将数组划分为三部分：< pivot、= pivot、> pivot
        特别适合处理有大量重复元素的数组
        :param arr: 待排序列表
        :param l: 排序区间的左边界（包含）
        :param r: 排序区间的右边界（包含）
        """
        # 递归终止条件
        if l >= r:
            return
        
        # 随机选择基准值
        i = random.randint(l, r)
        arr[l], arr[i] = arr[i], arr[l]
        
        # 基准值
        pivot = arr[l]
        
        # 三路分区指针：
        # lt: 小于区域的右边界
        # gt: 大于区域的左边界
        # i_idx: 当前处理元素的索引
        lt = l       # arr[l+1...lt] < pivot
        gt = r + 1   # arr[gt...r] > pivot
        i_idx = l + 1  # arr[lt+1...i-1] == pivot
        
        # 三路分区过程
        while i_idx < gt:
            if arr[i_idx] < pivot:
                # 当前元素小于基准值，将其交换到小于区域
                lt += 1
                arr[lt], arr[i_idx] = arr[i_idx], arr[lt]
                i_idx += 1
            elif arr[i_idx] > pivot:
                # 当前元素大于基准值，将其交换到大于区域
                gt -= 1
                arr[gt], arr[i_idx] = arr[i_idx], arr[gt]
                # 注意这里i_idx不自增，因为交换过来的元素还未处理
            else:
                # 当前元素等于基准值，保持在等于区域
                i_idx += 1
        
        # 将基准值放到等于区域的左边界
        arr[l], arr[lt] = arr[lt], arr[l]
        
        # 递归排序小于区域和大于区域
        self.quick_sort2(arr, l, lt - 1)
        self.quick_sort2(arr, gt, r)
    
    # 方法3: 快速选择算法（用于查找第k小元素）
    def quick_select(self, arr, l, r, k):
        """
        快速选择算法（用于查找第k小元素）
        与快速排序的区别：只处理包含目标元素的子数组
        平均时间复杂度：O(n)
        :param arr: 数组
        :param l: 当前处理区间的左边界（包含）
        :param r: 当前处理区间的右边界（包含）
        :param k: 目标元素在排序后数组中的索引位置
        :return: 第k小的元素值
        """
        # 递归终止条件：当区间只有一个元素时，就是要找的位置
        if l >= r:
            return arr[l]
        
        # 随机选择基准值
        i = random.randint(l, r)
        
        # 将基准值交换到末尾位置，便于后续分区操作
        arr[i], arr[r] = arr[r], arr[i]
        
        # 基准值
        pivot = arr[r]
        
        # 小于等于基准值区域的右边界（不包含）
        left = l
        
        # 遍历数组，将小于等于基准值的元素放到左侧
        for j in range(l, r):
            if arr[j] <= pivot:
                arr[left], arr[j] = arr[j], arr[left]
                left += 1
        
        # 将基准值放到正确位置
        arr[left], arr[r] = arr[r], arr[left]
        
        # 根据基准值位置决定下一步操作
        if left == k:
            # 如果基准值位置正好是目标位置，直接返回
            return arr[left]
        elif left < k:
            # 如果基准值位置小于目标位置，在右半部分继续查找
            return self.quick_select(arr, left + 1, r, k)
        else:
            # 如果基准值位置大于目标位置，在左半部分继续查找
            return self.quick_select(arr, l, left - 1, k)
    
    # 方法4: 堆排序实现（用于比较）
    def heap_sort(self, arr):
        """
        堆排序实现（用于比较）
        :param arr: 待排序列表
        """
        def heapify(arr, n, i):
            """
            调整堆结构
            :param arr: 数组
            :param n: 堆大小
            :param i: 当前节点索引
            """
            largest = i
            left = 2 * i + 1
            right = 2 * i + 2
            
            # 找到最大值的索引
            if left < n and arr[left] > arr[largest]:
                largest = left
            
            if right < n and arr[right] > arr[largest]:
                largest = right
            
            # 如果最大值不是当前节点，则交换并继续调整
            if largest != i:
                arr[i], arr[largest] = arr[largest], arr[i]
                heapify(arr, n, largest)
        
        n = len(arr)
        
        # 构建最大堆
        for i in range(n // 2 - 1, -1, -1):
            heapify(arr, n, i)
        
        # 逐个从堆顶取出元素
        for i in range(n - 1, 0, -1):
            arr[0], arr[i] = arr[i], arr[0]
            heapify(arr, i, 0)
    
    # 【优化版本1】小数组插入排序优化
    # 当数组长度小于阈值时，使用插入排序，减少递归开销
    def quick_sort_optimized(self, arr, l, r):
        """
        优化版本的快速排序（小数组使用插入排序）
        当数组长度小于阈值时，使用插入排序，减少递归开销
        :param arr: 待排序列表
        :param l: 排序区间的左边界（包含）
        :param r: 排序区间的右边界（包含）
        """
        # 小数组阈值，经验值为10-20
        INSERTION_SORT_THRESHOLD = 15
        
        # 对小数组使用插入排序
        if r - l <= INSERTION_SORT_THRESHOLD:
            self.insertion_sort(arr, l, r)
            return
        
        # 对大数组继续使用快速排序
        i = random.randint(l, r)
        arr[l], arr[i] = arr[i], arr[l]
        
        pivot = arr[l]
        lt = l       # arr[l+1...lt] < pivot
        gt = r + 1   # arr[gt...r] > pivot
        i_idx = l + 1  # arr[lt+1...i-1] == pivot
        
        while i_idx < gt:
            if arr[i_idx] < pivot:
                lt += 1
                arr[lt], arr[i_idx] = arr[i_idx], arr[lt]
                i_idx += 1
            elif arr[i_idx] > pivot:
                gt -= 1
                arr[gt], arr[i_idx] = arr[i_idx], arr[gt]
            else:
                i_idx += 1
        
        arr[l], arr[lt] = arr[lt], arr[l]
        
        self.quick_sort_optimized(arr, l, lt - 1)
        self.quick_sort_optimized(arr, gt, r)
    
    # 插入排序算法，用于小数组优化
    def insertion_sort(self, arr, l, r):
        """
        插入排序算法，用于小数组优化
        对小规模数组使用插入排序比快速排序更高效
        :param arr: 数组
        :param l: 排序区间的左边界（包含）
        :param r: 排序区间的右边界（包含）
        """
        # 从第二个元素开始，逐个插入到已排序序列中
        for i in range(l + 1, r + 1):
            key = arr[i]  # 当前要插入的元素
            j = i - 1     # 已排序序列的最后一个位置
            
            # 在已排序序列中找到合适的插入位置
            while j >= l and arr[j] > key:
                arr[j + 1] = arr[j]  # 元素后移
                j -= 1
            
            # 插入元素
            arr[j + 1] = key
    
    # 【优化版本2】尾递归优化
    # 将尾递归转换为迭代，减少栈空间使用
    def quick_sort_tail_recursive(self, arr, l, r):
        """
        尾递归优化版本的快速排序
        将尾递归转换为迭代，减少栈空间使用
        :param arr: 待排序列表
        :param l: 排序区间的左边界（包含）
        :param r: 排序区间的右边界（包含）
        """
        while l < r:
            # 随机选择基准值
            i = random.randint(l, r)
            arr[l], arr[i] = arr[i], arr[l]
            
            pivot = arr[l]
            lt = l       # arr[l+1...lt] < pivot
            gt = r + 1   # arr[gt...r] > pivot
            i_idx = l + 1  # arr[lt+1...i-1] == pivot
            
            while i_idx < gt:
                if arr[i_idx] < pivot:
                    lt += 1
                    arr[lt], arr[i_idx] = arr[i_idx], arr[lt]
                    i_idx += 1
                elif arr[i_idx] > pivot:
                    gt -= 1
                    arr[gt], arr[i_idx] = arr[i_idx], arr[gt]
                else:
                    i_idx += 1
            
            arr[l], arr[lt] = arr[lt], arr[l]
            
            # 优先处理较小的子数组，减少递归深度
            if lt - l < r - gt + 1:
                # 左边子数组较小，先递归处理左边
                self.quick_sort_tail_recursive(arr, l, lt - 1)
                # 尾递归优化：将右边子数组的处理转换为迭代
                l = gt
            else:
                # 右边子数组较小，先递归处理右边
                self.quick_sort_tail_recursive(arr, gt, r)
                # 尾递归优化：将左边子数组的处理转换为迭代
                r = lt - 1
    
    # 【优化版本3】三数取中法选择基准
    # 选择左、中、右三个位置的中位数作为基准，提高最坏情况性能
    def quick_sort_median_of_three(self, arr, l, r):
        """
        三数取中法优化的快速排序
        选择左、中、右三个位置的中位数作为基准，提高最坏情况性能
        :param arr: 待排序列表
        :param l: 排序区间的左边界（包含）
        :param r: 排序区间的右边界（包含）
        """
        # 递归终止条件
        if l >= r:
            return
        
        # 三数取中选择基准
        mid = l + (r - l) // 2
        self.median_of_three(arr, l, mid, r)
        
        pivot = arr[l]
        lt = l       # arr[l+1...lt] < pivot
        gt = r + 1   # arr[gt...r] > pivot
        i_idx = l + 1  # arr[lt+1...i-1] == pivot
        
        while i_idx < gt:
            if arr[i_idx] < pivot:
                lt += 1
                arr[lt], arr[i_idx] = arr[i_idx], arr[lt]
                i_idx += 1
            elif arr[i_idx] > pivot:
                gt -= 1
                arr[gt], arr[i_idx] = arr[i_idx], arr[gt]
            else:
                i_idx += 1
        
        arr[l], arr[lt] = arr[lt], arr[l]
        
        self.quick_sort_median_of_three(arr, l, lt - 1)
        self.quick_sort_median_of_three(arr, gt, r)
    
    # 三数取中辅助方法
    def median_of_three(self, arr, a, b, c):
        """
        三数取中辅助方法
        选择左、中、右三个位置的中位数作为基准，提高最坏情况性能
        :param arr: 数组
        :param a: 左边界索引
        :param b: 中间索引
        :param c: 右边界索引
        """
        # 确保arr[a] <= arr[b] <= arr[c]
        if arr[a] > arr[b]:
            arr[a], arr[b] = arr[b], arr[a]
        if arr[b] > arr[c]:
            arr[b], arr[c] = arr[c], arr[b]
        if arr[a] > arr[b]:
            arr[a], arr[b] = arr[b], arr[a]
        # 将中位数放到位置a（作为基准）
        arr[a], arr[b] = arr[b], arr[a]
    
    # 【LeetCode 215解法】数组中的第K个最大元素
    def find_kth_largest(self, nums, k):
        """
        LeetCode 215解法：数组中的第K个最大元素
        :param nums: 输入数组
        :param k: 第k大的元素
        :return: 第k大的元素值
        """
        # 第K大元素等价于第len(nums)-k小的元素
        # 深拷贝避免修改原数组
        nums_copy = nums.copy()
        return self.quick_select(nums_copy, 0, len(nums_copy) - 1, len(nums_copy) - k)
    
    # 【剑指Offer 40解法】最小的k个数
    def get_least_numbers(self, arr, k):
        """
        剑指Offer 40解法：最小的k个数
        :param arr: 输入数组
        :param k: 需要返回的最小元素个数
        :return: 包含最小k个数的数组
        """
        # 边界条件处理
        if k <= 0:
            return []
        if k >= len(arr):
            return arr.copy()
        
        # 深拷贝避免修改原数组
        nums_copy = arr.copy()
        
        # 使用快速选择找到第k小的元素
        self.quick_select(nums_copy, 0, len(nums_copy) - 1, k - 1)
        
        # 收集前k个最小元素
        return nums_copy[:k]
    
    # 【LeetCode 75解法】颜色分类（三路快排应用）
    def sort_colors(self, nums):
        """
        LeetCode 75解法：颜色分类（三路快排应用）
        :param nums: 包含0、1、2的数组，分别代表红、白、蓝三种颜色
        """
        # 三路快排思想：0放左边，1放中间，2放右边
        zero = -1      # [0...zero] == 0
        two = len(nums)  # [two...n-1] == 2
        i = 0          # 当前处理的位置
        
        while i < two:
            if nums[i] == 0:
                # 当前元素为0，交换到0区域的下一个位置
                zero += 1
                nums[zero], nums[i] = nums[i], nums[zero]
                i += 1
            elif nums[i] == 1:
                # 当前元素为1，保持在中间区域
                i += 1
            else:  # nums[i] == 2
                # 当前元素为2，交换到2区域的前一个位置
                two -= 1
                nums[i], nums[two] = nums[two], nums[i]
    
    # 【LeetCode 283解法】移动零（分区思想应用）
    def move_zeroes(self, nums):
        """
        LeetCode 283解法：移动零（分区思想应用）
        :param nums: 输入数组
        """
        # 双指针：将非零元素移动到数组前面
        non_zero_pos = 0  # 指向下一个非零元素应该放的位置
        
        # 第一次遍历：将所有非零元素移到前面
        for i in range(len(nums)):
            if nums[i] != 0:
                if i != non_zero_pos:
                    nums[i], nums[non_zero_pos] = nums[non_zero_pos], nums[i]
                non_zero_pos += 1
    
    # 【LeetCode 462解法】最少移动次数使数组元素相等II（中位数思想）
    def min_moves2(self, nums):
        """
        LeetCode 462解法：最少移动次数使数组元素相等II（中位数思想）
        :param nums: 输入数组
        :return: 最少移动次数
        """
        n = len(nums)
        # 深拷贝避免修改原数组
        nums_copy = nums.copy()
        median = self.quick_select(nums_copy, 0, n - 1, n // 2)
        
        # 计算所有元素到中位数的距离和
        moves = 0
        for num in nums:
            moves += abs(num - median)
        return moves
    
    # 【LeetCode 912解法】排序数组（标准快速排序实现）
    def sort_array(self, nums):
        """
        LeetCode 912解法：排序数组（标准快速排序实现）
        :param nums: 待排序数组
        :return: 排序后的数组
        """
        # 深拷贝避免修改原数组
        result = nums.copy()
        self.quick_sort_optimized(result, 0, len(result) - 1)
        return result
    
    # 【洛谷 P1177 解法】快速排序模板题
    def luogu_p1177(self, arr):
        """
        洛谷 P1177 解法：快速排序模板题
        :param arr: 待排序数组
        """
        self.quick_sort_optimized(arr, 0, len(arr) - 1)
    
    # 【POJ 2388 解法】Who's in the Middle（中位数问题）
    def find_median(self, nums):
        """
        POJ 2388 解法：Who's in the Middle（中位数问题）
        :param nums: 输入数组
        :return: 中位数
        """
        n = len(nums)
        # 中位数就是第 (n-1)//2 小的元素（0-based索引）
        # 深拷贝避免修改原数组
        nums_copy = nums.copy()
        return self.quick_select(nums_copy, 0, n - 1, (n - 1) // 2)
    
    # 【异常处理】健壮的快速排序实现
    def robust_quick_sort(self, nums):
        """
        异常处理：健壮的快速排序实现
        :param nums: 待排序数组
        """
        # 空数组或单元素数组检查
        if len(nums) <= 1:
            return
        
        # 执行排序
        self.quick_sort_optimized(nums, 0, len(nums) - 1)
    
    # 【AtCoder ABC121C 解法】Energy Drink Collector（贪心+排序）
    def energy_drink_collector(self, drinks, budget):
        """
        AtCoder ABC121C 解法：Energy Drink Collector（贪心+排序）
        :param drinks: 能量饮料信息，每行包含价格和数量
        :param budget: 预算
        :return: 能获得的最大能量
        """
        # 按价格升序排序
        sorted_drinks = sorted(drinks, key=lambda x: x[0])
        
        total_energy = 0
        remaining_budget = budget
        
        for price, amount in sorted_drinks:
            can_buy = min(amount, remaining_budget // price)
            total_energy += can_buy
            remaining_budget -= can_buy * price
            
            if remaining_budget < price:
                break
        
        return total_energy
    
    # 【Codeforces 401C 解法】Team（贪心构造）
    def construct_team(self, n, m):
        """
        Codeforces 401C 解法：Team（贪心构造）
        :param n: 0的数量
        :param m: 1的数量
        :return: 构造的01序列
        """
        # 构造一个01序列，满足特定约束条件
        result = []
        
        while n > 0 or m > 0:
            # 优先放置0的情况
            if n > m:
                # 检查是否可以放置00
                if n >= 2 and m >= 1:
                    result.append("001")
                    n -= 2
                    m -= 1
                else:
                    result.append("0")
                    n -= 1
            else:
                # 优先放置1的情况
                if m >= 2 and n >= 1:
                    result.append("110")
                    m -= 2
                    n -= 1
                else:
                    result.append("1")
                    m -= 1
        
        return ''.join(result)
    
    # 【调试辅助方法】打印数组内容
    def print_array(self, nums):
        """
        调试辅助方法：打印数组内容
        :param nums: 要打印的数组
        """
        print(' '.join(map(str, nums)))
    
    # 【测试验证方法】检查数组是否有序
    def is_sorted(self, nums):
        """
        测试验证方法：检查数组是否有序
        :param nums: 要检查的数组
        :return: 是否有序
        """
        for i in range(1, len(nums)):
            if nums[i] < nums[i-1]:
                return False
        return True

# 测试函数
def main():
    """
    主函数：测试各种快速排序实现
    """
    solution = QuickSortSolution()
    
    # 测试基础快速排序
    arr1 = [5, 2, 3, 1, 4]
    print("原始数组:", arr1)
    solution.quick_sort1(arr1, 0, len(arr1) - 1)
    print("排序后数组:", arr1)
    
    # 测试三路快速排序
    arr2 = [5, 2, 3, 1, 4, 2, 3]
    print("\n原始数组:", arr2)
    solution.quick_sort2(arr2, 0, len(arr2) - 1)
    print("三路快排后:", arr2)
    
    # 测试快速选择算法
    arr3 = [3, 2, 1, 5, 6, 4]
    k = 2  # 查找第2大的元素（即索引为4的元素）
    result = solution.quick_select(arr3, 0, len(arr3) - 1, len(arr3) - k)
    print("\n数组:", arr3)
    print(f"第{k}大的元素是: {result}")
    
    # 测试堆排序
    arr4 = [5, 2, 3, 1, 4]
    print("\n原始数组:", arr4)
    solution.heap_sort(arr4)
    print("堆排序后:", arr4)
    
    # 测试优化版本的快速排序
    arr5 = [9, 8, 7, 6, 5, 4, 3, 2, 1, 0, 5, 6, 7, 8, 9]
    print("\n原始数组:", arr5)
    solution.quick_sort_optimized(arr5, 0, len(arr5) - 1)
    print("优化快排后:", arr5)
    print(f"数组是否有序: {solution.is_sorted(arr5)}")
    
    # 测试颜色分类
    colors = [2, 0, 2, 1, 1, 0]
    print("\n原始颜色数组:", colors)
    solution.sort_colors(colors)
    print("颜色分类后:", colors)
    
    # 测试移动零
    zeros = [0, 1, 0, 3, 12]
    print("\n原始数组:", zeros)
    solution.move_zeroes(zeros)
    print("移动零后:", zeros)
    
    # 测试寻找第K大元素
    nums = [3, 2, 1, 5, 6, 4]
    k = 2
    print("\n数组:", nums)
    print(f"第{k}大的元素是: {solution.find_kth_largest(nums, k)}")
    
    # 测试最小的k个数
    arr6 = [3, 2, 1, 5, 6, 4]
    k = 3
    print("\n数组:", arr6)
    print(f"最小的{k}个数是: {solution.get_least_numbers(arr6, k)}")
    
    # 测试中位数问题
    arr7 = [1, 2, 3, 4, 5]
    print("\n数组:", arr7)
    print(f"中位数是: {solution.find_median(arr7)}")
    
    # 测试插入排序
    arr8 = [9, 8, 7, 6, 5]
    print("\n原始数组:", arr8)
    solution.insertion_sort(arr8, 0, len(arr8) - 1)
    print("插入排序后:", arr8)
    
    # 测试健壮性处理
    empty_arr = []
    single_arr = [5]
    solution.robust_quick_sort(empty_arr)
    solution.robust_quick_sort(single_arr)
    print("\n空数组排序后:", empty_arr)
    print("单元素数组排序后:", single_arr)

if __name__ == "__main__":
    main()