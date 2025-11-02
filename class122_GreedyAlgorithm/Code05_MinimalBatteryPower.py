# 执行所有任务的最少初始电量
# 每一个任务有两个参数，需要耗费的电量、至少多少电量才能开始这个任务
# 返回手机至少需要多少的初始电量，才能执行完所有的任务
# 测试链接 : https://leetcode.cn/problems/minimum-initial-energy-to-finish-tasks/

def minimumEffort(tasks):
    """
    计算执行所有任务的最少初始电量
    
    算法思路：
    贪心策略：
    1. 按照(至少电量-耗费电量)的差值降序排序任务
    2. 按排序后的顺序执行任务，维护当前所需最少初始电量
    
    正确性证明：
    1. 对于两个任务a和b，如果a先执行，需要的初始电量是max(need_a, need_b + cost_a)
       如果b先执行，需要的初始电量是max(need_b, need_a + cost_b)
    2. 如果max(need_a, need_b + cost_a) < max(need_b, need_a + cost_b)
       那么应该选择先执行任务a
    3. 通过数学推导可以得出，按照(need - cost)降序排序是最优策略
    
    时间复杂度：O(n * logn) - 主要是排序的时间复杂度
    空间复杂度：O(1) - 只使用常数额外空间
    
    :param tasks: 任务数组，每个任务包含[耗费电量, 至少电量]
    :return: 最少初始电量
    """
    # 按照(至少电量-耗费电量)的差值降序排序
    tasks.sort(key=lambda x: (x[1] - x[0]), reverse=True)
    
    ans = 0
    for job in tasks:
        # 当前任务需要的电量是耗费电量+之前任务需要的电量
        # 但不能低于该任务的至少电量要求
        ans = max(ans + job[0], job[1])
    return ans

# 测试用例
if __name__ == "__main__":
    # 额外测试用例
    testTasks = [[1, 3], [2, 4], [3, 6], [4, 8]]
    
    print("\n额外测试用例:")
    print("任务参数: [[耗费电量, 至少电量]] = [[1, 3], [2, 4], [3, 6], [4, 8]]")
    print("最少初始电量: " + str(minimumEffort(testTasks)))