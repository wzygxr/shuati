package class193;

/**
 * 矿场搭建问题，C++版
 * 题目描述：
 * 一共n个地点，地点至少2个，每个地点都有人，m条双向道路连通所有地点
 * 地震会发生在任何一个地点，地震发生时，其他地点的人都要去往救援点
 * 你可以在任何地点设立救援点，但是发生地震的地点，道路和救援点都会失效
 * 打印至少需要几个救援点，打印设立救援点的方案总数，方案认为是无序集合
 * 数据范围：1 <= n <= 1000，1 <= m <= 1000
 * 测试链接：https://www.luogu.com.cn/problem/P3225
 * 实现说明：C++版本和Java版本逻辑完全一致
 * 提交说明：提交以下代码，可以通过所有测试用例
 */

//#include <bits/stdc++.h>  // 包含所有标准库头文件
//
//using namespace std;  // 使用std命名空间
//
//using ll = long long;  // 定义long long类型别名
//
//const int MAXN = 1001;  // 最大顶点数，题目中n最大为1000
//const int MAXM = 1001;  // 最大边数，题目中m最大为1000
//int t, n, m;  // 测试用例编号t，顶点数n和边数m
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
//int sta[MAXN];  // 顶点栈，用于存储当前路径上的顶点
//int top;  // 栈顶指针
//
//bool cutVertex[MAXN];  // cutVertex数组，记录每个顶点是否为割点
//int vbccSiz[MAXN];  // vbccSiz数组，记录每个点双连通分量的大小
//int vbccArr[MAXN << 1];  // vbccArr数组，存储所有点双连通分量的节点
//int vbccl[MAXN];  // vbccl数组，记录每个点双连通分量在vbccArr中的起始位置
//int vbccr[MAXN];  // vbccr数组，记录每个点双连通分量在vbccArr中的结束位置
//int idx;  // vbccArr数组的当前索引
//int vbccCnt;  // 点双连通分量计数器
//
//ll ans1, ans2;  // ans1表示至少需要的救援点数量，ans2表示设立救援点的方案总数
//
///**
// * prepare函数，初始化所有数据结构
// */
//void prepare() {
//    cntg = cntd = top = idx = vbccCnt = 0;  // 初始化计数器和栈顶指针
//    for (int i = 1; i < MAXN; i++) {
//        head[i] = dfn[i] = low[i] = 0;  // 初始化邻接表、dfn和low数组
//        cutVertex[i] = false;  // 初始化割点数组
//    }
//    n = 0;  // 初始化顶点数n
//}
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
// * Tarjan算法求解点双连通分量和割点
// * @param u 当前访问的顶点
// * @param root 是否为根节点
// */
//void tarjan(int u, bool root) {
//    dfn[u] = low[u] = ++cntd;  // 初始化dfn和low值为当前时间戳
//    sta[++top] = u;  // 将当前顶点压入顶点栈
//    int son = 0;  // 记录子节点数量
//    
//    // 遍历当前顶点的所有邻边
//    for (int e = head[u]; e > 0; e = nxt[e]) {
//        int v = to[e];  // 获取边的终点
//        
//        if (dfn[v] == 0) {  // 如果v未被访问过
//            son++;  // 子节点数量加1
//            tarjan(v, false);  // 递归访问v
//            low[u] = min(low[u], low[v]);  // 更新low[u]为low[u]和low[v]的最小值
//            
//            // 如果low[v] >= dfn[u]，说明v无法通过非父子边回溯到u的祖先
//            if (low[v] >= dfn[u]) {
//                // 如果不是根节点或者根节点有至少2个子节点，则u是割点
//                if (!root || son >= 2) {
//                    cutVertex[u] = true;
//                }
//                
//                vbccCnt++;  // 点双连通分量计数器加1
//                vbccSiz[vbccCnt] = 1;  // 初始化点双连通分量大小为1（包含u）
//                vbccArr[++idx] = u;  // 将u添加到vbccArr数组
//                vbccl[vbccCnt] = idx;  // 记录点双连通分量的起始位置
//                
//                // 弹出顶点栈中的顶点，直到弹出v
//                int pop;
//                do {
//                    pop = sta[top--];  // 弹出顶点栈顶元素
//                    vbccSiz[vbccCnt]++;  // 点双连通分量大小加1
//                    vbccArr[++idx] = pop;  // 将弹出的顶点添加到vbccArr数组
//                } while (pop != v);  // 直到弹出v为止
//                
//                vbccr[vbccCnt] = idx;  // 记录点双连通分量的结束位置
//            }
//        } else {  // 如果v已被访问过，说明是回边
//            low[u] = min(low[u], dfn[v]);  // 更新low[u]为low[u]和dfn[v]的最小值
//        }
//    }
//}
//
///**
// * compute函数，计算至少需要的救援点数量和设立救援点的方案总数
// */
//void compute() {
//    if (vbccCnt == 1) {  // 如果只有一个点双连通分量
//        ans1 = 2;  // 至少需要2个救援点
//        ans2 = 1ll * n * (n - 1) / 2;  // 方案总数为n*(n-1)/2
//    } else {  // 如果有多个点双连通分量
//        ans1 = 0;  // 初始化至少需要的救援点数量
//        ans2 = 1;  // 初始化方案总数
//        for (int i = 1; i <= vbccCnt; i++) {  // 遍历每个点双连通分量
//            int siz = vbccSiz[i], cut = 0;  // 获取点双连通分量大小和割点数量
//            for (int j = vbccl[i]; j <= vbccr[i]; j++) {  // 遍历点双连通分量中的每个顶点
//                if (cutVertex[vbccArr[j]]) {  // 如果是割点
//                    cut++;  // 割点数量加1
//                }
//            }
//            if (cut == 1) {  // 如果割点数量为1
//                ans1 += 1;  // 至少需要的救援点数量加1
//                ans2 = ans2 * (siz - 1);  // 方案总数乘以（点双连通分量大小-1）
//            }
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
//    t = 0;  // 初始化测试用例编号
//    cin >> m;  // 读取边数m
//    
//    while (m != 0) {  // 当m不为0时
//        prepare();  // 初始化数据结构
//        for (int i = 1, u, v; i <= m; i++) {  // 读取m条边
//            cin >> u >> v;  // 读取边的起点u和终点v
//            n = max(n, u);  // 更新顶点数n
//            n = max(n, v);  // 更新顶点数n
//            addEdge(u, v);  // 添加边u->v
//            addEdge(v, u);  // 添加边v->u（无向图）
//        }
//        tarjan(1, true);  // 调用Tarjan算法
//        compute();  // 计算结果
//        cout << "Case " << (++t) << ": " << ans1 << " " << ans2 << "\n";  // 输出结果
//        cin >> m;  // 读取下一个测试用例的边数m
//    }
//    
//    return 0;  // 程序正常退出
//}