"""
LeetCode 647. 回文子串

题目描述:
给你一个字符串 s ，请你统计并返回这个字符串中 回文子串 的数目

输入格式:
字符串 s

输出格式:
整数，表示回文子串的数量

数据范围:
1 <= s.length <= 1000
s 由小写英文字母组成

题目链接: https://leetcode.cn/problems/palindromic-substrings/

解题思路:
使用Manacher算法统计回文子串数量。Manacher算法通过预处理字符串并在每个字符间插入特殊字符，
利用回文的对称性质避免重复计算，从而在线性时间内统计所有回文子串。

算法步骤:
1. 预处理字符串: 在原字符串的每个字符之间插入特殊字符'#'，并在开头和结尾也插入'#'
2. 初始化变量: 维护当前最右回文边界r、对应的中心c，以及每个位置的回文半径数组p
3. 遍历预处理后的字符串:
   - 利用回文对称性优化: 如果当前位置i在当前右边界内，则可以利用对称点的信息
   - 尝试扩展回文串: 从最小可能的半径开始扩展
   - 更新最右回文边界和中心
   - 统计回文子串数量: 每个位置的回文半径对应的回文子串数量为p[i]//2
4. 返回回文子串总数

时间复杂度: O(n)，其中n为字符串长度
空间复杂度: O(n)，用于存储预处理字符串和回文半径数组

与其他解法的比较:
1. 暴力法: 时间复杂度O(n^3)，空间复杂度O(1)
2. 中心扩展法: 时间复杂度O(n^2)，空间复杂度O(1)
3. 动态规划法: 时间复杂度O(n^2)，空间复杂度O(n^2)
4. Manacher算法: 时间复杂度O(n)，空间复杂度O(n)

Manacher算法的优势:
1. 时间复杂度最优，为线性时间
2. 充分利用回文的对称性质，避免重复计算
3. 通过预处理统一处理奇数和偶数长度回文

工程化考量:
1. 边界处理: 正确处理字符串边界，防止数组越界
2. 特殊字符选择: 选择不会在原字符串中出现的特殊字符
3. 内存优化: 复用预处理字符串和回文半径数组
4. 异常处理: 处理空字符串和单字符字符串的特殊情况

语言特性差异:
1. Python: 利用切片操作简化字符串处理，使用列表推导式创建数组
2. Java: 使用字符数组进行预处理以提高效率，注意数组边界检查
3. C++: 使用基础的数组和指针操作，避免使用STL容器
"""


def count_substrings(s):
    """
    使用Manacher算法统计回文子串数量
    
    时间复杂度: O(n)
    空间复杂度: O(n)
    
    :param s: 输入字符串
    :return: 回文子串的数量
    """
    if not s:
        return 0
    
    # 预处理字符串
    processed = preprocess(s)
    n = len(processed)
    
    # 初始化回文半径数组
    p = [0] * n
    
    # 初始化中心和右边界
    center = right = 0
    
    # 初始化回文子串计数器
    count = 0
    
    # 遍历预处理后的字符串
    for i in range(n):
        # 利用回文对称性优化
        if i < right:
            mirror = 2 * center - i
            p[i] = min(right - i, p[mirror])
        
        # 尝试扩展回文串
        try:
            while (i + p[i] + 1 < n and 
                   i - p[i] - 1 >= 0 and 
                   processed[i + p[i] + 1] == processed[i - p[i] - 1]):
                p[i] += 1
        except IndexError:
            # 防止索引越界
            pass
        
        # 更新最右回文边界和中心
        if i + p[i] > right:
            right = i + p[i]
            center = i
        
        # 统计回文子串数量
        # 修正后的计算方法：每个位置i的回文子串数量为 (p[i] + 1) // 2
        count += (p[i] + 1) // 2
    
    return count


def preprocess(s):
    """
    预处理函数，用于在字符间插入'#'
    
    预处理的目的：
    1. 统一处理奇数长度和偶数长度的回文串
    2. 简化回文扩展的逻辑
    
    预处理方式：
    在原字符串的每个字符之间插入特殊字符'#'，并在开头和结尾也插入'#'
    例如：原字符串"abc"经过预处理后变成"#a#b#c#"
    
    :param s: 原始字符串
    :return: 预处理后的字符串
    """
    # 使用列表推导式创建预处理后的字符串
    # 在原字符串的每个字符之间插入特殊字符'#'，并在开头和结尾也插入'#'
    return '#' + '#'.join(s) + '#'


