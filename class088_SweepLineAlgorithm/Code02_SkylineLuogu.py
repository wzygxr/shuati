"""
天际线问题 (洛谷 P1904)
题目链接: https://www.luogu.com.cn/problem/P1904

题目描述:
城市的天际线是从远处观看该城市中所有建筑物形成的轮廓的外部轮廓。
给你所有建筑物的位置和高度，请返回由这些建筑物形成的天际线。

解题思路:
使用扫描线算法结合自定义最大堆实现天际线问题的求解。
1. 将建筑物的左右边界作为事件点
2. 使用离散化技术处理坐标值
3. 使用自定义最大堆维护当前活动建筑物的高度
4. 扫描过程中记录高度变化的关键点

时间复杂度: O(n log n) - 排序和堆操作
空间复杂度: O(n) - 存储事件和堆

工程化考量:
1. 输入输出优化: 使用高效的IO处理方式
2. 边界条件: 处理建筑物边界重叠情况
3. 性能优化: 使用自定义堆和离散化减少系统开销
4. 可读性: 详细注释和模块化设计
"""

import bisect
import sys

# 最大数组容量
MAXN = 20001

# 建筑物数组，每个元素为[left, right, height]
arr = []

# 离散化后的坐标值数组
xsort = []

# 每个离散化坐标点对应的高度
height = [0] * MAXN

class MaxHeap:
    """自定义最大堆实现"""
    
    def __init__(self):
        """初始化堆"""
        self.heap = []
        self.heap_size = 0
    
    def is_empty(self):
        """检查堆是否为空"""
        return self.heap_size == 0
    
    def peek_height(self):
        """获取堆顶元素的高度"""
        if self.is_empty():
            raise IndexError("堆为空")
        return self.heap[0][0]
    
    def peek_end(self):
        """获取堆顶元素的结束位置"""
        if self.is_empty():
            raise IndexError("堆为空")
        return self.heap[0][1]
    
    def push(self, h, e):
        """向堆中添加元素"""
        self.heap.append((h, e))
        self.heap_size += 1
        self._heap_insert(self.heap_size - 1)
    
    def poll(self):
        """移除堆顶元素"""
        if self.is_empty():
            raise IndexError("堆为空")
        
        self._swap(0, self.heap_size - 1)
        self.heap_size -= 1
        self._heapify(0)
        return self.heap.pop()
    
    def _heap_insert(self, i):
        """堆插入操作（上浮）"""
        # 当前元素的高度大于父节点高度时，需要上浮
        while i > 0 and self.heap[i][0] > self.heap[(i - 1) // 2][0]:
            self._swap(i, (i - 1) // 2)
            i = (i - 1) // 2
    
    def _heapify(self, i):
        """堆化操作（下沉）"""
        left = i * 2 + 1  # 左子节点索引
        
        # 当存在子节点时继续下沉
        while left < self.heap_size:
            # 找到左右子节点中高度较大的节点索引
            if left + 1 < self.heap_size and self.heap[left + 1][0] > self.heap[left][0]:
                best = left + 1
            else:
                best = left
            
            # 比较当前节点与较大子节点，确定是否需要交换
            if self.heap[best][0] <= self.heap[i][0]:
                best = i
            
            # 如果当前节点已经是最大的，则停止下沉
            if best == i:
                break
            
            # 交换节点并继续下沉
            self._swap(best, i)
            i = best
            left = i * 2 + 1
    
    def _swap(self, i, j):
        """交换堆中两个元素"""
        self.heap[i], self.heap[j] = self.heap[j], self.heap[i]

def compute(n, m):
    """
    计算天际线高度
    
    Args:
        n: 建筑物数量
        m: 离散化后的坐标点数量
    """
    # 初始化自定义堆
    heap = MaxHeap()
    
    # 扫描线算法处理每个离散化后的坐标点
    j = 0
    for i in range(1, m + 1):
        # 将起始位置小于等于当前点的所有建筑物加入堆中
        while j < n and arr[j][0] <= i:
            heap.push(arr[j][2], arr[j][1])
            j += 1
        
        # 移除堆中结束位置小于当前点的建筑物
        while not heap.is_empty() and heap.peek_end() < i:
            heap.poll()
        
        # 当前点的最大高度即为堆顶元素的高度
        if not heap.is_empty():
            height[i] = heap.peek_height()

def prepare(n):
    """
    准备工作：对坐标进行离散化处理
    1) 收集大楼左边界、右边界-1、右边界的值
    2) 收集的所有值排序、去重
    3) 大楼的左边界和右边界，修改成排名值
    4) 大楼根据左边界排序
    5) 返回离散值的个数
    
    Args:
        n: 建筑物数量
    
    Returns:
        离散化后的坐标点数量
    """
    global xsort, arr
    
    # 收集所有需要离散化的坐标值
    # 包括大楼的左边界、右边界-1、右边界
    x_values = []
    for i in range(n):
        x_values.append(arr[i][0])      # 左边界
        x_values.append(arr[i][1] - 1)  # 右边界-1
        x_values.append(arr[i][1])      # 右边界
    
    # 对收集到的坐标值进行排序
    x_values.sort()
    
    # 排序后去重，得到m个不同的坐标值
    xsort = sorted(list(set(x_values)))
    m = len(xsort)
    
    # 将建筑物的左右边界修改为对应的排名值
    for i in range(n):
        arr[i][0] = bisect.bisect_left(xsort, arr[i][0])        # 左边界
        arr[i][1] = bisect.bisect_left(xsort, arr[i][1] - 1)    # 右边界-1
    
    # 所有建筑物根据左边界排序
    arr.sort(key=lambda x: x[0])
    
    return m

def main():
    """主函数，处理输入输出并调用计算函数"""
    global arr
    
    # 读取所有建筑物数据
    input_lines = sys.stdin.read().strip().split('\n')
    arr = []
    for line in input_lines:
        if line.strip():
            values = list(map(int, line.strip().split()))
            if len(values) == 3:
                arr.append([values[0], values[2], values[1]])  # [左边界, 右边界, 高度]
    
    n = len(arr)
    
    # 准备工作：坐标离散化
    m = prepare(n)
    
    # 计算天际线
    compute(n, m)
    
    # 输出结果
    result = []
    result.append(f"{xsort[0]} {height[1]}")
    pre = height[1]
    for i in range(2, m + 1):
        if pre != height[i]:
            result.append(f"{xsort[i-1]} {height[i]}")
        pre = height[i]
    
    print(' '.join(result))

# 由于这是洛谷题目，需要特殊处理输入输出格式
# 在实际提交时，请将函数调用注释掉
# if __name__ == "__main__":
#     main()