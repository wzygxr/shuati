# CodeForces 835D Palindromic characteristics
# 题目链接: https://codeforces.com/problemset/problem/835/D
#
# 题目大意: 
# 定义k回文串(k>1): 字符串本身是回文串，且左右两半相等，左右两半都是(k-1)回文串
# 定义1回文串: 字符串本身是回文串
# 求字符串中各个级别回文子串的数量
#
# 算法核心思想：
# 结合字符串哈希技术和动态规划，高效判断子串的回文级别
#
# 算法详细步骤：
# 1. 预处理阶段：计算前缀哈希、后缀哈希和幂次数组
# 2. 动态规划阶段：按长度从小到大计算每个子串的回文级别
# 3. 统计阶段：统计各级回文子串的数量
#
# 字符串哈希原理：
# - 前缀哈希：prefix_hash[i] = hash(s[0..i-1])
# - 后缀哈希：suffix_hash[i] = hash(s[i-1..n-1])
# - 子串哈希：hash(s[l..r]) = (prefix_hash[r+1] - prefix_hash[l] * base^(r-l+1)) % mod
# - 反向子串哈希：reverse_hash(s[l..r]) = (suffix_hash[l+1] - suffix_hash[r+2] * base^(r-l+1)) % mod
# - 回文判断：当子串哈希等于其反向哈希时，该子串为回文串
#
# 动态规划状态转移：
# - dp[i][j] 表示子串s[i..j]的回文级别
# - 对于长度为1的子串：dp[i][i] = 1
# - 对于长度>1的回文子串：
#   * 如果长度为偶数：检查左右两半是否相等且都是(k-1)级回文
#   * 如果长度为奇数：检查左右两半是否相等且都是(k-1)级回文
#
# 时间复杂度分析：
# - 预处理阶段：O(n)
# - 动态规划阶段：O(n^2)
# - 统计阶段：O(n^2)
# - 总体时间复杂度：O(n^2)
#
# 空间复杂度分析：
# - 哈希数组和幂次数组：O(n)
# - DP数组：O(n^2)
# - 总体空间复杂度：O(n^2)
#
# 相似题目：
# 1. LeetCode 647. 回文子串
# 2. LeetCode 5. 最长回文子串
# 3. Codeforces 137D Palindromes
# 4. SPOJ NUMOFPAL - Number of Palindromes
#
# 三种语言实现参考：
# - Java实现：Code11_PalindromicCharacteristics.java
# - Python实现：当前文件
# - C++实现：Code11_PalindromicCharacteristics.cpp

def preprocess(s):
    """
    预处理函数，计算前缀哈希、后缀哈希和幂次数组
    
    通过预计算这些数组，可以在O(1)时间内判断任意子串是否为回文串
    
    数学原理：
    1. 前缀哈希：prefix_hash[i] = hash(s[0..i-1])
       递推公式：prefix_hash[i] = (prefix_hash[i-1] * base + char_val) % mod
    2. 后缀哈希：suffix_hash[i] = hash(s[i-1..n-1])
       递推公式：suffix_hash[i] = (suffix_hash[i+1] * base + char_val) % mod
    3. 幂次数组：pow_arr[i] = base^i % mod
       递推公式：pow_arr[i] = (pow_arr[i-1] * base) % mod
    
    参数选择：
    - base = 131：常用哈希基数，质数
    - mod = 1000000007：大质数，防止溢出
    
    :param s: 输入字符串
    :return: 前缀哈希数组、后缀哈希数组、幂次数组
    :time complexity: O(n)
    :space complexity: O(n)
    """
    n = len(s)
    base = 131
    mod = 1000000007
    
    # 计算幂次数组，pow_arr[i] = base^i % mod
    # 用于快速计算子串哈希值
    pow_arr = [1] * (n + 1)
    for i in range(1, n + 1):
        pow_arr[i] = (pow_arr[i - 1] * base) % mod
    
    # 计算前缀哈希数组，prefix_hash[i] = hash(s[0..i-1])
    # 递推公式：prefix_hash[i] = (prefix_hash[i-1] * base + char_val) % mod
    prefix_hash = [0] * (n + 1)
    for i in range(1, n + 1):
        # 字符映射：'a'->1, 'b'->2, ..., 'z'->26
        # 避免0值映射导致的哈希冲突
        prefix_hash[i] = (prefix_hash[i - 1] * base + ord(s[i - 1]) - ord('a') + 1) % mod
    
    # 计算后缀哈希数组，suffix_hash[i] = hash(s[i-1..n-1])
    # 递推公式：suffix_hash[i] = (suffix_hash[i+1] * base + char_val) % mod
    suffix_hash = [0] * (n + 2)  # 多分配一个位置避免边界检查
    for i in range(n, 0, -1):  # 从后往前计算
        # 字符映射：'a'->1, 'b'->2, ..., 'z'->26
        suffix_hash[i] = (suffix_hash[i + 1] * base + ord(s[i - 1]) - ord('a') + 1) % mod
    
    return prefix_hash, suffix_hash, pow_arr

