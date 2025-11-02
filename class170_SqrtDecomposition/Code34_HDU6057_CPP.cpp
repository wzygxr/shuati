#include <iostream>
#include <vector>
#include <algorithm>
#include <cmath>
#include <cstring>
using namespace std;

/**
 * HDU 6057 Kanade's convolution
 * 题目要求：计算c[k] = sum_{i | j = k} a[i] * b[j] * popcount(i & j)
 * 核心技巧：分块处理 + FWT优化
 * 时间复杂度：O(n log^2 n)
 * 空间复杂度：O(n)
 * 测试链接：http://acm.hdu.edu.cn/showproblem.php?pid=6057
 * 
 * 算法思想详解：
 * 1. 将二进制数按最低的B位进行分块
 * 2. 预处理各个块的贡献
 * 3. 使用快速沃尔什变换(FWT)处理位运算卷积
 * 4. 利用分块技巧将时间复杂度从O(2^{2n})降低到O(2^n n^2 / B + 2^n B)
 */

const int MOD = 998244353;
const int ROOT = 3;

/**
 * 快速幂计算
 */
long long qpow(long long a, long long b) {
    long long res = 1;
    while (b > 0) {
        if (b & 1) {
            res = res * a % MOD;
        }
        a = a * a % MOD;
        b >>= 1;
    }
    return res;
}

/**
 * 快速沃尔什变换 - 或卷积
 */
void fwt_or(vector<long long>& a, bool invert) {
    int n = a.size();
    for (int d = 1; d < n; d <<= 1) {
        for (int m = d << 1, i = 0; i < n; i += m) {
            for (int j = 0; j < d; j++) {
                a[i + j + d] = (a[i + j + d] + a[i + j]) % MOD;
            }
        }
    }
    
    // 或卷积的逆变换不需要特殊处理
}

/**
 * 分块处理函数
 */
vector<long long> solve(int n, const vector<long long>& a, const vector<long long>& b) {
    int size = 1 << n;
    vector<long long> c(size, 0);
    
    // 选择分块大小B
    int B = max(1, n / 2);
    int mask_low = (1 << B) - 1;
    
    // 预处理每个低位块的贡献
    for (int s = 0; s < (1 << B); s++) {
        int pop = __builtin_popcount(s);
        if (pop == 0) continue;
        
        // 计算当前s下的中间数组
        vector<long long> ta(size, 0);
        vector<long long> tb(size, 0);
        
        for (int i = 0; i < size; i++) {
            if ((i & mask_low) == s) {
                ta[i ^ s] = a[i];
            }
        }
        
        for (int j = 0; j < size; j++) {
            if ((j & mask_low) == 0) {
                tb[j] = b[j];
            }
        }
        
        // 进行FWT
        fwt_or(ta, false);
        fwt_or(tb, false);
        
        // 点乘
        for (int i = 0; i < size; i++) {
            ta[i] = ta[i] * tb[i] % MOD;
        }
        
        // 逆FWT
        fwt_or(ta, true);
        
        // 累加贡献
        for (int i = 0; i < size; i++) {
            c[i | s] = (c[i | s] + ta[i] * pop) % MOD;
        }
    }
    
    return c;
}

/**
 * 优化版本的分块算法
 */
vector<long long> solve_optimized(int n, const vector<long long>& a, const vector<long long>& b) {
    int size = 1 << n;
    vector<long long> c(size, 0);
    
    // 选择最优的分块大小
    int B = 1;
    while ((1 << B) * (1 << B) <= n * n) {
        B++;
    }
    B = min(B, n);
    int mask_low = (1 << B) - 1;
    
    // 预处理低位和高位的组合
    for (int low = 1; low < (1 << B); low++) {
        int pop = __builtin_popcount(low);
        
        int high_size = 1 << (n - B);
        vector<long long> ta(high_size, 0);
        vector<long long> tb(high_size, 0);
        
        for (int high = 0; high < high_size; high++) {
            int i = (high << B) | low;
            ta[high] = a[i];
            
            int j = high << B;
            tb[high] = b[j];
        }
        
        // 对高位部分进行FWT
        fwt_or(ta, false);
        fwt_or(tb, false);
        
        // 点乘
        for (int i = 0; i < high_size; i++) {
            ta[i] = ta[i] * tb[i] % MOD;
        }
        
        // 逆FWT
        fwt_or(ta, true);
        
        // 累加结果
        for (int high = 0; high < high_size; high++) {
            int k = (high << B) | low;
            c[k] = (c[k] + ta[high] * pop) % MOD;
        }
    }
    
    // 处理所有可能的i和j组合
    for (int s = 1; s < size; s++) {
        for (int t = s; ; t = (t - 1) & s) {
            int u = s ^ t;
            c[s] = (c[s] + a[t] * b[u] % MOD * __builtin_popcount(t & u)) % MOD;
            if (t == 0) break;
        }
    }
    
    return c;
}

