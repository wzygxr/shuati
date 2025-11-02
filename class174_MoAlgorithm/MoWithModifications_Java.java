package class176;

import java.util.*;

/**
 * 带修改的莫队算法实现
 * 
 * 题目描述：
 * 给定一个数组，支持两种操作：
 * 1. 修改操作：将数组中某个位置的元素修改为新值
 * 2. 查询操作：查询区间[l, r]中有多少个不同的数
 * 
 * 解题思路：
 * 1. 使用带修改的莫队算法离线处理所有查询和修改
 * 2. 将数组分成大小为 n^(2/3) 的块（最优块大小）
 * 3. 按照块号、右端点块号、时间戳进行排序
 * 4. 维护当前区间的不同数计数和时间戳
 * 
 * 时间复杂度分析：
 * - 排序查询的时间复杂度为 O(m log m)
 * - 处理所有查询的时间复杂度为 O(n^(5/3))
 * - 总体时间复杂度为 O(n^(5/3) + m log m)
 * 
 * 空间复杂度分析：
 * - 存储数组、查询、修改、计数数组等需要 O(n + m) 的空间
 * 
 * 工程化考量：
 * 1. 异常处理：处理边界情况和无效查询
 * 2. 性能优化：使用最优的块大小 n^(2/3)
 * 3. 代码可读性：清晰的变量命名和详细的注释
 * 4. 模块化设计：将主要功能拆分为多个函数
 */
public class MoWithModifications_Java {
    
    // 用于存储查询的结构
    static class Query {
        int l;      // 查询的左边界
        int r;      // 查询的右边界
        int t;      // 查询的时间戳（在第几次修改之后）
        int idx;    // 查询的索引，用于输出答案时保持顺序
        int blockL; // 左端点所在的块
        int blockR; // 右端点所在的块
        
        public Query(int l, int r, int t, int idx, int blockSize) {
            this.l = l;
            this.r = r;
            this.t = t;
            this.idx = idx;
            this.blockL = l / blockSize;
            this.blockR = r / blockSize;
        }
    }
    
    // 用于存储修改的结构
    static class Modification {
        int pos;    // 修改的位置
        int oldVal; // 修改前的值
        int newVal; // 修改后的值
        
        public Modification(int pos, int oldVal, int newVal) {
            this.pos = pos;
            this.oldVal = oldVal;
            this.newVal = newVal;
        }
    }
    
    /**
     * 比较两个查询的顺序，用于带修改莫队算法的排序
     * 按照块号、右端点块号、时间戳进行排序
     */
    private static int compareQueries(Query q1, Query q2) {
        if (q1.blockL != q2.blockL) {
            return Integer.compare(q1.blockL, q2.blockL);
        }
        if (q1.blockR != q2.blockR) {
            return Integer.compare(q1.blockR, q2.blockR);
        }
        return Integer.compare(q1.t, q2.t);
    }
    
