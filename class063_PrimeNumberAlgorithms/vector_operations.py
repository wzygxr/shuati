# 向量运算的Python实现
# 包括：点积、叉积、线性相关性判定
# 时间复杂度：
# - 点积/叉积：O(n)
# - 线性相关性判定：O(n³)
# 空间复杂度：O(n²)

import math
import numpy as np

class Vector3D:
    """
    三维向量类，支持基本的向量运算
    时间复杂度：大多数操作O(1)
    空间复杂度：O(1)
    """
    def __init__(self, x: float = 0.0, y: float = 0.0, z: float = 0.0):
        self.x = x
        self.y = y
        self.z = z
    
    def __add__(self, other):
        """向量加法"""
        return Vector3D(self.x + other.x, self.y + other.y, self.z + other.z)
    
    def __sub__(self, other):
        """向量减法"""
        return Vector3D(self.x - other.x, self.y - other.y, self.z - other.z)
    
    def __mul__(self, scalar):
        """标量乘法"""
        return Vector3D(self.x * scalar, self.y * scalar, self.z * scalar)
    
    def __rmul__(self, scalar):
        """支持 scalar * vector 的形式"""
        return self * scalar
    
    def length(self):
        """计算向量长度"""
        return math.sqrt(self.x**2 + self.y**2 + self.z**2)
    
    def normalize(self):
        """返回单位向量"""
        len_val = self.length()
        if abs(len_val) < 1e-9:
            return Vector3D()  # 避免除以零
        return Vector3D(self.x/len_val, self.y/len_val, self.z/len_val)
    
    def __str__(self):
        """字符串表示"""
        return f"({self.x}, {self.y}, {self.z})"
    
    def __repr__(self):
        """官方字符串表示"""
        return self.__str__()

def dot_product_3d(a, b):
    """
    计算三维向量的点积
    时间复杂度：O(1)
    空间复杂度：O(1)
    """
    return a.x * b.x + a.y * b.y + a.z * b.z

def cross_product_3d(a, b):
    """
    计算三维向量的叉积
    时间复杂度：O(1)
    空间复杂度：O(1)
    """
    return Vector3D(
        a.y * b.z - a.z * b.y,
        a.z * b.x - a.x * b.z,
        a.x * b.y - a.y * b.x
    )

def angle_between_3d(a, b):
    """
    计算两个三维向量之间的夹角（弧度）
    时间复杂度：O(1)
    空间复杂度：O(1)
    """
    dot = dot_product_3d(a, b)
    len_product = a.length() * b.length()
    if abs(len_product) < 1e-9:
        return 0.0
    cos_theta = dot / len_product
    # 确保cos_theta在[-1, 1]范围内
    cos_theta = max(min(cos_theta, 1.0), -1.0)
    return math.acos(cos_theta)

class Vector:
    """
    通用向量类，支持任意维度的向量运算
    时间复杂度：大多数操作O(n)
    空间复杂度：O(n)
    """
    def __init__(self, data=None):
        if data is None:
            self.data = []
        elif isinstance(data, int):
            self.data = [0.0] * data
        else:
            self.data = list(data)
    
    def dimension(self):
        """返回向量维度"""
        return len(self.data)
    
    def __getitem__(self, index):
        """访问向量元素"""
        return self.data[index]
    
    def __setitem__(self, index, value):
        """设置向量元素"""
        self.data[index] = value
    
    def __add__(self, other):
        """向量加法"""
        if self.dimension() != other.dimension():
            raise ValueError("向量维度不匹配")
        result = Vector(self.dimension())
        for i in range(self.dimension()):
            result[i] = self[i] + other[i]
        return result
    
    def __sub__(self, other):
        """向量减法"""
        if self.dimension() != other.dimension():
            raise ValueError("向量维度不匹配")
        result = Vector(self.dimension())
        for i in range(self.dimension()):
            result[i] = self[i] - other[i]
        return result
    
    def __mul__(self, scalar):
        """标量乘法"""
        result = Vector(self.dimension())
        for i in range(self.dimension()):
            result[i] = self[i] * scalar
        return result
    
    def __rmul__(self, scalar):
        """支持 scalar * vector 的形式"""
        return self * scalar
    
    def length(self):
        """计算向量长度"""
        return math.sqrt(sum(x*x for x in self.data))
    
    def __str__(self):
        """字符串表示"""
        return "(" + ", ".join(str(x) for x in self.data) + ")"
    
    def __repr__(self):
        """官方字符串表示"""
        return self.__str__()

