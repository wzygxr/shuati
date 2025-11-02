# 柠檬水找零 (Lemonade Change)
# 在柠檬水摊上，每一杯柠檬水的售价为 5 美元。顾客排队购买你的产品，（按账单 bills 支付的顺序）一次购买一杯。
# 每位顾客只买一杯柠檬水，然后向你付 5 美元、10 美元或 20 美元。
# 你必须给每个顾客正确找零，也就是说净交易是每位顾客向你支付 5 美元。
# 注意，一开始你手头没有任何零钱。
# 给你一个整数数组 bills ，其中 bills[i] 是第 i 位顾客付的账。
# 如果你能给每位顾客正确找零，返回 true，否则返回 false。
# 
# 算法标签: 贪心算法(Greedy Algorithm)、资源分配(Resource Allocation)
# 时间复杂度: O(n)，其中n是数组长度
# 空间复杂度: O(1)，仅使用常数额外空间
# 测试链接 : https://leetcode.cn/problems/lemonade-change/
# 相关题目: LeetCode 455. 分发饼干、LeetCode 135. 分发糖果
# 贪心算法专题 - 资源分配与最优选择问题集合

"""
算法思路详解：
1. 贪心策略：找零时优先使用大面额纸币
   - 这个策略的核心思想是在找零时优先使用面额较大的纸币
   - 对于20美元的找零，优先使用一张10美元和一张5美元，而不是三张5美元
   - 这样可以保留更多的5美元纸币用于后续找零

2. 维护5美元和10美元纸币的数量
   - 只需要维护这两种面额的纸币数量，因为20美元不能用于找零
   - 通过计数器来跟踪可用的零钱

3. 收到5美元：5美元数量加1
   - 最简单的情况，不需要找零

4. 收到10美元：5美元数量减1，10美元数量加1
   - 需要找零5美元，检查是否有足够的5美元纸币

5. 收到20美元：优先用一张10美元和一张5美元找零，如果没有10美元则用三张5美元找零
   - 需要找零15美元，采用贪心策略优先使用大面额纸币

时间复杂度分析：
- 遍历时间复杂度：O(n)，其中n是数组长度
- 总体时间复杂度：O(n)

空间复杂度分析：
- 只使用了常数额外空间存储变量
- 空间复杂度：O(1)

是否最优解：
- 是，这是处理此类问题的最优解法
- 贪心策略保证了局部最优解能导致全局最优解

工程化最佳实践：
1. 异常处理：检查输入是否为空或格式不正确
2. 边界条件：处理空数组、单个元素等特殊情况
3. 性能优化：一次遍历完成计算，提前终止条件避免不必要的计算
4. 可读性：清晰的变量命名和详细注释，便于维护

极端场景与边界情况处理：
1. 空输入：bills为空数组
2. 极端值：只有一种面额的纸币
3. 重复数据：多个相同面额的纸币
4. 有序/逆序数据：纸币面额按顺序排列

跨语言实现差异与优化：
1. Java：使用增强for循环遍历数组，代码更简洁
2. C++：使用传统for循环，性能更优
3. Python：使用for循环，语法更灵活

调试与测试策略：
1. 打印中间过程：在循环中打印当前纸币面额和零钱数量
2. 用断言验证中间结果：确保零钱数量不为负
3. 性能退化排查：检查是否只遍历了一次数组
4. 边界测试：测试空数组、单元素等边界情况

实际应用场景与拓展：
1. 资源管理问题：在简单的资源分配策略中应用贪心算法
2. 模拟系统：用于模拟交易过程
3. 游戏开发：用于简单的经济系统设计

算法深入解析：
贪心算法在找零问题中的应用体现了其核心思想：
1. 局部最优选择：每次找零时选择最优的纸币组合
2. 无后效性：当前的选择不会影响之前的状态
3. 最优子结构：问题的最优解包含子问题的最优解
这个问题的关键洞察是，优先使用大面额纸币能为后续找零保留更多小面额纸币。
"""


