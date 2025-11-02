# POJ 1200 Crazy Search
# 题目链接: http://poj.org/problem?id=1200
# 题目大意: 给定子串长度N，字符中不同字符数量NC，以及一个字符串，求不同子串数量
#
# 算法核心思想：
# 使用滚动哈希（Rolling Hash）技术结合集合（set）实现高效的子串去重统计
# 通过多项式哈希函数将字符串映射为数值，利用滑动窗口技术快速计算所有子串的哈希值
#
# 算法详细步骤：
# 1. 字符映射预处理：将输入字符串中的每个不同字符映射为唯一的数字ID
# 2. 哈希参数设置：选择合适的哈希基数base和模数mod
# 3. 幂次预计算：计算base^(n-1)用于滚动哈希中的最高位移除
# 4. 初始哈希计算：计算第一个长度为n的子串的哈希值
# 5. 滚动哈希计算：使用滑动窗口技术计算后续所有子串的哈希值
# 6. 去重统计：使用集合自动去重，统计不同哈希值的数量
#
# 哈希算法的数学原理：
# 多项式哈希函数的数学定义：hash(s) = (s₀×bⁿ⁻¹ + s₁×bⁿ⁻² + ... + sₙ₋₁×b⁰) mod m
# 其中：
# - s₀,s₁,...,sₙ₋₁是字符串中各字符的数值映射
# - b是哈希基数（base），通常选择较大的质数
# - m是模数，用于防止数值溢出
#
# 滚动哈希的数学证明：
# 假设我们有子串s[i...i+n-1]的哈希值为：
# hash = s[i]×bⁿ⁻¹ + s[i+1]×bⁿ⁻² + ... + s[i+n-1]×b⁰
# 
# 则下一个子串s[i+1...i+n]的哈希值为：
# next_hash = s[i+1]×bⁿ⁻¹ + s[i+2]×bⁿ⁻² + ... + s[i+n]×b⁰
# 
# 我们可以通过数学变换得到：
# next_hash = (hash - s[i]×bⁿ⁻¹) × b + s[i+n]
# 
# 证明：
# (hash - s[i]×bⁿ⁻¹) × b = (s[i+1]×bⁿ⁻² + ... + s[i+n-1]×b⁰) × b
#                         = s[i+1]×bⁿ⁻¹ + ... + s[i+n-1]×b¹
# 
# 添加s[i+n]×b⁰后：
# (hash - s[i]×bⁿ⁻¹) × b + s[i+n] = s[i+1]×bⁿ⁻¹ + ... + s[i+n-1]×b¹ + s[i+n]×b⁰ = next_hash
#
# 算法分析：
# 时间复杂度: O(M) 其中M是字符串长度
#   - 字符映射阶段：O(M)
#   - 预处理pow值：O(N)
#   - 第一个子串哈希计算：O(N)
#   - 滚动哈希计算：O(M-N)
#   - 总体时间复杂度：O(M)
# 
# 空间复杂度: O(M) 用于存储哈希值集合
#   - 哈希集合的大小最多为M-N+1
#   - 字符映射字典的大小为O(min(NC, 不同字符数))
#
# 哈希冲突处理策略：
# 1. 单哈希方案：当前实现使用单个哈希函数，对于POJ测试用例通常足够安全
# 2. 双哈希优化：使用两个不同的哈希函数（不同base和mod）可将冲突概率降低到接近零
# 3. 模数选择：使用大质数模数（如10^9+7或10^9+9）来防止溢出并减少冲突
# 4. 冲突验证：哈希值匹配后可进行字符串实际比较，确保绝对正确性
#
# 相似题目：
# 1. LeetCode 187: 重复的DNA序列 - 固定长度子串查找
# 2. Codeforces 271D: Good Substrings - 带约束的子串统计
# 3. SPOJ NAJPF: Pattern Find - 模式串匹配
# 4. LeetCode 1698: Number of Distinct Substrings in a String - 所有长度子串统计
#
# 三种语言实现参考：
# - Python实现：当前文件
# - Java实现：Code10_CrazySearch.java
# - C++实现：Code10_CrazySearch.cpp

