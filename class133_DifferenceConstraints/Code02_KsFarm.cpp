#include <iostream>
#include <vector>
#include <queue>
#include <climits>
#include <cstring>
using namespace std;

/**
 * 小k的农场（洛谷P1993）差分约束系统解法
 * 
 * 题目描述：
 * 小k在农村有一个大农场，农场里有n个牛棚，编号为1到n。
 * 农场有m个约束条件，每个约束条件属于以下三种类型之一：
 * 1. A X Y：牛棚X的草量比牛棚Y多至少1
 * 2. B X Y：牛棚X的草量比牛棚Y多至多1
 * 3. C X Y：牛棚X的草量和牛棚Y的草量相等
 * 请判断是否存在满足所有约束条件的草量分配方案。
 * 
 * 解题思路：
 * 这是一个典型的差分约束系统问题，需要将不同类型的约束条件转化为不等式形式。
 * 
 * 设f[X]表示牛棚X的草量：
 * 1. A X Y：f[X] - f[Y] >= 1
 * 2. B X Y：f[X] - f[Y] <= 1
 * 3. C X Y：f[X] = f[Y]，即f[X] - f[Y] <= 0 且 f[Y] - f[X] <= 0
 * 
 * 为了求解这个系统，我们可以将约束转换为最长路径问题或最短路径问题。
 * 在这里，我们选择将其转换为最长路径问题（因为A类型约束是>=形式）。
 * 
 * 建图方式：
 * 1. A X Y：f[X] >= f[Y] + 1 → 从Y到X连权值为1的边
 * 2. B X Y：f[X] <= f[Y] + 1 → f[Y] >= f[X] - 1 → 从X到Y连权值为-1的边
 * 3. C X Y：f[X] = f[Y] → 从X到Y连权值为0的边，从Y到X连权值为0的边
 * 
 * 然后添加超级源点，从超级源点到所有点连权值为0的边（或从所有点到超级源点连边，取决于采用最长路还是最短路）。
 * 最后使用SPFA算法检测是否存在正环。如果存在正环，则无解；否则有解。
 * 
 * 时间复杂度：O(n * m)，其中n是牛棚数量，m是约束条件数量
 * 空间复杂度：O(n + m)
 * 
 * 相关题目：
 * 1. 洛谷 P1993 小k的农场 - 本题
 * 2. POJ 2983 Is the Information Reliable? - 类似题目
 * 3. USACO 2005 December Gold Layout - 类似题目
 */

const int MAXN = 10010;      // 最大节点数
const int MAXM = 100010;     // 最大边数
const int MAXQ = 1000010;    // 最大队列大小
const int INF = INT_MIN;     // 负无穷大

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
    memset(head, 0, sizeof(head[0]) * (n + 2));  // 清空头指针数组
    memset(dist, 0x8f, sizeof(dist[0]) * (n + 2));  // 距离初始化为负无穷大
    memset(update, 0, sizeof(update[0]) * (n + 2));  // 更新次数初始化为0
    memset(enter, false, sizeof(enter[0]) * (n + 2));  // 入队标记初始化为false
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
 * SPFA算法检测正环并求最长路径
 * @param s 超级源点
 * @return 是否存在正环
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
            
            // 松弛操作（最长路）
            if (dist[v] < dist[u] + w) {
                dist[v] = dist[u] + w;
                
                if (!enter[v]) {
                    // 如果入队次数超过节点数，说明存在正环
                    if (++update[v] > n + 1) { // +1 是为了包含超级源点
                        return true;  // 存在正环
                    }
                    queue_arr[t++] = v;
                    enter[v] = true;
                }
            }
        }
    }
    
    return false;  // 不存在正环
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);
    
    cin >> n >> m;
    prepare();
    
    // 读取m个约束条件
    for (int i = 1; i <= m; ++i) {
        char type;
        int x, y;
        cin >> type >> x >> y;
        
        if (type == 'A') {
            // A X Y：f[X] - f[Y] >= 1 → 从Y到X连权值为1的边
            addEdge(y, x, 1);
        } else if (type == 'B') {
            // B X Y：f[X] - f[Y] <= 1 → f[Y] - f[X] >= -1 → 从X到Y连权值为-1的边
            addEdge(x, y, -1);
        } else if (type == 'C') {
            // C X Y：f[X] = f[Y] → 双向连权值为0的边
            addEdge(x, y, 0);
            addEdge(y, x, 0);
        }
    }
    
    // 添加超级源点，从超级源点到所有点连权值为0的边
    int super_source = 0;
    for (int i = 1; i <= n; ++i) {
        addEdge(super_source, i, 0);
    }
    
    // 使用SPFA检测正环
    if (spfa(super_source)) {
        cout << "No" << endl;
    } else {
        cout << "Yes" << endl;
    }
    
    return 0;
}