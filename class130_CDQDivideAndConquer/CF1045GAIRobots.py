# CF1045G AI robots
# 平台: Codeforces
# 难度: 2200
# 标签: CDQ分治, 二维数点
# 链接: https://codeforces.com/problemset/problem/1045/G
# 
# 题目描述:
# 有n个机器人，每个机器人有一个位置x_i，视野范围r_i和智商q_i。
# 机器人i和机器人j能够相互交流当且仅当：
# 1. 机器人i能看到机器人j（|x_i - x_j| <= r_i）
# 2. 机器人j能看到机器人i（|x_i - x_j| <= r_j）
# 3. 他们的智商差不超过K（|q_i - q_j| <= K）
# 求有多少对机器人能够相互交流。
# 
# 解题思路:
# 使用CDQ分治解决三维偏序问题。
# 1. 第一维：按视野范围r从大到小排序
# 2. 第二维：位置x
# 3. 第三维：智商q
# 
# 由于要求相互看见，我们按视野从大到小排序后，
# 只需考虑右边（视野小的）能否被左边（视野大的）看见。
# 
# 时间复杂度：O(n log^2 n)
# 空间复杂度：O(n)

class Robot:
    def __init__(self, x, r, q, id):
        self.x = x
        self.r = r
        self.q = q
        self.id = id

class Solution:
    def __init__(self):
        self.bit = []  # 树状数组
        self.K = 0  # 智商差限制
    
    def lowbit(self, x):
        return x & (-x)
    
    def add(self, x, v, n):
        i = x
        while i <= n:
            self.bit[i] += v
            i += self.lowbit(i)
    
    def query(self, x):
        res = 0
        i = x
        while i > 0:
            res += self.bit[i]
            i -= self.lowbit(i)
        return res
    
    def solveAIRobots(self, x, r, q, K):
        n = len(x)
        if n == 0:
            return 0
        
        self.K = K
        
        # 创建机器人数组并按视野范围从大到小排序
        robots = []
        for i in range(n):
            robots.append(Robot(x[i], r[i], q[i], i))
        
        robots.sort(key=lambda robot: (-robot.r, robot.x, robot.q))  # 从大到小排序视野范围
        
        # 离散化q值
        sorted_q = sorted(q)
        unique_size = self.remove_duplicates(sorted_q)
        
        self.bit = [0] * (n + 1)  # 树状数组
        
        result = 0
        
        # 从左到右处理每个机器人
        for i in range(n):
            # 使用二分查找找到离散化后的值
            q_id = self.binary_search(sorted_q, 0, unique_size, robots[i].q) + 1
            
            # 查询在当前位置左侧，且智商在[robots[i].q-K, robots[i].q+K]范围内的机器人数量
            lower_bound = self.binary_search_lower(sorted_q, unique_size, robots[i].q - K) + 1
            upper_bound = self.binary_search_upper(sorted_q, unique_size, robots[i].q + K)
            
            # 查询范围内的机器人数量
            result += self.query(upper_bound - 1) - self.query(lower_bound - 1)
            
            # 将当前机器人插入到数据结构中
            self.add(q_id, 1, n)
        
        return result
    
    # 去重函数
    def remove_duplicates(self, nums):
        if len(nums) == 0:
            return 0
        unique_size = 1
        for i in range(1, len(nums)):
            if nums[i] != nums[unique_size - 1]:
                nums[unique_size] = nums[i]
                unique_size += 1
        return unique_size
    
    # 二分查找函数
    def binary_search(self, arr, l, r, target):
        left, right = l, r - 1
        while left <= right:
            mid = (left + right) // 2
            if arr[mid] >= target:
                right = mid - 1
            else:
                left = mid + 1
        return left
    
    # 二分查找下界
    def binary_search_lower(self, arr, size, target):
        left, right = 0, size - 1
        while left <= right:
            mid = (left + right) // 2
            if arr[mid] >= target:
                right = mid - 1
            else:
                left = mid + 1
        return left
    
    # 二分查找上界
    def binary_search_upper(self, arr, size, target):
        left, right = 0, size - 1
        while left <= right:
            mid = (left + right) // 2
            if arr[mid] > target:
                right = mid - 1
            else:
                left = mid + 1
        return left

def main():
    solution = Solution()
    
    # 测试用例
    x1 = [1, 2, 3]
    r1 = [3, 2, 1]
    q1 = [1, 2, 3]
    K1 = 1
    result1 = solution.solveAIRobots(x1, r1, q1, K1)
    
    print("输入: x = [1,2,3], r = [3,2,1], q = [1,2,3], K = 1")
    print("输出:", result1)

if __name__ == "__main__":
    main()