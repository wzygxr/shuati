import math

"""
牛客网 NC15277 区间异或和
题目要求：区间异或操作，单点查询
核心技巧：分块标记 - 对完整的块进行标记，对不完整的块进行暴力修改
时间复杂度：O(√n) / 操作
空间复杂度：O(n)
测试链接：https://ac.nowcoder.com/acm/problem/15277

算法思想详解：
1. 将数组分成大小为√n的块
2. 对于区间异或操作：
   - 对于完全包含在区间内的块，更新块标记（lazy标记）
   - 对于不完整的块，暴力更新每个元素的值
3. 对于单点查询：
   - 计算该元素所在块的标记异或上原始值
   - 返回最终结果

Python优化说明：
- 使用列表存储数组和块标记
- 实现高效的区间操作和查询函数
- 添加性能优化和边界检查
"""

class BlockXOR:
    """分块处理区间异或操作的类"""
    
    def __init__(self, array):
        """初始化数据结构
        
        Args:
            array: 输入数组
        """
        self.n = len(array)
        self.arr = array.copy()  # 复制原始数组
        self.block_size = int(math.sqrt(self.n)) + 1
        self.block_count = (self.n + self.block_size - 1) // self.block_size  # 向上取整
        self.block = [0] * self.block_count  # 初始化块标记数组
        self.operation_count = 0  # 记录操作次数，用于优化
        
    def xor_range(self, l, r, val):
        """区间异或操作
        
        Args:
            l: 左边界（包含，0-based）
            r: 右边界（包含，0-based）
            val: 异或的值
        """
        # 确保l <= r
        if l > r:
            l, r = r, l
        
        left_block = l // self.block_size
        right_block = r // self.block_size
        
        # 如果在同一个块内，直接暴力修改
        if left_block == right_block:
            for i in range(l, r + 1):
                self.arr[i] ^= val
            return
        
        # 处理左边不完整块
        for i in range(l, (left_block + 1) * self.block_size):
            self.arr[i] ^= val
        
        # 处理中间的完整块
        for i in range(left_block + 1, right_block):
            self.block[i] ^= val
        
        # 处理右边不完整块
        for i in range(right_block * self.block_size, r + 1):
            self.arr[i] ^= val
        
        # 更新操作计数
        self.operation_count += 1
        
        # 定期重置块标记，防止性能退化
        if self.operation_count % 1000 == 0:
            self._reset_blocks()
    
    def query(self, index):
        """单点查询
        
        Args:
            index: 查询的位置（0-based）
            
        Returns:
            查询位置的最终值
        """
        if not 0 <= index < self.n:
            raise IndexError("索引越界")
        
        block_id = index // self.block_size
        return self.arr[index] ^ self.block[block_id]
    
    def get_array(self):
        """获取完整的数组内容（考虑块标记的影响）
        
        Returns:
            处理后的完整数组
        """
        result = [0] * self.n
        for i in range(self.n):
            result[i] = self.query(i)
        return result
    
    def _reset_blocks(self):
        """重置所有块标记
        将块标记应用到原始数组，然后重置标记
        """
        # 先将块标记应用到原始数组
        for i in range(self.n):
            block_id = i // self.block_size
            self.arr[i] ^= self.block[block_id]
        
        # 重置所有块标记
        self.block = [0] * self.block_count
        self.operation_count = 0
    
    def __str__(self):
        """返回对象的字符串表示"""
        return f"BlockXOR(n={self.n}, block_size={self.block_size})"


def test_block_xor():
    """测试函数，演示基本功能"""
    print("请输入数组长度：")
    n = int(input())
    
    print("请输入数组元素：")
    array = list(map(int, input().split()))
    
    solution = BlockXOR(array)
    
    print("请输入操作数量：")
    q = int(input())
    
    print("操作格式：1 l r val (区间异或) 或 2 index (单点查询)")
    for _ in range(q):
        parts = input().split()
        op = int(parts[0])
        
        if op == 1:
            # 区间异或操作
            l = int(parts[1]) - 1  # 转换为0-based索引
            r = int(parts[2]) - 1
            val = int(parts[3])
            solution.xor_range(l, r, val)
            print("区间异或操作完成")
            
        elif op == 2:
            # 单点查询
            index = int(parts[1]) - 1  # 转换为0-based索引
            try:
                result = solution.query(index)
                print(f"查询结果：{result}")
            except IndexError as e:
                print(f"错误：{e}")


def performance_test():
    """性能测试函数"""
    import random
    
    SIZE = 100000
    print(f"生成大小为{SIZE}的随机数组...")
    large_array = list(range(SIZE))
    
    solution = BlockXOR(large_array)
    
    print("执行1000次随机区间操作...")
    for i in range(1000):
        l = random.randint(0, SIZE - 1)
        r = random.randint(0, SIZE - 1)
        val = random.randint(0, 99)
        solution.xor_range(l, r, val)
    
    print("执行100次随机查询并显示前10个结果...")
    for i in range(100):
        index = random.randint(0, SIZE - 1)
        result = solution.query(index)
        if i < 10:
            print(f"索引 {index} 的值: {result}")
    
    print("性能测试完成")


def run_demo():
    """运行演示"""
    print("1. 基本功能测试")
    print("2. 性能测试")
    print("请选择测试类型：")
    
    choice = input().strip()
    
    if choice == '1':
        test_block_xor()
    elif choice == '2':
        performance_test()
    else:
        print("无效选择，运行基本功能测试")
        test_block_xor()


if __name__ == "__main__":
    # 简单示例演示
    print("=== 区间异或和分块算法演示 ===")
    print("示例：")
    
    # 创建一个简单的示例
    example_array = [1, 2, 3, 4, 5, 6, 7, 8]
    print(f"原始数组: {example_array}")
    
    solution = BlockXOR(example_array)
    
    # 执行一些操作
    solution.xor_range(1, 5, 3)  # 索引1到5异或3
    print("对索引1-5（值2-6）异或3后：")
    
    # 显示每个元素的值
    for i in range(len(example_array)):
        print(f"索引 {i} 的值: {solution.query(i)}")
    
    # 运行交互式测试
    print("\n" + "="*50)
    run_demo()


"""
Python特定优化分析：
1. 使用列表.copy()方法创建数组副本，避免引用问题
2. 实现了__str__方法，方便调试
3. 添加了边界检查，提高代码健壮性
4. 使用异常处理机制，增强用户体验
5. 定期重置块标记，防止标记累积导致的潜在性能问题

时间复杂度分析：
- 区间操作：O(√n)
  - 两个不完整块各O(√n)个元素
  - O(√n)个完整块，每个块O(1)操作
- 单点查询：O(1)
  - 一次索引计算和一次异或操作

空间复杂度分析：
- O(n) 用于存储原始数组
- O(√n) 用于存储块标记
- 总体空间复杂度：O(n)

Python语言特性利用：
1. 列表推导式：可以用于高效创建数组
2. 异常处理：用于处理边界情况
3. 类封装：将算法封装为可重用的类
4. 文档字符串：提供详细的函数说明和使用示例
"""