"""
LeetCode 72. 编辑距离
给你两个单词 word1 和 word2，请返回将 word1 转换成 word2 所使用的最少操作数。
你可以对一个单词进行如下三种操作：插入一个字符、删除一个字符、替换一个字符。
测试链接：https://leetcode.cn/problems/edit-distance/

算法详解：
使用动态规划解决编辑距离问题，支持三种操作：插入、删除、替换。

时间复杂度：O(m*n)，其中m和n分别是word1和word2的长度
空间复杂度：O(min(m,n))，使用空间优化技术

工程化考量：
1. 异常处理：检查输入参数有效性
2. 边界处理：正确处理空字符串
3. 性能优化：使用滚动数组减少内存使用
4. 代码可读性：清晰的变量命名和注释
5. 类型注解：提高代码可读性和可维护性

Python特性：
1. 动态类型，代码简洁
2. 列表操作高效但需要注意内存使用
3. 支持大整数，无溢出问题
4. 内置min函数支持多参数比较
"""

from typing import Union
import time

class EditDistance:
    """
    编辑距离算法类
    提供多种实现版本，支持异常处理和性能优化
    """
    
    @staticmethod
    def min_distance_basic(word1: str, word2: str) -> int:
        """
        基础动态规划解法
        时间复杂度：O(m*n)
        空间复杂度：O(m*n)
        
        Args:
            word1: 第一个单词
            word2: 第二个单词
            
        Returns:
            int: 最小编辑距离
            
        Raises:
            TypeError: 输入不是字符串类型
        """
        # 类型检查
        if not isinstance(word1, str) or not isinstance(word2, str):
            raise TypeError("输入必须是字符串类型")
            
        m, n = len(word1), len(word2)
        
        # 特殊情况处理
        if m == 0:
            return n
        if n == 0:
            return m
            
        # 创建dp表
        dp = [[0] * (n + 1) for _ in range(m + 1)]
        
        # 初始化边界条件
        for i in range(m + 1):
            dp[i][0] = i  # 将word1前i个字符转换为空字符串需要i次删除
            
        for j in range(n + 1):
            dp[0][j] = j  # 将空字符串转换为word2前j个字符需要j次插入
            
        # 填充dp表
        for i in range(1, m + 1):
            for j in range(1, n + 1):
                if word1[i - 1] == word2[j - 1]:
                    # 字符相同，不需要操作
                    dp[i][j] = dp[i - 1][j - 1]
                else:
                    # 字符不同，取三种操作的最小值
                    dp[i][j] = min(
                        dp[i - 1][j],    # 删除
                        dp[i][j - 1],    # 插入
                        dp[i - 1][j - 1] # 替换
                    ) + 1
                    
        return dp[m][n]
    
    @staticmethod
    def min_distance_optimized(word1: str, word2: str) -> int:
        """
        空间优化版本
        使用滚动数组将空间复杂度从O(m*n)优化到O(min(m,n))
        
        时间复杂度：O(m*n)
        空间复杂度：O(min(m,n))
        """
        if not isinstance(word1, str) or not isinstance(word2, str):
            raise TypeError("输入必须是字符串类型")
            
        m, n = len(word1), len(word2)
        
        if m == 0:
            return n
        if n == 0:
            return m
            
        # 确保word1是较短的字符串以减少空间使用
        if m > n:
            return EditDistance.min_distance_optimized(word2, word1)
            
        # 使用两行数组存储状态
        prev = list(range(m + 1))  # 上一行
        curr = [0] * (m + 1)       # 当前行
        
        for j in range(1, n + 1):
            curr[0] = j  # 当前行的第一个元素
            
            for i in range(1, m + 1):
                if word1[i - 1] == word2[j - 1]:
                    curr[i] = prev[i - 1]
                else:
                    curr[i] = min(
                        prev[i],     # 删除
                        curr[i - 1], # 插入
                        prev[i - 1]  # 替换
                    ) + 1
                    
            # 交换数组，准备下一轮
            prev, curr = curr, prev
            
        return prev[m]
    
    @staticmethod
    def min_distance_super_optimized(word1: str, word2: str) -> int:
        """
        进一步空间优化版本（使用一维数组）
        
        时间复杂度：O(m*n)
        空间复杂度：O(min(m,n))
        """
        if not isinstance(word1, str) or not isinstance(word2, str):
            raise TypeError("输入必须是字符串类型")
            
        m, n = len(word1), len(word2)
        
        if m == 0:
            return n
        if n == 0:
            return m
            
        # 确保word1是较短的字符串
        if m > n:
            return EditDistance.min_distance_super_optimized(word2, word1)
            
        # 使用一维数组存储状态
        dp = list(range(m + 1))
        
        for j in range(1, n + 1):
            prev = dp[0]  # 保存左上角的值
            dp[0] = j     # 当前行的第一个元素
            
            for i in range(1, m + 1):
                temp = dp[i]  # 保存当前值
                
                if word1[i - 1] == word2[j - 1]:
                    dp[i] = prev
                else:
                    dp[i] = min(
                        dp[i],     # 删除
                        dp[i - 1], # 插入
                        prev       # 替换
                    ) + 1
                    
                prev = temp  # 更新左上角的值
                
        return dp[m]
    
    @staticmethod
    def run_tests() -> None:
        """
        运行单元测试，验证算法的正确性
        """
        print("=== LeetCode 72 编辑距离测试 ===\n")
        
        test_cases = [
            # (word1, word2, expected)
            ("horse", "ros", 3),
            ("intention", "intention", 0),
            ("abc", "def", 3),
            ("", "abc", 3),
            ("", "", 0),
            ("intention", "execution", 5),
            ("a", "b", 1),
            ("ab", "bc", 2),
        ]
        
        methods = [
            ("基础版本", EditDistance.min_distance_basic),
            ("优化版本", EditDistance.min_distance_optimized),
            ("超级优化", EditDistance.min_distance_super_optimized),
        ]
        
        for i, (word1, word2, expected) in enumerate(test_cases, 1):
            print(f"测试用例{i}: word1='{word1}', word2='{word2}'")
            print(f"期望结果: {expected}")
            
            all_passed = True
            for method_name, method in methods:
                try:
                    result = method(word1, word2)
                    status = "✓" if result == expected else "✗"
                    print(f"  {method_name}: {result} {status}")
                    if result != expected:
                        all_passed = False
                except Exception as e:
                    print(f"  {method_name}: 错误 - {e}")
                    all_passed = False
                    
            print("通过" if all_passed else "失败")
            print()
    
    @staticmethod
    def performance_test() -> None:
        """
        性能测试，测试算法在大规模数据下的表现
        """
        print("=== 性能测试 ===")
        
        # 生成测试数据
        word1 = "a" * 100
        word2 = "b" * 100
        
        methods = [
            ("基础版本", EditDistance.min_distance_basic),
            ("优化版本", EditDistance.min_distance_optimized),
            ("超级优化", EditDistance.min_distance_super_optimized),
        ]
        
        for method_name, method in methods:
            start_time = time.time()
            result = method(word1, word2)
            end_time = time.time()
            
            duration = (end_time - start_time) * 1000  # 转换为毫秒
            
            print(f"{method_name}:")
            print(f"  结果: {result}")
            print(f"  耗时: {duration:.2f} 毫秒")
            print(f"  期望: 100")
            print()


