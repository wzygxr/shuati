// Ann and Books问题 - 前缀和+哈希表实现 (C++版本)
// 题目来源: https://codeforces.com/problemset/problem/877/E
// 题目大意: 给定一个长度为n的数组arr，有q次查询，每次查询[l,r]区间内和等于k的子数组个数
// 约束条件: 1 <= n, q <= 10^5, |arr[i]| <= 10^9

#include <iostream>
#include <unordered_map>
#include <vector>
using namespace std;

const int MAXN = 100001;
int n, q, k;
int arr[MAXN];
long long prefixSum[MAXN]; // 前缀和数组

/**
 * 查询[l,r]区间内和等于k的子数组个数
 * 时间复杂度: O(r-l+1)
 * 设计思路: 使用前缀和和哈希表统计满足条件的子数组个数
 * 对于子数组[i,j]，其和为prefixSum[j] - prefixSum[i-1]
 * 要使子数组和等于k，即prefixSum[j] - prefixSum[i-1] = k
 * 变形得prefixSum[i-1] = prefixSum[j] - k
 * 因此我们可以在遍历过程中统计每个前缀和出现的次数，然后查找prefixSum[j] - k是否出现过
 * @param l 查询区间左边界
 * @param r 查询区间右边界
 * @return 区间内和等于k的子数组个数
 */
int query(int l, int r) {
    // 使用哈希表统计前缀和出现次数
    unordered_map<long long, int> count;
    // 前缀和为0出现1次（表示空前缀）
    count[0] = 1;
    
    int result = 0;
    
    // 遍历查询区间内的每个位置
    for (int i = l; i <= r; i++) {
        // 计算从位置l-1到位置i的前缀和
        long long currentSum = prefixSum[i] - prefixSum[l - 1];
        
        // 查找是否存在前缀和使得currentSum - prevSum = k
        // 即prevSum = currentSum - k
        result += count[currentSum - k];
        
        // 更新当前前缀和的计数
        count[currentSum]++;
    }
    
    return result;
}

int main() {
    // 优化输入输出速度
    ios::sync_with_stdio(false);
    cin.tie(nullptr);
    
    // 读取数组长度n和目标和k
    cin >> n >> k;
    
    // 读取初始数组并计算前缀和
    for (int i = 1; i <= n; i++) {
        cin >> arr[i];
        // 计算前缀和
        prefixSum[i] = prefixSum[i - 1] + arr[i];
    }

    // 读取查询次数q
    cin >> q;
    
    // 处理q次查询
    for (int i = 1; i <= q; i++) {
        int l, r;
        cin >> l >> r;
        // 输出查询结果
        cout << query(l, r) << "\n";
    }
    
    return 0;
}

// 算法说明：
// 1. 使用前缀和+哈希表解决子数组和问题
// 2. 时间复杂度：O(q*n)
// 3. 空间复杂度：O(n)
// 4. 核心思想：对于每个查询，使用哈希表统计前缀和出现次数，
//    通过查找currentSum - k是否存在来统计满足条件的子数组个数