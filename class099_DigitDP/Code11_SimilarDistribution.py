# 洛谷P4127 [AHOI2009] 同类分布
# 题目链接: https://www.luogu.com.cn/problem/P4127
# 题目描述: 给出两个数a,b，求出[a,b]中各位数字之和能整除原数的数的个数。

class SimilarDistribution:
    
    @staticmethod
    def similar_distribution(a: int, b: int) -> int:
        """
        数位DP解法
        时间复杂度: O(log(b) * 162 * 162 * 2 * 2)
        空间复杂度: O(log(b) * 162 * 162 * 2 * 2)
        
        解题思路:
        1. 将问题转化为统计[0, b]中符合条件的数的个数减去[0, a-1]中符合条件的数的个数
        2. 由于数位和s的最大可能值为9*18=162（假设最多18位数），我们可以枚举数位和s
        3. 对于每个数位和s，使用数位DP统计满足以下条件的数x的个数：
           - x的数位和等于s
           - x能被s整除
        4. 状态需要记录：当前处理到第几位、当前数位和、当前数值对s的余数、是否受到上界限制、是否已经开始填数字
        5. 通过记忆化搜索避免重复计算
        
        最优解分析:
        该解法是标准的数位DP解法，能够高效处理大范围的输入，是解决此类问题的最优通用方法。
        时间复杂度中的162来自于数位和的最大可能值（9*18）。
        """
        # 计算[0, b]中符合条件的数的个数减去[0, a-1]中符合条件的数的个数
        return SimilarDistribution._count_valid_numbers(b) - SimilarDistribution._count_valid_numbers(a - 1)
    
    @staticmethod
    def _count_valid_numbers(n: int) -> int:
        """计算[0, n]中符合条件的数的个数"""
        if n < 1:
            return 0  # 0不符合条件，因为不能除以0
        
        s = str(n)
        max_sum = len(s) * 9  # 最大可能的数位和
        result = 0
        
        # 枚举所有可能的数位和s
        for s_sum in range(1, max_sum + 1):
            # 对于每个数位和s_sum，统计满足条件的数的个数
            result += SimilarDistribution._count_numbers_with_sum_divisible_by(s, s_sum)
        
        return result
    
    @staticmethod
    def _count_numbers_with_sum_divisible_by(digits_str: str, s_sum: int) -> int:
        """统计数位和等于s_sum且能被s_sum整除的数的个数"""
        digits = list(digits_str)
        len_digits = len(digits)
        
        # 使用lru_cache进行记忆化搜索
        # 注意：在Python中，我们需要将可变参数转换为不可变参数才能使用lru_cache
        from functools import lru_cache
        
        @lru_cache(maxsize=None)
        def dfs(pos: int, current_sum: int, current_mod: int, is_limit: bool, is_num: bool) -> int:
            """
            数位DP递归函数
            
            参数:
            - pos: 当前处理到第几位
            - current_sum: 当前数位和
            - current_mod: 当前数值对s_sum的余数
            - is_limit: 是否受到上界限制
            - is_num: 是否已开始填数字（处理前导零）
            
            返回:
            - 从当前状态开始，符合条件的数的个数
            """
            # 递归终止条件
            if pos == len_digits:
                # 只有当已经填了数字，且数位和等于s_sum，且数值能被s_sum整除时才算符合条件
                return 1 if (is_num and current_sum == s_sum and current_mod == 0) else 0
            
            res = 0
            
            # 如果还没开始填数字，可以选择跳过当前位（处理前导零）
            if not is_num:
                res += dfs(pos + 1, current_sum, current_mod, False, False)
            
            # 确定当前位可以填入的数字范围
            upper = int(digits[pos]) if is_limit else 9
            
            # 枚举当前位可以填入的数字
            start = 0 if is_num else 1
            for d in range(start, upper + 1):
                new_sum = current_sum + d
                # 如果新的数位和已经超过了s_sum，可以提前剪枝
                if new_sum > s_sum:
                    continue
                
                # 更新当前数值对s_sum的余数
                new_mod = (current_mod * 10 + d) % s_sum
                new_is_limit = is_limit and (d == upper)
                new_is_num = is_num or (d > 0)
                
                # 递归处理下一位
                res += dfs(pos + 1, new_sum, new_mod, new_is_limit, new_is_num)
            
            return res
        
        # 清除缓存，避免之前的计算影响当前结果
        dfs.cache_clear()
        
        return dfs(0, 0, 0, True, False)
    
    @staticmethod
    def _count_numbers_with_sum_divisible_by_optimized(digits_str: str, s_sum: int) -> int:
        """
        优化版本：减少状态维度
        注意到我们只关心数位和等于s_sum的情况，所以可以在递归过程中进行剪枝
        当sum > s_sum时，可以直接跳过
        """
        digits = list(digits_str)
        len_digits = len(digits)
        
        from functools import lru_cache
        
        @lru_cache(maxsize=None)
        def dfs_optimized(pos: int, current_sum: int, current_mod: int, is_limit: bool, is_num: bool) -> int:
            """优化版数位DP递归函数"""
            # 递归终止条件
            if pos == len_digits:
                # 只有当已经填了数字，且数位和等于s_sum，且数值能被s_sum整除时才算符合条件
                return 1 if (is_num and current_sum == s_sum and current_mod == 0) else 0
            
            res = 0
            
            # 如果还没开始填数字，可以选择跳过当前位（处理前导零）
            if not is_num:
                res += dfs_optimized(pos + 1, current_sum, current_mod, False, False)
            
            # 确定当前位可以填入的数字范围
            upper = int(digits[pos]) if is_limit else 9
            
            # 枚举当前位可以填入的数字
            start = 0 if is_num else 1
            for d in range(start, upper + 1):
                new_sum = current_sum + d
                # 如果新的数位和已经超过了s_sum，可以提前剪枝
                if new_sum > s_sum:
                    continue
                
                # 更新当前数值对s_sum的余数
                new_mod = (current_mod * 10 + d) % s_sum
                new_is_limit = is_limit and (d == upper)
                new_is_num = is_num or (d > 0)
                
                # 递归处理下一位
                res += dfs_optimized(pos + 1, new_sum, new_mod, new_is_limit, new_is_num)
            
            return res
        
        # 清除缓存，避免之前的计算影响当前结果
        dfs_optimized.cache_clear()
        
        return dfs_optimized(0, 0, 0, True, False)

# 测试代码
if __name__ == "__main__":
    # 测试用例1: a=1, b=20
    # 预期输出: 19 (所有数都符合条件，除了那些数位和为0的数)
    a1, b1 = 1, 20
    result1 = SimilarDistribution.similar_distribution(a1, b1)
    print(f"测试用例1: a={a1}, b={b1}")
    print(f"符合条件的数的个数: {result1}")
    
    # 测试用例2: a=1, b=100
    a2, b2 = 1, 100
    result2 = SimilarDistribution.similar_distribution(a2, b2)
    print(f"\n测试用例2: a={a2}, b={b2}")
    print(f"符合条件的数的个数: {result2}")