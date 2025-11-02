package class062;

import java.util.*;

// 公交路线
// 给你一个数组 routes ，表示一系列公交线路，其中每个 routes[i] 表示一条公交线路，第 i 辆公交车将会在上面循环行驶。
// 例如，路线 routes[0] = [1, 5, 7] 表示第 0 辆公交车会一直按序列 1 -> 5 -> 7 -> 1 -> 5 -> 7 -> 1 -> ... 这样的路线行驶。
// 现在从 source 车站出发（初始时不在公交车上），需要前往 target 车站。 期间仅可乘坐公交车。
// 求出最少乘坐的公交车数量。如果不可能到达终点车站，返回 -1。
// 测试链接 : https://leetcode.cn/problems/bus-routes/
// 
// 算法思路：
// 使用BFS进行路线搜索。关键优化是构建站点到路线的映射，避免在站点层面进行搜索。
// 每个状态表示当前所在的路线，目标是找到包含目标站点的路线。
// 
// 时间复杂度：O(R + S)，其中R是路线数量，S是站点数量
// 空间复杂度：O(R + S)，用于存储映射关系和访问状态
// 
// 工程化考量：
// 1. 站点-路线映射：预处理每个站点属于哪些路线
// 2. 路线级别BFS：在路线层面进行搜索，减少状态空间
// 3. 访问标记：记录已访问的路线，避免重复计算
// 4. 边界情况：起点就是终点
public class Code25_BusRoutes {

    public static int numBusesToDestination(int[][] routes, int source, int target) {
        // 边界情况：起点就是终点
        if (source == target) {
            return 0;
        }
        
        int n = routes.length;
        
        // 构建站点到路线的映射
        Map<Integer, List<Integer>> stopToRoutes = new HashMap<>();
        for (int i = 0; i < n; i++) {
            for (int stop : routes[i]) {
                stopToRoutes.computeIfAbsent(stop, k -> new ArrayList<>()).add(i);
            }
        }
        
        // 边界情况：起点或终点不在任何路线上
        if (!stopToRoutes.containsKey(source) || !stopToRoutes.containsKey(target)) {
            return -1;
        }
        
        // BFS队列和访问记录（路线级别）
        Queue<Integer> queue = new LinkedList<>();
        boolean[] visitedRoute = new boolean[n];
        
        // 将包含起点的所有路线加入队列
        for (int route : stopToRoutes.get(source)) {
            queue.offer(route);
            visitedRoute[route] = true;
        }
        
        int buses = 1; // 已经乘坐了一辆公交车
        
        while (!queue.isEmpty()) {
            int size = queue.size();
            
            // 处理当前层的所有路线
            for (int i = 0; i < size; i++) {
                int currentRoute = queue.poll();
                
                // 检查当前路线是否包含目标站点
                for (int stop : routes[currentRoute]) {
                    if (stop == target) {
                        return buses;
                    }
                    
                    // 通过当前站点的其他路线继续搜索
                    for (int nextRoute : stopToRoutes.get(stop)) {
                        if (!visitedRoute[nextRoute]) {
                            visitedRoute[nextRoute] = true;
                            queue.offer(nextRoute);
                        }
                    }
                }
            }
            
            buses++;
        }
        
        return -1;
    }
    
    // 优化版本：使用双向BFS
    public static int numBusesToDestinationBidirectional(int[][] routes, int source, int target) {
        if (source == target) return 0;
        
        int n = routes.length;
        
        // 构建站点到路线的映射
        Map<Integer, List<Integer>> stopToRoutes = new HashMap<>();
        for (int i = 0; i < n; i++) {
            for (int stop : routes[i]) {
                stopToRoutes.computeIfAbsent(stop, k -> new ArrayList<>()).add(i);
            }
        }
        
        if (!stopToRoutes.containsKey(source) || !stopToRoutes.containsKey(target)) {
            return -1;
        }
        
        // 双向BFS
        Set<Integer> startRoutes = new HashSet<>(stopToRoutes.get(source));
        Set<Integer> targetRoutes = new HashSet<>(stopToRoutes.get(target));
        boolean[] visited = new boolean[n];
        
        // 如果起点和终点有共同的路线
        for (int route : startRoutes) {
            if (targetRoutes.contains(route)) {
                return 1;
            }
            visited[route] = true;
        }
        
        for (int route : targetRoutes) {
            visited[route] = true;
        }
        
        int buses = 1;
        
        while (!startRoutes.isEmpty() && !targetRoutes.isEmpty()) {
            // 总是从较小的集合开始扩展
            if (startRoutes.size() > targetRoutes.size()) {
                Set<Integer> temp = startRoutes;
                startRoutes = targetRoutes;
                targetRoutes = temp;
            }
            
            Set<Integer> nextRoutes = new HashSet<>();
            buses++;
            
            for (int route : startRoutes) {
                // 遍历当前路线的所有站点，找到相邻路线
                for (int stop : routes[route]) {
                    for (int nextRoute : stopToRoutes.get(stop)) {
                        if (targetRoutes.contains(nextRoute)) {
                            return buses;
                        }
                        if (!visited[nextRoute]) {
                            visited[nextRoute] = true;
                            nextRoutes.add(nextRoute);
                        }
                    }
                }
            }
            
            startRoutes = nextRoutes;
        }
        
        return -1;
    }
    
    // 单元测试
    public static void main(String[] args) {
        // 测试用例1：标准情况
        int[][] routes1 = {{1,2,7},{3,6,7}};
        int source1 = 1, target1 = 6;
        System.out.println("测试用例1 - 最少公交车数: " + numBusesToDestination(routes1, source1, target1)); // 期望输出: 2
        
        // 测试用例2：需要换乘多次
        int[][] routes2 = {{7,12},{4,5,15},{6},{15,19},{9,12,13}};
        int source2 = 15, target2 = 12;
        System.out.println("测试用例2 - 最少公交车数: " + numBusesToDestination(routes2, source2, target2)); // 期望输出: -1
        
        // 测试用例3：起点就是终点
        int[][] routes3 = {{1,2,3},{3,4,5}};
        int source3 = 3, target3 = 3;
        System.out.println("测试用例3 - 最少公交车数: " + numBusesToDestination(routes3, source3, target3)); // 期望输出: 0
        
        // 测试用例4：复杂换乘
        int[][] routes4 = {{1,2,3},{3,4,5},{5,6,7},{7,8,9}};
        int source4 = 1, target4 = 9;
        System.out.println("测试用例4 - 最少公交车数: " + numBusesToDestination(routes4, source4, target4)); // 期望输出: 4
    }
}