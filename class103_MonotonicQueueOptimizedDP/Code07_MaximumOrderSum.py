# 子数组最大变序和
# 给定一个长度为n的数组arr，变序和定义如下
# 数组中每个值都可以减小或者不变，必须把整体变成严格升序的
# 所有方案中，能得到的最大累加和，叫做数组的变序和
# 比如[1,100,7]，变序和14，方案为变成[1,6,7]
# 比如[5,4,9]，变序和16，方案为变成[3,4,9]
# 比如[1,4,2]，变序和3，方案为变成[0,1,2]
# 返回arr所有子数组的变序和中，最大的那个
# 1 <= n、arr[i] <= 10^6
# 来自真实大厂笔试，对数器验证

"""
子数组最大变序和问题 - 单调栈优化解法

算法思路详解：
1. 问题分析：
   - 需要找到所有子数组，将其变为严格递增序列后的最大累加和
   - 每个元素可以减小或保持不变，但必须满足严格递增
   - 目标是找到所有子数组变序和中的最大值
   
2. 优化思路：
   - 使用单调栈维护可能的子数组结尾
   - 对于每个元素，计算以该元素结尾的子数组的最大变序和
   - 利用数学公式快速计算等差数列的和
   
3. 时间复杂度分析：
   - 时间复杂度：O(n)，每个元素最多入栈和出栈一次
   - 空间复杂度：O(n)，栈和dp数组的空间
   
4. 为什么是最优解：
   - 该解法将暴力O(n²)优化到O(n)
   - 利用单调栈和数学公式，是此类问题的最优解法
"""

