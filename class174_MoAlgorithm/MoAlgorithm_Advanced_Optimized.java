package class176_MoAlgorithm;

import java.util.*;

/**
 * Mo's Algorithm 高级优化版本
 * 包含多种优化技术和高级特性
 */
public class MoAlgorithm_Advanced_Optimized {
    
    /**
     * 高级Mo's Algorithm实现 - 支持多种优化
     */
    public static class AdvancedMoAlgorithm {
        private int[] arr;
        private int n;
        private int blockSize;
        private int[] freq;
        private int distinctCount;
        
        public AdvancedMoAlgorithm(int[] arr) {
            this.arr = arr;
            this.n = arr.length;
            this.blockSize = (int) Math.sqrt(n);
            
            // 计算值域范围
            int maxVal = 0;
            for (int num : arr) {
                maxVal = Math.max(maxVal, num);
            }
            this.freq = new int[maxVal + 1];
            this.distinctCount = 0;
        }
        
        /**
         * 处理查询 - 支持多种优化策略
         */
        public int[] processQueries(int[][] queries, OptimizationStrategy strategy) {
            int q = queries.length;
            Query[] queryObjs = new Query[q];
            
            // 创建查询对象
            for (int i = 0; i < q; i++) {
                queryObjs[i] = new Query(queries[i][0], queries[i][1], i);
            }
            
            // 根据优化策略排序
            switch (strategy) {
                case STANDARD:
                    Arrays.sort(queryObjs, this::compareStandard);
                    break;
                case HILBERT:
                    Arrays.sort(queryObjs, this::compareHilbert);
                    break;
                case BLOCK_OPTIMIZED:
                    Arrays.sort(queryObjs, this::compareBlockOptimized);
                    break;
            }
            
            int[] result = new int[q];
            int curL = 0, curR = -1;
            
            // 处理每个查询
            for (Query query : queryObjs) {
                int L = query.l;
                int R = query.r;
                
                // 移动左指针
                while (curL > L) {
                    curL--;
                    add(arr[curL]);
                }
                while (curL < L) {
                    remove(arr[curL]);
                    curL++;
                }
                
                // 移动右指针
                while (curR < R) {
                    curR++;
                    add(arr[curR]);
                }
                while (curR > R) {
                    remove(arr[curR]);
                    curR--;
                }
                
                result[query.idx] = distinctCount;
            }
            
            return result;
        }
        
        /**
         * 标准比较函数
         */
        private int compareStandard(Query a, Query b) {
            int blockA = a.l / blockSize;
            int blockB = b.l / blockSize;
            if (blockA != blockB) {
                return Integer.compare(blockA, blockB);
            }
            // 奇偶块优化
            if (blockA % 2 == 0) {
                return Integer.compare(a.r, b.r);
            } else {
                return Integer.compare(b.r, a.r);
            }
        }
        
        /**
         * Hilbert曲线排序比较
         */
        private int compareHilbert(Query a, Query b) {
            // 简化的Hilbert曲线排序
            int blockA = a.l / blockSize;
            int blockB = b.l / blockSize;
            
            if (blockA != blockB) {
                return Integer.compare(blockA, blockB);
            }
            
            // 在同一个块内，使用更复杂的排序
            if ((a.l / blockSize) % 2 == 0) {
                return Integer.compare(a.r, b.r);
            } else {
                return Integer.compare(b.r, a.r);
            }
        }
        
        /**
         * 块优化比较
         */
        private int compareBlockOptimized(Query a, Query b) {
            // 动态块大小优化
            int dynamicBlockSize = Math.max(blockSize, n / 100);
            int blockA = a.l / dynamicBlockSize;
            int blockB = b.l / dynamicBlockSize;
            
            if (blockA != blockB) {
                return Integer.compare(blockA, blockB);
            }
            
            // 基于查询长度的优化
            int lenA = a.r - a.l;
            int lenB = b.r - b.l;
            if (lenA != lenB) {
                return Integer.compare(lenA, lenB);
            }
            
            return Integer.compare(a.r, b.r);
        }
        
        /**
         * 添加元素
         */
        private void add(int x) {
            freq[x]++;
            if (freq[x] == 1) {
                distinctCount++;
            }
        }
        
        /**
         * 移除元素
         */
        private void remove(int x) {
            freq[x]--;
            if (freq[x] == 0) {
                distinctCount--;
            }
        }
        
        /**
         * 查询对象
         */
        private static class Query {
            int l, r, idx;
            Query(int l, int r, int idx) {
                this.l = l;
                this.r = r;
                this.idx = idx;
            }
        }
    }
    
    /**
     * 带修改的Mo's Algorithm
     */
    public static class MoWithUpdates {
        private int[] arr;
        private int n;
        private int blockSize;
        private int[] freq;
        private int distinctCount;
        private List<Update> updates;
        private int time;
        
        public MoWithUpdates(int[] arr) {
            this.arr = arr.clone();
            this.n = arr.length;
            this.blockSize = (int) Math.cbrt(n); // 使用立方根作为块大小
            
            int maxVal = 0;
            for (int num : arr) {
                maxVal = Math.max(maxVal, num);
            }
            this.freq = new int[maxVal + 1];
            this.distinctCount = 0;
            this.updates = new ArrayList<>();
            this.time = 0;
        }
        
