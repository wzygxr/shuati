// Switch the Lamp On
// 题目链接: https://www.luogu.com.cn/problem/P4667
// 
// 算法思路：
// 这是一个0-1 BFS问题
// 将网格看作图，每个交点是一个节点
// 如果两个相邻块的方向一致，移动到下一个交点不需要转换，边权为0
// 如果两个相邻块的方向不一致，移动到下一个交点需要转换，边权为1
// 使用双端队列，权值为0的节点放在队首，权值为1的节点放在队尾
// 
// 时间复杂度：O(n * m)，其中n和m分别是网格的行数和列数
// 空间复杂度：O(n * m)，用于存储距离数组和队列

#define MAXN 1005
int queue[MAXN * MAXN][2];  // 双端队列，存储坐标 [x, y]
int head, tail;             // 队列头尾指针

// 0-1 BFS解法
int minSwitches(char** grid, int n, int m) {
    // 特殊情况：起点和终点重合
    if (n == 1 && m == 1) {
        return 0;
    }
    
    // 四个方向的移动：
    // 0: 上 (连接当前点和上方交点)
    // 1: 右 (连接当前点和右方交点)
    // 2: 下 (连接当前点和下方交点)
    // 3: 左 (连接当前点和左方交点)
    int dx[4] = {-1, 0, 1, 0};
    int dy[4] = {0, 1, 0, -1};
    
    // distance[i][j]表示从起点(0,0)到交点(i,j)的最小转换次数
    int distance[MAXN][MAXN];
    int i, j;
    for (i = 0; i <= n; i++) {
        for (j = 0; j <= m; j++) {
            distance[i][j] = MAXN * MAXN;
        }
    }
    
    // 初始化双端队列
    head = tail = 0;
    queue[tail][0] = 0;
    queue[tail++][1] = 0;
    distance[0][0] = 0;
    
    while (head < tail) {
        // 从队首取出节点
        int x = queue[head][0];
        int y = queue[head++][1];
        
        // 如果到达终点
        if (x == n && y == m) {
            return distance[x][y];
        }
        
        // 向四个方向扩展
        for (i = 0; i < 4; i++) {
            int nx = x + dx[i];
            int ny = y + dy[i];
            
            // 检查边界
            if (nx >= 0 && nx <= n && ny >= 0 && ny <= m) {
                // 计算权重
                int weight = 1;
                // 根据当前位置和移动方向判断是否需要转换
                if (i == 0 && x > 0 && grid[x - 1][y] == '\\') {
                    weight = 0;
                } else if (i == 1 && y < m && grid[x][y] == '/') {
                    weight = 0;
                } else if (i == 2 && x < n && grid[x][y] == '\\') {
                    weight = 0;
                } else if (i == 3 && y > 0 && grid[x][y - 1] == '/') {
                    weight = 0;
                }
                
                // 如果新路径更优
                if (distance[x][y] + weight < distance[nx][ny]) {
                    distance[nx][ny] = distance[x][y] + weight;
                    // 根据权重决定放在队首还是队尾
                    if (weight == 0) {
                        // 权重为0，放在队首
                        for (j = tail; j > head; j--) {
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