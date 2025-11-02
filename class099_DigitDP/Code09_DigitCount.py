# ZJOI2010 数字计数
# 给定两个正整数a和b，求在[a,b]中的所有整数中，每个数码（digit）各出现了多少次。
# 测试链接 : https://www.luogu.com.cn/problem/P2602


class Solution:
    def countDigits(self, a: int, b: int) -> list:
        """
        数位DP解法
        时间复杂度: O(log(b) * 2 * 2 * 10 * 10)
        空间复杂度: O(log(b) * 2 * 2 * 10)
        
        解题思路:
        1. 使用数位DP框架，逐位确定数字
        2. 对于每个数字0-9，分别计算其出现次数
        3. 状态需要记录：
           - 当前处理到第几位
           - 是否受到上界限制
           - 是否已开始填数字（处理前导零）
           - 当前数字的出现次数
        4. 通过记忆化搜索避免重复计算
        
        最优解分析:
        该解法是标准的数位DP解法，时间复杂度与状态数相关，是解决此类问题的最优通用方法。
        """
        
        def count_digits_up_to(n: int) -> list:
            if n < 0:
                return [0] * 10
            
            s = str(n)
            length = len(s)
            
            def dfs(pos: int, is_limit: bool, is_num: bool, target_digit: int) -> int:
                # 递归终止条件
                if pos == length:
                    return 0
                
                # 记忆化搜索
                if (pos, is_limit, is_num, target_digit) in memo and not is_limit and is_num:
                    return memo[(pos, is_limit, is_num, target_digit)]
                
                ans = 0
                
                # 如果还没开始填数字，可以选择跳过当前位（处理前导零）
                if not is_num:
                    ans += dfs(pos + 1, False, False, target_digit)
                
                # 确定当前位可以填入的数字范围
                up = int(s[pos]) if is_limit else 9
                
                # 枚举当前位可以填入的数字
                for d in range(0 if is_num else 1, up + 1):
                    # 如果当前位填的是目标数字，则计数加1
                    count = 1 if d == target_digit else 0
                    
                    # 递归处理下一位
                    ans += count + dfs(pos + 1, is_limit and d == up, True, target_digit)
                
                # 记忆化存储
                if not is_limit and is_num:
                    memo[(pos, is_limit, is_num, target_digit)] = ans
                
                return ans
            
            result = [0] * 10
            for digit in range(10):
                memo = {}  # 每个数字都需要独立的记忆化数组
                result[digit] = dfs(0, True, False, digit)
            
            return result
        
        # 答案为[0, b]中的数字计数减去[0, a-1]中的数字计数
        count_b = count_digits_up_to(b)
        count_a = count_digits_up_to(a - 1)
        
        return [count_b[i] - count_a[i] for i in range(10)]

# 测试方法
if __name__ == "__main__":
    solution = Solution()
    
    # 测试用例1
    a1, b1 = 1, 9
    print(f"a = {a1}, b = {b1}")
    result1 = solution.countDigits(a1, b1)
    print(" ".join(map(str, result1)))
    # 预期输出: 0 1 1 1 1 1 1 1 1 1
    
    # 测试用例2
    a2, b2 = 1, 99
    print(f"a = {a2}, b = {b2}")
    result2 = solution.countDigits(a2, b2)
    print(" ".join(map(str, result2)))
    # 预期输出: 9 20 20 20 20 20 20 20 20 20
    
    # 测试用例3
    a3, b3 = 1, 1000
    print(f"a = {a3}, b = {b3}")
    result3 = solution.countDigits(a3, b3)
    print(" ".join(map(str, result3)))