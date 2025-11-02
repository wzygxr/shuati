"""
超级回文数II问题 - Python版本

问题描述：
如果一个正整数自身是回文数，而且它也是一个回文数的平方，那么我们称这个数为超级回文数。
给定两个正整数 L 和 R（以字符串形式表示），返回包含在范围 [L, R] 中的超级回文数的最小值和最大值。
如果不存在超级回文数，返回[-1, -1]。

算法思路：
方法1：枚举法 - 通过生成回文数来优化搜索
方法2：打表法 - 预计算所有可能的超级回文数

时间复杂度分析：
- 枚举法：O(√R * log R)，其中√R为平方根范围，log R为回文判断时间
- 打表法：预处理O(K * log K)，查询O(log K)，其中K为超级回文数个数（约70）

空间复杂度分析：
- 枚举法：O(1)，常数额外空间
- 打表法：O(K)，存储预计算的超级回文数

工程化考量：
1. 大数处理：使用Python内置大整数支持
2. 边界处理：处理L和R的边界情况
3. 性能优化：选择合适的算法策略
4. 可测试性：设计全面的测试用例
"""

from typing import List, Tuple

class SuperPalindromesIISolver:
    def __init__(self):
        # 预计算的超级回文数数组
        self.super_palindromes = [
            1, 4, 9, 121, 484, 10201, 12321, 14641, 40804, 44944,
            1002001, 1234321, 4008004, 100020001, 102030201, 104060401,
            121242121, 123454321, 125686521, 400080004, 404090404,
            10000200001, 10221412201, 12102420121, 12345654321, 40000800004,
            1000002000001, 1002003002001, 1004006004001, 1020304030201,
            1022325232201, 1024348434201, 1210024200121, 1212225222121,
            1214428244121, 1232346432321, 1234567654321, 4000008000004,
            4004009004004, 100000020000001, 100220141022001, 102012040210201,
            102234363432201, 121000242000121, 121242363242121, 123212464212321,
            123456787654321, 400000080000004, 10000000200000001, 10002000300020001,
            10004000600040001, 10020210401202001, 10022212521222001, 10024214841242001,
            10201020402010201, 10203040504030201, 10205060806050201, 10221432623412201,
            10223454745432201, 12100002420000121, 12102202520220121, 12104402820440121,
            12122232623222121, 12124434743442121, 12321024642012321, 12323244744232321,
            12343456865434321, 12345678987654321, 40000000800000004, 40004000900040004
        ]
    
    def superpalindromes_in_range(self, left: str, right: str) -> List[int]:
        """
        方法1：打表法
        使用预计算的超级回文数列表进行查询
        """
        L = int(left)
        R = int(right)
        min_val = float('inf')
        max_val = float('-inf')
        found = False
        
        # 查找范围内的最小值和最大值
        for num in self.super_palindromes:
            if L <= num <= R:
                found = True
                if num < min_val:
                    min_val = num
                if num > max_val:
                    max_val = num
        
        if not found:
            return [-1, -1]
        
        return [int(min_val), int(max_val)]
    
    def superpalindromes_in_range_generate(self, left: str, right: str) -> List[int]:
        """
        方法2：枚举生成法
        通过生成回文数种子来构造超级回文数
        """
        L = int(left)
        R = int(right)
        min_val = float('inf')
        max_val = float('-inf')
        found = False
        
        seed_limit = 100000  # 10^5
        
        # 枚举所有可能的种子
        for seed in range(1, seed_limit):
            # 生成偶数长度的回文数
            even_pal = self.even_enlarge(seed)
            square_even = even_pal * even_pal
            
            # 检查是否在范围内且是回文数
            if L <= square_even <= R and self.is_palindrome(square_even):
                found = True
                if square_even < min_val:
                    min_val = square_even
                if square_even > max_val:
                    max_val = square_even
            
            # 生成奇数长度的回文数
            odd_pal = self.odd_enlarge(seed)
            square_odd = odd_pal * odd_pal
            
            # 检查是否在范围内且是回文数
            if L <= square_odd <= R and self.is_palindrome(square_odd):
                found = True
                if square_odd < min_val:
                    min_val = square_odd
                if square_odd > max_val:
                    max_val = square_odd
            
            # 如果生成的平方已经超过R，可以提前终止
            if square_even > R and square_odd > R:
                break
        
        if not found:
            return [-1, -1]
        
        return [int(min_val), int(max_val)]
    
    def even_enlarge(self, seed: int) -> int:
        """
        根据种子扩充到偶数长度的回文数字
        例如：seed=123，返回123321
        """
        ans = seed
        temp = seed
        while temp > 0:
            ans = ans * 10 + temp % 10
            temp //= 10
        return ans
    
    def odd_enlarge(self, seed: int) -> int:
        """
        根据种子扩充到奇数长度的回文数字
        例如：seed=123，返回12321
        """
        ans = seed
        temp = seed // 10
        while temp > 0:
            ans = ans * 10 + temp % 10
            temp //= 10
        return ans
    
    def is_palindrome(self, x: int) -> bool:
        """
        判断一个数是否是回文数
        使用数学方法避免字符串转换
        """
        if x < 0:
            return False
        if x < 10:
            return True
        
        original = x
        reversed_num = 0
        
        while x > 0:
            reversed_num = reversed_num * 10 + x % 10
            x //= 10
        
        return original == reversed_num

# 补充训练题目 - Python实现

