#include <iostream>
#include <vector>
using namespace std;

// 比特位计数
// 测试链接 : https://leetcode.cn/problems/counting-bits/
/*
题目描述：
给你一个整数 n ，对于 0 <= i <= n 中的每个 i ，计算其二进制表示中 1 的个数 ，返回一个长度为 n + 1 的数组 ans 作为答案。

示例：
输入：n = 2
输出：[0,1,1]
解释：
0 --> 0
1 --> 1
2 --> 10

输入：n = 5
输出：[0,1,1,2,1,2]
解释：
0 --> 0
1 --> 1
2 --> 10
3 --> 11
4 --> 100
5 --> 101

提示：
0 <= n <= 10^5

解题思路：
这是一个典型的动态规划问题，可以利用位运算的特性高效解决。

方法1：动态规划 + 最低有效位
观察二进制数的规律：
- 对于偶数 i，其二进制中 1 的个数等于 i/2 中 1 的个数
- 对于奇数 i，其二进制中 1 的个数等于 i/2 中 1 的个数加 1

可以统一表示为：dp[i] = dp[i >> 1] + (i & 1)

方法2：动态规划 + 最高有效位
对于数字 i，如果我们找到小于等于 i 的最大的 2 的幂 j，那么 dp[i] = dp[i - j] + 1

方法3：动态规划 + Brian Kernighan 算法
利用 n & (n - 1) 可以移除最右边的 1，因此 dp[i] = dp[i & (i - 1)] + 1

时间复杂度：O(n) - 我们只需要遍历一次 0 到 n
空间复杂度：O(n) - 需要一个长度为 n + 1 的数组来存储结果
*/

class Solution {
public:
    /**
     * 计算0到n之间每个数字的二进制表示中1的个数
     * 使用动态规划 + 最低有效位方法
     * @param n 输入整数
     * @return 包含每个数字二进制中1的个数的数组
     */
    vector<int> countBits(int n) {
        vector<int> dp(n + 1, 0);
        
        // 方法1：动态规划 + 最低有效位
        for (int i = 1; i <= n; i++) {
            // 对于数字i，右移一位得到i/2，然后加上i的最低位
            dp[i] = dp[i >> 1] + (i & 1);
        }
        
        return dp;
    }
    
    /**
     * 计算0到n之间每个数字的二进制表示中1的个数
     * 使用动态规划 + 最高有效位方法
     * @param n 输入整数
     * @return 包含每个数字二进制中1的个数的数组
     */
    vector<int> countBits2(int n) {
        vector<int> dp(n + 1, 0);
        int highestBit = 0; // 记录当前的最高有效位
        
        for (int i = 1; i <= n; i++) {
            // 如果i是2的幂，更新highestBit
            if ((i & (i - 1)) == 0) {
                highestBit = i;
            }
            // 利用最高有效位计算当前数字的1的个数
            dp[i] = dp[i - highestBit] + 1;
        }
        
        return dp;
    }
    
    /**
     * 计算0到n之间每个数字的二进制表示中1的个数
     * 使用动态规划 + Brian Kernighan算法
     * @param n 输入整数
     * @return 包含每个数字二进制中1的个数的数组
     */
    vector<int> countBits3(int n) {
        vector<int> dp(n + 1, 0);
        
        for (int i = 1; i <= n; i++) {
            // 利用n & (n - 1)移除最右边的1，然后加1
            dp[i] = dp[i & (i - 1)] + 1;
        }
        
        return dp;
    }
};

// 辅助函数：打印数组
void printArray(const vector<int>& arr) {
    cout << "[";
    for (size_t i = 0; i < arr.size(); i++) {
        cout << arr[i];
        if (i < arr.size() - 1) {
            cout << ", ";
        }
    }
    cout << "]" << endl;
}

// 测试代码
int main() {
    Solution solution;
    
    // 测试方法1
    vector<int> result1 = solution.countBits(5);
    cout << "Test 1: ";
    printArray(result1); // 输出: [0, 1, 1, 2, 1, 2]
    
    // 测试方法2
    vector<int> result2 = solution.countBits2(5);
    cout << "Test 2: ";
    printArray(result2); // 输出: [0, 1, 1, 2, 1, 2]
    
    // 测试方法3
    vector<int> result3 = solution.countBits3(5);
    cout << "Test 3: ";
    printArray(result3); // 输出: [0, 1, 1, 2, 1, 2]
    
    // 测试大数字
    int n = 100;
    vector<int> largeResult = solution.countBits(n);
    cout << "\nTest with n = " << n << endl;
    cout << "The count of 1's in " << n << " is: " << largeResult[n] << endl;
    
    return 0;
}