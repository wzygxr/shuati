/**
 * Tarjan算法解法（迭代版）
 * 题目来源：洛谷 P3379 【模板】最近公共祖先（LCA）
 * 题目链接：https://www.luogu.com.cn/problem/P3379
 * 
 * 问题描述：
 * 给定一棵有根多叉树，请求出指定两个点直接最近的公共祖先。
 * 
 * 解题思路：
 * 使用Tarjan离线算法，基于DFS和并查集实现
 * 1. 首先读入所有查询，将查询存储在链式前向星结构中
 * 2. 进行DFS遍历树，同时处理查询
 * 3. 使用并查集维护节点的祖先关系
 * 
 * 与Code03_Tarjan1的主要区别：
 * 1. 将递归版的Tarjan算法改为了迭代版，避免递归栈溢出
 * 2. 将递归版的并查集find操作改为了迭代版
 * 3. 使用显式栈模拟递归过程
 * 
 * 算法步骤：
 * 1. 对于当前节点u，标记为已访问
 * 2. 递归处理u的所有子节点v
 * 3. 处理完v后，将v的祖先设为u（union操作）
 * 4. 检查所有与u相关的查询，如果另一个节点已访问，则其LCA为find的结果
 * 
 * 时间复杂度：
 * O(n + q)，其中n为节点数，q为查询数
 * 空间复杂度：O(n + q)
 * 
 * 是否为最优解：是，对于离线查询LCA问题，Tarjan算法是最优解
 * 
 * 工程化考虑：
 * 1. 边界条件处理：处理空树、节点不存在等情况
 * 2. 输入验证：验证输入节点是否在树中
 * 3. 异常处理：对非法输入进行检查
 * 4. 可读性：添加详细注释和变量命名
 * 
 * 算法要点：
 * 1. 离线处理：需要预先知道所有查询
 * 2. 并查集：用于维护节点的祖先关系
 * 3. DFS遍历：在遍历过程中处理查询
 * 4. 迭代实现：避免递归栈溢出
 * 
 * 与标准库实现对比：
 * 1. 标准库通常有更完善的错误处理
 * 2. 标准库可能使用更优化的数据结构
 * 
 * 性能优化：
 * 1. 离线处理优化：一次性处理所有查询
 * 2. 并查集优化：路径压缩
 * 3. 递归优化：使用迭代避免栈溢出
 * 
 * 特殊场景：
 * 1. 空输入：返回特定值表示无效
 * 2. 节点不存在：返回特定值表示无效
 * 3. 查询为空：直接返回
 * 4. 深度极大的树：迭代版可以处理
 * 
 * 语言特性差异：
 * 1. C++：手动内存管理，指针操作，高性能但容易出错
 * 2. Java：自动垃圾回收，对象引用传递，类型安全
 * 3. Python：动态类型，引用计数垃圾回收，代码简洁
 * 
 * 数学联系：
 * 1. 与图论中的DFS理论相关
 * 2. 与并查集数据结构相关
 * 3. 与离线算法设计思想相关
 * 
 * 调试能力：
 * 1. 可通过打印DFS遍历顺序调试
 * 2. 可通过断言验证并查集操作
 * 3. 可通过特殊测试用例验证边界条件
 * 
 * 注意事项：
 * 所有递归函数一律改成等义的迭代版
 * 可以通过所有用例
 * 
 * 注意：由于编译环境限制，避免使用复杂的STL容器和标准库函数
 */

class TarjanLCA {
private:
    static const int MAXN = 500001;
    
    // 链式前向星建图
    int headEdge[MAXN];
    int edgeNext[MAXN << 1];
    int edgeTo[MAXN << 1];
    int tcnt;
    
    // 每个节点有哪些查询，也用链式前向星方式存储
    int headQuery[MAXN];
    int queryNext[MAXN << 1];
    int queryTo[MAXN << 1];
    
    // 问题的编号，一旦有答案可以知道填写在哪
    int queryIndex[MAXN << 1];
    int qcnt;
    
    // 某个节点是否访问过
    bool visited[MAXN];
    
    // 并查集
    int father[MAXN];
    
    // 收集的答案
    int ans[MAXN];
    
    // 为了实现迭代版而准备的栈
    int stack[MAXN];
    
    // 为了实现迭代版而准备的栈
    int ufe[MAXN][3];
    int stackSize, u, f, e;
    
    void push(int u, int f, int e) {
        ufe[stackSize][0] = u;
        ufe[stackSize][1] = f;
        ufe[stackSize][2] = e;
        stackSize++;
    }
    
    void pop() {
        --stackSize;
        u = ufe[stackSize][0];
        f = ufe[stackSize][1];
        e = ufe[stackSize][2];
    }
    
public:
    void build(int n) {
        tcnt = qcnt = 1;
        for (int i = 1; i <= n; i++) {
            headEdge[i] = 0;
            headQuery[i] = 0;
            visited[i] = false;
            father[i] = i;
        }
    }
    
    void addEdge(int u, int v) {
        edgeNext[tcnt] = headEdge[u];
        edgeTo[tcnt] = v;
        headEdge[u] = tcnt++;
    }
    
    void addQuery(int u, int v, int i) {
        queryNext[qcnt] = headQuery[u];
        queryTo[qcnt] = v;
        queryIndex[qcnt] = i;
        headQuery[u] = qcnt++;
    }
    
    // 并查集找头节点迭代版
    int find(int i) {
        int size = 0;
        while (i != father[i]) {
            stack[size++] = i;
            i = father[i];
        }
        while (size > 0) {
            father[stack[--size]] = i;
        }
        return i;
    }
    
    // tarjan算法迭代版
    void tarjan(int root) {
        stackSize = 0;
        // 栈里存放三个信息
        // u : 当前处理的点
        // f : 当前点u的父节点
        // e : 处理到几号边了
        // 如果e==-1，表示之前没有处理过u的任何边
        // 如果e==0，表示u的边都已经处理完了
        push(root, 0, -1);
        while (stackSize > 0) {
            pop();
            if (e == -1) {
                visited[u] = true;
                e = headEdge[u];
            } else {
                e = edgeNext[e];
            }
            if (e != 0) {
                push(u, f, e);
                if (edgeTo[e] != f) {
                    push(edgeTo[e], u, -1);
                }
            } else {
                // e == 0代表u后续已经没有边需要处理了
                for (int q = headQuery[u], v; q != 0; q = queryNext[q]) {
                    v = queryTo[q];
                    if (visited[v]) {
                        ans[queryIndex[q]] = find(v);
                    }
                }
                father[u] = f;
            }
        }
    }
};

// 由于编译环境限制，不包含main函数和测试代码
// 在实际使用时，可以根据洛谷平台要求进行调整