// HDU 5306 Gorgeous Sequence - 区间最值操作
// 题目描述：
// 维护一个序列 a，执行以下操作：
// 1. 0 l r t: 对于所有的 i ∈ [l,r]，将 a[i] 变成 min(a[i], t)
// 2. 1 l r: 输出 max{a[i] | i ∈ [l,r]}
// 3. 2 l r: 输出 Σ{a[i] | i ∈ [l,r]}
//
// 解题思路：
// 使用吉司机线段树（吉如一算法）
// 每个节点维护以下信息：
// - 最大值 mx
// - 次大值 sem
// - 最大值个数 cnt
// - 区间和 sum
//
// 关键技术：
// 1. 势能分析法：保证时间复杂度为 O(n log² n) 均摊
// 2. 三种更新情况：
//    a. t >= mx: 无需更新
//    b. sem < t < mx: 直接更新最大值
//    c. t <= sem: 递归处理
//
// 时间复杂度分析：
// 1. 建树：O(n)
// 2. 区间取min操作：O(n log² n) 均摊
// 3. 区间最大值查询：O(log n)
// 4. 区间和查询：O(log n)
//
// 是否最优解：是
// 这是解决区间最值操作问题的最优解法

#include <iostream>
#include <vector>
#include <algorithm>
#include <climits>
using namespace std;

class SegmentTree {
private:
    vector<long long> mx;    // 区间最大值
    vector<long long> sem;   // 区间次大值
    vector<int> cnt;         // 最大值个数
    vector<long long> sum;   // 区间和
    vector<long long> lazy;  // 懒惰标记
    int n;
    
    // 合并左右子节点信息
    void pushUp(int rt) {
        int l = rt << 1, r = rt << 1 | 1;
        sum[rt] = sum[l] + sum[r];
        
        if (mx[l] > mx[r]) {
            mx[rt] = mx[l];
            cnt[rt] = cnt[l];
            sem[rt] = max(sem[l], mx[r]);
        } else if (mx[l] < mx[r]) {
            mx[rt] = mx[r];
            cnt[rt] = cnt[r];
            sem[rt] = max(mx[l], sem[r]);
        } else {
            mx[rt] = mx[l];
            cnt[rt] = cnt[l] + cnt[r];
            sem[rt] = max(sem[l], sem[r]);
        }
    }
    
    // 下传懒惰标记
    void pushDown(int rt) {
        if (lazy[rt] < mx[rt]) {
            int l = rt << 1, r = rt << 1 | 1;
            if (mx[l] > lazy[rt] && sem[l] < lazy[rt]) {
                sum[l] += (lazy[rt] - mx[l]) * cnt[l];
                mx[l] = lazy[rt];
                lazy[l] = lazy[rt];
            }
            if (mx[r] > lazy[rt] && sem[r] < lazy[rt]) {
                sum[r] += (lazy[rt] - mx[r]) * cnt[r];
                mx[r] = lazy[rt];
                lazy[r] = lazy[rt];
            }
            lazy[rt] = LLONG_MAX;
        }
    }
    
public:
    SegmentTree(int size) {
        n = size;
        int size4 = 4 * n;
        mx.resize(size4);
        sem.resize(size4);
        cnt.resize(size4);
        sum.resize(size4);
        lazy.resize(size4, LLONG_MAX);
    }
    
    // 建立线段树
    void build(vector<int>& arr, int l, int r, int rt) {
        lazy[rt] = LLONG_MAX;
        if (l == r) {
            mx[rt] = sum[rt] = arr[l];
            sem[rt] = -1;
            cnt[rt] = 1;
            return;
        }
        int mid = (l + r) >> 1;
        build(arr, l, mid, rt << 1);
        build(arr, mid + 1, r, rt << 1 | 1);
        pushUp(rt);
    }
    
    // 区间取min操作
    void updateMin(int L, int R, long long val, int l, int r, int rt) {
        if (val >= mx[rt]) return;
        if (L <= l && r <= R && val > sem[rt]) {
            // 情况2：sem < val < mx，直接更新
            sum[rt] += (val - mx[rt]) * cnt[rt];
            mx[rt] = val;
            lazy[rt] = val;
            return;
        }
        pushDown(rt);
        int mid = (l + r) >> 1;
        if (L <= mid) updateMin(L, R, val, l, mid, rt << 1);
        if (R > mid) updateMin(L, R, val, mid + 1, r, rt << 1 | 1);
        pushUp(rt);
    }
    
    // 查询区间最大值
    long long queryMax(int L, int R, int l, int r, int rt) {
        if (L <= l && r <= R) return mx[rt];
        pushDown(rt);
        int mid = (l + r) >> 1;
        long long res = LLONG_MIN;
        if (L <= mid) res = max(res, queryMax(L, R, l, mid, rt << 1));
        if (R > mid) res = max(res, queryMax(L, R, mid + 1, r, rt << 1 | 1));
        return res;
    }
    
    // 查询区间和
    long long querySum(int L, int R, int l, int r, int rt) {
        if (L <= l && r <= R) return sum[rt];
        pushDown(rt);
        int mid = (l + r) >> 1;
        long long res = 0;
        if (L <= mid) res += querySum(L, R, l, mid, rt << 1);
        if (R > mid) res += querySum(L, R, mid + 1, r, rt << 1 | 1);
        return res;
    }
};

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);
    
    int T;
    cin >> T;
    
    while (T--) {
        int n, m;
        cin >> n >> m;
        
        vector<int> arr(n + 1);
        for (int i = 1; i <= n; i++) {
            cin >> arr[i];
        }
        
        SegmentTree segTree(n);
        segTree.build(arr, 1, n, 1);
        
        for (int i = 0; i < m; i++) {
            int op, l, r;
            cin >> op >> l >> r;
            
            if (op == 0) {
                long long t;
                cin >> t;
                segTree.updateMin(l, r, t, 1, n, 1);
            } else if (op == 1) {
                cout << segTree.queryMax(l, r, 1, n, 1) << endl;
            } else {
                cout << segTree.querySum(l, r, 1, n, 1) << endl;
            }
        }
    }
    
    return 0;
}