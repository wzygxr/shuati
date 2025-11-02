// 线段树分治算法的Java实现
// 包含可撤销并查集、动态图连通性和二分图判定问题的实现

import java.util.*;

// 可撤销并查集类
class RollbackDSU {
    private int[] father;       // 父节点数组
    private int[] rank;         // 秩数组（树高上界）
    private Stack<Integer> historyFatherNode;  // 父节点操作历史（节点）
    private Stack<Integer> historyFatherValue; // 父节点操作历史（值）
    private Stack<Integer> historyRankNode;    // 秩操作历史（节点）
    private Stack<Integer> historyRankValue;   // 秩操作历史（值）
    private int version;        // 当前版本号

    /**
     * 构造函数
     * @param size 节点数量
     * 时间复杂度：O(n)
     */
    public RollbackDSU(int size) {
        father = new int[size];
        rank = new int[size];
        Arrays.fill(rank, 1);
        // 初始化，每个节点的父节点是自身
        for (int i = 0; i < size; ++i) {
            father[i] = i;
        }
        historyFatherNode = new Stack<>();
        historyFatherValue = new Stack<>();
        historyRankNode = new Stack<>();
        historyRankValue = new Stack<>();
        version = 0;
    }

    /**
     * 查找节点的根节点
     * @param x 要查找的节点
     * @return 节点x的根节点
     * 时间复杂度：O(log n) - 由于没有路径压缩
     */
    public int find(int x) {
        while (x != father[x]) {
            x = father[x];
        }
        return x;
    }

    /**
     * 合并两个集合
     * @param x 第一个节点
     * @param y 第二个节点
     * @return 如果x和y原来不在同一个集合中则返回true，否则返回false
     * 时间复杂度：O(log n)
     */
    public boolean unite(int x, int y) {
        int fx = find(x);
        int fy = find(y);

        if (fx == fy) {
            return false;  // 已经在同一个集合中
        }

        // 按秩合并：将秩较小的树合并到秩较大的树上
        if (rank[fx] < rank[fy]) {
            int temp = fx;
            fx = fy;
            fy = temp;
        }

        // 记录操作前的状态，用于回滚
        historyFatherNode.push(fy);
        historyFatherValue.push(father[fy]);
        historyRankNode.push(fx);
        historyRankValue.push(rank[fx]);
        version++;

        // 执行合并操作
        father[fy] = fx;

        // 如果两棵树的秩相等，则合并后树的秩加1
        if (rank[fx] == rank[fy]) {
            rank[fx]++;
        }

        return true;
    }

    /**
     * 回滚到指定版本
     * @param targetVersion 要回滚到的版本号
     * 时间复杂度：O(当前版本 - 目标版本)
     */
    public void rollback(int targetVersion) {
        while (version > targetVersion) {
            // 恢复父节点状态
            int fy = historyFatherNode.pop();
            int prevFather = historyFatherValue.pop();
            father[fy] = prevFather;

            // 恢复秩状态
            int fx = historyRankNode.pop();
            int prevRank = historyRankValue.pop();
            rank[fx] = prevRank;

            version--;
        }
    }

    /**
     * 获取当前版本号
     * @return 当前版本号
     * 时间复杂度：O(1)
     */
    public int getVersion() {
        return version;
    }
}

// 线段树分治类
class SegmentTreeDivideConquer {
    private int maxTime;                   // 最大时间范围
    private List<List<int[]>> operations;  // 存储每个线段树节点上的操作

    /**
     * 构造函数
     * @param maxTime 最大时间范围
     * 时间复杂度：O(Q)
     */
    public SegmentTreeDivideConquer(int maxTime) {
        this.maxTime = maxTime;
        // 为线段树分配足够的空间，4倍大小通常足够
        operations = new ArrayList<>(4 * (maxTime + 1));
        for (int i = 0; i < 4 * (maxTime + 1); ++i) {
            operations.add(new ArrayList<>());
        }
    }

