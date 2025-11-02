"""
洛谷 P1081 开车旅行

题目描述：
小 A 和小 B 决定利用假期外出旅行，他们将想去的城市从 1 到 n 编号，且编号较小的城市在编号较大的城市的西边，
已知各个城市的海拔高度互不相同，记城市 i 的海拔高度为hi，城市 i 和城市 j 之间的距离 di,j 恰好是这两个城市海拔高度之差的绝对值，
即 di,j = |hi - hj|。
旅行过程中，小 A 和小 B 轮流开车，第一天小 A 开车，之后每天轮换一次。他们计划选择一个城市 s 作为起点，
一直向东行驶，并且最多行驶 x 公里就结束旅行。
小 B 总是沿着前进方向选择一个最近的城市作为目的地，而小 A 总是沿着前进方向选择第二近的城市作为目的地
（注意：本题中如果当前城市到两个城市的距离相同，则认为离海拔低的那个城市更近）。
如果其中任何一人无法按照自己的原则选择目的城市，或者到达目的地会使行驶的总距离超出 x 公里，他们就会结束旅行。

问题1 : 给定距离x0，返回1 ~ n-1中从哪个点出发，a行驶距离 / b行驶距离，比值最小
        如果从多个点出发时，比值都为最小，那么返回arr中的值最大的点
问题2 : 给定s、x，返回旅行停止时，a开了多少距离、b开了多少距离

解题思路：
这是一个结合了数据结构和倍增思想的复杂问题。

核心思想：
1. 预处理：对于每个城市，找到它右边的第一近和第二近城市
2. 倍增优化：预处理2^k轮a和b交替开车能到达的位置和距离
3. 查询处理：使用倍增快速计算任意起点和距离限制下的行驶情况

具体步骤：
1. 使用TreeSet或双向链表找到每个城市的第一近和第二近城市
2. 使用倍增思想预处理状态转移表
3. 对于查询，使用倍增快速计算结果

时间复杂度：预处理O(n log n)，查询O(log x)
空间复杂度：O(n log n)

相关题目：
- LeetCode 220. 存在重复元素 III (TreeSet应用)
- POJ 1733 - Parity game (离散化 + 倍增)
- Codeforces 822D - My pretty girl Noora (数学 + 倍增)
"""

import math
from typing import List, Tuple

# 最大节点数
MAXN = 100002
# 最大幂次
MAXP = 20

def preprocess_near(heights: List[int]) -> Tuple[List[int], List[int], List[int], List[int]]:
    """
    预处理每个城市的第一近和第二近城市
    
    Args:
        heights: 各城市海拔高度数组
    
    Returns:
        (to1, dist1, to2, dist2) 元组
        to1[i]: i城市右侧第一近城市编号
        dist1[i]: i城市到第一近城市的距离
        to2[i]: i城市右侧第二近城市编号
        dist2[i]: i城市到第二近城市的距离
    """
    n = len(heights) - 1  # heights[0] 不使用
    
    # 初始化结果数组
    to1 = [0] * (n + 1)
    dist1 = [0] * (n + 1)
    to2 = [0] * (n + 1)
    dist2 = [0] * (n + 1)
    
    # 创建城市信息数组并按海拔排序
    cities = [(i, heights[i]) for i in range(1, n + 1)]
    cities.sort(key=lambda x: x[1])  # 按海拔排序
    
    # 为每个城市找最近和次近城市
    for i in range(1, n + 1):
        city_i = i
        candidates = []
        
        # 找到当前城市在排序数组中的位置
        pos = -1
        for j, (city, height) in enumerate(cities):
            if city == city_i:
                pos = j
                break
        
        # 检查附近的几个城市
        for j in range(max(0, pos - 2), min(len(cities), pos + 3)):
            if cities[j][0] != city_i and cities[j][0] > city_i:  # 右侧城市
                candidates.append(cities[j][0])
        
        # 计算距离并排序
        distances = []
        for city_j in candidates:
            dist = abs(heights[city_i] - heights[city_j])
            distances.append((dist, heights[city_j], city_j))
        
        distances.sort(key=lambda x: (x[0], x[1]))  # 按距离、海拔排序
        
        # 更新最近和次近城市
        if len(distances) >= 1:
            to1[city_i] = distances[0][2]
            dist1[city_i] = distances[0][0]
        if len(distances) >= 2:
            to2[city_i] = distances[1][2]
            dist2[city_i] = distances[1][0]
    
    return to1, dist1, to2, dist2


