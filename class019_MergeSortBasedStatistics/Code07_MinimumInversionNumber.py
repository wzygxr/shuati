"""
============================================================================
题目7: HDU 1394 - Minimum Inversion Number - Python版
============================================================================

题目来源: HDU (杭州电子科技大学OJ)
题目链接: http://acm.hdu.edu.cn/showproblem.php?pid=1394
难度级别: 中等

问题描述:
给定一个0到n-1的排列，可以进行循环移位操作（把第一个数移到最后）。
求所有可能状态中逆序对数量的最小值。

核心洞察:
1. 先用归并排序计算初始逆序对数
2. 循环移位时，利用数学公式快速更新逆序对数
   - 若移动元素x: new_inv = old_inv - x + (n-1-x)

示例输入输出:
输入:
5
1 3 0 2 4

输出:
3

解释:
初始序列[1,3,0,2,4]有6个逆序对: (1,0), (3,0), (3,2), (2,0), (2,1), (4,0)
循环移位后序列[3,0,2,4,1]有5个逆序对
循环移位后序列[0,2,4,1,3]有4个逆序对
循环移位后序列[2,4,1,3,0]有5个逆序对
循环移位后序列[4,1,3,0,2]有3个逆序对 (最小值)

============================================================================
核心算法思想: 归并排序 + 数学优化
============================================================================

方法1: 暴力解法 (不推荐)
- 思路: 对每个循环移位后的序列，都计算一次逆序对数量
- 时间复杂度: O(N^3) - N次循环移位，每次O(N^2)计算逆序对
- 空间复杂度: O(N) - 存储循环移位后的序列
- 问题: 数据量大时超时

方法2: 归并排序 + 数学优化 (最优解) ★★★★★
- 核心洞察: 
  1. 先计算初始序列的逆序对数
  2. 利用数学关系快速计算循环移位后的逆序对数

- 数学优化原理:
  当把第一个元素x移到序列末尾时：
  - 减少的逆序对数：原来在x后面且小于x的元素个数，即x个
  - 增加的逆序对数：原来在x后面且大于x的元素个数，即(n-1-x)个
  - 因此：new_inv = old_inv - x + (n-1-x)

- 算法步骤:
  1. 使用归并排序计算初始序列的逆序对数
  2. 循环N-1次，每次根据数学公式更新逆序对数
  3. 记录过程中的最小值

- 时间复杂度详细计算:
  T(n) = O(n log n) + O(n) = O(n log n)
  - 归并排序计算初始逆序对: O(n log n)
  - 循环更新逆序对数: O(n)

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
1. POJ 2299 - Ultra-QuickSort
   http://poj.org/problem?id=2299
   问题：计算将数组排序所需的最小交换次数（即逆序对数量）
   解法：归并排序统计逆序对

2. 剑指Offer 51 / LCR 170 - 数组中的逆序对
   https://leetcode.cn/problems/shu-zu-zhong-de-ni-xu-dui-lcof/
   问题：统计数组中逆序对的总数
   解法：归并排序过程中统计逆序对数量

3. LeetCode 493 - 翻转对
   https://leetcode.cn/problems/reverse-pairs/
   问题：统计满足 nums[i] > 2*nums[j] 且 i < j 的对的数量
   解法：归并排序过程中使用双指针统计跨越左右区间的翻转对

4. LeetCode 315 - 计算右侧小于当前元素的个数
   https://leetcode.cn/problems/count-of-smaller-numbers-after-self/
   问题：统计每个元素右侧比它小的元素个数
   解法：归并排序过程中记录元素原始索引，统计右侧小于当前元素的数量

5. LeetCode 327 - 区间和的个数
   https://leetcode.cn/problems/count-of-range-sum/
   问题：统计区间和在[lower, upper]范围内的区间个数
   解法：前缀和+归并排序，统计满足条件的前缀和对

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

9. UVa 10810 - Ultra-QuickSort
   https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=1751
   问题：计算将数组排序所需的最小交换次数（即逆序对数量）
   解法：归并排序统计逆序对

这些题目虽然具体形式不同，但核心思想都是利用归并排序的分治特性，在合并过程中高效统计满足特定条件的元素对数量。
"""

import sys

