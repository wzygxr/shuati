# LeetCode 493. 翻转对
# 给定一个数组 nums ，如果 i < j 且 nums[i] > 2*nums[j] 我们将 (i, j) 称作一个重要翻转对。
# 你需要返回给定数组中的重要翻转对的数量。
# 测试链接: https://leetcode.cn/problems/reverse-pairs/

from typing import List
import bisect

class Solution:
    """
    使用树状数组解决翻转对问题
    
    解题思路:
    1. 翻转对的定义是 i < j 且 nums[i] > 2*nums[j]
    2. 我们可以将所有可能涉及到的数值进行离散化处理
    3. 从左到右遍历数组，对于每个元素nums[i]：
       - 查询已经处理过的元素中，有多少个元素满足 nums[j] < nums[i]/2.0（j < i）
       - 将当前元素加入树状数组
    4. 由于涉及到2*nums[j]，我们需要同时将nums[j]和2*nums[j]都加入离散化数组
    
    时间复杂度分析:
    - 离散化: O(n log n)
    - 遍历数组并查询更新: O(n log n)
    - 总时间复杂度: O(n log n)
    
    空间复杂度分析:
    - 树状数组: O(n)
    - 离散化数组: O(n)
    - 总空间复杂度: O(n)
    
    工程化考量:
    1. 离散化处理大数值范围
    2. 边界条件处理（整数溢出问题）
    3. 异常输入检查
    4. 详细注释和变量命名
    """

    class FenwickTree:
        def __init__(self, size: int):
            """
            初始化树状数组
            :param size: 数组大小
            """
            self.n = size
            self.tree = [0] * (size + 1)

        def lowbit(self, x: int) -> int:
            """
            lowbit操作，获取x的二进制表示中最右边的1所代表的值
            :param x: 输入整数
            :return: x & (-x)
            """
            return x & (-x)

        def add(self, index: int, delta: int) -> None:
            """
            在树状数组中更新指定位置的值（增加delta）
            :param index: 要更新的位置（从1开始）
            :param delta: 增加的值
            """
            # 沿路径向上更新所有相关节点
            while index <= self.n:
                self.tree[index] += delta
                index += self.lowbit(index)

        def query(self, index: int) -> int:
            """
            查询前缀和[1, index]的和
            :param index: 查询的右边界（从1开始）
            :return: 前缀和
            """
            sum_val = 0
            # 沿路径向下累加所有相关节点的值
            while index > 0:
                sum_val += self.tree[index]
                index -= self.lowbit(index)
            return sum_val

    def reversePairs(self, nums: List[int]) -> int:
        """
        计算数组中翻转对的数量
        :param nums: 输入数组
        :return: 翻转对的数量
        """
        n = len(nums)
        if n == 0:
            return 0
        
        # 离散化处理
        # 1. 收集所有需要离散化的值：nums[i] 和 2*nums[i]
        all_numbers = set()
        for num in nums:
            all_numbers.add(num)
            all_numbers.add(2 * num)
        
        # 2. 排序去重
        sorted_numbers = sorted(list(all_numbers))
        
        # 3. 建立数值到离散化索引的映射
        index_map = {}
        for i, num in enumerate(sorted_numbers):
            index_map[num] = i + 1
        
        # 4. 创建树状数组
        fenwick_tree = self.FenwickTree(len(sorted_numbers))
        
        result = 0
        
        # 5. 从左到右遍历数组
        for i in range(n):
            # 查询有多少个已经处理过的元素满足 nums[j] > 2*nums[i]
            # 也就是查询有多少个已经处理过的元素满足 nums[j] >= 2*nums[i]+1
            target = 2 * nums[i] + 1
            
            # 找到target在离散化数组中的位置
            pos = bisect.bisect_left(sorted_numbers, target)
            
            # 查询大于等于target的元素个数
            result += fenwick_tree.query(len(sorted_numbers)) - fenwick_tree.query(pos)
            
            # 将当前元素加入树状数组
            index = index_map[nums[i]]
            fenwick_tree.add(index, 1)
        
        return result


# 测试函数
def test():
    solution = Solution()
    
    # 测试用例1
    nums1 = [1, 3, 2, 3, 1]
    result1 = solution.reversePairs(nums1)
    print(f"Input: [1, 3, 2, 3, 1]")
    print(f"Output: {result1}")  # 期望输出: 2
    
    # 测试用例2
    nums2 = [2, 4, 3, 5, 1]
    result2 = solution.reversePairs(nums2)
    print(f"Input: [2, 4, 3, 5, 1]")
    print(f"Output: {result2}")  # 期望输出: 3
    
    # 测试用例3
    nums3 = [-5, -5]
    result3 = solution.reversePairs(nums3)
    print(f"Input: [-5, -5]")
    print(f"Output: {result3}")  # 期望输出: 1
    
    # 测试用例4
    nums4 = [2147483647, 2147483647, 2147483647, 2147483647, 2147483647, 2147483647]
    result4 = solution.reversePairs(nums4)
    print(f"Large numbers test case result: {result4}")  # 期望输出: 0


if __name__ == "__main__":
    test()