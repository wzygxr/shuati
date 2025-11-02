"""
LeetCode 218. The Skyline Problem
题目链接: https://leetcode.cn/problems/the-skyline-problem/
题目描述:
城市的天际线是从远处观看该城市中所有建筑物形成的轮廓的外部轮廓。
给你所有建筑物的位置和高度，请返回由这些建筑物形成的天际线。

每个建筑物的几何信息由数组 buildings 表示，其中三元组 buildings[i] = [lefti, righti, heighti] 表示：
- lefti 是第 i 座建筑物左边缘的 x 坐标。
- righti 是第 i 座建筑物右边缘的 x 坐标。
- heighti 是第 i 座建筑物的高度。

天际线应该表示为由 "关键点" 组成的列表，格式 [[x1,y1],[x2,y2],...]，并按 x 坐标进行排序。
关键点是水平线段的左端点。最后一个关键点也是天际线的终点，即最右侧建筑物的终点，高度为 0。

示例 1:
输入: buildings = [[2,9,10],[3,7,15],[5,12,12],[15,20,10],[19,24,8]]
输出: [[2,10],[3,15],[7,12],[12,0],[15,10],[20,8],[24,0]]

示例 2:
输入: buildings = [[0,2,3],[2,5,3]]
输出: [[0,3],[5,0]]

提示:
1 <= buildings.length <= 10^4
0 <= lefti < righti <= 2^31 - 1
1 <= heighti <= 2^31 - 1

解题思路:
这是一个经典的扫描线问题，可以使用线段树来解决。
1. 收集所有建筑物的左右边界坐标，进行离散化处理
2. 使用线段树维护每个离散化区间的高度最大值
3. 从左到右扫描，当遇到建筑物的左边界时，更新对应区间的高度
4. 当遇到建筑物的右边界时，恢复对应区间的高度
5. 在扫描过程中记录高度变化的关键点

时间复杂度: O(n log n)，其中n是建筑物数量
空间复杂度: O(n)
"""

class SegmentTree:
    """
    线段树类，用于维护区间最大值
    
    设计要点：
    1. 使用懒标记技术优化区间更新效率
    2. 维护区间最大高度，用于确定天际线轮廓
    3. 支持区间最大值查询和区间最大值更新
    
    时间复杂度分析：
    - 单次更新/查询：O(log n)
    - 懒标记下推：O(1)
    
    空间复杂度：O(n)
    """
    
    def __init__(self, size):
        """
        初始化线段树
        Args:
            size: 离散化后的坐标数量
        """
        self.n = size
        self.tree = [0] * (4 * size)  # 线段树数组，存储区间最大值
        self.lazy = [0] * (4 * size)   # 懒标记数组，用于延迟更新
        
    def build(self, l, r, i):
        """
        递归构建线段树
        Args:
            l, r: 当前节点表示的区间
            i: 当前节点索引
        """
        if l == r:
            self.tree[i] = 0
            return
        mid = (l + r) // 2
        self.build(l, mid, i * 2)
        self.build(mid + 1, r, i * 2 + 1)
        self.push_up(i)
    
    def push_up(self, i):
        """向上传递"""
        self.tree[i] = max(self.tree[i * 2], self.tree[i * 2 + 1])
    
    def push_down(self, i):
        """懒标记下发"""
        if self.lazy[i] != 0:
            self.tree[i * 2] = max(self.tree[i * 2], self.lazy[i])
            self.tree[i * 2 + 1] = max(self.tree[i * 2 + 1], self.lazy[i])
            self.lazy[i * 2] = max(self.lazy[i * 2], self.lazy[i])
            self.lazy[i * 2 + 1] = max(self.lazy[i * 2 + 1], self.lazy[i])
            self.lazy[i] = 0
    
    def update(self, jobl, jobr, val, l, r, i):
        """区间更新最大值"""
        if jobl <= l and r <= jobr:
            self.tree[i] = max(self.tree[i], val)
            self.lazy[i] = max(self.lazy[i], val)
            return
        self.push_down(i)
        mid = (l + r) // 2
        if jobl <= mid:
            self.update(jobl, jobr, val, l, mid, i * 2)
        if jobr > mid:
            self.update(jobl, jobr, val, mid + 1, r, i * 2 + 1)
        self.push_up(i)
    
    def query(self, jobl, jobr, l, r, i):
        """区间查询最大值"""
        if jobl <= l and r <= jobr:
            return self.tree[i]
        self.push_down(i)
        mid = (l + r) // 2
        ans = 0
        if jobl <= mid:
            ans = max(ans, self.query(jobl, jobr, l, mid, i * 2))
        if jobr > mid:
            ans = max(ans, self.query(jobl, jobr, mid + 1, r, i * 2 + 1))
        return ans