def preprocess_st(n: int, to1: List[int], to2: List[int], 
                  dist1: List[int], dist2: List[int]) -> Tuple[List[List[int]], List[List[int]], List[List[int]], List[List[int]]]:
    """
    倍增预处理
    
    Args:
        n: 城市数量
        to1, dist1: 第一近城市信息
        to2, dist2: 第二近城市信息
    
    Returns:
        (stto, stdist, sta, stb) 倍增数组
    """
    # stto[i][p]: 从i位置出发，a和b轮流开2^p轮之后，车到达了几号点
    stto = [[0] * (MAXP + 1) for _ in range(n + 1)]
    # stdist[i][p]: 从i位置出发，a和b轮流开2^p轮之后，总距离是多少
    stdist = [[0] * (MAXP + 1) for _ in range(n + 1)]
    # sta[i][p]: 从i位置出发，a和b轮流开2^p轮之后，a行驶了多少距离
    sta = [[0] * (MAXP + 1) for _ in range(n + 1)]
    # stb[i][p]: 从i位置出发，a和b轮流开2^p轮之后，b行驶了多少距离
    stb = [[0] * (MAXP + 1) for _ in range(n + 1)]
    
    # 倍增初始化
    for i in range(1, n + 1):
        # 一轮：a开到第二近，b开到第一近
        if to2[i] != 0 and to1[to2[i]] != 0:
            stto[i][0] = to1[to2[i]]  # 从i出发，a开到to2[i]，b再开到to1[to2[i]]
            stdist[i][0] = dist2[i] + dist1[to2[i]]  # 总距离
            sta[i][0] = dist2[i]  # a行驶距离
            stb[i][0] = dist1[to2[i]]  # b行驶距离
    
    # 生成倍增表
    for p in range(1, MAXP + 1):
        for i in range(1, n + 1):
            if stto[i][p - 1] != 0 and stto[stto[i][p - 1]][p - 1] != 0:
                stto[i][p] = stto[stto[i][p - 1]][p - 1]
                stdist[i][p] = stdist[i][p - 1] + stdist[stto[i][p - 1]][p - 1]
                sta[i][p] = sta[i][p - 1] + sta[stto[i][p - 1]][p - 1]
                stb[i][p] = stb[i][p - 1] + stb[stto[i][p - 1]][p - 1]
    
    return stto, stdist, sta, stb


def travel(s: int, x: int, stto: List[List[int]], stdist: List[List[int]], 
           sta: List[List[int]], stb: List[List[int]]) -> Tuple[int, int]:
    """
    计算从城市s出发，最多行驶x距离时，a和b各自行驶的距离
    
    Args:
        s: 起始城市
        x: 最大行驶距离
        stto, stdist, sta, stb: 倍增数组
    
    Returns:
        (a_dist, b_dist) 元组，a和b各自行驶的距离
    """
    a_dist = 0
    b_dist = 0
    
    # 使用倍增快速计算
    for p in range(MAXP, -1, -1):
        if (stto[s][p] != 0 and stdist[s][p] <= x):
            x -= stdist[s][p]
            a_dist += sta[s][p]
            b_dist += stb[s][p]
            s = stto[s][p]
    
    return a_dist, b_dist


# 测试用例
if __name__ == "__main__":
    # 模拟测试用例
    n = 4
    heights = [0, 10, 20, 15, 30]  # heights[0] 不使用
    
    print("测试用例:")
    print(f"城市数: {n}")
    print(f"各城市海拔: {heights[1:]}")
    
    # 预处理
    to1, dist1, to2, dist2 = preprocess_near(heights)
    stto, stdist, sta, stb = preprocess_st(n, to1, to2, dist1, dist2)
    
    # 查询示例
    a_dist, b_dist = travel(1, 25, stto, stdist, sta, stb)
    print(f"从城市1出发，最多行驶25距离:")
    print(f"a行驶距离: {a_dist}, b行驶距离: {b_dist}")