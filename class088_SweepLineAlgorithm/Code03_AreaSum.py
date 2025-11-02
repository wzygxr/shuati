"""
矩形面积并 - 扫描线算法实现
问题描述：给定平面上的n个矩形，求这些矩形的并集面积
解题思路：使用扫描线算法结合线段树来高效计算矩形面积并
算法复杂度：时间复杂度O(n log n)，空间复杂度O(n)
工程化考量：
1. 使用高效的IO处理，适用于竞赛环境
2. 线段树实现优化，利用问题特殊性避免懒更新
3. 离散化处理y坐标，减少空间使用
4. 边界条件处理完善，避免数组越界
测试链接 : https://www.luogu.com.cn/problem/P5490
"""

import bisect
import sys

# 最大矩形数量
MAXN = 300001

# 存储矩形信息：[左下x, 左下y, 右上x, 右上y]
rec = [[0 for _ in range(4)] for _ in range(MAXN)]

# 存储扫描线事件：[x坐标, y下界, y上界, 变化量(1或-1)]
line = [[0 for _ in range(4)] for _ in range(MAXN)]

# 存储所有y坐标用于离散化
ysort = [0] * MAXN

# 线段树某范围总长度
length = [0] * (MAXN << 2)

# 线段树某范围覆盖长度
cover = [0] * (MAXN << 2)

# 线段树某范围覆盖次数
times = [0] * (MAXN << 2)

def prepare(n):
    """
    离散化y坐标数组，去除重复元素
    @param n 原始元素个数
    @return 去重后的元素个数
    """
    global ysort
    ysort[1:n+1] = sorted(ysort[1:n+1])
    m = 1
    for i in range(2, n + 1):
        if ysort[m] != ysort[i]:
            m += 1
            ysort[m] = ysort[i]
    ysort[m + 1] = ysort[m]
    return m

def rank(n, num):
    """
    二分查找y坐标在离散化数组中的位置
    @param n 离散化数组长度
    @param num 要查找的y坐标值
    @return 离散化后的索引位置
    """
    ans = 0
    l, r = 1, n
    while l <= r:
        mid = (l + r) >> 1
        if ysort[mid] >= num:
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
    if l < r:
        mid = (l + r) >> 1
        build(l, mid, i << 1)
        build(mid + 1, r, i << 1 | 1)
    length[i] = ysort[r + 1] - ysort[l]
    times[i] = 0
    cover[i] = 0

def up(i):
    """
    更新线段树节点的覆盖长度
    @param i 当前节点在线段树中的索引
    """
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
    if jobl <= l and r <= jobr:
        times[i] += jobv
    else:
        mid = (l + r) >> 1
        if jobl <= mid:
            add(jobl, jobr, jobv, l, mid, i << 1)
        if jobr > mid:
            add(jobl, jobr, jobv, mid + 1, r, i << 1 | 1)
    up(i)

def compute(n):
    """
    计算n个矩形的面积并
    算法核心思想：
    1. 将每个矩形的左右边界作为扫描线事件
    2. 按x坐标排序所有扫描线事件
    3. 从左到右扫描，维护当前y轴上的覆盖长度
    4. 相邻扫描线之间的面积 = 覆盖长度 × x轴距离
    @param n 矩形数量
    @return 矩形面积并
    """
    global rec, line, ysort
    
    # 构造扫描线事件
    for i in range(1, n + 1):
        j = i + n
        x1, y1, x2, y2 = rec[i][0], rec[i][1], rec[i][2], rec[i][3]
        ysort[i] = y1
        ysort[j] = y2
        line[i][0] = x1
        line[i][1] = y1
        line[i][2] = y2
        line[i][3] = 1
        line[j][0] = x2
        line[j][1] = y1
        line[j][2] = y2
        line[j][3] = -1
    
    n <<= 1
    
    # 离散化y坐标
    m = prepare(n)
    
    # 构建线段树
    build(1, m, 1)
    
    # 按x坐标排序扫描线事件
    line[1:n+1] = sorted(line[1:n+1], key=lambda x: x[0])
    
    ans = 0
    pre = 0
    for i in range(1, n + 1):
        # 累加面积：当前覆盖长度 × 与前一条扫描线的距离
        ans += cover[1] * (line[i][0] - pre)
        pre = line[i][0]
        
        # 更新线段树中的覆盖情况
        add(rank(m, line[i][1]), rank(m, line[i][2]) - 1, line[i][3], 1, m, 1)
    
    return ans

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
    
    # 计算并输出矩形面积并
    print(compute(n))

# 由于这是洛谷题目，需要特殊处理输入输出格式
# 在实际提交时，请将函数调用注释掉
# if __name__ == "__main__":
#     main()