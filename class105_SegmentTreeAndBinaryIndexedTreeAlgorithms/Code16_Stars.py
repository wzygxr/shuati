# POJ 2352 Stars
# 题目描述：给定N个星星的坐标(x,y)，满足y坐标升序排列，若y相同则x升序排列。
# 每个星星的等级是它左下角区域内星星的数量（即x坐标≤其x，y坐标≤其y的星星数目，不包括自身）。
# 输出等级为0到N-1的星星数目。
# 题目链接：http://poj.org/problem?id=2352
# 解题思路：树状数组 + 离散化

class Code16_Stars:
    """
    使用树状数组解决Stars问题
    
    时间复杂度：O(N log N)
    空间复杂度：O(max_x)
    
    本题特点：
    1. 由于输入是按y升序排列的，所以对于每个星星来说，之前处理过的星星的y坐标都不超过它的y坐标
    2. 因此我们只需要统计之前处理过的星星中x坐标小于等于当前星星x坐标的数量
    3. 这可以通过树状数组高效实现，每次查询前缀和，然后更新树状数组
    """
    
    def __init__(self, max_x_value):
        """
        初始化树状数组和结果数组
        :param max_x_value: 最大x坐标值
        """
        # 初始化树状数组和结果数组
        # 注意：这里max_x_value+1是因为树状数组下标从1开始
        self.max_x = max_x_value
        self.bit = [0] * (self.max_x + 2)  # +2 防止溢出
        self.result = [0] * (self.max_x + 1)  # 等级最多为max_x
    
    def lowbit(self, x):
        """
        lowbit操作，获取x二进制表示中最低位的1所对应的值
        :param x: 输入整数
        :return: 最低位的1对应的值
        """
        return x & (-x)
    
    def update(self, x, val):
        """
        更新树状数组
        :param x: 要更新的位置（1-based）
        :param val: 要增加的值
        """
        while x <= self.max_x:
            self.bit[x] += val
            x += self.lowbit(x)
    
    def query(self, x):
        """
        查询前缀和，即1到x的累加和
        :param x: 查询上限（1-based）
        :return: 前缀和
        """
        sum_val = 0
        while x > 0:
            sum_val += self.bit[x]
            x -= self.lowbit(x)
        return sum_val
    
    def process_stars(self, stars):
        """
        处理星星数据，计算每个星星的等级
        :param stars: 星星坐标数组
        :return: 等级统计结果，result[i]表示等级为i的星星数目
        """
        # 统计每个星星的等级
        for x, y in stars:
            # 由于树状数组索引从1开始，我们将x坐标+1
            # 计算当前星星的等级：查询小于等于x的星星数量
            level = self.query(x + 1)  # 转换为1-based索引
            
            # 更新等级统计
            self.result[level] += 1
            
            # 将当前星星加入树状数组
            self.update(x + 1, 1)  # 转换为1-based索引
        
        return self.result
    
    def process_stars_with_discretization(self, stars):
        """
        处理星星数据（带离散化）
        当x坐标范围很大时使用离散化可以节省空间
        :param stars: 星星坐标数组
        :return: 等级统计结果
        """
        # 提取所有x坐标用于离散化
        xs = [star[0] for star in stars]
        
        # 离散化处理
        coordinate_mapping = self.discretize(xs)
        
        # 重置树状数组为离散化后的大小
        self.max_x = len(coordinate_mapping)
        self.bit = [0] * (self.max_x + 2)  # +2 防止溢出
        self.result = [0] * len(stars)  # 重置结果数组
        
        # 处理星星数据
        for x, y in stars:
            # 获取离散化后的值（从1开始）
            discretized_x = coordinate_mapping[x] + 1
            
            # 计算当前星星的等级
            level = self.query(discretized_x)
            
            # 更新等级统计
            self.result[level] += 1
            
            # 将当前星星加入树状数组
            self.update(discretized_x, 1)
        
        return self.result
    
    def discretize(self, nums):
        """
        离散化处理
        :param nums: 原始数据数组
        :return: 原始值到离散化值的映射
        """
        # 复制并去重
        unique_nums = list(set(nums))
        
        # 排序
        unique_nums.sort()
        
        # 构建映射
        mapping = {}
        for i, num in enumerate(unique_nums):
            mapping[num] = i  # 从0开始的离散化值
        
        return mapping
    
    @staticmethod
    def print_result(result, n):
        """
        打印结果
        :param result: 等级统计结果
        :param n: 星星数量
        """
        for i in range(n):
            print(result[i])

