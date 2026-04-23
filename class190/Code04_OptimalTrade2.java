package class190; // 定义包名为class190

// 最优贸易，C++版
// 一共有n个城市，每个城市给定水晶球的销售价格，给定m条道路
// 格式 a b op : op为1代表a到b的单向路，否则表示双向路
// 你要从1号城市出发，可以任选道路，最终来到n号城市，沿途可以买卖一次水晶球
// 在途中你可以任选一座城市买入，可以立即卖出，或者在之后的任何城市卖出
// 如果从1号城市能到达n号城市，打印挣到的最大钱数，如果不能到达打印0
// 1 <= n <= 10^5
// 1 <= m <= 5 * 10^5
// 测试链接 : https://www.luogu.com.cn/problem/P1073
// 如下实现是C++的版本，C++版本和java版本逻辑完全一样
// 提交如下代码，可以通过所有测试用例

//#include <bits/stdc++.h> // C++标准库头文件
//
//using namespace std; // 使用标准命名空间
//
//const int MAXN = 100001; // 定义最大节点数常量，最多100000个节点
//const int MAXM = 500001; // 定义最大边数常量，最多500000条边
//const int INF = 1000000001; // 定义无穷大常量
//int n, m; // n为城市数，m为道路数
//
//int val[MAXN]; // 存储每个城市水晶球的销售价格
//int a[MAXM]; // 存储每条道路的起点
//int b[MAXM]; // 存储每条道路的终点
//
//int head[MAXN]; // 邻接表的头指针数组
//int nxt[MAXM << 1]; // 邻接表的next数组（双向边所以乘以2）
//int to[MAXM << 1]; // 邻接表的to数组
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
//int sccMin[MAXN]; // 记录每个SCC内部的最低价格
//int sccMax[MAXN]; // 记录每个SCC内部的最高价格
//int sccCnt; // 强连通分量计数器
//
//int premin[MAXN]; // 记录到达每个SCC路径上的最低买入价
//int dp[MAXN]; // 动态规划数组，dp[i]表示到达SCC i的最大利润
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
//        sccMin[sccCnt] = INF; // 初始化该SCC的最低价格为无穷大
//        sccMax[sccCnt] = -INF; // 初始化该SCC的最高价格为负无穷
//        int pop; // 弹出的节点
//        do { // 弹出栈中属于该SCC的所有节点
//            pop = sta[top--]; // 弹出栈顶
//            belong[pop] = sccCnt; // 标记所属SCC
//            sccMin[sccCnt] = min(sccMin[sccCnt], val[pop]); // 更新最低价格
//            sccMax[sccCnt] = max(sccMax[sccCnt], val[pop]); // 更新最高价格
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
//    for (int u = 1; u <= sccCnt; u++) { // 初始化DP数组
//        premin[u] = INF; // 路径最低买入价初始为无穷大
//        dp[u] = -INF; // 最大利润初始为负无穷
//    }
//    int s = belong[1]; // 起点（1号城市）所在的SCC
//    premin[s] = sccMin[s]; // 起点的路径最低买入价为该SCC的最低价格
//    dp[s] = sccMax[s] - sccMin[s]; // 起点的最大利润为该SCC内的最高差价
//    for (int u = sccCnt; u > 0; u--) { // 按SCC编号逆序进行DP
//        for (int e = head[u]; e > 0; e = nxt[e]) { // 遍历u的所有出边
//            int v = to[e]; // 邻接SCC v
//            // 更新v的路径最低买入价：取当前值、u的最低买入价、v的最低价格的最小值
//            premin[v] = min(premin[v], min(premin[u], sccMin[v]));
//            // 更新v的最大利润：取当前值、u的最大利润、v的最高价格减路径最低买入价的最大值
//            dp[v] = max(dp[v], max(dp[u], sccMax[v] - premin[v]));
//        }
//    }
//    return dp[belong[n]]; // 返回终点（n号城市）所在SCC的最大利润
//}
//
//int main() { // 主程序入口
//    ios::sync_with_stdio(false); // 关闭IO同步，加速输入输出
//    cin.tie(nullptr); // 解除cin和cout绑定
//    cin >> n >> m; // 读取城市数和道路数
//    for (int i = 1; i <= n; i++) { // 读取每个城市的价格
//        cin >> val[i]; // 城市i的水晶球价格
//    }
//    for (int i = 1, op; i <= m; i++) { // 读取每条道路
//        cin >> a[i] >> b[i] >> op; // 读取起点、终点、操作类型
//        if (op == 1) { // 单向路
//            addEdge(a[i], b[i]); // 只添加一条边
//        } else { // 双向路
//            addEdge(a[i], b[i]); // 添加正向边
//            addEdge(b[i], a[i]); // 添加反向边
//        }
//    }
//    tarjan(1); // 执行Tarjan算法
//    if (belong[n] == 0) { // 如果n号城市不可达
//        cout << 0 << "\n"; // 输出0
//    } else { // 如果n号城市可达
//        condense(); // 执行缩点操作
//        int ans = dpOnDAG(); // 在DAG上DP求解
//        cout << ans << "\n"; // 输出答案
//    }
//    return 0; // 程序正常结束
//}
