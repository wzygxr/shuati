"""
LeetCode 474. 一和零
给你一个二进制字符串数组 strs 和两个整数 m 和 n 。
请你找出并返回 strs 的最大子集的大小，该子集中最多有 m 个 0 和 n 个 1 。
测试链接：https://leetcode.cn/problems/ones-and-zeroes/

算法详解：
将问题转化为二维费用的0-1背包问题，使用动态规划求解。

时间复杂度：O(len * m * n + L)，其中L是所有字符串总长度
空间复杂度：O(m * n)（优化版本）

工程化考量：
1. 异常处理：检查输入参数有效性
2. 边界处理：m或n为0的情况
3. 性能优化：使用空间优化技术
4. 代码质量：清晰的变量命名和状态转移逻辑

Python特性：
1. 动态类型使得代码简洁
2. 列表操作高效但需要注意内存使用
3. 内置函数简化代码
4. 支持大规模数据处理
"""

from typing import List
import time
import random

class OnesAndZeroes:
    """
    一和零问题解决方案类
    提供多种算法实现和测试功能
    """
    
    @staticmethod
    def find_max_form_optimized(strs: List[str], m: int, n: int) -> int:
        """
        空间优化版本（二维DP）
        时间复杂度：O(len * m * n + L)
        空间复杂度：O(m * n)
        
        Args:
            strs: 二进制字符串数组
            m: 最大0的数量
            n: 最大1的数量
            
        Returns:
            int: 最大子集大小
            
        Raises:
            TypeError: 输入参数类型错误
            ValueError: 参数值无效
        """
        if not isinstance(strs, list):
            raise TypeError("strs必须是列表类型")
        if not isinstance(m, int) or not isinstance(n, int):
            raise TypeError("m和n必须是整数")
        if m < 0 or n < 0:
            raise ValueError("m和n必须是非负整数")
            
        if not strs:
            return 0
            
        # dp[i][j]表示使用i个0和j个1时的最大子集大小
        dp = [[0] * (n + 1) for _ in range(m + 1)]
        
        for s in strs:
            # 统计当前字符串的0和1数量
            zeroes = s.count('0')
            ones = len(s) - zeroes
            
            # 从后往前更新，避免重复使用
            for i in range(m, zeroes - 1, -1):
                for j in range(n, ones - 1, -1):
                    dp[i][j] = max(dp[i][j], dp[i - zeroes][j - ones] + 1)
                    
        return dp[m][n]
    
    @staticmethod
    def find_max_form_with_pruning(strs: List[str], m: int, n: int) -> int:
        """
        带剪枝的优化版本
        时间复杂度：O(len * m * n + L)
        空间复杂度：O(m * n)
        """
        if not strs or m < 0 or n < 0:
            return 0
            
        # 按字符串长度排序（短字符串优先）
        strs_sorted = sorted(strs, key=len)
        
        dp = [[0] * (n + 1) for _ in range(m + 1)]
        
        for s in strs_sorted:
            zeroes = s.count('0')
            ones = len(s) - zeroes
            
            # 剪枝：如果0或1数量超过限制，跳过该字符串
            if zeroes > m or ones > n:
                continue
                
            for i in range(m, zeroes - 1, -1):
                for j in range(n, ones - 1, -1):
                    dp[i][j] = max(dp[i][j], dp[i - zeroes][j - ones] + 1)
                    
        return dp[m][n]
    
    @staticmethod
    def find_max_form_early_count(strs: List[str], m: int, n: int) -> int:
        """
        提前统计所有字符串的0和1数量
        时间复杂度：O(len * m * n + L)
        空间复杂度：O(m * n + len)
        """
        if not strs or m < 0 or n < 0:
            return 0
            
        # 提前统计所有字符串的0和1数量
        counts = []
        for s in strs:
            zeroes = s.count('0')
            ones = len(s) - zeroes
            counts.append((zeroes, ones))
            
        dp = [[0] * (n + 1) for _ in range(m + 1)]
        
        for zeroes, ones in counts:
            for i in range(m, zeroes - 1, -1):
                for j in range(n, ones - 1, -1):
                    dp[i][j] = max(dp[i][j], dp[i - zeroes][j - ones] + 1)
                    
        return dp[m][n]
    
    @staticmethod
    def run_tests() -> None:
        """
        运行单元测试，验证算法的正确性
        """
        print("=== LeetCode 474 一和零问题测试 ===\n")
        
        test_cases = [
            # (描述, 输入字符串, m, n, 期望结果)
            ("基本功能", ["10", "0001", "111001", "1", "0"], 5, 3, 4),
            ("官方示例", ["10", "0", "1"], 1, 1, 2),
            ("m为0", ["10", "0", "1"], 0, 1, 1),
            ("空数组", [], 5, 3, 0),
            ("全相同", ["1", "1", "1", "1"], 3, 3, 3),
            ("大数限制", ["10", "0001", "111001", "1", "0"], 10, 10, 5),
            ("小数限制", ["10", "0001", "111001", "1", "0"], 1, 1, 2),
        ]
        
        methods = [
            ("优化DP", OnesAndZeroes.find_max_form_optimized),
            ("剪枝优化", OnesAndZeroes.find_max_form_with_pruning),
            ("提前统计", OnesAndZeroes.find_max_form_early_count),
        ]
        
        all_passed = True
        
        for description, strs, m, n, expected in test_cases:
            print(f"{description}:")
            print(f"  输入字符串: {strs}")
            print(f"  m = {m}, n = {n}")
            print(f"  期望结果: {expected}")
            
            case_passed = True
            results = []
            
            for method_name, method in methods:
                try:
                    result = method(strs, m, n)
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
        
        # 生成测试数据：大规模字符串数组
        len_strs = 100
        str_len = 10
        strs = []
        for i in range(len_strs):
            s = ''.join(random.choice('01') for _ in range(str_len))
            strs.append(s)
            
        m, n = 50, 50
        
        print(f"测试数据规模: {len_strs}个字符串")
        print(f"每个字符串长度: {str_len}")
        print(f"m = {m}, n = {n}")
        print()
        
        methods = [
            ("优化DP", OnesAndZeroes.find_max_form_optimized),
            ("剪枝优化", OnesAndZeroes.find_max_form_with_pruning),
            ("提前统计", OnesAndZeroes.find_max_form_early_count),
        ]
        
        results = {}
        
        for method_name, method in methods:
            start_time = time.time()
            result = method(strs, m, n)
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
            
        # 测试更大规模数据
        print("大规模数据测试（优化DP版本）:")
        len_large = 500
        str_len_large = 20
        strs_large = []
        for i in range(len_large):
            s = ''.join(random.choice('01') for _ in range(str_len_large))
            strs_large.append(s)
            
        m_large, n_large = 100, 100
        
        start_time = time.time()
        result_large = OnesAndZeroes.find_max_form_optimized(strs_large, m_large, n_large)
        end_time = time.time()
        duration_large = (end_time - start_time) * 1000
        
        print(f"  数据规模: {len_large}个字符串")
        print(f"  每个字符串长度: {str_len_large}")
        print(f"  m = {m_large}, n = {n_large}")
        print(f"  结果: {result_large}")
        print(f"  耗时: {duration_large:.2f} 毫秒")
        print("  注意：优化DP版本可以高效处理大规模数据")


