package class062;

import java.util.*;

// 打开转盘锁
// 你有一个带有四个圆形拨轮的转盘锁。每个拨轮都有10个数字： '0', '1', '2', '3', '4', '5', '6', '7', '8', '9'。
// 每个拨轮可以自由旋转：例如把 '9' 变为 '0'，'0' 变为 '9'。每次旋转都只能旋转一个拨轮的一个数字。
// 锁的初始数字为 '0000' ，一个代表四个拨轮的数字的字符串。
// 列表 deadends 包含了一组死亡数字，一旦拨轮的数字和列表里的任何一个元素相同，这个锁将会被永久锁定，无法再被旋转。
// 字符串 target 代表可以解锁的数字，你需要给出解锁需要的最小旋转次数，如果无论如何不能解锁，返回 -1。
// 测试链接 : https://leetcode.cn/problems/open-the-lock/
// 
// 算法思路：
// 使用BFS搜索从"0000"到target的最短路径。每个状态可以旋转8个方向（每个拨轮可以向上或向下旋转）。
// 使用哈希集合记录死亡数字和已访问状态，避免重复访问。
// 
// 时间复杂度：O(10^4 * 8) = O(80000)，因为有10000个可能的状态，每个状态有8个邻居
// 空间复杂度：O(10000)，用于存储队列和访问状态
// 
// 工程化考量：
// 1. 状态表示：使用字符串表示锁的状态
// 2. 邻居生成：为每个位置生成向上和向下旋转的结果
// 3. 死亡数字处理：遇到死亡数字直接跳过
// 4. 边界情况：初始状态就是目标状态或死亡状态
public class Code19_OpenTheLock {

    public static int openLock(String[] deadends, String target) {
        // 使用哈希集合存储死亡数字，提高查找效率
        Set<String> deadSet = new HashSet<>(Arrays.asList(deadends));
        
        // 边界情况：初始状态就是死亡数字
        String start = "0000";
        if (deadSet.contains(start)) {
            return -1;
        }
        
        // 边界情况：初始状态就是目标状态
        if (start.equals(target)) {
            return 0;
        }
        
        // BFS队列和访问记录
        Queue<String> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        
        queue.offer(start);
        visited.add(start);
        int steps = 0;
        
        while (!queue.isEmpty()) {
            steps++;
            int size = queue.size();
            
            // 处理当前层的所有状态
            for (int i = 0; i < size; i++) {
                String current = queue.poll();
                
                // 生成所有可能的邻居状态
                for (String neighbor : getNeighbors(current)) {
                    // 跳过死亡数字和已访问状态
                    if (deadSet.contains(neighbor) || visited.contains(neighbor)) {
                        continue;
                    }
                    
                    // 如果找到目标状态
                    if (neighbor.equals(target)) {
                        return steps;
                    }
                    
                    // 加入队列并标记为已访问
                    visited.add(neighbor);
                    queue.offer(neighbor);
                }
            }
        }
        
        // 无法到达目标状态
        return -1;
    }
    
    // 生成当前状态的所有邻居状态（每个位置可以向上或向下旋转）
    private static List<String> getNeighbors(String current) {
        List<String> neighbors = new ArrayList<>();
        char[] chars = current.toCharArray();
        
        // 对每个位置进行向上和向下旋转
        for (int i = 0; i < 4; i++) {
            char original = chars[i];
            
            // 向上旋转（数字增加）
            chars[i] = (char) ((original - '0' + 1) % 10 + '0');
            neighbors.add(new String(chars));
            
            // 向下旋转（数字减少）
            chars[i] = (char) ((original - '0' + 9) % 10 + '0');
            neighbors.add(new String(chars));
            
            // 恢复原始字符
            chars[i] = original;
        }
        
        return neighbors;
    }
    
    // 单元测试
    public static void main(String[] args) {
        // 测试用例1：标准情况
        String[] deadends1 = {"0201","0101","0102","1212","2002"};
        String target1 = "0202";
        System.out.println("测试用例1 - 最小步数: " + openLock(deadends1, target1)); // 期望输出: 6
        
        // 测试用例2：无法解锁
        String[] deadends2 = {"8888"};
        String target2 = "0009";
        System.out.println("测试用例2 - 最小步数: " + openLock(deadends2, target2)); // 期望输出: 1
        
        // 测试用例3：初始状态就是死亡数字
        String[] deadends3 = {"0000"};
        String target3 = "8888";
        System.out.println("测试用例3 - 最小步数: " + openLock(deadends3, target3)); // 期望输出: -1
        
        // 测试用例4：初始状态就是目标状态
        String[] deadends4 = {"8888","9999"};
        String target4 = "0000";
        System.out.println("测试用例4 - 最小步数: " + openLock(deadends4, target4)); // 期望输出: 0
        
        // 测试用例5：复杂情况
        String[] deadends5 = {"1000","0100","0010","0001","9000","0900","0090","0009"};
        String target5 = "0002";
        System.out.println("测试用例5 - 最小步数: " + openLock(deadends5, target5)); // 期望输出: 2
    }
}