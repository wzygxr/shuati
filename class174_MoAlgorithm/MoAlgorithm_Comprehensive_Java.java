import java.util.*;

public class MoAlgorithm_Comprehensive_Java {

    // ======================= 普通莫队算法 =======================
    public static class ClassicMoAlgorithm {
        
        static class Query {
            int l, r, index, block;
            
            Query(int l, int r, int index, int blockSize) {
                this.l = l;
                this.r = r;
                this.index = index;
                this.block = l / blockSize;
            }
        }
        
        public static int[] solve(int[] a, int[][] queries) {
            int n = a.length;
            int q = queries.length;
            int blockSize = (int)Math.sqrt(n) + 1;
            
            // 构建查询对象
            Query[] qs = new Query[q];
            for (int i = 0; i < q; i++) {
                int l = queries[i][0] - 1; // 转换为0-based
                int r = queries[i][1] - 1;
                qs[i] = new Query(l, r, i, blockSize);
            }
            
            // 排序查询
            Arrays.sort(qs, (q1, q2) -> {
                if (q1.block != q2.block) {
                    return q1.block - q2.block;
                }
                // 奇偶排序优化
                return q1.block % 2 == 0 ? q1.r - q2.r : q2.r - q1.r;
            });
            
            int[] ans = new int[q];
            Map<Integer, Integer> count = new HashMap<>();
            int currentAns = 0;
            int currentL = 0;
            int currentR = -1;
            
            // 处理每个查询
            for (Query query : qs) {
                int l = query.l;
                int r = query.r;
                int idx = query.index;
                
                // 调整左右指针
                while (currentL > l) {
                    currentL--;
                    int num = a[currentL];
                    count.put(num, count.getOrDefault(num, 0) + 1);
                    if (count.get(num) == 1) {
                        currentAns++;
                    }
                }
                
                while (currentR < r) {
                    currentR++;
                    int num = a[currentR];
                    count.put(num, count.getOrDefault(num, 0) + 1);
                    if (count.get(num) == 1) {
                        currentAns++;
                    }
                }
                
                while (currentL < l) {
                    int num = a[currentL];
                    count.put(num, count.get(num) - 1);
                    if (count.get(num) == 0) {
                        currentAns--;
                    }
                    currentL++;
                }
                
                while (currentR > r) {
                    int num = a[currentR];
                    count.put(num, count.get(num) - 1);
                    if (count.get(num) == 0) {
                        currentAns--;
                    }
                    currentR--;
                }
                
                ans[idx] = currentAns;
            }
            
            return ans;
        }
    }

    // ======================= 带修改莫队算法 =======================
    public static class MoWithModifications {
        
        static class Query {
            int l, r, t, index, block;
            
            Query(int l, int r, int t, int index, int blockSize) {
                this.l = l;
                this.r = r;
                this.t = t;
                this.index = index;
                this.block = l / blockSize;
            }
        }
        
        static class Modification {
            int pos, oldVal, newVal;
            
            Modification(int pos, int oldVal, int newVal) {
                this.pos = pos;
                this.oldVal = oldVal;
                this.newVal = newVal;
            }
        }
        
