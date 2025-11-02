// COT2 - Count on a tree II (SPOJ SP10707) - 树上莫队
// 题目来源: SPOJ SP10707
// 题目链接: https://www.spoj.com/problems/COT2/
// 洛谷链接: https://www.luogu.com.cn/problem/SP10707
// 题意: 给定一棵树，每个节点有一个权值，每次询问两个节点之间的路径上有多少种不同的权值
// 算法思路: 使用树上莫队算法，通过欧拉序将树上问题转化为序列问题，利用DFS序构造欧拉序
// 时间复杂度: O((n + q) * sqrt(n))
// 空间复杂度: O(n)
// 适用场景: 树上路径不同节点值个数查询问题

#include <stdio.h>
#include <stdlib.h>
#include <math.h>
#include <algorithm>
#include <cstring>
#include <vector>
using namespace std;

const int MAXN = 40005;
const int MAXM = 100005;

// 链式前向星存图
struct Edge {
    int to, next;
} edges[MAXM * 2];
int head[MAXN], edgeCnt = 0;

// 树上信息
int depth[MAXN], fa[MAXN], up[MAXN][20];

// 欧拉序
int euler[MAXN * 2], first[MAXN], eulerCnt = 0;

// 莫队相关
int arr[MAXN * 2], block[MAXN * 2], cnt[MAXN * 2];
int blockSize, answer = 0, ans[MAXM];
bool visited[MAXN];

struct Query {
    int u, v, lca, id;
    
    bool operator<(const Query& other) const {
        if (block[u] != block[other.u]) {
            return block[u] < block[other.u];
        }
        return v < other.v;
    }
} query[MAXM];

// 添加边
void addEdge(int u, int v) {
    edges[edgeCnt] = {v, head[u]};
    head[u] = edgeCnt++;
    edges[edgeCnt] = {u, head[v]};
    head[v] = edgeCnt++;
}

// DFS预处理欧拉序和LCA
void dfs(int u, int father, int dep) {
    fa[u] = father;
    depth[u] = dep;
    first[u] = ++eulerCnt;
    euler[eulerCnt] = u;
    
    for (int i = head[u]; i != -1; i = edges[i].next) {
        int v = edges[i].to;
        if (v != father) {
            dfs(v, u, dep + 1);
            euler[++eulerCnt] = u;
        }
    }
}

// 预处理倍增祖先
void initLCA(int n) {
    for (int i = 1; i <= n; i++) {
        up[i][0] = fa[i];
    }
    
    for (int j = 1; (1 << j) <= n; j++) {
        for (int i = 1; i <= n; i++) {
            if (up[i][j - 1] != -1) {
                up[i][j] = up[up[i][j - 1]][j - 1];
            }
        }
    }
}

// 求LCA
int lca(int u, int v) {
    if (depth[u] < depth[v]) {
        swap(u, v);
    }
    
    for (int i = 19; i >= 0; i--) {
        if (depth[u] - (1 << i) >= depth[v]) {
            u = up[u][i];
        }
    }
    
    if (u == v) return u;
    
    for (int i = 19; i >= 0; i--) {
        if (up[u][i] != -1 && up[u][i] != up[v][i]) {
            u = up[u][i];
            v = up[v][i];
        }
    }
    
    return fa[u];
}

// 添加元素
void add(int pos) {
    if (cnt[arr[pos]] == 0) {
        answer++;
    }
    cnt[arr[pos]]++;
}

// 删除元素
void remove(int pos) {
    cnt[arr[pos]]--;
    if (cnt[arr[pos]] == 0) {
        answer--;
    }
}

int main() {
    int n, q;
    scanf("%d%d", &n, &q);
    
    // 初始化链式前向星
    memset(head, -1, sizeof(head));
    
    int values[MAXN];
    for (int i = 1; i <= n; i++) {
        scanf("%d", &values[i]);
    }
    
    for (int i = 1; i < n; i++) {
        int u, v;
        scanf("%d%d", &u, &v);
        addEdge(u, v);
    }
    
    // 初始化欧拉序
    eulerCnt = 0;
    dfs(1, -1, 0);
    initLCA(n);
    
    // 构造莫队数组
    for (int i = 1; i <= eulerCnt; i++) {
        arr[i] = values[euler[i]];
    }
    
    // 计算块大小
    blockSize = (int)sqrt((double)eulerCnt);
    
    // 为每个位置分配块
    for (int i = 1; i <= eulerCnt; i++) {
        block[i] = (i - 1) / blockSize + 1;
    }
    
    // 读取查询
    for (int i = 1; i <= q; i++) {
        int u, v;
        scanf("%d%d", &u, &v);
        int lcaNode = lca(u, v);
        
        // 树上莫队的特殊处理
        if (first[u] > first[v]) {
            swap(u, v);
        }
        
        query[i].u = first[u];
        query[i].v = first[v];
        query[i].lca = first[lcaNode];
        query[i].id = i;
    }
    
    // 按照莫队算法排序
    sort(query + 1, query + q + 1);
    
    int curL = 1, curR = 0;
    memset(visited, false, sizeof(visited));
    
    // 处理每个查询
    for (int i = 1; i <= q; i++) {
        int L = query[i].u;
        int R = query[i].v;
        int lcaPos = query[i].lca;
        int idx = query[i].id;
        
        // 扩展右边界
        while (curR < R) {
            curR++;
            int node = euler[curR];
            if (visited[node]) {
                remove(curR);
            } else {
                add(curR);
            }
            visited[node] = !visited[node];
        }
        
        // 收缩左边界
        while (curL > L) {
            curL--;
            int node = euler[curL];
            if (visited[node]) {
                remove(curL);
            } else {
                add(curL);
            }
            visited[node] = !visited[node];
        }
        
        // 收缩右边界
        while (curR > R) {
            int node = euler[curR];
            if (visited[node]) {
                remove(curR);
            } else {
                add(curR);
            }
            visited[node] = !visited[node];
            curR--;
        }
        
        // 扩展左边界
        while (curL < L) {
            int node = euler[curL];
            if (visited[node]) {
                remove(curL);
            } else {
                add(curL);
            }
            visited[node] = !visited[node];
            curL++;
        }
        
        // 特殊处理LCA
        if (lcaPos != L && lcaPos != R) {
            int node = euler[lcaPos];
            if (visited[node]) {
                remove(lcaPos);
            } else {
                add(lcaPos);
            }
            visited[node] = !visited[node];
        }
        
        ans[idx] = answer;
        
        // 恢复LCA状态
        if (lcaPos != L && lcaPos != R) {
            int node = euler[lcaPos];
            visited[node] = !visited[node];
        }
    }
    
    // 输出结果
    for (int i = 1; i <= q; i++) {
        printf("%d\n", ans[i]);
    }
    
    return 0;
}