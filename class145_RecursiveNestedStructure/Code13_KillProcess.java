package class039;

import java.util.*;

/**
 * LeetCode 582. Kill Process (杀死进程)
 * 来源: LeetCode
 * 网址: https://leetcode.cn/problems/kill-process/
 * 
 * 题目描述:
 * 给定n个进程，每个进程都有一个唯一的PID（进程ID）和它的PPID（父进程ID）。
 * 每个进程只有一个父进程，但是可能有多个子进程。这形成了一个树状结构。
 * 只有一个进程的PPID是0，这意味着这个进程没有父进程。所有的进程都应该是这个进程的后代。
 * 当一个进程被杀死时，它的所有子进程和后代进程也应该被杀死。
 * 给定一个PID和一个PPID列表，以及一个要杀死的进程kill，请返回所有应该被杀死的进程的ID列表。
 * 你可以以任意顺序返回答案。
 * 
 * 示例:
 * 输入：
 * pid = [1, 3, 10, 5]
 * ppid = [3, 0, 5, 3]
 * kill = 5
 * 输出：[5, 10]
 * 解释：
 *           3
 *         /   \
 *        1     5
 *             /
 *            10
 * 杀死进程5，其子进程10也应该被杀死。
 * 
 * 解题思路:
 * 1. 首先构建进程之间的父子关系映射
 * 2. 使用深度优先搜索（递归）从kill进程开始，收集所有应该被杀死的进程
 * 
 * 时间复杂度: O(n)，其中n是进程的数量，构建映射需要O(n)，DFS遍历需要O(n)
 * 空间复杂度: O(n)，用于存储映射和递归调用栈
 */
public class Code13_KillProcess {
    
    // 使用深度优先搜索解决问题
    public List<Integer> killProcess(List<Integer> pid, List<Integer> ppid, int kill) {
        // 结果列表，存储所有应该被杀死的进程ID
        List<Integer> result = new ArrayList<>();
        
        // 构建父进程到子进程的映射
        Map<Integer, List<Integer>> parentToChildren = buildProcessTree(pid, ppid);
        
        // 从kill进程开始，递归收集所有应该被杀死的进程
        dfs(parentToChildren, kill, result);
        
        return result;
    }
    
    /**
     * 构建父进程到子进程的映射
     * @param pid 进程ID列表
     * @param ppid 父进程ID列表
     * @return 父进程到子进程的映射
     */
    private Map<Integer, List<Integer>> buildProcessTree(List<Integer> pid, List<Integer> ppid) {
        Map<Integer, List<Integer>> parentToChildren = new HashMap<>();
        
        // 遍历所有进程，构建父子关系
        for (int i = 0; i < pid.size(); i++) {
            int parentId = ppid.get(i);
            int childId = pid.get(i);
            
            // 如果父进程不存在于映射中，创建一个空列表
            parentToChildren.putIfAbsent(parentId, new ArrayList<>());
            
            // 将子进程添加到父进程的子列表中
            parentToChildren.get(parentId).add(childId);
        }
        
        return parentToChildren;
    }
    
    /**
     * 深度优先搜索，收集所有应该被杀死的进程
     * @param parentToChildren 父进程到子进程的映射
     * @param currentProcess 当前处理的进程ID
     * @param result 结果列表
     */
    private void dfs(Map<Integer, List<Integer>> parentToChildren, int currentProcess, List<Integer> result) {
        // 将当前进程添加到结果列表中（标记为需要被杀死）
        result.add(currentProcess);
        
        // 获取当前进程的所有子进程
        List<Integer> children = parentToChildren.getOrDefault(currentProcess, Collections.emptyList());
        
        // 递归处理每个子进程
        for (int child : children) {
            dfs(parentToChildren, child, result);
        }
    }
    
    // 使用广度优先搜索解决问题（迭代方法）
    public List<Integer> killProcessBFS(List<Integer> pid, List<Integer> ppid, int kill) {
        // 结果列表，存储所有应该被杀死的进程ID
        List<Integer> result = new ArrayList<>();
        
        // 构建父进程到子进程的映射
        Map<Integer, List<Integer>> parentToChildren = buildProcessTree(pid, ppid);
        
        // 使用队列进行广度优先搜索
        Queue<Integer> queue = new LinkedList<>();
        queue.offer(kill);
        
        while (!queue.isEmpty()) {
            int currentProcess = queue.poll();
            result.add(currentProcess);
            
            // 将当前进程的所有子进程加入队列
            List<Integer> children = parentToChildren.getOrDefault(currentProcess, Collections.emptyList());
            for (int child : children) {
                queue.offer(child);
            }
        }
        
        return result;
    }
    
    // 测试函数
    public static void main(String[] args) {
        Code13_KillProcess solution = new Code13_KillProcess();
        
        // 测试用例1
        List<Integer> pid1 = Arrays.asList(1, 3, 10, 5);
        List<Integer> ppid1 = Arrays.asList(3, 0, 5, 3);
        int kill1 = 5;
        
        System.out.println("测试用例1 (DFS方法):");
        System.out.println("pid = [1, 3, 10, 5]");
        System.out.println("ppid = [3, 0, 5, 3]");
        System.out.println("kill = 5");
        System.out.println("输出: " + solution.killProcess(pid1, ppid1, kill1));
        System.out.println("期望: [5, 10]");
        System.out.println();
        
        System.out.println("测试用例1 (BFS方法):");
        System.out.println("输出: " + solution.killProcessBFS(pid1, ppid1, kill1));
        System.out.println("期望: [5, 10]");
        System.out.println();
        
        // 测试用例2
        List<Integer> pid2 = Arrays.asList(1);
        List<Integer> ppid2 = Arrays.asList(0);
        int kill2 = 1;
        
        System.out.println("测试用例2 (DFS方法):");
        System.out.println("pid = [1]");
        System.out.println("ppid = [0]");
        System.out.println("kill = 1");
        System.out.println("输出: " + solution.killProcess(pid2, ppid2, kill2));
        System.out.println("期望: [1]");
        System.out.println();
        
        // 测试用例3：更复杂的树结构
        List<Integer> pid3 = Arrays.asList(1, 2, 3, 4, 5, 6, 7);
        List<Integer> ppid3 = Arrays.asList(0, 1, 1, 2, 2, 3, 3);
        int kill3 = 2;
        
        System.out.println("测试用例3 (DFS方法):");
        System.out.println("pid = [1, 2, 3, 4, 5, 6, 7]");
        System.out.println("ppid = [0, 1, 1, 2, 2, 3, 3]");
        System.out.println("kill = 2");
        System.out.println("输出: " + solution.killProcess(pid3, ppid3, kill3));
        // 期望: [2, 4, 5]
        System.out.println("期望: [2, 4, 5]");
    }
}