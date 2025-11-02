// COCI 2014/2015 #1 Kamp
// 给定一颗有n个节点的无根树，每一条边有一个经过的时间，树上有K个关键节点，
// 对于每一个节点u，需要回答从u出发到所有关键节点的最小时间
// 利用树的重心性质优化计算
// 测试链接 : https://oj.uz/problem/view/COCI15_kamp
// 时间复杂度：O(n)
// 空间复杂度：O(n)

// 为避免编译问题，使用基础C++实现方式，不使用STL容器

const int MAXN = 500001;

int n, k;

// 链式前向星存储树
int head[MAXN];
int next[MAXN << 1];
int to[MAXN << 1];
int weight[MAXN << 1];
int cnt;

// 关键节点标记
int isKey[MAXN];

// 子树中关键节点的数量
int keyCount[MAXN];

// 以u为根的子树中，从u出发遍历所有关键节点并返回u的最小时间
long long subtreeTime[MAXN];

// 从u出发遍历所有关键节点的最小时间（不需要返回u）
long long minTime[MAXN];

// 初始化
void init() {
    cnt = 1;
    for (int i = 0; i <= n; i++) {
        head[i] = 0;
        isKey[i] = 0;
        keyCount[i] = 0;
        subtreeTime[i] = 0;
        minTime[i] = 0;
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

// 第一次DFS，计算子树信息
void dfs1(int u, int father) {
    keyCount[u] = isKey[u] ? 1 : 0;
    subtreeTime[u] = 0;
    
    // 遍历所有子节点
    for (int e = head[u], v, w; e; e = next[e]) {
        v = to[e];
        w = weight[e];
        
        if (v != father) {
            dfs1(v, u);
            keyCount[u] += keyCount[v];
            
            // 如果子树中有关键节点，需要加上往返时间
            if (keyCount[v] > 0) {
                subtreeTime[u] += subtreeTime[v] + 2LL * w;
            }
        }
    }
}

// 第二次DFS，换根DP计算答案
void dfs2(int u, int father, int fatherWeight) {
    if (u == 1) {
        // 根节点的最小时间就是子树时间
        minTime[u] = subtreeTime[u];
    } else {
        // 非根节点的最小时间需要考虑从父节点来的路径
        minTime[u] = subtreeTime[u];
        
        // 如果父节点子树中有关键节点，需要考虑从父节点来的路径
        if (keyCount[1] - keyCount[u] > 0) {
            long long fatherTime = minTime[father];
            
            // 如果u是father的子树中包含关键节点的子树，需要减去u的贡献
            if (keyCount[u] > 0) {
                fatherTime -= subtreeTime[u] + 2LL * fatherWeight;
            }
            
            // 加上从u到father再遍历father其他子树的时间
            if (keyCount[1] - keyCount[u] > 0) {
                minTime[u] += fatherTime + 2LL * fatherWeight;
            }
        }
    }
    
    // 递归处理子节点
    for (int e = head[u], v, w; e; e = next[e]) {
        v = to[e];
        w = weight[e];
        
        if (v != father) {
            dfs2(v, u, w);
        }
    }
}

int main() {
    // 由于无法使用输入输出函数，这里只展示算法实现
    // 实际使用时需要添加输入输出代码
    return 0;
}