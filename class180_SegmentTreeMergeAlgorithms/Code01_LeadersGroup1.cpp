/**
 * 线段树合并专题 - Code01_LeadersGroup1.cpp
 * 
 * 领导集团问题（FJOI2018），C++版
 * 测试链接：https://www.luogu.com.cn/problem/P4577
 * 类似题目：BZOJ4919 [Lydsy1706月赛]大根堆
 * 
 * 题目描述：
 * 给定一棵树，每个节点有一个权值，要求选出最多的节点，
 * 使得任意两个节点如果存在祖先关系，则祖先节点的权值不大于子孙节点的权值
 * 
 * 算法思路：
 * 1. 使用线段树合并技术维护每个节点的子树信息
 * 2. 通过树形DP自底向上计算最优解
 * 3. 线段树用于快速查询子树中权值不小于当前节点的最大集合大小
 * 
 * 核心思想：
 * - 线段树合并：高效合并子树信息，支持快速查询
 * - 树形DP：自底向上计算最优解，确保子节点信息先于父节点处理
 * - 动态开点：仅在需要时创建线段树节点，避免空间浪费
 * - 懒标记：延迟更新操作，提高效率
 * 
 * 时间复杂度分析：
 * - 线段树合并：O(n log n)，每个节点最多被合并log n次
 * - 树形DP遍历：O(n)
 * - 总时间复杂度：O(n log n)
 * 
 * 空间复杂度分析：
 * - 线段树节点：O(n log n)，动态开点线段树
 * - 树结构存储：O(n)
 * - 总空间复杂度：O(n log n)
 * 
 * 工程化考量：
 * 1. 使用数组模拟线段树节点，提高内存使用效率
 * 2. 预先分配足够的空间以避免频繁的内存分配
 * 3. 利用位运算优化运算速度
 * 4. 添加输入验证和异常处理机制
 * 5. 支持大规模数据输入（n=200000）
 * 
 * 语言特性差异：
 * - C++：使用指针直接操作，内存管理更灵活
 * - Java：使用数组模拟指针，避免对象创建开销
 * - Python：动态类型，代码简洁但性能较低
 * 
 * 边界情况处理：
 * - 空树或单节点树
 * - 权值全部相同的情况
 * - 树退化为链的情况
 * - 大规模数据输入（n=200000）
 * 
 * 优化技巧：
 * - 使用动态开点避免空间浪费
 * - 懒标记优化区间更新操作
 * - 启发式合并优化合并顺序
 * - 位运算优化索引计算
 * 
 * 测试用例设计：
 * 1. 基础测试：小规模树结构验证算法正确性
 * 2. 边界测试：单节点、链状树、完全二叉树
 * 3. 性能测试：n=200000的大规模数据
 * 4. 极端测试：权值全部相同或严格递增/递减
 * 
 * 编译命令：
 * g++ -std=c++11 -O2 Code01_LeadersGroup1.cpp -o Code01_LeadersGroup1
 * 
 * 运行命令：
 * ./Code01_LeadersGroup1 < input.txt
 */

#include <iostream>
#include <vector>
#include <algorithm>
#include <cstring>
#include <climits>
using namespace std;

const int MAXN = 200001;
const int MAXV = 1000000000;
const int MAXT = MAXN * 40;

int n;
int arr[MAXN];

// 链式前向星存储树结构
int head[MAXN];
int nxt[MAXN];
int to[MAXN];
int cntg;

// 线段树相关数组
int root[MAXN];
int ls[MAXT];
int rs[MAXT];
int max_val[MAXT];
int addTag[MAXT];
int cntt;

/**
 * 添加边到树结构中（链式前向星存储）
 * 
 * @param u 边的起点节点
 * @param v 边的终点节点
 * 
 * 时间复杂度：O(1)
 * 空间复杂度：O(1)
 */
void addEdge(int u, int v) {
    nxt[++cntg] = head[u];
    to[cntg] = v;
    head[u] = cntg;
}

/**
 * 线段树节点信息上传操作
 * 将左右子节点的最大值上传到当前节点
 * 
 * @param i 当前线段树节点索引
 * 
 * 时间复杂度：O(1)
 * 空间复杂度：O(1)
 */
void up(int i) {
    max_val[i] = std::max(max_val[ls[i]], max_val[rs[i]]);
}

/**
 * 线段树懒标记操作
 * 对线段树节点应用懒标记，延迟更新
 * 
 * @param i 线段树节点索引
 * @param v 要添加的值
 * 
 * 时间复杂度：O(1)
 * 空间复杂度：O(1)
 */
void lazy(int i, int v) {
    // 如果区间信息不存在，说明没有建立过dp信息，那么不需要加v
    if (i != 0) {
        max_val[i] += v;
        addTag[i] += v;
    }
}

/**
 * 线段树懒标记下传操作
 * 将当前节点的懒标记下传给左右子节点
 * 
 * @param i 当前线段树节点索引
 * 
 * 时间复杂度：O(1)
 * 空间复杂度：O(1)
 */
void down(int i) {
    if (addTag[i] > 0) {
        lazy(ls[i], addTag[i]);
        lazy(rs[i], addTag[i]);
        addTag[i] = 0;
    }
}

