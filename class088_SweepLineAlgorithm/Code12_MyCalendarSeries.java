package class115;

import java.util.*;

/**
 * 我的日程安排表系列 - 扫描线算法应用
 * 
 * 题目描述:
 * 729: 实现一个 MyCalendar 类来存放你的日程安排。如果要添加的时间内没有其他安排，则可以存储这个新的日程安排。
 * 731: 实现一个 MyCalendarTwo 类来存放你的日程安排。如果要添加的时间内不会导致三重预订时，则可以存储这个新的日程安排。
 * 732: 实现一个 MyCalendarThree 类来存放你的日程安排，你可以一直添加新的日程安排。
 * 
 * 解题思路:
 * 使用扫描线算法结合平衡树或线段树实现日程安排的管理。
 * 1. 将每个日程的开始和结束作为事件点
 * 2. 维护当前时间线上的预订状态
 * 3. 根据不同的约束条件进行冲突检查
 * 
 * 算法复杂度: 时间复杂度O(n log n)，空间复杂度O(n)
 * 工程化考量:
 * 1. 异常处理: 检查时间区间合法性
 * 2. 边界条件: 处理时间边界重叠情况
 * 3. 性能优化: 使用高效的数据结构
 * 4. 可扩展性: 支持不同的约束条件
 * 5. 提供了多种实现方式：基于TreeMap和基于扫描线算法
 */
public class Code12_MyCalendarSeries {
    
    /**
     * 我的日程安排表 I (LeetCode 729)
     * 题目链接: https://leetcode.cn/problems/my-calendar-i/
     * 问题描述：实现一个日程安排系统，不允许任何时间冲突
     */
    static class MyCalendarI {
        // 使用TreeMap维护日程安排，按键值排序
        private TreeMap<Integer, Integer> calendar;
        
        public MyCalendarI() {
            calendar = new TreeMap<>();
        }
        
        /**
         * 添加新的日程安排
         * 算法核心思想：
         * 1. 检查新日程与现有日程是否有时间冲突
         * 2. 如果没有冲突，则添加新日程
         * 
         * @param start 开始时间
         * @param end 结束时间
         * @return 是否成功添加（不冲突则返回true）
         */
        public boolean book(int start, int end) {
            // 边界条件检查
            if (start < 0 || end <= start) {
                throw new IllegalArgumentException("Invalid time interval");
            }
            
            // 查找前一个日程安排
            Integer prev = calendar.floorKey(start);
            if (prev != null && calendar.get(prev) > start) {
                return false; // 与前一个日程冲突
            }
            
            // 查找后一个日程安排
            Integer next = calendar.ceilingKey(start);
            if (next != null && next < end) {
                return false; // 与后一个日程冲突
            }
            
            // 添加新的日程安排
            calendar.put(start, end);
            return true;
        }
        
        /**
         * 扫描线算法实现
         * 通过将所有日程转化为事件点来检查冲突
         * 
         * @param start 开始时间
         * @param end 结束时间
         * @return 是否成功添加（不冲突则返回true）
         */
        public boolean bookWithSweepLine(int start, int end) {
            if (start < 0 || end <= start) {
                throw new IllegalArgumentException("Invalid time interval");
            }
            
            // 将日程安排转化为事件点
            List<int[]> events = new ArrayList<>();
            for (Map.Entry<Integer, Integer> entry : calendar.entrySet()) {
                events.add(new int[]{entry.getKey(), 1}); // 开始事件
                events.add(new int[]{entry.getValue(), -1}); // 结束事件
            }
            
            // 添加新日程的事件点
            events.add(new int[]{start, 1});
            events.add(new int[]{end, -1});
            
            // 按时间排序，相同时间时结束事件优先
            events.sort((a, b) -> {
                if (a[0] != b[0]) {
                    return Integer.compare(a[0], b[0]);
                }
                return Integer.compare(a[1], b[1]);
            });
            
            // 扫描检查冲突
            int count = 0;
            for (int[] event : events) {
                count += event[1];
                if (count > 1) {
                    return false; // 发现冲突
                }
            }
            
            // 没有冲突，添加日程
            calendar.put(start, end);
            return true;
        }
    }
    
