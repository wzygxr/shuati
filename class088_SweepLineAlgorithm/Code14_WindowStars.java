package class115;

import java.util.*;

/**
 * 窗口的星星 (洛谷 P1502)
 * 题目链接: https://www.luogu.com.cn/problem/P1502
 * 
 * 题目描述:
 * 给定一些星星的位置和亮度，求一个固定大小的窗口内星星亮度总和的最大值。
 * 窗口的边界不算在内部（即边界上的星星不计入亮度总和）。
 * 
 * 解题思路:
 * 使用扫描线算法结合线段树实现窗口内星星亮度总和的最大值计算。
 * 1. 将每个星星转化为一个矩形区域，表示窗口右上角可以放置的位置范围
 * 2. 使用扫描线算法处理这些矩形区域
 * 3. 线段树维护当前扫描线上的亮度总和
 * 4. 求线段树中的最大值
 * 
 * 时间复杂度: O(n log n) - 排序和线段树操作
 * 空间复杂度: O(n) - 存储事件和线段树
 * 
 * 工程化考量:
 * 1. 异常处理: 检查输入数据合法性
 * 2. 边界条件: 处理窗口边界和星星位置
 * 3. 性能优化: 使用离散化减少线段树规模
 * 4. 可读性: 详细注释和模块化设计
 */
public class Code14_WindowStars {
    
    // 星星类
    static class Star {
        int x, y; // 星星坐标
        int light; // 星星亮度
        
        Star(int x, int y, int light) {
            this.x = x;
            this.y = y;
            this.light = light;
        }
    }
    
    // 扫描线事件类
    static class Event implements Comparable<Event> {
        int x; // x坐标
        int y1, y2; // y坐标区间
        int light; // 亮度变化（正数表示增加，负数表示减少）
        
        Event(int x, int y1, int y2, int light) {
            this.x = x;
            this.y1 = y1;
            this.y2 = y2;
            this.light = light;
        }
        
        @Override
        public int compareTo(Event other) {
            if (this.x != other.x) {
                return Integer.compare(this.x, other.x);
            }
            // 相同x坐标时，增加事件优先于减少事件
            return Integer.compare(other.light, this.light);
        }
    }
    
    // 线段树节点类（支持区间加和区间最大值查询）
    static class SegmentTreeNode {
        int left, right; // 区间左右边界
        int max; // 区间最大值
        int lazy; // 懒标记
        
        SegmentTreeNode(int left, int right) {
            this.left = left;
            this.right = right;
            this.max = 0;
            this.lazy = 0;
        }
    }
    
    // 线段树数组
    private SegmentTreeNode[] tree;
    // y坐标离散化数组
    private int[] y;
    
