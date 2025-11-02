"""
子集 II - Python实现
给定一个整数数组 nums，其中可能包含重复元素，返回所有可能的子集（幂集）。
解集不能包含重复的子集。可以按任意顺序返回解集。

题目链接: https://leetcode.cn/problems/subsets-ii/
难度: 中等

解题思路:
1. 先对数组排序，使相同元素相邻
2. 使用回溯法生成所有子集
3. 通过跳过重复元素避免生成重复子集

时间复杂度: O(n * 2^n) - 共有2^n个子集，每个子集平均长度n
空间复杂度: O(n) - 递归栈深度为n
"""

from typing import List
import time
from functools import lru_cache

class Solution:
    def subsetsWithDup(self, nums: List[int]) -> List[List[int]]:
        """
        主方法：生成所有不重复子集（回溯法）
        
        Args:
            nums: 输入数组，可能包含重复元素
            
        Returns:
            所有不重复子集的列表
        """
        # 边界情况处理
        if not nums:
            return [[]]
        
        # 排序是去重的关键步骤
        nums.sort()
        
        result = []
        self._backtrack(nums, 0, [], result)
        return result
    
    def _backtrack(self, nums: List[int], start: int, current: List[int], result: List[List[int]]) -> None:
        """
        回溯法生成子集
        
        Args:
            nums: 输入数组
            start: 当前处理的起始位置
            current: 当前正在构建的子集
            result: 存储所有子集的结果列表
        """
        # 将当前子集加入结果（包括空集）
        result.append(current[:])  # 使用切片创建副本
        
        # 从start位置开始遍历数组
        for i in range(start, len(nums)):
            # 关键去重逻辑：跳过重复元素
            # 只有当i > start且当前元素等于前一个元素时才跳过
            if i > start and nums[i] == nums[i - 1]:
                continue
            
            # 选择当前元素
            current.append(nums[i])
            
            # 递归处理下一个位置
            self._backtrack(nums, i + 1, current, result)
            
            # 回溯：撤销选择
            current.pop()
    
    def subsetsWithDupBitmask(self, nums: List[int]) -> List[List[int]]:
        """
        状态压缩解法：使用位运算生成子集
        这种方法虽然直观但效率较低，主要用于理解状态压缩思想
        
        Args:
            nums: 输入数组
            
        Returns:
            所有不重复子集的列表
        """
        if not nums:
            return [[]]
        
        nums.sort()
        n = len(nums)
        result = []
        seen = set()
        
        # 枚举所有可能的子集掩码
        for mask in range(1 << n):
            subset = []
            key = []
            
            # 根据掩码生成子集
            for i in range(n):
                if mask & (1 << i):
                    subset.append(nums[i])
                    key.append(str(nums[i]))
            
            # 使用元组键值去重（Python中列表不可哈希，使用元组）
            tuple_key = tuple(key)
            if tuple_key not in seen:
                seen.add(tuple_key)
                result.append(subset)
        
        return result
    
    def subsetsWithDupIterative(self, nums: List[int]) -> List[List[int]]:
        """
        迭代解法：逐步构建子集
        这种方法更高效，适合处理较大规模数据
        
        Args:
            nums: 输入数组
            
        Returns:
            所有不重复子集的列表
        """
        result = [[]]  # 添加空集
        
        if not nums:
            return result
        
        nums.sort()
        
        start_index = 0  # 新元素开始的位置
        size = 0        # 上一轮结果的大小
        
        for i in range(len(nums)):
            # 如果当前元素与前一个元素相同，则只在上轮新生成的子集基础上添加
            # 否则在所有子集基础上添加
            if i > 0 and nums[i] == nums[i - 1]:
                start_index = size
            else:
                start_index = 0
            
            size = len(result)
            # 在当前选定的子集基础上添加新元素
            for j in range(start_index, size):
                new_subset = result[j][:]  # 创建副本
                new_subset.append(nums[i])
                result.append(new_subset)
        
        return result
    
    @lru_cache(maxsize=None)
    def _subsetsWithDupMemo(self, nums_tuple: tuple, start: int) -> List[tuple]:
        """
        记忆化递归解法（教学目的）
        使用记忆化优化重复计算，但实际效果可能不如迭代法
        
        Args:
            nums_tuple: 转换为元组的输入数组（为了可哈希）
            start: 当前起始位置
            
        Returns:
            子集元组列表
        """
        if start == len(nums_tuple):
            return [()]
        
        # 计算跳过重复元素的结束位置
        end = start
        while end < len(nums_tuple) and nums_tuple[end] == nums_tuple[start]:
            end += 1
        
        # 不包含当前元素的子集
        subsets_without = self._subsetsWithDupMemo(nums_tuple, end)
        
        # 包含当前元素的子集
        subsets_with = []
        current_num = nums_tuple[start]
        
        for subset in subsets_without:
            # 添加1个到多个当前元素
            for count in range(1, end - start + 1):
                new_subset = (current_num,) * count + subset
                subsets_with.append(new_subset)
        
        return subsets_without + subsets_with
    
    def subsetsWithDupMemo(self, nums: List[int]) -> List[List[int]]:
        """
        记忆化递归解法入口
        
        Args:
            nums: 输入数组
            
        Returns:
            所有不重复子集的列表
        """
        if not nums:
            return [[]]
        
        nums.sort()
        # 转换为元组以便使用lru_cache
        nums_tuple = tuple(nums)
        result_tuples = self._subsetsWithDupMemo(nums_tuple, 0)
        
        # 转换回列表格式
        return [list(subset) for subset in result_tuples]

