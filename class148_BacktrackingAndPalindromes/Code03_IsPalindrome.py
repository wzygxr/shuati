"""
回文数判断问题 - Python版本

问题描述：
判断一个整数是否是回文数。回文数是指正序（从左向右）和倒序（从右向左）读都是一样的整数。

算法思路：
方法1：数学方法 - 通过位运算判断回文
方法2：字符串方法 - 转换为字符串后判断
方法3：优化数学方法 - 只反转一半数字
方法4：递归方法 - 使用递归判断回文

时间复杂度分析：
- 数学方法：O(log n)，其中n为数字的位数
- 字符串方法：O(log n)，字符串转换和比较
- 优化数学方法：O(log n)，只反转一半数字
- 递归方法：O(log n)，递归深度为数字位数的一半

空间复杂度分析：
- 数学方法：O(1)，常数额外空间
- 字符串方法：O(log n)，需要存储字符串
- 优化数学方法：O(1)，常数额外空间
- 递归方法：O(log n)，递归调用栈

工程化考量：
1. 边界处理：负数、0、边界值
2. 溢出处理：Python内置大整数支持，无需担心溢出
3. 性能优化：选择合适的算法策略
4. 可测试性：设计全面的测试用例
"""

class PalindromeSolver:
    def __init__(self):
        pass
    
    def is_palindrome_math(self, x: int) -> bool:
        """
        方法1：数学方法
        通过数学运算反转数字，避免字符串转换
        """
        # 边界情况处理
        if x < 0:
            return False  # 负数不是回文数
        if x < 10:
            return True  # 单个数字是回文数
        if x % 10 == 0:
            return False  # 以0结尾的数字不是回文数（除了0本身）
        
        original = x
        reversed_num = 0
        
        while x > 0:
            reversed_num = reversed_num * 10 + x % 10
            x //= 10
        
        return original == reversed_num
    
    def is_palindrome_optimized(self, x: int) -> bool:
        """
        方法2：优化数学方法
        只反转一半数字，避免完全反转
        """
        # 边界情况处理
        if x < 0:
            return False
        if x < 10:
            return True
        if x % 10 == 0:
            return False
        
        reversed_num = 0
        
        # 当原始数字大于反转数字时继续
        while x > reversed_num:
            reversed_num = reversed_num * 10 + x % 10
            x //= 10
        
        # 数字长度为奇数时，通过reversed_num//10去除中间位
        return x == reversed_num or x == reversed_num // 10
    
    def is_palindrome_string(self, x: int) -> bool:
        """
        方法3：字符串方法
        转换为字符串后判断回文
        """
        if x < 0:
            return False
        
        s = str(x)
        return s == s[::-1]  # 使用切片反转字符串
    
    def is_palindrome_recursive(self, x: int) -> bool:
        """
        方法4：递归方法
        使用递归判断回文数
        """
        if x < 0:
            return False
        if x < 10:
            return True
        
        # 转换为字符串进行递归判断
        s = str(x)
        return self._is_palindrome_recursive_helper(s, 0, len(s) - 1)
    
    def _is_palindrome_recursive_helper(self, s: str, left: int, right: int) -> bool:
        """
        递归辅助函数
        """
        # 基础情况
        if left >= right:
            return True
        
        # 首尾字符不相等
        if s[left] != s[right]:
            return False
        
        # 递归判断剩余部分
        return self._is_palindrome_recursive_helper(s, left + 1, right - 1)
    
    def is_palindrome_generator(self, x: int) -> bool:
        """
        方法5：生成器方法
        使用生成器逐个比较数字的各位
        """
        if x < 0:
            return False
        if x < 10:
            return True
        
        # 获取数字的各位数字
        digits = []
        temp = x
        while temp > 0:
            digits.append(temp % 10)
            temp //= 10
        
        # 比较对称位置的数字
        left, right = 0, len(digits) - 1
        while left < right:
            if digits[left] != digits[right]:
                return False
            left += 1
            right -= 1
        
        return True

# 补充训练题目 - Python实现

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

