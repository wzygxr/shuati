// AtCoder ABC222 F - Expensive Expense
// 给定一棵树，边权为路费，点权为观光费。从u去v旅游的费用定义为路费加上v点的观光费
// 求从每个点出发到其它点旅游的最大费用
// 换根DP，与树的重心相关
// 测试链接 : https://atcoder.jp/contests/abc222/tasks/abc222_f
// 时间复杂度：O(n)
// 空间复杂度：O(n)

// 为避免编译问题，使用基础C++实现方式，不使用STL容器

const int MAXN = 200001;

int n;

// 链式前向星存储树
int head[MAXN];
int next[MAXN << 1];
int to[MAXN << 1];
int weight[MAXN << 1];
int cnt;

// 点权（观光费）
int D[MAXN];

// 以u为根的子树中，从u出发到子树节点的最大费用
long long maxDown[MAXN];

// 从u出发到所有节点的最大费用
long long maxCost[MAXN];

// 初始化
void init() {
    cnt = 1;
    for (int i = 0; i <= n; i++) {
        head[i] = 0;
        D[i] = 0;
        maxDown[i] = 0;
        maxCost[i] = 0;
    }
}

// 添加边
void addEdge(int u, int v, int w) {
    next[cnt] = head[u];
    to[cnt] = v;
    weight[cnt] = w;
    head[u] = cnt++;
    
    next[cnt] = head[v];
    to[cnt] = u;
    weight[cnt] = w;
    head[v] = cnt++;
}

// 求两个数的最大值
long long max(long long a, long long b) {
    return a > b ? a : b;
}

// 第一次DFS，计算向下最大费用
void dfs1(int u, int father) {
    maxDown[u] = D[u]; // 至少包含自己的观光费
    
    // 遍历所有子节点
    for (int e = head[u], v, w; e; e = next[e]) {
        v = to[e];
        w = weight[e];
        
        if (v != father) {
            dfs1(v, u);
            // 更新从u出发向下的最大费用
            maxDown[u] = max(maxDown[u], maxDown[v] + w);
        }
    }
}

// 第二次DFS，换根DP计算答案
void dfs2(int u, int father, long long fatherCost) {
    // 从u出发的最大费用是向下最大费用和从父节点来的最大费用的最大值
    maxCost[u] = max(maxDown[u], fatherCost + D[u]);
    
    // 计算从u到各个子节点的最大费用
    // 找到最大值和次大值
    long long max1 = -1, max2 = -1;
    int max1Child = -1;
    
    for (int e = head[u], v, w; e; e = next[e]) {
        v = to[e];
        w = weight[e];
        
        if (v != father) {
            long long cost = maxDown[v] + w;
            if (cost > max1) {
                max2 = max1;
                max1 = cost;
                max1Child = v;
            } else if (cost > max2) {
                max2 = cost;
            }
        }
    }
    
    // 递归处理子节点
    for (int e = head[u], v, w; e; e = next[e]) {
        v = to[e];
        w = weight[e];
        
        if (v != father) {
            // 计算从v向上看的最大费用
            long long upCost = fatherCost + w; // 从父节点来的费用
            
            // 如果v不是产生最大费用的子节点，可以加上最大费用
            // 否则加上次大费用
            if (v == max1Child) {
                upCost = max(upCost, max2 + w);
            } else {
                upCost = max(upCost, max1 + w);
            }
            
            // 加上v节点的观光费
            upCost += D[u];
            
            dfs2(v, u, upCost);
        }
    }
}

int main() {
    // 由于无法使用输入输出函数，这里只展示算法实现
    // 实际使用时需要添加输入输出代码
    return 0;
}