# -*- coding: utf-8 -*-

# 相同数的最远距离，Python版
# 题目来源：洛谷 P5906 【模板】回滚莫队&不删除莫队
# 题目链接：https://www.luogu.com.cn/problem/P5906
# 题目大意：
# 给定一个长度为n的数组arr，一共有m条查询，格式如下
# 查询 l r : 打印arr[l..r]范围上，相同的数的最远间隔距离
#            序列中两个元素的间隔距离指的是两个元素下标差的绝对值
# 1 <= n、m <= 2 * 10^5
# 1 <= arr[i] <= 2 * 10^9
# 
# 解题思路：
# 这是一个经典的回滚莫队问题
# 回滚莫队适用于这样的场景：
# 1. 可以很容易地向区间添加元素
# 2. 删除元素的操作比较困难或者代价较高
# 3. 通过"回滚"操作可以恢复到之前的状态
# 在这个问题中，我们需要维护相同数字的最大距离，添加操作容易，但删除操作需要复杂的维护
# 
# 算法要点：
# 1. 使用回滚莫队算法解决此问题
# 2. 对查询进行特殊排序：按照左端点所在的块编号排序，如果左端点在同一块内，则按照右端点位置排序
# 3. 维护两个数组：
#    - first[x]表示只考虑窗口右扩阶段，数字x首次出现的位置
#    - mostRight[x]表示窗口中数字x最右出现的位置
# 4. 对于同一块内的查询，使用暴力方法处理
# 5. 对于跨块的查询，通过扩展右边界和左边界来维护答案，然后通过回滚操作恢复状态
#
# 时间复杂度：O((n+m)*sqrt(n))
# 空间复杂度：O(n)
# 
# 相关题目：
# 1. 洛谷 P5906 相同数最远距离 - https://www.luogu.com.cn/problem/P5906
# 2. SPOJ ZQUERY Zero Query - https://www.spoj.com/problems/ZQUERY/
# 3. AtCoder JOISC 2014 C 历史研究 - https://www.luogu.com.cn/problem/AT_joisc2014_c
#
# 莫队算法变种题目推荐：
# 1. 普通莫队：
#    - 洛谷 P1494 小Z的袜子 - https://www.luogu.com.cn/problem/P1494
#    - SPOJ DQUERY - https://www.luogu.com.cn/problem/SP3267
#    - Codeforces 617E XOR and Favorite Number - https://codeforces.com/contest/617/problem/E
#    - 洛谷 P2709 小B的询问 - https://www.luogu.com.cn/problem/P2709
#
# 2. 带修莫队：
#    - 洛谷 P1903 数颜色 - https://www.luogu.com.cn/problem/P1903
#    - LibreOJ 2874 历史研究 - https://loj.ac/p/2874
#    - Codeforces 940F Machine Learning - https://codeforces.com/contest/940/problem/F
#
# 3. 树上莫队：
#    - SPOJ COT2 Count on a tree II - https://www.luogu.com.cn/problem/SP10707
#    - 洛谷 P4074 糖果公园 - https://www.luogu.com.cn/problem/P4074
#
# 4. 二次离线莫队：
#    - 洛谷 P4887 第十四分块(前体) - https://www.luogu.com.cn/problem/P4887
#    - 洛谷 P5398 GCD - https://www.luogu.com.cn/problem/P5398
#
# 5. 回滚莫队：
#    - 洛谷 P5906 相同数最远距离 - https://www.luogu.com.cn/problem/P5906
#    - SPOJ ZQUERY Zero Query - https://www.spoj.com/problems/ZQUERY/
#    - AtCoder JOISC 2014 C 历史研究 - https://www.luogu.com.cn/problem/AT_joisc2014_c

import sys
import math
from bisect import bisect_right

# 常量定义
MAXN = 200001
MAXB = 501

# 全局变量
n, m = 0, 0
arr = [0] * MAXN
query = [[0, 0, 0] for _ in range(MAXN)]
sorted_arr = [0] * MAXN
cntv = 0

blen, bnum = 0, 0
bi = [0] * MAXN
br = [0] * MAXB

# first[x]表示只考虑窗口右扩阶段，数字x首次出现的位置
first = [0] * MAXN
# mostRight[x]表示窗口中数字x最右出现的位置
mostRight = [0] * MAXN
# 答案信息，相同的数的最远间隔距离
maxDist = 0

ans = [0] * MAXN


# 二分查找离散化值
def kth(num):
    left, right, ret = 1, cntv, 0
    while left <= right:
        mid = (left + right) // 2
        if sorted_arr[mid] <= num:
            ret = mid
            left = mid + 1
        else:
            right = mid - 1
    return ret


