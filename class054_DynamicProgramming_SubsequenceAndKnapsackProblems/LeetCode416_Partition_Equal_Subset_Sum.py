"""
LeetCode 416. 分割等和子集
给你一个只包含正整数的非空数组 nums 。请你判断是否可以将这个数组分割成两个子集，使得两个子集的元素和相等。
测试链接：https://leetcode.cn/problems/partition-equal-subset-sum/

算法详解：
将问题转化为0-1背包问题，使用动态规划求解。

时间复杂度：O(n * target)，其中target = sum/2
空间复杂度：O(target)（优化版本）

工程化考量：
1. 异常处理：检查输入数组有效性
2. 边界处理：总和为奇数的情况直接返回false
3. 性能优化：使用空间优化技术
4. 代码质量：清晰的变量命名和类型注解

Python特性：
1. 动态类型使得代码简洁
2. 列表操作高效但需要注意内存使用
3. 内置函数简化代码
4. 支持大整数处理
"""

from typing import List
import time
import random

class PartitionEqualSubsetSum:
    """
    分割等和子集解决方案类
    提供多种算法实现和测试功能
    """
    
    @staticmethod
    def can_partition_basic(nums: List[int]) -> bool:
        """
        基础动态规划解法
        时间复杂度：O(n * target)
        空间复杂度：O(n * target)
        
        Args:
            nums: 输入数组，只包含正整数
            
        Returns:
            bool: 是否可以分割成两个和相等的子集
            
        Raises:
            TypeError: 输入不是列表类型
            ValueError: 数组包含非正整数
        """
        if not isinstance(nums, list):
            raise TypeError("输入必须是列表类型")
            
        if not nums:
            return False
            
        # 验证所有元素都是正整数
        for num in nums:
            if not isinstance(num, int) or num <= 0:
                raise ValueError("数组元素必须是正整数")
                
        n = len(nums)
        total = sum(nums)
        
        # 总和为奇数，不可能分割
        if total % 2 != 0:
            return False
            
        target = total // 2
        
        # 检查最大元素
        max_num = max(nums)
        if max_num > target:
            return False
            
        # 创建dp表
        # dp[i][j]表示前i个元素能否组成和为j
        dp = [[False] * (target + 1) for _ in range(n + 1)]
        dp[0][0] = True  # 前0个元素和为0总是可能
        
        for i in range(1, n + 1):
            num = nums[i - 1]
            for j in range(target + 1):
                if j < num:
                    dp[i][j] = dp[i - 1][j]
                else:
                    dp[i][j] = dp[i - 1][j] or dp[i - 1][j - num]
                    
        return dp[n][target]
    
    @staticmethod
    def can_partition_optimized(nums: List[int]) -> bool:
        """
        空间优化版本（使用一维数组）
        时间复杂度：O(n * target)
        空间复杂度：O(target)
        """
        if not isinstance(nums, list):
            raise TypeError("输入必须是列表类型")
            
        if not nums:
            return False
            
        for num in nums:
            if not isinstance(num, int) or num <= 0:
                raise ValueError("数组元素必须是正整数")
                
        total = sum(nums)
        
        if total % 2 != 0:
            return False
            
        target = total // 2
        
        max_num = max(nums)
        if max_num > target:
            return False
            
        # 使用一维数组
        dp = [False] * (target + 1)
        dp[0] = True  # 和为0总是可能
        
        for num in nums:
            # 从后往前更新，避免重复使用
            for j in range(target, num - 1, -1):
                dp[j] = dp[j] or dp[j - num]
                
            # 提前终止
            if dp[target]:
                return True
                
        return dp[target]
    
    @staticmethod
    def can_partition_bitmask(nums: List[int]) -> bool:
        """
        位运算优化版本
        时间复杂度：O(n * target)
        空间复杂度：O(target/32) ≈ O(target)
        
        使用Python的整数作为位掩码，每个bit表示一个和是否可达
        """
        if not isinstance(nums, list):
            raise TypeError("输入必须是列表类型")
            
        if not nums:
            return False
            
        for num in nums:
            if not isinstance(num, int) or num <= 0:
                raise ValueError("数组元素必须是正整数")
                
        total = sum(nums)
        
        if total % 2 != 0:
            return False
            
        target = total // 2
        
        max_num = max(nums)
        if max_num > target:
            return False
            
        # 使用整数作为位掩码
        # 初始状态：只有和为0可达（第0位为1）
        bitmask = 1
        
        for num in nums:
            # 将当前位掩码左移num位，然后与原始位掩码进行或操作
            # 这相当于将当前元素加到所有可达的和上
            bitmask |= (bitmask << num)
            
            # 检查target是否可达（第target位是否为1）
            if (bitmask >> target) & 1:
                return True
                
        return (bitmask >> target) & 1 == 1
    
    @staticmethod
    def can_partition_early_stop(nums: List[int]) -> bool:
        """
        带提前终止的优化版本
        在空间优化版本基础上添加更多提前终止条件
        """
        if not isinstance(nums, list) or not nums:
            return False
            
        total = sum(nums)
        
        if total % 2 != 0:
            return False
            
        target = total // 2
        
        # 按从大到小排序，可以更快达到target
        nums_sorted = sorted(nums, reverse=True)
        
        # 检查最大元素
        if nums_sorted[0] > target:
            return False
            
        dp = [False] * (target + 1)
        dp[0] = True
        
        current_max = 0  # 当前可达的最大和
        
        for num in nums_sorted:
            # 只更新从current_max到num的范围
            end = min(target, current_max + num)
            for j in range(end, num - 1, -1):
                if not dp[j] and dp[j - num]:
                    dp[j] = True
                    current_max = max(current_max, j)
                    
            if dp[target]:
                return True
                
        return False
    
    @staticmethod
    def run_tests() -> None:
        """
        运行单元测试，验证算法的正确性
        """
        print("=== LeetCode 416 分割等和子集测试 ===\n")
        
        test_cases = [
            # (描述, 输入数组, 期望结果)
            ("基本功能", [1, 5, 11, 5], True),
            ("官方示例", [1, 2, 3, 5], False),
            ("总和奇数", [1, 2, 3, 4, 5], False),
            ("单个元素", [1], False),
            ("空数组", [], False),
            ("两个相同", [2, 2], True),
            ("复杂情况", [1, 2, 3, 4, 5, 6, 7], True),
            ("大数情况", [100, 100, 100, 100, 100], True),
            ("无法分割", [1, 2, 5], False),
        ]
        
        methods = [
            ("基础DP", PartitionEqualSubsetSum.can_partition_basic),
            ("优化DP", PartitionEqualSubsetSum.can_partition_optimized),
            ("位运算", PartitionEqualSubsetSum.can_partition_bitmask),
            ("提前终止", PartitionEqualSubsetSum.can_partition_early_stop),
        ]
        
        all_passed = True
        
        for description, nums, expected in test_cases:
            print(f"{description}:")
            print(f"  输入数组: {nums}")
            print(f"  期望结果: {expected}")
            
            case_passed = True
            results = []
            
            for method_name, method in methods:
                try:
                    result = method(nums)
                    results.append(result)
                    status = "✓" if result == expected else "✗"
                    print(f"  {method_name}: {result} {status}")
                    
                    if result != expected:
                        case_passed = False
                        all_passed = False
                except Exception as e:
                    print(f"  {method_name}: 错误 - {e}")
                    case_passed = False
                    all_passed = False
            
            # 检查所有方法结果是否一致
            if len(set(results)) == 1 and case_passed:
                print("  结果一致性: 通过 ✓")
            else:
                print("  结果一致性: 失败 ✗")
                all_passed = False
                
            print()
            
        if all_passed:
            print("所有测试通过！ ✓")
        else:
            print("部分测试失败！ ✗")
            
        print()
    
    @staticmethod
    def performance_test() -> None:
        """
        性能测试，测试算法在大规模数据下的表现
        """
        print("=== 性能测试 ===")
        
        # 生成测试数据：中等规模数组
        n = 100
        nums = [random.randint(1, 100) for _ in range(n)]
        
        # 确保总和为偶数
        total = sum(nums)
        if total % 2 != 0:
            nums[0] += 1
            total += 1
            
        target = total // 2
        
        print(f"测试数据规模: {n}个元素")
        print(f"目标总和: {target}")
        print()
        
        methods = [
            ("优化DP", PartitionEqualSubsetSum.can_partition_optimized),
            ("位运算", PartitionEqualSubsetSum.can_partition_bitmask),
            ("提前终止", PartitionEqualSubsetSum.can_partition_early_stop),
        ]
        
        results = {}
        
        for method_name, method in methods:
            start_time = time.time()
            result = method(nums)
            end_time = time.time()
            duration = (end_time - start_time) * 1000  # 转换为毫秒
            
            results[method_name] = result
            
            print(f"{method_name}:")
            print(f"  结果: {result}")
            print(f"  耗时: {duration:.2f} 毫秒")
            print()
        
        # 验证结果一致性
        if len(set(results.values())) == 1:
            print("结果一致性验证: 通过 ✓")
        else:
            print("结果一致性验证: 失败 ✗")
            
        # 测试大规模数据（使用位运算版本）
        print("大规模数据测试（位运算版本）:")
        n_large = 500
        nums_large = [random.randint(1, 1000) for _ in range(n_large)]
        
        total_large = sum(nums_large)
        if total_large % 2 != 0:
            nums_large[0] += 1
            
        start_time = time.time()
        result_large = PartitionEqualSubsetSum.can_partition_bitmask(nums_large)
        end_time = time.time()
        duration_large = (end_time - start_time) * 1000
        
        print(f"  数据规模: {n_large}个元素")
        print(f"  结果: {result_large}")
        print(f"  耗时: {duration_large:.2f} 毫秒")
        print("  注意：位运算版本可以处理更大规模的数据")