        public static int[] solve(int[] a, int[][] queries, int[][] modifications) {
            int n = a.length;
            int q = queries.length;
            int m = modifications.length;
            int blockSize = (int)Math.pow(n, 2.0 / 3.0) + 1;
            
            // 构建查询对象
            Query[] qs = new Query[q];
            for (int i = 0; i < q; i++) {
                int l = queries[i][0] - 1; // 转换为0-based
                int r = queries[i][1] - 1;
                int t = 0; // 初始时间戳
                qs[i] = new Query(l, r, t, i, blockSize);
            }
            
            // 构建修改对象
            List<Modification> mods = new ArrayList<>(m);
            int[] arr = a.clone(); // 复制原数组
            for (int i = 0; i < m; i++) {
                int pos = modifications[i][0] - 1; // 转换为0-based
                int newVal = modifications[i][1];
                int oldVal = arr[pos];
                mods.add(new Modification(pos, oldVal, newVal));
                arr[pos] = newVal; // 更新数组
            }
            
            // 恢复原数组
            for (int i = m - 1; i >= 0; i--) {
                Modification mod = mods.get(i);
                a[mod.pos] = mod.oldVal;
            }
            
            // 排序查询
            Arrays.sort(qs, (q1, q2) -> {
                if (q1.block != q2.block) {
                    return q1.block - q2.block;
                }
                int blockR1 = q1.r / blockSize;
                int blockR2 = q2.r / blockSize;
                if (blockR1 != blockR2) {
                    return blockR1 - blockR2;
                }
                return q1.t - q2.t;
            });
            
            int[] ans = new int[q];
            Map<Integer, Integer> count = new HashMap<>();
            int currentAns = 0;
            int currentL = 0;
            int currentR = -1;
            int currentT = 0;
            arr = a.clone(); // 复制原数组用于修改
            
            // 应用修改函数
            class Modifier {
                void apply(int t) {
                    Modification mod = mods.get(t);
                    int pos = mod.pos;
                    int oldVal = mod.oldVal;
                    int newVal = mod.newVal;
                    
                    // 交换旧值和新值，以便撤销时使用
                    mod.oldVal = newVal;
                    mod.newVal = oldVal;
                    
                    // 如果修改位置在当前区间内，需要更新计数
                    if (pos >= currentL && pos <= currentR) {
                        // 移除旧值
                        count.put(oldVal, count.getOrDefault(oldVal, 0) - 1);
                        if (count.get(oldVal) == 0) {
                            currentAns--;
                        }
                        // 添加新值
                        count.put(newVal, count.getOrDefault(newVal, 0) + 1);
                        if (count.get(newVal) == 1) {
                            currentAns++;
                        }
                    }
                    
                    // 更新数组
                    arr[pos] = newVal;
                }
            }
            
            Modifier modifier = new Modifier();
            
            // 处理每个查询
            for (Query query : qs) {
                int l = query.l;
                int r = query.r;
                int t = query.t;
                int idx = query.index;
                
                // 调整时间戳
                while (currentT < t) {
                    modifier.apply(currentT);
                    currentT++;
                }
                while (currentT > t) {
                    currentT--;
                    modifier.apply(currentT);
                }
                
                // 调整左右指针
                while (currentL > l) {
                    currentL--;
                    int num = arr[currentL];
                    count.put(num, count.getOrDefault(num, 0) + 1);
                    if (count.get(num) == 1) {
                        currentAns++;
                    }
                }
                
                while (currentR < r) {
                    currentR++;
                    int num = arr[currentR];
                    count.put(num, count.getOrDefault(num, 0) + 1);
                    if (count.get(num) == 1) {
                        currentAns++;
                    }
                }
                
                while (currentL < l) {
                    int num = arr[currentL];
                    count.put(num, count.get(num) - 1);
                    if (count.get(num) == 0) {
                        currentAns--;
                    }
                    currentL++;
                }
                
                while (currentR > r) {
                    int num = arr[currentR];
                    count.put(num, count.get(num) - 1);
                    if (count.get(num) == 0) {
                        currentAns--;
                    }
                    currentR--;
                }
                
                ans[idx] = currentAns;
            }
            
            return ans;
        }
    }

    // ======================= 回滚莫队算法 =======================
    public static class RollbackMoAlgorithm {
        
        static class Query {
            int l, r, index, block;
            
            Query(int l, int r, int index, int blockSize) {
                this.l = l;
                this.r = r;
                this.index = index;
                this.block = l / blockSize;
            }
        }
        
        public static int[] solve(int[] a, int[][] queries) {
            int n = a.length;
            int q = queries.length;
            int blockSize = (int)Math.sqrt(n) + 1;
            
            // 构建查询对象
            Query[] qs = new Query[q];
            for (int i = 0; i < q; i++) {
                int l = queries[i][0] - 1; // 转换为0-based
                int r = queries[i][1] - 1;
                qs[i] = new Query(l, r, i, blockSize);
            }
            
            // 排序查询
            Arrays.sort(qs, (q1, q2) -> {
                if (q1.block != q2.block) {
                    return q1.block - q2.block;
                }
                return q1.r - q2.r; // 同一块内右端点升序
            });
            
            int[] ans = new int[q];
            Map<Integer, Integer> count = new HashMap<>();
            int currentAns = 0;
            int currentBlock = -1;
            int r = -1;
            
            // 处理每个查询
            for (Query query : qs) {
                int queryL = query.l;
                int queryR = query.r;
                int idx = query.index;
                int block = query.block;
                
                // 如果进入新块，重置状态
                if (block != currentBlock) {
                    count.clear();
                    currentAns = 0;
                    currentBlock = block;
                    r = Math.min((currentBlock + 1) * blockSize - 1, n - 1);
                }
                
                // 如果查询完全在同一块内，暴力处理
                if (queryR / blockSize == block) {
                    Map<Integer, Integer> localCount = new HashMap<>();
                    int localAns = 0;
                    for (int i = queryL; i <= queryR; i++) {
                        int num = a[i];
                        localCount.put(num, localCount.getOrDefault(num, 0) + 1);
                        localAns = Math.max(localAns, localCount.get(num));
                    }
                    ans[idx] = localAns;
                    continue;
                }
                
                // 右端点逐步扩展（只添加不删除）
                while (r < queryR) {
                    r++;
                    int num = a[r];
                    count.put(num, count.getOrDefault(num, 0) + 1);
                    currentAns = Math.max(currentAns, count.get(num));
                }
                
                // 左端点使用临时Map回滚
                Map<Integer, Integer> tempCount = new HashMap<>(count);
                int tempAns = currentAns;
                
                // 处理左半部分
                for (int i = queryL; i < (currentBlock + 1) * blockSize; i++) {
                    int num = a[i];
                    tempCount.put(num, tempCount.getOrDefault(num, 0) + 1);
                    tempAns = Math.max(tempAns, tempCount.get(num));
                }
                
                ans[idx] = tempAns;
            }
            
            return ans;
        }
    }

