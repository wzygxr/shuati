/**
 * LeetCode 542 - 01矩阵
 * 题目描述：
 * 给定一个由 0 和 1 组成的矩阵，找出每个元素到最近的 0 的距离。
 * 两个相邻元素间的距离为 1。
 * 
 * 算法：多源BFS
 * 时间复杂度：O(m * n)，其中m和n分别是矩阵的行数和列数
 * 空间复杂度：O(m * n)
 * 
 * 相关题目链接：
 * 1. LeetCode 542. 01 矩阵 - https://leetcode.cn/problems/01-matrix/
 * 2. LeetCode 994. 腐烂的橘子 - https://leetcode.cn/problems/rotting-oranges/
 * 3. LeetCode 286. 墙与门 - https://leetcode.cn/problems/walls-and-gates/
 * 4. LeetCode 317. 离建筑物最近的距离 - https://leetcode.cn/problems/shortest-distance-from-all-buildings/
 * 5. LeetCode 417. 太平洋大西洋水流问题 - https://leetcode.cn/problems/pacific-atlantic-water-flow/
 * 6. LeetCode 529. 扫雷游戏 - https://leetcode.cn/problems/minesweeper/
 * 7. LeetCode 695. 岛屿的最大面积 - https://leetcode.cn/problems/max-area-of-island/
 * 8. LeetCode 733. 图像渲染 - https://leetcode.cn/problems/flood-fill/
 * 9. LeetCode 773. 滑动谜题 - https://leetcode.cn/problems/sliding-puzzle/
 * 10. LeetCode 934. 最短的桥 - https://leetcode.cn/problems/shortest-bridge/
 * 11. 洛谷 P1162 走迷宫 - https://www.luogu.com.cn/problem/P1162
 * 12. 洛谷 P1443 马的遍历 - https://www.luogu.com.cn/problem/P1443
 * 13. POJ 3620 Avoid The Lakes - http://poj.org/problem?id=3620
 * 14. HDU 1241 Oil Deposits - https://acm.hdu.edu.cn/showproblem.php?pid=1241
 * 15. AtCoder ABC007 C - 幅優先探索 - https://atcoder.jp/contests/abc007/tasks/abc007_3
 */

// 由于编译环境限制，使用基本C++语法实现

const int MAXN = 10000;
const int INF = 2147483647;

// 使用基本数组模拟队列
int queue_x[MAXN];  // 存储x坐标
int queue_y[MAXN];  // 存储y坐标
int front, rear;    // 队列的前后指针

// 向队列添加元素
void queue_push(int x, int y) {
    queue_x[rear] = x;
    queue_y[rear] = y;
    rear++;
}

// 从队列取出元素
void queue_pop(int& x, int& y) {
    x = queue_x[front];
    y = queue_y[front];
    front++;
}

// 检查队列是否为空
bool queue_empty() {
    return front >= rear;
}

// 定义四个方向：上、右、下、左
int dirs[4][2] = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};

/**
 * 计算每个元素到最近的0的距离
 * 算法思路：
 * 1. 这是一个典型的多源BFS问题
 * 2. 将所有值为0的单元格作为BFS的起点，距离设为0
 * 3. 从这些起点开始，逐层向外扩展，每扩展一层距离加1
 * 4. 使用队列实现BFS，确保按距离从小到大处理
 * 
 * 具体实现：
 * 1. 初始化距离矩阵，所有位置设为INF表示未访问
 * 2. 将所有0的位置入队，距离设为0
 * 3. 使用BFS遍历，对每个位置检查四个方向的邻居
 * 4. 如果邻居未访问过，则更新其距离并入队
 * 
 * @param matrix 输入矩阵
 * @param m 矩阵行数
 * @param n 矩阵列数
 * @param dist 距离矩阵
 */
void updateMatrix(int matrix[][100], int m, int n, int dist[][100]) {
    // 初始化队列
    front = 0;
    rear = 0;
    
    // 初始化距离矩阵为INF（表示未访问）
    for (int i = 0; i < m; i++) {
        for (int j = 0; j < n; j++) {
            dist[i][j] = INF;
        }
    }
    
    // 将所有值为0的单元格入队，这些是BFS的起点
    for (int i = 0; i < m; i++) {
        for (int j = 0; j < n; j++) {
            if (matrix[i][j] == 0) {
                dist[i][j] = 0; // 0到自身的距离为0
                queue_push(i, j);
            }
        }
    }
    
    // 多源BFS遍历
    while (!queue_empty()) {
        // 从队列中取出一个位置
        int x, y;
        queue_pop(x, y);
        
        // 遍历四个方向的邻居
        for (int i = 0; i < 4; i++) {
            int nx = x + dirs[i][0];
            int ny = y + dirs[i][1];
            
            // 检查新位置是否有效且未访问过
            if (nx >= 0 && nx < m && ny >= 0 && ny < n && dist[nx][ny] == INF) {
                // 新位置的距离是当前位置的距离+1
                dist[nx][ny] = dist[x][y] + 1;
                // 将新位置入队，以便继续扩展
                queue_push(nx, ny);
            }
        }
    }
}

// 由于编译环境限制，这里不提供main函数
// 在实际使用中，需要根据具体环境提供输入输出方式