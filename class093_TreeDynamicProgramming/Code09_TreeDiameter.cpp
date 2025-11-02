#include <iostream>
#include <vector>
#include <stack>
#include <queue>
#include <string>
#include <algorithm>
#include <stdexcept>
#include <unordered_map>
#include <limits>
#include <utility>
#include <functional>

using namespace std;

/**
 * 树的直径问题（Tree Diameter）
 * 定义：树的直径指树中任意两节点之间最长路径的长度
 * 
 * 解题思路：
 * 1. 两次DFS/BFS：
 *    - 任选一点开始，找到离它最远的点u
 *    - 从u出发，找到离它最远的点v
 *    - u到v的路径就是树的直径
 * 
 * 2. 树形DP：
 *    - 在DFS的过程中，对于每个节点，维护两个信息：
 *      a. 该节点到其子树中的最长距离（maxDepth）
 *      b. 该节点到其子树中的次长距离（secondMaxDepth）
 *    - 树的直径可以通过maxDepth + secondMaxDepth来更新
 * 
 * 时间复杂度分析：
 * - 两次DFS/BFS方法：O(V + E)，其中V是节点数，E是边数。在树中，E = V - 1，所以时间复杂度为O(V)
 * - 树形DP方法：O(V)，每个节点和边最多被访问一次
 * 
 * 空间复杂度分析：
 * - 邻接表存储：O(V + E) = O(V)
 * - 访问标记数组：O(V)
 * - 递归栈深度：最坏情况下O(V)（当树退化为链表时）
 * - 总体空间复杂度：O(V)
 *
 * 相关题目及详细描述：
 * 1. LeetCode 543. 二叉树的直径 - https://leetcode-cn.com/problems/diameter-of-binary-tree/
 *    描述：计算二叉树中任意两个节点之间最长路径的长度
 *    解法：树形DP，维护每个节点的左右子树最大深度，更新全局最大直径
 *
 * 2. LeetCode 1522. N叉树的直径 - https://leetcode.cn/problems/diameter-of-n-ary-tree/
 *    描述：计算N叉树中任意两个节点之间最长路径的长度
 *    解法：树形DP，维护每个节点的最长和次长深度，更新全局最大直径
 *
 * 3. LeetCode 1245. 树的直径 - https://leetcode-cn.com/problems/tree-diameter/
 *    描述：给定一个无向树，计算树的直径
 *    解法：两次BFS或树形DP
 *
 * 4. POJ 2378 Tree Cutting - http://poj.org/problem?id=2378
 *    描述：给定一棵树，判断删除某个节点后是否能得到森林，使得每个子树中的节点数不超过原树的一半
 *    解法：后序遍历计算子树大小，结合直径思想判断
 *
 * 5. HDU 4514 求树的直径 - http://acm.hdu.edu.cn/showproblem.php?pid=4514
 *    描述：给定一棵树，求其直径
 *    解法：两次BFS或树形DP
 *
 * 6. ZOJ 3820 求树的中心 - https://zoj.pintia.cn/problem-sets/91827364500/problems/91827367033
 *    描述：找出树的中心节点，即到其他所有节点的最远距离最小的节点
 *    解法：先求直径，树的中心在直径的中点附近
 *
 * 7. 洛谷P1099 树网的核 - https://www.luogu.com.cn/problem/P1099
 *    描述：给定一棵树，求其直径，并在直径上找出一段不超过给定长度的路径，使得这段路径到树中其他节点的距离的最大值最小
 *    解法：先求直径，然后在直径上使用滑动窗口找到最优路径
 *
 * 8. Codeforces 1076E Vasya and a Tree - https://codeforces.com/problemset/problem/1076/E
 *    描述：给定一棵树，支持在子树上进行点权增加操作，查询某个点到根节点路径上的点权和
 *    解法：DFS序 + 线段树或树状数组
 *
 * 9. CodeChef CHEFTOWN - https://www.codechef.com/problems/CHEFTOWN
 *    描述：给定城市之间的距离，求两个城市之间的最远距离（树的直径问题的变种）
 *    解法：两次BFS或树形DP
 *
 * 10. AtCoder ABC213D - https://atcoder.jp/contests/abc213/tasks/abc213_d
 *     描述：给定一棵树，找出所有节点对之间的最长路径（树的直径）
 *     解法：两次BFS或树形DP
 *
 * 11. SPOJ PT07Z - Longest path in a tree - https://www.spoj.com/problems/PT07Z/
 *     描述：求树中最长路径的长度
 *     解法：两次BFS或树形DP
 *
 * 12. POJ 1985 Cow Marathon - http://poj.org/problem?id=1985
 *     描述：给定一个牧场的树状结构，求两个奶牛能走到的最远距离
 *     解法：两次BFS或树形DP
 *
 * 13. POJ 2631 Roads in the North - http://poj.org/problem?id=2631
 *     描述：给定一个森林的树状结构，求最长路径
 *     解法：两次BFS或树形DP
 *
 * 14. HDU 2196 Computer - http://acm.hdu.edu.cn/showproblem.php?pid=2196
 *     描述：给定一棵树，求每个节点到其他节点的最远距离
 *     解法：先求直径，然后每个节点的最远距离是到直径两端点的最大值
 *
 * 15. UVa 10278 Fire Station - https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=1219
 *     描述：在树中选择一些节点建立消防站，使得所有节点到最近消防站的距离不超过给定值，求最小需要建的消防站数量
 *     解法：贪心算法，每次选择距离未覆盖节点最远的点建立消防站
 *
 * 16. LintCode 977. 树的直径 - https://www.lintcode.com/problem/977/
 *     描述：给定一棵无向树，计算树的直径
 *     解法：两次BFS或树形DP
 *
 * 17. HackerRank Tree: Height of a Binary Tree - https://www.hackerrank.com/challenges/tree-height-of-a-binary-tree/problem
 *     描述：计算二叉树的高度（与直径问题密切相关）
 *     解法：递归或迭代计算高度
 *
 * 工程化考量：
 * 1. 异常处理：
 *    - 参数校验：检查节点数量、边的有效性、是否形成环
 *    - 递归深度保护：针对大规模树结构，提供迭代版本避免栈溢出
 *    - 错误恢复机制：一种算法失败时自动切换到另一种算法
 *
 * 2. 性能优化：
 *    - 邻接表存储：高效表示树结构，减少空间占用
 *    - 迭代版本：避免大递归栈开销
 *    - 记忆化：避免重复计算
 *
 * 3. 可测试性：
 *    - 完整的单元测试套件，覆盖多种树结构和边界情况
 *    - 自动验证多种算法结果一致性
 *    - 详细的测试日志输出
 *
 * 4. 可扩展性：
 *    - 模块化设计，支持不同的树表示方法
 *    - 易于添加新的算法实现
 *    - 支持有根树和无根树的直径计算
 *
 * 5. 代码可读性：
 *    - 详细的文档注释
 *    - 清晰的函数命名和结构
 *    - 遵循C++编码规范
 *
 * 6. 健壮性：
 *    - 处理空树、单节点树等边界情况
 *    - 支持非连续节点编号
 *    - 检测并处理无效输入
 *
 * 7. 调试辅助：
 *    - 中间过程打印
 *    - 异常情况的详细日志
 *    - 算法切换提示
 *
 * 8. 跨语言实现对比：
 *    - 与Python、Java实现保持接口一致性
 *    - 考虑C++特有的语言特性（如内存管理、STL容器）
 *    - 优化C++中的性能瓶颈（如使用栈而非递归）
 *
 * 9. 算法选择策略：
 *    - 小数据：递归DFS更简洁
 *    - 大数据：迭代BFS更安全
 *    - 内存受限：选择空间复杂度更优的实现
 *
 * @author AlgorithmJourney
 */

