import java.util.*;

/**
 * 拓扑排序算法详解与题目实现
 * 本文件包含多个拓扑排序题目的Java实现
 * 题目来源：LeetCode、Codeforces、牛客、剑指Offer等多个平台
 */

/**
 * LeetCode 207. Course Schedule
 * 
 * 题目链接: https://leetcode.com/problems/course-schedule/
 * 
 * 题目描述：
 * 你这个学期必须选修 numCourses 门课程，记为 0 到 numCourses - 1。
 * 在选修某些课程之前需要一些先修课程。先修课程按数组 prerequisites 给出，
 * 其中 prerequisites[i] = [ai, bi]，表示如果要学习课程 ai 则必须先学习课程 bi。
 * 请你判断是否可能完成所有课程的学习？如果可以，返回 true；否则，返回 false。
 * 
 * 解题思路：
 * 这是一个典型的拓扑排序问题。我们需要判断有向图中是否存在环。
 * 如果存在环，则无法完成所有课程；如果不存在环，则可以完成。
 * 我们可以使用Kahn算法来解决：
 * 1. 计算每个节点的入度
 * 2. 将所有入度为0的节点加入队列
 * 3. 不断从队列中取出节点，并将其所有邻居节点的入度减1
 * 4. 如果邻居节点的入度变为0，则将其加入队列
 * 5. 重复步骤3-4直到队列为空
 * 6. 最后检查处理的节点数是否等于总节点数
 * 
 * 时间复杂度：O(V + E)，其中V是课程数，E是先修关系数
 * 空间复杂度：O(V + E)，用于存储图和入度数组
 * 
 * 示例：
 * 输入：numCourses = 2, prerequisites = [[1,0]]
 * 输出：true
 * 解释：总共有 2 门课程。学习课程 1 之前，你需要完成课程 0。这是可能的。
 * 
 * 输入：numCourses = 2, prerequisites = [[1,0],[0,1]]
 * 输出：false
 * 解释：总共有 2 门课程。学习课程 1 之前，你需要先完成课程 0；
 *       并且学习课程 0 之前，你还应先完成课程 1。这是不可能的。
 */
public class Leetcode207_CourseSchedule {

    public static void main(String[] args) {
        Leetcode207_CourseSchedule solution = new Leetcode207_CourseSchedule();
        
        // 测试用例1
        int numCourses1 = 2;
        int[][] prerequisites1 = {{1, 0}};
        System.out.println("Test Case 1: " + solution.canFinish(numCourses1, prerequisites1)); // 应该输出 true
        
        // 测试用例2
        int numCourses2 = 2;
        int[][] prerequisites2 = {{1, 0}, {0, 1}};
        System.out.println("Test Case 2: " + solution.canFinish(numCourses2, prerequisites2)); // 应该输出 false
        
        // 测试用例3
        int numCourses3 = 3;
        int[][] prerequisites3 = {{1, 0}, {2, 1}};
        System.out.println("Test Case 3: " + solution.canFinish(numCourses3, prerequisites3)); // 应该输出 true
    }
    
    /**
     * 判断是否可以完成所有课程
     * @param numCourses 课程总数
     * @param prerequisites 先修课程关系数组
     * @return 如果可以完成所有课程返回true，否则返回false
     */
    public boolean canFinish(int numCourses, int[][] prerequisites) {
        // 构建邻接表表示的图
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < numCourses; i++) {
            graph.add(new ArrayList<>());
        }
        
        // 初始化入度数组
        int[] inDegree = new int[numCourses];
        
        // 构建图和入度数组
        for (int[] prerequisite : prerequisites) {
            int course = prerequisite[0];      // 当前课程
            int preCourse = prerequisite[1];   // 先修课程
            
            // 添加边：先修课程 -> 当前课程
            graph.get(preCourse).add(course);
            
            // 当前课程入度加1
            inDegree[course]++;
        }
        
