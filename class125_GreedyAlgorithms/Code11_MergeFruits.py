# 合并果子 (Merge Fruits)
# 在一个果园里，多多已经将所有的果子打了下来，而且按果子的不同种类分成了不同的堆。
# 多多决定把所有的果子合成一堆。
# 每一次合并，多多可以把两堆果子合并到一起，消耗的体力等于两堆果子的重量之和。
# 可以看出，所有的果子经过 n-1 次合并之后，就只剩下一堆了。
# 多多在合并果子时总共消耗的体力等于每次合并所耗体力之和。
# 因为还要花大力气把这些果子搬回家，所以多多在合并果子时要尽可能地节省体力。
# 假定每个果子重量都为 1，并且已知果子的种类数和每种果子的数目，
# 你的任务是设计出合并的次序方案，使多多耗费的体力最少，并输出这个最小的体力耗费值。
# 
# 算法标签: 贪心算法(Greedy Algorithm)、堆(Heap)、哈夫曼编码(Huffman Coding)
# 时间复杂度: O(n * logn)，其中n是果子种类数
# 空间复杂度: O(n)，最小堆的空间
# 测试链接 : https://www.luogu.com.cn/problem/P1090
# 相关题目: LeetCode 1046. 最后一块石头的重量、LeetCode 703. 数据流中的第K大元素
# 贪心算法专题 - 堆与哈夫曼编码问题集合

"""
算法思路详解：
1. 贪心策略：哈夫曼编码思想，每次选择重量最小的两堆果子合并
   - 这个策略的核心思想是优先合并较小的果子堆
   - 通过这种方式可以最小化总体力消耗
   - 类似于哈夫曼编码中优先合并频率较低的字符

2. 使用最小堆维护所有果子堆
   - 最小堆能高效地获取当前最小的元素
   - 插入和删除操作的时间复杂度都是O(logn)

3. 每次取出重量最小的两堆果子合并，合并后的重量重新放入堆中
   - 通过堆操作实现高效的元素管理
   - 保证每次都能取到当前最小的两个元素

4. 重复直到只剩下一堆果子，累加合并过程中的体力消耗
   - 通过n-1次合并操作完成所有果子的合并
   - 累加每次合并的消耗得到总消耗

时间复杂度分析：
- 堆初始化时间复杂度：O(n)
- 每次合并操作时间复杂度：O(logn)
- 总共需要n-1次合并操作
- 总体时间复杂度：O(n * logn)

空间复杂度分析：
- 最小堆存储空间：O(n)
- 其他变量存储空间：O(1)
- 总体空间复杂度：O(n)

是否最优解：
- 是，这是处理此类问题的最优解法
- 贪心策略保证了局部最优解能导致全局最优解

工程化最佳实践：
1. 异常处理：检查输入是否为空或格式不正确
2. 边界条件：处理空数组、单个元素等特殊情况
3. 性能优化：使用优先队列维护最小值，避免重复排序
4. 可读性：清晰的变量命名和详细注释，便于维护

极端场景与边界情况处理：
1. 空输入：fruits为空数组
2. 极端值：只有一种果子、所有果子重量相同
3. 重复数据：多个果子堆重量相同
4. 有序/逆序数据：果子堆重量按顺序排列

跨语言实现差异与优化：
1. Java：使用PriorityQueue实现最小堆，性能稳定
2. C++：使用priority_queue实现最小堆，底层实现可能更优化
3. Python：使用heapq实现最小堆，基于二叉堆算法

调试与测试策略：
1. 打印中间过程：在循环中打印当前合并的两堆果子和合并后的重量
2. 用断言验证中间结果：确保每次合并后堆中元素数量正确
3. 性能退化排查：检查堆操作的时间复杂度
4. 边界测试：测试空数组、单元素等边界情况

实际应用场景与拓展：
1. 数据压缩：哈夫曼编码是经典的贪心算法应用
2. 决策树构建：用于特征选择的贪心策略
3. 聚类算法：用于层次聚类的合并策略

算法深入解析：
贪心算法在合并果子问题中的应用体现了其核心思想：
1. 局部最优选择：每次选择重量最小的两堆果子合并
2. 无后效性：当前的选择不会影响之前的状态
3. 最优子结构：问题的最优解包含子问题的最优解
这个问题的关键洞察是，优先合并较小的果子堆能最小化总体力消耗，这与哈夫曼编码的思想一致。
"""