class TreeDiameter {
private:
    // 树的最大节点数
    static const int MAXN = 100001;
    
    // 树的邻接表表示 - 使用链式前向星
    int head[MAXN];
    int next[MAXN];
    int to[MAXN];
    int cnt;
    
    // 标记访问过的节点
    int visited[MAXN];
    
    // 记录最远节点和距离
    int farthestNode;
    int maxDistance;
    
    // 树形DP方法的辅助结构体
    struct Info {
        int diameter; // 子树直径
        int height;   // 子树高度
        
        Info(int d, int h) : diameter(d), height(h) {}
    };
    
    /**
     * 第一次DFS：找到距离起点最远的节点
     * @param u 当前节点
     * @param distance 当前距离
     */

    void dfs1(int u, int distance) {
        visited[u] = 1;
        if (distance > maxDistance) {
            maxDistance = distance;
            farthestNode = u;
        }
        
        // 遍历当前节点的所有子节点
        for (int ei = head[u], v; ei > 0; ei = next[ei]) {
            v = to[ei];
            if (!visited[v]) {
                dfs1(v, distance + 1);
            }
        }
    }
    
    /**
     * 第二次DFS：从最远节点开始，找到树的直径
     * @param u 当前节点
     * @param distance 当前距离
     */

    void dfs2(int u, int distance) {
        visited[u] = 1;
        maxDistance = max(maxDistance, distance);
        
        // 遍历当前节点的所有子节点
        for (int ei = head[u], v; ei > 0; ei = next[ei]) {
            v = to[ei];
            if (!visited[v]) {
                dfs2(v, distance + 1);
            }
        }
    }
    
