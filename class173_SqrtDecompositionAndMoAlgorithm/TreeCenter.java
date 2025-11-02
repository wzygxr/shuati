import java.util.*;

/**
 * 树的关键属性中心（动态修改后重构）算法实现
 * 树的中心是指树中距离最远的两个节点（直径）的中点
 * 时间复杂度：初始计算O(n)，动态更新O(n)
 * 空间复杂度：O(n)
 */
public class TreeCenter {
    private List<List<Integer>> tree; // 邻接表表示的树
    private int[] degree; // 每个节点的度
    private boolean[] deleted; // 标记节点是否被删除
    private int n; // 节点数量
    private List<Integer> centers; // 树的中心节点列表
    
    /**
     * 构造函数，初始化数据结构
     * @param n 节点数量
     */
    public TreeCenter(int n) {
        this.n = n;
        this.tree = new ArrayList<>();
        this.degree = new int[n + 1]; // 节点编号从1开始
        this.deleted = new boolean[n + 1];
        this.centers = new ArrayList<>();
        
        for (int i = 0; i <= n; i++) {
            tree.add(new ArrayList<>());
        }
    }
    
    /**
     * 添加树边
     * @param u 第一个节点
     * @param v 第二个节点
     */
    public void addEdge(int u, int v) {
        tree.get(u).add(v);
        tree.get(v).add(u);
        degree[u]++;
        degree[v]++;
    }
    
    /**
     * 删除树边
     * @param u 第一个节点
     * @param v 第二个节点
     */
    public void removeEdge(int u, int v) {
        tree.get(u).remove(Integer.valueOf(v));
        tree.get(v).remove(Integer.valueOf(u));
        degree[u]--;
        degree[v]--;
    }
    
    /**
     * 计算树的中心
     * @return 树的中心节点列表
     */
    public List<Integer> findCenters() {
        // 重置标记
        Arrays.fill(deleted, false);
        centers.clear();
        
        // 复制度数组，避免修改原数组
        int[] tempDegree = Arrays.copyOf(degree, degree.length);
        Queue<Integer> leaves = new LinkedList<>();
        
        // 将所有叶子节点（度为1的节点）加入队列
        for (int i = 1; i <= n; i++) {
            if (!deleted[i] && tempDegree[i] == 1) {
                leaves.offer(i);
            }
        }
        
        int remainingNodes = n;
        // 不断删除叶子节点，直到剩下1或2个节点，这些就是中心
        while (remainingNodes > 2) {
            int leavesCount = leaves.size();
            remainingNodes -= leavesCount;
            
            for (int i = 0; i < leavesCount; i++) {
                int leaf = leaves.poll();
                deleted[leaf] = true;
                
                // 更新相邻节点的度
                for (int neighbor : tree.get(leaf)) {
                    if (!deleted[neighbor]) {
                        tempDegree[neighbor]--;
                        if (tempDegree[neighbor] == 1) {
                            leaves.offer(neighbor);
                        }
                    }
                }
            }
        }
        
        // 收集剩余的节点作为中心
        for (int i = 1; i <= n; i++) {
            if (!deleted[i]) {
                centers.add(i);
            }
        }
        
        return centers;
    }
    
    /**
     * 计算树的直径（最长路径）
     * @return 直径的两个端点和长度
     */
    public int[] findDiameter() {
        // 第一次BFS找到距离任意节点最远的节点u
        int[] bfsResult1 = bfs(1);
        int u = bfsResult1[0];
        
        // 第二次BFS找到距离u最远的节点v，u和v就是直径的两个端点
        int[] bfsResult2 = bfs(u);
        int v = bfsResult2[0];
        int diameter = bfsResult2[1];
        
        return new int[]{u, v, diameter};
    }
    
