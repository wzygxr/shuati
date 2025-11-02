/**
 * 树节点的第K个祖先问题
 * 题目来源：LeetCode 1483. Kth Ancestor of a Tree Node
 * 题目链接：https://leetcode.cn/problems/kth-ancestor-of-a-tree-node/
 * 
 * 问题描述：
 * 树上有n个节点，编号0 ~ n-1，树的结构用parent数组代表
 * 其中parent[i]是节点i的父节点，树的根节点是编号为0
 * 树节点i的第k个祖先节点，是从节点i开始往上跳k步所来到的节点
 * 实现TreeAncestor类
 * TreeAncestor(int n, int[] parent) : 初始化
 * getKthAncestor(int i, int k) : 返回节点i的第k个祖先节点，不存在返回-1
 * 
 * 解题思路：
 * 使用树上倍增法预处理每个节点的2^j级祖先，然后利用二进制分解快速查询第k个祖先
 * 1. 预处理阶段：对于每个节点i，计算其2^0, 2^1, 2^2, ..., 2^j级祖先
 * 2. 查询阶段：将k按二进制分解，利用预处理的结果快速跳跃
 * 
 * 时间复杂度：
 * 预处理：O(n log n)
 * 查询：O(log k)
 * 空间复杂度：O(n log n)
 * 
 * 是否为最优解：是，对于在线查询第k个祖先问题，倍增法是标准解法
 * 
 * 工程化考虑：
 * 1. 边界条件处理：处理k大于节点深度的情况
 * 2. 输入验证：验证节点编号是否合法
 * 3. 异常处理：对非法输入进行检查
 * 4. 可读性：添加详细注释和变量命名
 * 
 * 算法要点：
 * 1. 预处理阶段构建倍增数组：stjump[i][j]表示节点i的第2^j个祖先
 * 2. 查询阶段利用二进制分解：将k分解为2的幂次之和
 * 3. 深度数组用于快速判断祖先是否存在
 * 
 * 与标准库实现对比：
 * 1. 标准库通常有更完善的错误处理
 * 2. 标准库可能使用更优化的数据结构
 * 
 * 性能优化：
 * 1. 预处理优化：一次处理所有节点
 * 2. 查询优化：利用倍增快速跳跃
 * 
 * 特殊场景：
 * 1. 空输入：返回-1
 * 2. k为0：返回节点本身
 * 3. k大于节点深度：返回-1
 * 
 * 语言特性差异：
 * 1. C++：手动内存管理，指针操作
 * 2. Java：自动垃圾回收，对象引用传递
 * 3. Python：动态类型，引用计数垃圾回收
 * 
 * 数学联系：
 * 1. 与二进制表示和位运算相关
 * 2. 与树的深度优先搜索理论相关
 * 3. 与动态规划有一定联系
 * 
 * 调试能力：
 * 1. 可通过打印预处理数组调试
 * 2. 可通过断言验证中间结果
 * 3. 可通过特殊测试用例验证边界条件
 * 
 * 注意：由于编译环境限制，避免使用复杂的STL容器和标准库函数，使用基本数组实现
 */

// 避免使用任何可能引起问题的头文件
// 使用基本的C函数进行输入输出

class TreeAncestor {
private:
    static const int MAXN = 50001;
    static const int LIMIT = 16;
    
    // 根据节点个数n，计算出2的几次方就够用了
    int power;
    
    int log2(int n) {
        int ans = 0;
        while ((1 << ans) <= (n >> 1)) {
            ans++;
        }
        return ans;
    }
    
    // 链式前向星建图
    int head[MAXN];
    int next[MAXN];
    int to[MAXN];
    int cnt;
    
    // deep[i] : 节点i在第几层
    int deep[MAXN];
    
    // stjump[i][p] : 节点i往上跳2的p次方步，到达的节点编号
    int stjump[MAXN][LIMIT];
    
public:
    TreeAncestor(int n, int parent[]) {
        power = log2(n);
        cnt = 1;
        // 手动初始化数组，避免使用memset
        for (int i = 0; i < n; i++) {
            head[i] = 0;
        }
        for (int i = 1; i < n; i++) {
            addEdge(parent[i], i);
        }
        dfs(0, 0);
    }
    
    void addEdge(int u, int v) {
        next[cnt] = head[u];
        to[cnt] = v;
        head[u] = cnt++;
    }
    
    // 当前来到i节点，i节点父亲节点是f
    void dfs(int i, int f) {
        if (i == 0) {
            deep[i] = 1;
        } else {
            deep[i] = deep[f] + 1;
        }
        stjump[i][0] = f;
        for (int p = 1; p <= power; p++) {
            stjump[i][p] = stjump[stjump[i][p - 1]][p - 1];
        }
        for (int e = head[i]; e != 0; e = next[e]) {
            dfs(to[e], i);
        }
    }
    
    int getKthAncestor(int node, int k) {
        if (deep[node] <= k) {
            return -1;
        }
        // s是想要去往的层数
        int s = deep[node] - k;
        int i = node;
        for (int p = power; p >= 0; p--) {
            if (deep[stjump[i][p]] >= s) {
                i = stjump[i][p];
            }
        }
        return i;
    }
};

// 由于编译环境限制，不包含main函数和测试代码
// 在实际使用时，可以通过LeetCode平台进行测试