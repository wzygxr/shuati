import sys
sys.setrecursionlimit(1 << 25)

# 经典TSP问题解法
def tsp(graph):
    n = len(graph)
    if n == 0:
        return 0
    
    # dp[mask][i] 表示访问了mask代表的城市集合，当前在城市i的最短路径长度
    dp = [[float('inf')] * n for _ in range(1 << n)]
    dp[1][0] = 0
    
    for mask in range(1 << n):
        for i in range(n):
            if dp[mask][i] == float('inf'):
                continue
            
            for j in range(n):
                # 如果城市j已经被访问过，跳过
                if mask & (1 << j):
                    continue
                
                # 如果从i到j有路径
                if graph[i][j] > 0:
                    new_mask = mask | (1 << j)
                    new_dist = dp[mask][i] + graph[i][j]
                    if dp[new_mask][j] > new_dist:
                        dp[new_mask][j] = new_dist
    
    # 找到访问所有城市并回到起点的最短路径
    result = float('inf')
    all_visited = (1 << n) - 1
    for i in range(n):
        if dp[all_visited][i] != float('inf') and graph[i][0] > 0:
            result = min(result, dp[all_visited][i] + graph[i][0])
    
    return result if result != float('inf') else -1

# LeetCode 980 哈密顿路径解法
def uniquePathsIII(grid):
    m, n = len(grid), len(grid[0])
    start_x = start_y = target_x = target_y = -1
    empty_count = 0
    
    for i in range(m):
        for j in range(n):
            if grid[i][j] == 0:
                empty_count += 1
            elif grid[i][j] == 1:
                start_x, start_y = i, j
            elif grid[i][j] == 2:
                target_x, target_y = i, j
    
    total_cells = empty_count + 2
    total_states = 1 << total_cells
    
    # dp[mask][pos] 表示访问了mask代表的单元格集合，当前位置为pos时的路径数量
    dp = [[0] * total_cells for _ in range(total_states)]
    
    start_pos = start_x * n + start_y
    dp[1 << start_pos][start_pos] = 1
    
    dx = [-1, 1, 0, 0]
    dy = [0, 0, -1, 1]
    
    for mask in range(total_states):
        for pos in range(total_cells):
            if dp[mask][pos] == 0:
                continue
            
            x, y = pos // n, pos % n
            
            # 尝试四个方向移动
            for d in range(4):
                nx, ny = x + dx[d], y + dy[d]
                
                # 检查边界和障碍物
                if nx < 0 or nx >= m or ny < 0 or ny >= n or grid[nx][ny] == -1:
                    continue
                
                new_pos = nx * n + ny
                # 如果新位置已经被访问过，跳过
                if mask & (1 << new_pos):
                    continue
                
                new_mask = mask | (1 << new_pos)
                dp[new_mask][new_pos] += dp[mask][pos]
    
    target_pos = target_x * n + target_y
    target_mask = (1 << total_cells) - 1
    return dp[target_mask][target_pos]

# CodeForces 165E 最大兼容数对解法
def compatibleNumbers(nums):
    n = len(nums)
    if n == 0:
        return []
    
    max_val = max(nums)
    bits = 0
    while (1 << bits) <= max_val:
        bits += 1
    
    complement = [-1] * (1 << bits)
    for i in range(n):
        complement[nums[i]] = i
    
    # SOS DP技术
    for mask in range(1 << bits):
        if complement[mask] != -1:
            sub_mask = mask
            while sub_mask > 0:
                if complement[sub_mask] == -1:
                    complement[sub_mask] = complement[mask]
                sub_mask = (sub_mask - 1) & mask
    
    result = [-1] * n
    for i in range(n):
        num = nums[i]
        complement_mask = ((1 << bits) - 1) ^ num
        if complement[complement_mask] != -1:
            result[i] = complement[complement_mask]
    
    return result

# 测试代码
if __name__ == "__main__":
    # 测试TSP问题
    graph = [
        [0, 10, 15, 20],
        [10, 0, 35, 25],
        [15, 35, 0, 30],
        [20, 25, 30, 0]
    ]
    print("TSP问题测试:")
    print("最短路径长度:", tsp(graph))
    
    # 测试LeetCode 980 哈密顿路径
    grid = [
        [1, 0, 0, 0],
        [0, 0, 0, 0],
        [0, 0, 2, -1]
    ]
    print("\nLeetCode 980 哈密顿路径测试:")
    print("路径数量:", uniquePathsIII(grid))
    
    # 测试CodeForces 165E 最大兼容数对
    nums = [3, 1, 4, 2]
    print("\nCodeForces 165E 最大兼容数对测试:")
    result = compatibleNumbers(nums)
    print("数组:", nums)
    print("结果:", result)