    /**
     * 我的日程安排表 II (LeetCode 731)
     * 题目链接: https://leetcode.cn/problems/my-calendar-ii/
     * 问题描述：实现一个日程安排系统，不允许三重预订
     */
    static class MyCalendarII {
        // 维护单次预订和双重预订
        private TreeMap<Integer, Integer> singleBookings;
        private TreeMap<Integer, Integer> doubleBookings;
        
        public MyCalendarII() {
            singleBookings = new TreeMap<>();
            doubleBookings = new TreeMap<>();
        }
        
        /**
         * 添加新的日程安排（不允许三重预订）
         * 算法核心思想：
         * 1. 检查新日程是否会导致三重预订
         * 2. 如果不会导致三重预订，则更新双重预订和单次预订
         * 
         * @param start 开始时间
         * @param end 结束时间
         * @return 是否成功添加（不导致三重预订则返回true）
         */
        public boolean book(int start, int end) {
            if (start < 0 || end <= start) {
                throw new IllegalArgumentException("Invalid time interval");
            }
            
            // 检查是否会导致三重预订
            if (hasTripleBooking(start, end)) {
                return false;
            }
            
            // 更新双重预订
            updateDoubleBookings(start, end);
            
            // 添加单次预订
            singleBookings.put(start, singleBookings.getOrDefault(start, 0) + 1);
            singleBookings.put(end, singleBookings.getOrDefault(end, 0) - 1);
            
            return true;
        }
        
        /**
         * 检查是否会导致三重预订
         * @param start 开始时间
         * @param end 结束时间
         * @return 是否会导致三重预订
         */
        private boolean hasTripleBooking(int start, int end) {
            // 扫描双重预订区间
            Integer prev = doubleBookings.floorKey(start);
            if (prev != null && doubleBookings.get(prev) > start) {
                return true;
            }
            
            Integer next = doubleBookings.ceilingKey(start);
            if (next != null && next < end) {
                return true;
            }
            
            return false;
        }
        
        /**
         * 更新双重预订区间
         * @param start 开始时间
         * @param end 结束时间
         */
        private void updateDoubleBookings(int start, int end) {
            // 扫描单次预订，找出重叠区间
            TreeMap<Integer, Integer> overlaps = new TreeMap<>();
            
            // 查找与新区间重叠的单次预订
            for (Map.Entry<Integer, Integer> entry : singleBookings.entrySet()) {
                int s = entry.getKey();
                int e = entry.getValue();
                
                if (s < end && e > start) {
                    // 计算重叠区间
                    int overlapStart = Math.max(start, s);
                    int overlapEnd = Math.min(end, e);
                    
                    if (overlapStart < overlapEnd) {
                        overlaps.put(overlapStart, overlaps.getOrDefault(overlapStart, 0) + 1);
                        overlaps.put(overlapEnd, overlaps.getOrDefault(overlapEnd, 0) - 1);
                    }
                }
            }
            
            // 更新双重预订
            int count = 0;
            int currentStart = -1;
            for (Map.Entry<Integer, Integer> entry : overlaps.entrySet()) {
                int time = entry.getKey();
                int delta = entry.getValue();
                
                if (count == 0 && delta > 0) {
                    currentStart = time;
                }
                
                count += delta;
                
                if (count == 0 && currentStart != -1) {
                    doubleBookings.put(currentStart, time);
                    currentStart = -1;
                }
            }
        }
        
