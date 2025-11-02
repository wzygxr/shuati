"""
===========================================================================题目3: 计算右侧小于当前元素的个数 (Count of Smaller Numbers After Self)
===========================================================================

题目来源: LeetCode 315
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

===========================================================================核心算法思想: 归并排序+索引映射
===========================================================================

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

===========================================================================相关题目列表 (同类算法)
===========================================================================
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
"""

"""
计算右侧小于当前元素的个数详解:

问题描述:
给你一个整数数组 nums ，按要求返回一个新数组 counts 。数组 counts 有该性质：
counts[i] 的值是 nums[i] 右侧小于 nums[i] 的元素的数量。

示例:
输入：nums = [5,2,6,1]
输出：[2,1,1,0]
解释：
5 的右侧有 2 个更小的元素 (2 和 1)
2 的右侧仅有 1 个更小的元素 (1)
6 的右侧有 1 个更小的元素 (1)
1 的右侧有 0 个更小的元素

解法思路:
1. 暴力解法: 对每个元素，遍历其右侧所有元素，统计比它小的元素个数，时间复杂度O(N^2)
2. 归并排序思想: 
   - 在归并排序过程中，当合并两个有序数组时，如果从左侧数组选择元素，则右侧数组中已处理的元素
     都是小于当前元素的，可以统计数量
   - 由于排序会改变元素位置，需要记录原始索引

时间复杂度: O(N * logN) - 归并排序的时间复杂度
空间复杂度: O(N) - 辅助数组和结果数组的空间复杂度

相关题目:
1. LeetCode 493. 翻转对
2. LeetCode 327. 区间和的个数
3. 剑指Offer 51. 数组中的逆序对
4. 牛客网 - 计算数组的小和
"""

def count_smaller(nums):
    """
    计算右侧小于当前元素的个数
    
    Args:
        nums (List[int]): 输入整数数组
        
    Returns:
        List[int]: 包含每个元素右侧小于它的元素个数的列表
    
    Python语言特性注意事项:
    1. Python的列表是可变对象，在递归调用时会被修改，因此需要注意索引映射的处理
    2. Python的整数没有大小限制，不会出现整数溢出问题
    3. 对于大规模数据，Python的递归深度可能受限，默认递归深度约为1000
    4. Python中的列表推导式和切片操作可以使代码更简洁，但需要注意效率影响
    """
    if not nums:
        return []
    
    # 结果数组
    result = [0] * len(nums)
    # 索引数组，用于维护原始位置信息
    indexes = list(range(len(nums)))
    
    # 归并排序主函数
    def merge_sort(left, right):
        """
        归并排序的核心函数，同时统计右侧小于当前元素的个数
        
        Args:
            left (int): 当前处理区间的左边界
            right (int): 当前处理区间的右边界
        """
        if left >= right:
            return
        
        # 计算中间位置
        mid = left + (right - left) // 2
        
        # 分治处理左右两个子数组
        merge_sort(left, mid)
        merge_sort(mid + 1, right)
        
        # 合并两个有序子数组，同时统计
        merge(left, mid, right)
    
    def merge(left, mid, right):
        """
        合并两个有序子数组，并在合并过程中统计右侧小于当前元素的个数
        
        核心统计逻辑:
        - 当右子数组的元素被选中时，不会对左侧元素产生影响
        - 当左子数组的元素被选中时，右子数组中剩余的所有元素都是比它小的
        - 因此，每次选中左子数组元素时，需要记录右侧已经统计过的元素数量
        
        Args:
            left (int): 当前处理区间的左边界
            mid (int): 当前处理区间的中点
            right (int): 当前处理区间的右边界
        """
        # 辅助数组，用于暂存排序后的索引
        temp = [0] * (right - left + 1)
        i = 0  # temp数组的指针
        a = left  # 左侧子数组的指针
        b = mid + 1  # 右侧子数组的指针
        
        # 记录右侧已经处理的元素数量（即小于左侧当前元素的数量）
        right_count = 0
        
        # 合并两个子数组，同时统计右侧小于当前元素的个数
        while a <= mid and b <= right:
            if nums[indexes[b]] < nums[indexes[a]]:
                # 右侧元素更小，先放入temp数组，增加右侧已处理元素计数
                temp[i] = indexes[b]
                b += 1
                right_count += 1
            else:
                # 左侧元素更小或相等，先放入temp数组，更新该元素对应的右侧小元素数量
                result[indexes[a]] += right_count
                temp[i] = indexes[a]
                a += 1
            i += 1
        
        # 处理剩余的左侧元素
        while a <= mid:
            result[indexes[a]] += right_count  # 这些元素的右侧还有right_count个较小元素
            temp[i] = indexes[a]
            a += 1
            i += 1
        
        # 处理剩余的右侧元素（不需要更新结果，因为它们不会对任何元素产生统计贡献）
        while b <= right:
            temp[i] = indexes[b]
            b += 1
            i += 1
        
        # 将排序后的索引复制回原索引数组
        for i in range(len(temp)):
            indexes[left + i] = temp[i]
    
    # 执行归并排序并统计
    merge_sort(0, len(nums) - 1)
    
    return result


