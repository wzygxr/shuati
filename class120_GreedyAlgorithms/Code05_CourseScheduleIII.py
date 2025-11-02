"""
课程表III - 贪心算法 + 最大堆解决方案（Python实现）

题目描述：
这里有n门不同的在线课程，按从1到n编号
给你一个数组courses，其中courses[i]=[durationi, lastDayi]表示第i门课将会持续上durationi天课
并且必须在不晚于lastDayi的时候完成
你的学期从第 1 天开始，且不能同时修读两门及两门以上的课程
返回你最多可以修读的课程数目

测试链接：https://leetcode.cn/problems/course-schedule-iii/

算法思想：
贪心算法 + 最大堆（优先队列）
1. 按照课程的截止时间进行排序（截止时间早的排在前面）
2. 使用最大堆来存储已选课程的持续时间
3. 遍历每个课程：
   - 如果当前时间加上课程持续时间不超过截止时间，则选择该课程
   - 如果超过截止时间，则检查是否可以替换已选课程中持续时间最长的课程

时间复杂度分析：
O(n*logn) - 排序需要O(n*logn)，每个课程进出堆一次需要O(logn)

空间复杂度分析：
O(n) - 最坏情况下所有课程都被选中，堆的大小为n

是否为最优解：
是，这是解决该问题的最优解

工程化考量：
1. 边界条件处理：处理空数组、单个课程等特殊情况
2. 输入验证：检查课程时间是否有效（持续时间 > 0，截止时间 > 0）
3. 异常处理：对非法输入进行检查
4. 可读性：使用清晰的变量命名和详细的注释

贪心策略证明：
按照截止时间排序可以保证我们优先考虑截止时间早的课程
使用最大堆来管理已选课程，当遇到冲突时替换持续时间最长的课程，可以最大化课程数量
"""

import heapq
from typing import List