def main():
    """
    主函数，运行测试和性能测试
    """
    try:
        # 运行单元测试
        PartitionEqualSubsetSum.run_tests()
        
        # 运行性能测试
        PartitionEqualSubsetSum.performance_test()
        
        print("所有测试完成！")
        
    except Exception as e:
        print(f"测试过程中发生错误: {e}")
        return 1
        
    return 0


if __name__ == "__main__":
    exit(main())


"""
复杂度分析详细计算：

基础动态规划：
- 时间：计算总和O(n) + 填充dp表O(n * target) → O(n * target)
- 空间：二维列表大小n×target → O(n * target)

空间优化版本：
- 时间：O(n * target)
- 空间：一维列表大小target → O(target)

位运算版本：
- 时间：O(n * target)
- 空间：整数位掩码，每个bit表示一个和 → O(target/32) ≈ O(target)

提前终止版本：
- 时间：O(n * target)，但实际运行可能更快
- 空间：O(target)

Python特性说明：
1. 使用整数作为位掩码非常高效
2. 列表操作简洁但需要注意内存使用
3. 内置函数sum、max等简化代码
4. 动态类型使得代码灵活但需要更多测试

调试技巧：
1. 打印dp表观察填充过程：
   def print_dp_table(dp, target):
       print("DP表:")
       for j in range(target + 1):
           if dp[j]:
               print(f"  和{j}: 可达")
           else:
               print(f"  和{j}: 不可达")

2. 使用小规模测试用例验证正确性
3. 添加断言验证关键假设

工程化建议：
1. 对于生产环境使用优化DP版本
2. 对于大规模数据使用位运算版本
3. 添加详细的日志记录
4. 编写全面的单元测试
5. 使用类型注解提高代码可读性
"""