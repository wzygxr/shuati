"""
Python版本基数排序实现

基数排序是一种非比较型整数排序算法，其原理是将整数按位数切割成不同的数字，
然后按每个位数分别比较。由于整数也可以表达字符串（比如名字或日期）和特定格式的浮点数，
所以基数排序也不是只能使用于整数。

基数排序有两种方法：
1. MSD（Most Significant Digit First）从高位开始进行排序
2. LSD（Least Significant Digit First）从低位开始进行排序

本实现使用LSD方法，适用于位数较少的整数排序。

LSD（从低位到高位）排序方法的适用场景：
- 当数据范围较大但位数较小时（如电话号码排序）
- 需要稳定排序的场景
- 当需要线性时间复杂度的排序算法时
- 对于大规模数据，如果数据范围不是很大，效率优于基于比较的排序算法

调试技巧：
1. 打印中间过程：在每轮排序后打印数组内容，观察排序过程
2. 检查计数数组：验证计数数组和前缀和的正确性
3. 验证稳定性：确保相等元素的相对顺序保持不变
4. 负数处理：验证偏移量计算和恢复是否正确

相关题目扩展（全平台覆盖）：

1. LeetCode 912. 排序数组
   链接：https://leetcode.cn/problems/sort-an-array/
   描述：给你一个整数数组 nums，请你将该数组升序排列。
   解法：基数排序，时间复杂度O(d*(n+k))，空间复杂度O(n+k)
   为什么最优：对于大规模整数数组，基数排序效率高于基于比较的排序算法

2. LeetCode 164. 最大间距
   链接：https://leetcode.cn/problems/maximum-gap/
   描述：给定一个无序的数组 nums，返回数组在排序之后，相邻元素之间最大的差值。
   要求：必须编写一个在「线性时间」内运行并使用「线性额外空间」的算法。
   解法：基数排序可以在O(n)时间内完成排序，然后遍历找出最大间距
   为什么最优：基于比较的排序无法达到低于O(nlogn)的时间复杂度

3. LeetCode 2343. 裁剪数字后查询第K小的数字
   链接：https://leetcode.cn/problems/query-kth-smallest-trimmed-number/
   描述：裁剪数字后查询第K小的数字
   解法：使用基数排序对裁剪后的数字进行高效排序

4. 洛谷 P1177 【模板】排序
   链接：https://www.luogu.com.cn/problem/P1177
   描述：将读入的N个数从小到大排序后输出。
   解法：基数排序是此题的高效解法之一，特别适合大规模整数数据

5. 计蒜客 - 整数排序
   链接：https://nanti.jisuanke.com/t/40256
   描述：给定一个包含N个整数的数组，将它们按升序排列后输出。
   解法：基数排序可以在O(d*(n+k))时间内完成排序，对于大规模数据效率高

6. HackerRank - Counting Sort 3
   链接：https://www.hackerrank.com/challenges/countingsort3/problem
   描述：使用计数排序的变种解决统计排序问题
   解法：基数排序的基础是计数排序，可以灵活应用于此类问题

7. Codeforces - Sort the Array
   链接：https://codeforces.com/problemset/problem/451/B
   描述：判断是否可以通过反转一个子数组使得整个数组有序
   解法：使用基数排序进行排序，然后比较确定是否满足条件

8. 牛客 - 数组排序
   链接：https://www.nowcoder.com/practice/2baf799ea0594abd974d37139de27896
   描述：对数组进行排序并输出
   解法：基数排序是高效解法之一，特别适合整数数组

9. HDU 1051. Wooden Sticks
   链接：http://acm.hdu.edu.cn/showproblem.php?pid=1051
   描述：贪心问题，需要先对木棍进行排序
   解法：使用基数排序可以高效排序，然后应用贪心策略

10. POJ 3664. Election Time
    链接：http://poj.org/problem?id=3664
    描述：选举问题，涉及对投票结果的排序
    解法：基数排序可以高效处理大量整数排序，适用于统计类问题

11. UVa 11462. Age Sort
    链接：https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=2457
    描述：对年龄进行排序，数据量很大
    解法：基数排序非常适合处理大规模整数数据，时间复杂度接近线性

12. USACO 2018 December Platinum - Sort It Out
    题目类型：最长递增子序列问题结合基数排序优化
    解法：使用O(N*logN)的LIS算法，结合基数排序进行优化

13. USACO 2018 Open Gold - OutOf Sorts
    题目类型：模拟优化问题，涉及排序算法分析
    解法：分析冒泡排序的优化版本，使用基数排序验证结果

14. SPOJ - MSORT
    链接：https://www.spoj.com/problems/MSORT/
    描述：高效排序大数据
    解法：基数排序是处理大规模数据的理想选择

15. CodeChef - MAX_DIFF
    链接：https://www.codechef.com/problems/MAX_DIFF
    描述：排序后计算最大差值
    解法：使用基数排序高效排序，然后计算差值
"""

