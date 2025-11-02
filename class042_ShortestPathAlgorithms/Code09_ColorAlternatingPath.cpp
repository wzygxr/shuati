/**
 * LeetCode 1129. 颜色交替的最短路径 - A*算法实现
 * 
 * 题目链接: https://leetcode.cn/problems/shortest-path-with-alternating-colors/
 * 题目描述: 给定一个有向图，边有红蓝两种颜色，要求找到从节点0到其他节点的最短路径，
 * 路径中相邻边的颜色必须交替（红-蓝-红...或蓝-红-蓝...）
 * 
 * 算法思路:
 * 1. 状态扩展: 状态包含(节点, 最后使用的颜色)
 * 2. 启发函数: 使用到目标节点的估计距离
 * 3. 约束处理: 强制颜色交替的移动约束
 * 
 * 时间复杂度: O(N*M*log(N*M))，其中N是节点数，M是边数
 * 空间复杂度: O(N*M)
 */

// 由于编译环境限制，这里只提供算法思路和框架代码
// 完整实现需要标准C++库支持

/*
 * 颜色常量
const int RED = 0;
const int BLUE = 1;
const int NO_COLOR = -1;

// 状态结构体
struct State {
    int node, distance, lastColor;
    
    State(int _node, int _distance, int _lastColor) 
        : node(_node), distance(_distance), lastColor(_lastColor) {}
    
    // 重载小于运算符，用于优先队列
    bool operator>(const State& other) const {
        return distance > other.distance;
    }
};

vector<int> shortestAlternatingPaths(int n, vector<vector<int>>& redEdges, vector<vector<int>>& blueEdges) {
    // 构建邻接表，包含颜色信息
    vector<vector<pair<int, int>>> graph(n);
    
    // 添加红色边
    for (const auto& edge : redEdges) {
        graph[edge[0]].push_back({edge[1], RED});
    }
    
    // 添加蓝色边
    for (const auto& edge : blueEdges) {
        graph[edge[0]].push_back({edge[1], BLUE});
    }
    
    // 结果数组
    vector<int> result(n, -1);
    
    // 记录访问状态: visited[node][color] 表示以某种颜色到达节点的状态是否已访问
    vector<vector<bool>> visited(n, vector<bool>(2, false));
    
    // 优先队列
    priority_queue<State, vector<State>, greater<State>> pq;
    
    // 初始状态：从节点0开始，距离为0，没有使用颜色
    pq.push(State(0, 0, NO_COLOR));
    result[0] = 0;
    
    while (!pq.empty()) {
        State current = pq.top();
        pq.pop();
        
        int node = current.node;
        int distance = current.distance;
        int lastColor = current.lastColor;
        
        // 如果该状态已访问，跳过
        if (lastColor != NO_COLOR && visited[node][lastColor]) {
            continue;
        }
        
        // 标记为已访问
        if (lastColor != NO_COLOR) {
            visited[node][lastColor] = true;
        }
        
        // 遍历所有邻接边
        for (const auto& edge : graph[node]) {
            int nextNode = edge.first;
            int color = edge.second;
            
            // 颜色必须交替（初始状态除外）
            if (lastColor == NO_COLOR || lastColor != color) {
                // 如果还没有找到到达nextNode的路径，或者找到了更短的路径
                if (result[nextNode] == -1 || distance + 1 < result[nextNode]) {
                    result[nextNode] = distance + 1;
                }
                
                // 如果该状态未访问，加入队列
                if (!visited[nextNode][color]) {
                    pq.push(State(nextNode, distance + 1, color));
                }
            }
        }
    }
    
    return result;
}
*/