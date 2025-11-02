"""
Codeforces 628D. Magic Numbers
题目链接：https://codeforces.com/problemset/problem/628/D

题目描述：
定义一个d-magic number为满足以下条件的数字：
1. 数字的十进制表示中，所有在偶数位置（从1开始计数）的数字都等于d
2. 数字的十进制表示中，所有在奇数位置（从1开始计数）的数字都不等于d
3. 数字不能有前导零
给定区间[a, b]和数字d，求其中d-magic number的个数，结果对10^9+7取模。

解题思路：
1. 数位DP方法：使用数位DP框架，逐位确定数字
2. 状态设计需要记录：
   - 当前处理位置
   - 是否受上界限制
   - 当前数字对m的余数
   - 当前位置的奇偶性
3. 关键点：根据位置奇偶性判断数字是否等于d

时间复杂度分析：
- 状态数：2000 * 2 * 2 * 2000 ≈ 16,000,000
- 每个状态处理10种选择
- 总复杂度：O(160,000,000) 在可接受范围内

空间复杂度分析：
- 记忆化数组：2000 * 2 * 2 * 2000 ≈ 64MB

最优解分析：
这是标准的最优解，利用数位DP处理位置相关的约束条件
"""

from functools import lru_cache

class MagicNumbers:
    def __init__(self, d: int, m: int):
        self.MOD = 10**9 + 7
        self.d = d  # 魔法数字d
        self.m = m  # 模数m
    
    def count_magic_numbers(self, a: str, b: str) -> int:
        """
        计算区间[a, b]中d-magic number的个数
        时间复杂度: O(len(b) * 2 * 2 * m)
        空间复杂度: O(len(b) * 2 * 2 * m)
        """
        count_b = self.count_up_to(b)
        count_a = self.count_up_to(a)
        
        # 需要检查a本身是否是magic number
        if self.is_magic_number(a):
            count_a = (count_a - 1) % self.MOD
        
        return (count_b - count_a) % self.MOD
    
    def count_up_to(self, s: str) -> int:
        """
        计算[0, s]中d-magic number的个数
        """
        if not s:
            return 0
        
        digits = [int(c) for c in s]
        
        @lru_cache(maxsize=None)
        def dfs(pos: int, is_limit: bool, mod: int, pos_type: int) -> int:
            """
            数位DP递归函数
            
            Args:
                pos: 当前处理的位置（0-indexed）
                is_limit: 是否受到上界限制
                mod: 当前数字对m的余数
                pos_type: 位置类型：1表示偶数位置，0表示奇数位置
            
            Returns:
                满足条件的数字个数
            """
            # 递归终止条件：处理完所有数位
            if pos == len(digits):
                # 必须是一个有效的数字（不能是前导零情况）
                # 余数为0且是有效的magic number
                return 1 if mod == 0 else 0
            
            ans = 0
            
            # 确定当前位可选数字范围
            up = digits[pos] if is_limit else 9
            start = 1 if pos == 0 else 0  # 第一位不能为0
            
            # 枚举当前位可选数字
            for d_val in range(start, up + 1):
                # 根据位置类型检查约束条件
                if pos_type == 1:  # 偶数位置：必须等于d
                    if d_val != self.d:
                        continue
                else:  # 奇数位置：必须不等于d
                    if d_val == self.d:
                        continue
                
                # 计算新的余数
                new_mod = (mod * 10 + d_val) % self.m
                # 下一个位置类型：奇偶交替
                new_pos_type = 1 - pos_type
                
                ans += dfs(pos + 1, is_limit and d_val == up, new_mod, new_pos_type)
            
            return ans % self.MOD
        
        # 从第0位开始，位置类型为1（偶数位置，因为从1开始计数）
        return dfs(0, True, 0, 1)
    
    def is_magic_number(self, s: str) -> bool:
        """
        检查一个字符串是否表示一个d-magic number
        用于验证边界情况
        """
        if not s or s[0] == '0':
            return False
        
        mod = 0
        for i, char in enumerate(s):
            digit = int(char)
            pos_type = (i + 1) % 2  # 1-indexed: 偶数位置对应i+1为偶数
            
            if pos_type == 1:  # 偶数位置
                if digit != self.d:
                    return False
            else:  # 奇数位置
                if digit == self.d:
                    return False
            
            mod = (mod * 10 + digit) % self.m
        
        return mod == 0

