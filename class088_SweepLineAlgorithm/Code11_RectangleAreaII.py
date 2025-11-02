"""
矩形面积 II - 扫描线算法实现
题目链接: https://leetcode.cn/problems/rectangle-area-ii/

题目描述:
我们给出了一个（轴对齐的）矩形列表 rectangles。
对于 rectangle[i] = [x1, y1, x2, y2]，其中 (x1, y1) 是矩形 i 左下角的坐标，
(x2, y2) 是该矩形右上角的坐标。
找出平面中所有矩形叠加覆盖后的总面积。
由于答案可能太大，请返回它对 10^9 + 7 取模的结果。

解题思路:
使用扫描线算法结合线段树实现矩形面积并的计算。
1. 将矩形拆分为左右两条边，作为扫描线事件
2. 按x坐标排序所有事件
3. 使用线段树维护y轴上的覆盖情况
4. 扫描过程中计算相邻扫描线之间的面积
5. 结果对 10^9 + 7 取模

算法复杂度: 时间复杂度O(n log n)，空间复杂度O(n)
工程化考量:
1. 异常处理: 检查坐标合法性
2. 边界条件: 处理坐标重复和边界情况
3. 性能优化: 使用离散化减少线段树规模
4. 数值处理: 大数取模运算
5. 可读性: 详细注释和模块化设计
6. 提供了两种实现方式：基本版本和优化版本
"""

class RectangleAreaII:
    def __init__(self):
        self.MOD = 1000000007
    
    def rectangle_area(self, rectangles):
        """
        计算矩形面积并（取模）
        算法核心思想：
        1. 将每个矩形的左右边界作为扫描线事件
        2. 按x坐标排序所有扫描线事件
        3. 使用线段树维护y轴上的覆盖长度
        4. 相邻扫描线之间的面积 = y轴覆盖长度 × x轴距离
        5. 结果对 10^9 + 7 取模
        
        Args:
            rectangles: 矩形数组，每个矩形为 [x1, y1, x2, y2]
        
        Returns:
            总面积对 10^9 + 7 取模的结果
        """
        # 边界条件检查
        if not rectangles:
            return 0
        
        # 收集所有y坐标用于离散化
        y_set = set()
        events = []
        
        for rect in rectangles:
            if len(rect) != 4:
                raise ValueError("Invalid rectangle format")
            
            x1, y1, x2, y2 = rect
            
            # 检查坐标合法性
            if x1 >= x2 or y1 >= y2:
                raise ValueError("Invalid rectangle coordinates")
            
            y_set.add(y1)
            y_set.add(y2)
            
            # 添加开始事件(矩形左边)和结束事件(矩形右边)
            events.append([x1, y1, y2, 1])
            events.append([x2, y1, y2, -1])
        
        # 对事件按x坐标排序
        events.sort(key=lambda x: x[0])
        
        # 离散化y坐标
        y = sorted(y_set)
        
        # 构建线段树
        size = len(y) - 1
        # cover数组记录每个节点的覆盖次数
        cover = [0] * (4 * size)
        # len数组记录每个节点的覆盖长度
        length = [0] * (4 * size)
        
        # 扫描线算法
        area = 0
        last_x = events[0][0]
        
        for event in events:
            x, y1, y2, flag = event
            
            # 计算当前扫描线与上一个扫描线之间的面积
            # 面积 = y轴覆盖长度 × x轴距离
            width = x - last_x
            height = length[1]
            
            # 累加面积，注意取模
            area = (area + width * height) % self.MOD
            last_x = x
            
            # 更新线段树中的覆盖情况
            left_index = self.find_index(y, y1)
            right_index = self.find_index(y, y2)
            self.update_tree(cover, length, y, 1, 0, size - 1, left_index, right_index - 1, flag)
        
        return area
    
    def update_tree(self, cover, length, y, node, left, right, l, r, flag):
        """
        更新线段树
        Args:
            cover: 覆盖次数数组
            length: 覆盖长度数组
            y: 离散化y坐标数组
            node: 当前节点编号
            left: 当前节点表示区间的左边界
            right: 当前节点表示区间的右边界
            l: 操作区间左边界
            r: 操作区间右边界
            flag: 操作值(+1表示添加覆盖，-1表示移除覆盖)
        """
        # 如果操作区间与当前节点区间无交集，直接返回
        if l > right or r < left:
            return
        
        # 如果操作区间完全包含当前节点区间，更新覆盖次数
        if l <= left and right <= r:
            cover[node] += flag
        else:
            # 否则递归更新左右子树
            mid = (left + right) // 2
            if l <= mid:
                self.update_tree(cover, length, y, node * 2, left, mid, l, r, flag)
            if r > mid:
                self.update_tree(cover, length, y, node * 2 + 1, mid + 1, right, l, r, flag)
        
        # 更新当前节点的覆盖长度
        if cover[node] > 0:
            # 如果当前区间被覆盖，长度为实际坐标长度
            length[node] = y[right + 1] - y[left]
        else:
            # 如果当前区间未被覆盖，长度为子区间的覆盖长度之和
            if left == right:
                length[node] = 0
            else:
                length[node] = length[node * 2] + length[node * 2 + 1]
    
    def find_index(self, y, value):
        """
        在离散化数组中查找值对应的索引
        Args:
            y: 离散化y坐标数组
            value: 要查找的y坐标值
        
        Returns:
            离散化后的索引
        """
        left, right = 0, len(y) - 1
        while left <= right:
            mid = (left + right) // 2
            if y[mid] == value:
                return mid
            elif y[mid] < value:
                left = mid + 1
            else:
                right = mid - 1
        return -1  # 理论上不会发生
    
    def rectangle_area_optimized(self, rectangles):
        """
        优化版本：使用更高效的实现
        通过使用更紧凑的数据结构来提高性能
        
        Args:
            rectangles: 矩形数组，每个矩形为 [x1, y1, x2, y2]
        
        Returns:
            总面积对 10^9 + 7 取模的结果
        """
        if not rectangles:
            return 0
        
        # 使用更紧凑的数据结构
        y_set = set()
        events = []
        
        for rect in rectangles:
            x1, y1, x2, y2 = rect
            
            y_set.add(y1)
            y_set.add(y2)
            
            # 使用列表表示事件，[x坐标, y下界, y上界, 标志]
            events.append([x1, y1, y2, 1])
            events.append([x2, y1, y2, -1])
        
        # 按x坐标排序
        events.sort(key=lambda x: x[0])
        
        # 离散化y坐标
        y = sorted(y_set)
        
        # 使用数组实现线段树（更高效）
        n = len(y) - 1
        # cover数组记录每个节点的覆盖次数
        cover = [0] * (4 * n)
        # len数组记录每个节点的覆盖长度
        length = [0] * (4 * n)
        
        area = 0
        last_x = events[0][0]
        
        for event in events:
            x, y1, y2, flag = event
            
            # 计算当前扫描线与上一个扫描线之间的面积
            width = x - last_x
            area = (area + width * length[1]) % self.MOD
            last_x = x
            
            # 更新线段树
            left_idx = self.find_index(y, y1)
            right_idx = self.find_index(y, y2)
            self.update_tree(cover, length, y, 1, 0, n - 1, left_idx, right_idx - 1, flag)
        
        return area

