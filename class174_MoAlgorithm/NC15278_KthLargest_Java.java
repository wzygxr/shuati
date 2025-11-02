package class176;

import java.util.*;

/**
 * 牛客网 NC15278 区间第k大问题的回滚莫队算法实现
 * 
 * 题目描述：
 * 给定一个数组，多次查询区间内的第k大元素
 * 
 * 解题思路：
 * 1. 区间第k大问题可以使用回滚莫队算法解决
 * 2. 回滚莫队主要用于解决难以撤销操作的问题
 * 3. 对于区间第k大问题，我们可以使用值域分块来维护
 * 
 * 时间复杂度分析：
 * - 回滚莫队的时间复杂度为 O(n * sqrt(n))，其中 n 是数组长度
 * - 值域分块的查询时间为 O(sqrt(maxValue))
 * - 总体时间复杂度为 O(n * sqrt(n) * sqrt(maxValue))
 * 
 * 空间复杂度分析：
 * - 存储数组、查询结构等需要 O(n) 的空间
 * - 值域分块数组需要 O(maxValue) 的空间
 * - 总体空间复杂度为 O(n + maxValue)
 * 
 * 工程化考量：
 * 1. 异常处理：处理边界情况和无效查询
 * 2. 性能优化：使用离散化来减小值域范围
 * 3. 代码可读性：清晰的变量命名和详细的注释
 */
public class NC15278_KthLargest_Java {
    
    // 用于存储查询的结构
    static class Query {
        int l;  // 查询的左边界
        int r;  // 查询的右边界
        int k;  // 第k大
        int idx;  // 查询的索引，用于输出答案时保持顺序
        int block;  // 查询所属的块
        
        public Query(int l, int r, int k, int idx, int blockSize) {
            this.l = l;
            this.r = r;
            this.k = k;
            this.idx = idx;
            this.block = l / blockSize;
        }
    }
    
    // 数组的值
    private static int[] nums;
    // 离散化后的值
    private static int[] values;
    // 离散化映射表
    private static Map<Integer, Integer> valueMap;
    // 块的大小
    private static int blockSize;
    // 离散化后的值域大小
    private static int valueRange;
    // 值域分块的块大小
    private static int valueBlockSize;
    // 每个值出现的次数
    private static int[] valueCount;
    // 每个值域块的总出现次数
    private static int[] blockCount;
    // 答案数组
    private static int[] answers;
    
    /**
     * 离散化函数
     * @param arr 原始数组
     * @return 离散化后的值域范围
     */
    private static int discretize(int[] arr) {
        Set<Integer> valueSet = new HashSet<>();
        for (int num : arr) {
            valueSet.add(num);
        }
        
        List<Integer> valueList = new ArrayList<>(valueSet);
        Collections.sort(valueList);
        
        valueMap = new HashMap<>();
        for (int i = 0; i < valueList.size(); i++) {
            valueMap.put(valueList.get(i), i + 1);  // 从1开始编号
        }
        
        values = new int[arr.length];
        for (int i = 0; i < arr.length; i++) {
            values[i] = valueMap.get(arr[i]);
        }
        
        return valueList.size();
    }
    
    /**
     * 比较两个查询的顺序，用于回滚莫队算法的排序
     * 左端点在同一块内的查询，按右端点降序排列；否则按左端点升序排列
     * @param q1 第一个查询
     * @param q2 第二个查询
     * @return 比较结果
     */
    private static int compareQueries(Query q1, Query q2) {
        if (q1.block != q2.block) {
            return Integer.compare(q1.l, q2.l);
        }
        // 同一块内的查询，按右端点降序排列，这样可以使用回滚莫队
        return Integer.compare(q2.r, q1.r);
    }
    
    /**
     * 添加一个值到统计中
     * @param val 要添加的值（离散化后）
     */
    private static void add(int val) {
        valueCount[val]++;
        blockCount[val / valueBlockSize]++;
    }
    
    /**
     * 从统计中移除一个值
     * @param val 要移除的值（离散化后）
     */
    private static void remove(int val) {
        valueCount[val]--;
        blockCount[val / valueBlockSize]--;
    }
    