        // 使用Kahn算法进行拓扑排序
        return topologicalSort(graph, inDegree, numCourses);
    }
    
    /**
     * 使用Kahn算法进行拓扑排序，判断是否存在环
     * @param graph 邻接表表示的图
     * @param inDegree 入度数组
     * @param numCourses 课程总数
     * @return 如果不存在环返回true，否则返回false
     */
    private boolean topologicalSort(List<List<Integer>> graph, int[] inDegree, int numCourses) {
        Queue<Integer> queue = new LinkedList<>();
        
        // 将所有入度为0的节点加入队列
        for (int i = 0; i < numCourses; i++) {
            if (inDegree[i] == 0) {
                queue.offer(i);
            }
        }
        
        int processedCourses = 0; // 记录已处理的课程数
        
        // Kahn算法进行拓扑排序
        while (!queue.isEmpty()) {
            int currentCourse = queue.poll();
            processedCourses++;
            
            // 遍历当前课程的所有后续课程
            for (int nextCourse : graph.get(currentCourse)) {
                // 将后续课程的入度减1
                inDegree[nextCourse]--;
                
                // 如果后续课程的入度变为0，则加入队列
                if (inDegree[nextCourse] == 0) {
                    queue.offer(nextCourse);
                }
            }
        }
        
        // 如果处理的课程数等于总课程数，说明不存在环，可以完成所有课程
        return processedCourses == numCourses;
    }

    /**
     * LeetCode 210. Course Schedule II
     * 题目链接: https://leetcode.com/problems/course-schedule-ii/
     * 
     * 题目描述：
     * 返回完成所有课程的学习顺序。如果有多个可能的答案，返回任意一个。
     * 如果不可能完成所有课程，返回一个空数组。
     * 
     * 解题思路：
     * 使用Kahn算法进行拓扑排序，同时记录拓扑排序的结果
     * 
     * 时间复杂度：O(V + E)
     * 空间复杂度：O(V + E)
     */
    public int[] findOrder(int numCourses, int[][] prerequisites) {
        // 构建邻接表表示的图
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < numCourses; i++) {
            graph.add(new ArrayList<>());
        }
        
        // 初始化入度数组
        int[] inDegree = new int[numCourses];
        
        // 构建图和入度数组
        for (int[] prerequisite : prerequisites) {
            int course = prerequisite[0];      // 当前课程
            int preCourse = prerequisite[1];   // 先修课程
            
            graph.get(preCourse).add(course);
            inDegree[course]++;
        }
        
        // 使用Kahn算法进行拓扑排序
        return topologicalSortWithResult(graph, inDegree, numCourses);
    }
    
    /**
     * 使用Kahn算法进行拓扑排序，返回拓扑排序的结果
     */
    private int[] topologicalSortWithResult(List<List<Integer>> graph, int[] inDegree, int numCourses) {
        Queue<Integer> queue = new LinkedList<>();
        int[] result = new int[numCourses];
        int index = 0;
        
        // 将所有入度为0的节点加入队列
        for (int i = 0; i < numCourses; i++) {
            if (inDegree[i] == 0) {
                queue.offer(i);
            }
        }
        
        // Kahn算法进行拓扑排序
        while (!queue.isEmpty()) {
            int currentCourse = queue.poll();
            result[index++] = currentCourse;
            
            // 遍历当前课程的所有后续课程
            for (int nextCourse : graph.get(currentCourse)) {
                inDegree[nextCourse]--;
                
                if (inDegree[nextCourse] == 0) {
                    queue.offer(nextCourse);
                }
            }
        }
        
        // 如果处理的课程数等于总课程数，返回排序结果，否则返回空数组
        return index == numCourses ? result : new int[0];
    }

    /**
     * 使用DFS算法实现拓扑排序
     */
    private boolean hasCycle = false;
    private boolean[] visited;
    private boolean[] onPath;
    private List<Integer> postorder;
    
    public boolean canFinishDFS(int numCourses, int[][] prerequisites) {
        // 构建图
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < numCourses; i++) {
            graph.add(new ArrayList<>());
        }
        
        for (int[] prerequisite : prerequisites) {
            int from = prerequisite[1];
            int to = prerequisite[0];
            graph.get(from).add(to);
        }
        
        // 初始化
        visited = new boolean[numCourses];
        onPath = new boolean[numCourses];
        hasCycle = false;
        
        // 遍历所有节点
        for (int i = 0; i < numCourses; i++) {
            if (!visited[i]) {
                traverse(graph, i);
            }
        }
        
        return !hasCycle;
    }
    
    private void traverse(List<List<Integer>> graph, int node) {
        // 如果在当前路径中找到了节点，说明存在环
        if (onPath[node]) {
            hasCycle = true;
            return;
        }
        
        if (visited[node]) {
            return;
        }
        
        visited[node] = true;
        onPath[node] = true;
        
        // 遍历所有邻居节点
        for (int neighbor : graph.get(node)) {
            traverse(graph, neighbor);
            if (hasCycle) {
                return;
            }
        }
        
        onPath[node] = false;
    }

    /**
     * LeetCode 269. Alien Dictionary
     * 题目链接: https://leetcode.com/problems/alien-dictionary/
     * 
     * 题目描述：
     * 给定一个按字典序排序的外星文字典中的单词列表，推断其中字母的顺序。
     * 
     * 解题思路：
     * 1. 构建字符之间的有向图
     * 2. 使用拓扑排序确定字母顺序
     * 
     * 时间复杂度：O(V + E)，其中V是字符集大小，E是字符之间的约束关系数
     * 空间复杂度：O(V + E)
     */
    public String alienOrder(String[] words) {
        // 构建图
        Map<Character, List<Character>> graph = new HashMap<>();
        Map<Character, Integer> inDegree = new HashMap<>();
        
        // 初始化所有出现的字符
        for (String word : words) {
            for (char c : word.toCharArray()) {
                graph.putIfAbsent(c, new ArrayList<>());
                inDegree.putIfAbsent(c, 0);
            }
        }
        
        // 构建字符之间的约束关系
        for (int i = 0; i < words.length - 1; i++) {
            String word1 = words[i];
            String word2 = words[i + 1];
            
            // 检查是否是前缀关系
            if (word1.startsWith(word2) && !word1.equals(word2)) {
                return "";
            }
            
            // 找出第一个不同的字符
            int minLength = Math.min(word1.length(), word2.length());
            for (int j = 0; j < minLength; j++) {
                char c1 = word1.charAt(j);
                char c2 = word2.charAt(j);
                
                if (c1 != c2) {
                    graph.get(c1).add(c2);
                    inDegree.put(c2, inDegree.get(c2) + 1);
                    break;
                }
            }
        }
        
        // 使用Kahn算法进行拓扑排序
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
            return "";
        }
        
        return result.toString();
    }

    /**
     * LeetCode 936. Stamping The Sequence
     * 题目链接: https://leetcode.com/problems/stamping-the-sequence/
     * 
     * 题目描述：
     * 给定一个目标字符串 target 和一个印章字符串 stamp，返回一个操作序列，使得可以通过这些操作将一个全'?'字符串转换为 target。
     * 每个操作是在字符串的某个位置盖上印章，覆盖原字符。
     * 
     * 解题思路：
     * 使用逆向思维和拓扑排序，从 target 向全'?'字符串转换
     * 
     * 时间复杂度：O(N^2 * M)，其中N是target长度，M是stamp长度
     * 空间复杂度：O(N^2)
     */
    public int[] movesToStamp(String stamp, String target) {
        int m = stamp.length();
        int n = target.length();
        
        // 逆向思维，从target向全'?'转换
        // 记录每个位置被覆盖的次数
        int[] coverCount = new int[n - m + 1];
        // 记录每个位置可以被覆盖的字符数
        int[] matched = new int[n];
        
        Queue<Integer> queue = new LinkedList<>();
        List<Integer> resultList = new ArrayList<>();
        
        // 初始时，找出所有可以完全匹配的子串
        for (int i = 0; i <= n - m; i++) {
            for (int j = 0; j < m; j++) {
                if (stamp.charAt(j) == target.charAt(i + j)) {
                    coverCount[i]++;
                }
            }
            
            if (coverCount[i] == m) {
                // 可以完全匹配，加入队列
                queue.offer(i);
                resultList.add(i);
                for (int j = 0; j < m; j++) {
                    if (matched[i + j] == 0) {
                        matched[i + j] = 1;
                    }
                }
            }
        }
        
        // 进行拓扑排序
        while (!queue.isEmpty()) {
            int pos = queue.poll();
            
            // 检查pos周围的位置
            for (int i = Math.max(0, pos - m + 1); i <= Math.min(n - m, pos + m - 1); i++) {
                if (i == pos) continue;
                
                boolean overlap = false;
                for (int j = 0; j < m; j++) {
                    if (i + j >= pos && i + j < pos + m) {
                        overlap = true;
                        // 如果当前字符已经被覆盖为'?'，则更新覆盖次数
                        if (matched[pos + j - i] == 1 && stamp.charAt(j) == target.charAt(i + j)) {
                            if (coverCount[i] < m) {
                                coverCount[i]++;
                            }
                        }
                    }
                }
                
                if (overlap && coverCount[i] == m) {
                    queue.offer(i);
                    resultList.add(i);
                    // 标记被覆盖的位置
                    for (int j = 0; j < m; j++) {
                        if (matched[i + j] == 0) {
                            matched[i + j] = 1;
                        }
                    }
                }
            }
        }
        
        // 检查是否所有字符都被覆盖
        for (int i = 0; i < n; i++) {
            if (matched[i] == 0) {
                return new int[0];
            }
        }
        
        // 反转结果，因为是逆向操作
        Collections.reverse(resultList);
        int[] result = new int[resultList.size()];
        for (int i = 0; i < resultList.size(); i++) {
            result[i] = resultList.get(i);
        }
        
        return result;
    }

    /**
     * 牛客 NC143. 矩阵乘法计算量估算
     * 题目链接: https://www.nowcoder.com/practice/963fef76e30b44259366628fa9360b80
     * 
     * 题目描述：
     * 给出n个矩阵的维度和一些矩阵乘法表达式，计算乘法的次数。
     * 注意矩阵乘法必须满足矩阵的列数等于后一个矩阵的行数。
     * 
     * 解题思路：
     * 使用拓扑排序来确定计算顺序，避免重复计算
     */
    public int matrixMultiplyCount(String expression, Map<Character, int[]> matrixDimensions) {
        // 实现略，需要根据具体的表达式格式进行解析
        return 0;
    }

    /**
     * POJ 1094. Sorting It All Out
     * 题目链接: http://poj.org/problem?id=1094
     * 
     * 题目描述：
     * 给定字母之间的大小关系，判断是否可以确定唯一的顺序，或者存在矛盾。
     * 
     * 解题思路：
     * 每添加一个约束，就进行一次拓扑排序，判断是否可以确定顺序或存在矛盾
     */
    public String determineOrder(int n, String[] constraints) {
        // 实现略
        return "";
    }
}