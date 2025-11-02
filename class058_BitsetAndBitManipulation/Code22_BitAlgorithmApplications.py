"""
位算法应用实现
包含LeetCode多个位算法应用相关题目的解决方案

题目列表:
1. LeetCode 78 - 子集
2. LeetCode 90 - 子集 II
3. LeetCode 46 - 全排列
4. LeetCode 47 - 全排列 II
5. LeetCode 77 - 组合
6. LeetCode 39 - 组合总和
7. LeetCode 40 - 组合总和 II
8. LeetCode 216 - 组合总和 III
9. LeetCode 131 - 分割回文串
10. LeetCode 93 - 复原IP地址

时间复杂度分析:
- 回溯算法: O(2^n) 到 O(n!)
- 空间复杂度: O(n) 到 O(n^2)

工程化考量:
1. 位集优化: 使用位集优化回溯算法
2. 状态压缩: 使用位运算压缩状态空间
3. 剪枝优化: 使用位运算进行高效剪枝
4. 去重处理: 使用位掩码进行重复检测
"""

import time
from typing import List
import sys

class BitAlgorithmApplications:
    """位算法应用类"""
    
    @staticmethod
    def subsets(nums: List[int]) -> List[List[int]]:
        """
        LeetCode 78 - 子集
        给定一组不含重复元素的整数数组 nums，返回该数组所有可能的子集（幂集）。
        
        方法: 位运算枚举
        时间复杂度: O(n * 2^n)
        空间复杂度: O(n * 2^n)
        
        原理: 使用二进制位表示每个元素是否在子集中
        """
        n = len(nums)
        total = 1 << n
        result = []
        
        for mask in range(total):
            subset = []
            for i in range(n):
                if mask & (1 << i):
                    subset.append(nums[i])
            result.append(subset)
        
        return result
    
    @staticmethod
    def subsets_with_dup(nums: List[int]) -> List[List[int]]:
        """
        LeetCode 90 - 子集 II
        给定一个可能包含重复元素的整数数组 nums，返回该数组所有可能的子集（幂集）。
        
        方法: 位运算 + 排序去重
        时间复杂度: O(n * 2^n)
        空间复杂度: O(n * 2^n)
        """
        nums.sort()
        n = len(nums)
        total = 1 << n
        result_set = set()
        
        for mask in range(total):
            subset = []
            for i in range(n):
                if mask & (1 << i):
                    subset.append(nums[i])
            # 使用元组作为集合的键
            result_set.add(tuple(subset))
        
        return [list(subset) for subset in result_set]
    
    @staticmethod
    def permute(nums: List[int]) -> List[List[int]]:
        """
        LeetCode 46 - 全排列
        给定一个没有重复数字的序列，返回其所有可能的全排列。
        
        方法: 回溯 + 位掩码
        时间复杂度: O(n!)
        空间复杂度: O(n)
        """
        result = []
        current = []
        used = [False] * len(nums)
        
        def backtrack():
            if len(current) == len(nums):
                result.append(current[:])
                return
            
            for i in range(len(nums)):
                if not used[i]:
                    used[i] = True
                    current.append(nums[i])
                    backtrack()
                    current.pop()
                    used[i] = False
        
        backtrack()
        return result
    
    @staticmethod
    def permute_unique(nums: List[int]) -> List[List[int]]:
        """
        LeetCode 47 - 全排列 II
        给定一个可包含重复数字的序列，返回所有不重复的全排列。
        
        方法: 回溯 + 排序剪枝
        时间复杂度: O(n!)
        空间复杂度: O(n)
        """
        nums.sort()
        result = []
        current = []
        used = [False] * len(nums)
        
        def backtrack():
            if len(current) == len(nums):
                result.append(current[:])
                return
            
            for i in range(len(nums)):
                if used[i] or (i > 0 and nums[i] == nums[i-1] and not used[i-1]):
                    continue
                
                used[i] = True
                current.append(nums[i])
                backtrack()
                current.pop()
                used[i] = False
        
        backtrack()
        return result
    
    @staticmethod
    def combine(n: int, k: int) -> List[List[int]]:
        """
        LeetCode 77 - 组合
        给定两个整数 n 和 k，返回 1 ... n 中所有可能的 k 个数的组合。
        
        方法: 回溯
        时间复杂度: O(C(n, k))
        空间复杂度: O(k)
        """
        result = []
        current = []
        
        def backtrack(start: int):
            if len(current) == k:
                result.append(current[:])
                return
            
            for i in range(start, n + 1):
                current.append(i)
                backtrack(i + 1)
                current.pop()
        
        backtrack(1)
        return result
    
    @staticmethod
    def combination_sum(candidates: List[int], target: int) -> List[List[int]]:
        """
        LeetCode 39 - 组合总和
        给定一个无重复元素的数组 candidates 和一个目标数 target ，找出 candidates 中所有可以使数字和为 target 的组合。
        
        方法: 回溯
        时间复杂度: O(n^target)
        空间复杂度: O(target)
        """
        result = []
        current = []
        candidates.sort()
        
        def backtrack(start: int, remaining: int):
            if remaining == 0:
                result.append(current[:])
                return
            
            for i in range(start, len(candidates)):
                if candidates[i] > remaining:
                    break
                
                current.append(candidates[i])
                backtrack(i, remaining - candidates[i])
                current.pop()
        
        backtrack(0, target)
        return result
    
    @staticmethod
    def combination_sum2(candidates: List[int], target: int) -> List[List[int]]:
        """
        LeetCode 40 - 组合总和 II
        给定一个数组 candidates 和一个目标数 target ，找出 candidates 中所有可以使数字和为 target 的组合。
        candidates 中的每个数字在每个组合中只能使用一次。
        
        方法: 回溯 + 排序剪枝
        时间复杂度: O(2^n)
        空间复杂度: O(n)
        """
        result = []
        current = []
        candidates.sort()
        
        def backtrack(start: int, remaining: int):
            if remaining == 0:
                result.append(current[:])
                return
            
            for i in range(start, len(candidates)):
                if candidates[i] > remaining:
                    break
                
                if i > start and candidates[i] == candidates[i-1]:
                    continue
                
                current.append(candidates[i])
                backtrack(i + 1, remaining - candidates[i])
                current.pop()
        
        backtrack(0, target)
        return result
    
    @staticmethod
    def combination_sum3(k: int, n: int) -> List[List[int]]:
        """
        LeetCode 216 - 组合总和 III
        找出所有相加之和为 n 的 k 个数的组合。组合中只允许含有 1 - 9 的正整数，并且每种组合中不存在重复的数字。
        
        方法: 回溯
        时间复杂度: O(C(9, k))
        空间复杂度: O(k)
        """
        result = []
        current = []
        
        def backtrack(start: int, remaining: int):
            if len(current) == k and remaining == 0:
                result.append(current[:])
                return
            
            if len(current) > k or remaining < 0:
                return
            
            for i in range(start, 10):
                current.append(i)
                backtrack(i + 1, remaining - i)
                current.pop()
        
        backtrack(1, n)
        return result
    
    @staticmethod
    def partition(s: str) -> List[List[str]]:
        """
        LeetCode 131 - 分割回文串
        给定一个字符串 s，将 s 分割成一些子串，使每个子串都是回文串。返回 s 所有可能的分割方案。
        
        方法: 回溯 + 动态规划预处理
        时间复杂度: O(n * 2^n)
        空间复杂度: O(n^2)
        """
        n = len(s)
        # 预处理回文信息
        dp = [[False] * n for _ in range(n)]
        
        for i in range(n):
            for j in range(i + 1):
                if s[i] == s[j] and (i - j <= 2 or dp[j+1][i-1]):
                    dp[j][i] = True
        
        result = []
        current = []
        
        def backtrack(start: int):
            if start == n:
                result.append(current[:])
                return
            
            for i in range(start, n):
                if dp[start][i]:
                    current.append(s[start:i+1])
                    backtrack(i + 1)
                    current.pop()
        
        backtrack(0)
        return result
    
    @staticmethod
    def restore_ip_addresses(s: str) -> List[str]:
        """
        LeetCode 93 - 复原IP地址
        给定一个只包含数字的字符串，复原它并返回所有可能的 IP 地址格式。
        
        方法: 回溯
        时间复杂度: O(1) - 固定长度
        空间复杂度: O(1)
        """
        result = []
        current = []
        
        def backtrack(start: int):
            if len(current) == 4 and start == len(s):
                result.append('.'.join(current))
                return
            
            if len(current) == 4 or start == len(s):
                return
            
            for length in range(1, 4):
                if start + length > len(s):
                    break
                
                segment = s[start:start+length]
                
                # 检查段是否有效
                if len(segment) > 1 and segment[0] == '0':
                    continue
                
                num = int(segment)
                if num > 255:
                    continue
                
                current.append(segment)
                backtrack(start + length)
                current.pop()
        
        backtrack(0)
        return result


