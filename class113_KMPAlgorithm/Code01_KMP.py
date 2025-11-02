"""
KMP算法及其应用题目集合（Python版本）

KMP算法（Knuth-Morris-Pratt算法）是一种高效的字符串匹配算法，通过预处理模式串来避免在匹配失败时文本串指针的回溯，
从而将时间复杂度从朴素匹配算法的O(n*m)降低到O(n+m)。

本模块包含：
1. KMP算法核心实现（next数组构建和匹配过程）
2. 多个LeetCode题目的KMP解法实现
3. 详细的测试用例

KMP算法数学原理深度解析：

1. 问题本质：
   - 朴素字符串匹配算法在字符不匹配时，文本串指针需要回溯到起始位置的下一个位置，导致重复比较
   - KMP算法的核心创新在于：利用已匹配的信息，避免重复比较

2. 关键概念：部分匹配表（next数组）
   - next[i]表示模式串中以i-1位置字符结尾的子串，其前缀和后缀的最大匹配长度
   - 前缀：不包含最后一个字符的子串
   - 后缀：不包含第一个字符的子串

3. next数组数学原理：
   - 假设对于位置i，我们要计算next[i]
   - 已经知道next[i-1] = k，表示s[0...k-1]和s[i-k-1...i-2]匹配
   - 如果s[k] == s[i-1]，则next[i] = k+1
   - 如果不相等，则递归查找next[k]，直到找到匹配或回溯到0

4. KMP匹配过程原理：
   - 双指针技术：i指向文本串，j指向模式串
   - 当字符匹配时，两个指针都前进
   - 当字符不匹配时，模式串指针j根据next[j]回退，文本串指针i保持不动
   - 这种回退策略确保了i永远不会回退，避免了重复比较

5. 算法正确性证明：
   - 数学归纳法证明next数组构建的正确性
   - 反证法证明匹配过程不会漏掉任何可能的匹配
   - 复杂度分析证明时间复杂度为O(n+m)

6. KMP算法优势：
   - 线性时间复杂度：O(n+m)
   - 空间复杂度：O(m)，仅需存储next数组
   - 预处理只需要一次，适用于重复匹配场景
   - 确定性算法，无哈希冲突风险

@author Algorithm Journey
@version 1.0
@since 2024-01-01
"""



def next_array(s):
    """
    构建next数组（部分匹配表）
    
    next数组是KMP算法的核心数据结构，它存储了模式串的前缀信息，用于在匹配失败时快速调整模式串指针。
    
    next数组详细定义：
    - next[i]表示模式串中以i-1位置字符结尾的子串，其真前缀和真后缀的最大匹配长度
    - 真前缀：不包含最后一个字符的前缀
    - 真后缀：不包含第一个字符的后缀
    
    next数组构建过程的数学原理解析：
    
    1. 初始化：
       - next[0] = -1（特殊标记，用于边界条件处理）
       - next[1] = 0（长度为1的子串没有真前缀和真后缀，匹配长度为0）
    
    2. 递推过程：
       - i: 当前要求解next值的位置
       - cn: 当前尝试匹配的前缀末尾位置（也是已匹配前缀的长度）
       - 对于i >= 2的情况：
         a) 若s[i-1] == s[cn]，则next[i] = cn + 1
         b) 若不相等且cn > 0，则递归查找next[cn]
         c) 若cn == 0，则next[i] = 0
    
    3. 数学归纳法证明：
       - 假设对于所有j < i，next[j]的值都是正确计算的
       - 证明next[i]的计算也是正确的：
         - 当s[i-1] == s[cn]时，显然前缀和后缀的匹配长度增加1
         - 当s[i-1] != s[cn]时，cn = next[cn]表示寻找更短的可能匹配
         - 由于next[cn]是当前已知的最长可能前缀，所以正确性得证
    
    4. 复杂度分析：
       - 时间复杂度：O(m)，虽然有嵌套循环，但i和cn都是单调递增的，整体最多执行2m次操作
       - 空间复杂度：O(m)，需要存储长度为m的next数组
    
    示例：
    对于模式串"ABABCABAA"
    - next[0] = -1
    - next[1] = 0
    - next[2] = 0
    - next[3] = 1
    - next[4] = 2
    - next[5] = 0
    - next[6] = 1
    - next[7] = 2
    - next[8] = 3
    - next[9] = 1
    
    参数:
        s: 模式串（字符串）
    返回:
        长度为len(s)+1的next数组，其中next[i]表示以i-1结尾的子串的前后缀最大匹配长度
    """
    # 获取模式串长度
    m = len(s)
    
    # 边界条件：单个字符的next数组只包含-1
    if m == 1:
        return [-1]
    
    # 初始化next数组，长度为m+1
    next_arr = [0] * (m + 1)
    # 特殊值标记，用于处理边界情况
    next_arr[0] = -1
    # 长度为1的子串没有真前缀和真后缀，匹配长度为0
    next_arr[1] = 0
    
    # i: 当前要求解next值的位置
    i = 2
    # cn: 当前尝试匹配的前缀末尾位置（也是已匹配前缀的长度）
    cn = 0
    
    # 构建next数组主循环
    while i <= m:
        # 情况1：字符匹配成功
        if s[i - 1] == s[cn]:
            # 匹配长度增加1
            next_arr[i] = cn + 1
            # 同时移动i和cn，准备处理下一个位置
            i += 1
            cn += 1
        # 情况2：字符不匹配，但cn仍有回退空间
        elif cn > 0:
            # 根据next数组递归回退cn
            # 这是KMP算法的关键优化，避免了暴力回溯
            cn = next_arr[cn]
        # 情况3：字符不匹配且cn已无法回退
        else:
            # 无法找到匹配的前缀，next值为0
            next_arr[i] = 0
            # 移动到下一个位置
            i += 1
    
    return next_arr


