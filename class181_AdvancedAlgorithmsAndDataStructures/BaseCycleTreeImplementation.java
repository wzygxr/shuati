package class008_AdvancedAlgorithmsAndDataStructures.base_cycle_tree_problems;

import java.util.*;

/**
 * 基环树 (Base Cycle Tree) 实现
 * 
 * 基环树是一种特殊的图结构，它由一个环和若干棵以环上节点为根的树组成。
 * 每个节点恰好有一条入边，因此整个图由一个或多个环组成，每个环上可能挂着一些树。
 * 
 * 应用场景：
 * 1. 数据结构：函数式数据结构、持久化数据结构
 * 2. 图论算法：环检测、强连通分量
 * 3. 数学：置换群、循环结构
 * 
 * 算法思路：
 * 1. 检测图中的环
 * 2. 对环上的每个节点，构建以其为根的子树
 * 3. 分析环的性质和子树的性质
 * 
 * 时间复杂度：O(V + E)
 * 空间复杂度：O(V)
 */
public class BaseCycleTreeImplementation {
    
    static class BaseCycleTree {
        private int n; // 节点数
        private int[] parent; // 每个节点的父节点
        private boolean[] visited; // 访问标记
        private List<List<Integer>> children; // 每个节点的子节点
        private List<Integer> cycle; // 环上的节点
        private boolean[] inCycle; // 标记节点是否在环上
        
        public BaseCycleTree(int n, int[] parent) {
            this.n = n;
            this.parent = parent.clone();
            this.visited = new boolean[n];
            this.children = new ArrayList<>();
            this.inCycle = new boolean[n];
            
            for (int i = 0; i < n; i++) {
                children.add(new ArrayList<>());
            }
            
            // 构建子节点关系
            for (int i = 0; i < n; i++) {
                if (parent[i] != -1) {
                    children.get(parent[i]).add(i);
                }
            }
            
            // 检测环
            findCycle();
        }
        
        // 检测环
        private void findCycle() {
            cycle = new ArrayList<>();
            Arrays.fill(visited, false);
            Arrays.fill(inCycle, false);
            
            // 找到环的起始节点
            int start = -1;
            for (int i = 0; i < n; i++) {
                if (!visited[i]) {
                    List<Integer> path = new ArrayList<>();
                    if (dfs(i, path)) {
                        start = i;
                        break;
                    }
                }
            }
            
            if (start == -1) return; // 没有环
            
            // 重新遍历以找到完整的环
            Arrays.fill(visited, false);
            List<Integer> path = new ArrayList<>();
            findCyclePath(start, path);
            
            // 标记环上的节点
            cycle = path;
            for (int node : cycle) {
                inCycle[node] = true;
            }
        }
        
        // DFS检测环
        private boolean dfs(int node, List<Integer> path) {
            if (visited[node]) {
                // 找到环的起点
                path.add(node);
                return true;
            }
            
            visited[node] = true;
            path.add(node);
            
            if (parent[node] != -1 && dfs(parent[node], path)) {
                return true;
            }
            
            path.remove(path.size() - 1);
            return false;
        }
        
        // 找到环的路径
        private boolean findCyclePath(int node, List<Integer> path) {
            if (visited[node]) {
                // 找到环的起点，截取环的部分
                int startIndex = path.indexOf(node);
                if (startIndex != -1) {
                    List<Integer> cyclePath = new ArrayList<>();
                    for (int i = startIndex; i < path.size(); i++) {
                        cyclePath.add(path.get(i));
                    }
                    path.clear();
                    path.addAll(cyclePath);
                    return true;
                }
                return false;
            }
            
            visited[node] = true;
            path.add(node);
            
            if (parent[node] != -1 && findCyclePath(parent[node], path)) {
                return true;
            }
            
            path.remove(path.size() - 1);
            return false;
        }
        
        // 获取环上的节点
        public List<Integer> getCycle() {
            return new ArrayList<>(cycle);
        }
        
        // 检查节点是否在环上
        public boolean isInCycle(int node) {
            return inCycle[node];
        }
        
        // 获取以指定节点为根的子树大小
        public int getSubtreeSize(int root) {
            return getSubtreeSizeHelper(root);
        }
        
        private int getSubtreeSizeHelper(int node) {
            int size = 1; // 包括节点本身
            for (int child : children.get(node)) {
                if (!inCycle[child]) { // 只计算不在环上的子节点
                    size += getSubtreeSizeHelper(child);
                }
            }
            return size;
        }
        
        // 获取环的长度
        public int getCycleLength() {
            return cycle.size();
        }
        
        // 获取所有子树的大小
        public Map<Integer, Integer> getAllSubtreeSizes() {
            Map<Integer, Integer> sizes = new HashMap<>();
            for (int node : cycle) {
                sizes.put(node, getSubtreeSize(node));
            }
            return sizes;
        }
        
        // 打印基环树结构
        public void printStructure() {
            System.out.println("Base Cycle Tree Structure:");
            System.out.println("Cycle: " + cycle);
            System.out.println("Cycle Length: " + getCycleLength());
            
            System.out.println("Subtree Sizes:");
            Map<Integer, Integer> sizes = getAllSubtreeSizes();
            for (Map.Entry<Integer, Integer> entry : sizes.entrySet()) {
                System.out.println("  Node " + entry.getKey() + ": " + entry.getValue());
            }
            
            System.out.println("Children Relationships:");
            for (int i = 0; i < n; i++) {
                if (!children.get(i).isEmpty()) {
                    System.out.println("  Node " + i + " -> " + children.get(i));
                }
            }
        }
    }
    
    // 测试方法
    public static void main(String[] args) {
        // 测试用例1：简单的基环树
        System.out.println("测试用例1: 简单基环树");
        // 节点: 0 1 2 3 4
        // 父节点: 1 2 0 4 1
        // 结构: 0->1->2->0 (环), 4->1, 3->4
        int[] parent1 = {1, 2, 0, 4, 1};
        BaseCycleTree bct1 = new BaseCycleTree(5, parent1);
        bct1.printStructure();
        System.out.println();
        
        // 测试用例2：单个环
        System.out.println("测试用例2: 单个环");
        // 节点: 0 1 2
        // 父节点: 1 2 0
        // 结构: 0->1->2->0 (环)
        int[] parent2 = {1, 2, 0};
        BaseCycleTree bct2 = new BaseCycleTree(3, parent2);
        bct2.printStructure();
        System.out.println();
        
        // 测试用例3：复杂基环树
        System.out.println("测试用例3: 复杂基环树");
        // 节点: 0 1 2 3 4 5 6
        // 父节点: 1 2 0 5 3 2 4
        // 结构: 0->1->2->0 (环), 3->5->2, 4->3, 6->4
        int[] parent3 = {1, 2, 0, 5, 3, 2, 4};
        BaseCycleTree bct3 = new BaseCycleTree(7, parent3);
        bct3.printStructure();
        System.out.println();
        
        // 测试用例4：多个独立的环
        System.out.println("测试用例4: 多个独立结构");
        // 节点: 0 1 2 3 4
        // 父节点: 1 0 3 2 1
        // 结构: 0->1->0 (环), 2->3->2 (环), 4->1
        int[] parent4 = {1, 0, 3, 2, 1};
        BaseCycleTree bct4 = new BaseCycleTree(5, parent4);
        bct4.printStructure();
    }
}