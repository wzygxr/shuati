// 统计可能的树根数目
// Alice 有一棵 n 个节点的树，节点编号为 0 到 n - 1 。树用一个长度为 n - 1 的二维整数数组 edges 表示，
// 其中 edges[i] = [ai, bi] ，表示树中节点 ai 和 bi 之间存在一条边。
// Bob 有一棵与 Alice 一样的树，Bob 知道这棵树的根节点，而 Alice 不知道。
// Bob 给了 Alice 一些关于树根的猜测，其中 guesses[i] = [ui, vi] 表示 Bob 猜测树中 ui 是 vi 的父节点。
// Alice 会告诉 Bob 这些猜测中有多少是正确的。
// 给你二维整数数组 edges ，Bob 的所有猜测和整数 k ，请你返回可能成为树根的 节点数目 。
// 如果没有这样的树，则返回 0 。
// 测试链接 : https://leetcode.cn/problems/count-number-of-possible-root-nodes/
// 提交以下的code，提交时请把类名改成"Main"，可以通过所有用例

/*
题目解析：
这是一道典型的换根DP问题。我们需要统计有多少个节点可以作为根，使得至少有k个猜测是正确的。

算法思路：
1. 第一次DFS：以节点0为根，计算每个节点子树内的正确猜测数
   - 对于每条边u-v，如果猜测中存在(u,v)，则表示u是v的父节点，这是一个正确猜测
   - 统计以0为根时的正确猜测数

2. 第二次DFS：换根DP，计算每个节点作为根时的正确猜测数
   - 当从节点u换根到节点v时：
     * 原来u是v的父节点，现在v是u的父节点
     * 如果猜测中存在(u,v)，则换根后这个猜测就不再正确
     * 如果猜测中存在(v,u)，则换根后这个猜测就变为正确
     * 因此：dp[v] = dp[u] - (u,v存在?) + (v,u存在?)

时间复杂度：O(n) - 两次DFS遍历
空间复杂度：O(n) - 存储图和DP数组
是否为最优解：是，换根DP是解决此类问题的最优方法
*/

#include <iostream>
#include <vector>
#include <set>
#include <cstdio>
using namespace std;

const int MAXN = 100001;

int n;
int head[MAXN], next_edge[MAXN << 1], to[MAXN << 1], cnt;
// 存储所有猜测，用于快速查找
set<pair<int, int> > guesses;
// dp[i]: 以节点i为根时的正确猜测数
int dp[MAXN];

void build() {
    cnt = 1;
    for (int i = 0; i <= n; i++) {
        head[i] = 0;
        dp[i] = 0;
    }
    guesses.clear();
}

void addEdge(int u, int v) {
    next_edge[cnt] = head[u];
    to[cnt] = v;
    head[u] = cnt++;
}

// 第一次DFS：计算以节点0为根时的正确猜测数
void dfs1(int u, int f) {
    for (int e = head[u], v; e; e = next_edge[e]) {
        v = to[e];
        if (v != f) {
            dfs1(v, u);
            // 如果猜测中存在(u,v)，则这是一个正确的猜测
            if (guesses.find(make_pair(u, v)) != guesses.end()) {
                dp[0]++;
            }
        }
    }
}

// 第二次DFS：换根DP，计算每个节点作为根时的正确猜测数
void dfs2(int u, int f) {
    for (int e = head[u], v; e; e = next_edge[e]) {
        v = to[e];
        if (v != f) {
            // 换根公式：
            // 原来u是v的父节点，现在v是u的父节点
            // 如果猜测中存在(u,v)，则换根后这个猜测就不再正确 (-1)
            // 如果猜测中存在(v,u)，则换根后这个猜测就变为正确 (+1)
            dp[v] = dp[u];
            if (guesses.find(make_pair(u, v)) != guesses.end()) {
                dp[v]--;
            }
            if (guesses.find(make_pair(v, u)) != guesses.end()) {
                dp[v]++;
            }
            dfs2(v, u);
        }
    }
}

int main() {
    int testCase;
    scanf("%d", &testCase);
    for (int t = 1; t <= testCase; t++) {
        scanf("%d", &n);
        build();
        for (int i = 1, u, v; i < n; i++) {
            scanf("%d%d", &u, &v);
            addEdge(u, v);
            addEdge(v, u);
        }
        int m, k;
        scanf("%d%d", &m, &k);
        // 读取所有猜测
        for (int i = 1, u, v; i <= m; i++) {
            scanf("%d%d", &u, &v);
            guesses.insert(make_pair(u, v));
        }
        // 第一次DFS计算以节点0为根时的正确猜测数
        dfs1(0, -1);
        // 第二次DFS换根计算所有节点作为根时的正确猜测数
        dfs2(0, -1);
        // 统计满足条件的根节点数目
        int ans = 0;
        for (int i = 0; i < n; i++) {
            if (dp[i] >= k) {
                ans++;
            }
        }
        printf("%d\n", ans);
    }
    return 0;
}