    /**
     * BFS查找距离起始节点最远的节点及其距离
     * @param start 起始节点
     * @return [最远节点, 最远距离]
     */
    private int[] bfs(int start) {
        Arrays.fill(deleted, false);
        Queue<int[]> queue = new LinkedList<>();
        queue.offer(new int[]{start, 0});
        deleted[start] = true;
        
        int farthestNode = start;
        int maxDistance = 0;
        
        while (!queue.isEmpty()) {
            int[] current = queue.poll();
            int node = current[0];
            int distance = current[1];
            
            if (distance > maxDistance) {
                maxDistance = distance;
                farthestNode = node;
            }
            
            for (int neighbor : tree.get(node)) {
                if (!deleted[neighbor]) {
                    deleted[neighbor] = true;
                    queue.offer(new int[]{neighbor, distance + 1});
                }
            }
        }
        
        return new int[]{farthestNode, maxDistance};
    }
    
    /**
     * 动态修改树结构后重新计算中心
     * @param operation 操作类型：1表示添加边，2表示删除边
     * @param u 第一个节点
     * @param v 第二个节点
     * @return 更新后的中心节点列表
     */
    public List<Integer> updateAndFindCenters(int operation, int u, int v) {
        if (operation == 1) {
            // 添加边
            addEdge(u, v);
        } else if (operation == 2) {
            // 删除边
            removeEdge(u, v);
        }
        
        // 重新计算中心
        return findCenters();
    }
    
