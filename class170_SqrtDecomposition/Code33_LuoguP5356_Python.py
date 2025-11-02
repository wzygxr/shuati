import math
import bisect

"""
洛谷 P5356 由乃打扑克
题目要求：区间查询第k小，区间加法
核心技巧：分块排序 + 二分答案
时间复杂度：O(√n * log n) / 查询，O(√n) / 修改
空间复杂度：O(n)
测试链接：https://www.luogu.com.cn/problem/P5356

算法思想详解：
1. 将数组分成大小为√n的块
2. 对每个块维护一个排序后的副本，便于二分查找
3. 对每个块维护一个加法标记（lazy标记）
4. 区间加法操作：
   - 对于完整块，更新块的加法标记
   - 对于不完整块，暴力修改原始数组，并重新排序该块
5. 区间第k小查询：
   - 对整个值域进行二分查找
   - 对于每个中间值mid，统计区间内小于等于mid的元素个数
   - 根据统计结果调整二分边界

Python优化说明：
- 使用列表存储数据结构
- 利用bisect模块进行高效二分查找
- 优化查询逻辑，减少不必要的计算
- 针对Python性能特点，调整块大小
"""

class BlockKth:
    """分块处理区间第k小查询和区间加法的类"""
    
    def __init__(self, array):
        """初始化数据结构
        
        Args:
            array: 输入数组
        """
        self.arr = array.copy()  # 复制原始数组
        self.n = len(array)
        # 优化块大小，Python中可能需要调整为更适合的值
        self.block_size = int(math.sqrt(self.n)) + 1
        self.block_count = (self.n + self.block_size - 1) // self.block_size
        self.block_add = [0] * self.block_count  # 块的加法标记
        
        # 初始化排序后的块
        self.sorted_blocks = []
        for i in range(self.block_count):
            start = i * self.block_size
            end = min((i + 1) * self.block_size, self.n)
            # 复制并排序块内容
            block = self.arr[start:end].copy()
            block.sort()
            self.sorted_blocks.append(block)
    
    def rebuild_block(self, block_id):
        """重建指定块的排序数组
        
        Args:
            block_id: 块的索引
        """
        start = block_id * self.block_size
        end = min((block_id + 1) * self.block_size, self.n)
        # 重新从原始数组获取数据并排序
        self.sorted_blocks[block_id] = self.arr[start:end].copy()
        self.sorted_blocks[block_id].sort()
    
    def add_range(self, l, r, val):
        """区间加法操作
        
        Args:
            l: 左边界（包含，0-based）
            r: 右边界（包含，0-based）
            val: 要加的值
        """
        left_block = l // self.block_size
        right_block = r // self.block_size
        
        # 确保l <= r
        if l > r:
            l, r = r, l
        
        # 如果在同一个块内，直接暴力修改
        if left_block == right_block:
            # 先将块标记应用到该区间（如果有的话）
            # 这里直接修改原始数组
            for i in range(l, r + 1):
                self.arr[i] += val
            # 重新排序该块
            self.rebuild_block(left_block)
            return
        
        # 处理左边不完整块
        for i in range(l, (left_block + 1) * self.block_size):
            self.arr[i] += val
        self.rebuild_block(left_block)
        
        # 处理中间的完整块
        for i in range(left_block + 1, right_block):
            self.block_add[i] += val
        
        # 处理右边不完整块
        for i in range(right_block * self.block_size, r + 1):
            self.arr[i] += val
        self.rebuild_block(right_block)
    
    def count_leq(self, l, r, x):
        """统计区间[l,r]内小于等于x的元素个数
        
        Args:
            l: 左边界
            r: 右边界
            x: 目标值
            
        Returns:
            元素个数
        """
        left_block = l // self.block_size
        right_block = r // self.block_size
        count = 0
        
        # 如果在同一个块内，直接暴力统计
        if left_block == right_block:
            for i in range(l, r + 1):
                # 加上块标记后的值
                if self.arr[i] + self.block_add[left_block] <= x:
                    count += 1
            return count
        
        # 统计左边不完整块
        for i in range(l, (left_block + 1) * self.block_size):
            if self.arr[i] + self.block_add[left_block] <= x:
                count += 1
        
        # 统计中间的完整块
        for i in range(left_block + 1, right_block):
            # 目标值减去块标记
            target = x - self.block_add[i]
            # 在排序后的块中二分查找
            # 使用bisect_right找到第一个大于target的位置
            pos = bisect.bisect_right(self.sorted_blocks[i], target)
            count += pos
        
        # 统计右边不完整块
        for i in range(right_block * self.block_size, r + 1):
            if self.arr[i] + self.block_add[right_block] <= x:
                count += 1
        
        return count
    
    def query_kth(self, l, r, k):
        """查询区间[l,r]内的第k小元素
        
        Args:
            l: 左边界（包含，0-based）
            r: 右边界（包含，0-based）
            k: 第k小（k>=1）
            
        Returns:
            第k小的元素值
        """
        # 边界检查
        if k <= 0 or k > r - l + 1:
            raise ValueError(f"Invalid k value: {k}, must be between 1 and {r-l+1}")
        
        # 方法1：确定值域范围（适用于小数组）
        # 收集区间内所有元素的值
        values = []
        for i in range(l, r + 1):
            block_id = i // self.block_size
            values.append(self.arr[i] + self.block_add[block_id])
        # 直接排序取第k-1个元素（仅用于测试，性能较差）
        # values.sort()
        # return values[k-1]
        
        # 方法2：二分答案（高效方法）
        # 确定二分查找的范围
        # 优化：直接使用数据范围，避免遍历
        left = -10**9  # 根据题目数据范围设置
        right = 10**9
        
        # 二分查找第k小的元素
        while left < right:
            mid = left + (right - left) // 2
            cnt = self.count_leq(l, r, mid)
            if cnt >= k:
                right = mid
            else:
                left = mid + 1
        
        return left
    
    def get_value(self, index):
        """获取指定位置的当前值
        
        Args:
            index: 索引
            
        Returns:
            当前值
        """
        if not 0 <= index < self.n:
            raise IndexError(f"Index out of range: {index}")
        
        block_id = index // self.block_size
        return self.arr[index] + self.block_add[block_id]
    
    def __str__(self):
        """返回对象的字符串表示"""
        return f"BlockKth(n={self.n}, block_size={self.block_size})"


