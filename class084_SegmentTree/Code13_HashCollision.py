# 哈希冲突问题 - 分块算法实现 (Python版本)
# 题目来源: https://www.luogu.com.cn/problem/P3396
# 题目大意: 给定一个长度为n的数组arr，支持两种操作：
# 1. 查询操作 A x y: 查询所有满足 i % x == y 的位置i对应的arr[i]之和
# 2. 更新操作 C x y: 将arr[x]的值更新为y
# 约束条件: 1 <= n、m <= 1.5 * 10^5
# 
# 解题思路:
# 1. 对于x <= sqrt(n)的情况，预处理dp[x][y]的值
# 2. 对于x > sqrt(n)的情况，直接暴力计算
# 3. 更新操作时，同时更新预处理结果
# 
# 时间复杂度分析:
# - 预处理: O(n * sqrt(n))
# - 查询: O(1) 对于x <= sqrt(n)，O(n/x) 对于x > sqrt(n)
# - 更新: O(sqrt(n))
# 
# 空间复杂度: O(n + sqrt(n)^2) = O(n)
# 
# 工程化考量:
# 1. 异常处理: 验证输入参数的有效性
# 2. 性能优化: 使用分块思想平衡预处理和查询的开销
# 3. 边界处理: 处理x=0或y>=x等边界情况
# 4. 内存管理: 合理设置数组大小避免内存溢出

import math

class HashCollision:
    def __init__(self, size):
        """
        构造函数
        :param size: 数组大小
        """
        self.n = size
        # 计算块大小，通常选择sqrt(n)
        self.blen = int(math.sqrt(self.n))
        
        # 初始化数组
        self.arr = [0] * (self.n + 1)
        
        # 初始化dp数组
        # dp[x][y]: 存储所有满足 i % x == y 的位置i对应的arr[i]之和
        self.dp = [[0] * (self.blen + 1) for _ in range(self.blen + 1)]
    
    def set_array(self, values):
        """
        设置数组初始值
        :param values: 初始值数组
        :raises ValueError: 如果初始值数组大小不匹配
        """
        if len(values) != self.n:
            raise ValueError("初始值数组大小不匹配")
        
        for i in range(1, self.n + 1):
            self.arr[i] = values[i - 1]
        
        # 进行预处理
        self.prepare()
    
    def query(self, x, y):
        """
        查询操作 A x y
        查询所有满足 i % x == y 的位置i对应的arr[i]之和
        :param x: 除数，必须大于0
        :param y: 余数，必须满足 0 <= y < x
        :return: 满足条件的位置对应的元素之和
        :raises ValueError: 如果x <= 0 或 y < 0 或 y >= x
        """
        # 参数验证
        if x <= 0:
            raise ValueError("除数x必须大于0")
        if y < 0 or y >= x:
            raise ValueError(f"余数y必须满足0 <= y < x, 但y={y}, x={x}")
        
        # 如果x小于等于块大小，则直接返回预处理结果
        if x <= self.blen:
            return self.dp[x][y]
        
        # 否则暴力计算（适用于x较大的情况）
        ans = 0
        i = y
        while i <= self.n:
            ans += self.arr[i]
            i += x
        return ans
    
    def update(self, i, v):
        """
        更新操作 C x y
        将arr[x]的值更新为y，并更新相关的预处理结果
        :param i: 要更新的位置，必须满足1 <= i <= n
        :param v: 新的值
        :raises ValueError: 如果位置i超出有效范围
        """
        # 参数验证
        if i < 1 or i > self.n:
            raise ValueError("位置i必须在1到n之间")
        
        # 计算值的变化量
        delta = v - self.arr[i]
        # 更新原数组
        self.arr[i] = v
        
        # 更新所有相关的预处理结果
        # 只需要更新x <= sqrt(n)的情况，因为这些被预处理了
        for x in range(1, self.blen + 1):
            self.dp[x][i % x] += delta
    
    def prepare(self):
        """
        预处理函数
        对于所有x <= sqrt(n)的情况，预处理dp[x][y]的值
        """
        # 初始化dp数组为0
        for x in range(1, self.blen + 1):
            for y in range(x):
                self.dp[x][y] = 0
        
        # 对于每个x <= sqrt(n)，计算所有y对应的dp[x][y]值
        for x in range(1, self.blen + 1):
            for i in range(1, self.n + 1):
                # i % x 表示位置i对x取余的结果
                # dp[x][i % x] 累加arr[i]的值
                self.dp[x][i % x] += self.arr[i]
    
    def get_status(self):
        """
        获取数组当前状态
        :return: 数组内容字符串
        """
        result = "数组状态: ["
        for i in range(1, min(self.n, 10) + 1):
            result += str(self.arr[i])
            if i < min(self.n, 10):
                result += ", "
        if self.n > 10:
            result += "..."
        result += "]"
        return result
    
    def get_preprocess_status(self):
        """
        获取预处理状态
        :return: 预处理状态字符串
        """
        result = f"预处理状态 (x <= {self.blen}):\n"
        for x in range(1, min(self.blen, 5) + 1):
            result += f"x={x}: ["
            for y in range(x):
                result += str(self.dp[x][y])
                if y < x - 1:
                    result += ", "
            result += "]\n"
        if self.blen > 5:
            result += "...\n"
        return result

