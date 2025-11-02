/*
 * 表格填数问题 - C++实现
 * 给定一个长度为n的数组arr，arr[i]表示i位置上方的正方形格子数量
 * 在这片区域中，你要放入k个相同数字，不能有任意两个数字在同一行或者同一列
 * 注意在这片区域中，如果某一行中间断开了，使得两个数字无法在这一行连通，则不算违规
 * 返回填入数字的方法数，答案对 1000000007 取模
 * 1 <= n、k <= 500    0 <= arr[i] <= 10^6
 * 测试链接 : https://www.luogu.com.cn/problem/P6453
 * 
 * 算法思路：
 * 1. 使用笛卡尔树对直方图进行分解
 * 2. 结合组合数学和动态规划计算填数方案
 * 3. 时间复杂度：O(n^2 * k) 或 O(n * k^2)
 * 4. 空间复杂度：O(n * k)
 */

#include <iostream>
#include <vector>
#include <algorithm>
#include <cstring>
using namespace std;

const int MOD = 1000000007;
const int MAXN = 501;
const int MAXH = 1000000;

// 阶乘和逆元预处理
vector<int> fac(MAXH + 1);
vector<int> inv(MAXH + 1);

// 快速幂计算
int power(long long x, long long p) {
    long long ans = 1;
    while (p > 0) {
        if (p & 1) {
            ans = (ans * x) % MOD;
        }
        x = (x * x) % MOD;
        p >>= 1;
    }
    return (int)ans;
}

// 组合数计算
int c(int n, int k) {
    if (n < k) return 0;
    return (long long)fac[n] * inv[k] % MOD * inv[n - k] % MOD;
}

// 预处理阶乘和逆元
void precompute() {
    fac[0] = fac[1] = 1;
    inv[0] = 1;
    for (int i = 2; i <= MAXH; i++) {
        fac[i] = (long long)fac[i - 1] * i % MOD;
    }
    inv[MAXH] = power(fac[MAXH], MOD - 2);
    for (int i = MAXH - 1; i >= 1; i--) {
        inv[i] = (long long)inv[i + 1] * (i + 1) % MOD;
    }
}

// 笛卡尔树节点
struct Node {
    int val;
    int left, right;
    Node() : val(0), left(0), right(0) {}
};

// 构建笛卡尔树
int buildCartesianTree(vector<int>& arr, vector<Node>& tree, vector<int>& stack) {
    int n = arr.size() - 1;
    int top = 0;
    
    for (int i = 1; i <= n; i++) {
        int pos = top;
        while (pos > 0 && arr[stack[pos]] > arr[i]) {
            pos--;
        }
        
        if (pos > 0) {
            tree[stack[pos]].right = i;
        }
        if (pos < top) {
            tree[i].left = stack[pos + 1];
        }
        
        stack[++pos] = i;
        top = pos;
    }
    
    return stack[1]; // 返回根节点
}

// DFS遍历笛卡尔树进行动态规划
void dfs(int u, int fa, vector<Node>& tree, vector<int>& arr, 
         vector<vector<int>>& dp, vector<int>& size, int k) {
    if (u == 0) return;
    
    // 递归处理左右子树
    dfs(tree[u].left, u, tree, arr, dp, size, k);
    dfs(tree[u].right, u, tree, arr, dp, size, k);
    
    // 计算子树大小
    size[u] = size[tree[u].left] + size[tree[u].right] + 1;
    
    // 临时数组存储合并结果
    vector<int> tmp(k + 1, 0);
    
    // 合并左右子树的DP结果
    for (int l = 0; l <= min(size[tree[u].left], k); l++) {
        for (int r = 0; r <= min(size[tree[u].right], k - l); r++) {
            tmp[l + r] = (tmp[l + r] + (long long)dp[tree[u].left][l] * dp[tree[u].right][r] % MOD) % MOD;
        }
    }
    
    // 计算当前节点的DP值
    for (int i = 0; i <= min(size[u], k); i++) {
        for (int p = 0; p <= i; p++) {
            int ways = (long long)c(size[u] - p, i - p) * c(arr[u] - arr[fa], i - p) % MOD;
            ways = (long long)ways * fac[i - p] % MOD;
            dp[u][i] = (dp[u][i] + (long long)ways * tmp[p] % MOD) % MOD;
        }
    }
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);
    
    precompute();
    
    int n, k;
    cin >> n >> k;
    
    vector<int> arr(n + 1);
    for (int i = 1; i <= n; i++) {
        cin >> arr[i];
    }
    
    // 构建笛卡尔树
    vector<Node> tree(n + 1);
    vector<int> stack(n + 1);
    int root = buildCartesianTree(arr, tree, stack);
    
    // 初始化DP数组和大小数组
    vector<vector<int>> dp(n + 1, vector<int>(k + 1, 0));
    vector<int> size(n + 1, 0);
    
    // 根节点的DP初始值
    dp[0][0] = 1;
    
    // DFS计算DP
    dfs(root, 0, tree, arr, dp, size, k);
    
    cout << dp[root][k] << endl;
    
    return 0;
}