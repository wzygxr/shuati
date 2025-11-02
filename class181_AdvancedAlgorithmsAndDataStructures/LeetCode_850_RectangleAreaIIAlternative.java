package class008_AdvancedAlgorithmsAndDataStructures.sweep_line_problems;

import java.util.*;

/**
 * LeetCode 850. 矩形面积 II (替代解法)
 * 
 * 题目来源：https://leetcode.cn/problems/rectangle-area-ii/
 * 
 * 题目描述：
 * 我们给出了一个（轴对齐的）矩形列表 rectangles。
 * 对于每个矩形，rectangle[i] = [x1, y1, x2, y2]，
 * 其中 (x1, y1) 是矩形左下角的坐标，(x2, y2) 是右上角的坐标。
 * 找出平面中所有矩形所覆盖的总面积。任何被两个或多个矩形覆盖的区域应只计算一次。
 * 由于答案可能太大，返回结果对 10^9 + 7 取模。
 * 
 * 算法思路：
 * 这个问题可以通过以下方法解决：
 * 1. 扫描线算法：按x坐标扫描，计算每个垂直条带的面积
 * 2. 坐标压缩：压缩y坐标以减少计算量
 * 3. 线段树：维护y轴上的覆盖长度
 * 
 * 使用扫描线算法的方法：
 * 1. 创建事件列表：矩形的左边界和右边界
 * 2. 按x坐标排序事件
 * 3. 扫描所有事件，维护当前y轴上的覆盖长度
 * 4. 计算每个条带的面积并累加
 * 
 * 时间复杂度：
 * - 扫描线算法：O(n^2)
 * - 坐标压缩+扫描线：O(n^2 log n)
 * - 线段树优化：O(n log n)
 * - 空间复杂度：O(n)
 * 
 * 应用场景：
 * 1. 计算机图形学：区域填充和遮挡计算
 * 2. 地理信息系统：区域面积计算
 * 3. CAD系统：图形区域分析
 * 
 * 相关题目：
 * 1. LeetCode 218. 天际线问题
 * 2. LeetCode 56. 合并区间
 * 3. LeetCode 1094. 拼车
 */
public class LeetCode_850_RectangleAreaIIAlternative {
    
    private static final int MOD = 1000000007;
    
    /**
     * 方法1：扫描线算法（坐标压缩）
     * 时间复杂度：O(n^2 log n)
     * 空间复杂度：O(n)
     * @param rectangles 矩形数组
     * @return 覆盖的总面积
     */
    public static int rectangleAreaCoordinateCompression(int[][] rectangles) {
        int n = rectangles.length;
        if (n == 0) return 0;
        
        // 收集所有y坐标用于坐标压缩
        Set<Integer> yCoords = new HashSet<>();
        for (int[] rect : rectangles) {
            yCoords.add(rect[1]); // y1
            yCoords.add(rect[3]); // y2
        }
        
        // 排序y坐标
        List<Integer> sortedY = new ArrayList<>(yCoords);
        Collections.sort(sortedY);
        int ySize = sortedY.size();
        
        // 创建y坐标到索引的映射
        Map<Integer, Integer> yIndexMap = new HashMap<>();
        for (int i = 0; i < ySize; i++) {
            yIndexMap.put(sortedY.get(i), i);
        }
        
        // 创建事件列表：[x坐标, 矩形索引, 类型(1表示进入，-1表示离开)]
        List<int[]> events = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            int[] rect = rectangles[i];
            events.add(new int[]{rect[0], i, 1});  // 左边界
            events.add(new int[]{rect[2], i, -1}); // 右边界
        }
        
        // 按x坐标排序事件
        events.sort((a, b) -> {
            if (a[0] != b[0]) {
                return Integer.compare(a[0], b[0]);
            }
            return Integer.compare(a[2], b[2]); // 离开事件优先于进入事件
        });
        
        // 记录每个y区间被覆盖的次数
        int[] coverageCount = new int[ySize];
        
        long totalArea = 0;
        int prevX = 0;
        
        // 扫描所有事件
        for (int[] event : events) {
            int x = event[0];
            int rectIndex = event[1];
            int type = event[2];
            
            // 计算前一个条带的面积
            if (x > prevX) {
                long height = 0;
                // 计算y轴上的覆盖长度
                for (int i = 0; i < ySize - 1; i++) {
                    if (coverageCount[i] > 0) {
                        height += sortedY.get(i + 1) - sortedY.get(i);
                    }
                }
                totalArea = (totalArea + (long)(x - prevX) * height) % MOD;
                prevX = x;
            }
            
            // 更新覆盖计数
            int[] rect = rectangles[rectIndex];
            int y1Index = yIndexMap.get(rect[1]);
            int y2Index = yIndexMap.get(rect[3]);
            
            for (int i = y1Index; i < y2Index; i++) {
                coverageCount[i] += type;
            }
        }
        
