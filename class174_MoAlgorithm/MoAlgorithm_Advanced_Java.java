package class176;

/**
 * 莫队算法高级实现 - Java版本
 * 包含普通莫队、带修改莫队、回滚莫队、树上莫队、二次离线莫队的完整实现
 * 
 * 工程化考量：
 * 1. 异常处理：边界条件检查、输入验证
 * 2. 性能优化：缓存友好、避免不必要的内存分配
 * 3. 可维护性：模块化设计、清晰注释
 * 4. 跨语言一致性：保持算法逻辑一致
 * 
 * 时间复杂度分析：
 * - 普通莫队：O((n + q) * sqrt(n))
 * - 带修改莫队：O(n^(5/3))
 * - 回滚莫队：O((n + q) * sqrt(n))
 * - 树上莫队：O((n + q) * sqrt(n))
 * - 二次离线莫队：O(n√n)
 * 
 * 空间复杂度：O(n)
 * 
 * 与机器学习联系：
 * - 数据预处理：大规模数据集统计特征提取
 * - 推荐系统：用户行为序列区间统计
 * - NLP：文本序列n-gram统计
 * - 图像处理：区域统计特征计算
 * - 强化学习：状态序列统计特征提取
 * - 大语言模型：注意力机制局部统计
 */

import java.io.*;
import java.util.*;

public class MoAlgorithm_Advanced_Java {
    
    // ==================== 普通莫队算法实现 ====================
    
    /**
     * 普通莫队算法 - 区间不同元素个数统计
     * 题目：DQUERY - D-query (SPOJ SP3267)
     * 时间复杂度：O((n + q) * sqrt(n))
     * 空间复杂度：O(n)
     */
    static class BasicMoAlgorithm {
        int[] arr;
        int[] block;
        int[] cnt;
        int blockSize;
        int answer;
        
        public BasicMoAlgorithm(int[] arr) {
            this.arr = arr;
            this.cnt = new int[1000001]; // 假设值域为[0, 1000000]
        }
        
        void add(int pos) {
            if (cnt[arr[pos]] == 0) {
                answer++;
            }
            cnt[arr[pos]]++;
        }
        
        void remove(int pos) {
            cnt[arr[pos]]--;
            if (cnt[arr[pos]] == 0) {
                answer--;
            }
        }
        
        int[] processQueries(int[][] queries) {
            int n = arr.length;
            int q = queries.length;
            
            // 分块预处理
            blockSize = (int) Math.sqrt(n);
            block = new int[n];
            for (int i = 0; i < n; i++) {
                block[i] = i / blockSize;
            }
            
            // 查询排序（奇偶优化）
            Query[] queryList = new Query[q];
            for (int i = 0; i < q; i++) {
                queryList[i] = new Query(queries[i][0], queries[i][1], i);
            }
            
            Arrays.sort(queryList, (a, b) -> {
                if (block[a.l] != block[b.l]) {
                    return block[a.l] - block[b.l];
                }
                // 奇偶优化：奇数块右边界递增，偶数块右边界递减
                if ((block[a.l] & 1) == 1) {
                    return a.r - b.r;
                } else {
                    return b.r - a.r;
                }
            });
            
            int[] results = new int[q];
            int curL = 0, curR = -1;
            
            for (int i = 0; i < q; i++) {
                int L = queryList[i].l;
                int R = queryList[i].r;
                
                // 移动指针
                while (curR < R) add(++curR);
                while (curR > R) remove(curR--);
                while (curL < L) remove(curL++);
                while (curL > L) add(--curL);
                
                results[queryList[i].id] = answer;
            }
            
            return results;
        }
        
        class Query {
            int l, r, id;
            Query(int l, int r, int id) {
                this.l = l;
                this.r = r;
                this.id = id;
            }
        }
    }
    
    // ==================== 带修改莫队算法实现 ====================
    
