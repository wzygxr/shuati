#include <iostream>
#include <vector>
#include <algorithm>
#include <cstring>
#include <set>
using namespace std;

/**
 * 洛谷P4602 [CTSC2018]混合果汁 - C++实现
 * 题目来源：https://www.luogu.com.cn/problem/P4602
 * 题目描述：线段树与整体二分结合的优化问题
 * 
 * 问题描述：
 * 有n种果汁，每种果汁有美味度d，单价p，数量l。现在有m个询问，每个询问给出需要的总数量g和最高预算v，
 * 要求选一些果汁，使得总数量至少g，总费用不超过v，并且所选果汁的最低美味度尽可能大。
 * 
 * 解题思路：
 * 使用整体二分法来二分可能的最低美味度d，对于每个d，使用线段树维护满足d' >= d的果汁，
 * 并支持查询在预算v下最多能买多少果汁。
 * 
 * 时间复杂度：O((n+m) * log(n) * log(max_p))
 * 空间复杂度：O(n + m)
 */

const int MAXN = 100005;
const int MAXM = 100005;
const long long INF = 1LL << 60;

// 果汁信息
int d[MAXN]; // 美味度
int p[MAXN]; // 单价
int l[MAXN]; // 数量
int n, m;

// 查询信息
int g[MAXM]; // 需要的总数量
long long v[MAXM]; // 最高预算
int ans[MAXM]; // 答案
int qid[MAXM]; // 查询的原始顺序

// 离散化
vector<int> disD, disP;

// 线段树结构
struct Node {
    int sumL; // 总数量
    long long sumCost; // 总费用
    Node() : sumL(0), sumCost(0) {}
};

Node tree[MAXN * 4];

// 线段树更新
void update(int o, int l, int r, int pos, int addL, long long addCost) {
    tree[o].sumL += addL;
    tree[o].sumCost += addCost;
    if (l == r) {
        return;
    }
    int mid = (l + r) >> 1;
    if (pos <= mid) {
        update(o << 1, l, mid, pos, addL, addCost);
    } else {
        update(o << 1 | 1, mid + 1, r, pos, addL, addCost);
    }
}

// 线段树查询：最多能买多少果汁，总费用不超过maxCost
int query(int o, int l, int r, int need, long long maxCost) {
    if (need <= 0) return 0;
    if (tree[o].sumL == 0 || tree[o].sumCost > maxCost) {
        return 0;
    }
    if (l == r) {
        int canBuy = min(need, tree[o].sumL);
        long long costPerUnit = disP[l-1]; // 注意这里要转换回原始价格
        int maxAffordable = (int)(maxCost / costPerUnit);
        return min(canBuy, maxAffordable);
    }
    int mid = (l + r) >> 1;
    int leftSon = o << 1;
    int rightSon = o << 1 | 1;
    
    // 优先选择价格低的（左子树）
    if (tree[leftSon].sumCost <= maxCost) {
        // 左子树全部购买，再买右子树的
        return tree[leftSon].sumL + query(rightSon, mid + 1, r, need - tree[leftSon].sumL, maxCost - tree[leftSon].sumCost);
    } else {
        // 只买左子树的一部分
        return query(leftSon, l, mid, need, maxCost);
    }
}

// 离散化处理
void discrete() {
    // 离散化美味度
    set<int> dSet;
    for (int i = 1; i <= n; i++) {
        dSet.insert(d[i]);
    }
    disD.assign(dSet.begin(), dSet.end());
    
    // 离散化价格
    set<int> pSet;
    for (int i = 1; i <= n; i++) {
        pSet.insert(p[i]);
    }
    disP.assign(pSet.begin(), pSet.end());
    
    // 转换为离散化后的值（从1开始）
    for (int i = 1; i <= n; i++) {
        d[i] = lower_bound(disD.begin(), disD.end(), d[i]) - disD.begin() + 1;
        p[i] = lower_bound(disP.begin(), disP.end(), p[i]) - disP.begin() + 1;
    }
}

// 整体二分核心函数
void solve(int ql, int qr, int l, int r) {
    if (ql > qr || l > r) return;
    
    if (l == r) {
        // 所有查询的答案都是disD[l-1]
        for (int i = ql; i <= qr; i++) {
            ans[qid[i]] = disD[l-1];
        }
        return;
    }
    
    int mid = (l + r + 1) >> 1;
    
    // 将所有美味度>=mid的果汁加入线段树
    for (int i = 1; i <= n; i++) {
        if (d[i] >= mid) {
            int pos = p[i];
            long long cost = (long long)disP[pos-1] * l[i];
            update(1, 1, disP.size(), pos, l[i], cost);
        }
    }
    
    // 记录哪些查询可以满足
    vector<int> left, right;
    
    for (int i = ql; i <= qr; i++) {
        int idx = qid[i];
        int maxBuy = query(1, 1, disP.size(), g[idx], v[idx]);
        if (maxBuy >= g[idx]) {
            // 可以满足，答案可能更大
            left.push_back(idx);
        } else {
            // 无法满足，答案必须更小
            right.push_back(idx);
        }
    }
    
    // 从线段树中移除所有果汁
    for (int i = 1; i <= n; i++) {
        if (d[i] >= mid) {
            int pos = p[i];
            long long cost = (long long)disP[pos-1] * l[i];
            update(1, 1, disP.size(), pos, -l[i], -cost);
        }
    }
    
    // 合并查询顺序
    int ptr = ql;
    for (int x : left) {
        qid[ptr++] = x;
    }
    for (int x : right) {
        qid[ptr++] = x;
    }
    
    // 递归处理左右两部分
    solve(ql, ql + left.size() - 1, mid, r);
    solve(ql + left.size(), qr, l, mid - 1);
}

int main() {
    // 输入优化
    ios::sync_with_stdio(false);
    cin.tie(0);
    
    // 读取输入
    cin >> n >> m;
    
    // 读取果汁信息
    for (int i = 1; i <= n; i++) {
        cin >> d[i] >> p[i] >> l[i];
    }
    
    // 离散化
    discrete();
    
    // 读取查询
    for (int i = 1; i <= m; i++) {
        cin >> g[i] >> v[i];
        qid[i] = i;
    }
    
    // 初始化线段树
    memset(tree, 0, sizeof(tree));
    
    // 整体二分求解
    solve(1, m, 1, disD.size());
    
    // 输出结果
    for (int i = 1; i <= m; i++) {
        cout << ans[i] << '\n';
    }
    
    return 0;
}