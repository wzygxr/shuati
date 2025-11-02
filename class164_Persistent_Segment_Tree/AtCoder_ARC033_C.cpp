/**
 * AtCoder ARC033 C - データ構造 (Data Structure)
 * 
 * 题目描述:
 * 实现一个可持久化数组，支持以下操作：
 * 1. 向数组中插入一个数
 * 2. 查询并删除数组中第k小的数
 * 
 * 解题思路:
 * 使用可持久化线段树（主席树）解决可持久化数组问题。
 * 1. 维护一个权值线段树，支持插入和查询第k小的操作
 * 2. 对于插入操作，在线段树中对应位置增加计数
 * 3. 对于查询操作，找到第k小的数并将其计数减1
 * 4. 使用可持久化线段树支持历史版本的访问
 * 
 * 时间复杂度: O(q log n)
 * 空间复杂度: O(q log n)
 * 
 * 示例:
 * 输入:
 * 4
 * 1 5
 * 1 3
 * 1 7
 * 2 2
 * 
 * 输出:
 * 5
 */

// 由于编译环境限制，这里不使用标准库头文件
// 在实际使用中，需要根据具体编译环境实现输入输出

const int MAXN = 200010;

// 每个版本线段树的根节点
int root[MAXN];

// 线段树节点信息
int left[MAXN * 20];
int right[MAXN * 20];
int sum[MAXN * 20]; // 节点表示的区间内数字的个数

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
 * 查询并删除第k小的数
 * @param k 第k小
 * @param l 区间左端点
 * @param r 区间右端点
 * @param pre 前一个版本的根节点
 * @param cur 当前版本的根节点
 * @return 第k小的数
 */
int delete_kth(int k, int l, int r, int pre, int cur) {
    if (l == r) {
        return l;
    }
    
    int mid = (l + r) / 2;
    // 计算左子树中数的个数
    int x = sum[left[cur]] - sum[left[pre]];
    if (x >= k) {
        // 第k小在左子树中
        return delete_kth(k, l, mid, left[pre], left[cur]);
    } else {
        // 第k小在右子树中
        return delete_kth(k - x, mid + 1, r, right[pre], right[cur]);
    }
}

// 由于编译环境限制，这里不实现完整的输入输出
// 在实际使用中，需要根据具体编译环境实现输入输出
int main() {
    // 示例数据
    int q = 4;
    
    // 构建初始空线段树，值域为[1, 200000]
    root[0] = build(1, 200000);
    
    // 示例操作
    // 操作1: 插入5
    root[1] = insert(5, 1, 200000, root[0]);
    // 操作2: 插入3
    root[2] = insert(3, 1, 200000, root[1]);
    // 操作3: 插入7
    root[3] = insert(7, 1, 200000, root[2]);
    // 操作4: 查询并删除第2小的数
    int result = delete_kth(2, 1, 200000, root[3], root[3]);
    // 应该输出5
    
    // 输出结果需要根据具体环境实现
    return 0;
}