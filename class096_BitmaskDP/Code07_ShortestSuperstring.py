# 最短超级串 (Shortest Superstring)
# 题目来源: LeetCode 943. Find the Shortest Superstring
# 题目链接: https://leetcode.cn/problems/find-the-shortest-superstring/
# 题目描述:
# 给定一个字符串数组 words ，找到以 words 中每个字符串作为子字符串的最短字符串。
# 如果有多个有效最短字符串满足题目条件，返回其中 任意一个 即可。
# 我们可以假设 words 中没有字符串是 words 中另一个字符串的子字符串。
#
# 解题思路:
# 这是一个经典的旅行商问题(TSP)变种，可以使用状态压缩DP解决。
# 1. 预处理计算重叠部分 overlap[i][j] 表示字符串words[i]的尾部与字符串words[j]的头部的最大重叠长度
# 2. dp[mask][i] 表示使用mask代表的字符串集合，且最后一个字符串是words[i]时的最短超级字符串
# 3. 状态转移: 对于每个状态，尝试添加一个新的字符串
#
# 时间复杂度: O(n^2 * 2^n + n * sum(len))
# 空间复杂度: O(n * 2^n)
# 其中n是字符串的数量，sum(len)是所有字符串长度之和
#
# 补充题目1: 最小必要团队 (Smallest Sufficient Team)
# 题目来源: LeetCode 1125. Smallest Sufficient Team
# 题目链接: https://leetcode.cn/problems/smallest-sufficient-team/
# 题目描述:
# 给定一个人数组和一个技能需求列表，找出最小的团队使得团队成员掌握的技能能够覆盖所有需求技能。
# 解题思路:
# 1. 状态压缩动态规划解法
# 2. 建立技能到索引的映射，便于位运算
# 3. 将每个人掌握的技能转换为位掩码表示
# 4. dp[mask] 表示覆盖技能集合mask所需的最小团队，使用List存储团队成员索引
# 时间复杂度: O(2^m * n) 其中m是需求技能数，n是人员数
# 空间复杂度: O(2^m)

# 常量定义
MAXN = 15        # 最大字符串数量
MAX_STATES = 1 << 12  # 最大状态数，2^12 = 4096
INF = float('inf')    # 无穷大常量

# LeetCode 943 最短超级串解法
def shortest_superstring(words):
    """
    计算最短超级串
    这是一个经典的旅行商问题(TSP)变种，可以使用状态压缩DP解决
    
    Args:
        words (List[str]): 字符串列表
    
    Returns:
        str: 最短超级串
    """
    n = len(words)
    
    # 预处理计算重叠部分
    # overlap[i][j] 表示字符串words[i]的尾部与字符串words[j]的头部的最大重叠长度
    overlap = [[0] * n for _ in range(n)]
    for i in range(n):
        for j in range(n):
            if i != j:
                overlap[i][j] = get_overlap(words[i], words[j])
    
    # dp[mask][i] 表示使用mask代表的字符串集合，且最后一个字符串是words[i]时的最短超级字符串长度
    # parent[mask][i] 用于回溯路径，记录前一个字符串的索引
    dp = [[INF] * n for _ in range(1 << n)]
    parent = [[-1] * n for _ in range(1 << n)]
    
    # 初始化：只包含一个字符串的情况
    # 1 << i 表示只包含第i个字符串的状态
    for i in range(n):
        dp[1 << i][i] = len(words[i])
    
    # 状态转移过程
    for mask in range(1 << n):
        # 枚举当前状态下的最后一个字符串
        for last in range(n):
            # 如果第last个字符串不在当前状态中，跳过
            if (mask & (1 << last)) == 0:
                continue
            
            # 如果当前状态不可达，跳过
            if dp[mask][last] == INF:
                continue
            
            # 尝试添加一个新的字符串
            for next_idx in range(n):
                # 如果第next_idx个字符串已经在当前状态中，跳过
                if (mask & (1 << next_idx)) != 0:
                    continue
                
                # 计算添加next_idx字符串后的新状态
                new_mask = mask | (1 << next_idx)
                # 计算新状态下的超级字符串长度
                new_length = dp[mask][last] + len(words[next_idx]) - overlap[last][next_idx]
                
                # 如果通过当前路径能得到更短的超级字符串，更新状态
                if dp[new_mask][next_idx] > new_length:
                    dp[new_mask][next_idx] = new_length
                    parent[new_mask][next_idx] = last
    
    # 找到包含所有字符串的最短超级字符串
    result_length = INF
    last_word = -1
    # 枚举所有字符串作为最后一个字符串的情况
    for i in range(n):
        # (1 << n) - 1 表示包含所有字符串的状态
        if dp[(1 << n) - 1][i] < result_length:
            result_length = dp[(1 << n) - 1][i]
            last_word = i
    
    # 回溯构建结果字符串
    # path列表存储构建超级字符串时的字符串顺序
    mask = (1 << n) - 1  # 包含所有字符串的状态
    path = []
    # 从后往前回溯，构建字符串顺序
    while mask > 0:
        path.append(last_word)
        prev = parent[mask][last_word]
        mask ^= (1 << last_word)  # 从状态中移除last_word字符串
        last_word = prev
    
    path.reverse()  # 反转路径，得到正确的顺序
    
    # 构建最终字符串
    result = words[path[0]]  # 复制第一个字符串
    # 依次连接后续字符串，注意重叠部分只需要复制一次
    for i in range(1, n):
        overlap_len = overlap[path[i - 1]][path[i]]
        result += words[path[i]][overlap_len:]  # 从重叠部分之后开始复制
    
    return result