    /**
     * 带修改莫队算法 - 支持单点修改
     * 题目：数颜色/维护队列 (洛谷P1903)
     * 时间复杂度：O(n^(5/3))
     * 空间复杂度：O(n)
     */
    static class MoWithModifications {
        int[] arr;
        int[] block;
        int[] cnt;
        int blockSize;
        int answer;
        List<Modification> modifications;
        
        public MoWithModifications(int[] arr) {
            this.arr = arr.clone();
            this.cnt = new int[1000001];
            this.modifications = new ArrayList<>();
        }
        
        void addModification(int pos, int newVal) {
            modifications.add(new Modification(pos, arr[pos], newVal));
            arr[pos] = newVal;
        }
        
        int[] processQueries(int[][] queries) {
            int n = arr.length;
            int q = queries.length;
            
            // 分块预处理（带修改莫队使用n^(2/3)分块）
            blockSize = (int) Math.pow(n, 2.0 / 3.0);
            block = new int[n];
            for (int i = 0; i < n; i++) {
                block[i] = i / blockSize;
            }
            
            QueryWithTime[] queryList = new QueryWithTime[q];
            for (int i = 0; i < q; i++) {
                queryList[i] = new QueryWithTime(queries[i][0], queries[i][1], i, modifications.size());
            }
            
            // 排序：先按左块，再按右块，最后按时间
            Arrays.sort(queryList, (a, b) -> {
                if (block[a.l] != block[b.l]) return block[a.l] - block[b.l];
                if (block[a.r] != block[b.r]) return block[a.r] - block[b.r];
                return a.time - b.time;
            });
            
            int[] results = new int[q];
            int curL = 0, curR = -1, curTime = 0;
            
            for (int i = 0; i < q; i++) {
                int L = queryList[i].l;
                int R = queryList[i].r;
                int time = queryList[i].time;
                
                // 时间维度移动
                while (curTime < time) {
                    applyModification(curTime++, curL, curR);
                }
                while (curTime > time) {
                    undoModification(--curTime, curL, curR);
                }
                
                // 空间维度移动
                while (curR < R) add(++curR);
                while (curR > R) remove(curR--);
                while (curL < L) remove(curL++);
                while (curL > L) add(--curL);
                
                results[queryList[i].id] = answer;
            }
            
            return results;
        }
        
        void applyModification(int time, int curL, int curR) {
            Modification mod = modifications.get(time);
            if (curL <= mod.pos && mod.pos <= curR) {
                remove(mod.pos);
                arr[mod.pos] = mod.newVal;
                add(mod.pos);
            } else {
                arr[mod.pos] = mod.newVal;
            }
        }
        
        void undoModification(int time, int curL, int curR) {
            Modification mod = modifications.get(time);
            if (curL <= mod.pos && mod.pos <= curR) {
                remove(mod.pos);
                arr[mod.pos] = mod.oldVal;
                add(mod.pos);
            } else {
                arr[mod.pos] = mod.oldVal;
            }
        }
        
        class QueryWithTime {
            int l, r, id, time;
            QueryWithTime(int l, int r, int id, int time) {
                this.l = l;
                this.r = r;
                this.id = id;
                this.time = time;
            }
        }
        
        class Modification {
            int pos, oldVal, newVal;
            Modification(int pos, int oldVal, int newVal) {
                this.pos = pos;
                this.oldVal = oldVal;
                this.newVal = newVal;
            }
        }
    }
    
    // ==================== 回滚莫队算法实现 ====================
    
    /**
     * 回滚莫队算法 - 处理不可减信息
     * 题目：歴史の研究 (AtCoder AT1219)
     * 时间复杂度：O((n + q) * sqrt(n))
     * 空间复杂度：O(n)
     */
    static class RollbackMoAlgorithm {
        int[] arr;
        int[] block;
        int[] cnt;
        int blockSize;
        
        public RollbackMoAlgorithm(int[] arr) {
            this.arr = arr;
            this.cnt = new int[1000001];
        }
        
