# 扫描线问题 - 分块算法实现 (Python版本)
# 题目来源: HDU 1542
# 题目链接: http://acm.hdu.edu.cn/showproblem.php?pid=1542
# 题目大意: 计算多个矩形覆盖的总面积
# 约束条件: 矩形数量n ≤ 100

import sys
import math
import bisect

class Line:
    def __init__(self, x, y1, y2, flag):
        self.x = x
        self.y1 = y1
        self.y2 = y2
        self.flag = flag  # 1表示进入，-1表示离开
    
    def __lt__(self, other):
        return self.x < other.x

def main():
    input = sys.stdin.read().splitlines()
    ptr = 0
    case_num = 0
    
    while True:
        # 跳过空行
        while ptr < len(input) and input[ptr].strip() == '':
            ptr += 1
        if ptr >= len(input):
            break
        
        n = int(input[ptr].strip())
        ptr += 1
        if n == 0:
            break
        
        case_num += 1
        m = 0
        lines = []
        y_coords = []
        
        # 读取每个矩形
        for i in range(n):
            # 跳过空行
            while ptr < len(input) and input[ptr].strip() == '':
                ptr += 1
            
            parts = input[ptr].strip().split()
            ptr += 1
            x1 = float(parts[0])
            y1 = float(parts[1])
            x2 = float(parts[2])
            y2 = float(parts[3])
            
            # 添加两条扫描线
            lines.append(Line(x1, y1, y2, 1))
            lines.append(Line(x2, y1, y2, -1))
            
            # 收集y坐标
            y_coords.append(y1)
            y_coords.append(y2)
        
        # 离散化y坐标
        y_coords = sorted(list(set(y_coords)))
        m = len(y_coords)
        
        # 初始化分块结构
        blen = int(math.sqrt(m - 1))
        if blen == 0:
            blen = 1
        block_count = (m - 1 + blen - 1) // blen
        
        cover = [0] * (m - 1)  # 每个位置被覆盖的次数
        block_cover = [0] * block_count  # 每个块的覆盖标记（延迟更新）
        
        # 更新区间覆盖
        def update_range(l, r, delta):
            left_block = l // blen
            right_block = r // blen
            
            if left_block == right_block:
                # 所有元素都在同一个块内，直接暴力更新
                for i in range(l, r + 1):
                    cover[i] += delta
            else:
                # 处理左边不完整的块
                for i in range(l, (left_block + 1) * blen):
                    cover[i] += delta
                
                # 处理中间完整的块（使用块标记）
                for i in range(left_block + 1, right_block):
                    block_cover[i] += delta
                
                # 处理右边不完整的块
                for i in range(right_block * blen, r + 1):
                    cover[i] += delta
        
        # 计算当前覆盖的总长度
        def calculate_covered_length():
            total = 0.0
            for i in range(m - 1):
                block_idx = i // blen
                total_cover = cover[i] + block_cover[block_idx]
                if total_cover > 0:
                    total += y_coords[i + 1] - y_coords[i]
            return total
        
        # 按照x坐标排序扫描线
        lines.sort()
        
        area = 0.0
        for i in range(len(lines) - 1):
            # 找到y1和y2在离散化数组中的位置
            y1_pos = bisect.bisect_left(y_coords, lines[i].y1)
            y2_pos = bisect.bisect_left(y_coords, lines[i].y2)
            
            # 更新覆盖区间
            update_range(y1_pos, y2_pos - 1, lines[i].flag)
            
            # 计算当前扫描线到下一条扫描线之间的面积
            current_length = calculate_covered_length()
            delta_x = lines[i + 1].x - lines[i].x
            area += current_length * delta_x
        
        print(f"Test case #{case_num}")
        print(f"Total explored area: {area:.2f}")
        print()

# 测试用例
# 示例：
# 输入：
# 2
# 10 10 20 20
# 15 15 25 25
# 0
# 输出：
# Test case #1
# Total explored area: 200.00

if __name__ == "__main__":
    main()

'''
时间复杂度分析：
- 离散化：O(n log n)
- 初始化分块：O(n)
- 扫描线排序：O(n log n)
- 每次更新操作：O(√n)
- 每次长度计算：O(n)
- 总体时间复杂度：O(n^2 + n√n)

空间复杂度分析：
- 存储扫描线：O(n)
- 存储y坐标：O(n)
- 分块数据结构：O(n)
- 总体空间复杂度：O(n)

Python语言特性注意事项：
1. 使用自定义Line类并实现__lt__方法进行排序
2. 使用bisect模块的bisect_left函数进行二分查找
3. 使用列表存储数组和扫描线
4. 使用集合(set)去重并排序实现离散化
5. 注意处理输入中的空行

算法说明：
扫描线算法是解决矩形覆盖问题的经典方法，其核心思想是：

1. 预处理阶段：
   - 将每个矩形转换为两条垂直扫描线（左边界+1，右边界-1）
   - 收集所有y坐标并进行离散化，减少处理范围
   - 初始化分块数据结构用于维护覆盖状态

2. 扫描阶段：
   - 按照x坐标从小到大排序所有扫描线
   - 从左到右处理每条扫描线，更新当前的覆盖状态
   - 对于每两条相邻的扫描线，计算当前覆盖的垂直长度
   - 将垂直长度乘以两条扫描线之间的水平距离，累加到总面积中

3. 分块优化：
   - 将y轴离散化后的区间分成√n大小的块
   - 对于完整的块，使用块标记进行延迟更新
   - 对于不完整的块，直接暴力更新每个元素

优化说明：
- 离散化是处理浮点数坐标的关键步骤，将连续的坐标映射到整数索引
- 分块技术可以将区间更新操作的时间复杂度从O(n)降低到O(√n)
- 使用延迟更新技术减少不必要的操作，提高效率

与其他方法的对比：
- 分块算法：实现简单，对于小规模数据足够高效
- 线段树：实现复杂，但时间复杂度更低(O(n log n))
- 暴力法：实现最简单，但时间复杂度最高(O(n^2))

工程化考虑：
1. 注意浮点数精度问题，特别是在计算面积时
2. 处理输入中的空行和异常情况
3. 在Python中，对于大规模数据可以考虑使用更高效的数据结构
4. 分块的大小可以根据实际数据规模进行调整
'''