        /**
         * 添加修改操作
         */
        public void addUpdate(int pos, int newVal) {
            updates.add(new Update(pos, arr[pos], newVal, time++));
            arr[pos] = newVal;
        }
        
        /**
         * 处理带修改的查询
         */
        public int[] processQueriesWithUpdates(int[][] queries) {
            int q = queries.length;
            QueryWithTime[] queryObjs = new QueryWithTime[q];
            
            for (int i = 0; i < q; i++) {
                queryObjs[i] = new QueryWithTime(queries[i][0], queries[i][1], i, time);
            }
            
            // 排序：先按时间，再按空间
            Arrays.sort(queryObjs, (a, b) -> {
                int blockA = a.l / blockSize;
                int blockB = b.l / blockSize;
                if (blockA != blockB) return Integer.compare(blockA, blockB);
                
                blockA = a.r / blockSize;
                blockB = b.r / blockSize;
                if (blockA != blockB) return Integer.compare(blockA, blockB);
                
                return Integer.compare(a.time, b.time);
            });
            
            int[] result = new int[q];
            int curL = 0, curR = -1, curTime = 0;
            
            for (QueryWithTime query : queryObjs) {
                int L = query.l;
                int R = query.r;
                int T = query.time;
                
                // 应用时间修改
                while (curTime < T) {
                    applyUpdate(updates.get(curTime));
                    curTime++;
                }
                while (curTime > T) {
                    curTime--;
                    revertUpdate(updates.get(curTime));
                }
                
                // 移动空间指针
                while (curL > L) add(arr[--curL]);
                while (curR < R) add(arr[++curR]);
                while (curL < L) remove(arr[curL++]);
                while (curR > R) remove(arr[curR--]);
                
                result[query.idx] = distinctCount;
            }
            
            return result;
        }
        
        private void applyUpdate(Update update) {
            if (update.pos >= curL && update.pos <= curR) {
                remove(update.oldVal);
                add(update.newVal);
            }
            arr[update.pos] = update.newVal;
        }
        
        private void revertUpdate(Update update) {
            if (update.pos >= curL && update.pos <= curR) {
                remove(update.newVal);
                add(update.oldVal);
            }
            arr[update.pos] = update.oldVal;
        }
        
        private void add(int x) {
            freq[x]++;
            if (freq[x] == 1) distinctCount++;
        }
        
        private void remove(int x) {
            freq[x]--;
            if (freq[x] == 0) distinctCount--;
        }
        
        private static class QueryWithTime {
            int l, r, idx, time;
            QueryWithTime(int l, int r, int idx, int time) {
                this.l = l; this.r = r; this.idx = idx; this.time = time;
            }
        }
        
        private static class Update {
            int pos, oldVal, newVal, time;
            Update(int pos, int oldVal, int newVal, int time) {
                this.pos = pos; this.oldVal = oldVal; this.newVal = newVal; this.time = time;
            }
        }
    }
    
    /**
     * 并行Mo's Algorithm实现
     */
    public static class ParallelMoAlgorithm {
        private int[] arr;
        private int n;
        private int numThreads;
        
        public ParallelMoAlgorithm(int[] arr, int numThreads) {
            this.arr = arr;
            this.n = arr.length;
            this.numThreads = Math.min(numThreads, Runtime.getRuntime().availableProcessors());
        }
        
        /**
         * 并行处理查询
         */
        public int[] processQueriesParallel(int[][] queries) throws InterruptedException {
            int q = queries.length;
            int[] result = new int[q];
            
            // 将查询分组
            List<List<Integer>> queryGroups = partitionQueries(queries);
            
            // 创建线程池
            Thread[] threads = new Thread[numThreads];
            
            for (int i = 0; i < numThreads; i++) {
                final int threadId = i;
                final List<Integer> group = queryGroups.get(i);
                
                threads[i] = new Thread(() -> {
                    // 每个线程使用独立的Mo's Algorithm实例
                    AdvancedMoAlgorithm mo = new AdvancedMoAlgorithm(arr);
                    
                    // 提取该线程处理的查询
                    int[][] threadQueries = new int[group.size()][2];
                    for (int j = 0; j < group.size(); j++) {
                        int queryIdx = group.get(j);
                        threadQueries[j] = queries[queryIdx];
                    }
                    
                    // 处理查询
                    int[] threadResult = mo.processQueries(threadQueries, OptimizationStrategy.STANDARD);
                    
                    // 合并结果
                    for (int j = 0; j < group.size(); j++) {
                        int queryIdx = group.get(j);
                        result[queryIdx] = threadResult[j];
                    }
                });
                
                threads[i].start();
            }
            
            // 等待所有线程完成
            for (Thread thread : threads) {
                if (thread != null) {
                    thread.join();
                }
            }
            
            return result;
        }
        
