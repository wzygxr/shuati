# 715. Range 模块 - 线段树实现
# 题目来源：LeetCode 715 https://leetcode.cn/problems/range-module/
# 
# 题目描述：
# Range模块是跟踪数字范围的模块。设计一个数据结构来跟踪表示为 半开区间 的范围并查询它们。
# 实现 RangeModule 类:
# RangeModule() 初始化数据结构的对象
# void addRange(int left, int right) 添加 半开区间 [left, right), 跟踪该区间中的每个实数。添加与当前跟踪的区间重叠的区间时，应当添加在区间 [left, right) 中尚未被跟踪的任何数字到该区间中。
# boolean queryRange(int left, int right) 只有在当前正在跟踪区间 [left, right) 中的每一个实数时，才返回 true ，否则返回 false 。
# void removeRange(int left, int right) 停止跟踪 半开区间 [left, right) 中当前正在跟踪的每个实数。
# 
# 解题思路：
# 使用线段树配合懒惰标记来维护区间覆盖状态
# 1. 使用线段树节点维护区间覆盖信息：0表示未完全覆盖，1表示完全覆盖
# 2. 使用懒惰标记优化区间更新操作：-1表示无操作，0表示删除，1表示添加
# 3. addRange操作：将区间[left, right)标记为完全覆盖
# 4. removeRange操作：将区间[left, right)标记为未完全覆盖
# 5. queryRange操作：查询区间[left, right)是否完全覆盖
# 
# 时间复杂度分析：
# - addRange：O(log n)
# - removeRange：O(log n)
# - queryRange：O(log n)
# 空间复杂度：O(n)

class SegmentTree:
    def __init__(self, max_size):
        """
        初始化线段树
        :param max_size: 线段树能处理的最大值范围
        
        时间复杂度: O(n)
        空间复杂度: O(n)
        """
        self.max_size = max_size
        # 线段树通常需要4倍空间
        self.tree = [0] * (4 * max_size)
        # 懒惰标记：-1-无操作，0-删除，1-添加
        self.lazy = [-1] * (4 * max_size)
    
    def _push_down(self, node, start, end):
        """
        下推懒惰标记
        将当前节点的懒惰标记传递给左右子节点
        :param node: 当前节点索引
        :param start: 当前节点维护的区间左边界
        :param end: 当前节点维护的区间右边界
        """
        # 如果当前节点有懒惰标记
        if self.lazy[node] != -1:
            mid = start + (end - start) // 2
            left_node = 2 * node + 1
            right_node = 2 * node + 2
            
            # 更新左子节点的值和懒惰标记
            self.tree[left_node] = self.lazy[node]
            self.lazy[left_node] = self.lazy[node]
            
            # 更新右子节点的值和懒惰标记
            self.tree[right_node] = self.lazy[node]
            self.lazy[right_node] = self.lazy[node]
            
            # 清除当前节点的懒惰标记
            self.lazy[node] = -1
    
    def add_range(self, left, right):
        """
        添加区间
        :param left: 区间左端点（包含）
        :param right: 区间右端点（不包含）
        
        时间复杂度: O(log n)
        """
        self._update_range(0, 0, self.max_size - 1, left, right - 1, 1)
    
    def remove_range(self, left, right):
        """
        删除区间
        :param left: 区间左端点（包含）
        :param right: 区间右端点（不包含）
        
        时间复杂度: O(log n)
        """
        self._update_range(0, 0, self.max_size - 1, left, right - 1, 0)
    
    def query_range(self, left, right):
        """
        查询区间是否完全覆盖
        :param left: 区间左端点（包含）
        :param right: 区间右端点（不包含）
        :return: 如果区间完全覆盖返回True，否则返回False
        
        时间复杂度: O(log n)
        """
        return self._query(0, 0, self.max_size - 1, left, right - 1)
    
    def _update_range(self, node, start, end, left, right, val):
        """
        更新区间的辅助函数
        :param node: 当前节点索引
        :param start: 当前节点维护的区间左边界
        :param end: 当前节点维护的区间右边界
        :param left: 更新区间左边界
        :param right: 更新区间右边界
        :param val: 要设置的值（0-删除，1-添加）
        """
        # 更新区间与当前节点维护区间无交集，直接返回
        if right < start or end < left:
            return
        
        # 当前节点维护区间完全包含在更新区间内
        if left <= start and end <= right:
            self.tree[node] = val
            self.lazy[node] = val
            return
        
        # 下推懒惰标记
        self._push_down(node, start, end)
        
        # 部分重叠，递归更新左右子树
        mid = start + (end - start) // 2
        left_node = 2 * node + 1
        right_node = 2 * node + 2
        
        self._update_range(left_node, start, mid, left, right, val)
        self._update_range(right_node, mid + 1, end, left, right, val)
        
        # 更新当前节点的值
        # 如果左右子节点都完全覆盖，则当前节点也完全覆盖
        self.tree[node] = 1 if (self.tree[left_node] == 1 and self.tree[right_node] == 1) else 0
    
    def _query(self, node, start, end, left, right):
        """
        查询辅助函数
        :param node: 当前节点索引
        :param start: 当前节点维护的区间左边界
        :param end: 当前节点维护的区间右边界
        :param left: 查询区间左边界
        :param right: 查询区间右边界
        :return: 区间是否完全覆盖
        """
        # 查询区间与当前节点维护区间无交集，返回True（不影响整体结果）
        if right < start or end < left:
            return True
        
        # 当前节点维护区间完全包含在查询区间内，返回覆盖状态
        if left <= start and end <= right:
            return self.tree[node] == 1
        
        # 下推懒惰标记
        self._push_down(node, start, end)
        
        # 部分重叠，递归查询左右子树
        mid = start + (end - start) // 2
        left_node = 2 * node + 1
        right_node = 2 * node + 2
        
        left_result = self._query(left_node, start, mid, left, right)
        right_result = self._query(right_node, mid + 1, end, left, right)
        
        # 只有左右子树都完全覆盖，才返回True
        return left_result and right_result


