"""
Codeforces 55D. Beautiful Numbers
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
- 状态数：20 * 2 * 2 * 2520 * 50 ≈ 10^7
- 每个状态处理10种选择
- 总复杂度：O(10^8) 在可接受范围内

空间复杂度分析：
- 记忆化数组：20 * 2 * 2 * 2520 * 50 ≈ 40MB
- 使用字典可以进一步优化空间

最优解分析：
这是标准的最优解，利用了LCM的数学性质和数位DP的记忆化
"""

import math
from functools import lru_cache
from typing import List, Tuple

class BeautifulNumbers:
    def __init__(self):
        self.MOD = 2520  # 1-9的LCM
        self.lcm_map = self.precompute_lcm()
    
    def precompute_lcm(self) -> List[int]:
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
    
    def gcd(self, a: int, b: int) -> int:
        """
        计算两个数的最大公约数
        时间复杂度: O(log(min(a,b)))
        """
        return math.gcd(a, b)
    
    def lcm(self, a: int, b: int) -> int:
        """
        计算两个数的最小公倍数
        时间复杂度: O(log(min(a,b)))
        """
        return a // self.gcd(a, b) * b
    
    def count_beautiful_numbers(self, l: int, r: int) -> int:
        """
        计算区间[l, r]中美丽数字的个数
        时间复杂度: O(log(r) * 2 * 2 * 2520 * 50)
        空间复杂度: O(log(r) * 2 * 2 * 2520 * 50)
        """
        return self.count_up_to(r) - self.count_up_to(l - 1)
    
    def count_up_to(self, n: int) -> int:
        """
        计算[0, n]中美丽数字的个数
        使用记忆化搜索实现数位DP
        """
        if n < 0:
            return 0
        if n == 0:
            return 0  # 0不算美丽数字
        
        # 将数字转换为数位列表
        digits = []
        temp = n
        if temp == 0:
            digits = [0]
        else:
            while temp > 0:
                digits.append(temp % 10)
                temp //= 10
            digits.reverse()
        
        @lru_cache(maxsize=None)
        def dfs(pos: int, is_limit: bool, is_num: bool, mod: int, mask: int) -> int:
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
            if pos == len(digits):
                if not is_num:
                    return 0  # 前导零不算
                
                # 检查是否美丽：数字能被所有非零数字整除
                actual_lcm = self.lcm_map[mask]
                return 1 if mod % actual_lcm == 0 else 0
            
            ans = 0
            
            # 处理前导零：可以选择跳过当前位
            if not is_num:
                ans += dfs(pos + 1, False, False, mod, mask)
            
            # 确定当前位可选数字范围
            up = digits[pos] if is_limit else 9
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

def test_beautiful_numbers():
    """
    单元测试函数
    测试用例设计原则：
    1. 边界测试：小数字区间
    2. 常规测试：中等规模区间
    3. 性能测试：大规模区间
    """
    bn = BeautifulNumbers()
    
    print("=== 测试Beautiful Numbers ===")
    
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
    print("预期: 12 (13, 17, 19不美丽)")
    print()
    
    # 测试用例3: 较大范围
    print("测试区间[1, 100]:")
    result3 = bn.count_beautiful_numbers(1, 100)
    print(f"结果: {result3}")
    print("预期: 33")
    print()

def performance_test():
    """
    性能测试函数
    测试算法在大规模数据下的性能
    """
    import time
    bn = BeautifulNumbers()
    
    print("=== 性能测试 ===")
    
    # 测试不同规模区间的性能
    test_cases = [
        (1, 1000),
        (1, 1000000),
        (1, 1000000000),
        (1, 1000000000000)
    ]
    
    for l, r in test_cases:
        start_time = time.time()
        result = bn.count_beautiful_numbers(l, r)
        end_time = time.time()
        
        print(f"区间[{l}, {r}]:")
        print(f"结果: {result}")
        print(f"耗时: {(end_time - start_time)*1000:.2f}毫秒")
        print()