    /**
     * 线段树更新操作，将边(u,v)添加到覆盖区间[l,r]的节点中
     * @param node 当前节点编号
     * @param nodeL 当前节点对应的左边界
     * @param nodeR 当前节点对应的右边界
     * @param l 边的存在起始时间
     * @param r 边的存在结束时间
     * @param u 边的第一个端点
     * @param v 边的第二个端点
     * 时间复杂度：O(log Q)，其中Q是最大时间范围
     */
    private void update(int node, int nodeL, int nodeR, int l, int r, int u, int v) {
        if (nodeR < l || nodeL > r) {
            return;  // 当前节点的区间与操作区间不相交
        }

        if (l <= nodeL && nodeR <= r) {
            // 当前节点的区间完全包含在操作区间内，直接添加操作
            operations.get(node).add(new int[]{u, v});
            return;
        }

        // 递归处理左右子节点
        int mid = (nodeL + nodeR) / 2;
        update(2 * node, nodeL, mid, l, r, u, v);
        update(2 * node + 1, mid + 1, nodeR, l, r, u, v);
    }

    /**
     * 添加边操作
     * @param l 边的存在起始时间
     * @param r 边的存在结束时间
     * @param u 边的第一个端点
     * @param v 边的第二个端点
     * 时间复杂度：O(log Q)
     */
    public void addEdge(int l, int r, int u, int v) {
        update(1, 1, maxTime, l, r, u, v);
    }

    /**
     * 获取线段树节点上的操作
     * @param node 线段树节点编号
     * @return 该节点上的所有操作
     * 时间复杂度：O(1)
     */
    public List<int[]> getOperations(int node) {
        return operations.get(node);
    }

    /**
     * 获取最大时间范围
     * @return 最大时间范围
     * 时间复杂度：O(1)
     */
    public int getMaxTime() {
        return maxTime;
    }
}

public class Main {
    // 动态图连通性问题解决方案
    // 问题描述：处理多个时间点的边添加/删除操作，并回答连通性查询
    // 时间复杂度：O(m log n log Q + q α(n))，其中m是边数，q是查询数，Q是时间范围
    public static void solveDynamicConnectivity(Scanner scanner) {
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        int q = scanner.nextInt();

        // 存储所有边及其时间区间
        List<int[]> edges = new ArrayList<>();
        int maxTime = 0;
        for (int i = 0; i < m; ++i) {
            int u = scanner.nextInt() - 1;  // 转换为0-based索引
            int v = scanner.nextInt() - 1;
            int l = scanner.nextInt();
            int r = scanner.nextInt();
            edges.add(new int[]{u, v, l, r});
            maxTime = Math.max(maxTime, r);
        }

        // 存储所有查询
        List<int[]> queries = new ArrayList<>();
        for (int i = 0; i < q; ++i) {
            int u = scanner.nextInt() - 1;
            int v = scanner.nextInt() - 1;
            int t = scanner.nextInt();
            queries.add(new int[]{u, v, t, i});
        }

        // 按时间排序查询
        queries.sort(Comparator.comparingInt(a -> a[2]));

        // 初始化线段树分治结构
        SegmentTreeDivideConquer stdc = new SegmentTreeDivideConquer(maxTime);

        // 添加边到线段树分治结构中
        for (int[] edge : edges) {
            stdc.addEdge(edge[2], edge[3], edge[0], edge[1]);
        }

        // 初始化可撤销并查集
        RollbackDSU dsu = new RollbackDSU(n);

        // 结果数组，按查询顺序存储答案
        boolean[] results = new boolean[q];
        
        // 当前处理的查询索引
        int[] currentQuery = {0};

        // DFS函数，处理线段树分治
        class DFSHandler {
            public void dfs(int node, int nodeL, int nodeR) {
                // 记录当前版本，用于回滚
                int currentVersion = dsu.getVersion();

                // 处理当前节点的所有边
                for (int[] op : stdc.getOperations(node)) {
                    dsu.unite(op[0], op[1]);
                }

                // 处理当前时间点的所有查询
                if (nodeL == nodeR) {
                    // 处理所有时间为nodeL的查询
                    while (currentQuery[0] < q && queries.get(currentQuery[0])[2] == nodeL) {
                        int[] query = queries.get(currentQuery[0]);
                        int u = query[0];
                        int v = query[1];
                        int idx = query[3];
                        // 检查u和v是否连通
                        results[idx] = (dsu.find(u) == dsu.find(v));
                        currentQuery[0]++;
                    }
                } else {
                    // 递归处理左右子节点
                    int mid = (nodeL + nodeR) / 2;
                    dfs(2 * node, nodeL, mid);
                    dfs(2 * node + 1, mid + 1, nodeR);
                }

                // 回滚到进入当前节点前的状态
                dsu.rollback(currentVersion);
            }
        }

        // 执行DFS，处理所有查询
        new DFSHandler().dfs(1, 1, maxTime);

        // 输出结果
        for (boolean res : results) {
            System.out.println(res ? "YES" : "NO");
        }
    }

