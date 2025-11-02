package class115;

import java.util.*;

/**
 * 矩形面积 II - 扫描线算法实现
 * 题目链接: https://leetcode.cn/problems/rectangle-area-ii/
 * 
 * 题目描述:
 * 我们给出了一个（轴对齐的）矩形列表 rectangles。
 * 对于 rectangle[i] = [x1, y1, x2, y2]，其中 (x1, y1) 是矩形 i 左下角的坐标，
 * (x2, y2) 是该矩形右上角的坐标。
 * 找出平面中所有矩形叠加覆盖后的总面积。
 * 由于答案可能太大，请返回它对 10^9 + 7 取模的结果。
 * 
 * 解题思路:
 * 使用扫描线算法结合线段树实现矩形面积并的计算。
 * 1. 将矩形拆分为左右两条边，作为扫描线事件
 * 2. 按x坐标排序所有事件
 * 3. 使用线段树维护y轴上的覆盖情况
 * 4. 扫描过程中计算相邻扫描线之间的面积
 * 5. 结果对 10^9 + 7 取模
 * 
 * 算法复杂度: 时间复杂度O(n log n)，空间复杂度O(n)
 * 工程化考量:
 * 1. 异常处理: 检查坐标合法性
 * 2. 边界条件: 处理坐标重复和边界情况
 * 3. 性能优化: 使用离散化减少线段树规模
 * 4. 数值处理: 大数取模运算
 * 5. 可读性: 详细注释和模块化设计
 * 6. 提供了两种实现方式：基本版本和优化版本
 */
public class Code11_RectangleAreaII {
    
    private static final int MOD = 1000000007;
    
