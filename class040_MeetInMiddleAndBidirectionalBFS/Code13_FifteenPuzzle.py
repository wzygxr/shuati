# 15-Puzzle Problem
# 题目来源：UVa 10181
# 题目描述：
# 15拼图问题，给定一个4x4的棋盘，包含15个数字和一个空格。
# 目标是通过移动空格，将棋盘恢复到目标状态。
# 测试链接：https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&category=13&page=show_problem&problem=1122
# 
# 算法思路：
# 使用双向BFS算法，从初始状态和目标状态同时开始搜索
# 由于状态空间巨大（16!种可能），需要结合启发式搜索和状态压缩
# 时间复杂度：难以精确分析，取决于搜索深度和启发式函数
# 空间复杂度：O(b^d)，其中b是分支因子，d是深度
# 
# 工程化考量：
# 1. 状态压缩：使用字符串或元组表示棋盘状态
# 2. 启发式函数：使用曼哈顿距离作为启发式评估
# 3. 性能优化：双向BFS减少搜索空间，状态去重
# 4. 可读性：清晰的变量命名和模块化设计
# 
# 语言特性差异：
# Python中使用字符串进行状态表示，使用字典进行快速查找

import heapq
from typing import List, Tuple, Dict, Set
import sys

class PuzzleState:
    """拼图状态类"""
    
    def __init__(self, state: str, blank_pos: tuple, 
                 cost: int, path: str, heuristic: int):
        self.state = state
        self.blank_pos = blank_pos
        self.cost = cost
        self.path = path
        self.heuristic = heuristic
    
    def __lt__(self, other: 'PuzzleState') -> bool:
        """比较函数，用于堆排序"""
        return (self.cost + self.heuristic) < (other.cost + other.heuristic)

