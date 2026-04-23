package class190; // 定义包名为class190

// 采蘑菇，C++版
// 给定一张n个点，m条边的有向图，每条边有初始收益、恢复系数两种边权
// 初始收益为非负整数，恢复系数范围[0, 0.8]，并且最多有一位小数
// 比如，如果重复走过一条边，该边的初始收益为10，恢复系数为0.6
// 那么依次获得的收益为，10、6、3、1、0，随后重复经过就没有收益了
// 给定起点s，找到一条必须从s出发的路径，打印收益累加和的最大值
// 1 <= n <= 8 * 10^4
// 1 <= m <= 2 * 10^5
// 测试链接 : https://www.luogu.com.cn/problem/P2656
// 如下实现是C++的版本，C++版本和java版本逻辑完全一样
// 提交如下代码，可以通过所有测试用例

//#include <bits/stdc++.h> // C++标准库头文件
//
//using namespace std; // 使用标准命名空间
//
//const int MAXN = 80001; // 定义最大节点数常量，最多80000个节点
//const int MAXM = 200001; // 定义最大边数常量，最多200000条边
//const int INF = 1000000001; // 定义无穷大常量，用于初始化dp数组
//int n, m, s; // n为节点数，m为边数，s为起点
//
//int a[MAXM]; // 存储每条边的起点
//int b[MAXM]; // 存储每条边的终点
//int init[MAXM]; // 存储每条边的初始收益
//int recover[MAXM]; // 存储每条边的恢复系数(乘以10后的整数)
//
//int head[MAXN]; // 邻接表的头指针数组
//int nxt[MAXM]; // 邻接表的next数组
//int to[MAXM]; // 邻接表的to数组
//int weight[MAXM]; // 邻接表的边权数组（用于缩点后的图）
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
//int dp[MAXN]; // 动态规划数组，dp[i]表示到达SCC i的最大收益
//
//void addEdge(int u, int v, int w) { // 添加一条带权边
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
//        int popv; // 弹出的节点
//        do { // 弹出栈中属于该SCC的所有节点
//            popv = sta[top--]; // 弹出栈顶
//            belong[popv] = sccCnt; // 标记所属SCC
//        } while (popv != u); // 直到弹出u
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
//        if (scc1 > 0 && scc2 > 0) { // 如果两个端点都有效（可达）
//            int val = init[i]; // 边的初始收益
//            int rec = recover[i]; // 边的恢复系数
//            if (scc1 == scc2) { // 如果边在同一个SCC内（形成环）
//                while (val > 0) { // 计算环的收益总和
//                    sum[scc1] += val; // 累加当前收益
//                    val = val * rec / 10; // 按恢复系数计算下次收益
//                }
//            } else { // 如果边连接不同SCC
//                addEdge(scc1, scc2, val); // 在DAG中添加带权边
//            }
//        }
//    }
//}
//
//int dpOnDAG() { // 在缩点后的DAG上进行动态规划
//    for (int u = 1; u <= sccCnt; u++) { // 初始化所有SCC的dp值为负无穷
//        dp[u] = -INF; // 设为负无穷表示不可达
//    }
//    dp[belong[s]] = sum[belong[s]]; // 起点的dp值为所在SCC的环收益
//    for (int u = sccCnt; u > 0; u--) { // 按SCC编号逆序进行DP（拓扑序）
//        for (int e = head[u]; e > 0; e = nxt[e]) { // 遍历u的所有出边
//            int v = to[e]; // 邻接SCC v
//            int w = weight[e]; // 边权（初始收益）
//            // 更新v的dp值：从u到v，获得u的dp值+边权+v的环收益
//            dp[v] = max(dp[v], dp[u] + w + sum[v]);
//        }
//    }
//    int ans = 0; // 初始化答案
//    for (int u = 1; u <= sccCnt; u++) { // 遍历所有SCC找最大值
//        ans = max(ans, dp[u]); // 更新最大收益
//    }
//    return ans; // 返回最大收益
//}
//
//int main() { // 主程序入口
//    ios::sync_with_stdio(false); // 关闭IO同步，加速输入输出
//    cin.tie(nullptr); // 解除cin和cout绑定
//    cin >> n >> m; // 读取节点数和边数
//    double rec; // 临时变量存储恢复系数
//    for (int i = 1; i <= m; i++) { // 读取每条边的信息
//        cin >> a[i] >> b[i] >> init[i]; // 读取起点、终点、初始收益
//        cin >> rec; // 读取恢复系数
//        recover[i] = (int)(rec * 10); // 转换为整数（乘以10）
//        addEdge(a[i], b[i], 0); // 添加边到原图（边权暂时为0）
//    }
//    cin >> s; // 读取起点
//    tarjan(s); // 执行Tarjan算法
//    condense(); // 执行缩点操作
//    int ans = dpOnDAG(); // 在DAG上DP求解
//    cout << ans << "\n"; // 输出答案
//    return 0; // 程序正常结束
//}
