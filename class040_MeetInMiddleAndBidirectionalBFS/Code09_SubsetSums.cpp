// Subset Sums (SPOJ SUBSUMS)
// 题目来源：SPOJ
// 题目描述：
// 给定一个数组和两个整数a和b，找出有多少个子集的和在[a, b]区间内。
// 注意：空集的和为0，空集也应该被考虑。
// 测试链接：https://www.spoj.com/problems/SUBSUMS/
// 
// 算法思路：
// 使用折半搜索（Meet in the Middle）算法，将数组分为两半分别计算所有可能的和，
// 然后对其中一半进行排序，通过二分查找找到符合条件的组合数目
// 时间复杂度：O(2^(n/2) * log(2^(n/2))) = O(2^(n/2) * n)
// 空间复杂度：O(2^(n/2))
// 
// 工程化考量：
// 1. 异常处理：检查输入是否合法
// 2. 性能优化：使用折半搜索减少搜索空间
// 3. 可读性：变量命名清晰，注释详细
// 
// 语言特性差异：
// C++中使用vector存储子集和，使用sort函数进行排序，使用lower_bound和upper_bound函数进行二分查找

#include <iostream>
#include <vector>
#include <algorithm>
using namespace std;

/**
 * 递归生成指定范围内所有可能的子集和
 * @param arr 输入数组
 * @param start 起始索引
 * @param end 结束索引
 * @param currentSum 当前累积和
 * @param sums 存储结果的向量
 */
void generateSubsetSums(const vector<int>& arr, int start, int end, int currentSum, vector<int>& sums) {
    // 递归终止条件
    if (start > end) {
        sums.push_back(currentSum);
        return;
    }
    
    // 不选择当前元素
    generateSubsetSums(arr, start + 1, end, currentSum, sums);
    
    // 选择当前元素
    generateSubsetSums(arr, start + 1, end, currentSum + arr[start], sums);
}

/**
 * 计算数组中和在[a, b]区间内的子集数目
 * @param arr 输入数组
 * @param a 区间左边界
 * @param b 区间右边界
 * @return 符合条件的子集数目
 */
int countSubsets(const vector<int>& arr, int a, int b) {
    // 边界条件检查
    if (arr.empty()) {
        // 空数组只有空集一种可能，检查0是否在[a, b]区间内
        return (a <= 0 && 0 <= b) ? 1 : 0;
    }
    
    int n = arr.size();
    int mid = n / 2;
    
    // 分别存储左右两部分的所有可能子集和
    vector<int> leftSums;
    vector<int> rightSums;
    
    // 计算左半部分的所有可能子集和
    generateSubsetSums(arr, 0, mid - 1, 0, leftSums);
    
    // 计算右半部分的所有可能子集和
    generateSubsetSums(arr, mid, n - 1, 0, rightSums);
    
    // 对右半部分的子集和进行排序，以便进行二分查找
    sort(rightSums.begin(), rightSums.end());
    
    // 统计符合条件的组合数目
    int count = 0;
    for (int leftSum : leftSums) {
        // 查找右半部分中满足 a - leftSum <= rightSum <= b - leftSum 的数目
        int lowerBound = a - leftSum;
        int upperBound = b - leftSum;
        
        // 查找第一个大于等于lowerBound的位置
        auto startIt = lower_bound(rightSums.begin(), rightSums.end(), lowerBound);
        
        // 查找第一个大于upperBound的位置
        auto endIt = upper_bound(rightSums.begin(), rightSums.end(), upperBound);
        
        // 累加符合条件的数目
        count += (endIt - startIt);
    }
    
    return count;
}

// 测试方法
int main() {
    // 读取输入
    cout << "请输入数组长度n，以及区间[a, b]：" << endl;
    int n, a, b;
    cin >> n >> a >> b;
    
    vector<int> arr(n);
    cout << "请输入数组元素：" << endl;
    for (int i = 0; i < n; i++) {
        cin >> arr[i];
    }
    
    // 计算结果
    int result = countSubsets(arr, a, b);
    cout << "满足条件的子集数目：" << result << endl;
    
    // 测试用例1
    cout << "\n测试用例1：" << endl;
    vector<int> arr1 = {1, -2, 3};
    int a1 = -1;
    int b1 = 2;
    cout << "数组：[1, -2, 3]" << endl;
    cout << "区间：[-1, 2]" << endl;
    cout << "期望输出：3" << endl; // 空集(0), {1}, {-2, 3}
    cout << "实际输出：" << countSubsets(arr1, a1, b1) << endl;
    
    // 测试用例2
    cout << "\n测试用例2：" << endl;
    vector<int> arr2 = {1, 2, 3, 4};
    int a2 = 4;
    int b2 = 7;
    cout << "数组：[1, 2, 3, 4]" << endl;
    cout << "区间：[4, 7]" << endl;
    cout << "期望输出：6" << endl; // {4}, {1,3}, {2,3}, {1,2,3}, {1,4}, {2,4}
    cout << "实际输出：" << countSubsets(arr2, a2, b2) << endl;
    
    return 0;
}