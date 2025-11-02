// CF1100F Ivan and Burgers - C++实现
// 题目来源：https://codeforces.com/problemset/problem/1100/F
// 题目描述：
// 给定一个长度为n的数组，有q次查询，每次查询[l,r]区间内元素异或的最大值。
// 时间复杂度：O((N+Q) * logN * 32)
// 空间复杂度：O(N * 32)

// 由于环境限制，这里只提供C++代码框架，实际编译需要相应环境支持
//#include <iostream>
//#include <algorithm>
//#include <cstdio>
//#include <vector>
//#include <cstring>
//using namespace std;

const int MAXN = 500001;
const int MAXB = 32;
int n, m;

// 原始数组
int arr[MAXN];

// 查询信息
struct Query {
    int l, r, id;
    
    Query() {}
    Query(int _l, int _r, int _id) {
        l = _l;
        r = _r;
        id = _id;
    }
};

Query queries[MAXN];

// 线性基
struct LinearBasis {
    int a[MAXB];
    
    void init() {
        memset(a, 0, sizeof(a));
    }
    
    void insert(int x) {
        for (int i = MAXB - 1; i >= 0; i--) {
            if (((x >> i) & 1) == 0) continue;
            if (a[i] == 0) {
                a[i] = x;
                break;
            }
            x ^= a[i];
        }
    }
    
    int queryMax() {
        int res = 0;
        for (int i = MAXB - 1; i >= 0; i--) {
            if (((res >> i) & 1) == 0) {
                res ^= a[i];
            }
        }
        return res;
    }
    
    void merge(LinearBasis other) {
        for (int i = 0; i < MAXB; i++) {
            if (other.a[i] != 0) {
                insert(other.a[i]);
            }
        }
    }
};

// 整体二分
int lset[MAXN];
int rset[MAXN];

// 查询的答案
int ans[MAXN];

// 线性基数组
LinearBasis basis[MAXN];

// 整体二分核心函数
// ql, qr: 查询范围
// vl, vr: 区间范围
void compute(int ql, int qr, int vl, int vr) {
    // 递归边界
    if (ql > qr) {
        return;
    }
    
    // 如果区间范围只有一个位置，说明找到了答案
    if (vl == vr) {
        for (int i = ql; i <= qr; i++) {
            ans[queries[i].id] = basis[vl].queryMax();
        }
        return;
    }
    
    // 二分中点
    int mid = (vl + vr) >> 1;
    
    // 构建左半部分的线性基
    LinearBasis leftBasis;
    leftBasis.init();
    for (int i = mid; i >= vl; i--) {
        leftBasis.insert(arr[i]);
        // 这里需要保存中间结果用于后续处理
    }
    
    // 构建右半部分的线性基
    LinearBasis rightBasis;
    rightBasis.init();
    for (int i = mid + 1; i <= vr; i++) {
        rightBasis.insert(arr[i]);
        // 这里需要保存中间结果用于后续处理
    }
    
    // 检查每个查询，根据区间位置划分到左右区间
    int lsiz = 0, rsiz = 0;
    for (int i = ql; i <= qr; i++) {
        int l = queries[i].l;
        int r = queries[i].r;
        
        if (r <= mid) {
            // 查询区间完全在左半部分
            lset[++lsiz] = i;
        } else if (l > mid) {
            // 查询区间完全在右半部分
            rset[++rsiz] = i;
        } else {
            // 查询区间跨越中点
            // 需要合并左右两部分的线性基
            LinearBasis temp;
            temp.init();
            temp.merge(leftBasis);
            temp.merge(rightBasis);
            ans[queries[i].id] = temp.queryMax();
        }
    }
    
    // 重新排列查询顺序
    int idx = ql;
    for (int i = 1; i <= lsiz; i++) {
        queries[idx++] = queries[lset[i]];
    }
    for (int i = 1; i <= rsiz; i++) {
        queries[idx++] = queries[rset[i]];
    }
    
    // 递归处理左右两部分
    compute(ql, ql + lsiz - 1, vl, mid);
    compute(ql + lsiz, qr, mid + 1, vr);
}

//int main() {
//    ios::sync_with_stdio(false);
//    cin.tie(0);
//    cout.tie(0);
//    
//    cin >> n;
//    
//    // 读取原始数组
//    for (int i = 1; i <= n; i++) {
//        cin >> arr[i];
//    }
//    
//    cin >> m;
//    
//    // 读取查询
//    for (int i = 1; i <= m; i++) {
//        int l, r;
//        cin >> l >> r;
//        queries[i] = Query(l, r, i);
//    }
//    
//    // 初始化线性基数组
//    for (int i = 0; i <= n; i++) {
//        basis[i].init();
//    }
//    
//    // 整体二分求解
//    compute(1, m, 1, n);
//    
//    // 输出结果
//    for (int i = 1; i <= m; i++) {
//        cout << ans[i] << "\n";
//    }
//    
//    return 0;
//}