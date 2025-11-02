# LeetCode 327. 区间和的个数
# 给定一个整数数组 nums 以及两个整数 lower 和 upper，求区间 [lower, upper] 内的区间和的个数。
# 测试链接: https://leetcode.cn/problems/count-of-range-sum/

from typing import List

class Solution:
    """
    区间和的个数 - 树状数组解法
    
    解题思路:
    1. 题目要求计算数组中区间和位于 [lower, upper] 范围内的子数组个数
    2. 利用前缀和思想：区间和 sum(i,j) = prefix[j+1] - prefix[i]
    3. 问题转化为：对于每个 j，计算有多少个 i < j 满足 lower <= prefix[j] - prefix[i] <= upper
    4. 进一步转化为：对于每个 j，统计 prefix[i] 的范围为 [prefix[j] - upper, prefix[j] - lower] 的 i 的个数
    5. 使用树状数组可以高效地统计这个范围查询
    6. 由于前缀和可能很大，需要进行离散化处理
    
    时间复杂度分析:
    - 计算前缀和: O(n)
    - 离散化: O(n log n)
    - 构建和操作树状数组: O(n log n)
    - 总时间复杂度: O(n log n)
    
    空间复杂度分析:
    - 前缀和数组: O(n)
    - 离散化数组和映射: O(n)
    - 树状数组: O(n)
    - 总空间复杂度: O(n)
    
    工程化考量:
    1. 离散化处理：由于前缀和可能超出整数范围，进行离散化
    2. 边界条件处理：处理空数组、单元素数组等特殊情况
    3. 数据类型溢出：Python自动处理大数，无需特别关注
    4. 异常输入检查：验证输入的有效性
    5. 代码可读性：使用清晰的变量命名和详细的注释
    """

    class FenwickTree:
        """
        树状数组类
        用于高效计算前缀和和单点更新
        """
        def __init__(self, size: int):
            """
            初始化树状数组
            :param size: 数组大小
            """
            self.n = size
            self.tree = [0] * (size + 1)  # 树状数组下标从1开始

        def lowbit(self, x: int) -> int:
            """
            lowbit操作，获取x的二进制表示中最低位的1所代表的值
            :param x: 输入整数
            :return: x & (-x)
            """
            return x & (-x)

        def update(self, index: int, delta: int) -> None:
            """
            在指定位置增加delta
            :param index: 索引位置（从1开始）
            :param delta: 增加的值
            """
            # 沿树状数组向上更新所有相关节点
            while index <= self.n:
                self.tree[index] += delta
                index += self.lowbit(index)

        def query(self, index: int) -> int:
            """
            查询前缀和[1, index]
            :param index: 查询的右边界（从1开始）
            :return: 前缀和
            """
            sum_val = 0
            # 沿树状数组向下累加所有相关节点的值
            while index > 0:
                sum_val += self.tree[index]
                index -= self.lowbit(index)
            return sum_val

        def query_range(self, left: int, right: int) -> int:
            """
            查询区间和[left, right]
            :param left: 区间左边界（从1开始）
            :param right: 区间右边界（从1开始）
            :return: 区间和
            """
            if left > right:
                return 0
            return self.query(right) - self.query(left - 1)

    def countRangeSum(self, nums: List[int], lower: int, upper: int) -> int:
        """
        计算区间和的个数 - 树状数组解法
        :param nums: 输入数组
        :param lower: 区间下界
        :param upper: 区间上界
        :return: 满足条件的子数组个数
        """
        # 边界条件检查
        if not nums:
            return 0

        n = len(nums)
        prefix_sums = [0] * (n + 1)  # 前缀和数组

        # 计算前缀和
        for i in range(n):
            prefix_sums[i + 1] = prefix_sums[i] + nums[i]

        # 离散化处理
        # 收集所有可能需要查询的值
        values_set = set()
        for sum_val in prefix_sums:
            values_set.add(sum_val)
            values_set.add(sum_val - lower)
            values_set.add(sum_val - upper)

        # 排序并去重
        sorted_values = sorted(values_set)

        # 建立值到索引的映射
        value_to_index = {value: i + 1 for i, value in enumerate(sorted_values)}  # 索引从1开始

        # 创建树状数组
        fenwick_tree = self.FenwickTree(len(sorted_values))
        count = 0

        # 从前向后遍历前缀和数组
        for prefix_sum in prefix_sums:
            # 查询满足条件的前缀和的数量：prefixSum[j] - upper <= prefixSum[i] <= prefixSum[j] - lower
            left_index = value_to_index[prefix_sum - upper]
            right_index = value_to_index[prefix_sum - lower]
            count += fenwick_tree.query_range(left_index, right_index)

            # 将当前前缀和加入树状数组
            current_index = value_to_index[prefix_sum]
            fenwick_tree.update(current_index, 1)

        return count

    def countRangeSumBruteForce(self, nums: List[int], lower: int, upper: int) -> int:
        """
        暴力解法（仅供比较，时间复杂度较高）
        时间复杂度: O(n²)
        空间复杂度: O(n)
        :param nums: 输入数组
        :param lower: 区间下界
        :param upper: 区间上界
        :return: 满足条件的子数组个数
        """
        if not nums:
            return 0

        n = len(nums)
        prefix_sums = [0] * (n + 1)

        # 计算前缀和
        for i in range(n):
            prefix_sums[i + 1] = prefix_sums[i] + nums[i]

        count = 0
        # 暴力枚举所有可能的子数组
        for i in range(n):
            for j in range(i + 1, n + 1):
                range_sum = prefix_sums[j] - prefix_sums[i]
                if lower <= range_sum <= upper:
                    count += 1

        return count

    def countRangeSumMergeSort(self, nums: List[int], lower: int, upper: int) -> int:
        """
        归并排序解法（另一种O(n log n)的解法）
        :param nums: 输入数组
        :param lower: 区间下界
        :param upper: 区间上界
        :return: 满足条件的子数组个数
        """
        if not nums:
            return 0

        n = len(nums)
        prefix_sums = [0] * (n + 1)

        # 计算前缀和
        for i in range(n):
            prefix_sums[i + 1] = prefix_sums[i] + nums[i]

        # 归并排序过程中统计满足条件的子数组个数
        return self._mergeSortAndCount(prefix_sums, 0, n, lower, upper)

    def _mergeSortAndCount(self, prefix_sums: List[int], left: int, right: int, lower: int, upper: int) -> int:
        """
        归并排序并统计满足条件的子数组个数
        :param prefix_sums: 前缀和数组
        :param left: 左边界
        :param right: 右边界
        :param lower: 区间下界
        :param upper: 区间上界
        :return: 满足条件的子数组个数
        """
        if left >= right:
            return 0

        mid = left + (right - left) // 2
        # 递归处理左右两部分
        count = self._mergeSortAndCount(prefix_sums, left, mid, lower, upper) + \
                self._mergeSortAndCount(prefix_sums, mid + 1, right, lower, upper)

        # 统计满足条件的子数组个数
        j = mid + 1
        k = mid + 1
        for i in range(left, mid + 1):
            # 找到最小的j，使得prefix_sums[j] - prefix_sums[i] >= lower
            while j <= right and prefix_sums[j] - prefix_sums[i] < lower:
                j += 1
            # 找到最大的k，使得prefix_sums[k] - prefix_sums[i] <= upper
            while k <= right and prefix_sums[k] - prefix_sums[i] <= upper:
                k += 1
            # 区间[j, k-1]内的所有前缀和都满足条件
            count += k - j

        # 合并两个有序数组
        self._merge(prefix_sums, left, mid, right)

        return count

    def _merge(self, prefix_sums: List[int], left: int, mid: int, right: int) -> None:
        """
        合并两个有序数组
        :param prefix_sums: 前缀和数组
        :param left: 左边界
        :param mid: 中间点
        :param right: 右边界
        """
        temp = []
        i = left
        j = mid + 1

        # 合并两个有序数组
        while i <= mid and j <= right:
            if prefix_sums[i] <= prefix_sums[j]:
                temp.append(prefix_sums[i])
                i += 1
            else:
                temp.append(prefix_sums[j])
                j += 1

        # 处理剩余元素
        while i <= mid:
            temp.append(prefix_sums[i])
            i += 1

        while j <= right:
            temp.append(prefix_sums[j])
            j += 1

        # 将临时数组复制回原数组
        for k in range(len(temp)):
            prefix_sums[left + k] = temp[k]

