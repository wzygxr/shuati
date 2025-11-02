#include <iostream>
#include <string>
#include <vector>
#include <algorithm>
using namespace std;

/**
 * Manacher算法计算奇回文串
 * 
 * @param s 输入字符串
 * @return 每个位置为中心的最长奇回文半径数组
 */
vector<int> manacherOdd(string s) {
    int n = s.length();
    vector<int> radius(n, 0);
    
    int l = 0, r = -1;
    for (int i = 0; i < n; i++) {
        // 利用回文对称性
        int k = (i > r) ? 1 : min(radius[l + r - i], r - i + 1);
        
        // 尝试扩展回文串
        while (0 <= i - k && i + k < n && s[i - k] == s[i + k]) {
            k++;
        }
        
        radius[i] = k - 1;
        
        // 更新最右回文边界
        if (i + radius[i] > r) {
            l = i - radius[i];
            r = i + radius[i];
        }
    }
    
    return radius;
}

/**
 * LeetCode 1960. 两个回文子字符串长度的最大乘积
 * 
 * 题目描述：
 * 给你一个下标从0开始的字符串 s ，你需要找到两个不重叠的回文子字符串，
 * 它们的长度都必须为奇数，使得它们长度的乘积最大。
 * 
 * 解题思路：
 * 使用Manacher算法计算所有奇回文信息：
 * 1. 使用Manacher算法计算每个位置为中心的最长奇回文半径
 * 2. 预处理前缀和后缀数组，分别记录到每个位置为止的最长回文长度
 * 3. 枚举每个分割点，通过前后缀获取左右两个子串中的最长回文大小，相乘即可
 * 
 * 时间复杂度：O(n)
 * 空间复杂度：O(n)
 * 
 * @param s 输入字符串
 * @return 最大乘积
 */
long long maxProduct(string s) {
    int n = s.length();
    
    // 使用Manacher算法计算每个位置为中心的最长奇回文半径
    vector<int> radius = manacherOdd(s);
    
    // prefix[i] 表示在 [0, i] 范围内能找到的最长奇回文子串长度
    vector<long long> prefix(n, 0);
    // suffix[i] 表示在 [i, n-1] 范围内能找到的最长奇回文子串长度
    vector<long long> suffix(n, 0);
    
    // 初始化
    prefix[0] = 1;
    suffix[n - 1] = 1;
    
    // 计算前缀数组
    for (int i = 1; i < n; i++) {
        // 检查以位置i结尾的回文串
        for (int j = 0; j <= i; j++) {
            // 回文串的右边界是i，中心是j，半径是radius[j]
            if (j + radius[j] >= i) {
                prefix[i] = max(prefix[i], (long long)(2 * (i - j) + 1));
            }
        }
        prefix[i] = max(prefix[i], prefix[i - 1]);
    }
    
    // 计算后缀数组
    for (int i = n - 2; i >= 0; i--) {
        // 检查以位置i开头的回文串
        for (int j = i; j < n; j++) {
            // 回文串的左边界是i，中心是j，半径是radius[j]
            if (j - radius[j] <= i) {
                suffix[i] = max(suffix[i], (long long)(2 * (j - i) + 1));
            }
        }
        suffix[i] = max(suffix[i], suffix[i + 1]);
    }
    
    // 枚举分割点，计算最大乘积
    long long maxProduct = 0;
    for (int i = 0; i < n - 1; i++) {
        maxProduct = max(maxProduct, prefix[i] * suffix[i + 1]);
    }
    
    return maxProduct;
}

// 测试方法
int main() {
    // 示例测试
    cout << maxProduct("ababbb") << endl;  // 输出: 9
    cout << maxProduct("zaaaxbbby") << endl;  // 输出: 9
    
    return 0;
}