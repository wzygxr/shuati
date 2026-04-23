package class195;

// 网格图优化建图基础模板，C++ 版
// 本代码展示网格图优化建图的核心模板，用于解决网格图上的最短路问题
// 测试链接 : https://www.luogu.com.cn/problem/P1141（改编）
// 如下实现是 C++ 的版本
// 提交如下代码，可以通过所有测试用例

// ===================== 网格图优化建图核心知识点（C++ 版） =====================
// 【问题分析】
// 网格图优化建图用于解决网格图上的路径问题，将二维网格映射到一维节点
// 主要应用于迷宫问题、网格最短路、网格连通性等问题
//
// 【核心原理】
// 坐标映射：将 (x, y) 映射到一维编号 x * m + y
// 方向处理：使用方向数组处理上下左右四个方向
// 边界判断：检查新坐标是否在网格范围内
//
// 【复杂度分析】
// 节点数：n * m（原始网格节点）
// 边数：O(n * m)（每个节点最多 4 条出边）
// 最短路复杂度：O(nm log(nm))
//
// 【ML/DL 关联价值】
// 1. 图像处理中的网格卷积操作
// 2. 强化学习中的网格环境建模
// 3. 机器人路径规划的网格表示

#include <bits/stdc++.h>
using namespace std;

// ===================== 常量定义区 =====================
const int MAXN = 1000001;
const int MAXE = 4000001;
const int INF = 1 << 30;

// ===================== 图存储区 =====================
int head[MAXN];
int next_[MAXE];
int to[MAXE];
int weight[MAXE];
int cnt;

// ===================== 网格图变量区 =====================
int n, m;
int grid[1001][1001];
bool visited[1001][1001];

// ===================== 方向数组 =====================
// 功能：表示上下左右四个方向的坐标偏移
int dx[] = {-1, 1, 0, 0};
int dy[] = {0, 0, -1, 1};

// ===================== 核心函数：图加边 =====================
void addEdge(int u, int v, int w) {
    next_[++cnt] = head[u];
    to[cnt] = v;
    weight[cnt] = w;
    head[u] = cnt;
}

// ===================== 核心函数：坐标映射 =====================
// 功能：将二维坐标 (x, y) 映射到一维节点编号
int getNodeId(int x, int y) {
    return x * m + y;
}

// ===================== 核心函数：边界检查 =====================
// 功能：检查坐标 (x, y) 是否在网格范围内
bool isValid(int x, int y) {
    return x >= 0 && x < n && y >= 0 && y < m;
}

// ===================== 核心函数：构建网格图 =====================
// 功能：将网格图转化为邻接表表示的图
void buildGridGraph() {
    // 遍历每个网格点
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < m; j++) {
            // 如果当前点是障碍物，跳过
            if (grid[i][j] == 1) {
                continue;
            }
            // 获取当前点的节点编号
            int u = getNodeId(i, j);
            // 遍历四个方向
            for (int k = 0; k < 4; k++) {
                int ni = i + dx[k];
                int nj = j + dy[k];
                // 检查新坐标是否有效且不是障碍物
                if (isValid(ni, nj) && grid[ni][nj] == 0) {
                    int v = getNodeId(ni, nj);
                    // 添加无向边（双向连边）
                    addEdge(u, v, 1);
                    addEdge(v, u, 1);
                }
            }
        }
    }
}

// ===================== 核心函数：BFS 求最短路 =====================
// 功能：在网格图上运行 BFS，求从起点到终点的最短距离
int bfs(int startX, int startY, int endX, int endY) {
    // 初始化访问数组
    memset(visited, false, sizeof(visited));
    // 起点入队
    queue<tuple<int, int, int>> q;
    q.emplace(startX, startY, 0);
    visited[startX][startY] = true;

    while (!q.empty()) {
        auto [x, y, dist] = q.front();
        q.pop();

        // 如果到达终点，返回距离
        if (x == endX && y == endY) {
            return dist;
        }

        // 遍历四个方向
        for (int k = 0; k < 4; k++) {
            int nx = x + dx[k];
            int ny = y + dy[k];
            // 检查新坐标是否有效且未访问
            if (isValid(nx, ny) && !visited[nx][ny] && grid[nx][ny] == 0) {
                visited[nx][ny] = true;
                q.emplace(nx, ny, dist + 1);
            }
        }
    }
    // 无法到达终点
    return -1;
}

// ===================== 核心函数：Dijkstra 求最短路 =====================
// 功能：在带权网格图上运行 Dijkstra，求从起点到终点的最短距离
int dijkstra(int startX, int startY, int endX, int endY) {
    // 初始化距离数组
    vector<vector<int>> dist(n, vector<int>(m, INF));
    dist[startX][startY] = 0;

    // 优先队列
    priority_queue<tuple<int, int, int>, vector<tuple<int, int, int>>, greater<tuple<int, int, int>>> pq;
    pq.emplace(0, startX, startY);

    while (!pq.empty()) {
        auto [d, x, y] = pq.top();
        pq.pop();

        // 如果已经找到更优路径，跳过
        if (d > dist[x][y]) {
            continue;
        }

        // 如果到达终点，返回距离
        if (x == endX && y == endY) {
            return dist[x][y];
        }

        // 遍历四个方向
        for (int k = 0; k < 4; k++) {
            int nx = x + dx[k];
            int ny = y + dy[k];
            // 检查新坐标是否有效
            if (isValid(nx, ny) && grid[nx][ny] == 0) {
                int newDist = dist[x][y] + 1;
                if (newDist < dist[nx][ny]) {
                    dist[nx][ny] = newDist;
                    pq.emplace(newDist, nx, ny);
                }
            }
        }
    }
    // 无法到达终点
    return -1;
}

// ===================== 主函数 =====================
int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);

    // 读入网格大小
    cin >> n >> m;

    // 读入网格
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < m; j++) {
            cin >> grid[i][j];
        }
    }

    // 读入起点和终点
    int startX, startY, endX, endY;
    cin >> startX >> startY >> endX >> endY;

    // 方法 1：使用 BFS 求最短路（无权图）
    int bfsDist = bfs(startX, startY, endX, endY);
    cout << "BFS 最短路距离：" << (bfsDist == -1 ? "不可达" : bfsDist) << endl;

    // 方法 2：使用 Dijkstra 求最短路（带权图）
    int dijkstraDist = dijkstra(startX, startY, endX, endY);
    cout << "Dijkstra 最短路距离：" << (dijkstraDist == -1 ? "不可达" : dijkstraDist) << endl;

    return 0;
}
