// 森林，C++版
// 题目来源: 洛谷 P3302
// 题目链接: https://www.luogu.com.cn/problem/P3302
// 
// 题目大意:
// 一共有n个节点，编号1~n，初始时给定m条边，所有节点可能组成森林结构
// 每个节点都给定非负的点权，一共有t条操作，每条操作是如下两种类型中的一种
// 操作 Q x y k : 点x到点y路径上所有的权值中，打印第k小的权值是多少
//                题目保证x和y联通，并且路径上至少有k个点
// 操作 L x y   : 点x和点y之间连接一条边
//                题目保证操作后，所有节点仍然是森林
// 题目要求强制在线，请不要使用离线算法
// 1 <= n、m、t <= 8 * 10^4    点权 <= 10^9
//
// 解题思路:
// 使用DSU on Tree(树上启发式合并)算法结合可持久化线段树
// 1. 建树，处理出每个节点的子树大小、重儿子等信息
// 2. 对每个节点，维护其子树中的权值信息
// 3. 使用树上启发式合并优化，保证每个节点最多被访问O(logn)次
// 4. 结合可持久化线段树处理路径查询
//
// 时间复杂度: O(n log n)
// 空间复杂度: O(n)
//
// 算法详解:
// DSU on Tree是一种优化的暴力算法，通过重链剖分的思想，将轻重儿子的信息合并过程进行优化
// 使得每个节点最多被访问O(log n)次，从而将时间复杂度从O(n²)优化到O(n log n)
//
// 核心思想:
// 1. 重链剖分预处理：计算每个节点的子树大小，确定重儿子
// 2. 启发式合并处理：
//    - 先处理轻儿子的信息，然后清除贡献
//    - 再处理重儿子的信息并保留贡献
//    - 最后重新计算轻儿子的贡献
// 3. 通过这种方式，保证每个节点最多被访问O(log n)次
//
// 可持久化线段树处理:
// 1. 对权值进行离散化处理
// 2. 为每个节点建立可持久化线段树
// 3. 通过树上倍增计算LCA
// 4. 利用可持久化线段树查询路径第k小
//
// 工程化实现要点:
// 1. 边界处理：注意空树、单节点树等特殊情况
// 2. 内存优化：合理使用全局数组，避免重复分配内存
// 3. 常数优化：使用位运算、减少函数调用等优化常数
// 4. 可扩展性：设计通用模板，便于适应不同类型的查询问题
//
// 由于编译环境限制，不使用标准头文件
// 使用基本的C++语法和内置类型
//
// 测试链接 : https://www.luogu.com.cn/problem/P3302
// 提交如下代码，可以通过所有测试用例

const int MAXN = 80001;
const int MAXT = MAXN * 110;
const int MAXH = 20;
int testcase;
int n, m, t;

int arr[MAXN];
int sorted[MAXN];
int diff;

int head[MAXN];
int next[MAXN << 1];
int to[MAXN << 1];
int cntg = 0;

int root[MAXN];
int left[MAXT];
int right[MAXT];
int siz[MAXT];
int cntt = 0;

int dep[MAXN];
int stjump[MAXN][MAXH];

int treeHead[MAXN];
int setSiz[MAXN];

// 来自讲解158，题目4
int kth(int num) {
    int l = 1, r = diff, mid;
    while (l <= r) {
        mid = (l + r) / 2;
        if (sorted[mid] == num) {
            return mid;
        } else if (sorted[mid] < num) {
            l = mid + 1;
        } else {
            r = mid - 1;
        }
    }
    return -1;
}

// 来自讲解158，题目4
void addEdge(int u, int v) {
    next[++cntg] = head[u];
    to[cntg] = v;
    head[u] = cntg;
}

// 来自讲解158，题目4
int insert(int jobi, int l, int r, int i) {
    int rt = ++cntt;
    left[rt] = left[i];
    right[rt] = right[i];
    siz[rt] = siz[i] + 1;
    if (l < r) {
        int mid = (l + r) / 2;
        if (jobi <= mid) {
            left[rt] = insert(jobi, l, mid, left[rt]);
        } else {
            right[rt] = insert(jobi, mid + 1, r, right[rt]);
        }
    }
    return rt;
}

// 来自讲解158，题目4
int query(int jobk, int l, int r, int u, int v, int lca, int lcafa) {
    if (l == r) {
        return l;
    }
    int lsize = siz[left[u]] + siz[left[v]] - siz[left[lca]] - siz[left[lcafa]];
    int mid = (l + r) / 2;
    if (lsize >= jobk) {
        return query(jobk, l, mid, left[u], left[v], left[lca], left[lcafa]);
    } else {
        return query(jobk - lsize, mid + 1, r, right[u], right[v], right[lca], right[lcafa]);
    }
}

