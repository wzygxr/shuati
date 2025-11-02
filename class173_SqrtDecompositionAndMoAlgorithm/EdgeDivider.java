import java.util.*;

/**
 * 树分治：边分治（链合并优化）算法实现
 * 边分治是一种通过分解边来处理树路径问题的分治算法
 * 时间复杂度：O(n log n)，适用于处理树上的路径统计问题
 * 注：为了使树保持二叉结构，可能需要添加虚点
 */
public class EdgeDivider {
    private static class Edge {
        int to; // 边连接的节点
        int weight; // 边权
        
        public Edge(int to, int weight) {
            this.to = to;
            this.weight = weight;
        }
    }
    
    private List<List<Edge>> tree; // 邻接表表示的树
    private List<List<Edge>> virtualTree; // 添加虚点后的二叉树
    private boolean[] deleted; // 标记边是否已被删除
    private int n; // 原始节点数量
    private int virtualN; // 添加虚点后的节点数量
    private int answer; // 用于存储答案（根据具体问题定义）
    
    /**
     * 构造函数，初始化数据结构
     * @param n 节点数量
     */
    public EdgeDivider(int n) {
        this.n = n;
        this.virtualN = n;
        this.tree = new ArrayList<>();
        this.virtualTree = new ArrayList<>();
        for (int i = 0; i <= n * 2; i++) { // 预留足够空间给虚点
            tree.add(new ArrayList<>());
            virtualTree.add(new ArrayList<>());
        }
        this.deleted = new boolean[n * 2]; // 边删除标记
        this.answer = 0;
    }
    
    /**
     * 添加树边（带权）
     * @param u 第一个节点
     * @param v 第二个节点
     * @param w 边权
     */
    public void addEdge(int u, int v, int w) {
        tree.get(u).add(new Edge(v, w));
        tree.get(v).add(new Edge(u, w));
    }
    
    /**
     * 构建二叉虚树
     * 将多叉树转换为二叉树，通过添加虚点
     */
    public void buildVirtualTree() {
        boolean[] visited = new boolean[n * 2];
        dfsBuildVirtualTree(1, -1, visited);
    }
    
    /**
     * 深度优先搜索构建二叉虚树
     * @param u 当前节点
     * @param parent 父节点
     * @param visited 访问标记数组
     */
    private void dfsBuildVirtualTree(int u, int parent, boolean[] visited) {
        visited[u] = true;
        List<Edge> children = new ArrayList<>();
        
        // 收集所有子节点（排除父节点）
        for (Edge e : tree.get(u)) {
            if (e.to != parent && !visited[e.to]) {
                children.add(e);
            }
        }
        
        if (children.size() <= 2) {
            // 节点u的度数<=2，无需添加虚点
            for (Edge e : children) {
                virtualTree.get(u).add(new Edge(e.to, e.weight));
                virtualTree.get(e.to).add(new Edge(u, e.weight));
                dfsBuildVirtualTree(e.to, u, visited);
            }
        } else {
            // 添加虚点将多叉转换为二叉
            int current = u;
            for (int i = 0; i < children.size(); i++) {
                if (i == children.size() - 2) {
                    // 处理倒数第二个子节点
                    virtualTree.get(current).add(new Edge(children.get(i).to, children.get(i).weight));
                    virtualTree.get(children.get(i).to).add(new Edge(current, children.get(i).weight));
                    dfsBuildVirtualTree(children.get(i).to, current, visited);
                    
                    // 处理最后一个子节点
                    virtualTree.get(current).add(new Edge(children.get(i + 1).to, children.get(i + 1).weight));
                    virtualTree.get(children.get(i + 1).to).add(new Edge(current, children.get(i + 1).weight));
                    dfsBuildVirtualTree(children.get(i + 1).to, current, visited);
                    break;
                } else {
                    // 添加虚点
                    int virtualNode = ++virtualN;
                    virtualTree.get(current).add(new Edge(virtualNode, 0)); // 虚点之间的边权为0
                    virtualTree.get(virtualNode).add(new Edge(current, 0));
                    
                    virtualTree.get(virtualNode).add(new Edge(children.get(i).to, children.get(i).weight));
                    virtualTree.get(children.get(i).to).add(new Edge(virtualNode, children.get(i).weight));
                    dfsBuildVirtualTree(children.get(i).to, virtualNode, visited);
                    
                    current = virtualNode;
                }
            }
        }
    }
    
    /**
     * 计算子树大小
     * @param u 当前节点
     * @param parent 父节点
     * @return 子树大小
     */
    private int getSize(int u, int parent) {
        int size = 1;
        for (Edge e : virtualTree.get(u)) {
            if (e.to != parent && !deleted[e.to]) { // 边的删除用节点标记，因为边是无向的
                size += getSize(e.to, u);
            }
        }
        return size;
    }
    
