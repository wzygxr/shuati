#!/usr/bin/env python3
# -*- coding: utf-8 -*-

"""
N皇后问题 Python实现

N皇后问题是一个经典的回溯算法问题，研究的是如何将 n 个皇后放置在 n×n 的棋盘上，
并且使皇后彼此之间不能相互攻击。

按照国际象棋的规则，皇后可以攻击与之处在同一行或同一列或同一斜线上的棋子。

核心知识点：
1. 问题分析：
   - 任意两个皇后不能在同一行
   - 任意两个皇后不能在同一列
   - 任意两个皇后不能在同一条对角线上

2. 解法思路：
   - 方法一：基于数组的回溯实现
     * 使用一个数组path记录每行皇后所在的列位置
     * 通过递归逐行尝试放置皇后
     * 对每个位置检查是否与之前放置的皇后冲突
   - 方法二：基于位运算的优化实现（推荐）
     * 使用位运算表示皇后的位置和约束条件
     * 通过位运算快速判断可放置位置
     * 效率远高于方法一

3. 约束条件判断：
   - 对于位置(i,j)和之前放置的皇后(k, path[k])，冲突条件为：
     * 同列：j == path[k]
     * 同对角线：abs(i-k) == abs(j-path[k])

   - 使用位运算时：
     * 列约束：用一个整数的二进制位表示各列是否被占用
     * 对角线约束：用两个整数分别表示两个方向的对角线是否被占用

算法复杂度分析：
- 时间复杂度：两种方法均为 O(N!)，因为对于第1个皇后有N种选择，第2个有N-1种选择，以此类推
- 空间复杂度：递归栈深度为N，所以空间复杂度为 O(N)

工程化考虑：
- 异常处理：输入校验，检查n是否为正整数
- 性能优化：位运算优化，大幅提升性能
- 代码可读性：函数命名清晰，添加详细注释
"""


