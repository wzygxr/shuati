# LeetCode 315 - 计算右侧小于当前元素的个数
# 题目来源: LeetCode
# 题目链接: https://leetcode.cn/problems/count-of-smaller-numbers-after-self/
# 难度级别: 困难

'''
============================================================================
题目9: LeetCode 315 - 计算右侧小于当前元素的个数
============================================================================

题目来源: LeetCode
题目链接: https://leetcode.cn/problems/count-of-smaller-numbers-after-self/
难度级别: 困难

问题描述:
给你一个整数数组 nums ，按要求返回一个新数组 counts 。
数组 counts 有该性质：counts[i] 的值是 nums[i] 右侧小于 nums[i] 的元素的数量。

示例输入输出:
输入: nums = [5,2,6,1]
输出: [2,1,1,0]
解释:
- 对于nums[0]=5，右侧小于5的元素有2和1，所以counts[0]=2
- 对于nums[1]=2，右侧小于2的元素有1，所以counts[1]=1
- 对于nums[2]=6，右侧小于6的元素有1，所以counts[2]=1
- 对于nums[3]=1，右侧没有元素，所以counts[3]=0

============================================================================
核心算法思想: 归并排序+索引映射
============================================================================

方法1: 暴力解法 (不推荐)
- 思路: 双重循环检查每个元素右侧有多少元素比它小
- 时间复杂度: O(N^2) - 双重循环
- 空间复杂度: O(N) - 结果数组
- 问题: 数据量大时超时

方法2: 归并排序思想 (最优解) ★★★★★
- 核心洞察: 
  1. 利用归并排序的分治过程统计元素之间的大小关系
  2. 关键挑战: 归并排序会改变元素顺序，需要维护原始索引
  3. 解决方案: 创建索引数组，对索引进行排序而非对值排序

- 归并排序过程:
  1. 分治: 将数组不断二分，直到只有一个元素
  2. 统计: 在合并过程中统计右侧小于当前元素的数量
  3. 合并: 按值的大小合并两个有序子数组

- 统计右侧小元素的关键步骤:
  - 当右子数组中的元素被选中时，不会对左侧元素产生影响
  - 当左子数组中的元素被选中时，右子数组中剩余的所有元素都是比它小的
  - 因此，每次选中左子数组元素时，需要记录右侧已经统计过的元素数量

- 时间复杂度详细计算:
  T(n) = 2T(n/2) + O(n)  [Master定理 case 2]
  = O(n log n)
  - 递归深度: log n
  - 每层合并与统计: O(n)

- 空间复杂度详细计算:
  S(n) = O(n) + O(log n)
  - O(n): 辅助数组、索引数组、结果数组
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

2. LeetCode 327 - 区间和的个数
   https://leetcode.cn/problems/count-of-range-sum/
   问题：统计区间和在[lower, upper]范围内的区间个数
   解法：前缀和+归并排序，统计满足条件的前缀和对

3. 剑指Offer 51 / LCR 170 - 数组中的逆序对
   https://leetcode.cn/problems/shu-zu-zhong-de-ni-xu-dui-lcof/
   问题：统计数组中逆序对的总数
   解法：归并排序过程中统计逆序对数量

4. LeetCode 1365 - 有多少小于当前数字的数字
   https://leetcode.cn/problems/how-many-numbers-are-smaller-than-the-current-number/
   问题：统计数组中小于当前数字的数字个数（全数组范围）
   解法：排序+哈希表映射

5. POJ 2299 - Ultra-QuickSort
   http://poj.org/problem?id=2299
   问题：计算将数组排序所需的最小交换次数（即逆序对数量）
   解法：归并排序统计逆序对

6. HackerRank - Merge Sort: Counting Inversions
   https://www.hackerrank.com/challenges/merge-sort/problem
   问题：统计逆序对数量
   解法：归并排序统计逆序对

7. 牛客网 - 计算右侧小于当前元素的个数
   问题：与LeetCode 315相同
   解法：归并排序+索引映射

8. 杭电OJ - 1394
   http://acm.hdu.edu.cn/showproblem.php?pid=1394
   问题：将数组循环左移，求所有可能排列中的最小逆序对数量
   解法：归并排序+逆序对性质分析

9. 洛谷 P1908 - 逆序对
   https://www.luogu.com.cn/problem/P1908
   问题：统计数组中逆序对的总数
   解法：归并排序统计逆序对

10. SPOJ - INVCNT
    https://www.spoj.com/problems/INVCNT/
    问题：统计逆序对数量
    解法：归并排序统计逆序对

这些题目虽然具体形式不同，但核心思想都是利用归并排序的分治特性，在合并过程中高效统计满足特定条件的元素对数量。
'''