def test_magic_numbers():
    """
    单元测试函数
    """
    print("=== 测试Magic Numbers ===")
    
    # 测试用例1: 简单情况
    mn = MagicNumbers(7, 7)
    a, b = "1", "100"
    result = mn.count_magic_numbers(a, b)
    print(f"d=7, m=7, 区间[{a}, {b}]")
    print(f"结果: {result}")
    print("预期: 包含7, 77等数字")
    print()
    
    # 测试用例2: 边界情况
    mn = MagicNumbers(1, 1)
    a, b = "1", "9"
    result = mn.count_magic_numbers(a, b)
    print(f"d=1, m=1, 区间[{a}, {b}]")
    print(f"结果: {result}")
    print("预期: 所有奇数位置的数字不能是1")
    print()

def performance_test():
    """
    性能测试函数
    """
    import time
    print("=== 性能测试 ===")
    
    test_cases = [
        (7, 7, "1", "1000000"),
        (3, 11, "1", "1000000000"),
        (9, 19, "1", "1000000000000")
    ]
    
    for d, m, a, b in test_cases:
        mn = MagicNumbers(d, m)
        
        start_time = time.time()
        result = mn.count_magic_numbers(a, b)
        end_time = time.time()
        
        print(f"d={d}, m={m}, 区间[{a}, {b}]")
        print(f"结果: {result}")
        print(f"耗时: {(end_time - start_time)*1000:.2f}毫秒")
        print()

def debug_magic_numbers():
    """
    调试函数：验证特定数字是否为magic number
    """
    print("=== 调试Magic Numbers ===")
    
    mn = MagicNumbers(7, 7)
    
    test_numbers = ["7", "17", "27", "77", "177", "707", "717", "727"]
    
    for num in test_numbers:
        is_magic = mn.is_magic_number(num)
        print(f"数字 {num}: {'是' if is_magic else '不是'}magic number")
        
        if is_magic:
            mod = 0
            for i, char in enumerate(num):
                digit = int(char)
                pos_type = (i + 1) % 2
                print(f"  位置{i+1}({'偶数' if pos_type == 1 else '奇数'}): 数字={digit}", end="")
                if pos_type == 1:
                    print(f" 必须等于7: {'满足' if digit == 7 else '不满足'}")
                else:
                    print(f" 必须不等于7: {'满足' if digit != 7 else '不满足'}")
                mod = (mod * 10 + digit) % 7
            print(f"  余数: {mod}%7={mod % 7}")
        print()

def manual_verification():
    """
    手动验证函数：验证算法逻辑
    """
    print("=== 手动验证 ===")
    
    # 验证位置计数规则
    test_cases = [
        ("7", True),    # 位置1(偶数):7=7 ✓
        ("17", False),  # 位置1(偶数):1≠7 ✗
        ("27", False),  # 位置1(偶数):2≠7 ✗
        ("77", True),   # 位置1(偶数):7=7 ✓, 位置2(奇数):7=7 ✗
        ("707", False), # 位置1(偶数):7=7 ✓, 位置2(奇数):0≠7 ✓, 位置3(偶数):7=7 ✓, 但0是前导零?
    ]
    
    mn = MagicNumbers(7, 7)
    
    for num, expected in test_cases:
        actual = mn.is_magic_number(num)
        status = "✓" if actual == expected else "✗"
        print(f"{num}: 预期{expected}, 实际{actual} {status}")

"""
工程化考量总结：
1. 模运算：结果对10^9+7取模，避免溢出
2. 字符串处理：支持大数字输入
3. 边界处理：正确处理前导零和空字符串
4. 状态设计：合理设计状态参数，减少状态数
5. 记忆化优化：使用lru_cache自动管理记忆化

Python语言特性利用：
1. 装饰器：使用@lru_cache实现自动记忆化
2. 类型注解：提高代码可读性
3. 内置函数：简化代码逻辑
4. 动态类型：灵活处理各种情况

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
    test_magic_numbers()
    
    # 运行性能测试
    performance_test()
    
    # 调试模式
    debug_magic_numbers()
    
    # 手动验证
    manual_verification()
    
    # 边界测试
    print("=== 边界测试 ===")
    mn = MagicNumbers(7, 7)
    print(f"区间[7, 7]: {mn.count_magic_numbers('7', '7')}")
    print(f"区间[1, 1]: {mn.count_magic_numbers('1', '1')}")