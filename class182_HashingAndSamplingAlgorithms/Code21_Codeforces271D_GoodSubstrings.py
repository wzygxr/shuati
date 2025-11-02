"""
Codeforces 271D - Good Substrings
题目链接：https://codeforces.com/contest/271/problem/D

题目描述：
给定一个字符串s，由小写英文字母组成。有些英文字母是好的，其余的是坏的。
字符串s[l...r]是好的，当且仅当其中最多有k个坏字母。
任务是找出字符串s中不同好子串的数量（内容不同的子串视为不同）。

算法核心思想：
1. 滑动窗口枚举：从每个起始位置开始，向右扩展并统计坏字母数量
2. 哈希去重：使用多项式滚动哈希和HashSet高效存储不同子串
3. 早期剪枝：当坏字母数量超过k时立即停止扩展
4. 预计算优化：预先计算哈希值和幂次数组，支持O(1)时间子串哈希值查询

时间复杂度：O(n²)
空间复杂度：O(n²)
"""

MAXN = 1501
base = 499
bad = [False] * 26
pow_arr = [0] * MAXN
hash_arr = [0] * MAXN


def get_hash(l, r):
    """计算子串哈希值"""
    ans = hash_arr[r]
    if l > 0:
        ans -= hash_arr[l - 1] * pow_arr[r - l + 1]
    return ans


def count_good_substrings(s, good_chars, k):
    """
    计算不同好子串的数量
    
    Args:
        s: 输入字符串
        good_chars: 好字母标记字符串
        k: 允许的最大坏字母数量
    
    Returns:
        不同好子串的数量
    """
    n = len(s)
    
    # 构建坏字母标记数组
    for i in range(26):
        bad[i] = (good_chars[i] == '0')
    
    # 预计算幂次数组
    pow_arr[0] = 1
    for i in range(1, n):
        pow_arr[i] = pow_arr[i - 1] * base
    
    # 构建前缀哈希数组
    hash_arr[0] = ord(s[0]) - ord('a') + 1
    for i in range(1, n):
        hash_arr[i] = hash_arr[i - 1] * base + (ord(s[i]) - ord('a') + 1)
    
    # 使用set存储不同好子串的哈希值
    unique_hashes = set()
    
    # 枚举所有可能的子串起始位置
    for i in range(n):
        # 从位置i开始，向右扩展子串，同时统计坏字母数量
        cnt = 0
        for j in range(i, n):
            # 检查当前字符是否是坏字母
            if bad[ord(s[j]) - ord('a')]:
                cnt += 1
            
            # 剪枝优化：如果坏字母数量超过k，停止向右扩展
            if cnt > k:
                break
            
            # 计算子串哈希值并加入set
            unique_hashes.add(get_hash(i, j))
    
    return len(unique_hashes)


def count_good_substrings_optimized(s, good_chars, k):
    """
    优化版本：使用双哈希减少冲突
    
    Args:
        s: 输入字符串
        good_chars: 好字母标记字符串
        k: 允许的最大坏字母数量
    
    Returns:
        不同好子串的数量
    """
    n = len(s)
    
    # 构建坏字母标记数组
    for i in range(26):
        bad[i] = (good_chars[i] == '0')
    
    # 双哈希参数
    base1, mod1 = 499, 1000000007
    base2, mod2 = 503, 1000000009
    
    # 预计算两组幂次数组
    pow1 = [0] * MAXN
    pow2 = [0] * MAXN
    
    # 预计算两组前缀哈希数组
    hash1 = [0] * MAXN
    hash2 = [0] * MAXN
    
    # 预处理第一组幂次数组
    pow1[0] = 1
    for i in range(1, n):
        pow1[i] = (pow1[i - 1] * base1) % mod1
    
    # 预处理第二组幂次数组
    pow2[0] = 1
    for i in range(1, n):
        pow2[i] = (pow2[i - 1] * base2) % mod2
    
    # 计算第一组前缀哈希
    hash1[0] = (ord(s[0]) - ord('a') + 1) % mod1
    for i in range(1, n):
        hash1[i] = (hash1[i - 1] * base1 + (ord(s[i]) - ord('a') + 1)) % mod1
    
    # 计算第二组前缀哈希
    hash2[0] = (ord(s[0]) - ord('a') + 1) % mod2
    for i in range(1, n):
        hash2[i] = (hash2[i - 1] * base2 + (ord(s[i]) - ord('a') + 1)) % mod2
    
    # 计算第一组哈希值的辅助函数
    def get_hash1(l, r):
        ans = hash1[r]
        if l > 0:
            ans = (ans - (hash1[l - 1] * pow1[r - l + 1]) % mod1 + mod1) % mod1
        return ans
    
    # 计算第二组哈希值的辅助函数
    def get_hash2(l, r):
        ans = hash2[r]
        if l > 0:
            ans = (ans - (hash2[l - 1] * pow2[r - l + 1]) % mod2 + mod2) % mod2
        return ans
    
    # 使用set存储双哈希值
    unique_hashes = set()
    
    # 枚举所有可能的子串起始位置
    for i in range(n):
        # 从位置i开始，向右扩展子串，同时统计坏字母数量
        cnt = 0
        for j in range(i, n):
            # 检查当前字符是否是坏字母
            if bad[ord(s[j]) - ord('a')]:
                cnt += 1
            
            # 剪枝优化：如果坏字母数量超过k，停止向右扩展
            if cnt > k:
                break
            
            # 计算双哈希值并加入set
            h1 = get_hash1(i, j)
            h2 = get_hash2(i, j)
            unique_hashes.add(f"{h1},{h2}")
    
    return len(unique_hashes)


def test_cases():
    """测试函数"""
    print("=== 测试 Codeforces 271D - Good Substrings ===")
    
    # 测试用例1
    s1 = "abcabc"
    mark1 = "101010101010101010101010101"
    k1 = 1
    result1 = count_good_substrings(s1, mark1, k1)
    print(f"输入: s=\"{s1}\", mark=\"{mark1}\", k={k1}")
    print(f"输出: {result1}")  # 期望: 9
    print()
    
    # 测试用例2
    s2 = "aba"
    mark2 = "111111111111111111111111111"
    k2 = 1
    result2 = count_good_substrings(s2, mark2, k2)
    print(f"输入: s=\"{s2}\", mark=\"{mark2}\", k={k2}")
    print(f"输出: {result2}")  # 期望: 5
    print()
    
    # 测试用例3
    s3 = "aaaaa"
    mark3 = "111111111111111111111111111"
    k3 = 2
    result3 = count_good_substrings(s3, mark3, k3)
    print(f"输入: s=\"{s3}\", mark=\"{mark3}\", k={k3}")
    print(f"输出: {result3}")  # 期望: 5
    print()
    
    # 优化版本测试
    print("--- 优化版本测试 ---")
    result1_opt = count_good_substrings_optimized(s1, mark1, k1)
    result2_opt = count_good_substrings_optimized(s2, mark2, k2)
    result3_opt = count_good_substrings_optimized(s3, mark3, k3)
    print(f"优化版本结果1: {result1_opt}")
    print(f"优化版本结果2: {result2_opt}")
    print(f"优化版本结果3: {result3_opt}")
    print()


if __name__ == "__main__":
    test_cases()