def get_hash(prefix_hash, pow_arr, l, r):
    """
    获取子串s[l..r]的哈希值
    
    利用预处理好的前缀哈希数组和幂次数组，在O(1)时间内计算任意子串的哈希值
    
    数学原理：
    假设我们要计算子串s[l..r]的哈希值：
    1. prefix_hash[r+1] = hash(s[0..r])
    2. prefix_hash[l] = hash(s[0..l-1])
    3. 要得到hash(s[l..r])，需要从prefix_hash[r+1]中减去prefix_hash[l]的影响
    4. prefix_hash[l] * pow_arr[r-l+1] = hash(s[0..l-1]) * base^(r-l+1)
    5. hash(s[l..r]) = (prefix_hash[r+1] - prefix_hash[l] * pow_arr[r-l+1]) % mod
    
    :param prefix_hash: 前缀哈希数组
    :param pow_arr: 幂次数组
    :param l: 左边界(0-based)
    :param r: 右边界(0-based)
    :return: 哈希值
    :time complexity: O(1)
    :space complexity: O(1)
    """
    if l > r:
        return 0
    mod = 1000000007
    # 计算子串哈希值
    # 加上mod再取模是为了确保结果为非负数
    res = (prefix_hash[r + 1] - (prefix_hash[l] * pow_arr[r - l + 1]) % mod + mod) % mod
    return res

def get_reverse_hash(suffix_hash, pow_arr, l, r):
    """
    获取子串s[l..r]的反向哈希值
    
    利用预处理好的后缀哈希数组和幂次数组，在O(1)时间内计算任意子串的反向哈希值
    
    数学原理：
    假设我们要计算子串s[l..r]的反向哈希值（即s[r..l]的哈希值）：
    1. suffix_hash[l+1] = hash(s[l..n-1])
    2. suffix_hash[r+2] = hash(s[r+1..n-1])
    3. 要得到hash(s[l..r])的反向哈希值，需要从suffix_hash[l+1]中减去suffix_hash[r+2]的影响
    4. suffix_hash[r+2] * pow_arr[r-l+1] = hash(s[r+1..n-1]) * base^(r-l+1)
    5. reverse_hash(s[l..r]) = (suffix_hash[l+1] - suffix_hash[r+2] * pow_arr[r-l+1]) % mod
    
    :param suffix_hash: 后缀哈希数组
    :param pow_arr: 幂次数组
    :param l: 左边界(0-based)
    :param r: 右边界(0-based)
    :return: 反向哈希值
    :time complexity: O(1)
    :space complexity: O(1)
    """
    if l > r:
        return 0
    mod = 1000000007
    # 计算反向子串哈希值
    # 加上mod再取模是为了确保结果为非负数
    res = (suffix_hash[l + 1] - (suffix_hash[r + 2] * pow_arr[r - l + 1]) % mod + mod) % mod
    return res

def is_palindrome(prefix_hash, suffix_hash, pow_arr, l, r):
    """
    判断子串s[l..r]是否为回文串
    
    利用字符串哈希技术，在O(1)时间内判断子串是否为回文串
    
    判断原理：
    字符串s是回文串当且仅当s与其反转字符串相等
    因此，我们可以通过比较子串的哈希值与其反向哈希值来判断是否为回文串
    
    :param prefix_hash: 前缀哈希数组
    :param suffix_hash: 后缀哈希数组
    :param pow_arr: 幂次数组
    :param l: 左边界(0-based)
    :param r: 右边界(0-based)
    :return: 是否为回文串
    :time complexity: O(1)
    :space complexity: O(1)
    """
    # 当子串的哈希值等于其反向哈希值时，该子串为回文串
    return get_hash(prefix_hash, pow_arr, l, r) == get_reverse_hash(suffix_hash, pow_arr, l, r)

