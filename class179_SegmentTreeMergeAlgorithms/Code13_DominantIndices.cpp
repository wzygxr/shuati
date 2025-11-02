// CF1009F Dominant Indices，C++版
// 测试链接 : https://codeforces.com/contest/1009/problem/F

#include <iostream>
#include <vector>
#include <cstring>
using namespace std;

/**
 * CF1009F Dominant Indices
 * 
 * 题目来源: Codeforces Round 484 (Div. 2)
 * 题目链接: https://codeforces.com/contest/1009/problem/F
 * 
 * 题目描述:
 * 给定一棵树，对于每个节点u，求其子树中距离u恰好为k的节点数最大的k值。
 * 如果有多个k值有相同的最大节点数，取最小的k。
 * 
 * 解题思路:
 * 1. 使用深度优先搜索（DFS）遍历整棵树
 * 2. 对于每个节点u，维护一个线段树，记录其子树中各个深度的节点数目
 * 3. 在DFS过程中，递归处理子节点，并将子节点的线段树合并到父节点
 * 4. 在合并过程中，动态更新每个节点的最优k值（出现次数最多的深度，且最小）
 * 
 * 算法复杂度:
 * - 时间复杂度: O(n log n)，其中n是树的节点数。每个节点最多被访问一次，
 *   每次线段树合并操作的时间复杂度是O(log n)。
 * - 空间复杂度: O(n log n)，动态开点线段树的空间复杂度。
 * 
 * 最优解验证:
 * 线段树合并是该问题的最优解。其他可能的解法包括暴力统计每个节点的子树深度分布，
 * 但时间复杂度为O(n^2)，无法通过大规模测试用例。
 * 
 * 线段树合并解决树形统计问题的核心思想:
 * 1. 后序遍历树，先处理所有子节点
 * 2. 为每个节点维护一个数据结构，记录所需的统计信息
 * 3. 将子节点的数据结构合并到父节点，形成父节点的完整统计信息
 * 4. 利用合并过程中的中间结果回答问题
 */

// 定义常量
const int MAXN = 100010;
const int MAX_DEPTH = 100000; // 树的最大深度
const int MAX_NODE = MAXN * 20; // 线段树节点数量上限

// 树的边表示
vector<int> tree[MAXN];
// 每个节点的答案
int ans[MAXN];
// 线段树的根节点数组
int root[MAXN];

// 线段树节点信息
struct Node {
    int ls, rs; // 左右子节点
    int maxVal; // 该区间的最大值
    int pos;    // 最大值对应的位置
} tr[MAX_NODE];

int cnt; // 线段树节点计数器

/**
 * 创建新的线段树节点
 * @return 新创建的节点编号
 */
int newNode() {
    cnt++;
    tr[cnt].ls = tr[cnt].rs = 0;
    tr[cnt].maxVal = 0;
    tr[cnt].pos = 0;
    return cnt;
}

/**
 * 向上合并线段树节点信息
 * @param p 当前节点编号
 */
void pushUp(int p) {
    int ls = tr[p].ls;
    int rs = tr[p].rs;
    
    // 如果左子树为空，直接使用右子树的信息
    if (!ls) {
        tr[p].maxVal = tr[rs].maxVal;
        tr[p].pos = tr[rs].pos;
        return;
    }
    // 如果右子树为空，直接使用左子树的信息
    if (!rs) {
        tr[p].maxVal = tr[ls].maxVal;
        tr[p].pos = tr[ls].pos;
        return;
    }
    
    // 左右子树都不为空，比较两个子树的最大值
    if (tr[ls].maxVal > tr[rs].maxVal) {
        // 左子树的最大值更大
        tr[p].maxVal = tr[ls].maxVal;
        tr[p].pos = tr[ls].pos;
    } else if (tr[ls].maxVal < tr[rs].maxVal) {
        // 右子树的最大值更大
        tr[p].maxVal = tr[rs].maxVal;
        tr[p].pos = tr[rs].pos;
    } else {
        // 最大值相等，取位置较小的
        tr[p].maxVal = tr[ls].maxVal;
        tr[p].pos = min(tr[ls].pos, tr[rs].pos);
    }
}

