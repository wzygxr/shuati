"""
LeetCode 1012. 至少有 1 位重复的数字 - Python实现
题目链接：https://leetcode.cn/problems/numbers-with-repeated-digits/

题目描述：
给定正整数 N，返回在 [1, N] 范围内具有至少 1 位重复数字的正整数的个数。

解题思路：
使用数位动态规划（Digit DP）解决该问题。我们采用补集的思想，先计算没有重复数字的个数，
然后用总数减去它得到至少有1位重复数字的个数。
状态定义：
dp[pos][mask][limit][lead] 表示处理到第pos位，已使用的数字状态为mask，
limit表示是否受到上界限制，lead表示是否有前导零

算法分析：
时间复杂度：O(log N * 2^10 * 2 * 2) = O(log N)
空间复杂度：O(log N * 2^10)

Python实现特点：
1. 使用多维列表实现记忆化数组
2. 使用递归函数处理数位DP
3. 支持大整数运算

最优解分析：
这是数位DP的标准解法，对于此类计数问题是最优解。通过补集思想将问题转化为计算
没有重复数字的个数，可以简化问题的复杂度。

工程化考量：
1. 位运算优化：使用位掩码表示已使用的数字状态
2. 补集思想：通过计算补集简化问题
3. 边界处理：正确处理前导零和上界限制
4. 性能优化：使用记忆化搜索避免重复计算
5. 代码可读性：清晰的变量命名和详细注释

相关题目链接：
- LeetCode 1012: https://leetcode.cn/problems/numbers-with-repeated-digits/
- LeetCode 2376: https://leetcode.cn/problems/count-special-integers/

多语言实现：
- Java: LeetCode1012_NumbersWithRepeatedDigits.java
- Python: LeetCode1012_NumbersWithRepeatedDigits.py
- C++: 暂无
"""

