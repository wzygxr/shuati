"""
LeetCode 354. 俄罗斯套娃信封问题
给你一个二维整数数组 envelopes ，其中 envelopes[i] = [wi, hi] ，表示第 i 个信封的宽度和高度。
当另一个信封的宽度和高度都比这个信封大的时候，这个信封就可以放进另一个信封里，如同俄罗斯套娃一样。
请计算最多能有多少个信封能组成一组"俄罗斯套娃"信封。
测试链接：https://leetcode.cn/problems/russian-doll-envelopes/

算法详解：
使用排序+LIS（最长递增子序列）方法解决俄罗斯套娃信封问题。

时间复杂度：O(n log n)
空间复杂度：O(n)

工程化考量：
1. 异常处理：检查输入数组有效性
2. 边界处理：单个信封的情况
3. 性能优化：使用贪心+二分查找优化LIS计算
4. 代码质量：清晰的排序逻辑和LIS实现

Python特性：
1. 使用内置排序函数简化代码
2. 列表操作高效但需要注意内存使用
3. 二分查找模块提供高效实现
4. 支持大规模数据处理
"""

from typing import List
import bisect
import time
import random

class RussianDollEnvelopes:
    """
    俄罗斯套娃信封问题解决方案类
    提供多种算法实现和测试功能
    """
    
    @staticmethod
    def max_envelopes_optimal(envelopes: List[List[int]]) -> int:
        """
        最优解法：排序 + LIS（贪心+二分查找）
        时间复杂度：O(n log n)
        空间复杂度：O(n)
        
        Args:
            envelopes: 信封列表，每个信封为[宽度, 高度]
            
        Returns:
            int: 最多能嵌套的信封数量
            
        Raises:
            TypeError: 输入不是列表类型
            ValueError: 信封格式不正确
        """
        if not isinstance(envelopes, list):
            raise TypeError("输入必须是列表类型")
            
        if not envelopes:
            return 0
            
        # 验证信封格式
        for env in envelopes:
            if not isinstance(env, list) or len(env) != 2:
                raise ValueError("每个信封必须是包含两个整数的列表")
            if not all(isinstance(x, int) and x > 0 for x in env):
                raise ValueError("信封的宽度和高度必须是正整数")
                
        n = len(envelopes)
        if n == 1:
            return 1
            
        # 排序：按宽度升序，宽度相同时按高度降序
        # 这样可以将问题转化为在高度序列上寻找最长递增子序列
        envelopes.sort(key=lambda x: (x[0], -x[1]))
        
        # 提取高度序列
        heights = [env[1] for env in envelopes]
        
        # 计算高度序列的最长递增子序列
        return RussianDollEnvelopes.length_of_lis(heights)
    
    @staticmethod
    def max_envelopes_dp(envelopes: List[List[int]]) -> int:
        """
        基础动态规划解法
        时间复杂度：O(n²)
        空间复杂度：O(n)
        """
        if not envelopes:
            return 0
            
        n = len(envelopes)
        if n == 1:
            return 1
            
        # 排序：按宽度升序，宽度相同时按高度升序
        envelopes.sort(key=lambda x: (x[0], x[1]))
        
        # dp[i]表示以第i个信封结尾的最大嵌套数量
        dp = [1] * n
        max_count = 1
        
        for i in range(1, n):
            for j in range(i):
                # 检查信封j是否可以放入信封i
                if (envelopes[i][0] > envelopes[j][0] and 
                    envelopes[i][1] > envelopes[j][1]):
                    dp[i] = max(dp[i], dp[j] + 1)
            max_count = max(max_count, dp[i])
            
        return max_count
    
    @staticmethod
    def max_envelopes_optimized_dp(envelopes: List[List[int]]) -> int:
        """
        优化的动态规划解法
        时间复杂度：O(n²)
        空间复杂度：O(n)
        
        优化点：在宽度相同的情况下，只考虑高度较小的信封
        """
        if not envelopes:
            return 0
            
        n = len(envelopes)
        if n == 1:
            return 1
            
        # 排序：按宽度升序，宽度相同时按高度降序
        envelopes.sort(key=lambda x: (x[0], -x[1]))
        
        dp = [1] * n
        max_count = 1
        
        for i in range(1, n):
            for j in range(i):
                # 由于排序时宽度相同的高度降序，所以只需要检查高度
                if envelopes[i][1] > envelopes[j][1]:
                    dp[i] = max(dp[i], dp[j] + 1)
            max_count = max(max_count, dp[i])
            
        return max_count
    
    @staticmethod
    def length_of_lis(nums: List[int]) -> int:
        """
        计算最长递增子序列长度（贪心+二分查找优化）
        时间复杂度：O(n log n)
        空间复杂度：O(n)
        """
        if not nums:
            return 0
            
        tails = []
        
        for num in nums:
            # 使用二分查找找到插入位置
            pos = bisect.bisect_left(tails, num)
            
            if pos == len(tails):
                # 当前数字大于所有尾部值，扩展序列
                tails.append(num)
            else:
                # 替换第一个大于等于当前数字的位置
                tails[pos] = num
                
        return len(tails)
    
    @staticmethod
    def run_tests() -> None:
        """
        运行单元测试，验证算法的正确性
        """
        print("=== LeetCode 354 俄罗斯套娃信封问题测试 ===\n")
        
        test_cases = [
            # (描述, 输入信封, 期望结果)
            ("基本功能", [[5,4],[6,4],[6,7],[2,3]], 3),
            ("官方示例", [[1,1],[1,1],[1,1]], 1),
            ("单个信封", [[5,4]], 1),
            ("空数组", [], 0),
            ("复杂情况", [[2,3],[5,4],[6,7],[6,4],[7,5]], 3),
            ("尺寸相同", [[1,1],[1,1],[1,1],[1,1]], 1),
            ("递增序列", [[1,1],[2,2],[3,3],[4,4],[5,5]], 5),
            ("递减序列", [[5,5],[4,4],[3,3],[2,2],[1,1]], 1),
        ]
        
        methods = [
            ("最优解法", RussianDollEnvelopes.max_envelopes_optimal),
            ("基础DP", RussianDollEnvelopes.max_envelopes_dp),
            ("优化DP", RussianDollEnvelopes.max_envelopes_optimized_dp),
        ]
        
        all_passed = True
        
        for description, envelopes, expected in test_cases:
            print(f"{description}:")
            print(f"  输入信封: {envelopes}")
            print(f"  期望结果: {expected}")
            
            case_passed = True
            results = []
            
            for method_name, method in methods:
                try:
                    result = method(envelopes)
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
        
        # 生成测试数据：大规模信封数组
        n = 10000
        envelopes = []
        for i in range(n):
            width = random.randint(1, 1000)
            height = random.randint(1, 1000)
            envelopes.append([width, height])
            
        print(f"测试数据规模: {n}个信封")
        
        methods = [
            ("最优解法", RussianDollEnvelopes.max_envelopes_optimal),
        ]
        
        # 对于小规模数据，也测试DP算法
        if n <= 1000:
            methods.append(("优化DP", RussianDollEnvelopes.max_envelopes_optimized_dp))
        
        results = {}
        
        for method_name, method in methods:
            start_time = time.time()
            result = method(envelopes)
            end_time = time.time()
            duration = (end_time - start_time) * 1000  # 转换为毫秒
            
            results[method_name] = result
            
            print(f"{method_name}:")
            print(f"  结果: {result}")
            print(f"  耗时: {duration:.2f} 毫秒")
            print()
        
        # 验证结果一致性（如果有多个方法）
        if len(results) > 1:
            if len(set(results.values())) == 1:
                print("结果一致性验证: 通过 ✓")
            else:
                print("结果一致性验证: 失败 ✗")
        
        # 测试更大规模数据（仅最优解法）
        print("大规模数据测试（最优解法）:")
        n_large = 50000
        envelopes_large = []
        for i in range(n_large):
            width = random.randint(1, 10000)
            height = random.randint(1, 10000)
            envelopes_large.append([width, height])
            
        start_time = time.time()
        result_large = RussianDollEnvelopes.max_envelopes_optimal(envelopes_large)
        end_time = time.time()
        duration_large = (end_time - start_time) * 1000
        
        print(f"  数据规模: {n_large}个信封")
        print(f"  结果: {result_large}")
        print(f"  耗时: {duration_large:.2f} 毫秒")
        print("  注意：最优解法可以高效处理大规模数据")


