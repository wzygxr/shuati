package class190; // 定义包名为class190

// 劫掠计划，C++版
// 一共有n个城市，给定m条有向道路，每个城市给定拥有的钱数
// 给定起点城市s，给定p个有酒吧的城市，有酒吧的城市才能作为终点
// 路线必须从s出发到任意终点停止，重复经过城市的话，钱仅获得一次
// 题目保证一定存在这样的路线，打印能获得的最大钱数
// 1 <= n、m <= 5 * 10^5
// 测试链接 : https://www.luogu.com.cn/problem/P3627
// 如下实现是C++的版本，C++版本和java版本逻辑完全一样
// 提交如下代码，可以通过所有测试用例

//#include <bits/stdc++.h> // C++标准库头文件
//
//using namespace std; // 使用标准命名空间
//
//const int MAXN = 500001; // 定义最大节点数常量，最多500000个节点
//const int MAXM = 500001; // 定义最大边数常量，最多500000条边
//const int INF = 1000000001; // 定义无穷大常量
//int n, m, s, p; // n为城市数，m为道路数，s为起点，p为有酒吧的城市数
//
//int money[MAXN]; // 存储每个城市的钱数
//bool isBar[MAXN]; // 标记每个城市是否有酒吧
//int a[MAXM]; // 存储每条道路的起点
//int b[MAXM]; // 存储每条道路的终点
//
//int head[MAXN]; // 邻接表的头指针数组
//int nxt[MAXM]; // 邻接表的next数组
//int to[MAXM]; // 邻接表的to数组
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
//int sum[MAXN]; // 记录每个SCC内部所有城市的钱数总和
//bool hasBar[MAXN]; // 记录每个SCC内部是否有酒吧
//int sccCnt; // 强连通分量计数器
//
//int dp[MAXN]; // 动态规划数组，dp[i]表示到达SCC i能获得的最大钱数
//
//void addEdge(int u, int v) { // 添加一条从u到v的有向边
//    nxt[++cntg] = head[u]; // 新边的next指向u原来的第一条边
//    to[cntg] = v; // 新边指向节点v
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
//        sum[sccCnt] = 0; // 初始化该SCC的钱数总和为0
//        hasBar[sccCnt] = false; // 初始化该SCC无酒吧
//        int pop; // 弹出的节点
//        do { // 弹出栈中属于该SCC的所有节点
//            pop = sta[top--]; // 弹出栈顶
//            belong[pop] = sccCnt; // 标记所属SCC
//            sum[sccCnt] += money[pop]; // 累加该城市的钱数
//            hasBar[sccCnt] |= isBar[pop]; // 标记该SCC是否有酒吧
//        } while (pop != u); // 直到弹出u
//    }
//}
//
//void condense() { // 缩点操作，构建缩点后的DAG
//    cntg = 0; // 重置边计数器
//    for (int i = 1; i <= sccCnt; i++) { // 初始化SCC的头指针
//        head[i] = 0; // 清零
//    }
//    for (int i = 1; i <= m; i++) { // 遍历所有道路
//        int scc1 = belong[a[i]]; // 道路起点的SCC编号
//        int scc2 = belong[b[i]]; // 道路终点的SCC编号
//        // 如果两个端点都可达且属于不同SCC，则添加边
//        if (scc1 > 0 && scc2 > 0 && scc1 != scc2) {
//            addEdge(scc1, scc2); // 在DAG中添加边
//        }
//    }
//}
//
//int dpOnDAG() { // 在缩点后的DAG上进行动态规划
//    for (int u = 1; u <= sccCnt; u++) { // 初始化所有SCC的dp值为负无穷
//        dp[u] = -INF; // 设为负无穷表示不可达
//    }
//    dp[belong[s]] = sum[belong[s]]; // 起点SCC的dp值为其钱数总和
//    for (int u = sccCnt; u > 0; u--) { // 按SCC编号逆序进行DP（拓扑序）
//        for (int e = head[u]; e > 0; e = nxt[e]) { // 遍历u的所有出边
//            int v = to[e]; // 邻接SCC v
//            // 更新v的dp值：从u到v，可以获得u的dp值+v的钱数总和
//            dp[v] = max(dp[v], dp[u] + sum[v]);
//        }
//    }
//    int ans = 0; // 初始化答案
//    for (int u = 1; u <= sccCnt; u++) { // 遍历所有SCC
//        if (hasBar[u]) { // 如果该SCC有酒吧（可以作为终点）
//            ans = max(ans, dp[u]); // 更新最大钱数
//        }
//    }
//    return ans; // 返回能获得的最大钱数
//}
//
//int main() { // 主程序入口
//    ios::sync_with_stdio(false); // 关闭IO同步，加速输入输出
//    cin.tie(nullptr); // 解除cin和cout绑定
//    cin >> n >> m; // 读取城市数和道路数
//    for (int i = 1; i <= m; i++) { // 读取每条道路
//        cin >> a[i] >> b[i]; // 读取起点和终点
//        addEdge(a[i], b[i]); // 添加边到原图
//    }
//    for (int i = 1; i <= n; i++) { // 读取每个城市的钱数
//        cin >> money[i]; // 城市i的钱数
//    }
//    cin >> s >> p; // 读取起点城市和有酒吧的城市数
//    for (int i = 1, x; i <= p; i++) { // 读取有酒吧的城市
//        cin >> x; // 酒吧城市编号
//        isBar[x] = true; // 标记该城市有酒吧
//    }
//    tarjan(s); // 执行Tarjan算法
//    condense(); // 执行缩点操作
//    int ans = dpOnDAG(); // 在DAG上DP求解
//    cout << ans << "\n"; // 输出答案
//    return 0; // 程序正常结束
//}