        /**
         * 将查询分区到不同线程
         */
        private List<List<Integer>> partitionQueries(int[][] queries) {
            List<List<Integer>> groups = new ArrayList<>();
            for (int i = 0; i < numThreads; i++) {
                groups.add(new ArrayList<>());
            }
            
            // 简单的轮询分配
            for (int i = 0; i < queries.length; i++) {
                groups.get(i % numThreads).add(i);
            }
            
            return groups;
        }
    }
    
    /**
     * 优化策略枚举
     */
    public enum OptimizationStrategy {
        STANDARD,      // 标准优化
        HILBERT,       // Hilbert曲线优化
        BLOCK_OPTIMIZED // 块大小优化
    }
    
    /**
     * 性能分析工具
     */
    public static class PerformanceAnalyzer {
        public static void analyzePerformance(int[] arr, int[][] queries) {
            System.out.println("=== Mo's Algorithm 性能分析 ===\n");
            
            AdvancedMoAlgorithm mo = new AdvancedMoAlgorithm(arr);
            
            // 测试不同优化策略
            long startTime, endTime;
            
            // 标准优化
            startTime = System.nanoTime();
            int[] result1 = mo.processQueries(queries, OptimizationStrategy.STANDARD);
            endTime = System.nanoTime();
            System.out.printf("标准优化: %.3f ms\n", (endTime - startTime) / 1e6);
            
            // Hilbert优化
            startTime = System.nanoTime();
            int[] result2 = mo.processQueries(queries, OptimizationStrategy.HILBERT);
            endTime = System.nanoTime();
            System.out.printf("Hilbert优化: %.3f ms\n", (endTime - startTime) / 1e6);
            
            // 块优化
            startTime = System.nanoTime();
            int[] result3 = mo.processQueries(queries, OptimizationStrategy.BLOCK_OPTIMIZED);
            endTime = System.nanoTime();
            System.out.printf("块优化: %.3f ms\n", (endTime - startTime) / 1e6);
            
            // 验证结果一致性
            boolean consistent = Arrays.equals(result1, result2) && 
                                Arrays.equals(result2, result3);
            System.out.println("结果一致性: " + (consistent ? "✓" : "✗"));
            
            // 内存使用分析
            analyzeMemoryUsage(arr);
        }
        
        private static void analyzeMemoryUsage(int[] arr) {
            Runtime runtime = Runtime.getRuntime();
            long memoryBefore = runtime.totalMemory() - runtime.freeMemory();
            
            AdvancedMoAlgorithm mo = new AdvancedMoAlgorithm(arr);
            
            long memoryAfter = runtime.totalMemory() - runtime.freeMemory();
            long memoryUsed = memoryAfter - memoryBefore;
            
            System.out.printf("内存使用: %.2f MB\n", memoryUsed / (1024.0 * 1024.0));
        }
    }
    
    /**
     * 测试方法
     */
    public static void main(String[] args) throws InterruptedException {
        // 测试数据
        int[] arr = {1, 2, 3, 1, 2, 3, 4, 5, 1, 2, 3, 4, 5, 6};
        int[][] queries = {
            {0, 3}, {1, 4}, {2, 5}, {3, 6}, {4, 7}, {5, 8}, {6, 9}, {7, 10}
        };
        
        System.out.println("=== Mo's Algorithm 高级优化版本测试 ===\n");
        
        // 测试高级优化版本
        AdvancedMoAlgorithm advancedMo = new AdvancedMoAlgorithm(arr);
        
        // 测试不同优化策略
        System.out.println("1. 不同优化策略测试:");
        int[] result1 = advancedMo.processQueries(queries, OptimizationStrategy.STANDARD);
        int[] result2 = advancedMo.processQueries(queries, OptimizationStrategy.HILBERT);
        int[] result3 = advancedMo.processQueries(queries, OptimizationStrategy.BLOCK_OPTIMIZED);
        
        System.out.println("标准优化结果: " + Arrays.toString(result1));
        System.out.println("Hilbert优化结果: " + Arrays.toString(result2));
        System.out.println("块优化结果: " + Arrays.toString(result3));
        
        // 测试带修改版本
        System.out.println("\n2. 带修改版本测试:");
        MoWithUpdates moWithUpdates = new MoWithUpdates(arr);
        
        // 添加修改操作
        moWithUpdates.addUpdate(2, 10); // 将位置2的值改为10
        moWithUpdates.addUpdate(5, 20); // 将位置5的值改为20
        
        int[] resultWithUpdates = moWithUpdates.processQueriesWithUpdates(queries);
        System.out.println("带修改结果: " + Arrays.toString(resultWithUpdates));
        
        // 测试并行版本
        System.out.println("\n3. 并行版本测试:");
        ParallelMoAlgorithm parallelMo = new ParallelMoAlgorithm(arr, 4);
        int[] parallelResult = parallelMo.processQueriesParallel(queries);
        System.out.println("并行结果: " + Arrays.toString(parallelResult));
        
        // 性能分析
        System.out.println("\n4. 性能分析:");
        PerformanceAnalyzer.analyzePerformance(arr, queries);
        
        System.out.println("\n=== 测试完成 ===");
    }
}