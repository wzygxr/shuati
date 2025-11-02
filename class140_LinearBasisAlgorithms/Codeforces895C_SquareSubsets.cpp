#include <iostream>
#include <vector>
#include <algorithm>
using namespace std;

/**
 * Codeforces 895C. Square Subsets
 * 题目链接：https://codeforces.com/contest/895/problem/C
 * 
 * 解题思路：
 * 这道题可以利用线性基和质因数分解来解决。
 * 1. 对于每个数，我们可以将其质因数分解，保留次数为奇数的质因数
 * 2. 这样，每个数可以表示为一个二进制向量，向量的每一位代表一个质数是否出现奇数次
 * 3. 问题转化为：在数组中选择一个非空子集，使得子集中所有数的向量异或结果为零向量
 * 4. 使用动态规划结合线性基来计算方案数
 * 
 * 时间复杂度：O(n * m * log m)，其中n是数组长度，m是质数的个数
 * 空间复杂度：O(2^m)，其中m是质数的个数
 */
class Solution {
private:
    const int MOD = 1000000007;
    const vector<int> PRIMES = {2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97};
    const int MAX_NUM = 70;
    const int PRIME_COUNT = PRIMES.size();
    
    /**
     * 将数字转换为向量表示
     * @param num 输入数字
     * @return 向量表示（二进制掩码）
     */
    int getMask(int num) {
        int mask = 0;
        for (int i = 0; i < PRIME_COUNT; i++) {
            int prime = PRIMES[i];
            int cnt = 0;
            while (num % prime == 0) {
                cnt++;
                num /= prime;
            }
            if (cnt % 2 == 1) {
                mask |= (1 << i);
            }
        }
        return mask;
    }
    
    /**
     * 快速幂计算
     * @param base 底数
     * @param exp 指数
     * @return base^exp mod MOD
     */
    long long powMod(long long base, int exp) {
        long long result = 1;
        while (exp > 0) {
            if (exp % 2 == 1) {
                result = (result * base) % MOD;
            }
            base = (base * base) % MOD;
            exp /= 2;
        }
        return result;
    }
    
public:
    /**
     * 计算满足条件的子集数目
     * @param a 输入数组
     * @return 满足条件的子集数目
     */
    int squareSubsets(vector<int>& a) {
        // 统计每个数的出现次数
        vector<int> count(MAX_NUM + 1, 0);
        for (int num : a) {
            count[num]++;
        }
        
        // 初始化dp数组，dp[mask]表示异或结果为mask的子集数目
        vector<long long> dp(1 << PRIME_COUNT, 0);
        dp[0] = 1; // 空子集
        
        // 处理每个数
        for (int num = 2; num <= MAX_NUM; num++) {
            if (count[num] == 0) {
                continue;
            }
            
            // 将数转换为向量：每个质数是否出现奇数次
            int mask = getMask(num);
            if (mask == 0) {
                // 这个数本身是平方数，可以选择任意次数，但至少选一次
                long long pow_val = powMod(2, count[num]);
                // 对于所有现有子集，可以选择添加任意非空的平方数集合
                for (int i = 0; i < (1 << PRIME_COUNT); i++) {
                    dp[i] = (dp[i] * pow_val) % MOD;
                }
            } else {
                // 非平方数，需要用线性基来处理
                // 创建一个临时数组，避免在更新过程中覆盖值
                vector<long long> temp = dp;
                long long pow2 = powMod(2, count[num] - 1); // 选偶数个该数的方式数
                
                // 对于每个现有的mask状态
                for (int i = 0; i < (1 << PRIME_COUNT); i++) {
                    // 选择奇数个该数
                    temp[i ^ mask] = (temp[i ^ mask] + dp[i] * pow2) % MOD;
                }
                
                dp = temp;
            }
        }
        
        // 减去空子集的情况
        return (int) ((dp[0] - 1 + MOD) % MOD);
    }
};

// 测试代码
int main() {
    Solution solution;
    
    // 测试用例1
    vector<int> a1 = {1, 1, 1};
    cout << "测试用例1结果: " << solution.squareSubsets(a1) << endl; // 预期输出: 7
    
    // 测试用例2
    vector<int> a2 = {2, 2, 2};
    cout << "测试用例2结果: " << solution.squareSubsets(a2) << endl; // 预期输出: 3
    
    // 测试用例3
    vector<int> a3 = {1, 2, 4, 8, 16, 32, 64, 128, 256, 512};
    cout << "测试用例3结果: " << solution.squareSubsets(a3) << endl; // 预期输出: 1023
    
    return 0;
}