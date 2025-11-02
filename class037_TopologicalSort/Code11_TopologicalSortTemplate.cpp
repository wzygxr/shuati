#include <iostream>
#include <vector>
#include <queue>
#include <cstring>

using namespace std;

/**
 * 拓扑排序模板题 - C++实现
 * 题目解析：对有向无环图进行拓扑排序，检测环的存在
 * 
 * 算法思路：
 * 1. 使用邻接表存储图结构
 * 2. 计算每个节点的入度
 * 3. 使用队列进行BFS拓扑排序
 * 4. 检测结果序列长度判断是否有环
 * 
 * 时间复杂度：O(V + E)
 * 空间复杂度：O(V + E)
 * 
 * 工程化考虑：
 * 1. 使用vector存储邻接表，提高内存效率
 * 2. 输入输出优化：使用scanf/printf
 * 3. 边界处理：空图、有环图等情况
 * 4. 模块化设计：分离建图和拓扑排序逻辑
 */
const int MAXN = 100001;

vector<int> graph[MAXN];
int indegree[MAXN];
int result[MAXN];
int n, m;

bool topologicalSort() {
    queue<int> q;
    int size = 0;
    
    // 将所有入度为0的节点加入队列
    for (int i = 1; i <= n; i++) {
        if (indegree[i] == 0) {
            q.push(i);
        }
    }
    
    while (!q.empty()) {
        int u = q.front();
        q.pop();
        result[size++] = u;
        
        // 遍历u的所有邻居
        for (int v : graph[u]) {
            if (--indegree[v] == 0) {
                q.push(v);
            }
        }
    }
    
    return size == n;
}

int main() {
    scanf("%d%d", &n, &m);
    
    // 初始化
    for (int i = 1; i <= n; i++) {
        graph[i].clear();
        indegree[i] = 0;
    }
    
    // 建图
    for (int i = 0; i < m; i++) {
        int u, v;
        scanf("%d%d", &u, &v);
        graph[u].push_back(v);
        indegree[v]++;
    }
    
    if (topologicalSort()) {
        for (int i = 0; i < n; i++) {
            printf("%d ", result[i]);
        }
        printf("\n");
    } else {
        printf("-1\n");
    }
    
    return 0;
}