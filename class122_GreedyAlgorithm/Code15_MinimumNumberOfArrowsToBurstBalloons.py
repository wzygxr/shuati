# 用最少数量的箭引爆气球
# 一些球形的气球贴在一堵用 XY 平面表示的墙面上。墙面上的气球记录在整数数组 points，
# 其中 points[i] = [xstart, xend] 表示水平直径在 xstart 和 xend 之间的气球。
# 你不知道气球的确切 y 坐标。
# 一支弓箭可以沿着 x 轴从不同点完全垂直地射出。在坐标 x 处射出一支箭，
# 若有一个气球的直径的开始和结束坐标为 xstart，xend，且满足 xstart ≤ x ≤ xend，
# 则该气球会被引爆。可以射出的弓箭的数量没有限制。弓箭一旦被射出之后，可以无限地前进。
# 给你一个数组 points，返回引爆所有气球所必须射出的最小弓箭数。
# 测试链接 : https://leetcode.cn/problems/minimum-number-of-arrows-to-burst-balloons/

def findMinArrowShots(points):
    """
    用最少数量的箭引爆气球
    
    算法思路：
    使用贪心策略：
    1. 按照气球的结束位置进行升序排序
    2. 贪心选择结束位置最早的气球，射出一支箭
    3. 这支箭能引爆所有与该气球重叠的气球
    4. 继续处理未被引爆的气球
    
    正确性分析：
    1. 为了使用最少的箭，我们应该尽可能多地引爆气球
    2. 按结束位置排序后，选择结束位置最早的气球射箭
    3. 这样可以保证与该气球重叠的所有气球都被引爆
    
    时间复杂度：O(n*logn) - 主要是排序的时间复杂度
    空间复杂度：O(logn) - 排序所需的额外空间
    
    :param points: 气球坐标数组
    :return: 最少弓箭数
    """
    # 边界情况处理
    if not points:
        return 0
    
    # 按照气球的结束位置进行升序排序
    points.sort(key=lambda x: x[1])
    
    arrows = 1           # 至少需要一支箭
    end = points[0][1]   # 第一支箭的位置
    
    # 从第二个气球开始遍历
    for i in range(1, len(points)):
        # 如果当前气球的开始位置大于箭的位置，说明需要新的箭
        if points[i][0] > end:
            arrows += 1
            end = points[i][1]
        # 否则当前箭可以引爆这个气球，不需要额外操作
    
    return arrows

# 测试用例
if __name__ == "__main__":
    # 测试用例1: points = [[10,16],[2,8],[1,6],[7,12]] -> 输出: 2
    points1 = [[10, 16], [2, 8], [1, 6], [7, 12]]
    print("测试用例1:")
    print("气球坐标:", points1)
    print("最少弓箭数:", findMinArrowShots(points1))  # 期望输出: 2
    
    # 测试用例2: points = [[1,2],[3,4],[5,6],[7,8]] -> 输出: 4
    points2 = [[1, 2], [3, 4], [5, 6], [7, 8]]
    print("\n测试用例2:")
    print("气球坐标:", points2)
    print("最少弓箭数:", findMinArrowShots(points2))  # 期望输出: 4
    
    # 测试用例3: points = [[1,2],[2,3],[3,4],[4,5]] -> 输出: 2
    points3 = [[1, 2], [2, 3], [3, 4], [4, 5]]
    print("\n测试用例3:")
    print("气球坐标:", points3)
    print("最少弓箭数:", findMinArrowShots(points3))  # 期望输出: 2
    
    # 测试用例4: points = [] -> 输出: 0
    points4 = []
    print("\n测试用例4:")
    print("气球坐标:", points4)
    print("最少弓箭数:", findMinArrowShots(points4))  # 期望输出: 0