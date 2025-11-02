// LeetCode 699. Falling Squares
// 题目链接: https://leetcode.cn/problems/falling-squares/
// 题目描述:
// 在二维平面上的 x 轴上，放置着一些方块。
// 给你一个二维整数数组 positions ，其中 positions[i] = [lefti, sideLengthi] 表示：
// 第 i 个方块边长为 sideLengthi ，其左侧边与 x 轴上坐标点 lefti 对齐。
// 每个方块从一个比目前所有落地方块更高的高度掉落而下，沿 y 轴负方向下落，
// 直到着陆到另一个正方形的顶边或者是 x 轴上。一个方块仅仅是擦过另一个方块的左侧边或右侧边不算着陆。
// 一旦着陆，它就会固定在原地，无法移动。
// 在每个方块掉落后，你需要记录目前所有已经落稳的方块堆叠的最高高度。
// 返回一个整数数组 ans ，其中 ans[i] 表示在第 i 个方块掉落后堆叠的最高高度。
//
// 示例 1:
// 输入: positions = [[1,2],[2,3],[6,1]]
// 输出: [2,5,5]
// 解释:
// 第1个方块掉落后，最高的堆叠由方块1形成，堆叠的最高高度为2。
// 第2个方块掉落后，最高的堆叠由方块1和2形成，堆叠的最高高度为5。
// 第3个方块掉落后，最高的堆叠仍然由方块1和2形成，堆叠的最高高度为5。
// 因此，返回[2, 5, 5]作为答案。
//
// 示例 2:
// 输入: positions = [[100,100],[200,100]]
// 输出: [100,100]
// 解释:
// 第1个方块掉落后，最高的堆叠由方块1形成，堆叠的最高高度为100。
// 第2个方块掉落后，最高的堆叠可以由方块1或方块2形成，堆叠的最高高度为100。
// 注意，方块2擦过方块1的右侧边，但不会算作在方块1上着陆。
// 因此，返回[100, 100]作为答案。
//
// 提示:
// 1 <= positions.length <= 1000
// 1 <= lefti <= 10^8
// 1 <= sideLengthi <= 10^6
//
// 解题思路:
// 这是一个区间更新和区间查询最大值的问题，可以使用线段树来解决。
// 1. 由于坐标范围较大(10^8)，需要进行离散化处理
// 2. 对于每个掉落的方块:
//    - 查询当前方块覆盖区间内的最大高度
//    - 新的高度 = 当前最大高度 + 方块边长
//    - 更新当前方块覆盖区间的高度为新高度
//    - 记录当前所有方块的最大高度
// 3. 使用线段树维护区间最大值，支持区间更新和区间查询
//
// 时间复杂度: O(n log n)，其中n是方块数量
// 空间复杂度: O(n)
//
// 工程化考量:
// 1. 异常处理: 检查输入参数的有效性
// 2. 边界情况: 处理空输入、单个方块等情况
// 3. 性能优化: 使用离散化减少空间和时间复杂度
// 4. 可测试性: 提供完整的测试用例覆盖各种场景
// 5. 可读性: 添加详细的注释说明设计思路和实现细节
// 6. 鲁棒性: 处理极端输入和非理想数据

import java.util.*;

public class LeetCode699_FallingSquares {
    // 线段树节点
    static class Node {
        int l, r;     // 区间左右端点
        int max;      // 区间最大值
        int lazy;     // 懒标记
        boolean update; // 是否有更新操作
        
