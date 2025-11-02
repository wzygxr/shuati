"""
窗口的星星 (洛谷 P1502)
题目链接: https://www.luogu.com.cn/problem/P1502

题目描述:
给定一些星星的位置和亮度，求一个固定大小的窗口内星星亮度总和的最大值。
窗口的边界不算在内部（即边界上的星星不计入亮度总和）。

解题思路:
使用扫描线算法结合线段树实现窗口内星星亮度总和的最大值计算。
1. 将每个星星转化为一个矩形区域，表示窗口右上角可以放置的位置范围
2. 使用扫描线算法处理这些矩形区域
3. 线段树维护当前扫描线上的亮度总和
4. 求线段树中的最大值

时间复杂度: O(n log n) - 排序和线段树操作
空间复杂度: O(n) - 存储事件和线段树

工程化考量:
1. 异常处理: 检查输入数据合法性
2. 边界条件: 处理窗口边界和星星位置
3. 性能优化: 使用离散化减少线段树规模
4. 可读性: 详细注释和模块化设计
"""

import bisect

class WindowStars:
    class Star:
        """星星类"""
        def __init__(self, x, y, light):
            self.x = x  # 星星x坐标
            self.y = y  # 星星y坐标
            self.light = light  # 星星亮度
    
    class Event:
        """扫描线事件类"""
        def __init__(self, x, y1, y2, light):
            self.x = x  # x坐标
            self.y1 = y1  # y坐标下界
            self.y2 = y2  # y坐标上界
            self.light = light  # 亮度变化（正数表示增加，负数表示减少）
        
        def __lt__(self, other):
            """比较函数，用于排序"""
            if self.x != other.x:
                return self.x < other.x
            # 相同x坐标时，增加事件优先于减少事件
            return other.light < self.light
    
    class SegmentTreeNode:
        """线段树节点类（支持区间加和区间最大值查询）"""
        def __init__(self, left, right):
            self.left = left  # 区间左边界
            self.right = right  # 区间右边界
            self.max_val = 0  # 区间最大值
            self.lazy = 0  # 懒标记
    
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
    
    def update_tree(self, node, left, right, value):
        """更新线段树（区间加值）"""
        if left > self.tree[node].right or right < self.tree[node].left:
            return
        
        if left <= self.tree[node].left and self.tree[node].right <= right:
            # 完全包含，更新当前节点
            self.tree[node].max_val += value
            self.tree[node].lazy += value
        else:
            # 下推懒标记
            self.push_down(node)
            
            mid = (self.tree[node].left + self.tree[node].right) // 2
            if left <= mid:
                self.update_tree(node * 2, left, right, value)
            if right > mid:
                self.update_tree(node * 2 + 1, left, right, value)
            
            # 更新当前节点
            self.tree[node].max_val = max(self.tree[node * 2].max_val, self.tree[node * 2 + 1].max_val)
    
    def push_down(self, node):
        """下推懒标记"""
        if self.tree[node].lazy != 0:
            if self.tree[node].left != self.tree[node].right:
                self.tree[node * 2].max_val += self.tree[node].lazy
                self.tree[node * 2].lazy += self.tree[node].lazy
                self.tree[node * 2 + 1].max_val += self.tree[node].lazy
                self.tree[node * 2 + 1].lazy += self.tree[node].lazy
            self.tree[node].lazy = 0
    
    def find_index(self, value):
        """在离散化数组中查找索引"""
        return bisect.bisect_left(self.y, value)
    
    def max_window_light(self, stars, w, h):
        """
        计算窗口内星星亮度总和的最大值
        Args:
            stars: 星星数组，每个元素为Star对象
            w: 窗口宽度
            h: 窗口高度
        Returns:
            最大亮度总和
        """
        # 边界条件检查
        if not stars or w <= 0 or h <= 0:
            return 0
        
        # 收集所有y坐标用于离散化
        y_set = set()
        events = []
        
        for star in stars:
            # 检查星星数据合法性
            if star.x < 0 or star.y < 0 or star.light < 0:
                raise ValueError("Invalid star data")
            
            # 计算窗口右上角可以放置的矩形区域
            # 窗口右上角在[x1, y1]到[x2, y2]范围内时，星星会被包含在窗口内
            x1 = star.x
            y1 = star.y
            x2 = star.x + w - 1  # 窗口宽度为w，边界不算
            y2 = star.y + h - 1  # 窗口高度为h，边界不算
            
            y_set.add(y1)
            y_set.add(y2)
            
            # 添加开始事件和结束事件
            events.append(self.Event(x1, y1, y2, star.light))  # 开始事件：增加亮度
            events.append(self.Event(x2 + 1, y1, y2, -star.light))  # 结束事件：减少亮度
        
        # 对事件按x坐标排序
        events.sort()
        
        # 离散化y坐标
        self.y = sorted(y_set)
        
        # 构建线段树
        size = len(self.y) - 1
        self.tree = []
        self.build_tree(1, 0, size - 1)
        
        # 扫描线算法
        max_light = 0
        
        for event in events:
            # 更新线段树
            left_index = self.find_index(event.y1)
            right_index = self.find_index(event.y2)
            self.update_tree(1, left_index, right_index, event.light)
            
            # 更新最大值
            max_light = max(max_light, self.tree[1].max_val)
        
        return max_light

def test_window_stars():
    """测试函数"""
    solution = WindowStars()
    
    # 测试用例1: 简单情况
    stars1 = [
        WindowStars.Star(1, 1, 10),
        WindowStars.Star(2, 2, 20),
        WindowStars.Star(3, 3, 30)
    ]
    result1 = solution.max_window_light(stars1, 2, 2)
    print(f"测试用例1 最大亮度: {result1}")  # 预期: 50 (星星2和3)
    
    # 测试用例2: 窗口包含所有星星
    stars2 = [
        WindowStars.Star(1, 1, 5),
        WindowStars.Star(3, 3, 10),
        WindowStars.Star(5, 5, 15)
    ]
    result2 = solution.max_window_light(stars2, 10, 10)
    print(f"测试用例2 最大亮度: {result2}")  # 预期: 30 (所有星星)
    
    # 测试用例3: 窗口太小，无法包含任何星星
    stars3 = [
        WindowStars.Star(10, 10, 100),
        WindowStars.Star(20, 20, 200)
    ]
    result3 = solution.max_window_light(stars3, 5, 5)
    print(f"测试用例3 最大亮度: {result3}")  # 预期: 0
    
    # 测试用例4: 星星在边界上
    stars4 = [
        WindowStars.Star(0, 0, 50),
        WindowStars.Star(1, 1, 100),
        WindowStars.Star(2, 2, 150)
    ]
    result4 = solution.max_window_light(stars4, 2, 2)
    print(f"测试用例4 最大亮度: {result4}")  # 预期: 250 (星星1和2)
    
    # 测试用例5: 空数组
    stars5 = []
    result5 = solution.max_window_light(stars5, 10, 10)
    print(f"测试用例5 最大亮度: {result5}")  # 预期: 0

if __name__ == "__main__":
    test_window_stars()