def dot_product(a, b):
    """
    计算两个任意维度向量的点积
    时间复杂度：O(n)
    空间复杂度：O(1)
    """
    if a.dimension() != b.dimension():
        raise ValueError("向量维度不匹配")
    return sum(a[i] * b[i] for i in range(a.dimension()))

def is_linearly_dependent(vectors):
    """
    高斯消元法判断向量组的线性相关性
    时间复杂度：O(m²n)，其中m是向量数量，n是向量维度
    空间复杂度：O(mn)
    """
    if not vectors:
        return False
    
    m = len(vectors)      # 向量数量
    n = vectors[0].dimension()  # 向量维度
    
    # 确保所有向量维度相同
    for v in vectors:
        if v.dimension() != n:
            raise ValueError("向量维度不一致")
    
    # 构造增广矩阵进行高斯消元
    mat = [[vectors[i][j] for j in range(n)] for i in range(m)]
    
    rank = 0
    for col in range(n):
        if rank >= m:
            break
        
        # 寻找主元
        pivot = rank
        for i in range(rank, m):
            if abs(mat[i][col]) > abs(mat[pivot][col]):
                pivot = i
        
        # 如果主元为零，继续下一列
        if abs(mat[pivot][col]) < 1e-9:
            continue
        
        # 交换行
        mat[rank], mat[pivot] = mat[pivot], mat[rank]
        
        # 归一化主行
        div = mat[rank][col]
        for j in range(col, n):
            mat[rank][j] /= div
        
        # 消去其他行
        for i in range(m):
            if i != rank and abs(mat[i][col]) > 1e-9:
                factor = mat[i][col]
                for j in range(col, n):
                    mat[i][j] -= factor * mat[rank][j]
        
        rank += 1
    
    # 如果秩小于向量数量，则线性相关
    return rank < m

def are_collinear(a, b, c):
    """
    判断三个三维点是否共线
    时间复杂度：O(1)
    空间复杂度：O(1)
    """
    ab = b - a
    ac = c - a
    cross = cross_product_3d(ab, ac)
    # 如果叉积的长度接近零，则三点共线
    return abs(cross.length()) < 1e-9

def are_coplanar(a, b, c, d):
    """
    判断四个三维点是否共面
    时间复杂度：O(1)
    空间复杂度：O(1)
    """
    ab = b - a
    ac = c - a
    ad = d - a
    # 计算混合积，若为零则共面
    triple_product = dot_product_3d(ab, cross_product_3d(ac, ad))
    return abs(triple_product) < 1e-9

# 力扣第1232题：缀点成线
def check_straight_line(coordinates):
    """
    检查所有点是否共线
    时间复杂度：O(n)
    空间复杂度：O(1)
    """
    if len(coordinates) <= 2:
        return True
    
    # 取前两个点作为基准线
    x0, y0 = coordinates[0]
    x1, y1 = coordinates[1]
    
    # 使用向量叉积判断三点共线
    for i in range(2, len(coordinates)):
        x2, y2 = coordinates[i]
        # 计算 (x1-x0)*(y2-y0) - (y1-y0)*(x2-x0)
        # 如果不为零，则三点不共线
        if (x1 - x0) * (y2 - y0) - (y1 - y0) * (x2 - x0) != 0:
            return False
    
    return True

# 力扣第363题：矩形区域不超过 K 的最大数值和
def max_sum_submatrix(matrix, k):
    """
    计算矩形区域不超过 K 的最大数值和
    时间复杂度：O(m²n²)，优化后可以达到O(m²nlogn)
    空间复杂度：O(n)
    """
    if not matrix or not matrix[0]:
        return 0
    
    m, n = len(matrix), len(matrix[0])
    max_sum = -float('inf')
    
    # 使用二维前缀和优化
    prefix_sum = [[0] * (n + 1) for _ in range(m + 1)]
    for i in range(m):
        for j in range(n):
            prefix_sum[i+1][j+1] = matrix[i][j] + prefix_sum[i][j+1] + prefix_sum[i+1][j] - prefix_sum[i][j]
    
    # 枚举所有可能的矩形区域
    for i1 in range(m + 1):
        for i2 in range(i1 + 1, m + 1):
            for j1 in range(n + 1):
                for j2 in range(j1 + 1, n + 1):
                    current_sum = prefix_sum[i2][j2] - prefix_sum[i1][j2] - prefix_sum[i2][j1] + prefix_sum[i1][j1]
                    if current_sum <= k and current_sum > max_sum:
                        max_sum = current_sum
    
    return max_sum

