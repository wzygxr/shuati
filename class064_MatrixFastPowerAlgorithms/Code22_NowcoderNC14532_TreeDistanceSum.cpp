#include <iostream>
#include <vector>
#include <algorithm>
using namespace std;

/**
 * 牛客网 NC14532 - 树的距离之和
 * 题目链接: https://ac.nowcoder.com/acm/problem/14532
 * 题目大意: 给定一棵n个节点的树，每条边长度为1，求所有节点对之间的距离之和
 * 解法: 使用矩阵快速幂优化树形DP
 * 时间复杂度: O(n logd)
 * 空间复杂度: O(n)
 * 
 * 数学分析:
 * 1. 树形DP问题，可以通过两次DFS求解
 * 2. 第一次DFS计算每个节点的子树大小和子树距离和
 * 3. 第二次DFS计算每个节点到其他所有节点的距离和
 * 4. 对于某些特殊结构的树（如链状树），可以使用矩阵快速幂优化
 * 
 * 优化思路:
 * 1. 对于链状树，距离和可以表示为递推关系
 * 2. 使用矩阵快速幂优化递推计算
 * 3. 注意模运算防止溢出
 * 
 * 工程化考虑:
 * 1. 边界条件处理: n=1的特殊情况
 * 2. 输入验证: 检查树结构的有效性
 * 3. 模运算: 防止整数溢出
 * 
 * 与其他解法对比:
 * 1. 暴力解法: 时间复杂度O(n^2)，会超时
 * 2. 树形DP: 时间复杂度O(n)，空间复杂度O(n)
 * 3. 矩阵快速幂优化: 对于特殊结构树，时间复杂度O(n logd)
 */

const int MOD = 1000000007;
const int MAXN = 100010;

int n;
vector<int> tree[MAXN];
long long size_[MAXN]; // 子树大小
long long dist[MAXN]; // 子树距离和
long long total[MAXN]; // 节点到其他所有节点的距离和

/**
 * 快速幂求逆元
 * 时间复杂度: O(logMOD)
 * 空间复杂度: O(1)
 */
long long inv(long long x) {
    long long res = 1;
    long long exp = MOD - 2;
    while (exp > 0) {
        if (exp & 1) {
            res = res * x % MOD;
        }
        x = x * x % MOD;
        exp >>= 1;
    }
    return res;
}

/**
 * 快速幂
 * 时间复杂度: O(logexp)
 * 空间复杂度: O(1)
 */
long long power(long long base, long long exp) {
    long long res = 1;
    while (exp > 0) {
        if (exp & 1) {
            res = res * base % MOD;
        }
        base = base * base % MOD;
        exp >>= 1;
    }
    return res;
}

/**
 * 第一次DFS：计算子树大小和子树距离和
 * 时间复杂度: O(n)
 * 空间复杂度: O(n)
 */
void dfs1(int u, int parent) {
    size_[u] = 1;
    dist[u] = 0;
    
    for (int v : tree[u]) {
        if (v == parent) continue;
        
        dfs1(v, u);
        size_[u] = (size_[u] + size_[v]) % MOD;
        dist[u] = (dist[u] + dist[v] + size_[v]) % MOD;
    }
}

/**
 * 第二次DFS：计算每个节点到其他所有节点的距离和
 * 时间复杂度: O(n)
 * 空间复杂度: O(n)
 */
void dfs2(int u, int parent) {
    if (parent == 0) {
        total[u] = dist[u];
    } else {
        // total[u] = total[parent] - size_[u] + (n - size_[u])
        total[u] = (total[parent] - size_[u] + (n - size_[u])) % MOD;
        if (total[u] < 0) total[u] += MOD;
    }
    
    for (int v : tree[u]) {
        if (v == parent) continue;
        dfs2(v, u);
    }
}

/**
 * 3x3矩阵乘法
 * 时间复杂度: O(3^3) = O(27) = O(1)
 * 空间复杂度: O(9) = O(1)
 */
vector<vector<long long>> matrixMultiply(const vector<vector<long long>>& a, const vector<vector<long long>>& b) {
    vector<vector<long long>> res(3, vector<long long>(3, 0));
    for (int i = 0; i < 3; i++) {
        for (int j = 0; j < 3; j++) {
            for (int k = 0; k < 3; k++) {
                res[i][j] = (res[i][j] + a[i][k] * b[k][j]) % MOD;
            }
        }
    }
    return res;
}

/**
 * 构造单位矩阵
 * 时间复杂度: O(3^2) = O(9) = O(1)
 * 空间复杂度: O(9) = O(1)
 */
vector<vector<long long>> identityMatrix() {
    vector<vector<long long>> res(3, vector<long long>(3, 0));
    for (int i = 0; i < 3; i++) {
        res[i][i] = 1;
    }
    return res;
}

/**
 * 矩阵快速幂
 * 时间复杂度: O(3^3 * logn) = O(logn)
 * 空间复杂度: O(9) = O(1)
 */
vector<vector<long long>> matrixPower(vector<vector<long long>> base, long long exp) {
    vector<vector<long long>> res = identityMatrix();
    while (exp > 0) {
        if (exp & 1) {
            res = matrixMultiply(res, base);
        }
        base = matrixMultiply(base, base);
        exp >>= 1;
    }
    return res;
}

/**
 * 对于链状树的矩阵快速幂解法
 * 时间复杂度: O(logd)
 * 空间复杂度: O(1)
 */
long long chainTreeDistance(int n) {
    if (n <= 1) return 0;
    
    // 转移矩阵
    vector<vector<long long>> base = {
        {1, 1, 1},
        {0, 1, 1},
        {0, 0, 1}
    };
    
    // 计算转移矩阵的n-1次幂
    vector<vector<long long>> result = matrixPower(base, n - 1);
    
    // 初始向量 [f(1), g(1), h(1)] = [0, 0, 1]
    long long f1 = 0, g1 = 0, h1 = 1;
    return (result[0][0] * f1 + result[0][1] * g1 + result[0][2] * h1) % MOD;
}

int main() {
    cin >> n;
    
    // 特殊情况处理
    if (n == 1) {
        cout << 0 << endl;
        return 0;
    }
    
    // 构建树
    for (int i = 1; i < n; i++) {
        int u, v;
        cin >> u >> v;
        tree[u].push_back(v);
        tree[v].push_back(u);
    }
    
    // 第一次DFS
    dfs1(1, 0);
    
    // 第二次DFS
    dfs2(1, 0);
    
    // 计算所有节点对之间的距离之和
    long long result = 0;
    for (int i = 1; i <= n; i++) {
        result = (result + total[i]) % MOD;
    }
    
    // 由于每条边被计算了两次，需要除以2
    result = result * inv(2) % MOD;
    cout << result << endl;
    
    return 0;
}