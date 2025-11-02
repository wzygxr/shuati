package class115;

import java.util.*;

/**
 * 覆盖的面积 (HDU 1255)
 * 题目链接: http://acm.hdu.edu.cn/showproblem.php?pid=1255
 * 
 * 题目描述:
 * 给定多个矩形，计算被至少两个矩形覆盖的区域面积。
 * 每个矩形由其左下角坐标(x1, y1)和右上角坐标(x2, y2)表示。
 * 
 * 解题思路:
 * 使用扫描线算法结合线段树实现被至少两个矩形覆盖的区域面积计算。
 * 1. 将矩形拆分为上下两条边，作为扫描线事件
 * 2. 按y坐标排序所有事件
 * 3. 使用线段树维护x轴上的覆盖情况
 * 4. 线段树需要维护被覆盖一次和被覆盖两次的长度
 * 5. 扫描过程中计算相邻扫描线之间的面积
 * 
 * 时间复杂度: O(n log n) - 排序和线段树操作
 * 空间复杂度: O(n) - 存储事件和线段树
 * 
 * 工程化考量:
 * 1. 异常处理: 检查坐标合法性
 * 2. 边界条件: 处理坐标重复和边界情况
 * 3. 性能优化: 使用离散化减少线段树规模
 * 4. 可读性: 详细注释和模块化设计
 */
public class Code13_CoveredArea {
    
    // 线段树节点类
    static class SegmentTreeNode {
        int left, right; // 区间左右边界
        int cover; // 当前区间被覆盖的次数
        double len1; // 被覆盖一次的长度
        double len2; // 被覆盖两次及以上的长度
        
        SegmentTreeNode(int left, int right) {
            this.left = left;
            this.right = right;
            this.cover = 0;
            this.len1 = 0;
            this.len2 = 0;
        }
    }
    
    // 扫描线事件类
    static class Event implements Comparable<Event> {
        double x; // x坐标
        double y1, y2; // y坐标区间
        int flag; // 1表示矩形开始，-1表示矩形结束
        
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
     * 计算被至少两个矩形覆盖的区域面积
     * @param rectangles 矩形数组，每个矩形为[x1, y1, x2, y2]
     * @return 被至少两个矩形覆盖的区域面积
     */
    public double calculateCoveredArea(double[][] rectangles) {
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
            
            // 添加开始事件和结束事件
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
            double currentX = event.x;
            area += tree[1].len2 * (currentX - lastX);
            lastX = currentX;
            
            // 更新线段树
            int leftIndex = findIndex(event.y1);
            int rightIndex = findIndex(event.y2);
            updateTree(1, leftIndex, rightIndex - 1, event.flag);
        }
        
        return area;
    }
    
    // 构建线段树
    private void buildTree(int node, int left, int right) {
        tree[node] = new SegmentTreeNode(left, right);
        if (left == right) {
            return;
        }
        int mid = (left + right) / 2;
        buildTree(node * 2, left, mid);
        buildTree(node * 2 + 1, mid + 1, right);
    }
    
    // 更新线段树
    private void updateTree(int node, int left, int right, int flag) {
        if (left > tree[node].right || right < tree[node].left) {
            return;
        }
        
        if (left <= tree[node].left && tree[node].right <= right) {
            tree[node].cover += flag;
        } else {
            int mid = (tree[node].left + tree[node].right) / 2;
            if (left <= mid) {
                updateTree(node * 2, left, right, flag);
            }
            if (right > mid) {
                updateTree(node * 2 + 1, left, right, flag);
            }
        }
        
        // 更新当前节点的覆盖长度
        updateNodeLength(node);
    }
    
