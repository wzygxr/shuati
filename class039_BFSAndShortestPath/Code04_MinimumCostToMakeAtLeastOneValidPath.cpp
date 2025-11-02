// 使网格图至少有一条有效路径的最小代价
// 给你一个 m * n 的网格图 grid 。 grid 中每个格子都有一个数字
// 对应着从该格子出发下一步走的方向。 grid[i][j] 中的数字可能为以下几种情况：
// 1 ，下一步往右走，也就是你会从 grid[i][j] 走到 grid[i][j + 1]
// 2 ，下一步往左走，也就是你会从 grid[i][j] 走到 grid[i][j - 1]
// 3 ，下一步往下走，也就是你会从 grid[i][j] 走到 grid[i + 1][j]
// 4 ，下一步往上走，也就是你会从 grid[i][j] 走到 grid[i - 1][j]
// 注意网格图中可能会有 无效数字 ，因为它们可能指向 grid 以外的区域
// 一开始，你会从最左上角的格子 (0,0) 出发
// 我们定义一条 有效路径 为从格子 (0,0) 出发，每一步都顺着数字对应方向走
// 最终在最右下角的格子 (m - 1, n - 1) 结束的路径
// 有效路径 不需要是最短路径
// 你可以花费1的代价修改一个格子中的数字，但每个格子中的数字 只能修改一次
// 请你返回让网格图至少有一条有效路径的最小代价
// 测试链接 : https://leetcode.cn/problems/minimum-cost-to-make-at-least-one-valid-path-in-a-grid/
// 
// 算法思路：
// 这也是一个0-1 BFS问题
// 将网格看作图，每个单元格是一个节点
// 如果按照原有方向移动，边权为0（不需要修改）
// 如果改变方向移动，边权为1（需要修改，花费1的代价）
// 使用双端队列，权值为0的节点放在队首，权值为1的节点放在队尾
// 
// 时间复杂度：O(m * n)，其中m和n分别是网格的行数和列数
// 空间复杂度：O(m * n)，用于存储距离数组和队列
// 
// 工程化考量：
// 1. 使用数组模拟双端队列
// 2. 使用distance数组记录到每个点的最小修改次数
// 3. 通过比较新路径和已有路径的权重来决定是否更新

#define MAXN 100005
int queue[MAXN][2];  // 双端队列，存储坐标 [x, y]
int head, tail;      // 队列头尾指针

// 0-1 BFS解法
int minCost(int** grid, int gridSize, int* gridColSize) {
    // 格子的数值对应的方向:
    // 1 右
    // 2 左
    // 3 下
    // 4 上
    int move[5][2] = {{0, 0}, {0, 1}, {0, -1}, {1, 0}, {-1, 0}};
    int m = gridSize;
    int n = gridColSize[0];
    
    // distance[i][j]表示从起点(0,0)到(i,j)的最小修改次数
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
        
        // 尝试四个方向
        for (int i = 1; i <= 4; i++) {
            int nx = x + move[i][0];
            int ny = y + move[i][1];
            // 如果当前格子的方向与尝试的方向一致，则不需要修改，权重为0；否则需要修改，权重为1
            int weight = (grid[x][y] != i) ? 1 : 0;
            int idx = nx * n + ny;
            int curr_idx = x * n + y;
            
            // 检查边界和是否能找到更优路径
            if (nx >= 0 && nx < m && ny >= 0 && ny < n && distance[curr_idx] + weight < distance[idx]) {
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
    return -1;
}