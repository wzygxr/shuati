package class184;

import java.util.*;

/**
 * 使用斐波那契堆优化的Dijkstra算法实现
 * 
 * 应用场景: 网络路由、地图导航
 * 时间复杂度: O(V log V + E) - 使用斐波那契堆
 * 空间复杂度: O(V + E)
 */
public class DijkstraWithFibonacciHeap {
    
    /**
     * 图的边类
     */
    static class Edge {
        int to;     // 目标节点
        int weight; // 边的权重
        
        Edge(int to, int weight) {
            this.to = to;
            this.weight = weight;
        }
    }
    
    /**
     * 斐波那契堆节点类
     */
    static class FibonacciHeapNode {
        int vertex;          // 顶点
        int distance;        // 距离
        int degree;          // 节点的度数
        boolean marked;      // 是否被标记
        FibonacciHeapNode parent;     // 父节点
        FibonacciHeapNode child;      // 第一个子节点
        FibonacciHeapNode left;       // 左兄弟节点
        FibonacciHeapNode right;      // 右兄弟节点
        
        FibonacciHeapNode(int vertex, int distance) {
            this.vertex = vertex;
            this.distance = distance;
            this.degree = 0;
            this.marked = false;
            this.parent = null;
            this.child = null;
            // 初始化为自环双向链表
            this.left = this;
            this.right = this;
        }
    }
    
    /**
     * 斐波那契堆实现
     */
    static class FibonacciHeap {
        private FibonacciHeapNode minNode;  // 指向最小节点
        private int size;                   // 堆中节点数量
        
        FibonacciHeap() {
            this.minNode = null;
            this.size = 0;
        }
        
        boolean isEmpty() {
            return minNode == null;
        }
        
        int size() {
            return size;
        }
        
        /**
         * 插入新节点到堆中
         * 时间复杂度：O(1) 均摊
         */
        FibonacciHeapNode insert(int vertex, int distance) {
            FibonacciHeapNode newNode = new FibonacciHeapNode(vertex, distance);
            
            // 将新节点添加到根链表
            if (minNode == null) {
                // 空堆情况
                minNode = newNode;
            } else {
                // 将新节点插入到根链表的minNode旁边
                linkRootList(newNode, minNode);
                
                // 更新最小节点
                if (newNode.distance < minNode.distance) {
                    minNode = newNode;
                }
            }
            
            // 增加节点计数
            size++;
            return newNode;
        }
        
        /**
         * 提取堆中的最小节点
         * 时间复杂度：O(log n) 均摊
         */
        FibonacciHeapNode extractMin() {
            if (isEmpty()) {
                return null;
            }
            
            FibonacciHeapNode min = minNode;
            
            // 将min的所有子节点提升到根链表
            if (min.child != null) {
                FibonacciHeapNode child = min.child;
                do {
                    FibonacciHeapNode nextChild = child.right;
                    
                    // 从子链表中移除child
                    removeFromChildList(child);
                    
                    // 添加到根链表
                    child.parent = null;
                    linkRootList(child, minNode);
                    
                    child = nextChild;
                } while (child != min.child);
                
                // 清除min的子节点引用
                min.child = null;
            }
            
            // 从根链表中移除min
            if (min.right == min) {
                // 根链表中只有一个节点
                minNode = null;
            } else {
                // 更新根链表
                minNode = min.right;  // 暂时将min的右侧设为新的minNode
                removeFromRootList(min);
                
                // 合并相同度数的树
                consolidate();
            }
            
            // 减少节点计数
            size--;
            
            return min;
        }
        
        /**
         * 减小节点的距离值
         * 时间复杂度：O(1) 均摊
         */
        void decreaseKey(FibonacciHeapNode node, int newDistance) {
            if (newDistance > node.distance) {
                throw new IllegalArgumentException("New distance cannot be greater than current distance");
            }
            
            node.distance = newDistance;
            FibonacciHeapNode parent = node.parent;
            
            // 如果节点在根链表中，或者父节点的距离不大于当前节点，无需其他操作
            if (parent == null || parent.distance <= node.distance) {
                // 如果是根链表中的节点且距离比当前minNode小，更新minNode
                if (parent == null && node.distance < minNode.distance) {
                    minNode = node;
                }
                return;
            }
            
            // 否则，需要进行级联剪枝操作
            cut(node, parent);
            cascadingCut(parent);
        }
        
        // ==================== 辅助方法 ====================
        
        private void linkRootList(FibonacciHeapNode node, FibonacciHeapNode root) {
            // 在根和根的右侧节点之间插入node
            node.right = root.right;
            node.left = root;
            root.right.left = node;
            root.right = node;
        }
        
        private void removeFromRootList(FibonacciHeapNode node) {
            node.left.right = node.right;
            node.right.left = node.left;
        }
        
