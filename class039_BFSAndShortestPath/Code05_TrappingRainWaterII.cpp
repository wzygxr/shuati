// 二维接雨水
// 给你一个 m * n 的矩阵，其中的值均为非负整数，代表二维高度图每个单元的高度
// 请计算图中形状最多能接多少体积的雨水。
// 测试链接 : https://leetcode.cn/problems/trapping-rain-water-ii/
// 
// 算法思路：
// 这是一个使用优先队列的BFS问题
// 从边界开始，因为边界无法存储雨水
// 使用优先队列（最小堆）维护当前所有边界点中高度最低的点
// 每次取出高度最低的点，检查其相邻点
// 如果相邻点未访问过，计算该点能存储的雨水量
// 雨水量 = max(当前点高度, 相邻点高度) - 相邻点实际高度
// 将相邻点加入优先队列，高度为max(当前点高度, 相邻点高度)
// 
// 时间复杂度：O(m * n * log(m * n))，其中m和n分别是矩阵的行数和列数
// 空间复杂度：O(m * n)，用于存储访问状态和优先队列
// 
// 工程化考量：
// 1. 使用数组模拟优先队列
// 2. 使用visited数组记录访问状态
// 3. 从边界开始处理，确保正确计算雨水量

#define MAXN 110
int heap[MAXN * MAXN][3];  // 优先队列，存储[高度, 行, 列]
int heap_size;             // 堆大小
int visited[MAXN][MAXN];   // 访问状态
int move[5] = {-1, 0, 1, 0, -1};  // 四个方向的移动

// 向最小堆中添加元素
void heap_push(int h, int r, int c) {
    heap[heap_size][0] = h;
    heap[heap_size][1] = r;
    heap[heap_size][2] = c;
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

// 使用优先队列的BFS解法
int trapRainWater(int** height, int heightSize, int* heightColSize) {
    if (heightSize == 0 || heightColSize[0] == 0) {
        return 0;
    }
    
    int n = heightSize;
    int m = heightColSize[0];
    
    // 初始化
    heap_size = 0;
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < m; j++) {
            visited[i][j] = 0;
        }
    }
    
    // 将边界点加入优先队列
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < m; j++) {
            // 边界点
            if (i == 0 || i == n - 1 || j == 0 || j == m - 1) {
                heap_push(height[i][j], i, j);
                visited[i][j] = 1;
            }
        }
    }
    
    int ans = 0;
    int result[3];
    while (heap_size > 0) {
        // 取出高度最低的点
        heap_pop(result);
        int h = result[0];
        int r = result[1];
        int c = result[2];
        
        // 累加雨水量
        ans += h - height[r][c];
        
        // 检查四个方向的相邻点
        for (int i = 0; i < 4; i++) {
            int nr = r + move[i];
            int nc = c + move[i + 1];
            // 检查边界和是否已访问
            if (nr >= 0 && nr < n && nc >= 0 && nc < m && !visited[nr][nc]) {
                // 新点的水位线是max(当前点水位线, 新点高度)
                int new_height = (height[nr][nc] > h) ? height[nr][nc] : h;
                heap_push(new_height, nr, nc);
                visited[nr][nc] = 1;
            }
        }
    }
    
    return ans;
}