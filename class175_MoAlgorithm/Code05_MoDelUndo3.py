# -*- coding: utf-8 -*-

# 只删回滚莫队入门题，Python版
# 题目来源：洛谷 P4137 Rmq Problem / mex
# 题目链接：https://www.luogu.com.cn/problem/P4137
# 题目大意：
# 本题最优解为主席树，讲解158，题目2，已经讲述
# 给定一个长度为n的数组arr，一共有m条查询，格式如下
# 查询 l r : 打印arr[l..r]内没有出现过的最小自然数，注意0是自然数
# 0 <= n、m、arr[i] <= 2 * 10^5
# 
# 解题思路：
# 只删回滚莫队是另一种回滚莫队的变体
# 与只增回滚莫队相反，只删回滚莫队的特点是：
# 1. 可以很容易地从区间中删除元素
# 2. 添加元素的操作比较困难或者代价较高
# 3. 通过"回滚"操作可以恢复到之前的状态
# 在这个问题中，我们需要维护区间内未出现的最小自然数（mex），删除元素时容易更新答案，但添加元素时较难
# 
# 算法要点：
# 1. 使用只删回滚莫队算法解决此问题
# 2. 对查询进行特殊排序：按照左端点所在的块编号排序，如果左端点在同一块内，则按照右端点位置逆序排序
# 3. 初始时认为整个数组都在窗口中，统计所有数字的出现次数
# 4. 通过收缩和扩展窗口边界来维护答案，然后通过回滚操作恢复状态
#
# 时间复杂度：O((n+m)*sqrt(n))
# 空间复杂度：O(n)
# 
# 相关题目：
# 1. 洛谷 P4137 Rmq Problem / mex - https://www.luogu.com.cn/problem/P4137
# 2. HDU 3339 In Action - https://acm.hdu.edu.cn/showproblem.php?pid=3339 (mex相关)
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
MAXN = 200001
MAXB = 501

# 全局变量
n, m = 0, 0
arr = [0] * MAXN
query = [[0, 0, 0] for _ in range(MAXN)]

blen, bnum = 0, 0
bi = [0] * MAXN
bl = [0] * MAXB

# 记录每个数字在当前窗口中的出现次数
cnt = [0] * MAXN
# 当前窗口内未出现的最小自然数（mex）
mex = 0
ans = [0] * MAXN


# 只删回滚莫队经典排序
# 排序规则：
# 1. 按照左端点所在的块编号排序
# 2. 如果左端点在同一块内，则按照右端点位置逆序排序
def QueryCmp(a, b):
    if bi[a[0]] != bi[b[0]]:
        return bi[a[0]] - bi[b[0]]
    return b[1] - a[1]  # 右端点逆序排序


# 删除数字num，更新出现次数和mex值
def del_num(num):
    global mex
    cnt[num] -= 1
    if cnt[num] == 0:  # 如果该数字的出现次数变为0
        mex = min(mex, num)  # 更新mex为更小的值


# 添加数字num（在回滚时使用）
def add(num):
    cnt[num] += 1


# 核心计算函数
def compute():
    global mex
    # 初始时，认为整个数组都在窗口中，统计所有数字的出现次数
    for i in range(1, n + 1):
        cnt[arr[i]] += 1
    
    # 计算初始的mex值
    mex = 0
    while cnt[mex] != 0:
        mex += 1
    
    # 当前窗口的左右边界
    winl, winr = 1, n
    
    # 按块处理查询
    block = 1
    qi = 1
    while block <= bnum and qi <= m:
        # 收缩左边界到当前块的左边界
        while winl < bl[block]:
            del_num(arr[winl])
            winl += 1
        
        # 保存当前状态
        beforeJob = mex
        
        # 处理属于当前块的所有查询
        while qi <= m and bi[query[qi][0]] == block:
            jobl = query[qi][0]  # 查询左边界
            jobr = query[qi][1]  # 查询右边界
            id = query[qi][2]    # 查询编号
            
            # 收缩右边界到jobr
            while winr > jobr:
                del_num(arr[winr])
                winr -= 1
            
            # 保存当前mex值
            backup = mex
            
            # 扩展左边界到jobl
            while winl < jobl:
                del_num(arr[winl])
                winl += 1
            
            # 记录答案
            ans[id] = mex
            
            # 恢复状态，保留右边界收缩的结果
            mex = backup
            
            # 收缩左边界回到当前块的左边界
            while winl > bl[block]:
                winl -= 1
                add(arr[winl])
            
            qi += 1
        
        # 扩展右边界回到数组末尾
        while winr < n:
            winr += 1
            add(arr[winr])
        
        # 恢复到块开始时的状态
        mex = beforeJob
        block += 1


# 预处理函数
def prepare():
    global n, m, blen, bnum
    # 分块处理
    blen = int(math.sqrt(n))
    bnum = (n + blen - 1) // blen
    
    # 计算每个位置属于哪个块
    for i in range(1, n + 1):
        bi[i] = (i - 1) // blen + 1
    
    # 计算每个块的左边界
    for i in range(1, bnum + 1):
        bl[i] = (i - 1) * blen + 1
    
    # 对查询进行排序
    query[1:m+1] = sorted(query[1:m+1], key=lambda x: (bi[x[0]], -x[1]))


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