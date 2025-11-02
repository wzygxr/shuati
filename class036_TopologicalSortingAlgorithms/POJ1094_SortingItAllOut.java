package class059;

import java.util.*;

/**
 * POJ 1094 - Sorting It All Out
 * 
 * 题目描述：
 * 给定n个大写字母和m个关系，每个关系形如"A<B"，表示A在B前面。
 * 要求判断在第几个关系后可以唯一确定一个拓扑序列，或者发现矛盾，或者始终无法确定。
 * 
 * 解题思路：
 * 这道题需要逐步添加关系并检查状态：
 * 1. 对于每个新添加的关系，检查是否产生矛盾（形成环）
 * 2. 如果产生矛盾，输出在第几个关系发现的矛盾
 * 3. 如果没有矛盾，检查是否能唯一确定拓扑序列
 * 4. 如果能唯一确定，输出序列
 * 5. 如果处理完所有关系仍然无法确定，输出无法确定
 * 
 * 关键点：
 * 1. 每次添加关系后都要重新进行拓扑排序
 * 2. 判断唯一性：在拓扑排序过程中，如果某一步有多个入度为0的节点，说明不唯一
 * 3. 判断矛盾：拓扑排序后，如果结果序列长度小于n，说明有环
 * 
 * 时间复杂度：O(m * (n + m))，每次都要进行一次拓扑排序
 * 空间复杂度：O(n + m)
 * 
 * 测试链接：http://poj.org/problem?id=1094
 */
public class POJ1094_SortingItAllOut {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        while (true) {
            int n = scanner.nextInt(); // 字符数量（A-Z中的前n个字母）
            int m = scanner.nextInt(); // 关系数量
            
            // 输入结束条件
            if (n == 0 && m == 0) {
                break;
            }
            
            // 存储所有关系
            String[] relations = new String[m];
            for (int i = 0; i < m; i++) {
                relations[i] = scanner.next();
            }
            
            // 逐步添加关系并检查状态
            int[][] graph = new int[26][26]; // 邻接矩阵
            int[] inDegree = new int[26];    // 入度数组
            
            boolean determined = false; // 是否已经确定顺序
            boolean inconsistent = false; // 是否存在矛盾
            
            for (int i = 0; i < m; i++) {
                // 添加新关系
                String relation = relations[i];
                int from = relation.charAt(0) - 'A';
                int to = relation.charAt(2) - 'A';
                if (graph[from][to] == 0) { // 避免重复添加
                    graph[from][to] = 1;
                    inDegree[to]++;
                }
                
                // 检查当前状态
                int[] result = new int[n];
                int type = topologicalSort(graph, inDegree, n, result);
                
                if (type == -1) { // 发现矛盾
                    System.out.println("Inconsistency found after " + (i + 1) + " relations.");
                    inconsistent = true;
                    break;
                } else if (type == 1) { // 唯一确定
                    System.out.print("Sorted sequence determined after " + (i + 1) + " relations: ");
                    for (int j = 0; j < n; j++) {
                        System.out.print((char) (result[j] + 'A'));
                    }
                    System.out.println(".");
                    determined = true;
                    break;
                }
                // type == 0 表示还无法确定，继续处理
            }
            
            // 处理完所有关系仍未确定或矛盾
            if (!determined && !inconsistent) {
                System.out.println("Sorted sequence cannot be determined.");
            }
        }
        
        scanner.close();
    }
    
    /**
     * 拓扑排序并判断状态
     * @param graph 邻接矩阵
     * @param inDegree 入度数组
     * @param n 节点数量
     * @param result 存储结果的数组
     * @return -1表示有矛盾，0表示无法确定，1表示唯一确定
     */
    public static int topologicalSort(int[][] graph, int[] inDegree, int n, int[] result) {
        int[] tempInDegree = new int[n];
        System.arraycopy(inDegree, 0, tempInDegree, 0, n);
        
        int count = 0;
        
        while (count < n) {
            // 查找入度为0的节点
            int zeroCount = 0;
            int zeroNode = -1;
            
            for (int i = 0; i < n; i++) {
                if (tempInDegree[i] == 0) {
                    zeroCount++;
                    zeroNode = i;
                }
            }
            
            // 如果没有入度为0的节点，说明有环（矛盾）
            if (zeroCount == 0) {
                return -1;
            }
            
            // 如果有多个入度为0的节点，说明无法唯一确定
            if (zeroCount > 1) {
                return 0;
            }
            
            // 只有一个入度为0的节点，可以确定当前位置
            result[count++] = zeroNode;
            tempInDegree[zeroNode] = -1; // 标记为已处理
            
            // 更新邻居节点的入度
            for (int i = 0; i < n; i++) {
                if (graph[zeroNode][i] == 1) {
                    tempInDegree[i]--;
                }
            }
        }
        
        // 成功生成完整的拓扑序列
        return 1;
    }
}