"""
状态压缩动态规划算法实现
使用位掩码技术解决复杂的组合优化问题

题目来源:
1. LeetCode 464 - 我能赢吗
2. LeetCode 691 - 贴纸拼词
3. LeetCode 943 - 最短超级串
4. LeetCode 1125 - 最小的必要团队
5. 旅行商问题（TSP）
6. 哈密顿路径问题

时间复杂度分析:
- 状态压缩DP: O(2^n * n) 或 O(2^n * n^2)
- 空间复杂度: O(2^n) 或 O(2^n * n)

工程化考量:
1. 状态表示: 使用位掩码紧凑表示状态
2. 状态转移: 优化状态转移方程
3. 内存优化: 使用滚动数组或位运算压缩
4. 边界处理: 处理空状态和终止状态
"""

import time
from typing import List, Tuple, Dict
from functools import lru_cache
import sys

class BitMaskDP:
    """状态压缩动态规划算法类"""
    
    @staticmethod
    def can_i_win(max_choosable_integer: int, desired_total: int) -> bool:
        """
        LeetCode 464 - 我能赢吗
        两个玩家轮流选择1到maxChoosableInteger的数字，先达到或超过desiredTotal的玩家获胜
        
        方法: 记忆化搜索 + 状态压缩
        时间复杂度: O(2^maxChoosableInteger * maxChoosableInteger)
        空间复杂度: O(2^maxChoosableInteger)
        """
        if max_choosable_integer >= desired_total:
            return True
        if max_choosable_integer * (max_choosable_integer + 1) // 2 < desired_total:
            return False
        
        @lru_cache(maxsize=None)
        def dfs(state: int, current_total: int) -> bool:
            for i in range(max_choosable_integer):
                if state & (1 << i):  # 数字i+1已经被选过
                    continue
                
                new_state = state | (1 << i)
                new_total = current_total + i + 1
                
                # 如果当前选择能直接获胜，或者对手会输
                if new_total >= desired_total or not dfs(new_state, new_total):
                    return True
            return False
        
        return dfs(0, 0)
    
    @staticmethod
    def min_stickers(stickers: List[str], target: str) -> int:
        """
        LeetCode 691 - 贴纸拼词
        用给定的贴纸拼出目标单词，求最少需要的贴纸数量
        
        方法: 状态压缩BFS
        时间复杂度: O(2^len(target) * n * len(stickers))
        空间复杂度: O(2^len(target))
        """
        n = len(target)
        total_states = 1 << n
        dp = [float('inf')] * total_states
        dp[0] = 0  # 空状态需要0张贴纸
        
        for state in range(total_states):
            if dp[state] == float('inf'):
                continue
            
            for sticker in stickers:
                new_state = state
                
                # 尝试用当前贴纸覆盖目标字符
                count = [0] * 26
                for c in sticker:
                    count[ord(c) - ord('a')] += 1
                
                for i in range(n):
                    if state & (1 << i):  # 该位置已经被覆盖
                        continue
                    if count[ord(target[i]) - ord('a')] > 0:
                        count[ord(target[i]) - ord('a')] -= 1
                        new_state |= (1 << i)
                
                if new_state != state:
                    dp[new_state] = min(dp[new_state], dp[state] + 1)
        
        return dp[total_states - 1] if dp[total_states - 1] != float('inf') else -1
    
    @staticmethod
    def shortest_superstring(words: List[str]) -> str:
        """
        LeetCode 943 - 最短超级串
        找到包含所有字符串的最短超级字符串
        
        方法: 状态压缩DP + 重叠计算
        时间复杂度: O(2^n * n^2)
        空间复杂度: O(2^n * n)
        """
        n = len(words)
        
        # 计算重叠度
        overlap = [[0] * n for _ in range(n)]
        for i in range(n):
            for j in range(n):
                if i == j:
                    continue
                length = min(len(words[i]), len(words[j]))
                for k in range(length, 0, -1):
                    if words[i][-k:] == words[j][:k]:
                        overlap[i][j] = k
                        break
        
        # DP状态: dp[mask][last] 表示使用mask集合，以last结尾的最短长度
        INF = float('inf')
        dp = [[INF] * n for _ in range(1 << n)]
        parent = [[-1] * n for _ in range(1 << n)]
        
        # 初始化
        for i in range(n):
            dp[1 << i][i] = len(words[i])
        
        # 动态规划
        for mask in range(1 << n):
            for last in range(n):
                if not (mask & (1 << last)) or dp[mask][last] == INF:
                    continue
                
                for next_node in range(n):
                    if mask & (1 << next_node):
                        continue
                    
                    new_mask = mask | (1 << next_node)
                    new_len = dp[mask][last] + len(words[next_node]) - overlap[last][next_node]
                    
                    if new_len < dp[new_mask][next_node]:
                        dp[new_mask][next_node] = new_len
                        parent[new_mask][next_node] = last
        
        # 重建结果
        final_mask = (1 << n) - 1
        last = min(range(n), key=lambda x: dp[final_mask][x])
        min_len = dp[final_mask][last]
        
        # 回溯构建字符串
        path = []
        mask = final_mask
        current = last
        
        while mask > 0:
            path.append(current)
            prev = parent[mask][current]
            if prev == -1:
                break
            mask ^= (1 << current)
            current = prev
        
        path.reverse()
        
        result = words[path[0]]
        for i in range(1, len(path)):
            overlap_len = overlap[path[i-1]][path[i]]
            result += words[path[i]][overlap_len:]
        
        return result
    
    @staticmethod
    def smallest_sufficient_team(req_skills: List[str], people: List[List[str]]) -> List[int]:
        """
        LeetCode 1125 - 最小的必要团队
        找到覆盖所有技能的最小团队
        
        方法: 状态压缩DP
        时间复杂度: O(2^m * n) - m为技能数量，n为人员数量
        空间复杂度: O(2^m)
        """
        m = len(req_skills)
        n = len(people)
        
        # 技能到索引的映射
        skill_to_index = {skill: i for i, skill in enumerate(req_skills)}
        
        # 将每个人的技能转换为位掩码
        people_skills = [0] * n
        for i in range(n):
            for skill in people[i]:
                if skill in skill_to_index:
                    people_skills[i] |= (1 << skill_to_index[skill])
        
        total_states = 1 << m
        dp = [float('inf')] * total_states
        parent_state = [-1] * total_states
        parent_person = [-1] * total_states
        dp[0] = 0
        
        for state in range(total_states):
            if dp[state] == float('inf'):
                continue
            
            for i in range(n):
                new_state = state | people_skills[i]
                if dp[state] + 1 < dp[new_state]:
                    dp[new_state] = dp[state] + 1
                    parent_state[new_state] = state
                    parent_person[new_state] = i
        
        # 重建团队
        team = []
        state = total_states - 1
        
        while state > 0:
            team.append(parent_person[state])
            state = parent_state[state]
        
        return team
    
    @staticmethod
    def hamiltonian_path(graph: List[List[int]]) -> List[int]:
        """
        哈密顿路径问题
        在图中找到访问所有顶点恰好一次的路径
        
        方法: 状态压缩DP
        时间复杂度: O(2^n * n^2)
        空间复杂度: O(2^n * n)
        """
        n = len(graph)
        if n == 0:
            return []
        
        # dp[mask][last] 表示访问mask集合，以last结尾的路径是否存在
        INF = float('inf')
        dp = [[INF] * n for _ in range(1 << n)]
        parent = [[-1] * n for _ in range(1 << n)]
        
        # 初始化：每个顶点单独作为路径起点
        for i in range(n):
            dp[1 << i][i] = 0
        
        # 动态规划
        for mask in range(1 << n):
            for last in range(n):
                if dp[mask][last] == INF:
                    continue
                
                for next_node in range(n):
                    if mask & (1 << next_node):  # 已经访问过
                        continue
                    if not graph[last][next_node]:  # 不可达
                        continue
                    
                    new_mask = mask | (1 << next_node)
                    if dp[mask][last] + 1 < dp[new_mask][next_node]:
                        dp[new_mask][next_node] = dp[mask][last] + 1
                        parent[new_mask][next_node] = last
        
        # 检查是否存在哈密顿路径
        final_mask = (1 << n) - 1
        for last in range(n):
            if dp[final_mask][last] != INF:
                # 重建路径
                path = []
                mask = final_mask
                current = last
                
                while mask > 0:
                    path.append(current)
                    prev = parent[mask][current]
                    if prev == -1:
                        break
                    mask ^= (1 << current)
                    current = prev
                
                path.reverse()
                return path
        
        return []  # 不存在哈密顿路径
    
    @staticmethod
    def subset_sum(nums: List[int], target: int) -> bool:
        """
        子集和问题（状态压缩版本）
        判断是否存在子集使得元素和等于目标值
        
        方法: 位运算枚举
        时间复杂度: O(2^n)
        空间复杂度: O(2^n)
        """
        n = len(nums)
        total = 1 << n
        
        dp = [False] * total
        dp[0] = True
        
        for mask in range(total):
            if not dp[mask]:
                continue
            
            current_sum = 0
            for i in range(n):
                if mask & (1 << i):
                    current_sum += nums[i]
            
            if current_sum == target:
                return True
            
            for i in range(n):
                if mask & (1 << i):
                    continue
                new_mask = mask | (1 << i)
                dp[new_mask] = True
        
        return False