    /**
     * 主解题函数
     * @param arr 初始数组
     * @param queriesInput 查询列表，每个查询包含[l, r, t]，t表示查询在第几次修改后执行
     * @param modificationsInput 修改列表，每个修改包含[pos, newVal]
     * @return 每个查询的结果（区间内不同数的数量）
     */
    public static int[] solve(int[] arr, int[][] queriesInput, int[][] modificationsInput) {
        // 异常处理
        if (arr == null || arr.length == 0 || queriesInput == null || queriesInput.length == 0) {
            return new int[0];
        }
        
        int n = arr.length;
        int m = queriesInput.length;
        int k = modificationsInput != null ? modificationsInput.length : 0;
        
        // 计算块的大小（最优为 n^(2/3)）
        int blockSize = (int)Math.pow(n, 2.0 / 3) + 1;
        
        // 创建原始数组的副本，用于记录修改
        int[] originalArr = arr.clone();
        
        // 创建修改对象
        List<Modification> modifications = new ArrayList<>(k);
        for (int i = 0; i < k; i++) {
            int pos = modificationsInput[i][0] - 1; // 转换为0-based
            int newVal = modificationsInput[i][1];
            modifications.add(new Modification(pos, originalArr[pos], newVal));
            originalArr[pos] = newVal; // 更新原始数组用于下一次修改
        }
        
        // 创建查询对象
        Query[] queries = new Query[m];
        for (int i = 0; i < m; i++) {
            // 假设输入是1-based的，转换为0-based
            int l = queriesInput[i][0] - 1;
            int r = queriesInput[i][1] - 1;
            int t = queriesInput[i][2]; // 时间戳（从0开始）
            queries[i] = new Query(l, r, t, i, blockSize);
        }
        
        // 对查询进行排序
        Arrays.sort(queries, MoWithModifications_Java::compareQueries);
        
        // 离散化处理
        Set<Integer> valueSet = new HashSet<>();
        for (int num : arr) {
            valueSet.add(num);
        }
        for (Modification mod : modifications) {
            valueSet.add(mod.oldVal);
            valueSet.add(mod.newVal);
        }
        List<Integer> valueList = new ArrayList<>(valueSet);
        Map<Integer, Integer> valueToId = new HashMap<>();
        for (int i = 0; i < valueList.size(); i++) {
            valueToId.put(valueList.get(i), i);
        }
        
        // 离散化数组
        int[] discreteArr = new int[n];
        for (int i = 0; i < n; i++) {
            discreteArr[i] = valueToId.get(arr[i]);
        }
        
        // 初始化结果数组
        int[] answers = new int[m];
        
        // 使用数组计数
        int valueRange = valueList.size();
        int[] count = new int[valueRange];
        int[] currentResultRef = new int[1];  // 使用数组作为引用传递
        currentResultRef[0] = 0;  // 当前区间内不同数的数量
        
        // 初始化当前区间的左右指针和时间戳
        int curL = 0;
        int curR = -1;
        int curT = 0;
        
        // 处理每个查询
        for (Query q : queries) {
            int l = q.l;
            int r = q.r;
            int t = q.t;
            int idx = q.idx;
            
            // 调整时间戳到目标时间
            while (curT < t) {
                applyModification(curT, discreteArr, curL, curR, count, modifications, valueToId, currentResultRef);
                curT++;
            }
            while (curT > t) {
                curT--;
                undoModification(curT, discreteArr, curL, curR, count, modifications, valueToId, currentResultRef);
            }
            
            // 调整左右指针到目标位置
            while (curR < r) {
                curR++;
                int numId = discreteArr[curR];
                count[numId]++;
                if (count[numId] == 1) {
                    currentResultRef[0]++;
                }
            }
            
            while (curR > r) {
                int numId = discreteArr[curR];
                count[numId]--;
                if (count[numId] == 0) {
                    currentResultRef[0]--;
                }
                curR--;
            }
            
            while (curL > l) {
                curL--;
                int numId = discreteArr[curL];
                count[numId]++;
                if (count[numId] == 1) {
                    currentResultRef[0]++;
                }
            }
            
            while (curL < l) {
                int numId = discreteArr[curL];
                count[numId]--;
                if (count[numId] == 0) {
                    currentResultRef[0]--;
                }
                curL++;
            }
            
            // 保存当前查询的结果
            answers[idx] = currentResultRef[0];
        }
        
        return answers;
    }
    
    /**
     * 应用修改操作
     */
    private static void applyModification(int t, int[] discreteArr, int curL, int curR, 
                                        int[] count, List<Modification> modifications, Map<Integer, Integer> valueToId, int[] currentResultRef) {
        Modification mod = modifications.get(t);
        int pos = mod.pos;
        int oldVal = mod.oldVal;
        int newVal = mod.newVal;
        
        // 如果修改的位置在当前区间内，需要更新计数
        if (pos >= curL && pos <= curR) {
            int oldId = valueToId.get(oldVal);
            count[oldId]--;
            if (count[oldId] == 0) {
                currentResultRef[0]--;
            }
            
            int newId = valueToId.get(newVal);
            count[newId]++;
            if (count[newId] == 1) {
                currentResultRef[0]++;
            }
        }
        
        // 更新离散化数组
        discreteArr[pos] = valueToId.get(newVal);
    }
    
