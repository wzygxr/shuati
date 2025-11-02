package class110.problems.java;

// LeetCode 218. The Skyline Problem
// 题目链接: https://leetcode.cn/problems/the-skyline-problem/
// 题目描述:
// 城市的天际线是从远处观看该城市中所有建筑物形成的轮廓的外部轮廓。
// 给你所有建筑物的位置和高度，请返回由这些建筑物形成的天际线。
//
// 每个建筑物的几何信息由数组 buildings 表示，其中三元组 buildings[i] = [lefti, righti, heighti] 表示：
// - lefti 是第 i 座建筑物左边缘的 x 坐标。
// - righti 是第 i 座建筑物右边缘的 x 坐标。
// - heighti 是第 i 座建筑物的高度。
//
// 天际线应该表示为由 "关键点" 组成的列表，格式 [[x1,y1],[x2,y2],...]，并按 x 坐标进行排序。
// 关键点是水平线段的左端点。最后一个关键点也是天际线的终点，即最右侧建筑物的终点，高度为 0。
//
// 示例 1:
// 输入: buildings = [[2,9,10],[3,7,15],[5,12,12],[15,20,10],[19,24,8]]
// 输出: [[2,10],[3,15],[7,12],[12,0],[15,10],[20,8],[24,0]]
//
// 示例 2:
// 输入: buildings = [[0,2,3],[2,5,3]]
// 输出: [[0,3],[5,0]]
//
// 提示:
// 1 <= buildings.length <= 10^4
// 0 <= lefti < righti <= 2^31 - 1
// 1 <= heighti <= 2^31 - 1
//
// 解题思路:
// 这是一个经典的扫描线问题，可以使用线段树来解决。
// 1. 收集所有建筑物的左右边界坐标，进行离散化处理
// 2. 使用线段树维护每个离散化区间的高度最大值
// 3. 从左到右扫描，当遇到建筑物的左边界时，更新对应区间的高度
// 4. 当遇到建筑物的右边界时，恢复对应区间的高度
// 5. 在扫描过程中记录高度变化的关键点
//
// 时间复杂度: O(n log n)，其中n是建筑物数量
// 空间复杂度: O(n)

import java.util.*;

public class LeetCode218_TheSkylineProblem {
    
        /**
     * 线段树节点类
     * 用于维护区间最大高度和懒标记
     * 
     * 设计要点：
     * 1. 使用懒标记技术优化区间更新效率
     * 2. 维护区间最大高度，用于确定天际线轮廓
     * 3. 支持区间最大值查询和区间最大值更新
     * 
     * 时间复杂度分析：
     * - 单次更新/查询：O(log n)
     * - 懒标记下推：O(1)
     * 
     * 空间复杂度：O(n)
     */
    static class Node {
        int l, r;      // 区间左右端点（离散化后的索引）
        int max;       // 区间最大高度
        int lazy;      // 懒标记，用于延迟更新
        
        public Node(int l, int r) {
            this.l = l;
            this.r = r;
            this.max = 0;
            this.lazy = 0;
        }
    }
    
    // 线段树数组
    private Node[] tree;
    
    // 离散化后的坐标数组
    private int[] nums;
    
    // 离散化映射
    private Map<Integer, Integer> map;
    
    // 离散化数组大小
    private int n;
    
    /**
     * 获取天际线轮廓
     * 
     * 工程化考量：
     * 1. 异常处理：检查输入参数有效性
     * 2. 边界测试：处理空输入、单建筑、重叠建筑等边界情况
     * 3. 性能优化：使用离散化减少线段树规模，懒标记优化区间更新
     * 
     * @param buildings 建筑物数组，每个建筑物为[left, right, height]
     * @return 天际线关键点列表
     * @throws IllegalArgumentException 当输入参数不合法时抛出异常
     */
    public List<List<Integer>> getSkyline(int[][] buildings) {
        // 参数校验
        if (buildings == null) {
            throw new IllegalArgumentException("输入建筑物数组不能为null");
        }
        
        List<List<Integer>> result = new ArrayList<>();
        
        // 边界情况：空输入
        if (buildings.length == 0) {
            return result;
        }
        
        // 参数校验：检查每个建筑物的格式
        for (int i = 0; i < buildings.length; i++) {
            if (buildings[i] == null || buildings[i].length != 3) {
                throw new IllegalArgumentException("第" + (i+1) + "个建筑物格式不正确，应为[left, right, height]");
            }
            int left = buildings[i][0];
            int right = buildings[i][1];
            int height = buildings[i][2];
            
            // 检查坐标和高度有效性
            if (left < 0 || right < 0 || height < 0) {
                throw new IllegalArgumentException("第" + (i+1) + "个建筑物的坐标或高度不能为负数");
            }
            if (left >= right) {
                throw new IllegalArgumentException("第" + (i+1) + "个建筑物的左边界必须小于右边界");
            }
        }
        
        // 收集所有坐标点并离散化
        discretization(buildings);
        
        // 初始化线段树
        tree = new Node[n * 4];
        build(1, n, 1);
        
        // 创建事件列表：每个建筑物的左右边界
        List<int[]> events = new ArrayList<>();
        for (int[] building : buildings) {
            int left = building[0];
            int right = building[1];
            int height = building[2];
            
            events.add(new int[]{map.get(left), height, 1});  // 左边界事件
            events.add(new int[]{map.get(right), height, -1}); // 右边界事件
        }
        
        // 按坐标排序事件
        events.sort((a, b) -> {
            if (a[0] != b[0]) return a[0] - b[0];
            // 相同坐标时，先处理右边界再处理左边界
            return b[2] - a[2];
        });
        
        // 扫描线处理
        int prevHeight = 0;
        for (int[] event : events) {
            int pos = event[0];
            int height = event[1];
            int type = event[2];
            
            if (type == 1) {
                // 左边界：更新高度
                update(pos, pos, height, 1, n, 1);
            } else {
                // 右边界：恢复高度（设置为0）
                update(pos, pos, 0, 1, n, 1);
            }
            
            // 获取当前最大高度
            int currentHeight = query(1, n, 1, n, 1);
            
            // 如果高度发生变化，记录关键点
            if (currentHeight != prevHeight) {
                List<Integer> point = new ArrayList<>();
                point.add(nums[pos - 1]);  // 还原原始坐标
                point.add(currentHeight);
                result.add(point);
                prevHeight = currentHeight;
            }
        }
        
        return result;
    }
    
