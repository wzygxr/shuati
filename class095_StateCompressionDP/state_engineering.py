#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
状态工程 (State Engineering)

技术原理：
状态工程是一种通过状态压缩和状态管理来优化算法性能的技术。
主要包括位压缩、Zobrist哈希、状态去重等方法，用于减少状态表示的空间
和提高状态比较的效率。

技术特点：
1. 位压缩：使用位运算表示状态，节省空间
2. 状态哈希：快速比较和查找状态
3. 状态去重：避免重复计算相同状态
4. 高效状态转移：快速生成后继状态

应用场景：
- 棋盘游戏状态表示
- 动态规划状态压缩
- 搜索算法状态管理
- 游戏AI状态评估

核心技术：
1. 位压缩(bitset/uint128)：用位表示状态
2. Zobrist哈希：状态去重和快速比较
3. 状态缓存：避免重复计算

时间复杂度：取决于具体应用
空间复杂度：通常比传统表示方法更优
"""

from typing import List, Dict, TypeVar, Generic, Optional, Tuple
import random


class BitCompression:
    """位压缩工具类"""
    
    @staticmethod
    def set_bit(state: int, bit: int) -> int:
        """
        设置指定位为1
        
        Args:
            state: 当前状态
            bit: 位索引
            
        Returns:
            更新后的状态
        """
        return state | (1 << bit)
    
    @staticmethod
    def clear_bit(state: int, bit: int) -> int:
        """
        清除指定位（设为0）
        
        Args:
            state: 当前状态
            bit: 位索引
            
        Returns:
            更新后的状态
        """
        return state & ~(1 << bit)
    
    @staticmethod
    def is_bit_set(state: int, bit: int) -> bool:
        """
        检查指定位是否为1
        
        Args:
            state: 当前状态
            bit: 位索引
            
        Returns:
            是否为1
        """
        return (state & (1 << bit)) != 0
    
    @staticmethod
    def toggle_bit(state: int, bit: int) -> int:
        """
        翻转指定位
        
        Args:
            state: 当前状态
            bit: 位索引
            
        Returns:
            更新后的状态
        """
        return state ^ (1 << bit)
    
    @staticmethod
    def count_bits(state: int) -> int:
        """
        计算状态中1的个数（汉明重量）
        
        Args:
            state: 状态
            
        Returns:
            1的个数
        """
        return bin(state).count('1')
    
    @staticmethod
    def get_lowest_bit_position(state: int) -> int:
        """
        获取最低位的1的位置
        
        Args:
            state: 状态
            
        Returns:
            最低位1的位置，如果没有1则返回-1
        """
        if state == 0:
            return -1
        return (state & -state).bit_length() - 1
    
    @staticmethod
    def print_binary(state: int) -> None:
        """
        打印二进制表示
        
        Args:
            state: 状态
        """
        print(bin(state))


class ZobristHashing:
    """Zobrist哈希工具类"""
    
    BOARD_SIZE = 64  # 假设64位棋盘
    MAX_PIECE_TYPES = 16  # 最多16种棋子类型
    _zobrist_table: List[List[int]] = []
    
    @classmethod
    def initialize_zobrist_table(cls) -> None:
        """初始化Zobrist哈希表"""
        cls._zobrist_table = [[0 for _ in range(cls.MAX_PIECE_TYPES)] 
                             for _ in range(cls.BOARD_SIZE)]
        random.seed(12345)  # 固定种子以保证一致性
        
        # 为每个位置和每种棋子类型生成随机数
        for pos in range(cls.BOARD_SIZE):
            for piece in range(cls.MAX_PIECE_TYPES):
                cls._zobrist_table[pos][piece] = random.getrandbits(64)
    
    @classmethod
    def calculate_hash(cls, board: List[int]) -> int:
        """
        计算棋盘状态的Zobrist哈希值
        
        Args:
            board: 棋盘状态数组，board[i]表示位置i的棋子类型
            
        Returns:
            哈希值
        """
        if not cls._zobrist_table:
            cls.initialize_zobrist_table()
        
        hash_value = 0
        
        for pos in range(min(len(board), cls.BOARD_SIZE)):
            piece = board[pos]
            if 0 <= piece < cls.MAX_PIECE_TYPES:
                hash_value ^= cls._zobrist_table[pos][piece]
        
        return hash_value
    
    @classmethod
    def update_hash(cls, current_hash: int, position: int, 
                   old_piece: int, new_piece: int) -> int:
        """
        更新哈希值（当某个位置的棋子发生变化时）
        
        Args:
            current_hash: 当前哈希值
            position: 位置
            old_piece: 旧棋子类型
            new_piece: 新棋子类型
            
        Returns:
            更新后的哈希值
        """
        if not cls._zobrist_table:
            cls.initialize_zobrist_table()
        
        new_hash = current_hash
        
        # 移除旧棋子的贡献
        if 0 <= old_piece < cls.MAX_PIECE_TYPES:
            new_hash ^= cls._zobrist_table[position][old_piece]
        
        # 添加新棋子的贡献
        if 0 <= new_piece < cls.MAX_PIECE_TYPES:
            new_hash ^= cls._zobrist_table[position][new_piece]
        
        return new_hash
    
    @classmethod
    def get_zobrist_value(cls, position: int, piece: int) -> int:
        """
        获取Zobrist表中的值（用于测试）
        
        Args:
            position: 位置
            piece: 棋子类型
            
        Returns:
            Zobrist值
        """
        if not cls._zobrist_table:
            cls.initialize_zobrist_table()
        
        if 0 <= position < cls.BOARD_SIZE and 0 <= piece < cls.MAX_PIECE_TYPES:
            return cls._zobrist_table[position][piece]
        return 0


T = TypeVar('T')


class StateCache(Generic[T]):
    """状态缓存类 - 用于避免重复计算相同状态"""
    
    def __init__(self):
        self.cache: Dict[int, T] = {}
        self.hit_count = 0
        self.miss_count = 0
    
    def get(self, hash_value: int) -> Optional[T]:
        """
        获取状态对应的值
        
        Args:
            hash_value: 状态哈希值
            
        Returns:
            状态值，如果不存在则返回None
        """
        value = self.cache.get(hash_value)
        if value is not None:
            self.hit_count += 1
        else:
            self.miss_count += 1
        return value
    
    def put(self, hash_value: int, value: T) -> None:
        """
        存储状态值
        
        Args:
            hash_value: 状态哈希值
            value: 状态值
        """
        self.cache[hash_value] = value
    
    def contains(self, hash_value: int) -> bool:
        """
        检查状态是否存在
        
        Args:
            hash_value: 状态哈希值
            
        Returns:
            是否存在
        """
        return hash_value in self.cache
    
    def size(self) -> int:
        """
        获取缓存大小
        
        Returns:
            缓存大小
        """
        return len(self.cache)
    
    def get_hit_rate(self) -> float:
        """
        获取命中率
        
        Returns:
            命中率
        """
        total = self.hit_count + self.miss_count
        return 0 if total == 0 else self.hit_count / total
    
    def clear(self) -> None:
        """清空缓存"""
        self.cache.clear()
        self.hit_count = 0
        self.miss_count = 0
    
    def get_stats(self) -> str:
        """
        获取统计信息
        
        Returns:
            统计信息字符串
        """
        return (f"缓存大小: {self.size()}, 命中: {self.hit_count}, "
                f"未命中: {self.miss_count}, 命中率: {self.get_hit_rate()*100:.2f}%")


class UInt128:
    """128位整数模拟类（用于更大规模的状态压缩）"""
    
    def __init__(self, high: int = 0, low: int = 0):
        self.high = high & 0xFFFFFFFFFFFFFFFF  # 高64位
        self.low = low & 0xFFFFFFFFFFFFFFFF   # 低64位
    
    def set_bit(self, bit: int) -> 'UInt128':
        """
        设置指定位为1
        
        Args:
            bit: 位索引(0-127)
            
        Returns:
            更新后的值
        """
        if bit < 64:
            return UInt128(self.high, self.low | (1 << bit))
        else:
            return UInt128(self.high | (1 << (bit - 64)), self.low)
    
    def is_bit_set(self, bit: int) -> bool:
        """
        检查指定位是否为1
        
        Args:
            bit: 位索引(0-127)
            
        Returns:
            是否为1
        """
        if bit < 64:
            return (self.low & (1 << bit)) != 0
        else:
            return (self.high & (1 << (bit - 64))) != 0
    
    def and_op(self, other: 'UInt128') -> 'UInt128':
        """
        与操作
        
        Args:
            other: 另一个UInt128
            
        Returns:
            结果
        """
        return UInt128(self.high & other.high, self.low & other.low)
    
    def or_op(self, other: 'UInt128') -> 'UInt128':
        """
        或操作
        
        Args:
            other: 另一个UInt128
            
        Returns:
            结果
        """
        return UInt128(self.high | other.high, self.low | other.low)
    
    def xor_op(self, other: 'UInt128') -> 'UInt128':
        """
        异或操作
        
        Args:
            other: 另一个UInt128
            
        Returns:
            结果
        """
        return UInt128(self.high ^ other.high, self.low ^ other.low)
    
    def left_shift(self, shift: int) -> 'UInt128':
        """
        左移操作
        
        Args:
            shift: 移位数
            
        Returns:
            结果
        """
        if shift == 0:
            return UInt128(self.high, self.low)
        if shift >= 128:
            return UInt128(0, 0)
        
        if shift < 64:
            new_high = (self.high << shift) | (self.low >> (64 - shift))
            new_low = self.low << shift
            return UInt128(new_high & 0xFFFFFFFFFFFFFFFF, 
                          new_low & 0xFFFFFFFFFFFFFFFF)
        else:
            new_high = self.low << (shift - 64)
            return UInt128(new_high & 0xFFFFFFFFFFFFFFFF, 0)
    
    def right_shift(self, shift: int) -> 'UInt128':
        """
        右移操作
        
        Args:
            shift: 移位数
            
        Returns:
            结果
        """
        if shift == 0:
            return UInt128(self.high, self.low)
        if shift >= 128:
            return UInt128(0, 0)
        
        if shift < 64:
            new_low = (self.low >> shift) | (self.high << (64 - shift))
            new_high = self.high >> shift
            return UInt128(new_high & 0xFFFFFFFFFFFFFFFF, 
                          new_low & 0xFFFFFFFFFFFFFFFF)
        else:
            new_low = self.high >> (shift - 64)
            return UInt128(0, new_low & 0xFFFFFFFFFFFFFFFF)
    
    def __eq__(self, other) -> bool:
        if not isinstance(other, UInt128):
            return False
        return self.high == other.high and self.low == other.low
    
    def __hash__(self) -> int:
        return hash((self.high, self.low))
    
    def __str__(self) -> str:
        return f"{self.high:016X}{self.low:016X}"
    
    def __repr__(self) -> str:
        return f"UInt128(0x{self.high:016X}, 0x{self.low:016X})"


def n_queens(n: int) -> int:
    """
    N皇后问题 - 使用位运算优化的状态压缩回溯算法
    题目来源: LeetCode 51. N-Queens, LeetCode 52. N-Queens II
    题目链接: https://leetcode.cn/problems/n-queens/
    题目链接: https://leetcode.cn/problems/n-queens-ii/
    
    题目描述:
    n 皇后问题研究的是如何将 n 个皇后放置在 n×n 的棋盘上，并且使皇后彼此之间不能相互攻击。
    给你一个整数 n ，返回 n 皇后问题 不同的解决方案的数量。
    
    解题思路:
    使用位运算优化的回溯算法，通过位掩码表示列、主对角线和副对角线的占用情况。
    1. 使用三个整数变量分别表示列、主对角线和副对角线的占用情况
    2. 使用位运算快速找到可用位置
    3. 通过位运算快速更新状态
    
    时间复杂度: O(N!)
    空间复杂度: O(N) - 递归栈空间
    
    工程化考量:
    1. 使用位运算优化性能
    2. 通过状态压缩减少内存使用
    3. 适用于n <= 16的情况
    4. 处理边界情况（n=1, n=2等）
    """
    def solve(row: int, columns: int, diagonals1: int, diagonals2: int) -> int:
        # 基线条件：所有皇后都已放置
        if row == n:
            return 1
        
        count = 0
        # 获取可用的位置（位为0表示可用）
        available_positions = ((1 << n) - 1) & ~(columns | diagonals1 | diagonals2)
        
        while available_positions != 0:
            # 获取最低位的1（选择一个可用位置）
            position = available_positions & -available_positions
            # 清除最低位的1
            available_positions &= available_positions - 1
            
            # 递归处理下一行，更新列和对角线的占用情况
            count += solve(row + 1, 
                          columns | position,
                          (diagonals1 | position) << 1,
                          (diagonals2 | position) >> 1)
        
        return count
    
    return solve(0, 0, 0, 0)


def tsp_dp(graph: List[List[int]]) -> int:
    """
    旅行商问题(TSP) - 使用状态压缩动态规划
    题目来源: 经典算法问题
    
    题目描述:
    给定n个城市和它们之间的距离，找到一条最短的路径，访问每个城市恰好一次并回到起点。
    
    解题思路:
    使用状态压缩DP，dp[mask][last]表示在mask状态下最后访问城市last时的最短距离。
    1. 使用位掩码表示已访问的城市集合
    2. 状态转移：从当前状态转移到新状态
    3. 最终结果：访问所有城市后回到起点
    
    时间复杂度: O(2^N * N^2)
    空间复杂度: O(2^N * N)
    
    工程化考量:
    1. 适用于n <= 20的情况
    2. 对于更大规模问题需要使用近似算法
    3. 处理对称图的优化
    4. 内存优化：使用滚动数组等技术
    """
    n = len(graph)
    if n <= 1:
        return 0
    
    total_states = 1 << n
    # dp[mask][last] = 在mask状态下最后访问城市last时的最短距离
    dp = [[float('inf')] * n for _ in range(total_states)]
    
    # 起点状态：只访问了城市0
    dp[1][0] = 0
    
    # 遍历所有状态
    for mask in range(1, total_states):
        for last in range(n):
            # 如果last不在mask中，跳过
            if (mask & (1 << last)) == 0:
                continue
            
            # 如果当前状态不可达，跳过
            if dp[mask][last] == float('inf'):
                continue
            
            # 尝试访问新城市
            for next_city in range(n):
                # 如果next_city已经在mask中，跳过
                if (mask & (1 << next_city)) != 0:
                    continue
                
                new_mask = mask | (1 << next_city)
                new_distance = dp[mask][last] + graph[last][next_city]
                
                if new_distance < dp[new_mask][next_city]:
                    dp[new_mask][next_city] = new_distance
    
    # 找到最短回路：访问所有城市后回到起点
    final_mask = (1 << n) - 1
    min_distance = float('inf')
    
    for last in range(n):
        if dp[final_mask][last] != float('inf'):
            min_distance = min(min_distance, 
                              dp[final_mask][last] + graph[last][0])
    
    return int(min_distance) if min_distance != float('inf') else -1


def min_push_box(grid: List[str]) -> int:
    """
    推箱子游戏 - 使用Zobrist哈希进行状态管理
    题目来源: LeetCode 1263. 推箱子
    题目链接: https://leetcode.cn/problems/minimum-moves-to-move-a-box-to-their-target-location/
    
    题目描述:
    「推箱子」是一款风靡全球的益智小游戏，玩家需要将箱子推到仓库中的目标位置。
    游戏地图用大小为 m x n 的网格 grid 表示，其中每个元素可以是墙、地板、箱子、玩家和目标。
    
    解题思路:
    使用BFS搜索最短路径，结合Zobrist哈希进行状态去重。
    1. 使用Zobrist哈希表示游戏状态（玩家位置+箱子位置）
    2. 使用BFS搜索最短推动次数
    3. 使用状态缓存避免重复访问相同状态
    
    时间复杂度: O(N*M*2^(N*M)) - 最坏情况
    空间复杂度: O(N*M*2^(N*M)) - 状态存储
    
    工程化考量:
    1. 使用Zobrist哈希进行状态压缩和快速比较
    2. 使用状态缓存避免重复计算
    3. 处理边界情况（无法到达、无解等）
    4. 优化搜索顺序，优先搜索更有可能的路径
    """
    m, n = len(grid), len(grid[0])
    player_x, player_y, box_x, box_y, target_x, target_y = -1, -1, -1, -1, -1, -1
    
    # 找到初始位置
    for i in range(m):
        for j in range(n):
            if grid[i][j] == 'S':
                player_x, player_y = i, j
            elif grid[i][j] == 'B':
                box_x, box_y = i, j
            elif grid[i][j] == 'T':
                target_x, target_y = i, j
    
    # 使用Zobrist哈希进行状态管理
    visited = set()
    
    # BFS队列：(玩家x, 玩家y, 箱子x, 箱子y, 推动次数)
    from collections import deque
    queue = deque([(player_x, player_y, box_x, box_y, 0)])
    
    # 初始状态
    initial_state = (player_x, player_y, box_x, box_y)
    initial_hash = ZobristHashing.calculate_hash(list(initial_state))
    visited.add(initial_hash)
    
    # 四个方向：上、右、下、左
    directions = [(-1, 0), (0, 1), (1, 0), (0, -1)]
    
    while queue:
        px, py, bx, by, pushes = queue.popleft()
        
        # 到达目标位置
        if bx == target_x and by == target_y:
            return pushes
        
        # 尝试四个方向移动
        for dx, dy in directions:
            new_x, new_y = px + dx, py + dy
            
            # 检查边界和墙
            if not (0 <= new_x < m and 0 <= new_y < n) or grid[new_x][new_y] == '#':
                continue
            
            # 如果移动到箱子位置，尝试推动箱子
            if new_x == bx and new_y == by:
                new_box_x, new_box_y = bx + dx, by + dy
                
                # 检查箱子推动后的位置是否合法
                if not (0 <= new_box_x < m and 0 <= new_box_y < n) or \
                   grid[new_box_x][new_box_y] == '#':
                    continue
                
                # 新状态
                new_state = [new_x, new_y, new_box_x, new_box_y]
                new_hash = ZobristHashing.calculate_hash(new_state)
                if new_hash not in visited:
                    visited.add(new_hash)
                    queue.append((new_x, new_y, new_box_x, new_box_y, pushes + 1))
            else:
                # 玩家移动但不推动箱子
                new_state = [new_x, new_y, bx, by]
                new_hash = ZobristHashing.calculate_hash(new_state)
                if new_hash not in visited:
                    visited.add(new_hash)
                    queue.append((new_x, new_y, bx, by, pushes))
    
    return -1  # 无法到达目标


def shortest_path_all_keys(grid: List[str]) -> int:
    """
    获取所有钥匙的最短路径 - 使用状态压缩BFS
    题目来源: LeetCode 864. Shortest Path to Get All Keys
    题目链接: https://leetcode.cn/problems/shortest-path-to-get-all-keys/
    
    题目描述:
    给定一个二维网格，其中包含：
    '.' - 空房间
    '#' - 墙壁
    '@' - 起点
    小写字母 - 钥匙
    大写字母 - 锁
    
    解题思路:
    使用BFS搜索最短路径，结合状态压缩表示钥匙收集情况。
    1. 使用位掩码表示已收集的钥匙
    2. 状态表示：(x, y, keys)
    3. BFS搜索最短路径
    
    时间复杂度: O(M*N*2^K) - M,N为网格大小，K为钥匙数量
    空间复杂度: O(M*N*2^K)
    
    工程化考量:
    1. 使用状态压缩减少状态表示空间
    2. 使用距离数组避免重复访问
    3. 处理边界情况（无法获取所有钥匙等）
    4. 优化搜索顺序
    """
    m, n = len(grid), len(grid[0])
    all_keys = 0
    start_x, start_y = -1, -1
    
    # 找到起点和所有钥匙
    for i in range(m):
        for j in range(n):
            c = grid[i][j]
            if c == '@':
                start_x, start_y = i, j
            elif 'a' <= c <= 'f':
                all_keys |= (1 << (ord(c) - ord('a')))
    
    # BFS搜索
    from collections import deque
    queue = deque([(start_x, start_y, 0, 0)])  # (x, y, keys, distance)
    visited = set()
    visited.add((start_x, start_y, 0))
    
    directions = [(0, 1), (1, 0), (0, -1), (-1, 0)]
    
    while queue:
        x, y, keys, distance = queue.popleft()
        
        if keys == all_keys:
            return distance
        
        for dx, dy in directions:
            nx, ny = x + dx, y + dy
            if not (0 <= nx < m and 0 <= ny < n):
                continue
            
            c = grid[nx][ny]
            if c == '#':  # 墙
                continue
            
            new_keys = keys
            if 'A' <= c <= 'F':
                # 遇到锁，检查是否有对应的钥匙
                lock = ord(c) - ord('A')
                if (keys & (1 << lock)) == 0:  # 没有钥匙
                    continue
            elif 'a' <= c <= 'f':
                # 捡到钥匙
                new_keys |= (1 << (ord(c) - ord('a')))
            
            if (nx, ny, new_keys) not in visited:
                visited.add((nx, ny, new_keys))
                queue.append((nx, ny, new_keys, distance + 1))
    
    return -1


def main():
    """测试示例"""
    print("=== 状态工程技术测试 ===")
    
    # 测试位压缩
    print("\n1. 位压缩测试:")
    state = 0
    print(f"初始状态: {bin(state)}")
    
    # 设置一些位
    state = BitCompression.set_bit(state, 3)
    state = BitCompression.set_bit(state, 7)
    state = BitCompression.set_bit(state, 15)
    print(f"设置位3,7,15后: {bin(state)}")
    
    # 检查位
    print(f"位3是否为1: {BitCompression.is_bit_set(state, 3)}")
    print(f"位5是否为1: {BitCompression.is_bit_set(state, 5)}")
    
    # 计算1的个数
    print(f"1的个数: {BitCompression.count_bits(state)}")
    
    # 翻转位
    state = BitCompression.toggle_bit(state, 3)
    print(f"翻转位3后: {bin(state)}")
    print(f"位3是否为1: {BitCompression.is_bit_set(state, 3)}")
    
    # 测试Zobrist哈希
    print("\n2. Zobrist哈希测试:")
    board = [-1] * 8  # -1表示空位
    board[0] = 1  # 位置0放置类型1的棋子
    board[3] = 2  # 位置3放置类型2的棋子
    board[7] = 3  # 位置7放置类型3的棋子
    
    hash1 = ZobristHashing.calculate_hash(board)
    print(f"初始哈希值: {hash1}")
    
    # 移动棋子
    hash2 = ZobristHashing.update_hash(hash1, 0, 1, -1)  # 移走位置0的棋子
    hash2 = ZobristHashing.update_hash(hash2, 1, -1, 1)  # 在位置1放置棋子
    print(f"移动后哈希值: {hash2}")
    
    # 验证一致性
    board[0] = -1
    board[1] = 1
    hash3 = ZobristHashing.calculate_hash(board)
    print(f"重新计算哈希值: {hash3}")
    print(f"一致性验证: {'通过' if hash2 == hash3 else '失败'}")
    
    # 测试状态缓存
    print("\n3. 状态缓存测试:")
    cache: StateCache[str] = StateCache()
    
    # 添加一些状态
    cache.put(hash1, "状态1")
    cache.put(hash2, "状态2")
    cache.put(12345, "状态3")
    
    print(f"缓存大小: {cache.size()}")
    print(f"查找存在的状态: {cache.get(hash1)}")
    print(f"查找不存在的状态: {cache.get(99999)}")
    
    # 测试命中率
    cache.get(hash1)  # 命中
    cache.get(99999)  # 未命中
    cache.get(hash2)  # 命中
    print(cache.get_stats())
    
    # 测试128位整数
    print("\n4. 128位整数测试:")
    uint128 = UInt128()
    print(f"初始值: {uint128}")
    
    # 设置一些位
    uint128 = uint128.set_bit(3).set_bit(67).set_bit(127)
    print(f"设置位3,67,127后: {uint128}")
    
    # 检查位
    print(f"位3是否为1: {uint128.is_bit_set(3)}")
    print(f"位64是否为1: {uint128.is_bit_set(64)}")
    
    # 位运算测试
    a = UInt128(0x1234567890ABCDEF, 0xFEDCBA0987654321)
    b = UInt128(0xAAAAAAAAAAAAAAAA, 0x5555555555555555)
    print(f"a: {a}")
    print(f"b: {b}")
    print(f"a & b: {a.and_op(b)}")
    print(f"a | b: {a.or_op(b)}")
    print(f"a ^ b: {a.xor_op(b)}")
    
    # 移位测试
    print(f"a << 5: {a.left_shift(5)}")
    print(f"a >> 5: {a.right_shift(5)}")
    
    # 测试N皇后问题
    print("\n5. N皇后问题测试:")
    for n in range(1, 9):
        print(f"{n}皇后问题的解决方案数量: {n_queens(n)}")
    
    # 测试旅行商问题
    print("\n6. 旅行商问题测试:")
    graph = [
        [0, 10, 15, 20],
        [10, 0, 35, 25],
        [15, 35, 0, 30],
        [20, 25, 30, 0]
    ]
    print(f"4城市TSP最短路径长度: {tsp_dp(graph)}")
    
    # 测试获取所有钥匙的最短路径
    print("\n7. 获取所有钥匙的最短路径测试:")
    grid = ["@.a.#", "###.#", "b.A.B"]
    print(f"网格{grid}的最短路径长度: {shortest_path_all_keys(grid)}")


if __name__ == "__main__":
    main()