# 测试函数
def test():
    print("=== 测试用例1（无需离散化）===")
    # 测试用例1：简单示例
    stars1 = [
        (1, 1),
        (2, 2),
        (3, 3),
        (1, 3),
        (2, 1)
    ]
    
    # 找出最大的x坐标
    max_x1 = max(star[0] for star in stars1)
    
    solver1 = Code16_Stars(max_x1)
    result1 = solver1.process_stars(stars1)
    Code16_Stars.print_result(result1, len(stars1))
    
    print("\n=== 测试用例2（使用离散化）===")
    # 测试用例2：使用离散化
    stars2 = [
        (10000, 1),
        (20000, 2),
        (5000, 3),
        (10000, 3),
        (20000, 1)
    ]
    
    solver2 = Code16_Stars(20000)  # 初始值不重要，会在离散化时重置
    result2 = solver2.process_stars_with_discretization(stars2)
    Code16_Stars.print_result(result2, len(stars2))
    
    print("\n=== 测试用例3（所有星星在同一点）===")
    # 测试用例3：边界情况 - 所有星星在同一点
    stars3 = [
        (1, 1),
        (1, 1),
        (1, 1)
    ]
    
    max_x3 = 1
    solver3 = Code16_Stars(max_x3)
    result3 = solver3.process_stars(stars3)
    Code16_Stars.print_result(result3, len(stars3))

# 性能测试
def performance_test():
    print("\n=== 性能测试 ===")
    import random
    import time
    
    # 生成测试数据
    n = 100000
    stars = []
    # 生成随机坐标，保持y升序排列
    for i in range(n):
        y = i // 100  # 保证y升序
        x = random.randint(1, 1000000)
        stars.append((x, y))
    
    # 确保数据按y升序排列，相同y时按x升序排列
    stars.sort(key=lambda p: (p[1], p[0]))
    
    # 测试普通方法
    print(f"测试处理{len(stars)}个星星的数据...")
    start_time = time.time()
    
    # 找出最大的x坐标
    max_x = max(star[0] for star in stars)
    solver = Code16_Stars(max_x)
    result = solver.process_stars(stars)
    
    normal_time = time.time() - start_time
    print(f"普通方法耗时: {normal_time:.6f}秒")
    
    # 测试离散化方法
    start_time = time.time()
    solver = Code16_Stars(max_x)  # 初始值不重要
    result_dis = solver.process_stars_with_discretization(stars)
    
    disc_time = time.time() - start_time
    print(f"离散化方法耗时: {disc_time:.6f}秒")
    
    # 验证结果是否一致
    is_consistent = True
    for i in range(n):
        if result[i] != result_dis[i]:
            is_consistent = False
            break
    print(f"结果一致性验证: {'通过' if is_consistent else '失败'}")

if __name__ == "__main__":
    # 运行测试
    test()
    
    # 运行性能测试
    performance_test()
    
    # 实际输入处理
    print("\n=== 输入测试（输入N和N个坐标）===")
    try:
        n = int(input("请输入星星数量N: "))
        stars = []
        max_x = 0
        for i in range(n):
            x, y = map(int, input(f"请输入第{i+1}个星星的坐标(x y): ").split())
            stars.append((x, y))
            max_x = max(max_x, x)
        
        # 处理输入数据
        solver = Code16_Stars(max_x)
        result = solver.process_stars(stars)
        
        # 输出结果
        print("\n输出结果：")
        Code16_Stars.print_result(result, n)
    except Exception as e:
        print(f"输入错误: {e}")

'''
算法总结：

1. 本题的关键洞察：
   - 由于输入的星星是按y坐标升序排列的，所以处理每个星星时，所有已处理的星星的y坐标都不大于当前星星的y坐标
   - 因此，当前星星的等级就是已处理星星中x坐标小于等于当前星星x坐标的数量
   - 这可以通过树状数组高效地进行前缀和查询和单点更新

2. 离散化的必要性：
   - 当x坐标范围很大时（比如到1e9），直接使用树状数组会导致空间浪费
   - 离散化可以将所有不同的x坐标映射到较小的连续整数范围，节省空间
   - 在本题中，如果x坐标范围不大，可以不使用离散化

3. 树状数组操作：
   - update(x, val): 在位置x增加val
   - query(x): 查询前缀和[1,x]
   - lowbit(x): 获取x二进制表示中最低位的1

4. 时间复杂度分析：
   - 树状数组的update和query操作都是O(log M)，其中M是最大x坐标值（或离散化后的坐标范围）
   - 处理n个星星的总时间复杂度为O(n log M)
   - 离散化的时间复杂度为O(n log n)
   - 因此总时间复杂度为O(n log n)

5. 空间复杂度：
   - 不使用离散化：O(M)，其中M是最大x坐标值
   - 使用离散化：O(n)，只需存储不同的x坐标

6. Python实现注意事项：
   - 在Python中，列表索引从0开始，但树状数组逻辑上从1开始，因此需要进行适当的转换
   - 对于大规模数据，离散化可以显著提高内存效率
   - 使用集合（set）进行去重操作时要注意顺序，需要重新排序以保持一致性
'''