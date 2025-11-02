package class176;

import java.util.*;

/**
 * 区间不同数问题的常规莫队算法实现
 * 
 * 题目描述：
 * 给定一个数组，多次查询区间[l, r]中有多少个不同的数。
 * 
 * 解题思路：
 * 1. 使用莫队算法离线处理所有查询
 * 2. 将数组分成大小为 sqrt(n) 的块
 * 3. 按照块号对查询进行排序，同一块内按右端点排序
 * 4. 维护当前区间的不同数计数
 * 
 * 时间复杂度分析：
 * - 排序查询的时间复杂度为 O(m log m)
 * - 处理所有查询的时间复杂度为 O(n * sqrt(n))
 * - 总体时间复杂度为 O(n * sqrt(n) + m log m)
 * 
 * 空间复杂度分析：
 * - 存储数组、查询、计数数组等需要 O(n + m) 的空间
 * 
 * 工程化考量：
 * 1. 异常处理：处理边界情况和无效查询
 * 2. 性能优化：使用奇偶排序优化，合理选择块的大小
 * 3. 代码可读性：清晰的变量命名和详细的注释
 * 4. 模块化设计：将主要功能拆分为多个函数
 */
public class RangeUniqueCount_Java {
    
    // 用于存储查询的结构
    static class Query {
        int l;      // 查询的左边界
        int r;      // 查询的右边界
        int idx;    // 查询的索引，用于输出答案时保持顺序
        int block;  // 查询所属的块
        
        public Query(int l, int r, int idx, int blockSize) {
            this.l = l;
            this.r = r;
            this.idx = idx;
            this.block = l / blockSize;
        }
    }
    
    /**
     * 比较两个查询的顺序，用于莫队算法的排序
     * 奇偶排序优化：偶数块按r升序，奇数块按r降序
     */
    private static int compareQueries(Query q1, Query q2) {
        if (q1.block != q2.block) {
            return Integer.compare(q1.block, q2.block);
        }
        // 奇偶排序优化
        return (q1.block % 2 == 0) ? Integer.compare(q1.r, q2.r) : Integer.compare(q2.r, q1.r);
    }
    
    /**
     * 主解题函数
     * @param arr 输入数组
     * @param queriesInput 查询列表，每个查询包含[l, r]
     * @return 每个查询的结果（区间内不同数的数量）
     */
    public static int[] solve(int[] arr, int[][] queriesInput) {
        // 异常处理
        if (arr == null || arr.length == 0 || queriesInput == null || queriesInput.length == 0) {
            return new int[0];
        }
        
        int n = arr.length;
        int m = queriesInput.length;
        
        // 计算块的大小
        int blockSize = (int)Math.sqrt(n) + 1;
        
        // 创建查询对象
        Query[] queries = new Query[m];
        for (int i = 0; i < m; i++) {
            // 假设输入是1-based的，转换为0-based
            int l = queriesInput[i][0] - 1;
            int r = queriesInput[i][1] - 1;
            queries[i] = new Query(l, r, i, blockSize);
        }
        
        // 对查询进行排序
        Arrays.sort(queries, RangeUniqueCount_Java::compareQueries);
        
        // 初始化结果数组
        int[] answers = new int[m];
        
        // 找到数组中的最大值和最小值，用于优化计数数组的大小
        int maxVal = Integer.MIN_VALUE;
        int minVal = Integer.MAX_VALUE;
        for (int num : arr) {
            maxVal = Math.max(maxVal, num);
            minVal = Math.min(minVal, num);
        }
        
        // 离散化处理（如果数值范围很大）
        // 这里为了简单，我们使用哈希表来计数
        Map<Integer, Integer> countMap = new HashMap<>();
        int currentResult = 0;  // 当前区间内不同数的数量
        
        // 初始化当前区间的左右指针
        int curL = 0;
        int curR = -1;
        
        // 处理每个查询
        for (Query q : queries) {
            int l = q.l;
            int r = q.r;
            int idx = q.idx;
            
            // 调整左右指针到目标位置
            while (curR < r) {
                curR++;
                int num = arr[curR];
                countMap.put(num, countMap.getOrDefault(num, 0) + 1);
                if (countMap.get(num) == 1) {
                    currentResult++;
                }
            }
            
            while (curR > r) {
                int num = arr[curR];
                countMap.put(num, countMap.get(num) - 1);
                if (countMap.get(num) == 0) {
                    currentResult--;
                }
                curR--;
            }
            
            while (curL > l) {
                curL--;
                int num = arr[curL];
                countMap.put(num, countMap.getOrDefault(num, 0) + 1);
                if (countMap.get(num) == 1) {
                    currentResult++;
                }
            }
            
            while (curL < l) {
                int num = arr[curL];
                countMap.put(num, countMap.get(num) - 1);
                if (countMap.get(num) == 0) {
                    currentResult--;
                }
                curL++;
            }
            
            // 保存当前查询的结果
            answers[idx] = currentResult;
        }
        
        return answers;
    }
    
