package class185.sweep_line_problems;

import java.util.*;

/**
 * LeetCode 218. 天际线问题 (The Skyline Problem)
 * 
 * 题目来源：https://leetcode.cn/problems/the-skyline-problem/
 * 
 * 题目描述：
 * 城市的天际线是从远处观看该城市中所有建筑物形成的轮廓的外部轮廓。
 * 现在，假设您获得了城市中所有建筑物的位置和高度，请编写一个程序来输出由这些建筑物形成的天际线。
 * 每个建筑物的几何信息用三元组 [Li, Ri, Hi] 表示，其中 Li 和 Ri 分别是第 i 座建筑物左右边缘的 x 坐标，
 * Hi 是其高度。可以保证 0 ≤ Li, Ri ≤ INT_MAX, 0 < Hi ≤ INT_MAX 和 Ri - Li > 0。
 * 您可以假设所有建筑物都是在绝对平坦且高度为 0 的表面上的完美矩形。
 * 
 * 例如，图 A 中所有建筑物的尺寸记录为：[ [2 9 10], [3 7 15], [5 12 12], [15 20 10], [19 24 8] ]。
 * 输出是以 [ [x1,y1], [x2, y2], [x3, y3], ... ] 格式的“关键点”的列表，
 * 按 x 坐标进行排序。关键点是水平线段的左端点。最后一个点的 y 值始终为 0，
 * 仅用于标记天际线的终点。此外，任何两个相邻建筑物之间的地面都应被视为天际线轮廓的一部分。
 * 
 * 注意：输出天际线中不得有连续的相同高度的水平线。
 * 例如 [...[2 3], [4 5], [7 5], [11 5], [12 7]...] 是不正确的答案；
 * 三条高度为 5 的线应该在最终输出中合并为一个：[...[2 3], [4 5], [12 7], ...]
 * 
 * 示例 1：
 * 输入：buildings = [[2,9,10],[3,7,15],[5,12,12],[15,20,10],[19,24,8]]
 * 输出：[[2,10],[3,15],[7,12],[12,0],[15,10],[20,8],[24,0]]
 * 
 * 示例 2：
 * 输入：buildings = [[0,2,3],[2,5,3]]
 * 输出：[[0,3],[5,0]]
 * 
 * 提示：
 * 1 <= buildings.length <= 10^4
 * 0 <= lefti < righti <= 2^31 - 1
 * 1 <= heighti <= 2^31 - 1
 * buildings 按 lefti 非递减排序
 * 
 * 解题思路：
 * 使用扫描线算法解决天际线问题。核心思想是：
 * 1. 将每个建筑物的左右边界转换为事件点
 * 2. 对所有事件点按x坐标排序
 * 3. 使用优先队列维护当前活跃建筑物的高度
 * 4. 扫描所有事件点，更新天际线关键点
 * 
 * 时间复杂度：O(n log n)，其中 n 是建筑物的数量
 * 空间复杂度：O(n)
 * 
 * 相关题目：
 * - LeetCode 850. 矩形面积II
 * - LeetCode 391. 完美矩形
 */
public class LeetCode_218_TheSkylineProblem {
    
    /**
     * 天际线问题的扫描线解法
     * @param buildings 建筑物数组，每个建筑物是 [left, right, height] 形式
     * @return 天际线关键点列表
     */
    public static List<List<Integer>> getSkyline(int[][] buildings) {
        List<List<Integer>> result = new ArrayList<>();
        
        // 创建事件点列表：[x坐标, 高度变化]
        // 高度为负数表示建筑物开始，正数表示建筑物结束
        List<int[]> events = new ArrayList<>();
        
        for (int[] building : buildings) {
            int left = building[0];
            int right = building[1];
            int height = building[2];
            
            // 添加建筑物开始事件（高度为负数）
            events.add(new int[]{left, -height});
            // 添加建筑物结束事件（高度为正数）
            events.add(new int[]{right, height});
        }
        
        // 按照x坐标排序事件点
        // 如果x坐标相同，按照高度排序（开始事件优先于结束事件）
        events.sort((a, b) -> {
            if (a[0] != b[0]) {
                return Integer.compare(a[0], b[0]);
            }
            return Integer.compare(a[1], b[1]);
        });
        
        // 使用 TreeMap 维护当前活跃建筑物的高度及其计数
        // TreeMap 可以自动排序，便于获取最大高度
        TreeMap<Integer, Integer> heightMap = new TreeMap<>();
        // 初始高度为0
        heightMap.put(0, 1);
        
        int prevHeight = 0;
        
        // 扫描所有事件点
        for (int[] event : events) {
            int x = event[0];
            int height = event[1];
            
            if (height < 0) {
                // 建筑物开始事件
                int h = -height;
                heightMap.put(h, heightMap.getOrDefault(h, 0) + 1);
            } else {
                // 建筑物结束事件
                heightMap.put(height, heightMap.get(height) - 1);
                if (heightMap.get(height) == 0) {
                    heightMap.remove(height);
                }
            }
            
            // 获取当前最大高度
            int currentHeight = heightMap.lastKey();
            
            // 如果高度发生变化，添加关键点
            if (currentHeight != prevHeight) {
                result.add(Arrays.asList(x, currentHeight));
                prevHeight = currentHeight;
            }
        }
        
        return result;
    }
    
    /**
     * 测试天际线问题解法
     */
    public static void main(String[] args) {
        System.out.println("=== LeetCode 218. 天际线问题 ===");
        
        // 测试用例1
        System.out.println("测试用例1:");
        int[][] buildings1 = {
            {2, 9, 10}, {3, 7, 15}, {5, 12, 12}, {15, 20, 10}, {19, 24, 8}
        };
        List<List<Integer>> result1 = getSkyline(buildings1);
        System.out.println("输入: " + Arrays.deepToString(buildings1));
        System.out.println("输出: " + result1);
        System.out.println("期望: [[2,10],[3,15],[7,12],[12,0],[15,10],[20,8],[24,0]]");
        System.out.println();
        
        // 测试用例2
        System.out.println("测试用例2:");
        int[][] buildings2 = {
            {0, 2, 3}, {2, 5, 3}
        };
        List<List<Integer>> result2 = getSkyline(buildings2);
        System.out.println("输入: " + Arrays.deepToString(buildings2));
        System.out.println("输出: " + result2);
        System.out.println("期望: [[0,3],[5,0]]");
        System.out.println();
        
        // 性能测试
        System.out.println("=== 性能测试 ===");
        Random random = new Random(42);
        int n = 10000;
        int[][] buildings = new int[n][3];
        
        for (int i = 0; i < n; i++) {
            int left = random.nextInt(1000000);
            int right = left + random.nextInt(10000) + 1;
            int height = random.nextInt(1000000) + 1;
            buildings[i][0] = left;
            buildings[i][1] = right;
            buildings[i][2] = height;
        }
        
        long startTime = System.nanoTime();
        List<List<Integer>> result = getSkyline(buildings);
        long endTime = System.nanoTime();
        
        System.out.println("10000个建筑物的天际线计算完成");
        System.out.println("关键点数量: " + result.size());
        System.out.println("运行时间: " + (endTime - startTime) / 1_000_000.0 + " ms");
    }
}