    /**
     * 撤销修改操作
     */
    private static void undoModification(int t, int[] discreteArr, int curL, int curR, 
                                        int[] count, List<Modification> modifications, Map<Integer, Integer> valueToId, int[] currentResultRef) {
        Modification mod = modifications.get(t);
        int pos = mod.pos;
        int oldVal = mod.oldVal;
        int newVal = mod.newVal;
        
        // 如果修改的位置在当前区间内，需要更新计数
        if (pos >= curL && pos <= curR) {
            int newId = valueToId.get(newVal);
            count[newId]--;
            if (count[newId] == 0) {
                currentResultRef[0]--;
            }
            
            int oldId = valueToId.get(oldVal);
            count[oldId]++;
            if (count[oldId] == 1) {
                currentResultRef[0]++;
            }
        }
        
        // 更新离散化数组
        discreteArr[pos] = valueToId.get(oldVal);
    }
    
    /**
     * 使用HashMap的优化版本，适用于数值范围较大的情况
     * @param arr 初始数组
     * @param queriesInput 查询列表，每个查询包含[l, r, t]，t表示查询在第几次修改后执行
     * @param modificationsInput 修改列表，每个修改包含[pos, newVal]
     * @return 每个查询的结果（区间内不同数的数量）
     */
    public static int[] solveWithHashMap(int[] arr, int[][] queriesInput, int[][] modificationsInput) {
        // 异常处理
        if (arr == null || arr.length == 0 || queriesInput == null || queriesInput.length == 0) {
            return new int[0];
        }
        
        int n = arr.length;
        int m = queriesInput.length;
        int k = modificationsInput != null ? modificationsInput.length : 0;
        
        // 计算块的大小（最优为 n^(2/3)）
        int blockSize = (int)Math.pow(n, 2.0 / 3) + 1;
        
        // 创建原始数组的副本，用于记录修改
        int[] originalArr = arr.clone();
        
        // 创建修改对象
        List<Modification> modifications = new ArrayList<>(k);
        for (int i = 0; i < k; i++) {
            int pos = modificationsInput[i][0] - 1; // 转换为0-based
            int newVal = modificationsInput[i][1];
            modifications.add(new Modification(pos, originalArr[pos], newVal));
            originalArr[pos] = newVal; // 更新原始数组用于下一次修改
        }
        
        // 创建查询对象
        Query[] queries = new Query[m];
        for (int i = 0; i < m; i++) {
            // 假设输入是1-based的，转换为0-based
            int l = queriesInput[i][0] - 1;
            int r = queriesInput[i][1] - 1;
            int t = queriesInput[i][2]; // 时间戳（从0开始）
            queries[i] = new Query(l, r, t, i, blockSize);
        }
        
        // 对查询进行排序
        Arrays.sort(queries, MoWithModifications_Java::compareQueries);
        
        // 初始化结果数组
        int[] answers = new int[m];
        
        // 使用HashMap计数
        Map<Integer, Integer> countMap = new HashMap<>();
        int[] currentResultRef = new int[1];
        currentResultRef[0] = 0;  // 当前区间内不同数的数量
        
        // 初始化当前区间的左右指针和时间戳
        int curL = 0;
        int curR = -1;
        int curT = 0;
        
        // 处理每个查询
        for (Query q : queries) {
            int l = q.l;
            int r = q.r;
            int t = q.t;
            int idx = q.idx;
            
            // 调整时间戳到目标时间
            while (curT < t) {
                applyModificationWithHashMap(curT, originalArr, curL, curR, countMap, modifications, currentResultRef);
                curT++;
            }
            while (curT > t) {
                curT--;
                undoModificationWithHashMap(curT, originalArr, curL, curR, countMap, modifications, currentResultRef);
            }
            
            // 调整左右指针到目标位置
            while (curR < r) {
                curR++;
                int num = originalArr[curR];
                countMap.put(num, countMap.getOrDefault(num, 0) + 1);
                if (countMap.get(num) == 1) {
                    currentResultRef[0]++;
                }
            }
            
            while (curR > r) {
                int num = originalArr[curR];
                countMap.put(num, countMap.get(num) - 1);
                if (countMap.get(num) == 0) {
                    currentResultRef[0]--;
                }
                curR--;
            }
            
            while (curL > l) {
                curL--;
                int num = originalArr[curL];
                countMap.put(num, countMap.getOrDefault(num, 0) + 1);
                if (countMap.get(num) == 1) {
                    currentResultRef[0]++;
                }
            }
            
            while (curL < l) {
                int num = originalArr[curL];
                countMap.put(num, countMap.get(num) - 1);
                if (countMap.get(num) == 0) {
                    currentResultRef[0]--;
                }
                curL++;
            }
            
            // 保存当前查询的结果
            answers[idx] = currentResultRef[0];
        }
        
        return answers;
    }
    
