from collections import deque
from typing import List

# 滑动谜题
# 在一个 2 x 3 的板上（board）有 5 块砖瓦，用数字 1~5 来表示, 以及一块空缺用 0 来表示。
# 一次移动定义为选择 0 与一个相邻的数字（上下左右）进行交换。
# 最终当板 board 的结果是 [[1,2,3],[4,5,0]] 谜板被解开。
# 给出一个谜板的初始状态，返回最少可以通过多少次移动解开谜板，如果不能解开谜板，则返回 -1 。
# 测试链接 : https://leetcode.cn/problems/sliding-puzzle/
# 
# 算法思路：
# 使用BFS搜索从初始状态到目标状态的最短路径。将2x3的板状态表示为字符串进行状态搜索。
# 每个状态可以生成最多4个邻居状态（0可以向4个方向移动）。
# 
# 时间复杂度：O(6! * 4) = O(2880)，因为有6! = 720种可能的状态，每个状态最多有4个邻居
# 空间复杂度：O(720)，用于存储队列和访问状态
# 
# 工程化考量：
# 1. 状态表示：将2x3矩阵转换为字符串进行状态搜索
# 2. 邻居生成：根据0的位置生成可能的移动方向
# 3. 预计算移动方向：提高代码可读性和性能
# 4. 边界情况：初始状态就是目标状态
class Solution:
    def slidingPuzzle(self, board: List[List[int]]) -> int:
        target = "123450"
        start = self._board_to_string(board)
        
        # 边界情况：初始状态就是目标状态
        if start == target:
            return 0
        
        # 预计算每个位置可以移动到的邻居位置
        neighbors = [
            [1, 3],     # 位置0的邻居：1, 3
            [0, 2, 4],  # 位置1的邻居：0, 2, 4
            [1, 5],     # 位置2的邻居：1, 5
            [0, 4],     # 位置3的邻居：0, 4
            [1, 3, 5],  # 位置4的邻居：1, 3, 5
            [2, 4]      # 位置5的邻居：2, 4
        ]
        
        queue = deque()
        visited = set()
        
        queue.append(start)
        visited.add(start)
        steps = 0
        
        while queue:
            steps += 1
            size = len(queue)
            
            for _ in range(size):
                current = queue.popleft()
                
                # 找到0的位置
                zero_pos = current.index('0')
                
                # 生成所有可能的邻居状态
                for neighbor_pos in neighbors[zero_pos]:
                    # 交换0和邻居位置
                    chars = list(current)
                    chars[zero_pos], chars[neighbor_pos] = chars[neighbor_pos], chars[zero_pos]
                    next_state = ''.join(chars)
                    
                    if next_state in visited:
                        continue
                    
                    if next_state == target:
                        return steps
                    
                    visited.add(next_state)
                    queue.append(next_state)
        
        return -1
    
    def _board_to_string(self, board: List[List[int]]) -> str:
        """将2x3矩阵转换为字符串"""
        return ''.join(str(num) for row in board for num in row)

# 单元测试
def test_sliding_puzzle():
    solution = Solution()
    
    # 测试用例1：标准情况
    board1 = [[1,2,3],[4,0,5]]
    assert solution.slidingPuzzle(board1) == 1, "测试用例1失败"
    print("测试用例1通过")
    
    # 测试用例2：需要多步
    board2 = [[1,2,3],[5,4,0]]
    assert solution.slidingPuzzle(board2) == -1, "测试用例2失败"
    print("测试用例2通过")
    
    # 测试用例3：初始状态就是目标状态
    board3 = [[1,2,3],[4,5,0]]
    assert solution.slidingPuzzle(board3) == 0, "测试用例3失败"
    print("测试用例3通过")
    
    # 测试用例4：复杂情况
    board4 = [[4,1,2],[5,0,3]]
    assert solution.slidingPuzzle(board4) == 5, "测试用例4失败"
    print("测试用例4通过")
    
    print("所有测试用例通过！")

if __name__ == "__main__":
    test_sliding_puzzle()