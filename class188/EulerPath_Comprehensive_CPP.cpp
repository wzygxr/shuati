/**
 * 欧拉路径算法完整实现 - C++版（面试准备）
 * 包含有向图和无向图的欧拉路径判定与构造，以及完整的面试知识点讲解
 */

// 包含iostream头文件，提供基本输入输出流功能
// 面试要点：C++标准库的使用和输入输出机制
// ML/DL关联：数据输入输出管道的设计
#include <iostream>
// 包含vector头文件，提供动态数组容器
// 面试要点：STL容器的特点和适用场景
// ML/DL关联：张量数据结构的底层实现
#include <vector>
// 包含stack头文件，提供栈容器适配器
// 面试要点：栈的特性和在算法中的应用
// ML/DL关联：递归展开为循环的实现机制
#include <stack>
// 包含queue头文件，提供队列容器适配器
// 面试要点：队列在BFS等算法中的应用
// ML/DL关联：广度优先搜索在图神经网络中的应用
#include <queue>
// 包含algorithm头文件，提供各种算法函数
// 面试要点：STL算法的使用和性能特点
// ML/DL关联：排序、查找等基础算法在数据处理中的应用
#include <algorithm>
// 包含unordered_map头文件，提供哈希映射容器
// 面试要点：哈希表的实现原理和性能特点
// ML/DL关联：特征映射和查找表的实现
#include <unordered_map>
// 包含unordered_set头文件，提供哈希集合容器
// 面试要点：集合操作的高效实现
// ML/DL关联：去重和成员查询操作
#include <unordered_set>
// 包含list头文件，提供双向链表容器
// 面试要点：链表操作的灵活性和性能特点
// ML/DL关联：动态数据结构在图表示中的应用
#include <list>
// 使用std命名空间，避免每次使用std::前缀
// 面试要点：命名空间的使用和避免命名冲突
// ML/DL关联：模块化编程在大型项目中的重要性
using namespace std;

/**
 * 欧拉路径求解器类
 * 包含有向图和无向图的欧拉路径判定与构造算法
 * 面试重点：掌握欧拉路径的判定定理和Hierholzer算法
 */
class EulerPathSolver {
public:
    /**
     * 检查有向图是否存在欧拉路径
     * @param edges 边列表，每个元素为{u, v}表示从u到v的有向边
     * @param n 节点数
     * @return pair<bool, int> (是否存在欧拉路径, 起点或-1表示不存在)
     * 
     * 面试考点：有向图欧拉路径判定定理
     */
    pair<bool, int> directedEulerPathCheck(const vector<pair<int, int>>& edges, int n) {
        // 初始化入度和出度数组 - 欧拉路径判定的核心数据结构
        // 创建大小为n+1的入度数组并初始化为0
        // 面试要点：数组大小为n+1是为了方便使用1-n的节点编号
        // ML/DL关联：零填充在卷积操作中的应用
        vector<int> inDeg(n + 1, 0);   // 入度数组，面试需说明初始化为0
        // 创建大小为n+1的出度数组并初始化为0
        // 面试要点：初始化的重要性和默认值选择
        // ML/DL关联：权重初始化对模型收敛的影响
        vector<int> outDeg(n + 1, 0);  // 出度数组
        
        // 构建邻接表 - 图的常用存储方式，面试需掌握
        // 创建二维向量作为邻接表，外层大小为n+1
        // 面试要点：邻接表的优缺点和空间复杂度分析
        // ML/DL关联：稀疏矩阵的存储优化
        vector<vector<int>> adj(n + 1);  // 邻接表，空间复杂度O(V+E)
        
        // 遍历所有边，统计度数并构建图 - 时间复杂度O(E)，面试需说明
        // 使用范围for循环遍历所有边
        // 面试要点：范围for循环的语法和性能特点
        // ML/DL关联：批处理中的循环操作
        for (auto& edge : edges) {  // 使用引用避免拷贝，提升性能
            // 从边中提取起点和终点
            // 面试要点：pair的使用和元素访问
            // ML/DL关联：特征对的表示
            int u = edge.first, v = edge.second;  // 获取边的起点和终点
            // 在邻接表中添加从u到v的边
            // 面试要点：邻接表的边添加操作
            // ML/DL关联：图中节点关系的表示
            adj[u].push_back(v);  // 在邻接表中添加边
            // 增加起点u的出度
            // 面试要点：度数统计的重要性
            // ML/DL关联：节点度数作为图特征
            outDeg[u]++;  // 增加起点出度
            // 增加终点v的入度
            // 面试要点：入度出度的区分
            // ML/DL关联：有向图中节点的流入流出特征
            inDeg[v]++;   // 增加终点入度
        }
        
        // 检查欧拉路径的度数条件 - 有向图欧拉路径判定定理，面试高频考点
        int startNodes = 0;  // 出度比入度多1的节点数（起点候选）
        int endNodes = 0;    // 入度比出度多1的节点数（终点候选）
        int startNode = -1;  // 记录起点
        
        for (int i = 1; i <= n; i++) {  // 遍历所有节点检查度数
            int diff = outDeg[i] - inDeg[i];  // 计算度数差
            
            if (diff == 1) {  // 出度比入度多1，可能是起点
                startNodes++;      // 增加起点候选数
                startNode = i;     // 记录该节点作为起点候选
            } else if (diff == -1) {  // 入度比出度多1，可能是终点
                endNodes++;  // 增加终点候选数
            } else if (diff != 0) {  // 度数差不为-1, 0, 1，违反欧拉路径条件
                return {false, -1};  // 不满足条件，返回false
            }
        }
        
        // 验证度数条件 - 欧拉路径判定定理的核心应用
        if (startNodes == 0 && endNodes == 0) {  // 欧拉回路情况
            // 所有节点度数平衡，任选一个有出度的节点作为起点
            for (int i = 1; i <= n; i++) {  // 寻找起始节点
                if (outDeg[i] > 0) {  // 如果有出度
                    return {true, i};  // 返回该节点作为起点
                }
            }
            // 如果所有节点度数都为0，返回节点1
            return {true, 1};
        } else if (startNodes == 1 && endNodes == 1) {  // 欧拉路径情况
            // 恰好有一个起点和一个终点，满足欧拉路径条件
            return {true, startNode};  // 返回找到的起点
        } else {  // 不满足欧拉路径度数条件
            return {false, -1};  // 返回false
        }
    }

