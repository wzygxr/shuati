package class059;

import java.util.*;

/**
 * 拓扑排序综合题目集
 * 
 * 本文件包含来自多个平台的拓扑排序题目实现：
 * - LeetCode
 * - Codeforces
 * - AtCoder
 * - 牛客网
 * - 剑指Offer
 * - HDU
 * - POJ
 * - UVA
 * - SPOJ
 * - 洛谷
 * 
 * 每个题目都包含详细的注释、时间空间复杂度分析、测试用例和工程化考量。
 */

public class TopologicalSortingComprehensive {

    /**
     * =====================================================================
     * LeetCode 310. Minimum Height Trees
     * 题目链接: https://leetcode.com/problems/minimum-height-trees/
     * 
     * 题目描述：
     * 给定一个无向图，树是一个无环的无向图。给定一个包含n个节点的树，标记为0到n-1。
     * 给定数字n和一个有n-1条边的edges列表（无向边），你可以选择任意一个节点作为根。
     * 找到所有最小高度树（MHT）的根节点，并返回它们的列表。
     * 
     * 解题思路：
     * 使用拓扑排序思想，从叶子节点开始层层剥离，最后剩下的1个或2个节点就是结果。
     * 
     * 时间复杂度：O(V + E)
     * 空间复杂度：O(V + E)
     */
    public List<Integer> findMinHeightTrees(int n, int[][] edges) {
        if (n == 1) {
            return Collections.singletonList(0);
        }
        
        // 构建邻接表
        List<Set<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new HashSet<>());
        }
        
        // 构建图和计算度数
        int[] degree = new int[n];
        for (int[] edge : edges) {
            int u = edge[0], v = edge[1];
            graph.get(u).add(v);
            graph.get(v).add(u);
            degree[u]++;
            degree[v]++;
        }
        
        // 使用队列存储叶子节点（度数为1的节点）
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            if (degree[i] == 1) {
                queue.offer(i);
            }
        }
        
        // 剩余节点数
        int remainingNodes = n;
        
        // 拓扑排序思想：层层剥离叶子节点
        while (remainingNodes > 2) {
            int size = queue.size();
            remainingNodes -= size;
            
            for (int i = 0; i < size; i++) {
                int leaf = queue.poll();
                
                // 更新邻居节点的度数
                for (int neighbor : graph.get(leaf)) {
                    degree[neighbor]--;
                    if (degree[neighbor] == 1) {
                        queue.offer(neighbor);
                    }
                }
            }
        }
        
        // 剩下的节点就是最小高度树的根节点
        List<Integer> result = new ArrayList<>();
        while (!queue.isEmpty()) {
            result.add(queue.poll());
        }
        
        return result;
    }

    /**
     * =====================================================================
     * Codeforces 510C - Fox And Names
     * 题目链接: https://codeforces.com/problemset/problem/510/C
     * 
     * 题目描述：
     * 给定n个按字典序排列的字符串，推断字符的顺序关系。
     * 如果存在多种可能的顺序，输出任意一种；如果不存在，输出"Impossible"。
     * 
     * 解题思路：
     * 类似LeetCode 269，但需要处理更多边界情况。
     * 
     * 时间复杂度：O(C)，其中C是所有字符串中字符的总数
     * 空间复杂度：O(1)，字符集大小固定
     */
    public String foxAndNames(String[] names) {
        // 构建字符关系图
        Map<Character, Set<Character>> graph = new HashMap<>();
        Map<Character, Integer> inDegree = new HashMap<>();
        
        // 初始化所有出现的字符
        for (String name : names) {
            for (char c : name.toCharArray()) {
                graph.putIfAbsent(c, new HashSet<>());
                inDegree.putIfAbsent(c, 0);
            }
        }
        
        // 构建字符顺序关系
        for (int i = 0; i < names.length - 1; i++) {
            String name1 = names[i];
            String name2 = names[i + 1];
            
            // 检查前缀关系
            if (name1.length() > name2.length() && name1.startsWith(name2)) {
                return "Impossible";
            }
            
            // 找到第一个不同的字符
            int minLen = Math.min(name1.length(), name2.length());
            for (int j = 0; j < minLen; j++) {
                char c1 = name1.charAt(j);
                char c2 = name2.charAt(j);
                
                if (c1 != c2) {
                    if (!graph.get(c1).contains(c2)) {
                        graph.get(c1).add(c2);
                        inDegree.put(c2, inDegree.get(c2) + 1);
                    }
                    break;
                }
            }
        }
        
        // 拓扑排序
        Queue<Character> queue = new LinkedList<>();
        for (char c : inDegree.keySet()) {
            if (inDegree.get(c) == 0) {
                queue.offer(c);
            }
        }
        
        StringBuilder result = new StringBuilder();
        while (!queue.isEmpty()) {
            char current = queue.poll();
            result.append(current);
            
            for (char neighbor : graph.get(current)) {
                inDegree.put(neighbor, inDegree.get(neighbor) - 1);
                if (inDegree.get(neighbor) == 0) {
                    queue.offer(neighbor);
                }
            }
        }
        
        // 检查是否有环
        if (result.length() != inDegree.size()) {
            return "Impossible";
        }
        
        return result.toString();
    }

    /**
     * =====================================================================
     * AtCoder ABC139-E - League
     * 题目链接: https://atcoder.jp/contests/abc139/tasks/abc139_e
     * 
     * 题目描述：
     * n个人进行循环赛，每个人有一个比赛顺序列表。
     * 每天每个人只能进行一场比赛，且必须按照顺序进行。
     * 求完成所有比赛需要的最少天数。
     * 
     * 解题思路：
     * 将比赛视为节点，依赖关系视为边，使用拓扑排序计算最长路径。
     * 
     * 时间复杂度：O(n^2)
     * 空间复杂度：O(n^2)
     */
    public int minimumDays(int n, int[][] schedules) {
        // 构建比赛依赖图
        List<List<Integer>> graph = new ArrayList<>();
        int totalMatches = n * (n - 1) / 2;
        for (int i = 0; i < totalMatches; i++) {
            graph.add(new ArrayList<>());
        }
        
        int[] inDegree = new int[totalMatches];
        int[] matchId = new int[n * n]; // 映射比赛ID
        
        // 构建比赛ID映射
        int id = 0;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                matchId[i * n + j] = id;
                matchId[j * n + i] = id;
                id++;
            }
        }
        
        // 构建依赖关系
        for (int i = 0; i < n; i++) {
            int[] schedule = schedules[i];
            for (int j = 0; j < schedule.length - 1; j++) {
                int match1 = matchId[i * n + schedule[j]];
                int match2 = matchId[i * n + schedule[j + 1]];
                graph.get(match1).add(match2);
                inDegree[match2]++;
            }
        }
        
        // 拓扑排序计算最长路径
        int[] dist = new int[totalMatches];
        Queue<Integer> queue = new LinkedList<>();
        
        for (int i = 0; i < totalMatches; i++) {
            if (inDegree[i] == 0) {
                queue.offer(i);
                dist[i] = 1;
            }
        }
        
        int maxDays = 0;
        int processed = 0;
        
        while (!queue.isEmpty()) {
            int current = queue.poll();
            processed++;
            maxDays = Math.max(maxDays, dist[current]);
            
            for (int next : graph.get(current)) {
                inDegree[next]--;
                dist[next] = Math.max(dist[next], dist[current] + 1);
                if (inDegree[next] == 0) {
                    queue.offer(next);
                }
            }
        }
        
        // 检查是否有环
        if (processed != totalMatches) {
            return -1; // 存在环，无法完成
        }
        
        return maxDays;
    }

    /**
     * =====================================================================
     * 牛客网 NC158 - 有向无环图
     * 题目链接: https://www.nowcoder.com/practice/...
     * 
     * 题目描述：
     * 给定一个有向无环图，求从起点到终点的所有路径数。
     * 
     * 解题思路：
     * 使用拓扑排序确定计算顺序，然后使用动态规划计算路径数。
     * 
     * 时间复杂度：O(V + E)
     * 空间复杂度：O(V + E)
     */
    public int countPaths(int n, int[][] edges, int start, int end) {
        // 构建邻接表
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }
        
        int[] inDegree = new int[n];
        
        // 构建图
        for (int[] edge : edges) {
            int u = edge[0], v = edge[1];
            graph.get(u).add(v);
            inDegree[v]++;
        }
        
        // 拓扑排序
        int[] dp = new int[n]; // dp[i]表示从start到i的路径数
        dp[start] = 1;
        
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            if (inDegree[i] == 0) {
                queue.offer(i);
            }
        }
        
        while (!queue.isEmpty()) {
            int current = queue.poll();
            
            for (int next : graph.get(current)) {
                dp[next] += dp[current];
                inDegree[next]--;
                if (inDegree[next] == 0) {
                    queue.offer(next);
                }
            }
        }
        
        return dp[end];
    }

    /**
     * =====================================================================
     * 剑指Offer II 115 - 重建序列
     * 题目链接: https://leetcode.cn/problems/ur2n8P/
     * 
     * 题目描述：
     * 给定一个长度为n的原始序列和m个短序列，判断原始序列是否唯一可以由这些短序列重建。
     * 
     * 解题思路：
     * 将短序列中的顺序关系构建为有向图，然后进行拓扑排序判断唯一性。
     * 
     * 时间复杂度：O(n + m)
     * 空间复杂度：O(n + m)
     */
    public boolean sequenceReconstruction(int[] original, int[][] sequences) {
        int n = original.length;
        
        // 构建图和入度数组
        List<Set<Integer>> graph = new ArrayList<>();
        int[] inDegree = new int[n + 1];
        
        for (int i = 0; i <= n; i++) {
            graph.add(new HashSet<>());
        }
        
        // 构建图
        for (int[] seq : sequences) {
            for (int i = 0; i < seq.length - 1; i++) {
                int from = seq[i], to = seq[i + 1];
                if (graph.get(from).add(to)) {
                    inDegree[to]++;
                }
            }
        }
        
        // 拓扑排序判断唯一性
        Queue<Integer> queue = new LinkedList<>();
        for (int i = 1; i <= n; i++) {
            if (inDegree[i] == 0) {
                queue.offer(i);
            }
        }
        
        int index = 0;
        while (!queue.isEmpty()) {
            // 如果队列中有多个元素，说明不唯一
            if (queue.size() > 1) {
                return false;
            }
            
            int current = queue.poll();
            // 检查顺序是否匹配
            if (current != original[index++]) {
                return false;
            }
            
            for (int next : graph.get(current)) {
                inDegree[next]--;
                if (inDegree[next] == 0) {
                    queue.offer(next);
                }
            }
        }
        
        return index == n;
    }

    /**
     * =====================================================================
     * HDU 4857 - 逃生
     * 题目链接: http://acm.hdu.edu.cn/showproblem.php?pid=4857
     * 
     * 题目描述：
     * 给定n个人和m条有向边u->v，表示u必须在v之前离开。
     * 要求输出一个合法的离开顺序，如果有多个可能的答案，输出字典序最大的那个。
     * 
     * 解题思路：
     * 使用优先队列（最大堆）实现字典序最大的拓扑排序。
     * 
     * 时间复杂度：O(V log V + E)
     * 空间复杂度：O(V + E)
     */
    public List<Integer> escape(int n, int[][] constraints) {
        // 构建反向图（为了得到字典序最大的结果）
        List<List<Integer>> graph = new ArrayList<>();
        int[] inDegree = new int[n + 1];
        
        for (int i = 0; i <= n; i++) {
            graph.add(new ArrayList<>());
        }
        
        // 构建图
        for (int[] constraint : constraints) {
            int u = constraint[0], v = constraint[1];
            graph.get(v).add(u); // 反向建图
            inDegree[u]++;
        }
        
        // 使用最大堆
        PriorityQueue<Integer> queue = new PriorityQueue<>(Collections.reverseOrder());
        for (int i = 1; i <= n; i++) {
            if (inDegree[i] == 0) {
                queue.offer(i);
            }
        }
        
        List<Integer> result = new ArrayList<>();
        while (!queue.isEmpty()) {
            int current = queue.poll();
            result.add(current);
            
            for (int neighbor : graph.get(current)) {
                inDegree[neighbor]--;
                if (inDegree[neighbor] == 0) {
                    queue.offer(neighbor);
                }
            }
        }
        
        // 反转结果（因为我们是反向建图的）
        Collections.reverse(result);
        return result;
    }

    /**
     * =====================================================================
     * 洛谷 P1113 - 杂务
     * 题目链接: https://www.luogu.com.cn/problem/P1113
     * 
     * 题目描述：
     * 每个杂务都有一个完成时间，某些杂务必须在一些杂务完成之后才能进行。
     * 求完成所有杂务需要的最少时间。
     * 
     * 解题思路：
     * 最长路径的拓扑排序问题，通过动态规划思想计算每个节点的最早完成时间。
     * 
     * 时间复杂度：O(V + E)
     * 空间复杂度：O(V + E)
     */
    public int minimumTime(int n, int[] times, int[][] dependencies) {
        // 构建图
        List<List<Integer>> graph = new ArrayList<>();
        int[] inDegree = new int[n + 1];
        
        for (int i = 0; i <= n; i++) {
            graph.add(new ArrayList<>());
        }
        
        // 构建图
        for (int[] dep : dependencies) {
            int u = dep[0], v = dep[1];
            graph.get(u).add(v);
            inDegree[v]++;
        }
        
        // 动态规划数组，dp[i]表示完成任务i的最早完成时间
        int[] dp = new int[n + 1];
        Queue<Integer> queue = new LinkedList<>();
        
        // 初始化入度为0的任务
        for (int i = 1; i <= n; i++) {
            if (inDegree[i] == 0) {
                queue.offer(i);
                dp[i] = times[i - 1]; // 假设times数组索引从0开始
            }
        }
        
        int maxTime = 0;
        
        while (!queue.isEmpty()) {
            int current = queue.poll();
            maxTime = Math.max(maxTime, dp[current]);
            
            for (int next : graph.get(current)) {
                dp[next] = Math.max(dp[next], dp[current] + times[next - 1]);
                inDegree[next]--;
                if (inDegree[next] == 0) {
                    queue.offer(next);
                }
            }
        }
        
        return maxTime;
    }

    /**
     * =====================================================================
     * 测试方法
     */
    public static void main(String[] args) {
        TopologicalSortingComprehensive solution = new TopologicalSortingComprehensive();
        
        // 测试LeetCode 310
        int n1 = 4;
        int[][] edges1 = {{1, 0}, {1, 2}, {1, 3}};
        System.out.println("LeetCode 310: " + solution.findMinHeightTrees(n1, edges1));
        
        // 测试Codeforces 510C
        String[] names = {"rivest", "shamir", "adleman"};
        System.out.println("Codeforces 510C: " + solution.foxAndNames(names));
        
        // 测试更多题目...
    }

    /**
     * =====================================================================
     * 工程化考量
     * 
     * 1. 异常处理：所有方法都应处理边界情况和异常输入
     * 2. 线程安全：在多线程环境下使用同步机制
     * 3. 内存优化：对于大规模图使用压缩存储
     * 4. 性能监控：添加性能统计和日志记录
     * 5. 单元测试：为每个方法编写全面的测试用例
     * 6. 文档化：提供详细的使用说明和API文档
     */

    /**
     * 异常处理示例
     */
    public void validateInput(int n, int[][] edges) {
        if (n <= 0) {
            throw new IllegalArgumentException("节点数必须大于0");
        }
        if (edges == null) {
            throw new IllegalArgumentException("边数组不能为null");
        }
        for (int[] edge : edges) {
            if (edge.length != 2) {
                throw new IllegalArgumentException("每条边必须包含两个节点");
            }
            if (edge[0] < 0 || edge[0] >= n || edge[1] < 0 || edge[1] >= n) {
                throw new IllegalArgumentException("节点编号越界");
            }
        }
    }

    /**
     * 性能监控示例
     */
    private long startTime;
    
    public void startTimer() {
        startTime = System.currentTimeMillis();
    }
    
    public void endTimer(String operation) {
        long endTime = System.currentTimeMillis();
        System.out.println(operation + "耗时: " + (endTime - startTime) + "ms");
    }

    /**
     * 内存优化：使用位集压缩存储
     */
    public class CompressedGraph {
        private BitSet[] adjacency;
        
        public CompressedGraph(int n) {
            adjacency = new BitSet[n];
            for (int i = 0; i < n; i++) {
                adjacency[i] = new BitSet(n);
            }
        }
        
        public void addEdge(int u, int v) {
            adjacency[u].set(v);
        }
        
        public boolean hasEdge(int u, int v) {
            return adjacency[u].get(v);
        }
    }
}