class RadixSort:
    BASE = 10  # 基数

    @staticmethod
    def sort_array(arr):
        """
        主排序函数，对整数数组进行升序排序

        算法步骤：
        1. 处理边界情况：数组长度小于等于1时直接返回
        2. 处理负数：找到数组中的最小值，将所有元素减去最小值转换为非负数
        3. 计算最大值的位数，确定排序轮数
        4. 执行基数排序
        5. 还原数组元素（加上之前减去的最小值）

        时间复杂度分析：
        1. 找最小值和最大值：O(n)
        2. 偏移处理：O(n)
        3. 基数排序：O(d*(n+k))，其中d是位数，k是基数
        4. 还原处理：O(n)
        总时间复杂度：O(d*(n+k))

        空间复杂度分析：
        1. 辅助数组help：O(n)
        2. 计数数组cnts：O(k)
        总空间复杂度：O(n+k)

        :param arr: 待排序的整数数组
        :return: 排序后的整数数组
        """
        if len(arr) <= 1:
            return arr

        n = len(arr)

        # 找到数组中的最小值
        min_val = arr[0]
        for i in range(1, n):
            min_val = min(min_val, arr[i])

        max_val = 0
        for i in range(n):
            # 数组中的每个数字，减去数组中的最小值，就把arr转成了非负数组
            # 这是处理负数的关键技巧：通过偏移将负数转换为非负数
            arr[i] -= min_val
            # 记录数组中的最大值
            max_val = max(max_val, arr[i])

        # 根据最大值在BASE进制下的位数，决定基数排序做多少轮
        RadixSort.radix_sort(arr, n, RadixSort.bits(max_val))

        # 数组中所有数都减去了最小值，所以最后不要忘了还原
        for i in range(n):
            arr[i] += min_val

        return arr

    @staticmethod
    def bits(number):
        """
        计算数字在BASE进制下的位数

        :param number: 输入数字
        :return: 该数字在BASE进制下的位数
        """
        ans = 0
        while number > 0:
            ans += 1
            number //= RadixSort.BASE
        return ans

    @staticmethod
    def radix_sort(arr, n, bits):
        """
        基数排序核心代码

        算法原理：
        1. 从最低位开始，对每一位进行计数排序
        2. 使用计数排序保证稳定性
        3. 重复此过程直到最高位

        :param arr: 待排序数组
        :param n: 数组长度
        :param bits: arr中最大值在BASE进制下有几位
        """
        # 辅助数组
        help_arr = [0] * n
        # 计数数组
        cnts = [0] * RadixSort.BASE

        # 理解的时候可以假设BASE = 10
        offset = 1
        while bits > 0:
            # 每一轮开始前清空计数数组
            for i in range(RadixSort.BASE):
                cnts[i] = 0

            # 统计当前位上各数字的出现次数
            # (arr[i] // offset) % BASE 是提取当前位数字的技巧
            for i in range(n):
                cnts[(arr[i] // offset) % RadixSort.BASE] += 1

            # 处理成前缀次数累加的形式
            for i in range(1, RadixSort.BASE):
                cnts[i] = cnts[i] + cnts[i - 1]

            # 从后向前遍历，保证排序的稳定性
            for i in range(n - 1, -1, -1):
                help_arr[cnts[(arr[i] // offset) % RadixSort.BASE] - 1] = arr[i]
                cnts[(arr[i] // offset) % RadixSort.BASE] -= 1

            # 将排序结果复制回原数组
            for i in range(n):
                arr[i] = help_arr[i]

            offset *= RadixSort.BASE
            bits -= 1

    @staticmethod
    def maximum_gap(nums):
        """
        LeetCode 164. 最大间距

        题目链接：https://leetcode.cn/problems/maximum-gap/

        题目描述：
        给定一个无序的数组 nums，返回数组在排序之后，相邻元素之间最大的差值。
        如果数组元素个数小于 2，则返回 0。
        要求：必须编写一个在「线性时间」内运行并使用「线性额外空间」的算法。

        解题思路：
        1. 使用基数排序在O(n)时间内完成排序
        2. 遍历排序后的数组，计算相邻元素之间的差值，找出最大值

        为什么基数排序是最优解：
        - 基于比较的排序算法最快只能达到O(nlogn)时间复杂度
        - 基数排序可以在线性时间内完成排序，符合题目的时间复杂度要求
        - 对于大规模数据，当数据范围不是特别大时，基数排序效率更高

        时间复杂度：O(d*(n+k))，其中d是位数，n是数组长度，k是基数
        空间复杂度：O(n+k)

        :param nums: 输入数组
        :return: 排序后相邻元素之间的最大差值
        """
        # 处理边界情况
        if len(nums) < 2:
            return 0

        # 创建数组副本以避免修改原数组
        nums_copy = nums.copy()
        
        # 使用基数排序对数组进行排序
        RadixSort.sort_array(nums_copy)

        # 遍历排序后的数组，找出相邻元素之间的最大差值
        max_gap = 0
        for i in range(1, len(nums_copy)):
            current_gap = nums_copy[i] - nums_copy[i - 1]
            if current_gap > max_gap:
                max_gap = current_gap

        return max_gap
    
    @staticmethod
    def smallest_trimmed_numbers(nums, queries):
        """
        LeetCode 2343. 裁剪数字后查询第K小的数字
        
        题目链接：https://leetcode.cn/problems/query-kth-smallest-trimmed-number/
        
        题目描述：
        给你一个下标从0开始的字符串数组nums，其中每个字符串长度相等且只包含数字。
        对于每个查询，你需要将nums中的每个数字裁剪到剩下最右边trimi个数位。
        在裁剪过后的数字中，找到nums中第ki小数字对应的下标。
        
        解题思路：
        1. 对于每个查询，提取裁剪后的数字
        2. 使用基数排序对裁剪后的数字进行排序，保留原始下标
        3. 返回第k小数字的原始下标
        
        为什么使用基数排序：
        - 数字长度固定，非常适合基数排序
        - 基数排序的稳定性保证了在相等情况下保持原始顺序
        - 对于每个查询，只需要从最低位到最高位排序，效率高
        
        时间复杂度：O(q * (m * n))，其中q是查询次数，m是数字长度，n是数组长度
        空间复杂度：O(n)
        
        :param nums: 字符串形式的数字数组
        :param queries: 查询数组，每个查询包含k和trim
        :return: 每个查询的结果数组
        """
        # 边界情况处理
        if not nums or not queries:
            return []
        
        m = len(nums)
        result = []
        
        # 对每个查询进行处理
        for k, trim in queries:
            # 提取原始下标
            indices = list(range(m))
            
            # 进行基数排序
            len_num = len(nums[0])
            start_pos = len_num - trim
            temp = [0] * m
            count = [0] * 10  # 0-9数字的计数数组
            
            # 从最低位到最高位进行排序
            for pos in range(len_num - 1, start_pos - 1, -1):
                # 清空计数数组
                count = [0] * 10
                
                # 统计当前位的数字出现次数
                for idx in indices:
                    digit = int(nums[idx][pos])
                    count[digit] += 1
                
                # 计算前缀和
                for i in range(1, 10):
                    count[i] += count[i - 1]
                
                # 从后向前放置元素，保证稳定性
                for i in range(m - 1, -1, -1):
                    idx = indices[i]
                    digit = int(nums[idx][pos])
                    count[digit] -= 1
                    temp[count[digit]] = idx
                
                # 复制回原数组
                indices = temp.copy()
            
            # 保存第k小的元素下标（注意下标从0开始）
            result.append(indices[k - 1])
        
        return result


# 测试函数
if __name__ == "__main__":
    print("======= 基数排序基本功能测试 =======\n")
    
    # 测试用例1：正常数组
    arr1 = [5, 2, 3, 1]
    print("测试用例1: 正常数组")
    print("排序前:", arr1)
    RadixSort.sort_array(arr1)
    print("排序后:", arr1)
    print()
    
    # 测试用例2：包含负数的数组
    arr2 = [-5, 2, -3, 1, 0]
    print("测试用例2: 包含负数的数组")
    print("排序前:", arr2)
    RadixSort.sort_array(arr2)
    print("排序后:", arr2)
    print()
    
    # 测试用例3：较大数字
    arr3 = [10000, 1000, 100, 10, 1]
    print("测试用例3: 较大数字")
    print("排序前:", arr3)
    RadixSort.sort_array(arr3)
    print("排序后:", arr3)
    print()
    
    print("======= LeetCode 164. 最大间距测试 =======\n")
    # 测试最大间距
    arr4 = [3, 6, 9, 1]
    print("数组:", arr4)
    max_gap = RadixSort.maximum_gap(arr4)
    print("最大间距:", max_gap)  # 应输出 3
    print()
    
    arr5 = [10]
    print("数组:", arr5)
    max_gap = RadixSort.maximum_gap(arr5)
    print("最大间距:", max_gap)  # 应输出 0
    print()
    
    print("======= LeetCode 2343. 裁剪数字后查询第K小的数字测试 =======\n")
    # 测试裁剪数字
    nums = ["102", "473", "251", "814"]
    queries = [[1, 1], [2, 3], [4, 2], [1, 2]]
    print("nums:", nums)
    print("queries:", queries)
    result = RadixSort.smallest_trimmed_numbers(nums, queries)
    print("结果:", result)  # 应输出 [2, 2, 1, 0]

"""
基数排序算法优化技巧：

1. 基数选择优化
   - 选择合适的基数（如256或1024）可以减少排序轮数
   - 对于大多数场景，BASE=10是平衡的选择
   - 使用2的幂作为基数可以利用位运算提高效率（例如：(num >> 8) & 0xFF）
   - 对于GPU并行处理，可以选择更大的基数以提高并行度

2. 内存使用优化
   - 可以复用辅助数组以减少内存分配开销
   - 对于特定场景，可以使用原地基数排序
   - 使用缓冲区交换技术避免重复复制
   - 对于大规模数据，可以采用外部排序思想，分批处理

3. 性能优化
   - 对于已经排序的位，可以提前终止排序过程
   - 使用并行计算处理不同的位（多线程或GPU加速）
   - 预分配内存避免动态扩容
   - 使用SIMD指令集优化数据并行处理（在Python中可以通过NumPy实现）
   - 缓存优化：按照数据局部性原则重新组织数据访问模式

4. 特殊数据处理
   - 对于稀疏数据，可以先进行压缩
   - 对于大量重复数据，可以先进行去重
   - 对于极长的数字，可以使用分段处理
   - 对于不同范围的数据，可以采用混合排序策略

5. 负数处理优化
   - 可以使用符号位分离的方式处理负数
   - 对于有符号整数，可以使用补码表示直接处理
   - 当数据范围对称时，可以使用偏移到无符号范围的方法

工程化考量：

1. 异常处理与健壮性
   - 处理空数组和单元素数组
   - 验证输入数据的有效性
   - 处理可能的溢出情况（在Python中整数没有大小限制，这是Python的优势）
   - 添加适当的错误提示和日志记录

2. 线程安全性
   - 当前实现不是线程安全的
   - 在多线程环境中使用时需要添加同步机制
   - 可以使用ThreadLocal变量避免线程安全问题

3. 可扩展性
   - 设计灵活的接口，支持不同的基数和数据类型
   - 提供参数配置选项，允许用户根据具体场景调整算法参数
   - 支持自定义排序策略

4. 文档化
   - 提供详细的API文档
   - 编写使用示例和测试用例
   - 记录算法的性能特性和限制

5. 单元测试
   - 编写全面的单元测试覆盖各种情况
   - 测试边界条件和异常输入
   - 实现性能测试，监控算法在不同数据规模下的表现

与标准库实现对比：

1. 与Python内置sorted()函数的对比
   - Python的sorted()函数使用Timsort算法，这是一种结合了归并排序和插入排序的混合排序算法
   - 对于一般数据，sorted()函数通常更快，因为它是经过高度优化的C实现
   - 对于特定场景（如大规模整数排序），基数排序可能更有优势
   - 基数排序是稳定的排序算法，而Timsort也是稳定的

2. 标准库的边界处理
   - Python的sorted()函数可以处理各种数据类型，而基数排序主要用于整数
   - 标准库实现了更多的边界情况检查和错误处理
   - 标准库的性能通常更好，因为它使用了更低级别的优化

跨语言实现差异：

1. Python vs Java实现
   - Python代码更简洁，可读性更好
   - Java的性能通常更高，尤其是对于大规模数据
   - Java需要处理整数溢出问题，而Python不需要
   - Python的列表操作比Java的数组操作更灵活，但通常也更慢

2. Python vs C++实现
   - C++可以更好地控制内存分配和释放
   - C++的性能通常显著高于Python
   - Python的自动垃圾回收简化了内存管理，但可能影响性能
   - C++可以使用更多的底层优化技术，如SIMD指令

3. Python特有优化
   - 使用PyPy代替CPython可以显著提高性能
   - 使用NumPy进行数组操作可以提高计算效率
   - 使用Cython编写关键部分可以获得接近C的性能
   - 使用multiprocessing模块进行并行计算

极端场景测试：

1. 空数组：直接返回原数组
2. 单元素数组：直接返回原数组
3. 包含相同元素的数组：验证稳定性
4. 完全有序数组：测试算法在已有序情况下的性能
5. 完全逆序数组：测试最坏情况下的性能
6. 包含极大值和极小值的数组：验证偏移量计算的正确性
7. 大规模数据：测试算法的可扩展性

面试技巧与常见问题：

1. 基数排序与比较排序的区别
   - 基数排序是非比较型排序，可以突破O(nlogn)的时间复杂度下限
   - 基数排序需要额外的空间，而有些比较排序可以原地进行
   - 基数排序通常只适用于整数或可分解为整数的数据，而比较排序适用于任何可比较的数据

2. 为什么基数排序是稳定的
   - 在每一轮计数排序中，从后向前处理元素，可以保证相等元素的相对顺序不变
   - 稳定性对于多级排序（如先按日期排序，再按时间排序）非常重要

3. 基数排序的实际应用场景
   - 电话号码排序
   - 银行卡号排序
   - 字符串排序（按字符分解）
   - 日期时间排序（按年月日时分秒分解）

4. 如何选择合适的基数
   - 较小的基数会增加排序轮数，但每轮的计数数组更小
   - 较大的基数会减少排序轮数，但每轮的计数数组更大
   - 通常选择与内存缓存大小相匹配的基数以获得最佳性能

5. 基数排序的内存优化方法
   - 复用辅助数组
   - 使用两个缓冲区交替进行排序，避免复制
   - 对于特殊数据，可以使用原地基数排序

数学原理与底层逻辑：

1. 稳定性证明
   - 基数排序的稳定性基于每一轮计数排序的稳定性
   - 在计数排序中，从后向前处理元素确保了相等元素的相对顺序不变
   - 数学归纳法可以证明LSD基数排序的稳定性

2. 时间复杂度分析
   - 每一轮计数排序的时间复杂度为O(n+k)
   - 排序轮数等于最大数字的位数d
   - 总时间复杂度为O(d*(n+k))
   - 当k远小于n且d为常数时，时间复杂度接近O(n)

3. 空间复杂度分析
   - 需要一个大小为n的辅助数组
   - 需要一个大小为k的计数数组
   - 总空间复杂度为O(n+k)

4. 稳定性的重要性
   - 多级排序的基础
   - 保持相等元素的相对顺序
   - 在某些应用中（如排序对象），稳定性是必需的

应用场景与问题迁移：

1. 字符串排序
   - 可以将字符串分解为字符进行基数排序
   - 对于变长字符串，可以使用MSD（最高位优先）的方法

2. 浮点数排序
   - 可以将浮点数的整数部分和小数部分分开处理
   - 需要注意精度问题

3. 分布式排序
   - 基数排序可以很好地适应分布式计算环境
   - 可以按位对数据进行分区和合并

4. 大数据处理
   - 对于无法一次性加载到内存的数据，可以采用外部基数排序
   - 结合磁盘和内存进行排序

5. 图像处理应用
   - 可以用于图像像素值的排序和统计
   - 图像直方图均衡化等操作的基础

6. 数据库索引
   - 基数排序可以用于数据库索引的构建
   - 提高查询效率

7. 机器学习应用
   - 特征工程中的数据预处理
   - 大规模数据集的排序和分析

Python语言特性的巧妙利用：

1. 整数处理的优势
   - Python的整数没有大小限制，不会发生溢出
   - 自动处理大整数计算，简化了算法实现

2. 列表推导式和生成器
   - 可以简化代码，提高可读性
   - 对于大规模数据，可以使用生成器减少内存使用

3. 内置函数和模块
   - 使用max()、min()等内置函数提高效率
   - 利用collections模块优化计数操作
   - 使用multiprocessing实现并行排序

4. 装饰器和上下文管理器
   - 可以用于添加性能监控和日志记录
   - 简化资源管理

5. 类型提示
   - 使用Python的类型提示提高代码可读性
   - 有助于IDE进行代码补全和错误检查

代码调试与问题定位技巧：

1. 打印中间过程
   - 在每轮排序后打印数组内容
   - 打印计数数组和前缀和数组
   - 监控关键变量的变化

2. 使用断言验证中间结果
   - 验证排序的稳定性
   - 验证计数数组和前缀和的正确性
   - 验证偏移量计算和恢复是否正确

3. 单元测试覆盖
   - 测试各种边界情况
   - 测试特殊输入
   - 测试性能和正确性

4. 性能分析
   - 使用cProfile分析性能瓶颈
   - 优化热点代码
   - 考虑算法参数调优

5. 日志记录
   - 添加详细的日志记录关键操作
   - 记录性能指标
   - 帮助问题定位和调试
"""