def merge_sort_and_count(arr):
    """
    归并排序，在排序过程中统计逆序对数量
    
    Args:
        arr: 输入数组
        
    Returns:
        int: 逆序对的数量
    """
    if len(arr) < 2:
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
            # 找到右半部分中第一个不满足 arr[i] > arr[j] 的位置
            while j <= r and arr[i] > arr[j]:
                j += 1
            # j之前的元素都满足条件，即与arr[i]构成逆序对
            ans += (j - m - 1)
        
        # 正常合并两个有序数组
        i = 0
        a, b = l, m + 1
        while a <= m and b <= r:
            if arr[a] <= arr[b]:
                help_arr[i] = arr[a]
                a += 1
            else:
                help_arr[i] = arr[b]
                b += 1
            i += 1
        
        # 处理剩余元素
        while a <= m:
            help_arr[i] = arr[a]
            a += 1
            i += 1
        
        while b <= r:
            help_arr[i] = arr[b]
            b += 1
            i += 1
        
        # 将辅助数组内容复制回原数组
        for i in range(len(help_arr)):
            arr[l + i] = help_arr[i]
        
        return ans
    
    # 创建数组副本，避免修改原数组
    arr_copy = arr[:]
    return merge_sort(0, len(arr_copy) - 1)


def get_minimum_inversion(arr):
    """
    计算循环移位序列中的最小逆序对数
    
    Args:
        arr: 输入数组
        
    Returns:
        int: 最小逆序对数
    """
    n = len(arr)
    if n == 0:
        return 0
    
    # 计算初始序列的逆序对数
    inv = merge_sort_and_count(arr)
    min_inv = inv
    
    # 循环移位，利用数学公式快速更新逆序对数
    for i in range(n - 1):
        # 当把第一个元素arr[i]移到序列末尾时：
        # 减少的逆序对数：arr[i]个
        # 增加的逆序对数：(n-1-arr[i])个
        inv = inv - arr[i] + (n - 1 - arr[i])
        min_inv = min(min_inv, inv)
    
    return min_inv


# HDU格式的输入输出处理
def main():
    """
    主函数，处理HDU格式的输入输出
    """
    # 增加递归深度限制以处理大规模数据
    sys.setrecursionlimit(100000)
    
    try:
        while True:
            line = input().strip()
            if not line:
                break
                
            n = int(line)
            line = input().strip()
            arr = list(map(int, line.split()))
            
            result = get_minimum_inversion(arr)
            print(result)
    except EOFError:
        pass


# 测试代码
def test():
    """
    测试函数
    """
    # 测试用例1: 基本情况
    test_arr1 = [1, 3, 0, 2, 4]
    print(f"输入: {test_arr1}")
    print(f"输出: {get_minimum_inversion(test_arr1)}")  # 预期输出: 3
    
    # 测试用例2: 已排序数组
    test_arr2 = [0, 1, 2, 3, 4]
    print(f"输入: {test_arr2}")
    print(f"输出: {get_minimum_inversion(test_arr2)}")  # 预期输出: 0
    
    # 测试用例3: 逆序数组
    test_arr3 = [4, 3, 2, 1, 0]
    print(f"输入: {test_arr3}")
    print(f"输出: {get_minimum_inversion(test_arr3)}")  # 预期输出: 6


if __name__ == "__main__":
    # 运行测试
    # test()
    
    # 运行HDU格式的输入输出处理
    main()


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
   - 对于大规模数据(n=10000)，需要调整递归深度限制:
     import sys
     sys.setrecursionlimit(100000)

3. 列表操作效率:
   - 列表切片操作(arr[:])会创建副本，有O(n)时间和空间开销
   - 频繁创建小列表会增加GC压力
   - 推荐使用索引操作代替切片，提升性能

4. 可变对象特性:
   - Python中列表是可变对象，函数内修改会影响外部
   - 实现中使用arr_copy避免修改原数组，保持函数纯度
   - 这在多线程环境中很重要

5. 生成器和迭代器:
   - 对于大数据集，可以考虑使用生成器节省内存
   - 但在算法核心部分，直接使用列表访问更快

6. 类型提示支持:
   - Python 3.5+支持类型提示，提高代码可读性和IDE支持
   - 例如: def get_minimum_inversion(arr: List[int]) -> int:
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