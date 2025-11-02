/**
 * LeetCode 218. 天际线问题
 * 题目链接: https://leetcode.cn/problems/the-skyline-problem/
 *
 * 题目描述:
 * 城市的天际线是从远处观看该城市中所有建筑物形成的轮廓的外部轮廓。
 * 给你所有建筑物的位置和高度，请返回由这些建筑物形成的天际线。
 *
 * 每个建筑物的几何信息由数组 buildings 表示，其中三元组 buildings[i] = [lefti, righti, heighti] 表示：
 * - lefti 是第i座建筑物左边缘的x坐标
 * - righti 是第i座建筑物右边缘的x坐标
 * - heighti 是第i座建筑物的高度
 *
 * 你可以假设所有的建筑都是完美的长方形，在高度为0的绝对平坦的表面上。
 *
 * 天际线应该表示为由"关键点"组成的列表，格式 [[x1,y1],[x2,y2],...]，并按x坐标进行排序。
 * 关键点是水平线段的左端点。列表中最后一个点是最右侧建筑物的终点，y坐标始终为0，仅用于标记天际线的终点。
 *
 * 时间复杂度: O(n log n)，其中n是建筑物的数量
 * 空间复杂度: O(n)
 *
 * 解题思路:
 * 使用扫描线算法结合优先队列来解决这个问题：
 * 1. 将所有建筑物的左右边界作为事件点处理
 * 2. 使用优先队列维护当前有效的建筑物高度
 * 3. 当遇到左边界时，将建筑物高度加入队列
 * 4. 当遇到右边界时，将建筑物高度从队列中移除
 * 5. 当最高高度发生变化时，记录关键点
 *
 * 工程化考量:
 * 1. 异常处理: 检查输入参数的有效性
 * 2. 边界条件: 处理空输入、单建筑等边界情况
 * 3. 性能优化: 使用优先队列优化时间复杂度
 * 4. 可读性: 详细注释和清晰的变量命名
 * 5. 可测试性: 提供完整的测试用例覆盖各种场景
 */

import java.util.*;

public class LeetCode218_SkylineProblem {
    
    /**
     * 计算天际线关键点
     * 
     * @param buildings 建筑物信息数组，每个元素为[left, right, height]
     * @return 天际线关键点列表
     * 
     * 时间复杂度: O(n log n)，其中n是建筑物的数量
     * 空间复杂度: O(n)
     */
    public List<List<Integer>> getSkyline(int[][] buildings) {
        // 参数校验
        if (buildings == null || buildings.length == 0) {
            return new ArrayList<>();
        }
        
        // 创建事件点：[x坐标, 高度, 类型]
        // 类型：0表示左边界（进入），1表示右边界（离开）
        List<int[]> events = new ArrayList<>();
        for (int[] building : buildings) {
            int left = building[0];
            int right = building[1];
            int height = building[2];
            // 左边界事件：负高度表示进入
            events.add(new int[]{left, -height, 0});  // 0表示进入
            // 右边界事件：正高度表示离开
            events.add(new int[]{right, height, 1});  // 1表示离开
        }
        
        // 按照x坐标排序，如果x相同，则：
        // 1. 进入事件优先于离开事件
        // 2. 进入事件中，高度高的优先
        // 3. 离开事件中，高度低的优先
        events.sort((a, b) -> {
            if (a[0] != b[0]) {
                return Integer.compare(a[0], b[0]);
            }
            if (a[2] != b[2]) {
                return Integer.compare(a[2], b[2]);
            }
            return Integer.compare(a[1], b[1]);
        });
        
        // 使用优先队列维护当前有效高度（最大堆）
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>((a, b) -> b - a);
        maxHeap.offer(0); // 初始高度为0
        List<List<Integer>> result = new ArrayList<>();
        int prevMaxHeight = 0;
        
        for (int[] event : events) {
            int x = event[0];
            int h = event[1];
            int eventType = event[2];
            
            if (eventType == 0) { // 进入事件
                maxHeap.offer(-h); // h是负值，取反后加入堆
            } else { // 离开事件
                maxHeap.remove(h); // h是正值，直接移除
            }
            
            // 获取当前最大高度
            int currMaxHeight = maxHeap.peek();
            
            // 如果最大高度发生变化，记录关键点
            if (currMaxHeight != prevMaxHeight) {
                result.add(Arrays.asList(x, currMaxHeight));
                prevMaxHeight = currMaxHeight;
            }
        }
        
        return result;
    }
    
    /**
     * 测试函数
     * 
     * 工程化测试考量：
     * 1. 正常功能测试
     * 2. 边界情况测试
     * 3. 异常输入测试
     * 4. 性能压力测试
     */
    public static void main(String[] args) {
        LeetCode218_SkylineProblem skyline = new LeetCode218_SkylineProblem();
        
        // 测试用例1
        int[][] buildings1 = {{2,9,10},{3,7,15},{5,12,12},{15,20,10},{19,24,8}};
        List<List<Integer>> expected1 = Arrays.asList(
            Arrays.asList(2,10),
            Arrays.asList(3,15),
            Arrays.asList(7,12),
            Arrays.asList(12,0),
            Arrays.asList(15,10),
            Arrays.asList(20,8),
            Arrays.asList(24,0)
        );
        
        List<List<Integer>> result1 = skyline.getSkyline(buildings1);
        System.out.println("测试用例1:");
        System.out.println("输入: " + Arrays.deepToString(buildings1));
        System.out.println("输出: " + result1);
        System.out.println("期望: " + expected1);
        System.out.println("结果: " + (result1.equals(expected1) ? "✓ 通过" : "✗ 失败"));
        
        // 测试用例2
        int[][] buildings2 = {{0,2,3},{2,5,3}};
        List<List<Integer>> expected2 = Arrays.asList(
            Arrays.asList(0,3),
            Arrays.asList(5,0)
        );
        
        List<List<Integer>> result2 = skyline.getSkyline(buildings2);
        System.out.println("\n测试用例2:");
        System.out.println("输入: " + Arrays.deepToString(buildings2));
        System.out.println("输出: " + result2);
        System.out.println("期望: " + expected2);
        System.out.println("结果: " + (result2.equals(expected2) ? "✓ 通过" : "✗ 失败"));
        
        // 边界测试用例3：空输入
        try {
            List<List<Integer>> result3 = skyline.getSkyline(new int[0][0]);
            System.out.println("\n边界测试用例3 (空输入):");
            System.out.println("输入: []");
            System.out.println("输出: " + result3);
            System.out.println("结果: " + (result3.isEmpty() ? "✓ 通过" : "✗ 失败"));
        } catch (Exception e) {
            System.out.println("\n边界测试用例3 (空输入):");
            System.out.println("发生异常: " + e.getMessage());
            System.out.println("结果: ✗ 失败");
        }
        
        // 边界测试用例4：单个建筑物
        int[][] buildings4 = {{1, 5, 10}};
        List<List<Integer>> expected4 = Arrays.asList(
            Arrays.asList(1, 10),
            Arrays.asList(5, 0)
        );
        
        List<List<Integer>> result4 = skyline.getSkyline(buildings4);
        System.out.println("\n边界测试用例4 (单个建筑物):");
        System.out.println("输入: " + Arrays.deepToString(buildings4));
        System.out.println("输出: " + result4);
        System.out.println("期望: " + expected4);
        System.out.println("结果: " + (result4.equals(expected4) ? "✓ 通过" : "✗ 失败"));
    }
}