# 计算字符串a的尾部与字符串b的头部的最大重叠长度
def get_overlap(a, b):
    """
    计算字符串a的尾部与字符串b的头部的最大重叠长度
    
    Args:
        a (str): 第一个字符串
        b (str): 第二个字符串
    
    Returns:
        int: 最大重叠长度
    """
    # 重叠长度最大为两个字符串长度的较小值
    max_overlap = min(len(a), len(b))
    
    # 从最大可能的重叠长度开始向下枚举
    for i in range(max_overlap, -1, -1):
        # 检查a的后i个字符是否与b的前i个字符相同
        if a[len(a) - i:] == b[:i]:
            return i
    return 0

# LeetCode 1125 最小必要团队解法
def smallest_sufficient_team(req_skills, people):
    """
    计算最小必要团队
    使用状态压缩动态规划解法
    
    Args:
        req_skills (List[str]): 技能需求列表
        people (List[List[str]]): 人员技能列表，每个元素是一个技能列表
    
    Returns:
        List[int]: 最小团队成员索引列表
    """
    m = len(req_skills)
    n = len(people)
    
    # 建立技能到索引的映射，便于位运算
    # skill_index字典将技能名称映射到索引
    skill_index = {skill: i for i, skill in enumerate(req_skills)}
    
    # 将每个人掌握的技能转换为位掩码表示
    # people_skills[i] 表示第i个人掌握的技能集合，用二进制位表示
    people_skills = [0] * n
    for i in range(n):
        for skill in people[i]:
            # 如果该技能在需求列表中
            if skill in skill_index:
                # 将第skill_index[skill]位设为1，表示掌握该技能
                people_skills[i] |= 1 << skill_index[skill]
    
    # dp[mask] 表示覆盖技能集合mask所需的最小团队大小
    # parent[mask] 用于回溯路径，记录选择的人员索引
    # prev_state[mask] 记录选择人员之前的技能状态
    dp = [INF] * (1 << m)
    dp[0] = 0  # 初始状态：不需要任何技能时，团队大小为0
    
    parent = [0] * (1 << m)
    prev_state = [0] * (1 << m)
    
    # 遍历所有可能的技能组合状态
    for mask in range(1 << m):
        # 如果当前状态不可达，跳过
        if dp[mask] == INF:
            continue
        
        # 尝试添加每个人员
        for i in range(n):
            # 添加人员i后的新技能集合
            # mask | people_skills[i] 表示将人员i的技能加入当前技能集合
            new_mask = mask | people_skills[i]
            
            # 如果通过当前路径能得到更小的团队
            if dp[new_mask] > dp[mask] + 1:
                dp[new_mask] = dp[mask] + 1
                parent[new_mask] = i      # 记录选择的人员索引
                prev_state[new_mask] = mask # 记录选择人员之前的技能状态
    
    # 回溯构建结果团队
    team = []
    # 从包含所有需求技能的状态开始回溯
    mask = (1 << m) - 1
    while mask > 0:
        person = parent[mask]        # 获取选择的人员索引
        team.append(person)          # 将人员加入团队
        mask = prev_state[mask]      # 回到选择人员之前的技能状态
    
    return team

# 测试方法
if __name__ == "__main__":
    # 测试 LeetCode 943 最短超级串
    words1 = ["alex", "loves", "leetcode"]
    print("LeetCode 943 最短超级串 测试:")
    print("输入:", words1)
    print("结果:", shortest_superstring(words1))
    
    words2 = ["catg", "ctaagt", "gcta", "ttca", "atgcatc"]
    print("\n输入:", words2)
    print("结果:", shortest_superstring(words2))
    
    # 测试 LeetCode 1125 最小必要团队
    req_skills1 = ["java", "nodejs", "reactjs"]
    people1 = [["java"], ["nodejs"], ["nodejs", "reactjs"]]
    print("\nLeetCode 1125 最小必要团队 测试:")
    print("技能需求:", req_skills1)
    print("人员技能:", people1)
    result1 = smallest_sufficient_team(req_skills1, people1)
    print("结果团队:", result1)