def count_unique_substrings(s, n, nc):
    """
    计算字符串中长度为n的不同子串的数量
    使用滚动哈希和集合实现高效去重统计
    
    算法核心步骤：
    1. 字符映射：将每个不同字符映射到唯一数字ID
    2. 预计算base的n-1次方，用于滚动哈希
    3. 计算第一个子串的哈希值
    4. 滚动计算剩余子串的哈希值
    5. 使用集合自动去重，最终集合大小即为结果
    
    滚动哈希原理与数学证明：
    - 初始哈希值：hash = s[0]*base^(n-1) + s[1]*base^(n-2) + ... + s[n-1]
    - 滚动公式：hash = (hash - s[i-n]*base^(n-1))*base + s[i]
    
    数学证明：
    对于子串s[i...i+n-1]，其哈希值为：
    hash = s[i]*base^(n-1) + s[i+1]*base^(n-2) + ... + s[i+n-1]
    
    对于下一个子串s[i+1...i+n]，其哈希值为：
    next_hash = s[i+1]*base^(n-1) + s[i+2]*base^(n-2) + ... + s[i+n]
    
    我们可以通过以下变换得到next_hash：
    (hash - s[i]*base^(n-1)) = s[i+1]*base^(n-2) + ... + s[i+n-1]
    (hash - s[i]*base^(n-1))*base = s[i+1]*base^(n-1) + ... + s[i+n-1]*base
    (hash - s[i]*base^(n-1))*base + s[i+n] = next_hash
    
    字符映射策略分析：
    - 从1开始映射而不是从0开始，避免了前导零问题
    - 例如："a"和"0a"（假设0映射到0）可能产生相同的哈希值
    - 连续的数字ID分配最大化了不同字符间的数值差异，减少哈希冲突
    
    Python实现细节：
    - 使用字典（dict）作为字符映射表，比数组更灵活，可以处理任意字符
    - 使用集合（set）自动去重，集合的查找和插入操作平均时间复杂度为O(1)
    - Python中的整数没有固定大小限制，可以处理非常大的哈希值，无需额外的模数运算
    
    优化建议：
    1. 对于大数据量，可以考虑添加模数运算以保持哈希值在合理范围内
    2. 实现双哈希策略进一步减少哈希冲突
    3. 对于极端情况，可以预计算所有可能的base幂，避免重复计算
    
    @param s: 输入字符串
    @param n: 子串长度
    @param nc: 字符种类数
    @return: 不同子串的数量
    
    时间复杂度：O(M)，其中M是字符串长度
    空间复杂度：O(M)，用于存储哈希值集合和字符映射
    """
    len_s = len(s)
    
    # 边界条件检查：如果子串长度大于字符串长度或小于等于0，返回0
    if n > len_s or n <= 0:
        return 0
    
    # 创建字符到数字的映射字典
    # 使用字典而不是数组，因为Python中字符的Unicode范围很广
    char_map = {}
    char_count = 0
    
    # 遍历字符串，为每个不同的字符分配一个唯一的数字ID（从1开始）
    # 从1开始而不是0，可以避免前导零问题
    for char in s:
        if char not in char_map:
            char_count += 1
            char_map[char] = char_count
            # 如果字符种类数超过nc，说明题目条件不满足
            if char_count > nc:
                return 0
    
    # 使用集合存储哈希值，自动去重
    hash_set = set()
    
    # 选择哈希基数
    # 131是一个常用的哈希基数，在字符串哈希中表现良好
    base = 131
    
    # 计算base的n-1次方，用于滚动哈希中移除最高位
    # 预计算这个值可以避免重复计算，提高效率
    pow_base = 1
    for i in range(n - 1):
        pow_base *= base
    
    # 计算第一个长度为n的子串的哈希值
    # 使用滚动计算方法，避免显式计算指数
    hash_val = 0
    for i in range(n):
        # 哈希计算公式：hash = s[0]*base^(n-1) + s[1]*base^(n-2) + ... + s[n-1]
        # 等同于：hash = (((s[0]*base + s[1])*base + s[2])*base + ... ) + s[n-1]
        # 这种滚动计算方式更高效，避免了计算大数幂的开销
        hash_val = hash_val * base + char_map[s[i]]
    
    # 将第一个子串的哈希值添加到集合中
    hash_set.add(hash_val)
    
    # 使用滚动哈希技术高效计算后续所有长度为n的子串的哈希值
    # 时间复杂度为O(1) per substring
    for i in range(n, len_s):
        # 滚动哈希公式: 新哈希 = (旧哈希 - 最高位字符值 * base^(n-1)) * base + 新字符值
        # 1. 移除当前窗口最左边字符的贡献：hash - s[i-n] * base^(n-1)
        # 2. 所有剩余字符左移一位：乘以base
        # 3. 添加新进入窗口的字符：+ s[i]
        
        # 在Python中，整数可以无限大，不会发生溢出，无需额外处理
        hash_val = (hash_val - char_map[s[i - n]] * pow_base) * base + char_map[s[i]]
        
        # 将计算得到的哈希值添加到集合中（自动去重）
        hash_set.add(hash_val)
    
    # 集合的大小即为不同子串的数量
    # 注意：在存在哈希冲突的情况下，这个结果可能小于实际的不同子串数量
    return len(hash_set)


