# 初始化问题 - 分块算法实现 (Python版本)
# 题目来源: https://www.luogu.com.cn/problem/P5309
# 题目大意: 给定一个长度为n的数组arr，支持两种操作：
# 操作 1 x y z : 从arr[y]开始，下标每次+x，所有相应位置的数都+z，题目保证 y <= x
# 操作 2 x y   : 打印arr[x..y]的累加和，答案对1000000007取余
# 约束条件: 1 <= n、m <= 2 * 10^5

import math
import sys

# 定义最大数组长度和块大小
MAXN = 200001
MAXB = 501
MOD = 1000000007

# 全局变量
n, m = 0, 0

# pre[x][y]: 对于步长为x，起始位置为y的序列，前缀和的增量
pre = [[0 for _ in range(MAXB)] for _ in range(MAXB)]

# suf[x][y]: 对于步长为x，起始位置为y的序列，后缀和的增量
suf = [[0 for _ in range(MAXB)] for _ in range(MAXB)]

# arr: 原始数组
arr = [0] * MAXN

# sum: 每个块的和
sum_arr = [0] * MAXB

# blen: 块大小, bnum: 块数量
blen, bnum = 0, 0

# bi[i]: 位置i属于哪个块
bi = [0] * MAXN

# bl[b]: 块b的左边界
bl = [0] * MAXB

# br[b]: 块b的右边界
br = [0] * MAXB

def add(x, y, z):
    """
    操作 1 x y z
    从arr[y]开始，下标每次+x，所有相应位置的数都+z
    参数:
        x: 步长
        y: 起始位置
        z: 增量
    """
    # 如果步长x小于等于块大小，则更新pre和suf数组
    if x <= blen:
        # 更新前缀和增量
        for i in range(y, x + 1):
            pre[x][i] += z
        # 更新后缀和增量
        for i in range(y, 0, -1):
            suf[x][i] += z
    else:
        # 否则直接更新原数组和块和
        i = y
        while i <= n:
            arr[i] += z
            sum_arr[bi[i]] += z
            i += x

def querySum(l, r):
    """
    查询区间和
    参数:
        l: 左边界
        r: 右边界
    返回:
        区间和
    """
    # 获取左右边界所在的块
    lb, rb = bi[l], bi[r]
    ans = 0
    
    # 如果左右边界在同一个块内
    if lb == rb:
        # 直接遍历计算
        for i in range(l, r + 1):
            ans += arr[i]
    else:
        # 否则分三部分计算
        # 1. 左边不完整块
        for i in range(l, br[lb] + 1):
            ans += arr[i]
        # 2. 右边不完整块
        for i in range(bl[rb], r + 1):
            ans += arr[i]
        # 3. 中间完整块
        for b in range(lb + 1, rb):
            ans += sum_arr[b]
    return ans

def query(l, r):
    """
    操作 2 x y
    查询arr[x..y]的累加和
    参数:
        l: 左边界
        r: 右边界
    返回:
        区间和对MOD取余
    """
    ans = querySum(l, r)
    
    # 对于所有步长x <= sqrt(n)，累加其对区间和的贡献
    for x in range(1, blen + 1):
        # 计算起始位置和结束位置在步长为x时对应的编号
        lth = (l - 1) // x + 1
        rth = (r - 1) // x + 1
        
        # 计算中间完整段的数量
        num = rth - lth - 1
        
        # 如果起始和结束位置在同一段
        if lth == rth:
            # 只需要计算起始段的贡献
            ans = ans + pre[x][(r - 1) % x + 1] - pre[x][(l - 1) % x]
        else:
            # 否则需要计算三部分的贡献
            # 1. 起始段的后缀贡献
            # 2. 中间完整段的贡献
            # 3. 结束段的前缀贡献
            ans = ans + suf[x][(l - 1) % x + 1] + pre[x][x] * num + pre[x][(r - 1) % x + 1]
    return ans % MOD

def prepare():
    """
    预处理函数
    初始化分块信息
    """
    global blen, bnum
    # 计算块大小，通常选择sqrt(n)
    blen = int(math.sqrt(n))
    
    # 计算块数量
    bnum = (n + blen - 1) // blen
    
    # 计算每个位置属于哪个块
    for i in range(1, n + 1):
        bi[i] = (i - 1) // blen + 1
    
    # 计算每个块的边界
    for b in range(1, bnum + 1):
        # 块的左边界
        bl[b] = (b - 1) * blen + 1
        # 块的右边界
        br[b] = min(b * blen, n)
        
        # 计算块的初始和
        for i in range(bl[b], br[b] + 1):
            sum_arr[b] += arr[i]

def main():
    global n, m
    # 读取数组长度n和操作次数m
    n, m = map(int, sys.stdin.readline().split())
    
    # 读取初始数组
    values = list(map(int, sys.stdin.readline().split()))
    for i in range(1, n + 1):
        arr[i] = values[i - 1]
    
    # 进行预处理
    prepare()
    
    # 处理m次操作
    for _ in range(m):
        line = list(map(int, sys.stdin.readline().split()))
        op, x, y = line[0], line[1], line[2]
        if op == 1:
            z = line[3]
            add(x, y, z)
        else:
            print(query(x, y))

if __name__ == "__main__":
    main()