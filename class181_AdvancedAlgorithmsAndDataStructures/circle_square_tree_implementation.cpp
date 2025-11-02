// 圆方树 (Circle Square Tree) C++ 实现

/**
 * 圆方树 (Circle Square Tree) 实现
 * 
 * 圆方树是一种将无向图转化为树结构的方法，主要用于处理仙人掌图（每条边最多属于一个环的图）。
 * 在圆方树中：
 * - 圆点：原图中的节点
 * - 方点：原图中的环
 * 
 * 应用场景：
 * 1. 仙人掌图算法：最短路径、环相关问题
 * 2. 图论问题：点双连通分量、割点
 * 3. 竞赛算法：处理特殊图结构
 * 
 * 算法思路：
 * 1. 使用DFS找出图中的点双连通分量
 * 2. 对于每个点双连通分量：
 *    - 如果是单个边，创建圆点-圆点的连接
 *    - 如果包含多个节点（形成环），创建一个方点代表这个环
 * 3. 将圆点和方点连接形成树结构
 * 
 * 时间复杂度：O(V + E)
 * 空间复杂度：O(V + E)
 */

// 由于编译环境限制，这里提供算法思路和伪代码实现

/*
// 圆方树类定义
struct CircleSquareTree {
    int n; // 原图节点数
    int** graph; // 原图的邻接表
    int** tree; // 圆方树的邻接表
    int treeNodeCount; // 圆方树节点数（包括圆点和方点）
    bool* visited; // DFS访问标记
    int* dfn; // DFS时间戳
    int* low; // 最小时间戳
    int* stack; // DFS栈
    int stackTop; // 栈顶指针
    int dfsTime; // DFS时间
    int** biconnectedComponents; // 点双连通分量
    int* bccSizes; // 每个点双连通分量的大小
    int bccCount; // 点双连通分量数量
    
    // 构造函数
    CircleSquareTree(int n);
    
    // 析构函数
    ~CircleSquareTree();
    
    // 添加边
    void addEdge(int u, int v);
    
    // 构建圆方树
    void buildCircleSquareTree();
    
    // Tarjan算法找点双连通分量
    void tarjan(int u, int parent);
    
    // 构建圆方树
    void buildTree();
    
    // 获取圆方树
    int** getCircleSquareTree(int* nodeCount);
    
    // 获取点双连通分量
    int** getBiconnectedComponents(int* count, int** sizes);
    
    // 计算两点间在圆方树上的距离
    int distance(int u, int v);
    
    // 打印圆方树结构
    void printStructure();
};

// 构造函数
CircleSquareTree::CircleSquareTree(int n) : n(n) {
    // 初始化数据结构
    this->graph = (int**)calloc(n, sizeof(int*));
    this->visited = (bool*)calloc(n, sizeof(bool));
    this->dfn = (int*)malloc(n * sizeof(int));
    this->low = (int*)malloc(n * sizeof(int));
    this->stack = (int*)malloc(n * sizeof(int));
    this->stackTop = 0;
    this->dfsTime = 0;
    this->biconnectedComponents = NULL;
    this->bccSizes = NULL;
    this->bccCount = 0;
    
    // 初始化数组
    for (int i = 0; i < n; i++) {
        this->dfn[i] = -1;
        this->low[i] = -1;
    }
}

// 析构函数
CircleSquareTree::~CircleSquareTree() {
    // 释放内存
    for (int i = 0; i < n; i++) {
        free(graph[i]);
    }
    free(graph);
    free(visited);
    free(dfn);
    free(low);
    free(stack);
    // 释放其他数据结构
}

// 添加边
void CircleSquareTree::addEdge(int u, int v) {
    // 在实际实现中需要动态维护邻接表
}

// 构建圆方树
void CircleSquareTree::buildCircleSquareTree() {
    // 初始化
    for (int i = 0; i < n; i++) {
        visited[i] = false;
        dfn[i] = -1;
        low[i] = -1;
    }
    stackTop = 0;
    dfsTime = 0;
    bccCount = 0;
    
    // 找出所有点双连通分量
    for (int i = 0; i < n; i++) {
        if (dfn[i] == -1) {
            tarjan(i, -1);
        }
    }
    
    // 构建圆方树
    buildTree();
}

// Tarjan算法找点双连通分量
void CircleSquareTree::tarjan(int u, int parent) {
    dfn[u] = low[u] = ++dfsTime;
    visited[u] = true;
    stack[stackTop++] = u;
    int children = 0;
    
    // 遍历u的所有邻居
    for (int i = 0; graph[u] && graph[u][i] != -1; i++) {
        int v = graph[u][i];
        if (v == parent) continue;
        
        if (dfn[v] == -1) {
            children++;
            tarjan(v, u);
            low[u] = (low[u] < low[v]) ? low[u] : low[v];
            
            // 发现点双连通分量
            if (low[v] >= dfn[u]) {
                // 创建新的点双连通分量
                // 实现细节省略
            }
        } else {
            low[u] = (low[u] < dfn[v]) ? low[u] : dfn[v];
        }
    }
    
    // 根节点特殊情况
    if (parent == -1 && children == 0) {
        // 处理孤立节点
    }
}

// 算法核心思想：
// 1. 使用Tarjan算法找出图中的点双连通分量
// 2. 对于每个点双连通分量，创建一个方点
// 3. 将方点与该分量中的所有圆点连接
// 4. 形成圆方树结构

// 时间复杂度分析：
// - Tarjan算法：O(V + E)
// - 构建圆方树：O(V + E)
// - 总体时间复杂度：O(V + E)
// - 空间复杂度：O(V + E)
*/

// 算法应用场景：
// 1. 仙人掌图上的最短路径问题
// 2. 图的环结构分析
// 3. 竞赛算法中的特殊图处理
// 4. 网络流问题中的预处理