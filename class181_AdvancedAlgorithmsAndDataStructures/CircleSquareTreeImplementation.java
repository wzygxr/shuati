package class008_AdvancedAlgorithmsAndDataStructures.circle_square_tree_problems;

import java.util.*;

/**
 * 圆方树 (Circle Square Tree) 实现
 * 
 * 圆方树是一种将无向图转化为树结构的方法，主要用于处理仙人掌图（每条边最多属于一个环的图）。
 * 在圆方树中：
 * - 圆点：原图中的节点
 * - 方点：原图中的环
 * 
 * 应用场景：
 * 1. 仙人掌图算法：最短路径、环相关问题
 * 2. 图论问题：点双连通分量、割点
 * 3. 竞赛算法：处理特殊图结构
 * 
 * 算法思路：
 * 1. 使用DFS找出图中的点双连通分量
 * 2. 对于每个点双连通分量：
 *    - 如果是单个边，创建圆点-圆点的连接
 *    - 如果包含多个节点（形成环），创建一个方点代表这个环
 * 3. 将圆点和方点连接形成树结构
 * 
 * 时间复杂度：O(V + E)
 * 空间复杂度：O(V + E)
 */
public class CircleSquareTreeImplementation {
    
    static class CircleSquareTree {
        private int n; // 原图节点数
        private List<List<Integer>> graph; // 原图的邻接表
        private List<List<Integer>> tree; // 圆方树的邻接表
        private int treeNodeCount; // 圆方树节点数（包括圆点和方点）
        private boolean[] visited; // DFS访问标记
        private int[] dfn; // DFS时间戳
        private int[] low; // 最小时间戳
        private Stack<Integer> stack; // DFS栈
        private int dfsTime; // DFS时间
        private Map<Integer, List<Integer>> biconnectedComponents; // 点双连通分量
        private int bccCount; // 点双连通分量数量
        
        public CircleSquareTree(int n) {
            this.n = n;
            this.graph = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                graph.add(new ArrayList<>());
            }
            
            this.visited = new boolean[n];
            this.dfn = new int[n];
            this.low = new int[n];
            this.stack = new Stack<>();
            this.dfsTime = 0;
            this.biconnectedComponents = new HashMap<>();
            this.bccCount = 0;
        }
        
        // 添加边
        public void addEdge(int u, int v) {
            graph.get(u).add(v);
            graph.get(v).add(u);
        }
        
        // 构建圆方树
        public void buildCircleSquareTree() {
            // 初始化
            Arrays.fill(visited, false);
            Arrays.fill(dfn, -1);
            Arrays.fill(low, -1);
            stack.clear();
            biconnectedComponents.clear();
            bccCount = 0;
            dfsTime = 0;
            
            // 找出所有点双连通分量
            for (int i = 0; i < n; i++) {
                if (dfn[i] == -1) {
                    tarjan(i, -1);
                }
            }
            
            // 构建圆方树
            buildTree();
        }
        
        // Tarjan算法找点双连通分量
        private void tarjan(int u, int parent) {
            dfn[u] = low[u] = ++dfsTime;
            visited[u] = true;
            stack.push(u);
            int children = 0;
            
            for (int v : graph.get(u)) {
                if (v == parent) continue;
                
                if (dfn[v] == -1) {
                    children++;
                    tarjan(v, u);
                    low[u] = Math.min(low[u], low[v]);
                    
                    // 发现点双连通分量
                    if (low[v] >= dfn[u]) {
                        List<Integer> bcc = new ArrayList<>();
                        int node;
                        do {
                            node = stack.pop();
                            bcc.add(node);
                        } while (node != v);
                        bcc.add(u); // 添加根节点
                        
                        biconnectedComponents.put(bccCount++, bcc);
                    }
                } else {
                    low[u] = Math.min(low[u], dfn[v]);
                }
            }
            
            // 根节点特殊情况
            if (parent == -1 && children == 0) {
                List<Integer> bcc = new ArrayList<>();
                bcc.add(u);
                biconnectedComponents.put(bccCount++, bcc);
            }
        }
        
        // 构建圆方树
        private void buildTree() {
            // 圆方树节点数 = 原图节点数 + 点双连通分量数
            treeNodeCount = n + bccCount;
            tree = new ArrayList<>();
            for (int i = 0; i < treeNodeCount; i++) {
                tree.add(new ArrayList<>());
            }
            
            // 为每个点双连通分量创建方点，并连接圆点
            for (int i = 0; i < bccCount; i++) {
                int squareNode = n + i; // 方点编号从n开始
                List<Integer> bcc = biconnectedComponents.get(i);
                
                // 连接方点和该分量中的所有圆点
                for (int circleNode : bcc) {
                    tree.get(squareNode).add(circleNode);
                    tree.get(circleNode).add(squareNode);
                }
            }
        }
        
