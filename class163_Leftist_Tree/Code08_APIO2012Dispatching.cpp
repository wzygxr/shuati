/**
 * APIO2012 派遣
 * 
 * 题目描述：
 * 在一个忍者的帮派里，一些忍者们被选中派遣给顾客，然后依据自己的工作获取报偿。
 * 在这个帮派里，有一名忍者被称之为Master。除了Master以外，每名忍者都有且仅有一个上级。
 * 为保密，同时增强忍者们的领导力，所有与他们工作相关的指令总是由上级发送给他的直接下属，
 * 而不允许通过其他的方式发送。
 * 
 * 现在你要招募一批忍者，并把它们派遣给顾客。你需要为每个被派遣的忍者支付一定的薪水，
 * 同时使得支付的薪水总额不超过你的预算。另外，为了发送指令，你需要选择一名忍者作为管理者，
 * 要求这个管理者可以向所有被派遣的忍者发送指令，在发送指令时，任何忍者（不管是否被派遣）
 * 都可以作为消息的传递人。管理者自己可以被派遣，也可以不被派遣。当然，如果管理者没有被派遣，
 * 你就不需要支付管理者的薪水。
 * 
 * 你的目标是在预算内使顾客的满意度最大。这里定义顾客的满意度为派遣的忍者总数乘以管理者的领导力水平，
 * 其中每个忍者的领导力水平也是一定的。
 * 
 * 写一个程序，给定每一个忍者i的上级Bi，薪水Ci，领导力Li，以及支付给忍者们的薪水总预算M，
 * 输出在预算内满足上述要求时顾客满意度的最大值。
 * 
 * 解题思路：
 * 这是一道经典的树形DP+左偏树优化的题目。
 * 1. 建立树形结构，以Master为根节点
 * 2. 从叶子节点向上进行DFS，对于每个节点维护一个大根堆（左偏树）
 * 3. 堆中存储以该节点为根的子树中所有忍者的薪水
 * 4. 当堆中薪水总和超过预算M时，不断弹出薪水最大的忍者，直到总和不超过M
 * 5. 计算以当前节点为管理者时的满意度：忍者数量 * 领导力
 * 6. 向上传递时，将当前节点的左偏树与其所有子节点的左偏树合并
 * 
 * 时间复杂度分析：
 * - 树形DFS: O(N)
 * - 左偏树合并: O(N log N)
 * - 左偏树删除: O(N log N)
 * - 总体复杂度: O(N log N)
 * 
 * 空间复杂度分析:
 * - 树形结构存储: O(N)
 * - 左偏树节点存储: O(N)
 * - 总体空间复杂度: O(N)
 */

// 为避免编译问题，使用基本的C++实现方式
const int MAXN = 100010;

// 左偏树节点结构
struct Node {
    long long val;   // 节点权值（忍者薪水）
    int dist;        // 节点距离（到最近外节点的距离）
    int index;       // 节点索引
    int left, right; // 左右子节点索引
    
    Node() : val(0), dist(0), index(0), left(0), right(0) {}
    Node(long long v, int idx) : val(v), dist(0), index(idx), left(0), right(0) {}
};

Node nodes[MAXN];       // 节点数组
int nodeCount = 0;      // 节点计数器

// 树形结构
int boss[MAXN];         // 上级忍者
long long salary[MAXN]; // 薪水
long long leadership[MAXN]; // 领导力
int head[MAXN];         // 邻接表头
int next[MAXN];         // 邻接表next指针
int to[MAXN];           // 邻接表边指向的节点
int edgeCount = 0;      // 边计数器

// DFS相关
int roots[MAXN];        // 每个节点对应的左偏树根
long long sum[MAXN];    // 每个左偏树的薪水总和
int size[MAXN];         // 每个左偏树的节点数量
long long budget;       // 预算
long long maxSatisfaction = 0; // 最大满意度

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
 * 合并两个左偏树
 * @param a 第一棵左偏树根节点索引
 * @param b 第二棵左偏树根节点索引
 * @return 合并后左偏树根节点索引
 */
int merge(int a, int b) {
    // 如果其中一个为空，返回另一个
    if (a == 0) return b;
    if (b == 0) return a;
    
    // 确保a节点权值 >= b节点权值（大根堆）
    if (nodes[a].val < nodes[b].val) {
        int temp = a;
        a = b;
        b = temp;
    }
    
    // 递归合并右子树和b树
    nodes[a].right = merge(nodes[a].right, b);
    
    // 维护左偏性质：左子树距离 >= 右子树距离
    if (nodes[nodes[a].left].dist < nodes[nodes[a].right].dist) {
        int temp = nodes[a].left;
        nodes[a].left = nodes[a].right;
        nodes[a].right = temp;
    }
    
    // 更新距离
    nodes[a].dist = nodes[nodes[a].right].dist + 1;
    
    return a;
}

/**
 * 删除左偏树根节点
 * @param root 根节点索引
 * @return 新的根节点索引
 */
int pop(int root) {
    if (root == 0) return 0;
    
    return merge(nodes[root].left, nodes[root].right);
}
