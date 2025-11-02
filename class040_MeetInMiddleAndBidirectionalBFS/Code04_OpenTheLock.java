package class063;

import java.util.*;

// 打开转盘锁
// 你有一个带有四个圆形拨轮的转盘锁。每个拨轮都有10个数字： '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' 。
// 每个拨轮可以自由旋转：例如把 '9' 变为 '0'，'0' 变为 '9' 。每次旋转都只能旋转一个拨轮的一位数字。
// 锁的初始数字为 '0000' ，一个代表四个拨轮的数字的字符串。
// 列表 deadends 包含了一组死亡数字，一旦拨轮的数字和列表里的任何一个元素相同，这个锁将会被永久锁定，无法再被旋转。
// 字符串 target 代表可以解锁的数字，你需要给出解锁需要的最小旋转次数，如果无论如何不能解锁，返回 -1 。
// 测试链接 : https://leetcode.cn/problems/open-the-lock/
// 
// 算法思路：
// 使用双向BFS算法，从起点"0000"和终点target同时开始搜索，一旦两个搜索相遇，就找到了最短路径
// 时间复杂度：O(10^4 * 8 + D)，其中D是deadends的长度，10^4是状态数，8是每个状态的邻居数
// 空间复杂度：O(10^4 + D)
// 
// 工程化考量：
// 1. 异常处理：检查初始状态是否在deadends中
// 2. 性能优化：使用双向BFS减少搜索空间
// 3. 可读性：变量命名清晰，注释详细
// 
// 语言特性差异：
// Java中使用HashSet进行快速查找和去重，使用Queue进行BFS操作

public class Code04_OpenTheLock {
    
    /**
     * 计算打开转盘锁所需的最小旋转次数
     * @param deadends 死亡数字列表
     * @param target 目标数字
     * @return 最小旋转次数，如果无法解锁返回-1
     */
    public static int openLock(String[] deadends, String target) {
        // 将deadends转换为HashSet以提高查找效率
        Set<String> deadSet = new HashSet<>(Arrays.asList(deadends));
        
        // 如果初始状态"0000"在deadends中，直接返回-1
        if (deadSet.contains("0000")) {
            return -1;
        }
        
        // 如果目标就是初始状态，返回0
        if ("0000".equals(target)) {
            return 0;
        }
        
        // 初始化双向BFS的队列和访问集合
        Queue<String> queue1 = new LinkedList<>(); // 从起点开始的队列
        Queue<String> queue2 = new LinkedList<>(); // 从终点开始的队列
        Set<String> visited1 = new HashSet<>();    // 从起点开始的访问集合
        Set<String> visited2 = new HashSet<>();    // 从终点开始的访问集合
        
        queue1.offer("0000");
        queue2.offer(target);
        visited1.add("0000");
        visited2.add(target);
        
        int steps = 0;
        
        // 双向BFS
        while (!queue1.isEmpty() && !queue2.isEmpty()) {
            // 总是从较小的队列开始扩展，优化性能
            if (queue1.size() > queue2.size()) {
                Queue<String> tempQueue = queue1;
                queue1 = queue2;
                queue2 = tempQueue;
                
                Set<String> tempVisited = visited1;
                visited1 = visited2;
                visited2 = tempVisited;
            }
            
            steps++;
            int size = queue1.size();
            
            // 扩展当前层的所有节点
            for (int i = 0; i < size; i++) {
                String current = queue1.poll();
                
                // 生成当前状态的所有邻居状态
                for (String next : getNeighbors(current)) {
                    // 如果邻居状态在deadends中，跳过
                    if (deadSet.contains(next)) {
                        continue;
                    }
                    
                    // 如果邻居状态已经被访问过，跳过
                    if (visited1.contains(next)) {
                        continue;
                    }
                    
                    // 如果邻居状态在另一侧的访问集合中，说明两路相遇，返回步数
                    if (visited2.contains(next)) {
                        return steps;
                    }
                    
                    // 将邻居状态加入队列和访问集合
                    queue1.offer(next);
                    visited1.add(next);
                }
            }
        }
        
        return -1; // 无法解锁
    }
    
    /**
     * 生成当前状态的所有邻居状态
     * @param s 当前状态
     * @return 邻居状态列表
     */
    private static List<String> getNeighbors(String s) {
        List<String> neighbors = new ArrayList<>();
        char[] chars = s.toCharArray();
        
        // 对每个位置尝试向上和向下旋转
        for (int i = 0; i < 4; i++) {
            char original = chars[i];
            
            // 向上旋转
            chars[i] = (char) ((original - '0' + 1) % 10 + '0');
            neighbors.add(new String(chars));
            
            // 向下旋转
            chars[i] = (char) ((original - '0' + 9) % 10 + '0');
            neighbors.add(new String(chars));
            
            // 恢复原字符
            chars[i] = original;
        }
        
        return neighbors;
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试用例1
        String[] deadends1 = {"0201", "0101", "0102", "1212", "2002"};
        String target1 = "0202";
        System.out.println("测试用例1:");
        System.out.println("deadends: [\"0201\",\"0101\",\"0102\",\"1212\",\"2002\"]");
        System.out.println("target: \"0202\"");
        System.out.println("期望输出: 6");
        System.out.println("实际输出: " + openLock(deadends1, target1));
        System.out.println();
        
        // 测试用例2
        String[] deadends2 = {"8888"};
        String target2 = "0009";
        System.out.println("测试用例2:");
        System.out.println("deadends: [\"8888\"]");
        System.out.println("target: \"0009\"");
        System.out.println("期望输出: 1");
        System.out.println("实际输出: " + openLock(deadends2, target2));
        System.out.println();
        
        // 测试用例3
        String[] deadends3 = {"8887","8889","8878","8898","8788","8988","7888","9888"};
        String target3 = "8888";
        System.out.println("测试用例3:");
        System.out.println("deadends: [\"8887\",\"8889\",\"8878\",\"8898\",\"8788\",\"8988\",\"7888\",\"9888\"]");
        System.out.println("target: \"8888\"");
        System.out.println("期望输出: -1");
        System.out.println("实际输出: " + openLock(deadends3, target3));
    }
}