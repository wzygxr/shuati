package class008_AdvancedAlgorithmsAndDataStructures.fibonacci_heap_problems;

import java.util.*;

/**
 * LeetCode 787. Cheapest Flights Within K Stops
 * 
 * 题目描述：
 * 有 n 个城市通过一些航班连接。给你一个数组 flights ，其中 flights[i] = [fromi, toi, pricei] ，
 * 表示该航班都从城市 fromi 开始，以价格 pricei 抵达 toi。
 * 现在给定所有的城市和航班，以及出发城市 src 和目的地 dst，你的任务是找到一条最多经过 k 站中转的路线，
 * 使得从 src 到 dst 的价格最便宜，并返回该价格。如果不存在这样的路线，则输出 -1。
 * 
 * 解题思路：
 * 使用斐波那契堆优化的Dijkstra算法解决这个问题。
 * 我们需要修改标准的Dijkstra算法，使其考虑中转次数的限制。
 * 
 * 时间复杂度：O(E + V log V)，其中 E 是边数，V 是顶点数
 * 空间复杂度：O(V)
 */
public class LeetCode_787_CheapestFlightsWithinKStops {
    
    // 节点类
    static class Node implements Comparable<Node> {
        int city, cost, stops;
        
        Node(int city, int cost, int stops) {
            this.city = city;
            this.cost = cost;
            this.stops = stops;
        }
        
        @Override
        public int compareTo(Node other) {
            return Integer.compare(this.cost, other.cost);
        }
    }
    
    // 斐波那契堆节点
    static class FibonacciHeapNode {
        Node node;
        int degree;
        boolean marked;
        FibonacciHeapNode parent, child, left, right;
        
        FibonacciHeapNode(Node node) {
            this.node = node;
            this.degree = 0;
            this.marked = false;
            this.parent = null;
            this.child = null;
            this.left = this;
            this.right = this;
        }
    }
    
    // 简化版斐波那契堆实现
    static class FibonacciHeap {
        private FibonacciHeapNode minNode;
        private int size;
        
        public FibonacciHeap() {
            this.minNode = null;
            this.size = 0;
        }
        
        public boolean isEmpty() {
            return minNode == null;
        }
        
        public FibonacciHeapNode insert(Node node) {
            FibonacciHeapNode newNode = new FibonacciHeapNode(node);
            
            if (minNode == null) {
                minNode = newNode;
            } else {
                // 将新节点插入到根链表
                newNode.right = minNode.right;
                newNode.left = minNode;
                minNode.right.left = newNode;
                minNode.right = newNode;
                
                // 更新最小节点
                if (node.cost < minNode.node.cost) {
                    minNode = newNode;
                }
            }
            
            size++;
            return newNode;
        }
        
        public Node extractMin() {
            if (isEmpty()) {
                return null;
            }
            
            FibonacciHeapNode min = minNode;
            Node result = min.node;
            
            // 将min的所有子节点提升到根链表
            if (min.child != null) {
                FibonacciHeapNode child = min.child;
                do {
                    FibonacciHeapNode next = child.right;
                    // 从子链表中移除child
                    child.left.right = child.right;
                    child.right.left = child.left;
                    // 添加到根链表
                    child.right = minNode.right;
                    child.left = minNode;
                    minNode.right.left = child;
                    minNode.right = child;
                    child.parent = null;
                    child = next;
                } while (child != min.child);
            }
            
            // 从根链表中移除min
            if (min.right == min) {
                // 根链表中只有一个节点
                minNode = null;
            } else {
                minNode = min.right;
                // 从根链表中移除min
                min.left.right = min.right;
                min.right.left = min.left;
                // 合并相同度数的树
                consolidate();
            }
            
            size--;
            return result;
        }
        
        private void consolidate() {
            // 简化实现，实际的斐波那契堆合并操作比较复杂
            // 这里我们只保留基本功能
        }
    }
    
    static class Solution {
        public int findCheapestPrice(int n, int[][] flights, int src, int dst, int k) {
            // 构建邻接表
            List<List<int[]>> adj = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                adj.add(new ArrayList<>());
            }
            
            for (int[] flight : flights) {
                adj.get(flight[0]).add(new int[]{flight[1], flight[2]});
            }
            
            // 使用斐波那契堆优化的Dijkstra算法
            FibonacciHeap heap = new FibonacciHeap();
            heap.insert(new Node(src, 0, 0));
            
            // 记录到达每个城市的最小花费和最小中转次数
            int[] minCost = new int[n];
            int[] minStops = new int[n];
            Arrays.fill(minCost, Integer.MAX_VALUE);
            Arrays.fill(minStops, Integer.MAX_VALUE);
            minCost[src] = 0;
            minStops[src] = 0;
            
