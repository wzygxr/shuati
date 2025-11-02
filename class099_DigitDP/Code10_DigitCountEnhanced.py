# 洛谷P2602 数字计数
# 题目链接: https://www.luogu.com.cn/problem/P2602
# 题目描述: 给定两个正整数a,b，求在区间[a,b]中的数，数字0~9的出现次数之和。

class DigitCountEnhanced:
    @staticmethod
    def digit_count(a: int, b: int) -> list:
        """
        数位DP解法
        时间复杂度: O(10 * log(b) * 10 * 2)
        空间复杂度: O(10 * log(b) * 10 * 2)
        
        解题思路:
        1. 将问题转化为统计[0, b]中每个数字出现的次数减去[0, a-1]中每个数字出现的次数
        2. 对于每个数字d(0-9)，使用数位DP统计在[0, n]范围内d出现的次数
        3. 状态需要记录：当前处理到第几位、当前已经出现d的次数、是否受到上界限制、是否已经开始填数字（处理前导零）
        4. 通过记忆化搜索避免重复计算
        
        最优解分析:
        该解法是标准的数位DP解法，能够高效处理大范围的输入，是解决此类问题的最优通用方法。
        """
        # 计算[0, b]中每个数字出现的次数减去[0, a-1]中每个数字出现的次数
        return [DigitCountEnhanced._count_digit(b, d) - DigitCountEnhanced._count_digit(a - 1, d) for d in range(10)]
    
    @staticmethod
    def _count_digit(n: int, digit: int) -> int:
        """统计[0, n]范围内digit出现的次数"""
        if n < 0:
            return 0
        if n < 10:
            # 对于小于10的数，直接判断digit是否小于等于n
            return 0 if digit == 0 else (1 if digit <= n else 0)
        
        s = str(n)
        
        from functools import lru_cache
        
        @lru_cache(maxsize=None)
        def dfs(pos: int, count: int, is_limit: bool, is_num: bool) -> int:
            """
            数位DP递归函数
            
            参数:
            - pos: 当前处理到第几位
            - count: 当前已经出现digit的次数
            - is_limit: 是否受到上界限制
            - is_num: 是否已开始填数字（处理前导零）
            
            返回:
            - 从当前状态开始，digit出现的次数总和
            """
            # 递归终止条件
            if pos == len(s):
                return count
            
            res = 0
            
            # 如果还没开始填数字，可以选择跳过当前位（处理前导零）
            if not is_num:
                res += dfs(pos + 1, count, False, False)
            
            # 确定当前位可以填入的数字范围
            upper = int(s[pos]) if is_limit else 9
            
            # 枚举当前位可以填入的数字
            start = 0 if is_num else 1
            for d in range(start, upper + 1):
                new_count = count
                if d == digit:
                    new_count += 1
                
                new_is_limit = is_limit and (d == upper)
                new_is_num = is_num or (d > 0)
                
                # 递归处理下一位
                res += dfs(pos + 1, new_count, new_is_limit, new_is_num)
            
            return res
        
        # 处理特殊情况：当digit是0时，我们需要额外处理
        if digit == 0:
            # 对于0的计数，我们需要计算[1, n]中0的个数
            # 因为在数位DP中，前导零不算作有效数字
            total = 0
            # 直接遍历每一位，计算每一位上0出现的次数
            for i in range(1, len(s) + 1):
                # 计算高位部分
                high = n // (10 ** i)
                # 计算当前位的数字
                current = (n // (10 ** (i - 1))) % 10
                # 计算低位部分
                low = n % (10 ** (i - 1))
                
                if current > 0:
                    total += (high) * (10 ** (i - 1))
                else:
                    total += (high - 1) * (10 ** (i - 1)) + (low + 1)
            
            return total
        
        # 清除缓存，避免之前的计算影响当前结果
        dfs.cache_clear()
        
        return dfs(0, 0, True, False)
    
    @staticmethod
    def _count_digit_optimized(n: int, digit: int) -> int:
        """
        优化版数位DP统计函数
        1. 更高效地处理前导零问题
        2. 优化记忆化搜索的状态设计
        """
        if n < 0:
            return 0
        if n < 10:
            return 0 if digit == 0 else (1 if digit <= n else 0)
        
        s = str(n)
        
        from functools import lru_cache
        
        @lru_cache(maxsize=None)
        def dfs_optimized(pos: int, count: int, is_limit: bool, has_leading_zero: bool) -> int:
            """优化版数位DP递归函数"""
            # 递归终止条件
            if pos == len(s):
                return count
            
            res = 0
            
            # 确定当前位可以填入的数字范围
            upper = int(s[pos]) if is_limit else 9
            
            # 枚举当前位可以填入的数字
            for d in range(0, upper + 1):
                new_is_limit = is_limit and (d == upper)
                new_has_leading_zero = has_leading_zero and (d == 0)
                
                new_count = count
                if not new_has_leading_zero and d == digit:
                    new_count += 1
                
                # 递归处理下一位
                res += dfs_optimized(pos + 1, new_count, new_is_limit, new_has_leading_zero)
            
            return res
        
        # 清除缓存，避免之前的计算影响当前结果
        dfs_optimized.cache_clear()
        
        return dfs_optimized(0, 0, True, True)
    
    @staticmethod
    def digit_count_optimized(a: int, b: int) -> list:
        """使用优化版数位DP函数的解决方案"""
        return [DigitCountEnhanced._count_digit_optimized(b, d) - DigitCountEnhanced._count_digit_optimized(a - 1, d) for d in range(10)]

# 测试代码
if __name__ == "__main__":
    # 测试用例1: a=1, b=10
    # 预期输出: [1, 2, 1, 1, 1, 1, 1, 1, 1, 1]
    # 解释: 0出现1次(10)，1出现2次(1, 10)，其他数字各出现1次
    a1, b1 = 1, 10
    result1 = DigitCountEnhanced.digit_count(a1, b1)
    print(f"测试用例1: a={a1}, b={b1}")
    print(f"各数字出现次数: {result1}")
    
    # 测试用例2: a=123, b=456
    a2, b2 = 123, 456
    result2 = DigitCountEnhanced.digit_count(a2, b2)
    print(f"\n测试用例2: a={a2}, b={b2}")
    print(f"各数字出现次数: {result2}")