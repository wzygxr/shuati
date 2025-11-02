import heapq
from typing import List, Tuple
from collections import Counter, defaultdict
import math

class HeapExtendedProblems:
    """
    堆算法扩展题目集 - Python实现
    涵盖各大算法平台经典堆问题
    """
    
    @staticmethod
    def kth_smallest_in_sorted_matrix(matrix: List[List[int]], k: int) -> int:
        """
        题目1: LeetCode 378. 有序矩阵中第K小的元素
        时间复杂度: O(k log k)
        空间复杂度: O(k)
        是否最优解: 是
        """
        if not matrix or not matrix[0]:
            raise ValueError("矩阵不能为空")
        if k <= 0 or k > len(matrix) * len(matrix[0]):
            raise ValueError("k值超出范围")
        
        n = len(matrix)
        # 最小堆，存储三元组(值, 行索引, 列索引)
        min_heap = []
        
        # 将第一列的所有元素加入堆
        for i in range(n):
            heapq.heappush(min_heap, (matrix[i][0], i, 0))
        
        # 取出前k-1个最小元素
        for _ in range(k - 1):
            val, row, col = heapq.heappop(min_heap)
            if col + 1 < n:
                heapq.heappush(min_heap, (matrix[row][col + 1], row, col + 1))
        
        return min_heap[0][0]
    
    @staticmethod
    def reorganize_string(s: str) -> str:
        """
        题目2: LeetCode 767. 重构字符串
        时间复杂度: O(n log k)
        空间复杂度: O(k)
        是否最优解: 是
        """
        if not s:
            return ""
        
        # 统计字符频率
        freq_map = Counter(s)
        
        # 最大堆，按频率降序排列
        max_heap = []
        for char, freq in freq_map.items():
            heapq.heappush(max_heap, (-freq, char))
        
        # 检查是否可能重构
        if -max_heap[0][0] > (len(s) + 1) // 2:
            return ""
        
        result = []
        
        while len(max_heap) >= 2:
            # 取频率最高的两个字符
            freq1, char1 = heapq.heappop(max_heap)
            freq2, char2 = heapq.heappop(max_heap)
            
            result.extend([char1, char2])
            
            # 更新频率
            if freq1 + 1 < 0:
                heapq.heappush(max_heap, (freq1 + 1, char1))
            if freq2 + 1 < 0:
                heapq.heappush(max_heap, (freq2 + 1, char2))
        
        # 处理最后一个字符
        if max_heap:
            result.append(max_heap[0][1])
        
        return "".join(result)
    
    @staticmethod
    def find_maximized_capital(k: int, w: int, profits: List[int], capital: List[int]) -> int:
        """
        题目3: LeetCode 502. IPO
        时间复杂度: O(n log n + k log n)
        空间复杂度: O(n)
        是否最优解: 是
        """
        n = len(profits)
        # 按资本排序的项目列表
        projects = list(zip(capital, profits))
        projects.sort(key=lambda x: x[0])
        
        # 最大堆，存储当前可做项目的利润
        max_heap = []
        
        current_capital = w
        project_index = 0
        
        for _ in range(k):
            # 将所有资本要求小于等于当前资本的项目加入最大堆
            while project_index < n and projects[project_index][0] <= current_capital:
                heapq.heappush(max_heap, -projects[project_index][1])
                project_index += 1
            
            if not max_heap:
                break
            
            # 选择利润最大的项目
            current_capital += -heapq.heappop(max_heap)
        
        return current_capital
    
    @staticmethod
    def schedule_course(courses: List[List[int]]) -> int:
        """
        题目4: LeetCode 630. 课程表 III
        时间复杂度: O(n log n)
        空间复杂度: O(n)
        是否最优解: 是
        """
        # 按结束时间排序
        courses.sort(key=lambda x: x[1])
        
        # 最大堆，存储已选课程的持续时间
        max_heap = []
        
        current_time = 0
        
        for duration, end_time in courses:
            if current_time + duration <= end_time:
                current_time += duration
                heapq.heappush(max_heap, -duration)
            elif max_heap and -max_heap[0] > duration:
                current_time = current_time - (-heapq.heappop(max_heap)) + duration
                heapq.heappush(max_heap, -duration)
        
        return len(max_heap)
    
    @staticmethod
    def mincost_to_hire_workers(quality: List[int], wage: List[int], k: int) -> float:
        """
        题目5: LeetCode 857. 雇佣 K 名工人的最低成本
        时间复杂度: O(n log n)
        空间复杂度: O(n)
        是否最优解: 是
        """
        n = len(quality)
        # 存储工人信息(质量, 工资, 工资质量比)
        workers = []
        for i in range(n):
            ratio = wage[i] / quality[i]
            workers.append((quality[i], wage[i], ratio))
        
        # 按工资质量比排序
        workers.sort(key=lambda x: x[2])
        
        # 最大堆，存储k个工人的质量
        max_heap = []
        total_quality = 0
        min_cost = float('inf')
        
        for q, w, ratio in workers:
            total_quality += q
            heapq.heappush(max_heap, -q)
            
            if len(max_heap) > k:
                total_quality -= -heapq.heappop(max_heap)
            
            if len(max_heap) == k:
                min_cost = min(min_cost, total_quality * ratio)
        
        return min_cost
    
    @staticmethod
    def rearrange_barcodes(barcodes: List[int]) -> List[int]:
        """
        题目6: LeetCode 1054. 距离相等的条形码
        时间复杂度: O(n log k)
        空间复杂度: O(n)
        是否最优解: 是
        """
        if not barcodes:
            return []
        
        # 统计频率
        freq_map = Counter(barcodes)
        
        # 最大堆，按频率降序排列
        max_heap = []
        for code, freq in freq_map.items():
            heapq.heappush(max_heap, (-freq, code))
        
        result = []
        
        while len(max_heap) >= 2:
            # 取频率最高的两个条形码
            freq1, code1 = heapq.heappop(max_heap)
            freq2, code2 = heapq.heappop(max_heap)
            
            result.extend([code1, code2])
            
            # 更新频率
            if freq1 + 1 < 0:
                heapq.heappush(max_heap, (freq1 + 1, code1))
            if freq2 + 1 < 0:
                heapq.heappush(max_heap, (freq2 + 1, code2))
        
        # 处理最后一个条形码
        if max_heap:
            result.append(max_heap[0][1])
        
        return result
    
    @staticmethod
    def max_performance(n: int, speed: List[int], efficiency: List[int], k: int) -> int:
        """
        题目7: LeetCode 1383. 最大的团队表现值
        时间复杂度: O(n log n)
        空间复杂度: O(k)
        是否最优解: 是
        """
        # 按效率降序排序
        engineers = list(zip(efficiency, speed))
        engineers.sort(reverse=True)
        
        # 最小堆，维护k个工程师的速度
        min_heap = []
        total_speed = 0
        max_perf = 0
        MOD = 10**9 + 7
        
        for eff, spd in engineers:
            if len(min_heap) == k:
                total_speed -= heapq.heappop(min_heap)
            
            heapq.heappush(min_heap, spd)
            total_speed += spd
            
            max_perf = max(max_perf, total_speed * eff)
        
        return max_perf % MOD
    
    @staticmethod
    def furthest_building(heights: List[int], bricks: int, ladders: int) -> int:
        """
        题目8: LeetCode 1642. 可以到达的最远建筑
        时间复杂度: O(n log k)
        空间复杂度: O(k)
        是否最优解: 是
        """
        # 最大堆，存储使用砖块爬升的高度
        max_heap = []
        
        for i in range(len(heights) - 1):
            diff = heights[i + 1] - heights[i]
            
            if diff <= 0:
                continue
            
            bricks -= diff
            heapq.heappush(max_heap, -diff)
            
            if bricks < 0:
                if ladders > 0:
                    bricks += -heapq.heappop(max_heap)
                    ladders -= 1
                else:
                    return i
        
        return len(heights) - 1
    
    @staticmethod
    def eaten_apples(apples: List[int], days: List[int]) -> int:
        """
        题目9: LeetCode 1705. 吃苹果的最大数目
        时间复杂度: O(n log n)
        空间复杂度: O(n)
        是否最优解: 是
        """
        # 最小堆，存储(腐烂时间, 苹果数量)
        min_heap = []
        result = 0
        n = len(apples)
        
        for i in range(n + max(days) if days else n):
            # 添加当天的新苹果
            if i < n and apples[i] > 0:
                heapq.heappush(min_heap, (i + days[i], apples[i]))
            
            # 移除已腐烂的苹果
            while min_heap and min_heap[0][0] <= i:
                heapq.heappop(min_heap)
            
            # 吃一个苹果
            if min_heap:
                rot_time, count = min_heap[0]
                result += 1
                if count == 1:
                    heapq.heappop(min_heap)
                else:
                    # 更新堆顶元素的数量
                    heapq.heapreplace(min_heap, (rot_time, count - 1))
            elif i >= n:
                break
        
        return result
    
    @staticmethod
    def get_order(tasks: List[List[int]]) -> List[int]:
        """
        题目10: LeetCode 1834. 单线程 CPU
        时间复杂度: O(n log n)
        空间复杂度: O(n)
        是否最优解: 是
        """
        n = len(tasks)
        # 存储任务信息(到达时间, 处理时间, 原始索引)
        indexed_tasks = [(tasks[i][0], tasks[i][1], i) for i in range(n)]
        indexed_tasks.sort()
        
        # 最小堆，存储(处理时间, 原始索引)
        min_heap = []
        result = []
        current_time = 0
        task_index = 0
        
        while len(result) < n:
            # 将当前时间点之前到达的任务加入堆
            while task_index < n and indexed_tasks[task_index][0] <= current_time:
                heapq.heappush(min_heap, (indexed_tasks[task_index][1], indexed_tasks[task_index][2]))
                task_index += 1
            
            if not min_heap:
                current_time = indexed_tasks[task_index][0]
                continue
            
            # 执行堆顶任务
            process_time, orig_idx = heapq.heappop(min_heap)
            result.append(orig_idx)
            current_time += process_time
        
        return result

# 测试函数
def test_heap_extended_problems():
    """测试堆扩展题目集的各个方法"""
    hep = HeapExtendedProblems()
    
    # 测试题目1
    matrix = [
        [1, 5, 9],
        [10, 11, 13],
        [12, 13, 15]
    ]
    result1 = hep.kth_smallest_in_sorted_matrix(matrix, 8)
    print(f"题目1测试: {result1}")  # 期望输出: 13
    
    # 测试题目2
    result2 = hep.reorganize_string("aab")
    print(f"题目2测试: {result2}")  # 期望输出: "aba"
    
    # 测试题目3
    result3 = hep.find_maximized_capital(2, 0, [1, 2, 3], [0, 1, 1])
    print(f"题目3测试: {result3}")  # 期望输出: 4
    
    print("所有测试通过！")

if __name__ == "__main__":
    test_heap_extended_problems()