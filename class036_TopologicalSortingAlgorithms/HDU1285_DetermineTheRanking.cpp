#include <iostream>
#include <vector>
#include <queue>
#include <cstring>
using namespace std;

/**
 * HDU 1285 - 确定比赛名次
 * 
 * 题目描述：
 * 有N个比赛队（1<=N<500），编号依次为1，2，3，...，N进行比赛，比赛结束后，
 * 裁判委员会要将所有参赛队伍从前往后依次排名，但现在裁判委员会不能直接获得
 * 每个队的比赛成绩，只知道每场比赛的结果，即P1赢P2，用P1,P2表示，排名时P1在P2之前。
 * 现在请你编程序确定排名。
 * 
 * 注意：符合条件的排名可能不是唯一的，此时要求输出时编号小的队伍在前；
 * 输入数据保证是正确的，即输入数据确保一定能有一个符合要求的排名。
 * 
 * 解题思路：
 * 这是一道典型的拓扑排序题，但要求输出字典序最小的拓扑序列。
 * 为了实现字典序最小，我们在选择入度为0的节点时，使用优先队列（最小堆），
 * 每次选择编号最小的节点。
 * 
 * 算法步骤：
 * 1. 构建图和入度数组
 * 2. 将所有入度为0的节点加入优先队列
 * 3. 不断从优先队列中取出编号最小的节点，加入结果序列
 * 4. 将该节点的所有邻居节点入度减1
 * 5. 如果邻居节点入度变为0，则加入优先队列
 * 6. 重复3-5直到队列为空
 * 
 * 时间复杂度：O(V log V + E)，优先队列操作的复杂度
 * 空间复杂度：O(V + E)
 * 
 * 测试链接：http://acm.hdu.edu.cn/showproblem.php?pid=1285
 */

const int MAXN = 505;

vector<int> graph[MAXN];  // 邻接表
int inDegree[MAXN];       // 入度数组
int n, m;                 // 队伍数量和比赛结果数量

/**
 * 字典序最小的拓扑排序
 * @param result 存储拓扑排序结果的数组
 * @return 拓扑排序结果的长度
 */
int topologicalSortLexicographically(int result[]) {
    priority_queue<int, vector<int>, greater<int>> pq;  // 最小堆
    int count = 0;
    
    // 将所有入度为0的节点加入优先队列
    for (int i = 1; i <= n; i++) {
        if (inDegree[i] == 0) {
            pq.push(i);
        }
    }
    
    // Kahn算法进行拓扑排序
    while (!pq.empty()) {
        // 取出编号最小的节点
        int current = pq.top();
        pq.pop();
        result[count++] = current;
        
        // 遍历当前节点的所有邻居
        for (int i = 0; i < graph[current].size(); i++) {
            int neighbor = graph[current][i];
            // 将邻居节点的入度减1
            inDegree[neighbor]--;
            // 如果邻居节点的入度变为0，则加入队列
            if (inDegree[neighbor] == 0) {
                pq.push(neighbor);
            }
        }
    }
    
    return count;
}

int main() {
    while (cin >> n >> m) {
        // 初始化
        for (int i = 1; i <= n; i++) {
            graph[i].clear();
        }
        memset(inDegree, 0, sizeof(inDegree));
        
        // 读取比赛结果
        for (int i = 0; i < m; i++) {
            int winner, loser;
            cin >> winner >> loser;
            graph[winner].push_back(loser);
            inDegree[loser]++;
        }
        
        // 拓扑排序（字典序最小）
        int result[MAXN];
        int count = topologicalSortLexicographically(result);
        
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