def lemonadeChange(bills):
    """
    柠檬水找零主函数 - 使用贪心算法判断是否能给每位顾客正确找零
    
    算法思路：
    1. 贪心策略：找零时优先使用大面额纸币
    2. 维护5美元和10美元纸币的数量
    3. 根据收到的纸币面额进行相应的处理
    
    Args:
        bills (List[int]): 顾客支付的纸币面额列表
        bills[i]表示第i位顾客支付的纸币面额（5、10或20）
    
    Returns:
        bool: 是否能给每位顾客正确找零
    
    时间复杂度: O(n)，其中n是数组长度
    空间复杂度: O(1)，仅使用常数额外空间
    
    Examples:
        >>> lemonadeChange([5, 5, 5, 10, 20])
        True
        >>> lemonadeChange([5, 5, 10, 10, 20])
        False
    """
    # 异常处理：检查输入是否为空
    if not bills:
        return True  # 空数组表示没有顾客，返回True
    
    five = 0   # 5美元纸币数量
    ten = 0    # 10美元纸币数量
    
    # 遍历顾客支付的纸币
    # 时间复杂度：O(n)
    for bill in bills:
        if bill == 5:
            # 收到5美元，不需要找零
            five += 1
        elif bill == 10:
            # 收到10美元，需要找零5美元
            if five > 0:
                five -= 1
                ten += 1
            else:
                # 没有5美元找零，返回False
                return False
        elif bill == 20:
            # 收到20美元，需要找零15美元
            # 贪心策略：优先使用一张10美元和一张5美元找零
            if ten > 0 and five > 0:
                ten -= 1
                five -= 1
            elif five >= 3:
                # 如果没有10美元，则用三张5美元找零
                five -= 3
            else:
                # 无法找零，返回False
                return False
    
    # 所有顾客都能正确找零
    return True


# 补充题目1: LeetCode 455. 分发饼干
# 题目描述: 假设你是一位很棒的家长，想要给你的孩子们一些小饼干。但是，每个孩子最多只能给一块饼干。
# 对每个孩子 i，都有一个胃口值 g[i]，这是能让孩子们满足胃口的饼干的最小尺寸；
# 并且每块饼干 j，都有一个尺寸 s[j]。如果 s[j] >= g[i]，我们可以将这个饼干 j 分配给孩子 i，
# 这个孩子会得到满足。你的目标是尽可能满足越多数量的孩子，并输出这个最大数值。
# 链接: https://leetcode.cn/problems/assign-cookies/

def findContentChildren(g, s):
    """
    分发饼干 - 使用贪心算法计算最多能满足的孩子数量
    
    算法思路:
    1. 贪心策略：对胃口值和饼干尺寸进行排序
    2. 尽可能用最小的能满足孩子胃口的饼干
    
    Args:
        g (List[int]): 孩子的胃口值列表，g[i]表示第i个孩子的最小满足饼干尺寸
        s (List[int]): 饼干的尺寸列表，s[j]表示第j块饼干的尺寸
    
    Returns:
        int: 最多能满足的孩子数量
    
    时间复杂度: O(n log n + m log m)，其中n是孩子数量，m是饼干数量，排序需要的时间
    空间复杂度: O(1)，只使用了常数额外空间
    是否最优解: 是，贪心策略是解决这类问题的最优方法
    
    工程化考量：
    1. 异常处理：检查输入是否为空
    2. 边界条件：处理空数组等特殊情况
    3. 性能优化：排序后使用双指针避免重复计算
    """
    # 异常处理：检查输入是否为空
    if not g or not s:
        return 0  # 空数组，无法满足任何孩子
    
    # 贪心策略：对胃口值和饼干尺寸进行排序，尽可能用最小的能满足孩子胃口的饼干
    # 时间复杂度：O(n log n + m log m)
    g.sort()  # 对孩子的胃口排序
    s.sort()  # 对饼干尺寸排序
    
    childIndex = 0  # 当前考虑的孩子索引
    cookieIndex = 0  # 当前考虑的饼干索引
    
    # 遍历饼干和孩子
    # 时间复杂度：O(n + m)
    while childIndex < len(g) and cookieIndex < len(s):
        # 如果当前饼干能满足当前孩子的胃口
        if s[cookieIndex] >= g[childIndex]:
            childIndex += 1  # 孩子得到满足，移动到下一个孩子
        # 无论如何都移动到下一个饼干
        cookieIndex += 1
    
    # 返回满足的孩子数量
    return childIndex


