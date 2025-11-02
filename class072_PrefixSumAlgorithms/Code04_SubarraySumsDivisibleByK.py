"""
和可被K整除的子数组 (Subarray Sums Divisible by K)

题目描述:
给定一个整数数组 A，返回其中元素之和可被 K 整除的（连续、非空）子数组的数目。

示例:
输入: A = [4,5,0,-2,-3,1], K = 5
输出: 7
解释: 有 7 个子数组满足其元素之和可被 K = 5 整除。

输入: A = [5], K = 9
输出: 0

提示:
1 <= A.length <= 3 * 10^4
-10^4 <= A[i] <= 10^4
2 <= K <= 10^4

题目链接: https://leetcode.com/problems/subarray-sums-divisible-by-k/

解题思路:
1. 利用前缀和的性质：如果两个前缀和除以K的余数相同，那么这两个位置之间的子数组和可被K整除
2. 使用前缀和 + 哈希表的方法
3. 遍历数组，计算前缀和并对K取余
4. 使用哈希表记录每个余数出现的次数
5. 对于相同的余数，任意两个位置之间的子数组都满足条件

时间复杂度: O(n) - 需要遍历数组一次
空间复杂度: O(min(n, K)) - 哈希表最多存储K个不同的余数或n个前缀和

工程化考量:
1. 负数余数处理：Python的模运算与数学定义不同，需要特殊处理
2. 边界条件：空数组、K=0等特殊情况
3. 性能优化：一次遍历完成所有计算
4. 哈希表初始化：空前缀和的余数为0出现1次

最优解分析:
这是最优解，时间复杂度O(n)，空间复杂度O(min(n,K))。
必须遍历所有元素才能统计所有满足条件的子数组。

数学原理:
设prefix[i]为前i个元素的和，则子数组[i,j]的和为prefix[j] - prefix[i-1]
要求(prefix[j] - prefix[i-1]) % K == 0 → prefix[j] % K == prefix[i-1] % K

算法调试技巧:
1. 打印中间过程：显示每个位置的前缀和和余数
2. 边界测试：测试K=0、负数数组等特殊情况
3. 性能测试：测试大规模数据下的性能表现

语言特性差异:
Python的模运算对于负数结果与数学定义不同，需要特殊处理。
Java/C++的模运算对于负数结果与数学定义一致，不需要额外处理。
"""

class Solution:
    def subarraysDivByK(self, A, K):
        """
        计算和可被K整除的子数组数目
        
        Args:
            A (List[int]): 输入数组
            K (int): 除数
            
        Returns:
            int: 和可被K整除的子数组数目
            
        异常场景处理:
        - 空数组：返回0
        - K=0：根据题目约束K>=2，无需处理
        - 负数处理：Python模运算需要特殊处理负数
        
        边界条件:
        - 数组为空
        - K=1（但题目约束K>=2）
        - 数组元素全为0
        - 数组元素全为负数
        """
        # 边界情况处理
        if not A or K == 0:
            return 0
        
        # 字典记录每个余数出现的次数
        # 初始化：余数为0出现1次（表示空前缀）
        # 数学原理：prefix[j] % K == prefix[i-1] % K
        remainder_count = {0: 1}
        
        count = 0   # 结果计数
        sum_val = 0 # 当前前缀和
        
        # 遍历数组，时间复杂度O(n)
        for i, num in enumerate(A):
            # 更新前缀和
            sum_val += num
            
            # 计算前缀和对K的余数
            # Python的模运算：对于负数，结果与数学定义不同
            # 例如：-1 % 5 = 4（Python），数学上应该是-1
            remainder = sum_val % K
            
            # 处理负数余数：如果余数为负，加上K使其为正
            # 这是Python特有的处理，确保余数在[0, K-1]范围内
            if remainder < 0:
                remainder += K
            
            # 调试打印：显示中间过程
            # print(f"位置 {i}: 数字 = {num}, 前缀和 = {sum_val}, 余数 = {remainder}")
            
            # 如果该余数之前出现过，说明存在满足条件的子数组
            if remainder in remainder_count:
                # 当前余数出现的次数就是新发现的子数组数量
                count += remainder_count[remainder]
                # 调试打印：找到新子数组
                # print(f"找到 {remainder_count[remainder]} 个新子数组，总计数 = {count}")
            
            # 更新该余数的出现次数
            remainder_count[remainder] = remainder_count.get(remainder, 0) + 1
        
        return count


def test_subarrays_divisible_by_k():
    """单元测试函数"""
    print("=== 和可被K整除的子数组单元测试 ===")
    solution = Solution()
    
    # 测试用例1：题目示例
    A1 = [4, 5, 0, -2, -3, 1]
    K1 = 5
    result1 = solution.subarraysDivByK(A1, K1)
    print(f"测试用例1 [4,5,0,-2,-3,1], K=5: {result1} (预期: 7)")
    
    # 测试用例2：单个元素
    A2 = [5]
    K2 = 9
    result2 = solution.subarraysDivByK(A2, K2)
    print(f"测试用例2 [5], K=9: {result2} (预期: 0)")
    
    # 测试用例3：空数组
    A3 = []
    K3 = 5
    result3 = solution.subarraysDivByK(A3, K3)
    print(f"测试用例3 [], K=5: {result3} (预期: 0)")
    
    # 测试用例4：全0数组
    A4 = [0, 0, 0]
    K4 = 3
    result4 = solution.subarraysDivByK(A4, K4)
    print(f"测试用例4 [0,0,0], K=3: {result4} (预期: 6)")
    
    # 测试用例5：负数数组
    A5 = [-1, -2, -3]
    K5 = 3
    result5 = solution.subarraysDivByK(A5, K5)
    print(f"测试用例5 [-1,-2,-3], K=3: {result5} (预期: 2)")
    
    # 测试用例6：边界情况
    A6 = [2, 3, 7]
    K6 = 1
    result6 = solution.subarraysDivByK(A6, K6)
    print(f"测试用例6 [2,3,7], K=1: {result6} (预期: 6)")

def performance_test():
    """性能测试函数"""
    print("\n=== 性能测试 ===")
    solution = Solution()
    size = 30000  # 3万元素
    import random
    large_array = [random.randint(-10000, 10000) for _ in range(size)]
    K = 100
    
    import time
    start_time = time.time()
    result = solution.subarraysDivByK(large_array, K)
    end_time = time.time()
    
    print(f"处理 {size} 个元素，子数组数量: {result}, 耗时: {end_time - start_time:.4f}秒")

if __name__ == "__main__":
    # 运行单元测试
    test_subarrays_divisible_by_k()
    
    # 运行性能测试（可选）
    # performance_test()
    
    print("\n=== 测试完成 ===")