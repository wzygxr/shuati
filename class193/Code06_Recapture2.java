package class193;

/**
 * 夺回据点问题，C++版
 * 题目描述：
 * 一共n个据点，m条双向道路，所有据点连通在一起
 * 每个据点给定点权，代表夺取这个据点需要的花费
 * 当夺取某个据点之后，再夺取相邻的据点，认为没有花费
 * 你可以按照任意顺序夺取所有据点，但是过程中必须保证
 * 那些还没夺取的据点都在同一个连通区里，不被已经夺取的据点隔开
 * 打印最小的总花费
 * 数据范围：1 <= n、m <= 10^5，1 <= 点权 <= 10^9
 * 测试链接：https://leetcode.cn/problems/s5kipK/
 * 提交说明：提交以下代码中的Solution类，可以通过所有测试用例
 */

//class Solution {
//public:
//    /**
//     * 最大顶点数，题目中n最大为100000
//     */
//    static const int MAXN = 100001;
//    
//    /**
//     * 最大边数，题目中m最大为100000
//     */
//    static const int MAXM = 100001;
//    
//    /**
//     * 顶点数n和边数m
//     */
//    int n, m;
//
//    /**
//     * 邻接表表头数组，head[u]表示以u为起点的第一条边的索引
//     */
//    int head[MAXN];
//    
//    /**
//     * 邻接表下一条边索引数组，nxt[e]表示与边e同起点的下一条边的索引
//     */
//    int nxt[MAXM << 1];
//    
//    /**
//     * 邻接表边的终点数组，to[e]表示边e的终点
//     */
//    int to[MAXM << 1];
//    
//    /**
//     * 边计数器，记录当前已添加的边数
//     */
//    int cntg;
//
//    /**
//     * dfn数组，记录每个顶点的深度优先搜索时间戳
//     */
//    int dfn[MAXN];
//    
//    /**
//     * low数组，记录每个顶点能通过非父子边回溯到的最早祖先的时间戳
//     */
//    int low[MAXN];
//    
//    /**
//     * 时间戳计数器，记录当前的时间戳
//     */
//    int cntd;
//
//    /**
//     * 顶点栈，用于存储当前路径上的顶点
//     */
//    int sta[MAXN];
//    
//    /**
//     * 栈顶指针
//     */
//    int top;
//
//    /**
//     * cutVertex数组，记录每个顶点是否为割点
//     */
//    bool cutVertex[MAXN];
//    
//    /**
//     * vbccArr数组，存储所有点双连通分量的节点
//     */
//    vector<vector<int>> vbccArr;
//
//    /**
//     * prepare函数，初始化所有数据结构
//     */
//    void prepare() {
//        cntg = cntd = top = 0;  // 初始化计数器和栈顶指针
//        for (int i = 1; i <= n; i++) {
//            head[i] = dfn[i] = low[i] = 0;  // 初始化邻接表、dfn和low数组
//            cutVertex[i] = false;  // 初始化割点数组
//        }
//        vbccArr.clear();  // 清空点双连通分量数组
//    }
//
//    /**
//     * 向邻接表中添加一条边
//     * @param u 边的起点
//     * @param v 边的终点
//     */
//    void addEdge(int u, int v) {
//        nxt[++cntg] = head[u];  // 将新边的nxt指向当前head[u]
//        to[cntg] = v;  // 设置新边的终点为v
//        head[u] = cntg;  // 更新head[u]为新边的索引
//    }
//
//    /**
//     * Tarjan算法求解点双连通分量和割点
//     * @param u 当前访问的顶点
//     * @param root 是否为根节点
//     */
//    void tarjan(int u, bool root) {
//        dfn[u] = low[u] = ++cntd;  // 初始化dfn和low值为当前时间戳
//        sta[++top] = u;  // 将当前顶点压入顶点栈
//        int son = 0;  // 记录子节点数量
//        
//        // 遍历当前顶点的所有邻边
//        for (int e = head[u]; e > 0; e = nxt[e]) {
//            int v = to[e];  // 获取边的终点
//            
//            if (dfn[v] == 0) {  // 如果v未被访问过
//                son++;  // 子节点数量加1
//                tarjan(v, false);  // 递归访问v
//                low[u] = min(low[u], low[v]);  // 更新low[u]为low[u]和low[v]的最小值
//                
//                // 如果low[v] >= dfn[u]，说明v无法通过非父子边回溯到u的祖先
//                if (low[v] >= dfn[u]) {
//                    // 如果不是根节点或者根节点有至少2个子节点，则u是割点
//                    if (!root || son >= 2) {
//                        cutVertex[u] = true;
//                    }
//                    
//                    vector<int> list;  // 创建新的点双连通分量列表
//                    list.push_back(u);  // 将u添加到点双连通分量列表
//                    
//                    // 弹出顶点栈中的顶点，直到弹出v
//                    int pop;
//                    do {
//                        pop = sta[top--];  // 弹出顶点栈顶元素
//                        list.push_back(pop);  // 将弹出的顶点添加到点双连通分量列表
//                    } while (pop != v);  // 直到弹出v为止
//                    
//                    vbccArr.push_back(list);  // 将点双连通分量列表添加到vbccArr数组
//                }
//            } else {  // 如果v已被访问过，说明是回边
//                low[u] = min(low[u], dfn[v]);  // 更新low[u]为low[u]和dfn[v]的最小值
//            }
//        }
//    }
//
//    /**
//     * minimumCost函数，求解最小总花费
//     * @param cost 每个据点的夺取花费
//     * @param roads 双向道路数组
//     * @return 最小总花费
//     */
//    long long minimumCost(vector<int>& cost, vector<vector<int>>& roads) {
//        n = cost.size();  // 获取顶点数n
//        m = roads.size();  // 获取边数m
//        prepare();  // 初始化数据结构
//        
//        // 读取m条边并添加到邻接表
//        for (int i = 0; i < m; i++) {
//            int u = roads[i][0] + 1;  // 转换为1-based索引
//            int v = roads[i][1] + 1;  // 转换为1-based索引
//            addEdge(u, v);  // 添加边u->v
//            addEdge(v, u);  // 添加边v->u（无向图）
//        }
//        
//        tarjan(1, true);  // 调用Tarjan算法
//        long long ans = 0;  // 初始化答案
//        
//        if (vbccArr.size() == 1) {  // 如果只有一个点双连通分量
//            ans = LLONG_MAX;  // 初始化答案为最大值
//            for (int i = 0; i < n; i++) {
//                ans = min(ans, (long long)cost[i]);  // 找到最小的点权
//            }
//        } else {  // 如果有多个点双连通分量
//            int maxVal = INT_MIN;  // 初始化最大点权
//            for (auto &vbcc : vbccArr) {  // 遍历每个点双连通分量
//                int cut = 0;  // 记录割点数量
//                int val = INT_MAX;  // 初始化最小点权
//                for (int cur : vbcc) {  // 遍历点双连通分量中的每个顶点
//                    if (cutVertex[cur]) {  // 如果是割点
//                        cut++;  // 割点数量加1
//                    } else {  // 如果不是割点
//                        val = min(val, cost[cur - 1]);  // 更新最小点权
//                    }
//                }
//                if (cut == 1) {  // 如果割点数量为1
//                    ans += val;  // 答案加上最小点权
//                    maxVal = max(maxVal, val);  // 更新最大点权
//                }
//            }
//            ans -= maxVal;  // 答案减去最大点权
//        }
//        
//        return ans;  // 返回答案
//    }
//};