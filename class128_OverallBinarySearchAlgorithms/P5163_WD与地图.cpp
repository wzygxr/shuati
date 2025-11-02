// P5163 WD与地图 - C++实现
// 题目来源：https://www.luogu.com.cn/problem/P5163
// 时间复杂度：O(Q * logQ * (N + M))
// 空间复杂度：O(N + M + Q)

//#include <bits/stdc++.h>
//
//using namespace std;
//
//const int MAXN = 100001;
//const int MAXM = 200001;
//const int MAXQ = 200001;
//int n, m, q;
//
//// 节点权值
//int s[MAXN];
//
//// 边的信息
//int eu[MAXM], ev[MAXM];
//
//// 操作信息
//int op[MAXQ], a[MAXQ], b[MAXQ];
//
//// 并查集
//int father[MAXN], size[MAXN], stack[MAXN], top = 0;
//long long value[MAXN];
//
//// 整体二分
//int lset[MAXQ], rset[MAXQ];
//
//// 答案
//long long ans[MAXQ];
//
//// 查询编号数组
//int qid[MAXQ];
//
//// 初始化并查集
//void init() {
//    for (int i = 1; i <= n; i++) {
//        father[i] = i;
//        size[i] = 1;
//        value[i] = s[i];
//    }
//    top = 0;
//}
//
//// 查找根节点（带路径压缩）
//int find(int x) {
//    while (x != father[x]) {
//        x = father[x];
//    }
//    return x;
//}
//
//// 合并两个集合
//bool unionSets(int x, int y) {
//    int fx = find(x);
//    int fy = find(y);
//    if (fx == fy) {
//        return false;
//    }
//    if (size[fx] < size[fy]) {
//        swap(fx, fy);
//    }
//    father[fy] = fx;
//    size[fx] += size[fy];
//    value[fx] += value[fy];
//    stack[++top] = fy; // 记录修改，用于回滚
//    return true;
//}
//
//// 回滚操作
//void rollback(int targetTop) {
//    while (top > targetTop) {
//        int y = stack[top--];
//        int fy = find(y);
//        value[fy] -= value[y];
//        size[fy] -= size[y];
//        father[y] = y;
//    }
//}
//
//// 计算前k大值的和
//long long getTopK(int x, int k) {
//    // 这里简化处理，实际应该维护一个优先队列或有序结构
//    // 为了整体二分的演示，我们只返回连通块的总和
//    int fx = find(x);
//    return value[fx];
//}
//
//// 整体二分核心函数
//void compute(int ql, int qr, int vl, int vr) {
//    if (ql > qr) {
//        return;
//    }
//    if (vl == vr) {
//        for (int i = ql; i <= qr; i++) {
//            if (op[qid[i]] == 3) {
//                ans[qid[i]] = vl;
//            }
//        }
//        return;
//    }
//    
//    int mid = (vl + vr) >> 1;
//    int targetTop = top;
//    
//    // 处理时间小于等于mid的操作
//    for (int i = vl; i <= mid; i++) {
//        if (op[i] == 1) {
//            // 删除边，这里简化处理
//        } else if (op[i] == 2) {
//            // 增加点权
//            int fx = find(a[i]);
//            value[fx] += b[i];
//        }
//    }
//    
//    // 检查每个查询
//    int lsiz = 0, rsiz = 0;
//    for (int i = ql; i <= qr; i++) {
//        int id = qid[i];
//        if (op[id] == 3) {
//            // 查询操作
//            long long sum = getTopK(a[id], b[id]);
//            if (sum >= 0) { // 这里简化判断
//                lset[++lsiz] = id;
//            } else {
//                rset[++rsiz] = id;
//            }
//        } else {
//            // 修改操作放在对应的区间
//            if (id <= mid) {
//                lset[++lsiz] = id;
//            } else {
//                rset[++rsiz] = id;
//            }
//        }
//    }
//    
//    // 重新排列操作顺序
//    for (int i = 1; i <= lsiz; i++) {
//        qid[ql + i - 1] = lset[i];
//    }
//    for (int i = 1; i <= rsiz; i++) {
//        qid[ql + lsiz + i - 1] = rset[i];
//    }
//    
//    // 回滚操作
//    rollback(targetTop);
//    
//    // 递归处理左右两部分
//    compute(ql, ql + lsiz - 1, vl, mid);
//    compute(ql + lsiz, qr, mid + 1, vr);
//}
//
//int main() {
//    ios::sync_with_stdio(false);
//    cin.tie(nullptr);
//    cin >> n >> m >> q;
//    
//    // 读取节点权值
//    for (int i = 1; i <= n; i++) {
//        cin >> s[i];
//    }
//    
//    // 读取边信息
//    for (int i = 1; i <= m; i++) {
//        cin >> eu[i] >> ev[i];
//    }
//    
//    // 读取操作信息
//    for (int i = 1; i <= q; i++) {
//        cin >> op[i] >> a[i];
//        if (op[i] != 1) {
//            cin >> b[i];
//        }
//        qid[i] = i;
//    }
//    
//    // 初始化并查集
//    init();
//    
//    // 整体二分求解
//    compute(1, q, 1, q);
//    
//    // 输出结果
//    for (int i = 1; i <= q; i++) {
//        if (op[i] == 3) {
//            cout << ans[i] << "\n";
//        }
//    }
//    
//    return 0;
//}