package class190; // 定义包名为class190

// 最大半连通子图，C++版
// 有向图中节点u和v，只要其中一点能到达另一点，就说两点是半连通的
// 如果一个有向图，任意两点都是半连通的，这样的有向图就是半连通图
// 有向图中的一个点集，该点集中只要某两点在原图中有边，那么这条边就保留，则可以得到一个子图
// 如果该子图既是半连通图，又有节点数量最多，那么这个子图就是原图的最大半连通子图
// 给定一张n个点，m条边的有向图，打印最大半连通子图的大小
// 可能存在多个最大半连通子图，打印这个数量，数量对给定的数字x取余
// 1 <= n <= 10^5
// 1 <= m <= 10^6
// 测试链接 : https://www.luogu.com.cn/problem/P2272
// 如下实现是C++的版本，C++版本和java版本逻辑完全一样
// 提交如下代码，可以通过所有测试用例

//#include <bits/stdc++.h> // C++标准库头文件
//
//using namespace std; // 使用标准命名空间
//
//using ll = long long; // 定义long long别名为ll
//
//const int MAXN = 100001; // 定义最大节点数常量，最多100000个节点
//const int MAXM = 1000001; // 定义最大边数常量，最多1000000条边
//int n, m, x; // n为节点数，m为边数，x为取模数
//int a[MAXM]; // 存储每条边的起点
//int b[MAXM]; // 存储每条边的终点
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
//int sccSiz[MAXN]; // 记录每个SCC的大小（包含的节点数）
//int sccCnt; // 强连通分量计数器
//
//ll edgeArr[MAXM]; // 用于存储缩点后的边，用long long打包两个int
//int cnte; // 缩点后边的计数器
//
//int indegree[MAXN]; // 缩点后DAG中每个SCC的入度
//int dpSum[MAXN]; // DP数组，dpSum[i]表示以SCC i结尾的最大半连通子图的节点数
//int dpCnt[MAXN]; // DP数组，dpCnt[i]表示以SCC i结尾的最大半连通子图的数量
//
//int ans1, ans2; // ans1为最大半连通子图的大小，ans2为数量
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
//        sccSiz[sccCnt] = 0; // 初始化该SCC的大小为0
//        int pop; // 弹出的节点
//        do { // 弹出栈中属于该SCC的所有节点
//            pop = sta[top--]; // 弹出栈顶
//            belong[pop] = sccCnt; // 标记所属SCC
//            sccSiz[sccCnt]++; // 该SCC的节点数加1
//        } while (pop != u); // 直到弹出u
//    }
//}
//
//void condense() { // 缩点操作，构建缩点后的DAG并去重边
//    cntg = 0; // 重置边计数器
//    for (int i = 1; i <= sccCnt; i++) { // 初始化SCC的头指针
//        head[i] = 0; // 清零
//    }
//    for (int i = 1; i <= m; i++) { // 遍历所有边
//        int scc1 = belong[a[i]]; // 边起点的SCC编号
//        int scc2 = belong[b[i]]; // 边终点的SCC编号
//        if (scc1 != scc2) { // 如果边连接不同SCC
//            // 将两个int打包成一个long long：高32位是scc1，低32位是scc2
//            edgeArr[++cnte] = ((1LL * scc1) << 32) | scc2;
//        }
//    }
//    sort(edgeArr + 1, edgeArr + cnte + 1); // 对边进行排序，便于去重
//    ll pre = 0, cur; // pre记录上一条边，cur记录当前边
//    for (int i = 1; i <= cnte; i++) { // 遍历排序后的边
//        cur = edgeArr[i]; // 获取当前边
//        if (cur != pre) { // 如果当前边与上一条不同（去重）
//            int scc1 = (int)(cur >> 32); // 解压出起点SCC
//            int scc2 = (int)(cur & 0xffffffffLL); // 解压出终点SCC
//            indegree[scc2]++; // 终点SCC的入度加1
//            addEdge(scc1, scc2); // 添加边到DAG
//            pre = cur; // 更新上一条边
//        }
//    }
//}
//
//void dpOnDAG() { // 在缩点后的DAG上进行动态规划
//    for (int u = sccCnt; u > 0; u--) { // 按SCC编号逆序进行DP（拓扑序）
//        if (indegree[u] == 0) { // 如果u是入度为0的起点
//            dpSum[u] = sccSiz[u]; // 以u结尾的路径的节点数为u的大小
//            dpCnt[u] = 1; // 以u结尾的路径的数量为1
//        }
//        for (int e = head[u]; e > 0; e = nxt[e]) { // 遍历u的所有出边
//            int v = to[e]; // 邻接SCC v
//            if (dpSum[v] < dpSum[u] + sccSiz[v]) { // 如果经过u到v能得到更长的路径
//                dpSum[v] = dpSum[u] + sccSiz[v]; // 更新以v结尾的最大节点数
//                dpCnt[v] = dpCnt[u]; // 更新以v结尾的路径数量
//            } else if (dpSum[v] == dpSum[u] + sccSiz[v]) { // 如果路径长度相同
//                dpCnt[v] = (dpCnt[v] + dpCnt[u]) % x; // 累加路径数量并取模
//            }
//        }
//    }
//    ans1 = ans2 = 0; // 初始化答案
//    for (int i = 1; i <= sccCnt; i++) { // 遍历所有SCC
//        if (dpSum[i] > ans1) { // 如果找到更大的半连通子图
//            ans1 = dpSum[i]; // 更新最大大小
//            ans2 = dpCnt[i]; // 更新数量
//        } else if (dpSum[i] == ans1) { // 如果大小相同
//            ans2 = (ans2 + dpCnt[i]) % x; // 累加数量并取模
//        }
//    }
//}
//
//int main() { // 主程序入口
//    ios::sync_with_stdio(false); // 关闭IO同步，加速输入输出
//    cin.tie(nullptr); // 解除cin和cout绑定
//    cin >> n >> m >> x; // 读取节点数、边数、取模数
//    for (int i = 1; i <= m; i++) { // 读取每条边
//        cin >> a[i] >> b[i]; // 读取起点和终点
//        addEdge(a[i], b[i]); // 添加边到原图
//    }
//    for (int i = 1; i <= n; i++) { // 对所有未访问的节点执行Tarjan
//        if (dfn[i] == 0) { // 如果节点i未被访问
//            tarjan(i); // 执行递归版
//        }
//    }
//    condense(); // 执行缩点操作
//    dpOnDAG(); // 在DAG上DP求解
//    cout << ans1 << "\n"; // 输出最大半连通子图的大小
//    cout << ans2 << "\n"; // 输出最大半连通子图的数量
//    return 0; // 程序正常结束
//}