class NQueens:
    """
    N皇后问题求解类
    """

    @staticmethod
    def total_n_queens1(n):
        """
        方法1：基于数组的回溯实现
        时间复杂度: O(N!)
        空间复杂度: O(N)
        
        :param n: 皇后数量
        :return: 解法数量
        """
        # 输入校验：n必须为正整数
        if n < 1:
            return 0

        def f1(i, path):
            """
            递归函数：在第i行放置皇后
            
            :param i: 当前行
            :param path: 前i行皇后的列位置
            :return: 解法数量
            """
            # 递归终止条件：所有行都已经放置了皇后
            if i == n:
                return 1

            ans = 0
            # 尝试在当前行的每一列放置皇后
            for j in range(n):
                # 检查当前位置是否合法（不与之前放置的皇后冲突）
                if NQueens._check(path, i, j):
                    # 在第i行第j列放置皇后
                    path[i] = j
                    # 递归处理下一行
                    ans += f1(i + 1, path)
            return ans

        # 创建一个数组path，用来记录每一行皇后所在的列位置
        # path[i]表示第i行的皇后放在了第path[i]列
        path = [0] * n
        return f1(0, path)

    @staticmethod
    def _check(path, i, j):
        """
        检查在第i行第j列放置皇后是否合法
        
        :param path: 前i行皇后的列位置
        :param i: 当前行
        :param j: 当前列
        :return: 是否合法
        """
        # 检查之前放置的皇后是否与当前位置冲突
        for k in range(i):
            # 冲突条件：
            # 1. 同列：j == path[k]
            # 2. 同对角线：行差的绝对值 == 列差的绝对值
            if j == path[k] or abs(i - k) == abs(j - path[k]):
                return False
        return True

    @staticmethod
    def total_n_queens2(n):
        """
        方法2：基于位运算的优化实现
        时间复杂度: O(N!)，但实际运行效率远高于方法1
        空间复杂度: O(N)
        
        :param n: 皇后数量
        :return: 解法数量
        """
        # 输入校验：n必须为正整数
        if n < 1:
            return 0

        def f2(limit, col, left, right):
            """
            位运算递归函数
            
            :param limit: 限制位，表示棋盘大小
            :param col: 列限制，表示哪些列已被占用
            :param left: 左对角线限制
            :param right: 右对角线限制
            :return: 解法数量
            """
            # 递归终止条件：所有列都放置了皇后
            if col == limit:
                # 所有皇后放完了！
                return 1

            # 总限制：不能放置皇后的位置
            ban = col | left | right
            # 可以放置皇后的位置
            candidate = limit & (~ban)
            # 放置皇后的尝试！
            # 一共有多少有效的方法
            ans = 0
            # 遍历所有可以放置皇后的位置
            while candidate != 0:
                # 提取出最右侧的1，表示选择在该位置放置皇后
                place = candidate & (-candidate)
                # 从candidate中移除已选择的位置
                candidate ^= place
                # 递归处理下一行
                # col | place：更新列的占用情况
                # (left | place) >> 1：更新右上->左下对角线的占用情况
                # (right | place) << 1：更新左上->右下对角线的占用情况
                ans += f2(limit, col | place, (left | place) >> 1, (right | place) << 1)
            return ans

        # limit表示棋盘的限制，比如n=4时，limit=1111(二进制)，表示4列
        limit = (1 << n) - 1
        return f2(limit, 0, 0, 0)

    @staticmethod
    def solve_n_queens(n):
        """
        LeetCode 51. N皇后问题 - 返回所有可能的解决方案
        题目链接：https://leetcode.cn/problems/n-queens/
        
        时间复杂度：O(N!)
        空间复杂度：O(N)
        
        :param n: 皇后数量
        :return: 所有解法的列表
        """
        def backtrack(solutions, queens, n, row, cols, diag1, diag2):
            """
            回溯函数：逐行放置皇后
            
            :param solutions: 所有解法的列表
            :param queens: 皇后位置数组
            :param n: 皇后数量
            :param row: 当前行
            :param cols: 列占用情况
            :param diag1: 主对角线占用情况
            :param diag2: 副对角线占用情况
            """
            # 递归终止条件：所有行都已放置皇后
            if row == n:
                # 根据queens数组构造棋盘
                board = NQueens._generate_board(queens, n)
                solutions.append(board)
                return

            # 在当前行尝试每一列
            for i in range(n):
                # 检查列是否被占用
                if i in cols:
                    continue
                # 检查主对角线是否被占用
                d1 = row - i
                if d1 in diag1:
                    continue
                # 检查副对角线是否被占用
                d2 = row + i
                if d2 in diag2:
                    continue

                # 在第row行第i列放置皇后
                queens[row] = i
                cols.add(i)
                diag1.add(d1)
                diag2.add(d2)

                # 递归处理下一行
                backtrack(solutions, queens, n, row + 1, cols, diag1, diag2)

                # 回溯，恢复状态
                queens[row] = -1
                cols.remove(i)
                diag1.remove(d1)
                diag2.remove(d2)

        solutions = []
        queens = [-1] * n
        cols = set()
        diag1 = set()
        diag2 = set()
        backtrack(solutions, queens, n, 0, cols, diag1, diag2)
        return solutions

    @staticmethod
    def _generate_board(queens, n):
        """
        根据皇后位置生成棋盘
        
        :param queens: 皇后位置数组
        :param n: 棋盘大小
        :return: 棋盘表示
        """
        board = []
        for i in range(n):
            row = ['.'] * n
            # 在皇后所在位置放置'Q'
            row[queens[i]] = 'Q'
            board.append(''.join(row))
        return board

    @staticmethod
    def total_n_queens(n):
        """
        LeetCode 52. N皇后计数问题
        题目链接：https://leetcode.cn/problems/n-queens-ii/
        
        时间复杂度：O(N!)
        空间复杂度：O(N)
        
        :param n: 皇后数量
        :return: 解法数量
        """
        # 直接使用已实现的方法
        return NQueens.total_n_queens2(n)
    
    @staticmethod
    def queens_attack(n, k, r_q, c_q, obstacles):
        """
        HackerRank Queen's Attack II 问题
        题目链接：https://www.hackerrank.com/challenges/queens-attack-2/problem
        
        题目描述：
        在一个 n×n 的棋盘上有一个皇后和若干障碍物，计算皇后能攻击多少个格子。
        皇后可以攻击同一行、同一列、同一对角线上的格子，但会被障碍物阻挡。
        
        参数说明：
        n: 棋盘大小
        k: 障碍物数量
        r_q: 皇后行位置（1-based）
        c_q: 皇后列位置（1-based）
        obstacles: 障碍物位置列表
        
        示例：
        输入：n=5, k=3, r_q=4, c_q=3, obstacles=[[5,5],[4,2],[2,3]]
        输出：10
        
        时间复杂度：O(k)
        空间复杂度：O(k)
        """
        # 将障碍物位置存储在集合中，便于快速查找
        obstacle_set = set()
        for obstacle in obstacles:
            obstacle_set.add((obstacle[0], obstacle[1]))
        
        # 8个方向的移动向量：上、下、左、右、左上、右上、左下、右下
        directions = [
            (-1, 0), (1, 0), (0, -1), (0, 1),  # 上下左右
            (-1, -1), (-1, 1), (1, -1), (1, 1)  # 四个对角线方向
        ]
        
        count = 0
        
        # 对每个方向计算能攻击的格子数
        for dx, dy in directions:
            # 从皇后位置开始，沿着当前方向移动
            x, y = r_q, c_q
            
            while True:
                # 移动到下一个位置
                x += dx
                y += dy
                
                # 检查是否越界
                if x < 1 or x > n or y < 1 or y > n:
                    break
                
                # 检查是否有障碍物
                if (x, y) in obstacle_set:
                    break
                
                # 如果没有障碍物且未越界，则可以攻击这个格子
                count += 1
        
        return count
    
    @staticmethod
    def chess_board_problem(board, k):
        """
        POJ 1321 棋盘问题
        题目链接：http://poj.org/problem?id=1321
        
        题目描述：
        在一个给定形状的棋盘（形状可能是不规则的）上面摆放棋子，棋子没有区别。
        要求摆放时任意的两个棋子不能放在棋盘中的同一行或者同一列，请编程求解对于给定形状和大小的棋盘，
        摆放k个棋子的所有可行的摆放方案C。
        
        参数说明：
        board: 棋盘，'#'表示可放置棋子的位置，'.'表示不可放置棋子的位置
        k: 需要放置的棋子数量
        
        示例：
        输入：
        board = ["#.",".#"]
        k = 1
        输出：2
        
        时间复杂度：O(2^n)
        空间复杂度：O(n)
        """
        n = len(board)
        
        def dfs(row, placed, used_cols):
            """
            深度优先搜索解决棋盘问题
            
            :param row: 当前行
            :param placed: 已放置棋子数
            :param used_cols: 已使用的列
            :return: 方案数
            """
            # 如果已经放置了k个棋子，找到一种方案
            if placed == k:
                return 1
            
            # 如果已经搜索完所有行，但还未放置k个棋子
            if row == n:
                return 0
            
            count = 0
            
            # 不在当前行放置棋子
            count += dfs(row + 1, placed, used_cols)
            
            # 在当前行尝试放置棋子
            for col in range(n):
                # 检查当前位置是否可以放置棋子
                if board[row][col] == '#' and col not in used_cols:
                    # 放置棋子
                    used_cols.add(col)
                    count += dfs(row + 1, placed + 1, used_cols)
                    # 回溯
                    used_cols.remove(col)
            
            return count
        
        return dfs(0, 0, set())
    
    @staticmethod
    def eight_queens_with_existing(existing_queens):
        """
        Aizu ALDS1_13_A 8 Queens Problem（部分皇后位置已知）
        题目链接：https://onlinejudge.u-aizu.ac.jp/courses/lesson/1/ALDS1/all/ALDS1_13_A
        
        题目描述：
        8皇后问题，但部分皇后的位置已经确定，需要完成剩余皇后的放置。
        
        参数说明：
        existing_queens: 已知皇后的位置，格式为[row, col]
        
        示例：
        输入：existing_queens = [[0,0],[1,1]]
        输出：是否能完成8皇后布局
        
        时间复杂度：O(N!)
        空间复杂度：O(N)
        """
        queens = [-1] * 8
        
        # 设置已知皇后位置
        for pos in existing_queens:
            queens[pos[0]] = pos[1]
        
        def is_valid(row):
            """检查到第row行为止的皇后布局是否有效"""
            for i in range(row):
                # 检查列冲突
                if queens[i] == queens[row]:
                    return False
                # 检查对角线冲突
                if abs(i - row) == abs(queens[i] - queens[row]):
                    return False
            return True
        
        def solve(row):
            """
            递归解决8皇后问题（部分位置已知）
            
            :param row: 当前行
            :return: 是否能完成布局
            """
            if row == 8:
                return True  # 所有皇后都已放置
            
            # 如果当前行已经有皇后，直接处理下一行
            if queens[row] != -1:
                if is_valid(row):
                    return solve(row + 1)
                else:
                    return False
            
            # 尝试在当前行的每一列放置皇后
            for col in range(8):
                queens[row] = col
                if is_valid(row):
                    if solve(row + 1):
                        return True
                queens[row] = -1  # 回溯
            
            return False
        
        return solve(0)
    
    @staticmethod
    def solve_k_queens(n, k):
        """
        变种题目1：多皇后问题 - 在n×n棋盘上放置k个皇后，使得它们互不攻击
        平台：POJ类似题目，常见于各大OJ的组合数学问题
        思路：使用回溯法，尝试在每一行放置皇后，但只需放置k个
        时间复杂度：O(N×N!)，其中N是棋盘大小
        空间复杂度：O(N)，递归栈和标记数组的空间
        
        参数：
        n: 棋盘大小
        k: 需要放置的皇后数量
        
        返回：
        满足条件的放置方案数
        """
        # 边界条件检查
        if k < 0 or k > n or n <= 0:
            return 1 if k == 0 else 0  # 放置0个皇后只有一种方式
        
        # 标记数组
        cols = [False] * n        # 列是否被占用
        diag1 = [False] * (2 * n)  # 左上到右下对角线
        diag2 = [False] * (2 * n)  # 右上到左下对角线
        
        def backtrack_k_queens(row, placed):
            """
            回溯函数：放置k个皇后
            
            :param row: 当前行
            :param placed: 已放置皇后数
            :return: 方案数
            """
            # 已经放置了k个皇后，找到一个有效解
            if placed == k:
                return 1
            
            # 已经处理完所有行但还没放够k个皇后
            if row == n:
                return 0
            
            count = 0
            
            # 尝试在当前行放置皇后
            for col in range(n):
                # 计算对角线索引
                d1 = row - col + n  # 避免负数
                d2 = row + col
                
                # 检查是否可以放置皇后
                if not cols[col] and not diag1[d1] and not diag2[d2]:
                    # 放置皇后
                    cols[col] = True
                    diag1[d1] = True
                    diag2[d2] = True
                    
                    # 递归到下一行，已放置皇后数+1
                    count += backtrack_k_queens(row + 1, placed + 1)
                    
                    # 回溯，撤销放置
                    cols[col] = False
                    diag1[d1] = False
                    diag2[d2] = False
            
            # 尝试在当前行不放置皇后，直接到下一行
            count += backtrack_k_queens(row + 1, placed)
            
            return count
        
        return backtrack_k_queens(0, 0)
    
    @staticmethod
    def solve_n_queens_with_obstacles(n, obstacles):
        """
        变种题目2：有障碍物的N皇后问题 - 某些格子不能放置皇后
        平台：类似LeetCode 51题的扩展，常见于面试题
        思路：在标准N皇后问题的基础上，增加对障碍物的检查
        时间复杂度：O(N!)
        空间复杂度：O(N)
        
        参数：
        n: 棋盘大小
        obstacles: 障碍物位置列表，格式为[[行, 列], ...]
        
        返回：
        所有满足条件的棋盘布局
        """
        solutions = []
        
        # 将障碍物转换为集合，方便快速查询
        obstacle_set = set()
        for obstacle in obstacles:
            # 假设障碍物坐标从0开始
            obstacle_set.add((obstacle[0], obstacle[1]))
        
        # 初始化标记数组
        cols = [False] * n
        diag1 = [False] * (2 * n)
        diag2 = [False] * (2 * n)
        
        # 初始化棋盘表示
        board = [['.' for _ in range(n)] for _ in range(n)]
        
        def backtrack_n_queens_with_obstacles(row):
            """
            回溯函数：在有障碍物的棋盘上放置皇后
            
            :param row: 当前行
            """
            if row == n:
                # 找到一个解决方案，转换为字符串表示
                solution = [''.join(row) for row in board]
                solutions.append(solution)
                return
            
            for col in range(n):
                # 检查当前位置是否是障碍物
                if (row, col) in obstacle_set:
                    continue
                
                # 检查是否可以放置皇后
                d1 = row - col + n
                d2 = row + col
                if not cols[col] and not diag1[d1] and not diag2[d2]:
                    # 放置皇后
                    board[row][col] = 'Q'
                    cols[col] = True
                    diag1[d1] = True
                    diag2[d2] = True
                    
                    # 递归到下一行
                    backtrack_n_queens_with_obstacles(row + 1)
                    
                    # 回溯
                    board[row][col] = '.'
                    cols[col] = False
                    diag1[d1] = False
                    diag2[d2] = False
        
        backtrack_n_queens_with_obstacles(0)
        return solutions
    
    @staticmethod
    def count_two_queens(n):
        """
        变种题目3：双皇后问题 - 计算两个皇后互不攻击的位置组合数
        平台：CodeChef、HackerEarth等平台常见题目
        思路：枚举第一个皇后的位置，然后计算第二个皇后的合法位置数
        时间复杂度：O(N²)
        空间复杂度：O(1)
        
        参数：
        n: 棋盘大小
        
        返回：
        满足条件的位置组合数
        """
        # 边界条件检查
        if n < 2:
            return 0  # 棋盘太小，无法放置两个皇后
        
        total = 0
        # 枚举第一个皇后的位置
        for r1 in range(n):
            for c1 in range(n):
                # 计算第二个皇后的合法位置数
                valid_positions = 0
                for r2 in range(n):
                    for c2 in range(n):
                        # 不能是同一个位置
                        if r1 == r2 and c1 == c2:
                            continue
                        # 检查是否在同一行、同一列或同一对角线
                        is_same_row = (r1 == r2)
                        is_same_col = (c1 == c2)
                        is_same_diag = (abs(r1 - r2) == abs(c1 - c2))
                        
                        if not is_same_row and not is_same_col and not is_same_diag:
                            valid_positions += 1
                total += valid_positions
        
        # 因为每个组合被计算了两次（Q1在(r1,c1)和Q2在(r2,c2)与Q1在(r2,c2)和Q2在(r1,c1)），所以要除以2
        return total // 2
    
    @staticmethod
    def can_cover_board(n, k):
        """
        变种题目4：皇后覆盖问题 - 检查k个皇后是否能覆盖整个棋盘
        平台：类似UVa OJ的组合优化问题
        思路：放置k个皇后，然后检查棋盘是否被完全覆盖
        时间复杂度：O(N^k)，其中k是皇后数量
        空间复杂度：O(N)
        
        参数：
        n: 棋盘大小
        k: 皇后数量
        
        返回：
        是否能覆盖整个棋盘
        """
        # 边界条件检查
        if k <= 0 or n <= 0:
            return k == 0 and n == 0  # 空棋盘不需要皇后覆盖
        
        # 棋盘是否被覆盖
        covered = [[False for _ in range(n)] for _ in range(n)]
        
        def backtrack_cover_board(row, col, placed):
            """
            回溯函数：放置皇后并检查覆盖情况
            
            :param row: 当前行
            :param col: 当前列
            :param placed: 已放置皇后数
            :return: 是否能覆盖整个棋盘
            """
            # 已经放置了k个皇后，检查是否覆盖了整个棋盘
            if placed == k:
                for i in range(n):
                    for j in range(n):
                        if not covered[i][j]:
                            return False
                return True
            
            # 遍历所有可能的放置位置
            for i in range(row, n):
                start_col = col if i == row else 0
                for j in range(start_col, n):
                    # 记录当前覆盖状态，用于回溯
                    old_covered = [row.copy() for row in covered]
                    
                    # 放置皇后并标记覆盖区域
                    new_covered = mark_coverage(i, j)
                    
                    # 更新覆盖状态
                    for x in range(n):
                        for y in range(n):
                            covered[x][y] = new_covered[x][y]
                    
                    # 递归放置下一个皇后
                    if backtrack_cover_board(i, j + 1, placed + 1):
                        return True
                    
                    # 回溯
                    for x in range(n):
                        for y in range(n):
                            covered[x][y] = old_covered[x][y]
            
            return False
        
        def mark_coverage(r, c):
            """
            标记皇后覆盖的区域
            
            :param r: 皇后行位置
            :param c: 皇后列位置
            :return: 覆盖状态
            """
            # 创建副本用于回溯
            temp_covered = [row.copy() for row in covered]
            
            # 标记同一行
            for j in range(n):
                temp_covered[r][j] = True
            
            # 标记同一列
            for i in range(n):
                temp_covered[i][c] = True
            
            # 标记左上到右下对角线
            i, j = r, c
            while i >= 0 and j >= 0:
                temp_covered[i][j] = True
                i -= 1
                j -= 1
            i, j = r + 1, c + 1
            while i < n and j < n:
                temp_covered[i][j] = True
                i += 1
                j += 1
            
            # 标记右上到左下对角线
            i, j = r, c
            while i >= 0 and j < n:
                temp_covered[i][j] = True
                i -= 1
                j += 1
            i, j = r + 1, c - 1
            while i < n and j >= 0:
                temp_covered[i][j] = True
                i += 1
                j -= 1
            
            return temp_covered
        
        return backtrack_cover_board(0, 0, 0)
    
    @staticmethod
    def total_n_queens_iterative(n):
        """
        变种题目5：N皇后问题的非递归解法 - 用于理解递归与非递归的差异
        平台：算法教学中常见的变体
        思路：使用栈模拟递归过程
        时间复杂度：O(N!)
        空间复杂度：O(N)
        
        参数：
        n: 棋盘大小
        
        返回：
        解决方案数量
        """
        if n <= 0:
            return 0
        
        count = 0
        # 记录每一行皇后的列位置
        queens = [-1] * n
        
        row = 0  # 当前处理的行
        col = 0  # 当前尝试的列
        
        while row >= 0:
            # 尝试在当前行放置皇后
            placed_in_row = False
            while col < n:
                # 检查是否可以安全放置
                safe = True
                for i in range(row):
                    # 检查列冲突
                    if queens[i] == col:
                        safe = False
                        break
                    # 检查对角线冲突
                    if abs(i - row) == abs(queens[i] - col):
                        safe = False
                        break
                
                if safe:
                    # 放置皇后
                    queens[row] = col
                    row += 1  # 移动到下一行
                    col = 0   # 从第一列开始尝试
                    placed_in_row = True
                    break     # 跳出当前列循环
                col += 1  # 尝试下一列
            
            # 如果当前行所有列都不能放置皇后，回溯
            if not placed_in_row:
                row -= 1  # 回溯到上一行
                if row >= 0:
                    col = queens[row] + 1  # 从上一行皇后的下一列开始尝试
                    queens[row] = -1       # 移除上一行的皇后
            elif row == n:
                # 找到一个解决方案
                count += 1
                row -= 1  # 回溯寻找下一个解决方案
                if row >= 0:
                    col = queens[row] + 1  # 从上一行皇后的下一列开始尝试
                    queens[row] = -1       # 移除上一行的皇后
        
        return count
    
    @staticmethod
    def total_n_queens_bitmask(n):
        """
        变种题目6：位运算优化的N皇后解法 - 更高效的实现
        平台：各大算法平台的优化版本
        思路：使用位运算来表示和检查冲突
        时间复杂度：O(N!)
        空间复杂度：O(N)
        
        参数：
        n: 棋盘大小
        
        返回：
        解决方案数量
        """
        if n <= 0:
            return 0
        
        # 预处理：创建位掩码表示棋盘大小
        limit = (1 << n) - 1  # 例如n=4时，limit=0b1111
        
        def backtrack_bitmask(col_mask, diag1_mask, diag2_mask):
            """
            位运算回溯函数
            
            :param col_mask: 列占用掩码
            :param diag1_mask: 主对角线占用掩码
            :param diag2_mask: 副对角线占用掩码
            :return: 解决方案数量
            """
            # 所有列都放置了皇后
            if col_mask == limit:
                return 1
            
            # 计算所有可以放置皇后的位置
            available_pos = limit & (~(col_mask | diag1_mask | diag2_mask))
            count = 0
            
            # 尝试所有可用位置
            while available_pos != 0:
                # 取出最右边的可用位置
                pos = available_pos & (-available_pos)
                # 移除已选择的位置
                available_pos &= (available_pos - 1)
                
                # 递归处理下一行
                count += backtrack_bitmask(
                    col_mask | pos,
                    (diag1_mask | pos) << 1,
                    (diag2_mask | pos) >> 1
                )
            
            return count
        
        return backtrack_bitmask(0, 0, 0)