def main():
    """
    主函数，运行测试和性能测试
    """
    try:
        # 运行单元测试
        RussianDollEnvelopes.run_tests()
        
        # 运行性能测试
        RussianDollEnvelopes.performance_test()
        
        print("所有测试完成！")
        
    except Exception as e:
        print(f"测试过程中发生错误: {e}")
        return 1
        
    return 0


if __name__ == "__main__":
    exit(main())


"""
复杂度分析详细计算：

最优解法（排序+LIS）：
- 时间：排序O(n log n) + LIS计算O(n log n) → O(n log n)
- 空间：排序O(log n) + LIS数组O(n) → O(n)

基础动态规划：
- 时间：排序O(n log n) + 双重循环O(n²) → O(n²)
- 空间：排序O(log n) + dp数组O(n) → O(n)

优化动态规划：
- 时间：O(n²)，但实际运行可能稍快
- 空间：O(n)

Python特性说明：
1. 使用lambda表达式简化排序逻辑
2. bisect模块提供高效的二分查找实现
3. 列表推导式简化代码
4. 动态类型使得代码灵活但需要更多测试

调试技巧：
1. 打印排序后的信封序列：
   def print_sorted_envelopes(envelopes):
       print("排序后的信封:")
       for i, env in enumerate(envelopes):
           print(f"  {i}: [{env[0]}, {env[1]}]")

2. 观察LIS计算过程：
   def print_lis_process(nums, tails):
       print("LIS计算过程:")
       print(f"  当前数字: {nums}")
       print(f"  tails数组: {tails}")

3. 使用小规模测试用例验证正确性

工程化建议：
1. 对于生产环境使用最优解法
2. 添加详细的输入验证和错误处理
3. 编写全面的单元测试
4. 使用类型注解提高代码可读性
5. 对于超大规模数据考虑使用C++扩展
"""