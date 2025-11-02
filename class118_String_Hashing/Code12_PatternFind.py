# SPOJ NAJPF Pattern Find
# 题目链接: https://www.spoj.com/problems/NAJPF/
# 题目大意: 给定一个字符串和一个模式串，找到模式串在字符串中所有出现的位置
# 
# 算法核心思想：
# 使用多项式滚动哈希（Polynomial Rolling Hash）算法实现高效的字符串匹配
# 通过将字符串转换为数值哈希，实现O(1)时间的子串比较
# 
# 算法详细步骤：
# 1. 预处理阶段：
#    - 计算文本字符串的前缀哈希数组
#    - 计算幂次数组，用于快速计算任意子串的哈希值
# 2. 模式串哈希计算：
#    - 计算模式串的哈希值
# 3. 匹配阶段：
#    - 在文本中使用滑动窗口，比较每个与模式串等长的子串哈希值
#    - 如果哈希值相等，则记录该位置
# 4. 结果输出：
#    - 输出匹配位置的数量和具体位置
# 
# 哈希算法原理：
# - 多项式滚动哈希函数：hash(s) = (s[1]*base^(m-1) + s[2]*base^(m-2) + ... + s[m]) % mod
# - 前缀哈希数组：prefixHash[i] = (prefixHash[i-1] * base + (s[i]-'a'+1)) % mod
# - 子串哈希计算：hash(l..r) = (prefixHash[r] - prefixHash[l-1] * base^(r-l+1)) % mod
# 
# 算法优势：
# - 高效性：预处理O(n)，查询O(n+m)，总体时间复杂度优于朴素O(nm)算法
# - 简洁性：实现简单，易于理解和维护
# - 可扩展性：可以处理多个模式串查询，只需预处理一次文本
# 
# 时间复杂度分析：
# - 预处理文本哈希和幂次数组：O(n)
# - 计算模式串哈希值：O(m)
# - 在文本中查找模式串：O(n)
# - 总体时间复杂度：O(n+m)
# 
# 空间复杂度分析：
# - 存储文本和模式串：O(n+m)
# - 存储哈希数组和幂次数组：O(n)
# - 存储结果列表：O(n)（最坏情况）
# - 总体空间复杂度：O(n+m)
# 
# 哈希冲突处理：
# - 使用大质数模数(1e9+7)和合适的基数(131)降低冲突概率
# - 在实际编程竞赛中，这种方法通常足够可靠
# - 对于生产环境，可以使用双哈希技术进一步降低冲突风险
# 
# 相似题目：
# 1. LeetCode 28: Implement strStr() - 查找子串首次出现位置
# 2. LeetCode 459: Repeated Substring Pattern - 检测重复子串模式
# 3. Codeforces 126B: Password - 查找满足特定条件的子串
# 4. POJ 1226: Substrings - 处理多个子串查询
# 5. HDU 1711: Number Sequence - 数值序列匹配问题
# 
# 三种语言实现参考：
# - Java实现：Code12_PatternFind.java
# - Python实现：当前文件
# - C++实现：Code12_PatternFind.cpp