    // ======================= 树上莫队算法 =======================
    public static class TreeMoAlgorithm {
        
        static class Query {
            int l, r, lca, index, block;
            
            Query(int l, int r, int lca, int index, int blockSize) {
                this.l = l;
                this.r = r;
                this.lca = lca;
                this.index = index;
                this.block = l / blockSize;
            }
        }
        
        public static int[] solve(int[] values, int[][] edges, int[][] queries) {
            int n = values.length;
            int q = queries.length;
            
            // 构建邻接表
            List<List<Integer>> adj = new ArrayList<>(n);
            for (int i = 0; i < n; i++) {
                adj.add(new ArrayList<>());
            }
            for (int[] edge : edges) {
                int u = edge[0] - 1; // 转换为0-based
                int v = edge[1] - 1;
                adj.get(u).add(v);
                adj.get(v).add(u);
            }
            
            // 预处理：欧拉序和LCA所需信息
            int[] inTime = new int[n];
            int[] outTime = new int[n];
            int[] depth = new int[n];
            int[] parent = new int[n];
            int logn = n > 0 ? (int)(Math.log(n) / Math.log(2)) + 1 : 0;
            int[][] up = new int[n][logn];
            List<Integer> euler = new ArrayList<>();
            int[] timeStamp = {0};
            
            // 初始化父数组
            Arrays.fill(parent, -1);
            // 初始化up表
            for (int[] row : up) {
                Arrays.fill(row, -1);
            }
            
            // DFS预处理
            class DFS {
                void dfs(int u, int p) {
                    inTime[u] = timeStamp[0];
                    euler.add(u);
                    timeStamp[0]++;
                    
                    parent[u] = p;
                    if (p != -1) {
                        depth[u] = depth[p] + 1;
                    } else {
                        depth[u] = 0;
                    }
                    
                    up[u][0] = p;
                    for (int k = 1; k < logn; k++) {
                        if (up[u][k-1] != -1) {
                            up[u][k] = up[up[u][k-1]][k-1];
                        }
                    }
                    
                    for (int v : adj.get(u)) {
                        if (v != p) {
                            dfs(v, u);
                        }
                    }
                    
                    outTime[u] = timeStamp[0];
                    euler.add(u);
                    timeStamp[0]++;
                }
            }
            
            new DFS().dfs(0, -1); // 假设根节点为0
            
            // LCA函数
            class LCA {
                int getLCA(int u, int v) {
                    if (depth[u] < depth[v]) {
                        int temp = u;
                        u = v;
                        v = temp;
                    }
                    
                    // 将u提升到与v同一深度
                    for (int k = logn - 1; k >= 0; k--) {
                        if (depth[u] - (1 << k) >= depth[v]) {
                            u = up[u][k];
                        }
                    }
                    
                    if (u == v) {
                        return u;
                    }
                    
                    for (int k = logn - 1; k >= 0; k--) {
                        if (up[u][k] != -1 && up[u][k] != up[v][k]) {
                            u = up[u][k];
                            v = up[v][k];
                        }
                    }
                    
                    return up[u][0];
                }
            }
            
            LCA lcaCalculator = new LCA();
            
            // 转换树查询为区间查询
            Query[] qs = new Query[q];
            for (int i = 0; i < q; i++) {
                int u = queries[i][0] - 1; // 转换为0-based
                int v = queries[i][1] - 1;
                
                if (inTime[u] > inTime[v]) {
                    int temp = u;
                    u = v;
                    v = temp;
                }
                
                int ancestor = lcaCalculator.getLCA(u, v);
                int l, r, lcaNode;
                
                if (ancestor == u) {
                    // 路径u->v在欧拉序中是连续的
                    l = inTime[u];
                    r = inTime[v];
                    lcaNode = -1;
                } else {
                    // 路径u->v需要考虑out[u]和in[v]，并额外处理LCA
                    l = outTime[u];
                    r = inTime[v];
                    lcaNode = ancestor;
                }
                
                qs[i] = new Query(l, r, lcaNode, i, 0); // 暂时不计算block
            }
            
            // 计算块大小并设置block
            int m = euler.size();
            int blockSize = (int)Math.sqrt(m) + 1;
            for (Query query : qs) {
                query.block = query.l / blockSize;
            }
            
            // 排序查询
            Arrays.sort(qs, (q1, q2) -> {
                if (q1.block != q2.block) {
                    return q1.block - q2.block;
                }
                // 奇偶排序优化
                return q1.block % 2 == 0 ? q1.r - q2.r : q2.r - q1.r;
            });
            
            int[] ans = new int[q];
            Map<Integer, Integer> count = new HashMap<>();
            boolean[] inRange = new boolean[n];
            int currentAns = 0;
            int currentL = 0;
            int currentR = -1;
            
            // 切换节点状态函数
            class Toggle {
                void toggle(int u) {
                    int num = values[u];
                    if (inRange[u]) {
                        // 移除节点
                        count.put(num, count.get(num) - 1);
                        if (count.get(num) == 0) {
                            currentAns--;
                        }
                    } else {
                        // 添加节点
                        count.put(num, count.getOrDefault(num, 0) + 1);
                        if (count.get(num) == 1) {
                            currentAns++;
                        }
                    }
                    inRange[u] = !inRange[u];
                }
            }
            
            Toggle toggle = new Toggle();
            
            // 处理每个查询
            for (Query query : qs) {
                int l = query.l;
                int r = query.r;
                int lcaNode = query.lca;
                int idx = query.index;
                
                // 调整左右指针
                while (currentL > l) {
                    currentL--;
                    toggle.toggle(euler.get(currentL));
                }
                while (currentR < r) {
                    currentR++;
                    toggle.toggle(euler.get(currentR));
                }
                while (currentL < l) {
                    toggle.toggle(euler.get(currentL));
                    currentL++;
                }
                while (currentR > r) {
                    toggle.toggle(euler.get(currentR));
                    currentR--;
                }
                
                // 处理LCA节点
                if (lcaNode != -1) {
                    toggle.toggle(lcaNode);
                }
                
                ans[idx] = currentAns;
                
                // 撤销LCA节点的处理
                if (lcaNode != -1) {
                    toggle.toggle(lcaNode);
                }
            }
            
            return ans;
        }
    }

