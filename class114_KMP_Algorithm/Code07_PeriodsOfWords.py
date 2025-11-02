"""
POI 2006 - Periods of Words

题目描述：
对于给定的字符串，计算所有周期的总和。
字符串的周期定义为：对于长度为n的字符串s，如果存在一个k (1 <= k < n)，
使得对于所有i (0 <= i < n-k)，都有s[i] = s[i+k]，则k是s的一个周期。

任务是计算所有周期的和。

算法思路：
使用KMP算法的next数组来解决这个问题。
对于长度为n的字符串，其所有周期可以通过next数组计算得出。
如果next[n-1] > 0，则n - next[n-1]是一个周期。
然后我们可以继续应用next函数来找到所有周期。

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


def calculate_periods_sum(s):
    """
    计算字符串所有周期的总和

    :param s: 输入字符串
    :return: 所有周期的总和
    """
    n = len(s)

    # 边界条件处理
    if n <= 1:
        return 0

    # 构建next数组
    next_array = build_next_array(s)

    total_sum = 0

    # 从最后一个位置开始，通过next数组找到所有周期
    pos = n - 1
    while pos > 0:
        # 如果当前位置有匹配的前后缀
        if next_array[pos] > 0:
            # 周期长度为 pos + 1 - next[pos]
            period = (pos + 1) - next_array[pos]
            # 如果周期长度小于当前位置+1，则是一个有效周期
            if period < pos + 1:
                total_sum += period
            # 移动到next[pos]-1位置继续查找
            pos = next_array[pos] - 1
        else:
            # 没有匹配的前后缀，向前移动
            pos -= 1

    return total_sum


# 测试方法
if __name__ == "__main__":
    # 测试用例1
    s1 = "aaaa"
    result1 = calculate_periods_sum(s1)
    print(f"字符串: {s1}")
    print(f"所有周期的总和: {result1}")
    print()

    # 测试用例2
    s2 = "ababab"
    result2 = calculate_periods_sum(s2)
    print(f"字符串: {s2}")
    print(f"所有周期的总和: {result2}")
    print()

    # 测试用例3
    s3 = "abcabcabc"
    result3 = calculate_periods_sum(s3)
    print(f"字符串: {s3}")
    print(f"所有周期的总和: {result3}")
    print()

    # 测试用例4
    s4 = "aabaaab"
    result4 = calculate_periods_sum(s4)
    print(f"字符串: {s4}")
    print(f"所有周期的总和: {result4}")