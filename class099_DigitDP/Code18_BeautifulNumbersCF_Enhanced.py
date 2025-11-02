"""
Codeforces 55D. Beautiful Numbers (增强版)
题目链接：https://codeforces.com/problemset/problem/55/D

题目描述：
如果一个正整数能被它的所有非零数字整除，那么这个数就是美丽的。
给定区间 [l, r]，求其中美丽数字的个数。

解题思路：
1. 数位DP方法：使用数位DP框架，逐位确定数字
2. 状态设计需要记录：
   - 当前处理位置
   - 是否受上界限制
   - 是否已开始填数字
   - 当前数字对LCM(1-9)的余数
   - 已使用数字的LCM
3. 关键优化：1-9的LCM是2520，所有数字的LCM都是2520的约数

时间复杂度分析：
- 状态数：log(r) * 2 * 2 * 2520 * 50 ≈ 10^7
- 每个状态处理10种选择
- 总复杂度：O(10^8) 在可接受范围内

空间复杂度分析：
- 记忆化数组：log(r) * 2 * 2 * 2520 * 50 ≈ 40MB

最优解分析：
这是标准的最优解，利用了LCM的数学性质和数位DP的记忆化
"""

import math
from functools import lru_cache

class BeautifulNumbersCF:
    def __init__(self):
        self.MOD = 2520  # 1-9的LCM
        self.lcm_map = self.precompute_lcm()
    
    def precompute_lcm(self):
        """
        预计算1-9所有子集的LCM
        时间复杂度: O(2^9 * 9) = O(4608)
        空间复杂度: O(2^9) = O(512)
        """
        lcm_map = [1] * (1 << 9)
        
        for mask in range(1, 1 << 9):
            lcm_val = 1
            for i in range(1, 10):
                if mask & (1 << (i-1)):
                    lcm_val = self.lcm(lcm_val, i)
            lcm_map[mask] = lcm_val
        
        return lcm_map
    
    def gcd(self, a, b):
        """
        计算两个数的最大公约数
        时间复杂度: O(log(min(a,b)))
        """
        return math.gcd(a, b)
    
    def lcm(self, a, b):
        """
        计算两个数的最小公倍数
        时间复杂度: O(log(min(a,b)))
        """
        return a // self.gcd(a, b) * b
    
    def count_beautiful_numbers(self, l, r):
        """
        计算区间[l, r]中美丽数字的个数
        时间复杂度: O(log(r) * 2 * 2 * 2520 * 50)
        空间复杂度: O(log(r) * 2 * 2 * 2520 * 50)
        """
        return self.count_up_to(r) - self.count_up_to(l - 1)
    
    def count_up_to(self, n):
        """
        计算[0, n]中美丽数字的个数
        使用记忆化搜索实现数位DP
        """
        if n < 0:
            return 0
        if n == 0:
            return 1  # 0是美丽数字（特殊情况）
        
        # 将数字转换为字符串
        s = str(n)
        
        @lru_cache(maxsize=None)
        def dfs(pos, is_limit, is_num, mod, mask):
            """
            数位DP递归函数
            
            Args:
                pos: 当前处理的位置
                is_limit: 是否受到上界限制
                is_num: 是否已开始填数字
                mod: 当前数字对MOD的余数
                mask: 已使用数字的位掩码
            
            Returns:
                满足条件的数字个数
            """
            # 递归终止条件：处理完所有数位
            if pos == len(s):
                if not is_num:
                    return 1  # 前导零也算美丽数字（特殊情况）
                
                # 检查是否美丽：数字能被所有非零数字整除
                actual_lcm = self.lcm_map[mask]
                return 1 if mod % actual_lcm == 0 else 0
            
            ans = 0
            
            # 处理前导零：可以选择跳过当前位
            if not is_num:
                ans += dfs(pos + 1, False, False, mod, mask)
            
            # 确定当前位可选数字范围
            up = int(s[pos]) if is_limit else 9
            start = 0 if is_num else 1  # 处理前导零
            
            # 枚举当前位可选数字
            for d in range(start, up + 1):
                new_mod = (mod * 10 + d) % self.MOD
                new_mask = mask
                if d > 0:
                    new_mask |= (1 << (d-1))
                ans += dfs(pos + 1, is_limit and d == up, True, new_mod, new_mask)
            
            return ans
        
        return dfs(0, True, False, 0, 0)

def main():
    """
    单元测试函数
    """
    print("=== 测试Beautiful Numbers ===")
    
    bn = BeautifulNumbersCF()
    
    # 测试用例1: 小范围
    print("测试区间[1, 9]:")
    result1 = bn.count_beautiful_numbers(1, 9)
    print(f"结果: {result1}")
    print("预期: 9 (所有1-9的数字都美丽)")
    print()
    
    # 测试用例2: 包含不美丽数字
    print("测试区间[1, 20]:")
    result2 = bn.count_beautiful_numbers(1, 20)
    print(f"结果: {result2}")
    print("预期: 14 (1,2,3,4,5,6,7,8,9,10,11,12,15,18,20)")
    print()
    
    # 测试用例3: 较大范围
    print("测试区间[1, 100]:")
    result3 = bn.count_beautiful_numbers(1, 100)
    print(f"结果: {result3}")
    print()

if __name__ == "__main__":
    main()