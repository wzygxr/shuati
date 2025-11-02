"""
组合数学工具模块

算法简介:
实现组合数学中的常用算法，包括容斥原理、生成函数、拉格朗日插值等。

适用场景:
1. 计数问题
2. 容斥原理应用
3. 生成函数计算
4. 多项式插值

核心思想:
1. 容斥原理处理重复计数问题
2. 生成函数处理组合计数问题
3. 拉格朗日插值进行多项式重建

时间复杂度: 根据具体算法而定
空间复杂度: 根据具体算法而定
"""

class Combinatorics:
    MOD = 1000000007
    
    def __init__(self, n=1000000):
        """初始化阶乘和逆元阶乘数组"""
        self.fact = [1] * (n + 1)
        self.ifact = [1] * (n + 1)
        for i in range(1, n + 1):
            self.fact[i] = self.fact[i - 1] * i % self.MOD
        self.ifact[n] = self.mod_inverse(self.fact[n], self.MOD)
        for i in range(n - 1, -1, -1):
            self.ifact[i] = self.ifact[i + 1] * (i + 1) % self.MOD
    
    def comb(self, n, k):
        """
        计算组合数 C(n, k)
        :param n: 总数
        :param k: 选择数
        :return: 组合数
        """
        if k < 0 or k > n:
            return 0
        return self.fact[n] * self.ifact[k] % self.MOD * self.ifact[n - k] % self.MOD
    
    def inclusion_exclusion(self, sets):
        """
        容斥原理实现
        :param sets: 集合列表
        :return: 并集大小
        """
        result = 0
        n = len(sets)
        
        # 枚举所有子集
        for mask in range(1, 1 << n):
            intersection = set()
            first = True
            count = 0
            
            # 计算交集
            for i in range(n):
                if mask & (1 << i):
                    count += 1
                    if first:
                        intersection = set(sets[i])
                        first = False
                    else:
                        intersection &= set(sets[i])
            
            # 根据容斥原理加减
            if count % 2 == 1:
                result = (result + len(intersection)) % self.MOD
            else:
                result = (result - len(intersection) + self.MOD) % self.MOD
        
        return result
    
    def ogf(self, coefficients, x):
        """
        普通生成函数 (OGF)
        :param coefficients: 系数数组
        :param x: 变量值
        :return: 生成函数值
        """
        result = 0
        power = 1
        for i in range(len(coefficients)):
            result = (result + coefficients[i] * power) % self.MOD
            power = power * x % self.MOD
        return result
    
    def egf(self, coefficients, x):
        """
        指数生成函数 (EGF)
        :param coefficients: 系数数组
        :param x: 变量值
        :return: 生成函数值
        """
        result = 0
        power = 1
        for i in range(len(coefficients)):
            result = (result + coefficients[i] * power % self.MOD * self.ifact[i]) % self.MOD
            power = power * x % self.MOD
        return result
    
    def lagrange_interpolation(self, x, y, target):
        """
        拉格朗日插值
        :param x: x坐标数组
        :param y: y坐标数组
        :param target: 目标x值
        :return: 插值结果
        """
        n = len(x)
        result = 0
        
        for i in range(n):
            numerator = 1   # 分子
            denominator = 1 # 分母
            
            for j in range(n):
                if i != j:
                    numerator = numerator * (target - x[j] + self.MOD) % self.MOD
                    denominator = denominator * (x[i] - x[j] + self.MOD) % self.MOD
            
            term = y[i] * numerator % self.MOD * self.mod_inverse(denominator, self.MOD) % self.MOD
            result = (result + term) % self.MOD
        
        return result
    
    def polynomial_reconstruction(self, points):
        """
        多点插值重建多项式
        :param points: 点集数组 [[x1,y1], [x2,y2], ...]
        :return: 多项式系数数组
        """
        n = len(points)
        result = [0] * n
        
        # 使用拉格朗日插值法重建多项式
        for i in range(n):
            x = [point[0] for point in points]
            y = [point[1] for point in points]
            
            # 计算第i项系数
            coefficient = 0
            for j in range(n):
                if j != i:
                    numerator = 1
                    denominator = 1
                    
                    for k in range(n):
                        if k != i:
                            numerator = numerator * (0 - x[k] + self.MOD) % self.MOD
                            if k != j:
                                denominator = denominator * (x[j] - x[k] + self.MOD) % self.MOD
                    
                    term = y[j] * numerator % self.MOD * self.mod_inverse(denominator, self.MOD) % self.MOD
                    coefficient = (coefficient + term) % self.MOD
            
            result[i] = coefficient
        
        return result
    
    @staticmethod
    def pow_mod(base, exp, mod):
        """快速幂运算"""
        result = 1
        base %= mod
        while exp > 0:
            if exp & 1:
                result = result * base % mod
            base = base * base % mod
            exp >>= 1
        return result
    
    @staticmethod
    def mod_inverse(a, mod):
        """模逆元"""
        return Combinatorics.pow_mod(a, mod - 2, mod)

# 测试用例
if __name__ == "__main__":
    # 创建组合数学工具实例
    combinatorics = Combinatorics(100)
    
    # 测试组合数计算
    print("C(10, 3) =", combinatorics.comb(10, 3))
    
    # 测试容斥原理
    sets = [{1, 2, 3, 4}, {3, 4, 5, 6}, {5, 6, 7, 8}]
    print("Inclusion-Exclusion result:", combinatorics.inclusion_exclusion(sets))
    
    # 测试拉格朗日插值
    x = [1, 2, 3, 4]
    y = [1, 4, 9, 16]  # y = x^2
    target = 5
    interpolated = combinatorics.lagrange_interpolation(x, y, target)
    print("Lagrange interpolation result:", interpolated)