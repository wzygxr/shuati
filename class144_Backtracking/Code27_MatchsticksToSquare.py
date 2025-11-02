from typing import List

class Solution:
    """
    LeetCode 473. 火柴拼正方形
    
    题目描述：
    你将得到一个整数数组 matchsticks ，其中 matchsticks[i] 是第 i 个火柴棒的长度。
    你要用所有的火柴棍拼成一个正方形。你不能折断任何一根火柴棒，但你可以把它们连在一起，
    而且每根火柴棒必须使用一次。
    如果你能使这个正方形，则返回 true，否则返回 false。
    
    示例：
    输入: matchsticks = [1,1,2,2,2]
    输出: true
    解释: 可以拼成一个边长为2的正方形，每边两根火柴
    
    输入: matchsticks = [3,3,3,3,4]
    输出: false
    解释: 不能用所有火柴拼成一个正方形
    
    提示：
    1 <= matchsticks.length <= 15
    0 <= matchsticks[i] <= 10^9
    
    链接：https://leetcode.cn/problems/matchsticks-to-square/
    
    算法思路：
    1. 计算所有火柴的总长度，如果不能被4整除，直接返回false
    2. 计算每条边的目标长度 = 总长度 / 4
    3. 将火柴从大到小排序，优先使用长火柴可以提前剪枝
    4. 使用回溯算法尝试将火柴分配到四条边
    5. 使用剪枝优化：如果当前边长度超过目标长度，提前终止
    
    时间复杂度：O(4^n)，最坏情况下需要尝试所有分配方式
    空间复杂度：O(n)，递归栈深度
    """
    
    def makesquare(self, matchsticks: List[int]) -> bool:
        total = sum(matchsticks)
        
        # 如果总长度不能被4整除，直接返回false
        if total % 4 != 0:
            return False
        
        # 目标边长
        target = total // 4
        
        # 从大到小排序，优先使用长火柴
        matchsticks.sort(reverse=True)
        
        # 如果最长的火柴大于目标边长，直接返回false
        if matchsticks[0] > target:
            return False
        
        # 四条边的当前长度
        sides = [0, 0, 0, 0]
        
        return self.backtrack(matchsticks, 0, sides, target)
    
    def backtrack(self, matchsticks: List[int], index: int, sides: List[int], target: int) -> bool:
        # 终止条件：所有火柴都已分配
        if index == len(matchsticks):
            # 检查四条边是否都等于目标长度
            return all(side == target for side in sides)
        
        # 尝试将当前火柴分配到四条边
        for i in range(4):
            # 剪枝：如果当前边长度加上当前火柴长度超过目标长度，跳过
            if sides[i] + matchsticks[index] > target:
                continue
            
            # 剪枝：如果当前边与前一条边长度相同，且前一条边没有分配当前火柴，跳过
            # 避免重复计算相同的情况
            if i > 0 and sides[i] == sides[i - 1]:
                continue
            
            sides[i] += matchsticks[index]
            if self.backtrack(matchsticks, index + 1, sides, target):
                return True
            sides[i] -= matchsticks[index]
        
        return False

def test_matchsticks_to_square():
    solution = Solution()
    
    # 测试用例1
    matchsticks1 = [1, 1, 2, 2, 2]
    result1 = solution.makesquare(matchsticks1)
    print("输入: matchsticks = [1, 1, 2, 2, 2]")
    print("输出:", result1)
    
    # 测试用例2
    matchsticks2 = [3, 3, 3, 3, 4]
    result2 = solution.makesquare(matchsticks2)
    print("\n输入: matchsticks = [3, 3, 3, 3, 4]")
    print("输出:", result2)
    
    # 测试用例3
    matchsticks3 = [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1]  # 12根长度为1的火柴
    result3 = solution.makesquare(matchsticks3)
    print("\n输入: matchsticks = [1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1]")
    print("输出:", result3)

if __name__ == "__main__":
    test_matchsticks_to_square()