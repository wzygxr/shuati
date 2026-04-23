package class190; // 定义包名为class190

// 软件安装，C++版
// 一共有n个物品，你的背包能容纳的总重量为m
// 选择某个物品时，重量消耗为当前物品的w，获得收益为当前物品的v
// 给定每个物品最多一件依赖物品，不拿依赖物品无法选择当前物品
// 如果一批物品循环依赖，想选的话就只能都选，有的物品不存在依赖物品
// 打印你能获得的最大收益
// 0 <= n <= 100
// 0 <= m <= 500
// 测试链接 : https://www.luogu.com.cn/problem/P2515
// 如下实现是C++的版本，C++版本和java版本逻辑完全一样
// 提交如下代码，可以通过所有测试用例

//#include <bits/stdc++.h> // C++标准库头文件
//
//using namespace std; // 使用标准命名空间
//
//const int MAXN = 301; // 定义最大节点数常量，最多300个节点（考虑虚拟根节点）
//const int MAXM = 601; // 定义最大容量常量，最多600
//int n, m; // n为物品数，m为背包容量
//int w[MAXN]; // 存储每个物品的重量
//int v[MAXN]; // 存储每个物品的价值
//int depend[MAXN]; // 存储每个物品的依赖物品编号，0表示无依赖
//
//int head[MAXN]; // 邻接表的头指针数组
//int nxt[MAXN]; // 邻接表的next数组
//int to[MAXN]; // 邻接表的to数组
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
//int wsum[MAXN]; // 记录每个SCC的总重量（循环依赖的物品重量和）
//int vsum[MAXN]; // 记录每个SCC的总价值（循环依赖的物品价值和）
//int sccCnt; // 强连通分量计数器
//
//int indegree[MAXN]; // 缩点后DAG中每个SCC的入度
//
//int siz[MAXN]; // 记录树的每个节点的子树大小（按dfn序）
//int weight[MAXN]; // 按dfn序存储每个节点的重量
//int value[MAXN]; // 按dfn序存储每个节点的价值
//int dfnCnt; // 树的dfn序号计数器
//int dp[MAXN][MAXM]; // DP数组，dp[i][j]表示处理到dfn序i，背包容量为j时的最大价值
//
//void addEdge(int a, int b) { // 添加一条从a到b的有向边
//    nxt[++cntg] = head[a]; // 新边的next指向a原来的第一条边
//    to[cntg] = b; // 新边指向节点b
//    head[a] = cntg; // 更新a的头指针指向新边
//}
//
//void tarjan(int u) { // Tarjan算法求强连通分量
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
//            wsum[sccCnt] += w[pop]; // 累加该SCC的总重量
//            vsum[sccCnt] += v[pop]; // 累加该SCC的总价值
//        } while (pop != u); // 直到弹出u
//    }
//}
//
//void condense() { // 缩点操作，构建缩点后的DAG
//    cntg = 0; // 重置边计数器
//    for (int i = 0; i <= sccCnt; i++) { // 初始化SCC的头指针
//        head[i] = 0; // 清零
//    }
//    for (int i = 1; i <= n; i++) { // 遍历所有物品
//        if (depend[i] > 0) { // 如果物品i有依赖
//            int scc1 = belong[depend[i]]; // 依赖物品的SCC编号
//            int scc2 = belong[i]; // 当前物品的SCC编号
//            if (scc1 != scc2) { // 如果属于不同SCC，添加依赖边
//                addEdge(scc1, scc2); // 添加从依赖SCC到当前SCC的边
//                indegree[scc2]++; // 当前SCC的入度加1
//            }
//        }
//    }
//    for (int i = 1; i <= sccCnt; i++) { // 遍历所有SCC
//        if (indegree[i] == 0) { // 如果SCC入度为0（没有依赖）
//            addEdge(0, i); // 从虚拟根节点0添加边到该SCC
//        }
//    }
//}
//
//int dfs(int u) { // 深度优先搜索，按dfn序重新编号并计算子树大小
//    int i = ++dfnCnt; // 分配新的dfn序号
//    siz[i] = 1; // 初始化子树大小为1（自己）
//    weight[i] = wsum[u]; // 记录该节点的重量
//    value[i] = vsum[u]; // 记录该节点的价值
//    for (int e = head[u]; e > 0; e = nxt[e]) { // 遍历所有子节点
//        int v = to[e]; // 子节点v
//        siz[i] += dfs(v); // 递归处理子节点，累加子树大小
//    }
//    return siz[i]; // 返回以i为根的子树大小
//}
//
//int knapsackOnTree() { // 在树上进行01背包DP
//    dfs(0); // 从虚拟根节点0开始DFS，建立dfn序
//    // 按dfn序逆序处理（后序遍历），i=1是虚拟根节点，从i=2开始是实际节点
//    for (int i = dfnCnt; i >= 2; i--) {
//        for (int j = 1; j <= m; j++) { // 遍历背包容量
//            // 不选当前节点：dp值等于跳过整个子树的dp值
//            dp[i][j] = dp[i + siz[i]][j];
//            // 选当前节点：在容量允许的情况下，价值为当前节点价值加上处理下一个节点的dp值
//            if (j - weight[i] >= 0) {
//                dp[i][j] = max(dp[i][j], value[i] + dp[i + 1][j - weight[i]]);
//            }
//        }
//    }
//    return dp[2][m]; // 返回根节点（dfn序为2，因为1是虚拟根）在容量m下的最大价值
//}
//
//int main() { // 主程序入口
//    ios::sync_with_stdio(false); // 关闭IO同步，加速输入输出
//    cin.tie(nullptr); // 解除cin和cout绑定
//    cin >> n >> m; // 读取物品数和背包容量
//    for (int i = 1; i <= n; i++) { // 读取每个物品的重量
//        cin >> w[i];
//    }
//    for (int i = 1; i <= n; i++) { // 读取每个物品的价值
//        cin >> v[i];
//    }
//    for (int i = 1; i <= n; i++) { // 读取每个物品的依赖
//        cin >> depend[i]; // 依赖物品编号，0表示无依赖
//        if (depend[i] > 0) { // 如果有依赖
//            addEdge(depend[i], i); // 添加依赖边（从依赖指向当前物品）
//        }
//    }
//    for (int i = 1; i <= n; i++) { // 对所有未访问的节点执行Tarjan
//        if (dfn[i] == 0) { // 如果节点i未被访问
//            tarjan(i); // 执行Tarjan算法
//        }
//    }
//    condense(); // 执行缩点操作
//    int ans = knapsackOnTree(); // 在缩点后的树上进行01背包DP
//    cout << ans << "\n"; // 输出最大收益
//    return 0; // 程序正常结束
//}
