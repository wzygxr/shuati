package class190; // 定义包名为class190

// 缩点最短路，C++版
// 给定一张n个点，m条边的有向图，每条边有非负边权
// 如果两点属同一个强连通分量，认为彼此到达的代价为0
// 找到从1号点到n号点的路径，打印最小的边权累加和，如果不能到达打印-1
// 1 <= n <= 2 * 10^5
// 1 <= m <= 10^6
// 测试链接 : https://www.luogu.com.cn/problem/P2169
// 如下实现是C++的版本，C++版本和java版本逻辑完全一样
// 提交如下代码，可以通过所有测试用例

//#include <bits/stdc++.h> // C++标准库头文件
//
//using namespace std; // 使用标准命名空间
//
//struct Node { // 定义优先队列节点结构体
//    int x, cost; // x为节点编号，cost为距离
//    bool operator<(const Node &other) const { // 重载小于运算符
//        return cost > other.cost; // 小顶堆，距离小的优先级高
//    }
//};
//
//const int MAXN = 200001; // 定义最大节点数常量，最多200000个节点
//const int MAXM = 1000001; // 定义最大边数常量，最多1000000条边
//const int INF = 1000000001; // 定义无穷大常量
//int n, m; // n为节点数，m为边数
//
//int a[MAXM]; // 存储每条边的起点
//int b[MAXM]; // 存储每条边的终点
//int val[MAXM]; // 存储每条边的权值
//
//int head[MAXN]; // 邻接表的头指针数组
//int nxt[MAXM]; // 邻接表的next数组
//int to[MAXM]; // 邻接表的to数组
//int weight[MAXM]; // 邻接表的边权数组
//int cntg; // 图的边计数器
//
//int dfn[MAXN]; // Tarjan算法中的深度优先序号数组
//int low[MAXN]; // Tarjan算法中的low值数组
//int cntd; // dfn序号计数器
//
//int sta[MAXN]; // Tarjan算法中的节点栈
//int top; // 栈顶指针
//
//int belong[MAXN]; // 记录每个节点属于哪个SCC
//int sccCnt; // 强连通分量计数器
//
//int dist[MAXN]; // 最短距离数组，dist[i]表示从起点到SCC i的最短距离
//bool vis[MAXN]; // 标记数组，vis[i]表示SCC i是否已确定最短距离
//priority_queue<Node> heap; // 优先队列，用于Dijkstra算法
//
//void addEdge(int u, int v, int w) { // 添加一条从u到v、权值为w的有向边
//    nxt[++cntg] = head[u]; // 新边的next指向u原来的第一条边
//    to[cntg] = v; // 新边指向节点v
//    weight[cntg] = w; // 设置新边的权重为w
//    head[u] = cntg; // 更新u的头指针指向新边
//}
//
//void tarjan(int u) { // 递归版Tarjan算法
//    dfn[u] = low[u] = ++cntd; // 初始化dfn和low值
//    sta[++top] = u; // 将u压入栈
//    for (int e = head[u]; e > 0; e = nxt[e]) { // 遍历u的所有邻接边
//        int v = to[e]; // 获取邻接节点v
//        if (dfn[v] == 0) { // 如果v未被访问
//            tarjan(v); // 递归访问v
//            low[u] = min(low[u], low[v]); // 更新low值
//        } else { // 如果v已被访问
//            if (belong[v] == 0) { // 如果v还在栈中
//                low[u] = min(low[u], dfn[v]); // 更新low值
//            }
//        }
//    }
//    if (dfn[u] == low[u]) { // 如果u是SCC的根节点
//        sccCnt++; // SCC计数加1
//        int pop; // 弹出的节点
//        do { // 弹出栈中属于该SCC的所有节点
//            pop = sta[top--]; // 弹出栈顶
//            belong[pop] = sccCnt; // 标记所属SCC
//        } while (pop != u); // 直到弹出u
//    }
//}
//
//void condense() { // 缩点操作，构建缩点后的DAG
//    cntg = 0; // 重置边计数器
//    for (int i = 1; i <= sccCnt; i++) { // 初始化SCC的头指针
//        head[i] = 0; // 清零
//    }
//    for (int i = 1; i <= m; i++) { // 遍历所有边
//        int scc1 = belong[a[i]]; // 边起点的SCC编号
//        int scc2 = belong[b[i]]; // 边终点的SCC编号
//        // 如果两个端点都可达且属于不同SCC，则添加带权边
//        if (scc1 > 0 && scc2 > 0 && scc1 != scc2) {
//            addEdge(scc1, scc2, val[i]); // 在DAG中添加边，权值为原边权
//        }
//    }
//}
//
//int dijkstra() { // Dijkstra算法求最短路径
//    for (int i = 1; i <= sccCnt; i++) { // 初始化距离数组和访问标记
//        dist[i] = INF; // 距离初始化为无穷大
//        vis[i] = false; // 访问标记初始化为false
//    }
//    dist[belong[1]] = 0; // 起点（1号点所在SCC）的距离为0
//    heap.push(Node{belong[1], 0}); // 将起点加入优先队列
//    while (!heap.empty()) { // 当优先队列不为空时循环
//        Node cur = heap.top(); // 取出距离最小的节点
//        heap.pop(); // 弹出队首
//        int u = cur.x; // 当前节点
//        int d = cur.cost; // 当前距离
//        if (!vis[u]) { // 如果该节点未被处理过
//            vis[u] = true; // 标记为已处理
//            for (int e = head[u]; e > 0; e = nxt[e]) { // 遍历u的所有出边
//                int v = to[e]; // 邻接节点v
//                int w = weight[e]; // 边权
//                // 如果v未被处理且经过u到v的距离更短
//                if (!vis[v] && dist[v] > d + w) {
//                    dist[v] = d + w; // 更新v的最短距离
//                    heap.push(Node{v, dist[v]}); // 将v加入优先队列
//                }
//            }
//        }
//    }
//    return dist[belong[n]]; // 返回终点（n号点所在SCC）的最短距离
//}
//
//int main() { // 主程序入口
//    ios::sync_with_stdio(false); // 关闭IO同步，加速输入输出
//    cin.tie(nullptr); // 解除cin和cout绑定
//    cin >> n >> m; // 读取节点数和边数
//    for (int i = 1; i <= m; i++) { // 读取每条边
//        cin >> a[i] >> b[i] >> val[i]; // 读取起点、终点、边权
//        addEdge(a[i], b[i], val[i]); // 添加边到原图
//    }
//    tarjan(1); // 执行递归版Tarjan算法
//    if (belong[n] == 0) { // 如果n号点不可达
//        cout << -1 << "\n"; // 输出-1表示不可达
//    } else { // 如果n号点可达
//        condense(); // 执行缩点操作
//        int ans = dijkstra(); // 使用Dijkstra算法求最短路径
//        cout << ans << "\n"; // 输出答案
//    }
//    return 0; // 程序正常结束
//}
