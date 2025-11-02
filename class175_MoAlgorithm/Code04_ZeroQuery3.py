# -*- coding: utf-8 -*-

# 累加和为0的最长子数组，Python版
# 题目来源：SPOJ ZQUERY - Zero Query
# 题目链接：https://www.spoj.com/problems/ZQUERY/
# 题目链接：https://www.luogu.com.cn/problem/SP20644
# 题目大意：
# 给定一个长度为n的数组arr，其中只有1和-1两种值
# 一共有m条查询，格式 l r : 打印arr[l..r]范围上，累加和为0的最长子数组长度
# 1 <= n、m <= 5 * 10^4
# 
# 解题思路：
# 这是一个将问题转化为经典模型的莫队应用
# 核心思想：
# 1. 子数组和为0等价于两个位置的前缀和相等
# 2. 因此问题转化为：在给定区间内，找到相等前缀和的最大距离
# 3. 这就变成了和Code03_SameNumberMaxDist1相同的问题
# 
# 算法要点：
# 1. 使用回滚莫队算法解决此问题
# 2. 首先将原数组转换为前缀和数组
# 3. 将查询范围从[l,r]转换为对应的前缀和范围
# 4. 对查询进行特殊排序：按照左端点所在的块编号排序，如果左端点在同一块内，则按照右端点位置排序
# 5. 维护两个数组：
#    - first[x]表示数字x首次出现的位置
#    - mostRight[x]表示数字x最右出现的位置
# 6. 对于同一块内的查询，使用暴力方法处理
# 7. 对于跨块的查询，通过扩展右边界和左边界来维护答案，然后通过回滚操作恢复状态
#
# 时间复杂度：O((n+m)*sqrt(n))
# 空间复杂度：O(n)
# 
# 相关题目：
# 1. SPOJ ZQUERY Zero Query - https://www.spoj.com/problems/ZQUERY/
# 2. 洛谷 SP20644 ZQUERY - https://www.luogu.com.cn/problem/SP20644
# 3. 洛谷 P5906 相同数最远距离 - https://www.luogu.com.cn/problem/P5906
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

# 常量定义
MAXN = 50002
MAXB = 301

# 全局变量
n, m = 0, 0
arr = [0] * MAXN
query = [[0, 0, 0] for _ in range(MAXN)]
sorted_arr = [0] * MAXN
cntv = 0

blen, bnum = 0, 0
bi = [0] * MAXN
br = [0] * MAXB

# first[x]表示数字x首次出现的位置
first = [0] * MAXN
# mostRight[x]表示数字x最右出现的位置
mostRight = [0] * MAXN
# 当前窗口内相等前缀和的最大距离
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


# 暴力计算[l,r]范围内和为0的最长子数组长度
def force(l, r):
    ret = 0
    # 遍历区间内所有前缀和
    for i in range(l, r + 1):
        # 如果是该前缀和值第一次出现，记录位置
        if first[arr[i]] == 0:
            first[arr[i]] = i
        else:
            # 否则计算与第一次出现位置的距离，并更新最大距离
            ret = max(ret, i - first[arr[i]])
    
    # 清除临时记录
    for i in range(l, r + 1):
        first[arr[i]] = 0
    
    return ret


# 向右扩展窗口时添加位置idx的元素（前缀和）
def addRight(idx):
    global maxDist
    num = arr[idx]
    mostRight[num] = idx  # 更新该前缀和值最右出现位置
    if first[num] == 0:
        first[num] = idx  # 如果是第一次出现，记录首次位置
    # 更新最大距离：当前前缀和值与首次出现位置的距离
    maxDist = max(maxDist, idx - first[num])


# 向左扩展窗口时添加位置idx的元素（前缀和）
def addLeft(idx):
    global maxDist
    num = arr[idx]
    if mostRight[num] == 0:
        mostRight[num] = idx  # 如果该前缀和值在右扩阶段未出现，记录位置
    else:
        # 否则计算与右扩阶段最右位置的距离，并更新最大距离
        maxDist = max(maxDist, mostRight[num] - idx)


# 从左边界删除元素
def delLeft(idx):
    num = arr[idx]
    # 如果删除的是该前缀和值的最右位置，则清除记录
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
    # 生成前缀和数组，下标从1开始，补充一个前缀长度为0的前缀和
    for i in range(1, n + 1):
        arr[i] += arr[i - 1]
    for i in range(n, -1, -1):
        arr[i + 1] = arr[i]
    n += 1
    
    # 原来查询范围 l..r，对应前缀和查询范围 l-1..r
    # 现在前缀和平移了，所以对应前缀查询范围 l..r+1
    for i in range(1, m + 1):
        query[i][1] += 1
    
    # 复制前缀和数组用于离散化
    for i in range(1, n + 1):
        sorted_arr[i] = arr[i]
    
    # 排序去重，实现离散化
    sorted_arr[1:n+1] = sorted(sorted_arr[1:n+1])
    cntv = 1
    for i in range(2, n + 1):
        if sorted_arr[cntv] != sorted_arr[i]:
            cntv += 1
            sorted_arr[cntv] = sorted_arr[i]
    
    # 将前缀和数组元素替换为离散化后的值
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
    line = sys.stdin.readline().split()
    n, m = int(line[0]), int(line[1])
    nums = list(map(int, sys.stdin.readline().split()))
    for i in range(1, n + 1):
        arr[i] = nums[i - 1]
    
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