def mergeFruits(fruits):
    """
    合并果子主函数 - 使用贪心算法和最小堆计算最小体力耗费值
    
    算法思路：
    1. 贪心策略：哈夫曼编码思想，每次选择重量最小的两堆果子合并
    2. 使用最小堆维护所有果子堆
    3. 每次取出重量最小的两堆果子合并，合并后的重量重新放入堆中
    
    Args:
        fruits (List[int]): 果子堆重量列表
        fruits[i]表示第i堆果子的重量
    
    Returns:
        int: 最小体力耗费值
    
    时间复杂度: O(n * logn)，其中n是果子种类数
    空间复杂度: O(n)，最小堆的空间
    
    Examples:
        >>> mergeFruits([1, 2, 9])
        15
        >>> mergeFruits([3, 5, 7, 9])
        45
    """
    # 异常处理：检查输入是否为空
    if not fruits:
        return 0
    
    # 边界条件：只有一堆果子，不需要合并
    if len(fruits) == 1:
        return 0
    
    # 使用最小堆维护所有果子堆
    # 时间复杂度：O(n)
    min_heap = fruits[:]
    heapq.heapify(min_heap)
    
    total_cost = 0  # 总体力消耗
    
    # 重复合并直到只剩下一堆果子
    # 需要进行n-1次合并操作
    # 时间复杂度：O(n * logn)
    while len(min_heap) > 1:
        # 取出重量最小的两堆果子
        # 时间复杂度：O(logn)
        first = heapq.heappop(min_heap)
        second = heapq.heappop(min_heap)
        
        # 合并两堆果子
        cost = first + second
        total_cost += cost
        
        # 将合并后的果子堆重新放入堆中
        # 时间复杂度：O(logn)
        heapq.heappush(min_heap, cost)
    
    return total_cost


# 测试函数
if __name__ == "__main__":
    # 测试用例1：一般情况
    fruits1 = [1, 2, 9]
    print("测试用例1结果:", mergeFruits(fruits1))  # 期望输出: 15
    
    # 测试用例2：多个果子堆
    fruits2 = [3, 5, 7, 9]
    print("测试用例2结果:", mergeFruits(fruits2))  # 期望输出: 45
    
    # 测试用例3：相同重量的果子堆
    fruits3 = [1, 1, 1, 1, 1]
    print("测试用例3结果:", mergeFruits(fruits3))  # 期望输出: 12
    
    # 测试用例4：边界情况 - 只有一堆果子
    fruits4 = [5]
    print("测试用例4结果:", mergeFruits(fruits4))  # 期望输出: 0
    
    # 测试用例5：极端情况 - 重量差异很大
    fruits5 = [1, 100]
    print("测试用例5结果:", mergeFruits(fruits5))  # 期望输出: 101


# ==========================================================================================
# 贪心算法专题 - 堆与哈夫曼编码问题集合
# ==========================================================================================


# LeetCode 703. 数据流中的第K大元素
# https://leetcode.cn/problems/kth-largest-element-in-a-stream/
# 题目描述：设计一个找到数据流中第K大元素的类。注意是排序后的第K大元素，不是第K个不同的元素。
# 算法思路：使用最小堆维护前K大的元素，堆顶即为第K大元素

class KthLargest:
    """
    找到数据流中第K大元素的类 - 使用最小堆实现
    
    算法思路：
    1. 使用最小堆维护前K大的元素
    2. 堆顶即为第K大元素
    """
    
    def __init__(self, k, nums):
        """
        初始化类
        
        Args:
            k (int): 第K大元素
            nums (List[int]): 初始数组
        """
        import heapq
        self.k = k
        self.min_heap = []
        # 将初始数组元素添加到最小堆中
        for num in nums:
            self.add(num)
    
    def add(self, val):
        """
        添加新元素到数据流中，并返回第K大元素
        
        Args:
            val (int): 新添加的元素
            
        Returns:
            int: 数据流中的第K大元素
        """
        import heapq
        # 如果堆的大小小于k，直接添加
        if len(self.min_heap) < self.k:
            heapq.heappush(self.min_heap, val)
        # 如果新元素大于堆顶元素，替换堆顶
        elif val > self.min_heap[0]:
            heapq.heappushpop(self.min_heap, val)
        # 返回堆顶元素（第K大元素）
        return self.min_heap[0]


