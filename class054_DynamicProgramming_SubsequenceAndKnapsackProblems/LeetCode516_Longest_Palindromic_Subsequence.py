"""
LeetCode 516. 最长回文子序列
给你一个字符串 s ，找出其中最长的回文子序列，并返回该序列的长度。
子序列定义为：不改变剩余字符顺序的情况下，删除某些字符或者不删除任何字符形成的一个序列。
测试链接：https://leetcode.cn/problems/longest-palindromic-subsequence/

算法详解：
使用动态规划求解最长回文子序列问题。

时间复杂度：O(n²)
空间复杂度：O(n²) 或 O(n)（优化版本）

工程化考量：
1. 异常处理：检查输入字符串有效性
2. 边界处理：单字符字符串的情况
3. 性能优化：使用空间优化技术
4. 代码质量：清晰的变量命名和类型注解

Python特性：
1. 动态类型使得代码简洁
2. 列表推导式创建二维数组
3. 内置函数简化代码
4. 支持大字符串处理
"""

from typing import List
import time
import random

class LongestPalindromicSubsequence:
    """
    最长回文子序列解决方案类
    提供多种算法实现和测试功能
    """
    
    @staticmethod
    def longest_palindrome_subseq_basic(s: str) -> int:
        """
        基础动态规划解法
        时间复杂度：O(n²)
        空间复杂度：O(n²)
        
        Args:
            s: 输入字符串
            
        Returns:
            int: 最长回文子序列长度
            
        Raises:
            TypeError: 输入不是字符串类型
        """
        if not isinstance(s, str):
            raise TypeError("输入必须是字符串类型")
            
        n = len(s)
        if n == 0:
            return 0
        if n == 1:
            return 1
            
        # 创建dp表
        dp = [[0] * n for _ in range(n)]
        
        # 初始化：单个字符都是回文
        for i in range(n):
            dp[i][i] = 1
            
        # 从长度为2的子串开始计算
        for length in range(2, n + 1):
            for i in range(n - length + 1):
                j = i + length - 1
                
                if s[i] == s[j]:
                    if length == 2:
                        dp[i][j] = 2
                    else:
                        dp[i][j] = dp[i + 1][j - 1] + 2
                else:
                    dp[i][j] = max(dp[i + 1][j], dp[i][j - 1])
                    
        return dp[0][n - 1]
    
    @staticmethod
    def longest_palindrome_subseq_optimized(s: str) -> int:
        """
        空间优化版本（使用一维数组）
        时间复杂度：O(n²)
        空间复杂度：O(n)
        """
        if not isinstance(s, str):
            raise TypeError("输入必须是字符串类型")
            
        n = len(s)
        if n == 0:
            return 0
        if n == 1:
            return 1
            
        # 使用一维数组存储状态
        dp = [1] * n  # 初始化每个字符自身都是回文
        
        for i in range(n - 2, -1, -1):
            prev = 0  # 保存dp[i+1][j-1]的值
            for j in range(i + 1, n):
                temp = dp[j]  # 保存当前值
                
                if s[i] == s[j]:
                    dp[j] = prev + 2
                else:
                    dp[j] = max(dp[j], dp[j - 1])
                    
                prev = temp  # 更新prev
                
        return dp[n - 1]
    
    @staticmethod
    def longest_palindrome_subseq_memo(s: str) -> int:
        """
        递归+记忆化搜索解法
        时间复杂度：O(n²)
        空间复杂度：O(n²)
        """
        if not isinstance(s, str):
            raise TypeError("输入必须是字符串类型")
            
        n = len(s)
        if n == 0:
            return 0
            
        memo = [[-1] * n for _ in range(n)]
        
        def dfs(i: int, j: int) -> int:
            if i > j:
                return 0
            if i == j:
                return 1
                
            if memo[i][j] != -1:
                return memo[i][j]
                
            if s[i] == s[j]:
                result = dfs(i + 1, j - 1) + 2
            else:
                result = max(dfs(i + 1, j), dfs(i, j - 1))
                
            memo[i][j] = result
            return result
            
        return dfs(0, n - 1)
    
    @staticmethod
    def longest_palindrome_subseq_lcs(s: str) -> int:
        """
        使用LCS方法求解
        LPS(s) = LCS(s, s[::-1])
        即字符串s的最长回文子序列长度等于s和s的逆序的最长公共子序列长度
        
        时间复杂度：O(n²)
        空间复杂度：O(n²)
        """
        if not isinstance(s, str):
            raise TypeError("输入必须是字符串类型")
            
        n = len(s)
        if n == 0:
            return 0
            
        # 计算s和s逆序的LCS
        s_rev = s[::-1]
        
        # 创建dp表
        dp = [[0] * (n + 1) for _ in range(n + 1)]
        
        for i in range(1, n + 1):
            for j in range(1, n + 1):
                if s[i - 1] == s_rev[j - 1]:
                    dp[i][j] = dp[i - 1][j - 1] + 1
                else:
                    dp[i][j] = max(dp[i - 1][j], dp[i][j - 1])
                    
        return dp[n][n]
    
    @staticmethod
    def run_tests() -> None:
        """
        运行单元测试，验证算法的正确性
        """
        print("=== LeetCode 516 最长回文子序列测试 ===\n")
        
        test_cases = [
            # (描述, 输入字符串, 期望结果)
            ("基本功能", "bbbab", 4),
            ("官方示例", "cbbd", 2),
            ("全相同字符", "aaaa", 4),
            ("单字符", "a", 1),
            ("空字符串", "", 0),
            ("交替字符", "abab", 3),
            ("复杂情况", "abcabcabc", 5),
            ("长回文", "abcdefghijklmnopqrstuvwxyzyxwvutsrqponmlkjihgfedcba", 51),
        ]
        
        methods = [
            ("基础DP", LongestPalindromicSubsequence.longest_palindrome_subseq_basic),
            ("优化DP", LongestPalindromicSubsequence.longest_palindrome_subseq_optimized),
            ("记忆化搜索", LongestPalindromicSubsequence.longest_palindrome_subseq_memo),
            ("LCS方法", LongestPalindromicSubsequence.longest_palindrome_subseq_lcs),
        ]
        
        all_passed = True
        
        for description, s, expected in test_cases:
            print(f"{description}:")
            print(f"  输入字符串: '{s}'")
            print(f"  期望结果: {expected}")
            
            case_passed = True
            results = []
            
            for method_name, method in methods:
                try:
                    result = method(s)
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
        
        # 生成测试数据：大规模字符串
        n = 500  # 减少规模避免内存爆炸
        s = ''.join(random.choices('abcdefghijklmnopqrstuvwxyz', k=n))
        
        print(f"测试数据规模: {n}个字符")
        
        methods = [
            ("基础DP", LongestPalindromicSubsequence.longest_palindrome_subseq_basic),
            ("优化DP", LongestPalindromicSubsequence.longest_palindrome_subseq_optimized),
            ("LCS方法", LongestPalindromicSubsequence.longest_palindrome_subseq_lcs),
        ]
        
        results = {}
        
        for method_name, method in methods:
            start_time = time.time()
            result = method(s)
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
            
        print("注意：记忆化搜索在大规模数据下可能栈溢出")