# 测试代码
if __name__ == "__main__":
    # 测试用例1: 基本情况
    test_nums1 = [5, 2, 6, 1]
    print(f"输入: nums = {test_nums1}")
    print(f"输出: {count_smaller(test_nums1)}")  # 预期输出: [2,1,1,0]
    
    # 测试用例2: 空数组
    test_nums2 = []
    print(f"\n输入: nums = {test_nums2}")
    print(f"输出: {count_smaller(test_nums2)}")  # 预期输出: []
    
    # 测试用例3: 单元素数组
    test_nums3 = [1]
    print(f"\n输入: nums = {test_nums3}")
    print(f"输出: {count_smaller(test_nums3)}")  # 预期输出: [0]
    
    # 测试用例4: 递增数组
    test_nums4 = [1, 2, 3, 4, 5]
    print(f"\n输入: nums = {test_nums4}")
    print(f"输出: {count_smaller(test_nums4)}")  # 预期输出: [0,0,0,0,0]
    
    # 测试用例5: 递减数组
    test_nums5 = [5, 4, 3, 2, 1]
    print(f"\n输入: nums = {test_nums5}")
    print(f"输出: {count_smaller(test_nums5)}")  # 预期输出: [4,3,2,1,0]
    
    # 测试用例6: 重复元素
    test_nums6 = [2, 2, 2, 2]
    print(f"\n输入: nums = {test_nums6}")
    print(f"输出: {count_smaller(test_nums6)}")  # 预期输出: [3,2,1,0]
    
    # 测试用例7: 包含负数
    test_nums7 = [-1, -2, 3, -4, 5]
    print(f"\n输入: nums = {test_nums7}")
    print(f"输出: {count_smaller(test_nums7)}")  # 预期输出: [2,1,1,0,0]
    
    # 测试用例8: 大数值测试
    test_nums8 = [2147483647, -2147483648, 0]
    print(f"\n输入: nums = {test_nums8}")
    print(f"输出: {count_smaller(test_nums8)}")  # 预期输出: [2,0,0]