def count_smaller(nums):
    """
    计算右侧小于当前元素的个数 - Python实现
    
    Args:
        nums: 输入整数数组
        
    Returns:
        list: 包含每个元素右侧小于它的元素个数的列表
        
    算法思路:
    使用归并排序的思想，在合并两个有序子数组的过程中统计右侧小于当前元素的个数。
    关键是维护原始索引信息，以便在排序后仍能正确更新结果数组。
    
    时间复杂度: O(n log n)
    空间复杂度: O(n)
    """
    n = len(nums)
    if n == 0:
        return []
    
    # 创建索引数组，保存值和原始索引
    indexed_nums = [(nums[i], i) for i in range(n)]
    
    # 结果数组，存储每个元素右侧小于它的元素个数
    result = [0] * n
    
    # 辅助数组，用于归并过程
    temp = [None] * n
    
    def merge_sort(left, right):
        """
        归并排序的核心方法
        
        Args:
            left: 当前处理区间的左边界
            right: 当前处理区间的右边界
        """
        # 基本情况：区间只有一个元素时直接返回
        if left == right:
            return
        
        # 计算中间位置
        mid = left + (right - left) // 2
        
        # 分治：递归处理左右子区间
        merge_sort(left, mid)
        merge_sort(mid + 1, right)
        
        # 合并两个有序子区间，同时统计结果
        merge(left, mid, right)
    
    def merge(left, mid, right):
        """
        合并两个有序子数组，并在合并过程中统计右侧小于当前元素的个数
        
        Args:
            left: 当前处理区间的左边界
            mid: 当前处理区间的中点
            right: 当前处理区间的右边界
        """
        i = left   # temp数组的指针
        a = left   # 左侧子数组的指针
        b = mid + 1  # 右侧子数组的指针
        
        # 合并两个子数组，同时统计右侧小于当前元素的个数
        while a <= mid and b <= right:
            if indexed_nums[a][0] <= indexed_nums[b][0]:
                # 当左侧元素小于等于右侧元素时，右侧数组中已经处理的元素都小于当前左侧元素
                # 统计右侧已处理的元素数量：b - (mid + 1) = b - mid - 1
                result[indexed_nums[a][1]] += (b - mid - 1)
                temp[i] = indexed_nums[a]
                a += 1
            else:
                # 当右侧元素小于左侧元素时，将右侧元素放入辅助数组
                # 此时不更新计数，因为左侧元素还未被处理
                temp[i] = indexed_nums[b]
                b += 1
            i += 1
        
        # 处理左侧剩余元素
        while a <= mid:
            # 左侧剩余元素的右侧所有元素都小于它
            result[indexed_nums[a][1]] += (b - mid - 1)
            temp[i] = indexed_nums[a]
            a += 1
            i += 1
        
        # 处理右侧剩余元素
        while b <= right:
            temp[i] = indexed_nums[b]
            b += 1
            i += 1
        
        # 将辅助数组内容复制回原数组
        for i in range(left, right + 1):
            if temp[i] is not None:
                indexed_nums[i] = temp[i]

    # 执行归并排序并统计
    merge_sort(0, n - 1)
    
    return result


def main():
    """
    主函数，用于测试
    """
    import sys
    # 调整递归深度限制以处理大规模数据
    sys.setrecursionlimit(1000000)
    
    # 测试用例1: 基本情况
    test1 = [5, 2, 6, 1]
    print("输入:", test1)
    print("输出:", count_smaller(test1))  # 预期输出: [2, 1, 1, 0]
    
    # 测试用例2: 空数组
    test2 = []
    print("输入:", test2)
    print("输出:", count_smaller(test2))  # 预期输出: []
    
    # 测试用例3: 单元素数组
    test3 = [1]
    print("输入:", test3)
    print("输出:", count_smaller(test3))  # 预期输出: [0]
    
    # 测试用例4: 逆序数组
    test4 = [5, 4, 3, 2, 1]
    print("输入:", test4)
    print("输出:", count_smaller(test4))  # 预期输出: [4, 3, 2, 1, 0]
    
    # 测试用例5: 有序数组
    test5 = [1, 2, 3, 4, 5]
    print("输入:", test5)
    print("输出:", count_smaller(test5))  # 预期输出: [0, 0, 0, 0, 0]
    
    # 测试用例6: 重复元素
    test6 = [2, 2, 2]
    print("输入:", test6)
    print("输出:", count_smaller(test6))  # 预期输出: [0, 0, 0]
    
    # 测试用例7: 包含负数
    test7 = [-1, -2, 3, -4, 5]
    print("输入:", test7)
    print("输出:", count_smaller(test7))  # 预期输出: [2, 1, 1, 0, 0]


# ============================================================================
# Python语言特有关注事项
# ============================================================================
#
# 1. 递归深度限制:
#    - Python默认递归深度限制约为1000层
#    - 对于大规模数据，使用sys.setrecursionlimit(1000000)设置更大的限制
#
# 2. 整数精度:
#    - Python的整数类型自动支持大整数，不会有溢出问题
#
# 3. 列表操作效率:
#    - 使用索引操作而非切片，避免创建新列表的开销
#    - 预先创建辅助数组，避免在递归中重复创建
#
# 4. 内存管理:
#    - Python自动管理内存，但频繁创建对象会增加GC压力
#    - 使用None初始化temp数组，避免不必要的对象创建
#
# ============================================================================
# 工程化考量
# ============================================================================
#
# 1. 性能优化:
#    - 对于小规模子数组(如n<10)，可考虑使用插入排序
#    - 可添加判断：当indexed_nums[mid][0] <= indexed_nums[mid+1][0]时，子数组已有序，可跳过合并
#
# 2. 错误处理:
#    - 可添加输入验证，检查输入是否为列表
#    - 可添加异常处理，处理输入格式错误
#
# 3. 可扩展性:
#    - 算法易于扩展到其他统计问题
#    - 可封装为模块供其他程序调用
#
# 4. 测试策略:
#    - 已提供多种测试用例，覆盖常见场景
#    - 可以添加性能测试，验证算法在大规模数据下的表现

if __name__ == "__main__":
    main()