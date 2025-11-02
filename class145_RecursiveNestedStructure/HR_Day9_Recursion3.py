# HackerRank Day 9: Recursion 3 (阶乘递归)
# 测试链接 : https://www.hackerrank.com/challenges/30-recursion/problem

class HR_Day9_Recursion3:
    def factorial(self, n: int) -> int:
        # 基础情况
        if n <= 1:
            return 1
        
        # 递归情况
        return n * self.factorial(n - 1)

# 测试用例
def main():
    solution = HR_Day9_Recursion3()
    
    # 测试用例1
    n1 = 3
    print(f"输入: {n1}")
    print(f"输出: {solution.factorial(n1)}")
    print(f"期望: 6\n")
    
    # 测试用例2
    n2 = 5
    print(f"输入: {n2}")
    print(f"输出: {solution.factorial(n2)}")
    print(f"期望: 120\n")

if __name__ == "__main__":
    main()