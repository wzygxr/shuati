"""
二维差分数组算法实现 - 洛谷P3397 地毯问题 - Python版本

问题描述：
在n×n的格子上有m个地毯，给出这些地毯的信息，问每个点被多少个地毯覆盖。

核心思想：
1. 利用二维差分数组处理区间更新操作
2. 对每个地毯覆盖区域，在差分数组中进行O(1)标记
3. 通过二维前缀和还原差分数组得到最终结果

算法详解：
1. 差分标记：对区域[(a,b),(c,d)]增加k，在差分数组中标记：
   - diff[a][b] += k
   - diff[c+1][b] -= k
   - diff[a][d+1] -= k
   - diff[c+1][d+1] += k
2. 前缀和还原：通过二维前缀和将差分数组还原为结果数组

时间复杂度分析：
1. 差分标记：O(m)，m为地毯数量
2. 前缀和还原：O(n²)，n为网格边长
3. 总体复杂度：O(m + n²)

空间复杂度分析：
O(n²)，用于存储差分数组

算法优势：
1. 区间更新效率高，每次操作O(1)
2. 适合处理大量区间更新操作
3. 空间效率高，复用同一数组

工程化考虑：
1. 输入输出优化：使用sys.stdin提高效率
2. 内存管理：使用列表推导式创建数组
3. 边界处理：扩展数组边界避免特殊判断

应用场景：
1. 图像处理中的区域操作
2. 游戏开发中的区域影响计算
3. 地理信息系统中的区域统计

相关题目：
1. 洛谷 P3397 地毯
2. LeetCode 2132. 用邮票贴满网格图
3. Codeforces 835C - Star sky

测试链接 : https://www.luogu.com.cn/problem/P3397

Python语言特性：
1. 使用列表推导式简化代码
2. 动态类型，开发效率高
3. 内置测试框架支持
"""

class DiffMatrixSolver:
    """
    二维差分数组解决方案类
    """
    
    def __init__(self, n):
        """
        构造函数：初始化差分数组
        
        设计思路：
        1. 创建(n+2)×(n+2)的二维列表
        2. 初始化为0，避免未定义行为
        3. 扩展边界简化索引处理
        
        :param n: 网格大小
        """
        self.n = n
        # 创建(n+2)×(n+2)的差分数组，初始化为0
        self.diff = [[0] * (n + 2) for _ in range(n + 2)]
    
    def add(self, a, b, c, d, k):
        """
        在二维差分数组中标记区域更新
        
        算法原理：
        对区域[(a,b),(c,d)]增加k，在差分数组中进行标记：
        1. diff[a][b] += k      // 左上角标记+k
        2. diff[c+1][b] -= k    // 右上角右侧标记-k
        3. diff[a][d+1] -= k    // 左下角下方标记-k
        4. diff[c+1][d+1] += k  // 右下角标记+k，补偿多减的部分
        
        时间复杂度：O(1)
        空间复杂度：O(1)
        
        :param a: 区域左上角行索引
        :param b: 区域左上角列索引
        :param c: 区域右下角行索引
        :param d: 区域右下角列索引
        :param k: 增加的值
        """
        # 参数校验：确保坐标在有效范围内
        if not (1 <= a <= self.n and 1 <= b <= self.n and 
                1 <= c <= self.n and 1 <= d <= self.n):
            print(f"错误：坐标越界 ({a},{b},{c},{d})")
            return
        
        if a > c or b > d:
            print(f"错误：坐标无效 ({a},{b},{c},{d})")
            return
        
        # 差分标记操作
        self.diff[a][b] += k
        self.diff[c + 1][b] -= k
        self.diff[a][d + 1] -= k
        self.diff[c + 1][d + 1] += k
    
    def build(self):
        """
        通过二维前缀和还原差分数组
        
        算法原理：
        利用容斥原理将差分数组还原为结果数组：
        diff[i][j] += diff[i-1][j] + diff[i][j-1] - diff[i-1][j-1]
        
        时间复杂度：O(n²)
        空间复杂度：O(1)（原地更新）
        """
        for i in range(1, self.n + 1):
            for j in range(1, self.n + 1):
                self.diff[i][j] += (self.diff[i - 1][j] + 
                                  self.diff[i][j - 1] - 
                                  self.diff[i - 1][j - 1])
    
    def clear(self):
        """
        清空差分数组
        
        工程化考虑：
        1. 避免重复分配内存
        2. 重置数组状态，为下一次计算做准备
        
        时间复杂度：O(n²)
        空间复杂度：O(1)
        """
        for i in range(1, self.n + 2):
            for j in range(1, self.n + 2):
                self.diff[i][j] = 0
    
    def get(self, i, j):
        """
        获取指定位置的最终值
        
        :param i: 行索引
        :param j: 列索引
        :return: 该位置的值
        """
        if not (1 <= i <= self.n and 1 <= j <= self.n):
            return 0
        return self.diff[i][j]
    
    def print_result(self):
        """
        打印结果矩阵
        
        用于调试和验证结果
        """
        for i in range(1, self.n + 1):
            line = [str(self.diff[i][j]) for j in range(1, self.n + 1)]
            print(" ".join(line))


