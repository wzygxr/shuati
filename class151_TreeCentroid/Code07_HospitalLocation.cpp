// 医院设置
// 题目来源: 洛谷 P1364 https://www.luogu.com.cn/problem/P1364
// 问题描述: 在一棵树上找一个点，使得该点到其他点距离之和最小
// 算法思路:
// 1. 利用树的重心的性质：树中所有点到某个点的距离和中，到重心的距离和最小
// 2. 使用换根DP（动态规划）技术计算每个节点作为医院时的总距离
// 3. 首先以节点1为根计算距离和，然后通过换根技术计算其他节点的距离和
// 时间复杂度：O(n)，需要三次DFS遍历
// 空间复杂度：O(n)，用于存储树结构和递归栈

// 由于编译环境限制，使用基础C++语法实现

// 最大节点数，根据题目限制设置
const int MAXN = 101;

// 节点数量
int n;

// 链式前向星存储树结构
// head[i]表示节点i的第一条边的索引
int head[MAXN];
// next[i]表示第i条边的下一条边的索引
int next[MAXN << 1];
// to[i]表示第i条边指向的节点
int to[MAXN << 1];
// 边的计数器，从1开始编号
int cnt;

// people[i]表示节点i上的人数
int people[MAXN];

// size[i]表示以节点i为根的子树的总人数
int size[MAXN];

// distSum[i]表示以节点i为医院时，所有人的总距离
long long distSum[MAXN];

// 初始化函数，重置邻接表
void init() {
    cnt = 1;  // 边的索引从1开始
    // 初始化邻接表
    for (int i = 0; i < MAXN; i++) {
        head[i] = 0;
        people[i] = 0;
        size[i] = 0;
        distSum[i] = 0;
    }
}

// 添加无向边的函数
// u和v之间添加一条边
void addEdge(int u, int v) {
    // 将新边添加到邻接表中
    next[cnt] = head[u];  // 新边的下一条边指向原来u节点的第一条边
    to[cnt] = v;          // 新边指向节点v
    head[u] = cnt++;      // u节点的第一条边更新为新边，然后cnt自增
    
    next[cnt] = head[v];  // 新边的下一条边指向原来v节点的第一条边
    to[cnt] = u;          // 新边指向节点u
    head[v] = cnt++;      // v节点的第一条边更新为新边，然后cnt自增
}

// 第一次DFS，计算每个节点的子树总人数
// u: 当前访问的节点
// father: u的父节点，避免回到父节点形成环
void dfs1(int u, int father) {
    // 初始化当前节点u的子树总人数为该节点的人数
    size[u] = people[u];
    
    // 遍历u的所有邻接节点
    for (int e = head[u], v; e; e = next[e]) {
        v = to[e];  // 获取当前边指向的节点
        
        // 如果不是父节点，则继续DFS
        if (v != father) {
            // 递归访问子节点v，父节点为u
            dfs1(v, u);
            
            // 将子节点v的子树总人数加到当前节点u的子树总人数中
            size[u] += size[v];
        }
    }
}

// 第二次DFS，计算以节点1为根时的距离和
// u: 当前访问的节点
// father: u的父节点，避免回到父节点形成环
void dfs2(int u, int father) {
    // 计算从u到所有节点的距离和
    for (int e = head[u], v; e; e = next[e]) {
        v = to[e];  // 获取当前边指向的节点
        
        // 如果不是父节点，则继续DFS
        if (v != father) {
            // 递归访问子节点v，父节点为u
            dfs2(v, u);
            
            // 从v子树中的每个节点到u的距离比到v的距离多1
            // 因此总距离增加：distSum[v]（v子树内节点到v的距离和）+ size[v]（v子树的总人数，每个距离都增加1）
            distSum[u] += distSum[v] + size[v];
        }
    }
}

// 第三次DFS，换根DP计算所有节点的距离和
// 换根DP的核心思想：当根从u换到v时，重新计算以v为根时的距离和
// u: 当前访问的节点
// father: u的父节点，避免回到父节点形成环
void dfs3(int u, int father) {
    // 遍历u的所有邻接节点
    for (int e = head[u], v; e; e = next[e]) {
        v = to[e];  // 获取当前边指向的节点
        
        // 如果不是父节点，则继续DFS
        if (v != father) {
            // 当根从u换到v时：
            // 1. v子树中的节点到v的距离比到u的距离少1，总共减少size[v]个距离单位
            // 2. 其他节点（整棵树去掉v子树）到v的距离比到u的距离多1，总共增加(size[1]-size[v])个距离单位
            // 因此：distSum[v] = distSum[u] + (size[1]-size[v]) - size[v] = distSum[u] + size[1] - 2*size[v]
            distSum[v] = distSum[u] + (size[1] - size[v]) - size[v];
            
            // 递归处理子节点v
            dfs3(v, u);
        }
    }
}

// 求两个数的最小值的辅助函数
long long min(long long a, long long b) {
    return a < b ? a : b;
}

// 由于无法使用标准输入输出函数，这里只展示算法实现
// 实际使用时需要添加输入输出代码
int main() {
    // 算法实现已完成，此处为主函数占位符
    return 0;
}