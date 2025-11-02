// POJ 2104 K-th Number - C++实现
// 题目来源：http://poj.org/problem?id=2104
// 题目描述：给定一个长度为n的数组，有m个查询，每个查询要求在指定区间内找到第k小的数
//
// 解题思路：使用整体二分算法，将所有查询一起处理，二分答案的值域，利用树状数组维护区间内小于等于mid的元素个数
// 时间复杂度：O((N+Q) * logN * log(maxValue))
// 空间复杂度：O(N + Q)
//
// 算法适用条件：
// 1. 询问的答案具有可二分性
// 2. 修改对判定答案的贡献互相独立
// 3. 修改如果对判定答案有贡献，则贡献为确定值
// 4. 贡献满足交换律、结合律，具有可加性
// 5. 题目允许离线操作
//
// 工程化考量：
// - 数据结构选择：使用树状数组实现前缀和查询，时间复杂度为O(logN)
// - 内存优化：预分配数组空间，避免动态内存分配的开销
// - 性能优化：使用离散化将大值域映射到小区间，减少计算量
// - 异常处理：处理数组边界和极端情况

#include <iostream>
#include <algorithm>
#include <vector>
#include <cstdio>
using namespace std;

const int MAXN = 100001;
int n, m; // n:数组长度, m:查询次数

// 原始数组，存储输入的数值
int arr[MAXN];

// 离散化后的数组，用于将大值域映射到小下标范围
int sorted[MAXN];

// 查询信息存储
struct Query {
    int l, r, k, id;
} queries[MAXN];

// 树状数组，用于维护当前值域范围内元素的个数
int tree[MAXN];

// 整体二分中用于分类查询的临时存储
int lset[MAXN]; // 满足条件的查询（答案在左半部分）
int rset[MAXN]; // 不满足条件的查询（答案在右半部分）

// 查询的答案存储数组
int ans[MAXN];

/**
 * 计算一个数的lowbit值
 * 功能：返回二进制表示中最低位的1所代表的数值
 * 例如：lowbit(6) = lowbit(110) = 2
 * 时间复杂度：O(1)
 */
int lowbit(int i) {
    return i & -i;
}

/**
 * 在树状数组中给位置i增加v
 * 功能：更新树状数组中的值，用于后续前缀和查询
 * 时间复杂度：O(logN)
 */
void add(int i, int v) {
    while (i <= n) {
        tree[i] += v;
        i += lowbit(i);
    }
}

/**
 * 计算前缀和[1..i]
 * 功能：计算从1到i的元素和
 * 时间复杂度：O(logN)
 */
int sum(int i) {
    int ret = 0;
    while (i > 0) {
        ret += tree[i];
        i -= lowbit(i);
    }
    return ret;
}

/**
 * 计算区间和[l..r]
 * 功能：计算从l到r的元素和
 * 时间复杂度：O(logN)
 */
int query(int l, int r) {
    return sum(r) - sum(l - 1);
}

/**
 * 整体二分核心函数
 * 功能：递归地对值域进行二分，并将查询分类处理
 * @param ql 查询范围的左端点
 * @param qr 查询范围的右端点
 * @param vl 值域范围的左端点（离散化后的下标）
 * @param vr 值域范围的右端点（离散化后的下标）
 * 时间复杂度：O(log(maxValue))
 */
void compute(int ql, int qr, int vl, int vr) {
    // 递归边界1：没有查询需要处理
    if (ql > qr) {
        return;
    }
    
    // 递归边界2：如果值域范围只有一个值，说明找到了答案
    if (vl == vr) {
        for (int i = ql; i <= qr; i++) {
            ans[queries[i].id] = sorted[vl];
        }
        return;
    }
    
    // 二分中点，将值域划分为左右两部分
    int mid = (vl + vr) >> 1;
    
    // 预处理：为树状数组添加贡献
    // 记录添加的位置，用于后续撤销操作
    vector<int> positions;
    for (int j = 1; j <= n; j++) {
        if (arr[j] <= sorted[mid]) {
            add(j, 1);
            positions.push_back(j);
        }
    }
    
    // 检查每个查询，根据满足条件的元素个数划分到左右区间
    int lsiz = 0, rsiz = 0;
    for (int i = ql; i <= qr; i++) {
        // 查询区间[queries[i].l, queries[i].r]中值小于等于sorted[mid]的元素个数
        int satisfy = query(queries[i].l, queries[i].r);
        
        if (satisfy >= queries[i].k) {
            // 说明第k小的数在左半部分值域
            lset[++lsiz] = i;
        } else {
            // 说明第k小的数在右半部分值域，需要在右半部分找第(k-satisfy)小的数
            queries[i].k -= satisfy;
            rset[++rsiz] = i;
        }
    }
    
    // 撤销对树状数组的修改，恢复到处理前的状态
    for (int pos : positions) {
        add(pos, -1);
    }
    
    // 保存当前查询信息的临时数组
    Query temp[MAXN];
    for (int i = ql; i <= qr; i++) {
        temp[i] = queries[i];
    }
    
    // 重新排列查询顺序，使得左集合的查询在前，右集合的查询在后
    for (int i = 1; i <= lsiz; i++) {
        queries[ql + i - 1] = temp[lset[i]];
    }
    for (int i = 1; i <= rsiz; i++) {
        queries[ql + lsiz + i - 1] = temp[rset[i]];
    }
    
    // 递归处理左右两部分
    // 左半部分：值域在[vl, mid]范围内的查询
    compute(ql, ql + lsiz - 1, vl, mid);
    // 右半部分：值域在[mid+1, vr]范围内的查询
    compute(ql + lsiz, qr, mid + 1, vr);
}

/**
 * 主函数，处理输入输出并调用整体二分算法
 * 工程化特点：
 * - 关闭同步提高输入输出效率
 * - 进行离散化处理减少计算量
 * - 合理的数据结构选择
 */
int main() {
    // 关闭同步提高输入输出效率
    ios::sync_with_stdio(false);
    cin.tie(0);
    
    // 读取数组长度和查询次数
    cin >> n >> m;
    
    // 读取原始数组
    for (int i = 1; i <= n; i++) {
        cin >> arr[i];
        sorted[i] = arr[i];
    }
    
    // 读取查询
    for (int i = 1; i <= m; i++) {
        cin >> queries[i].l >> queries[i].r >> queries[i].k;
        queries[i].id = i;
    }
    
    // 离散化：将大值域映射到小下标范围，减少二分的值域范围
    sort(sorted + 1, sorted + n + 1);
    int uniqueCount = 1;
    for (int i = 2; i <= n; i++) {
        if (sorted[i] != sorted[i - 1]) {
            sorted[++uniqueCount] = sorted[i];
        }
    }
    
    // 整体二分求解
    // 初始查询范围[1, m]，初始值域范围[1, uniqueCount]
    compute(1, m, 1, uniqueCount);
    
    // 输出结果
    for (int i = 1; i <= m; i++) {
        cout << ans[i] << "\n";
    }
    
    return 0;
}