# Codeforces 55D. Beautiful numbers
# 题目链接: https://codeforces.com/problemset/problem/55/D
# 题目描述: 如果一个正整数能被它的所有非零数字整除，那么这个数就是美丽的。给定区间[l,r]，求其中美丽数字的个数。

class BeautifulNumbers:
    MOD = 2520  # 1-9的最小公倍数
    
    @staticmethod
    def beautiful_numbers(l: int, r: int) -> int:
        """
        数位DP解法
        时间复杂度: O(log(r) * 2520 * 2520 * 2 * 2)
        空间复杂度: O(log(r) * 2520 * 2520 * 2 * 2)
        
        解题思路:
        1. 将问题转化为统计[0, r]中美丽数字的个数减去[0, l-1]中美丽数字的个数
        2. 关键观察：一个数能被其所有非零数字整除等价于这个数能被这些数字的最小公倍数(LCM)整除
        3. 由于1-9的最小公倍数是2520，而任意几个数字的LCM一定是2520的因数
        4. 状态需要记录：当前处理到第几位、当前数字的最小公倍数、当前数字对2520的余数、是否受到上界限制、是否已经开始填数字
        5. 通过记忆化搜索避免重复计算
        
        最优解分析:
        该解法是标准的数位DP解法，通过数学观察（使用LCM和模数2520）来优化状态设计，
        是解决此类问题的最优通用方法。时间复杂度中的2520来自于1-9的最小公倍数。
        """
        # 计算[0, r]中美丽数字的个数减去[0, l-1]中美丽数字的个数
        return BeautifulNumbers._count_beautiful_numbers(r) - BeautifulNumbers._count_beautiful_numbers(l - 1)
    
    @staticmethod
    def _gcd(a: int, b: int) -> int:
        """计算两个数的最大公约数"""
        while b:
            a, b = b, a % b
        return a
    
    @staticmethod
    def _lcm(a: int, b: int) -> int:
        """计算两个数的最小公倍数"""
        if a == 0 or b == 0:
            return a + b
        return a // BeautifulNumbers._gcd(a, b) * b
    
    @staticmethod
    def _get_factors():
        """获取2520的所有因数"""
        factors = []
        for i in range(1, BeautifulNumbers.MOD + 1):
            if BeautifulNumbers.MOD % i == 0:
                factors.append(i)
        return factors
    
    @staticmethod
    def _count_beautiful_numbers(n: int) -> int:
        """计算[0, n]中美丽数字的个数"""
        if n < 1:
            return 0  # 0不是正整数，不符合条件
        
        s = str(n)
        
        # 预处理：获取2520的所有因数
        factors = BeautifulNumbers._get_factors()
        
        # 映射函数：将LCM值映射到其在factors数组中的索引
        lcm_to_index = {}
        for i, factor in enumerate(factors):
            lcm_to_index[factor] = i
        
        from functools import lru_cache
        
        @lru_cache(maxsize=None)
        def dfs(pos: int, lcm_index: int, mod: int, is_limit: bool, is_num: bool) -> int:
            """
            数位DP递归函数
            
            参数:
            - pos: 当前处理到第几位
            - lcm_index: 当前数字的最小公倍数的索引
            - mod: 当前数字对MOD的余数
            - is_limit: 是否受到上界限制
            - is_num: 是否已开始填数字（处理前导零）
            
            返回:
            - 从当前状态开始，美丽数字的个数
            """
            # 递归终止条件
            if pos == len(s):
                # 只有当已经填了数字，且当前数字能被其最小公倍数整除时才算美丽数字
                return 1 if (is_num and mod % factors[lcm_index] == 0) else 0
            
            res = 0
            
            # 如果还没开始填数字，可以选择跳过当前位（处理前导零）
            if not is_num:
                res += dfs(pos + 1, lcm_index, mod, False, False)
            
            # 确定当前位可以填入的数字范围
            upper = int(s[pos]) if is_limit else 9
            
            # 枚举当前位可以填入的数字
            start = 0 if is_num else 1
            for d in range(start, upper + 1):
                new_is_limit = is_limit and (d == upper)
                new_is_num = is_num or (d > 0)
                
                new_mod = (mod * 10 + d) % BeautifulNumbers.MOD
                new_lcm_index = lcm_index
                
                if not new_is_num:
                    # 还没有开始填数字，LCM保持不变
                    new_lcm_index = lcm_index
                elif not is_num:
                    # 第一次填数字
                    new_lcm_index = lcm_to_index[d]
                elif d == 0:
                    # 当前位是0，不影响LCM
                    new_lcm_index = lcm_index
                else:
                    # 计算新的LCM
                    current_lcm = factors[lcm_index]
                    new_lcm = BeautifulNumbers._lcm(current_lcm, d)
                    new_lcm_index = lcm_to_index[new_lcm]
                
                # 递归处理下一位
                res += dfs(pos + 1, new_lcm_index, new_mod, new_is_limit, new_is_num)
            
            return res
        
        # 清除缓存，避免之前的计算影响当前结果
        dfs.cache_clear()
        
        # 初始时lcm_index设为0（对应factors[0]=1）
        return dfs(0, 0, 0, True, False)
    
    @staticmethod
    def _count_beautiful_numbers_optimized(n: int) -> int:
        """
        优化版本：使用更紧凑的状态设计
        1. 由于我们只关心当前数字是否能被其非零数字的LCM整除，
           我们可以直接记录当前数字的LCM，而不是其索引
        2. 使用哈希表或数组来存储已经计算过的状态
        """
        if n < 1:
            return 0
        
        s = str(n)
        
        from functools import lru_cache
        
        @lru_cache(maxsize=None)
        def dfs_optimized(pos: int, current_lcm: int, mod: int, is_limit: bool, is_num: bool) -> int:
            """优化版数位DP递归函数"""
            # 递归终止条件
            if pos == len(s):
                # 只有当已经填了数字，且当前数字能被其最小公倍数整除时才算美丽数字
                return 1 if (is_num and mod % current_lcm == 0) else 0
            
            res = 0
            
            # 如果还没开始填数字，可以选择跳过当前位（处理前导零）
            if not is_num:
                res += dfs_optimized(pos + 1, current_lcm, mod, False, False)
            
            # 确定当前位可以填入的数字范围
            upper = int(s[pos]) if is_limit else 9
            
            # 枚举当前位可以填入的数字
            start = 0 if is_num else 1
            for d in range(start, upper + 1):
                new_is_limit = is_limit and (d == upper)
                new_is_num = is_num or (d > 0)
                
                new_mod = (mod * 10 + d) % BeautifulNumbers.MOD
                new_lcm = current_lcm
                
                if new_is_num and d > 0:
                    # 计算新的LCM
                    new_lcm = BeautifulNumbers._lcm(current_lcm, d)
                
                # 递归处理下一位
                res += dfs_optimized(pos + 1, new_lcm, new_mod, new_is_limit, new_is_num)
            
            return res
        
        # 清除缓存，避免之前的计算影响当前结果
        dfs_optimized.cache_clear()
        
        # 初始时current_lcm设为1
        return dfs_optimized(0, 1, 0, True, False)

# 测试代码
if __name__ == "__main__":
    # 测试用例1: l=1, r=10
    # 预期输出: 10 (所有数字都能被其非零数字整除)
    l1, r1 = 1, 10
    result1 = BeautifulNumbers.beautiful_numbers(l1, r1)
    print(f"测试用例1: l={l1}, r={r1}")
    print(f"美丽数字的个数: {result1}")
    
    # 测试用例2: l=12, r=15
    # 预期输出: 2 (12能被1和2整除，15能被1和5整除，但13不能被3整除，14不能被4整除)
    l2, r2 = 12, 15
    result2 = BeautifulNumbers.beautiful_numbers(l2, r2)
    print(f"\n测试用例2: l={l2}, r={r2}")
    print(f"美丽数字的个数: {result2}")