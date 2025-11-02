class Solution:
    """
    LeetCode 60. 排列序列
    
    题目描述：
    给出集合 [1,2,3,...,n]，其所有元素共有 n! 种排列。
    按大小顺序列出所有排列情况，并找出第 k 个排列。
    
    示例：
    输入: n = 3, k = 3
    输出: "213"
    
    输入: n = 4, k = 9
    输出: "2314"
    
    输入: n = 3, k = 1
    输出: "123"
    
    提示：
    1 <= n <= 9
    1 <= k <= n!
    
    链接：https://leetcode.cn/problems/permutation-sequence/
    
    算法思路：
    1. 使用数学方法（康托展开）直接计算第k个排列
    2. 预先计算阶乘数组，用于快速确定每个位置的数字
    3. 从高位到低位依次确定每个位置的数字
    4. 使用列表记录可用的数字，每次选择一个数字后将其移除
    
    时间复杂度：O(n^2)，需要遍历n个位置，每个位置需要O(n)时间查找和删除
    空间复杂度：O(n)，存储阶乘数组和可用数字列表
    """
    
    def getPermutation(self, n: int, k: int) -> str:
        # 计算阶乘数组
        factorial = [1] * (n + 1)
        for i in range(1, n + 1):
            factorial[i] = factorial[i - 1] * i
        
        # 可用数字列表
        numbers = list(range(1, n + 1))
        result = []
        k -= 1  # 转换为0-based索引
        
        for i in range(n - 1, -1, -1):
            # 计算当前位应该选择第几个数字
            index = k // factorial[i]
            result.append(str(numbers[index]))
            # 移除已选择的数字
            numbers.pop(index)
            # 更新k值
            k %= factorial[i]
        
        return ''.join(result)

def test_permutation_sequence():
    solution = Solution()
    
    # 测试用例1
    n1, k1 = 3, 3
    result1 = solution.getPermutation(n1, k1)
    print(f"输入: n = {n1}, k = {k1}")
    print("输出:", result1)
    
    # 测试用例2
    n2, k2 = 4, 9
    result2 = solution.getPermutation(n2, k2)
    print(f"\n输入: n = {n2}, k = {k2}")
    print("输出:", result2)
    
    # 测试用例3
    n3, k3 = 3, 1
    result3 = solution.getPermutation(n3, k3)
    print(f"\n输入: n = {n3}, k = {k3}")
    print("输出:", result3)
    
    # 测试用例4
    n4, k4 = 1, 1
    result4 = solution.getPermutation(n4, k4)
    print(f"\n输入: n = {n4}, k = {k4}")
    print("输出:", result4)

if __name__ == "__main__":
    test_permutation_sequence()