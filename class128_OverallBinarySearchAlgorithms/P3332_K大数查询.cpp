// P3332 [ZJOI2013] K大数查询 - C++实现
// 题目来源：https://www.luogu.com.cn/problem/P3332
// 时间复杂度：O(M * logN * log(maxValue))
// 空间复杂度：O(N + M)

//#include <bits/stdc++.h>
//
//using namespace std;
//
//const int MAXN = 50001;
//const int MAXM = 50001;
//int n, m;
//
//// 事件编号组成的数组
//int eid[MAXM << 1];
//// op == 1，代表修改事件，区间[L,R]增加值val
//// op == 2，代表查询事件，[L,R]范围上查询第k大，q表示问题的编号
//int op[MAXM << 1];
//int L[MAXM << 1];
//int R[MAXM << 1];
//int val[MAXM << 1];
//int k[MAXM << 1];
//int q[MAXM << 1];
//int cnte = 0;
//
//// 树状数组，支持区间修改、单点查询
//long long tree[MAXN << 1];
//
//// 整体二分
//int lset[MAXM << 1];
//int rset[MAXM << 1];
//
//// 查询的答案
//long long ans[MAXN];
//
//int lowbit(int i) {
//    return i & -i;
//}
//
//void add(int i, long long v) {
//    int siz = n;
//    while (i <= siz) {
//        tree[i] += v;
//        i += lowbit(i);
//    }
//}
//
//// 区间加法 [l, r] += v
//void add(int l, int r, long long v) {
//    add(l, v);
//    add(r + 1, -v);
//}
//
//long long query(int i) {
//    long long ret = 0;
//    while (i > 0) {
//        ret += tree[i];
//        i -= lowbit(i);
//    }
//    return ret;
//}
//
//void compute(int el, int er, long long vl, long long vr) {
//    if (el > er) {
//        return;
//    }
//    if (vl == vr) {
//        for (int i = el; i <= er; i++) {
//            int id = eid[i];
//            if (op[id] == 2) {
//                ans[q[id]] = vl;
//            }
//        }
//    } else {
//        long long mid = (vl + vr) >> 1;
//        int lsiz = 0, rsiz = 0;
//        for (int i = el; i <= er; i++) {
//            int id = eid[i];
//            if (op[id] == 1) {
//                // 修改操作
//                if (val[id] <= mid) {
//                    // 对左半区间有贡献，执行修改
//                    add(L[id], R[id], 1);
//                    lset[++lsiz] = id;
//                } else {
//                    // 对左半区间无贡献，放入右半区间
//                    rset[++rsiz] = id;
//                }
//            } else {
//                // 查询操作
//                long long satisfy = query(R[id]) - query(L[id] - 1);
//                if (satisfy >= k[id]) {
//                    // 说明第k大的数在左半部分
//                    lset[++lsiz] = id;
//                } else {
//                    // 说明第k大的数在右半部分
//                    k[id] -= satisfy;
//                    rset[++rsiz] = id;
//                }
//            }
//        }
//        // 撤销对树状数组的修改
//        for (int i = 1; i <= lsiz; i++) {
//            int id = lset[i];
//            if (op[id] == 1 && val[id] <= (vl + vr) >> 1) {
//                add(L[id], R[id], -1);
//            }
//        }
//        // 重新排列事件顺序
//        for (int i = 1; i <= lsiz; i++) {
//            eid[el + i - 1] = lset[i];
//        }
//        for (int i = 1; i <= rsiz; i++) {
//            eid[el + lsiz + i - 1] = rset[i];
//        }
//        // 递归处理左右两部分
//        compute(el, el + lsiz - 1, vl, mid);
//        compute(el + lsiz, er, mid + 1, vr);
//    }
//}
//
//int main() {
//    ios::sync_with_stdio(false);
//    cin.tie(nullptr);
//    cin >> n >> m;
//    
//    long long minVal = 1e18;
//    long long maxVal = -1e18;
//    
//    for (int i = 1; i <= m; i++) {
//        eid[i] = i;
//        cin >> op[i] >> L[i] >> R[i];
//        
//        if (op[i] == 1) {
//            // 修改操作
//            cin >> val[i];
//            minVal = min(minVal, (long long)val[i]);
//            maxVal = max(maxVal, (long long)val[i]);
//        } else {
//            // 查询操作
//            cin >> k[i];
//            q[i] = i;
//        }
//    }
//    
//    // 整体二分求解
//    compute(1, m, minVal, maxVal);
//    
//    // 输出结果
//    for (int i = 1; i <= m; i++) {
//        if (op[i] == 2) {
//            cout << ans[q[i]] << '\n';
//        }
//    }
//    
//    return 0;
//}