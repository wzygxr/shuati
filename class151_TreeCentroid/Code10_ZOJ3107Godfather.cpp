// ZOJ 3107 Godfather
// 找到树的所有重心
// 树的重心定义：删除这个点后，剩余各个连通块中点数的最大值不超过总节点数的一半
// 测试链接 : https://zoj.pintia.cn/problem-sets/91827364500/problems/91827367606
// 时间复杂度：O(n)
// 空间复杂度：O(n)

// 为避免编译问题，使用基础C++实现方式，不使用STL容器

const int MAXN = 50001;

int n;

// 链式前向星存储树
int head[MAXN];
int next[MAXN << 1];
int to[MAXN << 1];
int cnt;

// 子树大小
int size[MAXN];

// 每个节点的最大子树大小
int maxSub[MAXN];

// 重心列表
int centroids[MAXN];
int centroidCount;

// 初始化
void init() {
    cnt = 1;
    for (int i = 0; i <= n; i++) {
        head[i] = 0;
        size[i] = 0;
        maxSub[i] = 0;
    }
    centroidCount = 0;
}

// 添加边
void addEdge(int u, int v) {
    next[cnt] = head[u];
    to[cnt] = v;
    head[u] = cnt++;
    
    next[cnt] = head[v];
    to[cnt] = u;
    head[v] = cnt++;
}

// 求两个数的最大值
int max(int a, int b) {
    return a > b ? a : b;
}

// 求两个数的最小值
int min(int a, int b) {
    return a < b ? a : b;
}

// 第一次DFS，计算每个节点的子树大小和最大子树大小
void dfs1(int u, int father) {
    size[u] = 1;
    maxSub[u] = 0;
    
    // 遍历所有子节点
    for (int e = head[u], v; e; e = next[e]) {
        v = to[e];
        if (v != father) {
            dfs1(v, u);
            size[u] += size[v];
            maxSub[u] = max(maxSub[u], size[v]);
        }
    }
    
    // 计算父节点方向的子树大小
    maxSub[u] = max(maxSub[u], n - size[u]);
}

// 找到所有重心
void findCentroids() {
    int minMaxSub = n; // 初始化为最大值
    
    // 找到最小的最大子树大小
    for (int i = 1; i <= n; i++) {
        if (maxSub[i] < minMaxSub) {
            minMaxSub = maxSub[i];
        }
    }
    
    // 收集所有具有最小最大子树大小的节点
    for (int i = 1; i <= n; i++) {
        if (maxSub[i] == minMaxSub) {
            centroids[centroidCount++] = i;
        }
    }
    
    // 排序（冒泡排序）
    for (int i = 0; i < centroidCount - 1; i++) {
        for (int j = 0; j < centroidCount - 1 - i; j++) {
            if (centroids[j] > centroids[j + 1]) {
                int temp = centroids[j];
                centroids[j] = centroids[j + 1];
                centroids[j + 1] = temp;
            }
        }
    }
}

int main() {
    // 由于无法使用输入输出函数，这里只展示算法实现
    // 实际使用时需要添加输入输出代码
    return 0;
}