    // 测试代码
    public static void main(String[] args) {
        testClassicMo();
        testMoWithModifications();
        testRollbackMo();
        testTreeMo();
    }
    
    private static void testClassicMo() {
        System.out.println("===== 测试普通莫队算法 ======");
        int[] a = {1, 2, 1, 3, 2, 4, 1, 5};
        int[][] queries = {{1, 4}, {2, 6}, {3, 8}, {1, 8}};
        int[] ans = ClassicMoAlgorithm.solve(a, queries);
        System.out.print("区间不同数个数结果: ");
        for (int x : ans) {
            System.out.print(x + " ");
        }
        System.out.println();
    }
    
    private static void testMoWithModifications() {
        System.out.println("\n===== 测试带修改莫队算法 ======");
        int[] a = {1, 2, 1, 3, 2};
        int[][] queries = {{1, 3}, {2, 5}, {1, 5}};
        int[][] modifications = {{2, 3}, {4, 4}}; // (位置, 新值)
        int[] ans = MoWithModifications.solve(a, queries, modifications);
        System.out.print("带修改莫队结果: ");
        for (int x : ans) {
            System.out.print(x + " ");
        }
        System.out.println();
    }
    
    private static void testRollbackMo() {
        System.out.println("\n===== 测试回滚莫队算法 ======");
        int[] a = {1, 2, 1, 3, 2, 1, 4};
        int[][] queries = {{1, 4}, {2, 6}, {1, 7}};
        int[] ans = RollbackMoAlgorithm.solve(a, queries);
        System.out.print("区间众数出现次数结果: ");
        for (int x : ans) {
            System.out.print(x + " ");
        }
        System.out.println();
    }
    
    private static void testTreeMo() {
        System.out.println("\n===== 测试树上莫队算法 ======");
        int[] values = {1, 2, 3, 1, 2, 4}; // 每个节点的值
        int[][] edges = {{1, 2}, {1, 3}, {2, 4}, {2, 5}, {3, 6}}; // 边
        int[][] queries = {{1, 5}, {4, 6}, {2, 3}}; // 路径查询
        int[] ans = TreeMoAlgorithm.solve(values, edges, queries);
        System.out.print("树上路径不同值个数结果: ");
        for (int x : ans) {
            System.out.print(x + " ");
        }
        System.out.println();
    }
}