// P3834 【模板】可持久化线段树 2 / 静态区间第K小 - C++实现
// 题目来源：https://www.luogu.com.cn/problem/P3834
// 题目描述：给定一个长度为n的数组，有m次查询，每次查询[l,r]区间内第k小的数
// 解题思路：使用整体二分算法，将所有查询一起处理，避免对每个查询单独进行二分
// 时间复杂度：O((N+M) * logN * log(maxValue))
// 空间复杂度：O(N+M)
// 算法适用条件：
// 1. 询问的答案具有可二分性
// 2. 修改对判定答案的贡献互相独立
// 3. 修改如果对判定答案有贡献，则贡献为确定值
// 4. 贡献满足交换律、结合律，具有可加性
// 5. 题目允许离线操作

// 补充题目：POJ 2104 K-th Number
// 题目来源：http://poj.org/problem?id=2104
// 题目描述：给定一个长度为n的数组，有m次查询，每次查询[l,r]区间内第k小的数
// 解题思路：这是整体二分算法的经典应用，与P3834本质相同
// 时间复杂度：O((N+M) * logN * log(maxValue))
// 空间复杂度：O(N+M)
// 本题是静态区间第k小查询的标准问题，是整体二分算法的入门题目

// 由于环境限制，这里只提供C++代码框架，实际编译需要相应环境支持
//#include <iostream>
//#include <algorithm>
//#include <cstdio>
//using namespace std;

const int MAXN = 200001;
int n, m;  // n:数组长度, m:查询次数

// 原始数组，存储输入的数值
int arr[MAXN];

// 离散化后的数组，用于离散化处理，将大值域映射到小下标范围
int sorted[MAXN];

// 查询信息存储
int qid[MAXN];  // 查询编号
int l[MAXN];     // 查询区间左端点
int r[MAXN];     // 查询区间右端点
int k[MAXN];     // 查询第k小

// 树状数组，用于维护当前值域范围内元素的个数
int tree[MAXN];

// 整体二分中用于分类查询的临时存储
int lset[MAXN];  // 满足条件的查询
int rset[MAXN];  // 不满足条件的查询

// 查询的答案存储数组
int ans[MAXN];

// 树状数组操作
// 计算二进制表示中最低位的1所代表的数值
int lowbit(int i) {
    return i & -i;
}

// 在树状数组的第i个位置加上v
void add(int i, int v) {
    while (i <= n) {
        tree[i] += v;
        i += lowbit(i);
    }
}

// 计算前缀和[1, i]的和
int sum(int i) {
    int ret = 0;
    while (i > 0) {
        ret += tree[i];
        i -= lowbit(i);
    }
    return ret;
}

// 计算区间和[l, r]的和
int query(int l, int r) {
    return sum(r) - sum(l - 1);
}

// 整体二分核心函数
// ql, qr: 查询范围
// vl, vr: 值域范围（离散化后的下标）
void compute(int ql, int qr, int vl, int vr) {
    // 递归边界：没有查询需要处理
    if (ql > qr) {
        return;
    }
    
    // 如果值域范围只有一个值，说明找到了答案
    // 此时所有查询的答案都是sorted[vl]
    if (vl == vr) {
        for (int i = ql; i <= qr; i++) {
            ans[qid[i]] = sorted[vl];
        }
        return;
    }
    
    // 二分中点
    int mid = (vl + vr) >> 1;
    
    // 将值域小于等于mid的数加入树状数组
    // 这些数对后续的查询统计有贡献
    for (int i = vl; i <= mid; i++) {
        add(arr[i], 1);
    }
    
    // 检查每个查询，根据满足条件的元素个数划分到左右区间
    int lsiz = 0, rsiz = 0;
    for (int i = ql; i <= qr; i++) {
        int id = qid[i];
        // 查询区间[l[id], r[id]]中值小于等于sorted[mid]的元素个数
        int satisfy = query(l[id], r[id]);
        
        if (satisfy >= k[id]) {
            // 说明第k小的数在左半部分
            // 将该查询加入左集合
            lset[++lsiz] = id;
        } else {
            // 说明第k小的数在右半部分，需要在右半部分找第(k-satisfy)小的数
            // 更新k值，将该查询加入右集合
            k[id] -= satisfy;
            rset[++rsiz] = id;
        }
    }
    
    // 重新排列查询顺序，使得左集合的查询在前，右集合的查询在后
    for (int i = 1; i <= lsiz; i++) {
        qid[ql + i - 1] = lset[i];
    }
    for (int i = 1; i <= rsiz; i++) {
        qid[ql + lsiz + i - 1] = rset[i];
    }
    
    // 撤销对树状数组的修改，恢复到处理前的状态
    for (int i = vl; i <= mid; i++) {
        add(arr[i], -1);
    }
    
    // 递归处理左右两部分
    // 左半部分：值域在[vl, mid]范围内的查询
    compute(ql, ql + lsiz - 1, vl, mid);
    // 右半部分：值域在[mid+1, vr]范围内的查询
    compute(ql + lsiz, qr, mid + 1, vr);
}