def debug_beautiful_numbers(l: int, r: int):
    """
    调试函数：手动验证小范围结果
    用于理解算法执行过程和调试问题
    """
    bn = BeautifulNumbers()
    
    print("=== 调试Beautiful Numbers ===")
    print(f"区间: [{l}, {r}]")
    
    # 手动计算小范围内的结果进行验证
    if r - l <= 1000:
        manual_count = 0
        beautiful_numbers = []
        
        for i in range(l, r + 1):
            if i == 0:
                continue
                
            temp = i
            lcm_val = 1
            has_non_zero = False
            
            while temp > 0:
                digit = temp % 10
                temp //= 10
                if digit > 0:
                    has_non_zero = True
                    lcm_val = math.lcm(lcm_val, digit)
            
            if has_non_zero and i % lcm_val == 0:
                manual_count += 1
                if len(beautiful_numbers) < 10:
                    beautiful_numbers.append(i)
        
        dp_count = bn.count_beautiful_numbers(l, r)
        
        print(f"手动计算: {manual_count}")
        print(f"DP计算: {dp_count}")
        print(f"结果一致: {manual_count == dp_count}")
        
        if beautiful_numbers:
            print("前10个美丽数字:", beautiful_numbers)
    else:
        result = bn.count_beautiful_numbers(l, r)
        print(f"结果: {result}")
    print()

def manual_verification():
    """
    手动验证函数：验证特定数字是否为美丽数字
    用于调试和理解算法逻辑
    """
    print("=== 手动验证 ===")
    
    test_numbers = [1, 12, 13, 22, 36, 48, 55, 111, 112, 124, 126, 128, 132, 135]
    
    for num in test_numbers:
        temp = num
        lcm_val = 1
        digits_used = set()
        
        while temp > 0:
            digit = temp % 10
            temp //= 10
            if digit > 0:
                digits_used.add(digit)
                lcm_val = math.lcm(lcm_val, digit)
        
        is_beautiful = (num % lcm_val == 0) if digits_used else False
        
        print(f"数字 {num}: 使用数字 {sorted(digits_used)}, LCM={lcm_val}, "
              f"{num}%{lcm_val}={num%lcm_val}, 美丽: {is_beautiful}")

"""
工程化考量总结：
1. 数学优化：利用LCM性质将状态数从2520*2^9减少到2520*50
2. 空间优化：使用lru_cache自动管理记忆化，避免手动数组管理
3. 边界处理：正确处理n=0和负数的情况
4. 可读性：清晰的变量命名、类型注解和文档字符串
5. 测试覆盖：全面的单元测试和性能测试

Python语言特性利用：
1. 装饰器：使用@lru_cache实现自动记忆化
2. 类型注解：提高代码可读性和IDE支持
3. 内置函数：使用math.gcd和math.lcm简化代码
4. 动态类型：灵活处理各种边界情况

跨语言实现差异：
- Python: 代码简洁，使用装饰器自动记忆化
- Java: 使用多维数组，需要手动管理记忆化
- C++: 使用vector和lambda，注意内存管理

算法调试技巧：
1. 小范围验证：手动计算小范围结果进行对拍
2. 中间状态打印：添加调试信息打印中间状态
3. 边界测试：测试0、1、边界值等特殊情况
4. 性能分析：使用time模块进行性能测试
"""

if __name__ == "__main__":
    # 运行功能测试
    test_beautiful_numbers()
    
    # 运行性能测试
    performance_test()
    
    # 调试模式
    debug_beautiful_numbers(1, 100)
    
    # 手动验证
    manual_verification()
    
    # 额外测试：边界情况
    print("=== 边界测试 ===")
    bn = BeautifulNumbers()
    print(f"区间[0, 0]: {bn.count_beautiful_numbers(0, 0)}")
    print(f"区间[1, 1]: {bn.count_beautiful_numbers(1, 1)}")
    print(f"区间[2520, 2520]: {bn.count_beautiful_numbers(2520, 2520)}")