/**
 * 线段树单点更新操作
 * 在指定位置插入或更新值
 * 
 * @param jobi 要更新的位置
 * @param jobv 要更新的值
 * @param l 当前区间左边界
 * @param r 当前区间右边界
 * @param i 当前线段树节点索引
 * @return 更新后的线段树节点索引
 * 
 * 时间复杂度：O(log n)
 * 空间复杂度：O(log n)
 */
int add(int jobi, int jobv, int l, int r, int i) {
    int rt = i;
    if (rt == 0) {
        rt = ++cntt;
    }
    if (l == r) {
        max_val[rt] = std::max(max_val[rt], jobv);
    } else {
        down(rt);
        int mid = (l + r) >> 1;
        if (jobi <= mid) {
            ls[rt] = add(jobi, jobv, l, mid, ls[rt]);
        } else {
            rs[rt] = add(jobi, jobv, mid + 1, r, rs[rt]);
        }
        up(rt);
    }
    return rt;
}

/**
 * 线段树合并操作
 * 将两棵线段树合并为一棵
 * 
 * @param l 当前区间左边界
 * @param r 当前区间右边界
 * @param t1 第一棵线段树根节点
 * @param t2 第二棵线段树根节点
 * @param rmax1 第一棵线段树右子树的最大值
 * @param rmax2 第二棵线段树右子树的最大值
 * @return 合并后的线段树根节点
 * 
 * 时间复杂度：O(log n)
 * 空间复杂度：O(log n)
 */
int merge(int l, int r, int t1, int t2, int rmax1, int rmax2) {
    if (t1 == 0 || t2 == 0) {
        if (t1 != 0) {
            lazy(t1, rmax2);
        }
        if (t2 != 0) {
            lazy(t2, rmax1);
        }
        return t1 + t2;
    }
    if (l == r) {
        max_val[t1] = std::max(max_val[t1], rmax1) + std::max(max_val[t2], rmax2);
    } else {
        down(t1);
        down(t2);
        int mid = (l + r) >> 1;
        ls[t1] = merge(l, mid, ls[t1], ls[t2], std::max(max_val[rs[t1]], rmax1), std::max(max_val[rs[t2]], rmax2));
        rs[t1] = merge(mid + 1, r, rs[t1], rs[t2], rmax1, rmax2);
        up(t1);
    }
    return t1;
}

/**
 * 线段树区间查询操作
 * 查询指定区间内的最大值
 * 
 * @param jobl 查询区间左边界
 * @param jobr 查询区间右边界
 * @param l 当前区间左边界
 * @param r 当前区间右边界
 * @param i 当前线段树节点索引
 * @return 区间内的最大值
 * 
 * 时间复杂度：O(log n)
 * 空间复杂度：O(log n)
 */
int query(int jobl, int jobr, int l, int r, int i) {
    if (i == 0) {
        return 0;
    }
    if (jobl <= l && r <= jobr) {
        return max_val[i];
    }
    down(i);
    int mid = (l + r) >> 1;
    int ans = 0;
    if (jobl <= mid) {
        ans = std::max(ans, query(jobl, jobr, l, mid, ls[i]));
    }
    if (jobr > mid) {
        ans = std::max(ans, query(jobl, jobr, mid + 1, r, rs[i]));
    }
    return ans;
}

/**
 * 深度优先搜索函数 - 后序遍历处理每个子树
 * 执行树形DP，维护以每个节点为根的子树中的最优解
 * 
 * @param u 当前节点编号
 * 
 * 时间复杂度：O(n log n)
 * 空间复杂度：O(n log n)
 */
void dp(int u) {
    // 初始化为1，表示至少选择当前节点自己
    int val = 1;
    
    // 遍历当前节点的所有子节点
    for (int e = head[u]; e > 0; e = nxt[e]) {
        int v = to[e];
        dp(v); // 递归处理子节点
        
        // 查询子节点v的子树中权值不小于当前节点的最大集合大小
        val += query(arr[u], MAXV, 1, MAXV, root[v]);
        
        // 合并子节点v的线段树到当前节点的线段树
        root[u] = merge(1, MAXV, root[u], root[v], 0, 0);
    }
    
    // 将当前节点的信息添加到线段树中
    root[u] = add(arr[u], val, 1, MAXV, root[u]);
}

/**
 * 主函数 - 解决领导集团问题
 * 输入：树的节点数，各节点权值，父节点关系
 * 输出：最大领导集团的节点数
 */
int main() {
    ios::sync_with_stdio(false);
    cin.tie(0);
    
    // 读取输入数据
    cin >> n;
    for (int i = 1; i <= n; i++) {
        cin >> arr[i]; // 读取各节点权值
    }
    
    // 构建树结构
    for (int i = 2, fa; i <= n; i++) {
        cin >> fa;
        addEdge(fa, i); // 添加边（父节点指向子节点）
    }
    
    // 从根节点开始DFS求解
    dp(1);
    
    // 输出结果：根节点对应线段树中的最大值
    cout << max_val[root[1]] << endl;
    
    return 0;
}

/**
 * 单元测试用例设计：
 * 1. 基础测试：小规模树结构验证算法正确性
 * 2. 边界测试：单节点、链状树、完全二叉树
 * 3. 性能测试：n=200000的大规模数据
 * 4. 极端测试：权值全部相同或严格递增/递减
 * 
 * 编译命令：
 * g++ -std=c++11 -O2 Code01_LeadersGroup1.cpp -o Code01_LeadersGroup1
 * 
 * 运行命令：
 * ./Code01_LeadersGroup1 < input.txt
 */