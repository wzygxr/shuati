// 网格中的最小路径和
// 题目描述：给定一个m*n的网格，每个格子有一个非负整数，从左上角出发，每次只能向右或向下移动一步，求到达右下角的最小路径和
// LeetCode题目链接：https://leetcode.cn/problems/minimum-path-sum/
// 
// 算法思路：
// 这道题可以用动态规划解决，但这里我们使用优先队列BFS（Dijkstra算法）来解决
// 虽然对于这道题来说动态规划更优，但这是展示优先队列BFS在网格问题中应用的好例子
// 
// 时间复杂度：O(m*n log(m*n))，其中m和n分别是网格的行数和列数，每个格子最多入队一次，每次堆操作的复杂度是log(m*n)
// 空间复杂度：O(m*n)，用于存储距离矩阵和优先队列
// 
// 工程化考量：
// 1. 异常处理：检查输入是否为空
// 2. 边界情况：处理1x1的网格
// 3. 优化：使用距离矩阵记录到达每个格子的最小路径和，避免重复计算

#define MAXN 205
#define INF 0x3f3f3f3f

// 简单的优先队列实现（最小堆）
int heap[MAXN * MAXN][3];  // 存储 [当前路径和, x坐标, y坐标]
int heap_size;

// 向最小堆中添加元素
void heap_push(int sum, int x, int y) {
    heap[heap_size][0] = sum;
    heap[heap_size][1] = x;
    heap[heap_size][2] = y;
    heap_size++;
    
    // 向上调整
    int i = heap_size - 1;
    while (i > 0) {
        int parent = (i - 1) / 2;
        if (heap[parent][0] <= heap[i][0]) break;
        // 交换
        int temp[3];
        temp[0] = heap[parent][0];
        temp[1] = heap[parent][1];
        temp[2] = heap[parent][2];
        heap[parent][0] = heap[i][0];
        heap[parent][1] = heap[i][1];
        heap[parent][2] = heap[i][2];
        heap[i][0] = temp[0];
        heap[i][1] = temp[1];
        heap[i][2] = temp[2];
        i = parent;
    }
}

// 从最小堆中取出最小元素
void heap_pop(int* result) {
    result[0] = heap[0][0];
    result[1] = heap[0][1];
    result[2] = heap[0][2];
    
    heap[0][0] = heap[heap_size-1][0];
    heap[0][1] = heap[heap_size-1][1];
    heap[0][2] = heap[heap_size-1][2];
    heap_size--;
    
    // 向下调整
    int i = 0;
    while (true) {
        int smallest = i;
        int left = 2 * i + 1;
        int right = 2 * i + 2;
        
        if (left < heap_size && heap[left][0] < heap[smallest][0])
            smallest = left;
        if (right < heap_size && heap[right][0] < heap[smallest][0])
            smallest = right;
        
        if (smallest == i) break;
        
        // 交换
        int temp[3];
        temp[0] = heap[smallest][0];
        temp[1] = heap[smallest][1];
        temp[2] = heap[smallest][2];
        heap[smallest][0] = heap[i][0];
        heap[smallest][1] = heap[i][1];
        heap[smallest][2] = heap[i][2];
        heap[i][0] = temp[0];
        heap[i][1] = temp[1];
        heap[i][2] = temp[2];
        i = smallest;
    }
}

// 计算网格中的最小路径和
int minPathSum(int** grid, int gridSize, int* gridColSize) {
    if (grid == 0 || gridSize == 0 || gridColSize[0] == 0) {
        return 0;
    }
    
    int m = gridSize;
    int n = gridColSize[0];
    
    // 特殊情况处理：如果网格只有一个格子
    if (m == 1 && n == 1) {
        return grid[0][0];
    }
    
    // 初始化距离矩阵，dist[i][j]表示从起点(0,0)到(i,j)的最小路径和
    int dist[MAXN][MAXN];
    int i, j;
    for (i = 0; i < m; i++) {
        for (j = 0; j < n; j++) {
            dist[i][j] = INF;
        }
    }
    dist[0][0] = grid[0][0];
    
    // 初始化优先队列
    heap_size = 0;
    heap_push(grid[0][0], 0, 0);
    
    // 定义两个方向：右、下（因为只能向右或向下移动）
    int directions[2][2] = {{0, 1}, {1, 0}};
    
    while (heap_size > 0) {
        int current[3];
        heap_pop(current);
        int currentSum = current[0];
        int x = current[1];
        int y = current[2];
        
        // 如果到达终点，返回当前路径和
        if (x == m - 1 && y == n - 1) {
            return currentSum;
        }
        
        // 如果当前路径和大于已知的最小路径和，跳过（因为已经找到了更优的路径）
        if (currentSum > dist[x][y]) {
            continue;
        }
        
        // 尝试所有可能的移动方向
        for (i = 0; i < 2; i++) {
            int nx = x + directions[i][0];
            int ny = y + directions[i][1];
            
            // 检查边界条件
            if (nx >= 0 && nx < m && ny >= 0 && ny < n) {
                int newSum = currentSum + grid[nx][ny];
                // 如果找到更优的路径，更新距离并加入队列
                if (newSum < dist[nx][ny]) {
                    dist[nx][ny] = newSum;
                    heap_push(newSum, nx, ny);
                }
            }
        }
    }
    
    // 正常情况下不会到达这里，因为题目保证存在至少一条路径
    return -1;
}