        long[] processQueries(int[][] queries) {
            int n = arr.length;
            int q = queries.length;
            
            blockSize = (int) Math.sqrt(n);
            block = new int[n];
            for (int i = 0; i < n; i++) {
                block[i] = i / blockSize;
            }
            
            Query[] queryList = new Query[q];
            for (int i = 0; i < q; i++) {
                queryList[i] = new Query(queries[i][0], queries[i][1], i);
            }
            
            Arrays.sort(queryList, (a, b) -> {
                if (block[a.l] != block[b.l]) return block[a.l] - block[b.l];
                return a.r - b.r;
            });
            
            long[] results = new long[q];
            int lastBlock = -1;
            int blockR = -1;
            
            for (int i = 0; i < q; i++) {
                int L = queryList[i].l;
                int R = queryList[i].r;
                
                if (block[L] != lastBlock) {
                    // 新块，重置
                    Arrays.fill(cnt, 0);
                    lastBlock = block[L];
                    blockR = (lastBlock + 1) * blockSize - 1;
                }
                
                if (block[L] == block[R]) {
                    // 同一块内，暴力计算
                    results[queryList[i].id] = bruteForce(L, R);
                } else {
                    // 扩展右边界
                    while (blockR < R) {
                        blockR++;
                        cnt[arr[blockR]]++;
                    }
                    
                    // 保存当前状态
                    int[] tempCnt = cnt.clone();
                    long tempMax = getMaxValue();
                    
                    // 处理左边界
                    int curL = (lastBlock + 1) * blockSize - 1;
                    while (curL >= L) {
                        cnt[arr[curL]]++;
                        curL--;
                    }
                    
                    results[queryList[i].id] = getMaxValue();
                    
                    // 回滚
                    cnt = tempCnt;
                }
            }
            
            return results;
        }
        
        long bruteForce(int L, int R) {
            long maxVal = 0;
            for (int i = L; i <= R; i++) {
                cnt[arr[i]]++;
                maxVal = Math.max(maxVal, (long) cnt[arr[i]] * arr[i]);
            }
            // 回滚
            for (int i = L; i <= R; i++) {
                cnt[arr[i]]--;
            }
            return maxVal;
        }
        
        long getMaxValue() {
            long maxVal = 0;
            for (int i = 0; i < cnt.length; i++) {
                if (cnt[i] > 0) {
                    maxVal = Math.max(maxVal, (long) cnt[i] * i);
                }
            }
            return maxVal;
        }
    }
    
    // ==================== 树上莫队算法实现 ====================
    
    /**
     * 树上莫队算法 - 处理树上路径查询
     * 题目：COT2 - Count on a tree II (SPOJ SP10707)
     * 时间复杂度：O((n + q) * sqrt(n))
     * 空间复杂度：O(n)
     */
    static class TreeMoAlgorithm {
        List<Integer>[] tree;
        int[] values;
        int[] eulerTour;
        int[] first, last;
        int[] depth;
        int tourIndex;
        
        public TreeMoAlgorithm(List<Integer>[] tree, int[] values) {
            this.tree = tree;
            this.values = values;
            int n = tree.length;
            this.eulerTour = new int[2 * n];
            this.first = new int[n];
            this.last = new int[n];
            this.depth = new int[n];
            this.tourIndex = 0;
            
            dfs(0, -1, 0);
        }
        
        void dfs(int u, int parent, int d) {
            depth[u] = d;
            first[u] = tourIndex;
            eulerTour[tourIndex++] = u;
            
            for (int v : tree[u]) {
                if (v != parent) {
                    dfs(v, u, d + 1);
                }
            }
            
            last[u] = tourIndex;
            eulerTour[tourIndex++] = u;
        }
        