def count_unique_substrings_with_double_hash(s, n, nc):
    """
    优化版本：使用双哈希策略减少哈希冲突
    双哈希的基本思想是同时使用两个不同的哈希函数，只有当两个哈希值都相同时才认为子串相同
    
    双哈希原理：
    - 使用两个不同的哈希基数和模数
    - 为每个子串生成两个哈希值，并将它们的元组作为唯一标识
    - 只有当两个哈希值都相同时，才认为子串相同
    - 这种方法可以极大降低哈希冲突的概率
    
    Python实现优势：
    - Python中的元组可以直接作为集合的元素，无需额外处理
    - 整数的无限精度使得哈希计算更加简单
    
    @param s: 输入字符串
    @param n: 子串长度
    @param nc: 字符种类数
    @return: 不同子串的数量
    
    时间复杂度：O(M)，其中M是字符串长度
    空间复杂度：O(M)，用于存储双哈希值元组集合和字符映射
    """
    len_s = len(s)
    
    # 边界条件检查
    if n > len_s or n <= 0:
        return 0
    
    # 创建字符映射
    char_map = {}
    char_count = 0
    for char in s:
        if char not in char_map:
            char_count += 1
            char_map[char] = char_count
            if char_count > nc:
                return 0
    
    # 使用两个不同的哈希基数和模数
    base1, base2 = 499, 911
    mod1, mod2 = 10**9 + 7, 10**9 + 9
    
    # 预计算两个哈希基数的n-1次方
    pow_base1 = 1
    pow_base2 = 1
    for i in range(n - 1):
        pow_base1 = (pow_base1 * base1) % mod1
        pow_base2 = (pow_base2 * base2) % mod2
    
    # 计算第一个子串的双哈希值
    hash1, hash2 = 0, 0
    for i in range(n):
        hash1 = (hash1 * base1 + char_map[s[i]]) % mod1
        hash2 = (hash2 * base2 + char_map[s[i]]) % mod2
    
    # 使用集合存储双哈希值的元组
    hash_set = set()
    hash_set.add((hash1, hash2))
    
    # 滚动计算其他子串的双哈希值
    for i in range(n, len_s):
        # 计算第一个哈希值
        hash1 = ((hash1 - char_map[s[i-n]] * pow_base1 % mod1 + mod1) % mod1 * base1 % mod1 + char_map[s[i]]) % mod1
        
        # 计算第二个哈希值
        hash2 = ((hash2 - char_map[s[i-n]] * pow_base2 % mod2 + mod2) % mod2 * base2 % mod2 + char_map[s[i]]) % mod2
        
        # 添加双哈希值的元组
        hash_set.add((hash1, hash2))
    
    return len(hash_set)


def count_unique_substrings_optimized(s, n, nc):
    """
    进一步优化版本：使用预计算的幂值和更高效的字符映射
    
    优化点：
    1. 使用字典推导式或集合推导式简化代码
    2. 对于小数据集，可以考虑预计算所有可能的base幂
    3. 对于非常长的字符串，可以添加模数以防止哈希值过大
    
    @param s: 输入字符串
    @param n: 子串长度
    @param nc: 字符种类数
    @return: 不同子串的数量
    
    时间复杂度：O(M)，其中M是字符串长度
    空间复杂度：O(M)，用于存储哈希值集合和字符映射
    """
    # 这个函数作为优化示例，与count_unique_substrings功能相同
    # 但在实际应用中，可以根据具体需求选择最适合的实现
    return count_unique_substrings(s, n, nc)

