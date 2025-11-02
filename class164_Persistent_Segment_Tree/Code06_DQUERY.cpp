/**
 * SPOJ DQUERY - D-query
 * 
 * 题目描述:
 * 给定一个长度为N的序列，进行Q次查询，每次查询区间[l,r]中不同数字的个数。
 * 
 * 解题思路:
 * 使用可持久化线段树解决区间不同元素个数问题。
 * 1. 对于每个位置i，记录上一次出现相同数字的位置last[i]
 * 2. 对于每个位置i，建立线段树，将位置i处的值设为1，位置last[i]处的值设为0
 * 3. 查询区间[l,r]时，查询第r个版本的线段树在区间[l,r]上的和
 * 
 * 时间复杂度: O((n + q) log n)
 * 空间复杂度: O(n log n)
 * 
 * 示例:
 * 输入:
 * 5
 * 1 1 2 1 3
 * 3
 * 1 5
 * 2 4
 * 3 5
 * 
 * 输出:
 * 3
 * 2
 * 3
 */

// 由于编译环境限制，这里不使用标准库头文件
// 在实际使用中，需要根据具体编译环境实现输入输出

const int MAXN = 30010;

// 原始数组
int arr[MAXN];
// 记录每个数字上一次出现的位置
int last[1000010];  // 假设值域不超过1000000
// 每个版本线段树的根节点
int root[MAXN];

// 线段树节点信息
int left[MAXN * 50];
int right[MAXN * 50];
int sum[MAXN * 50];

// 线段树节点计数器
int cnt = 0;

/**
 * 构建空线段树
 * @param l 区间左端点
 * @param r 区间右端点
 * @return 根节点编号
 */
int build(int l, int r) {
    int rt = ++cnt;
    sum[rt] = 0;
    if (l < r) {
        int mid = (l + r) / 2;
        left[rt] = build(l, mid);
        right[rt] = build(mid + 1, r);
    }
    return rt;
}

/**
 * 更新线段树中的一个位置
 * @param pos 要更新的位置
 * @param val 更新的值
 * @param l 区间左端点
 * @param r 区间右端点
 * @param pre 前一个版本的节点编号
 * @return 新节点编号
 */
int update(int pos, int val, int l, int r, int pre) {
    int rt = ++cnt;
    left[rt] = left[pre];
    right[rt] = right[pre];
    
    if (l == r) {
        sum[rt] = val;
        return rt;
    }
    
    int mid = (l + r) / 2;
    if (pos <= mid) {
        left[rt] = update(pos, val, l, mid, left[rt]);
    } else {
        right[rt] = update(pos, val, mid + 1, r, right[rt]);
    }
    sum[rt] = sum[left[rt]] + sum[right[rt]];
    return rt;
}

/**
 * 查询区间和
 * @param L 查询区间左端点
 * @param R 查询区间右端点
 * @param l 当前区间左端点
 * @param r 当前区间右端点
 * @param rt 当前节点编号
 * @return 区间和
 */
int query(int L, int R, int l, int r, int rt) {
    if (L <= l && r <= R) {
        return sum[rt];
    }
    
    int mid = (l + r) / 2;
    int ans = 0;
    if (L <= mid) ans += query(L, R, l, mid, left[rt]);
    if (R > mid) ans += query(L, R, mid + 1, r, right[rt]);
    return ans;
}

// 由于编译环境限制，这里不实现完整的输入输出
// 在实际使用中，需要根据具体编译环境实现输入输出
int main() {
    // 示例数据
    int n = 5;
    arr[1] = 1; arr[2] = 1; arr[3] = 2; arr[4] = 1; arr[5] = 3;
    
    // 初始化last数组
    for (int i = 0; i < 1000010; i++) {
        last[i] = 0;
    }
    
    // 构建空线段树
    root[0] = build(1, n);
    
    // 构建主席树
    for (int i = 1; i <= n; i++) {
        int val = arr[i];
        // 先将当前位置设为1
        root[i] = update(i, 1, 1, n, root[i - 1]);
        // 如果这个数字之前出现过，将之前位置设为0
        if (last[val] > 0) {
            int pos = last[val];
            root[i] = update(pos, 0, 1, n, root[i]);
        }
        last[val] = i;
    }
    
    // 示例查询
    int q = 3;
    int queries[3][2] = {{1, 5}, {2, 4}, {3, 5}};
    
    // 处理查询
    for (int i = 0; i < q; i++) {
        int l = queries[i][0];
        int r = queries[i][1];
        int ans = query(l, r, 1, n, root[r]);
        // 输出结果需要根据具体环境实现
    }
    
    return 0;
}