    /**
     * 检查无向图是否存在欧拉路径
     * @param edges 边列表，每个元素为{u, v}表示u和v之间的无向边
     * @param n 节点数
     * @return pair<bool, int> (是否存在欧拉路径, 起点或-1表示不存在)
     * 
     * 面试考点：无向图欧拉路径判定定理
     */
    pair<bool, int> undirectedEulerPathCheck(const vector<pair<int, int>>& edges, int n) {
        // 统计度数 - 无向图欧拉路径判定的核心
        vector<int> degree(n + 1, 0);  // 度数数组
        
        // 构建邻接表
        vector<vector<int>> adj(n + 1);
        
        // 遍历所有边，统计度数并构建图
        for (auto& edge : edges) {
            int u = edge.first, v = edge.second;
            adj[u].push_back(v);  // 无向边添加双向连接
            adj[v].push_back(u);
            degree[u]++;  // 两端点度数都增加
            degree[v]++;
        }
        
        // 统计奇度数节点数量 - 无向图欧拉路径判定定理，面试必考
        int oddDegreeCount = 0;  // 奇度数节点计数
        int startNode = -1;      // 起点
        
        for (int i = 1; i <= n; i++) {  // 遍历所有节点
            if (degree[i] % 2 == 1) {  // 如果度数为奇数
                oddDegreeCount++;  // 增加奇度数节点计数
                startNode = i;     // 记录奇度数节点作为起点候选
            }
        }
        
        // 检查奇度数节点数量 - 无向图欧拉路径判定定理的核心
        if (oddDegreeCount == 0) {  // 所有节点度数都为偶数，存在欧拉回路
            // 任选一个度数大于0的节点作为起点
            for (int i = 1; i <= n; i++) {
                if (degree[i] > 0) {
                    return {true, i};
                }
            }
            // 如果所有节点度数都为0，返回节点1
            return {true, 1};
        } else if (oddDegreeCount == 2) {  // 恰好有两个奇度数节点，存在欧拉路径
            return {true, startNode};  // 返回其中一个奇度数节点作为起点
        } else {  // 奇度数节点数不为0也不为2，不存在欧拉路径
            return {false, -1};
        }
    }

