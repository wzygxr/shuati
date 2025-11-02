# 区间和的个数，Python版
# 测试链接 : https://leetcode.cn/problems/count-of-range-sum/

"""
区间和的个数详解:

问题描述:
给你一个整数数组 nums 以及两个整数 lower 和 upper 。
求数组中，值位于范围 [lower, upper] （包含 lower 和 upper）之内的 区间和的个数 。
区间和 S(i, j) 表示在 nums 中，位置从 i 到 j 的元素之和，包含 i 和 j (i ≤ j)。

示例:
输入：nums = [-2,5,-1], lower = -2, upper = 2
输出：3
解释：存在三个区间：[0,0]、[2,2] 和 [0,2] ，对应的区间和分别是：-2 、-1 、2 。

解法思路:
1. 暴力解法: 计算所有可能区间的和，检查是否在范围内，时间复杂度O(N^3)或O(N^2)（使用前缀和优化）
2. 归并排序思想:
   - 使用前缀和将问题转换为: 找到满足条件 prefixSum[j] - prefixSum[i] ∈ [lower, upper] 的(i,j)对数量
   - 在归并排序过程中，对于左半部分的每个前缀和prefixSum[i]，在右半部分找到满足条件的prefixSum[j]数量
   - 由于两部分各自有序，可以使用双指针技巧优化查找过程

时间复杂度: O(N * logN) - 归并排序的时间复杂度
空间复杂度: O(N) - 前缀和数组和辅助数组的空间复杂度

相关题目:
1. LeetCode 493. 翻转对
2. LeetCode 315. 计算右侧小于当前元素的个数
3. 剑指Offer 51. 数组中的逆序对
4. 牛客网 - 计算数组的小和
"""

def count_range_sum(nums, lower, upper):
    """
    计算数组中区间和在指定范围内的个数
    
    Args:
        nums: 输入数组
        lower: 范围下界
        upper: 范围上界
        
    Returns:
        int: 区间和在指定范围内的个数
    """
    if not nums:
        return 0
    
    n = len(nums)
    # 计算前缀和数组
    prefix_sum = [0] * (n + 1)
    for i in range(n):
        prefix_sum[i + 1] = prefix_sum[i] + nums[i]
    
    def merge_sort(left, right):
        """
        归并排序，在排序过程中统计满足条件的区间和个数
        
        Args:
            left: 左边界
            right: 右边界
            
        Returns:
            int: 区间[left,right]中满足条件的区间和个数
        """
        if left == right:
            return 0
        
        mid = left + (right - left) // 2
        count = merge_sort(left, mid) + merge_sort(mid + 1, right)
        
        # 统计跨越左右两部分的满足条件的区间和个数
        l_ptr = mid + 1
        r_ptr = mid + 1
        for i in range(left, mid + 1):
            # 找到右半部分中第一个满足 prefix_sum[j] - prefix_sum[i] >= lower 的位置
            while l_ptr <= right and prefix_sum[l_ptr] - prefix_sum[i] < lower:
                l_ptr += 1
            # 找到右半部分中第一个满足 prefix_sum[j] - prefix_sum[i] > upper 的位置
            while r_ptr <= right and prefix_sum[r_ptr] - prefix_sum[i] <= upper:
                r_ptr += 1
            # 区间[l_ptr, r_ptr-1]中的元素都满足条件
            count += (r_ptr - l_ptr)
        
        # 合并两个有序数组
        help_arr = [0] * (right - left + 1)
        i = 0
        a, b = left, mid + 1
        while a <= mid and b <= right:
            if prefix_sum[a] <= prefix_sum[b]:
                help_arr[i] = prefix_sum[a]
                a += 1
            else:
                help_arr[i] = prefix_sum[b]
                b += 1
            i += 1
        
        while a <= mid:
            help_arr[i] = prefix_sum[a]
            a += 1
            i += 1
        
        while b <= right:
            help_arr[i] = prefix_sum[b]
            b += 1
            i += 1
        
        for i in range(len(help_arr)):
            prefix_sum[left + i] = help_arr[i]
        
        return count
    
    return merge_sort(0, n)


# 测试代码
if __name__ == "__main__":
    # 测试用例1: 基本情况
    test_nums1 = [-2, 5, -1]
    lower1, upper1 = -2, 2
    print(f"输入: nums = {test_nums1}, lower = {lower1}, upper = {upper1}")
    print(f"输出: {count_range_sum(test_nums1, lower1, upper1)}")  # 预期输出: 3
    
    # 测试用例2: 空数组
    test_nums2 = []
    lower2, upper2 = 0, 0
    print(f"输入: nums = {test_nums2}, lower = {lower2}, upper = {upper2}")
    print(f"输出: {count_range_sum(test_nums2, lower2, upper2)}")  # 预期输出: 0
    
    # 测试用例3: 单元素数组
    test_nums3 = [0]
    lower3, upper3 = 0, 0
    print(f"输入: nums = {test_nums3}, lower = {lower3}, upper = {upper3}")
    print(f"输出: {count_range_sum(test_nums3, lower3, upper3)}")  # 预期输出: 1
    
    # 测试用例4: 全为正数的数组
    test_nums4 = [1, 2, 3, 4]
    lower4, upper4 = 5, 10
    print(f"输入: nums = {test_nums4}, lower = {lower4}, upper = {upper4}")
    print(f"输出: {count_range_sum(test_nums4, lower4, upper4)}")  # 预期输出: 4
    
    # 测试用例5: 全为负数的数组
    test_nums5 = [-4, -3, -2, -1]
    lower5, upper5 = -6, -2
    print(f"输入: nums = {test_nums5}, lower = {lower5}, upper = {upper5}")
    print(f"输出: {count_range_sum(test_nums5, lower5, upper5)}")  # 预期输出: 4
    
    # 测试用例6: 大数值测试
    test_nums6 = [2147483647, -2147483647]
    lower6, upper6 = -2, 2
    print(f"输入: nums = [2147483647, -2147483647], lower = {lower6}, upper = {upper6}")
    print(f"输出: {count_range_sum(test_nums6, lower6, upper6)}")  # 预期输出: 1