def preprocess(s):
    """
    预处理函数，计算前缀哈希和幂次数组
    为后续快速计算任意子串的哈希值提供基础
    
    预处理内容：
    1. 幂次数组pow：存储base的各次幂，用于快速计算子串哈希
    2. 前缀哈希数组prefix_hash：从左到右计算哈希值
    
    哈希计算原理：
    - 多项式滚动哈希：将字符串视为base进制数
    - 前缀哈希：hash[i] = hash[i-1] * base + (s[i]-'a'+1) mod mod
    - 字符值偏移+1是为了避免'a'的哈希值为0，减少哈希冲突
    
    数学推导：
    - 对于字符串s[1..n]，哈希值为：s[1]*base^(n-1) + s[2]*base^(n-2) + ... + s[n]
    - 通过前缀哈希可以在O(1)时间内计算任意子串的哈希值
    
    :param s: 输入字符串
    :return: 前缀哈希数组、幂次数组
    
    时间复杂度：O(n) - 线性时间完成预处理
    空间复杂度：O(n) - 使用了两个长度为n+1的数组
    """
    n = len(s)
    base = 131  # 哈希基数，选择131作为哈希基数以减少哈希冲突
    mod = 1000000007  # 模数，使用1e9+7作为模数以控制哈希值大小并减少溢出
    
    # 计算幂次数组，pow_arr[i] = base^i % mod
    # 预计算幂次数组可以避免重复计算，提高哈希值计算效率
    pow_arr = [1] * (n + 1)
    pow_arr[0] = 1  # 初始化base^0 = 1
    for i in range(1, n + 1):
        # 递推计算并取模
        pow_arr[i] = (pow_arr[i - 1] * base) % mod
    
    # 计算前缀哈希数组，prefix_hash[i]表示子串s[1..i]的哈希值
    prefix_hash = [0] * (n + 1)
    prefix_hash[0] = 0  # 空字符串的哈希值为0
    for i in range(1, n + 1):
        # 哈希递推公式：prefix_hash[i] = (prefix_hash[i-1] * base + (s[i-1]-'a'+1)) % mod
        # 加1是为了避免字符'a'的哈希值为0，减少哈希冲突
        # 注意：Python中字符串索引从0开始，所以使用s[i-1]
        prefix_hash[i] = (prefix_hash[i - 1] * base + ord(s[i - 1]) - ord('a') + 1) % mod
    
    return prefix_hash, pow_arr

def get_hash(prefix_hash, pow_arr, l, r):
    """
    获取子串s[l..r]的哈希值
    利用前缀哈希数组快速计算任意子串的哈希值
    
    计算公式：
    hash(l..r) = (hash(1..r) - hash(1..l-1) * base^(r-l+1)) % mod
    
    数学原理详解：
    - 假设字符串s[1..r]的哈希值为：hash(1..r) = s[1]*base^(r-1) + s[2]*base^(r-2) + ... + s[r]
    - 字符串s[1..l-1]的哈希值为：hash(1..l-1) = s[1]*base^(l-2) + s[2]*base^(l-3) + ... + s[l-1]
    - 将hash(1..l-1)乘以base^(r-l+1)得到：s[1]*base^(r-1) + s[2]*base^(r-2) + ... + s[l-1]*base^(r-l+1)
    - 用hash(1..r)减去这个值，得到：s[l]*base^(r-l) + s[l+1]*base^(r-l-1) + ... + s[r]，即hash(l..r)
    
    取模处理说明：
    - 加上mod再取模是为了确保结果为非负数
    - 在减法操作中可能产生负数，需要调整为正数
    
    :param prefix_hash: 前缀哈希数组
    :param pow_arr: 幂次数组
    :param l: 左边界(1-based)
    :param r: 右边界(1-based)
    :return: 子串的哈希值
    
    时间复杂度：O(1) - 常数时间计算
    空间复杂度：O(1)
    """
    if l > r: 
        return 0  # 边界条件处理
    
    mod = 1000000007  # 模数
    
    # 哈希计算公式：hash(l..r) = (hash(1..r) - hash(1..l-1) * base^(r-l+1)) % mod
    # 加上mod再取模是为了确保结果非负
    res = (prefix_hash[r] - (prefix_hash[l - 1] * pow_arr[r - l + 1]) % mod + mod) % mod
    return res