# 主函数 - 测试代码
def main():
    # 测试三维向量运算
    print("=== 三维向量运算测试 ===")
    a = Vector3D(1, 2, 3)
    b = Vector3D(4, 5, 6)
    
    print(f"向量a: {a}")
    print(f"向量b: {b}")
    
    print(f"点积 a·b = {dot_product_3d(a, b)}")
    
    cross = cross_product_3d(a, b)
    print(f"叉积 a×b = {cross}")
    
    angle = angle_between_3d(a, b)
    print(f"夹角 θ = {angle} 弧度 = {angle * 180 / math.pi} 度")
    
    # 测试共线性和共面性
    c = Vector3D(2, 4, 6)  # c = 2a，应该与a和b共面
    print(f"\n点a, b, c共线？ {'是' if are_collinear(a, b, c) else '否'}")
    
    d = Vector3D(7, 8, 9)
    print(f"点a, b, c, d共面？ {'是' if are_coplanar(a, b, c, d) else '否'}")
    
    # 测试线性相关性
    print("\n=== 线性相关性测试 ===")
    
    # 线性相关的向量组
    dependent_vectors = [
        Vector([1, 2, 3]),
        Vector([4, 5, 6]),
        Vector([2, 3, 4])  # 这三个向量线性相关
    ]
    
    print("线性相关向量组:")
    for v in dependent_vectors:
        print(v)
    print(f"线性相关？ {'是' if is_linearly_dependent(dependent_vectors) else '否'}")
    
    # 线性无关的向量组
    independent_vectors = [
        Vector([1, 0, 0]),
        Vector([0, 1, 0]),
        Vector([0, 0, 1])  # 这三个向量线性无关
    ]
    
    print("线性无关向量组:")
    for v in independent_vectors:
        print(v)
    print(f"线性相关？ {'是' if is_linearly_dependent(independent_vectors) else '否'}")
    
    # 测试力扣第1232题
    print("\n=== 力扣第1232题测试 ===")
    coords1 = [[1, 2], [2, 3], [3, 4], [4, 5], [5, 6], [6, 7]]
    coords2 = [[1, 1], [2, 2], [3, 4], [4, 5], [5, 6], [7, 7]]
    
    print(f"示例1: {check_straight_line(coords1)}")  # 应返回True
    print(f"示例2: {check_straight_line(coords2)}")  # 应返回False
    
    # 测试力扣第363题
    print("\n=== 力扣第363题测试 ===")
    matrix = [
        [1, 0, 1],
        [0, -2, 3]
    ]
    k = 2
    print(f"最大矩形和 ≤ {k} 的值: {max_sum_submatrix(matrix, k)}")  # 应返回2
    
    """
    算法解释：
    1. 向量运算包括点积、叉积、线性相关性判定等基本操作
    2. 三维向量有专门的实现，支持常见的几何运算
    3. 通用向量类支持任意维度的向量运算
    4. 线性相关性判定使用高斯消元法计算向量组的秩
    5. 提供了力扣相关题目的解决方案
    
    时间复杂度分析：
    - 点积/叉积：O(n)，其中n是向量维度
    - 线性相关性判定：O(m²n)，其中m是向量数量，n是向量维度
    - 缀点成线：O(n)
    - 矩形区域最大和：O(m²n²)，可优化至O(m²nlogn)
    
    应用场景：
    1. 计算几何中的点、线、面关系判断
    2. 机器学习中的特征向量分析
    3. 物理学中的力、速度、加速度计算
    4. 计算机图形学中的变换和渲染
    5. 图像处理中的卷积运算
    6. 自然语言处理中的词向量运算
    
    相关题目：
    1. LeetCode 1232. Check If It Is a Straight Line - 检查是否为直线
    2. 向量点积、叉积相关问题 - 几何计算
    3. 向量线性相关性问题 - 线性代数
    """

if __name__ == "__main__":
    main()