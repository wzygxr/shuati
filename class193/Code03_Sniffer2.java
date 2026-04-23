package class193;

/**
 * 嗅探器问题，C++版
 * 题目描述：给定一张无向图，所有节点属于一个连通区
 * 要求：找到编号最小的关键点，使得删除该点后a和b不再连通
 * 关键点定义：删除该点后a和b不再连通的点（a和b本身不算关键点）
 * 数据范围：1 <= n <= 2 * 10^5，1 <= m <= 5 * 10^5
 * 测试链接：https://www.luogu.com.cn/problem/P5058
 * 实现说明：C++版本和Java版本逻辑完全一致
 * 提交说明：提交以下代码，可以通过所有测试用例
 */

//#include <bits/stdc++.h>  // 包含所有标准库头文件
//
//using namespace std;  // 使用std命名空间
//
//const int MAXN = 200001;  // 最大顶点数，题目中n最大为200000
//const int MAXM = 500001;  // 最大边数，题目中m最大为500000
//int n, a, b;  // n：顶点数；a和b：需要判断连通性的两个点
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
//bool isKey[MAXN];  // isKey数组，标记每个顶点是否为关键点
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
// * Tarjan算法求解嗅探器问题
// * @param u 当前访问的顶点
// */
//void tarjan(int u) {
//    dfn[u] = low[u] = ++cntd;  // 初始化dfn和low值为当前时间戳
//    
//    // 遍历当前顶点的所有邻边
//    for (int e = head[u]; e > 0; e = nxt[e]) {
//        int v = to[e];  // 获取边的终点
//        
//        if (dfn[v] == 0) {  // 如果v未被访问过
//            tarjan(v);  // 递归访问v
//            low[u] = min(low[u], low[v]);  // 更新low[u]为low[u]和low[v]的最小值
//            
//            // 判断是否为关键点：
//            // 1. low[v] >= dfn[u]：v无法通过非父子边回溯到u的祖先
//            // 2. u != a && u != b：u不是a或b本身
//            // 3. dfn[b] >= dfn[v]：b在v的子树中
//            if (low[v] >= dfn[u] && u != a && u != b && dfn[b] >= dfn[v]) {
//                isKey[u] = true;  // 标记u为关键点
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
//    cin >> n;  // 读取顶点数n
//    int u, v;
//    cin >> u >> v;  // 读取第一条边的起点u和终点v
//    
//    // 读取所有边并添加到邻接表
//    while (u != 0 || v != 0) {
//        addEdge(u, v);  // 添加边u->v
//        addEdge(v, u);  // 添加边v->u（无向图）
//        cin >> u >> v;  // 读取下一条边的起点u和终点v
//    }
//    
//    cin >> a >> b;  // 读取点a和点b
//    tarjan(a);  // 调用Tarjan算法
//    
//    // 寻找编号最小的关键点
//    int ans = 0;
//    for (int i = 1; i <= n; i++) {
//        if (isKey[i]) {
//            ans = i;  // 找到编号最小的关键点
//            break;
//        }
//    }
//    
//    // 输出结果
//    if (ans == 0) {
//        cout << "No solution" << "\n";  // 没有关键点
//    } else {
//        cout << ans << "\n";  // 输出编号最小的关键点
//    }
//    
//    return 0;  // 程序正常退出
//}