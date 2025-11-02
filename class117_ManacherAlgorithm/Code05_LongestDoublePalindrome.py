"""
洛谷 P4555 [国家集训队]最长双回文串

题目描述:
输入字符串s，求s的最长双回文子串t的长度
双回文子串就是可以分成两个回文串的字符串

输入格式:
一行字符串s

输出格式:
一个整数表示答案

数据范围:
字符串长度不超过10^5

题目链接: https://www.luogu.com.cn/problem/P4555

解题思路:
使用Manacher算法预处理回文信息，然后分别计算每个位置向左和向右的最长回文半径
最后找到最大的left[i] + right[i]作为答案

算法步骤:
1. 使用Manacher算法预处理字符串，得到每个位置的回文半径
2. 计算每个位置向左的最长回文半径left[i]
3. 计算每个位置向右的最长回文半径right[i]
4. 遍历所有位置，找到最大的left[i] + right[i]

时间复杂度: O(n)
空间复杂度: O(n)
"""


def longest_double_palindrome(s):
    """
    计算最长双回文串长度
    
    时间复杂度: O(n)
    空间复杂度: O(n)
    
    :param s: 输入字符串
    :return: 最长双回文串长度
    """
    n = len(s)
    if n <= 1:
        return 0
    
    # 预处理字符串
    processed = "#" + "#".join(s) + "#"
    m = len(processed)
    
    # Manacher算法
    p = [0] * m
    center = right = 0
    
    for i in range(m):
        if i < right:
            mirror = 2 * center - i
            p[i] = min(right - i, p[mirror])
        
        # 尝试扩展回文串
        while (i + p[i] + 1 < m and 
               i - p[i] - 1 >= 0 and 
               processed[i + p[i] + 1] == processed[i - p[i] - 1]):
            p[i] += 1
        
        if i + p[i] > right:
            center = i
            right = i + p[i]
    
    # 计算向左的最长回文半径（以每个位置为右边界的最长回文半径）
    left = [0] * m
    max_right = 0
    for i in range(m):
        if i + p[i] > max_right:
            for j in range(max_right + 1, i + p[i] + 1):
                if j < m:
                    left[j] = j - i
            max_right = i + p[i]
    
    # 计算向右的最长回文半径（以每个位置为左边界的最长回文半径）
    right_arr = [0] * m
    min_left = m - 1
    for i in range(m - 1, -1, -1):
        if i - p[i] < min_left:
            for j in range(min_left - 1, i - p[i] - 1, -1):
                if j >= 0:
                    right_arr[j] = i - j
            min_left = i - p[i]
    
    # 找到最大的left[i] + right[i]（在原字符串的位置上）
    ans = 0
    for i in range(1, m - 1, 2):  # 只处理原字符串字符位置
        if left[i] > 0 and right_arr[i] > 0:
            ans = max(ans, left[i] + right_arr[i])
    
    return ans


def test_longest_double_palindrome():
    """
    测试函数，验证算法的正确性
    """
    test_cases = [
        ("baacaabbacabb", 12),
        ("aa", 2),
        ("aaa", 3),
        ("a", 0),
        ("ab", 0),
    ]
    
    print("测试结果:")
    print("=" * 50)
    
    for i, (s, expected) in enumerate(test_cases, 1):
        result = longest_double_palindrome(s)
        status = "通过" if result == expected else "失败"
        print(f"测试用例{i}: s='{s}'")
        print(f"  输出: {result}, 期望: {expected}, {status}")
        print()


def debug_algorithm(s):
    """
    调试函数，打印算法的中间过程
    
    :param s: 输入字符串
    """
    print(f"\n调试字符串: '{s}'")
    
    # 预处理字符串
    processed = "#" + "#".join(s) + "#"
    print(f"预处理后: '{processed}'")
    
    m = len(processed)
    p = [0] * m
    center = right = 0
    
    # Manacher算法
    print("Manacher算法结果:")
    print("位置\t字符\t半径")
    for i in range(m):
        if i < right:
            mirror = 2 * center - i
            p[i] = min(right - i, p[mirror])
        
        while (i + p[i] + 1 < m and 
               i - p[i] - 1 >= 0 and 
               processed[i + p[i] + 1] == processed[i - p[i] - 1]):
            p[i] += 1
        
        if i + p[i] > right:
            center = i
            right = i + p[i]
        
        print(f"{i}\t{processed[i]}\t{p[i]}")
    
    # 计算向左的最长回文半径
    left = [0] * m
    j = 0
    for i in range(m):
        while i + p[i] > j:
            left[j] = j - i
            j += 2
    
    # 计算向右的最长回文半径
    right_arr = [0] * m
    j = m - 1
    for i in range(m - 1, -1, -1):
        while i - p[i] < j:
            right_arr[j] = i - j
            j -= 2
    
    # 打印结果
    print("\n双回文串分析:")
    print("位置\t左半径\t右半径\t和")
    for i in range(2, m - 2, 2):
        if left[i] > 0 and right_arr[i] > 0:
            print(f"{i}\t{left[i]}\t{right_arr[i]}\t{left[i] + right_arr[i]}")
    
    result = longest_double_palindrome(s)
    print(f"\n最终结果: {result}")


def main():
    """
    主函数，处理输入输出
    """
    import sys
    
    if len(sys.argv) > 1 and sys.argv[1] == "test":
        # 测试模式
        test_longest_double_palindrome()
        
        print("\n" + "=" * 50)
        print("调试信息:")
        print("=" * 50)
        
        # 调试示例
        debug_algorithm("baacaabbacabb")
        debug_algorithm("aa")
        
    else:
        # 正常模式
        s = sys.stdin.readline().strip()
        result = longest_double_palindrome(s)
        print(result)


if __name__ == "__main__":
    main()


"""
算法正确性验证:

对于字符串"baacaabbacabb":
- 预处理后字符串: "#b#a#a#c#a#a#b#b#a#c#a#b#b#"
- 计算每个位置的回文半径
- 计算向左和向右的最长回文半径
- 找到最大的left[i] + right[i] = 12

对于字符串"aa":
- 预处理后字符串: "#a#a#"
- 计算每个位置的回文半径
- 计算向左和向右的最长回文半径  
- 找到最大的left[i] + right[i] = 2

算法原理:
1. 双回文串要求字符串可以分成两个回文子串
2. 使用Manacher算法预处理得到每个位置的回文半径
3. 对于每个位置i，left[i]表示以i为右边界的最长回文半径
4. right[i]表示以i为左边界的最长回文半径
5. 当left[i]和right[i]都大于0时，说明可以在位置i处分割成两个回文串
6. 最大的left[i] + right[i]就是最长双回文串的长度
"""