# 补充题目2: LeetCode 135. 分发糖果
# 题目描述: n 个孩子站成一排。给你一个整数数组 ratings 表示每个孩子的评分。
# 你需要按照以下要求，给这些孩子分发糖果：
# 1. 每个孩子至少分配到 1 个糖果
# 2. 相邻两个孩子评分更高的孩子会获得更多的糖果
# 请你给每个孩子分发糖果，计算并返回需要准备的最小糖果数目。
# 链接: https://leetcode.cn/problems/candy/

def candy(ratings):
    """
    分发糖果 - 使用贪心算法计算需要准备的最小糖果数目
    
    算法思路:
    1. 贪心策略：两次遍历
    2. 第一次从左到右，确保右边评分高的孩子得到更多糖果
    3. 第二次从右到左，确保左边评分高的孩子得到更多糖果
    
    Args:
        ratings (List[int]): 孩子的评分列表，ratings[i]表示第i个孩子的评分
    
    Returns:
        int: 需要准备的最小糖果数目
    
    时间复杂度: O(n)，其中n是孩子数量，需要两次遍历
    空间复杂度: O(n)，需要一个数组存储每个孩子的糖果数
    是否最优解: 是，这是解决此类问题的最优方法
    
    工程化考量：
    1. 异常处理：检查输入是否为空
    2. 边界条件：处理空数组、单元素等情况
    3. 性能优化：两次遍历确保满足所有约束条件
    """
    # 异常处理：检查输入是否为空
    if not ratings:
        return 0  # 空数组，不需要糖果
    
    n = len(ratings)
    candies = [1] * n  # 初始化：每个孩子至少有1个糖果
    
    # 贪心策略：从左到右遍历，确保右边评分高的孩子得到更多糖果
    # 时间复杂度：O(n)
    for i in range(1, n):
        if ratings[i] > ratings[i - 1]:
            candies[i] = candies[i - 1] + 1
    
    # 贪心策略：从右到左遍历，确保左边评分高的孩子得到更多糖果
    # 时间复杂度：O(n)
    for i in range(n - 2, -1, -1):
        if ratings[i] > ratings[i + 1]:
            candies[i] = max(candies[i], candies[i + 1] + 1)
    
    # 计算总糖果数
    # 时间复杂度：O(n)
    return sum(candies)


# 补充题目3: LeetCode 605. 种花问题
# 题目描述: 假设有一个很长的花坛，一部分地块种植了花，另一部分却没有。
# 可是，花不能种植在相邻的地块上，它们会争夺水源，两者都会死去。
# 给你一个整数数组 flowerbed 表示花坛，由若干 0 和 1 组成，其中 0 表示没种植花，1 表示种植了花。
# 另有一个数 n ，能否在不打破种植规则的情况下种入 n 朵花？能则返回 true ，不能则返回 false。
# 链接: https://leetcode.cn/problems/can-place-flowers/

def canPlaceFlowers(flowerbed, n):
    """
    种花问题 - 使用贪心算法判断是否能在不打破种植规则的情况下种入n朵花
    
    算法思路:
    1. 贪心策略：遍历每个位置，如果可以种花就种
    2. 检查条件：当前位置、前一个位置和后一个位置都没有花
    
    Args:
        flowerbed (List[int]): 表示花坛的数组，0表示没种植花，1表示种植了花
        n (int): 需要种植的花的数量
    
    Returns:
        bool: 是否能在不打破种植规则的情况下种入n朵花
    
    时间复杂度: O(n)，其中n是花坛长度
    空间复杂度: O(1)，只使用了常数额外空间
    是否最优解: 是，一次遍历即可解决问题
    
    工程化考量：
    1. 异常处理：检查输入是否为空
    2. 边界条件：处理空数组、n为0等情况
    3. 性能优化：提前终止条件避免不必要的计算
    """
    # 异常处理：检查输入是否为空
    if not flowerbed:
        return n <= 0  # 空数组，只能种0朵花
    
    if n <= 0:
        return True  # 需要种0朵花，直接返回True
    
    count = 0  # 可以种植的花的数量
    len_flowerbed = len(flowerbed)
    
    # 贪心策略：遍历每个位置，如果可以种花就种
    # 时间复杂度：O(n)
    for i in range(len_flowerbed):
        # 检查当前位置、前一个位置和后一个位置是否都没有花
        left_empty = (i == 0) or (flowerbed[i - 1] == 0)
        right_empty = (i == len_flowerbed - 1) or (flowerbed[i + 1] == 0)
        
        if flowerbed[i] == 0 and left_empty and right_empty:
            count += 1  # 可以种花
            flowerbed[i] = 1  # 标记为已种花
            
            # 提前结束，如果已经能满足需求
            if count >= n:
                return True
    
    return count >= n


