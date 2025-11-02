package class062;

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
public class Code15_MatrixDistance {

    public static int MAXN = 1001;
    public static int MAXM = 1001;
    
    // 队列，存储坐标 [x, y]
    public static int[][] queue = new int[MAXN * MAXM][2];
    public static int l, r;
    
    // 距离矩阵，记录每个点到最近的1的距离
    public static int[][] dist;
    
    // 方向数组：上、右、下、左
    public static int[] move = new int[] {-1, 0, 1, 0, -1};
    
    // 主方法，计算矩阵距离
    public static int[][] matrixDistance(int[][] matrix) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return new int[0][0];
        }
        
        int n = matrix.length;
        int m = matrix[0].length;
        dist = new int[n][m];
        l = r = 0;
        
        // 初始化：将所有的1加入队列，并设置距离为0
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (matrix[i][j] == 1) {
                    queue[r][0] = i;
                    queue[r++][1] = j;
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
            int y = queue[l++][1];
            
            // 向四个方向扩展
            for (int k = 0; k < 4; k++) {
                int nx = x + move[k];
                int ny = y + move[k + 1];
                
                // 检查边界和是否未访问
                if (nx >= 0 && nx < n && ny >= 0 && ny < m && dist[nx][ny] == -1) {
                    dist[nx][ny] = dist[x][y] + 1;
                    queue[r][0] = nx;
                    queue[r++][1] = ny;
                }
            }
        }
        
        return dist;
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试用例
        int[][] matrix = {
            {0, 0, 0, 1},
            {0, 0, 1, 1},
            {0, 1, 1, 0}
        };
        
        int[][] result = matrixDistance(matrix);
        
        // 打印结果
        for (int[] row : result) {
            for (int d : row) {
                System.out.print(d + " ");
            }
            System.out.println();
        }
        // 预期输出：
        // 3 2 1 0 
        // 2 1 0 0 
        // 1 0 0 1 
    }
}