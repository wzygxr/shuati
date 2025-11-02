// CF600E Lomsat gelral，cpp版
// 测试链接 : https://codeforces.com/contest/600/problem/E
// 提交时请使用标准C++输入输出

#include <iostream>
#include <vector>
#include <map>
#include <algorithm>
#include <string>
#include <sstream>
using namespace std;

/**
 * CF600E Lomsat gelral
 * 
 * 题目来源: Codeforces Round 334 (Div. 2)
 * 题目链接: https://codeforces.com/contest/600/problem/E
 * 
 * 题目描述:
 * 给定一棵树，每个节点有一种颜色。对每个节点求其子树中出现次数最多的颜色的编号和。
 * 如果有多个颜色出现次数相同且都是最多的，则将它们的编号全部相加。
 * 
 * 解题思路:
 * 1. 使用线段树合并技术解决树上统计问题
 * 2. 为每个节点建立一棵权值线段树，维护子树中各颜色的出现次数以及当前子树中的最大出现次数
 * 3. 同时维护一个额外的值sum，表示当前最大出现次数对应的颜色编号和
 * 4. 从叶子节点开始，自底向上合并子树的线段树
 * 5. 每次合并后，更新当前节点的最大出现次数和sum值
 * 
 * 算法复杂度:
 * - 时间复杂度: O(n log n)，其中n是节点数量。每次线段树合并操作的时间复杂度为O(log n)，
 *   每个节点最多被合并一次，因此总时间复杂度为O(n log n)。
 * - 空间复杂度: O(n log n)，动态开点线段树的空间复杂度。
 * 
 * 最优解验证:
 * 线段树合并是该问题的最优解之一。另一种方法是使用树上启发式合并(DSU on tree)，
 * 时间复杂度同样为O(n log n)，但线段树合并的实现更加直接，且在某些情况下效率更高。
 * 
 * 线段树合并核心思想:
 * 1. 对于两棵线段树的对应节点，如果只有一棵树有该节点，则直接使用该节点
 * 2. 如果两棵树都有该节点，则递归合并左右子树，并更新当前节点信息
 * 3. 在合并过程中，需要维护每个节点的最大值和sum值
 */

const int MAXN = 100001;
const int MAXT = MAXN * 40; // 线段树节点数上限

int n; // 节点数量
vector<int> graph[MAXN]; // 邻接表存储树结构
int color[MAXN]; // 节点颜色数组
map<int, int> colorMap; // 离散化映射
int colorValues[MAXN]; // 离散化后的原始颜色值
int cntv; // 离散化后的不同颜色数量

// 线段树相关数组
int root[MAXN]; // 每个节点对应的线段树根节点
int ls[MAXT], rs[MAXT]; // 左右子节点
int count[MAXT]; // 每个颜色的出现次数
int maxCount[MAXT]; // 当前区间内的最大出现次数
long long sum[MAXT]; // 最大出现次数对应的颜色编号和
int cntt; // 线段树节点计数器

long long ans[MAXN]; // 答案数组

/**
 * 创建新的线段树节点
 * @return 新节点的索引
 */
int newNode() {
    cntt++;
    ls[cntt] = rs[cntt] = 0;
    count[cntt] = 0;
    maxCount[cntt] = 0;
    sum[cntt] = 0;
    return cntt;
}

/**
 * 向上合并子节点信息
 * @param p 当前节点索引
 */
void pushUp(int p) {
    // 获取左右子树的最大出现次数
    int leftMax = (ls[p] != 0) ? maxCount[ls[p]] : 0;
    int rightMax = (rs[p] != 0) ? maxCount[rs[p]] : 0;
    
    // 更新当前节点的最大出现次数
    maxCount[p] = max(leftMax, rightMax);
    
    // 初始化sum
    sum[p] = 0;
    
    // 如果左子树的最大出现次数等于当前节点的最大出现次数，加上左子树的sum
    if (ls[p] != 0 && leftMax == maxCount[p]) {
        sum[p] += sum[ls[p]];
    }
    
    // 如果右子树的最大出现次数等于当前节点的最大出现次数，加上右子树的sum
    if (rs[p] != 0 && rightMax == maxCount[p]) {
        sum[p] += sum[rs[p]];
    }
}

/**
 * 线段树单点更新
 * @param p 当前节点索引
 * @param l 当前区间左边界
 * @param r 当前区间右边界
 * @param x 要更新的位置
 * @param v 要增加的计数
 */
