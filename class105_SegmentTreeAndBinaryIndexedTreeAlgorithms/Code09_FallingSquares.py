# LeetCode 699. 掉落的方块
# 在无限长的数轴（坐标轴）上，我们根据给定的顺序放置“方块”。
# 第 i 个方块的边长是 squares[i] = [left, sideLength]，其中 left 表示该方块最左边的 x 坐标，
# sideLength 表示边长。
# 每个方块从一个比目前所有落下的方块更高的高度掉落而下，直到着陆到另一个正方形的顶边或者数轴上。
# 我们可以认为只有一个方块的底部边平行于数轴。
# 返回一个数组 ans，其中 ans[i] 表示在第 i 个方块掉落后，当前所有落下的方块堆叠的最高高度。
# 测试链接: https://leetcode.cn/problems/falling-squares/

from typing import List

def fallingSquares(positions: List[List[int]]) -> List[int]:
    """
    使用区间列表解决掉落的方块问题
    
    解题思路:
    1. 维护所有已放置方块的区间信息
    2. 对于每个新掉落的方块，检查与已有方块的重叠情况
    3. 计算堆叠高度并更新最大高度
    
    时间复杂度分析:
    - 每次查询和更新: O(n)
    - 总时间复杂度: O(n²)
    
    空间复杂度分析:
    - 区间列表: O(n)
    - 结果数组: O(n)
    - 总空间复杂度: O(n)
    
    工程化考量:
    1. 区间重叠检测
    2. 边界条件处理
    3. 详细注释和变量命名
    """
    # 存储已放置的方块信息: [left, right, height]
    intervals = []
    result = []
    max_height = 0
    
    for left, side_length in positions:
        right = left + side_length
        
        # 找到底部区域内的最大高度
        current_height = 0
        for l, r, h in intervals:
            # 检查是否有重叠
            if left < r and right > l:  # 两个区间重叠的条件
                current_height = max(current_height, h)
        
        # 计算新的高度
        new_height = current_height + side_length
        
        # 添加新的方块到列表
        intervals.append([left, right, new_height])
        
        # 更新最大高度
        max_height = max(max_height, new_height)
        result.append(max_height)
    
    return result


# 测试函数
def test():
    # 测试用例1
    positions1 = [[1, 2], [2, 3], [6, 1]]
    result1 = fallingSquares(positions1)
    print("Test case 1:")
    print("Input: [[1, 2], [2, 3], [6, 1]]")
    print("Output:", result1)
    # 期望输出: [2, 5, 5]
    
    # 测试用例2
    positions2 = [[100, 100], [200, 100]]
    result2 = fallingSquares(positions2)
    print("\nTest case 2:")
    print("Input: [[100, 100], [200, 100]]")
    print("Output:", result2)
    # 期望输出: [100, 100]
    
    # 测试用例3
    positions3 = [[1, 5], [2, 2], [4, 3]]
    result3 = fallingSquares(positions3)
    print("\nTest case 3:")
    print("Input: [[1, 5], [2, 2], [4, 3]]")
    print("Output:", result3)
    # 期望输出: [5, 7, 7]


if __name__ == "__main__":
    test()