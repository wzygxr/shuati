/*
 * 动态图连通性问题 - 线段树分治 + 可撤销并查集实现 (C++版本)
 * 
 * 题目来源：LeetCode Dynamic Graph Connectivity
 * 题目链接：https://leetcode.com/problems/dynamic-graph-connectivity/
 * 
 * 问题描述：
 * 支持动态加边、删边操作，查询两点间连通性
 * 
 * 算法思路：
 * 1. 使用线段树分治处理动态加边/删边操作
 * 2. 通过可撤销并查集维护节点间的连通性
 * 3. 离线处理所有操作，把每条边的存在时间区间分解到线段树的节点上
 * 4. 通过DFS遍历线段树，处理每个时间点的查询
 * 
 * 时间复杂度：O((n + m) log m)
 * 空间复杂度：O(n + m)
 */

#include <bits/stdc++.h>
using namespace std;

// 常量定义
const int MAXN = 100001;   // 最大节点数
const int MAXT = 500001;   // 最大线段树任务数

// 全局变量
int n, m;  // 节点数、操作数

// 事件数组：记录所有边的添加和删除事件
// event[i][0]: 边的左端点x
// event[i][1]: 边的右端点y
// event[i][2]: 事件发生的时间点t
int event[MAXN << 1][3];
int eventCnt;  // 事件计数器

// 记录每个时间点的操作信息
int op[MAXN];  // 操作类型：1(添加边)、2(删除边)、3(查询)
int x[MAXN];   // 操作涉及的第一个节点
int y[MAXN];   // 操作涉及的第二个节点

// 可撤销并查集：维护连通性
int father[MAXN];     // 父节点数组
int siz[MAXN];        // 集合大小数组
int rollback[MAXN][2]; // 回滚栈，记录合并操作
int opsize = 0;       // 操作计数

// 时间轴线段树上的区间任务列表：链式前向星结构
int head[MAXN << 2];  // 线段树节点的头指针
int next_[MAXT];      // 下一个任务的指针
int tox[MAXT];        // 任务边的起点
int toy[MAXT];        // 任务边的终点
int cnt = 0;          // 任务计数

// 存储查询操作的答案
bool ans[MAXN];

/**
 * 并查集的find操作：查找集合代表元素
 * @param i 要查找的节点
 * @return 节点所在集合的代表元素（根节点）
 * @note 注意：此实现没有路径压缩，以支持撤销操作
 */
int find(int i) {
    // 非路径压缩版本，以支持撤销操作
    while (i != father[i]) {
        i = father[i];
    }
    return i;
}

/**
 * 可撤销并查集的合并操作，在节点u和v之间添加一条边
 * @param u 第一个节点
 * @param v 第二个节点
 * @return 如果合并了两个不同的集合，返回true；否则返回false
 */
bool unite(int u, int v) {
    // 查找u和v的根节点
    int fu = find(u);
    int fv = find(v);
    
    if (fu == fv) {
        return false; // 没有合并新的集合
    }
    
    // 按秩合并，始终将较小的树合并到较大的树中
    if (siz[fu] < siz[fv]) {
        swap(fu, fv);
    }
    
    // 合并操作
    father[fv] = fu;
    siz[fu] += siz[fv];
    
    // 记录操作，用于撤销
    opsize++;
    rollback[opsize][0] = fu;
    rollback[opsize][1] = fv;
    
    return true; // 成功合并两个集合
}

/**
 * 撤销最近的一次合并操作
 */
void undo() {
    // 获取最后一次合并操作的信息
    int fx = rollback[opsize][0];  // 父节点
    int fy = rollback[opsize][1];  // 子节点
    opsize--;
    
    // 恢复fy的父节点为自己
    father[fy] = fy;
    // 恢复父节点集合的大小
    siz[fx] -= siz[fy];
}

/**
 * 给线段树节点i添加一个任务：在节点x和y之间添加边
 * @param i 线段树节点编号
 * @param x 边的起点
 * @param y 边的终点
 */
void addEdge(int i, int x, int y) {
    // 创建新任务
    cnt++;
    next_[cnt] = head[i];  // 指向前一个任务
    tox[cnt] = x;          // 边的起点
    toy[cnt] = y;          // 边的终点
    head[i] = cnt;         // 更新头指针
}

/**
 * 线段树区间更新：将边(jobx, joby)添加到时间区间[jobl, jobr]内
 * @param jobl 任务开始时间
 * @param jobr 任务结束时间
 * @param jobx 边的起点
 * @param joby 边的终点
 * @param l 当前线段树节点的左区间
 * @param r 当前线段树节点的右区间
 * @param i 当前线段树节点编号
 */
