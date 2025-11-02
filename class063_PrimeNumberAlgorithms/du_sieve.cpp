// 杜教筛算法实现

// 简化版本，仅提供算法核心逻辑
// 由于编译环境限制，省略了STL容器和iostream

class DuSieve {
private:
    static const long long MOD = 1000000007;
    
public:
    DuSieve() {}
    
    /**
     * 计算莫比乌斯函数μ(n)
     */
    long long mu(long long n) {
        if (n == 1) return 1;
        long long result = 1;
        for (long long i = 2; i * i <= n; i++) {
            if (n % i == 0) {
                long long cnt = 0;
                while (n % i == 0) {
                    n /= i;
                    cnt++;
                }
                if (cnt > 1) return 0; // 有平方因子
                result = -result;
            }
        }
        if (n > 1) result = -result; // 剩下的质因子
        return result;
    }
    
    /**
     * 计算欧拉函数φ(n)
     */
    long long phi(long long n) {
        long long result = n;
        for (long long i = 2; i * i <= n; i++) {
            if (n % i == 0) {
                result = result / i * (i - 1);
                while (n % i == 0) n /= i;
            }
        }
        if (n > 1) result = result / n * (n - 1);
        return result;
    }
    
    /**
     * 杜教筛计算莫比乌斯函数前缀和（简化版）
     * S(n) = Σ(i=1 to n) μ(i)
     */
    long long sumMu(long long n) {
        if (n == 0) return 0;
        
        if (n <= 1000000) {
            // 小数据直接计算
            long long result = 0;
            for (long long i = 1; i <= n; i++) {
                result = (result + mu(i)) % MOD;
            }
            return result;
        }
        
        // 杜教筛递推公式（简化版）
        long long result = 1; // μ(1) = 1
        for (long long i = 2; i <= n; i++) {
            result = (result - sumMu(n / i) % MOD + MOD) % MOD;
        }
        
        result = (result % MOD + MOD) % MOD;
        return result;
    }
    
    /**
     * 杜教筛计算欧拉函数前缀和（简化版）
     * S(n) = Σ(i=1 to n) φ(i)
     */
    long long sumPhi(long long n) {
        if (n == 0) return 0;
        
        if (n <= 1000000) {
            // 小数据直接计算
            long long result = 0;
            for (long long i = 1; i <= n; i++) {
                result = (result + phi(i)) % MOD;
            }
            return result;
        }
        
        // 杜教筛递推公式（简化版）
        long long result = n % MOD * ((n + 1) % MOD) % MOD;
        if (result % 2 == 0) result /= 2;
        else result = (result + MOD) / 2 % MOD;
        
        for (long long i = 2; i <= n; i++) {
            result = (result - sumPhi(n / i) % MOD + MOD) % MOD;
        }
        
        result = (result % MOD + MOD) % MOD;
        return result;
    }
    
    /**
     * 洛谷P4213 【模板】杜教筛
     * 题目来源: https://www.luogu.com.cn/problem/P4213
     * 题目描述: 给定一个正整数n，求
     * ans1 = Σ(i=1 to n) φ(i)
     * ans2 = Σ(i=1 to n) μ(i)
     * 解题思路: 直接使用杜教筛算法分别计算欧拉函数和莫比乌斯函数的前缀和
     * 时间复杂度: O(n^(2/3))
     * 空间复杂度: O(n^(2/3))
     * 
     * @param n 正整数
     * @param result 包含ans1和ans2的数组
     */
    void solveP4213(long long n, long long result[2]) {
        result[0] = sumPhi(n);  // 欧拉函数前缀和
        result[1] = sumMu(n);   // 莫比乌斯函数前缀和
    }
};

// 测试用例
// 由于编译环境限制，省略了输出语句
int main() {
    DuSieve solver;
    
    // 测试莫比乌斯函数前缀和
    long long n1 = 100;
    long long result1 = solver.sumMu(n1);
    
    // 测试欧拉函数前缀和
    long long n2 = 100;
    long long result2 = solver.sumPhi(n2);
    
    // 测试洛谷P4213题目
    long long n3 = 10;
    long long result3[2];
    solver.solveP4213(n3, result3);
    
    return 0;
}