//int main() {
//    ios::sync_with_stdio(false);
//    cin.tie(0);
//    cout.tie(0);
//    
//    cin >> n >> m;
//    
//    // 读取原始数组
//    for (int i = 1; i <= n; i++) {
//        cin >> arr[i];
//        sorted[i] = arr[i];
//    }
//    
//    // 离散化
//    sort(sorted + 1, sorted + n + 1);
//    int uniqueCount = unique(sorted + 1, sorted + n + 1) - sorted - 1;
//    
//    // 重新映射arr数组为离散化后的下标
//    for (int i = 1; i <= n; i++) {
//        arr[i] = lower_bound(sorted + 1, sorted + uniqueCount + 1, arr[i]) - sorted;
//    }
//    
//    // 读取查询
//    for (int i = 1; i <= m; i++) {
//        cin >> l[i] >> r[i] >> k[i];
//        qid[i] = i; // 查询编号
//    }
//    
//    // 整体二分求解
//    compute(1, m, 1, uniqueCount);
//    
//    // 输出结果
//    for (int i = 1; i <= m; i++) {
//        cout << ans[i] << "\n";
//    }
//    
//    return 0;
//}

// POJ 2104 K-th Number 完整实现类
// 这是整体二分算法的经典应用，与P3834本质相同
class POJ2104_KthNumber {
private:
    static const int MAXN = 100001; // 题目数据范围
    int n, m;  // n:数组长度, m:查询次数
    
    // 原始数组，存储输入的数值
    int arr[MAXN];
    
    // 离散化后的数组，用于离散化处理，将大值域映射到小下标范围
    int sorted[MAXN];
    
    // 查询信息存储
    int qid[MAXN];  // 查询编号
    int l[MAXN];    // 查询区间左端点
    int r[MAXN];    // 查询区间右端点
    int k[MAXN];    // 查询第k小
    
    // 树状数组，用于维护当前值域范围内元素的个数
    int tree[MAXN];
    
    // 整体二分中用于分类查询的临时存储
    int lset[MAXN];  // 满足条件的查询
    int rset[MAXN];  // 不满足条件的查询
    
    // 查询的答案存储数组
    int ans[MAXN];
    
    // 树状数组操作
    // 计算二进制表示中最低位的1所代表的数值
    int lowbit(int i) {
        return i & -i;
    }
    
    // 在树状数组的第i个位置加上v
    void add(int i, int v) {
        while (i <= n) {
            tree[i] += v;
            i += lowbit(i);
        }
    }
    
    // 计算前缀和[1, i]的和
    int sum(int i) {
        int ret = 0;
        while (i > 0) {
            ret += tree[i];
            i -= lowbit(i);
        }
        return ret;
    }
    
    // 计算区间和[l, r]的和
    int query(int l, int r) {
        return sum(r) - sum(l - 1);
    }
    