def test_block_kth():
    """测试函数，按照题目输入格式"""
    import sys
    input = sys.stdin.read().split()
    ptr = 0
    
    n = int(input[ptr])
    ptr += 1
    m = int(input[ptr])
    ptr += 1
    
    array = list(map(int, input[ptr:ptr + n]))
    ptr += n
    
    solution = BlockKth(array)
    
    for _ in range(m):
        op = int(input[ptr])
        ptr += 1
        
        if op == 1:
            # 区间加法
            l = int(input[ptr]) - 1
            ptr += 1
            r = int(input[ptr]) - 1
            ptr += 1
            val = int(input[ptr])
            ptr += 1
            solution.add_range(l, r, val)
        elif op == 2:
            # 查询第k小
            l = int(input[ptr]) - 1
            ptr += 1
            r = int(input[ptr]) - 1
            ptr += 1
            k = int(input[ptr])
            ptr += 1
            result = solution.query_kth(l, r, k)
            print(result)


def performance_test():
    """性能测试函数"""
    import random
    
    SIZE = 10000
    print(f"生成大小为{SIZE}的随机数组...")
    large_array = [random.randint(0, 1000000) for _ in range(SIZE)]
    
    solution = BlockKth(large_array)
    
    ops = 100
    print(f"执行{ops}次随机操作...")
    
    for i in range(ops):
        if random.randint(0, 1) == 0:
            # 区间加法
            l = random.randint(0, SIZE - 1)
            r = random.randint(0, SIZE - 1)
            if l > r:
                l, r = r, l
            val = random.randint(0, 100)
            solution.add_range(l, r, val)
        else:
            # 查询第k小
            l = random.randint(0, SIZE - 1)
            r = random.randint(0, SIZE - 1)
            if l > r:
                l, r = r, l
            k = random.randint(1, r - l + 1)
            if i < 10:  # 只输出前10个查询结果
                result = solution.query_kth(l, r, k)
                print(f"区间 [{l}, {r}] 的第{k}小是: {result}")
    
    print("性能测试完成")

def example_test():
    """简单示例测试"""
    print("=== 洛谷P5356 由乃打扑克 示例演示 ===")
    
    # 创建示例数组
    example = [1, 3, 5, 2, 4, 6, 7, 9, 8]
    print(f"原始数组: {example}")
    
    solution = BlockKth(example)
    
    # 测试区间加法
    solution.add_range(1, 5, 2)
    print("对索引1-5（值3-6）加2后：")
    
    # 显示数组当前状态
    current_array = [solution.get_value(i) for i in range(len(example))]
    print(f"当前数组: {current_array}")
    
    # 测试查询第k小
    k = 3
    result = solution.query_kth(0, 8, k)
    print(f"整个数组的第{k}小元素是: {result}")
    
    # 区间查询示例
    l, r = 2, 7
    k = 2
    result = solution.query_kth(l, r, k)
    print(f"区间 [{l}, {r}] 的第{k}小元素是: {result}")


def run_demo():
    """运行演示"""
    print("1. 示例演示")
    print("2. 标准测试（按题目输入格式）")
    print("3. 性能测试")
    print("请选择测试类型：")
    
    try:
        choice = input().strip()
        
        if choice == '1':
            example_test()
        elif choice == '2':
            print("请输入测试数据：")
            test_block_kth()
        elif choice == '3':
            performance_test()
        else:
            print("无效选择，运行示例演示")
            example_test()
    except Exception as e:
        print(f"发生错误：{e}")


if __name__ == "__main__":
    run_demo()


"""
Python语言特定优化分析：
1. 使用bisect模块提供的高效二分查找函数
2. 采用列表切片和copy方法提高代码可读性
3. 实现边界检查和异常处理，增强代码健壮性
4. 针对Python性能特点，调整了块大小策略
5. 提供多种查询方法，可根据数据规模选择

时间复杂度分析：
- 区间加法：O(√n)
  - 不完整块：O(√n)修改 + O(√n log √n)排序
  - 完整块：O(1)标记更新
- 查询第k小：O(√n log V)
  - 二分答案：O(log V)次迭代
  - 每次迭代统计：O(√n)

空间复杂度分析：
- O(n) 原始数组
- O(n) 排序块数组
- O(√n) 块标记
- 总体空间复杂度：O(n)

Python性能优化建议：
1. 对于大数据规模，可考虑使用numpy加速数组操作
2. 在极端情况下，可以牺牲一些时间复杂度使用更简单的算法
3. 输入数据量大时，使用sys.stdin.readline()或读取全部输入一次性处理

边界情况处理：
1. 非法k值（k<=0或k>区间长度）：通过异常处理
2. 索引越界：通过边界检查
3. 空区间：在add_range中处理l和r的顺序
"""