# LeetCode 1046. 最后一块石头的重量
# https://leetcode.cn/problems/last-stone-weight/
# 题目描述：有一堆石头，每块石头的重量都是正整数。每次从中选出两块最重的石头，然后将它们一起粉碎。
# 假设石头的重量分别为x和y，且x <= y。那么粉碎的可能结果如下：
# 如果x == y，那么两块石头都会被完全粉碎；
# 如果x != y，那么重量为x的石头会被完全粉碎，而重量为y的石头新重量为y-x。
# 最后，最多只会剩下一块石头。返回此石头的重量。如果没有石头剩下，就返回0。
# 算法思路：使用最大堆（Python中通过负数实现）维护石头重量，每次取出最大的两个石头进行操作

def lastStoneWeight(stones):
    """
    计算最后一块石头的重量 - 使用最大堆实现
    
    算法思路：
    1. 使用最大堆维护石头重量
    2. 每次取出最大的两个石头进行操作
    
    Args:
        stones (List[int]): 石头重量列表
        
    Returns:
        int: 最后剩下石头的重量，没有则返回0
    
    时间复杂度: O(n * logn)，其中n是石头数量
    空间复杂度: O(n)，堆的空间
    """
    # 转换为负数实现最大堆
    # 时间复杂度：O(n)
    max_heap = [-stone for stone in stones]
    heapq.heapify(max_heap)
    
    # 循环直到堆中只剩0或1个元素
    # 时间复杂度：O(n * logn)
    while len(max_heap) > 1:
        # 取出两个最大的石头
        # 时间复杂度：O(logn)
        stone1 = -heapq.heappop(max_heap)
        stone2 = -heapq.heappop(max_heap)
        
        # 如果重量不同，将差值放回堆中
        # 时间复杂度：O(logn)
        if stone1 != stone2:
            heapq.heappush(max_heap, -(abs(stone1 - stone2)))
    
    # 返回最后剩下的石头重量或0
    return -max_heap[0] if max_heap else 0


# LeetCode 215. 数组中的第K个最大元素
# https://leetcode.cn/problems/kth-largest-element-in-an-array/
# 题目描述：在未排序的数组中找到第k个最大的元素。请注意，你需要找的是数组排序后的第k个最大的元素，而不是第k个不同的元素。
# 算法思路：使用最小堆维护前K大的元素，堆顶即为第K大元素

def findKthLargest(nums, k):
    """
    找到数组中的第K个最大元素 - 使用最小堆实现
    
    算法思路：
    1. 使用最小堆维护前K大的元素
    2. 堆顶即为第K大元素
    
    Args:
        nums (List[int]): 未排序数组
        k (int): 第K大
        
    Returns:
        int: 第K大元素
    
    时间复杂度: O(n * logk)，其中n是数组长度
    空间复杂度: O(k)，堆的空间
    """
    min_heap = []
    
    # 遍历数组
    # 时间复杂度：O(n * logk)
    for num in nums:
        # 如果堆大小小于k，直接添加
        if len(min_heap) < k:
            heapq.heappush(min_heap, num)
        # 如果当前元素大于堆顶，替换堆顶
        elif num > min_heap[0]:
            heapq.heappushpop(min_heap, num)
    
    # 堆顶即为第K大元素
    return min_heap[0]


# LeetCode 347. 前K个高频元素
# https://leetcode.cn/problems/top-k-frequent-elements/
# 题目描述：给你一个整数数组nums和一个整数k，请你返回其中出现频率前k高的元素。你可以按任意顺序返回答案。
# 算法思路：使用最小堆维护前K高频率的元素，堆顶即为第K高频率的元素

def topKFrequent(nums, k):
    """
    找到前K个高频元素 - 使用最小堆实现
    
    算法思路：
    1. 统计每个元素出现的频率
    2. 使用最小堆维护前K高频率的元素
    3. 堆顶即为第K高频率的元素
    
    Args:
        nums (List[int]): 整数数组
        k (int): 要返回的元素个数
        
    Returns:
        List[int]: 前K个高频元素列表
    
    时间复杂度: O(n * logk)，其中n是数组长度
    空间复杂度: O(n + k)，哈希表和堆的空间
    """
    # 统计每个元素出现的频率
    # 时间复杂度：O(n)
    freq_map = {}
    for num in nums:
        freq_map[num] = freq_map.get(num, 0) + 1
    
    # 使用最小堆存储频率前K高的元素
    min_heap = []
    
    # 遍历频率映射
    # 时间复杂度：O(m * logk)，其中m是不同元素的个数
    for num, freq in freq_map.items():
        # 如果堆大小小于k，直接添加(频率,元素)对
        if len(min_heap) < k:
            heapq.heappush(min_heap, (freq, num))
        # 如果当前元素频率大于堆顶元素的频率，替换堆顶
        elif freq > min_heap[0][0]:
            heapq.heappushpop(min_heap, (freq, num))
    
    # 从堆中提取元素
    # 时间复杂度：O(k)
    return [item[1] for item in min_heap]


