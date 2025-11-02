#!/usr/bin/env python
# -*- coding: utf-8 -*-

"""
获取所有钥匙的最短路径

题目链接：https://leetcode.cn/problems/shortest-path-to-get-all-keys

题目描述：
给定一个二维网格 grid ，其中：
'.' 代表一个空房间、'#' 代表一堵墙、'@' 是起点
小写字母代表钥匙、大写字母代表锁
从起点开始出发，一次移动是指向四个基本方向之一行走一个单位空间
不能在网格外面行走，也无法穿过一堵墙
如果途经一个钥匙，我们就把它捡起来。除非我们手里有对应的钥匙，否则无法通过锁。
假设 k 为 钥匙/锁 的个数，且满足 1 <= k <= 6，
字母表中的前 k 个字母在网格中都有自己对应的一个小写和一个大写字母
换言之，每个锁有唯一对应的钥匙，每个钥匙也有唯一对应的锁
另外，代表钥匙和锁的字母互为大小写并按字母顺序排列
返回获取所有钥匙所需要的移动的最少次数。如果无法获取所有钥匙，返回 -1 。

解题思路：
这是一个状态空间搜索问题，可以使用BFS解决。
与传统BFS不同的是，这里的状态不仅包括位置(x,y)，还包括收集钥匙的状态。
我们用位运算来表示钥匙收集状态，第i位为1表示已收集第i把钥匙。
使用三维visited数组visited[x][y][state]来记录状态是否已访问。

算法应用场景：
- 游戏中的寻路问题
- 机器人路径规划
- 状态空间搜索问题

时间复杂度分析：
O(n*m*2^k)，其中n和m是网格的行数和列数，k是钥匙的数量

空间复杂度分析：
O(n*m*2^k)，存储访问状态数组
"""

from collections import deque

def shortestPathAllKeys(grid):
    """
    使用BFS求解最短路径
    
    算法核心思想：
    1. 这是一个状态空间搜索问题，状态包括位置和钥匙收集情况
    2. 使用BFS保证第一次到达目标状态时步数最少
    3. 用位运算优化钥匙状态的表示和处理
    4. 通过visited数组避免重复访问相同状态
    
    算法步骤：
    1. 初始化，将起点状态加入队列
    2. 按层进行BFS遍历，每层代表相同的步数
    3. 对于每个状态，向四个方向扩展
    4. 根据移动到的新位置更新钥匙状态
    5. 检查是否能通过锁（有对应钥匙）
    6. 如果收集到所有钥匙，返回步数
    
    时间复杂度: O(n*m*2^k)
    空间复杂度: O(n*m*2^k)
    
    Args:
        grid: List[str] - 字符串数组表示的网格
    
    Returns:
        int - 获取所有钥匙所需要的移动的最少次数，如果无法获取所有钥匙返回-1
    """
    n = len(grid)      # 网格行数
    m = len(grid[0])   # 网格列数
    
    # 寻找起点和统计所有钥匙
    start_x, start_y = 0, 0
    key_count = 0
    
    for i in range(n):
        for j in range(m):
            if grid[i][j] == '@':
                start_x, start_y = i, j
            elif 'a' <= grid[i][j] <= 'f':
                key_count |= 1 << (ord(grid[i][j]) - ord('a'))
    
    # BFS队列，存储(行坐标, 列坐标, 钥匙状态)
    queue = deque([(start_x, start_y, 0)])
    
    # visited[x][y][state]表示在位置(x,y)且钥匙收集状态为state时是否已访问
    visited = [[[False] * (1 << 6) for _ in range(m)] for _ in range(n)]
    visited[start_x][start_y][0] = True
    
    # 方向数组：上、右、下、左
    move = [(-1, 0), (0, 1), (1, 0), (0, -1)]
    
    # level表示移动的步数
    level = 0
    
    # BFS主循环
    while queue:
        # 按层遍历，保证同层节点具有相同的步数
        size = len(queue)
        for _ in range(size):
            x, y, s = queue.popleft()
            
            # 向四个方向扩展
            for dx, dy in move:
                nx, ny = x + dx, y + dy
                ns = s  # 新钥匙状态，初始与当前状态相同
                
                # 越界或者遇到墙，跳过
                if nx < 0 or nx >= n or ny < 0 or ny >= m or grid[nx][ny] == '#':
                    continue
                
                # 遇到锁但没有对应的钥匙，跳过
                # 检查方法：(ns & (1 << (ord(grid[nx][ny]) - ord('A')))) == 0
                # 如果结果为0，说明对应位为0，即没有该钥匙
                if 'A' <= grid[nx][ny] <= 'F' and ((ns & (1 << (ord(grid[nx][ny]) - ord('A')))) == 0):
                    continue
                
                # 遇到钥匙，收集钥匙
                # 使用位运算更新钥匙状态
                if 'a' <= grid[nx][ny] <= 'f':
                    ns |= (1 << (ord(grid[nx][ny]) - ord('a')))
                
                # 如果收集到了所有钥匙，返回步数
                if ns == key_count:
                    return level + 1
                
                # 如果该状态未访问过，加入队列
                if not visited[nx][ny][ns]:
                    visited[nx][ny][ns] = True
                    queue.append((nx, ny, ns))
        
        level += 1  # 步数增加
    
    # 无法收集所有钥匙
    return -1


# 测试用例
if __name__ == "__main__":
    # 测试用例1
    # 输入：grid = ["@.a..","###.#","b.A.B"]
    # 输出：8
    grid1 = ["@.a..","###.#","b.A.B"]
    result1 = shortestPathAllKeys(grid1)
    print(f"测试用例1结果: {result1}")  # 期望输出: 8
    
    # 测试用例2
    # 输入：grid = ["@..aA","..B#.","....b"]
    # 输出：6
    grid2 = ["@..aA","..B#.","....b"]
    result2 = shortestPathAllKeys(grid2)
    print(f"测试用例2结果: {result2}")  # 期望输出: 6
    
    # 测试用例3
    # 输入：grid = ["@Aa"]
    # 输出：-1
    grid3 = ["@Aa"]
    result3 = shortestPathAllKeys(grid3)
    print(f"测试用例3结果: {result3}")  # 期望输出: -1