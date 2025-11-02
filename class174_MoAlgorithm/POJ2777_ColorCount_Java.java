package class176;

import java.util.*;

/**
 * POJ 2777 颜色出现次数统计问题的普通莫队算法实现
 * 
 * 题目描述：
 * 给定一个长度为L的数组，每个元素代表一种颜色（用1到T之间的整数表示）。
 * 支持两种操作：
 * 1. C A B：将位置A的颜色改为B
 * 2. Q A B：查询区间[A,B]内有多少种不同的颜色
 * 
 * 解题思路：
 * 1. 这是一个支持单点修改和区间查询的问题，适合使用带修改莫队算法
 * 2. 带修改莫队在普通莫队的基础上增加了时间维度，对查询进行三维排序
 * 3. 维护当前区间内每种颜色的出现次数，以及当前不同颜色的数量
 * 
 * 时间复杂度分析：
 * - 带修改莫队的时间复杂度为 O(n^(5/3))，其中 n 是数组长度
 * - 空间复杂度为 O(n)
 * 
 * 工程化考量：
 * 1. 异常处理：处理数组边界情况
 * 2. 性能优化：使用快速输入输出
 * 3. 代码可读性：清晰的变量命名和详细的注释
 */
public class POJ2777_ColorCount_Java {
    
    // 用于存储查询的结构
    static class Query {
        int l;  // 查询的左边界
        int r;  // 查询的右边界
        int t;  // 查询的时间戳（即该查询之前有多少次修改操作）
        int idx;  // 查询的索引，用于输出答案时保持顺序
        
        public Query(int l, int r, int t, int idx) {
            this.l = l;
            this.r = r;
            this.t = t;
            this.idx = idx;
        }
    }
    
    // 用于存储修改操作的结构
    static class Modify {
        int pos;  // 修改的位置
        int oldColor;  // 修改前的颜色
        int newColor;  // 修改后的颜色
        
        public Modify(int pos, int oldColor, int newColor) {
            this.pos = pos;
            this.oldColor = oldColor;
            this.newColor = newColor;
        }
    }
    
    // 数组的当前颜色
    private static int[] colors;
    // 保存原始颜色的数组，用于回滚操作
    private static int[] originalColors;
    // 所有的修改操作
    private static List<Modify> modifies;
    // 所有的查询操作
    private static List<Query> queries;
    // 块的大小
    private static int blockSize;
    // 用于存储每种颜色出现的次数
    private static int[] count;
    // 当前区间内不同颜色的数量
    private static int currentResult;
    // 答案数组
    private static int[] answers;
    
    /**
     * 比较两个查询的顺序，用于带修改莫队算法的排序
     * 采用三维排序：块号 -> 右边界 -> 时间戳
     * @param q1 第一个查询
     * @param q2 第二个查询
     * @return 比较结果
     */
    private static int compareQueries(Query q1, Query q2) {
        // 首先按照左边界所在的块排序
        if (q1.l / blockSize != q2.l / blockSize) {
            return Integer.compare(q1.l / blockSize, q2.l / blockSize);
        }
        // 对于同一块内的查询，按照右边界排序
        if (q1.r / blockSize != q2.r / blockSize) {
            return Integer.compare(q1.r / blockSize, q2.r / blockSize);
        }
        // 对于同一块内且右边界也在同一块的查询，按照时间戳排序
        return Integer.compare(q1.t, q2.t);
    }
    
    /**
     * 添加一个元素到当前区间
     * @param pos 元素的位置
     */
    private static void add(int pos) {
        int color = colors[pos];
        // 如果该颜色之前没有出现过，增加不同颜色的计数
        if (count[color] == 0) {
            currentResult++;
        }
        // 增加该颜色的出现次数
        count[color]++;
    }
    
    /**
     * 从当前区间移除一个元素
     * @param pos 元素的位置
     */
    private static void remove(int pos) {
        int color = colors[pos];
        // 减少该颜色的出现次数
        count[color]--;
        // 如果该颜色现在不再出现，减少不同颜色的计数
        if (count[color] == 0) {
            currentResult--;
        }
    }
    
