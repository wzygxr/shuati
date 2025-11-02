"""
矩形周长并 (POJ 1177, HDU 1828)
题目链接: POJ 1177: http://poj.org/problem?id=1177
题目链接: HDU 1828: http://acm.hdu.edu.cn/showproblem.php?pid=1828

解题思路:
使用扫描线算法，将矩形边界作为事件点，从左到右扫描
维护当前扫描线覆盖的垂直区间，计算水平周长和垂直周长

时间复杂度: O(n log n) - 排序和线段树操作
空间复杂度: O(n) - 存储事件点和线段树
"""

class RectanglePerimeterUnion:
    class Event:
        def __init__(self, x, y1, y2, event_type):
            self.x = x           # 事件点的x坐标
            self.y1 = y1         # 垂直区间的下边界
            self.y2 = y2         # 垂直区间的上边界
            self.type = event_type # 事件类型: 1表示进入，-1表示离开
        
        def __lt__(self, other):
            if self.x != other.x:
                return self.x < other.x
            return self.type > other.type  # 进入事件优先处理
    
    class SegmentTreeNode:
        def __init__(self):
            self.cover = 0        # 当前区间被覆盖的次数
            self.length = 0       # 当前区间被覆盖的长度
            self.seg_count = 0     # 当前区间内连续区间的数量
            self.left_cover = 0   # 左端点是否被覆盖
            self.right_cover = 0  # 右端点是否被覆盖
    
    def __init__(self):
        self.events = []
        self.y_coords = []
        self.seg_tree = []
    
    def build(self, idx, l, r):
        """构建线段树"""
        if l == r:
            self.seg_tree[idx] = self.SegmentTreeNode()
            return
        mid = (l + r) // 2
        self.build(idx * 2, l, mid)
        self.build(idx * 2 + 1, mid + 1, r)
    
    def push_up(self, idx, l, r):
        """更新线段树节点信息"""
        if self.seg_tree[idx].cover > 0:
            self.seg_tree[idx].length = self.y_coords[r + 1] - self.y_coords[l]
            self.seg_tree[idx].seg_count = 1
            self.seg_tree[idx].left_cover = 1
            self.seg_tree[idx].right_cover = 1
        else:
            if l == r:
                self.seg_tree[idx].length = 0
                self.seg_tree[idx].seg_count = 0
                self.seg_tree[idx].left_cover = 0
                self.seg_tree[idx].right_cover = 0
            else:
                left_child = self.seg_tree[idx * 2]
                right_child = self.seg_tree[idx * 2 + 1]
                
                self.seg_tree[idx].length = left_child.length + right_child.length
                self.seg_tree[idx].seg_count = left_child.seg_count + right_child.seg_count
                
                if left_child.right_cover and right_child.left_cover:
                    self.seg_tree[idx].seg_count -= 1
                
                self.seg_tree[idx].left_cover = left_child.left_cover
                self.seg_tree[idx].right_cover = right_child.right_cover
    
    def update(self, idx, l, r, ql, qr, val):
        """更新线段树区间"""
        if ql <= l and r <= qr:
            self.seg_tree[idx].cover += val
            self.push_up(idx, l, r)
            return
        
        mid = (l + r) // 2
        if ql <= mid:
            self.update(idx * 2, l, mid, ql, qr, val)
        if qr > mid:
            self.update(idx * 2 + 1, mid + 1, r, ql, qr, val)
        self.push_up(idx, l, r)
    
    def calculate_perimeter(self, rectangles):
        """计算矩形并集的周长"""
        if not rectangles:
            return 0
        
        # 收集所有y坐标并去重排序
        y_set = set()
        for rect in rectangles:
            y_set.add(rect[1])
            y_set.add(rect[3])
        
        self.y_coords = sorted(y_set)
        
        # 创建y坐标到索引的映射
        y_index = {}
        for i, y in enumerate(self.y_coords):
            y_index[y] = i
        
        # 创建事件
        self.events = []
        for rect in rectangles:
            x1, y1, x2, y2 = rect
            self.events.append(self.Event(x1, y1, y2, 1))
            self.events.append(self.Event(x2, y1, y2, -1))
        
        # 按x坐标排序事件
        self.events.sort()
        
        # 初始化线段树
        n = len(self.y_coords) - 1
        self.seg_tree = [self.SegmentTreeNode() for _ in range(4 * n)]
        self.build(1, 0, n - 1)
        
        perimeter = 0
        last_length = 0
        last_seg_count = 0
        
        for i, event in enumerate(self.events):
            y1_idx = y_index[event.y1]
            y2_idx = y_index[event.y2] - 1
            
            if y1_idx <= y2_idx:
                self.update(1, 0, n - 1, y1_idx, y2_idx, event.type)
            
            # 计算水平周长
            if i > 0:
                perimeter += 2 * last_seg_count * (event.x - self.events[i - 1].x)
            
            # 计算垂直周长
            perimeter += abs(self.seg_tree[1].length - last_length)
            
            last_length = self.seg_tree[1].length
            last_seg_count = self.seg_tree[1].seg_count
        
        return perimeter

# 测试代码
if __name__ == "__main__":
    solver = RectanglePerimeterUnion()
    
    # 测试用例1: 单个矩形
    test1 = [[0, 0, 10, 10]]
    print(f"单个矩形周长: {solver.calculate_perimeter(test1)}")  # 期望: 40
    
    # 测试用例2: 两个相邻矩形
    test2 = [[0, 0, 10, 10], [10, 0, 20, 10]]
    print(f"两个相邻矩形周长: {solver.calculate_perimeter(test2)}")  # 期望: 60
    
    # 测试用例3: 两个重叠矩形
    test3 = [[0, 0, 10, 10], [5, 5, 15, 15]]
    print(f"两个重叠矩形周长: {solver.calculate_perimeter(test3)}")  # 期望: 60
    
    # 测试用例4: 三个矩形
    test4 = [[0, 0, 10, 10], [5, 5, 15, 15], [10, 10, 20, 20]]
    print(f"三个矩形周长: {solver.calculate_perimeter(test4)}")