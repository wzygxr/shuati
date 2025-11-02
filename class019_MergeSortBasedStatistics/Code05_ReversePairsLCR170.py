"""
============================================================================
题目5: 剑指 Offer 51 - 数组中的逆序对 (LCR 170) - Python版
============================================================================

题目来源: 剑指Offer / LCR 170
题目链接: https://leetcode.cn/problems/shu-zu-zhong-de-ni-xu-dui-lcof/
难度级别: 困难

问题描述:
在数组中的两个数字，如果前面一个数字大于后面的数字，则这两个数字组成一个逆序对。
输入一个数组，求出这个数组中的逆序对的总数。

示例输入输出:
输入: [7,5,6,4]
输出: 5
解释: 逆序对有(7,5), (7,6), (7,4), (5,4), (6,4)

============================================================================
核心算法思想: 归并排序分治统计
============================================================================

方法1: 暴力解法 (不推荐)
- 思路: 双重循环检查每一对 (i,j) 是否满足 i<j 且 nums[i] > nums[j]
- 时间复杂度: O(N^2) - 双重循环
- 空间复杂度: O(1) - 不需要额外空间
- 问题: 数据量大时超时

方法2: 归并排序思想 (最优解) ★★★★★
- 核心洞察: 利用归并排序过程统计跨越左右两个子数组的逆序对
  - 先统计左半部分内部的逆序对
  - 再统计右半部分内部的逆序对
  - 最后统计跨越左右两部分的逆序对（关键步骤）

- 统计跨区间逆序对的优化方法:
  - 在合并前，对每个左区间元素nums[i]，找到右区间中满足 nums[i] > nums[j] 的元素个数
  - 利用双指针技巧：由于左右子数组已排序，可以线性扫描而不需要嵌套循环
  - 这一步的时间复杂度为O(n)而非O(n²)

- 归并排序过程:
  1. 分治: 将数组不断二分，直到只有一个元素
  2. 统计: 统计三种类型的逆序对
  3. 合并: 将两个有序子数组合并

- 时间复杂度详细计算:
  T(n) = 2T(n/2) + O(n)  [Master定理 case 2]
  = O(n log n)
  - 递归深度: log n
  - 每层合并与统计: O(n)

- 空间复杂度详细计算:
  S(n) = O(n) + O(log n)
  - O(n): 辅助数组help
  - O(log n): 递归调用栈
  总计: O(n)

- 是否最优解: ★ 是 ★
  理由: 基于比较的算法下界为O(n log n)，本算法已达到最优

============================================================================
相关题目列表 (同类算法)
============================================================================
1. LeetCode 493 - 翻转对
   https://leetcode.cn/problems/reverse-pairs/
   问题：统计满足 nums[i] > 2*nums[j] 且 i < j 的对的数量
   解法：归并排序过程中使用双指针统计跨越左右区间的翻转对

2. LeetCode 315 - 计算右侧小于当前元素的个数
   https://leetcode.cn/problems/count-of-smaller-numbers-after-self/
   问题：统计每个元素右侧比它小的元素个数
   解法：归并排序过程中记录元素原始索引，统计右侧小于当前元素的数量

3. LeetCode 327 - 区间和的个数
   https://leetcode.cn/problems/count-of-range-sum/
   问题：统计区间和在[lower, upper]范围内的区间个数
   解法：前缀和+归并排序，统计满足条件的前缀和对

4. POJ 2299 - Ultra-QuickSort
   http://poj.org/problem?id=2299
   问题：计算将数组排序所需的最小交换次数（即逆序对数量）
   解法：归并排序统计逆序对

5. HDU 1394 - Minimum Inversion Number
   http://acm.hdu.edu.cn/showproblem.php?pid=1394
   问题：将数组循环左移，求所有可能排列中的最小逆序对数量
   解法：归并排序+逆序对性质分析

6. 洛谷 P1908 - 逆序对
   https://www.luogu.com.cn/problem/P1908
   问题：统计数组中逆序对的总数
   解法：归并排序统计逆序对

7. HackerRank - Merge Sort: Counting Inversions
   https://www.hackerrank.com/challenges/merge-sort/problem
   问题：统计逆序对数量
   解法：归并排序统计逆序对

8. SPOJ - INVCNT
   https://www.spoj.com/problems/INVCNT/
   问题：统计逆序对数量
   解法：归并排序统计逆序对

9. CodeChef - INVCNT
   https://www.codechef.com/problems/INVCNT
   问题：统计逆序对数量
   解法：归并排序或树状数组

这些题目虽然具体形式不同，但核心思想都是利用归并排序的分治特性，在合并过程中高效统计满足特定条件的元素对数量。
"""

