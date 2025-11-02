/**
 * 洛谷P2713 罗马游戏
 * 
 * 题目描述：
 * 罗马皇帝很喜欢玩杀人游戏。他的军队里面有n个士兵，每个士兵都是一个独立的团。
 * 最近举行了一次平面几何测试，每个士兵都得到了一个分数。
 * 皇帝很喜欢平面几何，他对那些得分很低的士兵嗤之以鼻。
 * 
 * 他决定玩这样一个游戏。它可以发两种命令：
 * - M i j 把i所在的团和j所在的团合并成一个团。如果i,j有一个士兵是死人，那么就忽略该命令。
 * - K i 把i所在的团里面得分最低的士兵杀死。如果i这个士兵已经死了，这条命令就忽略。
 * 
 * 皇帝希望他每发布一条K i命令，下面的将军就把被杀的士兵的分数报上来
 * （如果这条命令被忽略，那么就报0分）。
 * 
 * 解题思路：
 * 使用左偏树维护每个团的最小值（小根堆），配合并查集维护团的连通性。
 * 1. 使用左偏树维护每个团的士兵分数（小根堆）
 * 2. 使用并查集维护士兵所属的团
 * 3. 对于M操作：合并两个团
 * 4. 对于K操作：删除团中最小分数的士兵
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
 */

// 为避免编译问题，使用基本的C++实现方式
const int MAXN = 1000010;

// 左偏树节点结构
struct Node {
    int val;        // 节点权值（士兵分数）
    int dist;       // 节点距离（到最近外节点的距离）
    int index;      // 节点索引
    int left, right; // 左右子节点索引
    
    Node() : val(0), dist(0), index(0), left(0), right(0) {}
    Node(int v, int idx) : val(v), dist(0), index(idx), left(0), right(0) {}
};

Node nodes[MAXN];       // 节点数组
int parent[MAXN];       // 并查集父节点数组
bool killed[MAXN];     // 标记士兵是否被杀死
int nodeCount = 0;      // 节点计数器

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
    if (nodes[a].val > nodes[b].val) {
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
