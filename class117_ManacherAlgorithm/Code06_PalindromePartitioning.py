"""
分割回文串
给你一个字符串 s，请你将 s 分割成一些子串，使每个子串都是回文串。
返回符合要求的 最少分割次数
测试链接 : https://leetcode.cn/problems/palindrome-partitioning-ii/
"""

def minCut(s: str) -> int:
    """
    使用Manacher算法优化的解法
    
    算法思路：
    1. 首先使用Manacher算法计算所有位置的回文半径
    2. 根据回文半径信息构建预处理数组，记录每个区间是否为回文
    3. 使用动态规划计算最少分割次数
    
    时间复杂度：O(n^2)，其中n为字符串长度
    空间复杂度：O(n^2)，用于存储回文信息和DP数组
    
    相比暴力解法的优势：
    1. 使用Manacher算法预处理回文信息，避免重复计算
    2. DP过程中直接查表判断回文，提高效率
    """
    if not s or len(s) < 2:
        return 0
    
    # 使用Manacher算法获取回文信息
    p = manacher(s)
    
    n = len(s)
    # isPalindrome[i][j] 表示 s[i..j] 是否为回文
    isPalindrome = [[False] * n for _ in range(n)]
    
    # 根据Manacher算法的结果填充回文判断表
    # 遍历扩展字符串中的每个位置
    for i in range(2 * n + 1):
        # 获取以位置i为中心的回文半径
        radius = p[i]
        # 计算在原字符串中的实际中心位置和半径
        center = i // 2
        actualRadius = (radius - 1) // 2
        
        # 根据中心位置的奇偶性分别处理
        if i % 2 == 0:
            # 偶数位置对应原字符串字符之间的位置
            # 处理偶数长度回文
            for r in range(actualRadius + 1):
                left = center - r
                right = center + r - 1
                if left >= 0 and right < n:
                    isPalindrome[left][right] = True
        else:
            # 奇数位置对应原字符串中的字符位置
            # 处理奇数长度回文
            for r in range(actualRadius + 1):
                left = center - r
                right = center + r
                if left >= 0 and right < n:
                    isPalindrome[left][right] = True
    
    # 动态规划计算最少分割次数
    # dp[i] 表示 s[0..i-1] 的最少分割次数
    dp = [0] * (n + 1)
    for i in range(1, n + 1):
        # 初始化为最多分割次数（每个字符分割）
        dp[i] = i - 1
        # 尝试所有可能的最后一条分割线
        for j in range(i):
            # 如果 s[j..i-1] 是回文，则可以在此处分割
            if isPalindrome[j][i - 1]:
                if j == 0:
                    # 如果从头开始就是回文，不需要分割
                    dp[i] = 0
                else:
                    # 否则分割次数为前面部分的分割次数+1
                    dp[i] = min(dp[i], dp[j] + 1)
    
    return dp[n]


def manacher(s: str) -> list:
    """
    Manacher算法主函数，用于计算字符串中每个位置的回文半径
    
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
        每个位置的回文半径数组
    """
    # 预处理字符串
    processed = '#'.join('^{}$'.format(s))
    n = len(processed)
    p = [0] * n
    
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
    
    return p


# 测试代码
if __name__ == "__main__":
    s = input().strip()
    print(minCut(s))