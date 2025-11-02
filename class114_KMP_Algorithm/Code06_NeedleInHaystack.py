"""
SPOJ NHAY - A Needle in the Haystack

题目描述：
编写一个程序，在给定的输入字符串中找到所有给定模式的出现位置。
这通常被称为在干草堆中找针。

输入格式：
输入包含多个测试用例。
每个测试用例由两行组成：
第一行包含模式的长度m (1 <= m <= 10000)
第二行包含模式本身
第三行包含文本的长度n (1 <= n <= 1000000)
第四行包含文本本身

输出格式：
对于每个测试用例，输出所有匹配位置的索引（从0开始）。
如果没有匹配，则不输出任何内容。

算法思路：
使用KMP算法进行字符串匹配，找到所有匹配位置。

时间复杂度：O(n + m)，其中n是文本串长度，m是模式串长度
空间复杂度：O(m)，用于存储next数组
"""


def build_next_array(pattern):
    """
    构建KMP算法的next数组（部分匹配表）

    next[i]表示pattern[0...i]子串的最长相等前后缀的长度

    算法思路：
    1. 初始化next[0] = 0
    2. 使用双指针i和j，i指向当前处理的位置，j指向前缀的末尾
    3. 如果pattern[i] == pattern[j]，说明前缀和后缀可以延长，next[i] = j + 1
    4. 如果pattern[i] != pattern[j]，需要回退j指针到next[j-1]，直到匹配或j=0

    :param pattern: 模式串
    :return: next数组
    """
    length = len(pattern)
    next_array = [0] * length

    # 初始化
    next_array[0] = 0
    prefix_len = 0  # 当前最长相等前后缀的长度
    i = 1  # 当前处理的位置

    # 从位置1开始处理
    while i < length:
        # 如果当前字符匹配，可以延长相等前后缀
        if pattern[i] == pattern[prefix_len]:
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


def find_all_occurrences(text, pattern):
    """
    在文本串中查找模式串的所有出现位置

    :param text: 文本串
    :param pattern: 模式串
    :return: 所有匹配位置的列表（从0开始计数）
    """
    positions = []

    # 边界条件处理
    if not pattern or len(text) < len(pattern):
        return positions

    # 构建next数组
    next_array = build_next_array(pattern)

    text_index = 0  # 文本串指针
    pattern_index = 0  # 模式串指针

    # 匹配过程
    while text_index < len(text):
        # 字符匹配，两个指针都向前移动
        if text[text_index] == pattern[pattern_index]:
            text_index += 1
            pattern_index += 1
        # 字符不匹配且模式串指针不为0，根据next数组调整模式串指针
        elif pattern_index > 0:
            pattern_index = next_array[pattern_index - 1]
        # 字符不匹配且模式串指针为0，文本串指针向前移动
        else:
            text_index += 1

        # 如果模式串指针等于模式串长度，说明匹配成功
        if pattern_index == len(pattern):
            # 记录匹配位置（从0开始计数）
            positions.append(text_index - pattern_index)
            # 根据next数组调整模式串指针，继续查找下一个匹配
            pattern_index = next_array[pattern_index - 1]

    return positions


# 测试方法
if __name__ == "__main__":
    # 测试用例1
    text1 = "abcabcabcabc"
    pattern1 = "abc"
    result1 = find_all_occurrences(text1, pattern1)
    print(f"文本串: {text1}")
    print(f"模式串: {pattern1}")
    print(f"匹配位置: {' '.join(map(str, result1))}")
    print()

    # 测试用例2
    text2 = "abababab"
    pattern2 = "aba"
    result2 = find_all_occurrences(text2, pattern2)
    print(f"文本串: {text2}")
    print(f"模式串: {pattern2}")
    print(f"匹配位置: {' '.join(map(str, result2))}")
    print()

    # 测试用例3
    text3 = "aaaaa"
    pattern3 = "aa"
    result3 = find_all_occurrences(text3, pattern3)
    print(f"文本串: {text3}")
    print(f"模式串: {pattern3}")
    print(f"匹配位置: {' '.join(map(str, result3))}")
    print()

    # 测试用例4 - 无匹配
    text4 = "abcdef"
    pattern4 = "xyz"
    result4 = find_all_occurrences(text4, pattern4)
    print(f"文本串: {text4}")
    print(f"模式串: {pattern4}")
    print(f"匹配位置: {' '.join(map(str, result4))}")