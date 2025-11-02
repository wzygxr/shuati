/**
 * 洛谷P3377 【模板】左偏树/可并堆
 * 题目链接: https://www.luogu.com.cn/problem/P3377
 * 
 * 题目描述：
 * 如题，一开始有n个小根堆，每个堆包含且仅包含一个数。接下来需要支持两种操作：
 * 1. 1 x y：将第x个数和第y个数所在的小根堆合并（若第x或第y个数已经被删除或第x和第y个数在同一个堆内，则无视此操作）
 * 2. 2 x：输出第x个数所在的堆最小数，并将这个最小数删除（若有多个最小数，优先删除先输入的；若第x个数已经被删除，则输出-1并无视删除操作）
 * 
 * 解题思路：
 * 使用左偏树实现可并堆，支持快速合并操作和删除最小值操作。
 * 1. 使用左偏树维护每个小根堆
 * 2. 使用并查集维护每个节点所属的堆
 * 3. 对于操作1：合并两个堆
 * 4. 对于操作2：删除堆顶元素
 * 
 * 左偏树核心性质：
 * 1. 堆性质：父节点的值小于等于子节点的值
 * 2. 左偏性质：左子节点的距离大于等于右子节点的距离
 * 3. 距离定义：从节点到最近的外节点（空节点）的边数
 * 
 * 算法优势：
 * 1. 合并操作时间复杂度为O(log n)
 * 2. 插入和删除操作时间复杂度为O(log n)
 * 3. 支持高效处理动态集合合并问题
 * 
 * 时间复杂度分析：
 * - 左偏树合并: O(log n)
 * - 左偏树插入: O(log n)
 * - 左偏树删除: O(log n)
 * - 并查集操作: 近似 O(1)
 * - 总体复杂度: O(M * log N)
 * 
 * 空间复杂度分析:
 * - 左偏树节点存储: O(N)
 * - 并查集存储: O(N)
 * - 总体空间复杂度: O(N)
 * 
 * 相关题目：
 * - Java实现：Code06_LuoguP3377_LeftistTree.java
 * - Python实现：Code06_LuoguP3377_LeftistTree.py
 * - C++实现：Code06_LuoguP3377_LeftistTree.cpp
 */

// 为避免编译问题，使用基本的C++实现方式
const int MAXN = 100010;

// 左偏树节点结构
struct Node {
    int val;        // 节点权值
    int dist;       // 节点距离（到最近外节点的距离）
    int index;      // 节点索引
    int left, right; // 左右子节点索引
    int time;       // 输入时间，用于处理相同值时的优先级
    
    /**
     * 默认构造函数
     */
    Node() : val(0), dist(0), index(0), left(0), right(0), time(0) {}
    
    /**
     * 构造函数
     * @param v 节点权值
     * @param idx 节点索引
     * @param t 输入时间
     */
    Node(int v, int idx, int t) : val(v), dist(0), index(idx), left(0), right(0), time(t) {}
};

Node nodes[MAXN];       // 节点数组
int parent[MAXN];       // 并查集父节点数组
bool deleted[MAXN];     // 标记节点是否被删除
int nodeCount = 0;      // 节点计数器
int currentTime = 0;    // 时间戳

/**
 * 查找并查集根节点（带路径压缩）
 * @param x 节点索引
 * @return 根节点索引
 */
int find(int x) {
    if (parent[x] != x) {
        parent[x] = find(parent[x]);  // 路径压缩
    }
    return parent[x];
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
    
    // 确保a节点权值 <= b节点权值（小根堆）
    // 如果值相同，优先选择输入时间早的
    if (nodes[a].val > nodes[b].val || 
        (nodes[a].val == nodes[b].val && nodes[a].time > nodes[b].time)) {
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