    /**
     * 查询第k大的数
     * @param k 第k大
     * @return 第k大的数（原始值）
     */
    private static int queryKthLargest(int k) {
        int sum = 0;
        // 先按块查找
        for (int i = blockCount.length - 1; i >= 0; i--) {
            if (sum + blockCount[i] < k) {
                sum += blockCount[i];
            } else {
                // 在当前块中查找
                int start = i * valueBlockSize;
                int end = Math.min(start + valueBlockSize - 1, valueRange);
                for (int j = end; j >= start; j--) {
                    sum += valueCount[j];
                    if (sum >= k) {
                        // 将离散化后的值转换回原始值
                        for (Map.Entry<Integer, Integer> entry : valueMap.entrySet()) {
                            if (entry.getValue() == j) {
                                return entry.getKey();
                            }
                        }
                    }
                }
            }
        }
        return -1;  // 不应该到达这里
    }
    
    /**
     * 主解题函数
     * @param arr 输入数组
     * @param queriesInput 查询列表，每个查询包含[l, r, k]
     * @return 每个查询的第k大元素
     */
    public static int[] solve(int[] arr, int[][] queriesInput) {
        // 异常处理
        if (arr == null || arr.length == 0 || queriesInput == null || queriesInput.length == 0) {
            return new int[0];
        }
        
        nums = arr;
        int n = arr.length;
        int q = queriesInput.length;
        
        // 离散化
        valueRange = discretize(arr);
        
        // 计算块的大小
        blockSize = (int)Math.sqrt(n) + 1;
        valueBlockSize = (int)Math.sqrt(valueRange) + 1;
        
        // 创建查询
        Query[] queries = new Query[q];
        for (int i = 0; i < q; i++) {
            int l = queriesInput[i][0] - 1;  // 假设输入是1-based的
            int r = queriesInput[i][1] - 1;
            int k = queriesInput[i][2];
            queries[i] = new Query(l, r, k, i, blockSize);
        }
        
        // 对查询进行排序
        Arrays.sort(queries, NC15278_KthLargest_Java::compareQueries);
        
        // 初始化计数数组和答案数组
        valueCount = new int[valueRange + 2];
        blockCount = new int[(valueRange + valueBlockSize - 1) / valueBlockSize + 2];
        answers = new int[q];
        
        // 处理每个块
        int currentBlock = -1;
        int curR = -1;
        
        for (Query qObj : queries) {
            int l = qObj.l;
            int r = qObj.r;
            int k = qObj.k;
            int idx = qObj.idx;
            int block = qObj.block;
            
            // 如果是新的块，重置当前右端点和计数
            if (block != currentBlock) {
                // 清空计数
                Arrays.fill(valueCount, 0);
                Arrays.fill(blockCount, 0);
                currentBlock = block;
                curR = block * blockSize + blockSize - 1;
                curR = Math.min(curR, n - 1);
            }
            
            // 处理右端点
            while (curR < r) {
                curR++;
                add(values[curR]);
            }
            
            // 暂时保存当前状态，用于回滚
            int[] tempValueCount = Arrays.copyOf(valueCount, valueCount.length);
            int[] tempBlockCount = Arrays.copyOf(blockCount, blockCount.length);
            
            // 扩展左端点
            int tempL = block * blockSize;
            while (tempL > l) {
                tempL--;
                add(values[tempL]);
            }
            
            // 保存查询结果
            answers[idx] = queryKthLargest(k);
            
            // 回滚到之前的状态
            valueCount = tempValueCount;
            blockCount = tempBlockCount;
        }
        
        return answers;
    }
    
    /**
     * 主函数，用于测试
     */
    public static void main(String[] args) {
        // 测试用例
        int[] arr = {1, 3, 2, 4, 5};
        int[][] queries = {
            {1, 5, 2},  // 查询区间[1,5]的第2大元素
            {2, 4, 1},  // 查询区间[2,4]的第1大元素
            {3, 5, 3}   // 查询区间[3,5]的第3大元素
        };
        
        int[] results = solve(arr, queries);
        
        // 输出结果
        System.out.println("Query Results:");
        for (int result : results) {
            System.out.println(result);
        }
    }
}