    /**
     * 计算窗口内星星亮度总和的最大值
     * @param stars 星星数组，每个星星为[x, y, light]
     * @param w 窗口宽度
     * @param h 窗口高度
     * @return 最大亮度总和
     */
    public int maxWindowLight(Star[] stars, int w, int h) {
        // 边界条件检查
        if (stars == null || stars.length == 0 || w <= 0 || h <= 0) {
            return 0;
        }
        
        int n = stars.length;
        
        // 收集所有y坐标用于离散化
        Set<Integer> ySet = new TreeSet<>();
        List<Event> events = new ArrayList<>();
        
        for (Star star : stars) {
            // 检查星星数据合法性
            if (star.x < 0 || star.y < 0 || star.light < 0) {
                throw new IllegalArgumentException("Invalid star data");
            }
            
            // 计算窗口右上角可以放置的矩形区域
            // 窗口右上角在[x1, y1]到[x2, y2]范围内时，星星会被包含在窗口内
            int x1 = star.x;
            int y1 = star.y;
            int x2 = star.x + w - 1; // 窗口宽度为w，边界不算
            int y2 = star.y + h - 1; // 窗口高度为h，边界不算
            
            ySet.add(y1);
            ySet.add(y2);
            
            // 添加开始事件和结束事件
            events.add(new Event(x1, y1, y2, star.light)); // 开始事件：增加亮度
            events.add(new Event(x2 + 1, y1, y2, -star.light)); // 结束事件：减少亮度
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
        int maxLight = 0;
        
        for (Event event : events) {
            // 更新线段树
            int leftIndex = findIndex(event.y1);
            int rightIndex = findIndex(event.y2);
            updateTree(1, leftIndex, rightIndex, event.light);
            
            // 更新最大值
            maxLight = Math.max(maxLight, tree[1].max);
        }
        
        return maxLight;
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
    
    // 更新线段树（区间加值）
    private void updateTree(int node, int left, int right, int value) {
        if (left > tree[node].right || right < tree[node].left) {
            return;
        }
        
        if (left <= tree[node].left && tree[node].right <= right) {
            // 完全包含，更新当前节点
            tree[node].max += value;
            tree[node].lazy += value;
        } else {
            // 下推懒标记
            pushDown(node);
            
            int mid = (tree[node].left + tree[node].right) / 2;
            if (left <= mid) {
                updateTree(node * 2, left, right, value);
            }
            if (right > mid) {
                updateTree(node * 2 + 1, left, right, value);
            }
            
            // 更新当前节点
            tree[node].max = Math.max(tree[node * 2].max, tree[node * 2 + 1].max);
        }
    }
    
    // 下推懒标记
    private void pushDown(int node) {
        if (tree[node].lazy != 0) {
            if (tree[node].left != tree[node].right) {
                tree[node * 2].max += tree[node].lazy;
                tree[node * 2].lazy += tree[node].lazy;
                tree[node * 2 + 1].max += tree[node].lazy;
                tree[node * 2 + 1].lazy += tree[node].lazy;
            }
            tree[node].lazy = 0;
        }
    }
    
    // 在离散化数组中查找索引
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
    
    // 测试用例
    public static void main(String[] args) {
        Code14_WindowStars solution = new Code14_WindowStars();
        
        // 测试用例1: 简单情况
        Star[] stars1 = {
            new Star(1, 1, 10),
            new Star(2, 2, 20),
            new Star(3, 3, 30)
        };
        int result1 = solution.maxWindowLight(stars1, 2, 2);
        System.out.println("测试用例1 最大亮度: " + result1); // 预期: 50 (星星2和3)
        
        // 测试用例2: 窗口包含所有星星
        Star[] stars2 = {
            new Star(1, 1, 5),
            new Star(3, 3, 10),
            new Star(5, 5, 15)
        };
        int result2 = solution.maxWindowLight(stars2, 10, 10);
        System.out.println("测试用例2 最大亮度: " + result2); // 预期: 30 (所有星星)
        
        // 测试用例3: 窗口太小，无法包含任何星星
        Star[] stars3 = {
            new Star(10, 10, 100),
            new Star(20, 20, 200)
        };
        int result3 = solution.maxWindowLight(stars3, 5, 5);
        System.out.println("测试用例3 最大亮度: " + result3); // 预期: 0
        
        // 测试用例4: 星星在边界上
        Star[] stars4 = {
            new Star(0, 0, 50),
            new Star(1, 1, 100),
            new Star(2, 2, 150)
        };
        int result4 = solution.maxWindowLight(stars4, 2, 2);
        System.out.println("测试用例4 最大亮度: " + result4); // 预期: 250 (星星1和2)
        
        // 测试用例5: 大量星星
        Star[] stars5 = new Star[100];
        Random random = new Random();
        for (int i = 0; i < 100; i++) {
            int x = random.nextInt(1000);
            int y = random.nextInt(1000);
            int light = random.nextInt(100) + 1;
            stars5[i] = new Star(x, y, light);
        }
        int result5 = solution.maxWindowLight(stars5, 100, 100);
        System.out.println("测试用例5 最大亮度: " + result5);
        
        // 测试用例6: 空数组
        Star[] stars6 = {};
        int result6 = solution.maxWindowLight(stars6, 10, 10);
        System.out.println("测试用例6 最大亮度: " + result6); // 预期: 0
        
        // 性能测试
        System.out.println("\\n=== 性能测试 ===");
        Star[] stars7 = new Star[1000];
        for (int i = 0; i < 1000; i++) {
            int x = random.nextInt(10000);
            int y = random.nextInt(10000);
            int light = random.nextInt(1000) + 1;
            stars7[i] = new Star(x, y, light);
        }
        
        long startTime = System.currentTimeMillis();
        int result7 = solution.maxWindowLight(stars7, 100, 100);
        long endTime = System.currentTimeMillis();
        System.out.println("性能测试 最大亮度: " + result7);
        System.out.println("执行时间: " + (endTime - startTime) + "ms");
    }
    
    /**
     * 优化版本：使用更高效的数据结构
     */
    public int maxWindowLightOptimized(Star[] stars, int w, int h) {
        if (stars == null || stars.length == 0 || w <= 0 || h <= 0) {
            return 0;
        }
        
        // 使用更紧凑的数据结构
        TreeSet<Integer> ySet = new TreeSet<>();
        List<int[]> events = new ArrayList<>();
        
        for (Star star : stars) {
            int x1 = star.x;
            int y1 = star.y;
            int x2 = star.x + w - 1;
            int y2 = star.y + h - 1;
            
            ySet.add(y1);
            ySet.add(y2);
            
            events.add(new int[]{x1, y1, y2, star.light});
            events.add(new int[]{x2 + 1, y1, y2, -star.light});
        }
        
        // 按x坐标排序
        events.sort((a, b) -> {
            if (a[0] != b[0]) {
                return Integer.compare(a[0], b[0]);
            }
            return Integer.compare(b[3], a[3]);
        });
        
        // 离散化y坐标
        int[] yArr = new int[ySet.size()];
        int idx = 0;
        for (int val : ySet) {
            yArr[idx++] = val;
        }
        
        // 使用数组实现线段树
        int n = yArr.length - 1;
        int[] max = new int[4 * n];
        int[] lazy = new int[4 * n];
        
        int maxLight = 0;
        
        for (int[] event : events) {
            int x = event[0];
            int y1 = event[1];
            int y2 = event[2];
            int light = event[3];
            
            int leftIdx = findIndexOptimized(yArr, y1);
            int rightIdx = findIndexOptimized(yArr, y2);
            
            updateTreeOptimized(max, lazy, yArr, 1, 0, n - 1, leftIdx, rightIdx, light);
            maxLight = Math.max(maxLight, max[1]);
        }
        
        return maxLight;
    }
    
    private void updateTreeOptimized(int[] max, int[] lazy, int[] y, int node, int left, int right, 
                                    int l, int r, int value) {
        if (l > right || r < left) {
            return;
        }
        
        if (l <= left && right <= r) {
            max[node] += value;
            lazy[node] += value;
        } else {
            pushDownOptimized(max, lazy, node);
            int mid = (left + right) / 2;
            if (l <= mid) {
                updateTreeOptimized(max, lazy, y, node * 2, left, mid, l, r, value);
            }
            if (r > mid) {
                updateTreeOptimized(max, lazy, y, node * 2 + 1, mid + 1, right, l, r, value);
            }
            max[node] = Math.max(max[node * 2], max[node * 2 + 1]);
        }
    }
    
    private void pushDownOptimized(int[] max, int[] lazy, int node) {
        if (lazy[node] != 0) {
            max[node * 2] += lazy[node];
            lazy[node * 2] += lazy[node];
            max[node * 2 + 1] += lazy[node];
            lazy[node * 2 + 1] += lazy[node];
            lazy[node] = 0;
        }
    }
    
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
}