def print_subsets(subsets: List[List[int]]) -> None:
    """打印子集结果"""
    print("[", end="")
    for i, subset in enumerate(subsets):
        print("[", end="")
        print(",".join(map(str, subset)), end="")
        print("]", end="")
        if i < len(subsets) - 1:
            print(", ", end="")
    print("]")

def test_subsets_with_dup():
    """单元测试函数"""
    solution = Solution()
    
    print("=== 子集 II Python单元测试 ===")
    
    # 测试用例1: 包含重复元素的数组
    nums1 = [1, 2, 2]
    result1 = solution.subsetsWithDup(nums1)
    print(f"测试用例1 [1,2,2] 的子集数量: {len(result1)}")
    print("子集内容: ", end="")
    print_subsets(result1)
    
    # 验证结果正确性
    assert len(result1) == 6, "子集数量应为6"
    
    # 测试用例2: 不包含重复元素的数组
    nums2 = [1, 2, 3]
    result2 = solution.subsetsWithDup(nums2)
    print(f"测试用例2 [1,2,3] 的子集数量: {len(result2)}")
    assert len(result2) == 8, "子集数量应为8"
    
    # 测试用例3: 空数组
    nums3 = []
    result3 = solution.subsetsWithDup(nums3)
    print(f"测试用例3 [] 的子集数量: {len(result3)}")
    assert len(result3) == 1, "空数组子集数量应为1"
    
    # 测试用例4: 全重复元素
    nums4 = [2, 2, 2]
    result4 = solution.subsetsWithDup(nums4)
    print(f"测试用例4 [2,2,2] 的子集数量: {len(result4)}")
    assert len(result4) == 4, "全重复元素子集数量应为4"
    
    # 性能测试：较大规模数据
    nums5 = [1] * 10  # 填充10个重复元素
    start_time = time.time()
    result5 = solution.subsetsWithDup(nums5)
    end_time = time.time()
    print(f"性能测试: 处理10个重复元素耗时 {(end_time - start_time) * 1000:.2f} 毫秒")
    print(f"生成子集数量: {len(result5)}")
    
    # 测试不同解法的正确性
    print("\n=== 不同解法对比测试 ===")
    result1a = solution.subsetsWithDupBitmask(nums1)
    result1b = solution.subsetsWithDupIterative(nums1)
    result1c = solution.subsetsWithDupMemo(nums1)
    
    print(f"回溯法结果数量: {len(result1)}")
    print(f"位运算法结果数量: {len(result1a)}")
    print(f"迭代法结果数量: {len(result1b)}")
    print(f"记忆化法结果数量: {len(result1c)}")
    
    # 验证四种方法结果一致
    assert len(result1) == len(result1a), "不同解法结果数量应一致"
    assert len(result1) == len(result1b), "不同解法结果数量应一致"
    assert len(result1) == len(result1c), "不同解法结果数量应一致"
    
    # 测试大规模数据性能比较
    print("\n=== 大规模数据性能比较 ===")
    large_nums = [i % 5 for i in range(15)]  # 包含重复元素
    
    # 回溯法性能
    start = time.time()
    result_backtrack = solution.subsetsWithDup(large_nums)
    time_backtrack = time.time() - start
    
    # 迭代法性能
    start = time.time()
    result_iterative = solution.subsetsWithDupIterative(large_nums)
    time_iterative = time.time() - start
    
    # 位运算法性能
    start = time.time()
    result_bitmask = solution.subsetsWithDupBitmask(large_nums)
    time_bitmask = time.time() - start
    
    print(f"回溯法耗时: {time_backtrack:.4f} 秒, 结果数量: {len(result_backtrack)}")
    print(f"迭代法耗时: {time_iterative:.4f} 秒, 结果数量: {len(result_iterative)}")
    print(f"位运算法耗时: {time_bitmask:.4f} 秒, 结果数量: {len(result_bitmask)}")
    
    print("所有测试用例通过!")

