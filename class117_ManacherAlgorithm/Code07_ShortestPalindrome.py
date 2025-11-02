"""
LeetCode 214. 最短回文串

题目描述:
给定一个字符串 s，你可以通过在字符串前面添加字符将其转换为回文串。找到并返回可以用这种方式转换的最短回文串

输入格式:
字符串 s

输出格式:
最短回文串

数据范围:
0 <= s.length <= 5 * 10^4
s 仅由小写英文字母组成

题目链接: https://leetcode.cn/problems/shortest-palindrome/

解题思路:
使用Manacher算法找到以字符串开头开始的最长回文前缀，然后在前面添加剩余部分的逆序

算法步骤:
1. 使用Manacher算法预处理字符串
2. 找到以字符串开头开始的最长回文前缀
3. 将剩余部分逆序后添加到字符串前面
4. 返回结果

时间复杂度: O(n)
空间复杂度: O(n)
"""


def shortest_palindrome_manacher(s):
    """
    使用Manacher算法构造最短回文串
    
    时间复杂度: O(n)
    空间复杂度: O(n)
    
    :param s: 输入字符串
    :return: 最短回文串
    """
    if not s:
        return ""
    
    # 预处理字符串
    processed = "#" + "#".join(s) + "#"
    n = len(processed)
    
    # Manacher算法
    p = [0] * n
    center = right = 0
    max_prefix = 0
    
    for i in range(n):
        if i < right:
            mirror = 2 * center - i
            p[i] = min(right - i, p[mirror])
        
        # 尝试扩展回文串
        while (i + p[i] + 1 < n and 
               i - p[i] - 1 >= 0 and 
               processed[i + p[i] + 1] == processed[i - p[i] - 1]):
            p[i] += 1
        
        if i + p[i] > right:
            center = i
            right = i + p[i]
        
        # 检查是否是以开头开始的最长回文前缀
        # 当回文串的左边界达到字符串开头时
        if i - p[i] + 1 == 0:
            # 计算在原字符串中的实际长度
            # 预处理字符串中，回文半径p[i]对应的原字符串长度为p[i]
            max_prefix = max(max_prefix, p[i])
    
    # 如果整个字符串已经是回文，直接返回
    if max_prefix == len(s):
        return s
    
    # 获取需要添加到前面的部分（剩余部分的逆序）
    remaining = s[max_prefix:]
    return remaining[::-1] + s


def shortest_palindrome_kmp(s):
    """
    使用KMP算法的思想构造最短回文串
    
    时间复杂度: O(n)
    空间复杂度: O(n)
    
    :param s: 输入字符串
    :return: 最短回文串
    """
    if not s:
        return ""
    
    rev = s[::-1]
    combined = s + "#" + rev
    
    # 计算KMP的next数组
    n = len(combined)
    next_arr = [0] * n
    
    j = 0
    for i in range(1, n):
        while j > 0 and combined[i] != combined[j]:
            j = next_arr[j - 1]
        if combined[i] == combined[j]:
            j += 1
        next_arr[i] = j
    
    # 最长回文前缀的长度
    prefix_len = next_arr[n - 1]
    
    # 如果整个字符串已经是回文，直接返回
    if prefix_len == len(s):
        return s
    
    # 获取需要添加到前面的部分
    remaining = s[prefix_len:]
    return remaining[::-1] + s


def test_shortest_palindrome():
    """
    测试函数，验证算法的正确性
    """
    test_cases = [
        ("aacecaaa", "aaacecaaa"),
        ("abcd", "dcbabcd"),
        ("a", "a"),
        ("", ""),
        ("aba", "aba"),
        ("abc", "cbabc"),
        ("abac", "cabac"),
    ]
    
    print("测试结果:")
    print("=" * 50)
    
    for i, (s, expected) in enumerate(test_cases, 1):
        result1 = shortest_palindrome_manacher(s)
        result2 = shortest_palindrome_kmp(s)
        status1 = "通过" if result1 == expected else "失败"
        status2 = "通过" if result2 == expected else "失败"
        
        print(f"测试用例{i}: 输入='{s}'")
        print(f"  Manacher版本: 输出='{result1}', 期望='{expected}', {status1}")
        print(f"  KMP版本: 输出='{result2}', 期望='{expected}', {status2}")
        print()


