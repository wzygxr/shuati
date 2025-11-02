# LeetCode 70. Climbing Stairs (爬楼梯递归)
# 测试链接 : https://leetcode.cn/problems/climbing-stairs/

class LC70_ClimbingStairs:
    def climbStairs(self, n: int) -> int:
        # 使用记忆化递归
        memo = {}
        return self.climbStairsHelper(n, memo)
    
    def climbStairsHelper(self, n: int, memo: dict) -> int:
        # 基础情况
        if n <= 2:
            return n
        
        # 如果已经计算过，直接返回
        if n in memo:
            return memo[n]
        
        # 递归计算并存储结果
        memo[n] = self.climbStairsHelper(n - 1, memo) + self.climbStairsHelper(n - 2, memo)
        return memo[n]

# 测试用例
def main():
    solution = LC70_ClimbingStairs()
    
    # 测试用例1
    n1 = 2
    print(f"输入: {n1}")
    print(f"输出: {solution.climbStairs(n1)}")
    print(f"期望: 2\n")
    
    # 测试用例2
    n2 = 3
    print(f"输入: {n2}")
    print(f"输出: {solution.climbStairs(n2)}")
    print(f"期望: 3\n")

if __name__ == "__main__":
    main()