def kmp(s1, s2):
    """
    KMP算法核心实现 - 字符串匹配算法
    
    KMP算法通过巧妙利用已匹配信息，避免在匹配失败时文本串指针的回溯，
    从而实现线性时间复杂度的字符串匹配。
    
    算法匹配过程详细解析：
    
    1. 预处理：
       - 构建模式串s2的next数组，这是KMP算法的核心数据结构
       - next数组存储了模式串的前缀信息，用于在匹配失败时确定模式串指针的新位置
    
    2. 匹配过程：
       - x: 文本串s1的当前指针位置
       - y: 模式串s2的当前指针位置
       - 进入主循环，同时遍历两个字符串：
         a) 情况1：字符匹配成功(s1[x] == s2[y])
            - 两个指针都向前移动：x += 1, y += 1
            - 继续比较下一对字符
         b) 情况2：字符不匹配且模式串指针已在起始位置(y == 0)
            - 模式串无法再回退，文本串指针前进：x += 1
            - 从文本串下一个位置开始匹配
         c) 情况3：字符不匹配且模式串指针不在起始位置(y > 0)
            - 根据next数组调整模式串指针：y = next_arr[y]
            - 这是KMP算法的核心优化，避免了文本串指针的回溯
    
    3. 匹配结束条件：
       - 成功匹配：当y == m（模式串指针已到达末尾）时，表示找到完整匹配
         - 返回匹配起始位置：x - y
       - 匹配失败：当x == n（文本串已遍历完）且y < m时，表示未找到匹配
         - 返回-1表示匹配失败
    
    4. 算法正确性证明：
       - 数学归纳法证明不会漏掉任何可能的匹配
       - 假设在位置x,y处发生不匹配：
         - 根据next数组的定义，我们知道模式串前y-1个字符中有长度为next[y]的相同前后缀
         - 因此，可以安全地将模式串移动到next[y]位置继续匹配，而不会漏掉任何可能的匹配
         - 文本串指针x不需要回退，确保了O(n)的时间复杂度
    
    5. 复杂度分析：
       - 时间复杂度：O(n + m)，其中n是文本串长度，m是模式串长度
         - 构建next数组：O(m)
         - 匹配过程：O(n)，虽然有循环嵌套，但y在每次循环中要么增加，要么减少，整体最多执行2n次操作
       - 空间复杂度：O(m)，用于存储next数组
    
    6. 算法优化思路：
       - 优化next数组：在某些情况下可以进一步优化next数组，减少不必要的比较
       - 对于多次查询场景，可以预先生成并缓存next数组
    
    参数:
        s1: 文本串
        s2: 模式串
    返回:
        模式串在文本串中首次出现的索引，如果不存在则返回-1
    """
    # 边界条件处理：空模式串始终匹配，返回0
    if not s2:
        return 0
    # 边界条件处理：空文本串无法匹配非空模式串，返回-1
    if not s1:
        return -1
    
    # 获取文本串和模式串长度
    n, m = len(s1), len(s2)
    # x: 文本串指针，y: 模式串指针
    x, y = 0, 0
    
    # 预处理阶段：构建next数组，O(m)时间复杂度
    next_arr = next_array(s2)
    
    # 匹配阶段：O(n)时间复杂度
    while x < n and y < m:
        # 情况1：字符匹配成功，两个指针都向前移动
        if s1[x] == s2[y]:
            x += 1
            y += 1
        # 情况2：字符不匹配且模式串指针已在起始位置，文本串指针前进
        elif y == 0:
            x += 1
        # 情况3：字符不匹配且模式串指针不在起始位置，根据next数组回退模式串指针
        # 这是KMP算法的核心优化，避免了文本串指针的回溯
        else:
            y = next_arr[y]
    
    # 返回匹配结果：如果模式串完全匹配，返回起始位置；否则返回-1
    return x - y if y == m else -1


