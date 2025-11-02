package class060;

// 参加会议的最多员工数 - 基环树问题
// 一个公司准备组织一场会议，邀请一些员工参加
// 公司为每个员工准备了一个最喜欢的员工列表
// 每个员工 i 当且仅当他喜欢的员工也参加会议时，他才会参加
// 你最多能邀请多少员工参会？
// 测试链接 : https://leetcode.cn/problems/maximum-employees-to-be-invited-to-a-meeting/
// 请同学们务必参考如下代码中关于输入、输出的处理
// 这是输入输出处理效率很高的写法
// 提交以下所有代码，把主类名改成Main，可以直接通过

import java.util.*;

/**
 * 题目解析：
 * 这是基环树（Functional Graph）的经典题目，每个节点只有一条出边。
 * 需要处理环和链的情况，分类讨论最大参会人数。
 * 
 * 算法思路：
 * 1. 使用拓扑排序找出所有不在环上的节点
 * 2. 对于每个环，计算环的大小
 * 3. 对于大小为2的环，可以加上两条链的长度
 * 4. 对于大小大于2的环，只能选择环的大小
 * 5. 取所有情况的最大值
 * 
 * 时间复杂度：O(n)
 * 空间复杂度：O(n)
 * 
 * 相关题目扩展：
 * 1. LeetCode 2127. 参加会议的最多员工数 - https://leetcode.cn/problems/maximum-employees-to-be-invited-to-a-meeting/
 * 2. 洛谷 P1453 城市环路 - https://www.luogu.com.cn/problem/P1453
 * 3. LeetCode 1559. 二维网格图中探测环 - https://leetcode.cn/problems/detect-cycles-in-2d-grid/
 * 4. 洛谷 P2607 [ZJOI2008]骑士 - https://www.luogu.com.cn/problem/P2607
 * 
 * 工程化考虑：
 * 1. 图结构分析：识别基环树特性
 * 2. 分类讨论：处理不同大小的环
 * 3. 链长度计算：使用拓扑排序和DFS
 * 4. 边界处理：单节点、自环等情况
 * 5. 性能优化：线性时间复杂度的算法
 */
public class Code16_MaximumEmployeesToMeeting {

    public static int maximumInvitations(int[] favorite) {
        int n = favorite.length;
        
        // 计算入度
        int[] indegree = new int[n];
        for (int i = 0; i < n; i++) {
            indegree[favorite[i]]++;
        }
        
        // 拓扑排序：找出所有不在环上的节点
        boolean[] visited = new boolean[n];
        int[] depth = new int[n]; // 链的深度
        Queue<Integer> queue = new LinkedList<>();
        
        for (int i = 0; i < n; i++) {
            if (indegree[i] == 0) {
                queue.offer(i);
                visited[i] = true;
            }
        }
        
        // 计算链的深度
        while (!queue.isEmpty()) {
            int curr = queue.poll();
            int next = favorite[curr];
            depth[next] = Math.max(depth[next], depth[curr] + 1);
            
            if (--indegree[next] == 0) {
                queue.offer(next);
                visited[next] = true;
            }
        }
        
        int maxCircle = 0; // 最大环的大小
        int sumPairs = 0;   // 所有大小为2的环加上链的长度之和
        
        // 处理环
        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                // 找到环
                int circleSize = 0;
                int curr = i;
                
                while (!visited[curr]) {
                    visited[curr] = true;
                    circleSize++;
                    curr = favorite[curr];
                }
                
                if (circleSize == 2) {
                    // 大小为2的环：可以加上两条链的长度
                    sumPairs += 2 + depth[i] + depth[favorite[i]];
                } else {
                    // 大小大于2的环：只能选择环的大小
                    maxCircle = Math.max(maxCircle, circleSize);
                }
            }
        }
        
        return Math.max(maxCircle, sumPairs);
    }

    public static void main(String[] args) {
        // 测试用例1：大小为2的环加上链
        int[] favorite1 = {2, 2, 1, 2};
        System.out.println("测试用例1: " + maximumInvitations(favorite1)); // 输出: 3
        
        // 测试用例2：大小为3的环
        int[] favorite2 = {1, 2, 0};
        System.out.println("测试用例2: " + maximumInvitations(favorite2)); // 输出: 3
        
        // 测试用例3：多个环
        int[] favorite3 = {3, 0, 1, 4, 1};
        System.out.println("测试用例3: " + maximumInvitations(favorite3)); // 输出: 4
        
        // 测试用例4：自环
        int[] favorite4 = {0};
        System.out.println("测试用例4: " + maximumInvitations(favorite4)); // 输出: 1
    }
}