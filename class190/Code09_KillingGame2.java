package class190; // 定义包名为class190

// 杀人游戏，C++版
// 一共n个人，只有一个杀手，每个人是杀手的概率均等，其他人都是平民
// 给定m个知晓关系，如果x知晓y，那么y是不是杀手，x就知道情况了
// 知晓关系是单向且可传递的，比如a知晓b，b知晓c，那么a知晓c
// 你可以盘问任何人，不仅能知道对方身份，并且对方知晓的所有情况都能获得
// 但是如果你直接盘问到杀手的话，杀手会原地爆炸，炸死所有人
// 你一定要确定所有人的身份，而且你充分了解知晓关系网，会用最优的盘问策略
// 返回最优盘问策略下，杀手不爆炸还能被揪出来的概率，保留小数点后面6位
// 1 <= n <= 10^5
// 0 <= m <= 3 * 10^5
// 测试链接 : https://www.luogu.com.cn/problem/P4819
// 如下实现是C++的版本，C++版本和java版本逻辑完全一样
// 提交如下代码，可以通过所有测试用例

//#include <bits/stdc++.h> // C++标准库头文件
//
//using namespace std; // 使用标准命名空间
//
//using ll = long long; // 定义long long别名为ll
//
//const int MAXN = 100001; // 定义最大节点数常量，最多100000个人
//const int MAXM = 300001; // 定义最大边数常量，最多300000条边
//int n, m; // n为人数，m为知晓关系数
//int a[MAXM]; // 存储每条知晓关系的起点
//int b[MAXM]; // 存储每条知晓关系的终点
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
//int belong[MAXN]; // 记录每个人属于哪个SCC
//int sccSiz[MAXN]; // 记录每个SCC的大小
//int sccCnt; // 强连通分量计数器
//
//ll edgeArr[MAXM]; // 用于存储缩点后的边，用long long打包两个int
//int cnte; // 缩点后边的计数器
//
//int indegree[MAXN]; // 缩点后DAG中每个SCC的入度
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
//            sccSiz[sccCnt]++; // 该SCC的人数加1
//        } while (pop != u); // 直到弹出u
//    }
//}
//
//void condense() { // 缩点操作，构建缩点后的DAG并去重边
//    cntg = 0; // 重置边计数器
//    for (int i = 1; i <= sccCnt; i++) { // 初始化SCC的头指针
//        head[i] = 0; // 清零
//    }
//    cnte = 0; // 重置缩点后边计数器
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
//bool isolated(int i) { // 判断SCC i是否是孤立的（可以不盘问而确定身份）
//    if (indegree[i] > 0 || sccSiz[i] > 1) { // 如果有入度或大小大于1（强连通）
//        return false; // 不是孤立的
//    }
//    if (head[i] == 0) { // 如果没有出边（孤立的点）
//        return true; // 是孤立的
//    }
//    for (int e = head[i]; e > 0; e = nxt[e]) { // 遍历所有出边
//        int v = to[e]; // 邻接SCC v
//        if (indegree[v] == 1) { // 如果v只有i这一个入边
//            return false; // 不是孤立的（必须通过盘问i来确定v）
//        }
//    }
//    return true; // 是孤立的
//}
//
//double compute() { // 计算最优策略下杀手不爆炸的概率
//    int inZero = 0; // 统计缩点后DAG中入度为0的SCC数量
//    for (int i = 1; i <= sccCnt; i++) { // 遍历所有SCC
//        if (indegree[i] == 0) { // 如果入度为0
//            inZero++; // 计数加1
//        }
//    }
//    for (int i = 1; i <= sccCnt; i++) { // 尝试找到一个孤立点来减少盘问次数
//        if (isolated(i)) { // 如果找到孤立的SCC
//            inZero--; // 可以减少一次盘问
//            break; // 只能减少一次
//        }
//    }
//    // 返回概率：1 - (需要盘问的SCC数 / 总人数)
//    return 1.0 - (double)inZero / (double)n;
//}
//
//int main() { // 主程序入口
//    scanf("%d%d", &n, &m); // 读取人数和知晓关系数
//    for (int i = 1; i <= m; i++) { // 读取每条知晓关系
//        scanf("%d%d", &a[i], &b[i]); // 读取起点和终点（a知晓b）
//        addEdge(a[i], b[i]); // 添加边到原图
//    }
//    for (int i = 1; i <= n; i++) { // 对所有未访问的人执行Tarjan
//        if (dfn[i] == 0) { // 如果人i未被访问
//            tarjan(i); // 执行递归版
//        }
//    }
//    condense(); // 执行缩点操作
//    double ans = compute(); // 计算最优策略下的概率
//    printf("%.6lf", ans); // 输出结果，保留6位小数
//    return 0; // 程序正常结束
//}
