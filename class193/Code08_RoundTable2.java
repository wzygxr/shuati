package class193;

/**
 * 圆桌骑士问题，C++版
 * 题目描述：
 * 一共n个骑士，有m条厌恶关系，每条厌恶关系代表两个骑士互相讨厌对方
 * 你可以任选骑士参加圆桌会议，但是厌恶关系的骑士无法在圆桌中相邻
 * 圆桌会议的骑士数量必须是大于1的奇数，以防止赞同票和反对票一样多
 * 也许有的骑士，不管怎么安排都无法参加圆桌会议，打印这个数量
 * 数据范围：1 <= n <= 10^3，1 <= m <= 10^6
 * 测试链接：https://www.luogu.com.cn/problem/SP2878
 * 测试链接：https://www.spoj.com/problems/KNIGHTS/
 * 实现说明：C++版本和Java版本逻辑完全一样
 * 提交说明：提交如下代码，可以通过所有测试用例
 */

//#include <bits/stdc++.h>
//
//using namespace std;
//
///**
// * 最大顶点数，题目中n最大为1000
// */
//const int MAXN = 1001;
///**
// * 最大边数，题目中m最大为1000000
// */
//const int MAXM = 1000001;
///**
// * 顶点数n和边数m
// */
//int n, m;
///**
// * hate数组，记录骑士之间的厌恶关系
// */
//bool hate[MAXN][MAXN];
//
///**
// * 邻接表表头数组，head[u]表示以u为起点的第一条边的索引
// */
//int head[MAXN];
///**
// * 邻接表下一条边索引数组，nxt[e]表示与边e同起点的下一条边的索引
// */
//int nxt[MAXM << 1];
///**
// * 邻接表边的终点数组，to[e]表示边e的终点
// */
//int to[MAXM << 1];
///**
// * 边计数器，记录当前已添加的边数
// */
//int cntg;
//
///**
// * dfn数组，记录每个顶点的深度优先搜索时间戳
// */
//int dfn[MAXN];
///**
// * low数组，记录每个顶点能通过非父子边回溯到的最早祖先的时间戳
// */
//int low[MAXN];
///**
// * 时间戳计数器，记录当前的时间戳
// */
//int cntd;
//
///**
// * 顶点栈，用于存储当前路径上的顶点
// */
//int sta[MAXN];
///**
// * 栈顶指针
// */
//int top;
//
///**
// * vbccArr数组，存储所有点双连通分量的节点
// */
//int vbccArr[MAXN << 1];
///**
// * vbccl数组，记录每个点双连通分量在vbccArr中的起始位置
// */
//int vbccl[MAXN];
///**
// * vbccr数组，记录每个点双连通分量在vbccArr中的结束位置
// */
//int vbccr[MAXN];
///**
// * vbccArr数组的当前索引
// */
//int idx;
///**
// * 点双连通分量计数器
// */
//int vbccCnt;
//
///**
// * curVbcc数组，记录当前点双连通分量中的顶点
// */
//bool curVbcc[MAXN];
///**
// * color数组，记录每个顶点的颜色，用于判断是否存在奇数环
// */
//int color[MAXN];
///**
// * ok数组，记录每个骑士是否可以参加圆桌会议
// */
//bool ok[MAXN];
//
///**
// * prepare函数，初始化所有数据结构
// */
//void prepare() {
//    cntg = cntd = top = idx = vbccCnt = 0;  // 初始化计数器和栈顶指针
//    for (int i = 1; i <= n; i++) {
//        head[i] = dfn[i] = low[i] = 0;  // 初始化邻接表、dfn和low数组
//        ok[i] = false;  // 初始化ok数组
//        for (int j = 1; j <= n; j++) {
//            hate[i][j] = false;  // 初始化hate数组
//        }
//    }
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
// * Tarjan算法求解点双连通分量
// * @param u 当前访问的顶点
// */
//void tarjan(int u) {
//    dfn[u] = low[u] = ++cntd;  // 初始化dfn和low值为当前时间戳
//    sta[++top] = u;  // 将当前顶点压入顶点栈
//    
//    // 遍历当前顶点的所有邻边
//    for (int e = head[u]; e > 0; e = nxt[e]) {
//        int v = to[e];  // 获取边的终点
//        
//        if (dfn[v] == 0) {  // 如果v未被访问过
//            tarjan(v);  // 递归访问v
//            low[u] = min(low[u], low[v]);  // 更新low[u]为low[u]和low[v]的最小值
//            
//            // 如果low[v] >= dfn[u]，说明v无法通过非父子边回溯到u的祖先
//            if (low[v] >= dfn[u]) {
//                vbccCnt++;  // 点双连通分量计数器加1
//                vbccArr[++idx] = u;  // 将u添加到vbccArr数组
//                vbccl[vbccCnt] = idx;  // 记录点双连通分量的起始位置
//                
//                // 弹出顶点栈中的顶点，直到弹出v
//                int pop;
//                do {
//                    pop = sta[top--];  // 弹出顶点栈顶元素
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
// * oddLoop函数，判断是否存在节点数量为奇数的环
// * @param u 当前节点
// * @param c 当前分配给u节点的颜色，只有1和2两种颜色
// * @return 是否发现了节点数量为奇数的环
// */
//bool oddLoop(int u, int c) {
//    color[u] = c;  // 为当前节点分配颜色
//    
//    // 遍历当前节点的所有邻边
//    for (int e = head[u]; e > 0; e = nxt[e]) {
//        int v = to[e];  // 获取边的终点
//        
//        if (curVbcc[v]) {  // 如果v在当前点双连通分量中
//            if (color[v] == c) {  // 如果v的颜色与u相同，说明存在奇数环
//                return true;
//            }
//            if (color[v] == 0 && oddLoop(v, c == 1 ? 2 : 1)) {  // 如果v未被染色，递归染色
//                return true;
//            }
//        }
//    }
//    
//    return false;  // 未发现奇数环
//}
//
///**
// * compute函数，计算无法参加圆桌会议的骑士数量
// * @return 无法参加圆桌会议的骑士数量
// */
//int compute() {
//    for (int i = 1; i <= vbccCnt; i++) {  // 遍历每个点双连通分量
//        for (int j = vbccl[i]; j <= vbccr[i]; j++) {  // 遍历点双连通分量中的每个顶点
//            curVbcc[vbccArr[j]] = true;  // 标记当前顶点在点双连通分量中
//            color[vbccArr[j]] = 0;  // 初始化颜色为0
//        }
//        
//        bool check = oddLoop(vbccArr[vbccl[i]], 1);  // 判断是否存在奇数环
//        
//        for (int j = vbccl[i]; j <= vbccr[i]; j++) {  // 遍历点双连通分量中的每个顶点
//            curVbcc[vbccArr[j]] = false;  // 取消标记
//            ok[vbccArr[j]] = ok[vbccArr[j]] | check;  // 更新ok数组
//        }
//    }
//    
//    int ans = 0;  // 初始化无法参加圆桌会议的骑士数量
//    for (int i = 1; i <= n; i++) {  // 遍历每个骑士
//        if (!ok[i]) {  // 如果无法参加圆桌会议
//            ans++;  // 数量加1
//        }
//    }
//    
//    return ans;  // 返回无法参加圆桌会议的骑士数量
//}
//
///**
// * 主函数，程序入口
// * @return 程序退出码
// */
//int main() {
//    ios::sync_with_stdio(false);  // 关闭同步，加速输入输出
//    cin.tie(nullptr);  // 解绑cin和cout
//    
//    cin >> n >> m;  // 读取顶点数n和边数m
//    
//    while (n != 0 || m != 0) {  // 当n或m不为0时
//        prepare();  // 初始化数据结构
//        
//        for (int i = 1, u, v; i <= m; i++) {  // 读取m条厌恶关系
//            cin >> u >> v;  // 读取厌恶关系的起点u和终点v
//            hate[u][v] = true;  // 标记u和v互相厌恶
//            hate[v][u] = true;  // 标记v和u互相厌恶
//        }
//        
//        // 构建补图
//        for (int u = 1; u <= n; u++) {  // 遍历每个骑士
//            for (int v = u + 1; v <= n; v++) {  // 遍历每个其他骑士
//                if (!hate[u][v]) {  // 如果u和v不互相厌恶
//                    addEdge(u, v);  // 添加边u->v
//                    addEdge(v, u);  // 添加边v->u（无向图）
//                }
//            }
//        }
//        
//        // 调用Tarjan算法
//        for (int i = 1; i <= n; i++) {  // 遍历每个骑士
//            if (dfn[i] == 0) {  // 如果未被访问过
//                tarjan(i);  // 调用Tarjan算法
//            }
//        }
//        
//        int ans = compute();  // 计算无法参加圆桌会议的骑士数量
//        cout << ans << "\n";  // 输出结果
//        
//        cin >> n >> m;  // 读取下一个测试用例的顶点数n和边数m
//    }
//    
//    return 0;  // 程序正常退出
//}