    // 二分图判定问题解决方案
    // 问题描述：在动态图中判断每个时间点图是否是二分图
    // 时间复杂度：O(m log n log Q)
    public static void solveBipartiteChecking(Scanner scanner) {
        int n = scanner.nextInt();
        int m = scanner.nextInt();

        // 存储所有边及其时间区间
        List<int[]> edges = new ArrayList<>();
        int maxTime = 0;
        for (int i = 0; i < m; ++i) {
            int u = scanner.nextInt() - 1;  // 转换为0-based索引
            int v = scanner.nextInt() - 1;
            int l = scanner.nextInt();
            int r = scanner.nextInt();
            edges.add(new int[]{u, v, l, r});
            maxTime = Math.max(maxTime, r);
        }

        // 初始化线段树分治结构
        SegmentTreeDivideConquer stdc = new SegmentTreeDivideConquer(maxTime);

        // 添加边到线段树分治结构中
        for (int[] edge : edges) {
            stdc.addEdge(edge[2], edge[3], edge[0], edge[1]);
        }

        // 初始化扩展域可撤销并查集
        // 扩展域：每个节点u有两个表示，u表示与集合根节点同色，u+n表示与集合根节点异色
        RollbackDSU dsu = new RollbackDSU(2 * n);

        // 结果数组，记录每个时间点是否是二分图
        boolean[] isBipartite = new boolean[maxTime + 2];
        Arrays.fill(isBipartite, true);

        // DFS函数，处理二分图检测
        class BipartiteDFSHandler {
            public void dfs(int node, int nodeL, int nodeR, boolean conflictInherited) {
                // 如果从父节点继承了冲突，则当前区间内所有时间点都不是二分图
                if (conflictInherited) {
                    for (int t = nodeL; t <= nodeR; ++t) {
                        if (1 <= t && t <= maxTime) {
                            isBipartite[t] = false;
                        }
                    }
                    return;
                }

                // 记录当前版本，用于回滚
                int currentVersion = dsu.getVersion();

                // 标记当前区间是否发生冲突
                boolean conflictInThisNode = false;

                // 处理当前节点的所有边
                for (int[] op : stdc.getOperations(node)) {
                    int u = op[0];
                    int v = op[1];
                    // 检查添加这条边是否会导致矛盾
                    // 如果u和v已经在同一个集合中，或者u+n和v+n在同一个集合中，则存在奇环
                    if (dsu.find(u) == dsu.find(v) || dsu.find(u + n) == dsu.find(v + n)) {
                        conflictInThisNode = true;
                        // 标记该区间内所有时间点都不是二分图
                        for (int t = nodeL; t <= nodeR; ++t) {
                            if (1 <= t && t <= maxTime) {
                                isBipartite[t] = false;
                            }
                        }
                        break;  // 已经发现冲突，可以跳过继续添加边
                    }

                    // 正常添加边：u和v必须异色，u+n和v必须同色，u和v+n必须同色
                    dsu.unite(u, v + n);
                    dsu.unite(u + n, v);
                }

                // 如果当前节点没有冲突，且不是叶子节点，则继续递归
                if (!conflictInThisNode && nodeL < nodeR) {
                    int mid = (nodeL + nodeR) / 2;
                    dfs(2 * node, nodeL, mid, false);
                    dfs(2 * node + 1, mid + 1, nodeR, false);
                }

                // 回滚到进入当前节点前的状态
                dsu.rollback(currentVersion);
            }
        }

        // 执行二分图检测的DFS
        new BipartiteDFSHandler().dfs(1, 1, maxTime, false);

        // 输出每个时间点的结果
        for (int t = 1; t <= maxTime; ++t) {
            System.out.println(isBipartite[t] ? "Yes" : "No");
        }
    }

    // 主函数
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // 这里可以根据需要调用不同的解决方案函数
        // 例如：
        // solveDynamicConnectivity(scanner);
        // solveBipartiteChecking(scanner);
        scanner.close();
    }
}