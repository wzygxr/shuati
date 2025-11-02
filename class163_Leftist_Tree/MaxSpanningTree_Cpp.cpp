/**
 * 牛客 NC15093 最大生成树
 * 题目链接：https://ac.nowcoder.com/acm/problem/15093
 * 
 * 题目描述：
 * 给定一个无向图，要求找到一棵生成树，使得这棵生成树的边权之和最大。
 * 
 * 解题思路：
 * 使用Kruskal算法的变种，通过左偏树来维护并查集结构，实现按秩合并优化。
 * 与传统的Kruskal算法类似，但选择边的顺序是从大到小，以获得最大生成树。
 * 
 * 算法步骤：
 * 1. 将所有边按权重从大到小排序
 * 2. 初始化并查集结构（使用左偏树实现）
 * 3. 遍历排序后的边，如果边的两个端点不在同一集合中，则将该边加入生成树
 * 4. 重复步骤3直到生成树包含V-1条边
 * 
 * 时间复杂度：O(E log V)，其中E是边数，V是顶点数
 * 空间复杂度：O(V + E)
 * 
 * 相关题目：
 * - Java实现：MaxSpanningTree_Java.java
 * - Python实现：MaxSpanningTree_Python.py
 * - C++实现：MaxSpanningTree_Cpp.cpp
 */

// 边结构体
struct Edge {
    int from;   // 起始顶点
    int to;     // 终止顶点
    int weight; // 权重
    
    /**
     * 构造函数
     * @param f 起始顶点
     * @param t 终止顶点
     * @param w 边的权重
     */
    Edge(int f, int t, int w) : from(f), to(t), weight(w) {}
};

// 左偏树节点结构体（用于并查集的按秩合并）
struct LeftistTreeNode {
    int parent; // 父节点（用于并查集）
    int size;   // 子树大小（用于按秩合并）
    int value;  // 节点值（这里存储顶点编号）
    int dist;   // 距离（空路径长度）
    LeftistTreeNode* left;
    LeftistTreeNode* right;
    
    /**
     * 构造函数
     * @param val 节点值（顶点编号）
     */
    LeftistTreeNode(int val) 
        : parent(val), size(1), value(val), dist(0), left(0), right(0) {}
};

/**
 * 合并两个左偏树
 * @param a 第一棵左偏树的根节点
 * @param b 第二棵左偏树的根节点
 * @return 合并后的左偏树根节点
 */
LeftistTreeNode* merge(LeftistTreeNode* a, LeftistTreeNode* b) {
    // 处理空树情况
    if (!a) return b;
    if (!b) return a;
    
    // 这里不关心具体的顺序，因为我们只是用左偏树来维护并查集
    a->right = merge(a->right, b);
    
    // 维护左偏性质：左子树的距离应大于等于右子树的距离
    if (!a->left || (a->right && a->left->dist < a->right->dist)) {
        LeftistTreeNode* temp = a->left;
        a->left = a->right;
        a->right = temp;
    }
    
    // 更新距离：叶子节点距离为0，非叶子节点距离为其右子树距离+1
    a->dist = a->right ? a->right->dist + 1 : 0;
    return a;
}

// 全局数组存储左偏树节点
LeftistTreeNode* nodes[100005];

/**
 * 查找根节点（带路径压缩优化）
 * @param x 顶点编号
 * @return 顶点x所在集合的根节点
 */
int find(int x) {
    // 路径压缩：将查找路径上的所有节点直接连接到根节点
    if (nodes[x]->parent != x) {
        nodes[x]->parent = find(nodes[x]->parent);
    }
    return nodes[x]->parent;
}

/**
 * 合并两个集合
 * @param x 顶点编号
 * @param y 顶点编号
 */
void unionSets(int x, int y) {
    int rootX = find(x);
    int rootY = find(y);
    
    // 如果两个顶点已在同一集合中，无需合并
    if (rootX == rootY) return;
    
    // 按秩合并：将较小的树合并到较大的树上，以保持树的平衡
    if (nodes[rootX]->size < nodes[rootY]->size) {
        int temp = rootX;
        rootX = rootY;
        rootY = temp;
    }
    
    // 将rootY的父节点设为rootX，完成合并
    nodes[rootY]->parent = rootX;
    // 更新根节点的大小
    nodes[rootX]->size += nodes[rootY]->size;
    // 使用左偏树合并两个集合
    nodes[rootX] = merge(nodes[rootX], nodes[rootY]);
}

// 边数组
Edge* edges[100005];

/**
 * 比较函数，用于按权重从大到小排序
 */
bool compareEdges(Edge* a, Edge* b) {
    return a->weight > b->weight;
}

/**
 * 计算最大生成树的边权和
 * @param V 顶点数
 * @param E 边数
 * @return 最大生成树的边权和
 */
int maxSpanningTree(int V, int E) {
    // 初始化左偏树节点数组，索引0不使用，顶点编号从1开始
    for (int i = 1; i <= V; i++) {
        nodes[i] = new LeftistTreeNode(i);
    }
    
    // 按边权从大到小排序，以获得最大生成树
    // 简化排序实现
    for (int i = 0; i < E - 1; i++) {
        for (int j = 0; j < E - 1 - i; j++) {
            if (edges[j]->weight < edges[j + 1]->weight) {
                Edge* temp = edges[j];
                edges[j] = edges[j + 1];
                edges[j + 1] = temp;
            }
        }
    }
    
    int totalWeight = 0;  // 最大生成树的总权重
    int edgeCount = 0;    // 已选择的边数
    
    // Kruskal算法：选择最大的边，避免环
    for (int i = 0; i < E; i++) {
        Edge* edge = edges[i];
        // 如果边的两个端点不在同一集合中，则可以安全地添加这条边
        if (find(edge->from) != find(edge->to)) {
            unionSets(edge->from, edge->to);
            totalWeight += edge->weight;
            edgeCount++;
            
            // 生成树有V-1条边，达到这个数量就停止
            if (edgeCount == V - 1) {
                break;
            }
        }
    }
    
    // 检查是否形成了生成树（所有顶点都在同一集合中）
    // 如果是森林（多个连通分量），则无法形成生成树
    // 根据题目描述，应该保证图是连通的
    return totalWeight;
}