/**
 * 线段树更新操作
 * @param p 当前节点编号
 * @param l 当前区间左边界
 * @param r 当前区间右边界
 * @param x 需要更新的位置
 * @param v 更新的值（这里是+1）
 */
void update(int &p, int l, int r, int x, int v) {
    if (!p) {
        p = newNode();
    }
    if (l == r) {
        // 叶子节点，直接更新值
        tr[p].maxVal += v;
        tr[p].pos = l;
        return;
    }
    
    int mid = (l + r) >> 1;
    
    // 根据x的位置决定更新左子树还是右子树
    if (x <= mid) {
        update(tr[p].ls, l, mid, x, v);
    } else {
        update(tr[p].rs, mid + 1, r, x, v);
    }
    
    // 更新当前节点的最大值和对应位置
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
    if (!x) return y;
    if (!y) return x;
    
    // 叶子节点处理
    if (l == r) {
        // 合并两个叶子节点的值
        tr[x].maxVal += tr[y].maxVal;
        tr[x].pos = l;
        return x;
    }
    
    int mid = (l + r) >> 1;
    
    // 递归合并左右子树
    tr[x].ls = merge(tr[x].ls, tr[y].ls, l, mid);
    tr[x].rs = merge(tr[x].rs, tr[y].rs, mid + 1, r);
    
    // 合并后更新当前节点的信息
    pushUp(x);
    
    return x;
}

/**
 * 深度优先搜索遍历树
 * @param u 当前节点
 * @param fa 父节点
 */
void dfs(int u, int fa) {
    // 为当前节点创建线段树，并初始化为深度0（距离自己0）
    root[u] = newNode();
    update(root[u], 0, MAX_DEPTH, 0, 1);
    
    // 遍历所有子节点（排除父节点）
    for (int v : tree[u]) {
        if (v == fa) continue;
        
        // 递归处理子节点
        dfs(v, u);
        
        // 将子节点的线段树合并到当前节点
        root[u] = merge(root[u], root[v], 0, MAX_DEPTH);
    }
    
    // 记录当前节点的答案（线段树中最大值对应的位置）
    ans[u] = tr[root[u]].pos;
}

int main() {
    // 关闭同步，提高输入输出效率
    ios::sync_with_stdio(false);
    cin.tie(0);
    
    int n;
    cin >> n;
    
    // 读取树的边
    for (int i = 1; i < n; i++) {
        int u, v;
        cin >> u >> v;
        tree[u].push_back(v);
        tree[v].push_back(u);
    }
    
    // 初始化线段树节点计数器
    cnt = 0;
    
    // 从根节点（1号节点）开始DFS
    dfs(1, 0);
    
    // 输出所有节点的答案
    for (int i = 1; i <= n; i++) {
        cout << ans[i] << '\n';
    }
    
    return 0;
}

/**
 * 工程化考量：
 * 1. 输入输出效率：使用ios::sync_with_stdio(false)和cin.tie(0)提高IO效率
 * 2. 空间分配：使用结构体数组预分配线段树空间
 * 3. 异常处理：通过判断父节点避免重复访问
 * 4. 内存优化：动态开点线段树避免了预分配过大数组
 * 
 * C++语言特性：
 * 1. 引用传参：update函数中使用引用传递根节点，方便修改
 * 2. 结构体：使用结构体封装线段树节点信息
 * 3. 向量容器：使用vector存储树的边列表
 * 4. 关闭同步：C++特有的IO优化手段
 * 
 * 调试技巧：
 * 1. 可以使用printf进行中间结果打印
 * 2. 可以在merge和update函数中添加断言
 * 
 * 优化空间：
 * 1. 可以使用内存池管理线段树节点
 * 2. 可以根据实际数据调整MAX_DEPTH的大小
 * 3. 对于大数据量，可以考虑非递归实现DFS避免栈溢出
 */