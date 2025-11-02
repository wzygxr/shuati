# 最少划分问题 - 分块算法优化 (Python版本)
# 题目来源: https://www.luogu.com.cn/problem/CF786C
# 题目来源: https://codeforces.com/problemset/problem/786/C
# 题目大意: 给定一个长度为n的数组arr，考虑如下问题的解
# 数组arr划分成若干段子数组，保证每段不同数字的种类 <= k，返回至少划分成几段
# 打印k = 1, 2, 3..n时，所有的答案
# 约束条件: 1 <= arr[i] <= n <= 10^5

import math
import sys

# 定义最大数组长度
MAXN = 100001

# 全局变量
n, blen = 0, 0

# arr: 原始数组
arr = [0] * MAXN

# vis: 记录数字是否出现过
vis = [False] * MAXN

# ans: 存储每个k对应的答案
ans = [0] * MAXN

def query(limit):
    """
    查询当限制为limit时，最少需要划分成几段
    参数:
        limit: 每段不同数字种类的上限
    返回:
        最少段数
    """
    kind, cnt, start = 0, 0, 1
    
    # 遍历数组
    for i in range(1, n + 1):
        # 如果当前数字没有出现过
        if not vis[arr[i]]:
            kind += 1  # 不同数字种类数+1
            
            # 如果超过了限制
            if kind > limit:
                cnt += 1  # 段数+1
                
                # 清除之前段的标记
                for j in range(start, i):
                    vis[arr[j]] = False
                
                # 更新新段的起始位置
                start = i
                kind = 1  # 重置种类数为1
            
            # 标记当前数字已出现
            vis[arr[i]] = True
    
    # 处理最后一段
    if kind > 0:
        cnt += 1
        # 清除最后一段的标记
        for j in range(start, n + 1):
            vis[arr[j]] = False
    
    return cnt

def jump(l, r, curAns):
    """
    跳跃函数，用于优化计算
    参数:
        l: 左边界
        r: 右边界
        curAns: 当前答案
    返回:
        下一个需要计算的位置
    """
    find = l
    while l <= r:
        mid = (l + r) >> 1
        check = query(mid)
        
        if check < curAns:
            r = mid - 1
        elif check > curAns:
            l = mid + 1
        else:
            find = mid
            l = mid + 1
    return find + 1

def compute():
    """
    计算所有答案
    """
    # 对于k <= sqrt(n)的情况，直接计算
    for i in range(1, blen + 1):
        ans[i] = query(i)
    
    # 对于k > sqrt(n)的情况，使用跳跃优化
    i = blen + 1
    while i <= n:
        ans[i] = query(i)
        i = jump(i, n, ans[i])

def prepare():
    """
    预处理函数
    """
    global blen
    # 计算块大小，选择sqrt(n * log2(n))以优化性能
    log2n = 0
    while (1 << log2n) <= (n >> 1):
        log2n += 1
    blen = max(1, int(math.sqrt(n * log2n)))
    
    # 初始化答案数组为-1，表示未计算
    for i in range(1, n + 1):
        ans[i] = -1

def main():
    global n
    # 读取数组长度n
    n = int(sys.stdin.readline())
    
    # 读取初始数组
    values = list(map(int, sys.stdin.readline().split()))
    for i in range(1, n + 1):
        arr[i] = values[i - 1]
    
    # 进行预处理
    prepare()
    
    # 计算所有答案
    compute()
    
    # 输出所有答案
    for i in range(1, n + 1):
        # 如果答案未计算，则继承前一个答案
        if ans[i] == -1:
            ans[i] = ans[i - 1]
        print(ans[i], end=' ')
    print()

if __name__ == "__main__":
    main()