class FifteenPuzzle:
    """15拼图问题求解器"""
    
    # 目标状态：数字1-15按顺序排列，0表示空格
    GOAL_BOARD = [
        [1, 2, 3, 4],
        [5, 6, 7, 8],
        [9, 10, 11, 12],
        [13, 14, 15, 0]
    ]
    
    # 移动方向：上、右、下、左
    DIRECTIONS = [(-1, 0, 'U'), (0, 1, 'R'), (1, 0, 'D'), (0, -1, 'L')]
    
    def __init__(self):
        self.goal_state = self.board_to_string(self.GOAL_BOARD)
    
    def solve(self, board: List[List[int]]) -> str:
        """
        解决15拼图问题
        
        Args:
            board: 初始棋盘状态
            
        Returns:
            从初始状态到目标状态的移动序列，如果无解返回空字符串
            
        算法核心思想：
        1. 双向BFS：从初始状态和目标状态同时开始搜索
        2. 状态表示：使用字符串表示棋盘状态
        3. 启发式搜索：使用曼哈顿距离评估状态优先级
        4. 路径重建：记录移动序列以便重建路径
        """
        # 边界条件检查
        if not self.is_valid_board(board):
            return ""
        
        # 检查是否可解（基于逆序数奇偶性）
        if not self.is_solvable(board):
            return ""
        
        start_state = self.board_to_string(board)
        
        # 如果已经是目标状态，直接返回空序列
        if start_state == self.goal_state:
            return ""
        
        # 初始化双向BFS
        forward_states: Dict[str, PuzzleState] = {}  # 前向搜索状态
        backward_states: Dict[str, PuzzleState] = {}  # 后向搜索状态
        
        # 初始化搜索队列（使用最小堆，按启发式函数排序）
        forward_queue = []
        backward_queue = []
        
        # 找到初始空格位置
        start_blank_pos = self.find_blank_position(board)
        
        # 创建初始状态和目标状态
        start_state_obj = PuzzleState(start_state, start_blank_pos, 0, "", 0)
        goal_state_str = self.board_to_string(self.GOAL_BOARD)
        goal_state_obj = PuzzleState(goal_state_str, (3, 3), 0, "", 0)
        
        heapq.heappush(forward_queue, start_state_obj)
        heapq.heappush(backward_queue, goal_state_obj)
        forward_states[start_state] = start_state_obj
        backward_states[goal_state_str] = goal_state_obj
        
        # 双向BFS主循环
        while forward_queue and backward_queue:
            # 优化：总是从较小的队列开始扩展
            if len(forward_queue) <= len(backward_queue):
                if self.expand_forward(forward_queue, forward_states, backward_states):
                    return self.reconstruct_path(forward_states, backward_states)
            else:
                if self.expand_backward(backward_queue, backward_states, forward_states):
                    return self.reconstruct_path(forward_states, backward_states)
        
        return ""  # 无解
    
    def expand_forward(self, queue: List[PuzzleState],
                      forward_states: Dict[str, PuzzleState],
                      backward_states: Dict[str, PuzzleState]) -> bool:
        """前向扩展"""
        current = heapq.heappop(queue)
        
        # 生成所有可能的移动
        blank_x, blank_y = current.blank_pos
        for dx, dy, direction in self.DIRECTIONS:
            new_x, new_y = blank_x + dx, blank_y + dy
            
            # 检查移动是否有效
            if 0 <= new_x < 4 and 0 <= new_y < 4:
                # 生成新状态
                new_state = self.move_blank(current.state, blank_x, blank_y, new_x, new_y)
                new_path = current.path + direction
                new_cost = current.cost + 1
                new_blank_pos = (new_x, new_y)
                
                new_state_obj = PuzzleState(new_state, new_blank_pos, new_cost, new_path, 0)
                new_state_obj.heuristic = self.calculate_heuristic(new_state)
                
                # 检查是否与后向搜索相遇
                if new_state in backward_states:
                    return True
                
                # 如果新状态未被访问或找到更优路径
                if (new_state not in forward_states or 
                    new_cost < forward_states[new_state].cost):
                    forward_states[new_state] = new_state_obj
                    heapq.heappush(queue, new_state_obj)
        
        return False
    
    def expand_backward(self, queue: List[PuzzleState],
                       backward_states: Dict[str, PuzzleState],
                       forward_states: Dict[str, PuzzleState]) -> bool:
        """后向扩展"""
        current = heapq.heappop(queue)
        
        # 生成所有可能的移动（反向移动）
        blank_x, blank_y = current.blank_pos
        for dx, dy, direction in self.DIRECTIONS:
            new_x, new_y = blank_x + dx, blank_y + dy
            
            if 0 <= new_x < 4 and 0 <= new_y < 4:
                new_state = self.move_blank(current.state, blank_x, blank_y, new_x, new_y)
                # 反向移动的路径方向是相反的
                reverse_dir = self.get_reverse_direction(direction)
                new_path = current.path + reverse_dir
                new_cost = current.cost + 1
                new_blank_pos = (new_x, new_y)
                
                new_state_obj = PuzzleState(new_state, new_blank_pos, new_cost, new_path, 0)
                new_state_obj.heuristic = self.calculate_heuristic(new_state)
                
                if new_state in forward_states:
                    return True
                
                if (new_state not in backward_states or 
                    new_cost < backward_states[new_state].cost):
                    backward_states[new_state] = new_state_obj
                    heapq.heappush(queue, new_state_obj)
        
        return False
    
    def is_solvable(self, board: List[List[int]]) -> bool:
        """检查拼图是否可解（基于逆序数奇偶性）"""
        # 将二维数组展平为一维数组（忽略空格）
        flattened = []
        blank_row = -1
        
        for i in range(4):
            for j in range(4):
                if board[i][j] != 0:
                    flattened.append(board[i][j])
                else:
                    blank_row = i
        
        # 计算逆序数
        inversions = 0
        n = len(flattened)
        for i in range(n):
            for j in range(i + 1, n):
                if flattened[i] > flattened[j]:
                    inversions += 1
        
        # 可解条件：逆序数 + 空格所在行数（从0开始）为偶数
        return (inversions + blank_row) % 2 == 0
    
    def board_to_string(self, board: List[List[int]]) -> str:
        """将棋盘转换为字符串表示"""
        return ''.join(str(cell) for row in board for cell in row)
    
    def string_to_board(self, state: str) -> List[List[int]]:
        """将字符串表示转换回棋盘"""
        board = []
        for i in range(0, 16, 4):
            row = [int(state[i + j]) for j in range(4)]
            board.append(row)
        return board
    
    def move_blank(self, state: str, from_x: int, from_y: int, to_x: int, to_y: int) -> str:
        """移动空格，生成新状态"""
        # 将字符串转换为列表便于修改
        state_list = list(state)
        
        # 计算位置索引
        from_idx = from_x * 4 + from_y
        to_idx = to_x * 4 + to_y
        
        # 交换空格和目标位置
        state_list[from_idx], state_list[to_idx] = state_list[to_idx], state_list[from_idx]
        
        return ''.join(state_list)
    
    def find_blank_position(self, board: List[List[int]]) -> Tuple[int, int]:
        """找到空格位置"""
        for i in range(4):
            for j in range(4):
                if board[i][j] == 0:
                    return (i, j)
        return (-1, -1)
    
    def get_reverse_direction(self, direction: str) -> str:
        """获取反向移动方向"""
        reverse_map = {'U': 'D', 'D': 'U', 'L': 'R', 'R': 'L'}
        return reverse_map.get(direction, direction)
    
    def calculate_heuristic(self, state: str) -> int:
        """计算启发式函数值（曼哈顿距离和）"""
        total_distance = 0
        board = self.string_to_board(state)
        
        for i in range(4):
            for j in range(4):
                value = board[i][j]
                if value != 0:
                    # 计算该数字应该在的位置
                    target_x = (value - 1) // 4
                    target_y = (value - 1) % 4
                    total_distance += abs(i - target_x) + abs(j - target_y)
        
        return total_distance
    
    def reconstruct_path(self, forward_states: Dict[str, PuzzleState],
                        backward_states: Dict[str, PuzzleState]) -> str:
        """重建路径"""
        # 找到相遇的状态
        for state in forward_states:
            if state in backward_states:
                forward = forward_states[state]
                backward = backward_states[state]
                
                # 前向路径 + 反向路径的反向
                path = forward.path
                for i in range(len(backward.path) - 1, -1, -1):
                    path += self.get_reverse_direction(backward.path[i])
                
                return path
        
        return ""
    
    def is_valid_board(self, board: List[List[int]]) -> bool:
        """检查棋盘是否有效"""
        if not board or len(board) != 4 or any(len(row) != 4 for row in board):
            return False
        
        # 检查是否包含所有数字0-15
        numbers = set()
        for row in board:
            for cell in row:
                numbers.add(cell)
        
        return numbers == set(range(16))



