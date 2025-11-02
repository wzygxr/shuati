# LeetCode 1092. 最短公共超序列
# 给你两个字符串 str1 和 str2，返回同时以 str1 和 str2 作为子序列的最短字符串。
# 如果答案不止一个，则可以返回满足条件的任意一个答案。
# 测试链接 : https://leetcode.cn/problems/shortest-common-supersequence/

"""
算法详解：最短公共超序列（LeetCode 1092）

问题描述：
给你两个字符串 str1 和 str2，返回同时以 str1 和 str2 作为子序列的最短字符串。
超序列是指包含给定序列为子序列的序列。

算法思路：
1. 首先计算str1和str2的最长公共子序列(LCS)
2. 通过LCS构造最短公共超序列
3. 使用双指针技术，分别指向str1和str2的开头
4. 遍历LCS中的所有字符，对于每个字符：
   - 将str1中在该字符之前的部分添加到结果中
   - 将str2中在该字符之前的部分添加到结果中
   - 添加该字符本身
5. 最后将str1和str2剩余的部分添加到结果中

时间复杂度分析：
1. 计算LCS：O(m*n)
2. 构造超序列：O(m+n)
3. 总体时间复杂度：O(m*n)

空间复杂度分析：
1. dp数组：O(m*n)
2. LCS字符串：O(min(m,n))
3. 结果字符串：O(m+n)
4. 总体空间复杂度：O(m*n)

工程化考量：
1. 异常处理：检查输入是否为空
2. 边界处理：正确处理空字符串的情况
3. 内存优化：可复用部分计算结果

极端场景验证：
1. 输入字符串长度达到边界情况
2. 两个字符串完全相同的情况
3. 两个字符串完全不同的情况
4. 一个字符串为空的情况
5. 两个字符串都为空的情况
"""

def shortestCommonSupersequence(str1, str2):
    """
    计算两个字符串的最短公共超序列
    
    Args:
        str1 (str): 第一个字符串
        str2 (str): 第二个字符串
    
    Returns:
        str: 最短公共超序列
    """
    # 异常处理：检查输入是否为空
    if not str1:
        return str2
    if not str2:
        return str1
    
    m, n = len(str1), len(str2)
    
    # 计算LCS长度和构造dp表
    dp = [[0] * (n + 1) for _ in range(m + 1)]
    
    # 填充dp表
    for i in range(1, m + 1):
        for j in range(1, n + 1):
            if str1[i - 1] == str2[j - 1]:
                dp[i][j] = dp[i - 1][j - 1] + 1
            else:
                dp[i][j] = max(dp[i - 1][j], dp[i][j - 1])
    
    # 通过dp表回溯构造LCS
    lcs = []
    i, j = m, n
    while i > 0 and j > 0:
        if str1[i - 1] == str2[j - 1]:
            lcs.append(str1[i - 1])
            i -= 1
            j -= 1
        elif dp[i - 1][j] > dp[i][j - 1]:
            i -= 1
        else:
            j -= 1
    
    # 反转LCS列表，因为我们是从后往前构造的
    lcs.reverse()
    lcs_str = ''.join(lcs)
    
    # 通过LCS构造最短公共超序列
    result = []
    p1, p2 = 0, 0
    
    # 遍历LCS中的每个字符
    for ch in lcs_str:
        # 将str1中在该字符之前的部分添加到结果中
        while p1 < len(str1) and str1[p1] != ch:
            result.append(str1[p1])
            p1 += 1
        
        # 将str2中在该字符之前的部分添加到结果中
        while p2 < len(str2) and str2[p2] != ch:
            result.append(str2[p2])
            p2 += 1
        
        # 添加该字符本身
        result.append(ch)
        p1 += 1
        p2 += 1
    
    # 添加str1和str2剩余的部分
    while p1 < len(str1):
        result.append(str1[p1])
        p1 += 1
    
    while p2 < len(str2):
        result.append(str2[p2])
        p2 += 1
    
    return ''.join(result)

# 测试方法
if __name__ == "__main__":
    # 测试用例1
    str1 = "abac"
    str2 = "cab"
    print(f"Test 1: {shortestCommonSupersequence(str1, str2)}")  
    # 期望输出: "cabac"
    
    # 测试用例2
    str1 = "aaaaaaaa"
    str2 = "aaaaaaaa"
    print(f"Test 2: {shortestCommonSupersequence(str1, str2)}")  
    # 期望输出: "aaaaaaaa"
    
    # 测试用例3
    str1 = "abc"
    str2 = "def"
    print(f"Test 3: {shortestCommonSupersequence(str1, str2)}")  
    # 期望输出: "abcdef"
    
    # 测试用例4
    str1 = ""
    str2 = "abc"
    print(f"Test 4: {shortestCommonSupersequence(str1, str2)}")  
    # 期望输出: "abc"
    
    # 测试用例5
    str1 = "abc"
    str2 = ""
    print(f"Test 5: {shortestCommonSupersequence(str1, str2)}")  
    # 期望输出: "abc"