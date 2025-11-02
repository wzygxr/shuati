package class143;

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

/*
 * 算法思路：
 * 这是一道典型的01-BFS问题。
 * 在普通的BFS中，所有边的权重都是1，而在01-BFS中，边的权重只能是0或1。
 * 我们使用双端队列(deque)来实现：
 * 1. 当通过权重为0的边移动时，将新状态添加到队列前端
 * 2. 当通过权重为1的边移动时，将新状态添加到队列后端
 * 这样可以保证队列中的元素按距离单调递增排列。
 * 
 * 对于本题：
 * 1. 普通移动（从'.'到'.'）权重为0
 * 2. 使用魔法技能移动（从任意位置到'#'并将其变为'.'）权重为1
 * 
 * 时间复杂度：O(N * M)
 * 空间复杂度：O(N * M)
 * 
 * 示例：
 * 输入：
 * 3 3
 * S..
 * .#.
 * ..G
 * 输出：1
 * 
 * 输入：
 * 1 3
 * S#G
 * 输出：2
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StreamTokenizer;
import java.util.ArrayDeque;
import java.util.Arrays;

public class Code07_ZeroOneBFSExample1 {

    // 最大行列数
    public static int MAXN = 1001;
    
    // 迷宫行数和列数
    public static int n, m;
    
    // 迷宫地图
    public static char[][] maze = new char[MAXN][MAXN];
    
    // 距离数组，dist[i][j][0]表示不使用魔法到达(i,j)的最短距离
    // dist[i][j][1]表示使用魔法到达(i,j)的最短距离
    public static int[][][] dist = new int[MAXN][MAXN][2];
    
    // 访问标记数组
    public static boolean[][][] visited = new boolean[MAXN][MAXN][2];
    
    // 四个方向：上下左右
    public static int[] dx = {-1, 1, 0, 0};
    public static int[] dy = {0, 0, -1, 1};
    
    // 起点和终点坐标
    public static int sx, sy, gx, gy;
    
    // 01-BFS实现
    public static int bfs01() {
        // 初始化距离数组为无穷大
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                Arrays.fill(dist[i][j], Integer.MAX_VALUE);
                Arrays.fill(visited[i][j], false);
            }
        }
        
        // 双端队列，用于存储待处理的状态
        ArrayDeque<State> deque = new ArrayDeque<>();
        
        // 起点入队，距离为0，未使用魔法
        dist[sx][sy][0] = 0;
        deque.addFirst(new State(sx, sy, 0, 0));
        
        // 当双端队列不为空时，继续处理
        while (!deque.isEmpty()) {
            // 从队首取出状态（距离最小的状态）
            State curr = deque.pollFirst();
            int x = curr.x;
            int y = curr.y;
            int d = curr.dist;
            int magic = curr.magic;
            
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
                        deque.addFirst(new State(nx, ny, d, magic));
                    }
                }
                // 使用魔法技能移动（到墙且未使用过魔法）
                else if (maze[nx][ny] == '#' && magic == 0) {
                    // 如果未访问且距离更短，则更新距离并添加到队列
                    if (!visited[nx][ny][1] && d + 1 < dist[nx][ny][1]) {
                        dist[nx][ny][1] = d + 1;
                        // 权重为1的边，添加到队尾
                        deque.addLast(new State(nx, ny, d + 1, 1));
                    }
                }
            }
        }
        
        // 无法到达终点，返回-1
        return -1;
    }
    
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(System.out));
        
        // 读取行列数
        String[] parts = br.readLine().split(" ");
        n = Integer.parseInt(parts[0]);
        m = Integer.parseInt(parts[1]);
        
        // 读取迷宫并找到起点和终点
        for (int i = 0; i < n; i++) {
            String line = br.readLine();
            for (int j = 0; j < m; j++) {
                maze[i][j] = line.charAt(j);
                // 记录起点坐标
                if (maze[i][j] == 'S') {
                    sx = i;
                    sy = j;
                } 
                // 记录终点坐标
                else if (maze[i][j] == 'G') {
                    gx = i;
                    gy = j;
                }
            }
        }
        
        // 计算结果并输出
        out.println(bfs01());
        out.flush();
        out.close();
        br.close();
    }
    
    // 状态类，表示在迷宫中的一个状态
    static class State {
        int x, y, dist, magic;
        
        State(int x, int y, int dist, int magic) {
            this.x = x;
            this.y = y;
            this.dist = dist;
            this.magic = magic; // 0表示未使用魔法，1表示已使用魔法
        }
    }
}