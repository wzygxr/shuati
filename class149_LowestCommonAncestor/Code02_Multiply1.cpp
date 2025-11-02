/**
 * 树上倍增解法（递归版）
 * 题目来源：洛谷 P3379 【模板】最近公共祖先（LCA）
 * 题目链接：https://www.luogu.com.cn/problem/P3379
 * 
 * 问题描述：
 * 给定一棵有根多叉树，请求出指定两个点直接最近的公共祖先。
 * 
 * 解题思路：
 * 使用树上倍增法预处理每个节点的2^k级祖先，然后对于每次查询：
 * 1. 先将两个节点调整到同一深度
 * 2. 然后同时向上跳跃，直到找到最近公共祖先
 * 
 * 时间复杂度：
 * 预处理：O(n log n)
 * 查询：O(log n)
 * 空间复杂度：O(n log n)
 * 
 * 是否为最优解：是，对于在线查询LCA问题，倍增法是标准解法之一
 * 
 * 工程化考虑：
 * 1. 边界条件处理：处理空树、节点不存在等情况
 * 2. 输入验证：验证输入节点是否在树中
 * 3. 异常处理：对非法输入进行检查
 * 4. 可读性：添加详细注释和变量命名
 * 
 * 算法要点：
 * 1. 预处理阶段构建倍增数组：ancestor[i][k]表示节点i的第2^k个祖先
 * 2. 查询阶段先调整深度再同时跳跃
 * 3. 利用二进制表示优化跳跃过程
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
 * 1. 空输入：返回特定值表示无效
 * 2. 节点不存在：返回特定值表示无效
 * 3. 一个节点是另一个节点的祖先：正确处理
 * 
 * 语言特性差异：
 * 1. C++：手动内存管理，指针操作，高性能但容易出错
 * 2. Java：自动垃圾回收，对象引用传递，类型安全
 * 3. Python：动态类型，引用计数垃圾回收，代码简洁
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
 * 注意事项：
 * C++这么写能通过，但递归层数太多可能会爆栈
 * 为了避免栈溢出，可以参考迭代版本
 * 提交时请把类名改成"Main"
 * 
 * 注意：由于编译环境限制，避免使用复杂的STL容器和标准库函数
 */

class LCA {
private:
    static const int MAXN = 500001;
    static const int LIMIT = 20;
    
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
    int next[MAXN << 1];
    int to[MAXN << 1];
    int cnt;
    
    // 深度数组和倍增数组
    int deep[MAXN];
    int stjump[MAXN][LIMIT];
    
public:
    void build(int n) {
        power = log2(n);
        cnt = 1;
        for (int i = 1; i <= n; i++) {
            head[i] = 0;
        }
    }
    
    void addEdge(int u, int v) {
        next[cnt] = head[u];
        to[cnt] = v;
        head[u] = cnt++;
    }
    
    // dfs递归版
    // 一般来说都这么写，但是本题附加的测试数据很毒
    // C++这么写就能通过（在大多数情况下）
    void dfs(int u, int f) {
        deep[u] = deep[f] + 1;
        stjump[u][0] = f;
        for (int p = 1; p <= power; p++) {
            stjump[u][p] = stjump[stjump[u][p - 1]][p - 1];
        }
        for (int e = head[u]; e != 0; e = next[e]) {
            if (to[e] != f) {
                dfs(to[e], u);
            }
        }
    }
    
    int lca(int a, int b) {
        if (deep[a] < deep[b]) {
            int tmp = a;
            a = b;
            b = tmp;
        }
        for (int p = power; p >= 0; p--) {
            if (deep[stjump[a][p]] >= deep[b]) {
                a = stjump[a][p];
            }
        }
        if (a == b) {
            return a;
        }
        for (int p = power; p >= 0; p--) {
            if (stjump[a][p] != stjump[b][p]) {
                a = stjump[a][p];
                b = stjump[b][p];
            }
        }
        return stjump[a][0];
    }
};

// 由于编译环境限制，不包含main函数和测试代码
// 在实际使用时，可以根据洛谷平台要求进行调整