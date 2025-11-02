package class115;

import java.util.*;

/**
 * 矩形面积并 - 扫描线算法实现
 * 题目链接: http://poj.org/problem?id=1151
 * 题目链接: http://acm.hdu.edu.cn/showproblem.php?pid=1542
 * 
 * 题目描述:
 * 给定多个矩形区域的地图，计算这些地图覆盖的总面积。
 * 每个矩形由其左下角坐标(x1, y1)和右上角坐标(x2, y2)表示。
 * 
 * 解题思路:
 * 使用扫描线算法结合线段树实现矩形面积并的计算。
 * 1. 将矩形拆分为左右两条边，作为扫描线事件
 * 2. 按x坐标排序所有事件
 * 3. 使用线段树维护y轴上的覆盖情况
 * 4. 扫描过程中计算相邻扫描线之间的面积
 * 
 * 算法复杂度: 时间复杂度O(n log n)，空间复杂度O(n)
 * 工程化考量:
 * 1. 异常处理: 检查坐标合法性
 * 2. 边界条件: 处理坐标重复和边界情况
 * 3. 性能优化: 使用离散化减少线段树规模
 * 4. 可读性: 详细注释和模块化设计
 * 5. 精度处理: 使用double类型处理浮点坐标，避免精度问题
 */
public class Code07_RectangleAreaUnion {
    
    /**
     * 线段树节点类
     * 用于维护y轴上的覆盖情况
     */
    static class SegmentTreeNode {
        int left, right; // 区间左右边界(离散化后的索引)
        int cover; // 当前区间被覆盖的次数
        double len; // 当前区间被覆盖的长度(实际坐标长度)
        
        SegmentTreeNode(int left, int right) {
            this.left = left;
            this.right = right;
            this.cover = 0;
            this.len = 0;
        }
    }
    
    /**
     * 扫描线事件类
     * 表示矩形的左边或右边
     */
    static class Event implements Comparable<Event> {
        double x; // x坐标(扫描线位置)
        double y1, y2; // y坐标区间
        int flag; // 1表示矩形开始(左边)，-1表示矩形结束(右边)
        
        Event(double x, double y1, double y2, int flag) {
            this.x = x;
            this.y1 = y1;
            this.y2 = y2;
            this.flag = flag;
        }
        
        @Override
        public int compareTo(Event other) {
            return Double.compare(this.x, other.x);
        }
    }
    
    // 线段树数组
    private SegmentTreeNode[] tree;
    // y坐标离散化数组
    private double[] y;
    
    /**
     * 计算矩形面积并
     * 算法核心思想：
     * 1. 将每个矩形的左右边界作为扫描线事件
     * 2. 按x坐标排序所有扫描线事件
     * 3. 使用线段树维护y轴上的覆盖长度
     * 4. 相邻扫描线之间的面积 = y轴覆盖长度 × x轴距离
     * 
     * @param rectangles 矩形数组，每个矩形为[x1, y1, x2, y2]
     * @return 总面积
     * @throws IllegalArgumentException 当矩形坐标不合法时抛出异常
     */
    public double calculateArea(double[][] rectangles) {
        // 边界条件检查
        if (rectangles == null || rectangles.length == 0) {
            return 0.0;
        }
        
        int n = rectangles.length;
        
        // 收集所有y坐标用于离散化
        Set<Double> ySet = new TreeSet<>();
        List<Event> events = new ArrayList<>();
        
        for (double[] rect : rectangles) {
            double x1 = rect[0], y1 = rect[1], x2 = rect[2], y2 = rect[3];
            
            // 检查坐标合法性
            if (x1 >= x2 || y1 >= y2) {
                throw new IllegalArgumentException("Invalid rectangle coordinates");
            }
            
            ySet.add(y1);
            ySet.add(y2);
            
            // 添加开始事件(矩形左边)和结束事件(矩形右边)
            events.add(new Event(x1, y1, y2, 1));
            events.add(new Event(x2, y1, y2, -1));
        }
        
        // 对事件按x坐标排序
        Collections.sort(events);
        
        // 离散化y坐标
        y = new double[ySet.size()];
        int index = 0;
        for (double val : ySet) {
            y[index++] = val;
        }
        
        // 构建线段树
        int size = y.length - 1;
        tree = new SegmentTreeNode[4 * size];
        buildTree(1, 0, size - 1);
        
        // 扫描线算法
        double area = 0.0;
        double lastX = events.get(0).x;
        
        for (Event event : events) {
            // 计算当前扫描线与上一个扫描线之间的面积
            // 面积 = y轴覆盖长度 × x轴距离
            double currentX = event.x;
            area += tree[1].len * (currentX - lastX);
            lastX = currentX;
            
            // 更新线段树中的覆盖情况
            int leftIndex = findIndex(event.y1);
            int rightIndex = findIndex(event.y2);
            updateTree(1, leftIndex, rightIndex - 1, event.flag);
        }
        
        return area;
    }
    
