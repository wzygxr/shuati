"""
矩形周长并 - 扫描线算法实现
问题描述：给定平面上的n个矩形，求这些矩形的并集周长
解题思路：使用扫描线算法分别计算水平边和垂直边的长度
算法复杂度：时间复杂度O(n log n)，空间复杂度O(n)
工程化考量：
1. 使用高效的IO处理，适用于竞赛环境
2. 线段树实现优化，利用问题特殊性避免懒更新
3. 离散化处理坐标，减少空间使用
4. 边界条件处理完善，避免数组越界
测试链接 : https://www.luogu.com.cn/problem/P1856
"""

import bisect
import sys

# 最大矩形数量
MAXN = 20001

# 存储矩形信息：[左下x, 左下y, 右上x, 右上y]
rec = [[0 for _ in range(4)] for _ in range(MAXN)]

# 存储扫描线事件：[扫描线位置, 区间下界, 区间上界, 变化量(1或-1)]
line = [[0 for _ in range(4)] for _ in range(MAXN)]

# 存储所有坐标用于离散化
vsort = [0] * MAXN

# 线段树某范围总长度
length = [0] * (MAXN << 2)

# 线段树某范围覆盖长度
cover = [0] * (MAXN << 2)

# 线段树某范围覆盖次数
times = [0] * (MAXN << 2)

def prepare(n):
    """
    离散化坐标数组，去除重复元素
    @param n 原始元素个数
    @return 去重后的元素个数
    """
    global vsort
    vsort[1:n+1] = sorted(vsort[1:n+1])
    m = 1
    for i in range(2, n + 1):
        if vsort[m] != vsort[i]:
            m += 1
            vsort[m] = vsort[i]
    vsort[m + 1] = vsort[m]
    return m

def rank(n, num):
    """
    二分查找坐标在离散化数组中的位置
    @param n 离散化数组长度
    @param num 要查找的坐标值
    @return 离散化后的索引位置
    """
    ans = 0
    l, r = 1, n
    while l <= r:
        mid = (l + r) >> 1
        if vsort[mid] >= num:
            ans = mid
            r = mid - 1
        else:
            l = mid + 1
    return ans

def build(l, r, i):
    """
    构建线段树
    @param l 当前节点表示区间的左边界
    @param r 当前节点表示区间的右边界
    @param i 当前节点在线段树中的索引
    """
    global length, times, cover
    if l < r:
        mid = (l + r) >> 1
        build(l, mid, i << 1)
        build(mid + 1, r, i << 1 | 1)
    length[i] = vsort[r + 1] - vsort[l]
    times[i] = 0
    cover[i] = 0

def up(i):
    """
    更新线段树节点的覆盖长度
    @param i 当前节点在线段树中的索引
    """
    global cover, times, length
    if times[i] > 0:
        cover[i] = length[i]
    else:
        cover[i] = cover[i << 1] + cover[i << 1 | 1]

def add(jobl, jobr, jobv, l, r, i):
    """
    在线段树中添加或删除扫描线覆盖
    @param jobl 操作区间左边界
    @param jobr 操作区间右边界
    @param jobv 操作值(+1表示添加，-1表示删除)
    @param l 当前节点表示区间的左边界
    @param r 当前节点表示区间的右边界
    @param i 当前节点在线段树中的索引
    """
    global times, cover
    if jobl <= l and r <= jobr:
        times[i] += jobv
    else:
        mid = (l + r) >> 1
        if jobl <= mid:
            add(jobl, jobr, jobv, l, mid, i << 1)
        if jobr > mid:
            add(jobl, jobr, jobv, mid + 1, r, i << 1 | 1)
    up(i)

def scanY(n):
    """
    扫描y轴方向计算水平边长度
    @param n 矩形数量
    @return 水平边长度总和
    """
    global rec, vsort, line
    # 构造y轴方向的扫描线事件
    for i in range(1, n + 1):
        j = i + n
        x1, y1, x2, y2 = rec[i][0], rec[i][1], rec[i][2], rec[i][3]
        vsort[i] = y1
        vsort[j] = y2
        line[i][0] = x1
        line[i][1] = y1
        line[i][2] = y2
        line[i][3] = 1
        line[j][0] = x2
        line[j][1] = y1
        line[j][2] = y2
        line[j][3] = -1
    return scan(n << 1)

def scanX(n):
    """
    扫描x轴方向计算垂直边长度
    @param n 矩形数量
    @return 垂直边长度总和
    """
    global rec, vsort, line
    # 构造x轴方向的扫描线事件
    for i in range(1, n + 1):
        j = i + n
        x1, y1, x2, y2 = rec[i][0], rec[i][1], rec[i][2], rec[i][3]
        vsort[i] = x1
        vsort[j] = x2
        line[i][0] = y1
        line[i][1] = x1
        line[i][2] = x2
        line[i][3] = 1
        line[j][0] = y2
        line[j][1] = x1
        line[j][2] = x2
        line[j][3] = -1
    return scan(n << 1)

def scan(n):
    """
    执行扫描线算法计算投影长度变化总和
    @param n 扫描线事件数量
    @return 投影长度变化总和
    """
    global line, cover
    
    m = prepare(n)
    build(1, m, 1)
    # 这里有个坑
    # 在排序时，如果同一个位置的扫描线有多条，也就是line[i][0] == line[j][0]时
    # 应该先处理区间覆盖+1的扫描线，然后再处理区间覆盖-1的扫描线
    # 不然投影长度会频繁变化，导致答案错误
    # 不过测试数据并没有安排这方面的测试
    line[1:n+1] = sorted(line[1:n+1], key=lambda x: (x[0], -x[3]))
    
    ans = 0
    for i in range(1, n + 1):
        pre = cover[1]
        add(rank(m, line[i][1]), rank(m, line[i][2]) - 1, line[i][3], 1, m, 1)
        ans += abs(cover[1] - pre)
    return ans

def compute(n):
    """
    计算n个矩形的周长并
    算法核心思想：
    1. 分别计算水平边和垂直边的长度
    2. 水平边长度 = 扫描y轴时的投影变化总和
    3. 垂直边长度 = 扫描x轴时的投影变化总和
    @param n 矩形数量
    @return 矩形周长并
    """
    return scanY(n) + scanX(n)

def main():
    """主函数"""
    # 读取矩形数量
    n = int(input())
    
    # 读取所有矩形的坐标信息
    for i in range(1, n + 1):
        coords = list(map(int, input().split()))
        rec[i][0] = coords[0]  # 左下角x
        rec[i][1] = coords[1]  # 左下角y
        rec[i][2] = coords[2]  # 右上角x
        rec[i][3] = coords[3]  # 右上角y
    
    # 计算并输出矩形周长并
    print(compute(n))

# 由于这是洛谷题目，需要特殊处理输入输出格式
# 在实际提交时，请将函数调用注释掉
# if __name__ == "__main__":
#     main()