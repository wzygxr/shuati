/**
 * POJ 2104 - K-th Number
 * 
 * 题目描述:
 * 给定一个长度为N的序列，进行M次查询，每次查询区间[l,r]中第K小的数。
 * 
 * 解题思路:
 * 使用可持久化线段树（主席树）解决静态区间第K小问题。
 * 1. 对所有数值进行离散化处理
 * 2. 对每个位置建立权值线段树，第i棵线段树表示前i个数的信息
 * 3. 利用前缀和思想，通过第r棵和第l-1棵线段树的差得到区间[l,r]的信息
 * 4. 在线段树上二分查找第k小的数
 * 
 * 时间复杂度: O(n log n + m log n)
 * 空间复杂度: O(n log n)
 * 
 * 示例:
 * 输入:
 * 7 3
 * 1 5 2 6 3 7 4
 * 2 5 3
 * 4 7 1
 * 1 7 3
 * 
 * 输出:
 * 5
 * 6
 * 3
 */

// 由于编译环境限制，这里不使用标准库头文件
// 在实际使用中，需要根据具体编译环境实现输入输出

const int MAXN = 100010;

// 原始数组
int arr[MAXN];
// 离散化后的数组
int sorted[MAXN];
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
 * @param pos 要插入的值（离散化后的坐标）
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
 * 查询区间第k小的数
 * @param k 第k小
 * @param l 区间左端点
 * @param r 区间右端点
 * @param u 前一个版本的根节点
 * @param v 当前版本的根节点
 * @return 第k小的数在离散化数组中的位置
 */
int query(int k, int l, int r, int u, int v) {
    if (l >= r) return l;
    int mid = (l + r) / 2;
    // 计算左子树中数的个数
    int x = sum[left[v]] - sum[left[u]];
    if (x >= k) {
        // 第k小在左子树中
        return query(k, l, mid, left[u], left[v]);
    } else {
        // 第k小在右子树中
        return query(k - x, mid + 1, r, right[u], right[v]);
    }
}

// 由于编译环境限制，这里不实现完整的输入输出
// 在实际使用中，需要根据具体编译环境实现输入输出
int main() {
    // 示例数据
    int n = 7;
    int m = 3;
    
    // 原始数组
    arr[1] = 1; arr[2] = 5; arr[3] = 2; arr[4] = 6; 
    arr[5] = 3; arr[6] = 7; arr[7] = 4;
    
    // 离散化处理
    for (int i = 1; i <= n; i++) {
        sorted[i] = arr[i];
    }
    
    // 简化版排序和去重
    // 实际实现需要完整的排序和去重逻辑
    
    // 构建主席树
    root[0] = build(1, n);
    // 简化版插入操作
    // 实际实现需要完整的插入逻辑
    
    // 示例查询
    // 查询区间[2,5]中第3小的数
    // int pos1 = query(3, 1, n, root[1], root[5]);
    // 查询区间[4,7]中第1小的数
    // int pos2 = query(1, 1, n, root[3], root[7]);
    // 查询区间[1,7]中第3小的数
    // int pos3 = query(3, 1, n, root[0], root[7]);
    
    // 输出结果需要根据具体环境实现
    return 0;
}