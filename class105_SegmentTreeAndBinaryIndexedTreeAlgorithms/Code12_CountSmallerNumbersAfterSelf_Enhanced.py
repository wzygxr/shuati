# LeetCode 315. 计算右侧小于当前元素的个数
# 给定一个整数数组 nums，按要求返回一个新数组 counts。
# 数组 counts 有该性质：counts[i] 的值是 nums[i] 右侧小于 nums[i] 的元素的数量。
# 测试链接: https://leetcode.cn/problems/count-of-smaller-numbers-after-self/

from typing import List
import bisect

class Solution:
    """
    计算右侧小于当前元素的个数 - 树状数组解法
    
    解题思路:
    1. 题目要求计算每个元素右侧小于它的元素个数
    2. 我们可以将问题转化为：对于每个元素nums[i]，统计有多少个元素nums[j] (j > i)满足 nums[j] < nums[i]
    3. 使用树状数组可以高效地进行这类统计
    4. 具体步骤：
       a. 将数组元素离散化，以处理可能的大范围数值
       b. 从右到左遍历数组，对于每个元素nums[i]：
          - 查询树状数组中小于nums[i]的元素个数（即前缀和）
          - 将nums[i]加入树状数组
       c. 这样就能保证每次查询的都是当前元素右侧的元素
    
    时间复杂度分析:
    - 离散化: O(n log n)
    - 构建和操作树状数组: O(n log n)
    - 总时间复杂度: O(n log n)
    
    空间复杂度分析:
    - 树状数组: O(n)
    - 离散化数组和映射: O(n)
    - 结果数组: O(n)
    - 总空间复杂度: O(n)
    
    工程化考量:
    1. 离散化处理：由于输入数组可能包含很大范围的整数，离散化可以有效减少空间使用
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

    def countSmaller(self, nums: List[int]) -> List[int]:
        """
        计算右侧小于当前元素的个数 - 树状数组解法
        :param nums: 输入数组
        :return: 结果数组
        """
        # 边界条件检查
        if not nums:
            return []

        n = len(nums)
        result = []

        # 离散化处理
        # 1. 收集所有可能的数值
        values_set = set()
        for num in nums:
            values_set.add(num)

        # 2. 排序并去重
        sorted_values = sorted(values_set)

        # 3. 建立值到索引的映射
        value_to_index = {value: i + 1 for i, value in enumerate(sorted_values)}  # 索引从1开始

        # 创建树状数组
        fenwick_tree = self.FenwickTree(len(sorted_values))

        # 从右到左遍历数组
        for i in range(n - 1, -1, -1):
            current_value = nums[i]
            # 查询比当前值小的元素个数
            count = 0
            # 找到当前值在离散化数组中的位置
            index = value_to_index[current_value]
            # 查询比当前值小的元素个数，即查询[1, index-1]的前缀和
            if index > 1:
                count = fenwick_tree.query(index - 1)
            # 将结果添加到列表（注意后续需要反转）
            result.append(count)
            # 将当前值加入树状数组
            fenwick_tree.update(index, 1)

        # 反转结果列表，因为我们是从右到左计算的
        return result[::-1]

    def countSmallerMergeSort(self, nums: List[int]) -> List[int]:
        """
        另一种解法：归并排序过程中计算逆序对
        这种方法也能在O(n log n)时间内解决问题
        :param nums: 输入数组
        :return: 结果数组
        """
        n = len(nums)
        result = [0] * n
        if n == 0:
            return result
        
        # 创建索引数组，用于跟踪元素原始位置
        indexes = list(range(n))
        
        # 归并排序过程中计算右侧小于当前元素的个数
        self._merge_sort(nums, indexes, 0, n - 1, result)
        return result
    
    def _merge_sort(self, nums: List[int], indexes: List[int], left: int, right: int, result: List[int]) -> None:
        """
        归并排序的递归实现
        """
        if left >= right:
            return
        
        mid = left + (right - left) // 2
        self._merge_sort(nums, indexes, left, mid, result)
        self._merge_sort(nums, indexes, mid + 1, right, result)
        self._merge(nums, indexes, left, mid, right, result)
    
    def _merge(self, nums: List[int], indexes: List[int], left: int, mid: int, right: int, result: List[int]) -> None:
        """
        合并两个有序数组，并计算右侧小于当前元素的个数
        """
        temp_indexes = []
        i, j = left, mid + 1
        
        # 合并两个有序数组，并计算右侧小于当前元素的个数
        while i <= mid and j <= right:
            if nums[indexes[i]] <= nums[indexes[j]]:
                # 右侧比当前元素小的数量为j - (mid + 1)
                result[indexes[i]] += j - (mid + 1)
                temp_indexes.append(indexes[i])
                i += 1
            else:
                temp_indexes.append(indexes[j])
                j += 1
        
        # 处理剩余元素
        while i <= mid:
            result[indexes[i]] += j - (mid + 1)
            temp_indexes.append(indexes[i])
            i += 1
        
        while j <= right:
            temp_indexes.append(indexes[j])
            j += 1
        
        # 将临时数组复制回原数组
        for k in range(len(temp_indexes)):
            indexes[left + k] = temp_indexes[k]

    def countSmallerBruteForce(self, nums: List[int]) -> List[int]:
        """
        暴力解法（仅供比较，时间复杂度较高）
        时间复杂度: O(n²)
        空间复杂度: O(n)
        :param nums: 输入数组
        :return: 结果数组
        """
        result = []
        if not nums:
            return result
        
        n = len(nums)
        for i in range(n):
            count = 0
            for j in range(i + 1, n):
                if nums[j] < nums[i]:
                    count += 1
            result.append(count)
        return result

    def countSmallerWithBisect(self, nums: List[int]) -> List[int]:
        """
        利用bisect模块的解法
        从右到左遍历，维护一个有序数组，使用bisect_left找到插入位置
        该位置即为比当前元素小的元素个数
        时间复杂度: O(n²)，因为插入操作是O(n)
        空间复杂度: O(n)
        :param nums: 输入数组
        :return: 结果数组
        """
        result = []
        if not nums:
            return result
        
        sorted_list = []
        # 从右到左遍历
        for num in reversed(nums):
            # 找到插入位置，该位置即为比当前元素小的元素个数
            index = bisect.bisect_left(sorted_list, num)
            result.append(index)
            # 将当前元素插入到有序数组中
            bisect.insort(sorted_list, num)
        
        # 反转结果列表
        return result[::-1]

# 测试函数
def test_solution():
    solution = Solution()
    
    # 测试用例1
    nums1 = [5, 2, 6, 1]
    print("测试用例1:")
    print(f"输入: {nums1}")
    print(f"树状数组解法结果: {solution.countSmaller(nums1)}")  # 期望输出: [2, 1, 1, 0]
    print(f"归并排序解法结果: {solution.countSmallerMergeSort(nums1)}")  # 期望输出: [2, 1, 1, 0]
    print(f"Bisect解法结果: {solution.countSmallerWithBisect(nums1)}")  # 期望输出: [2, 1, 1, 0]
    print(f"暴力解法结果: {solution.countSmallerBruteForce(nums1)}")  # 期望输出: [2, 1, 1, 0]
    
    # 测试用例2
    nums2 = [-1, -1]
    print("\n测试用例2:")
    print(f"输入: {nums2}")
    print(f"树状数组解法结果: {solution.countSmaller(nums2)}")  # 期望输出: [0, 0]
    
    # 测试用例3 - 空数组
    nums3 = []
    print("\n测试用例3:")
    print(f"输入: {nums3}")
    print(f"树状数组解法结果: {solution.countSmaller(nums3)}")  # 期望输出: []
    
    # 测试用例4 - 大规模数据
    size = 1000
    nums4 = list(range(size, 0, -1))  # 逆序数组
    print("\n测试用例4 (大规模逆序数组):")
    print(f"数组长度: {size}")
    
    import time
    
    start_time1 = time.time()
    result1 = solution.countSmaller(nums4)
    end_time1 = time.time()
    print(f"树状数组解法耗时: {(end_time1 - start_time1) * 1000:.2f}ms")
    
    start_time2 = time.time()
    result2 = solution.countSmallerMergeSort(nums4)
    end_time2 = time.time()
    print(f"归并排序解法耗时: {(end_time2 - start_time2) * 1000:.2f}ms")
    
    start_time3 = time.time()
    result3 = solution.countSmallerWithBisect(nums4)
    end_time3 = time.time()
    print(f"Bisect解法耗时: {(end_time3 - start_time3) * 1000:.2f}ms")
    
    # 验证所有方法结果是否一致
    print(f"树状数组与归并排序结果一致: {result1 == result2}")
    print(f"树状数组与Bisect结果一致: {result1 == result3}")
    
    # 对比暴力解法（仅在小规模数据上测试）
    if size <= 1000:
        small_size = min(size, 500)  # 限制暴力解法的数组大小，避免超时
        small_nums = nums4[:small_size]
        
        start_time4 = time.time()
        result4 = solution.countSmallerBruteForce(small_nums)
        end_time4 = time.time()
        print(f"暴力解法(前{small_size}个元素)耗时: {(end_time4 - start_time4) * 1000:.2f}ms")
        
        # 验证暴力解法与树状数组解法在前small_size个元素上是否一致
        result1_small = solution.countSmaller(small_nums)
        print(f"暴力解法与树状数组解法结果一致: {result1_small == result4}")

# 运行测试
if __name__ == "__main__":
    test_solution()