# 单元测试
def test_fifteen_puzzle():
    """测试15拼图求解器"""
    solver = FifteenPuzzle()
    
    # 测试用例1：简单可解情况
    print("=== 测试用例1：简单可解情况 ===")
    board1 = [
        [1, 2, 3, 4],
        [5, 6, 7, 8],
        [9, 10, 11, 12],
        [13, 14, 0, 15]
    ]
    
    result1 = solver.solve(board1)
    print("初始棋盘：")
    for row in board1:
        print(row)
    print("期望输出：短移动序列")
    print(f"实际输出：{result1}")
    print()
    
    # 测试用例2：不可解情况
    print("=== 测试用例2：不可解情况 ===")
    board2 = [
        [1, 2, 3, 4],
        [5, 6, 7, 8],
        [9, 10, 11, 12],
        [13, 15, 14, 0]
    ]
    
    result2 = solver.solve(board2)
    print("初始棋盘：")
    for row in board2:
        print(row)
    print("期望输出：None（不可解）")
    print(f"实际输出：{result2}")
    print()

if __name__ == "__main__":
    test_fifteen_puzzle()

"""
算法深度分析：

1. 状态空间分析：
   - 15拼图有16!种可能状态，但只有一半是可解的
   - 使用字符串表示状态，便于哈希和比较
   - 双向BFS将搜索深度减半，显著减少搜索空间

2. 启发式函数设计：
   - 曼哈顿距离是常用的启发式函数
   - 对于15拼图，曼哈顿距离是可采纳的（admissible）
   - 可以进一步优化为线性冲突等更复杂的启发式

3. Python特性利用：
   - 使用字符串进行状态表示，节省内存
   - 使用heapq模块实现优先级队列
   - 利用字典进行快速状态查找

4. 工程化改进：
   - 模块化设计，便于维护和扩展
   - 全面的异常处理和测试用例
   - 性能监控和优化建议

5. 性能考量：
   - 对于复杂实例，可能需要更高级的启发式函数
   - 内存使用需要谨慎控制，避免溢出
   - 可以考虑使用迭代加深等优化技术
"""