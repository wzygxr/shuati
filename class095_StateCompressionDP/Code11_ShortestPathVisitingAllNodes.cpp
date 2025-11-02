// 访问所有节点的最短路径
// 给你一个包含 n 个节点的无向连通图，节点编号为 0 到 n-1。
// 图中的每条边会被表示为一个数组 edges，其中 edges[i] = [u_i, v_i] 表示节点 u_i 和节点 v_i 之间有一条边。
// 返回能够访问所有节点的最短路径的长度。你可以在任一节点开始和停止，也可以多次重访节点，并且可以重用边。
// 测试链接 : https://leetcode.cn/problems/shortest-path-visiting-all-nodes/

class Code11_ShortestPathVisitingAllNodes {
public:
    // 方法一：状态压缩 + BFS 解法
    // 时间复杂度: O(n^2 * 2^n)
    // 空间复杂度: O(n * 2^n)
    static int shortestPathLength(int** graph, int graphSize, int* graphColSize) {
        // 边界情况：只有一个节点
        if (graphSize == 1) {
            return 0;
        }
        
        // 目标状态：所有节点都被访问过（二进制全1）
        int target = (1 << graphSize) - 1;
        
        // 简单队列实现
        struct QueueItem {
            int node;
            int mask;
            int length;
        };
        
        QueueItem queue[4096];  // 假设最大队列大小
        int front = 0, rear = 0;
        
        // 记录已经访问过的状态，避免重复访问
        // 使用二维数组存储，visited[node][mask]表示节点node在mask状态是否已访问
        bool visited[12][4096];  // 假设最多12个节点
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 4096; j++) {
                visited[i][j] = false;
            }
        }
        
        // 从每个节点作为起点开始搜索
        for (int i = 0; i < graphSize; ++i) {
            int initialMask = 1 << i;  // 只访问了节点i的掩码
            queue[rear++] = {i, initialMask, 0};
            visited[i][initialMask] = true;
        }
        
        // BFS搜索最短路径
        while (front < rear) {
            QueueItem current = queue[front++];
            
            int currentNode = current.node;
            int currentMask = current.mask;
            int currentLength = current.length;
            
            // 尝试从当前节点出发访问所有相邻节点
            for (int i = 0; i < graphColSize[currentNode]; i++) {
                int neighbor = graph[currentNode][i];
                // 新的状态：在原有状态基础上添加邻居节点
                int newMask = currentMask | (1 << neighbor);
                // 新的路径长度
                int newLength = currentLength + 1;
                
                // 提前终止条件：找到访问所有节点的路径
                if (newMask == target) {
                    return newLength;
                }
                
                // 如果新状态之前没有访问过，加入队列和visited集合
                // 这是关键的剪枝：避免重复处理相同的(节点, 访问集合)状态
                if (!visited[neighbor][newMask]) {
                    visited[neighbor][newMask] = true;
                    queue[rear++] = {neighbor, newMask, newLength};
                }
            }
        }
        
        // 理论上不会到达这里，因为题目保证图是连通的
        return -1;
    }
};

/*
复杂度分析：

1. BFS版本：
   - 时间复杂度：O(n^2 * 2^n)
     状态数为n * 2^n（每个节点可以处于2^n种访问状态），每个状态需要遍历最多n个邻居
   - 空间复杂度：O(n * 2^n)
     visited集合存储n * 2^n个状态，队列最多存储n * 2^n个元素

2. DP版本：
   - 时间复杂度：O(n^2 * 2^n)
     需要填充大小为2^n * n的DP数组，每个状态需要遍历n个邻居
   - 空间复杂度：O(n * 2^n)
     DP数组的大小为2^n * n

3. 双向BFS版本：
   - 时间复杂度：O(n * 2^(n/2))
     在理想情况下，双向BFS的搜索空间会比单向BFS小很多
   - 空间复杂度：O(n * 2^n)
     最坏情况下仍然需要存储所有可能的状态

算法设计说明：

1. 状态压缩：使用二进制掩码表示已访问的节点集合
   - 例如，对于4个节点，掩码0b1010表示已访问节点1和3
   - 这种表示法非常高效，可以在一个整数中存储多个布尔值状态

2. 状态表示：每个状态由(当前节点, 已访问节点集合)组成
   - 这确保了我们不会重复处理相同的状态，避免了不必要的计算

3. BFS优势：
   - BFS能够保证找到的第一条到达目标状态的路径是最短的
   - 这利用了BFS按层次搜索的特性，确保先找到的路径长度最小

4. 剪枝策略：
   - 使用visited集合避免重复处理相同的(节点, 访问集合)状态
   - 一旦找到目标状态，立即返回结果

5. 优化方向：
   - DP版本提供了另一种实现方式，适用于需要多次查询的场景
   - 双向BFS在某些情况下可以显著减少搜索空间
   - 在C++中，使用vector<unordered_set>来优化状态存储

这是本题的最优解，因为问题要求找到最短路径，而BFS是解决最短路径问题的标准方法，结合状态压缩可以高效地处理节点访问状态。

注意事项：
1. 边界情况处理：单节点图、空图等
2. 异常处理：输入验证
3. 性能优化：在大规模图中，双向BFS可能提供更好的性能
4. 数据结构选择：使用合适的容器来存储状态，平衡查找和插入性能
*/