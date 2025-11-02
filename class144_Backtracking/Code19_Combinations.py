from typing import List

class Solution:
    """
    LeetCode 77. 组合
    
    题目描述：
    给定两个整数 n 和 k，返回范围 [1, n] 中所有可能的 k 个数的组合。
    你可以按任何顺序返回答案。
    
    示例：
    输入：n = 4, k = 2
    输出：[[1,2],[1,3],[1,4],[2,3],[2,4],[3,4]]
    
    输入：n = 1, k = 1
    输出：[[1]]
    
    提示：
    1 <= n <= 20
    1 <= k <= n
    
    链接：https://leetcode.cn/problems/combinations/
    
    算法思路：
    1. 使用回溯算法生成所有可能的组合
    2. 从1开始，每次选择一个数字，然后递归选择下一个数字
    3. 当组合长度达到k时，将其加入结果集
    4. 通过控制起始位置避免重复组合
    
    时间复杂度：O(C(n, k) * k)，其中C(n, k)是组合数，每个组合需要O(k)时间复制
    空间复杂度：O(k)，递归栈深度和存储路径的空间
    """
    
    def combine(self, n: int, k: int) -> List[List[int]]:
        result = []
        self.backtrack(n, k, 1, [], result)
        return result
    
    def backtrack(self, n: int, k: int, start: int, path: List[int], result: List[List[int]]) -> None:
        # 终止条件：组合长度达到k
        if len(path) == k:
            result.append(path[:])
            return
        
        # 剪枝优化：如果剩余的数字不足以填满组合，提前终止
        # 还需要选择的数字个数：k - len(path)
        # 从start到n至少要有这么多个数字：n - start + 1 >= k - len(path)
        # 所以 i 的范围是 start 到 n - (k - len(path)) + 1
        for i in range(start, n - (k - len(path)) + 2):
            path.append(i)  # 选择当前数字
            self.backtrack(n, k, i + 1, path, result)  # 递归选择下一个数字
            path.pop()  # 撤销选择

def test_combinations():
    solution = Solution()
    
    # 测试用例1
    n1, k1 = 4, 2
    result1 = solution.combine(n1, k1)
    print(f"输入: n = {n1}, k = {k1}")
    print("输出:", result1)
    
    # 测试用例2
    n2, k2 = 1, 1
    result2 = solution.combine(n2, k2)
    print(f"\n输入: n = {n2}, k = {k2}")
    print("输出:", result2)
    
    # 测试用例3
    n3, k3 = 5, 3
    result3 = solution.combine(n3, k3)
    print(f"\n输入: n = {n3}, k = {k3}")
    print("输出:", result3)

if __name__ == "__main__":
    test_combinations()