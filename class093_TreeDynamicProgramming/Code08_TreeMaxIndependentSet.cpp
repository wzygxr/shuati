// 树的最大独立集 (Tree Maximum Independent Set)
// 题目描述:
// 对于一棵有N个结点的无根树，选出尽量多的结点，使得任何两个结点均不相邻
// 这是一个经典的树形动态规划问题
//
// 解题思路:
// 1. 使用树形动态规划（Tree DP）的方法
// 2. 对于每个节点，我们需要知道以下信息：
//    - 当前节点被选中时，以该节点为根的子树能选出的最大独立集大小
//    - 当前节点不被选中时，以该节点为根的子树能选出的最大独立集大小
// 3. 递归处理子树，综合计算当前节点的信息
// 4. 状态转移方程：
//    - 当前节点被选中：dp[u][1] = weight[u] + sum(dp[v][0]) for each child v
//    - 当前节点不被选中：dp[u][0] = sum(max(dp[v][0], dp[v][1])) for each child v
//
// 时间复杂度: O(n) - n为树中节点的数量，需要遍历所有节点一次
// 空间复杂度: O(n) - 存储树结构和DP数组，递归调用栈深度为O(h)，h为树高
// 是否为最优解: 是，这是解决树的最大独立集问题的标准方法，无法进一步降低时间复杂度
//
// 【补充题目】
// 1. 洛谷 P1352 没有上司的舞会 - https://www.luogu.com.cn/problem/P1352
// 2. HDU 1520 Anniversary party - http://acm.hdu.edu.cn/showproblem.php?pid=1520
// 3. LeetCode 337. 打家劫舍III - https://leetcode-cn.com/problems/house-robber-iii/
// 4. LeetCode 2646. 最小化旅行的价格总和 - https://leetcode-cn.com/problems/minimize-the-total-price-of-the-trips/
// 5. Codeforces 1083C Max Mex - https://codeforces.com/problemset/problem/1083/C
// 6. AtCoder ABC163F path pass i - https://atcoder.jp/contests/abc163/tasks/abc163_f
// 7. POJ 3342 Party at Hali-Bula - http://poj.org/problem?id=3342
// 8. SPOJ PT07Z - Longest path in a tree - https://www.spoj.com/problems/PT07Z/
//
// 【工程化考量】
// 1. 使用链式前向星存储树结构，效率高于邻接表
// 2. 手动内存管理，避免STL容器的额外开销
// 3. 添加参数校验和异常处理
// 4. 提供多版本接口，适应不同的树结构表示
// 5. 支持无向树和有根树两种场景

#include <iostream>
#include <cstring>
#include <cassert>

// 为避免编译问题，使用基础C++实现方式，不使用STL容器

const int MAXN = 6001;
const int INF = 0x3f3f3f3f;

// 树的邻接表表示 - 使用链式前向星（高效的图存储结构）
int head[MAXN];  // 每个节点的第一个边的索引
int next[MAXN];  // 指向下一条边的索引
int to[MAXN];    // 边指向的节点
int cnt;         // 当前边的数量计数器

// dp[i][0] 表示节点i不被选中时，以i为根的子树的最大独立集大小
// dp[i][1] 表示节点i被选中时，以i为根的子树的最大独立集大小
int dp[MAXN][2];

// 节点权重（对于没有权重的版本，可以都设为1）
int weight[MAXN];

// 标记是否有父节点（用于找根节点）
int hasParent[MAXN];

// 构建树结构
void buildTree(int n) {
    // 参数校验
    if (n <= 0) {
        std::cerr << "错误：节点数量必须为正整数" << std::endl;
        return;
    }
    if (n >= MAXN) {
        std::cerr << "错误：节点数量超过最大限制：" << MAXN << std::endl;
        return;
    }
    
    // 初始化链式前向星
    memset(head, 0, sizeof(head));
    cnt = 1;  // 边从索引1开始，0表示空
    
    // 初始化数组
    memset(hasParent, 0, sizeof(hasParent));
    memset(weight, 0, sizeof(weight));  // 权重初始化为0
    memset(dp, 0, sizeof(dp));  // DP数组初始化为0
}

