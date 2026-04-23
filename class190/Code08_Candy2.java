package class190; // 定义包名为class190

// 糖果，C++版
// 一共n个人，每人至少得到一个糖果，一共k条要求，要求有5种类型
// 1 u v : u的糖果 == v的糖果    2 u v : u的糖果 < v的糖果
// 3 u v : u的糖果 >= v的糖果    4 u v : u的糖果 > v的糖果
// 5 u v : u的糖果 <= v的糖果
// 如果无法满足所有要求打印-1，否则打印需要的最少糖果数
// 1 <= n、k <= 10^5
// 测试链接 : https://www.luogu.com.cn/problem/P3275
// 如下实现是C++的版本，C++版本和java版本逻辑完全一样
// 提交如下代码，可以通过所有测试用例

//#include <bits/stdc++.h> // C++标准库头文件
//
//using namespace std; // 使用标准命名空间
//
//using ll = long long; // 定义long long别名为ll
//
//const int MAXN = 100001; // 定义最大节点数常量，最多100000个人
//const int MAXM = 200001; // 定义最大边数常量，最多200000条边
//int n, k, m; // n为人数，k为要求数，m为实际建边数
//int a[MAXM]; // 存储建边后的起点数组
//int b[MAXM]; // 存储建边后的终点数组
//int c[MAXM]; // 存储建边后的边权数组
//
//int head[MAXN]; // 邻接表的头指针数组
//int nxt[MAXM]; // 邻接表的next数组
//int to[MAXM]; // 邻接表的to数组
//int weight[MAXM]; // 邻接表的边权数组（表示糖果数量约束）
//int cntg; // 图的边计数器
//
//int dfn[MAXN]; // Tarjan算法中的深度优先序号数组
//int low[MAXN]; // Tarjan算法中的low值数组
//int cntd; // dfn序号计数器
//
//int sta[MAXN]; // Tarjan算法中的节点栈
//int top; // 栈顶指针
//
//int belong[MAXN]; // 记录每个人属于哪个SCC
//int sccSiz[MAXN]; // 记录每个SCC的大小
//int sccCnt; // 强连通分量计数器
//
//int indegree[MAXN]; // 缩点后DAG中每个SCC的入度
//ll dp[MAXN]; // 动态规划数组，dp[i]表示SCC i的最少糖果数
//
//void addEdge(int u, int v, int w) { // 添加一条从u到v、权值为w的有向边
//    nxt[++cntg] = head[u]; // 新边的next指向u原来的第一条边
//    to[cntg] = v; // 新边指向节点v
//    weight[cntg] = w; // 设置新边的权重为w（表示v的糖果数至少为u的糖果数+w）
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
//        sccSiz[sccCnt] = 0; // 初始化该SCC的大小为0
//        int pop; // 弹出的节点
//        do { // 弹出栈中属于该SCC的所有节点
//            pop = sta[top--]; // 弹出栈顶
//            belong[pop] = sccCnt; // 标记所属SCC
//            sccSiz[sccCnt]++; // 该SCC的人数加1
//        } while (pop != u); // 直到弹出u
//    }
//}
//
//bool condense() { // 缩点操作，构建缩点后的DAG，检查约束是否合法
//    cntg = 0; // 重置边计数器
//    for (int i = 1; i <= sccCnt; i++) { // 初始化SCC的头指针
//        head[i] = 0; // 清零
//    }
//    for (int i = 1; i <= m; i++) { // 遍历所有建好的边
//        int scc1 = belong[a[i]]; // 边起点的SCC编号
//        int scc2 = belong[b[i]]; // 边终点的SCC编号
//        int w = c[i]; // 边权（糖果数量差）
//        // 如果两个端点在同一SCC且边权为正（表示存在正权环，约束无法满足）
//        if (scc1 == scc2 && w > 0) {
//            return false; // 返回false表示无法满足所有要求
//        }
//        if (scc1 != scc2) { // 如果边连接不同SCC
//            indegree[scc2]++; // 终点SCC的入度加1
//            addEdge(scc1, scc2, w); // 在DAG中添加带权边
//        }
//    }
//    return true; // 返回true表示约束合法
//}
//
//ll dpOnDAG() { // 在缩点后的DAG上进行动态规划，求最少糖果数
//    for (int u = sccCnt; u > 0; u--) { // 按SCC编号逆序进行DP（拓扑序）
//        if (indegree[u] == 0) { // 如果u是入度为0的起点
//            dp[u] = 1; // 起点的最少糖果数为1（每人至少1个糖果）
//        }
//        for (int e = head[u]; e > 0; e = nxt[e]) { // 遍历u的所有出边
//            int v = to[e]; // 邻接SCC v
//            int w = weight[e]; // 边权（糖果数量差约束）
//            // 更新v的最少糖果数：v的糖果数至少为u的糖果数+w
//            dp[v] = max(dp[v], dp[u] + w);
//        }
//    }
//    ll ans = 0; // 初始化答案
//    for (int i = 1; i <= sccCnt; i++) { // 遍历所有SCC
//        ans += dp[i] * sccSiz[i]; // 累加每个SCC的糖果数（SCC糖果数 × SCC人数）
//    }
//    return ans; // 返回总糖果数
//}
//
//int main() { // 主程序入口
//    ios::sync_with_stdio(false); // 关闭IO同步，加速输入输出
//    cin.tie(nullptr); // 解除cin和cout绑定
//    cin >> n >> k; // 读取人数和要求数
//    m = 0; // 初始化实际建边数为0
//    for (int i = 1, op, u, v; i <= k; i++) { // 读取每条要求
//        cin >> op >> u >> v; // 读取要求类型和两个人
//        if (op == 1) { // u的糖果 == v的糖果
//            a[++m] = u; b[m] = v; c[m] = 0; // 添加u->v，边权0
//            a[++m] = v; b[m] = u; c[m] = 0; // 添加v->u，边权0
//        } else if (op == 2) { // u的糖果 < v的糖果
//            a[++m] = u; b[m] = v; c[m] = 1; // 添加u->v，边权1
//        } else if (op == 3) { // u的糖果 >= v的糖果
//            a[++m] = v; b[m] = u; c[m] = 0; // 添加v->u，边权0
//        } else if (op == 4) { // u的糖果 > v的糖果
//            a[++m] = v; b[m] = u; c[m] = 1; // 添加v->u，边权1
//        } else { // u的糖果 <= v的糖果
//            a[++m] = u; b[m] = v; c[m] = 0; // 添加u->v，边权0
//        }
//    }
//    for (int i = 1; i <= m; i++) { // 将所有建好的边添加到图中
//        addEdge(a[i], b[i], c[i]);
//    }
//    for (int i = 1; i <= n; i++) { // 对所有未访问的人执行Tarjan
//        if (dfn[i] == 0) { // 如果人i未被访问
//            tarjan(i); // 执行递归版
//        }
//    }
//    bool check = condense(); // 执行缩点操作并检查约束合法性
//    if (!check) { // 如果约束无法满足
//        cout << -1 << "\n"; // 输出-1
//    } else { // 如果约束可以满足
//        ll ans = dpOnDAG(); // 在DAG上DP求解最少糖果数
//        cout << ans << "\n"; // 输出答案
//    }
//    return 0; // 程序正常结束
//}