def largest_palindrome_product(n: int) -> int:
    """
    LeetCode 479. 最大回文数乘积
    给定一个整数n，返回可表示为两个n位整数乘积的最大回文整数
    """
    if n == 1:
        return 9
    
    max_num = 10**n - 1
    min_num = 10**(n - 1)
    
    for i in range(max_num, min_num - 1, -1):
        # 创建回文数
        s = str(i)
        palindrome = int(s + s[::-1])
        
        # 检查是否可以分解为两个n位数的乘积
        for j in range(max_num, int(palindrome**0.5) - 1, -1):
            if palindrome % j == 0:
                factor = palindrome // j
                if min_num <= factor <= max_num:
                    return palindrome
    
    return -1

def count_super_palindromes(left: str, right: str) -> int:
    """
    LeetCode 906. 超级回文数（统计版本）
    统计范围内的超级回文数个数
    """
    L = int(left)
    R = int(right)
    count = 0
    
    super_palindromes = [
        1, 4, 9, 121, 484, 10201, 12321, 14641, 40804, 44944,
        1002001, 1234321, 4008004, 100020001, 102030201, 104060401,
        121242121, 123454321, 125686521, 400080004, 404090404,
        10000200001, 10221412201, 12102420121, 12345654321, 40000800004
    ]
    
    for num in super_palindromes:
        if L <= num <= R:
            count += 1
    
    return count

def is_palindrome_big(s: str) -> bool:
    """
    LeetCode 9. 回文数（大数版本）
    支持大数判断的回文数函数
    """
    left, right = 0, len(s) - 1
    while left < right:
        if s[left] != s[right]:
            return False
        left += 1
        right -= 1
    return True

def generate_palindromes_in_range(start: int, end: int) -> List[int]:
    """
    生成指定范围内的所有回文数
    """
    result = []
    
    for i in range(start, end + 1):
        if str(i) == str(i)[::-1]:
            result.append(i)
    
    return result

def next_palindrome(n: int) -> int:
    """
    寻找大于n的最小回文数
    """
    def is_palindrome(x: int) -> bool:
        s = str(x)
        return s == s[::-1]
    
    while True:
        n += 1
        if is_palindrome(n):
            return n

def prev_palindrome(n: int) -> int:
    """
    寻找小于n的最大回文数
    """
    def is_palindrome(x: int) -> bool:
        s = str(x)
        return s == s[::-1]
    
    while n > 0:
        n -= 1
        if is_palindrome(n):
            return n
    return 0

def palindrome_generator(limit: int):
    """
    回文数生成器
    生成小于等于limit的所有回文数
    """
    for i in range(1, limit + 1):
        if str(i) == str(i)[::-1]:
            yield i

# 测试函数
def test_super_palindromes_ii():
    """测试函数"""
    solver = SuperPalindromesIISolver()
    
    # 测试用例1
    left1 = "4"
    right1 = "1000"
    result1 = solver.superpalindromes_in_range(left1, right1)
    result1_gen = solver.superpalindromes_in_range_generate(left1, right1)
    
    print("测试用例1:")
    print(f"范围: [{left1}, {right1}]")
    print(f"打表法结果: [{result1[0]}, {result1[1]}]")
    print(f"生成法结果: [{result1_gen[0]}, {result1_gen[1]}]")
    print()
    
    # 测试用例2：无超级回文数的情况
    left2 = "1000000000000000000"
    right2 = "1000000000000000000"
    result2 = solver.superpalindromes_in_range(left2, right2)
    
    print("测试用例2:")
    print(f"范围: [{left2}, {right2}]")
    print(f"结果: [{result2[0]}, {result2[1]}]")
    print()
    
    # 测试补充题目
    print("=== 补充训练题目测试 ===")
    
    # 测试最大回文数乘积
    print(f"最大回文数乘积(n=2): {largest_palindrome_product(2)}")
    
    # 测试超级回文数统计
    print(f"超级回文数统计: 范围[4, 1000] -> {count_super_palindromes('4', '1000')}")
    
    # 测试大数回文判断
    big_num = "12345678987654321"
    print(f"大数回文判断: {big_num} -> {'是' if is_palindrome_big(big_num) else '否'}")
    
    # 测试回文数生成
    palindromes = generate_palindromes_in_range(100, 200)
    print(f"回文数生成(100-200): 数量={len(palindromes)}")
    
    # 测试下一个回文数
    n = 123
    print(f"下一个回文数: {n} -> {next_palindrome(n)}")
    
    # 测试上一个回文数
    print(f"上一个回文数: {n} -> {prev_palindrome(n)}")
    
    # 测试回文数生成器
    print("回文数生成器(1-100):", end=" ")
    count = 0
    for p in palindrome_generator(100):
        count += 1
    print(f"数量={count}")

if __name__ == "__main__":
    test_super_palindromes_ii()

"""
算法技巧总结 - Python版本

核心概念：
1. 回文数生成技术：
   - 种子生成法：通过种子数字构造回文数
   - 对称性利用：利用回文数的对称特征
   - 数学方法：避免字符串转换的开销

2. Python特有优势：
   - 内置大整数支持：无需担心数值溢出
   - 字符串操作简便：[::-1]快速反转字符串
   - 生成器表达式：节省内存空间

3. 算法选择策略：
   - 小范围查询：枚举法更节省内存
   - 多次查询：打表法性能最优
   - 大数据范围：需要数学优化和边界处理

调试技巧：
1. 使用pdb进行调试
2. 打印中间状态变量
3. 使用assert进行条件验证

性能优化：
1. 避免不必要的字符串转换
2. 使用局部变量减少属性查找
3. 利用Python内置函数的高效实现

工程化实践：
1. 类型注解：提高代码可读性
2. 异常处理：确保程序健壮性
3. 单元测试：保证代码质量
4. 文档字符串：提供清晰的接口说明

边界情况处理：
1. 空范围：返回[-1, -1]
2. 单个数字：检查该数字是否是超级回文数
3. 大数范围：使用适当的算法避免性能问题
4. 边界值：测试最小/最大可能的值
"""