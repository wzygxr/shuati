# 蒲公英，Python版
# 题目来源：洛谷P4168 [Violet]蒲公英
# 题目链接：https://www.luogu.com.cn/problem/P4168
# 题目大意：
# 给定一个长度为n的数组arr，接下来有m条操作，每条操作格式如下
# 操作 l r : 打印arr[l..r]范围上的众数，如果有多个众数，打印值最小的
# 1 <= n <= 4 * 10^4
# 1 <= m <= 5 * 10^4
# 1 <= 数组中的值 <= 10^9
# 题目要求强制在线，具体规则可以打开测试链接查看
# 测试链接 : https://www.luogu.com.cn/problem/P4168

# 解题思路：
# 使用分块算法解决此问题，采用预处理优化查询
# 1. 将数组分成sqrt(n)大小的块
# 2. 对数组进行离散化处理，将大数值映射到小范围
# 3. 预处理freq[i][j]表示前i块中数字j出现的次数
# 4. 预处理mode[i][j]表示从第i块到第j块的众数（值最小的）
# 5. 对于查询操作：
#    - 如果在同一个块内，直接暴力统计
#    - 如果跨多个块，结合预处理信息和两端不完整块统计

# 时间复杂度分析：
# 1. 预处理：O(n*sqrt(n))，构建freq和mode数组
# 2. 查询操作：O(sqrt(n))，处理两端不完整块
# 空间复杂度：O(n*sqrt(n))，存储freq和mode数组

import math
import bisect
import sys

# 最大数组大小
MAXN = 40001
# 最大块数
MAXB = 201

# 全局变量
n = 0  # 数组长度
m = 0  # 操作数
s = 0  # 离散化后不同数字的个数
arr = [0] * MAXN  # 原数组
sortv = [0] * MAXN  # 数字做离散化

# 块大小和块数量
blen = 0
bnum = 0
bi = [0] * MAXN  # 每个元素所属的块
bl = [0] * MAXB  # 每个块的左右边界
br = [0] * MAXB

# freq[i][j]表示前i块中j出现的次数
freq = [[0 for _ in range(MAXN)] for _ in range(MAXB)]
# mode[i][j]表示从i块到j块中的众数(最小)
mode = [[0 for _ in range(MAXB)] for _ in range(MAXB)]
# 数字的词频统计（临时使用）
numCnt = [0] * MAXN

def lower(num):
    """
    二分查找离散化后的值
    时间复杂度：O(log(n))
    :param num: 原始数值
    :return: 离散化后的值
    """
    global sortv, s
    # 使用bisect模块进行二分查找
    # bisect_left返回第一个>=num的位置
    pos = bisect.bisect_left(sortv[1:s+1], num)
    return pos + 1 if pos < s and sortv[pos + 1] >= num else 0

def getCnt(l, r, v):
    """
    获取第l块到第r块中数字v出现的次数
    时间复杂度：O(1)
    :param l: 起始块
    :param r: 结束块
    :param v: 数字（离散化后的值）
    :return: 出现次数
    """
    return freq[r][v] - freq[l - 1][v]

def prepare():
    """
    预处理函数，构建分块结构和预处理数组
    时间复杂度：O(n*sqrt(n))
    """
    global n, blen, bnum, bi, bl, br, s, arr, sortv, freq, mode
    
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
    
    # 离散化
    # 复制原数组用于排序
    for i in range(1, n + 1):
        sortv[i] = arr[i]
    
    # 排序
    sortv[1:n+1] = sorted(sortv[1:n+1])
    
    # 去重，得到不同数字的个数
    s = 1
    for i in range(2, n + 1):
        if sortv[s] != sortv[i]:
            s += 1
            sortv[s] = sortv[i]
    
    # 将原数组中的数字映射为离散化后的值
    for i in range(1, n + 1):
        # 使用bisect模块进行二分查找
        pos = bisect.bisect_left(sortv[1:s+1], arr[i])
        arr[i] = pos + 1
    
    # 填好freq数组
    # 统计每块中各数字出现次数，并计算前缀和
    for i in range(1, bnum + 1):
        # 统计当前块中各数字出现次数
        for j in range(bl[i], br[i] + 1):
            freq[i][arr[j]] += 1
        
        # 计算前缀和
        for j in range(1, s + 1):
            freq[i][j] += freq[i - 1][j]
    
    # 填好mode数组
    # 预处理从第i块到第j块的众数
    for i in range(1, bnum + 1):
        for j in range(i, bnum + 1):
            # 初始众数为从第i块到第j-1块的众数
            most = mode[i][j - 1]
            mostCnt = getCnt(i, j, most)
            
            # 遍历第j块中的所有元素，更新众数
            for k in range(bl[j], br[j] + 1):
                cur = arr[k]
                curCnt = getCnt(i, j, cur)
                # 如果当前数字出现次数更多，或者出现次数相同但值更小，则更新众数
                if curCnt > mostCnt or (curCnt == mostCnt and cur < most):
                    most = cur
                    mostCnt = curCnt
            
            mode[i][j] = most

def query(l, r):
    """
    查询区间[l,r]的众数（值最小的）
    时间复杂度：O(sqrt(n))
    :param l: 区间左端点
    :param r: 区间右端点
    :return: 众数（原始值）
    """
    global arr, bi, bl, br, mode, numCnt, sortv
    
    most = 0
    
    # 如果在同一个块内，直接暴力统计
    if bi[l] == bi[r]:
        # 统计各数字出现次数
        for i in range(l, r + 1):
            numCnt[arr[i]] += 1
        
        # 找出众数
        for i in range(l, r + 1):
            if numCnt[arr[i]] > numCnt[most] or (numCnt[arr[i]] == numCnt[most] and arr[i] < most):
                most = arr[i]
        
        # 清空统计数组
        for i in range(l, r + 1):
            numCnt[arr[i]] = 0
    else:
        # 处理左端不完整块
        for i in range(l, br[bi[l]] + 1):
            numCnt[arr[i]] += 1
        
        # 处理右端不完整块
        for i in range(bl[bi[r]], r + 1):
            numCnt[arr[i]] += 1
        
        # 获取中间完整块的众数
        most = mode[bi[l] + 1][bi[r] - 1]
        # 计算该众数在完整块和不完整块中的总出现次数
        mostCnt = getCnt(bi[l] + 1, bi[r] - 1, most) + numCnt[most]
        
        # 检查左端不完整块中的数字是否能成为新的众数
        for i in range(l, br[bi[l]] + 1):
            cur = arr[i]
            curCnt = getCnt(bi[l] + 1, bi[r] - 1, cur) + numCnt[cur]
            if curCnt > mostCnt or (curCnt == mostCnt and cur < most):
                most = cur
                mostCnt = curCnt
        
        # 检查右端不完整块中的数字是否能成为新的众数
        for i in range(bl[bi[r]], r + 1):
            cur = arr[i]
            curCnt = getCnt(bi[l] + 1, bi[r] - 1, cur) + numCnt[cur]
            if curCnt > mostCnt or (curCnt == mostCnt and cur < most):
                most = cur
                mostCnt = curCnt
        
        # 清空统计数组
        for i in range(l, br[bi[l]] + 1):
            numCnt[arr[i]] = 0
        for i in range(bl[bi[r]], r + 1):
            numCnt[arr[i]] = 0
    
    # 返回原始值
    return sortv[most]

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
        a = int(line[0])
        b = int(line[1])
        a = (a + lastAns - 1) % n + 1
        b = (b + lastAns - 1) % n + 1
        l = min(a, b)
        r = max(a, b)
        lastAns = query(l, r)
        print(lastAns)

if __name__ == "__main__":
    main()