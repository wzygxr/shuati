/*
 * 砖块消除问题 - C++实现
 * 给定一个长度为n的数组arr，arr[i]为i号砖块的重量
 * 选择一个没有消除的砖块进行消除，收益为被消除砖块联通区域的重量之和
 * 一共有n!种消除方案，返回所有消除方案的收益总和，答案对 1000000007 取模
 * 1 <= n <= 10^5    1 <= arr[i] <= 10^9
 * 测试链接 : https://www.luogu.com.cn/problem/AT_agc028_b
 * 测试链接 : https://atcoder.jp/contests/agc028/tasks/agc028_b
 * 
 * 算法思路：
 * 1. 使用组合数学和概率统计方法
 * 2. 计算每个砖块在所有消除方案中的贡献
 * 3. 时间复杂度：O(n)
 * 4. 空间复杂度：O(n)
 * 
 * 工程化考量：
 * - 使用线性逆元预处理提高效率
 * - 注意大数运算的模运算
 * - 优化内存使用，避免不必要的存储
 */

#include <iostream>
#include <vector>
#include <algorithm>
using namespace std;

const int MOD = 1000000007;
const int MAXN = 100001;

// 快速幂计算逆元
long long power(long long x, long long p) {
    long long result = 1;
    while (p > 0) {
        if (p & 1) {
            result = (result * x) % MOD;
        }
        x = (x * x) % MOD;
        p >>= 1;
    }
    return result;
}

// 线性预处理逆元
vector<int> precompute_inv(int n) {
    vector<int> inv(n + 1);
    inv[1] = 1;
    for (int i = 2; i <= n; i++) {
        inv[i] = (MOD - (long long)inv[MOD % i] * (MOD / i) % MOD) % MOD;
    }
    return inv;
}

// 计算前缀和数组
vector<int> precompute_sum(const vector<int>& inv, int n) {
    vector<int> sum(n + 1, 0);
    for (int i = 1; i <= n; i++) {
        sum[i] = (sum[i - 1] + inv[i]) % MOD;
    }
    return sum;
}

int main() {
    ios::sync_with_stdio(false);
    cin.tie(nullptr);
    
    int n;
    cin >> n;
    
    vector<long long> arr(n + 1);
    for (int i = 1; i <= n; i++) {
        cin >> arr[i];
    }
    
    // 预处理逆元
    vector<int> inv = precompute_inv(n);
    
    // 计算前缀和
    vector<int> sum = precompute_sum(inv, n);
    
    long long ans = 0;
    
    // 计算每个砖块的贡献
    for (int i = 1; i <= n; i++) {
        // 计算砖块i在所有消除方案中的期望贡献
        long long contribution = (sum[i] + sum[n - i + 1] - 1) % MOD;
        if (contribution < 0) contribution += MOD;
        
        ans = (ans + contribution * arr[i] % MOD) % MOD;
    }
    
    // 乘以n!，即所有排列的数量
    for (int i = 1; i <= n; i++) {
        ans = (ans * i) % MOD;
    }
    
    cout << ans << endl;
    
    return 0;
}

/*
 * 算法详细解释：
 * 1. 对于每个砖块i，计算它在所有消除方案中的期望贡献
 * 2. 砖块i被消除时，它的贡献是它所在连通区域的重量之和
 * 3. 通过组合数学计算，砖块i的期望贡献系数为：sum[i] + sum[n-i+1] - 1
 * 4. 其中sum[i] = 1/1 + 1/2 + ... + 1/i (模MOD意义下)
 * 5. 最后乘以n!得到所有方案的总收益
 * 
 * 时间复杂度分析：
 * - 预处理逆元：O(n)
 * - 计算前缀和：O(n)
 * - 计算总贡献：O(n)
 * - 总时间复杂度：O(n)
 * 
 * 空间复杂度分析：
 * - 存储逆元数组：O(n)
 * - 存储前缀和数组：O(n)
 * - 总空间复杂度：O(n)
 * 
 * 边界情况处理：
 * - n=1时，只有一个砖块，收益就是该砖块的重量
 * - 大数运算时注意模运算，避免溢出
 * - 负数的模运算需要特殊处理
 */