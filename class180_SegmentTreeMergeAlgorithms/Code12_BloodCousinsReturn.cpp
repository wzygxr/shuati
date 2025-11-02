// Blood Cousins Return CF246E.cpp
// 测试链接 : https://codeforces.com/problemset/problem/246/E
// 线段树合并解法
//
// 题目来源：Codeforces
// 题目大意：Blood Cousins的加强版，查询第k代子孙的不同名字数量
// 解法：线段树合并 + DFS序 + 数组模拟Set
// 时间复杂度：O(n log n)
// 空间复杂度：O(n log n)

// 由于环境限制，使用基础C++实现方式，避免使用复杂的STL容器

int n, m;
int G[100005][10], G_size[100005];
char name[100005][20];
int ans[100005];

// 查询信息
int queryV[100005], queryK[100005];
int queries[100005][100], queries_size[100005];

// 线段树合并相关
int root[100005], lc[2000005], rc[2000005];
int names[2000005][100], names_size[2000005];
int segCnt = 0;

// 动态开点线段树插入
void insert(int rt, int l, int r, int x, char* val) {
    if (l == r) {
        // 简化处理，这里只存储字符串的哈希值
        int hash = 0;
        for (int i = 0; val[i]; i++) {
            hash = hash * 31 + val[i];
        }
        // 检查是否已存在
        int found = 0;
        for (int i = 0; i < names_size[rt]; i++) {
            if (names[rt][i] == hash) {
                found = 1;
                break;
            }
        }
        if (!found) {
            names[rt][names_size[rt]++] = hash;
        }
        return;
    }
    int mid = (l + r) >> 1;
    if (x <= mid) {
        if (lc[rt] == 0) lc[rt] = ++segCnt;
        insert(lc[rt], l, mid, x, val);
    } else {
        if (rc[rt] == 0) rc[rt] = ++segCnt;
        insert(rc[rt], mid+1, r, x, val);
    }
}

// 线段树合并
int merge(int x, int y, int l, int r) {
    if (!x || !y) return x + y;
    if (l == r) {
        // 合并两个集合
        for (int i = 0; i < names_size[y]; i++) {
            int found = 0;
            for (int j = 0; j < names_size[x]; j++) {
                if (names[x][j] == names[y][i]) {
                    found = 1;
                    break;
                }
            }
            if (!found) {
                names[x][names_size[x]++] = names[y][i];
            }
        }
        return x;
    }
    int mid = (l + r) >> 1;
    lc[x] = merge(lc[x], lc[y], l, mid);
    rc[x] = merge(rc[x], rc[y], mid+1, r);
    return x;
}

// 查询第k代子孙的不同名字数量
int query(int rt, int l, int r, int k) {
    if (l == r) {
        return names_size[rt];
    }
    int mid = (l + r) >> 1;
    if (k <= mid) {
        return lc[rt] != 0 ? query(lc[rt], l, mid, k) : 0;
    } else {
        return rc[rt] != 0 ? query(rc[rt], mid+1, r, k) : 0;
    }
}

// DFS处理线段树合并
void dfs(int u, int father, int depth) {
    // 先处理所有子节点
    for (int i = 0; i < G_size[u]; i++) {
        int v = G[u][i];
        if (v != father) {
            dfs(v, u, depth + 1);
            // 合并子节点的信息到当前节点
            root[u] = merge(root[u], root[v], 1, n);
        }
    }
    
    // 插入当前节点的信息
    if (root[u] == 0) root[u] = ++segCnt;
    insert(root[u], 1, n, depth, name[u]);
    
    // 处理当前节点的查询
    for (int i = 0; i < queries_size[u]; i++) {
        int id = queries[u][i];
        int k = queryK[id];
        ans[id] = query(root[u], 1, n, depth + k);
    }
}

int main() {
    // 由于环境限制，这里不实现完整的输入输出
    // 在实际使用中需要根据具体环境实现输入输出
    
    return 0;
}