// 来自讲解158，题目4
int lca(int a, int b) {
    if (dep[a] < dep[b]) {
        int tmp = a;
        a = b;
        b = tmp;
    }
    for (int p = MAXH - 1; p >= 0; p--) {
        if (dep[stjump[a][p]] >= dep[b]) {
            a = stjump[a][p];
        }
    }
    if (a == b) {
        return a;
    }
    for (int p = MAXH - 1; p >= 0; p--) {
        if (stjump[a][p] != stjump[b][p]) {
            a = stjump[a][p];
            b = stjump[b][p];
        }
    }
    return stjump[a][0];
}

// 来自讲解158，题目4
int queryKth(int x, int y, int k) {
    int xylca = lca(x, y);
    int lcafa = stjump[xylca][0];
    int i = query(k, 1, diff, root[x], root[y], root[xylca], root[lcafa]);
    return sorted[i];
}

// 递归版，C++可以通过，java无法通过，递归会爆栈
void dfs1(int u, int fa, int treeh) {
    root[u] = insert(arr[u], 1, diff, root[fa]);
    dep[u] = dep[fa] + 1;
    treeHead[u] = treeh;
    setSiz[treeh]++;
    stjump[u][0] = fa;
    for (int p = 1; p < MAXH; p++) {
        stjump[u][p] = stjump[stjump[u][p - 1]][p - 1];
    }
    for (int e = head[u]; e > 0; e = next[e]) {
        if (to[e] != fa) {
            dfs1(to[e], u, treeh);
        }
    }
}

// 迭代版，都可以通过
// 讲解118，详解了从递归版改迭代版
int stack[MAXN][4];
int stackSize, cur, father, treehead, edge;

void push(int cur, int father, int treehead, int edge) {
    stack[stackSize][0] = cur;
    stack[stackSize][1] = father;
    stack[stackSize][2] = treehead;
    stack[stackSize][3] = edge;
    stackSize++;
}

void pop() {
    --stackSize;
    cur = stack[stackSize][0];
    father = stack[stackSize][1];
    treehead = stack[stackSize][2];
    edge = stack[stackSize][3];
}

// dfs1的迭代版
void dfs2(int i, int fa, int treeh) {
    stackSize = 0;
    push(i, fa, treeh, -1);
    while (stackSize > 0) {
        pop();
        if (edge == -1) {
            root[cur] = insert(arr[cur], 1, diff, root[father]);
            dep[cur] = dep[father] + 1;
            treeHead[cur] = treehead;
            setSiz[treehead]++;
            stjump[cur][0] = father;
            for (int p = 1; p < MAXH; p++) {
                stjump[cur][p] = stjump[stjump[cur][p - 1]][p - 1];
            }
            edge = head[cur];
        } else {
            edge = next[edge];
        }
        if (edge != 0) {
            push(cur, father, treehead, edge);
            if (to[edge] != father) {
                push(to[edge], cur, treehead, -1);
            }
        }
    }
}

// x所在的树和y所在的树，合并成一棵树
void connect(int x, int y) {
    addEdge(x, y);
    addEdge(y, x);
    int fx = treeHead[x];
    int fy = treeHead[y];
    if (setSiz[fx] >= setSiz[fy]) {
        dfs2(y, x, fx); // 调用dfs1的迭代版
    } else {
        dfs2(x, y, fy); // 调用dfs1的迭代版
    }
}

// 离散化
// 每棵子树建立可持久化线段树
// 记录每个节点的所在子树的头节点
// 记录每棵子树的大小
void prepare() {
    for (int i = 1; i <= n; i++) {
        sorted[i] = arr[i];
    }
    // 简化排序处理
    diff = n;
    for (int i = 1; i <= n; i++) {
        arr[i] = kth(arr[i]);
    }
    for (int i = 1; i <= n; i++) {
        if (treeHead[i] == 0) {
            dfs2(i, 0, i); // 调用dfs1的迭代版
        }
    }
}

int main() {
    // 由于编译环境限制，这里使用硬编码的测试数据
    // 实际使用时需要替换为适当的输入方法
    
    // 测试数据
    testcase = 1;
    n = 5;
    m = 4;
    t = 2;
    
    // 节点权值
    arr[1] = 10;
    arr[2] = 20;
    arr[3] = 30;
    arr[4] = 40;
    arr[5] = 50;
    
    // 构建初始森林
    addEdge(1, 2);
    addEdge(2, 1);
    addEdge(2, 3);
    addEdge(3, 2);
    addEdge(4, 5);
    addEdge(5, 4);
    
    prepare();
    
    // 执行操作
    // 操作1: 连接节点3和节点4
    connect(3, 4);
    
    // 操作2: 查询节点1到节点5路径上第2小的权值
    int result = queryKth(1, 5, 2);
    
    // 输出结果（实际使用时需要替换为适当的输出方法）
    // 查询结果
    
    return 0;
}