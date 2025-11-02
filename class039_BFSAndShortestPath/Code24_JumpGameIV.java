package class062;

import java.util.*;

// 跳跃游戏 IV
// 给你一个整数数组 arr ，你一开始在数组的第一个元素处（下标为 0）。
// 每一步，你可以从下标 i 跳到下标 i + 1 、i - 1 或者 j ，其中 arr[i] == arr[j] 且 i != j。
// 请你返回到达数组最后一个元素的下标处所需的最少操作次数。
// 注意：任何时候你都不能跳到数组外面。
// 测试链接 : https://leetcode.cn/problems/jump-game-iv/
// 
// 算法思路：
// 使用BFS进行状态搜索。关键优化是使用值映射表记录相同值的所有位置，避免重复计算。
// 每个位置可以向左、向右移动，或者跳到所有相同值的位置。
// 
// 时间复杂度：O(n)，其中n是数组长度，每个位置最多被访问一次
// 空间复杂度：O(n)，用于存储队列、访问状态和值映射表
// 
// 工程化考量：
// 1. 值映射表：预处理相同值的位置，提高跳跃效率
// 2. 访问标记：使用数组记录已访问位置
// 3. 边界检查：确保移动后的位置在数组范围内
// 4. 性能优化：跳跃后清空值映射表，避免重复访问
public class Code24_JumpGameIV {

    public static int minJumps(int[] arr) {
        int n = arr.length;
        
        // 边界情况：数组只有一个元素
        if (n == 1) {
            return 0;
        }
        
        // 构建值映射表：值 -> 位置列表
        Map<Integer, List<Integer>> valueMap = new HashMap<>();
        for (int i = 0; i < n; i++) {
            valueMap.computeIfAbsent(arr[i], k -> new ArrayList<>()).add(i);
        }
        
        // BFS队列和访问记录
        Queue<Integer> queue = new LinkedList<>();
        boolean[] visited = new boolean[n];
        
        queue.offer(0);
        visited[0] = true;
        int steps = 0;
        
        while (!queue.isEmpty()) {
            steps++;
            int size = queue.size();
            
            // 处理当前层的所有位置
            for (int i = 0; i < size; i++) {
                int current = queue.poll();
                
                // 向左移动
                if (current - 1 >= 0 && !visited[current - 1]) {
                    if (current - 1 == n - 1) {
                        return steps;
                    }
                    visited[current - 1] = true;
                    queue.offer(current - 1);
                }
                
                // 向右移动
                if (current + 1 < n && !visited[current + 1]) {
                    if (current + 1 == n - 1) {
                        return steps;
                    }
                    visited[current + 1] = true;
                    queue.offer(current + 1);
                }
                
                // 跳跃到相同值的位置
                if (valueMap.containsKey(arr[current])) {
                    for (int jumpPos : valueMap.get(arr[current])) {
                        if (jumpPos != current && !visited[jumpPos]) {
                            if (jumpPos == n - 1) {
                                return steps;
                            }
                            visited[jumpPos] = true;
                            queue.offer(jumpPos);
                        }
                    }
                    // 重要优化：跳跃后清空该值的映射，避免重复访问
                    valueMap.remove(arr[current]);
                }
            }
        }
        
        return -1; // 理论上不会执行到这里
    }
    
    // 优化版本：使用双向BFS
    public static int minJumpsBidirectional(int[] arr) {
        int n = arr.length;
        if (n == 1) return 0;
        
        // 构建值映射表
        Map<Integer, List<Integer>> valueMap = new HashMap<>();
        for (int i = 0; i < n; i++) {
            valueMap.computeIfAbsent(arr[i], k -> new ArrayList<>()).add(i);
        }
        
        // 双向BFS
        Set<Integer> startSet = new HashSet<>();
        Set<Integer> endSet = new HashSet<>();
        boolean[] visited = new boolean[n];
        
        startSet.add(0);
        endSet.add(n - 1);
        visited[0] = true;
        visited[n - 1] = true;
        int steps = 0;
        
        while (!startSet.isEmpty() && !endSet.isEmpty()) {
            // 总是从较小的集合开始扩展
            if (startSet.size() > endSet.size()) {
                Set<Integer> temp = startSet;
                startSet = endSet;
                endSet = temp;
            }
            
            Set<Integer> nextSet = new HashSet<>();
            steps++;
            
            for (int current : startSet) {
                // 向左移动
                if (current - 1 >= 0 && !visited[current - 1]) {
                    if (endSet.contains(current - 1)) {
                        return steps;
                    }
                    visited[current - 1] = true;
                    nextSet.add(current - 1);
                }
                
                // 向右移动
                if (current + 1 < n && !visited[current + 1]) {
                    if (endSet.contains(current + 1)) {
                        return steps;
                    }
                    visited[current + 1] = true;
                    nextSet.add(current + 1);
                }
                
                // 跳跃到相同值的位置
                if (valueMap.containsKey(arr[current])) {
                    for (int jumpPos : valueMap.get(arr[current])) {
                        if (jumpPos != current && !visited[jumpPos]) {
                            if (endSet.contains(jumpPos)) {
                                return steps;
                            }
                            visited[jumpPos] = true;
                            nextSet.add(jumpPos);
                        }
                    }
                    valueMap.remove(arr[current]);
                }
            }
            
            startSet = nextSet;
        }
        
        return -1;
    }
    
    // 单元测试
    public static void main(String[] args) {
        // 测试用例1：标准情况
        int[] arr1 = {100,-23,-23,404,100,23,23,23,3,404};
        System.out.println("测试用例1 - 最少步数: " + minJumps(arr1)); // 期望输出: 3
        
        // 测试用例2：简单情况
        int[] arr2 = {7};
        System.out.println("测试用例2 - 最少步数: " + minJumps(arr2)); // 期望输出: 0
        
        // 测试用例3：需要多次跳跃
        int[] arr3 = {7,6,9,6,9,6,9,7};
        System.out.println("测试用例3 - 最少步数: " + minJumps(arr3)); // 期望输出: 1
        
        // 测试用例4：复杂跳跃
        int[] arr4 = {6,1,9};
        System.out.println("测试用例4 - 最少步数: " + minJumps(arr4)); // 期望输出: 2
        
        // 测试用例5：大量相同值
        int[] arr5 = {11,22,7,7,7,7,7,7,7,22,13};
        System.out.println("测试用例5 - 最少步数: " + minJumps(arr5)); // 期望输出: 3
    }
}