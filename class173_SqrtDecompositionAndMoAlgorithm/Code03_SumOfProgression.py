# 等差数列求和问题 - 分块算法实现 (Python版本)
# 题目来源: https://www.luogu.com.cn/problem/CF1921F
# 题目来源: https://codeforces.com/problemset/problem/1921/F
# 题目大意: 给定一个长度为n的数组arr，支持查询操作：
# 查询 s d k : arr[s]作为第1项、arr[s + 1*d]作为第2项、arr[s + 2*d]作为第3项...
#             每项的值 * 项的编号，一共k项都累加起来，打印累加和
# 约束条件: 
# 1 <= n <= 10^5
# 1 <= q <= 2 * 10^5

import math
import sys

# 定义最大数组长度和块大小
MAXN = 100001
MAXB = 401

# 全局变量
t, n, q, blen = 0, 0, 0, 0
arr = [0] * MAXN
f = [[0 for _ in range(MAXN)] for _ in range(MAXB)]
g = [[0 for _ in range(MAXN)] for _ in range(MAXB)]

def query(s, d, k):
    """
    查询操作
    arr[s]作为第1项、arr[s + 1*d]作为第2项、arr[s + 2*d]作为第3项...
    每项的值 * 项的编号，一共k项都累加起来，返回累加和
    参数:
        s: 起始位置
        d: 公差
        k: 项数
    返回:
        加权和
    """
    ans = 0
    
    # 如果d小于等于块大小，则使用预处理的结果
    if d <= blen:
        # g[d][s] 是从位置s开始的加权前缀和
        ans = g[d][s]
        
        # 如果s + d * k没有超出数组范围，则需要减去后面的部分
        if s + d * k <= n:
            # 减去从s + d * k开始的部分
            ans = ans - g[d][s + d * k] - f[d][s + d * k] * k
    else:
        # 否则暴力计算（适用于d较大的情况）
        for i in range(1, k + 1):
            ans += arr[s + (i - 1) * d] * i
    return ans

def prepare():
    """
    预处理函数
    预处理f和g数组
    """
    global blen
    # 计算块大小，通常选择sqrt(n)
    blen = int(math.sqrt(n))
    
    # 预处理f数组
    # 对于每个公差d <= sqrt(n)
    for d in range(1, blen + 1):
        # 从后往前计算前缀和
        for i in range(n, 0, -1):
            # f[d][i] = arr[i] + f[d][i+d]
            f[d][i] = arr[i] + (0 if i + d > n else f[d][i + d])
    
    # 预处理g数组
    # 对于每个公差d <= sqrt(n)
    for d in range(1, blen + 1):
        # 从后往前计算加权前缀和
        for i in range(n, 0, -1):
            # g[d][i] = arr[i] + g[d][i+d] + f[d][i+d]
            g[d][i] = f[d][i] + (0 if i + d > n else g[d][i + d])

def main():
    global t, n, q
    # 读取测试用例数t
    t = int(sys.stdin.readline())
    
    # 处理每个测试用例
    for _ in range(t):
        # 读取数组长度n和查询次数q
        n, q = map(int, sys.stdin.readline().split())
        
        # 读取初始数组
        values = list(map(int, sys.stdin.readline().split()))
        for i in range(1, n + 1):
            arr[i] = values[i - 1]
        
        # 进行预处理
        prepare()
        
        # 处理q次查询
        for _ in range(q):
            s, d, k = map(int, sys.stdin.readline().split())
            print(query(s, d, k))

if __name__ == "__main__":
    main()