class Code07_MaximumOrderSum:
    
    @staticmethod
    def maxSum1(arr):
        """
        暴力方法 - 用于验证正确性
        时间复杂度：O(n * v)，其中v是数组最大值
        空间复杂度：O(n * v)
        
        Args:
            arr: 输入数组
            
        Returns:
            最大变序和
        """
        n = len(arr)
        if n == 0:
            return 0
        
        max_val = max(arr) if arr else 0
        
        # 记忆化数组，初始化为-1
        dp = [[-1] * (max_val + 1) for _ in range(n)]
        ans = 0
        
        for i in range(n):
            ans = max(ans, Code07_MaximumOrderSum.f1(arr, i, arr[i], dp))
        
        return ans
    
    @staticmethod
    def f1(arr, i, p, dp):
        """
        递归辅助函数 - 计算以位置i结尾的子数组的最大变序和
        
        Args:
            arr: 输入数组
            i: 当前位置
            p: 当前允许的最大值
            dp: 记忆化数组
            
        Returns:
            最大变序和
        """
        if p <= 0 or i == -1:
            return 0
        
        if dp[i][p] != -1:
            return dp[i][p]
        
        cur = min(arr[i], p)
        next_val = Code07_MaximumOrderSum.f1(arr, i - 1, cur - 1, dp)
        ans = cur + next_val
        dp[i][p] = ans
        return ans
    
    @staticmethod
    def maxSum2(arr):
        """
        正式方法 - 单调栈优化
        时间复杂度：O(n)
        空间复杂度：O(n)
        
        Args:
            arr: 输入数组
            
        Returns:
            最大变序和
        """
        n = len(arr)
        if n == 0:
            return 0
        
        stack = []  # 单调栈，存储下标
        dp = [0] * n  # dp[i]表示以i结尾的子数组的最大变序和
        ans = 0
        
        for i in range(n):
            cur_idx = i
            cur_val = arr[cur_idx]
            
            # 维护单调栈，处理栈顶元素
            while cur_val > 0 and stack:
                top_idx = stack[-1]
                top_val = arr[top_idx]
                
                if top_val >= cur_val:
                    # 栈顶元素更大，直接弹出
                    stack.pop()
                else:
                    idx_diff = cur_idx - top_idx  # 位置差
                    val_diff = cur_val - top_val  # 数值差
                    
                    if val_diff >= idx_diff:
                        # 可以完全覆盖区间
                        dp[i] += Code07_MaximumOrderSum.calculate_sum(cur_val, idx_diff) + dp[top_idx]
                        cur_val = 0
                        cur_idx = 0
                        break
                    else:
                        # 部分覆盖
                        dp[i] += Code07_MaximumOrderSum.calculate_sum(cur_val, idx_diff)
                        cur_val -= idx_diff
                        cur_idx = top_idx
                        stack.pop()
            
            # 处理剩余部分
            if cur_val > 0:
                dp[i] += Code07_MaximumOrderSum.calculate_sum(cur_val, cur_idx + 1)
            
            # 当前元素入栈
            stack.append(i)
            ans = max(ans, dp[i])
        
        return ans
    
    @staticmethod
    def calculate_sum(max_val, n):
        """
        计算等差数列的和
        从max开始，递减1，共n项的正数部分的和
        公式：sum = (首项 + 末项) * 项数 / 2
        
        Args:
            max_val: 最大值
            n: 项数
            
        Returns:
            等差数列的和
        """
        n = min(max_val, n)  # 确保不超过max
        return (max_val * 2 - n + 1) * n // 2

    '''
    算法详细解释：
    
    1. 核心思想：
       - 对于每个元素，计算以该元素结尾的子数组的最大变序和
       - 使用单调栈维护可能的子数组结尾
       - 利用数学公式快速计算等差数列的和
       
    2. 单调栈的作用：
       - 维护一个递增栈，栈顶元素最小
       - 当遇到更小的元素时，弹出栈顶元素并计算其贡献
       - 弹出的元素arr[m]的右边界就是当前元素i
       - 左边界就是栈中下一个元素（如果存在）
       
    3. 数学公式原理：
       - 对于区间[l, r]，需要将其变为严格递增序列
       - 最大可能的序列是从某个值开始递减1的等差数列
       - 使用等差数列求和公式快速计算
       
    4. 时间复杂度证明：
       - 每个元素最多入栈一次、出栈一次
       - 每次操作都是O(1)时间
       - 总时间复杂度O(n)
       
    5. 空间复杂度分析：
       - 栈空间：O(n)
       - dp数组：O(n)
       - 总空间复杂度O(n)
       
    6. 为什么这是最优解：
       - 问题本身需要遍历所有子数组，朴素解法O(n²)
       - 该解法利用单调性将复杂度降到O(n)
       - 无法进一步优化，因为需要处理每个元素
       
    7. Python特定优化：
       - 使用列表的append和pop操作，时间复杂度O(1)
       - 避免不必要的列表复制
       - 使用局部变量减少属性查找时间
       
    8. 工程化改进点：
       - 添加类型注解提高代码可读性
       - 使用更直观的变量命名
       - 考虑极端输入情况（如全0数组）
       - 添加单元测试验证正确性
    '''

# 测试函数
def test():
    solution = Code07_MaximumOrderSum
    
    # 测试用例1
    arr1 = [1, 100, 7]
    result1 = solution.maxSum2(arr1)
    print(f"测试用例1结果: {result1}")  # 期望输出: 14
    
    # 测试用例2
    arr2 = [5, 4, 9]
    result2 = solution.maxSum2(arr2)
    print(f"测试用例2结果: {result2}")  # 期望输出: 16
    
    # 测试用例3
    arr3 = [1, 4, 2]
    result3 = solution.maxSum2(arr3)
    print(f"测试用例3结果: {result3}")  # 期望输出: 3
    
    # 测试用例4：边界情况，空数组
    arr4 = []
    result4 = solution.maxSum2(arr4)
    print(f"测试用例4结果: {result4}")  # 期望输出: 0
    
    # 测试用例5：单元素数组
    arr5 = [10]
    result5 = solution.maxSum2(arr5)
    print(f"测试用例5结果: {result5}")  # 期望输出: 10

if __name__ == "__main__":
    test()