class PerformanceTester:
    """性能测试工具类"""
    
    @staticmethod
    def test_can_i_win():
        """测试我能赢吗性能"""
        print("=== 我能赢吗性能测试 ===")
        
        test_cases = [
            (10, 11),  # 简单情况
            (10, 40),  # 复杂情况
            (5, 15)    # 边界情况
        ]
        
        for max_int, target in test_cases:
            start = time.time()
            result = BitMaskDP.can_i_win(max_int, target)
            elapsed = (time.time() - start) * 1e6  # 微秒
            
            print(f"maxChoosableInteger={max_int}, desiredTotal={target}")
            print(f"  结果: {'能赢' if result else '不能赢'}, 耗时: {elapsed:.2f} μs")
    
    @staticmethod
    def test_min_stickers():
        """测试贴纸拼词性能"""
        print("\n=== 贴纸拼词性能测试 ===")
        
        stickers = ["with", "example", "science"]
        target = "thehat"
        
        start = time.time()
        result = BitMaskDP.min_stickers(stickers, target)
        elapsed = (time.time() - start) * 1e6  # 微秒
        
        print(f"贴纸: {stickers}")
        print(f"目标: {target}")
        print(f"最少需要贴纸: {result}")
        print(f"耗时: {elapsed:.2f} μs")
    
    @staticmethod
    def run_unit_tests():
        """运行单元测试"""
        print("=== 状态压缩DP单元测试 ===")
        
        # 测试我能赢吗
        assert BitMaskDP.can_i_win(10, 11) == True
        print("can_i_win测试通过")
        
        # 测试子集和
        nums = [3, 34, 4, 12, 5, 2]
        assert BitMaskDP.subset_sum(nums, 9) == True
        assert BitMaskDP.subset_sum(nums, 30) == False
        print("subset_sum测试通过")
        
        print("所有单元测试通过!")
    
    @staticmethod
    def complexity_analysis():
        """复杂度分析"""
        print("\n=== 复杂度分析 ===")
        
        algorithms = {
            "can_i_win": {
                "time": "O(2^n * n)",
                "space": "O(2^n)",
                "desc": "记忆化搜索+状态压缩"
            },
            "min_stickers": {
                "time": "O(2^m * n * k)",
                "space": "O(2^m)",
                "desc": "状态压缩BFS"
            },
            "shortest_superstring": {
                "time": "O(2^n * n^2)",
                "space": "O(2^n * n)",
                "desc": "状态压缩DP+重叠计算"
            },
            "hamiltonian_path": {
                "time": "O(2^n * n^2)",
                "space": "O(2^n * n)",
                "desc": "状态压缩DP"
            }
        }
        
        for name, info in algorithms.items():
            print(f"{name}:")
            print(f"  时间复杂度: {info['time']}")
            print(f"  空间复杂度: {info['space']}")
            print(f"  描述: {info['desc']}")
            print()


def main():
    """主函数"""
    print("状态压缩动态规划算法实现")
    print("使用位掩码技术解决复杂的组合优化问题")
    print("=" * 50)
    
    # 运行单元测试
    PerformanceTester.run_unit_tests()
    
    # 运行性能测试
    PerformanceTester.test_can_i_win()
    PerformanceTester.test_min_stickers()
    
    # 复杂度分析
    PerformanceTester.complexity_analysis()
    
    # 示例使用
    print("\n=== 示例使用 ===")
    
    # 我能赢吗示例
    print("我能赢吗示例:")
    result = BitMaskDP.can_i_win(10, 11)
    print(f"maxChoosableInteger=10, desiredTotal=11 -> {'能赢' if result else '不能赢'}")
    
    # 子集和问题示例
    nums = [3, 34, 4, 12, 5, 2]
    target = 9
    print(f"\n子集和问题示例:")
    print(f"数组: {nums}, 目标: {target}")
    result = BitMaskDP.subset_sum(nums, target)
    print(f"结果: {'存在' if result else '不存在'}")


if __name__ == "__main__":
    main()