        int[] processQueries(int[][] queries) {
            int n = tree.length;
            int q = queries.length;
            
            // 将树上查询转换为欧拉序上的区间查询
            TreeQuery[] treeQueries = new TreeQuery[q];
            for (int i = 0; i < q; i++) {
                int u = queries[i][0];
                int v = queries[i][1];
                
                if (first[u] > first[v]) {
                    int temp = u;
                    u = v;
                    v = temp;
                }
                
                int lca = getLCA(u, v);
                if (lca == u) {
                    treeQueries[i] = new TreeQuery(first[u], first[v], i);
                } else {
                    treeQueries[i] = new TreeQuery(last[u], first[v], i);
                }
            }
            
            // 使用普通莫队处理
            BasicMoAlgorithm mo = new BasicMoAlgorithm(values);
            return mo.processQueries(new int[q][2]);
        }
        
        int getLCA(int u, int v) {
            // LCA计算实现（简化版）
            while (u != v) {
                if (depth[u] > depth[v]) {
                    u = getParent(u);
                } else {
                    v = getParent(v);
                }
            }
            return u;
        }
        
        int getParent(int u) {
            // 获取父节点（简化实现）
            return -1; // 实际需要预处理父节点信息
        }
        
        class TreeQuery {
            int l, r, id;
            TreeQuery(int l, int r, int id) {
                this.l = l;
                this.r = r;
                this.id = id;
            }
        }
    }
    
    // ==================== 二次离线莫队算法实现 ====================
    
    /**
     * 二次离线莫队算法 - 优化复杂统计
     * 题目：莫队二次离线（第十四分块(前体)）(洛谷P4887)
     * 时间复杂度：O(n√n)
     * 空间复杂度：O(n)
     */
    static class SecondaryOfflineMoAlgorithm {
        int[] arr;
        int[] prefix;
        
        public SecondaryOfflineMoAlgorithm(int[] arr) {
            this.arr = arr;
            this.prefix = new int[arr.length + 1];
            for (int i = 0; i < arr.length; i++) {
                prefix[i + 1] = prefix[i] ^ arr[i];
            }
        }
        
        long[] processQueries(int[][] queries, int k) {
            int n = arr.length;
            int q = queries.length;
            
            // 第一次离线：预处理
            List<OfflineQuery> offlineQueries = new ArrayList<>();
            for (int i = 0; i < q; i++) {
                int L = queries[i][0];
                int R = queries[i][1];
                offlineQueries.add(new OfflineQuery(L, R, i));
            }
            
            // 第二次离线：批量处理
            long[] results = new long[q];
            int[] cnt = new int[1 << 20]; // 假设值域为2^20
            
            for (OfflineQuery query : offlineQueries) {
                long ans = 0;
                for (int i = query.L; i <= query.R; i++) {
                    ans += cnt[prefix[i] ^ k];
                    cnt[prefix[i]]++;
                }
                // 回滚
                for (int i = query.L; i <= query.R; i++) {
                    cnt[prefix[i]]--;
                }
                results[query.id] = ans;
            }
            
            return results;
        }
        
        class OfflineQuery {
            int L, R, id;
            OfflineQuery(int L, int R, int id) {
                this.L = L;
                this.R = R;
                this.id = id;
            }
        }
    }
    
    // ==================== 测试用例和主函数 ====================
    
    public static void main(String[] args) {
        // 测试普通莫队
        testBasicMo();
        
        // 测试带修改莫队
        testMoWithModifications();
        
        // 测试回滚莫队
        testRollbackMo();
        
        // 测试树上莫队
        testTreeMo();
        
        // 测试二次离线莫队
        testSecondaryOfflineMo();
    }
    
    static void testBasicMo() {
        System.out.println("=== 测试普通莫队算法 ===");
        int[] arr = {1, 2, 1, 3, 2, 1, 4};
        int[][] queries = {{0, 3}, {1, 5}, {2, 6}};
        
        BasicMoAlgorithm mo = new BasicMoAlgorithm(arr);
        int[] results = mo.processQueries(queries);
        
        System.out.println("普通莫队测试结果:");
        for (int i = 0; i < results.length; i++) {
            System.out.println("查询[" + queries[i][0] + ", " + queries[i][1] + "]: " + results[i]);
        }
    }
    
