# LeetCode 78. Subsets
# 子集
# 题目来源：https://leetcode.cn/problems/subsets/

from typing import List

"""
问题描述：
给你一个整数数组 nums，数组中的元素互不相同。返回该数组所有可能的子集（幂集）。
解集不能包含重复的子集。你可以按任意顺序返回解集。

解题思路：
使用回溯算法，通过递归生成所有可能的子集
1. 选择：对于每个元素，可以选择包含或不包含在子集中
2. 约束：元素互不相同，不能重复选择同一个元素
3. 目标：生成所有可能的子集（包括空集和原数组本身）

时间复杂度：O(N * 2^N)，其中N是数组的长度，2^N是子集的总数，每个子集需要O(N)的时间复制
空间复杂度：O(N)，递归栈的深度最多为N
"""

class Subsets:
    """求解子集问题的类，提供三种实现方法"""
    
    def subsets(self, nums: List[int]) -> List[List[int]]:
        """
        使用回溯算法生成所有子集
        
        Args:
            nums: 输入的整数数组，元素互不相同
            
        Returns:
            List[List[int]]: 所有可能的子集
        """
        result = []
        if not nums:
            # 至少返回空集
            result.append([])
            return result
        
        # 存储当前子集
        current_subset = []
        
        # 开始回溯
        self._backtrack(nums, 0, current_subset, result)
        
        return result
    
    def _backtrack(self, nums: List[int], start: int, 
                   current_subset: List[int], result: List[List[int]]) -> None:
        """
        回溯函数
        
        Args:
            nums: 输入数组
            start: 当前考虑元素的起始索引
            current_subset: 当前正在构建的子集
            result: 存储所有子集的结果集
        """
        # 每次进入函数，都将当前子集的副本加入结果集
        result.append(current_subset.copy())
        
        # 遍历从start开始的所有元素，决定是否将其加入子集
        for i in range(start, len(nums)):
            # 选择当前元素，将其加入子集
            current_subset.append(nums[i])
            
            # 递归到下一层，考虑下一个位置的元素
            self._backtrack(nums, i + 1, current_subset, result)
            
            # 回溯：撤销选择，移除刚刚添加的元素
            current_subset.pop()
    
    def subsets_by_bitmask(self, nums: List[int]) -> List[List[int]]:
        """
        使用位运算生成所有子集
        
        Args:
            nums: 输入的整数数组
            
        Returns:
            List[List[int]]: 所有可能的子集
        """
        result = []
        if not nums:
            result.append([])
            return result
        
        n = len(nums)
        # 总共有2^n个子集
        total_subsets = 1 << n  # 相当于2^n
        
        # 遍历从0到2^n-1的所有数字，每个数字代表一个子集的位掩码
        for mask in range(total_subsets):
            subset = []
            
            # 检查每一位是否为1，如果为1则将对应的元素加入子集
            for i in range(n):
                # 检查mask的第i位是否为1
                if mask & (1 << i):
                    subset.append(nums[i])
            
            result.append(subset)
        
        return result
    
    def subsets_iterative(self, nums: List[int]) -> List[List[int]]:
        """
        使用增量法迭代构建子集
        
        Args:
            nums: 输入的整数数组
            
        Returns:
            List[List[int]]: 所有可能的子集
        """
        result = [[]]  # 初始时加入空集
        
        if not nums:
            return result
        
        # 对于每个元素，将其添加到现有所有子集中，生成新的子集
        for num in nums:
            # 创建新子集列表，基于现有子集但添加当前元素
            new_subsets = [subset + [num] for subset in result]
            # 将新子集加入结果集
            result.extend(new_subsets)
        
        return result

# 测试代码
if __name__ == "__main__":
    solution = Subsets()
    
    # 测试用例1
    nums1 = [1, 2, 3]
    print("测试用例1 - 回溯算法:")
    result1 = solution.subsets(nums1)
    print(result1)
    
    print("\n测试用例1 - 位运算:")
    result1_by_bitmask = solution.subsets_by_bitmask(nums1)
    print(result1_by_bitmask)
    
    print("\n测试用例1 - 迭代增量法:")
    result1_iterative = solution.subsets_iterative(nums1)
    print(result1_iterative)
    
    # 测试用例2
    nums2 = [0]
    print("\n测试用例2 - 回溯算法:")
    result2 = solution.subsets(nums2)
    print(result2)

"""
性能分析：

1. 回溯算法：
   - 时间复杂度：O(N * 2^N)，其中N是数组长度，2^N是子集的总数，每个子集需要O(N)的时间复制
   - 空间复杂度：O(N)，递归栈的深度最多为N，以及存储当前子集的空间
   - 优点：逻辑清晰，容易理解和实现
   - 缺点：递归可能导致栈溢出（对于非常大的数组）

2. 位运算解法：
   - 时间复杂度：O(N * 2^N)，需要遍历2^N个掩码，每个掩码需要O(N)的时间生成子集
   - 空间复杂度：O(N)，主要是存储结果的空间（不考虑输出）
   - 优点：代码简洁，对于小规模问题效率较高
   - 缺点：当N较大时（如超过30），2^N会超出整型范围；对于非常大的N不适用

3. 迭代增量法：
   - 时间复杂度：O(N * 2^N)，需要处理2^N个子集，每个子集可能需要O(N)的复制操作
   - 空间复杂度：O(N * 2^N)，存储所有子集
   - 优点：避免了递归可能导致的栈溢出问题，Python中列表推导式使代码更简洁
   - 缺点：空间复杂度较高

Python语言特性利用：
1. 利用列表推导式简化迭代增量法中的子集生成
2. 使用copy()方法或列表拼接创建子集的副本
3. 利用Python的位运算操作符简化位掩码实现
4. 使用类型提示提高代码可读性和可维护性

算法优化思路：
1. 剪枝优化：在特定应用场景中，可以根据条件进行剪枝，提前停止某些分支的搜索
2. 内存优化：对于非常大的数组，可以考虑使用生成器模式进行惰性计算
3. 并行计算：对于大规模数据，可以考虑使用multiprocessing库进行并行计算

工程化考量：
1. 对于包含重复元素的数组，需要修改算法以避免生成重复的子集
2. 在处理大规模数据时，需要考虑内存限制和性能瓶颈
3. 可以添加缓存机制，在某些应用场景中避免重复计算
4. 在多线程环境中使用时，需要确保线程安全
5. 可以添加日志记录，方便调试和性能监控

子集问题的应用场景：
1. 组合优化问题
2. 枚举所有可能的选择方案
3. 机器学习中的特征选择
4. 数据库查询优化
5. 密码学中的暴力破解（理论上）

回溯算法与动态规划的区别：
1. 回溯算法通常用于求解所有可能的解，而动态规划通常用于求解最优解
2. 回溯算法通过枚举所有可能来求解，时间复杂度通常较高
3. 动态规划通过存储中间结果避免重复计算，通常更高效
4. 回溯算法是一种试探法，而动态规划是一种分治方法
"""