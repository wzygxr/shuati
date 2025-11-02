/**
 * POJ 2761 Feed the dogs
 * 
 * 题目来源: POJ 2761
 * 题目链接: http://poj.org/problem?id=2761
 * 
 * 题目描述:
 * Wind非常喜欢漂亮的狗，她有n只宠物狗。所以Jiajia必须每天喂狗给Wind。
 * Jiajia爱Wind，但不爱狗，所以Jiajia用一种特殊的方式喂狗。
 * 午餐时间，狗会站在一条线上，从1到n编号，最左边的是1，第二个是2，以此类推。
 * 在每次喂食中，Jiajia选择一个区间[i,j]，选择第k个漂亮的狗来喂食。
 * 当然，Jiajia有自己决定每只狗漂亮值的方法。
 * 应该注意的是，Jiajia不想让任何位置被喂得太多，因为这可能会导致狗的死亡。
 * 如果这样，Wind会生气，后果会很严重。因此，任何喂食区间都不会完全包含另一个区间，尽管区间可能相互交叉。
 * 
 * 解题思路:
 * 使用可持久化线段树（主席树）解决区间第K小问题。
 * 1. 对数值进行离散化处理
 * 2. 建立可持久化线段树，每个位置对应一个版本
 * 3. 对于每个查询，在对应区间的线段树版本中查询第K小的值
 * 
 * 时间复杂度: O((n + m) log n)
 * 空间复杂度: O(n log n)
 * 
 * 约束条件:
 * n < 100001
 * m < 50001
 * 
 * 示例:
 * 输入:
 * 7 2
 * 1 5 2 6 3 7 4
 * 1 5 3
 * 2 7 1
 * 
 * 输出:
 * 3
 * 2
 */

const int MAXN = 100010;

// 原始数据
int arr[MAXN];
int sortedArr[MAXN];

// 离散化相关
int values[MAXN];

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
 * 查询区间第k小
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

int main() {
    // 读取n和m
    // int n, m;
    // n = 0;
    // m = 0;
    // 模拟输入读取
    // 实际使用时需要根据具体环境调整输入方式
    
    // 初始化数组
    // for (int i = 1; i <= n; i++) {
    //     arr[i] = 0;  // 实际使用时需要读取输入
    //     sortedArr[i] = arr[i];
    // }
    
    // 离散化处理
    // 排序去重等操作
    
    // 构建初始线段树
    // root[0] = build(1, n);
    
    // 逐个插入元素，构建可持久化线段树
    // for (int i = 1; i <= n; i++) {
    //     int pos = /* 二分查找离散化后的索引 */;
    //     root[i] = insert(pos, 1, n, root[i-1]);
    // }
    
    // 处理查询
    // for (int i = 1; i <= m; i++) {
    //     int l, r, k;
    //     // 实际使用时需要读取l, r, k
    //     
    //     // 查询区间[l,r]第k小的数
    //     int pos = query(k, 1, n, root[l-1], root[r]);
    //     // 实际使用时需要输出结果
    // }
    
    return 0;
}