def reverse_pairs(nums):
    """
    计算数组中逆序对的数量
    
    Args:
        nums: 输入数组
        
    Returns:
        int: 逆序对的数量
    """
    if not nums or len(nums) < 2:
        return 0
    
    def merge_sort(l, r):
        """
        归并排序，在排序过程中统计逆序对数量
        
        Args:
            l: 左边界
            r: 右边界
            
        Returns:
            int: 区间[l,r]中的逆序对数量
        """
        if l == r:
            return 0
        
        mid = (l + r) // 2
        return merge_sort(l, mid) + merge_sort(mid + 1, r) + merge(l, mid, r)
    
    def merge(l, m, r):
        """
        合并两个有序数组，并统计跨越左右两部分的逆序对
        
        Args:
            l: 左边界
            m: 中点
            r: 右边界
            
        Returns:
            int: 跨越左右两部分的逆序对数量
        """
        # 辅助数组
        help_arr = [0] * (r - l + 1)
        
        # 统计逆序对数量
        ans = 0
        j = m + 1
        for i in range(l, m + 1):
            # 找到右半部分中第一个不满足 nums[i] > nums[j] 的位置
            while j <= r and nums[i] > nums[j]:
                j += 1
            # j之前的元素都满足条件，即与nums[i]构成逆序对
            ans += (j - m - 1)
        
        # 正常合并两个有序数组
        i = 0
        a, b = l, m + 1
        while a <= m and b <= r:
            if nums[a] <= nums[b]:
                help_arr[i] = nums[a]
                a += 1
            else:
                help_arr[i] = nums[b]
                b += 1
            i += 1
        
        # 处理剩余元素
        while a <= m:
            help_arr[i] = nums[a]
            a += 1
            i += 1
        
        while b <= r:
            help_arr[i] = nums[b]
            b += 1
            i += 1
        
        # 将辅助数组内容复制回原数组
        for i in range(len(help_arr)):
            nums[l + i] = help_arr[i]
        
        return ans
    
    # 创建数组副本，避免修改原数组
    nums_copy = nums[:]
    return merge_sort(0, len(nums_copy) - 1)


# 测试代码
if __name__ == "__main__":
    # 测试用例1: 基本情况
    test_nums1 = [7, 5, 6, 4]
    print(f"输入: {test_nums1}")
    print(f"输出: {reverse_pairs(test_nums1)}")  # 预期输出: 5
    
    # 测试用例2: 空数组
    test_nums2 = []
    print(f"输入: {test_nums2}")
    print(f"输出: {reverse_pairs(test_nums2)}")  # 预期输出: 0
    
    # 测试用例3: 单元素数组
    test_nums3 = [1]
    print(f"输入: {test_nums3}")
    print(f"输出: {reverse_pairs(test_nums3)}")  # 预期输出: 0
    
    # 测试用例4: 升序数组
    test_nums4 = [1, 2, 3, 4]
    print(f"输入: {test_nums4}")
    print(f"输出: {reverse_pairs(test_nums4)}")  # 预期输出: 0
    
    # 测试用例5: 降序数组
    test_nums5 = [4, 3, 2, 1]
    print(f"输入: {test_nums5}")
    print(f"输出: {reverse_pairs(test_nums5)}")  # 预期输出: 6
    
    # 测试用例6: 重复元素
    test_nums6 = [2, 2, 2, 2]
    print(f"输入: {test_nums6}")
    print(f"输出: {reverse_pairs(test_nums6)}")  # 预期输出: 0
    
    # 测试用例7: 包含负数
    test_nums7 = [-1, -2, 3, -4]
    print(f"输入: {test_nums7}")
    print(f"输出: {reverse_pairs(test_nums7)}")  # 预期输出: 4


"""
============================================================================
Python语言特有关注事项
============================================================================

1. 整数精度优势:
   - Python的整数类型自动支持大整数，不会有溢出问题
   - 相比Java和C++，无需手动转换为long/long long类型
   - 适合处理极端大的结果

2. 递归深度限制:
   - Python默认递归深度限制约为1000层
   - 对于大规模数据(n=50000)，归并排序的递归深度(log2(50000)≈16层)完全没问题
   - 但处理n接近2^30的数据时，需要调整递归深度限制:
     import sys
     sys.setrecursionlimit(1000000)

3. 列表操作效率:
   - 列表切片操作(arr[:])会创建副本，有O(n)时间和空间开销
   - 频繁创建小列表会增加GC压力
   - 推荐使用索引操作代替切片，提升性能

4. 可变对象特性:
   - Python中列表是可变对象，函数内修改会影响外部
   - 实现中使用nums_copy避免修改原数组，保持函数纯度
   - 这在多线程环境中很重要

5. 生成器和迭代器:
   - 对于大数据集，可以考虑使用生成器节省内存
   - 但在算法核心部分，直接使用列表访问更快

6. 类型提示支持:
   - Python 3.5+支持类型提示，提高代码可读性和IDE支持
   - 例如: def reverse_pairs(nums: List[int]) -> int:
   - 需要导入: from typing import List

7. 性能考量:
   - Python的递归实现比迭代慢
   - 对于竞赛场景，Python可能在时间限制内无法处理最大规模数据
   - 实际应用中可以接受，但高性能场景考虑C++实现

8. 缓存装饰器:
   - 对于重复调用相同参数的场景，可以使用functools.lru_cache
   - 但此算法中不适用，因为每次处理的数组切片不同

9. 多进程并行:
   - Python的GIL限制了多线程性能提升
   - 对于大规模数据，考虑使用multiprocessing模块进行并行计算
   - 注意进程间通信的开销

10. 调试便利性:
    - Python的print调试和异常信息比C++更友好
    - 可以使用pdb进行交互式调试
    - 列表推导式等语法使代码更简洁，但可能牺牲可读性
"""