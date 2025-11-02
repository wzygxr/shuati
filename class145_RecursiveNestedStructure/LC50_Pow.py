# LeetCode 50. Pow(x, n) (快速幂递归)
# 测试链接 : https://leetcode.cn/problems/powx-n/

class LC50_Pow:
    def myPow(self, x: float, n: int) -> float:
        # 处理负指数
        N = n
        if N < 0:
            x = 1 / x
            N = -N
        
        return self.fastPow(x, N)
    
    def fastPow(self, x: float, n: int) -> float:
        # 基础情况
        if n == 0:
            return 1.0
        
        # 递归计算
        half = self.fastPow(x, n // 2)
        
        if n % 2 == 0:
            return half * half
        else:
            return half * half * x

# 测试用例
def main():
    solution = LC50_Pow()
    
    # 测试用例1
    x1 = 2.00000
    n1 = 10
    print(f"输入: x = {x1}, n = {n1}")
    print(f"输出: {solution.myPow(x1, n1)}")
    print(f"期望: 1024.00000\n")
    
    # 测试用例2
    x2 = 2.10000
    n2 = 3
    print(f"输入: x = {x2}, n = {n2}")
    print(f"输出: {solution.myPow(x2, n2)}")
    print(f"期望: 9.26100\n")
    
    # 测试用例3
    x3 = 2.00000
    n3 = -2
    print(f"输入: x = {x3}, n = {n3}")
    print(f"输出: {solution.myPow(x3, n3)}")
    print(f"期望: 0.25000\n")

if __name__ == "__main__":
    main()