    /**
     * 构建线段树
     * @param node 当前节点编号
     * @param left 当前节点表示区间的左边界(离散化索引)
     * @param right 当前节点表示区间的右边界(离散化索引)
     */
    private void buildTree(int node, int left, int right) {
        tree[node] = new SegmentTreeNode(left, right);
        if (left == right) {
            return;
        }
        int mid = (left + right) / 2;
        buildTree(node * 2, left, mid);
        buildTree(node * 2 + 1, mid + 1, right);
    }
    
    /**
     * 更新线段树
     * @param node 当前节点编号
     * @param left 操作区间左边界(离散化索引)
     * @param right 操作区间右边界(离散化索引)
     * @param flag 操作值(+1表示添加覆盖，-1表示移除覆盖)
     */
    private void updateTree(int node, int left, int right, int flag) {
        // 如果操作区间与当前节点区间无交集，直接返回
        if (left > tree[node].right || right < tree[node].left) {
            return;
        }
        
        // 如果操作区间完全包含当前节点区间，更新覆盖次数
        if (left <= tree[node].left && tree[node].right <= right) {
            tree[node].cover += flag;
        } else {
            // 否则递归更新左右子树
            int mid = (tree[node].left + tree[node].right) / 2;
            if (left <= mid) {
                updateTree(node * 2, left, right, flag);
            }
            if (right > mid) {
                updateTree(node * 2 + 1, left, right, flag);
            }
        }
        
        // 更新当前节点的覆盖长度
        if (tree[node].cover > 0) {
            // 如果当前区间被覆盖，长度为实际坐标长度
            tree[node].len = y[tree[node].right + 1] - y[tree[node].left];
        } else {
            // 如果当前区间未被覆盖，长度为子区间的覆盖长度之和
            if (tree[node].left == tree[node].right) {
                tree[node].len = 0;
            } else {
                tree[node].len = tree[node * 2].len + tree[node * 2 + 1].len;
            }
        }
    }
    
    /**
     * 在离散化数组中查找值对应的索引
     * @param value 要查找的y坐标值
     * @return 离散化后的索引
     */
    private int findIndex(double value) {
        int left = 0, right = y.length - 1;
        while (left <= right) {
            int mid = (left + right) / 2;
            if (Math.abs(y[mid] - value) < 1e-9) {
                return mid;
            } else if (y[mid] < value) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return -1; // 理论上不会发生
    }
    
    /**
     * 测试用例
     * 验证calculateArea方法的正确性
     */
    public static void main(String[] args) {
        Code07_RectangleAreaUnion solution = new Code07_RectangleAreaUnion();
        
        // 测试用例1: 两个不重叠的矩形
        // 矩形1: (0,0)到(1,1)，面积1
        // 矩形2: (2,2)到(3,3)，面积1
        // 总面积: 2.0
        double[][] rectangles1 = {
            {0, 0, 1, 1},
            {2, 2, 3, 3}
        };
        double area1 = solution.calculateArea(rectangles1);
        System.out.println("测试用例1 面积: " + area1); // 预期: 2.0
        
        // 测试用例2: 两个重叠的矩形
        // 矩形1: (0,0)到(2,2)，面积4
        // 矩形2: (1,1)到(3,3)，面积4
        // 重叠区域: (1,1)到(2,2)，面积1
        // 总面积: 4 + 4 - 1 = 7.0
        double[][] rectangles2 = {
            {0, 0, 2, 2},
            {1, 1, 3, 3}
        };
        double area2 = solution.calculateArea(rectangles2);
        System.out.println("测试用例2 面积: " + area2); // 预期: 7.0
        
        // 测试用例3: 三个矩形，部分重叠
        // 矩形1: (0,0)到(2,2)
        // 矩形2: (1,1)到(3,3)
        // 矩形3: (0.5,0.5)到(1.5,1.5)
        // 总面积: 8.75
        double[][] rectangles3 = {
            {0, 0, 2, 2},
            {1, 1, 3, 3},
            {0.5, 0.5, 1.5, 1.5}
        };
        double area3 = solution.calculateArea(rectangles3);
        System.out.println("测试用例3 面积: " + area3); // 预期: 8.75
        
        // 测试用例4: 空矩形数组
        // 总面积: 0.0
        double[][] rectangles4 = {};
        double area4 = solution.calculateArea(rectangles4);
        System.out.println("测试用例4 面积: " + area4); // 预期: 0.0
        
        // 测试用例5: 单个矩形
        // 矩形: (0,0)到(5,5)
        // 总面积: 25.0
        double[][] rectangles5 = {{0, 0, 5, 5}};
        double area5 = solution.calculateArea(rectangles5);
        System.out.println("测试用例5 面积: " + area5); // 预期: 25.0
    }
}