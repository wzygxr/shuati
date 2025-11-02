# 最小的必要团队 (Smallest Sufficient Team)
# 作为项目经理，你规划了一份需求的技能清单 req_skills，
# 其中 req_skills[i] 是使用某项技能的名称。
# 团队中的每位专家都掌握了一些技能，people[i] 表示第 i 位专家掌握的技能列表。
# 返回能满足所有技能需求的最小团队（最小人数），答案可以按任意顺序返回。
# 测试链接 : https://leetcode.cn/problems/smallest-sufficient-team/

class Code07_SmallestSufficientTeam:
    
    # 使用状态压缩动态规划解决最小团队问题
    # 核心思想：用二进制位表示技能掌握情况，通过状态转移找到最小团队
    # 时间复杂度: O(2^m * n)，其中m是技能数，n是人员数
    # 空间复杂度: O(2^m)
    @staticmethod
    def smallestSufficientTeam(req_skills, people):
        n = len(req_skills)
        
        # 建立技能到索引的映射
        skill_map = {skill: i for i, skill in enumerate(req_skills)}
        
        # skill[i] 表示第i个人掌握的技能集合（用二进制位表示）
        skill = [0] * len(people)
        for i, person_skills in enumerate(people):
            for s in person_skills:
                # 将掌握的技能添加到技能集合中
                skill[i] |= 1 << skill_map[s]
        
        # dp[mask] 表示掌握mask代表的技能集合所需的最小团队
        dp = {}
        # 初始状态：不掌握任何技能，需要空团队
        dp[0] = []
        
        # 状态转移：枚举所有可能的状态
        for mask in range(1 << n):
            # 如果当前状态不可达，跳过
            if mask not in dp:
                continue
            
            # 枚举每个人员
            for i in range(len(skill)):
                # 计算添加第i个人后的技能集合
                new_mask = mask | skill[i]
                # 如果添加第i个人后能获得更多技能
                if new_mask != mask:
                    # 如果新状态还未计算过，或者新状态的团队人数更少
                    current_team = dp[mask]
                    if new_mask not in dp or len(dp[new_mask]) > len(current_team) + 1:
                        # 更新状态：掌握newMask技能集合的最小团队
                        dp[new_mask] = current_team + [i]
        
        # 返回掌握所有技能的最小团队
        return dp[(1 << n) - 1]