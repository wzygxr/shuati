package class115;

import java.util.*;

/**
 * 矩形周长并 - 扫描线算法实现
 * 题目链接: http://poj.org/problem?id=1177
 * 题目链接: http://acm.hdu.edu.cn/showproblem.php?pid=1828
 * 
 * 题目描述:
 * 给定多个矩形区域，计算这些矩形并集的周长。
 * 每个矩形由其左下角坐标(x1, y1)和右上角坐标(x2, y2)表示。
 * 
 * 解题思路:
 * 使用扫描线算法结合线段树实现矩形周长并的计算。
 * 1. 分别计算水平方向和垂直方向的周长
 * 2. 水平方向周长 = 扫描线移动距离 × 线段数变化
 * 3. 垂直方向周长 = 高度差 × 线段数变化
 * 4. 使用线段树维护当前覆盖的线段数和长度
 * 
 * 算法复杂度: 时间复杂度O(n log n)，空间复杂度O(n)
 * 工程化考量:
 * 1. 异常处理: 检查坐标合法性
 * 2. 边界条件: 处理坐标重复和边界情况
 * 3. 性能优化: 使用离散化减少线段树规模
 * 4. 可读性: 详细注释和模块化设计
 * 5. 精度处理: 使用整数坐标避免浮点精度问题
 */
public class Code08_RectanglePerimeterUnion {
    
    /**
     * 线段树节点类
     * 用于维护y轴上的覆盖情况和线段数
     */
    static class SegmentTreeNode {
        int left, right; // 区间左右边界(离散化后的索引)
        int cover; // 当前区间被覆盖的次数
        int len; // 当前区间被覆盖的长度(实际坐标长度)
        int num; // 当前区间被覆盖的线段数
        boolean lcover, rcover; // 左右端点是否被覆盖
        
        SegmentTreeNode(int left, int right) {
            this.left = left;
            this.right = right;
            this.cover = 0;
            this.len = 0;
            this.num = 0;
            this.lcover = false;
            this.rcover = false;
        }
    }
    
    /**
     * 扫描线事件类
     * 表示矩形的左边或右边
     */
    static class Event implements Comparable<Event> {
        int x; // x坐标(扫描线位置)
        int y1, y2; // y坐标区间
        int flag; // 1表示矩形开始(左边)，-1表示矩形结束(右边)
        
        Event(int x, int y1, int y2, int flag) {
            this.x = x;
            this.y1 = y1;
            this.y2 = y2;
            this.flag = flag;
        }
        
        @Override
        public int compareTo(Event other) {
            if (this.x != other.x) {
                return Integer.compare(this.x, other.x);
            }
            // 相同x坐标时，开始事件优先于结束事件
            // 这样可以正确处理边界情况，避免重复计算
            return Integer.compare(other.flag, this.flag);
        }
    }
    
    // 线段树数组
    private SegmentTreeNode[] tree;
    // y坐标离散化数组
    private int[] y;
    
