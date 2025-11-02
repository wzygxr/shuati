#include <iostream>
#include <vector>
#include <map>
#include <algorithm>
using namespace std;

// CGCDSSQ - Codeforces 475D
// 题目大意：
// 给定一个长度为n的整数序列a[1], a[2], ..., a[n]
// 有q个查询x[1], x[2], ..., x[q]
// 对于每个查询x[i]，需要计算有多少个区间[l, r]满足gcd(a[l], a[l+1], ..., a[r]) = x[i]
// 其中gcd表示最大公约数

// 解题思路：
// 1. 使用Sparse Table预处理区间GCD查询
// 2. 利用GCD的性质：随着区间长度增加，GCD值单调不增
// 3. 对于每个左端点，不同的GCD值最多有log(max_value)种
// 4. 预处理所有可能的GCD值及其出现次数
// 5. 对于每个查询，直接输出对应的计数

// 时间复杂度分析：
// 预处理Sparse Table: O(n * logn)
// 预处理所有GCD值: O(n * log(max_value))
// 查询: O(1)
// 总时间复杂度: O(n * log(max_value) + q)

const int MAXN = 100005;

// 输入参数
int n, q;
int a[MAXN];

// Sparse Table数组，用于区间GCD查询
int st[MAXN][20];

// log数组
int log2_[MAXN];

// 记录每个GCD值出现的次数
map<int, long long> gcdCount;

// 计算两个数的最大公约数
int gcd(int a, int b) {
    return b == 0 ? a : gcd(b, a % b);
}

// 预处理log2数组
void precomputeLog() {
    log2_[1] = 0;
    for (int i = 2; i <= n; i++) {
        log2_[i] = log2_[i >> 1] + 1;
    }
}

// 构建Sparse Table用于区间GCD查询
void buildSparseTable() {
    // 初始化Sparse Table的第一层
    for (int i = 1; i <= n; i++) {
        st[i][0] = a[i];
    }
    
    // 动态规划构建Sparse Table
    for (int j = 1; (1 << j) <= n; j++) {
        for (int i = 1; i + (1 << j) - 1 <= n; i++) {
            st[i][j] = gcd(st[i][j - 1], st[i + (1 << (j - 1))][j - 1]);
        }
    }
}

// 查询区间[l,r]的GCD值
int queryGCD(int l, int r) {
    int k = log2_[r - l + 1];
    return gcd(st[l][k], st[r - (1 << k) + 1][k]);
}

// 预处理所有可能的GCD值及其出现次数
void preprocessGCD() {
    // 对于每个左端点
    for (int i = 1; i <= n; i++) {
        // 从左端点开始，向右扩展区间
        int j = i;
        while (j <= n) {
            // 当前区间的GCD值
            int currentGCD = queryGCD(i, j);
            
            // 找到GCD值保持不变的最长区间
            int left = j, right = n;
            int pos = j;
            
            while (left <= right) {
                int mid = (left + right) / 2;
                if (queryGCD(i, mid) == currentGCD) {
                    pos = mid;
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }
            
            // 更新GCD值的计数
            gcdCount[currentGCD] += (pos - j + 1);
            
            // 移动到下一个可能的GCD值
            j = pos + 1;
        }
    }
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(0);
    
    // 读取输入
    cin >> n;
    for (int i = 1; i <= n; i++) {
        cin >> a[i];
    }
    
    // 预处理log2数组
    precomputeLog();
    
    // 构建Sparse Table
    buildSparseTable();
    
    // 预处理所有可能的GCD值及其出现次数
    preprocessGCD();
    
    // 处理查询
    cin >> q;
    for (int i = 0; i < q; i++) {
        int x;
        cin >> x;
        cout << gcdCount[x] << "\n";
    }
    
    return 0;
}