class RangeModule:
    def __init__(self):
        """
        初始化RangeModule
        初始化数据结构的对象
        """
        # 由于范围很大，使用一个合理的大小作为示例
        # 实际应用中应该使用动态开点线段树
        self.MAX_RANGE = 1000001  # 简化版，实际范围是10^9
        self.segment_tree = SegmentTree(self.MAX_RANGE)
    
    def addRange(self, left: int, right: int) -> None:
        """
        添加半开区间 [left, right)
        跟踪该区间中的每个实数
        :param left: 区间左边界（包含）
        :param right: 区间右边界（不包含）
        """
        self.segment_tree.add_range(left, right)
    
    def queryRange(self, left: int, right: int) -> bool:
        """
        查询区间 [left, right) 是否完全被跟踪
        只有在当前正在跟踪区间中的每一个实数时，才返回 True ，否则返回 False
        :param left: 区间左边界（包含）
        :param right: 区间右边界（不包含）
        :return: 如果区间完全被跟踪返回True，否则返回False
        """
        return self.segment_tree.query_range(left, right)
    
    def removeRange(self, left: int, right: int) -> None:
        """
        移除半开区间 [left, right) 的跟踪
        停止跟踪区间中的每个实数
        :param left: 区间左边界（包含）
        :param right: 区间右边界（不包含）
        """
        self.segment_tree.remove_range(left, right)


# 测试代码
if __name__ == "__main__":
    # 测试用例
    range_module = RangeModule()
    range_module.addRange(10, 20)
    print(f"查询[10, 14]: {range_module.queryRange(10, 14)}")  # True
    print(f"查询[13, 15]: {range_module.queryRange(13, 15)}")  # True
    print(f"查询[16, 17]: {range_module.queryRange(16, 17)}")  # True
    range_module.removeRange(14, 16)
    print(f"删除后查询[10, 14]: {range_module.queryRange(10, 14)}")  # True
    print(f"删除后查询[13, 15]: {range_module.queryRange(13, 15)}")  # False
    print(f"删除后查询[16, 17]: {range_module.queryRange(16, 17)}")  # True