    /**
     * 优化版本，使用数组代替哈希表提高性能
     */
    public static int[] solveOptimized(int[] arr, int[][] queriesInput) {
        // 异常处理
        if (arr == null || arr.length == 0 || queriesInput == null || queriesInput.length == 0) {
            return new int[0];
        }
        
        int n = arr.length;
        int m = queriesInput.length;
        
        // 离散化处理
        Set<Integer> valueSet = new HashSet<>();
        for (int num : arr) {
            valueSet.add(num);
        }
        List<Integer> valueList = new ArrayList<>(valueSet);
        Map<Integer, Integer> valueToId = new HashMap<>();
        for (int i = 0; i < valueList.size(); i++) {
            valueToId.put(valueList.get(i), i);
        }
        
        int[] discreteArr = new int[n];
        for (int i = 0; i < n; i++) {
            discreteArr[i] = valueToId.get(arr[i]);
        }
        
        // 计算块的大小
        int blockSize = (int)Math.sqrt(n) + 1;
        
        // 创建查询对象
        Query[] queries = new Query[m];
        for (int i = 0; i < m; i++) {
            // 假设输入是1-based的，转换为0-based
            int l = queriesInput[i][0] - 1;
            int r = queriesInput[i][1] - 1;
            queries[i] = new Query(l, r, i, blockSize);
        }
        
        // 对查询进行排序
        Arrays.sort(queries, RangeUniqueCount_Java::compareQueries);
        
        // 初始化结果数组
        int[] answers = new int[m];
        
        // 使用数组计数
        int valueRange = valueList.size();
        int[] count = new int[valueRange];
        int currentResult = 0;  // 当前区间内不同数的数量
        
        // 初始化当前区间的左右指针
        int curL = 0;
        int curR = -1;
        
        // 处理每个查询
        for (Query q : queries) {
            int l = q.l;
            int r = q.r;
            int idx = q.idx;
            
            // 调整左右指针到目标位置
            while (curR < r) {
                curR++;
                int numId = discreteArr[curR];
                count[numId]++;
                if (count[numId] == 1) {
                    currentResult++;
                }
            }
            
            while (curR > r) {
                int numId = discreteArr[curR];
                count[numId]--;
                if (count[numId] == 0) {
                    currentResult--;
                }
                curR--;
            }
            
            while (curL > l) {
                curL--;
                int numId = discreteArr[curL];
                count[numId]++;
                if (count[numId] == 1) {
                    currentResult++;
                }
            }
            
            while (curL < l) {
                int numId = discreteArr[curL];
                count[numId]--;
                if (count[numId] == 0) {
                    currentResult--;
                }
                curL++;
            }
            
            // 保存当前查询的结果
            answers[idx] = currentResult;
        }
        
        return answers;
    }
    
    /**
     * 主函数，用于测试
     */
    public static void main(String[] args) {
        // 测试用例
        int[] arr = {1, 2, 1, 3, 4, 2, 5};
        int[][] queries = {
            {1, 5},  // 查询区间[1,5]中不同数的数量
            {2, 6},  // 查询区间[2,6]中不同数的数量
            {3, 7}   // 查询区间[3,7]中不同数的数量
        };
        
        int[] results = solveOptimized(arr, queries);
        
        // 输出结果
        System.out.println("Query Results:");
        for (int result : results) {
            System.out.println(result);
        }
    }
}