        private void removeFromChildList(FibonacciHeapNode node) {
            if (node.parent.child == node) {
                // 如果是父节点的第一个子节点，更新父节点的child指针
                if (node.right != node) {
                    node.parent.child = node.right;
                } else {
                    node.parent.child = null;
                }
            }
            
            // 更新子链表中的双向链接
            node.left.right = node.right;
            node.right.left = node.left;
        }
        
        private void linkAsChild(FibonacciHeapNode child, FibonacciHeapNode parent) {
            // 从根链表中移除child
            removeFromRootList(child);
            
            // 重置child的状态
            child.parent = parent;
            child.marked = false;
            
            // 将child添加到parent的子链表中
            if (parent.child == null) {
                // parent没有子节点
                parent.child = child;
                child.left = child;
                child.right = child;
            } else {
                // 将child插入到parent的第一个子节点旁边
                child.right = parent.child.right;
                child.left = parent.child;
                parent.child.right.left = child;
                parent.child.right = child;
            }
            
            // 增加parent的度数
            parent.degree++;
        }
        
        private void consolidate() {
            // 计算最大可能的度数
            int maxDegree = (int) (Math.log(size) / Math.log((1 + Math.sqrt(5)) / 2)) + 1;
            
            // 用于存储不同度数的根节点
            FibonacciHeapNode[] degreeTable = new FibonacciHeapNode[maxDegree];
            
            // 遍历所有根节点
            FibonacciHeapNode start = minNode;
            FibonacciHeapNode current = start;
            boolean isVisited;
            
            do {
                isVisited = false;
                int degree = current.degree;
                FibonacciHeapNode next = current.right;
                
                // 合并相同度数的树
                while (degreeTable[degree] != null) {
                    FibonacciHeapNode other = degreeTable[degree];
                    
                    // 确保current的距离不大于other
                    if (current.distance > other.distance) {
                        FibonacciHeapNode temp = current;
                        current = other;
                        other = temp;
                    }
                    
                    // 将other作为current的子节点
                    linkAsChild(other, current);
                    
                    // 清除度数表中的条目
                    degreeTable[degree] = null;
                    degree++;
                }
                
                // 记录当前度数的根节点
                degreeTable[degree] = current;
                
                // 移动到下一个根节点
                current = next;
                
                // 检查是否已经遍历完所有根节点
                if (current == start) {
                    isVisited = true;
                }
            } while (!isVisited);
            
            // 重建根链表并找到新的最小节点
            minNode = null;
            
            for (int i = 0; i < maxDegree; i++) {
                if (degreeTable[i] != null) {
                    // 初始化根链表
                    if (minNode == null) {
                        minNode = degreeTable[i];
                        minNode.left = minNode;
                        minNode.right = minNode;
                    } else {
                        // 将节点添加到根链表
                        linkRootList(degreeTable[i], minNode);
                        
                        // 更新最小节点
                        if (degreeTable[i].distance < minNode.distance) {
                            minNode = degreeTable[i];
                        }
                    }
                }
            }
        }
        
        private void cut(FibonacciHeapNode node, FibonacciHeapNode parent) {
            // 从父节点的子链表中移除node
            removeFromChildList(node);
            
            // 减少父节点的度数
            parent.degree--;
            
            // 将node添加到根链表
            node.parent = null;
            node.marked = false;
            linkRootList(node, minNode);
        }
        
        private void cascadingCut(FibonacciHeapNode node) {
            FibonacciHeapNode parent = node.parent;
            
            if (parent != null) {
                if (!node.marked) {
                    // 如果节点未被标记，标记它
                    node.marked = true;
                } else {
                    // 如果节点已被标记，进行剪切并继续级联
                    cut(node, parent);
                    cascadingCut(parent);
                }
            }
        }
    }
    
    /**
     * 使用斐波那契堆优化的Dijkstra算法
     * @param graph 邻接表表示的图
     * @param start 起始节点
     * @return 从起始节点到各节点的最短距离数组
     */
    public int[] dijkstra(List<List<Edge>> graph, int start) {
        int n = graph.size();
        int[] dist = new int[n];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[start] = 0;
        
        // 创建斐波那契堆
        FibonacciHeap fibHeap = new FibonacciHeap();
        FibonacciHeapNode[] nodes = new FibonacciHeapNode[n];
        
        // 插入所有节点到斐波那契堆
        for (int i = 0; i < n; i++) {
            nodes[i] = fibHeap.insert(i, dist[i]);
        }
        
        // Dijkstra算法主循环
        while (!fibHeap.isEmpty()) {
            // 提取距离最小的节点
            FibonacciHeapNode minNode = fibHeap.extractMin();
            int u = minNode.vertex;
            
            // 遍历u的所有邻居
            for (Edge edge : graph.get(u)) {
                int v = edge.to;
                int weight = edge.weight;
                
                // 松弛操作
                if (dist[u] != Integer.MAX_VALUE && dist[u] + weight < dist[v]) {
                    dist[v] = dist[u] + weight;
                    // 减小节点v的距离值
                    fibHeap.decreaseKey(nodes[v], dist[v]);
                }
            }
        }
        
        return dist;
    }
    
