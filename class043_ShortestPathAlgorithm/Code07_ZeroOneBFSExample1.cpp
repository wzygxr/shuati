// 01-BFS练习题1：迷宫最短路径
// 给定一个n*m的迷宫，其中：
// '.' 表示可以通行的空地
// '#' 表示墙，无法通行
// 'S' 表示起点
// 'G' 表示终点
// 每次可以向上下左右四个方向移动，每步耗时1。
// 现在你有一个魔法技能，可以将任意一个'#'变为'.'，使用这个技能耗时1。
// 求从S到G的最短时间。
// 测试链接: https://atcoder.jp/contests/abc176/tasks/abc176_d
// 
// 算法思路：
// 这是一道典型的01-BFS问题。
// 在普通的BFS中，所有边的权重都是1，而在01-BFS中，边的权重只能是0或1。
// 我们使用双端队列(deque)来实现：
// 1. 当通过权重为0的边移动时，将新状态添加到队列前端
// 2. 当通过权重为1的边移动时，将新状态添加到队列后端
// 这样可以保证队列中的元素按距离单调递增排列。
//
// 具体实现：
// 1. 使用状态(x, y, magic)表示当前位置和是否使用过魔法
// 2. 普通移动（从'.'到'.'或到'G'）权重为0
// 3. 使用魔法技能移动（从任意位置到'#'并将其变为'.'）权重为1
// 4. 使用双端队列存储待处理的状态
// 5. 权重为0的边添加到队首，权重为1的边添加到队尾
//
// 时间复杂度：O(N * M)
// 空间复杂度：O(N * M)
//
// 相关题目链接：
// 1. AtCoder ABC176 D - Wizard in Maze - https://atcoder.jp/contests/abc176/tasks/abc176_d
// 2. Codeforces 590C Three States - https://codeforces.com/problemset/problem/590/C
// 3. UVA 11573 Ocean Currents - https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=27&page=show_problem&problem=2620
// 4. SPOJ KATHTHI - https://www.spoj.com/problems/KATHTHI/
// 5. LeetCode 542. 01 Matrix - https://leetcode.cn/problems/01-matrix/
// 6. 洛谷 P4568 飞行路线 - https://www.luogu.com.cn/problem/P4568
// 7. HDU 5037 Frog - https://acm.hdu.edu.cn/showproblem.php?pid=5037
// 8. 牛客 NC50522 跳楼机 - https://ac.nowcoder.com/acm/problem/50522
// 9. ZOJ 3808 ZOJ3808 - https://zoj.pintia.cn/problem-sets/91827364500/problems/91827367908
// 10. POJ 3663 Costume Party - http://poj.org/problem?id=3663
// 11. 51Nod 1459 迷宫游戏 - https://www.51nod.com/Challenge/Problem.html#problemId=1459
// 12. 洛谷 P1379 八数码难题 - https://www.luogu.com.cn/problem/P1379
// 13. LeetCode 773. Sliding Puzzle - https://leetcode.cn/problems/sliding-puzzle/
// 14. Codeforces 1063B Labyrinth - https://codeforces.com/problemset/problem/1063/B
// 15. AtCoder ABC077 C - Snuke Coloring - https://atcoder.jp/contests/abc077/tasks/arc084_a

// 由于编译环境限制，使用基本C++语法实现

const int MAXN = 1001;
const int INF = 2147483647;

int n, m;
char maze[MAXN][MAXN];
int dist[MAXN][MAXN][2];
bool visited[MAXN][MAXN][2];

// 四个方向：上下左右
int dx[] = {-1, 1, 0, 0};
int dy[] = {0, 0, -1, 1};

// 起点和终点坐标
int sx, sy, gx, gy;

// 使用基本数组模拟双端队列
// 分别存储x, y, d, magic四个字段
int dq_x[2000000];     // 存储x坐标
int dq_y[2000000];     // 存储y坐标
int dq_d[2000000];     // 存储距离
int dq_magic[2000000]; // 存储魔法使用状态
int front, rear;       // 队列的前后指针

// 向队首添加元素
void push_front(int x, int y, int d, int magic) {
    front--;
    dq_x[front] = x;
    dq_y[front] = y;
    dq_d[front] = d;
    dq_magic[front] = magic;
}

// 向队尾添加元素
void push_back(int x, int y, int d, int magic) {
    dq_x[rear] = x;
    dq_y[rear] = y;
    dq_d[rear] = d;
    dq_magic[rear] = magic;
    rear++;
}

// 从队首取出元素
void pop_front(int& x, int& y, int& d, int& magic) {
    x = dq_x[front];
    y = dq_y[front];
    d = dq_d[front];
    magic = dq_magic[front];
    front++;
}

// 检查队列是否为空
bool empty() {
    return front >= rear;
}

// 01-BFS实现
int bfs01() {
    // 初始化距离数组为无穷大
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < m; j++) {
            dist[i][j][0] = INF;
            dist[i][j][1] = INF;
            visited[i][j][0] = false;
            visited[i][j][1] = false;
        }
    }
    
    // 初始化双端队列
    front = 1000000;
    rear = 1000000;
    
    // 起点入队，距离为0，未使用魔法
    dist[sx][sy][0] = 0;
    push_front(sx, sy, 0, 0);
    
    // 当双端队列不为空时，继续处理
    while (!empty()) {
        // 从队首取出状态（距离最小的状态）
        int x, y, d, magic;
        pop_front(x, y, d, magic);
        
        // 如果已经访问过，跳过（避免重复处理）
        if (visited[x][y][magic]) {
            continue;
        }
        
        // 标记为已访问
        visited[x][y][magic] = true;
        
        // 到达终点，返回距离
        if (x == gx && y == gy) {
            return d;
        }
        
        // 四个方向扩展
        for (int i = 0; i < 4; i++) {
            int nx = x + dx[i];
            int ny = y + dy[i];
            
            // 检查边界，如果超出边界则跳过
            if (nx < 0 || nx >= n || ny < 0 || ny >= m) {
                continue;
            }
            
            // 普通移动（到空地或终点）
            if (maze[nx][ny] == '.' || maze[nx][ny] == 'G') {
                // 如果未访问且距离更短，则更新距离并添加到队列
                if (!visited[nx][ny][magic] && d < dist[nx][ny][magic]) {
                    dist[nx][ny][magic] = d;
                    // 权重为0的边，添加到队首（保证队列按距离单调递增）
                    push_front(nx, ny, d, magic);
                }
            }
            // 使用魔法技能移动（到墙且未使用过魔法）
            else if (maze[nx][ny] == '#' && magic == 0) {
                // 如果未访问且距离更短，则更新距离并添加到队列
                if (!visited[nx][ny][1] && d + 1 < dist[nx][ny][1]) {
                    dist[nx][ny][1] = d + 1;
                    // 权重为1的边，添加到队尾
                    push_back(nx, ny, d + 1, 1);
                }
            }
        }
    }
    
    // 无法到达终点，返回-1
    return -1;
}

// 由于编译环境限制，这里不提供main函数
// 在实际使用中，需要根据具体环境提供输入输出方式