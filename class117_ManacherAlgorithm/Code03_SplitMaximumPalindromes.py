"""
LeetCode 132. 分割回文串 II

题目描述:
给你一个字符串 s，请你将 s 分割成一些子串，使每个子串都是回文串。返回符合要求的最少分割次数

输入格式:
字符串 s

输出格式:
整数，表示最少分割次数

数据范围:
1 <= s.length <= 2000
s 仅由小写英文字母组成

题目链接: https://leetcode.cn/problems/palindrome-partitioning-ii/

解题思路:
使用Manacher算法预处理回文信息，然后结合动态规划求解最少分割次数。

算法步骤:
1. 使用Manacher算法预处理字符串，得到每个位置的回文半径信息
2. 构建动态规划数组dp，dp[i]表示前i个字符的最少分割次数
3. 遍历字符串，对于每个位置i，如果s[0..i]是回文串，则dp[i] = 0
4. 否则，遍历所有可能的分割点j，如果s[j+1..i]是回文串，则更新dp[i] = min(dp[i], dp[j] + 1)
5. 返回dp[n-1]

时间复杂度: O(n^2)，其中n为字符串长度
空间复杂度: O(n)，用于存储预处理字符串和回文半径数组

与其他解法的比较:
1. 纯动态规划: 时间复杂度O(n^3)，空间复杂度O(n^2)
2. Manacher+动态规划: 时间复杂度O(n^2)，空间复杂度O(n)

工程化考量:
1. 边界处理: 正确处理字符串边界，防止数组越界
2. 内存优化: 复用预处理字符串和回文半径数组
3. 异常处理: 处理空字符串和单字符字符串的特殊情况
"""


def min_cut(s):
    """
    计算最少分割次数
    
    时间复杂度: O(n^2)
    空间复杂度: O(n)
    
    :param s: 输入字符串
    :return: 最少分割次数
    """
    n = len(s)
    if n <= 1:
        return 0
    
    # 使用Manacher算法预处理
    p = manacher_preprocess(s)
    
    # 动态规划数组
    dp = [float('inf')] * n
    
    for i in range(n):
        # 如果s[0..i]是回文串，不需要分割
        if is_palindrome(p, 0, i):
            dp[i] = 0
            continue
        
        # 遍历所有可能的分割点
        for j in range(i):
            # 如果s[j+1..i]是回文串
            if is_palindrome(p, j + 1, i):
                dp[i] = min(dp[i], dp[j] + 1)
    
    return dp[n - 1]


def manacher_preprocess(s):
    """
    使用Manacher算法预处理回文信息
    
    :param s: 输入字符串
    :return: 回文半径数组
    """
    processed = "#" + "#".join(s) + "#"
    n = len(processed)
    p = [0] * n
    center = right = 0
    
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
    
    return p


def is_palindrome(p, left, right):
    """
    判断子串s[left..right]是否为回文串
    
    :param p: Manacher预处理结果
    :param left: 子串左边界
    :param right: 子串右边界
    :return: 是否为回文串
    """
    # 将原字符串位置转换为预处理字符串位置
    processed_left = left * 2 + 1
    processed_right = right * 2 + 1
    processed_center = (processed_left + processed_right) // 2
    
    # 计算回文半径是否足够覆盖整个子串
    radius = (right - left) // 2
    return p[processed_center] >= radius + 1


def min_cut_optimized(s):
    """
    优化版本：使用动态规划预处理回文信息
    
    时间复杂度: O(n^2)
    空间复杂度: O(n^2)
    
    :param s: 输入字符串
    :return: 最少分割次数
    """
    n = len(s)
    if n <= 1:
        return 0
    
    # 预处理回文信息
    is_pal = [[False] * n for _ in range(n)]
    
    # 初始化对角线（单个字符都是回文）
    for i in range(n):
        is_pal[i][i] = True
    
    # 填充回文表
    for length in range(2, n + 1):
        for i in range(n - length + 1):
            j = i + length - 1
            if s[i] == s[j]:
                if length == 2 or is_pal[i + 1][j - 1]:
                    is_pal[i][j] = True
    
    # 动态规划
    dp = [float('inf')] * n
    for i in range(n):
        if is_pal[0][i]:
            dp[i] = 0
        else:
            for j in range(i):
                if is_pal[j + 1][i]:
                    dp[i] = min(dp[i], dp[j] + 1)
    
    return dp[n - 1]