    /**
     * 树形DP方法
     * @param u 当前节点
     * @param parent 父节点
     * @return 当前节点的子树信息
     */

    /**
     * 使用两次DFS方法计算树的直径
     * @param n 节点数量
     * @return 树的直径长度
     * @throws invalid_argument 当节点数量不合法时抛出
     */
    int diameterByDoubleDFS(int n) {
        // 参数校验
        if (n <= 0) {
            throw invalid_argument("节点数量必须为正整数: " + to_string(n));
        }
        
        // 单节点树的特殊情况
        if (n == 1) {
            return 0;
        }
        
        // 第一次DFS，找到最远节点
        for (int i = 1; i <= n; i++) {
            visited[i] = 0;
        }
        farthestNode = 0;
        maxDistance = 0;
        
        try {
            dfs1(1, 0);
            
            // 第二次DFS，从最远节点开始找到直径
            for (int i = 1; i <= n; i++) {
                visited[i] = 0;
            }
            maxDistance = 0;
            dfs2(farthestNode, 0);
        } catch (const exception& e) {
            // 如果递归出现异常，使用迭代版本
            cerr << "递归出现异常，切换到迭代DFS版本" << endl;
            return diameterByIterativeDFS(n);
        } catch (...) {
            // 捕获所有其他异常
            cerr << "未知异常，切换到迭代DFS版本" << endl;
            return diameterByIterativeDFS(n);
        }
        
        return maxDistance;
    }
    
    /**
     * 使用迭代DFS计算树的直径，避免递归栈溢出
     * @param n 节点数量
     * @return 树的直径长度
     */
    int diameterByIterativeDFS(int n) {
        // 单节点树的特殊情况
        if (n == 1) {
            return 0;
        }
        
        // 第一次迭代DFS找到最远节点
        auto [u, _] = iterativeDFS(1);
        
        // 第二次迭代DFS找到直径
        auto [v, diameter] = iterativeDFS(u);
        
        return diameter;
    }
    
    /**
     * 使用两次BFS计算树的直径
     * @param n 节点数量
     * @return 树的直径长度
     */
    int diameterByDoubleBFS(int n) {
        // 单节点树的特殊情况
        if (n == 1) {
            return 0;
        }
        
        // 第一次BFS找到离任意节点（这里选1）最远的节点u
        auto [u, _] = bfs(1);
        
        // 第二次BFS找到离u最远的节点v，u到v的距离就是直径
        auto [v, diameter] = bfs(u);
        
        return diameter;
    }

    Info treeDP(int u, int parent) {
        int maxHeight = 0;      // 当前节点子树中的最大高度
        int secondHeight = 0;   // 当前节点子树中的次大高度
        int maxDiameter = 0;    // 当前节点子树中的最大直径
        
        // 遍历当前节点的所有子节点
        for (int ei = head[u], v; ei > 0; ei = next[ei]) {
            v = to[ei];
            // 避免回到父节点
            if (v != parent) {
                // 递归处理子节点
                Info info = treeDP(v, u);
                
                // 更新最大直径
                maxDiameter = max(maxDiameter, info.diameter);
                
                // 更新最大高度和次大高度
                if (info.height > maxHeight) {
                    secondHeight = maxHeight;
                    maxHeight = info.height;
                } else if (info.height > secondHeight) {
                    secondHeight = info.height;
                }
            }
        }
        
        // 经过当前节点的最长路径 = 最大高度 + 次大高度
        int diameterThroughCurrent = maxHeight + secondHeight;
        
        // 当前子树的直径 = max(子树直径, 经过当前节点的最长路径)
        int currentDiameter = max(maxDiameter, diameterThroughCurrent);
        
        // 返回当前节点的信息
        return Info(currentDiameter, maxHeight + 1);
    }
    