def test_hash_collision():
    """
    单元测试函数
    """
    print("=== 哈希冲突算法单元测试 ===")
    
    # 测试1: 基本功能测试
    print("测试1: 基本功能测试")
    hc = HashCollision(10)
    
    # 设置初始数组
    values = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
    hc.set_array(values)
    
    # 验证查询结果
    result1 = hc.query(3, 0)  # 3 + 6 + 9 = 18
    result2 = hc.query(3, 1)  # 1 + 4 + 7 + 10 = 22
    result3 = hc.query(3, 2)  # 2 + 5 + 8 = 15
    
    if result1 == 18 and result2 == 22 and result3 == 15:
        print("查询测试通过")
    else:
        print("查询测试失败")
    
    # 验证更新结果
    hc.update(5, 50)  # 将位置5的值从5改为50
    result4 = hc.query(3, 1)  # 1 + 4 + 7 + 10 + (50 - 5) = 22 + 45 = 67
    
    if result4 == 67:
        print("更新测试通过")
    else:
        print("更新测试失败")
    
    # 测试2: 异常处理测试
    print("\n测试2: 异常处理测试")
    try:
        hc.query(0, 0)  # 应该抛出异常
        print("异常处理测试失败")
    except ValueError as e:
        print(f"异常处理测试通过: {e}")
    
    print(hc.get_status())
    print(hc.get_preprocess_status())
    
    print("=== 单元测试完成 ===")

def performance_test():
    """
    性能测试函数
    """
    import time
    
    print("=== 哈希冲突算法性能测试 ===")
    
    n = 100000
    m = 10000
    
    hc = HashCollision(n)
    
    # 初始化数组
    values = [i + 1 for i in range(n)]
    hc.set_array(values)
    
    # 测试查询性能
    start_time = time.time()
    
    total = 0
    for i in range(m):
        x = (i % 100) + 1  # x在1-100之间
        y = i % x
        total += hc.query(x, y)
    
    end_time = time.time()
    duration = (end_time - start_time) * 1000  # 转换为毫秒
    
    print("性能测试结果:")
    print(f"数据规模: n={n}, 操作次数: m={m}")
    print(f"总查询时间: {duration:.2f} 毫秒")
    print(f"平均查询时间: {duration / m:.4f} 毫秒/次")
    print(f"查询吞吐量: {m / (duration / 1000):.2f} 次/秒")
    print(f"查询结果总和: {total}")
    
    print("=== 性能测试完成 ===")

if __name__ == "__main__":
    # 运行单元测试
    test_hash_collision()
    
    # 运行性能测试
    performance_test()
    
    # 演示示例
    print("=== 哈希冲突算法演示 ===")
    
    demo = HashCollision(20)
    demo_values = [(i + 1) * 10 for i in range(20)]  # 10, 20, 30, ..., 200
    demo.set_array(demo_values)
    
    print(demo.get_status())
    
    # 演示查询操作
    print("\n查询演示:")
    print(f"查询所有位置为3的倍数的元素之和 (x=3, y=0): {demo.query(3, 0)}")
    print(f"查询所有位置除以4余1的元素之和 (x=4, y=1): {demo.query(4, 1)}")
    print(f"查询所有位置除以5余2的元素之和 (x=5, y=2): {demo.query(5, 2)}")
    
    # 演示更新操作
    print("\n更新演示:")
    print(f"更新前位置5的值: {demo.query(1, 4)}")  # 查询单个位置
    demo.update(5, 999)
    print(f"更新后位置5的值: {demo.query(1, 4)}")
    print(f"更新后所有位置为3的倍数的元素之和: {demo.query(3, 0)}")