class Solution:
    """
    LeetCode 526. 优美的排列
    
    题目描述：
    假设有从 1 到 n 的 n 个整数。用这些整数构造一个数组 perm（下标从 1 开始），
    只要满足下述条件之一，该数组就是一个优美的排列：
    1. perm[i] 能够被 i 整除
    2. i 能够被 perm[i] 整除
    
    给你一个整数 n ，返回可以构造的优美排列的数量。
    
    示例：
    输入：n = 2
    输出：2
    解释：第 1 个优美的排列是 [1,2]：perm[1] = 1 能被 i=1 整除，perm[2] = 2 能被 i=2 整除
          第 2 个优美的排列是 [2,1]：perm[1] = 2 能被 i=1 整除，i=2 能被 perm[2] = 1 整除
    
    输入：n = 1
    输出：1
    
    提示：
    1 <= n <= 15
    
    链接：https://leetcode.cn/problems/beautiful-arrangement/
    
    算法思路：
    1. 使用回溯算法生成所有可能的排列
    2. 在生成排列的过程中，提前剪枝：如果当前数字不满足优美排列的条件，则跳过
    3. 使用布尔数组标记已使用的数字
    4. 当排列完成时，计数加1
    
    时间复杂度：O(n!)，需要生成所有排列
    空间复杂度：O(n)，递归栈深度和标记数组
    """
    
    def countArrangement(self, n: int) -> int:
        self.count = 0
        used = [False] * (n + 1)
        self.backtrack(n, 1, used)
        return self.count
    
    def backtrack(self, n: int, pos: int, used: list) -> None:
        # 终止条件：已经排列完所有数字
        if pos > n:
            self.count += 1
            return
        
        for num in range(1, n + 1):
            # 如果数字未被使用且满足优美排列条件
            if not used[num] and self.is_valid(pos, num):
                used[num] = True
                self.backtrack(n, pos + 1, used)
                used[num] = False
    
    def is_valid(self, pos: int, num: int) -> bool:
        """检查位置pos放置数字num是否满足优美排列条件"""
        return num % pos == 0 or pos % num == 0

def test_beautiful_arrangement():
    solution = Solution()
    
    # 测试用例1
    n1 = 2
    result1 = solution.countArrangement(n1)
    print(f"输入: n = {n1}")
    print("输出:", result1)
    
    # 测试用例2
    n2 = 1
    result2 = solution.countArrangement(n2)
    print(f"\n输入: n = {n2}")
    print("输出:", result2)
    
    # 测试用例3
    n3 = 3
    result3 = solution.countArrangement(n3)
    print(f"\n输入: n = {n3}")
    print("输出:", result3)
    
    # 测试用例4
    n4 = 4
    result4 = solution.countArrangement(n4)
    print(f"\n输入: n = {n4}")
    print("输出:", result4)

if __name__ == "__main__":
    test_beautiful_arrangement()