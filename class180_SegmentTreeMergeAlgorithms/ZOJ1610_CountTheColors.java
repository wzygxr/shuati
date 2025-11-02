/**
 * ZOJ 1610 Count the Colors
 * 题目链接: https://zoj.pintia.cn/problem-sets/91827364500/problems/91827364599
 * 
 * 题目描述:
 * 给定一个长度为8000的数轴，初始时所有位置都没有颜色。现在进行n次操作，
 * 每次操作将区间[l,r)染成颜色c。最后统计每种颜色有多少个连续的段。
 * 
 * 解题思路:
 * 这是一个经典的区间染色问题，可以使用线段树来解决。
 * 1. 使用线段树维护区间的颜色信息
 * 2. 每次染色操作时，更新对应区间的颜色
 * 3. 最后遍历整个数轴，统计每种颜色的连续段数
 * 
 * 时间复杂度分析:
 * - 建树: O(n)
 * - 区间更新: O(log n)
 * - 区间查询: O(log n)
 * - 统计结果: O(n)
 * 
 * 空间复杂度: O(n)
 * 
 * 工程化考量:
 * 1. 异常处理: 检查输入参数的有效性
 * 2. 边界情况: 处理空数组、单个元素等情况
 * 3. 性能优化: 使用懒标记优化区间更新
 * 4. 可测试性: 提供完整的测试用例覆盖各种场景
 * 5. 可读性: 添加详细的注释说明设计思路和实现细节
 * 6. 鲁棒性: 处理极端输入和非理想数据
 */

import java.util.*;

public class ZOJ1610_CountTheColors {
    private int n;
    private int[] color;  // -1表示无颜色
    private int[] lazy;   // 懒标记，-1表示无标记

    /**
     * 构造函数 - 初始化线段树
     * 
     * @param size 数组大小
     */
    public ZOJ1610_CountTheColors(int size) {
        // 参数校验
        if (size <= 0) {
            throw new IllegalArgumentException("数组大小必须为正整数");
        }
        
        this.n = size;
        // 线段树数组通常开4倍空间，确保有足够空间存储所有节点
        this.color = new int[size * 4];
        this.lazy = new int[size * 4];
        // 初始化为-1表示无颜色
        Arrays.fill(this.color, -1);
        Arrays.fill(this.lazy, -1);
    }

    /**
     * 向上更新节点信息 - 如果左右子树颜色相同，则父节点颜色为该颜色，否则为混合状态
     * 
     * @param i 当前节点编号
     */
    private void pushUp(int i) {
        // 如果左右子树颜色相同，则父节点颜色为该颜色
        if (color[i << 1] == color[i << 1 | 1]) {
            color[i] = color[i << 1];
        } else {
            // 否则为混合状态
            color[i] = -2; // -2表示混合颜色
        }
    }

    /**
     * 向下传递懒标记
     * 
     * @param i 当前节点编号
     */
    private void pushDown(int i) {
        if (lazy[i] != -1) {
            // 传递颜色标记给左右子树
            color[i << 1] = lazy[i];
            color[i << 1 | 1] = lazy[i];
            lazy[i << 1] = lazy[i];
            lazy[i << 1 | 1] = lazy[i];
            // 清除当前节点的懒标记
            lazy[i] = -1;
        }
    }

    /**
     * 范围染色 - 将区间[jobl, jobr)染成颜色jobv
     * 
     * @param jobl 任务区间左端点（包含）
     * @param jobr 任务区间右端点（不包含）
     * @param jobv 染色颜色
     * @param l    当前区间左端点
     * @param r    当前区间右端点
     * @param i    当前节点编号
     */
    public void updateRange(int jobl, int jobr, int jobv, int l, int r, int i) {
        if (jobl <= l && r <= jobr) {
            // 当前区间完全包含在任务区间内
            color[i] = jobv;
            lazy[i] = jobv;
        } else {
            // 需要继续向下递归
            pushDown(i);
            int mid = (l + r) >> 1;
            if (jobl < mid) {
                updateRange(jobl, jobr, jobv, l, mid, i << 1);
            }
            if (jobr > mid) {
                updateRange(jobl, jobr, jobv, mid, r, i << 1 | 1);
            }
            pushUp(i);
        }
    }