def compute_palindromic_levels(s):
    """
    计算每个子串的回文级别
    
    使用动态规划方法，按长度从小到大计算每个子串的回文级别
    
    动态规划状态定义：
    dp[i][j] 表示子串s[i..j]的回文级别
    
    状态转移方程：
    1. 对于长度为1的子串：dp[i][i] = 1（所有单字符都是1级回文）
    2. 对于长度>1的子串s[i..j]：
       a. 首先判断是否为回文串
       b. 如果不是回文串：dp[i][j] = 0
       c. 如果是回文串：
          i. 长度为偶数：检查左右两半是否相等且都是(k-1)级回文
          ii. 长度为奇数：检查左右两半是否相等且都是(k-1)级回文
    
    :param s: 输入字符串
    :return: dp数组，dp[i][j]表示子串s[i..j]的回文级别
    :time complexity: O(n^2)
    :space complexity: O(n^2)
    """
    n = len(s)
    # 预处理哈希数组
    prefix_hash, suffix_hash, pow_arr = preprocess(s)
    
    # dp[i][j] 表示子串s[i..j]的回文级别
    # 初始化为0，表示不是回文串
    dp = [[0] * n for _ in range(n)]
    
    # 长度为1的子串都是1级回文
    for i in range(n):
        dp[i][i] = 1
    
    # 按长度从小到大计算
    # 从长度为2开始，因为长度为1的情况已经处理过了
    for length in range(2, n + 1):
        # 遍历所有长度为length的子串
        for i in range(n - length + 1):
            j = i + length - 1  # 计算右边界
            
            # 首先判断是否为回文串
            if is_palindrome(prefix_hash, suffix_hash, pow_arr, i, j):
                # 如果是回文串，判断是否为k级回文(k>1)
                if length % 2 == 0:
                    # 长度为偶数的情况
                    mid = (i + j) // 2
                    # 检查左右两半是否相等且都是(k-1)级回文
                    # 1. 左半部分：s[i..mid]
                    # 2. 右半部分：s[mid+1..j]
                    if (get_hash(prefix_hash, pow_arr, i, mid) == 
                        get_hash(prefix_hash, pow_arr, mid + 1, j) and 
                        dp[i][mid] > 0):  # 确保左半部分是回文串
                        # 如果左右两半相等且左半部分是k-1级回文，则当前子串是k级回文
                        dp[i][j] = dp[i][mid] + 1
                    else:
                        # 至少是1级回文
                        dp[i][j] = 1
                else:
                    # 长度为奇数的情况
                    mid = (i + j) // 2
                    # 检查左右两半是否相等且都是(k-1)级回文
                    # 1. 左半部分：s[i..mid-1]
                    # 2. 右半部分：s[mid+1..j]
                    # 3. 中间字符：s[mid]（不需要特别处理，因为它在反转后仍在中间）
                    if (get_hash(prefix_hash, pow_arr, i, mid - 1) == 
                        get_hash(prefix_hash, pow_arr, mid + 1, j) and 
                        dp[i][mid - 1] > 0):  # 确保左半部分是回文串
                        # 如果左右两半相等且左半部分是k-1级回文，则当前子串是k级回文
                        dp[i][j] = dp[i][mid - 1] + 1
                    else:
                        # 至少是1级回文
                        dp[i][j] = 1
    
    return dp

def count_palindromes(dp):
    """
    统计各级回文子串数量
    
    根据题目要求，如果一个子串是k级回文，那么它也是(k-1)级回文，直至1级回文
    因此，我们需要累加所有级别的回文子串数量
    
    :param dp: dp数组
    :return: 各级回文子串数量
    :time complexity: O(n^2)
    :space complexity: O(n)
    """
    n = len(dp)
    # 结果数组，count[k]表示k级回文子串的数量
    # 最高级别不会超过n，所以分配n+1个位置
    count = [0] * (n + 1)
    
    # 根据观察，如果一个子串是k级回文，那么它也是(k-1)级回文，直至1级回文
    # 因此，对于每个子串，我们需要将其贡献加到所有级别1到dp[i][j]上
    for i in range(n):
        for j in range(i, n):
            # 对于子串s[i..j]，如果它是k级回文(k=dp[i][j])
            # 那么它对级别1,2,...,k都有贡献
            for k in range(1, dp[i][j] + 1):
                count[k] += 1
    
    return count

# 主函数
if __name__ == "__main__":
    # 读取输入
    # s = input().strip()
    
    # 由于是示例，我们使用硬编码的测试数据
    # 示例输入: "abacaba"
    # 预期输出: 各级别回文子串的数量
    s = "abacaba"
    
    # 计算每个子串的回文级别
    dp = compute_palindromic_levels(s)
    
    # 统计各级回文子串数量
    count = count_palindromes(dp)
    
    # 输出结果
    result = []
    # 注意：题目要求输出级别1到n的回文子串数量
    for k in range(1, len(s) + 1):
        result.append(str(count[k]))
    
    print(" ".join(result))