def debug_algorithm(s):
    """
    调试函数，打印算法的中间过程
    
    :param s: 输入字符串
    """
    print(f"\n调试字符串: '{s}'")
    
    # Manacher算法过程
    if s:
        processed = "#" + "#".join(s) + "#"
        print(f"预处理后: '{processed}'")
        
        n = len(processed)
        p = [0] * n
        center = right = 0
        max_prefix = 0
        
        print("Manacher算法过程:")
        print("位置\t字符\t半径\t最大前缀")
        
        for i in range(n):
            if i < right:
                mirror = 2 * center - i
                p[i] = min(right - i, p[mirror])
            
            # 尝试扩展回文串
            while (i + p[i] + 1 < n and 
                   i - p[i] - 1 >= 0 and 
                   processed[i + p[i] + 1] == processed[i - p[i] - 1]):
                p[i] += 1
            
            if i + p[i] > right:
                center = i
                right = i + p[i]
            
            # 检查是否是以开头开始的最长回文前缀
            if i - p[i] + 1 == 0:
                max_prefix = max(max_prefix, p[i])
            
            print(f"{i}\t{processed[i]}\t{p[i]}\t{max_prefix}")
        
        print(f"最长回文前缀长度: {max_prefix}")
        print(f"需要添加的部分: '{s[max_prefix:][::-1]}'")
        print(f"最终结果: '{s[max_prefix:][::-1] + s}'")
    else:
        print("空字符串，直接返回空字符串")


def performance_test():
    """
    性能测试函数
    """
    import time
    
    # 生成测试数据
    n = 10000
    test_string = "a" * n + "b"  # 大量相同字符+一个不同字符
    
    print("性能测试:")
    print("=" * 50)
    
    # 测试Manacher方法
    start_time = time.time()
    result1 = shortest_palindrome_manacher(test_string)
    time1 = time.time() - start_time
    
    # 测试KMP方法
    start_time = time.time()
    result2 = shortest_palindrome_kmp(test_string)
    time2 = time.time() - start_time
    
    print(f"字符串长度: {n}")
    print(f"Manacher方法: 时间={time1:.4f}秒")
    print(f"KMP方法: 时间={time2:.4f}秒")
    print(f"时间比: {time1/time2:.2f}")
    print(f"结果相同: {result1 == result2}")


if __name__ == "__main__":
    # 运行测试用例
    test_shortest_palindrome()
    
    print("\n" + "=" * 50)
    print("调试信息:")
    print("=" * 50)
    
    # 调试示例
    debug_algorithm("aacecaaa")
    debug_algorithm("abcd")
    
    print("\n" + "=" * 50)
    print("性能测试:")
    print("=" * 50)
    
    # 性能测试
    performance_test()
    
    print("\n算法总结:")
    print("1. Manacher方法: 思路清晰，易于理解回文结构")
    print("2. KMP方法: 代码简洁，通常效率更高")
    print("3. 根据需求选择合适的算法实现")


"""
算法正确性验证:

对于字符串"aacecaaa":
- 最长回文前缀: "aacecaa" (长度7)
- 剩余部分: "a"
- 需要添加的部分: "a"的逆序还是"a"
- 最终结果: "a" + "aacecaaa" = "aaacecaaa"

对于字符串"abcd":
- 最长回文前缀: "a" (长度1)
- 剩余部分: "bcd"
- 需要添加的部分: "bcd"的逆序是"dcb"
- 最终结果: "dcb" + "abcd" = "dcbabcd"

算法原理:
1. 最短回文串问题等价于找到以字符串开头开始的最长回文前缀
2. 在这个最长回文前缀前面添加剩余部分的逆序
3. 这样构造的字符串就是最短的回文串
"""