        return (int) totalArea;
    }
    
    /**
     * 方法2：扫描线算法（线段树优化）
     * 时间复杂度：O(n log n)
     * 空间复杂度：O(n)
     * @param rectangles 矩形数组
     * @return 覆盖的总面积
     */
    public static int rectangleAreaSegmentTree(int[][] rectangles) {
        int n = rectangles.length;
        if (n == 0) return 0;
        
        // 收集所有y坐标用于坐标压缩
        Set<Integer> yCoords = new HashSet<>();
        for (int[] rect : rectangles) {
            yCoords.add(rect[1]); // y1
            yCoords.add(rect[3]); // y2
        }
        
        // 排序y坐标
        List<Integer> sortedY = new ArrayList<>(yCoords);
        Collections.sort(sortedY);
        
        // 创建事件列表：[x坐标, y1, y2, 类型(1表示进入，-1表示离开)]
        List<int[]> events = new ArrayList<>();
        for (int[] rect : rectangles) {
            events.add(new int[]{rect[0], rect[1], rect[3], 1});  // 左边界
            events.add(new int[]{rect[2], rect[1], rect[3], -1}); // 右边界
        }
        
        // 按x坐标排序事件
        events.sort((a, b) -> {
            if (a[0] != b[0]) {
                return Integer.compare(a[0], b[0]);
            }
            return Integer.compare(a[3], b[3]); // 离开事件优先于进入事件
        });
        
        // 使用线段树维护y轴上的覆盖长度
        SegmentTree segTree = new SegmentTree(sortedY);
        
        long totalArea = 0;
        int prevX = 0;
        
        // 扫描所有事件
        for (int[] event : events) {
            int x = event[0];
            int y1 = event[1];
            int y2 = event[2];
            int type = event[3];
            
            // 计算前一个条带的面积
            if (x > prevX) {
                totalArea = (totalArea + (long)(x - prevX) * segTree.query()) % MOD;
                prevX = x;
            }
            
            // 更新线段树
            segTree.update(y1, y2, type);
        }
        
        return (int) totalArea;
    }
    
    /**
     * 线段树类（用于维护y轴上的覆盖长度）
     */
    static class SegmentTree {
        private List<Integer> coords;
        private int[] count;
        private long[] length;
        
        public SegmentTree(List<Integer> coords) {
            this.coords = coords;
            int n = coords.size();
            this.count = new int[n * 4];
            this.length = new long[n * 4];
        }
        
        public void update(int y1, int y2, int delta) {
            update(1, 0, coords.size() - 1, y1, y2, delta);
        }
        
        private void update(int node, int start, int end, int y1, int y2, int delta) {
            if (start >= end) return;
            
            if (coords.get(start) == y1 && coords.get(end) == y2) {
                count[node] += delta;
            } else {
                int mid = (start + end) / 2;
                int yMid = coords.get(mid);
                
                if (y2 <= yMid) {
                    update(node * 2, start, mid, y1, y2, delta);
                } else if (y1 >= yMid) {
                    update(node * 2 + 1, mid, end, y1, y2, delta);
                } else {
                    update(node * 2, start, mid, y1, yMid, delta);
                    update(node * 2 + 1, mid, end, yMid, y2, delta);
                }
            }
            
            // 更新当前节点的覆盖长度
            if (count[node] > 0) {
                length[node] = coords.get(end) - coords.get(start);
            } else {
                length[node] = length[node * 2] + length[node * 2 + 1];
            }
        }
        
        public long query() {
            return length[1];
        }
    }
    
    /**
     * 方法3：暴力解法（用于对比）
     * 时间复杂度：O(n^3)
     * 空间复杂度：O(n^2)
     * @param rectangles 矩形数组
     * @return 覆盖的总面积
     */
    public static int rectangleAreaBruteForce(int[][] rectangles) {
        // 找到所有可能的x和y坐标
        Set<Integer> xCoords = new HashSet<>();
        Set<Integer> yCoords = new HashSet<>();
        
        for (int[] rect : rectangles) {
            xCoords.add(rect[0]);
            xCoords.add(rect[2]);
            yCoords.add(rect[1]);
            yCoords.add(rect[3]);
        }
        
        // 排序坐标
        List<Integer> sortedX = new ArrayList<>(xCoords);
        List<Integer> sortedY = new ArrayList<>(yCoords);
        Collections.sort(sortedX);
        Collections.sort(sortedY);
        
        long totalArea = 0;
        
        // 检查每个小矩形是否被任何输入矩形覆盖
        for (int i = 0; i < sortedX.size() - 1; i++) {
            for (int j = 0; j < sortedY.size() - 1; j++) {
                int x1 = sortedX.get(i);
                int x2 = sortedX.get(i + 1);
                int y1 = sortedY.get(j);
                int y2 = sortedY.get(j + 1);
                
                // 检查是否被任何矩形覆盖
                for (int[] rect : rectangles) {
                    if (rect[0] <= x1 && x2 <= rect[2] && rect[1] <= y1 && y2 <= rect[3]) {
                        totalArea = (totalArea + (long)(x2 - x1) * (y2 - y1)) % MOD;
                        break;
                    }
                }
            }
        }
        
        return (int) totalArea;
    }
    
    /**
     * 测试函数
     */
    public static void main(String[] args) {
        System.out.println("=== 测试 LeetCode 850. 矩形面积 II (替代解法) ===");
        
        // 测试用例1
        int[][] rectangles1 = {{1,1,3,3},{3,1,4,2},{3,2,4,4},{1,3,2,4},{2,3,3,4}};
        System.out.println("测试用例1:");
        System.out.println("矩形: [[1,1,3,3],[3,1,4,2],[3,2,4,4],[1,3,2,4],[2,3,3,4]]");
        System.out.println("坐标压缩解法结果: " + rectangleAreaCoordinateCompression(rectangles1));
        System.out.println("线段树解法结果: " + rectangleAreaSegmentTree(rectangles1));
        System.out.println("暴力解法结果: " + rectangleAreaBruteForce(rectangles1));
        System.out.println("期望结果: 6");
        System.out.println();
        
        // 测试用例2
        int[][] rectangles2 = {{1,1,2,2},{2,2,3,3}};
        System.out.println("测试用例2:");
        System.out.println("矩形: [[1,1,2,2],[2,2,3,3]]");
        System.out.println("坐标压缩解法结果: " + rectangleAreaCoordinateCompression(rectangles2));
        System.out.println("线段树解法结果: " + rectangleAreaSegmentTree(rectangles2));
        System.out.println("暴力解法结果: " + rectangleAreaBruteForce(rectangles2));
        System.out.println("期望结果: 2");
        System.out.println();
        
        // 测试用例3
        int[][] rectangles3 = {{0,0,2,2},{1,0,2,3},{1,0,3,1}};
        System.out.println("测试用例3:");
        System.out.println("矩形: [[0,0,2,2],[1,0,2,3],[1,0,3,1]]");
        System.out.println("坐标压缩解法结果: " + rectangleAreaCoordinateCompression(rectangles3));
        System.out.println("线段树解法结果: " + rectangleAreaSegmentTree(rectangles3));
        System.out.println("暴力解法结果: " + rectangleAreaBruteForce(rectangles3));
        System.out.println("期望结果: 6");
        System.out.println();
        
        // 性能测试
        System.out.println("=== 性能测试 ===");
        Random random = new Random(42);
        int n = 100;
        int[][] rectangles = new int[n][4];
        for (int i = 0; i < n; i++) {
            int x1 = random.nextInt(1000);
            int y1 = random.nextInt(1000);
            int x2 = x1 + random.nextInt(100) + 1;
            int y2 = y1 + random.nextInt(100) + 1;
            rectangles[i] = new int[]{x1, y1, x2, y2};
        }
        
        long startTime = System.nanoTime();
        int result1 = rectangleAreaCoordinateCompression(rectangles);
        long endTime = System.nanoTime();
        System.out.println("坐标压缩解法处理" + n + "个矩形时间: " + (endTime - startTime) / 1_000_000.0 + " ms, 结果: " + result1);
        
        startTime = System.nanoTime();
        int result2 = rectangleAreaSegmentTree(rectangles);
        endTime = System.nanoTime();
        System.out.println("线段树解法处理" + n + "个矩形时间: " + (endTime - startTime) / 1_000_000.0 + " ms, 结果: " + result2);
    }
}