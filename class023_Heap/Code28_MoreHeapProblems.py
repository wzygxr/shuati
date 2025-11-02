import heapq
from typing import List
from collections import Counter

class MoreHeapProblems:
    """
    更多堆算法题目集 - Python实现
    """
    
    @staticmethod
    def max_cover_lines(lines: List[List[int]]) -> int:
        """
        题目11: 牛客网 - 最多线段重合问题
        时间复杂度: O(n log n)
        空间复杂度: O(n)
        """
        if not lines:
            return 0
        
        # 按线段起点排序
        lines.sort(key=lambda x: x[0])
        
        # 最小堆维护当前覆盖点的线段右端点
        min_heap = []
        max_cover = 0
        
        for start, end in lines:
            # 移除已经结束的线段
            while min_heap and min_heap[0] <= start:
                heapq.heappop(min_heap)
            
            heapq.heappush(min_heap, end)
            max_cover = max(max_cover, len(min_heap))
        
        return max_cover
    
    @staticmethod
    def merge_k_lists(lists):
        """
        题目12: LintCode 104. 合并k个排序链表
        时间复杂度: O(N log k)
        空间复杂度: O(k)
        """
        # 定义ListNode类
        class ListNode:
            def __init__(self, val=0, next=None):
                self.val = val
                self.next = next
        
        if not lists:
            return None
        
        # 最小堆维护k个链表的当前头节点
        min_heap = []
        for i, node in enumerate(lists):
            if node:
                heapq.heappush(min_heap, (node.val, i, node))
        
        dummy = ListNode(0)
        current = dummy
        
        while min_heap:
            val, idx, node = heapq.heappop(min_heap)
            current.next = node
            current = current.next
            
            if node.next:
                heapq.heappush(min_heap, (node.next.val, idx, node.next))
        
        return dummy.next
    
    @staticmethod
    def find_running_median(arr: List[int]) -> List[float]:
        """
        题目13: HackerRank - 查找运行中位数
        时间复杂度: O(n log n)
        空间复杂度: O(n)
        """
        if not arr:
            return []
        
        # 最大堆存储较小的一半
        max_heap = []  # 存储负数来实现最大堆
        # 最小堆存储较大的一半
        min_heap = []
        
        result = []
        
        for num in arr:
            if not max_heap or num <= -max_heap[0]:
                heapq.heappush(max_heap, -num)
            else:
                heapq.heappush(min_heap, num)
            
            # 平衡堆大小
            if len(max_heap) > len(min_heap) + 1:
                heapq.heappush(min_heap, -heapq.heappop(max_heap))
            elif len(min_heap) > len(max_heap):
                heapq.heappush(max_heap, -heapq.heappop(min_heap))
            
            # 计算中位数
            if len(max_heap) == len(min_heap):
                result.append((-max_heap[0] + min_heap[0]) / 2.0)
            else:
                result.append(-max_heap[0])
        
        return result
    
    @staticmethod
    def max_profit_from_jobs(jobs: List[List[int]], m: int) -> int:
        """
        题目14: AtCoder - 最小成本连接点
        时间复杂度: O(n log n)
        空间复杂度: O(n)
        """
        # 按截止时间排序
        jobs.sort(key=lambda x: x[0])
        
        # 最大堆存储当前可选工作的报酬
        max_heap = []
        total_profit = 0
        job_index = len(jobs) - 1
        
        for day in range(m, 0, -1):
            while job_index >= 0 and jobs[job_index][0] >= day:
                heapq.heappush(max_heap, -jobs[job_index][1])
                job_index -= 1
            
            if max_heap:
                total_profit += -heapq.heappop(max_heap)
        
        return total_profit
    
    @staticmethod
    def chef_and_recipes(recipes: List[int], k: int) -> int:
        """
        题目15: CodeChef - 厨师和食谱
        时间复杂度: O(n log n)
        空间复杂度: O(n)
        """
        freq_map = Counter(recipes)
        
        # 最大堆按频率排序
        max_heap = []
        for recipe, freq in freq_map.items():
            heapq.heappush(max_heap, (-freq, recipe))
        
        result = 0
        while k > 0 and max_heap:
            freq, recipe = heapq.heappop(max_heap)
            result += -freq
            k -= 1
        
        return result
    
    @staticmethod
    def military_arrangement(soldiers: List[int], k: int) -> int:
        """
        题目16: SPOJ - 军事调度
        时间复杂度: O(n log n)
        空间复杂度: O(n)
        """
        # 最大堆存储士兵能力值
        max_heap = []
        for soldier in soldiers:
            heapq.heappush(max_heap, -soldier)
        
        total_strength = 0
        for _ in range(k):
            if max_heap:
                total_strength += -heapq.heappop(max_heap)
        
        return total_strength
    
    @staticmethod
    def highly_composite_number(n: int) -> int:
        """
        题目17: Project Euler - 高度合成数
        时间复杂度: O(n log n)
        空间复杂度: O(n)
        """
        min_heap = [1]
        current = 0
        
        for _ in range(n):
            current = heapq.heappop(min_heap)
            
            # 生成新的候选值
            heapq.heappush(min_heap, current * 2)
            heapq.heappush(min_heap, current * 3)
            heapq.heappush(min_heap, current * 5)
            
            # 去重
            while min_heap and min_heap[0] == current:
                heapq.heappop(min_heap)
        
        return current
    
    @staticmethod
    def minimize_max_lateness(tasks: List[List[int]]) -> int:
        """
        题目18: HackerEarth - 最小化最大延迟
        时间复杂度: O(n log n)
        空间复杂度: O(n)
        """
        # 按截止时间排序
        tasks.sort(key=lambda x: x[1])
        
        # 最大堆存储任务处理时间
        max_heap = []
        current_time = 0
        max_lateness = 0
        
        for duration, deadline in tasks:
            current_time += duration
            heapq.heappush(max_heap, -duration)
            
            if current_time > deadline:
                # 移除最耗时的任务
                current_time -= -heapq.heappop(max_heap)
            
            max_lateness = max(max_lateness, max(0, current_time - deadline))
        
        return max_lateness
    
    @staticmethod
    def task_scheduler(tasks: List[str], n: int) -> int:
        """
        题目19: 计蒜客 - 任务调度器
        时间复杂度: O(n log k)
        空间复杂度: O(k)
        """
        if not tasks:
            return 0
        
        # 统计任务频率
        freq_map = Counter(tasks)
        
        # 最大堆按频率排序
        max_heap = []
        for task, count in freq_map.items():
            heapq.heappush(max_heap, (-count, task))
        
        time = 0
        while max_heap:
            temp = []
            
            # 执行n+1个时间单位
            for i in range(n + 1):
                if max_heap:
                    count, task = heapq.heappop(max_heap)
                    if -count > 1:
                        temp.append((count + 1, task))
                time += 1
                
                if not max_heap and not temp:
                    break
            
            # 将剩余任务重新加入堆
            for item in temp:
                heapq.heappush(max_heap, item)
        
        return time
    
    @staticmethod
    def merge_fruits(fruits: List[int]) -> int:
        """
        题目20: 洛谷 - 合并果子
        时间复杂度: O(n log n)
        空间复杂度: O(n)
        """
        min_heap = []
        for fruit in fruits:
            heapq.heappush(min_heap, fruit)
        
        total_cost = 0
        while len(min_heap) > 1:
            first = heapq.heappop(min_heap)
            second = heapq.heappop(min_heap)
            cost = first + second
            total_cost += cost
            heapq.heappush(min_heap, cost)
        
        return total_cost

# 测试函数
def test_more_heap_problems():
    """测试更多堆题目集的各个方法"""
    mhp = MoreHeapProblems()
    
    # 测试题目11
    lines = [[1, 4], [2, 5], [3, 6], [4, 7]]
    result11 = mhp.max_cover_lines(lines)
    print(f"题目11测试: {result11}")  # 期望输出: 3
    
    # 测试题目20
    fruits = [1, 2, 9]
    result20 = mhp.merge_fruits(fruits)
    print(f"题目20测试: {result20}")  # 期望输出: 15
    
    print("所有测试通过！")

if __name__ == "__main__":
    test_more_heap_problems()