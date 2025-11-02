def manacher(s):
    """
    Manacher算法主函数，用于计算字符串中最长回文子串的长度
    
    算法原理：
    1. 预处理：在原字符串的每个字符之间插入特殊字符'#'，并在首尾也添加'#'
       这样可以将奇数长度和偶数长度的回文串统一处理为奇数长度的回文串
    2. 利用回文串的对称性，避免重复计算
    3. 维护当前最右回文边界r和对应的中心c，通过已计算的信息加速新位置的计算
    
    时间复杂度：O(n)，其中n为字符串长度
    空间复杂度：O(n)
    
    Args:
        s: 输入字符串
    
    Returns:
        最长回文子串的长度
        
    应用场景：
    1. LeetCode 5. 最长回文子串 - https://leetcode.com/problems/longest-palindromic-substring/
    2. LeetCode 647. 回文子串 - https://leetcode.com/problems/palindromic-substrings/
    3. LeetCode 214. 最短回文串 - https://leetcode.com/problems/shortest-palindrome/
    4. 洛谷 P3805 【模板】manacher - https://www.luogu.com.cn/problem/P3805
    5. UVa 11475 - Extend to Palindrome - https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=2470
    6. Codeforces 1326D2 - Prefix-Suffix Palindrome - https://codeforces.com/problemset/problem/1326/D2
    7. HackerRank - Palindromic Substrings
    8. AcWing 141. 周期 - https://www.acwing.com/problem/content/143/
    9. POJ 3240 - 回文串
    10. LeetCode 336. 回文对 - https://leetcode.com/problems/palindrome-pairs/
    11. LeetCode 131. 分割回文串 - https://leetcode.com/problems/palindrome-partitioning/
    12. LeetCode 132. 分割回文串 II - https://leetcode.com/problems/palindrome-partitioning-ii/
    """
    # 预处理字符串
    processed = '#'.join('^{}$'.format(s))
    n = len(processed)
    p = [0] * n
    max_len = 0
    
    # c: 当前最右回文子串的中心
    # r: 当前最右回文子串的右边界
    c = r = 0
    
    for i in range(1, n - 1):
        # 利用回文对称性优化
        # 如果i在当前右边界内，则可以利用对称点2*c-i的信息
        if i < r:
            p[i] = min(r - i, p[2 * c - i])
        
        # 尝试扩展回文串
        # 从当前半径开始，尝试向两边扩展
        try:
            while processed[i + p[i] + 1] == processed[i - p[i] - 1]:
                p[i] += 1
        except IndexError:
            # 边界情况处理
            pass
        
        # 更新最右回文边界和中心
        if i + p[i] > r:
            c, r = i, i + p[i]
        
        # 更新最大回文半径
        max_len = max(max_len, p[i])
    
    # 由于我们插入了'#'字符，实际回文长度就是半径
    return max_len


def longest_palindrome(s):
    """
    LeetCode 5. 最长回文子串
    给你一个字符串 s，找到 s 中最长的回文子串
    
    时间复杂度: O(n)
    空间复杂度: O(n)
    
    Args:
        s: 输入字符串
        
    Returns:
        最长回文子串
    """
    if not s:
        return ""
    
    # 预处理字符串
    processed = '#'.join('^{}$'.format(s))
    n = len(processed)
    p = [0] * n
    
    c = r = 0
    max_len = 0
    center_index = 0
    
    for i in range(1, n - 1):
        # 利用回文对称性
        if i < r:
            p[i] = min(r - i, p[2 * c - i])
        
        # 尝试扩展回文
        try:
            while processed[i + p[i] + 1] == processed[i - p[i] - 1]:
                p[i] += 1
        except IndexError:
            pass
        
        # 更新最右边界
        if i + p[i] > r:
            c, r = i, i + p[i]
        
        # 更新最长回文
        if p[i] > max_len:
            max_len = p[i]
            center_index = i
    
    # 从处理后的字符串中提取原始回文子串
    start = (center_index - max_len) // 2
    return s[start:start + max_len]


def count_substrings(s):
    """
    LeetCode 647. 回文子串
    给定一个字符串，计算其中回文子串的数目
    
    时间复杂度: O(n)
    空间复杂度: O(n)
    
    Args:
        s: 输入字符串
        
    Returns:
        回文子串的数目
    """
    if not s:
        return 0
    
    # 预处理字符串
    processed = '#'.join('^{}$'.format(s))
    n = len(processed)
    p = [0] * n
    
    c = r = 0
    count = 0
    
    for i in range(1, n - 1):
        # 利用回文对称性
        if i < r:
            p[i] = min(r - i, p[2 * c - i])
        
        # 尝试扩展回文
        try:
            while processed[i + p[i] + 1] == processed[i - p[i] - 1]:
                p[i] += 1
        except IndexError:
            pass
        
        # 更新最右边界
        if i + p[i] > r:
            c, r = i, i + p[i]
        
        # 每个回文半径可以贡献 (p[i]+1)//2 个回文子串
        count += (p[i] + 1) // 2
    
    return count


def shortest_palindrome(s):
    """
    LeetCode 214. 最短回文串
    给定一个字符串 s，可以通过在字符串前面添加字符将其转换为回文串。
    找到并返回可以用这种方式转换的最短回文串。
    
    时间复杂度: O(n)
    空间复杂度: O(n)
    
    Args:
        s: 输入字符串
        
    Returns:
        最短回文串
    """
    if len(s) <= 1:
        return s
    
    # 将字符串与其反转拼接，中间用特殊字符分隔
    combined = s + "#" + s[::-1]
    
    # 计算KMP的LPS数组
    lps = [0] * len(combined)
    for i in range(1, len(combined)):
        j = lps[i - 1]
        while j > 0 and combined[i] != combined[j]:
            j = lps[j - 1]
        if combined[i] == combined[j]:
            j += 1
        lps[i] = j
    
    # 找到原字符串的最长回文前缀
    overlap = lps[-1]
    
    # 在原字符串前添加反转的部分
    return s[overlap:][::-1] + s


def longest_palindrome_length(s):
    """
    洛谷 P3805 【模板】manacher
    题目描述：给出一个只由小写英文字符 a,b,c...y,z 组成的字符串 S ,
    求 S 中最长回文串的长度 。
    
    时间复杂度: O(n)
    空间复杂度: O(n)
    
    Args:
        s: 输入字符串
        
    Returns:
        最长回文子串的长度
    """
    return manacher(s)


# 测试洛谷P3805 Manacher模板题
if __name__ == "__main__":
    input_str = input().strip()
    print(manacher(input_str))