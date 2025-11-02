"""
完美矩形 - 扫描线算法应用
题目链接: https://leetcode.cn/problems/perfect-rectangle/

题目描述:
给你一个数组 rectangles ，其中 rectangles[i] = [xi, yi, ai, bi] 表示一个坐标轴平行的矩形。
这个矩形的左下顶点是 (xi, yi) ，右上顶点是 (ai, bi) 。
如果所有矩形一起精确覆盖某个矩形区域，则返回 true ；否则，返回 false 。

解题思路:
使用扫描线算法结合几何性质判断矩形是否完美覆盖。
1. 面积检查：所有矩形面积之和等于最外层矩形的面积
2. 顶点检查：除了四个角点外，其他所有顶点出现的次数都是偶数次
3. 边界检查：最终应该只有四个顶点，且正好是最外层矩形的四个角点

算法复杂度: 时间复杂度O(n log n)，空间复杂度O(n)
工程化考量:
1. 异常处理: 检查矩形数据合法性
2. 边界条件: 处理矩形重叠和边界情况
3. 性能优化: 使用哈希表快速统计顶点
4. 可读性: 详细注释和模块化设计
5. 提供了两种实现方式：基本版本和优化版本
"""

from collections import defaultdict
import bisect

class PerfectRectangle:
    def is_rectangle_cover(self, rectangles):
        """
        判断矩形是否完美覆盖
        算法核心思想：
        1. 面积检查：所有矩形面积之和必须等于最外层矩形的面积
        2. 顶点检查：除了四个角点外，其他所有顶点出现的次数都是偶数次
        3. 边界检查：最终应该只有四个顶点，且正好是最外层矩形的四个角点
        
        Args:
            rectangles: 矩形数组，每个元素为 [xi, yi, ai, bi]
        
        Returns:
            是否完美覆盖
        """
        # 边界条件检查
        if not rectangles:
            return False
        
        # 记录所有顶点及其出现次数
        point_count = defaultdict(int)
        
        # 计算所有矩形的面积和
        total_area = 0
        
        # 记录最小和最大的坐标，用于计算最终矩形的面积
        min_x = float('inf')
        min_y = float('inf')
        max_x = float('-inf')
        max_y = float('-inf')
        
        for rect in rectangles:
            if len(rect) != 4:
                raise ValueError("Invalid rectangle format")
            
            x1, y1, x2, y2 = rect
            
            # 检查坐标合法性
            if x1 >= x2 or y1 >= y2:
                raise ValueError("Invalid rectangle coordinates")
            
            # 更新边界坐标
            min_x = min(min_x, x1)
            min_y = min(min_y, y1)
            max_x = max(max_x, x2)
            max_y = max(max_y, y2)
            
            # 计算当前矩形的面积并累加到总面积
            total_area += (x2 - x1) * (y2 - y1)
            
            # 记录四个顶点
            points = [
                f"{x1},{y1}",  # 左下角
                f"{x1},{y2}",  # 左上角
                f"{x2},{y1}",  # 右下角
                f"{x2},{y2}"   # 右上角
            ]
            
            # 更新顶点计数
            for point in points:
                point_count[point] += 1
        
        # 检查面积条件：所有矩形面积之和必须等于最外层矩形的面积
        expected_area = (max_x - min_x) * (max_y - min_y)
        if total_area != expected_area:
            return False
        
        # 检查顶点条件：除了四个角点外，其他所有顶点出现的次数必须是偶数次
        # 四个角点应该只出现一次，其他顶点应该出现偶数次
        corner_points = [
            f"{min_x},{min_y}",  # 左下角
            f"{min_x},{max_y}",  # 左上角
            f"{max_x},{min_y}",  # 右下角
            f"{max_x},{max_y}"   # 右上角
        ]
        
        # 检查四个角点
        for corner in corner_points:
            count = point_count.get(corner, 0)
            if count != 1:
                return False
            del point_count[corner]
        
        # 检查其他顶点：出现次数必须是偶数次
        for count in point_count.values():
            if count % 2 != 0:
                return False
        
        return True
    
    def is_rectangle_cover_optimized(self, rectangles):
        """
        优化版本：使用扫描线算法进行更严格的检查
        通过扫描线算法检查矩形之间是否有重叠
        
        Args:
            rectangles: 矩形数组，每个元素为 [xi, yi, ai, bi]
        
        Returns:
            是否完美覆盖
        """
        if not rectangles:
            return False
        
        # 使用扫描线算法检查是否有重叠
        events = []
        
        for rect in rectangles:
            x1, y1, x2, y2 = rect
            
            # 添加开始事件(矩形下边界)和结束事件(矩形上边界)
            events.append([y1, x1, x2, 1])   # 开始事件
            events.append([y2, x1, x2, -1])  # 结束事件
        
        # 按y坐标排序
        events.sort(key=lambda a: (a[0], -a[3]))  # 相同y坐标时，开始事件优先
        
        # 使用列表维护当前活动的x区间
        active_intervals = []
        
        current_y = float('-inf')
        
        for event in events:
            y, x1, x2, event_type = event
            
            if event_type == 1:
                # 开始事件：检查是否有重叠
                # 找到插入位置
                pos = bisect.bisect_left(active_intervals, [x1, x2])
                
                # 检查与前一个区间的重叠
                if pos > 0 and active_intervals[pos-1][1] > x1:
                    return False  # 有重叠
                
                # 检查与后一个区间的重叠
                if pos < len(active_intervals) and active_intervals[pos][0] < x2:
                    return False  # 有重叠
                
                # 插入新区间
                active_intervals.insert(pos, [x1, x2])
            else:
                # 结束事件：移除区间
                pos = bisect.bisect_left(active_intervals, [x1, x2])
                if pos < len(active_intervals) and active_intervals[pos] == [x1, x2]:
                    active_intervals.pop(pos)
            
            current_y = y
        
        # 再次使用基本方法进行最终检查
        return self.is_rectangle_cover(rectangles)
    
    def print_point_statistics(self, rectangles):
        """
        调试辅助方法：打印顶点统计信息
        用于调试和理解算法过程
        
        Args:
            rectangles: 矩形数组
        """
        point_count = defaultdict(int)
        total_area = 0
        
        min_x, min_y = float('inf'), float('inf')
        max_x, max_y = float('-inf'), float('-inf')
        
        for rect in rectangles:
            x1, y1, x2, y2 = rect
            
            min_x = min(min_x, x1)
            min_y = min(min_y, y1)
            max_x = max(max_x, x2)
            max_y = max(max_y, y2)
            
            total_area += (x2 - x1) * (y2 - y1)
            
            points = [
                f"{x1},{y1}", f"{x1},{y2}",
                f"{x2},{y1}", f"{x2},{y2}"
            ]
            
            for point in points:
                point_count[point] += 1
        
        expected_area = (max_x - min_x) * (max_y - min_y)
        
        print(f"总面积: {total_area}")
        print(f"期望面积: {expected_area}")
        print(f"面积匹配: {total_area == expected_area}")
        print(f"边界: [{min_x}, {min_y}] - [{max_x}, {max_y}]")
        
        print("顶点统计:")
        for point, count in point_count.items():
            print(f"  {point}: {count}")