# 测试函数
def test_solution():
    solution = Solution()
    
    # 测试用例1
    nums1 = [-2, 5, -1]
    lower1 = -2
    upper1 = 2
    print("测试用例1:")
    print(f"输入数组: {nums1}")
    print(f"lower: {lower1}, upper: {upper1}")
    print(f"树状数组解法结果: {solution.countRangeSum(nums1, lower1, upper1)}")  # 期望输出: 3
    print(f"归并排序解法结果: {solution.countRangeSumMergeSort(nums1, lower1, upper1)}")  # 期望输出: 3
    print(f"暴力解法结果: {solution.countRangeSumBruteForce(nums1, lower1, upper1)}")  # 期望输出: 3
    
    # 测试用例2
    nums2 = [0]
    lower2 = 0
    upper2 = 0
    print("\n测试用例2:")
    print(f"输入数组: {nums2}")
    print(f"lower: {lower2}, upper: {upper2}")
    print(f"树状数组解法结果: {solution.countRangeSum(nums2, lower2, upper2)}")  # 期望输出: 1
    
    # 测试用例3 - 空数组
    nums3 = []
    lower3 = 0
    upper3 = 0
    print("\n测试用例3:")
    print(f"输入数组: {nums3}")
    print(f"lower: {lower3}, upper: {upper3}")
    print(f"树状数组解法结果: {solution.countRangeSum(nums3, lower3, upper3)}")  # 期望输出: 0
    
    # 测试用例4 - 大规模数据
    size = 1000
    nums4 = [(i % 3 == 0) and -1 or (i % 3 == 1) and 0 or 1 for i in range(size)]
    lower4 = -2
    upper4 = 2
    print("\n测试用例4 (大规模数据):")
    print(f"数组长度: {size}")
    print(f"lower: {lower4}, upper: {upper4}")
    
    import time
    
    # 测量树状数组解法的时间
    start_time1 = time.time()
    result1 = solution.countRangeSum(nums4, lower4, upper4)
    end_time1 = time.time()
    print(f"树状数组解法结果: {result1}")
    print(f"树状数组解法耗时: {(end_time1 - start_time1) * 1000:.2f}ms")
    
    # 测量归并排序解法的时间
    start_time2 = time.time()
    result2 = solution.countRangeSumMergeSort(nums4, lower4, upper4)
    end_time2 = time.time()
    print(f"归并排序解法结果: {result2}")
    print(f"归并排序解法耗时: {(end_time2 - start_time2) * 1000:.2f}ms")
    
    # 验证两种方法结果是否一致
    print(f"两种方法结果一致: {result1 == result2}")
    
    # 对比暴力解法（仅在小规模数据上测试）
    if size <= 1000:
        small_size = min(size, 300)  # 限制暴力解法的数组大小，避免超时
        small_nums = nums4[:small_size]
        
        start_time3 = time.time()
        result3 = solution.countRangeSumBruteForce(small_nums, lower4, upper4)
        end_time3 = time.time()
        print(f"暴力解法(前{small_size}个元素)结果: {result3}")
        print(f"暴力解法耗时: {(end_time3 - start_time3) * 1000:.2f}ms")
        
        # 验证暴力解法与树状数组解法在前small_size个元素上是否一致
        result4 = solution.countRangeSum(small_nums, lower4, upper4)
        print(f"暴力解法与树状数组解法结果一致: {result3 == result4}")

# 运行测试
if __name__ == "__main__":
    test_solution()