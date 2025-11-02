from typing import List

class Solution:
    """
    LeetCode 40. 组合总和 II
    
    题目描述：
    给定一个候选人编号的集合 candidates 和一个目标数 target ，找出 candidates 中所有可以使数字和为 target 的组合。
    candidates 中的每个数字在每个组合中只能使用一次。
    注意：解集不能包含重复的组合。 
    
    示例：
    输入: candidates = [10,1,2,7,6,1,5], target = 8
    输出: [[1,1,6],[1,2,5],[1,7],[2,6]]
    
    输入: candidates = [2,5,2,1,2], target = 5
    输出: [[1,2,2],[5]]
    
    提示：
    1 <= candidates.length <= 100
    1 <= candidates[i] <= 50
    1 <= target <= 30
    
    链接：https://leetcode.cn/problems/combination-sum-ii/
    
    算法思路：
    1. 先对数组进行排序，使相同元素相邻
    2. 使用回溯算法生成所有组合
    3. 对于重复元素，确保相同元素的相对顺序，避免生成重复组合
    4. 使用剪枝优化：当当前和超过target时提前终止
    
    时间复杂度：O(2^n)，其中n是数组长度
    空间复杂度：O(n)，递归栈深度
    """
    
    def combinationSum2(self, candidates: List[int], target: int) -> List[List[int]]:
        result = []
        candidates.sort()  # 先排序，使相同元素相邻
        self.backtrack(candidates, target, 0, [], result)
        return result
    
    def backtrack(self, candidates: List[int], target: int, start: int, path: List[int], result: List[List[int]]) -> None:
        # 终止条件：目标值为0
        if target == 0:
            result.append(path[:])
            return
        
        for i in range(start, len(candidates)):
            # 剪枝：如果当前数字已经大于剩余目标值，提前终止
            if candidates[i] > target:
                break
            
            # 去重关键：跳过重复元素（不是第一个出现的重复元素）
            if i > start and candidates[i] == candidates[i - 1]:
                continue
            
            path.append(candidates[i])
            # 注意：这里传入i+1，因为每个数字只能使用一次
            self.backtrack(candidates, target - candidates[i], i + 1, path, result)
            path.pop()

def test_combination_sum_ii():
    solution = Solution()
    
    # 测试用例1
    candidates1 = [10, 1, 2, 7, 6, 1, 5]
    target1 = 8
    result1 = solution.combinationSum2(candidates1, target1)
    print("输入: candidates = [10,1,2,7,6,1,5], target =", target1)
    print("输出:", result1)
    
    # 测试用例2
    candidates2 = [2, 5, 2, 1, 2]
    target2 = 5
    result2 = solution.combinationSum2(candidates2, target2)
    print("\n输入: candidates = [2,5,2,1,2], target =", target2)
    print("输出:", result2)

if __name__ == "__main__":
    test_combination_sum_ii()