def test_perfect_rectangle():
    """测试函数"""
    solution = PerfectRectangle()
    
    # 测试用例1: 完美覆盖
    # 矩形组合形成一个完整的矩形区域
    rectangles1 = [
        [1, 1, 3, 3],
        [3, 1, 4, 2],
        [3, 2, 4, 4],
        [1, 3, 2, 4],
        [2, 3, 3, 4]
    ]
    result1 = solution.is_rectangle_cover(rectangles1)
    print(f"测试用例1 结果: {result1}")  # 预期: true
    
    # 测试用例2: 有重叠
    # 矩形之间存在重叠区域
    rectangles2 = [
        [1, 1, 3, 3],
        [3, 1, 4, 2],
        [1, 3, 2, 4],
        [2, 2, 4, 4]
    ]
    result2 = solution.is_rectangle_cover(rectangles2)
    print(f"测试用例2 结果: {result2}")  # 预期: false
    
    # 测试用例3: 有空隙
    # 矩形之间存在空隙
    rectangles3 = [
        [1, 1, 2, 3],
        [2, 1, 3, 3],
        [3, 1, 4, 2],
        [3, 2, 4, 3]
    ]
    result3 = solution.is_rectangle_cover(rectangles3)
    print(f"测试用例3 结果: {result3}")  # 预期: true
    
    # 测试用例4: 单个矩形
    # 只有一个矩形，自然是完美覆盖
    rectangles4 = [[0, 0, 1, 1]]
    result4 = solution.is_rectangle_cover(rectangles4)
    print(f"测试用例4 结果: {result4}")  # 预期: true
    
    # 测试用例5: 两个相邻矩形
    # 两个矩形相邻，形成一个更大的矩形
    rectangles5 = [
        [0, 0, 1, 1],
        [1, 0, 2, 1]
    ]
    result5 = solution.is_rectangle_cover(rectangles5)
    print(f"测试用例5 结果: {result5}")  # 预期: true
    
    # 测试用例6: 面积不匹配
    # 两个矩形部分重叠，总面积不等于外接矩形面积
    rectangles6 = [
        [0, 0, 2, 2],
        [1, 1, 3, 3]
    ]
    result6 = solution.is_rectangle_cover(rectangles6)
    print(f"测试用例6 结果: {result6}")  # 预期: false
    
    # 测试用例7: 顶点条件不满足
    # 两个完全相同的矩形，顶点计数不满足条件
    rectangles7 = [
        [0, 0, 2, 2],
        [0, 0, 2, 2]  # 完全相同的矩形
    ]
    result7 = solution.is_rectangle_cover(rectangles7)
    print(f"测试用例7 结果: {result7}")  # 预期: false
    
    # 测试优化版本
    print("\n=== 优化版本测试 ===")
    result1_opt = solution.is_rectangle_cover_optimized(rectangles1)
    print(f"测试用例1 优化版本结果: {result1_opt}")
    
    result2_opt = solution.is_rectangle_cover_optimized(rectangles2)
    print(f"测试用例2 优化版本结果: {result2_opt}")

if __name__ == "__main__":
    test_perfect_rectangle()