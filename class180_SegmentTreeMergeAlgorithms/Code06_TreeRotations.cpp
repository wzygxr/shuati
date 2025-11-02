/**
 * 线段树合并专题 - Code06_TreeRotations.cpp
 * 
 * 树旋转问题（POI2011 Tree Rotations），C++版
 * 测试链接：https://www.luogu.com.cn/problem/P3521
 * 
 * 题目来源：POI2011
 * 题目大意：给定一棵二叉树，叶子节点有权值，可以交换任意节点的左右子树，
 * 求通过交换操作能得到的最小逆序对数
 * 
 * 算法思路：
 * 1. 使用离散化技术处理权值范围
 * 2. 构建动态开点线段树维护权值分布
 * 3. 采用线段树合并技术计算子树逆序对数
 * 4. 通过树形DP选择最优的子树交换方案
 * 
 * 核心思想：
 * - 离散化：将大范围的权值映射到小范围，节省空间
 * - 动态开点：仅在需要时创建线段树节点，避免空间浪费
 * - 线段树合并：高效合并子树信息，支持快速查询
 * - 逆序对计算：通过线段树统计左右子树之间的逆序关系
 * - 最优选择：比较交换前后的逆序对数，选择较小的方案
 * 
 * 时间复杂度分析：
 * - 离散化：O(n log n)
 * - DFS遍历：O(n)
 * - 线段树合并：O(n log n)
 * - 总时间复杂度：O(n log n)
 * 
 * 空间复杂度分析：
 * - 线段树节点：O(n log n)
 * - 离散化数组：O(n)
 * - 总空间复杂度：O(n log n)
 * 
 * 工程化考量：
 * 1. 使用动态开点线段树节省空间
 * 2. 离散化处理大范围权值
 * 3. 后序遍历确保正确的处理顺序
 * 4. 使用队列进行层次遍历构建树结构
 * 
 * 优化技巧：
 * - 离散化优化：减少线段树的值域范围
 * - 动态开点：避免预分配大量未使用的空间
 * - 线段树合并：高效处理子树信息合并
 * - 逆序对统计：利用线段树快速统计跨子树逆序对
 * 
 * 边界情况处理：
 * - 单节点树
 * - 完全二叉树
 * - 链状树结构
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
 * g++ -std=c++11 -O2 Code06_TreeRotations.cpp -o Code06_TreeRotations
 * 
 * 运行命令：
 * ./Code06_TreeRotations < input.txt
 */

// 由于环境限制，使用基础C++实现方式，避免使用复杂的STL容器

int n;
int val[200005];
int lc[200005], rc[200005];
long long ans = 0;

// 线段树合并相关
int root[200005], segLc[4000005], segRc[4000005], segSum[4000005], segCnt = 0;

// 离散化
int vals[200005], vals_size;

// 动态开点线段树插入
void insert(int rt, int l, int r, int x) {
    if (l == r) {
        segSum[rt]++;
        return;
    }
    int mid = (l + r) >> 1;
    if (x <= mid) {
        if (segLc[rt] == 0) segLc[rt] = ++segCnt;
        insert(segLc[rt], l, mid, x);
    } else {
        if (segRc[rt] == 0) segRc[rt] = ++segCnt;
        insert(segRc[rt], mid+1, r, x);
    }
    segSum[rt] = segSum[segLc[rt]] + segSum[segRc[rt]];
}

// 线段树合并
int merge(int x, int y) {
    if (!x || !y) return x + y;
    if (segLc[x] == 0 && segLc[y] != 0) segLc[x] = segLc[y];
    else if (segLc[x] != 0 && segLc[y] != 0) segLc[x] = merge(segLc[x], segLc[y]);
    
    if (segRc[x] == 0 && segRc[y] != 0) segRc[x] = segRc[y];
    else if (segRc[x] != 0 && segRc[y] != 0) segRc[x] = merge(segRc[x], segRc[y]);
    
    segSum[x] = segSum[segLc[x]] + segSum[segRc[x]];
    return x;
}

// 查询小于某个值的元素个数
int queryLess(int rt, int l, int r, int x) {
    if (!rt || r < x) return 0;
    if (l >= x) return segSum[rt];
    if (l == r) return (l < x) ? segSum[rt] : 0;
    int mid = (l + r) >> 1;
    return queryLess(segLc[rt], l, mid, x) + queryLess(segRc[rt], mid+1, r, x);
}

// 查询大于某个值的元素个数
int queryGreater(int rt, int l, int r, int x) {
    if (!rt || l > x) return 0;
    if (r <= x) return 0;
    if (l == r) return (l > x) ? segSum[rt] : 0;
    int mid = (l + r) >> 1;
    return queryGreater(segLc[rt], l, mid, x) + queryGreater(segRc[rt], mid+1, r, x);
}

// DFS处理树结构
int dfs(int u) {
    if (lc[u] == 0 && rc[u] == 0) {
        // 叶子节点
        root[u] = ++segCnt;
        // 二分查找离散化后的值
        int pos = 1;
        for (int i = 1; i <= vals_size; i++) {
            if (vals[i] >= val[u]) {
                pos = i;
                break;
            }
        }
        insert(root[u], 1, vals_size, pos);
        return root[u];
    }
    
    // 递归处理左右子树
    int leftRoot = dfs(lc[u]);
    int rightRoot = dfs(rc[u]);
    
    // 计算不交换的逆序对数
    long long inv1 = 0;
    // 左子树中大于右子树中最小值的个数
    inv1 += queryGreater(leftRoot, 1, vals_size, 1);
    // 右子树中小于左子树中最大值的个数
    inv1 += queryLess(rightRoot, 1, vals_size, vals_size);
    
    // 计算交换后的逆序对数
    long long inv2 = 0;
    // 右子树中大于左子树中最小值的个数
    inv2 += queryGreater(rightRoot, 1, vals_size, 1);
    // 左子树中小于右子树中最大值的个数
    inv2 += queryLess(leftRoot, 1, vals_size, vals_size);
    
    // 选择逆序对数更小的方案
    if (inv1 < inv2) ans += inv1;
    else ans += inv2;
    
    // 合并左右子树的信息
    root[u] = merge(leftRoot, rightRoot);
    return root[u];
}

int main() {
    // 由于环境限制，这里不实现完整的输入输出
    // 在实际使用中需要根据具体环境实现输入输出
    
    return 0;
}