# 补充题目4: LeetCode 406. 根据身高重建队列
# 题目描述: 假设有打乱顺序的一群人站成一个队列，数组 people 表示队列中一些人的属性（不一定按顺序）。
# 每个 people[i] = [hi, ki] 表示第 i 个人的身高为 hi ，前面 正好 有 ki 个身高大于或等于 hi 的人。
# 请你重新构造并返回输入数组 people 所表示的队列。返回的队列应该格式化为数组 queue ，
# 其中 queue[j] = [hj, kj] 是队列中第 j 个人的属性（queue[0] 是排在队列前面的人）。
# 链接: https://leetcode.cn/problems/queue-reconstruction-by-height/

def reconstructQueue(people):
    """
    根据身高重建队列 - 使用贪心算法重新构造队列
    
    算法思路:
    1. 贪心策略：按身高降序排序，身高相同时按ki升序排序
    2. 遍历排序后的数组，根据ki插入到相应位置
    
    Args:
        people (List[List[int]]): 包含每个人身高和前面人数的二维列表
        people[i] = [hi, ki]表示第i个人的身高为hi，前面正好有ki个身高大于或等于hi的人
    
    Returns:
        List[List[int]]: 重新构造的队列
    
    时间复杂度: O(n^2)，其中n是人数，排序需要O(n log n)，插入操作需要O(n^2)
    空间复杂度: O(n)，需要一个新数组存储结果
    是否最优解: 是，这是解决此类问题的最优方法
    
    工程化考量：
    1. 异常处理：检查输入是否为空
    2. 边界条件：处理空数组、单元素等情况
    3. 性能优化：排序后使用插入策略保证正确性
    """
    # 异常处理：检查输入是否为空或长度不足
    if not people or len(people) <= 1:
        return people  # 空数组或只有一个人，直接返回
    
    # 贪心策略：按身高降序排序，身高相同时按ki升序排序
    # 时间复杂度：O(n log n)
    people.sort(key=lambda x: (-x[0], x[1]))
    
    result = []
    
    # 遍历排序后的数组，根据ki插入到相应位置
    # 时间复杂度：O(n^2)
    for p in people:
        result.insert(p[1], p)  # 插入到索引为ki的位置
    
    return result


# 补充题目5: LeetCode 1005. K 次取反后最大化的数组和
# 题目描述: 给你一个整数数组 nums 和一个整数 k ，按以下方法修改数组：
# 1. 选择某个下标 i 并将 nums[i] 替换为 -nums[i] 。
# 你可以重复这个过程恰好 k 次。你也可以选择同一个下标 i 多次。
# 以这种方式修改数组后，返回数组 可能的最大和 。
# 链接: https://leetcode.cn/problems/maximize-sum-of-array-after-k-negations/

def largestSumAfterKNegations(nums, k):
    """
    K 次取反后最大化的数组和 - 使用贪心算法计算取反后可能的最大数组和
    
    算法思路:
    1. 贪心策略：每次将最小的负数变为正数
    2. 如果k还有剩余且为奇数，将最小的数取反
    
    Args:
        nums (List[int]): 整数数组
        k (int): 取反操作的次数
    
    Returns:
        int: 取反后可能的最大数组和
    
    时间复杂度: O(n log n)，其中n是数组长度，排序需要的时间
    空间复杂度: O(1)，只使用了常数额外空间（不考虑排序所需的空间）
    是否最优解: 是，贪心策略是解决此类问题的最优方法
    
    工程化考量：
    1. 异常处理：检查输入是否为空
    2. 边界条件：处理空数组、k为0等情况
    3. 性能优化：排序后使用贪心策略
    """
    # 异常处理：检查输入是否为空
    if not nums:
        return 0  # 空数组，和为0
    
    # 贪心策略：每次将最小的负数变为正数，这样可以最大化数组和
    # 排序数组，从小到大
    # 时间复杂度：O(n log n)
    nums.sort()
    i = 0
    
    # 尽可能将负数变为正数
    # 时间复杂度：O(n)
    while i < len(nums) and k > 0 and nums[i] < 0:
        nums[i] = -nums[i]  # 取反
        k -= 1  # 减少取反次数
        i += 1  # 移动到下一个元素
    
    # 如果k还有剩余，且k是奇数，需要将最小的正数取反
    if k > 0 and k % 2 == 1:
        # 重新排序，找到最小的数
        # 时间复杂度：O(n log n)
        nums.sort()
        nums[0] = -nums[0]  # 将最小的数取反
    
    # 计算数组和
    # 时间复杂度：O(n)
    return sum(nums)


