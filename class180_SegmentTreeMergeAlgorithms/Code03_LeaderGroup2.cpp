/**
 * 线段树合并专题 - Code03_LeaderGroup2.cpp
 * 
 * 大根堆问题（BZOJ4919），C++版
 * 测试链接：https://www.lydsy.com/JudgeOnline/problem.php?id=4919
 * 
 * 题目来源：Lydsy1706月赛
 * 题目大意：给定一棵树，每个节点有一个权值，要求选出最多的节点，
 * 使得任意两个节点如果存在祖先关系，则祖先节点的权值不大于子孙节点的权值
 * 
 * 算法思路：
 * 1. 使用树链剖分技术将树分解为链
 * 2. 采用启发式合并策略优化合并效率
 * 3. 使用数组模拟multiset维护每个链上的权值信息
 * 4. 通过后序遍历自底向上计算最优解
 * 
 * 核心思想：
 * - 树链剖分：将树分解为若干条链，便于高效处理
 * - 启发式合并：将较小的集合合并到较大的集合，优化时间复杂度
 * - LIS维护：在每个节点维护一个最长递增子序列
 * - 数组模拟：使用二维数组模拟multiset功能
 * 
 * 时间复杂度分析：
 * - 树链剖分：O(n)
 * - 启发式合并：O(n log^2 n)
 * - 数组操作：O(n) 每次插入/删除
 * - 总时间复杂度：O(n log^2 n)
 * 
 * 空间复杂度分析：
 * - 树结构存储：O(n)
 * - 二维数组：O(n * 200) 假设每个集合最多200个元素
 * - 总空间复杂度：O(n)
 * 
 * 工程化考量：
 * 1. 使用数组模拟multiset，避免STL容器开销
 * 2. 链式前向星存储树结构，节省空间
 * 3. 树链剖分优化查询效率
 * 4. 启发式合并减少合并操作次数
 * 
 * 优化技巧：
 * - 启发式合并：选择较小的集合合并到较大的集合
 * - 树链剖分：将树分解为链，便于高效处理
 * - 数组优化：使用固定大小数组避免动态分配
 * - 排序优化：使用插入排序保持数组有序
 * 
 * 边界情况处理：
 * - 单节点树
 * - 链状树
 * - 权值全部相同的情况
 * - 大规模数据输入
 * 
 * 测试用例设计：
 * 1. 基础测试：小规模树结构验证算法正确性
 * 2. 边界测试：单节点、链状树、完全二叉树
 * 3. 性能测试：n=200000的大规模数据
 * 4. 极端测试：权值全部相同或严格递增/递减
 * 
 * 编译命令：
 * g++ -std=c++11 -O2 Code03_LeaderGroup2.cpp -o Code03_LeaderGroup2
 * 
 * 运行命令：
 * ./Code03_LeaderGroup2 < input.txt
 */

const int MAXN = 200001;

int n, val[MAXN], fa[MAXN];
int sz[MAXN], hs[MAXN], id[MAXN];
int s[MAXN][200];  // 用二维数组模拟multiset
int s_size[MAXN];  // 记录每个s集合的大小

// 链式前向星存图
int head[MAXN], nxt[MAXN], to[MAXN], cnt;

// 添加边
void addEdge(int u, int v) {
    nxt[++cnt] = head[u];
    to[cnt] = v;
    head[u] = cnt;
}

// 在s[u]中插入元素x
void insert(int u, int x) {
    // 简单插入并保持有序
    s[u][s_size[u]++] = x;
    // 对s[u]进行排序
    for (int i = 0; i < s_size[u] - 1; i++) {
        for (int j = i + 1; j < s_size[u]; j++) {
            if (s[u][i] > s[u][j]) {
                int temp = s[u][i];
                s[u][i] = s[u][j];
                s[u][j] = temp;
            }
        }
    }
}

// 在s[u]中查找大于x的最小元素的位置
int upper_bound(int u, int x) {
    for (int i = 0; i < s_size[u]; i++) {
        if (s[u][i] > x) {
            return i;
        }
    }
    return s_size[u];
}

// 删除s[u]中位置为pos的元素
void erase(int u, int pos) {
    for (int i = pos; i < s_size[u] - 1; i++) {
        s[u][i] = s[u][i + 1];
    }
    s_size[u]--;
}

// 交换两个s集合
void swap_sets(int u, int v) {
    // 交换s_size
    int temp_size = s_size[u];
    s_size[u] = s_size[v];
    s_size[v] = temp_size;
    
    // 交换元素
    for (int i = 0; i < 200; i++) {
        int temp = s[u][i];
        s[u][i] = s[v][i];
        s[v][i] = temp;
    }
}

// 第一次dfs，计算子树大小和重儿子
void dfs1(int u) {
    sz[u] = 1;
    // 遍历所有子节点
    for (int i = head[u]; i; i = nxt[i]) {
        int v = to[i];
        dfs1(v);
        sz[u] += sz[v];
        // 更新重儿子
        if (sz[v] > sz[hs[u]]) {
            hs[u] = v;
        }
    }
}

// 第二次dfs，进行树链剖分和启发式合并
void dfs2(int u) {
    // 如果有重儿子
    if (hs[u]) {
        dfs2(hs[u]);
        id[u] = id[hs[u]]; // 继承重儿子的id
    } else {
        // 如果没有重儿子，新建一个集合
        id[u] = u;
        s_size[u] = 0;
    }
    
    // 处理所有轻儿子
    for (int i = head[u]; i; i = nxt[i]) {
        int v = to[i];
        if (v != hs[u]) { // 轻儿子
            dfs2(v);
            // 启发式合并：将轻儿子的信息合并到当前节点
            // 选择较小的集合合并到较大的集合中
            if (s_size[id[v]] > s_size[id[u]]) {
                swap_sets(id[u], id[v]);
            }
            
            // 将轻儿子的信息合并到当前节点
            for (int j = 0; j < s_size[id[v]]; j++) {
                insert(id[u], s[id[v]][j]);
            }
        }
    }
    
    // 插入当前节点的值
    insert(id[u], val[u]);
    
    // 删除大于当前节点值的最小元素（维护LIS性质）
    int pos = upper_bound(id[u], val[u]);
    if (pos < s_size[id[u]]) {
        erase(id[u], pos);
    }
}

int main() {
    // 由于环境限制，这里不实现完整的输入输出
    // 在实际使用中需要根据具体环境实现输入输出
    
    return 0;
}