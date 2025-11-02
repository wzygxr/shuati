package class122;

/**
 * 运输计划，C++版，递归不用改迭代
 * 
 * 题目来源：洛谷 P2680 运输计划
 * 题目链接：https://www.luogu.com.cn/problem/P2680
 * 
 * 算法原理：
 * 这是一道经典的树上问题，结合了以下几种关键技术：
 * 1. 二分答案：答案具有单调性，可以通过二分法寻找最优解
 * 2. 树上边差分：用于统计每条边被超过限制的路径覆盖的次数
 * 3. Tarjan离线LCA算法：用于快速计算树上任意两点间的最近公共祖先
 * 4. DFS遍历：用于检查是否存在一条边满足删除条件
 * 
 * 解题思路：
 * 1. 二分答案：二分最大运输代价，检查能否通过删除一条边达到该代价
 * 2. 对于每个二分的值limit，找出所有超过limit的运输计划
 * 3. 使用树上边差分技术统计每条边被这些超限路径覆盖的次数
 * 4. 通过DFS遍历检查是否存在一条边，满足：
 *    a. 被所有超限路径覆盖（即覆盖次数等于超限路径数）
 *    b. 边权不小于需要减少的值（maxCost - limit）
 * 5. 如果存在这样的边，则当前limit可行，尝试更小的值；否则尝试更大的值
 * 
 * 树上边差分原理：
 * 对于从u到v的路径，我们执行以下操作：
 * num[u]++; num[v]++; num[lca] -= 2;
 * 然后通过DFS累加子树和，得到每条边的真实覆盖次数
 * 注意：这里统计的是点的标记，实际应用中需要转换为边的覆盖次数
 * 
 * 时间复杂度分析：
 * 1. 二分答案：O(log(maxCost))
 * 2. Tarjan离线LCA：O(N + M)
 * 3. 树上边差分标记：O(M)
 * 4. DFS检查：O(N)
 * 总体时间复杂度：O((N + M) * log(maxCost))
 * 
 * 空间复杂度分析：
 * 1. 图存储：O(N + M)
 * 2. Tarjan相关数组：O(N + M)
 * 3. 差分数组：O(N)
 * 总体空间复杂度：O(N + M)
 * 
 * 工程化考量：
 * 1. 这是C++版本的实现，与Java版本逻辑完全一样
 * 2. C++版本可以顺利通过所有测试用例，因为运行效率更高
 * 3. 使用递归实现，代码更简洁易懂
 * 4. 使用链式前向星存储树结构，提高遍历效率
 * 5. Tarjan算法中使用并查集优化，保证查询效率
 * 6. 使用快速IO提高输入效率
 */

// 如下实现是C++的版本，C++版本和java版本逻辑完全一样
// 提交如下代码，可以通过所有测试用例

//#include <bits/stdc++.h>
//
//using namespace std;
//
//const int MAXN = 300001;
//const int MAXM = 300001;
//int n, m;
//int num[MAXN];
//int headEdge[MAXN];
//int edgeNext[MAXN << 1];
//int edgeTo[MAXN << 1];
//int edgeWeight[MAXN << 1];
//int tcnt;
//int headQuery[MAXN];
//int queryNext[MAXM << 1];
//int queryTo[MAXM << 1];
//int queryIndex[MAXM << 1];
//int qcnt;
//bool visited[MAXN];
//int unionfind[MAXN];
//int quesu[MAXM];
//int quesv[MAXM];
//int dist[MAXN];
//int lca[MAXM];
//int cost[MAXM];
//int maxCost;
//int atLeast;
//int beyond;
//
//void build() {
//    tcnt = 1;
//    qcnt = 1;
//    for(int i = 1; i <= n; i++) {
//        headEdge[i] = 0;
//        headQuery[i] = 0;
//        visited[i] = false;
//        unionfind[i] = i;
//    }
//    maxCost = 0;
//}
//
//void addEdge(int u, int v, int w) {
//    edgeNext[tcnt] = headEdge[u];
//    edgeTo[tcnt] = v;
//    edgeWeight[tcnt] = w;
//    headEdge[u] = tcnt++;
//}
//
//void addQuery(int u, int v, int i) {
//    queryNext[qcnt] = headQuery[u];
//    queryTo[qcnt] = v;
//    queryIndex[qcnt] = i;
//    headQuery[u] = qcnt++;
//}
//
//int find(int i) {
//    if(i != unionfind[i]) {
//        unionfind[i] = find(unionfind[i]);
//    }
//    return unionfind[i];
//}
//
//void tarjan(int u, int f, int w) {
//    visited[u] = true;
//    dist[u] = dist[f] + w;
//    for(int e = headEdge[u]; e != 0; e = edgeNext[e]) {
//        int v = edgeTo[e];
//        if(v != f) {
//            tarjan(v, u, edgeWeight[e]);
//        }
//    }
//    for(int e = headQuery[u]; e != 0; e = queryNext[e]) {
//        int v = queryTo[e];
//        if(visited[v]) {
//            int i = queryIndex[e];
//            lca[i] = find(v);
//            cost[i] = dist[u] + dist[v] - 2 * dist[lca[i]];
//            maxCost = max(maxCost, cost[i]);
//        }
//    }
//    unionfind[u] = f;
//}
//
//bool dfs(int u, int f, int w) {
//    for(int e = headEdge[u]; e != 0; e = edgeNext[e]) {
//        int v = edgeTo[e];
//        if(v != f) {
//            if(dfs(v, u, edgeWeight[e])) {
//                return true;
//            }
//        }
//    }
//    for(int e = headEdge[u]; e != 0; e = edgeNext[e]) {
//        int v = edgeTo[e];
//        if(v != f) {
//            num[u] += num[v];
//        }
//    }
//    return (num[u] == beyond && w >= atLeast);
//}
//
//bool f(int limit) {
//    atLeast = maxCost - limit;
//    for(int i = 1; i <= n; i++) {
//        num[i] = 0;
//    }
//    beyond = 0;
//    for(int i = 1; i <= m; i++) {
//        if(cost[i] > limit) {
//            num[quesu[i]]++;
//            num[quesv[i]]++;
//            num[lca[i]] -= 2;
//            beyond++;
//        }
//    }
//    if(beyond == 0) return true;
//    return dfs(1, 0, 0);
//}
//
//int compute() {
//    tarjan(1, 0, 0);
//    int l = 0;
//    int r = maxCost;
//    int ans = 0;
//    while(l <= r) {
//        int mid = (l + r) / 2;
//        if(f(mid)) {
//            ans = mid;
//            r = mid - 1;
//        } else {
//            l = mid + 1;
//        }
//    }
//    return ans;
//}
//
//int main(){
//    ios::sync_with_stdio(false);
//    cin.tie(nullptr);
//    cin >> n;
//    build();
//    cin >> m;
//    for(int i = 1; i < n; i++){
//        int u,v,w;
//        cin >> u >> v >> w;
//        addEdge(u,v,w);
//        addEdge(v,u,w);
//    }
//    for(int i = 1; i <= m; i++){
//        int u,v;
//        cin >> u >> v;
//        quesu[i] = u;
//        quesv[i] = v;
//        addQuery(u,v,i);
//        addQuery(v,u,i);
//    }
//    cout << compute() << "\n";
//    return 0;
//}