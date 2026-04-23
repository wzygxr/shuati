package class193;

/**
 * 点双连通分量模板题2，C++版
 * 题目描述：给定一张无向图，包含n个顶点和m条边
 * 要求：
 * 1. 忽略所有孤立点，打印点双连通分量的个数
 * 2. 打印每个点双连通分量内部的节点编号，编号按照从小到大组织
 * 3. 内部节点编号是一个序列，序列字典序小的点双连通分量先打印
 * 数据范围：1 <= n <= 5 * 10^4，1 <= m <= 3 * 10^5
 * 测试链接：https://www.luogu.com.cn/problem/B3610
 * 实现说明：C++版本和Java版本逻辑完全一致
 * 提交说明：提交以下代码，可以通过所有测试用例
 */

//#include <bits/stdc++.h>  // 包含所有标准库头文件
//
//using namespace std;  // 使用std命名空间
//
//const int MAXN = 50001;  // 最大顶点数，题目中n最大为50000
//const int MAXM = 300001;  // 最大边数，题目中m最大为300000
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
//int sta[MAXN];  // 顶点栈，用于存储当前路径上的顶点
//int top;  // 栈顶指针
//
//vector<vector<int>> vbccArr;  // vbccArr数组，存储所有点双连通分量的节点
//
///**
// * VbccCmp函数，用于比较两个点双连通分量的字典序
// * @param o1 第一个点双连通分量
// * @param o2 第二个点双连通分量
// * @return 比较结果，true表示o1字典序小于o2，false表示o1字典序大于o2
// */
//bool VbccCmp(const vector<int> &o1, const vector<int> &o2) {
//    int size = min(o1.size(), o2.size());
//    for (int i = 0; i < size; i++) {
//        if (o1[i] != o2[i]) {
//            return o1[i] < o2[i];
//        }
//    }
//    return o1.size() < o2.size();
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
//            // 如果low[v] == dfn[u]，说明v无法通过非父子边回溯到u的祖先
//            if (low[v] == dfn[u]) {
//                vector<int> list;  // 创建新的点双连通分量列表
//                list.push_back(u);  // 将u添加到点双连通分量列表
//                
//                // 弹出顶点栈中的顶点，直到弹出v
//                int pop;
//                do {
//                    pop = sta[top--];  // 弹出顶点栈顶元素
//                    list.push_back(pop);  // 将弹出的顶点添加到点双连通分量列表
//                } while (pop != v);  // 直到弹出v为止
//                
//                vbccArr.push_back(list);  // 将点双连通分量列表添加到vbccArr数组
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
//        if (dfn[i] == 0 && head[i] > 0) {  // 如果顶点i未被访问过且不是孤立点
//            tarjan(i);  // 调用Tarjan算法
//        }
//    }
//    
//    cout << vbccArr.size() << "\n";  // 输出点双连通分量的个数
//    
//    // 对每个点双连通分量内部的节点进行排序
//    for (int i = 0; i < vbccArr.size(); i++) {
//        sort(vbccArr[i].begin(), vbccArr[i].end());
//    }
//    
//    // 对点双连通分量进行字典序排序
//    sort(vbccArr.begin(), vbccArr.end(), VbccCmp);
//    
//    // 输出每个点双连通分量的节点
//    for (int i = 0; i < vbccArr.size(); i++) {
//        for (int node : vbccArr[i]) {
//            cout << node << " ";  // 输出节点编号
//        }
//        cout << "\n";  // 换行
//    }
//    
//    return 0;  // 程序正常退出
//}