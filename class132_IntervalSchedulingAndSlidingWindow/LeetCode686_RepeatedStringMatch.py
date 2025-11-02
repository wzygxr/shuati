"""
LeetCode 686. Repeated String Match

题目描述：
给定两个字符串 a 和 b，寻找重复 a 的最小次数，使得 b 成为重复 a 后的字符串的子串。
如果不存在这样的次数，则返回 -1。

解题思路：
这是一个字符串匹配问题，需要找到最小的重复次数使得 b 成为重复 a 后的字符串的子串。

算法步骤：
1. 计算理论最小重复次数：ceil(len(b) / len(a))
2. 从理论最小次数开始尝试，最多尝试 3 次额外的重复
3. 对于每次重复，检查 b 是否为重复字符串的子串
4. 如果找到则返回重复次数，否则返回 -1

为什么最多尝试 3 次额外重复：
- 理论最小次数确保重复字符串长度 >= b 的长度
- 额外的 1-2 次重复处理边界情况：
  - b 可能从一个 a 的末尾开始
  - b 可能到下一个 a 的开头结束
- 如果 3 次尝试后仍未找到，则说明不可能匹配

时间复杂度：O(n * m)，其中 n 是 a 的长度，m 是重复次数
空间复杂度：O(n * m)

相关题目：
- LeetCode 466. 统计重复个数（字符串匹配与循环节）
- LeetCode 459. 重复的子字符串
- LeetCode 28. 实现 strStr()
"""

import math

def repeatedStringMatch(a, b):
    """
    寻找重复 a 的最小次数，使得 b 成为重复 a 后的字符串的子串
    
    Args:
        a: 字符串 a
        b: 字符串 b
    
    Returns:
        最小重复次数，如果不存在则返回 -1
    """
    len_a = len(a)
    len_b = len(b)
    
    # 边界情况处理
    if len_a == 0:
        return -1
    
    if len_b == 0:
        return 0
    
    # 计算理论最小重复次数
    # 确保重复后的字符串长度至少等于 b 的长度
    min_repetitions = math.ceil(len_b / len_a)
    
    # 构建初始重复字符串
    repeated_str = a * min_repetitions
    
    # 尝试最多 3 次额外重复
    # 处理边界情况：b 可能跨越多个 a 的边界
    for i in range(3):
        # 检查 b 是否为当前重复字符串的子串
        if b in repeated_str:
            return min_repetitions
        
        # 添加一次额外重复
        min_repetitions += 1
        repeated_str += a
    
    # 如果尝试了足够的重复次数仍未找到，则不可能匹配
    return -1

# 测试用例
if __name__ == "__main__":
    # 测试用例1
    a1 = "abcd"
    b1 = "cdabcdab"
    print("测试用例1:")
    print(f"输入: a = \"{a1}\", b = \"{b1}\"")
    print(f"输出: {repeatedStringMatch(a1, b1)}")  # 期望输出: 3
    
    # 测试用例2
    a2 = "a"
    b2 = "aa"
    print("\n测试用例2:")
    print(f"输入: a = \"{a2}\", b = \"{b2}\"")
    print(f"输出: {repeatedStringMatch(a2, b2)}")  # 期望输出: 2
    
    # 测试用例3
    a3 = "a"
    b3 = "a"
    print("\n测试用例3:")
    print(f"输入: a = \"{a3}\", b = \"{b3}\"")
    print(f"输出: {repeatedStringMatch(a3, b3)}")  # 期望输出: 1
    
    # 测试用例4
    a4 = "abc"
    b4 = "wxyz"
    print("\n测试用例4:")
    print(f"输入: a = \"{a4}\", b = \"{b4}\"")
    print(f"输出: {repeatedStringMatch(a4, b4)}")  # 期望输出: -1
    
    # 测试用例5
    a5 = "abc"
    b5 = "cabcabca"
    print("\n测试用例5:")
    print(f"输入: a = \"{a5}\", b = \"{b5}\"")
    print(f"输出: {repeatedStringMatch(a5, b5)}")  # 期望输出: 4