def str_str(haystack, needle):
    """
    LeetCode 28: strStr()
    实现 strStr() 函数
    给你两个字符串 haystack 和 needle，请你在 haystack 字符串中找出 needle 字符串的第一个匹配项的下标（下标从 0 开始）
    如果 needle 不是 haystack 的一部分，则返回 -1
    
    时间复杂度：O(n + m)，其中n是文本串长度，m是模式串长度
    空间复杂度：O(m)，用于存储next数组
    
    参数:
        haystack: 文本串
        needle: 模式串
    返回:
        模式串在文本串中首次出现的索引，如果不存在则返回-1
    """
    return kmp(haystack, needle)


def repeated_substring_pattern(s):
    """
    LeetCode 459: 重复的子字符串
    
    题目描述：给定一个非空的字符串，检查它是否可以通过由它的一个子串重复多次构成
    
    算法思路深度解析：
    
    1. 问题转化：
       - 如果字符串s由子串p重复k次构成，那么s = p * k，其中k >= 2
       - 这种情况下，s + s会包含s作为其内部子串，且起始位置不是0或len(s)
    
    2. 数学证明：
       - 假设s = p * k，其中p是子串，k >= 2
       - 那么s + s = p * k + p * k = p * 2k
       - 移除首尾字符后，target = s + s的[1:-1]部分
       - 则s必然是target的子串，起始位置为len(p)
    
    3. 反向证明：
       - 如果s是s + s的[1:-1]部分的子串，则s必然由重复子串构成
       - 假设s在target中的起始位置为i（1 <= i < len(s)）
       - 那么对于所有0 <= j < len(s) - i，有s[j] = s[i + j]
       - 这意味着子串p = s[0:i]是s的一个重复子串
    
    4. 算法实现：
       - 构造doubled = s + s
       - 截取target = doubled[1:-1]（去掉首尾字符）
       - 使用KMP算法检查s是否是target的子串
       - 如果是，则s由重复子串构成
    
    5. 边界条件处理：
       - 空字符串或长度为1的字符串不可能由重复子串构成
       - 因此当len(s) < 2时直接返回False
    
    6. 复杂度分析：
       - 时间复杂度：O(n)
         - 构造字符串：O(n)
         - KMP匹配：O(n)
       - 空间复杂度：O(n)
         - 存储doubled和target：O(n)
         - KMP算法的next数组：O(n)
    
    7. 示例分析：
       - 对于s = "abab"，doubled = "abababab"，target = "bababa"
       - s确实是target的子串，起始位置为1，因此返回True
       - 对于s = "aba"，doubled = "abaaba"，target = "baab"
       - s不是target的子串，因此返回False
    
    参数:
        s: 输入字符串
    返回:
        bool: 是否由重复子串构成
    """
    # 边界条件检查：长度小于2的字符串不可能由重复子串构成
    if len(s) < 2:
        return False
    
    # 构造s+s并去掉首尾字符
    doubled = s + s
    # 截取中间部分，避免匹配到原始字符串的起始位置
    target = doubled[1:-1]  # 去掉首尾字符
    
    # 使用KMP算法检查s是否是target的子串
    # 如果是，则s由重复子串构成
    return kmp(target, s) != -1


