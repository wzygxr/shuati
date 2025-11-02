"""
LeetCode 466. 统计重复个数

题目描述：
定义 str = [str, n] 表示重复字符串，由 n 个连续的字符串 str 组成。
例如 ["abc", 3] = "abcabcabc"。
如果我们可以从 s2 中删除某些字符使其变为 s1，则称字符串 s1 可以从字符串 s2 获得。
现在给你两个非空字符串 s1 和 s2（每个最多 100 个字符长）和两个整数 0 <= n1 <= 10^6 和 1 <= n2 <= 10^6。
现在考虑字符串 S1 和 S2，其中 S1=[s1,n1] 、S2=[s2,n2] 。
请你找出一个可以满足使 [S2, M] 从 S1 获得的最大整数 M 。

解题思路：
这是一道需要寻找循环节的字符串匹配问题。

核心思想：
1. 预处理：计算从s1的每个位置开始，匹配s2中每个字符需要的最小长度
2. 倍增优化：预处理从每个位置开始匹配一个s2需要的长度，然后使用倍增思想计算匹配多个s2
3. 循环节：寻找循环节，利用循环节快速计算结果

具体步骤：
1. 预处理 next 数组：next[i][j] 表示从 s1 的位置 i 开始，至少需要多少长度才能找到字符 'a'+j
2. 预处理 st 数组：st[i][p] 表示从 s1 的位置 i 开始，至少需要多少长度才能匹配 2^p 个 s2
3. 倍增计算：使用 st 数组快速计算能匹配多少个 s2
4. 结果计算：总匹配数除以 n2 得到最终结果

时间复杂度：O(len1 * len2 + log(n1 * len1))
空间复杂度：O(len1 * log(n1 * len1))

相关题目：
- LeetCode 686. 重复叠加字符串匹配
- LeetCode 28. 实现 strStr()
- LeetCode 139. 单词拆分
"""

def get_max_repetitions(s1: str, n1: int, s2: str, n2: int) -> int:
    """
    计算最大重复数M
    
    Args:
        s1: 字符串s1
        n1: s1重复次数
        s2: 字符串s2
        n2: s2重复次数
    
    Returns:
        最大整数M，使得[S2,M]可以从S1获得
    """
    len1, len2 = len(s1), len(s2)
    
    # next[i][j] : 从i位置出发，至少需要多少长度，能找到j字符
    next_arr = [[0] * 26 for _ in range(len1)]
    
    # 预处理next数组
    if not find(s1, len1, next_arr, s2):
        return 0
    
    # st[i][p] : 从i位置出发，至少需要多少长度，可以获得2^p个s2
    st = [[0] * 30 for _ in range(len1)]
    
    # 计算匹配一个s2需要的长度
    for i in range(len1):
        cur, length = i, 0
        for c in s2:
            length += next_arr[cur][ord(c) - ord('a')]
            cur = (cur + next_arr[cur][ord(c) - ord('a')]) % len1
        st[i][0] = length
    
    # 倍增预处理
    for p in range(1, 30):
        for i in range(len1):
            st[i][p] = st[i][p - 1] + st[(st[i][p - 1] + i) % len1][p - 1]
    
    ans = 0
    start = 0
    
    # 倍增计算能匹配多少个s2
    for p in range(29, -1, -1):
        if st[start % len1][p] + start <= len1 * n1:
            ans += 1 << p
            start += st[start % len1][p]
    
    return ans // n2


def find(s1: str, len1: int, next_arr: list, s2: str) -> bool:
    """
    预处理next数组
    
    Args:
        s1: 字符串s1
        len1: s1长度
        next_arr: next数组
        s2: 字符串s2
    
    Returns:
        s2中的字符是否都能在s1中找到
    """
    right = [-1] * 26
    
    # 从右到左扫描，记录每个字符最后出现的位置
    for i in range(len1 - 1, -1, -1):
        right[ord(s1[i]) - ord('a')] = i + len1
    
    # 计算next数组
    for i in range(len1 - 1, -1, -1):
        right[ord(s1[i]) - ord('a')] = i
        for j in range(26):
            if right[j] != -1:
                next_arr[i][j] = right[j] - i + 1
            else:
                next_arr[i][j] = -1
    
    # 检查s2中的每个字符是否都能在s1中找到
    for c in s2:
        if next_arr[0][ord(c) - ord('a')] == -1:
            return False
    
    return True


# 测试用例
if __name__ == "__main__":
    # 测试用例1
    s1 = "acb"
    n1 = 4
    s2 = "ab"
    n2 = 2
    print("测试用例1:")
    print(f"输入: s1 = \"{s1}\", n1 = {n1}, s2 = \"{s2}\", n2 = {n2}")
    print("输出:", get_max_repetitions(s1, n1, s2, n2))  # 期望输出: 2
    
    # 测试用例2
    s1_2 = "aaa"
    n1_2 = 3
    s2_2 = "aa"
    n2_2 = 1
    print("\n测试用例2:")
    print(f"输入: s1 = \"{s1_2}\", n1 = {n1_2}, s2 = \"{s2_2}\", n2 = {n2_2}")
    print("输出:", get_max_repetitions(s1_2, n1_2, s2_2, n2_2))  # 期望输出: 4
    
    # 测试用例3
    s1_3 = "bacaba"
    n1_3 = 3
    s2_3 = "abacab"
    n2_3 = 1
    print("\n测试用例3:")
    print(f"输入: s1 = \"{s1_3}\", n1 = {n1_3}, s2 = \"{s2_3}\", n2 = {n2_3}")
    print("输出:", get_max_repetitions(s1_3, n1_3, s2_3, n2_3))  # 期望输出: 2