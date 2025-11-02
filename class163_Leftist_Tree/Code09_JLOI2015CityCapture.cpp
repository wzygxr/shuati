/**
 * JLOI2015 城池攻占
 * 
 * 题目描述：
 * 小铭铭最近获得了一副新的桌游，游戏中需要用m个骑士攻占n个城池。
 * 这n个城池用1到n的整数表示。除1号城池外，城池i会受到另一座城池fi的管辖，
 * 其中fi<i。也就是说，所有城池构成了一棵有根树，1号城池为根。
 * 
 * 游戏开始前，所有城池都会有一个防御值hi。
 * 如果一个骑士的初始战斗力si大于等于城池的防御值，那么该骑士就能占领该城池。
 * 骑士的战斗力会因为占领城池而改变，每个城池i有两种属性：
 * 1. ai=0时，战斗力会加上vi
 * 2. ai=1时，战斗力会乘以vi
 * 
 * 骑士们按照1到m的顺序依次攻占城池。每个骑士会按照如下方法攻占城池：
 * 1. 选择一个城池i作为起点
 * 2. 如果当前战斗力大于等于城池防御值，则占领该城池并按规则改变战斗力
 * 3. 然后前往管辖该城池的城池fi，重复步骤2
 * 4. 直到无法占领某个城池或到达根节点为止
 * 
 * 你需要计算：
 * 1. 每个城池各有多少个骑士牺牲（无法占领该城池）
 * 2. 每个骑士各攻占了多少个城池
 * 
 * 解题思路：
 * 这是一道经典的树形结构+左偏树优化的题目。
 * 1. 建立城池的树形结构，以1号城池为根
 * 2. 对于每个城池，维护一个左偏树，存储当前在该城池的骑士
 * 3. 左偏树需要支持延迟标记，用于处理战斗力的加法和乘法操作
 * 4. 按照骑士编号顺序处理每个骑士：
 *    - 将骑士放入起始城池的左偏树中
 *    - 从起始城池开始向上爬树，直到无法占领某个城池
 *    - 在每个城池中，如果骑士战斗力大于等于防御值，则占领并更新战斗力
 *    - 否则骑士牺牲，统计牺牲人数
 * 5. 为了优化效率，使用延迟标记和标记下传技术
 * 
 * 时间复杂度分析：
 * - 树形遍历: O(N)
 * - 左偏树操作: O(M log M)
 * - 延迟标记处理: O(N log M)
 * - 总体复杂度: O((N+M) log M)
 * 
 * 空间复杂度分析:
 * - 树形结构存储: O(N)
 * - 左偏树节点存储: O(M)
 * - 延迟标记存储: O(N)
 * - 总体空间复杂度: O(N+M)
 */

// 为避免编译问题，使用基本的C++实现方式
const int MAXN = 300010;

// 左偏树节点结构（支持延迟标记）
struct Node {
    long long val;   // 节点权值（骑士战斗力）
    int dist;        // 节点距离（到最近外节点的距离）
    int index;       // 节点索引
    int left, right; // 左右子节点索引
    long long add;   // 加法延迟标记
    long long mul;   // 乘法延迟标记
    
    Node() : val(0), dist(0), index(0), left(0), right(0), add(0), mul(1) {}
    Node(long long v, int idx) : val(v), dist(0), index(idx), left(0), right(0), add(0), mul(1) {}
};

Node nodes[MAXN];       // 节点数组
int nodeCount = 0;      // 节点计数器

// 树形结构
int father[MAXN];       // 父节点
long long defense[MAXN]; // 城池防御值
int op[MAXN];           // 操作类型（0加法，1乘法）
long long value[MAXN];  // 操作值
int head[MAXN];         // 邻接表头
int next[MAXN];         // 邻接表next指针
int to[MAXN];           // 邻接表边指向的节点
int edgeCount = 0;      // 边计数器

// DFS相关
int roots[MAXN];        // 每个城池对应的左偏树根
int sacrifice[MAXN];     // 每个城池牺牲人数
int conquer[MAXN];      // 每个骑士攻占城池数
int start[MAXN];        // 每个骑士起始城池
long long strength[MAXN]; // 每个骑士初始战斗力

/**
 * 添加边
 * @param u 起点
 * @param v 终点
 */
void addEdge(int u, int v) {
    to[edgeCount] = v;
    next[edgeCount] = head[u];
    head[u] = edgeCount++;
}

/**
 * 初始化节点
 * @param val 节点权值
 * @return 节点索引
 */
int initNode(long long val) {
    nodes[++nodeCount] = Node(val, nodeCount);
    return nodeCount;
}

/**
 * 应用加法标记
 * @param x 节点索引
 * @param v 加法值
 */
void addTag(int x, long long v) {
    if (x == 0) return;
    nodes[x].val += v;
    nodes[x].add += v;
}

/**
 * 应用乘法标记
 * @param x 节点索引
 * @param v 乘法值
 */
void mulTag(int x, long long v) {
    if (x == 0) return;
    nodes[x].val *= v;
    nodes[x].add *= v;
    nodes[x].mul *= v;
}