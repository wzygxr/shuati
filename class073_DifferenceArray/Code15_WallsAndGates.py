import time
from collections import deque

"""
LeetCode 286. 墙与门 (Walls and Gates)

题目描述:
你被给定一个 m x n 的二维网格 rooms。
网格中的每个元素有以下三种可能的值：
-1 表示墙壁或障碍物
0 表示一扇门
INF 表示一个空房间。我们使用值 2^31 - 1 = 2147483647 来表示 INF

你需要填充每个空房间，使其表示到最近的门的距离。如果不可能到达门（比如周围都是墙壁），保持 INF 不变。

示例:
输入：
[[2147483647,-1,0,2147483647],
 [2147483647,2147483647,2147483647,-1],
 [2147483647,-1,2147483647,-1],
 [0,-1,2147483647,2147483647]]
输出：
[[3,-1,0,1],
 [2,2,1,-1],
 [1,-1,2,-1],
 [0,-1,3,4]]

提示:
m 和 n 的值在 [1, 250] 范围内。
rooms[i][j] 的值只能是 -1、0 或 2^31 - 1。

题目链接: https://leetcode.com/problems/walls-and-gates/

解题思路:
这个问题可以使用广度优先搜索（BFS）来解决：
1. 首先，将所有的门（值为0的单元格）加入队列
2. 然后从每个门开始，进行广度优先搜索
3. 每次搜索时，更新相邻的空房间（值为INF的单元格）的距离为当前距离+1
4. 继续搜索直到队列为空

这种方法的优点是：
- BFS能够保证第一次到达某个单元格时，得到的是到最近门的最短距离
- 从所有门同时开始BFS，可以避免重复计算

时间复杂度: O(m*n)，其中 m 和 n 分别是网格的行数和列数。
每个单元格最多被访问一次，因为一旦被访问，它的值就会被更新为一个非INF值。

空间复杂度: O(m*n)，队列在最坏情况下可能需要存储所有单元格。

这是最优解，因为我们需要至少遍历每个单元格一次，时间复杂度无法更低。
"""

# 表示空房间的常量
INF = 2147483647  # 2^31 - 1
# 表示墙壁的常量
WALL = -1
# 表示门的常量
GATE = 0

def walls_and_gates(rooms):
    """
    填充每个空房间到最近门的距离
    
    Args:
        rooms: 二维网格，表示房间、墙壁和门
    """
    # 参数校验
    if not rooms or not rooms[0]:
        return
    
    rows = len(rooms)
    cols = len(rooms[0])
    
    # 创建队列，用于BFS
    q = deque()
    
    # 首先将所有的门（值为0的单元格）加入队列
    for i in range(rows):
        for j in range(cols):
            if rooms[i][j] == GATE:
                q.append((i, j))
    
    # 定义四个方向：上、右、下、左
    directions = [(-1, 0), (0, 1), (1, 0), (0, -1)]
    
    # BFS过程
    while q:
        row, col = q.popleft()
        distance = rooms[row][col]
        
        # 探索四个方向的相邻单元格
        for dr, dc in directions:
            new_row = row + dr
            new_col = col + dc
            
            # 检查新位置是否有效，并且是一个空房间（值为INF）
            if (0 <= new_row < rows and 0 <= new_col < cols and 
                rooms[new_row][new_col] == INF):
                # 更新距离
                rooms[new_row][new_col] = distance + 1
                # 将新位置加入队列
                q.append((new_row, new_col))

def walls_and_gates_alternative(rooms):
    """
    另一种实现方式，使用更简洁的代码
    
    Args:
        rooms: 二维网格，表示房间、墙壁和门
    """
    if not rooms or not rooms[0]:
        return
    
    m = len(rooms)
    n = len(rooms[0])
    q = deque()
    
    # 添加所有门到队列
    for i in range(m):
        for j in range(n):
            if rooms[i][j] == GATE:
                q.append((i, j))
    
    # BFS
    while q:
        row, col = q.popleft()
        
        # 上
        if row > 0 and rooms[row - 1][col] == INF:
            rooms[row - 1][col] = rooms[row][col] + 1
            q.append((row - 1, col))
        # 右
        if col < n - 1 and rooms[row][col + 1] == INF:
            rooms[row][col + 1] = rooms[row][col] + 1
            q.append((row, col + 1))
        # 下
        if row < m - 1 and rooms[row + 1][col] == INF:
            rooms[row + 1][col] = rooms[row][col] + 1
            q.append((row + 1, col))
        # 左
        if col > 0 and rooms[row][col - 1] == INF:
            rooms[row][col - 1] = rooms[row][col] + 1
            q.append((row, col - 1))

def print_matrix(matrix):
    """
    打印二维数组
    
    Args:
        matrix: 要打印的二维数组
    """
    for row in matrix:
        formatted_row = []
        for cell in row:
            if cell == INF:
                formatted_row.append("INF")
            else:
                formatted_row.append(str(cell))
        print(f"[{', '.join(formatted_row)}]")
    print()

# 测试代码
def main():
    # 测试用例1
    rooms1 = [
        [INF, WALL, GATE, INF],
        [INF, INF, INF, WALL],
        [INF, WALL, INF, WALL],
        [GATE, WALL, INF, INF]
    ]
    
    print("测试用例1 - 原始矩阵:")
    print_matrix(rooms1)
    
    walls_and_gates(rooms1)
    
    print("测试用例1 - 处理后矩阵:")
    print_matrix(rooms1)
    
    # 测试用例2
    rooms2 = [
        [INF, WALL, GATE, INF],
        [INF, INF, INF, WALL],
        [INF, WALL, INF, WALL],
        [GATE, WALL, INF, INF]
    ]
    
    print("测试用例2 - 使用替代方法:")
    print_matrix(rooms2)
    
    walls_and_gates_alternative(rooms2)
    
    print("测试用例2 - 处理后矩阵:")
    print_matrix(rooms2)
    
    # 测试用例3 - 边界情况：只有门和墙
    rooms3 = [
        [GATE, WALL],
        [WALL, GATE]
    ]
    
    print("测试用例3 - 原始矩阵:")
    print_matrix(rooms3)
    
    walls_and_gates(rooms3)
    
    print("测试用例3 - 处理后矩阵:")
    print_matrix(rooms3)
    
    # 测试用例4 - 边界情况：只有一个单元格
    rooms4 = [[INF]]
    
    print("测试用例4 - 原始矩阵:")
    print_matrix(rooms4)
    
    walls_and_gates(rooms4)
    
    print("测试用例4 - 处理后矩阵:")
    print_matrix(rooms4)
    
    # 性能测试
    print("性能测试:")
    large_rooms = [[INF for _ in range(100)] for _ in range(100)]
    # 添加一些门
    for i in range(10):
        large_rooms[i * 10][i * 10] = GATE
    # 添加一些墙
    for i in range(0, 100, 5):
        for j in range(0, 100, 5):
            large_rooms[i][j] = WALL
    
    start_time = time.time()
    walls_and_gates(large_rooms)
    end_time = time.time()
    
    print(f"大矩阵处理耗时: {(end_time - start_time) * 1000:.2f}ms")
    print("大矩阵处理结果示例（左上角10x10）:")
    for i in range(10):
        row_str = []
        for j in range(10):
            if large_rooms[i][j] == INF:
                row_str.append("INF")
            elif large_rooms[i][j] == WALL:
                row_str.append("WALL")
            else:
                row_str.append(str(large_rooms[i][j]))
        print("\t".join(row_str))

if __name__ == "__main__":
    main()