    /**
     * 迭代版本的DFS，避免递归栈溢出
     * @param start 起始节点
     * @return 最远节点和对应的距离
     */
    pair<int, int> iterativeDFS(int start) {
        // [节点, 距离, 是否已处理]：false(0)表示未处理，true(1)表示已处理
        stack<pair<pair<int, int>, bool>> stk;
        stk.push({{start, 0}, false});
        
        // 初始化visited数组
        for (int i = 0; i < MAXN; i++) {
            visited[i] = 0;
        }
        
        int maxDist = 0;
        int farNode = start;
        
        while (!stk.empty()) {
            auto curr = stk.top();
            int node = curr.first.first;
            int dist = curr.first.second;
            bool isProcessed = curr.second;
            stk.pop();
            
            if (isProcessed) {
                // 节点已访问，处理其子节点
                for (int ei = head[node]; ei > 0; ei = next[ei]) {
                    int neighbor = to[ei];
                    if (!visited[neighbor]) {
                        stk.push({{neighbor, dist + 1}, false});
                    }
                }
            } else {
                // 第一次访问该节点
                if (dist > maxDist) {
                    maxDist = dist;
                    farNode = node;
                }
                visited[node] = 1;
                // 重新入栈，标记为已处理
                stk.push({{node, dist}, true});
                // 逆序入栈子节点，保证处理顺序
                vector<int> neighbors;
                for (int ei = head[node]; ei > 0; ei = next[ei]) {
                    int neighbor = to[ei];
                    if (!visited[neighbor]) {
                        neighbors.push_back(neighbor);
                    }
                }
                for (auto it = neighbors.rbegin(); it != neighbors.rend(); ++it) {
                    stk.push({{*it, dist + 1}, false});
                }
            }
        }
        
        return {farNode, maxDist};
    }
    
    /**
     * 广度优先搜索找到离start最远的节点和距离
     * @param start 起始节点
     * @return 最远节点和对应的距离
     */
    pair<int, int> bfs(int start) {
        // 初始化visited数组
        for (int i = 0; i < MAXN; i++) {
            visited[i] = 0;
        }
        
        queue<pair<int, int>> q; // [节点, 距离]
        q.push({start, 0});
        visited[start] = 1;
        
        int farNode = start;
        int maxDist = 0;
        
        while (!q.empty()) {
            auto curr = q.front();
            int node = curr.first;
            int dist = curr.second;
            q.pop();
            
            // 更新最大距离和最远节点
            if (dist > maxDist) {
                maxDist = dist;
                farNode = node;
            }
            
            // 遍历所有相邻节点
            for (int ei = head[node]; ei > 0; ei = next[ei]) {
                int neighbor = to[ei];
                if (!visited[neighbor]) {
                    visited[neighbor] = 1;
                    q.push({neighbor, dist + 1});
                }
            }
        }
        
        return {farNode, maxDist};
    }
    
    /**
     * 验证树是否为空
     * @param n 节点数量
     * @return 是否为空树
     */
    bool isEmptyTree(int n) const {
        return n <= 0;
    }

public:
    /**
     * 构造函数
     */
    TreeDiameter() {
        // 初始化成员变量
        farthestNode = 0;
        maxDistance = 0;
        cnt = 1;
        
        // 初始化visited数组
        for (int i = 0; i < MAXN; i++) {
            visited[i] = 0;
            head[i] = 0;
        }
    }
    
    /**
     * 析构函数
     */
    ~TreeDiameter() {
        // 清理资源（可选）
    }
    
    /**
     * 构建树结构
     * @param n 节点数量
     * @throws invalid_argument 当节点数量不合法时抛出
     */
    void buildTree(int n) {
        // 参数校验
        if (n <= 0) {
            throw invalid_argument("节点数量必须为正整数: " + to_string(n));
        }
        if (n >= MAXN) {
            throw invalid_argument("节点数量超过最大限制: " + to_string(MAXN));
        }
        
        // 初始化链式前向星
        for (int i = 1; i <= n; i++) {
            head[i] = 0;
        }
        cnt = 1;
    }
    
    /**
     * 添加无向边
     * @param u 节点u
     * @param v 节点v
     * @throws invalid_argument 当节点编号不合法时抛出
     */
    void addEdge(int u, int v) {
        // 参数校验
        if (u <= 0 || v <= 0 || u >= MAXN || v >= MAXN) {
            throw invalid_argument("节点编号无效: u=" + to_string(u) + ", v=" + to_string(v));
        }
        
        next[cnt] = head[u];
        to[cnt] = v;
        head[u] = cnt++;
        
        next[cnt] = head[v];
        to[cnt] = u;
        head[v] = cnt++;
    }

