# Static Range Sum Queries (CSES 1646)
# 
# 题目描述:
# 给定一个数组，处理多个查询：计算区间[a,b]内元素的和。
# 
# 示例:
# 输入:
# 8 4
# 3 2 4 5 1 1 5 3
# 2 4
# 5 6
# 1 8
# 3 3
# 输出:
# 11
# 2
# 24
# 4
# 
# 提示:
# 1 <= n, q <= 2 * 10^5
# -10^9 <= x <= 10^9
# 
# 题目链接: https://cses.fi/problemset/task/1646
# 
# 解题思路:
# 使用基础前缀和技巧，预处理前缀和数组，然后O(1)时间查询。
# 
# 时间复杂度: 
# - 初始化: O(n) - 需要遍历整个数组构建前缀和数组
# - 查询: O(1) - 每次查询只需要常数时间
# 空间复杂度: O(n) - 需要额外的前缀和数组空间
# 
# 工程化考量:
# 1. 边界条件处理：空数组、单元素数组
# 2. 性能优化：预处理前缀和，查询时O(1)时间
# 3. 空间优化：必须存储前缀和数组，无法避免
# 4. 大数处理：元素值可能很大，需要确保整数范围
# 
# 最优解分析:
# 这是最优解，因为查询次数可能很多，预处理后可以实现O(1)查询时间。
# 对于静态数组的区间和查询，前缀和是最佳选择。
# 
# 算法核心:
# 前缀和公式：
# prefixSum[i] = prefixSum[i-1] + arr[i-1]
# 区间和公式：
# sumRange(a, b) = prefixSum[b] - prefixSum[a-1]
# 
# 算法调试技巧:
# 1. 打印中间过程：显示前缀和数组的计算过程
# 2. 边界测试：测试空数组、单元素数组等特殊情况
# 3. 性能测试：测试大规模数组下的性能表现
# 
# 语言特性差异:
# Python中列表是动态数组，可以直接获取长度。
# 与Java相比，Python有自动内存管理，无需手动释放数组内存。
# 与C++相比，Python是动态类型语言，无需显式声明数组类型。

import sys

class PrefixSumArray:
    def __init__(self, arr):
        n = len(arr)
        # 创建前缀和数组，大小为n+1
        # 使用n+1可以避免边界检查
        self.prefixSum = [0] * (n + 1)
        
        # 计算前缀和，时间复杂度O(n)
        for i in range(1, n + 1):
            # 前缀和公式：当前前缀和 = 前一个前缀和 + 当前元素
            self.prefixSum[i] = self.prefixSum[i - 1] + arr[i - 1]
            # 调试打印：显示前缀和计算过程
            # print(f"位置 {i}: prefixSum[{i}] = {self.prefixSum[i]}")
    
    def sumRange(self, left, right):
        # 使用前缀和公式计算区间和，时间复杂度O(1)
        # 公式：区间和 = 右边界前缀和 - 左边界前缀和
        result = self.prefixSum[right] - self.prefixSum[left - 1]
        
        # 调试打印：显示查询过程
        # print(f"查询区间 [{left}, {right}]: 结果 = {result}")
        
        return result

def main():
    import sys
    input = sys.stdin.read
    data = input().split()
    
    n = int(data[0])
    q = int(data[1])
    
    arr = [int(data[i + 2]) for i in range(n)]
    
    prefixSumArray = PrefixSumArray(arr)
    
    index = 2 + n
    results = []
    
    for _ in range(q):
        a = int(data[index])
        index += 1
        b = int(data[index])
        index += 1
        result = prefixSumArray.sumRange(a, b)
        results.append(str(result))
    
    print('\n'.join(results))

if __name__ == "__main__":
    main()