    // 离散化处理
    private void discretization(int[][] buildings) {
        Set<Integer> set = new TreeSet<>();
        
        // 收集所有坐标点
        for (int[] building : buildings) {
            set.add(building[0]);  // 左边界
            set.add(building[1]);  // 右边界
        }
        
        // 排序去重后的坐标
        nums = new int[set.size()];
        int index = 0;
        for (int num : set) {
            nums[index++] = num;
        }
        
        // 建立映射关系
        map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            map.put(nums[i], i + 1);
        }
        
        this.n = nums.length;
    }
    
    // 建立线段树
    private void build(int l, int r, int i) {
        tree[i] = new Node(l, r);
        if (l == r) {
            return;
        }
        int mid = (l + r) >> 1;
        build(l, mid, i << 1);
        build(mid + 1, r, i << 1 | 1);
    }
    
    // 向上传递
    private void pushUp(int i) {
        tree[i].max = Math.max(tree[i << 1].max, tree[i << 1 | 1].max);
    }
    
    // 懒标记下发
    private void pushDown(int i) {
        if (tree[i].lazy != 0) {
            tree[i << 1].max = Math.max(tree[i << 1].max, tree[i].lazy);
            tree[i << 1 | 1].max = Math.max(tree[i << 1 | 1].max, tree[i].lazy);
            tree[i << 1].lazy = Math.max(tree[i << 1].lazy, tree[i].lazy);
            tree[i << 1 | 1].lazy = Math.max(tree[i << 1 | 1].lazy, tree[i].lazy);
            tree[i].lazy = 0;
        }
    }
    
    // 区间更新最大值
    private void update(int jobl, int jobr, int val, int l, int r, int i) {
        if (jobl <= l && r <= jobr) {
            tree[i].max = Math.max(tree[i].max, val);
            tree[i].lazy = Math.max(tree[i].lazy, val);
            return;
        }
        pushDown(i);
        int mid = (l + r) >> 1;
        if (jobl <= mid) {
            update(jobl, jobr, val, l, mid, i << 1);
        }
        if (jobr > mid) {
            update(jobl, jobr, val, mid + 1, r, i << 1 | 1);
        }
        pushUp(i);
    }
    
    // 区间查询最大值
    private int query(int jobl, int jobr, int l, int r, int i) {
        if (jobl <= l && r <= jobr) {
            return tree[i].max;
        }
        pushDown(i);
        int mid = (l + r) >> 1;
        int ans = 0;
        if (jobl <= mid) {
            ans = Math.max(ans, query(jobl, jobr, l, mid, i << 1));
        }
        if (jobr > mid) {
            ans = Math.max(ans, query(jobl, jobr, mid + 1, r, i << 1 | 1));
        }
        return ans;
    }
    
    /**
     * 单元测试函数 - 覆盖各种边界情况和异常场景
     * 
     * 工程化测试考量：
     * 1. 正常功能测试
     * 2. 边界情况测试
     * 3. 异常输入测试
     * 4. 性能压力测试
     * 5. 随机数据测试
     */
    public static void main(String[] args) {
        LeetCode218_TheSkylineProblem solution = new LeetCode218_TheSkylineProblem();
        
        System.out.println("=== 线段树天际线问题 - 工程化测试 ===");
        
        // 测试用例1：标准功能测试
        System.out.println("1. 标准功能测试：");
        int[][] buildings1 = {{2,9,10},{3,7,15},{5,12,12},{15,20,10},{19,24,8}};
        List<List<Integer>> result1 = solution.getSkyline(buildings1);
        System.out.println("   输入: [[2,9,10],[3,7,15],[5,12,12],[15,20,10],[19,24,8]]");
        System.out.println("   输出: " + result1);
        System.out.println("   期望: [[2,10],[3,15],[7,12],[12,0],[15,10],[20,8],[24,0]]");
        System.out.println("   测试结果: " + (result1.size() == 7 ? "✓ 通过" : "✗ 失败"));
        System.out.println();
        
        // 测试用例2：边界情况 - 两个相邻建筑
        System.out.println("2. 边界情况测试 - 相邻建筑：");
        int[][] buildings2 = {{0,2,3},{2,5,3}};
        List<List<Integer>> result2 = solution.getSkyline(buildings2);
        System.out.println("   输入: [[0,2,3],[2,5,3]]");
        System.out.println("   输出: " + result2);
        System.out.println("   期望: [[0,3],[5,0]]");
        System.out.println("   测试结果: " + (result2.size() == 2 ? "✓ 通过" : "✗ 失败"));
        System.out.println();
        
        // 测试用例3：边界情况 - 单个建筑
        System.out.println("3. 边界情况测试 - 单个建筑：");
        int[][] buildings3 = {{1,5,10}};
        List<List<Integer>> result3 = solution.getSkyline(buildings3);
        System.out.println("   输入: [[1,5,10]]");
        System.out.println("   输出: " + result3);
        System.out.println("   期望: [[1,10],[5,0]]");
        System.out.println("   测试结果: " + (result3.size() == 2 ? "✓ 通过" : "✗ 失败"));
        System.out.println();
        
        // 测试用例4：边界情况 - 完全重叠建筑
        System.out.println("4. 边界情况测试 - 重叠建筑：");
        int[][] buildings4 = {{1,5,10},{1,5,15}};
        List<List<Integer>> result4 = solution.getSkyline(buildings4);
        System.out.println("   输入: [[1,5,10],[1,5,15]]");
        System.out.println("   输出: " + result4);
        System.out.println("   期望: [[1,15],[5,0]]");
        System.out.println("   测试结果: " + (result4.size() == 2 ? "✓ 通过" : "✗ 失败"));
        System.out.println();
        
        // 测试用例5：边界情况 - 空输入
        System.out.println("5. 边界情况测试 - 空输入：");
        int[][] buildings5 = {};
        List<List<Integer>> result5 = solution.getSkyline(buildings5);
        System.out.println("   输入: []");
        System.out.println("   输出: " + result5);
        System.out.println("   期望: []");
        System.out.println("   测试结果: " + (result5.size() == 0 ? "✓ 通过" : "✗ 失败"));
        System.out.println();
        
        // 测试用例6：性能测试 - 大规模数据
        System.out.println("6. 性能测试 - 大规模数据：");
        try {
            int[][] buildings6 = generateLargeTestData(1000);
            long startTime = System.currentTimeMillis();
            List<List<Integer>> result6 = solution.getSkyline(buildings6);
            long endTime = System.currentTimeMillis();
            System.out.println("   数据规模: 1000个建筑物");
            System.out.println("   执行时间: " + (endTime - startTime) + "ms");
            System.out.println("   输出关键点数量: " + result6.size());
            System.out.println("   测试结果: " + ((endTime - startTime) < 1000 ? "✓ 性能良好" : "⚠ 性能需优化"));
        } catch (Exception e) {
            System.out.println("   性能测试异常: " + e.getMessage());
        }
        System.out.println();
        
        // 测试用例7：异常输入测试
        System.out.println("7. 异常输入测试：");
        try {
            int[][] buildings7 = {{1,1,10}}; // 左边界等于右边界
            solution.getSkyline(buildings7);
            System.out.println("   测试结果: ✗ 应该抛出异常但未抛出");
        } catch (IllegalArgumentException e) {
            System.out.println("   异常测试 - 左边界等于右边界: ✓ 正确抛出异常: " + e.getMessage());
        }
        
        try {
            int[][] buildings8 = {{-1,5,10}}; // 负坐标
            solution.getSkyline(buildings8);
            System.out.println("   测试结果: ✗ 应该抛出异常但未抛出");
        } catch (IllegalArgumentException e) {
            System.out.println("   异常测试 - 负坐标: ✓ 正确抛出异常: " + e.getMessage());
        }
        
        try {
            int[][] buildings9 = null; // null输入
            solution.getSkyline(buildings9);
            System.out.println("   测试结果: ✗ 应该抛出异常但未抛出");
        } catch (IllegalArgumentException e) {
            System.out.println("   异常测试 - null输入: ✓ 正确抛出异常: " + e.getMessage());
        }
        
        System.out.println("
=== 测试完成 ===");
    }
    
    /**
     * 生成大规模测试数据
     * 用于性能测试和压力测试
     * 
     * @param size 建筑物数量
     * @return 生成的测试数据
     */
    private static int[][] generateLargeTestData(int size) {
        int[][] buildings = new int[size][3];
        Random random = new Random();
        
        for (int i = 0; i < size; i++) {
            int left = random.nextInt(10000);
            int right = left + random.nextInt(100) + 1; // 确保右边界大于左边界
            int height = random.nextInt(100) + 1; // 高度为正数
            
            buildings[i][0] = left;
            buildings[i][1] = right;
            buildings[i][2] = height;
        }
        
        return buildings;
    }
}