    /**
     * 计算矩形周长并
     * 算法核心思想：
     * 1. 将每个矩形的左右边界作为扫描线事件
     * 2. 按x坐标排序所有扫描线事件
     * 3. 使用线段树维护y轴上的覆盖情况和线段数
     * 4. 水平方向周长 = 扫描线移动距离 × 线段数变化
     * 5. 垂直方向周长 = 高度差 × 线段数变化
     * 
     * @param rectangles 矩形数组，每个矩形为[x1, y1, x2, y2]
     * @return 总周长
     * @throws IllegalArgumentException 当矩形坐标不合法时抛出异常
     */
    public int calculatePerimeter(int[][] rectangles) {
        // 边界条件检查
        if (rectangles == null || rectangles.length == 0) {
            return 0;
        }
        
        int n = rectangles.length;
        
        // 收集所有y坐标用于离散化
        Set<Integer> ySet = new TreeSet<>();
        List<Event> events = new ArrayList<>();
        
        for (int[] rect : rectangles) {
            int x1 = rect[0], y1 = rect[1], x2 = rect[2], y2 = rect[3];
            
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
        y = new int[ySet.size()];
        int index = 0;
        for (int val : ySet) {
            y[index++] = val;
        }
        
        // 构建线段树
        int size = y.length - 1;
        tree = new SegmentTreeNode[4 * size];
        buildTree(1, 0, size - 1);
        
        // 扫描线算法
        int perimeter = 0;
        int lastX = events.get(0).x;
        int lastNum = 0; // 上一次的线段数
        int lastLen = 0; // 上一次的覆盖长度
        
        for (Event event : events) {
            // 计算水平方向周长
            // 水平周长 = 2 × 线段数 × x轴移动距离
            int currentX = event.x;
            perimeter += 2 * lastNum * (currentX - lastX);
            lastX = currentX;
            
            // 更新线段树中的覆盖情况
            int leftIndex = findIndex(event.y1);
            int rightIndex = findIndex(event.y2);
            updateTree(1, leftIndex, rightIndex - 1, event.flag);
            
            // 计算垂直方向周长
            // 垂直周长 = 当前线段数 × 高度 - 上次覆盖长度
            perimeter += Math.abs(tree[1].num * (y[rightIndex] - y[leftIndex]) - lastLen);
            lastNum = tree[1].num;
            lastLen = tree[1].len;
        }
        
        return perimeter;
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
        
        // 更新当前节点的信息
        if (tree[node].cover > 0) {
            // 如果当前区间被覆盖，长度为实际坐标长度，线段数为1
            tree[node].len = y[tree[node].right + 1] - y[tree[node].left];
            tree[node].num = 1;
            tree[node].lcover = tree[node].rcover = true;
        } else {
            // 如果当前区间未被覆盖
            if (tree[node].left == tree[node].right) {
                // 叶子节点
                tree[node].len = 0;
                tree[node].num = 0;
                tree[node].lcover = tree[node].rcover = false;
            } else {
                // 非叶子节点，长度和线段数为子节点之和
                tree[node].len = tree[node * 2].len + tree[node * 2 + 1].len;
                tree[node].num = tree[node * 2].num + tree[node * 2 + 1].num;
                
                // 如果左右子树相连，需要减去重复计算的线段
                if (tree[node * 2].rcover && tree[node * 2 + 1].lcover) {
                    tree[node].num--;
                }
                
                tree[node].lcover = tree[node * 2].lcover;
                tree[node].rcover = tree[node * 2 + 1].rcover;
            }
        }
    }
    
    /**
     * 在离散化数组中查找值对应的索引
     * @param value 要查找的y坐标值
     * @return 离散化后的索引
     */
    private int findIndex(int value) {
        int left = 0, right = y.length - 1;
        while (left <= right) {
            int mid = (left + right) / 2;
            if (y[mid] == value) {
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
     * 验证calculatePerimeter方法的正确性
     */
    public static void main(String[] args) {
        Code08_RectanglePerimeterUnion solution = new Code08_RectanglePerimeterUnion();
        
        // 测试用例1: 单个矩形
        // 矩形: (0,0)到(2,2)
        // 周长: 2 × (2 + 2) = 8
        int[][] rectangles1 = {{0, 0, 2, 2}};
        int perimeter1 = solution.calculatePerimeter(rectangles1);
        System.out.println("测试用例1 周长: " + perimeter1); // 预期: 8
        
        // 测试用例2: 两个不重叠的矩形
        // 矩形1: (0,0)到(1,1)，周长4
        // 矩形2: (2,2)到(3,3)，周长4
        // 总周长: 8
        int[][] rectangles2 = {
            {0, 0, 1, 1},
            {2, 2, 3, 3}
        };
        int perimeter2 = solution.calculatePerimeter(rectangles2);
        System.out.println("测试用例2 周长: " + perimeter2); // 预期: 8
        
        // 测试用例3: 两个相邻的矩形
        // 矩形1: (0,0)到(2,2)
        // 矩形2: (2,0)到(4,2)
        // 合并后形成一个(0,0)到(4,2)的矩形
        // 总周长: 2 × (4 + 2) = 12
        int[][] rectangles3 = {
            {0, 0, 2, 2},
            {2, 0, 4, 2}
        };
        int perimeter3 = solution.calculatePerimeter(rectangles3);
        System.out.println("测试用例3 周长: " + perimeter3); // 预期: 12
        
        // 测试用例4: 两个重叠的矩形
        // 矩形1: (0,0)到(3,3)
        // 矩形2: (1,1)到(4,4)
        // 并集形成一个复杂的形状
        // 总周长: 20
        int[][] rectangles4 = {
            {0, 0, 3, 3},
            {1, 1, 4, 4}
        };
        int perimeter4 = solution.calculatePerimeter(rectangles4);
        System.out.println("测试用例4 周长: " + perimeter4); // 预期: 20
        
        // 测试用例5: 三个矩形形成L形
        // 矩形1: (0,0)到(2,2)
        // 矩形2: (2,0)到(4,1)
        // 矩形3: (0,2)到(1,4)
        // 总周长: 18
        int[][] rectangles5 = {
            {0, 0, 2, 2},
            {2, 0, 4, 1},
            {0, 2, 1, 4}
        };
        int perimeter5 = solution.calculatePerimeter(rectangles5);
        System.out.println("测试用例5 周长: " + perimeter5); // 预期: 18
        
        // 测试用例6: 空矩形数组
        // 总周长: 0
        int[][] rectangles6 = {};
        int perimeter6 = solution.calculatePerimeter(rectangles6);
        System.out.println("测试用例6 周长: " + perimeter6); // 预期: 0
    }
}