def find_pattern(text, pattern):
    """
    查找模式串在文本中的所有出现位置
    使用哈希技术实现高效的字符串匹配
    
    算法步骤：
    1. 预处理文本字符串，计算前缀哈希和幂次数组
    2. 计算模式串的哈希值
    3. 在文本中使用滑动窗口技术，遍历所有可能的匹配位置
       - 对于每个位置i，计算从i开始长度为m的子串的哈希值
       - 如果与模式串哈希值相等，则记录该位置
    4. 返回所有匹配位置的列表
    
    滑动窗口原理：
    - 窗口大小固定为模式串长度m
    - 从文本字符串的起始位置开始，依次向右滑动一个字符
    - 每次滑动后比较当前窗口与模式串的哈希值
    
    哈希匹配优势：
    - 每次窗口滑动后的哈希比较只需O(1)时间
    - 相比朴素O(nm)算法，大幅提高了匹配效率
    
    注意事项：
    - 本实现使用单哈希，理论上存在哈希冲突可能
    - 在实际编程竞赛中，使用大质数模数和合适的基数通常足够可靠
    - 对于要求更高的场景，可以考虑使用双哈希（两个不同的哈希函数）
    
    :param text: 文本字符串
    :param pattern: 模式串
    :return: 所有出现位置的列表，位置从1开始计数
    
    时间复杂度：O(n+m)
    - 预处理文本：O(n)
    - 计算模式串哈希值：O(m)
    - 查找过程：O(n)
    空间复杂度：O(n+k)，其中k是匹配数量
    """
    n = len(text)  # 文本长度
    m = len(pattern)  # 模式串长度
    
    # 如果模式串长度大于文本长度，不可能匹配
    if m > n:
        return []
    
    # 预处理文本字符串，计算前缀哈希和幂次数组
    prefix_hash, pow_arr = preprocess(text)
    
    # 计算模式串的哈希值
    # 使用与文本相同的哈希算法，确保可比较性
    pattern_hash = 0
    base = 131  # 哈希基数
    mod = 1000000007  # 模数
    for i in range(m):
        # 多项式滚动哈希算法：hash = (hash * base + (pattern[i]-'a'+1)) % mod
        pattern_hash = (pattern_hash * base + ord(pattern[i]) - ord('a') + 1) % mod
    
    # 在文本中滑动窗口查找模式串
    # i是当前窗口的起始位置，窗口长度为m
    # 窗口范围：[i, i+m-1]，必须保证不超出文本边界
    positions = []
    for i in range(n - m + 1):
        # 计算当前窗口的哈希值并与模式串哈希值比较
        # 使用O(1)时间计算子串哈希值
        # 注意：Python中索引从0开始，但我们的哈希计算使用1-based索引
        if get_hash(prefix_hash, pow_arr, i + 1, i + m) == pattern_hash:
            # 如果哈希值相等，则记录该位置
            # 注意：题目要求位置从1开始计数
            positions.append(i + 1)
    
    return positions

# 主函数
if __name__ == "__main__":
    """
    主函数，处理输入输出并协调整个算法流程
    
    处理流程：
    1. 读取测试用例数量
    2. 对于每个测试用例：
       - 读取文本字符串和模式串
       - 调用find_pattern方法查找所有匹配位置
       - 输出结果（匹配数量和位置）
    
    输入格式：
    第一行：测试用例数量t
    每个测试用例：
     - 第一行：文本字符串
     - 第二行：模式串
    
    输出格式：
    对于每个测试用例：
     - 如果找到匹配：
       - 第一行：匹配数量
       - 第二行：所有匹配位置（从1开始计数）
     - 如果未找到匹配：
       - 一行："Not Found"
     - 测试用例之间输出空行
    
    时间复杂度：O(t*(n+m))，其中t是测试用例数量
    空间复杂度：O(n+m)
    """
    # 读取测试用例数量
    # t = int(input())
    
    # 由于是示例，我们使用硬编码的测试数据
    # 示例输入: text="AABAACAADAABAABA", pattern="AABA"
    text = "AABAACAADAABAABA"
    pattern = "AABA"
    
    positions = find_pattern(text, pattern)
    
    if not positions:
        print("Not Found")
    else:
        print(len(positions))
        print(" ".join(map(str, positions)))