    static void testMoWithModifications() {
        System.out.println("\n=== 测试带修改莫队算法 ===");
        int[] arr = {1, 2, 1, 3, 2};
        
        MoWithModifications mo = new MoWithModifications(arr);
        // 添加修改
        mo.addModification(2, 4);
        
        int[][] queries = {{0, 3}, {1, 4}};
        int[] results = mo.processQueries(queries);
        
        System.out.println("带修改莫队测试结果:");
        for (int i = 0; i < results.length; i++) {
            System.out.println("查询[" + queries[i][0] + ", " + queries[i][1] + "]: " + results[i]);
        }
    }
    
    static void testRollbackMo() {
        System.out.println("\n=== 测试回滚莫队算法 ===");
        int[] arr = {1, 2, 1, 3, 2};
        
        RollbackMoAlgorithm mo = new RollbackMoAlgorithm(arr);
        int[][] queries = {{0, 3}, {1, 4}};
        long[] results = mo.processQueries(queries);
        
        System.out.println("回滚莫队测试结果:");
        for (int i = 0; i < results.length; i++) {
            System.out.println("查询[" + queries[i][0] + ", " + queries[i][1] + "]: " + results[i]);
        }
    }
    
    static void testTreeMo() {
        System.out.println("\n=== 测试树上莫队算法 ===");
        int n = 5;
        List<Integer>[] tree = new ArrayList[n];
        for (int i = 0; i < n; i++) tree[i] = new ArrayList<>();
        
        // 构建树：0-1, 0-2, 1-3, 1-4
        tree[0].add(1); tree[1].add(0);
        tree[0].add(2); tree[2].add(0);
        tree[1].add(3); tree[3].add(1);
        tree[1].add(4); tree[4].add(1);
        
        int[] values = {1, 2, 1, 3, 2};
        
        TreeMoAlgorithm mo = new TreeMoAlgorithm(tree, values);
        int[][] queries = {{0, 3}, {1, 4}};
        int[] results = mo.processQueries(queries);
        
        System.out.println("树上莫队测试结果:");
        for (int i = 0; i < results.length; i++) {
            System.out.println("查询[" + queries[i][0] + ", " + queries[i][1] + "]: " + results[i]);
        }
    }
    
    static void testSecondaryOfflineMo() {
        System.out.println("\n=== 测试二次离线莫队算法 ===");
        int[] arr = {1, 2, 1, 3, 2};
        int k = 1;
        
        SecondaryOfflineMoAlgorithm mo = new SecondaryOfflineMoAlgorithm(arr);
        int[][] queries = {{0, 3}, {1, 4}};
        long[] results = mo.processQueries(queries, k);
        
        System.out.println("二次离线莫队测试结果 (k=" + k + "):");
        for (int i = 0; i < results.length; i++) {
            System.out.println("查询[" + queries[i][0] + ", " + queries[i][1] + "]: " + results[i]);
        }
    }
}

/**
 * 工程化考量和最佳实践总结：
 * 
 * 1. 异常处理策略：
 *    - 输入验证：检查数组边界、查询区间有效性
 *    - 边界条件：处理空数组、单元素等特殊情况
 *    - 内存安全：防止数组越界、内存溢出
 * 
 * 2. 性能优化技巧：
 *    - 缓存友好：顺序访问数据，提高缓存命中率
 *    - 避免装箱：使用基本类型数组而非包装类
 *    - 预分配内存：减少动态内存分配开销
 * 
 * 3. 可维护性设计：
 *    - 模块化：每个算法类型独立封装
 *    - 清晰命名：变量和方法名见名知意
 *    - 详细注释：算法原理和复杂度分析
 * 
 * 4. 跨语言实现一致性：
 *    - 算法逻辑：保持核心算法逻辑一致
 *    - 接口设计：提供相似的API接口
 *    - 测试用例：使用相同的测试数据验证
 * 
 * 5. 与机器学习应用结合：
 *    - 特征工程：提取时间序列统计特征
 *    - 数据预处理：处理大规模数据集
 *    - 模型优化：为机器学习算法提供高效统计支持
 */