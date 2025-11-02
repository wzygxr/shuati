"""
HackerRank String Function Calculation

题目描述：
给定一个字符串t，定义函数f(S) = |S| * (S在t中出现的次数)，其中S是t的任意子串。
求所有子串S中f(S)的最大值。

解题思路：
这是一个经典的后缀数组应用问题。我们可以使用以下方法：
1. 构建字符串的后缀数组和高度数组（LCP数组）
2. 对于每个可能的子串长度，计算该长度的所有子串的出现次数
3. 使用单调栈来高效计算每个长度对应的最大出现次数

具体步骤：
1. 构建后缀数组和LCP数组
2. 对于LCP数组中的每个值，使用单调栈计算以该值为最小值的区间能贡献的最大f值
3. 同时考虑所有单个字符的情况

时间复杂度：O(N)
空间复杂度：O(N)

注意：这个问题也可以用后缀自动机解决，但后缀数组更容易理解和实现。
"""

import sys

def suffix_array(s):
    """
    构建字符串的后缀数组
    :param s: 输入字符串
    :return: 后缀数组
    """
    n = len(s)
    # 初始排序：按第一个字符排序
    suffixes = [(s[i], i) for i in range(n)]
    suffixes.sort()
    
    # 倍增算法构建后缀数组
    k = 1
    while k < n:
        # 根据前k个字符的排名对后缀进行排序
        rank = [0] * n
        for i in range(n):
            rank[suffixes[i][1]] = i
        
        # 构建新的排序键
        new_suffixes = []
        for i in range(n):
            suffix_idx = suffixes[i][1]
            next_rank = rank[suffix_idx + k] if suffix_idx + k < n else -1
            new_suffixes.append(((rank[suffix_idx], next_rank), suffix_idx))
        
        # 排序
        new_suffixes.sort()
        suffixes = [(x[0], x[1]) for x in new_suffixes]
        k *= 2
    
    return [x[1] for x in suffixes]

def lcp_array(s, suffix_array):
    """
    根据后缀数组构建LCP数组
    :param s: 输入字符串
    :param suffix_array: 后缀数组
    :return: LCP数组
    """
    n = len(s)
    rank = [0] * n
    for i in range(n):
        rank[suffix_array[i]] = i
    
    lcp = [0] * n
    h = 0
    
    for i in range(n):
        if rank[i] > 0:
            j = suffix_array[rank[i] - 1]
            while i + h < n and j + h < n and s[i + h] == s[j + h]:
                h += 1
            lcp[rank[i]] = h
            if h > 0:
                h -= 1
    
    return lcp

def solve_string_function_calculation(s):
    """
    解决String Function Calculation问题
    :param s: 输入字符串
    :return: f(S)的最大值
    """
    if not s:
        return 0
    
    n = len(s)
    
    # 特殊情况：所有字符相同
    if len(set(s)) == 1:
        # 对于n个相同字符，长度为k的子串出现次数为n-k+1
        # f(k) = k * (n-k+1)
        # 求最大值
        max_val = 0
        for k in range(1, n + 1):
            val = k * (n - k + 1)
            max_val = max(max_val, val)
        return max_val
    
    # 构建后缀数组和LCP数组
    sa = suffix_array(s)
    lcp = lcp_array(s, sa)
    
    # 使用单调栈计算最大f值
    # 在LCP数组上使用单调栈，计算每个LCP值能贡献的最大f值
    stack = []
    max_result = n  # 至少有n个单字符子串，每个出现1次，f=1*n=n
    
    # 在LCP数组前后添加0，便于处理边界情况
    extended_lcp = [0] + lcp + [0]
    
    for i in range(len(extended_lcp)):
        # 维护单调递增栈
        while stack and (i == len(extended_lcp) - 1 or extended_lcp[stack[-1]] > extended_lcp[i]):
            # 弹出栈顶元素，计算以该元素为最小值的区间的贡献
            idx = stack.pop()
            height = extended_lcp[idx]
            
            # 计算区间的左右边界
            left = stack[-1] + 1 if stack else 0
            right = i - 1
            
            # 区间长度
            width = right - left + 1
            
            # 以height为长度的子串出现次数为width
            # f = height * width
            if height > 0:
                result = height * width
                max_result = max(max_result, result)
        
        stack.append(i)
    
    return max_result

def main():
    """主函数"""
    # 读取输入
    line = sys.stdin.readline().strip()
    if not line:
        return
    
    # 求解并输出结果
    result = solve_string_function_calculation(line)
    print(result)

if __name__ == "__main__":
    main()