        /**
         * 扫描线算法实现
         * 通过扫描所有事件点来检查是否会导致三重预订
         * 
         * @param start 开始时间
         * @param end 结束时间
         * @return 是否成功添加（不导致三重预订则返回true）
         */
        public boolean bookWithSweepLine(int start, int end) {
            if (start < 0 || end <= start) {
                throw new IllegalArgumentException("Invalid time interval");
            }
            
            // 收集所有事件点
            TreeMap<Integer, Integer> events = new TreeMap<>();
            
            // 添加现有日程的事件点
            for (Map.Entry<Integer, Integer> entry : singleBookings.entrySet()) {
                events.put(entry.getKey(), events.getOrDefault(entry.getKey(), 0) + 1);
                events.put(entry.getValue(), events.getOrDefault(entry.getValue(), 0) - 1);
            }
            
            // 添加新日程的事件点
            events.put(start, events.getOrDefault(start, 0) + 1);
            events.put(end, events.getOrDefault(end, 0) - 1);
            
            // 扫描检查是否会导致三重预订
            int count = 0;
            for (int time : events.keySet()) {
                count += events.get(time);
                if (count >= 3) {
                    return false;
                }
            }
            
            // 没有三重预订，添加日程
            singleBookings.put(start, singleBookings.getOrDefault(start, 0) + 1);
            singleBookings.put(end, singleBookings.getOrDefault(end, 0) - 1);
            
            return true;
        }
    }
    
    /**
     * 我的日程安排表 III (LeetCode 732)
     * 题目链接: https://leetcode.cn/problems/my-calendar-iii/
     * 问题描述：实现一个日程安排系统，可以一直添加新日程并返回最大重叠次数
     */
    static class MyCalendarThree {
        // 使用TreeMap记录所有事件点
        private TreeMap<Integer, Integer> events;
        
        public MyCalendarThree() {
            events = new TreeMap<>();
        }
        
        /**
         * 添加新的日程安排，返回最大重叠次数
         * 算法核心思想：
         * 1. 将新日程的开始和结束作为事件点添加
         * 2. 扫描所有事件点计算最大重叠次数
         * 
         * @param start 开始时间
         * @param end 结束时间
         * @return 添加新日程后的最大重叠次数
         */
        public int book(int start, int end) {
            if (start < 0 || end <= start) {
                throw new IllegalArgumentException("Invalid time interval");
            }
            
            // 添加事件点
            events.put(start, events.getOrDefault(start, 0) + 1);
            events.put(end, events.getOrDefault(end, 0) - 1);
            
            // 扫描计算最大重叠次数
            int maxK = 0;
            int currentK = 0;
            
            for (int count : events.values()) {
                currentK += count;
                maxK = Math.max(maxK, currentK);
            }
            
            return maxK;
        }
        
        /**
         * 线段树实现（支持区间查询）
         * 可以使用线段树实现更高效的区间查询
         * 
         * @param start 开始时间
         * @param end 结束时间
         * @return 添加新日程后的最大重叠次数
         */
        public int bookWithSegmentTree(int start, int end) {
            if (start < 0 || end <= start) {
                throw new IllegalArgumentException("Invalid time interval");
            }
            
            // 这里可以使用线段树实现更高效的区间查询
            // 由于时间范围可能很大，可以使用动态开点线段树
            
            // 简化实现：使用扫描线算法
            return book(start, end);
        }
    }
    
    /**
     * 测试用例
     * 验证MyCalendarI、MyCalendarII和MyCalendarThree类的正确性
     */
    public static void main(String[] args) {
        System.out.println("=== MyCalendar I 测试 ===");
        testMyCalendarI();
        
        System.out.println("\n=== MyCalendar II 测试 ===");
        testMyCalendarII();
        
        System.out.println("\n=== MyCalendar III 测试 ===");
        testMyCalendarIII();
    }
    