    /**
     * 使用Hierholzer算法构造有向图的欧拉路径
     * @param startNode 起点
     * @param edges 边列表
     * @param n 节点数
     * @return vector<int> 欧拉路径的节点序列
     * 
     * 面试重点：Hierholzer算法的实现与复杂度分析
     */
    vector<int> hierholzerDirected(int startNode, const vector<pair<int, int>>& edges, int n) {
        // 构建邻接表并使用链式前向星或邻接表
        vector<list<int>> adj(n + 1);  // 使用list便于删除操作
        
        for (auto& edge : edges) {
            adj[edge.first].push_back(edge.second);  // 添加边
        }
        
        // 使用栈实现Hierholzer算法（迭代版）- 避免递归深度过大导致栈溢出
        stack<int> pathStack;  // 用栈模拟递归过程，面试高频考点
        vector<int> circuit;   // 存储最终电路
        
        pathStack.push(startNode);  // 将起点压入栈
        int current = startNode;    // 当前节点
        
        while (!pathStack.empty()) {  // 当栈不为空时继续
            if (!adj[current].empty()) {  // 如果当前节点还有未访问的边
                pathStack.push(current);              // 将当前节点压入栈
                int next = adj[current].front();      // 获取下一个节点
                adj[current].pop_front();             // 删除已访问的边
                current = next;                       // 移动到下一个节点
            } else {  // 如果当前节点没有未访问的边
                circuit.push_back(current);  // 将当前节点加入路径
                current = pathStack.top();   // 回溯到栈顶节点
                pathStack.pop();             // 弹出栈顶节点
            }
        }
        
        reverse(circuit.begin(), circuit.end());  // 反转路径得到正确顺序 - 算法关键步骤
        return circuit;  // 返回欧拉路径
    }

    /**
     * 使用Hierholzer算法构造无向图的欧拉路径
     * @param startNode 起点
     * @param edges 边列表
     * @param n 节点数
     * @return vector<int> 欧拉路径的节点序列
     */
    vector<int> hierholzerUndirected(int startNode, const vector<pair<int, int>>& edges, int n) {
        // 构建邻接表，使用边索引便于标记已访问
        vector<vector<pair<int, int>>> adj(n + 1);  // {邻接节点, 边索引}
        
        for (int i = 0; i < edges.size(); i++) {
            int u = edges[i].first, v = edges[i].second;
            adj[u].push_back({v, i});
            adj[v].push_back({u, i});
        }
        
        vector<bool> visited(edges.size(), false);  // 标记边是否已访问
        stack<int> pathStack;
        vector<int> circuit;
        
        pathStack.push(startNode);
        int current = startNode;
        
        while (!pathStack.empty()) {
            bool foundUnvisitedEdge = false;
            
            // 查找未访问的边
            for (auto& edge : adj[current]) {
                int neighbor = edge.first;
                int edgeIdx = edge.second;
                
                if (!visited[edgeIdx]) {  // 如果边未被访问
                    visited[edgeIdx] = true;  // 标记为已访问
                    pathStack.push(current);  // 将当前节点压入栈
                    current = neighbor;       // 移动到邻接节点
                    foundUnvisitedEdge = true;
                    break;  // 找到一条未访问边就跳出
                }
            }
            
            if (!foundUnvisitedEdge) {  // 如果没有未访问的边
                circuit.push_back(current);  // 将当前节点加入路径
                if (!pathStack.empty()) {
                    current = pathStack.top();  // 回溯
                    pathStack.pop();
                }
            }
        }
        
        reverse(circuit.begin(), circuit.end());  // 反转路径
        return circuit;
    }

    /**
     * 完整解决有向图欧拉路径问题
     * @param edges 边列表
     * @param n 节点数
     * @return pair<bool, vector<int>> (是否成功, 欧拉路径)
     * 
     * 面试标准流程：判定 -> 构造
     */
    pair<bool, vector<int>> solveDirectedEulerPath(const vector<pair<int, int>>& edges, int n) {
        // 首先检查是否存在欧拉路径
        auto checkResult = directedEulerPathCheck(edges, n);  // 欧拉路径判定
        
        if (!checkResult.first) {  // 如果不存在
            return {false, {}};  // 返回失败
        }
        
        int startNode = checkResult.second;  // 获取起点
        
        // 使用Hierholzer算法构造路径
        vector<int> path = hierholzerDirected(startNode, edges, n);  // 构造欧拉路径
        
        return {true, path};  // 返回成功和路径
    }

