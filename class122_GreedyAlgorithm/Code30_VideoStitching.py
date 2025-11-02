import time
import random
from typing import List

class Code30_VideoStitching:
    """
    视频拼接
    
    题目描述：
    你将会获得一系列视频片段，这些片段来自于一项持续时长为 time 秒的体育赛事。
    这些片段可能有所重叠，也可能长度不一。使用数组 clips 描述所有的视频片段，
    其中 clips[i] = [starti, endi] 表示：某个视频片段开始于 starti 并于 endi 结束。
    甚至可以对这些片段自由地再剪辑。例如，片段 [0, 7] 可以剪切成 [0, 1] + [1, 3] + [3, 7] 三部分。
    我们需要将这些片段进行再剪辑，并将剪辑后的内容拼接成覆盖整个运动过程的片段（[0, time]）。
    返回所需片段的最小数目，如果无法完成该任务，则返回 -1。
    
    来源：LeetCode 1024
    链接：https://leetcode.cn/problems/video-stitching/
    
    算法思路：
    使用贪心算法：
    1. 将片段按开始时间排序，如果开始时间相同则按结束时间降序排序
    2. 维护当前覆盖的最远位置 cur_end 和下一个要覆盖的位置 next_end
    3. 遍历排序后的片段：
        - 如果片段的开始时间大于当前覆盖的最远位置，说明有间隔，无法拼接
        - 如果片段的开始时间小于等于当前覆盖的最远位置，更新下一个要覆盖的位置
        - 当遍历到当前覆盖范围的边界时，增加片段计数并更新当前覆盖范围
    
    时间复杂度：O(n * logn) - 排序的时间复杂度
    空间复杂度：O(1) - 只使用常数空间
    
    关键点分析：
    - 贪心策略：每次选择能覆盖当前范围且延伸最远的片段
    - 排序策略：按开始时间排序，开始时间相同时按结束时间降序
    - 边界处理：检查是否能覆盖整个区间 [0, time]
    
    工程化考量：
    - 输入验证：检查clips数组和time参数的有效性
    - 边界处理：处理time=0的情况
    - 性能优化：避免不必要的排序操作
    """
    
    @staticmethod
    def video_stitching(clips: List[List[int]], time: int) -> int:
        """
        视频拼接的最小片段数
        
        Args:
            clips: 视频片段数组
            time: 目标时长
            
        Returns:
            int: 最小片段数，如果无法拼接返回-1
        """
        # 输入验证
        if not clips:
            return 0 if time == 0 else -1
        if time < 0:
            raise ValueError("时间不能为负数")
        if time == 0:
            return 0
        
        # 按开始时间排序，开始时间相同时按结束时间降序
        clips.sort(key=lambda x: (x[0], -x[1]))
        
        count = 0  # 片段计数
        cur_end = 0  # 当前覆盖的最远位置
        next_end = 0  # 下一个要覆盖的位置
        i = 0  # 当前处理的片段索引
        n = len(clips)
        
        while i < n and cur_end < time:
            # 找到所有开始时间小于等于cur_end的片段中，结束时间最远的
            while i < n and clips[i][0] <= cur_end:
                next_end = max(next_end, clips[i][1])
                i += 1
            
            # 如果没有找到可以扩展的片段，说明无法拼接
            if cur_end == next_end:
                return -1
            
            # 选择当前片段，更新当前覆盖范围
            count += 1
            cur_end = next_end
            
            # 如果已经覆盖了目标范围，提前结束
            if cur_end >= time:
                break
        
        # 检查是否覆盖了整个区间 [0, time]
        return count if cur_end >= time else -1
    
    @staticmethod
    def video_stitching_dp(clips: List[List[int]], time: int) -> int:
        """
        另一种实现：使用动态规划思想
        时间复杂度：O(n * time)
        空间复杂度：O(time)
        """
        if not clips:
            return 0 if time == 0 else -1
        if time < 0:
            raise ValueError("时间不能为负数")
        if time == 0:
            return 0
        
        # dp[i] 表示覆盖区间 [0, i] 所需的最小片段数
        dp = [float('inf')] * (time + 1)
        dp[0] = 0
        
        for i in range(1, time + 1):
            for clip in clips:
                start, end = clip
                # 如果当前片段可以覆盖到i
                if start < i <= end:
                    dp[i] = min(dp[i], dp[start] + 1)
        
        return dp[time] if dp[time] != float('inf') else -1
    
    @staticmethod
    def video_stitching_merge(clips: List[List[int]], time: int) -> int:
        """
        使用区间合并的思路
        时间复杂度：O(n * logn)
        空间复杂度：O(1)
        """
        if not clips:
            return 0 if time == 0 else -1
        if time < 0:
            raise ValueError("时间不能为负数")
        if time == 0:
            return 0
        
        # 按开始时间排序
        clips.sort(key=lambda x: x[0])
        
        count = 0
        current_end = 0
        next_end = 0
        index = 0
        n = len(clips)
        
        while current_end < time:
            count += 1
            
            # 找到所有开始时间小于等于current_end的片段中，结束时间最大的
            while index < n and clips[index][0] <= current_end:
                next_end = max(next_end, clips[index][1])
                index += 1
            
            # 如果没有进展，说明无法拼接
            if next_end == current_end:
                return -1
            
            current_end = next_end
            
            # 如果已经覆盖了目标范围，提前结束
            if current_end >= time:
                break
            
            # 如果已经处理完所有片段但还没有覆盖完，返回-1
            if index >= n:
                return -1
        
        return count
    
    @staticmethod
    def run_tests():
        """运行测试用例"""
        print("=== 视频拼接测试 ===")
        
        # 测试用例1: clips = [[0,2],[4,6],[8,10],[1,9],[1,5],[5,9]], time = 10
        clips1 = [[0,2],[4,6],[8,10],[1,9],[1,5],[5,9]]
        time1 = 10
        print(f"测试用例1:")
        print(f"Clips: {clips1}")
        print(f"Time: {time1}")
        result1 = Code30_VideoStitching.video_stitching(clips1, time1)
        result1_dp = Code30_VideoStitching.video_stitching_dp(clips1, time1)
        result1_merge = Code30_VideoStitching.video_stitching_merge(clips1, time1)
        print(f"贪心结果: {result1}")  # 期望: 3
        print(f"DP结果: {result1_dp}")  # 期望: 3
        print(f"合并结果: {result1_merge}")  # 期望: 3
        
        # 测试用例2: clips = [[0,1],[1,2]], time = 5
        clips2 = [[0,1],[1,2]]
        time2 = 5
        print(f"\n测试用例2:")
        print(f"Clips: {clips2}")
        print(f"Time: {time2}")
        result2 = Code30_VideoStitching.video_stitching(clips2, time2)
        result2_dp = Code30_VideoStitching.video_stitching_dp(clips2, time2)
        result2_merge = Code30_VideoStitching.video_stitching_merge(clips2, time2)
        print(f"贪心结果: {result2}")  # 期望: -1
        print(f"DP结果: {result2_dp}")  # 期望: -1
        print(f"合并结果: {result2_merge}")  # 期望: -1
        
        # 测试用例3: clips = [[0,4],[2,8]], time = 5
        clips3 = [[0,4],[2,8]]
        time3 = 5
        print(f"\n测试用例3:")
        print(f"Clips: {clips3}")
        print(f"Time: {time3}")
        result3 = Code30_VideoStitching.video_stitching(clips3, time3)
        result3_dp = Code30_VideoStitching.video_stitching_dp(clips3, time3)
        result3_merge = Code30_VideoStitching.video_stitching_merge(clips3, time3)
        print(f"贪心结果: {result3}")  # 期望: 2
        print(f"DP结果: {result3_dp}")  # 期望: 2
        print(f"合并结果: {result3_merge}")  # 期望: 2
        
        # 测试用例4: 空数组, time = 0
        clips4 = []
        time4 = 0
        print(f"\n测试用例4:")
        print(f"Clips: {clips4}")
        print(f"Time: {time4}")
        result4 = Code30_VideoStitching.video_stitching(clips4, time4)
        result4_dp = Code30_VideoStitching.video_stitching_dp(clips4, time4)
        result4_merge = Code30_VideoStitching.video_stitching_merge(clips4, time4)
        print(f"贪心结果: {result4}")  # 期望: 0
        print(f"DP结果: {result4_dp}")  # 期望: 0
        print(f"合并结果: {result4_merge}")  # 期望: 0
        
        # 测试用例5: 单个片段覆盖整个区间
        clips5 = [[0,10]]
        time5 = 10
        print(f"\n测试用例5:")
        print(f"Clips: {clips5}")
        print(f"Time: {time5}")
        result5 = Code30_VideoStitching.video_stitching(clips5, time5)
        result5_dp = Code30_VideoStitching.video_stitching_dp(clips5, time5)
        result5_merge = Code30_VideoStitching.video_stitching_merge(clips5, time5)
        print(f"贪心结果: {result5}")  # 期望: 1
        print(f"DP结果: {result5_dp}")  # 期望: 1
        print(f"合并结果: {result5_merge}")  # 期望: 1
    
    @staticmethod
    def performance_test():
        """性能测试方法"""
        # 生成大规模测试数据
        large_clips = []
        for _ in range(10000):
            start = random.randint(0, 1000)
            end = start + random.randint(1, 100)
            large_clips.append([start, end])
        time_val = 1000
        
        print("\n=== 性能测试 ===")
        
        start_time1 = time.time()
        result1 = Code30_VideoStitching.video_stitching(large_clips, time_val)
        end_time1 = time.time()
        print(f"贪心算法执行时间: {(end_time1 - start_time1) * 1000:.2f} 毫秒")
        print(f"结果: {result1}")
        
        start_time2 = time.time()
        result2 = Code30_VideoStitching.video_stitching_dp(large_clips, time_val)
        end_time2 = time.time()
        print(f"动态规划执行时间: {(end_time2 - start_time2) * 1000:.2f} 毫秒")
        print(f"结果: {result2}")
        
        start_time3 = time.time()
        result3 = Code30_VideoStitching.video_stitching_merge(large_clips, time_val)
        end_time3 = time.time()
        print(f"合并算法执行时间: {(end_time3 - start_time3) * 1000:.2f} 毫秒")
        print(f"结果: {result3}")
    
    @staticmethod
    def analyze_complexity():
        """算法复杂度分析"""
        print("\n=== 算法复杂度分析 ===")
        print("贪心算法:")
        print("- 时间复杂度: O(n * logn)")
        print("  - 排序: O(n * logn)")
        print("  - 遍历: O(n)")
        print("- 空间复杂度: O(1)")
        
        print("\n动态规划:")
        print("- 时间复杂度: O(n * time)")
        print("  - 外层循环: O(time)")
        print("  - 内层循环: O(n)")
        print("- 空间复杂度: O(time)")
        
        print("\n合并算法:")
        print("- 时间复杂度: O(n * logn)")
        print("  - 排序: O(n * logn)")
        print("  - 遍历: O(n)")
        print("- 空间复杂度: O(1)")
        
        print("\n贪心策略证明:")
        print("1. 按开始时间排序可以确保覆盖连续性")
        print("2. 选择结束时间最远的片段是最优选择")
        print("3. 数学归纳法证明贪心选择性质")
        
        print("\n工程化考量:")
        print("1. 输入验证：处理非法输入和边界情况")
        print("2. 性能优化：选择合适的算法策略")
        print("3. 可读性：清晰的算法逻辑和注释")
        print("4. 测试覆盖：全面的测试用例设计")

def main():
    """主函数"""
    Code30_VideoStitching.run_tests()
    Code30_VideoStitching.performance_test()
    Code30_VideoStitching.analyze_complexity()

if __name__ == "__main__":
    main()