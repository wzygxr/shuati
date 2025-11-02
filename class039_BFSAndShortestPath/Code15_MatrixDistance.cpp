// 矩阵距离问题
// 题目描述：给定一个0-1矩阵，求每个0到最近的1的曼哈顿距离
// 这是一个典型的多源BFS问题
// 思路：正难则反，从所有的1同时开始BFS，这样每个0第一次被访问时就是到最近1的最短距离
// 
// 时间复杂度：O(n * m)，其中n和m分别是矩阵的行数和列数，每个格子最多被访问一次
// 空间复杂度：O(n * m)，用于存储队列、访问状态和距离矩阵
// 
// 工程化考量：
// 1. 异常处理：检查输入是否为空
// 2. 边界情况：全为0或全为1的情况
// 3. 优化：使用距离矩阵直接记录距离，避免重复计算

#define MAXN 1001
#define MAXM 1001

// 队列，存储坐标 [x, y]
int queue[MAXN * MAXM][2];
int l, r;

// 距离矩阵，记录每个点到最近的1的距离
int dist[MAXN][MAXM];

// 方向数组：上、右、下、左
int move[5] = {-1, 0, 1, 0, -1};

// 主方法，计算矩阵距离
void matrixDistance(int** matrix, int n, int m, int** result) {
    if (matrix == 0 || n == 0 || m == 0) {
        return;
    }
    
    // 初始化队列和距离矩阵
    l = r = 0;
    int i, j;
    for (i = 0; i < n; i++) {
        for (j = 0; j < m; j++) {
            if (matrix[i][j] == 1) {
                queue[r][0] = i;
                queue[r][1] = j;
                r++;
                dist[i][j] = 0;
            } else {
                // 初始时0的距离设为-1表示未访问
                dist[i][j] = -1;
            }
        }
    }
    
    // 多源BFS
    while (l < r) {
        int x = queue[l][0];
        int y = queue[l][1];
        l++;
        
        // 向四个方向扩展
        for (int k = 0; k < 4; k++) {
            int nx = x + move[k];
            int ny = y + move[k + 1];
            
            // 检查边界和是否未访问
            if (nx >= 0 && nx < n && ny >= 0 && ny < m && dist[nx][ny] == -1) {
                dist[nx][ny] = dist[x][y] + 1;
                queue[r][0] = nx;
                queue[r][1] = ny;
                r++;
            }
        }
    }
    
    // 将结果复制到输出矩阵
    for (i = 0; i < n; i++) {
        for (j = 0; j < m; j++) {
            result[i][j] = dist[i][j];
        }
    }
}