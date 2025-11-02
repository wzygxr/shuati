from typing import List
from collections import deque

"""
LeetCode 773 - 滑动谜题
题目描述：
在一个 2x3 的板上（board）有 5 个砖块，以及一个空的格子。
一次移动定义为选择空的格子与一个相邻的数字（上下左右）进行交换。
最后当板 board 的结果是 [[1,2,3],[4,5,0]] 时，返回最少的移动次数；如果不存在这样的结果，返回 -1。

算法：01-BFS算法
时间复杂度：O(6! * 4) = O(720 * 4) = O(2880)，因为2x3的板共有6个位置，所以状态总数为6!
空间复杂度：O(6!) = O(720)
"""

def sliding_puzzle(board: List[List[int]]) -> int:
    """
    解决滑动谜题问题的主函数
    
    Args:
        board: 2x3的棋盘，表示为二维列表
    
    Returns:
        最少的移动次数，如果无解则返回-1
    """
    # 目标状态
    TARGET = "123450"
    
    # 记录每个位置可以移动到的位置（0-5对应board中的每个位置）
    DIRECTIONS = [
        [1, 3],       # 位置0可以移动到位置1和3
        [0, 2, 4],    # 位置1可以移动到位置0、2和4
        [1, 5],       # 位置2可以移动到位置1和5
        [0, 4],       # 位置3可以移动到位置0和4
        [1, 3, 5],    # 位置4可以移动到位置1、3和5
        [2, 4]        # 位置5可以移动到位置2和4
    ]
    
    # 将棋盘转换为字符串表示
    start = "".join(str(num) for row in board for num in row)
    
    # 如果初始状态就是目标状态，直接返回0
    if start == TARGET:
        return 0
    
    # 使用双端队列实现01-BFS
    # 由于每次移动的代价都是1，所以这里可以简化为普通BFS
    dq = deque([start])
    # 记录已经访问过的状态，避免重复访问
    visited = set([start])
    
    steps = 0
    
    while dq:
        size = len(dq)
        steps += 1
        
        # 处理当前层的所有状态
        for _ in range(size):
            current = dq.popleft()
            
            # 找到空格（0）的位置
            zero_pos = current.index('0')
            
            # 尝试所有可能的移动方向
            for dir_pos in DIRECTIONS[zero_pos]:
                # 生成新的状态
                # 将字符串转换为列表以便交换字符
                chars = list(current)
                chars[zero_pos], chars[dir_pos] = chars[dir_pos], chars[zero_pos]
                next_state = "".join(chars)
                
                # 如果是目标状态，返回步数
                if next_state == TARGET:
                    return steps
                
                # 如果是新状态，加入队列
                if next_state not in visited:
                    visited.add(next_state)
                    dq.append(next_state)
    
    # 如果无法到达目标状态，返回-1
    return -1

# 测试函数
def test_sliding_puzzle():
    # 测试用例1
    board1 = [[1, 2, 3], [4, 0, 5]]
    result1 = sliding_puzzle(board1)
    assert result1 == 1
    print(f"测试用例1通过: 预期结果=1, 实际结果={result1}")
    
    # 测试用例2
    board2 = [[1, 2, 3], [5, 4, 0]]
    result2 = sliding_puzzle(board2)
    assert result2 == -1
    print(f"测试用例2通过: 预期结果=-1, 实际结果={result2}")
    
    # 测试用例3
    board3 = [[4, 1, 2], [5, 0, 3]]
    result3 = sliding_puzzle(board3)
    assert result3 == 5
    print(f"测试用例3通过: 预期结果=5, 实际结果={result3}")
    
    # 测试用例4：初始状态就是目标状态
    board4 = [[1, 2, 3], [4, 5, 0]]
    result4 = sliding_puzzle(board4)
    assert result4 == 0
    print(f"测试用例4通过: 预期结果=0, 实际结果={result4}")

# 运行测试
if __name__ == "__main__":
    test_sliding_puzzle()
    print("所有测试用例通过！")