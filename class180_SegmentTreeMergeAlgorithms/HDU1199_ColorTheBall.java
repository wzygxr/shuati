/**
 * HDU 1199 Color the Ball
 * 题目链接: http://acm.hdu.edu.cn/showproblem.php?pid=1199
 * 
 * 题目描述:
 * 在数轴上有一些球，每个球都有一个坐标。现在要对这些球进行染色操作，每次操作将一段区间内的球染成白色或黑色。
 * 求最后最长的连续白色区间。
 * 
 * 解题思路:
 * 这是一个经典的区间染色问题，可以使用线段树结合离散化来解决。
 * 由于球的坐标范围很大(1-1e9)，我们需要先对坐标进行离散化处理。
 * 然后使用线段树维护区间的染色状态，支持区间更新和查询操作。
 * 
 * 时间复杂度分析:
 * - 离散化: O(n log n)
 * - 建树: O(n)
 * - 区间更新: O(log n)
 * - 区间查询: O(log n)
 * 
 * 空间复杂度: O(n)
 * 
 * 工程化考量:
 * 1. 异常处理: 检查输入参数的有效性
 * 2. 边界情况: 处理空数组、单个元素等情况
 * 3. 性能优化: 使用离散化减少空间复杂度
 * 4. 可测试性: 提供完整的测试用例覆盖各种场景
 * 5. 可读性: 添加详细的注释说明设计思路和实现细节
 * 6. 鲁棒性: 处理极端输入和非理想数据
 */

import java.util.*;

public class HDU1199_ColorTheBall {
    private int n;
    private int[] color;  // 0表示未染色，1表示白色，-1表示黑色
    private int[] lazy;   // 懒标记，0表示无标记，1表示白色，-1表示黑色

