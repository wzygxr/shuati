package class190; // 定义包名为class190

// 缩点结合动态规划模版题，C++版
// 给定一张n个点，m条边的有向图，每个点给定非负点权
// 如果重复经过一个点，点权只获得一次
// 找到一条路径，使得点权累加和最大，打印这个值
// 1 <= n <= 10^4
// 1 <= m <= 10^5
// 测试链接 : https://www.luogu.com.cn/problem/P3387
// 如下实现是C++的版本，C++版本和java版本逻辑完全一样
// 提交如下代码，可以通过所有测试用例

//#include <bits/stdc++.h> // C++标准库头文件，包含所有常用库
//
//using namespace std; // 使用标准命名空间
//
//const int MAXN = 10001; // 定义最大节点数常量，最多10000个节点
//const int MAXM = 100001; // 定义最大边数常量，最多100000条边
//int n, m; // n为实际节点数，m为实际边数
//
//int arr[MAXN]; // 存储每个节点的点权值
//int a[MAXM]; // 存储每条边的起点
//int b[MAXM]; // 存储每条边的终点
//
//int head[MAXN]; // 邻接表的头指针数组，head[i]表示节点i的第一条边
//int nxt[MAXM]; // 邻接表的next数组，存储下一条边的索引
//int to[MAXM]; // 邻接表的to数组，存储边指向的节点
//int cntg; // 图的边计数器，用于添加边时分配索引
//
//int dfn[MAXN]; // Tarjan算法中记录每个节点的深度优先序号(discovery time)
//int low[MAXN]; // Tarjan算法中记录每个节点能到达的最小dfn值
//int cntd; // dfn序号计数器，每次访问新节点时递增
//
//int sta[MAXN]; // Tarjan算法中用于存储当前DFS路径上的节点栈
//int top; // 栈顶指针，指向栈顶元素的位置
//
//int belong[MAXN]; // 记录每个节点属于哪个强连通分量(SCC)
//int sum[MAXN]; // 记录每个SCC内部所有节点的点权之和
//int sccCnt; // 强连通分量的计数器
//
//int indegree[MAXN]; // 缩点后DAG中每个SCC的入度
//int que[MAXN]; // 拓扑排序中使用的队列数组
//int dp[MAXN]; // 动态规划数组，dp[i]表示到达SCC i的最大点权和
//
//void addEdge(int u, int v) { // 添加一条从u到v的有向边
//    nxt[++cntg] = head[u]; // 新边的next指向u节点原来的第一条边
//    to[cntg] = v; // 新边指向节点v
//    head[u] = cntg; // 更新u节点的头指针指向新边
//}
//
//void tarjan(int u) { // Tarjan算法求强连通分量，u为当前节点
//    dfn[u] = low[u] = ++cntd; // 初始化u的dfn和low值为当前序号
//    sta[++top] = u; // 将u压入栈中
//    for (int e = head[u]; e > 0; e = nxt[e]) { // 遍历u的所有邻接边
//        int v = to[e]; // v为u通过边e到达的节点
//        if (dfn[v] == 0) { // 如果v未被访问过（dfn为0表示未访问）
//            tarjan(v); // 递归访问v
//            low[u] = min(low[u], low[v]); // u的low值取min(当前值, v的low值)
//        } else { // 如果v已被访问过
//            if (belong[v] == 0) { // 如果v还在栈中（belong为0表示未出栈）
//                low[u] = min(low[u], dfn[v]); // 用v的dfn更新u的low值
//            }
//        }
//    }
//    if (dfn[u] == low[u]) { // 如果u的dfn等于low，说明u是一个SCC的根节点
//        sccCnt++; // 发现一个新的强连通分量
//        int pop; // 用于存储弹出的节点
//        do { // 开始弹出栈中属于该SCC的所有节点
//            pop = sta[top--]; // 弹出栈顶节点
//            belong[pop] = sccCnt; // 标记pop属于当前SCC
//            sum[sccCnt] += arr[pop]; // 将该节点的点权累加到SCC的总权值中
//        } while (pop != u); // 直到弹出u本身为止
//    }
//}
//
//void condense() { // 缩点操作：将SCC缩成单个节点，构建DAG
//    cntg = 0; // 重置边计数器，准备重新建图
//    for (int i = 1; i <= sccCnt; i++) { // 初始化新图的每个SCC节点的头指针
//        head[i] = 0; // 清零头指针，表示没有边
//    }
//    for (int i = 1; i <= m; i++) { // 遍历原图的每条边
//        int scc1 = belong[a[i]]; // 获取边i起点的SCC编号
//        int scc2 = belong[b[i]]; // 获取边i终点的SCC编号
//        if (scc1 != scc2) { // 如果两个端点属于不同的SCC，则在DAG中添加边
//            indegree[scc2]++; // 终点SCC的入度加1
//            addEdge(scc1, scc2); // 添加从scc1到scc2的有向边
//        }
//    }
//}
//
//int topo() { // 使用拓扑排序进行DP求解最大点权和
//    int l = 1, r = 0; // 初始化队列的左右指针，l为队头，r为队尾
//    for (int i = 1; i <= sccCnt; i++) { // 将所有入度为0的SCC加入队列
//        if (indegree[i] == 0) { // 如果SCC i的入度为0
//            dp[i] = sum[i]; // 初始化dp[i]为其自身的点权和
//            que[++r] = i; // 将i加入队列尾部
//        }
//    }
//    while (l <= r) { // 当队列不为空时继续处理
//        int u = que[l++]; // 取出队头元素u
//        for (int e = head[u]; e > 0; e = nxt[e]) { // 遍历u的所有出边
//            int v = to[e]; // v为u指向的节点
//            dp[v] = max(dp[v], dp[u] + sum[v]); // 更新v的dp值，取最大值
//            if (--indegree[v] == 0) { // 将v的入度减1，如果变为0则加入队列
//                que[++r] = v; // 将v加入队列
//            }
//        }
//    }
//    int ans = 0; // 初始化答案为0
//    for (int i = 1; i <= sccCnt; i++) { // 遍历所有SCC，找到最大dp值
//        ans = max(ans, dp[i]); // 更新最大值
//    }
//    return ans; // 返回最大点权和
//}
//
//int dpOnDAG() { // 直接在DAG上进行DP，不依赖拓扑序
//    for (int u = sccCnt; u > 0; u--) { // 按照SCC编号的逆序进行DP
//        if (indegree[u] == 0) { // 如果u是入度为0的起点
//            dp[u] = sum[u]; // 初始化dp[u]为其自身的点权和
//        }
//        for (int e = head[u]; e > 0; e = nxt[e]) { // 遍历u的所有出边
//            int v = to[e]; // v为u指向的节点
//            dp[v] = max(dp[v], dp[u] + sum[v]); // 更新v的dp值
//        }
//    }
//    int ans = 0; // 初始化答案为0
//    for (int u = 1; u <= sccCnt; u++) { // 遍历所有SCC
//        ans = max(ans, dp[u]); // 更新最大值
//    }
//    return ans; // 返回最大点权和
//}
//
//int main() { // 主程序入口
//    ios::sync_with_stdio(false); // 关闭C++标准IO的同步，加速输入输出
//    cin.tie(nullptr); // 解除cin和cout的绑定，进一步加速
//    cin >> n >> m; // 读取节点数n和边数m
//    for (int i = 1; i <= n; i++) { // 读取每个节点的点权
//        cin >> arr[i]; // 读取节点i的点权值
//    }
//    for (int i = 1; i <= m; i++) { // 读取每条边的信息
//        cin >> a[i] >> b[i]; // 读取第i条边的起点和终点
//        addEdge(a[i], b[i]); // 添加边到原图中
//    }
//    for (int i = 1; i <= n; i++) { // 对所有未访问的节点执行Tarjan算法
//        if (dfn[i] == 0) { // 如果节点i未被访问过（dfn为0）
//            tarjan(i); // 从节点i开始执行Tarjan算法
//        }
//    }
//    condense(); // 执行缩点操作，构建DAG
//    // int ans = topo(); // 可以使用拓扑排序求解（此行被注释）
//    int ans = dpOnDAG(); // 使用直接DP方法求解最大点权和
//    cout << ans << "\n"; // 输出答案
//    return 0; // 程序正常结束
//}
