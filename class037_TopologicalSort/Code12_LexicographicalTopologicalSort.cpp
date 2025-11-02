#include <iostream>
#include <vector>
#include <queue>
#include <functional>

using namespace std;

/**
 * 字典序最小拓扑排序 - C++实现
 * 题目解析：输出字典序最小的拓扑排序序列
 * 
 * 算法思路：
 * 1. 使用邻接表存储图结构
 * 2. 计算每个节点的入度
 * 3. 使用优先队列（最小堆）存储入度为0的节点
 * 4. 每次取出编号最小的节点进行处理
 * 
 * 时间复杂度：O((V + E) log V)
 * 空间复杂度：O(V + E)
 * 
 * 工程化考虑：
 * 1. 使用priority_queue实现最小堆
 * 2. 输入输出优化：使用scanf/printf
 * 3. 边界处理：空图、有环图等情况
 * 4. 模块化设计：分离建图和拓扑排序逻辑
 */
const int MAXN = 100001;

vector<int> graph[MAXN];
int indegree[MAXN];
int result[MAXN];
int n, m;

bool lexicographicalTopologicalSort() {
    // 使用最小堆（greater<int>）
    priority_queue<int, vector<int>, greater<int>> minHeap;
    int size = 0;
    
    // 将所有入度为0的节点加入最小堆
    for (int i = 1; i <= n; i++) {
        if (indegree[i] == 0) {
            minHeap.push(i);
        }
    }
    
    while (!minHeap.empty()) {
        int u = minHeap.top();
        minHeap.pop();
        result[size++] = u;
        
        // 遍历u的所有邻居
        for (int v : graph[u]) {
            if (--indegree[v] == 0) {
                minHeap.push(v);
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
    
    if (lexicographicalTopologicalSort()) {
        for (int i = 0; i < n; i++) {
            printf("%d ", result[i]);
        }
        printf("\n");
    } else {
        printf("-1\n");
    }
    
    return 0;
}