// 添加无向边（适用于一般树结构）
void addEdge(int u, int v) {
    // 参数校验
    if (u <= 0 || v <= 0 || u >= MAXN || v >= MAXN) {
        std::cerr << "错误：节点编号无效：" << u << ", " << v << std::endl;
        return;
    }
    
    // 添加u->v的边
    next[cnt] = head[u];
    to[cnt] = v;
    head[u] = cnt++;
    
    // 添加v->u的边（因为是无向图）
    next[cnt] = head[v];
    to[cnt] = u;
    head[v] = cnt++;
}

// 添加有向边（适用于有根树）
void addDirectedEdge(int u, int v) {
    // 参数校验
    if (u <= 0 || v <= 0 || u >= MAXN || v >= MAXN) {
        std::cerr << "错误：节点编号无效：" << u << ", " << v << std::endl;
        return;
    }
    
    // 只添加u->v的边
    next[cnt] = head[u];
    to[cnt] = v;
    head[u] = cnt++;
}

// 设置父子关系（适用于有根树，如公司组织结构）
void setParent(int parent, int child) {
    // 参数校验
    if (parent <= 0 || child <= 0 || parent >= MAXN || child >= MAXN) {
        std::cerr << "错误：节点编号无效：" << parent << ", " << child << std::endl;
        return;
    }
    
    addDirectedEdge(parent, child);
    hasParent[child] = 1;
}

// 设置节点权重
void setWeight(int node, int w) {
    if (node <= 0 || node >= MAXN) {
        std::cerr << "错误：节点编号无效：" << node << std::endl;
        return;
    }
    weight[node] = w;
}

// 求两个数的最大值
int max(int a, int b) {
    return a > b ? a : b;
}

// 深度优先搜索 + 动态规划
void dfs(int u, int parent) {
    // 初始化当前节点的DP值
    // 当前节点不被选中时，初始值为0
    dp[u][0] = 0;
    // 当前节点被选中时，初始值为其权重
    dp[u][1] = weight[u];
    
    // 遍历当前节点的所有相邻节点
    for (int ei = head[u]; ei > 0; ei = next[ei]) {
        int v = to[ei];
        // 避免回到父节点（防止重复访问）
        if (v != parent) {
            // 递归处理子节点
            dfs(v, u);
            
            // 更新当前节点的DP值
            // 当前节点不被选中：可以选择子节点选或不选的最大值之和
            dp[u][0] += max(dp[v][0], dp[v][1]);
            // 当前节点被选中：子节点都不能选，只能取子节点不被选中的情况
            dp[u][1] += dp[v][0];
        }
    }
}

// 树形DP主函数 - 适用于有根树（如通过setParent构建的树）
int maxIndependentSet(int n) {
    // 参数校验
    if (n <= 0) {
        return 0;  // 空树的最大独立集大小为0
    }
    
    // 找到根节点（没有父节点的节点）
    int root = 1;
    bool foundRoot = false;
    for (int i = 1; i <= n; i++) {
        if (!hasParent[i]) {
            root = i;
            foundRoot = true;
            break;
        }
    }
    
    if (!foundRoot) {
        std::cerr << "错误：无法找到根节点，树结构可能存在环" << std::endl;
        return 0;
    }
    
    // 初始化DP数组
    memset(dp, 0, sizeof(dp));
    
    // 执行树形DP
    dfs(root, -1);
    
    // 返回根节点选或不选的最大值
    return max(dp[root][0], dp[root][1]);
}

// 树形DP主函数 - 适用于无根树（通过addEdge构建的树）
int maxIndependentSetUndirected(int n) {
    // 参数校验
    if (n <= 0) {
        return 0;  // 空树的最大独立集大小为0
    }
    
    // 初始化DP数组
    memset(dp, 0, sizeof(dp));
    
    // 对于无根树，任意选择一个节点作为根（这里选择节点1）
    int root = 1;
    dfs(root, -1);
    
    // 返回根节点选或不选的最大值
    return max(dp[root][0], dp[root][1]);
}