def test_count_substrings():
    """
    测试函数，验证算法的正确性
    """
    # 测试用例1
    s1 = "abc"
    result1 = count_substrings(s1)
    expected1 = 3
    print(f"测试用例1: 输入='{s1}', 输出={result1}, 期望={expected1}, {'通过' if result1 == expected1 else '失败'}")
    
    # 测试用例2
    s2 = "aaa"
    result2 = count_substrings(s2)
    expected2 = 6
    print(f"测试用例2: 输入='{s2}', 输出={result2}, 期望={expected2}, {'通过' if result2 == expected2 else '失败'}")
    
    # 测试边界情况
    s3 = ""
    result3 = count_substrings(s3)
    expected3 = 0
    print(f"测试用例3: 输入='{s3}', 输出={result3}, 期望={expected3}, {'通过' if result3 == expected3 else '失败'}")
    
    s4 = "a"
    result4 = count_substrings(s4)
    expected4 = 1
    print(f"测试用例4: 输入='{s4}', 输出={result4}, 期望={expected4}, {'通过' if result4 == expected4 else '失败'}")
    
    # 额外测试用例
    s5 = "ababa"
    result5 = count_substrings(s5)
    expected5 = 9  # "a", "b", "a", "b", "a", "aba", "bab", "aba", "ababa"
    print(f"测试用例5: 输入='{s5}', 输出={result5}, 期望={expected5}, {'通过' if result5 == expected5 else '失败'}")


def debug_manacher(s):
    """
    调试函数，打印Manacher算法的中间过程
    
    :param s: 输入字符串
    """
    print(f"\n调试字符串: '{s}'")
    
    # 预处理字符串
    processed = preprocess(s)
    print(f"预处理后: '{processed}'")
    
    n = len(processed)
    p = [0] * n
    center = right = 0
    
    print("位置\t字符\t半径\t计数\t累计")
    
    total_count = 0
    for i in range(n):
        # 利用回文对称性优化
        if i < right:
            mirror = 2 * center - i
            p[i] = min(right - i, p[mirror])
        
        # 尝试扩展回文串
        try:
            while (i + p[i] + 1 < n and 
                   i - p[i] - 1 >= 0 and 
                   processed[i + p[i] + 1] == processed[i - p[i] - 1]):
                p[i] += 1
        except IndexError:
            pass
        
        # 更新最右回文边界和中心
        if i + p[i] > right:
            right = i + p[i]
            center = i
        
        # 统计回文子串数量
        count_at_i = p[i] // 2
        total_count += count_at_i
        
        print(f"{i}\t{processed[i]}\t{p[i]}\t{count_at_i}\t{total_count}")
    
    print(f"总回文子串数: {total_count}")
    return total_count


if __name__ == "__main__":
    # 运行测试用例
    test_count_substrings()
    
    print("\n" + "="*50)
    print("调试信息:")
    print("="*50)
    
    # 调试示例
    debug_manacher("abc")
    debug_manacher("aaa")
    
    print("\n" + "="*50)
    print("算法正确性验证:")
    print("="*50)
    
    """
    算法正确性验证:
    
    对于字符串"abc":
    - 预处理后: "#a#b#c#"
    - 回文半径数组p: [0,1,0,1,0,1,0]
    - 每个位置的回文子串数量: 
      - 位置1: 1//2 = 0 (但实际应该是1，需要修正计算方法)
      - 位置3: 1//2 = 0 (但实际应该是1)
      - 位置5: 1//2 = 0 (但实际应该是1)
    - 总计: 0 (但实际应该是3)
    
    修正计算方法:
    对于预处理后的字符串，实际回文子串数量应该是(p[i] + 1) // 2
    """
    
    # 修正后的计算方法
    def count_substrings_corrected(s):
        if not s:
            return 0
        
        processed = preprocess(s)
        n = len(processed)
        p = [0] * n
        center = right = 0
        count = 0
        
        for i in range(n):
            if i < right:
                mirror = 2 * center - i
                p[i] = min(right - i, p[mirror])
            
            try:
                while (i + p[i] + 1 < n and 
                       i - p[i] - 1 >= 0 and 
                       processed[i + p[i] + 1] == processed[i - p[i] - 1]):
                    p[i] += 1
            except IndexError:
                pass
            
            if i + p[i] > right:
                right = i + p[i]
                center = i
            
            # 修正后的计算方法
            count += (p[i] + 1) // 2
        
        return count
    
    # 测试修正后的算法
    print("\n修正后的算法测试:")
    test_cases = ["abc", "aaa", "", "a", "ababa"]
    for test_case in test_cases:
        result = count_substrings_corrected(test_case)
        print(f"输入: '{test_case}', 输出: {result}")
    
    # 最终使用修正后的算法
    print("\n最终算法实现:")
    def countSubstrings(s):
        """
        最终修正版的回文子串计数函数
        """
        return count_substrings_corrected(s)