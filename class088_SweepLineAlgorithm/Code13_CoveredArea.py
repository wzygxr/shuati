"""
覆盖的面积 (HDU 1255)
题目链接: http://acm.hdu.edu.cn/showproblem.php?pid=1255

题目描述:
给定多个矩形，计算被至少两个矩形覆盖的区域面积。
每个矩形由其左下角坐标(x1, y1)和右上角坐标(x2, y2)表示。

解题思路:
使用扫描线算法结合线段树实现被至少两个矩形覆盖的区域面积计算。
1. 将矩形拆分为上下两条边，作为扫描线事件
2. 按y坐标排序所有事件
3. 使用线段树维护x轴上的覆盖情况
4. 线段树需要维护被覆盖一次和被覆盖两次的长度
5. 扫描过程中计算相邻扫描线之间的面积

时间复杂度: O(n log n) - 排序和线段树操作
空间复杂度: O(n) - 存储事件和线段树

工程化考量:
1. 异常处理: 检查坐标合法性
2. 边界条件: 处理坐标重复和边界情况
3. 性能优化: 使用离散化减少线段树规模
4. 可读性: 详细注释和模块化设计
"""

import bisect

class CoveredArea:
    class SegmentTreeNode:
        """线段树节点类"""
        def __init__(self, left, right):
            self.left = left  # 区间左边界
            self.right = right  # 区间右边界
            self.cover = 0  # 当前区间被覆盖的次数
            self.len1 = 0.0  # 被覆盖一次的长度
            self.len2 = 0.0  # 被覆盖两次及以上的长度
    
    class Event:
        """扫描线事件类"""
        def __init__(self, x, y1, y2, flag):
            self.x = x  # x坐标
            self.y1 = y1  # y坐标下界
            self.y2 = y2  # y坐标上界
            self.flag = flag  # 1表示矩形开始，-1表示矩形结束
        
        def __lt__(self, other):
            """比较函数，用于排序"""
            return self.x < other.x
    
    def __init__(self):
        self.tree = []  # 线段树数组
        self.y = []  # y坐标离散化数组
    
    def build_tree(self, node, left, right):
        """构建线段树"""
        if node >= len(self.tree):
            self.tree.extend([None] * (node - len(self.tree) + 1))
        
        self.tree[node] = self.SegmentTreeNode(left, right)
        
        if left == right:
            return
        
        mid = (left + right) // 2
        self.build_tree(node * 2, left, mid)
        self.build_tree(node * 2 + 1, mid + 1, right)
    
    def update_tree(self, node, left, right, flag):
        """更新线段树"""
        if left > self.tree[node].right or right < self.tree[node].left:
            return
        
        if left <= self.tree[node].left and self.tree[node].right <= right:
            self.tree[node].cover += flag
        else:
            mid = (self.tree[node].left + self.tree[node].right) // 2
            if left <= mid:
                self.update_tree(node * 2, left, right, flag)
            if right > mid:
                self.update_tree(node * 2 + 1, left, right, flag)
        
        # 更新当前节点的覆盖长度
        self.update_node_length(node)
    
    def update_node_length(self, node):
        """更新节点的覆盖长度"""
        if self.tree[node].cover >= 2:
            # 被覆盖两次及以上
            self.tree[node].len2 = self.y[self.tree[node].right + 1] - self.y[self.tree[node].left]
            self.tree[node].len1 = 0.0
        elif self.tree[node].cover == 1:
            # 被覆盖一次
            self.tree[node].len1 = self.y[self.tree[node].right + 1] - self.y[self.tree[node].left]
            if self.tree[node].left == self.tree[node].right:
                self.tree[node].len2 = 0.0
            else:
                self.tree[node].len2 = (self.tree[node * 2].len1 + self.tree[node * 2].len2 + 
                                       self.tree[node * 2 + 1].len1 + self.tree[node * 2 + 1].len2)
        else:
            # 没有被覆盖
            if self.tree[node].left == self.tree[node].right:
                self.tree[node].len1 = 0.0
                self.tree[node].len2 = 0.0
            else:
                self.tree[node].len1 = self.tree[node * 2].len1 + self.tree[node * 2 + 1].len1
                self.tree[node].len2 = self.tree[node * 2].len2 + self.tree[node * 2 + 1].len2
    
    def find_index(self, value):
        """在离散化数组中查找索引"""
        return bisect.bisect_left(self.y, value)
    
    def calculate_covered_area(self, rectangles):
        """
        计算被至少两个矩形覆盖的区域面积
        Args:
            rectangles: 矩形数组，每个矩形为[x1, y1, x2, y2]
        Returns:
            被至少两个矩形覆盖的区域面积
        """
        # 边界条件检查
        if not rectangles:
            return 0.0
        
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
            
            # 添加开始事件和结束事件
            events.append(self.Event(x1, y1, y2, 1))
            events.append(self.Event(x2, y1, y2, -1))
        
        # 对事件按x坐标排序
        events.sort()
        
        # 离散化y坐标
        self.y = sorted(y_set)
        
        # 构建线段树
        size = len(self.y) - 1
        self.tree = []
        self.build_tree(1, 0, size - 1)
        
        # 扫描线算法
        area = 0.0
        last_x = events[0].x
        
        for event in events:
            # 计算当前扫描线与上一个扫描线之间的面积
            current_x = event.x
            area += self.tree[1].len2 * (current_x - last_x)
            last_x = current_x
            
            # 更新线段树
            left_index = self.find_index(event.y1)
            right_index = self.find_index(event.y2)
            self.update_tree(1, left_index, right_index - 1, event.flag)
        
        return area

def test_covered_area():
    """测试函数"""
    solution = CoveredArea()
    
    # 测试用例1: 两个重叠的矩形
    rectangles1 = [
        [0, 0, 2, 2],
        [1, 1, 3, 3]
    ]
    area1 = solution.calculate_covered_area(rectangles1)
    print(f"测试用例1 覆盖面积: {area1}")  # 预期: 1.0
    
    # 测试用例2: 三个矩形，部分重叠
    rectangles2 = [
        [0, 0, 3, 3],
        [1, 1, 4, 4],
        [2, 2, 5, 5]
    ]
    area2 = solution.calculate_covered_area(rectangles2)
    print(f"测试用例2 覆盖面积: {area2}")  # 预期: 4.0
    
    # 测试用例3: 四个矩形形成网格
    rectangles3 = [
        [0, 0, 2, 2],
        [0, 2, 2, 4],
        [2, 0, 4, 2],
        [2, 2, 4, 4]
    ]
    area3 = solution.calculate_covered_area(rectangles3)
    print(f"测试用例3 覆盖面积: {area3}")  # 预期: 0.0 (没有重叠)
    
    # 测试用例4: 三个矩形完全重叠
    rectangles4 = [
        [0, 0, 2, 2],
        [0, 0, 2, 2],
        [0, 0, 2, 2]
    ]
    area4 = solution.calculate_covered_area(rectangles4)
    print(f"测试用例4 覆盖面积: {area4}")  # 预期: 4.0
    
    # 测试用例5: 空数组
    rectangles5 = []
    area5 = solution.calculate_covered_area(rectangles5)
    print(f"测试用例5 覆盖面积: {area5}")  # 预期: 0.0

if __name__ == "__main__":
    test_covered_area()