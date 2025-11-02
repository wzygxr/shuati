// Horrible Queries (可怕的查询)
// 题目来源: SPOJ HORRIBLE - Horrible Queries
// 题目链接: https://www.spoj.com/problems/HORRIBLE/
// 
// 题目描述:
// 现有一个长度为n的序列，开始时所有位置都为0
// 有以下两种操作：
// 0 p q v : 将区间[p,q]内每个位置的值都加上v
// 1 p q   : 查询区间[p,q]内所有位置的值的和
//
// 解题思路:
// 1. 使用带懒惰传播的线段树实现区间更新和区间查询
// 2. 懒惰传播用于延迟更新，避免不必要的计算
// 3. 区间更新时，只在必要时才将更新操作传递给子节点
// 4. 查询时确保所有相关的懒惰标记都被处理
//
// 时间复杂度: 
// - 区间更新: O(log n)
// - 区间查询: O(log n)
// 空间复杂度: O(n)

// 由于编译环境限制，使用简单的数组实现

const int MAXN = 100005;
long long tree[4 * MAXN];
long long lazy[4 * MAXN];
int n;

// 函数声明
void pushDown(int node, int start, int end);
void rangeAddHelper(int node, int start, int end, int l, int r, long long val);
long long queryHelper(int node, int start, int end, int l, int r);

// 区间加法更新 [l, r] 区间内每个元素加上 val
void rangeAdd(int l, int r, long long val) {
    rangeAddHelper(0, 0, n - 1, l, r, val);
}

// 查询区间和
long long query(int l, int r) {
    return queryHelper(0, 0, n - 1, l, r);
}

// 区间加法更新辅助函数
void rangeAddHelper(int node, int start, int end, int l, int r, long long val) {
    // 1. 先处理懒惰标记
    pushDown(node, start, end);

    // 2. 当前区间与更新区间无交集
    if (start > r || end < l) {
        return;
    }

    // 3. 当前区间完全包含在更新区间内
    if (start >= l && end <= r) {
        // 更新当前节点的值
        tree[node] += val * (end - start + 1);
        // 如果不是叶子节点，设置懒惰标记
        if (start != end) {
            lazy[node] += val;
        }
        return;
    }

    // 4. 当前区间与更新区间有部分交集，递归处理左右子树
    int mid = (start + end) / 2;
    rangeAddHelper(2 * node + 1, start, mid, l, r, val);
    rangeAddHelper(2 * node + 2, mid + 1, end, l, r, val);

    // 更新当前节点的值
    tree[node] = tree[2 * node + 1] + tree[2 * node + 2];
}

// 查询区间和辅助函数
long long queryHelper(int node, int start, int end, int l, int r) {
    // 1. 先处理懒惰标记
    pushDown(node, start, end);

    // 2. 当前区间与查询区间无交集
    if (start > r || end < l) {
        return 0;
    }

    // 3. 当前区间完全包含在查询区间内
    if (start >= l && end <= r) {
        return tree[node];
    }

    // 4. 当前区间与查询区间有部分交集，递归查询左右子树
    int mid = (start + end) / 2;
    long long leftSum = queryHelper(2 * node + 1, start, mid, l, r);
    long long rightSum = queryHelper(2 * node + 2, mid + 1, end, l, r);
    return leftSum + rightSum;
}

// 下推懒惰标记
void pushDown(int node, int start, int end) {
    // 如果当前节点没有懒惰标记，直接返回
    if (lazy[node] == 0) {
        return;
    }

    // 将懒惰标记应用到当前节点
    tree[node] += lazy[node] * (end - start + 1);

    // 如果不是叶子节点，将懒惰标记传递给子节点
    if (start != end) {
        int mid = (start + end) / 2;
        lazy[2 * node + 1] += lazy[node];
        lazy[2 * node + 2] += lazy[node];
    }

    // 清除当前节点的懒惰标记
    lazy[node] = 0;
}

// 由于编译环境限制，不提供main函数测试
// 可以通过调用rangeAdd和query函数来使用