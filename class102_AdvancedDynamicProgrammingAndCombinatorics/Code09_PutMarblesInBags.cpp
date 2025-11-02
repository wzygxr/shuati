// 将珠子放进背包中
// 你有 k 个背包。给你一个下标从 0 开始的整数数组 weights ，其中 weights[i] 是第 i 个珠子的重量。
// 同时给你整数 k 。请你按照如下规则将所有的珠子放进 k 个背包：
// 1. 没有背包是空的。
// 2. 如果第 i 个珠子和第 j 个珠子在同一个背包，那么下标在 i 和 j 之间的所有珠子都必须在这同一个背包中。
// 3. 如果一个背包有珠子 i1, i2, ..., im，则该背包的开销是 weights[i1] + weights[im]。
// 4. 整个分配方案的开销是所有 k 个背包的开销之和。
// 请你返回所有可能的分配方案中，最大开销与最小开销的差值。

// 算法思路：
// 这是一个贪心算法问题。
// 关键观察：要将数组分割成 k 个连续子数组，我们需要在数组中选择 k-1 个分割点。
// 每个分割点 i 的贡献是 weights[i] + weights[i+1]（左边子数组的末尾和右边子数组的开头）。
// 因此，我们可以计算所有可能分割点的贡献值，然后选择最大的 k-1 个和最小的 k-1 个，
// 它们的差值就是答案。
// 
// 注意：数组的第一个元素和最后一个元素在任何分配方案中都会被计算一次，
// 所以它们的贡献在最大开销和最小开销中是相同的，可以忽略。
// 
// 时间复杂度：O(n * log(n))
// 空间复杂度：O(n)
// 
// 测试链接 : https://leetcode.cn/problems/put-marbles-in-bags/

#include <stdio.h>
#include <stdlib.h>

/**
 * 简单排序函数（冒泡排序）
 * 
 * @param arr 待排序数组
 * @param n 数组长度
 */
void sort(long long* arr, int n) {
    for (int i = 0; i < n - 1; i++) {
        for (int j = 0; j < n - i - 1; j++) {
            if (arr[j] > arr[j + 1]) {
                long long temp = arr[j];
                arr[j] = arr[j + 1];
                arr[j + 1] = temp;
            }
        }
    }
}

/**
 * 计算将珠子放进背包中的最大开销与最小开销的差值
 * 
 * @param weights 珠子的重量数组
 * @param weightsSize 数组长度
 * @param k 背包数量
 * @return 最大开销与最小开销的差值
 * 
 * 时间复杂度：O(n * log(n))
 * 空间复杂度：O(n)
 */
long long putMarbles(int* weights, int weightsSize, int k) {
    int n = weightsSize;
    
    // 特殊情况：如果只有一个背包或者每个珠子一个背包，差值为0
    if (k == 1 || k == n) {
        return 0;
    }
    
    // 计算所有可能分割点的贡献值
    // 分割点 i 的贡献是 weights[i] + weights[i+1]
    long long* contributions = new long long[n - 1];
    for (int i = 0; i < n - 1; i++) {
        contributions[i] = (long long) weights[i] + weights[i + 1];
    }
    
    // 排序贡献值
    sort(contributions, n - 1);
    
    // 计算最大开销和最小开销的差值
    // 选择最大的 k-1 个贡献值减去最小的 k-1 个贡献值
    long long maxSum = 0, minSum = 0;
    for (int i = 0; i < k - 1; i++) {
        minSum += contributions[i];                    // 最小的 k-1 个
        maxSum += contributions[n - 2 - i];            // 最大的 k-1 个
    }
    
    delete[] contributions;  // 释放内存
    return maxSum - minSum;
}

// 添加main函数用于测试
int main() {
    // 测试用例1
    int weights1[] = {1, 3, 5, 1};
    int k1 = 2;
    long long result1 = putMarbles(weights1, 4, k1);
    
    // 测试用例2
    int weights2[] = {1, 3};
    int k2 = 2;
    long long result2 = putMarbles(weights2, 2, k2);
    
    // 测试用例3
    int weights3[] = {1, 4, 2, 5, 2};
    int k3 = 3;
    long long result3 = putMarbles(weights3, 5, k3);
    
    return 0;
}