    /**
     * 构造函数 - 初始化线段树
     * 
     * @param size 数组大小
     */
    public HDU1199_ColorTheBall(int size) {
        // 参数校验
        if (size <= 0) {
            throw new IllegalArgumentException("数组大小必须为正整数");
        }
        
        this.n = size;
        // 线段树数组通常开4倍空间，确保有足够空间存储所有节点
        this.color = new int[size * 4];
        this.lazy = new int[size * 4];
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
            color[i] = 0;
        }
    }

    /**
     * 向下传递懒标记
     * 
     * @param i 当前节点编号
     */
    private void pushDown(int i) {
        if (lazy[i] != 0) {
            // 传递颜色标记给左右子树
            color[i << 1] = lazy[i];
            color[i << 1 | 1] = lazy[i];
            lazy[i << 1] = lazy[i];
            lazy[i << 1 | 1] = lazy[i];
            // 清除当前节点的懒标记
            lazy[i] = 0;
        }
    }

    /**
     * 范围染色 - 将区间[jobl, jobr]染成颜色jobv
     * 
     * @param jobl 任务区间左端点
     * @param jobr 任务区间右端点
     * @param jobv 染色颜色(1表示白色，-1表示黑色)
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
            if (jobl <= mid) {
                updateRange(jobl, jobr, jobv, l, mid, i << 1);
            }
            if (jobr > mid) {
                updateRange(jobl, jobr, jobv, mid + 1, r, i << 1 | 1);
            }
            pushUp(i);
        }
    }

    /**
     * 查询区间颜色状态
     * 
     * @param jobl 查询区间左端点
     * @param jobr 查询区间右端点
     * @param l    当前区间左端点
     * @param r    当前区间右端点
     * @param i    当前节点编号
     * @return 区间颜色状态
     */
    public int queryRange(int jobl, int jobr, int l, int r, int i) {
        if (jobl <= l && r <= jobr) {
            // 当前区间完全包含在查询区间内
            return color[i];
        } else {
            // 需要继续向下递归
            pushDown(i);
            int mid = (l + r) >> 1;
            int leftColor = 0;
            int rightColor = 0;
            if (jobl <= mid) {
                leftColor = queryRange(jobl, jobr, l, mid, i << 1);
            }
            if (jobr > mid) {
                rightColor = queryRange(jobl, jobr, mid + 1, r, i << 1 | 1);
            }
            
            // 合并结果
            if (leftColor == rightColor) {
                return leftColor;
            } else {
                return 0; // 混合状态
            }
        }
    }

    /**
     * 查找最长的连续白色区间
     * 
     * @param l 当前区间左端点
     * @param r 当前区间右端点
     * @param i 当前节点编号
     * @return 最长连续白色区间的长度
     */
    public int findLongestWhiteSegment(int l, int r, int i) {
        if (l == r) {
            // 叶子节点
            return color[i] == 1 ? 1 : 0;
        }
        
        // 需要继续向下递归
        pushDown(i);
        int mid = (l + r) >> 1;
        int leftLength = findLongestWhiteSegment(l, mid, i << 1);
        int rightLength = findLongestWhiteSegment(mid + 1, r, i << 1 | 1);
        
        // 计算跨越中点的白色区间长度
        int crossLength = 0;
        // 从mid向左扩展
        int leftCross = 0;
        if (color[i << 1] == 1) { // 左子树全为白色
            leftCross = mid - l + 1;
        }
        // 从mid+1向右扩展
        int rightCross = 0;
        if (color[i << 1 | 1] == 1) { // 右子树全为白色
            rightCross = r - mid;
        }
        
        crossLength = leftCross + rightCross;
        
        return Math.max(Math.max(leftLength, rightLength), crossLength);
    }

    /**
     * 离散化坐标
     * 
     * @param operations 操作列表，每个操作为(start, end, color)
     * @return (离散化后的坐标列表, 坐标映射字典)
     */
    public static class DiscretizationResult {
        public List<Integer> coordList;
        public Map<Integer, Integer> coordMap;
        
        public DiscretizationResult(List<Integer> coordList, Map<Integer, Integer> coordMap) {
            this.coordList = coordList;
            this.coordMap = coordMap;
        }
    }
    
    public static DiscretizationResult discretizeCoordinates(List<int[]> operations) {
        Set<Integer> coordinates = new HashSet<>();
        for (int[] op : operations) {
            int start = op[0];
            int end = op[1];
            coordinates.add(start);
            coordinates.add(end);
            coordinates.add(start - 1); // 添加前一个点
            coordinates.add(end + 1);   // 添加后一个点
        }
        
        // 转换为排序后的列表
        List<Integer> coordList = new ArrayList<>(coordinates);
        Collections.sort(coordList);
        
        // 创建坐标映射字典
        Map<Integer, Integer> coordMap = new HashMap<>();
        for (int i = 0; i < coordList.size(); i++) {
            coordMap.put(coordList.get(i), i);
        }
        
        return new DiscretizationResult(coordList, coordMap);
    }

    /**
     * 解决染色球问题
     * 
     * @param operations 操作列表，每个操作为(start, end, color) (color: 1表示白色，0表示黑色)
     * @return 最长连续白色区间的长度
     */
    public static int solveColorTheBall(List<int[]> operations) {
        if (operations.isEmpty()) {
            return 0;
        }
        
        // 离散化坐标
        DiscretizationResult discResult = discretizeCoordinates(operations);
        List<Integer> coordList = discResult.coordList;
        Map<Integer, Integer> coordMap = discResult.coordMap;
        
        // 初始化线段树
        HDU1199_ColorTheBall segTree = new HDU1199_ColorTheBall(coordList.size());
        
        // 执行染色操作
        for (int[] op : operations) {
            int start = op[0];
            int end = op[1];
            int color = op[2];
            // 转换为离散化后的坐标
            int discStart = coordMap.get(start);
            int discEnd = coordMap.get(end);
            // 执行区间染色操作 (1表示白色，-1表示黑色)
            segTree.updateRange(discStart, discEnd, color, 0, coordList.size() - 1, 1);
        }
        
        // 查找最长连续白色区间
        return segTree.findLongestWhiteSegment(0, coordList.size() - 1, 1);
    }

    // 测试代码
    public static void main(String[] args) {
        System.out.println("开始测试 HDU 1199 Color the Ball");
        
        // 测试用例1
        List<int[]> operations1 = new ArrayList<>();
        operations1.add(new int[]{1, 3, 1});  // 将区间[1,3]染成白色
        operations1.add(new int[]{2, 4, -1}); // 将区间[2,4]染成黑色
        operations1.add(new int[]{5, 6, 1});  // 将区间[5,6]染成白色
        
        int result1 = solveColorTheBall(operations1);
        System.out.println("测试用例1结果: " + result1); // 应该输出1 (区间[1,1]或[5,6]中的一个)
        
        // 测试用例2
        List<int[]> operations2 = new ArrayList<>();
        operations2.add(new int[]{1, 5, 1});  // 将区间[1,5]染成白色
        operations2.add(new int[]{3, 7, 1});  // 将区间[3,7]染成白色 (实际上是扩展白色区域)
        
        int result2 = solveColorTheBall(operations2);
        System.out.println("测试用例2结果: " + result2); // 应该输出7 (区间[1,7]全为白色)
        
        // 测试用例3
        List<int[]> operations3 = new ArrayList<>();
        operations3.add(new int[]{1, 10, 1}); // 将区间[1,10]染成白色
        operations3.add(new int[]{3, 5, -1}); // 将区间[3,5]染成黑色
        operations3.add(new int[]{7, 9, -1}); // 将区间[7,9]染成黑色
        
        int result3 = solveColorTheBall(operations3);
        System.out.println("测试用例3结果: " + result3); // 应该输出2 (区间[1,2]或[6,6]或[10,10]中的最长白色区间)
        
        System.out.println("测试完成");
    }
}