package class193;

/**
 * 割点模板题，C++版
 * 题目描述：给定一张无向图，包含n个顶点和m条边，图不一定连通
 * 要求：输出图中割点的数量，并按从小到大的顺序输出所有割点的编号
 * 数据范围：1 <= n <= 2 * 10^4，1 <= m <= 10^5
 * 测试链接：https://www.luogu.com.cn/problem/P3388
 * 实现说明：C++版本和Java版本逻辑完全一致
 * 提交说明：提交以下代码，可以通过所有测试用例
 */

//#include <bits/stdc++.h>  // 包含所有标准库头文件
//
//using namespace std;  // 使用std命名空间
//
//const int MAXN = 20001;  // 最大顶点数，题目中n最大为20000
//const int MAXM = 100001;  // 最大边数，题目中m最大为100000
//int n, m;  // 顶点数n和边数m
//
//int head[MAXN];  // 邻接表表头数组，head[u]表示以u为起点的第一条边的索引
//int nxt[MAXM << 1];  // 邻接表下一条边索引数组，nxt[e]表示与边e同起点的下一条边的索引
//int to[MAXM << 1];  // 邻接表边的终点数组，to[e]表示边e的终点
//int cntg;  // 边计数器，记录当前已添加的边数
//
//int dfn[MAXN];  // dfn数组，记录每个顶点的深度优先搜索时间戳
//int low[MAXN];  // low数组，记录每个顶点能通过非父子边回溯到的最早祖先的时间戳
//int cntd;  // 时间戳计数器，记录当前的时间戳
//
//bool cutVertex[MAXN];  // cutVertex数组，标记每个顶点是否为割点
//
///**
// * 向邻接表中添加一条边
// * @param u 边的起点
// * @param v 边的终点
// */
//void addEdge(int u, int v) {
//    nxt[++cntg] = head[u];  // 将新边的nxt指向当前head[u]
//    to[cntg] = v;  // 设置新边的终点为v
//    head[u] = cntg;  // 更新head[u]为新边的索引
//}
//
///**
// * Tarjan算法求解割点
// * @param u 当前访问的顶点
// * @param root 是否为根节点
// */
//void tarjan(int u, bool root) {
//    dfn[u] = low[u] = ++cntd;  // 初始化dfn和low值为当前时间戳
//    int son = 0;  // 记录当前顶点的子节点数
//    
//    // 遍历当前顶点的所有邻边
//    for (int e = head[u]; e > 0; e = nxt[e]) {
//        int v = to[e];  // 获取边的终点
//        
//        if (dfn[v] == 0) {  // 如果v未被访问过
//            son++;  // 子节点数加1
//            tarjan(v, false);  // 递归访问v
//            low[u] = min(low[u], low[v]);  // 更新low[u]为low[u]和low[v]的最小值
//            
//            // 如果low[v] >= dfn[u]，说明v无法通过非父子边回溯到u的祖先
//            if (low[v] >= dfn[u]) {
//                // 如果不是根节点，或者是根节点且子节点数>=2，则u是割点
//                if (!root || son >= 2) {
//                    cutVertex[u] = true;
//                }
//            }
//        } else {  // 如果v已被访问过，说明是回边
//            low[u] = min(low[u], dfn[v]);  // 更新low[u]为low[u]和dfn[v]的最小值
//        }
//    }
//}
//
///**
// * 主函数，程序入口
// * @return 程序退出状态码
// */
//int main() {
//    ios::sync_with_stdio(false);  // 关闭同步，加速输入输出
//    cin.tie(nullptr);  // 解绑cin和cout，加速输入输出
//    
//    cin >> n >> m;  // 读取顶点数n和边数m
//    
//    // 读取m条边并添加到邻接表
//    for (int i = 1, u, v; i <= m; i++) {
//        cin >> u >> v;  // 读取边的起点u和终点v
//        addEdge(u, v);  // 添加边u->v
//        addEdge(v, u);  // 添加边v->u（无向图）
//    }
//    
//    // 遍历所有顶点，处理每个连通分量
//    for (int i = 1; i <= n; i++) {
//        if (dfn[i] == 0) {  // 如果顶点i未被访问过
//            tarjan(i, true);  // 调用Tarjan算法
//        }
//    }
//    
//    // 统计割点数量
//    int cutCnt = 0;
//    for (int i = 1; i <= n; i++) {
//        if (cutVertex[i]) {
//            cutCnt++;
//        }
//    }
//    
//    cout << cutCnt << "\n";  // 输出割点数量
//    
//    // 按从小到大的顺序输出所有割点
//    for (int i = 1; i <= n; i++) {
//        if (cutVertex[i]) {
//            cout << i << " ";
//        }
//    }
//    
//    cout << "\n";  // 换行
//    return 0;  // 程序正常退出
//}