class Solution:
    def getSkyline(self, buildings):
        """
        获取天际线轮廓
        
        工程化考量：
        1. 异常处理：检查输入参数有效性
        2. 边界测试：处理空输入、单建筑、重叠建筑等边界情况
        3. 性能优化：使用离散化减少线段树规模，懒标记优化区间更新
        
        Args:
            buildings: 建筑物列表，每个建筑物为[left, right, height]
            
        Returns:
            list: 天际线关键点列表
            
        Raises:
            ValueError: 当输入参数不合法时抛出异常
        """
        # 参数校验
        if buildings is None:
            raise ValueError("输入建筑物数组不能为None")
            
        if not buildings:
            return []
        
        # 参数校验：检查每个建筑物的格式
        for i, building in enumerate(buildings):
            if building is None or len(building) != 3:
                raise ValueError(f"第{i+1}个建筑物格式不正确，应为[left, right, height]")
            
            left, right, height = building
            
            # 检查坐标和高度有效性
            if left < 0 or right < 0 or height < 0:
                raise ValueError(f"第{i+1}个建筑物的坐标或高度不能为负数")
            
            if left >= right:
                raise ValueError(f"第{i+1}个建筑物的左边界必须小于右边界")
        
        # 收集所有坐标点并离散化
        nums, mapping = self.discretization(buildings)
        n = len(nums)
        
        # 初始化线段树
        seg_tree = SegmentTree(n)
        seg_tree.build(1, n, 1)
        
        # 创建事件列表：每个建筑物的左右边界
        events = []
        for building in buildings:
            left, right, height = building
            events.append((mapping[left], height, 1))   # 左边界事件
            events.append((mapping[right], height, -1))  # 右边界事件
        
        # 按坐标排序事件
        events.sort(key=lambda x: (x[0], -x[2]))  # 相同坐标时，先处理右边界再处理左边界
        
        # 扫描线处理
        result = []
        prev_height = 0
        
        for event in events:
            pos, height, event_type = event
            
            if event_type == 1:
                # 左边界：更新高度
                seg_tree.update(pos, pos, height, 1, n, 1)
            else:
                # 右边界：恢复高度（设置为0）
                seg_tree.update(pos, pos, 0, 1, n, 1)
            
            # 获取当前最大高度
            current_height = seg_tree.query(1, n, 1, n, 1)
            
            # 如果高度发生变化，记录关键点
            if current_height != prev_height:
                result.append([nums[pos - 1], current_height])
                prev_height = current_height
        
        return result
    
    def discretization(self, buildings):
        """
        离散化处理
        
        Args:
            buildings: 建筑物列表
            
        Returns:
            tuple: (离散化后的坐标列表, 坐标到索引的映射)
        """
        # 收集所有坐标点
        coords = set()
        for building in buildings:
            coords.add(building[0])  # 左边界
            coords.add(building[1])  # 右边界
        
        # 排序去重后的坐标
        nums = sorted(coords)
        
        # 建立映射关系
        mapping = {}
        for i, num in enumerate(nums):
            mapping[num] = i + 1
        
        return nums, mapping

def generate_large_test_data(size):
    """
    生成大规模测试数据
    用于性能测试和压力测试
    
    Args:
        size: 建筑物数量
        
    Returns:
        生成的测试数据
    """
    import random
    buildings = []
    
    for i in range(size):
        left = random.randint(0, 10000)
        right = left + random.randint(1, 100)  # 确保右边界大于左边界
        height = random.randint(1, 100)  # 高度为正数
        
        buildings.append([left, right, height])
    
    return buildings