def main():
    """
    主函数，运行测试和性能测试
    """
    try:
        # 运行单元测试
        EditDistance.run_tests()
        
        # 运行性能测试
        EditDistance.performance_test()
        
        print("所有测试完成！")
        
    except Exception as e:
        print(f"测试过程中发生错误: {e}")
        return 1
        
    return 0


if __name__ == "__main__":
    exit(main())


"""
复杂度分析详细计算：

基础版本：
- 时间：外层循环m次，内层循环n次，每次操作O(1) → O(m*n)
- 空间：dp列表大小(m+1)*(n+1) → O(m*n)

优化版本：
- 时间：同上 → O(m*n)
- 空间：两个列表，每个大小min(m,n)+1 → O(min(m,n))

超级优化版本：
- 时间：同上 → O(m*n)  
- 空间：一个列表，大小min(m,n)+1 → O(min(m,n))

Python特性说明：
1. 列表推导式创建二维数组更简洁
2. 多重赋值交换变量非常方便
3. 动态类型使得代码更灵活但需要更多测试
4. 内置min函数支持多参数比较，代码更简洁

与Java/C++的差异：
1. 代码更简洁，但性能可能较低
2. 动态类型使得开发更快但需要更多测试
3. 内存管理自动，无需手动释放
4. 支持大整数，无溢出问题

工程化建议：
1. 添加类型注解提高代码可读性
2. 使用异常处理确保程序健壮性
3. 编写单元测试覆盖各种边界情况
4. 对于性能敏感场景考虑使用PyPy或C扩展

调试技巧：
1. 使用print语句输出中间状态
2. 使用pdb进行交互式调试
3. 编写断言验证关键假设
4. 使用logging模块记录运行日志
"""