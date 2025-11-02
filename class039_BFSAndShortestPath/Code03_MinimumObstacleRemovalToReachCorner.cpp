// 到达角落需要移除障碍物的最小数目
// 给你一个下标从 0 开始的二维整数数组 grid ，数组大小为 m x n
// 每个单元格都是两个值之一：
// 0 表示一个 空 单元格，
// 1 表示一个可以移除的 障碍物
// 你可以向上、下、左、右移动，从一个空单元格移动到另一个空单元格。
// 现在你需要从左上角 (0, 0) 移动到右下角 (m - 1, n - 1) 
// 返回需要移除的障碍物的最小数目
// 测试链接 : https://leetcode.cn/problems/minimum-obstacle-removal-to-reach-corner/
// 
// 算法思路：
// 这是一个典型的0-1 BFS问题
// 将网格看作图，每个单元格是一个节点
// 如果移动到空单元格(0)，边权为0
// 如果移动到障碍物单元格(1)，边权为1（需要移除障碍物）
// 使用双端队列，权值为0的节点放在队首，权值为1的节点放在队尾
// 
// 时间复杂度：O(m * n)，其中m和n分别是网格的行数和列数
// 空间复杂度：O(m * n)，用于存储距离数组和队列
// 
// 工程化考量：
// 1. 使用数组模拟双端队列
// 2. 使用distance数组记录到每个点的最小移除障碍物数目
// 3. 通过比较新路径和已有路径的权重来决定是否更新

#define MAXN 100005
int queue[MAXN][2];  // 双端队列，存储坐标 [x, y]
int head, tail;      // 队列头尾指针

// 0-1 BFS解法
int minimumObstacles(int** grid, int gridSize, int* gridColSize) {
    // 四个方向的移动：上、右、下、左
    int move[5] = {-1, 0, 1, 0, -1};
    int m = gridSize;
    int n = gridColSize[0];
    
    // distance[i][j]表示从起点(0,0)到(i,j)需要移除的障碍物最小数目
    int distance[MAXN];
    for (int i = 0; i < m * n; i++) {
        distance[i] = MAXN;
    }
    
    // 初始化双端队列
    head = tail = 0;
    queue[tail][0] = 0;
    queue[tail++][1] = 0;
    distance[0] = 0;
    
    while (head < tail) {
        // 从队首取出节点
        int x = queue[head][0];
        int y = queue[head++][1];
        
        // 如果到达终点
        if (x == m - 1 && y == n - 1) {
            return distance[x * n + y];
        }
        
        // 向四个方向扩展
        for (int i = 0; i < 4; i++) {
            int nx = x + move[i];
            int ny = y + move[i + 1];
            // 检查边界
            if (nx >= 0 && nx < m && ny >= 0 && ny < n) {
                // 计算移动到新位置需要增加的权重（0或1）
                int weight = grid[nx][ny];
                int idx = nx * n + ny;
                int curr_idx = x * n + y;
                // 如果新路径更优
                if (distance[curr_idx] + weight < distance[idx]) {
                    distance[idx] = distance[curr_idx] + weight;
                    // 根据权重决定放在队首还是队尾
                    if (weight == 0) {
                        // 权重为0，放在队首
                        for (int j = tail; j > head; j--) {
                            queue[j][0] = queue[j-1][0];
                            queue[j][1] = queue[j-1][1];
                        }
                        queue[head][0] = nx;
                        queue[head][1] = ny;
                        tail++;
                    } else {
                        // 权重为1，放在队尾
                        queue[tail][0] = nx;
                        queue[tail++][1] = ny;
                    }
                }
            }
        }
    }
    return -1;
}