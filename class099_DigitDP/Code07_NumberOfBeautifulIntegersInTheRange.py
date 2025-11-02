# 范围中美丽整数的数目
# 给你两个正整数：low 和 high 。如果一个整数满足以下条件，我们称它为美丽整数：
# 1. 偶数数位的数目与奇数数位的数目相同；
# 2. 这个整数能被 k 整除。
# 请你返回范围 [low, high] 中美丽整数的数目。
# 测试链接 : https://leetcode.cn/problems/number-of-beautiful-integers-in-the-range/


class Solution:
    def numberOfBeautifulIntegers(self, low: int, high: int, k: int) -> int:
        """
        数位DP解法
        时间复杂度: O(log(high) * 2 * 2 * 10 * 10 * k)
        空间复杂度: O(log(high) * 2 * 2 * 10 * 10 * k)
        
        解题思路:
        1. 使用数位DP框架，逐位确定数字
        2. 状态需要记录：
           - 当前处理到第几位
           - 是否受到上界限制
           - 是否已开始填数字（处理前导零）
           - 奇数数位的个数
           - 偶数数位的个数
           - 当前数字对k的余数
        3. 通过记忆化搜索避免重复计算
        
        最优解分析:
        该解法是标准的数位DP解法，时间复杂度与状态数相关，是解决此类问题的最优通用方法。
        """
        def beautiful_integers(n: int) -> int:
            if n < 0:
                return 0
            
            s = str(n)
            length = len(s)
            
            # 使用字典进行记忆化
            memo = {}
            
            def dfs(pos: int, is_limit: bool, is_num: bool, 
                   odd_count: int, even_count: int, remainder: int) -> int:
                # 递归终止条件
                if pos == length:
                    # 只有当已经填了数字，且奇数数位和偶数数位个数相等，且能被k整除时才算一个美丽整数
                    return 1 if is_num and odd_count == even_count and remainder == 0 else 0
                
                # 记忆化搜索
                if (pos, is_limit, is_num, odd_count, even_count, remainder) in memo and not is_limit and is_num:
                    return memo[(pos, is_limit, is_num, odd_count, even_count, remainder)]
                
                ans = 0
                
                # 如果还没开始填数字，可以选择跳过当前位（处理前导零）
                if not is_num:
                    ans += dfs(pos + 1, False, False, odd_count, even_count, remainder)
                
                # 确定当前位可以填入的数字范围
                up = int(s[pos]) if is_limit else 9
                
                # 枚举当前位可以填入的数字
                for d in range(0 if is_num else 1, up + 1):
                    # 根据数字的奇偶性更新奇数数位和偶数数位的个数
                    new_odd_count = odd_count + (1 if d % 2 == 1 else 0)
                    new_even_count = even_count + (1 if d % 2 == 0 else 0)
                    
                    # 更新当前数字对k的余数
                    new_remainder = (remainder * 10 + d) % k
                    
                    # 递归处理下一位
                    ans += dfs(pos + 1, is_limit and d == up, True, 
                              new_odd_count, new_even_count, new_remainder)
                
                # 记忆化存储
                if not is_limit and is_num:
                    memo[(pos, is_limit, is_num, odd_count, even_count, remainder)] = ans
                
                return ans
            
            return dfs(0, True, False, 0, 0, 0)
        
        # 答案为[0, high]中的美丽整数个数减去[0, low-1]中的美丽整数个数
        return beautiful_integers(high) - beautiful_integers(low - 1)


# 测试方法
if __name__ == "__main__":
    solution = Solution()
    
    # 测试用例1
    low1, high1, k1 = 10, 20, 3
    print(f"low = {low1}, high = {high1}, k = {k1}")
    print(f"美丽整数的数目: {solution.numberOfBeautifulIntegers(low1, high1, k1)}")
    # 预期输出: 2 (11和19是美丽整数)
    
    # 测试用例2
    low2, high2, k2 = 1, 10, 1
    print(f"low = {low2}, high = {high2}, k = {k2}")
    print(f"美丽整数的数目: {solution.numberOfBeautifulIntegers(low2, high2, k2)}")
    # 预期输出: 1 (10是美丽整数)
    
    # 测试用例3
    low3, high3, k3 = 5, 5, 2
    print(f"low = {low3}, high = {high3}, k = {k3}")
    print(f"美丽整数的数目: {solution.numberOfBeautifulIntegers(low3, high3, k3)}")
    # 预期输出: 0 (没有美丽整数)