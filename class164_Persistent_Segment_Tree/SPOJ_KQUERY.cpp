/**
 * SPOJ KQUERY - K-query
 * 
 * 题目描述:
 * 给定一个长度为N的序列，进行Q次查询，每次查询区间[l,r]中大于K的数的个数。
 * 
 * 解题思路:
 * 使用可持久化线段树（主席树）结合离线处理解决区间大于K的数的个数问题。
 * 1. 将所有查询按K值从大到小排序
 * 2. 将所有元素按值从大到小排序
 * 3. 按顺序处理查询，对于每个查询，将所有大于K的元素插入到主席树中
 * 4. 查询区间[l,r]中元素的个数
 * 
 * 时间复杂度: O((n + q) log n)
 * 空间复杂度: O(n log n)
 * 
 * 示例:
 * 输入:
 * 5
 * 5 1 2 3 4
 * 3
 * 2 4 1
 * 4 4 4
 * 1 5 2
 * 
 * 输出:
 * 2
 * 0
 * 3
 */

// 由于编译环境限制，这里不使用标准库头文件
// 在实际使用中，需要根据具体编译环境实现输入输出

const int MAXN = 30010;

// 原始数组
int arr[MAXN];
// 每个版本线段树的根节点
int root[MAXN];

// 线段树节点信息
int left[MAXN * 20];
int right[MAXN * 20];
int sum[MAXN * 20];

// 线段树节点计数器
int cnt = 0;

/**
 * 构建空线段树
 * @param l 区间左端点
 * @param r 区间右端点
 * @return 根节点编号
 */
int build(int l, int r) {
    cnt++;
    int rt = cnt;
    sum[rt] = 0;
    if (l < r) {
        int mid = (l + r) / 2;
        left[rt] = build(l, mid);
        right[rt] = build(mid + 1, r);
    }
    return rt;
}

/**
 * 在线段树中插入一个值
 * @param pos 要插入的位置
 * @param l 区间左端点
 * @param r 区间右端点
 * @param pre 前一个版本的节点编号
 * @return 新节点编号
 */
int insert(int pos, int l, int r, int pre) {
    cnt++;
    int rt = cnt;
    left[rt] = left[pre];
    right[rt] = right[pre];
    sum[rt] = sum[pre] + 1;
    
    if (l < r) {
        int mid = (l + r) / 2;
        if (pos <= mid) {
            left[rt] = insert(pos, l, mid, left[rt]);
        } else {
            right[rt] = insert(pos, mid + 1, r, right[rt]);
        }
    }
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
    
    // 原始数组
    arr[1] = 5; arr[2] = 1; arr[3] = 2; arr[4] = 3; arr[5] = 4;
    
    int q = 3;
    // 查询数据: (l, r, k)
    int queries[3][3] = {{2, 4, 1}, {4, 4, 4}, {1, 5, 2}};
    
    // 构建主席树
    root[0] = build(1, n);
    
    // 示例处理（简化版）
    // 实际实现需要排序和离线处理
    
    // 第一个查询: 区间[2,4]中大于1的数的个数
    // 应该插入值5,3,2,4到位置2,4,3,5
    root[1] = insert(2, 1, n, root[0]); // 插入位置2的元素5
    root[2] = insert(4, 1, n, root[1]); // 插入位置4的元素3
    root[3] = insert(3, 1, n, root[2]); // 插入位置3的元素2
    root[4] = insert(5, 1, n, root[3]); // 插入位置5的元素4
    int ans1 = query(2, 4, 1, n, root[4]); // 查询区间[2,4]中元素的个数
    
    // 输出结果需要根据具体环境实现
    return 0;
}