    // 更新节点的覆盖长度
    private void updateNodeLength(int node) {
        if (tree[node].cover >= 2) {
            // 被覆盖两次及以上
            tree[node].len2 = y[tree[node].right + 1] - y[tree[node].left];
            tree[node].len1 = 0;
        } else if (tree[node].cover == 1) {
            // 被覆盖一次
            tree[node].len1 = y[tree[node].right + 1] - y[tree[node].left];
            if (tree[node].left == tree[node].right) {
                tree[node].len2 = 0;
            } else {
                tree[node].len2 = tree[node * 2].len1 + tree[node * 2].len2 + 
                                 tree[node * 2 + 1].len1 + tree[node * 2 + 1].len2;
            }
        } else {
            // 没有被覆盖
            if (tree[node].left == tree[node].right) {
                tree[node].len1 = 0;
                tree[node].len2 = 0;
            } else {
                tree[node].len1 = tree[node * 2].len1 + tree[node * 2 + 1].len1;
                tree[node].len2 = tree[node * 2].len2 + tree[node * 2 + 1].len2;
            }
        }
    }
    
    // 在离散化数组中查找索引
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
    
    // 测试用例
    public static void main(String[] args) {
        Code13_CoveredArea solution = new Code13_CoveredArea();
        
        // 测试用例1: 两个重叠的矩形
        double[][] rectangles1 = {
            {0, 0, 2, 2},
            {1, 1, 3, 3}
        };
        double area1 = solution.calculateCoveredArea(rectangles1);
        System.out.println("测试用例1 覆盖面积: " + area1); // 预期: 1.0
        
        // 测试用例2: 三个矩形，部分重叠
        double[][] rectangles2 = {
            {0, 0, 3, 3},
            {1, 1, 4, 4},
            {2, 2, 5, 5}
        };
        double area2 = solution.calculateCoveredArea(rectangles2);
        System.out.println("测试用例2 覆盖面积: " + area2); // 预期: 4.0
        
        // 测试用例3: 四个矩形形成网格
        double[][] rectangles3 = {
            {0, 0, 2, 2},
            {0, 2, 2, 4},
            {2, 0, 4, 2},
            {2, 2, 4, 4}
        };
        double area3 = solution.calculateCoveredArea(rectangles3);
        System.out.println("测试用例3 覆盖面积: " + area3); // 预期: 0.0 (没有重叠)
        
        // 测试用例4: 三个矩形完全重叠
        double[][] rectangles4 = {
            {0, 0, 2, 2},
            {0, 0, 2, 2},
            {0, 0, 2, 2}
        };
        double area4 = solution.calculateCoveredArea(rectangles4);
        System.out.println("测试用例4 覆盖面积: " + area4); // 预期: 4.0
        
        // 测试用例5: 空数组
        double[][] rectangles5 = {};
        double area5 = solution.calculateCoveredArea(rectangles5);
        System.out.println("测试用例5 覆盖面积: " + area5); // 预期: 0.0
        
        // 测试用例6: 复杂重叠模式
        double[][] rectangles6 = {
            {0, 0, 4, 4},
            {1, 1, 3, 3},
            {2, 0, 5, 2},
            {0, 2, 2, 5}
        };
        double area6 = solution.calculateCoveredArea(rectangles6);
        System.out.println("测试用例6 覆盖面积: " + area6); // 预期: 复杂计算
        
        // 性能测试
        System.out.println("\\n=== 性能测试 ===");
        double[][] rectangles7 = new double[1000][4];
        Random random = new Random();
        for (int i = 0; i < 1000; i++) {
            double x1 = random.nextDouble() * 100;
            double y1 = random.nextDouble() * 100;
            double x2 = x1 + random.nextDouble() * 10 + 1;
            double y2 = y1 + random.nextDouble() * 10 + 1;
            rectangles7[i] = new double[]{x1, y1, x2, y2};
        }
        
        long startTime = System.currentTimeMillis();
        double area7 = solution.calculateCoveredArea(rectangles7);
        long endTime = System.currentTimeMillis();
        System.out.println("性能测试 覆盖面积: " + area7);
        System.out.println("执行时间: " + (endTime - startTime) + "ms");
    }
    
    /**
     * 调试辅助方法：打印线段树状态
     */
    private void printTreeState() {
        System.out.println("线段树状态:");
        for (int i = 1; i < tree.length && tree[i] != null; i++) {
            System.out.printf("节点%d: [%d,%d] cover=%d len1=%.2f len2=%.2f%n", 
                i, tree[i].left, tree[i].right, tree[i].cover, tree[i].len1, tree[i].len2);
        }
    }
}