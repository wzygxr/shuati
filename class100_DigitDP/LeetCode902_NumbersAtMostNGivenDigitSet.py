"""
LeetCode 902. 最大为 N 的数字组合 - Python实现
题目链接：https://leetcode.cn/problems/numbers-at-most-n-given-digit-set/

题目描述：
我们有一组排序的数字 D，它是 {'1','2','3','4','5','6','7','8','9'} 的非空子集。
现在，我们用这些数字来构造数字，可以重复使用，例如 '11' 和 '12'。
返回可以构造出的小于或等于 N 的正整数的数目。

解题思路：
使用数位动态规划（Digit DP）解决该问题。我们逐位构造数字，确保不超过N。
状态定义：
dp[pos][limit][lead] 表示处理到第pos位，limit表示是否受到上界限制，lead表示是否有前导零

算法分析：
时间复杂度：O(log N * 2 * 2 * |D|) = O(log N * |D|)
空间复杂度：O(log N)

Python实现特点：
1. 使用多维列表实现记忆化数组
2. 使用递归函数处理数位DP
3. 支持大整数运算

最优解分析：
这是数位DP的标准解法，对于此类计数问题是最优解。通过逐位构造数字并使用记忆化搜索，
可以高效地计算满足条件的数字个数。

工程化考量：
1. 数组排序：对可用数字进行排序以优化搜索过程
2. 边界处理：正确处理前导零和上界限制
3. 性能优化：使用记忆化搜索避免重复计算
4. 代码可读性：清晰的变量命名和详细注释

相关题目链接：
- LeetCode 902: https://leetcode.cn/problems/numbers-at-most-n-given-digit-set/
- AcWing 1084: https://www.acwing.com/problem/content/1086/

多语言实现：
- Java: LeetCode902_NumbersAtMostNGivenDigitSet.java
- Python: LeetCode902_NumbersAtMostNGivenDigitSet.py
- C++: 暂无
"""

class Solution:
    def atMostNGivenDigitSet(self, D, N: int) -> int:
        """
        主函数：计算可以构造出的小于或等于 N 的正整数的数目
        
        Args:
            D: 可用的数字字符数组
            N: 上界
            
        Returns:
            满足条件的数字个数
            
        时间复杂度：O(log N * |D|)
        空间复杂度：O(log N)
        """
        # 将字符串数组转换为整数数组并排序
        digits = [int(d) for d in D]
        digits.sort()
        
        # 将N转换为数字数组
        n_str = str(N)
        len_n = len(n_str)
        digits_n = [int(c) for c in n_str]
        
        # 记忆化数组
        # dp[pos][limit][lead] 
        # pos: 当前处理到第几位
        # limit: 是否受到上界限制
        # lead: 是否有前导零
        dp = [[[-1 for _ in range(2)] for _ in range(2)] for _ in range(len_n)]
        
        # 数位DP核心函数
        def dfs(pos, limit, lead):
            """
            数位DP核心函数
            
            Args:
                pos: 当前处理到第几位
                limit: 是否受到上界限制
                lead: 是否有前导零
                
            Returns:
                满足条件的数字个数
            """
            # 递归终止条件：处理完所有数位
            if pos == len_n:
                # 只有在没有前导零的情况下才算一个有效数字
                return 0 if lead else 1
            
            # 记忆化搜索优化：如果该状态已经计算过，直接返回结果
            if not limit and not lead and dp[pos][1 if limit else 0][1 if lead else 0] != -1:
                return dp[pos][1 if limit else 0][1 if lead else 0]
            
            result = 0
            
            # 如果有前导零，可以继续选择前导零
            if lead:
                result += dfs(pos + 1, False, True)
            
            # 确定当前位可以填入的数字范围
            max_digit = digits_n[pos] if limit else 9
            
            # 枚举当前位可以填入的数字
            for digit in digits:
                # 如果当前数字超过限制，跳出循环
                if digit > max_digit:
                    break
                
                # 递归处理下一位
                result += dfs(pos + 1, limit and (digit == max_digit), False)
            
            # 记忆化存储结果
            if not limit and not lead:
                dp[pos][1 if limit else 0][1 if lead else 0] = result
            
            return result
        
        # 从最高位开始进行数位DP
        return dfs(0, True, True)

    def atMostNGivenDigitSetMath(self, D, N: int) -> int:
        """
        数学方法实现 - 替代解法
        通过分别计算位数小于N和位数等于N的情况来计算结果
        
        Args:
            D: 可用的数字字符数组
            N: 上界
            
        Returns:
            满足条件的数字个数
        """
        # 将字符串数组转换为整数数组
        digits = [int(d) for d in D]
        
        n_str = str(N)
        len_n = len(n_str)
        
        result = 0
        
        # 计算位数小于len_n的所有数字个数
        for i in range(1, len_n):
            result += len(digits) ** i
        
        # 计算位数等于len_n且小于等于N的数字个数
        same = True
        for i in range(len_n):
            digit = int(n_str[i])
            count = 0
            
            for d in digits:
                if d < digit:
                    count += 1
                elif d == digit:
                    # 找到相同数字，继续比较下一位
                    break
                else:
                    # 当前数字大于目标数字，后面不会有匹配
                    same = False
                    break
            
            result += count * (len(digits) ** (len_n - i - 1))
            
            if not same:
                break
            
            # 如果没有找到相同数字，说明N不能被构造出来
            found = False
            for d in digits:
                if d == digit:
                    found = True
                    break
            
            if not found:
                same = False
        
        # 如果N本身可以被构造出来，需要加上1
        if same:
            result += 1
        
        return result

# 测试方法
if __name__ == "__main__":
    solution = Solution()
    
    # 测试用例1
    D1 = ["1", "3", "5", "7"]
    N1 = 100
    result1 = solution.atMostNGivenDigitSet(D1, N1)
    result1_math = solution.atMostNGivenDigitSetMath(D1, N1)
    print("测试用例1:")
    print(f"D = {D1}, N = {N1}")
    print(f"数位DP结果: {result1}")
    print(f"数学方法结果: {result1_math}")
    print(f"期望输出: 20")
    print(f"测试结果: {'通过' if result1 == 20 and result1_math == 20 else '失败'}")
    print()
    
    # 测试用例2
    D2 = ["1", "4", "9"]
    N2 = 1000000000
    result2 = solution.atMostNGivenDigitSet(D2, N2)
    result2_math = solution.atMostNGivenDigitSetMath(D2, N2)
    print("测试用例2:")
    print(f"D = {D2}, N = {N2}")
    print(f"数位DP结果: {result2}")
    print(f"数学方法结果: {result2_math}")
    print(f"期望输出: 29523")
    print(f"测试结果: {'通过' if result2 == 29523 and result2_math == 29523 else '失败'}")
    print()
    
    # 测试用例3
    D3 = ["7"]
    N3 = 8
    result3 = solution.atMostNGivenDigitSet(D3, N3)
    result3_math = solution.atMostNGivenDigitSetMath(D3, N3)
    print("测试用例3:")
    print(f"D = {D3}, N = {N3}")
    print(f"数位DP结果: {result3}")
    print(f"数学方法结果: {result3_math}")
    print(f"期望输出: 1")
    print(f"测试结果: {'通过' if result3 == 1 and result3_math == 1 else '失败'}")
    print()