    /**
     * 线段树节点类
     * 用于维护y轴上的覆盖情况
     */
    static class SegmentTreeNode {
        int left, right; // 区间左右边界(离散化后的索引)
        int cover; // 当前区间被覆盖的次数
        int len; // 当前区间被覆盖的长度(实际坐标长度)
        
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
            return Integer.compare(this.x, other.x);
        }
    }
    
    // 线段树数组
    private SegmentTreeNode[] tree;
    // y坐标离散化数组
    private int[] y;
    
    /**
     * 计算矩形面积并（取模）
     * 算法核心思想：
     * 1. 将每个矩形的左右边界作为扫描线事件
     * 2. 按x坐标排序所有扫描线事件
     * 3. 使用线段树维护y轴上的覆盖长度
     * 4. 相邻扫描线之间的面积 = y轴覆盖长度 × x轴距离
     * 5. 结果对 10^9 + 7 取模
     * 
     * @param rectangles 矩形数组，每个矩形为 [x1, y1, x2, y2]
     * @return 总面积对 10^9 + 7 取模的结果
     */
    public int rectangleArea(int[][] rectangles) {
        // 边界条件检查
        if (rectangles == null || rectangles.length == 0) {
            return 0;
        }
        
        int n = rectangles.length;
        
        // 收集所有y坐标用于离散化
        Set<Integer> ySet = new TreeSet<>();
        List<Event> events = new ArrayList<>();
        
        for (int[] rect : rectangles) {
            if (rect.length != 4) {
                throw new IllegalArgumentException("Invalid rectangle format");
            }
            
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
        long area = 0; // 使用long防止溢出
        int lastX = events.get(0).x;
        
        for (Event event : events) {
            // 计算当前扫描线与上一个扫描线之间的面积
            // 面积 = y轴覆盖长度 × x轴距离
            int currentX = event.x;
            long width = currentX - lastX;
            long height = tree[1].len;
            
            // 累加面积，注意取模
            area = (area + width * height) % MOD;
            lastX = currentX;
            
            // 更新线段树中的覆盖情况
            int leftIndex = findIndex(event.y1);
            int rightIndex = findIndex(event.y2);
            updateTree(1, leftIndex, rightIndex - 1, event.flag);
        }
        
        return (int) area;
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
     * 优化版本：使用更高效的离散化和线段树实现
     * 通过使用数组替代对象来提高性能
     * 
     * @param rectangles 矩形数组，每个矩形为 [x1, y1, x2, y2]
     * @return 总面积对 10^9 + 7 取模的结果
     */
    public int rectangleAreaOptimized(int[][] rectangles) {
        if (rectangles == null || rectangles.length == 0) {
            return 0;
        }
        
        // 使用更紧凑的数据结构
        Set<Integer> ySet = new TreeSet<>();
        List<int[]> events = new ArrayList<>();
        
        for (int[] rect : rectangles) {
            int x1 = rect[0], y1 = rect[1], x2 = rect[2], y2 = rect[3];
            
            ySet.add(y1);
            ySet.add(y2);
            
            // 使用int[]数组表示事件，[x坐标, y下界, y上界, 标志]
            events.add(new int[]{x1, y1, y2, 1});
            events.add(new int[]{x2, y1, y2, -1});
        }
        
        // 按x坐标排序
        events.sort((a, b) -> Integer.compare(a[0], b[0]));
        
        // 离散化y坐标
        int[] yArr = new int[ySet.size()];
        int idx = 0;
        for (int val : ySet) {
            yArr[idx++] = val;
        }
        
        // 使用数组实现线段树（更高效）
        int n = yArr.length - 1;
        // cover数组记录每个节点的覆盖次数
        int[] cover = new int[4 * n];
        // len数组记录每个节点的覆盖长度
        int[] len = new int[4 * n];
        
        // 构建线段树
        buildTreeOptimized(cover, len, yArr, 1, 0, n - 1);
        
        long area = 0;
        int lastX = events.get(0)[0];
        
        for (int[] event : events) {
            int x = event[0], y1 = event[1], y2 = event[2], flag = event[3];
            
            // 计算当前扫描线与上一个扫描线之间的面积
            long width = x - lastX;
            area = (area + width * len[1]) % MOD;
            lastX = x;
            
            // 更新线段树
            int leftIdx = findIndexOptimized(yArr, y1);
            int rightIdx = findIndexOptimized(yArr, y2);
            updateTreeOptimized(cover, len, yArr, 1, 0, n - 1, leftIdx, rightIdx - 1, flag);
        }
        
        return (int) area;
    }
    
    /**
     * 构建线段树(优化版本)
     * @param cover 覆盖次数数组
     * @param len 覆盖长度数组
     * @param y 离散化y坐标数组
     * @param node 当前节点编号
     * @param left 当前节点表示区间的左边界
     * @param right 当前节点表示区间的右边界
     */
    private void buildTreeOptimized(int[] cover, int[] len, int[] y, int node, int left, int right) {
        if (left == right) {
            return;
        }
        int mid = (left + right) / 2;
        buildTreeOptimized(cover, len, y, node * 2, left, mid);
        buildTreeOptimized(cover, len, y, node * 2 + 1, mid + 1, right);
    }
    
    /**
     * 更新线段树(优化版本)
     * @param cover 覆盖次数数组
     * @param len 覆盖长度数组
     * @param y 离散化y坐标数组
     * @param node 当前节点编号
     * @param left 当前节点表示区间的左边界
     * @param right 当前节点表示区间的右边界
     * @param l 操作区间左边界
     * @param r 操作区间右边界
     * @param flag 操作值(+1表示添加覆盖，-1表示移除覆盖)
     */
    private void updateTreeOptimized(int[] cover, int[] len, int[] y, int node, int left, int right, 
                                    int l, int r, int flag) {
        // 如果操作区间与当前节点区间无交集，直接返回
        if (l > right || r < left) {
            return;
        }
        
        // 如果操作区间完全包含当前节点区间，更新覆盖次数
        if (l <= left && right <= r) {
            cover[node] += flag;
        } else {
            // 否则递归更新左右子树
            int mid = (left + right) / 2;
            if (l <= mid) {
                updateTreeOptimized(cover, len, y, node * 2, left, mid, l, r, flag);
            }
            if (r > mid) {
                updateTreeOptimized(cover, len, y, node * 2 + 1, mid + 1, right, l, r, flag);
            }
        }
        
        // 更新当前节点的覆盖长度
        if (cover[node] > 0) {
            // 如果当前区间被覆盖，长度为实际坐标长度
            len[node] = y[right + 1] - y[left];
        } else {
            // 如果当前区间未被覆盖，长度为子区间的覆盖长度之和
            if (left == right) {
                len[node] = 0;
            } else {
                len[node] = len[node * 2] + len[node * 2 + 1];
            }
        }
    }
    
    /**
     * 在离散化数组中查找值对应的索引(优化版本)
     * @param y 离散化y坐标数组
     * @param value 要查找的y坐标值
     * @return 离散化后的索引
     */
    private int findIndexOptimized(int[] y, int value) {
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
        return -1;
    }
    
    /**
     * 测试用例
     * 验证rectangleArea和rectangleAreaOptimized方法的正确性
     */
    public static void main(String[] args) {
        Code11_RectangleAreaII solution = new Code11_RectangleAreaII();
        
        // 测试用例1: 标准情况
        // 矩形1: (0,0)到(2,2)，面积4
        // 矩形2: (1,1)到(3,3)，面积4
        // 重叠区域: (1,1)到(2,2)，面积1
        // 总面积: 4 + 4 - 1 = 7
        int[][] rectangles1 = {
            {0, 0, 2, 2},
            {1, 1, 3, 3}
        };
        int result1 = solution.rectangleArea(rectangles1);
        System.out.println("测试用例1 面积: " + result1); // 预期: 7
        
        // 测试用例2: 单个矩形
        // 矩形: (0,0)到(1,1)
        // 总面积: 1
        int[][] rectangles2 = {{0, 0, 1, 1}};
        int result2 = solution.rectangleArea(rectangles2);
        System.out.println("测试用例2 面积: " + result2); // 预期: 1
        
        // 测试用例3: 三个矩形
        // 矩形1: (0,0)到(3,3)
        // 矩形2: (2,2)到(5,5)
        // 矩形3: (1,1)到(4,4)
        // 总面积: 27
        int[][] rectangles3 = {
            {0, 0, 3, 3},
            {2, 2, 5, 5},
            {1, 1, 4, 4}
        };
        int result3 = solution.rectangleArea(rectangles3);
        System.out.println("测试用例3 面积: " + result3); // 预期: 27
        
        // 测试用例4: 空数组
        // 总面积: 0
        int[][] rectangles4 = {};
        int result4 = solution.rectangleArea(rectangles4);
        System.out.println("测试用例4 面积: " + result4); // 预期: 0
        
        // 测试用例5: 大数测试
        // 矩形: (0,0)到(1000000000,1000000000)
        // 面积: 1000000000 * 1000000000 = 1000000000000000000
        // 取模后: 49
        int[][] rectangles5 = {
            {0, 0, 1000000000, 1000000000}
        };
        int result5 = solution.rectangleArea(rectangles5);
        System.out.println("测试用例5 面积: " + result5); // 预期: 49 (取模后)
        
        // 测试优化版本
        System.out.println("\n=== 优化版本测试 ===");
        int result1Opt = solution.rectangleAreaOptimized(rectangles1);
        System.out.println("测试用例1 优化版本面积: " + result1Opt);
        
        int result3Opt = solution.rectangleAreaOptimized(rectangles3);
        System.out.println("测试用例3 优化版本面积: " + result3Opt);
        
        // 性能测试：大量矩形
        System.out.println("\n=== 性能测试 ===");
        int[][] rectangles6 = new int[1000][4];
        Random random = new Random();
        for (int i = 0; i < 1000; i++) {
            int x1 = random.nextInt(1000);
            int y1 = random.nextInt(1000);
            int x2 = x1 + random.nextInt(100) + 1;
            int y2 = y1 + random.nextInt(100) + 1;
            rectangles6[i] = new int[]{x1, y1, x2, y2};
        }
        
        long startTime = System.currentTimeMillis();
        int result6 = solution.rectangleArea(rectangles6);
        long endTime = System.currentTimeMillis();
        System.out.println("性能测试 面积: " + result6);
        System.out.println("执行时间: " + (endTime - startTime) + "ms");
    }
}