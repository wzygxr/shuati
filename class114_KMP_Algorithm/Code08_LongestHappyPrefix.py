"""
LeetCode 1392. 最长快乐前缀

题目描述：
「快乐前缀」是在原字符串中既是非空前缀也是后缀（不包括原字符串自身）的字符串。
给你一个字符串 s，请你返回它的最长快乐前缀。
如果不存在满足题意的前缀，则返回一个空字符串 ""。

示例：
输入：s = "level"
输出："l"

输入：s = "ababab"
输出："abab"

算法思路：
使用KMP算法的next数组来解决这个问题。
最长快乐前缀就是字符串的最长相等前后缀。
next[n-1]表示整个字符串的最长相等前后缀的长度。
因此，答案就是长度为next[n-1]的前缀。

时间复杂度：O(N)，其中N是字符串长度
空间复杂度：O(N)，用于存储next数组
"""


def build_next_array(s):
    """
    构建KMP算法的next数组（部分匹配表）

    next[i]表示s[0...i]子串的最长相等前后缀的长度

    算法思路：
    1. 初始化next[0] = 0
    2. 使用双指针i和j，i指向当前处理的位置，j指向前缀的末尾
    3. 如果s[i] == s[j]，说明前缀和后缀可以延长，next[i] = j + 1
    4. 如果s[i] != s[j]，需要回退j指针到next[j-1]，直到匹配或j=0

    :param s: 输入字符串
    :return: next数组
    """
    length = len(s)
    next_array = [0] * length

    # 初始化
    next_array[0] = 0
    prefix_len = 0  # 当前最长相等前后缀的长度
    i = 1  # 当前处理的位置

    # 从位置1开始处理
    while i < length:
        # 如果当前字符匹配，可以延长相等前后缀
        if s[i] == s[prefix_len]:
            prefix_len += 1
            next_array[i] = prefix_len
            i += 1
        # 如果不匹配且前缀长度大于0，需要回退
        elif prefix_len > 0:
            prefix_len = next_array[prefix_len - 1]
        # 如果不匹配且前缀长度为0，next[i] = 0
        else:
            next_array[i] = 0
            i += 1

    return next_array


def longest_prefix(s):
    """
    找到字符串的最长快乐前缀

    :param s: 输入字符串
    :return: 最长快乐前缀
    """
    # 边界条件处理
    if len(s) <= 1:
        return ""

    # 构建next数组
    next_array = build_next_array(s)

    # 最长快乐前缀的长度就是next[n-1]
    prefix_length = next_array[len(s) - 1]

    # 返回长度为prefix_length的前缀
    return s[:prefix_length]


# 测试方法
if __name__ == "__main__":
    # 测试用例1
    s1 = "level"
    result1 = longest_prefix(s1)
    print(f"字符串: {s1}")
    print(f"最长快乐前缀: \"{result1}\"")
    print()

    # 测试用例2
    s2 = "ababab"
    result2 = longest_prefix(s2)
    print(f"字符串: {s2}")
    print(f"最长快乐前缀: \"{result2}\"")
    print()

    # 测试用例3
    s3 = "leetcodeleet"
    result3 = longest_prefix(s3)
    print(f"字符串: {s3}")
    print(f"最长快乐前缀: \"{result3}\"")
    print()

    # 测试用例4
    s4 = "a"
    result4 = longest_prefix(s4)
    print(f"字符串: {s4}")
    print(f"最长快乐前缀: \"{result4}\"")
    print()

    # 测试用例5
    s5 = "abcabcabc"
    result5 = longest_prefix(s5)
    print(f"字符串: {s5}")
    print(f"最长快乐前缀: \"{result5}\"")