    /**
     * 完整解决无向图欧拉路径问题
     * @param edges 边列表
     * @param n 节点数
     * @return pair<bool, vector<int>> (是否成功, 欧拉路径)
     */
    pair<bool, vector<int>> solveUndirectedEulerPath(const vector<pair<int, int>>& edges, int n) {
        // 首先检查是否存在欧拉路径
        auto checkResult = undirectedEulerPathCheck(edges, n);
        
        if (!checkResult.first) {
            return {false, {}};
        }
        
        int startNode = checkResult.second;
        
        // 使用Hierholzer算法构造路径
        vector<int> path = hierholzerUndirected(startNode, edges, n);
        
        return {true, path};
    }
};

/**
 * 主函数：演示欧拉路径算法的使用
 * 面试中常要求现场编写此类演示代码
 */
int main() {
    EulerPathSolver solver;  // 创建求解器实例
    
    cout << "=== 欧拉路径算法C++实现（面试准备） ===" << endl;
    cout << endl;
    
    // 示例1：有向图欧拉路径
    cout << "示例1：有向图欧拉路径" << endl;
    vector<pair<int, int>> directedEdges = {{1, 2}, {2, 3}, {3, 1}, {1, 4}, {4, 3}};  // 有向边列表
    cout << "输入边: ";
    for (auto& edge : directedEdges) {
        cout << "(" << edge.first << "," << edge.second << ") ";
    }
    cout << endl;
    
    auto result = solver.solveDirectedEulerPath(directedEdges, 4);  // 求解
    
    if (result.first) {  // 如果求解成功
        cout << "存在欧拉路径: ";
        for (int i = 0; i < result.second.size(); i++) {  // 输出路径
            cout << result.second[i];
            if (i < result.second.size() - 1) cout << " -> ";
        }
        cout << endl;
    } else {  // 如果求解失败
        cout << "不存在欧拉路径" << endl;  // 输出失败信息
    }
    cout << endl;
    
    // 示例2：无向图欧拉路径
    cout << "示例2：无向图欧拉路径" << endl;
    vector<pair<int, int>> undirectedEdges = {{1, 2}, {2, 3}, {3, 4}, {4, 1}, {1, 3}};  // 无向边列表
    cout << "输入边: ";
    for (auto& edge : undirectedEdges) {
        cout << "(" << edge.first << "," << edge.second << ") ";
    }
    cout << endl;
    
    result = solver.solveUndirectedEulerPath(undirectedEdges, 4);  // 求解
    
    if (result.first) {
        cout << "存在欧拉路径: ";
        for (int i = 0; i < result.second.size(); i++) {
            cout << result.second[i];
            if (i < result.second.size() - 1) cout << " - ";
        }
        cout << endl;
    } else {
        cout << "不存在欧拉路径" << endl;
    }
    cout << endl;
    
    // 示例3：面试常见问题 - 无效情况
    cout << "示例3：无效情况（度数不满足条件）" << endl;
    vector<pair<int, int>> invalidEdges = {{1, 2}, {2, 3}, {3, 4}, {4, 5}, {5, 1}, {1, 6}};  // 度数不满足条件
    cout << "输入边: ";
    for (auto& edge : invalidEdges) {
        cout << "(" << edge.first << "," << edge.second << ") ";
    }
    cout << endl;
    
    result = solver.solveDirectedEulerPath(invalidEdges, 6);
    
    if (result.first) {
        cout << "存在欧拉路径: ";
        for (int i = 0; i < result.second.size(); i++) {
            cout << result.second[i];
            if (i < result.second.size() - 1) cout << " -> ";
        }
        cout << endl;
    } else {
        cout << "不存在欧拉路径（度数条件不满足）" << endl;
    }
    cout << endl;
    
    cout << "=== 面试知识点总结 ===" << endl;
    cout << "1. 欧拉路径判定定理：" << endl;
    cout << "   - 有向图：最多一个起点(出度-入度=1)，最多一个终点(入度-出度=1)，其余度数平衡" << endl;
    cout << "   - 无向图：奇度数节点数为0(回路)或2(路径)" << endl;
    cout << "2. Hierholzer算法：O(E)时间复杂度，使用栈避免递归" << endl;
    cout << "3. 图的表示：邻接表O(V+E)空间，适合稀疏图" << endl;
    cout << "4. 边界处理：空图、孤立点、重边、自环" << endl;
    
    return 0;  // 程序正常结束
}