package class008_AdvancedAlgorithmsAndDataStructures.fibonacci_heap_problems;

import java.util.*;

/**
 * LeetCode 1719. Number Of Ways To Reconstruct A Tree
 * 
 * 题目描述：
 * 给你一个数组 pairs ，其中 pairs[i] = [xi, yi] ，并且满足：
 * - pairs 中没有重复元素
 * - xi < yi
 * 
 * 请你构造一个有 n 个节点的树，使得对于 pairs 中的每个 [xi, yi] ，
 * xi 是 yi 的祖先或者 yi 是 xi 的祖先。
 * 
 * 返回：
 * - 如果有多种构造方法，返回任意一种的有效方案即可，返回 2；
 * - 如果有且仅有一种构造方法，返回 1；
 * - 如果无法构造满足条件的树，返回 0。
 * 
 * 解题思路：
 * 这个问题可以转化为图论问题。我们需要分析节点之间的父子关系。
 * 使用斐波那契堆来优化寻找具有最大度数的节点的过程。
 * 
 * 时间复杂度：O(n log n + m)，其中 n 是节点数，m 是边数
 * 空间复杂度：O(n + m)
 */
public class LeetCode_1719_NumberOfWaysToReconstructATree {
    
    // 简化版斐波那契堆节点
    static class FibonacciHeapNode {
        int value, degree;
        boolean marked;
        FibonacciHeapNode parent, child, left, right;
        
        FibonacciHeapNode(int value, int degree) {
            this.value = value;
            this.degree = degree;
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
        
        public FibonacciHeapNode insert(int value, int degree) {
            FibonacciHeapNode newNode = new FibonacciHeapNode(value, degree);
            
            if (minNode == null) {
                minNode = newNode;
            } else {
                // 将新节点插入到根链表
                newNode.right = minNode.right;
                newNode.left = minNode;
                minNode.right.left = newNode;
                minNode.right = newNode;
                
                // 更新最小节点（在这里我们寻找度数最大的节点）
                if (degree > minNode.degree) {
                    minNode = newNode;
                }
            }
            
            size++;
            return newNode;
        }
        
        public int extractMaxDegree() {
            if (isEmpty()) {
                return -1;
            }
            
            FibonacciHeapNode max = minNode;
            int result = max.value;
            
            // 从根链表中移除max
            if (max.right == max) {
                // 根链表中只有一个节点
                minNode = null;
            } else {
                // 更新minNode为任意一个相邻节点
                minNode = max.right;
                // 从根链表中移除max
                max.left.right = max.right;
                max.right.left = max.left;
            }
            
            size--;
            return result;
        }
    }
    
    static class Solution {
        public int checkWays(int[][] pairs) {
            // 构建邻接表和度数表
            Map<Integer, Set<Integer>> adj = new HashMap<>();
            Map<Integer, Integer> degree = new HashMap<>();
            
            // 统计所有节点
            Set<Integer> nodes = new HashSet<>();
            for (int[] pair : pairs) {
                int u = pair[0], v = pair[1];
                nodes.add(u);
                nodes.add(v);
                
                // 更新邻接表
                adj.computeIfAbsent(u, k -> new HashSet<>()).add(v);
                adj.computeIfAbsent(v, k -> new HashSet<>()).add(u);
                
                // 更新度数
                degree.put(u, degree.getOrDefault(u, 0) + 1);
                degree.put(v, degree.getOrDefault(v, 0) + 1);
            }
            
            int n = nodes.size();
            
            // 使用斐波那契堆按度数排序节点
            FibonacciHeap heap = new FibonacciHeap();
            Map<Integer, FibonacciHeapNode> nodeMap = new HashMap<>();
            
            for (int node : nodes) {
                int deg = degree.get(node);
                FibonacciHeapNode heapNode = heap.insert(node, deg);
                nodeMap.put(node, heapNode);
            }
            
            // 检查是否存在根节点（度数为n-1的节点）
            boolean hasRoot = false;
            for (int node : nodes) {
                if (degree.get(node) == n - 1) {
                    hasRoot = true;
                    break;
                }
            }
            
            if (!hasRoot) {
                return 0; // 无法构造树
            }
            
            // 构建父子关系
            Map<Integer, Integer> parent = new HashMap<>(); // child -> parent
            boolean multipleWays = false;
            
            // 按度数从大到小处理节点
            while (!heap.isEmpty()) {
                int node = heap.extractMaxDegree();
                int nodeDegree = degree.get(node);
                
                // 寻找父节点：度数大于等于当前节点度数且与当前节点相邻的节点
                Integer parentNode = null;
                int parentDegree = Integer.MAX_VALUE;
                
                for (int neighbor : adj.getOrDefault(node, new HashSet<>())) {
                    int neighborDegree = degree.get(neighbor);
                    // 父节点的度数应该大于等于当前节点的度数
                    if (neighborDegree >= nodeDegree && neighborDegree < parentDegree) {
                        parentNode = neighbor;
                        parentDegree = neighborDegree;
                    }
                }
                
                if (parentNode == null) {
                    // 根节点不需要父节点
                    if (nodeDegree != n - 1) {
                        return 0; // 无法构造树
                    }
                } else {
                    // 检查父子关系是否有效
                    Set<Integer> nodeNeighbors = adj.getOrDefault(node, new HashSet<>());
                    Set<Integer> parentNeighbors = adj.getOrDefault(parentNode, new HashSet<>());
                    
                    // 当前节点的所有邻居都应该是父节点的邻居
                    if (!parentNeighbors.containsAll(nodeNeighbors)) {
                        return 0; // 无法构造树
                    }
                    
                    // 如果父节点和当前节点度数相同，说明有多种构造方法
                    if (degree.get(parentNode) == nodeDegree) {
                        multipleWays = true;
                    }
                    
                    parent.put(node, parentNode);
                }
            }
            
            return multipleWays ? 2 : 1;
        }
        
