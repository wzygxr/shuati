#include <iostream>
#include <vector>
#include <queue>
#include <cstring>
using namespace std;

/**
 * UVA 10305 - Ordering Tasks
 * 
 * 题目描述：
 * 给定n个任务和m个任务之间的先后顺序关系，要求输出一个满足所有约束条件的任务执行顺序。
 * 
 * 解题思路：
 * 这是一道经典的拓扑排序模板题。我们可以使用Kahn算法来解决：
 * 1. 计算每个节点的入度
 * 2. 将所有入度为0的节点加入队列
 * 3. 不断从队列中取出节点，将其加入结果序列，并将其所有邻居节点的入度减1
 * 4. 如果邻居节点的入度变为0，则将其加入队列
 * 5. 重复步骤3-4直到队列为空
 * 
 * 时间复杂度：O(V + E)，其中V是节点数，E是边数
 * 空间复杂度：O(V + E)
 * 
 * 测试链接：https://vjudge.net/problem/UVA-10305
 */

using namespace std;

const int MAXN = 105;  // 最大节点数

vector<int> graph[MAXN];  // 邻接表
int inDegree[MAXN];       // 入度数组
int n, m;                 // 节点数和边数

/**
 * 拓扑排序函数
 * @param result 存储拓扑排序结果的数组
 * @return 拓扑排序结果的长度
 */
int topologicalSort(int result[]) {
    queue<int> q;
    int count = 0;
    
    // 将所有入度为0的节点加入队列
    for (int i = 1; i <= n; i++) {
        if (inDegree[i] == 0) {
            q.push(i);
        }
    }
    
    // Kahn算法进行拓扑排序
    while (!q.empty()) {
        int current = q.front();
        q.pop();
        result[count++] = current;
        
        // 遍历当前节点的所有邻居
        for (int i = 0; i < graph[current].size(); i++) {
            int neighbor = graph[current][i];
            // 将邻居节点的入度减1
            inDegree[neighbor]--;
            // 如果邻居节点的入度变为0，则加入队列
            if (inDegree[neighbor] == 0) {
                q.push(neighbor);
            }
        }
    }
    
    return count;
}

int main() {
    while (true) {
        cin >> n >> m;
        
        // 输入结束条件
        if (n == 0 && m == 0) {
            break;
        }
        
        // 初始化
        for (int i = 1; i <= n; i++) {
            graph[i].clear();
        }
        memset(inDegree, 0, sizeof(inDegree));
        
        // 读取约束关系
        for (int i = 0; i < m; i++) {
            int u, v;
            cin >> u >> v;
            graph[u].push_back(v);
            inDegree[v]++;
        }
        
        // 拓扑排序
        int result[MAXN];
        int count = topologicalSort(result);
        
        // 输出结果
        for (int i = 0; i < count; i++) {
            if (i > 0) {
                cout << " ";
            }
            cout << result[i];
        }
        cout << endl;
    }
    
    return 0;
}