// 【二叉树结构定义】用于LeetCode 337打家劫舍III
struct TreeNode {
    int val;
    TreeNode *left;
    TreeNode *right;
    TreeNode() : val(0), left(nullptr), right(nullptr) {}
    TreeNode(int x) : val(x), left(nullptr), right(nullptr) {}
    TreeNode(int x, TreeNode *left, TreeNode *right) : val(x), left(left), right(right) {}
};

// 辅助函数：返回 [不抢劫当前节点的最大金额, 抢劫当前节点的最大金额]
void robHelper(TreeNode* node, int& not_rob, int& do_rob) {
    if (node == nullptr) {
        not_rob = 0;
        do_rob = 0;
        return;
    }
    
    int left_not_rob, left_do_rob;
    int right_not_rob, right_do_rob;
    
    // 递归处理左右子树
    robHelper(node->left, left_not_rob, left_do_rob);
    robHelper(node->right, right_not_rob, right_do_rob);
    
    // 不抢劫当前节点：左右子树可以抢也可以不抢，取最大值
    not_rob = max(left_not_rob, left_do_rob) + max(right_not_rob, right_do_rob);
    // 抢劫当前节点：左右子树都不能抢
    do_rob = node->val + left_not_rob + right_not_rob;
}

// 【打家劫舍III - LeetCode 337】二叉树版本的最大独立集
int rob(TreeNode* root) {
    int not_rob, do_rob;
    robHelper(root, not_rob, do_rob);
    return max(not_rob, do_rob);
}

// 单元测试函数
void runUnitTests() {
    std::cout << "===== 运行单元测试 =====" << std::endl;
    
    // 测试用例1：单节点树
    try {
        buildTree(1);
        setWeight(1, 100);
        int result = maxIndependentSet(1);
        std::cout << "测试用例1（单节点树）: 期望=100, 实际=" << result 
                  << " " << (result == 100 ? "通过" : "失败") << std::endl;
        assert(result == 100 && "单节点树测试失败");
    } catch (const std::exception& e) {
        std::cout << "测试用例1（单节点树）: 失败 - " << e.what() << std::endl;
    }
    
    // 测试用例2：简单的树结构
    try {
        buildTree(3);
        setWeight(1, 10);
        setWeight(2, 20);
        setWeight(3, 30);
        setParent(1, 2);
        setParent(1, 3);
        int result = maxIndependentSet(3);
        std::cout << "测试用例2（简单树）: 期望=50, 实际=" << result 
                  << " " << (result == 50 ? "通过" : "失败") << std::endl;
        assert(result == 50 && "简单树测试失败");
    } catch (const std::exception& e) {
        std::cout << "测试用例2（简单树）: 失败 - " << e.what() << std::endl;
    }
    
    std::cout << "===== 单元测试结束 =====" << std::endl;
}

// 主函数，用于演示和测试
int main() {
    // 运行单元测试
    runUnitTests();
    
    // 【没有上司的舞会 - 洛谷 P1352】示例
    std::cout << "\n===== 没有上司的舞会示例 =====" << std::endl;
    try {
        int n = 6;
        buildTree(n);
        
        // 设置节点权重（员工的快乐指数）
        setWeight(1, 1);
        setWeight(2, 2);
        setWeight(3, 3);
        setWeight(4, 4);
        setWeight(5, 5);
        setWeight(6, 6);
        
        // 设置上下级关系
        setParent(1, 2);
        setParent(1, 3);
        setParent(2, 4);
        setParent(2, 5);
        setParent(3, 6);
        
        int maxHappiness = maxIndependentSet(n);
        std::cout << "最大快乐指数: " << maxHappiness << std::endl; // 应该输出 13 (选择节点1,4,5,6)
    } catch (const std::exception& e) {
        std::cout << "错误: " << e.what() << std::endl;
    }
    
    return 0;
}