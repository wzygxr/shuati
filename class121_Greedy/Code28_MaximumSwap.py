# 最大交换
# 给定一个非负整数，你至多可以交换一次数字中的任意两位。返回你能得到的最大值。
# 测试链接: https://leetcode.cn/problems/maximum-swap/

class Solution:
    def maximumSwap(self, num: int) -> int:
        """
        最大交换问题的贪心解法
        
        解题思路：
        1. 将数字转换为字符数组便于处理
        2. 记录每个数字最后出现的位置
        3. 从左到右遍历，对于每个位置，尝试用后面最大的数字替换
        4. 如果找到可以交换的位置，进行一次交换并返回结果
        
        贪心策略的正确性：
        局部最优：在当前位置尝试用后面最大的数字替换
        全局最优：通过一次交换得到最大值
        
        时间复杂度：O(n)，其中n是数字的位数
        空间复杂度：O(n)，用于存储字符数组和位置信息
        
        Args:
            num: 输入数字
            
        Returns:
            交换一次后能得到的最大值
        """
        # 边界条件处理
        if num < 10:
            return num
        
        # 将数字转换为字符列表
        digits = list(str(num))
        n = len(digits)
        
        # 记录每个数字最后出现的位置
        last = [-1] * 10
        for i in range(n):
            last[int(digits[i])] = i
        
        # 从左到右遍历，尝试交换
        for i in range(n):
            # 从9到当前数字+1，寻找可以交换的最大数字
            for d in range(9, int(digits[i]), -1):
                # 如果这个数字出现在当前位置之后
                if last[d] > i:
                    # 交换这两个位置的数字
                    digits[i], digits[last[d]] = digits[last[d]], digits[i]
                    # 返回结果（只交换一次）
                    return int(''.join(digits))
        
        # 如果没有找到可以交换的位置，返回原数字
        return num
    
    def maximumSwap2(self, num: int) -> int:
        """
        最大交换问题的另一种解法（更直观）
        
        解题思路：
        1. 找到第一个下降的位置
        2. 在这个位置之后找到最大的数字
        3. 将这个最大数字与前面第一个比它小的数字交换
        
        时间复杂度：O(n)
        空间复杂度：O(n)
        """
        if num < 10:
            return num
        
        digits = list(str(num))
        n = len(digits)
        
        # 找到第一个下降的位置
        i = 0
        while i < n - 1 and digits[i] >= digits[i + 1]:
            i += 1
        
        # 如果整个数字是递减的，无法交换得到更大的数字
        if i == n - 1:
            return num
        
        # 在下降位置之后找到最大的数字
        max_index = i + 1
        for j in range(i + 2, n):
            if digits[j] >= digits[max_index]:
                max_index = j
        
        # 从左边找到第一个比最大数字小的位置
        for j in range(i + 1):
            if digits[j] < digits[max_index]:
                # 交换这两个位置的数字
                digits[j], digits[max_index] = digits[max_index], digits[j]
                return int(''.join(digits))
        
        return num

# 测试代码
def test_maximum_swap():
    solution = Solution()
    
    # 测试用例1
    # 输入: num = 2736
    # 输出: 7236
    # 解释: 交换数字2和数字7得到7236
    print(f"测试用例1结果: {solution.maximumSwap(2736)}")  # 期望输出: 7236
    
    # 测试用例2
    # 输入: num = 9973
    # 输出: 9973
    # 解释: 无法交换得到更大的数字
    print(f"测试用例2结果: {solution.maximumSwap(9973)}")  # 期望输出: 9973
    
    # 测试用例3
    # 输入: num = 98368
    # 输出: 98863
    # 解释: 交换数字3和数字8得到98863
    print(f"测试用例3结果: {solution.maximumSwap(98368)}")  # 期望输出: 98863
    
    # 测试用例4：边界情况
    # 输入: num = 1
    # 输出: 1
    print(f"测试用例4结果: {solution.maximumSwap(1)}")  # 期望输出: 1
    
    # 测试用例5：复杂情况
    # 输入: num = 1993
    # 输出: 9913
    # 解释: 交换数字1和数字9得到9913
    print(f"测试用例5结果: {solution.maximumSwap(1993)}")  # 期望输出: 9913
    
    # 测试用例6：极限情况
    # 输入: num = 123456789
    # 输出: 923456781
    # 解释: 交换数字1和数字9得到923456781
    print(f"测试用例6结果: {solution.maximumSwap(123456789)}")  # 期望输出: 923456781

if __name__ == "__main__":
    test_maximum_swap()