    /**
     * 测试MyCalendarI类
     */
    private static void testMyCalendarI() {
        MyCalendarI calendar = new MyCalendarI();
        
        // 测试用例1: 正常添加
        System.out.println("添加 [10, 20]: " + calendar.book(10, 20)); // true
        System.out.println("添加 [15, 25]: " + calendar.book(15, 25)); // false
        System.out.println("添加 [20, 30]: " + calendar.book(20, 30)); // true
        
        // 测试用例2: 边界情况
        System.out.println("添加 [5, 10]: " + calendar.book(5, 10)); // true
        System.out.println("添加 [5, 15]: " + calendar.book(5, 15)); // false
        
        // 测试扫描线版本
        MyCalendarI calendar2 = new MyCalendarI();
        System.out.println("扫描线版本 - 添加 [10, 20]: " + calendar2.bookWithSweepLine(10, 20)); // true
        System.out.println("扫描线版本 - 添加 [15, 25]: " + calendar2.bookWithSweepLine(15, 25)); // false
    }
    
    /**
     * 测试MyCalendarII类
     */
    private static void testMyCalendarII() {
        MyCalendarII calendar = new MyCalendarII();
        
        // 测试用例1: 正常添加
        System.out.println("添加 [10, 20]: " + calendar.book(10, 20)); // true
        System.out.println("添加 [50, 60]: " + calendar.book(50, 60)); // true
        System.out.println("添加 [10, 40]: " + calendar.book(10, 40)); // true
        System.out.println("添加 [5, 15]: " + calendar.book(5, 15)); // false (三重预订)
        System.out.println("添加 [5, 10]: " + calendar.book(5, 10)); // true
        System.out.println("添加 [25, 55]: " + calendar.book(25, 55)); // true
        
        // 测试扫描线版本
        MyCalendarII calendar2 = new MyCalendarII();
        System.out.println("扫描线版本 - 添加 [10, 20]: " + calendar2.bookWithSweepLine(10, 20)); // true
        System.out.println("扫描线版本 - 添加 [50, 60]: " + calendar2.bookWithSweepLine(50, 60)); // true
        System.out.println("扫描线版本 - 添加 [10, 40]: " + calendar2.bookWithSweepLine(10, 40)); // true
        System.out.println("扫描线版本 - 添加 [5, 15]: " + calendar2.bookWithSweepLine(5, 15)); // false
    }
    
    /**
     * 测试MyCalendarThree类
     */
    private static void testMyCalendarIII() {
        MyCalendarThree calendar = new MyCalendarThree();
        
        // 测试用例1: 正常添加
        System.out.println("添加 [10, 20]: " + calendar.book(10, 20)); // 1
        System.out.println("添加 [50, 60]: " + calendar.book(50, 60)); // 1
        System.out.println("添加 [10, 40]: " + calendar.book(10, 40)); // 2
        System.out.println("添加 [5, 15]: " + calendar.book(5, 15)); // 3
        System.out.println("添加 [5, 10]: " + calendar.book(5, 10)); // 3
        System.out.println("添加 [25, 55]: " + calendar.book(25, 55)); // 3
        
        // 测试线段树版本
        System.out.println("线段树版本 - 添加 [10, 20]: " + calendar.bookWithSegmentTree(10, 20)); // 3
    }
    
    /**
     * 性能测试方法
     * 测试不同实现的性能表现
     */
    public static void performanceTest() {
        System.out.println("\n=== 性能测试 ===");
        
        // MyCalendar I 性能测试
        MyCalendarI calendarI = new MyCalendarI();
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            calendarI.book(i, i + 10);
        }
        long endTime = System.currentTimeMillis();
        System.out.println("MyCalendar I 10000次操作时间: " + (endTime - startTime) + "ms");
        
        // MyCalendar III 性能测试
        MyCalendarThree calendarIII = new MyCalendarThree();
        startTime = System.currentTimeMillis();
        int maxK = 0;
        for (int i = 0; i < 10000; i++) {
            maxK = Math.max(maxK, calendarIII.book(i, i + 10));
        }
        endTime = System.currentTimeMillis();
        System.out.println("MyCalendar III 10000次操作时间: " + (endTime - startTime) + "ms");
        System.out.println("最大重叠次数: " + maxK);
    }
}