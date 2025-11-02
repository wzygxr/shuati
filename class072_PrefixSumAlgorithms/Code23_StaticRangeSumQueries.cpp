// Static Range Sum Queries (CSES 1646)
// 
// 题目描述:
// 给定一个数组，处理多个查询：计算区间[a,b]内元素的和。
// 
// 示例:
// 输入:
// 8 4
// 3 2 4 5 1 1 5 3
// 2 4
// 5 6
// 1 8
// 3 3
// 输出:
// 11
// 2
// 24
// 4
// 
// 提示:
// 1 <= n, q <= 2 * 10^5
// -10^9 <= x <= 10^9
// 
// 题目链接: https://cses.fi/problemset/task/1646
// 
// 解题思路:
// 使用基础前缀和技巧，预处理前缀和数组，然后O(1)时间查询。
// 
// 时间复杂度: 
// - 初始化: O(n) - 需要遍历整个数组构建前缀和数组
// - 查询: O(1) - 每次查询只需要常数时间
// 空间复杂度: O(n) - 需要额外的前缀和数组空间
// 
// 工程化考量:
// 1. 边界条件处理：空数组、单元素数组
// 2. 性能优化：预处理前缀和，查询时O(1)时间
// 3. 空间优化：必须存储前缀和数组，无法避免
// 4. 大数处理：元素值可能很大，需要确保整数范围
// 
// 最优解分析:
// 这是最优解，因为查询次数可能很多，预处理后可以实现O(1)查询时间。
// 对于静态数组的区间和查询，前缀和是最佳选择。
// 
// 算法核心:
// 前缀和公式：
// prefixSum[i] = prefixSum[i-1] + arr[i-1]
// 区间和公式：
// sumRange(a, b) = prefixSum[b] - prefixSum[a-1]
// 
// 算法调试技巧:
// 1. 打印中间过程：显示前缀和数组的计算过程
// 2. 边界测试：测试空数组、单元素数组等特殊情况
// 3. 性能测试：测试大规模数组下的性能表现
// 
// 语言特性差异:
// C++中数组需要手动管理内存。
// 与Java相比，C++需要手动释放数组内存。
// 与Python相比，C++是静态类型语言，需要显式声明数组类型。

#include <iostream>
#include <vector>
using namespace std;

class PrefixSumArray {
private:
    vector<long long> prefixSum;  // 前缀和数组
    
public:
    // 构造函数，初始化前缀和数组
    PrefixSumArray(const vector<int>& arr) {
        int n = arr.size();
        // 创建前缀和数组，大小为n+1
        // 使用n+1可以避免边界检查
        prefixSum.resize(n + 1, 0);
        
        // 计算前缀和，时间复杂度O(n)
        for (int i = 1; i <= n; i++) {
            // 前缀和公式：当前前缀和 = 前一个前缀和 + 当前元素
            prefixSum[i] = prefixSum[i - 1] + arr[i - 1];
            // 调试打印：显示前缀和计算过程
            // cout << "位置 " << i << ": prefixSum[" << i << "] = " << prefixSum[i] << endl;
        }
    }
    
    // 计算子数组区间和
    long long sumRange(int left, int right) {
        // 使用前缀和公式计算区间和，时间复杂度O(1)
        // 公式：区间和 = 右边界前缀和 - 左边界前缀和
        long long result = prefixSum[right] - prefixSum[left - 1];
        
        // 调试打印：显示查询过程
        // cout << "查询区间 [" << left << ", " << right << "]: 结果 = " << result << endl;
        
        return result;
    }
};

int main() {
    ios::sync_with_stdio(false);
    cin.tie(0);
    
    int n, q;
    cin >> n >> q;
    
    vector<int> arr(n);
    for (int i = 0; i < n; i++) {
        cin >> arr[i];
    }
    
    PrefixSumArray prefixSumArray(arr);
    
    for (int i = 0; i < q; i++) {
        int a, b;
        cin >> a >> b;
        long long result = prefixSumArray.sumRange(a, b);
        cout << result << "\n";
    }
    
    return 0;
}