    /**
     * 应用修改操作（使用HashMap）
     */
    private static void applyModificationWithHashMap(int t, int[] originalArr, int curL, int curR, 
                                                   Map<Integer, Integer> countMap, List<Modification> modifications, int[] currentResultRef) {
        Modification mod = modifications.get(t);
        int pos = mod.pos;
        int oldVal = mod.oldVal;
        int newVal = mod.newVal;
        
        // 如果修改的位置在当前区间内，需要更新计数
        if (pos >= curL && pos <= curR) {
            countMap.put(oldVal, countMap.getOrDefault(oldVal, 0) - 1);
            if (countMap.get(oldVal) == 0) {
                currentResultRef[0]--;
            }
            
            countMap.put(newVal, countMap.getOrDefault(newVal, 0) + 1);
            if (countMap.get(newVal) == 1) {
                currentResultRef[0]++;
            }
        }
        
        // 更新数组
        originalArr[pos] = newVal;
    }
    
    /**
     * 撤销修改操作（使用HashMap）
     */
    private static void undoModificationWithHashMap(int t, int[] originalArr, int curL, int curR, 
                                                  Map<Integer, Integer> countMap, List<Modification> modifications, int[] currentResultRef) {
        Modification mod = modifications.get(t);
        int pos = mod.pos;
        int oldVal = mod.oldVal;
        int newVal = mod.newVal;
        
        // 如果修改的位置在当前区间内，需要更新计数
        if (pos >= curL && pos <= curR) {
            countMap.put(newVal, countMap.getOrDefault(newVal, 0) - 1);
            if (countMap.get(newVal) == 0) {
                currentResultRef[0]--;
            }
            
            countMap.put(oldVal, countMap.getOrDefault(oldVal, 0) + 1);
            if (countMap.get(oldVal) == 1) {
                currentResultRef[0]++;
            }
        }
        
        // 更新数组
        originalArr[pos] = oldVal;
    }
    
    /**
     * 主函数，用于测试
     */
    public static void main(String[] args) {
        // 测试用例
        int[] arr = {1, 2, 1, 3, 4, 2, 5};
        
        // 查询列表：每个查询为[l, r, t]，表示在第t次修改后查询区间[l, r]
        int[][] queries = {
            {1, 5, 0},  // 查询区间[1,5]在第0次修改后（即初始状态）
            {2, 6, 1},  // 查询区间[2,6]在第1次修改后
            {3, 7, 2}   // 查询区间[3,7]在第2次修改后
        };
        
        // 修改列表：每个修改为[pos, newVal]，表示将位置pos的值修改为newVal
        int[][] modifications = {
            {2, 6},     // 将位置2的值修改为6
            {4, 7},     // 将位置4的值修改为7
            {6, 8}      // 将位置6的值修改为8
        };
        
        // 测试离散化版本
        int[] results = solve(arr, queries, modifications);
        
        // 输出结果
        System.out.println("Query Results (Discretized Version):");
        for (int result : results) {
            System.out.println(result);
        }
        
        // 测试HashMap版本
        int[] results2 = solveWithHashMap(arr, queries, modifications);
        
        // 输出结果
        System.out.println("\nQuery Results (HashMap Version):");
        for (int result : results2) {
            System.out.println(result);
        }
        
        // 验证两种方法结果一致
        boolean allEqual = true;
        for (int i = 0; i < results.length; i++) {
            if (results[i] != results2[i]) {
                allEqual = false;
                break;
            }
        }
        System.out.println("\nResults match: " + allEqual);
    }
}