# 测试函数
if __name__ == "__main__":
    # 测试用例1：可以正确找零
    bills1 = [5, 5, 5, 10, 20]
    print("测试用例1结果:", lemonadeChange(bills1))  # 期望输出: True
    
    # 测试用例2：无法正确找零
    bills2 = [5, 5, 10, 10, 20]
    print("测试用例2结果:", lemonadeChange(bills2))  # 期望输出: False
    
    # 测试用例3：第一个顾客就无法找零
    bills3 = [10, 10]
    print("测试用例3结果:", lemonadeChange(bills3))  # 期望输出: False
    
    # 测试用例4：复杂情况
    bills4 = [5, 5, 10, 20, 5, 5, 5, 5, 5, 5, 5, 5, 5, 10, 5, 5, 20, 5, 20, 5]
    print("测试用例4结果:", lemonadeChange(bills4))  # 期望输出: True
    
    # 测试用例5：边界情况 - 空数组
    bills5 = []
    print("测试用例5结果:", lemonadeChange(bills5))  # 期望输出: True
    
    # 测试补充题目1: 分发饼干
    print("\n测试补充题目1: 分发饼干")
    g1 = [1, 2, 3]
    s1 = [1, 1]
    print("测试用例1结果:", findContentChildren(g1, s1))  # 期望输出: 1
    
    g2 = [1, 2]
    s2 = [1, 2, 3]
    print("测试用例2结果:", findContentChildren(g2, s2))  # 期望输出: 2
    
    # 测试补充题目2: 分发糖果
    print("\n测试补充题目2: 分发糖果")
    ratings1 = [1, 0, 2]
    print("测试用例1结果:", candy(ratings1))  # 期望输出: 5
    
    ratings2 = [1, 2, 2]
    print("测试用例2结果:", candy(ratings2))  # 期望输出: 4
    
    # 测试补充题目3: 种花问题
    print("\n测试补充题目3: 种花问题")
    flowerbed1 = [1, 0, 0, 0, 1]
    print("测试用例1结果:", canPlaceFlowers(flowerbed1, 1))  # 期望输出: True
    
    flowerbed2 = [1, 0, 0, 0, 1]
    print("测试用例2结果:", canPlaceFlowers(flowerbed2, 2))  # 期望输出: False
    
    # 测试补充题目4: 根据身高重建队列
    print("\n测试补充题目4: 根据身高重建队列")
    people1 = [[7, 0], [4, 4], [7, 1], [5, 0], [6, 1], [5, 2]]
    print("测试用例1结果:", reconstructQueue(people1))  # 期望输出: [[5, 0], [7, 0], [5, 2], [6, 1], [4, 4], [7, 1]]
    
    # 测试补充题目5: K 次取反后最大化的数组和
    print("\n测试补充题目5: K 次取反后最大化的数组和")
    nums1 = [4, 2, 3]
    k1 = 1
    print("测试用例1结果:", largestSumAfterKNegations(nums1, k1))  # 期望输出: 5
    
    nums2 = [3, -1, 0, 2]
    k2 = 3
    print("测试用例2结果:", largestSumAfterKNegations(nums2, k2))  # 期望输出: 6
    
    nums3 = [2, -3, -1, 5, -4]
    k3 = 2
    print("测试用例3结果:", largestSumAfterKNegations(nums3, k3))  # 期望输出: 13