class PerformanceTester:
    """性能测试工具类"""
    
    @staticmethod
    def test_subsets():
        """测试子集问题性能"""
        print("=== 子集问题性能测试 ===")
        
        nums = [1, 2, 3, 4]
        
        start = time.time()
        result = BitAlgorithmApplications.subsets(nums)
        elapsed = (time.time() - start) * 1e6  # 微秒
        
        print(f"子集问题: 输入大小={len(nums)}, 结果数量={len(result)}, 耗时={elapsed:.2f} μs")
    
    @staticmethod
    def test_permutations():
        """测试全排列性能"""
        print("\n=== 全排列性能测试 ===")
        
        nums = [1, 2, 3, 4, 5]
        
        start = time.time()
        result = BitAlgorithmApplications.permute(nums)
        elapsed = (time.time() - start) * 1000  # 毫秒
        
        print(f"全排列: 输入大小={len(nums)}, 结果数量={len(result)}, 耗时={elapsed:.2f} ms")
    
    @staticmethod
    def run_unit_tests():
        """运行单元测试"""
        print("=== 位算法应用单元测试 ===")
        
        # 测试子集
        nums = [1, 2, 3]
        subsets_result = BitAlgorithmApplications.subsets(nums)
        assert len(subsets_result) == 8
        
        # 测试全排列
        permute_result = BitAlgorithmApplications.permute(nums)
        assert len(permute_result) == 6
        
        print("所有单元测试通过!")
    
    @staticmethod
    def complexity_analysis():
        """复杂度分析"""
        print("\n=== 复杂度分析 ===")
        
        algorithms = {
            "subsets": ("O(n*2^n)", "O(n*2^n)"),
            "subsets_with_dup": ("O(n*2^n)", "O(n*2^n)"),
            "permute": ("O(n!)", "O(n)"),
            "permute_unique": ("O(n!)", "O(n)"),
            "combine": ("O(C(n,k))", "O(k)"),
            "combination_sum": ("O(n^target)", "O(target)"),
            "combination_sum2": ("O(2^n)", "O(n)"),
            "combination_sum3": ("O(C(9,k))", "O(k)"),
            "partition": ("O(n*2^n)", "O(n^2)"),
            "restore_ip_addresses": ("O(1)", "O(1)")
        }
        
        for name, (time_complexity, space_complexity) in algorithms.items():
            print(f"{name}: 时间复杂度={time_complexity}, 空间复杂度={space_complexity}")