def test_rectangle_area():
    """测试函数"""
    solution = RectangleAreaII()
    
    # 测试用例1: 标准情况
    # 矩形1: (0,0)到(2,2)，面积4
    # 矩形2: (1,1)到(3,3)，面积4
    # 重叠区域: (1,1)到(2,2)，面积1
    # 总面积: 4 + 4 - 1 = 7
    rectangles1 = [
        [0, 0, 2, 2],
        [1, 1, 3, 3]
    ]
    result1 = solution.rectangle_area(rectangles1)
    print(f"测试用例1 面积: {result1}")  # 预期: 7
    
    # 测试用例2: 单个矩形
    # 矩形: (0,0)到(1,1)
    # 总面积: 1
    rectangles2 = [[0, 0, 1, 1]]
    result2 = solution.rectangle_area(rectangles2)
    print(f"测试用例2 面积: {result2}")  # 预期: 1
    
    # 测试用例3: 三个矩形
    # 矩形1: (0,0)到(3,3)
    # 矩形2: (2,2)到(5,5)
    # 矩形3: (1,1)到(4,4)
    # 总面积: 27
    rectangles3 = [
        [0, 0, 3, 3],
        [2, 2, 5, 5],
        [1, 1, 4, 4]
    ]
    result3 = solution.rectangle_area(rectangles3)
    print(f"测试用例3 面积: {result3}")  # 预期: 27
    
    # 测试用例4: 空数组
    # 总面积: 0
    rectangles4 = []
    result4 = solution.rectangle_area(rectangles4)
    print(f"测试用例4 面积: {result4}")  # 预期: 0
    
    # 测试用例5: 大数测试
    # 矩形: (0,0)到(1000000000,1000000000)
    # 面积: 1000000000 * 1000000000 = 1000000000000000000
    # 取模后: 49
    rectangles5 = [
        [0, 0, 1000000000, 1000000000]
    ]
    result5 = solution.rectangle_area(rectangles5)
    print(f"测试用例5 面积: {result5}")  # 预期: 49 (取模后)
    
    # 测试优化版本
    print("\n=== 优化版本测试 ===")
    result1_opt = solution.rectangle_area_optimized(rectangles1)
    print(f"测试用例1 优化版本面积: {result1_opt}")
    
    result3_opt = solution.rectangle_area_optimized(rectangles3)
    print(f"测试用例3 优化版本面积: {result3_opt}")
    
    # 性能测试：大量矩形
    print("\n=== 性能测试 ===")
    import random
    rectangles6 = []
    for i in range(1000):
        x1 = random.randint(0, 1000)
        y1 = random.randint(0, 1000)
        x2 = x1 + random.randint(1, 100)
        y2 = y1 + random.randint(1, 100)
        rectangles6.append([x1, y1, x2, y2])
    
    import time
    start_time = time.time()
    result6 = solution.rectangle_area(rectangles6)
    end_time = time.time()
    print(f"性能测试 面积: {result6}")
    print(f"执行时间: {(end_time - start_time) * 1000:.2f}ms")

if __name__ == "__main__":
    test_rectangle_area()