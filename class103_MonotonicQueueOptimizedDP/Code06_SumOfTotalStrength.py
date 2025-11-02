# 巫师力量和
# 题目可以简化为如下的描述
# 给定一个长度为n的数组arr，下标从0开始
# 任何一个子数组的指标为，子数组累加和 * 子数组中最小值
# 返回arr中所有子数组指标的累加和，答案对 1000000007 取模
# 1 <= n <= 10^5
# 1 <= arr[i] <= 10^9
# 测试链接 : https://leetcode.cn/problems/sum-of-total-strength-of-wizards/

"""
巫师力量和问题 - 单调栈优化解法

算法思路详解：
1. 问题分析：
   - 需要计算所有子数组的(累加和 * 最小值)之和
   - 朴素解法：枚举所有子数组，时间复杂度O(n^2)，会超时
   
2. 优化思路：
   - 使用单调栈找到每个元素作为最小值的影响范围
   - 对于每个元素arr[m]，找到左右边界l和r，使得arr[m]是区间[l+1, r-1]的最小值
   - 计算所有以arr[m]为最小值的子数组的贡献
   
3. 数学变换：
   - 对于固定的最小值arr[m]，需要计算所有包含m的子数组的累加和之和
   - 使用前缀和的前缀和(sumsum数组)来高效计算
   
4. 时间复杂度分析：
   - 时间复杂度：O(n)，每个元素最多入栈和出栈一次
   - 空间复杂度：O(n)，栈和前缀和数组的空间
   
5. 为什么是最优解：
   - 该解法将朴素O(n^2)优化到O(n)
   - 利用单调栈和前缀和的前缀和，是此类问题的最优解法
   
6. 工程化考量：
   - 使用大整数避免溢出
   - 及时取模防止溢出
   - 列表预分配空间
"""

MOD = 1000000007

class Code06_SumOfTotalStrength:
    
    @staticmethod
    def totalStrength(arr):
        """
        计算所有子数组的(累加和 * 最小值)之和
        
        Args:
            arr: 输入数组
            
        Returns:
            所有子数组指标之和，对MOD取模
        """
        n = len(arr)
        if n == 0:
            return 0
        
        # 计算前缀和的前缀和(sumsum数组)
        # sumsum[i] = sum_{j=0}^{i} prefixSum[j]
        # 其中prefixSum[j] = sum_{k=0}^{j} arr[k]
        sumsum = [0] * n
        pre = arr[0] % MOD
        sumsum[0] = pre
        
        for i in range(1, n):
            pre = (pre + arr[i]) % MOD
            sumsum[i] = (sumsum[i - 1] + pre) % MOD
        
        # 单调栈，用于找到每个元素作为最小值的影响范围
        stack = []
        ans = 0
        
        # 第一遍遍历：处理每个元素作为最小值的情况
        for i in range(n):
            # 维护单调递增栈
            while stack and arr[stack[-1]] >= arr[i]:
                m = stack.pop()  # 当前最小值的位置
                l = stack[-1] if stack else -1  # 左边界
                ans = (ans + Code06_SumOfTotalStrength.calculate_sum(arr, sumsum, l, m, i)) % MOD  # 计算贡献
            
            stack.append(i)
        
        # 处理栈中剩余元素
        while stack:
            m = stack.pop()
            l = stack[-1] if stack else -1
            ans = (ans + Code06_SumOfTotalStrength.calculate_sum(arr, sumsum, l, m, n)) % MOD
        
        return ans
    
    @staticmethod
    def calculate_sum(arr, sumsum, l, m, r):
        """
        计算以arr[m]为最小值的所有子数组的贡献
        
        Args:
            arr: 输入数组
            sumsum: 前缀和的前缀和数组
            l: 左边界（不包含）
            m: 最小值位置
            r: 右边界（不包含）
            
        Returns:
            贡献值，对MOD取模
        """
        # 计算左半部分的贡献
        left = sumsum[r - 1]
        if m - 1 >= 0:
            left = (left - sumsum[m - 1] + MOD) % MOD
        left = (left * (m - l)) % MOD
        
        # 计算右半部分的贡献
        right = 0
        if m - 1 >= 0:
            right = (right + sumsum[m - 1]) % MOD
        if l - 1 >= 0:
            right = (right - sumsum[l - 1] + MOD) % MOD
        right = (right * (r - m)) % MOD
        
        # 总贡献 = (左半部分 - 右半部分) * 最小值
        return ((left - right + MOD) % MOD * arr[m]) % MOD

    '''
    算法详细解释：
    
    1. 核心思想：
       - 对于每个元素arr[m]，找到它作为最小值的最大区间[l+1, r-1]
       - 计算所有包含m且最小值是arr[m]的子数组的贡献
       
    2. 单调栈的作用：
       - 维护一个递增栈，栈顶元素最小
       - 当遇到更小的元素时，弹出栈顶元素并计算其贡献
       - 弹出的元素arr[m]的右边界就是当前元素i
       - 左边界就是栈中下一个元素（如果存在）
       
    3. sumsum数组的数学意义：
       - prefixSum[i] = arr[0] + arr[1] + ... + arr[i]
       - sumsum[i] = prefixSum[0] + prefixSum[1] + ... + prefixSum[i]
       - 用于高效计算子数组累加和的和
       
    4. 贡献计算原理：
       - 对于区间[l+1, r-1]中的每个子数组[start, end]包含m
       - 需要计算所有这样的子数组的累加和之和
       - 使用sumsum数组可以O(1)时间计算
       
    5. 边界情况处理：
       - l = -1时，表示左边界是数组开头
       - r = n时，表示右边界是数组结尾
       - 及时取模防止溢出
       
    6. 时间复杂度证明：
       - 每个元素最多入栈一次、出栈一次
       - 每次操作都是O(1)时间
       - 总时间复杂度O(n)
       
    7. 空间复杂度分析：
       - 栈空间：O(n)
       - sumsum数组：O(n)
       - 总空间复杂度O(n)
       
    8. 为什么这是最优解：
       - 问题本身需要遍历所有子数组，朴素解法O(n^2)
       - 该解法利用单调性将复杂度降到O(n)
       - 无法进一步优化，因为需要处理每个元素
       
    9. Python特定优化：
       - 使用列表的append和pop操作，时间复杂度O(1)
       - 避免不必要的列表复制
       - 使用局部变量减少属性查找时间
       
    10. 工程化改进点：
        - 添加类型注解提高代码可读性
        - 使用更直观的变量命名
        - 考虑极端输入情况（如全0数组）
        - 添加单元测试验证正确性
    '''

# 测试函数
def test():
    solution = Code06_SumOfTotalStrength
    
    # 测试用例1
    arr1 = [1, 3, 1, 2]
    result1 = solution.totalStrength(arr1)
    print(f"测试用例1结果: {result1}")  # 期望输出: 44
    
    # 测试用例2
    arr2 = [5, 4, 6]
    result2 = solution.totalStrength(arr2)
    print(f"测试用例2结果: {result2}")  # 期望输出: 213
    
    # 测试用例3：边界情况，空数组
    arr3 = []
    result3 = solution.totalStrength(arr3)
    print(f"测试用例3结果: {result3}")  # 期望输出: 0
    
    # 测试用例4：单元素数组
    arr4 = [10]
    result4 = solution.totalStrength(arr4)
    print(f"测试用例4结果: {result4}")  # 期望输出: 100
    
    # 测试用例5：全相同元素
    arr5 = [2, 2, 2, 2]
    result5 = solution.totalStrength(arr5)
    print(f"测试用例5结果: {result5}")  # 期望输出: 需要计算

if __name__ == "__main__":
    test()