    /**
     * 应用或回滚一个修改操作
     * @param modifyIdx 要应用的修改操作的索引
     * @param apply true表示应用修改，false表示回滚修改
     */
    private static void applyModify(int modifyIdx, boolean apply) {
        Modify modify = modifies.get(modifyIdx);
        int pos = modify.pos;
        int oldColor = modify.oldColor;
        int newColor = modify.newColor;
        
        // 确定要切换的颜色
        int fromColor = apply ? oldColor : newColor;
        int toColor = apply ? newColor : oldColor;
        
        // 如果当前位置在查询区间内，需要更新统计
        if (pos >= queries.get(0).l && pos <= queries.get(0).r) {
            // 先移除旧颜色的影响
            remove(pos);
            // 更新颜色
            colors[pos] = toColor;
            // 再添加新颜色的影响
            add(pos);
        } else {
            // 如果当前位置不在查询区间内，直接更新颜色
            colors[pos] = toColor;
        }
    }
    
    /**
     * 主解题函数
     * @param L 数组长度
     * @param T 颜色种类数
     * @param O 操作数
     * @param initialColors 初始颜色数组
     * @param operations 操作列表
     * @return 每个查询操作的结果
     */
    public static List<Integer> solve(int L, int T, int O, int[] initialColors, List<String[]> operations) {
        // 初始化数据结构
        colors = Arrays.copyOf(initialColors, L + 1);  // 位置从1开始
        originalColors = Arrays.copyOf(initialColors, L + 1);
        modifies = new ArrayList<>();
        queries = new ArrayList<>();
        
        // 处理所有操作
        int queryIndex = 0;
        for (String[] op : operations) {
            char type = op[0].charAt(0);
            int A = Integer.parseInt(op[1]);
            int B = Integer.parseInt(op[2]);
            
            if (type == 'C') {
                // 修改操作
                modifies.add(new Modify(A, colors[A], B));
                colors[A] = B;  // 立即应用修改，便于后续操作使用最新状态
            } else if (type == 'Q') {
                // 查询操作
                queries.add(new Query(A, B, modifies.size() - 1, queryIndex++));
            }
        }
        
        // 恢复原始颜色，因为我们需要重新应用修改
        colors = Arrays.copyOf(originalColors, L + 1);
        
        // 计算块的大小，对于带修改莫队，通常取n^(2/3)
        blockSize = (int)Math.pow(L, 2.0 / 3.0) + 1;
        
        // 对查询进行排序
        queries.sort(POJ2777_ColorCount_Java::compareQueries);
        
        // 初始化计数数组和答案数组
        count = new int[T + 2];  // 颜色编号最大为T
        currentResult = 0;
        answers = new int[queries.size()];
        
        // 初始化当前区间的左右指针和时间戳
        int curL = 1;
        int curR = 0;
        int curT = -1;
        
        // 处理每个查询
        for (Query q : queries) {
            // 调整时间戳到目标时间
            while (curT < q.t) {
                applyModify(++curT, true);
            }
            while (curT > q.t) {
                applyModify(curT--, false);
            }
            
            // 调整左右指针到目标位置
            while (curL > q.l) add(--curL);
            while (curR < q.r) add(++curR);
            while (curL < q.l) remove(curL++);
            while (curR > q.r) remove(curR--);
            
            // 保存当前查询的结果
            answers[q.idx] = currentResult;
        }
        
        // 收集所有查询的结果
        List<Integer> resultList = new ArrayList<>();
        for (int ans : answers) {
            resultList.add(ans);
        }
        
        return resultList;
    }
    
    /**
     * 主函数，用于测试
     */
    public static void main(String[] args) {
        // 测试用例
        int L = 10;
        int T = 3;
        int O = 4;
        int[] initialColors = {0, 1, 2, 1, 3, 2, 1, 2, 3, 1, 2};  // 位置从1开始，索引0不使用
        
        List<String[]> operations = new ArrayList<>();
        operations.add(new String[] {"Q", "1", "10"});
        operations.add(new String[] {"C", "2", "3"});
        operations.add(new String[] {"Q", "1", "10"});
        operations.add(new String[] {"Q", "3", "6"});
        
        List<Integer> results = solve(L, T, O, initialColors, operations);
        
        // 输出结果
        System.out.println("Query Results:");
        for (int result : results) {
            System.out.println(result);
        }
    }
}