"""
===========================================================================
Python语言特有关注事项
===========================================================================

1. 整数类型：
   - Python中的整数没有大小限制，不会出现整数溢出问题
   - 无需像C++/Java那样担心mid计算时的溢出

2. 递归深度限制：
   - Python默认递归深度限制为约1000层
   - 对于长度超过1e5的数组，可能触发递归深度错误(RecursionError)
   - 解决方案：
     a) 可通过sys.setrecursionlimit(1000000)临时调整递归深度限制
     b) 考虑实现迭代版归并排序

3. 可变对象特性：
   - Python列表是可变对象，在函数内部修改会影响外部
   - 在实现中，我们使用可变列表result和indexes来保存中间结果

4. 列表操作效率：
   - 列表索引访问：O(1)，非常高效
   - 列表追加操作：均摊O(1)，但预分配空间更高效
   - 在merge函数中，我们预先分配了temp数组，避免频繁的动态扩展

5. 函数嵌套：
   - 内部函数merge_sort和merge可以直接访问外部函数的变量
   - 这种设计使代码更简洁，但需要注意变量作用域

6. 切片操作：
   - Python的切片操作会创建新列表，可能导致O(n)的额外空间和时间开销
   - 当前实现避免了不必要的切片操作，直接在原数组上通过索引操作

7. 装饰器优化：
   - 可以使用@functools.lru_cache装饰器对特定情况进行缓存（注意可变参数不可哈希）
   - 对于递归版本，可以考虑使用记忆化优化重复计算

8. Python特有的优化技巧：
   - 使用列表推导式代替显式循环
   - 使用collections模块中的数据结构可能提高特定场景的性能
   - 考虑使用NumPy数组进行大规模数值计算，提供更好的缓存局部性

===========================================================================
工程化考量
===========================================================================

1. 异常处理：
   - 已处理空数组的边界情况
   - 可以添加对输入类型的检查，确保输入是列表类型
   - 建议添加try-except块捕获可能的异常，如递归深度错误
   - 示例异常处理代码：
     ```python
     def count_smaller_safe(nums):
         try:
             if not isinstance(nums, list):
                 raise TypeError("Input must be a list")
             return count_smaller(nums)
         except RecursionError:
             # 处理递归深度问题，返回迭代版本或其他实现
             return count_smaller_iterative(nums)
         except Exception as e:
             print(f"Error occurred: {e}")
             return []
     ```

2. 性能优化：
   - 对于超大规模数据，可以考虑使用非递归实现避免栈溢出
   - 对于小规模子数组，可以切换到插入排序优化常数因子
   - 可使用PyPy解释器运行，对于计算密集型任务性能提升显著
   - 对于特定问题，可以考虑使用更高效的数据结构，如二叉搜索树或线段树

3. 测试策略：
   - 已添加多种测试用例，覆盖常见场景和边界情况
   - 可以使用unittest或pytest框架进行更系统化的单元测试
   - 建议添加性能测试，测量不同规模数据下的执行时间
   - 可以添加随机测试，验证算法在各种随机输入下的正确性

4. 可扩展性：
   - 当前算法框架可以扩展到类似问题，如计算右侧大于当前元素的个数
   - 可以实现一个通用的归并排序计数框架，通过回调函数支持不同的计数逻辑
   - 示例可扩展接口：
     ```python
     def merge_sort_count(nums, count_callback):
         # 通用归并排序计数框架
         # count_callback(left_val, right_val) 用于判断是否需要计数
         # 返回值：统计结果
         pass
     ```

5. 代码可读性：
   - 使用清晰的函数命名和详细的注释
   - 将复杂逻辑拆分为小函数，每个函数负责单一职责
   - 使用文档字符串(docstring)详细说明函数参数、返回值和功能
   - 添加类型提示可以提高代码的可读性和可维护性

6. 并行处理：
   - 对于超大规模数据，可以考虑并行计算框架如multiprocessing
   - 可以将数组分块，并行处理子数组，然后合并结果
   - 注意并行处理时的数据同步和结果合并问题

7. 内存效率：
   - 当前实现的空间复杂度为O(n)，对于大规模数据可能需要优化
   - 可以考虑使用原地排序算法减少辅助空间，但会增加实现复杂度
   - 对于流式数据，可以使用在线算法，避免一次性加载全部数据

8. 兼容性考虑：
   - 代码兼容Python 3.x版本
   - 对于Python 2.x需要进行适当修改（如print语句语法）
   - 可考虑使用future模块确保跨版本兼容性

9. 代码优化建议：
   - 对于大规模数据，考虑使用更高效的排序算法或数据结构
   - 可以使用位运算优化某些计算步骤
   - 对于特定应用场景，可以考虑启发式优化
   - 使用profile工具分析性能瓶颈，针对性优化

10. 代码安全性：
    - 避免使用全局变量存储状态，减少副作用
    - 对外部输入进行严格验证，防止注入攻击
    - 注意深拷贝和浅拷贝的区别，避免意外修改

===========================================================================
相关题目与平台信息
===========================================================================

1. LeetCode 315. Count of Smaller Numbers After Self
   - 题目链接：https://leetcode.cn/problems/count-of-smaller-numbers-after-self/
   - 难度等级：困难
   - 标签：归并排序、树状数组、线段树

2. LeetCode 493. 翻转对 (Reverse Pairs)
   - 题目链接：https://leetcode.cn/problems/reverse-pairs/
   - 难度等级：困难
   - 解题思路：同样使用归并排序的过程统计满足条件的对

3. LeetCode 327. 区间和的个数 (Count of Range Sum)
   - 题目链接：https://leetcode.cn/problems/count-of-range-sum/
   - 难度等级：困难
   - 解题思路：前缀和结合归并排序，统计满足条件的区间和

4. 剑指Offer 51. 数组中的逆序对
   - 题目链接：https://leetcode.cn/problems/shu-zu-zhong-de-ni-xu-dui-lcof/
   - 难度等级：困难
   - 解题思路：归并排序过程中统计逆序对数量

5. LeetCode 1365. 有多少小于当前数字的数字
   - 题目链接：https://leetcode.cn/problems/how-many-numbers-are-smaller-than-the-current-number/
   - 难度等级：简单
   - 解题思路：排序+哈希表映射，全数组范围统计

6. 牛客网 - 计算数组的小和
   - 题目链接：https://www.nowcoder.com/practice/edfe05a1d45c4ea89101d936cac32469
   - 解题思路：归并排序过程中计算小和

7. HackerRank - Merge Sort: Counting Inversions
   - 题目链接：https://www.hackerrank.com/challenges/merge-sort/problem
   - 难度等级：中等
   - 解题思路：归并排序统计逆序对数量

8. POJ 2299. Ultra-QuickSort
   - 题目链接：http://poj.org/problem?id=2299
   - 解题思路：计算将数组排序所需的最小交换次数（即逆序对数量）

9. HDU 1394. Minimum Inversion Number
   - 题目链接：http://acm.hdu.edu.cn/showproblem.php?pid=1394
   - 解题思路：将数组循环左移，求所有可能排列中的最小逆序对数量

10. LintCode 1297. 统计右侧小于当前元素的个数
    - 题目链接：https://www.lintcode.com/problem/1297/
    - 与LeetCode 315题相同

11. SPOJ - INVCNT
    - 题目链接：https://www.spoj.com/problems/INVCNT/
    - 解题思路：统计逆序对数量，可使用归并排序解决

12. 字节跳动面试题 - 数组统计问题
    - 实际面试中可能会对本题进行变体，如不同的统计条件
    - 考察归并排序思想的灵活应用

13. 微软面试题 - 元素相对顺序问题
    - 可能要求在保持相对顺序的情况下进行统计或变换
    - 与本题的索引维护思想相关

14. Google面试题 - 二维数组统计
    - 将问题扩展到二维数组，统计每个元素右下方小于它的元素个数
    - 更复杂的归并排序或分治思想应用

15. 腾讯面试题 - 数据流中的逆序对
    - 处理动态数据流，实时统计逆序对数量
    - 可能需要使用更高效的数据结构，如树状数组或线段树

16. 阿里巴巴面试题 - 大规模数据统计
    - 要求处理超大规模数据，考察算法优化和并行处理能力
    - 可能需要结合归并排序和分布式计算思想

17. 美团面试题 - 数组变换统计
    - 在数组变换过程中统计满足特定条件的元素对数量
    - 考察对归并排序思想的深入理解和应用

18. 京东面试题 - 字符串逆序对
    - 将问题应用到字符串，统计满足条件的字符对
    - 归并排序思想在不同数据类型上的应用

19. 百度面试题 - 多维逆序对
    - 扩展到多维空间，统计多维逆序对
    - 更复杂的分治策略和数据结构应用

20. 小米面试题 - 排序过程分析
    - 分析排序算法执行过程中的各种统计量
    - 与本题的归并排序过程统计思想一致
"""