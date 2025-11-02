# 根据身高重建队列（Queue Reconstruction by Height）
# 题目来源：LeetCode 406
# 题目链接：https://leetcode.cn/problems/queue-reconstruction-by-height/
# 
# 问题描述：
# 假设有打乱顺序的一群人站成一个队列，每个人由一个整数对(h, k)表示，
# 其中h是这个人的身高，k是排在这个人前面且身高大于或等于h的人数。
# 请重建这个队列，使其满足上述要求。
# 
# 算法思路：
# 使用贪心策略，按照身高降序、k值升序排序：
# 1. 将人群按照身高降序、k值升序排序
# 2. 按照排序后的顺序，将每个人插入到结果队列的第k个位置
# 3. 这样能保证前面身高更高的人先被放置，后面插入的人不会影响前面人的k值
# 
# 时间复杂度：O(n²) - 插入操作的时间复杂度
# 空间复杂度：O(n) - 需要存储结果队列
# 
# 是否最优解：是。这是该问题的最优解法。
# 
# 适用场景：
# 1. 队列重建问题
# 2. 带约束的排序问题
# 
# 异常处理：
# 1. 处理空数组情况
# 2. 处理单元素数组
# 
# 工程化考量：
# 1. 输入验证：检查数组是否为空
# 2. 边界条件：处理单元素和双元素数组
# 3. 性能优化：使用列表提高插入效率
# 
# 相关题目：
# 1. LeetCode 135. 分发糖果 - 双向约束问题
# 2. LeetCode 56. 合并区间 - 区间合并问题
# 3. LeetCode 252. 会议室 - 区间重叠判断
# 4. 牛客网 NC140 排序 - 各种排序算法实现
# 5. LintCode 391. 数飞机 - 区间调度相关
# 6. HackerRank - Jim and the Orders - 贪心调度问题
# 7. CodeChef - TACHSTCK - 区间配对问题
# 8. AtCoder ABC104C - All Green - 动态规划相关
# 9. Codeforces 1363C - Game On Leaves - 博弈论相关
# 10. POJ 3169 - Layout - 差分约束系统

class Solution:
    """
    重建队列
    
    Args:
        people: List[List[int]] - 人群数组，每个元素是[h, k]
    
    Returns:
        List[List[int]] - 重建后的队列
    """
    def reconstructQueue(self, people):
        # 边界条件检查
        if not people:
            return []
        
        n = len(people)
        if n == 1:
            return people  # 只有一个人，直接返回
        
        # 按照身高降序、k值升序排序
        people.sort(key=lambda x: (-x[0], x[1]))
        
        # 使用列表存储结果
        result = []
        
        for person in people:
            # 将每个人插入到第k个位置
            result.insert(person[1], person)
        
        return result


def main():
    solution = Solution()
    
    # 测试用例1: 基本情况
    people1 = [[7, 0], [4, 4], [7, 1], [5, 0], [6, 1], [5, 2]]
    result1 = solution.reconstructQueue(people1)
    print("测试用例1:")
    print(f"输入人群: {people1}")
    print(f"重建队列: {result1}")
    print("期望输出: [[5,0],[7,0],[5,2],[6,1],[4,4],[7,1]]")
    print()
    
    # 测试用例2: 简单情况
    people2 = [[6, 0], [5, 0], [4, 0], [3, 2], [2, 2], [1, 4]]
    result2 = solution.reconstructQueue(people2)
    print("测试用例2:")
    print(f"输入人群: {people2}")
    print(f"重建队列: {result2}")
    print("期望输出: [[4,0],[5,0],[2,2],[3,2],[1,4],[6,0]]")
    print()
    
    # 测试用例3: 边界情况 - 单元素数组
    people3 = [[5, 0]]
    result3 = solution.reconstructQueue(people3)
    print("测试用例3:")
    print(f"输入人群: {people3}")
    print(f"重建队列: {result3}")
    print("期望输出: [[5,0]]")
    print()
    
    # 测试用例4: 边界情况 - 空数组
    people4 = []
    result4 = solution.reconstructQueue(people4)
    print("测试用例4:")
    print(f"输入人群: {people4}")
    print(f"重建队列: {result4}")
    print("期望输出: []")
    print()
    
    # 测试用例5: 复杂情况 - 相同身高
    people5 = [[5, 2], [5, 0], [5, 1], [4, 0], [4, 1]]
    result5 = solution.reconstructQueue(people5)
    print("测试用例5:")
    print(f"输入人群: {people5}")
    print(f"重建队列: {result5}")
    print("期望输出: [[4,0],[5,0],[5,1],[4,1],[5,2]]")


if __name__ == "__main__":
    main()