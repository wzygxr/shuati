#include <iostream>
#include <vector>
#include <queue>
#include <climits>
#include <cstring>
using namespace std;

/**
 * 差分约束系统详解（形式1）：
 * 
 * 1. 问题定义：
 *    差分约束系统是一种特殊的n元一次不等式组，包含n个变量x1, x2, ..., xn
 *    和m个约束条件，每个约束条件形如 xi - xj <= ck，其中ck是常数。
 *    目标是求出一组解使得所有约束条件都满足，或者判断无解。
 * 
 * 2. 核心思想：
 *    将差分约束系统转化为图论问题。每个变量xi看作图中的一个节点，
 *    每个约束条件 xi - xj <= ck 转化为从节点j到节点i的一条权值为ck的有向边。
 *    然后通过求最短路径来得到解。
 * 
 * 3. 转化原理：
 *    差分约束 xi - xj <= ck 可以变形为 xi <= xj + ck
 *    这与最短路径中的三角不等式 dist[v] <= dist[u] + w(u,v) 非常相似
 *    因此，如果从节点j到节点i有一条权值为ck的边，那么最短路径算法会保证这个不等式成立
 * 
 * 4. 解的存在性：
 *    如果图中存在负环，则差分约束系统无解
 *    否则，从超级源点到各点的最短距离就是一组可行解
 * 
 * 5. 超级源点：
 *    为了确保图的连通性，添加一个超级源点0，向所有变量节点连权值为0的边
 *    这相当于添加约束 xi - x0 <= 0，即 xi <= x0
 * 
 * 时间复杂度分析：
 * - 建图：O(m)，其中m是约束条件数量
 * - SPFA算法：平均O(k*m)，最坏O(n*m)，其中k是常数，n是变量数量
 * - 总体：O(n + m)
 * 
 * 空间复杂度分析：
 * - 链式前向星存储图：O(n + m)
 * - dist数组、update数组、enter数组：O(n)
 * - 队列：O(n*m)（最坏情况）
 * - 总体：O(n + m)
 */

const int MAXN = 5001;      // 最大节点数
const int MAXM = 10001;     // 最大边数
const int MAXQ = 5000001;   // 最大队列大小
const int INF = INT_MAX;    // 无穷大

// 链式前向星结构
int head[MAXN];             // 每个节点的第一条边的索引
int next_edge[MAXM];        // 下一条边的索引
int to[MAXM];               // 边的目标节点
int weight[MAXM];           // 边的权值
int cnt;                    // 边的计数器

// SPFA算法需要的数组
int dist[MAXN];             // 距离数组
int update[MAXN];           // 更新次数数组
bool enter[MAXN];           // 是否在队列中的标记数组
int queue_arr[MAXQ];        // 队列数组
int h, t;                   // 队列头尾指针

int n, m;                   // 节点数和边数

/**
 * 初始化函数
 */
void prepare() {
    cnt = 1;                 // 边从1开始计数
    h = t = 0;               // 队列头尾指针初始化
    memset(head, 0, sizeof(head[0]) * (n + 1));  // 清空头指针数组
    memset(dist, 0x3f, sizeof(dist[0]) * (n + 1));  // 距离初始化为无穷大
    memset(update, 0, sizeof(update[0]) * (n + 1));  // 更新次数初始化为0
    memset(enter, false, sizeof(enter[0]) * (n + 1));  // 入队标记初始化为false
}

/**
 * 添加边的函数
 * @param u 起点
 * @param v 终点
 * @param w 边权
 */
void addEdge(int u, int v, int w) {
    next_edge[cnt] = head[u];
    to[cnt] = v;
    weight[cnt] = w;
    head[u] = cnt++;
}

/**
 * SPFA算法检测负环并求最短路径
 * @param s 超级源点
 * @return 是否存在负环
 */
bool spfa(int s) {
    dist[s] = 0;
    update[s] = 1;
    queue_arr[t++] = s;
    enter[s] = true;
    
    while (h < t) {
        int u = queue_arr[h++];
        enter[u] = false;
        
        for (int ei = head[u]; ei != 0; ei = next_edge[ei]) {
            int v = to[ei];
            int w = weight[ei];
            
            // 松弛操作（最短路）
            if (dist[v] > dist[u] + w) {
                dist[v] = dist[u] + w;
                
                if (!enter[v]) {
                    // 如果入队次数超过节点数，说明存在负环
                    if (++update[v] > n) {
                        return true;  // 存在负环
                    }
                    queue_arr[t++] = v;
                    enter[v] = true;
                }
            }
        }
    }
    
    return false;  // 不存在负环
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);
    
    cin >> n >> m;
    prepare();
    
    // 添加超级源点0，向所有变量节点连权值为0的边
    for (int i = 1; i <= n; ++i) {
        addEdge(0, i, 0);
    }
    
    // 读取m个约束条件
    for (int i = 1; i <= m; ++i) {
        int u, v, w;
        cin >> u >> v >> w;
        // 形式1的连边方式：xi - xj <= ck 转化为边 j -> i，权值为ck
        addEdge(v, u, w);
    }
    
    // 使用SPFA检测负环
    if (spfa(0)) {
        cout << "NO" << endl;
    } else {
        // 输出解
        for (int i = 1; i <= n; ++i) {
            cout << dist[i] << " ";
        }
        cout << endl;
    }
    
    return 0;
}