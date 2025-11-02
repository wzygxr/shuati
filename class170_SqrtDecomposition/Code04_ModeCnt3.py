# 空间少求众数的次数，Python版
# 题目来源：洛谷P5048 [Ynoi2019 模拟赛] Yuno loves sqrt technology III
# 题目链接：https://www.luogu.com.cn/problem/P5048
# 题目大意：
# 给定一个长度为n的数组arr，接下来有m条操作，每条操作格式如下
# 操作 l r : 打印arr[l..r]范围上，众数到底出现了几次
# 1 <= 所有数值 <= 5 * 10^5
# 内存空间只有64MB，题目要求强制在线，具体规则可以打开测试链接查看
# 测试链接 : https://www.luogu.com.cn/problem/P5048

# 解题思路：
# 使用分块算法解决此问题，采用预处理优化查询
# 1. 将数组分成sqrt(n)大小的块
# 2. 对数组元素按值和下标进行排序，构建sortList数组
# 3. 预处理modeCnt[i][j]表示从第i块到第j块中众数的出现次数
# 4. 对于查询操作：
#    - 如果在同一个块内，直接暴力统计
#    - 如果跨多个块，结合预处理信息和两端不完整块统计

# 时间复杂度分析：
# 1. 预处理：O(n*sqrt(n))，构建modeCnt数组
# 2. 查询操作：O(sqrt(n))，处理两端不完整块
# 空间复杂度：O(n*sqrt(n))，存储sortList、listIdx和modeCnt数组

import math
import sys

# 最大数组大小
MAXN = 500001
# 最大块数
MAXB = 801

# 全局变量
n = 0  # 数组长度
m = 0  # 操作数
arr = [0] * MAXN  # 原数组

# 块大小和块数量
blen = 0
bnum = 0
bi = [0] * MAXN  # 每个元素所属的块
bl = [0] * MAXB  # 每个块的左右边界
br = [0] * MAXB

# sortList数组，存储(值, 下标)对
# 使用列表存储元组 (值, 下标)
sortList = [(0, 0)] * MAXN
# listIdx[i] = j，表示arr[i]这个元素在sortList里的j位置
listIdx = [0] * MAXN
# modeCnt[i][j]表示从i块到j块中众数的出现次数
modeCnt = [[0 for _ in range(MAXB)] for _ in range(MAXB)]
# 数字词频统计
numCnt = [0] * MAXN

def prepare():
    """
    预处理函数，构建分块结构和预处理数组
    时间复杂度：O(n*sqrt(n))
    """
    global n, blen, bnum, bi, bl, br, arr, sortList, listIdx, modeCnt, numCnt
    
    # 建块
    blen = int(math.sqrt(n))
    bnum = (n + blen - 1) // blen
    
    # 计算每个元素属于哪个块
    for i in range(1, n + 1):
        bi[i] = (i - 1) // blen + 1
    
    # 计算每个块的左右边界
    for i in range(1, bnum + 1):
        bl[i] = (i - 1) * blen + 1
        br[i] = min(i * blen, n)
    
    # 构建sortList数组，存储(值, 下标)对
    for i in range(1, n + 1):
        sortList[i] = (arr[i], i)  # (值, 下标)
    
    # 按值和下标排序
    # 首先按值排序，值相同时按下标排序
    sortList[1:n+1] = sorted(sortList[1:n+1], key=lambda x: (x[0], x[1]))
    
    # 构建listIdx数组，记录每个元素在sortList中的位置
    for i in range(1, n + 1):
        listIdx[sortList[i][1]] = i
    
    # 填好modeCnt数组
    # 预处理从第i块到第j块中众数的出现次数
    for i in range(1, bnum + 1):
        for j in range(i, bnum + 1):
            # 初始众数出现次数为从第i块到第j-1块的众数出现次数
            cnt = modeCnt[i][j - 1]
            # 遍历第j块中的所有元素，更新众数出现次数
            for k in range(bl[j], br[j] + 1):
                numCnt[arr[k]] += 1
                cnt = max(cnt, numCnt[arr[k]])
            modeCnt[i][j] = cnt
        
        # 清空统计数组
        for k in range(1, n + 1):
            numCnt[k] = 0

def query(l, r):
    """
    查询区间[l,r]中众数的出现次数
    时间复杂度：O(sqrt(n))
    :param l: 区间左端点
    :param r: 区间右端点
    :return: 众数的出现次数
    """
    global arr, bi, bl, br, modeCnt, numCnt, sortList, listIdx
    
    ans = 0
    # 如果在同一个块内，直接暴力统计
    if bi[l] == bi[r]:
        # 统计各数字出现次数，同时更新最大出现次数
        for i in range(l, r + 1):
            numCnt[arr[i]] += 1
            ans = max(ans, numCnt[arr[i]])
        
        # 清空统计数组
        for i in range(l, r + 1):
            numCnt[arr[i]] = 0
    else:
        # 获取中间完整块的众数出现次数
        ans = modeCnt[bi[l] + 1][bi[r] - 1]
        
        # 处理左端不完整块
        # 通过listIdx找到该元素在sortList中的位置，然后向后查找连续相同值的元素
        for i in range(l, br[bi[l]] + 1):
            idx = listIdx[i]
            # 向后查找连续相同值的元素，直到超出范围或下标大于r
            while idx + ans <= n and sortList[idx + ans][0] == arr[i] and sortList[idx + ans][1] <= r:
                ans += 1
        
        # 处理右端不完整块
        # 通过listIdx找到该元素在sortList中的位置，然后向前查找连续相同值的元素
        for i in range(bl[bi[r]], r + 1):
            idx = listIdx[i]
            # 向前查找连续相同值的元素，直到超出范围或下标小于l
            while idx - ans >= 1 and sortList[idx - ans][0] == arr[i] and sortList[idx - ans][1] >= l:
                ans += 1
    
    return ans

def main():
    global n, m, arr
    
    # 读取数组长度和操作数
    line = input().split()
    n = int(line[0])
    m = int(line[1])
    
    # 读取数组元素
    elements = list(map(int, input().split()))
    for i in range(1, n + 1):
        arr[i] = elements[i - 1]
    
    # 预处理
    prepare()
    
    # 强制在线处理
    lastAns = 0
    for _ in range(m):
        line = input().split()
        l = int(line[0])
        r = int(line[1])
        l ^= lastAns
        r ^= lastAns
        lastAns = query(l, r)
        print(lastAns)

if __name__ == "__main__":
    main()