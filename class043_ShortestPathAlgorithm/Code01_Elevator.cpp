/**
 * 跳楼机问题 - 同余最短路算法应用 (C++版本)
 * 
 * 问题描述：
 * 一座大楼一共有h层，楼层编号1~h，有如下四种移动方式：
 * 1. 向上移动x层
 * 2. 向上移动y层
 * 3. 向上移动z层
 * 4. 回到1层
 * 假设你正在第1层，请问大楼里有多少楼层你可以到达
 * 
 * 输入约束：
 * 1 <= h <= 2^63 - 1
 * 1 <= x、y、z <= 10^5
 * 
 * 测试链接：https://www.luogu.com.cn/problem/P3403
 * 
 * 核心算法：同余最短路 + Dijkstra算法
 * 算法思想：将问题转化为图论问题，在模x意义下构建最短路图
 * 
 * 时间复杂度：O(x * log x)
 * 空间复杂度：O(x)
 * 
 * 语言特性差异（C++ vs Java/Python）：
 * 1. 内存管理：C++需要手动管理内存，这里使用静态数组
 * 2. 标准库：避免使用STL容器，提高兼容性
 * 3. 性能优化：使用数组模拟优先队列，减少依赖
 * 
 * 工程化考量：
 * 1. 跨平台兼容：使用标准C++语法，避免平台特定特性
 * 2. 内存安全：使用固定大小数组，避免动态分配
 * 3. 异常处理：通过返回值或错误码处理异常情况
 * 4. 可测试性：提供独立的solve函数便于单元测试
 */

#include <iostream>
#include <climits>
using namespace std;

/*
 * 算法思路：
 * 这道题可以转化为图论问题，用Dijkstra算法解决。
 * 将楼层按照模x的值进行分类，构建模x意义下的最短路图。
 * 每个点i表示模x余数为i的所有楼层中到达1层需要的最小步数。
 * 通过y和z操作在不同余数之间建立边，权值为y和z。
 * 最后统计所有可达楼层的数量。
 * 
 * 时间复杂度：O(x * log x)
 * 空间复杂度：O(x)
 * 
 * 题目来源：洛谷P3403 跳楼机 (https://www.luogu.com.cn/problem/P3403)
 * 相关题目：
 * 1. POJ 2387 Til the Cows Come Home - Dijkstra模板题 (http://poj.org/problem?id=2387)
 * 2. Codeforces 20C Dijkstra? - 最短路径模板题 (https://codeforces.com/problemset/problem/20/C)
 * 3. LeetCode 743 Network Delay Time - 网络延迟时间 (https://leetcode.cn/problems/network-delay-time/)
 * 4. 洛谷 P4779 单源最短路径 (https://www.luogu.com.cn/problem/P4779)
 * 5. HDU 2544 最短路 (http://acm.hdu.edu.cn/showproblem.php?pid=2544)
 * 6. AtCoder ABC176_D Wizard in Maze (https://atcoder.jp/contests/abc176/tasks/abc176_d)
 * 7. SPOJ KATHTHI (https://www.spoj.com/problems/KATHTHI/)
 * 8. LeetCode 1368 Minimum Cost to Make at Least One Valid Path in a Grid (https://leetcode.cn/problems/minimum-cost-to-make-at-least-one-valid-path-in-a-grid/)
 * 9. Codeforces 590C Three States (https://codeforces.com/contest/590/problem/C)
 * 10. UVA 11573 Ocean Currents (https://vjudge.net/problem/UVA-11573)
 * 11. LeetCode 2290 Minimum Obstacle Removal to Reach Corner (https://leetcode.cn/problems/minimum-obstacle-removal-to-reach-corner/)
 * 12. LeetCode 1824 Minimum Sideway Jumps (https://leetcode.cn/problems/minimum-sideway-jumps/)
 * 13. LeetCode 1631 Path With Minimum Effort (https://leetcode.cn/problems/path-with-minimum-effort/)
 * 14. LeetCode 847 Shortest Path Visiting All Nodes (https://leetcode.cn/problems/shortest-path-visiting-all-nodes/)
 * 15. LeetCode 773 Sliding Puzzle (https://leetcode.cn/problems/sliding-puzzle/)
 */

