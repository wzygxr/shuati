package class080;

import java.util.*;

// 最小的必要团队 (Smallest Sufficient Team)
// 作为项目经理，你规划了一份需求的技能清单 req_skills，
// 其中 req_skills[i] 是使用某项技能的名称。
// 团队中的每位专家都掌握了一些技能，people[i] 表示第 i 位专家掌握的技能列表。
// 返回能满足所有技能需求的最小团队（最小人数），答案可以按任意顺序返回。
// 测试链接 : https://leetcode.cn/problems/smallest-sufficient-team/
public class Code07_SmallestSufficientTeam {

    // 使用状态压缩动态规划解决最小团队问题
    // 核心思想：用二进制位表示技能掌握情况，通过状态转移找到最小团队
    // 时间复杂度: O(2^m * n)，其中m是技能数，n是人员数
    // 空间复杂度: O(2^m)
    public static int[] smallestSufficientTeam(String[] req_skills, List<List<String>> people) {
        int n = req_skills.length;
        
        // 建立技能到索引的映射
        Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i < n; i++) {
            map.put(req_skills[i], i);
        }
        
        // skill[i] 表示第i个人掌握的技能集合（用二进制位表示）
        int[] skill = new int[people.size()];
        for (int i = 0; i < people.size(); i++) {
            for (String s : people.get(i)) {
                // 将掌握的技能添加到技能集合中
                skill[i] |= 1 << map.get(s);
            }
        }
        
        // dp[mask] 表示掌握mask代表的技能集合所需的最小团队
        List<Integer>[] dp = new List[1 << n];
        // 初始化：将所有状态设为null
        for (int i = 0; i < (1 << n); i++) {
            dp[i] = null;
        }
        // 初始状态：不掌握任何技能，需要空团队
        dp[0] = new ArrayList<>();
        
        // 状态转移：枚举所有可能的状态
        for (int mask = 0; mask < (1 << n); mask++) {
            // 如果当前状态不可达，跳过
            if (dp[mask] == null) {
                continue;
            }
            
            // 枚举每个人员
            for (int i = 0; i < skill.length; i++) {
                // 计算添加第i个人后的技能集合
                int newMask = mask | skill[i];
                // 如果添加第i个人后能获得更多技能
                if (newMask != mask) {
                    // 如果新状态还未计算过，或者新状态的团队人数更少
                    if (dp[newMask] == null || dp[newMask].size() > dp[mask].size() + 1) {
                        // 更新状态：掌握newMask技能集合的最小团队
                        dp[newMask] = new ArrayList<>(dp[mask]);
                        dp[newMask].add(i);
                    }
                }
            }
        }
        
        // 将结果转换为数组并返回
        List<Integer> result = dp[(1 << n) - 1];
        return result.stream().mapToInt(Integer::intValue).toArray();
    }

}