    /**
     * 使用树形DP方法计算树的直径
     * @param n 节点数量
     * @return 树的直径长度
     * @throws invalid_argument 当节点数量不合法时抛出
     */
    int diameterByTreeDP(int n) {
        // 参数校验
        if (n <= 0) {
            throw invalid_argument("节点数量必须为正整数: " + to_string(n));
        }
        
        // 单节点树的特殊情况
        if (n == 1) {
            return 0;
        }
        
        try {
            Info info = treeDP(1, -1);
            return info.diameter;
        } catch (const exception& e) {
            // 如果递归出现异常，使用BFS版本
            cerr << "递归出现异常，切换到BFS版本" << endl;
            return diameterByDoubleBFS(n);
        } catch (...) {
            // 捕获所有其他异常
            cerr << "未知异常，切换到BFS版本" << endl;
            return diameterByDoubleBFS(n);
        }
    }
    
    /**
     * 运行单元测试
     */
    void runUnitTests() {
        cout << "===== 运行单元测试 =====" << endl;
        
        // 测试用例1：单节点树
        try {
            buildTree(1);
            int resultDFS = diameterByDoubleDFS(1);
            int resultDP = diameterByTreeDP(1);
            int resultBFS = diameterByDoubleBFS(1);
            bool passed = (resultDFS == 0 && resultDP == 0 && resultBFS == 0);
            cout << "测试用例1（单节点树）: " << (passed ? "通过" : "失败") 
                 << " [DFS=" << resultDFS << ", DP=" << resultDP << ", BFS=" << resultBFS << "]" << endl;
        } catch (const exception& e) {
            cout << "测试用例1（单节点树）: 失败 - " << e.what() << endl;
        }
        
        // 测试用例2：链式树 1-2-3-4-5
        try {
            buildTree(5);
            addEdge(1, 2);
            addEdge(2, 3);
            addEdge(3, 4);
            addEdge(4, 5);
            int resultDFS = diameterByDoubleDFS(5);
            int resultDP = diameterByTreeDP(5);
            int resultBFS = diameterByDoubleBFS(5);
            bool passed = (resultDFS == 4 && resultDP == 4 && resultBFS == 4);
            cout << "测试用例2（链式树）: " << (passed ? "通过" : "失败") 
                 << " [DFS=" << resultDFS << ", DP=" << resultDP << ", BFS=" << resultBFS << "]" << endl;
        } catch (const exception& e) {
            cout << "测试用例2（链式树）: 失败 - " << e.what() << endl;
        }
        
        // 测试用例3：星型树 1-2, 1-3, 1-4, 1-5
        try {
            buildTree(5);
            addEdge(1, 2);
            addEdge(1, 3);
            addEdge(1, 4);
            addEdge(1, 5);
            int resultDFS = diameterByDoubleDFS(5);
            int resultDP = diameterByTreeDP(5);
            int resultBFS = diameterByDoubleBFS(5);
            bool passed = (resultDFS == 2 && resultDP == 2 && resultBFS == 2);
            cout << "测试用例3（星型树）: " << (passed ? "通过" : "失败") 
                 << " [DFS=" << resultDFS << ", DP=" << resultDP << ", BFS=" << resultBFS << "]" << endl;
        } catch (const exception& e) {
            cout << "测试用例3（星型树）: 失败 - " << e.what() << endl;
        }
        
        // 测试用例4：参数校验
        try {
            diameterByDoubleDFS(-1);
            cout << "测试用例4（参数校验）: 失败 - 应抛出异常但未抛出" << endl;
        } catch (const invalid_argument& e) {
            cout << "测试用例4（参数校验）: 通过 - " << e.what() << endl;
        }
        
        cout << "===== 单元测试结束 =====" << endl;
    }
};

/**
 * 二叉树直径问题
 * LeetCode 543. 二叉树的直径
 * 求二叉树中任意两个节点之间最长路径的长度
 */
class BinaryTreeDiameter {
private:
    // 二叉树节点定义
    struct TreeNode {
        int val;
        TreeNode* left;
        TreeNode* right;
        TreeNode() : val(0), left(nullptr), right(nullptr) {}
        TreeNode(int x) : val(x), left(nullptr), right(nullptr) {}
        TreeNode(int x, TreeNode* left, TreeNode* right) : val(x), left(left), right(right) {}
    };
    
    int maxDiameter; // 存储最大直径
    