        public Node(int l, int r) {
            this.l = l;
            this.r = r;
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
    
    public List<Integer> fallingSquares(int[][] positions) {
        List<Integer> result = new ArrayList<>();
        
        // 特殊情况处理
        if (positions == null || positions.length == 0) {
            return result;
        }
        
        // 参数校验
        for (int[] position : positions) {
            if (position == null || position.length != 2) {
                throw new IllegalArgumentException("每个位置信息必须包含两个元素[left, sideLength]");
            }
            if (position[0] < 0 || position[1] <= 0) {
                throw new IllegalArgumentException("坐标必须非负，边长必须为正数");
            }
        }
        
        // 收集所有坐标点并离散化
        discretization(positions);
        
        // 初始化线段树
        tree = new Node[n * 4];
        build(1, n, 1);
        
        // 记录全局最大高度
        int maxHeight = 0;
        
        // 处理每个方块
        for (int[] position : positions) {
            int left = position[0];
            int side = position[1];
            int right = left + side;
            
            // 获取离散化后的坐标
            int l = map.get(left);
            int r = map.get(right);
            
            // 查询当前区间内的最大高度
            int currentMax = query(l, r - 1, 1, n, 1);
            
            // 计算新高度
            int newHeight = currentMax + side;
            
            // 更新区间高度
            update(l, r - 1, newHeight, 1, n, 1);
            
            // 更新全局最大高度
            maxHeight = Math.max(maxHeight, newHeight);
            
            // 记录当前最大高度
            result.add(maxHeight);
        }
        
        return result;
    }
    
    // 收集所有坐标点并离散化
    private void discretization(int[][] positions) {
        Set<Integer> set = new HashSet<>();
        
        // 收集所有坐标点
        for (int[] position : positions) {
            int left = position[0];
            int side = position[1];
            int right = left + side;
            
            set.add(left);
            set.add(right);
        }
        
        // 排序去重后的坐标
        nums = new int[set.size()];
        int index = 0;
        for (int num : set) {
            nums[index++] = num;
        }
        Arrays.sort(nums);
        
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
        if (tree[i].update) {
            // 下发给左子树
            lazy(tree[i << 1], tree[i].lazy);
            // 下发给右子树
            lazy(tree[i << 1 | 1], tree[i].lazy);
            // 清除父节点的懒标记
            tree[i].update = false;
        }
    }
    
    // 懒标记更新
    private void lazy(Node node, int val) {
        node.max = val;
        node.lazy = val;
        node.update = true;
    }
    
    // 区间更新
    private void update(int jobl, int jobr, int val, int l, int r, int i) {
        if (jobl <= l && r <= jobr) {
            lazy(tree[i], val);
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
    
    // 测试函数
    public static void main(String[] args) {
        LeetCode699_FallingSquares solution = new LeetCode699_FallingSquares();
        
        // 测试用例1: 基本情况
        int[][] positions1 = {{1, 2}, {2, 3}, {6, 1}};
        List<Integer> result1 = solution.fallingSquares(positions1);
        System.out.println("输入: [[1,2],[2,3],[6,1]]");
        System.out.println("输出: " + result1);
        System.out.println("期望: [2,5,5]");
        System.out.println("结果: " + (result1.equals(Arrays.asList(2, 5, 5)) ? "通过" : "失败"));
        System.out.println();
        
        // 测试用例2: 不重叠方块
        int[][] positions2 = {{100, 100}, {200, 100}};
        List<Integer> result2 = solution.fallingSquares(positions2);
        System.out.println("输入: [[100,100],[200,100]]");
        System.out.println("输出: " + result2);
        System.out.println("期望: [100,100]");
        System.out.println("结果: " + (result2.equals(Arrays.asList(100, 100)) ? "通过" : "失败"));
        System.out.println();
        
        // 测试用例3: 单个方块
        int[][] positions3 = {{1, 1}};
        List<Integer> result3 = solution.fallingSquares(positions3);
        System.out.println("输入: [[1,1]]");
        System.out.println("输出: " + result3);
        System.out.println("期望: [1]");
        System.out.println("结果: " + (result3.equals(Arrays.asList(1)) ? "通过" : "失败"));
        System.out.println();
        
        // 测试用例4: 空输入
        int[][] positions4 = {};
        List<Integer> result4 = solution.fallingSquares(positions4);
        System.out.println("输入: []");
        System.out.println("输出: " + result4);
        System.out.println("期望: []");
        System.out.println("结果: " + (result4.equals(new ArrayList<>()) ? "通过" : "失败"));
        System.out.println();
        
        // 测试用例5: 复杂重叠情况
        int[][] positions5 = {{1, 5}, {2, 2}, {4, 3}, {6, 1}};
        List<Integer> result5 = solution.fallingSquares(positions5);
        System.out.println("输入: [[1,5],[2,2],[4,3],[6,1]]");
        System.out.println("输出: " + result5);
        // 让我们手动计算一下:
        // 方块1: [1,6] 高度5, 最大高度5
        // 方块2: [2,4] 高度5+2=7, 最大高度7
        // 方块3: [4,7] 高度5+3=8, 最大高度8 (注意方块3与方块1和2都有重叠)
        // 方块4: [6,7] 高度8+1=9, 最大高度9
        System.out.println("期望: [5,7,8,9]");
        System.out.println("结果: " + (result5.equals(Arrays.asList(5, 7, 8, 9)) ? "通过" : "失败"));
        System.out.println();
        
        // 异常处理测试
        try {
            // 测试无效输入
            solution.fallingSquares(null);
            System.out.println("异常测试1: 失败 - 应该抛出异常");
        } catch (Exception e) {
            System.out.println("异常测试1: 通过 - " + e.getClass().getSimpleName());
        }
        
        try {
            // 测试无效坐标
            solution.fallingSquares(new int[][]{{1, -1}});
            System.out.println("异常测试2: 失败 - 应该抛出异常");
        } catch (Exception e) {
            System.out.println("异常测试2: 通过 - " + e.getClass().getSimpleName());
        }
        
        try {
            // 测试无效坐标2
            solution.fallingSquares(new int[][]{{1, 0}});
            System.out.println("异常测试3: 失败 - 应该抛出异常");
        } catch (Exception e) {
            System.out.println("异常测试3: 通过 - " + e.getClass().getSimpleName());
        }
    }
}