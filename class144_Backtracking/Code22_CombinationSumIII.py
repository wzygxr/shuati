from typing import List

class Solution:
    """
    LeetCode 216. 组合总和 III
    
    题目描述：
    找出所有相加之和为 n 的 k 个数的组合。组合中只允许含有 1 - 9 的正整数，并且每种组合中不存在重复的数字。
    
    示例：
    输入: k = 3, n = 7
    输出: [[1,2,4]]
    
    输入: k = 3, n = 9
    输出: [[1,2,6],[1,3,5],[2,3,4]]
    
    输入: k = 4, n = 1
    输出: []
    
    提示：
    2 <= k <= 9
    1 <= n <= 60
    
    链接：https://leetcode.cn/problems/combination-sum-iii/
    
    算法思路：
    1. 使用回溯算法生成所有可能的组合
    2. 从1开始，每次选择一个数字，然后递归选择下一个数字
    3. 当组合长度达到k且和为n时，将其加入结果集
    4. 通过控制起始位置避免重复组合
    5. 使用剪枝优化：当当前和已经超过n时提前终止
    
    时间复杂度：O(C(9, k))，从9个数字中选择k个数字的组合数
    空间复杂度：O(k)，递归栈深度
    """
    
    def combinationSum3(self, k: int, n: int) -> List[List[int]]:
        result = []
        self.backtrack(k, n, 1, [], result)
        return result
    
    def backtrack(self, k: int, n: int, start: int, path: List[int], result: List[List[int]]) -> None:
        # 终止条件：已选择k个数字
        if k == 0:
            # 如果和为n，加入结果集
            if n == 0:
                result.append(path[:])
            return
        
        # 剪枝优化：如果剩余的数字不足以填满组合，提前终止
        # 还需要选择的数字个数：k
        # 从start到9至少要有这么多个数字：9 - start + 1 >= k
        # 所以 i 的范围是 start 到 9 - k + 1
        for i in range(start, 10 - k):
            # 剪枝：如果当前数字已经大于剩余目标值，提前终止
            if i > n:
                break
            
            path.append(i)
            self.backtrack(k - 1, n - i, i + 1, path, result)
            path.pop()

def test_combination_sum_iii():
    solution = Solution()
    
    # 测试用例1
    k1, n1 = 3, 7
    result1 = solution.combinationSum3(k1, n1)
    print(f"输入: k = {k1}, n = {n1}")
    print("输出:", result1)
    
    # 测试用例2
    k2, n2 = 3, 9
    result2 = solution.combinationSum3(k2, n2)
    print(f"\n输入: k = {k2}, n = {n2}")
    print("输出:", result2)
    
    # 测试用例3
    k3, n3 = 4, 1
    result3 = solution.combinationSum3(k3, n3)
    print(f"\n输入: k = {k3}, n = {n3}")
    print("输出:", result3)

if __name__ == "__main__":
    test_combination_sum_iii()