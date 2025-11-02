"""
LeetCode 334. 递增的三元子序列
给你一个整数数组 nums ，判断这个数组中是否存在长度为 3 的递增子序列。
如果存在这样的三元组下标 (i, j, k) 且满足 i < j < k ，使得 nums[i] < nums[j] < nums[k] ，返回 true ；否则，返回 false 。
测试链接：https://leetcode.cn/problems/increasing-triplet-subsequence/

算法详解：
使用贪心思想判断是否存在递增三元组，时间复杂度O(n)，空间复杂度O(1)。

工程化考量：
1. 异常处理：检查输入数组有效性
2. 边界处理：数组长度小于3的情况
3. 性能优化：提前终止遍历
4. 代码质量：清晰的变量命名和类型注解
5. 单元测试：覆盖各种边界情况

Python特性：
1. 动态类型使得代码简洁
2. 使用float('inf')表示无穷大
3. 列表推导式创建测试数据
4. 内置函数简化代码
"""

from typing import List
import time
import random

class IncreasingTriplet:
    """
    递增三元子序列判断类
    提供多种算法实现和测试功能
    """
    
    @staticmethod
    def increasing_triplet_greedy(nums: List[int]) -> bool:
        """
        贪心算法解法（最优解）
        时间复杂度：O(n)
        空间复杂度：O(1)
        
        Args:
            nums: 整数数组
            
        Returns:
            bool: 是否存在递增三元组
            
        Raises:
            TypeError: 输入不是列表类型
        """
        if not isinstance(nums, list):
            raise TypeError("输入必须是列表类型")
            
        if len(nums) < 3:
            return False
            
        # 使用float('inf')表示无穷大
        first = float('inf')  # 当前最小值
        second = float('inf') # 当前次小值（比first大）
        
        for num in nums:
            if num <= first:
                # 更新最小值
                first = num
            elif num <= second:
                # 更新次小值
                second = num
            else:
                # 找到比second大的数，存在递增三元组
                return True
                
        return False
    
    @staticmethod
    def increasing_triplet_dp(nums: List[int]) -> bool:
        """
        动态规划解法
        时间复杂度：O(n²)
        空间复杂度：O(n)
        
        可以扩展到判断任意长度的递增子序列
        """
        if not isinstance(nums, list):
            raise TypeError("输入必须是列表类型")
            
        n = len(nums)
        if n < 3:
            return False
            
        # dp[i]表示以nums[i]结尾的最长递增子序列长度
        dp = [1] * n
        
        for i in range(1, n):
            for j in range(i):
                if nums[j] < nums[i]:
                    dp[i] = max(dp[i], dp[j] + 1)
                    if dp[i] >= 3:
                        return True  # 提前终止
                        
        return False
    
    @staticmethod
    def increasing_triplet_binary_search(nums: List[int]) -> bool:
        """
        二分查找解法（LIS思想）
        时间复杂度：O(n log k)，其中k≤3 → O(n)
        空间复杂度：O(k) → O(1)
        """
        if not isinstance(nums, list):
            raise TypeError("输入必须是列表类型")
            
        n = len(nums)
        if n < 3:
            return False
            
        # tails数组：tails[i]表示长度为i+1的递增子序列的最小末尾元素
        tails = [float('inf')] * 3
        length = 0  # 当前最长递增子序列长度
        
        for num in nums:
            # 二分查找找到num在tails中的位置
            left, right = 0, length
            while left < right:
                mid = (left + right) // 2
                if tails[mid] < num:
                    left = mid + 1
                else:
                    right = mid
                    
            tails[left] = num
            if left == length:
                length += 1
                if length >= 3:
                    return True
                    
        return False
    
    @staticmethod
    def run_tests() -> None:
        """
        运行单元测试，验证算法的正确性
        """
        print("=== LeetCode 334 递增的三元子序列测试 ===\n")
        
        test_cases = [
            # (描述, 输入数组, 期望结果)
            ("严格递增", [1, 2, 3, 4, 5], True),
            ("严格递减", [5, 4, 3, 2, 1], False),
            ("非连续递增", [2, 1, 5, 0, 4, 6], True),
            ("长度小于3", [1, 2], False),
            ("空数组", [], False),
            ("全部重复", [1, 1, 1, 1, 1], False),
            ("复杂情况", [5, 1, 6, 2, 7, 3, 8], True),
            ("刚好存在", [1, 2, 0, 3], True),
            ("边界情况1", [1, 2, -1, 3], True),
            ("边界情况2", [5, 4, 3, 2, 1, 6], True),
        ]
        
        methods = [
            ("贪心算法", IncreasingTriplet.increasing_triplet_greedy),
            ("动态规划", IncreasingTriplet.increasing_triplet_dp),
            ("二分查找", IncreasingTriplet.increasing_triplet_binary_search),
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
        
        # 生成大规模测试数据
        n = 10000
        nums = [random.randint(1, 1000000) for _ in range(n)]
        
        methods = [
            ("贪心算法", IncreasingTriplet.increasing_triplet_greedy),
            ("二分查找", IncreasingTriplet.increasing_triplet_binary_search),
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
        
        # 动态规划在大规模数据下性能较差，单独测试小规模数据
        small_nums = [random.randint(1, 1000) for _ in range(100)]
        start_time = time.time()
        dp_result = IncreasingTriplet.increasing_triplet_dp(small_nums)
        end_time = time.time()
        dp_duration = (end_time - start_time) * 1000
        
        print("动态规划（小规模数据测试）:")
        print(f"  结果: {dp_result}")
        print(f"  耗时: {dp_duration:.2f} 毫秒")
        print("  注意：动态规划在大规模数据下性能较差")
        print()
        
        # 验证结果一致性
        if len(set(results.values())) == 1:
            print("结果一致性验证: 通过 ✓")
        else:
            print("结果一致性验证: 失败 ✗")


def main():
    """
    主函数，运行测试和性能测试
    """
    try:
        # 运行单元测试
        IncreasingTriplet.run_tests()
        
        # 运行性能测试
        IncreasingTriplet.performance_test()
        
        print("所有测试完成！")
        
    except Exception as e:
        print(f"测试过程中发生错误: {e}")
        return 1
        
    return 0


if __name__ == "__main__":
    exit(main())


"""
复杂度分析详细计算：

贪心算法（最优解）：
- 时间：单次遍历数组O(n)，每次操作O(1) → O(n)
- 空间：使用常数个变量 → O(1)
- 最优解确认：是，因为必须遍历整个数组才能确定结果

动态规划：
- 时间：外层循环n次，内层循环n次 → O(n²)
- 空间：dp列表大小n → O(n)
- 最优解：否，时间复杂度较高

二分查找：
- 时间：遍历数组n次，每次二分查找O(log k)其中k≤3 → O(n)
- 空间：tails列表大小3 → O(1)
- 最优解：是，但贪心算法更简洁

Python特性说明：
1. 使用float('inf')表示无穷大，比sys.maxsize更直观
2. 列表推导式生成测试数据非常方便
3. 动态类型使得代码简洁但需要更多测试
4. 使用类型注解提高代码可读性

调试技巧：
1. 打印first和second的实时变化：
   def increasing_triplet_debug(nums):
       first = float('inf')
       second = float('inf')
       for i, num in enumerate(nums):
           print(f"索引{i}: num={num}, first={first}, second={second}")
           if num <= first:
               first = num
           elif num <= second:
               second = num
           else:
               print("找到三元组！")
               return True
       return False

2. 使用断言验证关键假设：
   assert first <= second, "first应该小于等于second"

3. 边界测试：
   - 空数组：[] → False
   - 单元素：[1] → False  
   - 双元素：[1,2] → False
   - 三元素：[1,2,3] → True

工程化建议：
1. 对于生产环境使用贪心算法
2. 添加详细的日志记录
3. 编写全面的单元测试
4. 考虑使用mypy进行静态类型检查
"""