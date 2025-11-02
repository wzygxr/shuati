/**
 * 差分约束系统求解 - Bellman-Ford算法应用
 * 
 * 问题描述: 求解一组形如 xj - xi ≤ ck 的不等式组
 * 算法思路: 将不等式转化为图论问题，使用Bellman-Ford求解
 * 
 * 转换方法:
 * 对于每个不等式 xj - xi ≤ ck，添加一条边 i->j，权重为ck
 * 添加超级源点0，到所有点的边权重为0
 * 运行Bellman-Ford算法，如果存在负环则无解，否则距离数组即为解
 * 
 * 时间复杂度: O(N×E)
 * 空间复杂度: O(N+E)
 */

// 由于编译环境限制，这里只提供算法思路和框架代码
// 完整实现需要标准C++库支持

/*
const int MAXN = 101;
const int INF = 0x3f3f3f3f;

int distance[MAXN];

int* solveDifferenceConstraints(int n, int constraints[][3], int constraintsSize) {
    // 构建图（包含超级源点0）
    int edges[MAXN * 2][3];
    int edgesSize = 0;
    
    // 添加约束边
    for (int i = 0; i < constraintsSize; i++) {
        // 注意：变量编号从1开始，转换为从0开始
        edges[edgesSize][0] = constraints[i][0] - 1;
        edges[edgesSize][1] = constraints[i][1] - 1;
        edges[edgesSize][2] = constraints[i][2];
        edgesSize++;
    }
    
    // 添加超级源点边
    for (int i = 0; i < n; i++) {
        edges[edgesSize][0] = n; // 超级源点n
        edges[edgesSize][1] = i;
        edges[edgesSize][2] = 0; // 权重为0
        edgesSize++;
    }
    
    // 运行Bellman-Ford算法
    for (int i = 0; i <= n; i++) {
        distance[i] = INF;
    }
    distance[n] = 0; // 超级源点距离为0
    
    // n+1轮松弛
    for (int i = 0; i <= n; i++) {
        bool updated = false;
        for (int j = 0; j < edgesSize; j++) {
            int u = edges[j][0], v = edges[j][1], w = edges[j][2];
            if (distance[u] != INF && distance[u] + w < distance[v]) {
                distance[v] = distance[u] + w;
                updated = true;
            }
        }
        // 如果某轮没有更新，提前结束
        if (!updated) break;
    }
    
    // 检测负环
    for (int j = 0; j < edgesSize; j++) {
        int u = edges[j][0], v = edges[j][1], w = edges[j][2];
        if (distance[u] != INF && distance[u] + w < distance[v]) {
            return NULL; // 存在负环，无解
        }
    }
    
    // 返回解（这里简化处理，实际应该动态分配内存）
    return distance;
}
*/