        // 获取圆方树
        public List<List<Integer>> getCircleSquareTree() {
            return tree;
        }
        
        // 获取圆方树节点数
        public int getTreeNodeCount() {
            return treeNodeCount;
        }
        
        // 获取点双连通分量
        public Map<Integer, List<Integer>> getBiconnectedComponents() {
            return new HashMap<>(biconnectedComponents);
        }
        
        // 计算两点间在圆方树上的距离
        public int distance(int u, int v) {
            if (u == v) return 0;
            
            // BFS计算最短距离
            boolean[] visited = new boolean[treeNodeCount];
            Queue<Integer> queue = new LinkedList<>();
            Map<Integer, Integer> distances = new HashMap<>();
            
            queue.offer(u);
            visited[u] = true;
            distances.put(u, 0);
            
            while (!queue.isEmpty()) {
                int node = queue.poll();
                int dist = distances.get(node);
                
                for (int neighbor : tree.get(node)) {
                    if (!visited[neighbor]) {
                        if (neighbor == v) {
                            return dist + 1;
                        }
                        visited[neighbor] = true;
                        distances.put(neighbor, dist + 1);
                        queue.offer(neighbor);
                    }
                }
            }
            
            return -1; // 不连通
        }
        
        // 打印圆方树结构
        public void printStructure() {
            System.out.println("Circle-Square Tree Structure:");
            System.out.println("Original nodes: " + n);
            System.out.println("Biconnected components: " + bccCount);
            System.out.println("Tree nodes: " + treeNodeCount);
            
            System.out.println("Biconnected Components:");
            for (Map.Entry<Integer, List<Integer>> entry : biconnectedComponents.entrySet()) {
                System.out.println("  BCC " + entry.getKey() + ": " + entry.getValue());
            }
            
            System.out.println("Tree Edges:");
            for (int i = 0; i < treeNodeCount; i++) {
                if (!tree.get(i).isEmpty()) {
                    System.out.println("  Node " + i + " -> " + tree.get(i));
                }
            }
        }
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试用例1：简单的仙人掌图
        System.out.println("测试用例1: 简单仙人掌图");
        CircleSquareTree cst1 = new CircleSquareTree(5);
        
        // 构建图结构:
        // 0-1-2 (链)
        // 1-3-4 (链)
        // 1-2-3 (环)
        cst1.addEdge(0, 1);
        cst1.addEdge(1, 2);
        cst1.addEdge(1, 3);
        cst1.addEdge(2, 3);
        cst1.addEdge(3, 4);
        
        cst1.buildCircleSquareTree();
        cst1.printStructure();
        
        System.out.println("节点0和节点4在圆方树上的距离: " + cst1.distance(0, 4));
        System.out.println();
        
        // 测试用例2：树结构
        System.out.println("测试用例2: 树结构");
        CircleSquareTree cst2 = new CircleSquareTree(5);
        
        // 构建树结构:
        // 0-1-2
        // |   |
        // 3   4
        cst2.addEdge(0, 1);
        cst2.addEdge(0, 3);
        cst2.addEdge(1, 2);
        cst2.addEdge(2, 4);
        
        cst2.buildCircleSquareTree();
        cst2.printStructure();
        
        System.out.println("节点3和节点4在圆方树上的距离: " + cst2.distance(3, 4));
        System.out.println();
        
        // 测试用例3：复杂仙人掌图
        System.out.println("测试用例3: 复杂仙人掌图");
        CircleSquareTree cst3 = new CircleSquareTree(6);
        
        // 构建图结构:
        // 0-1-2 (链)
        // 0-2 (形成环)
        // 2-3-4 (链)
        // 2-4 (形成环)
        // 4-5 (链)
        cst3.addEdge(0, 1);
        cst3.addEdge(1, 2);
        cst3.addEdge(0, 2);
        cst3.addEdge(2, 3);
        cst3.addEdge(3, 4);
        cst3.addEdge(2, 4);
        cst3.addEdge(4, 5);
        
        cst3.buildCircleSquareTree();
        cst3.printStructure();
        
        System.out.println("节点0和节点5在圆方树上的距离: " + cst3.distance(0, 5));
    }
}