class Code05_CourseScheduleIII:
    
    @staticmethod
    def schedule_course(courses: List[List[int]]) -> int:
        """
        计算最多可以修读的课程数目
        
        Args:
            courses: 课程数组，每个元素是[durationi, lastDayi]
            
        Returns:
            最多可以修读的课程数目
            
        算法步骤：
        1. 按照课程的截止时间进行排序
        2. 使用最大堆存储已选课程的持续时间
        3. 维护当前已使用的时间
        4. 遍历每个课程，动态调整已选课程
        """
        # 输入验证
        if not courses:
            return 0
        
        # 按照课程的截止时间进行排序（截止时间早的排在前面）
        courses.sort(key=lambda x: x[1])
        
        # 最大堆，存储已选课程的持续时间（使用负值实现最大堆）
        heap = []
        current_time = 0  # 当前已使用的时间
        
        for duration, last_day in courses:
            # 验证课程时间有效性
            if duration <= 0 or last_day <= 0:
                raise ValueError("课程持续时间和截止时间必须大于0")
            
            # 如果当前时间加上课程持续时间不超过截止时间
            if current_time + duration <= last_day:
                # 使用负值实现最大堆
                heapq.heappush(heap, -duration)
                current_time += duration
            # 如果超过截止时间，但当前课程的持续时间比已选课程中最长的短
            elif heap and -heap[0] > duration:
                # 替换策略：用当前课程替换已选课程中持续时间最长的课程
                longest_duration = -heapq.heappop(heap)
                current_time += duration - longest_duration
                heapq.heappush(heap, -duration)
            # 其他情况：不选择当前课程
        
        return len(heap)
    
    @staticmethod
    def debug_schedule_course(courses: List[List[int]]) -> int:
        """
        调试版本：打印计算过程中的中间结果
        
        Args:
            courses: 课程数组
            
        Returns:
            最多可以修读的课程数目
        """
        if not courses:
            print("空数组，无法修读任何课程")
            return 0
        
        print("原始课程安排:")
        for i, (duration, last_day) in enumerate(courses):
            print(f"课程{i}: [持续时间={duration}, 截止时间={last_day}]")
        
        # 按照课程的截止时间进行排序
        courses.sort(key=lambda x: x[1])
        
        print("\n按截止时间排序后的课程安排:")
        for i, (duration, last_day) in enumerate(courses):
            print(f"课程{i}: [持续时间={duration}, 截止时间={last_day}]")
        
        heap = []
        current_time = 0
        selected_count = 0
        
        print("\n选课过程:")
        for i, (duration, last_day) in enumerate(courses):
            print(f"\n考虑课程{i}: [持续时间={duration}, 截止时间={last_day}]")
            print(f"当前时间: {current_time}")
            
            # 如果当前时间加上课程持续时间不超过截止时间
            if current_time + duration <= last_day:
                heapq.heappush(heap, -duration)
                current_time += duration
                selected_count += 1
                print(f"选择该课程，当前已选课程数: {selected_count}, 当前时间更新为: {current_time}")
            # 如果超过截止时间，但当前课程的持续时间比已选课程中最长的短
            elif heap and -heap[0] > duration:
                longest_duration = -heapq.heappop(heap)
                print(f"替换策略: 用当前课程(持续时间={duration})替换已选课程中持续时间最长的课程(持续时间={longest_duration})")
                
                current_time += duration - longest_duration
                heapq.heappush(heap, -duration)
                print(f"替换完成，当前时间更新为: {current_time}, 已选课程数保持不变: {selected_count}")
            else:
                print("无法选择该课程，跳过")
            
            # 打印当前堆的内容
            heap_contents = [-x for x in heap]
            heap_contents.sort(reverse=True)
            print(f"当前已选课程的持续时间: {heap_contents}")
        
        print(f"\n最终结果: 最多可以修读 {len(heap)} 门课程")
        return len(heap)
    
    @staticmethod
    def test_schedule_course():
        """
        测试函数：验证课程表III算法的正确性
        """
        print("课程表III算法测试开始")
        print("====================")
        
        # 测试用例1: [[100,200],[200,1300],[1000,1250],[2000,3200]]
        courses1 = [[100,200], [200,1300], [1000,1250], [2000,3200]]
        result1 = Code05_CourseScheduleIII.schedule_course(courses1)
        print("输入: [[100,200],[200,1300],[1000,1250],[2000,3200]]")
        print("输出:", result1)
        print("预期: 3")
        print("✓ 通过" if result1 == 3 else "✗ 失败")
        print()
        
        # 测试用例2: [[1,2]]
        courses2 = [[1,2]]
        result2 = Code05_CourseScheduleIII.schedule_course(courses2)
        print("输入: [[1,2]]")
        print("输出:", result2)
        print("预期: 1")
        print("✓ 通过" if result2 == 1 else "✗ 失败")
        print()
        
        # 测试用例3: [[3,2],[4,3]]
        courses3 = [[3,2], [4,3]]
        result3 = Code05_CourseScheduleIII.schedule_course(courses3)
        print("输入: [[3,2],[4,3]]")
        print("输出:", result3)
        print("预期: 0")
        print("✓ 通过" if result3 == 0 else "✗ 失败")
        print()
        
        # 测试用例4: [[5,5],[4,6],[2,6]]
        courses4 = [[5,5], [4,6], [2,6]]
        result4 = Code05_CourseScheduleIII.schedule_course(courses4)
        print("输入: [[5,5],[4,6],[2,6]]")
        print("输出:", result4)
        print("预期: 2")
        print("✓ 通过" if result4 == 2 else "✗ 失败")
        print()
        
        print("测试结束")
    
    @staticmethod
    def performance_test():
        """
        性能测试：测试算法在大规模数据下的表现
        """
        import time
        import random
        
        print("性能测试开始")
        print("============")
        
        # 生成大规模测试数据
        n = 10000
        courses = []
        for i in range(n):
            duration = random.randint(1, 100)
            last_day = random.randint(duration, 1000)
            courses.append([duration, last_day])
        
        start_time = time.time()
        result = Code05_CourseScheduleIII.schedule_course(courses)
        end_time = time.time()
        
        print(f"数据规模: {n} 门课程")
        print(f"执行时间: {end_time - start_time:.4f} 秒")
        print(f"最多可以修读的课程数: {result}")
        print("性能测试结束")
    
    @staticmethod
    def compare_with_alternative(courses: List[List[int]]) -> None:
        """
        与替代解法对比
        
        Args:
            courses: 课程数组
        """
        print("贪心算法结果:", Code05_CourseScheduleIII.schedule_course(courses))
        
        # 替代解法：暴力回溯（仅用于小规模对比）
        def backtrack(courses, index, current_time, count):
            if index == len(courses):
                return count
            
            # 不选择当前课程
            max_count = backtrack(courses, index + 1, current_time, count)
            
            # 尝试选择当前课程
            duration, last_day = courses[index]
            if current_time + duration <= last_day:
                max_count = max(max_count, backtrack(courses, index + 1, current_time + duration, count + 1))
            
            return max_count
        
        if len(courses) <= 10:  # 只对小规模数据进行暴力计算
            sorted_courses = sorted(courses, key=lambda x: x[1])
            brute_result = backtrack(sorted_courses, 0, 0, 0)
            print("暴力回溯结果:", brute_result)
        else:
            print("暴力回溯: 数据规模太大，无法计算")


def main():
    """
    主函数：运行测试
    """
    print("课程表III - 贪心算法 + 最大堆解决方案（Python实现）")
    print("===========================================")
    
    # 运行基础测试
    Code05_CourseScheduleIII.test_schedule_course()
    
    print("\n调试模式示例:")
    debug_courses = [[100,200], [200,1300], [1000,1250], [2000,3200]]
    print("对测试用例 [[100,200],[200,1300],[1000,1250],[2000,3200]] 进行调试跟踪:")
    debug_result = Code05_CourseScheduleIII.debug_schedule_course(debug_courses)
    print("最终结果:", debug_result)
    
    print("\n算法分析:")
    print("- 时间复杂度: O(n*logn) - 排序需要O(n*logn)，每个课程进出堆一次需要O(logn)")
    print("- 空间复杂度: O(n) - 最坏情况下所有课程都被选中，堆的大小为n")
    print("- 贪心策略: 按照截止时间排序，使用最大堆管理已选课程")
    print("- 最优性: 这种贪心策略能够得到全局最优解")
    print("- Python特性: 使用负值实现最大堆")
    print("- 替换策略: 当遇到冲突时，用短课程替换长课程可以最大化课程数量")
    
    # 可选：运行性能测试
    # print("\n性能测试:")
    # Code05_CourseScheduleIII.performance_test()
    
    # 可选：与替代解法对比
    # print("\n算法对比:")
    # test_courses = [[100,200], [200,1300], [1000,1250], [2000,3200]]
    # Code05_CourseScheduleIII.compare_with_alternative(test_courses)


if __name__ == "__main__":
    main()