def longest_prefix(s):
    """
    LeetCode 1392: 最长快乐前缀
    
    题目描述：编写一个算法来查找字符串s的最长的快乐前缀，快乐前缀是既是前缀又是后缀的字符串，但不能是整个字符串本身
    
    算法思路深度解析：
    
    1. 问题转化：
       - 寻找最长的非空前缀，该前缀同时也是后缀
       - 这正是KMP算法中next数组的核心功能
    
    2. next数组特性应用：
       - 在我们的next数组定义中，next[i]表示前i个字符的最长相等前后缀长度
       - 因此，next[len(s)]正好表示整个字符串的最长相等前后缀长度
       - 这正是我们要找的最长快乐前缀的长度
    
    3. 数学原理：
       - 对于字符串s，next[len(s)]的值就是最大的k，使得s[0...k-1] == s[len(s)-k...len(s)-1]
       - 这正好满足快乐前缀的定义：既是前缀又是后缀的最长字符串
    
    4. 算法实现：
       - 计算整个字符串的next数组
       - 获取next[len(s)]作为最长快乐前缀的长度
       - 截取字符串前next[len(s)]个字符作为结果
    
    5. 边界条件处理：
       - 空字符串或长度为1的字符串没有快乐前缀
       - 因此当len(s) < 2时直接返回空字符串
    
    6. 复杂度分析：
       - 时间复杂度：O(n)，主要用于构建next数组
       - 空间复杂度：O(n)，用于存储next数组
    
    7. 示例分析：
       - 对于s = "level"，next数组计算结果为next[5] = 1
       - 因此最长快乐前缀是s[0:1] = "l"
       - 对于s = "ababab"，next数组计算结果为next[6] = 4
       - 因此最长快乐前缀是s[0:4] = "abab"
    
    8. 算法优化：
       - 该实现已经是最优的，时间复杂度为O(n)，空间复杂度为O(n)
       - 对于重复查询场景，可以考虑缓存next数组
    
    参数:
        s: 输入字符串
    返回:
        str: 最长快乐前缀（既是前缀又是后缀的最长字符串）
    """
    # 边界条件：长度小于2的字符串没有快乐前缀
    if len(s) < 2:
        return ""
    
    # 计算整个字符串的next数组
    next_arr = next_array(s)
    # 获取最长快乐前缀的长度
    # 注意：next_arr的长度是len(s)+1，所以索引应该是len(s)
    max_len = next_arr[len(s)]
    
    # 返回最长快乐前缀
    return s[:max_len]


def shortest_palindrome(s):
    """
    LeetCode 214: 最短回文串
    
    题目描述：给定一个字符串s，你可以通过在字符串前面添加字符将其转换为回文串，请找出并返回可以用这种方式转换的最短回文串
    
    算法思路深度解析：
    
    1. 问题转化：
       - 要找到最短的回文串，可以通过在s前面添加最少的字符实现
       - 这等价于找到s中最长的前缀，使其本身是一个回文串
       - 然后将s中剩余的非回文后缀反转并添加到s前面
    
    2. 数学模型：
       - 假设s的最长回文前缀长度为k
       - 则最短回文串为：reverse(s[k:]) + s
       - 因此，关键是找到最大的k，使得s[0...k-1]是回文串
    
    3. KMP算法应用：
       - 构造字符串combined = s + "#" + reversed_s
       - 其中"#"是一个不会在s中出现的特殊字符，用于分隔s和reversed_s
       - 计算combined的next数组，next[len(combined)]表示s和reversed_s的最长公共前缀和后缀长度
       - 这正好对应s的最长回文前缀长度
    
    4. 数学证明：
       - 假设next[len(combined)] = k
       - 则s[0...k-1] == reversed_s[0...k-1]
       - 而reversed_s[0...k-1] = reverse(s[-k:])
       - 因此s[0...k-1] == reverse(s[-k:])
       - 这意味着s[0...k-1]是回文串
    
    5. 算法实现：
       - 构造reversed_s = reverse(s)
       - 构造combined = s + "#" + reversed_s
       - 计算combined的next数组
       - 获取max_prefix_len = next[len(combined)]
       - 构造最短回文串：reverse(s[max_prefix_len:]) + s
    
    6. 边界条件处理：
       - 空字符串或长度为1的字符串本身就是回文串
       - 因此当len(s) <= 1时直接返回s
    
    7. 复杂度分析：
       - 时间复杂度：O(n)
         - 反转字符串：O(n)
         - 构造combined：O(n)
         - 计算next数组：O(n)
       - 空间复杂度：O(n)
         - 存储reversed_s和combined：O(n)
         - 存储next数组：O(n)
    
    8. 示例分析：
       - 对于s = "aacecaaa"，reversed_s = "aaacecaa"
       - combined = "aacecaaa#aaacecaa"
       - next[len(combined)] = 7
       - 因此最短回文串是reverse(s[7:]) + s = "a" + "aacecaaa" = "aaacecaaa"
    
    参数:
        s: 输入字符串
    返回:
        str: 通过在前面添加最少字符得到的最短回文串
    """
    # 边界条件：空字符串或长度为1的字符串本身就是回文串
    if len(s) <= 1:
        return s
    
    # 反转字符串
    reversed_s = s[::-1]  # 使用Python切片操作反转字符串
    # 构造组合字符串，使用#作为分隔符，确保不会有错误匹配
    combined = s + "#" + reversed_s
    
    # 计算组合字符串的next数组
    next_arr = next_array(combined)
    # 获取最长回文前缀的长度
    max_prefix_len = next_arr[len(combined)]
    
    # 构造最短回文串：
    # 1. 取反转字符串的前(len(reversed_s) - max_prefix_len)个字符
    # 2. 拼接到原字符串前面
    return reversed_s[:len(reversed_s) - max_prefix_len] + s


