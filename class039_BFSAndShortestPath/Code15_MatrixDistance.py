# 矩阵距离问题
# 题目描述：给定一个0-1矩阵，求每个0到最近的1的曼哈顿距离
# 这是一个典型的多源BFS问题
# 思路：正难则反，从所有的1同时开始BFS，这样每个0第一次被访问时就是到最近1的最短距离
# 
# 时间复杂度：O(n * m)，其中n和m分别是矩阵的行数和列数，每个格子最多被访问一次
# 空间复杂度：O(n * m)，用于存储队列、访问状态和距离矩阵
# 
# 工程化考量：
# 1. 异常处理：检查输入是否为空
# 2. 边界情况：全为0或全为1的情况
# 3. 优化：使用距离矩阵直接记录距离，避免重复计算
import collections

def matrix_distance(matrix):
    """
    计算矩阵中每个0到最近的1的曼哈顿距离
    
    参数：
        matrix: 二维整数数组，包含0和1
    
    返回：
        二维整数数组，表示每个位置到最近的1的距离
    """
    if not matrix or not matrix[0]:
        return []
    
    n = len(matrix)
    m = len(matrix[0])
    # 初始化距离矩阵，-1表示未访问
    dist = [[-1 for _ in range(m)] for _ in range(n)]
    queue = collections.deque()
    
    # 方向数组：上、右、下、左
    moves = [(-1, 0), (0, 1), (1, 0), (0, -1)]
    
    # 初始化：将所有的1加入队列，并设置距离为0
    for i in range(n):
        for j in range(m):
            if matrix[i][j] == 1:
                queue.append((i, j))
                dist[i][j] = 0
    
    # 多源BFS
    while queue:
        x, y = queue.popleft()
        
        # 向四个方向扩展
        for dx, dy in moves:
            nx = x + dx
            ny = y + dy
            
            # 检查边界和是否未访问
            if 0 <= nx < n and 0 <= ny < m and dist[nx][ny] == -1:
                dist[nx][ny] = dist[x][y] + 1
                queue.append((nx, ny))
    
    return dist

# 测试代码
if __name__ == "__main__":
    # 测试用例
    matrix = [
        [0, 0, 0, 1],
        [0, 0, 1, 1],
        [0, 1, 1, 0]
    ]
    
    result = matrix_distance(matrix)
    
    # 打印结果
    for row in result:
        print(' '.join(map(str, row)))
    # 预期输出：
    # 3 2 1 0
    # 2 1 0 0
    # 1 0 0 1