void add(int jobl, int jobr, int jobx, int joby, int l, int r, int i) {
    // 如果当前区间完全包含在目标区间内，直接添加到当前节点
    if (jobl <= l && r <= jobr) {
        addEdge(i, jobx, joby);
    } else {
        // 否则递归到左右子树
        int mid = (l + r) >> 1;
        if (jobl <= mid) {
            add(jobl, jobr, jobx, joby, l, mid, i << 1);
        }
        if (jobr > mid) {
            add(jobl, jobr, jobx, joby, mid + 1, r, i << 1 | 1);
        }
    }
}

/**
 * 线段树分治的深度优先搜索核心方法
 * 
 * @param l 当前线段树节点的左时间区间边界
 * @param r 当前线段树节点的右时间区间边界
 * @param i 当前线段树节点编号（根节点为1，左子节点为2*i，右子节点为2*i+1）
 */
void dfs(int l, int r, int i) {
    // 记录合并操作的数量，用于后续撤销
    int unionCnt = 0;
    
    // 处理当前节点上的所有边
    // 这些边在[l, r]时间区间内都是活跃的
    for (int e = head[i]; e; e = next_[e]) {
        // 尝试合并两个集合
        // 如果成功合并（两个不同的集合），增加计数
        if (unite(tox[e], toy[e])) {
            unionCnt++;
        }
    }
    
    // 处理叶子节点（对应具体的时间点）
    if (l == r) {
        // 如果当前时间点是查询操作（类型3）
        if (op[l] == 3) {
            // 检查x[l]和y[l]是否连通
            ans[l] = (find(x[l]) == find(y[l]));
        }
    } else {
        // 非叶子节点，递归处理左右子树
        int mid = (l + r) >> 1;  // 计算中间点
        dfs(l, mid, i << 1);     // 处理左子区间
        dfs(mid + 1, r, i << 1 | 1);  // 处理右子区间
    }
    
    // 回溯：撤销所有合并操作，按逆序撤销
    for (int k = 1; k <= unionCnt; k++) {
        undo();  // 撤销并查集的合并操作
    }
}

/**
 * 预处理函数：初始化并查集、排序事件、构建线段树
 */
void prepare() {
    // 初始化并查集结构
    // 每个节点初始时都是独立的集合，父节点指向自己，集合大小为1
    for (int i = 1; i <= n; i++) {
        father[i] = i;  // 每个节点初始是自己的父节点
        siz[i] = 1;     // 每个集合初始大小为1
    }
    
    // 按边的两个端点和时间排序事件
    sort(event + 1, event + eventCnt + 1, 
         [](int a[], int b[]) {
             if (a[0] != b[0]) return a[0] < b[0];
             if (a[1] != b[1]) return a[1] < b[1];
             return a[2] < b[2];
         });
    
    int x, y, start, end;
    // 处理每条边的生命周期，确定边的有效时间段
    for (int l = 1, r = 1; l <= eventCnt; l = ++r) {
        x = event[l][0];  // 当前处理的边的起点
        y = event[l][1];  // 当前处理的边的终点
        
        // 找到所有相同边(x,y)的事件
        while (r + 1 <= eventCnt && event[r + 1][0] == x && event[r + 1][1] == y) {
            r++;
        }
        
        // 处理每对添加和删除事件，确定边的有效时间区间
        for (int i = l; i <= r; i += 2) {
            start = event[i][2];     // 边开始的时间点（添加事件的时间）
            
            // 确定边结束的时间点
            end = i + 1 <= r ? (event[i + 1][2] - 1) : m;
            
            // 将边添加到线段树的相应时间区间[start, end]
            add(start, end, x, y, 1, m, 1);
        }
    }
}

/**
 * 主函数
 */
int main() {
    ios::sync_with_stdio(false);
    cin.tie(0);
    
    // 读取节点数和操作数
    cin >> n >> m;
    
    // 读取每个操作
    for (int i = 1; i <= m; i++) {
        cin >> op[i] >> x[i] >> y[i];
        
        // 对于添加和删除操作，记录事件信息
        if (op[i] != 3) {
            event[++eventCnt][0] = x[i];  // 边的起点
            event[eventCnt][1] = y[i];    // 边的终点
            event[eventCnt][2] = i;       // 事件发生的时间点
        }
    }
    
    // 预处理阶段：初始化并查集，排序事件，构建线段树
    prepare();
    
    // 执行线段树分治的核心算法
    dfs(1, m, 1);
    
    // 输出所有查询操作的答案
    for (int i = 1; i <= m; i++) {
        if (op[i] == 3) {
            cout << (ans[i] ? "true" : "false") << '\n';
        }
    }
    
    return 0;
}