        // 标准解法（使用TreeSet）
        public int checkWays2(int[][] pairs) {
            // 构建邻接表和度数表
            Map<Integer, Set<Integer>> adj = new HashMap<>();
            Map<Integer, Integer> degree = new HashMap<>();
            
            // 统计所有节点
            Set<Integer> nodes = new HashSet<>();
            for (int[] pair : pairs) {
                int u = pair[0], v = pair[1];
                nodes.add(u);
                nodes.add(v);
                
                // 更新邻接表
                adj.computeIfAbsent(u, k -> new HashSet<>()).add(v);
                adj.computeIfAbsent(v, k -> new HashSet<>()).add(u);
                
                // 更新度数
                degree.put(u, degree.getOrDefault(u, 0) + 1);
                degree.put(v, degree.getOrDefault(v, 0) + 1);
            }
            
            int n = nodes.size();
            
            // 检查是否存在根节点（度数为n-1的节点）
            boolean hasRoot = false;
            for (int node : nodes) {
                if (degree.get(node) == n - 1) {
                    hasRoot = true;
                    break;
                }
            }
            
            if (!hasRoot) {
                return 0; // 无法构造树
            }
            
            // 构建父子关系
            Map<Integer, Integer> parent = new HashMap<>(); // child -> parent
            boolean multipleWays = false;
            
            // 按度数从大到小排序节点
            List<Integer> sortedNodes = new ArrayList<>(nodes);
            sortedNodes.sort((a, b) -> degree.get(b) - degree.get(a));
            
            // 处理每个节点
            for (int node : sortedNodes) {
                int nodeDegree = degree.get(node);
                
                // 寻找父节点：度数大于等于当前节点度数且与当前节点相邻的节点
                Integer parentNode = null;
                int parentDegree = Integer.MAX_VALUE;
                
                for (int neighbor : adj.getOrDefault(node, new HashSet<>())) {
                    int neighborDegree = degree.get(neighbor);
                    // 父节点的度数应该大于等于当前节点的度数
                    if (neighborDegree >= nodeDegree && neighborDegree < parentDegree) {
                        parentNode = neighbor;
                        parentDegree = neighborDegree;
                    }
                }
                
                if (parentNode == null) {
                    // 根节点不需要父节点
                    if (nodeDegree != n - 1) {
                        return 0; // 无法构造树
                    }
                } else {
                    // 检查父子关系是否有效
                    Set<Integer> nodeNeighbors = adj.getOrDefault(node, new HashSet<>());
                    Set<Integer> parentNeighbors = adj.getOrDefault(parentNode, new HashSet<>());
                    
                    // 当前节点的所有邻居都应该是父节点的邻居
                    if (!parentNeighbors.containsAll(nodeNeighbors)) {
                        return 0; // 无法构造树
                    }
                    
                    // 如果父节点和当前节点度数相同，说明有多种构造方法
                    if (degree.get(parentNode) == nodeDegree) {
                        multipleWays = true;
                    }
                    
                    parent.put(node, parentNode);
                }
            }
            
            return multipleWays ? 2 : 1;
        }
    }
    
    // 测试方法
    public static void main(String[] args) {
        Solution solution = new Solution();
        
        // 测试用例1
        int[][] pairs1 = {{1,2},{2,3}};
        System.out.println("测试用例1:");
        System.out.println("pairs: " + Arrays.deepToString(pairs1));
        System.out.println("结果: " + solution.checkWays(pairs1));
        System.out.println("标准解法结果: " + solution.checkWays2(pairs1));
        System.out.println();
        
        // 测试用例2
        int[][] pairs2 = {{1,2},{2,3},{1,3}};
        System.out.println("测试用例2:");
        System.out.println("pairs: " + Arrays.deepToString(pairs2));
        System.out.println("结果: " + solution.checkWays(pairs2));
        System.out.println("标准解法结果: " + solution.checkWays2(pairs2));
        System.out.println();
        
        // 测试用例3
        int[][] pairs3 = {{1,2},{2,3},{2,4},{1,5}};
        System.out.println("测试用例3:");
        System.out.println("pairs: " + Arrays.deepToString(pairs3));
        System.out.println("结果: " + solution.checkWays(pairs3));
        System.out.println("标准解法结果: " + solution.checkWays2(pairs3));
        System.out.println();
        
        // 测试用例4
        int[][] pairs4 = {{1,2},{1,3},{2,3}};
        System.out.println("测试用例4:");
        System.out.println("pairs: " + Arrays.deepToString(pairs4));
        System.out.println("结果: " + solution.checkWays(pairs4));
        System.out.println("标准解法结果: " + solution.checkWays2(pairs4));
    }
}