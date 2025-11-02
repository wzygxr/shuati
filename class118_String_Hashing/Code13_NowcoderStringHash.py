# 牛客网字符串哈希题
# 题目链接: https://www.nowcoder.com/practice/dadbd37fee7c43f0ae407db11b16b4bf
# 题目大意: 给定N个字符串，计算其中不同字符串的个数
#
# 算法核心思想：
# 使用多项式滚动哈希算法将每个字符串映射为一个整数，然后使用集合（set）数据结构自动去重并统计数量
#
# 算法详细步骤：
# 1. 对于每个输入字符串，计算其哈希值
# 2. 将所有哈希值存储在集合中，利用集合的自动去重特性
# 3. 返回集合的大小，即为不同字符串的数量
#
# 多项式滚动哈希原理：
# - 哈希函数定义：H(s) = (s[0]*base^(n-1) + s[1]*base^(n-2) + ... + s[n-1]*base^0) % mod
# - 滚动计算：H(i) = (H(i-1) * base + s[i]) % mod
# - 字符映射：使用ord(char)将字符映射为ASCII值
#
# 哈希参数选择：
# - base = 131：常用的哈希基数，质数，分布均匀
# - mod = 1000000007：大质数，防止哈希值溢出
#
# 时间复杂度分析：
# - 计算单个字符串哈希值：O(M)，M为字符串长度
# - 处理N个字符串：O(N*M)
# - 集合插入操作：平均O(1)
# - 总体时间复杂度：O(N*M)
#
# 空间复杂度分析：
# - 存储哈希值集合：O(N)，最坏情况下所有字符串都不同
# - 存储输入字符串：O(N*M)
# - 总体空间复杂度：O(N*M)
#
# 哈希冲突处理：
# - 使用大质数作为模数降低冲突概率
# - 在实际应用中，可以使用双哈希进一步降低冲突风险
#
# 相似题目：
# 1. 洛谷 P3370 【模板】字符串哈希
# 2. LeetCode 187. 重复的DNA序列
# 3. Codeforces 271D Good Substrings
# 4. POJ 1200 Crazy Search
#
# 三种语言实现参考：
# - Java实现：Code13_NowcoderStringHash.java
# - Python实现：当前文件
# - C++实现：Code13_NowcoderStringHash.cpp

def calculate_hash(s):
    """
    计算字符串的哈希值
    
    使用多项式滚动哈希算法：hash(s) = (s[0]*base^(n-1) + s[1]*base^(n-2) + ... + s[n-1]) % mod
    
    算法原理：
    1. 多项式哈希将字符串视为base进制的数字，每个字符对应一个数字位
    2. 使用滚动计算方式避免计算大数幂：hash = hash * base + char_val
    3. 取模运算防止数值溢出并保持哈希值在合理范围内
    
    参数选择说明：
    - base = 131：质数，常用哈希基数，分布均匀
    - mod = 1000000007：大质数，防止哈希值溢出
    
    示例计算过程：
    对于字符串"abc"：
    - 初始 hash = 0
    - 处理 'a': hash = (0 * 131 + 97) % 1000000007 = 97
    - 处理 'b': hash = (97 * 131 + 98) % 1000000007 = 12805
    - 处理 'c': hash = (12805 * 131 + 99) % 1000000007 = 1677544
    
    :param s: 输入字符串
    :return: 计算得到的哈希值
    :time complexity: O(M)，其中M是字符串长度
    :space complexity: O(1)
    """
    base = 131
    mod = 1000000007
    hash_val = 0
    for char in s:
        # 滚动哈希计算：hash = (hash * base + char_ascii_value) % mod
        # 使用ord(char)获取字符的ASCII值作为字符映射
        hash_val = (hash_val * base + ord(char)) % mod
    return hash_val

def count_unique_strings(strings):
    """
    计算不同字符串的个数
    
    利用Python集合（set）的自动去重特性，将每个字符串的哈希值存储在集合中，
    最终集合的大小即为不同字符串的数量
    
    算法优势：
    1. 使用哈希值而非字符串本身进行比较，提高效率
    2. 利用集合的O(1)平均时间复杂度插入操作
    3. 自动处理重复元素的去重
    
    :param strings: 字符串列表
    :return: 不同字符串的个数
    :time complexity: O(N*M)，其中N是字符串个数，M是字符串平均长度
    :space complexity: O(N)，存储哈希值集合
    """
    # 使用集合存储哈希值，利用集合的自动去重特性
    # Python的set内部使用哈希表实现，插入和查找的平均时间复杂度为O(1)
    hash_set = set()
    
    # 计算每个字符串的哈希值并添加到集合中
    for s in strings:
        # 计算当前字符串的哈希值
        hash_val = calculate_hash(s)
        # 将哈希值添加到集合中，重复的哈希值会被自动忽略
        hash_set.add(hash_val)
    
    # 返回集合的大小，即为不同字符串的数量
    return len(hash_set)

# 主函数
if __name__ == "__main__":
    # 读取输入
    # n = int(input())
    # strings = []
    # for _ in range(n):
    #     strings.append(input().strip())
    
    # 由于是示例，我们使用硬编码的测试数据
    # 示例输入: 4个字符串 "abc", "aaaa", "abc", "abcc"
    # 预期输出: 3 (不同字符串为"abc", "aaaa", "abcc")
    n = 4
    strings = ["abc", "aaaa", "abc", "abcc"]
    
    # 计算并输出结果
    result = count_unique_strings(strings)
    print(result)