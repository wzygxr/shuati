// 有向图博弈 (SG函数在有向图上的应用)
// 一个有向无环图，在若干点上有若干棋子，两人轮流移动棋子
// 每次只能将一个棋子沿有向边移动一步
// 当无棋子可移动时输，即移动最后一枚棋子者胜
// 
// 题目来源：
// 1. POJ 2425 A Chess Game - http://poj.org/problem?id=2425
// 2. HDU 1524 A Chess Game - http://acm.hdu.edu.cn/showproblem.php?pid=1524
// 3. POJ 2599 A New Stone Game - http://poj.org/problem?id=2599
// 
// 算法核心思想：
// 1. SG函数方法：通过递推计算每个节点的SG值，SG值不为0表示必胜态，为0表示必败态
// 2. SG定理：整个游戏的SG值等于各棋子所在节点SG值的异或和
// 
// 时间复杂度分析：
// 1. 预处理：O(n * max_degree) - 计算每个节点的SG值
// 2. 查询：O(m) - m为棋子数，计算所有棋子SG值的异或和
// 
// 空间复杂度分析：
// 1. 图存储：O(n + e) - n为节点数，e为边数
// 2. SG数组：O(n) - 存储每个节点的SG值
// 
// 工程化考量：
// 1. 异常处理：处理非法输入和边界情况
// 2. 性能优化：预处理SG值避免重复计算
// 3. 可读性：添加详细注释说明算法原理
// 4. 可扩展性：支持不同的图结构和查询

// 最大节点数
const int MAXN = 1001;

// 图的邻接表表示
int graph[MAXN][MAXN]; // graph[i][j]表示节点i的第j个邻居
int degree[MAXN]; // degree[i]表示节点i的度数

// SG数组，sg[i]表示节点i的SG值
int sg[MAXN];

// visited数组用于记忆化搜索
int visited[MAXN];

// appear数组用于计算mex值
int appear[MAXN];

// 
// 算法原理：
// 1. 对于每个节点，计算其后继节点的SG值集合
// 2. 节点的SG值等于不属于该集合的最小非负整数(mex)
// 3. 根据SG定理，整个游戏的SG值等于各棋子所在节点SG值的异或和
// 
// SG函数定义：
// SG(x) = mex{SG(y) | 存在从x到y的有向边}
// 其中mex(S)表示不属于集合S的最小非负整数
// 
// 对于有向图博弈，节点x的后继状态为所有可以直接到达的节点
int computeSG(int node) {
    // 记忆化搜索
    if (visited[node]) {
        return sg[node];
    }
    
    // 标记已访问
    visited[node] = 1;
    
    // 初始化appear数组
    int i, j;
    for (i = 0; i < MAXN; i++) {
        appear[i] = 0;
    }
    
    // 计算节点node的所有后继节点的SG值
    for (i = 0; i < degree[node]; i++) {
        int next = graph[node][i];
        // 标记后继节点的SG值已出现
        int nextSG = computeSG(next);
        if (nextSG < MAXN) {
            appear[nextSG] = 1;
        }
    }
    
    // 计算mex值，即不属于appear集合的最小非负整数
    for (i = 0; i < MAXN; i++) {
        if (appear[i] == 0) {
            sg[node] = i;
            return sg[node];
        }
    }
    
    return 0; // 理论上不会执行到这里
}

// 
// 算法原理：
// 根据SG定理计算整个游戏的SG值
// 1. 对于每个棋子，计算其所在节点的SG值
// 2. 整个游戏的SG值等于各棋子所在节点SG值的异或和
// 3. SG值不为0表示必胜态，为0表示必败态
int solve(int* chessPositions, int chessCount, int n) {
    // 计算所有棋子所在节点SG值的异或和
    int xorSum = 0;
    int i;
    for (i = 0; i < chessCount; i++) {
        int pos = chessPositions[i];
        if (pos >= 0 && pos < n) {
            xorSum ^= sg[pos];
        }
    }
    
    // SG值不为0表示必胜态，为0表示必败态
    return xorSum != 0 ? 1 : 0; // 1表示WIN，0表示LOSE
}

// 构建SG函数
void buildSG(int n) {
    int i;
    // 初始化visited数组
    for (i = 0; i < n; i++) {
        visited[i] = 0;
    }
    
    // 计算每个节点的SG值
    for (i = 0; i < n; i++) {
        if (!visited[i]) {
            sg[i] = computeSG(i);
        }
    }
}

// 测试示例
int main() {
    // 构建一个简单的有向图
    // 节点0 -> 节点1, 节点2
    // 节点1 -> 节点3
    // 节点2 -> 节点3
    // 节点3 -> (无后继)
    
    // 初始化图
    int i;
    for (i = 0; i < MAXN; i++) {
        degree[i] = 0;
    }
    
    // 添加边 0 -> 1
    graph[0][degree[0]] = 1;
    degree[0]++;
    
    // 添加边 0 -> 2
    graph[0][degree[0]] = 2;
    degree[0]++;
    
    // 添加边 1 -> 3
    graph[1][degree[1]] = 3;
    degree[1]++;
    
    // 添加边 2 -> 3
    graph[2][degree[2]] = 3;
    degree[2]++;
    
    // 构建SG函数
    buildSG(4);
    
    // 示例1: 棋子在节点0
    int chess1[1] = {0};
    int result1 = solve(chess1, 1, 4);
    // 预期结果: 1 (WIN)
    
    // 示例2: 棋子在节点1和节点2
    int chess2[2] = {1, 2};
    int result2 = solve(chess2, 2, 4);
    // 预期结果: 0 (LOSE)
    
    return 0;
}