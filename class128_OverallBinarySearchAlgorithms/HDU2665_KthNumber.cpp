// HDU 2665 Kth Number - 优化版C++实现
// 题目来源：http://acm.hdu.edu.cn/showproblem.php?pid=2665
// 题目描述：给定一个长度为n的数组，有m个查询，每个查询要求在指定区间内找到第k小的数
//
// 解题思路：使用整体二分算法，将所有查询一起处理，二分答案的值域，利用树状数组维护区间内小于等于mid的元素个数
// 时间复杂度：O((N+Q) * logN * log(maxValue))
// 空间复杂度：O(N + Q)
//
// 算法适用场景：
// 1. 需要处理大量区间第k小查询
// 2. 数据是静态的（不需要实时更新）
// 3. 数据范围较大，需要离散化处理
//
// 工程化优化点：
// 1. 关闭同步提高输入输出效率
// 2. 优化树状数组元素添加/移除逻辑，使用位置跟踪避免双重循环
// 3. 高效的离散化处理
// 4. 多组测试用例支持

#include <iostream>
#include <algorithm>
#include <vector>
#include <cstdio>
#include <cstring>
using namespace std;

const int MAXN = 100001;
int n, m; // n:数组长度, m:查询次数

// 原始数组，存储输入的数值
int arr[MAXN];

// 离散化后的数组，用于将大值域映射到小区间
int sorted[MAXN];

// 查询信息存储
int queryL[MAXN]; // 查询区间左端点
int queryR[MAXN]; // 查询区间右端点
int queryK[MAXN]; // 查询第k小
int queryId[MAXN]; // 查询编号

// 树状数组，用于维护前缀和
int tree[MAXN];

// 整体二分中用于分类查询的数组
int lset[MAXN]; // 答案在左半部分的查询
int rset[MAXN]; // 答案在右半部分的查询

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
            ans[queryId[i]] = sorted[vl];
        }
        return;
    }
    
    // 二分中点，将值域划分为左右两部分
    int mid = (vl + vr) >> 1;
    
    // 优化点：使用vector记录添加的位置，避免双重循环
    // 这种方法将时间复杂度从O(n^2)降低到O(n)
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
        int id = queryId[i];
        // 查询区间[queryL[id], queryR[id]]中值小于等于sorted[mid]的元素个数
        int satisfy = query(queryL[id], queryR[id]);
        
        if (satisfy >= queryK[id]) {
            // 说明第k小的数在左半部分值域
            lset[++lsiz] = id;
        } else {
            // 说明第k小的数在右半部分值域，需要在右半部分找第(k-satisfy)小的数
            queryK[id] -= satisfy;
            rset[++rsiz] = id;
        }
    }
    
    // 撤销对树状数组的修改，恢复到处理前的状态
    // 利用之前记录的位置，高效地移除元素
    for (int pos : positions) {
        add(pos, -1);
    }
    
    // 保存当前查询ID数组的临时副本
    int temp[MAXN];
    for (int i = ql; i <= qr; i++) {
        temp[i] = queryId[i];
    }
    
    // 重新排列查询顺序，使得左集合的查询在前，右集合的查询在后
    for (int i = 1; i <= lsiz; i++) {
        queryId[ql + i - 1] = lset[i];
    }
    for (int i = 1; i <= rsiz; i++) {
        queryId[ql + lsiz + i - 1] = rset[i];
    }
    
    // 递归处理左右两部分
    // 左半部分：值域在[vl, mid]范围内的查询
    compute(ql, ql + lsiz - 1, vl, mid);
    // 右半部分：值域在[mid+1, vr]范围内的查询
    compute(ql + lsiz, qr, mid + 1, vr);
}

/**
 * 主函数，处理多组测试用例
 * 工程化特点：
 * 1. 关闭同步提高输入输出效率
 * 2. 进行离散化处理减少计算量
 * 3. 注意数组初始化和状态重置
 */
int main() {
    // 关闭同步提高输入输出效率
    ios::sync_with_stdio(false);
    cin.tie(0);
    
    int t; // 测试用例数量
    cin >> t;
    
    while (t--) {
        // 读取数组长度和查询次数
        cin >> n >> m;
        
        // 读取原始数组
        for (int i = 1; i <= n; i++) {
            cin >> arr[i];
            sorted[i] = arr[i];
        }
        
        // 读取查询
        for (int i = 1; i <= m; i++) {
            cin >> queryL[i] >> queryR[i] >> queryK[i];
            queryId[i] = i;
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
        
        // 清空树状数组，准备下一组测试用例
        memset(tree, 0, sizeof(tree));
    }
    
    return 0;
}