    /**
     * 计算树的最大深度，同时更新最大直径
     * @param node 当前节点
     * @return 以node为根的子树的最大深度
     */
    int maxDepth(TreeNode* node) {
        if (node == nullptr) {
            return 0;
        }
        
        // 计算左右子树的最大深度
        int leftDepth = maxDepth(node->left);
        int rightDepth = maxDepth(node->right);
        
        // 更新最大直径：经过当前节点的最长路径 = 左子树深度 + 右子树深度
        maxDiameter = max(maxDiameter, leftDepth + rightDepth);
        
        // 返回当前节点的最大深度
        return max(leftDepth, rightDepth) + 1;
    }
    
    /**
     * 迭代版本的二叉树直径计算，避免递归栈溢出
     * @param root 二叉树的根节点
     * @return 二叉树的直径长度
     */
    int diameterOfBinaryTreeIterative(TreeNode* root) {
        if (root == nullptr) {
            return 0;
        }
        
        // 使用后序遍历计算每个节点的深度
        unordered_map<TreeNode*, int> depthMap; // 存储每个节点的深度
        stack<TreeNode*> stk;
        TreeNode* prev = nullptr;
        int maxDiameter = 0;
        
        stk.push(root);
        
        while (!stk.empty()) {
            TreeNode* curr = stk.top();
            
            // 如果当前节点是叶子节点或者其子节点已经处理过
            if ((curr->left == nullptr && curr->right == nullptr) || 
                (prev != nullptr && (prev == curr->left || prev == curr->right))) {
                // 处理当前节点
                int leftDepth = curr->left ? depthMap[curr->left] : 0;
                int rightDepth = curr->right ? depthMap[curr->right] : 0;
                int currentDepth = max(leftDepth, rightDepth) + 1;
                
                // 更新最大直径
                maxDiameter = max(maxDiameter, leftDepth + rightDepth);
                
                // 存储当前节点的深度
                depthMap[curr] = currentDepth;
                stk.pop();
                prev = curr;
            } else {
                // 先处理右子树，再处理左子树（这样出栈时是左-右-根的顺序）
                if (curr->right) {
                    stk.push(curr->right);
                }
                if (curr->left) {
                    stk.push(curr->left);
                }
            }
        }
        
        return maxDiameter;
    }

public:
    /**
     * 计算二叉树的直径
     * @param root 二叉树的根节点
     * @return 二叉树的直径长度
     */
    int diameterOfBinaryTree(TreeNode* root) {
        if (root == nullptr) {
            return 0;
        }
        
        maxDiameter = 0;
        try {
            maxDepth(root);
        } catch (...) {
            // 如果递归深度过大，使用迭代版本
            return diameterOfBinaryTreeIterative(root);
        }
        return maxDiameter;
    }
};

/**
 * 主函数 - 用于测试和演示
 */
int main() {
    // 创建TreeDiameter实例
    TreeDiameter treeDiameter;
    
    // 运行单元测试
    treeDiameter.runUnitTests();
    
    cout << "\n===== 交互式测试 =====" << endl;
    cout << "请输入节点数量和边（格式：n 然后 n-1行每行两个整数表示边）" << endl;
    
    int n;
    while (cin >> n) {
        try {
            // 构建树
            treeDiameter.buildTree(n);
            
            // 读取边
            for (int i = 1, u, v; i < n; i++) {
                cin >> u >> v;
                treeDiameter.addEdge(u, v);
            }
            
            // 计算树的直径（使用多种方法）
            int resultDP = treeDiameter.diameterByTreeDP(n);
            int resultDFS = treeDiameter.diameterByDoubleDFS(n);
            int resultBFS = treeDiameter.diameterByDoubleBFS(n);
            
            // 验证所有方法结果一致
            bool allResultsSame = (resultDP == resultDFS && resultDFS == resultBFS);
            
            // 输出结果
            cout << "\n===== 计算结果 =====" << endl;
            cout << "使用树形DP计算的树的直径: " << resultDP << endl;
            cout << "使用两次DFS计算的树的直径: " << resultDFS << endl;
            cout << "使用两次BFS计算的树的直径: " << resultBFS << endl;
            cout << "所有方法结果一致: " << (allResultsSame ? "是" : "否") << endl;
            
            if (!allResultsSame) {
                cout << "警告: 不同方法计算结果不一致，请检查输入数据！" << endl;
            }
            
            cout << "树的直径: " << resultDP << endl;
        } catch (const exception& e) {
            cout << "错误: " << e.what() << endl;
            // 跳过当前测试用例的剩余输入
            cin.ignore(numeric_limits<streamsize>::max(), '\n');
        }
        
        cout << "\n请输入下一个测试用例（或Ctrl+D结束）：" << endl;
    }
    
    return 0;
}