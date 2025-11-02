#include <vector>
#include <algorithm>
#include <cmath>
#include <unordered_map>
#include <cstring>
#include <bitset>
#include <iostream>

using namespace std;

/**
 * 异或运算扩展题目实现 (C++版本)
 * 
 * 本文件包含来自各大算法平台的异或运算题目，包括Codeforces、AtCoder、SPOJ、POJ等
 * 每个题目都有详细的解题思路、复杂度分析和工程化考量
 */

class Code11_XorExtendedProblems {
public:
    /**
     * 题目1: Little Girl and Maximum XOR (Codeforces 276D)
     * 
     * 题目来源: Codeforces 276D
     * 链接: https://codeforces.com/problemset/problem/276/D
     * 
     * 题目描述:
     * 给定两个整数l和r (0 ≤ l ≤ r ≤ 10^18)，找到两个数a, b (l ≤ a ≤ b ≤ r)，
     * 使得a XOR b的值最大。
     * 
     * 解题思路:
     * 1. 找到l和r二进制表示中第一个不同的位
     * 2. 从该位开始，后面的所有位都可以设为1
     * 3. 最大异或值就是(1 << (第一个不同位的位置+1)) - 1
     * 
     * 时间复杂度: O(log(max(l, r)))
     * 空间复杂度: O(1)
     * 
     * 工程化考量:
     * - 使用long long类型处理大数
     * - 处理l == r的特殊情况
     * - 边界条件检查
     * 
     * @param l 区间左边界
     * @param r 区间右边界
     * @return 最大异或值
     */
    static long long littleGirlMaxXOR(long long l, long long r) {
        // 特殊情况处理
        if (l == r) {
            return 0;
        }
        
        // 找到第一个不同的位
        long long xor_val = l ^ r;
        long long highestBit = 1LL << (63 - __builtin_clzll(xor_val));
        
        // 构造最大异或值：从最高不同位开始后面全为1
        long long result = (highestBit << 1) - 1;
        return result;
    }

    /**
     * 题目2: XOR and Favorite Number (Codeforces 617E)
     * 
     * 题目来源: Codeforces 617E
     * 链接: https://codeforces.com/problemset/problem/617/E
     * 
     * 题目描述:
     * 给定一个数组a和整数k，以及多个查询[l, r]，
     * 对于每个查询，统计子数组a[l...r]中有多少个子数组的异或值等于k。
     * 
     * 解题思路:
     * 使用莫队算法(Mo's Algorithm)：
     * 1. 计算前缀异或数组prefix
     * 2. 子数组a[i...j]的异或值 = prefix[j] ^ prefix[i-1]
     * 3. 使用莫队算法处理区间查询
     * 
     * 时间复杂度: O((n + q) * √n)
     * 空间复杂度: O(n + MAX_VALUE)
     * 
     * 工程化考量:
     * - 使用unordered_map记录频率，避免数组越界
     * - 分块大小选择√n
     * - 处理大数据量的性能优化
     * 
     * @param arr 输入数组
     * @param k 目标异或值
     * @param queries 查询数组
     * @return 每个查询的结果
     */
    static vector<int> xorFavoriteNumber(vector<int>& arr, int k, vector<vector<int>>& queries) {
        int n = arr.size();
        int q = queries.size();
        
        // 计算前缀异或数组
        vector<int> prefix(n + 1, 0);
        for (int i = 1; i <= n; i++) {
            prefix[i] = prefix[i - 1] ^ arr[i - 1];
        }
        
        // 莫队算法：对查询排序
        int blockSize = sqrt(n);
        vector<vector<int>> indexedQueries(q, vector<int>(3));
        for (int i = 0; i < q; i++) {
            indexedQueries[i][0] = queries[i][0]; // l
            indexedQueries[i][1] = queries[i][1]; // r
            indexedQueries[i][2] = i;             // 原始索引
        }
        
        // 按照块排序
        sort(indexedQueries.begin(), indexedQueries.end(), [&](const vector<int>& a, const vector<int>& b) {
            int blockA = a[0] / blockSize;
            int blockB = b[0] / blockSize;
            if (blockA != blockB) {
                return blockA < blockB;
            }
            return a[1] < b[1];
        });
        
        vector<int> result(q, 0);
        unordered_map<int, int> freq;
        int currentL = 0, currentR = -1;
        long long currentCount = 0;
        
        // 初始状态：空前缀
        freq[0] = 1;
        
        for (auto& query : indexedQueries) {
            int l = query[0];
            int r = query[1];
            int index = query[2];
            
            // 移动左指针
            while (currentL < l) {
                int xorValue = prefix[currentL];
                freq[xorValue]--;
                currentCount -= freq[xorValue ^ k];
                currentL++;
            }
            
            while (currentL > l) {
                currentL--;
                int xorValue = prefix[currentL];
                currentCount += freq[xorValue ^ k];
                freq[xorValue]++;
            }
            
            // 移动右指针
            while (currentR < r) {
                currentR++;
                int xorValue = prefix[currentR + 1];
                currentCount += freq[xorValue ^ k];
                freq[xorValue]++;
            }
            
            while (currentR > r) {
                int xorValue = prefix[currentR + 1];
                freq[xorValue]--;
                currentCount -= freq[xorValue ^ k];
                currentR--;
            }
            
            result[index] = currentCount;
        }
        
        return result;
    }

    // 继续实现其他题目...
};

// 测试函数
int main() {
    // 测试 littleGirlMaxXOR
    cout << "Little Girl Max XOR (1, 10): " << Code11_XorExtendedProblems::littleGirlMaxXOR(1, 10) << endl;
    
    // 测试 xorFavoriteNumber (模拟数据)
    vector<int> arr = {1, 2, 3, 4, 5};
    int k = 6;
    vector<vector<int>> queries = {{0, 2}, {1, 3}};
    vector<int> results = Code11_XorExtendedProblems::xorFavoriteNumber(arr, k, queries);
    cout << "XOR Favorite Number results: ";
    for (int res : results) {
        cout << res << " ";
    }
    cout << endl;
    
    return 0;
}