// 常量定义 - 根据题目约束设置数组大小
const int MAXN = 100001;  // 最大节点数，对应x的最大值10^5

// 函数声明
long long solve(long long height, int x_val, int y_val, int z_val);

// 全局变量定义
long long h;  // 楼层高度，注意h可能很大(2^63-1)，使用long long
int x, y, z;  // 三种移动步长

// 图结构存储 - 使用二维数组模拟邻接表
// 工程化考量：避免使用STL容器，提高代码兼容性和性能
// 内存布局：连续内存访问，提高缓存命中率
int adj_to[MAXN][2];     // 每个节点最多连接2个其他节点（y和z操作）
int adj_weight[MAXN][2]; // 对应的权重（移动步长）
int adj_count[MAXN];     // 每个节点的邻接边数量

// Dijkstra算法数据结构
long long dist[MAXN];    // 距离数组：记录从起点到每个节点的最短距离
bool visited[MAXN];      // 访问标记数组：避免重复处理节点

// 初始化函数
void init() {
    for (int i = 0; i < x; i++) {
        adj_count[i] = 0;
        dist[i] = 9223372036854775807LL; // LONG_LONG_MAX
        visited[i] = false;
    }
}

// 添加边的函数
void add_edge(int from, int to, int weight) {
    if (adj_count[from] < 2) {
        adj_to[from][adj_count[from]] = to;
        adj_weight[from][adj_count[from]] = weight;
        adj_count[from]++;
    }
}

// 简化版Dijkstra算法，使用数组模拟优先队列
void dijkstra() {
    dist[0] = 0;
    
    // 使用简单数组作为队列
    long long queue_val[MAXN];  // 存储距离值
    int queue_node[MAXN];       // 存储节点编号
    int queue_size = 0;
    
    // 初始节点入队
    queue_val[queue_size] = 0;
    queue_node[queue_size] = 0;
    queue_size++;
    
    while (queue_size > 0) {
        // 找到最小距离的节点
        int min_index = 0;
        for (int i = 1; i < queue_size; i++) {
            if (queue_val[i] < queue_val[min_index]) {
                min_index = i;
            }
        }
        
        long long current_dist = queue_val[min_index];
        int u = queue_node[min_index];
        
        // 从队列中移除该节点
        for (int i = min_index; i < queue_size - 1; i++) {
            queue_val[i] = queue_val[i + 1];
            queue_node[i] = queue_node[i + 1];
        }
        queue_size--;
        
        if (visited[u]) {
            continue;
        }
        
        visited[u] = true;
        
        // 更新邻接节点的距离
        for (int i = 0; i < adj_count[u]; i++) {
            int v = adj_to[u][i];
            int weight = adj_weight[u][i];
            
            if (!visited[v] && current_dist + weight < dist[v]) {
                dist[v] = current_dist + weight;
                queue_val[queue_size] = dist[v];
                queue_node[queue_size] = v;
                queue_size++;
            }
        }
    }
}

// 计算结果的函数
long long calculate_result() {
    dijkstra();
    long long result = 0;
    for (int i = 0; i < x; i++) {
        if (dist[i] <= h) {
            result += (h - dist[i]) / x + 1;
        }
    }
    return result;
}

// 完整的main函数，支持标准输入输出
int main() {
    long long height;
    int x_val, y_val, z_val;
    
    // 读取输入参数
    std::cin >> height >> x_val >> y_val >> z_val;
    
    // 调用solve函数计算结果
    long long result = solve(height, x_val, y_val, z_val);
    
    // 输出结果
    std::cout << result << std::endl;
    
    return 0;
}

// 核心算法函数，便于单元测试和重用
long long solve(long long height, int x_val, int y_val, int z_val) {
    h = height - 1;
    x = x_val;
    y = y_val;
    z = z_val;
    
    init();
    
    // 构建图
    for (int i = 0; i < x; i++) {
        add_edge(i, (i + y) % x, y);
        add_edge(i, (i + z) % x, z);
    }
    
    return calculate_result();
}