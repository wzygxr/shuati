// P3527 [POI2011] MET-Meteors - 陨石雨 - C++实现
// 题目来源：https://www.luogu.com.cn/problem/P3527
// 时间复杂度：O(K * logK * logM)
// 空间复杂度：O(N + M + K)

//#include <bits/stdc++.h>
//
//using namespace std;
//
//const int MAXN = 300001;
//int n, m, k;
//
//// 国家编号
//int qid[MAXN];
//// 国家的需求
//int need[MAXN];
//
//// 陨石雨的参数
//int rainl[MAXN];
//int rainr[MAXN];
//int num[MAXN];
//
//// 国家拥有的区域列表
//int head[MAXN];
//int nxt[MAXN];
//int to[MAXN];
//int cnt = 0;
//
//// 树状数组，支持范围修改、单点查询
//long long tree[MAXN << 1];
//
//// 整体二分
//int lset[MAXN];
//int rset[MAXN];
//
//// 每个国家的答案
//int ans[MAXN];
//
//void addEdge(int i, int v) {
//    nxt[++cnt] = head[i];
//    to[cnt] = v;
//    head[i] = cnt;
//}
//
//int lowbit(int i) {
//    return i & -i;
//}
//
//void add(int i, int v) {
//    int siz = m * 2;
//    while (i <= siz) {
//        tree[i] += v;
//        i += lowbit(i);
//    }
//}
//
//void add(int l, int r, int v) {
//    add(l, v);
//    add(r + 1, -v);
//}
//
//long long query(int i) {
//    long long ret = 0;
//    while (i > 0) {
//    	ret += tree[i];
//        i -= lowbit(i);
//    }
//    return ret;
//}
//
//void compute(int ql, int qr, int vl, int vr) {
//    if (ql > qr) {
//        return;
//    }
//    if (vl == vr) {
//        for (int i = ql; i <= qr; i++) {
//            ans[qid[i]] = vl;
//        }
//    } else {
//        int mid = (vl + vr) >> 1;
//        for (int i = vl; i <= mid; i++) {
//            add(rainl[i], rainr[i], num[i]);
//        }
//        int lsiz = 0, rsiz = 0;
//        for (int i = ql; i <= qr; i++) {
//            int id = qid[i];
//            long long satisfy = 0;
//            for (int e = head[id]; e > 0; e = nxt[e]) {
//                satisfy += query(to[e]) + query(to[e] + m);
//                if (satisfy >= need[id]) {
//                    break;
//                }
//            }
//            if (satisfy >= need[id]) {
//                lset[++lsiz] = id;
//            } else {
//                need[id] -= satisfy;
//                rset[++rsiz] = id;
//            }
//        }
//        for (int i = 1; i <= lsiz; i++) {
//            qid[ql + i - 1] = lset[i];
//        }
//        for (int i = 1; i <= rsiz; i++) {
//            qid[ql + lsiz + i - 1] = rset[i];
//        }
//        for (int i = vl; i <= mid; i++) {
//            add(rainl[i], rainr[i], -num[i]);
//        }
//        compute(ql, ql + lsiz - 1, vl, mid);
//        compute(ql + lsiz, qr, mid + 1, vr);
//    }
//}
//
//int main() {
//    ios::sync_with_stdio(false);
//    cin.tie(nullptr);
//    cin >> n >> m;
//    for (int i = 1, nation; i <= m; i++) {
//        cin >> nation;
//        addEdge(nation, i);
//    }
//    for (int i = 1; i <= n; i++) {
//        qid[i] = i;
//        cin >> need[i];
//    }
//    cin >> k;
//    for (int i = 1; i <= k; i++) {
//        cin >> rainl[i] >> rainr[i] >> num[i];
//        if (rainr[i] < rainl[i]) {
//            rainr[i] += m;
//        }
//    }
//    compute(1, n, 1, k + 1);
//    for (int i = 1; i <= n; i++) {
//        if (ans[i] == k + 1) {
//            cout << "NIE" << '\n';
//        } else {
//            cout << ans[i] << '\n';
//        }
//    }
//    return 0;
//}