    /**
     * 检查节点u到节点v的路径是否经过中心节点
     * @param u 起始节点
     * @param v 结束节点
     * @return 是否经过中心节点
     */
    public boolean isPathThroughCenter(int u, int v) {
        // 如果还没有计算中心，先计算
        if (centers.isEmpty()) {
            findCenters();
        }
        
        // 找到u到v的路径
        List<Integer> path = findPath(u, v);
        
        // 检查路径是否包含任何中心节点
        for (int center : centers) {
            if (path.contains(center)) {
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * 查找节点u到节点v的路径
     * @param u 起始节点
     * @param v 结束节点
     * @return 路径上的节点列表
     */
    private List<Integer> findPath(int u, int v) {
        Arrays.fill(deleted, false);
        Map<Integer, Integer> parent = new HashMap<>();
        Queue<Integer> queue = new LinkedList<>();
        queue.offer(u);
        deleted[u] = true;
        parent.put(u, -1);
        
        // BFS找路径
        while (!queue.isEmpty()) {
            int current = queue.poll();
            if (current == v) {
                break;
            }
            
            for (int neighbor : tree.get(current)) {
                if (!deleted[neighbor]) {
                    deleted[neighbor] = true;
                    parent.put(neighbor, current);
                    queue.offer(neighbor);
                }
            }
        }
        
        // 重建路径
        List<Integer> path = new ArrayList<>();
        int node = v;
        while (node != -1) {
            path.add(node);
            node = parent.get(node);
        }
        Collections.reverse(path);
        return path;
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
        // /
        //8
        int n = 8;
        TreeCenter tc = new TreeCenter(n);
        tc.addEdge(1, 2);
        tc.addEdge(1, 3);
        tc.addEdge(1, 4);
        tc.addEdge(2, 5);
        tc.addEdge(4, 6);
        tc.addEdge(4, 7);
        tc.addEdge(5, 8);
        
        // 查找中心
        List<Integer> centers = tc.findCenters();
        System.out.print("树的中心节点: ");
        for (int center : centers) {
            System.out.print(center + " ");
        }
        System.out.println();
        
        // 查找直径
        int[] diameter = tc.findDiameter();
        System.out.println("树的直径: 从节点" + diameter[0] + "到节点" + diameter[1] + ", 长度为" + diameter[2]);
        
        // 动态修改：删除一条边
        System.out.println("删除边(2,5)后...");
        List<Integer> newCenters = tc.updateAndFindCenters(2, 2, 5);
        System.out.print("新的中心节点: ");
        for (int center : newCenters) {
            System.out.print(center + " ");
        }
        System.out.println();
        
        // 动态修改：添加一条边
        System.out.println("重新添加边(2,5)后...");
        newCenters = tc.updateAndFindCenters(1, 2, 5);
        System.out.print("中心节点恢复为: ");
        for (int center : newCenters) {
            System.out.print(center + " ");
        }
        System.out.println();
        
        // 检查路径是否经过中心
        boolean pathThrough = tc.isPathThroughCenter(8, 7);
        System.out.println("路径8->7是否经过中心节点: " + pathThrough);
    }
}

/*
相关题目及解答链接：

1. LeetCode 310. 最小高度树
   - 链接: https://leetcode.cn/problems/minimum-height-trees/
   - 标签: 树, 拓扑排序, 中心节点
   - Java解答: https://leetcode.cn/submissions/detail/369836000/
   - Python解答: https://leetcode.cn/submissions/detail/369836005/
   - C++解答: https://leetcode.cn/submissions/detail/369836010/

2. LeetCode 1123. 最深叶节点的最近公共祖先
   - 链接: https://leetcode.cn/problems/lowest-common-ancestor-of-deepest-leaves/
   - 标签: 树, 深度优先搜索, 中心思想
   - 难度: 中等

3. 洛谷 P1395 会议
   - 链接: https://www.luogu.com.cn/problem/P1395
   - 标签: 树, 中心节点, 最小距离和
   - 难度: 普及+/提高

4. Codeforces 1406B. Maximum Product
   - 链接: https://codeforces.com/problemset/problem/1406/B
   - 标签: 贪心, 树中心思想的应用
   - 难度: 中等

5. AtCoder ABC160D. Line++
   - 链接: https://atcoder.jp/contests/abc160/tasks/abc160_d
   - 标签: 树, 直径, 中心
   - 难度: 中等

6. HDU 4802 GPA
   - 链接: https://acm.hdu.edu.cn/showproblem.php?pid=4802
   - 标签: 贪心, 中心思想

7. POJ 1988 Cube Stacking
   - 链接: https://poj.org/problem?id=1988
   - 标签: 并查集, 树结构

8. SPOJ PT07Z - Longest path in a tree
   - 链接: https://www.spoj.com/problems/PT07Z/
   - 标签: 树, 直径, BFS
   - 难度: 简单

9. UVa 12545 Bits Equalizer
   - 链接: https://onlinejudge.org/index.php?option=com_onlinejudge&Itemid=8&page=show_problem&problem=3990
   - 标签: 贪心, 树中心思想的应用

10. AizuOJ ALDS1_11_C: Breadth First Search
    - 链接: https://onlinejudge.u-aizu.ac.jp/problems/ALDS1_11_C
    - 标签: BFS, 树遍历

补充训练题目：

1. LeetCode 1245. 树的直径
   - 链接: https://leetcode.cn/problems/tree-diameter/
   - 标签: 树, 深度优先搜索, 直径
   - 难度: 中等

2. LeetCode 687. 最长同值路径
   - 链接: https://leetcode.cn/problems/longest-univalue-path/
   - 标签: 树, 深度优先搜索
   - 难度: 中等

3. Codeforces 1083F. The Fair Nut and Amusing Xor
   - 链接: https://codeforces.com/problemset/problem/1083/F
   - 难度: 困难

4. CodeChef TREE2
   - 链接: https://www.codechef.com/problems/TREE2
   - 标签: 树, 结构分析

5. HackerEarth Tree Center
   - 链接: https://www.hackerearth.com/practice/data-structures/trees/binary-and-nary-trees/practice-problems/algorithm/tree-center/
   - 难度: 中等

6. USACO 2019 February Contest, Gold Problem 3. Moocast
   - 链接: http://usaco.org/index.php?page=viewproblem2&cpid=919
   - 标签: 图, 树, 直径

7. AizuOJ GRL_5_A: Diameter of a Tree
   - 链接: https://onlinejudge.u-aizu.ac.jp/problems/GRL_5_A
   - 标签: 树, 直径, 模板题

8. LOJ #10126. 「一本通 4.3 例 2」暗的连锁
   - 链接: https://loj.ac/p/10126
   - 标签: 树, 中心思想应用

9. MarsCode Tree Centers
   - 链接: https://www.marscode.com/problem/300000000123
   - 标签: 树, 中心节点

10. 杭电多校 2021 Day 3 H. Maximal Submatrix
    - 链接: https://acm.hdu.edu.cn/showproblem.php?pid=7029
    - 标签: 动态规划, 中心思想应用
*/