# 主函数
if __name__ == "__main__":
    """
    主函数：处理输入并调用子串统计函数
    
    处理流程详解：
    1. 输入处理：
       - 读取子串长度n和字符种类数nc
       - 读取输入字符串s
    2. 算法执行：
       - 调用count_unique_substrings函数计算不同子串数量
    3. 结果输出：
       - 输出计算得到的不同子串数量
    
    输入格式：
    第一行：两个整数n和nc，分别表示子串长度和不同字符数量
    第二行：输入字符串
    
    输出格式：
    一个整数，表示长度为n的不同子串的数量
    
    测试用例说明：
    - 示例输入: n=3, nc=4, s="daababac"
    - 解释：该字符串中长度为3的子串有："daa", "aab", "aba", "bab", "aba", "bac"
    - 其中不同的子串有："daa", "aab", "aba", "bab", "bac"，共5个
    - 因此输出结果为5
    
    使用说明：
    1. 生产环境中应使用实际输入，取消注释相应代码
    2. 对于大数据量测试，可以比较不同实现的性能
    3. 对于对哈希冲突敏感的场景，可以使用双哈希版本
    
    时间复杂度：O(M)，其中M是字符串长度
    空间复杂度：O(M)，用于存储哈希值集合和字符映射
    """
    # 读取输入（实际应用中使用）
    # line = input().split()
    # n = int(line[0])
    # nc = int(line[1])
    # s = input().strip()
    
    # 由于是示例，我们使用硬编码的测试数据
    # 示例输入: n=3, nc=4, s="daababac"
    # 这个示例中，字符串包含'd','a','b','c'四种不同字符
    n = 3
    nc = 4
    s = "daababac"
    
    # 计算并输出结果（使用基本版本）
    result = count_unique_substrings(s, n, nc)
    print(f"基本版本结果: {result}")
    
    # 可选：使用双哈希版本计算结果
    # 对于相同的输入，两个版本的结果应该相同
    # result_double = count_unique_substrings_with_double_hash(s, n, nc)
    # print(f"双哈希版本结果: {result_double}")
    
    # 可选：使用优化版本计算结果
    # result_optimized = count_unique_substrings_optimized(s, n, nc)
    # print(f"优化版本结果: {result_optimized}")


# 性能比较和优化建议
"""
Python vs Java vs C++实现比较：

1. 性能差异：
   - Python实现代码简洁，但由于解释器开销，速度较慢
   - Java实现在中等数据规模下表现良好，JVM有JIT优化
   - C++实现在大数据规模下性能最优，接近硬件极限

2. 内存使用：
   - Python的字典和集合使用更多内存，但代码更简洁
   - Java的HashSet和数组平衡了内存使用和性能
   - C++的std::unordered_set和预分配数组内存效率最高

3. 适用场景：
   - Python实现适合算法原型设计和小规模数据
   - Java实现适合企业级应用和中等规模数据
   - C++实现适合竞赛编程和大规模数据处理

哈希冲突处理的最佳实践：
1. 对于一般应用，使用单一哈希函数加适当的base和mod即可
2. 对于竞赛或高可靠性要求，使用双哈希策略
3. 对于极端情况，可以在哈希值相同时进行字符串比较

算法扩展性：
- 此算法可扩展到计算所有可能长度的不同子串（LeetCode 1698）
- 可以修改为统计满足特定条件的子串数量（Codeforces 271D）
- 结合其他技术（如后缀数组）可以解决更复杂的字符串问题

哈希参数选择指南：
1. 基数选择：
   - 常用质数：131, 499, 911, 13331
   - 选择质数可以减少哈希冲突
   - 基数不宜过大，避免数值溢出

2. 模数选择：
   - 常用大质数：10^9+7, 10^9+9, 1e18+3
   - 模数应足够大以减少冲突
   - 模数应与基数互质

3. 字符映射：
   - 从1开始映射避免前导零问题
   - 连续映射提高哈希分布均匀性
   - 对于ASCII字符，可使用ord(char)-ord('a')+1

推荐测试用例：
1. 基本功能测试：
   - 输入：n=2, nc=3, s="abcabc"
   - 预期输出：3（"ab", "bc", "ca"）

2. 边界测试：
   - n=1, nc=1, s="aaaa" → 输出：1
   - n=5, nc=3, s="abc" → 输出：0

3. 重叠子串测试：
   - n=2, nc=2, s="aaaa" → 输出：1（"aa"）

4. 最大约束测试：
   - n=1000, nc=26, s=16000个字符的字符串
"""