if __name__ == "__main__":
    try:
        test_subsets_with_dup()
    except AssertionError as e:
        print(f"测试失败: {e}")
    except Exception as e:
        print(f"发生错误: {e}")

"""
Python实现特点分析：

1. 语言特性利用：
   - 使用列表切片创建副本，避免引用问题
   - 利用元组的可哈希性进行去重
   - 使用装饰器@lru_cache实现记忆化

2. 动态类型优势：
   - 无需声明变量类型，代码更简洁
   - 列表操作非常灵活
   - 内置函数丰富，开发效率高

3. 性能特点：
   - 递归深度限制：Python默认递归深度有限
   - 列表操作效率：append/pop操作高效
   - 内存使用：Python对象开销较大

4. 与Java/C++的差异：
   - 语法更简洁，可读性更强
   - 动态类型 vs 静态类型
   - 垃圾回收机制不同
   - 性能通常低于C++，开发效率高于C++

5. Python特有优化：
   - 使用生成器表达式减少内存使用
   - 利用内置函数提高性能
   - 使用f-string进行字符串格式化

6. 工程化考量：
   - 类型提示提高代码可读性
   - 文档字符串提供API文档
   - 单元测试框架完善
   - 异常处理机制健全

7. 调试和开发：
   - REPL环境便于快速测试
   - 丰富的第三方库支持
   - 调试工具完善

算法实现技巧：
1. 去重策略：排序 + 跳过重复元素
2. 回溯模板：选择 -> 递归 -> 撤销选择
3. 状态压缩：位运算表示子集
4. 记忆化：使用lru_cache优化递归

性能优化建议：
- 对于小规模数据，回溯法足够
- 对于中等规模，迭代法更优
- 对于大规模重复数据，考虑特殊优化
- 注意Python的递归深度限制
"""

# 子集 II (Subsets II)
# 给你一个整数数组 nums ，其中可能包含重复元素，
# 请你返回该数组所有可能的子集（幂集）。
# 解集不能包含重复的子集。返回的解集中，子集可以按任意顺序排列。
# 测试链接 : https://leetcode.cn/problems/subsets-ii/

class Code14_SubsetsII:
    
    # 使用状态压缩解决子集II问题
    # 核心思想：用二进制位表示元素选择状态，通过位运算生成所有子集
    # 时间复杂度: O(n * 2^n)
    # 空间复杂度: O(n)
    @staticmethod
    def subsetsWithDup(nums):
        # 先对数组排序，便于处理重复元素
        nums.sort()
        
        n = len(nums)
        # 用于存储所有不重复的子集
        result_set = set()
        
        # 枚举所有可能的状态（0到2^n-1）
        for mask in range(1 << n):
            subset = []
            # 根据mask生成对应的子集
            for i in range(n):
                # 如果第i位为1，表示选择第i个元素
                if (mask & (1 << i)) != 0:
                    subset.append(nums[i])
            # 将子集添加到集合中（自动去重）
            result_set.add(tuple(subset))
        
        # 将Set转换为List并返回
        return [list(subset) for subset in result_set]