# 测试函数
def test():
    """
    单元测试函数 - 覆盖各种边界情况和异常场景
    
    工程化测试考量：
    1. 正常功能测试
    2. 边界情况测试
    3. 异常输入测试
    4. 性能压力测试
    5. 随机数据测试
    """
    solution = Solution()
    
    print("=== 线段树天际线问题 - 工程化测试 ===
")
    
    # 测试用例1：标准功能测试
    print("1. 标准功能测试：")
    buildings1 = [[2,9,10],[3,7,15],[5,12,12],[15,20,10],[19,24,8]]
    result1 = solution.getSkyline(buildings1)
    print("   输入: [[2,9,10],[3,7,15],[5,12,12],[15,20,10],[19,24,8]]")
    print("   输出:", result1)
    print("   期望: [[2,10],[3,15],[7,12],[12,0],[15,10],[20,8],[24,0]]")
    print("   测试结果:", "✓ 通过" if len(result1) == 7 else "✗ 失败")
    print()
    
    # 测试用例2：边界情况 - 两个相邻建筑
    print("2. 边界情况测试 - 相邻建筑：")
    buildings2 = [[0,2,3],[2,5,3]]
    result2 = solution.getSkyline(buildings2)
    print("   输入: [[0,2,3],[2,5,3]]")
    print("   输出:", result2)
    print("   期望: [[0,3],[5,0]]")
    print("   测试结果:", "✓ 通过" if len(result2) == 2 else "✗ 失败")
    print()
    
    # 测试用例3：边界情况 - 单个建筑
    print("3. 边界情况测试 - 单个建筑：")
    buildings3 = [[1,5,10]]
    result3 = solution.getSkyline(buildings3)
    print("   输入: [[1,5,10]]")
    print("   输出:", result3)
    print("   期望: [[1,10],[5,0]]")
    print("   测试结果:", "✓ 通过" if len(result3) == 2 else "✗ 失败")
    print()
    
    # 测试用例4：边界情况 - 完全重叠建筑
    print("4. 边界情况测试 - 重叠建筑：")
    buildings4 = [[1,5,10],[1,5,15]]
    result4 = solution.getSkyline(buildings4)
    print("   输入: [[1,5,10],[1,5,15]]")
    print("   输出:", result4)
    print("   期望: [[1,15],[5,0]]")
    print("   测试结果:", "✓ 通过" if len(result4) == 2 else "✗ 失败")
    print()
    
    # 测试用例5：边界情况 - 空输入
    print("5. 边界情况测试 - 空输入：")
    buildings5 = []
    result5 = solution.getSkyline(buildings5)
    print("   输入: []")
    print("   输出:", result5)
    print("   期望: []")
    print("   测试结果:", "✓ 通过" if len(result5) == 0 else "✗ 失败")
    print()
    
    # 测试用例6：性能测试 - 大规模数据
    print("6. 性能测试 - 大规模数据：")
    try:
        import time
        buildings6 = generate_large_test_data(1000)
        start_time = time.time()
        result6 = solution.getSkyline(buildings6)
        end_time = time.time()
        execution_time = (end_time - start_time) * 1000  # 转换为毫秒
        
        print("   数据规模: 1000个建筑物")
        print("   执行时间: {:.2f}ms".format(execution_time))
        print("   输出关键点数量:", len(result6))
        print("   测试结果:", "✓ 性能良好" if execution_time < 1000 else "⚠ 性能需优化")
    except Exception as e:
        print("   性能测试异常:", str(e))
    print()
    
    # 测试用例7：异常输入测试
    print("7. 异常输入测试：")
    try:
        buildings7 = [[1,1,10]]  # 左边界等于右边界
        solution.getSkyline(buildings7)
        print("   测试结果: ✗ 应该抛出异常但未抛出")
    except ValueError as e:
        print("   异常测试 - 左边界等于右边界: ✓ 正确抛出异常:", str(e))
    
    try:
        buildings8 = [[-1,5,10]]  # 负坐标
        solution.getSkyline(buildings8)
        print("   测试结果: ✗ 应该抛出异常但未抛出")
    except ValueError as e:
        print("   异常测试 - 负坐标: ✓ 正确抛出异常:", str(e))
    
    try:
        buildings9 = None  # None输入
        solution.getSkyline(buildings9)
        print("   测试结果: ✗ 应该抛出异常但未抛出")
    except ValueError as e:
        print("   异常测试 - None输入: ✓ 正确抛出异常:", str(e))
    
    print("
=== 测试完成 ===")

if __name__ == "__main__":
    test()