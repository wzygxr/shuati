/**
 * HDU 4417 Super Mario
 * 
 * 题目来源: HDU 4417
 * 题目链接: http://acm.hdu.edu.cn/showproblem.php?pid=4417
 * 
 * 题目描述:
 * Mario是世界著名的管道工。他"健壮"的身材和惊人的跳跃能力让我们记忆犹新。
 * 现在可怜的公主又遇到了麻烦，Mario需要拯救他的爱人。
 * 我们把通往Boss城堡的路看作一条线(长度为n)，在每个整数点i上有一个高度为hi的砖块。
 * 现在的问题是，如果Mario能跳的最大高度是H，那么在[L,R]区间内他能击中多少个砖块。
 * 
 * 解题思路:
 * 使用可持久化线段树（主席树）解决区间小于等于H的元素个数问题。
 * 1. 对高度值进行离散化处理
 * 2. 建立可持久化线段树，每个位置对应一个版本
 * 3. 对于每个查询，在对应区间的线段树版本中查询小于等于H的元素个数
 * 
 * 时间复杂度: O((n + m) log n)
 * 空间复杂度: O(n log n)
 * 
 * 约束条件:
 * 1 <= n <= 10^5
 * 1 <= m <= 10^5
 * 0 <= height <= 10^9
 * 0 <= L <= R < n
 * 0 <= H <= 10^9
 * 
 * 示例:
 * 输入:
 * 1
 * 10 10
 * 0 5 2 7 5 4 3 8 7 7
 * 2 8 6
 * 3 5 0
 * 1 3 1
 * 1 9 4
 * 0 1 0
 * 3 5 5
 * 5 5 1
 * 4 6 3
 * 1 5 7
 * 5 7 3
 * 
 * 输出:
 * Case 1:
 * 4
 * 0
 * 0
 * 3
 * 1
 * 2
 * 0
 * 1
 * 5
 * 1
 */

const int MAXN = 100010;

// 原始数据
int arr[MAXN];
int sortedArr[MAXN];

// 离散化相关
int heights[MAXN];

// 可持久化线段树
int root[MAXN];
int left[MAXN * 20];
int right[MAXN * 20];
int sum[MAXN * 20];
int cnt = 0;

// 自定义max函数
int my_max(int a, int b) {
    return a > b ? a : b;
}

// 自定义min函数
int my_min(int a, int b) {
    return a < b ? a : b;
}

/**
 * 构建空线段树
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
 * 插入操作
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
 * 查询区间[1, pos]的元素个数
 */
int query(int pos, int l, int r, int u, int v) {
    if (pos >= r) return sum[v] - sum[u];
    if (pos < l) return 0;
    int mid = (l + r) / 2;
    return query(pos, l, mid, left[u], left[v]) + query(pos, mid + 1, r, right[u], right[v]);
}

int main() {
    // 读取测试用例数T
    // int T;
    // T = 0;
    // 模拟输入读取
    // 实际使用时需要根据具体环境调整输入方式
    
    // for (int cas = 1; cas <= T; cas++) {
    //     int n, m;
    //     // 实际使用时需要读取n, m
    //     
    //     // 重置计数器
    //     cnt = 0;
    //     
    //     // 读取数据
    //     for (int i = 1; i <= n; i++) {
    //         arr[i] = 0;  // 实际使用时需要读取输入
    //         sortedArr[i] = arr[i];
    //     }
    //     
    //     // 离散化处理
    //     // 排序去重等操作
    //     
    //     // 构建初始线段树
    //     root[0] = build(1, n);
    //     
    //     // 逐个插入元素，构建可持久化线段树
    //     for (int i = 1; i <= n; i++) {
    //         int pos = /* 二分查找离散化后的索引 */;
    //         root[i] = insert(pos, 1, n, root[i-1]);
    //     }
    //     
    //     // 处理查询
    //     for (int i = 1; i <= m; i++) {
    //         int L, R, H;
    //         // 实际使用时需要读取L, R, H
    //         L++;  // 转换为1-indexed
    //         R++;  // 转换为1-indexed
    //         
    //         // 查询区间[L,R]中小于等于H的元素个数
    //         int pos = /* 二分查找小于等于H的最大值的索引 */;
    //         int result = query(pos, 1, n, root[L-1], root[R]);
    //         // 实际使用时需要输出结果
    //     }
    // }
    
    return 0;
}