def rotate_string(s, goal):
    """
    LeetCode 796: 旋转字符串
    
    题目描述：给定两个字符串, s和goal，如果s在若干次旋转操作之后，能变成goal，那么返回true
    旋转操作指的是将s最左边的字符移动到最右边
    
    算法思路深度解析：
    
    1. 问题分析：
       - 旋转操作：每次将第一个字符移到末尾
       - 例如，s = "abcde"，旋转一次得到"bcdea"，旋转两次得到"cdeab"等
       - 我们需要判断goal是否是s经过若干次旋转后的结果
    
    2. 关键洞察：
       - 如果s经过k次旋转后等于goal，则goal必须是s+s的子串
       - 且起始位置为k，长度为len(s)
    
    3. 数学证明：
       - 假设s经过k次旋转后等于goal
       - 则goal = s[k:] + s[:k]
       - 而s+s = s + s = s[0:] + s[0:] = s[0:k] + s[k:] + s[0:k] + s[k:]
       - 因此，goal = s[k:] + s[:k]必然是s+s的子串
    
    4. 反向证明：
       - 如果goal是s+s的子串，且长度等于s的长度
       - 假设起始位置为k
       - 则goal = s+s[k:k+len(s)] = s[k:] + s[:k - len(s)]（当k >= len(s)时）
       - 但由于k < 2*len(s)，所以k - len(s)在0到len(s)之间
       - 因此goal = s[k - len(s):] + s[:k - len(s)]
       - 这意味着goal是s经过(k - len(s))次旋转后的结果
    
    5. 算法实现：
       - 首先检查s和goal的长度是否相等
       - 如果长度为0，直接返回True
       - 构造doubled = s + s
       - 使用KMP算法检查goal是否是doubled的子串
    
    6. 边界条件处理：
       - 长度不同的字符串不可能通过旋转得到
       - 空字符串可以通过0次旋转得到自身
    
    7. 复杂度分析：
       - 时间复杂度：O(n)
         - 构造doubled：O(n)
         - KMP匹配：O(n)
       - 空间复杂度：O(n)
         - 存储doubled：O(n)
         - KMP算法的next数组：O(n)
    
    8. 示例分析：
       - 对于s = "abcde"，doubled = "abcdeabcde"
       - goal = "cdeab"，确实是doubled的子串（起始位置2），因此返回True
       - 对于s = "abcde"，goal = "abced"，不是doubled的子串，因此返回False
    
    参数:
        s: 原始字符串
        goal: 目标字符串
    返回:
        bool: 是否可以通过旋转得到
    """
    # 边界条件：长度不同的字符串不可能通过旋转得到
    if len(s) != len(goal):
        return False
    # 边界条件：空字符串可以通过0次旋转得到自身
    if len(s) == 0:
        return True
    
    # 构造s+s
    doubled = s + s
    # 使用KMP算法检查goal是否是doubled的子串
    # 如果是，则说明s可以通过旋转得到goal
    return kmp(doubled, goal) != -1