# LeetCode 973. 最接近原点的K个点
# https://leetcode.cn/problems/k-closest-points-to-origin/
# 题目描述：我们有一个由平面上的点组成的列表points，需要从中找出K个距离原点(0, 0)最近的点。
# 距离可以通过欧几里德距离计算，但为了简化计算，可以使用距离的平方（避免开根号）。
# 算法思路：使用最大堆维护K个最近的点，堆顶即为第K近的点

def kClosest(points, k):
    """
    找到最接近原点的K个点 - 使用最大堆实现
    
    算法思路：
    1. 使用最大堆维护K个最近的点
    2. 堆顶即为第K近的点
    
    Args:
        points (List[List[int]]): 点坐标列表 [[x1, y1], [x2, y2], ...]
        k (int): 要返回的点个数
        
    Returns:
        List[List[int]]: 最接近原点的K个点列表
    
    时间复杂度: O(n * logk)，其中n是点的数量
    空间复杂度: O(k)，堆的空间
    """
    # 使用最大堆（存储负的距离平方，实现最大堆）
    max_heap = []
    
    # 遍历所有点
    # 时间复杂度：O(n * logk)
    for point in points:
        x, y = point
        # 计算距离的平方（避免开根号）
        distance_squared = x * x + y * y
        
        # 如果堆大小小于k，直接添加(负距离平方,点)对
        if len(max_heap) < k:
            heapq.heappush(max_heap, (-distance_squared, point))
        # 如果当前点距离小于堆顶元素的距离，替换堆顶
        elif distance_squared < -max_heap[0][0]:
            heapq.heappushpop(max_heap, (-distance_squared, point))
    
    # 从堆中提取点
    # 时间复杂度：O(k)
    return [item[1] for item in max_heap]


# 测试补充题目
if __name__ == "__main__":
    print("\n=== 测试补充题目 ===")
    
    # 测试LeetCode 703
    print("\n测试LeetCode 703 - 数据流中的第K大元素:")
    kth_largest = KthLargest(3, [4, 5, 8, 2])
    print(kth_largest.add(3))  # 输出: 4
    print(kth_largest.add(5))  # 输出: 5
    print(kth_largest.add(10))  # 输出: 5
    print(kth_largest.add(9))  # 输出: 8
    print(kth_largest.add(4))  # 输出: 8
    
    # 测试LeetCode 1046
    print("\n测试LeetCode 1046 - 最后一块石头的重量:")
    print(lastStoneWeight([2, 7, 4, 1, 8, 1]))  # 输出: 1
    print(lastStoneWeight([1]))  # 输出: 1
    print(lastStoneWeight([]))  # 输出: 0
    
    # 测试LeetCode 215
    print("\n测试LeetCode 215 - 数组中的第K个最大元素:")
    print(findKthLargest([3, 2, 1, 5, 6, 4], 2))  # 输出: 5
    print(findKthLargest([3, 2, 3, 1, 2, 4, 5, 5, 6], 4))  # 输出: 4
    
    # 测试LeetCode 347
    print("\n测试LeetCode 347 - 前K个高频元素:")
    print(topKFrequent([1, 1, 1, 2, 2, 3], 2))  # 输出: [1, 2]或[2, 1]
    print(topKFrequent([1], 1))  # 输出: [1]
    
    # 测试LeetCode 973
    print("\n测试LeetCode 973 - 最接近原点的K个点:")
    print(kClosest([[1, 3], [-2, 2]], 1))  # 输出: [[-2, 2]]
    print(kClosest([[3, 3], [5, -1], [-2, 4]], 2))  # 输出: [[3, 3], [-2, 4]]或[[-2, 4], [3, 3]]