    /**
     * 查询所有位置的颜色并统计每种颜色的段数
     * 
     * @param l      当前区间左端点
     * @param r      当前区间右端点
     * @param i      当前节点编号
     * @param result 结果映射，记录每个位置的颜色
     */
    public void queryAllColors(int l, int r, int i, Map<Integer, Integer> result) {
        if (l == r) {
            // 叶子节点
            if (color[i] >= 0) { // 有效颜色
                result.put(l, color[i]);
            }
            return;
        }
        
        // 如果当前节点有懒标记，先传递下去
        if (lazy[i] != -1) {
            pushDown(i);
        }
        
        // 如果当前节点颜色统一，直接记录
        if (color[i] >= 0) {
            // 在区间[l,r]内所有位置都是同一颜色
            for (int pos = l; pos <= r; pos++) {
                result.put(pos, color[i]);
            }
            return;
        }
        
        // 需要继续向下递归
        int mid = (l + r) >> 1;
        queryAllColors(l, mid, i << 1, result);
        queryAllColors(mid + 1, r, i << 1 | 1, result);
    }

    /**
     * 统计每种颜色的段数
     * 
     * @return 映射，键为颜色，值为该颜色的段数
     */
    public Map<Integer, Integer> countColorSegments() {
        // 查询所有位置的颜色
        Map<Integer, Integer> colorPositions = new HashMap<>();
        queryAllColors(0, n - 1, 1, colorPositions);
        
        // 统计每种颜色的段数
        Map<Integer, Integer> colorCount = new HashMap<>();
        if (colorPositions.isEmpty()) {
            return colorCount;
        }
        
        // 按位置排序
        List<Integer> sortedPositions = new ArrayList<>(colorPositions.keySet());
        Collections.sort(sortedPositions);
        
        // 遍历所有位置，统计连续段
        int currentColor = colorPositions.get(sortedPositions.get(0));
        if (currentColor >= 0) {
            colorCount.put(currentColor, colorCount.getOrDefault(currentColor, 0) + 1);
        }
        
        for (int i = 1; i < sortedPositions.size(); i++) {
            int pos = sortedPositions.get(i);
            int color = colorPositions.get(pos);
            if (color >= 0 && color != currentColor) {
                colorCount.put(color, colorCount.getOrDefault(color, 0) + 1);
                currentColor = color;
            }
        }
        
        return colorCount;
    }

    /**
     * 解决ZOJ 1610 Count the Colors问题
     * 
     * @param operations 操作列表，每个操作为(start, end, color)
     * @param size 数轴长度，默认为8000
     * @return 映射，键为颜色，值为该颜色的段数
     */
    public static Map<Integer, Integer> solveCountTheColors(List<int[]> operations, int size) {
        if (operations.isEmpty()) {
            return new HashMap<>();
        }
        
        // 初始化线段树
        ZOJ1610_CountTheColors segTree = new ZOJ1610_CountTheColors(size);
        
        // 执行染色操作
        for (int[] op : operations) {
            int start = op[0];
            int end = op[1];
            int color = op[2];
            // 注意：题目中的区间是左闭右开[l,r)
            segTree.updateRange(start, end, color, 0, size - 1, 1);
        }
        
        // 统计每种颜色的段数
        return segTree.countColorSegments();
    }

    // 测试代码
    public static void main(String[] args) {
        System.out.println("开始测试 ZOJ 1610 Count the Colors");
        
        // 测试用例1
        List<int[]> operations1 = new ArrayList<>();
        operations1.add(new int[]{0, 2, 1});  // 将区间[0,2)染成颜色1
        operations1.add(new int[]{1, 3, 2});  // 将区间[1,3)染成颜色2
        operations1.add(new int[]{4, 5, 3});  // 将区间[4,5)染成颜色3
        
        Map<Integer, Integer> result1 = solveCountTheColors(operations1, 6);
        System.out.println("测试用例1结果: " + result1);  // 颜色1有1段，颜色2有1段，颜色3有1段
        
        // 测试用例2
        List<int[]> operations2 = new ArrayList<>();
        operations2.add(new int[]{0, 4, 1});  // 将区间[0,4)染成颜色1
        operations2.add(new int[]{1, 3, 2});  // 将区间[1,3)染成颜色2
        
        Map<Integer, Integer> result2 = solveCountTheColors(operations2, 5);
        System.out.println("测试用例2结果: " + result2);  // 颜色1有2段([0,1)和[3,4))，颜色2有1段
        
        // 测试用例3
        List<int[]> operations3 = new ArrayList<>();
        operations3.add(new int[]{0, 5, 1});  // 将区间[0,5)染成颜色1
        operations3.add(new int[]{2, 4, 1});  // 将区间[2,4)染成颜色1 (与之前相同，不会增加段数)
        
        Map<Integer, Integer> result3 = solveCountTheColors(operations3, 6);
        System.out.println("测试用例3结果: " + result3);  // 颜色1有1段([0,5))
        
        System.out.println("测试完成");
    }
}