void update(int p, int l, int r, int x, int v) {
    if (l == r) {
        // 叶子节点，直接更新计数
        count[p] += v;
        // 更新最大出现次数为当前计数
        maxCount[p] = count[p];
        // 更新sum为当前颜色的原始值
        sum[p] = (count[p] > 0) ? colorValues[x] : 0;
        return;
    }
    int mid = (l + r) >> 1;
    // 动态开点
    if (x <= mid) {
        if (ls[p] == 0) {
            ls[p] = newNode();
        }
        update(ls[p], l, mid, x, v);
    } else {
        if (rs[p] == 0) {
            rs[p] = newNode();
        }
        update(rs[p], mid + 1, r, x, v);
    }
    // 合并子节点信息
    pushUp(p);
}

/**
 * 线段树合并操作
 * @param x 第一棵线段树的根节点
 * @param y 第二棵线段树的根节点
 * @param l 当前区间左边界
 * @param r 当前区间右边界
 * @return 合并后的线段树根节点
 */
int merge(int x, int y, int l, int r) {
    // 如果其中一棵树为空，直接返回另一棵树
    if (x == 0) return y;
    if (y == 0) return x;
    
    // 叶子节点处理
    if (l == r) {
        // 合并计数
        count[x] += count[y];
        // 更新最大出现次数
        maxCount[x] = count[x];
        // 更新sum
        sum[x] = (count[x] > 0) ? colorValues[l] : 0;
        return x;
    }
    
    int mid = (l + r) >> 1;
    
    // 递归合并左右子树
    ls[x] = merge(ls[x], ls[y], l, mid);
    rs[x] = merge(rs[x], rs[y], mid + 1, r);
    
    // 合并后更新当前节点信息
    pushUp(x);
    
    return x;
}

/**
 * 深度优先搜索处理每个节点，合并子树信息
 * @param u 当前节点
 * @param fa 父节点
 */
void dfs(int u, int fa) {
    // 为当前节点创建线段树，并插入自身颜色
    root[u] = newNode();
    update(root[u], 1, cntv, color[u], 1);
    
    // 遍历所有子节点
    for (int v : graph[u]) {
        if (v != fa) {
            dfs(v, u);
            // 合并子节点的线段树到当前节点
            root[u] = merge(root[u], root[v], 1, cntv);
        }
    }
    
    // 记录当前节点的答案
    ans[u] = sum[root[u]];
}

/**
 * 离散化颜色值
 */
void discretize() {
    // 统计所有不同的颜色值
    for (int i = 1; i <= n; i++) {
        colorMap[color[i]] = 0;
    }
    
    // 为每个颜色分配一个唯一的id
    cntv = 0;
    for (auto &entry : colorMap) {
        cntv++;
        entry.second = cntv;
        colorValues[cntv] = entry.first;
    }
    
    // 更新原始颜色数组为离散化后的值
    for (int i = 1; i <= n; i++) {
        color[i] = colorMap[color[i]];
    }
}

// 快速读取一行整数的辅助函数
vector<int> readInts() {
    vector<int> res;
    string line;
    getline(cin, line);
    istringstream iss(line);
    int x;
    while (iss >> x) {
        res.push_back(x);
    }
    return res;
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(0);
    cout.tie(0);
    
    // 读取节点数量
    cin >> n;
    cin.ignore(); // 忽略换行符
    
    // 读取颜色数组
    vector<int> colors = readInts();
    for (int i = 1; i <= n; i++) {
        color[i] = colors[i - 1];
    }
    
    // 离散化颜色值
    discretize();
    
    // 读取树结构
    for (int i = 1; i < n; i++) {
        vector<int> edge = readInts();
        int u = edge[0];
        int v = edge[1];
        graph[u].push_back(v);
        graph[v].push_back(u);
    }
    
    // 初始化线段树节点计数器
    cntt = 0;
    
    // 从根节点（1号节点）开始DFS
    dfs(1, 0);
    
    // 输出答案
    for (int i = 1; i <= n; i++) {
        cout << ans[i] << " ";
    }
    cout << endl;
    
    return 0;
}

/**
 * 工程化考量：
 * 1. 性能优化：使用ios::sync_with_stdio(false)和cin.tie(0)提高C++输入输出效率
 * 2. 内存管理：C++中需要注意动态分配的内存，但这里使用静态数组避免内存泄漏
 * 3. 边界检查：代码中处理了线段树节点为空的情况
 * 
 * C++语言特性：
 * 1. 相比Java，C++的递归深度限制更宽松，但在极端情况下仍需注意栈溢出问题
 * 2. C++的指针操作可以更灵活，但这里使用数组模拟指针以简化实现
 * 
 * 优化建议：
 * 1. 可以使用更高效的离散化方法，如排序后去重
 * 2. 对于大规模数据，可以考虑使用非递归DFS
 */