package class155;

/**
 * 洛谷 P2483 K短路问题
 * 题目链接：https://www.luogu.com.cn/problem/P2483
 * 
 * 题目描述：
 * 有n个点编号1~n，有m条边，每条边都是正数边权，组成有向带权图。
 * 从1号点走到n号点，就认为是一次旅行。
 * 一次旅行中，边不能重复选，点可以重复经过，如果到达了n号点，那么旅行立刻停止。
 * 从1号点走到n号点，会有很多通路方案，通路方案的路费为选择边的边权累加和。
 * 虽然每次旅行都是从1号点到n号点，但是你希望每次旅行的通路方案都是不同的。
 * 任何两次旅行，只要选择的边稍有不同，就认为是不同的通路方案。
 * 你的钱数为money，用来支付路费，打印你一共能进行几次旅行。
 * 
 * 解题思路：
 * 使用可持久化左偏树（Persistent Leftist Tree）结合A*算法来解决K短路问题。
 * 
 * 核心思想：
 * 1. 首先在反图上运行Dijkstra算法，计算从终点n到所有点的最短距离，作为A*算法的启发函数
 * 2. 构建最短路树，并为每个节点生成左偏树，表示从该节点出发选择非树边的可能性
 * 3. 使用小根堆维护所有可能的路径，每次取出路径长度最小的路径
 * 4. 对于取出的路径，生成新的可能路径并加入堆中
 * 5. 直到钱不够支付下一条路径的费用为止
 * 
 * 算法步骤：
 * 1. 在反图上运行Dijkstra算法，得到从终点n到所有点的最短距离
 * 2. 构建最短路树，为每个节点生成左偏树
 * 3. 使用小根堆维护所有可能路径
 * 4. 逐步生成K短路，直到钱不够为止
 * 
 * 时间复杂度：O((n + m) log n + k log k)
 * 空间复杂度：O(n + m + k)
 * 
 * 相关题目：
 * - Java实现：Code05_KShortestPath1.java
 * - C++实现：Code05_KShortestPath2.java
 */