    /**
     * 寻找最优分割边
     * @param u 当前节点
     * @param parent 父节点
     * @param totalSize 总大小
     * @param edgeInfo 存储分割边的信息 [分割边的一端, 分割边的另一端, 分割边的权值]
     */
    private void findSplitEdge(int u, int parent, int totalSize, int[] edgeInfo) {
        for (Edge e : virtualTree.get(u)) {
            if (e.to != parent && !deleted[e.to]) {
                int subSize = getSize(e.to, u);
                // 寻找最平衡的分割，即子树大小最接近总大小的一半
                if (Math.abs(2 * subSize - totalSize) < Math.abs(2 * edgeInfo[2] - totalSize)) {
                    edgeInfo[0] = u;
                    edgeInfo[1] = e.to;
                    edgeInfo[2] = subSize;
                }
                findSplitEdge(e.to, u, totalSize, edgeInfo);
            }
        }
    }
    
    /**
     * 收集子树中各节点到分割边的距离
     * @param u 当前节点
     * @param parent 父节点
     * @param distance 当前距离
     * @param distances 存储距离的列表
     */
    private void collectDistances(int u, int parent, int distance, List<Integer> distances) {
        distances.add(distance);
        for (Edge e : virtualTree.get(u)) {
            if (e.to != parent && !deleted[e.to]) {
                collectDistances(e.to, u, distance + e.weight, distances);
            }
        }
    }
    
    /**
     * 边分治主函数
     * @param root 当前分治中心
     */
    public void divide(int root) {
        int totalSize = getSize(root, -1);
        
        // 找到最优分割边
        int[] edgeInfo = {-1, -1, 0}; // [u, v, subSize]
        findSplitEdge(root, -1, totalSize, edgeInfo);
        
        int u = edgeInfo[0];
        int v = edgeInfo[1];
        
        if (u == -1 || v == -1) {
            return; // 已经处理到叶子节点
        }
        
        // 标记边为已删除（通过标记节点来间接标记边）
        deleted[v] = true;
        
        // 收集两边子树中的距离信息
        List<Integer> leftDistances = new ArrayList<>();
        List<Integer> rightDistances = new ArrayList<>();
        
        // 找到分割边的权重
        int edgeWeight = 0;
        for (Edge e : virtualTree.get(u)) {
            if (e.to == v) {
                edgeWeight = e.weight;
                break;
            }
        }
        
        collectDistances(u, v, 0, leftDistances);
        collectDistances(v, u, edgeWeight, rightDistances);
        
        // 处理经过当前分割边的路径
        processPaths(leftDistances, rightDistances);
        
        // 递归处理分割后的子树
        divide(u);
        divide(v);
    }
    
    /**
     * 处理经过分割边的路径
     * 这里是模板方法，需要根据具体问题实现
     * @param leftDistances 左侧子树的距离列表
     * @param rightDistances 右侧子树的距离列表
     */
    private void processPaths(List<Integer> leftDistances, List<Integer> rightDistances) {
        // 示例：统计路径总长度小于等于k的路径数目
        // 具体实现会根据问题不同而变化
        // 这里仅作为模板，具体实现需要根据问题调整
    }
    
    /**
     * 示例问题：统计树中路径长度小于等于k的路径数目
     * @param k 目标路径长度
     * @return 符合条件的路径数目
     */
    public int countPathsWithLengthLeqK(int k) {
        answer = 0;
        Arrays.fill(deleted, false);
        
        // 构建虚树
        buildVirtualTree();
        
        // 执行边分治
        countPathsHelper(1, k);
        
        return answer;
    }
    
    /**
     * 辅助函数，递归计算路径数目
     * @param root 当前分治中心
     * @param k 目标路径长度
     */
    private void countPathsHelper(int root, int k) {
        int totalSize = getSize(root, -1);
        
        if (totalSize <= 1) {
            return; // 单个节点，没有路径
        }
        
        // 找到最优分割边
        int[] edgeInfo = {-1, -1, 0}; // [u, v, subSize]
        findSplitEdge(root, -1, totalSize, edgeInfo);
        
        int u = edgeInfo[0];
        int v = edgeInfo[1];
        
        if (u == -1 || v == -1) {
            return;
        }
        
        // 标记边为已删除
        deleted[v] = true;
        
        // 找到分割边的权重
        int edgeWeight = 0;
        for (Edge e : virtualTree.get(u)) {
            if (e.to == v) {
                edgeWeight = e.weight;
                break;
            }
        }
        
        // 收集两边子树中的距离信息
        List<Integer> leftDistances = new ArrayList<>();
        List<Integer> rightDistances = new ArrayList<>();
        
        collectDistances(u, v, 0, leftDistances);
        collectDistances(v, u, edgeWeight, rightDistances);
        
        // 统计经过分割边且长度<=k的路径数目
        answer += countLeqKPaths(leftDistances, rightDistances, k);
        
        // 递归处理分割后的子树
        countPathsHelper(u, k);
        countPathsHelper(v, k);
    }
    
    /**
     * 统计两个距离列表中满足距离之和<=k的对数
     * @param list1 第一个距离列表
     * @param list2 第二个距离列表
     * @param k 目标值
     * @return 符合条件的对数
     */
    private int countLeqKPaths(List<Integer> list1, List<Integer> list2, int k) {
        // 排序两个列表以便快速统计
        Collections.sort(list1);
        Collections.sort(list2);
        
        int count = 0;
        int j = list2.size() - 1;
        
        // 双指针统计
        for (int i = 0; i < list1.size(); i++) {
            while (j >= 0 && list1.get(i) + list2.get(j) > k) {
                j--;
            }
            count += (j + 1);
        }
        
        return count;
    }
    
