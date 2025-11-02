#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
LeetCode 1222 Queens That Can Attack the King

题目描述：
在一个 8x8 的棋盘上，有一个白色的国王和一些黑色的皇后。
给你一个二维整数数组 queens，其中 queens[i] = [xQueeni, yQueeni] 表示第 i 个黑色皇后在棋盘上的位置。
还给你一个长度为 2 的整数数组 king，其中 king = [xKing, yKing] 表示白色国王的位置。

请你返回能够直接攻击国王的黑色皇后的坐标。你可以以任何顺序返回答案。

解题思路：
我们可以从国王的位置出发，向8个方向（上下左右和4个对角线方向）搜索，
找到第一个遇到的皇后，这个皇后就是能够攻击国王的皇后。

时间复杂度：O(n)
空间复杂度：O(1)
"""

class Solution:
    def queens_attackthe_king(self, queens, king):
        """
        找到能够攻击国王的皇后
        
        Args:
            queens: 皇后位置列表，每个元素为[x, y]
            king: 国王位置[x, y]
            
        Returns:
            能够攻击国王的皇后位置列表
        """
        # 创建集合标记皇后位置
        queen_set = set(map(tuple, queens))
        
        king_x, king_y = king
        
        # 8个方向：上下左右和4个对角线方向
        directions = [
            (-1, 0), (1, 0), (0, -1), (0, 1),  # 上下左右
            (-1, -1), (-1, 1), (1, -1), (1, 1)  # 4个对角线方向
        ]
        
        result = []
        
        # 向8个方向搜索
        for dx, dy in directions:
            # 从国王位置开始，沿着当前方向搜索
            x, y = king_x + dx, king_y + dy
            
            while 0 <= x < 8 and 0 <= y < 8:
                if (x, y) in queen_set:
                    # 找到皇后，添加到结果中
                    result.append([x, y])
                    break  # 找到第一个皇后后停止搜索这个方向
                x += dx
                y += dy
        
        return result


# 测试方法
def main():
    solution = Solution()
    
    # 测试用例1
    queens1 = [[0,1],[1,0],[4,0],[0,4],[3,3],[2,4]]
    king1 = [0,0]
    print("测试用例1:")
    print("皇后位置:", queens1)
    print("国王位置:", king1)
    print("结果:", solution.queens_attackthe_king(queens1, king1))
    print()
    
    # 测试用例2
    queens2 = [[0,0],[1,1],[2,2],[3,4],[3,5],[4,4],[4,5]]
    king2 = [3,3]
    print("测试用例2:")
    print("皇后位置:", queens2)
    print("国王位置:", king2)
    print("结果:", solution.queens_attackthe_king(queens2, king2))
    print()
    
    # 测试用例3
    queens3 = [[5,6],[7,7],[2,1],[0,7],[1,6],[5,1],[3,7],[0,3],[4,0],[1,2],[6,3],[5,0],[0,4],[2,2],[1,1]]
    king3 = [3,4]
    print("测试用例3:")
    print("皇后位置:", queens3)
    print("国王位置:", king3)
    print("结果:", solution.queens_attackthe_king(queens3, king3))


if __name__ == "__main__":
    main()