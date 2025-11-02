package class060;

// Sorting It All Out - 拓扑排序状态判断
// 给定一系列关系，逐步判断拓扑排序的状态
// 可能的状态：唯一确定、存在矛盾、无法确定
// 测试链接 : http://poj.org/problem?id=1094
// 请同学们务必参考如下代码中关于输入、输出的处理
// 这是输入输出处理效率很高的写法
// 提交以下所有代码，把主类名改成Main，可以直接通过

import java.util.*;

/**
 * 题目解析：
 * 这是拓扑排序状态判断的经典题目，需要逐步添加关系并判断当前状态。
 * 三种可能状态：
 * 1. 唯一确定：存在唯一的拓扑序列
 * 2. 存在矛盾：图中存在环
 * 3. 无法确定：存在多个可能的拓扑序列
 * 
 * 算法思路：
 * 1. 逐步添加边关系
 * 2. 每次添加后尝试进行拓扑排序
 * 3. 根据拓扑排序结果判断当前状态
 * 4. 如果队列中同时存在多个入度为0的节点，说明无法确定
 * 
 * 时间复杂度：O(n * m)，其中n是节点数，m是边数
 * 空间复杂度：O(n + m)
 * 
 * 相关题目扩展：
 * 1. POJ 1094 Sorting It All Out - http://poj.org/problem?id=1094
 * 2. 洛谷 P1347 排序 - https://www.luogu.com.cn/problem/P1347
 * 3. Timus 1280. Topological Sorting - https://acm.timus.ru/problem.aspx?space=1&num=1280
 * 
 * 工程化考虑：
 * 1. 输入验证：验证关系字符串的有效性
 * 2. 边界处理：空关系、单节点等情况
 * 3. 性能优化：增量式拓扑排序，避免重复计算
 * 4. 异常处理：处理非法输入格式
 * 5. 状态判断：精确判断三种可能状态
 */
public class Code14_SortingItAllOut {

    public static int n, m;
    public static List<String> relations;

    /**
     * 拓扑排序状态判断
     * @return 0-无法确定, 1-唯一确定, 2-存在矛盾
     */
    public static int topologicalSortState(int[][] graph, int[] indegree) {
        int[] tempIndegree = Arrays.copyOf(indegree, n);
        Queue<Integer> queue = new LinkedList<>();
        
        // 统计入度为0的节点
        for (int i = 0; i < n; i++) {
            if (tempIndegree[i] == 0) {
                queue.offer(i);
            }
        }
        
        boolean determined = true;
        List<Integer> result = new ArrayList<>();
        
        while (!queue.isEmpty()) {
            // 如果队列中有多个节点，说明无法确定
            if (queue.size() > 1) {
                determined = false;
            }
            
            int u = queue.poll();
            result.add(u);
            
            for (int v = 0; v < n; v++) {
                if (graph[u][v] == 1) {
                    if (--tempIndegree[v] == 0) {
                        queue.offer(v);
                    }
                }
            }
        }
        
        // 检查是否有环
        if (result.size() < n) {
            return 2; // 存在矛盾（有环）
        }
        
        return determined ? 1 : 0; // 1-唯一确定, 0-无法确定
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            n = scanner.nextInt();
            m = scanner.nextInt();
            
            if (n == 0 && m == 0) break;
            
            relations = new ArrayList<>();
            for (int i = 0; i < m; i++) {
                relations.add(scanner.next());
            }
            
            int[][] graph = new int[n][n];
            int[] indegree = new int[n];
            boolean foundResult = false;
            int resultStep = -1;
            int resultState = -1;
            List<Integer> resultSequence = null;
            
            // 逐步添加关系
            for (int step = 0; step < m; step++) {
                String relation = relations.get(step);
                int u = relation.charAt(0) - 'A';
                int v = relation.charAt(2) - 'A';
                
                // 添加边
                graph[u][v] = 1;
                indegree[v]++;
                
                // 检查状态
                int state = topologicalSortState(graph, indegree);
                
                if (state == 1 || state == 2) {
                    foundResult = true;
                    resultStep = step + 1;
                    resultState = state;
                    
                    // 如果是唯一确定，记录拓扑序列
                    if (state == 1) {
                        resultSequence = getTopologicalSequence(graph, indegree);
                    }
                    break;
                }
            }
            
            if (foundResult) {
                if (resultState == 1) {
                    System.out.print("Sorted sequence determined after " + resultStep + " relations: ");
                    for (int node : resultSequence) {
                        System.out.print((char)('A' + node));
                    }
                    System.out.println(".");
                } else {
                    System.out.println("Inconsistency found after " + resultStep + " relations.");
                }
            } else {
                System.out.println("Sorted sequence cannot be determined.");
            }
        }
        
        scanner.close();
    }
    
    /**
     * 获取拓扑序列（唯一确定时）
     */
    public static List<Integer> getTopologicalSequence(int[][] graph, int[] indegree) {
        int[] tempIndegree = Arrays.copyOf(indegree, n);
        Queue<Integer> queue = new LinkedList<>();
        List<Integer> result = new ArrayList<>();
        
        for (int i = 0; i < n; i++) {
            if (tempIndegree[i] == 0) {
                queue.offer(i);
            }
        }
        
        while (!queue.isEmpty()) {
            int u = queue.poll();
            result.add(u);
            
            for (int v = 0; v < n; v++) {
                if (graph[u][v] == 1) {
                    if (--tempIndegree[v] == 0) {
                        queue.offer(v);
                    }
                }
            }
        }
        
        return result;
    }
}