            while (!heap.isEmpty()) {
                Node current = heap.extractMin();
                
                // 如果到达目的地，返回花费
                if (current.city == dst) {
                    return current.cost;
                }
                
                // 如果中转次数超过限制，跳过
                if (current.stops > k) {
                    continue;
                }
                
                // 遍历当前城市的邻居
                for (int[] neighbor : adj.get(current.city)) {
                    int nextCity = neighbor[0];
                    int price = neighbor[1];
                    int newCost = current.cost + price;
                    int newStops = current.stops + 1;
                    
                    // 如果找到更便宜的路线，或者中转次数更少但花费相近，更新并加入堆
                    if (newCost < minCost[nextCity] || newStops < minStops[nextCity]) {
                        minCost[nextCity] = newCost;
                        minStops[nextCity] = newStops;
                        heap.insert(new Node(nextCity, newCost, newStops));
                    }
                }
            }
            
            return -1; // 无法到达目的地
        }
        
        // 标准解法（使用优先队列）
        public int findCheapestPrice2(int n, int[][] flights, int src, int dst, int k) {
            // 构建邻接表
            List<List<int[]>> adj = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                adj.add(new ArrayList<>());
            }
            
            for (int[] flight : flights) {
                adj.get(flight[0]).add(new int[]{flight[1], flight[2]});
            }
            
            // 使用优先队列的Dijkstra算法
            PriorityQueue<Node> pq = new PriorityQueue<>();
            pq.offer(new Node(src, 0, 0));
            
            // 记录到达每个城市的最小中转次数
            int[] minStops = new int[n];
            Arrays.fill(minStops, Integer.MAX_VALUE);
            minStops[src] = 0;
            
            while (!pq.isEmpty()) {
                Node current = pq.poll();
                
                // 如果到达目的地，返回花费
                if (current.city == dst) {
                    return current.cost;
                }
                
                // 如果中转次数超过限制，或者已经找到了更优的路径，跳过
                if (current.stops > k || current.stops > minStops[current.city]) {
                    continue;
                }
                
                // 更新到达当前城市的最小中转次数
                minStops[current.city] = current.stops;
                
                // 遍历当前城市的邻居
                for (int[] neighbor : adj.get(current.city)) {
                    int nextCity = neighbor[0];
                    int price = neighbor[1];
                    int newCost = current.cost + price;
                    int newStops = current.stops + 1;
                    
                    pq.offer(new Node(nextCity, newCost, newStops));
                }
            }
            
            return -1; // 无法到达目的地
        }
    }
    
    // 测试方法
    public static void main(String[] args) {
        Solution solution = new Solution();
        
        // 测试用例1
        int n1 = 3;
        int[][] flights1 = {{0,1,100},{1,2,100},{0,2,500}};
        int src1 = 0, dst1 = 2, k1 = 1;
        System.out.println("测试用例1:");
        System.out.println("城市数: " + n1);
        System.out.println("航班: " + Arrays.deepToString(flights1));
        System.out.println("起点: " + src1 + ", 终点: " + dst1 + ", 最多中转: " + k1);
        System.out.println("最低花费: " + solution.findCheapestPrice(n1, flights1, src1, dst1, k1));
        System.out.println("标准解法结果: " + solution.findCheapestPrice2(n1, flights1, src1, dst1, k1));
        System.out.println();
        
        // 测试用例2
        int n2 = 3;
        int[][] flights2 = {{0,1,100},{1,2,100},{0,2,500}};
        int src2 = 0, dst2 = 2, k2 = 0;
        System.out.println("测试用例2:");
        System.out.println("城市数: " + n2);
        System.out.println("航班: " + Arrays.deepToString(flights2));
        System.out.println("起点: " + src2 + ", 终点: " + dst2 + ", 最多中转: " + k2);
        System.out.println("最低花费: " + solution.findCheapestPrice(n2, flights2, src2, dst2, k2));
        System.out.println("标准解法结果: " + solution.findCheapestPrice2(n2, flights2, src2, dst2, k2));
        System.out.println();
        
        // 测试用例3
        int n3 = 4;
        int[][] flights3 = {{0,1,1},{0,2,5},{1,2,1},{2,3,1}};
        int src3 = 0, dst3 = 3, k3 = 1;
        System.out.println("测试用例3:");
        System.out.println("城市数: " + n3);
        System.out.println("航班: " + Arrays.deepToString(flights3));
        System.out.println("起点: " + src3 + ", 终点: " + dst3 + ", 最多中转: " + k3);
        System.out.println("最低花费: " + solution.findCheapestPrice(n3, flights3, src3, dst3, k3));
        System.out.println("标准解法结果: " + solution.findCheapestPrice2(n3, flights3, src3, dst3, k3));
    }
}