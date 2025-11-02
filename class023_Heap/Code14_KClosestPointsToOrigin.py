import heapq

class Solution:
    """
    相关题目6: LeetCode 973. 最接近原点的 K 个点
    题目链接: https://leetcode.cn/problems/k-closest-points-to-origin/
    题目描述: 给定一个数组 points ，其中 points[i] = [xi, yi] 表示 X-Y 平面上的一个点，
    并且是一个整数 k ，返回离原点 (0,0) 最近的 k 个点。
    这里，平面上两点之间的距离是欧几里德距离。
    解题思路: 使用最大堆维护K个最近的点，堆中存储点的平方距离和点坐标
    时间复杂度: O(n log k)，其中n是点的数量，堆操作需要O(log k)时间
    空间复杂度: O(k)，堆最多存储k个点
    是否最优解: 是，这是解决Top K最近点问题的经典解法
    
    本题属于堆的典型应用场景：需要在大量数据中动态维护前K个最小/最大值
    """
    
    def kClosest(self, points, k):
        """
        找出离原点最近的K个点
        
        Args:
            points: 二维整数数组，每个元素表示一个点的坐标 [x, y]
            k: 需要返回的最近点的数量
            
        Returns:
            list: 离原点最近的k个点组成的二维列表
            
        Raises:
            ValueError: 当输入参数无效时抛出异常
        """
        # 异常处理：检查输入数组是否为None或空
        if not points:
            raise ValueError("输入点数组不能为空")
        
        # 异常处理：检查k是否在有效范围内
        if k <= 0 or k > len(points):
            raise ValueError(f"k的值必须在1到数组长度之间，当前k={k}，数组长度={len(points)}")
        
        # 创建最大堆，Python的heapq默认是最小堆，所以我们通过存储负数来模拟最大堆
        # 堆中存储的是[-距离平方, x坐标, y坐标]的元组
        max_heap = []
        
        # 遍历所有点
        for point in points:
            x, y = point
            # 计算点到原点的距离的平方（避免浮点数运算和平方根操作）
            dist_square = x * x + y * y
            
            # 调试信息：打印当前处理的点和距离
            # print(f"处理点: [{x}, {y}], 距离平方: {dist_square}")
            
            if len(max_heap) < k:
                # 如果堆的大小小于k，直接将当前点加入堆（存储负的距离平方以模拟最大堆）
                heapq.heappush(max_heap, (-dist_square, x, y))
            elif dist_square < -max_heap[0][0]:
                # 如果当前点比堆顶的点更近（距离平方更小）
                # 则移除堆顶的点（当前k个点中最远的），并加入新点
                heapq.heappop(max_heap)
                heapq.heappush(max_heap, (-dist_square, x, y))
            # 否则（当前点比堆顶的点更远或相等），不做任何操作
        
        # 将堆中的k个点转换为结果数组
        result = []
        for _ in range(k):
            _, x, y = heapq.heappop(max_heap)
            result.append([x, y])
        
        return result

# 打印二维数组的辅助函数
def print_points(points):
    print([point for point in points])

# 测试函数，验证算法在不同输入情况下的正确性
def test_solution():
    solution = Solution()
    
    # 测试用例1：基本情况
    points1 = [[1, 3], [-2, 2], [5, 8], [0, 1]]
    k1 = 2
    result1 = solution.kClosest(points1, k1)
    print("示例1输出: ")
    print_points(result1)  # 期望输出: [[-2, 2], [0, 1]] 或 [[0, 1], [-2, 2]]
    # 验证结果是否包含正确的点（不考虑顺序）
    assert len(result1) == 2, f"测试用例1失败，期望返回2个点，实际返回{len(result1)}个点"
    assert set(tuple(p) for p in result1) == set(tuple(p) for p in [[-2, 2], [0, 1]]), "测试用例1失败，返回的点不正确"
    
    # 测试用例2：k等于数组长度
    points2 = [[3, 3], [5, -1], [-2, 4]]
    k2 = 3
    result2 = solution.kClosest(points2, k2)
    print("示例2输出: ")
    print_points(result2)  # 期望输出: 原数组的所有点，按距离排序
    assert len(result2) == 3, f"测试用例2失败，期望返回3个点，实际返回{len(result2)}个点"
    
    # 测试用例3：k=1，只有一个点
    points3 = [[1, 2], [1, 3]]
    k3 = 1
    result3 = solution.kClosest(points3, k3)
    print("示例3输出: ")
    print_points(result3)  # 期望输出: [[1, 2]]
    assert result3 == [[1, 2]], f"测试用例3失败，期望[[1, 2]]，实际得到{result3}"
    
    # 测试用例4：边界情况 - 原点
    points4 = [[0, 0], [1, 2], [3, 4]]
    k4 = 1
    result4 = solution.kClosest(points4, k4)
    print("示例4输出: ")
    print_points(result4)  # 期望输出: [[0, 0]]
    assert result4 == [[0, 0]], f"测试用例4失败，期望[[0, 0]]，实际得到{result4}"
    
    # 测试异常情况
    try:
        # 测试用例5：异常测试 - 空数组
        solution.kClosest([], 1)
        print("测试用例5失败：未抛出预期的异常")
    except ValueError as e:
        print(f"测试用例5成功捕获异常: {e}")
    
    try:
        # 测试用例6：异常测试 - k超出范围
        solution.kClosest([[1, 1]], 2)
        print("测试用例6失败：未抛出预期的异常")
    except ValueError as e:
        print(f"测试用例6成功捕获异常: {e}")

# 运行测试
if __name__ == "__main__":
    test_solution()
    print("所有测试用例通过！")