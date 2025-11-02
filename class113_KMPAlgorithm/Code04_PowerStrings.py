"""
POJ 2406 Power Strings
题目链接: http://poj.org/problem?id=2406

题目描述:
给定一个字符串s，找到最大的n使得s = a^n（即字符串a重复n次等于s）。

算法思路:
使用KMP算法的next数组性质。对于一个字符串s，如果s的长度能被(长度-next[长度])整除，
且next[长度]不为0，则s是由其前(长度-next[长度])个字符重复组成的。
重复次数为长度/(长度-next[长度])。

时间复杂度: O(m)，其中m是字符串长度
空间复杂度: O(m)，用于存储next数组

示例:
输入:
abcd
aaaa
ababab

输出:
1
4
3
"""


def next_array(s):
    """
    构建next数组（部分匹配表）
    next[i]表示模式串中以i-1位置字符结尾的子串，其前缀和后缀匹配的最大长度

    算法思路：
    1. next[0] = -1，next[1] = 0（根据定义）
    2. 从i=2开始，使用双指针技术计算next[i]
    3. cn指针表示当前要和前一个字符比对的下标
    4. 如果s[i-1] == s[cn]，说明匹配成功，next[i] = ++cn
    5. 如果不匹配且cn > 0，cn回退到next[cn]
    6. 如果不匹配且cn == 0，next[i] = 0

    :param s: 字符串
    :return: next数组
    """
    n = len(s)
    
    if n == 1:
        return [-1]
    
    next_arr = [0] * (n + 1)
    next_arr[0] = -1
    next_arr[1] = 0
    
    # i表示当前要求next值的位置
    # cn表示当前要和前一个字符比对的下标
    i = 2
    cn = 0
    while i <= n:
        if s[i - 1] == s[cn]:
            cn += 1
            next_arr[i] = cn
            i += 1
        elif cn > 0:
            cn = next_arr[cn]
        else:
            next_arr[i] = 0
            i += 1
    
    return next_arr


def power(s):
    """
    计算字符串能由其子串重复的最大次数

    算法思路:
    1. 使用KMP算法构建next数组
    2. 根据next数组的性质判断字符串是否由子串重复组成
    3. 如果是，则计算重复次数

    :param s: 输入字符串
    :return: 字符串能由其子串重复的最大次数
    """
    if not s:
        return 0
    
    n = len(s)
    # 构建next数组
    next_arr = next_array(s)
    
    # 根据next数组判断是否由子串重复组成
    period = n - next_arr[n] if n < len(next_arr) else n
    
    # 如果n能被period整除，则说明字符串由长度为period的子串重复组成
    if period != 0 and n % period == 0:
        return n // period
    
    # 否则字符串不能由子串重复组成，返回1
    return 1


def main():
    """
    测试用例和使用示例
    """
    # 测试用例1: 无重复模式
    s1 = "abcd"
    result1 = power(s1)
    print("测试用例1:")
    print("字符串:", s1)
    print("重复次数:", result1)  # 期望输出: 1
    print()

    # 测试用例2: 全部相同字符
    s2 = "aaaa"
    result2 = power(s2)
    print("测试用例2:")
    print("字符串:", s2)
    print("重复次数:", result2)  # 期望输出: 4
    print()

    # 测试用例3: 重复模式
    s3 = "ababab"
    result3 = power(s3)
    print("测试用例3:")
    print("字符串:", s3)
    print("重复次数:", result3)  # 期望输出: 3
    print()

    # 测试用例4: 空字符串
    s4 = ""
    result4 = power(s4)
    print("测试用例4:")
    print("字符串:", s4)
    print("重复次数:", result4)  # 期望输出: 0
    print()

    # 测试用例5: 单字符
    s5 = "a"
    result5 = power(s5)
    print("测试用例5:")
    print("字符串:", s5)
    print("重复次数:", result5)  # 期望输出: 1


# 运行测试
if __name__ == "__main__":
    main()