def main():
    """
    主函数，运行测试和性能测试
    """
    try:
        # 运行单元测试
        LongestPalindromicSubsequence.run_tests()
        
        # 运行性能测试
        LongestPalindromicSubsequence.performance_test()
        
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
- 时间：外层循环n次，内层循环n次 → O(n²)
- 空间：二维列表大小n×n → O(n²)

空间优化版本：
- 时间：O(n²)
- 空间：一维列表大小n → O(n)

记忆化搜索：
- 时间：O(n²)
- 空间：记忆化数组O(n²) + 递归栈O(n) → O(n²)

LCS方法：
- 时间：O(n²)
- 空间：O(n²)

Python特性说明：
1. 列表推导式创建二维数组非常简洁
2. 字符串切片操作高效（s[::-1]）
3. 动态类型使得代码灵活但需要更多测试
4. 递归深度限制可能影响记忆化搜索

调试技巧：
1. 打印dp表观察填充过程：
   def print_dp_table(dp, s):
       n = len(s)
       print("DP表:")
       print("   " + " ".join(s))
       for i in range(n):
           print(f"{s[i]} " + " ".join(f"{dp[i][j]:2d}" for j in range(n)))

2. 使用小规模测试用例验证正确性
3. 添加断言验证关键假设

工程化建议：
1. 对于生产环境使用空间优化版本
2. 添加详细的日志记录
3. 编写全面的单元测试
4. 对于超大规模数据考虑使用C++扩展
"""