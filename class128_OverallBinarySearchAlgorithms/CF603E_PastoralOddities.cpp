// CF603E Pastoral Oddities - C++实现
// 题目来源：https://www.luogu.com.cn/problem/CF603E
// 时间复杂度：O(M * logN * logM)
// 空间复杂度：O(N + M)
// 
// 题目大意：
// 给定一张图，每次加边后求一个边集，使得每个点度数为奇数且最大边权最小。
// 
// 解题思路：
// 1. 使用整体二分对边权进行二分
// 2. 使用可撤销并查集维护连通性
// 3. 检查所有连通块大小是否都是偶数
// 4. 根据统计结果将操作分为两类递归处理
// 
// 算法详解：
// 1. 整体二分：将所有操作一起处理，对边权进行二分，避免对每个查询单独二分
// 2. 可撤销并查集：支持合并操作的回滚，用于维护图的连通性
// 3. 度数检查：通过检查每个连通块的大小是否为偶数来判断是否存在满足条件的边集

//#include <bits/stdc++.h>
//
//using namespace std;
//
//const int MAXN = 100001;
//const int MAXM = 300001;
//int n, m;
//
//// 边的信息
//int u[MAXM], v[MAXM], w[MAXM];
//
//// 查询编号
//int qid[MAXM];
//
//// 并查集
//int father[MAXN], size[MAXN], stack[MAXN], top = 0;
//
//// 整体二分
//int lset[MAXM], rset[MAXM];
//
//// 答案
//int ans[MAXM];
//
//// 边的辅助结构
//struct Edge {
//    int u, v, w, id;
//    bool operator<(const Edge& other) const {
//        return w < other.w;
//    }
//};
//
//// 初始化并查集
//// 将每个节点初始化为一个独立的集合
//void init() {
//    for (int i = 1; i <= n; i++) {
//        father[i] = i;
//        size[i] = 1;
//    }
//    top = 0;
//}
//
//// 查找根节点（带路径压缩）
//// 使用路径压缩优化，使查找操作的时间复杂度接近O(1)
//int find(int x) {
//    while (x != father[x]) {
//        x = father[x];
//    }
//    return x;
//}
//
//// 合并两个集合
//// 使用按秩合并优化，将较小的树合并到较大的树上
//// 返回true表示成功合并，false表示已在同一集合中
//bool unionSets(int x, int y) {
//    int fx = find(x);
//    int fy = find(y);
//    if (fx == fy) {
//        return false;
//    }
//    // 按秩合并，将较小的树合并到较大的树上
//    if (size[fx] < size[fy]) {
//        swap(fx, fy);
//    }
//    father[fy] = fx;
//    size[fx] += size[fy];
//    stack[++top] = fy; // 记录修改，用于回滚
//    return true;
//}
//
//// 检查所有连通块大小是否都是偶数
//// 根据题目要求，每个点的度数必须为奇数，这等价于每个连通块的大小为偶数
//bool check() {
//    for (int i = 1; i <= n; i++) {
//        // 只检查根节点，避免重复计算
//        if (find(i) == i && (size[i] & 1)) {
//            // 如果存在大小为奇数的连通块，则无法满足条件
//            return false;
//        }
//    }
//    return true;
//}
//
//// 回滚操作
//// 将并查集的状态回滚到targetTop时刻
//void rollback(int targetTop) {
//    while (top > targetTop) {
//        int y = stack[top--];
//        int fy = find(y);
//        size[fy] -= size[y];
//        father[y] = y;
//    }
//}
//
//// 整体二分核心函数
//// ql, qr: 当前处理的查询范围
//// vl, vr: 当前处理的值域范围（边权范围）
//void compute(int ql, int qr, int vl, int vr) {
//    // 递归边界：没有查询需要处理
//    if (ql > qr) {
//        return;
//    }
//    // 递归边界：值域范围只有一个值，找到了答案
//    if (vl == vr) {
//        for (int i = ql; i <= qr; i++) {
//            ans[qid[i]] = vl;
//        }
//        return;
//    }
//    
//    // 二分中点
//    int mid = (vl + vr) >> 1;
//    int targetTop = top;
//    
//    // 添加编号小于等于mid的边到并查集中
//    for (int i = vl; i <= mid; i++) {
//        unionSets(u[i], v[i]);
//    }
//    
//    // 检查每个查询，并根据结果将查询分为两类
//    int lsiz = 0, rsiz = 0;
//    for (int i = ql; i <= qr; i++) {
//        int id = qid[i];
//        // 检查当前状态是否满足条件
//        if (check()) {
//            // 满足条件，答案可能更小，分到左区间
//            lset[++lsiz] = id;
//        } else {
//            // 不满足条件，答案必须更大，分到右区间
//            rset[++rsiz] = id;
//        }
//    }
//    
//    // 重新排列查询顺序，使左区间的查询在前，右区间的查询在后
//    for (int i = 1; i <= lsiz; i++) {
//        qid[ql + i - 1] = lset[i];
//    }
//    for (int i = 1; i <= rsiz; i++) {
//        qid[ql + lsiz + i - 1] = rset[i];
//    }
//    
//    // 回滚操作，恢复到添加边之前的状态
//    rollback(targetTop);
//    
//    // 递归处理左右两部分
//    // 左区间：答案范围[vl, mid]
//    compute(ql, ql + lsiz - 1, vl, mid);
//    // 右区间：答案范围[mid+1, vr]
//    compute(ql + lsiz, qr, mid + 1, vr);
//}
//
//int main() {
//    ios::sync_with_stdio(false);
//    cin.tie(nullptr);
//    cin >> n >> m;
//    
//    // 读取边信息
//    vector<Edge> edges(m + 1);
//    for (int i = 1; i <= m; i++) {
//        cin >> edges[i].u >> edges[i].v >> edges[i].w;
//        u[i] = edges[i].u;
//        v[i] = edges[i].v;
//        w[i] = edges[i].w;
//        edges[i].id = i;
//    }
//    
//    // 按权重排序，这是整体二分的前提条件
//    sort(edges.begin() + 1, edges.end());
//    
//    // 重新编号，使边按权重递增顺序排列
//    for (int i = 1; i <= m; i++) {
//        u[i] = edges[i].u;
//        v[i] = edges[i].v;
//        w[i] = edges[i].w;
//        qid[i] = edges[i].id;
//    }
//    
//    // 初始化并查集
//    init();
//    
//    // 整体二分求解
//    compute(1, m, 1, m);
//    
//    // 输出结果
//    vector<int> result(m + 1);
//    for (int i = 1; i <= m; i++) {
//        result[qid[i]] = ans[qid[i]];
//    }
//    
//    int maxWeight = -1;
//    for (int i = 1; i <= m; i++) {
//        if (result[i] > 0) {
//            maxWeight = max(maxWeight, w[result[i]]);
//        }
//        if (maxWeight > 0) {
//            cout << maxWeight << "\n";
//        } else {
//            cout << "-1\n";
//        }
//    }
//    
//    return 0;
//}