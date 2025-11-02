# -*- coding: utf-8 -*-

# 秃子酋长，Python版
# 题目来源：洛谷 P8078 [COCI2010-2011#6] KRUZNICA
# 题目链接：https://www.luogu.com.cn/problem/P8078
# 题目大意：
# 给定一个长度为n的数组arr，一共有m条查询，格式如下
# 查询 l r : 打印arr[l..r]范围上，如果所有数排序后，
#            相邻的数在原序列中的位置的差的绝对值之和
# 注意arr很特殊，1~n这些数字在arr中都只出现1次
# 1 <= n、m <= 5 * 10^5
# 
# 解题思路：
# 这是一道比较复杂的莫队题目，需要维护相邻元素在原序列中位置差的绝对值之和
# 解决思路：
# 1. 将数组中的值看作下标，将下标看作值，建立pos数组，pos[i]表示数字i在原数组中的位置
# 2. 维护一个链表结构，last[i]表示数字i在当前窗口排序后前一个相邻数字，next[i]表示后一个相邻数字
# 3. 当添加或删除数字时，维护链表结构并更新答案
# 
# 算法要点：
# 1. 使用只删回滚莫队算法解决此问题
# 2. 对查询进行特殊排序：按照左端点所在的块编号排序，如果左端点在同一块内，则按照右端点位置逆序排序
# 3. 维护链表结构来表示当前窗口中数字的排序关系
# 4. 通过收缩和扩展窗口边界来维护答案，然后通过回滚操作恢复状态
#
# 时间复杂度：O((n+m)*sqrt(n))
# 空间复杂度：O(n)
# 
# 相关题目：
# 1. 洛谷 P8078 [COCI2010-2011#6] KRUZNICA - https://www.luogu.com.cn/problem/P8078
# 2. COCI 2010-2011 Contest #6 KRUZNICA - https://oj.uz/problem/view/COCI11_kruznica
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
MAXN = 500001

# 全局变量
n, m = 0, 0
arr = [0] * MAXN
query = [[0, 0, 0] for _ in range(MAXN)]
# pos[i]表示数字i在原数组中的位置
pos = [0] * MAXN

blen, bnum = 0, 0
bi = [0] * MAXN
bl = [0] * MAXN

# last[i]表示数字i在当前窗口排序后前一个相邻数字
# next[i]表示数字i在当前窗口排序后后一个相邻数字
last_arr = [0] * (MAXN + 1)
next_arr = [0] * (MAXN + 1)
# 当前窗口的答案
sum_val = 0
ans = [0] * MAXN


# 查询排序比较函数
def QueryCmp(a, b):
    if bi[a[0]] != bi[b[0]]:
        return bi[a[0]] - bi[b[0]]
    return b[1] - a[1]  # 右端点逆序排序


# 删除数字num，维护链表结构并更新答案
def del_num(num):
    global sum_val
    less_val = last_arr[num]  # less是前一个数字
    more_val = next_arr[num]  # more是后一个数字
    
    # 从答案中减去num与前一个数字在原数组中的位置差
    if less_val != 0:
        sum_val -= abs(pos[num] - pos[less_val])
    
    # 从答案中减去num与后一个数字在原数组中的位置差
    if more_val != n + 1:
        sum_val -= abs(pos[more_val] - pos[num])
    
    # 加上前一个数字与后一个数字在原数组中的位置差
    if less_val != 0 and more_val != n + 1:
        sum_val += abs(pos[more_val] - pos[less_val])
    
    # 更新链表结构
    next_arr[less_val] = more_val
    last_arr[more_val] = less_val


# 添加数字num（在回滚时使用）
# 加数字的顺序，就是删数字顺序的回滚，才能这么方便的更新
def add(num):
    next_arr[last_arr[num]] = num
    last_arr[next_arr[num]] = num


# 核心计算函数
def compute():
    global sum_val
    # 初始化链表结构
    # 对于1到n的每个数字，设置其前一个和后一个数字
    for v in range(1, n + 1):
        last_arr[v] = v - 1
        next_arr[v] = v + 1
    next_arr[0] = 1           # 0的后继是1
    last_arr[n + 1] = n       # n+1的前驱是n
    
    # 初始时认为整个数组都在窗口中，计算答案
    for v in range(2, n + 1):
        sum_val += abs(pos[v] - pos[v - 1])
    
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
        beforeJob = sum_val
        
        # 处理属于当前块的所有查询
        while qi <= m and bi[query[qi][0]] == block:
            jobl = query[qi][0]  # 查询左边界
            jobr = query[qi][1]  # 查询右边界
            id = query[qi][2]    # 查询编号
            
            # 收缩右边界到jobr
            while winr > jobr:
                del_num(arr[winr])
                winr -= 1
            
            # 保存当前答案
            backup = sum_val
            
            # 扩展左边界到jobl
            while winl < jobl:
                del_num(arr[winl])
                winl += 1
            
            # 记录答案
            ans[id] = sum_val
            
            # 恢复状态，保留右边界收缩的结果
            sum_val = backup
            
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
        sum_val = beforeJob
        block += 1


# 预处理函数
def prepare():
    global n, m, blen, bnum
    # 建立pos数组，pos[i]表示数字i在原数组中的位置
    for i in range(1, n + 1):
        pos[arr[i]] = i
    
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