"""
===========================================================================
Python语言特有关注事项
===========================================================================
1. 整数类型: 
   - Python的整数没有固定大小限制，不会出现整数溢出问题
   - 与Java/C++不同，无需显式使用long类型来存储大数值
   - 但仍需注意浮点数精度问题（本题不涉及）

2. 递归深度限制: 
   - Python的默认递归深度限制约为1000
   - 对于非常大的数组，可能会抛出RecursionError
   - 可以通过sys.setrecursionlimit()调整递归深度限制
   - 也可以考虑实现非递归版本的归并排序

3. 列表操作: 
   - Python列表的动态扩容机制可能带来性能开销
   - 预先分配列表大小可以提高性能（如代码中已做的那样）
   - 列表切片操作会创建新的列表，应尽量避免在递归中频繁使用

4. 闭包特性: 
   - merge_sort函数作为嵌套函数可以直接访问外部函数的变量
   - 这种闭包结构使代码更简洁，但要注意变量作用域

5. 内存管理: 
   - Python的垃圾回收机制自动管理内存，但递归调用可能导致内存消耗增加
   - 对于非常大的数据集，需要考虑内存使用效率

6. 性能优化: 
   - Python中的递归实现比迭代实现慢
   - 可以考虑使用更底层的优化，如NumPy数组操作
   - 对于小规模数据，可以使用更简单的暴力解法
"""

"""
===========================================================================
工程化考量
===========================================================================
1. 异常处理: 
   - 已添加对空数组的处理
   - 可以添加对无效输入的检查，如lower > upper的情况
   - 可以考虑添加类型检查，确保输入参数类型正确

2. 性能优化: 
   - 对于小规模数组（如长度<100），可以使用O(N^2)的暴力解法
   - 可以使用itertools模块中的工具函数简化实现
   - 考虑使用记忆化或缓存机制优化重复计算

3. 测试策略: 
   - 已提供多种测试用例，覆盖常见场景
   - 可以使用unittest或pytest框架进行更系统的测试
   - 建议添加随机测试用例和边界值测试

4. 代码可读性: 
   - 使用了清晰的函数和变量命名
   - 添加了详细的注释说明算法思路
   - 函数参数和返回值都有明确的文档字符串

5. 可扩展性: 
   - 可以将核心逻辑抽象为可配置的类
   - 可以扩展支持流数据处理
   - 考虑添加进度显示，用于处理大规模数据

6. 并行处理: 
   - 对于超大规模数据，可以考虑使用multiprocessing模块并行化处理
   - 但需要注意Python的GIL对多线程的限制
   - 可以使用concurrent.futures库简化并行任务管理

7. 边界情况处理: 
   - 空数组：返回0
   - 单元素数组：检查该元素是否在范围内
   - 全部为相同元素：优化计算逻辑
   - 大数值：Python无需特殊处理
"""

"""
===========================================================================
相关题目与平台信息
===========================================================================
1. LeetCode 327. Count of Range Sum
   - 题目链接：https://leetcode.cn/problems/count-of-range-sum/
   - 难度等级：困难
   - 标签：归并排序、前缀和、二分查找

2. LeetCode 493. 翻转对 (Reverse Pairs)
   - 题目链接：https://leetcode.cn/problems/reverse-pairs/
   - 难度等级：困难
   - 解题思路：同样使用归并排序的过程统计满足条件的对

3. LeetCode 315. 计算右侧小于当前元素的个数 (Count of Smaller Numbers After Self)
   - 题目链接：https://leetcode.cn/problems/count-of-smaller-numbers-after-self/
   - 难度等级：困难
   - 解题思路：类似的归并排序框架，统计右侧较小的元素个数

4. 剑指Offer 51. 数组中的逆序对
   - 题目链接：https://leetcode.cn/problems/shu-zu-zhong-de-ni-xu-dui-lcof/
   - 难度等级：困难
   - 解题思路：归并排序过程中统计逆序对数量

5. 牛客网 - 计算数组的小和
   - 题目链接：https://www.nowcoder.com/practice/edfe05a1d45c4ea89101d936cac32469
   - 解题思路：归并排序过程中计算小和

6. LintCode 1497. 区间和的个数
   - 题目链接：https://www.lintcode.com/problem/1497/
   - 与LeetCode 327题相同

7. HackerRank - Sum of Intervals
   - 类似问题，但可能有不同的约束条件
   - 可能需要处理重叠区间

8. CodeChef - Range Sum Query
   - 可能需要多次查询不同区间的和
   - 可能需要预处理优化

9. 华为机试 - 区间和统计
   - 类似问题，但可能有不同的输入输出格式要求

10. 字节跳动面试题 - 前缀和区间统计
    - 实际面试中可能会对本题进行变体，如不同的范围条件或附加约束

11. POJ 2299 - Ultra-QuickSort
    - 题目链接：http://poj.org/problem?id=2299
    - 计算逆序对数量，与本题使用类似的归并排序框架

12. SPOJ - INVCNT
    - 题目链接：https://www.spoj.com/problems/INVCNT/
    - 逆序对计数问题，可使用归并排序解决

13. 计蒜客 - 区间和查询
    - 可能包含多个查询操作
    - 考察前缀和和二分查找的应用

14. AtCoder - Range Sum Query
    - 类似的区间查询问题，可能有不同的约束条件

15. USACO - Cow Sorting
    - 问题涉及逆序对的计算，与本题思路相关
"""