def test_min_cut():
    """
    测试函数，验证算法的正确性
    """
    test_cases = [
        ("aab", 1),
        ("a", 0),
        ("ab", 1),
        ("aba", 0),
        ("abcba", 0),
        ("abcbd", 2),
        ("leet", 2),
    ]
    
    print("测试结果:")
    print("=" * 50)
    
    for i, (s, expected) in enumerate(test_cases, 1):
        result1 = min_cut(s)
        result2 = min_cut_optimized(s)
        status1 = "通过" if result1 == expected else "失败"
        status2 = "通过" if result2 == expected else "失败"
        
        print(f"测试用例{i}: 输入='{s}'")
        print(f"  Manacher+DP: 输出={result1}, 期望={expected}, {status1}")
        print(f"  纯DP: 输出={result2}, 期望={expected}, {status2}")
        print()


def performance_test():
    """
    性能测试函数
    """
    import time
    
    # 生成测试数据
    n = 1000
    test_string = "a" + "b" * (n - 2) + "a"
    
    print("性能测试:")
    print("=" * 50)
    
    # 测试Manacher+DP方法
    start_time = time.time()
    result1 = min_cut(test_string)
    time1 = time.time() - start_time
    
    # 测试纯DP方法
    start_time = time.time()
    result2 = min_cut_optimized(test_string)
    time2 = time.time() - start_time
    
    print(f"字符串长度: {n}")
    print(f"Manacher+DP方法: 结果={result1}, 时间={time1:.4f}秒")
    print(f"纯DP方法: 结果={result2}, 时间={time2:.4f}秒")
    print(f"时间比: {time1/time2:.2f}")


def debug_algorithm(s):
    """
    调试函数，打印算法的中间过程
    
    :param s: 输入字符串
    """
    print(f"\n调试字符串: '{s}'")
    n = len(s)
    
    # 使用Manacher预处理
    p = manacher_preprocess(s)
    print("Manacher预处理完成")
    
    # 动态规划过程
    dp = [float('inf')] * n
    
    for i in range(n):
        if is_palindrome(p, 0, i):
            dp[i] = 0
            print(f"dp[{i}] = 0 (s[0..{i}]是回文)")
        else:
            for j in range(i):
                if is_palindrome(p, j + 1, i):
                    if dp[j] + 1 < dp[i]:
                        dp[i] = dp[j] + 1
                        print(f"dp[{i}] = dp[{j}] + 1 = {dp[i]} (在位置{j}分割)")
    
    print(f"最终结果: dp[{n-1}] = {dp[n-1]}")


if __name__ == "__main__":
    # 运行测试用例
    test_min_cut()
    
    print("\n" + "=" * 50)
    print("调试信息:")
    print("=" * 50)
    
    # 调试示例
    debug_algorithm("aab")
    debug_algorithm("ab")
    
    print("\n" + "=" * 50)
    print("性能测试:")
    print("=" * 50)
    
    # 性能测试
    performance_test()
    
    print("\n算法总结:")
    print("1. Manacher+动态规划方法适用于大规模数据")
    print("2. 纯动态规划方法实现更简单，适用于小规模数据")
    print("3. 根据实际需求选择合适的算法实现")


"""
算法正确性验证:

对于字符串"aab":
- s[0..0] = "a" 是回文，dp[0] = 0
- s[0..1] = "aa" 是回文，dp[1] = 0  
- s[0..2] = "aab" 不是回文
  - 在j=0处分割: s[1..2] = "ab" 不是回文
  - 在j=1处分割: s[2..2] = "b" 是回文，dp[2] = dp[1] + 1 = 1
- 最终结果: 1

对于字符串"ab":
- s[0..0] = "a" 是回文，dp[0] = 0
- s[0..1] = "ab" 不是回文
  - 在j=0处分割: s[1..1] = "b" 是回文，dp[1] = dp[0] + 1 = 1
- 最终结果: 1
"""