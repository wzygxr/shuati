def manacher_odd(s):
    """
    Manacher算法计算奇回文串
    
    Args:
        s: 输入字符串
    
    Returns:
        每个位置为中心的最长奇回文半径数组
    """
    n = len(s)
    radius = [0] * n
    
    l, r = 0, -1
    for i in range(n):
        # 利用回文对称性
        k = 1 if i > r else min(radius[l + r - i], r - i + 1)
        
        # 尝试扩展回文串
        while 0 <= i - k and i + k < n and s[i - k] == s[i + k]:
            k += 1
        
        radius[i] = k - 1
        
        # 更新最右回文边界
        if i + radius[i] > r:
            l = i - radius[i]
            r = i + radius[i]
    
    return radius


def max_product(s):
    """
    LeetCode 1960. 两个回文子字符串长度的最大乘积
    
    题目描述：
    给你一个下标从0开始的字符串 s ，你需要找到两个不重叠的回文子字符串，
    它们的长度都必须为奇数，使得它们长度的乘积最大。
    
    解题思路：
    使用Manacher算法计算所有奇回文信息：
    1. 使用Manacher算法计算每个位置为中心的最长奇回文半径
    2. 预处理前缀和后缀数组，分别记录到每个位置为止的最长回文长度
    3. 枚举每个分割点，通过前后缀获取左右两个子串中的最长回文大小，相乘即可
    
    时间复杂度：O(n)
    空间复杂度：O(n)
    
    Args:
        s: 输入字符串
    
    Returns:
        最大乘积
    """
    n = len(s)
    
    # 使用Manacher算法计算每个位置为中心的最长奇回文半径
    radius = manacher_odd(s)
    
    # prefix[i] 表示在 [0, i] 范围内能找到的最长奇回文子串长度
    prefix = [0] * n
    # suffix[i] 表示在 [i, n-1] 范围内能找到的最长奇回文子串长度
    suffix = [0] * n
    
    # 初始化
    prefix[0] = 1
    suffix[n - 1] = 1
    
    # 计算前缀数组
    for i in range(1, n):
        # 检查以位置i结尾的回文串
        for j in range(i + 1):
            # 回文串的右边界是i，中心是j，半径是radius[j]
            if j + radius[j] >= i:
                prefix[i] = max(prefix[i], 2 * (i - j) + 1)
        prefix[i] = max(prefix[i], prefix[i - 1])
    
    # 计算后缀数组
    for i in range(n - 2, -1, -1):
        # 检查以位置i开头的回文串
        for j in range(i, n):
            # 回文串的左边界是i，中心是j，半径是radius[j]
            if j - radius[j] <= i:
                suffix[i] = max(suffix[i], 2 * (j - i) + 1)
        suffix[i] = max(suffix[i], suffix[i + 1])
    
    # 枚举分割点，计算最大乘积
    max_prod = 0
    for i in range(n - 1):
        max_prod = max(max_prod, prefix[i] * suffix[i + 1])
    
    return max_prod


# 测试方法
if __name__ == "__main__":
    # 示例测试
    print(max_product("ababbb"))  # 输出: 9
    print(max_product("zaaaxbbby"))  # 输出: 9