def test_str_str():
    """
    测试strStr函数
    """
    print("测试LeetCode 28: strStr()")
    
    # 测试用例1: 基本匹配
    haystack1 = "hello"
    needle1 = "ll"
    result1 = str_str(haystack1, needle1)
    print(f"文本串: {haystack1}")
    print(f"模式串: {needle1}")
    print(f"结果: {result1} (期望: 2)")
    
    # 测试用例2: 不匹配
    haystack2 = "aaaaa"
    needle2 = "bba"
    result2 = str_str(haystack2, needle2)
    print(f"文本串: {haystack2}")
    print(f"模式串: {needle2}")
    print(f"结果: {result2} (期望: -1)")
    
    # 测试用例3: 模式串为空
    haystack3 = "abc"
    needle3 = ""
    result3 = str_str(haystack3, needle3)
    print(f"文本串: {haystack3}")
    print(f"模式串: {needle3}")
    print(f"结果: {result3} (期望: 0)")


def test_repeated_substring_pattern():
    """
    测试repeatedSubstringPattern函数
    """
    print("测试LeetCode 459: 重复的子字符串")
    
    # 测试用例1: "abab" -> True ("ab"重复两次)
    print(f"字符串: \"abab\" 结果: {repeated_substring_pattern('abab')} (期望: True)")
    
    # 测试用例2: "aba" -> False
    print(f"字符串: \"aba\" 结果: {repeated_substring_pattern('aba')} (期望: False)")
    
    # 测试用例3: "abcabcabcabc" -> True ("abc"重复四次)
    print(f"字符串: \"abcabcabcabc\" 结果: {repeated_substring_pattern('abcabcabcabc')} (期望: True)")


def test_longest_prefix():
    """
    测试longestPrefix函数
    """
    print("测试LeetCode 1392: 最长快乐前缀")
    
    # 测试用例1: "level" -> "l"
    print(f"字符串: \"level\" 结果: \"{longest_prefix('level')}\" (期望: l)")
    
    # 测试用例2: "ababab" -> "abab"
    print(f"字符串: \"ababab\" 结果: \"{longest_prefix('ababab')}\" (期望: abab)")
    
    # 测试用例3: "leetcodeleet" -> "leet"
    print(f"字符串: \"leetcodeleet\" 结果: \"{longest_prefix('leetcodeleet')}\" (期望: leet)")


def test_shortest_palindrome():
    """
    测试shortestPalindrome函数
    """
    print("测试LeetCode 214: 最短回文串")
    
    # 测试用例1: "aacecaaa" -> "aaacecaaa"
    print(f"字符串: \"aacecaaa\" 结果: \"{shortest_palindrome('aacecaaa')}\" (期望: aaacecaaa)")
    
    # 测试用例2: "abcd" -> "dcbabcd"
    print(f"字符串: \"abcd\" 结果: \"{shortest_palindrome('abcd')}\" (期望: dcbabcd)")
    
    # 测试用例3: "a" -> "a"
    print(f"字符串: \"a\" 结果: \"{shortest_palindrome('a')}\" (期望: a)")


def test_rotate_string():
    """
    测试rotateString函数
    """
    print("测试LeetCode 796: 旋转字符串")
    
    # 测试用例1: s="abcde", goal="cdeab" -> True
    print(f"s: \"abcde\", goal: \"cdeab\" 结果: {rotate_string('abcde', 'cdeab')} (期望: True)")
    
    # 测试用例2: s="abcde", goal="abced" -> False
    print(f"s: \"abcde\", goal: \"abced\" 结果: {rotate_string('abcde', 'abced')} (期望: False)")
    
    # 测试用例3: s="", goal="" -> True
    print(f"s: \"\", goal: \"\" 结果: {rotate_string('', '')} (期望: True)")


if __name__ == "__main__":
    # 测试LeetCode 28: strStr()
    test_str_str()
    print("===============")
    
    # 测试LeetCode 459: 重复的子字符串
    test_repeated_substring_pattern()
    print("===============")
    
    # 测试LeetCode 1392: 最长快乐前缀
    test_longest_prefix()
    print("===============")
    
    # 测试LeetCode 214: 最短回文串
    test_shortest_palindrome()
    print("===============")
    
    # 测试LeetCode 796: 旋转字符串
    test_rotate_string()