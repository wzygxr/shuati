# 根据字符出现频率排序
# 给定一个字符串 s ，根据字符出现的 频率 对其进行 降序排序 。
# 一个字符出现的 频率 是它出现在字符串中的次数。
# 返回 已排序的字符串 。如果有多个答案，返回其中任何一个。
# 测试链接 : https://leetcode.cn/problems/sort-characters-by-frequency/

def frequencySort(s):
    """
    根据字符出现频率排序
    
    算法思路：
    使用贪心策略：
    1. 统计每个字符出现的频率
    2. 按频率降序排序字符
    3. 构建结果字符串
    
    正确性分析：
    1. 我们需要按频率降序排列字符
    2. 贪心选择频率最高的字符，可以得到正确的结果
    
    时间复杂度：O(n + k*logk) - n是字符串长度，k是字符集大小
    空间复杂度：O(n + k) - 需要存储字符频率和结果字符串
    
    :param s: 输入字符串
    :return: 按频率排序后的字符串
    """
    # 边界情况处理
    if not s:
        return s
    
    # 统计每个字符出现的频率
    frequency_map = {}
    for char in s:
        frequency_map[char] = frequency_map.get(char, 0) + 1
    
    # 按频率降序排序字符
    sorted_chars = sorted(frequency_map.items(), key=lambda x: x[1], reverse=True)
    
    # 构建结果字符串
    result = []
    for char, frequency in sorted_chars:
        result.append(char * frequency)
    
    return ''.join(result)

# 测试用例
if __name__ == "__main__":
    # 测试用例1: s = "tree" -> 输出: "eert" 或 "eetr"
    s1 = "tree"
    print("测试用例1:")
    print("字符串:", s1)
    print("排序结果:", frequencySort(s1))  # 期望输出: "eert" 或 "eetr"
    
    # 测试用例2: s = "cccaaa" -> 输出: "cccaaa" 或 "aaaccc"
    s2 = "cccaaa"
    print("\n测试用例2:")
    print("字符串:", s2)
    print("排序结果:", frequencySort(s2))  # 期望输出: "cccaaa" 或 "aaaccc"
    
    # 测试用例3: s = "Aabb" -> 输出: "bbAa" 或 "bbaA"
    s3 = "Aabb"
    print("\n测试用例3:")
    print("字符串:", s3)
    print("排序结果:", frequencySort(s3))  # 期望输出: "bbAa" 或 "bbaA"
    
    # 测试用例4: s = "abcdefg" -> 输出: "abcdefg" 或其他排列
    s4 = "abcdefg"
    print("\n测试用例4:")
    print("字符串:", s4)
    print("排序结果:", frequencySort(s4))  # 期望输出: "abcdefg" 或其他排列