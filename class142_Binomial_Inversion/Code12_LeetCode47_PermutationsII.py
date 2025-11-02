#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
LeetCode 47: 全排列 II (Permutations II) - Python实现
题目链接：https://leetcode.cn/problems/permutations-ii/
题目描述：给定一个可包含重复数字的序列nums，按任意顺序返回所有不重复的全排列

二项式反演应用：
当序列包含重复元素时，直接生成全排列会产生重复结果。
可以使用二项式反演思想结合回溯算法来避免重复排列。

算法原理：
1. 先对数组排序，使相同元素相邻
2. 使用回溯算法生成所有排列
3. 通过剪枝避免重复排列：
   - 如果当前元素与前一个元素相同且前一个元素未被使用，则跳过
   - 这样可以确保相同元素的相对顺序固定，避免重复

时间复杂度分析：
- 最坏情况：O(n * n!) - 需要生成所有排列，每个排列需要O(n)时间复制
- 平均情况：由于剪枝，实际生成的排列数远小于n!

空间复杂度分析：
- O(n) - 递归栈深度为n，标记数组大小为n

工程化考量：
- 使用生成器模式避免内存溢出
- 排序预处理确保相同元素相邻
- 剪枝策略优化性能
"""

import math
from typing import List
from collections import Counter

class Solution:
    """
    全排列II解决方案类
    """
    
    def permuteUnique(self, nums: List[int]) -> List[List[int]]:
        """
        主函数：生成所有不重复的全排列
        
        Args:
            nums: 输入数组，可能包含重复元素
            
        Returns:
            所有不重复的全排列
            
        Raises:
            TypeError: 如果输入不是列表
            ValueError: 如果输入列表为空
        """
        if not isinstance(nums, list):
            raise TypeError("输入必须是列表类型")
        
        if len(nums) == 0:
            return []
        
        # 初始化结果集和辅助数据结构
        result = []
        used = [False] * len(nums)
        current = []
        
        # 关键步骤：先对数组排序，使相同元素相邻
        # 这样可以在回溯时通过比较相邻元素来避免重复
        nums_sorted = sorted(nums)
        
        # 开始回溯搜索
        self._backtrack(nums_sorted, used, current, result)
        
        return result
    
    def _backtrack(self, nums: List[int], used: List[bool], 
                   current: List[int], result: List[List[int]]) -> None:
        """
        回溯算法核心函数
        
        Args:
            nums: 排序后的输入数组
            used: 标记数组，记录哪些元素已被使用
            current: 当前正在构建的排列
            result: 存储所有排列的结果集
        """
        # 终止条件：当前排列长度等于数组长度
        if len(current) == len(nums):
            # 添加当前排列的副本到结果集
            result.append(current[:])  # 使用切片创建副本
            return
        
        # 遍历所有可能的下一位置选择
        for i in range(len(nums)):
            # 剪枝条件1：当前元素已被使用
            if used[i]:
                continue
            
            # 剪枝条件2：避免重复排列的关键
            # 如果当前元素与前一个元素相同，且前一个元素未被使用，则跳过
            # 这样可以确保相同元素的相对顺序固定
            if i > 0 and nums[i] == nums[i - 1] and not used[i - 1]:
                continue
            
            # 选择当前元素
            used[i] = True
            current.append(nums[i])
            
            # 递归搜索下一层
            self._backtrack(nums, used, current, result)
            
            # 回溯：撤销选择
            current.pop()
            used[i] = False
    
    def count_unique_permutations(self, nums: List[int]) -> int:
        """
        使用二项式反演思想计算不重复排列数（数学方法）
        
        算法原理：
        设数组中有k种不同的数字，每种数字出现次数为c1, c2, ..., ck
        则总排列数为：n! / (c1! * c2! * ... * ck!)
        
        时间复杂度：O(n) - 需要统计频率和计算阶乘
        空间复杂度：O(k) - 需要存储频率统计
        
        Args:
            nums: 输入数组
            
        Returns:
            不重复排列的数量（数学计算结果）
        """
        if not nums:
            return 0
        
        # 统计每个数字的出现频率
        freq = Counter(nums)
        
        # 计算n!
        numerator = math.factorial(len(nums))
        
        # 计算分母：c1! * c2! * ... * ck!
        denominator = 1
        for count in freq.values():
            denominator *= math.factorial(count)
        
        return numerator // denominator
    
    def permute_unique_generator(self, nums: List[int]):
        """
        生成器版本：逐个生成排列，避免内存溢出
        
        Args:
            nums: 输入数组
            
        Yields:
            每个不重复的排列
        """
        if not nums:
            return
        
        nums_sorted = sorted(nums)
        used = [False] * len(nums)
        current = []
        
        # 使用嵌套函数实现生成器
        def backtrack_gen():
            if len(current) == len(nums_sorted):
                yield current[:]
                return
            
            for i in range(len(nums_sorted)):
                if used[i]:
                    continue
                
                if i > 0 and nums_sorted[i] == nums_sorted[i - 1] and not used[i - 1]:
                    continue
                
                used[i] = True
                current.append(nums_sorted[i])
                
                yield from backtrack_gen()
                
                current.pop()
                used[i] = False
        
        yield from backtrack_gen()

def run_tests():
    """
    单元测试函数
    """
    solution = Solution()
    
    # 测试用例1：[1,1,2]
    print("=== 测试用例1: [1,1,2] ===")
    nums1 = [1, 1, 2]
    result1 = solution.permuteUnique(nums1)
    print(f"排列数量：{len(result1)}")
    print(f"数学计算数量：{solution.count_unique_permutations(nums1)}")
    print("排列结果：")
    for perm in result1:
        print(perm)
    
    # 测试用例2：[1,2,3]
    print("\n=== 测试用例2: [1,2,3] ===")
    nums2 = [1, 2, 3]
    result2 = solution.permuteUnique(nums2)
    print(f"排列数量：{len(result2)}")
    print(f"数学计算数量：{solution.count_unique_permutations(nums2)}")
    
    # 测试用例3：[1,1,1]
    print("\n=== 测试用例3: [1,1,1] ===")
    nums3 = [1, 1, 1]
    result3 = solution.permuteUnique(nums3)
    print(f"排列数量：{len(result3)}")
    print(f"数学计算数量：{solution.count_unique_permutations(nums3)}")
    
    # 测试生成器版本
    print("\n=== 测试生成器版本 ===")
    nums4 = [1, 1, 2]
    count = 0
    for perm in solution.permute_unique_generator(nums4):
        count += 1
        if count <= 3:  # 只显示前3个排列
            print(f"排列 {count}: {perm}")
    print(f"总排列数：{count}")

def interactive_test():
    """
    交互式测试函数
    """
    solution = Solution()
    
    print("\n=== 交互式测试 ===")
    print("请输入数组元素（用空格分隔，输入q退出）：")
    
    while True:
        try:
            user_input = input().strip()
            if user_input.lower() in ['q', 'quit', 'exit']:
                break
            
            if not user_input:
                continue
            
            # 解析输入
            nums = []
            for num_str in user_input.split():
                try:
                    num = int(num_str)
                    nums.append(num)
                except ValueError:
                    print(f"无法解析数字: {num_str}")
                    break
            
            if nums:
                # 使用生成器版本避免内存溢出
                count = 0
                max_display = 5
                first_few = []
                
                print("正在生成排列...")
                for perm in solution.permute_unique_generator(nums):
                    count += 1
                    if count <= max_display:
                        first_few.append(perm)
                
                print(f"不重复排列数量：{count}")
                print(f"数学计算数量：{solution.count_unique_permutations(nums)}")
                
                if first_few:
                    print(f"前{len(first_few)}个排列：")
                    for i, perm in enumerate(first_few, 1):
                        print(f"  {i}: {perm}")
                
                if count > max_display:
                    print(f"... 还有 {count - max_display} 个排列未显示")
            
            print("\n请输入数组元素（用空格分隔，输入q退出）：")
            
        except KeyboardInterrupt:
            print("\n程序被用户中断")
            break
        except Exception as e:
            print(f"发生错误：{e}")
            break

if __name__ == "__main__":
    # 运行单元测试
    run_tests()
    
    # 运行交互式测试
    interactive_test()

"""
算法优化思路：
1. 剪枝策略：通过排序和相邻元素比较，避免生成重复排列
2. 生成器模式：使用yield逐个生成排列，避免内存溢出
3. 并行计算：对于大规模数据，可以考虑使用多进程并行生成排列

边界条件处理：
- 空数组：返回空列表
- 单元素数组：返回单个排列
- 全相同元素：只有一个排列

异常场景：
- 输入不是列表：抛出TypeError
- 输入列表为空：返回空结果
- 数组长度过大：使用生成器模式避免内存溢出

工程化扩展：
- 缓存优化：对于相同输入，可以缓存结果
- 性能监控：添加性能统计和日志记录
- 类型注解：使用Python类型注解提高代码可读性
- 单元测试：添加更全面的测试用例

跨语言对比：
- Python优势：语法简洁，内置大整数支持
- Python劣势：执行效率相对较低
- 优化策略：使用生成器避免内存问题，使用C扩展提高性能
"""