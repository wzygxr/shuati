/**
 * 最小圈问题 - 01分数规划解法
 * 
 * <h3>题目信息</h3>
 * <ul>
 *   <li><strong>题目来源</strong>：Luogu P3199</li>
 *   <li><strong>题目描述</strong>：给定一个有向带权图，求所有环的平均值中最小的平均值。
 *   环的平均值定义为：环中边的权值和 / 环中边的数量。</li>
 *   <li><strong>数据范围</strong>：
 *     <ul>
 *       <li>1 <= n <= 3000（节点数）</li>
 *       <li>1 <= m <= 10000（边数）</li>
 *       <li>-10^7 <= 边权 <= 10^7</li>
 *     </ul>
 *   </li>
 *   <li><strong>测试链接</strong>：<a href="https://www.luogu.com.cn/problem/P3199">Luogu P3199</a></li>
 * </ul>
 * 
 * <h3>算法思路</h3>
 * <p>使用01分数规划 + 二分查找 + DFS判负环的方法：</p>
 * <ol>
 *   <li><strong>01分数规划</strong>：将环平均值最小化问题转化为判定性问题</li>
 *   <li><strong>二分查找</strong>：在可能的平均值范围内进行二分</li>
 *   <li><strong>DFS判负环</strong>：通过DFS递归判断是否存在负环</li>
 * </ol>
 * 
 * <h3>数学原理</h3>
 * <p>我们需要最小化 R = (Σw(e)) / |C|，其中e∈环C，|C|是环的边数。</p>
 * <p>对于给定的L，判断是否存在环C使得 R < L：</p>
 * <ul>
 *   <li>等价于：Σw(e) < L * |C|</li>
 *   <li>等价于：Σ(w(e) - L) < 0</li>
 * </ul>
 * <p>这相当于将每条边的权值更新为(w(e) - L)，然后判断图中是否存在负环。</p>
 * 
 * <h3>复杂度分析</h3>
 * <ul>
 *   <li><strong>时间复杂度</strong>：O(log(1/ε) * n * m)，其中ε是精度要求（本题中取1e-9）</li>
 *   <li><strong>空间复杂度</strong>：O(n + m)，使用邻接表存储图结构</li>
 * </ul>
 * 
 * <h3>注意事项</h3>
 * <ul>
 *   <li>使用DFS判负环比SPFA更高效，具有更强的剪枝能力</li>
 *   <li>需要处理精度问题，避免浮点数比较误差</li>
 *   <li>图可能不连通，需要从每个节点开始搜索</li>
 * </ul>
 */

#include <iostream>
#include <cstring>
#include <vector>
#include <iomanip>
using namespace std;

// 常量定义
const int MAXN = 3001;        // 最大节点数
const int MAXM = 10001;       // 最大边数
const double MAXE = 1e7;      // 最大边权绝对值
const double PRECISION = 1e-9; // 精度控制

// 边结构体，用于邻接表存储
struct Edge {
    int to;     // 目标节点
    double w;   // 边的权值
    Edge(int to, double w) : to(to), w(w) {}
};

// 图的邻接表存储（比链式前向星更适合C++）
vector<vector<Edge>> graph;

// DFS判负环需要的数据结构
double value[MAXN]; // 每个点的累积边权
bool path[MAXN];    // 每个点是否在当前递归路径上

// 全局变量，存储节点数和边数
int n, m;

/**
 * DFS递归判断负环
 * 
 * <p>这是SPFA算法的递归实现版本，具有更强的剪枝能力：</p>
 * <ul>
 *   <li>只有当可以松弛时才继续递归，避免不必要的搜索</li>
 *   <li>使用路径标记检测环的存在</li>
 *   <li>从每个节点开始搜索，确保覆盖所有连通分量</li>
 * </ul>
 * 
 * @param u 当前访问的节点
 * @param L 当前尝试的比率值
 * @return 如果从当前节点出发存在负环，返回true；否则返回false
 */
bool dfs(int u, double L) {
    // 标记当前节点在递归路径上
    path[u] = true;
    
    // 遍历当前节点的所有出边
    for (const Edge& e : graph[u]) {
        int v = e.to;
        // 更新边权：w(e) - L
        double w = e.w - L;
        
        // 松弛操作：如果通过u到v可以使value[v]更小
        if (value[v] > value[u] + w) {
            value[v] = value[u] + w;
            
            // 如果v已经在当前递归路径上，说明找到了负环
            // 或者从v出发递归找到了负环
            if (path[v] || dfs(v, L)) {
                return true;
            }
        }
    }
    
    // 回溯：标记当前节点不在递归路径上
    path[u] = false;
    return false;
}

/**
 * 检查函数：判断给定的平均值L是否可行
 * 
 * <p>核心思想：将原图的边权更新为(w(e) - L)，然后判断图中是否存在负环。</p>
 * <p>如果存在负环，说明存在平均值小于L的环；否则说明当前平均值过大。</p>
 * 
 * @param L 当前尝试的平均值
 * @return 如果存在平均值小于L的环，返回true；否则返回false
 */
bool check(double L) {
    // 初始化距离数组和路径标记数组
    memset(value, 0, sizeof(value));
    memset(path, false, sizeof(path));
    
    // 从每个节点开始DFS搜索，确保覆盖所有连通分量
    for (int i = 1; i <= n; ++i) {
        if (dfs(i, L)) {
            return true;
        }
    }
    
    return false;
}

/**
 * 主函数：处理输入输出，执行二分查找算法
 * 
 * <p>算法流程：</p>
 * <ol>
 *   <li>读取输入数据（节点数、边数、边权）</li>
 *   <li>初始化二分查找的左右边界</li>
 *   <li>进行二分查找，每次调用check函数判断当前平均值是否可行</li>
 *   <li>输出结果，保留8位小数</li>
 * </ol>
 */
int main() {
    // 读取节点数和边数
    cin >> n >> m;
    
    // 初始化图的邻接表
    graph.resize(n + 1);
    
    // 读取每条边的信息并添加到图中
    for (int i = 0; i < m; ++i) {
        int u, v;
        double w;
        cin >> u >> v >> w;
        graph[u].emplace_back(v, w);
    }
    
    // 初始化二分查找的左右边界
    // 左边界为最小可能边权，右边界为最大可能边权
    double left = -MAXE;
    double right = MAXE;
    double result = 0.0;
    
    // 二分查找过程
    while (left < right && right - left >= PRECISION) {
        double mid = (left + right) / 2.0;
        
        if (check(mid)) {
            // 如果存在平均值小于mid的环，调整右边界
            right = mid - PRECISION;
        } else {
            // 否则记录当前结果并调整左边界
            result = mid;
            left = mid + PRECISION;
        }
    }
    
    // 输出结果，保留8位小数
    cout << fixed << setprecision(8) << result << endl;
    
    return 0;
}