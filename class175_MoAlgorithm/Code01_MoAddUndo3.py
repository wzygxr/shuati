# 只增回滚莫队入门题，python版
# 题目来源：AtCoder JOISC 2014 Day1 历史研究 (歴史の研究)
# 题目链接：https://www.luogu.com.cn/problem/AT_joisc2014_c
# 题目大意：
# 给定一个大小为n的数组arr，有m条查询，格式 l r : 打印arr[l..r]范围上的最大重要度
# 如果一段范围上，数字x出现c次，那么这个数字的重要度为x * c
# 范围上的最大重要度，就是该范围上，每种数字的重要度，取最大值
# 1 <= n、m <= 10^5
# 1 <= arr[i] <= 10^9
# 
# 解题思路：
# 这是一道经典的回滚莫队（只增回滚莫队）题目
# 回滚莫队适用于添加元素容易，删除元素困难的情况
# 只增回滚莫队：只能向当前区间添加元素，不能删除元素，但可以通过回滚操作恢复状态
# 
# 算法要点：
# 1. 使用分块策略，块大小通常选择 sqrt(n)
# 2. 对查询进行特殊排序：按照左端点所在的块编号排序，如果左端点在同一块内，则按照右端点位置排序
# 3. 对于同一块内的查询，使用暴力方法处理
# 4. 对于跨块的查询，先扩展右边界，然后扩展左边界，计算答案后通过回滚操作恢复状态
#
# 时间复杂度：O((n+m)*sqrt(n))
# 空间复杂度：O(n)
# 
# 相关题目：
# 1. LOJ 2874. 「JOISC 2014 Day1」历史研究 - https://loj.ac/p/2874
# 2. LibreOJ 2874 历史研究 - https://loj.ac/problems/view/2874
# 3. 洛谷 P4688 掉进兔子洞 - https://www.luogu.com.cn/problem/P4688 (二次离线莫队应用)
# 4. LibreOJ 6277 数列分块入门 1 - https://loj.ac/p/6277 (分块基础)
# 5. LibreOJ 6278 数列分块入门 2 - https://loj.ac/p/6278 (分块应用)
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
MAXN = 100001
MAXB = 401

# 全局变量
n, m = 0, 0
arr = [0] * MAXN
query = [[0, 0, 0] for _ in range(MAXN)]
sorted_arr = [0] * MAXN
cntv = 0

blen, bnum = 0, 0
bi = [0] * MAXN
br = [0] * MAXB

# 词频表，记录每个数字在当前窗口中的出现次数
cnt = [0] * MAXN
# 当前窗口的最大重要度
curAns = 0

# 收集所有答案
ans = [0] * MAXN

# 二分查找，找到num在sorted数组中的位置
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

# 暴力遍历arr[l..r]得到答案，用于处理同一块内的查询
def force(l, r):
    global cnt, sorted_arr
    ret = 0
    # 统计词频
    for i in range(l, r + 1):
        cnt[arr[i]] += 1
    # 计算最大重要度
    for i in range(l, r + 1):
        ret = max(ret, cnt[arr[i]] * sorted_arr[arr[i]])
    # 清除临时统计结果
    for i in range(l, r + 1):
        cnt[arr[i]] -= 1
    return ret

# 窗口增加num，更新词频和当前答案
def add(num):
    global cnt, curAns, sorted_arr
    cnt[num] += 1
    curAns = max(curAns, cnt[num] * sorted_arr[num])

# 窗口减少num，只更新词频，不更新答案（因为是只增回滚莫队）
def del_(num):
    global cnt
    cnt[num] -= 1

# 核心计算函数
def compute():
    global curAns, cnt, ans, arr, query, bi, br, bnum, m
    # 按块处理查询
    qi = 1
    for block in range(1, bnum + 1):
        if qi > m:
            break
        # 每个块开始时重置状态
        curAns = 0
        for i in range(1, cntv + 1):
            cnt[i] = 0
        # 当前窗口的左右边界
        winl = br[block] + 1
        winr = br[block]
        
        # 处理属于当前块的所有查询
        while qi <= m and bi[query[qi][0]] == block:
            jobl = query[qi][0]  # 查询左边界
            jobr = query[qi][1]  # 查询右边界
            id_ = query[qi][2]   # 查询编号
            
            # 如果查询区间完全在当前块内，使用暴力方法
            if jobr <= br[block]:
                ans[id_] = force(jobl, jobr)
            else:
                # 否则使用莫队算法
                # 先扩展右边界到jobr
                while winr < jobr:
                    winr += 1
                    add(arr[winr])
                
                # 保存当前答案，然后扩展左边界到jobl
                backup = curAns
                while winl > jobl:
                    winl -= 1
                    add(arr[winl])
                
                # 记录答案
                ans[id_] = curAns
                
                # 恢复状态，只保留右边界扩展的结果
                curAns = backup
                while winl <= br[block]:
                    del_(arr[winl])
                    winl += 1
            qi += 1

# 预处理函数
def prepare():
    global n, arr, sorted_arr, cntv, blen, bnum, bi, br
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
    global n, m, arr, query
    # 读取输入
    line = sys.stdin.readline().split()
    n, m = int(line[0]), int(line[1])
    line = sys.stdin.readline().split()
    for i in range(1, n + 1):
        arr[i] = int(line[i - 1])
    for i in range(1, m + 1):
        line = sys.stdin.readline().split()
        query[i][0] = int(line[0])
        query[i][1] = int(line[1])
        query[i][2] = i
    
    prepare()
    compute()
    
    # 输出结果
    for i in range(1, m + 1):
        print(ans[i])

if __name__ == "__main__":
    main()