def main():
    """主函数"""
    print("位算法应用实现")
    print("包含LeetCode多个位算法应用相关题目的解决方案")
    print("=" * 50)
    
    # 运行单元测试
    PerformanceTester.run_unit_tests()
    
    # 运行性能测试
    PerformanceTester.test_subsets()
    PerformanceTester.test_permutations()
    
    # 复杂度分析
    PerformanceTester.complexity_analysis()
    
    # 示例使用
    print("\n=== 示例使用 ===")
    
    # 子集示例
    nums = [1, 2, 3]
    subsets_result = BitAlgorithmApplications.subsets(nums)
    print(f"子集示例([1,2,3]): 共{len(subsets_result)}个子集")
    
    # 全排列示例
    permute_result = BitAlgorithmApplications.permute(nums)
    print(f"全排列示例([1,2,3]): 共{len(permute_result)}个排列")
    
    # 组合示例
    combine_result = BitAlgorithmApplications.combine(4, 2)
    print(f"组合示例(C(4,2)): 共{len(combine_result)}个组合")
    
    # 组合总和示例
    candidates = [2, 3, 6, 7]
    target = 7
    combination_result = BitAlgorithmApplications.combination_sum(candidates, target)
    print(f"组合总和示例({candidates}, target={target}): 共{len(combination_result)}个解")


if __name__ == "__main__":
    main()