// #include <bits/stdc++.h>
// 
// using namespace std;
// 
// const int MAXN = 50001;   // 最大节点数
// const int MAXM = 200001;  // 最大边数
// const int MAXT = 1000001; // 最大左偏树节点数
// const int MAXH = 4200001; // 最大小根堆节点数
// const double INF = 1e18;  // 无穷大
// 
// int n, m;
// double money;
// 
// // 正图
// int headg[MAXN];     // 正图邻接表头
// int tog[MAXM];       // 正图边指向的节点
// int nextg[MAXM];     // 正图邻接表next指针
// double weightg[MAXM]; // 正图边权
// int cntg = 0;        // 正图边计数器
// 
// // 反图
// int headr[MAXN];     // 反图邻接表头
// int tor[MAXM];       // 反图边指向的节点
// int nextr[MAXM];     // 反图邻接表next指针
// double weightr[MAXM]; // 反图边权
// int cntr = 0;        // 反图边计数器
// 
// // 左偏树代表基于之前的通路方案，选择非树边的可能性
// // 左偏树的头就代表最优的选择，假设编号为h的节点是头
// // to[h] : 选择最优非树边，这个非树边在正图里指向哪个节点
// int to[MAXT];
// // cost[h] : 基于之前的通路方案，最优选择会让路费增加多少
// double cost[MAXT];
// int ls[MAXT];
// int rs[MAXT];
// int dist[MAXT];
// int cntt = 0;
// 
// // rt[u] : 在最短路树上，节点u及其所有祖先节点，所拥有的全部非树边，组成的左偏树
// int rt[MAXN];
// 
// // heap是经典的小根堆，放着很多(key, val)数据，根据val来组织小根堆
// int key[MAXH];
// double val[MAXH];
// int heap[MAXH];
// int cntd, cnth;
// 
// // dijkstra算法需要，根据反图跑dijkstra，生成从节点n开始的最短路树
// // vis[u] : 节点u到节点n的最短距离，是否已经计算过了
// bool vis[MAXN];
// // path[u] : 最短路树上，到达节点u的树边，编号是什么，也代表正图上，所对应的边
// int path[MAXN];
// // dis[u] : 最短路树上，节点n到节点u的最短距离
// double dis[MAXN];
// 
// /**
//  * 向正图中添加边
//  * @param u 起点
//  * @param v 终点
//  * @param w 边权
//  */
// void addEdgeG(int u, int v, double w){
//     nextg[++cntg] = headg[u];
//     tog[cntg] = v;
//     weightg[cntg] = w;
//     headg[u] = cntg;
// }
// 
// /**
//  * 向反图中添加边
//  * @param u 起点
//  * @param v 终点
//  * @param w 边权
//  */
// void addEdgeR(int u, int v, double w){
//     nextr[++cntr] = headr[u];
//     tor[cntr] = v;
//     weightr[cntr] = w;
//     headr[u] = cntr;
// }
// 
// /**
//  * 初始化一个左偏树节点
//  * @param t 指向的节点
//  * @param v 增量成本
//  * @return 新节点编号
//  */
// int init(int t, double v){
//     to[++cntt] = t;
//     cost[cntt] = v;
//     ls[cntt] = rs[cntt] = dist[cntt] = 0;
//     return cntt;
// }
// 
// /**
//  * 克隆一个左偏树节点（可持久化关键操作）
//  * @param i 要克隆的节点编号
//  * @return 新节点编号
//  */
// int clone(int i){
//     to[++cntt] = to[i];
//     cost[cntt] = cost[i];
//     ls[cntt] = ls[i];
//     rs[cntt] = rs[i];
//     dist[cntt] = dist[i];
//     return cntt;
// }
// 
// /**
//  * 合并两个左偏树
//  * @param i 第一棵左偏树的根节点
//  * @param j 第二棵左偏树的根节点
//  * @return 合并后的左偏树根节点
//  */
// int merge(int i, int j){
//     if(i == 0 || j == 0){
//         return i + j;
//     }
//     // 维护小根堆性质（cost小的优先）
//     if(cost[i] > cost[j]){
//         swap(i, j);
//     }
//     // 克隆根节点以保持历史版本不变
//     int h = clone(i);
//     // 递归合并右子树
//     rs[h] = merge(rs[h], j);
//     // 维护左偏性质
//     if(dist[ls[h]] < dist[rs[h]]){
//         swap(ls[h], rs[h]);
//     }
//     // 更新距离
//     dist[h] = dist[rs[h]] + 1;
//     return h;
// }
// 
// /**
//  * (k, v)组成一个数据，放到堆上，根据v来组织小根堆
//  * @param k 键值
//  * @param v 值
//  */
// void heapAdd(int k, double v){
//     key[++cntd] = k;
//     val[cntd] = v;
//     heap[++cnth] = cntd;
//     // 上浮操作维护堆性质
//     int cur = cnth, father = cur / 2;
//     while(cur > 1 && val[heap[father]] > val[heap[cur]]){
//         swap(heap[father], heap[cur]);
//         cur = father;
//         father = cur / 2;
//     }
// }
// 
// /**
//  * 小根堆上，堆顶的数据(k, v)弹出，并返回数据所在的下标ans
//  * 根据返回值ans，key[ans]得到k，val[ans]得到v
//  * @return 数据所在的下标
//  */
// int heapPop(){
//     int ans = heap[1];
//     // 将最后一个元素移到堆顶
//     heap[1] = heap[cnth--];
//     // 下沉操作维护堆性质
//     int cur = 1, l = cur * 2, r = l + 1, best;
//     while(l <= cnth){
//         // 找到左右子节点中较小的那个
//         best = r <= cnth && val[heap[r]] < val[heap[l]] ? r : l;
//         // 比较父节点与子节点中的最小者
//         best = val[heap[best]] < val[heap[cur]] ? best : cur;
//         if(best == cur) {
//             break;
//         }
//         // 交换元素
//         swap(heap[best], heap[cur]);
//         cur = best;
//         l = cur * 2;
//         r = l + 1;
//     }
//     return ans;
// }
// 
// /**
//  * 判断堆是否为空
//  * @return 堆是否为空
//  */
// bool heapEmpty(){
//     return cnth == 0;
// }
// 
// /**
//  * 根据反图跑dijkstra算法
//  * 得到从节点n出发的最短路树、每个节点到节点n的最短距离信息
//  * 最短路树如果有多个，找到任何一个即可
//  */
// void dijkstra(){
//     fill(dis, dis + MAXN, INF);
//     dis[n] = 0;
//     cntd = cnth = 0;
//     heapAdd(n, 0.0);
//     while(!heapEmpty()){
//         int top = heapPop();
//         int u = key[top];
//         double w = val[top];
//         if(!vis[u]){
//             vis[u] = true;
//             for(int e = headr[u], v; e != 0; e = nextr[e]){
//                 v = tor[e];
//                 if(dis[v] > w + weightr[e]){
//                     dis[v] = w + weightr[e];
//                     path[v] = e;
//                     heapAdd(v, dis[v]);
//                 }
//             }
//         }
//     }
// }
// 
// /**
//  * 在最短路树上的每个节点，生成自己的左偏树
//  * 节点u的左偏树 = 节点u自己的非树边左偏树 + 节点u在最短路树上的父亲的左偏树
//  * 课上重点解释了这么做的意义
//  */
// void mergeRoad(){
//     cntd = cnth = 0;
//     for(int i = 1; i <= n; i++){
//         heapAdd(i, dis[i]);
//     }
//     dist[0] = -1;
//     while(!heapEmpty()){
//         int top = heapPop();
//         int u = key[top];
//         for(int e = headg[u], v; e != 0; e = nextg[e]){
//             v = tog[e];
//             // path[u]既是边在反图中的编号，也是边在正图中的编号
//             // 因为正反图同步建立，边的正图编号 == 边的反图编号
//             if(e != path[u]){
//                 // 计算选择这条非树边的增量成本
//                 rt[u] = merge(rt[u], init(v, weightg[e] + dis[v] - dis[u]));
//             }
//         }
//         if(path[u] != 0){
//             // 合并当前节点与父节点的左偏树
//             rt[u] = merge(rt[u], rt[tog[path[u]]]);
//         }
//     }
// }
// 
// /**
//  * 从路费第1小的方案开始，逐渐找到第2小、第3小...
//  * 看看money能够覆盖几次旅行，返回旅行的次数
//  * @return 旅行次数
//  */
// int expand(){
//     int ans = 0;
//     // 扣除最短路径的费用
//     money -= dis[1];
//     if(money >= 0){
//         ans++;
//         cntd = cnth = 0;
//         if(rt[1] != 0){
//             // 开始阶段
//             // 1号节点左偏树的堆顶，代表增加代价最小的非树边，放入决策堆
//             // 目前路通方案的路费，同步放入
//             heapAdd(rt[1], dis[1] + cost[rt[1]]);
//         }
//         while(!heapEmpty()){
//             int top = heapPop();
//             int h = key[top];
//             double w = val[top];
//             // 扣除当前路径的费用
//             money -= w;
//             if(money < 0){
//                 break;
//             }
//             ans++;
//             // 当前选择的非树边，被左偏树上的左儿子替换
//             if(ls[h] != 0){
//                 heapAdd(ls[h], w - cost[h] + cost[ls[h]]);
//             }
//             // 当前选择的非树边，被左偏树上的右儿子替换
//             if(rs[h] != 0){
//                 heapAdd(rs[h], w - cost[h] + cost[rs[h]]);
//             }
//             // 当前选择的非树边，指向节点to[h]，那么从to[h]的左偏树里，新增一个最优的非树边
//             if(to[h] != 0 && rt[to[h]] != 0){
//                 heapAdd(rt[to[h]], w + cost[rt[to[h]]]);
//             }
//         }
//     }
//     return ans;
// }
// 
// /**
//  * 主函数
//  * 输入格式：
//  * 第一行包含三个整数n、m和money，分别表示节点数、边数和钱数
//  * 接下来m行，每行包含三个整数u、v和w，表示从节点u到节点v有一条边权为w的边
//  * 输出格式：
//  * 输出一个整数，表示能进行的旅行次数
//  */
// int main(){
//     ios::sync_with_stdio(false);
//     cin.tie(NULL);
//     cin >> n >> m >> money;
//     int u, v;
//     double w;
//     for(int i = 1; i <= m; i++){
//         cin >> u >> v >> w;
//         // 题目说了，一旦到达节点n，旅行立刻停止
//         // 所以从节点n出发的边，一律忽略
//         if(u != n){
//             addEdgeG(u, v, w);
//             addEdgeR(v, u, w);
//         }
//     }
//     dijkstra();
//     mergeRoad();
//     int ans = expand();
//     cout << ans << endl;
//     return 0;
// }