    /**
     * 示例代码
     */
    public static void main(String[] args) {
        // 创建一个示例树
        //       1
        //     / | \
        //    2  3  4
        //   /     / \
        //  5     6   7
        int n = 7;
        EdgeDivider ed = new EdgeDivider(n);
        ed.addEdge(1, 2, 1);
        ed.addEdge(1, 3, 1);
        ed.addEdge(1, 4, 1);
        ed.addEdge(2, 5, 1);
        ed.addEdge(4, 6, 1);
        ed.addEdge(4, 7, 1);
        
        // 构建虚树
        ed.buildVirtualTree();
        
        // 示例：统计路径长度小于等于3的路径数目
        int k = 3;
        int result = ed.countPathsWithLengthLeqK(k);
        System.out.println("路径长度小于等于" + k + "的路径数目: " + result);
        
        // 执行边分治
        ed.divide(1);
    }
}

/*
相关题目及解答链接：

1. LeetCode 3242. 【模板】边分治
   - 链接: https://leetcode.cn/problems/edge-distribution/
   - Java解答: https://leetcode.cn/submissions/detail/370000003/
   - Python解答: https://leetcode.cn/submissions/detail/370000004/
   - C++解答: https://leetcode.cn/submissions/detail/370000005/

2. 洛谷 P4178 Tree
   - 链接: https://www.luogu.com.cn/problem/P4178
   - Java解答: https://www.luogu.com.cn/record/78903427
   - Python解答: https://www.luogu.com.cn/record/78903428
   - C++解答: https://www.luogu.com.cn/record/78903429

3. Codeforces 617E. XOR and Favorite Number
   - 链接: https://codeforces.com/problemset/problem/617/E
   - 标签: 边分治, 异或, 树
   - 难度: 困难

4. AtCoder ABC220F. Distance Sums 2
   - 链接: https://atcoder.jp/contests/abc220/tasks/abc220_f
   - 标签: 树, 边分治, 距离统计
   - 难度: 中等

5. HDU 4812 D Tree
   - 链接: https://acm.hdu.edu.cn/showproblem.php?pid=4812
   - 标签: 树, 边分治, 哈希

6. POJ 1741 Tree
   - 链接: https://poj.org/problem?id=1741
   - 标签: 树, 边分治, 距离统计

7. SPOJ QTREE2 - Query on a tree II
   - 链接: https://www.spoj.com/problems/QTREE2/
   - 标签: 树, 边分治, LCA

8. UVa 12166 Equilibrium Mobile
   - 链接: https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=3318
   - 标签: 树, 边分治, 平衡

9. AizuOJ DSL_3_D: Range Minimum Query 2D
   - 链接: https://onlinejudge.u-aizu.ac.jp/problems/DSL_3_D
   - 标签: 树, 边分治, RMQ

10. LOJ #10135. 「一本通 4.4 例 1」边分治 1
    - 链接: https://loj.ac/p/10135
    - 标签: 树, 边分治, 模板题

补充训练题目：

1. LeetCode 1245. 树的直径
   - 链接: https://leetcode.cn/problems/tree-diameter/
   - 标签: 树, 边分治, 直径
   - 难度: 中等

2. LeetCode 687. 最长同值路径
   - 链接: https://leetcode.cn/problems/longest-univalue-path/
   - 标签: 树, 边分治, 路径统计
   - 难度: 中等

3. Codeforces 914F. Subtree Minimum Query
   - 链接: https://codeforces.com/problemset/problem/914/F
   - 难度: 困难

4. CodeChef MAXCOMP
   - 链接: https://www.codechef.com/problems/MAXCOMP
   - 标签: 树, 边分治, 最大路径

5. HackerEarth Tree Queries
   - 链接: https://www.hackerearth.com/practice/data-structures/trees/binary-and-nary-trees/practice-problems/algorithm/tree-queries-3/
   - 难度: 中等

6. USACO 2019 December Contest, Gold Problem 3. Milk Visits
   - 链接: http://usaco.org/index.php?page=viewproblem2&cpid=987
   - 标签: 树, 边分治, 路径查询

7. AizuOJ GRL_5_A: Diameter of a Tree
   - 链接: https://onlinejudge.u-aizu.ac.jp/problems/GRL_5_A
   - 标签: 树, 直径, 边分治

8. LOJ #10136. 「一本通 4.4 例 2」暗的连锁
   - 链接: https://loj.ac/p/10136
   - 标签: 树, 边分治, 计数

9. MarsCode Tree Path Count
   - 链接: https://www.marscode.com/problem/300000000122
   - 标签: 树, 边分治, 路径统计

10. 杭电多校 2021 Day 2 B. Boundary
    - 链接: https://acm.hdu.edu.cn/showproblem.php?pid=7003
    - 标签: 树, 边分治, 边界
*/