# 暴力计算[l,r]范围内相同数字的最远距离
def force(l, r):
    ret = 0
    # 遍历区间内所有元素
    for i in range(l, r + 1):
        # 如果是该数字第一次出现，记录位置
        if first[arr[i]] == 0:
            first[arr[i]] = i
        else:
            # 否则计算与第一次出现位置的距离，并更新最大距离
            ret = max(ret, i - first[arr[i]])
    
    # 清除临时记录
    for i in range(l, r + 1):
        first[arr[i]] = 0
    
    return ret


# 向右扩展窗口时添加位置idx的元素
def addRight(idx):
    global maxDist
    num = arr[idx]
    mostRight[num] = idx  # 更新该数字最右出现位置
    if first[num] == 0:
        first[num] = idx  # 如果是第一次出现，记录首次位置
    # 更新最大距离：当前元素与首次出现位置的距离
    maxDist = max(maxDist, idx - first[num])


# 向左扩展窗口时添加位置idx的元素
def addLeft(idx):
    global maxDist
    num = arr[idx]
    if mostRight[num] == 0:
        mostRight[num] = idx  # 如果该数字在右扩阶段未出现，记录位置
    else:
        # 否则计算与右扩阶段最右位置的距离，并更新最大距离
        maxDist = max(maxDist, mostRight[num] - idx)


# 从左边界删除元素
def delLeft(idx):
    num = arr[idx]
    # 如果删除的是该数字的最右位置，则清除记录
    if mostRight[num] == idx:
        mostRight[num] = 0


# 核心计算函数
def compute():
    global maxDist
    # 按块处理查询
    block = 1
    qi = 1
    while block <= bnum and qi <= m:
        # 每个块开始时重置状态
        maxDist = 0
        for i in range(1, cntv + 1):
            first[i] = 0
            mostRight[i] = 0
        
        # 当前窗口的左右边界
        winl = br[block] + 1
        winr = br[block]
        
        # 处理属于当前块的所有查询
        while qi <= m and bi[query[qi][0]] == block:
            jobl = query[qi][0]  # 查询左边界
            jobr = query[qi][1]  # 查询右边界
            id = query[qi][2]    # 查询编号
            
            # 如果查询区间完全在当前块内，使用暴力方法
            if jobr <= br[block]:
                ans[id] = force(jobl, jobr)
            else:
                # 否则使用回滚莫队算法
                # 先扩展右边界到jobr
                while winr < jobr:
                    winr += 1
                    addRight(winr)
                
                # 保存当前答案，然后扩展左边界到jobl
                backup = maxDist
                while winl > jobl:
                    winl -= 1
                    addLeft(winl)
                
                # 记录答案
                ans[id] = maxDist
                
                # 恢复状态，只保留右边界扩展的结果
                maxDist = backup
                while winl <= br[block]:
                    delLeft(winl)
                    winl += 1
            
            qi += 1
        
        block += 1


# 预处理函数
def prepare():
    global n, m, cntv, blen, bnum
    # 复制原数组用于离散化
    for i in range(1, n + 1):
        sorted_arr[i] = arr[i]
    
    # 排序去重，实现离散化
    sorted_arr[1:n+1] = sorted(sorted_arr[1:n+1])
    cntv = 1
    for i in range(2, n + 1):
        if sorted_arr[cntv] != sorted_arr[i]:
            cntv += 1
            sorted_arr[cntv] = sorted_arr[i]
    
    # 将原数组元素替换为离散化后的值
    for i in range(1, n + 1):
        arr[i] = kth(arr[i])
    
    # 分块处理
    blen = int(math.sqrt(n))
    bnum = (n + blen - 1) // blen
    
    # 计算每个位置属于哪个块
    for i in range(1, n + 1):
        bi[i] = (i - 1) // blen + 1
    
    # 计算每个块的右边界
    for i in range(1, bnum + 1):
        br[i] = min(i * blen, n)
    
    # 对查询进行排序
    query[1:m+1] = sorted(query[1:m+1], key=lambda x: (bi[x[0]], x[1]))


def main():
    global n, m
    # 读取输入
    n = int(sys.stdin.readline())
    nums = list(map(int, sys.stdin.readline().split()))
    for i in range(1, n + 1):
        arr[i] = nums[i - 1]
    
    m = int(sys.stdin.readline())
    for i in range(1, m + 1):
        l, r = map(int, sys.stdin.readline().split())
        query[i][0] = l
        query[i][1] = r
        query[i][2] = i
    
    prepare()
    compute()
    
    # 输出结果
    for i in range(1, m + 1):
        print(ans[i])


if __name__ == "__main__":
    main()