def is_palindrome_linked_list(head) -> bool:
    """
    LeetCode 234. 回文链表
    判断一个链表是否为回文链表
    """
    # 找到链表中点
    slow = fast = head
    while fast and fast.next:
        slow = slow.next
        fast = fast.next.next
    
    # 反转后半部分链表
    prev = None
    while slow:
        next_node = slow.next
        slow.next = prev
        prev = slow
        slow = next_node
    
    # 比较前后两部分
    left, right = head, prev
    while right:
        if left.val != right.val:
            return False
        left = left.next
        right = right.next
    
    return True

# 链表节点定义（用于测试回文链表）
class ListNode:
    def __init__(self, val=0, next=None):
        self.val = val
        self.next = next

def test_palindrome():
    """测试函数"""
    solver = PalindromeSolver()
    
    # 测试用例
    test_cases = [121, -121, 10, -101, 0, 1, 12321, 12345, 1001, 9999, 1234321]
    
    print("回文数判断测试:")
    for num in test_cases:
        result1 = solver.is_palindrome_math(num)
        result2 = solver.is_palindrome_optimized(num)
        result3 = solver.is_palindrome_string(num)
        result4 = solver.is_palindrome_recursive(num)
        result5 = solver.is_palindrome_generator(num)
        
        print(f"数字: {num:8d} -> ", end="")
        print(f"数学: {'是' if result1 else '否'}, ", end="")
        print(f"优化: {'是' if result2 else '否'}, ", end="")
        print(f"字符串: {'是' if result3 else '否'}, ", end="")
        print(f"递归: {'是' if result4 else '否'}, ", end="")
        print(f"生成器: {'是' if result5 else '否'}")
    print()
    
    # 测试补充题目
    print("=== 补充训练题目测试 ===")
    
    # 测试回文串验证
    test_str = "A man, a plan, a canal: Panama"
    print(f"回文串验证: '{test_str}' -> {'是' if is_palindrome_string(test_str) else '否'}")
    
    # 测试验证回文字符串II
    test_str2 = "aba"
    test_str3 = "abca"
    print(f"验证回文字符串II: '{test_str2}' -> {'是' if valid_palindrome(test_str2) else '否'}")
    print(f"验证回文字符串II: '{test_str3}' -> {'是' if valid_palindrome(test_str3) else '否'}")
    
    # 测试最长回文子串
    test_str4 = "babad"
    print(f"最长回文子串: '{test_str4}' -> '{longest_palindrome(test_str4)}'")
    
    # 测试回文子串统计
    test_str5 = "abc"
    print(f"回文子串统计: '{test_str5}' -> {count_substrings(test_str5)}")
    
    # 测试最长回文子序列
    test_str6 = "bbbab"
    print(f"最长回文子序列: '{test_str6}' -> {longest_palindrome_subseq(test_str6)}")
    
    # 测试回文链表（简单演示）
    # 创建链表: 1->2->2->1
    head = ListNode(1)
    head.next = ListNode(2)
    head.next.next = ListNode(2)
    head.next.next.next = ListNode(1)
    # 注意：这里需要实际实现链表判断逻辑，此处仅为演示

if __name__ == "__main__":
    test_palindrome()

"""
算法技巧总结 - Python版本

核心概念：
1. 回文数判断技术：
   - 数学方法：通过数字反转判断回文
   - 优化方法：只反转一半数字，避免完全反转
   - 字符串方法：利用Python字符串操作的便利性
   - 递归方法：递归判断首尾字符
   - 生成器方法：逐个比较数字的各位

2. Python特有优势：
   - 内置大整数支持：无需担心数值溢出
   - 字符串切片：[::-1]快速反转字符串
   - 列表推导式：简化数字分解操作

3. 算法选择策略：
   - 性能要求高：使用优化数学方法
   - 代码简洁：使用字符串方法
   - 教学演示：使用递归或生成器方法

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
1. 负数：负数不是回文数
2. 零：0是回文数
3. 个位数：所有个位数都是回文数
4. 以0结尾的数：除了0本身，其他以0结尾的数不是回文数
"""