def z_function(s):
    """
    Z函数计算
    Z函数z[i]表示字符串s从位置i开始与字符串s从位置0开始的最长公共前缀长度
    
    算法原理：
    1. 维护一个匹配区间[l, r]，表示当前已知的最右匹配区间
    2. 对于当前位置i，如果i <= r，可以利用已计算的信息优化
    3. 利用对称性，z[i]至少为min(r - i + 1, z[i - l])
    4. 在此基础之上继续向右扩展匹配
    
    时间复杂度：O(n)
    空间复杂度：O(n)
    
    Args:
        s: 输入字符串
    
    Returns:
        Z函数数组
    """
    n = len(s)
    z = [0] * n
    z[0] = n
    
    # l: 当前最右匹配区间的左边界
    # r: 当前最右匹配区间的右边界
    l = r = 0
    
    for i in range(1, n):
        # 利用已计算的信息优化
        # 如果i在当前匹配区间内
        if i <= r:
            z[i] = min(r - i + 1, z[i - l])
        
        # 继续向右扩展匹配
        while i + z[i] < n and s[z[i]] == s[i + z[i]]:
            z[i] += 1
        
        # 更新最右匹配区间
        if i + z[i] - 1 > r:
            l = i
            r = i + z[i] - 1
    
    return z


def extended_kmp(a, b):
    """
    扩展KMP计算
    计算字符串a的每个后缀与字符串b的最长公共前缀长度
    
    时间复杂度：O(n + m)，其中n是a的长度，m是b的长度
    空间复杂度：O(n + m)
    
    Args:
        a: 主字符串
        b: 模式字符串
    
    Returns:
        E数组，其中e[i]表示a[i:]与b的最长公共前缀长度
    """
    n, m = len(a), len(b)
    
    # 先计算b的Z函数
    z = z_function(b)
    
    # 计算扩展KMP
    e = [0] * n
    l = r = 0
    
    for i in range(n):
        # 利用已计算的信息优化
        if i <= r:
            e[i] = min(r - i + 1, z[i - l])
        
        # 继续向右扩展匹配
        while i + e[i] < n and e[i] < m and a[i + e[i]] == b[e[i]]:
            e[i] += 1
        
        # 更新最右匹配区间
        if i + e[i] - 1 > r:
            l = i
            r = i + e[i] - 1
    
    return e


def xor_sum(arr):
    """计算数组的权值: xor(i * (arr[i] + 1))"""
    result = 0
    for i in range(len(arr)):
        result ^= (i + 1) * (arr[i] + 1)
    return result


def sum_scores(s):
    """
    LeetCode 2223. 构造字符串的总得分和
    你需要从空字符串开始构造一个长度为n的字符串s，构造过程为每次给当前字符串前面添加一个字符。
    构造过程中得到的所有字符串编号为1到n，其中长度为i的字符串编号为si。
    si的得分为si和sn的最长公共前缀的长度（注意s == sn）。
    请你返回每一个si的得分之和。
    
    时间复杂度: O(n)
    空间复杂度: O(n)
    """
    n = len(s)
    
    # 计算Z函数
    z = z_function(s)
    
    # 计算得分总和
    # 每个si的得分就是z[i]
    return sum(z)


def minimum_time_to_initial_state(word, k):
    """
    LeetCode 3031. 将单词恢复初始状态所需的最短时间 II
    给你一个下标从0开始的字符串word和一个整数k。
    每一秒执行以下操作：
    1. 移除word的前k个字符
    2. 在word的末尾添加k个任意字符
    返回将word恢复到初始状态所需的最短时间（该时间必须大于零）。
    
    时间复杂度: O(n)
    空间复杂度: O(n)
    """
    n = len(word)
    
    # 计算Z函数
    z = z_function(word)
    
    # 查找满足条件的最小时间
    for i in range(k, n, k):
        # 如果从位置i开始的后缀与原字符串的最长公共前缀长度等于后缀长度
        # 说明在第(i//k)步后可以恢复原字符串
        if z[i] >= n - i:
            return i // k
    
    # 最坏情况需要完全替换
    return (n + k - 1) // k


def extended_kmp_template(a, b):
    """
    洛谷 P5410 【模板】扩展 KMP（Z 函数）
    题目描述：给定两个字符串 a,b，求：
    1. b 与 b 每一个后缀串的最长公共前缀长度（即 b 的 Z 函数）
    2. a 与 b 每一个后缀串的最长公共前缀长度（即扩展 KMP）
    
    时间复杂度: O(n + m)
    空间复杂度: O(n + m)
    """
    n, m = len(a), len(b)
    
    # 计算b的Z函数
    z = z_function(b)
    
    # 计算a与b的扩展KMP
    e = extended_kmp(a, b)
    
    # 计算异或和
    z_xor = xor_sum(z)
    e_xor = xor_sum(e)
    
    return z_xor, e_xor


# 测试洛谷P5410 扩展KMP模板题
if __name__ == "__main__":
    a = input().strip()
    b = input().strip()
    
    # 计算b的Z函数
    z = z_function(b)
    # 计算a与b的扩展KMP
    e = extended_kmp(a, b)
    
    print(xor_sum(z))
    print(xor_sum(e))