def main():
    n = 14
    print("测试开始")
    print(f"解决{n}皇后问题")

    import time
    start = time.time()
    print(f"方法1答案 : {NQueens.total_n_queens1(n)}")
    end = time.time()
    print(f"方法1运行时间 : {(end - start) * 1000:.0f} 毫秒")

    start = time.time()
    print(f"方法2答案 : {NQueens.total_n_queens2(n)}")
    end = time.time()
    print(f"方法2运行时间 : {(end - start) * 1000:.0f} 毫秒")
    print("测试结束")

    print("=======")
    print("只有位运算的版本，才能10秒内跑完16皇后问题的求解过程")
    start = time.time()
    ans = NQueens.total_n_queens2(16)
    end = time.time()
    print(f"16皇后问题的答案 : {ans}")
    print(f"运行时间 : {(end - start) * 1000:.0f} 毫秒")

    # 测试LeetCode 51
    print("=======")
    print("LeetCode 51. N皇后问题测试")
    solutions = NQueens.solve_n_queens(4)
    for i, solution in enumerate(solutions):
        print(f"解法{i + 1}:")
        for row in solution:
            print(row)
        print()

    # 测试LeetCode 52
    print("LeetCode 52. N皇后计数问题测试")
    print(f"4皇后问题的解法数：{NQueens.total_n_queens(4)}")
    print(f"8皇后问题的解法数：{NQueens.total_n_queens(8)}")
    
    # 测试HackerRank Queen's Attack II
    print("=======")
    print("HackerRank Queen's Attack II 测试")
    obstacles = [[5, 5], [4, 2], [2, 3]]
    print("5x5棋盘，皇后在(4,3)，障碍物在(5,5),(4,2),(2,3)")
    print(f"皇后能攻击的格子数：{NQueens.queens_attack(5, 3, 4, 3, obstacles)}")
    
    # 测试POJ 1321 棋盘问题
    print("=======")
    print("POJ 1321 棋盘问题测试")
    board1 = ["#.", ".#"]
    print("2x2棋盘，可放置位置为(0,0)和(1,1)，放置1个棋子")
    print(f"方案数：{NQueens.chess_board_problem(board1, 1)}")
    
    board2 = [
        "...#",
        "..#.",
        ".#..",
        "#..."
    ]
    print("4x4棋盘，放置4个棋子")
    print(f"方案数：{NQueens.chess_board_problem(board2, 4)}")
    
    # 测试Aizu ALDS1_13_A 8 Queens Problem
    print("=======")
    print("Aizu ALDS1_13_A 8 Queens Problem 测试")
    existing_queens = [[0, 0], [1, 1]]
    print("已知皇后在(0,0)和(1,1)")
    print(f"能否完成8皇后布局：{NQueens.eight_queens_with_existing(existing_queens)}")
    
    # 测试新增的变种题目
    print("\n=======")
    print("======= 变种题目测试 =======")
    
    # 测试多皇后问题
    n4, k4 = 5, 3
    k_queens_count = NQueens.solve_k_queens(n4, k4)
    print(f"\n1. 多皇后问题:")
    print(f"在{n4}×{n4}棋盘上放置{k4}个互不攻击的皇后，方案数: {k_queens_count}")
    
    # 测试有障碍物的N皇后问题
    n5 = 4
    obstacles5 = [[0, 0], [2, 2]]  # (0,0)和(2,2)位置有障碍物
    solutions_with_obstacles = NQueens.solve_n_queens_with_obstacles(n5, obstacles5)
    print(f"\n2. 有障碍物的N皇后问题:")
    print(f"n = {n5} 的解决方案数量: {len(solutions_with_obstacles)}")
    for i, solution in enumerate(solutions_with_obstacles):
        print(f"  解决方案 {i + 1}:")
        for row in solution:
            print(f"    {row}")
    
    # 测试双皇后问题
    n6 = 5
    two_queens_count = NQueens.count_two_queens(n6)
    print(f"\n3. 双皇后问题:")
    print(f"在{n6}×{n6}棋盘上放置2个互不攻击的皇后，组合数: {two_queens_count}")
    
    # 测试皇后覆盖问题
    n7, k7 = 4, 4
    can_cover = NQueens.can_cover_board(n7, k7)
    print(f"\n4. 皇后覆盖问题:")
    print(f"使用{k7}个皇后{'能' if can_cover else '不能'}覆盖{n7}×{n7}的棋盘")
    
    # 测试非递归解法
    n8 = 8
    iterative_count = NQueens.total_n_queens_iterative(n8)
    print(f"\n5. 非递归解法:")
    print(f"{n8}皇后解决方案数: {iterative_count}")
    print(f"验证是否与递归解法一致: {iterative_count == NQueens.total_n_queens(n8)}")
    
    # 测试位运算优化解法
    n9 = 12
    print(f"\n6. 位运算优化解法性能测试:")
    import time
    start = time.time()
    bitmask_count = NQueens.total_n_queens_bitmask(n9)
    end = time.time()
    print(f"{n9}皇后解决方案数: {bitmask_count}, 耗时: {(end - start) * 1000:.0f}ms")


if __name__ == "__main__":
    main()