def main():
    """
    主函数，运行测试和性能测试
    """
    try:
        # 运行单元测试
        OnesAndZeroes.run_tests()
        
        # 运行性能测试
        OnesAndZeroes.performance_test()
        
        print("所有测试完成！")
        
    except Exception as e:
        print(f"测试过程中发生错误: {e}")
        return 1
        
    return 0


if __name__ == "__main__":
    exit(main())


"""
复杂度分析详细计算：

优化DP版本：
- 时间：遍历字符串O(len) + 统计0/1数量O(L) + DP计算O(len * m * n) → O(len * m * n + L)
- 空间：二维列表大小m×n → O(m * n)

剪枝优化版本：
- 时间：排序O(len log len) + 其他操作相同 → O(len * m * n + L + len log len)
- 空间：O(m * n)

提前统计版本：
- 时间：O(len * m * n + L)
- 空间：O(m * n + len) 用于存储统计结果

Python特性说明：
1. 使用字符串的count方法高效统计0和1数量
2. 列表推导式创建二维数组
3. 内置排序函数简化代码
4. 动态类型使得代码灵活但需要更多测试

调试技巧：
1. 打印每个字符串的0和1数量：
   def print_counts(strs):
       for i, s in enumerate(strs):
           zeroes = s.count('0')
           ones = len(s) - zeroes
           print(f"  字符串{i}: '{s}' -> 0:{zeroes}, 1:{ones}")

2. 观察dp表的填充过程：
   def print_dp_table(dp, m, n):
       print("DP表:")
       for i in range(m + 1):
           for j in range(n + 1):
               print(f"  dp[{i}][{j}] = {dp[i][j]}")

3. 使用小规模测试用例验证正确性

工程化建议：
1. 对于生产环境使用优化DP版本
2. 添加详细的输入验证和错误处理
3. 编写全面的单元测试
4. 使用类型注解提高代码可读性
5. 对于超大规模数据考虑使用C++扩展
"""