    /**
     * 使用标准优先队列的Dijkstra算法（用于对比）
     * @param graph 邻接表表示的图
     * @param start 起始节点
     * @return 从起始节点到各节点的最短距离数组
     */
    public int[] dijkstraWithPriorityQueue(List<List<Edge>> graph, int start) {
        int n = graph.size();
        int[] dist = new int[n];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[start] = 0;
        
        // 使用优先队列（最小堆）
        PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[1] - b[1]); // [vertex, distance]
        pq.offer(new int[]{start, 0});
        
        while (!pq.isEmpty()) {
            int[] current = pq.poll();
            int u = current[0];
            int d = current[1];
            
            // 如果当前距离大于已知最短距离，跳过
            if (d > dist[u]) {
                continue;
            }
            
            // 遍历u的所有邻居
            for (Edge edge : graph.get(u)) {
                int v = edge.to;
                int weight = edge.weight;
                
                // 松弛操作
                if (dist[u] != Integer.MAX_VALUE && dist[u] + weight < dist[v]) {
                    dist[v] = dist[u] + weight;
                    pq.offer(new int[]{v, dist[v]});
                }
            }
        }
        
        return dist;
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) {
        DijkstraWithFibonacciHeap solution = new DijkstraWithFibonacciHeap();
        
        // 创建测试图
        //     10
        // (0)----->(1)
        //  |        |
        //  |5       |1
        //  |        |
        //  v  3     v
        // (2)----->(3)
        //  |        |
        //  |2       |4
        //  |        |
        //  v        v
        // (4)<-----(5)
        //       6
        
        int n = 6;
        List<List<Edge>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }
        
        // 添加边
        graph.get(0).add(new Edge(1, 10));
        graph.get(0).add(new Edge(2, 5));
        graph.get(1).add(new Edge(3, 1));
        graph.get(2).add(new Edge(3, 3));
        graph.get(2).add(new Edge(4, 2));
        graph.get(3).add(new Edge(5, 4));
        graph.get(5).add(new Edge(4, 6));
        
        System.out.println("=== 测试Dijkstra算法 ===");
        System.out.println("图的邻接表表示:");
        for (int i = 0; i < n; i++) {
            System.out.print("节点 " + i + ": ");
            for (Edge edge : graph.get(i)) {
                System.out.print("(" + edge.to + ", " + edge.weight + ") ");
            }
            System.out.println();
        }
        
        int start = 0;
        System.out.println("\n从节点 " + start + " 开始的最短路径:");
        
        // 使用斐波那契堆的Dijkstra算法
        int[] dist1 = solution.dijkstra(graph, start);
        System.out.println("斐波那契堆优化结果: " + Arrays.toString(dist1));
        
        // 使用标准优先队列的Dijkstra算法
        int[] dist2 = solution.dijkstraWithPriorityQueue(graph, start);
        System.out.println("标准优先队列结果: " + Arrays.toString(dist2));
        
        // 验证结果一致性
        System.out.println("结果一致性: " + Arrays.equals(dist1, dist2));
    }
    
    /**
     * 性能测试
     */
    public static void performanceTest() {
        System.out.println("\n=== 性能测试 ===");
        
        // 创建随机图
        int n = 1000;
        int m = 5000;
        List<List<Edge>> graph = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            graph.add(new ArrayList<>());
        }
        
        Random random = new Random(42); // 固定种子以确保可重复性
        for (int i = 0; i < m; i++) {
            int u = random.nextInt(n);
            int v = random.nextInt(n);
            int weight = random.nextInt(100) + 1; // 1-100的权重
            graph.get(u).add(new Edge(v, weight));
        }
        
        DijkstraWithFibonacciHeap solution = new DijkstraWithFibonacciHeap();
        int start = 0;
        
        // 测试斐波那契堆优化的Dijkstra算法
        long startTime = System.currentTimeMillis();
        int[] dist1 = solution.dijkstra(graph, start);
        long time1 = System.currentTimeMillis() - startTime;
        
        // 测试标准优先队列的Dijkstra算法
        startTime = System.currentTimeMillis();
        int[] dist2 = solution.dijkstraWithPriorityQueue(graph, start);
        long time2 = System.currentTimeMillis() - startTime;
        
        System.out.println("图的规模: " + n + " 个节点, " + m + " 条边");
        System.out.println("斐波那契堆优化Dijkstra算法耗时: " + time1 + " ms");
        System.out.println("标准优先队列Dijkstra算法耗时: " + time2 + " ms");
        System.out.println("性能提升: " + (double)time2 / time1 + "倍");
        
        // 验证结果一致性
        System.out.println("结果一致性: " + Arrays.equals(dist1, dist2));
    }
}