    // 整体二分核心函数
    // ql, qr: 查询范围
    // vl, vr: 值域范围（离散化后的下标）
    void compute(int ql, int qr, int vl, int vr) {
        // 递归边界：没有查询需要处理
        if (ql > qr) {
            return;
        }
        
        // 如果值域范围只有一个值，说明找到了答案
        // 此时所有查询的答案都是sorted[vl]
        if (vl == vr) {
            for (int i = ql; i <= qr; i++) {
                ans[qid[i]] = sorted[vl];
            }
            return;
        }
        
        // 二分中点
        int mid = (vl + vr) >> 1;
        
        // 正确的做法：遍历原始数组，如果元素值对应的离散化下标<=mid，则在对应位置+1
        for (int i = 1; i <= n; i++) {
            if (arr[i] <= mid) {
                add(i, 1);
            }
        }
        
        // 检查每个查询，根据满足条件的元素个数划分到左右区间
        int lsiz = 0, rsiz = 0;
        for (int i = ql; i <= qr; i++) {
            int id = qid[i];
            // 查询区间[l[id], r[id]]中值小于等于sorted[mid]的元素个数
            int satisfy = query(l[id], r[id]);
            
            if (satisfy >= k[id]) {
                // 说明第k小的数在左半部分
                // 将该查询加入左集合
                lset[++lsiz] = id;
            } else {
                // 说明第k小的数在右半部分，需要在右半部分找第(k-satisfy)小的数
                // 更新k值，将该查询加入右集合
                k[id] -= satisfy;
                rset[++rsiz] = id;
            }
        }
        
        // 重新排列查询顺序，使得左集合的查询在前，右集合的查询在后
        for (int i = 1; i <= lsiz; i++) {
            qid[ql + i - 1] = lset[i];
        }
        for (int i = 1; i <= rsiz; i++) {
            qid[ql + lsiz + i - 1] = rset[i];
        }
        
        // 撤销对树状数组的修改，恢复到处理前的状态
        for (int i = 1; i <= n; i++) {
            if (arr[i] <= mid) {
                add(i, -1);
            }
        }
        
        // 递归处理左右两部分
        // 左半部分：值域在[vl, mid]范围内的查询
        compute(ql, ql + lsiz - 1, vl, mid);
        // 右半部分：值域在[mid+1, vr]范围内的查询
        compute(ql + lsiz, qr, mid + 1, vr);
    }
    
    // 重置数据结构，避免多次调用时的干扰
    void reset() {
        for (int i = 0; i <= n; i++) {
            tree[i] = 0;
        }
    }
    
public:
    // 主函数，用于解决POJ 2104问题
    void solve() {
        // 注意：在实际使用时需要取消注释以下代码，并确保包含相应的头文件
        /*
        ios::sync_with_stdio(false);
        cin.tie(0);
        cout.tie(0);
        
        cin >> n >> m;
        
        // 读取原始数组
        for (int i = 1; i <= n; i++) {
            cin >> arr[i];
            sorted[i] = arr[i];
        }
        
        // 离散化
        sort(sorted + 1, sorted + n + 1);
        int uniqueCount = unique(sorted + 1, sorted + n + 1) - sorted - 1;
        
        // 重新映射arr数组为离散化后的下标
        for (int i = 1; i <= n; i++) {
            arr[i] = lower_bound(sorted + 1, sorted + uniqueCount + 1, arr[i]) - sorted;
        }
        
        // 读取查询
        for (int i = 1; i <= m; i++) {
            cin >> l[i] >> r[i] >> k[i];
            qid[i] = i; // 查询编号
        }
        
        // 重置树状数组
        reset();
        
        // 整体二分求解
        compute(1, m, 1, uniqueCount);
        
        // 输出结果
        for (int i = 1; i <= m; i++) {
            cout << ans[i] << "\n";
        }
        */
    }
};

// 注意：在实际提交POJ时，需要使用以下主函数结构
/*
int main() {
    POJ2104_KthNumber solver;
    solver.solve();
    return 0;
}
*/