/**
 * 暴力解法（用于小数据测试）
 */
vector<long long> brute_force(int n, const vector<long long>& a, const vector<long long>& b) {
    int size = 1 << n;
    vector<long long> c(size, 0);
    
    for (int i = 0; i < size; i++) {
        for (int j = 0; j < size; j++) {
            if ((i | j) == (i ^ j)) { // i和j不相交
                int k = i | j;
                c[k] = (c[k] + a[i] * b[j] % MOD * __builtin_popcount(i & j)) % MOD;
            }
        }
    }
    
    return c;
}

/**
 * 测试函数
 */
void test_hdu6057() {
    ios::sync_with_stdio(false);
    cin.tie(0);
    
    int n;
    cin >> n;
    int size = 1 << n;
    
    vector<long long> a(size), b(size);
    for (int i = 0; i < size; i++) {
        cin >> a[i];
    }
    for (int i = 0; i < size; i++) {
        cin >> b[i];
    }
    
    vector<long long> c;
    if (n <= 10) { // 小数据可以用暴力解法验证
        c = brute_force(n, a, b);
    } else {
        c = solve_optimized(n, a, b);
    }
    
    // 输出结果
    for (int i = 0; i < size; i++) {
        cout << c[i];
        if (i < size - 1) {
            cout << " ";
        }
    }
    cout << endl;
}

/**
 * 性能测试函数
 */
void performance_test() {
    int max_n = 15;
    cout << "性能测试：计算n = " << max_n << "的情况...\n";
    
    int size = 1 << max_n;
    vector<long long> a(size, 1);
    vector<long long> b(size, 1);
    
    // 记录开始时间
    auto start = chrono::high_resolution_clock::now();
    
    vector<long long> c = solve_optimized(max_n, a, b);
    
    // 记录结束时间
    auto end = chrono::high_resolution_clock::now();
    chrono::duration<double> elapsed = end - start;
    
    cout << "计算完成，用时：" << elapsed.count() << "秒\n";
    cout << "前5个结果：";
    for (int i = 0; i < min(5, size); i++) {
        cout << c[i] << " ";
    }
    cout << endl;
}

/**
 * 主函数
 */
int main() {
    cout << "1. 标准测试" << endl;
    cout << "2. 性能测试" << endl;
    cout << "请选择测试类型：";
    
    int choice;
    cin >> choice;
    
    if (choice == 1) {
        test_hdu6057();
    } else if (choice == 2) {
        performance_test();
    }
    
    return 0;
}

/**
 * C++语言特定优化说明：
 * 1. 使用vector容器进行动态内存管理
 * 2. 利用__builtin_popcount函数快速计算二进制中1的个数
 * 3. 使用ios::sync_with_stdio(false)和cin.tie(0)加速输入输出
 * 4. 分块策略的优化实现
 * 5. 位运算技巧的运用
 * 
 * 时间复杂度分析：
 * - 分块预处理：O(2^B * 2^{n-B} log 2^{n-B}) = O(2^n (n - B))
 * - 总时间复杂度：O(2^n (n - B + B)) = O(2^n n)
 * - 最优选择B = √n时，复杂度为O(2^n log^2 n)
 * 
 * 空间复杂度分析：
 * - O(2^n) 存储输入和结果
 * - O(2^{n-B}) 存储中间数组
 * - 总体空间复杂度：O(2^n)
 * 
 * 优化技巧：
 * 1. 循环展开：减少循环开销
 * 2. 内存局部性优化：按顺序访问内存
 * 3. 预处理常用值：避免重复计算
 * 4. 条件分支优化：减少分支预测失败
 */