def test_normal_case():
    """测试用例1：正常情况 - 洛谷P3397样例"""
    print("=== 测试用例1：正常情况 ===")
    n1 = 5
    solver1 = DiffMatrixSolver(n1)
    
    # 添加三个地毯
    solver1.add(2, 2, 3, 3, 1)  # 地毯1：区域[2,2,3,3]
    solver1.add(3, 3, 5, 5, 1)  # 地毯2：区域[3,3,5,5]
    solver1.add(1, 2, 1, 4, 1)  # 地毯3：区域[1,2,1,4]
    
    solver1.build()
    solver1.print_result()
    print()


def test_edge_case_1():
    """测试用例2：边界情况 - 单个地毯覆盖整个网格"""
    print("=== 测试用例2：边界情况 ===")
    n2 = 3
    solver2 = DiffMatrixSolver(n2)
    
    solver2.add(1, 1, 3, 3, 1)  # 地毯覆盖整个3×3网格
    solver2.build()
    solver2.print_result()
    print()


def test_edge_case_2():
    """测试用例3：边界情况 - 多个地毯重叠"""
    print("=== 测试用例3：重叠情况 ===")
    n3 = 4
    solver3 = DiffMatrixSolver(n3)
    
    solver3.add(1, 1, 3, 3, 1)  # 地毯1
    solver3.add(2, 2, 4, 4, 1)  # 地毯2
    solver3.add(1, 1, 4, 4, 1)  # 地毯3（覆盖整个网格）
    
    solver3.build()
    solver3.print_result()
    print()


def test_performance():
    """测试用例4：性能测试 - 大规模数据"""
    print("=== 测试用例4：性能测试 ===")
    import time
    import random
    
    n4 = 100
    k4 = 1000
    solver4 = DiffMatrixSolver(n4)
    
    # 生成随机地毯数据
    random.seed(42)  # 固定随机种子确保结果可重现
    for _ in range(k4):
        a = random.randint(1, n4)
        b = random.randint(1, n4)
        c = min(a + random.randint(0, 10), n4)
        d = min(b + random.randint(0, 10), n4)
        solver4.add(a, b, c, d, 1)
    
    start_time = time.time()
    solver4.build()
    end_time = time.time()
    
    print(f"网格大小: {n4}×{n4}")
    print(f"地毯数量: {k4}")
    print(f"计算耗时: {(end_time - start_time) * 1000:.2f}ms")
    print()


def test_exception_case():
    """测试用例5：异常情况测试"""
    print("=== 测试用例5：异常情况测试 ===")
    n5 = 5
    solver5 = DiffMatrixSolver(n5)
    
    # 测试越界坐标
    solver5.add(0, 1, 3, 3, 1)  # 行索引越界
    solver5.add(1, 0, 3, 3, 1)  # 列索引越界
    solver5.add(1, 1, 6, 3, 1)  # 行索引越界
    solver5.add(1, 1, 3, 6, 1)  # 列索引越界
    
    # 测试无效坐标
    solver5.add(3, 3, 1, 1, 1)  # 左上角在右下角之后
    
    solver5.build()
    solver5.print_result()
    print()


def main():
    """主函数：执行所有测试用例"""
    try:
        test_normal_case()
        test_edge_case_1()
        test_edge_case_2()
        test_performance()
        test_exception_case()
        print("所有测试用例执行完成！")
    except Exception as e:
        print(f"测试过程中出现异常: {e}")


if __name__ == "__main__":
    main()