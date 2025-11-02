"""
超级回文数问题 - Python版本

问题描述：
如果一个正整数自身是回文数，而且它也是一个回文数的平方，那么我们称这个数为超级回文数。
给定两个正整数 L 和 R（以字符串形式表示），返回包含在范围 [L, R] 中的超级回文数的数目。

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

import math
from typing import List

class SuperPalindromesSolver:
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
    
    def superpalindromes_in_range(self, left: str, right: str) -> int:
        """
        方法1：枚举法
        通过生成回文数来优化搜索，避免直接遍历大范围
        """
        L = int(left)
        R = int(right)
        count = 0
        
        # 计算种子的上界（对于10^18范围，种子只需到10^5级别）
        seed_limit = 100000  # 10^5
        
        # 枚举所有可能的种子
        for seed in range(1, seed_limit):
            # 生成偶数长度的回文数
            even_pal = self.even_enlarge(seed)
            square_even = even_pal * even_pal
            
            # 检查是否在范围内且是回文数
            if L <= square_even <= R and self.is_palindrome(square_even):
                count += 1
            
            # 生成奇数长度的回文数
            odd_pal = self.odd_enlarge(seed)
            square_odd = odd_pal * odd_pal
            
            # 检查是否在范围内且是回文数
            if L <= square_odd <= R and self.is_palindrome(square_odd):
                count += 1
            
            # 如果生成的平方已经超过R，可以提前终止
            if square_even > R and square_odd > R:
                break
        
        return count
    
    def superpalindromes_in_range_table(self, left: str, right: str) -> int:
        """
        方法2：打表法（最优解）
        预计算所有可能的超级回文数，查询时直接统计
        """
        L = int(left)
        R = int(right)
        count = 0
        
        # 统计在范围内的超级回文数
        for num in self.super_palindromes:
            if L <= num <= R:
                count += 1
        
        return count
    
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

def is_palindrome_number(x: int) -> bool:
    """
    LeetCode 9. 回文数
    判断一个整数是否是回文数
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

def largest_palindrome(n: int) -> int:
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
                    return palindrome % 1337
    
    return -1

def is_palindrome_string(s: str) -> bool:
    """
    LeetCode 125. 验证回文串
    给定一个字符串，验证它是否是回文串，只考虑字母和数字字符，可以忽略字母的大小写
    """
    left, right = 0, len(s) - 1
    
    while left < right:
        # 跳过非字母数字字符
        while left < right and not s[left].isalnum():
            left += 1
        while left < right and not s[right].isalnum():
            right -= 1
        
        # 比较字符（忽略大小写）
        if s[left].lower() != s[right].lower():
            return False
        
        left += 1
        right -= 1
    
    return True

def valid_palindrome(s: str) -> bool:
    """
    LeetCode 680. 验证回文字符串 II
    给定一个非空字符串s，最多删除一个字符，判断是否能成为回文字符串
    """
    def is_palindrome_range(left: int, right: int) -> bool:
        while left < right:
            if s[left] != s[right]:
                return False
            left += 1
            right -= 1
        return True
    
    left, right = 0, len(s) - 1
    
    while left < right:
        if s[left] != s[right]:
            # 尝试删除左边或右边的字符
            return is_palindrome_range(left + 1, right) or is_palindrome_range(left, right - 1)
        left += 1
        right -= 1
    
    return True

def longest_palindrome(s: str) -> str:
    """
    LeetCode 5. 最长回文子串
    使用中心扩展法找到最长回文子串
    """
    if not s:
        return ""
    
    def expand_around_center(left: int, right: int) -> int:
        while left >= 0 and right < len(s) and s[left] == s[right]:
            left -= 1
            right += 1
        return right - left - 1
    
    start, end = 0, 0
    
    for i in range(len(s)):
        # 奇数长度回文
        len1 = expand_around_center(i, i)
        # 偶数长度回文
        len2 = expand_around_center(i, i + 1)
        
        max_len = max(len1, len2)
        
        if max_len > end - start:
            start = i - (max_len - 1) // 2
            end = i + max_len // 2
    
    return s[start:end + 1]

def count_substrings(s: str) -> int:
    """
    LeetCode 647. 回文子串
    统计字符串中的回文子串数目
    """
    def expand_around_center(left: int, right: int) -> int:
        count = 0
        while left >= 0 and right < len(s) and s[left] == s[right]:
            count += 1
            left -= 1
            right += 1
        return count
    
    total = 0
    for i in range(len(s)):
        # 奇数长度回文子串
        total += expand_around_center(i, i)
        # 偶数长度回文子串
        total += expand_around_center(i, i + 1)
    
    return total

def longest_palindrome_subseq(s: str) -> int:
    """
    LeetCode 516. 最长回文子序列
    找出字符串中最长的回文子序列的长度
    """
    n = len(s)
    # dp[i][j]表示s[i:j+1]的最长回文子序列长度
    dp = [[0] * n for _ in range(n)]
    
    # 单个字符的回文子序列长度为1
    for i in range(n):
        dp[i][i] = 1
    
    # 从短到长填充dp数组
    for length in range(2, n + 1):
        for i in range(n - length + 1):
            j = i + length - 1
            if s[i] == s[j]:
                dp[i][j] = dp[i + 1][j - 1] + 2
            else:
                dp[i][j] = max(dp[i + 1][j], dp[i][j - 1])
    
    return dp[0][n - 1]

def test_super_palindromes():
    """测试函数"""
    solver = SuperPalindromesSolver()
    
    # 测试用例1
    left1 = "4"
    right1 = "1000"
    result1 = solver.superpalindromes_in_range(left1, right1)
    result1_table = solver.superpalindromes_in_range_table(left1, right1)
    
    print("测试用例1:")
    print(f"范围: [{left1}, {right1}]")
    print(f"枚举法结果: {result1}")
    print(f"打表法结果: {result1_table}")
    print()
    
    # 测试用例2
    left2 = "1"
    right2 = "2"
    result2 = solver.superpalindromes_in_range(left2, right2)
    result2_table = solver.superpalindromes_in_range_table(left2, right2)
    
    print("测试用例2:")
    print(f"范围: [{left2}, {right2}]")
    print(f"枚举法结果: {result2}")
    print(f"打表法结果: {result2_table}")
    print()
    
    # 测试补充题目
    print("=== 补充训练题目测试 ===")
    
    # 测试回文数判断
    print(f"回文数判断: 12321 -> {'是' if is_palindrome_number(12321) else '否'}")
    print(f"回文数判断: 12345 -> {'是' if is_palindrome_number(12345) else '否'}")
    
    # 测试最大回文数乘积
    print(f"最大回文数乘积(n=2): {largest_palindrome(2)}")
    
    # 测试回文串验证
    test_str = "A man, a plan, a canal: Panama"
    print(f"回文串验证: '{test_str}' -> {'是' if is_palindrome_string(test_str) else '否'}")
    
    # 测试最长回文子串
    test_str2 = "babad"
    print(f"最长回文子串: '{test_str2}' -> '{longest_palindrome(test_str2)}'")
    
    # 测试回文子串统计
    test_str3 = "abc"
    print(f"回文子串统计: '{test_str3}' -> {count_substrings(test_str3)}")
    
    # 测试最长回文子序列
    test_str4 = "bbbab"
    print(f"最长回文子序列: '{test_str4}' -> {longest_palindrome_subseq(test_str4)}")

if __name__ == "__main__":
    test_super_palindromes()

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
   - 动态类型：灵活的数值处理

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
"""