class Solution:
    def numDupDigitsAtMostN(self, N: int) -> int:
        """
        主函数：计算在 [1, N] 范围内具有至少 1 位重复数字的正整数的个数
        
        Args:
            N: 上界
            
        Returns:
            至少有1位重复数字的正整数的个数
            
        时间复杂度：O(log N)
        空间复杂度：O(log N * 2^10)
        """
        # 将N转换为数字数组
        n_str = str(N)
        len_n = len(n_str)
        digits_n = [int(c) for c in n_str]
        
        # 记忆化数组
        # dp[pos][mask][limit][lead] 
        # pos: 当前处理到第几位
        # mask: 已使用的数字状态（用位运算表示）
        # limit: 是否受到上界限制
        # lead: 是否有前导零
        dp = [[[[-1 for _ in range(2)] for _ in range(2)] for _ in range(1024)] for _ in range(len_n)]
        
        # 数位DP核心函数 - 计算没有重复数字的个数
        def dfs(pos, mask, limit, lead):
            """
            数位DP核心函数 - 计算没有重复数字的个数
            
            Args:
                pos: 当前处理到第几位
                mask: 已使用的数字状态（用位运算表示）
                limit: 是否受到上界限制
                lead: 是否有前导零
                
            Returns:
                没有重复数字的个数
            """
            # 递归终止条件：处理完所有数位
            if pos == len_n:
                # 只有在没有前导零的情况下才算一个有效数字
                return 0 if lead else 1
            
            # 记忆化搜索优化：如果该状态已经计算过，直接返回结果
            if not limit and not lead and dp[pos][mask][1 if limit else 0][1 if lead else 0] != -1:
                return dp[pos][mask][1 if limit else 0][1 if lead else 0]
            
            result = 0
            
            # 如果有前导零，可以继续选择前导零
            if lead:
                result += dfs(pos + 1, mask, False, True)
            
            # 确定当前位可以填入的数字范围
            max_digit = digits_n[pos] if limit else 9
            
            # 枚举当前位可以填入的数字
            for digit in range(max_digit + 1):
                # 跳过前导零
                if lead and digit == 0:
                    continue
                
                # 如果该数字已经使用过，跳过
                if (mask >> digit) & 1:
                    continue
                
                # 递归处理下一位，更新mask
                result += dfs(pos + 1, mask | (1 << digit), limit and (digit == max_digit), False)
            
            # 记忆化存储结果
            if not limit and not lead:
                dp[pos][mask][1 if limit else 0][1 if lead else 0] = result
            
            return result
        
        # 计算没有重复数字的个数
        unique_count = dfs(0, 0, True, True)
        
        # 用总数减去没有重复数字的个数，得到至少有1位重复数字的个数
        return N - unique_count

    def numDupDigitsAtMostNMath(self, N: int) -> int:
        """
        数学方法实现 - 替代解法
        直接计算至少有1位重复数字的个数
        
        Args:
            N: 上界
            
        Returns:
            至少有1位重复数字的正整数的个数
        """
        # 将N转换为数字数组
        n_str = str(N)
        len_n = len(n_str)
        digits = [int(c) for c in n_str]
        
        result = 0
        
        # 计算位数小于len_n的所有数字中重复数字的个数
        # 位数为i的数字总共有9*10^(i-1)个，其中没有重复数字的有9*A(9,i-1)个
        for i in range(1, len_n):
            total = 9
            for j in range(1, i):
                total *= (10 - j)
            result += 9 * (10 ** (i - 1)) - total
        
        # 计算位数等于len_n且小于等于N的数字中重复数字的个数
        used = [False] * 10
        for i in range(len_n):
            # 计算小于digits[i]且未使用的数字个数
            count = 0
            for j in range(1 if i == 0 else 0, digits[i]):
                if not used[j]:
                    count += 1
            
            # 计算剩余位置可以填入的数字组合数
            remaining = 1
            available = 10 - i - 1
            for j in range(i + 1, len_n):
                remaining *= available
                available -= 1
            
            # 计算没有重复数字的个数
            unique = count * remaining
            
            # 计算有重复数字的个数
            total = count * (10 ** (len_n - i - 1))
            result += total - unique
            
            # 如果当前数字已经被使用，说明N本身有重复数字
            if used[digits[i]]:
                break
            
            used[digits[i]] = True
        
        # 检查N本身是否有重复数字
        has_dup = False
        check = [False] * 10
        for i in range(len_n):
            digit = digits[i]
            if check[digit]:
                has_dup = True
                break
            check[digit] = True
        
        if has_dup:
            result += 1
        
        return result

# 测试方法
if __name__ == "__main__":
    solution = Solution()
    
    # 测试用例1
    N1 = 20
    result1 = solution.numDupDigitsAtMostN(N1)
    result1_math = solution.numDupDigitsAtMostNMath(N1)
    print("测试用例1:")
    print(f"N = {N1}")
    print(f"数位DP结果: {result1}")
    print(f"数学方法结果: {result1_math}")
    print(f"期望输出: 1")
    print(f"测试结果: {'通过' if result1 == 1 and result1_math == 1 else '失败'}")
    print()
    
    # 测试用例2
    N2 = 100
    result2 = solution.numDupDigitsAtMostN(N2)
    result2_math = solution.numDupDigitsAtMostNMath(N2)
    print("测试用例2:")
    print(f"N = {N2}")
    print(f"数位DP结果: {result2}")
    print(f"数学方法结果: {result2_math}")
    print(f"期望输出: 10")
    print(f"测试结果: {'通过' if result2 == 10 and result2_math == 10 else '失败'}")
    print()
    
    # 测试用例3
    N3 = 1000
    result3 = solution.numDupDigitsAtMostN(N3)
    result3_math = solution.numDupDigitsAtMostNMath(N3)
    print("测试用例3:")
    print(f"N = {N3}")
    print(f"数位DP结果: {result3}")
    print(f"数学方法结果: {result3_math}")
    print(f"期望输出: 262")
    print(f"测试结果: {'通过' if result3 == 262 and result3_math == 262 else '失败'}")
    print()